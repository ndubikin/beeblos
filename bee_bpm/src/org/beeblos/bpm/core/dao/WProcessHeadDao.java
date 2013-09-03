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
import org.beeblos.bpm.core.error.WProcessDataFieldException;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessHeadException;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.WProcessHead;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.sp.common.util.StringPair;


public class WProcessHeadDao {
	
	private static final Log logger = LogFactory.getLog(WProcessHeadDao.class.getName());
	
	public WProcessHeadDao (){
		
	}
	
	public Integer add(WProcessHead processHead) throws WProcessHeadException {
		
		logger.debug("add() WProcessHead - Name: ["+processHead.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.save(processHead));

		} catch (HibernateException ex) {
			logger.error("WProcessHeadDao: add - Can't store process definition record "+ 
					processHead.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessHeadException("WProcessHeadDao: add - Can't store process definition record "+ 
					processHead.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WProcessHead processHead) throws WProcessHeadException {
		
		logger.debug("update() WProcessHead < id = "+processHead.getId()+">");
		
		try {

			HibernateUtil.update(processHead);


		} catch (HibernateException ex) {
			logger.error("WProcessHeadDao: update - Can't update process definition record "+ 
					processHead.getName()  +
					" - id = "+processHead.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WProcessHeadException("WProcessHeadDao: update - Can't update process definition record "+ 
					processHead.getName()  +
					" - id = "+processHead.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WProcessHead processHead) throws WProcessHeadException  {

		logger.debug("delete() WProcessHead - Name: ["+processHead.getName()+"]");
		
		try {

			//process = getWProcessByPK(process.getId());
			// nes 20130821 - borro a mano el registro en w_process_head_managed_data porque si no da un error
			// en el hibernateUtil.delete del processHead. 
			/*
			 *  TODO hay que revisar por q da ese error q no tiene mucho sentido pues el cascade está en all
			 *	y la rel es 1 a 1. En concreto mirando la sql que tira hibernate, ahora mismo está intentando hacer
			 *  un update sobre  w_process_head_data_field poniendo el head_id a null lo que es inválido y la bd patea:
			 *  (dejo aquí el update q trata de hacer hibernate antes del delete para tenerlo presente e investigar)
			 *      update
					        bee_bpm_dev.w_process_head_data_field 
					    set
					        process_head_id=null 
					    where
					        process_head_id=?
			 *  
			 */
			if (processHead.getManagedTableConfiguration()!=null) {
				try {
				
					new WProcessHeadManagedDataDao().delete(processHead.getManagedTableConfiguration());
					processHead.setManagedTableConfiguration(null); // to avoid error in delete processHead ...
					
				} catch (WProcessHeadException e) {
					String mess ="WProcessHeadDao: delete - Can't delete Managed Data Record for proccess head definition record "
								+ processHead.getName() +
								" <id = "+processHead.getId()+ "> \n"+" - "+e.getMessage()+"\n"+e.getCause();
					logger.error(mess);
					// continues deleting process head record ...
				}
			}

			// dml 20130822 
			if (processHead.getProcessDataFieldDef()!=null) {
				try {
					for (WProcessDataField pdf : processHead.getProcessDataFieldDef()){
						new WProcessDataFieldDao().delete(pdf);	
					}
					processHead.setProcessDataFieldDef(null); // to avoid error in delete processHead ...
				} catch (WProcessDataFieldException e) {
					String mess ="WProcessHeadDao: delete - Can't delete process data field record for proccess head definition record "
								+ processHead.getName() +
								" <id = "+processHead.getId()+ "> \n"+" - "+e.getMessage()+"\n"+e.getCause();
					logger.error(mess);
					// continues deleting process head record ...
				}
			}
			HibernateUtil.delete(processHead);

		} catch (HibernateException ex) {
			String mess ="WProcessHeadDao: delete - Can't delete proccess head definition record "+ processHead.getName() +
							" <id = "+processHead.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause();
			logger.error( mess );
			throw new WProcessHeadException( mess );

//		} catch (WProcessHeadException ex1) {
//			logger.error("WProcessHeadDao: delete - Exception in deleting process rec "+ process.getName() +
//					" <id = "+process.getId()+ "> no esta almacenada \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );
//			throw new WProcessHeadException("WProcessHeadDao: delete - Exception in deleting process rec "+ process.getName() +
//					" <id = "+process.getId()+ "> not stored \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );

		} 

	}

	public WProcessHead getWProcessHeadByPK(Integer id) throws WProcessHeadException {

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
			throw new WProcessHeadException("WProcessHeadDao: getWProcessByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}
	
	
	public WProcessHead getWProcessHeadByName(String name) throws WProcessHeadException {

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
			throw new WProcessHeadException("getWProcessByName;  can't obtain process name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}


	// id=processId
	public String getProcessName(Integer id) throws WProcessDefException {

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
	
	public List<WProcessHead> getWProcessHeadList(Integer currentUserId) throws WProcessHeadException {

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
			throw new WProcessHeadException("WProcessHeadDao: getWProcesss() - can't obtain process list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return processList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String blank, Integer currentUserId)
	throws WProcessHeadException {
		 
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
		
				tx.commit();

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
				throw new WProcessHeadException(
						"Can't obtain WProcessHead combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;


	}

	@SuppressWarnings("unchecked")
	public List<WProcessHead> finderProcessHead (Date initialInsertDateFilter, Date finalInsertDateFilter, 
			boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
			Integer userId, boolean isAdmin, String action, Integer currentUserId ) 
					throws WProcessHeadException {

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
			
			logger.debug(" ---->>>>>>>>>> userFilter:["+userFilter+"]");
			logger.debug(" ---->>>>>>>>>> requiredFilter:["+requiredFilter+"]");
			logger.debug(" ---->>>>>>>>>> filter:["+filter+"]");
			
			// load base query phrase
			String query = getBaseQuery( isAdmin );
			
			logger.debug(" ---->>>>>>>>>> base query:["+query+"]");

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
				throw new WProcessHeadException(message);

			} catch (Exception ex) {
				if (tx != null)
					tx.rollback();
				String message="WProcessHeadDao: 002B getWProcesss() - can't obtain process list - " +
						ex.getMessage()+"\n"+ex.getCause();
				logger.warn(message );
				throw new WProcessHeadException(message);
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
	// DAVID: no t parece q esta se arreglaria mejor con un count como le puse al WProcessDef.exists

	/**
	 * returns true if there is 1 or more process versions for given processHeadId
	 * or false if doesn't have any version
	 *
	 * @param  Integer processHeadId
	 * @return boolean
	 */
	public boolean hasVersions(Integer processHeadId)
			throws WProcessHeadException {

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

			logger.debug("[QUERY]: "+query);
			
			result = (BigInteger) session.createSQLQuery(query)
					.setParameter("processHeadId", processHeadId)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessHeadDao: headProcessHasWProcessDef() - can't obtain result: "
					+ ex.getMessage() + "\n" + ex.getCause());
			throw new WProcessHeadException(
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
	