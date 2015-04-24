package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.bpmn.MessageBegin;
import org.beeblos.bpm.core.model.bpmn.StepTypeDefEmailDConf;
import org.beeblos.bpm.core.model.noper.EmailDConfBeeBPM;
import org.beeblos.bpm.core.util.StepDefStepTypeConfigurationUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.sp.common.util.HibernateUtil;

/**
 * RequestType role dao.
 * 
 * @author dml 20141031
 *
 */
public class StepTypeDefEmailDConfDao {

	private static final Log logger = LogFactory.getLog(StepTypeDefEmailDConfDao.class.getName());

	public StepTypeDefEmailDConfDao() {

	}
	
/*
	public Integer add(StepTypeDefEmailDConf instance) throws StepTypeDefEmailDConfException {

		logger.debug("add() StepTypeDefEmailDConf - emailDConf/stepTypeDef: [" 
				+ ((instance!=null&&instance.getEmailDConf()!=null)?instance.getEmailDConf().getId():"null") + ", " 
				+ instance.getStepTypeDefId() + "]");

		try {

			return Integer.valueOf(HibernateUtil.save(instance));

		} catch (HibernateException ex) {
			String mess = "HibernateException: add - Can't add instance definition "
					+ " id emailDConf/stepTypeDef: " 
					+ ((instance!=null&&instance.getEmailDConf()!=null)?instance.getEmailDConf().getId():"null") + ", " 
					+ instance.getStepTypeDefId()
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new StepTypeDefEmailDConfException(mess);

		} catch (Exception ex) {
			String mess = "Exception: add - Can't add instance definition "
					+ " id emailDConf/stepTypeDef: " 
					+ ((instance!=null&&instance.getEmailDConf()!=null)?instance.getEmailDConf().getId():"null") + ", " 
					+ instance.getStepTypeDefId() 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new StepTypeDefEmailDConfException(mess);

		}

	}

	public void delete(StepTypeDefEmailDConf instance) throws StepTypeDefEmailDConfException {

		logger.debug("delete() StepTypeDefEmailDConf - emailDConf/stepTypeDef: [" 
				+ ((instance!=null&&instance.getEmailDConf()!=null)?instance.getEmailDConf().getId():"null") + ", " 
				+ instance.getStepTypeDefId() + "]");

		try {

			instance = getStepTypeDefEmailDConfByPK(instance.getId());

			HibernateUtil.delete(instance);

		} catch (HibernateException ex) {
			String mess = "HibernateException: delete - Can't delete instance definition "
					+ " id emailDConf/stepTypeDef: " 
					+ ((instance!=null&&instance.getEmailDConf()!=null)?instance.getEmailDConf().getId():"null") + ", " 
					+ instance.getStepTypeDefId() 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new StepTypeDefEmailDConfException(mess);

		} catch (Exception ex) {
			String mess = "Exception: delete - Can't delete instance definition "
					+ " id emailDConf/stepTypeDef: " 
					+ ((instance!=null&&instance.getEmailDConf()!=null)?instance.getEmailDConf().getId():"null") + ", " 
					+ instance.getStepTypeDefId() 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new StepTypeDefEmailDConfException(mess);

		}

	}

	public StepTypeDefEmailDConf getStepTypeDefEmailDConfByPK(Integer id) throws StepTypeDefEmailDConfException {

		StepTypeDefEmailDConf instance = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			instance = (StepTypeDefEmailDConf) session.get(StepTypeDefEmailDConf.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getStepTypeDefEmailDConfByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new StepTypeDefEmailDConfException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getStepTypeDefEmailDConfByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"")+ " "
					+ ex.getClass();
			logger.warn(mess);
			throw new StepTypeDefEmailDConfException(mess);			

		}

		return instance;
	}

	public boolean existsRelation(Integer idStepTypeDef, Integer idEmailDConf)throws StepTypeDefEmailDConfException {

		Integer id = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			id = (Integer) session
						.createQuery("Select id From StepTypeDefEmailDConf Where emailDConf.id = :idEmailDConf And stepTypeDefId = :idStepTypeDef ")
						.setInteger("idEmailDConf", idEmailDConf)
						.setInteger("idStepTypeDef", idStepTypeDef)
						.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
			    tx.rollback(); 
			String mess = "HibernateException: existsRelation - Can't check if the relation with ids emailDConf/stepTypeDef = " 
					+ ((idEmailDConf != null)?idEmailDConf:"") + ", "
					+ ((idStepTypeDef != null)?idStepTypeDef:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"");
			logger.error(mess);
			throw new StepTypeDefEmailDConfException(mess);
		} catch (Exception ex){
			if (tx != null)
			    tx.rollback(); 
			String mess = "Exception: existsRelation - Can't check if the relation with ids emailDConf/stepTypeDef = " 
					+ ((idEmailDConf != null)?idEmailDConf:"") + ", "
					+ ((idStepTypeDef != null)?idStepTypeDef:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"") + " " + ex.getClass();
			logger.error(mess);
			throw new StepTypeDefEmailDConfException(mess);
		}

		if (id == null){
			return false;
		}
		return true;
	}
*/
	
