package org.beeblos.bpm.core.bl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.ManageStepTypeDefEmailDaemonConfDao;
import org.beeblos.bpm.core.error.ManageStepTypeDefEmailDaemonConfException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.bpmn.MessageBegin;
import org.beeblos.bpm.core.model.noper.EmailDConfBeeBPM;
import org.joda.time.DateTime;

import com.sp.daemon.bl.DaemonConfBL;
import com.sp.daemon.email.EmailDConf;
import com.sp.daemon.error.DaemonConfException;
import com.sp.daemon.util.EmailDaemonConfigurationList;

public class ManageStepTypeDefEmailDaemonConfBL {

	private static final Log logger = LogFactory
			.getLog(ManageStepTypeDefEmailDaemonConfBL.class);

	public ManageStepTypeDefEmailDaemonConfBL() {

	}

	/**
	 * Saves the new relation with the EmailDaemonConf into the "stepTypeConfiguration" field 
	 * of the WStepDef
	 * 
	 * @author dmuleiro 20150424
	 * 
	 * @param idStepDef
	 * @param idEmailDConf
	 * @param idCurrentUser
	 * @throws ManageStepTypeDefEmailDaemonConfException
	 */
	public void addNewEmailDConfToStepDef(Integer idStepDef,
			Integer idEmailDConf, Integer idCurrentUser)
			throws ManageStepTypeDefEmailDaemonConfException {

		logger.debug("addNewEmailDConfToStepDef to stepdef = " + idStepDef 
				+ " with idEmailDConf = " + idEmailDConf);

		/**
		 * Loading step def to add the new relation
		 */
		WStepDef stepDef = null;
		try {
			stepDef = new WStepDefBL().getWStepDefByPK(idStepDef, null, idCurrentUser);
		} catch (WStepDefException e) {
			String mess = "Error trying to load step def with id: " + idStepDef
					+ (e.getMessage()!=null?". "+e.getMessage():"")
					+ (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new ManageStepTypeDefEmailDaemonConfException(mess, e);
		}
		
		if (stepDef == null || stepDef.getId() == null || stepDef.getId().equals(0)){
			throw new ManageStepTypeDefEmailDaemonConfException("Step def is not valid");
		}

		/**
		 * Controlling if EmailDConf already exists into the WStepDef
		 */
		this._existsRelation(stepDef, idEmailDConf);

		try {
		
			EmailDConf edc = (EmailDConf) new DaemonConfBL().getDaemonConfSubObjectByPK(
					idEmailDConf, EmailDConf.class);
		
			/**
			 * Adding relation to step def depending on the step type
			 */
			if (edc != null 
					&& stepDef.getStepTypeDef() instanceof EmailDaemonConfigurationList){
				((EmailDaemonConfigurationList) stepDef.getStepTypeDef()).getEmailDaemonConfiguration().add(edc);
			}

		} catch (DaemonConfException e) {
			String mess = "Error trying to load email daemon configuration with id: " + idEmailDConf
					+ (e.getMessage()!=null?". "+e.getMessage():"")
					+ (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new ManageStepTypeDefEmailDaemonConfException(mess, e);
		}
		
		/**
		 * Updating the step def...
		 */
		try {
			new WStepDefBL().updateStepTypeConfigurationField(stepDef, idCurrentUser);
		} catch (WStepDefException e) {
			String mess = "Error trying to update stepTypeConfiguration field of step def: " + idStepDef
					+ (e.getMessage()!=null?". "+e.getMessage():"")
					+ (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new ManageStepTypeDefEmailDaemonConfException(mess, e);
		}
		
	}

	/**
	 * Deletes the relation between the EmailDaemonConf and the WStepTypeDef and saves the 
	 * deletion into the "stepTypeConfiguration" field from the WStepDef
	 * 
	 * @author dmuleiro 20150424
	 * 
	 * @param stepDef
	 * @param instance
	 * @param idCurrentUser
	 * @throws ManageStepTypeDefEmailDaemonConfException
	 */
	public void removeEmailDConfFromStepDef(
			WStepDef stepDef, Integer idEmailDConf, Integer idCurrentUser)
			throws ManageStepTypeDefEmailDaemonConfException {

		logger.debug("removeEmailDConfFromStepDef()");

		if (stepDef == null || stepDef.getId() == null || stepDef.getId().equals(0)){
			throw new ManageStepTypeDefEmailDaemonConfException("Step def is not valid");
		}

		if (idEmailDConf == null || idEmailDConf.equals(0)){
			throw new ManageStepTypeDefEmailDaemonConfException("Email daemon configuration id is not valid");
		}

		if (stepDef.getStepTypeDef() instanceof EmailDaemonConfigurationList){
			
			EmailDaemonConfigurationList edcl = (EmailDaemonConfigurationList) stepDef.getStepTypeDef();
			
			if (edcl.getEmailDaemonConfiguration() != null && !edcl.getEmailDaemonConfiguration().isEmpty()){
				
				for (EmailDConf edc : edcl.getEmailDaemonConfiguration()){
					
					if (edc != null && edc.getId() != null
							&& edc.getId().equals(idEmailDConf)){

						edcl.getEmailDaemonConfiguration().remove(edc);

						/**
						 * Updating the step def...
						 */
						try {
							new WStepDefBL().updateStepTypeConfigurationField(stepDef, idCurrentUser);
						} catch (WStepDefException e) {
							String mess = "Error trying to update stepTypeConfiguration field of step def: "
									+ stepDef.getId()
									+ (e.getMessage()!=null?". "+e.getMessage():"")
									+ (e.getCause()!=null?". "+e.getCause():"");
							logger.error(mess);
							throw new ManageStepTypeDefEmailDaemonConfException(mess, e);
						}

						return;
						
					}
				}

				String mess = "The email daemon configuration with id: " + idEmailDConf
						+ " was not find in the step def";
				logger.error(mess);
				throw new ManageStepTypeDefEmailDaemonConfException(mess);

			}
		}

	}

	/**
	 * If relation exists throws an exception
	 * 
	 * @author dmuleiro 20150424
	 * 
	 * @param stepDef
	 * @param idEmailDConf
	 * @throws ManageStepTypeDefEmailDaemonConfException
	 */
	private void _existsRelation(WStepDef stepDef, Integer idEmailDConf)
			throws ManageStepTypeDefEmailDaemonConfException {
		
		if (stepDef != null){
			
			if (stepDef.getStepTypeDef() instanceof MessageBegin){
				
				MessageBegin mb = (MessageBegin) stepDef.getStepTypeDef();
				
				if (mb.getEmailDaemonConfiguration() != null && !mb.getEmailDaemonConfiguration().isEmpty()){
					
					for (EmailDConf edc : mb.getEmailDaemonConfiguration()){
						
						if (edc != null && edc.getId() != null
								&& edc.getId().equals(idEmailDConf)){
							
							throw new ManageStepTypeDefEmailDaemonConfException(
									"Relation with email daemon configuration with id: " 
											+ idEmailDConf + " already exist in this step def");
							
						}
					}
				}
			}
		}
	}
	
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
	public List<EmailDConfBeeBPM> getEmailDConfListByProcessAndStep(
			Integer processDefId, Integer stepDefId) 
			throws ManageStepTypeDefEmailDaemonConfException {
	
		DateTime begin = new DateTime();
		
		List<EmailDConfBeeBPM> list =
				new ManageStepTypeDefEmailDaemonConfDao().getEmailDConfListByProcessAndStep(processDefId, stepDefId);
	
		DateTime end = new DateTime();
		
		Double time = ((end.getMillis() - begin.getMillis())/(double) 1000);
		logger.debug("Tiempo consulta (s) para ManageStepTypeDefEmailDaemonConfBL.getEmailDConfListByProcessAndStep: " + time);

		return list;
	}
}