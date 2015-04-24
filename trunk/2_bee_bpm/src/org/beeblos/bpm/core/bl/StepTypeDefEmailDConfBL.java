package org.beeblos.bpm.core.bl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.StepTypeDefEmailDConfDao;
import org.beeblos.bpm.core.error.StepTypeDefEmailDConfException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.bpmn.MessageBegin;
import org.beeblos.bpm.core.model.bpmn.StepTypeDefEmailDConf;
import org.beeblos.bpm.core.model.noper.EmailDConfBeeBPM;
import org.joda.time.DateTime;

import com.sp.daemon.bl.DaemonConfBL;
import com.sp.daemon.email.EmailDConf;
import com.sp.daemon.error.DaemonConfException;

public class StepTypeDefEmailDConfBL {

	private static final Log logger = LogFactory
			.getLog(StepTypeDefEmailDConfBL.class);

	public StepTypeDefEmailDConfBL() {

	}

	/**
	 * Creates the StepTypeDefEmailDConf and saves it into the "stepTypeConfiguration" field from the WStepDef
	 * 
	 * @author dmuleiro 20150424
	 * 
	 * @param idStepDef
	 * @param idEmailDConf
	 * @param idCurrentUser
	 * @throws StepTypeDefEmailDConfException
	 */
	public void addNewEmailDConfToStepDef(Integer idStepDef,
			Integer idEmailDConf, Integer idCurrentUser)
			throws StepTypeDefEmailDConfException {

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
			throw new StepTypeDefEmailDConfException(mess, e);
		}
		
		if (stepDef == null || stepDef.getId() == null || stepDef.getId().equals(0)){
			throw new StepTypeDefEmailDConfException("Step def is not valid");
		}

		/**
		 * Controlling if EmailDConf already exists into the WStepDef
		 */
		this._existsRelation(stepDef, idEmailDConf);

		/**
		 * Creating new relation
		 */
		StepTypeDefEmailDConf instance = new StepTypeDefEmailDConf();
		
		instance.setStepTypeDefId(stepDef.getStepTypeDef().getId());
		
		try {
		
			instance.setEmailDConf((EmailDConf) 
					new DaemonConfBL().getDaemonConfSubObjectByPK(idEmailDConf, EmailDConf.class));
		
		} catch (DaemonConfException e) {
			String mess = "Error trying to load email daemon configuration with id: " + idEmailDConf
					+ (e.getMessage()!=null?". "+e.getMessage():"")
					+ (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new StepTypeDefEmailDConfException(mess, e);
		}
		
		instance.setAddDate(new DateTime());
		instance.setAddUser(idCurrentUser);
		
		/**
		 * Adding relation to step def depending on the step type
		 */
		if (stepDef.getStepTypeDef() instanceof MessageBegin){
			((MessageBegin) stepDef.getStepTypeDef()).getEmailDConfs().add(instance);
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
			throw new StepTypeDefEmailDConfException(mess, e);
		}
		
	}

	/**
	 * Deletes the StepTypeDefEmailDConf and saves the deletion into the "stepTypeConfiguration" 
	 * field from the WStepDef
	 * 
	 * @author dmuleiro 20150424
	 * 
	 * @param stepDef
	 * @param instance
	 * @param idCurrentUser
	 * @throws StepTypeDefEmailDConfException
	 */
	public void removeEmailDConfFromStepDef(
			WStepDef stepDef, Integer idEmailDConf, Integer idCurrentUser)
			throws StepTypeDefEmailDConfException {

		logger.debug("removeEmailDConfFromStepDef()");

		if (stepDef == null || stepDef.getId() == null || stepDef.getId().equals(0)){
			throw new StepTypeDefEmailDConfException("Step def is not valid");
		}

		if (idEmailDConf == null || idEmailDConf.equals(0)){
			throw new StepTypeDefEmailDConfException("Email daemon configuration id is not valid");
		}

		if (stepDef.getStepTypeDef() instanceof MessageBegin){
			
			MessageBegin mb = (MessageBegin) stepDef.getStepTypeDef();
			
			if (mb.getEmailDConfs() != null && !mb.getEmailDConfs().isEmpty()){
				
				for (StepTypeDefEmailDConf stdedc : mb.getEmailDConfs()){
					
					if (stdedc.getEmailDConf() != null && stdedc.getEmailDConf().getId() != null
							&& stdedc.getEmailDConf().getId().equals(idEmailDConf)){

						mb.getEmailDConfs().remove(stdedc);

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
							throw new StepTypeDefEmailDConfException(mess, e);
						}

						return;
						
					}
				}

				String mess = "The email daemon configuration with id: " + idEmailDConf
						+ " was not find in the step def";
				logger.error(mess);
				throw new StepTypeDefEmailDConfException(mess);

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
	 * @throws StepTypeDefEmailDConfException
	 */
	private void _existsRelation(WStepDef stepDef, Integer idEmailDConf)
			throws StepTypeDefEmailDConfException {
		
		if (stepDef != null){
			
			if (stepDef.getStepTypeDef() instanceof MessageBegin){
				
				MessageBegin mb = (MessageBegin) stepDef.getStepTypeDef();
				
				if (mb.getEmailDConfs() != null && !mb.getEmailDConfs().isEmpty()){
					
					for (StepTypeDefEmailDConf stdedc : mb.getEmailDConfs()){
						
						if (stdedc.getEmailDConf() != null && stdedc.getEmailDConf().getId() != null
								&& stdedc.getEmailDConf().getId().equals(idEmailDConf)){
							
							throw new StepTypeDefEmailDConfException(
									"Relation with emaildconf with id: " + idEmailDConf + " already exist in this step def");
							
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
			throws WStepDefException {
	
		DateTime begin = new DateTime();
		
		List<EmailDConfBeeBPM> list =
				new StepTypeDefEmailDConfDao().getEmailDConfListByProcessAndStep(processDefId, stepDefId);
	
		DateTime end = new DateTime();
		
		Double time = ((end.getMillis() - begin.getMillis())/(double) 1000);
		logger.debug("Tiempo consulta (s) para StepTypeDefEmailDConfBL.getEmailDConfListByProcessAndStep: " + time);

		return list;
	}
}