package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.ALIVE;
import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;
import static org.beeblos.bpm.core.util.Constants.DEFAULT_PROCESS_STATUS;
import static org.beeblos.bpm.core.util.Constants.EMAIL_DEFAULT_SUBJECT;
import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;
import static org.beeblos.bpm.core.util.Constants.OMNIADMIN;
import static org.beeblos.bpm.core.util.Constants.PROCESS_STEP;
import static org.beeblos.bpm.core.util.Constants.TURNBACK_STEP;
import static org.beeblos.bpm.core.util.Constants.WRITE_EMAIL_TO_FILESYSTEM;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepWorkDao;
import org.beeblos.bpm.core.dao.WUserRoleDao;
import org.beeblos.bpm.core.error.CantLockTheStepException;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.error.WStepAlreadyProcessedException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepLockedByAnotherUserException;
import org.beeblos.bpm.core.error.WStepNotLockedException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WEmailAccount;
import org.beeblos.bpm.core.model.WExternalMethod;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessStatus;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepUser;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WStepWorkSequence;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.noper.StepWorkLight;
import org.beeblos.bpm.core.model.noper.WProcessDefThin;
import org.beeblos.bpm.core.model.noper.WRuntimeSettings;
import org.beeblos.bpm.core.model.noper.WStepWorkCheckObject;
import org.beeblos.bpm.core.model.util.RouteEvaluationOrder;
import org.beeblos.bpm.tm.impl.MethodSynchronizerImpl;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.email.core.bl.SendEmailBL;
import com.email.core.error.SendEmailException;
import com.email.core.model.Email;
import com.sp.common.core.error.BeeblosBLException;
import com.sp.common.util.Resourceutil;
import com.sp.common.util.StringPair;

public class WStepWorkBL {
	
	private static final String INSERT = "INSERT";
	private static final Log logger = LogFactory.getLog(WStepWorkBL.class.getName());
	
	public WStepWorkBL (){
		
	}
	
	// ######### TRANSACCION URGENTE METER TRANSACCION #####################
	
	// TODO: ES NECESARIO METER CONTROL TRANSACCIONAL AQUÍ PARA ASEGURAR QUE O SE GRABAN AMBOS REGISTROS O NINGUNO.
	// AHORA MISMO SI EL INSERT DEL WORK NO DA ERROR Y POR ALGUN MOTIVO NO SE PUEDE INSERTAR EL STEP, QUEDA EL WORK AGREGADO PERO SIN STEP ...
	/**
	 * Create a new instance of a process. This method requires ProcessWork and StepWork objects
	 * correctly filled.
	 * 
	 * There is possible to insert or start a new process in any step of the process map..
	 * 
	 * @param work
	 * @param swco
	 * @param currentUser
	 * @return
	 * @throws WStepWorkException
	 * @throws WProcessWorkException
	 * @throws WStepWorkSequenceException
	 */
	public Integer start(WProcessWork work, WStepWork stepw, Integer currentUser) 
			throws WStepWorkException, WProcessWorkException, WStepWorkSequenceException {
		
		logger.debug("start() WStepWork - work:"+work.getReference()+" CurrentStep: ["+stepw.getCurrentStep().getName()+"] ...");
		
		Integer workId;
		
		if ( work.getId()!=null && work.getId()!=0 ) {
			throw new WStepWorkException("Can't start new workflow with an existing work (work id:"+work.getId()+")");
		}
		
		// nes 20130912 - if process is not active throws exception
		if (!work.getProcessDef().isActive()) {
			logger.warn("Trying to start a new workflow referring an inactive process id:"+work.getProcessDef().getId());
			throw new WProcessWorkException("This process is inactive. Can't start a new workflow.");
		}
			
		if (work.getStatus() == null) {
			work.setStatus(new WProcessStatus(DEFAULT_PROCESS_STATUS));
		}
		
		// adds the new ProcessWork
		WProcessWorkBL wpbl = new WProcessWorkBL(); 
		workId = wpbl.add(work, currentUser);
		work = wpbl.getWProcessWorkByPK(workId, currentUser); // checks all properties was correctly stored in the object
		
		// if exists managed data set with just created work id
		if (stepw.getManagedData()!=null) {
			stepw.getManagedData().setCurrentWorkId(work.getId()); // process-work id
			System.out.println("TESTEAR ESTO QUE ESTE CARGANDO BIEN EL PROCESS-HEAD-ID");
			stepw.getManagedData().setProcessId(work.getProcessHeadId()); // process head id
			stepw.getManagedData().setOperation(INSERT);
//			swco.setManagedData(managedData);  // delegamos en el dao para que inserte el managed data
		}

		// if work was persisted ok thencontinue with step-work
		stepw.setwProcessWork(work);
		// timestamp & trace info
		stepw.setArrivingDate(new Date());
		stepw.setInsertUser( new WUserDef(currentUser) );
		stepw.setModDate( DEFAULT_MOD_DATE);
		Integer idGeneratedStep= new WStepWorkDao().add(stepw);
		
		// dml 20130827 - al inyectar insertamos un primer "log" con el primer step
		this.createStepWorkSequenceLog(null, stepw, false, 
				null, stepw.getCurrentStep(), currentUser);

		_sendEmailNotification(stepw, currentUser);

		return idGeneratedStep;
	}
	
