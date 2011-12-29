package org.beeblos.bpm.core.util;

import static org.beeblos.bpm.core.util.Resourceutil.getIntegerProperty;

import java.util.Date;


public class Constants {
	
	//public static final Integer VENCIMIENTO_FACTURA_PROVEEDOR_DEFAULT  = getIntegerProperty("dfag.vencimiento.factura.default.en.dias",60);
	
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
	
}
