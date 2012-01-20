package org.beeblos.bpm.core.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;
import org.beeblos.bpm.core.model.noper.WorkingProcessStep;
import org.beeblos.bpm.core.model.noper.WorkingProcessWork;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


public class WProcessDefDao {
	
	private static final Log logger = LogFactory.getLog(WProcessDefDao.class.getName());
	
	public WProcessDefDao (){
		
	}
	
	public Integer add(WProcessDef process) throws WProcessDefException {
		
		logger.debug("add() WProcessDef - Name: ["+process.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(process));

		} catch (HibernateException ex) {
			logger.error("WProcessDefDao: add - Can't store process definition record "+ 
					process.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao: add - Can't store process definition record "+ 
					process.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WProcessDef process) throws WProcessDefException {
		
		logger.debug("update() WProcessDef < id = "+process.getId()+">");
		
		try {

			HibernateUtil.actualizar(process);


		} catch (HibernateException ex) {
			logger.error("WProcessDefDao: update - Can't update process definition record "+ 
					process.getName()  +
					" - id = "+process.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WProcessDefException("WProcessDefDao: update - Can't update process definition record "+ 
					process.getName()  +
					" - id = "+process.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WProcessDef process) throws WProcessDefException {

		logger.debug("delete() WProcessDef - Name: ["+process.getName()+"]");
		
		try {

			//process = getWProcessDefByPK(process.getId());

			HibernateUtil.borrar(process);

		} catch (HibernateException ex) {
			logger.error("WProcessDefDao: delete - Can't delete proccess definition record "+ process.getName() +
					" <id = "+process.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao:  delete - Can't delete proccess definition record  "+ process.getName() +
					" <id = "+process.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

//		} catch (WProcessDefException ex1) {
//			logger.error("WProcessDefDao: delete - Exception in deleting process rec "+ process.getName() +
//					" <id = "+process.getId()+ "> no esta almacenada \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );
//			throw new WProcessDefException("WProcessDefDao: delete - Exception in deleting process rec "+ process.getName() +
//					" <id = "+process.getId()+ "> not stored \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );

		} 

	}

	public WProcessDef getWProcessDefByPK(Integer id) throws WProcessDefException {

		WProcessDef process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			process = (WProcessDef) session.get(WProcessDef.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDefDao: getWProcessDefByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao: getWProcessDefByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}
	
	
	public WProcessDef getWProcessDefByName(String name) throws WProcessDefException {

		WProcessDef  process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			process = (WProcessDef) session.createCriteria(WProcessDef.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDefDao: getWProcessDefByName - can't obtain process name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("getWProcessDefByName;  can't obtain process name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}

	
	public List<WProcessDef> getWProcessDefs() throws WProcessDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WProcessDef> processs = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processs = session.createQuery("From WProcessDef order by name ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDefDao: getWProcessDefs() - can't obtain process list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao: getWProcessDefs() - can't obtain process list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return processs;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String blank )
	throws WProcessDefException {
		 
			List<WProcessDef> lwpd = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwpd = session
						.createQuery("From WProcessDef order by name ")
						.list();
		
				if (lwpd!=null) {
					
					// inserta los extras
					if ( firstLineText!=null && !"".equals(firstLineText) ) {
						if ( !firstLineText.equals("WHITESPACE") ) {
							retorno.add(new StringPair(null,firstLineText));  // deja la primera línea con lo q venga
						} else {
							retorno.add(new StringPair(null," ")); // deja la primera línea en blanco ...
						}
					}
					
					if ( blank!=null && !"".equals(blank) ) {
						if ( !blank.equals("WHITESPACE") ) {
							retorno.add(new StringPair(null,blank));  // deja la separación línea con lo q venga
						} else {
							retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
						}
					}
					
				
					
					for (WProcessDef wpd: lwpd) {
						retorno.add(new StringPair(wpd.getId(),wpd.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WProcessDefException(
						"Can't obtain WProcessDefs combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;


	}

	// dml 20120120
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboActiveProcessList(String firstLineText, String blank )
	throws WProcessDefException {
		 
			List<WProcessDef> lwpd = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwpd = session
						.createQuery("From WProcessDef Where active IS TRUE order by name")
						.list();
		
				if (lwpd!=null) {
					
					// inserta los extras
					if ( firstLineText!=null && !"".equals(firstLineText) ) {
						if ( !firstLineText.equals("WHITESPACE") ) {
							retorno.add(new StringPair(null,firstLineText));  // deja la primera línea con lo q venga
						} else {
							retorno.add(new StringPair(null," ")); // deja la primera línea en blanco ...
						}
					}
					
					if ( blank!=null && !"".equals(blank) ) {
						if ( !blank.equals("WHITESPACE") ) {
							retorno.add(new StringPair(null,blank));  // deja la separación línea con lo q venga
						} else {
							retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
						}
					}
					
				
					
					for (WProcessDef wpd: lwpd) {
						retorno.add(new StringPair(wpd.getId(),wpd.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WProcessDefException(
						"Can't obtain WProcessDefs combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;


	}

	@SuppressWarnings("unchecked")
	public List<WProcessDef> getProcessListByFinder (Date initialInsertDateFilter, Date finalInsertDateFilter, 
			boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
			String listZoneFilter, String workZoneFilter, String additinalZoneFilter,
			Integer userId, boolean isAdmin ) 
					throws WProcessDefException {

			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;
			org.hibernate.Query q = null;
						
			SimpleDateFormat fmtShortDate = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat fmtLongDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			Date from = null;
			Date to = null;
			
			List<WProcessDef> lprocess = null;
			
			// build filter from user params. String and Integer values will be added to
			// the String directly in the string filter.
			// Date parameters must be added to hibernate query in the try / catch clause below
			String userFilter = getSQLFilter(initialInsertDateFilter, finalInsertDateFilter, 
										strictInsertDateFilter, nameFilter, commentFilter, 
										listZoneFilter, workZoneFilter, additinalZoneFilter);
			
			String requiredFilter = "";//getRequiredFilter(userId, isAdmin);
			
			String filter = userFilter + ( userFilter!=null && !"".equals(userFilter) 
								&& requiredFilter!=null && !"".equals(requiredFilter)?" AND ":"" ) 
								+ requiredFilter ;
			
			filter = (( filter != null && !"".equals(filter)) ? " WHERE ":"") + filter;
			
			System.out.println(" ---->>>>>>>>>> userFilter:["+userFilter+"]");
			System.out.println(" ---->>>>>>>>>> requiredFilter:["+requiredFilter+"]");
			System.out.println(" ---->>>>>>>>>> filter:["+filter+"]");
			
			// load base query phrase
			String query = getBaseQuery( isAdmin );
			
			System.out.println(" ---->>>>>>>>>> base query:["+query+"]");

			// builds full query phrase
			query += filter+getSQLOrder();

			System.out.println(" ---->>>>>>>>>> FULL query:["+query+"]");
			System.out.println(" ---->>>>>>>>>> userId: "+userId);

			
			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();

				tx.begin();
				
				q = session
						.createSQLQuery(query)
						.addEntity("WProcessDef", WProcessDef.class);

				// setting date parameters
				try {
					
					if (strictInsertDateFilter) {
		                from = fmtLongDate.parse(fmtShortDate.format(initialInsertDateFilter)+" 00:00:00");                
		                to = fmtLongDate.parse(fmtShortDate.format(initialInsertDateFilter)+" 23:59:59");                
						q.setParameter("insertdateFrom", from);
						q.setParameter("insertdateTo", to);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error setting date fields to hibernate SQL Query: "
							+ e.getMessage()+"\n"+e.getCause());	
				}
				
				// set userId
				//q.setInteger("userId",userId);
				
				// retrieve list
				lprocess = q.list();
				
				tx.commit();

			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				String message="WProcessDefDao: 002 getWProcessDefs() - can't obtain process list - " +
						ex.getMessage()+"\n"+ex.getCause();
				logger.warn(message );
				throw new WProcessDefException(message);

			} catch (Exception ex) {
				if (tx != null)
					tx.rollback();
				String message="WProcessDefDao: 002B getWProcessDefs() - can't obtain process list - " +
						ex.getMessage()+"\n"+ex.getCause();
				logger.warn(message );
				throw new WProcessDefException(message);
			}
			
			return lprocess;
		}

	private String getRequiredFilter ( Integer userId, boolean isAdmin ) {
		
		String reqFilter = " ( ( wsr.id_role in ";
		reqFilter +="(select wur.id_role from w_user_def wud, w_user_role wur where wur.id_user=:userId ) OR  ";
		reqFilter +=" ( wsu.id_user =:userId ) OR  ";
		reqFilter +=" ( wswa.id_role in ";
		reqFilter +="(select wur.id_role from w_user_def wud, w_user_role wur where wur.id_user=:userId )) OR  ";
		reqFilter +=" ( wswa.id_user =:userId )  ) ) ";
	
		return reqFilter;
	
	}

	private String getBaseQuery(boolean isAdmin) {
	
		String baseQueryTmp="SELECT * FROM w_process_def wpd ";
		baseQueryTmp +="LEFT JOIN w_step_def wsd ON wpd.id_begin=wsd.id ";
	
		return baseQueryTmp;

	}

	private String getFilter(Date initialInsertDateFilter, Date finalInsertDateFilter, 
		boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
		String listZoneFilter, String workZoneFilter, String additinalZoneFilter ) {

		String filter="";
	
		if ( nameFilter!=null && ! "".equals(nameFilter) ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" wpd.name = "+nameFilter;
		}

		if ( commentFilter!=null && ! "".equals(commentFilter) ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" comments = "+commentFilter;
		}

		if ( listZoneFilter!=null && ! "".equals(listZoneFilter) ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" idListZone = "+listZoneFilter;
		}

		if ( workZoneFilter!=null && ! "".equals(workZoneFilter) ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" idWorkZone = "+workZoneFilter;
		}

		if ( additinalZoneFilter!=null && ! "".equals(additinalZoneFilter) ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" idAdditionalZone = "+additinalZoneFilter;
		}

		if (initialInsertDateFilter!=null){

			if (strictInsertDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" wpd.insertDate >= :insertdateFrom ";
			} else {
				if (finalInsertDateFilter!=null){
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (wpd.insertDate >= :insertdateFrom AND wpd.insertDate <= :insertdateTo) ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" wpd.insertDate <= :insertdateTo ";
				}
			}
		}

		return filter;
	
	}

	private String getSQLFilter(Date initialInsertDateFilter, Date finalInsertDateFilter, 
			boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
			String listZoneFilter, String workZoneFilter, String additinalZoneFilter ) {

		String filter="";

	
		if (nameFilter!=null && ! "".equals(nameFilter)){
			if (filter == ""){
				filter+=" wpd.name LIKE '%"+nameFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wpd.name LIKE '%"+nameFilter.trim()+"%' ";
			}
		}
		
		if (commentFilter!=null && ! "".equals(commentFilter)){
			if (filter == ""){
				filter+=" wpd.comments LIKE '%"+commentFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wpd.comments LIKE '%"+commentFilter.trim()+"%' ";
			}
		}
	
		if (listZoneFilter!=null && ! "".equals(listZoneFilter)){
			if (filter == ""){
				filter+=" wpd.id_list_zone LIKE '%"+listZoneFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wpd.id_list_zone LIKE '%"+listZoneFilter.trim()+"%' ";
			}
		}
		
		if (workZoneFilter!=null && ! "".equals(workZoneFilter)){
			if (filter == ""){
				filter+=" wpd.id_work_zone LIKE '%"+workZoneFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wpd.id_work_zone LIKE '%"+workZoneFilter.trim()+"%' ";
			}
		}
		
		if (additinalZoneFilter!=null && ! "".equals(additinalZoneFilter)){
			if (filter == ""){
				filter+=" wpd.id_additional_zone LIKE '%"+additinalZoneFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wpd.id_additional_zone LIKE '%"+additinalZoneFilter.trim()+"%' ";
			}
		}
		
		if (initialInsertDateFilter!=null){
	
			java.sql.Date initialInsertDateFilterSQL=new java.sql.Date(initialInsertDateFilter.getTime());
			
			if (strictInsertDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" wpd.insert_date >= :insertdateFrom AND wpd.insert_date <= :insertdateTo ";
			} else {
				if (finalInsertDateFilter!=null){
					java.sql.Date finalInsertDateFilterSQL=new java.sql.Date(finalInsertDateFilter.getTime());
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (wpd.insert_date >= '"+initialInsertDateFilterSQL+"' AND wpd.insert_date <= '"+finalInsertDateFilterSQL+"') ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" wpd.insert_date >= '"+initialInsertDateFilterSQL+"' ";
				}
			}
		}
		
		return filter;
	}


	
	private String getOrder() {
	
		return " ORDER BY insertDate ";
		
	}



	private String getSQLOrder() {
	
		return " ORDER BY wpd.insert_date ";
		
	}
	
	// dml 20120118
	public List<WProcessDefLight> getWorkingProcessListByFinder(boolean onlyWorkingProcessesFilter, 
			String processNameFilter, Date initialProductionDateFilter, Date finalProductionDateFilter, 
			boolean estrictProductionDateFilter, Integer productionUserFilter, String action) 
	throws WProcessDefException {

		String filter = "";
		
		if (onlyWorkingProcessesFilter) {
			if (!"".equals(filter)) {
				filter += " AND wpd.active IS TRUE ";
			} else {
				filter += " wpd.active IS TRUE ";

			}
		}
		
		if (processNameFilter != null && !"".endsWith(processNameFilter)){
			if (!"".equals(filter)) {
				filter += " AND wpd.name LIKE '%"+processNameFilter+"%'";
			} else {
				filter += " wpd.name LIKE '%"+processNameFilter+"%'";

			}			
		}
		
		if (initialProductionDateFilter!=null){
			
			java.sql.Date initialProductionDateFilterSQL=new java.sql.Date(initialProductionDateFilter.getTime());
			
			if (estrictProductionDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" wpd.production_date = '"+initialProductionDateFilterSQL+"' ";
			} else {
				if (finalProductionDateFilter!=null){
					java.sql.Date finalProductionDateFilterSQL=new java.sql.Date(finalProductionDateFilter.getTime());
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (wpd.production_date >= '"+initialProductionDateFilterSQL+"' AND wpd.production_date <= '"+finalProductionDateFilterSQL+"') ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" wpd.production_date >= '"+initialProductionDateFilterSQL+"' ";
				}
			}
		}
		
		if (productionUserFilter != null && productionUserFilter != 0) {
			if (!"".equals(filter)) {
				filter += " AND wpd.production_user = "+productionUserFilter;
			} else {
				filter += " wpd.production_user = "+productionUserFilter;

			}
		}
		
		
		if (filter != null && !"".equals(filter)){
			filter = "WHERE " + filter;
		}

		logger.debug("------>> getWorkingProcessListByFinder -> filter:" + filter
				+ "<<-------");

		String query = _armaQueryWorkingProcessDefLight(filter, action);

		return getWorkingProcessListByFinder(query);
	}

	// dml 20120118
	private String _armaQueryWorkingProcessDefLight(String filter, String action) {

		String tmpQuery = "SELECT ";
		tmpQuery += " DISTINCT(wpd.id), ";
		tmpQuery += " wpd.name, ";
		tmpQuery += " wpd.comments, ";
		tmpQuery += " wpd.production_date, ";
		tmpQuery += " wpd.production_user, ";
		tmpQuery += " (SELECT COUNT(id) FROM w_step_work work2 WHERE work2.decided_date IS NULL AND work2.id_process = wpd.id) AS liveWorks, ";
		tmpQuery += " (SELECT COUNT(DISTINCT reference) FROM w_step_work work3 WHERE work3.decided_date IS NULL AND work3.id_process = wpd.id) liveSteps, ";
		tmpQuery += " wpd.active ";

		tmpQuery += " FROM w_process_def wpd ";
		tmpQuery += " LEFT OUTER JOIN w_step_work work ON work.id_process = wpd.id ";

		tmpQuery += filter;

		if (action == null || action.equals("")) {
			tmpQuery += " ORDER by wpd.id ASC;";
		}

		logger.debug("------>> getWorkingProcessListByFinder -> query:" + tmpQuery
				+ "<<-------");

		System.out.println("QUERY:" + tmpQuery);

		return tmpQuery;
	}

	// dml 20120118
	private List<WProcessDefLight> getWorkingProcessListByFinder(String query)
			throws WProcessDefException {

		Integer id;
		String name;
		String comments;
		Date productionDate;
		Integer productionUser;
		
		Integer liveWorks;
		Integer liveSteps;
		
		boolean status;
		
		
		Session session = null;
		Transaction tx = null;

		List<Object[]> result = null;
		List<WProcessDefLight> returnList = new ArrayList<WProcessDefLight>();

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


					id = (cols[0] != null ? new Integer(
							cols[0].toString()) : null);
					name = (cols[1] != null ? cols[1].toString() : "");
					comments = (cols[2] != null ? cols[2].toString() : "");
					productionDate = (cols[3] != null ? (Date) cols[3] : null);
					productionUser = (cols[4] != null ? new Integer(
							cols[4].toString()) : null);
					liveWorks = (cols[5] != null ? new Integer(
							cols[5].toString()) : null);
					liveSteps= (cols[6] != null ? new Integer(
							cols[6].toString()) : null);
					status = (cols[7] != null ? (Boolean) cols[7] : false);

					returnList.add(new WProcessDefLight(id, name, comments, productionDate, 
							productionUser, liveWorks, liveSteps, status));
				}

			} else {

				returnList = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDefDao: getWorkingProcessListByFinder() - It cannot be posible to get the WProcessDefLight list - "
					+ ex.getMessage()
					+ "\n"
					+ ex.getLocalizedMessage()
					+ " \n"
					+ ex.getCause());
			throw new WProcessDefException(ex);

		}

		return returnList;
	}

	// dml 20120118
	public List<WorkingProcessWork> getWorkingProcessWorkListByFinder(Integer idProcess, 
			boolean onlyActiveWorksFilter, Date initialStartedDateFilter, Date finalStartedDateFilter, 
			boolean estrictStartedDateFilter, Date initialFinishedDateFilter, Date finalFinishedDateFilter, 
			boolean estrictFinishedDateFilter, String action) throws WProcessDefException {

		String filter = "";

		if (idProcess != null && idProcess != 0) {
			if (!"".equals(filter)) {
				filter += " AND ";
			}
			filter += " wpd.id = " + idProcess;
		}
		
		if (onlyActiveWorksFilter) {
			if (!"".equals(filter)) {
				filter += " AND work.decided_date IS NULL ";
			} else {
				filter += " work.decided_date IS NULL ";

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

		if (filter != null && !"".equals(filter)){
			filter = "WHERE " + filter;
		}
		
		logger.debug("------>> getWorkingProcessWorkListByFinder -> filter:" + filter
				+ "<<-------");

		String query = _armaQueryWorkingProcessWork(idProcess, filter, action);

		return getWorkingProcessWorkListByFinder(query);
		
	}

	// dml 20120118
	private String _armaQueryWorkingProcessWork(Integer idProcess, String filter, String action) {

		
		String tmpQuery = "SELECT ";
		tmpQuery += " DISTINCT(work.reference), ";
		tmpQuery += " wpd.id, ";
		tmpQuery += " wpd.name, ";
		tmpQuery += " (SELECT COUNT(id) FROM w_step_work work2 WHERE work2.decided_date IS NULL AND work2.reference = work.reference ) AS liveSteps, ";
		tmpQuery += " (SELECT arriving_date FROM w_step_work WHERE reference = work.reference AND id_previous_step IS NULL ) AS arriving_date, ";
		tmpQuery += " IF(((SELECT count(id) FROM w_step_work work2 WHERE work2.decided_date IS NULL AND work2.reference = work.reference)) > 0, 'running', 'finished') AS status, ";
		tmpQuery += " (SELECT max(decided_date) FROM w_step_work WHERE reference = work.reference AND (SELECT COUNT(id) FROM w_step_work WHERE decided_date IS NULL AND reference = work.reference) = 0 ) AS finished_date, ";
		tmpQuery += " work.comments ";

		tmpQuery += " FROM w_step_work work ";
		tmpQuery += " LEFT OUTER JOIN w_process_def wpd ON wpd.id=work.id_process ";

		tmpQuery += filter;

		if (action == null || action.equals("")) {
			tmpQuery += " ORDER by wpd.id ASC;";
		} 

		logger.debug("------>> getWorkingProcessWorkListByFinder -> query:" + tmpQuery
				+ "<<-------");

		System.out.println("QUERY:" + tmpQuery);

		return tmpQuery;
	}

	// dml 20120118
	private List<WorkingProcessWork> getWorkingProcessWorkListByFinder(String query)
			throws WProcessDefException {

		Integer idProcess;
		String processName;
		String workReference;
		String workComments;

		Integer liveSteps;
		
		Date started;
		
		String status;
		
		Date finished;
		
		
		Session session = null;
		Transaction tx = null;

		List<Object[]> result = null;
		List<WorkingProcessWork> returnList = new ArrayList<WorkingProcessWork>();

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


					workReference = (cols[0] != null ? cols[0].toString() : "");
					idProcess = (cols[1] != null ? new Integer(
							cols[1].toString()) : null);
					processName = (cols[2] != null ? cols[2].toString() : "");
					liveSteps = (cols[3] != null ? new Integer(
							cols[3].toString()) : null);
					started = (cols[4] != null ? (Date) cols[4] : null);
					status = (cols[5] != null ? cols[5].toString() : "");
					finished = (cols[6] != null ? (Date) cols[6] : null);
					workComments = (cols[7] != null ? cols[7].toString() : "");

					returnList.add(new WorkingProcessWork(idProcess, processName, 
							workReference, workComments, liveSteps, started, status, finished));
				}

			} else {

				returnList = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
				logger.warn("WProcessDefDao: getWorkingProcessWorkListByFinder() - " +
						"It cannot be posible to get the WorkingProcessWork list - "
					+ ex.getMessage() + "\n" + ex.getLocalizedMessage() + " \n" + ex.getCause());
			throw new WProcessDefException(ex);

		}

		return returnList;
	}
	
	// dml 20120118
	public List<WorkingProcessStep> getWorkingProcessStepListByFinder(Integer processIdFilter, 
			Integer stepIdFilter, String stepTypeFilter, String referenceFilter, 
			Date initialArrivingDateFilter, Date finalArrivingDateFilter, boolean estrictArrivingDateFilter,  		
			Date initialOpenedDateFilter, Date finalOpenedDateFilter, boolean estrictOpenedDateFilter, 		
			Date initialDeadlineDateFilter, Date finalDeadlineDateFilter, boolean estrictDeadlineDateFilter, 		
			Date initialDecidedDateFilter, Date finalDecidedDateFilter, boolean estrictDecidedDateFilter, 		
			String action) 
					throws WProcessDefException {
		
		String filter = "";

		if (processIdFilter != null && processIdFilter != 0) {
			if (!"".equals(filter)) {
				filter += " AND ";
			}
			filter += " work.id_process = " + processIdFilter;
		}
		
		if (stepIdFilter != null && stepIdFilter != 0) {
			if (!"".equals(filter)) {
				filter += " AND ";
			}
			filter += " work.id_current_step = " + stepIdFilter;
		}
		
		if (!"".equals(stepTypeFilter) || !"ALL".equals(stepTypeFilter)) {
			if ("PENDING".equals(stepTypeFilter)) {
				if (!"".equals(filter)) {
					filter += " AND ";
				}
				filter += " work.decided_date IS NOT NULL ";				
			} else if ("PROCESSED".equals(stepTypeFilter)){
				if (!"".equals(filter)) {
					filter += " AND ";
				}
				filter += " work.decided_date IS NULL ";				
			}
		}
		
		if (referenceFilter != null && !"".equals(referenceFilter)) {
			if (!"".equals(filter)) {
				filter += " AND work.reference LIKE '%"+referenceFilter+"%'";
			} else {
				filter += " work.reference LIKE '%"+referenceFilter+"%'";

			}
		}
		
		if (initialArrivingDateFilter!=null){
			
			java.sql.Date initialArrivingDateFilterSQL=new java.sql.Date(initialArrivingDateFilter.getTime());
			
			if (estrictArrivingDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" work.arriving_date = '"+initialArrivingDateFilterSQL+"' ";
			} else {
				if (finalArrivingDateFilter!=null){
					java.sql.Date finalArrivingDateFilterSQL=new java.sql.Date(finalArrivingDateFilter.getTime());
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (work.arriving_date >= '"+initialArrivingDateFilterSQL+"' AND work.arriving_date <= '"+finalArrivingDateFilterSQL+"') ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" work.arriving_date >= '"+initialArrivingDateFilterSQL+"' ";
				}
			}
		}

		if (initialOpenedDateFilter!=null){
			
			java.sql.Date initialOpenedDateFilterSQL=new java.sql.Date(initialOpenedDateFilter.getTime());
			
			if (estrictOpenedDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" work.opened_date = '"+initialOpenedDateFilterSQL+"' ";
			} else {
				if (finalOpenedDateFilter!=null){
					java.sql.Date finalOpenedDateFilterSQL=new java.sql.Date(finalOpenedDateFilter.getTime());
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (work.opened_date >= '"+initialOpenedDateFilterSQL+"' AND work.opened_date <= '"+finalOpenedDateFilterSQL+"') ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" work.opened_date >= '"+initialOpenedDateFilterSQL+"' ";
				}
			}
		}

		if (initialDeadlineDateFilter!=null){
			
			java.sql.Date initialDeadlineDateFilterSQL=new java.sql.Date(initialDeadlineDateFilter.getTime());
			
			if (estrictDeadlineDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" work.deadline_date = '"+initialDeadlineDateFilterSQL+"' ";
			} else {
				if (finalDeadlineDateFilter!=null){
					java.sql.Date finalDeadlineDateFilterSQL=new java.sql.Date(finalDeadlineDateFilter.getTime());
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (work.deadline_date >= '"+initialDeadlineDateFilterSQL+"' AND work.deadline_date <= '"+finalDeadlineDateFilterSQL+"') ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" work.deadline_date >= '"+initialDeadlineDateFilterSQL+"' ";
				}
			}
		}

		if (initialDecidedDateFilter!=null){
			
			java.sql.Date initialDecidedDateFilterSQL=new java.sql.Date(initialDecidedDateFilter.getTime());
			
			if (estrictDecidedDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" work.decided_date = '"+initialDecidedDateFilterSQL+"' ";
			} else {
				if (finalDecidedDateFilter!=null){
					java.sql.Date finalDecidedDateFilterSQL=new java.sql.Date(finalDecidedDateFilter.getTime());
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (work.decided_date >= '"+initialDecidedDateFilterSQL+"' AND work.decided_date <= '"+finalDecidedDateFilterSQL+"') ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" work.decided_date >= '"+initialDecidedDateFilterSQL+"' ";
				}
			}
		}

		if (filter != null && !"".equals(filter)){
			filter = "WHERE " + filter;
		}
		
		logger.debug("------>> getWorkingProcessListStepByFinder -> filter:" + filter
				+ "<<-------");

		String query = _armaQueryWorkingProcessStep(filter, action);

		return getWorkingProcessStepListByFinder(query);
		
	}

	// dml 20120118
	private String _armaQueryWorkingProcessStep(String filter, String action) {

		String tmpQuery = "SELECT ";
		tmpQuery += " work.id_process, ";
		tmpQuery += " work.id_current_step, ";
		tmpQuery += " step.name, ";
		tmpQuery += " work.arriving_date, ";
		tmpQuery += " work.opened_date, ";
		tmpQuery += " work.opener_user, ";
		tmpQuery += " work.decided_date, ";
		tmpQuery += " work.performer_user_id, ";
		tmpQuery += " work.deadline_date, ";
		tmpQuery += " work.deadline_time, ";
		tmpQuery += " work.reference ";

		tmpQuery += " FROM w_step_work work ";
		tmpQuery += " LEFT OUTER JOIN w_step_def step ON step.id = work.id_current_step ";

		tmpQuery += filter;

		if (action == null || action.equals("")) {
			tmpQuery += " ORDER by work.id_process ASC;";
		} 

		logger.debug("------>> getWorkingProcessStepListByFinder -> query:" + tmpQuery
				+ "<<-------");

		System.out.println("QUERY:" + tmpQuery);

		return tmpQuery;
	}

	// dml 20120118
	private List<WorkingProcessStep> getWorkingProcessStepListByFinder(String query)
			throws WProcessDefException {

		Integer idProcess;
		Integer idStep;
		String stepName;
		Date arrivingDate;
		Date openedDate;
		Integer openerUser;
		Date decidedDate;
		Integer performer;
		Date deadlineDate;
		Date deadlineTime;
		String workReference;
		
		
		Session session = null;
		Transaction tx = null;

		List<Object[]> result = null;
		List<WorkingProcessStep> returnList = new ArrayList<WorkingProcessStep>();

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
					idStep = (cols[1] != null ? new Integer(
							cols[1].toString()) : null);
					stepName = (cols[2] != null ? cols[2].toString() : "");
					arrivingDate = (cols[3] != null ? (Date) cols[3] : null);
					openedDate = (cols[4] != null ? (Date) cols[4] : null);
					openerUser = (cols[5] != null ? new Integer(
							cols[5].toString()) : null);
					decidedDate = (cols[6] != null ? (Date) cols[6] : null);
					performer = (cols[7] != null ? new Integer(
							cols[7].toString()) : null);
					deadlineDate = (cols[8] != null ? (Date) cols[8] : null);
					deadlineTime = (cols[9] != null ? (Date) cols[9] : null);
					workReference = (cols[10] != null ? cols[10].toString() : "");

					returnList.add(new WorkingProcessStep(idProcess, idStep, stepName, 
							workReference, arrivingDate, openedDate, openerUser, decidedDate, 
							performer, deadlineDate, deadlineTime));
				}

			} else {

				returnList = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
				logger.warn("WProcessDefDao: getWorkingProcessStepListByFinder() - " +
						"It cannot be posible to get the WorkingProcessStep list - "
					+ ex.getMessage() + "\n" + ex.getLocalizedMessage() + " \n" + ex.getCause());
			throw new WProcessDefException(ex);

		}

		return returnList;
	}


}
	