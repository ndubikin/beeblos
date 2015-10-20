package org.beeblos.bpm.core.dao;

import static com.sp.common.util.ConstantsCommon.DATE_FORMAT;
import static com.sp.common.util.ConstantsCommon.DATE_HOUR_COMPLETE_FORMAT;
import static org.beeblos.bpm.core.util.Constants.ALIVE;
import static org.beeblos.bpm.core.util.Constants.PROCESSED;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.ManagedDataSynchronizerException;
import org.beeblos.bpm.core.error.WStepDataFieldException;
import org.beeblos.bpm.core.error.WStepLockedByAnotherUserException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WStepDataField;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.enumerations.ProcessStage;
import org.beeblos.bpm.core.model.enumerations.StepWorkStatus;
import org.beeblos.bpm.core.model.noper.StepWorkLight;
import org.beeblos.bpm.core.model.noper.WStepWorkCheckObject;
import org.beeblos.bpm.tm.TableManager;
import org.beeblos.bpm.tm.exception.TableManagerException;
import org.beeblos.bpm.tm.impl.ManagedDataSynchronizerJavaAppImpl;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.jadira.usertype.dateandtime.joda.columnmapper.DateColumnLocalDateMapper;
import org.jadira.usertype.dateandtime.joda.columnmapper.TimeColumnLocalTimeMapper;
import org.jadira.usertype.dateandtime.joda.columnmapper.TimestampColumnDateTimeMapper;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.sp.common.util.HibernateUtil;


public class WStepWorkDao {
	
	private static final Log logger = LogFactory.getLog(WStepWorkDao.class.getName());
	
	public WStepWorkDao (){
		
	}
	
	// note: swco must arrive full filled & controlled (null object,etc)
	public Integer add(WStepWork stepw) throws WStepWorkException {
		logger.debug("add() WStepWork - CurrentStep-Work:["+(stepw.getCurrentStep()!=null?stepw.getCurrentStep().getName():"null")
				+" processWork reference:"
				+(stepw.getwProcessWork()!=null?stepw.getwProcessWork().getReference():"null")+"]");
		
		Integer id=null;
		Serializable res;
		
		Transaction tx = null;
		try {

			Session session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

//			id = Integer.valueOf(HibernateUtil.save(swco));
			res = session.save(stepw);

			tx.commit();
			
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess="HibernateException: add - Can't store swco definition record "+ 
								stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getReference()+" - "+ex.getMessage()+" "
					+(ex.getCause()!=null?ex.getCause():"");
			logger.error( mess );
			throw new WStepWorkException( mess );
			
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			
			String mess="Exception: add - Can't store swco definition record "+ 
					stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getReference()+" - "
					+ex.getMessage()+" "
					+(ex.getCause()!=null?ex.getCause():"")+" "
					+ex.getClass();
			logger.error( mess );
			throw new WStepWorkException( mess );			
		}

		/**
		 *  set  custom data
		 *  idWork & idStepWork is assigned in previous add
		 *  nes 20141024 - sacado afuera del session por charla con dml
		 */
		try {
			if (stepw.getManagedData()!=null) {
				stepw.getManagedData().setIdWork(stepw.getwProcessWork().getId());
				stepw.getManagedData().setCurrentStepWorkId(stepw.getId());
				if (stepw.getManagedData().getPk()!=null && stepw.getManagedData().getPk()!=0){
					logger.error("WStepWorkDao: add - trying insert managed custom data with assigned pk id:"+stepw.getManagedData().getPk()
							+" pk will be forced to null.");
					stepw.getManagedData().setPk(null);
				}
				_persistStepWorkManagedData(stepw);
			}
		} catch (WStepWorkException ex) {
			String mess="WStepWorkException: add - Can't store swco definition record "+ 
					stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getReference()+" - "
					+ex.getMessage()+" "
					+(ex.getCause()!=null?ex.getCause():"");
			logger.error( mess );
			throw ex;
		} catch (Exception ex) {
			String mess="WStepWorkException: add - Can't store swco definition record "+ 
					stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getReference()+" - "
					+ex.getMessage()+" "
					+(ex.getCause()!=null?ex.getCause():"");
			logger.error( mess );
			throw new WStepWorkException( mess );
		}
		
		id=Integer.valueOf(res.toString());
		return id;
		
	}
	
	/**
	 * 2 phase update: 1st hibernate mapped data, 2nd: managed data (vía jdbc)
	 * 
	 * @param stepw
	 * @param currentUserId
	 * @throws WStepWorkException
	 */
	public void update(WStepWork stepw, Integer currentUserId) throws WStepWorkException {
		logger.debug(">> update WStepWork  id:"+(stepw!=null && stepw.getId()!=null?stepw.getId():"null"));
		
		// note: add basic transactional control to grant insert in 2 tables (w_step_work & managed table if exists)
		// or rollback it ...
		Transaction tx = null;
		try {

			Session session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			session.update(stepw);
//			HibernateUtil.update(swco);

			tx.commit();
			
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess= "HibernateException: update - Can't update swco definition record "+ 
							stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getReference()  +
							" - id = "+stepw.getId()+"\n - "+ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.error( mess );
			throw new WStepWorkException( mess );
			
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess= "Exception: update - Can't update swco definition record "+ 
							stepw.getCurrentStep().getName()+" "+stepw.getwProcessWork().getReference()  +
							" - id = "+stepw.getId()+"\n - "+ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.error( mess );
			throw new WStepWorkException( mess );
		}
		
		/**
		 *  set  custom data
		 *  idWork & idStepWork is assigned in add
		 *   nes 20141024 - sacado afuera del session por charla con dml 
		 */
		if (stepw.getManagedData()!=null) {
			logger.debug(">>> updating managed data ...");
			stepw.getManagedData().setIdWork(stepw.getwProcessWork().getId());
			logger.debug(">>> updating managed data ... 1");
			stepw.getManagedData().setCurrentStepWorkId(stepw.getId());
			logger.debug(">>> updating managed data ... 2");			
			if (stepw.getManagedData().getPk()==null || stepw.getManagedData().getPk()==0){
				logger.error("WStepWorkDao: update - trying update managed custom data with null pk id - "
						+" A new record will be inserted in table:"+stepw.getManagedData().getManagedTableConfiguration().getName());
				stepw.getManagedData().setPk(null);
			}
			logger.debug(">>> updating managed data ... 3");
			_persistStepWorkManagedData(stepw);
			logger.debug(">>> updating managed data ... 4");
			_synchronizeManagedData(stepw,currentUserId); // nes 20140629
			logger.debug(">>> finishing update managed data ... ");
		}
		
	}

	public void lockStepWork( 
			Integer id, DateTime modDate, Integer modUser, boolean isAdmin ) 
					throws WStepWorkException {
		logger.debug("lockStepWork() WStepWork  id:["+(id!=null?id:"null")+"]");
		
		if (id==null || id==0) throw new WStepWorkException("Error trying update wstepwork with id=null or 0!!");
		
		Transaction tx = null;
		try {

			Session session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			//			Integer id, boolean locked, Integer lockedBy, 
			// Date lockedSince, 	Date modDate, Integer modUser
			String hql = "UPDATE WStepWork "
							+ "Set locked= :locked,  "
							+ " lockedBy= :lockedBy, "
							+ " lockedSince= :lockedSince, "
							+ " modDate= :modDate, "
							+ " modUser= :modUser, "
							+ " adminProcess= :adminProcess "
							+ "Where id= :id";

			Query query = session.createQuery(hql);
			query.setInteger("id", id);
			query.setInteger("modUser", modUser);
			query.setInteger("lockedBy", modUser);
			query.setParameter("modDate", modDate);
			query.setParameter("lockedSince", modDate);
			query.setBoolean("adminProcess", isAdmin);
			query.setBoolean("locked", true);

			int rowCount = query.executeUpdate();

			tx.commit();

			logger.debug("lock() updated [rowCount =  "
					+ rowCount + "]");

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: lock - can't update database table w_step_work - id = "
					+ (id!=null?id:"null")	+ " - " + ex.getMessage() + " - " + (ex.getCause()!=null?ex.getCause():"");
			logger.error( mess );
			throw new WStepWorkException(ex);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: lock - can't update database table w_step_work - id = "
					+ (id!=null?id:"null")	+ " - " + ex.getMessage() + " - " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ex.getClass();
			logger.error( mess );
			throw new WStepWorkException(ex);

		}
					
	}
	
