package org.beeblos.bpm.core.model;

import org.joda.time.DateTime;

public class WStepTypeDef {

	private Integer id;
	private String name;
	private String type;
	private boolean active;
	private boolean engineReq;
	private boolean deleted;
	private String comments;
	private DateTime insertDate;
	private Integer insertUser;
	private DateTime modDate;
	private Integer modUser;
	
	public WStepTypeDef(Integer id, String name, String type, boolean active,
			boolean engineReq, boolean deleted, String comments,
			DateTime insertDate, Integer insertUser, DateTime modDate,
			Integer modUser) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.active = active;
		this.engineReq = engineReq;
		this.deleted = deleted;
		this.comments = comments;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.modDate = modDate;
		this.modUser = modUser;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the engineReq
	 */
	public boolean isEngineReq() {
		return engineReq;
	}

	/**
	 * @param engineReq the engineReq to set
	 */
	public void setEngineReq(boolean engineReq) {
		this.engineReq = engineReq;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the insertDate
	 */
	public DateTime getInsertDate() {
		return insertDate;
	}

	/**
	 * @param insertDate the insertDate to set
	 */
	public void setInsertDate(DateTime insertDate) {
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
	public DateTime getModDate() {
		return modDate;
	}

	/**
	 * @param modDate the modDate to set
	 */
	public void setModDate(DateTime modDate) {
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

	public WStepTypeDef() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WStepTypeDef(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WStepTypeDef [" + (id != null ? "id=" + id + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (type != null ? "type=" + type + ", " : "") + "active="
				+ active + ", engineReq=" + engineReq + ", deleted=" + deleted
				+ ", "
				+ (comments != null ? "comments=" + comments + ", " : "")
				+ (insertDate != null ? "insertDate=" + insertDate + ", " : "")
				+ (insertUser != null ? "insertUser=" + insertUser + ", " : "")
				+ (modDate != null ? "modDate=" + modDate + ", " : "")
				+ (modUser != null ? "modUser=" + modUser : "") + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + (engineReq ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result
				+ ((insertUser == null) ? 0 : insertUser.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (!(obj instanceof WStepTypeDef))
			return false;
		WStepTypeDef other = (WStepTypeDef) obj;
		if (active != other.active)
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (deleted != other.deleted)
			return false;
		if (engineReq != other.engineReq)
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
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
