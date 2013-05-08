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
import org.beeblos.bpm.core.dao.WStepDefDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepHeadException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepUser;
import org.beeblos.bpm.core.model.noper.StringPair;



public class WStepDefBL {
	
	private static final Log logger = LogFactory.getLog(WStepDefBL.class.getName());
	
	public WStepDefBL (){
		
	}
	
	public Integer add(WStepDef step, Integer currentUserId) throws WStepDefException, WStepHeadException {
		
		logger.debug("add() WStepDef - Name: ["+step.getName()+"]");
		
		// dml 20130430 - si es un nuevo WStepHead se guarda antes de guardar el WStepDef y se rellena la informacion esencial
		if (step.getStepHead() != null
				&& (step.getStepHead().getId() == null
				|| step.getStepHead().getId().equals(0))){
			
			Integer stepHeadId = new WStepHeadBL().add(step.getStepHead(), currentUserId);
			
			this._setFirstWStepDefData(step, stepHeadId, currentUserId);
			
			
		}
		
		// timestamp & trace info
		step.setInsertDate(new Date());
		step.setModDate( DEFAULT_MOD_DATE );
		step.setInsertUser(currentUserId);
		step.setModUser(currentUserId);
		return new WStepDefDao().add(step);

	}
	
	// dml 20130430
	private void _setFirstWStepDefData(WStepDef step, Integer stepHeadId, Integer currentUserId) throws WStepHeadException {
		
		if (step != null){
			
//			step.setActive(true);

			step.setStepHead(new WStepHeadBL().getWStepHeadByPK(stepHeadId, currentUserId));
			
			if (step.getVersion() == null
					|| step.getVersion().equals(0)){
				step.setVersion(FIRST_WPROCESSDEF_VERSION);
			}
		
		}
		
	}
	
	// dml 20130430
	public void createFirstWStepDef(Integer stepHeadId, Integer currentUserId) throws WStepDefException, WStepHeadException{

		WStepDef wpd = new WStepDef(EMPTY_OBJECT);

		this._setFirstWStepDefData(wpd, stepHeadId, currentUserId);
		
		this.add(wpd, currentUserId);
		
	}

