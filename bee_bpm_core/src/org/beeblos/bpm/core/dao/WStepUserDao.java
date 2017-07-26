package org.beeblos.bpm.core.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WStepUserException;
import org.beeblos.bpm.core.model.WStepUser;
import org.hibernate.HibernateException;

import com.sp.hb4util.core.util.HibernateUtil;

/**
 * Step user relation dao.
 * 
 * @author dmuleiro dml 20141031
 * 
 *
 */
public class WStepUserDao {

	private static final Log logger = LogFactory.getLog(WStepUserDao.class.getName());

	public WStepUserDao() {

	}

	public Integer add(WStepUser stepUser) throws WStepUserException {

		logger.debug("add() WStepUser - step/user: [" 
				+ ((stepUser!=null)?stepUser.getIdStep():"null") + ", " 
				+ ((stepUser!=null&&stepUser.getUser()!=null)?stepUser.getUser().getId():"null") + "]");

		try {

			return Integer.valueOf(HibernateUtil.save(stepUser));

		} catch (HibernateException ex) {
			String mess = "HibernateException: add - Can't add stepUser definition "
					+ " id step/user: " 
					+ ((stepUser!=null)?stepUser.getIdStep():"null") + ", " 
					+ ((stepUser!=null&&stepUser.getUser()!=null)?stepUser.getUser().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WStepUserException(mess);

		} catch (Exception ex) {
			String mess = "Exception: add - Can't add stepUser definition "
					+ " id step/user: " 
					+ ((stepUser!=null)?stepUser.getIdStep():"null") + ", " 
					+ ((stepUser!=null&&stepUser.getUser()!=null)?stepUser.getUser().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WStepUserException(mess);

		}

	}

	public void update(WStepUser stepUser) throws WStepUserException {

		logger.debug("update() WStepUser - step/user: [" 
				+ ((stepUser!=null)?stepUser.getIdStep():"null") + ", " 
				+ ((stepUser!=null&&stepUser.getUser()!=null)?stepUser.getUser().getId():"null") + "]");

		try {

			HibernateUtil.update(stepUser);

		} catch (HibernateException ex) {
			String mess = "HibernateException: update - Can't update stepUser definition "
					+ " id step/user: " 
					+ ((stepUser!=null)?stepUser.getIdStep():"null") + ", " 
					+ ((stepUser!=null&&stepUser.getUser()!=null)?stepUser.getUser().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WStepUserException(mess);

		} catch (Exception ex) {
			String mess = "Exception: update - Can't update stepUser definition "
					+ " id step/user: " 
					+ ((stepUser!=null)?stepUser.getIdStep():"null") + ", " 
					+ ((stepUser!=null&&stepUser.getUser()!=null)?stepUser.getUser().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WStepUserException(mess);

		}

	}

	public void delete(WStepUser stepUser) throws WStepUserException {

		logger.debug("delete() WStepUser - step/user: [" 
				+ ((stepUser!=null)?stepUser.getIdStep():"null") + ", " 
				+ ((stepUser!=null&&stepUser.getUser()!=null)?stepUser.getUser().getId():"null") + "]");

		try {

			stepUser = getStepUserByPK(stepUser.getId());

			HibernateUtil.delete(stepUser);

		} catch (HibernateException ex) {
			String mess = "HibernateException: delete - Can't delete stepUser definition "
					+ " id step/user: " 
					+ ((stepUser!=null)?stepUser.getIdStep():"null") + ", " 
					+ ((stepUser!=null&&stepUser.getUser()!=null)?stepUser.getUser().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WStepUserException(mess);

		} catch (Exception ex) {
			String mess = "Exception: delete - Can't delete stepUser definition "
					+ " id step/user: " 
					+ ((stepUser!=null)?stepUser.getIdStep():"null") + ", " 
					+ ((stepUser!=null)?stepUser.getUser().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WStepUserException(mess);

		}

	}

	public WStepUser getStepUserByPK(Integer id) throws WStepUserException {

		WStepUser stepUser = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			stepUser = (WStepUser) session.get(WStepUser.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getWStepUserByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WStepUserException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getWStepUserByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"")+ " "
					+ ex.getClass();
			logger.warn(mess);
			throw new WStepUserException(mess);			

		}

		return stepUser;
	}

	public boolean existsStepUser(Integer idStep, Integer idUser)throws WStepUserException {

		Integer id = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			id = (Integer) session
						.createQuery("Select id From WStepUser Where idStep = :idStep And user.id = :idUser ")
						.setInteger("idStep", idStep)
						.setInteger("idUser", idUser)
						.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
			    tx.rollback(); 
			String mess = "HibernateException: existsStepUser - Can't check if the step user with ids step/user = " 
					+ ((idStep != null)?idStep:"") + ", "
					+ ((idUser != null)?idUser:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"");
			logger.error(mess);
			throw new WStepUserException(mess);
		} catch (Exception ex){
			if (tx != null)
			    tx.rollback(); 
			String mess = "Exception: existsStepUser - Can't check if the step user with ids step/user = " 
					+ ((idStep != null)?idStep:"") + ", "
					+ ((idUser != null)?idUser:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"") + " " + ex.getClass();
			logger.error(mess);
			throw new WStepUserException(mess);
		}

		if (id == null){
			return false;
		}
		return true;
	}
	

}