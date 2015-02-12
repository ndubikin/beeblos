package org.beeblos.bpm.core.graph;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class MxArray {

	private List<MxPoint> mxPoint;
	private String as;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 2;
		return "MxArray ["
				+ (mxPoint != null ? "mxPoint="
						+ mxPoint.subList(0, Math.min(mxPoint.size(), maxLen))
						+ ", " : "") + (as != null ? "as=" + as : "") + "]";
	}

	

	public MxArray(List<MxPoint> mxPoint, String as) {
		this.mxPoint = mxPoint;
		this.as = as;
	}

	public MxArray() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MxArray(List<MxPoint> mxPoint) {
		this.mxPoint = mxPoint;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((as == null) ? 0 : as.hashCode());
		result = prime * result + ((mxPoint == null) ? 0 : mxPoint.hashCode());
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
		if (!(obj instanceof MxArray))
			return false;
		MxArray other = (MxArray) obj;
		if (as == null) {
			if (other.as != null)
				return false;
		} else if (!as.equals(other.as))
			return false;
		if (mxPoint == null) {
			if (other.mxPoint != null)
				return false;
		} else if (!mxPoint.equals(other.mxPoint))
			return false;
		return true;
	}
}
