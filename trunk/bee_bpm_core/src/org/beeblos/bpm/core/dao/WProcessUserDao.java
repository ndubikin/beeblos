package org.beeblos.bpm.core.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessUserException;
import org.beeblos.bpm.core.model.WProcessUser;
import org.hibernate.HibernateException;

import com.sp.hb4util.core.util.HibernateUtil;

/**
 * Process user relation dao.
 * 
 * @author dmuleiro dml 20141031
 * 
 *
 */
public class WProcessUserDao {

	private static final Log logger = LogFactory.getLog(WProcessUserDao.class.getName());

	public WProcessUserDao() {

	}

	public Integer add(WProcessUser processUser) throws WProcessUserException {

		logger.debug("add() WProcessUser - process/user: [" 
				+ ((processUser!=null)?processUser.getIdProcess():"null") + ", " 
				+ ((processUser!=null&&processUser.getUser()!=null)?processUser.getUser().getId():"null") + "]");

		try {

			return Integer.valueOf(HibernateUtil.save(processUser));

		} catch (HibernateException ex) {
			String mess = "HibernateException: add - Can't add processUser definition "
					+ " id process/user: " 
					+ ((processUser!=null)?processUser.getIdProcess():"null") + ", " 
					+ ((processUser!=null&&processUser.getUser()!=null)?processUser.getUser().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WProcessUserException(mess);

		} catch (Exception ex) {
			String mess = "Exception: add - Can't add processUser definition "
					+ " id process/user: " 
					+ ((processUser!=null)?processUser.getIdProcess():"null") + ", " 
					+ ((processUser!=null&&processUser.getUser()!=null)?processUser.getUser().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WProcessUserException(mess);

		}

	}

	public void update(WProcessUser processUser) throws WProcessUserException {

		logger.debug("update() WProcessUser - process/user: [" 
				+ ((processUser!=null)?processUser.getIdProcess():"null") + ", " 
				+ ((processUser!=null&&processUser.getUser()!=null)?processUser.getUser().getId():"null") + "]");

		try {

			HibernateUtil.update(processUser);

		} catch (HibernateException ex) {
			String mess = "HibernateException: update - Can't update processUser definition "
					+ " id process/user: " 
					+ ((processUser!=null)?processUser.getIdProcess():"null") + ", " 
					+ ((processUser!=null&&processUser.getUser()!=null)?processUser.getUser().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WProcessUserException(mess);

		} catch (Exception ex) {
			String mess = "Exception: update - Can't update processUser definition "
					+ " id process/user: " 
					+ ((processUser!=null)?processUser.getIdProcess():"null") + ", " 
					+ ((processUser!=null&&processUser.getUser()!=null)?processUser.getUser().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WProcessUserException(mess);

		}

	}

	public void delete(WProcessUser processUser) throws WProcessUserException {

		logger.debug("delete() WProcessUser - process/user: [" 
				+ ((processUser!=null)?processUser.getIdProcess():"null") + ", " 
				+ ((processUser!=null&&processUser.getUser()!=null)?processUser.getUser().getId():"null") + "]");

		try {

			processUser = getProcessUserByPK(processUser.getId());

			HibernateUtil.delete(processUser);

		} catch (HibernateException ex) {
			String mess = "HibernateException: delete - Can't delete processUser definition "
					+ " id process/user: " 
					+ ((processUser!=null)?processUser.getIdProcess():"null") + ", " 
					+ ((processUser!=null&&processUser.getUser()!=null)?processUser.getUser().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WProcessUserException(mess);

		} catch (Exception ex) {
			String mess = "Exception: delete - Can't delete processUser definition "
					+ " id process/user: " 
					+ ((processUser!=null)?processUser.getIdProcess():"null") + ", " 
					+ ((processUser!=null)?processUser.getUser().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WProcessUserException(mess);

		}

	}

	public WProcessUser getProcessUserByPK(Integer id) throws WProcessUserException {

		WProcessUser processUser = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			processUser = (WProcessUser) session.get(WProcessUser.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getWProcessUserByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WProcessUserException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getWProcessUserByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"")+ " "
					+ ex.getClass();
			logger.warn(mess);
			throw new WProcessUserException(mess);			

		}

		return processUser;
	}

	public boolean existsProcessUser(Integer idProcess, Integer idUser)throws WProcessUserException {

		Integer id = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			id = (Integer) session
						.createQuery("Select id From WProcessUser Where idProcess = :idProcess And user.id = :idUser ")
						.setInteger("idProcess", idProcess)
						.setInteger("idUser", idUser)
						.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
			    tx.rollback(); 
			String mess = "HibernateException: existsProcessUser - Can't check if the process user with ids process/user = " 
					+ ((idProcess != null)?idProcess:"") + ", "
					+ ((idUser != null)?idUser:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"");
			logger.error(mess);
			throw new WProcessUserException(mess);
		} catch (Exception ex){
			if (tx != null)
			    tx.rollback(); 
			String mess = "Exception: existsProcessUser - Can't check if the process user with ids process/user = " 
					+ ((idProcess != null)?idProcess:"") + ", "
					+ ((idUser != null)?idUser:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"") + " " + ex.getClass();
			logger.error(mess);
			throw new WProcessUserException(mess);
		}

		if (id == null){
			return false;
		}
		return true;
	}
	

}