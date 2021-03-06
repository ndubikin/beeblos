package org.beeblos.bpm.web.bean;

import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;
import static org.beeblos.bpm.core.util.Constants.ALIVE;
import static org.beeblos.bpm.core.util.Constants.CONSULTA_TAREA;
import static org.beeblos.bpm.core.util.Constants.FAIL;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepWorkBL;
import org.beeblos.bpm.core.error.CantLockTheStepException;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepLockedByAnotherUserException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.wc.taglib.bean.util.TareaWorkflowUtil;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import com.sp.common.jsf.util.UtilsVs;


public class ConsultaTareaBean extends CoreManagedBean {
	
	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(ConsultaTareaBean.class);

	private Integer idPasoProceso=null; // id del registro corriente ( cuando pincha en la lista )
	
	private Integer idProceso=null; // el proceso elegido en el combo
	
	//dml 20111219
	private List<SelectItem> lPasosValidos= new ArrayList<SelectItem>();
	private Integer version;
	private Integer selectedStepDefId;
	private String filtroComentariosYReferencia;

	private Integer idObject; // id del objeto correspondiente al paso
	private String idObjectType; // tipo del objeto del paso corriente
	
	private List<SelectItem> lProceso = new ArrayList<SelectItem>(); // para cargar combo de Procesos

	private Integer usuarioLogueado;

	private String consultaPalabrasNombre;
	private Date procesoFechaLlegado;
	private Date procesoFechaRevisado;
	private Date proyectoFechaLimite;
	
	List<WStepWork> lTareas = null; // new ArrayList<WStepWork>(); nes 20111221
	private Integer nResultados = 0;  // martin - 20101221

	// nes 20110913 HARDCODE
	List<WStepWork> lTareasDepto4 = null;
	private Integer nResultadosDepto4 = 0;  

	
	public ConsultaTareaBean() {

		_init();

	}
	
