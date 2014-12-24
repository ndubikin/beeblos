package org.beeblos.bpm.core.model;

// Generated Nov 9, 2011 1:15:47 PM by Hibernate Tools 3.4.0.CR1

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

/**
 * Defines a User for BeeBPM
 * Indicates the users belonging a role
 * 
 * OJO OJO HASHCODE NO PUEDE INCLUIR USERS RELATED!!!!!!!!!!!
 * 
 * 
 */
public class WUserDef implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String login;
	private String email;
	private boolean active;
	private Integer insertUser;
	private DateTime insertDate;
	private Integer modUser;
	private DateTime modDate;

	// MANY2MANY
	Set<WUserRole> rolesRelated=new HashSet<WUserRole>();

	public WUserDef() {
		super();
	}
	
	public WUserDef(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
		}	
	}
	
	public WUserDef(Integer idUser) {
		this.id=idUser;
	}

	public WUserDef(String name, String login, boolean active, Integer insertUser,
			DateTime insertDate) {
		this.name=name;
		this.login=login;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Integer getInsertUser() {
		return insertUser;
	}

	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}

	public DateTime getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(DateTime insertDate) {
		this.insertDate = insertDate;
	}

	public Integer getModUser() {
		return modUser;
	}

	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}

	public DateTime getModDate() {
		return modDate;
	}

	public void setModDate(DateTime modDate) {
		this.modDate = modDate;
	}

	public Set<WUserRole> getRolesRelated() {
		return rolesRelated;
	}

	public List<WUserRole> getRolesRelatedAsList() {
		if (rolesRelated != null){
			return new ArrayList<WUserRole>(rolesRelated);
		}
		return null;
	}

	public void setRolesRelated(Set<WUserRole> rolesRelated) {
		this.rolesRelated = rolesRelated;
	}

	/**
	 * OJO OJO HASHCODE NO PUEDE INCLUIR ROLE RELATED!!!!!!!!!!!
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
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
		WUserDef other = (WUserDef) obj;
		if (active != other.active)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (rolesRelated == null) {
			if (other.rolesRelated != null)
				return false;
		} else if (!rolesRelated.equals(other.rolesRelated))
			return false;
		return true;
	}

	public boolean empty() {

		if (id!=null && ! id.equals(0)) return false;
		if (name!=null && ! "".equals(name)) return false;
		if (login!=null && ! "".equals(login)) return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return "WUserDef [" + (id != null ? "id=" + id + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (login != null ? "login=" + login + ", " : "")
				+ (email != null ? "email=" + email + ", " : "") + "active="
				+ active + ", insertUser=" + insertUser + ", "
				+ (insertDate != null ? "insertDate=" + insertDate + ", " : "")
				+ (modUser != null ? "modUser=" + modUser + ", " : "")
				+ (modDate != null ? "modDate=" + modDate + ", " : "")
				+ "]";
	}
/*
	// dml 20120508
	public void addRole( WRoleDef role, boolean active, Integer insertUser ) {
		WUserRole wur = new WUserRole(active, insertUser, new DateTime());
		wur.setUser(this);
		wur.setRole(role);
		rolesRelated.add(wur);
	}
*/
	
}
