package org.beeblos.bpm.core.model;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

/**
 * Defines the step type. IE: task, message, begin, end, etc
 * Step types must match with valid objects of BPMN event, activity or gateway 
 * 
 * @author pab
 *
 */
@XmlRootElement
public class WStepTypeDef implements Serializable {

	protected static final long serialVersionUID = 1L;
	
	protected Integer id;
	
	/**
	 * name of step type
	 */
	protected String name;
	
	/**
	 * kind of step type
	 */
	protected String type;
	
	/**
	 * indicates there is an active or valid step type or an old or
	 * inactive step type
	 */
	protected boolean active;
	/**
	 * indicates this kind of step requires beeBPM workflow engine
	 * to work / support
	 */
	protected boolean engineReq;
	
	/**
	 * Indicates a deleted step type (to database cross reference
	 * support)
	 */
	protected boolean deleted;
	/**
	 * comments related with this step type
	 */
	protected String comments;
	
	/**
	 * indicates this kind of step allows to define responses to use
	 * defalut step processors
	 */
	protected Boolean allowedResponses;
	
	/**
	 * This value will have the class type of the "WStepTypeDef" (e.g. MessageBegin, Timer, ...) 
	 */
	protected String relatedClass;

	// timestamps
	protected DateTime insertDate;
	protected Integer insertUser;
	protected DateTime modDate;
	protected Integer modUser;
	

	public WStepTypeDef() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WStepTypeDef(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public WStepTypeDef(Integer id, String name, String type, boolean active,
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

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the engineReq
	 */
	public boolean isEngineReq() {
		return engineReq;
	}

	/**
	 * @param engineReq the engineReq to set
	 */
	public void setEngineReq(boolean engineReq) {
		this.engineReq = engineReq;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the insertDate
	 */
	public DateTime getInsertDate() {
		return insertDate;
	}

	/**
	 * @param insertDate the insertDate to set
	 */
	public void setInsertDate(DateTime insertDate) {
		this.insertDate = insertDate;
	}

	/**
	 * @return the insertUser
	 */
	public Integer getInsertUser() {
		return insertUser;
	}

	/**
	 * @param insertUser the insertUser to set
	 */
	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}

	/**
	 * @return the modDate
	 */
	public DateTime getModDate() {
		return modDate;
	}

	/**
	 * @param modDate the modDate to set
	 */
	public void setModDate(DateTime modDate) {
		this.modDate = modDate;
	}

	/**
	 * @return the modUser
	 */
	public Integer getModUser() {
		return modUser;
	}

	/**
	 * @param modUser the modUser to set
	 */
	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}


	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * indicates this kind of step allows to define responses to use
	 * defalut step processors
	 * 
	 * @return the allowedResponses
	 */
	public Boolean getAllowedResponses() {
		return allowedResponses;
	}

	/**
	 * @param allowedResponses the allowedResponses to set
	 */
	public void setAllowedResponses(Boolean allowedResponses) {
		this.allowedResponses = allowedResponses;
	}

	public String getRelatedClass() {
		return relatedClass;
	}

	public void setRelatedClass(String relatedClass) {
		this.relatedClass = relatedClass;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WStepTypeDef [" + (id != null ? "id=" + id + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (type != null ? "type=" + type + ", " : "") + "active="
				+ active + ", engineReq=" + engineReq + ", deleted=" + deleted
				+ ", "
				+ (comments != null ? "comments=" + comments + ", " : "")
				+ (insertDate != null ? "insertDate=" + insertDate + ", " : "")
				+ (insertUser != null ? "insertUser=" + insertUser + ", " : "")
				+ (modDate != null ? "modDate=" + modDate + ", " : "")
				+ (modUser != null ? "modUser=" + modUser + ", " : "")
				+ (allowedResponses != null ? "allowedResponses=" + allowedResponses : "")
				+ (relatedClass != null ? "relatedClass=" + relatedClass : "")
				+ "]";
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
		result = prime * result + ((relatedClass == null) ? 0 : relatedClass.hashCode());
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
		if (!(obj instanceof WStepTypeDef))
			return false;
		WStepTypeDef other = (WStepTypeDef) obj;
		if (active != other.active)
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
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
		if (relatedClass == null) {
			if (other.relatedClass != null)
				return false;
		} else if (!relatedClass.equals(other.relatedClass))
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

	/**
	 * Returns this object into XML format (into a String)
	 * 
	 * @return
	 */
	public String marshal()  {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(WStepTypeDef.class);
			Marshaller marshaller = context.createMarshaller();
			StringWriter stringWriter = new StringWriter();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(this, stringWriter);
			return stringWriter.toString();			
		} catch (JAXBException e) {
			String mess = "Error marshalling WStepTypeDef "
					+e.getMessage()+(e.getCause()!=null?". "+e.getCause():" ");
			System.out.println(mess);
		}

		return null;
		
	}

	/**
	 * Unmarshals an XML String and fills the WStepTypeDef hierarchy
	 * 
	 * @param str
	 * 
	 */
	public void unmarshal(String str)  {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(WStepTypeDef.class);
			Unmarshaller unmarshaler = context.createUnmarshaller();
			StringReader stringReader = new StringReader(str);
			this.setObj((WStepTypeDef)unmarshaler.unmarshal(stringReader));			
		} catch (JAXBException e) {
			String mess = "Error unmarshalling WStepTypeDef "
					+ e.getMessage()+(e.getCause()!=null?". "+e.getCause():" ");
			System.out.println(mess);
		}

	}

	/**
	 * Constructor using object WStepTypeDef
	 */
	public void setObj(WStepTypeDef wstd) {

		this.id = wstd.getId();
		this.name = wstd.getName();
		this.type = wstd.getType();
		this.active = wstd.isActive();
		this.engineReq = wstd.isEngineReq();
		this.deleted = wstd.isDeleted();
		this.comments = wstd.getComments();
		this.allowedResponses = wstd.getAllowedResponses();
		this.relatedClass = wstd.getRelatedClass();
		this.insertDate = wstd.getInsertDate();
		this.insertUser = wstd.getInsertUser();
		this.modDate = wstd.getModDate();
		this.modUser = wstd.getModUser();
	
	}

}
