package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WDataTypeException;
import org.beeblos.bpm.core.error.WSystemException;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.sp.common.model.WDataType;
import com.sp.common.util.HibernateUtil;
import com.sp.common.util.StringPair;


public class WDataTypeDao {
	
	private static final Log logger = LogFactory.getLog(WDataTypeDao.class.getName());
	
	public WDataTypeDao (){
		
	}
	
	public Integer add(WDataType dataType) throws WDataTypeException {
		
		logger.debug("add() WDataType - Name: ["+dataType.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.save(dataType));

		} catch (HibernateException ex) {
			
			String mess = "HibernateException: add() - " 
				+ "It was not posible to get the WDataType list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WDataTypeException(ex);

		} catch (Exception ex) {
			
			String mess = "Exception: add() - "
					+ "It was not posible to get the WDataType list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WDataTypeException(ex);
			
		}

	}
	
	
	public void update(WDataType dataType) throws WDataTypeException {
		
		logger.debug("update() WDataType < id = "+dataType.getId()+">");
		
		try {

			HibernateUtil.update(dataType);


		} catch (HibernateException ex) {
			
			String mess = "HibernateException: update() - " 
				+ "It was not posible to get the WDataType list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WDataTypeException(ex);

		} catch (Exception ex) {
			
			String mess = "Exception: update() - "
					+ "It was not posible to get the WDataType list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WDataTypeException(ex);
			
		}
					
	}
	
	
	public void delete(WDataType dataType) throws WDataTypeException {

		logger.debug("delete() WDataType - Name: ["+dataType.getName()+"]");
		
		try {

			HibernateUtil.delete(dataType);

		} catch (HibernateException ex) {
			
			String mess = "HibernateException: delete() - " 
				+ "It was not posible to get the WDataType list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WDataTypeException(ex);

		} catch (Exception ex) {
			
			String mess = "Exception: delete() - "
					+ "It was not posible to get the WDataType list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WDataTypeException(ex);
			
		}

	}

	public WDataType getWDataTypeByPK(Integer id) throws WDataTypeException {

		WDataType dataType = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			dataType = (WDataType) session.get(WDataType.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "HibernateException: getWDataTypeByPK() - " 
				+ "It was not posible to get the WDataType list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WDataTypeException(ex);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "Exception: getWDataTypeByPK() - "
					+ "It was not posible to get the WDataType list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WDataTypeException(ex);
			
		}

		return dataType;
	}
	
	
	public WDataType getWDataTypeByName(String name) throws WDataTypeException {

		WDataType dataType = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			dataType = (WDataType) session.createCriteria(WDataType.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "HibernateException: getWDataTypeByName() - " 
				+ "It was not posible to get the WDataType list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WDataTypeException(ex);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "Exception: getWDataTypeByName() - "
					+ "It was not posible to get the WDataType list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WDataTypeException(ex);
			
		}

		return dataType;
	}

	
	public List<WDataType> getWDataTypeList() throws WDataTypeException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WDataType> dataType = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			dataType = session.createQuery("From WDataType order by id ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "HibernateException: getWDataTypeList() - " 
				+ "It was not posible to get the WDataType list - "
				+ ex.getMessage() + " " 
				+ ex.getLocalizedMessage() + " " 
				+ (ex.getCause()!=null?ex.getCause():"");
				
			logger.warn( mess );
			throw new WDataTypeException(ex);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			
			String mess = "Exception: getWDataTypeList() - "
					+ "It was not posible to get the WDataType list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			
			logger.warn( mess );
			throw new WDataTypeException(ex);
			
		}

		return dataType;
	}
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String separationLine )
	throws WDataTypeException {
		 
			List<WDataType> ldt = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				ldt = session
						.createQuery("From WDataType Order By name ")
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
					
				
					
					for (WDataType dt: ldt) {
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
					+ "It was not posible to get the WDataType list - "
					+ ex.getMessage() + " " 
					+ ex.getLocalizedMessage() + " " 
					+ (ex.getCause()!=null?ex.getCause():"");
					
				logger.warn( mess );
				throw new WDataTypeException(ex);

			} catch (Exception ex) {
				if (tx != null)
					tx.rollback();
				
				String mess = "Exception: getComboList() - "
						+ "It was not posible to get the WDataType list - "
						+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " 
						+ (ex.getCause()!=null?ex.getCause():"")+" "
						+ ex.getClass();
				
				logger.warn( mess );
				throw new WDataTypeException(ex);
				
			}

			return retorno;


	}

}
	