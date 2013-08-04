package org.beeblos.security.st.bl;
import com.sp.common.util.StringPair;
import java.util.Date;
import java.util.List;

import org.beeblos.security.st.dao.UsuarioDao;
import org.beeblos.security.st.error.UsuarioException;
import org.beeblos.security.st.model.Usuario;

public class UsuarioBL {

	public UsuarioBL() {

	}

	public void agregar(Usuario usuario) throws UsuarioException {
		
		_controlesConsistenciaDatosRecibidos(usuario);
		_controlesNoRedundancia(usuario);
		
		usuario.setFechaAlta(new Date());
		usuario.setFechaModificacion(new Date());
		
		UsuarioDao uDao = new UsuarioDao();
		uDao.agregar(usuario);

	}
	
	public void actualizar(Usuario usuario) throws UsuarioException {
	
		UsuarioDao usuarioDao = new UsuarioDao();
		Usuario instGuardada = usuarioDao.obtenerUsuarioPorPK(usuario.getIdUsuario());
		
		// Actualizar solo si hay cambios
		if (!usuario.equals(instGuardada)){
		
			_controlesConsistenciaDatosRecibidos(usuario);
			_controlesCamposFijos(usuario);
			_controlesNoRedundanciaActualizar(usuario);
			
			usuario.setFechaModificacion(new Date());
			
			UsuarioDao uDao = new UsuarioDao();
			uDao.actualizar(usuario);
		}
	
	}

	// nes 20080109
	public void actualizarPwd(Usuario usuario) throws UsuarioException {
		UsuarioDao uDao = new UsuarioDao();
		uDao.actualizarPwd(usuario);
	}
	
	
	public Usuario autenticarUsuario(Usuario usuario) throws UsuarioException {

		UsuarioDao usuarioDao = new UsuarioDao();
		return usuarioDao.autenticarUsuario(usuario);

	}

	public void borrar(Usuario usuario) throws UsuarioException {
		UsuarioDao uDao = new UsuarioDao();
		uDao.borrar(usuario);
	}

	public Usuario obtenerUsuarioPorNombre(String nombres) throws UsuarioException {
		UsuarioDao uDao = new UsuarioDao();
		return uDao.obtenerUsuarioPorNombre(nombres);
	}

	public Usuario obtenerUsuarioPorPK(Integer pk) throws UsuarioException {
		UsuarioDao uDao = new UsuarioDao();
		return uDao.obtenerUsuarioPorPK(pk);
	}

	public List<Usuario> obtenerUsuarios() throws UsuarioException {
		UsuarioDao uDao = new UsuarioDao();
		return uDao.obtenerUsuarios();

	}
	
	//rrl 20120710
	public List<StringPair> obtenerUsuariosParaCombo( 
			String textoPrimeraLinea, String separacion) 
	throws UsuarioException {

		UsuarioDao usuarioCuentasEmailDao = new UsuarioDao();
		return usuarioCuentasEmailDao.obtenerUsuariosParaCombo(textoPrimeraLinea, separacion);

	}

	public Usuario obtenerUsuarioLoginPorNombre(String usuarioLogin) throws UsuarioException {
		UsuarioDao uDao = new UsuarioDao();
		return uDao.obtenerUsuarioLoginPorNombre(usuarioLogin);
	}
	
	public List<Usuario> finderUsuario(
			String filtroNombre, String filtroApellidos, String filtroLogin
	) throws UsuarioException {
		
		return new UsuarioDao().finderUsuario(filtroNombre, filtroApellidos, filtroLogin);
		
	}
	
	private void _controlesConsistenciaDatosRecibidos(Usuario instancia) throws UsuarioException {
		
		String loginTmp = instancia.getUsuarioLogin();
				
		if ( loginTmp==null || "".equals( loginTmp) || " ".equals( loginTmp) || "".equals( loginTmp.trim())) {
			throw new UsuarioException("El Usuario que se intenta agregar NO TIENE login");
		} 
		
	}
	
	private void _controlesNoRedundancia(Usuario instancia) throws UsuarioException {
		
		UsuarioDao usuarioDao = new UsuarioDao();
		
		String nombreTmp = instancia.getUsuarioLogin();
		
		if((usuarioDao.obtenerUsuarioPorNombre(nombreTmp)!= null)){
			throw new UsuarioException("Ya existe un  Usuario con nombre largo:" + nombreTmp);
		}
	
	}
	
	private void _controlesNoRedundanciaActualizar(Usuario instancia) throws UsuarioException {
		
		UsuarioDao usuarioDao = new UsuarioDao();
		
		String nombreTmp = instancia.getUsuarioLogin();
		
		if (usuarioDao.verificarNombreDuplicado(instancia.getUsuarioLogin(),instancia.getIdUsuario())){
			throw new UsuarioException("Ya existe un Usuario con nombre largo :" + nombreTmp);
		}
		
	}
	
	// Para controlar que los campos de fecha y usuario alta no esten modificados
	private void _controlesCamposFijos(Usuario instancia) throws UsuarioException {
		
		UsuarioDao cDao = new UsuarioDao();
		Usuario c = cDao.obtenerUsuarioPorPK(instancia.getIdUsuario());
		
		// controla que est� modificando el registro correcto:
			if (!c.getFechaAlta().equals(instancia.getFechaAlta()) || 
					( c.getUsuarioAlta()==null && instancia.getUsuarioAlta()!=null ) || // nes 20101013 - nota: esto est� porq no lo estamos grabando pero lo l�gico ser�a q no pueda ser null a nivel de bd
					( c.getUsuarioAlta()!=null && !c.getUsuarioAlta().equals(instancia.getUsuarioAlta())) ) {
				throw new UsuarioException("No se puede actualizar - el registro de la base de datos ha cambiado o hay un error en los datos que se intentan actualizar.");
			}
		}
		

	

}
