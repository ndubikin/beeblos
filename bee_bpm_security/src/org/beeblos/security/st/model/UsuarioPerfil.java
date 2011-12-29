package org.beeblos.security.st.model;

import java.io.Serializable;

public class UsuarioPerfil implements Serializable {

	/**
	 * Esta calse se usara para perfilar el usuario
	 */
	private static final long serialVersionUID = -7171775844967201783L;
	private Integer idPais;
	private Integer idDepto;
	private Integer idPerfil;
	private Integer idUsuario;
	private String nombrePais;
	private String nombreDepto;
	private String nombrePerfil;
	private String nombreUsuario;
	private String usuarioLogin;

	public UsuarioPerfil() {

	}

	public Integer getIdPais() {
		return idPais;
	}

	public Integer getIdDepto() {
		return idDepto;
	}

	public Integer getIdPerfil() {
		return idPerfil;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public String getNombrePais() {
		return nombrePais;
	}

	public String getNombreDepto() {
		return nombreDepto;
	}

	public String getNombrePerfil() {
		return nombrePerfil;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public String getUsuarioLogin() {
		return usuarioLogin;
	}

	public void setIdPais(Integer idPais) {
		this.idPais = idPais;
	}

	public void setIdDepto(Integer idDepto) {
		this.idDepto = idDepto;
	}

	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public void setNombrePais(String nombrePais) {
		this.nombrePais = nombrePais;
	}

	public void setNombreDepto(String nombreDepto) {
		this.nombreDepto = nombreDepto;
	}

	public void setNombrePerfil(String nombrePerfil) {
		this.nombrePerfil = nombrePerfil;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public void setUsuarioLogin(String usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}

}
