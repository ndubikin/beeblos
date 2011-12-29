package org.beeblos.bpm.wc.taglib.bean.micuenta;

import static org.beeblos.bpm.wc.taglib.util.Constantes.SUCCESS_MIUSUARIO;

import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.security.MD5Hash;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.UtilsVs;
import org.beeblos.security.st.bl.DepartamentoBL;
import org.beeblos.security.st.bl.UsuarioBL;
import org.beeblos.security.st.error.DepartamentoException;
import org.beeblos.security.st.error.UsuarioException;
import org.beeblos.security.st.model.Usuario;

public class MiUsuarioBean extends CoreManagedBean {

	private static final Log logger = LogFactory.getLog(MiUsuarioBean.class);

	private static final long serialVersionUID = -3619314142932182990L;
	
	private Integer idUsuario = 0;
	private String nombres;
	private String apellidos;
	private String password;
	private String passwordConfirmation;
	private String usuarioLogin;

	
	private Usuario currentUsuario;

	private String email;
	private Integer idDepto = 0;
	
	
	
	private List<SelectItem> listadoDeptos;

	private String msgUsuarios;
	private boolean muestraBotonBorra;
	private boolean muestraBtnReset;
	private boolean muestraNuevaAlta;
	private boolean muestraPassword;
	private boolean disableBotonBorrar;
	private String valueBtn;
	
	
	public MiUsuarioBean() {
		super();
		
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);
		this.idUsuario = cs.getUsuario().getIdUsuario();
		
		reset();
	}
	
	
	public void reset(){
		loadBean();
		limpiaPantalla();
	}
	
	private String loadBean() {

		
		if (this.idUsuario!=null && this.idUsuario!=0){
			try {
				this.currentUsuario = new UsuarioBL().obtenerUsuarioPorPK(this.idUsuario);
				
				this.password = this.currentUsuario.getPassword();
				this.passwordConfirmation = this.password;
				
			} catch (UsuarioException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
		this.idDepto = 0;

		return SUCCESS_MIUSUARIO;

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
		

		return SUCCESS_MIUSUARIO;

	}
	
	
	

	public void guardar() {
		
		if (this.idUsuario!=null && this.idUsuario!=0) {
			actualizar();
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
			usuarioActualizar.setNombres(this.currentUsuario.getNombres());
			
			//usuarioActualizar.setUsuarioLogin(this.currentUsuario.getUsuarioLogin());
			//usuarioActualizar.setIdDepto(this.currentUsuario.getIdDepto());
			
			if ((this.password !=null) && (this.password.trim()!="") && 
				(this.password.equals(this.passwordConfirmation)) 
				){
				
				String hashpass = MD5Hash.encode(this.password.getBytes());
				usuarioActualizar.setPassword(hashpass);
			}
			
			uBl.actualizar(usuarioActualizar);
			
		} catch (UsuarioException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
	}
	


	public String getApellidos() {
		return apellidos;
	}

	public String getEmail() {
		return email;
	}


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

	public String getUsuarioLogin() {
		return usuarioLogin;
	}
		

		// Metodo q ue resetea el password del Usuario RRG 16/01/2008
//	public String resetPassword() {
//
//		logger.debug(" usuarioBean:resetPassword:"+this.getIdUsuario()+" - "+this.getUsuarioLogin());
//		Usuario usuarioLocal = new Usuario();
//
//		UsuarioBL uBl = new UsuarioBL();
//
//		usuarioLocal.setIdUsuario(getIdUsuario());
//		usuarioLocal.setApellidos(getApellidos());
//		usuarioLocal.setUsuarioLogin(getUsuarioLogin());
//		usuarioLocal.setNombres(getNombres());
//		usuarioLocal.setEmail(getEmail());
//
//		if (getIdDepto() == null) {
//
//			setIdDepto(0);
//		} else {
//			usuarioLocal.setIdDepto(getIdDepto());
//		}
//
//		if (getPassword() == null || getPassword() == "") {
//
//			String hashPassword = MD5Hash.encode("12345".getBytes());
//			usuarioLocal.setPassword(hashPassword);
//
//		}
//
//		try {
//			uBl.actualizar(usuarioLocal);
//		} catch (UsuarioException ex) {
//
//			logger
//					.info("Ocurrio Un Error al tratar de resetear el  password del Usuario: "
//							+ ex.getMessage());
//
//		}
//		// Para habilitar resetar los botones RRG 29/01/2008
//
//		this.muestraBotonBorra = false;
//		modificarValueBtn();
//		this.muestraPassword = true;
//		this.muestraBtnReset = false;
//		this.muestraNuevaAlta = false;
//		limpiaBean();
//
//		return ConstantesGP.SUCCESS_MIUSUARIO;
//
//	}
	
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	
	public void setIdDepto(Integer idDepto) {
		this.idDepto = idDepto;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public List<SelectItem> getListadoDeptos() {
		try {
			return UtilsVs.castStringPairToSelectitem( new DepartamentoBL()
					.obtenerDepartamentosNombreParaCombo("Seleccione...", "BLANCO") );
		} catch (DepartamentoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listadoDeptos;
	}


	public void setListadoDeptos(List<SelectItem> listadoDeptos) {
		this.listadoDeptos = listadoDeptos;
	}

	public void setMsgUsuarios(String msgUsuarios) {
		this.msgUsuarios = msgUsuarios;
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

	public void setUsuarioLogin(String usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
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
	}

	public void setDisableBotonBorrar(boolean disableBotonBorrar) {
		this.disableBotonBorrar = disableBotonBorrar;
	}


	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}


	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

}
