package org.beeblos.bpm.core.model;

import org.joda.time.DateTime;

public class WTrackWork implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;

	private Integer idObject;
	private String idObjectType;

	private WProcessDef process;
	private Integer version;

	private WStepDef previousStep;
	private WStepDef currentStep;

	private String response;

	private boolean adminProcess;

	private DateTime insertDate;
	private Integer insertUser;
	private DateTime modDate;
	private Integer modUser;

	private String comments;
	private String userNotes;
	
	public WTrackWork(){
		
	}

	public WTrackWork(Integer id, Integer idObject, String idObjectType,
			WProcessDef process, Integer version, WStepDef previousStep,
			WStepDef currentStep, String response, boolean adminProcess,
			DateTime insertDate, Integer insertUser, String comments,
			String userNotes) {
		this.id = id;
		this.idObject = idObject;
		this.idObjectType = idObjectType;
		this.process = process;
		this.version = version;
		this.previousStep = previousStep;
		this.currentStep = currentStep;
		this.response = response;
		this.adminProcess = adminProcess;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.comments = comments;
		this.userNotes = userNotes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public WProcessDef getProcess() {
		return process;
	}

	public void setProcess(WProcessDef process) {
		this.process = process;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public WStepDef getPreviousStep() {
		return previousStep;
	}

	public void setPreviousStep(WStepDef previousStep) {
		this.previousStep = previousStep;
	}

	public WStepDef getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(WStepDef currentStep) {
		this.currentStep = currentStep;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public boolean isAdminProcess() {
		return adminProcess;
	}

	public void setAdminProcess(boolean adminProcess) {
		this.adminProcess = adminProcess;
	}

	public DateTime getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(DateTime insertDate) {
		this.insertDate = insertDate;
	}

	public Integer getInsertUser() {
		return insertUser;
	}

	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getUserNotes() {
		return userNotes;
	}

	public void setUserNotes(String userNotes) {
		this.userNotes = userNotes;
	}

	@Override
	public String toString() {
		return "WTrackWork [id=" + id + ", idObject=" + idObject
				+ ", idObjectType=" + idObjectType + ", process=" + process
				+ ", version=" + version + ", previousStep=" + previousStep
				+ ", currentStep=" + currentStep + ", response=" + response
				+ ", adminProcess=" + adminProcess + ", insertDate="
				+ insertDate + ", insertUser=" + insertUser + ", modDate="
				+ modDate + ", modUser=" + modUser + ", comments=" + comments
				+ ", userNotes=" + userNotes + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (adminProcess ? 1231 : 1237);
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result
				+ ((currentStep == null) ? 0 : currentStep.hashCode());
		result = prime * result
				+ ((idObject == null) ? 0 : idObject.hashCode());
		result = prime * result
				+ ((idObjectType == null) ? 0 : idObjectType.hashCode());
		result = prime * result
				+ ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result
				+ ((insertUser == null) ? 0 : insertUser.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
		result = prime * result
				+ ((previousStep == null) ? 0 : previousStep.hashCode());
		result = prime * result + ((process == null) ? 0 : process.hashCode());
		result = prime * result
				+ ((response == null) ? 0 : response.hashCode());
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
		if (getClass() != obj.getClass())
			return false;
		WTrackWork other = (WTrackWork) obj;
		if (adminProcess != other.adminProcess)
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (currentStep == null) {
			if (other.currentStep != null)
				return false;
		} else if (!currentStep.equals(other.currentStep))
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
		if (insertDate == null) {
			if (other.insertDate != null)
				return false;
		} else if (!insertDate.equals(other.insertDate))
			return false;
		if (insertUser == null) {
			if (other.insertUser != null)
				return false;
		} else if (!insertUser.equals(other.insertUser))
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
		if (response == null) {
			if (other.response != null)
				return false;
		} else if (!response.equals(other.response))
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

}
