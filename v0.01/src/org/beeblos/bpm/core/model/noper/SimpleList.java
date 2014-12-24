package org.beeblos.bpm.core.model.noper;

public class SimpleList {

	Integer integer1;
	String string2;
	
	public SimpleList() {
		// TODO Auto-generated constructor stub
	}

	public SimpleList(String string1, String string2) {
		this.integer1 = (string1==null?null:new Integer(string1));
		this.string2 = (string2==null?null:string2);
	}
	
	public SimpleList(Integer integer1, String string2) {
		this.integer1 = (integer1==null?null:integer1);
		this.string2 = (string2==null?null:string2);
	}

	public Integer getInteger1() {
		return integer1;
	}

	public void setInteger1(Integer integer1) {
		this.integer1 = integer1;
	}

	public String getString2() {
		return string2;
	}

	public void setString2(String string2) {
		this.string2 = string2;
	}

	@Override
	public String toString() {
		return "StringPair ["
				+ (integer1 != null ? "integer1=" + integer1 + ", " : "")
				+ (string2 != null ? "string2=" + string2 : "") + "]";
	}
	
	public String toStringForIndex() {
		return (integer1 != null ? "string1=" + integer1 + " " : "")
				+ (string2 != null ? "string2=" + string2 : " ") ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((integer1 == null) ? 0 : integer1.hashCode());
		result = prime * result + ((string2 == null) ? 0 : string2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SimpleList))
			return false;
		SimpleList other = (SimpleList) obj;
		if (integer1 == null) {
			if (other.integer1 != null)
				return false;
		} else if (!integer1.equals(other.integer1))
			return false;
		if (string2 == null) {
			if (other.string2 != null)
				return false;
		} else if (!string2.equals(other.string2))
			return false;
		return true;
	}
	
	
	

}
