package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;
import static org.beeblos.bpm.core.util.Constants.PROCESS_STEP;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.AlreadyExistsRunningProcessException;
import org.beeblos.bpm.core.error.InjectorException;
import org.beeblos.bpm.core.error.WProcessDataFieldException;
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
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.enumerations.ProcessDataFieldStatus;
import org.beeblos.bpm.core.model.noper.WRuntimeSettings;
import org.beeblos.bpm.tm.exception.TableManagerException;
import org.joda.time.DateTime;

import com.sp.common.model.FileSP;
import com.sp.common.model.ManagedDataField;


/*
 * Clase de conexión o contacto entre BeeBPM y el mundo exterior.
 * Su primer cometido es permitir insertar o inyectar un nuevo workflow que quiere decir crear
 * una nueva instancia de un proceso existente ...
 * 
 * nes 20110124 - completé los mensajes de error ...
 * nes 20140707 - refactorizado: movido a 2_bee_bpm porque no tiene mucho sentido que BL de BeeBPM
 * esté fuera del core ...
 * 
 */
public class BeeBPMBL { 

	private static final Log logger = LogFactory.getLog(BeeBPMBL.class);

	private static final boolean AUTO_LOCK=true;// nes 20151021
	
	/**
	 * class properties will be used by mostly all methods ...
	 * 
	 * this is the selected processDef to build / inject stepWork
	 */
	WProcessDef selectedProcess;
	/**
	 * selected stepDef related with the injection of a wstepwork
	 */
	WStepDef selectedStepDef;


	public BeeBPMBL() {
		super();
	}


	/**
	 * starts a workflow process for given idObject/idObjectType and inserts it in the
	 * indicated idStep
	 * 
	 * si idStep viene vacio implica que hace un start por lo que hay que leer el WProcessDef y su beginStep
	 * 
	 * @param idProcess - required
	 * @param idStep - optional
	 * @param idObject - wProcessDef must indicate if this value is optional or required
	 * @param idObjectType  - wProcessDef must indicate if this value is optional or required
	 * @param objReference - reference or title for this new instance of a process
	 * @param objComments - optional
	 * @param managedData - ¿optional???? ELIMINADO NES 20140707
	 * @param userId - external user id that starts the process... Note: BeeBPM has WUserIdMapper to map external users with internal (bpm) users...
	 * @return
	 * @throws InjectorException
	 * @throws AlreadyExistsRunningProcessException
	 * @throws WStepWorkException
	 * @throws WProcessWorkException
	 * @throws TableManagerException
	 * @throws WStepWorkSequenceException
	 * @throws WProcessDataFieldException 
	 */
	public Integer injector(
			Integer idProcess, Integer idStep, 
			Integer idObject, String idObjectType,
			String objReference, String objComments, Integer userId ) 
					throws InjectorException, AlreadyExistsRunningProcessException, 
							WStepWorkException, WProcessWorkException, TableManagerException, 
							WStepWorkSequenceException, WProcessDataFieldException {
		
		Integer idStepWork=null;
		
		// load class property "selectedProcess" with the definition (WProcessDef) of process to launch...
		try {
			selectedProcess = _loadProcessDef(idProcess, userId);
		} catch (WProcessDefException e) {
			String mess="Can't inject new process for id process def:"
					+idProcess+" ....";
			logger.error(mess);
			throw new InjectorException(e);
		}
		
		// if idStep comes null then start at process definition starting point ...
		// at this time BeeBPM has restriction that have 1 and only 1 begin step ... (nes 20140707)
		if (idStep==null || idStep==0) {
			idStep=selectedProcess.getBeginStep().getId();
		}

		// controla que los elementos necesarios vengan cargados y si no sale por InyectorException
		_checkConsistencyBeforeStepLaunch(
				idProcess, idStep, idObject, idObjectType,
				objReference, objComments, userId); 
		
		// check for existing alive steps for this process / idObject
		_checkForAliveSteps(
							idProcess, idStep, idObject, idObjectType, 
							selectedProcess.isAllowedMultipleInstances(),
							objReference, objComments, userId); // controla que no exista ya 1 workflow para el proceso indicado y el objeto indicado
		
		// load step definition (WStepDef) in class property selectedStepDef
		try {
			selectedStepDef = _loadStepDef(idStep, userId);
		} catch (WStepDefException e) {
			String mess="Can't inject new process for id beginstep def:"
					+idStep+" ....";
			logger.error(mess);
			throw new InjectorException(e);
		}
		
		// create processWork obj
		WProcessWork processWork = _setProcessWork(
				idProcess,  idStep, idObject,  idObjectType, objReference,  objComments,  userId);
		
		// create stepWork obj
		WStepWork stepWork = _setStepWork(processWork,userId); // nes 20140707 quito managed data de aqui... , managedData);
		
//		_synchronizeManagedDataFields(processWork,stepWork);

		//idStepWork = new WStepWorkBL().add(_setStepWork(), usuarioLogueado) ;
		idStepWork = new WStepWorkBL()
							.start( processWork, stepWork, userId) ;

		return idStepWork;
		
	}
	