	public void unlockStepWork( 
			Integer id, DateTime modDate, Integer modUser, boolean isAdmin ) 
					throws WStepWorkException {
		logger.debug("unlockStepWork() WStepWork  id:["+(id!=null?id:"null")+"]");
		
		if (id==null || id==0) throw new WStepWorkException("Error trying update wstepwork with id=null or 0!!");
		
		Transaction tx = null;
		try {

			Session session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			//			Integer id, boolean locked, Integer lockedBy, 
			// DateTime lockedSince, 	DateTime modDate, Integer modUser
			String hql = "UPDATE WStepWork Set "
								+ " locked=0, "
								+ " lockedBy= :null1, "
								+ " lockedSince= :null2, "
								+ " modDate= :modDate, "
								+ " modUser= :modUser, "
								+ " adminProcess= :adminProcess "
							+ " Where id= :id ";

			Query query = session.createQuery(hql);
			query.setInteger("id", id);
			query.setInteger("modUser", modUser);
			query.setString("null1", null);
			query.setString("null2", null);
			query.setParameter("modDate", modDate);
			query.setBoolean("adminProcess", isAdmin);
			

			int rowCount = query.executeUpdate();

			tx.commit();

			logger.debug("unlock() updated [rowCount =  "
					+ rowCount + "]");

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess ="HibernateException: unlock - can't update database table w_step_work - id = "
					+ (id!=null?id:"null") + " - " + ex.getMessage() + " - " + (ex.getCause()!=null?ex.getCause():"");
			logger.error(mess);
			throw new WStepWorkException(ex);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess ="Exception: unlock - can't update database table w_step_work - id = "
					+ (id!=null?id:"null") + " - " + ex.getMessage() + " - " 
					+ (ex.getCause()!=null?ex.getCause():"")+" "
					+ ex.getClass();
			logger.error(mess);
			throw new WStepWorkException(ex);
		}
					
	}
	
	public void delete(WStepWork stepw) throws WStepWorkException {

		logger.debug("delete() WStepWork - CurrentStep-Work: ["
						+(stepw.getCurrentStep()!=null?stepw.getCurrentStep().getStepHead().getName():"null")
						+" processWork reference:"
						+(stepw.getwProcessWork()!=null?stepw.getwProcessWork().getReference():"null")+"]");
		
		try {

			stepw = getWStepWorkByPK(stepw.getId());

			HibernateUtil.delete(stepw);

		} catch (HibernateException ex) {
			String mess = "WStepWorkDao: HibernateException - delete - Can't delete proccess definition record "
							+ stepw.getCurrentStep().getStepHead().getName()+" "+stepw.getwProcessWork().getReference() 
							+ " <id = "+stepw.getId()+ "> \n"+" - "+ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.error(mess);
			throw new WStepWorkException(mess);

		} catch (WStepWorkException e) {
			String mess = "WStepWorkException: WStepWorkException - delete - Exception in deleting swco rec "
						+ stepw.getCurrentStep().getStepHead().getName()+" "+stepw.getwProcessWork().getReference() 
						+ " <id = "+stepw.getId()+ "> no esta almacenada \n"+" - "+e.getMessage()+" "+e.getCause() ;
			logger.error(mess);
			throw new WStepWorkException(mess);
			
		} catch (Exception e) {
			String mess = "Exception: WStepWorkException - delete - Exception in deleting swco rec "
						+ stepw.getCurrentStep().getStepHead().getName()+" "+stepw.getwProcessWork().getReference() 
						+ " <id = "+stepw.getId()+ "> no esta almacenada \n"+" - "+e.getMessage()+" "+e.getCause() ;
			logger.error(mess);
			throw new WStepWorkException(mess);
		} 

	}

	/**
	 * returns a WStepWork
	 * 
	 * @param id
	 * @return
	 * @throws WStepWorkException
	 */
	public WStepWork getWStepWorkByPK(Integer id) throws WStepWorkException {
		logger.debug(">>> getWStepWorkByPK WStepWork  id:"+(id!=null?id:"null"));
		
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
			String mess="HibernateException: getWStepWorkByPK - we can't obtain the required id = "+
							id + "]  almacenada - \n"+ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WStepWorkException(mess);
		
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess="Exception: getWStepWorkByPK - we can't obtain the required id = "+
							id + "]  almacenada - \n"+ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WStepWorkException(mess);
		}
		
