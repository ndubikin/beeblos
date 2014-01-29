package org.beeblos.bpm.core.dao;

import static com.sp.common.util.ConstantsCommon.DATE_FORMAT;
import static com.sp.common.util.ConstantsCommon.DATE_HOUR_COMPLETE_FORMAT;
import static com.sp.common.util.ConstantsCommon.LAST_ADDED;
import static com.sp.common.util.ConstantsCommon.LAST_MODIFIED;
import static org.beeblos.bpm.core.util.Constants.ALIVE;
import static org.beeblos.bpm.core.util.Constants.PROCESSED;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.noper.ProcessWorkLight;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.jadira.usertype.dateandtime.joda.columnmapper.TimestampColumnDateTimeMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.sp.common.util.HibernateUtil;


public class WProcessWorkDao {
	
	private static final Log logger = LogFactory.getLog(WProcessWorkDao.class.getName());
	
	public WProcessWorkDao (){
		
	}
	
	/**
	 * Insert a new process work -> new process is launched!
	 * 
	 * @param processWork
	 * @return
	 * @throws WProcessWorkException
	 */
	public Integer add(WProcessWork processWork) throws WProcessWorkException {
		
		logger.debug("add() WProcessWork - Name: ["+processWork.getReference()+"]");
		Integer id=null;

		try {
			// if no object related, relate object itself
			if (processWork.getIdObjectType()==WProcessWork.class.getName()) {
				processWork.setIdObject(1);
			}

			id = Integer.valueOf(HibernateUtil.save(processWork));
			
			if (processWork.getIdObjectType()==WProcessWork.class.getName()) {
				processWork.setIdObject(id);
				HibernateUtil.update(processWork);
			}

			// if exists managed data then will be explore if there are managed data fields to synchronize at startup process level...
			if (processWork.getProcessDef().getProcessHead().getManagedTableConfiguration()!=null
					&& processWork.getProcessDef().getProcessHead().getManagedTableConfiguration().getName()!=null
					&& !"".equals(processWork.getProcessDef().getProcessHead().getManagedTableConfiguration().getName()) ) {
				
				ManagedData md = 
						org.beeblos.bpm.tm.TableManagerBeeBpmUtil
										.createManagedDataObject(processWork);
			}
			
		} catch (HibernateException ex) {
			logger.error("WProcessWorkDao: add - Can't store process definition record "+ 
					processWork.getReference()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessWorkException("WProcessWorkDao: add - Can't store process definition record "+ 
					processWork.getReference()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

		return id;
	}
	
	
	public void update(WProcessWork processWork) throws WProcessWorkException {
		
		logger.debug("update() WProcessWork < id = "+processWork.getId()+">");
		
		try {

			HibernateUtil.update(processWork);


		} catch (HibernateException ex) {
			logger.error("WProcessWorkDao: update - Can't update process definition record "+ 
					processWork.getReference()  +
					" - id = "+processWork.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WProcessWorkException("WProcessWorkDao: update - Can't update process definition record "+ 
					processWork.getReference()  +
					" - id = "+processWork.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WProcessWork processWork) throws WProcessWorkException {

		logger.debug("delete() WProcessWork - Name: ["+processWork.getReference()+"]");
		
		try {

			HibernateUtil.delete(processWork);

		} catch (HibernateException ex) {
			logger.error("WProcessWorkDao: delete - Can't delete proccess definition record "+ processWork.getReference() +
					" <id = "+processWork.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessWorkException("WProcessWorkDao:  delete - Can't delete proccess definition record  "+ processWork.getReference() +
					" <id = "+processWork.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} 

	}

	public WProcessWork getWProcessWorkByPK(Integer id) throws WProcessWorkException {

		WProcessWork process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			process = (WProcessWork) session.get(WProcessWork.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessWorkDao: getWProcessWorkByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessWorkException("WProcessWorkDao: getWProcessWorkByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}
	
	
	public WProcessWork getWProcessWorkByName(String name) throws WProcessWorkException {

		WProcessWork  process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			process = (WProcessWork) session.createCriteria(WProcessWork.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessWorkDao: getWProcessWorkByName - can't obtain process name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessWorkException("getWProcessWorkByName;  can't obtain process name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}

	/**
	 * @author dmuleiro - 20130829
	 * 
	 * Returns the List<WProcessWork> related with a concrete WProcessDef.
	 *
	 * @param  Integer processId
	 * @param  Integer currentUserId
	 * 
	 * @return List<WProcessWork>
	 * 
	 * @throws WProcessWorkException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<WProcessWork> getWProcessWorkListByProcessId(Integer processId) throws WProcessWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WProcessWork> processWorkList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processWorkList = session.createQuery("From WProcessWork Where processDef.id = :processId Order By id")
					.setInteger("processId", processId)
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessWorkDao: getWProcessWorksByProcessId() - can't obtain processWork list - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new WProcessWorkException(
					"WProcessWorkDao: getWProcessWorksByProcessId() - can't obtain processWork list: "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

		return processWorkList;
	}

	public List<WProcessWork> getWProcessWorks() throws WProcessWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WProcessWork> processs = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processs = session.createQuery("From WProcessWork order by id ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessWorkDao: getWProcessWorks() - can't obtain processWork list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessWorkException("WProcessWorkDao: getWProcessWorks() - can't obtain processWork list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return processs;
	}
	
	public List<ProcessWorkLight> finderWorkingWork(Integer idProcess, 
			String workTypeFilter, boolean onlyActiveWorkingProcessesFilter, 
			DateTime initialStartedDateFilter, DateTime finalStartedDateFilter, 
			boolean estrictStartedDateFilter, DateTime initialFinishedDateFilter, DateTime finalFinishedDateFilter, 
			boolean estrictFinishedDateFilter, String action) throws WProcessWorkException {

		String filter = "";

		filter = buildWorkingWorkFilter(idProcess, workTypeFilter,
				onlyActiveWorkingProcessesFilter, initialStartedDateFilter, finalStartedDateFilter,
				estrictStartedDateFilter, initialFinishedDateFilter,
				finalFinishedDateFilter, estrictFinishedDateFilter, filter);

		if (filter != null && !"".equals(filter)){
			filter = "WHERE " + filter;
		}

		String query = _buildFinderWorkingWorkQuery(idProcess, filter, action);
		
		logger.debug("------>> getWorkingWorkListFinder -> query:" + query
				+ "<<-------");

		return getWorkingWorkList(query);
		
	}

	private String buildWorkingWorkFilter(Integer idProcess,
			String workTypeFilter, boolean onlyActiveWorkingProcessesFilter, 
			DateTime initialStartedDateFilter,
			DateTime finalStartedDateFilter, boolean estrictStartedDateFilter,
			DateTime initialFinishedDateFilter, DateTime finalFinishedDateFilter,
			boolean estrictFinishedDateFilter, String filter) {
		
		if (idProcess != null && idProcess != 0) {
			if (!"".equals(filter)) {
				filter += " AND ";
			}
			filter += " wpd.id = " + idProcess;
		}
		
		if (!"".equals(workTypeFilter) || !"ALL".equals(workTypeFilter)) {
			if ("PROCESSING".equals(workTypeFilter)) {
				if (!"".equals(filter)) {
					filter += " AND ";
				}
				filter += " pw.end_time IS NULL ";				
			} else if ("FINISHED".equals(workTypeFilter)){
				if (!"".equals(filter)) {
					filter += " AND ";
				}
				filter += " pw.end_time IS NOT NULL ";				
			}
		}
		
		if (onlyActiveWorkingProcessesFilter) {
			if (!"".equals(filter)) {
				filter += " AND wpd.active IS TRUE ";
			} else {
				filter += " wpd.active IS TRUE ";

			}
		}
		
		DateTimeFormatter fmtShortDate = DateTimeFormat.forPattern(DATE_FORMAT);
		DateTimeFormatter fmtLongDate = DateTimeFormat.forPattern(DATE_HOUR_COMPLETE_FORMAT);

		DateTime from = null;
		DateTime to = null;

		if (initialStartedDateFilter != null) {
			if (estrictStartedDateFilter) {
				if (!"".equals(filter)) {
					filter += " AND ";
				}
				from = fmtLongDate.parseDateTime(fmtShortDate.print(initialStartedDateFilter)
						+ " 00:00:00");
				to = fmtLongDate.parseDateTime(fmtShortDate.print(initialStartedDateFilter)
						+ " 23:59:59");
				filter += " pw.starting_time >= '" + from + "' AND pw.starting_time <= '" + to + "' ";
			} else {
				if (finalStartedDateFilter != null) {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " (pw.starting_time >= '" + initialStartedDateFilter
							+ "' AND pw.starting_time <= '" + finalStartedDateFilter + "') ";
				} else {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " pw.starting_time >= '" + initialStartedDateFilter + "' ";
				}
			}
		}

		if (initialFinishedDateFilter != null) {
			if (estrictFinishedDateFilter) {
				if (!"".equals(filter)) {
					filter += " AND ";
				}
				from = fmtLongDate.parseDateTime(fmtShortDate.print(initialFinishedDateFilter)
						+ " 00:00:00");
				to = fmtLongDate.parseDateTime(fmtShortDate.print(initialFinishedDateFilter)
						+ " 23:59:59");
				filter += " pw.end_time >= '" + from + "' AND pw.end_time <= '" + to + "' ";
			} else {
				if (finalFinishedDateFilter != null) {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " (pw.end_time >= '" + initialFinishedDateFilter
							+ "' AND pw.end_time <= '" + finalFinishedDateFilter + "') ";
				} else {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " pw.end_time >= '" + initialFinishedDateFilter + "' ";
				}
			}
		}

		logger.debug("QUERY FILTER:" + filter);

		return filter;
	}

	private String _buildFinderWorkingWorkQuery(Integer idProcess, String filter, String searchOrder) {

		String tmpQuery = "SELECT ";
		tmpQuery += " pw.id_process, "; // 0
		tmpQuery += " ph.name, ";
		tmpQuery += " pw.reference, ";
		tmpQuery += " pw.comments, "; // 3
		tmpQuery += " (SELECT COUNT(sw.id) FROM w_step_work sw LEFT OUTER JOIN w_process_work wpw ON sw.id_work=wpw.id WHERE sw.decided_date IS NULL AND wpw.id_process = pw.id_process AND sw.id_work = pw.id ) AS liveSteps, ";
		tmpQuery += " pw.starting_time, ";
		tmpQuery += " ps.id AS processStatus, "; // status id 6 	nes 20130828
		tmpQuery += " ps.name AS statusName, "; // status name
		tmpQuery += " pw.end_time, ";
		tmpQuery += " pw.id AS idWork";		// 9

		tmpQuery += " FROM w_process_work pw ";
		tmpQuery += " LEFT OUTER JOIN w_process_def wpd ON wpd.id = pw.id_process ";
		tmpQuery += " LEFT OUTER JOIN w_process_head ph ON ph.id=wpd.head_id ";
		tmpQuery += " LEFT OUTER JOIN w_process_status ps ON ps.id = pw.id_status ";

		tmpQuery += filter;

		if (searchOrder!=null && searchOrder.equals(LAST_ADDED)) {
			tmpQuery += " ORDER by pw.insert_date DESC;";
		} else if (searchOrder!=null && searchOrder.equals(LAST_MODIFIED)) {
			tmpQuery += " ORDER by pw.mod_date DESC;";  //rrl 20130206 Ajuste antes estaba con i.fecha_modificacion
		} else {
			tmpQuery += " ORDER by pw.id ASC;";
		}

		logger.debug("------>> getWorkingProcessWorkListByFinder -> query:" + tmpQuery
						+ "<<-------");

		logger.debug("QUERY:" + tmpQuery);

		return tmpQuery;
	}

	private List<ProcessWorkLight> getWorkingWorkList(String query)
			throws WProcessWorkException {

		Integer idProcess,idWork,statusId, liveSteps;
		String processName, workReference, workComments, stausName;
		DateTime started, finished;
		
		Session session = null;
		Transaction tx = null;

		List<Object[]> result = null;
		List<ProcessWorkLight> returnList = new ArrayList<ProcessWorkLight>();

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			Hibernate.initialize(result);

			result = session.createSQLQuery(query).list();

			tx.commit();

			if (result != null) {

				for (Object irObj : result) {

					Object[] cols = (Object[]) irObj;


					idProcess = (cols[0] != null ? new Integer(
									cols[0].toString()) : null);
					processName = (cols[1] != null ? cols[1].toString() : "");
					workReference = (cols[2] != null ? cols[2].toString() : "");
					workComments = (cols[3] != null ? cols[3].toString() : "");
					liveSteps = (cols[4] != null ? new Integer(
									cols[4].toString()) : null);
					
					//started = (cols[5] != null ? (Date) cols[5] : null);
					started = (cols[5] != null ? new TimestampColumnDateTimeMapper().fromNonNullValue((Timestamp) cols[5]) : null);

					// nes 20130828
					statusId = (cols[6] != null ? new Integer(
								cols[6].toString()) : null);
					stausName = (cols[7] != null ? cols[7].toString() : "");
					
					//finished = (cols[8] != null ? (Date) cols[8] : null);
					finished = (cols[8] != null ? new TimestampColumnDateTimeMapper().fromNonNullValue((Timestamp) cols[8]) : null);
					
					idWork = (cols[9] != null ? new Integer(
								cols[9].toString()) : null);

					returnList.add(new ProcessWorkLight(idProcess, processName, workReference, 
							workComments, liveSteps, started, stausName, finished, idWork));
				}

			} else {

				returnList = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
				logger.warn("WProcessDefDao: getWorkingProcessWorkListByFinder() - " +
						"It cannot be posible to get the ProcessWorkLight list - "
					+ ex.getMessage() + "\n" + ex.getLocalizedMessage() + " \n" + ex.getCause());
			throw new WProcessWorkException(ex);

		}

		return returnList;
	}	

	/**
	 * returns qty of existing works in WStepWork
	 * default: ALL
	 * 
	 * @param processId
	 * @param mode: ALL / ALIVE / PROCESSED
	 * @return
	 * @throws WProcessWorkException
	 */
	public Integer getWorkCount (Integer processId, String mode) 
			throws WProcessWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		BigInteger qtySteps;

		String query = "SELECT COUNT(*) FROM w_process_work wpw WHERE wpw.id_process = " + processId;
		
		if (mode.equals(ALIVE)) {
			query += " AND wsw.decided_date is null";
		} else if (mode.equals(PROCESSED)) {
			query += " AND wsw.decidedDate is not null";
		} 
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			qtySteps = (BigInteger) session
								.createSQLQuery(query)
								.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess="WProcessWorkDao: getWorkCount() - error trying count processWork - " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn(mess);
			throw new WProcessWorkException(mess);

		}
		
		if (qtySteps != null){
			return qtySteps.intValue();
		}
		
		return 0;		
	}

}
	