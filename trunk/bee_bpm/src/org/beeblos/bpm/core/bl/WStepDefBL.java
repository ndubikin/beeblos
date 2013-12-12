package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.ALL;
import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;
import static org.beeblos.bpm.core.util.Constants.DELETED_BOOL;
import static org.beeblos.bpm.core.util.Constants.FIRST_WPROCESSDEF_VERSION;
import static org.beeblos.bpm.core.util.Constants.NOT_DELETED_BOOL;

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
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepUser;

import com.sp.common.util.StringPair;



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

			// DAVID: NO ENTIENDO POR QUE EN EL ADD MANDÁS A RECARGAR INFO QUE VIENE EN EL STEP QUE TE PASAN
			// POR EJEMPLO: SI ESTE STEP VIENE CON COMENTARIOS Y INSTRUCCIONES, TE LAS CARGÁS EN EL MÉTODO SIGUIENTE ...
			// ¿A QUE SE DEBE ESTO ...?
			this._setFirstWStepDefData(step, stepHeadId, null, null, null, currentUserId);
			
			
		}
		
		// timestamp & trace info
		step.setInsertDate(new Date());
		step.setModDate( DEFAULT_MOD_DATE );
		step.setInsertUser(currentUserId);
		step.setModUser(currentUserId);
		return new WStepDefDao().add(step);

	}
	
	/**
	 * 
	 * load a step object with their head, rules, comments, instructions, etc ...
	 * 
	 * @param step
	 * @param stepHeadId
	 * @param rules
	 * @param stepComments
	 * @param instructions
	 * @param currentUserId
	 * @throws WStepHeadException
	 */
	// DAVID EXPLICA BREVEMENTE EN LA DOC PARA QUE SE USA ESTE INICIALIZADOR Y EN QUE CASOS OK?
	private void _setFirstWStepDefData(
			WStepDef step, Integer stepHeadId, String rules, String stepComments, String instructions, Integer currentUserId) 
					throws WStepHeadException {
		
		if (step != null){
			
//			step.setActive(true);

			step.setStepHead(new WStepHeadBL().getWStepHeadByPK(stepHeadId, currentUserId));
			step.setRules(rules);
			step.setStepComments(stepComments);
			step.setInstructions(instructions);
			
			if (step.getVersion() == null
					|| step.getVersion().equals(0)){
				step.setVersion(FIRST_WPROCESSDEF_VERSION);
			}
		
		}
		
	}
	
	// dml 20130430
	// nota: public Integer createFirstWStepDef( creado por david que renombro a new porque no entiendo el concepto de
	// first y además esto se llama desde un loop del sincronizador de xml cada vez q se va a agregar 1 paso ...
	// DAVID BORRA ESTE COMENTARIO LUEGO Y DEJA UNA BREVE JAVADOR QUE EXPLIQUE EL USO QUE TIENE ESTE MÉTODO OK?
	public Integer createNewWStepDef(
			Integer stepHeadId, String rules, String stepComments, String instructions, Integer currentUserId) 
					throws WStepDefException, WStepHeadException{

		WStepDef wsd = new WStepDef();

		this._setFirstWStepDefData(wsd, stepHeadId, rules, stepComments, instructions, currentUserId);
		
		return this.add(wsd, currentUserId);
		
	}

	public void update(WStepDef step, Integer processHeadId, Integer currentUserId) 
			throws WStepDefException {
		
		logger.debug("update() WStepDef < id = "+step.getId()+">");
		
		if (!step.equals(new WStepDefDao().getStepDefByPK(step.getId(), processHeadId)) ) {

			// timestamp & trace info
			step.setModDate(new Date());
			step.setModUser(currentUserId);
			new WStepDefDao().update(step);
			
		} else {
			
			logger.debug("WStepDefBL.update - nothing to do ...");
		}
		

					
	}
	
	/**
	 * @author dmuleiro - 20130830
	 * 
	 * Updates the step's "logic delete" putting the field "delete" as the user wants. 
	 *
	 * @param  Integer stepId
	 * @param  boolean deleted
	 * @param  Integer currentUserId
	 * 
	 * @return void
	 * 
	 * @throws WStepDefException 
	 * 
	 */
	private void _updateStepDeletedField(Integer stepId, boolean deleted, Integer currentUserId)
			throws WStepDefException {

		logger.debug("updateStepDeletedField() WStepDef < id = " + stepId + ">");

		if (stepId != null 
				&& !stepId.equals(0)) {

			new WStepDefDao().updateStepDeletedField(stepId, deleted, currentUserId, new Date());

		}

	}

	/**
	 * Deletes a step def controlling:
	 * 	>> step with routes can't be deleted. If all routes has deleted mark, then the step def will be 
	 *  	marked for delete
	 *  >> shared steps will not be deleted ... (must be delete process first or unlink it from the other processes...)
	 *  >> step with works (used steps ...) will not be deleted (but there will be marked for delete ...)
	 *  
	 *  NOTA: ESTE MÉTODO DEBERIA UTILIZARSE SOLO EN ENTORNOS DE DESARROLLO!!
	 *  
	 *  TODO: revisar si se puede utilizar en entornos de producción y si no se puede revisar que se puede hacer para tener esta
	 *  funcionalidad para entornos de producción (si es que hay que tenerla ...)
	 * 
	 * @param stepDefId
	 * @param processHeadId
	 * @param currentUserId
	 * @throws WStepDefException
	 * @throws WStepWorkException
	 * @throws WProcessDefException
	 * @throws WStepSequenceDefException
	 * @throws WStepHeadException
	 * @throws WStepWorkSequenceException
	 */
	public void delete(Integer stepDefId, Integer processHeadId, Integer currentUserId) 
			throws WStepDefException, WStepWorkException, WProcessDefException, 
			WStepSequenceDefException, WStepHeadException, WStepWorkSequenceException  {

		logger.debug("delete(stepid) WProcessDef - Name: ["+stepDefId+"]");
		
		if (stepDefId==null || stepDefId==0) {
			String mess = "Trying delete step with id null or 0 ...";
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		
		boolean deleteFromBD = true;
		
		// dml 20130830 - si el step tiene rutas no se puede eliminar de la BD. Si todas las rutas estan deleted se podrá marcar
		// como "deleted", de lo contrario devuelve exception.
		if (this._stepHasRoutes(stepDefId, currentUserId)){
			
			// dml 20130830 - si tiene rutas y ninguna tiene "deleted=false" entonces se marca como deleted, en caso contrario
			// tira exception porque no se puede "borrar" un step con rutas "activas" asociadas
			if (!this._stepHasAliveRoutes(stepDefId, currentUserId)){
				
				deleteFromBD = false;
				
			} else {
				String mess = "Delete step with related NOT DELETED step sequence is not allowed ...";
				logger.error(mess);
				throw new WStepDefException(mess);
			}
			
		}
		
		// dml 20130830 - si el step se esta usando en algun otro proceso no se puede ni borrar ni marcar como borrado (para PURGE)
		if (this.isSharedStep(stepDefId, currentUserId)) {
			String mess = "Delete shared step is not allowed because this step belongs few processes ...";
			logger.error(mess);
			throw new WStepSequenceDefException(mess);
		}

		Integer qtyWorks;
		try {
			
			qtyWorks = new WStepWorkBL().getWorkCountByStep(stepDefId,ALL);
		
		} catch (WStepWorkException e) {
			String mess = "Error verifiyng existence of works related with this step id:"+stepDefId
					+ " "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		
		// dml 20130830 - si el step se usa en algun work no se puede borrar de la BD pero si marcar como borrado (para PURGE)
		if (qtyWorks != null && qtyWorks > 0) {
			deleteFromBD = false;
		}

		Integer workingRoutes = 
				new WStepWorkSequenceBL().countSequenceWork(stepDefId, currentUserId);
		
		// dml 20130830 - si tiene rutas recorridas (work) no se puede borrar pero si marcar como borrado (para PURGE)
		if (workingRoutes != null
				&& workingRoutes > 0){
			deleteFromBD = false;
		} 

		WStepDef step = this.getWStepDefByPK(stepDefId, processHeadId, currentUserId);
		Integer stepHeadId = step.getStepHead().getId();
		
		// dml 20130830 - si llega aqui (no sale por exception) pero no se puede borrar definitivamente de la BD 
		// se marca como "deleted" (para que el PURGE luego limpie todo)
		if (!deleteFromBD){
			this._updateStepDeletedField(stepDefId, DELETED_BOOL, currentUserId);
		} else {
			// se borrarán en cascada (por el mapping) los roles y users relacionados con el step
			new WStepDefDao().delete(step);
			logger.info("The WProcessDef " + step.getName() + " has been correctly deleted by user " + currentUserId);

			this._checkAndDeleteStepHead(stepHeadId, currentUserId);
		}
							
	}	
	
	/**
	 * @author dmuleiro - 20130830
	 * 
	 * If the step def has any route returns true else false
	 *
	 * @param  Integer stepDefId
	 * @param  Integer currentUserId
	 * 
	 * @return Integer
	 * 
	 */
	private boolean _stepHasRoutes(Integer stepDefId, Integer currentUserId) throws WStepDefException{
		
		// checks if step is used in other process or in the same process in other routes ...
		Integer qtyRoutes=0;
		WStepSequenceDefBL routesBL = new WStepSequenceDefBL();

		// send null processId to count all routes in all processes. If
		try {
			
			qtyRoutes = routesBL.countIncomingRoutes(stepDefId, null, null, currentUserId);
			
			if (qtyRoutes != null && qtyRoutes > 0) return true;
			
			qtyRoutes = routesBL.countOutgoingRoutes(stepDefId, null, null, currentUserId);
			
			if (qtyRoutes != null && qtyRoutes > 0) return true;
			
		} catch (WStepSequenceDefException e) {
			String mess = "Error verifiyng existence of another uses for this step id:"+stepDefId
							+ " "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		
		return false;

	}

	/**
	 * @author dmuleiro - 20130830
	 * 
	 * If the step has routes marked as "NOT DELETED" it returns true, if it has not routes or they are all deleted returns false
	 *
	 * @param  Integer stepDefId
	 * @param  Integer currentUserId
	 * 
	 * @return Integer
	 * 
	 */
	private boolean _stepHasAliveRoutes(
			Integer stepDefId, Integer currentUserId) throws WStepDefException {
		
		WStepSequenceDefBL routesBL = new WStepSequenceDefBL();

		// checks if step is used in other process or in the same process in other routes ... and if they are deleted
		List<WStepSequenceDef> routes = null;

		// send null processId to count all routes in all processes. If
		try {
			
			routes = routesBL.getIncomingRoutes(stepDefId, null, null, currentUserId);
			
			if (routes != null && !routes.isEmpty()) {
				
				for (WStepSequenceDef route : routes){
					// si hay alguna ruta y no esta marcada como borrada no se hace nada
					if (!route.isDeleted()){
						return true;
					}
					
				}
			}
			
			routes = routesBL.getOutgoingRoutes(stepDefId, null, null, currentUserId);
			
			if (routes != null && !routes.isEmpty()) {
				
				for (WStepSequenceDef route : routes){
					// si hay alguna ruta y no esta marcada como borrada no se hace nada
					if (!route.isDeleted()){
						return true;
					}
					
				}
			}
		
		} catch (WStepSequenceDefException e) {
			String mess = "Error verifiyng existence of another uses for this step id:"+stepDefId
							+ " "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		
		return false;

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
	public Integer cloneWStepDef(Integer stepId, Integer stepHeadId, Integer processId, Integer processHeadId,
			Integer userId) 
			throws  WStepHeadException, WStepSequenceDefException, WStepDefException  {
		
		boolean newStep=false; // by default generates a new version of current stepId
		boolean cloneRoutes=true;
		boolean cloneResponses=true;
		boolean clonePermissions=true;
		
		return cloneWStepDef( stepId,  stepHeadId,  processId, processHeadId,
				 newStep,  cloneRoutes,	cloneResponses, clonePermissions, userId);
		
	}
	
	/**
	 *clone step with options:
	 *
	 * generateNewStep: indicates must generate a new step with your new step_head and version =1
	 * generateNewStep=false -> generates a new version of stepId
	 * generateNewStep=true -> generates a new step copying data from stepId and version=1
	 * cloneRoutes: indicates must generate new routes copying origin step routes
	 * cloneResponses: indicates must generate new responses copying origin step responses
	 * clonePermissions: indicates must generate new permissions copying origin step permissions
	 * 
	 * processId must passed if only the routes for 1 process will be cloned .... Remember 
	 * WStepSequeceDef has incoming and outgoint routes for a step/process
	 *  
	 * 
	 * @param stepId
	 * @param stepHeadId
	 * @param processId
	 * @param processHeadId
	 * @param generateNewStep
	 * @param cloneRoutes
	 * @param cloneResponses
	 * @param clonePermissions
	 * @param userId
	 * @return
	 * @throws WStepHeadException
	 * @throws WStepSequenceDefException
	 * @throws WStepDefException
	 */
	public Integer cloneWStepDef(Integer stepId, Integer stepHeadId, Integer processId, Integer processHeadId,
									boolean generateNewStep, boolean cloneRoutes,
									boolean cloneResponses,
									boolean clonePermissions, Integer userId) 
			throws  WStepHeadException, WStepSequenceDefException, WStepDefException  {
		
		Integer clonedId=null,newVersion=0;
		WStepDef newStep,currentStep;
		
		try {
			
			newStep = this.getWStepDefByPK(stepId,processHeadId,userId);
			currentStep = this.getWStepDefByPK(stepId,processHeadId, userId);

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
				routes = new WStepSequenceDefBL().getOutgoingRoutes(stepId, NOT_DELETED_BOOL, processId, userId);
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
				routes = new WStepSequenceDefBL().getIncomingRoutes(stepId, NOT_DELETED_BOOL, processId, userId);
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
	public void deactivateStep(Integer stepId, Integer processHeadId,Integer currentUserId) 
			throws WStepDefException, WStepSequenceDefException{
		
		if (stepId != null
				&& !stepId.equals(0)){
			
			WStepDef wsd = this.getWStepDefByPK(stepId, processHeadId, currentUserId);
			
			if (wsd != null){
				
				if (wsd.isActive()){
					
					wsd.setActive(false);
					this.update(wsd, processHeadId, currentUserId);
					
				} else {

					String message = "The step you're trying to deactivate is already non active.";
					logger.error(message);
					throw new WStepDefException(message);
					
				}
				
			}
			
		}
		
	}

	// dml 20130509
	public void activateStep(Integer stepId, Integer processHeadId, Integer currentUserId) 
			throws WStepDefException, WStepSequenceDefException{
		
		if (stepId != null
				&& !stepId.equals(0)){
			
			WStepDef wsd = this.getWStepDefByPK(stepId, processHeadId, currentUserId);
			
			if (wsd != null){
				
				if (!wsd.isActive()){
					
					wsd.setActive(true);
					this.update(wsd,processHeadId, currentUserId);
					
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
	
	// dml 20130827
	public boolean existsStep(Integer stepId) throws WStepDefException {

		return new WStepDefDao().existsStep(stepId);
	
	}
	
	/**
	 * Returns a StepDef
	 * 
	 * note: this method may be called from a processDef or without a processDef
	 * (remember: a step may be shared by any processes ...) then ...
	 * if the method is called with a defined process def, it need processHeadId to
	 * return step data fields related only for the indicated processHeadId
	 * 
	 * 
	 * @param id
	 * @param processHeadId
	 * @param currentUserId
	 * @return
	 * @throws WStepDefException
	 */
	public WStepDef getWStepDefByPK(Integer id, Integer processHeadId, Integer currentUserId) 
			throws WStepDefException {

		WStepDef stepDef =  new WStepDefDao().getStepDefByPK(id, processHeadId);
		
//		try {
//			stepDef.getStepHead().setDataFieldDef(
//						new WStepDataFieldBL().getWStepDataFieldSet(
//								stepDef.getStepHead().getId(),userId ) );
//		} catch (WStepDataFieldException e) {
//			logger.error("Can't load manually the dataField set !!!");
//			e.printStackTrace();
//		}
		
		return stepDef;
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
	
	public List<WStepDef> getStepDefs(Integer processId, Boolean deleted, Integer userId) throws WStepDefException {

		return new WStepDefDao().getStepDefs(processId, deleted);
	
	}
	

	/**
	 * Returns a list of process def id which use given step def id
	 * 
	 * @author dmuleiro - 20130507
	 * 
	 * @param stepDefId
	 * @return
	 * @throws WStepDefException
	 */
	public List<Integer> getProcessIdList(Integer stepDefId, Integer userId) throws WStepDefException{
		
		return new WStepDefDao().getProcessIdList(stepDefId);
		
	}
	
	/**
	 * @author dmuleiro - 20130507
	 * 
	 * If the step is used by more than one process then it's a shared step
	 *
	 * @param  Integer stepId
	 * @param  Integer currentUserId
	 * 
	 * @return boolean
	 * 
	 * @throws WStepDefException 
	 * 
	 */
	public boolean isSharedStep(Integer stepDefId, Integer currentUserId) throws WStepDefException {

		if (stepDefId == null
				|| stepDefId.equals(0)){
			String mess = "stepId cannot be null or zero";
			logger.error(mess);
			throw new WStepDefException(mess);
		}

		List<Integer> processIdList = this.getProcessIdList(stepDefId, currentUserId);
		
		if (processIdList == null){
			String mess = "Error trying to retrieve the processIdList for step: "+stepDefId;
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		
		// si le pasamos processId nulo, si la lista no esta vacia devuelve "true"
		if (processIdList != null
				&& processIdList.size() > 1){
			return true;
		} else {
			return false;
		}
	
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
	@Deprecated
	public List<StringPair> getComboList(
			Integer processId, Integer versionId, Integer userId, boolean allItems, 
			String firstLineText, String blank )
	throws WProcessDefException {
		
		// dml 20130129 - checking if the user is process admin
		boolean userIsProcessAdmin = new WProcessDefBL().userIsProcessAdmin(userId, processId, userId);

		return new WStepDefDao().getComboList(processId, versionId, userId, userIsProcessAdmin, allItems, 
				firstLineText, blank);
		
	}
	
	// 
	/**
	 * 
	 * dml 20130129 - new combo method with userId and allItems
	 * 
	 * returs a list of pairs: id/step-def 
	 * 
	 * @param processId
	 * @param userId
	 * @param allItems
	 * @param firstLineText
	 * @param blank
	 * @return
	 * @throws WProcessDefException
	 */
	public List<StringPair> getComboList(
			Integer processId, Integer userId, boolean allItems, 
			String firstLineText, String blank )
	throws WProcessDefException {
		
		// dml 20130129 - checking if the user is process admin
		boolean userIsProcessAdmin = new WProcessDefBL().userIsProcessAdmin(userId, processId, userId);

		return new WStepDefDao().getComboList(processId, userId, userIsProcessAdmin, allItems, 
				firstLineText, blank);
		
	}

	public List<WStepDef> finderWStepDef (String nameFilter, String commentFilter, 
			String instructionsFilter, Integer userId, boolean isAdmin, String action, 
			Integer stepHeadId, String activeFilter) 
	throws WStepDefException {
		
		return new WStepDefDao().finderWStepDef(nameFilter, commentFilter, instructionsFilter, 
				userId, isAdmin, action, stepHeadId, activeFilter);

	}


}
	