package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;
import static org.beeblos.bpm.core.util.Constants.ALL;
import static org.beeblos.bpm.core.util.Constants.DELETED_BOOL;
import static org.beeblos.bpm.core.util.Constants.FIRST_WPROCESSDEF_VERSION;
import static org.beeblos.bpm.core.util.Constants.NOT_DELETED_BOOL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepDefDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepHeadException;
import org.beeblos.bpm.core.error.WStepRoleException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepUserException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepHead;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepUser;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.WUserRole;
import org.joda.time.DateTime;

import com.sp.common.util.StringPair;



public class WStepDefBL {
	
	private static final Log logger = LogFactory.getLog(WStepDefBL.class.getName());
	
	public WStepDefBL (){
		
	}
	
	public Integer add(WStepDef step, Integer currentUserId) throws WStepDefException, WStepHeadException {
		
		logger.debug("add() WStepDef - Name: ["+step.getName()+"]");
		
		DateTime now = new DateTime();
		
		step.nullateEmtpyObjects(); // nes 20150212 - hay que anular porque ahora vienen por WS y demas las altas...
		
		_setDefaultValues(step);
		
		// dml 20130430 - si es un nuevo WStepHead se guarda antes de guardar el WStepDef y se rellena la informacion esencial
		// nes 20141206 - if there is not a WStepHead then will be created with WStepDef data...
		_setAndSaveStepHead(step, now, currentUserId);
		_checkResponsesTimestamps(step,now,currentUserId);
		
		// timestamp & trace info
		step.setInsertDate(now);
		step.setModDate( DEFAULT_MOD_DATE_TIME );
		step.setInsertUser(currentUserId);
		step.setModUser(currentUserId);
		return new WStepDefDao().add(step);

	}

	private void _setDefaultValues(WStepDef step) {

		if(step.getVersion() == null ||
				step.getVersion() == 0){
			step.setVersion(1);
		}
	}

	/**
	 * Set WStepHead and persist. Update WStepDef with persisted data...
	 * nes 20141206
	 * @param step
	 * @param currentUserId
	 * @throws WStepHeadException
	 */
	private void _setAndSaveStepHead(WStepDef step, DateTime now, Integer currentUserId)
			throws WStepHeadException {
		
		Integer stepHeadId=null;
		
		if (step.getStepHead() != null
				&& (step.getStepHead().getId() == null
				|| step.getStepHead().getId().equals(0))){
			
			stepHeadId = new WStepHeadBL().add(step.getStepHead(), currentUserId);
			
		} else if (step.getStepHead()==null) {
			
			stepHeadId = persistStepHeadComingNull(step,now,currentUserId);

		}
		
		if (stepHeadId!=null) {

			this._setFirstWStepDefData(step, stepHeadId, step.getRules(), 
					step.getStepComments(), step.getInstructions(), currentUserId);
		}
	}
	
	/**
	 * creates a WStepHead for a new WStepDef with arrives with a null WStepHead
	 * Esto lo creé porque los test fallan al intentar agregar un nuevo WStepDef si no le hemos
	 * definido previamente su WStepHead. Como esta situación se podría dar a nivel de uso 
	 * por API del BeeBPM, prefiero agregar esta solución para que quede mas robusta la BL
	 * nes 20141206
	 * @param step
	 * @param currentUserId
	 * @return
	 */
	private Integer persistStepHeadComingNull(WStepDef step, DateTime now, Integer currentUserId) {
		
		WStepHead stepHead = new WStepHead("pending name...","", now, DEFAULT_MOD_DATE_TIME );
		Integer stepHeadId;
		try {
			stepHeadId = new WStepHeadBL().add(stepHead, currentUserId);
		} catch (WStepHeadException e) {
			stepHeadId=null;
			String mess = "Error creating new WStepHead for WStepDef with empty WStepHead..."+ e.getMessage()
							+" "+(e.getCause()!=null?e.getCause():"null");
			logger.error(mess);
		}
		return stepHeadId;
	}
	
