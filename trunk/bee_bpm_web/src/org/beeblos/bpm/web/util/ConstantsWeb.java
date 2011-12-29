package org.beeblos.bpm.web.util;

import static org.beeblos.bpm.core.util.Resourceutil.getIntegerProperty;

public class ConstantsWeb {

	public static final String DEFAULT_ENCODING="UTF-8";  // nes 20110125
	
	public static final String DOCUMENTO_FISICO 	= "0";
	public static final String DOCUMENTO_VIRTUAL 	= "1";
	
	public static final String CONSULTA_TAREA 		= "CONSULTA_TAREA";
	public static final String PROCESAR_TAREA 		= "PROCESAR_TAREA"; // NES 20101220
	public static final String PASO_DESBLOQUEADO 		= "PASO_DESBLOQUEADO"; // NES 20111219
	
	public static final String ALIVE = "A";
	public static final String PROCESSED = "P"; 
	
	/** Nombre de par�metros en el "web.xml" que indican la ruta de las 
	    imagenes para los mensajes de error. */
	public static final String INFO_MESSAGE_ICON = "INFO_MESSAGE_ICON";
	public static final String WARN_MESSAGE_ICON = "WARN_MESSAGE_ICON";
	public static final String ERROR_MESSAGE_ICON = "ERROR_MESSAGE_ICON";
	public static final String FATAL_MESSAGE_ICON = "FATAL_MESSAGE_ICON";
	
	// nombres de keys de retorno para navegacion JSF
	
	public static final String FAIL 					= "FAIL";	
	
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

	public static final String PAGINA_PROCESO_DEFAULT = "/dap/procesarProyectoDAP/pestanias/procesarProyectoDefault.xhtml";
	public static final String PAGINA_LISTA_DEFAULT ="/dap/procesarProyectoDAP/pestanias/listaProyectosPpDAP.xhtml";
	public static final String PAGINA_ANEXA_DEFAULT ="/fichas/fichaPropuestaReducida.xhtml";
	
	public static final String PAGINA_PROCESO_OK_DEFAULT ="/dap/procesarProyectoDAP/pestanias/procesoOkDefault.xhtml";
	public static final String PAGINA_GUARDAR_OK_DEFAULT ="/dap/procesarProyectoDAP/pestanias/guardarOkDefault.xhtml";
	public static final String PAGINA_PROCESO_CANCEL_DEFAULT ="/dap/procesarProyectoDAP/pestanias/procesoCancelDefault.xhtml";
	
	public static final String PROCESAR_PAGINA_BIENVENIDA ="PROCESAR_PAGINA_BIENVENIDA";
	
	public static final String NONE = "NONE";

}
