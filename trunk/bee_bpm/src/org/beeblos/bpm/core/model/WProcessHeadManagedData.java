package org.beeblos.bpm.core.model;

// Generated Jul 30, 2013 5:35:35 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * WProcessHeadManagedData generated by hbm2java
 */
public class WProcessHeadManagedData implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int headId;
	private String name;
	private Boolean ignoreCase;
	private String schema;
	private String catalog;
	private String comment;
	private Date insertDate;
	private int insertUser;
	private Date modDate;
	private int modUser;

	public WProcessHeadManagedData() {
	}

	public WProcessHeadManagedData(Integer headId, String name,
			String schema, String catalog, Date insertDate, int insertUser,
			Date modDate, int modUser) {
		setHeadId(headId);
		this.name = name;
		this.schema = schema;
		this.catalog = catalog;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.modDate = modDate;
		this.modUser = modUser;
	}

	public int getHeadId() {
		return this.headId;
	}

	public void setHeadId(int headId) {
		this.headId = headId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIgnoreCase() {
		return this.ignoreCase;
	}

	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public String getSchema() {
		return this.schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getCatalog() {
		return this.catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public int getInsertUser() {
		return this.insertUser;
	}

	public void setInsertUser(int insertUser) {
		this.insertUser = insertUser;
	}

	public Date getModDate() {
		return this.modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	public int getModUser() {
		return this.modUser;
	}

	public void setModUser(int modUser) {
		this.modUser = modUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((catalog == null) ? 0 : catalog.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + headId;
		result = prime * result
				+ ((ignoreCase == null) ? 0 : ignoreCase.hashCode());
		result = prime * result
				+ ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result + insertUser;
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + modUser;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((schema == null) ? 0 : schema.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WProcessHeadManagedData))
			return false;
		WProcessHeadManagedData other = (WProcessHeadManagedData) obj;
		if (catalog == null) {
			if (other.catalog != null)
				return false;
		} else if (!catalog.equals(other.catalog))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (headId != other.headId)
			return false;
		if (ignoreCase == null) {
			if (other.ignoreCase != null)
				return false;
		} else if (!ignoreCase.equals(other.ignoreCase))
			return false;
		if (insertDate == null) {
			if (other.insertDate != null)
				return false;
		} else if (!insertDate.equals(other.insertDate))
			return false;
		if (insertUser != other.insertUser)
			return false;
		if (modDate == null) {
			if (other.modDate != null)
				return false;
		} else if (!modDate.equals(other.modDate))
			return false;
		if (modUser != other.modUser)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (schema == null) {
			if (other.schema != null)
				return false;
		} else if (!schema.equals(other.schema))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WProcessHeadManagedData [headId=" + headId + ", "
				+ (name != null ? "name=" + name + ", " : "")
				+ (ignoreCase != null ? "ignoreCase=" + ignoreCase + ", " : "")
				+ (schema != null ? "schema=" + schema + ", " : "")
				+ (catalog != null ? "catalog=" + catalog + ", " : "")
				+ (comment != null ? "comment=" + comment + ", " : "")
				+ (insertDate != null ? "insertDate=" + insertDate + ", " : "")
				+ "insertUser=" + insertUser + ", "
				+ (modDate != null ? "modDate=" + modDate + ", " : "")
				+ "modUser=" + modUser + "]";
	}

	

}
