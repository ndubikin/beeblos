package org.beeblos.bpm.wc.taglib.bean;

import static org.beeblos.bpm.wc.taglib.util.Constantes.NOM_APLICACION;
import static org.beeblos.bpm.wc.taglib.util.Constantes.TITULO_PRINCIPAL_DEFAULT;
import static org.beeblos.bpm.wc.taglib.util.Constantes.USE_SECURITY;
import static org.beeblos.bpm.wc.taglib.util.Constantes.USUARIO_PAGINA_INICIO_DEFAULT;
import static org.beeblos.bpm.wc.taglib.util.Constantes.WELCOME_PAGE;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.util.Configuration;
import org.beeblos.bpm.core.util.HibernateSession;
import org.beeblos.bpm.core.util.HibernateSessionsUtil;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.security.MD5Hash;
import org.beeblos.bpm.wc.taglib.security.UsuarioRol;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.security.st.bl.DepartamentoBL;
import org.beeblos.security.st.bl.UsuarioBL;
import org.beeblos.security.st.bl.UsuarioFuncionesBL;
import org.beeblos.security.st.bl.UsuarioLoginBL;
import org.beeblos.security.st.bl.UsuarioPerfilesBL;
import org.beeblos.security.st.error.DepartamentoException;
import org.beeblos.security.st.error.UsuarioException;
import org.beeblos.security.st.error.UsuarioFuncionesException;
import org.beeblos.security.st.error.UsuarioLoginException;
import org.beeblos.security.st.error.UsuarioPerfilesException;
import org.beeblos.security.st.model.Departamento;
import org.beeblos.security.st.model.Usuario;
import org.beeblos.security.st.model.UsuarioFunciones;
import org.beeblos.security.st.model.UsuarioLogin;
import org.beeblos.security.st.model.UsuarioPerfiles;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;


public class Login_x_Bean extends CoreManagedBean {

//	private static  LogFactory lf = LogFactory.getFactory();
	
	private static final Log logger = LogFactory.getLog(Login_x_Bean.class);


	private static final long serialVersionUID = -6751199459089704171L;

	private String password = "";

	private String usuarioLogin = "";
	private boolean logged;
	
	// dml 20120131
	private HibernateSession hibernateSessionParameters;

	private void construirContextoSeguridad(Usuario usuario) {

		ContextoSeguridad contextoSeguridad = new ContextoSeguridad(usuario);

		Usuario u = new Usuario();

		if(usuario.getAdmon().equals("S")){

			u.setAdmon(UsuarioRol.ADMINISTRADOR.getNombre());
			contextoSeguridad.cambiaPerfilDeUsuario(u.getAdmon());
			
			// ARREGLAR - PARCHE POR TEMA DEVOLUCION PARAMETROS EN JSF 1.2 FACELETS
			contextoSeguridad.setPermiteCualquiera(true);
			contextoSeguridad.setSoloAdmin(true);
			
		} else {

			u.setAdmon(UsuarioRol.USUARIO.getNombre());
			contextoSeguridad.cambiaPerfilDeUsuario(u.getAdmon());
			
			// ARREGLAR - PARCHE POR TEMA DEVOLUCION PARAMETROS EN JSF 1.2 FACELETS
			contextoSeguridad.setPermiteCualquiera(true);
			contextoSeguridad.setSoloAdmin(false);
			
		}

		
		// mrico 20110622 - Control de funciones del usuario 
		if(USE_SECURITY){		
			 try {
				 				UsuarioFunciones listaFunciones = 
					new UsuarioFuncionesBL()
						.obtenerFuncionesGeneral(usuario.getIdUsuario());
				
				contextoSeguridad.setUsuarioFunciones(listaFunciones);
				
	//			System.out.println("Usuario: "+usuario.getIdUsuario()+" ; Funciones: " + listaFunciones.printListaFunciones());
				
			} catch (UsuarioFuncionesException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}				
		
		// aunque no se use seguridad,
		} else {
			UsuarioFunciones listaFunciones = new UsuarioFunciones();
			contextoSeguridad.setUsuarioFunciones(listaFunciones);
		}

		// mrico 20110809
		contextoSeguridad.setIdDepartamento(usuario.getIdDepto());
		
		// dml 20120131
		contextoSeguridad.setHibernateParameters(HibernateSession.loadDefaultHibernateSessionParameters());
		
		getSession().setAttribute(SECURITY_CONTEXT, contextoSeguridad);
				
		
		// nes 20110903 - setea la página de inicio del usuario dependiendo de su departamento
		contextoSeguridad.setUsuarioPaginaInicio( 
				setPaginaInicio(
						usuario.getIdDepto(), usuario.getIdUsuario() ));
		
		logger.info("---------_> PAGINA INICIO: "+contextoSeguridad.getUsuarioPaginaInicio()+"] <<<<<-------------------------");
		
		// rrl 20111108 - setea el titulo principal del departamento del usuario
		estableceTituloPrincipalDepartamento(usuario.getIdDepto(), contextoSeguridad);
	}


