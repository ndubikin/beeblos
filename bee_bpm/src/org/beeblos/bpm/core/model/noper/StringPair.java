package org.beeblos.bpm.core.model.noper;

public class StringPair {

	String string1;
	String string2;
	
	public StringPair() {
		// TODO Auto-generated constructor stub
	}

	public StringPair(String string1, String string2) {
		this.string1 = (string1==null?null:string1);
		this.string2 = (string2==null?null:string2);
	}
	
	public StringPair(int integer1, String string2) {
		this.string1 = new Integer(integer1).toString().trim();
		this.string2 = (string2==null?null:string2);
	}

	public String getString1() {
		return string1;
	}

	public void setString1(String string1) {
		this.string1 = string1;
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
				+ (string1 != null ? "string1=" + string1 + ", " : "")
				+ (string2 != null ? "string2=" + string2 : "") + "]";
	}
	
	public String toStringForIndex() {
		return (string1 != null ? "string1=" + string1 + " " : "")
				+ (string2 != null ? "string2=" + string2 : " ") ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((string1 == null) ? 0 : string1.hashCode());
		result = prime * result + ((string2 == null) ? 0 : string2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof StringPair))
			return false;
		StringPair other = (StringPair) obj;
		if (string1 == null) {
			if (other.string1 != null)
				return false;
		} else if (!string1.equals(other.string1))
			return false;
		if (string2 == null) {
			if (other.string2 != null)
				return false;
		} else if (!string2.equals(other.string2))
			return false;
		return true;
	}
	
	
	

}
