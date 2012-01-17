package org.beeblos.bpm.core.model;

// Generated Oct 30, 2010 12:25:05 AM by Hibernate Tools 3.3.0.GA

import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * WStepDef generated by hbm2java
 * 
 * DEFINE EL PASO EN SI, LAS PÁGINAS RELACIONADAS, LAS RESPUESTAS POSIBLES
 */
public class WStepDef implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private Integer idDept;
	private Integer idPhase;
	private String instructions;
	private String stepComments;
	private String idListZone;
	private String idWorkZone;
	private String idAdditionalZone;
	
	private String submitForm; // instruccion para submit
	
	private WTimeUnit timeUnit;
	private Integer assignedTime;
	private Date deadlineDate;
	private Date deadlineTime;
	private WTimeUnit reminderTimeUnit;
	private Integer reminderTime; // en unidades de tiempo indicadas en reminderTimeUnit
	
	boolean runtimeModifiable; // si se pueden modificar los deadline y eso en runtime ...
	
	private boolean sentAdminNotice;
	private boolean arrivingAdminNotice;
	private boolean deadlineAdminNotice;
	private boolean reminderAdminNotice;
	private boolean expiredAdminNotice;
	private boolean sentUserNotice;
	private boolean arrivingUserNotice;
	private boolean deadlineUserNotice;
	private boolean reminderUserNotice;
	private boolean expiredUserNotice;
	
	private boolean emailNotification;
	private boolean engineNotification;
	
	private Set<WStepResponseDef> response = new HashSet<WStepResponseDef>();
