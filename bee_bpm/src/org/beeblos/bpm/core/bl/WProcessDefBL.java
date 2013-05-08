package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.ALL;
import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;
import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;
import static org.beeblos.bpm.core.util.Constants.FIRST_WPROCESSDEF_VERSION;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessDefDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessRole;
import org.beeblos.bpm.core.model.WProcessUser;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;



public class WProcessDefBL {
	
	private static final Log logger = LogFactory.getLog(WProcessDefBL.class.getName());
	
	public WProcessDefBL (){
		
	}
	
	public Integer add(WProcessDef process, Integer currentUserId) throws WProcessDefException, WProcessException {
		
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
		return new WProcessDefDao().add(process);

	}
	
	// dml 20130430
	private void _setFirstWProcessDefData(WProcessDef process, Integer processHeadId, Integer currentUserId) throws WProcessException {
		
		if (process != null){
			
			process.setActive(true);

			process.setProcess(new WProcessHeadBL().getProcessByPK(processHeadId, currentUserId));
			
			if (process.getVersion() == null
					|| process.getVersion().equals(0)){
				process.setVersion(FIRST_WPROCESSDEF_VERSION);
			}
		
		}
		
	}
	
	// dml 20130430
	public void createFirstWProcessDef(Integer processHeadId, Integer currentUserId) throws WProcessDefException, WProcessException{

		WProcessDef wpd = new WProcessDef(EMPTY_OBJECT);

		this._setFirstWProcessDefData(wpd, processHeadId, currentUserId);
		
		this.add(wpd, currentUserId);
		
	}
	
	public void update(WProcessDef process, Integer currentUserId) throws WProcessDefException {
		
		logger.debug("update() WProcessDef < id = "+process.getId()+">");
		
		if (!process.equals(new WProcessDefDao().getWProcessDefByPK(process.getId())) ) {

			// timestamp & trace info
			process.setModDate(new Date());
			process.setModUser(currentUserId);
			new WProcessDefDao().update(process);
			
		} else {
			
			logger.debug("WProcessDefBL.update - nothing to do ...");
		}
			
	}
	
