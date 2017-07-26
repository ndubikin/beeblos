package org.beeblos.bpm.core.bl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WUserDeviceDao;
import org.beeblos.bpm.core.error.WUserDeviceException;
import org.beeblos.bpm.core.error.WUserRoleException;
import org.beeblos.bpm.core.model.WUserDevice;
import org.beeblos.bpm.core.model.WUserRole;
import org.joda.time.DateTime;

/**
 * TODO: Under construction!!
 * 
 * @author pab 20160322
 *
 */
public class WUserDeviceBL {

	private static final Log logger = LogFactory
			.getLog(WUserDeviceBL.class);

	public WUserDeviceBL() {

	}

	/**
	 */
	public WUserDevice addNewUserDevice(WUserDevice userDevice, Integer currUserId)
			throws WUserRoleException, WUserDeviceException {

		logger.debug("WUserDeviceBL:addNewUserDevice() with idUser = " + userDevice.getUserId()
				+ " and idDevice = " + userDevice.getDeviceId());

		Integer idUserDevice = this.add(userDevice, userDevice.getUserId(), currUserId);

		if (idUserDevice != null){
			return this.getUserDeviceByPK(idUserDevice);
		}

		return null;

	}
	
	public WUserDevice getUserDeviceByPK(Integer pk) throws WUserDeviceException {
		return new WUserDeviceDao().getUserRoleByPK(pk);
	}

	public Integer add(WUserDevice instance, String idCurrentUser, Integer currUserId)
			throws WUserRoleException, WUserDeviceException {

		logger.debug("WUserRoleBL:add()");

//		_consistencyDataControl(instance);??
// 		_redundancyDataControl(instance); ¿¿

		instance.setAddDate(new DateTime());
		instance.setAddUser(currUserId);

		WUserDeviceDao wUserDeviceDao = new WUserDeviceDao();

		return wUserDeviceDao.add(instance);

	}

	public void update(WUserDevice instance, Integer idCurrentUser)
			throws WUserDeviceException {

		logger.debug("WUserRoleBL:update()");

		WUserDeviceDao wUserDeviceDao = new WUserDeviceDao();

//		_consistencyDataControl(instance); ¿¿

		wUserDeviceDao.update(instance);

	}
	
	public List<WUserDevice> getUserDeviceListByUserId( String idUser ) 
			throws WUserDeviceException {
		
		logger.debug("WUserDeviceBL:getUserDeviceListByUserId()");
		
		return new WUserDeviceDao().getUserDeviceListByUserId(idUser);
	}

	/**
	 */
	public void delete(WUserDevice instance, Integer idCurrentUser)
			throws WUserDeviceException {

		logger.debug("WUserDeviceBL:delete()");

		WUserDeviceDao wUserDeviceDao = new WUserDeviceDao();
		wUserDeviceDao.delete(instance);

	}

	public WUserDevice getUserDeviceByDevicePK(String deviceId)
			throws WUserDeviceException {

		WUserDeviceDao wUserDeviceDao = new WUserDeviceDao();
		return wUserDeviceDao.getUserDeviceByDevicePK(deviceId);

	}


	private void _consistencyDataControl(WUserRole instance)
			throws WUserRoleException {

		Integer idUserTmp = instance.getUser().getId();
		Integer idRoleTmp = instance.getRole().getId();

		if (idUserTmp == null || idUserTmp.equals(0)) {
			throw new WUserRoleException(
					"The user role you are trying to add HAS NOT A VALID user id.");
		}

		if (idRoleTmp == null || idRoleTmp.equals(0)) {
			throw new WUserRoleException(
					"The user role you are trying to add HAS NOT A VALID role id.");
		}

	}

	public boolean existsUserDevice(WUserDevice instance)throws WUserDeviceException {

		if (instance == null || instance.getUserId() == null || instance.getDeviceId() == null 
				|| instance.getAuthorizationUser() == null) {
			throw new WUserDeviceException(
					"The user device instance is not valid!");
		}

		WUserDeviceDao wUserDeviceDao = new WUserDeviceDao();
		return wUserDeviceDao.existsUserDevice(instance.getUserId(), instance.getDeviceId());

	}

	public boolean existsUserDevice(String userId, String deviceId) throws WUserDeviceException{
		
		if(userId==null||userId.equals("")||deviceId==null||deviceId.equals(""))
			throw new WUserDeviceException("Either userId or deviceId were null or empty.");
		
		WUserDeviceDao wUserDeviceDao = new WUserDeviceDao();
		return wUserDeviceDao.existsUserDevice(userId, deviceId);
	}
	
	private void _redundancyDataControl(WUserDevice instance)
			throws WUserDeviceException {

		boolean existUserRole = this.existsUserDevice(instance);

		if (existUserRole) {
			throw new WUserDeviceException(
					"The user device you are trying to add EXISTS YET");
		}

	}

}
