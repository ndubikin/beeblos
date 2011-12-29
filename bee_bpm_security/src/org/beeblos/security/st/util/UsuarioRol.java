package org.beeblos.security.st.util;


	
	public enum UsuarioRol {    
	    ADMINISTRADOR(1, "Admon"),
	    USUARIO(2, "User");    
	    
	    private Integer idRol;
	    private String nombre;
	    
	    private UsuarioRol(Integer idRol, String nombre) {
	        this.idRol = idRol;
	        this.nombre = nombre;
	    }
	    
	    public Integer getIdRol(){
	        return this.idRol;
	    }
	    
	    public String getNombre(){
	        return this.nombre;
	    }
	};


