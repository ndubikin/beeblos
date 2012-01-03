package org.beeblos.bpm.wc.taglib.util;

import static org.beeblos.bpm.core.util.Resourceutil.getIntegerProperty;
import static org.beeblos.bpm.core.util.Resourceutil.getStringProperty;


public class Constantes {
	
	public static final String CODIGO_PAIS_DEFECTO = "ES";
	public static final String NOMBRE_PAIS_DEFECTO = "España";
	public static final String ID_PAIS_GENERICO = "99"; 
	public static final String CODIGO_MONEDA_DEFECTO = "€";	

	public static final String WELCOME_PAGE 				= "WELCOME_PAGE";
	public static final Boolean USE_SECURITY = (getIntegerProperty("bdc.auth.ok") == 18329 ? false
			: true);
	public static final String USUARIO_PAGINA_INICIO_DEFAULT = "/paginasInicio/paginaInicioDefault.xhtml";
	public static String NOM_APLICACION = getStringProperty("beeblos.nombre.aplicacion","XXXX"); // LO CORTO A 4 PORQ DEJARON EL CAMPO DE USUARIOLOGIN DE ESE LARGO, LUEGO VER BIEN DE EMPROLIJAR ESTO ...
	public static final String TITULO_PRINCIPAL_DEFAULT = "Beeblos BPM Suite"; //Beeblos BPM Suite
	
	public static final String SUCCESS_UCE 				= "SUCCESS_UCE";
	public static final String SUCCESS_MIUSUARIO		="SUCCESS_MIUSUARIO";
	
	public static final String SUCCESS_WROLEDEF = "SUCCESS_WROLEDEF";
	
	public static final String WPROCESSDEF_QUERY = "WPROCESSDEF_QUERY";
	
}
