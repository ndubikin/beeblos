package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.ALL;
import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;
import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;
import static org.beeblos.bpm.core.util.Constants.FIRST_WPROCESSDEF_VERSION;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessDefDao;
import org.beeblos.bpm.core.error.WDataTypeException;
import org.beeblos.bpm.core.error.WProcessDataFieldException;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessHeadException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepHeadException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessHead;
import org.beeblos.bpm.core.model.WProcessHeadManagedDataConfiguration;
import org.beeblos.bpm.core.model.WProcessRole;
import org.beeblos.bpm.core.model.WProcessUser;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;
import org.beeblos.bpm.tm.exception.TableManagerException;

import com.sp.common.util.IntegerPair;
import com.sp.common.util.StringPair;



public class WProcessDefBL {
	
	private static final Log logger = LogFactory.getLog(WProcessDefBL.class.getName());
	
	private static final String _ROOT_MANAGED_TABLE_NAME = "wmt_";
	
	public WProcessDefBL (){
		
	}
	
	public Integer add(WProcessDef process, Integer currentUserId) 
			throws WProcessDefException, WProcessHeadException, WStepSequenceDefException {
		
		logger.debug("add() WProcessDef - Name: ["+process.getName()+"]");
		
		// dml 20130430 - si es un nuevo WProcessHead se guarda antes de guardar el WProcessDef y se rellena la informacion esencial
		if (process.getProcess() != null
				&& (process.getProcess().getId() == null
				|| process.getProcess().getId().equals(0))){
			
			Integer processHeadId = new WProcessHeadBL().add(process.getProcess(), currentUserId);
			
			this._setFirstWProcessDefData(process, processHeadId, currentUserId);
						
		}
		
		// timestamp & trace info
		process.setInsertDate(new Date());
		process.setModDate( DEFAULT_MOD_DATE );
		process.setInsertUser(currentUserId);
		process.setModUser(currentUserId);
		Integer newProcessId = new WProcessDefDao().add(process, currentUserId);
		process.setId(newProcessId);
		
		// dml 20130508 - a un nuevo proceso creado tenemos que asociarle un mapa vacío con su "id" para que el WS
		// del editor sepa cuando se le da al "Save" a que proceso tiene que asociar el mapa, ya que si no tiene
		// esta información no será capaz de asociar el xml que le entra con el proceso en el que tiene que
		// guardar el mapa.
		String processMap = createEmptyProcessXmlMap(newProcessId, currentUserId);
		this.updateProcessXmlMap(newProcessId, processMap, currentUserId);
		process.setProcessMap(processMap);
		
		return newProcessId;

	}
	
	// dml 20130508 -
	public String createEmptyProcessXmlMap(Integer newProcessId, Integer currentUserId) 
			throws WProcessDefException, WStepSequenceDefException{
		
		String returnValue = "";
		
		// lo recargo para tener el process.getBeginStep().getName() que me hará falta para crear el mapa
		WProcessDef process = null;
		if (newProcessId != null){
			process = this.getWProcessDefByPK(newProcessId, currentUserId);
		} else {
			return null;
		}
		
		if (process == null){
			return null;
		}
		
		returnValue += "<mxGraphModel><root>";
		returnValue += "<Workflow label=\"" + process.getName() + "\" id=\"0\" description=\"\" spId=\"" + process.getId() + "\"><mxCell/></Workflow>";
		returnValue += "<Layer label=\"Default Layer\" description=\"\" id=\"1\"><mxCell parent=\"0\"/></Layer>";
		
		returnValue += "<Symbol label=\"Begin\" description=\"\" href=\"\" id=\"3\"><mxCell style=\"symbol;image=images/symbols/event.png\" vertex=\"1\" parent=\"1\"><mxGeometry x=\"320\" y=\"230\" width=\"32\" height=\"32\" as=\"geometry\"/></mxCell></Symbol>";
		
		if (process.getBeginStep() != null){
			String responses = "responses=\"";
			if (process.getBeginStep().getResponse() != null
				&& !process.getBeginStep().getResponse().isEmpty()){
				for (WStepResponseDef response : process.getBeginStep().getResponse()){
					
					responses += response.getName() + "|"; 
					
				}
			} 
			responses += "\"";
			
			returnValue += "<Edge label=\"\" description=\"\" id=\"4\"><mxCell style=\"straightEdge\" edge=\"1\" parent=\"1\" source=\"3\" target=\"2\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell></Edge>";
			
			returnValue += "<Task description=\"" + process.getBeginStep().getStepComments() + 
					"\" href=\"\" id=\"2\" label=\"" + process.getBeginStep().getName() 
					+ "\" spId=\"" + process.getBeginStep().getId() + "\" " 
					+ responses + "><mxCell parent=\"1\" vertex=\"1\">"
					+ "<mxGeometry as=\"geometry\" height=\"32\" width=\"72\" x=\"430\" y=\"230\"/></mxCell></Task>";
		}
		
		returnValue += "</root></mxGraphModel>";
		
		return returnValue;
		
	}
	
