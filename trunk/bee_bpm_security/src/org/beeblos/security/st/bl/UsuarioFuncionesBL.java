package org.beeblos.security.st.bl;

import org.beeblos.security.st.dao.UsuarioFuncionesDao;
import org.beeblos.security.st.error.UsuarioFuncionesException;
import org.beeblos.security.st.model.UsuarioFunciones;


public class UsuarioFuncionesBL {
	

	public UsuarioFunciones obtenerFunciones(Integer idUsuario, Integer idAplicacion) throws UsuarioFuncionesException {

		UsuarioFuncionesDao gPPersonaDao = new UsuarioFuncionesDao();
		return gPPersonaDao.obtenerFunciones(idUsuario, idAplicacion);

	}
	
	public UsuarioFunciones obtenerFuncionesGeneral(Integer idUsuario) throws UsuarioFuncionesException {

		UsuarioFuncionesDao gPPersonaDao = new UsuarioFuncionesDao();
		return gPPersonaDao.obtenerFuncionesGeneral(idUsuario);

	}
	
}
