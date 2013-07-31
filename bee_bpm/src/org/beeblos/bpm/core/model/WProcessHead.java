package org.beeblos.bpm.core.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.beeblos.bpm.tm.model.Table;

public class WProcessHead implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String name;	
	private String comments;
	
	private WProcessHeadManagedData managedTable; // data fields table
	
	Set<WProcessHeadManagedData> processDataFieldDef = new HashSet<WProcessHeadManagedData>(0);
	
	private Date insertDate;
	private Integer insertUser;
	private Date modDate;
	private Integer modUser;
	
	public WProcessHead() {
		super();
	}

	
	public WProcessHead(Integer id) {
		this.id = id;
	}

	public WProcessHead(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public WProcessHead(String name, String comments, 
			Date fechaAlta, Date fechaModificacion) {
		this.name = name;
		this.comments = comments;
		this.insertDate = fechaAlta;
		this.modDate = fechaModificacion;
	}

	public WProcessHead(String name, String comments,
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

	public WProcessHeadManagedData getManagedTable() {
		return managedTable;
	}


	public void setManagedTable(WProcessHeadManagedData managedTable) {
		this.managedTable = managedTable;
	}


	public Set<WProcessHeadManagedData> getProcessDataFieldDef() {
		return processDataFieldDef;
	}


	public void setProcessDataFieldDef(
			Set<WProcessHeadManagedData> processDataFieldDef) {
		this.processDataFieldDef = processDataFieldDef;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result + ((insertUser == null) ? 0 : insertUser.hashCode());
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
		if (getClass() != obj.getClass())
			return false;
		WProcessHead other = (WProcessHead) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
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
		return "WProcessHead [id=" + id + ", name=" + name + ", comments=" + comments + ", insertDate="
				+ insertDate + ", insertUser=" + insertUser + ", modDate=" + modDate + ", modUser="
				+ modUser + "]";
	}


	public boolean empty() {

		if (id!=null && ! id.equals(0)) return false;
		if (name!=null && ! "".equals(name)) return false;
		if (comments!=null && ! "".equals(comments)) return false;
		
		return true;
	}
	
}
