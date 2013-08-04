package org.beeblos.security.st.bl;

// Generado 25-nov-2010 12:52:05 

import com.sp.common.util.StringPair;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.security.st.dao.UsuarioCuentasEmailDao;
import org.beeblos.security.st.error.UsuarioCuentasEmailException;
import org.beeblos.security.st.model.UsuarioCuentasEmail;

/**
 * Bussiness Logic object for domain model class UsuarioCuentasEmail.
 * @see org.beeblos.security.st.model.bdc.model.UsuarioCuentasEmail
 */

public class UsuarioCuentasEmailBL {

    private static final Log logger = LogFactory.getLog(UsuarioCuentasEmailBL.class);

	
 
	public UsuarioCuentasEmailBL() {

	}

	public Integer agregar(UsuarioCuentasEmail instancia) throws UsuarioCuentasEmailException  {
		
		logger.debug("UsuarioCuentasEmailBL:agregar()");
		
		_controlesConsistenciaDatosRecibidos(instancia);
		_controlesNoRedundancia(instancia);
		
		UsuarioCuentasEmailDao usuarioCuentasEmailDao = new UsuarioCuentasEmailDao();
		
		//martin - 20101221
		//Control de "esPreferida"
		if (instancia.isPreferida()){
			UsuarioCuentasEmail anteriorCuentaPref = usuarioCuentasEmailDao.getCuentaPreferida(instancia.getIdUsuario());
			if (anteriorCuentaPref != null){
				//Se anula la preferencia anterior
				anteriorCuentaPref.setPreferida(false);
				
				usuarioCuentasEmailDao.actualizar(anteriorCuentaPref);
			}
		}
				
		return usuarioCuentasEmailDao.agregar(instancia);
	
	}
	

	public void actualizar(UsuarioCuentasEmail instancia) throws UsuarioCuentasEmailException {
		
		logger.debug("UsuarioCuentasEmailBL:actualizar()");
		
		UsuarioCuentasEmailDao usuarioCuentasEmailDao = new UsuarioCuentasEmailDao();
				
		_controlesConsistenciaDatosRecibidos(instancia);
		_controlesNoRedundanciaActualizar(instancia);
		
		//martin - 20101221
		//Control de "esPreferida"
		if (instancia.isPreferida()){
			UsuarioCuentasEmail anteriorCuentaPref = usuarioCuentasEmailDao.getCuentaPreferida(instancia.getIdUsuario());
			
			if (anteriorCuentaPref != null && 
				instancia.getIdUce() != anteriorCuentaPref.getIdUce()){
			
				//Se anula la preferencia anterior
				anteriorCuentaPref.setPreferida(false);
				
				usuarioCuentasEmailDao.actualizar(anteriorCuentaPref);
			}
		}
				
		new UsuarioCuentasEmailDao().actualizar(instancia);
				
	}

	
	public void borrar(UsuarioCuentasEmail instancia) throws UsuarioCuentasEmailException {

		logger.debug("UsuarioCuentasEmailBL:borrar()");
		
		UsuarioCuentasEmailDao usuarioCuentasEmailDao = new UsuarioCuentasEmailDao();
		usuarioCuentasEmailDao.borrar(instancia);

	}

	public List<UsuarioCuentasEmail> obtenerListaUsuarioCuentasEmail() throws UsuarioCuentasEmailException {

		UsuarioCuentasEmailDao usuarioCuentasEmailDao = new UsuarioCuentasEmailDao();
		return usuarioCuentasEmailDao.obtenerListaUsuarioCuentasEmail();

	}

//	public List<StringPair> obtenerListaUsuarioCuentasEmailParaCombo( String textoPrimeraLinea, String separacion) 
//	throws UsuarioCuentasEmailException {
//
//		UsuarioCuentasEmailDao usuarioCuentasEmailDao = new UsuarioCuentasEmailDao();
//		return usuarioCuentasEmailDao.obtenerListaUsuarioCuentasEmailParaCombo(textoPrimeraLinea, separacion);
//
//	}
	
