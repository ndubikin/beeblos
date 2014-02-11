package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WExternalMethodException;
import org.beeblos.bpm.core.model.WExternalMethod;
import org.hibernate.HibernateException;

import com.sp.common.util.HibernateUtil;
import com.sp.common.util.StringPair;

public class WExternalMethodDao {

	private static final Log logger = LogFactory.getLog(WExternalMethodDao.class.getName());

	public WExternalMethodDao() {

	}

	public Integer add(WExternalMethod externalMethod) throws WExternalMethodException {

		logger.debug("add() WExternalMethod - Name: [" + externalMethod.getClassname() + "]");

		try {

			return Integer.valueOf(HibernateUtil.save(externalMethod));

		} catch (HibernateException ex) {
			logger.error("WExternalMethodDao: add - Can't store externalMethod definition record " + externalMethod.getClassname()
					+ " - " + ex.getMessage() + "\n" + ex.getCause());
			throw new WExternalMethodException("WExternalMethodDao: add - Can't store externalMethod definition record "
					+ externalMethod.getClassname() + " - " + ex.getMessage() + "\n" + ex.getCause());

		}

	}

	public void update(WExternalMethod externalMethod) throws WExternalMethodException {

		logger.debug("update() WExternalMethod < id = " + externalMethod.getId() + ">");

		try {

			HibernateUtil.update(externalMethod);

		} catch (HibernateException ex) {
			logger.error("WExternalMethodDao: update - Can't update externalMethod definition record "
					+ externalMethod.getClassname() + " - id = " + externalMethod.getId() + "\n - " + ex.getMessage() + "\n"
					+ ex.getCause());
			throw new WExternalMethodException(
					"WExternalMethodDao: update - Can't update externalMethod definition record " + externalMethod.getClassname()
							+ " - id = " + externalMethod.getId() + "\n - " + ex.getMessage() + "\n"
							+ ex.getCause());

		}

	}

	public void delete(WExternalMethod externalMethod) throws WExternalMethodException {

		logger.debug("delete() WExternalMethod - Name: [" + externalMethod.getClassname() + "]");

		try {

			externalMethod = getExternalMethodByPK(externalMethod.getId());

			HibernateUtil.delete(externalMethod);

		} catch (HibernateException ex) {
			logger.error("WExternalMethodDao: delete - Can't delete proccess definition record "
					+ externalMethod.getClassname() + " <id = " + externalMethod.getId() + "> \n" + " - " + ex.getMessage()
					+ "\n" + ex.getCause());
			throw new WExternalMethodException(
					"WExternalMethodDao:  delete - Can't delete proccess definition record  "
							+ externalMethod.getClassname() + " <id = " + externalMethod.getId() + "> \n" + " - "
							+ ex.getMessage() + "\n" + ex.getCause());

		} catch (WExternalMethodException e) {
			logger.error("WExternalMethodDao: delete - Exception in deleting externalMethod rec " + externalMethod.getClassname()
					+ " <id = " + externalMethod.getId() + "> no esta almacenada \n" + " - "
					+ e.getMessage() + "\n" + e.getCause());
			throw new WExternalMethodException("WExternalMethodDao: delete - Exception in deleting externalMethod rec "
					+ externalMethod.getClassname() + " <id = " + externalMethod.getId() + "> not stored \n" + " - "
					+ e.getMessage() + "\n" + e.getCause());

		}

	}

	public WExternalMethod getExternalMethodByPK(Integer id) throws WExternalMethodException {

		WExternalMethod externalMethod = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			externalMethod = (WExternalMethod) session.get(WExternalMethod.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WExternalMethodDao: getWExternalMethodByPK - we can't obtain the required id = " + id
					+ "] - \n" + ex.getMessage() + "\n" + ex.getCause());
			throw new WExternalMethodException(
					"WExternalMethodDao: getWExternalMethodByPK - we can't obtain the required id : " + id
							+ " - " + ex.getMessage() + "\n" + ex.getCause());

		}

		return externalMethod;
	}

	public List<WExternalMethod> getWExternalMethods() throws WExternalMethodException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WExternalMethod> externalMethods = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			externalMethods = session.createQuery("From WExternalMethod Order By classname ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WExternalMethodDao: getWExternalMethods() - can't obtain externalMethod list - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new WExternalMethodException("WExternalMethodDao: getWExternalMethods() - can't obtain externalMethod list: "
					+ ex.getMessage() + "\n" + ex.getCause());

		}

		return externalMethods;
	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(String textoPrimeraLinea, String separacion)
			throws WExternalMethodException {

		List<WExternalMethod> externalMethodList = null;
		List<StringPair> retorno = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			externalMethodList = session.createQuery("From WExternalMethod Order By classname ").list();

			tx.commit();

			if (externalMethodList != null) {

				// inserta los extras
				if (textoPrimeraLinea != null && !"".equals(textoPrimeraLinea)) {
					if (!textoPrimeraLinea.equals("WHITESPACE")) {
						retorno.add(new StringPair(null, textoPrimeraLinea)); 
					} else {
						retorno.add(new StringPair(null, " ")); 
					}
				}

				if (separacion != null && !"".equals(separacion)) {
					if (!separacion.equals("WHITESPACE")) {
						retorno.add(new StringPair(null, separacion)); 
					} else {
						retorno.add(new StringPair(null, " ")); 
					}
				}

				for (WExternalMethod wphm : externalMethodList) {
					retorno.add(new StringPair(wphm.getId(), wphm.getClassname() + "-" + wphm.getMethodname()));
				}
			} else {
				// nes - si el select devuelve null entonces devuelvo null
				retorno = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new WExternalMethodException("Can't obtain WExternalMethods combo list " + ex.getMessage()
					+ "\n" + ex.getCause());
		} catch (Exception e) {
		}

		return retorno;

	}

}