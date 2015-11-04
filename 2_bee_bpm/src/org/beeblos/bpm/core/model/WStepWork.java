package org.beeblos.bpm.core.model;

// Generated Oct 30, 2010 12:25:05 AM by Hibernate Tools 3.3.0.GA

import static com.sp.common.util.ConstantsCommon.EMPTY_OBJECT;
import static org.beeblos.bpm.core.util.Constants.W_SYSROLE_ORIGINATOR_ID;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.beeblos.bpm.core.model.noper.WRuntimeSettings;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


/**
 * WStepWork - represents a task or item instance
 */
public class WStepWork implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	/**
	 * Header of this process work. Common for all stepWork items
	 * 
	 */	
	private WProcessWork wProcessWork; //
	
	/**
	 * to manage custom data fields related with this step work
	 * also contains table configuration data to access this info
	 * 
	 */
	private ManagedData managedData;  
	
	// TODO - QUITAR ESTAS DOS PROPIEDADES Q YA LAS TENEMOS EN WPROCESSWORK
//	private WProcessDef process;
	
//	@Deprecated
//	private Integer version;
	
	private WStepDef previousStep;
	
	/**
	 * Current step definition
	 */
	private WStepDef currentStep;
	
	private String userInstructions;
	private String userNotes;
	
	/**
	 * Indicates the user set some user notes to himself
	 * and must be stored with this step
	 */
	private boolean myNotes;
	/**
	 * Indicates user notes must be sent to next step as personal o customized
	 * instructions (load nextStepInstructions field)
	 */
	private boolean sendUserNotesToNextStep;
	
	private DateTime arrivingDate;
	private DateTime openedDate;
	private WUserDef openerUser;
	/**
	 * datetime at which the step was performed
	 * decidedDate != null >> the step was performed
	 */
	private DateTime decidedDate;
	private WUserDef performer;
	
	/**
	 * refactorizado nes 20151018 - pasado de String a Integer
	 */
	private WStepResponseDef response;
	private String nextStepInstructions;
	
	private WTimeUnit timeUnit;
	private Integer assignedTime;
	private LocalDate deadlineDate;
	private LocalTime deadlineTime;
	private WTimeUnit reminderTimeUnit;
	private Integer reminderTime; // en unidades de tiempo indicadas en reminderTimeUnit
	
	/**
	 * indicates this step was performed by a user as Administrator
	 * of the process of step
	 */
	private boolean adminProcess;
	
	/**
	 * Indicates this task is locked
	 */
	private boolean locked;
	private WUserDef lockedBy; // id del usuario que lo bloqueó
	private DateTime lockedSince;
	
	private boolean sentBack;
	
	/**
	 * Field to store id or url data specific for this instance.
	 * At definition time the arquitect can indicate de idDefaultProcessor (i.e. an URL) with
	 * the step may be processed (manual or automatic)
	 * If the URL requires specific instance data, this is the property hold this info.
	 * This info + wStepDef.idDefaultProcessor (or wProcessDef.idProcessorStep) + url base will
	 * be a full url to access to process the instance (wstepwork)
	 * 
	 */
	private String urlData;
	
	/**
	 * a list of persons or roles have assigned this task
	 * This list is only valid for a specific (or an instance) task
	 * This list must be created between task creation time and task performed
	 * time. No modifications has accepted after the task is realized.
	 */
	private Set<WStepWorkAssignment> assignedTo = new HashSet<WStepWorkAssignment>();

	private WUserDef insertUser; // user has processed previous step ( and inserted current step )
	private DateTime modDate;
	private Integer modUser;

	public WStepWork() {
		super();
	}
	
	public WStepWork(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			this.wProcessWork=new WProcessWork( EMPTY_OBJECT );
			this.managedData=new ManagedData( EMPTY_OBJECT );
//			this.process=new WProcessDef( EMPTY_OBJECT );
			this.previousStep=new WStepDef( EMPTY_OBJECT );
			this.currentStep=new WStepDef( EMPTY_OBJECT );
			this.openerUser=new WUserDef( EMPTY_OBJECT );
			this.performer=new WUserDef( EMPTY_OBJECT );
			this.timeUnit=new WTimeUnit( EMPTY_OBJECT );
			this.reminderTimeUnit=new WTimeUnit( EMPTY_OBJECT );
			this.lockedBy=new WUserDef( EMPTY_OBJECT );
			this.insertUser=new WUserDef( EMPTY_OBJECT );
			this.response=new WStepResponseDef();
			
		}	
	}

	public WStepWork(WStepWork step) {

		this.id = step.id;
//		this.process = step.process;
//		this.version = step.version;
		this.previousStep = step.previousStep;
		this.currentStep = step.currentStep;
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

	public WStepWork(Integer id) {

		this.id = id;
	}


	public WStepWork(Integer id,/* WProcessDef process, Integer version,*/
			WStepDef previousStep, WStepDef currentStep, String reference, String comments,
			DateTime arrivingDate, DateTime openedDate, WUserDef openerUser,
			DateTime decidedDate, WUserDef performer, WStepResponseDef response, // nes 20151018
			String nextStepInstructions, WTimeUnit timeUnit,
			Integer assignedTime, LocalDate deadlineDate, LocalTime deadlineTime,
			WTimeUnit reminderTimeUnit, Integer reminderTime,
			boolean adminProcess, boolean locked, WUserDef lockedBy,
			DateTime lockedSince, Set<WStepWorkAssignment> assignedTo,
			WUserDef insertUser, DateTime modDate, Integer modUser) {
		this.id = id;
//		this.process = process;
//		this.version = version;
		this.previousStep = previousStep;
		this.currentStep = currentStep;
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
	 * Flag indicating step work was already processed
	 * nes 20151018
	 * @return
	 */
	public boolean isProcessed() {
		if (this.decidedDate!=null) {
			return true;
		} else {
			return false;
		}
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

//	/**
//	 * @return the process
//	 */
//	public WProcessDef getProcess() {
//		return process;
//	}
//
//	/**
//	 * @param process the process to set
//	 */
//	public void setProcess(WProcessDef process) {
//		this.process = process;
//	}

//	/**
//	 * @return the version
//	 */
//	@Deprecated
//	public Integer getVersion() {
//		return version;
//	}
//
//
//	/**
//	 * @param version the version to set
//	 */
//	@Deprecated	
//	public void setVersion(Integer version) {
//		this.version = version;
//	}

	/**
	 * @return the previousStep
	 */
	public WStepDef getPreviousStep() {
		return previousStep;
	}


	public ManagedData getManagedData() {
		return managedData;
	}

	public void setManagedData(ManagedData managedData) {
		this.managedData = managedData;
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

	public DateTime getArrivingDate() {
		return this.arrivingDate;
	}

	public void setArrivingDate(DateTime arrivingDate) {
		this.arrivingDate = arrivingDate;
	}

	public DateTime getOpenedDate() {
		return this.openedDate;
	}

	public void setOpenedDate(DateTime openedDate) {
		this.openedDate = openedDate;
	}

	public WUserDef getOpenerUser() {
		return this.openerUser;
	}

	public void setOpenerUser(WUserDef openerUser) {
		this.openerUser = openerUser;
	}

	public DateTime getDecidedDate() {
		return this.decidedDate;
	}

	public void setDecidedDate(DateTime decidedDate) {
		this.decidedDate = decidedDate;
	}

	public WUserDef getPerformer() {
		return this.performer;
	}

	public void setPerformer(WUserDef performer) {
		this.performer = performer;
	}

	public Integer getAssignedTime() {
		return this.assignedTime;
	}

	public void setAssignedTime(Integer assignedTime) {
		this.assignedTime = assignedTime;
	}

	public LocalDate getDeadlineDate() {
		return this.deadlineDate;
	}

	public void setDeadlineDate(LocalDate deadlineDate) {
		this.deadlineDate = deadlineDate;
	}

	public LocalTime getDeadlineTime() {
		return this.deadlineTime;
	}

	public void setDeadlineTime(LocalTime deadlineTime) {
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
	 * @return the assignedTo
	 */
	public List<WStepWorkAssignment> getAssignedToAsList() {
		if (assignedTo != null){
			return new ArrayList<WStepWorkAssignment>(assignedTo);
		}
		return null;
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
	public DateTime getModDate() {
		return modDate;
	}


	/**
	 * @param modDate the modDate to set
	 */
	public void setModDate(DateTime modDate) {
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
	public DateTime getLockedSince() {
		return lockedSince;
	}


	/**
	 * @param lockedSince the lockedSince to set
	 */
	public void setLockedSince(DateTime lockedSince) {
		this.lockedSince = lockedSince;
	}


	/**
	 * refactorizado de String a WStepResponseDef
	 * nes 20151018
	 * @return
	 */
	public WStepResponseDef getResponse() {
		return response;
	}

	/**
	 * refactorizado de String a WStepResponseDef
	 * nes 20151018
	 * @return
	 */
	public void setResponse(WStepResponseDef response) {
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
	
	
	
	/**
	 * @return the urlData
	 * 
	 * Field to store id or url data specific for this instance.
	 * At definition time the arquitect can indicate de idDefaultProcessor (i.e. an URL) with
	 * the step may be processed (manual or automatic)
	 * If the URL requires specific instance data, this is the property hold this info.
	 * This info + wStepDef.idDefaultProcessor (or wProcessDef.idProcessorStep) + url base will
	 * be a full url to access to process the instance (wstepwork)
	 * 
	 */
	public String getUrlData() {
		return urlData;
	}

	/**
	 * @param urlData the urlData to set
	 *
	 * Field to store id or url data specific for this instance.
	 * At definition time the arquitect can indicate de idDefaultProcessor (i.e. an URL) with
	 * the step may be processed (manual or automatic)
	 * If the URL requires specific instance data, this is the property hold this info.
	 * This info + wStepDef.idDefaultProcessor (or wProcessDef.idProcessorStep) + url base will
	 * be a full url to access to process the instance (wstepwork)
	 * 
	 */
	public void setUrlData(String urlData) {
		this.urlData = urlData;
	}

	/**
	 * checks if current stepWork has assigned sysrole-originator
	 * nes 20141014
	 * @return
	 */
	public boolean isSysroleOriginator(){
		for (WStepRole wsr: this.getCurrentStep().getRolesRelatedAsList()) {
			if (wsr.getRole().getId().equals(W_SYSROLE_ORIGINATOR_ID)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Builds a WRuntimeSettings object from current wStepWork
	 * nes 20150411
	 * @return
	 */
	public WRuntimeSettings getRuntimeSettings() {
		
		WRuntimeSettings runtimeSettings = 
				new WRuntimeSettings(this.getNextStepInstructions(), 
						null, this.getManagedData(), 
						this.getTimeUnit(), this.getAssignedTime(), 
						this.getDeadlineDate(), this.getDeadlineTime(), 
						this.getReminderTimeUnit(), this.getReminderTime());
		
		
		return runtimeSettings;
	}

	@Override
	public String toString() {
		return "WStepWork ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (wProcessWork != null ? "wProcessWork=" + wProcessWork + ", "
						: "")
				+ (managedData != null ? "managedData="
						+ managedData + ", " : "")
//				+ (process != null ? "process=" + process + ", " : "")
				+ (previousStep != null ? "previousStep=" + previousStep + ", "
						: "")
				+ (currentStep != null ? "currentStep=" + currentStep + ", "
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((insertUser == null) ? 0 : insertUser.hashCode());
		result = prime * result + (locked ? 1231 : 1237);
		result = prime * result
				+ ((lockedBy == null) ? 0 : lockedBy.hashCode());
		result = prime * result
				+ ((lockedSince == null) ? 0 : lockedSince.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
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
//		result = prime * result + ((process == null) ? 0 : process.hashCode());
		result = prime * result
				+ ((reminderTime == null) ? 0 : reminderTime.hashCode());
		result = prime
				* result
				+ ((reminderTimeUnit == null) ? 0 : reminderTimeUnit.hashCode());
		result = prime * result
				+ ((response == null) ? 0 : response.hashCode());
		result = prime * result + (sendUserNotesToNextStep ? 1231 : 1237);
		result = prime * result + (sentBack ? 1231 : 1237);
		result = prime * result
				+ ((timeUnit == null) ? 0 : timeUnit.hashCode());
		result = prime
				* result
				+ ((userInstructions == null) ? 0 : userInstructions.hashCode());
		result = prime * result
				+ ((userNotes == null) ? 0 : userNotes.hashCode());
		result = prime * result
				+ ((wProcessWork == null) ? 0 : wProcessWork.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
//		if (process == null) {
//			if (other.process != null)
//				return false;
//		} else if (!process.equals(other.process))
//			return false;
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
		if (sentBack != other.sentBack)
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
		if (wProcessWork == null) {
			if (other.wProcessWork != null)
				return false;
		} else if (!wProcessWork.equals(other.wProcessWork))
			return false;
		return true;
	}

	// nes 20151018
	public boolean empty() {

		if (id!=null && ! id.equals(0)) return false;

		if (this.wProcessWork!=null && !this.wProcessWork.empty()) return false;
		if (this.managedData !=null && !this.managedData.empty()) return false;
		if (this.previousStep !=null && !this.previousStep.empty()) return false;
		if (this.currentStep !=null && !this.currentStep.empty()) return false;
		if (this.openerUser !=null && !this.openerUser.empty()) return false;
		if (this.performer !=null && !this.performer.empty()) return false;
		if (this.timeUnit !=null && !this.timeUnit.empty()) return false;
		if (this.reminderTimeUnit!=null && !this.reminderTimeUnit.empty()) return false;
		if (this.lockedBy !=null && !this.lockedBy.empty()) return false;
		if (this.insertUser !=null && !this.insertUser.empty()) return false;
		if (this.response !=null && !this.response.empty()) return false; 

		
		if (this.userInstructions  != null && !"".equals(userInstructions)) return false;
		if (this.userNotes  != null && !"".equals(userNotes)) return false;
		if (this.urlData  != null && !"".equals(urlData)) return false;
		if (this.nextStepInstructions  != null && !"".equals(nextStepInstructions)) return false;
		
		if (this.arrivingDate  != null ) return false;
		if (this.openedDate  != null ) return false;
		if (this.decidedDate  != null ) return false;
		if (this.lockedSince  != null ) return false;

		if (this.deadlineDate  != null ) return false;
		if (this.deadlineTime  != null ) return false;

		if (assignedTime!=null && !assignedTime.equals(0)) return false;
		if (reminderTime!=null && !reminderTime.equals(0)) return false;

		return true;
	}


}