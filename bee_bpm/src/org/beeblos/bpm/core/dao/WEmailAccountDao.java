package org.beeblos.bpm.core.dao;

// Generado 25-nov-2010 12:52:05 con Hibernate Tools 3.3.0.GA

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WEmailAccountException;
import org.beeblos.bpm.core.model.WEmailAccount;
import com.sp.common.util.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

/**
 * Dao object for domain model class WEmailAccount.
 * 
 * @see org.beeblos.WEmailAccount.st.model.bdc.model.WEmailAccount
 */

public class WEmailAccountDao {

	private static final Log logger = LogFactory
			.getLog(WEmailAccountDao.class);

	public WEmailAccountDao() {

	}

	public Integer add(WEmailAccount instance)
			throws WEmailAccountException {
		logger.debug("add() name: [" + instance.getName() + "]");
		logger.info("add() name: [" + instance.getName() + "]");
		try {
			return Integer.valueOf(HibernateUtil.save(instance));
		} catch (HibernateException ex) {
			logger.error("WEmailAccountDao: add - Cannot save WEmailAccount "
					+ instance.getName());
			throw new WEmailAccountException(ex);
		}
	}

	public void update(WEmailAccount instance)
			throws WEmailAccountException {

		logger.debug("update() WEmailAccount < id = " + instance.getId()
				+ ">");

		try {

			HibernateUtil.update(instance);

			logger.info("update() WEmailAccount < id = "
					+ instance.getId() + ">");

		} catch (HibernateException ex) {
			logger.error("WEmailAccountDao:update - Cannot update WEmailAccount "
					+ instance.getName()
					+ " - id = "
					+ instance.getId()
					+ "\n - " + ex.getMessage());
			throw new WEmailAccountException(ex);

		}

	}

	public void delete(WEmailAccount instance)
			throws WEmailAccountException {

		logger.debug("delete() name: [" + instance.getName() + "]");

		try {

			instance = getWEmailAccountByPK(instance.getId());

			HibernateUtil.delete(instance);

			logger.info("delete() WEmailAccount < id = "
					+ instance.getId() + ">");

		} catch (HibernateException ex) {
			logger.error("WEmailAccountDao: delete - It is not possible to delete the instance "
					+ instance.getName()
					+ " <id = "
					+ instance.getId()
					+ "> \n" + " - " + ex.getMessage());
			throw new WEmailAccountException(ex);

		} catch (WEmailAccountException e) {
			logger.error("WEmailAccountDao: delete - The instance "
					+ instance.getName() + " <id = " + instance.getId()
					+ "> does not exist \n" + " - " + e.getMessage());
			throw new WEmailAccountException(e);

		}

	}

