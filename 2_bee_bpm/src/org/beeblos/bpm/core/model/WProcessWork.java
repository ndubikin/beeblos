package org.beeblos.bpm.core.model;

// Generated Jan 20, 2012 7:08:40 PM by Hibernate Tools 3.4.0.CR1

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.beeblos.bpm.core.model.noper.WProcessDefThin;
import org.joda.time.DateTime;


/**
 * WProcessWork will represent an instance of a process.
 * Each processWork joins many wStepWork (tasks)
 * A process work refers to a WProcessDef which defines the 
 * process, their steps (or tasks) routes, timers, users,
 * roles, etc.
 * 
 * @author nes
 *
 */
public class WProcessWork implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * process work id (instance id)
	 */
	private Integer id;

	/**
	 * process work instance status from WProcessSatus table
	 */
	private WProcessStatus status;

	/**
	 * Process definition data for this process
	 * 
	 */	
	private WProcessDefThin processDef;
//	@Deprecated
//	private int version;
	
	/**
	 * DateTime at this instance was created (injected / started)
	 */
	private DateTime startingTime;
	/**
	 * TODO: VER PA Q PUSE ESTO ...
	 */
	private Integer startingType;
	
	/**
	 * DateTime at this instance of process is finished.
	 * There is mandatory a finished process work will have all their
	 * related step works finished too.
	 * 
	 */
	private DateTime endTime;
	
	private String reference;
	private String comments;
	
	/**
	 * timestamps
	 */
	private Integer insertUser;
	private DateTime insertDate;
	private Integer modUser;
	private DateTime modDate;
	
	/**
	 * related object referred by this instance
	 */
	private Integer idObject;
	/**
	 * related object type (java class) referred by this instance
	 */
	private String idObjectType;
	
	/**
	 * instance users belonging to runtime roles defined for this process
	 * nes 20160317
	 */
	private Set<WUserRoleWork> rtrUser = new HashSet<WUserRoleWork>();

	public WProcessWork() {
		super();
	}
	
	public WProcessWork(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			this.status=new WProcessStatus();
			
		}	
	}

	public WProcessWork(WProcessStatus status, WProcessDefThin processDefLigth,
			DateTime startingTime, String reference, Integer insertUser,
			DateTime insertDate, Integer modUser, DateTime modDate) {
		this.status = status;
		this.processDef = processDefLigth;
		this.startingTime = startingTime;
		this.reference = reference;
		this.insertUser = insertUser;
		this.insertDate = insertDate;
		this.modUser = modUser;
		this.modDate = modDate;
	}

	public WProcessWork(WProcessStatus status, WProcessDefThin processDefLigth,
			DateTime startingTime, Integer startingType, DateTime endTime,
			String reference, String comments, Integer insertUser, DateTime insertDate,
			Integer modUser, DateTime modDate, Integer idObject,
			String idObjectType) {
		this.status = status;
		this.processDef = processDefLigth;
		this.startingTime = startingTime;
		this.startingType = startingType;
		this.endTime = endTime;
		this.reference = reference;
		this.comments = comments;
		this.insertUser = insertUser;
		this.insertDate = insertDate;
		this.modUser = modUser;
		this.modDate = modDate;
		this.idObject = idObject;
		this.idObjectType = idObjectType;
	}
	
	/**
	 * convenience method to symplify access to managed table configuration
	 * @return
	 */
	public WProcessHeadManagedDataConfiguration getManagedTableConfiguration() {
		return processDef.getProcessHead().getManagedTableConfiguration();
	}

	/**
	 * convenience method to symplify access to processHead id
	 * @return
	 */
	public Integer getProcessHeadId() {
		return processDef.getProcessHead().getId();
	}
	/**
	 * convenience method to return process name (stored in processHead)
	 * nes 20160527
	 * @return
	 */
	public String getShortName() {
		return this.processDef.getProcessHead().getShortName();
	}

	/**
	 * convenience method to return process name (stored in processHead)
	 * @return
	 */
	public String getName() {
		return this.processDef.getProcessHead().getName();
	}

	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public WProcessStatus getStatus() {
		return status;
	}

	public void setStatus(WProcessStatus status) {
		this.status = status;
	}

	public WProcessDefThin getProcessDef() {
		return processDef;
	}

	public void setProcessDef(WProcessDefThin processDef) {
		this.processDef = processDef;
	}

	public DateTime getStartingTime() {
		return this.startingTime;
	}

	public void setStartingTime(DateTime startingTime) {
		this.startingTime = startingTime;
	}

	public Integer getStartingType() {
		return this.startingType;
	}

	public void setStartingType(Integer startingType) {
		this.startingType = startingType;
	}

	public DateTime getEndTime() {
		return this.endTime;
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public Integer getModUser() {
		return this.modUser;
	}

	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}

	public DateTime getModDate() {
		return this.modDate;
	}

	public void setModDate(DateTime modDate) {
		this.modDate = modDate;
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

	/**
	 * @return the rtrUser
	 */
	public Set<WUserRoleWork> getRtrUser() {
		return rtrUser;
	}

	/**
	 * @param rtrUser the rtrUser to set
	 */
	public void setRtrUser(Set<WUserRoleWork> rtrUser) {
		this.rtrUser = rtrUser;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 2;
		return "WProcessWork ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (status != null ? "status=" + status + ", " : "")
				+ (processDef != null ? "processDef=" + processDef + ", " : "")
				+ (startingTime != null ? "startingTime=" + startingTime + ", "
						: "")
				+ (startingType != null ? "startingType=" + startingType + ", "
						: "")
				+ (endTime != null ? "endTime=" + endTime + ", " : "")
				+ (reference != null ? "reference=" + reference + ", " : "")
				+ (comments != null ? "comments=" + comments + ", " : "")
				+ (insertUser != null ? "insertUser=" + insertUser + ", " : "")
				+ (insertDate != null ? "insertDate=" + insertDate + ", " : "")
				+ (modUser != null ? "modUser=" + modUser + ", " : "")
				+ (modDate != null ? "modDate=" + modDate + ", " : "")
				+ (idObject != null ? "idObject=" + idObject + ", " : "")
				+ (idObjectType != null ? "idObjectType=" + idObjectType + ", "
						: "")
				+ (rtrUser != null ? "rtrUser=" + toString(rtrUser, maxLen)
						: "") + "]";
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((idObject == null) ? 0 : idObject.hashCode());
		result = prime * result
				+ ((idObjectType == null) ? 0 : idObjectType.hashCode());
		result = prime * result
				+ ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result
				+ ((insertUser == null) ? 0 : insertUser.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
		result = prime * result
				+ ((processDef == null) ? 0 : processDef.hashCode());
		result = prime * result
				+ ((reference == null) ? 0 : reference.hashCode());
		result = prime * result + ((rtrUser == null) ? 0 : rtrUser.hashCode());
		result = prime * result
				+ ((startingTime == null) ? 0 : startingTime.hashCode());
		result = prime * result
				+ ((startingType == null) ? 0 : startingType.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		if (!(obj instanceof WProcessWork))
			return false;
		WProcessWork other = (WProcessWork) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
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
		if (processDef == null) {
			if (other.processDef != null)
				return false;
		} else if (!processDef.equals(other.processDef))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (rtrUser == null) {
			if (other.rtrUser != null)
				return false;
		} else if (!rtrUser.equals(other.rtrUser))
			return false;
		if (startingTime == null) {
			if (other.startingTime != null)
				return false;
		} else if (!startingTime.equals(other.startingTime))
			return false;
		if (startingType == null) {
			if (other.startingType != null)
				return false;
		} else if (!startingType.equals(other.startingType))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	public boolean empty() {

		if (id!=null && ! id.equals(0)) return false;
//		if (version != 0) return false;
		if (status!=null && ! status.empty()) return false;		
		if (reference!=null && ! "".equals(reference)) return false;		
		if (comments!=null && ! "".equals(comments)) return false;
		if (idObject!=null && ! idObject.equals(0)) return false;
		if (idObjectType!=null && ! "".equals(idObjectType)) return false;

		return true;
	}

}
