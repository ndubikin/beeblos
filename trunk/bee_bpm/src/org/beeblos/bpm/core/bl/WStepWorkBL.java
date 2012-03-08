package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;
import static org.beeblos.bpm.core.util.Constants.DEFAULT_PROCESS_STATUS;
import static org.beeblos.bpm.core.util.Constants.OMNIADMIN;
import static org.beeblos.bpm.core.util.Constants.PROCESS_STEP;
import static org.beeblos.bpm.core.util.Constants.TURNBACK_STEP;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepWorkDao;
import org.beeblos.bpm.core.dao.WUserRoleDao;
import org.beeblos.bpm.core.email.model.Email;
import org.beeblos.bpm.core.error.CantLockTheStepException;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.error.WStepAlreadyProcessedException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepLockedByAnotherUserException;
import org.beeblos.bpm.core.error.WStepNotLockedException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessStatus;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepUser;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.WUserEmailAccounts;
import org.beeblos.bpm.core.model.noper.StepWorkLight;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.model.noper.WRuntimeSettings;




public class WStepWorkBL {
	
	private static final Log logger = LogFactory.getLog(WStepWorkBL.class.getName());
	
	public WStepWorkBL (){
		
	}
	
	public Integer add(WStepWork stepw, Integer currentUser) throws WStepWorkException {
		
		logger.debug("add() WStepWork - CurrentStep-Work: ["+stepw.getCurrentStep().getName()+"-"+stepw.getwProcessWork().getReference()+"]");
		
		// timestamp & trace info
		stepw.setArrivingDate(new Date());
		stepw.setInsertUser( new WUserDef(currentUser) );
		stepw.setModDate( DEFAULT_MOD_DATE);
		stepw.setModUser(currentUser);
		return new WStepWorkDao().add(stepw);

	}
	
	
	// TODO: ES NECESARIO METER CONTROL TRANSACCIONAL AQUÍ PARA ASEGURAR QUE O SE GRABAN AMBOS REGISTROS O NINGUNO.
	// AHORA MISMO SI EL INSERT DEL WORK NO DA ERROR Y POR ALGUN MOTIVO NO SE PUEDE INSERTAR EL STEP, QUEDA EL WORK AGREGADO PERO SIN STEP ...
	public Integer start(WProcessWork work, WStepWork stepw, Integer currentUser) throws WStepWorkException, WProcessWorkException {
		
		logger.debug("start() WStepWork - work:"+work.getReference()+" CurrentStep: ["+stepw.getCurrentStep().getName()+"]");
		
		Integer workId;
		
		if ( work.getId()==null || work.getId()==0 ) {
			
			// dml 20120217
			if (work.getStatus() == null) {
				work.setStatus(new WProcessStatus(DEFAULT_PROCESS_STATUS));
			}
			
			WProcessWorkBL wpbl = new WProcessWorkBL(); 
			workId = wpbl.add(work, currentUser);
			work = wpbl.getWProcessWorkByPK(workId	, currentUser); // recovers persisted work to assure all propreties are correctely loaded in the object
			
		} else {
			throw new WStepWorkException("Can't start new workflow with an existing work (work id:"+work.getId()+")");
		}
		
		stepw.setwProcessWork(work);
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
	
	//rrl 20110118: recupera los workitems de 1 objeto dado
	public List<WStepWork> getStepListByIdWork(
			Integer idWork, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		return new WStepWorkDao().getStepListByIdWork(idWork, currentUser);
		
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
			Integer idStepWork, Integer idResponse, /*String comments,*/ WRuntimeSettings runtimeSettings,
			/*Integer idProcess, Integer idObject, String idObjectType, */Integer currentUser,
			boolean isAdminProcess, String typeOfProcess) 
	throws WProcessDefException, WStepDefException, WStepWorkException, WStepSequenceDefException, 
			WStepLockedByAnotherUserException, WStepNotLockedException, WUserDefException, WStepAlreadyProcessedException {

		Integer qtyNewRoutes=0;


		// dml 20120308 - añadido proveniente de PasoBean y que no existia en la lógica de esta operación.
		this.checkLock(idStepWork, currentUser, false); // verifies the user has the step locked before process it ...

		// dml 20120308 - añadido proveniente de PasoBean y que no existia en la lógica de esta operación.
		this.checkStatus(idStepWork, currentUser, false); // verifies step is process pending at this time ...
		
		
		// dml 20120308 - añadido proveniente de PasoBean. Recargamos el storesStep completo mediante el idStepWork
		// que le pasamos desde el PasoBean
		WStepWork storedStep = new WStepWorkBL().getWStepWorkByPK(idStepWork, currentUser);

		// dml 20120308 - este método lo comento ya que se realiza (al igual que las dos llamadas a métodos
		// privados de abajo que provenian del PasoBean) tanto desde _executeProcessStep(...) como desde
		// _executeTurnBack(...)
/*		List<WStepSequenceDef> routes 
						= new WStepSequenceDefBL()
									.getWStepSequenceDefs(
											storedStep.getProcess().getId(), // process
											storedStep.getVersion(), 		 // version
											storedStep.getCurrentStep().getId(), // current step id
											currentUser);			
*/		

		// dml 20120308 - SE HACE ESTA LLAMADA EN EL _executeProcessStep y desde el _executeTurnBack 
		// (metodo _setCurrentStepToProcessed)
		//_setCurrentWorkitemToProcessed( storedStep, idResponse, currentUser );
		
		// dml 20120308 - La llamada a este método ya se obvia ya que dentro del _executeProcessStep
		// hacemos el bucle de creación de newStep que se realizaba en este mismo llamando al método
		// _setNextStep(...) y desde el _executeTurnBack al igual con el "new WStepWork()"
		//_startNewWorkitems( routes, idResponse , currentUser, runtimeSettings, storedStep, isAdminProcess); 

		
		/* dml 20120308 - Lógica previa a traer toda la lógica existente en PasoBean para procesar un WStepWork

		// 1 - validar que el step esté activo y no haya sido tocado por nadie en el interín
		WStepWork storedStep = new WStepWorkBL().getWStepWorkByPK(idStepWork, currentUser);
		
		_checkPreProcessStepWork(idStepWork, currentUser, idObject, idObjectType, storedStep); // step is active an is not processed nor blocked by another

		// 2 - validar que la respuesta esté dentro de las válidas
		_checkValidResponse(idStepWork, idResponse, storedStep);
		
		// 3 - validar que el usuario es válido para realizar la tarea ...( ver si vale la pena )
		 */	

		
		if ( typeOfProcess.equals(PROCESS_STEP) ) {

			qtyNewRoutes = _executeProcessStep(runtimeSettings, currentUser, storedStep, idResponse, isAdminProcess);
			
		} else if ( typeOfProcess.equals(TURNBACK_STEP) ) {

			qtyNewRoutes = _executeTurnBack(runtimeSettings, currentUser, storedStep, idResponse, isAdminProcess);
			
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
	throws WStepLockedByAnotherUserException,  CantLockTheStepException, WStepWorkException {
	
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
		
		Integer idObject = storedStep.getwProcessWork().getIdObject();
		String idObjectType = storedStep.getwProcessWork().getIdObjectType();
		
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
		} catch (WUserDefException e) {
			String message = "Current user indicated does not exist or have a problem ("
					+ currentUser
					+ ") The step can't be locked ..." 
					+ e.getMessage() + " - "
					+ e.getCause();
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
			WRuntimeSettings runtimeSettings, Integer currentUser,  WStepWork storedStep, 
			Integer idResponse, boolean isAdminProcess ) 
	throws WStepWorkException, WStepSequenceDefException, WUserDefException, WStepDefException {
		
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
		
/*		
 * 		DML 20120308 - ESTE ES EL RECORRIDO GEMELO AL FOR DE ABAJO QUE FUNCIONABA BIEN EN PASOBEAN, 
 * 		CREO QUE SON SIMILARES PERO LO DEJO AQUÍ POR SI ACASO. 
 * 
		if ( routes.size()==0 ) { // no next steps - this tree ends here ...
			
			ret = false; // tree finished ...

		} else {
			
			for ( WStepSequenceDef route: routes) { // for each route
				
				if ( route.getToStep()!=null ) {
					
					if ( ( route.getValidResponses()!=null && 
								route.getValidResponses().contains(idResponse.toString().trim()+"|") ) 
							||
							route.isAfterAll() ) {

						_setNextStep(route, currentUser, runtimeSettings, currentStep, isAdminProcess);
						ret=true;
						
					}
					
					
				} else { // this route points to end tree - no action required ...

					// if ret arrives here with false, all ok; 
					// if ret arrives here with true it indicates there are another valid routes for this step
					// but no action is required
					
				}
				
			}
		}
*/		
		// dml 20120308 - Esto pasa de hacerse en PasoBean por lo que
		// se realizaba tanto si es TurnBack como si no a realizarse solo en este _executeProcessStep
		for (WStepSequenceDef seq: lnextSteps ) {
			if (seq.isAfterAll() 
					// dml 20120308 - he cambiado esta comprobacion por la original ya que me parece que no
					// realizan lo mismo (la que esta debajo), y que funcionaba bien
					//|| _hasInValidResponseList(seq.getValidResponses(), storedStep.getCurrentStep().getResponse()) ){
					|| (seq.getValidResponses()!=null 
						&& seq.getValidResponses().contains(idResponse.toString().trim()+"|") ) ){
				
				// dml 20120308 - si entra por aquí suma uno a esto, que debe ser el sustituto 
				// de devolver true o false del método gemelo que había en PasoBean
				qty++;
				
				// dml 20120308 - ¿Por qué se ponia aquí el newStep a NULL?
				//newStep=null;
				
				// dml 20120308 - Reciclado el método que existía en esta clase pero añadiendo la lógica de su
				// gemelo que existía en PasoBean, añadiendo sus variables y rellenando también algunas que estaban
				// en este método y no en PasoBean. Explicacion en su interior
				_setNewStep(newStep, storedStep, seq.getToStep(), runtimeSettings, currentUser, now, isAdminProcess );
				
				this.add(newStep, currentUser);
				
				// dml 20120308 - si esta la opcion activa enviamos emails a los users/roles indicados
				if ( newStep.getCurrentStep().isEmailNotification() ) {
					
					_emailNotificationArrivingStep(newStep);
				
				}
			}
		}
		
		// dml 20120308 - a diferencia del método de PasoBean gemelo a este, en el método es donde se cargan
		// los nuevos datos del storedStep y es en este propio metodo donde se hace el update
		// Esta llamada a este método en PasoBean se hacia antes del for, pero no tiene consecuencias
		// realizarlo aquí ya que no interfiere con el bucle al no tratar la misma variable (storedStep VS newStep)
		_setCurrentStepToProcessed( storedStep, runtimeSettings, currentUser, now, idResponse );
		this.update(storedStep, currentUser);
		
		return qty;
		
	}
	
	private Integer _executeTurnBack (
			WRuntimeSettings runtimeSettings, Integer currentUser,  WStepWork storedStep, 
			Integer idResponse, boolean isAdminProcess ) 
	throws WStepWorkException, WStepSequenceDefException, WUserDefException, WStepDefException {
		
		Integer qty=0;
		Date now = new Date();

		WStepWork newStep = new WStepWork();
		WStepDef toStep = storedStep.getPreviousStep();
		
		
		// TODO: urgentemente definir transaccion aquí ...
	
		qty++;
		
		// dml 20120308 - aquí como si es turnBack es donde se le mete como nuevo paso un new WStepWork()
		_setNewStep(newStep, storedStep, toStep, runtimeSettings, currentUser, now, isAdminProcess );
		
		this.add(newStep, currentUser);
	
		// dml 20120308 - si esta la opcion activa enviamos emails a los users/roles indicados
		if ( newStep.getCurrentStep().isEmailNotification() ) {
			
			_emailNotificationArrivingStep(newStep);
		
		}

		// dml 20120308 - a diferencia del método de PasoBean gemelo a este, en el método es donde se cargan
		// los nuevos datos del storedStep y es en este propio metodo donde se hace el update
		_setCurrentStepToProcessed( storedStep, runtimeSettings, currentUser, now, idResponse );
		this.update(storedStep, currentUser);
		
		return qty;
	}
	
	
	private void _setCurrentStepToProcessed (
			WStepWork storedStep, WRuntimeSettings runtimeSettings, 
			Integer currentUser, Date now, Integer idResponse ) 
	throws WUserDefException {
		
		// dml 20120308 - añadi estos 3 sets que estaban en PasoBean
		storedStep.setLocked(false);
		storedStep.setLockedBy(null);
		storedStep.setLockedSince(null);
		
		// dml 20120307 cambie esta linea por la siguiente ya que si no fuera asi daria error antes en el bean
		// Esto no estaba en este método, esta traido de PasoBean
		//if ( lRespuestasCombo!= null && lRespuestasCombo.size()>0 ) { // nes 20121222
		if ( idResponse != null && idResponse != 0 ) { // nes 20121222
			storedStep.setResponse(idResponse.toString());
		} else {
			storedStep.setResponse("no responses list");
		}

		
		storedStep.setDecidedDate(now);
		//dml 20120308 - comente la que estaba en este metodo y le puse la que estaba en el metodo de PasoBean
		//storedStep.setPerformer(new WUserDefBL().getWUserDefByPK(currentUser));
		storedStep.setPerformer(new WUserDef(currentUser));// nes 20120126
		
		// timestamp

		storedStep.setModUser(currentUser);
		storedStep.setModDate(now);
		
		
		
	}

	private void _setStepWork(Integer idObject, String idObjectType,
			Integer currentUser, WProcessDef process, WStepDef stepDef,
			WStepWork stepWork, Date now) {
		
		stepWork.setPreviousStep(null); // como no viene de ningún lado el previous es null
		stepWork.setCurrentStep(process.getBeginStep());
		
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
			WRuntimeSettings runtimeSettings, Integer currentUser, Date now, 
			boolean isAdminProcess ) 
	throws WStepDefException {

		// dml 20120308 - Cambiada la instrucción por la que había en PasoBean
		newStep.setCurrentStep(new WStepDefBL().getWStepDefByPK(toStep.getId(), currentUser));
		//newStep.setCurrentStep(toStep);
		newStep.setPreviousStep(storedStep.getCurrentStep());
		
		newStep.setArrivingDate(now);
		
		// dml 20120308 - Nuevas intrucciones añadidas de su método gemelo PasoBean
		newStep.setAdminProcess(isAdminProcess);
		newStep.setProcess( storedStep.getProcess() );
		newStep.setVersion( storedStep.getVersion() );
		newStep.setPreviousStep( storedStep.getCurrentStep() );
		newStep.setwProcessWork(storedStep.getwProcessWork());
		// put user instructions to next step
		if ( newStep.isSendUserNotesToNextStep() ) {
			newStep.setUserInstructions(storedStep.getUserNotes());
		}
		
		
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
	
	//NOTA: VOLVER A PONER ESTE MÉTODO COMO PRIVADO CUANDO SE ACABE DE PROBAR
	// email notifications related to a new step generation
	// step arrived!!
	public void _emailNotificationArrivingStep( WStepWork newStep ) {
		
		//LO PRIMERO QUE VOY A HACER ES COMPONER LA PARTE DEL EMAIL QUE CONOZCO, PARA ESO OBTENGO EL PROCESO
		//QUE ESTARÁ RELACIONADO CON ESTE STEP DEL QUE NECESITO VARIOS DATOS
		WProcessDef process = newStep.getwProcessWork().getProcess();
		
		//CREO EL EMAIL DIRECTAMENTE CON LA LLAMADA A UN METODO PRIVADO QUE CARGA TODOS LOS VALORES QUE
		//NECESITARA PARA SU ENVIO MENOS LA LISTA "TO" QUE SE HARÁ DESPUES
		Email email = this._buildEmail(process.getwUserEmailAccounts(), 
				"Execute ProcessStep: "+newStep.getId()+" / "+newStep.getVersion());
				
		if ( newStep.getCurrentStep().isArrivingAdminNotice()) {

			
			System.out.println("ADMIN EMAIL TEMPLATE: ");
			email.setBodyText(process.getArrivingAdminNoticeTemplate().getTemplate());
			//ESTE MÉTODO PRIVADO SERÁ EL ENCARGADO DE OBTENER TODAS LAS CUENTAS DE EMAIL SIN REPETIR
			//EL SEGUNDO ATRIBUTO INDICA SI LAS CUENTAS SERÁN DE ADMINISTRADORES (true) O DE USUARIOS NORMALES (false)
			email.setListaTo(this.getReplyEmailAccounts(newStep, true));
			
			this._sendEmail(email);
			
			
		}
		
		if ( newStep.getCurrentStep().isArrivingUserNotice() ) {
			// avisar a los usuarios y roles definidos
			
			System.out.println("USER EMAIL TEMPLATE: ");
			email.setBodyText(process.getArrivingUserNoticeTemplate().getTemplate());
			//IGUAL QUE CON LOS ADMINISTRADORES PERO CON EL INDICADOR A FALSE 
			email.setListaTo(this.getReplyEmailAccounts(newStep, false));
			
			this._sendEmail(email);

		}
		
			
	}
	
	// dml 20120307
	private void _sendEmail(Email email) {
	
		//ESTAMOS IMPRIMIENDO POR PANTALLA EN VEZ DE ENVIAR EMAILS PORQUE NO TENEMOS TANTAS DIRECCIONES
		//PERO EL CODIGO COMENTADO DEBAJO FUNCIONA BIEN, POR LO MENOS PARA ENVIAR UN EMAIL A UNA LISTA
		//CON UN SOLO "TO". SI LOS BOOLEAN PARA ENVIAR EMAIL A ADMIN/USERS ESTAN DESACTIVADOS O BIEN
		//NO EXISTE NINGUN ADMIN NI NINGUN USUARIO NORMAL ASOCIADO AL WSTEPDEF NO SE IMPRIMIRA NADA
		//YA QUE LA LISTA SERÁ VACÍA, Y EN CASO DE ENVIAR TAMPOCO DEBIDO A LA COMPROBACIÓN
		System.out.println("REPLY TO: ");
		for (String emails : email.getListaTo()) {
			
			System.out.println(emails+", ");
			
		}
		
		/*	try {
			
			if (email.getListaTo().size() > 0) {
				new EnviarEmailBL().enviar(email);
			}
		} catch (EnviarEmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
	}
	
	// dml 20120307
	private Email _buildEmail(WUserEmailAccounts sendFrom, String emailSubject) {
		
		Email email = new Email();
		
		email.setFrom(sendFrom.getEmail());
		email.setIdFrom(sendFrom.getId());
		email.setPwd(sendFrom.getOutputPassword());
		
		email.setSubject(emailSubject);
		
		return email;
		
	}
	
	// dml 20120307
	private ArrayList<String> getReplyEmailAccounts(WStepWork step, boolean isAdmin){
		
		ArrayList<String> replyEmailList = new ArrayList<String>();
		
		//PRIMERO METEMOS EN LA LISTA DE REPLY TODOS LOS EMAILS DE LOS USUARIOS RELACIONADOS CON
		//ESTE PASO (COMPROBANDO SI EL WSTEPUSER ES ADMIN O NO)
		//SOLO SE INCLUIRAN EMAILS QUE NO SE HAYAN METIDO PREVIAMENTE. SI EXISTEN DOS USUARIOS
		//CON EL MISMO EMAIL SOLO SE ENVIARÁ UNA COPIA DEL MISMO
		if (step.getCurrentStep() != null){
			
			if (step.getCurrentStep().getUsersRelated() != null
					&& step.getCurrentStep().getUsersRelated().size() > 0) {
				
				for (WStepUser wsu : step.getCurrentStep().getUsersRelated()){
					
					//SI QUEREMOS IMPRIMIR ADMIN Y VIENE UN USER NORMAL PASAMOS ITERACION
					if (isAdmin != wsu.isAdmin()) {
						continue;
					}
					
					//COMPROBAMOS QUE EXISTEN USUARIO E EMAIL Y QUE NO ESTA REPETIDO
					if (this._existsAndNotRepeatEmail(wsu.getUser(), replyEmailList)){
					
						replyEmailList.add(wsu.getUser().getEmail());
							
					}					
				}			
			}
			
			//OBTENEMOS TODOS LOS ROLES DE UN WSTEPDEF, Y DE EL OBTENEMOS TODOS LOS USUARIOS
			//QUE TIENEN EL MISMO, Y SOBRE CADA UNO HACEMOS EL MISMO PROCESO DE ANTES, SI NO
			//ESTA SU EMAIL EN LA LISTA LO INCLUIMOS
			if (step.getCurrentStep().getRolesRelated() != null
					&& step.getCurrentStep().getRolesRelated().size() > 0) {
				
				for (WStepRole wsr : step.getCurrentStep().getRolesRelated()) {
					
					//SI QUEREMOS IMPRIMIR ADMIN Y VIENE UN USER NORMAL PASAMOS ITERACION
					if (isAdmin != wsr.isAdmin()) {
						continue;
					}
					
					//CON ESTE METODO PRIVADO "GETROLESUSERS" OBTENEMOS DIRECTAMENTE LA LISTA
					//DE USUARIOS RELACIONADOS CON UN ROLE MEDIANTE LA TABLA W_USER_ROLE
					for (WUserDef user : this._getRoleUsers(wsr)) {
						
						//COMPROBAMOS QUE EXISTEN USUARIO E EMAIL Y QUE NO ESTA REPETIDO
						if (this._existsAndNotRepeatEmail(user, replyEmailList)){
						
							replyEmailList.add(user.getEmail());
								
						}					
					}
				}
			}	
		}
		
		return replyEmailList;
		
	}
	
	// dml 20120307
	private boolean _existsAndNotRepeatEmail(WUserDef user, ArrayList<String> replyEmailList){
		
		if (user.getEmail() != null
				&& !"".equals(user.getEmail())){
		
			if (!replyEmailList.contains(user.getEmail())){
				
				return true;
				
			}
		}	
		
		return false;
		
	}
	
	// dml 20120307
	private List<WUserDef> _getRoleUsers(WStepRole stepRole){
		
		List<WUserDef> roleUsers = new ArrayList<WUserDef>();
		
		if (stepRole != null
				&& stepRole.getRole() != null
				&& stepRole.getRole().getId() != null) {
			
			try {
					
				roleUsers = new WUserRoleDao().getWUserDefByRole(stepRole.getRole().getId(), null);
				
			} catch (WUserDefException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return roleUsers;
		
	}
	
	// chequea que dentro si la ruta está dentro de las respuestas indicadas ...
	private boolean _hasInValidResponseList (
			String validResponses, Set<WStepResponseDef> responses) {
		logger.debug("---> validResponses:"+validResponses+" - respuestas:"+responses.toString());
		
		
		for (WStepResponseDef response: responses) {
			// dml 20120308 - CAMBIE LA COMA POR LA BARRA PARA ADAPTARLO A LO QUE ESTABA EN PasoBean
			if (validResponses.contains(response+"|"/*","*/)) return true;
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
		if ( storedStep.getwProcessWork().getIdObject()!=idObject ) {
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
	
	// dml 20120308 - Método de la antigua lógica de processStep cuya llamada se ha comentado al traer la nueva
	// lógica de PasoBean que funcionaba
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
	
	// dml 20120308 - Método de la antigua lógica de processStep cuya llamada se ha comentado al traer la nueva
	// lógica de PasoBean que funcionaba
	private void _checkValidResponse(
			Integer idStepWork, Integer idResponse, String isTurnBack,	WStepWork storedStep)
	throws WStepWorkException {
		
		Boolean validResponse=false;
		if ( isTurnBack.equals(TURNBACK_STEP) ) {
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
	
	// dml 20120308 - Método heredado INTEGRO de PasoBean, el cual se ha tomado como referencia para completar
	// su GEMELO que existía en esta clase y es el que se va a usar con las modificaciones que requeria.
	// Una vez comprobado que la nueva lógica creada funciona se puede borrar.
	private void _setCurrentWorkitemToProcessed( WStepWork stored, Integer idResponse, 
			Integer currentUser) throws WStepWorkException {
		
		stored.setDecidedDate( new Date() );
		stored.setPerformer(new WUserDef(currentUser));// nes 20120126
		stored.setLocked(false);
		stored.setLockedBy(null);
		stored.setLockedSince(null);
		// dml 20120307 cambie esta linea por la siguiente ya que si no fuera asi daria error antes en el bean
		//if ( lRespuestasCombo!= null && lRespuestasCombo.size()>0 ) { // nes 20121222
		if ( idResponse != null && idResponse != 0 ) { // nes 20121222
			stored.setResponse(idResponse.toString());
		} else {
			stored.setResponse("no responses list");
		}
		
		new WStepWorkBL().update(stored, currentUser); // actualiza el paso actual a "ejecutado" y lo desbloquea
		
	}
	
	
	// dml 20120308 - Método heredado INTEGRO de PasoBean, el cual se ha tomado como referencia para completar
	// su GEMELO que existía en esta clase y es el que se va a usar con las modificaciones que requeria.
	// Una vez comprobado que la nueva lógica creada funciona se puede borrar.
	private boolean _startNewWorkitems( 
			List<WStepSequenceDef> routes, Integer idResponse , Integer currentUser, 
			WRuntimeSettings runtimeSettings, WStepWork currentStep, boolean isAdminProcess) 
	throws WStepDefException, WStepWorkException {

		boolean ret = false;
		
		if ( routes.size()==0 ) { // no next steps - this tree ends here ...
		
			ret = false; // tree finished ...

		} else {
			
			for ( WStepSequenceDef route: routes) { // for each route
				
				if ( route.getToStep()!=null ) {
					
					if ( ( route.getValidResponses()!=null && 
								route.getValidResponses().contains(idResponse.toString().trim()+"|") ) 
							||
							route.isAfterAll() ) {

						_setNextStep(route, currentUser, runtimeSettings, currentStep, isAdminProcess);
						ret=true;
						
					}
					
					
				} else { // this route points to end tree - no action required ...

					// if ret arrives here with false, all ok; 
					// if ret arrives here with true it indicates there are another valid routes for this step
					// but no action is required
					
				}
				
			}
			
			
		}
		
		return ret;
	}

	// dml 20120308 - Método heredado INTEGRO de PasoBean, el cual se ha tomado como referencia para completar
	// su GEMELO que existía en esta clase y es el que se va a usar con las modificaciones que requeria.
	// Una vez comprobado que la nueva lógica creada funciona se puede borrar.
	private void _setNextStep( WStepSequenceDef route, Integer currentUser,
			WRuntimeSettings rS, WStepWork currentStep, boolean isAdminProcess)
			throws WStepDefException, WStepWorkException {

		WStepWork newWorkItem;
		
		newWorkItem = new WStepWork();
		
		newWorkItem.setAdminProcess( isAdminProcess );
		
		newWorkItem.setProcess( currentStep.getProcess() );
		newWorkItem.setVersion( currentStep.getVersion() );
		newWorkItem.setPreviousStep( currentStep.getCurrentStep() );
		
		newWorkItem.setCurrentStep( new WStepDefBL().getWStepDefByPK(route.getToStep().getId(), currentUser));
		
		// nes 20120126
		newWorkItem.setwProcessWork(currentStep.getwProcessWork());
		
		// nes 20120126 - esto pasó al objeto WProcessWork por lo q no hay q actualizarlo ...
//		newWorkItem.setIdObject(pasoActual.getPro.getIdObject());
//		newWorkItem.setIdObjectType(pasoActual.getIdObjectType());
//		
//		newWorkItem.setReference(pasoActual.getReference() );
//		newWorkItem.setComments(pasoActual.getComments() );
		newWorkItem.setArrivingDate( new Date() );
		
		// put user instructions to next step
		if ( currentStep.isSendUserNotesToNextStep() ) {
			newWorkItem.setUserInstructions(currentStep.getUserNotes());
		}
		
//					newWorkItem.setOpenedDate = pasoActual.openedDate;
//					newWorkItem.openerUser = pasoActual.openerUser;
//					newWorkItem.decidedDate = pasoActual.decidedDate;
//					newWorkItem.performer = pasoActual.performer;
//					newWorkItem.response = pasoActual.response;
//					newWorkItem.nextStepInstructions = pasoActual.nextStepInstructions;

		// this values may be changed by user ( depending permissions and security )
		newWorkItem.setTimeUnit( ( rS.getTimeUnit()==null? newWorkItem.getTimeUnit(): rS.getTimeUnit()) );
		newWorkItem.setAssignedTime( ( rS.getAssignedTime()==null? newWorkItem.getAssignedTime(): rS.getAssignedTime()) );
		newWorkItem.setDeadlineDate( ( rS.getDeadlineDate()==null? newWorkItem.getDeadlineDate(): rS.getDeadlineDate()) );
		newWorkItem.setDeadlineTime( ( rS.getDeadlineTime()==null? newWorkItem.getDeadlineTime(): rS.getDeadlineTime()) );
		newWorkItem.setReminderTimeUnit( ( rS.getReminderTimeUnit()==null? newWorkItem.getReminderTimeUnit(): rS.getReminderTimeUnit()) );
		newWorkItem.setReminderTime( ( rS.getReminderTime()==null? newWorkItem.getReminderTime(): rS.getReminderTime()) );


		
//					newWorkItem.locked = pasoActual.locked;
//					newWorkItem.lockedBy = pasoActual.lockedBy;
//					newWorkItem.lockedSince = pasoActual.lockedSince;

		// TODO: HAY QUE DEJAR LA POSIBILIDAD QUE ASIGNEN ESTE PASO A 1 USUARIO O ROL O GRUPO DETERMINADO EN TIEMPO
		// DE EJECUCIÓN ( PODRIA SER UNA REASIGNACIÓN O PODRIA SER UNA REDIRECCIÓN 
//					newWorkItem.setAssignedTo( this.assignedTo );

//					newWorkItem.setAssignedTo( newWorkItem.getCurrentStep().getAssigned() );
		
//					newWorkItem.insertUser = pasoActual.insertUser;
//					newWorkItem.modDate = pasoActual.modDate;
//					newWorkItem.modUser = pasoActual.modUser;		
		

		try {
			
			new WStepWorkBL().add(newWorkItem, currentUser);

		} catch (WStepWorkException e) {
			
			String mensaje = "_setNextStep: Error al intentar generar el nuevo paso : "
				+e.getMessage()+" - "+e.getCause();
			logger.error(mensaje);
			throw new WStepWorkException( mensaje );
		
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
	