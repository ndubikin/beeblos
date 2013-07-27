package org.beeblos.bpm.core.dao;

import static org.beeblos.bpm.core.util.Constants.LAST_W_PROCESS_DEF_ADDED;
import static org.beeblos.bpm.core.util.Constants.LAST_W_PROCESS_DEF_MODIFIED;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessException;
import org.beeblos.bpm.core.model.WProcessHead;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;


public class WProcessHeadDao {
	
	private static final Log logger = LogFactory.getLog(WProcessHeadDao.class.getName());
	
	public WProcessHeadDao (){
		
	}
	
	public Integer add(WProcessHead process, Integer currentUserId) throws WProcessException {
		
		logger.debug("add() WProcessHead - Name: ["+process.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(process));

		} catch (HibernateException ex) {
			logger.error("WProcessHeadDao: add - Can't store process definition record "+ 
					process.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessException("WProcessHeadDao: add - Can't store process definition record "+ 
					process.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WProcessHead process, Integer currentUserId) throws WProcessException {
		
		logger.debug("update() WProcessHead < id = "+process.getId()+">");
		
		try {

			HibernateUtil.actualizar(process);


		} catch (HibernateException ex) {
			logger.error("WProcessHeadDao: update - Can't update process definition record "+ 
					process.getName()  +
					" - id = "+process.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WProcessException("WProcessHeadDao: update - Can't update process definition record "+ 
					process.getName()  +
					" - id = "+process.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WProcessHead process, Integer currentUserId) throws WProcessException {

		logger.debug("delete() WProcessHead - Name: ["+process.getName()+"]");
		
		try {

			//process = getWProcessByPK(process.getId());

			HibernateUtil.borrar(process);

		} catch (HibernateException ex) {
			logger.error("WProcessHeadDao: delete - Can't delete proccess definition record "+ process.getName() +
					" <id = "+process.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessException("WProcessHeadDao:  delete - Can't delete proccess definition record  "+ process.getName() +
					" <id = "+process.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

//		} catch (WProcessException ex1) {
//			logger.error("WProcessHeadDao: delete - Exception in deleting process rec "+ process.getName() +
//					" <id = "+process.getId()+ "> no esta almacenada \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );
//			throw new WProcessException("WProcessHeadDao: delete - Exception in deleting process rec "+ process.getName() +
//					" <id = "+process.getId()+ "> not stored \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );

		} 

	}

	public WProcessHead getWProcessByPK(Integer id, Integer currentUserId) throws WProcessException {

		WProcessHead process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			process = (WProcessHead) session.get(WProcessHead.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessHeadDao: getWProcessByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessException("WProcessHeadDao: getWProcessByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}
	
	
	public WProcessHead getWProcessByName(String name, Integer currentUserId) throws WProcessException {

		WProcessHead  process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			process = (WProcessHead) session.createCriteria(WProcessHead.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessHeadDao: getWProcessByName - can't obtain process name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessException("getWProcessByName;  can't obtain process name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}


	// id=processId
	public String getProcessName(Integer id, Integer currentUserId) throws WProcessDefException {

		String name = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			name = (String) session
					.createQuery("Select WProcessHead.name from WProcessHead Where WProcessHead.id = :id)")
						.setInteger("id",id)
						.uniqueResult();


			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessHead: getProcessName - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("WProcessDef: getProcessName - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return name;
	}
	
	public List<WProcessHead> getWProcessList(Integer currentUserId) throws WProcessException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WProcessHead> processList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processList = session.createQuery("From WProcessHead Order By name ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessHeadDao: getWProcesss() - can't obtain process list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessException("WProcessHeadDao: getWProcesss() - can't obtain process list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return processList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String blank, Integer currentUserId)
	throws WProcessException {
		 
			List<WProcessHead> lwph = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwph = session
						.createQuery("From WProcessHead Order By name ")
						.list();
		
				if (lwph!=null) {
					
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
					
				
					
					for (WProcessHead wph: lwph) {
						retorno.add(new StringPair(wph.getId(),wph.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WProcessException(
						"Can't obtain WProcessHead combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;


	}

	@SuppressWarnings("unchecked")
	public List<WProcessHead> getProcessListByFinder (Date initialInsertDateFilter, Date finalInsertDateFilter, 
			boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
			Integer userId, boolean isAdmin, String action, Integer currentUserId ) 
					throws WProcessException {

			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;
			org.hibernate.Query q = null;
						
			SimpleDateFormat fmtShortDate = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat fmtLongDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			Date from = null;
			Date to = null;
			
			List<WProcessHead> lprocess = null;
			
			// build filter from user params. String and Integer values will be added to
			// the String directly in the string filter.
			// Date parameters must be added to hibernate query in the try / catch clause below
			String userFilter = getSQLFilter(initialInsertDateFilter, finalInsertDateFilter, 
										strictInsertDateFilter, nameFilter, commentFilter);
			
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
						.addEntity("WProcessHead", WProcessHead.class);

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
				String message="WProcessHeadDao: 002 getWProcesss() - can't obtain process list - " +
						ex.getMessage()+"\n"+ex.getCause();
				logger.warn(message );
				throw new WProcessException(message);

			} catch (Exception ex) {
				if (tx != null)
					tx.rollback();
				String message="WProcessHeadDao: 002B getWProcesss() - can't obtain process list - " +
						ex.getMessage()+"\n"+ex.getCause();
				logger.warn(message );
				throw new WProcessException(message);
			}
			
			return lprocess;
		}

	private String getBaseQuery(boolean isAdmin) {
	
		String baseQueryTmp="SELECT * FROM w_process_head wph ";
	
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
			filter +=" wph.comments = "+commentFilter;
		}

		if (initialInsertDateFilter!=null){

			if (strictInsertDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" wph.insertDate >= :insertdateFrom ";
			} else {
				if (finalInsertDateFilter!=null){
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (wph.insertDate >= :insertdateFrom AND wph.insertDate <= :insertdateTo) ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" wph.insertDate <= :insertdateTo ";
				}
			}
		}

		return filter;
	
	}

	private String getSQLFilter(Date initialInsertDateFilter, Date finalInsertDateFilter, 
			boolean strictInsertDateFilter, String nameFilter, String commentFilter ) {

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
				filter+=" wph.comments LIKE '%"+commentFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wph.comments LIKE '%"+commentFilter.trim()+"%' ";
			}
		}
			
		if (initialInsertDateFilter!=null){
	
			java.sql.Date initialInsertDateFilterSQL=new java.sql.Date(initialInsertDateFilter.getTime());
			
			if (strictInsertDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" wph.insert_date >= :insertdateFrom AND wph.insert_date <= :insertdateTo ";
			} else {
				if (finalInsertDateFilter!=null){
					java.sql.Date finalInsertDateFilterSQL=new java.sql.Date(finalInsertDateFilter.getTime());
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (wph.insert_date >= '"+initialInsertDateFilterSQL+"' AND wph.insert_date <= '"+finalInsertDateFilterSQL+"') ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" wph.insert_date >= '"+initialInsertDateFilterSQL+"' ";
				}
			}
		}
		
		return filter;
	}

	// dml 20120416
	private String getSQLOrder(String action) {
		
		String ret = "";
		
		if (action==null || action.equals("")) {
			
			ret = "";
			
		} else if (action.equals(LAST_W_PROCESS_DEF_ADDED)) {
			
			ret = " ORDER by wph.insert_date DESC ";
			
		} else if (action.equals(LAST_W_PROCESS_DEF_MODIFIED)) {
			
			ret = " ORDER by wph.mod_date DESC ";
			
		}
		
		return ret;
		
	}

	// dml 20130507
	public boolean headProcessHasWProcessDef(Integer processHeadId, Integer currentUserId)
			throws WProcessException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		BigInteger result = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			String query =  "SELECT IF(wpd.head_id = :processHeadId,true,false) " +
					" FROM w_process_def wpd " +
					" WHERE wpd.head_id = :processHeadId " +
					" GROUP BY wpd.head_id ";

			System.out.println("[QUERY]: "+query);
			
			result = (BigInteger) session.createSQLQuery(query)
					.setParameter("processHeadId", processHeadId)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessHeadDao: headProcessHasWProcessDef() - can't obtain result: "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new WProcessException(
					"WProcessHeadDao: headProcessHasWProcessDef() - can't obtain result: "
							+ ex.getMessage() + "\n" + ex.getCause());

		}

		if (result == null || result.intValue() == 0) {
			return false;
		} else {
			return true;
		}

	}

}
	