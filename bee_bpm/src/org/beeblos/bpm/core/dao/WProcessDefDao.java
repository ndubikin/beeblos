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
	public List<WProcessDefLight> getWorkingProcessListByFinder(String action, String filter1) throws WProcessDefException {

		String filter = "";
/*
		if (idEstadoRequerido != null) {
			if (!"".equals(filtro)) {
				filtro += " AND p.id_estado = ";
			} else {
				filtro += " p.id_estado = ";

			}
			if (idEstadoRequerido.size() != 1) {
				for (int i = 0; i < idEstadoRequerido.size() - 1; i++) {
					filtro += idEstadoRequerido.get(i) + " or p.id_estado = ";
				}
			}
			filtro += idEstadoRequerido.get(idEstadoRequerido.size() - 1) + " ";

		}

		if (proveedorNombre != null && !"".equals(proveedorNombre)) {
			if (!"".equals(filtro)) {
				filtro += " AND ";
			}
			filtro += " prov.proveedor_nombre LIKE '%" + proveedorNombre.trim()
					+ "%' ";
		}

		// rrl 20111010
		if (idProveedor != null && !idProveedor.equals(0)) {
			if (!"".equals(filtro)) {
				filtro += " AND ";
			}
			filtro += " p.id_proveedor = " + idProveedor.toString();
		}

		if (idDepartamento != null && !idDepartamento.equals(0)) {
			if (!"".equals(filtro)) {
				filtro += " AND ";
			}
			filtro += " p.id_departamento = " + idDepartamento.toString();
		}

		if (fechaEntradaInicial != null) {

			java.sql.Date fechaEntradaInicialSQL = new java.sql.Date(
					fechaEntradaInicial.getTime());

			if (estrictoFechaEntrada) {
				if (!"".equals(filtro)) {
					filtro += " AND ";
				}
				filtro += " p.fecha_entrada = '" + fechaEntradaInicialSQL
						+ "' ";
			} else {
				if (fechaEntradaFinal != null) {
					java.sql.Date fechaEntradaFinalSQL = new java.sql.Date(
							fechaEntradaFinal.getTime());
					if (!"".equals(filtro)) {
						filtro += " AND ";
					}
					filtro += " (p.fecha_entrada >= '" + fechaEntradaInicialSQL
							+ "' AND p.fecha_entrada <= '"
							+ fechaEntradaFinalSQL + "') ";
				} else {
					if (!"".equals(filtro)) {
						filtro += " AND ";
					}
					filtro += " p.fecha_entrada >= '" + fechaEntradaInicialSQL
							+ "' ";
				}
			}
		}
*/

		logger.debug("------>> getWorkingProcessListByFinder -> filter:" + filter
				+ "<<-------");

		String query = _armaQueryWorkingProcessDefLight(filter, action);

		return getWorkingProcessListByFinder(query);
	}

	private String _armaQueryWorkingProcessDefLight(String filter, String action) {

		String tmpQuery = "SELECT ";
		tmpQuery += " wpd.id, ";
		tmpQuery += " wpd.name, ";
		tmpQuery += " wpd.production_date, ";
		tmpQuery += " wpd.production_user, ";
		tmpQuery += " liveWorks, ";
		tmpQuery += " liveSteps, ";
		tmpQuery += " wpd.active ";

		tmpQuery += " FROM w_process_def wpd ";
		tmpQuery += " LEFT OUTER JOIN (SELECT id_process, COUNT(distinct reference) as liveWorks FROM w_step_work WHERE decided_date IS NULL) w ON w.id_process = wpd.id ";
		tmpQuery += " LEFT OUTER JOIN (SELECT id_process, count(id) as liveSteps FROM w_step_work WHERE decided_date IS NULL) s ON s.id_process = wpd.id ";

		tmpQuery += filter;

		if (action == null || action.equals("")) {
			tmpQuery += " ORDER by wpd.id ASC;";
		}

		logger.debug("------>> getWorkingProcessListByFinder -> query:" + tmpQuery
				+ "<<-------");

		System.out.println("QUERY:" + tmpQuery);

		return tmpQuery;
	}

	private List<WProcessDefLight> getWorkingProcessListByFinder(String query)
			throws WProcessDefException {

		Integer id;
		String name;
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
					productionDate = (cols[2] != null ? (Date) cols[2] : null);
					productionUser = (cols[3] != null ? new Integer(
							cols[3].toString()) : null);
					liveWorks = (cols[4] != null ? new Integer(
							cols[4].toString()) : null);
					liveSteps= (cols[5] != null ? new Integer(
							cols[5].toString()) : null);
					status = (cols[6] != null ? (Boolean) cols[6] : false);

					returnList.add(new WProcessDefLight(id, name, productionDate, 
							productionUser, liveWorks, liveSteps, status));
				}

			} else {
				// Si el select devuelve null entonces devuelvo null
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
			boolean onlyActiveWorksFilter, String action, String filter1) throws WProcessDefException {

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
		
		if (filter != null && !"".equals(filter)){
			filter = "WHERE " + filter;
		}
		
		logger.debug("------>> getWorkingProcessListByFinder -> filter:" + filter
				+ "<<-------");

		String query = _armaQueryWorkingProcessWork(idProcess, filter, action);

		return getWorkingProcessWorkListByFinder(query);
		
	}

	private String _armaQueryWorkingProcessWork(Integer idProcess, String filter, String action) {

		
		String tmpQuery = "SELECT ";
		tmpQuery += " DISTINCT(work.reference), ";
		tmpQuery += " wpd.id, ";
		tmpQuery += " wpd.name, ";
		tmpQuery += " (SELECT COUNT(id) FROM w_step_work work2 WHERE work2.decided_date IS NULL AND work2.reference = work.reference ) AS liveSteps, ";
		tmpQuery += " (SELECT arriving_date FROM w_step_work WHERE reference = work.reference AND id_previous_step IS NULL ) AS arrivingDate, ";
		tmpQuery += " IF(((SELECT count(id) FROM w_step_work work2 WHERE work2.decided_date IS NULL AND work2.reference = work.reference)) > 0, 'running', 'finished') AS status, ";
		tmpQuery += " (SELECT max(decided_date) FROM w_step_work WHERE reference = work.reference AND (SELECT COUNT(id) FROM w_step_work WHERE decided_date IS NULL AND reference = work.reference) = 0 ) AS finishedDate ";

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

	private List<WorkingProcessWork> getWorkingProcessWorkListByFinder(String query)
			throws WProcessDefException {

		Integer idProcess;
		String processName;
		String workReference;

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

					returnList.add(new WorkingProcessWork(idProcess, processName, 
							workReference, liveSteps, started, status, finished));
				}

			} else {
				// Si el select devuelve null entonces devuelvo null
				returnList = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
				logger.warn("WProcessDefDao: getWorkingProcessListByFinder() - " +
						"It cannot be posible to get the WorkingProcessWork list - "
					+ ex.getMessage() + "\n" + ex.getLocalizedMessage() + " \n" + ex.getCause());
			throw new WProcessDefException(ex);

		}

		return returnList;
	}
	
	public List<WorkingProcessStep> getWorkingProcessStepListByFinder(Integer idProcess, 
			boolean onlyActiveWorksFilter, String action, String filter1) throws WProcessDefException {
		
		return null;
		
	}


}
	