	// procesa 1 paso - devuelve la cantidad de nuevas rutas lanzadas ... ( workitems generados ... )
	public Integer processStep (
			Integer idStepWork, Integer idResponse, /*String comments,*/ WRuntimeSettings runtimeSettings,
			/*Integer idProcess, Integer idObject, String idObjectType, */Integer currentUser,
			boolean isAdminProcess, String typeOfProcess) 
	throws WProcessDefException, WStepDefException, WStepWorkException, WStepSequenceDefException, 
			WStepLockedByAnotherUserException, WStepNotLockedException, WUserDefException, 
			WStepAlreadyProcessedException, WStepWorkSequenceException, WProcessWorkException {
		logger.debug(">>> processStep >> id:"+( idStepWork!=null?idStepWork:"null") );
		
		Date now = new Date();
		Integer qtyNewRoutes=0;

		// verifies the user has the step locked before process it ...
		// if not there may be possible an error in the process chain ...
		if (!checkLock(idStepWork, currentUser, false)) {
			throw new WStepNotLockedException("Current step is not locked. Please try process again ...");
		}

		// checks the step was not processed ... 
		this.checkStatus(idStepWork, currentUser, false); 

		// load current step from database
		WStepWork currentStep = new WStepWorkBL().getWStepWorkByPK(idStepWork, currentUser);

		// sets managed data 
		currentStep.setManagedData(runtimeSettings.getManagedData());//NOTA NESTOR: VER BIEN QUE HACEMOS AQUÍ ...
		
		// set current workitem to processed status
		_setCurrentWorkitemToProcessed( currentStep, idResponse, now, currentUser );

		// insert new steps 
		if ( typeOfProcess.equals(PROCESS_STEP) ) {

			qtyNewRoutes = _executeProcessStep(runtimeSettings, currentUser, currentStep, idResponse, isAdminProcess, now);

			// if no new routes nor alive tasks then the process work is finished ...
			if(qtyNewRoutes.equals(0) 
				&& getStepWorkCountByProcess(
						currentStep.getwProcessWork().getProcessDef().getId(),ALIVE).equals(0)  ){

					new WProcessWorkBL().finalize(currentStep.getwProcessWork(), currentUser);

			}

			
		} else if ( typeOfProcess.equals(TURNBACK_STEP) ) {

			_executeTurnBack(runtimeSettings, currentUser, currentStep, idResponse, isAdminProcess, now);
			qtyNewRoutes=1;
			
		}

		return qtyNewRoutes; // devuelve la cantidad de nuevas rutas generadas ...
		
	}


	
	public Integer add(WStepWork stepw, Integer currentUser) throws WStepWorkException {
		
		logger.debug("add() WStepWork - CurrentStep-Work: ["+stepw.getCurrentStep().getName()
				+"-"+stepw.getwProcessWork().getReference()+"]");
		
		// timestamp & trace info
		stepw.setArrivingDate(new Date());
		stepw.setInsertUser( new WUserDef(currentUser) );
		stepw.setModDate( DEFAULT_MOD_DATE);
		stepw.setModUser(currentUser);
		return new WStepWorkDao().add(stepw);

	}

	// ######### TRANSACCION URGENTE METER TRANSACCION #####################
	
