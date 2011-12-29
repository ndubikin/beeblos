package org.beeblos.security.st.model;

// Generado 22-dic-2010 10:55:25 



import java.util.Date;

/**
 * Domain model class UsuarioLogin.
 */

public class UsuarioLoginAmpliado implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private Integer idUsuario;
	private String usuarioLogin;
	private String usuarioNombre;
	private String usuarioApellidos;
	private boolean loginExitoso;
	private String loginIp;
	private String loginIntento;
	private String loginOrigen;
	private Date fecha;
	
	//mrico 20110620
	private String perfiles;
 
	public UsuarioLoginAmpliado() {

	}

	public UsuarioLoginAmpliado(
		 	Integer idUsuario
		 		,boolean loginExitoso
		 		,String loginIp
		 		,String loginIntento
		 		,Date fecha
		) {
		super();
		this.idUsuario = idUsuario;
		this.loginExitoso = loginExitoso;
		this.loginIp = loginIp;
		this.setLoginIntento(loginIntento);
		this.fecha = fecha;
	}

	public UsuarioLoginAmpliado(
		 	Integer idUsuario
		 	  , String usuarioLogin
		 	  , String usuarioNombre
		 	  , String usuarioApellidos
		 	  , boolean loginExitoso
		 	  ,String loginIp
		 	  ,Date fecha
		) {
		super();
		this.idUsuario = idUsuario;
		
		this.usuarioLogin= usuarioLogin;
		this.usuarioNombre = usuarioNombre;
		this.usuarioApellidos = usuarioApellidos;
		
		this.loginExitoso = loginExitoso;
		this.loginIp = loginIp;
		this.setLoginIntento(loginIntento);
		this.fecha = fecha;
	}

	
		
	public Integer getIdUsuario(){
		
		return idUsuario;
	
	}
		
	public void setIdUsuario (Integer idUsuario){
	
		this.idUsuario = idUsuario;
	}
		
	public boolean getLoginExitoso(){
		
		return loginExitoso;
	
	}
		
	public void setLoginExitoso (boolean loginExitoso){
	
		this.loginExitoso = loginExitoso;
	}
		
	public String getLoginIp(){
		
		return loginIp;
	
	}
		
	public void setLoginIp (String loginIp){
	
		this.loginIp = loginIp;
	}
	
	public void setLoginIntento(String loginIntento) {
		this.loginIntento = loginIntento;
	}

	public String getLoginIntento() {
		return loginIntento;
	}

		
	public Date getFecha(){
		
		return fecha;
	
	}
		
	public void setFecha (Date fecha){
	
		this.fecha = fecha;
	}
		
	
	
	public String toString() {
		
		String ret = "";
		 
		ret += " idUsuario:["+idUsuario+"] ";
		ret += " loginExitoso:["+loginExitoso+"] ";
		ret += " loginIp:["+loginIp+"] ";
		ret += " loginIntento:["+loginIntento+"] ";
		ret += " fecha:["+fecha+"] ";
		
		return ret;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioLogin other = (UsuarioLogin) obj;
		if (loginExitoso != other.getLoginExitoso()) 
			return false;
		if (loginIp == null) {
			if (other.getLoginIp() != null)
				return false;
		} else if (!loginIp.equals(other.getLoginIp()))
			return false;
		if (loginIntento == null) {
			if (other.getLoginIntento() != null)
				return false;
		} else if (!loginIntento.equals(other.getLoginIntento()))
			return false;
		
		if (fecha == null) {
			if (other.getFecha() != null)
				return false;
		} else if (!fecha.equals(other.getFecha()))
			return false;
		return true;
	}

	public void setLoginOrigen(String loginOrigen) {
		this.loginOrigen = loginOrigen;
	}

	public String getLoginOrigen() {
		return loginOrigen;
	}

	public void setPerfiles(String perfiles) {
		this.perfiles = perfiles;
	}

	public String getPerfiles() {
		return perfiles;
	}

	public void setUsuarioLogin(String usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}

	public String getUsuarioLogin() {
		return usuarioLogin;
	}

	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}

	public String getUsuarioNombre() {
		return usuarioNombre;
	}

	public void setUsuarioApellidos(String usuarioApellidos) {
		this.usuarioApellidos = usuarioApellidos;
	}

	public String getUsuarioApellidos() {
		return usuarioApellidos;
	}

	
}


