package org.beeblos.bpm.core.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessRoleException;
import org.beeblos.bpm.core.model.WProcessRole;
import org.hibernate.HibernateException;

import com.sp.common.util.HibernateUtil;

/**
 * Process role dao.
 * 
 * @author dml 20141031
 *
 */
public class WProcessRoleDao {

	private static final Log logger = LogFactory.getLog(WProcessRoleDao.class.getName());

	public WProcessRoleDao() {

	}

	public Integer add(WProcessRole processRole) throws WProcessRoleException {

		logger.debug("add() WProcessRole - process/role: [" 
				+ ((processRole!=null)?processRole.getIdProcess():"null") + ", " 
				+ ((processRole!=null&&processRole.getRole()!=null)?processRole.getRole().getId():"null") + "]");

		try {

			return Integer.valueOf(HibernateUtil.save(processRole));

		} catch (HibernateException ex) {
			String mess = "HibernateException: add - Can't add processRole definition "
					+ " id process/role: " 
					+ ((processRole!=null)?processRole.getIdProcess():"null") + ", " 
					+ ((processRole!=null&&processRole.getRole()!=null)?processRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WProcessRoleException(mess);

		} catch (Exception ex) {
			String mess = "Exception: add - Can't add processRole definition "
					+ " id process/role: " 
					+ ((processRole!=null)?processRole.getIdProcess():"null") + ", " 
					+ ((processRole!=null&&processRole.getRole()!=null)?processRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WProcessRoleException(mess);

		}

	}

	public void update(WProcessRole processRole) throws WProcessRoleException {

		logger.debug("update() WProcessRole - process/role: [" 
				+ ((processRole!=null)?processRole.getIdProcess():"null") + ", " 
				+ ((processRole!=null&&processRole.getRole()!=null)?processRole.getRole().getId():"null") + "]");

		try {

			HibernateUtil.update(processRole);

		} catch (HibernateException ex) {
			String mess = "HibernateException: update - Can't update processRole definition "
					+ " id process/role: " 
					+ ((processRole!=null)?processRole.getIdProcess():"null") + ", " 
					+ ((processRole!=null&&processRole.getRole()!=null)?processRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WProcessRoleException(mess);

		} catch (Exception ex) {
			String mess = "Exception: update - Can't update processRole definition "
					+ " id process/role: " 
					+ ((processRole!=null)?processRole.getIdProcess():"null") + ", " 
					+ ((processRole!=null&&processRole.getRole()!=null)?processRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WProcessRoleException(mess);

		}

	}

	public void delete(WProcessRole processRole) throws WProcessRoleException {

		logger.debug("delete() WProcessRole - process/role: [" 
				+ ((processRole!=null)?processRole.getIdProcess():"null") + ", " 
				+ ((processRole!=null&&processRole.getRole()!=null)?processRole.getRole().getId():"null") + "]");

		try {

			processRole = getProcessRoleByPK(processRole.getId());

			HibernateUtil.delete(processRole);

		} catch (HibernateException ex) {
			String mess = "HibernateException: delete - Can't delete processRole definition "
					+ " id process/role: " 
					+ ((processRole!=null)?processRole.getIdProcess():"null") + ", " 
					+ ((processRole!=null&&processRole.getRole()!=null)?processRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WProcessRoleException(mess);

		} catch (Exception ex) {
			String mess = "Exception: delete - Can't delete processRole definition "
					+ " id process/role: " 
					+ ((processRole!=null)?processRole.getIdProcess():"null") + ", " 
					+ ((processRole!=null)?processRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WProcessRoleException(mess);

		}

	}

	public WProcessRole getProcessRoleByPK(Integer id) throws WProcessRoleException {

		WProcessRole processRole = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			processRole = (WProcessRole) session.get(WProcessRole.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getWProcessRoleByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WProcessRoleException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getWProcessRoleByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"")+ " "
					+ ex.getClass();
			logger.warn(mess);
			throw new WProcessRoleException(mess);			

		}

		return processRole;
	}

	public boolean existsProcessRole(Integer idProcess, Integer idRole)throws WProcessRoleException {

		Integer id = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			id = (Integer) session
						.createQuery("Select id From WProcessRole Where idProcess = :idProcess And role.id = :idRole ")
						.setInteger("idProcess", idProcess)
						.setInteger("idRole", idRole)
						.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
			    tx.rollback(); 
			String mess = "HibernateException: existsProcessRole - Can't check if the process role with ids process/role = " 
					+ ((idProcess != null)?idProcess:"") + ", "
					+ ((idRole != null)?idRole:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"");
			logger.error(mess);
			throw new WProcessRoleException(mess);
		} catch (Exception ex){
			if (tx != null)
			    tx.rollback(); 
			String mess = "Exception: existsProcessRole - Can't check if the process role with ids process/role = " 
					+ ((idProcess != null)?idProcess:"") + ", "
					+ ((idRole != null)?idRole:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"") + " " + ex.getClass();
			logger.error(mess);
			throw new WProcessRoleException(mess);
		}

		if (id == null){
			return false;
		}
		return true;
	}
	

}