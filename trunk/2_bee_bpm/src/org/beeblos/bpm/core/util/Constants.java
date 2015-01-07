package org.beeblos.bpm.core.util;

import static com.sp.common.util.Resourceutil.getIntegerProperty;
import static com.sp.common.util.Resourceutil.getStringProperty;


public class Constants {
	
	public static final String INIT_EV_STRING = "InitEv";
	public static final String DEFAULT_LAYER_XML = "<Layer description=\"\" id=\"1\" label=\"Default Layer\"><mxCell parent=\"0\" /></Layer>";
	public static final String DEFAULT_MXCELL_XML = "<mxCell parent=\"1\" vertex=\"1\"> <mxGeometry as=\"geometry\" height=\"32\" width=\"72\" x=\"0\" y=\"0\" /> </mxCell>";
	public static final String DEFAULT_EDGE_MXCELL_XML = "<mxCell edge=\"1\" parent=\"1\" source=\"\" style=\"straightEdge\" target=\"\"><mxGeometry as=\"geometry\" relative=\"1\" /></mxCell>";
	/**
	 * Rol de sistema para indicar el usuario originador de una instancia de un proceso.
	 * De otra forma: sería el usuario que inicia un workflow.
	 * Se define como rol porque sería un rol dinámico que se asigna en tiempo de ejecución
	 * para cada instancia.
	 * nes 20141014
	 */
	public static final Integer W_SYSROLE_ORIGINATOR_ID=getIntegerProperty("system.role.originator",1);
	/**
	 * Rol genérico del sistema para asociar los administradores del proceso.
	 * nes 20141014
	 */
	public static final Integer W_SYSROLE_PROCESS_ADMIN_ID=getIntegerProperty("system.role.process.admin",2);

	//rrl 20141022
	public static final String W_PROCESS_DEF_FORM_VIEW="/app/forms/wProcessDefFormCall.jsf";
	public static final String W_STEP_DEF_FORM_VIEW="/app/forms/wStepDefFormCall.jsf";

	//rrl 20141111
	public static final String W_PROCESS_HEAD_FORM_VIEW="/app/forms/wProcessHeadFormCall.jsf";
	public static final String W_STEP_HEAD_FORM_VIEW="/app/forms/wStepHeadFormCall.jsf";
	
	public static final String W_EMAIL_TEMPLATE_FORM_VIEW="/app/wfCrud/wEmailTemplateCrud/wEmailTemplateCrudWithTemplate.xhtml";
	public static final String W_EMAIL_ACCOUNT_FORM_VIEW="/app/wfCrud/wUserEmailAccountCrud/wUserEmailAccountCrudWithTemplate.xhtml";

	// dml 20140312 CONSTANTES MIGRACION JSF2 DESDE 2_FBBVA_DAP (MIRAR DE PONER EN COMMON)
	public static final String TAB_CARATULA = "caratula";// nes 20130226
	public static final String MY_USER_ACCOUNT_URI="/app/myaccount/myUserAccountCallWithTemplate.jsf";

	// dml 20130916
	public static final String MESSAGES_CORE_FILENAME = "messages_core";
	
	// nes 20130912

	public static final String ALIVE = "ALIVE";
	public static final String PROCESSED = "PROCESSED"; 
	public static final String ALL = "ALL";
	public static final String DELETED = "DELETED";
	
	public static final String FINISHED = "Finished";
	
	// dml 20130830
	public static final boolean DELETED_BOOL = true;
	public static final boolean NOT_DELETED_BOOL = false;

	// dml 20130821
	public static final Integer TEXT_DATA_TYPE = 2; // VARCHAR
	public static final int DEFAULT_VARCHAR_LENGHT = 45;

	// nes 20130803
	public static final int ACTIVE_DATA_FIELDS = 1;
	public static final int ALL_DATA_FIELDS = 0;
	
	// dml 20130709
	public static String PROCESS_XML_MAP_LOCATION = getStringProperty("process.xml.map.location","/processXmlMapTmp.xml");
	public static String WORKFLOW_EDITOR_URI = getStringProperty("workflow.editor.uri","/general/javascript/work/editors/workfloweditor.jsf");
	public static String WORKFLOW_VIEW_URI = getStringProperty("workflow.view.xml.map.uri","/general/javascript/work/editors/workflowviewxmlmap.jsf");

	// dml 20130510
	public static String HIBERNATE_CONFIGURATION_LIST_XML = ppxx();

	// dml 20130510
	public static String APP_NAME = getStringProperty("beeblos.nombre.aplicacion","XXXX"); // LO CORTO A 4 PORQ DEJARON EL CAMPO DE USUARIOLOGIN DE ESE LARGO, LUEGO VER BIEN DE EMPROLIJAR ESTO ...

