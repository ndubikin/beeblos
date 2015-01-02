package org.beeblos.bpm.core.graph;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Symbol {
	
	private String description;
	private String xmlId;
	private String label;
	private MxCell mxCell;
	
	public Symbol() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Symbol(String description, String id, String label,
			MxCell mxCell) {
		this.description = description;
		this.xmlId = id;
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
	 * @return the xmlId
	 */
	@XmlAttribute(name="xmlId")
	public String getXmlId() {
		return xmlId;
	}
	/**
	 * @param xmlId the xmlId to set
	 */
	public void setXmlId(String id) {
		this.xmlId = id;
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
	public MxCell getMxCell() {
		return mxCell;
	}
	
	/**
	 * @param mxCell the mxCell to set
	 */
	public void setMxCell(MxCell mxCell) {
		this.mxCell = mxCell;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Symbol ["
				+ (description != null ? "description=" + description + ", "
						: "") + (xmlId != null ? "xmlId=" + xmlId + ", " : "")
				+ (label != null ? "label=" + label + ", " : "")
				+ (mxCell != null ? "mxCell=" + mxCell : "") + "]";
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
		result = prime * result + ((xmlId == null) ? 0 : xmlId.hashCode());
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
		if (xmlId == null) {
			if (other.xmlId != null)
				return false;
		} else if (!xmlId.equals(other.xmlId))
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
