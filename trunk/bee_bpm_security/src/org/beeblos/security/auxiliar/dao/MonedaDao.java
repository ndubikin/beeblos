package org.beeblos.security.auxiliar.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.security.auxiliar.error.MonedaException;
import org.beeblos.security.auxiliar.model.Moneda;
import org.beeblos.security.st.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;


public class MonedaDao {
	
	private static final Log logger = LogFactory.getLog(MonedaDao.class);
	
	public MonedaDao (){
		
	}
	
	public Integer agregar(Moneda moneda) throws MonedaException {
		logger.debug("agregar() MonedaNombre: ["+moneda.getMonedaNombre()+"]");
		logger.info("agregar() MonedaNombre: ["+moneda.getMonedaNombre()+"]");
		try {

			return Integer.valueOf(HibernateUtil.guardar(moneda));

		} catch (HibernateException ex) {
			logger.error("MonedaDao: agregar - No se pudo guardar la moneda "+ moneda.getMonedaNombre());
			throw new MonedaException(ex);

		}

	}
	
	
	public void actualizar(Moneda moneda) throws MonedaException {
		
		logger.debug("actualizar() Moneda < id = "+moneda.getIdMoneda()+">");
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		
		try {

			//HibernateUtil.actualizar;
			
			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			session.update(moneda);
						
			tx.commit();
			
			logger.info("actualizar() Moneda < id = "+moneda.getIdMoneda()+">");

		} catch (HibernateException ex) {
			logger.error("MonedaDao:actualizar - No se pudo actualizar la moneda "+ moneda.getMonedaNombre()  +
					" - id = "+moneda.getIdMoneda()+"\n - "+ex.getMessage()   );
			throw new MonedaException(ex);

		}
					
	}
	
	
	public void borrar(Moneda moneda) throws MonedaException {

		logger.debug("borrar() MonedaNombre: ["+moneda.getMonedaNombre()+"]");
		
		try {

			moneda = obtenerMonedaPorPK(moneda.getIdMoneda());
			
			HibernateUtil.borrar(moneda);
			
			logger.info("borrar() Moneda: ["+moneda.getIdMoneda()+"]");

		} catch (HibernateException ex) {

			logger.error("MonedaDao: borrar - No se pudo borrar la moneda "+ moneda.getMonedaNombre() +
					" <id = "+moneda.getIdMoneda()+ "> \n"+" - "+ex.getMessage() );
			
			throw new MonedaException(ex);

		} catch (MonedaException ex1) {
			logger.error("MonedaDao: borrar - La moneda "+ moneda.getMonedaNombre() +
					" <id = "+moneda.getIdMoneda()+ "> no esta almacenada \n"+" - "+ex1.getMessage() );
			throw new MonedaException(ex1);

		} 

	}

	public Moneda obtenerMonedaPorPK(int i) throws MonedaException {

		Moneda moneda = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			moneda = (Moneda) session.get(Moneda.class, i);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("MonedaDao: obtenerMonedaPorPK - No hay ninguna moneda con id = "+ i + "]  almacenada - \n"+ex.getMessage() );
			throw new MonedaException("No se pudo obtener el Moneda: " + i + " - "
					+ ex.getMessage());

		}

		return moneda;
	}
	
	
	public List<Moneda> obtenerMonedaPorNombre(String monedaNombre) throws MonedaException {

		List<Moneda>  monedas = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			monedas = session.createCriteria(Moneda.class).add(
					Restrictions.naturalId().set("monedaNombre", monedaNombre))
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("MonedaDao: obtenerMonedaPorNombre - No hay ninguna moneda con nombre = "+monedaNombre+ "]  almacenada - \n"+ex.getMessage() );
			throw new MonedaException("No se pudo obtener la lista de Monedas por nombre: " + monedaNombre
					+ " - " + ex.getMessage());

		}

		return monedas;
	}

	
	public List<Moneda> obtenerMonedas() throws MonedaException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Moneda> monedas = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			monedas = session.createQuery("From Moneda order by idMoneda").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("MonedaDao: obtenerMonedas() - No pudo recuperarse la lista de monedas almacenadas - "+ex.getMessage() );
			throw new MonedaException("Error al recuperar lista de monedas: "
					+ ex.getMessage());

		}

		return monedas;
	}
}
	