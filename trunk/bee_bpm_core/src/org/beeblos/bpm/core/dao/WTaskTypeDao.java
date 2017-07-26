package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WTaskTypeException;
import org.beeblos.bpm.core.model.WTaskType;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.sp.hb4util.core.util.HibernateUtil;
import com.sp.common.util.StringPair;

public class WTaskTypeDao {

	private static final Log logger = LogFactory.getLog(WTaskTypeDao.class.getName());
	
	public WTaskTypeDao (){
		
	}
	
	public Integer add(WTaskType taskType) throws WTaskTypeException {
		
		logger.debug("add() WTaskType - Name: ["+taskType.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.save(taskType));

		} catch (HibernateException ex) {
			
			String mess = "HibernateException: add() - " 
				+ "It was not posible to get the WTaskType list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WTaskTypeException(ex);

		} catch (Exception ex) {
			
			String mess = "Exception: add() - "
					+ "It was not posible to get the WTaskType list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WTaskTypeException(ex);
			
		}

	}
	
	
	public void update(WTaskType taskType) throws WTaskTypeException {
		
		logger.debug("update() WTaskType < id = "+taskType.getId()+">");
		
		try {

			HibernateUtil.update(taskType);


		} catch (HibernateException ex) {
			
			String mess = "HibernateException: update() - " 
				+ "It was not posible to get the WTaskType list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WTaskTypeException(ex);

		} catch (Exception ex) {
			
			String mess = "Exception: update() - "
					+ "It was not posible to get the WTaskType list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WTaskTypeException(ex);
			
		}
					
	}
	
	
	public void delete(WTaskType taskType) throws WTaskTypeException {

		logger.debug("delete() WTaskType - Name: ["+taskType.getName()+"]");
		
		try {

			HibernateUtil.delete(taskType);

		} catch (HibernateException ex) {
			
			String mess = "HibernateException: delete() - " 
				+ "It was not posible to get the WTaskType list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WTaskTypeException(ex);

		} catch (Exception ex) {
			
			String mess = "Exception: delete() - "
					+ "It was not posible to get the WTaskType list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WTaskTypeException(ex);
			
		}

	}

	public WTaskType getWTaskTypeByPK(Integer id) throws WTaskTypeException {

		WTaskType taskType = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			taskType = (WTaskType) session.get(WTaskType.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "HibernateException: getWTaskTypeByPK() - " 
				+ "It was not posible to get the WTaskType list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WTaskTypeException(ex);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "Exception: getWTaskTypeByPK() - "
					+ "It was not posible to get the WTaskType list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WTaskTypeException(ex);
			
		}

		return taskType;
	}
	
	
	public WTaskType getWTaskTypeByName(String name) throws WTaskTypeException {

		WTaskType taskType = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			taskType = (WTaskType) session.createCriteria(WTaskType.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "HibernateException: getWTaskTypeByName() - " 
				+ "It was not posible to get the WTaskType list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WTaskTypeException(ex);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "Exception: getWTaskTypeByName() - "
					+ "It was not posible to get the WTaskType list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WTaskTypeException(ex);
			
		}

		return taskType;
	}

	
	public List<WTaskType> getWTaskTypeList() throws WTaskTypeException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WTaskType> taskType = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			taskType = session.createQuery("From WTaskType order by id ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "HibernateException: getWTaskTypeList() - " 
				+ "It was not posible to get the WTaskType list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WTaskTypeException(ex);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "Exception: getWTaskTypeList() - "
					+ "It was not posible to get the WTaskType list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WTaskTypeException(ex);
			
		}

		return taskType;
	}
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String separationLine )
	throws WTaskTypeException
	{
		 
			List<WTaskType> ldt = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				ldt = session
						.createQuery("From WTaskType Order By name ")
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
					
				
					
					for (WTaskType dt: ldt) {
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
					+ "It was not posible to get the WTaskType list - "
					+ ex.getMessage() + " " 
					+ ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"");
					
				logger.warn( mess );
				throw new WTaskTypeException(ex);

			} catch (Exception ex) {
				if (tx != null)
					tx.rollback();
				
				String mess = "Exception: getComboList() - "
						+ "It was not posible to get the WTaskType list - "
						+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
						+ (ex.getCause()!=null?ex.getCause():"")+" "
						+ ex.getClass();
				
				logger.warn( mess );
				throw new WTaskTypeException(ex);
				
			}


			return retorno;


	}
}