	// dml 20130430
	private void _setFirstWProcessDefData(WProcessDef process, Integer processHeadId, Integer currentUserId) 
			throws WProcessHeadException {
		
		if (process != null){
			
			process.setActive(true);

			process.setProcess(new WProcessHeadBL().getProcessHeadByPK(processHeadId, currentUserId));

			if (process.getVersion() == null
					|| process.getVersion().equals(0)){
				process.setVersion(FIRST_WPROCESSDEF_VERSION);
			}
		
		}
		
	}
	
	// dml 20130430
	public void createFirstWProcessDef(Integer processHeadId, Integer currentUserId) 
			throws WProcessDefException, WProcessHeadException, WStepSequenceDefException{

		WProcessDef wpd = new WProcessDef(EMPTY_OBJECT);

		this._setFirstWProcessDefData(wpd, processHeadId, currentUserId);
		
		this.add(wpd, currentUserId);
		
	}
	
	public void update(WProcessDef process, Integer currentUserId) throws WProcessDefException {
		
		logger.debug("update() WProcessDef < id = "+process.getId()+">");
		
		WProcessDef storedProcess = new WProcessDefDao().getWProcessDefByPK(process.getId(), currentUserId); 
		if (!process.equals(storedProcess) ) {

			Date now = new Date();
			
			// timestamp & trace info
			process.setModDate(now);
			process.setModUser(currentUserId);
			
			// check head && managed data table changes ...
			// managed table only can be added if the WProcessDef and WProcessHead already exists
			// There is not possible to add it in "add" method for WProcessDef ...
			if (process.getProcess()!=null) {
				
				if (process.getProcess().getManagedTableConfiguration()!=null) {
				
					if (process.getProcess().getManagedTableConfiguration().getHeadId()==0
							|| storedProcess.getProcess().getManagedTableConfiguration()==null
							|| storedProcess.getProcess().getManagedTableConfiguration().getHeadId()==0) {
						
						try {
							
							// set pk (same id that process-head-id
							process.getProcess().getManagedTableConfiguration().setHeadId(process.getProcess().getId());
							process.getProcess().getManagedTableConfiguration()
										.setName(_ROOT_MANAGED_TABLE_NAME+process.getProcess().getId());
							
							Integer id = new WProcessHeadManagedDataBL()
													.add(process.getProcess().getManagedTableConfiguration(), currentUserId);
							if ( id!=process.getProcess().getId() ) {
								logger.error("WProcessDef:update Error trying persist ManagedTable for process head id:"
										+(process.getProcess().getId()!=null?process.getProcess().getId():"null")
										+". The w_process_head_managed_data was added with id:"+(id!=null?id:"null"));
							}
							
							process.getProcess().getManagedTableConfiguration().setHeadId(id);
						
						} catch (WProcessHeadException e) {
							logger.error("Can't add process head managed table name:"
											+(process.getProcess()
														.getManagedTableConfiguration()
														.getName()!=null 
																? process.getProcess().getManagedTableConfiguration().getName()
																: "null")
											+ e.getMessage()+" - "+e.getCause());
						}
						
					} 
			
				}
				
				if ( process.getProcess().getProcessDataFieldDef()!=null ) {
					for (WProcessDataField pdf: process.getProcess().getProcessDataFieldDef()){
						
					}
				}
			}
			
			
			new WProcessDefDao().update(process, currentUserId);
			
		} else {
			
			logger.debug("WProcessDefBL.update - nothing to do ...");
		}
			
	}
	
	// dml 20130703
	public void updateProcessXmlMap(Integer processId, String processMap, Integer currentUserId)
			throws WProcessDefException {

		logger.debug("updateProcessXmlMap() WProcessDef < id = " + processId + ">");

		if (processId != null 
				&& !processId.equals(0) 
				&& processMap != null
				&& !processMap.isEmpty()) {

			new WProcessDefDao().updateProcessXmlMap(processId, processMap, currentUserId, new Date());

		} else {

			logger.debug("WProcessDefBL.updateProcessXmlMap - error updating process xml map. processId/processMap are not correct ...");

			throw new WProcessDefException(
					"The processId and processMap must have a correct value.");

		}

	}

