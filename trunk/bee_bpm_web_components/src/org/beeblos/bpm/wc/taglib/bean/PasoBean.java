package org.beeblos.bpm.wc.taglib.bean;


import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;
import static org.beeblos.bpm.core.util.Constants.NONE;
import static org.beeblos.bpm.core.util.Constants.PROCESS_STEP;
import static org.beeblos.bpm.core.util.Constants.TURNBACK_STEP;
import static org.beeblos.bpm.core.util.Constants.PAGINA_ANEXA_DEFAULT;
import static org.beeblos.bpm.core.util.Constants.PAGINA_LISTA_DEFAULT;
import static org.beeblos.bpm.core.util.Constants.PAGINA_PROCESO_CANCEL_DEFAULT;
import static org.beeblos.bpm.core.util.Constants.PAGINA_PROCESO_DEFAULT;
import static org.beeblos.bpm.core.util.Constants.PAGINA_PROCESO_OK_DEFAULT;
import static org.beeblos.bpm.core.util.Constants.PROCESADOR_PASO_DEFAULT;
import static org.beeblos.bpm.core.util.Constants.TASK_PROCESSED_OK;
import static org.beeblos.bpm.core.util.Constants.PROCESS_TASK;
import static org.beeblos.bpm.core.util.Constants.WELCOME_PAGE;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WStepWorkBL;
import org.beeblos.bpm.core.error.CantLockTheStepException;
import org.beeblos.bpm.core.error.CustomSaveException;
import org.beeblos.bpm.core.error.CustomValidationException;
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
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WTimeUnit;
import org.beeblos.bpm.core.model.noper.WRuntimeSettings;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.HelperUtil;


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
	
	private String procesadorPaso;
	
	private String submitForm;
	
	private String mensajeProceso;
	
	//rrl 20111220
	private boolean instruccionesYComentariosEnabled;
	
	//rrl 20130807
	private String buttonPress;

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
		this.procesadorPaso=PROCESADOR_PASO_DEFAULT;
		this.clickAutomatico="document.step_process_form.submit();";
		
	}

	// nes 20101003
	public String irAlMenuPrincipal(){
		return WELCOME_PAGE;
	}
	
	// setea el paso en el que se va a trabajar
	public void setPaso() 
	throws CantLockTheStepException, WStepLockedByAnotherUserException, WStepWorkException {
		logger.debug("setPaso ["+(pasoActual!=null?pasoActual.getId():"null")+"]");
		

		paginaProceso=PAGINA_PROCESO_DEFAULT;
		procesadorPaso=PROCESADOR_PASO_DEFAULT;
	
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
//TODO: COMENTAR BIEN PARA QUE SIRVE ESTA PARTE DEL IF ... Y LA DE ABAJO TAMBIÉN ...
			
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

	//rrl 20120619
	public boolean isCustomSave() {
		logger.debug("isCustomSave ["+(pasoActual!=null?pasoActual.getId():"null")+"]");
		
		boolean result=false;

		//rrl 20120622
		String claseAInvocar = pasoActual.getCurrentStep().getCustomSaveRefClass();
		String metodoAInvocar = pasoActual.getCurrentStep().getCustomSaveMethod();

		if (claseAInvocar!=null && !"".equals(claseAInvocar) &&
				metodoAInvocar!=null && !"".equals(metodoAInvocar)) {
			result=true;
		}
		
		return result;
	}
	
	//rrl 20120619
	public boolean customSave() throws CustomSaveException {
		logger.debug("customSave ["+(pasoActual!=null?pasoActual.getId():"null")+"]");
		
		boolean result=false;
		
		// Invocar el metodo por introspeccion
		//rrl 20120622
		String claseAInvocar = pasoActual.getCurrentStep().getCustomSaveRefClass();
		String metodoAInvocar = pasoActual.getCurrentStep().getCustomSaveMethod();
			
		try {

			System.out.println("### TRAZA: PasoBean.customSave(): Invoca el metodo por introspeccion. clase="+claseAInvocar+" - metodo="+metodoAInvocar);
			String beanName = claseAInvocar.substring(claseAInvocar.lastIndexOf(".")+1);
			beanName = Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1);

			Object objValidation = HelperUtil.getBeanFromSession(beanName);
			
			if (objValidation != null) {
				System.out.println("### TRAZA: PasoBean.customSave(): obtiene de session beanName="+beanName);
				
				objValidation.getClass()
					.getMethod(metodoAInvocar, null)
					.invoke(objValidation, null);

				System.out.println("### TRAZA: PasoBean.customSave(): Invoca el metodo CON EXITO");
				
				result=true;
				
			} else {
				
				System.out.println("### TRAZA: PasoBean.customSave(): NO OBTIENE de session beanName="+beanName);
				
			}
			
		} catch (IllegalAccessException e) {
			logger.error("customSave: IllegalAccessException: Intento de acceso ilegal a la clase [" +claseAInvocar+"]  ..."+e.getMessage()+" "+e.getCause());
			e.printStackTrace();
		} catch (SecurityException e) {
			logger.error("customSave: SecurityException al acceder a la clase [" +claseAInvocar+"]  ..."+e.getMessage()+" "+e.getCause());			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			logger.error("customSave: IllegalArgumentException accediendo al metodo:["+metodoAInvocar+"] de la clase [" +claseAInvocar+"]  ..."+e.getMessage()+" "+e.getCause());			e.printStackTrace();
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			logger.error("customSave: InvocationTargetException accediendo al metodo:["+metodoAInvocar+"] de la clase [" +claseAInvocar+"]  ..."+e.getMessage()+" "+e.getCause());			e.printStackTrace();
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			logger.error("customSave: NoSuchMethodException no existe el metodo:["+metodoAInvocar+"] de la clase [" +claseAInvocar+"]  ..."+e.getMessage()+" "+e.getCause());			e.printStackTrace();
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean customValidationOK() throws CustomValidationException   {
		logger.debug("pasoBean:customValidationOK ["+(pasoActual!=null?pasoActual.getId():"null")+"]");

		boolean result=false;
		
		if ( !pasoActual.getCurrentStep().isCustomValidation()) { 
			throw new CustomValidationException("No custom validation is defined in step ...");
		}
			
		if (pasoActual.getCurrentStep().getCustomValidationRefClass() == null ||
				"".equals(pasoActual.getCurrentStep().getCustomValidationRefClass()) ) {
			throw new CustomValidationException("No reference class for custom validation is defined in step ...");
		}


		if ( pasoActual.getCurrentStep().getCustomValidationMethod() == null ||
				"".equals(pasoActual.getCurrentStep().getCustomValidationMethod()) ) {
			throw new CustomValidationException("No validation method for custom validation is defined in step ...");
		}

		String claseAInvocar = pasoActual.getCurrentStep().getCustomValidationRefClass();
		String metodoAInvocar = pasoActual.getCurrentStep().getCustomValidationMethod();
		
	   	try {
	   		
			Class<?> validationClass;
			Object objValidation = new Object();
	   		
	   		validationClass = Class.forName(pasoActual.getCurrentStep().getCustomValidationRefClass());
			objValidation = validationClass.newInstance();

			objValidation = getObject(validationClass);
			
			result = 
				(Boolean) objValidation.getClass()
								.getMethod(pasoActual.getCurrentStep().getCustomValidationMethod(), null)
								.invoke(objValidation, null);
			

		} catch (ClassNotFoundException e) {
			logger.error("customValidationOK: ClassNotFoundException: La clase invocada [" +claseAInvocar+"] no existe ...");
			e.printStackTrace();
		} catch (InstantiationException e) {
			logger.error("customValidationOK: InstantiationException: No se puede instanciar la clase [" +claseAInvocar+"]  ..."+e.getMessage()+" "+e.getCause());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.error("customValidationOK: IllegalAccessException: Intento de acceso ilegal a la clase [" +claseAInvocar+"]  ..."+e.getMessage()+" "+e.getCause());
			e.printStackTrace();
		} catch (SecurityException e) {
			logger.error("customValidationOK: SecurityException al acceder a la clase [" +claseAInvocar+"]  ..."+e.getMessage()+" "+e.getCause());			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			logger.error("customValidationOK: IllegalArgumentException accediendo al metodo:["+metodoAInvocar+"] de la clase [" +claseAInvocar+"]  ..."+e.getMessage()+" "+e.getCause());			e.printStackTrace();
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			logger.error("customValidationOK: InvocationTargetException accediendo al metodo:["+metodoAInvocar+"] de la clase [" +claseAInvocar+"]  ..."+e.getMessage()+" "+e.getCause());			e.printStackTrace();
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			logger.error("customValidationOK: NoSuchMethodException no existe el metodo:["+metodoAInvocar+"] de la clase [" +claseAInvocar+"]  ..."+e.getMessage()+" "+e.getCause());			e.printStackTrace();
			e.printStackTrace();
		}

		return result;
		

	}

	
	private Object getObject( Class validationClass ) {


		// 1ero verifico si la clase es un bean que está en session, si lo es lo devuelvo ...
		String nombreBean =  validationClass.getName();
		nombreBean = nombreBean.substring(nombreBean.lastIndexOf(".")+1);
		String nombreBeanEL = "#{" + nombreBean.substring(0,1).toLowerCase()+nombreBean.substring(1)+"}";
			
		ValueExpression valueBinding = super.getValueExpression(nombreBeanEL);
			
		if (valueBinding != null) { 
			
			return 
					( Object ) valueBinding
							.getValue(super.getELContext());

			
		} else {
			return new Object();
		}
		
	}
		
	
	public String procesar()   {
		
		logger.debug("pasoBean:procesar ["+(pasoActual!=null?pasoActual.getId():"null")+"]");
		
		String ret = PROCESS_TASK;
		//Date now = new Date(); // ( date/time )
		
		if (lRespuestasCombo!=null && lRespuestasCombo.size()>0 ) {
			if (idRespuesta==null || idRespuesta==0) {
				
				String message = "Debe indicar una respuesta válida para que se pueda procesar el paso";
				super.createWindowMessage(ERROR_MESSAGE, message, null);
				logger.info(message);
				return PROCESS_TASK;
				
			}
		}
		
		if  ( controlPasoHacambiadoEstado() ) {
			String message = "Este paso ha cambiado el estado mientras usted lo tenia en procesamiento. Avise al administrador id:["
							+pasoActual.getId()+"]";
			super.createWindowMessage(ERROR_MESSAGE, message, null);
			logger.info(message);
			return PROCESS_TASK;
			
		}
		
		try {

			//rrl 20120619 si existe guardar personalizado, se ejecuta
			if ( isCustomSave() ) { 
				if ( ! customSave() ) {
					String message = "Error al intentar guardar personalizado el paso actual ...";
					super.createWindowMessage(ERROR_MESSAGE, message, null);
					logger.info(message);
					return PROCESS_TASK;					
				}
			}
			
			// si se definió una custom validation la ejecuta y si no valida sale con error
			if ( pasoActual.getCurrentStep().isCustomValidation()) { 
				if ( ! customValidationOK() ) {
					String message = "Faltan completar datos para poder procesar el paso. Por favor revise las tareas requeridas y asegurese que ha realizado todo correctamente ...";
					super.createWindowMessage(ERROR_MESSAGE, message, null);
					logger.info(message);
					return PROCESS_TASK;					
				}
			}
			
			
			// dml 20120308 - creo el runtimeSettings con la información del PasoBean necesaria para
			// procesar el WStepWork
			WRuntimeSettings runtimeSettings = 
					new WRuntimeSettings((pasoActual.isSendUserNotesToNextStep())?pasoActual.getUserNotes():null, 
					null, this.pasoActual.getManagedData(), this.sTimeUnit, this.sAssignedTime, this.sDeadlineDate, this.sDeadlineTime, 
					this.sReminderTimeUnit, this.sReminderTime);
			
			// dml 20120308 - Procesa el WStepWork y devuelve el numero de nuevas rutas lanzadas
			Integer cantidadNuevasRutasLanzadas = 
					new WStepWorkBL()
						.processStep(idStepWork, idRespuesta, runtimeSettings, usuarioLogueado, adminProcess,
									PROCESS_STEP);
			
			System.out.println("WSTEPWORK "+idStepWork+" PROCESADO CORRECTAMENTE");
			System.out.println("- "+cantidadNuevasRutasLanzadas+" nuevas rutas creadas.");
			

			setMensajeproceso();
			
			_limpiarVariablesDelBean();
			
			paginaAnexa=null;
			paginaProceso=PAGINA_PROCESO_OK_DEFAULT;
			ret = TASK_PROCESSED_OK; // VA A LA PAGINA DE BIENVENIDA DEL MOTOR DE PROCESAMIENTO DE TAREAS ...

		} catch (WStepSequenceDefException e) {

			String message = "Error al intentar obtener la lista de rutas para avanzar al siguiente paso: "
							+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);
			ret=null;

		} catch (WStepWorkException e) {

			String message = "Error al intentar establecer el paso actual como 'procesado' : "
				+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);
			ret=null;

		} catch (WStepDefException e) {
			
			String message = "Error al intentar establecer el próximo paso - No se puede leer la definición de la base de datos ... : "
				+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);
			ret=null;

		} catch (WStepLockedByAnotherUserException e) {

			String message = "El paso está bloqueado por otro usuario."
				+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);
			ret=null;

		} catch (WStepAlreadyProcessedException e) {

			String message = "Esta tarea ya se ha procesado. Si piensa que es un error, por favor anote los datos y avise al administrador del sistema ...";		
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);
			ret=null;
		} catch (CustomValidationException e) {
			String message = "Error al intentar invocar en el paso actual el metodo customSaveMethod definido en la base de datos ... : "
					+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);
			ret=null;
		} catch (WProcessDefException e) {
			String message = "Error al intentar invocar en el paso actual el metodo customSaveMethod definido en la base de datos ... : "
					+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);
			ret=null;
		} catch (WStepNotLockedException e) {
			String message = "Error al intentar invocar en el paso actual el metodo customSaveMethod definido en la base de datos ... : "
					+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);
			ret=null;
		} catch (WUserDefException e) {
			String message = "Error al intentar invocar en el paso actual el metodo customSaveMethod definido en la base de datos ... : "
					+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);
			ret=null;
		} catch (CustomSaveException e) {
			String message = "Error al intentar invocar en el paso actual el metodo customSaveMethod definido en la base de datos ... : "
				+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);
			ret=null;
		} catch (WStepWorkSequenceException e) {
			String message = "Error al intentar invocar en el paso actual el metodo customSaveMethod definido en la base de datos ... : "
				+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);
			ret=null;
		} catch (WProcessWorkException e) {
			String message = "Error al intentar invocar en el paso actual el metodo customSaveMethod definido en la base de datos ... : "
					+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);
			ret=null;
		}
				
		return ret;
	}

	// se corresponde con la acción del botón "guardar"
	// guarda los datos ingresados por el usuario en el step y desbloquea el paso
	public String guardar()  {
		logger.debug("pasoBean:guardar ["+(pasoActual!=null?pasoActual.getId():"null")+"]");
		
		String ret = PROCESS_TASK;
		
		try {

			//rrl 20120619 si existe guardar personalizado, se ejecuta
			if ( isCustomSave() ) { 
				if ( ! customSave() ) {
					String message = "Error al intentar guardar personalizado el paso actual. Por favor, anote la referencia de la tarea y avise a soporte.";		
					super.createWindowMessage(ERROR_MESSAGE, message, null);
					logger.info(message);
					return PROCESS_TASK;					
				}
			}

			new WStepWorkBL().update(pasoActual, usuarioLogueado);
			
			setMensajeproceso();
			
			_limpiarVariablesDelBean(); // nes 20111219
			
			paginaAnexa=null;
			paginaProceso=PAGINA_PROCESO_OK_DEFAULT;
			
			ret =  TASK_PROCESSED_OK; 
			
		} catch (WStepWorkException e) {
			
			String message = "PasoBean:guardar WStepWorkException Error al intentar guardar el paso: "
					+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (CustomSaveException e) {
			e.printStackTrace();
			String message = "PasoBean:guardar CustomSaveException Error al intentar invocar en el paso actual el metodo customSaveMethod definido en la base de datos ... : "
				+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
		System.out.println("---------->>> guardar <<<-------------");

		return ret; // nes 20111213
		
	}


	// devuelve el paso al que lo envió
	public String devolver()  {
		logger.debug("pasoBean:devolver ["+(pasoActual!=null?pasoActual.getId():"null")+"]");
		
		//rrl y nestor 20111222
		if ( pasoActual.getPreviousStep()==null || pasoActual.getPreviousStep().getId()==null || 
                 pasoActual.getPreviousStep().getId().equals(0) ) {
	        String message = "Error no se puede devolver porque no hay paso previo en el workflow  id:["+pasoActual.getId()+"] . Por favor, anote la referencia de la tarea y avise a soporte." ;                
			super.createWindowMessage(ERROR_MESSAGE, message, null);
           	return PROCESS_TASK;
		}

		String ret = PROCESS_TASK;

//		Date now = new Date(); // ( date/time )
		
		try {
/*			
			_executeSaveActionInRelatedBean( now );
			
			new WStepWorkBL().checkLock(idStepWork, usuarioLogueado, false); // verifies the user has the step locked befor process it ...
			new WStepWorkBL().checkStatus(idStepWork, usuarioLogueado, false); // verifies step is process pending at this time ...
			
			_setCurrentWorkitemToProcessed( now );
			
			_startReturnedWorkitem( now ); 

			// set bean to show message and refresh list
			// antes de limpiar cargo el message que voy a mostrar
			this.mensajeProceso=	this.pasoActual.getwProcessWork().getReference()+" - "
									+ pasoActual.getwProcessWork().getComments()+ "\n ("
									+ pasoActual.getCurrentStep().getName()+") \n ("
									+ pasoActual.getCurrentStep().getStepComments()+ " )"
									+ " fué devuelto al paso anterior : "+pasoActual.getPreviousStep().getName();
*/
			
			
			
			// dml 20120308 - creo el runtimeSettings con la información del PasoBean necesaria para
			// procesar el WStepWork
			WRuntimeSettings runtimeSettings = 
					new WRuntimeSettings((pasoActual.isSendUserNotesToNextStep())?pasoActual.getUserNotes():null, 
					null, this.pasoActual.getManagedData(), this.sTimeUnit, this.sAssignedTime, this.sDeadlineDate, this.sDeadlineTime, 
					this.sReminderTimeUnit, this.sReminderTime);
			
			// dml 20120308 - Procesa el WStepWork y devuelve el numero de nuevas rutas lanzadas
			Integer nuevasRutasLanzadas = new WStepWorkBL().processStep(idStepWork, idRespuesta, /*comments,*/ 
					runtimeSettings, /*idProcess, idObject, ret,*/ usuarioLogueado, adminProcess,
					TURNBACK_STEP);
			
			// dml 20120308 - Esto deberia ser un 1 siempre
			System.out.println("WSTEPWORK "+idStepWork+" DEVUELTO CORRECTAMENTE");
			System.out.println("- "+nuevasRutasLanzadas+" nuevas rutas creadas.");

			
			// set bean to show message and refresh list
			// antes de limpiar cargo el message que voy a mostrar
			setMensajeproceso();
						
			_limpiarVariablesDelBean();
			
			paginaAnexa=null;
			paginaProceso=PAGINA_PROCESO_OK_DEFAULT;
			ret = TASK_PROCESSED_OK; // VA A LA PAGINA DE BIENVENIDA DEL MOTOR DE PROCESAMIENTO DE TAREAS ...


		} catch (WStepWorkException e) {

			String message = "Error al intentar establecer el paso actual como 'procesado' : "
				+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			ret=null;
		} catch (WStepDefException e) {
			
			String message = "Error al intentar establecer el próximo paso - No se puede leer la definición de la base de datos ... : "
				+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			ret=null;

		} catch (WStepLockedByAnotherUserException e) {

			String message = "El paso está bloqueado por otro usuario."
				+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			ret=null;

		} catch (WStepAlreadyProcessedException e) {

			String message = "Esta tarea ya se ha procesado ..."
				+e.getMessage()+" - "+e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			ret=null;
		} catch (WProcessDefException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WStepSequenceDefException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WStepNotLockedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WUserDefException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WStepWorkSequenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WProcessWorkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return ret;
		
	}
	
	// cancela las modificaciones ( no guarda ) y desbloquea el paso
	public String cancelar()  {
		logger.debug("pasoBean:cancelar ["+(pasoActual!=null?pasoActual.getId():"null")+"]");
		
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

		return TASK_PROCESSED_OK; // VA A LA PAGINA DE BIENVENIDA DEL MOTOR DE PROCESAMIENTO DE TAREAS ...
		
	}
	
	// desbloquea el paso corriente ...
	public String desbloquear()  {
		logger.debug("pasoBean:desbloquear ["+(pasoActual!=null?pasoActual.getId():"null")+"]");
	
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

		return TASK_PROCESSED_OK; // VA A LA PAGINA DE BIENVENIDA DEL MOTOR DE PROCESAMIENTO DE TAREAS ...
		
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
		} catch (WStepWorkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return procesado;
	}

	private void setMensajeproceso() {
		if (this.pasoActual!=null) {
		this.mensajeProceso=	this.pasoActual.getwProcessWork().getReference()+" - "
				+ pasoActual.getwProcessWork().getComments()+ "\n ("
				+ pasoActual.getCurrentStep().getName()+") \n ("
				+ pasoActual.getCurrentStep().getStepComments()+ " )";
		} else {
			this.mensajeProceso="current step is null ...";
		}
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

	//rrl 20120619
	private void _setCurrentStepWork() 
	throws WStepLockedByAnotherUserException, WStepWorkException, CantLockTheStepException {
		
		try {
			
			pasoActual= new WStepWorkBL().getStepWithLock(idStepWork, usuarioLogueado);
			idObject = pasoActual.getwProcessWork().getIdObject();
			idObjectType = pasoActual.getwProcessWork().getIdObjectType();
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
		} 
		
		// RAUL REVISAR ESTO QUE CARGA AQUI QUE DICE SUBMIT FORM A VER SI NOS PUEDE AFECTAR EN ALGO OK?
		// setea el submit para hacer submit automatico del form de trabajo junto con el procesar ( o guardar )
		
		//rrl 20120619 NESTOR: NO SE UTILIZA EN NINGUN SITIO este codigo (o almenos no lo encuentro)
		//
//		if ( pasoActual.getCurrentStep().getSubmitForm()!=null ){
//			this.setSubmitForm(	"document."
//							+pasoActual.getCurrentStep().getSubmitForm()
//							+".submit();"); // form a submitear automaticamente desde la vista 20110214
//		} else {
//			this.setSubmitForm(null);
//		}

		// si currentStep ( wstepdef ) tiene indicado procesador del paso lo cargo
		if (pasoActual.getCurrentStep().getIdDefaultProcessor()!=null && 
				! "".equals(pasoActual.getCurrentStep().getIdDefaultProcessor()) ) {

			loadCustomProcesorForm();
			
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
		} else if (pasoActual.getwProcessWork().getProcessDef().getWorkZoneId()!=null && 
				! "".equals(pasoActual.getwProcessWork().getProcessDef().getWorkZoneId())) {
			
			paginaProceso=pasoActual.getwProcessWork().getProcessDef().getWorkZoneId();
			
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
		} else if (pasoActual.getwProcessWork().getProcessDef().getListZoneId()!=null && 
				! "".equals(pasoActual.getwProcessWork().getProcessDef().getListZoneId())) {
			
			paginaLista=pasoActual.getwProcessWork().getProcessDef().getListZoneId();
			
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
			
		} else if (pasoActual.getwProcessWork().getProcessDef().getAdditionalZoneId()!=null && 
				! "".equals(pasoActual.getwProcessWork().getProcessDef().getAdditionalZoneId())) {
			
			paginaAnexa=pasoActual.getwProcessWork().getProcessDef().getAdditionalZoneId();
			
		} else { // setea la página por defecto que tengamos en el proyecto (Constantes)
			
			paginaAnexa=PAGINA_ANEXA_DEFAULT;
		
		}
	}

	// carga el nombre del xhtml que va a utilizar para procesar este step
	private void loadCustomProcesorForm() throws WStepWorkException {

		//rrl 20120619
		String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("").replaceAll("\\\\", "/");
		String xhtmlFormName = contextPath + pasoActual.getCurrentStep().getIdDefaultProcessor();
		if ( !contextPath.endsWith("/") ) {
			xhtmlFormName = contextPath + "/" + pasoActual.getCurrentStep().getIdDefaultProcessor();
		}
		
		File xhtmlFile = new File(xhtmlFormName);
		if (xhtmlFile.exists()) {
			  logger.debug("### TRAZA: EXISTE pasoActual.getCurrentStep().getIdDefaultProcessor() = " + pasoActual.getCurrentStep().getIdDefaultProcessor());
			  procesadorPaso=pasoActual.getCurrentStep().getIdDefaultProcessor();
		} else {
			logger.debug("### TRAZA: NO EXISTE pasoActual.getCurrentStep().getIdDefaultProcessor() = " + pasoActual.getCurrentStep().getIdDefaultProcessor());
			  logger.error("_setCurrentStepWork: No existe la ruta del fichero para el procesador de paso indicado [" +pasoActual.getCurrentStep().getIdDefaultProcessor()+"] - idStepWork:["+idStepWork+"]");
			  throw new WStepWorkException("No existe la ruta del fichero para el procesador de paso indicado: " + pasoActual.getCurrentStep().getIdDefaultProcessor() + " - idStepWork:["+idStepWork+"]");				  
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
		if (cs!=null) {
			usuarioLogueado=cs.getUsuario().getIdUsuario();
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

	//rrl 20120619
	public String getProcesadorPaso() {
		return procesadorPaso;
	}

	public void setProcesadorPaso(String procesadorPaso) {
		this.procesadorPaso = procesadorPaso;
	}
	
	//rrl 20130806
	public String getButtonPress() {
		return buttonPress;
	}

	public void setButtonPress(String buttonPress) {
		this.buttonPress = buttonPress;
	}

}
