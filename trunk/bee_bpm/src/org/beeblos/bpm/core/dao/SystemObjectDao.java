package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.SystemObjectException;
import org.beeblos.bpm.core.model.SystemObject;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

public class SystemObjectDao {

	private static final Log logger = LogFactory.getLog(SystemObjectDao.class.getName());

	public SystemObjectDao() {

	}

	public Integer add(SystemObject systemObject) throws SystemObjectException {

		logger.debug("add() SystemObject - Name: [" + systemObject.getName() + "]");

		try {

			return Integer.valueOf(HibernateUtil.guardar(systemObject));

		} catch (HibernateException ex) {
			logger.error("SystemObjectDao: add - Can't store systemObject definition record "
					+ systemObject.getName() + " - " + ex.getMessage() + "\n" + ex.getCause());
			throw new SystemObjectException(
					"SystemObjectDao: add - Can't store systemObject definition record "
							+ systemObject.getName() + " - " + ex.getMessage() + "\n"
							+ ex.getCause());

		}

	}

	public void update(SystemObject systemObject) throws SystemObjectException {

		logger.debug("update() SystemObject < id = " + systemObject.getId() + ">");

		try {

			HibernateUtil.actualizar(systemObject);

		} catch (HibernateException ex) {
			logger.error("SystemObjectDao: update - Can't update systemObject definition record "
					+ systemObject.getName() + " - id = " + systemObject.getId() + "\n - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new SystemObjectException(
					"SystemObjectDao: update - Can't update systemObject definition record "
							+ systemObject.getName() + " - id = " + systemObject.getId() + "\n - "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

	}

	public void delete(SystemObject systemObject) throws SystemObjectException {

		logger.debug("delete() SystemObject - Name: [" + systemObject.getName() + "]");

		try {

			systemObject = getSystemObjectByPK(systemObject.getId());

			HibernateUtil.borrar(systemObject);

		} catch (HibernateException ex) {
			logger.error("SystemObjectDao: delete - Can't delete proccess definition record "
					+ systemObject.getName() + " <id = " + systemObject.getId() + "> \n" + " - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new SystemObjectException(
					"SystemObjectDao:  delete - Can't delete proccess definition record  "
							+ systemObject.getName() + " <id = " + systemObject.getId() + "> \n"
							+ " - " + ex.getMessage() + "\n" + ex.getCause());

		} catch (SystemObjectException ex1) {
			logger.error("SystemObjectDao: delete - Exception in deleting systemObject rec "
					+ systemObject.getName() + " <id = " + systemObject.getId()
					+ "> not stored \n" + " - " + ex1.getMessage() + "\n" + ex1.getCause());
			throw new SystemObjectException(
					"SystemObjectDao: delete - Exception in deleting systemObject rec "
							+ systemObject.getName() + " <id = " + systemObject.getId()
							+ "> not stored \n" + " - " + ex1.getMessage() + "\n" + ex1.getCause());

		}

	}

	public SystemObject getSystemObjectByPK(Integer id) throws SystemObjectException {

		SystemObject systemObject = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			systemObject = (SystemObject) session.get(SystemObject.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("SystemObjectDao: getSystemObjectByPK - we can't obtain the required id = " + id
					+ "]  stored - \n" + ex.getMessage() + "\n" + ex.getCause());
			throw new SystemObjectException(
					"SystemObjectDao: getSystemObjectByPK - we can't obtain the required id : " + id
							+ " - " + ex.getMessage() + "\n" + ex.getCause());

		}

		return systemObject;
	}

	public SystemObject getSystemObjectByName(String name) throws SystemObjectException {

		SystemObject systemObject = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			systemObject = (SystemObject) session.createCriteria(SystemObject.class)
					.add(Restrictions.naturalId().set("name", name)).uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("SystemObjectDao: getSystemObjectByName - can't obtain systemObject name = "
					+ name + "]  stored - \n" + ex.getMessage() + "\n" + ex.getCause());
			throw new SystemObjectException("getSystemObjectByName;  can't obtain systemObject name: "
					+ name + " - " + ex.getMessage() + "\n" + ex.getCause());

		}

		return systemObject;
	}

	@SuppressWarnings("unchecked")
	public List<SystemObject> getSystemObjects() throws SystemObjectException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<SystemObject> systemObjects = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			systemObjects = session.createQuery("From SystemObject Order By hierarchy ASC ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("SystemObjectDao: getSystemObjects() - can't obtain systemObject list - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new SystemObjectException(
					"SystemObjectDao: getSystemObjects() - can't obtain systemObject list: "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

		return systemObjects;
	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(String firstLineText, String separation)
			throws SystemObjectException {

		List<SystemObject> systemObjectList = null;
		List<StringPair> returnValue = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			systemObjectList = session.createQuery("From SystemObject Order By name ASC ").list();

			tx.commit();

			if (systemObjectList != null) {

				// inserta los extras
				if (firstLineText != null && !"".equals(firstLineText)) {
					if (!firstLineText.equals("WHITESPACE")) {
						returnValue.add(new StringPair(null, firstLineText)); 
					} else {
						returnValue.add(new StringPair(null, " ")); 
					}
				}

				if (separation != null && !"".equals(separation)) {
					if (!separation.equals("WHITESPACE")) {
						returnValue.add(new StringPair(null, separation));
					} else {
						returnValue.add(new StringPair(null, " ")); 
					}
				}

				for (SystemObject systemObject : systemObjectList) {
					returnValue.add(new StringPair(systemObject.getId(), systemObject.getName()));
				}
			} else {
				returnValue = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new SystemObjectException("Can't obtain systemObject combo list " + ex.getMessage()
					+ "\n" + ex.getCause());
		} catch (Exception e) {
		}

		return returnValue;

	}

	public boolean duplicatedNameVerification(String name, Integer id) throws SystemObjectException {

		SystemObject systemObject = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			systemObject = (SystemObject) session
					.createQuery("From SystemObject WHERE  name= ? AND id != ?")
					.setString(0, name)
					.setInteger(1, id)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new SystemObjectException("It was no possible to get the systemObject: " + name
					+ " - " + ex.getMessage() + " ; " + ex.getCause());

		}
		if (systemObject != null)
			return true;
		else
			return false;
	}

}
