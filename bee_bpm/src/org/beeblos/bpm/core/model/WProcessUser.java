package org.beeblos.bpm.core.model;

// Generated Nov 9, 2011 1:15:47 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * WProcessUser generated by hbm2java
 */
public class WProcessUser implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private WProcessDef process;
	private WUserDef user;
	private boolean admin;
	private Integer idObject;
	private String idObjectType;
	private Integer insertUser;
	private Date insertDate;

	public WProcessUser() {
	}



	public WProcessUser(boolean admin, Integer idObject, String idObjectType, 
			Integer insertUser, Date insertDate) {
		this.admin = admin;
		this.idObject = idObject;
		this.idObjectType = idObjectType;
		this.insertUser = insertUser;
		this.insertDate = insertDate;
	}




	public WProcessDef getProcess() {
		return process;
	}



	public void setProcess(WProcessDef process) {
		this.process = process;
	}



	public WUserDef getUser() {
		return user;
	}



	public void setUser(WUserDef user) {
		this.user = user;
	}



	public boolean isAdmin() {
		return admin;
	}



	public void setAdmin(boolean admin) {
		this.admin = admin;
	}



	public Integer getIdObject() {
		return idObject;
	}



	public void setIdObject(Integer idObject) {
		this.idObject = idObject;
	}



	public String getIdObjectType() {
		return idObjectType;
	}



	public void setIdObjectType(String idObjectType) {
		this.idObjectType = idObjectType;
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

}