		if ( stepw != null && stepw.getCurrentStep() != null ) {
			logger.debug(">>> loading step data fields...  id:"+(id!=null?id:"null"));

			try {
	
				List<WStepDataField> dataFields = 
						new WStepDataFieldDao()
								.getWStepDataFieldList(
										stepw.getwProcessWork().getProcessHeadId(),
										stepw.getCurrentStep().getStepHead().getId() );
	
				stepw.getCurrentStep().setDataFieldDef(dataFields);
	
			} catch (WStepDataFieldException e) {
				String mess = "WStepWorkDao: getWStepWorkByPK - WStepDataFieldException can't load related step data fields = "+
									id + "] - \n"+e.getMessage()+" "+e.getCause();
				logger.error( mess );
				throw new WStepWorkException( mess );
			}		
			
			// set  custom data
			if (stepw.getCurrentStep().getDataFieldDef()!=null
				&& stepw.getCurrentStep().getDataFieldDef().size()>0) {
				logger.debug(">>> entrando en _loadStepWorkManagedData");
				loadStepWorkManagedData(stepw);
			
			}
		
		}
		logger.debug(">>> getWStepWorkByPK ending ...");
		return stepw;
	}
	
	/**
	 * returns a WStepWork
	 * 
	 * @param id
	 * @return
	 * @throws WStepWorkException
	 */
	public Integer getIdProcessWorkFromWStepWork(Integer id) throws WStepWorkException {
		logger.debug(">>> getIdProcessWorkFromWStepWork WStepWork  id:"+(id!=null?id:"null"));
		
		Integer idPW = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			idPW = (Integer) session
					.createQuery("Select wProcessWork.id From WStepWork Where id= :id ")
					.setInteger("id", id)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess="HibernateException: getIdProcessWorkFromWStepWork - we can't obtain the required id = "+
							id + "]: "+ex.getMessage()+(ex.getCause()!=null?". "+ex.getCause():"");
			logger.warn(mess);
			throw new WStepWorkException(mess);
		
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess="Exception: getIdProcessWorkFromWStepWork - we can't obtain the required id = "+
							id + "]: "+ex.getMessage()+(ex.getCause()!=null?". "+ex.getCause():"");
			logger.warn(mess);
			throw new WStepWorkException(mess);
		}
		
		return idPW;
	}
	
	/**
	 * returns a WStepWorkCheckObject
	 * 
	 * @param id
	 * @return
	 * @throws WStepWorkException
	 */
	public WStepWorkCheckObject getWStepWorkCheckObjectByPK(Integer id) throws WStepWorkException {

		WStepWorkCheckObject swco = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			swco = (WStepWorkCheckObject) session.get(WStepWorkCheckObject.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "WStepWorkDao: getWStepWorkCheckObjectByPK HibernateException - we can't obtain the required swco id = "+
					id + "]  almacenada - "+ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():""); 
			logger.warn( mess );
			throw new WStepWorkException( mess );

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "WStepWorkDao: getWStepWorkCheckObjectByPK Exception - we can't obtain the required swco id = "+
					id + "]  almacenada - "+ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
					+ex.getClass(); 
			logger.warn( mess );
			throw new WStepWorkException( mess );
		}

		return swco;
	}	

	/**
	 * Returns a list with all existing step works without any filter nor condition ...
	 * 
	 * ***** ADMIN method ****** 
	 * 
	 * 
	 * @return
	 * @throws WStepWorkException
	 */
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
			logger.warn("HibernateException: getWStepWorks() - can't obtain swco list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"") );
			throw new WStepWorkException("WStepWorkDao: getWStepWorks() - can't obtain swco list: "
					+ ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():""));

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("Exception: getWStepWorks() - can't obtain swco list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"") );
			throw new WStepWorkException("WStepWorkDao: getWStepWorks() - can't obtain swco list: "
					+ ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():""));
		}

		// set  custom data
		for (WStepWork stepw: stepws) {
			if (stepw.getCurrentStep() != null
					&& stepw.getCurrentStep().getDataFieldDef() != null
					&& stepw.getCurrentStep().getDataFieldDef().size()>0) {
				loadStepWorkManagedData(stepw);
			}
		}
		
		return stepws;
	}
	
	
	// loads the managedData for astepWork:
	// >>ManagedTableConfiguration with managed table info 
	// >>data field list (definition) for this step
	// >>current data from managed table
	public void loadStepWorkManagedData(WStepWork stepWork) throws WStepWorkException{
		logger.debug(">>> _loadStepWorkManagedData");
		
		if (stepWork.getCurrentStep().getDataFieldDef()!=null 
				&& stepWork.getCurrentStep().getDataFieldDef().size()>0){

			// if there is defined custom data fields for a step(def) && managed table is defined ...
			// then load stepWorkManagedData
			if ( stepWork.getwProcessWork().getManagedTableConfiguration()!=null
					&& stepWork.getwProcessWork().getManagedTableConfiguration().getName()!=null
					&& !"".equals(stepWork.getwProcessWork().getManagedTableConfiguration().getName()) ) {
				
				// when load a wStepWork we must load related managed data ...
				ManagedData md = 
						org.beeblos.bpm.tm.TableManagerBeeBpmUtil.createManagedDataObject(stepWork);

				// IMPLEMENTAR
				TableManager tm = new TableManager();
				try {
					tm.loadRecord(md);
					stepWork.setManagedData(md);
				
				} catch (TableManagerException e) {
					String message = "TableManagerException: can't retrieve stored custom data from managed table:"
							+ (md.getManagedTableConfiguration()!=null?(md.getManagedTableConfiguration().getName()!=null?md.getManagedTableConfiguration().getName():"null"):"managed table data is null")
							+ e.getMessage() + " - "
							+ e.getCause();
						logger.warn(message);

						throw new WStepWorkException(message);
				} catch (Exception e) {
					String message = "Exception: can't retrieve stored custom data from managed table:"
							+ (md.getManagedTableConfiguration()!=null?(md.getManagedTableConfiguration().getName()!=null?md.getManagedTableConfiguration().getName():"null"):"managed table data is null")
							+ e.getMessage() + " - "
							+ e.getCause();
						logger.warn(message);

						throw new WStepWorkException(message);						
				}
			}
		} 
	}

	/**
	 * Builds a wStepWork managedData structure.
	 * It will be used i.e. when creates a new WStepWork for inject a new workflow
	 * @param stepWork
	 * @throws WStepWorkException
	 */
	public ManagedData buildStepWorkManagedDataObject(WStepWork stepWork) throws WStepWorkException{
		logger.debug(">>> _loadStepWorkManagedData");
		
		if (stepWork.getCurrentStep().getDataFieldDef()!=null 
				&& stepWork.getCurrentStep().getDataFieldDef().size()>0){

			// if there is defined custom data fields for a step(def) && managed table is defined ...
			// then load stepWorkManagedData
			if ( stepWork.getwProcessWork().getManagedTableConfiguration()!=null
					&& stepWork.getwProcessWork().getManagedTableConfiguration().getName()!=null
					&& !"".equals(stepWork.getwProcessWork().getManagedTableConfiguration().getName()) ) {
				
				// when load a wStepWork we must load related managed data ...
				ManagedData md = 
						org.beeblos.bpm.tm.TableManagerBeeBpmUtil.createManagedDataObject(stepWork);

				return md;
			}
		} 
		
		logger.debug("WStepWork has not DataField defined data or no fields are exposed for this stepDef with id:"
						+(stepWork!=null  && stepWork.getCurrentStep()!=null 
							&& stepWork.getCurrentStep().getId()!=null
								?stepWork.getCurrentStep().getId():"null"));
		return null;
	}
	
	// persists managed data for a stepWork
	// TODO: ojo porque estoy persistiendo el managed data de un step work cuando le hago update.
	// pero el problema es que si esto es un -- resulta que puedo generar "n" rutas y esas 
	// rutas son al final wStepWork, por lo que podría estar persistiendo "n" veces el mismo managed
	// data, porque el managed data se persiste desde el step pero a nivel del "work".
	// HAY QUE DARLE UNA VUELTA A ESTO ...
	private void _persistStepWorkManagedData(WStepWork stepWork) throws WStepWorkException{
		logger.debug(">>> _persistStepWorkManagedData ....");
		
		if (stepWork.getManagedData()!=null
				&& stepWork.getManagedData().getDataField()!=null 
				&& stepWork.getManagedData().getDataField().size()>0){
			logger.debug(">>> has managed data to persist ....");
			
			ManagedData md = stepWork.getManagedData(); // nes 20140629
			TableManager tm = new TableManager();
			try {
				
				tm.persist(md);
			
			} catch (TableManagerException e) {
				String message = "TableManagerException: can't persist custom data at managed table:"
						+ (stepWork.getManagedData().getManagedTableConfiguration()!=null
								?(stepWork.getManagedData().getManagedTableConfiguration().getName()!=null
									?stepWork.getManagedData().getManagedTableConfiguration().getName()
									:"null")
								: "managed table data is null")
						+ e.getMessage() + " "
						+ (e.getCause()!=null?e.getCause():" ");
	
				logger.warn(message);

				throw new WStepWorkException(message);
			}
		} 
	}
	
	private void _synchronizeManagedData(WStepWork stepWork, Integer currentUserId ) throws WStepWorkException{
		logger.debug(">>> _persistStepWorkManagedData ....");
		
		if (stepWork.getManagedData()!=null
				&& stepWork.getManagedData().getDataField()!=null 
				&& stepWork.getManagedData().getDataField().size()>0){
			logger.debug(">>> has managed data to inspect synchro ....");
			
			Integer externalUserId=currentUserId; // TODO IMPLEMENTAR: new WUseridmapperBL().getExternalUser(currentUserId);
			
			ManagedData md = stepWork.getManagedData(); // nes 20140629

			if (md.getDataField().size()>0) {
				
				ManagedDataSynchronizerJavaAppImpl pwSynchronizer = 
						new ManagedDataSynchronizerJavaAppImpl();
				
				// retrieves data from external sources and update fields in managed table
				try {
					
					pwSynchronizer.synchronizeProcessWorkManagedData(
							stepWork.getwProcessWork(), md, ProcessStage.STEP_WORK_WAS_PROCESSED, externalUserId);
				
				} catch (ManagedDataSynchronizerException e) {
					String message = "ManagedDataSynchronizerException: can't synchronize custom data with java app"
							+ (stepWork.getManagedData().getManagedTableConfiguration()!=null
									?(stepWork.getManagedData().getManagedTableConfiguration().getName()!=null
										?stepWork.getManagedData().getManagedTableConfiguration().getName()
										:"null")
									: "??? null")
							+ e.getMessage() + " "
						+ (e.getCause()!=null?e.getCause():" ");
					logger.error(message);
				}
				
				logger.debug(">> managed data has been syncrhonized ...");				
			}
		} 
	}
	

	/**
	 * returns step list by user
	 * 	// ALL WORKITEMS
	 * 
	 * NOTA NES 20141206 - REVISAR A VER SI ESTE MÉTODO LO UTILIZAMOS PARA ALGO PORQUE AHORA MISMO
	 * PARA WORK UTILIZAMOS EL getWorkListByProcess QUE ESTÁ UN POCO MAS ABAJO...
	 * NO OBSTANTE UNIFIQUÉ EL ARMADO DE QUERIES PARA QUE NO TENGAMOS COSAS REPETIDAS EN DIFERENTES
	 * SITIOS DE LA CLASE ...
	 * 
	 * @param userId
	 * @param idProcess
	 * @param status
	 * @return
	 * @throws WStepWorkException
	 */
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
		
		// nes 20141206
		String query = getBaseQuery(false,userId);
		
		// nes 20141206 - recupero del método getBaseQuery que arma este mismo trozo de query
		// dejo comentado hasta verificar que va bien
//		String query="SELECT * FROM w_step_work w ";
//		
//		query +="left join w_step_def wsd on w.id_current_step=wsd.id ";			// WSD - stepDef
//		query +="left join w_step_role wsr on wsd.id=wsr.id_step ";					// WSR - step-role rel
//		query +="left join w_step_user wsu on wsd.id=wsu.id_step ";					// WSU - step-user rel
//		query +="left join w_step_work_assignment wswa on w.id=wswa.id_step_work ";	// WSWA - step work assignment
		
		// nes 20141206 - recupero del método getRequiredFilter que arma este mismo trozo de query
		// dejo comentado hasta verificar que va bien
		
		query +=" WHERE ";
		query += getRequiredFilter(userId,false); // nes 20141206 - paso el parametro admin como false porque este método
												  // no permite o no prevé que se devuelvan pasos en modo admin...
		
