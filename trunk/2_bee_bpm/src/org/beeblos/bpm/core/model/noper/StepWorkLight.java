package org.beeblos.bpm.core.model.noper;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class StepWorkLight implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * stepWork pk
	 */
	private Integer idStepWork;
	
	/**
	 * WProcessWork id for this stepWork
	 */
	private Integer idProcessWork;
	
	/**
	 * WProcessDef id for this stepWork
	 */
	private Integer idProcess;
	/**
	 * WStepDef id for this step 
	 */
	private Integer idStep;
	/**
	 * step/task name
	 */
	private String stepName;
	/**
	 * Step/task reference for this item
	 */
	private String reference;
	/**
	 * comments for this item...
	 */
	private String comments;
	
	private DateTime arrivingDate;
	
	
	/**
	 * first person/user to view the task / work
	 */
	private Integer openerUser;
	private String openerUserLogin;
	private String openerUserName;
	/**
	 * Datetime the step/task was open (view for a person or system)
	 */
	private DateTime openedDate;

	/**
	 * person who realize the work / task
	 */
	private Integer performer;
	private String performerLogin;
	private String performerName;

	/**
	 * Datetime the task was realized
	 */
	private DateTime decidedDate;
	
	private LocalTime deadlineTime;
	private LocalDate deadlineDate;
	
	// dml 20120123
	/**
	 * Indicates the stepWork is locked.
	 * Only pending stepWork can be locked
	 */
	private boolean locked;
	/**
	 * Userid who locks the task/step
	 */
	private Integer lockedBy;
	
	//rrl 20150409 ITS: 917
	/**
	 * response to the stepWork
	 */
	private String response;
	/**
	 * nes 20151018
	 */
	private Integer idResponse;
	
	//rrl 20151028 ITS:1331
	/**
	 * related object referred by this instance
	 */
	private Integer idObject;
	/**
	 * related object type (java class) referred by this instance
	 */
	private String idObjectType;
	/**
	 * WProcessDef id for this stepWork
	 */
	private Integer idProcessDef;
	/**
	 * Indicates this task is locked
	 */
	private String lockedByName;
	private DateTime lockedSince;
	
	/**
	 * WProcessWork name for this stepWork
	 */
	private String processWorkName;
	
	private boolean sentBack;

	public StepWorkLight() {
		
	}

	public StepWorkLight(Integer idProcessWork, Integer idProcess, Integer idStep,
			String stepName, String reference, String comments, DateTime arrivingDate, DateTime openedDate,
			Integer openerUser, DateTime decidedDate, Integer performer,
			LocalDate deadlineDate, LocalTime deadlineTime, boolean locked, Integer lockedBy,
			Integer idStepWork, 
			String openerUserLogin, String openerUserName,
			String performerLogin, String performerName, String response, Integer idResponse, // nes 20151018
			Integer idObject, String idObjectType, Integer idProcessDef, String lockedByName, DateTime lockedSince, String processWorkName, boolean sentBack) { //rrl 20151029 ITS:1331
		super();
		this.idProcessWork = idProcessWork;
		this.idProcess = idProcess;
		this.idStep = idStep;
		this.stepName = stepName;
		this.reference = reference;
		this.comments = comments;
		this.arrivingDate = arrivingDate;
		this.openedDate = openedDate;
		this.openerUser = openerUser;
		this.decidedDate = decidedDate;
		this.performer = performer;
		this.deadlineDate = deadlineDate;
		this.deadlineTime = deadlineTime;
		this.locked = locked;
		this.lockedBy = lockedBy;
		this.idStepWork = idStepWork;
		this.openerUserLogin = openerUserLogin;
		this.openerUserName = openerUserName;
		this.performerLogin = performerLogin;
		this.performerName = performerName;
		this.response = response;
		this.idResponse = idResponse;
		this.idObject = idObject;
		this.idObjectType = idObjectType;
		this.idProcessDef = idProcessDef;
		this.lockedByName = lockedByName;
		this.lockedSince = lockedSince;
		this.processWorkName = processWorkName;
		this.sentBack = sentBack;
	}

	/**
	 * @return the idProcessWork
	 */
	public Integer getIdProcessWork() {
		return idProcessWork;
	}

	/**
	 * @param idProcessWork the idProcessWork to set
	 */
	public void setIdProcessWork(Integer idProcessWork) {
		this.idProcessWork = idProcessWork;
	}

	public Integer getIdProcess() {
		return idProcess;
	}

	public void setIdProcess(Integer idProcess) {
		this.idProcess = idProcess;
	}

	public Integer getIdStep() {
		return idStep;
	}

	public void setIdStep(Integer idStep) {
		this.idStep = idStep;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getWorkReference() {
		return reference;
	}

	public void setWorkReference(String workReference) {
		this.reference = workReference;
	}

	public DateTime getArrivingDate() {
		return arrivingDate;
	}

	public void setArrivingDate(DateTime arrivingDate) {
		this.arrivingDate = arrivingDate;
	}

	public DateTime getOpenedDate() {
		return openedDate;
	}

	public void setOpenedDate(DateTime openedDate) {
		this.openedDate = openedDate;
	}

	public Integer getOpenerUser() {
		return openerUser;
	}

	public void setOpenerUser(Integer openerUser) {
		this.openerUser = openerUser;
	}

	public DateTime getDecidedDate() {
		return decidedDate;
	}

	public void setDecidedDate(DateTime decidedDate) {
		this.decidedDate = decidedDate;
	}

	public Integer getPerformer() {
		return performer;
	}

	public void setPerformer(Integer performer) {
		this.performer = performer;
	}

	public LocalTime getDeadlineTime() {
		return deadlineTime;
	}

	public void setDeadlineTime(LocalTime deadlineTime) {
		this.deadlineTime = deadlineTime;
	}

	public LocalDate getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(LocalDate deadlineDate) {
		this.deadlineDate = deadlineDate;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public Integer getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(Integer lockedBy) {
		this.lockedBy = lockedBy;
	}

	public Integer getIdStepWork() {
		return idStepWork;
	}

	public void setIdStepWork(Integer idStepWork) {
		this.idStepWork = idStepWork;
	}
	
	//rrl 20150409 ITS: 917
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * @return the idResponse nes 20151018
	 */
	public Integer getIdResponse() {
		return idResponse;
	}

	/**
	 * @param idResponse the idResponse to set
	 */
	public void setIdResponse(Integer idResponse) {
		this.idResponse = idResponse;
	}

	public String getOpenerUserLogin() {
		return openerUserLogin;
	}

	public void setOpenerUserLogin(String openerUserLogin) {
		this.openerUserLogin = openerUserLogin;
	}

	public String getOpenerUserName() {
		return openerUserName;
	}

	public void setOpenerUserName(String openerUserName) {
		this.openerUserName = openerUserName;
	}

	public String getPerformerLogin() {
		return performerLogin;
	}

	public void setPerformerLogin(String performerLogin) {
		this.performerLogin = performerLogin;
	}

	public String getPerformerName() {
		return performerName;
	}

	public void setPerformerName(String performerName) {
		this.performerName = performerName;
	}

	//rrl 20151029 ITS:1331
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
	
	public Integer getIdProcessDef() {
		return idProcessDef;
	}

	public void setIdProcessDef(Integer idProcessDef) {
		this.idProcessDef = idProcessDef;
	}
	
	public String getLockedByName() {
		return lockedByName;
	}

	public void setLockedByName(String lockedByName) {
		this.lockedByName = lockedByName;
	}
	
	public DateTime getLockedSince() {
		return lockedSince;
	}

	public void setLockedSince(DateTime lockedSince) {
		this.lockedSince = lockedSince;
	}
	
	public String getProcessWorkName() {
		return processWorkName;
	}

	public void setProcessWorkName(String processWorkName) {
		this.processWorkName = processWorkName;
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
		result = prime * result
				+ ((arrivingDate == null) ? 0 : arrivingDate.hashCode());
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
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
				+ ((idProcess == null) ? 0 : idProcess.hashCode());
		result = prime * result
				+ ((idProcessDef == null) ? 0 : idProcessDef.hashCode());
		result = prime * result
				+ ((idProcessWork == null) ? 0 : idProcessWork.hashCode());
		result = prime * result
				+ ((idResponse == null) ? 0 : idResponse.hashCode());
		result = prime * result + ((idStep == null) ? 0 : idStep.hashCode());
		result = prime * result
				+ ((idStepWork == null) ? 0 : idStepWork.hashCode());
		result = prime * result + (locked ? 1231 : 1237);
		result = prime * result
				+ ((lockedBy == null) ? 0 : lockedBy.hashCode());
		result = prime * result
				+ ((lockedByName == null) ? 0 : lockedByName.hashCode());
		result = prime * result
				+ ((lockedSince == null) ? 0 : lockedSince.hashCode());
		result = prime * result
				+ ((openedDate == null) ? 0 : openedDate.hashCode());
		result = prime * result
				+ ((openerUser == null) ? 0 : openerUser.hashCode());
		result = prime * result
				+ ((openerUserLogin == null) ? 0 : openerUserLogin.hashCode());
		result = prime * result
				+ ((openerUserName == null) ? 0 : openerUserName.hashCode());
		result = prime * result
				+ ((performer == null) ? 0 : performer.hashCode());
		result = prime * result
				+ ((performerLogin == null) ? 0 : performerLogin.hashCode());
		result = prime * result
				+ ((performerName == null) ? 0 : performerName.hashCode());
		result = prime * result
				+ ((processWorkName == null) ? 0 : processWorkName.hashCode());
		result = prime * result
				+ ((reference == null) ? 0 : reference.hashCode());
		result = prime * result
				+ ((response == null) ? 0 : response.hashCode());
		result = prime * result + (sentBack ? 1231 : 1237);
		result = prime * result
				+ ((stepName == null) ? 0 : stepName.hashCode());
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
		StepWorkLight other = (StepWorkLight) obj;
		if (arrivingDate == null) {
			if (other.arrivingDate != null)
				return false;
		} else if (!arrivingDate.equals(other.arrivingDate))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
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
		if (idProcess == null) {
			if (other.idProcess != null)
				return false;
		} else if (!idProcess.equals(other.idProcess))
			return false;
		if (idProcessDef == null) {
			if (other.idProcessDef != null)
				return false;
		} else if (!idProcessDef.equals(other.idProcessDef))
			return false;
		if (idProcessWork == null) {
			if (other.idProcessWork != null)
				return false;
		} else if (!idProcessWork.equals(other.idProcessWork))
			return false;
		if (idResponse == null) {
			if (other.idResponse != null)
				return false;
		} else if (!idResponse.equals(other.idResponse))
			return false;
		if (idStep == null) {
			if (other.idStep != null)
				return false;
		} else if (!idStep.equals(other.idStep))
			return false;
		if (idStepWork == null) {
			if (other.idStepWork != null)
				return false;
		} else if (!idStepWork.equals(other.idStepWork))
			return false;
		if (locked != other.locked)
			return false;
		if (lockedBy == null) {
			if (other.lockedBy != null)
				return false;
		} else if (!lockedBy.equals(other.lockedBy))
			return false;
		if (lockedByName == null) {
			if (other.lockedByName != null)
				return false;
		} else if (!lockedByName.equals(other.lockedByName))
			return false;
		if (lockedSince == null) {
			if (other.lockedSince != null)
				return false;
		} else if (!lockedSince.equals(other.lockedSince))
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
		if (openerUserLogin == null) {
			if (other.openerUserLogin != null)
				return false;
		} else if (!openerUserLogin.equals(other.openerUserLogin))
			return false;
		if (openerUserName == null) {
			if (other.openerUserName != null)
				return false;
		} else if (!openerUserName.equals(other.openerUserName))
			return false;
		if (performer == null) {
			if (other.performer != null)
				return false;
		} else if (!performer.equals(other.performer))
			return false;
		if (performerLogin == null) {
			if (other.performerLogin != null)
				return false;
		} else if (!performerLogin.equals(other.performerLogin))
			return false;
		if (performerName == null) {
			if (other.performerName != null)
				return false;
		} else if (!performerName.equals(other.performerName))
			return false;
		if (processWorkName == null) {
			if (other.processWorkName != null)
				return false;
		} else if (!processWorkName.equals(other.processWorkName))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (response == null) {
			if (other.response != null)
				return false;
		} else if (!response.equals(other.response))
			return false;
		if (sentBack != other.sentBack)
			return false;
		if (stepName == null) {
			if (other.stepName != null)
				return false;
		} else if (!stepName.equals(other.stepName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StepWorkLight [idStepWork=" + idStepWork + ", idProcessWork="
				+ idProcessWork + ", idProcess=" + idProcess + ", idStep="
				+ idStep + ", stepName=" + stepName + ", reference="
				+ reference + ", comments=" + comments + ", arrivingDate="
				+ arrivingDate + ", openerUser=" + openerUser
				+ ", openerUserLogin=" + openerUserLogin + ", openerUserName="
				+ openerUserName + ", openedDate=" + openedDate
				+ ", performer=" + performer + ", performerLogin="
				+ performerLogin + ", performerName=" + performerName
				+ ", decidedDate=" + decidedDate + ", deadlineTime="
				+ deadlineTime + ", deadlineDate=" + deadlineDate + ", locked="
				+ locked + ", lockedBy=" + lockedBy + ", response=" + response
				+ ", idResponse=" + idResponse + ", idObject=" + idObject
				+ ", idObjectType=" + idObjectType + ", idProcessDef="
				+ idProcessDef + ", lockedByName=" + lockedByName
				+ ", lockedSince=" + lockedSince + ", processWorkName="
				+ processWorkName + ", sentBack=" + sentBack + "]";
	}
	
}
