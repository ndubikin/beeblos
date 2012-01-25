package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;
import static org.beeblos.bpm.core.util.Constants.OMNIADMIN;
import static org.beeblos.bpm.core.util.Constants.TURNBACK;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessDefDao;
import org.beeblos.bpm.core.dao.WStepWorkDao;
import org.beeblos.bpm.core.error.CantLockTheStepException;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepAlreadyProcessedException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepLockedByAnotherUserException;
import org.beeblos.bpm.core.error.WStepNotLockedException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.noper.StepWorkLight;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.model.noper.WRuntimeSettings;




public class WStepWorkBL {
	
	private static final Log logger = LogFactory.getLog(WStepWorkBL.class.getName());
	
	public WStepWorkBL (){
		
	}
	
	public Integer add(WStepWork stepw, Integer currentUser) throws WStepWorkException {
		
		logger.debug("add() WStepWork - Name: ["+stepw.getIdObjectType()+"-"+stepw.getIdObjectType()+"]");
		
		// timestamp & trace info
		stepw.setArrivingDate(new Date());
		stepw.setInsertUser( new WUserDef(currentUser) );
		stepw.setModDate( DEFAULT_MOD_DATE);
		return new WStepWorkDao().add(stepw);

	}
	
	
	public void update(WStepWork stepw, Integer currentUser) throws WStepWorkException {
		
		logger.debug("update() WStepWork < id = "+stepw.getId()+">");
		
		if (!stepw.equals(new WStepWorkDao().getWStepWorkByPK(stepw.getId())) ) {

			// timestamp & trace info
			stepw.setModDate(new Date());
			stepw.setModUser(currentUser);

			new WStepWorkDao().update(stepw);
			
		} else {
			
			logger.debug("WStepWorkBL.update - nothing to do ...");
		}
				
	}
	
	
	public void delete(WStepWork stepw, Integer currentUser) throws WStepWorkException {

		logger.info("delete() WStepWork - Name: ["+stepw.getId()+"] user:"+currentUser);
		
		new WStepWorkDao().delete(stepw);

	}

	public WStepWork getWStepWorkByPK(Integer id, Integer currentUser) throws WStepWorkException {

		return new WStepWorkDao().getWStepWorkByPK(id);
	}
	
	
	public WStepWork getWStepWorkByName(String name, Integer currentUser) throws WStepWorkException {

		return new WStepWorkDao().getWStepWorkByName(name);
	}

	
	public List<WStepWork> getWStepWorks(Integer currentUser) throws WStepWorkException {

		return new WStepWorkDao().getWStepWorks();
	
	}
	
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WStepWorkException {
		 
		return new WStepWorkDao().getComboList(textoPrimeraLinea, separacion);


	}

	
	public Boolean existsActiveProcess(
			Integer idProcess, Integer idObject, String idObjectType, Integer currentUser ) 
	throws WStepWorkException {
		
		return new WStepWorkDao().existsActiveProcess(idProcess, idObject, idObjectType);
	}

	/*
	 ********************** MÉTODOS PARA RECUPERAR LISTAS DE PASOS  ****************************************
	 */

	
	// recupera los workitems activos de 1 proceso (mapa) para un paso corriente 
	public List<WStepWork> getActiveSteps (
			Integer idObject, String idObjectType, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		// TODO: filtrar para el usuario que lo solicita		
		return new WStepWorkDao().getActiveSteps(idObject, idObjectType, currentUser);
		
		
	}
	
	
		

	// retrieve workitems for an userId and for a 
	// status: null=all, A=alive P=Processed
	public List<WStepWork> getStepListByUser(
			Integer userId, Integer idProcess, String status, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		return new WStepWorkDao().getStepListByUser(userId, idProcess, status);
		
		
	}

	
	
	// retrieves workitems for a process
	// status: null=all, A=alive P=Processed
	public List<WStepWork> getStepListByProcess(
			Integer idProcess, String status, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		// TODO: filtrar para el usuario que lo solicita		
		return new WStepWorkDao().getStepListByProcess(idProcess, status);
		
		
	}