//		query +=" where ( ";
//
//		/**
//		 * permissions check from database ...
//		 * role(s) has permissions for the step and user belongs the role(s) - definition time
//		 */
//		query +=" wsr.id_role in "; 
//		query +=" (select wur.id_role from w_user_def wud, w_user_role wur where wur.id_user=:userId ) OR  ";
//
//		/**
//		 * user has explicit permissions for this step ...  - definition time
//		 */
//		query +=" ( wsu.id_user =:userId ) OR  ";
//		
//		/**
//		 * role has permissions for this step ...  -  work time - explicit permissions for the role to
//		 * this stepWork given at runtime
//		 */
//		query +=" ( wswa.id_role in ";
//		query +=" (select wur.id_role from w_user_def wud, w_user_role wur where wur.id_user=:userId )) OR  ";
//
//		/**
//		 * user has permissions for this step ... -  work time - explicit permissions for the user to
//		 * this stepWork given at runtime
//		 */
//		query +=" ( wswa.id_user =:userId ) ";
//		
//		/**
//		 * run time role belongs this step... -  work time - explicit permissions for the runtime role to
//		 * this stepWork given at runtime
//		 * nes 20141206
//		 */
//		
//		
//		query +=" ) ";// end where

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
			String message="HibernateException: 001 getWStepWorks() - can't obtain swco list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(message );
			throw new WStepWorkException(message);
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String message="Exception: 001 getWStepWorks() - can't obtain swco list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(message );
			throw new WStepWorkException(message);
		}

		return stepws;
	}
	
	
	
	// ALL WORKITEMS
	/**
	 * OJO - METODO QUE DEBEMOS REVISAR
	 * AHORA MISMO LO UTILIZAMOS DESE DevelopmentBL para obtener la lista de steps y luego hacer delete
	 * comentario de nes 20141206
	 * 
	 * @param idProcess
	 * @param status
	 * @return
	 * @throws WStepWorkException
	 */
	@SuppressWarnings("unchecked")
	public List<WStepWork> getWorkListByProcessAndStatus (
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
							.createQuery("From WStepWork where wProcessWork.processDef.id=? order by deadlineDate, deadlineTime")
							.setParameter(0, idProcess)
							.list();
			} else {

				if ( status==PROCESSED ) {
					stepws = session
								.createQuery("From WStepWork where wProcessWork.processDef.id=? And decidedDate IS NOT NULL order by deadlineDate, deadlineTime")
								.setParameter(0, idProcess)
								.list();
				} else { // ALIVE
					stepws = session
								.createQuery("From WStepWork where wProcessWork.processDef.id=? And decidedDate IS NULL order by deadlineDate, deadlineTime")
								.setParameter(0, idProcess)
								.list();
					
				}
			}

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="HibernateException: 001 getWStepWorks() - can't obtain swco list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(message );
			throw new WStepWorkException(message);
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String message="Exception: 001 getWStepWorks() - can't obtain swco list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(message );
			throw new WStepWorkException(message);
		}

		return stepws;
	}

	// NEW  WITH ALL FILTERS ...
	/**
	 * returns list of step work for a given processId, userId (optional if currentUserId is
	 * a  Admin)
	 * 
	 * METODO UTILIZADO POR BL PARA TRABAJAR - RECUPERAR TAREAS DE LOS USUARIOS
	 * 
	 * @param idProcess
	 * @param idCurrentStep
	 * @param status
	 * @param userId
	 * @param isAdmin
	 * @param arrivingDate
	 * @param openDate
	 * @param deadlineDate
	 * @param commentsAndReferenceFilter
	 * @param currentUserId
	 * @return
	 * @throws WStepWorkException
	 */
	@SuppressWarnings("unchecked")
	public List<WStepWork> getWorkListByProcess (
			Integer idProcess, Integer idCurrentStep, String status,
			Integer userId, boolean isAdmin, 
			LocalDate arrivingDate, LocalDate openDate, LocalDate deadlineDate, 
			String commentsAndReferenceFilter,
			Integer currentUserId) 
	throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		org.hibernate.Query q = null;
		
		
		List<WStepWork> stepws = null;
		
		// build filter from user params. String and Integer values will be added to
		// the String directly in the string filter.
		// Date parameters must be added to hibernate query in the try / catch clause below
		String userFilter = " (" + 
							getSQLFilter(idProcess, idCurrentStep, status, arrivingDate, openDate, deadlineDate, commentsAndReferenceFilter ) +
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
			
				DateTimeFormatter fmtShortDate = DateTimeFormat.forPattern(DATE_FORMAT);
				DateTimeFormatter fmtLongDate = DateTimeFormat.forPattern(DATE_HOUR_COMPLETE_FORMAT);

				DateTime from = null;
				DateTime to = null;

				if (arrivingDate!=null) {
	                from = fmtLongDate.parseDateTime(fmtShortDate.print(arrivingDate)+" 00:00:00");                
	                to = fmtLongDate.parseDateTime(fmtShortDate.print(arrivingDate)+" 23:59:59");                
					q.setParameter("arrivingdateFrom", from);
					q.setParameter("arrivingdateTo", to);
				}
				
				if (openDate!=null) {
	                from = fmtLongDate.parseDateTime(fmtShortDate.print(openDate)+" 00:00:00");                
	                to = fmtLongDate.parseDateTime(fmtShortDate.print(openDate)+" 23:59:59");                
					q.setParameter("openeddateFrom", from);
					q.setParameter("openeddateTo", to);
				}
				
				if (deadlineDate!=null) {
	                from = fmtLongDate.parseDateTime(fmtShortDate.print(deadlineDate)+" 00:00:00");                
	                to = fmtLongDate.parseDateTime(fmtShortDate.print(deadlineDate)+" 23:59:59");                
					q.setParameter("deadlinedateFrom", from);
					q.setParameter("deadlinedateTo", to);
				}
			
			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
				logger.error("Error setting date fields to hibernate SQL Query: "
						+ e.getMessage()+" "+e.getCause());	
			}
				
			// nes 20130221 - si es admin van todos, no se filtra por usuario ...
			// set userId
//			if (!isAdmin) { // nes 20140204 - al agregarle reqFilter si es admin, siempre hay que setear userId ...
				q.setInteger("userId",userId);
