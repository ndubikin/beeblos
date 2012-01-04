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
	
	private Integer id;
	private WUseridmapper user;
	private WRoleDef role;
	private boolean active;
	private int insertUser;
	private Date insertDate;

	public WUserRole() {
		super();
	}
	
	public WUserRole(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			this.user=new WUseridmapper( EMPTY_OBJECT );
			this.role = new WRoleDef( EMPTY_OBJECT );

		}	
	}

	public WUserRole(WUseridmapper user, WRoleDef role, boolean active, int insertUser,
			Date insertDate) {
		this.user = user;
		this.role = role;
		this.active = active;
		this.insertUser = insertUser;
		this.insertDate = insertDate;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public WUseridmapper getUser() {
		return user;
	}

	public void setUser(WUseridmapper user) {
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

	public int getInsertUser() {
		return this.insertUser;
	}

	public void setInsertUser(int insertUser) {
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
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WUserRole))
			return false;
		WUserRole other = (WUserRole) obj;
		if (active != other.active)
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
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
		return "WUserRole [" + (id != null ? "id=" + id + ", " : "")
				+ (user != null ? "user=" + user + ", " : "")
				+ (role != null ? "role=" + role + ", " : "") + "active="
				+ active + ", insertUser=" + insertUser + ", "
				+ (insertDate != null ? "insertDate=" + insertDate : "") + "]";
	}

	public boolean empty() {

		if (id!=null && ! id.equals(0)) return false;
		if (user!=null && ! user.empty()) return false;
		if (role!=null && ! role.empty()) return false;
		
		return true;
	}
	
	
}