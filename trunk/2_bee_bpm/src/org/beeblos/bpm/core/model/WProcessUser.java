package org.beeblos.bpm.core.model;

// Generated Nov 9, 2011 1:15:47 PM by Hibernate Tools 3.4.0.CR1

import org.joda.time.DateTime;

/**
 * relacion process-user: indica los usuarios que tienen asignados/asociados
 * permisos en tiempo de definición al proceso (WProcessDef)
 * 
 * nes 20141029 - agregado implements WUserCol
 * 
 * 
 */
public class WProcessUser implements java.io.Serializable, WUserCol {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private Integer idProcess;
	private WUserDef user;
	
	private boolean admin;
	private Integer idObject;
	private String idObjectType;
	
	private Integer insertUser;
	private DateTime insertDate;

	public WProcessUser() {
	}

	public WProcessUser(Integer id) {
		this.id = id;
	}



	public WProcessUser(boolean admin, Integer idObject, String idObjectType, 
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
	 * @see org.beeblos.bpm.core.model.WUserCol#getUser()
	 */
	@Override
	public WUserDef getUser() {
		return user;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#setUser(org.beeblos.bpm.core.model.WUserDef)
	 */
	@Override
	public void setUser(WUserDef user) {
		this.user = user;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#isAdmin()
	 */
	@Override
	public boolean isAdmin() {
		return admin;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#setAdmin(boolean)
	 */
	@Override
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#getIdObject()
	 */
	@Override
	public Integer getIdObject() {
		return idObject;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#setIdObject(java.lang.Integer)
	 */
	@Override
	public void setIdObject(Integer idObject) {
		this.idObject = idObject;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#getIdObjectType()
	 */
	@Override
	public String getIdObjectType() {
		return idObjectType;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#setIdObjectType(java.lang.String)
	 */
	@Override
	public void setIdObjectType(String idObjectType) {
		this.idObjectType = idObjectType;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#getInsertUser()
	 */
	@Override
	public Integer getInsertUser() {
		return this.insertUser;
	}

	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#setInsertUser(java.lang.Integer)
	 */
	@Override
	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}

	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#getInsertDate()
	 */
	@Override
	public DateTime getInsertDate() {
		return this.insertDate;
	}

	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#setInsertDate(org.joda.time.DateTime)
	 */
	@Override
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
		result = prime * result + ((idProcess == null) ? 0 : idProcess.hashCode());
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
		WProcessUser other = (WProcessUser) obj;
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
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WProcessUser [id=" + id + ", idProcess=" + idProcess + ", user=" + user + ", admin=" + admin
				+ ", idObject=" + idObject + ", idObjectType=" + idObjectType + ", insertUser=" + insertUser
				+ ", insertDate=" + insertDate + "]";
	}
	

}