///			}
			
			// retrieve list
			stepws = q.list();
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepWorkDao: 002 getWorkListByProcess() - can't obtain swco list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(message );
			throw new WStepWorkException(message);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepWorkDao: 002B getWorkListByProcess() - can't obtain swco list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(message );
			throw new WStepWorkException(message);
		}
		
		return stepws;
	}


	/**
	 * required filter is set the conditions to recover only steps the given user can be process/view
	 * if comes in admin mode the filter will recover all steps for this admin...
	 * 
	 * nes 20141206 - agregado filtro para runtime roles introducido en esta version (w_USER_ROLE_WORK)
	 * 
	 * @param userId
	 * @param isAdmin
	 * @return
	 */
	private String getRequiredFilter ( Integer userId, boolean isAdmin ) {
		
		String reqFilter=""; // para las queries no se puede devolver null, hay que devolver ""
		
		// nes 20130221 - if isAdmin doesn't filter result ...
		if (!isAdmin) {
			reqFilter = " ( ";

			/**
			 * permissions check from database ...
			 * role(s) has permissions for the step and user belongs the role(s) - definition time
			 */			
			reqFilter += " ( wsr.id_role in ";
			reqFilter +=" 	(select wur.id_role from w_user_role wur where wur.id_user=:userId )) OR  ";

			/**
			 * user has explicit permissions for this step ...  - definition time
			 */
			reqFilter +=" ( wsu.id_user=:userId ) OR "; // nes 20140204 - faltaba que traiga los del usuario específicamente indicado en el step (solo traiamos los usuarios que pertenecian a roles)

			/**
			 * runtime role(s) has permissions for the step and user belongs the role(s) - work time time
			 * nes 20141206
			 */			
			reqFilter += " ( wsr.id_role in ";
			reqFilter +=" 	 (select WURW.id_role from w_user_role_work WURW where WURW.id_user=:userId AND w.id_work=WURW.id_process_work ) ) OR  ";
			
			/**
			 * role has permissions for this step ...  -  work time - explicit permissions for the role to
			 * this stepWork given at runtime
			 */
			reqFilter +=" ( wswa.id_role in ";
			reqFilter +=" 	(select wur.id_role from w_user_role wur where wur.id_user=:userId )) OR  ";

			/**
			 * user has permissions for this step ... -  work time - explicit permissions for the user to
			 * this stepWork given at runtime
			 */
			reqFilter +=" ( wswa.id_user =:userId ) ";
			
			reqFilter += " ) ";
			
		} else {
			
			// si viene con el filtro de "isAdmin" prendido, entonces devuelvo la lista de procesos sobre los que
			// el usuario es admin (esto debe estar seteado a nivel de process_role or process_user
			// Si el usuario es admin a nivel de un paso, esta query no se entera ...
			
			// TODO: hay que agregar el filtro para que traiga los steps sobre los que el usuario sea administrador
			// a nivel de step ... 
			
			reqFilter =  "( wpw.id_process in  ( SELECT distinct wpd.id FROM w_process_def wpd ";

			reqFilter += "    left join w_process_role wpr ON wpd.id=wpr.id_process AND wpr.admin=1 ";
			reqFilter += "    left join w_user_role wur ON wpr.id_role=wur.id_role ";
			reqFilter += "    left join w_process_user wpu ON wpd.id=wpu.id_process ";

			reqFilter += "    WHERE wur.id_user=:userId ";
			reqFilter += "            OR (wpu.admin=1 AND wpu.id_user=:userId) ) ) ";
			
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
	
	// ALL WORKITEMS FOR A PROCESS IN A SINGLE STEP
	/**
	 * returns all workitems for a process and a single step
	 * 
	 * ***** ADMIN method ****** 
	 * 
	 * @param idProcess
	 * @param idCurrentStep
	 * @param status
	 * @return
	 * @throws WStepWorkException
	 */
	@SuppressWarnings("unchecked")
	public List<WStepWork> getWorkListByProcessAndStep (
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
			String message="WStepWorkDao: 002 getWStepWorks() - can't obtain swco list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(message );
			throw new WStepWorkException(message);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String message="Exception: 002 getWStepWorks() - can't obtain swco list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
					+ex.getClass();
			logger.warn(message );
			throw new WStepWorkException(message);
		}

		return stepws;
	}


	private String getFilter(Integer idProcess, Integer idCurrentStep,
			String status, LocalDate arrivingDate, LocalDate openedDate, LocalDate deadlineDate ) {

		String filter="";
		
		if ( idProcess!=null ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" wProcessWork.processDef.id= "+idProcess;
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
			String status,	LocalDate arrivingDate, LocalDate openedDate, LocalDate deadlineDate,
			String commentsAndReferenceFilter) {

		String filter="";
		
		if ( idProcess!=null && idProcess > 0) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" wpw.id_process= "+idProcess;
		}

		if ( idCurrentStep!=null && idCurrentStep > 0) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" w.id_current_step= "+idCurrentStep;
		}

		if ( status!=null && !"".equals(status) ) {

			if ( status==PROCESSED ) {
				if ( filter ==null || !"".equals(filter)) {
					filter +=" AND ";
				}

				filter +=" w.decided_date IS NOT NULL ";

			} else { // ALIVE
				
				if ( filter ==null || !"".equals(filter)) {
					filter +=" AND ";
				}

				filter +=" w.decided_date IS NULL ";
			}
				
		}
		
		// llegó el:
		if (arrivingDate!=null) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter+=" w.arriving_date>=:arrivingdateFrom AND w.arriving_date<=:arrivingdateTo";

		}
		
		// revisado el:
		if (openedDate != null) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter+=" w.opened_date>=:openeddateFrom AND w.opened_date<=:openeddateTo";

		}
		
		// fecha limite
		if (deadlineDate != null) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter+=" w.deadline_date>=:deadlinedateFrom AND w.deadline_date<=:deadlinedateTo";

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

		return " ORDER BY w.deadlineDate, w.deadlineTime DESC ";
		
	}
	
	// dml 20130321 - añadido el "group by" para que la consulta no repita resultados en el caso de que el "userId" tenga
	// más de un perfil asociado al mismo paso
	private String getSQLGroupBy() {

		return " GROUP BY w.id ";
		
	}
	
	private String getSQLOrder() {

		return " ORDER BY w.deadline_date, w.deadline_time, w.arriving_date  DESC ";
		
	}
	

	/**
	 * returns workitems for an object of a given process
	 * (recupera los workitems de 1 objeto para 1 proceso dado)
	 * Parameters must not be null.
	 * 
	 * ***** ADMIN method ******
	 * 
	 * @param idProcess
	 * @param idObject
	 * @param idObjectType
	 * @param currentUser
	 * @return
	 * @throws WStepWorkException
	 */
	@SuppressWarnings("unchecked")
	public List<WStepWork> getWorkListByProcess(
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
				.createQuery("From WStepWork where wProcessWork.processDef.id=? AND wProcessWork.idObject=? and wProcessWork.idObjectType=? ")
				.setParameter(0, idProcess)
				.setParameter(1, idObject)
				.setParameter(2, idObjectType)
				.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepWorkDao: 005 getWStepWorks() - can't obtain swco list - " +
							ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(message );
			throw new WStepWorkException(message);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String message="Exception: 005 getWStepWorks() - can't obtain swco list - " +
							ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
							+ex.getClass();
			logger.warn(message );
			throw new WStepWorkException(message);
		}

		return stepws;
	}

	
	/**
	 * returns all workitems for an idObject and idObjectType
	 * (recupera los workitems de 1 objeto para 1 proceso dado)
	 * Parameters must not be null.
	 * 
	 * ***** ADMIN method ******
	 * 
	 * 	NES 20140529
		ESTE PARECE EL MISMO QUE EL DE LA LINEA 1229 SALVO QUE EL OTRO RETORNA SOLO LOS "ALIVE" Y ESTE PARECE QUE "TODOS"
		rrl 20110118 
	 *  
	 * @param idObject
	 * @param idObjectType
	 * @param currentUser
	 * @return
	 * @throws WStepWorkException
	 */
	@SuppressWarnings("unchecked")
	public List<WStepWork> getWorkListByIdObject(
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
				.createQuery("From WStepWork where wProcessWork.idObject=? and wProcessWork.idObjectType=? order by wProcessWork.processDef.id ")
				.setParameter(0, idObject)
				.setParameter(1, idObjectType)
				.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepWorkDao: 005 getWStepWorks() - can't obtain swco list - " +
							ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(message );
			throw new WStepWorkException(message);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String message="Exception: 005 getWStepWorks() - can't obtain swco list - " +
							ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
							+ex.getClass();
			logger.warn(message );
			throw new WStepWorkException(message);
		}

		return stepws;
	}
	

	/**
	 * 
	 * returns all workitems for a given ProcessWork - 
	 * add filter by processed or all workitems.
	 * idProcessWork must not be null.
	 * 
	 * ***** ADMIN method ******
	 *  
	 *  dml 20120130
	 * 
	 * @param idProcessWork
	 * @param status
	 * @param currentUser
	 * @return
	 * @throws WStepWorkException
	 */
	@SuppressWarnings("unchecked")
	public List<WStepWork> getWorkListByIdWorkAndStatus(
			Integer idProcessWork, String status, Integer currentUser ) 
	throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepWork> stepws = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			// dml 20130826 - le añado el status porque me hará falta para pintar el editor
			if ( status==null || "".equals(status) ) {
				stepws = session
						.createQuery("From WStepWork where wProcessWork.id = :idWork Order By openedDate ")
						.setParameter("idWork", idProcessWork)
						.list();
			} else {

				if ( status.equals(PROCESSED)) {
					stepws = session
							.createQuery("From WStepWork Where wProcessWork.id = :idWork And decidedDate IS NOT NULL Order By openedDate")
							.setParameter("idWork", idProcessWork)
							.list();
				} else { // ALIVE
					stepws = session
							.createQuery("From WStepWork Where wProcessWork.id = :idWork And decidedDate IS NULL Order By openedDate")
							.setParameter("idWork", idProcessWork)
							.list();					
				}
			}

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="WStepWorkDao: 005 getStepListByIdWork() - can't obtain swco list - " +
							ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(message );
			throw new WStepWorkException(message);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String message="Exception: 005 getStepListByIdWork() - can't obtain swco list - " +
							ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
							+ex.getClass();
			logger.warn(message );
			throw new WStepWorkException(message);
		}

		return stepws;
	}
	
	
	
	/**
	 * Returns active stepWork list for given idObject/idObjectType
	 * IMPORTANT: This query don't check permissions for given user ... 
	 * 
	 * ***** ADMIN method ****** 
	 * 
	 * @param idObject
	 * @param idObjectType
	 * @param currentUser
	 * @return
	 * @throws WStepWorkException
	 */
	@SuppressWarnings("unchecked")
	public List<WStepWork> getAliveSteps (
			Integer idObject, String idObjectType) 
	throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepWork> stepws = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			stepws = session
				.createQuery("From WStepWork where wProcessWork.idObject= :idObject AND wProcessWork.idObjectType= :idObjectType AND decidedDate is NULL ")
				.setParameter("idObject", idObject)
				.setParameter("idObjectType", idObjectType)
				.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="HibernateException 007 at getAliveSteps - can't obtain active stepwork list - " +
							ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.error(message );
			throw new WStepWorkException(message);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String message="Exception 007 at getAliveSteps - can't obtain active stepwork list - " +
							ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
							+ex.getClass();
			logger.error(message );
			throw new WStepWorkException(message);
		}

		return stepws;
	}
	
	/**
	 * returns the list of alive step names for given idObject...
	 * 
	 * @param idObject
	 * @param idObjectType
	 * @return
	 * @throws WStepWorkException
	 */
	public List<String> getAliveStepNameList (
			Integer idObject, String idObjectType) 
	throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<String> stepNameList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			stepNameList = session
				.createQuery("Select currentStep.stepHead.name  From WStepWork where wProcessWork.idObject= :idObject AND wProcessWork.idObjectType= :idObjectType AND decidedDate is NULL ")
				.setParameter("idObject", idObject)
				.setParameter("idObjectType", idObjectType)
				.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message="HibernateException at getAliveStepNameList - can't obtain active stepwork list - " +
							ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.error(message );
			throw new WStepWorkException(message);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String message="Exception at getAliveStepNameList - can't obtain active stepwork list - " +
							ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
							+ex.getClass();
			logger.error(message );
			throw new WStepWorkException(message);
		}

		return stepNameList;
	}
	
