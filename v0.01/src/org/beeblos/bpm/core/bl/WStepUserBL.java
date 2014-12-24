package org.beeblos.bpm.core.bl;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepUserDao;
import org.beeblos.bpm.core.error.WStepUserException;
import org.beeblos.bpm.core.model.WStepUser;
import org.beeblos.bpm.core.model.WUserDef;
import org.joda.time.DateTime;

public class WStepUserBL {

	private static final Log logger = LogFactory
			.getLog(WStepUserBL.class);

	public WStepUserBL() {

	}

	/**
	 * Adds the new relation between the idStep and the user id and returns the
	 * complete relation's object.
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param idStep
	 *            the step id
	 * @param idUser
	 *            the user id
	 * @param isAdmin
	 *            the is admin
	 * @param idCurrentUser
	 *            the id current user
	 * @return the w step user
	 * @throws WStepUserException
	 *             the w step user exception
	 */
	public WStepUser addNewStepUser(Integer idStep, Integer idUser, boolean isAdmin, Integer idCurrentUser)
			throws WStepUserException {

		logger.debug("WStepUserBL:addNewStepUser() with idStep = " + idStep
				+ " and idUser = " + idUser);

		WStepUser instance = new WStepUser();
		instance.setIdStep(idStep);
		instance.setUser(new WUserDef(idUser));
		instance.setAdmin(isAdmin);
		
		Integer idStepUser = this.add(instance, idCurrentUser);

		if (idStepUser != null){
			return this.getStepUserByPK(idStepUser);
		}
		
		return null;
		
	}

	public Integer add(WStepUser instance, Integer idCurrentUser)
			throws WStepUserException {

		logger.debug("WStepUserBL:add()");

		_consistencyDataControl(instance);
		_redundancyDataControl(instance);

		instance.setInsertDate(new DateTime());
		instance.setInsertUser(idCurrentUser);
		
		WStepUserDao wStepUserDao = new WStepUserDao();

		return wStepUserDao.add(instance);

	}

	public void update(WStepUser instance, Integer idCurrentUser)
			throws WStepUserException {

		logger.debug("WStepUserBL:update()");

		WStepUserDao wStepUserDao = new WStepUserDao();

		_consistencyDataControl(instance);

		wStepUserDao.update(instance);

	}

	/**
	 * Deletes the WStepUser.
	 * 
	 * IMPORTANT: This should only be called from the method "deleteStepRelatedUser"
	 * of WStepDefBL because it is the correct way of deleting relations between users
	 * and stepes
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param instance
	 *            the instance
	 * @param idCurrentUser
	 *            the id current user
	 * @throws WStepUserException
	 *             the w step user exception
	 */
	public void delete(WStepUser instance, Integer idCurrentUser)
			throws WStepUserException {

		logger.debug("WStepUserBL:delete()");

		WStepUserDao wStepUserDao = new WStepUserDao();
		wStepUserDao.delete(instance);

	}

	public WStepUser getStepUserByPK(Integer id)
			throws WStepUserException {

		WStepUserDao wStepUserDao = new WStepUserDao();
		return wStepUserDao.getStepUserByPK(id);

	}

    private void _consistencyDataControl(WStepUser instance)
			throws WStepUserException {

		Integer idStepTmp = instance.getIdStep();
		Integer idUserTmp = instance.getUser().getId();

		if (idStepTmp == null || idStepTmp.equals(0)) {
			throw new WStepUserException(
					"The step user you are trying to add HAS NOT A VALID step id.");
		}

		if (idUserTmp == null || idUserTmp.equals(0)) {
			throw new WStepUserException(
					"The step user you are trying to add HAS NOT A VALID user id.");
		}

	}

	public boolean existsStepUser(WStepUser instance)throws WStepUserException {

		if (instance == null 
				|| instance.getIdStep() == null || instance.getIdStep().equals(0)
				|| instance.getUser() == null || instance.getUser().getId().equals(0)) {
			throw new WStepUserException(
					"The step user is not valid!");
		}

		WStepUserDao wStepUserDao = new WStepUserDao();
		return wStepUserDao.existsStepUser(instance.getIdStep(), instance.getUser().getId());

	}
	
	private void _redundancyDataControl(WStepUser instance)
			throws WStepUserException {

		boolean existStepUser = this.existsStepUser(instance);

		if (existStepUser) {
			throw new WStepUserException(
					"The step user you are trying to add EXISTS YET");
		}

	}

}