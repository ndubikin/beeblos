package org.beeblos.bpm.core.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

//import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;

// Generated Nov 9, 2011 1:15:47 PM by Hibernate Tools 3.4.0.CR1

/**
 * WRoleDef generated by hbm2java
 */
public class WRoleDef implements java.io.Serializable {

	/**
	 * Definition of a role: name, desctiption and eventually an objectType related 
	 * and their id 
	 * 
	 * To know wich users are related with a role you must see w_step_role object ...
	 * w_step_role represents the relation table and if you ask for an user you can
	 * obtain the list of roles belonging to
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String description;
	private Integer idObject;
	private String idObjectType;
	
	// MANY2MANY
	Set<WUserRole> usersRelated=new HashSet<WUserRole>();

	private Integer insertUser;
	private Date insertDate;
	private Integer modUser;
	private Date modDate;

	public WRoleDef() {
		super();
	}

	public WRoleDef(boolean createEmtpyObjects ){
		super();
//		if ( createEmtpyObjects ) {
			// no objects to create for now ...
//		}	
	}
	
	public WRoleDef(Integer id) {
		this.id = id;
	}

	public WRoleDef(String name) {
		this.name = name;
	}

	public WRoleDef(String name, String description, Integer idObject,
			String idObjectType) {
		this.name = name;
		this.description = description;
		this.idObject = idObject;
		this.idObjectType = idObjectType;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIdObject() {
		return this.idObject;
	}

	public void setIdObject(Integer idObject) {
		this.idObject = idObject;
	}

	public String getIdObjectType() {
		return this.idObjectType;
	}

	public void setIdObjectType(String idObjectType) {
		this.idObjectType = idObjectType;
	}


	public Set<WUserRole> getUsersRelated() {
		return usersRelated;
	}

	public void setUsersRelated(Set<WUserRole> usersRelated) {
		this.usersRelated = usersRelated;
	}

	public Integer getInsertUser() {
		return insertUser;
	}

	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idObject == null) ? 0 : idObject.hashCode());
		result = prime * result + ((idObjectType == null) ? 0 : idObjectType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "WRoleDef ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (description != null ? "description=" + description + ", "
						: "")
				+ (idObject != null ? "idObject=" + idObject + ", " : "")
				+ (idObjectType != null ? "idObjectType=" + idObjectType + ", "
						: "")
				+ (insertUser != null ? "insertUser=" + insertUser + ", " : "")
				+ (insertDate != null ? "insertDate=" + insertDate + ", " : "")
				+ (modUser != null ? "modUser=" + modUser + ", " : "")
				+ (modDate != null ? "modDate=" + modDate : "") + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WRoleDef other = (WRoleDef) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}		
	

	public boolean empty() {

		if (id!=null && ! id.equals(0)) return false;
		
		if (name!=null && ! "".equals(name)) return false;
		if (description!=null && ! "".equals(description)) return false;
		if (idObject!=null && ! idObject.equals(0)) return false;
		if (idObjectType!=null && ! "".equals(idObjectType)) return false;		
		
		return true;
	}

	// dml 20120508
	public void addUser( WUserDef user, boolean active, Integer insertUser ) {
		WUserRole wur = new WUserRole(active, insertUser, new Date());
		wur.setRole(this);
		wur.setUser(user);
		usersRelated.add(wur);
	}
	
}