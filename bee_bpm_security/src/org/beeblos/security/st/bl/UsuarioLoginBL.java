package org.beeblos.security.st.bl;

// Generado 16-dic-2010 9:15:04 



import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.security.st.dao.UsuarioLoginDao;
import org.beeblos.security.st.error.UsuarioLoginException;
import org.beeblos.security.st.model.UsuarioLogin;
import org.beeblos.security.st.model.UsuarioLoginAmpliado;

/**
 * Bussiness Logic object for domain model class UsuarioLogin.
 * @see org.beeblos.security.st.model.bdc.model.UsuarioLogin
 */

public class UsuarioLoginBL {

    private static final Log logger = LogFactory.getLog(UsuarioLoginBL.class);

	
 
	public UsuarioLoginBL() {

	}

	public Integer agregar(UsuarioLogin instancia) throws UsuarioLoginException  {
		
		logger.debug("UsuarioLoginBL:agregar()");
		
		_controlesConsistenciaDatosRecibidos(instancia);
		
		// Fecha
		Date date = new Date();
		instancia.setFecha(date);

		UsuarioLoginDao usuarioLoginDao = new UsuarioLoginDao();
		return usuarioLoginDao.agregar(instancia);
	
	}
	

	public void actualizar(UsuarioLogin instancia) throws UsuarioLoginException {
		
		logger.debug("UsuarioLoginBL:actualizar()");
		
		_controlesConsistenciaDatosRecibidos(instancia);
						
		new UsuarioLoginDao().actualizar(instancia);
	}

	
	public void borrar(UsuarioLogin instancia) throws UsuarioLoginException {

		logger.debug("UsuarioLoginBL:borrar()");
		
		UsuarioLoginDao usuarioLoginDao = new UsuarioLoginDao();
		usuarioLoginDao.borrar(instancia);

	}

	public List<UsuarioLogin> obtenerListaUsuarioLogin() throws UsuarioLoginException {

		UsuarioLoginDao usuarioLoginDao = new UsuarioLoginDao();
		return usuarioLoginDao.obtenerListaUsuarioLogin();

	}

	public UsuarioLogin obtenerUsuarioLoginPorPK(Integer pk) throws UsuarioLoginException {
		UsuarioLoginDao usuarioLoginDao = new UsuarioLoginDao();
		return usuarioLoginDao.obtenerUsuarioLoginPorPK(pk);
	}
	
	public List<UsuarioLogin> obtenerUsuarioLoginsPorIdUsuario(Integer idUsuario) throws UsuarioLoginException {
		UsuarioLoginDao usuarioLoginDao = new UsuarioLoginDao();
		return usuarioLoginDao.obtenerUsuarioLoginsPorIdUsuario(idUsuario);
	}
		
	
	private void _controlesConsistenciaDatosRecibidos(UsuarioLogin instancia) throws UsuarioLoginException {
		
		Integer idUsuario = instancia.getIdUsuario();
				
		if (idUsuario==null) {
			throw new UsuarioLoginException("El Login debe estar asociado a algún IdUsuario (o al menos a idUsuario=0 en caso de error)");
		} 
		
	}
	
	//mrico - 20110629
	public List<UsuarioLoginAmpliado> finderUsuarioLogin(
			String filtroNombreUsuarioLogin, String filtroApellidosUsuario, 
			Date fechaIngresoInicial, Date fechaIngresoFinal, String aplicacion 
	) throws UsuarioLoginException {
		return new UsuarioLoginDao().finderUsuarioLogin(filtroNombreUsuarioLogin, filtroApellidosUsuario, fechaIngresoInicial, fechaIngresoFinal,aplicacion );
	}
	
	
}




