package org.beeblos.security.st.dao;

// Generado 25-nov-2010 12:52:05 con Hibernate Tools 3.3.0.GA

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.beeblos.security.st.error.WUserEmailAccountsException;
import org.beeblos.security.st.model.WUserEmailAccounts;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

/**
 * Dao object for domain model class WUserEmailAccounts.
 * 
 * @see org.beeblos.security.st.model.bdc.model.WUserEmailAccounts
 */

public class WUserEmailAccountsDao {

	private static final Log logger = LogFactory
			.getLog(WUserEmailAccountsDao.class);

	public WUserEmailAccountsDao() {

	}

	public Integer add(WUserEmailAccounts instance)
			throws WUserEmailAccountsException {
		logger.debug("add() name: [" + instance.getName() + "]");
		logger.info("add() name: [" + instance.getName() + "]");
		try {
			return Integer.valueOf(HibernateUtil.guardar(instance));
		} catch (HibernateException ex) {
			logger.error("WUserEmailAccountsDao: add - Cannot save WUserEmailAccounts "
					+ instance.getName());
			throw new WUserEmailAccountsException(ex);
		}
	}

	public void update(WUserEmailAccounts instance)
			throws WUserEmailAccountsException {

		logger.debug("update() WUserEmailAccounts < id = " + instance.getId()
				+ ">");

		try {

			HibernateUtil.actualizar(instance);

			logger.info("update() WUserEmailAccounts < id = "
					+ instance.getId() + ">");

		} catch (HibernateException ex) {
			logger.error("WUserEmailAccountsDao:update - Cannot update WUserEmailAccounts "
					+ instance.getName()
					+ " - id = "
					+ instance.getId()
					+ "\n - " + ex.getMessage());
			throw new WUserEmailAccountsException(ex);

		}

	}

	public void delete(WUserEmailAccounts instance)
			throws WUserEmailAccountsException {

		logger.debug("delete() name: [" + instance.getName() + "]");

		try {

			instance = getWUserEmailAccountsByPK(instance.getId());

			HibernateUtil.borrar(instance);

			logger.info("delete() WUserEmailAccounts < id = "
					+ instance.getId() + ">");

		} catch (HibernateException ex) {
			logger.error("WUserEmailAccountsDao: delete - It is not possible to delete the instance "
					+ instance.getName()
					+ " <id = "
					+ instance.getId()
					+ "> \n" + " - " + ex.getMessage());
			throw new WUserEmailAccountsException(ex);

		} catch (WUserEmailAccountsException ex1) {
			logger.error("WUserEmailAccountsDao: delete - The instance "
					+ instance.getName() + " <id = " + instance.getId()
					+ "> does not exist \n" + " - " + ex1.getMessage());
			throw new WUserEmailAccountsException(ex1);

		}

	}