	/**
	 * set timestamps for responses in a new step
	 * this method only can be called from add step!!!!!!!!
	 * nes 20141206
	 * @param step
	 * @param now
	 * @param currentUserId
	 */
	private void _checkResponsesTimestamps(WStepDef step, DateTime now, Integer currentUserId) {
		if (step.getResponse()!=null && step.getResponse().size()>0){
			for(WStepResponseDef response: step.getResponse()) {
				response.setInsertDate(now);
				response.setModDate(DEFAULT_MOD_DATE_TIME);
				response.setInsertUser(currentUserId);
				response.setModUser(null);
			}
		}
	}
	/**
	 * 
	 * Este método es el encargado de asignar el "WStepHead" creado al "WStepDef" antes de persistirlo. 
	 * También se asignan el resto de datos.
	 * 
	 * NOTA PARA NESTOR: Este método se penso en un principio solamente para asignar el un stepHeadId, pero cuando se creo
	 * la funcionalidad de poder insertar WStepDefs desde un mapa con los datos "rules, stepComments e instructions"
	 * se le añadieron. Lo que no me di cuenta es que en la llamada desde "WStepDefBL.add() estaba borrando estos valores
	 * pero así como lo puse ahora deberia funcionar bien.
	 * 
	 * @param step
	 * @param stepHeadId
	 * @param rules
	 * @param stepComments
	 * @param instructions
	 * @param currentUserId
	 * @throws WStepHeadException
	 */
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
	
	/**
	 * Se crea el nuevo WStepDef a partir de la informacion que se le pasa única y exclusivamente en la firma del método.
	 * 
	 * @author dmuleiro 20130430
	 * 
	 * @param stepHeadId
	 * @param rules
	 * @param stepComments
	 * @param instructions
	 * @param currentUserId
	 * @return
	 * @throws WStepDefException
	 * @throws WStepHeadException
	 */
	public Integer createNewWStepDef(
			Integer stepHeadId, String rules, String stepComments, String instructions, Integer currentUserId) 
					throws WStepDefException, WStepHeadException{

		WStepDef wsd = new WStepDef();

		this._setFirstWStepDefData(wsd, stepHeadId, rules, stepComments, instructions, currentUserId);
		
		return this.add(wsd, currentUserId);
		
	}

	/**
	 * Update an existing WStepDef
	 * 
	 * @param step
	 * @param processHeadId
	 * @param currentUserId
	 * @throws WStepDefException
	 */
	public void update(WStepDef step, Integer processHeadId, Integer currentUserId) //@TODO *** hay que ver por q le meti el processHeadId aquí Y DOCUMENTARLO...***
			throws WStepDefException {
		
		logger.debug("update() WStepDef < id = "+step.getId()+">");
		
		if (!step.equals(new WStepDefDao().getStepDefByPK(step.getId(), processHeadId)) ) {

			// timestamp & trace info
			step.setModDate(new DateTime());
			step.setModUser(currentUserId);
			new WStepDefDao().update(step);
			
		} else {
			
			logger.debug("WStepDefBL.update - nothing to do ...");
		}
	}
	
	/**
	 * Updates the step type configuration xml field marshaling it WStepTypeDef object
	 *
	 * @author dmuleiro 20150424
	 * 
	 * @param  Integer wsd
	 * @param  Integer currentUserId
	 * 
	 * @return void
	 * 
	 * @throws WStepDefException 
	 * 
	 */
	public void updateStepTypeConfigurationField(WStepDef wsd, Integer currentUserId)
			throws WStepDefException {
		
		if (wsd == null || wsd.getId() == null || wsd.getStepTypeDef() == null){
			throw new WStepDefException("The step def is not valid!");
		}

		new WStepDefDao().updateStepTypeConfigurationField(wsd, currentUserId, new DateTime());

	}

