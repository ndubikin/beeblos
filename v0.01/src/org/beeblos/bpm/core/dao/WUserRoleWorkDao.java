package org.beeblos.bpm.core.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WUserRoleWorkException;
import org.beeblos.bpm.core.model.WUserRoleWork;
import org.hibernate.HibernateException;

import com.sp.common.util.HibernateUtil;


/**
 * DAO for runtime roles mgmt
 * 
 * 
 * @author nes 20141215
 *
 */
public class WUserRoleWorkDao {
	
	private static final Log logger = LogFactory.getLog(WUserRoleWorkDao.class.getName());
	
	public WUserRoleWorkDao (){
		super();
	}
	
	public Integer add(WUserRoleWork userRoleWork) throws WUserRoleWorkException {
		logger.debug("add() WUserRoleWork - user/role/processWork: [" 
				+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
				+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
				+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null") + "]");

		try {

			return Integer.valueOf(HibernateUtil.save(userRoleWork));

		} catch (HibernateException ex) {
			String mess = "HibernateException: add - Can't add userRoleWork definition "
					+ " id user/role/processWork: " 
					+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
					+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
					+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WUserRoleWorkException(mess);

		} catch (Exception ex) {
			String mess = "Exception: add - Can't add userRoleWork definition "
					+ " id user/role/processWork: " 
					+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
					+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
					+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WUserRoleWorkException(mess);

		}

	}

	/**
	 * no tendría mucho sentido hacer un update sobre un WURPW ... solo altas o bajas ...
	 * @param userRoleWork
	 * @throws WUserRoleWorkException
	 */
	@Deprecated
	public void update(WUserRoleWork userRoleWork) throws WUserRoleWorkException {
		// por eso dejo este logger info ....
		logger.info("update() WUserRoleWork - user/role/processWork: [" 
				+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
				+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
				+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null") + "]");

		try {

			HibernateUtil.update(userRoleWork);

		} catch (HibernateException ex) {
			String mess = "HibernateException: update - Can't update userRoleWork definition "
					+ " user/role/processWork: [" 
				+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
				+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
				+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WUserRoleWorkException(mess);

		} catch (Exception ex) {
			String mess = "Exception: update - Can't update userRoleWork definition "
					+ " user/role/processWork: [" 
				+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
				+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
				+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null")
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WUserRoleWorkException(mess);

		}

	}

	public void delete(WUserRoleWork userRoleWork) throws WUserRoleWorkException {

		logger.debug("delete() WUserRoleWork - user/role/processWork: [" 
				+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
				+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
				+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null") + "]");

		try {

			userRoleWork = getUserRoleWorkByPK(userRoleWork.getId());

			HibernateUtil.delete(userRoleWork);

		} catch (HibernateException ex) {
			String mess = "HibernateException: delete - Can't delete userRoleWork definition "
					+ " user/role/processWork: [" 
				+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
				+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
				+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null")
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WUserRoleWorkException(mess);

		} catch (Exception ex) {
			String mess = "Exception: delete - Can't delete userRoleWork definition "
					+ " user/role/processWork: [" 
				+ ((userRoleWork!=null&&userRoleWork.getIdUser()!=null)?userRoleWork.getIdUser():"null") + ", " 
				+ ((userRoleWork!=null&&userRoleWork.getIdRole()!=null)?userRoleWork.getIdRole():"null") + ", "
				+ ((userRoleWork!=null&&userRoleWork.getIdProcessWork()!=null)?userRoleWork.getIdProcessWork():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WUserRoleWorkException(mess);

		}

	}

	/**
	 * returns a w-user-role-processWork
	 * 
	 * @param id
	 * @return
	 * @throws WUserRoleWorkException
	 */
	public WUserRoleWork getUserRoleWorkByPK(Integer id) throws WUserRoleWorkException {

		WUserRoleWork userRoleWork = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			userRoleWork = (WUserRoleWork) session.get(WUserRoleWork.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getUserRoleWorkByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WUserRoleWorkException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getUserRoleWorkByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"")+ " "
					+ ex.getClass();
			logger.warn(mess);
			throw new WUserRoleWorkException(mess);			

		}

		return userRoleWork;
	}



}
	