package org.beeblos.bpm.core.model;

// Generated Oct 30, 2010 12:25:05 AM by Hibernate Tools 3.3.0.GA

import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * WProcessDef generated by hbm2java
 */
public class WProcessDef implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String comments;
	private WStepDef beginStep;
	
	private String idListZone; // pag que define la lista de seleccion de steps
	private String idWorkZone; // pag que define la zona de trabajo
	private String idAdditionalZone; // pag que define la zona de ayuda ( info adicional )
	
	private String idProcessorStep; // dml 20120619
	
	/*
	 * DESIGN AND TEST TIME FEATURE
	 * At design time the designer can define a list of roles and users that may interact
	 * with the workflow. This info is only to effects to simplify and control the screen
	 * presentation for users and roles that can be assigned to a step or a task ...
	 * 
	 */
	
	// MANY2MANY
	Set<WProcessRole> rolesRelated=new HashSet<WProcessRole>();
	Set<WProcessUser> usersRelated=new HashSet<WProcessUser>();
//	Set<WRoleDef> adminRoles=new HashSet<WRoleDef>();
//	Set<WUserDef> adminUsers=new HashSet<WUserDef>();
	
	
	private Date insertDate;
	private Integer insertUser;
	private Date modDate;
	private Integer modUser;
	
	// dml 20120105
	private String adminEmail;
	
	// dml 20120118
	private boolean active;
	private Date productionDate;
	private Integer productionUser;
	private Integer totalTime;
	private WTimeUnit totalTimeUnit;
	private String globalDeadlineDate;
	
	// dml 20120223
	private WEmailAccount systemEmailAccount;
	
	// dml 20120306
	private WEmailTemplates arrivingAdminNoticeTemplate;
	private WEmailTemplates arrivingUserNoticeTemplate;
	

	public WProcessDef() {
		super();
	}

	
	public WProcessDef(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			this.beginStep=new WStepDef( EMPTY_OBJECT );
			this.systemEmailAccount = new WEmailAccount(EMPTY_OBJECT);
			this.arrivingAdminNoticeTemplate = new WEmailTemplates(EMPTY_OBJECT);
			this.arrivingUserNoticeTemplate = new WEmailTemplates(EMPTY_OBJECT);
			
		}	
	}
	
	public WProcessDef(String name, String comments, WStepDef beginStep,
			Date fechaAlta, Date fechaModificacion) {
		this.name = name;
		this.comments = comments;
		this.beginStep = beginStep;
		this.insertDate = fechaAlta;
		this.modDate = fechaModificacion;
	}

	public WProcessDef(String name, String comments, WStepDef beginStep,
			String idListZone, String idWorkZone, String idAdditionalZone,
			Date insertDate, Integer insertUser, Date modDate,
			Integer modUser, String adminEmail ) {
		this.name = name;
		this.comments = comments;
		this.beginStep = beginStep;
		this.idListZone = idListZone;
		this.idWorkZone = idWorkZone;
		this.idAdditionalZone = idAdditionalZone;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.modDate = modDate;
		this.modUser = modUser;
		this.adminEmail = adminEmail;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}


	/**
	 * @return the beginStep
	 */
	public WStepDef getBeginStep() {
		return beginStep;
	}

	/**
	 * @param beginStep the beginStep to set
	 */
	public void setBeginStep(WStepDef beginStep) {
		this.beginStep = beginStep;
	}

	public String getIdListZone() {
		return this.idListZone;
	}

	public void setIdListZone(String idListZone) {
		this.idListZone = idListZone;
	}

	public String getIdWorkZone() {
		return this.idWorkZone;
	}

	public void setIdWorkZone(String idWorkZone) {
		this.idWorkZone = idWorkZone;
	}

	public String getIdAdditionalZone() {
		return this.idAdditionalZone;
	}

	public void setIdAdditionalZone(String idAdditionalZone) {
		this.idAdditionalZone = idAdditionalZone;
	}


	
	public String getIdProcessorStep() {
		return idProcessorStep;
	}


	public void setIdProcessorStep(String idProcessorStep) {
		this.idProcessorStep = idProcessorStep;
	}


	public Set<WProcessRole> getRolesRelated() {
		return rolesRelated;
	}

	public void setRolesRelated(Set<WProcessRole> rolesRelated) {
		this.rolesRelated = rolesRelated;
	}

	public Set<WProcessUser> getUsersRelated() {
		return usersRelated;
	}

	public void setUsersRelated(Set<WProcessUser> usersRelated) {
		this.usersRelated = usersRelated;
	}



	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public Integer getInsertUser() {
		return insertUser;
	}

	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}

	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	public Integer getModUser() {
		return modUser;
	}

	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}

	public String getAdminEmail() {
		return adminEmail;
	}


	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public Date getProductionDate() {
		return productionDate;
	}


	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}


	public Integer getProductionUser() {
		return productionUser;
	}


	public void setProductionUser(Integer productionUser) {
		this.productionUser = productionUser;
	}


	public Integer getTotalTime() {
		return totalTime;
	}


	public void setTotalTime(Integer totalTime) {
		this.totalTime = totalTime;
	}


	public WTimeUnit getTotalTimeUnit() {
		return totalTimeUnit;
	}


	public void setTotalTimeUnit(WTimeUnit totalTimeUnit) {
		this.totalTimeUnit = totalTimeUnit;
	}


	public String getGlobalDeadlineDate() {
		return globalDeadlineDate;
	}


	public void setGlobalDeadlineDate(String globalDeadlineDate) {
		this.globalDeadlineDate = globalDeadlineDate;
	}


	public WEmailAccount getSystemEmailAccount() {
		return systemEmailAccount;
	}


	public void setSystemEmailAccount(WEmailAccount systemEmailAccount) {
		this.systemEmailAccount = systemEmailAccount;
	}


	public WEmailTemplates getArrivingAdminNoticeTemplate() {
		return arrivingAdminNoticeTemplate;
	}


	public void setArrivingAdminNoticeTemplate(
			WEmailTemplates arrivingAdminNoticeTemplate) {
		this.arrivingAdminNoticeTemplate = arrivingAdminNoticeTemplate;
	}


	public WEmailTemplates getArrivingUserNoticeTemplate() {
		return arrivingUserNoticeTemplate;
	}


	public void setArrivingUserNoticeTemplate(
			WEmailTemplates arrivingUserNoticeTemplate) {
		this.arrivingUserNoticeTemplate = arrivingUserNoticeTemplate;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result
				+ ((adminEmail == null) ? 0 : adminEmail.hashCode());
		result = prime
				* result
				+ ((arrivingAdminNoticeTemplate == null) ? 0
						: arrivingAdminNoticeTemplate.hashCode());
		result = prime
				* result
				+ ((arrivingUserNoticeTemplate == null) ? 0
						: arrivingUserNoticeTemplate.hashCode());
		result = prime * result
				+ ((beginStep == null) ? 0 : beginStep.hashCode());
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime
				* result
				+ ((globalDeadlineDate == null) ? 0 : globalDeadlineDate
						.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((idAdditionalZone == null) ? 0 : idAdditionalZone.hashCode());
		result = prime
				* result
				+ ((idProcessorStep == null) ? 0 : idProcessorStep.hashCode());
		result = prime * result
				+ ((idListZone == null) ? 0 : idListZone.hashCode());
		result = prime * result
				+ ((idWorkZone == null) ? 0 : idWorkZone.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result
				+ ((productionUser == null) ? 0 : productionUser.hashCode());
		result = prime * result
				+ ((rolesRelated == null) ? 0 : rolesRelated.hashCode());
		result = prime * result
				+ ((totalTime == null) ? 0 : totalTime.hashCode());
		result = prime * result
				+ ((totalTimeUnit == null) ? 0 : totalTimeUnit.hashCode());
		result = prime * result
				+ ((usersRelated == null) ? 0 : usersRelated.hashCode());
		result = prime
				* result
				+ ((systemEmailAccount == null) ? 0 : systemEmailAccount
						.hashCode());
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
		WProcessDef other = (WProcessDef) obj;
		if (active != other.active)
			return false;
		if (adminEmail == null) {
			if (other.adminEmail != null)
				return false;
		} else if (!adminEmail.equals(other.adminEmail))
			return false;
		if (arrivingAdminNoticeTemplate == null) {
			if (other.arrivingAdminNoticeTemplate != null)
				return false;
		} else if (!arrivingAdminNoticeTemplate
				.equals(other.arrivingAdminNoticeTemplate))
			return false;
		if (arrivingUserNoticeTemplate == null) {
			if (other.arrivingUserNoticeTemplate != null)
				return false;
		} else if (!arrivingUserNoticeTemplate
				.equals(other.arrivingUserNoticeTemplate))
			return false;
		if (beginStep == null) {
			if (other.beginStep != null)
				return false;
		} else if (!beginStep.equals(other.beginStep))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (globalDeadlineDate == null) {
			if (other.globalDeadlineDate != null)
				return false;
		} else if (!globalDeadlineDate.equals(other.globalDeadlineDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idAdditionalZone == null) {
			if (other.idAdditionalZone != null)
				return false;
		} else if (!idAdditionalZone.equals(other.idAdditionalZone))
			return false;
		if (idProcessorStep == null) {
			if (other.idProcessorStep != null)
				return false;
		} else if (!idProcessorStep.equals(other.idProcessorStep))
			return false;
		if (idListZone == null) {
			if (other.idListZone != null)
				return false;
		} else if (!idListZone.equals(other.idListZone))
			return false;
		if (idWorkZone == null) {
			if (other.idWorkZone != null)
				return false;
		} else if (!idWorkZone.equals(other.idWorkZone))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (productionUser == null) {
			if (other.productionUser != null)
				return false;
		} else if (!productionUser.equals(other.productionUser))
			return false;
		if (rolesRelated == null) {
			if (other.rolesRelated != null)
				return false;
		} else if (!rolesRelated.equals(other.rolesRelated))
			return false;
		if (totalTime == null) {
			if (other.totalTime != null)
				return false;
		} else if (!totalTime.equals(other.totalTime))
			return false;
		if (totalTimeUnit == null) {
			if (other.totalTimeUnit != null)
				return false;
		} else if (!totalTimeUnit.equals(other.totalTimeUnit))
			return false;
		if (usersRelated == null) {
			if (other.usersRelated != null)
				return false;
		} else if (!usersRelated.equals(other.usersRelated))
			return false;
		if (systemEmailAccount == null) {
			if (other.systemEmailAccount != null)
				return false;
		} else if (!systemEmailAccount.equals(other.systemEmailAccount))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WProcessDef [id=" + id + ", name=" + name + ", comments=" + comments
				+ ", beginStep=" + beginStep + ", idListZone=" + idListZone + ", idWorkZone="
				+ idWorkZone + ", idAdditionalZone=" + idAdditionalZone + ", idProcessorStep="
				+ idProcessorStep + ", rolesRelated=" + rolesRelated + ", usersRelated="
				+ usersRelated + ", insertDate=" + insertDate + ", insertUser=" + insertUser
				+ ", modDate=" + modDate + ", modUser=" + modUser + ", adminEmail=" + adminEmail
				+ ", active=" + active + ", productionDate=" + productionDate + ", productionUser="
				+ productionUser + ", totalTime=" + totalTime + ", totalTimeUnit=" + totalTimeUnit
				+ ", globalDeadlineDate=" + globalDeadlineDate + ", systemEmailAccount="
				+ systemEmailAccount + ", arrivingAdminNoticeTemplate="
				+ arrivingAdminNoticeTemplate + ", arrivingUserNoticeTemplate="
				+ arrivingUserNoticeTemplate + "]";
	}
	

	public boolean empty() {

		if (id!=null && ! id.equals(0)) return false;
		if (name!=null && ! "".equals(name)) return false;
		if (comments!=null && ! "".equals(comments)) return false;
		
		if (beginStep!=null && ! beginStep.empty()) return false;
		
		if (idListZone!=null && ! "".equals(idListZone)) return false;
		if (idWorkZone!=null && ! "".equals(idWorkZone)) return false;
		if (idAdditionalZone!=null && ! "".equals(idAdditionalZone)) return false;
		if (adminEmail!=null && ! "".equals(adminEmail)) return false;
		
		// dml 20120306
		if (arrivingAdminNoticeTemplate!=null && ! arrivingAdminNoticeTemplate.empty()) return false;
		if (arrivingUserNoticeTemplate!=null && ! arrivingUserNoticeTemplate.empty()) return false;

		// dml 20120619
		if (idProcessorStep!=null && ! "".equals(idProcessorStep)) return false;

		return true;
	}
	
	public void addRole( WRoleDef role, boolean admin, Integer idObject, String idObjectType, Integer insertUser ) {
		WProcessRole wpr = new WProcessRole(admin, idObject, idObjectType, insertUser, new Date() );
		wpr.setProcess(this);
		wpr.setRole(role);
		rolesRelated.add(wpr);
	}
	
	public void addUser( WUserDef user, boolean admin, Integer idObject, String idObjectType, Integer insertUser ) {
		WProcessUser wpu = new WProcessUser(admin, idObject, idObjectType, insertUser, new Date() );
		wpu.setProcess(this);
		wpu.setUser(user);
		usersRelated.add(wpu);
	}

}