	public List<String> delete(Integer processId, boolean deleteRelatedSteps, Integer currentUserId) 
			throws WProcessWorkException, WProcessDefException, WStepSequenceDefException, 
			WStepWorkException, WProcessHeadException, WStepDefException, WStepHeadException {

		logger.debug("delete() WProcessDef - Name: ["+processId+"]");
		
		if (processId==null || processId==0) {
			String mess = "Trying delete process with id null or 0 ...";
			logger.error(mess);
			throw new WProcessWorkException(mess);
		}
		
		Integer qtyWorks;
		try {
			qtyWorks = new WStepWorkBL().getStepWorkCountByProcess(processId,ALL);
		} catch (WStepWorkException e) {
			String mess = "Error verifiyng existence of works related with this process id:"+processId
					+ " "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WProcessWorkException(mess);
		}
		
		if (qtyWorks>0) {
			String mess = "Delete process with works is not allowed ... This process has "+qtyWorks+" works";
			logger.error(mess);
			throw new WProcessWorkException(mess);
		}
			
		try {
			qtyWorks = new WProcessWorkBL().getWorkCount(processId,ALL);
		} catch (WProcessWorkException e) {
			String mess = "Error verifiyng existence of works related with this process id:"+processId
					+ " "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WProcessWorkException(mess);
		}
		
		if (qtyWorks>0) {
			String mess = "Delete process with works is not allowed ... This process has "+qtyWorks+" works";
			logger.error(mess);
			throw new WProcessWorkException(mess);
		}
			
		List<WStepDef> stepsToDelete = new ArrayList<WStepDef>(); // to store and return deleted steps

		// check for related steps and delete 
		if (deleteRelatedSteps) {
			
			stepsToDelete = this._checkAndDeleteRelatedSteps(processId,currentUserId);
			
		}
		
		
		this._deleteRelatedSequences(processId, currentUserId);// this is estrictly version related info 
		
		WProcessDef process = this.getWProcessDefByPK(processId, currentUserId);
		Integer processHeadId = process.getProcess().getId();
		
		// se borrarán en cascada (por el mapping) los roles y users relacionados con el process
		new WProcessDefDao().delete(process, currentUserId);
		logger.info("The WProcessDef " + process.getName() + " has been correctly deleted by user " + currentUserId);

		List<String> deletedSteps = this._deleteRelatedSteps(stepsToDelete, processHeadId, processId, currentUserId);
		
		// delete processHead and related managed table only if all process-def has already deleted
		this._checkAndDeleteProcessHead(processHeadId, currentUserId);
		
		

		return deletedSteps;
		
	}
	
	// dml 20130507
	private void _deleteRelatedSequences(Integer processId, Integer currentUserId) throws WProcessWorkException{
				
		List<WStepSequenceDef> wssdList;
		WStepSequenceDefBL wssdBL = new WStepSequenceDefBL();
		
		try {
			
			wssdList = new WStepSequenceDefBL().getStepSequenceList(processId, null, currentUserId);
			
			if (wssdList != null
					&& !wssdList.isEmpty()){
				
				for (WStepSequenceDef wssd : wssdList){
					
					wssdBL.deleteRoute(wssd, currentUserId);
					
				}
				
			}
			
		} catch (WStepSequenceDefException e) {
			String mess = "Impossible to delete process " + processId + " step sequence defs";
			logger.error(mess);
			throw new WProcessWorkException(mess);
		} catch (WStepWorkSequenceException e) {
			String mess = "Impossible to delete process " + processId + " step sequence defs because it has related WStepWorkSequences";
			logger.error(mess);
			throw new WProcessWorkException(mess);
		}
		
	}

	// dml 20130507
	private List<String> _deleteRelatedSteps(List<WStepDef> stepList, Integer processHeadId, Integer processId, Integer currentUserId) 
			throws WStepDefException, WStepWorkException, WStepHeadException, 
			WProcessDefException, WStepSequenceDefException {
				
		WStepDefBL wsdBL = new WStepDefBL();
		List<String> deletedSteps = new ArrayList<String>();
		
		try {
			
			if (stepList != null
					&& !stepList.isEmpty()){
				
				for (WStepDef wsd : stepList){
					
					deletedSteps.add(wsd.getName());
					wsdBL.delete(wsd.getId(), processHeadId, currentUserId); // nes 20130808 - por agregado de filtro para step-data-field
					logger.info("The WStepDef " + wsd.getName() + " has been correctly deleted by user " + currentUserId);
					
				}
				
			}
			
		} catch (WStepDefException e) {
			String mess = "Impossible to delete step defs";
			logger.error(mess);
			throw new WStepDefException(mess);
		} catch (WProcessDefException e) {
			String mess = "Impossible to delete step defs";
			logger.error(mess);
			throw new WProcessDefException(mess);
		} catch (WStepSequenceDefException e) {
			String mess = "Impossible to delete step defs";
			logger.error(mess);
			throw new WStepSequenceDefException(mess);
		} catch (WStepWorkSequenceException e) {
			String mess = "Impossible to delete step defs";
			logger.error(mess);
			throw new WStepSequenceDefException(mess);
		}
		
		return deletedSteps;
		
	}

	// dml 20130507 - nes 20130821
	private void _checkAndDeleteProcessHead(Integer processHeadId, Integer currentUserId) 
			throws WProcessHeadException, WProcessDefException{
		
		if (processHeadId==null) {
			throw new WProcessHeadException("Error: trying delete process head with id=null");
		}
		
		// if don't exists another process versions for this processHead, then delete processHead and ManagedTable ...
		if (!hasVersions(processHeadId)) {

			WProcessHeadBL phBL = new WProcessHeadBL();
			
			WProcessHead processHead = phBL.getProcessHeadByPK(processHeadId, currentUserId);		

			phBL.delete(processHead, currentUserId);
			
			logger.info("The WProcessHead " + processHeadId + " has been correctly deleted by user " + currentUserId);
			
		}
		
	}

