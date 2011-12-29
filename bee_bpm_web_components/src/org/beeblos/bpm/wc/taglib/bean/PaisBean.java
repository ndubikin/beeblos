package org.beeblos.bpm.wc.taglib.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlCommandButton;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.security.auxiliar.bl.PaisBL;
import org.beeblos.security.auxiliar.error.PaisException;
import org.beeblos.security.auxiliar.model.Pais;

public class PaisBean extends CoreManagedBean {

	private static final Log logger = LogFactory.getLog(PaisBean.class);

	private static final long serialVersionUID = 4031555875133328797L;
	
	private Pais pais;
//	private String paisNombre;
	
	private List<Pais> paises;
//	private List<SelectItem> listadoPaises;
	private String mensajes;
	
	private HtmlCommandButton btnBorrar;
	
	private String currentSession;

	private boolean disableBtnBorrar;
	
	private boolean actualizacion; 
	
	
	public PaisBean() {

		_reset();
		
		disableBtnBorrar = false;
		actualizacion = false;

	}
	
	// nes 20100927
	private void _reset() {
		
		this.pais = new Pais();
		
		// recarga la lista para reflejar la actualización realizada ...
		this.paises=null;
		this.paises=this.getPaises();		
		
	}
	
	public void guardar() {
		
		// martin - 20101004 - Ahora el id es asignado, 
		// as� que no podemos distinguir entre actualizar y guardar de esa forma
		
		// Solucion: Al actualizar no puede modificarse el ID y se usa para ver 
		// si ya existe (actualizar) o no (agregar)
			
		String idPais = pais.getIdPais();
		if (idPais!=null ||  "".equals(idPais)) {

			try {
				if ((new PaisBL().obtenerPaisPorPK(idPais) != null)){
					actualizar();
				} else {
					agregar();
				}
			} catch (PaisException e) {
				//martin - 20100930
				logger.error("Ocurrio un Error al tratar de confirmar la presencia o no  del Pais: [ id = " 
						+ this.pais.getIdPais() + " ; Nombre: "
						+ this.pais.getPaisNombre()
						+"] "	+ e.getMessage() + " : " + e.getCause());
				
				String params[] = {this.pais.getIdPais() + ",", "Error al intentar actualizar el Pais "+ pais.toString() +" \n"+e.getMessage() };				
				agregarMensaje("34",e.getMessage(),params,FGPException.ERROR);	
			}
		} 
		_reset();
	}
				

	public void actualizar()  {

//		String retorno = FAIL;  // rrl 20110614 DEPENDENCIA NO NECESARIA
		
		try {
			
			new PaisBL()
				.actualizar(pais);
		
//			retorno=SUCCESS_PAIS;  // rrl 20110614 DEPENDENCIA NO NECESARIA
			
			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla
						
			
		} catch (PaisException e) {

			//martin - 20100930
			logger.error("Ocurrio Un Error al tratar de Actualizar el Pais: [ id = " 
					+ this.pais.getIdPais() + " ; Nombre: "
					+ this.pais.getPaisNombre()
					+"] "	+ e.getMessage() + " : " + e.getCause());
			
			String params[] = {this.pais.getIdPais() + ",", "Error al intentar actualizar el Pais "+ pais.toString() +" \n"+e.getMessage() };				
			agregarMensaje("34",e.getMessage(),params,FGPException.ERROR);	
			
		}


	}

	public String agregar() {

//		String retorno = FAIL;  // rrl 20110614 DEPENDENCIA NO NECESARIA

		PaisBL pBl = new PaisBL();

		try {

			pBl.agregar(pais);

//			retorno = SUCCESS_PAIS;  // rrl 20110614 DEPENDENCIA NO NECESARIA

			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla

		} catch (PaisException ex2) {

			//martin - 20100930
			logger.error("Ocurrio Un Error al tratar de Agregar el Pais: [ID: " 
					+ this.pais.getIdPais() + " ; Nombre: "
					+ this.pais.getPaisNombre()
					+"] "	+ ex2.getMessage() + " : " + ex2.getCause());
			
			String params[] = {this.pais.getIdPais() + ",", "Error al intentar agregar el Pais "+ pais.toString() +" \n"+ex2.getMessage() };				
			agregarMensaje("34",ex2.getMessage(),params,FGPException.ERROR);			

		}

//		return retorno;  // rrl 20110614 DEPENDENCIA NO NECESARIA
		
		return null;
	}
	
