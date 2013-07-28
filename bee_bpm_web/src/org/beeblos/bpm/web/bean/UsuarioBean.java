package org.beeblos.bpm.web.bean;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_PASS;
import static org.beeblos.bpm.core.util.Constants.FAIL;
import static org.beeblos.bpm.core.util.Constants.SUCCESS_USUARIO;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.security.MD5Hash;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.UtilsVs;
import org.beeblos.security.st.bl.DepartamentoBL;
import org.beeblos.security.st.bl.UsuarioBL;
import org.beeblos.security.st.bl.UsuarioCuentasEmailBL;
import org.beeblos.security.st.error.DepartamentoException;
import org.beeblos.security.st.error.UsuarioCuentasEmailException;
import org.beeblos.security.st.error.UsuarioException;
import org.beeblos.security.st.model.Usuario;
import org.beeblos.security.st.model.UsuarioCuentasEmail;

public class UsuarioBean extends CoreManagedBean {

	private static final Log logger = LogFactory.getLog(UsuarioBean.class);

	private static final long serialVersionUID = -3619314142932182990L;
	
	private Integer idUsuario = 0;
	private String nombres;
	private String apellidos;
	private String password;
	private String suLectura;
	private String usuarioLogin;
	private String admon;
	
	private Usuario currentUsuario;

	private String email;
	private Integer idDepto = 0;
	private Integer idCorreo = 0;
	
	private List<Usuario> usuarios = new ArrayList<Usuario>();
	private List<UsuarioCuentasEmail> listaEmail = new ArrayList<UsuarioCuentasEmail>();

	private UsuarioCuentasEmail currentEmail;
	private Integer currentRow;
	
	//Para saber si pintar o no la lista de correos
	private boolean listaEmailVacia;
	
	//Necesario para reseteo del currentEmail
	private UsuarioCuentasEmail newEmail = new UsuarioCuentasEmail();
	
//	@SuppressWarnings("unused")
//	private List<SelectItem> listadoAreas;
	
	
	private List<SelectItem> listadoDeptos;

	private boolean showResetPassMsg = false ;	
	
	private String msgUsuarios;
	private boolean muestraBotonBorra;
	private boolean muestraBtnReset;
	private boolean muestraNuevaAlta;
	private boolean muestraPassword;
	private boolean disableBotonBorrar;
	private String valueBtn;
	
	
	public UsuarioBean() {
		super();
		
		reset();
	}
	
	public void reset(){
		limpiaBean();
		limpiaPantalla();
	}
	
	private String limpiaBean() {

		this.idUsuario = 0;
		this.currentUsuario = new Usuario();
				
		this.idDepto = 0;
		
		//Valores por defecto
		this.currentUsuario.setPassword("");
		this.currentUsuario.setHabilitado("S");
		this.currentUsuario.setAdmon("N");
		this.currentUsuario.setJefeDepartamento("N");
		this.currentUsuario.setSuLectura("S");
		this.currentUsuario.setEmail("");
	
		//this.currentUsuario.apellidos = "";
		//this.currentUsuario.usuarioLogin = " ";
		
		//this.currentUsuario.nombres = " ";
		
		//this.currentUsuario.setIdArea(0);
		
				
		this.currentEmail = new UsuarioCuentasEmail();
		this.listaEmail = new ArrayList<UsuarioCuentasEmail>();
		
		this.currentRow=0;
		
		//this.idDepto = 0;
		
		this.showResetPassMsg = false;	
		
		return SUCCESS_USUARIO;

	}

	public String limpiaPantalla() {

		this.muestraBotonBorra = false;
		this.setDisableBotonBorrar(true);
		this.muestraPassword = true;
		this.muestraBtnReset = false;
		this.muestraNuevaAlta = false;
		
		this.valueBtn="Guardar";
		
		
		//getSession().setAttribute("usuarioBean", null);
		//setMsgUsuarios("Ingrese Nuevo Usuario");
		

		return SUCCESS_USUARIO;

	}
	
	
	

	public void guardar() {
		
		if (this.idUsuario!=null && this.idUsuario!=0) {
			actualizar();
		} else {
			agrega();
		}
		reset();
			
	}

