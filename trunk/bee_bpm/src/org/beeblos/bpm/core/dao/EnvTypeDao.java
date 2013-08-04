package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.EnvTypeException;
import org.beeblos.bpm.core.model.EnvType;
import com.sp.common.util.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

public class EnvTypeDao {

	private static final Log logger = LogFactory.getLog(EnvTypeDao.class.getName());

	public EnvTypeDao() {

	}

	public Integer add(EnvType envType) throws EnvTypeException {

		logger.debug("add() EnvType - Name: [" + envType.getName() + "]");

		try {

			return Integer.valueOf(HibernateUtil.guardar(envType));

		} catch (HibernateException ex) {
			logger.error("EnvTypeDao: add - Can't store envType definition record "
					+ envType.getName() + " - " + ex.getMessage() + "\n" + ex.getCause());
			throw new EnvTypeException(
					"EnvTypeDao: add - Can't store envType definition record "
							+ envType.getName() + " - " + ex.getMessage() + "\n"
							+ ex.getCause());

		}

	}

	public void update(EnvType envType) throws EnvTypeException {

		logger.debug("update() EnvType < id = " + envType.getId() + ">");

		try {

			HibernateUtil.actualizar(envType);

		} catch (HibernateException ex) {
			logger.error("EnvTypeDao: update - Can't update envType definition record "
					+ envType.getName() + " - id = " + envType.getId() + "\n - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new EnvTypeException(
					"EnvTypeDao: update - Can't update envType definition record "
							+ envType.getName() + " - id = " + envType.getId() + "\n - "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

	}

	public void delete(EnvType envType) throws EnvTypeException {

		logger.debug("delete() EnvType - Name: [" + envType.getName() + "]");

		try {

			envType = getEnvTypeByPK(envType.getId());

			HibernateUtil.borrar(envType);

		} catch (HibernateException ex) {
			logger.error("EnvTypeDao: delete - Can't delete proccess definition record "
					+ envType.getName() + " <id = " + envType.getId() + "> \n" + " - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new EnvTypeException(
					"EnvTypeDao:  delete - Can't delete proccess definition record  "
							+ envType.getName() + " <id = " + envType.getId() + "> \n"
							+ " - " + ex.getMessage() + "\n" + ex.getCause());

		} catch (EnvTypeException ex1) {
			logger.error("EnvTypeDao: delete - Exception in deleting envType rec "
					+ envType.getName() + " <id = " + envType.getId()
					+ "> not stored \n" + " - " + ex1.getMessage() + "\n" + ex1.getCause());
			throw new EnvTypeException(
					"EnvTypeDao: delete - Exception in deleting envType rec "
							+ envType.getName() + " <id = " + envType.getId()
							+ "> not stored \n" + " - " + ex1.getMessage() + "\n" + ex1.getCause());

		}

	}

	public EnvType getEnvTypeByPK(Short id) throws EnvTypeException {

		EnvType envType = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			envType = (EnvType) session.get(EnvType.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("EnvTypeDao: getEnvTypeByPK - we can't obtain the required id = " + id
					+ "]  stored - \n" + ex.getMessage() + "\n" + ex.getCause());
			throw new EnvTypeException(
					"EnvTypeDao: getEnvTypeByPK - we can't obtain the required id : " + id
							+ " - " + ex.getMessage() + "\n" + ex.getCause());

		}

		return envType;
	}

	public EnvType getEnvTypeByName(String name) throws EnvTypeException {

		EnvType envType = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			envType = (EnvType) session.createCriteria(EnvType.class)
					.add(Restrictions.naturalId().set("name", name)).uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("EnvTypeDao: getEnvTypeByName - can't obtain envType name = "
					+ name + "]  stored - \n" + ex.getMessage() + "\n" + ex.getCause());
			throw new EnvTypeException("getEnvTypeByName;  can't obtain envType name: "
					+ name + " - " + ex.getMessage() + "\n" + ex.getCause());

		}

		return envType;
	}

	@SuppressWarnings("unchecked")
	public List<EnvType> getEnvTypes() throws EnvTypeException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<EnvType> envTypeList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			envTypeList = session.createQuery("From EnvType Order By id ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("EnvTypeDao: getEnvTypes() - can't obtain envType list - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new EnvTypeException(
					"EnvTypeDao: getEnvTypes() - can't obtain envType list: "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

		return envTypeList;
	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(String firstLineText, String separation)
			throws EnvTypeException {

		List<EnvType> envTypeList = null;
		List<StringPair> returnValue = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			envTypeList = session.createQuery("From EnvType Order By name ").list();

			tx.commit();
			
			if (envTypeList != null) {

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

				for (EnvType envType : envTypeList) {
					returnValue.add(new StringPair(envType.getId(), envType.getName()));
				}
			} else {
				returnValue = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new EnvTypeException("Can't obtain envType combo list " + ex.getMessage()
					+ "\n" + ex.getCause());
		} catch (Exception e) {
		}

		return returnValue;

	}

	public boolean duplicatedNameVerification(String name, Short id) throws EnvTypeException {

		EnvType envType = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			envType = (EnvType) session
					.createQuery("From EnvType WHERE  name= ? AND id != ?")
					.setString(0, name)
					.setInteger(1, id)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new EnvTypeException("It was no possible to get the envType: " + name
					+ " - " + ex.getMessage() + " ; " + ex.getCause());

		}
		if (envType != null)
			return true;
		else
			return false;
	}

}
