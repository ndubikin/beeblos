package org.beeblos.bpm.wc.taglib.bean;

import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;
import static org.beeblos.bpm.core.util.Constants.ACTIVE_DATA_FIELDS;
import static org.beeblos.bpm.core.util.Constants.PAGINA_ANEXA_DEFAULT;
import static org.beeblos.bpm.core.util.Constants.PAGINA_LISTA_DEFAULT;
import static org.beeblos.bpm.core.util.Constants.PAGINA_PROCESO_DEFAULT;
import static org.beeblos.bpm.core.util.Constants.WELCOME_PAGE;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepSequenceDefBL;
import org.beeblos.bpm.core.error.AlreadyExistsRunningProcessException;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.util.ListConverters;
import org.beeblos.bpm.tm.exception.TableManagerException;
import org.beeblos.bpm.wc.security.error.InyectorException;
import org.beeblos.bpm.wc.taglib.bean.util.BeeBPMBL;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;

import com.sp.common.jsf.util.UtilsVs;


/**
 * @author nes
 * 
 * Clase para manejar los workflow e inyectar en 1 workflow 1 item determinado ( Proyecto por ahora, luego etc )
 *
 */

public class InyectorBean  extends CoreManagedBean {

	private static final Log logger = LogFactory.getLog(InyectorBean.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer usuarioLogueado;
	
	private Integer selectedProcessId;
	private Integer version;
	private Integer selectedStepDefId;
	
	private WProcessDef selectedProcess;
	private WStepDef selectedStepDef;
	
	private ManagedData managedData;
	
	private Boolean botonInyectarDisabled;
	
	private Integer idStepWork; // el id del paso  
	
	private Integer idObject;   // id y tipo de objeto relacionado con este paso
	private String 	idObjectType;
	
	private String objReference; // una referencia para identificar el objet, puede ser el título en el caso de 1 proyecto
	private String objComments;
	private String objIdUsuario; // el id reconocido por el usuario para ese objeto a inyectar
	
	private List<SelectItem> lPasosValidos= new ArrayList<SelectItem>();
	
	private List<SelectItem> lProcesosActivos= new ArrayList<SelectItem>(); // muestra la lista de procesos que están operativos
	
//	private WStepWork pasoActual = new WStepWork();
	
	private String paginaProceso;
	private String paginaLista;
	private String paginaAnexa;
	
	private Integer currentUser;

	public InyectorBean() {
		init();
	}

	public void init()  {
		try {
			
			botonInyectarDisabled=true;
			_limpiarVariablesDelBean();			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void _limpiarVariablesDelBean() {
		
		this.selectedStepDefId=null;
		this.selectedProcessId=null;
		this.selectedStepDef=null;
		this.selectedProcess=null;

		//rrl 20130729 
		this.objReference=null;
		this.objComments=null;
		
		this.idStepWork=null;
		this.idObject=null;
		this.idObjectType=null;
//		this.pasoActual=null;
		this.lPasosValidos=new ArrayList<SelectItem>();
		this.lProcesosActivos= new ArrayList<SelectItem>();
		this.currentUser=null;
		this.paginaProceso=PAGINA_PROCESO_DEFAULT;
		this.paginaLista=PAGINA_LISTA_DEFAULT;
		this.paginaAnexa=PAGINA_ANEXA_DEFAULT;
		
	}


	// nes 20101003
	public String irAlMenuPrincipal(){
		
		return WELCOME_PAGE;
		
	}

	// set selectedProcessId and load data for this process
	public String changeProcess() throws InyectorException {
		
		logger.debug(" id"+objIdUsuario+" ref:"+objReference);
		logger.debug(" idObject"+idObject+" clase:"+idObjectType);
		
		setShowHeaderMessage(false); // nes 20100117 oculta mensajes previos en pantalla
		
		// check selectedProcessId has a valid value
		if (selectedProcessId==null || selectedProcessId==0) {
			selectedStepDefId=null;
			return null;
		}

		WProcessDefBL pdBL = new WProcessDefBL(); 
		WStepSequenceDefBL wsdBL = new WStepSequenceDefBL();
		WStepDefBL wsBL= new WStepDefBL();
		
		// load selected process ... and valid step list for user selection
		
		try {
			
			setProcesoSeleccionado(pdBL.getWProcessDefByPK(selectedProcessId, null));
			
			lPasosValidos= 
					UtilsVs
					.castStringPairToSelectitem(
							wsBL.getComboList(selectedProcessId, "Seleccionar paso ...", null));
			
			if (selectedProcess.getBeginStep()!=null 
					&& selectedProcess.getBeginStep().getId()!=null 
					&& selectedProcess.getBeginStep().getId()!=0 ){
				selectedStepDefId=selectedProcess.getBeginStep().getId();
			}
			
			// if exists custom data fields 
			if (selectedProcess!=null
					&& selectedProcess.getProcess().getProcessDataFieldDef()!=null
					&& selectedProcess.getProcess().getProcessDataFieldDef().size()>0) {
				managedData = new ManagedData();
				managedData.setDataField( 
						ListConverters.convertWProcessDataFieldToList
						 (selectedProcess.getProcess().getProcessDataFieldDef(),null,null,ACTIVE_DATA_FIELDS) );
				managedData.setChanged(false);
				managedData.setManagedTableConfiguration(
						selectedProcess.getProcess().getManagedTableConfiguration());
			} else {
				managedData=null;
			}
			
		} catch (WStepSequenceDefException e) {
			String message = "InyectorBean.changeProcess(): Error en la recuperacion del mapa del proceso ...";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (WProcessDefException e) {
			String message = "InyectorBean.changeProcess(): Error en la recuperación de la definición del proceso ...";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

		return null;
		
	}
	
	public String changeStep() {
		
		if (selectedStepDefId!=null && selectedProcessId!=null) {
			
			botonInyectarDisabled=false;
			
			WStepDefBL stepDefBL = new WStepDefBL();
			
			try {
				setPasoSeleccionado(
						stepDefBL.getWStepDefByPK(
								selectedStepDefId, selectedProcess.getProcess().getId(), currentUser));// nes 20130808 - por agregado filtro
			} catch (WStepDefException e) {
				logger.error("can't get WStepDef for id:"+selectedStepDefId+"  >> will be reset to null!!");
				setSelectedStepDefId(null);
				setSelectedStepDefId(0);
			}
			
		} else {
			botonInyectarDisabled=true;
		}

		return null;
		
	}

	
	// called from xhtml view ...
	public String launchWork() throws InyectorException {
		
		String ret=null;
		setShowHeaderMessage(false); // nes 20100117 oculta mensajes previos en pantalla
		_controlConsistenciaStepALanzar(); // controla que los elementos necesarios vengan cargados y si no sale por InyectorException
		
		try {
			
			//idStepWork = new WStepWorkBL().add(_setStepWork(), usuarioLogueado) ;
			idStepWork = new BeeBPMBL()
								.inyectar(
										selectedProcessId, selectedStepDefId, 
										idObject, idObjectType, objReference, objComments, 
										managedData,
										_getLoggedUser());
			
			//rrl 20100114
			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla
			
			//rrl 20130729 
			_limpiarVariablesDelBean();

			// setea el mensaje en pantalla al usuario
		} catch (WStepWorkException e) {
			String message = "InyectorBean.launchWork() WStepWorkException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			throw new InyectorException(message);
			
		} catch (AlreadyExistsRunningProcessException e) {
			String message = "InyectorBean.launchWork() AlreadyExistsRunningProcessException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			throw new InyectorException(message);
			
		} catch (InyectorException e) {
			String message = "InyectorBean.launchWork() InyectorException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			throw new InyectorException(message);
			
		} catch (WProcessWorkException e) {
			String message = "InyectorBean.launchWork() WProcessWorkException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			throw new InyectorException(message);
		} catch (TableManagerException e) {
			String message = "InyectorBean.launchWork() TableManagerException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			throw new InyectorException(message);
		} catch (WStepWorkSequenceException e) {
			String message = "InyectorBean.launchWork() WStepWorkSequenceException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			throw new InyectorException(message);
		}
		
		return ret;
		
	}
	

	private void _controlConsistenciaStepALanzar() throws InyectorException {
		
		if (selectedProcessId==null || selectedProcessId==0) {
			
			String mensaje = "Debe elegir un proceso de la lista ....\n";
			mensaje +=" Id:"+objIdUsuario+" ref:"+objReference; // NOTA REVISAR ESTOS objIdUsuario PORQUE LOS ESTABA USANDO MAL ... NES 20111216
			logger.info("inyectar:"+mensaje);
			throw new InyectorException(mensaje);
			
		}
		
		if (selectedStepDefId==null || selectedStepDefId==0) {
			
			String mensaje = "Debe elegir un paso de la lista para insertar el elemento en el workflow....\n";
			mensaje +=" Id:"+objIdUsuario+" ref:"+objReference;
			logger.info("inyectar:"+mensaje);
			throw new InyectorException(mensaje);
		}
		
		// NESTOR ARREGLAR ESTO LUEGO EXIGIENDO ESTE DATO SI HAY OBJ DEFINIDOS EN EL PROCESO.
//		if (idObject==null || idObject==0 ||
//				idObjectType==null || "".equals(idObjectType)) {
//			
//			String mensaje = "El elemento a insertar en el workflow no está definido ( idObject, idObjectType ) ....\n";
//			mensaje +=" Id:"+objIdUsuario+" ref:"+objReference;
//			logger.info("inyectar:"+mensaje);
//			throw new InyectorException(mensaje);
//		}
	}
//	
//	private void _controlExistenciaPasosVivos() 
//	throws AlreadyExistsRunningProcessException, WStepWorkException {
//		
//		try {
//			if ( new WStepWorkBL().existsActiveProcess(selectedProcessId, idObject, idObjectType, currentUser)) {
//				
//				String mensaje = "Ya existe un wofkflow en ejecución para este elemento ....\n";
//				logger.info("inyectar: "+mensaje);
//				throw new AlreadyExistsRunningProcessException(mensaje);
//			}
//		} catch (WStepWorkException e) {
//			String mensaje = "El sistema arrojó un error al intentar verificar si existen procesos activos para este objeto ....\n";
//			logger.error(mensaje);
//			throw new WStepWorkException(mensaje);
//		}
//		
//	}
	
//	private void _definicionObjetosDelEntorno() throws InyectorException {
//		
//		try {
//			selectedProcess = new WProcessDefBL().getWProcessDefByPK(selectedProcessId, usuarioLogueado);
//			selectedStepDef = new WStepDefBL().getWStepDefByPK(selectedStepDefId, usuarioLogueado);
//
//		} catch (WProcessDefException e) {
//
//			String mensaje = "No se puede definir el proceso indicado ....\n";
//			mensaje +="error:"+e.getMessage()+" - "+ e.getCause();
//			logger.info("inyectar: _definicionObjetosDelEntorno: "+mensaje);
//			throw new InyectorException( mensaje );
//
//		} catch (WStepDefException e) {
//
//			String mensaje = "No se puede definir el paso indicado ....\n";
//			mensaje +="error:"+e.getMessage()+" - "+ e.getCause();
//			logger.info("inyectar: _definicionObjetosDelEntorno: "+mensaje);
//			throw new InyectorException( mensaje );
//
//		} catch (WStepSequenceDefException e) {
//			String mensaje = "No se puede cargar el proceso indicado id:"+selectedProcessId;
//			mensaje +=" ERROR:"+e.getMessage()+" - "+ e.getCause();
//			logger.info("inyectar: _definicionObjetosDelEntorno: "+mensaje);
//			throw new InyectorException( mensaje );
//		}
//		
//	}
//	private WProcessWork _setProcessWork() {
//		
//		WProcessWork newWorkObject = new WProcessWork();
//		
//		newWorkObject.setProcess(selectedProcess);
//		newWorkObject.setVersion(version);
//		
////		pasoAInyectar.setPreviousStep(null);
////		pasoAInyectar.setCurrentStep(selectedStepDef);
//		
//		newWorkObject.setIdObject(idObject);
//		newWorkObject.setIdObjectType(idObjectType);
//		
//		newWorkObject.setReference(objReference);
//		newWorkObject.setComments(objComments);
//		
//		newWorkObject.setStartingTime( new Date() );
//		
//		return newWorkObject;
//		
//	}
//	
//	private WStepWork _setStepWork(WProcessWork work) {
//		
//		WStepWork pasoAInyectar = new WStepWork();
//		
//		pasoAInyectar.setProcess(selectedProcess);
//		pasoAInyectar.setVersion(version);
//		
//		pasoAInyectar.setPreviousStep(null);
//		pasoAInyectar.setCurrentStep(selectedStepDef);
//		
//		// esta valor se carga en WProcessWorkBL.start si corresponde
//		// al lanzamiento del workflow
//		pasoAInyectar.setwProcessWork(work); // nes 20120126
//
////		pasoAInyectar.setIdObject(idObject);
////		pasoAInyectar.setIdObjectType(idObjectType);
//		
//		// nes 20110106 - asocio los identificadores conocidos por el usuario al paso ...
////		pasoAInyectar.setReference(objReference);
////		pasoAInyectar.setComments(objComments);
//		
//		pasoAInyectar.setArrivingDate(new Date());
//		
//
//		pasoAInyectar.setOpenedDate(null);
//		pasoAInyectar.setOpenerUser(null);
//
//		pasoAInyectar.setDecidedDate(null);
//		pasoAInyectar.setPerformer(null);
//
//		// TODO: AJUSTAR CUANDO DEJEMOS MODIFICAR ESTO EN EL FORM ...
//		pasoAInyectar.setTimeUnit(selectedStepDef.getTimeUnit());
//
//		pasoAInyectar.setAssignedTime(selectedStepDef.getAssignedTime());
//		pasoAInyectar.setDeadlineDate(selectedStepDef.getDeadlineDate());
//		pasoAInyectar.setDeadlineTime(selectedStepDef.getDeadlineTime());
//		pasoAInyectar.setReminderTimeUnit(selectedStepDef.getReminderTimeUnit());
//		pasoAInyectar.setReminderTime(selectedStepDef.getReminderTime()); // en unidades de tiempo indicadas en reminderTimeUnit
//		
//		pasoAInyectar.setAdminProcess(false); // esto va en true si es un administrador quien decide el paso ( en vez del usuario o grupo asignado )
//		
//		pasoAInyectar.setLocked(false);
//		pasoAInyectar.setLockedBy(null);
//		pasoAInyectar.setLockedSince(null);
//		
//		// TODO: ver como se debe asignar este paso ...
//		pasoAInyectar.setAssignedTo(null); 
//
//		pasoAInyectar.setInsertUser(new WUserDef(usuarioLogueado)); // nes 20121222
//		pasoAInyectar.setModDate(ConstantesBDC.DEFAULT_MOD_DATE); // 1/1/1970 por default
//		pasoAInyectar.setModUser(null);
//		
//		return pasoAInyectar;
//		
//	}

	
	private Integer _getLoggedUser() {
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);
		if (cs!=null) 
			usuarioLogueado=cs.getUsuario().getIdUsuario();
		else 
			usuarioLogueado=null;
		
		return usuarioLogueado;
	}
	
	public Integer getIdObject() {
		return idObject;
	}
	public void setIdObject(Integer idObject) {
		this.idObject = idObject;
	}
	public String getIdObjectType() {
		return idObjectType;
	}
	public void setIdObjectType(String idObjectType) {
		this.idObjectType = idObjectType;
	}
//	public WStepWork getPasoActual() {
//		return pasoActual;
//	}
//	public void setPasoActual(WStepWork pasoActual) {
//		this.pasoActual = pasoActual;
//	}
	public String getPaginaProceso() {
		return paginaProceso;
	}
	public void setPaginaProceso(String paginaProceso) {
		this.paginaProceso = paginaProceso;
	}
	public String getPaginaLista() {
		return paginaLista;
	}
	public void setPaginaLista(String paginaLista) {
		this.paginaLista = paginaLista;
	}
	public String getPaginaAnexa() {
		return paginaAnexa;
	}
	public void setPaginaAnexa(String paginaAnexa) {
		this.paginaAnexa = paginaAnexa;
	}
	public Integer getIdStepWork() {
		return idStepWork;
	}
	public void setIdStepWork(Integer idStepWork) {
		this.idStepWork = idStepWork;
	}
	public List<SelectItem> getlPasosValidos() {
		return lPasosValidos;
	}

	/**
	 * @param lPasosValidos the lPasosValidos to set
	 */
	public void setlPasosValidos(List<SelectItem> lPasosValidos) {
		
		if (lPasosValidos==null || lPasosValidos.size()==0) {
			try {
				lPasosValidos=
						UtilsVs
						.castStringPairToSelectitem(
								new WStepDefBL().getComboList(
										selectedProcessId, "Seleccionar paso ...", null));
			} catch (WProcessDefException e) {
				e.printStackTrace();
				lPasosValidos.add(new SelectItem(null,"No se pudo cargar la lista de procesos ..."));
			}
		}
		
		this.lPasosValidos = lPasosValidos;
	}

	public Integer getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(Integer currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * @return the lProcesosActivos
	 */
	public List<SelectItem> getlProcesosActivos() {
		if (lProcesosActivos==null || lProcesosActivos.size()==0) {
			try {
				lProcesosActivos=
						UtilsVs
						.castStringPairToSelectitem(
								new WProcessDefBL().getComboList("Seleccionar ...", null,getCurrentUser()));
			} catch (WProcessDefException e) {
				e.printStackTrace();
				lProcesosActivos.add(new SelectItem(null,"No se pudo cargar la lista de procesos ..."));
			}
		}
		return lProcesosActivos;
	}

	/**
	 * @param lProcesosActivos the lProcesosActivos to set
	 */
	public void setlProcesosActivos(List<SelectItem> lProcesosActivos) {
		this.lProcesosActivos = lProcesosActivos;
	}
	public Integer getSelectedProcessId() {
		return selectedProcessId;
	}
	public void setSelectedProcessId(Integer selectedProcessId) {
		this.selectedProcessId = selectedProcessId;
	}
	public Integer getSelectedStepDefId() {
		return selectedStepDefId;
	}
	public void setSelectedStepDefId(Integer selectedStepDefId) {
		this.selectedStepDefId = selectedStepDefId;
	}
	public WProcessDef getProcesoSeleccionado() {
		return selectedProcess;
	}
	public void setProcesoSeleccionado(WProcessDef selectedProcess) {
		this.selectedProcess = selectedProcess;
	}
	public WStepDef getPasoSeleccionado() {
		return selectedStepDef;
	}
	public void setPasoSeleccionado(WStepDef selectedStepDef) {
		this.selectedStepDef = selectedStepDef;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getObjReference() {
		return objReference;
	}
	public void setObjReference(String objReference) {
		this.objReference = objReference;
	}
	
	
	
	public String getObjComments() {
		return objComments;
	}

	public void setObjComments(String objComments) {
		this.objComments = objComments;
	}

	public String getObjIdUsuario() {
		return objIdUsuario;
	}
	public void setObjIdUsuario(String objIdUsuario) {
		this.objIdUsuario = objIdUsuario;
	}
	public Boolean getBotonInyectar() {
		return botonInyectarDisabled;
	}
	public void setBotonInyectar(Boolean botonInyectarDisabled) {
		this.botonInyectarDisabled = botonInyectarDisabled;
	}
	public Integer getUsuarioLogueado() {
		return usuarioLogueado;
	}
	public void setUsuarioLogueado(Integer usuarioLogueado) {
		this.usuarioLogueado = usuarioLogueado;
	}
	public Boolean getBotonInyectarDisabled() {
		return botonInyectarDisabled;
	}
	public void setBotonInyectarDisabled(Boolean botonInyectarDisabled) {
		this.botonInyectarDisabled = botonInyectarDisabled;
	}

	public ManagedData getManagedData() {
		return managedData;
	}

	public void setManagedData(ManagedData managedData) {
		this.managedData = managedData;
	}

}
