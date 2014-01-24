package org.beeblos.bpm.core.dao;

import static org.beeblos.bpm.core.util.Constants.LAST_W_STEP_DEF_ADDED;
import static org.beeblos.bpm.core.util.Constants.LAST_W_STEP_DEF_MODIFIED;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WStepHeadException;
import org.beeblos.bpm.core.model.WStepHead;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.sp.common.util.HibernateUtil;
import com.sp.common.util.StringPair;

public class WStepHeadDao {

	private static final Log logger = LogFactory.getLog(WStepHeadDao.class.getName());

	public WStepHeadDao() {

	}

	public Integer add(WStepHead step) throws WStepHeadException {

		logger.debug("add() WStepHead - Name: [" + step.getName() + "]");

		try {

			return Integer.valueOf(HibernateUtil.save(step));

		} catch (HibernateException ex) {
			logger.error("WStepHeadDao: add - Can't store step definition record " + step.getName()
					+ " - " + ex.getMessage() + "\n" + ex.getCause());
			throw new WStepHeadException("WStepHeadDao: add - Can't store step definition record "
					+ step.getName() + " - " + ex.getMessage() + "\n" + ex.getCause());

		}

	}

	public void update(WStepHead step) throws WStepHeadException {

		logger.debug("update() WStepHead < id = " + step.getId() + ">");

		try {

			HibernateUtil.update(step);

		} catch (HibernateException ex) {
			logger.error("WStepHeadDao: update - Can't update step definition record "
					+ step.getName() + " - id = " + step.getId() + "\n - " + ex.getMessage() + "\n"
					+ ex.getCause());
			throw new WStepHeadException(
					"WStepHeadDao: update - Can't update step definition record " + step.getName()
							+ " - id = " + step.getId() + "\n - " + ex.getMessage() + "\n"
							+ ex.getCause());

		}

	}

	public void delete(WStepHead step) throws WStepHeadException {

		logger.debug("delete() WStepHead - Name: [" + step.getName() + "]");

		try {

			step = getStepDefByPK(step.getId());

			HibernateUtil.delete(step);

		} catch (HibernateException ex) {
			logger.error("WStepHeadDao: delete - Can't delete proccess definition record "
					+ step.getName() + " <id = " + step.getId() + "> \n" + " - " + ex.getMessage()
					+ "\n" + ex.getCause());
			throw new WStepHeadException(
					"WStepHeadDao:  delete - Can't delete proccess definition record  "
							+ step.getName() + " <id = " + step.getId() + "> \n" + " - "
							+ ex.getMessage() + "\n" + ex.getCause());

		} catch (WStepHeadException e) {
			logger.error("WStepHeadDao: delete - Exception in deleting step rec " + step.getName()
					+ " <id = " + step.getId() + "> no esta almacenada \n" + " - "
					+ e.getMessage() + "\n" + e.getCause());
			throw new WStepHeadException("WStepHeadDao: delete - Exception in deleting step rec "
					+ step.getName() + " <id = " + step.getId() + "> not stored \n" + " - "
					+ e.getMessage() + "\n" + e.getCause());

		}

	}

	public WStepHead getStepDefByPK(Integer id) throws WStepHeadException {

		WStepHead step = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			step = (WStepHead) session.get(WStepHead.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepHeadDao: getWStepHeadByPK - we can't obtain the required id = " + id
					+ "] - \n" + ex.getMessage() + "\n" + ex.getCause());
			throw new WStepHeadException(
					"WStepHeadDao: getWStepHeadByPK - we can't obtain the required id : " + id
							+ " - " + ex.getMessage() + "\n" + ex.getCause());

		}

