package org.beeblos.bpm.core.model;

// Generated Nov 9, 2011 1:15:47 PM by Hibernate Tools 3.4.0.CR1

import org.joda.time.DateTime;

/**
 * relacion step-user: indica los usuarios que tienen asignados/asociados
 * permisos en tiempo de definición (WStepDef)
 * 
 * nes 20141029 - agregado implements WUserCol
 * 
 * 
 */
public class WStepUser implements java.io.Serializable, WUserCol {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private WStepDef step;
	private WUserDef user;
	private boolean admin;
	private Integer idObject;
	private String idObjectType;
	private Integer insertUser;
	private DateTime insertDate;

	public WStepUser() {
	}



	public WStepUser(boolean admin, Integer idObject, String idObjectType, 
			Integer insertUser, DateTime insertDate) {
		this.admin = admin;
		this.idObject = idObject;
		this.idObjectType = idObjectType;
		this.insertUser = insertUser;
		this.insertDate = insertDate;
	}



	public WStepDef getStep() {
		return step;
	}



	public void setStep(WStepDef step) {
		this.step = step;
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

	public DateTime getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(DateTime insertDate) {
		this.insertDate = insertDate;
	}

}
