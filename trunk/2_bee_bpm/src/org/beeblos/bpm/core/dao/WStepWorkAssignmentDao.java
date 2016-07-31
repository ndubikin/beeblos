package org.beeblos.bpm.core.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.model.WStepWorkAssignment;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sp.hb4util.core.util.HibernateUtil;

/**
 * Lo hice pero por ahora no le doy uso
 * @author nestor 20160315
 *
 */
public class WStepWorkAssignmentDao {
	
	private static final Log logger = LogFactory.getLog(WStepWorkAssignmentDao.class.getName());
	
	public WStepWorkAssignmentDao (){
		
	}
	

	/**
	 * 
	 * @param instance
	 * @return
	 * @throws WStepWorkException
	 */
	public Integer add(WStepWorkAssignment instance) throws WStepWorkException {
		logger.debug("add() WStepWorkAssignment :["+(instance!=null?instance.toString():"null")+"]");
		
		Integer id=null;
		Serializable res;
		
		Transaction tx = null;
		try {

			Session session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			res = session.save(instance);

			tx.commit();
			
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess="HibernateException: add - WStepWorkAssignment:"
					+(instance!=null?instance.toString():"null")
					+ex.getMessage()+" "
					+(ex.getCause()!=null?ex.getCause():"");
			logger.error( mess );
			throw new WStepWorkException( mess );
			
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			
			String mess="Exception: add - WStepWorkAssignment:"
					+(instance!=null?instance.toString():"null")
					+ex.getMessage()+" "
					+(ex.getCause()!=null?ex.getCause():"")+" "
					+ex.getClass();
			logger.error( mess );
			throw new WStepWorkException( mess );			
		}

		return id;
		
	}
	
	/**
	 * 
	 * 
	 * @param instance
	 * @param currentUserId
	 * @throws WStepWorkException
	 */
	public void update(WStepWorkAssignment instance, Integer currentUserId) 
			throws WStepWorkException {
		logger.debug(">>> update WStepWorkAssignment  id:"+(instance!=null && instance.getId()!=null?instance.getId():"null"));
		
		// note: add basic transactional control to grant insert in 2 tables (w_step_work & managed table if exists)
		// or rollback it ...
		Transaction tx = null;
		try {

			Session session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			session.update(instance);
//			HibernateUtil.update(swco);

			tx.commit();
			
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess= "HibernateException: update - WStepWorkAssignment with id:"
							+(instance!=null && instance.getId()!=null?instance.getId():"null")+" "
							+ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.error( mess );
			throw new WStepWorkException( mess );
			
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess= "Exception: update - WStepWorkAssignment with id:"
					+(instance!=null && instance.getId()!=null?instance.getId():"null")+" "
					+ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.error( mess );
			throw new WStepWorkException( mess );
		}
		

	}

	/**
	 * 
	 * @param stepWorkId
	 * @param currentUser
	 * @return
	 * @throws WStepWorkException
	 */
	@SuppressWarnings("unchecked")
	public List<WStepWorkAssignment> getByWStepWorkId(
			Integer stepWorkId, Integer currentUser ) 
					throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepWorkAssignment> wswaList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			wswaList = session
				.createQuery("From WStepWorkAssignment where id= :stepWorkId ")
				.setParameter("stepWorkId", stepWorkId)
				.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="HibernateException can't retrieve WStepWorkAssignment list...." +
							ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(message );
			throw new WStepWorkException(message);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
				String message="HibernateException can't retrieve WStepWorkAssignment list...." +
							ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
							+ex.getClass();
			logger.warn(message );
			throw new WStepWorkException(message);
		}

		return wswaList;
	}
	
}