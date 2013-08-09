package org.beeblos.bpm.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WStepHead implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String name;	
	private String comments;

	private Date insertDate;
	private Integer insertUser;
	private Date modDate;
	private Integer modUser;

	Set<WStepDataField> dataFieldDef=new HashSet<WStepDataField>();

	public WStepHead() {
		super();
	}

	public WStepHead(Integer id) {
		this.id = id;
	}

	public WStepHead(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public WStepHead(String name, String comments, 
			Date fechaAlta, Date fechaModificacion) {
		this.name = name;
		this.comments = comments;
		this.insertDate = fechaAlta;
		this.modDate = fechaModificacion;
	}

	public WStepHead(String name, String comments,
			Date insertDate, Integer insertUser, Date modDate,
			Integer modUser) {
		this.name = name;
		this.comments = comments;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.modDate = modDate;
		this.modUser = modUser;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}	

	public Date getInsertDate() {
		return insertDate;
	}


	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}


	public Integer getInsertUser() {
		return insertUser;
	}


	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}


	public Date getModDate() {
		return modDate;
	}


	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}


	public Integer getModUser() {
		return modUser;
	}


	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}


	public Set<WStepDataField> getDataFieldDef() {
		return dataFieldDef;
	}


	public void setDataFieldDef(Set<WStepDataField> dataFieldDef) {
		this.dataFieldDef = dataFieldDef;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result
				+ ((dataFieldDef == null) ? 0 : dataFieldDef.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result
				+ ((insertUser == null) ? 0 : insertUser.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WStepHead))
			return false;
		WStepHead other = (WStepHead) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (dataFieldDef == null) {
			if (other.dataFieldDef != null)
				return false;
		} else if (!dataFieldDef.equals(other.dataFieldDef))
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
		return true;
	}


	@Override
	public String toString() {
		return "WStepHead [" + (id != null ? "id=" + id + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (comments != null ? "comments=" + comments + ", " : "")
				+ (insertDate != null ? "insertDate=" + insertDate + ", " : "")
				+ (insertUser != null ? "insertUser=" + insertUser + ", " : "")
				+ (modDate != null ? "modDate=" + modDate + ", " : "")
				+ (modUser != null ? "modUser=" + modUser + ", " : "")
				+ (dataFieldDef != null ? "dataFieldDef=" + dataFieldDef : "")
				+ "]";
	}


	public boolean empty() {

		if (id!=null && ! id.equals(0)) return false;
		if (name!=null && ! "".equals(name)) return false;
		if (comments!=null && ! "".equals(comments)) return false;
		
		return true;
	}

	public List<WStepDataField> getStepDataFieldList() {

		if (dataFieldDef != null
				&& dataFieldDef.size() > 0) {

			List<WStepDataField> dfl = 
					new ArrayList<WStepDataField>(dataFieldDef.size()+1);
			
			dfl = new ArrayList<WStepDataField>(dataFieldDef);

			return dfl;
		}

		return null;

	}
	
	
}