	/**
	 * 
	 * NOTA IMPORTANTE: EL UNICO SENTIDO QUE TIENE (QUE SE ME OCURRE AHORA) de hacer un inject de un 
	 * wStepWork sería que reinsertemos un wStepWork existente en un nuevo paso que salte la secuencia
	 * lógica de pasos.
	 * 
	 * Esto es: tengo 1 ProcessWork y éste tiene un StepWork dado y quiero reactivarlo y meterlo en algun 
	 * sitio. Con un método así podría hacerlo.
	 * 
	 * NO SE ME OCURRE otra utilidad para pasar un wStepWork a un metodo 'inject' ...
	 * 
	 * 
	 * Injects (start) a new process instance. This method is intended to launch starting
	 * with an InitEvent (begin step)
	 * 
	 * Mandatory: The begin step will be loaded with all the parameters required by process definition,
	 * like reference, related object, instructions to next step, custom data (if exists), etc.
	 * 
	 * stepWork MUST arrives without id (id-step-work must be null) and ManagedData stepWork id
	 * MUST be null too (and the start algorithm will fill this fields at launch time ...)
	 * 
	 * Launch at the begin step implies the table insert of the wStepWork for begin step and move to the
	 * next step(s)
	 * 
	 * Launch step has not be responses but may have logical rules to route the step ... (FALTA IMPLEMENTAR...)
	 * 
	 * nes 20150411
	 * 
	 * @param stepWork
	 * @param isAdminProcess
	 * @param typeOfProcess
	 * @param attachedDocuments - @author dmuleiro 20150415
	 * @param currentUserId
	 * 
	 * @throws InjectorException
	 * @throws AlreadyExistsRunningProcessException
	 * @throws WStepWorkException
	 * @throws WProcessWorkException
	 * @throws TableManagerException
	 * @throws WStepWorkSequenceException
	 * @throws WProcessDataFieldException
	 */
	public Integer injector( WStepWork stepWork, boolean isAdminProcess, String typeOfProcess,
			List<FileSP> attachedDocuments, Integer currentUserId ) 
					throws InjectorException, AlreadyExistsRunningProcessException, 
							WStepWorkException, WProcessWorkException, TableManagerException, 
							WStepWorkSequenceException, WProcessDataFieldException {
		logger.debug(">>> injecting new stepwork ... (begin step ...");
		
		/**
		 * return info: qty new routes created
		 */
		Integer qtyNewRoutes=null;
		
		/**
		 * Create wStepWork for begin step ...
		 * Managed data will be persisted below in this method ...
		 */
		
		Integer idStepWork = this.injector(
				stepWork.getwProcessWork().getProcessDef().getId(), 
				stepWork.getCurrentStep().getId(), 
				stepWork.getwProcessWork().getIdObject(), 
				stepWork.getwProcessWork().getIdObjectType(), 
				stepWork.getwProcessWork().getReference(), 
				stepWork.getwProcessWork().getComments(), 
				currentUserId);
		
		logger.debug(">>> wStepWork was created ...");
		
		WStepWorkBL wswBL = new WStepWorkBL();
		
		WStepWork justCreatedStepWork;
		
		try {

			justCreatedStepWork = wswBL.getStepWithLock(idStepWork, currentUserId);
			
			if (stepWork.getManagedData()!=null) {
				
				_updateStepWorkManagedData(stepWork, currentUserId, idStepWork,
						wswBL, justCreatedStepWork);
				
			}

			
			wswBL.lockStep(idStepWork, currentUserId, currentUserId, isAdminProcess);
			
			/**
			 * and now process step work ...
			 */
			qtyNewRoutes = processCreatedStep(idStepWork, stepWork.getRuntimeSettings(), wswBL,
					isAdminProcess, PROCESS_STEP, currentUserId);
			
			
			/**
			 * @author dmuleiro 20150415 - uploads the new attached documents (if there are any)
			 * 
			 * NOTA: THIS FUNCTIONALITY SHOULD BE DONE INSIDE "WStepWork.start" BUT WE
			 * CANNOT IMPLEMENT THIS OPTION BECAUSE THE START METHOD DOES NOT RECEIVE 
			 * A RuntimeSettings OBJECT WHEN IT MUST DO IT.
			 */
			if (justCreatedStepWork != null && justCreatedStepWork.getwProcessWork() != null){
				new WStepWorkBL().uploadFileInfoList(justCreatedStepWork.getwProcessWork().getId(), attachedDocuments, currentUserId);
			}

		} catch (WStepLockedByAnotherUserException e) {
			String mess="WStepLockedByAnotherUserException - Can't update new instance just injected: "
					+idStepWork+" ...."+ " "
					+e.getMessage()+"  "+(e.getCause()!=null?e.getCause():" ");
			logger.error(mess);
			throw new InjectorException(e);
		

		} catch (WStepNotLockedException e) {
			String mess="WStepNotLockedException - Can't update new instance just injected: "
					+idStepWork+" ...."+ " "
					+e.getMessage()+"  "+(e.getCause()!=null?e.getCause():" ");
			logger.error(mess);
			throw new InjectorException(e);

		}
		
		return qtyNewRoutes;
		
	}