	public WEmailAccount getWEmailAccountByPK(Integer i)
			throws WEmailAccountException {

		WEmailAccount instance = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			instance = (WEmailAccount) session.get(
					WEmailAccount.class, i);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WEmailAccountDao: getWEmailAccountByPK - There are not any WEmailAccount with id = "
					+ i + "]  almacenado - \n" + ex.getMessage());
			throw new WEmailAccountException("The WEmailAccount: "
					+ i + " - " + ex.getMessage() + "does not exist.");

		}

		return instance;
	}

	public WEmailAccount getWEmailAccountByName(String instanceName)
			throws WEmailAccountException {

		WEmailAccount instance = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instance = (WEmailAccount) session
					.createCriteria(WEmailAccount.class)
					.add(Restrictions.naturalId().set("name", instanceName))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WEmailAccountDao: getWEmailAccountByName - The WEmailAccount with name: "
					+ instanceName
					+ " - "
					+ ex.getMessage()
					+ " does not exist");
			throw new WEmailAccountException(
					"The WEmailAccount with name: " + instanceName + " - "
							+ ex.getMessage() + " does not exist");

		}

		return instance;
	}

	public List<WEmailAccount> getWEmailAccountList()
			throws WEmailAccountException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WEmailAccount> instances = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instances = session.createQuery(
					"From WEmailAccount order by id").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WEmailAccountDao: getWEmailAccountList() - It was not possible to get the WEmailAccount list - "
					+ ex.getMessage());

			throw new WEmailAccountException(
					"Error getting the WEmailAccountList: "
							+ ex.getMessage());

		}

		return instances;
	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getWEmailAccountComboList(
			Integer idSpecificUser, String firstLineText, String blank)
			throws WEmailAccountException {

		List<WEmailAccount> wueal = null;
		List<StringPair> result = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			if (idSpecificUser != null) {

				wueal = session
						.createQuery(
								"From WEmailAccount wuea WHERE wuea.wUserDef.id= ? order by  wuea.userDefaultAccount desc, wuea.email ")
						.setInteger(0, idSpecificUser).list();

			}
			
			tx.commit();

			if (wueal != null) {
				if (!wueal.get(0).isUserDefaultAccount() && wueal.size() > 1) {
					// inserta los extras
					if (firstLineText != null && !"".equals(firstLineText)) {
						if (!firstLineText.equals("WHITE")) {
							result.add(new StringPair(null, firstLineText));
						} else {
							result.add(new StringPair(null, " "));
						}
					}

					if (blank != null && !"".equals(blank)) {
						if (!blank.equals("WHITE")) {
							result.add(new StringPair(null, blank));
						} else {
							result.add(new StringPair(null, " "));
						}
					}

				}

				for (WEmailAccount wuea : wueal) {
					result.add(new StringPair(wuea.getId(), wuea.getName()
							+ " <" + wuea.getEmail() + "> "));
				}

			} else {
				result = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message = "An error has ocurred getting the list of WEmailAccount for Combo List"
					+ " - " + ex.getMessage() + " ; " + ex.getCause();
			logger.error(message);
			throw new WEmailAccountException(message);
		} catch (Exception e) {
		}

		return result;

	}

	// martin - 20101214
	@SuppressWarnings("unchecked")
	public List<WEmailAccount> getWEmailAccountListByUser(
			Integer idSpecificUser) throws WEmailAccountException {

		List<WEmailAccount> result = null;
		// List<StringPair> result = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			if (idSpecificUser != null) {

				result = session
						.createQuery(
								"From WEmailAccount wuea WHERE wuea.wUserDef.id= ? order by  wuea.userDefaultAccount desc, wuea.email ")
						.setInteger(0, idSpecificUser).list();

			}
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message = "Ocurrio un error al intentar obtain the list of WEmailAccount para combo"
					+ " - " + ex.getMessage() + " ; " + ex.getCause();
			logger.error(message);
			throw new WEmailAccountException(message);
		} catch (Exception e) {
		}

		return result;

	}

	public List<WEmailAccount> wEmailAccountFinder(String nameFilter, String emailFilter)
			throws WEmailAccountException {

		List<WEmailAccount> result = null;
		// List<StringPair> result = new ArrayList<StringPair>(10);

		
		String query="SELECT *  " + getSelectPhrase();
		String order = " ORDER BY wuea.name ";
		String filter = "";
		if (nameFilter != null && !"".equals(nameFilter)) {
			filter = "WHERE wuea.name LIKE '%"+nameFilter+"%' " ;
		}
		if (emailFilter != null && !"".equals(emailFilter)) {
			if ("".equals(filter)) {
				filter += " WHERE ";
			} else {
				filter += " OR ";
			}
			filter += " wuea.email LIKE '%"+emailFilter+"%' " ;
		}

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		org.hibernate.Query q = null;
		
		query += filter + order;
		
		System.out.println("QUERY: "+query);

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			q = session.createSQLQuery(query).addEntity("WEmailAccount", WEmailAccount.class);
			
			result = q.list();
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WEmailAccountDao: wEmailAccountFinder - Cannot get the finder list: "
					+ nameFilter
					+ " - "
					+ ex.getMessage()
					+ " does not exist");
			throw new WEmailAccountException(
					"The WEmailAccount with name: " + nameFilter + " - "
							+ ex.getMessage() + " does not exist");

		}

		return result;
	}

	private String getSelectPhrase() {
		String query="FROM w_user_email_accounts wuea ";
		return query;
	}
	
	public boolean duplicatedNameVerification(String instanceName, Integer id)
			throws WEmailAccountException {

		WEmailAccount instance = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instance = (WEmailAccount) session
					.createQuery(
							"From WEmailAccount WHERE  name= ? AND id != ?")
					.setString(0, instanceName).setInteger(1, id)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WEmailAccountDao: duplicatedNameVerification - Cannot load un WEmailAccount "
					+ instanceName + " - " + ex.getMessage());
			throw new WEmailAccountException(
					"Cannot obtain the WEmailAccount: " + instanceName
							+ " - " + ex.getMessage() + " ; " + ex.getCause());

		}
		if (instance != null)
			return true;
		else
			return false;
	}

	public WEmailAccount getDefaultAccount(Integer idUser)
			throws WEmailAccountException {

		WEmailAccount result = null;
		// List<StringPair> result = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			if (idUser != null) {

				result = (WEmailAccount) session
						.createQuery(
								"From WEmailAccount wuea WHERE wuea.wUserDef.id= ? AND wuea.userDefaultAccount = true order by  wuea.userDefaultAccount desc, wuea.email asc")
						.setInteger(0, idUser).uniqueResult();

			}
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message = "An error has ocurred while obtaining the WEmailAccount Combo list"
					+ " - " + ex.getMessage() + " ; " + ex.getCause();
			logger.error(message);
			throw new WEmailAccountException(message);
		} catch (Exception e) {
		}

		return result;

	}
	
	public List<WEmailAccount> getDefaultAccountList(Integer idUser)
			throws WEmailAccountException {

		List<WEmailAccount> result = null;
		// List<StringPair> result = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			if (idUser != null) {

				result = (List<WEmailAccount>) session
						.createQuery(
								"From WEmailAccount wuea WHERE wuea.wUserDef.id= ? AND wuea.userDefaultAccount = true order by  wuea.userDefaultAccount desc, wuea.email asc")
						.setInteger(0, idUser).list();

			}
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message = "An error has ocurred while obtaining the WEmailAccount Combo list"
					+ " - " + ex.getMessage() + " ; " + ex.getCause();
			logger.error(message);
			throw new WEmailAccountException(message);
		} catch (Exception e) {
		}

		return result;

	}	

}
