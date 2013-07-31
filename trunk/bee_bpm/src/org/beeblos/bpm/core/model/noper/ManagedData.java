package org.beeblos.bpm.core.model.noper;

import java.util.List;

public class ManagedData {

	private Integer currentWorkId;
	private Integer currentStepWorkId;

	private boolean changed;
	
	List<ManagedDataField> dataField;
	
	// dml 20120124
	private Integer idWork;
	
	public ManagedData() {
		changed=false;
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
	
	
	
}
