package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WTimeUnitException;
import org.beeblos.bpm.core.model.WTimeUnit;
import com.sp.common.util.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;




public class WTimeUnitDao {
	
	private static final Log logger = LogFactory.getLog(WTimeUnitDao.class.getName());
	
	public WTimeUnitDao (){
		
	}
	
	public Integer add(WTimeUnit timeUnit) throws WTimeUnitException {
		
		logger.debug("add() WTimeUnit - Name: ["+timeUnit.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.save(timeUnit));

		} catch (HibernateException ex) {
			logger.error("WTimeUnitDao: add - Can't store timeUnit definition record "+ 
					timeUnit.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WTimeUnitException("WTimeUnitDao: add - Can't store timeUnit definition record "+ 
					timeUnit.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WTimeUnit timeUnit) throws WTimeUnitException {
		
		logger.debug("update() WTimeUnit < id = "+timeUnit.getId()+">");
		
		try {

			HibernateUtil.update(timeUnit);


		} catch (HibernateException ex) {
			logger.error("WTimeUnitDao: update - Can't update timeUnit definition record "+ 
					timeUnit.getName()  +
					" - id = "+timeUnit.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WTimeUnitException("WTimeUnitDao: update - Can't update timeUnit definition record "+ 
					timeUnit.getName()  +
					" - id = "+timeUnit.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WTimeUnit timeUnit) throws WTimeUnitException {

		logger.debug("delete() WTimeUnit - Name: ["+timeUnit.getName()+"]");
		
		try {

			timeUnit = getWTimeUnitByPK(timeUnit.getId());

			HibernateUtil.delete(timeUnit);

		} catch (HibernateException ex) {
			logger.error("WTimeUnitDao: delete - Can't delete proccess definition record "+ timeUnit.getName() +
					" <id = "+timeUnit.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WTimeUnitException("WTimeUnitDao:  delete - Can't delete proccess definition record  "+ timeUnit.getName() +
					" <id = "+timeUnit.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (WTimeUnitException ex1) {
			logger.error("WTimeUnitDao: delete - Exception in deleting timeUnit rec "+ timeUnit.getName() +
					" <id = "+timeUnit.getId()+ "> no esta almacenada \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );
			throw new WTimeUnitException("WTimeUnitDao: delete - Exception in deleting timeUnit rec "+ timeUnit.getName() +
					" <id = "+timeUnit.getId()+ "> not stored \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );

		} 

	}

	public WTimeUnit getWTimeUnitByPK(Integer id) throws WTimeUnitException {

		WTimeUnit timeUnit = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			timeUnit = (WTimeUnit) session.get(WTimeUnit.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WTimeUnitDao: getWTimeUnitByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WTimeUnitException("WTimeUnitDao: getWTimeUnitByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return timeUnit;
	}
	
	
	public WTimeUnit getWTimeUnitByName(String name) throws WTimeUnitException {

		WTimeUnit  timeUnit = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			timeUnit = (WTimeUnit) session.createCriteria(WTimeUnit.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WTimeUnitDao: getWTimeUnitByName - can't obtain timeUnit name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WTimeUnitException("getWTimeUnitByName;  can't obtain timeUnit name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return timeUnit;
	}

	
	public List<WTimeUnit> getWTimeUnits() throws WTimeUnitException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WTimeUnit> timeUnits = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			timeUnits = session.createQuery("From WTimeUnit order by name ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WTimeUnitDao: getWTimeUnits() - can't obtain timeUnit list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WTimeUnitException("WTimeUnitDao: getWTimeUnits() - can't obtain timeUnit list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return timeUnits;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WTimeUnitException {
		 
			List<WTimeUnit> lwpd = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwpd = session
						.createQuery("From WTimeUnit order by name ")
						.list();
		
				tx.commit();

				if (lwpd!=null) {
					
					// inserta los extras
					if ( textoPrimeraLinea!=null && !"".equals(textoPrimeraLinea) ) {
						if ( !textoPrimeraLinea.equals("WHITESPACE") ) {
							retorno.add(new StringPair(null,textoPrimeraLinea));  // deja la primera línea con lo q venga
						} else {
							retorno.add(new StringPair(null," ")); // deja la primera línea en blanco ...
						}
					}
					
					if ( separacion!=null && !"".equals(separacion) ) {
						if ( !separacion.equals("WHITESPACE") ) {
							retorno.add(new StringPair(null,separacion));  // deja la separación línea con lo q venga
						} else {
							retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
						}
					}
					
				
					
					for (WTimeUnit wpd: lwpd) {
						retorno.add(new StringPair(wpd.getId(),wpd.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WTimeUnitException(
						"Can't obtain WTimeUnits combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;


	}

}
	