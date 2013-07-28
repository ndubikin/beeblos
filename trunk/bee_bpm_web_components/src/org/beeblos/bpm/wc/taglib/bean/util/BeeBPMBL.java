package org.beeblos.bpm.wc.taglib.bean.util;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepWorkBL;
import org.beeblos.bpm.core.error.AlreadyExistsRunningProcessException;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.wc.security.error.InyectorException;

/*
 * 
 * nes 20110124 - completé los mensajes de error ...
 * 
 */
public class BeeBPMBL {

	private static final Log logger = LogFactory.getLog(BeeBPMBL.class);
	
	WProcessDef procesoSeleccionado;
	WStepDef pasoSeleccionado;


	public BeeBPMBL() {
		super();
	}

	// si idStep viene vacio implica que hace un start por lo que hay que leer el WProcessDef y su beginStep
	public Integer inyectar(
			Integer idProcess, Integer idStep, 
			Integer idObject, String idObjectType,
			String objReference, String objComments, Integer userId) 
					throws InyectorException, AlreadyExistsRunningProcessException, 
							WStepWorkException, WProcessWorkException {
		
		Integer idStepWork=null;
		
		_controlConsistenciaStepALanzar(
							idProcess, idStep, idObject, idObjectType,
							objReference, objComments, userId); // controla que los elementos necesarios vengan cargados y si no sale por InyectorException
		
		_controlExistenciaPasosVivos(
							idProcess, idStep, idObject, idObjectType,
							objReference, objComments, userId); // controla que no exista ya 1 workflow para el proceso indicado y el objeto indicado
		
		_definicionObjetosDelEntorno(idProcess,  idStep, idObject,  idObjectType,  userId);
		
		//idStepWork = new WStepWorkBL().add(_setStepWork(), usuarioLogueado) ;
		idStepWork = new WStepWorkBL()
							.start(
									_setProcessWork(idProcess,  idStep, idObject,  idObjectType, objReference,  objComments,  userId), 
									_setStepWork(null,userId), 
									userId) ;

		return idStepWork;
		
	}
	

	private void _controlConsistenciaStepALanzar(
			Integer idProcess, Integer idStep, 
			Integer idObject, String idObjectType,
			String objReference, String objComments, Integer userId) 
					throws InyectorException {
		
		if (idProcess==null || idProcess==0) {
			
			String mensaje = "Debe elegir un proceso de la lista ....\n";
			mensaje +=" Id:"+idObject+" ref:"+objReference; // NOTA REVISAR ESTOS objIdUsuario PORQUE LOS ESTABA USANDO MAL ... NES 20111216
			logger.info("inyectar:"+mensaje);
			throw new InyectorException(mensaje);
			
		}
		
		if (idStep==null || idStep==0) {
			
			String mensaje = "Debe elegir un paso de la lista para insertar el elemento en el workflow....\n";
			mensaje +=" Id:"+idObject+" ref:"+objReference;
			logger.info("inyectar:"+mensaje);
			throw new InyectorException(mensaje);
		}
	// NESTOR ARREGLAR ESTO LUEGO CUANDO TENGAMOS EL CONTROL DE OBJETOS EN EL WPROCESS-DEF	
//		if (idObject==null || idObject==0 ||
//				idObjectType==null || "".equals(idObjectType)) {
//			
//			String mensaje = "El elemento a insertar en el workflow no está definido ( idObject, idObjectType ) ....\n";
//			mensaje +=" Id:"+idObject+" ref:"+objReference;
//			logger.info("inyectar:"+mensaje);
//			throw new InyectorException(mensaje);
//		}
	}
	
	private void _controlExistenciaPasosVivos(
			Integer idProcess, Integer idStep, 
			Integer idObject, String idObjectType,
			String objReference, String objComments, Integer userId) 
	throws AlreadyExistsRunningProcessException, WStepWorkException {
		
		// si no hay referencias a objetos "base" no es necesario controlar si hay pasos vivos 
		// porque no hay contra que hacerlo ...
		if (idObject==null || idObject==0 ||
				idObjectType==null || "".equals(idObjectType)) {
			return;
		}
		
		try {
			if ( new WStepWorkBL().existsActiveProcess(idProcess, idObject, idObjectType, userId)) {
				
				String mensaje = "Ya existe un wofkflow en ejecución para este elemento ....\n";
				logger.info("inyectar: "+mensaje);
				throw new AlreadyExistsRunningProcessException(mensaje);
			}
		} catch (WStepWorkException e) {
			String mensaje = "El sistema arrojó un error al intentar verificar si existen procesos activos para este objeto ....\n";
			logger.error(mensaje);
			throw new WStepWorkException(mensaje);
		}
		
	}
	
