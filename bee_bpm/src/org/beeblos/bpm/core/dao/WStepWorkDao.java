package org.beeblos.bpm.core.dao;

import static org.beeblos.bpm.core.util.Constants.PROCESSED;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WStepLockedByAnotherUserException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;


public class WStepWorkDao {
	
	private static final Log logger = LogFactory.getLog(WStepWorkDao.class);
	
	public WStepWorkDao (){
		
	}
	
	public Integer add(WStepWork stepw) throws WStepWorkException {
		
		logger.debug("add() WStepWork - Name: ["+stepw.getIdObjectType()+" "+stepw.getIdObject()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(stepw));

		} catch (HibernateException ex) {
			logger.error("WStepWorkDao: add - Can't store stepw definition record "+ 
					stepw.getIdObjectType()+" "+stepw.getIdObject()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepWorkException("WStepWorkDao: add - Can't store stepw definition record "+ 
					stepw.getIdObjectType()+" "+stepw.getIdObject()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WStepWork stepw) throws WStepWorkException {
		
		logger.debug("update() WStepWork < id = "+stepw.getId()+">");
		
		try {

			HibernateUtil.actualizar(stepw);


		} catch (HibernateException ex) {
			logger.error("WStepWorkDao: update - Can't update stepw definition record "+ 
					stepw.getIdObjectType()+" "+stepw.getIdObject()  +
					" - id = "+stepw.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WStepWorkException("WStepWorkDao: update - Can't update stepw definition record "+ 
					stepw.getIdObjectType()+" "+stepw.getIdObject()  +
					" - id = "+stepw.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WStepWork stepw) throws WStepWorkException {

		logger.debug("delete() WStepWork - Name: ["+stepw.getIdObjectType()+" "+stepw.getIdObject()+"]");
		
		try {

			stepw = getWStepWorkByPK(stepw.getId());

			HibernateUtil.borrar(stepw);

		} catch (HibernateException ex) {
			logger.error("WStepWorkDao: delete - Can't delete proccess definition record "+ stepw.getIdObjectType()+" "+stepw.getIdObject() +
					" <id = "+stepw.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepWorkException("WStepWorkDao:  delete - Can't delete proccess definition record  "+ stepw.getIdObjectType()+" "+stepw.getIdObject() +
					" <id = "+stepw.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (WStepWorkException ex1) {
			logger.error("WStepWorkDao: delete - Exception in deleting stepw rec "+ stepw.getIdObjectType()+" "+stepw.getIdObject() +
					" <id = "+stepw.getId()+ "> no esta almacenada \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );
			throw new WStepWorkException("WStepWorkDao: delete - Exception in deleting stepw rec "+ stepw.getIdObjectType()+" "+stepw.getIdObject() +
					" <id = "+stepw.getId()+ "> not stored \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );

		} 

	}

	public WStepWork getWStepWorkByPK(Integer id) throws WStepWorkException {

		WStepWork stepw = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			stepw = (WStepWork) session.get(WStepWork.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepWorkDao: getWStepWorkByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepWorkException("WStepWorkDao: getWStepWorkByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return stepw;
	}
	
	
	public WStepWork getWStepWorkByName(String name) throws WStepWorkException {

		WStepWork  stepw = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			stepw = (WStepWork) session.createCriteria(WStepWork.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepWorkDao: getWStepWorkByName - can't obtain stepw name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepWorkException("getWStepWorkByName;  can't obtain stepw name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return stepw;
	}

	
	@SuppressWarnings("unchecked")
	public List<WStepWork> getWStepWorks() throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepWork> stepws = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			stepws = session.createQuery("From WStepWork").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepWorkDao: getWStepWorks() - can't obtain stepw list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepWorkException("WStepWorkDao: getWStepWorks() - can't obtain stepw list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return stepws;
	}
	
	// ALL WORKITEMS
	@SuppressWarnings("unchecked")
	public List<WStepWork> getStepListByUser (
			Integer userId, Integer idProcess, String status ) 
	throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepWork> stepws = null;
		
		String statusHQLFilter;
		if ( status==null || "".equals(status) ) {
			statusHQLFilter="";
		}  else if ( status==PROCESSED ) {
			statusHQLFilter=" AND w.decided_date IS NOT NULL ";
		} else { // ALIVE
			statusHQLFilter=" AND w.decided_date IS NULL ";
			
		}

		String query="SELECT * FROM w_step_work w ";
		query +="left join w_step_def wsd on w.id_current_step=wsd.id ";
		query +="left join w_step_role wsr on wsd.id=wsr.id_step ";
		query +="left join w_step_user wsu on wsd.id=wsu.id_step ";
		query +="left join w_step_work_assignment wswa on w.id=wswa.id_step_work ";		
		query +="where ( wsr.id_role in ";
		query +="(select wur.id_role from w_user_def wud, w_user_role wur where wur.id_user=:userId ) OR  ";
		query +=" ( wsu.id_user =:userId ) OR  ";
		query +=" ( wswa.id_role in ";
		query +="(select wur.id_role from w_user_def wud, w_user_role wur where wur.id_user=:userId )) OR  ";
		query +=" ( wswa.id_user =:userId )  ) ";

		query +=statusHQLFilter; // add status filter defined before ...
		query += " ORDER BY w.arriving_date DESC ";
	
		System.out.println(query);
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			stepws = session
					.createSQLQuery(query)
					.addEntity("WStepWork", WStepWork.class)
					.setInteger("userId",userId)
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepWorkDao: 001 getWStepWorks() - can't obtain stepw list - " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn(message );
			throw new WStepWorkException(message);

		}

		return stepws;
	}
	
	
	
	// ALL WORKITEMS
	@SuppressWarnings("unchecked")
	public List<WStepWork> getStepListByProcess (
			Integer idProcess, String status ) 
	throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepWork> stepws = null;
	
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
// nes 20101210
			if ( status==null || "".equals(status) ) {
				stepws = session
							.createQuery("From WStepWork where process.id=? order by deadlineDate, deadlineTime")
							.setParameter(0, idProcess)
							.list();
			} else {

				if ( status==PROCESSED ) {
					stepws = session
								.createQuery("From WStepWork where process.id=? And decidedDate IS NOT NULL order by deadlineDate, deadlineTime")
								.setParameter(0, idProcess)
								.list();
				} else { // ALIVE
					stepws = session
								.createQuery("From WStepWork where process.id=? And decidedDate IS NULL order by deadlineDate, deadlineTime")
								.setParameter(0, idProcess)
								.list();
					
				}
			}

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepWorkDao: 001 getWStepWorks() - can't obtain stepw list - " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn(message );
			throw new WStepWorkException(message);

		}

		return stepws;
	}

	// NEW METHOD WITH ALL FILTERS ...
	
	@SuppressWarnings("unchecked")
	public List<WStepWork> getStepListByProcess (
			Integer idProcess, Integer idCurrentStep, String status,
			Integer userId, boolean isAdmin, 
			Date arrivingDate, Date openedDate,	Date deadlineDate, String instructionsAndReferenceFilter  ) 
	throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		org.hibernate.Query q = null;
		
		
		SimpleDateFormat fmtShortDate = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat fmtLongDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		Date from = null;
		Date to = null;
		
		List<WStepWork> stepws = null;
		
		// build filter from user params. String and Integer values will be added to
		// the String directly in the string filter.
		// Date parameters must be added to hibernate query in the try / catch clause below
		String userFilter = " (" + 
							getSQLFilter(idProcess, idCurrentStep, status, arrivingDate, openedDate, deadlineDate, instructionsAndReferenceFilter ) +
							" ) ";
		
		String requiredFilter = getRequiredFilter(userId, isAdmin);
		
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
					.addEntity("WStepWork", WStepWork.class);

			// setting date parameters
				try {
				
				if (arrivingDate!=null) {
	                from = fmtLongDate.parse(fmtShortDate.format(arrivingDate)+" 00:00:00");                
	                to = fmtLongDate.parse(fmtShortDate.format(arrivingDate)+" 23:59:59");                
					q.setParameter("arrivingdateFrom", from);
					q.setParameter("arrivingdateTo", to);
				}
				
				if (openedDate!=null) {
	                from = fmtLongDate.parse(fmtShortDate.format(openedDate)+" 00:00:00");                
	                to = fmtLongDate.parse(fmtShortDate.format(openedDate)+" 23:59:59");                
					q.setParameter("openeddateFrom", from);
					q.setParameter("openeddateTo", to);
				}
				
				if (deadlineDate!=null) {
	                from = fmtLongDate.parse(fmtShortDate.format(deadlineDate)+" 00:00:00");                
	                to = fmtLongDate.parse(fmtShortDate.format(deadlineDate)+" 23:59:59");                
					q.setParameter("deadlinedateFrom", from);
					q.setParameter("deadlinedateTo", to);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error setting date fields to hibernate SQL Query: "
						+ e.getMessage()+"\n"+e.getCause());	
			}
			
			// set userId
			q.setInteger("userId",userId);
			
			// retrieve list
			stepws = q.list();
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepWorkDao: 002 getWStepWorks() - can't obtain stepw list - " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn(message );
			throw new WStepWorkException(message);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepWorkDao: 002B getWStepWorks() - can't obtain stepw list - " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn(message );
			throw new WStepWorkException(message);
		}
		
		return stepws;
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
		
		String baseQueryTmp="SELECT * FROM w_step_work w ";
		baseQueryTmp +="left join w_step_def wsd on w.id_current_step=wsd.id ";
		baseQueryTmp +="left join w_step_role wsr on wsd.id=wsr.id_step ";
		baseQueryTmp +="left join w_step_user wsu on wsd.id=wsu.id_step ";
		baseQueryTmp +="left join w_step_work_assignment wswa on w.id=wswa.id_step_work ";	
		
		return baseQueryTmp;
	
	}
	
	
	
	
	
	
	
	
	
	
	/*
	 * 
	 */
	

	// ALL WORKITEMS FOR A PROCESS IN A CONCRETE STEP
	@SuppressWarnings("unchecked")
	public List<WStepWork> getStepListByProcess (
			Integer idProcess, Integer idCurrentStep, String status ) 
	throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		org.hibernate.Query q = null;
		
		List<WStepWork> stepws = null;
		
		String filter = getFilter(idProcess, idCurrentStep, status, null, null, null );
		
		filter += (( filter != null && !"".equals(filter)) ? " WHERE ":"");
		
		String query = "From WStepWork ";

		query += filter+getOrder();

		System.out.println("------------>>>"+query);

		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			q = session.createQuery(query);
			
			stepws = q.list();
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepWorkDao: 002 getWStepWorks() - can't obtain stepw list - " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn(message );
			throw new WStepWorkException(message);

		}

		return stepws;
	}


	private String getFilter(Integer idProcess, Integer idCurrentStep,
			String status,	Date arrivingDate, Date openedDate,	Date deadlineDate ) {

		String filter="";
		
		if ( idProcess!=null ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" process.id= "+idProcess;
		}

		if ( idCurrentStep!=null ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" currentStep.id= "+idCurrentStep;
		}

		if ( status!=null && !"".equals(status) ) {

			if ( status==PROCESSED ) {
				if ( filter ==null || !"".equals(filter)) {
					filter +=" AND ";
				}

				filter +=" decidedDate IS NOT NULL ";

			} else { // ALIVE
				
				if ( filter ==null || !"".equals(filter)) {
					filter +=" AND ";
				}

				filter +=" decidedDate IS NULL ";
			}
				
		}
		
		// llegó el:
		if (arrivingDate!=null) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter+=" arrivingDate>=:arrivingdateFrom AND arrivingDate<=:arrivingdateTo";

		}
		
		// revisado el:
		if (openedDate!=null) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter+=" openedDate>=:openeddateFrom AND openedDate<=:openeddateTo";

		}
		
		// fecha limite
		if (deadlineDate!=null) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter+=" deadlineDate>=:deadlinedateFrom AND deadlineDate<=:deadlinedateTo";

		}

		
		return filter;
	}

	
	private String getSQLFilter(Integer idProcess, Integer idCurrentStep,
			String status,	Date arrivingDate, Date openedDate,	Date deadlineDate,
			String instructionsAndReferenceFilter ) {

		String filter="";
		
		if ( idProcess!=null && idProcess > 0) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" id_process= "+idProcess;
		}

		if ( idCurrentStep!=null && idCurrentStep > 0) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" id_current_step= "+idCurrentStep;
		}

