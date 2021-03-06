package org.beeblos.bpm.core.model;

// Generated Oct 31, 2010 9:23:40 PM by Hibernate Tools 3.3.0.GA

/**
 * WStepAssignedDef generated by hbm2java
 */
public class WStepAssignedDef implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;

	private String assignedTo;
	private String assignedToObject;

	public WStepAssignedDef() {
	}

	public WStepAssignedDef(String assignedTo, String assignedToObject) {
		this.assignedTo = assignedTo;
		this.assignedToObject = assignedToObject;
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAssignedTo() {
		return this.assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getAssignedToObject() {
		return this.assignedToObject;
	}

	public void setAssignedToObject(String assignedToObject) {
		this.assignedToObject = assignedToObject;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((assignedTo == null) ? 0 : assignedTo.hashCode());
		result = prime
				* result
				+ ((assignedToObject == null) ? 0 : assignedToObject.hashCode());
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
		if (!(obj instanceof WStepAssignedDef))
			return false;
		WStepAssignedDef other = (WStepAssignedDef) obj;
		if (assignedTo == null) {
			if (other.assignedTo != null)
				return false;
		} else if (!assignedTo.equals(other.assignedTo))
			return false;
		if (assignedToObject == null) {
			if (other.assignedToObject != null)
				return false;
		} else if (!assignedToObject.equals(other.assignedToObject))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WStepAssignedDef [assignedTo=" + assignedTo
				+ ", assignedToObject=" + assignedToObject + ", id=" + id + "]";
	}

	
	
	
}
