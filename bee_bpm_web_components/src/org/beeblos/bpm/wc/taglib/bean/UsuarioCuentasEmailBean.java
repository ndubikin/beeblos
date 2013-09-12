package org.beeblos.bpm.wc.taglib.bean;


import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;
import static org.beeblos.bpm.core.util.Constants.PASS_PHRASE;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlCommandButton;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.util.DesEncrypter;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import com.sp.common.jsf.util.UtilsVs;
import org.beeblos.security.st.bl.UsuarioBL;
import org.beeblos.security.st.bl.UsuarioCuentasEmailBL;
import org.beeblos.security.st.error.UsuarioCuentasEmailException;
import org.beeblos.security.st.error.UsuarioException;
import org.beeblos.security.st.model.UsuarioCuentasEmail;


public class UsuarioCuentasEmailBean extends CoreManagedBean {

	private static final long serialVersionUID = -4021524365949197101L;

	private static final Log logger = LogFactory.getLog(UsuarioCuentasEmailBean.class);
	

	
//	private Usuario usuario;
	
	private Integer idUsuarioCuentasEmail; // el tipo de sancion q estoy editando en caso de hacerlo 
	private String uceNombre; 
	private String uceEmail; 
	private UsuarioCuentasEmail currentUCE;
	
	private ArrayList<UsuarioCuentasEmail> listaUce = new ArrayList<UsuarioCuentasEmail>();
	
	private Integer idUsuario;
	private List<SelectItem> listaUsuarios = new ArrayList<SelectItem>();
	
	
	
	private String mensajes;
	
	private HtmlCommandButton btnBorrar;
	private HtmlCommandButton btnGuardar;
	private boolean disableBtnBorrar;
	private boolean disableBtnGuardar;
	
	
	private String currentSession;
	

	
	public UsuarioCuentasEmailBean() {

		_init();
		
		getListaUsuarios();

	}
	
	private void _init() {
		
		this.currentUCE = new UsuarioCuentasEmail();
		this.idUsuarioCuentasEmail=0;
		this.idUsuario=0;
		this.uceNombre=null;
		this.uceEmail=null;
		
		this.disableBtnBorrar=true;
		this.disableBtnGuardar = true;
		
	}
	
	
	private void _reset() {
		
		this.currentUCE = new UsuarioCuentasEmail();
		this.idUsuarioCuentasEmail=0;
		//this.idUsuario=0;
		this.uceNombre=null;
		this.uceEmail=null;
		
		this.disableBtnBorrar=true;
		this.disableBtnGuardar = true;
		
	}
	
	

	public void recargaListaUsuario() {
		// recarga la lista para reflejar la actualización realizada ...
		if (this.idUsuario!=null && this.idUsuario!=0) {
			
			changeUsuario(); // recarga la lista de cuentas de correo para el usuario corriente
			this.disableBtnGuardar=false;
			this.disableBtnBorrar=true;
		} else {
			//this.disableBtnGuardar=true;
			listaUce=null;
			
		}
		_reset();
	}
	
