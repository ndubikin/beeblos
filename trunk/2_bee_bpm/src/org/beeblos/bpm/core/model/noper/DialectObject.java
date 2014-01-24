package org.beeblos.bpm.core.model.noper;

public class DialectObject {
	
	private String dialectValue;
	private String dialectName;
	
	public DialectObject(){
		
	}

	public DialectObject(String dialectValue, String dialectName) {
		super();
		this.dialectValue = dialectValue;
		this.dialectName = dialectName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dialectValue == null) ? 0 : dialectValue.hashCode());
		result = prime * result
				+ ((dialectName == null) ? 0 : dialectName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DialectObject other = (DialectObject) obj;
		if (dialectValue == null) {
			if (other.dialectValue != null)
				return false;
		} else if (!dialectValue.equals(other.dialectValue))
			return false;
		if (dialectName == null) {
			if (other.dialectName != null)
				return false;
		} else if (!dialectName.equals(other.dialectName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DialectObject [dialectName=" + dialectName + ", dialectValue="
				+ dialectValue + "]";
	}

	public String getDialectValue() {
		return dialectValue;
	}

	public void setDialectValue(String dialectValue) {
		this.dialectValue = dialectValue;
	}
	
	public String getDialectName() {
		return dialectName;
	}

	public void setDialectName(String dialectName) {
		this.dialectName = dialectName;
	}

}