	/*
	 * martin - 20101004 - 
	 * Metodo para buscar paiss por Nombre
	 */
	public String buscar() {

//		String retorno = FAIL;   // rrl 20110614 DEPENDENCIA NO NECESARIA
		
		Pais paisBuscado = null;
		PaisBL pBL = new PaisBL();
		try {
			
			this.paises= pBL.finderPais(pais.getIdPais(),pais.getPaisNombre());
//			retorno = SUCCESS_PAIS;   // rrl 20110614 DEPENDENCIA NO NECESARIA
			
//			
//			if (paisBuscado != null) {
//				this.pais =paisBuscado;
//
//				retorno = SUCCESS_PAIS;
//
//				this.paises = null;
//
//			} else {
//				
//				agregarMensaje(FacesMessage.SEVERITY_ERROR,
//						"errorPaisNoExiste", null);
//				
//				retorno = SUCCESS_PAIS;
//				
//				this.paises = null;
//				_reset();
//
//			}
			
			
			
		} catch (PaisException e) {
			logger
					.info("Ocurrio Un Error al tratar de insetar un Depto: "
							+ e.getMessage());
		}
//		return retorno;   // rrl 20110614 DEPENDENCIA NO NECESARIA
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
	
	public String borra() {

//		String retorno = FAIL;   // rrl 20110614 DEPENDENCIA NO NECESARIA

		PaisBL pBl = new PaisBL();

		try {
			pBl.borra(pais);
//			retorno = SUCCESS_PAIS;  // rrl 20110614 DEPENDENCIA NO NECESARIA
			_reset();
			
		} catch (PaisException e) {

			//martin - 20100930
			logger.error("Ocurrio Un Error al tratar de Borrar el Pais: [ id = " 
					+ this.pais.getIdPais() + " ; Nombre: "
					+ this.pais.getPaisNombre()
					+"] "	+ e.getMessage() + " : " + e.getCause());
			
			String params[] = {this.pais.getIdPais() + ",", "Error al intentar borrar Pais "+ pais.toString() +" \n"+e.getMessage() };				
			agregarMensaje("34",e.getMessage(),params,FGPException.ERROR);		
			

		}

//		return retorno;   // rrl 20110614 DEPENDENCIA NO NECESARIA
		return null;
	}

	public Pais getPais() {
		if (pais == null) {
			pais = new Pais();
		}
		return pais;
	}
	
	public void setPais(Pais pais) {
		this.pais = pais;
	}
	

	public String getIdPais() {
		return getPais().getIdPais();
	}
	
	public void setIdPais(String idPais) {
		getPais().setIdPais(idPais);
	}
	
	public String getPaisNombre() {
		return getPais().getPaisNombre();
	}
	
	public void setPaisNombre(String paisNombre) {
		getPais().setPaisNombre(paisNombre);
	}
	
	public String getPaisComentario() {
		return getPais().getPaisComentario();
	}
	
	public void setPaisComentario(String paisComentario) {
		getPais().setPaisComentario(paisComentario);
	}

	public List<Pais> getPaises() {

		if(paises == null){

			PaisBL pBl = new PaisBL();

			try {

				this.paises = pBl.obtenerPaises();

			} catch (PaisException ex) {

				logger.info("Ocurrio Un Error al tratar de obtener los Paises: "
						+ ex.getMessage());

			}

		}

		return paises;
	}

	
	public List<SelectItem> getListadoPaises() {

		List<SelectItem> retorno = new ArrayList<SelectItem>();
		PaisBL aBl = new PaisBL();
		List<Pais> listadopaises = null;

		try {

			listadopaises = aBl.obtenerPaises();
			for (Pais a : listadopaises)
				retorno.add(new SelectItem(a.getIdPais(), a.getPaisNombre()));

		} catch (PaisException e) {

			logger
					.info("Ocurrio Un Error al tratar de Obtener el Listado de Paises para el Crear el Combo: "
							+ e.getMessage());
		}

		return retorno;
	}

	public String getMensajes() {
		return mensajes;
	}
	
	public void setMensajes(String mensajes) {
		this.mensajes = mensajes;
	}

	public void setPaises(List<Pais> paises) {
		this.paises = paises;
	}
	
	
	public boolean isDisableBtnBorrar() {
		
		logger.debug("isDisableBtnBorrar:IdPais = " + this.getPaisNombre());
		System.out.println("isDisableBtnBorrar:IdPais = " + this.getPaisNombre());
		
		if( this.pais.getIdPais().isEmpty() || this.pais.getIdPais()!= null || !this.pais.getIdPais().equals("")){
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

	public void setActualizacion(boolean actualizacion) {
		this.actualizacion = actualizacion;
	}

	public boolean isActualizacion() {
		return actualizacion;
	}
	

}