	/**
	 * refactorizado nes 20151018 - se utiliza en el injector con wStepWork para definir el 
	 * managed data del stepWork (en caso que tenga claro está ... )
	 * 
	 * @param stepWork
	 * @param currentUserId
	 * @param idStepWork
	 * @param wswBL
	 * @param justCreatedStepWork
	 * @throws WStepWorkException
	 */
	private void _updateStepWorkManagedData(WStepWork stepWork,
			Integer currentUserId, Integer idStepWork, WStepWorkBL wswBL,
			WStepWork justCreatedStepWork) throws WStepWorkException {
		/**
		 * update step work id and process work id at ManagedData and persist...
		 */
		stepWork.getManagedData().setCurrentStepWorkId(idStepWork);
		stepWork.getManagedData().setCurrentWorkId(justCreatedStepWork.getwProcessWork().getId());
		
		for (ManagedDataField mdf: stepWork.getManagedData().getDataField()) {
			mdf.setCurrentStepWorkId(idStepWork);
			mdf.setCurrentWorkId(justCreatedStepWork.getwProcessWork().getId());
		}
		
		logger.debug(">>> MD was updated with ids ...");
		
		justCreatedStepWork.setManagedData(stepWork.getManagedData());
		
		// updates managed data of created wstepWork...
		wswBL.update(justCreatedStepWork, currentUserId);

		logger.debug(">>> MD was persisted ...");
	}