	public void update(WStepWork stepw, Integer currentUser) throws WStepWorkException {
		
		logger.debug("update() WStepWork < id = "+stepw.getId()+">");
		
		if ( !stepw.equals(new WStepWorkDao().getWStepWorkByPK(stepw.getId())) ) {

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

	
	public List<WStepWork> getWStepWorks(Integer currentUser) throws WStepWorkException {

		return new WStepWorkDao().getWStepWorks();
	
	}
	
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WStepWorkException {
		 
		return new WStepWorkDao().getComboList(textoPrimeraLinea, separacion);


	}

	
	public Boolean existsActiveProcess(
			Integer processId, Integer idObject, String idObjectType, Integer currentUser ) 
	throws WStepWorkException {
		
		return new WStepWorkDao().existsActiveProcess(processId, idObject, idObjectType);
	}

	/**
	 * returns qty of existing step works for a given processDefId (process version)
	 * 
	 * @param processId
	 * @param mode: A = alive, P = processed
	 * @return
	 * @throws WStepWorkException
	 */
	public Integer getStepWorkCountByProcess (Integer processId, String mode) throws WStepWorkException {
		return new WStepWorkDao().getStepWorkCountByProcess(processId, mode);
	}
	
	/**
	 * @author dmuleiro
	 * 
	 * Returns the number of "WStepWork" registers related to a concrete "WStepDef"
	 *
	 * @param  Integer stepId
	 * @param  String mode
	 * 
	 * @return Integer
	 * 
	 * @throws WStepWorkException
	 * 
	 */
	public Integer getWorkCountByStep(Integer stepId, String mode) throws WStepWorkException {
		return new WStepWorkDao().getWorkCountByStep(stepId, mode);
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
	public List<WStepWork> getWorkListByProcessAndStatus(
			Integer idProcess, String status, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		// TODO: filtrar para el usuario que lo solicita		
		return new WStepWorkDao().getWorkListByProcessAndStatus(idProcess, status);
		
		
	}


	// retrieves workitems for a process and a step ( idCurrentStep ) 
	public List<WStepWork> getWorkListByProcessAndStep(
			Integer idProcess, Integer idCurrentStep, Integer currentUser, String status ) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		// TODO: filtrar para el usuario que lo solicita
		
		return new WStepWorkDao().getWorkListByProcessAndStep(idProcess, idCurrentStep, status);
		
		
	}

	/**
	 * Returns step work list for a process and a user
	 * If isAdmin comes with true then must assume this is an "administrator" query
	 * 
	 * @param idProcess
	 * @param idCurrentStep
	 * @param status
	 * @param userId
	 * @param isAdmin
	 * @param arrivingDate
	 * @param openedDate
	 * @param deadlineDate
	 * @param filtroComentariosYReferencia
	 * @return
	 * @throws WStepWorkException
	 */
	public List<WStepWork> getWorkListByProcess (
			Integer idProcess, Integer idCurrentStep, String status,
			Integer userId, boolean isAdmin, 
			Date arrivingDate, Date openedDate,	Date deadlineDate, 
			String filtroComentariosYReferencia) 
	throws WStepWorkException {
		
		return new WStepWorkDao()
						.getWorkListByProcess(
								idProcess, idCurrentStep, status,
								userId, isAdmin, arrivingDate, openedDate, 
								deadlineDate, filtroComentariosYReferencia);
		
	}
	
	
	
			

	// recupera los workitems de 1 objeto para 1 proceso dado
	public List<WStepWork> getWorkListByProcess(
			Integer idProcess, Integer idObject, String idObjectType, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		
		return new WStepWorkDao().getWorkListByProcess(idProcess, idObject, idObjectType, currentUser);
		
	}


	//rrl 20110118: recupera los workitems de 1 objeto dado
	public List<WStepWork> getWorkListByIdObject(
			Integer idObject, String idObjectType, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		return new WStepWorkDao().getWorkListByIdObject(idObject, idObjectType, currentUser);
		
	}
	
	//rrl 20110118: recupera los workitems de 1 objeto dado
	public List<WStepWork> getWorkListByIdWorkAndStatus(
			Integer idWork, String status, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		return new WStepWorkDao().getWorkListByIdWorkAndStatus(idWork, status, currentUser);
		
	}
	
	
	/*
	 ********************** MÉTODOS PARA TRABAJO DEL WORKFLOW  ****************************************
	 */

	// TODO NESTOR 20130731 - NO TENGO CLARO POR QUE PERO A ESTE MÉTODO NO LO LLAMA NADIE ...
	// NO SE SI ES VIEJO O SI LO USAMOS SOLO EN DAP, HAY QUE REVISAR ...
	
	// lanza un workflow
//	public Integer run(
//			Integer processId, Integer idObject, String idObjectType, Integer currentUser) 
//	throws WProcessDefException, WStepDefException, WStepWorkException, WStepSequenceDefException {
//	
//		/*
//		 * revisa que exista algún proceso activo para la tupla: processId, idObject, idObjectType
//		 * pero esto puede ser opcional, o sea habria que indicar en los parámetros del wf si se
//		 * permite lanzar mas de 1 proceso de este tipo para la tupla
//		 * 
//  		 */
//		if ( ! existsActiveProcess(processId, idObject, idObjectType, currentUser) ) {
//		
//			WProcessDef process = new WProcessDefBL().getWProcessDefByPK(processId, currentUser);
//			WStepDef stepDef = new WStepDefBL().getWStepDefByPK(process.getBeginStep().getId(), currentUser);
//			
//			WStepWork stepWork = new WStepWork();
//	
//			Date now = new Date();
//	
//			// seteo paso 
//			_setStepWork(idObject, idObjectType, currentUser, process, stepDef, stepWork,
//					now);
//			
//			Integer idStepWork = this.add(stepWork, currentUser); // inserta en la tala de step work
//			
//			return idStepWork;
//
//		} else {
//			throw new WStepWorkException("WStepWorkBL: Already exists a step for this [process id:"
//					+processId
//					+" and object type " + idObjectType 
//					+" with id:"+idObject+"]") ;
//		}
//		
//	}


	/**
	 * Returns a unlocked stepWork 
	 * 
	 * @param idStepWork
	 * @param currentUser
	 * @return
	 * @throws WStepNotLockedException
	 * @throws WStepLockedByAnotherUserException
	 * @throws WStepWorkException
	 */
	public WStepWork getStep (
			Integer idStepWork, Integer currentUser ) 
	throws WStepNotLockedException, WStepLockedByAnotherUserException, WStepWorkException {
		logger.debug(">> getStep - returns an unlocked step - idStepWork:"+(idStepWork!=null?idStepWork:"null"));
		
		return new WStepWorkBL().getWStepWorkByPK(idStepWork, currentUser);
		
	}
	
	
	// IMPLEMENTAR CORRECTAMENTE isAdmin
	// devuelve 1 paso y lo deja lockeado
	/**
	 * Returns a locked stepWork
	 * 
	 * @param idStepWork
	 * @param currentUser
	 * @return
	 * @throws WStepLockedByAnotherUserException
	 * @throws CantLockTheStepException
	 * @throws WStepWorkException
	 */
	public WStepWork getStepWithLock (
			Integer idStepWork, Integer currentUser ) 
	throws WStepLockedByAnotherUserException,  CantLockTheStepException, WStepWorkException {
		logger.debug(">> getStepWithLock idStepWork:"+(idStepWork!=null?idStepWork:"null"));
	
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

			// set open date for the stepWork
			_setOpenInfo(currentUser, storedStep);
			
			// TODO IMPLEMENTAR CORRECTAMENTE ESTO ...
			boolean isAdmin=false;
			_lockStep(storedStep.getId(), currentUser, isAdmin);
			
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
		logger.debug(">> step was locked!! idStepWork:"+(idStepWork!=null?idStepWork:"null"));
		return storedStep;

	}

	public boolean checkLock( 
			Integer idStepWork, Integer currentUser, boolean isAdminUser ) 
					throws WStepLockedByAnotherUserException {
		logger.debug(">>> checkLock ... id:"+(idStepWork!=null?idStepWork:"null"));
		
		boolean locked=false;
		
		try {
			
			locked = new WStepWorkDao().isLockedByUser(idStepWork, currentUser);
			
			if ( !locked  ) {  // nes 20140208
				logger.info("the step id:"+idStepWork+" is not locked ...");
			}
			
		} catch (WStepWorkException e) {
			String message="Can't get the step ...";
			logger.error(message);
			e.printStackTrace();	
		}
		return locked;
	}
	

	public void checkStatus( Integer idStepWork, Integer currentUser, boolean isAdminUser ) 
			throws WStepAlreadyProcessedException, WStepWorkException  {
		logger.debug(">> checkStatus idStepWork:"+(idStepWork!=null?idStepWork:"null"));
		
		try {
			
			WStepWorkCheckObject swco = 
					new WStepWorkDao().getWStepWorkCheckObjectByPK(idStepWork);
			
			if ( swco.getDecidedDate()!=null || swco.getIdPerformer()!=null ) {
				String message="This step already was processed ... id:"+idStepWork;
				throw new WStepAlreadyProcessedException(message);	
			}
			
		} catch (WStepWorkException e) {
			String message="Can't get the step to check preconditions before process it!! Please try again ...";
			logger.error(message);
			e.printStackTrace();
			throw new WStepWorkException(message);
		}
		
		
	}
	
	// unlock given step
	public Boolean unlockStep (
			Integer idStepWork, Integer currentUser, boolean isAdmin ) 
	throws WStepNotLockedException, WStepLockedByAnotherUserException, WStepWorkException {
		logger.debug(">> unlockStep idStepWork:"+(idStepWork!=null?idStepWork:"null"));
		
		// get the step
		WStepWork stepToUnlock = 
				new WStepWorkBL().getWStepWorkByPK(idStepWork, currentUser);
		
		
		// checks it and unlock
		if ( stepToUnlock.isLocked() && 
				( stepToUnlock.getLockedBy().getId().equals(currentUser) || // nes 20111218
						isAdmin ||
						currentUser.equals(OMNIADMIN) ) ) {

			this._unlockStep(stepToUnlock, currentUser, (isAdmin || currentUser.equals(OMNIADMIN)));

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
	
	// IMPLEMENTAR CORRECTAMENTE EL LOCK Y EL UNLOCK!!!!!!!!!!!!!!!!!
	// Y VER BIEN COMO DEFINIR SI ES ADMIN USER O SI ES USUARIO NORMAL EN USO DE SUS FACULTADES (PERMISOS)
	// DEFINIDOS ...
	
	// lock given step
	// isAdmin 
	public Boolean lockStep (
			Integer idStepWork, Integer lockUserId, Integer currentUser, boolean isAdminUser ) 
	throws WStepNotLockedException, WStepLockedByAnotherUserException, WStepWorkException {
		logger.debug(">> unlockStep idStepWork:"+(idStepWork!=null?idStepWork:"null"));
		
		// get the step
//		WStepWork stepToLock = 
//				new WStepWorkBL().getWStepWorkByPK(idStepWork, currentUser);
		
		// 1st check user permissions
		

		_lockStep(idStepWork, currentUser, isAdminUser);
		
		return true;

	}
	
	private void _setOpenInfo(Integer currentUser, WStepWork storedStep) throws WUserDefException {
		// set step opened date and user
		if ( storedStep.getOpenedDate()==null ) {
			storedStep.setOpenedDate(new Date());
			storedStep.setOpenerUser(new WUserDefBL().getWUserDefByPK(currentUser));
		}
	}
	
	private boolean _checkCurrentLockStatus(Integer id) {
		return true;
	}
	
	/**
	 * locks a step. If the step is locked by same user nothing to do but doesn't return exception ...
	 * 
	 * @param idStepWork
	 * @param currentUser
	 * @param isAdmin
	 * @throws WStepWorkException
	 * @throws WStepLockedByAnotherUserException
	 */
	private void _lockStep( Integer idStepWork, Integer currentUser, boolean isAdmin )
			throws WStepWorkException, WStepLockedByAnotherUserException {
		logger.debug(">> _lockStep 2 idStepWork:"+(idStepWork!=null?idStepWork:"null"));
		
		// IMPLEMENTAR BIEN ESTO Y CHEQUEAR EL USUARIO QUE LOCKEA EL PASO
		
		// if step is not locked then locks it !
		if ( _checkCurrentLockStatus(idStepWork) ) {

			Date now = new Date();

			new WStepWorkDao()
						.lockStepWork( idStepWork, now, currentUser, isAdmin );
			
			
		} else {
			//if ( !stepToLock.getLockedBy().getId().equals(currentUser)) { 
				String message = "The indicated step ("+ idStepWork +") is already locked by another user ..." ;
				logger.info(message);
				throw new WStepLockedByAnotherUserException(message);
			//}
		}
	}
	
	// unlocks a step. 
	private void _unlockStep( WStepWork stepToUnlock, Integer currentUser, boolean isAdmin ) 
			throws WStepWorkException, WStepLockedByAnotherUserException {
		
		// if step is not locked then locks it !
		if ( stepToUnlock.isLocked() ) {

			stepToUnlock.setLocked(false);
			stepToUnlock.setLockedBy(null);
			stepToUnlock.setLockedSince(null);

			// nota nes 20130805 - no puedo mandarlo al update porque se pasa por la managed table
			// y lee varias veces el paso lo q le mete un overhead importante ...
//			this.update(stepToUnlock, currentUser);
			
			// timestamp & trace info
			stepToUnlock.setModDate(new Date());
			stepToUnlock.setModUser(currentUser);
			
			new WStepWorkDao().unlockStepWork(
									  stepToUnlock.getId(),stepToUnlock.getModDate(),
									  stepToUnlock.getModUser(), isAdmin);
						
			if ( isAdmin ) {
				logger.info("Step: "+stepToUnlock.getId()+" was unlocked by admin:"+currentUser);
			}
		} 
	}
	
	/**
	 * Generates a new stepWork for each new valid route outgoing from current step.
	 * A route generates a new stepWork or nothing if the route goes to end ...
	 * Managed data is set to null in stepWork and is persisted only 1 time because
	 * the managedData is managed at "work" level, and for the instance of a process (work)
	 * we have only 1 set of managedData ...
	 * 
	 * @param runtimeSettings
	 * @param currentUser
	 * @param currentStepWork
	 * @param idResponse
	 * @param isAdminProcess
	 * @param now
	 * @return
	 * @throws WStepWorkException
	 * @throws WStepSequenceDefException
	 * @throws WUserDefException
	 * @throws WStepDefException
	 * @throws WStepWorkSequenceException
	 */
	private Integer _executeProcessStep(
			WRuntimeSettings runtimeSettings, Integer currentUser,  WStepWork currentStepWork, 
			Integer idResponse, boolean isAdminProcess, Date now ) 
	throws WStepWorkException, WStepSequenceDefException, WUserDefException, WStepDefException, WStepWorkSequenceException {
		if (logger.isDebugEnabled()){
			logger.debug(">>> _executeProcessStep >> idStepWork"+currentStepWork.getId()
					+" isAdminProcess:"+isAdminProcess
					+" idResponse:"+idResponse);
		}
		
		Integer qty=0;

		// create an emty object to build new steps
		WStepWork newStepWork = new WStepWork();
		
		
		System.out.println("VERIFICAR QUE CARGUE CORRECTAMENTE LAS RUTAS PARA EL RUTEO DEL STEP ...");
		// load routes from current step
		List<WStepSequenceDef> routes = new WStepSequenceDefBL()
												.getStepSequenceList(
														currentStepWork.getwProcessWork().getProcessDef().getId(), 
														currentStepWork.getCurrentStep().getId(),
														currentUser);
		

		logger.debug(">>> _executeProcessStep >> qty routes:"+routes.size());
		// TODO: urgentemente definir transaccion aquí ...
		
		if ( routes.size()==0 ) { // no next steps - this tree ends here ...
			
			qty = 0; // tree finished ...
		
		} else {
		
			// process each route ( generates a new step for each new valid route )
			for (WStepSequenceDef route: routes ) {
				logger.debug(">>> _executeProcessStep >> "+route.getId()+"/"+route.getName());
				
				// if corresponds to take this route....
				if (route.isAfterAll() 
						|| (route.getValidResponses()!=null 
							&& route.getValidResponses().contains(idResponse.toString().trim()+"|") ) ) {
					logger.debug(">>> _executeProcessStep >> processing route to: "+(route.getToStep()!=null?route.getToStep().getName():"end this route..."));

					if ( route.getToStep()!=null ) {
						
						qty++;
						
						_setNewWorkingStepAndInsertRec(runtimeSettings,
								currentUser, currentStepWork, isAdminProcess,
								now, newStepWork, route);
						
						// dml 20130827 - si vamos hacia adelante (procesamos) el beginStep y el endStep los marca la ruta
						this.createStepWorkSequenceLog(route, newStepWork, false, 
								route.getFromStep(), route.getToStep(), currentUser);

						// if route has external method execution then execute it!
						try {
							_executeExternalMethod(route, currentStepWork, currentUser);
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						// checks for notifications subscribers for the new step and send the emails
						_sendEmailNotification(newStepWork, currentUser);
						
						// nes 20130913
						// if route evaluation order is first true condition then breaks for loop and return
						if ( currentStepWork.getCurrentStep().getRouteEvalOrder() != null
								&& currentStepWork.getCurrentStep().getRouteEvalOrder()
									.equals(RouteEvaluationOrder.FIRST_TRUE_CONDITION.getId())) {
							break;
						}
					}
					else {  // ending routes
						
						// write step-work-sequence log file 
						this.createStepWorkSequenceLog(route, currentStepWork, false, 
								route.getFromStep(), null, currentUser);

						// if route has external method execution then execute it!
						try {
							_executeExternalMethod(route, currentStepWork, currentUser);
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						// this route points to end tree - no other action required ...
						// if ret arrives here with false, all ok; 
						// if ret arrives here with true it indicates there are another valid routes for this step
						// but no action is required
					}
				}
			}
		}
		
		return qty;
		
	}
	
	// dml 20130827 - creamos el log en WStepWorkSequence de la nueva ruta creada
	/**
	 * Creates a log record related with the route taken by this instance of process
	 * Principal function of this trace is to draw the map with taken routes
	 * 
	 * @param route
	 * @param stepWork
	 * @param sentBack
	 * @param beginStep
	 * @param endStep
	 * @param currentUserId
	 * @throws WStepWorkSequenceException
	 */
	private void createStepWorkSequenceLog(WStepSequenceDef route, WStepWork stepWork, boolean sentBack, 
			WStepDef beginStep, WStepDef endStep, Integer currentUserId) throws WStepWorkSequenceException{
		
		WStepWorkSequence sws = new WStepWorkSequence(EMPTY_OBJECT);
		sws.setStepSequence((route != null)?new WStepSequenceDef(route.getId()):null);
		sws.setStepWork((stepWork != null)?new WStepWork(stepWork.getId()):null);

		sws.setSentBack(sentBack);
		
		sws.setBeginStep((beginStep != null)?new WStepDef(beginStep.getId()):null);
		sws.setEndStep((endStep != null)?new WStepDef(endStep.getId()):null);
		
		sws.setExecutionDate(new Date());

		new WStepWorkSequenceBL().add(sws, currentUserId);
		
	}

	private void _setNewWorkingStepAndInsertRec(
			WRuntimeSettings runtimeSettings, Integer currentUser,
			WStepWork currentStepWork, boolean isAdminProcess, Date now,
			WStepWork newStepWork, WStepSequenceDef route)
			throws WStepDefException, WStepWorkException {
		
		_setNewStep(newStepWork, currentStepWork, route.getToStep(), runtimeSettings, currentUser, now, isAdminProcess );
		
		this.add(newStepWork, currentUser);
	}

	private void _sendEmailNotification(WStepWork newStep, Integer currentUserId) {
		if ( newStep.getCurrentStep().isEmailNotification()  ) {		
			try {
				_emailNotificationArrivingStep(newStep, "", currentUserId);
			} catch (SendEmailException e) {
				logger.info("SendEmailException: there is not possible sending email notification to users involved");
			} catch (Exception e) {
				logger.info("Exception: there is not possible sending email notification to users involved");
			}
		}
	}
	
	/**
	 * Execute external method associated with this route ...
	 * Route related methods will be executed when navigate the route ...
	 * 
	 * @param route
	 * @param currentStepWork
	 * @param currentUser
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private void _executeExternalMethod(
			WStepSequenceDef route, WStepWork currentStepWork, Integer currentUserId) throws InstantiationException, IllegalAccessException {
		logger.debug(">>>_executeExternalMethod check...");
		if (route.getExternalMethod()!=null && route.getExternalMethod().size()>0){
			for (WExternalMethod method: route.getExternalMethod()) {
				logger.debug(">>>_executeExternalMethod:"+method.getClassname()+"."+method.getMethodname());
				if (method.getParamlistName().length>0){
					_setParamValues(method,currentStepWork, route, currentUserId);
				}
				new MethodSynchronizerImpl().invokeExternalMethod(method, currentUserId);
			}
		}
	}
	
	/**
	 * Load current value for each required parameter in method.paramlistName ...
	 * The scope to search properties is for route, currentStepWork and currentUserId ...
	 * 
	 * @param method
	 * @param route
	 * @param currentStepWork
	 * @param currentUserId
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private void _setParamValues(WExternalMethod method, WStepWork currentStepWork, WStepSequenceDef route, Integer currentUserId) throws InstantiationException, IllegalAccessException {
		int tope=method.getParamlistName().length;
		Object[] paramValueObj = new Object[tope];
		for (int i=0;i<tope;i++) {
			String paramName=method.getParamlistName()[i];
			Class paramType = method.getParamlistType()[i];
			Object obj = new Object(); //paramType.newInstance();
			if (paramName.equals("idCurrentUser")) {
				paramValueObj[i]=currentUserId;
			} else {
				obj = _setPropertyValue(currentStepWork, route, paramName);
				paramValueObj[i]=obj;
			}
		}
		method.setParamlist(paramValueObj);

		
	}

	/**
	 * @param currentStepWork
	 * @param route
	 * @param paramName
	 */
	private Object _setPropertyValue(WStepWork currentStepWork,
			WStepSequenceDef route, String paramName) {
		Object obj=null;
		// get field from their bean ...
		try {
			if (PropertyUtils.isReadable(currentStepWork, paramName)) {
					obj = PropertyUtils.getIndexedProperty(currentStepWork, paramName);
			} else if (PropertyUtils.isReadable(currentStepWork.getwProcessWork(), paramName)) {
				obj = PropertyUtils.getProperty(currentStepWork.getwProcessWork(), paramName);
			} else if (PropertyUtils.isReadable(route, paramName)) {
				obj = PropertyUtils.getIndexedProperty(route, paramName);
			}
		} catch (IllegalAccessException e) {
			String mess = "WStepWorkBL:_setParamValues IllegalAccessException - error PropertyUtils getting property name:"
					+paramName + " "
					+e.getMessage()+" - "+e.getCause();
			obj=null;
			logger.error(mess);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			String mess = "WStepWorkBL:_setParamValues InvocationTargetException - error PropertyUtils getting property name:"
					+paramName + " "
					+e.getMessage()+" - "+e.getCause();
			obj=null;
			logger.error(mess);
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			String mess = "WStepWorkBL:_setParamValues NoSuchMethodException - error PropertyUtils getting property name:"
					+paramName + " "
					+e.getMessage()+" - "+e.getCause();
			obj=null;
			logger.error(mess);
			e.printStackTrace();
		} catch (Exception e) {
			String mess = "WStepWorkBL:_setParamValues Exception - error PropertyUtils getting property name:"
					+paramName + " "
					+e.getMessage()+" - "+e.getCause();
			obj=null;
			logger.error(mess);
			e.printStackTrace();

		}
		return obj;
	}
	
	private void _executeTurnBack (
			WRuntimeSettings runtimeSettings, Integer currentUser,  WStepWork currentStepWork, 
			Integer idResponse, boolean isAdminProcess, Date now ) 
	throws WStepWorkException, WStepSequenceDefException, WUserDefException, WStepDefException, WStepWorkSequenceException {

		WStepWork newStep = new WStepWork();
		
		// TODO: urgentemente definir transaccion aquí ...

		_setNewStep(newStep, currentStepWork, currentStepWork.getPreviousStep(), runtimeSettings, currentUser, now, isAdminProcess );
		
		this.add(newStep, currentUser);
	
		// dml 20130827 - si vamos hacia atras (turnBack) el beginStep y el endStep los marca el currentStepWork
		this.createStepWorkSequenceLog(null, currentStepWork, true, currentStepWork.getCurrentStep(),
				currentStepWork.getPreviousStep(), currentUser);
		
		_sendEmailNotification(newStep, currentUser);
		
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
			WStepWork newStepWork, WStepWork currentStepWork, WStepDef toStepDef,  
			WRuntimeSettings runtimeSettings, Integer currentUser, Date now, 
			boolean isAdminProcess ) 
	throws WStepDefException {

		// set from y to step definitions in work object
		//newStepWork.setCurrentStep(new WStepDefBL().getWStepDefByPK(toStepDef.getId(), currentUser));
		newStepWork.setCurrentStep( toStepDef );
		newStepWork.setPreviousStep(currentStepWork.getCurrentStep());
		
		newStepWork.setArrivingDate(now);
		newStepWork.setAdminProcess(isAdminProcess);
		
		System.out.println("VERIFICAR QUE EL WPROCESSWORK QUE ESTOY SETEANDO AQUÍ SEA EL DATO CORRECTO ...");
		
		newStepWork.setwProcessWork( currentStepWork.getwProcessWork() );
//		newStepWork.setVersion( currentStepWork.getVersion() );

		newStepWork.setwProcessWork(currentStepWork.getwProcessWork());

		// put run time user instructions to next step
		// dml 20120508
		if ( runtimeSettings.getInstructions() != null ) {
			newStepWork.setUserInstructions(runtimeSettings.getInstructions());
		}
		
		
		// si se permiten modificar estos valores en runtime se toman del runtimeSettings, si no de la
		// propia definición del paso ...
		if (newStepWork.getCurrentStep().isRuntimeModifiable()) {
			newStepWork.setTimeUnit(runtimeSettings.getTimeUnit());
			newStepWork.setAssignedTime(runtimeSettings.getAssignedTime()); // cantidad de tiempo asignado para resolver el trabajo
			newStepWork.setDeadlineDate(runtimeSettings.getDeadlineDate()); // fecha de deadline de la tarea
			newStepWork.setDeadlineTime(runtimeSettings.getDeadlineTime()); // hora de deadline de la tarea  ( si no hay fecha se asume que la hora es la hora del día arrivingDate)
			newStepWork.setReminderTimeUnit(runtimeSettings.getReminderTimeUnit());
			newStepWork.setReminderTime(runtimeSettings.getReminderTime());			
		} else {
			newStepWork.setTimeUnit(newStepWork.getCurrentStep().getTimeUnit());
			newStepWork.setAssignedTime(newStepWork.getCurrentStep().getAssignedTime()); // cantidad de tiempo asignado para resolver el trabajo
			newStepWork.setDeadlineDate(newStepWork.getCurrentStep().getDeadlineDate()); // fecha de deadline de la tarea
			newStepWork.setDeadlineTime(newStepWork.getCurrentStep().getDeadlineTime()); // hora de deadline de la tarea  ( si no hay fecha se asume que la hora es la hora del día arrivingDate)
			newStepWork.setReminderTimeUnit(newStepWork.getCurrentStep().getReminderTimeUnit());
			newStepWork.setReminderTime(newStepWork.getCurrentStep().getReminderTime());
		}
		
		newStepWork.setOpenedDate(null);
		newStepWork.setOpenerUser(null);
		newStepWork.setDecidedDate(null);
		newStepWork.setPerformer(null);
		
		// timestamp
		newStepWork.setInsertUser( new WUserDef(currentUser) );
		newStepWork.setModUser(currentUser);
		newStepWork.setModDate(now);
		
	}
	

	private void _emailNotificationArrivingStep( WStepWork stepWork, String subject, Integer currentUserId ) throws SendEmailException {
		
		if ( ! stepWork.getCurrentStep().isArrivingAdminNotice() && 
				! stepWork.getCurrentStep().isArrivingUserNotice()) return; 

		WProcessDefThin process = stepWork.getwProcessWork().getProcessDef();// nes 20130830
		
		if ( process == null ) {
			logger.error("there is trying to send an email and process arrives null ...");
			return;
		}
		
		if ( subject==null || "".equals(subject) ) {
			subject=EMAIL_DEFAULT_SUBJECT;
		}

		Email emailMessage = 
			this._buildEmail(
				process.getSystemEmailAccount(), 
				( subject==null || "".equals(subject)?"":subject ) );
		
		if ( stepWork.getCurrentStep().isArrivingAdminNotice() ) {
			
			System.out.println("ADMIN EMAIL TEMPLATE: ");
			
			if ( process.getArrivingAdminNoticeTemplate() != null ) {
			
				String templateWithObjectData = this._buildTemplate(process.getArrivingAdminNoticeTemplate().getTemplate(), stepWork);
			
				emailMessage.setBodyText(templateWithObjectData);
				
				//ESTE MÉTODO PRIVADO SERÁ EL ENCARGADO DE OBTENER TODAS LAS CUENTAS DE EMAIL SIN REPETIR
				//EL SEGUNDO ATRIBUTO INDICA SI LAS CUENTAS SERÁN DE ADMINISTRADORES (true) O DE USUARIOS NORMALES (false)
				emailMessage.setListaTo(this.getEmailAccountList(stepWork, true));
				
				this._sendEmail(emailMessage, currentUserId);
				
			} else {
				logger.error("there is no template associated to process or step to send email to tracker ...");
			}
			
			
		}
		
		
		
		if ( stepWork.getCurrentStep().isArrivingUserNotice() ) {
			// avisar a los usuarios y roles definidos
			
			System.out.println("USER EMAIL TEMPLATE: ");

			if (process != null 
					&& process.getArrivingUserNoticeTemplate() != null) {
			
				String templateWithObjectData = this._buildTemplate(process.getArrivingUserNoticeTemplate().getTemplate(), stepWork);
			
				emailMessage.setBodyText(templateWithObjectData);
	
				
				//IGUAL QUE CON LOS ADMINISTRADORES PERO CON EL INDICADOR A FALSE 
				emailMessage.setListaTo(this.getEmailAccountList(stepWork, false));
				
				this._sendEmail(emailMessage, currentUserId);

			} else {
				logger.error("there is no template associated to process or step to send email to Worker ...");
			}

		}
		
			
	}
	

	private void _sendEmail(Email email, Integer currentUserId) throws SendEmailException {

		try {
			
			if (email.getListaTo().size() > 0) {
				
				if ( ! WRITE_EMAIL_TO_FILESYSTEM ) {
				
					new SendEmailBL().sendEmail(email, null, null, currentUserId);
				
				} else {
					
					this._buildFile(email);
					
				}
			}
	
		} catch (SendEmailException e) {
			String message=
					"(_sendEmail) Error sending email:"
							+e.getMessage()+" - "+e.getCause()+" - "+e.getClass();
			logger.error(message);
			throw e;
		} catch (IOException e) {
			String message=
					"(_sendEmail) Error sending email:"
							+e.getMessage()+" - "+e.getCause()+" - "+e.getClass();
			logger.error(message);
			throw new SendEmailException( message  );
		} catch (BeeblosBLException e) {
			String message=
					"(_sendEmail) Error saving the email in beeblos:"
							+e.getMessage()+" - "+e.getCause()+" - "+e.getClass();
			logger.error(message);
			throw new SendEmailException( message  );
		}
		
	}
	
	// dml 20120307
	private Email _buildEmail(WEmailAccount senderEmailAccount, String emailSubject) {
		
		Email emailMessage = new Email();
		
		emailMessage.setFrom(senderEmailAccount.getEmail());
		// dml 20140313 BORRAR emailMessage.setIdFrom(senderEmailAccount.getId());
		emailMessage.setPwd(senderEmailAccount.getOutputPassword());
		
		emailMessage.setSubject(emailSubject);
		
		return emailMessage;
		
	}
	
	// dml 20120315
	private String _buildTemplate(String template, WStepWork wsw){
		
		System.out.println("TEMPLATE ANTES DE SUSTITUIR: "+ template);
		
		String pattern = ""; 
		String objectData = "";
		
		
		while (template.contains("{")){
			
			pattern = template.substring(template.indexOf("{"),template.indexOf("}")+1);
			
			objectData = 
					Resourceutil.getStringPropertyEmailTemplate(pattern, pattern.replace("{", "¿").replace("}", "?"));
			
			if (!objectData.contains("¿")){

				String tempData = this._getData(wsw, objectData);
				
				if (tempData != null) {
			
					objectData = tempData;

				} else {
					
					objectData = pattern.replace("{", "¿").replace("}", "?");
					
				}
			}	
			// dml 20120315 - si no encontramos el patron lo dejamos como viene
			template = template.replace(pattern, objectData);
				
		}
		
		System.out.println("TEMPLATE DESPUES DE SUSTITUIR: "+ template);
		
		return template;
		
	}
	
	private String _getData(WStepWork wsw, String data){
		
		try {

			if (data.contains(".")
					&& data.substring(0, data.indexOf(".")).equals("WStepWork")){
				
				data = data.substring(data.indexOf(".")+1);
			
				Class cls = Class.forName(wsw.getClass().getName());
							
				Method m = cls.getMethod("get"+data.substring(0, data.indexOf(".")), new Class[0]);
				
				//Primero obtenemos el objeto de primer nivel del WStepWork y ahora iteramos
				Object res = m.invoke(wsw, new Object[0]);
				
				while (data.contains(".")) {
					
					data = data.substring(data.indexOf(".")+1);
					
					cls = Class.forName(res.getClass().getName());
					
					if (data.contains(".")){
						m = cls.getMethod("get"+data.substring(0, data.indexOf(".")), new Class[0]);
						res = m.invoke(res, new Object[0]);
					} else {
						m = cls.getMethod("get"+data, new Class[0]);
						res = m.invoke(res, new Object[0]);
						break;
					}
				}

				return res.toString();

			} else {
				System.out.println("CLASE NO CORRESPONDE!");
			}
									
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
		return null;
		
		
	}
	
	private void _buildFile(Email email) throws IOException{
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_hh_mm_ss");
		
		String path = Resourceutil.getStringProperty("email.path.server", "");
		
		path += "notificationEmail"+sdf.format(new Date())+".txt"; 
		
		File f = new File(path);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
		System.out.println(">>>>>>> email will be writing in: "+f.getAbsolutePath() );
		
		bufferedWriter.append("SEND FROM: \n");
		bufferedWriter.append(" -> " + email.getFrom()+"\n");
		
		bufferedWriter.append("\nSEND TO: \n");
		for (String to: email.getListaTo()){
			bufferedWriter.append(" -> " + to+"\n");
		}

		bufferedWriter.append("\nSUBJECT: \n");
		bufferedWriter.append(" -> " + email.getSubject()+"\n");

		bufferedWriter.append("\nBODY MESSAGE: \n");
		bufferedWriter.append(" -> " + email.getBodyText()+"\n");

		bufferedWriter.flush();
		
		System.out.println(" - Email NO enviado, creado en la ruta: " + path);
		
		
	}
	
	private ArrayList<String> getEmailAccountList(WStepWork stepWork, boolean isAdmin){
		
		ArrayList<String> emailList = new ArrayList<String>();
		
		if (stepWork.getCurrentStep() != null){
			
			getUsersRelatedEmailList(stepWork, isAdmin, emailList);

			getRoleRelatedEmailList(stepWork, isAdmin, emailList);	
		}
		
		return emailList;
		
	}

	private void getRoleRelatedEmailList(WStepWork stepWork, boolean isAdmin,
			ArrayList<String> emailList) {
		if (stepWork.getCurrentStep().getRolesRelated() != null
				&& stepWork.getCurrentStep().getRolesRelated().size() > 0) {
			
			for (WStepRole wsr : stepWork.getCurrentStep().getRolesRelated()) {
				
				if (isAdmin != wsr.isAdmin()) {
					continue;
				}
				
				for (WUserDef user : this._getRoleUsers(wsr)) {
					
					if (this._checkForRepeatedEmailAdress(user, emailList)){

						emailList.add(user.getEmail());
							
					}					
				}
			}
		}
	}

	private void getUsersRelatedEmailList(WStepWork stepWork, boolean isAdmin,
			ArrayList<String> emailList) {
		if (stepWork.getCurrentStep().getUsersRelated() != null
				&& stepWork.getCurrentStep().getUsersRelated().size() > 0) {
			
			for (WStepUser wsu : stepWork.getCurrentStep().getUsersRelated()){
				
				if (isAdmin != wsu.isAdmin()) {
					continue;
				}
				
				if (this._checkForRepeatedEmailAdress(wsu.getUser(), emailList)){
				
					emailList.add(wsu.getUser().getEmail());
						
				}					
			}			
		}
	}
	
	// dml 20120307
	private boolean _checkForRepeatedEmailAdress(WUserDef user, ArrayList<String> replyEmailList){
		
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
	

	private void _setCurrentWorkitemToProcessed( WStepWork currentStep, Integer idResponse, 
			Date now, Integer currentUser) throws WStepWorkException {
		
		currentStep.setDecidedDate( now );
		currentStep.setPerformer(new WUserDef(currentUser));// nes 20120126
		currentStep.setLocked(false);
		currentStep.setLockedBy(null);
		currentStep.setLockedSince(null);

		if ( idResponse != null && idResponse != 0 ) { // nes 20121222
			currentStep.setResponse(idResponse.toString());
		} else {
			currentStep.setResponse("no responses list");
		}
		
		new WStepWorkBL().update(currentStep, currentUser); // actualiza el paso actual a "ejecutado" y lo desbloquea
		
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
	
	public List<StepWorkLight> finderStepWork(Integer processIdFilter, 
			Integer stepIdFilter, String stepTypeFilter, String referenceFilter, Integer idWorkFilter, 
			DateTime initialArrivingDateFilter, DateTime finalArrivingDateFilter, boolean estrictArrivingDateFilter,  		
			DateTime initialOpenedDateFilter, DateTime finalOpenedDateFilter, boolean estrictOpenedDateFilter, 		
			LocalDate initialDeadlineDateFilter, LocalDate finalDeadlineDateFilter, boolean estrictDeadlineDateFilter, 		
			DateTime initialDecidedDateFilter, DateTime finalDecidedDateFilter, boolean estrictDecidedDateFilter, 		
			String action, boolean onlyActiveWorkingProcessesFilter)
	throws WStepWorkException {

		return new WStepWorkDao().finderStepWork(processIdFilter, stepIdFilter, 
				stepTypeFilter, referenceFilter, idWorkFilter, initialArrivingDateFilter, 
				finalArrivingDateFilter, estrictArrivingDateFilter, 
				initialOpenedDateFilter, finalOpenedDateFilter, estrictOpenedDateFilter,
				initialDeadlineDateFilter, finalDeadlineDateFilter, estrictDeadlineDateFilter, 
				initialDecidedDateFilter, finalDecidedDateFilter, estrictDecidedDateFilter, 
				action, onlyActiveWorkingProcessesFilter);
		
	}
	
	/**
	 * NOTA: ESTE ES EL MÉTODO VIEJO, EL NUEVO CON DATETIME DE JODA ES EL DE ARRIBA, ESTE SOLO LO DEJAMOS PARA
	 * EL MANTENIMIENTO DE BEE_BPM MIENTRAS NO LO MIGRAMOS A JSF2
	 * 
	 * @author dmuleiro 20140212
	 * 
	 * @param initialDecidedDateFilter
	 * @param finalDecidedDateFilter
	 * @param estrictDecidedDateFilter
	 * @param action
	 * @param onlyActiveWorkingProcessesFilter
	 * @return
	 * @throws WStepWorkException
	 */
	@Deprecated
	public List<StepWorkLight> finderStepWorkVIEJO(Integer processIdFilter, 
			Integer stepIdFilter, String stepTypeFilter, String referenceFilter, Integer idWorkFilter, 
			Date initialArrivingDateFilter, Date finalArrivingDateFilter, boolean estrictArrivingDateFilter,  		
			Date initialOpenedDateFilter, Date finalOpenedDateFilter, boolean estrictOpenedDateFilter, 		
			Date initialDeadlineDateFilter, Date finalDeadlineDateFilter, boolean estrictDeadlineDateFilter, 		
			Date initialDecidedDateFilter, Date finalDecidedDateFilter, boolean estrictDecidedDateFilter, 		
			String action, boolean onlyActiveWorkingProcessesFilter)
	throws WStepWorkException {

		DateTime initialArrivingDateFilterDATETIME = 
				(initialArrivingDateFilter != null)?new DateTime(initialArrivingDateFilter.getTime()):null;
		DateTime finalArrivingDateFilterDATETIME = 
				(finalArrivingDateFilter != null)?new DateTime(finalArrivingDateFilter.getTime()):null;

		DateTime initialOpenedDateFilterDATETIME = 
				(initialOpenedDateFilter != null)?new DateTime(initialOpenedDateFilter.getTime()):null;
		DateTime finalOpenedDateFilterDATETIME = 
				(finalOpenedDateFilter != null)?new DateTime(finalOpenedDateFilter.getTime()):null;

		LocalDate initialDeadlineDateFilterDATETIME = 
				(initialDeadlineDateFilter != null)?new LocalDate(initialDeadlineDateFilter.getTime()):null;
		LocalDate finalDeadlineDateFilterDATETIME = 
				(finalDeadlineDateFilter != null)?new LocalDate(finalDeadlineDateFilter.getTime()):null;

		DateTime initialDecidedDateFilterDATETIME = 
				(initialDecidedDateFilter != null)?new DateTime(initialDecidedDateFilter.getTime()):null;
		DateTime finalDecidedDateFilterDATETIME = 
				(finalDecidedDateFilter != null)?new DateTime(finalDecidedDateFilter.getTime()):null;

		return this.finderStepWork(processIdFilter, stepIdFilter, 
				stepTypeFilter, referenceFilter, idWorkFilter, 
				initialArrivingDateFilterDATETIME, finalArrivingDateFilterDATETIME, estrictArrivingDateFilter, 
				initialOpenedDateFilterDATETIME, finalOpenedDateFilterDATETIME, estrictOpenedDateFilter,
				initialDeadlineDateFilterDATETIME, finalDeadlineDateFilterDATETIME, estrictDeadlineDateFilter, 
				initialDecidedDateFilterDATETIME, finalDecidedDateFilterDATETIME, estrictDecidedDateFilter, 
				action, onlyActiveWorkingProcessesFilter);
		
	}

	/**
	 * returns a list with all process def id which references (or use) the given stepDefId
	 *
	 * @author dml 20130507
	 * 
	 * @param stepDefId
	 * @param userId
	 * @return
	 * @throws WStepWorkException
	 */
	public List<Integer> getRelatedProcessDefIdList(
			Integer stepDefId, Integer processDefId, Integer userId) 
			throws WStepWorkException {

		return new WStepWorkDao().getRelatedProcessDefIdList(stepDefId,processDefId);
	
	}
	
	/**
	 * Returns true if there is process def different of given processDefId using this stepDefId
	 * If processDefId is null or zero, then returns true if there is works using this stepDefId
	 * Admin method
	 * 
	 * @param stepDefId
	 * @param processDefId
	 * @param currentUserId
	 * @return
	 * @throws WStepWorkException
	 * @throws WProcessDefException
	 */
	public boolean stepDefHasWorksRelated(
			Integer stepDefId, Integer processDefId, Integer currentUserId ) 
					throws WStepWorkException, WProcessDefException {

		if (getProcessListReferringThisStep(stepDefId,processDefId,currentUserId).size()>0) return true;
		else return false;
	}
	
	/**
	 * Returns a list of id for processes referring a given stepDefId.
	 * Si processDefId es distinto de nulo devolverá la lista de ids diferentes al 
	 * processDefId dado.
	 * Este es un método para administración para determinar si un determinado stepDef 
	 * tiene works que estén perteneciendo a otros procesos, de cara a limpiar la base
	 * de datos de procesos antiguos, obsoletos o similar.
	 * Admin method
	 * 
	 * @param stepDefId
	 * @param processDefId
	 * @param currentUserId
	 * 
	 * @return
	 * @throws WStepWorkException
	 * @throws WProcessDefException
	 */
	public List<Integer> getProcessListReferringThisStep(
			Integer stepDefId, Integer processDefId, Integer currentUserId) 
					throws WStepWorkException, WProcessDefException {

		if (stepDefId == null
				|| stepDefId.equals(0) ){
			String mess = "stepDefId cannot be null nor zero";
			logger.error(mess);
			throw new WProcessDefException(mess);
		}

		List<Integer> processIdList = this.getRelatedProcessDefIdList(stepDefId, processDefId, currentUserId);
		
		return processIdList;
	
	}

}
	