	public String setPaginaInicio(Integer idDepartamento, Integer idUsuario ) {
		// rrl 20110902
		// Segun el departamento al que pertenece el usuario se carga dinamicamente el welcome.xhtml
		
		String urlPaginaInicioDpto = USUARIO_PAGINA_INICIO_DEFAULT;
		
		if (idDepartamento!=null && !idDepartamento.equals(0)) {
			
			try {

				urlPaginaInicioDpto = Configuration
										.getConfigurationResourceBundle()
											.getString("pagina.inicio.dpto."+idDepartamento	);

					

			} catch (Exception e) {
				String mensaje = "Ocurrio Un Error al tratar de obtener [pagina.inicio.dpto."+idDepartamento+
								"] para idUsuario:[" + idUsuario + 
								"] "	+ e.getMessage() + " : " + e.getCause();
				logger.error( mensaje );
				logger.error( "se utilizará la página de inicio por defecto !!!");
				e.printStackTrace();
			}
			
		} 
		
		return urlPaginaInicioDpto;
	}
	
	//rrl 20111108
	private void estableceTituloPrincipalDepartamento(Integer idDepartamento, ContextoSeguridad contextoSeguridad) {

		contextoSeguridad.setTituloPrincipal( TITULO_PRINCIPAL_DEFAULT );
		contextoSeguridad.setDepartamentoAbreviatura("");
		
		if (idDepartamento!=null && !idDepartamento.equals(0)) {

			try {
				Departamento departamento = new DepartamentoBL().obtenerDepartamentoPorPK(idDepartamento);
				if (departamento!=null && 
						departamento.getDepartamentoTitulo()!=null && 
						!"".equals(departamento.getDepartamentoTitulo())) {
					
					int endIndex = departamento.getDepartamentoAbreviatura().indexOf(".");
					
					contextoSeguridad.setTituloPrincipal( departamento.getDepartamentoTitulo() );
					contextoSeguridad.setDepartamentoAbreviatura(departamento.getDepartamentoAbreviatura().substring(0, endIndex));
				}
				
			} catch (DepartamentoException e) {
				String mensaje = "Ocurrio Un Error al tratar de obtener el titulo príncipal [DepartamentoBL().obtenerDepartamentoPorPK("+idDepartamento+
								")] "	+ e.getMessage() + " : " + e.getCause();
				logger.error( mensaje );
				logger.error( "se utilizará el título principal inicio por defecto !!!");
				e.printStackTrace();
				e.printStackTrace();
			}
		}
	}
	
	
	// martin - 20101222
	// Completa el UsuarioLogin y lo almacena - Caso EXITO
	private void guardarLoginExito(Integer idUsuario){
		
		UsuarioLogin ul = new UsuarioLogin();
		
		ul.setIdUsuario(idUsuario);
		ul.setFecha(new Date());
		
		//nes - 20111018
		ul.setLoginOrigen(NOM_APLICACION);	

		
				
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();  
		String ip = httpServletRequest.getRemoteAddr(); 
		ul.setLoginIp(ip);
				
		//logger.info("Usuario Remoto: "+httpServletRequest.getRemoteUser());
		//logger.info("User Principal: "+httpServletRequest.getUserPrincipal());
		
		
		// mrico 20110622
		// guarda los perfiles del usuario en el login 
		if( USE_SECURITY ){	
		
			 try {
						
				UsuarioPerfiles listaPerfiles = new UsuarioPerfilesBL().obtenerPerfiles(idUsuario, null); // mando null porq no llevamos control a nivel de app
				ul.setPerfiles(listaPerfiles.toString());
				
			} catch (UsuarioPerfilesException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
			
		ul.setLoginExitoso(true);
		
		try {
					
			new UsuarioLoginBL().agregar(ul);
			
		} catch (UsuarioLoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void guardarLoginFracaso(String loginIntento){
		
		UsuarioLogin ul = new UsuarioLogin();
		
		ul.setIdUsuario(0); 		// En este caso no hay usuario
		ul.setFecha(new Date());
				
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();  
		String ip = httpServletRequest.getRemoteAddr(); 
		ul.setLoginIp(ip);
		
		//logger.info("Usuario Remoto: "+httpServletRequest.getRemoteUser()); //Sale Null ????
		ul.setLoginIntento(loginIntento);
		
		ul.setLoginExitoso(false);
			
		//nes 20111018
		ul.setLoginOrigen(NOM_APLICACION);
		
		try {
		
			new UsuarioLoginBL().agregar(ul);
			
		} catch (UsuarioLoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getPassword() {
		return password;
	}

	public String getUsuarioLogin() {
		return usuarioLogin;
	}

	
	public String login() {

		setShowHeaderMessage(false); // nes 20101230
		String retorno = "FAIL";
		String hashPassword = MD5Hash.encode(password.getBytes());
		
		Usuario usuarioActual = new Usuario();
		usuarioActual.setUsuarioLogin(this.usuarioLogin);
		usuarioActual.setPassword(hashPassword.trim());

		UsuarioBL uBl = new UsuarioBL();
				
		try {
			
			Usuario usuario = uBl.autenticarUsuario(usuarioActual);

			if (usuario != null) {

					construirContextoSeguridad(usuario);
					
					// mrico 20110706
					// Si se usa seguridad, debe tener algun permiso
					if( USE_SECURITY ){
						
						ContextoSeguridad contextoSeguridad = (ContextoSeguridad) getSession().getAttribute(SECURITY_CONTEXT);
						
						if(!contextoSeguridad.getUsuarioFunciones().listaVacia()){
							
							guardarLoginExito(usuario.getIdUsuario());
							logger.info("Login_x_bean: construirContextoSeguridad: login:"+usuario.getIdUsuario()+" "+usuario.getApellidos()+" "+usuario.getNombres());
							this.logged=true;
							retorno = "LOGIN";
		
						} else {
							logger.error("ERROR LOGIN: intento de acceso de usuario sin permisos:"+this.usuarioLogin); // nes 20110902
							guardarLoginFracaso(this.usuarioLogin);
							String mensaje = "El usuario no tiene funciones asignadas en el sistema";
							String params[] = { "", mensaje };
							agregarMensaje("3", "errorPermisos 1", params, FGPException.WARN);
						}
						
						
					} else {	
						guardarLoginExito(usuario.getIdUsuario());
						logger.info("Login_x_bean: construirContextoSeguridad: login:"+usuario.getIdUsuario()+" "+usuario.getApellidos()+" "+usuario.getNombres());
						this.logged=true;
						
						retorno = "LOGIN";
					}

			} else {
				
				logger.error("ERROR LOGIN: error autenticación:"+this.usuarioLogin); // nes 20110902
				
				guardarLoginFracaso(this.usuarioLogin);		
				
				agregarMensaje("1", "errorAutenticacion 1", null, FGPException.WARN);
			}

		} catch (UsuarioException ex) {
			
			logger.info("Login_x_bean: construirContextoSeguridad: ERROR LOGIN: UsuarioException :"+this.usuarioLogin+" - ["
					+ex.getMessage()+" - cause:"+ex.getCause()+"]"); // nes 20110902
			
			guardarLoginFracaso(this.usuarioLogin);
			
			agregarMensaje("1", "errorAutenticacion 2", null, FGPException.WARN);

		}


		return retorno;
		
	}
	
	public String logout() {
		logger.info("Login_x_bean: construirContextoSeguridad: Logoff:"+this.usuarioLogin);

		//HZC:20110322, esto remueve todas las variables de sesion 
		Enumeration e = getSession().getAttributeNames();
	    while (e.hasMoreElements()) {
	      String name = (String) e.nextElement();
	      getSession().removeAttribute(name);
	    }
	    getSession().invalidate();//HZC:20110408
		return "LOGOUT";
	}
	
	
	public String goToWelcome() {
		return WELCOME_PAGE;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsuarioLogin(String usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}

//	public String getUsuarioLogout() {
//		this.logged=false;
//		return logout();
//	}

	public boolean isLogged() {
		System.out.println(" logged:"+logged);
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}


	public HibernateSession getHibernateSessionParameters() {
		return hibernateSessionParameters;
	}


	public void setHibernateSessionParameters(HibernateSession hibernateSessionParameters) {
		this.hibernateSessionParameters = hibernateSessionParameters;
	}
	
	
	

}