	/**
	 * Process just created begin step. 
	 * As a begin step, there is not responses nor runtimeSettings
	 * nes 20150411 
	 * 
	 * @param idStepWork
	 * @param runtimeSettings
	 * @param wswBL
	 * @param isAdminProcess
	 * @param typeOfProcess
	 * @param currentUserId
	 * @return
	 * @throws WStepWorkException
	 * @throws WStepWorkSequenceException
	 * @throws WProcessWorkException
	 */
	private Integer processCreatedStep(Integer idStepWork, WRuntimeSettings runtimeSettings,
			WStepWorkBL wswBL, boolean isAdminProcess,
			String typeOfProcess, Integer currentUserId) throws WStepWorkException,
			WStepWorkSequenceException, WProcessWorkException {
		logger.debug(">>> move from begin step to the next instance ...");
		try {
			Integer qtyCreatedRoutes = 
					wswBL.processStep(idStepWork, null, runtimeSettings, currentUserId, isAdminProcess, typeOfProcess, AUTO_LOCK);
			return qtyCreatedRoutes;
		} catch (WProcessDefException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WStepDefException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WStepSequenceDefException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WStepLockedByAnotherUserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WStepNotLockedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WUserDefException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WStepAlreadyProcessedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info(">>> No new routes were created...");
		
		return null;// null will be an error because if no new routes was created, the instance of the process will be suspended...
	}
	

	/**
	 * Creates an empty new wStepWork and returns it
	 * nes 20150411
	 * 
	 * @param idProcess
	 * @param idBeginStep
	 * @param idObject
	 * @param idObjectType
	 * @param objReference
	 * @param objComments
	 * @param userId
	 * @return
	 * @throws InjectorException
	 * @throws AlreadyExistsRunningProcessException
	 * @throws WStepWorkException
	 * @throws WProcessWorkException
	 * @throws TableManagerException
	 * @throws WStepWorkSequenceException
	 * @throws WProcessDataFieldException
	 */
	public WStepWork createWStepWork(
			Integer idProcess, Integer idBeginStep, 
			Integer idObject, String idObjectType,
			String objReference, String objComments, Integer userId ) 
					throws 	WStepWorkException, WProcessWorkException, TableManagerException, 
							WStepWorkSequenceException, WProcessDataFieldException {
	
		
		
		// load class property "selectedProcess" with the definition (WProcessDef) of process to launch...
		try {
			selectedProcess = _loadProcessDef(idProcess, userId);
		} catch (WProcessDefException e) {
			String mess="Can't create a wStepWork for id process:"
					+idProcess+" ....";
			logger.error(mess);
			throw new WStepWorkException(e);
		}
		
		// if idStep comes null then start at process definition starting point ...
		// at this time BeeBPM has restriction that have 1 and only 1 begin step ... (nes 20140707)
		// nes 20150410 - analizar esto porque ahora vamos a soportar 'n' begin step por lo que este puntero
		// quedaría obsoleto ...
		if (idBeginStep==null || idBeginStep==0) {
			idBeginStep=selectedProcess.getBeginStep().getId();
		}

		// controla que los elementos necesarios vengan cargados y si no sale por InyectorException
		_checkConsistencyBeforeStepLaunch(
				idProcess, idBeginStep, idObject, idObjectType,
				objReference, objComments, userId);
		
		
		// load step definition (WStepDef) in class property selectedStepDef
		try {
			selectedStepDef = _loadStepDef(idBeginStep, userId);
		} catch (WStepDefException e) {
			String mess="Can't create a wStepWork for begin id step:"
					+idBeginStep+" ....";
			logger.error(mess);
			throw new WStepWorkException(e);
		}
		
		// create processWork obj
		WProcessWork processWork = _setProcessWork(
				idProcess,  idBeginStep, idObject,  idObjectType, objReference,  objComments,  userId);
		
		// create stepWork obj
		WStepWork stepWork = _setStepWork(processWork,userId); // nes 20140707 quito managed data de aqui... , managedData);

		// set managed data object for stepWork
		_setManagedData(userId, stepWork);
		
		return stepWork;
	}

	
	/**
	 * Creates a wStepWork object for given process and beginStep.
	 * it avoids to reload the objects from de database to speed it!
	 * nes 20150411
	 * 
	 * @param process
	 * @param beginStep
	 * @param idObject
	 * @param idObjectType
	 * @param objReference
	 * @param objComments
	 * @param userId
	 * @return
	 * @throws WStepWorkException
	 * @throws WProcessWorkException
	 * @throws TableManagerException
	 * @throws WStepWorkSequenceException
	 * @throws WProcessDataFieldException
	 */
	public WStepWork createWStepWork(
			WProcessDef process, WStepDef beginStep, 
			Integer idObject, String idObjectType,
			String objReference, String objComments, Integer userId ) 
					throws 	WStepWorkException {
		logger.debug(">>> creating a new createWStepWork... ");
		
		if (process==null || process.getId()==null || process.getId()==0){
			String mess="given process is null! can't create a new WStepWork...";
			logger.error(mess);
			throw new WStepWorkException(mess);
		}
		
		if (beginStep==null || beginStep.getId()==null || beginStep.getId()==0){
			String mess="given step is null! can't create a new WStepWork...";
			logger.error(mess);
			throw new WStepWorkException(mess);
		}		
		
		/**
		 * Assign given process and step to class properties...
		 */
		selectedProcess = process;
		selectedStepDef = beginStep;

		// create processWork obj
		WProcessWork processWork = _setProcessWork(
				process.getId(),  beginStep.getId(), idObject,  idObjectType, objReference,  objComments,  userId);
		
		// create stepWork obj
		WStepWork stepWork = _setStepWork(processWork,userId); // nes 20140707 quito managed data de aqui... , managedData);
		
		// set managed data object for stepWork
		_setManagedData(userId, stepWork);
	
		return stepWork;
	}

	
	/**
	 * load processDef to launch
	 * 
	 * @param idProcess
	 * @param userId
	 * @return
	 * @throws WProcessDefException
	 */
	private WProcessDef _loadProcessDef(Integer idProcess, Integer userId) 
			throws WProcessDefException {
		try {
			return new WProcessDefBL().getWProcessDefByPK(idProcess, userId);
		} catch (WProcessDefException e) {
			String mess="WProcessDefException: can't load process def for id:"
							+idProcess+" "+e.getMessage()+" "+(e.getCause()!=null?e.getCause():" ");
			logger.error(mess);
			throw new WProcessDefException(mess);
		} catch (WStepSequenceDefException e) {
			String mess="WProcessDefException: can't load process def for id:"
							+idProcess+" "+e.getMessage()+" "+(e.getCause()!=null?e.getCause():" ");
			logger.error(mess);
			throw new WProcessDefException(mess);
		}
	}
	

	private void _checkConsistencyBeforeStepLaunch(
			Integer idProcess, Integer idStep, 
			Integer idObject, String idObjectType,
			String objReference, String objComments, Integer userId) 
					throws WStepWorkException {
		
		if (idProcess==null || idProcess==0) {
			
			String mess = "You must select a process ....";
			mess +=" Id:"+idObject+" ref:"+objReference; // NOTA REVISAR ESTOS objIdUsuario PORQUE LOS ESTABA USANDO MAL ... NES 20111216
			logger.info("inyectar:"+mess);
			throw new WStepWorkException(mess);
			
		}
		
		if (idStep==null || idStep==0) {
			
			String mensaje = "You must select a step into process will be launched....";
			mensaje +=" Id:"+idObject+" ref:"+objReference;
			logger.info("inyectar:"+mensaje);
			throw new WStepWorkException(mensaje);
		}
		// TODO NESTOR ARREGLAR ESTO LUEGO CUANDO TENGAMOS EL CONTROL DE OBJETOS EN EL WPROCESS-DEF	
//		if (idObject==null || idObject==0 ||
//				idObjectType==null || "".equals(idObjectType)) {
//			
//			String mensaje = "El elemento a insertar en el workflow no está definido ( idObject, idObjectType ) ....\n";
//			mensaje +=" Id:"+idObject+" ref:"+objReference;
//			logger.info("inyectar:"+mensaje);
//			throw new InyectorException(mensaje);
//		}
	}
	
	/**
	 * check for alive steps for this process before launch 
	 * 
	 * @param idProcess
	 * @param idStep
	 * @param idObject
	 * @param idObjectType
	 * @param allowsMultipleInstances
	 * @param objReference
	 * @param objComments
	 * @param userId
	 * @throws AlreadyExistsRunningProcessException
	 * @throws WStepWorkException
	 */
	private void _checkForAliveSteps(
			Integer idProcess, Integer idStep, 
			Integer idObject, String idObjectType, boolean allowsMultipleInstances,
			String objReference, String objComments, Integer userId) 
	throws AlreadyExistsRunningProcessException, WStepWorkException {
		
		// si no hay referencias a objetos "base" no es necesario controlar si hay pasos vivos 
		// porque no hay contra que hacerlo ...
		if (idObject==null || idObject==0 ||
				idObjectType==null || "".equals(idObjectType)) {
			return;
		}
		
		if (allowsMultipleInstances) return; // si permite multiples instancias no seria necesario controlar ...
		
		try {
			if ( new WStepWorkBL().existsActiveProcess(idProcess, idObject, idObjectType, userId)) {
				
				String mensaje = "Ya existe una instancia del wofkflow en ejecución para este elemento ....\n";
				logger.info("inyectar: "+mensaje);
				throw new AlreadyExistsRunningProcessException(mensaje);
			}
		} catch (WStepWorkException e) {
			String mensaje = "El sistema arrojó un error al intentar verificar si existen procesos activos para este objeto ....\n";
			logger.error(mensaje);
			throw new WStepWorkException(mensaje);
		}
		
	}
	
	/**
	 * Loads indicated WStepDef to prepare wStepWork to launch ...
	 * 
	 * @param idStep
	 * @param userId
	 * @return
	 * @throws WStepDefException
	 */
	private WStepDef _loadStepDef(
			Integer idStep,	Integer userId) throws WStepDefException {
		
		try {

			return new WStepDefBL()
							.getWStepDefByPK(
									idStep, selectedProcess.getProcess().getId(), userId);// nes 20130808 por agregado de filtro para step-data-field

		} catch (WStepDefException e) {

			String mensaje = "No se puede cargar el paso indicado id:"+(idStep!=null?idStep:"null");
			mensaje +=" error:"+e.getMessage()+" "+(e.getCause()!=null?e.getCause():" ");
			logger.error(mensaje);
			throw new WStepDefException( mensaje );
		}
		
	}
	
	private WProcessWork _setProcessWork(
			Integer idProcess, Integer idStep, 
			Integer idObject, String idObjectType,
			String objReference, String objComments, Integer userId) {
		
		WProcessWork newWorkObject = new WProcessWork();
		
		newWorkObject.setProcessDef(selectedProcess.getAsProcessDefThin());
//		newWorkObject.setVersion(version); (obsoleto y ano se usa el idProcess ya implica la version ...)
		
//		pasoAInyectar.setPreviousStep(null);
//		pasoAInyectar.setCurrentStep(selectedStepDef);
		
		newWorkObject.setIdObject(idObject);
		newWorkObject.setIdObjectType(idObjectType);
		
		newWorkObject.setReference(objReference);
		newWorkObject.setComments(objComments);
		
		newWorkObject.setStartingTime( new DateTime() );
		
		return newWorkObject;
		
	}
	
	private WStepWork _setStepWork(WProcessWork processWork, Integer userId) { // NES 20140707 ELIMINADO , ManagedData md QUE NO DEBE IR AQUI PIENSO...
		
		WStepWork stepToBeInjected = new WStepWork();
		
		if (processWork!=null) {
			stepToBeInjected.setwProcessWork(processWork);
		}
		if (stepToBeInjected.getwProcessWork()!=null) {
			stepToBeInjected.getwProcessWork().setProcessDef( selectedProcess.getAsProcessDefThin() );
		}

		//		pasoAInyectar.setVersion(version);
		
		stepToBeInjected.setPreviousStep(null);
		stepToBeInjected.setCurrentStep(selectedStepDef);
		
		// esta valor se carga en WProcessWorkBL.start si corresponde
		// al lanzamiento del workflow
		stepToBeInjected.setwProcessWork(processWork); // nes 20120126

//		pasoAInyectar.setIdObject(idObject);
//		pasoAInyectar.setIdObjectType(idObjectType);
		
		// nes 20110106 - asocio los identificadores conocidos por el usuario al paso ...
//		pasoAInyectar.setReference(objReference);
//		pasoAInyectar.setComments(objComments);
		
		stepToBeInjected.setArrivingDate(new DateTime());
		

		stepToBeInjected.setOpenedDate(null);
		stepToBeInjected.setOpenerUser(null);

		stepToBeInjected.setDecidedDate(null);
		stepToBeInjected.setPerformer(null);

		// TODO: AJUSTAR CUANDO DEJEMOS MODIFICAR ESTO EN EL FORM ...
		stepToBeInjected.setTimeUnit(selectedStepDef.getTimeUnit());

		stepToBeInjected.setAssignedTime(selectedStepDef.getAssignedTime());
		stepToBeInjected.setDeadlineDate(selectedStepDef.getDeadlineDate());
		stepToBeInjected.setDeadlineTime(selectedStepDef.getDeadlineTime());
		stepToBeInjected.setReminderTimeUnit(selectedStepDef.getReminderTimeUnit());
		stepToBeInjected.setReminderTime(selectedStepDef.getReminderTime()); // en unidades de tiempo indicadas en reminderTimeUnit
		
		stepToBeInjected.setAdminProcess(false); // esto va en true si es un administrador quien decide el paso ( en vez del usuario o grupo asignado )
		
		stepToBeInjected.setLocked(false);
		stepToBeInjected.setLockedBy(null);
		stepToBeInjected.setLockedSince(null);
		
		// TODO: ver como se debe asignar este paso ...
		stepToBeInjected.setAssignedTo(null); 

		stepToBeInjected.setInsertUser(new WUserDef(userId)); // nes 20121222
		stepToBeInjected.setModDate(DEFAULT_MOD_DATE_TIME); // 1/1/1970 por default
		stepToBeInjected.setModUser(null);

		return stepToBeInjected;
		
	}
	
	/**
	 * set managed data to a given wStepWork
	 * for a new wStepWork (not persisted yet...) the ManagedData has't the wStepWork id and the
	 * processWorkId, then the persist process will consider this situation ...
	 * Note: this methods works with class properties like selectedStepDef and selectedProcess
	 * nes 20140411
	 * 
	 * @param userId
	 * @param stepWork
	 */
	private void _setManagedData(Integer userId, WStepWork stepWork) {
		/**
		 * ManagedData definition ...
		 * 
		 */
		if (selectedStepDef.getDataFieldDef()!=null
			&& selectedStepDef.getDataFieldDef().size() > 0	) {
			try {
				stepWork.setManagedData(
						new WStepWorkBL().buildStepWorkManagedDataObject(
								stepWork, userId)
						);
			} catch (WStepWorkException e) {
				String mess="can't set managed data for new wStepWork object ... "
						+e.getMessage()+" "+(e.getCause()!=null?e.getCause():"null");
				logger.error(mess+" ... continues without managed data....");
			}
		}
	}
	
	public void _synchronizeManagedDataFields(WProcessWork processWork, WStepWork stepWork) 
			throws WProcessDataFieldException {
		
		// 1st check if exists managed data fields
		Integer qtyPdf=null;
		try {
			qtyPdf = 
				new WProcessDataFieldBL()
						.hasProcessDataFields(processWork.getProcessHeadId(), ProcessDataFieldStatus.ACTIVE, null);
		} catch (WProcessDataFieldException e) {
			// TODO Auto-generated catch block
			String mess = "WProcessDataFieldException: Error checking for 'active managed data fields' belonging to this process...";
			throw (e);
		}
		
		if (qtyPdf==null || qtyPdf==0) return; // no process data field exists > then nothing to do...
		
		// 2nd if exists:
			// 2a: checks if any mdf has synchro configurations at create work process level and if they are then synchronize
			// 2b: checks if any mdf has synchro configurations at create step work level and if they are then synchronize
		//  
		
	}
}
