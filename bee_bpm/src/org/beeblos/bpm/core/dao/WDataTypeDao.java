package org.beeblos.bpm.core.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WDataTypeException;
import org.beeblos.bpm.core.model.WDataType;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;


public class WDataTypeDao {
	
	private static final Log logger = LogFactory.getLog(WDataTypeDao.class.getName());
	
	public WDataTypeDao (){
		
	}
	
	public Integer add(WDataType dataType) throws WDataTypeException {
		
		logger.debug("add() WDataType - Name: ["+dataType.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(dataType));

		} catch (HibernateException ex) {
			logger.error("WDataTypeDao: add - Can't store process definition record "+ 
					dataType.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WDataTypeException("WDataTypeDao: add - Can't store process definition record "+ 
					dataType.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WDataType dataType) throws WDataTypeException {
		
		logger.debug("update() WDataType < id = "+dataType.getId()+">");
		
		try {

			HibernateUtil.actualizar(dataType);


		} catch (HibernateException ex) {
			logger.error("WDataTypeDao: update - Can't update process definition record "+ 
					dataType.getName()  +
					" - id = "+dataType.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WDataTypeException("WDataTypeDao: update - Can't update process definition record "+ 
					dataType.getName()  +
					" - id = "+dataType.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WDataType dataType) throws WDataTypeException {

		logger.debug("delete() WDataType - Name: ["+dataType.getName()+"]");
		
		try {

			HibernateUtil.borrar(dataType);

		} catch (HibernateException ex) {
			logger.error("WDataTypeDao: delete - Can't delete proccess definition record "+ dataType.getName() +
					" <id = "+dataType.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WDataTypeException("WDataTypeDao:  delete - Can't delete proccess definition record  "+ dataType.getName() +
					" <id = "+dataType.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

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
			logger.warn("WDataTypeDao: getWDataTypeByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WDataTypeException("WDataTypeDao: getWDataTypeByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

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
			logger.warn("WDataTypeDao: getWDataTypeByName - can't obtain process name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WDataTypeException("getWDataTypeByName;  can't obtain process name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

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
			logger.warn("WDataTypeDao: getWDataTypeList() - can't obtain process list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WDataTypeException("WDataTypeDao: getWDataTypeList() - can't obtain process list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return dataType;
	}


}
	