package org.beeblos.security.auxiliar.bl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.security.auxiliar.dao.MonedaDao;
import org.beeblos.security.auxiliar.error.MonedaException;
import org.beeblos.security.auxiliar.model.Moneda;



public class MonedaBL {

	private static final Log logger = LogFactory.getLog(MonedaBL.class);
	
	public MonedaBL() {

	}

	public Integer agregar(Moneda moneda) throws MonedaException {
		
		logger.debug("MonedaBL:agregar()");
		
		MonedaDao tDao = new MonedaDao();
		return tDao.agregar(moneda);

	}

	public void actualizar(Moneda moneda) throws MonedaException {
		logger.debug("MonedaBL:actualizar()");
		
		new MonedaDao().actualizar(moneda);
		//aDao.actualizar(Moneda);
	}


	public void borrar(Moneda moneda) throws MonedaException {

		logger.debug("MonedaBL:borrar()");
		
		MonedaDao tDao = new MonedaDao();
		tDao.borrar(moneda);
		
	}

	public List<Moneda> obtenerMonedas() throws MonedaException {

		MonedaDao tDao = new MonedaDao();
		return tDao.obtenerMonedas();

	}


	public List<Moneda> obtenerMonedaPorNombre(String nombre) throws MonedaException {

		MonedaDao tDao = new MonedaDao();
		return tDao.obtenerMonedaPorNombre(nombre);

	}
		

	public Moneda obtenerMonedaPorPK(Integer pk) throws MonedaException {
		MonedaDao tDao = new MonedaDao();
		return tDao.obtenerMonedaPorPK(pk);
	}
	
}

	