	private List<WStepDef> _checkAndDeleteRelatedSteps(Integer processId, Integer currentUserId) 
			throws WProcessWorkException, WStepSequenceDefException, WStepWorkException {
		
		List<WStepDef> returnValue = new ArrayList<WStepDef>();
		try {
			
			List<WStepDef> stepDefList = loadStepList(processId, currentUserId);
			
			for (WStepDef stepDef: stepDefList) {
				
				if ( !checkSharedStep(stepDef.getId(), processId, currentUserId) 
						&& !checkWorkReferral(stepDef.getId(),processId, currentUserId) ) {
					
					returnValue.add(stepDef);
					
				}
				
			}
			
		} catch (WStepDefException e) {
			String mess = "can't obtain step def list for given process id:"+processId;
			throw new WProcessWorkException(mess);
		}
		
		
		return returnValue;
	}
	

	public boolean checkSharedStep(Integer stepId, Integer processId, Integer currentUserId) throws WStepSequenceDefException {
		
		try {

			return new WStepDefBL().stepIsShared(stepId, currentUserId);
		
		} catch (Exception e) {
			String mess = "Error checking shared steps for process id:"+processId;
			throw new WStepSequenceDefException(mess);
		}

	}
	
	public boolean checkWorkReferral(Integer stepId, Integer processId, Integer currentUserId) throws WStepWorkException {
		
		try {

			return new WStepWorkBL().isAnotherProcessUsingWorkStep(stepId, processId, currentUserId);
			
		} catch (Exception e) {
			String mess = "Error checking shared work steps for process id:"+processId;
			throw new WStepWorkException(mess);
		}
		
	}
	
	// dml 20130506
	public void deactivateProcess(Integer processId, Integer currentUserId) throws WProcessDefException, WStepSequenceDefException{
		
		if (processId != null
				&& !processId.equals(0)){
			
			WProcessDef wpd = this.getWProcessDefByPK(processId, currentUserId);
			
			if (wpd != null){
				
				if (wpd.isActive()){
					
					wpd.setActive(false);
					this.update(wpd, currentUserId);
					
				} else {

					String message = "The process you're trying to deactivate is already non active.";
					logger.error(message);
					throw new WProcessDefException(message);
					
				}
				
			}
			
		}
		
	}

	// dml 20130506
	public void activateProcess(Integer processId, Integer currentUserId) throws WProcessDefException, WStepSequenceDefException{
		
		if (processId != null
				&& !processId.equals(0)){
			
			WProcessDef wpd = this.getWProcessDefByPK(processId, currentUserId);
			
			if (wpd != null){
				
				if (!wpd.isActive()){
					
					wpd.setActive(true);
					this.update(wpd, currentUserId);
					
				} else {

					String message = "The process you're trying to activate is already active.";
					logger.error(message);
					throw new WProcessDefException(message);
					
				}
				
			}
			
		}
		
	}

	public WProcessDef getWProcessDefByPK(Integer id, Integer currentUserId) 
				throws WProcessDefException, WStepSequenceDefException {

		WProcessDef wpd;
		wpd = new WProcessDefDao().getWProcessDefByPK(id, currentUserId);
		
		if (wpd != null){
			
			try {
				
				wpd.setlSteps(loadStepList(wpd.getId(),currentUserId));
//				wpd.setManagedDataDef(loadManagedDataDef(wpd));
				
				
			} catch (WStepDefException e1) {
				String mess="Error: getWProcessDefByPK "+e1.getMessage();
				throw new WStepSequenceDefException(mess);
			}
			
			try {
				wpd.setStepSequenceList(loadStepSequenceList(wpd.getId(),currentUserId));
			} catch (WStepSequenceDefException e) {
				String mess="Error: getWProcessDefByPK "+e.getMessage();
				throw new WStepSequenceDefException(mess);
			}
			
		}
		
		return wpd;
		
	}
	
//	private void loadManagedDataDef( WProcessDef wpd ) {
//
//		if (wpd.getProcess().getId()==null  
//				|| wpd.getProcess().getId()==0 
//				|| wpd.getProcess().getManagedTableConfiguration()==null
//				|| wpd.getProcess().getManagedTableConfiguration().getName()==null
//				|| "".equals(wpd.getProcess().getManagedTableConfiguration().getName())) {
//			wpd.setManagedDataDef(null);
//			return;
//		}
//		
//		
//	}
	
	public void _createManagedDataObject(WProcessDef wpd) {
		
		
	}
	
	
/*	
	public WProcessDef getWProcessDefByName(String name, Integer currentUserId) throws WProcessDefException, WStepSequenceDefException {

		WProcessDef wpd;
		wpd = new WProcessDefDao().getWProcessDefByName(name, currentUserId);
		
		try {
			wpd.setlSteps(loadStepList(wpd.getId(),currentUserId));
		} catch (WStepDefException e1) {
			String mess="Error: getWProcessDefByName "+e1.getMessage();
			throw new WStepSequenceDefException(mess);
		}
		
		try {
			wpd.setStepSequenceList(loadStepSequenceList(wpd.getId(),currentUserId));
		} catch (WStepSequenceDefException e) {
			String mess="Error: getWProcessDefByName "+e.getMessage();
			throw new WStepSequenceDefException(mess);
		}
		
		return wpd;
	}
*/
	
