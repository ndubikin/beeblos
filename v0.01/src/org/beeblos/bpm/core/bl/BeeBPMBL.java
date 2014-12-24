package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.AlreadyExistsRunningProcessException;
import org.beeblos.bpm.core.error.InjectorException;
import org.beeblos.bpm.core.error.WProcessDataFieldException;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.enumerations.ProcessDataFieldStatus;
import org.beeblos.bpm.tm.exception.TableManagerException;
import org.joda.time.DateTime;


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
	
	WProcessDef selectedProcess;
	WStepDef selectedStepDef;


	public BeeBPMBL() {
		super();
	}


	/**
	 * starts a workflow process for given idObject/idObjectType.
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
		selectedProcess = _loadProcessDef(idProcess, userId);
		
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
		selectedStepDef = _loadStepDef(idStep, userId);
		
		// create processWork obj
		WProcessWork processWork = _setProcessWork(idProcess,  idStep, idObject,  idObjectType, objReference,  objComments,  userId);
		
		// create stepWork obj
		WStepWork stepWork = _setStepWork(processWork,userId); // nes 20140707 quito managed data de aqui... , managedData);
		
//		_synchronizeManagedDataFields(processWork,stepWork);

		//idStepWork = new WStepWorkBL().add(_setStepWork(), usuarioLogueado) ;
		idStepWork = new WStepWorkBL()
							.start( processWork, stepWork, userId) ;

		return idStepWork;
		
	}


	/**
	 * load process def to launch
	 * 
	 * @param idProcess
	 * @param userId
	 * @return
	 * @throws InjectorException
	 */
	private WProcessDef _loadProcessDef(Integer idProcess, Integer userId) throws InjectorException {
		try {
			return new WProcessDefBL().getWProcessDefByPK(idProcess, userId);
		} catch (WProcessDefException e) {
			String mess="WProcessDefException: BeeBPMBL: inyecting work: can't load process def for id:"
							+idProcess+" "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new InjectorException(mess);
		} catch (WStepSequenceDefException e) {
			String mess="WProcessDefException: BeeBPMBL: inyecting work: can't load process def for id:"
							+idProcess+" "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			throw new InjectorException(mess);
		}
	}
	

	private void _checkConsistencyBeforeStepLaunch(
			Integer idProcess, Integer idStep, 
			Integer idObject, String idObjectType,
			String objReference, String objComments, Integer userId) 
					throws InjectorException {
		
		if (idProcess==null || idProcess==0) {
			
			String mensaje = "You must select a process ....\n";
			mensaje +=" Id:"+idObject+" ref:"+objReference; // NOTA REVISAR ESTOS objIdUsuario PORQUE LOS ESTABA USANDO MAL ... NES 20111216
			logger.info("inyectar:"+mensaje);
			throw new InjectorException(mensaje);
			
		}
		
		if (idStep==null || idStep==0) {
			
			String mensaje = "You must select a step into process will be launched....\n";
			mensaje +=" Id:"+idObject+" ref:"+objReference;
			logger.info("inyectar:"+mensaje);
			throw new InjectorException(mensaje);
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
	
	private WStepDef _loadStepDef(
			Integer idStep,	Integer userId) throws InjectorException {
		
		try {

			return new WStepDefBL()
							.getWStepDefByPK(
									idStep, selectedProcess.getProcess().getId(), userId);// nes 20130808 por agregado de filtro para step-data-field

		} catch (WStepDefException e) {

			String mensaje = "No se puede definir el paso indicado ....\n";
			mensaje +="error:"+e.getMessage()+" - "+ e.getCause();
			logger.info("inyectar: _definicionObjetosDelEntorno: "+mensaje);
			throw new InjectorException( mensaje );
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
		
		stepToBeInjected.setManagedData(null); // NES 20140707 - NO VEO QUE HARIAMOS CON ESTO AQUI ... QUITO MD ...> md);// nes 20130830
		
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
