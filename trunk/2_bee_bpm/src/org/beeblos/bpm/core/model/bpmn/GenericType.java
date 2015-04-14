package org.beeblos.bpm.core.model.bpmn;

import org.joda.time.DateTime;

/**
 * Defines the Generic Type
 * 
 * @author dml 20150414
 *
 */
public class GenericType extends InitEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4915439836593940872L;
	
	public GenericType(){
		
	}
	
	public GenericType(Integer id, String name, String type, boolean active,
			boolean engineReq, boolean deleted, String comments,
			DateTime insertDate, Integer insertUser, DateTime modDate,
			Integer modUser) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.active = active;
		this.engineReq = engineReq;
		this.deleted = deleted;
		this.comments = comments;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.modDate = modDate;
		this.modUser = modUser;
	}

	@Override
	public String toString() {
		return "GenericType [getParamPrueba()=" + getParamPrueba() + ", getType()=" + getType() + ", isActive()="
				+ isActive() + ", isEngineReq()=" + isEngineReq() + ", isDeleted()=" + isDeleted() + ", getComments()="
				+ getComments() + ", getInsertDate()=" + getInsertDate() + ", getInsertUser()=" + getInsertUser()
				+ ", getModDate()=" + getModDate() + ", getModUser()=" + getModUser() + ", getId()=" + getId()
				+ ", getName()=" + getName() + ", getAllowedResponses()=" + getAllowedResponses() + ", toString()="
				+ super.toString() + ", getClass()=" + getClass() + "]";
	}

}
