package org.beeblos.bpm.core.model;

import java.util.List;

import com.sp.common.model.ManagedDataField;

public class ManagedData {

	private Integer pk;
	
	private Integer currentWorkId; // used like as pk because 1 work must have only 1 managed-data-record
	private Integer currentStepWorkId; // used for tracking purposes: stores las step-work-id update the managed-data-record 
	private Integer processId; // version - for external access reference ...
	
	private String operation;
	
	WProcessHeadManagedDataConfiguration managedTableConfiguration;

	private boolean changed;
	
	List<ManagedDataField> dataField;
	
	// dml 20120124
	private Integer idWork;
	
	public ManagedData() {
		changed=false;
	}

	public ManagedData(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			this.managedTableConfiguration=new WProcessHeadManagedDataConfiguration();			
		}	
	}

	public Integer getPk() {
		return pk;
	}

	public void setPk(Integer pk) {
		this.pk = pk;
	}

	public Integer getCurrentWorkId() {
		return currentWorkId;
	}

	public void setCurrentWorkId(Integer currentWorkId) {
		this.currentWorkId = currentWorkId;
	}

	public Integer getCurrentStepWorkId() {
		return currentStepWorkId;
	}

	public void setCurrentStepWorkId(Integer currentStepWorkId) {
		this.currentStepWorkId = currentStepWorkId;
	}

	public Integer getProcessId() {
		return processId;
	}

	public void setProcessId(Integer processId) {
		this.processId = processId;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public List<ManagedDataField> getDataField() {
		return dataField;
	}

	public void setDataField(List<ManagedDataField> dataField) {
		this.dataField = dataField;
	}

	public Integer getIdWork() {
		return idWork;
	}

	public void setIdWork(Integer idWork) {
		this.idWork = idWork;
	}
	
	public WProcessHeadManagedDataConfiguration getManagedTableConfiguration() {
		return managedTableConfiguration;
	}

	public void setManagedTableConfiguration(
			WProcessHeadManagedDataConfiguration managedTableConfiguration) {
		this.managedTableConfiguration = managedTableConfiguration;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (changed ? 1231 : 1237);
		result = prime
				* result
				+ ((currentStepWorkId == null) ? 0 : currentStepWorkId
						.hashCode());
		result = prime * result
				+ ((currentWorkId == null) ? 0 : currentWorkId.hashCode());
		result = prime * result
				+ ((dataField == null) ? 0 : dataField.hashCode());
		result = prime * result + ((idWork == null) ? 0 : idWork.hashCode());
		result = prime
				* result
				+ ((managedTableConfiguration == null) ? 0
						: managedTableConfiguration.hashCode());
		result = prime * result
				+ ((operation == null) ? 0 : operation.hashCode());
		result = prime * result + ((pk == null) ? 0 : pk.hashCode());
		result = prime * result
				+ ((processId == null) ? 0 : processId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ManagedData))
			return false;
		ManagedData other = (ManagedData) obj;
		if (changed != other.changed)
			return false;
		if (currentStepWorkId == null) {
			if (other.currentStepWorkId != null)
				return false;
		} else if (!currentStepWorkId.equals(other.currentStepWorkId))
			return false;
		if (currentWorkId == null) {
			if (other.currentWorkId != null)
				return false;
		} else if (!currentWorkId.equals(other.currentWorkId))
			return false;
		if (dataField == null) {
			if (other.dataField != null)
				return false;
		} else if (!dataField.equals(other.dataField))
			return false;
		if (idWork == null) {
			if (other.idWork != null)
				return false;
		} else if (!idWork.equals(other.idWork))
			return false;
		if (managedTableConfiguration == null) {
			if (other.managedTableConfiguration != null)
				return false;
		} else if (!managedTableConfiguration
				.equals(other.managedTableConfiguration))
			return false;
		if (operation == null) {
			if (other.operation != null)
				return false;
		} else if (!operation.equals(other.operation))
			return false;
		if (pk == null) {
			if (other.pk != null)
				return false;
		} else if (!pk.equals(other.pk))
			return false;
		if (processId == null) {
			if (other.processId != null)
				return false;
		} else if (!processId.equals(other.processId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ManagedData ["
				+ (pk != null ? "pk=" + pk + ", " : "")
				+ (currentWorkId != null ? "currentWorkId=" + currentWorkId
						+ ", " : "")
				+ (currentStepWorkId != null ? "currentStepWorkId="
						+ currentStepWorkId + ", " : "")
				+ (processId != null ? "processId=" + processId + ", " : "")
				+ (operation != null ? "operation=" + operation + ", " : "")
				+ (managedTableConfiguration != null ? "managedTableConfiguration="
						+ managedTableConfiguration + ", "
						: "") + "changed=" + changed + ", "
				+ (dataField != null ? "dataField=" + dataField + ", " : "")
				+ (idWork != null ? "idWork=" + idWork : "") + "]";
	}
	
	
}
