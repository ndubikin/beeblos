package org.beeblos.security.st.dao;

import org.beeblos.security.st.error.UsuarioException;
import org.beeblos.security.st.model.UsuarioPerfil;
import org.beeblos.security.st.util.UsuarioRol;

public class UsuarioPerfilDao {

	public UsuarioPerfilDao() {

	}

	public UsuarioPerfil consultaPerfilUsuario(Integer idPerfil)
			throws UsuarioException {

		UsuarioPerfil up = new UsuarioPerfil();
		
		
		//Simulando que se uso un perfil Normal 
		if (idPerfil.equals(1)) {

			up.setNombrePerfil( UsuarioRol.USUARIO.getNombre() );
			

		}
		

		return up;

	}

}
