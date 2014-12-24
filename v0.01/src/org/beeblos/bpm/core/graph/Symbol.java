package org.beeblos.bpm.core.graph;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Symbol {
	
	private String description;
	private String id;
	private String label;
	private List<MxCell> mxCell;
	
	public Symbol() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Symbol(String description, String id, String label,
			List<MxCell> mxCell) {
		this.description = description;
		this.id = id;
		this.label = label;
		this.mxCell = mxCell;
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
	/**
	 * @return the mxCell
	 */
	@XmlElement(name="mxCell")
	public List<MxCell> getMxCell() {
		return mxCell;
	}
	/**
	 * @param mxCell the mxCell to set
	 */
	public void setMxCell(List<MxCell> mxCell) {
		this.mxCell = mxCell;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 2;
		return "Symbol ["
				+ (description != null ? "description=" + description + ", "
						: "")
				+ (id != null ? "id=" + id + ", " : "")
				+ (label != null ? "label=" + label + ", " : "")
				+ (mxCell != null ? "mxCell="
						+ mxCell.subList(0, Math.min(mxCell.size(), maxLen))
						: "") + "]";
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
		result = prime * result + ((mxCell == null) ? 0 : mxCell.hashCode());
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
		if (!(obj instanceof Symbol))
			return false;
		Symbol other = (Symbol) obj;
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
		if (mxCell == null) {
			if (other.mxCell != null)
				return false;
		} else if (!mxCell.equals(other.mxCell))
			return false;
		return true;
	}
	
	
}
