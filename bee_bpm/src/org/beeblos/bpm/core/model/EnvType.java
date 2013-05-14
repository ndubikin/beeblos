package org.beeblos.bpm.core.model;

import java.util.Date;

import org.hibernate.type.descriptor.sql.TinyIntTypeDescriptor;


public class EnvType implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private Short id;

	private String name;

	private String color;

	private Integer addUser;
	private Date addDate;
	private Integer modUser;
	private Date modDate;
 
	public EnvType() {

	}

	public EnvType(Short id, String name, String color) {
		super();
		this.id = id;
		this.name = name;
		this.color = color;
	}

	public EnvType(Short id, String name, String color, Integer addUser, Date addDate,
			Integer modUser, Date modDate) {
		super();
		this.id = id;
		this.name = name;
		this.color = color;
		this.addUser = addUser;
		this.addDate = addDate;
		this.modUser = modUser;
		this.modDate = modDate;
	}

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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
		return "EnvType [id=" + id + ", name=" + name + ", color=" + color + ", addUser=" + addUser
				+ ", addDate=" + addDate + ", modUser=" + modUser + ", modDate=" + modDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addDate == null) ? 0 : addDate.hashCode());
		result = prime * result + ((addUser == null) ? 0 : addUser.hashCode());
		result = prime * result + ((color == null) ? 0 : color.hashCode());
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
		EnvType other = (EnvType) obj;
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
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
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
				
		if (id != null && id.intValue() != 0) return false;
		if (name != null && !name.equals("")) return false;
		if (color != null && !color.equals("")) return false;
		
		return true;
		
	}
	
}