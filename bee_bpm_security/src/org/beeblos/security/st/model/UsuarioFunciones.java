package org.beeblos.security.st.model;

import static org.beeblos.security.st.util.Constantes.USE_SECURITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsuarioFunciones implements Serializable {
	
	private static final long serialVersionUID = -6876422218230354505L;

	private List<Integer> listaFunciones= new ArrayList<Integer>();
		
	
	public UsuarioFunciones() {
		
	}
	
		
	public boolean tieneFuncion(Integer funcion){
		
		if(USE_SECURITY){	
		
			for(Integer permiso : this.listaFunciones){
				if(funcion.equals(permiso)){
					return true;
				}
			}
			return false;
			
		//Si no se requiere seguridad se devuelve siempre true	
		} else {
			return true;
		}
		
	}
	
	public boolean listaVacia(){
 		
		return (this.listaFunciones.isEmpty() || this.listaFunciones.size()==0);
		
	}
	

	public List<Integer> getListaFunciones() {
		return listaFunciones;
	}


	public void setListaFunciones(List<Integer> listaFunciones) {
		this.listaFunciones = listaFunciones;
	}

	
	public String printListaFunciones() {
		
		String lista= "";
		
		for(Integer idFunc: listaFunciones){
			lista += lista + idFunc.toString();
		}
		
		return lista;
	}
	
	
	
	

}
