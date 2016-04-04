package org.beeblos.bpm.core.model;

import java.io.Serializable;

import org.beeblos.bpm.core.model.enumerations.DeviceType;
import org.joda.time.DateTime;

/**
 * Links a userId with a deviceId. This object can be accessed from WUserDefBL or 
 * WUserDeviceBL, but either way the code will be in WUserDeviceBL since the other
 * BL will call this one to do the work.
 * 
 * Notes: A device can't be authorized for >1 users.
 * 
 * @author pab 20160322
 *
 */
public class WUserDevice implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	/**
	 * The user id that corresponds with the device which is the next property.
	 */
	private String userId;
	
	/**
	 * The device's unique identification code.
	 */
	private String deviceId;
	
	/**
	 * The device's phone number, if it has a SIM card.
	 */
	private String phoneNumber;
	
	/**
	 * The type of the device. As of now only possible are: DEVICE_TYPE_MOBILE or DEVICE_TYPE_TABLET.
	 */
	private DeviceType deviceType;
	
	/**
	 * If the device has already been accepted in the system.
	 * 
	 * TODO: Authorization must be done by a user with this "priviledge". Right now the authorization
	 * is automatic, when creating the object set isAuthorized to true. pab 20160322 
	 */
	private Boolean isAuthorized;
	
	/**
	 * The user that authorized the device linking it to its userId. The authorizationUser
	 * is likely to be different than the userId, but could be the same.  
	 */
	private Integer authorizationUser;
	
	/**
	 * The date the device was authorized. Null if the device hasn't been authorized yet.
	 */
	private DateTime authorizationDate;
	
	private Integer addUser;
	private DateTime addDate;
	private Integer modUser;
	private DateTime modDate;
	
	public WUserDevice() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WUserDevice(Integer id, String userId, String deviceId, String phoneNumber, DeviceType deviceType,
			Boolean isAuthorized, Integer authorizationUser, DateTime authorizationDate, Integer addUser,
			DateTime addDate, Integer modUser, DateTime modDate) {
		super();
		this.id = id;
		this.userId = userId;
		this.deviceId = deviceId;
		this.phoneNumber = phoneNumber;
		this.deviceType = deviceType;
		this.isAuthorized = isAuthorized;
		this.authorizationUser = authorizationUser;
		this.authorizationDate = authorizationDate;
		this.addUser = addUser;
		this.addDate = addDate;
		this.modUser = modUser;
		this.modDate = modDate;
	}

	public WUserDevice(String userId, String deviceId, String phoneNumber, DeviceType deviceType, Boolean isAuthorized,
			Integer authorizationUser, DateTime authorizationDate, Integer addUser, DateTime addDate, Integer modUser,
			DateTime modDate) {
		super();
		this.userId = userId;
		this.deviceId = deviceId;
		this.phoneNumber = phoneNumber;
		this.deviceType = deviceType;
		this.isAuthorized = isAuthorized;
		this.authorizationUser = authorizationUser;
		this.authorizationDate = authorizationDate;
		this.addUser = addUser;
		this.addDate = addDate;
		this.modUser = modUser;
		this.modDate = modDate;
	}

	@Override
	public String toString() {
		return "WUserDevice [id=" + id + ", userId=" + userId + ", deviceId=" + deviceId + ", phoneNumber="
				+ phoneNumber + ", deviceType=" + deviceType + ", isAuthorized=" + isAuthorized + ", authorizationUser="
				+ authorizationUser + ", authorizationDate=" + authorizationDate + ", addUser=" + addUser + ", addDate="
				+ addDate + ", modUser=" + modUser + ", modDate=" + modDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addDate == null) ? 0 : addDate.hashCode());
		result = prime * result + ((addUser == null) ? 0 : addUser.hashCode());
		result = prime * result + ((authorizationDate == null) ? 0 : authorizationDate.hashCode());
		result = prime * result + ((authorizationUser == null) ? 0 : authorizationUser.hashCode());
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result + ((deviceType == null) ? 0 : deviceType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isAuthorized == null) ? 0 : isAuthorized.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		WUserDevice other = (WUserDevice) obj;
		if (addDate == null) {
			if (other.addDate != null)
				return false;
		} else if (!addDate.equals(other.addDate))
			return false;
		if (addUser == null) {
			if (other.addUser != null)
				return false;
		} else if (!addUser.equals(other.addUser))
			return false;
		if (authorizationDate == null) {
			if (other.authorizationDate != null)
				return false;
		} else if (!authorizationDate.equals(other.authorizationDate))
			return false;
		if (authorizationUser == null) {
			if (other.authorizationUser != null)
				return false;
		} else if (!authorizationUser.equals(other.authorizationUser))
			return false;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (deviceType != other.deviceType)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isAuthorized == null) {
			if (other.isAuthorized != null)
				return false;
		} else if (!isAuthorized.equals(other.isAuthorized))
			return false;
		if (modDate == null) {
			if (other.modDate != null)
				return false;
		} else if (!modDate.equals(other.modDate))
			return false;
		if (modUser == null) {
			if (other.modUser != null)
				return false;
		} else if (!modUser.equals(other.modUser))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceUUID) {
		this.deviceId = deviceUUID;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public Boolean getIsAuthorized() {
		return isAuthorized;
	}

	public void setIsAuthorized(Boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}

	public Integer getAuthorizationUser() {
		return authorizationUser;
	}

	public void setAuthorizationUser(Integer authorizationUser) {
		this.authorizationUser = authorizationUser;
	}

	public DateTime getAuthorizationDate() {
		return authorizationDate;
	}

	public void setAuthorizationDate(DateTime authorizationDate) {
		this.authorizationDate = authorizationDate;
	}

	public Integer getAddUser() {
		return addUser;
	}

	public void setAddUser(Integer insertUser) {
		this.addUser = insertUser;
	}

	public DateTime getAddDate() {
		return addDate;
	}

	public void setAddDate(DateTime insertDate) {
		this.addDate = insertDate;
	}

	public Integer getModUser() {
		return modUser;
	}

	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}

	public DateTime getModDate() {
		return modDate;
	}

	public void setModDate(DateTime modDate) {
		this.modDate = modDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
