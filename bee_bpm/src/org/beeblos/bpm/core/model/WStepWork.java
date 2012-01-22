package org.beeblos.bpm.core.model;

// Generated Oct 30, 2010 12:25:05 AM by Hibernate Tools 3.3.0.GA

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * WStepWork generated by hbm2java
 */
public class WStepWork implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private WProcessWork wProcessWork;
	
	private WProcessDef process;
	private Integer version;
	
	private WStepDef previousStep;
	private WStepDef currentStep;
	
	private Integer idObject;
	private String idObjectType;
	
	// fields to link with user known properties about objects
//	private String reference;
//	private String comments; 
	
	private String userInstructions;
	private String userNotes;
		
	private boolean myNotes;
	private boolean sendUserNotesToNextStep;
	
	private Date arrivingDate;
	private Date openedDate;
	private Integer openerUser;
	
	private Date decidedDate;
	private Integer performer;
	
	private String response;
	private String nextStepInstructions;
	
	private WTimeUnit timeUnit;
	private Integer assignedTime;
	private Date deadlineDate;
	private Date deadlineTime;
	private WTimeUnit reminderTimeUnit;
	private Integer reminderTime; // en unidades de tiempo indicadas en reminderTimeUnit
	
	private boolean adminProcess;
	
	private boolean locked;
	private WUserDef lockedBy; // id del usuario que lo bloqueó
	private Date lockedSince;
	
	private boolean sentBack;
	
	private Set<WStepWorkAssignment> assignedTo = new HashSet<WStepWorkAssignment>();

	private WUserDef insertUser; // user has processed previous step ( and inserted current step )
	private Date modDate;
	private Integer modUser;

	public WStepWork() {
		super();
	}
	
	public WStepWork(WStepWork step) {

		this.id = step.id;
		this.process = step.process;
		this.version = step.version;
		this.previousStep = step.previousStep;
		this.currentStep = step.currentStep;
		this.idObject = step.idObject;
		this.idObjectType = step.idObjectType;

		this.arrivingDate = step.arrivingDate;
		this.openedDate = step.openedDate;
		this.openerUser = step.openerUser;
		this.decidedDate = step.decidedDate;
		this.performer = step.performer;
		this.response = step.response;
		this.nextStepInstructions = step.nextStepInstructions;
		this.timeUnit = step.timeUnit;
		this.assignedTime = step.assignedTime;
		this.deadlineDate = step.deadlineDate;
		this.deadlineTime = step.deadlineTime;
		this.reminderTimeUnit = step.reminderTimeUnit;
		this.reminderTime = step.reminderTime;
		this.adminProcess = step.adminProcess;
		this.locked = step.locked;
		this.lockedBy = step.lockedBy;
		this.lockedSince = step.lockedSince;
		this.assignedTo = step.assignedTo;
		this.insertUser = step.insertUser;
		this.modDate = step.modDate;
		this.modUser = step.modUser;
	}


	public WStepWork(Integer id, WProcessDef process, Integer version,
			WStepDef previousStep, WStepDef currentStep, Integer idObject,
			String idObjectType, String reference, String comments,
			Date arrivingDate, Date openedDate, Integer openerUser,
			Date decidedDate, Integer performer, String response,
			String nextStepInstructions, WTimeUnit timeUnit,
			Integer assignedTime, Date deadlineDate, Date deadlineTime,
			WTimeUnit reminderTimeUnit, Integer reminderTime,
			boolean adminProcess, boolean locked, WUserDef lockedBy,
			Date lockedSince, Set<WStepWorkAssignment> assignedTo,
			WUserDef insertUser, Date modDate, Integer modUser) {
		this.id = id;
		this.process = process;
		this.version = version;
		this.previousStep = previousStep;
		this.currentStep = currentStep;
		this.idObject = idObject;
		this.idObjectType = idObjectType;
		this.arrivingDate = arrivingDate;
		this.openedDate = openedDate;
		this.openerUser = openerUser;
		this.decidedDate = decidedDate;
		this.performer = performer;
		this.response = response;
		this.nextStepInstructions = nextStepInstructions;
		this.timeUnit = timeUnit;
		this.assignedTime = assignedTime;
		this.deadlineDate = deadlineDate;
		this.deadlineTime = deadlineTime;
		this.reminderTimeUnit = reminderTimeUnit;
		this.reminderTime = reminderTime;
		this.adminProcess = adminProcess;
		this.locked = locked;
		this.lockedBy = lockedBy;
		this.lockedSince = lockedSince;
		this.assignedTo = assignedTo;
		this.insertUser = insertUser;
		this.modDate = modDate;
		this.modUser = modUser;
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


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public WProcessWork getwProcessWork() {
		return wProcessWork;
	}

	public void setwProcessWork(WProcessWork wProcessWork) {
		this.wProcessWork = wProcessWork;
	}

	/**
	 * @return the process
	 */
	public WProcessDef getProcess() {
		return process;
	}

	/**
	 * @param process the process to set
	 */
	public void setProcess(WProcessDef process) {
		this.process = process;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}


	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}


	/**
	 * @return the previousStep
	 */
	public WStepDef getPreviousStep() {
		return previousStep;
	}


	/**
	 * @param previousStep the previousStep to set
	 */
	public void setPreviousStep(WStepDef previousStep) {
		this.previousStep = previousStep;
	}


	/**
	 * @return the currentStep
	 */
	public WStepDef getCurrentStep() {
		return currentStep;
	}


	/**
	 * @param currentStep the currentStep to set
	 */
	public void setCurrentStep(WStepDef currentStep) {
		this.currentStep = currentStep;
	}


	public Integer getIdObject() {
		return this.idObject;
	}

	public void setIdObject(Integer idObject) {
		this.idObject = idObject;
	}

	public String getIdObjectType() {
		return this.idObjectType;
	}

	public void setIdObjectType(String idObjectType) {
		this.idObjectType = idObjectType;
	}

	public Date getArrivingDate() {
		return this.arrivingDate;
	}

	public void setArrivingDate(Date arrivingDate) {
		this.arrivingDate = arrivingDate;
	}

	public Date getOpenedDate() {
		return this.openedDate;
	}

	public void setOpenedDate(Date openedDate) {
		this.openedDate = openedDate;
	}

	public Integer getOpenerUser() {
		return this.openerUser;
	}

	public void setOpenerUser(Integer openerUser) {
		this.openerUser = openerUser;
	}

	public Date getDecidedDate() {
		return this.decidedDate;
	}

	public void setDecidedDate(Date decidedDate) {
		this.decidedDate = decidedDate;
	}

	public Integer getPerformer() {
		return this.performer;
	}

	public void setPerformer(Integer performer) {
		this.performer = performer;
	}

	public Integer getAssignedTime() {
		return this.assignedTime;
	}

	public void setAssignedTime(Integer assignedTime) {
		this.assignedTime = assignedTime;
	}

	public Date getDeadlineDate() {
		return this.deadlineDate;
	}

	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}

	public Date getDeadlineTime() {
		return this.deadlineTime;
	}

	public void setDeadlineTime(Date deadlineTime) {
		this.deadlineTime = deadlineTime;
	}

	public boolean isAdminProcess() {
		return this.adminProcess;
	}

	public void setAdminProcess(boolean adminProcess) {
		this.adminProcess = adminProcess;
	}



	/**
	 * @return the assignedTo
	 */
	public Set<WStepWorkAssignment> getAssignedTo() {
		return assignedTo;
	}



	/**
	 * @param assignedTo the assignedTo to set
	 */
	public void setAssignedTo(Set<WStepWorkAssignment> assignedTo) {
		this.assignedTo = assignedTo;
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
	 * @return the insertUser
	 */
	public WUserDef getInsertUser() {
		return insertUser;
	}


	/**
	 * @param insertUser the insertUser to set
	 */
	public void setInsertUser(WUserDef insertUser) {
		this.insertUser = insertUser;
	}


	/**
	 * @return the modDate
	 */
	public Date getModDate() {
		return modDate;
	}


	/**
	 * @param modDate the modDate to set
	 */
	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}


	/**
	 * @return the modUser
	 */
	public Integer getModUser() {
		return modUser;
	}


	/**
	 * @param modUser the modUser to set
	 */
	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}


	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}


	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}


	/**
	 * @return the lockedBy
	 */
	public WUserDef getLockedBy() {
		return lockedBy;
	}


	/**
	 * @param lockedBy the lockedBy to set
	 */
	public void setLockedBy(WUserDef lockedBy) {
		this.lockedBy = lockedBy;
	}


	/**
	 * @return the lockedSince
	 */
	public Date getLockedSince() {
		return lockedSince;
	}


	/**
	 * @param lockedSince the lockedSince to set
	 */
	public void setLockedSince(Date lockedSince) {
		this.lockedSince = lockedSince;
	}


	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getNextStepInstructions() {
		return nextStepInstructions;
	}

	public void setNextStepInstructions(String nextStepInstructions) {
		this.nextStepInstructions = nextStepInstructions;
	}

	public String getUserInstructions() {
		return userInstructions;
	}

	public void setUserInstructions(String userInstructions) {
		this.userInstructions = userInstructions;
	}

	public String getUserNotes() {
		return userNotes;
	}

	public void setUserNotes(String userNotes) {
		this.userNotes = userNotes;
	}

	public boolean isMyNotes() {
		return myNotes;
	}

	public void setMyNotes(boolean myNotes) {
		this.myNotes = myNotes;
	}

	public boolean isSendUserNotesToNextStep() {
		return sendUserNotesToNextStep;
	}

	public void setSendUserNotesToNextStep(boolean sendUserNotesToNextStep) {
		this.sendUserNotesToNextStep = sendUserNotesToNextStep;
	}

	public boolean isSentBack() {
		return sentBack;
	}

	public void setSentBack(boolean sentBack) {
		this.sentBack = sentBack;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (adminProcess ? 1231 : 1237);
		result = prime * result
				+ ((arrivingDate == null) ? 0 : arrivingDate.hashCode());
		result = prime * result
				+ ((assignedTime == null) ? 0 : assignedTime.hashCode());
		result = prime * result
				+ ((assignedTo == null) ? 0 : assignedTo.hashCode());

		result = prime * result
				+ ((currentStep == null) ? 0 : currentStep.hashCode());
		result = prime * result
				+ ((deadlineDate == null) ? 0 : deadlineDate.hashCode());
		result = prime * result
				+ ((deadlineTime == null) ? 0 : deadlineTime.hashCode());
		result = prime * result
				+ ((decidedDate == null) ? 0 : decidedDate.hashCode());
		result = prime * result
				+ ((idObject == null) ? 0 : idObject.hashCode());
		result = prime * result
				+ ((idObjectType == null) ? 0 : idObjectType.hashCode());
		result = prime * result
				+ ((insertUser == null) ? 0 : insertUser.hashCode());
		result = prime * result + (locked ? 1231 : 1237);
		result = prime * result
				+ ((lockedBy == null) ? 0 : lockedBy.hashCode());
		result = prime * result
				+ ((lockedSince == null) ? 0 : lockedSince.hashCode());
		result = prime * result + (myNotes ? 1231 : 1237);
		result = prime
				* result
				+ ((nextStepInstructions == null) ? 0 : nextStepInstructions
						.hashCode());
		result = prime * result
				+ ((openedDate == null) ? 0 : openedDate.hashCode());
		result = prime * result
				+ ((openerUser == null) ? 0 : openerUser.hashCode());
		result = prime * result
				+ ((performer == null) ? 0 : performer.hashCode());
		result = prime * result
				+ ((previousStep == null) ? 0 : previousStep.hashCode());
		result = prime * result + ((process == null) ? 0 : process.hashCode());
		result = prime * result
				+ ((reminderTime == null) ? 0 : reminderTime.hashCode());
		result = prime
				* result
				+ ((reminderTimeUnit == null) ? 0 : reminderTimeUnit.hashCode());
		result = prime * result
				+ ((response == null) ? 0 : response.hashCode());
		result = prime * result + (sendUserNotesToNextStep ? 1231 : 1237);
		result = prime * result
				+ ((timeUnit == null) ? 0 : timeUnit.hashCode());
		result = prime
				* result
				+ ((userInstructions == null) ? 0 : userInstructions.hashCode());
		result = prime * result
				+ ((userNotes == null) ? 0 : userNotes.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WStepWork))
			return false;
		WStepWork other = (WStepWork) obj;
		if (adminProcess != other.adminProcess)
			return false;
		if (arrivingDate == null) {
			if (other.arrivingDate != null)
				return false;
		} else if (!arrivingDate.equals(other.arrivingDate))
			return false;
		if (assignedTime == null) {
			if (other.assignedTime != null)
				return false;
		} else if (!assignedTime.equals(other.assignedTime))
			return false;
		if (assignedTo == null) {
			if (other.assignedTo != null)
				return false;
		} else if (!assignedTo.equals(other.assignedTo))
			return false;
		if (currentStep == null) {
			if (other.currentStep != null)
				return false;
		} else if (!currentStep.equals(other.currentStep))
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
		if (decidedDate == null) {
			if (other.decidedDate != null)
				return false;
		} else if (!decidedDate.equals(other.decidedDate))
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
		if (insertUser == null) {
			if (other.insertUser != null)
				return false;
		} else if (!insertUser.equals(other.insertUser))
			return false;
		if (locked != other.locked)
			return false;
		if (lockedBy == null) {
			if (other.lockedBy != null)
				return false;
		} else if (!lockedBy.equals(other.lockedBy))
			return false;
		if (lockedSince == null) {
			if (other.lockedSince != null)
				return false;
		} else if (!lockedSince.equals(other.lockedSince))
			return false;
		if (myNotes != other.myNotes)
			return false;
		if (nextStepInstructions == null) {
			if (other.nextStepInstructions != null)
				return false;
		} else if (!nextStepInstructions.equals(other.nextStepInstructions))
			return false;
		if (openedDate == null) {
			if (other.openedDate != null)
				return false;
		} else if (!openedDate.equals(other.openedDate))
			return false;
		if (openerUser == null) {
			if (other.openerUser != null)
				return false;
		} else if (!openerUser.equals(other.openerUser))
			return false;
		if (performer == null) {
			if (other.performer != null)
				return false;
		} else if (!performer.equals(other.performer))
			return false;
		if (previousStep == null) {
			if (other.previousStep != null)
				return false;
		} else if (!previousStep.equals(other.previousStep))
			return false;
		if (process == null) {
			if (other.process != null)
				return false;
		} else if (!process.equals(other.process))
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
		if (response == null) {
			if (other.response != null)
				return false;
		} else if (!response.equals(other.response))
			return false;
		if (sendUserNotesToNextStep != other.sendUserNotesToNextStep)
			return false;
		if (timeUnit == null) {
			if (other.timeUnit != null)
				return false;
		} else if (!timeUnit.equals(other.timeUnit))
			return false;
		if (userInstructions == null) {
			if (other.userInstructions != null)
				return false;
		} else if (!userInstructions.equals(other.userInstructions))
			return false;
		if (userNotes == null) {
			if (other.userNotes != null)
				return false;
		} else if (!userNotes.equals(other.userNotes))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	@Override
	public String toString() {
		
		System.out.println("----------------_>>>>>>>>>>>> wstepwork.tostring -------------------------------------");
		
		return "WStepWork ["
				+ (id != null ? "id=" + id + ", " : "")
//				+ (process != null ? "process=" + process + ", " : "")
				+ (version != null ? "version=" + version + ", " : "")
				+ (previousStep != null ? "previousStep=" + previousStep + ", "
						: "")
				+ (currentStep != null ? "currentStep=" + currentStep + ", "
						: "")
				+ (idObject != null ? "idObject=" + idObject + ", " : "")
				+ (idObjectType != null ? "idObjectType=" + idObjectType + ", "
						: "")
				+ (userInstructions != null ? "userInstructions="
						+ userInstructions + ", " : "")
				+ (userNotes != null ? "userNotes=" + userNotes + ", " : "")
				+ "myNotes="
				+ myNotes
				+ ", sendUserNotesToNextStep="
				+ sendUserNotesToNextStep
				+ ", "
				+ (arrivingDate != null ? "arrivingDate=" + arrivingDate + ", "
						: "")
				+ (openedDate != null ? "openedDate=" + openedDate + ", " : "")
				+ (openerUser != null ? "openerUser=" + openerUser + ", " : "")
				+ (decidedDate != null ? "decidedDate=" + decidedDate + ", "
						: "")
				+ (performer != null ? "performer=" + performer + ", " : "")
				+ (response != null ? "response=" + response + ", " : "")
				+ (nextStepInstructions != null ? "nextStepInstructions="
						+ nextStepInstructions + ", " : "")
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
				+ "adminProcess="
				+ adminProcess
				+ ", locked="
				+ locked
				+ ", "
				+ (lockedBy != null ? "lockedBy=" + lockedBy + ", " : "")
				+ (lockedSince != null ? "lockedSince=" + lockedSince + ", "
						: "") + "sentBack=" + sentBack + ", "
				+ (assignedTo != null ? "assignedTo=" + assignedTo + ", " : "")
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


}