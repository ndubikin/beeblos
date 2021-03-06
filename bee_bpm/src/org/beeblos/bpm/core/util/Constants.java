package org.beeblos.bpm.core.util;

import static org.beeblos.bpm.core.util.Resourceutil.getIntegerProperty;
import static org.beeblos.bpm.core.util.Resourceutil.getStringProperty;

import java.util.Date;

import org.joda.time.DateTime;


public class Constants {
	
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
	public static String WORKFLOW_EDITOR_URI = getStringProperty("workflow.editor.uri","/general/javascript/work/editors/workfloweditor.html");
	public static String WORKFLOW_VIEW_URI = getStringProperty("workflow.view.xml.map.uri","/general/javascript/work/editors/workflowviewxmlmap.html");

	// dml 20130510
	public static String HIBERNATE_CONFIGURATION_LIST_XML = getStringProperty("hibernate.configuration.list.xml","hibernateConfigurationList.xml");

	// dml 20130510
	public static String APP_NAME = getStringProperty("beeblos.nombre.aplicacion","XXXX"); // LO CORTO A 4 PORQ DEJARON EL CAMPO DE USUARIOLOGIN DE ESE LARGO, LUEGO VER BIEN DE EMPROLIJAR ESTO ...

	public static final String ACTIVE = "Active"; // dml 20130508
	public static final String INACTIVE = "Inactive"; // dml 20130508

	public static Integer FIRST_WPROCESSDEF_VERSION = 1; // dml 20130506
	public static Integer FIRST_WSTEPDEF_VERSION = 1; // dml 20130508
	
	public static final boolean WRITE_EMAIL_TO_FILESYSTEM = getStringProperty("email.to.filesystem","no").equals("yes");
	public static final String EMAIL_DEFAULT_SUBJECT = getStringProperty("email.default.subject");
	
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
	
	public static final boolean EMPTY_OBJECT = true; // to work with emtpy objects ( this is a not null objet but empty one )
	
	public static final String PASS_PHRASE = "My Pass Phrase!"; // HZC 20110111
	
	// fecha fija para inicializar mod_date en las diferentes tablas ...
	public static final Date DEFAULT_MOD_DATE = new Date(0200000002L);
	public static final DateTime DEFAULT_MOD_DATE_TIME = new DateTime(0200000002L);
	
	public static final Integer ID_ROLE_DEF_DIRECTORES_DEPTO = getIntegerProperty("directoresdepto.role_def.id");
	public static final Integer ID_ROLE_DEF_SUBDIRECTORES = getIntegerProperty("subdirectores.role_def.id");
	
	public static final String FICHERO_BDC_MENSAJES_ERROR = "org.beeblos.bpm.wc.taglib.util.error_messages";
	public static final String FICHERO_BDC_MENSAJES	      = "org.beeblos.bpm.wc.taglib.util.messages";
	
	// general use constants
	public static final String FAIL = "FAIL";
	
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

	// dml 20120113
	public static final String LAST_W_STEP_DEF_ADDED = "LAST_W_STEP_DEF_ADDED";	
	public static final String LAST_W_STEP_DEF_MODIFIED = "LAST_W_STEP_DEF_MODIFIED";

	// dml 20120416
	public static final String LAST_W_PROCESS_DEF_ADDED = "LAST_W_PROCESS_DEF_ADDED";	
	public static final String LAST_W_PROCESS_DEF_MODIFIED = "LAST_W_PROCESS_DEF_MODIFIED";

	public static final String PROCESS_STEP = "PROCESS_STEP";
	public static final String TURNBACK_STEP = "TURNBACK_STEP";
	
	public static final String PROCESADOR_PASO_DEFAULT = "/task/processWithForm.xhtml";
	public static final String PAGINA_PROCESO_DEFAULT = "/task/processor/tabs/defaultStepProcessor.xhtml";
	public static final String PAGINA_LISTA_DEFAULT ="/dap/procesarProyectoDAP/pestanias/listaProyectosPpDAP.xhtml";
	public static final String PAGINA_ANEXA_DEFAULT ="/fichas/fichaPropuestaReducida.xhtml";
	
	public static final String PAGINA_PROCESO_OK_DEFAULT ="/task/tabs/processOkDefault.xhtml";
	public static final String PAGINA_GUARDAR_OK_DEFAULT ="/dap/procesarProyectoDAP/pestanias/guardarOkDefault.xhtml";
	public static final String PAGINA_PROCESO_CANCEL_DEFAULT ="/dap/procesarProyectoDAP/pestanias/procesoCancelDefault.xhtml";
	
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
	
}