	public List<StringPair> obtenerListaUsuarioCuentasEmailParaCombo( 
			Integer idUsuarioEspecifico, String textoPrimeraLinea, String separacion) 
	throws UsuarioCuentasEmailException {

		UsuarioCuentasEmailDao usuarioCuentasEmailDao = new UsuarioCuentasEmailDao();
		return usuarioCuentasEmailDao.obtenerListaUsuarioCuentasEmailParaCombo(idUsuarioEspecifico, textoPrimeraLinea, separacion);

	}	

	public UsuarioCuentasEmail obtenerUsuarioCuentasEmailPorPK(Integer pk) throws UsuarioCuentasEmailException {
		UsuarioCuentasEmailDao usuarioCuentasEmailDao = new UsuarioCuentasEmailDao();
		return usuarioCuentasEmailDao.obtenerUsuarioCuentasEmailPorPK(pk);
	}
	
	public UsuarioCuentasEmail obtenerUsuarioCuentasEmailPorNombre(String nombre) throws UsuarioCuentasEmailException {
		UsuarioCuentasEmailDao usuarioCuentasEmailDao = new UsuarioCuentasEmailDao();
		return usuarioCuentasEmailDao.obtenerUsuarioCuentasEmailPorNombre(nombre);
	}
	

	public List<UsuarioCuentasEmail> obtenerUsuarioCuentasEmailPorUsuario(Integer idUsuarioEspecifico) throws UsuarioCuentasEmailException {
		UsuarioCuentasEmailDao usuarioCuentasEmailDao = new UsuarioCuentasEmailDao();
		return usuarioCuentasEmailDao.obtenerListaUsuarioCuentasEmailPorUsuario(idUsuarioEspecifico);
	}
	
	
//	public List<UsuarioCuentasEmail> finderUsuarioCuentasEmail(
//			String filtroName, String filtroApellido1, String filtroApellido2
//		) throws UsuarioCuentasEmailException {
//		
//		return new UsuarioCuentasEmailDao().finderUsuarioCuentasEmail(filtroName, filtroApellido1);
//		
//	}
	
	
	private void _controlesConsistenciaDatosRecibidos(UsuarioCuentasEmail instancia) throws UsuarioCuentasEmailException {
		
		String nombreTmp = instancia.getUceNombre();
				
		if (nombreTmp==null || "".equals(nombreTmp) || " ".equals(nombreTmp) || "".equals(nombreTmp.trim())) {
			throw new UsuarioCuentasEmailException("La Cuenta de Correo que se intenta agregar NO TIENE nombre");
		} 
		
	}
	
	private void _controlesNoRedundancia(UsuarioCuentasEmail instancia) throws UsuarioCuentasEmailException {
		
		UsuarioCuentasEmailDao usuarioCuentasEmailDao = new UsuarioCuentasEmailDao();
		
		String nombreTmp = instancia.getUceNombre();
		
		if((usuarioCuentasEmailDao.obtenerUsuarioCuentasEmailPorNombre(nombreTmp)!= null)){
			throw new UsuarioCuentasEmailException("Ya existe una Cuenta de Correo con nombre:" + nombreTmp);
		}
	
	}
	
	private void _controlesNoRedundanciaActualizar(UsuarioCuentasEmail instancia) throws UsuarioCuentasEmailException {
		
		UsuarioCuentasEmailDao usuarioCuentasEmailDao = new UsuarioCuentasEmailDao();
		
		String nombreTmp = instancia.getUceNombre();
		
		if (usuarioCuentasEmailDao.verificarNombreDuplicado(instancia.getUceNombre(),instancia.getIdUce())){
			throw new UsuarioCuentasEmailException("Ya existe una Cuenta de Correo con nombre:" + nombreTmp);
		}
		
	}
	
	
	
}




