package org.beeblos.bpm.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

//import static com.sp.common.util.ConstantsCommon.EMPTY_OBJECT;

// Generated Nov 9, 2011 1:15:47 PM by Hibernate Tools 3.4.0.CR1

/**
 * BeeBPM Role definitin class
 * 
 * OJO OJO HASHCODE NO PUEDE INCLUIR USERS RELATED!!!!!!!!!!!
 * 
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
	
	/**
	 * indicates this is a system role
	 * system role can't be deleted by users
	 * there are some mandatory roles must be exist in the system: ie: ORIGINATOR
	 * and PROCESS_ADMIN
	 * nes 20141016
	 */
	private boolean systemRole;
	
	/**
	 * idicates if the role is a predefined (static) role or it is a 
	 * runtime role.
	 * Runtime role indicates the users are assigned to role at runtime.
	 * The fact to assign the users at runtime implies same role (roleId-rolename)
	 * may have different users for different objects or process.
	 * At this time the runtime role will be related with a process work (WProcessWork):
	 * each runtime role there will be same for a WProcessWork and it will be change
	 * between different WProcessWork.
	 * This flag will be related with WUserRoleWork (w_user_role_work): if the role
	 * is a runtime role the WUserRoleWork will be the users belonging to this role
	 * for each WProcessWork using it.
	 * nes 20141206
	 *  
	 */
	private boolean runtimeRole;
	
	/**
	 * External method related with runtime role which must provide id user list to assign
	 * to this runtime role...
	 * nes 20141206
	 * 
	 */
	private Integer idExternalMethod;

	/**
	 * named query (hibernate) to execute and which must return user-id-list with users belonging to this
	 * runtime role form this instance
	 * If this field have valid date implies query and idExternalMethod must be null.
	 * Evaluation order: 1) idExternalMethod / 2) namedQuery / 3) query
	 * First not null implies ignoring others
	 * nes 20160311
	 */
	private String namedQuery; 

	/**
	 * query (select) which must return user-id-list with users belonging to this
	 * runtime role form this instance
	 * If this field have valid date implies namedQuery and idExternalMethod must be null.
	 * Evaluation order: 1) idExternalMethod / 2) namedQuery / 3) query
	 * First not null implies ignoring others
	 * nes 20160311
	 */
	private String query;

	
	/**
	 * Users belonging this role
	 * MANY2MANY
	 */
	Set<WUserRole> usersRelated=new HashSet<WUserRole>();

	/**
	 * timestamps
	 */
	private Integer insertUser;
	private DateTime insertDate;
	private Integer modUser;
	private DateTime modDate;

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

	/**
	 * @return the systemRole
	 */
	public boolean isSystemRole() {
		return systemRole;
	}

	/**
	 * @param systemRole the systemRole to set
	 */
	public void setSystemRole(boolean systemRole) {
		this.systemRole = systemRole;
	}

	
	
	/**
	 * idicates if the role is a predefined (static) role or it is a 
	 * runtime role.
	 * Runtime role indicates the users are assigned to role at runtime.
	 * The fact to assign the users at runtime implies same role (roleId-rolename)
	 * may have different users for different objects or process.
	 * At this time the runtime role will be related with a process work (WProcessWork):
	 * each runtime role there will be same for a WProcessWork and it will be change
	 * between different WProcessWork.
	 * This flag will be related with WUserRoleWork (w_user_role_work): if the role
	 * is a runtime role the WUserRoleWork will be the users belonging to this role
	 * for each WProcessWork using it.
	 * nes 20141206
	 *  
	 * @return the runtimeRole
	 */
	public boolean isRuntimeRole() {
		return runtimeRole;
	}

	/**
	 * idicates if the role is a predefined (static) role or it is a 
	 * runtime role.
	 * Runtime role indicates the users are assigned to role at runtime.
	 * The fact to assign the users at runtime implies same role (roleId-rolename)
	 * may have different users for different objects or process.
	 * At this time the runtime role will be related with a process work (WProcessWork):
	 * each runtime role there will be same for a WProcessWork and it will be change
	 * between different WProcessWork.
	 * This flag will be related with WUserRoleWork (w_user_role_work): if the role
	 * is a runtime role the WUserRoleWork will be the users belonging to this role
	 * for each WProcessWork using it.
	 * nes 20141206
	 *  
	 * @param runtimeRole the runtimeRole to set
	 */
	public void setRuntimeRole(boolean runtimeRole) {
		this.runtimeRole = runtimeRole;
	}

	/**
	 * External method related with runtime role which must provide id user list to assign
	 * to this runtime role...
	 * nes 20141206
	 * 
	 * @return the idExternalMethod
	 */
	public Integer getIdExternalMethod() {
		return idExternalMethod;
	}

	/**
	 * External method related with runtime role which must provide id user list to assign
	 * to this runtime role...
	 * nes 20141206
	 * 
	 * @param idExternalMethod the idExternalMethod to set
	 */
	public void setIdExternalMethod(Integer idExternalMethod) {
		this.idExternalMethod = idExternalMethod;
	}

	/**
	 * @return the namedQuery
	 */
	public String getNamedQuery() {
		return namedQuery;
	}

	/**
	 * @param namedQuery the namedQuery to set
	 */
	public void setNamedQuery(String namedQuery) {
		this.namedQuery = namedQuery;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	public Set<WUserRole> getUsersRelated() {
		return usersRelated;
	}

	public List<WUserRole> getUsersRelatedAsList() {
		if (usersRelated != null){
			return new ArrayList<WUserRole>(usersRelated);
		}
		return null;
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


	/**
	 * OJO OJO HASHCODE NO PUEDE INCLUIR USERS RELATED!!!!!!!!!!!
	 */
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((idExternalMethod == null) ? 0 : idExternalMethod.hashCode());
		result = prime * result
				+ ((idObject == null) ? 0 : idObject.hashCode());
		result = prime * result
				+ ((idObjectType == null) ? 0 : idObjectType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((namedQuery == null) ? 0 : namedQuery.hashCode());
		result = prime * result + ((query == null) ? 0 : query.hashCode());		
		result = prime * result + (runtimeRole ? 1231 : 1237);
		result = prime * result + (systemRole ? 1231 : 1237);

		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 2;
		return "WRoleDef ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (description != null ? "description=" + description + ", "
						: "")
				+ (idObject != null ? "idObject=" + idObject + ", " : "")
				+ (idObjectType != null ? "idObjectType=" + idObjectType + ", "
						: "")
				+ "systemRole="
				+ systemRole
				+ ", runtimeRole="
				+ runtimeRole
				+ ", "
				+ (idExternalMethod != null ? "idExternalMethod="
						+ idExternalMethod + ", " : "")
				+ (usersRelated != null ? "usersRelated="
						+ toString(usersRelated, maxLen) + ", " : "")
				+ (insertUser != null ? "insertUser=" + insertUser + ", " : "")
				+ (insertDate != null ? "insertDate=" + insertDate + ", " : "")
				+ (modUser != null ? "modUser=" + modUser + ", " : "")
				+ (modDate != null ? "modDate=" + modDate : "") + "]";
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
		if (!(obj instanceof WRoleDef))
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
		if (idExternalMethod == null) {
			if (other.idExternalMethod != null)
				return false;
		} else if (!idExternalMethod.equals(other.idExternalMethod))
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (namedQuery == null) {
			if (other.namedQuery != null)
				return false;
		} else if (!namedQuery.equals(other.namedQuery))
			return false;
		if (query == null) {
			if (other.query != null)
				return false;
		} else if (!query.equals(other.query))
			return false;
		if (runtimeRole != other.runtimeRole)
			return false;
		if (systemRole != other.systemRole)
			return false;
		if (usersRelated == null) {
			if (other.usersRelated != null)
				return false;
		} else if (!usersRelated.equals(other.usersRelated))
			return false;
		return true;
	}		
	

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	public boolean empty() {

		if (id!=null && ! id.equals(0)) return false;
		
		if (name!=null && ! "".equals(name)) return false;
		if (description!=null && ! "".equals(description)) return false;
		if (idObject!=null && ! idObject.equals(0)) return false;
		if (idObjectType!=null && ! "".equals(idObjectType)) return false;		
		
		//nes 20141206
		if (idExternalMethod!=null && ! idExternalMethod.equals(0)) return false;
		
		return true;
	}
/*
	// dml 20120508
	public void addUser( WUserDef user, boolean active, Integer insertUser ) {
		WUserRole wur = new WUserRole(active, insertUser, new DateTime());
		wur.setRole(this);
		wur.setUser(user);
		usersRelated.add(wur);
	}
*/	
}
