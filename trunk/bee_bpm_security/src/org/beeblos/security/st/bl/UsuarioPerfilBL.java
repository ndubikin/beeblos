package org.beeblos.security.st.bl;

import org.beeblos.security.st.dao.UsuarioPerfilDao;
import org.beeblos.security.st.error.UsuarioException;
import org.beeblos.security.st.model.UsuarioPerfil;

public class UsuarioPerfilBL {

	public UsuarioPerfilBL() {

	}

	public UsuarioPerfil consultaPerfilUsuario(Integer idPerfil)
			throws UsuarioException {

		UsuarioPerfilDao upDao = new UsuarioPerfilDao();

		return upDao.consultaPerfilUsuario(idPerfil);

	}

}
