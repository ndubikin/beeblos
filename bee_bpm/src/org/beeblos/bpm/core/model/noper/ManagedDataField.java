package org.beeblos.bpm.core.model.noper;

import org.beeblos.bpm.core.model.WDataType;
import org.beeblos.bpm.core.model.WProcessDataField;



public class ManagedDataField {

	private Integer currentWorkId;
	private Integer currentStepWorkId;
	private WProcessDataField dataFieldDefinition;
	
	WDataType dataType;
	String name;
	String value;
	boolean modified;
	boolean required;
	boolean readOnly;
	boolean forcedModification;
	
	public ManagedDataField(Integer currentWorkId, Integer currentStepWorkId,
			WProcessDataField dataFieldDefinition, WDataType dataType,
			String name, String value, boolean modified) {
		setCurrentWorkId(currentWorkId);
		setCurrentStepWorkId(currentStepWorkId);
		setDataFieldDefinition(dataFieldDefinition);
		setDataType(dataType);
		setName(name);
		setValue(value);
		setModified(modified);
	}
	
	public ManagedDataField(Integer currentWorkId, Integer currentStepWorkId,
			WProcessDataField dataFieldDefinition, WDataType dataType,
			String name, String value, boolean modified, boolean required,
			boolean readOnly, boolean forcedModification) {
		setCurrentWorkId(currentWorkId);
		setCurrentStepWorkId(currentStepWorkId);
		setDataFieldDefinition(dataFieldDefinition);
		setDataType(dataType);
		setName(name);
		setValue(value);
		setModified(modified);
		setRequired(required);
		setReadOnly(readOnly);
		setForcedModification(forcedModification);
	}

	public WDataType getDataType() {
		return dataType;
	}
	public void setDataType(WDataType dataType) {
		this.dataType = dataType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isModified() {
		return modified;
	}
	public void setModified(boolean modified) {
		this.modified = modified;
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

	public WProcessDataField getDataFieldDefinition() {
		return dataFieldDefinition;
	}

	public void setDataFieldDefinition(WProcessDataField dataFieldDefinition) {
		this.dataFieldDefinition = dataFieldDefinition;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isForcedModification() {
		return forcedModification;
	}

	public void setForcedModification(boolean forcedModification) {
		this.forcedModification = forcedModification;
	}

	@Override
	public String toString() {
		return "ManagedDataField ["
				+ (currentWorkId != null ? "currentWorkId=" + currentWorkId
						+ ", " : "")
				+ (currentStepWorkId != null ? "currentStepWorkId="
						+ currentStepWorkId + ", " : "")
				+ (dataFieldDefinition != null ? "dataFieldDefinition="
						+ dataFieldDefinition + ", " : "")
				+ (dataType != null ? "dataType=" + dataType + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (value != null ? "value=" + value + ", " : "") + "modified="
				+ modified + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((currentStepWorkId == null) ? 0 : currentStepWorkId
						.hashCode());
		result = prime * result
				+ ((currentWorkId == null) ? 0 : currentWorkId.hashCode());
		result = prime
				* result
				+ ((dataFieldDefinition == null) ? 0 : dataFieldDefinition
						.hashCode());
		result = prime * result
				+ ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + (modified ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ManagedDataField))
			return false;
		ManagedDataField other = (ManagedDataField) obj;
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
		if (dataFieldDefinition == null) {
			if (other.dataFieldDefinition != null)
				return false;
		} else if (!dataFieldDefinition.equals(other.dataFieldDefinition))
			return false;
		if (dataType == null) {
			if (other.dataType != null)
				return false;
		} else if (!dataType.equals(other.dataType))
			return false;
		if (modified != other.modified)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	
	
}