	public WUserEmailAccounts getWUserEmailAccountsByPK(Integer i)
			throws WUserEmailAccountsException {

		WUserEmailAccounts instance = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			instance = (WUserEmailAccounts) session.get(
					WUserEmailAccounts.class, i);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WUserEmailAccountsDao: getWUserEmailAccountsByPK - There are not any WUserEmailAccounts with id = "
					+ i + "]  almacenado - \n" + ex.getMessage());
			throw new WUserEmailAccountsException("The WUserEmailAccounts: "
					+ i + " - " + ex.getMessage() + "does not exist.");

		}

		return instance;
	}

	public WUserEmailAccounts getWUserEmailAccountsByName(String instanceName)
			throws WUserEmailAccountsException {

		WUserEmailAccounts instance = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instance = (WUserEmailAccounts) session
					.createCriteria(WUserEmailAccounts.class)
					.add(Restrictions.naturalId().set("name", instanceName))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WUserEmailAccountsDao: getWUserEmailAccountsByName - The WUserEmailAccounts with name: "
					+ instanceName
					+ " - "
					+ ex.getMessage()
					+ " does not exist");
			throw new WUserEmailAccountsException(
					"The WUserEmailAccounts with name: " + instanceName + " - "
							+ ex.getMessage() + " does not exist");

		}

		return instance;
	}

	public List<WUserEmailAccounts> getWUserEmailAccountsList()
			throws WUserEmailAccountsException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WUserEmailAccounts> instances = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instances = session.createQuery(
					"From WUserEmailAccounts order by id").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WUserEmailAccountsDao: getWUserEmailAccountsList() - It was not possible to get the WUserEmailAccounts list - "
					+ ex.getMessage());

			throw new WUserEmailAccountsException(
					"Error getting the WUserEmailAccountsList: "
							+ ex.getMessage());

		}

		return instances;
	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getWUserEmailAccountsComboList(
			Integer idSpecificUser, String firstLineText, String blank)
			throws WUserEmailAccountsException {

		List<WUserEmailAccounts> wueal = null;
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
								"From WUserEmailAccounts wuea WHERE wuea.wUserDef.id= ? order by  wuea.userDefaultAccount desc, wuea.email ")
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

				for (WUserEmailAccounts wuea : wueal) {
					result.add(new StringPair(wuea.getId(), wuea.getName()
							+ " <" + wuea.getEmail() + "> "));
				}

			} else {
				result = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message = "An error has ocurred getting the list of WUserEmailAccounts for Combo List"
					+ " - " + ex.getMessage() + " ; " + ex.getCause();
			logger.error(message);
			throw new WUserEmailAccountsException(message);
		} catch (Exception e) {
		}

		return result;

	}

	// martin - 20101214
	@SuppressWarnings("unchecked")
	public List<WUserEmailAccounts> getWUserEmailAccountsListByUser(
			Integer idSpecificUser) throws WUserEmailAccountsException {

		List<WUserEmailAccounts> result = null;
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
								"From WUserEmailAccounts wuea WHERE wuea.wUserDef.id= ? order by  wuea.userDefaultAccount desc, wuea.email ")
						.setInteger(0, idSpecificUser).list();

			}
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message = "Ocurrio un error al intentar obtain the list of WUserEmailAccounts para combo"
					+ " - " + ex.getMessage() + " ; " + ex.getCause();
			logger.error(message);
			throw new WUserEmailAccountsException(message);
		} catch (Exception e) {
		}

		return result;

	}

	public boolean duplicatedNameVerification(String instanceName, Integer id)
			throws WUserEmailAccountsException {

		WUserEmailAccounts instance = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instance = (WUserEmailAccounts) session
					.createQuery(
							"From WUserEmailAccounts WHERE  name= ? AND id != ?")
					.setString(0, instanceName).setInteger(1, id)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WUserEmailAccountsDao: duplicatedNameVerification - Cannot load un WUserEmailAccounts "
					+ instanceName + " - " + ex.getMessage());
			throw new WUserEmailAccountsException(
					"Cannot obtain the WUserEmailAccounts: " + instanceName
							+ " - " + ex.getMessage() + " ; " + ex.getCause());

		}
		if (instance != null)
			return true;
		else
			return false;
	}

	public WUserEmailAccounts getDefaultAccount(Integer idUser)
			throws WUserEmailAccountsException {

		WUserEmailAccounts result = null;
		// List<StringPair> result = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			if (idUser != null) {

				result = (WUserEmailAccounts) session
						.createQuery(
								"From WUserEmailAccounts wuea WHERE wuea.wUserDef.id= ? AND wuea.userDefaultAccount = true order by  wuea.userDefaultAccount desc, wuea.email asc")
						.setInteger(0, idUser).uniqueResult();

			}
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message = "An error has ocurred while obtaining the WUserEmailAccounts Combo list"
					+ " - " + ex.getMessage() + " ; " + ex.getCause();
			logger.error(message);
			throw new WUserEmailAccountsException(message);
		} catch (Exception e) {
		}

		return result;

	}

}
