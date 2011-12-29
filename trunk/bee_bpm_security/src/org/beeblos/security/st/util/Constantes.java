package org.beeblos.security.st.util;

import static org.beeblos.security.st.util.Resourceutil.getIntegerProperty;
import static org.beeblos.security.st.util.Resourceutil.getStringProperty;

public class Constantes {
	
	public static final String CODIGO_PAIS_DEFECTO = "ES";
	public static final String NOMBRE_PAIS_DEFECTO = "España";
	public static final String ID_PAIS_GENERICO = "99"; 
	public static final String CODIGO_MONEDA_DEFECTO = "€";	


	public static final Boolean USE_SECURITY = (getIntegerProperty("bdc.auth.ok") == 18329 ? false
			: true);



	
	//rrl 20110610
	public static final String FICHERO_BDC_MENSAJES_ERROR = "org.beeblos.bpm.wc.taglib.util.error_messages";
	public static final String FICHERO_BDC_MENSAJES	      = "org.beeblos.bpm.wc.taglib.util.messages";
	
	public static final Boolean ACTIVO 			= true;


	public static final String DEFAULT_PASS = "12345";

	public static final String DOCUMENTO_FISICO = "0";
	public static final String DOCUMENTO_VIRTUAL = "1";
	
	public static final Integer TERRITORIO_ID_ESTADO 		= 0;
	public static final Integer TERRITORIO_ID_LOCALIDAD 	= 1;

	


	// nombres de keys de retorno para navegacion JSF



	public static final String SUCCESS_PAIS 			= "SUCCESS_PAIS";
	public static final String SUCCESS_TERRITORIO 		= "SUCCESS_TERRITORIO"; 
	
	public static final String SUCCESS_USUARIO 		= "SUCCESS_USUARIO";
	public static final String SUCCESS_MIUSUARIO 	= "SUCCESS_MIUSUARIO";
	
	
	// funciones en GP
	public static final Integer GP_F01 = 401;
	public static final Integer GP_F02 = 402;
	public static final Integer GP_F02_1 = 403;
	public static final Integer GP_F02_2 = 404;
	public static final Integer GP_F03 = 405;
	public static final Integer GP_F04 = 406;
	public static final Integer GP_F05 = 407;
	public static final Integer GP_F06 = 408;
	public static final Integer GP_F07 = 409;
	public static final Integer GP_F08 = 410;
	public static final Integer GP_F09 = 411;
	public static final Integer GP_F10 = 412;
	public static final Integer GP_F11 = 413;
	public static final Integer GP_F12 = 414;
	public static final Integer GP_F13 = 415;
	public static final Integer GP_F14 = 416;
	public static final Integer GP_F15 = 417;
	public static final Integer GP_F16 = 418;
	public static final Integer GP_F17 = 419;
	public static final Integer GP_F18 = 420;
	public static final Integer GP_F19 = 421;
	public static final Integer GP_F20 = 422;
	public static final Integer GP_F21 = 423;
	public static final Integer GP_F22 = 424;
	public static final Integer GP_F23 = 425;
	public static final Integer GP_F24 = 426;
	public static final Integer GP_F24_1 = 427;
	public static final Integer GP_F25 = 428;
	public static final Integer GP_F26 = 429;
	public static final Integer GP_F27 = 430;
	public static final Integer GP_F28 = 431;
	public static final Integer GP_F29 = 432;
	
	
	// funciones en DAP
	public static final Integer DAP_F01 = 501;
	public static final Integer DAP_F01_1 = 502;
	public static final Integer DAP_F01_2 = 503;
	public static final Integer DAP_F01_3 = 504;
	public static final Integer DAP_F02 = 505;
	public static final Integer DAP_F02_1 = 506;
	public static final Integer DAP_F02_2 = 507;
	public static final Integer DAP_F03 = 508;
	public static final Integer DAP_F04 = 509;
	public static final Integer DAP_F05 = 510;
	public static final Integer DAP_F06 = 511;
	public static final Integer DAP_F07 = 512;
	public static final Integer DAP_F08 = 513;
	public static final Integer DAP_F09 = 514;
	public static final Integer DAP_F10 = 515;
	public static final Integer DAP_F11 = 516;
	public static final Integer DAP_F12 = 517;
	public static final Integer DAP_F13 = 518;
	public static final Integer DAP_F15 = 519;
	public static final Integer DAP_F16 = 520;
	public static final Integer DAP_F17 = 521;
	public static final Integer DAP_F18 = 522;
	public static final Integer DAP_F19 = 523;
	public static final Integer DAP_F20 = 524;
	public static final Integer DAP_F21 = 525;
	public static final Integer DAP_F22 = 526;
	public static final Integer DAP_F23 = 527;
	public static final Integer DAP_F24 = 528;
	public static final Integer DAP_F25 = 529;
	public static final Integer DAP_F26 = 530;
	public static final Integer DAP_F27 = 531;
	public static final Integer DAP_F28 = 532;
	public static final Integer DAP_F29 = 533;
	public static final Integer DAP_F30 = 534; //mrico 20110704 - Permisos para consulta usuarioLogin 
	
	public static final Integer DAP_564 = 564; // AGREGAR PROVEEDOR
	public static final Integer DAP_565 = 565; // CONSULTA PROVEEDOR
	
	public static final Integer DAP_566 = 566; // AGREGAR INFORME
	public static final Integer DAP_567 = 567; // AGREGAR FACTURA
	public static final Integer DAP_568 = 568; // VER PANEL FACTURAS PENDIENTES Y ANULADAS ( USUARIO NO-DFAG )
	
	public static final Integer DAP_569 = 569; // VER PRESUPUESTO
	public static final Integer DAP_570 = 570; // AGREGAR PRESUPUESTO
	
	public static final Integer DAP_571 = 571; // AGREGAR PROVEEDOR
	

	
}
