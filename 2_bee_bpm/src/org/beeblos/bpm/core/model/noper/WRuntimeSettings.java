package org.beeblos.bpm.core.model.noper;

// Generated Oct 30, 2010 12:25:05 AM by Hibernate Tools 3.3.0.GA

import java.util.List;

import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WTimeUnit;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.sp.common.model.FileSP;


/**
 * WRuntimeSettings will be used to inyect/synchronize data at runtime in a process / wstepwork
 * 
 * ie: will be sent instructions, deadline (if it's modifiable at runtime), data fields, attachments,
 * etc
 * 
 */
public class WRuntimeSettings implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * instructions to next step...
	 */
	private String instructions;
	
	/**
	 * My notes...
	 * refactirized nes 20151104
	 */
	private String stepNotes;

	/**
	 * To update or synchronize managed data at step...
	 * ManagedData contains de definition of user data fields related with a wProcessWork instance
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
	
	/**
	 * New files to attach to the idProcessWork
	 * 
	 * @author dmuleiro 20150414
	 */
	private List<FileSP> fileSPList;
	

	public WRuntimeSettings() {

	}


	/**
	 * WRuntimeSettings constructor with params...
	 * 
	 * @param instructions
	 * @param stepNotes
	 * @param md
	 * @param timeUnit
	 * @param assignedTime
	 * @param deadlineDate
	 * @param deadlineTime
	 * @param reminderTimeUnit
	 * @param reminderTime
	 */
	public WRuntimeSettings(String instructions, String stepNotes,
			ManagedData md,
			WTimeUnit timeUnit, Integer assignedTime, LocalDate deadlineDate,
			LocalTime deadlineTime, WTimeUnit reminderTimeUnit, Integer reminderTime) {
		super();
		this.instructions = instructions;
		this.stepNotes = stepNotes;
		this.managedData = md;
		this.timeUnit = timeUnit;
		this.assignedTime = assignedTime;
		this.deadlineDate = deadlineDate;
		this.deadlineTime = deadlineTime;
		this.reminderTimeUnit = reminderTimeUnit;
		this.reminderTime = reminderTime;
	}





	public String getStepNotes() {
		return this.stepNotes;
	}

	public void setStepNotes(String stepNotes) {
		this.stepNotes = stepNotes;
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


	/**
	 * @return the fileSPList
	 */
	public List<FileSP> getFileSPList() {
		return fileSPList;
	}


	/**
	 * @param fileSPList the fileSPList to set
	 */
	public void setFileSPList(List<FileSP> fileSPList) {
		this.fileSPList = fileSPList;
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
				+ ((stepNotes == null) ? 0 : stepNotes.hashCode());
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
		if (stepNotes == null) {
			if (other.stepNotes != null)
				return false;
		} else if (!stepNotes.equals(other.stepNotes))
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
				+ reminderTimeUnit + ", stepNotes=" + stepNotes
				+ ", timeUnit=" + timeUnit + "]";
	}



}
