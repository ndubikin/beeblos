package org.beeblos.bpm.wc.taglib.bean.micuenta;

import static org.beeblos.bpm.core.util.Constants.PASS_PHRASE;
import static org.beeblos.bpm.core.util.Constants.FAIL;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlCommandButton;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.beeblos.security.st.bl.UsuarioBL;
import org.beeblos.security.st.bl.UsuarioCuentasEmailBL;
import org.beeblos.security.st.error.UsuarioCuentasEmailException;
import org.beeblos.security.st.error.UsuarioException;
import org.beeblos.security.st.model.UsuarioCuentasEmail;
import com.sp.common.util.StringPair;
import org.beeblos.bpm.core.util.DesEncrypter;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.Constantes;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import com.sp.common.jsf.util.UtilsVs;

public class MiUsuarioCuentasEmailBean extends CoreManagedBean {

	private static final long serialVersionUID = -4021524365949197101L;

	private static final Log logger = LogFactory.getLog(MiUsuarioCuentasEmailBean.class);
	

	
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
	
	//rrl 20120201
	private String contraseniaSalidaEstatica;
	private boolean editableContraseniaSalida;
	private boolean cambiadoContraseniaSalida;
	
	//rrl 20120612
	private String accion; // la accion para la pantalla p.e: GENERIC_CRUD

	
	public MiUsuarioCuentasEmailBean() {

		super();
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);
		
		this.idUsuario = cs.getUsuario().getIdUsuario();
		
		
		_init();
		