	public List<String> delete(Integer processId, boolean deleteRelatedSteps, Integer currentUserId) 
			throws WProcessWorkException, WProcessDefException, WStepSequenceDefException, 
			WStepWorkException, WProcessException, WStepDefException {

		logger.debug("delete() WProcessDef - Name: ["+processId+"]");
		
		if (processId==null || processId==0) {
			String mess = "Trying delete process with id null or 0 ...";
			logger.error(mess);
			throw new WProcessWorkException(mess);
		}
		
		Integer qtyWorks;
		try {
			qtyWorks = new WStepWorkBL().getWorkCount(processId,ALL);
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
		
		
		this._deleteRelatedSequences(processId, currentUserId);
		
		WProcessDef process = this.getWProcessDefByPK(processId, currentUserId);
		Integer processHeadId = process.getProcess().getId();
		
		// se borrarán en cascada (por el mapping) los roles y users relacionados con el process
		new WProcessDefDao().delete(process);
		logger.info("The WProcessDef " + process.getName() + " has been correctly deleted by user " + currentUserId);

		List<String> deletedSteps = this._deleteRelatedSteps(stepsToDelete, currentUserId);
		
		this._checkAndDeleteProcessHead(processHeadId, currentUserId);
		
		

		return deletedSteps;
		
	}
	
	// dml 20130507
	private void _deleteRelatedSequences(Integer processId, Integer currentUserId) throws WProcessWorkException{
				
		List<WStepSequenceDef> wssdList;
		WStepSequenceDefBL wssdBL = new WStepSequenceDefBL();
		
		try {
			
			wssdList = new WStepSequenceDefBL().getStepSequenceList(processId, null);
			
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
		}
		
	}

	// dml 20130507
	private List<String> _deleteRelatedSteps(List<WStepDef> stepList, Integer currentUserId) throws WStepDefException {
				
		WStepDefBL wsdBL = new WStepDefBL();
		List<String> deletedSteps = new ArrayList<String>();
		
		try {
			
			if (stepList != null
					&& !stepList.isEmpty()){
				
				for (WStepDef wsd : stepList){
					
					deletedSteps.add(wsd.getName());
					wsdBL.delete(wsd, currentUserId);
					logger.info("The WStepDef " + wsd.getName() + " has been correctly deleted by user " + currentUserId);
					
				}
				
			}
			
		} catch (WStepDefException e) {
			String mess = "Impossible to delete step defs";
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		
		return deletedSteps;
		
	}

	// dml 20130507
	private void _checkAndDeleteProcessHead(Integer processHeadId, Integer currentUserId) throws WProcessException{
		
		WProcessHeadBL wpBL = new WProcessHeadBL();
		
		if (!wpBL.headProcessHasWProcessDef(processHeadId)){
			
			wpBL.delete(wpBL.getProcessByPK(processHeadId, currentUserId), currentUserId);
			logger.info("The WProcessHead " + processHeadId + " has been correctly deleted by user " + currentUserId);
			
		}
		
	}

	private List<WStepDef> _checkAndDeleteRelatedSteps(Integer processId, Integer userId) 
			throws WProcessWorkException, WStepSequenceDefException, WStepWorkException {
		
		List<WStepDef> returnValue = new ArrayList<WStepDef>();
		try {
			
			List<WStepDef> stepDefList = loadStepList(processId, userId);
			
			for (WStepDef stepDef: stepDefList) {
				
				if ( !checkSharedStep(stepDef.getId(), processId, userId) 
						&& !checkWorkReferral(stepDef.getId(),processId, userId) ) {
					
					returnValue.add(stepDef);
					
				}
				
			}
			
		} catch (WStepDefException e) {
			String mess = "can't obtain step def list for given process id:"+processId;
			throw new WProcessWorkException(mess);
		}
		
		
		return returnValue;
	}
	

	public boolean checkSharedStep(Integer stepId, Integer processId, Integer userId) throws WStepSequenceDefException {
		
		try {

			return new WStepDefBL().isAnotherProcessUsingStep(stepId, processId, userId);
		
		} catch (Exception e) {
			String mess = "Error checking shared steps for process id:"+processId;
			throw new WStepSequenceDefException(mess);
		}

	}
	
	public boolean checkWorkReferral(Integer stepId, Integer processId, Integer userId) throws WStepWorkException {
		
		try {

			return new WStepWorkBL().isAnotherProcessUsingWorkStep(stepId, processId, userId);
			
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
		wpd = new WProcessDefDao().getWProcessDefByPK(id);
		
		if (wpd != null){
			
			try {
				wpd.setlSteps(loadStepList(wpd.getId(),currentUserId));
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
	
	
	public WProcessDef getWProcessDefByName(String name, Integer currentUserId) throws WProcessDefException, WStepSequenceDefException {

		WProcessDef wpd;
		wpd = new WProcessDefDao().getWProcessDefByName(name);
		
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

	// dml 20130506
	public List<WProcessDef> getProcessList(Integer currentUserId) throws WProcessDefException {

		List<WProcessDef> wpdList = new WProcessDefDao().getWProcessDefs();
		
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
	public Integer getLastVersionNumber(Integer processHeadId) throws WProcessDefException {

		return new WProcessDefDao().getLastVersionNumber(processHeadId);
	
	}
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WProcessDefException {
		 
		return new WProcessDefDao().getComboList(textoPrimeraLinea, separacion);
		
	}

	// dml 20120120
	public List<StringPair> getComboActiveProcessList(String firstLineText, String blank )
	throws WProcessDefException {
	
		return new WProcessDefDao().getComboActiveProcessList(firstLineText, blank);
	
	}	
	
	public List<WProcessDef> getProcessListByFinder (Date initialInsertDateFilter, Date finalInsertDateFilter, 
			boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
			String listZoneFilter, String workZoneFilter, String additinalZoneFilter,
			Integer userId, boolean isAdmin, String action ) 
	throws WProcessDefException {
		
		return new WProcessDefDao().getProcessListByFinder(initialInsertDateFilter, finalInsertDateFilter, 
				strictInsertDateFilter, nameFilter, commentFilter, listZoneFilter, 
				workZoneFilter, additinalZoneFilter, userId, isAdmin, action);

	}

	public List<WProcessDefLight> getWorkingProcessListFinder(boolean onlyWorkingProcessesFilter, 
			String processNameFilter, Date initialProductionDateFilter, Date finalProductionDateFilter, 
			boolean estrictProductionDateFilter, Integer productionUserFilter, String action)
	throws WProcessDefException {
		
		return new WProcessDefDao().getWorkingProcessListFinder(onlyWorkingProcessesFilter, 
				processNameFilter, initialProductionDateFilter, finalProductionDateFilter,
				estrictProductionDateFilter, productionUserFilter, action);
		
	}

	public String getProcessNameByVersionId(Integer id, Integer currentUserId) 
			throws WProcessDefException, WStepSequenceDefException {

		return new WProcessDefDao().getProcessNameByVersionId(id);
	
	}
	
	
	// clone a process version (w_process_def, related users and roles, related steps and routes ...)
	// returns new process id
	public Integer cloneWProcessDef(Integer processId, Integer processHeadId, Integer userId) 
			throws  WProcessException, WStepSequenceDefException, WProcessDefException  {
		
		Integer clonedId=null,newversion=0;
		WProcessDef newprocver,procver;
		List<WStepSequenceDef> routes = new ArrayList<WStepSequenceDef>();
		try {
			newprocver = this.getWProcessDefByPK(processId,userId);
			procver = this.getWProcessDefByPK(processId,userId);
			newversion=this.getLastVersionNumber(processHeadId)+1;
		} catch (WProcessDefException e) {
			String mess = "Error cloning process version: can't get original process version id:"+processId
							+" - "+e.getMessage()+" - "+e.getCause();
			throw new WProcessDefException(mess);
		} catch (WStepSequenceDefException e) {
			String mess = "Error cloning process version: can't get original process version id:"+processId
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WStepSequenceDefException(mess);
		}
		
		try {
			newprocver.setId(null);
			newprocver.setVersion(newversion); // new version number ....
			newprocver.setRolesRelated(new HashSet<WProcessRole>());
			newprocver.setUsersRelated(new HashSet<WProcessUser>());
			
			if ( procver.getRolesRelated().size()>0) {
				for ( WProcessRole processRole: procver.getRolesRelated() ) {
					processRole.setProcess(null);
					newprocver
						.addRole(
								processRole.getRole(), processRole.isAdmin(), 
								processRole.getIdObject(), processRole.getIdObjectType(), 
								userId);
				}
			}

			if ( procver.getUsersRelated().size()>0) {
				for ( WProcessUser processUser: procver.getUsersRelated() ) {
					processUser.setProcess(null);
					newprocver
						.addUser(
								processUser.getUser(), processUser.isAdmin(), 
								processUser.getIdObject(), processUser.getIdObjectType(), 
								userId);
				}
			}
			
		} catch (Exception e) {
			String mess = "Error cloning process version:"+processId+" can't clone related role or user list ..."
					+" - "+e.getClass()+" -" +e.getMessage()+" - "+e.getCause();
			throw new WProcessDefException(mess);
		}
			
		try {
			clonedId = this.add(newprocver,userId);
		} catch (WProcessDefException e) {
			String mess = "Error cloning process version: can't ADD new clone process version original-id:"+processId
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WProcessDefException(mess);
		} catch (WProcessException e) {
			String mess = "Error cloning process version: can't ADD new clone process version original-id:"+processId
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WProcessException(mess);
		}			
		
		// clone routes (the workflow map really ...)
		WStepSequenceDefBL seqBL = new WStepSequenceDefBL();
		try {
			routes = new WStepSequenceDefBL().getStepSequenceList(processId, userId);
			if (routes.size()>0){
				for (WStepSequenceDef route: routes) {
					route.setProcess(new WProcessDef());
					route.getProcess().setId(clonedId);
					seqBL.add(route, userId); // insert new cloned route
					logger.debug("inserted new route:"+route.getId()+":"+route.getProcess().getId()+" user:"+userId);
				}
			}			
		} catch (WStepSequenceDefException e) {
			String mess = "Error cloning routes for old process id:"+processId+"  newProcessId:"+newprocver
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WStepSequenceDefException(mess);
		}

		
		return clonedId;
	}
	
	// dml 20130129
	public boolean userIsProcessAdmin(Integer userId, Integer processId) throws WProcessDefException{
		
		return new WProcessDefDao().userIsProcessAdmin(userId, processId);
		
	}
	
	
	// nes 20130502 - traido desde el backing bean ...
	private List<WStepDef> loadStepList(Integer processId, Integer userId) 
			throws WStepDefException {

		List<WStepDef> lsteps=new ArrayList<WStepDef>();
		try {

			if (processId != null && processId != 0) {

				lsteps = new WStepDefBL().getStepDefs(processId, userId);

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

				// dml 20120326 NOTA: LA VERSION ESTA HARDCODEADA A 1
				routes = new WStepSequenceDefBL().getStepSequenceList(processId, currentUserId);
				
			}
			
		} catch (WStepSequenceDefException ex1) {

			routes=null;
			String mess = "Error loading StepSequenceList:"+ ex1.getMessage() + " - " + ex1.getCause();
			throw new WStepSequenceDefException(mess);

		}
		return routes;
	}
}
	