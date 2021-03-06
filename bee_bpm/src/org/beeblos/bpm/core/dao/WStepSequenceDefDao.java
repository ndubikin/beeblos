package org.beeblos.bpm.core.dao;

import static org.beeblos.bpm.core.util.Constants.ALIVE;
import static org.beeblos.bpm.core.util.Constants.DELETED;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.hibernate.HibernateException;

import com.sp.common.util.HibernateUtil;
import com.sp.common.util.StringPair;



public class WStepSequenceDefDao {
	
	private static final Log logger = LogFactory.getLog(WStepSequenceDefDao.class.getName());
	
	public WStepSequenceDefDao (){
		
	}
	
	public Integer add(WStepSequenceDef stepSeq) throws WStepSequenceDefException {
		
		logger.debug("add() WStepSequenceDef - Name: ["+stepSeq.getFromStep()+"/"+stepSeq.getToStep()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.save(stepSeq));

		} catch (HibernateException ex) {
			logger.error("WStepSequenceDefDao: add - Can't store stepSeq definition record "+ 
					stepSeq.getFromStep()+"/"+stepSeq.getToStep()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: add - Can't store stepSeq definition record "+ 
					stepSeq.getFromStep()+"/"+stepSeq.getToStep()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WStepSequenceDef stepSeq) throws WStepSequenceDefException {
		
		logger.debug("update() WStepSequenceDef < id = "+stepSeq.getId()+">");
		
		try {

			HibernateUtil.update(stepSeq);


		} catch (HibernateException ex) {
			logger.error("WStepSequenceDefDao: update - Can't update stepSeq definition record "+ 
					stepSeq.getFromStep()+"/"+stepSeq.getToStep() +
					" - id = "+stepSeq.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WStepSequenceDefException("WStepSequenceDefDao: update - Can't update stepSeq definition record "+ 
					stepSeq.getFromStep()+"/"+stepSeq.getToStep()  +
					" - id = "+stepSeq.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	/**
	 * @author dmuleiro - 20130830
	 * 
	 * Updates the step's sequence "logic delete" putting the field "delete" as the user wants. 
	 *
	 * @param  Integer stepSequenceId
	 * @param  boolean deleted
	 * @param  Integer currentUserId
	 * 
	 * @return void
	 * 
	 * @throws WStepSequenceDefException 
	 * 
	 */
	public void updateStepSequenceDeletedField(Integer stepSequenceId, boolean deleted, Integer modUserId,
			Date modDate) throws WStepSequenceDefException {

		logger.debug("updateStepSequenceDeletedField() WStepSequenceDef < id = " + stepSequenceId + ">");

		try {

			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			session.createQuery(
					"UPDATE WStepSequenceDef SET deleted = :deleted, modUser = :modUserId, modDate = :modDate WHERE id = :stepSequenceId")
					.setParameter("deleted", deleted)
					.setParameter("stepSequenceId", stepSequenceId)
					.setParameter("modUserId", modUserId)
					.setParameter("modDate", modDate)
					.executeUpdate();

			tx.commit();

		} catch (HibernateException ex) {
			String message = "SlaDao: update - Can't update deleted field for stepSequence " + stepSequenceId
					+ " - and deleted = " + deleted + "\n - " + ex.getMessage() + "\n"
					+ ex.getCause();
			logger.error(message);
			throw new WStepSequenceDefException(message);

		}

	}

	public void deleteRoute(WStepSequenceDef stepSeq) throws WStepSequenceDefException {

		logger.debug("delete() WStepSequenceDef - Name: ["+stepSeq.getFromStep()+"/"+stepSeq.getToStep()+"]");
		
		try {

			stepSeq = getWStepSequenceDefByPK(stepSeq.getId());

			HibernateUtil.delete(stepSeq);

		} catch (HibernateException ex) {
			logger.error("WStepSequenceDefDao: delete - Can't delete proccess definition record "+ 
					stepSeq.getFromStep()+"/"+stepSeq.getToStep() +
					" <id = "+stepSeq.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao:  delete - Can't delete proccess definition record  "+
					stepSeq.getFromStep()+"/"+stepSeq.getToStep() +
					" <id = "+stepSeq.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

		} catch (WStepSequenceDefException e) {
			logger.error("WStepSequenceDefDao: delete - Exception in deleting stepSeq rec "+ 
					stepSeq.getFromStep()+"/"+stepSeq.getToStep() +
					" <id = "+stepSeq.getId()+ "> no esta almacenada \n"+" - "+e.getMessage()+"\n"+e.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: delete - Exception in deleting stepSeq rec "+ 
					stepSeq.getFromStep()+"/"+stepSeq.getToStep() +
					" <id = "+stepSeq.getId()+ "> not stored \n"+" - "+e.getMessage()+"\n"+e.getCause() );

		} 

	}
	
	// dml 20130705
	/**
	 * Deletes all process routes (the process itself)
	 * 
	 * @param process
	 * @throws WStepSequenceDefException
	 */
	public void deleteProcessRoutes(WProcessDef process) 
			throws WStepSequenceDefException {

		logger.debug("delete() by process & version - processName: [" + process.getName() + "]");

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			session.createQuery("delete from WStepSequenceDef where process=?")// AND version=?")
					.setParameter(0, process)
//					.setParameter(1, version)
					.executeUpdate();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepSequenceDefDao: getWStepSequenceDefByPK - error while trying to delete process id = "+
					process.getId() + "]  - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefByPK - error while trying to delete process id = : " + 
					process.getId() + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	// dml 20130827
	public boolean existsRoute(Integer routeId) throws WStepSequenceDefException {
	
		Integer storedId = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			storedId = (Integer) session.createSQLQuery("SELECT id FROM w_step_sequence_def WHERE id = " + routeId)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepSequenceDefDao: existsRoute - can't obtain route id = " +
					routeId + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: existsRoute - can't obtain route id: " + 
					routeId + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		if (storedId == null){
			return false;
		} else {
			return true;
		}
		
	}	

	public WStepSequenceDef getWStepSequenceDefByPK(Integer id) throws WStepSequenceDefException {

		WStepSequenceDef stepSeq = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			stepSeq = (WStepSequenceDef) session.get(WStepSequenceDef.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepSequenceDefDao: getWStepSequenceDefByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return stepSeq;
	}
	
	
//	public WStepSequenceDef getWStepSequenceDefByName(String name) throws WStepSequenceDefException {
//
//		WStepSequenceDef  stepSeq = null;
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
//			stepSeq = (WStepSequenceDef) session.createCriteria(WStepSequenceDef.class).add(
//					Restrictions.naturalId().set("name", name))
//					.uniqueResult();
//
//			tx.commit();
//
//		} catch (HibernateException ex) {
//			if (tx != null)
//				tx.rollback();
//			logger.warn("WStepSequenceDefDao: getWStepSequenceDefByName - can't obtain stepSeq name = " +
//					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
//			throw new WStepSequenceDefException("getWStepSequenceDefByName;  can't obtain stepSeq name: " + 
//					name + " - " + ex.getMessage()+"\n"+ex.getCause());
//
//		}
//
//		return stepSeq;
//	}

	
	@SuppressWarnings("unchecked")
	public List<WStepSequenceDef> getWStepSequenceDefs() throws WStepSequenceDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepSequenceDef> stepSeqs = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			stepSeqs = session.createQuery("From WStepSequenceDef ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WStepSequenceDefDao: getWStepSequenceDefs() - can't obtain stepSeq list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefs() - can't obtain stepSeq list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return stepSeqs;
	}

	// nes 20110216 - este método hay que estudiarlo para ver como podemos hacer para devolver la secuencia de pasos en el orden correcto ( o lo mas parecido al orden correcto )
	@SuppressWarnings("unchecked")
	public List<WStepSequenceDef> getFullWStepSequenceDefs(
			Integer idProcess, Integer version ) 
	throws WStepSequenceDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepSequenceDef> stepSeqs = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			stepSeqs = session
							.createQuery("From WStepSequenceDef WHERE process.id=? and version=?  ")
							.setParameter(0, idProcess)
							.setParameter(1, version)
							.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			logger.warn("WStepSequenceDefDao: getWStepSequenceDefs() - can't obtain stepSeq list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefs() - can't obtain stepSeq list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			logger.warn("WStepSequenceDefDao: getWStepSequenceDefs() - Ex - can't obtain stepSeq list - " +
					e.getMessage()+"\n"+e.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefs() - Ex - can't obtain stepSeq list: "
					+ e.getMessage()+"\n"+e.getCause());
		}

		return stepSeqs;
	}
	
	@SuppressWarnings("unchecked")
	public List<WStepSequenceDef> getStepSequenceList(
			Integer processId, Integer idFromStep ) 
	throws WStepSequenceDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepSequenceDef> stepSeqs = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			stepSeqs = session
							.createQuery("From WStepSequenceDef WHERE process.id=? and fromStep.id = ?  ")
							.setParameter(0, processId)
							.setParameter(1, idFromStep)
							.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			logger.warn("WStepSequenceDefDao: getWStepSequenceDefs() - can't obtain stepSeq list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefs() - can't obtain stepSeq list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			logger.warn("WStepSequenceDefDao: getWStepSequenceDefs() - Ex - can't obtain stepSeq list - " +
					e.getMessage()+"\n"+e.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefs() - Ex - can't obtain stepSeq list: "
					+ e.getMessage()+"\n"+e.getCause());
		}

		return stepSeqs;
	}

	/**
	 * @author dmuleiro 20130125 (dml 20130829 - added deleted where clause)
	 * 
	 * Returns the List<WStepSequenceDef> related with a concrete WProcessDef.
	 *
	 * @param Integer processId
	 * @param String deleted: null or "ALL": returns all routes, "DELETED": returns deleted routes, "ALIVE": returns alive routes
	 * @param Integer currentUserId
	 * 
	 * @return List<WStepSequenceDef>
	 * 
	 * @throws WStepSequenceDefException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<WStepSequenceDef> getStepSequenceList(
			Integer processId, String deleted) 
	throws WStepSequenceDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepSequenceDef> stepSeqs = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			if (deleted.equals(DELETED)){
				stepSeqs = session
						.createQuery("From WStepSequenceDef Where process.id=? And deleted IS TRUE Order By fromStep.id")
						.setParameter(0, processId)
						.list();				
			} else if (deleted.equals(ALIVE)){
				stepSeqs = session
						.createQuery("From WStepSequenceDef Where process.id=? And deleted IS NOT TRUE Order By fromStep.id")
						.setParameter(0, processId)
						.list();				

			} else { // ALL
				stepSeqs = session
						.createQuery("From WStepSequenceDef Where process.id=? Order By fromStep.id")
						.setParameter(0, processId)
						.list();				
			}

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			logger.warn("WStepSequenceDefDao: getWStepSequenceDefList() - can't obtain stepSeq list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefList() - can't obtain stepSeq list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			logger.warn("WStepSequenceDefDao: getWStepSequenceDefList() - Ex - can't obtain stepSeq list - " +
					e.getMessage()+"\n"+e.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefList() - Ex - can't obtain stepSeq list: "
					+ e.getMessage()+"\n"+e.getCause());
		}

		return stepSeqs;
	}

	// dml 20120323
	@SuppressWarnings("unchecked")
	public List<WStepSequenceDef> getOutgoingRoutes(
			Integer stepId, Boolean deleted, Integer processId ) 
	throws WStepSequenceDefException {
	
		if (stepId==null || stepId.equals(0)) {
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefListByToStep() - can't obtain stepSeq list (outgoing) for stepId ="
					+(stepId==null?"null":"0"));
		}
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepSequenceDef> stepSeqs = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			String processWhereClause = "";
			if (processId != null) {
				processWhereClause = " AND process_id= " + processId;
			} 

			String deletedWhereClause = "";
			if (deleted != null) {
				if (deleted){
					deletedWhereClause = " AND deleted IS TRUE ";
				} else {
					deletedWhereClause = " AND deleted IS NOT TRUE ";
				}
			} 
			
			String sqlQuery = " SELECT * FROM w_step_sequence_def ";
			sqlQuery += "WHERE id_origin_step = " + stepId;
			sqlQuery += processWhereClause + deletedWhereClause;
			sqlQuery += " ORDER BY id_origin_step ";
			
			stepSeqs = session.createSQLQuery(sqlQuery).addEntity(WStepSequenceDef.class).list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			logger.warn("WStepSequenceDefDao: getWStepSequenceDefListByFromStep() - can't obtain stepSeq list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefListByFromStep() - can't obtain stepSeq list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			logger.warn("WStepSequenceDefDao: getWStepSequenceDefListByFromStep() - Ex - can't obtain stepSeq list - " +
					e.getMessage()+"\n"+e.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefListByFromStep() - Ex - can't obtain stepSeq list: "
					+ e.getMessage()+"\n"+e.getCause());
		}

		return stepSeqs;

	}
	
	public Integer countOutgoingRoutes(
			Integer stepId, Integer processId ) 
	throws WStepSequenceDefException {
	
		if (stepId==null || stepId.equals(0)) {
			throw new WStepSequenceDefException("WStepSequenceDefDao: countOutgoingRoutes() - can't obtain stepSeq list (outgoing) for stepId ="
					+(stepId==null?"null":"0"));
		}
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		Long qtyOutgoingRoutes = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			if (processId != null && !processId.equals(0)) {
				qtyOutgoingRoutes = (Long) session
								.createQuery("SELECT COUNT (id) FROM WStepSequenceDef WHERE fromStep.id = ? and process.id=? ")
								.setParameter(0, stepId)
								.setParameter(1, processId)
								.uniqueResult();
			} else {
				qtyOutgoingRoutes = (Long) session
						.createQuery("SELECT COUNT (id) FROM WStepSequenceDef WHERE fromStep.id = ? ORDER BY fromStep.id")
						.setParameter(0, stepId)
						.uniqueResult();
			}
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			logger.warn("WStepSequenceDefDao: countOutgoingRoutes() - can't obtain stepSeq list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: countOutgoingRoutes() - can't obtain stepSeq list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			logger.warn("WStepSequenceDefDao: getWStepSequenceDefListByFromStep() - Ex - can't obtain stepSeq list - " +
					e.getMessage()+"\n"+e.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefListByFromStep() - Ex - can't obtain stepSeq list: "
					+ e.getMessage()+"\n"+e.getCause());
		}

		if (qtyOutgoingRoutes != null){
			return qtyOutgoingRoutes.intValue();
		} else {
			return 0;
		}

	}	
	
	// dml 20120323
	@SuppressWarnings("unchecked")
	public List<WStepSequenceDef> getIncomingRoutes(
			Integer stepId, Boolean deleted, Integer processId) 
	throws WStepSequenceDefException {
	
		if (stepId==null || stepId.equals(0)) {
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefListByToStep() - can't obtain stepSeq list (incoming) for stepId ="
					+(stepId==null?"null":"0"));
		}
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WStepSequenceDef> stepSeqs = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			String processWhereClause = "";
			if (processId != null) {
				processWhereClause = " AND process_id= " + processId;
			} 

			String deletedWhereClause = "";
			if (deleted != null) {
				if (deleted){
					deletedWhereClause = " AND deleted IS TRUE ";
				} else {
					deletedWhereClause = " AND deleted IS NOT TRUE ";
				}
			} 
			
			String sqlQuery = " SELECT * FROM w_step_sequence_def ";
			sqlQuery += "WHERE id_dest_step = " + stepId;
			sqlQuery += processWhereClause + deletedWhereClause;
			sqlQuery += " ORDER BY id_dest_step ";
			
			stepSeqs = session.createSQLQuery(sqlQuery).addEntity(WStepSequenceDef.class).list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			logger.warn("WStepSequenceDefDao: getWStepSequenceDefListByToStep() - can't obtain stepSeq list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefListByToStep() - can't obtain stepSeq list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			logger.warn("WStepSequenceDefDao: getWStepSequenceDefListByToStep() - Ex - can't obtain stepSeq list - " +
					e.getMessage()+"\n"+e.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefListByToStep() - Ex - can't obtain stepSeq list: "
					+ e.getMessage()+"\n"+e.getCause());
		}

		return stepSeqs;

	}
	
	public Integer countIncomingRoutes(
			Integer stepId, Integer processId) 
	throws WStepSequenceDefException {
	
		if (stepId==null || stepId.equals(0)) {
			throw new WStepSequenceDefException("WStepSequenceDefDao: countIncomingRoutes() - can't count stepseq (incoming) for stepId ="
					+(stepId==null?"null":"0"));
		}
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		Long qtyIncomingRoutes = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			if (processId != null) {
				qtyIncomingRoutes = (Long) session
								.createQuery("SELECT COUNT (id) FROM WStepSequenceDef WHERE toStep.id = ? and process.id=? ")
								.setParameter(0, stepId)
								.setParameter(1, processId)
								.uniqueResult();
			} else {
				qtyIncomingRoutes = (Long) session
						.createQuery("SELECT COUNT (id) FROM WStepSequenceDef WHERE toStep.id = ? ORDER BY toStep.id")
						.setParameter(0, stepId)
						.uniqueResult();
			}

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			logger.warn("WStepSequenceDefDao: countIncomingRoutes() - error executing query - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: countIncomingRoutes() - error executing query - "
					+ ex.getMessage()+"\n"+ex.getCause());

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			logger.warn("WStepSequenceDefDao: getWStepSequenceDefListByToStep() - Ex - can't obtain stepSeq list - " +
					e.getMessage()+"\n"+e.getCause() );
			throw new WStepSequenceDefException("WStepSequenceDefDao: getWStepSequenceDefListByToStep() - Ex - can't obtain stepSeq list: "
					+ e.getMessage()+"\n"+e.getCause());
		}

		if (qtyIncomingRoutes != null){
			return qtyIncomingRoutes.intValue();
		} else {
			return 0;
		}

	}	
	
	
	// nes 20101217
	// returns a list with step names
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			Integer idProcess, Integer version,
			String firstLineText, String blank )
	throws WProcessDefException {
		 
			List<Object> lwsd = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwsd = session
							.createQuery("select distinct wssd.id, wssd.fromStep.name, wssd.fromStep.stepComments from WStepSequenceDef wssd WHERE process.id=? and version=?  order by wssd.fromStep.name")
							.setParameter(0, idProcess)
							.setParameter(1, version)
							.list();

				tx.commit();

				if (lwsd!=null) {
					
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
					
					String nombrePaso="";
					for (Object wsd: lwsd) {
						Object [] cols= (Object []) wsd;
						
						nombrePaso=(cols[1]!=null ? cols[1].toString().trim():"") +" "+(cols[2]!=null ? cols[2].toString().trim():""); 
						
						retorno.add(new StringPair(
										(cols[0]!=null ? new Integer(cols[0].toString()):null),
										nombrePaso ) );
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
/* dml 20130926 NO EXISTEN VERSION EN EL WSTEPSEQUENCEDEF	
	// nes 20101217
	public Integer getLastVersionWStepSequenceDef (
			Integer idProcess ) 
	throws WStepSequenceDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		org.hibernate.Query q = null;
		Integer maxVersion = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			q = session.createQuery("select max(version) from WStepSequenceDef where process.id=:idProcess");
			
			q.setParameter("idProcess", idProcess);
			maxVersion = (Integer)q.uniqueResult();

			tx.commit();
			
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess="WStepSequenceDefDao: getLastVersionWStepSequenceDef() - can't obtain stepSeq list - " +
							ex.getMessage()+"\n"+ex.getCause();
			logger.warn( mess );
			throw new WStepSequenceDefException( mess );

		}

		return maxVersion;
	}
*/	

}
	