package org.beeblos.bpm.wc.taglib.security;

import java.io.Serializable;

import org.beeblos.bpm.core.util.HibernateSession;
import org.beeblos.security.st.model.Usuario;
import org.beeblos.security.st.model.UsuarioFunciones;

public class ContextoSeguridad implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5325246901614750181L;
	// Modo indicador de autorizaci�n para m�ltiples roles
	public final static int AUTH_MODE_ANY = 1;
	// Modo indicador de autorizaci�n por defecto
	public final static int AUTH_MODE_NONE = -1;
	// Modo indicador de autorizaci�n excluyendo un rol
	public final static int AUTH_MODE_NOT = 2;
	// Modo indicador de autorizaci�n para un rol simple
	public final static int AUTH_MODE_SINGLE = 0;
	public final static int AUTH_MODE_SU = 3; // Se agrego para el manejo de
												// Solo Lectura RRG 01/10/2008
	// Modo indicador de autorizaci�n
	private int authMode = AUTH_MODE_NONE;
	// Datos del usuario autenticado
	private Usuario usuario = new Usuario();
	// Datos del usuario autenticado respecto al proyecto seleccionado

	private boolean permiteCualquiera;
	private boolean soloAdmin;

	// mrico 20110624
	private UsuarioFunciones usuarioFunciones;
	
	// mrico 20110809
	private Integer idDepartamento;
	
	// rrl 20110902 La pagina de inicio que mostrar cuando el usuario se conecta  
	private String usuarioPaginaInicio;
	
	// rrl 20111108 Titulo principal que mostrar en pantalla cuando el usuario se conecta  
	private String tituloPrincipal;
	private String departamentoAbreviatura;
	
	// dml 20120131
	private HibernateSession hibernateParameters;

	
	public ContextoSeguridad() {

	}

	public ContextoSeguridad(Usuario usuario) {
		this.usuario = usuario;
	}

	public void cambiaPerfilDeUsuario(String usuarioPerfil) {
		this.usuario.setAdmon(usuarioPerfil);
	}

	/* **************************************************************** */

	// parche por el tema del pasaje de parametros en EL para JSF 1.2 - 2.1

	/* **************************************************************** */

	public int getAuthMode() {
		return authMode;
	}

	public boolean isPermiteCualquiera() {
		return permiteCualquiera;
	}

	public void setPermiteCualquiera(boolean permiteCualquiera) {
		this.permiteCualquiera = permiteCualquiera;
	}

	public boolean isSoloAdmin() {
		return soloAdmin;
	}

	public void setSoloAdmin(boolean soloAdmin) {
		this.soloAdmin = soloAdmin;
	}

	public String getAdmon() {
		return usuario.getAdmon();
	}

	public String getSuLectura() {
		return usuario.getSuLectura();
	}

	public Integer getIdDepto() {
		return usuario.getIdDepto();
	}

	public Integer getIdUsuario() {
		return usuario.getIdUsuario();
	}

	private String[] getPerfilesFromProperty(String property) {
		String[] perfiles = property.split(",");
		for (int i = 0; i < perfiles.length; i++) {
			perfiles[i] = perfiles[i].trim();
		}
		return perfiles;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public String getUsuarioLogin() {
		return usuario.getUsuarioLogin();
	}

	public boolean inAuthMode() {
		return authMode != AUTH_MODE_NONE;
	}

	public boolean noPermitido(String perfilesParam) {
		String[] perfiles = getPerfilesFromProperty(perfilesParam);
		boolean isAuthorized = false;
		for (int i = 0; i < perfiles.length; i++) {
			String perfil = perfiles[i];
			if (permitido(perfil)) {
				isAuthorized = false;
				break;
			} else {
				isAuthorized = true;
			}
		}
		return isAuthorized;
	}

	public boolean permiteCualquiera( String perfilesParam ) {
		
		// modalidad antigua de control de permisos, la dejo por si tenemos algo
		// mapeado con las propiedades "Admon" y alguna otra del "usuario"
		String[] perfiles = getPerfilesFromProperty(perfilesParam);
		boolean isAuthorized = false;
		for (int i = 0; i < perfiles.length; i++) {
			String perfil = perfiles[i];
			if (permitido(perfil)) {
				isAuthorized = true;
				break;
			}
		}
		// nes 20110711 - controla si tiene permisos para ver esta opción
		for (int i = 0; i < perfiles.length; i++) {
			String perfil = perfiles[i];
			if ( usuarioFunciones.tieneFuncion( new Integer(perfil) ) ) {
				isAuthorized = true;
				break;
			}
		}
		
		
		return isAuthorized;
	}

	public boolean permitido(String perfil) {
		final String perfilNombre = usuario.getAdmon();
		if (perfilNombre != null && perfilNombre.equals(perfil)) {
			return true;
		}
		return false;
	}

	/**
	 * RRG 01/10/2008 Habilita o no los controles si el usuario tiene Solo
	 * lectura = N
	 * 
	 * @param suLectura
	 * @return
	 */
	public boolean habilitaSuLectura(String suLectura) {
		final String suLecturaL = usuario.getSuLectura();
		if (suLecturaL != null && suLecturaL.equals(suLectura)) {
			return true;
		}
		return false;
	}

	public void setAuthMode(Integer authMode) {
		this.authMode = authMode;
	}

	public void setUsuarioFunciones(UsuarioFunciones usuarioFunciones) {
		this.usuarioFunciones = usuarioFunciones;
	}

	public UsuarioFunciones getUsuarioFunciones() {
		return usuarioFunciones;
	}

	// mrico 20110809
	// nes 20110824 - hay usuarios administradores q no tienen id departamento por lo q en esos casos
	// devuelvo false ( porque no corresponde al idDepartamento q van a pasar ... ( eso si no podrian pasar
	// null en este método
	public boolean isDepartamentoUsuario(Integer idDepartamento){
		if ( this.idDepartamento!=null ) {
			return this.idDepartamento.equals(idDepartamento);
		} else {
			return false;
		}
	}
	
	// mrico 20110809
	public void setIdDepartamento(Integer idDepartamento) {
		this.idDepartamento = idDepartamento;
	}

	public Integer getIdDepartamento() {
		return idDepartamento;
	}

	// rrl 20110902
	public String getUsuarioPaginaInicio() {
		return usuarioPaginaInicio;
	}

	public void setUsuarioPaginaInicio(String usuarioPaginaInicio) {
		this.usuarioPaginaInicio = usuarioPaginaInicio;
	}
	
	// rrl 20111108
	public String getTituloPrincipal() {
		return tituloPrincipal;
	}

	public void setTituloPrincipal(String tituloPrincipal) {
		this.tituloPrincipal = tituloPrincipal;
	}
	
	// rrl 20111108
	public String getTituloPrincipalDepartamento() {
		String result = null;
		if (departamentoAbreviatura!=null && !"".equals(departamentoAbreviatura)) {
			result = tituloPrincipal.toUpperCase() + " - " +  departamentoAbreviatura.toUpperCase();
		} else {
			result = tituloPrincipal.toUpperCase();
		}
		return result;
	}
	
	// rrl 20111108
	public String getDepartamentoAbreviatura() {
		return departamentoAbreviatura;
	}

	public void setDepartamentoAbreviatura(String departamentoAbreviatura) {
		this.departamentoAbreviatura = departamentoAbreviatura;
	}

	public HibernateSession getHibernateParameters() {
		return hibernateParameters;
	}

	public void setHibernateParameters(HibernateSession hibernateParameters) {
		this.hibernateParameters = hibernateParameters;
	}
	

}
