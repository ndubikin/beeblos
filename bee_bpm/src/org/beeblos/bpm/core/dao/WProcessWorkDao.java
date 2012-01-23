package org.beeblos.bpm.core.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.model.noper.StepWorkLight;
import org.beeblos.bpm.core.model.noper.ProcessWorkLight;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


public class WProcessWorkDao {
	
	private static final Log logger = LogFactory.getLog(WProcessWorkDao.class.getName());
	
	public WProcessWorkDao (){
		
	}
	
	public Integer add(WProcessWork workProcess) throws WProcessWorkException {
		
		logger.debug("add() WProcessWork - Name: ["+workProcess.getReference()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(workProcess));

		} catch (HibernateException ex) {
			logger.error("WProcessWorkDao: add - Can't store process definition record "+ 
					workProcess.getReference()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessWorkException("WProcessWorkDao: add - Can't store process definition record "+ 
					workProcess.getReference()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WProcessWork workProcess) throws WProcessWorkException {
		
		logger.debug("update() WProcessWork < id = "+workProcess.getId()+">");
		
		try {

			HibernateUtil.actualizar(workProcess);


		} catch (HibernateException ex) {
			logger.error("WProcessWorkDao: update - Can't update process definition record "+ 
					workProcess.getReference()  +
					" - id = "+workProcess.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WProcessWorkException("WProcessWorkDao: update - Can't update process definition record "+ 
					workProcess.getReference()  +
					" - id = "+workProcess.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WProcessWork workProcess) throws WProcessWorkException {

		logger.debug("delete() WProcessWork - Name: ["+workProcess.getReference()+"]");
		
		try {

			HibernateUtil.borrar(workProcess);

		} catch (HibernateException ex) {
			logger.error("WProcessWorkDao: delete - Can't delete proccess definition record "+ workProcess.getReference() +
					" <id = "+workProcess.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessWorkException("WProcessWorkDao:  delete - Can't delete proccess definition record  "+ workProcess.getReference() +
					" <id = "+workProcess.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

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
	
	public List<ProcessWorkLight> getWorkingWorkListFinder(Integer idProcess, 
			boolean onlyActiveWorksFilter, Date initialStartedDateFilter, Date finalStartedDateFilter, 
			boolean estrictStartedDateFilter, Date initialFinishedDateFilter, Date finalFinishedDateFilter, 
			boolean estrictFinishedDateFilter, String action) throws WProcessWorkException {

		String filter = "";

		filter = buildWorkingWorkFilter(idProcess, onlyActiveWorksFilter,
				initialStartedDateFilter, finalStartedDateFilter,
				estrictStartedDateFilter, initialFinishedDateFilter,
				finalFinishedDateFilter, estrictFinishedDateFilter, filter);

		if (filter != null && !"".equals(filter)){
			filter = "WHERE " + filter;
		}

		String query = _buildWorkingWorkQuery(idProcess, filter, action);
		
		logger.debug("------>> getWorkingWorkListFinder -> query:" + query
				+ "<<-------");

		return getWorkingWorkList(query);
		
	}

	private String buildWorkingWorkFilter(Integer idProcess,
			boolean onlyActiveWorksFilter, Date initialStartedDateFilter,
			Date finalStartedDateFilter, boolean estrictStartedDateFilter,
			Date initialFinishedDateFilter, Date finalFinishedDateFilter,
			boolean estrictFinishedDateFilter, String filter) {
		if (idProcess != null && idProcess != 0) {
			if (!"".equals(filter)) {
				filter += " AND ";
			}
			filter += " wpd.id = " + idProcess;
		}
		
		if (onlyActiveWorksFilter) {
			if (!"".equals(filter)) {
				filter += " AND pw.end_time IS NULL ";
			} else {
				filter += " pw.end_time IS NULL ";

			}
		}
		
		if (initialStartedDateFilter!=null){
			
			java.sql.Date initialStartedDateFilterSQL=new java.sql.Date(initialStartedDateFilter.getTime());
			
			if (estrictStartedDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" arriving_date = '"+initialStartedDateFilterSQL+"' ";
			} else {
				if (finalStartedDateFilter!=null){
					java.sql.Date finalStartedDateFilterSQL=new java.sql.Date(finalStartedDateFilter.getTime());
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (arriving_date >= '"+initialStartedDateFilterSQL+"' AND arriving_date <= '"+finalStartedDateFilterSQL+"') ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" arriving_date >= '"+initialStartedDateFilterSQL+"' ";
				}
			}
		}

		if (initialFinishedDateFilter!=null){
			
			java.sql.Date initialFinishedDateFilterSQL=new java.sql.Date(initialFinishedDateFilter.getTime());
			
			if (estrictFinishedDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" finished_date = '"+initialFinishedDateFilterSQL+"' ";
			} else {
				if (finalFinishedDateFilter!=null){
					java.sql.Date finalFinishedDateFilterSQL=new java.sql.Date(finalFinishedDateFilter.getTime());
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (finished_date >= '"+initialFinishedDateFilterSQL+"' AND finished_date <= '"+finalFinishedDateFilterSQL+"') ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" finished_date >= '"+initialFinishedDateFilterSQL+"' ";
				}
			}
		}
		return filter;
	}

	private String _buildWorkingWorkQuery(Integer idProcess, String filter, String action) {

		
		String tmpQuery = "SELECT ";
		tmpQuery += " pw.reference, ";
		tmpQuery += " wpd.id, ";
		tmpQuery += " wpd.name, ";
		tmpQuery += " (SELECT COUNT(id_work) FROM w_step_work work WHERE work.decided_date IS NULL AND work.id_work = pw.id ) AS liveSteps, ";
		tmpQuery += " pw.starting_time, ";
		tmpQuery += " ps.name, ";
		tmpQuery += " pw.end_time, ";
		tmpQuery += " pw.comments ";

		tmpQuery += " FROM w_process_work pw ";
		tmpQuery += " LEFT OUTER JOIN w_process_def wpd ON wpd.id=pw.id_process ";
		tmpQuery += " LEFT OUTER JOIN w_process_status ps ON ps.id=pw.id_status ";

		tmpQuery += filter;

		if (action == null || action.equals("")) {
			tmpQuery += " ORDER by wpd.id ASC;";
		} 

		logger.debug("------>> getWorkingProcessWorkListByFinder -> query:" + tmpQuery
				+ "<<-------");

		System.out.println("QUERY:" + tmpQuery);

		return tmpQuery;
	}

	private List<ProcessWorkLight> getWorkingWorkList(String query)
			throws WProcessWorkException {

		Integer idProcess;
		String processName;
		String reference;
		String comments;

		Integer liveSteps;
		
		Date started;
		
		String status;
		
		Date finished;
		
		
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


					reference = (cols[0] != null ? cols[0].toString() : "");
					idProcess = (cols[1] != null ? new Integer(
							cols[1].toString()) : null);
					processName = (cols[2] != null ? cols[2].toString() : "");
					liveSteps = (cols[3] != null ? new Integer(
							cols[3].toString()) : null);
					started = (cols[4] != null ? (Date) cols[4] : null);
					status = (cols[5] != null ? cols[5].toString() : "");
					finished = (cols[6] != null ? (Date) cols[6] : null);
					comments = (cols[7] != null ? cols[7].toString() : "");

					returnList.add(new ProcessWorkLight(idProcess, processName, 
							reference, comments, liveSteps, started, status, finished));
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

}
	