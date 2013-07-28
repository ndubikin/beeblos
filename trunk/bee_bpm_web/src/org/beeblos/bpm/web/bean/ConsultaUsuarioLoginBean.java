/**
 * BackingBean para cargar la lista de propuestas
 * 
 * Notas:
 * Voy a ir dejando algunas notas y reflexiones aquí como para que queden de guía (sobre todo en situaciones 'raras'
 * 
 * rich:pickList -> me han dado algunos problemas extraños al tratar de manipular la lista que contiene los resultados
 * de la pickList (en este caso es la selectedSubareas. Si la intento manipular recorriéndola en el backing bean
 * o haciéndole un clear o similar, como que lanza excepciones raras en consola.
 * ==> mi sospecha es que al manejar la pickList el richfaces directamente, como que la tiene el pillada a nivel
 * de cliente (js o lo que fuera) y la sincronización y comunicación con el backing bean no es del todo limpia ...
 * 
 * 
 * 
 * 
 * 
 */
package org.beeblos.bpm.web.bean;

import static org.beeblos.bpm.core.util.Constants.CONSULTA_USUARIO_LOGIN;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.security.st.bl.UsuarioLoginBL;
import org.beeblos.security.st.model.UsuarioLoginAmpliado;



/**
 * @author nes
 * 
 */
public class ConsultaUsuarioLoginBean extends CoreManagedBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory
			.getLog(ConsultaUsuarioLoginBean.class);

	private String filtroUsuarioLogin;
	private String filtroApellidos;
	private Date filtroFechaInicial;
	private Date filtroFechaFinal;
	
	
	private Integer nResultados = 0;  // martin - 20101221

	private ArrayList<UsuarioLoginAmpliado> listaUsuarioLogin = new ArrayList<UsuarioLoginAmpliado>();

	/**
	 * 
	 */

	public ConsultaUsuarioLoginBean() {

		_init();

	}

	private void _init(){
		filtroUsuarioLogin="";
		
		System.out.println("ConsultaUsuarioLoginBean construktor");
			
		setnResultados(0);		
	}
	
	public void reset(){
		super.reset();
		
		filtroUsuarioLogin = null;
		
		listaUsuarioLogin = new ArrayList<UsuarioLoginAmpliado>();
		
				
//		documentoDescripcion = "";		
		
		setnResultados(0); 
		
	}


	public String buscarUsuarioLogin() {
		
		try {
			listaUsuarioLogin = (ArrayList<UsuarioLoginAmpliado>) 
			new UsuarioLoginBL()
				.finderUsuarioLogin(this.filtroUsuarioLogin,
						this.filtroApellidos,
						filtroFechaInicial, 
						filtroFechaFinal,
						"DAP");				
			
			setnResultados(listaUsuarioLogin.size()); // martin - 20101221
		
		} catch (Exception e) {
			logger.warn("error al intentar cargar la lista de convocatorias - "+e.getMessage());
		} 
		
		return CONSULTA_USUARIO_LOGIN; 
		
		
	}
		
	
	public String getFiltroUsuarioLogin() {
		return filtroUsuarioLogin;
	}

	public void setFiltroUsuarioLogin(String filtroUsuarioLogin) {
		this.filtroUsuarioLogin = filtroUsuarioLogin;
	}


	public ArrayList<UsuarioLoginAmpliado> getListaUsuarioLogin() {
		return listaUsuarioLogin;
	}


	public void setListaUsuarioLogin(ArrayList<UsuarioLoginAmpliado> listaUsuarioLogin) {
		this.listaUsuarioLogin = listaUsuarioLogin;
	}

	public void setnResultados(Integer nResultados) {
		this.nResultados = nResultados;
	}


	public Integer getnResultados() {
		return nResultados;
	}

	public void setFiltroFechaInicial(Date filtroFechaInicial) {
		this.filtroFechaInicial = filtroFechaInicial;
	}

	public Date getFiltroFechaInicial() {
		return filtroFechaInicial;
	}

	public void setFiltroFechaFinal(Date filtroFechaFinal) {
		this.filtroFechaFinal = filtroFechaFinal;
	}

	public Date getFiltroFechaFinal() {
		return filtroFechaFinal;
	}

	public void setFiltroApellidos(String filtroApellidos) {
		this.filtroApellidos = filtroApellidos;
	}

	public String getFiltroApellidos() {
		return filtroApellidos;
	}
	
}
