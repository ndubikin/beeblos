package org.beeblos.bpm.core.model;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class WSystem {

	private Integer id;
	private String name;
	private BigDecimal systemTypeId;
	private Integer mainAdminId;
	private String defaultTimezone;
	private Integer modUser;
	private DateTime modDate;
	
	public WSystem() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WSystem(Integer id, String name, BigDecimal systemTypeId,
			Integer mainAdminId, String defaultTimezone, Integer modUser,
			DateTime modDate) {
		this.id = id;
		this.name = name;
		this.systemTypeId = systemTypeId;
		this.mainAdminId = mainAdminId;
		this.defaultTimezone = defaultTimezone;
		this.modUser = modUser;
		this.modDate = modDate;
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
	/**
	 * @return the systemTypeId
	 */
	public BigDecimal getSystemTypeId() {
		return systemTypeId;
	}
	/**
	 * @param systemTypeId the systemTypeId to set
	 */
	public void setSystemTypeId(BigDecimal systemTypeId) {
		this.systemTypeId = systemTypeId;
	}
	/**
	 * @return the mainAdminId
	 */
	public Integer getMainAdminId() {
		return mainAdminId;
	}
	/**
	 * @param mainAdminId the mainAdminId to set
	 */
	public void setMainAdminId(Integer mainAdminId) {
		this.mainAdminId = mainAdminId;
	}
	/**
	 * @return the defaultTimezone
	 */
	public String getDefaultTimezone() {
		return defaultTimezone;
	}
	/**
	 * @param defaultTimezone the defaultTimezone to set
	 */
	public void setDefaultTimezone(String defaultTimezone) {
		this.defaultTimezone = defaultTimezone;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WSystem ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (systemTypeId != null ? "systemTypeId=" + systemTypeId + ", "
						: "")
				+ (mainAdminId != null ? "mainAdminId=" + mainAdminId + ", "
						: "")
				+ (defaultTimezone != null ? "defaultTimezone="
						+ defaultTimezone + ", " : "")
				+ (modUser != null ? "modUser=" + modUser + ", " : "")
				+ (modDate != null ? "modDate=" + modDate : "") + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((defaultTimezone == null) ? 0 : defaultTimezone.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((mainAdminId == null) ? 0 : mainAdminId.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((systemTypeId == null) ? 0 : systemTypeId.hashCode());
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
		if (!(obj instanceof WSystem))
			return false;
		WSystem other = (WSystem) obj;
		if (defaultTimezone == null) {
			if (other.defaultTimezone != null)
				return false;
		} else if (!defaultTimezone.equals(other.defaultTimezone))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mainAdminId == null) {
			if (other.mainAdminId != null)
				return false;
		} else if (!mainAdminId.equals(other.mainAdminId))
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
		if (systemTypeId == null) {
			if (other.systemTypeId != null)
				return false;
		} else if (!systemTypeId.equals(other.systemTypeId))
			return false;
		return true;
	}
	
}