	private void _definicionObjetosDelEntorno(
			Integer idProcess, Integer idStep, 
			Integer idObject, String idObjectType, Integer userId) throws InyectorException {
		
		try {
			procesoSeleccionado = new WProcessDefBL().getWProcessDefByPK(idProcess, userId);
			pasoSeleccionado = new WStepDefBL().getWStepDefByPK(idStep, userId);

		} catch (WProcessDefException e) {

			String mensaje = "No se puede definir el proceso indicado ....\n";
			mensaje +="error:"+e.getMessage()+" - "+ e.getCause();
			logger.info("inyectar: _definicionObjetosDelEntorno: "+mensaje);
			throw new InyectorException( mensaje );

		} catch (WStepDefException e) {

			String mensaje = "No se puede definir el paso indicado ....\n";
			mensaje +="error:"+e.getMessage()+" - "+ e.getCause();
			logger.info("inyectar: _definicionObjetosDelEntorno: "+mensaje);
			throw new InyectorException( mensaje );

		} catch (WStepSequenceDefException e) {
			String mensaje = "No se puede cargar el proceso indicado id:"+idProcess;
			mensaje +=" ERROR:"+e.getMessage()+" - "+ e.getCause();
			logger.info("inyectar: _definicionObjetosDelEntorno: "+mensaje);
			throw new InyectorException( mensaje );
		}
		
	}
	
	private WProcessWork _setProcessWork(
			Integer idProcess, Integer idStep, 
			Integer idObject, String idObjectType,
			String objReference, String objComments, Integer userId) {
		
		WProcessWork newWorkObject = new WProcessWork();
		
		newWorkObject.setProcess(procesoSeleccionado);
//		newWorkObject.setVersion(version); (obsoleto y ano se usa el idProcess ya implica la version ...)
		
//		pasoAInyectar.setPreviousStep(null);
//		pasoAInyectar.setCurrentStep(pasoSeleccionado);
		
		newWorkObject.setIdObject(idObject);
		newWorkObject.setIdObjectType(idObjectType);
		
		newWorkObject.setReference(objReference);
		newWorkObject.setComments(objComments);
		
		newWorkObject.setStartingTime( new Date() );
		
		return newWorkObject;
		
	}
	
	private WStepWork _setStepWork(WProcessWork work, Integer userId) {
		
		WStepWork pasoAInyectar = new WStepWork();
		
		pasoAInyectar.setProcess(procesoSeleccionado);
//		pasoAInyectar.setVersion(version);
		
		pasoAInyectar.setPreviousStep(null);
		pasoAInyectar.setCurrentStep(pasoSeleccionado);
		
		// esta valor se carga en WProcessWorkBL.start si corresponde
		// al lanzamiento del workflow
		pasoAInyectar.setwProcessWork(work); // nes 20120126

//		pasoAInyectar.setIdObject(idObject);
//		pasoAInyectar.setIdObjectType(idObjectType);
		
		// nes 20110106 - asocio los identificadores conocidos por el usuario al paso ...
//		pasoAInyectar.setReference(objReference);
//		pasoAInyectar.setComments(objComments);
		
		pasoAInyectar.setArrivingDate(new Date());
		

		pasoAInyectar.setOpenedDate(null);
		pasoAInyectar.setOpenerUser(null);

		pasoAInyectar.setDecidedDate(null);
		pasoAInyectar.setPerformer(null);

		// TODO: AJUSTAR CUANDO DEJEMOS MODIFICAR ESTO EN EL FORM ...
		pasoAInyectar.setTimeUnit(pasoSeleccionado.getTimeUnit());

		pasoAInyectar.setAssignedTime(pasoSeleccionado.getAssignedTime());
		pasoAInyectar.setDeadlineDate(pasoSeleccionado.getDeadlineDate());
		pasoAInyectar.setDeadlineTime(pasoSeleccionado.getDeadlineTime());
		pasoAInyectar.setReminderTimeUnit(pasoSeleccionado.getReminderTimeUnit());
		pasoAInyectar.setReminderTime(pasoSeleccionado.getReminderTime()); // en unidades de tiempo indicadas en reminderTimeUnit
		
		pasoAInyectar.setAdminProcess(false); // esto va en true si es un administrador quien decide el paso ( en vez del usuario o grupo asignado )
		
		pasoAInyectar.setLocked(false);
		pasoAInyectar.setLockedBy(null);
		pasoAInyectar.setLockedSince(null);
		
		// TODO: ver como se debe asignar este paso ...
		pasoAInyectar.setAssignedTo(null); 

		pasoAInyectar.setInsertUser(new WUserDef(userId)); // nes 20121222
		pasoAInyectar.setModDate(DEFAULT_MOD_DATE); // 1/1/1970 por default
		pasoAInyectar.setModUser(null);
		
		return pasoAInyectar;
		
	}
}