	// dml 20130506
	public List<WProcessDef> getProcessList(Integer currentUserId) throws WProcessDefException {

		List<WProcessDef> wpdList = new WProcessDefDao().getWProcessDefs(currentUserId);
		
		// dml 20130506
		if (wpdList != null){
		
			for (WProcessDef wpd : wpdList){
				
				try {
					
					wpd.setlSteps(loadStepList(wpd.getId(),currentUserId));
					wpd.setStepSequenceList(loadStepSequenceList(wpd.getId(), currentUserId));
				
				} catch (WStepSequenceDefException e) {
				
					String mess="Error: getProcessList: can't get routes related with process id: "
							+ wpd.getId() + "Error: " + e.getMessage();
					logger.error(mess);
				
				} catch (WStepDefException e) {
					
					String mess="Error: getProcessList: can't get step sequence for process id: "
							+ wpd.getId() + "Error: " + e.getMessage();
					logger.error(mess);
				
				}
				
			}
			
		} else {
			
			String mess="Error: getWProcessDefs: Impossible to get the current wProcessDefList";
			logger.error(mess);
			throw new WProcessDefException(mess);
			
		}
		
		return wpdList;
	
	}
	
	// dml 20130430
	public Integer getLastVersionNumber(Integer processHeadId, Integer currentUserId) throws WProcessDefException {

		return new WProcessDefDao().getLastVersionNumber(processHeadId, currentUserId);
	
	}
	
