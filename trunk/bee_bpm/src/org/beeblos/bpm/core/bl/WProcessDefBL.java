package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;
import static org.beeblos.bpm.core.util.Constants.ALL;
import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;
import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;
import static org.beeblos.bpm.core.util.Constants.FIRST_WPROCESSDEF_VERSION;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

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
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WStepWorkSequence;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;
import org.beeblos.bpm.tm.exception.TableAlreadyExistsException;
import org.beeblos.bpm.tm.exception.TableManagerException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxStyleUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.sp.common.util.IntegerPair;
import com.sp.common.util.StringPair;



public class WProcessDefBL {
	
	private static final boolean CLONE_PERMISSIONS = true;

	private static final boolean CLONE_RESPONSES = true;

	private static final boolean GENERATE_NEW_STEP = true;
	
	boolean DONT_CLONE_ROUTES = false;

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

	/**
	 * Deletes a process def and all it's relations like sequences, steps (if user like), etc.
	 * Don't delete related works, then check if exists related works and then throws exception
	 * 
	 * @param processDefId
	 * @param deleteRelatedSteps
	 * @param currentUserId
	 * @return
	 * @throws WProcessWorkException
	 * @throws WProcessDefException
	 * @throws WStepSequenceDefException
	 * @throws WStepWorkException
	 * @throws WProcessHeadException
	 * @throws WStepDefException
	 * @throws WStepHeadException
	 */
	public List<String> delete(Integer processDefId, boolean deleteRelatedSteps, Integer currentUserId) 
			throws WProcessWorkException, WProcessDefException, WStepSequenceDefException, 
			WStepWorkException, WProcessHeadException, WStepDefException, WStepHeadException {

		logger.debug("delete() WProcessDef - Name: ["+processDefId+"]");
		
		if (processDefId==null || processDefId==0) {
			String mess = "Trying delete process with id null or 0 ...";
			logger.error(mess);
			throw new WProcessWorkException(mess);
		}
		
		Integer qtyWorks;
		try {
			qtyWorks = new WStepWorkBL().getStepWorkCountByProcess(processDefId,ALL);
		} catch (WStepWorkException e) {
			String mess = "WStepWorkException: Error verifiyng existence of step works related with this process id:"+processDefId
					+ " "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WProcessWorkException(mess);
		}
		
		if (qtyWorks>0) {
			String mess = "Delete process with works is not allowed ... This process has "+qtyWorks+" step works";
			logger.error(mess);
			throw new WProcessWorkException(mess);
		}
			
		try {
			qtyWorks = new WProcessWorkBL().getWorkCount(processDefId,ALL);
		} catch (WProcessWorkException e) {
			String mess = "Error verifiyng existence of works related with this process id:"+processDefId
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

		// obtains a list of related steps that may be deleted
		if (deleteRelatedSteps) {
			
			stepsToDelete = this._loadListOfStepsToDelete(processDefId,currentUserId);
			
		}
		
		// sequence is strictly version related info -> we can delete it
		this._deleteRelatedSequences(processDefId, currentUserId); 
		
		WProcessDef process = this.getWProcessDefByPK(processDefId, currentUserId);
		Integer processHeadId = process.getProcess().getId();
		
		// se borrarán en cascada (por el mapping) los roles y users relacionados con el process
		new WProcessDefDao().delete(process, currentUserId);
		logger.info("The WProcessDef " + process.getName() + " has been correctly deleted by user " + currentUserId);

		List<String> deletedSteps = this._deleteRelatedSteps(stepsToDelete, processHeadId, processDefId, currentUserId);
		
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

	/**
	 * delete stepDef list (step by step)
	 * 
	 * @author dml 20130507
	 * @param stepList
	 * @param processHeadId
	 * @param processId
	 * @param currentUserId
	 * @return
	 * @throws WProcessDefException
	 */
	private List<String> _deleteRelatedSteps(
			List<WStepDef> stepList, Integer processHeadId, Integer processId, Integer currentUserId) 
					throws WProcessDefException {
				
		WStepDefBL wsdBL = new WStepDefBL();
		List<String> deletedSteps = new ArrayList<String>();
		
		if (stepList != null
				&& !stepList.isEmpty()){
			
			for (WStepDef stepDef : stepList){
				
				try {
					
					wsdBL.delete(stepDef.getId(), processHeadId, currentUserId); // nes 20130808 - por agregado de filtro para step-data-field
					deletedSteps.add(stepDef.getName());						
					logger.info("The WStepDef " + stepDef.getName() + " has been correctly deleted by user " + currentUserId);
					
				} catch (WStepDefException e) {
					String mess = "WStepDefException: there is not possible delete step def "+ stepDef.getName() + " Error:"+e.getMessage()+" - "+e.getCause();
					logger.error(mess);
					throw new WProcessDefException(mess);
				} catch (WProcessDefException e) {
					String mess = "WProcessDefException: there is not possible delete step def "+ stepDef.getName() + " Error:"+e.getMessage()+" - "+e.getCause();
					logger.error(mess);
					throw new WProcessDefException(mess);
				} catch (WStepSequenceDefException e) {
					String mess = "WStepSequenceDefException: there is not possible delete step def "+ stepDef.getName() + " Error:"+e.getMessage()+" - "+e.getCause();
					logger.error(mess);
					throw new WProcessDefException(mess);
				} catch (WStepWorkSequenceException e) {
					String mess = "WStepWorkSequenceException: there is not possible delete step def "+ stepDef.getName() + " Error:"+e.getMessage()+" - "+e.getCause();
					logger.error(mess);
					throw new WProcessDefException(mess);
				} catch (WStepWorkException e) {
					String mess = "WStepWorkException: there is not possible delete step def "+ stepDef.getName() + " Error:"+e.getMessage()+" - "+e.getCause();
					logger.error(mess);
					throw new WProcessDefException(mess);
				} catch (WStepHeadException e) {
					String mess = "WStepHeadException: there is not possible delete step def "+ stepDef.getName() + " Error:"+e.getMessage()+" - "+e.getCause();
					logger.error(mess);
					throw new WProcessDefException(mess);
				}
			
			}

		} else {
			logger.info("trying to delete an empty step def list .... none to delete ...");
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

	/**
	 * Load step def list for a processDefId and checks for each step if it is
	 * used in another process o if there is any work (task) using this step.
	 * 
	 * @param processDefId
	 * @param currentUserId
	 * @return
	 * @throws WProcessDefException
	 */
	private List<WStepDef> _loadListOfStepsToDelete(Integer processDefId, Integer currentUserId) 
			throws WProcessDefException {
		
		List<WStepDef> returnValue = new ArrayList<WStepDef>();
		
		Integer ALL_PROCESSES = null;
		
		try {
			
			// load step def list
			List<WStepDef> stepDefList = loadStepList(processDefId, currentUserId);
			
			
			for (WStepDef stepDef: stepDefList) {
				
				if ( !checkSharedStep(stepDef.getId(), processDefId, currentUserId) 
						&& !checkWorkReferral(stepDef.getId(), ALL_PROCESSES, currentUserId) ) {
					
					returnValue.add(stepDef);
					
				}
				
			}
			
		} catch (WStepDefException e) {
			String mess = "can't obtain step def list for given process id:"+processDefId;
			throw new WProcessDefException(mess);
		}
		
		return returnValue;
		
	}
	

	/**
	 * Check if a step is shared by more than 1 process (definition level)
	 * 
	 * @param stepDefId
	 * @param processDefId
	 * @param currentUserId
	 * @return
	 * @throws WProcessDefException
	 */
	public boolean checkSharedStep(Integer stepDefId, Integer processDefId, Integer currentUserId) 
			throws WProcessDefException {
		
		try {
			
			return new WStepDefBL().stepIsShared(stepDefId, currentUserId);
			
		} catch (WStepDefException e) {
			String mess = "WStepDefException: Error checking shared steps for process id:"+processDefId;
			logger.error(mess);
			throw new WProcessDefException(mess);

		} catch (Exception e) {
			String mess = "Exception: Error checking shared steps for process id:"+processDefId;
			logger.error(mess);
			throw new WProcessDefException(mess);
		}

	}
	
	/**
	 * check if a step has works for another processDefId that given one
	 * 
	 * @param stepDefId
	 * @param processDefId
	 * @param currentUserId
	 * @return
	 * @throws WProcessDefException
	 */
	public boolean checkWorkReferral(Integer stepDefId, Integer processDefId, Integer currentUserId) 
			throws WProcessDefException {

		try {
			
			return new WStepWorkBL()
				.stepDefHasWorksRelated(stepDefId, processDefId, currentUserId);
			
		} catch (WProcessDefException e) {
			String mess = "Error checking works for process def id:"+processDefId;
			throw new WProcessDefException(mess);
		} catch (WStepWorkException e) {
			String mess = "Error checking works for process def id:"+processDefId;
			throw new WProcessDefException(mess);
		}

	}
	
	// dml 20130506
	public void deactivateProcess(Integer processId, Integer currentUserId) 
			throws WProcessDefException, WStepSequenceDefException{
		
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
	 * @param boolean startsNewVersionAtCurrent -> true: new version =1, false: new version = current version
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
			Integer processDefId, Integer processHeadId, boolean startsNewVersionAtCurrent, boolean emptyRoleList, 
			boolean emptyUserList, int createNewClonedSteps, boolean emptyWorkflowMap, Integer currentUserId ) 
			throws  WProcessHeadException, WStepSequenceDefException, WProcessDefException  {
		
		Integer clonedId=null, newversion=1;
		WProcessDef newprocver,procver;
		WProcessHeadManagedDataConfiguration mdf=null;
		Set<WProcessDataField> dataFieldDef=null;
		List<WStepSequenceDef> routes = new ArrayList<WStepSequenceDef>();
		List<IntegerPair> relClonedSequences=null;
		List<IntegerPair> relClonedSteps=null;
		try {
			newprocver = this.getWProcessDefByPK(processDefId, currentUserId);
			procver = this.getWProcessDefByPK(processDefId, currentUserId);
			newversion=(startsNewVersionAtCurrent?getLastVersionNumber(processHeadId, currentUserId)+1:1);				
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
			
			// initialize newprocver object:
			
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
		
		// if createNewClonedSteps parameter then clone step list ...
		if (createNewClonedSteps==1) {
			// clonar cada paso y reacomodar la nueva secuencia (porque las secuencias tendrán q referir a los nuevos pasos
			relClonedSteps=
					cloneStepList(
							newprocver.getProcess().getId(), // head id
							clonedId, // new process def id
							procver.getlSteps(), // step list
							currentUserId);
			
		} else if (createNewClonedSteps==2) {
			// clean step && sequence list
		} 
		
		// clone step sequence list
		relClonedSequences = cloneCurrentRoutes(processDefId,
					currentUserId, clonedId, newprocver, routes, relClonedSteps);
		

		// to be implemented: migrate xml map
		// load xml map and for each sequence update edge's spIds
		if (relClonedSequences != null) {
			
			String oldXmlMap = this.getProcessDefXmlMap(processDefId, currentUserId);
			
			String newXmlMap = this.reloadXmlMapSpIds(oldXmlMap, clonedId, 
					relClonedSteps, relClonedSequences, currentUserId);
			
			this.updateProcessXmlMap(clonedId, newXmlMap, currentUserId);
			
		}
		
		return clonedId;
	}

	// clone each step and update routes with new stepId
	private List<IntegerPair> cloneStepList(Integer processHeadId,
			Integer clonedProcessDefId, List<WStepDef> stepList, Integer currentUserId) 
					throws WStepSequenceDefException {

		// clone steps
		
		WStepDefBL stepDefBL = new WStepDefBL();
		
		List<IntegerPair> relClonedSteps = new ArrayList<IntegerPair>(); // reserve step id pairs to migrate map and routes
		
		int newStepId=0;

		try {

			if (stepList.size()>0){
				
				for (WStepDef step:stepList) {
					
					try {
						newStepId = 
								stepDefBL.cloneWStepDef(
										step.getId(), 
										step.getStepHead().getId(), 
										clonedProcessDefId, 
										processHeadId,
										GENERATE_NEW_STEP,  DONT_CLONE_ROUTES,
										CLONE_RESPONSES, CLONE_PERMISSIONS, currentUserId);

						relClonedSteps.add(new IntegerPair(step.getId(),newStepId));

						logger.debug("inserted (cloned) new step:"+newStepId+" for old step id:"+step.getId()+" user:"+ currentUserId);

					} catch (WStepHeadException e) {
						String mess = "WStepHeadException: Error cloning stepDef "
											+step.getId()+"for new process id:"+clonedProcessDefId+" "
											+e.getMessage()+" - "+e.getCause();
						logger.error(mess);
					} catch (WStepDefException e) {
						String mess = "WStepDefException: Error cloning stepDef "
								+step.getId()+"for new process id:"+clonedProcessDefId+" "
								+e.getMessage()+" - "+e.getCause();
						logger.error(mess);
					} 
				}
			}			

		} catch (WStepSequenceDefException e) {
			String mess = "WStepSequenceDefException: Error cloning stepDef for new process id:"+clonedProcessDefId+" "
						+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WStepSequenceDefException(mess);
		}
		
		if (relClonedSteps.size()<1) relClonedSteps=null;
		return relClonedSteps;
	}
	
	/**
	 *  clone existing routes and insert in sequence table
	 *  
	 * @param processDefId
	 * @param currentUserId
	 * @param clonedId
	 * @param newprocver
	 * @param routes
	 * @param relClonedSteps >> list of id pairs of cloned steps (originalId, newId)
	 * @return
	 * @throws WStepSequenceDefException
	 */
	private List<IntegerPair> cloneCurrentRoutes(Integer processDefId,
			Integer currentUserId, Integer clonedId, WProcessDef newprocver,
			List<WStepSequenceDef> routes, List<IntegerPair> relClonedSteps) 
					throws WStepSequenceDefException {
		
		// clone routes
		WStepSequenceDefBL seqBL = new WStepSequenceDefBL();
		List<IntegerPair> relClonedSequences = new ArrayList<IntegerPair>(); // reserve seq id pairs to migrate map
		
		boolean stepsWasCloned = relClonedSteps!=null;
		
		try {
			routes = new WStepSequenceDefBL()
							.getStepSequenceList(processDefId, null, currentUserId);

			if (routes != null && routes.size()>0){
				for (WStepSequenceDef route: routes) {
					route.setProcess(new WProcessDef());
					route.getProcess().setId(clonedId);
					if (stepsWasCloned && route.getFromStep()!=null) {
						route.setFromStep(new WStepDef(getNewStepId(relClonedSteps,route.getFromStep().getId() ) ) );
					}
					if (stepsWasCloned && route.getToStep()!=null) {
						route.setToStep(new WStepDef(getNewStepId(relClonedSteps,route.getToStep().getId() ) ) );
					}

					Integer oldRouteId = route.getId();
					int newRouteId=seqBL.add(route, currentUserId); // insert new cloned route
					relClonedSequences.add(new IntegerPair(oldRouteId, newRouteId));
					logger.debug("inserted new route:"+newRouteId+":"+route.getProcess().getId()+" user:"+ currentUserId);
				}
			}			
		} catch (WStepSequenceDefException e) {
			String mess = "Error cloning routes for old process id:"+processDefId+"  newProcessId:"+newprocver
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WStepSequenceDefException(mess);
		}
		return relClonedSequences;
	}

	// returns new cloned step if from previous step id ...
	private Integer getNewStepId(List<IntegerPair> relClonedSteps, int previousStepId ) {
		for (IntegerPair ip: relClonedSteps) {
			if ( ip.getInt1() == previousStepId ) return ip.getInt2();
		}
		return previousStepId;
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
			
			// initialize managed data configuration in process head
			mdf.setComment(
					(mdf.getComment()!=null?mdf.getComment():"")
					+" cloned from headid "+procver.getProcess().getId()+" ");
			mdf.setHeadId(0);
			mdf.setName(null);
			newprocver.getProcess().setManagedTableConfiguration(mdf);
			
			// clean data field list in process head
			newprocver.getProcess().setProcessDataFieldDef(null);
			
			// updates new process to create managed table
			update( newprocver,currentUserId );	
			
			// if exists data field list then add it (via WProcessDataFieldBL)
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

				// reload newprocver object with process data field list
				try {
					newprocver=getWProcessDefByPK(newprocver.getId(), currentUserId);
				} catch (WStepSequenceDefException e) {
					String mess="buildManagedTable: Error WStepSequenceDefException reloading newprocver after adding new process data field to cloned process "+e.getMessage()+" - "+e.getCause(); 
					logger.error(mess);
					throw new WProcessDefException(mess);
				}
			}
			
			// create managed table
			createManagedTable(newprocver.getProcess().getManagedTableConfiguration().getSchema(),
								newprocver.getProcess().getManagedTableConfiguration().getName(),
								newprocver.getProcess().getProcessDataFieldDefAsList());
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
	
	public String reloadXmlMapSpIds(String processXmlMap, Integer newProcessId, 
			List<IntegerPair> relClonedSteps, List<IntegerPair> relClonedSequences, Integer currentUserId) 
	{
		
		String returnValue = null;
		
		if (processXmlMap == null){
			return returnValue;
		}
		
		mxGraph graph = new mxGraph();
		
		Document xmlParsedDoc = mxXmlUtils.parseXml(processXmlMap);
		mxCodec dec = new mxCodec(xmlParsedDoc.getDocumentElement().getOwnerDocument());
		dec.decode(xmlParsedDoc.getDocumentElement(), graph.getModel());
		
		((mxCell) graph.getModel().getRoot()).setAttribute("spId", newProcessId.toString());
		
		if (relClonedSteps != null){

			// change old step spIds to new ones

		}
		
		if (relClonedSequences != null){
			
			List<Object> xmlMapEdgeList = new ArrayList<Object>(
					Arrays.asList(mxGraphModel.getChildCells(graph.getModel(), graph.getDefaultParent(), false, true)));

			String spId = "";
			boolean existSpId = false;
			mxCell xmlMapEdge = null;
			for (Object xmlMapEdgeObject : xmlMapEdgeList){
				
				xmlMapEdge = (mxCell) xmlMapEdgeObject;
				
				if (xmlMapEdge.isEdge()){

					spId = xmlMapEdge.getAttribute("spId");
					
					if (spId != null
							&& !"".equals(spId)){
						
						for (IntegerPair routeIds : relClonedSequences){
							
							existSpId = false;
							if (routeIds.getInt1().equals(Integer.valueOf(spId))){
								
								xmlMapEdge.setAttribute("spId", routeIds.getInt2().toString());
								existSpId = true;
								break;
								
							}
							
						}
						
						if (!existSpId){
							xmlMapEdge.setAttribute("spId", "");
						}
						
					}
					
				}
			
			}
			
			
		}
				
		// lo codificamos para devolverlo
		returnValue = mxXmlUtils.getXml(new mxCodec().encode(graph.getModel()));
			
		return returnValue;

	}
	
	// nes 20130502 - traido desde el backing bean ...
	private List<WStepDef> loadStepList(Integer processId, Integer currentUserId) 
			throws WStepDefException {

		List<WStepDef> lsteps=new ArrayList<WStepDef>();
		try {

			if (processId != null && processId != 0) {

				lsteps = new WStepDefBL().getStepDefs(processId, null, currentUserId);

			}
		} catch (WStepDefException e) {
			
			lsteps=null;
			String mess = "Error loading step list:"+ e.getMessage() + " - " + e.getCause();
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
	
	/**
	 * creates managed table (if doesn't exist - if exist throws error)
	 * 
	 * @param schemaName
	 * @param tableName
	 * @param dataFieldList
	 * @throws WProcessDefException 
	 */
	public void createManagedTable(String schemaName, String tableName, List<WProcessDataField> dataFieldList) 
			throws WProcessDefException {
		try {
			new TableManagerBL()
					.createManagedTable(
							schemaName, 
							tableName, 
							dataFieldList);
		} catch (TableAlreadyExistsException e) {
			String message = "WProcessDefFormBean.createManagedTable() TableAlreadyExistsException: " + 
					e.getMessage() + " - " + e.getCause();
			logger.error(message);
			throw new WProcessDefException(message);
		
		} catch (TableManagerException e) {
			String message = "WProcessDefFormBean.createManagedTable() TableManagerException: " + 
					e.getMessage() + " - " + e.getCause();
			logger.error(message);
			throw new WProcessDefException(message);
		}
	}
	
	public boolean hasVersions(Integer processHeadId) throws WProcessDefException {
		return new WProcessDefDao().hasVersions(processHeadId);
	}
}
	