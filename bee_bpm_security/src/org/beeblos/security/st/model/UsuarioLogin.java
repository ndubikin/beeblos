package org.beeblos.security.st.model;

// Generado 22-dic-2010 10:55:25 



import java.util.Date;

/**
 * Domain model class UsuarioLogin.
 */

public class UsuarioLogin implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id=0;
	private Integer idUsuario;
	private boolean loginExitoso;
	private String loginIp;
	private String loginIntento;
	private String loginOrigen;
	private Date fecha;
	
	//mrico 20110620
	private String perfiles;
 
	public UsuarioLogin() {

	}

	public UsuarioLogin(
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

	public UsuarioLogin(
		 	 Integer id
		 	  ,Integer idUsuario
		 	  ,boolean loginExitoso
		 	  ,String loginIp
		 	  ,String loginIntento
		 	  ,Date fecha
		) {
		super();
		this.id = id;
		this.idUsuario = idUsuario;
		this.loginExitoso = loginExitoso;
		this.loginIp = loginIp;
		this.setLoginIntento(loginIntento);
		this.fecha = fecha;
	}

	public Integer getId(){
		
		return id;
	
	}
		
	public void setId (Integer id){
	
		this.id = id;
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
		if (idUsuario == null) {
			if (other.idUsuario != null)
				return false;
		} else if (!idUsuario.equals(other.idUsuario))
			return false;
		if (loginExitoso != other.loginExitoso) 
			return false;
		if (loginIp == null) {
			if (other.loginIp != null)
				return false;
		} else if (!loginIp.equals(other.loginIp))
			return false;
		if (loginIntento == null) {
			if (other.loginIntento != null)
				return false;
		} else if (!loginIntento.equals(other.loginIntento))
			return false;
		
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
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

	
}


