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
import org.beeblos.bpm.core.model.noper.StepWorkLight;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


public class WStepWorkDao {
	
	private static final Log logger = LogFactory.getLog(WStepWorkDao.class.getName());
	
	public WStepWorkDao (){
		
	}
	
	public Integer add(WStepWork stepw) throws WStepWorkException {
		
		logger.debug("add() WStepWork - CurrentStep-Work: ["+stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getId()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(stepw));

		} catch (HibernateException ex) {
			logger.error("WStepWorkDao: add - Can't store stepw definition record "+ 
					stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getReference()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepWorkException("WStepWorkDao: add - Can't store stepw definition record "+ 
					stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getReference()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WStepWork stepw) throws WStepWorkException {
		
		logger.debug("update() WStepWork < id = "+stepw.getId()+">");
		
		try {

			HibernateUtil.actualizar(stepw);


		} catch (HibernateException ex) {
			logger.error("WStepWorkDao: update - Can't update stepw definition record "+ 
					stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getReference()  +
					" - id = "+stepw.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WStepWorkException("WStepWorkDao: update - Can't update stepw definition record "+ 
					stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getReference()  +
					" - id = "+stepw.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WStepWork stepw) throws WStepWorkException {

		logger.debug("delete() WStepWork - CurrentStep-Work: ["+stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getReference()+"]");
		
		try {

			stepw = getWStepWorkByPK(stepw.getId());

			HibernateUtil.borrar(stepw);

		} catch (HibernateException ex) {
			logger.error("WStepWorkDao: delete - Can't delete proccess definition record "+ stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getReference() +
					" <id = "+stepw.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepWorkException("WStepWorkDao:  delete - Can't delete proccess definition record  "+ stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getReference() +
					" <id = "+stepw.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (WStepWorkException ex1) {
			logger.error("WStepWorkDao: delete - Exception in deleting stepw rec "+ stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getReference() +
					" <id = "+stepw.getId()+ "> no esta almacenada \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );
			throw new WStepWorkException("WStepWorkDao: delete - Exception in deleting stepw rec "+ stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getReference() +
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
	
		logger.debug("-->>> getStepListByUser: "+query);
		
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
	public List<WStepWork> getWorkStepListByProcess (
			Integer idProcess, Integer idCurrentStep, String status,
			Integer userId, boolean isAdmin, 
			Date arrivingDate, Date openedDate,	Date deadlineDate, 
			String commentsAndReferenceFilter  ) 
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
							getSQLFilter(idProcess, idCurrentStep, status, arrivingDate, openedDate, deadlineDate, commentsAndReferenceFilter ) +
							" ) ";
		
		String requiredFilter = getRequiredFilter(userId, isAdmin);
		
		String filter = userFilter + ( userFilter!=null && !"".equals(userFilter) 
							&& requiredFilter!=null && !"".equals(requiredFilter)?" AND ":"" ) 
							+ requiredFilter ;
		
		filter = (( filter != null && !"".equals(filter)) ? " WHERE ":"") + filter;
		
		logger.debug(" ---->>>>>>>>>> userFilter:["+userFilter+"]"
				+"\n ---->>>>>>>>>> requiredFilter:["+requiredFilter+"]"
				+"\n ---->>>>>>>>>> filter:["+filter+"]");
	
		// load base query phrase
		String query = getBaseQuery( isAdmin, userId );
		
		logger.debug(" ---->>>>>>>>>> base query:["+query+"]");

		
		// dml 20130321 - añadido el "group by" para que la consulta no repita resultados en el caso de que el "userId" tenga
		// más de un perfil asociado al mismo paso
		query += filter + getSQLGroupBy();
		
		// builds full query phrase
//		query += filter+getSQLOrder();
		query += getSQLOrder();

		logger.debug(" ---->>>>>>>>>> FULL query:["+query+"]"
					+"\n ---->>>>>>>>>> userId: "+userId);

		
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
			
			// nes 20130221 - si es admin van todos, no se filtra por usuario ...
			// set userId
			if (!isAdmin) {
				q.setInteger("userId",userId);
			}
			
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
		
		String reqFilter=""; // para las queries no se puede devolver null, hay que devolver ""
		// nes 20130221 - if isAdmin doesn't filter result ...
		if (!isAdmin) {
			reqFilter = " ( ( wsr.id_role in ";
			reqFilter +="(select wur.id_role from w_user_role wur where wur.id_user=:userId )) OR  ";
			reqFilter +=" ( wswa.id_role in ";
			reqFilter +="(select wur.id_role from w_user_role wur where wur.id_user=:userId )) OR  ";
			reqFilter +=" ( wswa.id_user =:userId ) ) ";
		}
		return reqFilter;
		
	}
	
	
	private String getBaseQuery(boolean isAdmin, Integer userId) {
		
		String baseQueryTmp="SELECT * FROM w_step_work w ";
		baseQueryTmp +="left join w_process_work wpw on w.id_work=wpw.id "; // dml 20130321 - para filtro por instructions (nextStepInstructions en el objeto asociado wProcessWork) y reference
		baseQueryTmp +="left join w_step_def wsd on w.id_current_step=wsd.id ";
		baseQueryTmp +="left join w_step_role wsr on wsd.id=wsr.id_step ";
		baseQueryTmp +="left join w_step_user wsu on wsd.id=wsu.id_step AND wsu.id_user = " + userId + " ";
		baseQueryTmp +="left join w_step_work_assignment wswa on w.id=wswa.id_step_work ";	
		
		return baseQueryTmp;
	
	}
	
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

		logger.debug("------------>>> getStepListByProcess: "+query);

		
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
			String commentsAndReferenceFilter) {

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

		// dml 20130321 - cambiado a comments y reference que es a lo que realmente se refiere
		if ( commentsAndReferenceFilter!=null && !"".equals(commentsAndReferenceFilter) ) {

			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" ( wpw.comments LIKE '%"+commentsAndReferenceFilter+"%' ";
			filter +=" OR wpw.reference LIKE '%"+commentsAndReferenceFilter+"%' ) ";

		}
		
		return filter;
	}
	
	

	private String getOrder() {

		return " ORDER BY deadlineDate, deadlineTime DESC ";
		
	}
	
	// dml 20130321 - añadido el "group by" para que la consulta no repita resultados en el caso de que el "userId" tenga
	// más de un perfil asociado al mismo paso
	private String getSQLGroupBy() {

		return " GROUP BY w.id ";
		
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
				.createQuery("From WStepWork where process.id=? AND wProcessWork.idObject=? and wProcessWork.idObjectType=? ")
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
				.createQuery("From WStepWork where wProcessWork.idObject=? and wProcessWork.idObjectType=? order by process.id ")
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
	
	// dml 20120130
	@SuppressWarnings("unchecked")
	public List<WStepWork> getStepListByIdWork(
			Integer idWork, Integer currentUser ) 
	throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepWork> stepws = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			stepws = session
				.createQuery("From WStepWork where wProcessWork.id = :idWork order by openedDate ")
				.setParameter("idWork", idWork)
				.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepWorkDao: 005 getStepListByIdWork() - can't obtain stepw list - " +
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
				.createQuery("From WStepWork where wProcessWork.idObject=? AND wProcessWork.idObjectType=? AND decidedDate is NULL ")
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
						retorno.add(new StringPair(sw.getId(),sw.getCurrentStep().getName()+"-"+sw.getwProcessWork().getReference()));
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
						.createQuery("From WStepWork Where process.id=? AND wProcessWork.idObject=? AND wProcessWork.idObjectType=? ")
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
	


	public List<StepWorkLight> getWorkingStepListFinder(Integer processIdFilter, 
			Integer stepIdFilter, String stepTypeFilter, String referenceFilter, Integer idWorkFilter, 
			Date initialArrivingDateFilter, Date finalArrivingDateFilter, boolean estrictArrivingDateFilter,  		
			Date initialOpenedDateFilter, Date finalOpenedDateFilter, boolean estrictOpenedDateFilter, 		
			Date initialDeadlineDateFilter, Date finalDeadlineDateFilter, boolean estrictDeadlineDateFilter, 		
			Date initialDecidedDateFilter, Date finalDecidedDateFilter, boolean estrictDecidedDateFilter, 		
			String action, boolean onlyActiveWorkingProcessesFilter) 
					throws WStepWorkException {
		
		String filter = "";

		filter = buildWorkingStepFilter(processIdFilter, stepIdFilter,
				stepTypeFilter, referenceFilter, idWorkFilter, initialArrivingDateFilter,
				finalArrivingDateFilter, estrictArrivingDateFilter,
				initialOpenedDateFilter, finalOpenedDateFilter,
				estrictOpenedDateFilter, initialDeadlineDateFilter,
				finalDeadlineDateFilter, estrictDeadlineDateFilter,
				initialDecidedDateFilter, finalDecidedDateFilter,
				estrictDecidedDateFilter, onlyActiveWorkingProcessesFilter, filter);

		if (filter != null && !"".equals(filter)){
			filter = "WHERE " + filter;
		}

		String query = buildWorkingStepQuery(filter, action);
		
		logger.debug("------>> getWorkingStepListFinder -> query:" + query
				+ "<<-------");

		return getWorkingStepList(query);
		
	}

	private String buildWorkingStepFilter(Integer processIdFilter,
			Integer stepIdFilter, String stepTypeFilter,
			String referenceFilter, Integer idWorkFilter, Date initialArrivingDateFilter,
			Date finalArrivingDateFilter, boolean estrictArrivingDateFilter,
			Date initialOpenedDateFilter, Date finalOpenedDateFilter,
			boolean estrictOpenedDateFilter, Date initialDeadlineDateFilter,
			Date finalDeadlineDateFilter, boolean estrictDeadlineDateFilter,
			Date initialDecidedDateFilter, Date finalDecidedDateFilter,
			boolean estrictDecidedDateFilter, boolean onlyActiveWorkingProcessesFilter, 
			String filter) {
		
		if (onlyActiveWorkingProcessesFilter) {
			if (!"".equals(filter)) {
				filter += " AND wpd.active IS TRUE ";
			} else {
				filter += " wpd.active IS TRUE ";

			}
		}
		
		if (processIdFilter != null && processIdFilter != 0) {
			if (!"".equals(filter)) {
				filter += " AND ";
			}
			filter += " sw.id_process = " + processIdFilter;
		}
		
		if (stepIdFilter != null && stepIdFilter != 0) {
			if (!"".equals(filter)) {
				filter += " AND ";
			}
			filter += " sw.id_current_step = " + stepIdFilter;
		}
		
		if (!"".equals(stepTypeFilter) || !"ALL".equals(stepTypeFilter)) {
			if ("PENDING".equals(stepTypeFilter)) {
				if (!"".equals(filter)) {
					filter += " AND ";
				}
				filter += " sw.decided_date IS NULL ";				
			} else if ("PROCESSED".equals(stepTypeFilter)){
				if (!"".equals(filter)) {
					filter += " AND ";
				}
				filter += " sw.decided_date IS NOT NULL ";				
			}
		}
		
		if (referenceFilter != null && !"".equals(referenceFilter)) {
			if (!"".equals(filter)) {
				filter += " AND pw.reference LIKE '%"+referenceFilter+"%'";
			} else {
				filter += " pw.reference LIKE '%"+referenceFilter+"%'";

			}
		}
		
		if (idWorkFilter != null && idWorkFilter != 0) {
			if (!"".equals(filter)) {
				filter += " AND ";
			}
			filter += " sw.id_work = " + idWorkFilter;
		}
		
		if (initialArrivingDateFilter!=null){
			
			java.sql.Date initialArrivingDateFilterSQL=new java.sql.Date(initialArrivingDateFilter.getTime());
			
			if (estrictArrivingDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" sw.arriving_date >= '"+initialArrivingDateFilterSQL+" 00:00:00' ";
				filter+=" AND sw.arriving_date <= '"+initialArrivingDateFilterSQL+" 23:59:59' ";
			} else {
				if (finalArrivingDateFilter!=null){
					java.sql.Date finalArrivingDateFilterSQL=new java.sql.Date(finalArrivingDateFilter.getTime());
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (sw.arriving_date >= '"+initialArrivingDateFilterSQL+"' AND sw.arriving_date <= '"+finalArrivingDateFilterSQL+"') ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" sw.arriving_date >= '"+initialArrivingDateFilterSQL+"' ";
				}
			}
		}

		if (initialOpenedDateFilter!=null){
			
			java.sql.Date initialOpenedDateFilterSQL=new java.sql.Date(initialOpenedDateFilter.getTime());
			
			if (estrictOpenedDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" sw.opened_date >= '"+initialOpenedDateFilterSQL+" 00:00:00' ";
				filter+=" AND sw.opened_date <= '"+initialOpenedDateFilterSQL+" 23:59:59' ";
			} else {
				if (finalOpenedDateFilter!=null){
					java.sql.Date finalOpenedDateFilterSQL=new java.sql.Date(finalOpenedDateFilter.getTime());
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (sw.opened_date >= '"+initialOpenedDateFilterSQL+"' AND sw.opened_date <= '"+finalOpenedDateFilterSQL+"') ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" sw.opened_date >= '"+initialOpenedDateFilterSQL+"' ";
				}
			}
		}

		if (initialDeadlineDateFilter!=null){
			
			java.sql.Date initialDeadlineDateFilterSQL=new java.sql.Date(initialDeadlineDateFilter.getTime());
			
			if (estrictDeadlineDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" sw.deadline_date >= '"+initialDeadlineDateFilterSQL+" 00:00:00' ";
				filter+=" AND sw.deadline_date <= '"+initialDeadlineDateFilterSQL+" 23:59:59' ";
			} else {
				if (finalDeadlineDateFilter!=null){
					java.sql.Date finalDeadlineDateFilterSQL=new java.sql.Date(finalDeadlineDateFilter.getTime());
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (sw.deadline_date >= '"+initialDeadlineDateFilterSQL+"' AND sw.deadline_date <= '"+finalDeadlineDateFilterSQL+"') ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" sw.deadline_date >= '"+initialDeadlineDateFilterSQL+"' ";
				}
			}
		}

		if (initialDecidedDateFilter!=null){
			
			java.sql.Date initialDecidedDateFilterSQL=new java.sql.Date(initialDecidedDateFilter.getTime());
			
			if (estrictDecidedDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" sw.decided_date >= '"+initialDecidedDateFilterSQL+" 00:00:00' ";
				filter+=" AND sw.decided_date <= '"+initialDecidedDateFilterSQL+" 23:59:59' ";
			} else {
				if (finalDecidedDateFilter!=null){
					java.sql.Date finalDecidedDateFilterSQL=new java.sql.Date(finalDecidedDateFilter.getTime());
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (sw.decided_date >= '"+initialDecidedDateFilterSQL+"' AND sw.decided_date <= '"+finalDecidedDateFilterSQL+"') ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" sw.decided_date >= '"+initialDecidedDateFilterSQL+"' ";
				}
			}
		}

		logger.debug("QUERY FILTER:" + filter);

		return filter;
	}

	private String buildWorkingStepQuery(String filter, String action) {

		String tmpQuery = "SELECT ";
		tmpQuery += " sw.id_process, ";
		tmpQuery += " sw.id_current_step, ";
		tmpQuery += " step.name step_name, ";
		tmpQuery += " sw.arriving_date, ";
		tmpQuery += " sw.opened_date, ";
		tmpQuery += " sw.opener_user, ";
		tmpQuery += " sw.decided_date, ";
		tmpQuery += " sw.performer_user_id, ";
		tmpQuery += " sw.deadline_date, ";
		tmpQuery += " sw.deadline_time, ";
		tmpQuery += " pw.reference, ";
		tmpQuery += " sw.locked, ";
		tmpQuery += " sw.locked_by, ";
		tmpQuery += " sw.id, ";
		tmpQuery += " opener.login, ";
		tmpQuery += " opener.name opener_name, ";
		tmpQuery += " performer.login, ";
		tmpQuery += " performer.name performer_name ";

		tmpQuery += " FROM w_step_work sw ";
		tmpQuery += " LEFT OUTER JOIN w_step_def step ON step.id = sw.id_current_step ";
		tmpQuery += " LEFT OUTER JOIN w_process_work pw ON pw.id = sw.id_work ";
		tmpQuery += " LEFT OUTER JOIN w_process_def wpd ON wpd.id = sw.id_process ";
		tmpQuery += " LEFT OUTER JOIN w_user_def opener ON opener.id = sw.opener_user "; 
		tmpQuery += " LEFT OUTER JOIN w_user_def performer ON performer.id = sw.performer_user_id "; 

		tmpQuery += filter;

		if (action == null || action.equals("")) {
			tmpQuery += " ORDER by sw.decided_date DESC; ";
		} 

		logger.debug("------>> getWorkingProcessStepListByFinder -> query:" + tmpQuery
				+ "<<-------");

		logger.debug("QUERY:" + tmpQuery);

		return tmpQuery;
	}

	private List<StepWorkLight> getWorkingStepList(String query)
			throws WStepWorkException {

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
		String reference;
		
		// dml 20120123
		boolean locked;
		Integer lockedBy;
		Integer idStepWork;
		
		// dml 20120124
		String openerUserLogin;
		String openerUserName;
		String performerLogin;
		String performerName;
		
		
		Session session = null;
		Transaction tx = null;

		List<Object[]> result = null;
		List<StepWorkLight> returnList = new ArrayList<StepWorkLight>();

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
					reference = (cols[10] != null ? cols[10].toString() : "");
					locked = (cols[11] != null ? (Boolean) cols[11] : false);
					lockedBy = (cols[12] != null ? new Integer(
							cols[12].toString()) : null);
					idStepWork = (cols[13] != null ? new Integer(
							cols[13].toString()) : null);
					openerUserLogin = (cols[14] != null ? cols[14].toString() : "");
					openerUserName = (cols[15] != null ? cols[15].toString() : "");
					performerLogin = (cols[16] != null ? cols[16].toString() : "");
					performerName = (cols[17] != null ? cols[17].toString() : "");
					
					
					returnList.add(new StepWorkLight(idProcess, idStep, stepName, 
							reference, arrivingDate, openedDate, openerUser, decidedDate, 
							performer, deadlineDate, deadlineTime, locked, lockedBy, idStepWork, 
							openerUserLogin, openerUserName, performerLogin, performerName));
				}

			} else {

				returnList = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
				logger.warn("WProcessDefDao: getWorkingProcessStepListByFinder() - " +
						"It cannot be posible to get the StepWorkLight list - "
					+ ex.getMessage() + "\n" + ex.getLocalizedMessage() + " \n" + ex.getCause());
			throw new WStepWorkException(ex);

		}

		return returnList;
	}

	
	
}
	