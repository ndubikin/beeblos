
package org.beeblos.bpm.core.model;

// Generated Nov 9, 2011 1:15:47 PM by Hibernate Tools 3.4.0.CR1

import static com.sp.common.util.ConstantsCommon.EMPTY_OBJECT;

import org.joda.time.DateTime;

/**
 * WUserRole 
 * 
 * Relation User-Role object (table)
 * 
 * OJO EQUALS ES ESPECIAL - NO PUEDE COMPARAR OBJETOS.
 * 
 * Indicates the users belonging a role
 */
public class WUserRole implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private WUserDef user;
	private WRoleDef role;
	
	private boolean active;
	private Integer insertUser;
	private DateTime insertDate;

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
			DateTime insertDate) {
		this.user = user;
		this.role = role;
		this.active = active;
		this.insertUser = insertUser;
		this.insertDate = insertDate;
	}

	public WUserRole(boolean active, Integer insertUser, DateTime insertDate) {
		this.active = active;
		this.insertUser = insertUser;
		this.insertDate = insertDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/**
	 * nes 20141125 - OJO OJO QUE ESTO NO PUEDE USAR EL EQUALS DEFAULT ...
	 */
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
		
		// nes 20141125
		if (role == null) {
			if (other.role != null)
				return false;
		} else { 
			if ( role.getId()== null ) {
				if (other.role.getId()==null) {
					return false;
				}
			} else {
				if (!role.getId().equals(other.getId())) {
					return false;
				}
			}
		}
		
		// nes 20141125
		if (user == null) {
			if (other.user != null)
				return false;
		} else {
			if (user.getId()==null) {
				if (other.getId()!=null) {
					return false;
				}
			} else {
				if (!user.getId().equals(other.user.getId()))
					return false;
			}
		}
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
