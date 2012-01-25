package org.beeblos.bpm.web.bean.wf;

import static org.beeblos.bpm.web.util.ConstantsWeb.NONE;
import static org.beeblos.bpm.web.util.ConstantsWeb.PAGINA_ANEXA_DEFAULT;
import static org.beeblos.bpm.web.util.ConstantsWeb.PAGINA_LISTA_DEFAULT;
import static org.beeblos.bpm.web.util.ConstantsWeb.PAGINA_PROCESO_CANCEL_DEFAULT;
import static org.beeblos.bpm.web.util.ConstantsWeb.PAGINA_PROCESO_DEFAULT;
import static org.beeblos.bpm.web.util.ConstantsWeb.PAGINA_PROCESO_OK_DEFAULT;
import static org.beeblos.bpm.web.util.ConstantsWeb.PROCESAR_PAGINA_BIENVENIDA;
import static org.beeblos.bpm.web.util.ConstantsWeb.PROCESAR_TAREA;
import static org.beeblos.bpm.web.util.ConstantsWeb.WELCOME_PAGE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepSequenceDefBL;
import org.beeblos.bpm.core.bl.WStepWorkBL;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.error.CantLockTheStepException;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepAlreadyProcessedException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepLockedByAnotherUserException;
import org.beeblos.bpm.core.error.WStepNotLockedException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WTimeUnit;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;





/**
 * @author nes
 *
 */

public class PasoBean  extends CoreManagedBean {

