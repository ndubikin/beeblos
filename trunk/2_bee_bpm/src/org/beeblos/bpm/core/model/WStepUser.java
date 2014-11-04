package org.beeblos.bpm.core.model;

// Generated Nov 9, 2011 1:15:47 PM by Hibernate Tools 3.4.0.CR1

import org.joda.time.DateTime;

/**
 * relacion step-user: indica los usuarios que tienen asignados/asociados
 * permisos en tiempo de definición (WStepDef)
 * 
 * nes 20141029 - agregado implements WUserCol
 * 
 * 
 */
public class WStepUser implements java.io.Serializable, WUserCol {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private Integer idStep;
	private WUserDef user;
	
	private boolean admin;
	private Integer idObject;
	private String idObjectType;
	
	private Integer insertUser;
	private DateTime insertDate;

	public WStepUser() {
	}

	public WStepUser(boolean admin, Integer idObject, String idObjectType, 
			Integer insertUser, DateTime insertDate) {
		this.admin = admin;
		this.idObject = idObject;
		this.idObjectType = idObjectType;
		this.insertUser = insertUser;
		this.insertDate = insertDate;
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getIdStep() {
		return idStep;
	}



	public void setIdStep(Integer idStep) {
		this.idStep = idStep;
	}



	public WUserDef getUser() {
		return user;
	}



	public void setUser(WUserDef user) {
		this.user = user;
	}



	public boolean isAdmin() {
		return admin;
	}



	public void setAdmin(boolean admin) {
		this.admin = admin;
	}



	public Integer getIdObject() {
		return idObject;
	}



	public void setIdObject(Integer idObject) {
		this.idObject = idObject;
	}



	public String getIdObjectType() {
		return idObjectType;
	}



	public void setIdObjectType(String idObjectType) {
		this.idObjectType = idObjectType;
	}



	public Integer getInsertUser() {
		return this.insertUser;
	}

	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}

	public DateTime getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(DateTime insertDate) {
		this.insertDate = insertDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (admin ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idObject == null) ? 0 : idObject.hashCode());
		result = prime * result + ((idObjectType == null) ? 0 : idObjectType.hashCode());
		result = prime * result + ((idStep == null) ? 0 : idStep.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		WStepUser other = (WStepUser) obj;
		if (admin != other.admin)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idObject == null) {
			if (other.idObject != null)
				return false;
		} else if (!idObject.equals(other.idObject))
			return false;
		if (idObjectType == null) {
			if (other.idObjectType != null)
				return false;
		} else if (!idObjectType.equals(other.idObjectType))
			return false;
		if (idStep == null) {
			if (other.idStep != null)
				return false;
		} else if (!idStep.equals(other.idStep))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WStepUser [id=" + id + ", idStep=" + idStep + ", user=" + user + ", admin=" + admin + ", idObject="
				+ idObject + ", idObjectType=" + idObjectType + ", insertUser=" + insertUser + ", insertDate="
				+ insertDate + "]";
	}

}