	/**
	 * Gets all the EmailDConfBeeBPM related with any WStepDef (it could be filtered by 
	 * processDefId and stepDefId)
	 * 
	 * @author dmuleiro 20150421
	 * 
	 * @param processDefId
	 * @param stepDefId
	 * @return
	 * @throws WStepDefException
	 */
	@SuppressWarnings("unchecked")
	public List<EmailDConfBeeBPM> getEmailDConfListByProcessAndStep(
			Integer processDefId, Integer stepDefId) 
			throws WStepDefException {
	
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<EmailDConfBeeBPM> list = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			String hqlQuery = " Select wpd.id, wpd.process.name, wsd ";
			
			hqlQuery += " From WStepDef wsd, WProcessDef wpd ";
			hqlQuery += " Left Outer Join wpd.steps As step ";
			
			hqlQuery += " Where wsd.id = step.id ";
			
			/**
			 * This filters only the wsd which contains any "emailDConf"
			 */
			hqlQuery += " And wsd.stepTypeConfiguration Like '%<emailDConfs>%' ";
			
			if (stepDefId  != null && !stepDefId.equals(0)){
				hqlQuery += " And wsd.id = :stepDefId ";
			} else if (processDefId  != null && !processDefId.equals(0)){
				hqlQuery += " And wpd.id = :processDefId ";
			}
			
			Query query = session.createQuery(hqlQuery);
			
			/**
			 * If "stepDef" is set we have not to put "processDef" because it is implicit
			 */
			if (stepDefId  != null && !stepDefId.equals(0)){
				query.setParameter("stepDefId", stepDefId);
			} else if (processDefId  != null && !processDefId.equals(0)){
				query.setParameter("processDefId", processDefId);
			}
			
			List<Object[]> objectList = query.list();
			
			tx.commit();

			if (objectList != null){
				
				list = new ArrayList<EmailDConfBeeBPM>();
				
				for (Object[] cols : objectList){
					
					if (cols[2] != null && cols[2] instanceof WStepDef){
						
						WStepDef wsd = (WStepDef) cols[2];
						
						StepDefStepTypeConfigurationUtil.recoverStepTypeConfigurationFromXml(wsd);
						
						if (wsd.getStepTypeDef() != null && wsd.getStepTypeDef() instanceof MessageBegin){
							
							MessageBegin mb = (MessageBegin) wsd.getStepTypeDef();
							
							if (mb.getEmailDConfs() != null && !mb.getEmailDConfs().isEmpty()){
								
								for (StepTypeDefEmailDConf object : mb.getEmailDConfs()) {
									
									list.add(new EmailDConfBeeBPM(
											(cols[0]!=null)?new Integer(cols[0].toString()):null, 
											(cols[1]!=null)?cols[1].toString():null, 
											wsd.getId(),
											wsd.getName(),
											object.getEmailDConf()));
									
								}
								
							}
							
						}
					}
					
				}
			}
			
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("HibernateException: getEmailDConfListByProcessAndStep() - can't obtain EmailDConfBeeBPM list - " +
					ex.getMessage()+" "+ex.getCause() );
			throw new WStepDefException("WStepDefDao: getWStepDefs() - can't obtain EmailDConfBeeBPM list: "
					+ ex.getMessage()+" "+ex.getCause());
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("Exception: getEmailDConfListByProcessAndStep() - can't obtain EmailDConfBeeBPM list - " +
					ex.getMessage()+" "+ex.getCause() );
			throw new WStepDefException("WStepDefDao: getWStepDefs() - can't obtain EmailDConfBeeBPM list: "
					+ ex.getMessage()+" "+ex.getCause());
		}

		return list;
	
	}	

}