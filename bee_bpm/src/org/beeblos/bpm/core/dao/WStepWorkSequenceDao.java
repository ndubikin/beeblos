package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.model.WStepWorkSequence;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;

import com.sp.common.util.StringPair;

public class WStepWorkSequenceDao {

	private static final Log logger = LogFactory.getLog(WStepWorkSequenceDao.class.getName());

	public WStepWorkSequenceDao() {

	}

	public Integer add(WStepWorkSequence stepWorkSequence) throws WStepWorkSequenceException {

		try {

			return Integer.valueOf(HibernateUtil.guardar(stepWorkSequence));

		} catch (HibernateException ex) {
			logger.error("WStepWorkSequenceDao: add - Can't store stepWorkSequence definition record "
					+ stepWorkSequence.getStepSequence().getName()
					+ " - "
					+ ex.getMessage()
					+ "\n"
					+ ex.getCause());
			throw new WStepWorkSequenceException(
					"WStepWorkSequenceDao: add - Can't store stepWorkSequence definition record "
							+ stepWorkSequence.getStepSequence().getName() + " - "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

	}

	public void update(WStepWorkSequence stepWorkSequence) throws WStepWorkSequenceException {

		logger.debug("update() WStepWorkSequence < id = " + stepWorkSequence.getId() + ">");

		try {

			HibernateUtil.actualizar(stepWorkSequence);

		} catch (HibernateException ex) {
			logger.error("WStepWorkSequenceDao: update - Can't update stepWorkSequence definition record "
					+ stepWorkSequence.getStepSequence().getName()
					+ " - id = "
					+ stepWorkSequence.getId() + "\n - " + ex.getMessage() + "\n" + ex.getCause());
			throw new WStepWorkSequenceException(
					"WStepWorkSequenceDao: update - Can't update stepWorkSequence definition record "
							+ stepWorkSequence.getStepSequence().getName() + " - id = "
							+ stepWorkSequence.getId() + "\n - " + ex.getMessage() + "\n"
							+ ex.getCause());

		}

	}

	public void delete(WStepWorkSequence stepWorkSequence) throws WStepWorkSequenceException {

		logger.debug("update() WStepWorkSequence < id = " + stepWorkSequence.getId() + ">");

		try {

			stepWorkSequence = getWStepWorkSequenceByPK(stepWorkSequence.getId());

			HibernateUtil.borrar(stepWorkSequence);

		} catch (HibernateException ex) {
			logger.error("WStepWorkSequenceDao: delete - Can't delete proccess definition record "
					+ stepWorkSequence.getStepSequence().getName() + " <id = "
					+ stepWorkSequence.getId() + "> \n" + " - " + ex.getMessage() + "\n"
					+ ex.getCause());
			throw new WStepWorkSequenceException(
					"WStepWorkSequenceDao:  delete - Can't delete proccess definition record  "
							+ stepWorkSequence.getStepSequence().getName() + " <id = "
							+ stepWorkSequence.getId() + "> \n" + " - " + ex.getMessage() + "\n"
							+ ex.getCause());

		} catch (WStepWorkSequenceException ex1) {
			logger.error("WStepWorkSequenceDao: delete - Exception in deleting stepWorkSequence rec "
					+ stepWorkSequence.getStepSequence().getName()
					+ " <id = "
					+ stepWorkSequence.getId()
					+ "> not stored \n"
					+ " - "
					+ ex1.getMessage()
					+ "\n" + ex1.getCause());
			throw new WStepWorkSequenceException(
					"WStepWorkSequenceDao: delete - Exception in deleting stepWorkSequence rec "
							+ stepWorkSequence.getStepSequence().getName() + " <id = "
							+ stepWorkSequence.getId() + "> not stored \n" + " - "
							+ ex1.getMessage() + "\n" + ex1.getCause());

		}

	}

	public WStepWorkSequence getWStepWorkSequenceByPK(Integer id) throws WStepWorkSequenceException {

		WStepWorkSequence stepWorkSequence = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			stepWorkSequence = (WStepWorkSequence) session.get(WStepWorkSequence.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepWorkSequenceDao: getWStepWorkSequenceByPK - we can't obtain the required id = "
					+ id + "]  stored - \n" + ex.getMessage() + "\n" + ex.getCause());
			throw new WStepWorkSequenceException(
					"WStepWorkSequenceDao: getWStepWorkSequenceByPK - we can't obtain the required id : "
							+ id + " - " + ex.getMessage() + "\n" + ex.getCause());

		}

		return stepWorkSequence;
	}

	@SuppressWarnings("unchecked")
	public List<WStepWorkSequence> getWStepWorkSequences() throws WStepWorkSequenceException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepWorkSequence> stepWorkSequenceList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			stepWorkSequenceList = session.createQuery("From WStepWorkSequence Order By id ")
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepWorkSequenceDao: getWStepWorkSequences() - can't obtain stepWorkSequence list - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new WStepWorkSequenceException(
					"WStepWorkSequenceDao: getWStepWorkSequences() - can't obtain stepWorkSequence list: "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

		return stepWorkSequenceList;
	}

	// dml 20130827
	@SuppressWarnings("unchecked")
	public List<WStepWorkSequence> getWStepWorkSequencesByWorkingProcessId(Integer workingProcessId) throws WStepWorkSequenceException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepWorkSequence> stepWorkSequenceList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			stepWorkSequenceList = session.createQuery("From WStepWorkSequence Where stepWork.wProcessWork.id = :wpId Order By executionDate ASC")
					.setInteger("wpId", workingProcessId)
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepWorkSequenceDao: getWStepWorkSequences() - can't obtain stepWorkSequence list - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new WStepWorkSequenceException(
					"WStepWorkSequenceDao: getWStepWorkSequences() - can't obtain stepWorkSequence list: "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

		return stepWorkSequenceList;
	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(String firstLineText, String separation)
			throws WStepWorkSequenceException {

		List<WStepWorkSequence> stepWorkSequenceList = null;
		List<StringPair> returnValue = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			stepWorkSequenceList = session.createQuery("From WStepWorkSequence Order By id ")
					.list();

			tx.commit();

			if (stepWorkSequenceList != null) {

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

				for (WStepWorkSequence stepWorkSequence : stepWorkSequenceList) {
					returnValue.add(new StringPair(stepWorkSequence.getId(), stepWorkSequence
							.getStepSequence().getName()));
				}
			} else {
				returnValue = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new WStepWorkSequenceException("Can't obtain stepWorkSequence combo list "
					+ ex.getMessage() + "\n" + ex.getCause());
		} catch (Exception e) {
		}

		return returnValue;

	}

}