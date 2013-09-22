package org.beeblos.bpm.core.dao;

import static org.beeblos.bpm.core.util.Constants.ALIVE;
import static org.beeblos.bpm.core.util.Constants.PROCESSED;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.noper.ProcessWorkLight;
import com.sp.common.core.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


public class WProcessWorkDao {
	
	private static final Log logger = LogFactory.getLog(WProcessWorkDao.class.getName());
	
	public WProcessWorkDao (){
		
	}
	
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
			Date initialStartedDateFilter, Date finalStartedDateFilter, 
			boolean estrictStartedDateFilter, Date initialFinishedDateFilter, Date finalFinishedDateFilter, 
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
			Date initialStartedDateFilter,
			Date finalStartedDateFilter, boolean estrictStartedDateFilter,
			Date initialFinishedDateFilter, Date finalFinishedDateFilter,
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
		
		if (initialStartedDateFilter!=null){
			
			java.sql.Date initialStartedDateFilterSQL=new java.sql.Date(initialStartedDateFilter.getTime());
			
			if (estrictStartedDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" pw.starting_time >= '"+initialStartedDateFilterSQL+" 00:00:00' ";
				filter+=" AND pw.starting_time <= '"+initialStartedDateFilterSQL+" 23:59:59' ";
			} else {
				if (finalStartedDateFilter!=null){
					java.sql.Date finalStartedDateFilterSQL=new java.sql.Date(finalStartedDateFilter.getTime());
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (pw.starting_time >= '"+initialStartedDateFilterSQL+"' AND pw.starting_time <= '"+finalStartedDateFilterSQL+"') ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" pw.starting_time >= '"+initialStartedDateFilterSQL+"' ";
				}
			}
		}

		if (initialFinishedDateFilter!=null){
			
			java.sql.Date initialFinishedDateFilterSQL=new java.sql.Date(initialFinishedDateFilter.getTime());
			
			if (estrictFinishedDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" pw.end_time >= '"+initialFinishedDateFilterSQL+" 00:00:00' ";
				filter+=" AND pw.end_time <= '"+initialFinishedDateFilterSQL+" 23:59:59' ";
			} else {
				if (finalFinishedDateFilter!=null){
					java.sql.Date finalFinishedDateFilterSQL=new java.sql.Date(finalFinishedDateFilter.getTime());
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (pw.end_time >= '"+initialFinishedDateFilterSQL+"' AND pw.end_time <= '"+finalFinishedDateFilterSQL+"') ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" pw.end_time >= '"+initialFinishedDateFilterSQL+"' ";
				}
			}
		}

		logger.debug("QUERY FILTER:" + filter);

		return filter;
	}

	private String _buildFinderWorkingWorkQuery(Integer idProcess, String filter, String action) {

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

		if (action == null || action.equals("")) {
			tmpQuery += " ORDER by wpd.id ASC;";
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
		Date started, finished;
		
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
					started = (cols[5] != null ? (Date) cols[5] : null);
					// nes 20130828
					statusId = (cols[6] != null ? new Integer(
								cols[6].toString()) : null);
					stausName = (cols[7] != null ? cols[7].toString() : "");
					finished = (cols[8] != null ? (Date) cols[8] : null);
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

	// returns # existing works in WStepWork
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
	