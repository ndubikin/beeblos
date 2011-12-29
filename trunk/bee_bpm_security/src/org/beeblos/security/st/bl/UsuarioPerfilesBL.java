package org.beeblos.security.st.bl;

import org.beeblos.security.st.dao.UsuarioPerfilesDao;
import org.beeblos.security.st.error.UsuarioPerfilesException;
import org.beeblos.security.st.model.UsuarioPerfiles;


public class UsuarioPerfilesBL {
	

	public UsuarioPerfiles obtenerPerfiles(Integer idUsuario, Integer idAplicacion) throws UsuarioPerfilesException {

		UsuarioPerfilesDao gPPersonaDao = new UsuarioPerfilesDao();
		return gPPersonaDao.obtenerPerfiles(idUsuario, idAplicacion);

	}
	
}
