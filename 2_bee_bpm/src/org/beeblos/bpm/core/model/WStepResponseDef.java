package org.beeblos.bpm.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.joda.time.DateTime;

// Generated Oct 30, 2010 12:25:05 AM by Hibernate Tools 3.3.0.GA

/**
 * WStepResponsesDef generated by hbm2java
 * 
 * RESPUESTA PARA EL PASO "STEP"
 */
@XmlAccessorType(XmlAccessType.NONE)
public class WStepResponseDef implements java.io.Serializable, Comparable<WStepResponseDef> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer respOrder;
	private String name;
	
	private Integer insertUser;
	private DateTime insertDate;
	private Integer modUser;
	private DateTime modDate;

	public WStepResponseDef() {
		super();
	}

	public WStepResponseDef(Integer respOrder, String name) {
		super();
		this.respOrder = respOrder;
		this.name = name;
	}
	
	public WStepResponseDef(Integer id, Integer respOrder, String name) {
		super();
		this.id = id;
		this.respOrder = respOrder;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	@XmlAttribute(name="id")
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
	 * @return the respOrder
	 */
	@XmlAttribute(name="respOrder")
	public Integer getRespOrder() {
		return respOrder;
	}

	/**
	 * @param respOrder the respOrder to set
	 */
	public void setRespOrder(Integer respOrder) {
		this.respOrder = respOrder;
	}

	/**
	 * @return the name
	 */
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result
				+ ((insertUser == null) ? 0 : insertUser.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((respOrder == null) ? 0 : respOrder.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WStepResponseDef other = (WStepResponseDef) obj;
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
		if (respOrder == null) {
			if (other.respOrder != null)
				return false;
		} else if (!respOrder.equals(other.respOrder))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WStepResponseDef [respOrder=" + respOrder + ", name=" + name
				+ ", insertUser=" + insertUser + ", insertDate=" + insertDate
				+ ", modUser=" + modUser + ", modDate=" + modDate + "]";
	}

	public boolean empty() {

		if (id!=null && ! id.equals(0)) return false;
		if (name!=null && ! "".equals(name)) return false;
	
		return true;
	}
	@Override
	public int compareTo(WStepResponseDef o) {
		return(this.id.compareTo(((WStepResponseDef)o).getId()));
	}
}
