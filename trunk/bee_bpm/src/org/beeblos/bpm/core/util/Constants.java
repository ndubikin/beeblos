package org.beeblos.bpm.core.util;

import static org.beeblos.bpm.core.util.Resourceutil.getIntegerProperty;

import java.util.Date;


public class Constants {
	
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
	
	public static final String ALIVE = "A";
	public static final String PROCESSED = "P"; 
	
	public static final boolean EMPTY_OBJECT = true; // to work with emtpy objects ( this is a not null objet but empty one )
	
	public static final String PASS_PHRASE = "My Pass Phrase!"; // HZC 20110111
	
	// fecha fija para inicializar mod_date en las diferentes tablas ...
	public static final Date DEFAULT_MOD_DATE = new Date(0200000002L);
	
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
	public static final String LOAD_WPROCESSDEF = "LOAD_WPROCESSDEF";
	public static final String WPROCESSDEF_QUERY = "WPROCESSDEF_QUERY";
	public static final String SUCCESS_WROLEDEF = "SUCCESS_WROLEDEF";

	// dml 20120110
	public static final String CREATE_NEW_WPROCESSDEF = "CREATE_NEW_WPROCESSDEF";
	
	// dml 20120112
	public static final String SUCCESS_FORM_WSTEPDEF = "SUCCESS_FORM_WSTEPDEF";
	public static final String LOAD_WSTEPDEF = "LOAD_WSTEPDEF";
	public static final String CREATE_NEW_WSTEPDEF = "CREATE_NEW_WSTEPDEF";
	public static final String WSTEPDEF_QUERY = "WSTEPDEF_QUERY";

	// dml 20120113
	public static final String LAST_W_STEP_DEF_ADDED = "LAST_W_STEP_DEF_ADDED";	
	public static final String LAST_W_STEP_DEF_MODIFIED = "LAST_W_STEP_DEF_MODIFIED";

	
}