		getListaUsuarios();

	}
	
	private void _init() {
		
		this.currentUCE = new UsuarioCuentasEmail();
		this.currentUCE.setFormato("Texto");  // rrl 20110727 //TODO: OJO !! En la BBDD este campo está como IS NOT NULL si se va ha quitar
		
		this.idUsuarioCuentasEmail=0;
		this.uceNombre=null;
		this.uceEmail=null;
		
		this.disableBtnBorrar=true;
		this.disableBtnGuardar = true;

		//rrl 20120201
		editableContraseniaSalida = false;
		cambiadoContraseniaSalida = false;
		
		
		try {
			this.listaUce = (ArrayList<UsuarioCuentasEmail>) new UsuarioCuentasEmailBL()
				.obtenerUsuarioCuentasEmailPorUsuario(this.idUsuario);

				 		
		} catch (UsuarioCuentasEmailException e) {
			logger.error("Ocurrio Un Error: No debe " 
					+ e.getMessage() + " : " + e.getCause());
		
			String params[] = {this.currentUCE.getIdUce().toString(), "Ocurrio Un Error al tratar de obtener la lista de usuarios:" 
				+ e.getMessage() + " : " + e.getCause() };
			agregarMensaje("53",e.getMessage(),params,FGPException.ERROR);	
		}
		
		
		
	}
	
	
	private void _reset() {
		
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);

		//rrl 20120612 El idUsuario es seleccionado por un COMBO cuando accion es GENERIC_CRUD 
		if (accion==null || accion.equals("")) {
			this.idUsuario = cs.getUsuario().getIdUsuario();
		}
		accion = "";
		
		this.currentUCE = new UsuarioCuentasEmail();
		this.currentUCE.setFormato("Texto");  // rrl 20110727 //TODO: OJO !! En la BBDD este campo está como IS NOT NULL si se va ha quitar
		this.idUsuarioCuentasEmail=0;
		
		this.uceNombre=null;
		this.uceEmail=null;
		
		try {
			this.listaUce = (ArrayList<UsuarioCuentasEmail>) new UsuarioCuentasEmailBL()
				.obtenerUsuarioCuentasEmailPorUsuario(this.idUsuario);

				 		
		} catch (UsuarioCuentasEmailException e) {
			logger.error("Ocurrio Un Error: No debe " 
					+ e.getMessage() + " : " + e.getCause());
		
			String params[] = {this.currentUCE.getIdUce().toString(), "Ocurrio Un Error al tratar de obtener la lista de usuarios:" 
				+ e.getMessage() + " : " + e.getCause() };
			agregarMensaje("53",e.getMessage(),params,FGPException.ERROR);	
		}
		
		
		this.disableBtnBorrar=true;
		this.disableBtnGuardar = true;
		
		//rrl 20120201
		editableContraseniaSalida = false;
		cambiadoContraseniaSalida = false;
		
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
				
				//rrl 20120201
				contraseniaSalidaEstatica = currentUCE.getContraseniaSalida();
				if (contraseniaSalidaEstatica!=null && !"".equals(contraseniaSalidaEstatica.trim())) {
					cambiadoContraseniaSalida = false;
					editableContraseniaSalida = false;
				} else {
					cambiadoContraseniaSalida = true;
					editableContraseniaSalida = true;
				}
				
			} catch (UsuarioCuentasEmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
	
	// si tiene id es actualización, si no lo tiene es alta ...
	// nes 20101013 - cambié la firma - debe devolver String ...
	public String guardar() {
		
		if (idUsuario==null || idUsuario==0) {
			//martin - 20100930
			logger.error("Ocurrio Un Error: La Cuenta de Correo a almacenar debe estar asociado a un usuario [ Nombre: "
					+ this.currentUCE.getUceNombre());
			
			String params[] = {this.currentUCE.getUceNombre() + ",", "Error: La Cuenta de Correo a almacenar debe estar asociado a un usuario." }; // nes 20101013				
			agregarMensaje("53",null,params,FGPException.ERROR);	
		} else { 
			
			if (idUsuarioCuentasEmail!=null && idUsuarioCuentasEmail!=0) {
				actualizar(); //rrl 20120201
			} else {
				agregar();  //rrl 20120201
			}
			_reset();
			recargaListaUsuario();
		}
		
		return null;
	}
		
	// nes 20101013 - cambié la firma - debe devolver String ...
	public String actualizar() {
		
		String retorno = FAIL;

		try {

			// uce.setUsuario(usuario); COMENTARIO: no es necesario hacer el set porque no se cambia el usuario

			// como no lo tengo lo leo
			UsuarioCuentasEmail savedUCE = new UsuarioCuentasEmailBL()
							.obtenerUsuarioCuentasEmailPorPK(idUsuarioCuentasEmail);
			
			
			savedUCE.setUceNombre(currentUCE.getUceNombre());
			savedUCE.setUceEmail(currentUCE.getUceEmail());
			savedUCE.setUceDireccionDeRespuesta(currentUCE.getUceDireccionDeRespuesta());
//			savedUCE.setUceTextoDeLaFirma(currentUCE.getUceTextoDeLaFirma());    //rrl 20110727 Campo no se utiliza
			
//			//HZC:11012011, cifrar la contrasenia
//		    DesEncrypter encrypter = new DesEncrypter(PASS_PHRASE);
//			savedUCE.setContraseniaSalida(encrypter.encrypt(currentUCE.getContraseniaSalida()));
			
			//rrl 20120102
			if (cambiadoContraseniaSalida) {
				
				if (currentUCE.getContraseniaSalida()!=null && !"".equals(currentUCE.getContraseniaSalida())) {
				    DesEncrypter encrypter = new DesEncrypter(PASS_PHRASE);
					savedUCE.setContraseniaSalida(encrypter.encrypt(currentUCE.getContraseniaSalida()));
				} else {
					savedUCE.setContraseniaSalida(null);
				}
			} else {
				savedUCE.setContraseniaSalida(contraseniaSalidaEstatica);
			}
			
			savedUCE.setFormato(currentUCE.getFormato());   //rrl 20110727 Campo no se utiliza  //TODO: OJO !! En la BBDD este campo está como IS NOT NULL si se va ha quitar
			
			//rrl 20110727 Campo no se utiliza
			// Rellena el texto de la firma con el tipo Texto/Html
//			if (currentUCE.getFormato().equals("Texto")) {
//				savedUCE.setUceFirmaAdjuntaHtml(null);
//				savedUCE.setUceFirmaAdjuntaTxt(currentUCE.getUceFirmaAdjuntaTxt());
//			} else {
//				savedUCE.setUceFirmaAdjuntaHtml(currentUCE.getUceFirmaAdjuntaHtml());
//				savedUCE.setUceFirmaAdjuntaTxt(null);
//			}
			
			savedUCE.setUceFirmaAdjuntaTxt(currentUCE.getUceFirmaAdjuntaTxt());
			savedUCE.setUceFirmaAdjuntaHtml(currentUCE.getUceFirmaAdjuntaHtml());
				
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
					
			retorno=Constantes.SUCCESS_UCE;

			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla
		
		} catch (UsuarioCuentasEmailException e) {

			//martin - 20100930
			logger.error("Ocurrio Un Error al tratar de Actualizar el UsuarioCuentasEmail: [ id = " 
					+ this.currentUCE.getIdUce() + " ; Nombre: "
					+ this.currentUCE.getUceNombre()
					+"] "	+ e.getMessage() + " : " + e.getCause());
			
			String params[] = {this.currentUCE.getIdUce() + ",", "Error al actualizar UsuarioCuentasEmail "+e.getMessage()+"\n"+e.getCause() }; // nes 20101013				
			agregarMensaje("53",e.getMessage(),params,FGPException.ERROR);		
			
		}

		return retorno;
	}

	// nes 20100927
	public String agregar() {

		String retorno = FAIL;

		UsuarioCuentasEmailBL uceBL = new UsuarioCuentasEmailBL();

		try {

			// nes 20101013
			
			currentUCE
				.setIdUsuario(this.idUsuario);

			// rrl 20110727 Campo no se utiliza
			// Rellena el texto de la firma con el tipo Texto/Html
//			if (currentUCE.getFormato().equals("Texto")) {
//				currentUCE.setUceFirmaAdjuntaHtml(null);
//			} else {
//				currentUCE.setUceFirmaAdjuntaTxt(null);
//			}

			//rrl 20120201
			if (currentUCE.getContraseniaSalida()!=null && !"".equals(currentUCE.getContraseniaSalida())) {
			    DesEncrypter encrypter = new DesEncrypter(PASS_PHRASE);
				String contraseniaSalida = encrypter.encrypt(currentUCE.getContraseniaSalida());
				currentUCE.setContraseniaSalida(contraseniaSalida);
			} else {
				currentUCE.setContraseniaSalida(null);
			}
			
			uceBL.agregar(currentUCE);
			
			retorno=Constantes.SUCCESS_UCE;
			
			setShowHeaderMessage(true); // muestra mensaje de OK en pantalla

			
		} catch (UsuarioCuentasEmailException ex2) {

			//martin - 20100930
			logger.error("Ocurrio Un Error al tratar de Agregar el UsuarioCuentasEmail: [Nombre: " 
					+ this.currentUCE.getUceNombre()
					+"] "	+ ex2.getMessage() + " : " + ex2.getCause());
			
			String params[] = {this.currentUCE.getUceNombre().toString(), "Error al Agregar UsuarioCuentasEmail "
						+ex2.getMessage()+"\n"+ex2.getCause() };	// nes 20101013			
			agregarMensaje("53",ex2.getMessage(),params,FGPException.ERROR);	
		
			
		} 

		return retorno;

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

		String retorno = FAIL;

		UsuarioCuentasEmailBL uceBL = new UsuarioCuentasEmailBL();
		
		try {

			currentUCE = uceBL.obtenerUsuarioCuentasEmailPorPK(idUsuarioCuentasEmail);
			
			uceBL.borrar(currentUCE);
			retorno = Constantes.SUCCESS_UCE;
			_reset();
			recargaListaUsuario(); 
			
		} catch (UsuarioCuentasEmailException e) {

			//martin - 20100930
			logger.error("Ocurrio Un Error al tratar de Borrar el UsuarioCuentasEmail: [ id = " 
					+ this.currentUCE.getIdUce() + " ; Nombre: "
					+ this.currentUCE.getUceNombre()
					+"] "	+ e.getMessage() + " : " + e.getCause());
			
			String params[] = {this.currentUCE.getIdUce() + ",", "Error al borrar UsuarioCuentasEmail "
					+e.getMessage()+"\n"+e.getCause() }; // nes 20101013				
			agregarMensaje("53",e.getMessage(),params,FGPException.ERROR);			
		}

		return retorno;

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
			logger.error("Ocurrio Un Error: No debe " 
					+ e.getMessage() + " : " + e.getCause());
		
			String params[] = {this.currentUCE.getIdUce().toString(), "Ocurrio Un Error al tratar de obtener la lista de usuarios:" 
				+ e.getMessage() + " : " + e.getCause() };
			agregarMensaje("53",e.getMessage(),params,FGPException.ERROR);	
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
//				this.listaUsuarios = UtilsVs.castStringPairToSelectitem( new UsuarioBL()
//												.obtenerUsuariosParaCombo("Seleccionar ...", null));
				this.listaUsuarios = UtilsVs.castStringPairToSelectitem(
						new UsuarioBL()
						.obtenerUsuariosParaCombo("Seleccionar ...", null));
				
			} catch (UsuarioException e) {
				
				logger.error("Ocurrio Un Error al tratar de obtener la lista de usuarios:" 
							+ e.getMessage() + " : " + e.getCause());
				
				String params[] = {this.currentUCE.getIdUce().toString(), "Ocurrio Un Error al tratar de obtener la lista de usuarios:" 
						+ e.getMessage() + " : " + e.getCause() };		
				agregarMensaje("53",e.getMessage(),params,FGPException.ERROR);	
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
	
	//rrl 20120201
	public List<SelectItem> getListaSeguridad() {
		
		List<SelectItem> listaSeguridad = new ArrayList<SelectItem>();
		
		listaSeguridad.add(new SelectItem("none", "NONE"));
		listaSeguridad.add(new SelectItem("STARTTLS", "STARTTLS"));
		listaSeguridad.add(new SelectItem("SSL/TLS", "SSL/TLS"));
		
		return listaSeguridad;
	}
	
	//rrl 20120201
	public String getContraseniaSalidaEstatica() {
		return contraseniaSalidaEstatica;
	}

	public void setContraseniaSalidaEstatica(String contraseniaSalidaEstatica) {
		this.contraseniaSalidaEstatica = contraseniaSalidaEstatica;
	}

	public boolean isEditableContraseniaSalida() {
		return editableContraseniaSalida;
	}

	public void setEditableContraseniaSalida(boolean editableContraseniaSalida) {
		this.editableContraseniaSalida = editableContraseniaSalida;
	}

	public boolean isCambiadoContraseniaSalida() {
		return cambiadoContraseniaSalida;
	}

	public void setCambiadoContraseniaSalida(boolean cambiadoContraseniaSalida) {
		this.cambiadoContraseniaSalida = cambiadoContraseniaSalida;
	}
	
	public String modificarContrasenia() {
		
		editableContraseniaSalida = true;
		cambiadoContraseniaSalida = true;
		currentUCE.setContraseniaSalida(null);
		
		return null;
	}
	
	public String cambiarContraseniaSalida() {
		
		cambiadoContraseniaSalida = true;
		
		return null;
	}

	//rrl 20120612
	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}
	

}
