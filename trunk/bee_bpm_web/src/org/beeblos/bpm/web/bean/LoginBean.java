package org.beeblos.bpm.web.bean;

import javax.faces.application.FacesMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.security.MD5Hash;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.web.security.UsuarioRol;
import org.beeblos.security.st.bl.UsuarioBL;
import org.beeblos.security.st.error.UsuarioException;
import org.beeblos.security.st.model.Usuario;

public class LoginBean extends CoreManagedBean {

	private static final Log logger = LogFactory.getLog(LoginBean.class);

	private static final long serialVersionUID = -6751199459089704171L;

	private String password;

	private String usuarioLogin;

	private void construirContextoSeguridad(Usuario usuario) {

		ContextoSeguridad contextoSeguridad = new ContextoSeguridad(usuario);

		Usuario u = new Usuario();
		
		if(usuario.getAdmon().equals("S")){
			
			u.setAdmon(UsuarioRol.ADMINISTRADOR.getNombre());
			contextoSeguridad.cambiaPerfilDeUsuario(u.getAdmon());
			
		}
		else {
			
			u.setAdmon(UsuarioRol.USUARIO.getNombre());
			contextoSeguridad.cambiaPerfilDeUsuario(u.getAdmon());
			
		}
		
		getSession().setAttribute(SECURITY_CONTEXT, contextoSeguridad);

	}

	public String getPassword() {
		return password;
	}

	public String getUsuarioLogin() {
		return usuarioLogin;
	}

	public String login() {

		String retorno = "FAIL";
		String hashPassword = MD5Hash.encode(password.getBytes());
		Usuario usuario = new Usuario();
		usuario.setUsuarioLogin(usuarioLogin);
		usuario.setPassword(hashPassword.trim());

		UsuarioBL uBl = new UsuarioBL();

		try {

			usuario = uBl.autenticarUsuario(usuario);

			if (usuario != null) {

				construirContextoSeguridad(usuario);
				retorno = "LOGIN";

			} else {
				
				agregarMensaje(FacesMessage.SEVERITY_ERROR,
						"errorAutenticacion",null);
			}

		} catch (UsuarioException ex) {
			
			agregarMensaje(FacesMessage.SEVERITY_ERROR,
					"errorAutenticacion",null);
			
			logger.info("Error al Autenticar un Usuario: "+ex.getMessage());
		}

		return retorno;
	}

	public String logout() {
		getSession().setAttribute(SECURITY_CONTEXT, null);
		return "LOGOUT";
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsuarioLogin(String usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}

}
