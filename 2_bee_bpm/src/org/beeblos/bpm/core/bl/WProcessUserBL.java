package org.beeblos.bpm.core.bl;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessUserDao;
import org.beeblos.bpm.core.error.WProcessUserException;
import org.beeblos.bpm.core.model.WProcessUser;
import org.beeblos.bpm.core.model.WUserDef;
import org.joda.time.DateTime;

public class WProcessUserBL {

	private static final Log logger = LogFactory
			.getLog(WProcessUserBL.class);

	public WProcessUserBL() {

	}

	/**
	 * Adds the new relation between the idProcess and the user id and returns the
	 * complete relation's object.
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param idProcess
	 *            the process id
	 * @param idUser
	 *            the user id
	 * @param isAdmin
	 *            the is admin
	 * @param idCurrentUser
	 *            the id current user
	 * @return the w process user
	 * @throws WProcessUserException
	 *             the w process user exception
	 */
	public WProcessUser addNewProcessUser(Integer idProcess, Integer idUser, boolean isAdmin, Integer idCurrentUser)
			throws WProcessUserException {

		logger.debug("WProcessUserBL:addNewProcessUser() with idProcess = " + idProcess
				+ " and idUser = " + idUser);

		WProcessUser instance = new WProcessUser();
		instance.setIdProcess(idProcess);
		instance.setUser(new WUserDef(idUser));
		instance.setAdmin(isAdmin);
		
		Integer idProcessUser = this.add(instance, idCurrentUser);

		if (idProcessUser != null){
			return this.getProcessUserByPK(idProcessUser);
		}
		
		return null;
		
	}

	public Integer add(WProcessUser instance, Integer idCurrentUser)
			throws WProcessUserException {

		logger.debug("WProcessUserBL:add()");

		_consistencyDataControl(instance);
		_redundancyDataControl(instance);

		instance.setInsertDate(new DateTime());
		instance.setInsertUser(idCurrentUser);
		
		WProcessUserDao wProcessUserDao = new WProcessUserDao();

		return wProcessUserDao.add(instance);

	}

	public void update(WProcessUser instance, Integer idCurrentUser)
			throws WProcessUserException {

		logger.debug("WProcessUserBL:update()");

		WProcessUserDao wProcessUserDao = new WProcessUserDao();

		_consistencyDataControl(instance);

		wProcessUserDao.update(instance);

	}

	/**
	 * Deletes the WProcessUser.
	 * 
	 * IMPORTANT: This should only be called from the method "deleteProcessRelatedUser"
	 * of WProcessDefBL because it is the correct way of deleting relations between users
	 * and processes
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param instance
	 *            the instance
	 * @param idCurrentUser
	 *            the id current user
	 * @throws WProcessUserException
	 *             the w process user exception
	 */
	public void delete(WProcessUser instance, Integer idCurrentUser)
			throws WProcessUserException {

		logger.debug("WProcessUserBL:delete()");

		WProcessUserDao wProcessUserDao = new WProcessUserDao();
		wProcessUserDao.delete(instance);

	}

	public WProcessUser getProcessUserByPK(Integer id)
			throws WProcessUserException {

		WProcessUserDao wProcessUserDao = new WProcessUserDao();
		return wProcessUserDao.getProcessUserByPK(id);

	}

    private void _consistencyDataControl(WProcessUser instance)
			throws WProcessUserException {

		Integer idProcessTmp = instance.getIdProcess();
		Integer idUserTmp = instance.getUser().getId();

		if (idProcessTmp == null || idProcessTmp.equals(0)) {
			throw new WProcessUserException(
					"The process user you are trying to add HAS NOT A VALID process id.");
		}

		if (idUserTmp == null || idUserTmp.equals(0)) {
			throw new WProcessUserException(
					"The process user you are trying to add HAS NOT A VALID user id.");
		}

	}

	public boolean existsProcessUser(WProcessUser instance)throws WProcessUserException {

		if (instance == null 
				|| instance.getIdProcess() == null || instance.getIdProcess().equals(0)
				|| instance.getUser() == null || instance.getUser().getId().equals(0)) {
			throw new WProcessUserException(
					"The process user is not valid!");
		}

		WProcessUserDao wProcessUserDao = new WProcessUserDao();
		return wProcessUserDao.existsProcessUser(instance.getIdProcess(), instance.getUser().getId());

	}
	
	private void _redundancyDataControl(WProcessUser instance)
			throws WProcessUserException {

		boolean existProcessUser = this.existsProcessUser(instance);

		if (existProcessUser) {
			throw new WProcessUserException(
					"The process user you are trying to add EXISTS YET");
		}

	}

}