	// dml 20130710
	public String getProcessDefXmlMap(Integer processDefId, Integer currentUserId) throws WProcessDefException {

		return new WProcessDefDao().getProcessDefXmlMap(processDefId, currentUserId);
	
	}
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion, Integer currentUserId )
	throws WProcessDefException {
		 
		return new WProcessDefDao().getComboList(textoPrimeraLinea, separacion, currentUserId);
		
	}

	// dml 20120120
	public List<StringPair> getComboActiveProcessList(String firstLineText, String blank, Integer currentUserId )
	throws WProcessDefException {
	
		return new WProcessDefDao().getComboActiveProcessList(firstLineText, blank, currentUserId);
	
	}	
	
	public List<WProcessDef> finderWProcessDefLight (Date initialInsertDateFilter, Date finalInsertDateFilter, 
			boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
			String listZoneFilter, String workZoneFilter, String additinalZoneFilter,
			Integer userId, boolean isAdmin, String action, Integer currentUserId ) 
	throws WProcessDefException {
		
		return new WProcessDefDao().finderWProcessDef(initialInsertDateFilter, finalInsertDateFilter, 
				strictInsertDateFilter, nameFilter, commentFilter, listZoneFilter, 
				workZoneFilter, additinalZoneFilter, userId, isAdmin, action, currentUserId);

	}

	public List<WProcessDefLight> finderWProcessDefLight(boolean onlyWorkingProcessesFilter, 
			String processNameFilter, Date initialProductionDateFilter, Date finalProductionDateFilter, 
			boolean strictProductionDateFilter, Integer productionUserFilter, String action, 
			Integer processHeadId, String activeFilter, Integer currentUserId)
	throws WProcessDefException {
		
		return new WProcessDefDao().finderWProcessDefLight(onlyWorkingProcessesFilter, 
				processNameFilter, initialProductionDateFilter, finalProductionDateFilter,
				strictProductionDateFilter, productionUserFilter, action, processHeadId, 
				activeFilter, currentUserId);
		
	}

	public String getProcessNameByVersionId(Integer id, Integer currentUserId) 
			throws WProcessDefException, WStepSequenceDefException {

		return new WProcessDefDao().getProcessNameByVersionId(id, currentUserId);
	
	}
	
	
	/**
	 *  create a new version from current wprocessdef (w_process_def, related users and roles, related steps and routes ...)
	 *  generating a new Process Head for cloned version. Cloned new version starts at version=1
	 *  
	 *  Status: ahora mismo simplemente clona el proceso pero reutiliza los steps actuales. Falta el ajuste de los spid en los
	 *  edge del mapa.
	 *  TODO: implementar el clonar los steps y correspondientemente clonar las sequences con los nuevos step-id
	 *  
	 *  returns new process id
	 *  
	 * @param processDefId
	 * @param processHeadId
	 * @param currentUserId
	 * @param boolean startsNewVersionAt1 -> true: new version =1, false: new version = current version
	 * @param boolean emptyRoleList
	 * @param boolean emptyUserList
	 * @param Integer createNewClonedSteps  --> 0: use existing steps, 1: yes create a clone of each existing step, 
	 * 											 2: empty step list
	 * @param boolean emptyWorkflowMap 
	 * @param boolean persistNewVersion -> if true persiste the new version, if not only returns full loaded object ...
	 * 
	 * @return
	 * @throws WProcessHeadException
	 * @throws WStepSequenceDefException
	 * @throws WProcessDefException
	 * 
	 */
	public Integer cloneWProcessDef(
			String newProcessName,
			Integer processDefId, Integer processHeadId, boolean startsNewVersionAt1, boolean emptyRoleList, 
			boolean emptyUserList, int createNewClonedSteps, boolean emptyWorkflowMap, Integer currentUserId ) 
			throws  WProcessHeadException, WStepSequenceDefException, WProcessDefException  {
		
		Integer clonedId=null, newversion=1;
		WProcessDef newprocver,procver;
		WProcessHeadManagedDataConfiguration mdf=null;
		Set<WProcessDataField> dataFieldDef=null;
		List<WStepSequenceDef> routes = new ArrayList<WStepSequenceDef>();
		try {
			newprocver = this.getWProcessDefByPK(processDefId, currentUserId);
			procver = this.getWProcessDefByPK(processDefId, currentUserId);
			if ( !startsNewVersionAt1 ) {
				newversion=this.getLastVersionNumber(processHeadId, currentUserId)+1;				
			}
		} catch (WProcessDefException e) {
			String mess = "Error cloning process version: can't get original process version id:"+processDefId
							+" - "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WProcessDefException(mess);
		} catch (WStepSequenceDefException e) {
			String mess = "Error cloning process version: can't get original process version id:"+processDefId
					+" - "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WStepSequenceDefException(mess);
		}

		// if not a new process name then add "cloned" to existing process name ...
		if (newProcessName==null || "".equals(newProcessName)) {
			newProcessName="Cloned "+procver.getName();
		}
		
		try {
			
			newprocver.setId(null);
			newprocver.getProcess().setId(null);
			newprocver.setVersion(newversion);
			
			// reserving Managed Data Configuration and Process Data Field list
			if (newprocver.getProcess().getManagedTableConfiguration()!=null) {
				mdf = newprocver.getProcess().getManagedTableConfiguration();
			}
			if (newprocver.getProcess().getProcessDataFieldDef()!=null) {
				dataFieldDef = new HashSet<WProcessDataField>();
				for (WProcessDataField pdf:newprocver.getProcess().getProcessDataFieldDef() ) {
					pdf.setId(null);
					pdf.setProcessHeadId(null);
					dataFieldDef.add(pdf);
				}
			}
			
			// mandatory: empty process head and set new name to add the new process 
			newprocver.setProcess(new WProcessHead());
			newprocver.setName(newProcessName);

			// replicate user and role list
			loadUsersAndRoles(currentUserId, emptyRoleList, emptyUserList,
					newprocver, procver);
			
		} catch (Exception e) {
			String mess = "Error cloning process version:"+processDefId+" can't clone related role or user list ..."
					+" - "+e.getClass()+" -" +e.getMessage()+" - "+e.getCause();
			throw new WProcessDefException(mess);
		}
		
		// clean current process map (must be migrated for spIds of new edges and new steps)
		if (emptyWorkflowMap) {
			newprocver.setProcessMap(null);
		}
		
		// persist process def and obtain process head id of new process
		clonedId = addClonedProcessDef(processDefId, currentUserId, clonedId,
				newprocver);			
		
		// build managed table (if exists)
		buildManagedTable(currentUserId, newprocver, procver, mdf, dataFieldDef);
		
		List<IntegerPair> relClonedSequences=null;
		// if createNewClonedSteps parameter then clone step list ...
		if (createNewClonedSteps==1) {
			// clonar cada paso y reacomodar la nueva secuencia (porque las secuencias tendrán q referir a los nuevos pasos
			
		} else if (createNewClonedSteps==2) {
			// clean step sequence list
		} else {
			relClonedSequences = cloneCurrentRoutes(processDefId,
					currentUserId, clonedId, newprocver, routes);
		}

		// to be implemented: migrate xml map
		// load xml map and for each sequence update edge's spIds
		if (relClonedSequences!=null) {
			for (IntegerPair seqPair: relClonedSequences) {
				//xxxxxx
			}
		}
		
		return clonedId;
	}

	// clone each step and update routes with new stepId
	private List<IntegerPair> cloneStepListAndRoutes(Integer processDefId,
			Integer currentUserId, Integer clonedId, WProcessDef newprocver,
			List<WStepSequenceDef> routes) throws WStepSequenceDefException {
		// obtener la lista de steps (se obtiene a partir de las rutas :( )
		
		// clone routes (the workflow map really ...)
		WStepSequenceDefBL seqBL = new WStepSequenceDefBL();
		List<IntegerPair> relClonedSequences = new ArrayList<IntegerPair>(); // reserve seq id pairs to migrate map
		try {
			routes = new WStepSequenceDefBL().getStepSequenceList(processDefId, null, currentUserId);
			if (routes.size()>0){
				for (WStepSequenceDef route: routes) {
					route.setProcess(new WProcessDef());
					route.getProcess().setId(clonedId);
					int newRouteId=seqBL.add(route, currentUserId); // insert new cloned route
					relClonedSequences.add(new IntegerPair(route.getId(),newRouteId));
					logger.debug("inserted new route:"+route.getId()+":"+route.getProcess().getId()+" user:"+ currentUserId);
				}
			}			
		} catch (WStepSequenceDefException e) {
			String mess = "Error cloning routes for old process id:"+processDefId+"  newProcessId:"+newprocver
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WStepSequenceDefException(mess);
		}
		return relClonedSequences;
	}
	
	// clone existing routes and insert in sequence table
	private List<IntegerPair> cloneCurrentRoutes(Integer processDefId,
			Integer currentUserId, Integer clonedId, WProcessDef newprocver,
			List<WStepSequenceDef> routes) throws WStepSequenceDefException {
		// clone routes (the workflow map really ...)
		WStepSequenceDefBL seqBL = new WStepSequenceDefBL();
		List<IntegerPair> relClonedSequences = new ArrayList<IntegerPair>(); // reserve seq id pairs to migrate map
		try {
			routes = new WStepSequenceDefBL().getStepSequenceList(processDefId, null, currentUserId);
			if (routes.size()>0){
				for (WStepSequenceDef route: routes) {
					route.setProcess(new WProcessDef());
					route.getProcess().setId(clonedId);
					int newRouteId=seqBL.add(route, currentUserId); // insert new cloned route
					relClonedSequences.add(new IntegerPair(route.getId(),newRouteId));
					logger.debug("inserted new route:"+route.getId()+":"+route.getProcess().getId()+" user:"+ currentUserId);
				}
			}			
		} catch (WStepSequenceDefException e) {
			String mess = "Error cloning routes for old process id:"+processDefId+"  newProcessId:"+newprocver
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WStepSequenceDefException(mess);
		}
		return relClonedSequences;
	}

	private Integer addClonedProcessDef(Integer processDefId,
			Integer currentUserId, Integer clonedId, WProcessDef newprocver)
			throws WStepSequenceDefException, WProcessDefException,
			WProcessHeadException {
		try {
			
			clonedId = this.add(newprocver, currentUserId);
			
		} catch (WProcessDefException e) {
			String mess = "Error cloning process version: can't ADD new clone process version original-id:"+processDefId
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WProcessDefException(mess);
		} catch (WProcessHeadException e) {
			String mess = "Error cloning process version: can't ADD new clone process version original-id:"+processDefId
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WProcessHeadException(mess);
		}
		return clonedId;
	}

	private void buildManagedTable(Integer currentUserId,
			WProcessDef newprocver, WProcessDef procver,
			WProcessHeadManagedDataConfiguration mdf,
			Set<WProcessDataField> dataFieldDef) throws WProcessDefException {
		if (mdf!=null) {
			mdf.setComment(
					(mdf.getComment()!=null?mdf.getComment():"")
					+" cloned from headid "+procver.getProcess().getId()+" ");
			mdf.setHeadId(0);
			mdf.setName(null);
			newprocver.getProcess().setManagedTableConfiguration(mdf);
			
			newprocver.getProcess().setProcessDataFieldDef(null);
			
			// updates new process to create managed table
			update( newprocver,currentUserId );	
			
			if (dataFieldDef!=null) {
				
				WProcessDataFieldBL wdfBL = new WProcessDataFieldBL();
				int phId = newprocver.getProcess().getId();
				
				for (WProcessDataField pdf:dataFieldDef) {
					pdf.setProcessHeadId(phId);
					try {
						wdfBL.add(pdf, currentUserId);
					} catch (WProcessDataFieldException e) {
						logger.error("buildManagedTable: Error WProcessDataFieldException adding new process data field to cloned process "+e.getMessage()+" - "+e.getCause());
					} catch (WDataTypeException e) {
						logger.error("buildManagedTable: Error WDataTypeException adding new process data field to cloned process "+e.getMessage()+" - "+e.getCause());
					} catch (TableManagerException e) {
						logger.error("buildManagedTable: Error TableManagerException adding new process data field to cloned process "+e.getMessage()+" - "+e.getCause());
					}
				}
			}
		}
	}

	private void loadUsersAndRoles(Integer currentUserId,
			boolean emptyRoleList, boolean emptyUserList,
			WProcessDef newprocver, WProcessDef procver) {
		// load users and roles
		newprocver.setRolesRelated(new HashSet<WProcessRole>());
		newprocver.setUsersRelated(new HashSet<WProcessUser>());				
		
		if ( !emptyRoleList && procver.getRolesRelated().size()>0) {
			for ( WProcessRole processRole: procver.getRolesRelated() ) {
				processRole.setProcess(null);
				newprocver
					.addRole(
							processRole.getRole(), processRole.isAdmin(), 
							processRole.getIdObject(), processRole.getIdObjectType(), 
							currentUserId);
			}
		}

		if ( !emptyUserList && procver.getUsersRelated().size()>0) {
			for ( WProcessUser processUser: procver.getUsersRelated() ) {
				processUser.setProcess(null);
				newprocver
					.addUser(
							processUser.getUser(), processUser.isAdmin(), 
							processUser.getIdObject(), processUser.getIdObjectType(), 
							currentUserId);
			}
		}
	}
	
	/**
	 *  clone a process version (w_process_def, related users and roles, related steps and routes ...)
	 *  generating a new Process Head for cloned version. Cloned new version starts at version=1
	 *  
	 *  returns new process id
	 *  
	 * @param processDefId
	 * @param processHeadId
	 * @param currentUserId
	 * @param boolean emptyRoleList
	 * @param boolean emptyUserList
	 * @param Integer createNewClonedSteps  --> 0/null: use existing steps, 1: yes create a clone of each existing step, 
	 * 											 2: empty step list
	 * @param boolean emptyWorkflowMap 
	 * 
	 * @return
	 * @throws WProcessHeadException
	 * @throws WStepSequenceDefException
	 * @throws WProcessDefException
	 * 
	 */	
	public WProcessDef createNewVersion(
			Integer processDefId, Integer processHeadId, Integer currentUserId,
			boolean emptyRoleList, boolean emptyUserList, Integer createNewClonedSteps, boolean emptyWorkflowMap,
			boolean persistNewVersion ) throws WProcessDefException {
		
		Integer newVersionId;
		WProcessDef newprocver,procver;
		List<WStepSequenceDef> routes = new ArrayList<WStepSequenceDef>();
		try {
			newprocver = this.getWProcessDefByPK(processDefId, currentUserId);
			procver = this.getWProcessDefByPK(processDefId, currentUserId);
		} catch (WProcessDefException e) {
			String mess = "Error WProcessDefException creating new process version: can't get original process version [id:"+processDefId
							+"] - "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WProcessDefException(mess);
		} catch (WStepSequenceDefException e) {
			String mess = "Error WProcessDefException creating new process version: can't get original process version [id:"+processDefId
					+"] - "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WProcessDefException(mess);
		}
		

		try {
			
			newprocver.setId(null);
			
			Integer lastVersion = 
					new WProcessDefBL().getLastVersionNumber(processHeadId, currentUserId);
			
			newprocver.setVersion(lastVersion+1);
			
			loadUsersAndRoles(currentUserId, emptyRoleList, emptyUserList,
					newprocver, procver);
			
		} catch (WProcessDefException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (persistNewVersion) {

			try {
				newVersionId = add(newprocver, currentUserId);
			} catch (WProcessDefException e) {
				String mess = "Error WProcessDefException creating new process version: can't ADD new processdef object version original-id:"+processDefId
						+" - "+e.getMessage()+" - "+e.getCause();
				logger.error(mess);
				throw new WProcessDefException(mess);
			} catch (WProcessHeadException e) {
				String mess = "Error WProcessHeadException creating new process version: can't ADD new clone processdef object version original-id:"+processDefId
						+" - "+e.getMessage()+" - "+e.getCause();
				logger.error(mess);
				throw new WProcessDefException(mess);
			} catch (WStepSequenceDefException e) {
				String mess = "Error WProcessHeadException creating new process version: can't ADD new clone processdef object version original-id:"+processDefId
						+" - "+e.getMessage()+" - "+e.getCause();
				logger.error(mess);
				throw new WProcessDefException(mess);
			}
			
		}

		return newprocver;
		
	}
	
	// dml 20130129
	public boolean userIsProcessAdmin(Integer userId, Integer processId, Integer currentUserId) throws WProcessDefException{
		
		return new WProcessDefDao().userIsProcessAdmin(userId, processId, currentUserId);
		
	}
	
	
	// nes 20130502 - traido desde el backing bean ...
	private List<WStepDef> loadStepList(Integer processId, Integer currentUserId) 
			throws WStepDefException {

		List<WStepDef> lsteps=new ArrayList<WStepDef>();
		try {

			if (processId != null && processId != 0) {

				lsteps = new WStepDefBL().getStepDefs(processId, null, currentUserId);

			}
		} catch (WStepDefException ex1) {
			
			lsteps=null;
			String mess = "Error loading step list:"+ ex1.getMessage() + " - " + ex1.getCause();
			throw new WStepDefException(mess);

		}
		
		return lsteps;
	}
	
	private List<WStepSequenceDef> loadStepSequenceList(Integer processId, Integer currentUserId) 
			throws WStepSequenceDefException{
		
		List<WStepSequenceDef> routes = new ArrayList<WStepSequenceDef>();
		try {

			if (processId != null && processId != 0) {

				routes = new WStepSequenceDefBL().getStepSequenceList(processId, null, currentUserId);
				
			}
			
		} catch (WStepSequenceDefException ex1) {

			routes=null;
			String mess = "Error loading StepSequenceList:"+ ex1.getMessage() + " - " + ex1.getCause();
			throw new WStepSequenceDefException(mess);

		}
		return routes;
	}
	
	public boolean hasVersions(Integer processHeadId) throws WProcessDefException {
		return new WProcessDefDao().hasVersions(processHeadId);
	}
}
	