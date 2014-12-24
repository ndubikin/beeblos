package org.beeblos.bpm.core.model;

import org.joda.time.DateTime;


/**
 * WUserRoleWork - users assigned to runtime roles for a process-work
 * runtime roles are 'dynamic' roles and their users are assigned at
 * runtime. Assigned users scope for the role and the process-work.
 * 
 * nes 20141215
 * 
 */
public class WUserRoleWork implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id; // autonumeric
	
	/**
	 * Related user
	 */
	private Integer idUser; 
	
	/**
	 * related role
	 */
	private Integer idRole;
	
	/**
	 * related process work
	 */
	private Integer idProcessWork;
	
	/**
	 * timestamps
	 */
	private DateTime insertDate;
	private Integer insertUser;
	private DateTime modDate;
	private Integer modUser;

	
	
	public WUserRoleWork() {
		super();
		// TODO Auto-generated constructor stub
	}


	public WUserRoleWork(Integer id, Integer idUser, Integer idRole,
			Integer idProcessWork) {
		this.id = id;
		this.idUser = idUser;
		this.idRole = idRole;
		this.idProcessWork = idProcessWork;
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
	 * @return the idUser
	 */
	public Integer getIdUser() {
		return idUser;
	}


	/**
	 * @param idUser the idUser to set
	 */
	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}


	/**
	 * @return the idRole
	 */
	public Integer getIdRole() {
		return idRole;
	}


	/**
	 * @param idRole the idRole to set
	 */
	public void setIdRole(Integer idRole) {
		this.idRole = idRole;
	}


	/**
	 * @return the idProcessWork
	 */
	public Integer getIdProcessWork() {
		return idProcessWork;
	}


	/**
	 * @param idProcessWork the idProcessWork to set
	 */
	public void setIdProcessWork(Integer idProcessWork) {
		this.idProcessWork = idProcessWork;
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


	public boolean empty() {

		if (id!=null && !id.equals(0)) return false;

		if (idUser!=null && !idUser.equals(0)) return false;
		if (idRole!=null && !idRole.equals(0)) return false;
		if (idProcessWork!=null && !idProcessWork.equals(0)) return false;

		return true;
	}
	

	public void nullateEmtpyObjects() {
	   
	}
	
    public void recoverEmtpyObjects() {

    }


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((idProcessWork == null) ? 0 : idProcessWork.hashCode());
		result = prime * result + ((idRole == null) ? 0 : idRole.hashCode());
		result = prime * result + ((idUser == null) ? 0 : idUser.hashCode());
		result = prime * result
				+ ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result
				+ ((insertUser == null) ? 0 : insertUser.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
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
		if (!(obj instanceof WUserRoleWork))
			return false;
		WUserRoleWork other = (WUserRoleWork) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idProcessWork == null) {
			if (other.idProcessWork != null)
				return false;
		} else if (!idProcessWork.equals(other.idProcessWork))
			return false;
		if (idRole == null) {
			if (other.idRole != null)
				return false;
		} else if (!idRole.equals(other.idRole))
			return false;
		if (idUser == null) {
			if (other.idUser != null)
				return false;
		} else if (!idUser.equals(other.idUser))
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
		return true;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WUserRoleWork ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (idUser != null ? "idUser=" + idUser + ", " : "")
				+ (idRole != null ? "idRole=" + idRole + ", " : "")
				+ (idProcessWork != null ? "idProcessWork=" + idProcessWork
						+ ", " : "")
				+ (insertDate != null ? "insertDate=" + insertDate + ", " : "")
				+ (insertUser != null ? "insertUser=" + insertUser + ", " : "")
				+ (modDate != null ? "modDate=" + modDate + ", " : "")
				+ (modUser != null ? "modUser=" + modUser : "") + "]";
	}
	
    
}
