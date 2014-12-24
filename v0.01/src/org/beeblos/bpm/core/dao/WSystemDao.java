package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WSystemException;
import org.beeblos.bpm.core.model.WSystem;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.sp.common.util.HibernateUtil;
import com.sp.common.util.StringPair;

public class WSystemDao {
	private static final Log logger = LogFactory.getLog(WSystemDao.class.getName());

	public WSystemDao (){

	}

	public Integer add(WSystem system) throws WSystemException {

		logger.debug("add() WSystem - Name: ["+system.getName()+"]");

		try {

			return Integer.valueOf(HibernateUtil.save(system));

		} catch (HibernateException ex) {

			String mess = "HibernateException: add() - " 
				+ "It was not posible to get the WSystem list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WSystemException(ex);

		} catch (Exception ex) {

			String mess = "Exception: add() - "
					+ "It was not posible to get the WSystem list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WSystemException(ex);
			
		}

	}


	public void update(WSystem system) throws WSystemException {

		logger.debug("update() WSystem < id = "+system.getId()+">");

		try {

			HibernateUtil.update(system);


		} catch (HibernateException ex) {

			String mess = "HibernateException: update() - " 
				+ "It was not posible to get the WSystem list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WSystemException(ex);

		} catch (Exception ex) {

			String mess = "Exception: update() - "
					+ "It was not posible to get the WSystem list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WSystemException(ex);
			
		}

	}


	public void delete(WSystem system) throws WSystemException {

		logger.debug("delete() WSystem - Name: ["+system.getName()+"]");

		try {

			HibernateUtil.delete(system);

		} catch (HibernateException ex) {

			String mess = "HibernateException: delete() - " 
				+ "It was not posible to get the WSystem list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WSystemException(ex);

		} catch (Exception ex) {
			
			String mess = "Exception: delete() - "
					+ "It was not posible to get the WSystem list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WSystemException(ex);
			
		}


	}

	public WSystem getWSystemByPK(Integer id) throws WSystemException {

		WSystem system = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			system = (WSystem) session.get(WSystem.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "HibernateException: getWSystemByPK() - " 
				+ "It was not posible to get the WSystem list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WSystemException(ex);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "Exception: getWSystemByPK() - "
					+ "It was not posible to get the WSystem list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WSystemException(ex);
			
		}

		return system;
	}


	public WSystem getWSystemByName(String name) throws WSystemException {

		WSystem system = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			system = (WSystem) session.createCriteria(WSystem.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "HibernateException: getWSystemByName() - " 
				+ "It was not posible to get the WSystem list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WSystemException(ex);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "Exception: getWSystemByName() - "
					+ "It was not posible to get the WSystem list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WSystemException(ex);
			
		}
		return system;
	}


	public List<WSystem> getWSystemList() throws WSystemException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WSystem> system = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			system = session.createQuery("From WSystem order by id ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "HibernateException: getWSystemList() - " 
				+ "It was not posible to get the WSystem list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WSystemException(ex);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "Exception: getWSystemList() - "
					+ "It was not posible to get the WSystem list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WSystemException(ex);
			
		}
		

		return system;
	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String separationLine )
					throws WSystemException {

		List<WSystem> ldt = null;
		List<StringPair> retorno = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			ldt = session
					.createQuery("From WSystem Order By name ")
					.list();

			if (ldt!=null) {

				// inserta los extras
				if ( firstLineText!=null && !"".equals(firstLineText) ) {
					if ( !firstLineText.equals("WHITESPACE") ) {
						retorno.add(new StringPair(null,firstLineText));  // deja la primera línea con lo q venga
					} else {
						retorno.add(new StringPair(null," ")); // deja la primera línea en blanco ...
					}
				}

				if ( separationLine!=null && !"".equals(separationLine) ) {
					if ( !separationLine.equals("WHITESPACE") ) {
						retorno.add(new StringPair(null,separationLine));  // deja la separación línea con lo q venga
					} else {
						retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
					}
				}



				for (WSystem dt: ldt) {
					retorno.add(new StringPair(dt.getId(),dt.getName()));
				}
			} else {
				// nes  - si el select devuelve null entonces devuelvo null
				retorno=null;
			}

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "HibernateException: getComboList() - " 
				+ "It was not posible to get the WSystem list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WSystemException(ex);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "Exception: getComboList() - "
					+ "It was not posible to get the WSystem list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WSystemException(ex);
			
		}
		
		return retorno;


	}

}
