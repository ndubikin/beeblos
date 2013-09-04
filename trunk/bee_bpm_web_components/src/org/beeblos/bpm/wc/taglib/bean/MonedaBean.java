package org.beeblos.bpm.wc.taglib.bean;

import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlCommandButton;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.security.auxiliar.bl.MonedaBL;
import org.beeblos.security.auxiliar.error.MonedaException;
import org.beeblos.security.auxiliar.model.Moneda;

//import static com.softpoint.fgp.util.ConstantesDAP.*;


public class MonedaBean extends CoreManagedBean {

	private static final Log logger = LogFactory.getLog(MonedaBean.class);

	private static final long serialVersionUID = 4031555875133328797L;

	private Moneda moneda;

	private List<Moneda> listamoneda = new ArrayList<Moneda>();
	
	private String mensajes;
	
	private HtmlCommandButton btnBorrar;
	

	private String currentSession;
	
	private boolean disableBtnBorrar;
	

	public MonedaBean() {

		_reset();
		
		disableBtnBorrar = false;

	}
	
	
	// nes 20100927
	private void _reset() {
		
		this.moneda = new Moneda();
		
		// recarga la lista para reflejar la actualización realizada ...
		this.listamoneda=null;
		this.getListamoneda();		
		
	}
	
	// nes 20100927
	// si tiene id es actualización, si no lo tiene es alta ...
	public void guardar() {
		
		reset();
		

		if (moneda.getIdMoneda()!=null && moneda.getIdMoneda()!=0) {
			actualizar();
		} else {
			agregar();
		}
		
		_reset();
		
	}
	
	
	// nes 20100927
	public void actualizar() {
		
//		String retorno = FAIL;    //rrl 20110615 DEPENDENCIA NO NECESARIA

		try {
			
			new MonedaBL()
						.actualizar(moneda);
			
//			retorno=SUCCESS_MONEDA;   //rrl 20110615 DEPENDENCIA NO NECESARIA
			
			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla
		
		} catch (MonedaException e) {
			String message = "MonedaBean.actualizar() MonedaException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

	}

	// nes 20100927
	public String agregar() {

//		String retorno = FAIL;   //rrl 20110615 DEPENDENCIA NO NECESARIA

		MonedaBL monedaBL = new MonedaBL();

		try {

			monedaBL.agregar(moneda);
//			retorno=SUCCESS_MONEDA;   //rrl 20110615 DEPENDENCIA NO NECESARIA
			
			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla

		} catch (MonedaException e) {
			String message = "MonedaBean.agregar() MonedaException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

//		return retorno;   //rrl 20110615 DEPENDENCIA NO NECESARIA
		return null;

	}
	
		
	public String enableBtnBorrar(){
		
		if(btnBorrar != null){
			btnBorrar.setDisabled(false);	
		}			
		
		setDisableBtnBorrar(false);
		
		return null;
	}

	public String borra(ActionEvent event) {
		return this.borra();
	}
	
	// nes 20100927
	public String borra() {

//		String retorno = FAIL;   //rrl 20110615 DEPENDENCIA NO NECESARIA

		MonedaBL monedaBL = new MonedaBL();

		try {
			monedaBL.borrar(moneda);
//			retorno = SUCCESS_MONEDA;   //rrl 20110615 DEPENDENCIA NO NECESARIA
			_reset();
		} catch (MonedaException e) {
			String message = "MonedaBean.borra() MonedaException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

//		return retorno;   //rrl 20110615 DEPENDENCIA NO NECESARIA
		return null;

	}

	// nes 20100927
	public Moneda getmoneda() {
		if (moneda == null) {
			moneda = new Moneda();
		}
		return moneda;
	}



	// nes 20100927
	public List<Moneda> getListamoneda() {

		if (this.listamoneda==null) {
			MonedaBL monedaBL = new MonedaBL();
	
			//List<Moneda> retorno = null;
	
			try {
	
				this.listamoneda = monedaBL.obtenerMonedas();
	
			} catch (MonedaException ex) {
	
				logger.info("Ocurrio Un Error al tratar de obtener la lista de Monedas: "
						+ ex.getMessage());
	
			}
		}
		
		return listamoneda;
	}



//	@SuppressWarnings("unchecked")
//	public List<SelectItem> getListadoMonedas() {
//
//		List<SelectItem> retorno = new ArrayList<SelectItem>();
//		MonedaBL monedaBL = new MonedaBL();
//		List<Moneda> listadomonedas = null;
//
//		try {
//
//			listadomonedas = monedaBL.obtenerTiposTratamiento();
//			for (Moneda a : listadomonedas)
//				retorno.add(new SelectItem(a.getIdMoneda(), a.getMonedaSimbolo()));
//
//		} catch (MonedaException e) {
//
//			logger
//					.info("Ocurrio Un Error al tratar de OIbtener el Listado de Monedas para el Crear el Combo: "
//							+ e.getMessage());
//		}
//
//		return retorno;
//	}

	public String getMensajes() {
		return mensajes;
	}


	public void setMensajes(String mensajes) {
		this.mensajes = mensajes;
	}

	public boolean isDisableBtnBorrar() {
		//nes 20100927
		logger.debug("isDisableBtnBorrar:IdMoneda = " + this.moneda.getMonedaNombre());
		
		if( isEmpty(this.moneda.getIdMoneda())){ // nes 20100927
			setDisableBtnBorrar(true);
		}else{
			setDisableBtnBorrar(false);
		}
		
		return disableBtnBorrar;
	}
	
	public boolean isEmpty(Object obj){
		
		return ( obj == null || obj.toString().trim().equals("") );				
		
	}

	public void setDisableBtnBorrar(boolean disableBtnBorrar) {
		this.disableBtnBorrar = disableBtnBorrar;
	}
	
	public HttpSession getSession() {
		return (HttpSession) getContext().getExternalContext()
				.getSession(false);
	}

	public String getCurrentSession() {
	
		HttpSession session = getSession();		
		currentSession = session.getId();
		
		return currentSession;
	}

	public void setCurrentSession(String currentSession) {
		this.currentSession = currentSession;
	}
/*
	public String cambioPais(ActionEvent event){ 
		
		MonedaBL monedaBl = new MonedaBL();
		
		try{
			
			getListaMoneda().clear();
			getListaMoneda().add(new SelectItem(0,"Seleccionar..."));
			
			List<Moneda> listTemp = bl.obtenerMonedaPorPais(idPaisMoneda);
			
			for (Moneda moneda: listTemp) {
				listaMoneda.add(new SelectItem(moneda.getIdMoneda(), moneda.getMonedaSimbolo()));
			} 			
			
			
		} catch (MonedaException e) {

			logger.error("Ocurrio Un Error al tratar de Obtener los Tipos de Documento de Persona Jurídica para crear el Combo \"Tipo Documento Persona Fisica\": "  
							+ e.getMessage());																		
		}
		
		return null;
	}
*/	
	





		

}
