package org.beeblos.bpm.core.model.noper;

import java.util.Date;

public class WorkingProcessStep {

	private Integer idProcess;
	private Integer idStep;
	private String stepName;
	private Date arrivingDate;
	private Date openedDate;
	private Integer openerUser;
	private Date decidedDate;
	private Integer performer;
	private Date deadlineTime;
	
	public WorkingProcessStep() {
		
	}

	public WorkingProcessStep(Integer idProcess, Integer idStep,
			String stepName, Date arrivingDate, Date openedDate,
			Integer openerUser, Date decidedDate, Integer performer,
			Date deadlineTime) {
		super();
		this.idProcess = idProcess;
		this.idStep = idStep;
		this.stepName = stepName;
		this.arrivingDate = arrivingDate;
		this.openedDate = openedDate;
		this.openerUser = openerUser;
		this.decidedDate = decidedDate;
		this.performer = performer;
		this.deadlineTime = deadlineTime;
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

	@Override
	public String toString() {
		return "WorkingProcessStep [idProcess=" + idProcess + ", idStep="
				+ idStep + ", stepName=" + stepName + ", arrivingDate="
				+ arrivingDate + ", openedDate=" + openedDate + ", openerUser="
				+ openerUser + ", decidedDate=" + decidedDate + ", performer="
				+ performer + ", deadlineTime=" + deadlineTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arrivingDate == null) ? 0 : arrivingDate.hashCode());
		result = prime * result
				+ ((deadlineTime == null) ? 0 : deadlineTime.hashCode());
		result = prime * result
				+ ((decidedDate == null) ? 0 : decidedDate.hashCode());
		result = prime * result
				+ ((idProcess == null) ? 0 : idProcess.hashCode());
		result = prime * result + ((idStep == null) ? 0 : idStep.hashCode());
		result = prime * result
				+ ((openedDate == null) ? 0 : openedDate.hashCode());
		result = prime * result
				+ ((openerUser == null) ? 0 : openerUser.hashCode());
		result = prime * result
				+ ((performer == null) ? 0 : performer.hashCode());
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
		WorkingProcessStep other = (WorkingProcessStep) obj;
		if (arrivingDate == null) {
			if (other.arrivingDate != null)
				return false;
		} else if (!arrivingDate.equals(other.arrivingDate))
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
		if (stepName == null) {
			if (other.stepName != null)
				return false;
		} else if (!stepName.equals(other.stepName))
			return false;
		return true;
	}
	
}