	// retrieves workitems for a process and a step ( idCurrentStep ) 
	public List<WStepWork> getStepListByProcess(
			Integer idProcess, Integer idCurrentStep, Integer currentUser, String status ) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		// TODO: filtrar para el usuario que lo solicita
		
		return new WStepWorkDao().getStepListByProcess(idProcess, idCurrentStep, status);
		
		
	}

	public List<WStepWork> getStepListByProcess (
			Integer idProcess, Integer idCurrentStep, String status,
			Integer userId, boolean isAdmin, 
			Date arrivingDate, Date openedDate,	Date deadlineDate, String filtroInstruccionesYNombrePaso  ) 
	throws WStepWorkException {
		
		return new WStepWorkDao()
						.getStepListByProcess(idProcess, idCurrentStep, status,
												userId, isAdmin, 
													arrivingDate, openedDate, deadlineDate, filtroInstruccionesYNombrePaso);
		
	}
	
	
	
			

	// recupera los workitems de 1 objeto para 1 proceso dado
	public List<WStepWork> getStepListByProcess(
			Integer idProcess, Integer idObject, String idObjectType, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		
		return new WStepWorkDao().getStepListByProcess(idProcess, idObject, idObjectType, currentUser);
		
	}


	//rrl 20110118: recupera los workitems de 1 objeto dado
	public List<WStepWork> getStepListByIdObject(
			Integer idObject, String idObjectType, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		return new WStepWorkDao().getStepListByIdObject(idObject, idObjectType, currentUser);
		
	}
	
	
	/*
	 ********************** MÉTODOS PARA TRABAJO DEL WORKFLOW  ****************************************
	 */

	// lanza un workflow
	public Integer run(
			Integer idProcess, Integer idObject, String idObjectType, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
	
		
		if ( ! existsActiveProcess(idProcess, idObject, idObjectType, currentUser) ) {
		
			WProcessDef process = new WProcessDefBL().getWProcessDefByPK(idProcess, currentUser);
			WStepDef stepDef = new WStepDefBL().getWStepDefByPK(process.getBeginStep().getId(), currentUser);
			
			WStepWork stepWork = new WStepWork();
	
			Date now = new Date();
	
			// seteo paso 
			_setStepWork(idObject, idObjectType, currentUser, process, stepDef, stepWork,
					now);
			
			Integer idStepWork = this.add(stepWork, currentUser); // inserta en la tala de step work
			
			return idStepWork;

		} else {
			throw new WStepWorkException("WStepWorkBL: Already exists a step for this [process id:"
					+idProcess
					+" and object type " + idObjectType 
					+" with id:"+idObject+"]") ;
		}
		
	}

	// procesa 1 paso - devuelve la cantidad de nuevas rutas lanzadas ... ( workitems generados ... )
	public Integer processStep (
			Integer idStepWork, Integer idResponse, String comments, WRuntimeSettings runtimeSettings,
			Integer idProcess, Integer idObject, String idObjectType, Integer currentUser ) 
	throws WProcessDefException, WStepDefException, WStepWorkException, WStepSequenceDefException, 
			WStepLockedByAnotherUserException, WStepNotLockedException, WUserDefException {
	
		Integer qtyNewRoutes=0;
		
		// 1 - validar que el step esté activo y no haya sido tocado por nadie en el interín
		WStepWork storedStep = new WStepWorkBL().getWStepWorkByPK(idStepWork, currentUser);
		
		_checkPreProcessStepWork(idStepWork, currentUser, idObject, idObjectType, storedStep); // step is active an is not processed nor blocked by another

		
		// 2 - validar que la respuesta esté dentro de las válidas
		_checkValidResponse(idStepWork, idResponse, storedStep);
		
		// 3 - validar que el usuario es válido para realizar la tarea ...( ver si vale la pena )
		
		if ( !idResponse.equals(TURNBACK) ) {

			qtyNewRoutes = _executeProcessStep(runtimeSettings, currentUser, storedStep);
		} else {

			qtyNewRoutes = _executeTurnBack(runtimeSettings, currentUser, storedStep);
			
		}
		
		return qtyNewRoutes; // devuelve la cantidad de nuevas rutas generadas ...
		
	}

	// devuelve 1 paso sin bloquearlo ...
	public WStepWork getStep (
			Integer idStepWork, Integer currentUser ) 
	throws WStepNotLockedException, WStepLockedByAnotherUserException, WStepWorkException {
	
		return new WStepWorkBL().getWStepWorkByPK(idStepWork, currentUser);
		
	}
	
	// devuelve 1 paso y lo deja lockeado
	public WStepWork getStepWithLock (
			Integer idStepWork, Integer currentUser ) 
	throws WStepLockedByAnotherUserException,  CantLockTheStepException, WStepWorkException, WUserDefException {
	
		WStepWork storedStep;
		try {
			storedStep = new WStepWorkBL().getWStepWorkByPK(idStepWork, currentUser);
		} catch (WStepWorkException e) {
			String message = "The indicated step ("
				+ idStepWork
				+ ") can't be retrieved ..." 
				+ e.getMessage() + " - "
				+ e.getCause();
			logger.warn(message);
			storedStep=null;
			throw new WStepWorkException(message);
		}
		
		Integer idObject = storedStep.getIdObject();
		String idObjectType = storedStep.getIdObjectType();
		
		try {
			
			_checkPreLockStepWork(idStepWork, currentUser, idObject, idObjectType, storedStep); // step is active an is not processed nor locked by another user

			_setOpenInfo(currentUser, storedStep);
			
			this._lockStep(storedStep, currentUser);
			
			
		} catch ( WStepWorkException swe ) {  // if can't lock it returns exception ...
				
				String message = "The indicated step ("
					+ idStepWork
					+ ") can't be locked ..." 
					+ swe.getMessage() + " - "
					+ swe.getCause();
				logger.warn(message);
				storedStep=null;
				throw new CantLockTheStepException(message);
		} 
		
		return storedStep;

	}

	public void checkLock( Integer idStepWork, Integer currentUser, boolean isAdminUser ) throws WStepLockedByAnotherUserException {
		
		try {
			
			WStepWork stepToCheck = new WStepWorkBL().getWStepWorkByPK(idStepWork, currentUser);
			if ( stepToCheck.getLockedBy()!=null && !currentUser.equals(stepToCheck.getLockedBy().getId()) ) {  // nes 20111218
				String message="The step is locked by another user ...";
				throw new WStepLockedByAnotherUserException(message);		
			}
			
		} catch (WStepWorkException e) {
			String message="Can't get the step ...";
			logger.error(message);
			e.printStackTrace();	
		}
		
		
	}
	

	public void checkStatus( Integer idStepWork, Integer currentUser, boolean isAdminUser ) throws WStepAlreadyProcessedException  {
		
		try {
			
			WStepWork stepToCheck = new WStepWorkBL().getWStepWorkByPK(idStepWork, currentUser);
			if ( stepToCheck.getDecidedDate()!=null || stepToCheck.getPerformer()!=null ) {
				String message="The step already was processed ...";
				throw new WStepAlreadyProcessedException(message);		
			}
			
		} catch (WStepWorkException e) {
			String message="Can't get the step ...";
			logger.error(message);
			e.printStackTrace();	
		}
		
		
	}
	
	// unlock given step
	public Boolean unlockStep (
			Integer idStepWork, Integer currentUser, boolean isAdmin ) 
	throws WStepNotLockedException, WStepLockedByAnotherUserException, WStepWorkException {
	
		// get the step
		WStepWork stepToUnlock = 
				new WStepWorkBL().getWStepWorkByPK(idStepWork, currentUser);
		
		
		// checks it and unlock
		if ( stepToUnlock.isLocked() && 
				( stepToUnlock.getLockedBy().getId().equals(currentUser) || // nes 20111218
						isAdmin ||
						currentUser.equals(OMNIADMIN) ) ) {

			this._unlockStep(stepToUnlock, currentUser, isAdmin);

		} else if ( ! stepToUnlock.isLocked() ){
			String message = "The indicated step ("+ stepToUnlock.getId() +")is not locked " ;
			logger.debug(message);
			throw new WStepNotLockedException(message);		
		} else {
			String message = "The indicated step ("+ stepToUnlock.getId() +")is locked by another user ... " ;
			logger.debug(message);
			throw new WStepLockedByAnotherUserException(message);		
			
		}
		
		return true;

	}
	
	// lock given step
	// isAdmin 
	public Boolean lockStep (
			Integer idStepWork, Integer lockUserId, Integer currentUser, boolean isAdminUser ) 
	throws WStepNotLockedException, WStepLockedByAnotherUserException, WStepWorkException {
	
		// get the step
		WStepWork stepToLock = 
				new WStepWorkBL().getWStepWorkByPK(idStepWork, currentUser);
		
		// 1st check user permissions
		

		_lockStep(stepToLock, currentUser);
		
		return true;

	}
	
	private void _setOpenInfo(Integer currentUser, WStepWork storedStep) throws WUserDefException {
		// set step opened date and user
		if ( storedStep.getOpenedDate()==null ) {
			storedStep.setOpenedDate(new Date());
			storedStep.setOpenerUser(new WUserDefBL().getWUserDefByPK(currentUser));
		}
	}
	
	// locks a step. If the step is locked by same user, does nothing but don't return exception ...
	private void _lockStep( WStepWork stepToLock, Integer currentUser ) throws WStepWorkException, WStepLockedByAnotherUserException {
		
		// if step is not locked then locks it !
		if ( !stepToLock.isLocked() ) {
			Date now = new Date();
			
			stepToLock.setLocked(true);
			stepToLock.setLockedBy(new WUserDef(currentUser));
			stepToLock.setLockedSince(now);
			
			this.update(stepToLock, currentUser);
			
		} else {
			if ( !stepToLock.getLockedBy().getId().equals(currentUser)) { 
				String message = "The indicated step ("+ stepToLock.getId() +") is already locked by another user ..." ;
				logger.info(message);
				throw new WStepLockedByAnotherUserException(message);
			}
		}
	}
	
	// unlocks a step. 
	private void _unlockStep( WStepWork stepToUnlock, Integer currentUser, boolean isAdmin ) throws WStepWorkException, WStepLockedByAnotherUserException {
		
		// if step is not locked then locks it !
		if ( stepToUnlock.isLocked() ) {

			stepToUnlock.setLocked(false);
			stepToUnlock.setLockedBy(null);
			stepToUnlock.setLockedSince(null);
			this.update(stepToUnlock, currentUser);
						
			if ( isAdmin ) {
				logger.info("Step: "+stepToUnlock.getId()+" was unlocked by admin:"+currentUser);
			}
		} 
	}
	
	private Integer _executeProcessStep(
			WRuntimeSettings runtimeSettings, Integer currentUser,  WStepWork storedStep ) 
	throws WStepWorkException, WStepSequenceDefException, WUserDefException {
		
		Integer qty=0;
		Date now = new Date();
		WStepWork newStep = new WStepWork();
		
		// 4 - carga secuencia siguiente (rutas salientes desde el currentStep
		List<WStepSequenceDef> lnextSteps = 
				new WStepSequenceDefBL()
					.getWStepSequenceDefs(
							storedStep.getProcess().getId(), 
							storedStep.getVersion(), 
							storedStep.getCurrentStep().getId(),
							currentUser);
		
		// TODO: urgentemente definir transaccion aquí ...
		
		for (WStepSequenceDef seq: lnextSteps ) {
			if (seq.isAfterAll() ||
				_hasInValidResponseList(seq.getValidResponses(), storedStep.getCurrentStep().getResponse()) ){
				
				qty++;
				newStep=null;
				
				_setNewStep(newStep, storedStep, seq.getToStep(), runtimeSettings, currentUser, now );
				
				this.add(newStep, currentUser);
				
			}
		}
		
		_setCurrentStepToProcessed( storedStep, runtimeSettings, currentUser, now );
		this.update(storedStep, currentUser);
		
		return qty;
	}
	
	private Integer _executeTurnBack (
			WRuntimeSettings runtimeSettings, Integer currentUser,  WStepWork storedStep ) 
	throws WStepWorkException, WStepSequenceDefException, WUserDefException {
		
		Integer qty=0;
		Date now = new Date();

		WStepWork newStep = new WStepWork();
		WStepDef toStep = storedStep.getPreviousStep();
		
		
		// TODO: urgentemente definir transaccion aquí ...
	
		qty++;
		
		_setNewStep(newStep, storedStep, toStep, runtimeSettings, currentUser, now );
		
		this.add(newStep, currentUser);
	
		_setCurrentStepToProcessed( storedStep, runtimeSettings, currentUser, now );
		this.update(storedStep, currentUser);
		
		return qty;
	}
	
	
	private void _setCurrentStepToProcessed (
			WStepWork storedStep, WRuntimeSettings runtimeSettings, Integer currentUser, Date now ) throws WUserDefException {
		

		
		storedStep.setDecidedDate(now);
		storedStep.setPerformer(new WUserDefBL().getWUserDefByPK(currentUser));
		
		// timestamp

		storedStep.setModUser(currentUser);
		storedStep.setModDate(now);
		
		
		
	}

	private void _setStepWork(Integer idObject, String idObjectType,
			Integer currentUser, WProcessDef process, WStepDef stepDef,
			WStepWork stepWork, Date now) {
		
		stepWork.setPreviousStep(null); // como no viene de ningún lado el previous es null
		stepWork.setCurrentStep(process.getBeginStep());
		
		stepWork.setIdObject(idObject);
		stepWork.setIdObjectType(idObjectType);
		
		stepWork.setArrivingDate(new Date());
		
		stepWork.setTimeUnit(stepDef.getTimeUnit());
		stepWork.setAssignedTime(stepDef.getAssignedTime()); // cantidad de tiempo asignado para resolver el trabajo
		stepWork.setDeadlineDate(stepDef.getDeadlineDate()); // fecha de deadline de la tarea
		stepWork.setDeadlineTime(stepDef.getDeadlineTime()); // hora de deadline de la tarea  ( si no hay fecha se asume que la hora es la hora del día arrivingDate)
		stepWork.setReminderTimeUnit(stepDef.getReminderTimeUnit());
		stepWork.setReminderTime(stepDef.getReminderTime());
		
		// indico las asignaciones que tenga este paso
//		WStepWorkAssignment swa = new WStepWorkAssignment();
//		
//		for (WStepAssignedDef assigned: stepDef.getAssigned()){
//			
//			
//			if (assigned.getAssignedToObject().equals(ROLE)){
//				swa.setIdAssignedRole(new Integer(assigned.getAssignedTo()));
//				swa.setIdAssignedUser(null);
//			} else {
//				swa.setIdAssignedRole(null);
//				swa.setIdAssignedUser(assigned.getAssignedTo());
//			}
//			
//			// timestamp
//			swa.setInsertDate(now);
//			swa.setModDate(now);
//			swa.setInsertUser(currentUser);
//			swa.setModUser(currentUser);
//			
//			stepWork.getAssignedTo().add(swa);
//		}
		
		stepWork.setOpenedDate(null);
		stepWork.setOpenerUser(null);
		stepWork.setDecidedDate(null);
		stepWork.setPerformer(null);
		
		// timestamp
		stepWork.setInsertUser( new WUserDef(currentUser) );
		stepWork.setModUser(currentUser);
		stepWork.setModDate(now);
	}

	
	private void _setNewStep (
			WStepWork newStep, WStepWork storedStep, WStepDef toStep,  
			WRuntimeSettings runtimeSettings, Integer currentUser, Date now ) {
		
		newStep.setCurrentStep(toStep);
		newStep.setPreviousStep(storedStep.getCurrentStep());
		
		newStep.setIdObject(storedStep.getIdObject());
		newStep.setIdObjectType(storedStep.getIdObjectType());
		
		newStep.setArrivingDate(now);
		
		// si se permiten modificar estos valores en runtime se toman del runtimeSettings, si no de la
		// propia definición del paso ...
		if (newStep.getCurrentStep().isRuntimeModifiable()) {
			newStep.setTimeUnit(runtimeSettings.getTimeUnit());
			newStep.setAssignedTime(runtimeSettings.getAssignedTime()); // cantidad de tiempo asignado para resolver el trabajo
			newStep.setDeadlineDate(runtimeSettings.getDeadlineDate()); // fecha de deadline de la tarea
			newStep.setDeadlineTime(runtimeSettings.getDeadlineTime()); // hora de deadline de la tarea  ( si no hay fecha se asume que la hora es la hora del día arrivingDate)
			newStep.setReminderTimeUnit(runtimeSettings.getReminderTimeUnit());
			newStep.setReminderTime(runtimeSettings.getReminderTime());			
		} else {
			newStep.setTimeUnit(newStep.getCurrentStep().getTimeUnit());
			newStep.setAssignedTime(newStep.getCurrentStep().getAssignedTime()); // cantidad de tiempo asignado para resolver el trabajo
			newStep.setDeadlineDate(newStep.getCurrentStep().getDeadlineDate()); // fecha de deadline de la tarea
			newStep.setDeadlineTime(newStep.getCurrentStep().getDeadlineTime()); // hora de deadline de la tarea  ( si no hay fecha se asume que la hora es la hora del día arrivingDate)
			newStep.setReminderTimeUnit(newStep.getCurrentStep().getReminderTimeUnit());
			newStep.setReminderTime(newStep.getCurrentStep().getReminderTime());
		}
		
		// indico las asignaciones que tenga este paso
//		WStepWorkAssignment swa = new WStepWorkAssignment();
//		
//		for (WStepAssignedDef assigned: newStep.getCurrentStep().getAssigned()){
//
//			if (assigned.getAssignedToObject().equals(ROLE)){
//				swa.setIdAssignedRole(new Integer(assigned.getAssignedTo()));
//				swa.setIdAssignedUser(null);
//			} else {
//				swa.setIdAssignedRole(null);
//				swa.setIdAssignedUser(assigned.getAssignedTo());
//			}
//			
//			// timestamp
//			swa.setInsertDate(now);
//			swa.setModDate(now);
//			swa.setInsertUser(currentUser);
//			swa.setModUser(currentUser);
//			
//			newStep.getAssignedTo().add(swa);
//		}
		
		newStep.setOpenedDate(null);
		newStep.setOpenerUser(null);
		newStep.setDecidedDate(null);
		newStep.setPerformer(null);
		
		// timestamp
		newStep.setInsertUser( new WUserDef(currentUser) );
		newStep.setModUser(currentUser);
		newStep.setModDate(now);
		
		
		
	}
	
	// chequea que dentro si la ruta está dentro de las respuestas indicadas ...
	private boolean _hasInValidResponseList (
			String validResponses, Set<WStepResponseDef> responses) {
		logger.debug("---> validResponses:"+validResponses+" - respuestas:"+responses.toString());
		
		
		for (WStepResponseDef response: responses) {
			if (validResponses.contains(response+",")) return true;
		} 
		return false;
	}


	private void _checkPreLockStepWork(
			Integer idStepWork, Integer currentUser, Integer idObject, String idObjectType, WStepWork storedStep ) 
	throws WStepWorkException, WStepLockedByAnotherUserException {

		// los dos primeros controles pueden ser reduntantes pero pienso que obligan
		// al implementador a mandar además del step el id del objeto que está modificando
		// como para que no la lie y mande otra cosa y piense que procesó algo que en
		// realidad no era ...
		
		// Si el objeto que me indican es nulo devuelvo
		if ( idObject== null || idObject==0 || idObjectType==null ) {
			String message = "The indicated object is null or 0 (idObject:["
								+ idObject  +"] - type:[" + idObjectType
								+ "])";

			logger.debug(message);
			throw new WStepWorkException(message);
		}
		
		// Si no pertenecen al mismo objeto me vuelvo ...
		if ( storedStep.getIdObject()!=idObject ) {
			String message = "The indicated step have a diferent object than indicated ("
								+ idObject  +" - " + idObjectType
								+ ")";
			logger.debug(message);
			throw new WStepWorkException(message);
		}
		
		// Si ya está resuelto aviso y me vuelvo ...
		if (storedStep.getDecidedDate()!=null) {
			String message = "The indicated step ("
								+ idStepWork
								+ ") already was processed by date:"
								+ storedStep.getDecidedDate()
								+ " by: "+ storedStep.getPerformer() 
								+ (storedStep.isAdminProcess()?" as Admin":"");
			logger.debug(message);
			throw new WStepWorkException(message);
		}
	}
	
	private void _checkPreProcessStepWork(
			Integer idStepWork, Integer currentUser, Integer idObject, String idObjectType, WStepWork storedStep ) 
	throws WStepWorkException, WStepLockedByAnotherUserException, WStepNotLockedException {
		// Si no está bloqueado aviso y me vuelvo ...
		if ( !storedStep.isLocked() ) {
			String message = "The indicated step ("
								+ idStepWork
								+ ") must be locked before proccess it ..." ;
			logger.debug(message);
			throw new WStepNotLockedException(message);
		}
		
		// Si está bloqueado por otro usuario aviso y me vuelvo ...
		if ( !storedStep.getLockedBy().getId().equals(currentUser) ) { // nes 20111218
			String message = "The indicated step ("
								+ idStepWork
								+ ") is locked by another user:"
								+ storedStep.getLockedBy()
								+ " since: "+ storedStep.getLockedSince();
			logger.debug(message);
			throw new WStepLockedByAnotherUserException(message);
		}
	}
	
	private void _checkValidResponse(
			Integer idStepWork, Integer idResponse,	WStepWork storedStep)
	throws WStepWorkException {
		
		Boolean validResponse=false;
		if ( idResponse==TURNBACK ) {
			validResponse=true;
		} else {
			for (WStepResponseDef response: storedStep.getCurrentStep().getResponse()) {
				if (response.getId()==idResponse) { validResponse=true; break; } 
			}
		}
		if ( !validResponse ) {
				String message = "The indicated response ("
					+ idResponse
					+ ") for step :"
					+ idStepWork
					+ " is invalid ...";
				
				//PONERLOG
				throw new WStepWorkException(message);
		}
	}
	
	//rrl 20101216
	// recupera los workitems activos de 1 proceso (mapa)
	// llegó el: es una fecha
	// revisado el: es una fecha
	// fecha limite: es una fecha
	// el status puede ser: null=todos, A=vivos P=Procesados
	// el usuario que lo solicita
	public List<WStepWork> getStepListByProcessName (
			Integer idProcess,	Date arrivingDate, Date openedDate,	Date deadlineDate, String status, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		return new WStepWorkDao().getStepListByProcessName(idProcess, arrivingDate, openedDate, deadlineDate, status, currentUser);
	}
	
	public List<StepWorkLight> getWorkingStepListFinder(Integer processIdFilter, 
			Integer stepIdFilter, String stepTypeFilter, String referenceFilter, Integer idWorkFilter, 
			Date initialArrivingDateFilter, Date finalArrivingDateFilter, boolean estrictArrivingDateFilter,  		
			Date initialOpenedDateFilter, Date finalOpenedDateFilter, boolean estrictOpenedDateFilter, 		
			Date initialDeadlineDateFilter, Date finalDeadlineDateFilter, boolean estrictDeadlineDateFilter, 		
			Date initialDecidedDateFilter, Date finalDecidedDateFilter, boolean estrictDecidedDateFilter, 		
			String action, boolean onlyActiveWorkingProcessesFilter)
	throws WStepWorkException {

		return new WStepWorkDao().getWorkingStepListFinder(processIdFilter, stepIdFilter, 
				stepTypeFilter, referenceFilter, idWorkFilter, initialArrivingDateFilter, 
				finalArrivingDateFilter, estrictArrivingDateFilter, 
				initialOpenedDateFilter, finalOpenedDateFilter, estrictOpenedDateFilter,
				initialDeadlineDateFilter, finalDeadlineDateFilter, estrictDeadlineDateFilter, 
				initialDecidedDateFilter, finalDecidedDateFilter, estrictDecidedDateFilter, 
				action, onlyActiveWorkingProcessesFilter);
		
	}
	
}
	