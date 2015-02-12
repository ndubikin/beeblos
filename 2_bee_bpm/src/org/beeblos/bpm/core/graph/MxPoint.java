package org.beeblos.bpm.core.graph;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "as", "x", "y"})
public class MxPoint {

	private String x;
	private String y;
	private String as;
	
	public MxPoint(String x, String y) {
		this.x = x;
		this.y = y;
	}
	public MxPoint(String x, String y, String as) {
		this.x = x;
		this.y = y;
		this.as = as;
	}

	public MxPoint() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the x
	 */
	@XmlAttribute(name="x")
	public String getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(String x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	@XmlAttribute(name="y")
	public String getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(String y) {
		this.y = y;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MxPoint [" + (x != null ? "x=" + x + ", " : "")
				+ (y != null ? "y=" + y + ", " : "")
				+ (as != null ? "as=" + as : "") + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((as == null) ? 0 : as.hashCode());
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
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
		if (!(obj instanceof MxPoint))
			return false;
		MxPoint other = (MxPoint) obj;
		if (as == null) {
			if (other.as != null)
				return false;
		} else if (!as.equals(other.as))
			return false;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
	}
	/**
	 * @return the as
	 */
	@XmlAttribute(name="as")
	public String getAs() {
		return as;
	}
	/**
	 * @param as the as to set
	 */
	public void setAs(String as) {
		this.as = as;
	}
	
}