// dejo comentada nes 20140705	
//	/**
//	 * método con poco sentido: ¿para que querría devolver una lista de wStepWork para combo ...???
//	 * y máxime sin filtrar ...!!
//	 * 
//	 * @param textoPrimeraLinea
//	 * @param separacion
//	 * @return
//	 * @throws WStepWorkException
//	 */
//	@SuppressWarnings("unchecked")
//	public List<StringPair> getComboList(
//			String textoPrimeraLinea, String separacion )
//	throws WStepWorkException {
//		 
//			List<WStepWork> lsw = null;
//			List<StringPair> retorno = new ArrayList<StringPair>(10);
//			
//			org.hibernate.Session session = null;
//			org.hibernate.Transaction tx = null;
//
//			try {
//
//				session = HibernateUtil.obtenerSession();
//				tx = session.getTransaction();
//				tx.begin();
//
//				lsw = session
//						.createQuery("From WStepWork order by name ")
//						.list();
//		
//				tx.commit();
//				
//				if (lsw!=null) {
//					
//					// inserta los extras
//					if ( textoPrimeraLinea!=null && !"".equals(textoPrimeraLinea) ) {
//						if ( !textoPrimeraLinea.equals("WHITESPACE") ) {
//							retorno.add(new StringPair(null,textoPrimeraLinea));  // deja la primera línea con lo q venga
//						} else {
//							retorno.add(new StringPair(null," ")); // deja la primera línea en blanco ...
//						}
//					}
//					
//					if ( separacion!=null && !"".equals(separacion) ) {
//						if ( !separacion.equals("WHITESPACE") ) {
//							retorno.add(new StringPair(null,separacion));  // deja la separación línea con lo q venga
//						} else {
//							retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
//						}
//					}
//					
//				
//					
//					for (WStepWork sw: lsw) {
//						retorno.add(new StringPair(sw.getId(),sw.getCurrentStep().getName()+"-"+sw.getwProcessWork().getReference()));
//					}
//				} else {
//					// nes  - si el select devuelve null entonces devuelvo null
//					retorno=null;
//				}
//				
//				
//			} catch (HibernateException ex) {
//				if (tx != null)
//					tx.rollback();
//				throw new WStepWorkException(
//						"Can't obtain WStepWorks combo list "
//						+ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():""));
//			} catch (Exception e) {}
//
//			return retorno;
//
//
//	}

	/**
	 * return true if exists alive processes for given  def id, objectId and objectType id
	 * 
	 * @param idProcess  >> refers to wProcessDefId
	 * @param idObject
	 * @param idObjectType
	 * @return
	 * @throws WStepWorkException
	 */
	public Boolean existsActiveProcess(
			Integer idProcess, Integer idObject, String idObjectType) 
	throws WStepWorkException {
		logger.debug("existsActiveProcess >> idProcess:"
				+(idProcess!=null?idProcess:"null")
				+" idObject:"+(idObject!=null?idObject:"")
				+" idObjectType"+(idObjectType!=null?idObjectType:"null"));
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		Boolean retorno=false;
		Long qtyActiveSteps=null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

// nes 20140411 - pasado a criteria ...
//					session
//						.createQuery("From  Where wProcessWork.processDef.id=? AND =? AND =? ")
//						.setParameter(0, idProcess)
//						.setParameter(1, idObject)
//						.setParameter(2, idObjectType.trim())
//						.list()
//						.size();

			tx.begin();

			Criteria crit = session.createCriteria(WStepWork.class, "wsw")
			.createAlias("wProcessWork", "wpw", JoinType.LEFT_OUTER_JOIN)
			.createAlias("wProcessWork.processDef", "processDef", JoinType.LEFT_OUTER_JOIN);
			
			crit.setProjection(Projections.rowCount());
			if (idProcess!=null) crit.add( Restrictions.eq("processDef.id",idProcess));
			if (idObject!=null) crit.add( Restrictions.eq("wpw.idObject",idObject));
			if (idObjectType!=null) crit.add( Restrictions.eq("wpw.idObjectType",idObjectType.trim()));
			
			// como indica "activos", estarán activos o no resueltos los q no tengan fecha de resolución ...
			crit.add(Restrictions.eq("decidedDate", null));

			qtyActiveSteps = (Long) crit.uniqueResult();

			tx.commit();
			
			if( qtyActiveSteps>0 ) {
				retorno=true;
			} else {
				retorno=false;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "WStepWorkDao: existsActiveProcess() HibernateException - can't obtain stepwork query list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():""); 
			logger.error( mess );
			throw new WStepWorkException( mess );
			
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: existsActiveProcess() HibernateException - can't obtain stepwork query list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
					+ex.getClass(); 
			logger.error( mess );
			throw new WStepWorkException( mess );			

		}
		
		return retorno;
	}
	
	/**
	 * checks if exists processes related with given ids...
	 * User is responsible to provide a coherent set of ids ...
	 * To check for alive  please use existsActiveProcess
	 * 
	 * @param idProcess >> refers to wProcessDef id (definition)
	 * @param idObject
	 * @param idObjectType
	 * @return
	 * @throws WStepWorkException
	 */
	public Boolean existsProcess(
			Integer idProcess, Integer idObject, String idObjectType) 
	throws WStepWorkException {
		logger.debug("existsProcess >> idProcess:"
				+(idProcess!=null?idProcess:"null")
				+" idObject:"+(idObject!=null?idObject:"")
				+" idObjectType"+(idObjectType!=null?idObjectType:"null"));
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		Boolean retorno=false;
		Long qtyActiveSteps=null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			Criteria crit = session.createCriteria(WStepWork.class)
					.createAlias("wProcessWork", "wpw", JoinType.LEFT_OUTER_JOIN)
					.createAlias("wpw.processDef", "processDef", JoinType.LEFT_OUTER_JOIN);					
			
			crit.setProjection(Projections.rowCount());
			
			if (idProcess!=null) crit.add( Restrictions.eq("processDef.id",idProcess));
			if (idObject!=null) crit.add( Restrictions.eq("wpw.idObject",idObject));
			if (idObjectType!=null) crit.add( Restrictions.eq("wpw.idObjectType",idObjectType.trim()));

			qtyActiveSteps = (Long) crit.uniqueResult(); 

			tx.commit();
			
			if( qtyActiveSteps>0 ) {
				retorno=true;
			} else {
				retorno=false;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: existsActiveProcess() - can't obtain stepwork query list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn( mess );
			throw new WStepWorkException( mess );

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: existsActiveProcess() - can't obtain stepwork query list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
					+ex.getClass();
			logger.warn( mess );
			throw new WStepWorkException( mess );
		}
		
		return retorno;
	}	
	
	/**
	 *  returns qty of existing step works for a given processDefId ( version)
	 *  
	 *  nes 20140411 - pasado a criteria ...
	 *  
	 * @param processDefId
	 * @param mode: A = alive, P = processed
	 * @return
	 * @throws WStepWorkException
	 */
	public Integer getStepWorkCountByProcess(Integer processDefId, String mode) 
			throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		Long qtySteps;

//		String query = "SELECT COUNT(*) FROM  wsw LEFT OUTER JOIN w_process_work wpw ON wpw.id=wsw.id_work "
//							+"WHERE wpw.id_process = " + processDefId;
//		
//		if (mode.equals(ALIVE)) {
//			query += " AND wsw.decided_date is null";
//		} else if (mode.equals(PROCESSED)) {
//			query += " AND wsw.decidedDate is not null";
//		}
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			Criteria crit = session.createCriteria(WStepWork.class)
					.createAlias("wProcessWork", "wpw", JoinType.LEFT_OUTER_JOIN)
					.createAlias("wpw.processDef", "processDef", JoinType.LEFT_OUTER_JOIN);
			
			crit.setProjection(Projections.rowCount());
			
			if (processDefId!=null) crit.add( Restrictions.eq("processDef.id",processDefId));
			
			if (mode.equals(ALIVE)) {
				crit.add( Restrictions.eq("decidedDate",null));
			} else if (mode.equals(PROCESSED)) {
				crit.add( Restrictions.ne("decidedDate",null));
			}

			qtySteps = (Long) crit.uniqueResult();
			
//			qtySteps = (BigInteger) session
//								.createSQLQuery(query)
//								.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess="HibernateException: getWorkCount() - error trying count stepWork - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WStepWorkException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess="Exception: getWorkCount() - error trying count stepWork - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
					+ex.getClass();
			logger.warn(mess);
			throw new WStepWorkException(mess);
			
		}
		
		if (qtySteps != null){
			return qtySteps.intValue();
		}
		
		return 0;		
	}
	
	/**
	 * 
	 * Returns the number of "WStepWork" items related to a given "WStepDef"
	 * 
	 * @author dmuleiro - 20130830 // mod nes 20140705
	 *
	 * @param  Integer stepId  >> refers to wStepDef id
	 * @param  String mode
	 * 
	 * @return Integer
	 * 
	 * @throws WStepWorkException
	 * 
	 */
	public Integer getWorkCountByStep(Integer stepId, String mode) 
			throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		Long qtySteps;

//		String query = "SELECT COUNT(*) FROM w_step_work wsw WHERE wsw.id_current_step = " 
//				+ stepId + " OR wsw.id_previous_step = " + stepId;
//		
//		if (mode != null){
//			if (mode.equals(ALIVE)) {
//				query += " AND wsw.decided_date is null";
//			} else if (mode.equals(PROCESSED)) {
//				query += " AND wsw.decidedDate is not null";
//			}
//		}
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			Criteria crit = session.createCriteria(WStepWork.class)
					.createAlias("currentStep", "currentStep", JoinType.LEFT_OUTER_JOIN); // refers to WStepDef nes 20140705
			
			crit.setProjection(Projections.rowCount());
			
			if (stepId!=null) {
				crit.add( 
						Restrictions.or(
								Restrictions.eq("currentStep.id",stepId)
//								, Restrictions.eq("idPreviousStep",stepId) // nes 20140705 - este no iria no aplica al previos aqui según la explicación del método...  
								) );
			}
			
			if (mode.equals(ALIVE)) {
				crit.add( Restrictions.eq("decidedDate",null));
			} else if (mode.equals(PROCESSED)) {
				crit.add( Restrictions.ne("decidedDate",null));
			}

			qtySteps = (Long) crit.uniqueResult();
			