//	private Set<WStepAssignedDef> assigned = new HashSet<WStepAssignedDef>();
	
	// at design time the designer may be associate some roles or users to the step
	// MANY2MANY
	Set<WStepRole> rolesRelated=new HashSet<WStepRole>();
	Set<WStepUser> usersRelated=new HashSet<WStepUser>();
	
	// dml 20120113
	private Date insertDate;
	private Integer insertUser;
	private Date modDate;
	private Integer modUser;

	public WStepDef() {
	}

	public WStepDef(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			this.timeUnit =new WTimeUnit( EMPTY_OBJECT );
			this.reminderTimeUnit =new WTimeUnit( EMPTY_OBJECT );
			
		}	
	}

	public WStepDef(Integer id, String name, Integer idDept, Integer idPhase,
			String instructions, String stepComments, String idListZone,
			String idWorkZone, String idAdditionalZone/*,
			Set<WStepResponseDef> response*/) {
		super();
		this.id = id;
		this.name = name;
		this.idDept = idDept;
		this.idPhase = idPhase;
		this.instructions = instructions;
		this.stepComments = stepComments;
		this.idListZone = idListZone;
		this.idWorkZone = idWorkZone;
		this.idAdditionalZone = idAdditionalZone;
//		this.response = response;
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

	public Integer getIdDept() {
		return this.idDept;
	}

	public void setIdDept(Integer idDept) {
		this.idDept = idDept;
	}

	public Integer getIdPhase() {
		return this.idPhase;
	}

	public void setIdPhase(Integer idPhase) {
		this.idPhase = idPhase;
	}

	public String getStepComments() {
		return this.stepComments;
	}

	public void setStepComments(String stepComments) {
		this.stepComments = stepComments;
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




	public String getSubmitForm() {
		return submitForm;
	}



	public void setSubmitForm(String submitForm) {
		this.submitForm = submitForm;
	}



	/**
	 * @return the response
	 */
	public Set<WStepResponseDef> getResponse() {
		return response;
	}



	/**
	 * @param response the response to set
	 */
	public void setResponse(Set<WStepResponseDef> response) {
		this.response = response;
	}



//	/**
//	 * @return the assigned
//	 */
//	public Set<WStepAssignedDef> getAssigned() {
//		return assigned;
//	}
//
//
//
//	/**
//	 * @param assigned the assigned to set
//	 */
//	public void setAssigned(Set<WStepAssignedDef> assigned) {
//		this.assigned = assigned;
//	}



	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return instructions;
	}

	/**
	 * @param instructions the instructions to set
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}



	/**
	 * @return the assignedTime
	 */
	public Integer getAssignedTime() {
		return assignedTime;
	}



	/**
	 * @param assignedTime the assignedTime to set
	 */
	public void setAssignedTime(Integer assignedTime) {
		this.assignedTime = assignedTime;
	}



	/**
	 * @return the deadlineDate
	 */
	public Date getDeadlineDate() {
		return deadlineDate;
	}



	/**
	 * @param deadlineDate the deadlineDate to set
	 */
	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}



	/**
	 * @return the deadlineTime
	 */
	public Date getDeadlineTime() {
		return deadlineTime;
	}



	/**
	 * @param deadlineTime the deadlineTime to set
	 */
	public void setDeadlineTime(Date deadlineTime) {
		this.deadlineTime = deadlineTime;
	}



	/**
	 * @return the reminderTimeUnit
	 */
	public WTimeUnit getReminderTimeUnit() {
		return reminderTimeUnit;
	}



	/**
	 * @param reminderTimeUnit the reminderTimeUnit to set
	 */
	public void setReminderTimeUnit(WTimeUnit reminderTimeUnit) {
		this.reminderTimeUnit = reminderTimeUnit;
	}



	/**
	 * @return the reminderTime
	 */
	public Integer getReminderTime() {
		return reminderTime;
	}



	/**
	 * @param reminderTime the reminderTime to set
	 */
	public void setReminderTime(Integer reminderTime) {
		this.reminderTime = reminderTime;
	}



	/**
	 * @return the runtimeModifiable
	 */
	public boolean isRuntimeModifiable() {
		return runtimeModifiable;
	}



	/**
	 * @param runtimeModifiable the runtimeModifiable to set
	 */
	public void setRuntimeModifiable(boolean runtimeModifiable) {
		this.runtimeModifiable = runtimeModifiable;
	}



	/**
	 * @return the timeUnit
	 */
	public WTimeUnit getTimeUnit() {
		return timeUnit;
	}



	/**
	 * @param timeUnit the timeUnit to set
	 */
	public void setTimeUnit(WTimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (arrivingAdminNotice ? 1231 : 1237);
		result = prime * result + (arrivingUserNotice ? 1231 : 1237);
		result = prime * result
				+ ((assignedTime == null) ? 0 : assignedTime.hashCode());
		result = prime * result + (deadlineAdminNotice ? 1231 : 1237);
		result = prime * result
				+ ((deadlineDate == null) ? 0 : deadlineDate.hashCode());
		result = prime * result
				+ ((deadlineTime == null) ? 0 : deadlineTime.hashCode());
		result = prime * result + (deadlineUserNotice ? 1231 : 1237);
		result = prime * result + (emailNotification ? 1231 : 1237);
		result = prime * result + (engineNotification ? 1231 : 1237);
		result = prime * result + (expiredAdminNotice ? 1231 : 1237);
		result = prime * result + (expiredUserNotice ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((idAdditionalZone == null) ? 0 : idAdditionalZone.hashCode());
		result = prime * result + ((idDept == null) ? 0 : idDept.hashCode());
		result = prime * result
				+ ((idListZone == null) ? 0 : idListZone.hashCode());
		result = prime * result + ((idPhase == null) ? 0 : idPhase.hashCode());
		result = prime * result
				+ ((idWorkZone == null) ? 0 : idWorkZone.hashCode());
		result = prime * result
				+ ((instructions == null) ? 0 : instructions.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (reminderAdminNotice ? 1231 : 1237);
		result = prime * result
				+ ((reminderTime == null) ? 0 : reminderTime.hashCode());
		result = prime
				* result
				+ ((reminderTimeUnit == null) ? 0 : reminderTimeUnit.hashCode());
		result = prime * result + (reminderUserNotice ? 1231 : 1237);
		result = prime * result
				+ ((response == null) ? 0 : response.hashCode());
		result = prime * result
				+ ((rolesRelated == null) ? 0 : rolesRelated.hashCode());
		result = prime * result + (runtimeModifiable ? 1231 : 1237);
		result = prime * result + (sentAdminNotice ? 1231 : 1237);
		result = prime * result + (sentUserNotice ? 1231 : 1237);
		result = prime * result
				+ ((stepComments == null) ? 0 : stepComments.hashCode());
		result = prime * result
				+ ((submitForm == null) ? 0 : submitForm.hashCode());
		result = prime * result
				+ ((timeUnit == null) ? 0 : timeUnit.hashCode());
		result = prime * result
				+ ((usersRelated == null) ? 0 : usersRelated.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WStepDef))
			return false;
		WStepDef other = (WStepDef) obj;
		if (arrivingAdminNotice != other.arrivingAdminNotice)
			return false;
		if (arrivingUserNotice != other.arrivingUserNotice)
			return false;
		if (assignedTime == null) {
			if (other.assignedTime != null)
				return false;
		} else if (!assignedTime.equals(other.assignedTime))
			return false;
		if (deadlineAdminNotice != other.deadlineAdminNotice)
			return false;
		if (deadlineDate == null) {
			if (other.deadlineDate != null)
				return false;
		} else if (!deadlineDate.equals(other.deadlineDate))
			return false;
		if (deadlineTime == null) {
			if (other.deadlineTime != null)
				return false;
		} else if (!deadlineTime.equals(other.deadlineTime))
			return false;
		if (deadlineUserNotice != other.deadlineUserNotice)
			return false;
		if (emailNotification != other.emailNotification)
			return false;
		if (engineNotification != other.engineNotification)
			return false;
		if (expiredAdminNotice != other.expiredAdminNotice)
			return false;
		if (expiredUserNotice != other.expiredUserNotice)
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
		if (idDept == null) {
			if (other.idDept != null)
				return false;
		} else if (!idDept.equals(other.idDept))
			return false;
		if (idListZone == null) {
			if (other.idListZone != null)
				return false;
		} else if (!idListZone.equals(other.idListZone))
			return false;
		if (idPhase == null) {
			if (other.idPhase != null)
				return false;
		} else if (!idPhase.equals(other.idPhase))
			return false;
		if (idWorkZone == null) {
			if (other.idWorkZone != null)
				return false;
		} else if (!idWorkZone.equals(other.idWorkZone))
			return false;
		if (instructions == null) {
			if (other.instructions != null)
				return false;
		} else if (!instructions.equals(other.instructions))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (reminderAdminNotice != other.reminderAdminNotice)
			return false;
		if (reminderTime == null) {
			if (other.reminderTime != null)
				return false;
		} else if (!reminderTime.equals(other.reminderTime))
			return false;
		if (reminderTimeUnit == null) {
			if (other.reminderTimeUnit != null)
				return false;
		} else if (!reminderTimeUnit.equals(other.reminderTimeUnit))
			return false;
		if (reminderUserNotice != other.reminderUserNotice)
			return false;
		if (response == null) {
			if (other.response != null)
				return false;
		} else if (!response.equals(other.response))
			return false;
		if (rolesRelated == null) {
			if (other.rolesRelated != null)
				return false;
		} else if (!rolesRelated.equals(other.rolesRelated))
			return false;
		if (runtimeModifiable != other.runtimeModifiable)
			return false;
		if (sentAdminNotice != other.sentAdminNotice)
			return false;
		if (sentUserNotice != other.sentUserNotice)
			return false;
		if (stepComments == null) {
			if (other.stepComments != null)
				return false;
		} else if (!stepComments.equals(other.stepComments))
			return false;
		if (submitForm == null) {
			if (other.submitForm != null)
				return false;
		} else if (!submitForm.equals(other.submitForm))
			return false;
		if (timeUnit == null) {
			if (other.timeUnit != null)
				return false;
		} else if (!timeUnit.equals(other.timeUnit))
			return false;
		if (usersRelated == null) {
			if (other.usersRelated != null)
				return false;
		} else if (!usersRelated.equals(other.usersRelated))
			return false;
		return true;
	}




	@Override
	public String toString() {
		return "WStepDef ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (idDept != null ? "idDept=" + idDept + ", " : "")
				+ (idPhase != null ? "idPhase=" + idPhase + ", " : "")
				+ (instructions != null ? "instructions=" + instructions + ", "
						: "")
				+ (stepComments != null ? "stepComments=" + stepComments + ", "
						: "")
				+ (idListZone != null ? "idListZone=" + idListZone + ", " : "")
				+ (idWorkZone != null ? "idWorkZone=" + idWorkZone + ", " : "")
				+ (idAdditionalZone != null ? "idAdditionalZone="
						+ idAdditionalZone + ", " : "")
				+ (submitForm != null ? "submitForm=" + submitForm + ", " : "")
				+ (timeUnit != null ? "timeUnit=" + timeUnit + ", " : "")
				+ (assignedTime != null ? "assignedTime=" + assignedTime + ", "
						: "")
				+ (deadlineDate != null ? "deadlineDate=" + deadlineDate + ", "
						: "")
				+ (deadlineTime != null ? "deadlineTime=" + deadlineTime + ", "
						: "")
				+ (reminderTimeUnit != null ? "reminderTimeUnit="
						+ reminderTimeUnit + ", " : "")
				+ (reminderTime != null ? "reminderTime=" + reminderTime + ", "
						: "")
				+ "runtimeModifiable="
				+ runtimeModifiable
				+ ", sentAdminNotice="
				+ sentAdminNotice
				+ ", arrivingAdminNotice="
				+ arrivingAdminNotice
				+ ", deadlineAdminNotice="
				+ deadlineAdminNotice
				+ ", reminderAdminNotice="
				+ reminderAdminNotice
				+ ", expiredAdminNotice="
				+ expiredAdminNotice
				+ ", sentUserNotice="
				+ sentUserNotice
				+ ", arrivingUserNotice="
				+ arrivingUserNotice
				+ ", deadlineUserNotice="
				+ deadlineUserNotice
				+ ", reminderUserNotice="
				+ reminderUserNotice
				+ ", expiredUserNotice="
				+ expiredUserNotice
				+ ", emailNotification="
				+ emailNotification
				+ ", engineNotification="
				+ engineNotification
				+ ", "
				+ (response != null ? "response=" + response + ", " : "")
				+ (rolesRelated != null ? "rolesRelated=" + rolesRelated + ", "
						: "")
				+ (usersRelated != null ? "usersRelated=" + usersRelated + ", "
						: "")
				+ (insertDate != null ? "insertDate=" + insertDate + ", " : "")
				+ (insertUser != null ? "insertUser=" + insertUser + ", " : "")
				+ (modDate != null ? "modDate=" + modDate + ", " : "")
				+ (modUser != null ? "modUser=" + modUser : "") + "]";
	}




	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}


	public boolean empty() {

		if (id!=null && ! id.equals(0)) return false;
		if (idDept!=null && ! idDept.equals(0)) return false;
		if (idPhase!=null && ! idPhase.equals(0)) return false;
		if (assignedTime!=null && ! assignedTime.equals(0)) return false;
		if (reminderTime!=null && ! reminderTime.equals(0)) return false;
	
		if (name!=null && ! "".equals(name)) return false;
		if (instructions!=null && ! "".equals(instructions)) return false;
		if (stepComments!=null && ! "".equals(stepComments)) return false;
		
		if (idListZone!=null && ! "".equals(idListZone)) return false;
		if (idWorkZone!=null && ! "".equals(idWorkZone)) return false;
		if (idAdditionalZone!=null && ! "".equals(idAdditionalZone)) return false;
		
		if (idAdditionalZone!=null && ! "".equals(idAdditionalZone)) return false;

		if (timeUnit!=null && ! timeUnit.empty()) return false;
		if (reminderTimeUnit!=null && ! reminderTimeUnit.empty()) return false;

		if (deadlineDate!=null ) return false;
		if (deadlineTime!=null ) return false;
		
		return true;
	}


	
	public Set<WStepRole> getRolesRelated() {
		return rolesRelated;
	}

	public void setRolesRelated(Set<WStepRole> rolesRelated) {
		this.rolesRelated = rolesRelated;
	}

	public Set<WStepUser> getUsersRelated() {
		return usersRelated;
	}

	public void setUsersRelated(Set<WStepUser> usersRelated) {
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
	
	

	public boolean isSentAdminNotice() {
		return sentAdminNotice;
	}

	public void setSentAdminNotice(boolean sentAdminNotice) {
		this.sentAdminNotice = sentAdminNotice;
	}

	public boolean isArrivingAdminNotice() {
		return arrivingAdminNotice;
	}

	public void setArrivingAdminNotice(boolean arrivingAdminNotice) {
		this.arrivingAdminNotice = arrivingAdminNotice;
	}

	public boolean isDeadlineAdminNotice() {
		return deadlineAdminNotice;
	}

	public void setDeadlineAdminNotice(boolean deadlineAdminNotice) {
		this.deadlineAdminNotice = deadlineAdminNotice;
	}

	public boolean isReminderAdminNotice() {
		return reminderAdminNotice;
	}

	public void setReminderAdminNotice(boolean reminderAdminNotice) {
		this.reminderAdminNotice = reminderAdminNotice;
	}

	public boolean isExpiredAdminNotice() {
		return expiredAdminNotice;
	}

	public void setExpiredAdminNotice(boolean expiredAdminNotice) {
		this.expiredAdminNotice = expiredAdminNotice;
	}

	public boolean isSentUserNotice() {
		return sentUserNotice;
	}

	public void setSentUserNotice(boolean sentUserNotice) {
		this.sentUserNotice = sentUserNotice;
	}

	public boolean isArrivingUserNotice() {
		return arrivingUserNotice;
	}

	public void setArrivingUserNotice(boolean arrivingUserNotice) {
		this.arrivingUserNotice = arrivingUserNotice;
	}

	public boolean isDeadlineUserNotice() {
		return deadlineUserNotice;
	}

	public void setDeadlineUserNotice(boolean deadlineUserNotice) {
		this.deadlineUserNotice = deadlineUserNotice;
	}

	public boolean isReminderUserNotice() {
		return reminderUserNotice;
	}

	public void setReminderUserNotice(boolean reminderUserNotice) {
		this.reminderUserNotice = reminderUserNotice;
	}

	public boolean isExpiredUserNotice() {
		return expiredUserNotice;
	}

	public void setExpiredUserNotice(boolean expiredUserNotice) {
		this.expiredUserNotice = expiredUserNotice;
	}

	public boolean isEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	public boolean isEngineNotification() {
		return engineNotification;
	}

	public void setEngineNotification(boolean engineNotification) {
		this.engineNotification = engineNotification;
	}

	// nes 20111209
	public void addRole( WRoleDef role, boolean admin, Integer idObject, String idObjectType, Integer insertUser ) {
		WStepRole wsr = new WStepRole(admin, idObject, idObjectType, insertUser, new Date() );
		wsr.setStep(this);
		wsr.setRole(role);
		rolesRelated.add(wsr);
	}
	
	// nes 20111209
	public void addUser( WUserDef user, boolean admin, Integer idObject, String idObjectType, Integer insertUser ) {
		WStepUser wsu = new WStepUser(admin, idObject, idObjectType, insertUser, new Date() );
		wsu.setStep(this);
		wsu.setUser(user);
		usersRelated.add(wsu);
	}

	// dml 20120110
	public void addResponse( WStepResponseDef wStepResponseDef ) {
		response.add(wStepResponseDef);
	}

}
