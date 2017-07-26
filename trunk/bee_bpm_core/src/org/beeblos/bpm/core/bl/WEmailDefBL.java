package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.MANAGED_EMAIL_TEMPLATE_THEME_ID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WEmailDefDao;
import org.beeblos.bpm.core.error.WEmailDefException;
import org.beeblos.bpm.core.error.WRoleDefException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WEmailDef;
import org.joda.time.DateTime;

import com.sp.common.core.bl.EmailTemplateBL;
import com.sp.common.core.error.EmailTemplateException;
import com.sp.common.core.model.EmailTemplateTheme;

/**
 * Clase que gestiona los objetos WEmailDef
 * 
 * @author dmuleiro
 *
 */
public class WEmailDefBL {

	private static final Log logger = LogFactory.getLog(WEmailDefBL.class.getName());
	
	public WEmailDefBL (){
		
	}
	
	/**
	 * @param instance
	 * @param currentUserId
	 * @return
	 * @throws WEmailDefException
	 */
	public Integer add(WEmailDef instance, Integer currentUserId)
			throws WEmailDefException {

		_checkMinimumData(instance, currentUserId);
		
		_checkNewEmailTemplate(instance, currentUserId);
		
		instance.setAddUser(currentUserId);
		instance.setAddDate(new DateTime());

		return new WEmailDefDao().add(instance);

	}

	/**
	 * @param instance
	 * @param currentUserId
	 * @return
	 * @throws WEmailDefException
	 */
	public void update(WEmailDef instance, Integer currentUserId)
			throws WEmailDefException {

		_checkMinimumData(instance, currentUserId);
		
		_checkNewEmailTemplate(instance, currentUserId);

		instance.setModDate(DateTime.now());
		instance.setModUser(currentUserId);
		
		new WEmailDefDao().update(instance);
		
	}
	
	private void _checkMinimumData(WEmailDef instance, Integer currentUserId) throws WEmailDefException{
		
		if (instance != null){
			if (!instance.isSendEmailIsActive()){
				String mess = "If it is not marked the sendEmail as active we don't have to do de cheking";
				logger.debug(mess);
				return;
			}
			
			if ((instance.getRolesRelatedIdList() == null || instance.getRolesRelatedIdList().length == 0)
					&& (instance.getUsersRelatedIdList() == null || instance.getUsersRelatedIdList().length == 0)
					&& (instance.getOtherEmails() == null || "".equals(instance.getOtherEmails().trim()))){
				String mess = "If 'Send email' is active, the email should have almost one valid destination";
				logger.debug(mess);
				throw new WEmailDefException(mess);
			}
			
			if (instance.getEmailTemplate() == null 
					|| instance.getEmailTemplate().getDefaultEmailAccount() == null
					|| instance.getEmailTemplate().getDefaultEmailAccount().empty()){
				String mess = "If 'Send email' is active, the email should have a default email account";
				logger.debug(mess);
				throw new WEmailDefException(mess);
			}
		}
		
	}

	/**
	 * 
	 * 
	 * @author dmuleiro 20160525
	 * 
	 * @param emailDef
	 * @param currentUserId
	 * @throws WEmailDefException 
	 *
	 */
	public void loadRoleAndUserList(WEmailDef emailDef, Integer currentUserId) throws WEmailDefException {
		/**
		 * Loading roles related to show into the popup view - dml 201605125
		 */
		if (emailDef != null
				&& emailDef.getRolesRelatedIdList() != null
				&& emailDef.getRolesRelatedIdList().length != 0){
			
			try {
				emailDef.setRolesRelated(
						new WRoleDefBL().getWRoleDefByPkArray(
								emailDef.getRolesRelatedIdList(), currentUserId));
			} catch (WRoleDefException e) {
				String mess = "Error tryig to load role list from step sequence's email def"
						+ (e.getMessage()!=null?". "+e.getMessage():"")
						+ (e.getCause()!=null?". "+e.getCause():"");
				logger.error(mess);
				throw new WEmailDefException(mess);
			}
			
		}
		
		/**
		 * Loading roles related to show into the popup view - dml 201605125
		 */
		if (emailDef != null
				&& emailDef.getUsersRelatedIdList() != null
				&& emailDef.getUsersRelatedIdList().length != 0){
			
			try {
				emailDef.setUsersRelated(
						new WUserDefBL().getWUserDefByPkArray(
								emailDef.getUsersRelatedIdList(), currentUserId));
			} catch (WUserDefException e) {
				String mess = "Error tryig to load role list from step sequence's email def"
						+ (e.getMessage()!=null?". "+e.getMessage():"")
						+ (e.getCause()!=null?". "+e.getCause():"");
				logger.error(mess);
				throw new WEmailDefException(mess);
			}
			
		}
	}

	/**
	 * Checks if the email template associated is new and if it is it will save it before adding/updating
	 * the WEmailDef
	 * 
	 * @author dmuleiro 20160525
	 * 
	 * @param instance
	 * @param currentUserId
	 * @throws WEmailDefException
	 * 
	 */
	private void _checkNewEmailTemplate(WEmailDef instance, Integer currentUserId) throws WEmailDefException {
		
		if (instance != null && instance.getEmailTemplate() != null
				&& (instance.getEmailTemplate().getId() == null || instance.getEmailTemplate().getId().equals(0))){
			
			instance.getEmailTemplate().setName(
					instance.getIdObject() + " / " + instance.getIdObjectType());
			instance.getEmailTemplate().setEmailTemplateTheme(
					new EmailTemplateTheme(MANAGED_EMAIL_TEMPLATE_THEME_ID));
			try {
				Integer idET = new EmailTemplateBL().add(instance.getEmailTemplate(), currentUserId);
				
				instance.getEmailTemplate().setId(idET);
				
			} catch (EmailTemplateException e) {
				String mess = "Error trying to create the new associated Email Template "
						+ (e.getMessage()!=null?". "+e.getMessage():"")
						+ (e.getCause()!=null?". "+e.getCause():"");
				logger.error(mess);
				throw new WEmailDefException(mess);
			}
			
		}
	}

	public void delete(WEmailDef instance, Integer currentUserId)
			throws WEmailDefException {

		new WEmailDefDao().delete(instance);
		
	}

	/**
	 * @param id
	 * @param currentUserId
	 * @return
	 * @throws WEmailDefException
	 */
	public WEmailDef getByPK(Integer id, Integer currentUserId) 
			throws WEmailDefException {

		return new WEmailDefDao().getByPK(id);
	
	}

}
