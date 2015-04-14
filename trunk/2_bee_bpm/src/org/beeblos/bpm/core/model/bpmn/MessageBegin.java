package org.beeblos.bpm.core.model.bpmn;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

/**
 * Defines the step type Begin Message
 * 
 * @author dml 20150413
 *
 */
public class MessageBegin extends InitEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4915439836593940872L;
	
	/**
	 * Cuentas del demonio
	 */
	public Set<MessageBeginEmailDConf> emailDConfs;
	
	public MessageBegin(){
		
	}
	
	public MessageBegin(Integer id, String name, String type, boolean active,
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

	public Set<MessageBeginEmailDConf> getEmailDConfs() {
		return emailDConfs;
	}

	public List<MessageBeginEmailDConf> getEmailDConfsAsList() {
		if (emailDConfs != null){
			return new ArrayList<MessageBeginEmailDConf>(emailDConfs);
		}
		return null;
	}

	public void setEmailDConfs(Set<MessageBeginEmailDConf> emailDConfs) {
		this.emailDConfs = emailDConfs;
	}


	@Override
	public String toString() {
		return "MessageBegin [emailDConfs=" + emailDConfs + ", id=" + id + ", name=" + name + ", type="
				+ type + ", active=" + active + ", engineReq=" + engineReq + ", deleted=" + deleted + ", comments="
				+ comments + ", allowedResponses=" + allowedResponses + ", insertDate=" + insertDate + ", insertUser="
				+ insertUser + ", modDate=" + modDate + ", modUser=" + modUser + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result
				+ ((emailDConfs == null) ? 0 : emailDConfs.hashCode());
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + (engineReq ? 1231 : 1237);
		result = prime * result
				+ ((allowedResponses == null) ? 0 : allowedResponses.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result
				+ ((insertUser == null) ? 0 : insertUser.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
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
		if (!(obj instanceof MessageBegin))
			return false;
		MessageBegin other = (MessageBegin) obj;
		if (active != other.active)
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (emailDConfs == null) {
			if (other.emailDConfs != null)
				return false;
		} else if (!emailDConfs.equals(other.emailDConfs))
			return false;
		if (deleted != other.deleted)
			return false;
		if (engineReq != other.engineReq)
			return false;
		if (allowedResponses == null) {
			if (other.allowedResponses != null)
				return false;
		} else if (!allowedResponses.equals(other.allowedResponses))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	/**
	 * returns true if current step type is an empty object...
	 * @return
	 */
	public boolean empty() {

		if (id!=null && id!=0) return false;
		if (name!=null && !"".equals(name)) return false;
		if (type!=null && !"".equals(type)) return false;
		if (comments!=null && !"".equals(comments)) return false;
		return true;
	}
	/**
	 * 
	 * nullates empty objects to persist
	 */
	public void nullateEmtpyObjects() {

	}
	
    /**
     * 
     * recover empty objects to persist
     */
    public void recoverEmtpyObjects() {

    	
    }
}
