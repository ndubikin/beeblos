/**
 * 
 */
package org.beeblos.bpm.web.bean;

import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;

import com.sp.common.util.HibernateUtil;


/**
 * @author nestor
 *
 */
public class LoggedUserBean extends CoreManagedBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String usuarioLogueado;
	
	

	public LoggedUserBean() {
		super();
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);
		if (cs!=null) 
			usuarioLogueado=cs.getUsuario().getNombres()+" "+cs.getUsuario().getApellidos();
		else 
			usuarioLogueado="";
	}

	public LoggedUserBean(String usuarioLogueado) {
		super();
		this.usuarioLogueado = usuarioLogueado;
	}

	public  String getUsuarioLogueado() {
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);
		if (cs!=null) 
			this.usuarioLogueado=cs.getUsuario().getNombres()+" "+cs.getUsuario().getApellidos();
		else 
			this.usuarioLogueado="";
		return usuarioLogueado;
	}

	public void setUsuarioLogueado(String usuarioLogueado) {
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);
		if (cs!=null) 
			this.usuarioLogueado=cs.getUsuario().getNombres()+" "+cs.getUsuario().getApellidos();
		else 
			this.usuarioLogueado="";
	}

	public String getUrl() {
		return HibernateUtil.getUrl();
		
	}
	
}
