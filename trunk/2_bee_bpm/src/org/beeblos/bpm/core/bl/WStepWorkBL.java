package org.beeblos.bpm.core.bl;

import static com.sp.common.core.util.ConstantsSPC.BEEBLOS_DEFAULT_REPOSITORY_ID;
import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;
import static com.sp.common.util.ConstantsCommon.EMPTY_OBJECT;
import static org.beeblos.bpm.core.util.Constants.ALIVE;
import static org.beeblos.bpm.core.util.Constants.BEEBLOS_WPROCESSWORK_DOCCLASS_ID;
import static org.beeblos.bpm.core.util.Constants.DEFAULT_PROCESS_STATUS;
import static org.beeblos.bpm.core.util.Constants.OMNIADMIN;
import static org.beeblos.bpm.core.util.Constants.PROCESS_STEP;
import static org.beeblos.bpm.core.util.Constants.TURNBACK_STEP;
import static org.beeblos.bpm.core.util.Constants.WRITE_EMAIL_TO_FILESYSTEM;
import static org.beeblos.bpm.core.util.Constants.W_SYSROLE_ORIGINATOR_ID;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepWorkDao;
import org.beeblos.bpm.core.error.CantLockTheStepException;
import org.beeblos.bpm.core.error.WExternalMethodException;
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
import org.beeblos.bpm.core.error.WUserRoleException;
import org.beeblos.bpm.core.error.WUserRoleWorkException;
import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WEmailAccount;
import org.beeblos.bpm.core.model.WExternalMethod;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessStatus;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepUser;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WStepWorkAssignment;
import org.beeblos.bpm.core.model.WStepWorkSequence;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.WUserRole;
import org.beeblos.bpm.core.model.WUserRoleWork;
import org.beeblos.bpm.core.model.enumerations.StartProcessResult;
import org.beeblos.bpm.core.model.enumerations.StepWorkStatus;
import org.beeblos.bpm.core.model.noper.StepWorkLight;
import org.beeblos.bpm.core.model.noper.WProcessDefThin;
import org.beeblos.bpm.core.model.noper.WRuntimeSettings;
import org.beeblos.bpm.core.model.noper.WStartProcessResult;
import org.beeblos.bpm.core.model.noper.WStepWorkCheckObject;
import org.beeblos.bpm.core.model.util.RouteEvaluationOrder;
import org.beeblos.bpm.tm.impl.MethodSynchronizerImpl;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.email.core.bl.SendEmailBL;
import com.email.core.error.SendEmailException;
import com.email.core.model.Email;
import com.email.tray.core.util.EmailPersonalizationUtilBL;
import com.sp.common.core.bl.DocumentManagerBL;
import com.sp.common.core.error.BeeblosBLException;
import com.sp.common.core.model.UserEmailAccount;
import com.sp.common.core.util.ApplicationURLUtil;
import com.sp.common.model.FileSP;
import com.sp.common.util.Resourceutil;

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
	 * It is possible to insert or start a new process in any step of the process map..
	 * 
	 * If the process start at 'begin' step then will be necesary execution of a 'processStep' because "begin step" is
	 * has no user assigned to process it. CALLER OF THIS METHOD must execute the processStep. 
	 * This behavior is intentionally defined to permit the developer different choices...
	 * (please see BeeBPMBL.injector ...)
	 * 
	 * @param processWork
	 * @param swco
	 * @param currentUserId
	 * @return
	 * @throws WStepWorkException
	 * @throws WProcessWorkException
	 * @throws WStepWorkSequenceException
	 */
	/*
	 *  TODO start dml 20150415:
	 *  OJO: AQUI NO ESTAMOS CONSIDERANDO SUBIR DOCUMENTOS ADJUNTOS
	 *  PORQ NO RECIBIMOS EL RUNTIMESETTINGS
	 */
	public Integer start(WProcessWork processWork, WStepWork stepWork, Integer currentUserId) 
			throws WStepWorkException, WProcessWorkException, WStepWorkSequenceException {
		logger.debug("start() WStepWork - processWork reference:"
				+(processWork!=null && processWork.getReference()!=null?processWork.getReference():"null")
				+" CurrentStep:"
				+(stepWork!=null && stepWork.getCurrentStep()!=null?stepWork.getCurrentStep().getName():"null")
				);
		
		Integer idWork;//nes 20141206 refactorized

		if ( processWork.getId()!=null && processWork.getId()!=0 ) {
			throw new WStepWorkException("Can't start new workflow with an existing work (work id:"+processWork.getId()+")");
		}

		// nes 20130912 - if process is not active throws exception
		if (!processWork.getProcessDef().isActive()) {
			logger.warn("Trying to start a new workflow referring an inactive process id:"+processWork.getProcessDef().getId());
			throw new WProcessWorkException("This process is inactive. Can't start a new workflow.");
		}

		if (processWork.getStatus() == null) {
			processWork.setStatus(new WProcessStatus(DEFAULT_PROCESS_STATUS));
		}

		// add the new ProcessWork
		WProcessWorkBL wpbl = new WProcessWorkBL();
		idWork = wpbl.add(processWork, currentUserId);
		processWork = wpbl.getWProcessWorkByPK(idWork, currentUserId); // checks all properties was correctly stored in the object
		
		// if exists managed data set it with just created workId
		if (stepWork.getManagedData()!=null) {
			stepWork.getManagedData().setCurrentWorkId(processWork.getId()); // process-work id
			System.out.println("TESTEAR ESTO QUE ESTE CARGANDO BIEN EL PROCESS-HEAD-ID");
			stepWork.getManagedData().setProcessId(processWork.getProcessHeadId()); // process head id
			stepWork.getManagedData().setOperation(INSERT);
//			swco.setManagedData(managedData);  // delegamos en el dao para que inserte el managed data
		}

		// if processWork was persisted ok then continue with step-work
		stepWork.setwProcessWork(processWork);
		// timestamp & trace info
		stepWork.setArrivingDate(new DateTime());
		stepWork.setInsertUser( new WUserDef(currentUserId) );
		stepWork.setModDate( DEFAULT_MOD_DATE_TIME );
		
		// nes 20140707 - if they're defined assigns runtime users to stepWork...
		if ( stepWork.getCurrentStep().isRuntimeAssignedUsers()) {
			_assignRuntimeUsers(
					stepWork, currentUserId, // currentUserId is the originator as we are in 'start' method ...
					stepWork.getCurrentStep().getIdUserAssignmentMethod(),
					currentUserId);
		}

		Integer idGeneratedStep= new WStepWorkDao().add(stepWork);
		
		/**
		 * nes 20141206 - assign users to runtime roles if they're defined...
		 * ( WUserRoleWork )
		 */
		_assignUsersToRuntimeRoles(processWork,currentUserId);

		// dml 20130827 - al inyectar insertamos un primer "log" con el primer step
		this.createStepWorkSequenceLog(null, stepWork, false, 
				null, stepWork.getCurrentStep(), currentUserId);

		_sendEmailNotification(stepWork, currentUserId);

		return idGeneratedStep;
	}
	
	
	/**
	 * process a step - returns qty new routes started ( new workitems generated if no end routes has)
	 * 
	 * precondition: wStepWork MUST BE LOCKED!!!
	 * 
	 * 	 
	 *  
	 * @param idStepWork
	 * @param idResponse
	 * @param runtimeSettings
	 * @param isAdminProcess - indicates the process will be realized as admin condition instead of assigned user task condition...
	 * @param typeOfProcess: PROCESS_STEP or TURNBACK_STEP
	 * @param autoLock - if true then this method will try to lock the step... (default:false)
	 * @param startProcessResult -> to return processing results (status, routes created, wStepWork created, errorList, etc)  // nes 20151026
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
	public Integer processStep (
			Integer idStepWork, Integer idResponse, /*String comments,*/ WRuntimeSettings runtimeSettings,
			/*Integer idProcess, Integer idObject, String idObjectType, */
			boolean isAdminProcess, String processingDirection, boolean autoLock, 
			WStartProcessResult startProcessResult, Integer currentUserId ) 
	
					throws WProcessDefException, WStepDefException, WStepWorkException, WStepSequenceDefException, 
							WStepLockedByAnotherUserException, WStepNotLockedException, WUserDefException, 
							WStepAlreadyProcessedException, WStepWorkSequenceException, WProcessWorkException {
		logger.debug(">>> processStep >> id:"+( idStepWork!=null?idStepWork:"null") );
		
		DateTime now = new DateTime();
		Integer qtyNewRoutes=0;
		startProcessResult.setStartProcessResult(StartProcessResult.FAIL); // nes 20151026 - comienzo poniendo a fail por si sale por throws...

		// nes 20151020 - store the list of generated wStepWorks
		List<WStepWork> generatedStepWorkList = new ArrayList<WStepWork>();

		/**
		 * convention over configuration
		 * nes 20150411
		 */
		if (processingDirection==null) {
			processingDirection=PROCESS_STEP;
		}
		// verifies the user has the step locked before process it ...
		// if not there may be possible an error in the process chain ...
		if (!checkLock(idStepWork, currentUserId, false)) {
			if (autoLock) {
				this.lockStep(idStepWork, currentUserId, currentUserId, isAdminProcess); // if can't lock the step throw exception...
			} else {
				throw new WStepNotLockedException("Current step is not locked. Please try process again ...");
			}
		}

		// check the step is 'pending process' ... 
		this.checkStatus(idStepWork, currentUserId, false); 

		// load current step from database
		WStepWork currentStep = new WStepWorkBL().getWStepWorkByPK(idStepWork, currentUserId);
		
		// if there are attached documents, then uploads to Beeblos and relate it with the wProcessWork
		_uploadRelatedDocumentsToProcessWork(runtimeSettings, currentUserId, currentStep);

		// sets managed data 
		currentStep.setManagedData( 
					(runtimeSettings!=null && runtimeSettings.getManagedData()!=null //nes 20150411
						?runtimeSettings.getManagedData():null)
				);//NOTA NESTOR: VER BIEN QUE HACEMOS AQUÍ ...
		
		// set current workitem to processed status
		_setCurrentWorkitemToProcessed( currentStep, runtimeSettings, idResponse, now, currentUserId ); // NES 20151104 - added runtimeSettings 

		// insert new steps 
		if ( processingDirection.equals(PROCESS_STEP) ) {
			logger.debug(">>> PROCESS_STEP");
			
			qtyNewRoutes = _executeProcessStep(runtimeSettings, currentStep, idResponse, 
												isAdminProcess, now, startProcessResult, // nes 20151026 
												generatedStepWorkList, currentUserId);
			
			logger.debug(">>> qty routes:"+qtyNewRoutes); 
			
			// if no new routes nor alive tasks then the process work is finished ...
			if(qtyNewRoutes.equals(0) 
				&& getStepWorkCountByProcess(
						currentStep.getwProcessWork().getProcessDef().getId(),ALIVE).equals(0)  ){

					new WProcessWorkBL().finalize(currentStep.getwProcessWork(), currentUserId);

			}

			
		} else if ( processingDirection.equals(TURNBACK_STEP) ) {

			_executeTurnBack(runtimeSettings, currentUserId, currentStep, idResponse, isAdminProcess, now);
			qtyNewRoutes=1;
			
		}
		
		// nes 20151026 - agrego las rutas recorridas a la lista ... (antes de que se llame a si mismo y recorra nuevas ...)
		startProcessResult.setQtyStepWorkCreated(startProcessResult.getQtyStepWorkCreated()+qtyNewRoutes);
		
		/**
		 * if automatic steps will be created then try to processs it ( RECURSIVE )
		 * nes 20151020
		 */
		if (generatedStepWorkList.size()>0) {
			_processJustGeneratedAutomaticSteps(runtimeSettings, isAdminProcess, 
					processingDirection,
					startProcessResult, // nes 20151026
					generatedStepWorkList, currentUserId);
		}

		/**
		 * tanto si va por la ruta normal o marcha atrás, si ejecuta es que el proceso 
		 * termina bien, si saliese por error seria StartProcessResult.FAIL...
		 */
		startProcessResult.setStartProcessResult(StartProcessResult.SUCCESS);
		
		return qtyNewRoutes; // devuelve la cantidad de nuevas rutas generadas ...
		
	}

	/**
	 * if automatic steps will be created then try to processs it
	 * nes 20151020
	 *  
	 * @param runtimeSettings
	 * @param currentUserId
	 * @param isAdminProcess
	 * @param typeOfProcess
	 * @param generatedStepWorkList
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
	private void _processJustGeneratedAutomaticSteps(
			WRuntimeSettings runtimeSettings, 
			boolean isAdminProcess, String typeOfProcess,
			WStartProcessResult startProcessResult, // nes 20151026
			List<WStepWork> generatedStepWorkList, Integer currentUserId ) {
		/**
		 * for each end event it must be processed because end-event has no human interfase to resolve
		 * the task...
		 */
		for (WStepWork wsw: generatedStepWorkList) {
			Integer NULL_RESPONSE=null;
			boolean AUTO_LOCK=true;
			if (wsw.getCurrentStep().getStepTypeDef().isAutomaticProcess()){
				try {
					this.processStep (
							wsw.getId(), NULL_RESPONSE, runtimeSettings,
							isAdminProcess, typeOfProcess, AUTO_LOCK, startProcessResult, currentUserId );
				}  catch (Exception e) { // NOTA NES 20151020 - ESTO HABRIA QUE LOGUEARLO EN UNA TABLA PARA VER LUEGO QUE HACER CON EL ERROR!!
					logger.error("ERROR!!!: _processJustGeneratedAutomaticSteps class:"
							+e.getClass()+" "
							+" wswId:"+(wsw!=null&&wsw.getId()!=null?wsw.getId():"null")+"  "
							+e.getMessage()+" "
							+(e.getCause()!=null?e.getCause():" "));
				}
			}
		}
	}

	/**
	 * @param runtimeSettings
	 * @param currentUser
	 * @param currentStep
	 */
	private void _uploadRelatedDocumentsToProcessWork(WRuntimeSettings runtimeSettings,
			Integer currentUser, WStepWork currentStep) {
		/**
		 * @author dmuleiro 20150414 - new attached documents will be uploaded (if there are any)
		 * attached documents will be related with a wProcessWork
		 * at each wStepWork will be exposed or not (designer responability)
		 * 
		 */
		if (currentStep != null && currentStep.getwProcessWork() != null
				&& currentStep.getwProcessWork().getId() != null
				&& runtimeSettings != null && runtimeSettings.getFileSPList() != null){
			this.uploadFileInfoList(currentStep.getwProcessWork().getId(), 
					runtimeSettings.getFileSPList(), currentUser);
		}
	}

	/**
     * Upload new documents to Beeblos (if there are any) and relates it with WProcessWork
     * 
     * @author dmuleiro 20150414
     * 
     * @return
	 * @throws WStepWorkException 
     */
	public void uploadFileInfoList(Integer idWProcessWork, List<FileSP> newAttachedDocuments, 
			Integer currentUserId) {

		logger.debug(">>> uploadFileInfoList init");

		if (newAttachedDocuments == null || newAttachedDocuments.isEmpty()){
			return;
		}
		
		try {

			List<Long> returnedBeeblosId = 
					new DocumentManagerBL().uploadFileSPListToBeeblos(
					newAttachedDocuments, BEEBLOS_DEFAULT_REPOSITORY_ID,
					BEEBLOS_WPROCESSWORK_DOCCLASS_ID, WProcessWork.class.getName(),
					idWProcessWork.toString(), 
					"WProcessWork id:" + idWProcessWork, 
					null, currentUserId);

			logger.debug("uploadFileInfoList. returnedBeeblos docids: " + returnedBeeblosId.toString());

		} catch (Exception e) {
			String mess="Error trying to upload beeblos files. " + e.getMessage()
					+ (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
		}

	}


	
	public Integer add(WStepWork stepw, Integer currentUser) throws WStepWorkException {
		
		logger.debug("add() WStepWork - CurrentStep-Work: ["+stepw.getCurrentStep().getName()
				+"-"+stepw.getwProcessWork().getReference()+"]");
		
		// timestamp & trace info
		stepw.setArrivingDate(new DateTime());
		stepw.setInsertUser( new WUserDef(currentUser) );
		stepw.setModDate( DEFAULT_MOD_DATE_TIME);
		stepw.setModUser(currentUser);
		return new WStepWorkDao().add(stepw);

	}

	/**
	 * Create wStepWork managed data for a wStepWork object...
	 * @param stepWork
	 * @param currentUserId
	 * @throws WStepWorkException
	 */
	public void loadStepWorkManagedData(
			WStepWork stepWork, Integer currentUserId) throws WStepWorkException{
		new WStepWorkDao().loadStepWorkManagedData(stepWork);
	}
	
	/**
	 * Create wStepWork managed data for a wStepWork object...
	 * @param stepWork
	 * @param currentUserId
	 * @throws WStepWorkException
	 */
	public ManagedData buildStepWorkManagedDataObject(
			WStepWork stepWork, Integer currentUserId) throws WStepWorkException{
		return new WStepWorkDao().buildStepWorkManagedDataObject(stepWork);
	}
	
	
	
	// ######### TRANSACCION URGENTE METER TRANSACCION #####################
	/**
	 * updates a step work
	 * 
	 * nes 20140707 - added currentUserId as param
	 * 
	 * @param stepw
	 * @param currentUserId
	 * @throws WStepWorkException
	 */
	public void update(WStepWork stepw, Integer currentUserId) throws WStepWorkException {
		logger.debug(">>> update WStepWork id:" + (stepw!=null && stepw.getId()!=null?stepw.getId():"null") );
		
		if ( !stepw.equals(new WStepWorkDao().getWStepWorkByPK(stepw.getId())) ) {
			logger.debug(">>>>>>>> 0");
			// timestamp & trace info
			stepw.setModDate(new DateTime());
			stepw.setModUser(currentUserId);
			logger.debug(">>>>>>>> 1");
			new WStepWorkDao().update(stepw,currentUserId);
			logger.debug(">>>>>>>> 2");
		} else {
			
			logger.debug("WStepWorkBL.update - nothing to do ...");
		}
		logger.debug(">>>>>>>> 3");
	}
	
	
	public void delete(WStepWork stepw, Integer currentUser) throws WStepWorkException {

		logger.info("delete() WStepWork - Name: ["+stepw.getId()+"] user:"+currentUser);
		
		new WStepWorkDao().delete(stepw);

	}

	public WStepWork getWStepWorkByPK(Integer id, Integer currentUser) throws WStepWorkException {

		return new WStepWorkDao().getWStepWorkByPK(id);
	}

	public Integer getIdProcessWorkFromWStepWork(Integer id, Integer currentUser) throws WStepWorkException {

		return new WStepWorkDao().getIdProcessWorkFromWStepWork(id);
	}

	public List<WStepWork> getWStepWorks(Integer currentUser) throws WStepWorkException {

		return new WStepWorkDao().getWStepWorks();
	
	}
	
	// dejo comentada nes 20140705
//	/**
//	 * Este método no tiene sentido porque a priori no tendria sentido devolver una lista de wStepWork para
//	 * comobo y además sin filtrar,o sea toda la bd???
//	 * @param textoPrimeraLinea
//	 * @param separacion
//	 * @return
//	 * @throws WStepWorkException
//	 */
//	@Deprecated
//	public List<StringPair> getComboList(
//			String textoPrimeraLinea, String separacion )
//	throws WStepWorkException {
//		 
//		return new WStepWorkDao().getComboList(textoPrimeraLinea, separacion);
//
//
//	}

	/**
	 * returns true if exists active process for process id, id object, idObjectType ...
	 * 
	 * @param processId - >> refers to wProcessDefId
	 * @param idObject
	 * @param idObjectType
	 * @param currentUser
	 * @return
	 * @throws WStepWorkException
	 */
	public Boolean existsActiveProcess(
			Integer processId, Integer idObject, String idObjectType, Integer currentUser ) 
	throws WStepWorkException {
		
		return new WStepWorkDao().existsActiveProcess(processId, idObject, idObjectType);
	}
	
	/**
	 * checks if exists processes related with given ids...
	 * User is responsible to provide a coherent set of ids ... 
	 * To check for alive process please use existsActiveProcess 
	 * 
	 * @param processId
	 * @param idObject
	 * @param idObjectType
	 * @param currentUser
	 * @return
	 * @throws WStepWorkException
	 */
	public Boolean existsProcess(
			Integer processId, Integer idObject, String idObjectType, Integer currentUser ) 
	throws WStepWorkException {
		
		return new WStepWorkDao().existsProcess(processId, idObject, idObjectType);
	}

	/**
	 * returns qty of existing step works for a given processDefId (process version)
	 * 
	 * @param processId  >> refers to wProcessDef id
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
	 * @param  Integer stepId >> refers to wStepDef id
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

	
	/**
	 * Returns alive stepWork items for a given idObject / idObjectType WITH PERMISSIONS!
	 * 
	 * ***** WORK method ******
	 * 
	 * @author dmuleiro 20140529
	 * 
	 * @param idObject
	 * @param idObjectType
	 * @param hasAdminRights
	 * @param currentUserId
	 * @return
	 * @throws WProcessDefException
	 * @throws WStepDefException
	 * @throws WStepWorkException
	 */
	public List<WStepWork> getAliveSteps(Integer idObject, String idObjectType, 
			boolean isAdmin, Integer currentUserId) throws WStepWorkException  {
		
		// TODO: filtrar para el usuario que lo solicita		
		List<WStepWork> stepList = new WStepWorkDao().getAliveSteps(idObject, idObjectType);
		
		/**
		 * Obtenemos de la lista de steps vivos los que tienen permiso para el "currentUserId"
		 */
		stepList = this._checkUserPermisionnsForStepList(currentUserId, stepList, isAdmin, currentUserId);
		
		// nota: refactoricé un hasAdminRights que usabamos en otro método para que puedas usarlo aquí ...
		// 
		
		return stepList;
	}
	
	/**
	 * Returns a string list with alive current steps for synchronization with external systems
	 * (like show in a grid current status of process, etc)
	 * 
	 * @param idObject
	 * @param idObjectType
	 * @param isAdmin
	 * @param currentUserId
	 * @return
	 * @throws WStepWorkException
	 */
	public List<String> getAliveStepNameList(Integer idObject, String idObjectType, Integer currentUserId) 
			throws WStepWorkException  {
		
		
		List<String> stepList = new WStepWorkDao().getAliveStepNameList(idObject, idObjectType);
		
		return stepList;
	}
	
	/** 
	 * Recibe una lista de StepWork y devuelve la lista de los cuales el "currentUserId" tiene permisos
	 * 
	 * Para cada WStepWork de la lista recibida:
	 * 
	 * 1. si hasAdminRights está en true: 
	 * si currentUserId es admin de este proceso wStepWork.wProcessWork.processDef entonces devolvemos el
	 * elemento y si no lo es pasamos al siguiente punto ...
	 * 
	 * 2. si hasAdminRights está en false: 
	 * simplemente a cada elemento devuelto por el dao hay que averiguar si el currentUserId tiene 
	 * permisos para procesarlo, si los tiene lo devolvemos, si no, lo quitamos de la lista.
	 * 
	 * @author dmuleiro 20140529
	 * 
 	 * nes 20140628 - its 391 - el "user" debe ser el del bpm (WUserDef) que es el que
	 * tiene los roles
	 * 
	 * @param userId
	 * @param stepList
	 * @param isAdmin
	 * @param currentUserId
	 * @return
	 */
	private List<WStepWork> _checkUserPermisionnsForStepList(Integer userId, // cambiado el nombre nes 20140705
			List<WStepWork> stepList, boolean isAdmin, Integer currentUserId){
		
		WUserDef user = null;
		try {
			user = new WUserDefBL().getWUserDefByPK(userId);
		} catch (WUserDefException e) {
			logger.error("Error getting WUserDef to check user permissions wuid:"+userId);
			e.printStackTrace();
		}
		
		if (stepList != null && user != null){
			
			List<WStepWork> stepWorkWithPermissionsList = new ArrayList<WStepWork>();
			
			for (WStepWork step : stepList){
				
				try {
					/**
					 * If request comes at "admin" mode, then check if the user is an admin ... 
					 */
					if (isAdmin){
						
						if (new WProcessDefBL().userIsProcessAdmin(
								currentUserId, step.getwProcessWork().getProcessDef().getId(), currentUserId)){
							stepWorkWithPermissionsList.add(step);
							continue;
						}
					}
					
					if (this.userHasStepWorkPermissions(user, step, isAdmin, currentUserId)){
						stepWorkWithPermissionsList.add(step);
					}
						
				} catch (WProcessDefException e) {
					logger.error("Error trying to check the processDef with id <" 
							+ step.getwProcessWork().getProcessDef().getId() + "> permissions: " + e.getMessage());
					e.printStackTrace();
				}
			
			}
			
			return stepWorkWithPermissionsList;

		}
	
		return null;
	}
		
	/**
	 * Comprueba si un "user" tiene permisos dentro de un "WStepWork" (para ello comprueba dentro del 
	 * "currentStep" asociado al mismo y dentro del set de "WStepWorkAssignments"
	 * 
	 * @author dmuleiro 20140529
	 * 
	 * @param user
	 * @param step
	 * @param hasAdminRights
	 * @param currentUserId
	 * @return
	 */
	public boolean userHasStepWorkPermissions(
			WUserDef user, WStepWork step, boolean hasAdminRights, Integer currentUserId){
		
		if (step != null){
			
			/**
			 * Si el WStepDef definido asociado a este WStepWork tiene permisos devuelve "true"
			 */
			if (new WStepDefBL().userHasStepDefPermission(user, step.getCurrentStep())){
				return true;
			}
			
			/**
			 * Comprobamos el idAssignedUser/idAssignedRole de los WStepWorkAssignments
			 */
			if (step.getAssignedTo() != null){
				for (WStepWorkAssignment stepWorkAssignment : step.getAssignedTo()){
					
					if (stepWorkAssignment.getIdAssignedUser() != null
							&& stepWorkAssignment.getIdAssignedUser().equals(user.getId())){
						return true;
					}
					
					if (stepWorkAssignment.getIdAssignedRole() != null){
						
						if (user.getRolesRelated() != null){
							
							for (WUserRole userRole : user.getRolesRelated()){ //nes 20140628 - its 391
							
								if (stepWorkAssignment.getIdAssignedRole().equals(userRole.getRole().getId())){
									return true;
								}
							}
						}
					}
				}
			}
		}
		
		return false;
	}

	
	/**
	 *
	 * retrieve workitems for an userId with
	 * status: null=all, A=alive P=Processed	 
	 *
	 * ***** WORK method ******  
	 * 
	 * @param userId
	 * @param idProcess
	 * @param status
	 * @param currentUser
	 * @return
	 * @throws WProcessDefException
	 * @throws WStepDefException
	 * @throws WStepWorkException
	 */
	public List<WStepWork> getStepListByUser(
			Integer userId, Integer idProcess, String status, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		return new WStepWorkDao().getStepListByUser(userId, idProcess, status);
		
		
	}

	
	
	/**
	 * 
	 * retrieves workitems for a process
	 * status: null=all, A=alive P=Processed
	 * 
	 * ***** ADMIN method ****** 
	 * 
	 * @param idProcess
	 * @param status
	 * @param currentUser
	 * @return
	 * @throws WProcessDefException
	 * @throws WStepDefException
	 * @throws WStepWorkException
	 */
	public List<WStepWork> getWorkListByProcessAndStatus(
			Integer idProcess, String status, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		// TODO: filtrar para el usuario que lo solicita		
		return new WStepWorkDao().getWorkListByProcessAndStatus(idProcess, status);
		
		
	}


 
	/**
	 * returns workitems for a process and a step ( idCurrentStep )
	 * 
	 * ***** ADMIN method ****** 
	 * 
	 * @param idProcess
	 * @param idCurrentStep
	 * @param currentUser
	 * @param status
	 * @return
	 * @throws WProcessDefException
	 * @throws WStepDefException
	 * @throws WStepWorkException
	 */
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
	 * @param openDate
	 * @param deadlineDate
	 * @param filtroComentariosYReferencia
	 * @return
	 * @throws WStepWorkException
	 */
	public List<WStepWork> getWorkListByProcess (
			Integer idProcess, Integer idCurrentStep, String status,
			Integer userId, boolean isAdmin, 
			LocalDate arrivingDate, LocalDate openDate, LocalDate deadlineDate, 
			String filtroComentariosYReferencia, Integer currentUserId) 
	throws WStepWorkException {
		
		/**
		 * checks if currentUserId is admin ...
		 *  if not then forces it to off  
		 */
		
		boolean userHasAdminRights = false;
		userHasAdminRights = hasAdminRights(idProcess, userId, currentUserId);
		
		if (!userHasAdminRights) {
			isAdmin=false;
		}
		
		return new WStepWorkDao()
						.getWorkListByProcess(
								idProcess, idCurrentStep, status,
								userId, isAdmin, arrivingDate, openDate, 
								deadlineDate, filtroComentariosYReferencia,
								currentUserId);
		
	}

	/**
	 * Check if a given user has admin rights over given idProcess...
	 * 
	 * refactorizado nes 20140529
	 * 
	 * @param idProcess
	 * @param userId
	 * @param currentUserId
	 * @return
	 */
	private boolean hasAdminRights(Integer idProcess, Integer userId,
			Integer currentUserId ) {
		boolean userHasAdminRights=false;
		try {
			userHasAdminRights = 
					new WProcessDefBL().userIsProcessAdmin(userId, idProcess, currentUserId);
		} catch (WProcessDefException e) {
			String mess="Error trying check if user is admin process for userid:"
					+(userId!=null?userId:"null")
					+ " and processid:"
					+(idProcess!=null?idProcess:"null")+" "
					+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
		}
		return userHasAdminRights;
	}
	

	/**
	 * returns workitems for an object of a given process
	 * (recupera los workitems de 1 objeto para 1 proceso dado)
	 * ADMIN method
	 *  
	 * @param idProcess
	 * @param idObject
	 * @param idObjectType
	 * @param currentUser
	 * @return
	 * @throws WProcessDefException
	 * @throws WStepDefException
	 * @throws WStepWorkException
	 */
	public List<WStepWork> getWorkListByProcess(
			Integer idProcess, Integer idObject, String idObjectType, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		
		return new WStepWorkDao().getWorkListByProcess(idProcess, idObject, idObjectType, currentUser);
		
	}


	/**
	 * Devuelve todos los workitems de un elemento dado, tanto los pendientes
	 * como los ya realizados
	 * 
	 * Nota: es muy parecido al método getActiveSteps de esta misma clase que devuelve 
	 * todos los de un idObject/idObjectType pero que estén activos ...
	 * 
	 * creó: rrl 20110118
	 * 
	 * ***** ADMIN method ******
	 * 
	 *  
	 * @param idObject
	 * @param idObjectType
	 * @param currentUser
	 * @return
	 * @throws WProcessDefException
	 * @throws WStepDefException
	 * @throws WStepWorkException
	 */
	public List<WStepWork> getWorkListByIdObject(
			Integer idObject, String idObjectType, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		return new WStepWorkDao().getWorkListByIdObject(idObject, idObjectType, currentUser);
		
	}
	
	/**
	 * 
	 * returns all workitems for a given ProcessWork - 
	 * add filter by processed or all workitems.
	 * idProcessWork must not be null.
	 * 
	 * ***** ADMIN method ******
	 *  ATENCION: no filtra los del currentUserId, devuelve todos.
	 *  Es un método para admin...
	 *  
	 * @param idProcessWork
	 * @param status
	 * @param currentUser
	 * @return
	 * @throws WProcessDefException
	 * @throws WStepDefException
	 * @throws WStepWorkException
	 */
	public List<WStepWork> getWorkListByIdWorkAndStatus(
			Integer idProcessWork, String status, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		return new WStepWorkDao().getWorkListByIdWorkAndStatus(idProcessWork, status, currentUser);
		
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
//			DateTime now = new DateTime();
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
	throws WStepLockedByAnotherUserException,  WStepNotLockedException, WStepWorkException {
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
				throw new WStepNotLockedException(message);
		} catch (WUserDefException e) {
			String message = "Current user indicated does not exist or have a problem ("
					+ currentUser
					+ ") The step can't be locked ..." 
					+ e.getMessage() + " - "
					+ e.getCause();
				logger.warn(message);
				storedStep=null;
				throw new WStepNotLockedException(message);
		} 
		logger.debug(">> step was locked!! idStepWork:"+(idStepWork!=null?idStepWork:"null"));
		return storedStep;

	}

	/**
	 * confirms a wStepWork is locked (required condition to process it...)
	 * @param idStepWork
	 * @param currentUser
	 * @param isAdminUser
	 * @return
	 * @throws WStepLockedByAnotherUserException
	 */
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
				String message="This step was already processed... id:"+idStepWork;
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

	/**
	 * lock given step
	 * 
	 * @param idStepWork - step work to lock
	 * @param lockUserId - user will be lock the step
	 * @param currentUserId - current user sending the request
	 * @param isAdminUser - current user work as an admin instead of as assigned user/group for the task: ATENCION!!: NO ESTAMOS CONTROLANDO QUE EL USUARIO CURRENT SEA UN ADMIN...
	 * @return
	 * @throws WStepNotLockedException
	 * @throws WStepLockedByAnotherUserException
	 * @throws WStepWorkException
	 */
	public Boolean lockStep (
			Integer idStepWork, Integer lockUserId, Integer currentUserId, boolean isAdminUser ) 
	throws WStepNotLockedException, WStepLockedByAnotherUserException, WStepWorkException {
		logger.debug(">> lockStep idStepWork:"+(idStepWork!=null?idStepWork:"null"));
		
		// get the step
//		WStepWork stepToLock = 
//				new WStepWorkBL().getWStepWorkByPK(idStepWork, currentUser);
		
		// 1st check user permissions
		

		_lockStep(idStepWork, currentUserId, isAdminUser);
		
		return true;

	}
	
	private void _setOpenInfo(Integer currentUser, WStepWork storedStep) throws WUserDefException {
		// set step opened date and user
		if ( storedStep.getOpenedDate()==null ) {
			storedStep.setOpenedDate(new DateTime());
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
	 * @param currentUserId
	 * @param isAdmin - trying lock the step as admin condition...
	 * @throws WStepWorkException
	 * @throws WStepLockedByAnotherUserException
	 */
	private void _lockStep( Integer idStepWork, Integer currentUserId, boolean isAdmin )
			throws WStepWorkException, WStepLockedByAnotherUserException {
		logger.debug(">> _lockStep 2 idStepWork:"+(idStepWork!=null?idStepWork:"null"));
		
		// IMPLEMENTAR BIEN ESTO Y CHEQUEAR EL USUARIO QUE LOCKEA EL PASO
		
		// if step is not locked then locks it !
		if ( _checkCurrentLockStatus(idStepWork) ) {

			DateTime now = new DateTime();

			new WStepWorkDao()
						.lockStepWork( idStepWork, now, currentUserId, isAdmin );
			
			
		} else {
			//if ( !stepToLock.getLockedBy().getId().equals(currentUser)) { 
				String message = "The indicated step ("+ idStepWork +") is already locked by another user ..." ;
				logger.info(message);
				throw new WStepLockedByAnotherUserException(message);
			//}
		}
	}
	
	// unlocks a step. 
	private void _unlockStep( WStepWork stepToUnlock, Integer currentUserId, boolean isAdmin ) 
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
			stepToUnlock.setModDate(new DateTime());
			stepToUnlock.setModUser(currentUserId);
			
			new WStepWorkDao().unlockStepWork(
									  stepToUnlock.getId(),stepToUnlock.getModDate(),
									  stepToUnlock.getModUser(), isAdmin);
						
			if ( isAdmin ) {
				logger.info("Step: "+stepToUnlock.getId()+" was unlocked by admin:"+currentUserId);
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
	 * @param currentStepWork
	 * @param idResponse
	 * @param isAdminProcess
	 * @param now
	 * @param startProcessResult - resultado del procesamiento (lista de step work ids, lista de errores o warnings..)  // nes 20151026
	 * @param generatedStepWorkList - lista de stepWorks generados... para luego ver si procesa esas rutas...
	 * @param currentUserId
	 * @return
	 * @throws WStepWorkException
	 * @throws WStepSequenceDefException
	 * @throws WUserDefException
	 * @throws WStepDefException
	 * @throws WStepWorkSequenceException
	 */
	private Integer _executeProcessStep(
			WRuntimeSettings runtimeSettings, WStepWork currentStepWork, 
			Integer idResponse, boolean isAdminProcess, DateTime now, 
			WStartProcessResult startProcessResult,  // nes 20151026
			List<WStepWork> generatedStepWorkList,  Integer currentUserId ) 
	throws WStepWorkException, WStepSequenceDefException, WStepDefException, WStepWorkSequenceException {
		if (logger.isDebugEnabled()){
			logger.debug(">>> _executeProcessStep >> idStepWork"+currentStepWork.getId()
					+" isAdminProcess:"+isAdminProcess
					+" idResponse:"+idResponse);
		}
		
		Integer qty=0;
		
		// create an empty object to build new steps
		WStepWork newStepWork = new WStepWork();
		
		
		System.out.println("VERIFICAR QUE CARGUE CORRECTAMENTE LAS RUTAS PARA EL RUTEO DEL STEP ...");
		
		try {
		
			/**
			 * load outgoing routes from current step
			 * nes:20150505 - its: 967 - arreglado el método getStepSequenceList para evitar que traiga rutas deleted
			 */
			List<WStepSequenceDef> routes = new WStepSequenceDefBL()
													.getStepSequenceList(
															currentStepWork.getwProcessWork().getProcessDef().getId(), 
															currentStepWork.getCurrentStep().getId(),
															currentUserId);
			
	
			logger.debug(">>> _executeProcessStep >> qty routes:"+routes.size());
			// TODO: urgentemente definir transaccion aquí ...
			
			/**
			 * if there is outgoing routes defined then generates new wStepWork that correspond as
			 * bussiness logic defined...
			 */
			if ( routes.size() > 0 ) { // generates next steps / routes
			
				/**
				 *  process each route ( generates a new step for each new valid route )
				 */
				for (WStepSequenceDef route: routes ) {
					logger.debug(">>> _executeProcessStep >> "+route.getId()+"/"+route.getName());
					
					/**
					 *  if corresponds to get this route....
					 *  Route may come with no parameters and no responses. In this scenario, the route
					 *  will be processed....
					 *  nes 20150411 - refactorized - created method _isEnabledRoute to clean the logic....
					 */
					if ( _isEnabledRoute(route, idResponse) ) {
						logger.debug(">>> _executeProcessStep >> processing route to: "
								+(route.getToStep()!=null?route.getToStep().getName():"end this route..."));
	
						/**
						 * obtains destiny step of this route ...
						 */
						if ( route.getToStep()!=null ) {
							
							qty++;
							
							_setNewStepWorkAndPersists(runtimeSettings,
									currentUserId, currentStepWork, isAdminProcess,
									now, newStepWork, route);
							
							// dml 20130827 - si vamos hacia adelante (procesamos) el beginStep y el endStep los marca la ruta
							this.createStepWorkSequenceLog(route, newStepWork, false, 
									route.getFromStep(), route.getToStep(), currentUserId);
	
							// nes 20151020 - devuelvo ahora la lista de pasos para post-procesar
							generatedStepWorkList.add(newStepWork);
							
							startProcessResult.getStepWorkIdList().add(newStepWork.getId()); // nes 20151026
							
							/**
							 *  if route has external method execution related then execute it!
							 */
							_executeRouteExternalMethodSafe(route, currentStepWork, currentUserId);
							
							/**
							 *  checks for notification subscribers for the new step and send emails
							 */
							_sendEmailNotification(newStepWork, currentUserId);
							
							// nes 20130913
							// if route evaluation order is first true condition then breaks for loop and return
							if ( routeEvaluationOrderHasFirstTrueCondition(currentStepWork) ) {
								break;
							}
						}
						else {  // ending routes ( not destiny step ...)
							
							logger.debug(">>> _executeProcessStep ending routes ( not destiny step ...)");// nes 20151020
							
							// write step-work-sequence log file 
							this.createStepWorkSequenceLog(route, currentStepWork, false, 
									route.getFromStep(), null, currentUserId);
	
							// if route has external method execution then execute it!
							_executeRouteExternalMethodSafe(route, currentStepWork, currentUserId);
							
							// this route points to end tree - no other action required ...
							// if ret arrives here with false, all ok; 
							// if ret arrives here with true it indicates there are another valid routes for this step
							// but no action is required
						}
					} // end if _isEnabledRoute
				} // go to next route ... (endfor)
				
			} else { // no next steps - this tree finishes here ...
				
				qty = 0; // no new routes ...
			
			}

			// nes  - agregado para que anote en el log una exception aqui ya que estamos en niveles muy internos
			// 			del proceso y luego es dificil detectar el error real...
		} catch (WStepWorkException e) {
			String mess = "Exception ERROR WStepWorkException _executeProcessStep >>> "+ e.getMessage()+" "
					+(e.getCause()!=null?e.getCause():" ")+" "
					+ e.getClass();
			logger.error(mess);
			throw e;
		} catch (WStepSequenceDefException e) {
			String mess = "Exception ERROR WStepSequenceDefException _executeProcessStep >>> "+ e.getMessage()+" "
					+(e.getCause()!=null?e.getCause():" ")+" "
					+ e.getClass();
			logger.error(mess);
			throw e;
			

		} catch (WStepDefException e) {
			String mess = "Exception ERROR WStepDefException _executeProcessStep >>> "+ e.getMessage()+" "
					+(e.getCause()!=null?e.getCause():" ")+" "
					+ e.getClass();
			logger.error(mess);
			throw e;
		} catch (WStepWorkSequenceException e) {
			String mess = "Exception ERROR WStepWorkSequenceException _executeProcessStep >>> "+ e.getMessage()+" "
					+(e.getCause()!=null?e.getCause():" ")+" "
					+ e.getClass();
			logger.error(mess);
			throw e;

		
		
		} catch (Exception e) {
			String mess = "Exception ERROR _executeProcessStep >>> "+ e.getMessage()+" "
					+(e.getCause()!=null?e.getCause():" ")+" "
					+ e.getClass();
			logger.error(mess);
			throw new WStepWorkException(mess);
		}
		
		if (qty==0) {
			logger.warn(">>> no outgoing routes were generated ... this process finishes here ... ");
		}

		return qty;
		
	}

	/**
	 * checks if the route is enabled or is selected...
	 * nes 20150411
	 * @return
	 */
	private boolean _isEnabledRoute(WStepSequenceDef route, Integer idResponse) {
		if ( route.isAfterAll() 
				|| route.isEnabled() 
				&& ( route.getValidResponses()==null
					|| route.getValidResponses().length()==0
					|| _routeBelongsValidResponseSelected(idResponse, route) ) ) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Checks if a step has first true condition for route evaluation order 
	 * for outgoing routes ...
	 * 
	 * @param currentStepWork
	 * @return
	 */
	private boolean routeEvaluationOrderHasFirstTrueCondition(
			WStepWork currentStepWork) {
		return currentStepWork.getCurrentStep().getRouteEvalOrder() != null
				&& currentStepWork.getCurrentStep().getRouteEvalOrder()
					.equals(RouteEvaluationOrder.FIRST_TRUE_CONDITION.getId());
	}

	/**
	 * Checks if route belongs with the response seleted for currentStep
	 * @param idResponse
	 * @param route
	 * @return
	 */
	private boolean _routeBelongsValidResponseSelected(Integer idResponse,
			WStepSequenceDef route) {
		return route.getValidResponses()!=null 
				/**
				 * each route stores id list with valid responses accepted to 
				 * to get this route...
				 */
			&& route.getValidResponses().contains(idResponse.toString().trim()+"|");
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
		
		sws.setExecutionDate(new DateTime());

		new WStepWorkSequenceBL().add(sws, currentUserId);
		
	}

	/**
	 * Sets a new step work and add it to db
	 * If new step has user assignment at runtime, executes referred method to assign it...
	 * 
	 * @param runtimeSettings
	 * @param currentUser
	 * @param currentStepWork
	 * @param isAdminProcess
	 * @param now
	 * @param newStepWork
	 * @param route
	 * @throws WStepDefException
	 * @throws WStepWorkException
	 */
	private void _setNewStepWorkAndPersists(
			WRuntimeSettings runtimeSettings, Integer currentUserId,
			WStepWork currentStepWork, boolean isAdminProcess, DateTime now,
			WStepWork newStepWork, WStepSequenceDef route)
			throws WStepDefException, WStepWorkException {
		
		_setNewStep(newStepWork, currentStepWork, route.getToStep(), runtimeSettings, currentUserId, now, isAdminProcess );
		
		/**
		 * Assign run time assigned users ...
		 */
		if ( newStepWork.getCurrentStep().isRuntimeAssignedUsers()) {
			List<WStepWorkAssignment> swal = _assignRuntimeUsers(
						newStepWork, currentStepWork.getwProcessWork().getInsertUser(), // nes 20141016 - originator
						newStepWork.getCurrentStep().getIdUserAssignmentMethod(), // external method to load runtime users...
						currentUserId);
			if (swal!=null && swal.size()>0) {
				newStepWork.getAssignedTo().addAll(swal);
			}
		}


		/**
		 * persist new step work
		 */
		this.add(newStepWork, currentUserId);
	
	}

	
	/**
	 * Assign users to runtime roles if it has defined for this process...
	 * nes 20141216
	 * 
	 * @param processWork
	 * @param currentUserId
	 */
	public void _assignUsersToRuntimeRoles(WProcessWork processWork, Integer currentUserId){
		logger.debug(">>> _assignUsersToRuntimeRoles ... idProcessDef:"
					+(processWork!=null?processWork.getProcessDef().getId():"null"));
		
		try {
			// get runtime role list
			List<WRoleDef> runtimeRoleList = 
					new WProcessDefBL().getProcessRuntimeRoles(processWork.getProcessDef().getId(), currentUserId);
			//for each runtime role assign users
			if(runtimeRoleList!=null && runtimeRoleList.size()>0) {
				for (WRoleDef role: runtimeRoleList){
					if (role.isRuntimeRole() 
							&& role.getIdExternalMethod()!=null
							&& role.getIdExternalMethod()!=0) {
						_assignRuntimeUsersToRole(processWork, role, currentUserId);
					} 
				}
			}
		} catch (WProcessDefException e) {
			/**
			 * continues to finish start process with warnings!!
			 */
			String mess="WProcessDefException: error trying to get runtime roles for this process... MUST CHECK THIS INJECTED PROCESS!!"
					+e.getMessage()+" "
					+(e.getCause()!=null?e.getCause():" ");
			logger.error(mess);
		}
		
		
	}

	/**
	 * Obtains runtime user list and load it for this instance (wProcessWork)
	 * nes 20141215
	 * 
	 * @param processWork
	 * @param role
	 * @param currentUserId
	 */
	private void _assignRuntimeUsersToRole(WProcessWork processWork, WRoleDef role, Integer currentUserId){
		logger.debug(">>> _assignRuntimeUsersToRole"+(role!=null?role.getId():"null")
				+" idExternalMethod:"+(role.getIdExternalMethod()!=null?role.getIdExternalMethod():"null"));
		
		/**
		 * Obtains the user id list from external method
		 */

		try {
			
			/**
			 * External method to obtain runtime user list to assign a role must return an Integer[] or List<Integer>
			 */
			Object objList = _executeExternalMethod(new Object[]{processWork}, role.getIdExternalMethod(), currentUserId);
			
			if (objList!=null) {
				_addRuntimeUserToRole(objList, role, processWork.getId(), currentUserId);
			}
			
		} catch (InstantiationException e) {
			/**
			 * continues to finish start process with warnings!!
			 */
			String mess="InstantiationException: error trying to execute WExternalMethod... runtime users must not be assigned!!! MUST CHECK THIS INJECTED PROCESS!!"
					+e.getMessage()+" "
					+(e.getCause()!=null?e.getCause():" ");
			logger.error(mess);
		} catch (IllegalAccessException e) {
			/**
			 * continues to finish start process with warnings!!
			 */
			String mess="InstantiationException: error trying to execute WExternalMethod... runtime users must not be assigned!!! MUST CHECK THIS INJECTED PROCESS!!"
					+e.getMessage()+" "
					+(e.getCause()!=null?e.getCause():" ");
			logger.error(mess);
		}
			
	
		
	}
	
	/**
	 * La lista de id de usuarios devuelta por el external method como un objeto debo procesarla
	 * y agregar la lista de id de usuarios a swaList para que se agreguen al step work como 
	 * usuarios en runtime para ese step work...
	 * 
	 * TODO: ESTOY ASUMIENDO QUE ME DEVUELVEN LA LISTA DE USUARIOS DE W CUANDO LO MAS PROBABLE
	 * ES QUE ME DEVUELVAN LA LISTA DE USUARIOS EXTERNOS, POR LO QUE HABRIA QUE CONVERTIR.
	 * (QUEDA PENDIENTE... PORQUE AHORA SON IGUALES LOS ID-USUARIO EXTERNOS DE LOS INTERNOS)
	 * 
	 * @param objList
	 * @param swaList
	 */
	private void _addRuntimeUserToRole(Object objList, WRoleDef role, Integer processWorkId, Integer currentUserId){
		logger.debug(">>> processReturnedIdUser");
		
		List<WUserRoleWork> urwList = new ArrayList<WUserRoleWork>();
		
		if (objList instanceof ArrayList<?> ) {

			ArrayList<?> al = (ArrayList<?>) objList;
			if (al.size() > 0) {
				// Iterate.
				for (int i = 0; i < al.size(); i++) {
					// Still not enough for a type.
					Object o = al.get(0);
					if (o instanceof Integer) {
						urwList.add(new WUserRoleWork(null,(Integer)o,role.getId(),processWorkId) );
					}
				}
			}
			
		} else if ( objList.getClass().equals(java.lang.Integer[].class) ) {
			for ( Integer idUser: (Integer[]) objList){
				urwList.add(new WUserRoleWork(null,idUser,role.getId(),processWorkId) );	
			}
		}
		
		if (urwList!=null && urwList.size()>0) {
			new WUserRoleWorkBL().add(urwList, currentUserId);
		}
	}
 
	/**
	 * Runtime users must be defined by an external method, or because the step refers to the
	 * system role 'originator' in which case we must assign the user at runtime...
	 * 
	 * Assign runtime users and send a notification if the method fails or can't obtain almost
	 * 1 assigned user ...
	 * 
	 * If can't obtain almost 1 user to assign the step, then assigns it to administrator user (role ...)
	 * 
	 * lastmod: nes 20141016
	 *
	 * @param stepWork
	 * @param idOriginatorUser
	 * @param idExternalMethod
	 * @param currentUserId
	 * @return
	 */
	private List<WStepWorkAssignment> _assignRuntimeUsers(
			WStepWork stepWork, Integer idOriginatorUser, Integer idExternalMethod, Integer currentUserId) {
		logger.debug("--->_assignRuntimeUsers");
		
		List<WStepWorkAssignment> swaList = new ArrayList<WStepWorkAssignment>();
		
		/**
		 * if wstepdef indicates originator must have access to this step... then add
		 * a WStepWorkAssignment with originator id as worker
		 */
		if (stepWork.isSysroleOriginator()) {
			swaList.add(createWStepWorkAssignmentObj(W_SYSROLE_ORIGINATOR_ID,idOriginatorUser));
		}
		
		/**
		 * executes external method ...
		 * External method to assign users or roles at runtime must return an object
		 * containing a list of IntegerPair
		 * 
		 * nes 20141213 - a esto aún no lo estamos utilizando...
		 */
		try {
			
			Object objList = _executeExternalMethod(new Object[]{stepWork}, idExternalMethod, currentUserId);// nes 20141215 - generalizado
			if (objList!=null) {
				_addExternalMethodReturnedUsersToList(objList,swaList);
			}
			
		} catch (Exception e) {
			logger.error("Error trying execute external method id:"
					+ (idExternalMethod!=null?idExternalMethod:"null")
					+" to assign RUNTIME USERS... "+e.getMessage()+" "+e.getCause());
			
		}

		if (swaList.size()<1) swaList=null;
		
		return swaList;
		
	}

	/**
	 * Crea un WStepWorkAssignment para el ID usuario indicado y para el 
	 * Rol indicado (que puede ser null ...
	 *
	 * @param idRole 
	 * @param idOriginatorUser
	 * @return
	 */
	private WStepWorkAssignment createWStepWorkAssignmentObj(
			Integer idRole,
			Integer idOriginatorUser) {
		WStepWorkAssignment assignedPerson = 
				new WStepWorkAssignment(idRole
							, idOriginatorUser
							, true // active
							, false // reassigned
							, null, null
							, false // from reassignment
							, null, null);
		return assignedPerson;
	}

	/**
	 * La lista de id de usuarios devuelta por el external method como un objeto debo procesarla
	 * y agregar la lista de id de usuarios a swaList para que se agreguen al step work como 
	 * usuarios en runtime para ese step work...
	 * 
	 * TODO: ESTOY ASUMIENDO QUE ME DEVUELVEN LA LISTA DE USUARIOS DE W CUANDO LO MAS PROBABLE
	 * ES QUE ME DEVUELVAN LA LISTA DE USUARIOS EXTERNOS, POR LO QUE HABRIA QUE CONVERTIR.
	 * (QUEDA PENDIENTE... PORQUE AHORA SON IGUALES LOS ID-USUARIO EXTERNOS DE LOS INTERNOS)
	 * 
	 * @param objList
	 * @param swaList
	 */
	private void _addExternalMethodReturnedUsersToList(Object objList, List<WStepWorkAssignment> swaList){
		logger.debug(">>> processReturnedIdUser");
		
		if (objList instanceof ArrayList<?> ) {

			ArrayList<?> al = (ArrayList<?>) objList;
			if (al.size() > 0) {
				// Iterate.
				for (int i = 0; i < al.size(); i++) {
					// Still not enough for a type.
					Object o = al.get(0);
					if (o instanceof Integer) {
						swaList.add(createWStepWorkAssignmentObj(W_SYSROLE_ORIGINATOR_ID,(Integer)o ) );
					}
				}
			}
			
		} else if ( objList.getClass().equals(java.lang.Integer[].class) ) {
			for ( Integer i: (Integer[]) objList){
				swaList.add(createWStepWorkAssignmentObj(W_SYSROLE_ORIGINATOR_ID,i ) );	
			}
		}
		
	}
	
	/**
	 * generates email notification for step with isEmailNotification enabled
	 * 
	 * @param newStep
	 * @param currentUserId
	 */
	private void _sendEmailNotification(WStepWork newStep, Integer currentUserId) {
		logger.debug(">>> _sendEmailNotification");
		
		if ( newStep.getCurrentStep().isEmailNotification()  ) {		
			try {
		
				this._sendArrivingStepEmailNotifications(newStep, currentUserId);
			
			} catch (SendEmailException e) {
				logger.error("SendEmailException: there is not possible to send email notifications to involved users...");
			} catch (Exception e) {
				logger.error("Exception: there is not possible to send email notifications to involved users..."
						+" "+e.getMessage()+" "+e.getCause()+" "+e.getClass());
			}
		}
		
	}
	

	/**
	 * Execute external GET  methods...
	 * 
	 * @param Object[] objList
	 * @param methodId
	 * @param currentUserId
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private Object _executeExternalMethod(
			Object[] objList, Integer methodId, Integer currentUserId) // nes 20141215
					throws InstantiationException, IllegalAccessException {
		logger.debug(">>>_executeExternalMethod id:"+(methodId!=null?methodId:"null"));
		
		if (methodId==null || methodId==0){
			logger.warn("_executeExternalMethod: no method id arrives!!!");
			return null;
		}
		
		Object result=null;
		WExternalMethod method;
		try {
			method = new WExternalMethodBL().getExternalMethodByPK(methodId);
			if (method!=null){
				if (method.getParamlistName().length>0){
					_setParamValues(method, objList, currentUserId); // nes 20141215 - generalizado setParamValues
				}
				result = new MethodSynchronizerImpl().invokeExternalMethod(method, null,currentUserId);
			}			
		} catch (WExternalMethodException e) {
			String mess = "Error trying to get external method definition for methodId:"
							+(methodId!=null?methodId:"null")
							+ e.getMessage()+" - "+e.getClass();
			logger.error(mess);
		}

		return result;
	}
	
	/**
	 * Execute external method associated with a route ...
	 * Route related methods will be executed when navigates the route ...
	 * 
	 * Nota respecto al its: 775 - nes 20150116: agregué el wProcessWork que está
	 * adentro del wStepWork a la lista de objetos pues el problema era que no encontraba
	 * este dato con arrayUtils ( PropertyUtils.isReadable(o, paramName) )
	 * Lo que no entiendo es como en las pruebas funcionó y al principio funcionó correctamente.
	 * Se me ocurren dos posibles alternativas: 
	 * 1) pasaba el parámetro indicado y perdí la version...
	 * 2) cambiamos la version de common-beanUtils
	 * 
	 * @param route
	 * @param currentStepWork
	 * @param currentUser
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private void _executeRouteExternalMethodSafe(
			WStepSequenceDef route, WStepWork currentStepWork, Integer currentUserId) {
		logger.debug(">>>_executeRouteExternalMethod check...");

		try {
			if (route.getExternalMethod()!=null && route.getExternalMethod().size()>0){
				for (WExternalMethod method: route.getExternalMethod()) {
					logger.debug(">>>_executeRouteExternalMethod:"+method.getClassname()+"."+method.getMethodname());
					if (method.getParamlistName().length>0){
						_setParamValues(
								method,
								new Object[]{
											currentStepWork,
											currentStepWork.getwProcessWork(), // nes 20150116 - its:775 - no encontraba idObject pues estaba en wProcessWork
											route}, currentUserId);
					}
					new MethodSynchronizerImpl().invokeExternalMethod(method, currentUserId);
				}
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // nes 20141215 - generalizado setParamValues

	}
	
	/**
	 * Load current value for each required parameter in method.paramlistName ...
	 * ESTO HAY QUE REPENSARLO PORQUE PARTIMOS DE LA BASE QUE NO SABEMOS QUE PARAMETROS
	 * SE INDICAN EN EL EXTERNAL METHOD DEFINICION (LO MAS NORMAL SERIA OBJECT ID Y COSAS ASI
	 * QUE SON GENERICAS PERO NO ES OBLIGATORIO Y LUEGO EL TEMA ES COMO HACEMOS PARA PODER
	 * DEFINIR DATOS A NIVEL DE EXTERNAL METHOD Y COMO HACEMOS PARA PASAR OBJETOS A CIEGAS
	 * PARA BUSCAR ESOS DATOS...
	 * 
	 * @param method
	 * @param objList[] - list of objects to search paramName defined in externalMethod
	 * @param currentUserId
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private void _setParamValues(
			WExternalMethod method, //WStepWork currentStepWork, WStepSequenceDef route,
			Object[] objList, // nes 20141215 - generalizado
			Integer currentUserId) throws InstantiationException, IllegalAccessException {
		/**
		 * set array limit at qty parameters defined for external method.
		 */
		int tope=method.getParamlistName().length;
		/**
		 * object[] to load paramlist
		 */
		Object[] paramValueObj = new Object[tope];
		for (int i=0;i<tope;i++) {
			String paramName=method.getParamlistName()[i];
			Class paramType = method.getParamlistType()[i];
			Object obj = new Object(); //paramType.newInstance();
			if (paramName.equals("idCurrentUser")) {
				paramValueObj[i]=currentUserId;
			} else {
				obj = _setPropertyValue(objList, paramName);
				paramValueObj[i]=obj;
			}
		}
		
		method.setParamlist(paramValueObj);
		
	}

	/**
	 * Set property value for for ....
	 * search paramName in the current scoped objects: currentStepWork -> currentProcessWork -> route
	 * and if exists load the value in paramlist as an Object
	 * 
	 * There will be interesting to generalize this method to work with introspection and annotations 
	 * like the search for paramName will be in all the passed object and not in concrete stepWork or route....
	 * 
	 * @param mainObjList - object list to find/obtain paramName
	 * @param paramName - the name of parameter must be passed to an exernal method
	 */
	private Object _setPropertyValue(Object[] mainObjList, String paramName) {
		Object obj=null;
		// find field (paramName) in currentStepWork / currentProcessWork and route 
		try {
			
			for (Object o: mainObjList) {//o = ((WStepWork)o).getwProcessWork();
				if (PropertyUtils.isReadable(o, paramName)) {
					/**
					 * try to get property in 2 ways: getNestedProperty and getIndexedProperty
					 * if both two fails the catch error ... 
					 * nes 20141215
					 */
					try {
						obj = PropertyUtils.getNestedProperty(o, paramName);
					} catch (Exception e) {
						obj = PropertyUtils.getIndexedProperty(o, paramName);						
					}
					
				}
			}

		} catch (IllegalAccessException e) {
			String mess = "WStepWorkBL:_setParamValues IllegalAccessException - error PropertyUtils getting property name:"
					+paramName + " "
					+e.getMessage()+" "
					+(e.getCause()!=null?e.getCause():"null");
			obj=null;
			logger.error(mess);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			String mess = "WStepWorkBL:_setParamValues InvocationTargetException - error PropertyUtils getting property name:"
					+paramName + " "
					+e.getMessage()+" "
					+(e.getCause()!=null?e.getCause():"null");
			obj=null;
			logger.error(mess);
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			String mess = "WStepWorkBL:_setParamValues NoSuchMethodException - error PropertyUtils getting property name:"
					+paramName + " "
					+e.getMessage()+" "
					+(e.getCause()!=null?e.getCause():"null");
			obj=null;
			logger.error(mess);
			e.printStackTrace();
		} catch (Exception e) {
			String mess = "WStepWorkBL:_setParamValues Exception - error PropertyUtils getting property name:"
					+paramName + " "
					+e.getMessage()+" "
					+(e.getCause()!=null?e.getCause():"null");
			obj=null;
			logger.error(mess);
			e.printStackTrace();

		}
		return obj;
	}
	
	private void _executeTurnBack (
			WRuntimeSettings runtimeSettings, Integer currentUser,  WStepWork currentStepWork, 
			Integer idResponse, boolean isAdminProcess, DateTime now ) 
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
			WStepWork stepWork, DateTime now) {
		
		stepWork.setPreviousStep(null); // como no viene de ningún lado el previous es null
		stepWork.setCurrentStep(process.getBeginStep());
		
		stepWork.setArrivingDate(new DateTime());
		
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
			WRuntimeSettings runtimeSettings, Integer currentUser, DateTime now, 
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
		if ( runtimeSettings!=null && runtimeSettings.getInstructions() != null ) {
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
	

	/**
	 * send arriving step notifications to the given stepWork
	 * @param stepWork
	 * @param currentUserId
	 * @throws SendEmailException
	 */
	private void _sendArrivingStepEmailNotifications( 
			WStepWork stepWork, Integer currentUserId ) throws SendEmailException {
		logger.debug(">>> _sendArrivingStepEmailNotifications init");
		
		if ( !stepWork.getCurrentStep().isArrivingAdminNotice() 
				&& !stepWork.getCurrentStep().isArrivingUserNotice()) {
			return; 
		}

		WProcessDefThin process = stepWork.getwProcessWork().getProcessDef();// nes 20130830
		
		if ( process == null ) {
			logger.error("_sendArrivingStepEmailNotifications: process is not valid");
			return;
		}
		
		/**
		 * builds email object with process.getSystemEmailAccount() as email
		 * sender account ...
		 */
		Email emailMessage = this._buildEmail(process.getSystemEmailAccount());
		logger.debug(">> email account is loaded ...");
		
		// creates the list with the object must use to replace the email subject and body
		List<Object> stepWorkObjectAsList = new ArrayList<Object>();
		stepWorkObjectAsList.add(stepWork);
		
		ApplicationURLUtil objParams = this._buildStepWorkProcessorUrl(stepWork); // dml 20151124 - refactorizado...
		if (objParams != null){
			stepWorkObjectAsList.add(objParams);
		}
		
		if ( stepWork.getCurrentStep().isArrivingAdminNotice() ) {
		
			if ( process.getArrivingAdminNoticeTemplate() != null 
					&& process.getArrivingAdminNoticeTemplate().getId() != null) { // nes 20141124
		
				logger.debug(">>> _sendArrivingStepEmailNotifications: ArrivingAdminNoticeTemplate id: "+
						process.getArrivingAdminNoticeTemplate().getId() );
			
				// "subject" personalization
				String subject = EmailPersonalizationUtilBL.personalizeEmailPart(
						process.getArrivingAdminNoticeTemplate().getSubject(), 
						stepWorkObjectAsList);

				emailMessage.setSubject(subject);
				
				// "body" personalization
				String body = EmailPersonalizationUtilBL.personalizeEmailPart(
						process.getArrivingAdminNoticeTemplate().getHtmlTemplate(), 
						stepWorkObjectAsList);

				emailMessage.setHtmlFormatted(true);
				emailMessage.setBodyHtml(body);
				
				//ESTE MÉTODO PRIVADO SERÁ EL ENCARGADO DE OBTENER TODAS LAS CUENTAS DE EMAIL SIN REPETIR
				//EL SEGUNDO ATRIBUTO INDICA SI LAS CUENTAS SERÁN DE ADMINISTRADORES (true) O DE USUARIOS NORMALES (false)
				emailMessage.setListaTo(this.getEmailAccountList(stepWork, true));
				
				this._sendEmail(emailMessage, currentUserId);
				
			} else {
				logger.error("There is no email template associated to process in order to send email to tracker");
			}
		}
		
		
		
		if ( stepWork.getCurrentStep().isArrivingUserNotice() ) {
			// avisar a los usuarios y roles definidos
			
			if (process != null 
					&& process.getArrivingUserNoticeTemplate() != null) {
			
				logger.debug("_sendArrivingStepEmailNotifications: ArrivingUserNoticeTemplate ... "  ); // nes 20141127

				// Personalizamos el "subject"
				String subject = EmailPersonalizationUtilBL.personalizeEmailPart(
						process.getArrivingUserNoticeTemplate().getSubject(), 
						stepWorkObjectAsList);

				emailMessage.setSubject(subject);
				
				// Personalizamos el "body" (aquí se usa el txt, o por lo menos es el que usabamos hasta ahora)
				String body = EmailPersonalizationUtilBL.personalizeEmailPart(
						process.getArrivingUserNoticeTemplate().getHtmlTemplate(), 
						stepWorkObjectAsList);

				emailMessage.setHtmlFormatted(true);
				emailMessage.setBodyHtml(body);

				//IGUAL QUE CON LOS ADMINISTRADORES PERO CON EL INDICADOR A FALSE 
				emailMessage.setListaTo(this.getEmailAccountList(stepWork, false));
				
				logger.debug("email to list:"+(emailMessage!=null && emailMessage.getListaTo()!=null?emailMessage.getListaTo().toString():"VACIO")); // nes 20151124
				
				this._sendEmail(emailMessage, currentUserId);

			} else {
				logger.error("There is no email template associated to process in order to send email to worker");
			}

		}
		
		logger.debug(">>> _sendArrivingStepEmailNotifications finished...");
			
	}
	
	/**
	 * Algorithm that builds the WorkStep URL (inside the ApplicationURLUtil) in order to process itself
	 * 
	 * @author dmuleiro 20141128
	 * 
	 * @param stepWork
	 * @return
	 */
	private ApplicationURLUtil _buildStepWorkProcessorUrl(WStepWork stepWork){ // dml 20151124 - refactorizado...
		
		try {
		
			String stepWorkUrl = null;
			
			/**
			 *  First checks for specific step default processor is defined. ('then' part)
			 *  If not then checks for general process default processor (first 'else').
			 *  If there is not step work processor url we return null value (second 'else')
			 */
			if (stepWork.getCurrentStep().getIdDefaultProcessor() != null
					&& !"".equals(stepWork.getCurrentStep().getIdDefaultProcessor())){
				
				stepWorkUrl = stepWork.getCurrentStep().getIdDefaultProcessor();
				
			} else if (stepWork.getwProcessWork().getProcessDef().getProcessorStepId() != null
					&& !"".equals(stepWork.getwProcessWork().getProcessDef().getProcessorStepId())){
				
				stepWorkUrl = stepWork.getwProcessWork().getProcessDef().getProcessorStepId();
				
			} else {
				
				logger.info("The step work with id: " + stepWork.getId() + " have not a valid URL to process itself!");
				return null;
				
			}
			
			/**
			 *  Second step: we take the parameter data to identify this specific step work
			 */
			if (stepWork.getUrlData() != null
					&& !"".equals(stepWork.getUrlData())){
				stepWorkUrl += stepWork.getUrlData();
			} 
			
			/**
			 * Third step: We replace the parameters
			 */
			stepWorkUrl = stepWorkUrl.replace("[idStepWork]", stepWork.getId().toString());
			stepWorkUrl = stepWorkUrl.replace("[idObject]", stepWork.getwProcessWork().getIdObject().toString());
			stepWorkUrl = stepWorkUrl.replace("[idObjectType]", stepWork.getwProcessWork().getIdObjectType());
			
			/**
			 *  Fourth step: We build the final URL with the "process step id" and the "step work url data"
			 */
			return new ApplicationURLUtil(stepWorkUrl); // dml 20151124 - refactorizado...

		} catch (Exception e){
			logger.info("The step's work (with id: " + stepWork.getId()
					+ ") URL sustitution has fail. The step work {publicUrl} will not be set!");
			return null;
		}
			
	}
	
	/**
	 * Este método envia el email o lo escribe en filesystem según tenga el parámetro WRITE_EMAIL_TO_FILESYSTEM
	 * cargado (PARA TESTING)
	 * 
	 * @param email
	 * @param currentUserId
	 * @throws SendEmailException
	 */
	private void _sendEmail(Email email, Integer currentUserId) throws SendEmailException {
		logger.debug(">>> _sendEmail email:"+(email!=null?email:"null")
				+" currentUserId:"+(currentUserId!=null?currentUserId:"null"));
		
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
	
	/**
	 * creates Email object and load with UserEmailAccount, user and password 
	 * dml 20120307 - nes 20141127
	 * 
	 * @param senderEmailAccount
	 * @return
	 * @throws SendEmailException
	 */
	private Email _buildEmail(WEmailAccount senderEmailAccount) throws SendEmailException {
		logger.debug(">>> _buildEmail senderEmailAccount:"
				+(senderEmailAccount!=null?senderEmailAccount.getEmail():"null"));

		Email emailMessage = new Email();
		
		try {
			
			// nes 20141125
			UserEmailAccount uea = new UserEmailAccount(senderEmailAccount.getwUserDef().getId(), 
					senderEmailAccount.getName(), 
					senderEmailAccount.isUserDefaultAccount(), senderEmailAccount.getEmail(), 
					senderEmailAccount.getReplyTo(), senderEmailAccount.getSignatureTxt(), 
					senderEmailAccount.getSignatureText(),  senderEmailAccount.getSignatureHtml(),  
					senderEmailAccount.getSignatureFile(), 
					senderEmailAccount.getInputServerType(), senderEmailAccount.getInputServerName(), 
					senderEmailAccount.getInputPort(), 
					senderEmailAccount.getConnectionSecurity(), senderEmailAccount.getIdentificationMethod(), 
					//senderEmailAccount.getFormat(), 
					senderEmailAccount.getOutputServerName(), senderEmailAccount.getOutputServer(), 
					senderEmailAccount.getOutputPort(), 
					senderEmailAccount.getOutputConnectionSecurity(), senderEmailAccount.getOutputIdentificationMethod(), 
					senderEmailAccount.getOutputUserName(), senderEmailAccount.getOutputPassword(), 
					senderEmailAccount.getIdExchange());
			
			logger.debug("_buildEmail: Sender email account--> "+uea.toString());
			
			emailMessage.setFrom(senderEmailAccount.getEmail());
			// dml 20140313 BORRAR emailMessage.setIdFrom(senderEmailAccount.getId());
			
			emailMessage.setFromUserEmailAccount(uea); // nes 20141125
			
			emailMessage.setPwd(senderEmailAccount.getOutputPassword());

		} catch (Exception e) {
			String mess = "Exception: _buildEmail "+e.getMessage()+" "+e.getCause()+" "+e.getClass();
			logger.error(mess);
			throw new SendEmailException(mess);
		}
		
		return emailMessage;
		
	}
	
/* dml 20141126 BORRAR: PASAMOS A USAR EL MÉTODO 'personalizeEmailPart' DE EmailPersonalizationUtilBL	
	// dml 20120315
	private String _buildTemplate(String template, WStepWork wsw){
		logger.debug(">>> _buildTemplate TEMPLATE ANTES DE SUSTITUIR:"
							+(template!=null?template:"null"));
		
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
		
		logger.debug(">>> _buildTemplate TEMPLATE DESPUES DE SUSTITUIR:"
				+(template!=null?template:"null"));
		
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
*/	
	
	private void _buildFile(Email email) throws IOException{
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_hh_mm_ss");
		
		String path = Resourceutil.getStringProperty("email.path.server", "");
		
		path += "notificationEmail"+sdf.format(new DateTime())+".txt"; 
		
		File f = new File(path);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
		logger.info(">>>>>>> email will be writing in: "+f.getAbsolutePath() );
		
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
		
		logger.info(" - Email NO enviado, creado en la ruta: " + path);
		
		
	}
	
	/**
	 * Return a string with email list of users related with given stepWork
	 * 
	 * @param stepWork
	 * @param isAdmin
	 * @return
	 */
	public ArrayList<String> getEmailAccountList(WStepWork stepWork, boolean isAdmin){
		
		ArrayList<String> emailList = new ArrayList<String>();
		
		if (stepWork.getCurrentStep() != null){
			
			getUsersRelatedEmailList(stepWork, isAdmin, emailList);

			getRoleRelatedEmailList(stepWork, isAdmin, emailList);	
		}
		
		return emailList;
		
	}

	/**
	 * Return an email list with users belonging roles assigned to a stepWork
	 * 
	 * @param stepWork
	 * @param isAdmin
	 * @param emailList
	 */
	private void getRoleRelatedEmailList(WStepWork stepWork, boolean isAdmin,
			ArrayList<String> emailList) {
		if (stepWork.getCurrentStep().getRolesRelated() != null
				&& stepWork.getCurrentStep().getRolesRelated().size() > 0) {
			
			for (WStepRole wsr : stepWork.getCurrentStep().getRolesRelated()) {
				
				if (isAdmin != wsr.isAdmin()) {
					continue;
				}
				
				// nes 20141224 - added email to runtime role users
				if ( wsr.getRole().isRuntimeRole() ) {
					
					for ( WUserDef user : this._getRuntimeRoleUsers(wsr, stepWork.getwProcessWork().getId()) ) {
						
						if (this._checkForRepeatedEmailAdress(user, emailList)){

							emailList.add(user.getEmail());
								
						}					
					}					
					
				} else {
					for (WUserDef user : this._getRoleUsers(wsr)) {
						
						if (this._checkForRepeatedEmailAdress(user, emailList)){

							emailList.add(user.getEmail());
								
						}					
					}
				}
			}
		}
	}

	/**
	 * Return an email list with users assigned to a stepWork
	 * 
	 * @param stepWork
	 * @param isAdmin
	 * @param emailList
	 */
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
	
	/**
	 * Returns a WUserDef list with users with permissions for given Role
	 * dml 20120307
	 * 
	 * @param stepRole
	 * @return
	 */
	private List<WUserDef> _getRoleUsers(WStepRole stepRole){
		
		List<WUserDef> roleUsers = new ArrayList<WUserDef>();
		
		if (stepRole != null
				&& stepRole.getRole() != null
				&& stepRole.getRole().getId() != null) {
			
			try {
					
				roleUsers = new WUserRoleBL().getUserDefListByRole(stepRole.getRole().getId(), null);
				
			} catch (WUserRoleException e) {
				String mess = "WUserRoleException: Error retrieving user def list for a role: "
						+e.getMessage()+" "
						+(e.getCause()!=null?e.getCause():" ");
				logger.error(mess);
			}
		}
		
		return roleUsers;
		
	}
	
	/**
	 * Returns a WUserDef list with users with permissions for given RuntimeRole
	 * dml 20120307
	 * 
	 * @param stepRole
	 * @param idProcessWork - runtime roles has users assigned for each step work ...
	 * @return
	 */
	private List<WUserDef> _getRuntimeRoleUsers(WStepRole stepRole, Integer idProcessWork){
		
		List<WUserDef> roleUsers = new ArrayList<WUserDef>();
		
		if (stepRole != null
				&& stepRole.getRole() != null
				&& stepRole.getRole().getId() != null) {
			
			try {
				
				roleUsers = new WUserRoleWorkBL().getUserDefListByRole(stepRole.getRole().getId(), idProcessWork, null);
				
			} catch (WUserRoleWorkException e) {
				String mess = "WUserRoleWorkException: Error retrieving user def list for a runtime role: "
						+e.getMessage()+" "
						+(e.getCause()!=null?e.getCause():" ");
				logger.error(mess);
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


	/**
	 * revisar que el paso no esté ya resuelto y que si trae un objeto relacionado
	 * es diferene de null, que sea el mismo que el que está persistido (revisar bien esto - nes 20151108)
	 * 
	 * @param idStepWork
	 * @param currentUser
	 * @param idObject
	 * @param idObjectType
	 * @param storedStep
	 * @throws WStepWorkException
	 * @throws WStepLockedByAnotherUserException
	 */
	private void _checkPreLockStepWork(
			Integer idStepWork, Integer currentUser, Integer idObject, String idObjectType, WStepWork storedStep ) 
	throws WStepWorkException, WStepLockedByAnotherUserException {

		// los dos primeros controles pueden ser reduntantes pero pienso que obligan
		// al implementador a mandar además del step el id del objeto que está modificando
		// como para que no la lie y mande otra cosa y piense que procesó algo que en
		// realidad no era ...
		
		// nota nes 20151108 - aquí puede venir tanto un workitem que tenga un objeto/clase referenciado
		// como uno que no lo tenga, por lo que no debería ser obligatorio que venga objectId  objectClassId
		
//		// Si el objeto que me indican es nulo devuelvo
//		if ( idObject== null || idObject==0 || idObjectType==null ) {
//			String message = "The indicated object is null or 0 (idObject:["
//								+ idObject  +"] - type:[" + idObjectType
//								+ "])";
//
//			logger.debug(message);
//			throw new WStepWorkException(message);
//		}
		
		if ( idObject != null && idObject==0 && idObjectType != null ) {

			// Si no pertenecen al mismo objeto me vuelvo ...
			if ( storedStep.getwProcessWork().getIdObject()!=idObject ) {
				String message = "The indicated step have a diferent object than indicated ("
									+ idObject  +" - " + idObjectType
									+ ")";
				logger.debug(message);
				throw new WStepWorkException(message);
			}
			
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
	

	/**
	 * set a stepWork to processed and persist it
	 * @param currentStep
	 * @param idResponse
	 * @param now
	 * @param currentUserId
	 * @throws WStepWorkException
	 */
	private void _setCurrentWorkitemToProcessed( WStepWork currentStep, WRuntimeSettings runtimeSettings,
			Integer idResponse,	DateTime now, Integer currentUserId) throws WStepWorkException {
		logger.debug(">>> _setCurrentWorkitemToProcessed currentStep id:"
			+(currentStep!=null && currentStep.getId()!=null?currentStep.getId():"null")); // nes 20141125
		
		currentStep.setDecidedDate( now );
		currentStep.setPerformer(new WUserDef(currentUserId));// nes 20120126
		currentStep.setLocked(false);
		currentStep.setLockedBy(null);
		currentStep.setLockedSince(null);
		// nes 20151104 - set my notes on current step to persist...
		if (runtimeSettings.getStepNotes()!=null && !"".equals(runtimeSettings.getStepNotes())){
			currentStep.setMyNotes(true);
			currentStep.setUserNotes(runtimeSettings.getStepNotes());
		}
		
		logger.debug(">>> _setCurrentWorkitemToProcessed 1...");
		
		// nes 20151018 - refactorizada response de String a WStepResponseDef
		if ( idResponse != null && idResponse != 0 ) { // nes 20121222
			currentStep.setResponse(new WStepResponseDef(idResponse));
//		} else {
//			currentStep.setResponse("no responses list");
		}
		
		logger.debug(">>> _setCurrentWorkitemToProcessed 2...");
		
		new WStepWorkBL().update(currentStep, currentUserId); // actualiza el paso actual a "ejecutado" y lo desbloquea
		logger.debug(">>> wStepWork updated...");
		
	}
	
	
	//rrl 20101216
	// recupera los workitems activos de 1 proceso (mapa)
	// llegó el: es una fecha
	// revisado el: es una fecha
	// fecha limite: es una fecha
	// el status puede ser: null=todos, A=vivos P=Procesados
	// el usuario que lo solicita
	public List<WStepWork> getStepListByProcessName (
			Integer idProcess,	LocalDate arrivingDate, LocalDate openedDate, LocalDate deadlineDate, String status, Integer currentUser) 
	throws WProcessDefException, WStepDefException, WStepWorkException {
		
		return new WStepWorkDao().getStepListByProcessName(idProcess, arrivingDate, openedDate, deadlineDate, status, currentUser);
	}
	
	/**
	 * Finder for stepWork 
	 * 
	 * @param processIdFilter - process def id
	 * @param stepIdFilter - step def id
	 * @param stepWorkProcessingStatusFilter
	 * @param referenceFilter
	 * @param idWorkFilter
	 * @param initialArrivingDateFilter
	 * @param finalArrivingDateFilter
	 * @param estrictArrivingDateFilter
	 * @param initialOpenedDateFilter
	 * @param finalOpenedDateFilter
	 * @param estrictOpenedDateFilter
	 * @param initialDeadlineDateFilter
	 * @param finalDeadlineDateFilter
	 * @param estrictDeadlineDateFilter
	 * @param initialDecidedDateFilter
	 * @param finalDecidedDateFilter
	 * @param estrictDecidedDateFilter
	 * @param action
	 * @param onlyActiveProcessDefFilter
	 * @param maxResults
	 * @return
	 * @throws WStepWorkException
	 */
	public List<StepWorkLight> finderStepWork(Integer processIdFilter, 
			Integer stepIdFilter, StepWorkStatus stepWorkProcessingStatusFilter, String referenceFilter, Integer idWorkFilter, 
			LocalDate initialArrivingDateFilter, LocalDate finalArrivingDateFilter, boolean estrictArrivingDateFilter,  		
			LocalDate initialOpenedDateFilter, LocalDate finalOpenedDateFilter, boolean estrictOpenedDateFilter, 		
			LocalDate initialDeadlineDateFilter, LocalDate finalDeadlineDateFilter, boolean estrictDeadlineDateFilter, 		
			LocalDate initialDecidedDateFilter, LocalDate finalDecidedDateFilter, boolean estrictDecidedDateFilter, 		
			String action, boolean onlyActiveProcessDefFilter,
			Integer maxResults) //rrl 20151102 ITS:1334
	throws WStepWorkException {

		return new WStepWorkDao().finderStepWork(processIdFilter, stepIdFilter, 
				stepWorkProcessingStatusFilter, referenceFilter, idWorkFilter, initialArrivingDateFilter, 
				finalArrivingDateFilter, estrictArrivingDateFilter, 
				initialOpenedDateFilter, finalOpenedDateFilter, estrictOpenedDateFilter,
				initialDeadlineDateFilter, finalDeadlineDateFilter, estrictDeadlineDateFilter, 
				initialDecidedDateFilter, finalDecidedDateFilter, estrictDecidedDateFilter, 
				action, onlyActiveProcessDefFilter, maxResults);
		
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
/*	@Deprecated
	public List<StepWorkLight> finderStepWorkVIEJO(Integer processIdFilter, 
			Integer stepIdFilter, String stepTypeFilter, String referenceFilter, Integer idWorkFilter, 
			DateTime initialArrivingDateFilter, DateTime finalArrivingDateFilter, boolean estrictArrivingDateFilter,  		
			DateTime initialOpenedDateFilter, DateTime finalOpenedDateFilter, boolean estrictOpenedDateFilter, 		
			DateTime initialDeadlineDateFilter, DateTime finalDeadlineDateFilter, boolean estrictDeadlineDateFilter, 		
			DateTime initialDecidedDateFilter, DateTime finalDecidedDateFilter, boolean estrictDecidedDateFilter, 		
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
*/
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