		if ( status!=null && !"".equals(status) ) {

			if ( status==PROCESSED ) {
				if ( filter ==null || !"".equals(filter)) {
					filter +=" AND ";
				}

				filter +=" decided_date IS NOT NULL ";

			} else { // ALIVE
				
				if ( filter ==null || !"".equals(filter)) {
					filter +=" AND ";
				}

				filter +=" decided_date IS NULL ";
			}
				
		}
		
		// llegó el:
		if (arrivingDate!=null) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter+=" arriving_date>=:arrivingdateFrom AND arriving_date<=:arrivingdateTo";

		}
		
		// revisado el:
		if (openedDate!=null) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter+=" opened_date>=:openeddateFrom AND opened_date<=:openeddateTo";

		}
		
		// fecha limite
		if (deadlineDate!=null) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter+=" deadline_date>=:deadlinedateFrom AND deadline_date<=:deadlinedateTo";

		}

		// dml 20111220
		if ( instructionsAndReferenceFilter!=null && !"".equals(instructionsAndReferenceFilter) ) {

			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" ( w.instructions LIKE '%"+instructionsAndReferenceFilter+"%' ";
			filter +=" OR w.reference LIKE '%"+instructionsAndReferenceFilter+"%' ) ";

		}
		
		return filter;
	}
	
	

	private String getOrder() {

		return " ORDER BY deadlineDate, deadlineTime DESC ";
		
	}
	
	
	
	private String getSQLOrder() {

		return " ORDER BY w.deadline_date, w.deadline_time, w.arriving_date  DESC ";
		
	}
	
	
	

	// ALL WORKITEMS FOR 1 OBJECT ( AND OBJECT TYPE ) 
	@SuppressWarnings("unchecked")
	public List<WStepWork> getStepListByProcess(
			Integer idProcess, Integer idObject, String idObjectType, Integer currentUser ) 
	throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepWork> stepws = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			stepws = session
				.createQuery("From WStepWork where process.id=? AND idObject=? and idObjectType=? ")
				.setParameter(0, idProcess)
				.setParameter(1, idObject)
				.setParameter(2, idObjectType)
				.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepWorkDao: 005 getWStepWorks() - can't obtain stepw list - " +
							ex.getMessage()+"\n"+ex.getCause();
			logger.warn(message );
			throw new WStepWorkException(message);

		}

		return stepws;
	}
	
	//rrl 20110118 
	@SuppressWarnings("unchecked")
	public List<WStepWork> getStepListByIdObject(
			Integer idObject, String idObjectType, Integer currentUser ) 
	throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepWork> stepws = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			stepws = session
				.createQuery("From WStepWork where idObject=? and idObjectType=? order by process.id ")
				.setParameter(0, idObject)
				.setParameter(1, idObjectType)
				.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepWorkDao: 005 getWStepWorks() - can't obtain stepw list - " +
							ex.getMessage()+"\n"+ex.getCause();
			logger.warn(message );
			throw new WStepWorkException(message);

		}

		return stepws;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<WStepWork> getActiveSteps (
			Integer idObject, String idObjectType, Integer currentUser ) 
	throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepWork> stepws = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			stepws = session
				.createQuery("From WStepWork where idObject=? AND idObjectType=? AND decidedDate is NULL ")
				.setParameter(0, idObject)
				.setParameter(1, idObjectType)
				.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepWorkDao: 007 getWStepWorks() - can't obtain active stepwork list - " +
							ex.getMessage()+"\n"+ex.getCause();
			logger.error(message );
			throw new WStepWorkException(message);

		}

		return stepws;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WStepWorkException {
		 
			List<WStepWork> lsw = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lsw = session
						.createQuery("From WStepWork order by name ")
						.list();
		
				if (lsw!=null) {
					
					// inserta los extras
					if ( textoPrimeraLinea!=null && !"".equals(textoPrimeraLinea) ) {
						if ( !textoPrimeraLinea.equals("WHITESPACE") ) {
							retorno.add(new StringPair(null,textoPrimeraLinea));  // deja la primera línea con lo q venga
						} else {
							retorno.add(new StringPair(null," ")); // deja la primera línea en blanco ...
						}
					}
					
					if ( separacion!=null && !"".equals(separacion) ) {
						if ( !separacion.equals("WHITESPACE") ) {
							retorno.add(new StringPair(null,separacion));  // deja la separación línea con lo q venga
						} else {
							retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
						}
					}
					
				
					
					for (WStepWork sw: lsw) {
						retorno.add(new StringPair(sw.getId(),sw.getIdObject()+"-"+sw.getIdObjectType().trim()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WStepWorkException(
						"Can't obtain WStepWorks combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;


	}

	public Boolean existsActiveProcess(
			Integer idProcess, Integer idObject, String idObjectType) 
	throws WStepWorkException {
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		Boolean retorno=false;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			int qtyActiveSteps = 
					session
						.createQuery("From WStepWork Where process.id=? AND idObject=? AND idObjectType=? ")
						.setParameter(0, idProcess)
						.setParameter(1, idObject)
						.setParameter(2, idObjectType.trim())
						.list()
						.size();

			tx.commit();
			
			if( qtyActiveSteps>0 ) {
				retorno=true;
			} else {
				retorno=false;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepWorkDao: existsActiveProcess() - can't obtain stepwork query list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepWorkException("WStepWorkDao: existsActiveProcess() - can't obtain stepwork query list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}
		
		return retorno;
	}
	
	public Boolean isLockedByUser (
			Integer idStepWork, String idLockedBy ) 
	throws WStepWorkException, WStepLockedByAnotherUserException {
		
		Boolean retorno=true; 
		
		WStepWork stepWork = this.getWStepWorkByPK(idStepWork);
		
		if (stepWork.isLocked()){
			
			if(stepWork.getLockedBy().getId().equals(idLockedBy)){ // nes 20111218
				
				retorno=true;

			} else {

				throw new WStepLockedByAnotherUserException("The required step ("
						+ stepWork.getLockedBy()
						+ ") is blocked by another user ... ");
				
			}
			
		} else {
			
			retorno=false;
		}
		return retorno;
	}
	
	//rrl 20101216
	@SuppressWarnings("unchecked")
	public List<WStepWork> getStepListByProcessName (
			Integer idProcess,	Date arrivingDate, Date openedDate,	Date deadlineDate, String status, Integer currentUser) 
	throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		
		org.hibernate.Query q = null;

		SimpleDateFormat fmtShortDate = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat fmtLongDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		Date from = null;
		Date to = null;
		
		List<WStepWork> stepws = null;
	
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			String filter=getFilter(idProcess, null, status, arrivingDate, openedDate, deadlineDate);
			filter += (( filter != null && !"".equals(filter)) ? " WHERE ":"");
			
			String query = "FROM WStepWork ";
			query += filter;
			query += getOrder();
			
			q = session.createQuery(query);
			
			// setting date parameters
			try {
				
				if (arrivingDate!=null) {
	                from = fmtLongDate.parse(fmtShortDate.format(arrivingDate)+" 00:00:00");                
	                to = fmtLongDate.parse(fmtShortDate.format(arrivingDate)+" 23:59:59");                
					q.setParameter("arrivingdateFrom", from);
					q.setParameter("arrivingdateTo", to);
				}
				
				if (openedDate!=null) {
	                from = fmtLongDate.parse(fmtShortDate.format(openedDate)+" 00:00:00");                
	                to = fmtLongDate.parse(fmtShortDate.format(openedDate)+" 23:59:59");                
					q.setParameter("openeddateFrom", from);
					q.setParameter("openeddateTo", to);
				}
				
				if (deadlineDate!=null) {
	                from = fmtLongDate.parse(fmtShortDate.format(deadlineDate)+" 00:00:00");                
	                to = fmtLongDate.parse(fmtShortDate.format(deadlineDate)+" 23:59:59");                
					q.setParameter("deadlinedateFrom", from);
					q.setParameter("deadlinedateTo", to);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error setting date fields of hibernate sql query: "
						+ e.getMessage()+"\n"+e.getCause());	
			}
			
			stepws = q.list();	
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			String message="WStepWorkDao: 001 getWStepWorks() - can't obtain stepw list - " +
					ex.getMessage()+"\n"+ex.getCause();
			logger.warn(message );
			throw new WStepWorkException(message);

		}

		return stepws;
	}
	
	
	
}
	