	public void actualizar(){

		//Usuario usuarioLocal = new Usuario();

		UsuarioBL uBl = new UsuarioBL();
		Usuario usuarioActualizar;
		try {
			usuarioActualizar = uBl.obtenerUsuarioPorPK(this.idUsuario);
			
			usuarioActualizar.setApellidos(this.currentUsuario.getApellidos());
			usuarioActualizar.setUsuarioLogin(this.currentUsuario.getUsuarioLogin());
			usuarioActualizar.setNombres(this.currentUsuario.getNombres());
			usuarioActualizar.setEmail(this.currentUsuario.getEmail());
			usuarioActualizar.setHabilitado(this.currentUsuario.getHabilitado());
			usuarioActualizar.setJefeDepartamento(this.currentUsuario.getJefeDepartamento());
			usuarioActualizar.setAdmon(this.currentUsuario.getAdmon());
			usuarioActualizar.setSuLectura(this.currentUsuario.getSuLectura());
			usuarioActualizar.setIdDepto(this.currentUsuario.getIdDepto());
			
			uBl.actualizar(usuarioActualizar);
			
		} catch (UsuarioException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
//		if (getPassword() == null) {
//
//			Usuario usuPass = new Usuario();
//			usuPass = uBl.obtenerUsuarioPorPK(usuarioActualizar.getIdUsuario());
//			usuarioActualizar.setPassword(usuPass.getPassword());
//
//		}

	}

	public String agrega() {

		String retorno = "FAIL";
		
		 try {
			 
			// martin - 20101221
			// Codificación de la password
			String hashpass = MD5Hash.encode(this.password.getBytes());
			this.currentUsuario.setPassword(hashpass);
			
			new UsuarioBL().agregar(this.currentUsuario);
			retorno = SUCCESS_USUARIO;
			
		} catch (UsuarioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		UsuarioBL uBl = new UsuarioBL();
//
//		Usuario usuarioLocal = new Usuario();
//
//		String nombreUsuarioLoginDigitado = getUsuarioLogin();
//
//		Integer idUsuarioCapturado = 0;
//
//		if (usuarioLocal.getIdUsuario() == null) {
//
//			idUsuarioCapturado = getIdUsuario();
//		}
//
//		usuarioLocal.setUsuarioLogin(nombreUsuarioLoginDigitado);
//		usuarioLocal.setApellidos(getApellidos());
//		usuarioLocal.setNombres(getNombres());
//		usuarioLocal.setEmail(getEmail());
//		usuarioLocal.setSuLectura(getSuLectura());
//		usuarioLocal.setAdmon(getAdmon());
//		//usuarioLocal.setIdArea(getIdArea());
//
//		if (this.getIdDepto() == null) {
//
//			this.setIdDepto(0);
//		} else {
//			usuarioLocal.setIdDepto(this.getIdDepto());
//		}
//
//		if (getPassword() != null) {
//			String hashPassword = MD5Hash.encode(getPassword().getBytes());
//			usuarioLocal.setPassword(hashPassword);
//		}
//
//		usuarioLocal.setIdUsuario(idUsuarioCapturado);
//		Usuario usuariosEnSistema = null;
//
//		try {
//
//			usuariosEnSistema = uBl
//					.obtenerUsuarioLoginPorNombre(nombreUsuarioLoginDigitado);
//			if (idUsuarioCapturado == 0 && usuariosEnSistema == null) {
//
//				uBl.agregar(usuarioLocal);
//
//				retorno = Constantes.SUCCESS_USUARIO;
//
//				limpiaBean();
//
//			} else {
//
//				usuariosEnSistema = uBl.obtenerUsuarioPorPK(idUsuarioCapturado);
//
//				if (usuariosEnSistema != null) {
//
//					actualizar();
//
//					retorno = Constantes.SUCCESS_USUARIO;
//
//					limpiaBean();
//
//				} else {
//
//					agregarMensaje(FacesMessage.SEVERITY_ERROR,
//							"errorUsuarioEnSistema", null);
//					limpiaBean();
//
//				}
//
//			}
//		} catch (UsuarioInsertException ex1) {
//
//			logger.info("Ocurrio Un Error al tratar de insetar un Usuario: "
//					+ ex1.getMessage());
//
//		} catch (UsuarioException ex2) {
//
//			logger.info("Ocurrio Un Error al tratar de Actualizar un Usuario: "
//					+ ex2.getMessage());
//
//		}
//
//		// Ajustes de los botones desde el bean RRG 29/01/2008
//		this.muestraBotonBorra = false;
//		this.muestraBotonGuardar = true;
//		this.muestraBotonModif = false;
//		this.muestraPassword = true;
//		this.muestraBtnReset = false;
//		this.muestraNuevaAlta = false;
//
//		getSession().setAttribute("usuarioBean", null);

		
		return retorno;

	}

	public String borra() {
		
		logger.debug(" borra() :" +this.getIdUsuario());
		
		
		String retorno = "FAIL";

		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);

		Usuario usuarioLocal = new Usuario();

		usuarioLocal.setIdUsuario(getIdUsuario());

		UsuarioBL uBl = new UsuarioBL();

		try {

			uBl.borrar(usuarioLocal);

			logger.info("Usuario Borrado por :" + cs.getUsuarioLogin() + " - "
					+ cs.getUsuario().getNombres() + " "
					+ cs.getUsuario().getApellidos());

			retorno = SUCCESS_USUARIO;
			limpiaBean();

		} catch (UsuarioException e) {

			logger.info("Ocurrio Un Error al borrar  un Usuario: "
					+ e.getMessage());

		}

//		// Para el manejo de los botones y la constrase�a desde el Bean RRG
//		// 29/01/2008
//		this.muestraBotonBorra = false;
//		this.muestraBotonGuardar = true;
//		this.muestraBotonModif = false;
//		this.muestraPassword = true;
//		this.muestraBtnReset = false;
//		this.muestraNuevaAlta = true;
////		setMsgUsuarios("Nuevo Usuario");
////		getSession().setAttribute("usuarioBean", null);

		reset();
		
		return retorno;

	}
	
	public String nuevoUsuario (ActionEvent event){
		reset();
		return SUCCESS_USUARIO;
	}
	
	
	public void resetListaEmail(){
		this.currentEmail = new UsuarioCuentasEmail();
		getListaEmail();
	}
		
//	public void agregarCuentaCorreo (ActionEvent event){
//			
//		agregarCuentaCorreo ();
//	}
	
	public void agregarCuentaCorreo (){
		
		String retorno = FAIL;
		
		if (this.currentEmail!=null && this.currentEmail.getIdUce()==0){
			
			try {
				new UsuarioCuentasEmailBL().agregar(this.currentEmail);
			} catch (UsuarioCuentasEmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			resetListaEmail();
			retorno = SUCCESS_USUARIO;
		}
		//return retorno;
		
	}
	
//	public void actualizarCuentaCorreo (ActionEvent event){
//		
//		actualizarCuentaCorreo ();
//	}
	
	public void actualizarCuentaCorreo (){
		
		if (this.currentEmail !=null && this.currentEmail.getIdUce()!=0){
			
			try {
				new UsuarioCuentasEmailBL().actualizar(this.currentEmail);
			} catch (UsuarioCuentasEmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		resetListaEmail();
	}
	
	public void borrarCuentaCorreo (){
		
		if (this.currentEmail !=null && this.currentEmail.getIdUce()!=0){
			
			try {
				new UsuarioCuentasEmailBL().borrar(this.currentEmail);
			} catch (UsuarioCuentasEmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		resetListaEmail();
	}
	

	/*
	 * Metodo para buscar usuarios por login de Usuario en el Sistema RRG
	 * 14/01/2008
	 */
//	public String buscaUsuario() {
//
//		String retorno = "FAIL";
//		Usuario usuario = new Usuario();
//		UsuarioDao uDao = new UsuarioDao();
//		try {
//			usuario = uDao.obtenerUsuarioLoginPorNombre(this.usuarioLogin);
//			if (usuario != null) {
//
//				this.admon = usuario.getAdmon();
//				this.apellidos = usuario.getApellidos();
//				this.nombres = usuario.getNombres();
//				this.email = usuario.getEmail();
//				//this.idArea = usuario.getIdArea();
//				this.idDepto = usuario.getIdDepto();
//				this.idUsuario = usuario.getIdUsuario();
//				this.suLectura = usuario.getSuLectura();
//				this.usuarioLogin = usuario.getUsuarioLogin();
//
//				// Modificacion para el manejo de la Actualizacion 29/01/2008
//				// RRG
//
//				this.muestraBotonBorra = true;
//				this.muestraBotonGuardar = false;
//				this.muestraBotonModif = true;
//				this.muestraPassword = false;
//				this.muestraBtnReset = true;
//				this.muestraNuevaAlta = true;
//				
//				setMsgUsuarios("Usuario "+this.usuarioLogin+ " por favor modifique los datos que desee");
//
//				retorno = Constantes.SUCCESS_USUARIO;
//
//				this.usuarios = null;
//
//			} else {
//
//				agregarMensaje(FacesMessage.SEVERITY_ERROR,
//						"errorUsuarioNoExiste", null);
//				this.usuarios = null;
//				limpiaBean();
//
//			}
//
//		} catch (UsuarioException e) {
//			logger
//					.info("Ocurrio Un Error al tratar de insetar un Depto: "
//							+ e.getMessage());
//		}
//		return retorno;
//
//	}

	/*
	 * ajax - maneja cambio desplegable Area
	 * 
	 * nes 20080108
	 */
	public void changeArea() {

	}

	public String getAdmon() {
		return admon;
	}

	public String getApellidos() {
		return apellidos;
	}


	public String getEmail() {
		return email;
	}

//	public Integer getIdArea() {
//		return idArea;
//	}

	public Integer getIdDepto() {
		return idDepto;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public String getMsgUsuarios() {
		return msgUsuarios;
	}

	public String getNombres() {
		return nombres;
	}

	public String getPassword() {
		return password;
	}

	public String getSuLectura() {
		return suLectura;
	}

	public String getUsuarioLogin() {
		return usuarioLogin;
	}

	public List<Usuario> getUsuarios() {
		List<Usuario> retorno = new ArrayList<Usuario>();
		try {
			retorno = obtenerUsuarios();
		} catch (UsuarioException e) {
			logger
				.info("Ocurrio Un Error al tratar de insetar un Depto: "
					+ e.getMessage());
		}
		return retorno;
	}

	public boolean isMuestraBotonBorra() {
		return muestraBotonBorra;
	}

	public boolean isMuestraBtnReset() {
		return muestraBtnReset;
	}

	public boolean isMuestraNuevaAlta() {
		return muestraNuevaAlta;
	}

	public boolean isMuestraPassword() {
		return muestraPassword;
	}

	

	public void modificarUsuario() {
		modificarValueBtn();
		if (this.idUsuario!=null && this.idUsuario!=0){
			try {
				this.currentUsuario = new UsuarioBL().obtenerUsuarioPorPK(this.idUsuario);
				
				//modificarValueBtn();
			} catch (UsuarioException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		this.admon = getAdmon();
//		this.apellidos = getApellidos();
//		this.nombres = getNombres();
//		this.email = getEmail();
//		//this.idArea = getIdArea();
//		this.idDepto = getIdDepto();
//		this.idUsuario = getIdUsuario();
//
//		this.suLectura = getSuLectura();
//		this.usuarioLogin = getUsuarioLogin();

		// Ajuste para que muestre los botones de Modif, Borrar y Cambia Pass
		// RRG 29/01/2008

		this.muestraBotonBorra = true;
		
		this.muestraPassword = false;
		this.muestraBtnReset = true;
		this.muestraNuevaAlta = true;
		
		this.showResetPassMsg = false;	
		
		//setMsgUsuarios("Modificacion de Usuarios");
		modificarValueBtn();
		
	}

	public void modificarCorreo() {
		
		if (this.idCorreo!=null && this.idCorreo!=0){
			try {
				this.currentEmail = new UsuarioCuentasEmailBL().obtenerUsuarioCuentasEmailPorPK(this.idUsuario);
			} catch (UsuarioCuentasEmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			modificarValueBtn();
		}
//		this.admon = getAdmon();
//		this.apellidos = getApellidos();
//		this.nombres = getNombres();
//		this.email = getEmail();
//		//this.idArea = getIdArea();
//		this.idDepto = getIdDepto();
//		this.idUsuario = getIdUsuario();
//
//		this.suLectura = getSuLectura();
//		this.usuarioLogin = getUsuarioLogin();

		// Ajuste para que muestre los botones de Modif, Borrar y Cambia Pass
		// RRG 29/01/2008
	
	}
	
	
	private List<Usuario> obtenerUsuarios() throws UsuarioException {

		UsuarioBL uBl = new UsuarioBL();

		return uBl.obtenerUsuarios();

	}

	// martin - 20101222
	public void resetPassword() {

		logger.debug(" usuarioBean:resetPassword:"+this.getIdUsuario()+" - "+this.getUsuarioLogin());

		UsuarioBL uBL = new UsuarioBL();
		
		String hashPassword = MD5Hash.encode(DEFAULT_PASS.getBytes());
		
		try{
			Usuario usuarioLocal = uBL.obtenerUsuarioPorPK(this.idUsuario);

			usuarioLocal.setPassword(hashPassword);

			uBL.actualizar(usuarioLocal);
			
		} catch (UsuarioException ex) {

			logger.info("Ocurrio Un Error al tratar de resetear el  password del Usuario: "
				+ ex.getMessage());
		}
		
		this.showResetPassMsg=true;		

	}
	
	public String buscarUsuarios(ActionEvent e) {
		return buscarUsuarios();
		
	}
	
	public String buscarUsuarios() {
		
		try {
			this.usuarios = (ArrayList<Usuario>) new UsuarioBL()
					.finderUsuario( 
						nombres.trim(), 
						apellidos.trim(),
						usuarioLogin.trim()
						);			
			
			//nResultados = usuarios.size();
					
		} catch (Exception e) {
			logger.warn("error al intentar cargar la lista de propuestas - "+e.getMessage());
		} 
		
		return SUCCESS_USUARIO; 
				
	}

	public void setAdmon(String admon) {
		this.admon = admon;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public void setEmail(String email) {
		this.email = email;
	}

//	public void setIdArea(Integer idArea) {
//		this.idArea = idArea;
//	}

	public void setIdDepto(Integer idDepto) {
		this.idDepto = idDepto;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

//	public List<SelectItem> getListadoAreas() {
//		return listadoAreas;
//	}

	public List<SelectItem> getListadoDeptos() {
		try {
			return UtilsVs
					.castStringPairToSelectitem(
							new DepartamentoBL()
								.obtenerDepartamentosNombreParaCombo("Seleccione...", "BLANCO") );
		} catch (DepartamentoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listadoDeptos;
	}

//	public void setListadoAreas(List<SelectItem> listadoAreas) {
//		this.listadoAreas = listadoAreas;
//	}

	public void setListadoDeptos(List<SelectItem> listadoDeptos) {
		this.listadoDeptos = listadoDeptos;
	}

	public void setMsgUsuarios(String msgUsuarios) {
		this.msgUsuarios = msgUsuarios;
	}

	public void setMuestraBotonBorra(boolean muestraBotonBorra) {
		this.muestraBotonBorra = muestraBotonBorra;
	}

	public void setMuestraBtnReset(boolean muestraBtnReset) {
		this.muestraBtnReset = muestraBtnReset;
	}

	public void setMuestraNuevaAlta(boolean muestraNuevaAlta) {
		this.muestraNuevaAlta = muestraNuevaAlta;
	}

	public void setMuestraPassword(boolean muestraPassword) {
		this.muestraPassword = muestraPassword;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSuLectura(String suLectura) {
		this.suLectura = suLectura;
	}

	public void setUsuarioLogin(String usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public void setListaEmail(List<UsuarioCuentasEmail> listaEmail) {
		this.listaEmail = listaEmail;
	}

	public List<UsuarioCuentasEmail> getListaEmail() {
		try {
			listaEmail = new UsuarioCuentasEmailBL().obtenerUsuarioCuentasEmailPorUsuario(this.idUsuario);
		} catch (UsuarioCuentasEmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listaEmail;
	}

	public void setCurrentEmail(UsuarioCuentasEmail currentEmail) {
		this.currentEmail = currentEmail;
	}

	public UsuarioCuentasEmail getCurrentEmail() {
		return currentEmail;
	}

	public void setCurrentRow(Integer currentRow) {
		this.currentRow = currentRow;
	}

	public Integer getCurrentRow() {
		return currentRow;
	}

	public void setCurrentUsuario(Usuario currentUsuario) {
		this.currentUsuario = currentUsuario;
	}

	public Usuario getCurrentUsuario() {
		return currentUsuario;
	}

	public void setValueBtn(String valueBtn) {
		this.valueBtn = valueBtn;
	}

	public String getValueBtn() {
		return valueBtn;
	}
	
	public void modificarValueBtn() {
		if (idUsuario != 0){
			this.valueBtn = "Actualizar" ;
		} else {
			this.valueBtn = "Guardar";
		}
		this.showResetPassMsg = false;	
	}

	public void setDisableBotonBorrar(boolean disableBotonBorrar) {
		this.disableBotonBorrar = disableBotonBorrar;
	}

	public boolean isDisableBotonBorrar() {
		return !this.isMuestraBotonBorra();
	}
	
	//Para mostrar la tabla de correos o no
	public boolean isListaEmailVacia() {
		this.listaEmail = getListaEmail();
		return (this.listaEmail != null && this.listaEmail.size()!=0);
	}

	public void setNewEmail(UsuarioCuentasEmail newEmail) {
		this.newEmail = newEmail;
	}

	public UsuarioCuentasEmail getNewEmail() {
		UsuarioCuentasEmail newEmailParaAgregar = new UsuarioCuentasEmail();
		newEmailParaAgregar.setIdUsuario(this.idUsuario);
		return newEmailParaAgregar;
	}

	public void setIdCorreo(Integer idCorreo) {
		this.idCorreo = idCorreo;
	}

	public Integer getIdCorreo() {
		return idCorreo;
	}

//	public void setShowResetPassMsg(boolean showResetPassMsg) {
//		this.showResetPassMsg = showResetPassMsg;
//	}

	public boolean isShowResetPassMsg() {
		return showResetPassMsg;
	}
		

	
}
