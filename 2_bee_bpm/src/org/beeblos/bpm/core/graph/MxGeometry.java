package org.beeblos.bpm.core.graph;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class MxGeometry {

	private MxArray array;
	private String relative;
	private String as;
	private String height;
	private String width;
	private String x;
	private String y;
	private List<MxPoint> mxPoint;
	
	public MxGeometry() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MxGeometry(MxArray array, String relative, String as) {
		this.array = array;
		this.relative = relative;
		this.as = as;
	}
	
	public MxGeometry(MxArray array, String relative, String as, String height,
			String width, String x, String y) {
		this.array = array;
		this.relative = relative;
		this.as = as;
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
	}
	
	public MxGeometry(MxArray array, String relative, String as, String height,
			String width, String x, String y, List<MxPoint> mxPoint) {
		this.array = array;
		this.relative = relative;
		this.as = as;
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
		this.mxPoint = mxPoint;
	}

	/**
	 * @return the relative
	 */
	@XmlAttribute(name="relative")
	public String getRelative() {
		return relative;
	}
	/**
	 * @param relative the relative to set
	 */
	public void setRelative(String relative) {
		this.relative = relative;
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
	
	
	/**
	 * @return the height
	 */
	@XmlAttribute(name="height")
	public String getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(String height) {
		this.height = height;
	}
	/**
	 * @return the width
	 */
	@XmlAttribute(name="width")
	public String getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
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
		final int maxLen = 2;
		return "MxGeometry ["
				+ (array != null ? "array=" + array + ", " : "")
				+ (relative != null ? "relative=" + relative + ", " : "")
				+ (as != null ? "as=" + as + ", " : "")
				+ (height != null ? "height=" + height + ", " : "")
				+ (width != null ? "width=" + width + ", " : "")
				+ (x != null ? "x=" + x + ", " : "")
				+ (y != null ? "y=" + y + ", " : "")
				+ (mxPoint != null ? "mxPoint="
						+ mxPoint.subList(0, Math.min(mxPoint.size(), maxLen))
						: "") + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((array == null) ? 0 : array.hashCode());
		result = prime * result + ((as == null) ? 0 : as.hashCode());
		result = prime * result + ((height == null) ? 0 : height.hashCode());
		result = prime * result + ((mxPoint == null) ? 0 : mxPoint.hashCode());
		result = prime * result
				+ ((relative == null) ? 0 : relative.hashCode());
		result = prime * result + ((width == null) ? 0 : width.hashCode());
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
		if (!(obj instanceof MxGeometry))
			return false;
		MxGeometry other = (MxGeometry) obj;
		if (array == null) {
			if (other.array != null)
				return false;
		} else if (!array.equals(other.array))
			return false;
		if (as == null) {
			if (other.as != null)
				return false;
		} else if (!as.equals(other.as))
			return false;
		if (height == null) {
			if (other.height != null)
				return false;
		} else if (!height.equals(other.height))
			return false;
		if (mxPoint == null) {
			if (other.mxPoint != null)
				return false;
		} else if (!mxPoint.equals(other.mxPoint))
			return false;
		if (relative == null) {
			if (other.relative != null)
				return false;
		} else if (!relative.equals(other.relative))
			return false;
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
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
	 * @return the array
	 */
	@XmlElement(name="Array")
	public MxArray getArray() {
		return array;
	}
	/**
	 * @param array the array to set
	 */
	public void setArray(MxArray array) {
		this.array = array;
	}
	/**
	 * @return the mxPoint
	 */
	@XmlElement(name="mxPoint")
	public List<MxPoint> getMxPoint() {
		return mxPoint;
	}
	/**
	 * @param mxPoint the mxPoint to set
	 */
	public void setMxPoint(List<MxPoint> mxPoint) {
		this.mxPoint = mxPoint;
	}
}
