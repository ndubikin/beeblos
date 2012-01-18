package org.beeblos.bpm.core.model.noper;

import java.util.Date;

public class WorkingProcessWork {

	private Integer idProcess;
	private String processName;
	private String workReference;
	private Integer liveSteps;
	private Date started;
	private String status;
	private Date finished;
	
	public WorkingProcessWork() {
		
	}
	
	public WorkingProcessWork(Integer idProcess, String processName,
			String workReference, Integer liveSteps, Date started, String status,
			Date finished) {
		super();
		this.idProcess = idProcess;
		this.processName = processName;
		this.workReference = workReference;
		this.liveSteps = liveSteps;
		this.started = started;
		this.status = status;
		this.finished = finished;
	}

	public Integer getIdProcess() {
		return idProcess;
	}

	public void setIdProcess(Integer idProcess) {
		this.idProcess = idProcess;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getWorkReference() {
		return workReference;
	}

	public void setWorkReference(String workReference) {
		this.workReference = workReference;
	}

	public Integer getLiveSteps() {
		return liveSteps;
	}

	public void setLiveSteps(Integer liveSteps) {
		this.liveSteps = liveSteps;
	}

	public Date getStarted() {
		return started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getFinished() {
		return finished;
	}

	public void setFinished(Date finished) {
		this.finished = finished;
	}

	@Override
	public String toString() {
		return "WorkingProcessWork [idProcess=" + idProcess + ", processName="
				+ processName + ", workReference=" + workReference + ", liveSteps="
				+ liveSteps + ", started=" + started + ", status=" + status
				+ ", finished=" + finished + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((finished == null) ? 0 : finished.hashCode());
		result = prime * result
				+ ((idProcess == null) ? 0 : idProcess.hashCode());
		result = prime * result
				+ ((processName == null) ? 0 : processName.hashCode());
		result = prime * result + ((started == null) ? 0 : started.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((liveSteps == null) ? 0 : liveSteps.hashCode());
		result = prime * result
				+ ((workReference == null) ? 0 : workReference.hashCode());
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
		WorkingProcessWork other = (WorkingProcessWork) obj;
		if (finished == null) {
			if (other.finished != null)
				return false;
		} else if (!finished.equals(other.finished))
			return false;
		if (idProcess == null) {
			if (other.idProcess != null)
				return false;
		} else if (!idProcess.equals(other.idProcess))
			return false;
		if (processName == null) {
			if (other.processName != null)
				return false;
		} else if (!processName.equals(other.processName))
			return false;
		if (started == null) {
			if (other.started != null)
				return false;
		} else if (!started.equals(other.started))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (liveSteps == null) {
			if (other.liveSteps != null)
				return false;
		} else if (!liveSteps.equals(other.liveSteps))
			return false;
		if (workReference == null) {
			if (other.workReference != null)
				return false;
		} else if (!workReference.equals(other.workReference))
			return false;
		return true;
	}
	
	
}