	public static final String ACTIVE = "Active"; // dml 20130508
	public static final String INACTIVE = "Inactive"; // dml 20130508

	public static Integer FIRST_WPROCESSDEF_VERSION = 1; // dml 20130506
	public static Integer FIRST_WSTEPDEF_VERSION = 1; // dml 20130508
	
	public static final boolean WRITE_EMAIL_TO_FILESYSTEM = getStringProperty("email.to.filesystem","no").equals("yes");
	
	public static String BEEBLOS_PROTOCOL = getStringProperty("repository.protocol");
	public static String BEEBLOS_IP = getStringProperty("repository.ip");
	public static Integer BEEBLOS_PORT = getIntegerProperty("repository.port");
	public static String BEEBLOS_USER = getStringProperty("repository.user");
	public static String BEEBLOS_PWD = getStringProperty("repository.pwd");

	public static String BEEBLOS_SERVLET = BEEBLOS_PROTOCOL+"://"
			+BEEBLOS_IP + (BEEBLOS_PORT!=null?":"+BEEBLOS_PORT:"")
			+getStringProperty("repository.servlet");

//	public static String BEEBLOS_DEFAULT_REPOSITORY_ID=getStringProperty("repository.repositoryId");
	public static final String DEFAULT_ENCODING="UTF-8";

	public static final String TRAZA_ENVIO_EMAIL = "TRAZA_ENVIO_EMAIL";
	public static final String REPORTE = "reporte";
	public static final String EMAIL_MASIVO = "emailMasivo";
	public static final String TRUE = "true"; // HZC 20101229

	public static final Integer DEFAULT_PROCESS_STATUS = 1;

	public static final String LOAD_WPROCESSWORK = "LOAD_WPROCESSWORK";

	public static final String PROCESSING = "PROCESSING";
	public static final String PENDING = "PENDING";

	public static final String WORKINGPROCESS_QUERY = "WORKINGPROCESS_QUERY";
	public static final String WORKINGWORKS_QUERY = "WORKINGWORKS_QUERY";
	public static final String WORKINGSTEPS_QUERY = "WORKINGSTEPS_QUERY";

	//public static final Integer VENCIMIENTO_FACTURA_PROVEEDOR_DEFAULT  = getIntegerProperty("dfag.vencimiento.factura.default.en.dias",60);

	public static final String TEXT_XML="text/xml";
	
	/** PARÁMETROS PARA EL SISTEMA DE WORKFLOW */
	public static final String ROLE = "ROLE";
	public static final String USER = "USER";
	public static final Integer TURNBACK =-1; // PAR INDICAR QUE EL PASO VUELVE ATRÁS ...

	public static final String OMNIADMIN = "OMNIADMIN";
	
	public static final Integer ID_ROLE_DEF_DIRECTORES_DEPTO = getIntegerProperty("directoresdepto.role_def.id");
	public static final Integer ID_ROLE_DEF_SUBDIRECTORES = getIntegerProperty("subdirectores.role_def.id");
	
	public static final String FICHERO_BDC_MENSAJES_ERROR = "org.beeblos.bpm.wc.taglib.util.error_messages";
	public static final String FICHERO_BDC_MENSAJES	      = "org.beeblos.bpm.wc.taglib.util.messages";
	
	/**
	 * Nombre de par�metros en el "web.xml" que indican la ruta de las imagenes
	 * para los mensajes de error.
	 */
	public static final String INFO_MESSAGE_ICON 			= "INFO_MESSAGE_ICON";
	public static final String WARN_MESSAGE_ICON 			= "WARN_MESSAGE_ICON";
	public static final String ERROR_MESSAGE_ICON 			= "ERROR_MESSAGE_ICON";
	public static final String FATAL_MESSAGE_ICON 			= "FATAL_MESSAGE_ICON";
	

	public static final String SUCCESS_FORM_WPROCESSDEF = "SUCCESS_FORM_WPROCESSDEF";
	public static final String SUCCESS_FORM_WPROCESS = "SUCCESS_FORM_WPROCESS"; // dml 20130430
	public static final String LOAD_WPROCESSDEF = "LOAD_WPROCESSDEF";
	public static final String LOAD_WPROCESSHEAD = "LOAD_WPROCESSHEAD";
	public static final String WPROCESSDEF_QUERY = "WPROCESSDEF_QUERY";
	public static final String SUCCESS_WROLEDEF = "SUCCESS_WROLEDEF";

