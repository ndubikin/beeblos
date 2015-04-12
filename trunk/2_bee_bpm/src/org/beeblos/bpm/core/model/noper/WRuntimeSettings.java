package org.beeblos.bpm.core.model.noper;

// Generated Oct 30, 2010 12:25:05 AM by Hibernate Tools 3.3.0.GA

import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WTimeUnit;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


/**
 * WStepDef generated by hbm2java
 * 
 * defines step, related pages and possible responses for an instance (WStepWork)
 */
public class WRuntimeSettings implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String instructions;
	private String stepComments;

	/**
	 * To update or synchronize managed data from step...
	 */
	private ManagedData managedData;
	
	
//	private String idListZone;
//	private String idWorkZone;
//	private String idAdditionalZone;
	
	/**
	 * Base URL of webapp
	 * Optional
	 * Full URL must be built with baseUrl+appUrl+urlData
	 * 
	 */
	private String baseUrl;
	/**
	 * Application specific URL with step processor info and some of
	 * standard parameters (ie: idStepWork, idObject, idObjectType)
	 * This info is programmer responsability
	 * Optional
	 * Full URL must be built with baseUrl+appUrl+urlData
	 */
	private String appUrl;
	/**
	 * Specific url string with data of the instance.
	 * Optional
	 * Full URL must be built with baseUrl+appUrl+urlData
	 */
	private String urlData;
	
	private WTimeUnit timeUnit;
	private Integer assignedTime;
	private LocalDate deadlineDate;
	private LocalTime deadlineTime;
	private WTimeUnit reminderTimeUnit;
	private Integer reminderTime; // en unidades de tiempo indicadas en reminderTimeUnit
	

	public WRuntimeSettings() {

	}


	/**
	 * WRuntimeSettings constructor with params...
	 * 
	 * @param instructions
	 * @param stepComments
	 * @param md
	 * @param timeUnit
	 * @param assignedTime
	 * @param deadlineDate
	 * @param deadlineTime
	 * @param reminderTimeUnit
	 * @param reminderTime
	 */
	public WRuntimeSettings(String instructions, String stepComments,
			ManagedData md,
			WTimeUnit timeUnit, Integer assignedTime, LocalDate deadlineDate,
			LocalTime deadlineTime, WTimeUnit reminderTimeUnit, Integer reminderTime) {
		super();
		this.instructions = instructions;
		this.stepComments = stepComments;
		this.managedData = md;
		this.timeUnit = timeUnit;
		this.assignedTime = assignedTime;
		this.deadlineDate = deadlineDate;
		this.deadlineTime = deadlineTime;
		this.reminderTimeUnit = reminderTimeUnit;
		this.reminderTime = reminderTime;
	}





	public String getStepComments() {
		return this.stepComments;
	}

	public void setStepComments(String stepComments) {
		this.stepComments = stepComments;
	}

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

	public ManagedData getManagedData() {
		return managedData;
	}


	public void setManagedData(ManagedData managedData) {
		this.managedData = managedData;
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
	public LocalDate getDeadlineDate() {
		return deadlineDate;
	}

	/**
	 * @param deadlineDate the deadlineDate to set
	 */
	public void setDeadlineDate(LocalDate deadlineDate) {
		this.deadlineDate = deadlineDate;
	}

	/**
	 * @return the deadlineTime
	 */
	public LocalTime getDeadlineTime() {
		return deadlineTime;
	}

	/**
	 * @param deadlineTime the deadlineTime to set
	 */
	public void setDeadlineTime(LocalTime deadlineTime) {
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





	/**
	 * @return the baseUrl
	 * Base URL of webapp
	 * Optional
	 * Full URL must be built with baseUrl+appUrl+urlData
	 * 
	 */
	public String getBaseUrl() {
		return baseUrl;
	}


	/**
	 * @param baseUrl the baseUrl to set
	 * Base URL of webapp
	 * Optional
	 * Full URL must be built with baseUrl+appUrl+urlData
	 * 
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}


	/**
	 * @return the appUrl
	 * Specific url string with data of the instance.
	 * Optional
	 * Full URL must be built with baseUrl+appUrl+urlData 
	 */
	public String getAppUrl() {
		return appUrl;
	}


	/**
	 * @param appUrl the appUrl to set
	 * Specific url string with data of the instance.
	 * Optional
	 * Full URL must be built with baseUrl+appUrl+urlData 
	 */
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}


	/**
	 * @return the urlData
	 * Specific url string with data of the instance.
	 * Optional
	 * Full URL must be built with baseUrl+appUrl+urlData 
	 */
	public String getUrlData() {
		return urlData;
	}


	/**
	 * @param urlData the urlData to set
	 * Specific url string with data of the instance.
	 * Optional
	 * Full URL must be built with baseUrl+appUrl+urlData 
	 */
	public void setUrlData(String urlData) {
		this.urlData = urlData;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((assignedTime == null) ? 0 : assignedTime.hashCode());
		result = prime * result
				+ ((deadlineDate == null) ? 0 : deadlineDate.hashCode());
		result = prime * result
				+ ((deadlineTime == null) ? 0 : deadlineTime.hashCode());
		result = prime * result
				+ ((instructions == null) ? 0 : instructions.hashCode());
		result = prime * result
				+ ((reminderTime == null) ? 0 : reminderTime.hashCode());
		result = prime
				* result
				+ ((reminderTimeUnit == null) ? 0 : reminderTimeUnit.hashCode());
		result = prime * result
				+ ((stepComments == null) ? 0 : stepComments.hashCode());
		result = prime * result
				+ ((timeUnit == null) ? 0 : timeUnit.hashCode());
		return result;
	}





	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WRuntimeSettings))
			return false;
		WRuntimeSettings other = (WRuntimeSettings) obj;
		if (assignedTime == null) {
			if (other.assignedTime != null)
				return false;
		} else if (!assignedTime.equals(other.assignedTime))
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
		if (instructions == null) {
			if (other.instructions != null)
				return false;
		} else if (!instructions.equals(other.instructions))
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
		if (stepComments == null) {
			if (other.stepComments != null)
				return false;
		} else if (!stepComments.equals(other.stepComments))
			return false;
		if (timeUnit == null) {
			if (other.timeUnit != null)
				return false;
		} else if (!timeUnit.equals(other.timeUnit))
			return false;
		return true;
	}





	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WRuntimeSettings [assignedTime=" + assignedTime
				+ ", deadlineDate=" + deadlineDate + ", deadlineTime="
				+ deadlineTime + ", instructions=" + instructions
				+ ", reminderTime=" + reminderTime + ", reminderTimeUnit="
				+ reminderTimeUnit + ", stepComments=" + stepComments
				+ ", timeUnit=" + timeUnit + "]";
	}



}
