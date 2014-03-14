package org.beeblos.bpm.core.model;

// Generated Jul 29, 2013 1:15:55 PM by Hibernate Tools 3.4.0.CR1

import org.joda.time.DateTime;

/**
 * WProcessDataField generated by hbm2java
 */
public class WStepDataField implements java.io.Serializable {

	/**
	 * this Class manages related fields for a a step
	 * Only must select a step already defined in w_process_data_field 
	 * because the datafields are managed at process-head level
	 * Used datafields can't be deleted from w_process_data_field:
	 * the system will check for all values = null to delete a process_data_field
	 * from managed table.
	 *  
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer stepHeadId;
	private Integer processHeadId; // step data fields are related directly with process
									// each process has your own datafields
//	private WProcessHeadLight processHeadLigth;
//	private WStepHead stepHead;
	
	private WProcessDataField dataField;
	private Integer order;
	private String name;
	private String comments;
	private boolean active;
	private boolean readOnly; // indicates this data field must be showeD but no modified (default step processor)
	private boolean forceModification; // idicates user must input the value in this WStepDataField
	private boolean required;

	private Integer length;
	private String defaultValue;
	// trail
	private DateTime insertDate;
	private Integer insertUser;
	private DateTime modDate;
	private Integer modUser;

	public WStepDataField() {
		super();
	}
	
	public WStepDataField(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			this.dataField = new WProcessDataField(createEmtpyObjects);
		}	
	}
	
	public WStepDataField(Integer id, Integer stepHeadId,
			WProcessDataField dataField, String name, Boolean required,
			String comments, Boolean active, Boolean readOnly, Integer length, String defaultValue) {
		this.id = id;
		this.stepHeadId = stepHeadId;
		this.dataField = dataField;
		this.name = name;
		this.required = required;
		this.comments = comments;
		this.active = active;
		this.readOnly= readOnly; 
		this.length = length;
		this.defaultValue = defaultValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public WProcessDataField getDataField() {
		return dataField;
	}

	public void setDataField(WProcessDataField dataField) {
		this.dataField = dataField;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public boolean isForceModification() {
		return forceModification;
	}

	public void setForceModification(boolean forceModification) {
		this.forceModification = forceModification;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public boolean isActive() {
		return active;
	}

	public boolean isRequired() {
		return required;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public DateTime getInsertDate() {
		return insertDate;
	}

	@Override
	public String toString() {
		return "WStepDataField ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (stepHeadId != null ? "stepHeadId=" + stepHeadId + ", " : "")
				+ (processHeadId != null ? "processHeadId=" + processHeadId
						+ ", " : "")
				+ (dataField != null ? "dataField=" + dataField + ", " : "")
				+ (order != null ? "order=" + order + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (comments != null ? "comments=" + comments + ", " : "")
				+ "active="
				+ active
				+ ", readOnly="
				+ readOnly
				+ ", forceModification="
				+ forceModification
				+ ", required="
				+ required
				+ ", "
				+ (length != null ? "length=" + length + ", " : "")
				+ (defaultValue != null ? "defaultValue=" + defaultValue + ", "
						: "")
				+ (insertDate != null ? "insertDate=" + insertDate + ", " : "")
				+ (insertUser != null ? "insertUser=" + insertUser + ", " : "")
				+ (modDate != null ? "modDate=" + modDate + ", " : "")
				+ (modUser != null ? "modUser=" + modUser : "") + "]";
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

	public Integer getStepHeadId() {
		return stepHeadId;
	}

	public void setStepHeadId(Integer stepHeadId) {
		this.stepHeadId = stepHeadId;
	}

	public Integer getProcessHeadId() {
		return processHeadId;
	}

	public void setProcessHeadId(Integer processHeadId) {
		this.processHeadId = processHeadId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result
				+ ((dataField == null) ? 0 : dataField.hashCode());
		result = prime * result
				+ ((defaultValue == null) ? 0 : defaultValue.hashCode());
		result = prime * result + (forceModification ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result
				+ ((insertUser == null) ? 0 : insertUser.hashCode());
		result = prime * result + ((length == null) ? 0 : length.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result
				+ ((processHeadId == null) ? 0 : processHeadId.hashCode());
		result = prime * result + (readOnly ? 1231 : 1237);
		result = prime * result + (required ? 1231 : 1237);
		result = prime * result
				+ ((stepHeadId == null) ? 0 : stepHeadId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WStepDataField))
			return false;
		WStepDataField other = (WStepDataField) obj;
		if (active != other.active)
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (dataField == null) {
			if (other.dataField != null)
				return false;
		} else if (!dataField.equals(other.dataField))
			return false;
		if (defaultValue == null) {
			if (other.defaultValue != null)
				return false;
		} else if (!defaultValue.equals(other.defaultValue))
			return false;
		if (forceModification != other.forceModification)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		if (length == null) {
			if (other.length != null)
				return false;
		} else if (!length.equals(other.length))
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (processHeadId == null) {
			if (other.processHeadId != null)
				return false;
		} else if (!processHeadId.equals(other.processHeadId))
			return false;
		if (readOnly != other.readOnly)
			return false;
		if (required != other.required)
			return false;
		if (stepHeadId == null) {
			if (other.stepHeadId != null)
				return false;
		} else if (!stepHeadId.equals(other.stepHeadId))
			return false;
		return true;
	}




}
