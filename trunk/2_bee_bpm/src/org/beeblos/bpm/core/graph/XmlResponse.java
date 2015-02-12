package org.beeblos.bpm.core.graph;

import org.beeblos.bpm.core.model.WStepResponseDef;

public class XmlResponse {

	private Integer id;
	private String name;

	public XmlResponse() {
		super();
	}
	public XmlResponse(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	/*
	 * Util para convertir una response en una xmlresponse y poder compararlas mas facilmente
	 */
	public XmlResponse wStepResponseDefToXmlResponse(WStepResponseDef res){
		XmlResponse a = new XmlResponse();
		a.setId(res.getId());
		a.setName(res.getName());
		return a;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "XmlResponse [" + (id != null ? "id=" + id + ", " : "")
				+ (name != null ? "name=" + name : "") + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (!(obj instanceof XmlResponse))
			return false;
		XmlResponse other = (XmlResponse) obj;
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
}