	private static final Log logger = LogFactory.getLog(PasoBean.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer idStepWork; // el id del paso  
	
	private Integer idObject;   // id y tipo de objeto relacionado con este paso
	private String 	idObjectType;
	
	private String nombreBean, nombreBeanEL; // nes 20110106

	private Integer usuarioLogueado; // para guardar el usuario logueado
	private boolean usuarioIsAdmin;
	
	private Integer idRespuesta; // id de la respuesta que decida el usuario en la vista
	
	private String instruccionesAlProximoPaso; // comentarios o instrucciones que se deseen hacer llegar al proximo paso ...
	
	private String clickAutomatico;
	
	private WTimeUnit sTimeUnit;
	private Integer sAssignedTime;
	private Date sDeadlineDate;
	private Date sDeadlineTime;
	private WTimeUnit sReminderTimeUnit;
	private Integer sReminderTime; // en unidades de tiempo indicadas en reminderTimeUnit
	
	private boolean adminProcess;
	private String assignedTo;  // to asign manually o redirect a step to a user ( here must be a valid user id )
		
	private boolean hayRespuestas;
	
	private List<SelectItem> lRespuestasCombo = new ArrayList<SelectItem>(5);
	private List<WStepWork> lPasosVivos= new ArrayList<WStepWork>();
	
	private WStepWork pasoActual = new WStepWork();
	
	private String paginaProceso;
	private String paginaLista;
	private String paginaAnexa;
	
	private String submitForm;
	
	private String mensajeProceso;
	
	//rrl 20111220
	private boolean instruccionesYComentariosEnabled;

	public PasoBean() {
		
	}

	public void init()  {

		_limpiarVariablesDelBean();

	}

	private void _limpiarVariablesDelBean() {
		
		this.idStepWork=null;
		this.idObject=null;
		this.idObjectType=null;
		this.pasoActual=null;
		this.lRespuestasCombo=new ArrayList<SelectItem>(5);
		this.lPasosVivos=new ArrayList<WStepWork>();
		this.hayRespuestas=false;
		
		_setLoggedUser(); //setea usuarioLogueado y isAdmin
		
		usuarioIsAdmin=false;
		
		this.paginaProceso=PAGINA_PROCESO_DEFAULT;
		this.paginaLista=PAGINA_LISTA_DEFAULT;
		this.paginaAnexa=PAGINA_ANEXA_DEFAULT;
		this.clickAutomatico="document.step_process_form.submit();";
		
	}


	// nes 20101003
	public String irAlMenuPrincipal(){
		
		return WELCOME_PAGE;
		
	}
	

	// setea el paso en el que se va a trabajar
	public void setPaso() 
	throws CantLockTheStepException, WStepLockedByAnotherUserException, WStepWorkException, WUserDefException {

		paginaProceso=PAGINA_PROCESO_DEFAULT;
	
		if (usuarioLogueado==null) _setLoggedUser();
		
		this.lRespuestasCombo=new ArrayList<SelectItem>(5);
		this.lPasosVivos=new ArrayList<WStepWork>();
		this.hayRespuestas=false;
		
		if ( idStepWork!=null && idStepWork!=0 ) {
			
			_setCurrentStepWork();

		} else if ( idObject!=null && idObject!=0 &&
				idObjectType!=null && !"".equals(idObjectType) ) {
		
			this.lPasosVivos = _getListActiveSteps();
			
//TODO: FALTA CONTROLAR QUE SEA PARA EL USUARIO O PARA EL ROL DEL USUARIO ...
			
			if (this.lPasosVivos!=null) {
				if (this.lPasosVivos.size()>0){
					if (this.lPasosVivos.size()==1) {
						this.idStepWork=lPasosVivos.get(0).getId();
						_setCurrentStepWork();
					}

// TODO: FALTA VER QUE HACEMOS SI HAY MAS DE 1 PASO VIVO PARA ESTE USUARIO / OBJETO					
					
				} else {
					throw new WStepWorkException("No hay pasos pendientes para este objeto idObject"+idObject+"("+idObjectType+")");
				}
			}

		}
		logger.debug("------------_>>>>>"+paginaProceso);
	}

	
	public String procesar()   {
		logger.debug("pasoBean:procesar ["+pasoActual.getId()+"]");

		
		String ret = PROCESAR_TAREA;
		Date now = new Date(); // ( date/time )
		
		if (lRespuestasCombo!=null && lRespuestasCombo.size()>0 ) {
			if (idRespuesta==null || idRespuesta==0) {
				
				String mensaje = "Debe indicar una respuesta válida para que se pueda procesar el paso";
				String params[] = {mensaje + ",", ".Por favor, confirme los valores ingresados." };		
				agregarMensaje("62",mensaje,params,FGPException.WARN);
				logger.info(mensaje);
				return PROCESAR_TAREA;
				
			}
		}
		
		if  ( controlPasoHacambiadoEstado() ) {
			String mensaje = "Este paso ha cambiado el estado mientras usted lo tenia en procesamiento. Avise al administrador id:["
							+pasoActual.getId()+"]";
			String params[] = {mensaje + ",", ".Por favor, confirme los valores ingresados." };		
			agregarMensaje("62",mensaje,params,FGPException.WARN);
			logger.error(mensaje);
			return PROCESAR_TAREA;
			
		}
		
		try {
			
			_executeSaveActionInRelatedBean( now );
			
			new WStepWorkBL().checkLock(idStepWork, usuarioLogueado, false); // verifies the user has the step locked before process it ...
			new WStepWorkBL().checkStatus(idStepWork, usuarioLogueado, false); // verifies step is process pending at this time ...
			
			// retrieve routing info from definition
			List<WStepSequenceDef> caminos 
							= new WStepSequenceDefBL()
										.getWStepSequenceDefs(
												pasoActual.getProcess().getId(), // process
												pasoActual.getVersion(), 		 // version
												pasoActual.getCurrentStep().getId(), // current step id
												usuarioLogueado);			
			
			_setCurrentWorkitemToProcessed( now );
			
			_startNewWorkitems( caminos, now ); 
			
			setMensajeproceso();
			
			_limpiarVariablesDelBean();
			
			paginaAnexa=null;
			paginaProceso=PAGINA_PROCESO_OK_DEFAULT;
			ret = PROCESAR_PAGINA_BIENVENIDA; // VA A LA PAGINA DE BIENVENIDA DEL MOTOR DE PROCESAMIENTO DE TAREAS ...

		} catch (WStepSequenceDefException e) {

			String mensaje = "Error al intentar obtener la lista de rutas para avanzar al siguiente paso: "
							+e.getMessage()+" - "+e.getCause();
			String params[] = {mensaje + ",", ".Por favor, avise a soporte." };		
			agregarMensaje("62",mensaje,params,FGPException.WARN);
			logger.info(mensaje);
			ret=null;

		} catch (WStepWorkException e) {

			String mensaje = "Error al intentar establecer el paso actual como 'procesado' : "
				+e.getMessage()+" - "+e.getCause();
			String params[] = {mensaje + ",", ".Por favor, anote la referencia de la tarea y avise a soporte." };		
			agregarMensaje("62",mensaje,params,FGPException.WARN);
			logger.info(mensaje);
			ret=null;

		} catch (WStepDefException e) {
			
			String mensaje = "Error al intentar establecer el próximo paso - No se puede leer la definición de la base de datos ... : "
				+e.getMessage()+" - "+e.getCause();
			String params[] = {mensaje + ",", ".Por favor, anote la referencia de la tarea y avise a soporte." };		
			agregarMensaje("62",mensaje,params,FGPException.WARN);

			logger.info(mensaje);
			ret=null;

		} catch (WStepLockedByAnotherUserException e) {

			String mensaje = "El paso está bloqueado por otro usuario."
				+e.getMessage()+" - "+e.getCause();
			String params[] = {mensaje + ",", "\nPara procesarlo debe salir y seleccionarlo nuevamente de la tarea en la lista de tareas ... \n Si el problema persiste avise al administrador del sistema ..." };		
			agregarMensaje("62",mensaje,params,FGPException.WARN);
			logger.info(mensaje);
			ret=null;

		} catch (WStepAlreadyProcessedException e) {

			String mensaje = "Esta tarea ya se ha procesado ..."
				+e.getMessage()+" - "+e.getCause();
			String params[] = {mensaje + ",", "\nSi piensa que es un error, por favor anote los datos y avise al administrador del sistema ..." };		
			agregarMensaje("62",mensaje,params,FGPException.WARN);
			logger.info(mensaje);
			ret=null;
		} catch (WUserDefException e) {
			String mensaje = "Error al cargar un usuario ..."
					+e.getMessage()+" - "+e.getCause();
			String params[] = {mensaje + ",", "\nSi piensa que es un error, por favor anote los datos y avise al administrador del sistema ..." };		
			agregarMensaje("62",mensaje,params,FGPException.WARN);
			logger.info(mensaje);
			ret=null;
		}
				
		return ret;
	}

	// se corresponde con la acción del botón "guardar"
	// guarda los datos ingresados por el usuario en el step y desbloquea el paso
	public String guardar()  {
		logger.debug("pasoBean:guardar ["+pasoActual.getId()+"]");
		
		String ret = PROCESAR_TAREA;
//		FichaProyectoBean fpb = (FichaProyectoBean)HelperUtil.getBeanFromSession("fichaProyectoBean");
		
		try {

//			fpb.actualizar(); // TODO VER SI HAY QUE GUARDAR LA FICHA DE PROYECTO ACÁ EN ESTE CASO Q NO ESTOY TAN SEGURO ...

			new WStepWorkBL().update(pasoActual, usuarioLogueado);
			
			setMensajeproceso();
			
			_limpiarVariablesDelBean(); // nes 20111219
			
			paginaAnexa=null;
			paginaProceso=PAGINA_PROCESO_OK_DEFAULT;
			
			ret =  PROCESAR_PAGINA_BIENVENIDA; 
			

		} catch (WStepWorkException e) {
			
			String mensaje = "PasoBean:guardar WStepWorkException Error al intentar guardar el paso: "
					+e.getMessage()+" - "+e.getCause();
				String params[] = {mensaje + ",", ".Por favor, anote la referencia de la tarea y avise a soporte." };		
				agregarMensaje("62",mensaje,params,FGPException.WARN);
				logger.error(mensaje);
		}
		
		System.out.println("---------->>> guardar <<<-------------");

		return ret; // nes 20111213
		
	}


	// devuelve el paso al que lo envió
	public String devolver()  {
		logger.debug("pasoBean:devolver ["+pasoActual.getId()+"]");
		
		//rrl y nestor 20111222
		if ( pasoActual.getPreviousStep()==null || pasoActual.getPreviousStep().getId()==null || 
                 pasoActual.getPreviousStep().getId().equals(0) ) {
	        String mensaje = "Error no se puede devolver porque no hay paso previo en el workflow  id:["+pasoActual.getId()+"]";
            String params[] = {mensaje + ",", ".Por favor, anote la referencia de la tarea y avise a soporte." };                
            agregarMensaje("62",mensaje,params,FGPException.WARN);
            logger.error(mensaje);
           	return PROCESAR_TAREA;
		}

		String ret = PROCESAR_TAREA;
		Date now = new Date(); // ( date/time )
		
		try {
			
			_executeSaveActionInRelatedBean( now );
			
			new WStepWorkBL().checkLock(idStepWork, usuarioLogueado, false); // verifies the user has the step locked befor process it ...
			new WStepWorkBL().checkStatus(idStepWork, usuarioLogueado, false); // verifies step is process pending at this time ...
			
			_setCurrentWorkitemToProcessed( now );
			
			_startReturnedWorkitem( now ); 
			
			// set bean to show message and refresh list
			// antes de limpiar cargo el mensaje que voy a mostrar
			// nes 20120120 - agregado objeto wProcessWork
			this.mensajeProceso=	this.pasoActual.getwProcessWork().getReference()+" - "
									+ pasoActual.getwProcessWork().getComments()+ "\n ("
									+ pasoActual.getCurrentStep().getName()+") \n ("
									+ pasoActual.getCurrentStep().getStepComments()+ " )"
									+ " fué devuelto al paso anterior : "+pasoActual.getPreviousStep().getName();
			
			_limpiarVariablesDelBean();
			
			paginaAnexa=null;
			paginaProceso=PAGINA_PROCESO_OK_DEFAULT;
			ret = PROCESAR_PAGINA_BIENVENIDA; // VA A LA PAGINA DE BIENVENIDA DEL MOTOR DE PROCESAMIENTO DE TAREAS ...


		} catch (WStepWorkException e) {

			String mensaje = "Error al intentar establecer el paso actual como 'procesado' : "
				+e.getMessage()+" - "+e.getCause();
			String params[] = {mensaje + ",", ".Por favor, anote la referencia de la tarea y avise a soporte." };		
			agregarMensaje("62",mensaje,params,FGPException.WARN);
			logger.info(mensaje);
			ret=null;

		} catch (WStepDefException e) {
			
			String mensaje = "Error al intentar establecer el próximo paso - No se puede leer la definición de la base de datos ... : "
				+e.getMessage()+" - "+e.getCause();
			String params[] = {mensaje + ",", ".Por favor, anote la referencia de la tarea y avise a soporte." };		
			agregarMensaje("62",mensaje,params,FGPException.WARN);

			logger.info(mensaje);
			ret=null;

		} catch (WStepLockedByAnotherUserException e) {

			String mensaje = "El paso está bloqueado por otro usuario."
				+e.getMessage()+" - "+e.getCause();
			String params[] = {mensaje + ",", "\nPara procesarlo debe salir y seleccionarlo nuevamente de la tarea en la lista de tareas ... \n Si el problema persiste avise al administrador del sistema ..." };		
			agregarMensaje("62",mensaje,params,FGPException.WARN);
			logger.info(mensaje);
			ret=null;

		} catch (WStepAlreadyProcessedException e) {

			String mensaje = "Esta tarea ya se ha procesado ..."
				+e.getMessage()+" - "+e.getCause();
			String params[] = {mensaje + ",", "\nSi piensa que es un error, por favor anote los datos y avise al administrador del sistema ..." };		
			agregarMensaje("62",mensaje,params,FGPException.WARN);
			logger.info(mensaje);
			ret=null;
		} catch (WUserDefException e) {
			String mensaje = "Error al obtener usuario ..."
					+e.getMessage()+" - "+e.getCause();
			String params[] = {mensaje + ",", "\nSi piensa que es un error, por favor anote los datos y avise al administrador del sistema ..." };		
			agregarMensaje("62",mensaje,params,FGPException.WARN);
			logger.info(mensaje);
			ret=null;
		}
				
		return ret;
		
	}
	
	// cancela las modificaciones ( no guarda ) y desbloquea el paso
	public String cancelar()  {
		logger.debug("pasoBean:cancelar ["+pasoActual.getId()+"]");
		
		try {
			_unlockCurrentStepWork();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(" error trying unlock wstep:"+e.getMessage()+" - "+e.getCause()) ;
		} 
		
		setMensajeproceso();
		
		_limpiarVariablesDelBean();
		
		paginaAnexa=null;
		paginaProceso=PAGINA_PROCESO_CANCEL_DEFAULT;

		return PROCESAR_PAGINA_BIENVENIDA; // VA A LA PAGINA DE BIENVENIDA DEL MOTOR DE PROCESAMIENTO DE TAREAS ...
		
	}
	
	// desbloquea el paso corriente ...
	public String desbloquear()  {
		logger.debug("pasoBean:desbloquear ["+pasoActual.getId()+"]");
	
		try {
			_unlockCurrentStepWork();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(" error trying unlock wstep:"+e.getMessage()+" - "+e.getCause()) ;
		} 
		
		setMensajeproceso();
		
		_limpiarVariablesDelBean();
		
		paginaAnexa=null;
		paginaProceso=PAGINA_PROCESO_CANCEL_DEFAULT;

		return PROCESAR_PAGINA_BIENVENIDA; // VA A LA PAGINA DE BIENVENIDA DEL MOTOR DE PROCESAMIENTO DE TAREAS ...
		
	}
	
	
	// prueba nes 20110112
	public void renderizaCampos() {
		System.out.println("---------->>> renderizaCampos <<<-------------");
	}

	private boolean controlPasoHacambiadoEstado() {
		boolean procesado=false;

		try {
			new WStepWorkBL().checkStatus(pasoActual.getId(), usuarioLogueado, false);
		} catch (WStepAlreadyProcessedException e) {
			procesado=true;
		} 

		return procesado;
	}

	private void setMensajeproceso() {
		// nes 20120120 - agregado objeto wProcessWork
		this.mensajeProceso=	this.pasoActual.getwProcessWork().getReference()+" - "
				+ pasoActual.getwProcessWork().getComments()+ "\n ("
				+ pasoActual.getCurrentStep().getName()+") \n ("
				+ pasoActual.getCurrentStep().getStepComments()+ " )";
	}	
	
	private void cargaRespuestasCombo() {
		
		if ( this.pasoActual.getCurrentStep().getResponse().size()>0 ) {
			
			lRespuestasCombo.add( new SelectItem(null,"Seleccione una respuesta ..."));
			
			for (WStepResponseDef resp: pasoActual.getCurrentStep().getResponse()) {
				lRespuestasCombo.add(new SelectItem(resp.getId(),resp.getName()));
			}
			//rrl 20110112: El primer valor del combo es el valor por defecto.
//			if (this.idRespuesta == null) {
//				this.idRespuesta = Integer.parseInt(lRespuestasCombo.get(0).getValue().toString());
//			}

		} else {
			lRespuestasCombo=null;
		}
		
		// porque en el xhtml no agarra la propiedad de la lista, hay q tener getter y setter ...
		this.hayRespuestas= (lRespuestasCombo!=null ? ! lRespuestasCombo.isEmpty(): false) ; // nes 20111222 
	}
	
	private List<WStepWork> _getListActiveSteps() throws WStepWorkException {

		try {
			
			return new WStepWorkBL().getActiveSteps(idObject, idObjectType, usuarioLogueado);
			
		} catch (WProcessDefException e) {
			
			String message = "setPaso: WProcessDefException: no se pudo obtener la lista de pasos para el idObjeto:"+idObject+" con tipo:"+idObjectType;
			logger.error(message);
			idStepWork=0;


		} catch (WStepDefException e) {
			
			String message = "setPaso: WStepDefException: no se pudo obtener la lista de pasos para el idProyecto:"+idObject+" con tipo:"+idObjectType;
			logger.error(message);
			idStepWork=0;

		}
		
		return null;
	}

	private void _setCurrentStepWork() 
	throws WStepLockedByAnotherUserException, WStepWorkException, CantLockTheStepException, WUserDefException {
		
		try {
			
			pasoActual= new WStepWorkBL().getStepWithLock(idStepWork, usuarioLogueado);
			this.cargaRespuestasCombo();
	
		} catch (CantLockTheStepException e) {
			System.out.println("WStepNotLockedException:"+idStepWork);
			idStepWork=0;
			throw e;
		} catch (WStepLockedByAnotherUserException e) {
			System.out.println("WStepLockedByAnotherUserException:"+idStepWork);
			idStepWork=0;
			throw e;
		} catch (WStepWorkException e) {
			System.out.println("WStepWorkException:"+idStepWork);
			idStepWork=0;
			throw e;
		} catch (WUserDefException e) {
			System.out.println("WStepWorkException:"+idStepWork);
			idStepWork=0;
			throw e;
		} 
		
		// setea el submit para hacer submit automatico del form de trabajo junto con el procesar ( o guardar )
		if ( pasoActual.getCurrentStep().getSubmitForm()!=null ){
			this.setSubmitForm(	"document."
							+pasoActual.getCurrentStep().getSubmitForm()
							+".submit();"); // form a submitear automaticamente desde la vista 20110214
		} else {
			this.setSubmitForm(null);
		}

		// si currentStep ( wstepdef ) tiene indicador uso ese ...
		if (pasoActual.getCurrentStep().getIdWorkZone()!=null && 
				! "".equals(pasoActual.getCurrentStep().getIdWorkZone()) ) {

			if ( ! NONE.equals(pasoActual.getCurrentStep().getIdWorkZone())) {
				paginaProceso=pasoActual.getCurrentStep().getIdWorkZone();
			} else {
				paginaProceso=null;
			}
			
		// si no el del proceso			
		} else if (pasoActual.getProcess().getIdWorkZone()!=null && 
				! "".equals(pasoActual.getProcess().getIdWorkZone())) {
			
			paginaProceso=pasoActual.getProcess().getIdWorkZone();
			
		// y si tampoco uso el default			
		} else { 
			
			paginaProceso=PAGINA_PROCESO_DEFAULT;
		
		}
		
		// si currentStep ( wstepdef ) tiene indicador uso ese ...
		if (pasoActual.getCurrentStep().getIdListZone()!=null && 
				! "".equals(pasoActual.getCurrentStep().getIdListZone()) ) {
			
			if (! NONE.equals(pasoActual.getCurrentStep().getIdListZone())) {
				paginaLista=pasoActual.getCurrentStep().getIdListZone();
			} else {
				paginaProceso=null;				
			}
			
		// si no el del proceso
		} else if (pasoActual.getProcess().getIdListZone()!=null && 
				! "".equals(pasoActual.getProcess().getIdListZone())) {
			
			paginaLista=pasoActual.getProcess().getIdListZone();
			
		// y si tampoco uso el default
		} else { 
			
			paginaLista=PAGINA_LISTA_DEFAULT;
		
		}

		
		if (pasoActual.getCurrentStep().getIdAdditionalZone()!=null && 
				! "".equals(pasoActual.getCurrentStep().getIdAdditionalZone()) ) {

			if (! NONE.equals(pasoActual.getCurrentStep().getIdAdditionalZone()) ) {
				paginaAnexa=pasoActual.getCurrentStep().getIdAdditionalZone();
			} else {
				paginaAnexa=null;				
			}
			
		} else if (pasoActual.getProcess().getIdAdditionalZone()!=null && 
				! "".equals(pasoActual.getProcess().getIdAdditionalZone())) {
			
			paginaAnexa=pasoActual.getProcess().getIdAdditionalZone();
			
		} else { // setea la página por defecto que tengamos en el proyecto (Constantes)
			
			paginaAnexa=PAGINA_ANEXA_DEFAULT;
		
		}
	}
	
	private void _unlockCurrentStepWork() 
	throws WStepLockedByAnotherUserException, WStepWorkException, CantLockTheStepException {
		
		
			if ( pasoActual!=null ) { // 

				try {
					new WStepWorkBL().unlockStep(pasoActual.getId(), usuarioLogueado, usuarioIsAdmin);
				} catch (WStepNotLockedException e) {
					logger.info("step was not locked ... ");
				}

			}			

	}
	
	
	private void _executeSaveActionInRelatedBean( Date now ) {
		
		//TODO IMPLEMENTAR ...
	
	}
	
	private void _setCurrentWorkitemToProcessed( Date now ) throws WStepWorkException, WUserDefException {
		
		pasoActual.setDecidedDate(now);
		pasoActual.setPerformer(new WUserDefBL().getWUserDefByPK(usuarioLogueado));
		pasoActual.setLocked(false);
		pasoActual.setLockedBy(null);
		pasoActual.setLockedSince(null);
		if ( lRespuestasCombo!= null && lRespuestasCombo.size()>0 ) { // nes 20121222
			pasoActual.setResponse(idRespuesta.toString());
		} else {
			pasoActual.setResponse("no responses list");
		}
		
		new WStepWorkBL().update(pasoActual, usuarioLogueado); // actualiza el paso actual a "ejecutado" y lo desbloquea
		
	}
	
	
	
	private boolean _startNewWorkitems( 
			List<WStepSequenceDef> routes, Date now ) 
	throws WStepDefException, WStepWorkException {

		boolean ret = false;
		boolean sentBackFalse=false;
		
		if ( routes.size()==0 ) { // no next steps - this tree ends here ...
		
			ret = false; // tree finished ...

		} else {
			
			for ( WStepSequenceDef route: routes) { // for each route
				
				if ( route.getToStep()!=null ) {
					
					if ( ( route.getValidResponses()!=null && 
								route.getValidResponses().contains(idRespuesta.toString().trim()+"|") ) 
							||
							route.isAfterAll() ) {

						_setNextStep(now, route, sentBackFalse);
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

	
	private boolean _startReturnedWorkitem( 
			Date now ) 
	throws WStepDefException, WStepWorkException {

		boolean ret = false;
		boolean sentBack=true;
			

			
		if ( pasoActual.getPreviousStep()!=null ) {
			
			// creo la ruta de devolución:
			WStepSequenceDef route = 
				new WStepSequenceDef(
						pasoActual.getProcess(), pasoActual.getVersion(), 
						pasoActual.getCurrentStep(), pasoActual.getPreviousStep(), 
						true, true, null);

			_setNextStep(now, route, sentBack);

			ret=true;
		}
				

		
		return ret;
	}	
	
	private void _setNextStep(Date now, WStepSequenceDef route, boolean sentBack)
			throws WStepDefException, WStepWorkException {

		WStepWork newWorkItem;
		
		newWorkItem = new WStepWork();
		
		newWorkItem.setAdminProcess( adminProcess );
		
		newWorkItem.setProcess( pasoActual.getProcess() );
		newWorkItem.setVersion( pasoActual.getVersion() );
		newWorkItem.setPreviousStep( pasoActual.getCurrentStep() );
		
		newWorkItem.setCurrentStep( new WStepDefBL().getWStepDefByPK(route.getToStep().getId(), usuarioLogueado));
		
		// nes 20120120 - agregado objeto wProcessWork
		newWorkItem.getwProcessWork().setReference(pasoActual.getwProcessWork().getReference() );
		newWorkItem.getwProcessWork().setComments(pasoActual.getwProcessWork().getComments() );
		newWorkItem.setArrivingDate( now );
		
		// put user instructions to next step
		if ( pasoActual.isSendUserNotesToNextStep() ) {
			newWorkItem.setUserInstructions(pasoActual.getUserNotes());
		}
		
//					newWorkItem.setOpenedDate = pasoActual.openedDate;
//					newWorkItem.openerUser = pasoActual.openerUser;
//					newWorkItem.decidedDate = pasoActual.decidedDate;
//					newWorkItem.performer = pasoActual.performer;
//					newWorkItem.response = pasoActual.response;
//					newWorkItem.nextStepInstructions = pasoActual.nextStepInstructions;

		// this values may be changed by user ( depending permissions and security )
		newWorkItem.setTimeUnit( ( this.sTimeUnit==null? newWorkItem.getTimeUnit(): this.sTimeUnit) );
		newWorkItem.setAssignedTime( ( this.sAssignedTime==null? newWorkItem.getAssignedTime(): this.sAssignedTime) );
		newWorkItem.setDeadlineDate( ( this.sDeadlineDate==null? newWorkItem.getDeadlineDate(): this.sDeadlineDate) );
		newWorkItem.setDeadlineTime( ( this.sDeadlineTime==null? newWorkItem.getDeadlineTime(): this.sDeadlineTime) );
		newWorkItem.setReminderTimeUnit( ( this.sReminderTimeUnit==null? newWorkItem.getReminderTimeUnit(): this.sReminderTimeUnit) );
		newWorkItem.setReminderTime( ( this.sReminderTime==null? newWorkItem.getReminderTime(): this.sReminderTime) );


		
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
			
			new WStepWorkBL().add(newWorkItem, usuarioLogueado);

		} catch (WStepWorkException e) {
			
			String mensaje = "_setNextStep: Error al intentar generar el nuevo paso : "
				+e.getMessage()+" - "+e.getCause();
			logger.error(mensaje);
			throw new WStepWorkException( mensaje );
		
		}
		
		
	}
	
	
	/**
	 * @return the idObject
	 */
	public Integer getIdObject() {
		return idObject;
	}

	/**
	 * @param idObject the idObject to set
	 */
	public void setIdObject(Integer idObject) {
		this.idObject = idObject;
	}

	/**
	 * @return the idObjectType
	 */
	public String getIdObjectType() {
		return idObjectType;
	}

	/**
	 * @param idObjectType the idObjectType to set
	 */
	public void setIdObjectType(String idObjectType) {
		this.idObjectType = idObjectType;
	}

	/**
	 * @return the pasoActual
	 */
	public WStepWork getPasoActual() {
		return pasoActual;
	}

	/**
	 * @param pasoActual the pasoActual to set
	 */
	public void setPasoActual(WStepWork pasoActual) {
		this.pasoActual = pasoActual;
	}

	/**
	 * @return the paginaProceso
	 */
	public String getPaginaProceso() {
		return paginaProceso;
	}

	/**
	 * @param paginaProceso the paginaProceso to set
	 */
	public void setPaginaProceso(String paginaProceso) {
		this.paginaProceso = paginaProceso;
	}

	/**
	 * @return the paginaLista
	 */
	public String getPaginaLista() {
		return paginaLista;
	}

	/**
	 * @param paginaLista the paginaLista to set
	 */
	public void setPaginaLista(String paginaLista) {
		this.paginaLista = paginaLista;
	}

	/**
	 * @return the paginaAnexa
	 */
	public String getPaginaAnexa() {
		return paginaAnexa;
	}

	/**
	 * @param paginaAnexa the paginaAnexa to set
	 */
	public void setPaginaAnexa(String paginaAnexa) {
		this.paginaAnexa = paginaAnexa;
	}

	/**
	 * @return the lrespuestas
	 */
	public List<SelectItem> getlRespuestasCombo() {
		return lRespuestasCombo;
	}



	/**
	 * @return the idStepWork
	 */
	public Integer getIdStepWork() {
		return idStepWork;
	}

	/**
	 * @param idStepWork the idStepWork to set
	 */
	public void setIdStepWork(Integer idStepWork) {
		this.idStepWork = idStepWork;
	}


	/**
	 * @param lRespuestas the lRespuestas to set
	 * 
	 * TODO: REVISAR ESTO DESPUES TENGO DUDA SI ES CORRECTO HACERLO ASI O HAY QUE HACERLO EN EL GET ...
	 */
	public void setlRespuestasCombo(List<SelectItem> lRespuestasCombo) {
		this.lRespuestasCombo = lRespuestasCombo;
	}
	public List<WStepWork> getlPasosVivos() {
		return lPasosVivos;
	}
	public void setlPasosVivos(List<WStepWork> lPasosVivos) {
		this.lPasosVivos = lPasosVivos;
	}
	public Integer getIdRespuesta() {
		return idRespuesta;
	}

	public void setIdRespuesta(Integer idRespuesta) {
		this.idRespuesta = idRespuesta;
	}
	public Integer getUsuarioLogueado() {
		return usuarioLogueado;
	}


	public String getNombreBean() {
		return nombreBean;
	}

	public void setNombreBean(String nombreBean) {
		this.nombreBean = nombreBean;
	}

	public String getNombreBeanEL() {
		return nombreBeanEL;
	}

	public void setNombreBeanEL(String nombreBeanEL) {
		this.nombreBeanEL = nombreBeanEL;
	}

	private void _setLoggedUser() {
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);
		if (cs!=null) {			usuarioLogueado= cs.getUsuario().getIdUsuario();
//			usuarioIsAdmin=cs.getUsuario().getAdmon()=="S"; // nes 20110107	
		} else { 
			usuarioLogueado=null;
//			usuarioIsAdmin=false; // nes 20110107
		}
		
	}

	public String getInstruccionesAlProximoPaso() {
		return instruccionesAlProximoPaso;
	}

	public void setInstruccionesAlProximoPaso(String instruccionesAlProximoPaso) {
		this.instruccionesAlProximoPaso = instruccionesAlProximoPaso;
	}

	public WTimeUnit getsTimeUnit() {
		return sTimeUnit;
	}

	public void setsTimeUnit(WTimeUnit sTimeUnit) {
		this.sTimeUnit = sTimeUnit;
	}

	public Integer getsAssignedTime() {
		return sAssignedTime;
	}

	public void setsAssignedTime(Integer sAssignedTime) {
		this.sAssignedTime = sAssignedTime;
	}

	public Date getsDeadlineDate() {
		return sDeadlineDate;
	}

	public void setsDeadlineDate(Date sDeadlineDate) {
		this.sDeadlineDate = sDeadlineDate;
	}

	public Date getsDeadlineTime() {
		return sDeadlineTime;
	}

	public void setsDeadlineTime(Date sDeadlineTime) {
		this.sDeadlineTime = sDeadlineTime;
	}

	public WTimeUnit getsReminderTimeUnit() {
		return sReminderTimeUnit;
	}

	public void setsReminderTimeUnit(WTimeUnit sReminderTimeUnit) {
		this.sReminderTimeUnit = sReminderTimeUnit;
	}

	public Integer getsReminderTime() {
		return sReminderTime;
	}

	public void setsReminderTime(Integer sReminderTime) {
		this.sReminderTime = sReminderTime;
	}

	public void setUsuarioLogueado(Integer usuarioLogueado) {
		this.usuarioLogueado = usuarioLogueado;
	}

	public boolean isAdminProcess() {
		return adminProcess;
	}

	public void setAdminProcess(boolean adminProcess) {
		this.adminProcess = adminProcess;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	public boolean isHayRespuestas() {
		return hayRespuestas;
	}

	public void setHayRespuestas(boolean hayRespuestas) {
		this.hayRespuestas = hayRespuestas;
	}

	public String getMensajeProceso() {
		return mensajeProceso;
	}

	public void setMensajeProceso(String mensajeProceso) {
		this.mensajeProceso = mensajeProceso;
	}

	public String getClickAutomatico() {
		return clickAutomatico;
	}

	public void setClickAutomatico(String clickAutomatico) {
		this.clickAutomatico = clickAutomatico;
	}

	public String getSubmitForm() {
		return submitForm;
	}

	public void setSubmitForm(String submitForm) {
		this.submitForm = submitForm;
	}

	//rrl 20111220
	public boolean isInstruccionesYComentariosEnabled() {
		
		instruccionesYComentariosEnabled=false;

		// Si vienen datos activa Instrucciones y comentarios para el paso siguiente
		if (pasoActual!=null && pasoActual.isMyNotes() ) {
			if (pasoActual.isMyNotes() || pasoActual.isSendUserNotesToNextStep() || 
					(pasoActual.getUserNotes()!=null && !"".equals(pasoActual.getUserNotes()))) {
				
				instruccionesYComentariosEnabled=true;
				
			}
		}
		
		return instruccionesYComentariosEnabled;
	}

	public void setInstruccionesYComentariosEnabled(
			boolean instruccionesYComentariosEnabled) {
		this.instruccionesYComentariosEnabled = instruccionesYComentariosEnabled;
	}
		
	
}
