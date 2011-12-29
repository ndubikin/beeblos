package org.beeblos.bpm.wc.taglib.bean;


import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.security.st.bl.DepartamentoBL;
import org.beeblos.security.st.error.DepartamentoException;
import org.beeblos.security.st.model.Departamento;

public class DepartamentoBean extends CoreManagedBean {


	private static final Log logger = LogFactory.getLog(DepartamentoBean.class);

	private static final long serialVersionUID = 4031555875133328797L;

	private Departamento departamento;
	@SuppressWarnings("unused")
	private List<Departamento> departamentos;
	@SuppressWarnings("unused")
	private List<SelectItem> listadoDepartamentos;
	private String mensajes;
	
	private String currentSession;

	public DepartamentoBean() {

		_reset();
	}
	
	
	private void _reset() {
		this.departamento = new Departamento();
		
		// recarga la lista para reflejar la actualización realizada ...
		this.departamentos=null;
		this.getDepartamentos();
	
	}

	public void actualizar(){

		try {
			new DepartamentoBL()
			.actualizar(departamento);
		
		} catch (DepartamentoException e) {
			
			logger.error("Ocurrio Un Error al tratar de Actualizar el Departamento: [ id = " 
					+ this.departamento.getIdDepartamento() + " ; Nombre: "
					+ this.departamento.getDepartamentoNombre()
					+"] "	+ e.getMessage() + " : " + e.getCause());
			
			String params[] = {this.departamento.getIdDepartamento() + ",", "Error al tratar de Actualizar el Departamento: [ id = " 
					+ this.departamento.getIdDepartamento() + " ; Nombre: "
					+ this.departamento.getDepartamentoNombre()
					+"] "	+ e.getMessage() + " : " + e.getCause()};
			agregarMensaje("48",e.getMessage(),params,FGPException.ERROR);	

		}

	}

	public String agrega() {

//		String retorno = "FAIL";   //rrl 20110614 DEPENDENCIA NO NECESARIA

		DepartamentoBL aBl = new DepartamentoBL();

//		Departamento departamentoLocal = new Departamento();

		//String nombreDepartamentoDigitada = departamento.getDepartamentoNombre();
		//Integer idDepartamentoCapturada = departamento.getIdDepartamento();

		//departamentoLocal.setDepartamentoNombre(nombreDepartamentoDigitada);
		//departamentoLocal.setIdDepartamento(idDepartamentoCapturada);

		//Departamento departamentoEnSistema = null;
//
//		try {
//			departamentoEnSistema = aBl.obtenerDepartamentoPorNombre(nombreDepartamentoDigitada);
//		} catch (DepartamentoException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		if (this.departamento !=null && this.departamento.getIdDepartamento() == 0) {
			
			try {

				aBl.agregar(this.departamento);
			
			} catch (DepartamentoException ex1) {

				logger.error("Ocurrio Un Error al tratar de Agregar el Departamento: Nombre: "
						+ this.departamento.getDepartamentoNombre()
						+"] "	+ ex1.getMessage() + " : " + ex1.getCause());
				
				String params[] = {this.departamento.getIdDepartamento() + ",", "Error al tratar de Agregar el Departamento: [ Nombre: "
						+ this.departamento.getDepartamentoNombre()
						+"] "	+ ex1.getMessage() + " : " + ex1.getCause()};
				agregarMensaje("48",ex1.getMessage(),params,FGPException.ERROR);	
					
			} 

//			retorno = "SUCCESS_DEPARTAMENTO";    //rrl 20110614 DEPENDENCIA NO NECESARIA

			
		} else if (this.departamento !=null && this.departamento.getIdDepartamento() != 0){

			actualizar();
				setDepartamento(null);
//				retorno = "SUCCESS_DEPARTAMENTO";    //rrl 20110614 DEPENDENCIA NO NECESARIA

		} else {

				logger.error("Ocurrio un Error al tratar de confirmar la presencia o no  del Departamento: [ id = " 
					+ this.departamento.getIdDepartamento() + " ; Nombre: "
						+ this.departamento.getDepartamentoNombre()
						+"] ");
				
				String params[] = {this.departamento.getIdDepartamento() + ",", "Error al intentar actualizar el Departamento "+ departamento.toString() +" \n" };				
				agregarMensaje("48","",params,FGPException.ERROR);	

		}

		_reset()	;
	
//	return retorno;    //rrl 20110614 DEPENDENCIA NO NECESARIA
	return null;

	}

	public String borra() {

//		String retorno = "FAIL";     //rrl 20110614 DEPENDENCIA NO NECESARIA

		DepartamentoBL aBl = new DepartamentoBL();

		if(this.departamento !=null && this.departamento.getIdDepartamento() != 0){
				
			try {
				aBl.borra(departamento);
				
//				retorno = "SUCCESS_DEPARTAMENTO";    //rrl 20110614 DEPENDENCIA NO NECESARIA
				
			} catch (DepartamentoException e) {
	
				logger.info("Ocurrio Un Error al borrar  un Departamento: "
						+ e.getMessage());
	
			}
		}
		
		_reset();
		
//		return retorno;    //rrl 20110614 DEPENDENCIA NO NECESARIA
		return null;

	}

	public Departamento getDepartamento() {
		if (departamento == null) {
			departamento = new Departamento();
		}
		return departamento;
	}

	public String getDepartamentoNombre() {
		return getDepartamento().getDepartamentoNombre();
	}


	public Integer getIdDepartamento() {
		return getDepartamento().getIdDepartamento();
	}
	
	public List<Departamento> getDepartamentos() {

		if(departamentos == null){

			DepartamentoBL dBl = new DepartamentoBL();

			try {

				this.departamentos = dBl.obtenerDepartamentos();

			} catch (DepartamentoException ex) {

				logger.info("Ocurrio Un Error al tratar de obtener los Departamentos: "
						+ ex.getMessage());

			}

		}

		return departamentos;
	}
	

	@SuppressWarnings("unchecked")
	public List<SelectItem> getListadoDepartamentos() {

		List<SelectItem> retorno = new ArrayList<SelectItem>();
		DepartamentoBL aBl = new DepartamentoBL();
		List<Departamento> listadodepartamentos = null;

		try {

			listadodepartamentos = aBl.obtenerDepartamentos();
			for (Departamento a : listadodepartamentos)
				retorno.add(new SelectItem(a.getIdDepartamento(), a.getDepartamentoNombre()));

		} catch (DepartamentoException e) {

			logger
					.info("Ocurrio Un Error al tratar de OIbtener el Listado de Departamentos para el Crear el Combo: "
							+ e.getMessage());
		}

		return retorno;
	}

	public String getMensajes() {
		return mensajes;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public void setDepartamentoNombre(String departamentoNombre) {
		getDepartamento().setDepartamentoNombre(departamentoNombre);
	}

	public void setDepartamentos(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}

	public void setIdDepartamento(Integer idDepartamento) {
		getDepartamento().setIdDepartamento(idDepartamento);
	}

	public void setMensajes(String mensajes) {
		this.mensajes = mensajes;
	}
	
	public HttpSession getSession() {
		return (HttpSession) getContext().getExternalContext()
				.getSession(false);
	}

	public String getCurrentSession() {
	
		HttpSession session = getSession();		
		currentSession = session.getId();
		
		return currentSession;
	}

	public void setCurrentSession(String currentSession) {
		this.currentSession = currentSession;
	}
	

}
