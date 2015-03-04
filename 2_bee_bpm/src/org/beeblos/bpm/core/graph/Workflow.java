package org.beeblos.bpm.core.graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.beeblos.bpm.core.model.WExternalMethod;

public class Workflow {

	private String description;
	private String id;
	private String spId;
	private String label;
	private Set<WExternalMethod> method;

	
	public Workflow() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Workflow(String description, String id, String spId, String label) {
		this.description = description;
		this.id = id;
		this.spId = spId;
		this.label = label;
	}

	public Workflow(String description, String id, String spId, String label,
			Set<WExternalMethod> method) {
		this.description = description;
		this.id = id;
		this.spId = spId;
		this.label = label;
		this.method = method;
	}

	/**
	 * @return the description
	 */
	@XmlAttribute(name="description")
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the id
	 */
	@XmlAttribute(name="id")
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the spId
	 */
	@XmlAttribute(name="spId")
	public String getSpId() {
		return spId;
	}
	/**
	 * @param spId the spId to set
	 */
	public void setSpId(String spId) {
		this.spId = spId;
	}
	/**
	 * @return the label
	 */
	@XmlAttribute(name="label")
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
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
	/**
	 * @return the method
	 */
	@XmlElement(name="method")
	public Set<WExternalMethod> getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(Set<WExternalMethod> method) {
		this.method = method;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 2;
		return "Workflow ["
				+ (description != null ? "description=" + description + ", "
						: "") + (id != null ? "id=" + id + ", " : "")
				+ (spId != null ? "spId=" + spId + ", " : "")
				+ (label != null ? "label=" + label + ", " : "")
				+ (method != null ? "method=" + toString(method, maxLen) : "")
				+ "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((spId == null) ? 0 : spId.hashCode());
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
		if (!(obj instanceof Workflow))
			return false;
		Workflow other = (Workflow) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (spId == null) {
			if (other.spId != null)
				return false;
		} else if (!spId.equals(other.spId))
			return false;
		return true;
	}
}
