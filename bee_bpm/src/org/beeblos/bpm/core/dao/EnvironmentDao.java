package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.EnvironmentException;
import org.beeblos.bpm.core.model.Environment;
import com.sp.common.util.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

public class EnvironmentDao {

	private static final Log logger = LogFactory.getLog(EnvironmentDao.class.getName());

	public EnvironmentDao() {

	}

	public Integer add(Environment environment) throws EnvironmentException {

		logger.debug("add() Environment - Name: [" + environment.getName() + "]");

		try {

			return Integer.valueOf(HibernateUtil.save(environment));

		} catch (HibernateException ex) {
			logger.error("EnvironmentDao: add - Can't store environment definition record "
					+ environment.getName() + " - " + ex.getMessage() + "\n" + ex.getCause());
			throw new EnvironmentException(
					"EnvironmentDao: add - Can't store environment definition record "
							+ environment.getName() + " - " + ex.getMessage() + "\n"
							+ ex.getCause());

		}

	}

	public void update(Environment environment) throws EnvironmentException {

		logger.debug("update() Environment < id = " + environment.getId() + ">");

		try {

			HibernateUtil.update(environment);

		} catch (HibernateException ex) {
			logger.error("EnvironmentDao: update - Can't update environment definition record "
					+ environment.getName() + " - id = " + environment.getId() + "\n - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new EnvironmentException(
					"EnvironmentDao: update - Can't update environment definition record "
							+ environment.getName() + " - id = " + environment.getId() + "\n - "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

	}

	public void delete(Environment environment) throws EnvironmentException {

		logger.debug("delete() Environment - Name: [" + environment.getName() + "]");

		try {

			environment = getEnvironmentByPK(environment.getId());

			HibernateUtil.delete(environment);

		} catch (HibernateException ex) {
			logger.error("EnvironmentDao: delete - Can't delete proccess definition record "
					+ environment.getName() + " <id = " + environment.getId() + "> \n" + " - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new EnvironmentException(
					"EnvironmentDao:  delete - Can't delete proccess definition record  "
							+ environment.getName() + " <id = " + environment.getId() + "> \n"
							+ " - " + ex.getMessage() + "\n" + ex.getCause());

		} catch (EnvironmentException ex1) {
			logger.error("EnvironmentDao: delete - Exception in deleting environment rec "
					+ environment.getName() + " <id = " + environment.getId()
					+ "> not stored \n" + " - " + ex1.getMessage() + "\n" + ex1.getCause());
			throw new EnvironmentException(
					"EnvironmentDao: delete - Exception in deleting environment rec "
							+ environment.getName() + " <id = " + environment.getId()
							+ "> not stored \n" + " - " + ex1.getMessage() + "\n" + ex1.getCause());

		}

	}

	public Environment getEnvironmentByPK(Integer id) throws EnvironmentException {

		Environment environment = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			environment = (Environment) session.get(Environment.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("EnvironmentDao: getEnvironmentByPK - we can't obtain the required id = " + id
					+ "]  stored - \n" + ex.getMessage() + "\n" + ex.getCause());
			throw new EnvironmentException(
					"EnvironmentDao: getEnvironmentByPK - we can't obtain the required id : " + id
							+ " - " + ex.getMessage() + "\n" + ex.getCause());

		}

		return environment;
	}

	public Environment getEnvironmentByName(String name) throws EnvironmentException {

		Environment environment = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			environment = (Environment) session.createCriteria(Environment.class)
					.add(Restrictions.naturalId().set("name", name)).uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("EnvironmentDao: getEnvironmentByName - can't obtain environment name = "
					+ name + "]  stored - \n" + ex.getMessage() + "\n" + ex.getCause());
			throw new EnvironmentException("getEnvironmentByName;  can't obtain environment name: "
					+ name + " - " + ex.getMessage() + "\n" + ex.getCause());

		}

		return environment;
	}

	@SuppressWarnings("unchecked")
	public List<Environment> getEnvironments() throws EnvironmentException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Environment> environmentList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			environmentList = session.createQuery("From Environment Order By id ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("EnvironmentDao: getEnvironments() - can't obtain environment list - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new EnvironmentException(
					"EnvironmentDao: getEnvironments() - can't obtain environment list: "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

		return environmentList;
	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(String firstLineText, String separation)
			throws EnvironmentException {

		List<Environment> environmentList = null;
		List<StringPair> returnValue = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			environmentList = session.createQuery("From Environment Order By name ").list();

			tx.commit();
			
			if (environmentList != null) {

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

				for (Environment environment : environmentList) {
					returnValue.add(new StringPair(environment.getId(), environment.getName()));
				}
			} else {
				returnValue = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new EnvironmentException("Can't obtain environment combo list " + ex.getMessage()
					+ "\n" + ex.getCause());
		} catch (Exception e) {
		}

		return returnValue;

	}

	public boolean duplicatedNameVerification(String name, Integer id) throws EnvironmentException {

		Environment environment = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			environment = (Environment) session
					.createQuery("From Environment WHERE  name= ? AND id != ?")
					.setString(0, name)
					.setInteger(1, id)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new EnvironmentException("It was no possible to get the environment: " + name
					+ " - " + ex.getMessage() + " ; " + ex.getCause());

		}
		if (environment != null)
			return true;
		else
			return false;
	}

}