//			qtySteps = (BigInteger) session
//								.createSQLQuery(query)
//								.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess="WStepWorkDao: getWorkCount() - error trying count stepWork - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WStepWorkException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess="Exception: getWorkCount() - error trying count stepWork - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
					+ex.getClass();
			logger.warn(mess);
			throw new WStepWorkException(mess);
			
		}
		
		if (qtySteps != null){
			return qtySteps.intValue();
		}
		
		return 0;		
	}

	/**
	 * Checks if the stepWork is locked by given UserId
	 * returns true, false if the step is not locked, and WStepLockedByAnotherUserException
	 * 
	 * @param pk
	 * @param idLockedBy
	 * @return
	 * @throws WStepWorkException
	 * @throws WStepLockedByAnotherUserException
	 */
	public Boolean isLockedByUser (
			Integer pk, Integer idLockedBy ) 
	throws WStepWorkException, WStepLockedByAnotherUserException {
		
		Boolean ret=true; 
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		Integer stepLockedBy =null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

	        Criteria criteria = session.createCriteria(WStepWork.class.getName());
	        criteria.add(Restrictions.eq("id", pk));
	        criteria.setProjection(Projections.property("lockedBy.id"));

	        stepLockedBy = (Integer) criteria.uniqueResult();			        

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess="WStepWorkDAO: isLockedByUser() error trying to access wstepwork id:"
							+ pk+ " - " + ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"");
			logger.error(mess);
			throw new WStepWorkException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess="Exception: isLockedByUser() error trying to access wstepwork id:"
							+ pk+ " - " + ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"")+" "
							+ex.getClass();
			logger.error(mess);
			throw new WStepWorkException(mess);
		}
		
		if(stepLockedBy==null){ 
			ret=false; // step is not locked
		} else if (stepLockedBy.equals(idLockedBy)){
			ret=true; // step is locked by given user id
		} else { 
			throw new WStepLockedByAnotherUserException("The required step ("
					+ pk
					+ ") is locked by another user ... ");
		}		
		return ret;
	}
	
	//rrl 20101216
	@SuppressWarnings("unchecked")
	public List<WStepWork> getStepListByProcessName ( Integer idProcess,	
			LocalDate arrivingDate, LocalDate openedDate, LocalDate deadlineDate, 
			String status, Integer currentUser) 
	throws WStepWorkException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		
		org.hibernate.Query q = null;

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
				
				DateTimeFormatter fmtShortDate = DateTimeFormat.forPattern(DATE_FORMAT);
				DateTimeFormatter fmtLongDate = DateTimeFormat.forPattern(DATE_HOUR_COMPLETE_FORMAT);

				DateTime from = null;
				DateTime to = null;

				if (arrivingDate!=null) {
	                from = fmtLongDate.parseDateTime(fmtShortDate.print(arrivingDate)+" 00:00:00");                
	                to = fmtLongDate.parseDateTime(fmtShortDate.print(arrivingDate)+" 23:59:59");                
					q.setParameter("arrivingdateFrom", from);
					q.setParameter("arrivingdateTo", to);
				}
				
				if (openedDate!=null) {
	                from = fmtLongDate.parseDateTime(fmtShortDate.print(openedDate)+" 00:00:00");                
	                to = fmtLongDate.parseDateTime(fmtShortDate.print(openedDate)+" 23:59:59");                
					q.setParameter("openeddateFrom", from);
					q.setParameter("openeddateTo", to);
				}
				
				if (deadlineDate!=null) {
	                from = fmtLongDate.parseDateTime(fmtShortDate.print(deadlineDate)+" 00:00:00");                
	                to = fmtLongDate.parseDateTime(fmtShortDate.print(deadlineDate)+" 23:59:59");                
					q.setParameter("deadlinedateFrom", from);
					q.setParameter("deadlinedateTo", to);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error setting date fields of hibernate sql query: "
						+ e.getMessage()+" "+e.getCause());	
			}
			
			stepws = q.list();	
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			String message="WStepWorkDao: 001 getWStepWorks() - can't obtain swco list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(message );
			throw new WStepWorkException(message);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			String message="Exception: 001 getWStepWorks() - can't obtain swco list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
					+ex.getClass();
			logger.warn(message );
			throw new WStepWorkException(message);

		}

		return stepws;
	}
	

	/**
	 * Returns a list of all  def id which has WORKS referring given stepDefId
	 * and distinct of processDefId
	 * 
	 * @author 	dml 20130507
	 * 
	 * @param stepDefId
	 * @param processDefId
	 * @return
	 * @throws WStepWorkException
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getRelatedProcessDefIdList(Integer stepDefId, Integer processDefId) 
			throws WStepWorkException {
	
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Integer> processIdList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			String query =  " SELECT DISTINCT(wpw.id_process) " +
							" FROM w_step_work wsw " +
							" LEFT OUTER JOIN w_process_work wpw ON wpw.id=wsw.id_work" +
							" WHERE (wsw.id_current_step = :stepId OR wsw.id_previous_step = :stepId) ";
			
			// nes 20130905
			if (processDefId!=null && processDefId!=0) {
				query+="AND wpw.id_process != "+processDefId;
			}
			
			logger.debug("[QUERY]: "+query);
			
			processIdList = (ArrayList<Integer>) session.createSQLQuery(query)
					.setInteger("stepId", stepDefId)
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "WStepWorkDao: getProcessIdList() - can't obtain id  list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WStepWorkException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getProcessIdList() - can't obtain id  list - " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
					+ex.getClass();
			logger.warn(mess);
			throw new WStepWorkException(mess);
			
		}

		return processIdList;
	
	}	

	/**
	 * StepWork finder 
	 * 
	 * @param processIdFilter
	 * @param stepIdFilter
	 * @param stepWorkProcessingStatusFilter - StepWorkStatus enum
	 * @param referenceFilter
	 * @param idWorkFilter
	 * @param initialArrivingDateFilter
	 * @param finalArrivingDateFilter
	 * @param estrictArrivingDateFilter
	 * @param initialOpenedDateFilter
	 * @param finalOpenedDateFilter
	 * @param estrictOpenedDateFilter
	 * @param initialDeadlineDateFilter
	 * @param finalDeadlineDateFilter
	 * @param estrictDeadlineDateFilter
	 * @param initialDecidedDateFilter
	 * @param finalDecidedDateFilter
	 * @param estrictDecidedDateFilter
	 * @param action
	 * @param onlyActiveProcessDefFilter
	 * @return
	 * @throws WStepWorkException
	 */
	public List<StepWorkLight> finderStepWork(Integer processIdFilter, 
			Integer stepIdFilter, StepWorkStatus stepWorkProcessingStatusFilter, String referenceFilter, Integer idWorkFilter, 
			LocalDate initialArrivingDateFilter, LocalDate finalArrivingDateFilter, boolean estrictArrivingDateFilter,  		
			LocalDate initialOpenedDateFilter, LocalDate finalOpenedDateFilter, boolean estrictOpenedDateFilter, 		
			LocalDate initialDeadlineDateFilter, LocalDate finalDeadlineDateFilter, boolean estrictDeadlineDateFilter, 		
			LocalDate initialDecidedDateFilter, LocalDate finalDecidedDateFilter, boolean estrictDecidedDateFilter, 		
			String action, boolean onlyActiveProcessDefFilter) 
					throws WStepWorkException {
		
		String filter = "";

		filter = buildWorkingStepFilter(processIdFilter, stepIdFilter,
				stepWorkProcessingStatusFilter, referenceFilter, idWorkFilter, initialArrivingDateFilter,
				finalArrivingDateFilter, estrictArrivingDateFilter,
				initialOpenedDateFilter, finalOpenedDateFilter,
				estrictOpenedDateFilter, initialDeadlineDateFilter,
				finalDeadlineDateFilter, estrictDeadlineDateFilter,
				initialDecidedDateFilter, finalDecidedDateFilter,
				estrictDecidedDateFilter, onlyActiveProcessDefFilter, filter);

		if (filter != null && !"".equals(filter)){
			filter = "WHERE " + filter;
		}

		String query = buildWorkingStepQuery(filter, action);
		
		logger.debug("------>> finderStepWork -> query:" + query
				+ "<<-------");

		return getWorkingStepList(query);
		
	}

	private String buildWorkingStepFilter(Integer processIdFilter,
			Integer stepIdFilter, StepWorkStatus stepWorkProcessingStatusFilter,
			String referenceFilter, Integer idWorkFilter, LocalDate initialArrivingDateFilter,
			LocalDate finalArrivingDateFilter, boolean estrictArrivingDateFilter,
			LocalDate initialOpenedDateFilter, LocalDate finalOpenedDateFilter,
			boolean estrictOpenedDateFilter, LocalDate initialDeadlineDateFilter,
			LocalDate finalDeadlineDateFilter, boolean estrictDeadlineDateFilter,
			LocalDate initialDecidedDateFilter, LocalDate finalDecidedDateFilter,
			boolean estrictDecidedDateFilter, boolean onlyActiveProcessDefFilter, 
			String filter) {
		
		if (onlyActiveProcessDefFilter) {
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
			filter += " pw.id_process = " + processIdFilter;
		}
		
		if (stepIdFilter != null && stepIdFilter != 0) {
			if (!"".equals(filter)) {
				filter += " AND ";
			}
			filter += " sw.id_current_step = " + stepIdFilter;
		}
		
		/**
		 * nes 20141028 - pasado a enum
		 */
		if (stepWorkProcessingStatusFilter.equals(StepWorkStatus.PENDING)) {
			if (!"".equals(filter)) {
				filter += " AND ";
			}
			filter += " sw.decided_date IS NULL ";				
		} else if (stepWorkProcessingStatusFilter.equals(StepWorkStatus.PROCESSED)){
			if (!"".equals(filter)) {
				filter += " AND ";
			}
			filter += " sw.decided_date IS NOT NULL ";				
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
		
		DateTimeFormatter fmtShortDate = DateTimeFormat.forPattern(DATE_FORMAT);
		DateTimeFormatter fmtLongDate = DateTimeFormat.forPattern(DATE_HOUR_COMPLETE_FORMAT);

		DateTime from = null;
		DateTime to = null;

		if (initialArrivingDateFilter != null) {
			if (estrictArrivingDateFilter) {
				if (!"".equals(filter)) {
					filter += " AND ";
				}
				from = fmtLongDate.parseDateTime(fmtShortDate.print(initialArrivingDateFilter)
						+ " 00:00:00");
				to = fmtLongDate.parseDateTime(fmtShortDate.print(initialArrivingDateFilter)
						+ " 23:59:59");
				filter += " sw.arriving_date >= '" + from + "' AND sw.arriving_date <= '" + to + "' ";
			} else {
				if (finalArrivingDateFilter != null) {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " (sw.arriving_date >= '" + initialArrivingDateFilter
							+ "' AND sw.arriving_date <= '" + finalArrivingDateFilter + "') ";
				} else {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " sw.arriving_date >= '" + initialArrivingDateFilter + "' ";
				}
			}
		}

		if (initialOpenedDateFilter != null) {
			if (estrictOpenedDateFilter) {
				if (!"".equals(filter)) {
					filter += " AND ";
				}
				from = fmtLongDate.parseDateTime(fmtShortDate.print(initialOpenedDateFilter)
						+ " 00:00:00");
				to = fmtLongDate.parseDateTime(fmtShortDate.print(initialOpenedDateFilter)
						+ " 23:59:59");
				filter += " sw.opened_date >= '" + from + "' AND sw.opened_date <= '" + to + "' ";
			} else {
				if (finalOpenedDateFilter != null) {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " (sw.opened_date >= '" + initialOpenedDateFilter
							+ "' AND sw.opened_date <= '" + finalOpenedDateFilter + "') ";
				} else {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " sw.opened_date >= '" + initialOpenedDateFilter + "' ";
				}
			}
		}

		if (initialDeadlineDateFilter != null) {
			if (estrictDeadlineDateFilter) {
				if (!"".equals(filter)) {
					filter += " AND ";
				}
				filter += " sw.deadline_date = '" + initialDeadlineDateFilter;
			} else {
				if (finalDeadlineDateFilter != null) {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " (sw.deadline_date >= '" + initialDeadlineDateFilter
							+ "' AND sw.deadline_date <= '" + finalDeadlineDateFilter + "') ";
				} else {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " sw.deadline_date >= '" + initialDeadlineDateFilter + "' ";
				}
			}
		}

		if (initialDecidedDateFilter != null) {
			if (estrictDecidedDateFilter) {
				if (!"".equals(filter)) {
					filter += " AND ";
				}
				from = fmtLongDate.parseDateTime(fmtShortDate.print(initialDecidedDateFilter)
						+ " 00:00:00");
				to = fmtLongDate.parseDateTime(fmtShortDate.print(initialDecidedDateFilter)
						+ " 23:59:59");
				filter += " sw.decided_date >= '" + from + "' AND sw.decided_date <= '" + to + "' ";
			} else {
				if (finalDecidedDateFilter != null) {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " (sw.decided_date >= '" + initialDecidedDateFilter
							+ "' AND sw.decided_date <= '" + finalDecidedDateFilter + "') ";
				} else {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " sw.decided_date >= '" + initialDecidedDateFilter + "' ";
				}
			}
		}

		logger.debug("QUERY FILTER:" + filter);

		return filter;
	}

	private String buildWorkingStepQuery(String filter, String action) {

		String tmpQuery = "SELECT ";
		tmpQuery += " pw.id_process AS idProcess, ";  //0
		tmpQuery += " sw.id_current_step, "; //1
		tmpQuery += " sh.name step_name, "; //2
		tmpQuery += " sw.arriving_date, "; //3
		tmpQuery += " sw.opened_date, "; //4 
		tmpQuery += " sw.opener_user, "; //5
		tmpQuery += " sw.decided_date, "; //6
		tmpQuery += " sw.performer_user_id, "; //7
		tmpQuery += " sw.deadline_date, "; //8
		tmpQuery += " sw.deadline_time, "; //9
		tmpQuery += " pw.reference, "; //10
		tmpQuery += " sw.locked, "; //11
		tmpQuery += " sw.locked_by, "; //12
		tmpQuery += " sw.id AS stepWorkId, "; //13
		tmpQuery += " opener.login AS opener_login, "; //14
		tmpQuery += " opener.name opener_name, "; //15
		tmpQuery += " performer.login AS performer_login, ";//16
		tmpQuery += " performer.name AS performer_name, ";//17
		tmpQuery += " pw.comments, "; //18
		tmpQuery += " wsrd.name AS responseName, ";  //19 nes 20151018
		tmpQuery += " pw.id AS idProcessWork, ";  //20 dml 20150413
		tmpQuery += " sw.response "; //21 rrl 20150409 ITS: 917 - nes 20151018 - pase a Integer id
		
		tmpQuery += " FROM w_step_work sw ";
		tmpQuery += " LEFT OUTER JOIN w_step_def step ON step.id = sw.id_current_step ";
		tmpQuery += " LEFT OUTER JOIN w_step_head sh  ON step.head_id = sh.id ";
		tmpQuery += " LEFT OUTER JOIN w_process_work pw ON pw.id = sw.id_work ";
		tmpQuery += " LEFT OUTER JOIN w_process_def wpd ON wpd.id = pw.id_process ";
		tmpQuery += " LEFT OUTER JOIN w_user_def opener ON opener.id = sw.opener_user "; 
		tmpQuery += " LEFT OUTER JOIN w_user_def performer ON performer.id = sw.performer_user_id ";
		tmpQuery += " LEFT OUTER JOIN w_step_response_def wsrd ON wsrd.id = sw.response "; // nes 20151018 

		tmpQuery += filter;

		if (action == null || action.equals("")) {
			tmpQuery += " ORDER by sw.arriving_date DESC; ";
		} 

		logger.debug("------>> finderStepWork -> query:" + tmpQuery
				+ "<<-------");

		return tmpQuery;
	}

	@SuppressWarnings("unchecked")
	private List<StepWorkLight> getWorkingStepList(String query)
			throws WStepWorkException {

		Integer idProcessWork;
		Integer idProcess;
		Integer idStep;
		String stepName;
		DateTime arrivingDate;
		DateTime openedDate;
		Integer openerUser;
		DateTime decidedDate;
		Integer performer;
		LocalDate deadlineDate;
		LocalTime deadlineTime;
		String reference;
		String comments;
		String response; // nes 20151018 - queda el nombre de la response
		Integer idResponse; // nes 20151018 - el id
		
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

					//arrivingDate = (cols[3] != null ? (Date) cols[3] : null);
					arrivingDate = (cols[3] != null ? new TimestampColumnDateTimeMapper().fromNonNullValue((Timestamp) cols[3]) : null);

					//openedDate = (cols[4] != null ? (Date) cols[4] : null);
					openedDate = (cols[4] != null ? new TimestampColumnDateTimeMapper().fromNonNullValue((Timestamp) cols[4]) : null);

					openerUser = (cols[5] != null ? new Integer(cols[5].toString()) : null);
					
					//decidedDate = (cols[6] != null ? (Date) cols[6] : null);
					decidedDate = (cols[6] != null ? new TimestampColumnDateTimeMapper().fromNonNullValue((Timestamp) cols[6]) : null);
					
					performer = (cols[7] != null ? new Integer(
							cols[7].toString()) : null);
					
					//deadlineDate = (cols[8] != null ? (Date) cols[8] : null);
					deadlineDate = (cols[8] != null ? new DateColumnLocalDateMapper().fromNonNullValue((java.sql.Date) cols[8]) : null);

					//deadlineTime = (cols[9] != null ? (Date) cols[9] : null);
					deadlineTime = (cols[9] != null ? new TimeColumnLocalTimeMapper().fromNonNullValue((java.sql.Time) cols[9]) : null);
					
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
					comments = (cols[18] != null ? cols[18].toString() : "");
					response = (cols[19] != null ? cols[19].toString() : "");  //rrl 20150409 ITS: 917 // nes 20151018 - pasa a ser Integer
					idResponse = (cols[21] != null ? new Integer(cols[21].toString()) : null); // nes 20151018
					
					// dml 20150413
					idProcessWork = (cols[20] != null ? new Integer(
							cols[20].toString()) : null);
					
					returnList.add(new StepWorkLight(idProcessWork, idProcess, idStep, stepName, 
							reference, comments, arrivingDate, openedDate, openerUser, decidedDate, 
							performer, deadlineDate, deadlineTime, locked, lockedBy, idStepWork, 
							openerUserLogin, openerUserName, performerLogin, performerName, response, idResponse));
				}

			} else {

				returnList = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getWorkingProcessStepListByFinder() - " +
					"It cannot be posible to get the StepWorkLight list - "
				+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " + (ex.getCause()!=null?ex.getCause():"");
			logger.warn( mess );
			throw new WStepWorkException(ex);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getWorkingProcessStepListByFinder() - " +
					"It cannot be posible to get the StepWorkLight list - "
					+ ex.getMessage() + " " + ex.getLocalizedMessage() + " " + (ex.getCause()!=null?ex.getCause():"")+" "+
					ex.getClass();
			logger.warn( mess );
			throw new WStepWorkException(ex);
			
		}

		return returnList;
	}
	
	
}
	