		return step;
	}

	public WStepHead getStepDefByName(String name) throws WStepHeadException {

		WStepHead step = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			step = (WStepHead) session.createCriteria(WStepHead.class)
					.add(Restrictions.naturalId().set("name", name)).uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepHeadDao: getWStepHeadByName - can't obtain step name = " + name
					+ "]  almacenada - \n" + ex.getMessage() + "\n" + ex.getCause());
			throw new WStepHeadException("getWStepHeadByName;  can't obtain step name: " + name
					+ " - " + ex.getMessage() + "\n" + ex.getCause());

		}

		return step;
	}

	public List<WStepHead> getWStepHeads() throws WStepHeadException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepHead> steps = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			steps = session.createQuery("From WStepHead Order By name ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepHeadDao: getWStepHeads() - can't obtain step list - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new WStepHeadException("WStepHeadDao: getWStepHeads() - can't obtain step list: "
					+ ex.getMessage() + "\n" + ex.getCause());

		}

		return steps;
	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(String textoPrimeraLinea, String separacion)
			throws WStepHeadException {

		List<WStepHead> stepList = null;
		List<StringPair> retorno = new ArrayList<StringPair>(10);

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			stepList = session.createQuery("From WStepHead Order By name ").list();

			tx.commit();

			if (stepList != null) {

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

				for (WStepHead wsh : stepList) {
					retorno.add(new StringPair(wsh.getId(), wsh.getName()));
				}
			} else {
				// nes - si el select devuelve null entonces devuelvo null
				retorno = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new WStepHeadException("Can't obtain WStepHeads combo list " + ex.getMessage()
					+ "\n" + ex.getCause());
		} catch (Exception e) {
		}

		return retorno;

	}

	public List<WStepHead> getStepListByFinder(String nameFilter, String commentFilter,
			Integer userId, boolean isAdmin, String action) throws WStepHeadException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		org.hibernate.Query q = null;

		List<WStepHead> stepList = null;

		String userFilter = getSQLFilter(nameFilter, commentFilter);

		String requiredFilter = "";// getRequiredFilter(userId, isAdmin);

		String filter = userFilter
				+ (userFilter != null && !"".equals(userFilter) && requiredFilter != null
						&& !"".equals(requiredFilter) ? " AND " : "") + requiredFilter;

		filter = ((filter != null && !"".equals(filter)) ? " WHERE " : "") + filter;

		logger.debug(" ---->>>>>>>>>> userFilter:[" + userFilter + "]");
		logger.debug(" ---->>>>>>>>>> requiredFilter:[" + requiredFilter + "]");
		logger.debug(" ---->>>>>>>>>> filter:[" + filter + "]");

		// load base query phrase
		String query = getBaseQuery(isAdmin);

		logger.debug(" ---->>>>>>>>>> base query:[" + query + "]");

		// builds full query phrase
		query += filter + getSQLOrder(action);

		logger.debug(" ---->>>>>>>>>> FULL query:[" + query + "]");
		logger.debug(" ---->>>>>>>>>> userId: " + userId);

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			q = session.createSQLQuery(query).addEntity("WStepHead", WStepHead.class);

			// set userId
			// q.setInteger("userId",userId);

			// retrieve list
			stepList = q.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message = "WStepHeadDao: 002 getWStepHeads() - can't obtain step list - "
					+ ex.getMessage() + "\n" + ex.getCause();
			logger.warn(message);
			throw new WStepHeadException(message);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String message = "WStepHeadDao: 002B getWStepHeads() - can't obtain step list - "
					+ ex.getMessage() + "\n" + ex.getCause();
			logger.warn(message);
			throw new WStepHeadException(message);
		}

		return stepList;
	}

	private String getBaseQuery(boolean isAdmin) {

		String baseQueryTmp = "SELECT * FROM w_step_head wsh ";

		return baseQueryTmp;

	}

	private String getSQLFilter(String nameFilter, String commentFilter) {

		String filter = "";

		if (nameFilter != null && !"".equals(nameFilter)) {
			if (filter == "") {
				filter += " wsh.name LIKE '%" + nameFilter.trim() + "%' ";
			} else {
				filter += " AND wsh.name LIKE '%" + nameFilter.trim() + "%' ";
			}
		}

		if (commentFilter != null && !"".equals(commentFilter)) {
			if (filter == "") {
				filter += " wsh.comments LIKE '%" + commentFilter.trim() + "%' ";
			} else {
				filter += " AND wsh.comments LIKE '%" + commentFilter.trim() + "%' ";
			}
		}

		return filter;
	}

	private String getSQLOrder(String action) {

		String ret = "";

		if (action == null || action.equals("")) {

			ret = "";

		} else if (action.equals(LAST_W_STEP_DEF_ADDED)) {

			ret = " ORDER by wsh.insert_date DESC ";

		} else if (action.equals(LAST_W_STEP_DEF_MODIFIED)) {

			ret = " ORDER by wsh.mod_date DESC ";

		}

		return ret;

	}

	// dml 20130507
	public boolean headStepHasWStepDef(Integer stepHeadId)
			throws WStepHeadException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		BigInteger result = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			String query =  "SELECT IF(wsd.head_id = :stepHeadId,true,false) " +
					" FROM w_step_def wsd " +
					" WHERE wsd.head_id = :stepHeadId " +
					" GROUP BY wsd.head_id ";

			logger.debug("[QUERY]: "+query);
			
			result = (BigInteger) session.createSQLQuery(query)
					.setParameter("stepHeadId", stepHeadId)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepHeadDao: headStepHasWStepDef() - can't obtain result: "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new WStepHeadException(
					"WStepHeadDao: headStepHasWStepDef() - can't obtain result: "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

		if (result == null || result.intValue() == 0) {
			return false;
		} else {
			return true;
		}

	}

}
