package org.beeblos.security.st.model;

import java.io.Serializable;
import java.util.Date;

public class Usuario implements Serializable {

	private static final long serialVersionUID = -5528952936090045290L;
	
	private Integer idUsuario;
	 
	private String admon;
	private String apellidos;
	private String email;
	//private Integer idArea;
	private Integer idDepto;
	private String nombres;
	private String password;
	private String suLectura;
	private String usuarioLogin;
	
	private Date lastLoginDate;
	private String habilitado;
	private String jefeDepartamento;
	private Date fechaAlta;
	private String usuarioAlta;
	private Date fechaModificacion;
	private String usuarioModificacion;

	// rrl 20110510 Nombre de la clase a importar/exportar XML <--> OBJECT
	public String getClassName() {
		return getClass().getName();
	}	
	
	public Usuario() {
		
	}
	
	public Usuario(
		 	String nombres
		 		,String apellidos
		 		,String email
		 		,String usuarioLogin
		 		,String password
		 		,String admon
		 		,String suLectura
		 		,Integer idDepto
		) {
		super();
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.email = email;
		this.usuarioLogin = usuarioLogin;
		this.password = password;
		this.admon = admon;
		this.suLectura = suLectura;
		this.idDepto = idDepto;
	}
	
	public Usuario(
		 	 Integer idUsuario
		 	  ,String nombres
		 	  ,String apellidos
		 	  ,String email
		 	  ,String usuarioLogin
		 	  ,String password
		 	  ,String admon
		 	  ,String suLectura
		 	  ,Integer idDepto
		) {
		super();
		this.idUsuario = idUsuario;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.email = email;
		this.usuarioLogin = usuarioLogin;
		this.password = password;
		this.admon = admon;
		this.suLectura = suLectura;
		this.idDepto = idDepto;
	}
	
	
	public Usuario(String usuarioLogin, String password) {
		this.usuarioLogin = usuarioLogin;
		this.password = password;
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
	
	public Date getLastLoginDate(){
		
		return lastLoginDate;
	
	}
		
	public void setLastLoginDate (Date lastLoginDate){
	
		this.lastLoginDate = lastLoginDate;
	}
		
	public String getHabilitado(){
		
		return habilitado;
	
	}
		
	public void setHabilitado (String habilitado){
	
		this.habilitado = habilitado;
	}
		
	public String getJefeDepartamento(){
		
		return jefeDepartamento;
	
	}
		
	public void setJefeDepartamento (String jefeDepartamento){
	
		this.jefeDepartamento = jefeDepartamento;
	}
		
	public Date getFechaAlta(){
		
		return fechaAlta;
	
	}
		
	public void setFechaAlta (Date fechaAlta){
	
		this.fechaAlta = fechaAlta;
	}
		
	public String getUsuarioAlta(){
		
		return usuarioAlta;
	
	}
		
	public void setUsuarioAlta (String usuarioAlta){
	
		this.usuarioAlta = usuarioAlta;
	}
		
	public Date getFechaModificacion(){
		
		return fechaModificacion;
	
	}
		
	public void setFechaModificacion (Date fechaModificacion){
	
		this.fechaModificacion = fechaModificacion;
	}
		
	public String getUsuarioModificacion(){
		
		return usuarioModificacion;
	
	}
		
	public void setUsuarioModificacion (String usuarioModificacion){
	
		this.usuarioModificacion = usuarioModificacion;
	}
	
public String toString() {
		
		String ret = "";
		 
		ret += " nombres:["+nombres+"] ";
		ret += " apellidos:["+apellidos+"] ";
		ret += " email:["+email+"] ";
		ret += " usuarioLogin:["+usuarioLogin+"] ";
		ret += " password:["+password+"] ";
		ret += " admon:["+admon+"] ";
		ret += " suLectura:["+suLectura+"] ";
		ret += " idDepto:["+idDepto+"] ";
		ret += " lastLoginDate:["+lastLoginDate+"] ";
		ret += " habilitado:["+habilitado+"] ";
		ret += " jefeDepartamento:["+jefeDepartamento+"] ";
		
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
		Usuario other = (Usuario) obj;
		if (nombres == null) {
			if (other.nombres != null)
				return false;
		} else if (!nombres.equals(other.nombres))
			return false;
		if (apellidos == null) {
			if (other.apellidos != null)
				return false;
		} else if (!apellidos.equals(other.apellidos))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (usuarioLogin == null) {
			if (other.usuarioLogin != null)
				return false;
		} else if (!usuarioLogin.equals(other.usuarioLogin))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (admon == null) {
			if (other.admon != null)
				return false;
		} else if (!admon.equals(other.admon))
			return false;
		if (suLectura == null) {
			if (other.suLectura != null)
				return false;
		} else if (!suLectura.equals(other.suLectura))
			return false;
		if (idDepto == null) {
			if (other.idDepto != null)
				return false;
		} else if (!idDepto.equals(other.idDepto))
			return false;
		if (lastLoginDate == null) {
			if (other.lastLoginDate != null)
				return false;
		} else if (!lastLoginDate.equals(other.lastLoginDate))
			return false;
		if (habilitado == null) {
			if (other.habilitado != null)
				return false;
		} else if (!habilitado.equals(other.habilitado))
			return false;
		if (jefeDepartamento == null) {
			if (other.jefeDepartamento != null)
				return false;
		} else if (!jefeDepartamento.equals(other.jefeDepartamento))
			return false;
		return true;
	}
	
	

}
