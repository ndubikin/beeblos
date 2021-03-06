package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;
import static com.sp.common.util.ConstantsCommon.VACIO;
import static org.beeblos.bpm.core.util.Constants.PROCESS_STEP;

import java.util.ArrayList;
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
import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.enumerations.EventType;
import org.beeblos.bpm.core.model.enumerations.ProcessDataFieldStatus;
import org.beeblos.bpm.core.model.enumerations.StartProcessResult;
import org.beeblos.bpm.core.model.noper.WRuntimeSettings;
import org.beeblos.bpm.core.model.noper.WStartProcessResult;
import org.beeblos.bpm.tm.exception.TableManagerException;
import org.joda.time.DateTime;

import com.sp.common.model.FileSP;
import com.sp.common.model.ManagedDataField;
import com.sp.common.util.StringPair;


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
	 * default injector...
	 * 
	 * @param idProcess
	 * @param idStep
	 * @param idObject
	 * @param idObjectType
	 * @param objReference
	 * @param objComments
	 * @param managedData - 20151109/20160521 List<StringPair> - se debe incluir la lista de campos (ManagedDataField.columnName/value) - la consistencia/coherencia queda 100% en manos del programador
	 * @param attachedDocuments
	 * @param isAdminProcess - indica que el proceso está siendo lanzado por un administrador en su calidad de administrador...
	 * @param currentUserId
	 * @return
	 * @throws InjectorException
	 * @throws AlreadyExistsRunningProcessException
	 * @throws WStepWorkException
	 * @throws WProcessWorkException
	 * @throws TableManagerException
	 * @throws WStepWorkSequenceException
	 * @throws WProcessDataFieldException
	 */
	public WStartProcessResult injector(
			Integer idProcess, Integer idStep, 
			Integer idObject, String idObjectType,
			String objReference, String objComments,
			List<StringPair> managedData, // nes 20151109 - nes 20160521: paso a StringPair para simplificar. El control de consistencia queda en manos del programador...
			List<FileSP> attachedDocuments, // nes 20151026 - lista de documentos a adjuntar al proceso lanzado...
			boolean isAdminProcess, // nes 20151026
			Integer currentUserId ) 
					throws InjectorException, AlreadyExistsRunningProcessException, 
							WStepWorkException, WProcessWorkException, TableManagerException, 
							WStepWorkSequenceException, WProcessDataFieldException {
		logger.debug(">>> WStartProcessResult injector ... userId:"+(currentUserId!=null?currentUserId:"null"));
		
		Integer idStepWork=null;
		WStartProcessResult startProcessResult = new WStartProcessResult(VACIO);
		startProcessResult.setStartProcessResult(StartProcessResult.FAIL); // nes 20151026
		
		idStepWork =
				this._injector(
						idProcess, 
						idStep,
						idObject,
						idObjectType,
						objReference,
						objComments,
						managedData,
						startProcessResult, // nes 20151026
						isAdminProcess, // nes 20160521
						currentUserId);

		// if just created stepWork belongs an INITEV with no engine required, the BeeBPM must
		// process de BeginStep ....
		_processInitEventAndSetWStartProcessResult(idStepWork, isAdminProcess,
				attachedDocuments, 1, startProcessResult, currentUserId);
		
		startProcessResult.setStartProcessResult(StartProcessResult.SUCCESS); // nes 20151026
		
		return startProcessResult;
		
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
	 * @param managedDataStrLst // nes 20160521
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
	public WStartProcessResult injector( 
				WStepWork stepWork, boolean isAdminProcess, //String typeOfProcess, nes 20151026 - no tiene sentido, el inyector siempre va hacia adelante, no pude ir hacia atrás
				List<StringPair> managedDataStrLst,// nes 20160521
				List<FileSP> attachedDocuments, Integer currentUserId ) 
	throws InjectorException, AlreadyExistsRunningProcessException, 
							WStepWorkException, WProcessWorkException, TableManagerException, 
							WStepWorkSequenceException, WProcessDataFieldException {
		logger.debug(">>> injecting new stepwork ... userId:"+(currentUserId!=null?currentUserId:"null"));
		
		/**
		 * return info: qty new routes created
		 */
		Integer qtyNewRoutes=null;
		WStartProcessResult startProcessResult = new WStartProcessResult(VACIO); // nes 20151108
		
		if (managedDataStrLst!=null && managedDataStrLst.size()<1){
			managedDataStrLst=null;
		}
		
		/**
		 * Create wStepWork for begin step ...
		 * Managed data will be persisted below in this method ...
		 */
		Integer idStepWork =
				this._injector(
						stepWork.getwProcessWork().getProcessDef().getId(), 
						stepWork.getCurrentStep().getId(), 
						stepWork.getwProcessWork().getIdObject(), 
						stepWork.getwProcessWork().getIdObjectType(), 
						stepWork.getwProcessWork().getReference(), 
						stepWork.getwProcessWork().getComments(),
						managedDataStrLst, // nes 20160521 
						startProcessResult, // nes 20151026
						isAdminProcess, // nes 20160521
						currentUserId) ;
		
		logger.debug(">>> wStepWork was created ...");
		
		// if just created stepWork belongs an INITEV with no engine required, the BeeBPM must
		// process de BeginStep ....
		_processInitEventAndSetWStartProcessResult(idStepWork, isAdminProcess,
				attachedDocuments, qtyNewRoutes, startProcessResult, currentUserId);
		
		return startProcessResult;
		
	}

	/**
	 * Procesa el stepWork recién creado. Se utiliza desde el inyector: en caso que cree un "beginStep" (INITEV) que no 
	 * sea procesado por el engine (si crea un MessageBegin debe ser procesado por el engine asi que no pasaría por aquí...)
	 * sea creado e inmediatamente necesita ser procesado para llevar el proceso a un nuevo paso que pueda ser atendido por 
	 * un usuario o por el engine... (timer, message, etc)
	 * 
	 * nes 20151026 - refactorizado desde el injector pues todos lo deben utilizar (todos
	 * los inyectores ...)
	 * 
	 * @param idStepWork
	 * @param isAdminProcess
	 * @param attachedDocuments
	 * @param currentUserId
	 * @param qtyNewRoutes
	 * @param startProcessResult - idStepWork, etc...
	 * @return
	 * @throws WStepWorkException
	 * @throws WStepWorkSequenceException
	 * @throws WProcessWorkException
	 * @throws InjectorException
	 */
	private void _processInitEventAndSetWStartProcessResult(Integer idStepWork,
			boolean isAdminProcess, List<FileSP> attachedDocuments,
			Integer qtyNewRoutes, WStartProcessResult startProcessResult, Integer currentUserId ) 
		throws WStepWorkException, WStepWorkSequenceException, WProcessWorkException, InjectorException {
		WStepWork justCreatedStepWork;
		
		try {
			
			// recupero el stepWork creado...
			WStepWorkBL wswBL = new WStepWorkBL();
			justCreatedStepWork = wswBL.getStep(idStepWork, currentUserId); // nes 20151028 - va sin lock porque si corresponde luego se bloquea...
			
			// if the step is NOT an INITEV or if it's but Engine is required, then return as is...
			// completing given WStartProcessResult 
			// nes 20151026
			if ( !justCreatedStepWork.getCurrentStep().getStepTypeDef().getEventType().equals(EventType.INITEV) 
					&& !justCreatedStepWork.getCurrentStep().getStepTypeDef().isEngineReq() ) {
				startProcessResult.setQtyStepWorkCreated(1); // 1 route created at start ... 
				if (startProcessResult.getStepWorkIdList()==null) {
					startProcessResult.setStepWorkIdList(new ArrayList<Integer>());
				}
				startProcessResult.getStepWorkIdList().add(idStepWork); // created wStepWork id
				return;
			}
			
			// if it's an INITEV and not engine process it, then we will continue with process the begin step...
			
			if (justCreatedStepWork.getManagedData()!=null) {
				
				_updateStepWorkManagedData(justCreatedStepWork, currentUserId, idStepWork,
						wswBL, justCreatedStepWork);
				
			}

			
			wswBL.lockStep(idStepWork, currentUserId, currentUserId, isAdminProcess);
			
			/**
			 * and now process step work ...
			 */
			qtyNewRoutes = processCreatedStep(idStepWork, justCreatedStepWork.getRuntimeSettings(), wswBL,
					isAdminProcess, PROCESS_STEP, startProcessResult, currentUserId);
			
			
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
		
 		} catch (Exception e) {
			String mess="Exception - Can't update new instance just injected: "
					+idStepWork+" ...."+ " "
					+e.getMessage()+"  "+(e.getCause()!=null?e.getCause():" ")+". "
					+e.getClass();
			logger.error(mess);
			throw new InjectorException(e);
			
		}
		//return qtyNewRoutes; nes 20151026
	}
	
	/**
	 * starts a workflow process for given idObject/idObjectType and inserts it in the
	 * indicated idStep
	 * 
	 * si idStep viene vacio implica que hace un start por lo que hay que leer el WProcessDef y su beginStep
	 * 
	 * nes 20151026 - paso a privado (_injector) y creo llamada publica que asegure ejecutar el INIEV 
	 * 
	 * @param idProcess - required
	 * @param idStep - optional
	 * @param idObject - wProcessDef must indicate if this value is optional or required
	 * @param idObjectType  - wProcessDef must indicate if this value is optional or required
	 * @param objReference - reference or title for this new instance of a process
	 * @param objComments - optional
	 * @param managedData - optional - nes 20151109/20160521 - List<StringPair>
	 * @param startProcessResult - para devolver los resultados del inject... // nes 20151026
	 * @param isAdminProcess - indica que lo está inyectando un administrador en su rol de administrador
	 * @param userId - external user id who starts the process... Note: BeeBPM has WUserIdMapper to map external users with internal (bpm) users...
	 * @return
	 * @throws InjectorException
	 * @throws AlreadyExistsRunningProcessException
	 * @throws WStepWorkException
	 * @throws WProcessWorkException
	 * @throws TableManagerException
	 * @throws WStepWorkSequenceException
	 * @throws WProcessDataFieldException 
	 */
	private Integer _injector(
			Integer idProcess, Integer idStep, 
			Integer idObject, String idObjectType,
			String objReference, String objComments,  
			List<StringPair> managedData, // nes 20151109 - nes 20160521: paso a StringPair para simplificar. El control de consistencia queda en manos del programador...
			WStartProcessResult startProcessResult,
			boolean isAdminProcess, // nes 20160521
			Integer userId ) 
					throws InjectorException, AlreadyExistsRunningProcessException, 
							WStepWorkException, WProcessWorkException, TableManagerException, 
							WStepWorkSequenceException, WProcessDataFieldException {
		
		Integer idStepWork=null;
		startProcessResult.setStartProcessResult(StartProcessResult.FAIL); // nes 20151026
		
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
			if (selectedProcess!=null && selectedProcess.getBeginStep()!=null && selectedProcess.getBeginStep().getId()!=null) { // nes 20151026
				idStep=selectedProcess.getBeginStep().getId();
			}
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
		WStepWork stepWork = _setStepWork(processWork, isAdminProcess, userId); // nes 20160521 -managedData va al start para después del insert
		
//		_synchronizeManagedDataFields(processWork,stepWork);

		//idStepWork = new WStepWorkBL().add(_setStepWork(), usuarioLogueado) ;
		idStepWork = new WStepWorkBL()
							.start( processWork, stepWork, managedData, isAdminProcess, userId) ;
		
		startProcessResult.setStartProcessResult(StartProcessResult.SUCCESS); // nes 20151026
		
		return idStepWork;
		
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
	 * @param processingDirection -> go ahead or turn back...
	 * @pram startProcessResult - object to retunrn processing results...  // nes 20151026
	 * @param currentUserId
	 * @return
	 * @throws WStepWorkException
	 * @throws WStepWorkSequenceException
	 * @throws WProcessWorkException
	 */
	private Integer processCreatedStep(Integer idStepWork, WRuntimeSettings runtimeSettings,
			WStepWorkBL wswBL, boolean isAdminProcess, String processingDirection, 
			 WStartProcessResult startProcessResult, Integer currentUserId ) throws WStepWorkException,
			WStepWorkSequenceException, WProcessWorkException {
		logger.debug(">>> move from begin step to the next instance ...");
		try {
			Integer qtyCreatedRoutes = 
					wswBL.processStep(idStepWork, null, runtimeSettings, isAdminProcess, processingDirection, 
							AUTO_LOCK, startProcessResult, // nes 20151026 
							currentUserId);
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
	 * utility method for clean wStepWork creation
	 * 
	 * nes 20150411
	 * 
	 * @param idProcess
	 * @param idBeginStep
	 * @param idObject
	 * @param idObjectType
	 * @param objReference
	 * @param objComments
	 * @param isAdminProcess - nes 20160521
	 * @param currentUserId
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
			String objReference, String objComments, 
			boolean isAdminProcess, Integer currentUserId ) 
					throws 	WStepWorkException, WProcessWorkException, TableManagerException, 
							WStepWorkSequenceException, WProcessDataFieldException {
		logger.debug(">>> createWStepWork... userId:"+(currentUserId!=null?currentUserId:"null"));
		
		// load class property "selectedProcess" with the definition (WProcessDef) of process to launch...
		try {
			selectedProcess = _loadProcessDef(idProcess, currentUserId);
		} catch (WProcessDefException e) {
			String mess="Can't create a wStepWork for id process:"
					+idProcess+" ....";
			logger.error(mess);
			throw new WStepWorkException(e);
		}
		
		// if idStep comes null then start at process definition starting point ...
		// at this time BeeBPM has restriction that have 1 and only 1 begin step ... (nes 20140707)
		// nes 20150410 - analizar esto porque ahora vamos a soportar 'n' begin step por lo que este puntero
		// quedaría obsoleto ... aunque podríamos dejarlo como "default-begin-step" de forma que si no se indica
		// nada, arranque por este idBeginStep ...  nota el 20160521 by nes
		if (idBeginStep==null || idBeginStep==0) {
			idBeginStep=selectedProcess.getBeginStep().getId();
		}

		// controla que los elementos necesarios vengan cargados y si no sale por InyectorException
		_checkConsistencyBeforeStepLaunch(
				idProcess, idBeginStep, idObject, idObjectType,
				objReference, objComments, currentUserId);
		
		
		// load step definition (WStepDef) in class property selectedStepDef
		try {
			selectedStepDef = _loadStepDef(idBeginStep, currentUserId);
		} catch (WStepDefException e) {
			String mess="Can't create a wStepWork for begin id step:"
					+idBeginStep+" ....";
			logger.error(mess);
			throw new WStepWorkException(e);
		}
		
		// create processWork obj
		WProcessWork processWork = _setProcessWork(
				idProcess,  idBeginStep, idObject,  idObjectType, objReference,  objComments,  currentUserId);
		
		// create stepWork obj
		WStepWork stepWork = _setStepWork(processWork,isAdminProcess, currentUserId); // nes 20140707 quito managed data de aqui... , managedData);

		// nes 20160521 - esto hay que quitarlo porque nunca lo utilizamos en realidad... lo dejo por un tiempo hasta que limpiemos...
//		// set managed data object for stepWork
//		_setManagedData(currentUserId, stepWork);
		
		return stepWork;
	}

	
	/**
	 * Utility method for clean wStepWork creation
	 * creates the wStepWork from given pre-loaded process and beginStep
	 * nes 20150411
	 * 
	 * @param process
	 * @param beginStep
	 * @param idObject
	 * @param idObjectType
	 * @param objReference
	 * @param objComments
	 * @param isAdminProcess - nes 20160521
	 * @param currentUserId
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
			String objReference, String objComments,
			boolean isAdminProcess,	Integer currentUserId ) 
					throws 	WStepWorkException {
		logger.debug(">>> createWStepWork 2 ... userId:"+(currentUserId!=null?currentUserId:"null"));
		
		ManagedData _NULL_MANAGED_DATA = null; // nes 20151109
		
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
				process.getId(),  beginStep.getId(), idObject,  idObjectType, objReference,  objComments,  currentUserId);
		
		// create stepWork obj
		WStepWork stepWork = _setStepWork(processWork, isAdminProcess, currentUserId); // nes 20160521 - quitado managedData y agregado isAdminProcess
	
		// nes 20160521 - managedData ahora lo cargamos en el start después del insert del wStepWork...
//		// set managed data object for stepWork
//		_setManagedData(currentUserId, stepWork);
	
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
	
	/**
	 * crea un wStepWork...
	 * 
	 * nes 20151109 - agregué el managedData...
	 * 
	 * @param processWork
	 * ---- removed nes 20160521 ---- @param managedData - nes 20151109 - set managedData if it's not null...
	 * @param isAdminProcess - nes 20160521
	 * @param userId
	 * @return
	 */
	private WStepWork _setStepWork(WProcessWork processWork, /*ManagedData managedData, nes 20160521*/ 
			boolean isAdminProcess, Integer userId) { 
		
		
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
		
		// nes 20160521 - el managedData no lo estabamos usando en realidad aqui por lo que
		// lo quito y lo paso al Start como un List<StringPair> para que luego del insert lo carguemos
		// según lo que venga desde el inyector...
//		// nes 20151109 si el managedData es diferente de null lo cargo
//		if (managedData!=null) {
//			stepToBeInjected.setManagedData(managedData); 
//		}
//		
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

		// nes 20160521 - paso como parametro (faltaba...)
		stepToBeInjected.setAdminProcess(isAdminProcess); // esto va en true si es un administrador quien decide el paso ( en vez del usuario o grupo asignado )
		
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
	
//	/**
//	 * set managed data to a given wStepWork
//	 * for a new wStepWork (not persisted yet...) the ManagedData hasn't the wStepWork id and the
//	 * processWorkId, then the persist process will consider this situation ...
//	 * Note: this methods works with class properties like selectedStepDef and selectedProcess
//	 * nes 20140411
//	 * 
//	 * @param userId
//	 * @param stepWork
//	 */
//	private void _setManagedData(Integer userId, WStepWork stepWork) {
//		/**
//		 * ManagedData definition ...
//		 * 
//		 */
//		if (selectedStepDef.getDataFieldDef()!=null
//			&& selectedStepDef.getDataFieldDef().size() > 0	) {
//			try {
//				stepWork.setManagedData(
//						new WStepWorkBL().buildStepWorkManagedDataObject(
//								stepWork, userId)
//						);
//			} catch (WStepWorkException e) {
//				String mess="can't set managed data for new wStepWork object ... "
//						+e.getMessage()+" "+(e.getCause()!=null?e.getCause():"null");
//				logger.error(mess+" ... continues without managed data....");
//			}
//		}
//	}
	
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
	
	/**
	 * Process a step
	 * 
	 * nes 20151028 - creado porque antes habia que llamar directamente a WStepWorkBL y no es lo mas elegante...
	 * 
	 * @param idStepWork
	 * @param idResponse
	 * @param runtimeSettings
	 * @param isAdminProcess
	 * @param processingDirection
	 * @param autoLock
	 * @param currentUserId
	 * @return
	 * @throws WProcessDefException
	 * @throws WStepDefException
	 * @throws WStepWorkException
	 * @throws WStepSequenceDefException
	 * @throws WStepLockedByAnotherUserException
	 * @throws WStepNotLockedException
	 * @throws WUserDefException
	 * @throws WStepAlreadyProcessedException
	 * @throws WStepWorkSequenceException
	 * @throws WProcessWorkException
	 */
	public WStartProcessResult processStep (
			Integer idStepWork, Integer idResponse, WRuntimeSettings runtimeSettings,
			boolean isAdminProcess, String processingDirection, boolean autoLock, Integer currentUserId ) 
	
					throws WProcessDefException, WStepDefException, WStepWorkException, WStepSequenceDefException, 
							WStepLockedByAnotherUserException, WStepNotLockedException, WUserDefException, 
							WStepAlreadyProcessedException, WStepWorkSequenceException, WProcessWorkException {
	
		WStartProcessResult startProcessResult = new WStartProcessResult(VACIO);
		
		new WStepWorkBL().processStep(idStepWork, idResponse, runtimeSettings, isAdminProcess,
				processingDirection, autoLock, startProcessResult, currentUserId);
		
		
		return null;
		
	}
}
