package org.beeblos.bpm.core.graph;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MxCell {

	private String parent;
	private String target;
	private String source;
	private String vertex;
	private String style;
	private String edge;
	private MxGeometry mxGeometry;
	
	public MxCell(String parent, String target, String source, String vertex,
			String style, String edge, MxGeometry mxGeometry) {
		this.parent = parent;
		this.target = target;
		this.source = source;
		this.vertex = vertex;
		this.style = style;
		this.edge = edge;
		this.mxGeometry = mxGeometry;
	}
	public MxCell(String parent, String source, String vertex, String style,
			String edge, MxGeometry mxGeometry) {
		this.parent = parent;
		this.source = source;
		this.vertex = vertex;
		this.style = style;
		this.edge = edge;
		this.mxGeometry = mxGeometry;
	}
	public MxCell(String parent, String vertex, String style,
			MxGeometry mxGeometry) {
		this.parent = parent;
		this.vertex = vertex;
		this.style = style;
		this.mxGeometry = mxGeometry;
	}
	public MxCell() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public MxCell(String parent, String vertex, String style, String edge,
			MxGeometry mxGeometry) {
		this.parent = parent;
		this.vertex = vertex;
		this.style = style;
		this.edge = edge;
		this.mxGeometry = mxGeometry;
	}
	
	/**
	 * @return the target
	 */
	@XmlAttribute(name="target")
	public String getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	/**
	 * @return the source
	 */
	@XmlAttribute(name="source")
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the style
	 */
	@XmlAttribute(name="style")
	public String getStyle() {
		return style;
	}
	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}
	/**
	 * @return the parent
	 */
	@XmlAttribute(name="parent")
	public String getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}
	/**
	 * @return the vertex
	 */
	@XmlAttribute(name="vertex")
	public String getVertex() {
		return vertex;
	}
	/**
	 * @param vertex the vertex to set
	 */
	public void setVertex(String vertex) {
		this.vertex = vertex;
	}
	/**
	 * @return the mxGeometry
	 */
	@XmlElement(name="mxGeometry")
	public MxGeometry getMxGeometry() {
		return mxGeometry;
	}
	/**
	 * @param mxGeometry the mxGeometry to set
	 */
	public void setMxGeometry(MxGeometry mxGeometry) {
		this.mxGeometry = mxGeometry;
	}
	
	/**
	 * @return the edge
	 */
	@XmlAttribute(name="edge")
	public String getEdge() {
		return edge;
	}
	/**
	 * @param edge the edge to set
	 */
	public void setEdge(String edge) {
		this.edge = edge;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MxCell [" + (parent != null ? "parent=" + parent + ", " : "")
				+ (source != null ? "source=" + source + ", " : "")
				+ (vertex != null ? "vertex=" + vertex + ", " : "")
				+ (style != null ? "style=" + style + ", " : "")
				+ (edge != null ? "edge=" + edge + ", " : "")
				+ (mxGeometry != null ? "mxGeometry=" + mxGeometry : "") + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edge == null) ? 0 : edge.hashCode());
		result = prime * result
				+ ((mxGeometry == null) ? 0 : mxGeometry.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((style == null) ? 0 : style.hashCode());
		result = prime * result + ((vertex == null) ? 0 : vertex.hashCode());
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
		if (!(obj instanceof MxCell))
			return false;
		MxCell other = (MxCell) obj;
		if (edge == null) {
			if (other.edge != null)
				return false;
		} else if (!edge.equals(other.edge))
			return false;
		if (mxGeometry == null) {
			if (other.mxGeometry != null)
				return false;
		} else if (!mxGeometry.equals(other.mxGeometry))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (style == null) {
			if (other.style != null)
				return false;
		} else if (!style.equals(other.style))
			return false;
		if (vertex == null) {
			if (other.vertex != null)
				return false;
		} else if (!vertex.equals(other.vertex))
			return false;
		return true;
	}
}