	// dml 20120110
	public static final String CREATE_NEW_WPROCESSDEF = "CREATE_NEW_WPROCESSDEF";
	public static final String CREATE_NEW_WPROCESSHEAD = "CREATE_NEW_WPROCESSHEAD";
	
	// dml 20120112
	public static final String SUCCESS_FORM_WSTEPDEF = "SUCCESS_FORM_WSTEPDEF";
	public static final String SUCCESS_FORM_WSTEPHEAD = "SUCCESS_FORM_WSTEPHEAD"; // dml 20130508
	public static final String LOAD_WSTEPDEF = "LOAD_WSTEPDEF";
	public static final String LOAD_WSTEPHEAD = "LOAD_WSTEPHEAD"; // dml 20130508
	public static final String CREATE_NEW_WSTEPDEF = "CREATE_NEW_WSTEPDEF";
	public static final String CREATE_NEW_WSTEPHEAD = "CREATE_NEW_WSTEPHEAD"; // dml 20130508
	public static final String WSTEPDEF_QUERY = "WSTEPDEF_QUERY";

	public static final String PROCESS_STEP = "PROCESS_STEP";
	public static final String TURNBACK_STEP = "TURNBACK_STEP";
	
	public static final String PROCESADOR_PASO_DEFAULT = "/task/processWithForm.xhtml";
	public static final String PAGINA_PROCESO_DEFAULT = "/task/processor/tabs/defaultStepProcessor.xhtml";
	public static final String PAGINA_LISTA_DEFAULT ="/dap/procesarProyectoDAP/pestanias/listaProyectosPpDAP.xhtml";
	public static final String PAGINA_ANEXA_DEFAULT ="/fichas/fichaPropuestaReducida.xhtml";
	
	public static final String PAGINA_PROCESO_OK_DEFAULT ="/task/tabs/processOkDefault.xhtml";
	public static final String PAGINA_GUARDAR_OK_DEFAULT ="/dap/procesarProyectoDAP/pestanias/guardarOkDefault.xhtml";
	public static final String PAGINA_PROCESO_CANCEL_DEFAULT ="/dap/procesarProyectoDAP/pestanias/procesoCancelDefault.xhtml";
	
	public static final String PAGINA_PROCESO_PROYECTO_CANCEL_DEFAULT ="/dap/procesarProyectoDAP/pestanias/procesoCancelDefault.xhtml";
	
	public static final String TASK_SUCCESSFULLY_PROCESSED_URI="/app/bpm/taskSuccessfullyProcessed.jsf"; //rrl 20140206
	
	public static final String TASK_PROCESSED_OK ="TASK_PROCESSED_OK";
	
	public static final String NONE = "NONE";
	public static final String CONSULTA_TAREA 		= "CONSULTA_TAREA";
	public static final String PROCESS_TASK 		= "PROCESS_TASK"; // NES 20101220
	public static final String PASO_DESBLOQUEADO 		= "PASO_DESBLOQUEADO"; // NES 20111219
	public static final String SUCCESS_MIUSUARIO		="SUCCESS_MIUSUARIO";
	
	public static final String INYECTAR_WORKFLOW 		= "INYECTAR_WORKFLOW";  // nes 20101217

	public static final String SUCCESS_PAIS 			= "SUCCESS_PAIS";
	public static final String SUCCESS_TIPOTERRITORIO	= "SUCCESS_TIPOTERRITORIO";
	public static final String SUCCESS_MONEDA 			= "SUCCESS_MONEDA";
	public static final String SUCCESS_DEPARTAMENTO 	= "SUCCESS_DEPARTAMENTO";

	public static final String CONSULTA_USUARIO_LOGIN 	= "CONSULTA_USUARIO_LOGIN";
	public static final String SUCCESS_USUARIO  		= "SUCCESS_USUARIO";
	public static final String SUCCESS_ENVIAR_A_GTD 	= "SUCCESS_ENVIAR_A_GTD";

	public static final String SUCCESS_WELCOME 			= "SUCCESS_WELCOME";
	
	public static final String WELCOME_PAGE				= "WELCOME_PAGE";

	public static final String SUCCESS_TERRITORIO = "SUCCESS_TERRITORIO"; // mrico 20110418
			
	public static final String DEFAULT_PASS = "12345";
	
	public static final Integer TERRITORIO_ID_ESTADO=0;
	public static final Integer TERRITORIO_ID_LOCALIDAD=1;
	
	public static String ppxx(){
		System.out.println(">>>>>>>>>< en Constants.2-bee-bpm");
		return getStringProperty("hibernate.configuration.list.xml","hibernateConfigurationList.xml");
	}
}