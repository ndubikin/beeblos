package org.beeblos.bpm.core.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessStatusException;
import org.beeblos.bpm.core.model.WProcessStatus;
import com.sp.common.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;


public class WProcessStatusDao {
	
	private static final Log logger = LogFactory.getLog(WProcessStatusDao.class.getName());
	
	public WProcessStatusDao (){
		
	}
	
	public Integer add(WProcessStatus status) throws WProcessStatusException {
		
		logger.debug("add() WProcessStatus - Name: ["+status.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.save(status));

		} catch (HibernateException ex) {
			logger.error("WProcessStatusDao: add - Can't store process status definition record "+ 
					status.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessStatusException("WProcessStatusDao: add - Can't store process status definition record "+ 
					status.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WProcessStatus status) throws WProcessStatusException {
		
		logger.debug("update() WProcessStatus < id = "+status.getId()+">");
		
		try {

			HibernateUtil.update(status);


		} catch (HibernateException ex) {
			logger.error("WProcessStatusDao: update - Can't update process definition record "+ 
					status.getName()  +
					" - id = "+status.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WProcessStatusException("WProcessStatusDao: update - Can't update process definition record "+ 
					status.getName()  +
					" - id = "+status.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WProcessStatus status) throws WProcessStatusException {

		logger.debug("delete() WProcessStatus - Name: ["+status.getName()+"]");
		
		try {

			HibernateUtil.delete(status);

		} catch (HibernateException ex) {
			logger.error("WProcessStatusDao: delete - Can't delete proccess definition record "+ status.getName() +
					" <id = "+status.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessStatusException("WProcessStatusDao:  delete - Can't delete proccess definition record  "+ status.getName() +
					" <id = "+status.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} 

	}

	public WProcessStatus getWProcessStatusByPK(Integer id) throws WProcessStatusException {

		WProcessStatus process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			process = (WProcessStatus) session.get(WProcessStatus.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessStatusDao: getWProcessStatusByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessStatusException("WProcessStatusDao: getWProcessStatusByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}
	
	
	public WProcessStatus getWProcessStatusByName(String name) throws WProcessStatusException {

		WProcessStatus  process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			process = (WProcessStatus) session.createCriteria(WProcessStatus.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessStatusDao: getWProcessStatusByName - can't obtain process name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessStatusException("getWProcessStatusByName;  can't obtain process name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}

	
	public List<WProcessStatus> getWProcessStatusList() throws WProcessStatusException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WProcessStatus> processs = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processs = session.createQuery("From WProcessStatus order by id ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessStatusDao: getWProcessStatuss() - can't obtain processStatus list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessStatusException("WProcessStatusDao: getWProcessStatuss() - can't obtain processStatus list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return processs;
	}


}
	