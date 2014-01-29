package org.beeblos.bpm.core.model;

// Generated Nov 9, 2011 1:15:47 PM by Hibernate Tools 3.4.0.CR1

import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;

import java.util.Date;

/**
 * WUserRole generated by hbm2java
 * 
 * Indicates the users belonging a role
 */
public class WUserRole implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private WUserDef user;
	private WRoleDef role;
	private boolean active;
	private Integer insertUser;
	private Date insertDate;

	public WUserRole() {
		super();
	}
	
	public WUserRole(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			this.user=new WUserDef( EMPTY_OBJECT );
			this.role = new WRoleDef( EMPTY_OBJECT );

		}	
	}

	public WUserRole(WUserDef user, WRoleDef role, boolean active, Integer insertUser,
			Date insertDate) {
		this.user = user;
		this.role = role;
		this.active = active;
		this.insertUser = insertUser;
		this.insertDate = insertDate;
	}

	public WUserRole(boolean active, Integer insertUser, Date insertDate) {
		this.active = active;
		this.insertUser = insertUser;
		this.insertDate = insertDate;
	}

	public WUserDef getUser() {
		return user;
	}

	public void setUser(WUserDef user) {
		this.user = user;
	}

	public WRoleDef getRole() {
		return role;
	}

	public void setRole(WRoleDef role) {
		this.role = role;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Integer getInsertUser() {
		return this.insertUser;
	}

	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}

	public Date getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
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
		WUserRole other = (WUserRole) obj;
		if (active != other.active)
			return false;
		return true;
	}

	public boolean empty() {

		if (user!=null && ! user.empty()) return false;
		if (role!=null && ! role.empty()) return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "WUserRole [" + "active="
				+ active + ", insertUser=" + insertUser + ", "
				+ (insertDate != null ? "insertDate=" + insertDate : "") + "]";
	}
	
	
}