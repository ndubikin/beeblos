package org.beeblos.bpm.core.model;

// Generated Oct 30, 2010 12:25:05 AM by Hibernate Tools 3.3.0.GA

import org.joda.time.DateTime;

/**
 * WProcessDef generated by hbm2java
 */
public class ObjectM implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String comments;

	/*
	 * DESIGN AND TEST TIME FEATURE
	 * At design time the designer can define a list of roles and users that may interact
	 * with the workflow. This info is only to effects to simplify and control the screen
	 * presentation for users and roles that can be assigned to a step or a task ...
	 * 
	 */
	
	private DateTime insertDate;
	private Integer insertUser;
	private DateTime modDate;
	private Integer modUser;

	public ObjectM() {
		super();
	}

	
	public ObjectM(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			//this.beginStep=new WStepDef( EMPTY_OBJECT );
			
		}	
	}
	
	public ObjectM(String name, String comments, WStepDef beginStep,
			DateTime fechaAlta, DateTime fechaModificacion) {
		this.name = name;
		this.comments = comments;
		//this.beginStep = beginStep;
		this.insertDate = fechaAlta;
		this.modDate = fechaModificacion;
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

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}






	public DateTime getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(DateTime insertDate) {
		this.insertDate = insertDate;
	}

	public Integer getInsertUser() {
		return insertUser;
	}

	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}

	public DateTime getModDate() {
		return modDate;
	}

	public void setModDate(DateTime modDate) {
		this.modDate = modDate;
	}

	public Integer getModUser() {
		return modUser;
	}

	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ObjectM))
			return false;
		ObjectM other = (ObjectM) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ObjectM [" + (id != null ? "id=" + id + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (comments != null ? "comments=" + comments + ", " : "")
				+ (insertDate != null ? "insertDate=" + insertDate + ", " : "")
				+ (insertUser != null ? "insertUser=" + insertUser + ", " : "")
				+ (modDate != null ? "modDate=" + modDate + ", " : "")
				+ (modUser != null ? "modUser=" + modUser : "") + "]";
	}
	
	// must implement method empty!!
	public boolean empty() {

		if (id!=null && ! id.equals(0)) return false;
		if (name!=null && ! "".equals(name)) return false;
		if (comments!=null && ! "".equals(comments)) return false;
		
//		if (beginStep!=null && ! beginStep.empty()) return false;
		
		return true;
	}
	
//	public void addRole( WRoleDef role, boolean admin, Integer idObject, String idObjectType, Integer insertUser ) {
//		WProcessRole wpr = new WProcessRole(admin, idObject, idObjectType, insertUser, new DateTime() );
//		wpr.setProcess(this);
//		wpr.setRole(role);
//		rolesRelated.add(wpr);
//	}
//	
//	public void addUser( WUserDef user, boolean admin, Integer idObject, String idObjectType, Integer insertUser ) {
//		WProcessUser wpu = new WProcessUser(admin, idObject, idObjectType, insertUser, new DateTime() );
//		wpu.setProcess(this);
//		wpu.setUser(user);
//		usersRelated.add(wpu);
//	}

}