	private void _init(){
		
		_setLoggedUser(); // setea usuarioLogueado
		
		consultaPalabrasNombre = "";
		nResultados = 0; // martin - 20101221

		//dml 20111219
		this.lPasosValidos = new ArrayList<SelectItem>();
		this.setSelectedStepDefId(null);
		this.filtroComentariosYReferencia = "";
		
		//rrl: 20101222
		try {
			
			lProceso = UtilsVs
						.castStringPairToSelectitem(
								new WProcessDefBL().getComboList("Todos ...", null, usuarioLogueado));
			
		} catch (WProcessDefException e) {
			String message = "ConsultaTareaBean._init() WProcessDefException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
		try {
			cargaListaTareasDepto4(null);
		} catch (Exception e) {
			logger.error("ConsultaTareasBean(): error al intentar cargar la lista de tareas");
		} 
		
		// nes 20111221
		this.lTareas = null;
		
	}

	public void reset(){

		idPasoProceso=0;
		consultaPalabrasNombre="";
		procesoFechaLlegado=null;
		procesoFechaRevisado=null;
		proyectoFechaLimite=null;
		nResultados = 0; // martin - 20101221

	}
	

	
	public String cargarPaso(){

		String retorno=FAIL;
		
		if (usuarioLogueado==null) _setLoggedUser();
		
		try {

			retorno = new TareaWorkflowUtil()
							.cargarPasoWorkflow(idPasoProceso, idObject, idObjectType);

			this.lTareas=null; // nes 20110220 - obligo a que se recargue la lista porque si no no refresca bien ...
			nResultados=0;
			this.lTareasDepto4=null;
			nResultadosDepto4=0;
			
		} catch (CantLockTheStepException e) {
			String message = "ConsultaTareaBean.cargarPaso() CantLockTheStepException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);

			retorno=FAIL;
			
		} catch (WStepLockedByAnotherUserException e) {
			String message = "ConsultaTareaBean.cargarPaso() WStepLockedByAnotherUserException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);

			retorno=FAIL;
			
		} catch (WStepWorkException e) {
			String message = "ConsultaTareaBean.cargarPaso() WStepWorkException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);

			retorno=FAIL;
			
		} catch (WUserDefException e) {
			String message = "ConsultaTareaBean.cargarPaso() WUserDefException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			
			retorno=FAIL;
			
		}

		return retorno;

	}
	
	public String desbloquear() {
		
		String retorno=FAIL;
		
		if (usuarioLogueado==null) _setLoggedUser();
		
		try {

			retorno = new TareaWorkflowUtil()
							.desbloquearPasoWorkflow(idPasoProceso, idObject, idObjectType);

			this.lTareas=null; // nes 20110220 - obligo a que se recargue la lista porque si no no refresca bien ...
			nResultados=0;
			this.lTareasDepto4=null;
			nResultadosDepto4=0;
			
		} catch (CantLockTheStepException e) {
			String message = "ConsultaTareaBean.desbloquear() CantLockTheStepException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);

			retorno=FAIL;
			
		} catch (WStepLockedByAnotherUserException e) {
			String message = "ConsultaTareaBean.desbloquear() WStepLockedByAnotherUserException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);

			retorno=FAIL;
			
		} catch (WStepWorkException e) {
			String message = "ConsultaTareaBean.desbloquear() WStepWorkException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);

			retorno=FAIL;
			
		} catch (WUserDefException e) {
			String message = "ConsultaTareaBean.desbloquear() WUserDefException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		
			retorno=FAIL;
			
		}

		return retorno;		
	}

	public String desbloquearDesdeMenuItem() {
		
		if (usuarioLogueado==null) _setLoggedUser();
		
		try {

			new TareaWorkflowUtil()
							.desbloquearPasoWorkflow(idPasoProceso, idObject, idObjectType);

			this.lTareas=null; // nes 20110220 - obligo a que se recargue la lista porque si no no refresca bien ...
			nResultados=0;
			this.lTareasDepto4=null;
			nResultadosDepto4=0;
			
		} catch (CantLockTheStepException e) {
			String message = "ConsultaTareaBean.desbloquearDesdeMenuItem() CantLockTheStepException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			
		} catch (WStepLockedByAnotherUserException e) {
			String message = "ConsultaTareaBean.desbloquearDesdeMenuItem() WStepLockedByAnotherUserException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (WStepWorkException e) {
			String message = "ConsultaTareaBean.desbloquearDesdeMenuItem() WStepWorkException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (WUserDefException e) {
			String message = "ConsultaTareaBean.desbloquearDesdeMenuItem() WUserDefException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

		return null;		
	}


//	public String buscarTareas(ActionEvent event) {
//		buscarTareas();
//		return null;
//	}

	public String buscarTareas() {		
		
		try {
			cargaListaTareas(consultaPalabrasNombre.trim());
		} catch (Exception e) {
			logger.warn("error al intentar cargar la lista de tareas - "+e.getMessage());
		} 
		
		return CONSULTA_TAREA; 
	}
	
	private void cargaListaTareas(String argumento) throws WStepWorkException {
		
		try {
			
			lTareas = (ArrayList<WStepWork>)
							new WStepWorkBL()
									.getWorkListByProcess(
											idProceso, selectedStepDefId, ALIVE, usuarioLogueado, 
											false, procesoFechaLlegado, procesoFechaRevisado,
											proyectoFechaLimite,filtroComentariosYReferencia);
			
			nResultados = lTareas.size(); // martin - 20101221

			lTareasDepto4=null; // nes 20111222 - anulo por las dudas para asegurarme que recargue


		} catch (WStepWorkException e) {
			logger.error("Ocurrio Un Error al consultar la lista de tareas: "+e.getMessage()+"\n"+e.getCause());	
			throw new WStepWorkException("WStepWork: error al recuperar la lista de pasos de proceso - "+e.getMessage()+" : "+e.getCause());
		}
		
	}
	
	
	public String buscarTareasDepto4() {		
		
		try {
			cargaListaTareasDepto4(consultaPalabrasNombre.trim());
		} catch (Exception e) {
			logger.warn("error al intentar cargar la lista de tareas - "+e.getMessage());
		} 
		
		return CONSULTA_TAREA; 
	}
	
	
	private void cargaListaTareasDepto4(String argumento) throws WStepWorkException {
		
		try {
			
			// nes 20110913 HARDCODE
			lTareasDepto4 = (ArrayList<WStepWork>)
									new WStepWorkBL()
										.getWorkListByProcess( null, 400, ALIVE, usuarioLogueado, false, null, null, null, "");
										
			nResultadosDepto4 = lTareasDepto4.size(); // martin - 20101221

		} catch (WStepWorkException e) {
			logger.error("Ocurrio Un Error al consultar la lista de tareas: "+e.getMessage()+"\n"+e.getCause());	
			throw new WStepWorkException("WStepWork: error al recuperar la lista de pasos de proceso - "+e.getMessage()+" : "+e.getCause());
		}
		
	}
	
	// dml 20111223
	public String cargaFichaProyecto() {

		String retorno = FAIL;



		return retorno;

	}

	public Integer getIdPasoProceso() {
		return idPasoProceso;
	}
	public void setIdPasoProceso(Integer idPasoProceso) {
		this.idPasoProceso = idPasoProceso;
	}
	public Integer getIdProceso() {
		return idProceso;
	}
	public void setIdProceso(Integer idProceso) {
		this.idProceso = idProceso;
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
	public List<SelectItem> getlProceso() {
		return lProceso;
	}
	public void setlProceso(List<SelectItem> lProceso) {
		this.lProceso = lProceso;
	}
	public void setIdObjectType(String idObjectType) {
		this.idObjectType = idObjectType;
	}
	public String getConsultaPalabrasNombre() {
		return consultaPalabrasNombre;
	}
	public void setConsultaPalabrasNombre(String consultaPalabrasNombre) {
		this.consultaPalabrasNombre = consultaPalabrasNombre;
	}
	public Date getProcesoFechaLlegado() {
		return procesoFechaLlegado;
	}
	public void setProcesoFechaLlegado(Date procesoFechaLlegado) {
		this.procesoFechaLlegado = procesoFechaLlegado;
	}
	public Date getProcesoFechaRevisado() {
		return procesoFechaRevisado;
	}
	public void setProcesoFechaRevisado(Date procesoFechaRevisado) {
		this.procesoFechaRevisado = procesoFechaRevisado;
	}
	public Date getProyectoFechaLimite() {
		return proyectoFechaLimite;
	}
	public void setProyectoFechaLimite(Date proyectoFechaLimite) {
		this.proyectoFechaLimite = proyectoFechaLimite;
	}
    public List<WStepWork> getlTareas() {

        // rrl 20110217
        if (lTareas == null) {
            buscarTareas();
        }

        return lTareas;
    } 
	public void setlTareas(List<WStepWork> lTareas) {
		this.lTareas = lTareas;
	}
	public void setnResultados(Integer nResultados) {
		this.nResultados = nResultados;
	}
	public Integer getnResultados() {
		return nResultados;
	}
	

	public List<WStepWork> getlTareasDepto4() {

        if (lTareasDepto4 == null) {
            buscarTareasDepto4();
        }
		
		return lTareasDepto4;
	}

	public void setlTareasDepto4(List<WStepWork> lTareasDepto4) {
		this.lTareasDepto4 = lTareasDepto4;
	}

	public Integer getnResultadosDepto4() {
		return nResultadosDepto4;
	}

	public void setnResultadosDepto4(Integer nResultadosDepto4) {
		this.nResultadosDepto4 = nResultadosDepto4;
	}

//	@Override
//	public String toString() {
//		final int maxLen = 2;
//		return "ConsultaTareaBean [consultaPalabrasNombre="
//				+ consultaPalabrasNombre
//				+ ", idPasoProceso="
//				+ idPasoProceso
//				+ ", lTareas="
//				+ (lTareas != null ? lTareas.subList(0, Math.min(
//						lTareas.size(), maxLen)) : null)
//				+ ", procesoFechaLlegado=" + procesoFechaLlegado
//				+ ", procesoFechaRevisado=" + procesoFechaRevisado
//				+ ", proyectoFechaLimite=" + proyectoFechaLimite + "]";
//	}

	
	private void _setLoggedUser() {
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);
		if (cs!=null) 
			usuarioLogueado=cs.getUsuario().getIdUsuario();
		else 
			usuarioLogueado=null;
		
	}

	public Integer getUsuarioLogueado() {
		return usuarioLogueado;
	}

	public List<SelectItem> getlPasosValidos() {
		return lPasosValidos;
	}

	public void setlPasosValidos(List<SelectItem> lPasosValidos) {
		this.lPasosValidos = lPasosValidos;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getSelectedStepDefId() {
		return selectedStepDefId;
	}

	public void setSelectedStepDefId(Integer selectedStepDefId) {
		this.selectedStepDefId = selectedStepDefId;
	}
		
	public void cambiarPaso() {

		if (idProceso != null && idProceso != 0) {

			cargarListaPasos();

		}
		else {

			lPasosValidos = new ArrayList<SelectItem>();
			lPasosValidos.add(new SelectItem(null,"Todos ..."));

		}
	
	}
	
	// dml 2011122
	public void cargarListaPasos() {


//			if ((lPasosValidos==null || lPasosValidos.size()==0)
//					||((lPasosValidos.size() == 1) && ("Seleccionar paso ...".equals(lPasosValidos.get(0).getLabel())))) {
				try {
					
					//version = new WStepSequenceDefBL().getLastVersionWStepSequenceDef(idProceso);// nes 20130506 quitada version
					
					lPasosValidos=
							UtilsVs
								.castStringPairToSelectitem(
									new WStepDefBL().getComboList(idProceso, "Seleccionar paso ...", null));
					
				} catch (WProcessDefException e) {
					e.printStackTrace();
					lPasosValidos.add(new SelectItem(null,"No se pudo cargar la lista de procesos ..."));
				}
//			}

	}

	public String getFiltroComentariosYReferencia() {
		return filtroComentariosYReferencia;
	}

	public void setFiltroComentariosYReferencia(
			String filtroComentariosYReferencia) {
		this.filtroComentariosYReferencia = filtroComentariosYReferencia;
	}
	
	public String getLockedSinceDateInString(Date date){
		return date.toString();
	}
	

}