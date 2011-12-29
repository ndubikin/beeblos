package org.beeblos.security.auxiliar.bl;

import java.util.List;

import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.security.auxiliar.dao.PaisDao;
import org.beeblos.security.auxiliar.error.PaisException;
import org.beeblos.security.auxiliar.model.Pais;

public class PaisBL {

	public PaisBL() {

	}

	public void agregar(Pais pais) throws PaisException {

		PaisDao pDao = new PaisDao();
		pDao.agregar(pais);

	}

	public void actualizar(Pais pais) throws PaisException {
		
		PaisDao pDao = new PaisDao();
		pDao.actualizar(pais);
		
	}

	public void borra(Pais pais) throws PaisException {

		PaisDao pDao = new PaisDao();
		pDao.borrar(pais);

	}

	public List<Pais> obtenerPaises() throws PaisException {

		PaisDao pDao = new PaisDao();
		return pDao.obtenerPaises();

	}
	
	public List<Pais> obtenerPaises(Integer idPais) throws PaisException {
		
		PaisDao pDao = new PaisDao();
		return pDao.obtenerPaises(idPais);
		
	}
/*
	public List<Pais> obtenerPaisesPorNombre() throws PaisException {

		PaisDao pDao = new PaisDao();
		return pDao.obtenerPaisesPorNombre();

	}
*/
	public List<StringPair> obtenerPaisesParaCombo(
			String textoPrimeraLinea, String separacion 
	) throws PaisException {

		PaisDao pDao = new PaisDao();
		return pDao.obtenerPaisesParaCombo(textoPrimeraLinea, separacion);

	}

	public Pais obtenerPaisPorNombre(String nombre) throws PaisException {

		PaisDao pDao = new PaisDao();
		return pDao.obtenerPaisPorNombre(nombre);

	}

	public Pais obtenerPaisPorPK(String pk) throws PaisException {
		
		PaisDao pDao = new PaisDao();
		return pDao.obtenerPaisPorPK(pk);
	}
	
	//mrico - 20110118
	public List<Pais> finderPais(
			String filtroId, String filtroNombre
	) throws PaisException {
		
		return new PaisDao().finderPais(filtroId, filtroNombre);
	
	}
	
	
}
