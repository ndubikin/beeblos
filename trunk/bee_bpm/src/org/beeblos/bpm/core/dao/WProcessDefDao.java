package org.beeblos.bpm.core.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
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
/*
	public List<WProcessDef> finder(Date initialInsertDateFilter, Date finalInsertDateFilter, 
			boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
			String listZoneFilter, String workZoneFilter, String additinalZoneFilter)
	throws WProcessDefException {
		
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
				filter+=" wpd.insert_date = '"+initialInsertDateFilterSQL+"' ";
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
		
		if (filter!=null && !"".equals(filter)) {
			filter= " WHERE "+filter;
		}

		logger.debug("002 ------>> finderWProcessDef -> filter:"+filter+"<<-------");
		
		String query = _armaQuery(filter);

		return finder(query);
	
	}
	
	public List<WProcessDef> finder(String query) throws WProcessDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Object[]> result = null;
		List<WProcessDef> returnList = new ArrayList<WProcessDef>();
		
		Integer id, idBegin, idInsertUser, idModUser;
		String name, idListZone, idWorkZone, idAdditionalZone, comments;
		Date insertDate, modDate = new Date();

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			Hibernate.initialize(result); // nes 20100821
	
			result = session
			.createSQLQuery(query)
			.list();

			tx.commit();
	
			WStepDef wsd = null;
			WProcessDef wpd = null;
	
			if (result!=null) {
				for (Object irObj: result) {
			
					Object [] cols= (Object []) irObj;
			
					id = (cols[0]!=null ? new Integer(cols[0].toString()):null);
					
					name = (cols[1]!=null ? cols[1].toString():"");
					comments = (cols[2]!=null ? cols[2].toString():"");

					idBegin = (cols[3]!=null ? new Integer(cols[3].toString()):null);
					idListZone = (cols[4]!=null ? cols[4].toString():"");
					idWorkZone = (cols[5]!=null ? cols[5].toString():"");
					idAdditionalZone = (cols[6]!=null ? cols[6].toString():"");
			
					insertDate = (cols[7]!=null ? (Date)cols[7]:null);
					idInsertUser = (cols[8]!=null ? new Integer(cols[8].toString()):null);

					modDate = (cols[9]!=null ? (Date)cols[9]:null);
					idModUser = (cols[10]!=null ? new Integer(cols[10].toString()):null);
			
					wsd = new WStepDef();
					wsd.setId(idBegin);
					wpd = new WProcessDef(name, comments, wsd, idListZone, idWorkZone, idAdditionalZone, insertDate, idInsertUser, modDate, idModUser);
					wpd.setId(id);
					
					returnList.add(wpd);
		
				}
		
			} else {
				
				returnList=null;
				
			}

		} catch (HibernateException ex) {
			
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDefDAO: finder() - It cannot be possible get the WProcessDef list - "+
					ex.getMessage()+ "\n"+ex.getLocalizedMessage()+" \n"+ex.getCause());
			throw new WProcessDefException(ex);

		} catch (Exception e) {
			
			logger.warn("Exception WProcessDefDAO: finderWProcessDef() - an error managing the WProcessDef list has happened - "+
					e.getMessage()+ "\n"+e.getLocalizedMessage()+" \n"+e.getCause());
			e.getStackTrace();
			throw new WProcessDefException(e);
			
		}

		return returnList;

	}



	private String _armaQuery(String filter){

		String tmpQuery = "SELECT wpd.id, wpd.name, wpd.comments, wpd.id_begin , wpd.id_list_zone,";
		tmpQuery += " wpd.id_work_zone, wpd.id_additional_zone, wpd.insert_date, wpd.insert_user,";
		tmpQuery += " wpd.mod_date, wpd.mod_user  ";
		tmpQuery += " FROM w_process_def wpd ";
		tmpQuery += filter;
		tmpQuery += " ORDER by wpd.id;";

		logger.debug("------>> finderWProcessDef -> query:"+tmpQuery+"<<-------");
	
		System.out.println("QUERY: "+tmpQuery);

		return tmpQuery;

	}

*/	
	
	
	
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


//	// nes 20101001 - hasta el final ...
//	public List<WProcessDefAmpliada> finderWProcessDef(
//			String filtroNombreWProcessDef, Integer processAnio
//	) throws WProcessDefException {
//		
//		
//		String estado="";
//		String filtro="";
//
//		
//		if (filtroNombreWProcessDef!=null && ! "".equals(filtroNombreWProcessDef)){
//			filtro=" c.process_nombre LIKE '%"+filtroNombreWProcessDef.trim()+"%' ";
//		} else 	{ 
//			filtro="";
//		}
//
//		if (processAnio!=null && processAnio>0){
//			if (!"".equals(filtro)) {
//				filtro+=" AND c.process_anio="+processAnio;
//			} else {
//				filtro+=" c.process_anio="+processAnio;
//			}
//		}
//		
//		
////		// el año de la process
////		if (processAnio!=null && !"".equals(processAnio)){
////			if (!"".equals(filtro)) {
////				filtro+=" AND c.process_anio='"+processAnio+"' ";
////			} else {
////				filtro+=" c.process_anio='"+processAnio+"' ";
////			}
////		}
//		
//		if (filtro!=null && !"".equals(filtro)) {
//			filtro= " WHERE "+filtro;
//		}
//
//		logger.debug("002 ------>> finderWProcessDef -> filtro:"+filtro+"<<-------");
//		
//		String query = _armaQuery(filtro);
//
//		return finderWProcessDef(query);
//
//	}
	

	