	/**
	 * @author dmuleiro - 20130830
	 * 
	 * Updates the step's "logical delete" field  
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
		logger.debug("updateStepDeletedField() WStepDef id:" + stepId);

		if (stepId != null 
				&& !stepId.equals(0)) {

			new WStepDefDao().updateStepDeletedField(stepId, deleted, currentUserId, new DateTime());

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
	 *  TODO: revisar si se puede utilizar en entornos de producción y si no se puede revisar que se 
	 *  puede hacer para tener esta funcionalidad para entornos de producción (si es que hay que tenerla ...)
	 * 
	 * @param stepDefId
	 * @param processHeadId
	 * @param currentUserId
	 * @throws WStepDefException
	 * @throws WStepHeadException
	 */
	public void delete(Integer stepDefId, Integer processHeadId, Integer currentUserId) 
			throws WStepDefException, WStepHeadException  {
		logger.debug("delete(stepid) WProcessDef - Name: ["
						+(stepDefId!=null?stepDefId:"null")+"]");
		
		if (stepDefId==null || stepDefId==0) {
			String mess = "Trying delete step with id null or 0 ...";
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		
		/**
		 * flag to indicate the step may be deleted from DB
		 * if not there will be checked as 'deleted' flag
		 */
		boolean allowDeleteFromBD = true;
		
		// dml 20130830 - si el step tiene rutas no se puede eliminar de la BD. Si todas las rutas estan deleted se podrá marcar
		// como "deleted", de lo contrario devuelve exception.
		if (this._stepHasRoutes(stepDefId, currentUserId)){
			
			// dml 20130830 - si tiene rutas y ninguna tiene "deleted=false" entonces se marca como deleted, en caso contrario
			// tira exception porque no se puede "borrar" un step con rutas "activas" asociadas
			if (!this._stepHasAliveRoutes(stepDefId, currentUserId)){
				
				allowDeleteFromBD = false;
				
			} else {
				String mess = "Delete step id:"+stepDefId+" with related and NOT DELETED routes is not allowed ...";
				logger.error(mess);
				throw new WStepDefException(mess);
			}
			
		}
		
		// dml 20130830 - si el step se esta usando en algun otro proceso no se puede ni borrar ni marcar como borrado (para PURGE)
		if (this.isSharedStep(stepDefId, currentUserId)) {
			String mess = "Delete shared step is not allowed because this step belongs few processes ... id:"
					+stepDefId;
			logger.error(mess);
			throw new WStepDefException(mess);
		}

		Integer qtyWorks;
		try {
			
			qtyWorks = new WStepWorkBL().getWorkCountByStep(stepDefId,ALL);
		
		} catch (WStepWorkException e) {
			String mess = "Error WStepWorkException checking existing works related with this step id:"+stepDefId
					+ " "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		
		// dml 20130830 - si el step se usa en algun work no se puede borrar de la BD pero si marcar como borrado (para PURGE)
		if (qtyWorks != null && qtyWorks > 0) {
			allowDeleteFromBD = false;
		}

		// dml 20130830 - si tiene rutas recorridas (work) no se puede borrar pero si marcar como borrado (para PURGE)
		Integer workingRoutes;
		try {
			workingRoutes = new WStepWorkSequenceBL().countSequenceWork(stepDefId, currentUserId);
			if (workingRoutes != null
					&& workingRoutes > 0){
				allowDeleteFromBD = false;
			} 
		} catch (WStepWorkSequenceException e) {
			String mess = "Error WStepWorkSequenceException checking existing working routes related with this step id:"+stepDefId
					+ " "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		

		WStepDef step = this.getWStepDefByPK(stepDefId, processHeadId, currentUserId);
		Integer stepHeadId = step.getStepHead().getId();
		
		// dml 20130830 - si llega aqui (no sale por exception) pero no se puede borrar definitivamente de la BD 
		// se marca como "deleted" (para que el PURGE luego limpie todo)
		if (!allowDeleteFromBD){
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

	/**
	 * checks if wStepHead belongs a wStepDef. if not this will be deleted...
	 * dml 20130507
	 * 
	 * @param stepHeadId
	 * @param currentUserId
	 * @throws WStepHeadException
	 */
	private void _checkAndDeleteStepHead(Integer stepHeadId, Integer currentUserId) throws WStepHeadException{
		
		WStepHeadBL wsBL = new WStepHeadBL();
		
		if (!wsBL.hasWStepDef(stepHeadId)){
			
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

		// persist new step without permissions ...
		try {

			//rrl 20141113
			newStep.setId(null);
			newStep.setVersion(newVersion); // new version number ....
			newStep.setRolesRelated(null);
			newStep.setUsersRelated(null);
			
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

		// cloning permissions ...
		try {

			WStepRoleBL wStepRoleBL = new WStepRoleBL();
			WStepUserBL wStepUserBL = new WStepUserBL();
			WStepRole tmpStepRole;
			WStepUser tmpStepUser;

			newStep.setRolesRelated(new HashSet<WStepRole>());
			newStep.setUsersRelated(new HashSet<WStepUser>());
			
			//rrl 20141113 Una vez que tenemos el "id" del nuevo WStepDef, creamos las relaciones con WRoleDef y WUserDef directamente.
			if (clonePermissions) {
				if ( currentStep.getRolesRelated().size()>0) {
					for ( WStepRole stepRole: currentStep.getRolesRelated() ) {
						tmpStepRole = 
								wStepRoleBL.addNewStepRoleX(newStep.getId(), stepRole.getRole().getId(), stepRole.isAdmin(), userId);
						newStep.getRolesRelated().add(tmpStepRole);
					}
				}

				if ( currentStep.getUsersRelated().size()>0) {
					for ( WStepUser stepUser: currentStep.getUsersRelated() ) {
						tmpStepUser = wStepUserBL.addNewStepUser(newStep.getId(), stepUser.getUser().getId(), stepUser.isAdmin(), userId);
						newStep.getUsersRelated().add(tmpStepUser);
					}
				}
			}

		} catch (Exception e) {
			String mess = "Error cloning step version:"+stepId+" can't clone related role or user list ..."
					+" - "+e.getClass()+" -" +e.getMessage()+" - "+e.getCause();
			throw new WStepDefException(mess);
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

		// nes 20141229 - this code is commented because it has already been applied in WStepDefDao ...
//		try {
//			
//			List<WStepDataField> stepDataFieldList = new WStepDataFieldBL().getWStepDataFieldList(
//					null, stepDef.getStepHead().getId(), currentUserId);
//			
//			stepDef.setDataFieldDef(stepDataFieldList);
//			
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
	
	/**
	 * return a full list of step defs in the system
	 * @param userId
	 * @return
	 * @throws WStepDefException
	 */
	public List<WStepDef> getStepDefs(Integer userId) throws WStepDefException {

		// nota: falta revisar el tema de los permisos de usuario para esto ...
		return new WStepDefDao().getWStepDefs();
	
	}
	
	/**
	 * Returns the list of step def for a given processId
	 * 
	 * @param processId
	 * @param deleted
	 * @param userId
	 * @return
	 * @throws WStepDefException
	 */
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
	
	/**
	 * Returns all wStepDef list
	 * @param firstLineText
	 * @param blank
	 * @return
	 * @throws WStepDefException
	 */
	public List<StringPair> getComboList(
			String firstLineText, String blank )
	throws WStepDefException {
		 
		return new WStepDefDao().getComboList(firstLineText, blank);


	}

	/**
	 * returns the list of active steps for given processId
	 * @param processId
	 * @param firstLineText
	 * @param blank
	 * @return
	 * @throws WProcessDefException
	 */
	public List<StringPair> getComboList(
			Integer processId, String firstLineText, String blank )
	throws WProcessDefException {
		
		return new WStepDefDao().getComboList(processId, firstLineText, blank);
		
	}
	
	/**
	 * returns the list of active steps for given processId and event type: ie: InitEv...
	 * nes 20150410
	 * 
	 * @param processId
	 * @param firstLineText
	 * @param blank
	 * @return
	 * @throws WProcessDefException
	 */
	public List<StringPair> getComboList(
			Integer processId, String eventTypeId, String firstLineText, String blank )
	throws WProcessDefException {
		
		return new WStepDefDao().getComboList(processId, eventTypeId, firstLineText, blank);
		
	}
	
	/**
	 * returns a list with stepDef wich refers stepDefIdList passed as parameter
	 * Normally stepDefIdList will be obtained from current wStepWork for a user
	 * To resolve runtime users stepDef with permissions
	 * nes 20160331
	 * 
	 * @param stepDefIdList
	 * @param firstLineText
	 * @param blank
	 * @return
	 * @throws WStepDefException
	 */
	public List<StringPair> getComboList(
			List<Integer> stepDefIdList, String firstLineText, String blank )
	throws WStepDefException {

		if (stepDefIdList==null || stepDefIdList.size()==0){
			return null;
		}
		
		List<StringPair> listRet = new ArrayList<StringPair>();

		// inserta los extras
		if ( firstLineText!=null && !"".equals(firstLineText) ) {
			if ( !firstLineText.equals("WHITESPACE") ) {
				listRet.add(new StringPair(null,firstLineText));  // deja la primera línea con lo q venga
			} else {
				listRet.add(new StringPair(null," ")); // deja la primera línea en blanco ...
			}
		}
		
		if ( blank!=null && !"".equals(blank) ) {
			if ( !blank.equals("WHITESPACE") ) {
				listRet.add(new StringPair(null,blank));  // deja la separación línea con lo q venga
			} else {
				listRet.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
			}
		}
		
		WStepDefDao wsdDao = new WStepDefDao();
		List<StringPair> unsortedList = new ArrayList<StringPair>();
		for (Integer idStepDef: stepDefIdList) {
			unsortedList.add(wsdDao.getStepDefByPKAsStringPair(idStepDef));
		}

		//java 8 lambda expression with type information removed.
		Collections.sort(unsortedList, (wsd1, wsd2) -> wsd1.getString2().compareTo(wsd2.getString2()));
		
		listRet.addAll(unsortedList);
		
		return listRet;


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
			String instructionsFilter, Integer userId, boolean isAdmin, String searchOrder, 
			Integer stepHeadId, String activeFilter) 
	throws WStepDefException {
		
		return new WStepDefDao().finderWStepDef(nameFilter, commentFilter, instructionsFilter, 
				userId, isAdmin, searchOrder, stepHeadId, activeFilter);

	}
	
	/**
	 * Checks if an user has valid permissions in a "stepDef"
	 * 
	 * nes 20140628 - its 391 - el "user" debe ser el del bpm (WUserDef) que es el que
	 * tiene los roles
	 * 
	 * @author dmuleiro 20140529
	 *  
	 * @param userId
	 * @param step
	 * @return
	 */
	public boolean userHasStepDefPermission(WUserDef user, WStepDef step){
		
		if (user == null || user.getId() == null || user.getId().equals(0)){
			logger.error("The user is not valid!");
			return false;
		}
		
		if (step != null){
			
			/**
			 * Comprobamos los users relacionados con el step def
			 */
			if (step.getUsersRelated() != null){
				
				for (WStepUser stepUser : step.getUsersRelated()){
					
					if (stepUser.getUser() != null && stepUser.getUser().getId() != null 
							&& stepUser.getUser().getId().equals(user.getId())){
						return true;
					}
				}
			}
			/**
			 * Comprobamos si tenemos algun "role" compartido entre los "roles" del step y los del "user"
			 */
			if (step.getRolesRelated() != null && user.getRolesRelated() != null){
				
				for (WStepRole stepRole : step.getRolesRelated()){
					for (WUserRole userRole : user.getRolesRelated()){ // nes 20140628 - its 391
					
						if (stepRole.getRole() != null && stepRole.getRole().getId() != null
								&& userRole.getRole() != null && userRole.getRole().getId() != null
								&& stepRole.getRole().getId().equals(userRole.getRole().getId())){
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}

	/**
	 * Add a new StepRole relation row
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param stepDef
	 *            the step def
	 * @param idRole
	 *            the role id
	 * @param isAdmin
	 *            the is admin
	 * @param idCurrentUser
	 *            the id current user
	 * @throws WStepDefException
	 *             the w step def exception
	 */
	public Integer addRelatedRole(Integer idStepDef, Integer idRole, boolean isAdmin, Integer idCurrentUser) 
			throws WStepDefException{
		
		if (idStepDef == null || idStepDef.equals(0)){
			throw new WStepDefException("addRelatedRole(): The step is not valid!");
		}
		
		if (idRole == null || idRole.equals(0)){
			throw new WStepDefException("addRelatedRole(): The role id is not valid!");
		}
		
		Integer idStepRole=null;
		
		try {
			
			idStepRole = 
					new WStepRoleBL().addNewStepRole(idStepDef, idRole, isAdmin, idCurrentUser);
			

			
		} catch (WStepRoleException e) {
			String mess = "addStepRelatedRole(): Error trying to create the step role. "
					+ e.getMessage() + (e.getCause()!=null?e.getCause():"");
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		
		return idStepRole;
		
	}
	
	/**
	 * Deletes the step related role from the step "rolesRelated" list and returns the same step
	 * without the new value added.
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param stepDef
	 *            the step def
	 * @param idRole
	 *            the role id
	 * @param isAdmin
	 *            the is admin
	 * @param idCurrentUser
	 *            the id current user
	 * @throws WStepDefException
	 *             the w step def exception
	 */
	public WStepDef deleteStepRelatedRole(WStepDef stepDef, Integer idRole, Integer idCurrentUser) throws WStepDefException{
		
		if (stepDef == null || stepDef.getId() == null || stepDef.getId().equals(0)){
			throw new WStepDefException("addStepRelatedRole(): The step is not valid!");
		}
		
		if (idRole == null || idRole.equals(0)){
			throw new WStepDefException("addStepRelatedRole(): The role id is not valid!");
		}
		
		WStepRole stepRoleToDelete = null;
		if (stepDef != null && stepDef.getRolesRelated() != null){
			for (WStepRole stepRole : stepDef.getRolesRelated()){
				if (stepRole != null && stepRole.getRole() != null
						&& stepRole.getRole() != null
						&& stepRole.getRole().getId().equals(idRole)){
					stepRoleToDelete = stepRole;
					break;
				}
			}
		}
		
		if (stepRoleToDelete == null){
			throw new WStepDefException("The relation between the role with id: " + idRole
					+ " and the step: " + stepDef.getId() + " does not exist.");
		}
		
		stepDef.getRolesRelated().remove(stepRoleToDelete);

		try {
			
			new WStepRoleBL().delete(stepRoleToDelete, idCurrentUser);
			
		} catch (WStepRoleException e) {
			String mess = "deleteStepRelatedRole(): Error trying to delete the step role. "
					+ e.getMessage() + (e.getCause()!=null?e.getCause():"");
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		
		return stepDef;
		
	}
	
	/**
	 * Adds the step related user to the step "usersRelated" list and returns the same step
	 * with the new value added.
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param stepDef
	 *            the step def
	 * @param idUser
	 *            the user id
	 * @param isAdmin
	 *            the is admin
	 * @param idCurrentUser
	 *            the id current user
	 * @throws WStepDefException
	 *             the w step def exception
	 */
	public WStepDef addStepRelatedUser(WStepDef stepDef, Integer idUser, boolean isAdmin, Integer idCurrentUser) throws WStepDefException{
		
		if (stepDef == null || stepDef.getId() == null || stepDef.getId().equals(0)){
			throw new WStepDefException("addStepRelatedUser(): The step is not valid!");
		}
		
		if (idUser == null || idUser.equals(0)){
			throw new WStepDefException("addStepRelatedUser(): The user id is not valid!");
		}
		
		try {
			WStepUser stepUser = 
					new WStepUserBL().addNewStepUser(stepDef.getId(), idUser, isAdmin, idCurrentUser);
			
			if (stepUser != null){
				
				if (stepDef.getUsersRelated() == null){
					stepDef.setUsersRelated(new HashSet<WStepUser>());
				}
				stepDef.getUsersRelated().add(stepUser);
				
			}
			
		} catch (WStepUserException e) {
			String mess = "addStepRelatedUser(): Error trying to create the step user. "
					+ e.getMessage() + (e.getCause()!=null?e.getCause():"");
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		
		return stepDef;
		
	}
	
	/**
	 * Deletes the step related user from the step "usersRelated" list and returns the same step
	 * without the new value added.
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param stepDef
	 *            the step def
	 * @param idUser
	 *            the user id
	 * @param isAdmin
	 *            the is admin
	 * @param idCurrentUser
	 *            the id current user
	 * @throws WStepDefException
	 *             the w step def exception
	 */
	public WStepDef deleteStepRelatedUser(WStepDef stepDef, Integer idUser, Integer idCurrentUser) throws WStepDefException{
		
		if (stepDef == null || stepDef.getId() == null || stepDef.getId().equals(0)){
			throw new WStepDefException("addStepRelatedUser(): The step is not valid!");
		}
		
		if (idUser == null || idUser.equals(0)){
			throw new WStepDefException("addStepRelatedUser(): The user id is not valid!");
		}
		
		WStepUser stepUserToDelete = null;
		if (stepDef != null && stepDef.getUsersRelated() != null){
			for (WStepUser stepUser : stepDef.getUsersRelated()){
				if (stepUser != null && stepUser.getUser() != null
						&& stepUser.getUser() != null
						&& stepUser.getUser().getId().equals(idUser)){
					stepUserToDelete = stepUser;
					break;
				}
			}
		}
		
		if (stepUserToDelete == null){
			throw new WStepDefException("The relation between the user with id: " + idUser
					+ " and the step: " + stepDef.getId() + " does not exist.");
		}
		
		stepDef.getUsersRelated().remove(stepUserToDelete);

		try {
			
			new WStepUserBL().delete(stepUserToDelete, idCurrentUser);
			
		} catch (WStepUserException e) {
			String mess = "deleteStepRelatedUser(): Error trying to delete the step user. "
					+ e.getMessage() + (e.getCause()!=null?e.getCause():"");
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		
		return stepDef;
		
	}

}
	