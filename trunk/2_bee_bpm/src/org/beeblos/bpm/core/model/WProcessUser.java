package org.beeblos.bpm.core.model;

// Generated Nov 9, 2011 1:15:47 PM by Hibernate Tools 3.4.0.CR1

import org.joda.time.DateTime;

/**
 * relacion process-user: indica los usuarios que tienen asignados/asociados
 * permisos en tiempo de definición al proceso (WProcessDef)
 * 
 * nes 20141029 - agregado implements WUserCol
 * 
 * 
 */
public class WProcessUser implements java.io.Serializable, WUserCol {

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
	private DateTime insertDate;

	public WProcessUser() {
	}



	public WProcessUser(boolean admin, Integer idObject, String idObjectType, 
			Integer insertUser, DateTime insertDate) {
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



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#getUser()
	 */
	@Override
	public WUserDef getUser() {
		return user;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#setUser(org.beeblos.bpm.core.model.WUserDef)
	 */
	@Override
	public void setUser(WUserDef user) {
		this.user = user;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#isAdmin()
	 */
	@Override
	public boolean isAdmin() {
		return admin;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#setAdmin(boolean)
	 */
	@Override
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#getIdObject()
	 */
	@Override
	public Integer getIdObject() {
		return idObject;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#setIdObject(java.lang.Integer)
	 */
	@Override
	public void setIdObject(Integer idObject) {
		this.idObject = idObject;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#getIdObjectType()
	 */
	@Override
	public String getIdObjectType() {
		return idObjectType;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#setIdObjectType(java.lang.String)
	 */
	@Override
	public void setIdObjectType(String idObjectType) {
		this.idObjectType = idObjectType;
	}



	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#getInsertUser()
	 */
	@Override
	public Integer getInsertUser() {
		return this.insertUser;
	}

	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#setInsertUser(java.lang.Integer)
	 */
	@Override
	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}

	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#getInsertDate()
	 */
	@Override
	public DateTime getInsertDate() {
		return this.insertDate;
	}

	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.model.WUserCol#setInsertDate(org.joda.time.DateTime)
	 */
	@Override
	public void setInsertDate(DateTime insertDate) {
		this.insertDate = insertDate;
	}

}
