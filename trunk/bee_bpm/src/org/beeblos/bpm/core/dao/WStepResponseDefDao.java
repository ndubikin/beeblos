package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WStepResponseDefException;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;





public class WStepResponseDefDao {
	
	private static final Log logger = LogFactory.getLog(WStepResponseDefDao.class.getName());
	
	public WStepResponseDefDao (){
		
	}
	
	public Integer add(WStepResponseDef response) throws WStepResponseDefException {
		
		logger.debug("add() WStepResponseDef - Name: ["+response.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(response));

		} catch (HibernateException ex) {
			logger.error("WStepResponseDefDao: add - Can't store response definition record "+ 
					response.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepResponseDefException("WStepResponseDefDao: add - Can't store response definition record "+ 
					response.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WStepResponseDef response) throws WStepResponseDefException {
		
		logger.debug("update() WStepResponseDef < id = "+response.getId()+">");
		
		try {

			HibernateUtil.actualizar(response);


		} catch (HibernateException ex) {
			logger.error("WStepResponseDefDao: update - Can't update response definition record "+ 
					response.getName()  +
					" - id = "+response.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WStepResponseDefException("WStepResponseDefDao: update - Can't update response definition record "+ 
					response.getName()  +
					" - id = "+response.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WStepResponseDef response) throws WStepResponseDefException {

		logger.debug("delete() WStepResponseDef - Name: ["+response.getName()+"]");
		
		try {

			response = getWStepResponseDefByPK(response.getId());

			HibernateUtil.borrar(response);

		} catch (HibernateException ex) {
			logger.error("WStepResponseDefDao: delete - Can't delete proccess definition record "+ response.getName() +
					" <id = "+response.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepResponseDefException("WStepResponseDefDao:  delete - Can't delete proccess definition record  "+ response.getName() +
					" <id = "+response.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (WStepResponseDefException ex1) {
			logger.error("WStepResponseDefDao: delete - Exception in deleting response rec "+ response.getName() +
					" <id = "+response.getId()+ "> no esta almacenada \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );
			throw new WStepResponseDefException("WStepResponseDefDao: delete - Exception in deleting response rec "+ response.getName() +
					" <id = "+response.getId()+ "> not stored \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );

		} 

	}

	public WStepResponseDef getWStepResponseDefByPK(Integer id) throws WStepResponseDefException {

		WStepResponseDef response = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			response = (WStepResponseDef) session.get(WStepResponseDef.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepResponseDefDao: getWStepResponseDefByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepResponseDefException("WStepResponseDefDao: getWStepResponseDefByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return response;
	}
	
	
	public WStepResponseDef getWStepResponseDefByName(String name) throws WStepResponseDefException {

		WStepResponseDef  response = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			response = (WStepResponseDef) session.createCriteria(WStepResponseDef.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepResponseDefDao: getWStepResponseDefByName - can't obtain response name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepResponseDefException("getWStepResponseDefByName;  can't obtain response name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return response;
	}

	
	public List<WStepResponseDef> getWStepResponseDefs() throws WStepResponseDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepResponseDef> responses = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			responses = session.createQuery("From WStepResponseDef order by name ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepResponseDefDao: getWStepResponseDefs() - can't obtain response list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepResponseDefException("WStepResponseDefDao: getWStepResponseDefs() - can't obtain response list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return responses;
	}
	
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WStepResponseDefException {
		 
			List<WStepResponseDef> lwpd = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwpd = session
						.createQuery("From WStepResponseDef order by name ")
						.list();
		
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
					retorno.add(new StringPair("",""));
				
					
					for (WStepResponseDef wpd: lwpd) {
						retorno.add(new StringPair(wpd.getId(),wpd.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WStepResponseDefException(
						"Can't obtain WStepResponseDefs combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;


	}

}
	