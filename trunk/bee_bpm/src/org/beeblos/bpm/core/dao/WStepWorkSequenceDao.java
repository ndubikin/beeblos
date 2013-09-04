package org.beeblos.bpm.core.dao;

import java.math.BigInteger;
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

			return Integer.valueOf(HibernateUtil.save(stepWorkSequence));

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

			HibernateUtil.update(stepWorkSequence);

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

			HibernateUtil.delete(stepWorkSequence);

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

		} catch (WStepWorkSequenceException e) {
			logger.error("WStepWorkSequenceDao: delete - Exception in deleting stepWorkSequence rec "
					+ stepWorkSequence.getStepSequence().getName()
					+ " <id = "
					+ stepWorkSequence.getId()
					+ "> not stored \n"
					+ " - "
					+ e.getMessage()
					+ "\n" + e.getCause());
			throw new WStepWorkSequenceException(
					"WStepWorkSequenceDao: delete - Exception in deleting stepWorkSequence rec "
							+ stepWorkSequence.getStepSequence().getName() + " <id = "
							+ stepWorkSequence.getId() + "> not stored \n" + " - "
							+ e.getMessage() + "\n" + e.getCause());

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

	/**
	 * @author dmuleiro - 20130827
	 * 
	 * Returns the List<WStepWorkSequence> related with a concrete WProcessWork.
	 *
	 * @param  Integer processId
	 * @param  Integer currentUserId
	 * 
	 * @return List<WStepWorkSequence>
	 * 
	 */
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
			logger.warn("WStepWorkSequenceDao: getWStepWorkSequencesByWorkingProcessId() - can't obtain stepWorkSequence list - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new WStepWorkSequenceException(
					"WStepWorkSequenceDao: getWStepWorkSequencesByWorkingProcessId() - can't obtain stepWorkSequence list: "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

		return stepWorkSequenceList;
	}

	/**
	 * @author dmuleiro - 20130829
	 * 
	 * Returns the List<WStepWorkSequence> related with a concrete WProcessDef.
	 *
	 * @param  Integer processId
	 * @param  Integer currentUserId
	 * 
	 * @return List<WStepWorkSequence>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<WStepWorkSequence> getWStepWorkSequencesByProcessId(Integer processId) throws WStepWorkSequenceException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepWorkSequence> stepWorkSequenceList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			stepWorkSequenceList = session.createQuery("From WStepWorkSequence Where stepWork.wProcessWork.processDef.id = :processId Order By executionDate ASC")
					.setInteger("processId", processId)
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepWorkSequenceDao: getWStepWorkSequencesByProcessId() - can't obtain stepWorkSequence list - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new WStepWorkSequenceException(
					"WStepWorkSequenceDao: getWStepWorkSequencesByProcessId() - can't obtain stepWorkSequence list: "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

		return stepWorkSequenceList;
	}

	/**
	 * @author dmuleiro - 20130829
	 * 
	 * Returns the number of "WStepWorkSequence" registers related to a concrete "WStepSequenceDef"
	 *
	 * @param  Integer routeId
	 * @param  Integer currentUserId
	 * 
	 * @return Integer
	 * 
	 */
	public Integer countRouteRelatedStepWorkSequences(Integer routeId) 
			throws WStepWorkSequenceException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		BigInteger count = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			String sqlQuery = "SELECT COUNT(*) FROM w_step_work_sequence WHERE step_sequence_def_id = " + routeId;

			count = (BigInteger) session.createSQLQuery(sqlQuery)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepWorkSequenceDao: countRouteRelatedStepWorkSequences() - can't count stepWorkSequence - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new WStepWorkSequenceException(
					"WStepWorkSequenceDao: countRouteRelatedStepWorkSequences() - can't count stepWorkSequence: "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

		if (count == null || count.intValue() == 0){
			return 0;
		} else {
			return count.intValue();
		}

	}

	/**
	 * @author dmuleiro - 20130830
	 * 
	 * Returns the number of "WStepWorkSequence" registers related to a concrete "WStepDef"
	 *
	 * @param  Integer stepId
	 * @param  Integer currentUserId
	 * 
	 * @return Integer
	 * 
	 * @throws WStepWorkSequenceException
	 * 
	 */
	public Integer countStepRelatedStepWorkSequences(Integer stepId) 
			throws WStepWorkSequenceException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		BigInteger count = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			String sqlQuery = "SELECT COUNT(*) FROM w_step_work_sequence " +
					"WHERE begin_step_id = " + stepId + " OR end_step_id = " + stepId;;

			count = (BigInteger) session.createSQLQuery(sqlQuery)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepWorkSequenceDao: countStepRelatedStepWorkSequences() - can't count stepWorkSequence - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new WStepWorkSequenceException(
					"WStepWorkSequenceDao: countStepRelatedStepWorkSequences() - can't count stepWorkSequence: "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

		if (count == null || count.intValue() == 0){
			return 0;
		} else {
			return count.intValue();
		}

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
