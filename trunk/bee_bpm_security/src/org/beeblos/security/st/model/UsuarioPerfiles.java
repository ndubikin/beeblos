package org.beeblos.security.st.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsuarioPerfiles implements Serializable {
	
	private static final long serialVersionUID = -6876422218230354505L;

	private List<Integer> listaPerfiles= new ArrayList<Integer>();
		
	
	public UsuarioPerfiles() {
		
	}
	
		
	public boolean tienePerfil(String perfil){
		 		
		
		for(Integer permiso : this.listaPerfiles){
			if(perfil.equals(permiso)){
				return true;
			}
		}
		return false;
	}
	
	public String toString(){
		 		
		String lista = "";
		for(Integer perfil : this.listaPerfiles){
			if(perfil.equals(perfil)){
				lista += perfil.toString() + " - ";
			}
		}
		return lista ;
	}
	

	public List<Integer> getListaPerfiles() {
		return listaPerfiles;
	}


	public void setListaPerfiles(List<Integer> listaPerfiles) {
		this.listaPerfiles = listaPerfiles;
	}

	
	
	
	

}
