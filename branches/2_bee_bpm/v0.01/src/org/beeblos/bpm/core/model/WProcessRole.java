package org.beeblos.bpm.core.model;

// Generated Nov 9, 2011 1:15:47 PM by Hibernate Tools 3.4.0.CR1

import org.joda.time.DateTime;

/**
 * Relacion process-role: indica los roles que tienen asignados/asociados
 * permisos en tiempo de definición al proceso (WProcessDef)
 * 
 * Los roles se asignan en tiempo de definición pero a estos roles se les
 * puede asignar usuarios en runtime. Esos roles tendrán la propiedad runtimeRole
 * a true.
 * nes 20141206
 * 
 */
public class WProcessRole implements java.io.Serializable, WRoleCol {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Autonumeric id to manage relation manually 
	 */
	private Integer id;
	
	/**
	 * WProcessDef id
	 */
	private Integer idProcess;
	/**
	 * Related role assigned to wProcessDef
	 */
	private WRoleDef role;
	
	private boolean admin;
	private Integer idObject;
	private String idObjectType;
	private Integer insertUser;
	private DateTime insertDate;

	public WProcessRole() {
	}

	public WProcessRole(boolean admin, Integer idObject, String idObjectType, 
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



	public Integer getIdProcess() {
		return idProcess;
	}



	public void setIdProcess(Integer idProcess) {
		this.idProcess = idProcess;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WRoleCol#getRole()
	 */
	@Override
	public WRoleDef getRole() {
		return role;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WRoleCol#setRole(org.beeblos.bpm.core.model.WRoleDef)
	 */
	@Override
	public void setRole(WRoleDef role) {
		this.role = role;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WRoleCol#isAdmin()
	 */
	@Override
	public boolean isAdmin() {
		return admin;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WRoleCol#setAdmin(boolean)
	 */
	@Override
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WRoleCol#getIdObject()
	 */
	@Override
	public Integer getIdObject() {
		return idObject;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WRoleCol#setIdObject(java.lang.Integer)
	 */
	@Override
	public void setIdObject(Integer idObject) {
		this.idObject = idObject;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WRoleCol#getIdObjectType()
	 */
	@Override
	public String getIdObjectType() {
		return idObjectType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (admin ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idObject == null) ? 0 : idObject.hashCode());
		result = prime * result + ((idObjectType == null) ? 0 : idObjectType.hashCode());
		result = prime * result + ((idProcess == null) ? 0 : idProcess.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		WProcessRole other = (WProcessRole) obj;
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
		if (idProcess == null) {
			if (other.idProcess != null)
				return false;
		} else if (!idProcess.equals(other.idProcess))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WRoleCol#setIdObjectType(java.lang.String)
	 */
	@Override
	public void setIdObjectType(String idObjectType) {
		this.idObjectType = idObjectType;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WRoleCol#getInsertUser()
	 */
	@Override
	public Integer getInsertUser() {
		return this.insertUser;
	}

	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WRoleCol#setInsertUser(java.lang.Integer)
	 */
	@Override
	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}

	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WRoleCol#getInsertDate()
	 */
	@Override
	public DateTime getInsertDate() {
		return this.insertDate;
	}

	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WRoleCol#setInsertDate(org.joda.time.DateTime)
	 */
	@Override
	public void setInsertDate(DateTime insertDate) {
		this.insertDate = insertDate;
	}

	@Override
	public String toString() {
		return "WProcessRole [id=" + id + ", idProcess=" + idProcess + ", role=" + role + ", admin=" + admin
				+ ", idObject=" + idObject + ", idObjectType=" + idObjectType + ", insertUser=" + insertUser
				+ ", insertDate=" + insertDate + "]";
	}


}