	public void update(WStepDef step, Integer currentUserId) throws WStepDefException {
		
		logger.debug("update() WStepDef < id = "+step.getId()+">");
		
		if (!step.equals(new WStepDefDao().getStepDefByPK(step.getId())) ) {

			// timestamp & trace info
			step.setModDate(new Date());
			step.setModUser(currentUserId);
			new WStepDefDao().update(step);
			
		} else {
			
			logger.debug("WStepDefBL.update - nothing to do ...");
		}
		

					
	}
	
	
	public void delete(Integer stepId, Integer currentUserId) throws WStepDefException, WStepWorkException, WStepHeadException  {

		logger.debug("delete() WProcessDef - Name: ["+stepId+"]");
		
		if (stepId==null || stepId==0) {
			String mess = "Trying delete step with id null or 0 ...";
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		
		Integer qtyWorks;
		try {
			qtyWorks = new WStepWorkBL().getWorkCountByStep(stepId,ALL);
		} catch (WStepWorkException e) {
			String mess = "Error verifiyng existence of works related with this step id:"+stepId
					+ " "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		
		if (qtyWorks > 0) {
			String mess = "Delete step with works is not allowed ... This step has "+qtyWorks+" works";
			logger.error(mess);
			throw new WStepWorkException(mess);
		}
/* NO ESTAN EN LOS PROCESOS			
		try {
			qtyWorks = new WProcessWorkBL().getWorkCount(stepId,ALL);
		} catch (WProcessWorkException e) {
			String mess = "Error verifiyng existence of works related with this process id:"+stepId
					+ " "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WProcessWorkException(mess);
		}
		
		if (qtyWorks>0) {
			String mess = "Delete process with works is not allowed ... This process has "+qtyWorks+" works";
			logger.error(mess);
			throw new WProcessWorkException(mess);
		}
*/			

//		this._deleteRelatedSequences(stepId, currentUserId);
		
		WStepDef step = this.getWStepDefByPK(stepId, currentUserId);
		Integer stepHeadId = step.getStepHead().getId();
		
		// se borrarán en cascada (por el mapping) los roles y users relacionados con el process
		new WStepDefDao().delete(step);
		logger.info("The WProcessDef " + step.getName() + " has been correctly deleted by user " + currentUserId);

		this._checkAndDeleteStepHead(stepHeadId, currentUserId);
		
	}
	
	// dml 20130507
	private void _deleteRelatedSequences(Integer stepId, Integer currentUserId) throws WProcessWorkException{
				
		List<WStepSequenceDef> wssdList;
		WStepSequenceDefBL wssdBL = new WStepSequenceDefBL();
		
		try {
			
			wssdList = new WStepSequenceDefBL().getStepSequenceList(stepId, null);
			
			if (wssdList != null
					&& !wssdList.isEmpty()){
				
				for (WStepSequenceDef wssd : wssdList){
					
					wssdBL.deleteRoute(wssd, currentUserId);
					
				}
				
			}
			
		} catch (WStepSequenceDefException e) {
			String mess = "Impossible to delete process " + stepId + " step sequence defs";
			logger.error(mess);
			throw new WProcessWorkException(mess);
		}
		
	}

	// dml 20130507
	private void _checkAndDeleteStepHead(Integer stepHeadId, Integer currentUserId) throws WStepHeadException{
		
		WStepHeadBL wsBL = new WStepHeadBL();
		
		if (!wsBL.headStepHasWStepDef(stepHeadId)){
			
			wsBL.delete(wsBL.getWStepHeadByPK(stepHeadId, currentUserId), currentUserId);
			logger.info("The WStepHead " + stepHeadId + " has been correctly deleted by user " + currentUserId);
			
		}
		
	}
	
	public void deleteStepRole(WStepDef step, WStepRole wsr) throws WStepDefException {

//		logger.debug("delete() WStepDef - Name: ["+step.getName()+"] id:["+step.getId()+"] wsteprole.role:"+wsr.getRole().getId()+" wsteprolw.step:"+wsr.getStep().getId());
		
//		if ( wsr.getStep().getId() != step.getId() ) {
//			throw new WStepDefException("can't delete wsteprole id:"+wsr.getStep().getId()+ " for this step with id:"+step.getId() );
//		}
		
		new WStepDefDao().deleteStepRole(step,wsr);

	}
	
	// clone a process version (w_process_def, related users and roles, related steps and routes ...)
	// returns new process id
	public Integer cloneWStepDef(Integer stepId, Integer stepHeadId, Integer userId) 
			throws  WStepHeadException, WStepSequenceDefException, WStepDefException  {
		
		Integer clonedId=null,newversion=0;
		WStepDef newstepver,stepver;
		List<WStepSequenceDef> routes = new ArrayList<WStepSequenceDef>();
		try {
			newstepver = this.getWStepDefByPK(stepId,userId);
			stepver = this.getWStepDefByPK(stepId,userId);
			newversion=this.getLastVersionNumber(stepHeadId)+1;
		} catch (WStepDefException e) {
			String mess = "Error cloning step version: can't get original step version id:"+stepId
							+" - "+e.getMessage()+" - "+e.getCause();
			throw new WStepDefException(mess);
		}
		
		try {
			newstepver.setId(null);
			newstepver.setVersion(newversion); // new version number ....
			newstepver.setRolesRelated(new HashSet<WStepRole>());
			newstepver.setUsersRelated(new HashSet<WStepUser>());
			
			if ( stepver.getRolesRelated().size()>0) {
				for ( WStepRole stepRole: stepver.getRolesRelated() ) {
					stepRole.setStep(null);
					newstepver
						.addRole(
								stepRole.getRole(), stepRole.isAdmin(), 
								stepRole.getIdObject(), stepRole.getIdObjectType(), 
								userId);
				}
			}

			if ( stepver.getUsersRelated().size()>0) {
				for ( WStepUser stepUser: stepver.getUsersRelated() ) {
					stepUser.setStep(null);
					newstepver
						.addUser(
								stepUser.getUser(), stepUser.isAdmin(), 
								stepUser.getIdObject(), stepUser.getIdObjectType(), 
								userId);
				}
			}
			
		} catch (Exception e) {
			String mess = "Error cloning step version:"+stepId+" can't clone related role or user list ..."
					+" - "+e.getClass()+" -" +e.getMessage()+" - "+e.getCause();
			throw new WStepDefException(mess);
		}
			
		try {
			clonedId = this.add(newstepver,userId);
		} catch (WStepDefException e) {
			String mess = "Error cloning process version: can't ADD new clone process version original-id:"+stepId
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WStepDefException(mess);
		} catch (WStepHeadException e) {
			String mess = "Error cloning process version: can't ADD new clone process version original-id:"+stepId
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WStepHeadException(mess);
		}			
		
		// clone routes (the workflow map really ...)
		WStepSequenceDefBL seqBL = new WStepSequenceDefBL();
		try {
			routes = new WStepSequenceDefBL().getStepSequenceList(stepId, userId);
			if (routes.size()>0){
				for (WStepSequenceDef route: routes) {
					route.setProcess(new WProcessDef());
					route.getProcess().setId(clonedId);
					seqBL.add(route, userId); // insert new cloned route
					logger.debug("inserted new route:"+route.getId()+":"+route.getProcess().getId()+" user:"+userId);
				}
			}			
		} catch (WStepSequenceDefException e) {
			String mess = "Error cloning routes for old process id:"+stepId+"  newProcessId:"+newstepver
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WStepSequenceDefException(mess);
		}

		
		return clonedId;
	}
	
	// dml 20130430
	public Integer getLastVersionNumber(Integer stepHeadId) throws WStepDefException {

		return new WStepDefDao().getLastVersionNumber(stepHeadId);
	
	}
	
	public WStepDef getWStepDefByPK(Integer id, Integer userId) throws WStepDefException {

		return new WStepDefDao().getStepDefByPK(id);
	}
	
	
	public WStepDef getWStepDefByName(String name, Integer userId) throws WStepDefException {

		return new WStepDefDao().getStepDefByName(name);
	}

	
	public List<WStepDef> getStepDefs(Integer userId) throws WStepDefException {

		// nota: falta revisar el tema de los permisos de usuario para esto ...
		return new WStepDefDao().getWStepDefs();
	
	}
	
	public List<WStepDef> getStepDefs(Integer stepId, Integer userId) throws WStepDefException {

		return new WStepDefDao().getStepDefs(stepId);
	
	}
	
	// dml 20130507
	public List<Integer> getProcessIdList(Integer stepId, Integer userId) throws WStepDefException{
		
		return new WStepDefDao().getProcessIdList(stepId);
		
	}
	
	// dml 20130507
	public boolean isAnotherProcessUsingStep(Integer stepId, Integer processId, Integer userId) 
			throws WStepDefException, WProcessDefException {

		if (stepId == null
				|| stepId.equals(0)
				|| processId == null
				|| processId.equals(0)){
			String mess = "stepId and processId cannot be null or zero";
			logger.error(mess);
			throw new WProcessDefException(mess);
		}

		List<Integer> processIdList = this.getProcessIdList(stepId, userId);
		
		if (processIdList == null){
			String mess = "Error trying to retrieve the processIdList:"+processId;
			logger.error(mess);
			throw new WProcessDefException(mess);
		}
			
		for (Integer pid : processIdList){
			
			if (!pid.equals(processId)){
				return true;
			}
			
		} 

		return false;
	
	}	
	
	public List<StringPair> getComboList(
			String firstLineText, String blank )
	throws WStepDefException {
		 
		return new WStepDefDao().getComboList(firstLineText, blank);


	}
	
	public List<StringPair> getComboList(
			Integer processId, String firstLineText, String blank )
	throws WProcessDefException {
		
		return new WStepDefDao().getComboList(processId, firstLineText, blank);
		
	}

	// dml 20130129 - new combo method with userId and allItems
	public List<StringPair> getComboList(
			Integer processId, Integer versionId, Integer userId, boolean allItems, 
			String firstLineText, String blank )
	throws WProcessDefException {
		
		// dml 20130129 - checking if the user is process admin
		boolean userIsProcessAdmin = new WProcessDefBL().userIsProcessAdmin(userId, processId);

		return new WStepDefDao().getComboList(processId, versionId, userId, userIsProcessAdmin, allItems, 
				firstLineText, blank);
		
	}

	public List<WStepDef> getStepListByFinder (String nameFilter, String commentFilter, 
			String instructionsFilter, Integer userId, boolean isAdmin, String action, 
			Integer stepHeadId, String activeFilter) 
	throws WStepDefException {
		
		return new WStepDefDao().getStepListByFinder(nameFilter, commentFilter, instructionsFilter, 
				userId, isAdmin, action, stepHeadId, activeFilter);

	}


}
	