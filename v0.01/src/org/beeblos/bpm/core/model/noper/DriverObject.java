package org.beeblos.bpm.core.model.noper;

public class DriverObject {

	private String driverOrDBValue;
	private String driverName;
	
	public DriverObject(){
		
	}

	public DriverObject(String driverOrDBValue, String driverName) {
		super();
		this.driverOrDBValue = driverOrDBValue;
		this.driverName = driverName;
	}

	public String getDriverOrDBValue() {
		return driverOrDBValue;
	}

	public void setDriverOrDBValue(String driverOrDBValue) {
		this.driverOrDBValue = driverOrDBValue;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	@Override
	public String toString() {
		return "DriverObject [driverOrDBValue=" + driverOrDBValue
				+ ", driverName=" + driverName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((driverName == null) ? 0 : driverName.hashCode());
		result = prime * result
				+ ((driverOrDBValue == null) ? 0 : driverOrDBValue.hashCode());
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
		DriverObject other = (DriverObject) obj;
		if (driverName == null) {
			if (other.driverName != null)
				return false;
		} else if (!driverName.equals(other.driverName))
			return false;
		if (driverOrDBValue == null) {
			if (other.driverOrDBValue != null)
				return false;
		} else if (!driverOrDBValue.equals(other.driverOrDBValue))
			return false;
		return true;
	}
	
}
