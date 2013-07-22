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
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepHeadException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
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
	public Integer createFirstWStepDef(Integer stepHeadId, Integer currentUserId) throws WStepDefException, WStepHeadException{

		WStepDef wsd = new WStepDef();

		this._setFirstWStepDefData(wsd, stepHeadId, currentUserId);
		
		return this.add(wsd, currentUserId);
		
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
	
	public void delete(Integer stepId, Integer currentUserId) 
			throws WStepDefException, WStepWorkException, WProcessDefException, 
			WStepSequenceDefException, WStepHeadException  {

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

		// si el step se esta usando en alguna StepSequence no se permite borrarlo
		if (this.isAnotherProcessUsingStep(stepId, null, currentUserId)) {
			String mess = "Delete step with related step sequence is not allowed ...";
			logger.error(mess);
			throw new WStepSequenceDefException(mess);
		}
		
		WStepDef step = this.getWStepDefByPK(stepId, currentUserId);
		Integer stepHeadId = step.getStepHead().getId();
		
		// se borrarán en cascada (por el mapping) los roles y users relacionados con el step
		new WStepDefDao().delete(step);
		logger.info("The WProcessDef " + step.getName() + " has been correctly deleted by user " + currentUserId);

		this._checkAndDeleteStepHead(stepHeadId, currentUserId);
		
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
	
	/*
	 * clone a step version (w_step_def, related users and roles, related steps and routes ...)
	 * returns new step id
	 * 
	 */
	public Integer cloneWStepDef(Integer stepId, Integer stepHeadId, Integer processId, Integer userId) 
			throws  WStepHeadException, WStepSequenceDefException, WStepDefException  {
		
		boolean newStep=false; // by default generates a new version of current stepId
		boolean cloneRoutes=true;
		boolean cloneResponses=true;
		boolean clonePermissions=true;
		
		return cloneWStepDef( stepId,  stepHeadId,  processId,
				 newStep,  cloneRoutes,	cloneResponses, clonePermissions, userId);
		
	}
	
	/*
	 *clone step with options:
	 *
	 * generateNewStep: indicates must generate a new step with your new step_head and version =1
	 * 	generateNewStep=false -> generates a new version of stepId
	 *  generateNewStep=true -> generates a new step copying data from stepId and version=1
	 * cloneRoutes: indicates must generate new routes copying origin step routes
	 * cloneResponses: indicates must generate new responses copying origin step responses
	 * clonePermissions: indicates must generate new permissions copying origin step permissions
	 * 
	 * processId must passed if only the routes for 1 process will be cloned .... Remember 
	 * WStepSequeceDef has incoming and outgoint routes for a step/process
	 * 
	 */
	public Integer cloneWStepDef(Integer stepId, Integer stepHeadId, Integer processId, 
									boolean generateNewStep, boolean cloneRoutes,
									boolean cloneResponses,
									boolean clonePermissions, Integer userId) 
			throws  WStepHeadException, WStepSequenceDefException, WStepDefException  {
		
		Integer clonedId=null,newVersion=0;
		WStepDef newStep,currentStep;
		
		try {
			
			newStep = this.getWStepDefByPK(stepId,userId);
			currentStep = this.getWStepDefByPK(stepId,userId);

			// new step or new version ...
			if (generateNewStep) {
				newVersion=1;
			} else {
				newVersion=this.getLastVersionNumber(stepHeadId)+1;	
			}
			
		} catch (WStepDefException e) {
			String mess = "Error cloning step version: can't get original step version id:"+stepId
							+" - "+e.getMessage()+" - "+e.getCause();
			throw new WStepDefException(mess);
		}
		
		// cloning permissions ...
		try {

			newStep.setId(null);
			newStep.setVersion(newVersion); // new version number ....
			newStep.setRolesRelated(new HashSet<WStepRole>());
			newStep.setUsersRelated(new HashSet<WStepUser>());
			
			if (clonePermissions) {
				if ( currentStep.getRolesRelated().size()>0) {
					for ( WStepRole stepRole: currentStep.getRolesRelated() ) {
						stepRole.setStep(null);
						newStep
							.addRole(
									stepRole.getRole(), stepRole.isAdmin(), 
									stepRole.getIdObject(), stepRole.getIdObjectType(), 
									userId);
					}
				}

				if ( currentStep.getUsersRelated().size()>0) {
					for ( WStepUser stepUser: currentStep.getUsersRelated() ) {
						stepUser.setStep(null);
						newStep
							.addUser(
									stepUser.getUser(), stepUser.isAdmin(), 
									stepUser.getIdObject(), stepUser.getIdObjectType(), 
									userId);
					}
				}
			}
			
		} catch (Exception e) {
			String mess = "Error cloning step version:"+stepId+" can't clone related role or user list ..."
					+" - "+e.getClass()+" -" +e.getMessage()+" - "+e.getCause();
			throw new WStepDefException(mess);
		}

		// persist new step with permissions ...
		try {
			clonedId = this.add(newStep,userId);
		} catch (WStepDefException e) {
			String mess = "Error cloning step version: can't ADD new clone step version original-id:"+stepId
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WStepDefException(mess);
		} catch (WStepHeadException e) {
			String mess = "Error cloning step version: can't ADD new clone step version original-id:"+stepId
					+" - "+e.getMessage()+" - "+e.getCause();
			throw new WStepHeadException(mess);
		}			
		
		// cloning routes (the workflow map really ...)
		if (cloneRoutes) {
			List<WStepSequenceDef> routes = new ArrayList<WStepSequenceDef>();
			WStepSequenceDefBL seqBL = new WStepSequenceDefBL();
			// clone outgoing routes
			try {
				routes = new WStepSequenceDefBL().getOutgoingRoutes(stepId, processId, userId);
				if (routes.size()>0){
					for (WStepSequenceDef route: routes) {
						route.setFromStep(new WStepDef(clonedId));
						seqBL.add(route, userId); // insert new cloned route
						logger.debug("inserted new route:"+route.getId()+":"+route.getId()+" user:"+userId);
					}
				}			
			} catch (WStepSequenceDefException e) {
				String mess = "Error cloning outgoing routes for old step id:"+stepId+"  newProcessId:"+newStep
						+" - "+e.getMessage()+" - "+e.getCause();
				throw new WStepSequenceDefException(mess);
			}

			// clone ingoing routes
			try {
				routes = new WStepSequenceDefBL().getIncomingRoutes(stepId, processId, userId);
				if (routes.size()>0){
					for (WStepSequenceDef route: routes) {
						route.setToStep(new WStepDef(clonedId));
						seqBL.add(route, userId); // insert new cloned route
						logger.debug("inserted new route:"+route.getId()+":"+route.getId()+" user:"+userId);
					}
				}			
			} catch (WStepSequenceDefException e) {
				String mess = "Error cloning incoming routes for old step id:"+stepId+"  newProcessId:"+newStep
						+" - "+e.getMessage()+" - "+e.getCause();
				throw new WStepSequenceDefException(mess);
			}
			
		
		}
		
		// cloning responses
		if (cloneResponses) {
			if ( currentStep.getResponse().size()>0) {
				for ( WStepResponseDef response: currentStep.getResponse() ) {
					response.setId(null);
					newStep
						.addResponse(response);
				}
			}			
		}
		
		return clonedId;
	}
	
//	private void cloneStepRoutes(Integer sourceStepId, Integer outgoingStepId, Integer processId ) {
//
//		List<WStepSequenceDef> outgoingRoutes;
//		Integer outgoingRoutesSize;
//		List<WStepSequenceDef> incomingRoutes;
//		Integer incomingRoutesSize;
//		WStepSequenceDefBL wssBL = new WStepSequenceDefBL();
//		
//		try {
//			
//			outgoingRoutes = wssBL.getOutgoingRoutes(this.currObjId, currentWStepDefId, getCurrentUserId());
//
//			if (outgoingRoutes != null){
//				outgoingRoutesSize = outgoingRoutes.size();
//			} else {
//				outgoingRoutesSize = 0;
//			}
//			
//		} catch (WStepSequenceDefException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	
	// dml 20130509
	public void deactivateStep(Integer stepId, Integer currentUserId) throws WStepDefException, WStepSequenceDefException{
		
		if (stepId != null
				&& !stepId.equals(0)){
			
			WStepDef wsd = this.getWStepDefByPK(stepId, currentUserId);
			
			if (wsd != null){
				
				if (wsd.isActive()){
					
					wsd.setActive(false);
					this.update(wsd, currentUserId);
					
				} else {

					String message = "The step you're trying to deactivate is already non active.";
					logger.error(message);
					throw new WStepDefException(message);
					
				}
				
			}
			
		}
		
	}

	// dml 20130509
	public void activateStep(Integer stepId, Integer currentUserId) throws WStepDefException, WStepSequenceDefException{
		
		if (stepId != null
				&& !stepId.equals(0)){
			
			WStepDef wsd = this.getWStepDefByPK(stepId, currentUserId);
			
			if (wsd != null){
				
				if (!wsd.isActive()){
					
					wsd.setActive(true);
					this.update(wsd, currentUserId);
					
				} else {

					String message = "The step you're trying to activate is already active.";
					logger.error(message);
					throw new WStepDefException(message);
					
				}
				
			}
			
		}
		
	}

	// dml 20130430
	public Integer getLastVersionNumber(Integer stepHeadId) throws WStepDefException {

		return new WStepDefDao().getLastVersionNumber(stepHeadId);
	
	}
	
	public WStepDef getWStepDefByPK(Integer id, Integer userId) throws WStepDefException {

		return new WStepDefDao().getStepDefByPK(id);
	}
	
	
	
/* dml 20130509 metodo pasado a WStepHeadDao	
	public WStepDef getWStepDefByName(String name, Integer userId) throws WStepDefException {

		return new WStepDefDao().getStepDefByName(name);
	}
*/
	
	public List<WStepDef> getStepDefs(Integer userId) throws WStepDefException {

		// nota: falta revisar el tema de los permisos de usuario para esto ...
		return new WStepDefDao().getWStepDefs();
	
	}
	
	public List<WStepDef> getStepDefs(Integer processId, Integer userId) throws WStepDefException {

		return new WStepDefDao().getStepDefs(processId);
	
	}
	
	// dml 20130507
	public List<Integer> getProcessIdList(Integer stepId, Integer userId) throws WStepDefException{
		
		return new WStepDefDao().getProcessIdList(stepId);
		
	}
	
	// dml 20130507
	public boolean isAnotherProcessUsingStep(Integer stepId, Integer processId, Integer userId) 
			throws WStepDefException, WProcessDefException {

		if (stepId == null
				|| stepId.equals(0)){
			String mess = "stepId cannot be null or zero";
			logger.error(mess);
			throw new WStepDefException(mess);
		}

		List<Integer> processIdList = this.getProcessIdList(stepId, userId);
		
		if (processIdList == null){
			String mess = "Error trying to retrieve the processIdList:"+processId;
			logger.error(mess);
			throw new WProcessDefException(mess);
		}
		
		// si le pasamos processId nulo, si la lista no esta vacia devuelve "true"
		if ((processId == null
				|| processId.equals(0))
				&& !processIdList.isEmpty()){
			return true;
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
		boolean userIsProcessAdmin = new WProcessDefBL().userIsProcessAdmin(userId, processId, userId);

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
	