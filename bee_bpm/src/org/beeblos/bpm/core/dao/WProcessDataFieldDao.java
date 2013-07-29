package org.beeblos.bpm.core.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDataFieldException;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;


public class WProcessDataFieldDao {
	
	private static final Log logger = LogFactory.getLog(WProcessDataFieldDao.class.getName());
	
	public WProcessDataFieldDao(){
		
	}
	
	public Integer add(WProcessDataField processDataField) throws WProcessDataFieldException {
		
		logger.debug("add() WProcessDataField - Name: ["+processDataField.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(processDataField));

		} catch (HibernateException ex) {
			logger.error("WProcessDataFieldDao: add - Can't store process processDataField definition record "+ 
					processDataField.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDataFieldException("WProcessDataFieldDao: add - Can't store process definition record "+ 
					processDataField.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WProcessDataField processDataField) throws WProcessDataFieldException {
		
		logger.debug("update() WProcessDataField < id = "+processDataField.getId()+">");
		
		try {

			HibernateUtil.actualizar(processDataField);


		} catch (HibernateException ex) {
			logger.error("WProcessDataFieldDao: update - Can't update process definition record "+ 
					processDataField.getName()  +
					" - id = "+processDataField.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WProcessDataFieldException("WProcessDataFieldDao: update - Can't update process definition record "+ 
					processDataField.getName()  +
					" - id = "+processDataField.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WProcessDataField processDataField) throws WProcessDataFieldException {

		logger.debug("delete() WProcessDataField - Name: ["+processDataField.getName()+"]");
		
		try {

			HibernateUtil.borrar(processDataField);

		} catch (HibernateException ex) {
			logger.error("WProcessDataFieldDao: delete - Can't delete process definition record "+ processDataField.getName() +
					" <id = "+processDataField.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDataFieldException("WProcessDataFieldDao:  delete - Can't delete process definition record  "+ processDataField.getName() +
					" <id = "+processDataField.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} 

	}

	public WProcessDataField getWProcessDataFieldByPK(Integer id) throws WProcessDataFieldException {

		WProcessDataField process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			process = (WProcessDataField) session.get(WProcessDataField.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDataFieldDao: getWProcessDataFieldByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDataFieldException("WProcessDataFieldDao: getWProcessDataFieldByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}

	
	public WProcessDataField getWProcessDataFieldByName(String name) throws WProcessDataFieldException {

		WProcessDataField process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			process = (WProcessDataField) session.createCriteria(WProcessDataField.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDataFieldDao: getWProcessDataFieldByName - can't obtain process name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDataFieldException("getWProcessDataFieldByName;  can't obtain process name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}

	
	public List<WProcessDataField> getWProcessDataFieldList() throws WProcessDataFieldException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WProcessDataField> processs = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processs = session.createQuery("From WProcessDataField order by id ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDataFieldDao: getWProcessDataFieldList() - can't obtain process list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDataFieldException("WProcessDataFieldDao: getWProcessDataFieldList() - can't obtain process list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return processs;
	}


}
	
