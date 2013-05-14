package org.beeblos.bpm.core.model;

import java.util.Date;


public class Environment implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private EnvType envType;
	
	private String name;
	private String comments;

	private Integer addUser;
	private Date addDate;
	private Integer modUser;
	private Date modDate;
 
	public Environment() {

	}

	public Environment(boolean createEmtpyObjects ){
		super();
		
		if ( createEmtpyObjects ) {
			this.envType = new EnvType();
		}
		
	}

	public Environment(Integer id, EnvType envType, String name, String comments) {
		super();
		this.id = id;
		this.envType = envType;
		this.name = name;
		this.comments = comments;
	}

	public Environment(Integer id, EnvType envType, String name, String comments, Integer addUser,
			Date addDate, Integer modUser, Date modDate) {
		super();
		this.id = id;
		this.envType = envType;
		this.name = name;
		this.comments = comments;
		this.addUser = addUser;
		this.addDate = addDate;
		this.modUser = modUser;
		this.modDate = modDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EnvType getEnvType() {
		return envType;
	}

	public void setEnvType(EnvType envType) {
		this.envType = envType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getAddUser() {
		return addUser;
	}

	public void setAddUser(Integer addUser) {
		this.addUser = addUser;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public Integer getModUser() {
		return modUser;
	}

	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}

	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	@Override
	public String toString() {
		return "Environment [id=" + id + ", envType=" + envType + ", name=" + name + ", comments="
				+ comments + ", addUser=" + addUser + ", addDate=" + addDate + ", modUser="
				+ modUser + ", modDate=" + modDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addDate == null) ? 0 : addDate.hashCode());
		result = prime * result + ((addUser == null) ? 0 : addUser.hashCode());
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((envType == null) ? 0 : envType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Environment other = (Environment) obj;
		if (addDate == null) {
			if (other.addDate != null)
				return false;
		} else if (!addDate.equals(other.addDate))
			return false;
		if (addUser == null) {
			if (other.addUser != null)
				return false;
		} else if (!addUser.equals(other.addUser))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (envType == null) {
			if (other.envType != null)
				return false;
		} else if (!envType.equals(other.envType))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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

	public boolean empty(){
				
		if (id != null && !id.equals(0)) return false;
		if (envType != null & !envType.empty()) return false;
		if (name != null && !name.equals("")) return false;
		if (comments != null && !comments.equals("")) return false;
		
		return false;
		
	}
	
}