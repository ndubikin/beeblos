package org.beeblos.bpm.core.model;

// Generated Oct 31, 2010 2:38:18 PM by Hibernate Tools 3.3.0.GA

import java.util.Date;

/**
 * WStepWorkAssignment generated by hbm2java
 */
public class WStepWorkAssignment implements java.io.Serializable {

	/**
	 * this entity drives the manual assignation of a step or a manual
	 * reassign of a step.
	 * In this scenario, the user can assign a step to a role or to a user ( 1 user or
	 * 1 role )
	 * In a loop case when the step goes to another step and return to this step 
	 * there must be not a problem because the reference is between step-work ( workitem ) 
	 * and this entity, so 1 and only 1 manual assignment is supported for a concrete
	 * workitem.
	 * The field "active" may be drive the possibility of have more than 1 assignment and
	 * user must set the active reassignment...
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer idAssignedRole;
	private Integer idAssignedUser; // userId
	private boolean active;
	private boolean reassigned;
	private Integer reassignedBy; // userId
	private Date reassignedDate;
	private boolean fromReassignment;
	private Integer fromReassignmentBy; // userId
	private Date fromReassignmentDate;
	
	private Date insertDate;
	private Integer insertUser;
	private Date modDate;
	private Integer modUser;

	public WStepWorkAssignment() {
		super();
	}


	public WStepWorkAssignment(Integer idAssignedRole, Integer idAssignedUser,
			boolean active, boolean reassigned, Integer reassignedBy,
			Date reassignedDate, boolean fromReassignment,
			Integer fromReassignmentBy, Date fromReassignmentDate) {
		this.idAssignedRole = idAssignedRole;
		this.idAssignedUser = idAssignedUser;
		this.active = active;
		this.reassigned = reassigned;
		this.reassignedBy = reassignedBy;
		this.reassignedDate = reassignedDate;
		this.fromReassignment = fromReassignment;
		this.fromReassignmentBy = fromReassignmentBy;
		this.fromReassignmentDate = fromReassignmentDate;
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isReassigned() {
		return reassigned;
	}

	public void setReassigned(boolean reassigned) {
		this.reassigned = reassigned;
	}

	public boolean isFromReassignment() {
		return fromReassignment;
	}

	public void setFromReassignment(boolean fromReassignment) {
		this.fromReassignment = fromReassignment;
	}

	public Integer getIdAssignedRole() {
		return this.idAssignedRole;
	}

	public void setIdAssignedRole(Integer idAssignedRole) {
		this.idAssignedRole = idAssignedRole;
	}

	public Date getReassignedDate() {
		return this.reassignedDate;
	}

	public void setReassignedDate(Date reassignedDate) {
		this.reassignedDate = reassignedDate;
	}


	public void setFromReassignmentDate(Date fromReassignmentDate) {
		this.fromReassignmentDate = fromReassignmentDate;
	}

	
	public Integer getIdAssignedUser() {
		return idAssignedUser;
	}


	public void setIdAssignedUser(Integer idAssignedUser) {
		this.idAssignedUser = idAssignedUser;
	}


	public Integer getReassignedBy() {
		return reassignedBy;
	}


	public void setReassignedBy(Integer reassignedBy) {
		this.reassignedBy = reassignedBy;
	}


	public Integer getFromReassignmentBy() {
		return fromReassignmentBy;
	}


	public void setFromReassignmentBy(Integer fromReassignmentBy) {
		this.fromReassignmentBy = fromReassignmentBy;
	}


	public Date getFromReassignmentDate() {
		return fromReassignmentDate;
	}


	/**
	 * @return the insertDate
	 */
	public Date getInsertDate() {
		return insertDate;
	}

	/**
	 * @param insertDate the insertDate to set
	 */
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	/**
	 * @return the insertUser
	 */
	public Integer getInsertUser() {
		return insertUser;
	}

	/**
	 * @param insertUser the insertUser to set
	 */
	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}

	/**
	 * @return the modDate
	 */
	public Date getModDate() {
		return modDate;
	}

	/**
	 * @param modDate the modDate to set
	 */
	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	/**
	 * @return the modUser
	 */
	public Integer getModUser() {
		return modUser;
	}

	/**
	 * @param modUser the modUser to set
	 */
	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + (fromReassignment ? 1231 : 1237);
		result = prime
				* result
				+ ((fromReassignmentBy == null) ? 0 : fromReassignmentBy
						.hashCode());
		result = prime
				* result
				+ ((fromReassignmentDate == null) ? 0 : fromReassignmentDate
						.hashCode());
		result = prime * result
				+ ((idAssignedRole == null) ? 0 : idAssignedRole.hashCode());
		result = prime * result
				+ ((idAssignedUser == null) ? 0 : idAssignedUser.hashCode());
		result = prime * result + (reassigned ? 1231 : 1237);
		result = prime * result
				+ ((reassignedBy == null) ? 0 : reassignedBy.hashCode());
		result = prime * result
				+ ((reassignedDate == null) ? 0 : reassignedDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WStepWorkAssignment))
			return false;
		WStepWorkAssignment other = (WStepWorkAssignment) obj;
		if (active != other.active)
			return false;
		if (fromReassignment != other.fromReassignment)
			return false;
		if (fromReassignmentBy == null) {
			if (other.fromReassignmentBy != null)
				return false;
		} else if (!fromReassignmentBy.equals(other.fromReassignmentBy))
			return false;
		if (fromReassignmentDate == null) {
			if (other.fromReassignmentDate != null)
				return false;
		} else if (!fromReassignmentDate.equals(other.fromReassignmentDate))
			return false;
		if (idAssignedRole == null) {
			if (other.idAssignedRole != null)
				return false;
		} else if (!idAssignedRole.equals(other.idAssignedRole))
			return false;
		if (idAssignedUser == null) {
			if (other.idAssignedUser != null)
				return false;
		} else if (!idAssignedUser.equals(other.idAssignedUser))
			return false;
		if (reassigned != other.reassigned)
			return false;
		if (reassignedBy == null) {
			if (other.reassignedBy != null)
				return false;
		} else if (!reassignedBy.equals(other.reassignedBy))
			return false;
		if (reassignedDate == null) {
			if (other.reassignedDate != null)
				return false;
		} else if (!reassignedDate.equals(other.reassignedDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WStepWorkAssignment ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (idAssignedRole != null ? "idAssignedRole=" + idAssignedRole
						+ ", " : "")
				+ (idAssignedUser != null ? "idAssignedUser=" + idAssignedUser
						+ ", " : "")
				+ "active="
				+ active
				+ ", reassigned="
				+ reassigned
				+ ", "
				+ (reassignedBy != null ? "reassignedBy=" + reassignedBy + ", "
						: "")
				+ (reassignedDate != null ? "reassignedDate=" + reassignedDate
						+ ", " : "")
				+ "fromReassignment="
				+ fromReassignment
				+ ", "
				+ (fromReassignmentBy != null ? "fromReassignmentBy="
						+ fromReassignmentBy + ", " : "")
				+ (fromReassignmentDate != null ? "fromReassignmentDate="
						+ fromReassignmentDate + ", " : "")
				+ (insertDate != null ? "insertDate=" + insertDate + ", " : "")
				+ (insertUser != null ? "insertUser=" + insertUser + ", " : "")
				+ (modDate != null ? "modDate=" + modDate + ", " : "")
				+ (modUser != null ? "modUser=" + modUser : "") + "]";
	}

	
	
	
}
