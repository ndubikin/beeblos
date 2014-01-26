package org.beeblos.bpm.core.model.noper;

import java.util.Date;

public class StepWorkLight {

	private Integer idProcess;
	private Integer idStep;
	private String stepName;
	private String reference;
	private String comments;
	private Date arrivingDate;
	private Date openedDate;
	private Integer openerUser;
	private Date decidedDate;
	private Integer performer;
	private Date deadlineTime;
	private Date deadlineDate;
	
	// dml 20120123
	private boolean locked;
	private Integer lockedBy;
	private Integer idStepWork;
	
	// dml 20120124
	private String openerUserLogin;
	private String openerUserName;
	private String performerLogin;
	private String performerName;
	
	public StepWorkLight() {
		
	}

	public StepWorkLight(Integer idProcess, Integer idStep,
			String stepName, String reference, String comments, Date arrivingDate, Date openedDate,
			Integer openerUser, Date decidedDate, Integer performer,
			Date deadlineDate, Date deadlineTime, boolean locked, Integer lockedBy,
			Integer idStepWork, String openerUserLogin, String openerUserName,
			String performerLogin, String performerName) {
		super();
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

	public Date getArrivingDate() {
		return arrivingDate;
	}

	public void setArrivingDate(Date arrivingDate) {
		this.arrivingDate = arrivingDate;
	}

	public Date getOpenedDate() {
		return openedDate;
	}

	public void setOpenedDate(Date openedDate) {
		this.openedDate = openedDate;
	}

	public Integer getOpenerUser() {
		return openerUser;
	}

	public void setOpenerUser(Integer openerUser) {
		this.openerUser = openerUser;
	}

	public Date getDecidedDate() {
		return decidedDate;
	}

	public void setDecidedDate(Date decidedDate) {
		this.decidedDate = decidedDate;
	}

	public Integer getPerformer() {
		return performer;
	}

	public void setPerformer(Integer performer) {
		this.performer = performer;
	}

	public Date getDeadlineTime() {
		return deadlineTime;
	}

	public void setDeadlineTime(Date deadlineTime) {
		this.deadlineTime = deadlineTime;
	}

	public Date getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(Date deadlineDate) {
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

	@Override
	public String toString() {
		return "StepWorkLight [idProcess=" + idProcess + ", idStep=" + idStep
				+ ", stepName=" + stepName 
				+ ", reference=" + reference
				+ ", comments=" + comments
				+ ", arrivingDate=" + arrivingDate + ", openedDate="
				+ openedDate + ", openerUser=" + openerUser + ", decidedDate="
				+ decidedDate + ", performer=" + performer + ", deadlineTime="
				+ deadlineTime + ", deadlineDate=" + deadlineDate + ", locked="
				+ locked + ", lockedBy=" + lockedBy + ", idStepWork="
				+ idStepWork + ", openerUserLogin=" + openerUserLogin
				+ ", openerUserName=" + openerUserName + ", performerLogin="
				+ performerLogin + ", performerName=" + performerName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arrivingDate == null) ? 0 : arrivingDate.hashCode());
		result = prime * result
				+ ((deadlineDate == null) ? 0 : deadlineDate.hashCode());
		result = prime * result
				+ ((deadlineTime == null) ? 0 : deadlineTime.hashCode());
		result = prime * result
				+ ((decidedDate == null) ? 0 : decidedDate.hashCode());
		result = prime * result
				+ ((idProcess == null) ? 0 : idProcess.hashCode());
		result = prime * result + ((idStep == null) ? 0 : idStep.hashCode());
		result = prime * result
				+ ((idStepWork == null) ? 0 : idStepWork.hashCode());
		result = prime * result + (locked ? 1231 : 1237);
		result = prime * result
				+ ((lockedBy == null) ? 0 : lockedBy.hashCode());
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
				+ ((reference == null) ? 0 : reference.hashCode());
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());		
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
		if (idProcess == null) {
			if (other.idProcess != null)
				return false;
		} else if (!idProcess.equals(other.idProcess))
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
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		
		if (stepName == null) {
			if (other.stepName != null)
				return false;
		} else if (!stepName.equals(other.stepName))
			return false;
		return true;
	}
	
}
