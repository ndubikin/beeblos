package org.beeblos.bpm.core.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WStepRoleException;
import org.beeblos.bpm.core.model.WStepRole;
import org.hibernate.HibernateException;

import com.sp.hb4util.core.util.HibernateUtil;

/**
 * Step role dao.
 * 
 * @author dml 20141031
 *
 */
public class WStepRoleDao {

	private static final Log logger = LogFactory.getLog(WStepRoleDao.class.getName());

	public WStepRoleDao() {

	}

	public Integer add(WStepRole stepRole) throws WStepRoleException {

		logger.debug("add() WStepRole - step/role: [" 
				+ ((stepRole!=null)?stepRole.getIdStep():"null") + ", " 
				+ ((stepRole!=null&&stepRole.getRole()!=null)?stepRole.getRole().getId():"null") + "]");

		try {

			return Integer.valueOf(HibernateUtil.save(stepRole));

		} catch (HibernateException ex) {
			String mess = "HibernateException: add - Can't add stepRole definition "
					+ " id step/role: " 
					+ ((stepRole!=null)?stepRole.getIdStep():"null") + ", " 
					+ ((stepRole!=null&&stepRole.getRole()!=null)?stepRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WStepRoleException(mess);

		} catch (Exception ex) {
			String mess = "Exception: add - Can't add stepRole definition "
					+ " id step/role: " 
					+ ((stepRole!=null)?stepRole.getIdStep():"null") + ", " 
					+ ((stepRole!=null&&stepRole.getRole()!=null)?stepRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WStepRoleException(mess);

		}

	}

	public void update(WStepRole stepRole) throws WStepRoleException {

		logger.debug("update() WStepRole - step/role: [" 
				+ ((stepRole!=null)?stepRole.getIdStep():"null") + ", " 
				+ ((stepRole!=null&&stepRole.getRole()!=null)?stepRole.getRole().getId():"null") + "]");

		try {

			HibernateUtil.update(stepRole);

		} catch (HibernateException ex) {
			String mess = "HibernateException: update - Can't update stepRole definition "
					+ " id step/role: " 
					+ ((stepRole!=null)?stepRole.getIdStep():"null") + ", " 
					+ ((stepRole!=null&&stepRole.getRole()!=null)?stepRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WStepRoleException(mess);

		} catch (Exception ex) {
			String mess = "Exception: update - Can't update stepRole definition "
					+ " id step/role: " 
					+ ((stepRole!=null)?stepRole.getIdStep():"null") + ", " 
					+ ((stepRole!=null&&stepRole.getRole()!=null)?stepRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WStepRoleException(mess);

		}

	}

	public void delete(WStepRole stepRole) throws WStepRoleException {

		logger.debug("delete() WStepRole - step/role: [" 
				+ ((stepRole!=null)?stepRole.getIdStep():"null") + ", " 
				+ ((stepRole!=null&&stepRole.getRole()!=null)?stepRole.getRole().getId():"null") + "]");

		try {

			stepRole = getStepRoleByPK(stepRole.getId());

			HibernateUtil.delete(stepRole);

		} catch (HibernateException ex) {
			String mess = "HibernateException: delete - Can't delete stepRole definition "
					+ " id step/role: " 
					+ ((stepRole!=null)?stepRole.getIdStep():"null") + ", " 
					+ ((stepRole!=null&&stepRole.getRole()!=null)?stepRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WStepRoleException(mess);

		} catch (Exception ex) {
			String mess = "Exception: delete - Can't delete stepRole definition "
					+ " id step/role: " 
					+ ((stepRole!=null)?stepRole.getIdStep():"null") + ", " 
					+ ((stepRole!=null)?stepRole.getRole().getId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WStepRoleException(mess);

		}

	}

	public WStepRole getStepRoleByPK(Integer id) throws WStepRoleException {

		WStepRole stepRole = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			stepRole = (WStepRole) session.get(WStepRole.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getWStepRoleByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WStepRoleException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getWStepRoleByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"")+ " "
					+ ex.getClass();
			logger.warn(mess);
			throw new WStepRoleException(mess);			

		}

		return stepRole;
	}

	public boolean existsStepRole(Integer idStep, Integer idRole)throws WStepRoleException {

		Integer id = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			id = (Integer) session
						.createQuery("Select id From WStepRole Where idStep = :idStep And role.id = :idRole ")
						.setInteger("idStep", idStep)
						.setInteger("idRole", idRole)
						.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
			    tx.rollback(); 
			String mess = "HibernateException: existsStepRole - Can't check if the step role with ids step/role = " 
					+ ((idStep != null)?idStep:"") + ", "
					+ ((idRole != null)?idRole:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"");
			logger.error(mess);
			throw new WStepRoleException(mess);
		} catch (Exception ex){
			if (tx != null)
			    tx.rollback(); 
			String mess = "Exception: existsStepRole - Can't check if the step role with ids step/role = " 
					+ ((idStep != null)?idStep:"") + ", "
					+ ((idRole != null)?idRole:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"") + " " + ex.getClass();
			logger.error(mess);
			throw new WStepRoleException(mess);
		}

		if (id == null){
			return false;
		}
		return true;
	}
	

}