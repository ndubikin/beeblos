package org.beeblos.bpm.core.model.mobile;

import java.util.List;

import com.sp.common.util.StringPair;

/**
 * Class to pass a simple WStepWork to mobile devices.
 * 
 * It has a few of the properties of the WStepWork. 
 * 
 * TODO: + comments
 * 
 * @author pab
 *
 */
public class WStepWorkMobile {

	private Integer id;
	private String stepName;
	private String arrivingDate;
	private String userInstructions;
	private String userNotes;
	private Boolean sendUserNotesToNextStep;
	private String insertUser;
	private List<StringPair> dataFields;
	
	private List<WStepResponseDefMobile> responses;
	
	private String processReference;
	private String processName;
	private String processComments;
	public WStepWorkMobile() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public WStepWorkMobile(Integer id, String stepName, String arrivingDate, String userInstructions, String userNotes,
			Boolean sendUserNotesToNextStep, String insertUser, List<StringPair> dataFields,
			List<WStepResponseDefMobile> responses, String processReference, String processName,
			String processComments) {
		super();
		this.id = id;
		this.stepName = stepName;
		this.arrivingDate = arrivingDate;
		this.userInstructions = userInstructions;
		this.userNotes = userNotes;
		this.sendUserNotesToNextStep = sendUserNotesToNextStep;
		this.insertUser = insertUser;
		this.dataFields = dataFields;
		this.responses = responses;
		this.processReference = processReference;
		this.processName = processName;
		this.processComments = processComments;
	}

	public WStepWorkMobile(Integer id, String stepName, String arrivingDate, String userInstructions,
			String userNotes, Boolean sendUserNotesToNextStep, List<WStepResponseDefMobile> responses,
			String processReference, String processName, String processComments) {
		super();
		this.id = id;
		this.stepName = stepName;
		this.arrivingDate = arrivingDate;
		this.userInstructions = userInstructions;
		this.userNotes = userNotes;
		this.sendUserNotesToNextStep = sendUserNotesToNextStep;
		this.responses = responses;
		this.processReference = processReference;
		this.processName = processName;
		this.processComments = processComments;
	}
	
	public WStepWorkMobile(Integer id, String stepName, String arrivingDate, String userInstructions, String userNotes,
			Boolean sendUserNotesToNextStep, String insertUser, List<WStepResponseDefMobile> responses,
			String processReference, String processName, String processComments) {
		super();
		this.id = id;
		this.stepName = stepName;
		this.arrivingDate = arrivingDate;
		this.userInstructions = userInstructions;
		this.userNotes = userNotes;
		this.sendUserNotesToNextStep = sendUserNotesToNextStep;
		this.insertUser = insertUser;
		this.responses = responses;
		this.processReference = processReference;
		this.processName = processName;
		this.processComments = processComments;
	}

	@Override
	public String toString() {
		return "WStepWorkMobile [id=" + id + ", stepName=" + stepName + ", arrivingDate=" + arrivingDate
				+ ", userInstructions=" + userInstructions + ", userNotes=" + userNotes + ", sendUserNotesToNextStep="
				+ sendUserNotesToNextStep + ", insertUser=" + insertUser + ", dataFields=" + dataFields + ", responses="
				+ responses + ", processReference=" + processReference + ", processName=" + processName
				+ ", processComments=" + processComments + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arrivingDate == null) ? 0 : arrivingDate.hashCode());
		result = prime * result + ((dataFields == null) ? 0 : dataFields.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((insertUser == null) ? 0 : insertUser.hashCode());
		result = prime * result + ((processComments == null) ? 0 : processComments.hashCode());
		result = prime * result + ((processName == null) ? 0 : processName.hashCode());
		result = prime * result + ((processReference == null) ? 0 : processReference.hashCode());
		result = prime * result + ((responses == null) ? 0 : responses.hashCode());
		result = prime * result + ((sendUserNotesToNextStep == null) ? 0 : sendUserNotesToNextStep.hashCode());
		result = prime * result + ((stepName == null) ? 0 : stepName.hashCode());
		result = prime * result + ((userInstructions == null) ? 0 : userInstructions.hashCode());
		result = prime * result + ((userNotes == null) ? 0 : userNotes.hashCode());
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
		WStepWorkMobile other = (WStepWorkMobile) obj;
		if (arrivingDate == null) {
			if (other.arrivingDate != null)
				return false;
		} else if (!arrivingDate.equals(other.arrivingDate))
			return false;
		if (dataFields == null) {
			if (other.dataFields != null)
				return false;
		} else if (!dataFields.equals(other.dataFields))
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
		if (processComments == null) {
			if (other.processComments != null)
				return false;
		} else if (!processComments.equals(other.processComments))
			return false;
		if (processName == null) {
			if (other.processName != null)
				return false;
		} else if (!processName.equals(other.processName))
			return false;
		if (processReference == null) {
			if (other.processReference != null)
				return false;
		} else if (!processReference.equals(other.processReference))
			return false;
		if (responses == null) {
			if (other.responses != null)
				return false;
		} else if (!responses.equals(other.responses))
			return false;
		if (sendUserNotesToNextStep == null) {
			if (other.sendUserNotesToNextStep != null)
				return false;
		} else if (!sendUserNotesToNextStep.equals(other.sendUserNotesToNextStep))
			return false;
		if (stepName == null) {
			if (other.stepName != null)
				return false;
		} else if (!stepName.equals(other.stepName))
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
		return true;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getArrivingDate() {
		return arrivingDate;
	}
	public void setArrivingDate(String arrivingDate) {
		this.arrivingDate = arrivingDate;
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
	public Boolean getSendUserNotesToNextStep() {
		return sendUserNotesToNextStep;
	}
	public void setSendUserNotesToNextStep(Boolean sendUserNotesToNextStep) {
		this.sendUserNotesToNextStep = sendUserNotesToNextStep;
	}
	public List<WStepResponseDefMobile> getResponses() {
		return responses;
	}
	public void setResponses(List<WStepResponseDefMobile> responses) {
		this.responses = responses;
	}
	public String getProcessReference() {
		return processReference;
	}
	public void setProcessReference(String processReference) {
		this.processReference = processReference;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getProcessComments() {
		return processComments;
	}
	public void setProcessComments(String processComments) {
		this.processComments = processComments;
	}

	public String getInsertUser() {
		return insertUser;
	}
	public void setInsertUser(String insertUser) {
		this.insertUser = insertUser;
	}
	public List<StringPair> getDataFields() {
		return dataFields;
	}
	public void setDataFields(List<StringPair> dataFields) {
		this.dataFields = dataFields;
	}
}
