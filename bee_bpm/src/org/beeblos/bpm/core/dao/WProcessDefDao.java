package org.beeblos.bpm.core.dao;

import static org.beeblos.bpm.core.util.Constants.ACTIVE;
import static org.beeblos.bpm.core.util.Constants.INACTIVE;
import static org.beeblos.bpm.core.util.Constants.LAST_W_PROCESS_DEF_ADDED;
import static org.beeblos.bpm.core.util.Constants.LAST_W_PROCESS_DEF_MODIFIED;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import com.sp.common.util.StringPair;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class WProcessDefDao {
	
	private static final Log logger = LogFactory.getLog(WProcessDefDao.class.getName());
	
	public WProcessDefDao (){
		
	}
	
	public Integer add(WProcessDef process, Integer currentUserId) throws WProcessDefException {
		
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
	
	
	public void update(WProcessDef process, Integer currentUserId) throws WProcessDefException {
		
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
	
	// dml 20130703
	public void updateProcessXmlMap(Integer processId, String processMap, Integer modUserId,
			Date modDate) throws WProcessDefException {

		logger.debug("updateProcessXmlMap() WProcessDef < id = " + processId + ">");

		try {

			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			session.createQuery(
					"UPDATE WProcessDef SET processMap = :processMap, modUser = :modUserId, modDate = :modDate WHERE id = :processId")
					.setString("processMap", processMap)
					.setInteger("modUserId", modUserId)
					.setInteger("processId", processId)
					.setParameter("modDate", modDate)
					.executeUpdate();

			tx.commit();

		} catch (HibernateException ex) {
			String message = "SlaDao: update - Can't update xml map for process " + processId
					+ " - and processMap = " + processMap + "\n - " + ex.getMessage() + "\n"
					+ ex.getCause();
			logger.error(message);
			throw new WProcessDefException(message);

		}

	}

	public void delete(WProcessDef process, Integer currentUserId) throws WProcessDefException {

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

	public WProcessDef getWProcessDefByPK(Integer id, Integer currentUserId) throws WProcessDefException {

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
	
/*	
	public WProcessDef getWProcessDefByName(String name, Integer currentUserId) throws WProcessDefException {

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
*/
	
	// versionId = id from WProcessDef table
	public String getProcessNameByVersionId(Integer versionId, Integer currentUserId) throws WProcessDefException {

		String name = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			name = (String) session
					.createQuery("Select WProcessDef.process.name from WProcessDef where WProcessDef.id = :id")
						.setInteger("id",versionId)
						.uniqueResult();


			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDefDao: getWProcessDefByPK - we can't obtain the required id = "+
					versionId + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao: getWProcessDefByPK - we can't obtain the required id : " + 
					versionId + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return name;
	}
	
	// dml 20130430
	public Integer getLastVersionNumber(Integer processHeadId, Integer currentUserId) throws WProcessDefException {
	
		Integer version = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			version = (Integer) session.createSQLQuery("SELECT MAX(version) FROM w_process_def WHERE head_id = " + processHeadId)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDefDao: getLastWProcessDefVersion - can't obtain process last version = " +
					processHeadId + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("getLastWProcessDefVersion;  can't obtain process last version: " + 
					processHeadId + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		if (version == null){
			return 0;
		}
		
		return version;
	}	
	
	// dml 20130710
	public String getProcessDefXmlMap(Integer processDefId, Integer currentUserId) throws WProcessDefException {
	
		String processMap = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processMap = (String) session.createQuery("Select processMap From WProcessDef Where id = :processDefId")
					.setInteger("processDefId", processDefId)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDefDao: getProcessDefXmlMap - can't obtain process xml map = " +
					processDefId + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("getLastWProcessDefVersion;  can't obtain process xml map: " + 
					processDefId + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return processMap;
	}	
	
	public List<WProcessDef> getWProcessDefs(Integer currentUserId) throws WProcessDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WProcessDef> processList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processList = session.createQuery("From WProcessDef Order By process.name ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDefDao: getWProcessDefs() - can't obtain process list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao: getWProcessDefs() - can't obtain process list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return processList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String blank, Integer currentUserId )
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
						.createQuery("From WProcessDef Order By process.name ")
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
	public List<StringPair> getComboActiveProcessList(String firstLineText, String blank, Integer currentUserId )
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
						.createQuery("From WProcessDef Where active IS TRUE order by process.name")
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
			Integer userId, boolean isAdmin, String action, Integer currentUserId ) 
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
			query += filter+getSQLOrder(action);

			logger.debug(" ---->>>>>>>>>> FULL query:["+query+"] ---->>>>>>>>>> userId: "+userId);
			
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
		baseQueryTmp += " LEFT OUTER JOIN w_process_head wph ON wpd.head_id = wph.id ";
		baseQueryTmp +=" LEFT JOIN w_step_def wsd ON wpd.id_begin=wsd.id ";
	
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
			filter +=" wph.name = "+nameFilter;
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
				filter+=" wph.name LIKE '%"+nameFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wph.name LIKE '%"+nameFilter.trim()+"%' ";
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

	// dml 20120416
	private String getSQLOrder(String action) {
		
		String ret = "";
		
		if (action==null || action.equals("")) {
			
			ret = "";
			
		} else if (action.equals(LAST_W_PROCESS_DEF_ADDED)) {
			
			ret = " ORDER by wpd.insert_date DESC ";
			
		} else if (action.equals(LAST_W_PROCESS_DEF_MODIFIED)) {
			
			ret = " ORDER by wpd.mod_date DESC ";
			
		}
		
		return ret;
		
	}

	public List<WProcessDefLight> getWorkingProcessListFinder(boolean onlyActiveWorkingProcessesFilter, 
			String processNameFilter, Date initialProductionDateFilter, Date finalProductionDateFilter, 
			boolean estrictProductionDateFilter, Integer productionUserFilter, String action, 
			Integer processHeadId, String activeFilter, Integer currentUserId) 
	throws WProcessDefException {

		String filter = "";
		
		filter = buildWorkingProcessFilter(onlyActiveWorkingProcessesFilter,
				processNameFilter, initialProductionDateFilter,
				finalProductionDateFilter, estrictProductionDateFilter,
				productionUserFilter, filter, processHeadId, activeFilter);
		
		
		if (filter != null && !"".equals(filter)){
			filter = "WHERE " + filter;
		}

		String query = buildWorkingProcessQuery(filter, action);
		

		logger.debug("------>> getWorkingProcessListFinder -> query:" + query
				+ "<<-------");

		return getWorkingProcessListByFinder(query, currentUserId);
	}

	private String buildWorkingProcessFilter(
			boolean onlyActiveWorkingProcessesFilter, String processNameFilter,
			Date initialProductionDateFilter, Date finalProductionDateFilter,
			boolean estrictProductionDateFilter, Integer productionUserFilter,
			String filter, Integer processHeadId, String activeFilter) {
		
		if (onlyActiveWorkingProcessesFilter) {
			if (!"".equals(filter)) {
				filter += " AND wpd.active IS TRUE ";
			} else {
				filter += " wpd.active IS TRUE ";

			}
		}
		
		if (processNameFilter != null && !"".endsWith(processNameFilter)){
			if (!"".equals(filter)) {
				filter += " AND wph.name LIKE '%"+processNameFilter+"%'";
			} else {
				filter += " wph.name LIKE '%"+processNameFilter+"%'";

			}			
		}
		
		if (initialProductionDateFilter!=null){
			
			java.sql.Date initialProductionDateFilterSQL=new java.sql.Date(initialProductionDateFilter.getTime());
			
			if (estrictProductionDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" wpd.production_date >= '"+initialProductionDateFilterSQL+" 00:00:00' ";
				filter+=" AND wpd.production_date <= '"+initialProductionDateFilterSQL+" 23:59:59' ";
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

		// dml 20130508
		if (activeFilter != null
				&& (activeFilter.equals(ACTIVE)
				|| activeFilter.equals(INACTIVE))) {
			if (!"".equals(filter)) {
				filter += " AND ";
			}
			if (activeFilter.equals(ACTIVE)) {
				filter += " wpd.active IS TRUE ";
			} else if (activeFilter.equals(INACTIVE)){
				filter += " wpd.active IS FALSE ";

			}
		}
		
		// dml 20130508
		if (processHeadId != null
				&& !processHeadId.equals(0)) {
			if (!"".equals(filter)) {
				filter += " AND wpd.head_id = " + processHeadId + " ";
			} else {
				filter += " wpd.head_id = " + processHeadId + " ";

			}
		}
		
		System.out.println("QUERY FILTER:" + filter);

		return filter;
	}

	private String buildWorkingProcessQuery(String filter, String action) {

		String tmpQuery = "SELECT ";
		tmpQuery += " wpd.id, ";
		tmpQuery += " wph.name, ";
		tmpQuery += " wpd.comments, ";
		tmpQuery += " wpd.production_date, ";
		tmpQuery += " wpd.production_user, ";
		tmpQuery += " (SELECT COUNT(id) FROM w_process_work pw WHERE pw.end_time IS NULL AND pw.id_process = wpd.id) AS liveWorks, ";
		tmpQuery += " (SELECT COUNT(id) FROM w_step_work sw WHERE sw.decided_date IS NULL AND sw.id_process = wpd.id) AS liveSteps, ";
		tmpQuery += " wpd.active, ";
		tmpQuery += " wpd.version ";

		tmpQuery += " FROM w_process_def wpd ";
		tmpQuery += " LEFT OUTER JOIN w_process_head wph ON wpd.head_id = wph.id ";

		tmpQuery += filter;

		if (action == null || action.equals("")) {
			tmpQuery += " ORDER by wpd.id ASC;";
		}

		System.out.println("QUERY:" + tmpQuery);

		return tmpQuery;
	}

	private List<WProcessDefLight> getWorkingProcessListByFinder(String query, Integer currentUserId)
			throws WProcessDefException {

		Integer id;
		String name;
		String comments;
		Date productionDate;
		Integer productionUser;
		
		Integer liveWorks;
		Integer liveSteps;
		
		boolean status;
		Integer version;
		
		
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
					version= (cols[8] != null ? new Integer(cols[8].toString()) : null);

					returnList.add(new WProcessDefLight(id, name, comments, productionDate, 
							productionUser, liveWorks, liveSteps, status, version));
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
	
	// dml 20130129
	public boolean userIsProcessAdmin(Integer userId, Integer processId, Integer currentUserId)
			throws WProcessDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		String result = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			String query = " SELECT ";
			query += " IF (" + userId + " IN ";
			query += " (SELECT id_user FROM w_process_user WHERE id_process = " + processId
					+ " AND admin=true )";
			query += " OR ";
			query += " (SELECT COUNT(*) FROM w_process_role pr LEFT JOIN w_user_role ur ON pr.id_role=ur.id_role";
			query += " WHERE pr.admin = true AND ur.id_user= " + userId
					+ " ), 'YES','NO' ) as ADMINISTRATOR ";

			result = (String) session.createSQLQuery(query).uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDefDao: userIsProcessAdmin() - can't obtain process list - "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new WProcessDefException(
					"WProcessDefDao: userIsProcessAdmin() - can't obtain process list: "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

		if (result != null && !"".equals(result)) {
			if (result.equals("YES")) {
				return true;
			} else if (result.equals("NO")) {
				return false;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

}
	