	// nes - esto está en estudio ...
	public void cargarCurrentUsuarioCuentasEmail(){
		
		System.out.println("------------->>>>>>>>< cargar current tipo de sancion:"+idUsuarioCuentasEmail);
		if (idUsuarioCuentasEmail!=null && idUsuarioCuentasEmail!=0) {
			try {
				currentUCE = new UsuarioCuentasEmailBL().obtenerUsuarioCuentasEmailPorPK(idUsuarioCuentasEmail);
			} catch (UsuarioCuentasEmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
	

	public void guardar() {
		//String retorno=FAIL;
		
		if (idUsuario==null || idUsuario==0) {
			String message = "Ocurrio Un Error: La Cuenta de Correo a almacenar debe estar asociado a un usuario [ Nombre: "
					+ this.currentUCE.getUceNombre();
			super.createWindowMessage(ERROR_MESSAGE, message, null);
		} else { 
				
			if (idUsuarioCuentasEmail!=null && idUsuarioCuentasEmail!=0) {
				//retorno = 
				actualizar();
			} else {
				//retorno = 
				agregar();
			}
			_reset();
			recargaListaUsuario();
		}
		//return retorno;
	}
		

	public String actualizar() {

		try {

			// como no lo tengo lo leo
			UsuarioCuentasEmail savedUCE = new UsuarioCuentasEmailBL()
							.obtenerUsuarioCuentasEmailPorPK(idUsuarioCuentasEmail);
			
			
			savedUCE.setUceNombre(currentUCE.getUceNombre());
			savedUCE.setUceEmail(currentUCE.getUceEmail());
			savedUCE.setUceDireccionDeRespuesta(currentUCE.getUceDireccionDeRespuesta());
			savedUCE.setUceTextoDeLaFirma(currentUCE.getUceTextoDeLaFirma());
			
		    DesEncrypter encrypter = new DesEncrypter(PASS_PHRASE);
			savedUCE.setContraseniaSalida(encrypter.encrypt(currentUCE.getContraseniaSalida()));
			
		
			savedUCE.setFormato(currentUCE.getFormato());
			savedUCE.setMetodoIdentificacion(currentUCE.getMetodoIdentificacion());
			savedUCE.setMetodoIdentificacionSalida(currentUCE.getMetodoIdentificacionSalida());
			savedUCE.setNombreDelServidorEntrada(currentUCE.getNombreDelServidorEntrada());
			savedUCE.setNombreServidorDeSalida(currentUCE.getNombreServidorDeSalida());
			savedUCE.setNombreUsuarioSalida(currentUCE.getNombreUsuarioSalida());
			savedUCE.setPreferida(currentUCE.isPreferida());
			savedUCE.setPuertoEntrada(currentUCE.getPuertoEntrada());
			savedUCE.setPuertoSalida(currentUCE.getPuertoSalida());
			savedUCE.setSeguridadConexion(currentUCE.getSeguridadConexion());
			savedUCE.setSeguridadDeLaConexionSalida(currentUCE.getSeguridadDeLaConexionSalida());
			savedUCE.setTipoServidorEntrada(currentUCE.getTipoServidorEntrada());
		
			
			new UsuarioCuentasEmailBL()
							.actualizar(savedUCE);
					
//			retorno=ConstantesDAP.SUCCESS_UCE;    //rrl 20110614 DEPENDENCIA NO NECESARIA

			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla
		
		} catch (UsuarioCuentasEmailException e) {
			String message = "UsuarioCuentasEmailBean.actualizar() UsuarioCuentasEmailException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

//		return retorno;    //rrl 20110614 DEPENDENCIA NO NECESARIA
		return null;
	}

	// nes 20100927
	public String agregar() {

//		String retorno = FAIL;  //rrl 20110614 DEPENDENCIA NO NECESARIA

		UsuarioCuentasEmailBL uceBL = new UsuarioCuentasEmailBL();

		try {

			// nes 20101013
			
			currentUCE
				.setIdUsuario(this.idUsuario);
			
			uceBL.agregar(currentUCE);
			
//			retorno=ConstantesDAP.SUCCESS_UCE;   //rrl 20110614 DEPENDENCIA NO NECESARIA
			
			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla

			
		} catch (UsuarioCuentasEmailException e) {
			String message = "UsuarioCuentasEmailBean.agregar() UsuarioCuentasEmailException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} 

//		return retorno;    //rrl 20110614 DEPENDENCIA NO NECESARIA
		return null;

	}
	
	public void enableBtnBorrar(){
		
		if(btnBorrar != null){
			btnBorrar.setDisabled(false);	
		}			
		
		setDisableBtnBorrar(false);
		
		//return null;
	}
	
	public void enableBtnGuardar(){
		
		if(btnGuardar != null){
			btnGuardar.setDisabled(false);	
		}			
		
		setDisableBtnBorrar(false);
		
		//return null;
	}

	public String borra(ActionEvent event) {
		return this.borra();
	}
	
	// nes 20100927
	public String borra() {

//		String retorno = FAIL;    //rrl 20110614 DEPENDENCIA NO NECESARIA

		UsuarioCuentasEmailBL uceBL = new UsuarioCuentasEmailBL();
		
		try {

			currentUCE = uceBL.obtenerUsuarioCuentasEmailPorPK(idUsuarioCuentasEmail);
			
			uceBL.borrar(currentUCE);
//			retorno = ConstantesDAP.SUCCESS_UCE;    //rrl 20110614 DEPENDENCIA NO NECESARIA
			_reset();
			recargaListaUsuario(); 
			
		} catch (UsuarioCuentasEmailException e) {
			String message = "UsuarioCuentasEmailBean.borra() UsuarioCuentasEmailException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

//		return retorno;    //rrl 20110614 DEPENDENCIA NO NECESARIA
		return null;

	}

	
	/**
	 * @return the currentUCE
	 */
	public UsuarioCuentasEmail getCurrentUCE() {
		if (currentUCE == null) {
			currentUCE = new UsuarioCuentasEmail();
		}
		return currentUCE;
	}


	/**
	 * @param currentUCE the currentUCE to set
	 */
	public void setCurrentUCE(UsuarioCuentasEmail currentUCE) {
		this.currentUCE = currentUCE;
	}


//	public Usuario getUsuario() {
//		if (usuario == null) {
//
//			usuario = new Usuario();
//
//		}
//		return usuario;
//	}
//	
//	public void setUsuario(Usuario usuario) {
//		this.usuario = usuario;
//	}

	// COMENTARIO : ESTO NO SE PARA Q SERIA PERO EN PPIO NO VA ...
//	
//	public Integer getIdUsuario() {
//		return getUsuarioCuentasEmail().getUsuario().getIdUsuario();
//	}
//	
//	public void setIdUsuario(Integer id) {
//		getUsuarioCuentasEmail().getUsuario().setIdUsuario(id);
//	}
//
//	public Integer getIdUce() {
//		return getUsuarioCuentasEmail().getIdUce();
//	}
//	
//	public void setIdUsuarioCuentasEmail(Integer id) {
//		getUsuarioCuentasEmail().setIdUsuarioCuentasEmail(id);
//	}

	public void changeUsuario() {

		try {
			this.listaUce = (ArrayList<UsuarioCuentasEmail>) new UsuarioCuentasEmailBL()
				.obtenerUsuarioCuentasEmailPorUsuario(this.idUsuario);

				 		
		} catch (UsuarioCuentasEmailException e) {
			String message = "UsuarioCuentasEmailBean.changeUsuario() UsuarioCuentasEmailException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
	}
	


//	public List<UsuarioCuentasEmail> getListaTiposSancion() {
///*
//		if (this.listaTiposSancion==null) {
//			UsuarioCuentasEmailBL uceBL = new UsuarioCuentasEmailBL();
//	
//			//List<UsuarioCuentasEmail> retorno = null;
//	
//			try {
//	
//				this.listaTiposSancion = uceBL.obtenerTiposSancion();
//				//this.listaTiposSancion = null;
//	
//			} catch (UsuarioCuentasEmailException ex) {
//	
//				logger.info("Ocurrio Un Error al tratar de obtener la lista de TiposSancion: "
//						+ ex.getMessage());
//	
//			}
//		}
//	*/	
//		return listaTiposSancion;
//	}

//	@SuppressWarnings("unchecked")
//	public List<SelectItem> getListaUsuariosCombo() {
//
//		List<SelectItem> retorno = new ArrayList<SelectItem>();
//		UsuarioBL usuarioBL = new UsuarioBL();
//		List<SelectItem> listadousuarios = null;
//		try {
//			listadousuarios = usuarioBL.obtenerUsuariosNombreParaCombo("Seleccionar...", "" );
//			for (SelectItem a : listadousuarios)
//				retorno.add(a);
//
//		} catch (UsuarioException e) {
//
//			logger
//					.info("Ocurrio Un Error al tratar de OIbtener el Listado de Usuarios para el Crear el Combo: "
//							+ e.getMessage());
//		}
//
//		return retorno;
//	}


//	@SuppressWarnings("unchecked")
//	public List<SelectItem> getListadoTiposSancion() {
//
//		List<SelectItem> retorno = new ArrayList<SelectItem>();
//		UsuarioCuentasEmailBL uceBL = new UsuarioCuentasEmailBL();
//		List<UsuarioCuentasEmail> listadouces = null;
//
//		try {
//
//			listadouces = uceBL.obtenerTiposSancion();
//			for (UsuarioCuentasEmail a : listadouces)
//				retorno.add(new SelectItem(a.getIdUce(), a.getUceNombreCorto()));
//
//		} catch (UsuarioCuentasEmailException e) {
//
//			logger
//					.info("Ocurrio Un Error al tratar de OIbtener el Listado de TiposSancion para el Crear el Combo: "
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
		
		logger.debug("isDisableBtnBorrar:IdUsuarioCuentasEmail = " + this.currentUCE.getUceNombre());
		
		if( isEmpty(this.currentUCE.getIdUce()) || this.currentUCE.getIdUce() == 0){ // nes 20100927
			setDisableBtnBorrar(true);
		}else{
			setDisableBtnBorrar(false);
		}
		
		return disableBtnBorrar;
	}
	
	public boolean isDisableBtnGuardar() {
		
		logger.debug("isDisableBtnGuardar:IdUsuarioCuentasEmail = " + this.currentUCE.getUceNombre());
		
		if( isEmpty(this.idUsuarioCuentasEmail) || this.idUsuarioCuentasEmail == 0){ // nes 20101119
			this.disableBtnGuardar = true;
		}else{
			this.disableBtnGuardar = false;
		}
		
		return disableBtnBorrar;
	}
	
	
	public boolean isEmpty(Object obj){
		
		return ( obj == null || obj.toString().trim().equals("") );				
		
	}

	public void setDisableBtnBorrar(boolean disableBtnBorrar) {
		this.disableBtnBorrar = disableBtnBorrar;
	}
	
	public void setDisableBtnGuardar (boolean disableBtnGuardar) {
		this.disableBtnGuardar = disableBtnGuardar;
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

	
	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * @return the idUsuario
	 */
	public Integer getIdUsuario() {
		return idUsuario;
	}


	


	/**
	 * @return the listaUsuarios
	 * 
	 * Nota: hago que el get cargue el combo si no está cargado
	 *       El set no lo necesito porque no voy a setear nada en la lista de usuarios
	 */
	public List<SelectItem> getListaUsuarios() {
		
		if (this.listaUsuarios==null || this.listaUsuarios.size()==0) {
			
			try {
				this.listaUsuarios = UtilsVs.castStringPairToSelectitem( new UsuarioBL()
												.obtenerUsuariosParaCombo("Seleccionar ...", null) );
				
			} catch (UsuarioException e) {
				String message = "UsuarioCuentasEmailBean.getListaUsuarios() UsuarioException: ";
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			}
		}
		
		return listaUsuarios;
	}


	/**
	 * @return the idUsuarioCuentasEmail
	 */
	public Integer getIdUsuarioCuentasEmail() {
		return idUsuarioCuentasEmail;
	}


	/**
	 * @param idUsuarioCuentasEmail the idUsuarioCuentasEmail to set
	 */
	public void setIdUsuarioCuentasEmail(Integer idUsuarioCuentasEmail) {
		this.idUsuarioCuentasEmail = idUsuarioCuentasEmail;
	}


	/**
	 * @return the listaUce
	 */
	public ArrayList<UsuarioCuentasEmail> getListaUce() {
		
		//recargaListaUsuario();
		return listaUce;
	}


	/**
	 * @param listaUce the listaUce to set
	 */
	public void setListaUce(ArrayList<UsuarioCuentasEmail> listaUce) {
		this.listaUce = listaUce;
	}


	/**
	 * @return the uceNombre
	 */
	public String getUceNombre() {
		return uceNombre;
	}


	/**
	 * @param uceNombre the uceNombre to set
	 */
	public void setUceNombre(String uceNombre) {
		this.uceNombre = uceNombre;
	}


	public void setUceEmail(String uceEmail) {
		this.uceEmail = uceEmail;
	}


	public String getUceEmail() {
		return uceEmail;
	}

	
	public List<String> getListaFormatos() {
		
		List<String> listaFormatos = new ArrayList<String>();
		
		listaFormatos.add("Texto");
		listaFormatos.add("HTML");
		
		return listaFormatos;
	}
	
	public List<String> getListaSeguridad() {
		
		List<String> listaSeguridad = new ArrayList<String>();
		
		listaSeguridad.add("none");
		listaSeguridad.add("STARTTLS");
		listaSeguridad.add("SSL/TLS");
				
		return listaSeguridad;
	}
	

}
