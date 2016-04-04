package org.beeblos.bpm.core.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WUserDeviceException;
import org.beeblos.bpm.core.error.WUserRoleException;
import org.beeblos.bpm.core.model.WUserDevice;
import org.beeblos.bpm.core.model.WUserRole;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.sp.common.util.HibernateUtil;

public class WUserDeviceDao {
	private static final Log logger = LogFactory.getLog(WUserDeviceDao.class.getName());
	
	public WUserDeviceDao (){
		super();
	}
	
	public Integer add(WUserDevice userDevice) throws WUserDeviceException {

		logger.debug("add() WUserDevice - userId/authUser/deviceId: [" 
				+ ((userDevice!=null&&userDevice.getUserId()!=null)?userDevice.getUserId():"null") + ", " 
				+ ((userDevice!=null&&userDevice.getAuthorizationUser()!=null)?userDevice.getAuthorizationUser():"null") + ", " 
				+ ((userDevice!=null&&userDevice.getDeviceId()!=null)?userDevice.getDeviceId():"null") + "]");

		try {

			return Integer.valueOf(HibernateUtil.save(userDevice));

		} catch (HibernateException ex) {
			String mess = "HibernateException: add - Can't add userRole definition "
					+ " id userId/authUser/deviceId: " 
					+ ((userDevice!=null&&userDevice.getUserId()!=null)?userDevice.getUserId():"null") + ", " 
					+ ((userDevice!=null&&userDevice.getAuthorizationUser()!=null)?userDevice.getAuthorizationUser():"null") + ", " 
					+ ((userDevice!=null&&userDevice.getDeviceId()!=null)?userDevice.getDeviceId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WUserDeviceException(mess);

		} catch (Exception ex) {
			String mess = "Exception: add - Can't add userRole definition "
					+ " id userId/authUser/deviceId: " 
					+ ((userDevice!=null&&userDevice.getUserId()!=null)?userDevice.getUserId():"null") + ", " 
					+ ((userDevice!=null&&userDevice.getAuthorizationUser()!=null)?userDevice.getAuthorizationUser():"null") + ", " 
					+ ((userDevice!=null&&userDevice.getDeviceId()!=null)?userDevice.getDeviceId():"null")
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WUserDeviceException(mess);

		}

	}

	public void update(WUserDevice userDevice) throws WUserDeviceException {

		logger.debug("update() WUserDevice - userId/authUser/deviceId: [" 
				+ ((userDevice!=null&&userDevice.getUserId()!=null)?userDevice.getUserId():"null") + ", " 
				+ ((userDevice!=null&&userDevice.getAuthorizationUser()!=null)?userDevice.getAuthorizationUser():"null") + ", " 
				+ ((userDevice!=null&&userDevice.getDeviceId()!=null)?userDevice.getDeviceId():"null") + "]");

		try {

			HibernateUtil.update(userDevice);

		} catch (HibernateException ex) {
			String mess = "HibernateException: update - Can't update userRole definition "
					+ " id userId/authUser/deviceId: " 
					+ ((userDevice!=null&&userDevice.getUserId()!=null)?userDevice.getUserId():"null") + ", " 
					+ ((userDevice!=null&&userDevice.getAuthorizationUser()!=null)?userDevice.getAuthorizationUser():"null") + ", " 
					+ ((userDevice!=null&&userDevice.getDeviceId()!=null)?userDevice.getDeviceId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WUserDeviceException(mess);

		} catch (Exception ex) {
			String mess = "Exception: update - Can't update userRole definition "
					+ " id userId/authUser/deviceId: " 
					+ ((userDevice!=null&&userDevice.getUserId()!=null)?userDevice.getUserId():"null") + ", " 
					+ ((userDevice!=null&&userDevice.getAuthorizationUser()!=null)?userDevice.getAuthorizationUser():"null") + ", " 
					+ ((userDevice!=null&&userDevice.getDeviceId()!=null)?userDevice.getDeviceId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WUserDeviceException(mess);

		}

	}

	public void delete(WUserDevice userDevice) throws WUserDeviceException {

		logger.debug("delete() WUserDevice - userId/authUser/deviceId: [" 
				+ ((userDevice!=null&&userDevice.getUserId()!=null)?userDevice.getUserId():"null") + ", " 
				+ ((userDevice!=null&&userDevice.getAuthorizationUser()!=null)?userDevice.getAuthorizationUser():"null") + ", " 
				+ ((userDevice!=null&&userDevice.getDeviceId()!=null)?userDevice.getDeviceId():"null") + "]");

		try {

			userDevice = getUserDeviceByDevicePK(userDevice.getDeviceId());

			HibernateUtil.delete(userDevice);

		} catch (HibernateException ex) {
			String mess = "HibernateException: delete - Can't delete userRole definition "
					+ " id userId/authUser/deviceId: " 
					+ ((userDevice!=null&&userDevice.getUserId()!=null)?userDevice.getUserId():"null") + ", " 
					+ ((userDevice!=null&&userDevice.getAuthorizationUser()!=null)?userDevice.getAuthorizationUser():"null") + ", " 
					+ ((userDevice!=null&&userDevice.getDeviceId()!=null)?userDevice.getDeviceId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null"); 
			logger.error(mess);
			throw new WUserDeviceException(mess);

		} catch (Exception ex) {
			String mess = "Exception: delete - Can't delete userRole definition "
					+ " id userId/authUser/deviceId: " 
					+ ((userDevice!=null&&userDevice.getUserId()!=null)?userDevice.getUserId():"null") + ", " 
					+ ((userDevice!=null&&userDevice.getAuthorizationUser()!=null)?userDevice.getAuthorizationUser():"null") + ", " 
					+ ((userDevice!=null&&userDevice.getDeviceId()!=null)?userDevice.getDeviceId():"null") 
					+ " " + ex.getMessage()
					+ " " + (ex.getCause()!=null?ex.getCause():"null")
					+ " " + ex.getClass(); 
			logger.error(mess);
			throw new WUserDeviceException(mess);

		}

	}

	public WUserDevice getUserDeviceByDevicePK(String deviceId) throws WUserDeviceException {

		WUserDevice userDevice = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			userDevice = (WUserDevice) session.createCriteria(WUserDevice.class, "userDevice")
					.add( Restrictions.eq("userDevice.deviceId", deviceId))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getUserDeviceByDevicePK - we can't obtain the required id = " + deviceId
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WUserDeviceException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getUserDeviceByDevicePK - we can't obtain the required id = " + deviceId
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"")+ " "
					+ ex.getClass();
			logger.warn(mess);
			throw new WUserDeviceException(mess);			

		}

		return userDevice;
	}

	public WUserDevice getUserRoleByPK(Integer id) throws WUserDeviceException {

		WUserDevice userDevice = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			userDevice = (WUserDevice) session.get(WUserDevice.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getWUserDeviceByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"");
			logger.warn(mess);
			throw new WUserDeviceException(mess);

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getWUserDeviceByPK - we can't obtain the required id = " + id
					+ "] - " + ex.getMessage() 
					+ " " + (ex.getCause()!=null?ex.getCause():"")+ " "
					+ ex.getClass();
			logger.warn(mess);
			throw new WUserDeviceException(mess);			

		}

		return userDevice;
	}
	
	/**
      * Returns the userDevice list for a given User Id
	  */
	@SuppressWarnings("unchecked")
	public List<WUserDevice> getUserDeviceListByUserId( String idUser ) throws WUserDeviceException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WUserDevice> userDevices = null;
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			userDevices = session.createCriteria(WUserDevice.class, "userDevice")
					.add( Restrictions.eq("userDevice.userId", idUser))
					.list();

			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getWUserDeviceByUserId - we can't obtain the devices for user id = " + idUser
					+ ". " + e.getMessage() + " " + (e.getCause()!=null?e.getCause():"");
			logger.error(mess);
			throw new WUserDeviceException(mess);
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getWUserDeviceByUserId - we can't obtain the devices for user id = " + idUser
					+ ". " + e.getMessage() + " " + (e.getCause()!=null?e.getCause():"")+ " "
					+ e.getClass();
			logger.error(mess);
			throw new WUserDeviceException(mess);			
		}

		return userDevices;
	}

	public boolean existsUserDevice(String idUser, String idDevice)throws WUserDeviceException {

		Integer id = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			id = (Integer) session
						.createQuery("Select id From WUserDevice Where userId = :idUser And deviceId = :idDevice ")
						.setString("idUser", idUser)
						.setString("idDevice", idDevice)
						.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
			    tx.rollback(); 
			String mess = "HibernateException: existsUserRole - Can't check if the user role with ids userId/authUser/deviceId = " 
					+ ((idUser != null)?idUser:"") + ", "
					+ ((idDevice != null)?idDevice:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"");
			logger.error(mess);
			throw new WUserDeviceException(mess);
		} catch (Exception ex){
			if (tx != null)
			    tx.rollback(); 
			String mess = "Exception: existsUserRole - Can't check if the user role with ids userId/authUser/deviceId = " 
					+ ((idUser != null)?idUser:"") + ", "
					+ ((idDevice != null)?idDevice:"") + " exists. "
					+ ex.getMessage() + " " + (ex.getCause()!=null?ex.getCause():"") + " " + ex.getClass();
			logger.error(mess);
			throw new WUserDeviceException(mess);
		}

		if (id == null){
			return false;
		}
		return true;
	}
}
