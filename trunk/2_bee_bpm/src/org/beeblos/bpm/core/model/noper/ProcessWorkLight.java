package org.beeblos.bpm.core.model.noper;

import org.joda.time.DateTime;

/**
 * 
 * ProcessWorkLight is a thin object resume of WProcessWork mainly created to manage lists of
 * processWork objects to display purposes...
 * 
 * Process work are the instances of a defined process (WProcessDef) running in a bee-bpm server (or db)
 * The process work must be injected or started by someone or somewhere... At start time the system
 * creates the ProcessWork and their tasks (stepWork): only the tasks belonging to "start" event are 
 * created and wait for processing.  
 * This method search for all instances (active / all) works
 * Each work can have many step-works: tasks to realize (or realized) in that instance of
 * the process: 
 *
 *
 */
public class ProcessWorkLight {

	/**
	 * ProcessWork id
	 */
	private Integer id;
	
	/**
	 * id process def for this processWork
	 */
	private Integer idProcess;
	
	/**
	 * Process definition name
	 */
	private String processName;
	
	/**
	 * reference (user readable) for this instance of 
	 * process work
	 */
	private String workReference;
	/**
	 * reference (user readable) for this instance of 
	 * process work 
	 */
	private String workComments;
	/**
	 * qty of live steps (WStepWork) runnint at this time for
	 * this WProcessWork
	 */
	private Integer liveSteps;
	
	/**
	 * time this instance was started (injected)
	 */
	private DateTime started;

	/**
	 * Finish date for this process.
	 * Note: if the process if finished there is mandatory
	 * that's no live steps exists
	 */
	private DateTime finished;
	/**
	 * Status id for this instance
	 */
	private Integer statusId;

	/**
	 * Status name for this instance
	 */
	private String status;
	
	public ProcessWorkLight() {
		
	}
	
	public ProcessWorkLight(Integer idProcess, String processName,
			String workReference, String workComments, Integer liveSteps, DateTime started, String status,
			DateTime finished, Integer id) {
		super();
		this.idProcess = idProcess;
		this.processName = processName;
		this.workReference = workReference;
		this.workComments = workComments;
		this.liveSteps = liveSteps;
		this.started = started;
		this.status = status;
		this.finished = finished;
		this.id = id;
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

	public String getWorkComments() {
		return workComments;
	}

	public void setWorkComments(String workComments) {
		this.workComments = workComments;
	}

	public Integer getLiveSteps() {
		return liveSteps;
	}

	public void setLiveSteps(Integer liveSteps) {
		this.liveSteps = liveSteps;
	}

	public DateTime getStarted() {
		return started;
	}

	public void setStarted(DateTime started) {
		this.started = started;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DateTime getFinished() {
		return finished;
	}

	public void setFinished(DateTime finished) {
		this.finished = finished;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ProcessWorkLight ["
				+ (idProcess != null ? "idProcess=" + idProcess + ", " : "")
				+ (processName != null ? "processName=" + processName + ", "
						: "")
				+ (workReference != null ? "workReference=" + workReference
						+ ", " : "")
				+ (workComments != null ? "workComments=" + workComments + ", "
						: "")
				+ (liveSteps != null ? "liveSteps=" + liveSteps + ", " : "")
				+ (started != null ? "started=" + started + ", " : "")
				+ (statusId != null ? "statusId=" + statusId + ", " : "")
				+ (status != null ? "status=" + status + ", " : "")
				+ (finished != null ? "finished=" + finished + ", " : "")
				+ (id != null ? "idWork=" + id : "") + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((finished == null) ? 0 : finished.hashCode());
		result = prime * result
				+ ((idProcess == null) ? 0 : idProcess.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((liveSteps == null) ? 0 : liveSteps.hashCode());
		result = prime * result
				+ ((processName == null) ? 0 : processName.hashCode());
		result = prime * result + ((started == null) ? 0 : started.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((statusId == null) ? 0 : statusId.hashCode());
		result = prime * result
				+ ((workComments == null) ? 0 : workComments.hashCode());
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
		if (!(obj instanceof ProcessWorkLight))
			return false;
		ProcessWorkLight other = (ProcessWorkLight) obj;
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (liveSteps == null) {
			if (other.liveSteps != null)
				return false;
		} else if (!liveSteps.equals(other.liveSteps))
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
		if (statusId == null) {
			if (other.statusId != null)
				return false;
		} else if (!statusId.equals(other.statusId))
			return false;
		if (workComments == null) {
			if (other.workComments != null)
				return false;
		} else if (!workComments.equals(other.workComments))
			return false;
		if (workReference == null) {
			if (other.workReference != null)
				return false;
		} else if (!workReference.equals(other.workReference))
			return false;
		return true;
	}
	
	public boolean empty() {

		if (id!=null && !id.equals(0)) return false;
		if (idProcess!=null && !idProcess.equals(0)) return false;
		if (liveSteps!=null && !liveSteps.equals(0)) return false;
		if (statusId!=null && !statusId.equals(0)) return false;

		if (processName!=null && ! "".equals(processName)) return false;
		if (workReference!=null && ! "".equals(workReference)) return false;
		if (workComments!=null && ! "".equals(workComments)) return false;
		if (status!=null && ! "".equals(status)) return false;
		
		if (started!=null) return false;
		if (finished!=null) return false;

		return true;
	}
}
