package org.beeblos.bpm.core.model;

import static com.sp.common.util.ConstantsCommon.EMPTY_OBJECT;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

import com.sp.common.core.model.EmailTemplate;

/**
 * There are some objects in bpm which need to send and email. This is an instance which
 * contains all the data to send many emails to many people
 * 
 * @author dmuleiro 20160524
 */
public class WEmailDef implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 */
	private Integer id;
	
	private boolean sendEmailIsActive;
	
	/**
	 * If it is false, the email addresses will be set into the email's "To" field. If it is true, email
	 * addresses will be set into the "bcc" field
	 */
	private boolean emailDestinationFieldBcc;

	/**
	 * Object which will need this email sending
	 */
	private Integer idObject;
	private String idObjectType;
	
	/**
	 * Roles where the email will be sent
	 */
	private Integer[] rolesRelatedIdList;

	/**
	 * NOT PERSISTED
	 * 
	 * It will be loaded when the "idObject" related will be show in the view
	 */
	private List<WRoleDef> rolesRelated;
	
	/**
	 * Users where the email will be sent
	 */
	private Integer[] usersRelatedIdList;
	
	/**
	 * NOT PERSISTED
	 * 
	 * It will be loaded when the "idObject" related will be show in the view
	 */
	private List<WUserDef> usersRelated;
	
	/**
	 * Another emails where the email will be sent
	 */
	private String otherEmails;
	
	/**
	 * Email template which will have the email info to send in the object related
	 */
	private EmailTemplate emailTemplate;
		
	/**
	 * Datos de control
	 */
	private DateTime addDate;
	private Integer addUser;
	private DateTime modDate;
	private Integer modUser;
	
	public WEmailDef() {
		super();
	}
	
	public WEmailDef(boolean createEmptyObjets) {
		super();
		if (createEmptyObjets){
			this.emailTemplate = new EmailTemplate(EMPTY_OBJECT);
		}
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isEmailDestinationFieldBcc() {
		return emailDestinationFieldBcc;
	}

	public void setEmailDestinationFieldBcc(boolean emailDestinationFieldBcc) {
		this.emailDestinationFieldBcc = emailDestinationFieldBcc;
	}

	public void switchEmailDestinationFieldBcc() {
		this.emailDestinationFieldBcc = !this.emailDestinationFieldBcc;
	}

	public boolean isSendEmailIsActive() {
		return sendEmailIsActive;
	}

	public void setSendEmailIsActive(boolean sendEmailIsActive) {
		this.sendEmailIsActive = sendEmailIsActive;
	}

	public Integer getIdObject() {
		return idObject;
	}

	public void setIdObject(Integer idObject) {
		this.idObject = idObject;
	}

	public String getIdObjectType() {
		return idObjectType;
	}

	public void setIdObjectType(String idObjectType) {
		this.idObjectType = idObjectType;
	}

	public List<WRoleDef> getRolesRelated() {
		return rolesRelated;
	}

	public void setRolesRelated(List<WRoleDef> rolesRelated) {
		this.rolesRelated = rolesRelated;
	}

	public List<WUserDef> getUsersRelated() {
		return usersRelated;
	}

	public void setUsersRelated(List<WUserDef> usersRelated) {
		this.usersRelated = usersRelated;
	}

	public Integer[] getRolesRelatedIdList() {
		return rolesRelatedIdList;
	}

	public void setRolesRelatedIdList(Integer[] rolesRelatedIdList) {
		this.rolesRelatedIdList = rolesRelatedIdList;
	}

	public Integer[] getUsersRelatedIdList() {
		return usersRelatedIdList;
	}

	public void setUsersRelatedIdList(Integer[] usersRelatedIdList) {
		this.usersRelatedIdList = usersRelatedIdList;
	}

	public String getOtherEmails() {
		return otherEmails;
	}

	public void setOtherEmails(String otherEmails) {
		this.otherEmails = otherEmails;
	}

	public EmailTemplate getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(EmailTemplate emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public DateTime getAddDate() {
		return addDate;
	}

	public void setAddDate(DateTime addDate) {
		this.addDate = addDate;
	}

	public Integer getAddUser() {
		return addUser;
	}

	public void setAddUser(Integer addUser) {
		this.addUser = addUser;
	}

	public DateTime getModDate() {
		return modDate;
	}

	public void setModDate(DateTime modDate) {
		this.modDate = modDate;
	}

	public Integer getModUser() {
		return modUser;
	}

	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}

	@Override
	public String toString() {
		return "WEmailDef [id=" + id + ", sendEmailIsActive=" + sendEmailIsActive + ", emailDestinationFieldBcc="
				+ emailDestinationFieldBcc + ", idObject=" + idObject + ", idObjectType=" + idObjectType
				+ ", rolesRelatedIdList=" + rolesRelatedIdList + ", rolesRelated=" + rolesRelated
				+ ", usersRelatedIdList=" + usersRelatedIdList + ", usersRelated=" + usersRelated + ", otherEmails="
				+ otherEmails + ", emailTemplate=" + emailTemplate + ", addDate=" + addDate + ", addUser=" + addUser
				+ ", modDate=" + modDate + ", modUser=" + modUser + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (emailDestinationFieldBcc ? 1231 : 1237);
		result = prime * result + ((emailTemplate == null) ? 0 : emailTemplate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idObject == null) ? 0 : idObject.hashCode());
		result = prime * result + ((idObjectType == null) ? 0 : idObjectType.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
		result = prime * result + ((otherEmails == null) ? 0 : otherEmails.hashCode());
		result = prime * result + ((rolesRelatedIdList == null) ? 0 : rolesRelatedIdList.hashCode());
		result = prime * result + (sendEmailIsActive ? 1231 : 1237);
		result = prime * result + ((usersRelatedIdList == null) ? 0 : usersRelatedIdList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WEmailDef other = (WEmailDef) obj;
		if (emailDestinationFieldBcc != other.emailDestinationFieldBcc)
			return false;
		if (emailTemplate == null) {
			if (other.emailTemplate != null)
				return false;
		} else if (!emailTemplate.equals(other.emailTemplate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idObject == null) {
			if (other.idObject != null)
				return false;
		} else if (!idObject.equals(other.idObject))
			return false;
		if (idObjectType == null) {
			if (other.idObjectType != null)
				return false;
		} else if (!idObjectType.equals(other.idObjectType))
			return false;
		if (modDate == null) {
			if (other.modDate != null)
				return false;
		} else if (!modDate.equals(other.modDate))
			return false;
		if (modUser == null) {
			if (other.modUser != null)
				return false;
		} else if (!modUser.equals(other.modUser))
			return false;
		if (otherEmails == null) {
			if (other.otherEmails != null)
				return false;
		} else if (!otherEmails.equals(other.otherEmails))
			return false;
		if (rolesRelatedIdList == null) {
			if (other.rolesRelatedIdList != null)
				return false;
		} else if (!rolesRelatedIdList.equals(other.rolesRelatedIdList))
			return false;
		if (sendEmailIsActive != other.sendEmailIsActive)
			return false;
		if (usersRelatedIdList == null) {
			if (other.usersRelatedIdList != null)
				return false;
		} else if (!usersRelatedIdList.equals(other.usersRelatedIdList))
			return false;
		return true;
	}

	public boolean empty() {
		
		if (id!=null && !id.equals(0)) return false;
		
		if (sendEmailIsActive) return false;
		
		if (idObject!=null && !idObject.equals(0)) return false;
		if (idObjectType!=null && !"".equals(idObjectType)) return false;

		if (rolesRelatedIdList!=null && rolesRelatedIdList.length != 0) return false;
		if (usersRelated!=null && usersRelatedIdList.length != 0) return false;

		if (otherEmails!=null && !"".equals(otherEmails)) return false;

		if (emailTemplate!=null && !emailTemplate.empty()) return false;
		
		
		return true;
	}
	
	/**
	 * anula los objetos vacios dentro del objeto
	 */
	public void nullateEmtpyObjects() {
		
		if (this.emailTemplate!=null && this.emailTemplate.empty()) {
			this.emailTemplate = null;
		} else if (this.emailTemplate!=null && !this.emailTemplate.empty()){
			this.emailTemplate.nullateEmtpyObjects();
		}
		
	}

	/**
	 * recupera los objetos vacios para persistir
	 */
	public void recoverEmtpyObjects() {
			  
		if (this.emailTemplate == null) {
			this.emailTemplate = new EmailTemplate(EMPTY_OBJECT);
		} else {
			this.emailTemplate.recoverEmtpyObjects();
		}

	}
}