//	public List<WProcessDefAmpliada> finderWProcessDef(
//			String query
//	) throws WProcessDefException {
//		
//		
//		org.hibernate.Session session = null;
//		org.hibernate.Transaction tx = null;
//		
//		List<Object[]> resultado = null;
//		List<WProcessDefAmpliada> retorno = new ArrayList<WProcessDefAmpliada>();
//		List<ProyectoResumido> lpr = new ArrayList<ProyectoResumido>();
//
//		ProyectoDao pdao = new ProyectoDao();
//		
//		Integer idWProcessDef, processAnio;
//		String processNombre, processComentarios;
//		Date processFechaApertura, processFechaCierre = new Date();
//		
//		
//		try {
//
//			session = HibernateUtil.obtenerSession();
//			tx = session.getTransaction();
//			tx.begin();
//
//			Hibernate.initialize(resultado); // nes 20100821
//			
//			resultado = session
//					.createSQLQuery(query)
//					.list();
//
//			tx.commit();
//			
//			if (resultado!=null) {
//				for (Object irObj: resultado) {
//					
//					Object [] cols= (Object []) irObj;
//
////					c.id_process, c.process_anio, c.process_nombre, 
////						0					1					2					
////					c.process_fecha_apertura, c.process_fecha_cierre, c.process_comentarios
////						3									4							5
//					
//					
//
//					
//					idWProcessDef 			= (cols[0]!=null ? new Integer(cols[0].toString()):null); 
//					processAnio		= (cols[1]!=null ? new Integer(cols[1].toString()):null);
//					processNombre		= (cols[2]!=null ? cols[2].toString():"");
//
//					processFechaApertura= (cols[3]!=null ? (Date)cols[3]:null);
//					processFechaCierre = (cols[4]!=null ? (Date)cols[4]:null);
//					
//					processComentarios	= (cols[5]!=null ? cols[5].toString():"");
//					
//					if (idWProcessDef!=null && idWProcessDef!=0) {
//						lpr = pdao.finderProyecto(idWProcessDef);
//					} else {
//						lpr=null;
//					}
//					
//					retorno.add( new WProcessDefAmpliada(idWProcessDef, processNombre, processComentarios,
//							processAnio,  processFechaApertura, processFechaCierre,    
//							lpr		));
//				
//				}
//			} else {
//				// nes  - si el select devuelve null entonces devuelvo null
//				retorno=null;
//			}
//		
//		
//		} catch (HibernateException ex) {
//			if (tx != null)
//				tx.rollback();
//			logger.warn("ProyectoDAO: finderWProcessDef() - No pudo recuperarse la lista de proyectos almacenadas - "+
//					ex.getMessage()+ "\n"+ex.getLocalizedMessage()+" \n"+ex.getCause());
//			throw new WProcessDefException(ex);
//		
//		} catch (Exception e) {
//			logger.warn("Exception ProyectoDAO: finderWProcessDef() - error en el manejo de la lista de proyectos almacenadas - "+
//					e.getMessage()+ "\n"+e.getLocalizedMessage()+" \n"+e.getCause());
//			e.getStackTrace();
//			throw new WProcessDefException(e);
//		}
//
//		return retorno;
//		
//	}
//	
////
////			  id_process 
////			  process_anio
////			  process_nombre
////			  process_fecha_apertura
////			  process_fecha_cierre
////			  id_tipo_process
////			  process_comentarios
////			  fecha_alta
////			  usuario_alta
////			  fecha_modificacion
////			  usuario_modificacion
//
//
//	private String _armaQuery(String filtro){
//		
//		String tmpQuery = "SELECT c.id_process, c.process_anio, c.process_nombre,  ";
//		tmpQuery += "  c.process_fecha_apertura, c.process_fecha_cierre, c.process_comentarios  ";
//		tmpQuery += " FROM process c ";
////		tmpQuery += " LEFT OUTER JOIN  proyecto p   ON  p.id_process=c.id_process ";
////		tmpQuery += " LEFT OUTER JOIN  institucion 	i   ON  p.id_institucion=i.id_institucion ";
////		tmpQuery += " LEFT OUTER JOIN  persona 		per ON  p.id_persona_solicitante=per.id_persona";
////		tmpQuery += " LEFT OUTER JOIN  estado_proyecto ep ON  p.id_estado_proyecto=ep.id_estado_proyecto ";
//		tmpQuery += filtro;
//		tmpQuery += " ORDER by c.process_anio, c.process_nombre ASC;";
//		
//		logger.debug("------>> finderWProcessDef -> query:"+tmpQuery+"<<-------");
//	
//		return tmpQuery;
//	}
//	
//	public boolean verificaNombreRepetido(
//			String processNombre, Integer idWProcessDef) 
//	throws WProcessDefException {
//
//		WProcessDef process = null;
//		org.hibernate.Session session = null;
//		org.hibernate.Transaction tx = null;
//
//		try {
//
//			session = HibernateUtil.obtenerSession();
//			tx = session.getTransaction();
//
//			tx.begin();
//
//			process = (WProcessDef) session
//							.createQuery("From WProcessDef WHERE  processNombre= ? AND idWProcessDef != ?")
//								.setString(0, processNombre)
//								.setInteger(1,	idWProcessDef)
//								.uniqueResult();
//
//			tx.commit();
//
//		} catch (HibernateException ex) {
//			if (tx != null)
//				tx.rollback();
//			logger.warn("WProcessDefDao: obtenerWProcessDefPorNombre - No se pudo recuperar un WProcessDef "+ processNombre +" - "+ex.getMessage()  );
//			throw new WProcessDefException("No se pudo obtener el WProcessDef: " + processNombre
//					+ " - " + ex.getMessage() + " ; " + ex.getCause());
//
//		}
//		if (process!=null) return true;
//		else return false;
//	}

}
	