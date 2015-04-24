package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.ManageStepTypeDefEmailDaemonConfException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.noper.EmailDConfBeeBPM;
import org.beeblos.bpm.core.util.StepDefStepTypeConfigurationUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.sp.common.util.HibernateUtil;
import com.sp.daemon.email.EmailDConf;
import com.sp.daemon.util.EmailDaemonConfigurationList;

/**
 * RequestType role dao.
 * 
 * @author dml 20141031
 *
 */
public class ManageStepTypeDefEmailDaemonConfDao {

	private static final Log logger = LogFactory.getLog(ManageStepTypeDefEmailDaemonConfDao.class.getName());

	public ManageStepTypeDefEmailDaemonConfDao() {

	}
	
	/**
	 * Gets all the EmailDConfBeeBPM related with any WStepDef which supports having EmailDaemonConfs
	 * 
	 * It could be filtered by processDefId and stepDefId
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
			throws ManageStepTypeDefEmailDaemonConfException {
	
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
						
						if (wsd.getStepTypeDef() != null 
								&& wsd.getStepTypeDef() instanceof EmailDaemonConfigurationList){
							
							EmailDaemonConfigurationList edcl = 
									(EmailDaemonConfigurationList) wsd.getStepTypeDef();
							
							if (edcl.getEmailDConfs() != null && !edcl.getEmailDConfs().isEmpty()){
								
								for (EmailDConf edc : edcl.getEmailDConfs()) {
									
									list.add(new EmailDConfBeeBPM(
											(cols[0]!=null)?new Integer(cols[0].toString()):null, 
											(cols[1]!=null)?cols[1].toString():null, 
											wsd.getId(),
											wsd.getName(),
											edc));
									
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
			throw new ManageStepTypeDefEmailDaemonConfException("WStepDefDao: getWStepDefs() - can't obtain EmailDConfBeeBPM list: "
					+ ex.getMessage()+" "+ex.getCause());
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("Exception: getEmailDConfListByProcessAndStep() - can't obtain EmailDConfBeeBPM list - " +
					ex.getMessage()+" "+ex.getCause() );
			throw new ManageStepTypeDefEmailDaemonConfException("WStepDefDao: getWStepDefs() - can't obtain EmailDConfBeeBPM list: "
					+ ex.getMessage()+" "+ex.getCause());
		}

		return list;
	
	}	

}