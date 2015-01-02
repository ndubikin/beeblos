package org.beeblos.bpm.core.bl;


import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepRoleDao;
import org.beeblos.bpm.core.error.WStepRoleException;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WRoleDef;
import org.joda.time.DateTime;

public class WStepRoleBL {

	private static final Log logger = LogFactory
			.getLog(WStepRoleBL.class);

	public WStepRoleBL() {

	}

	/**
	 * Add a new relation between the Step and the Role and returns added object
	 * 
	 * @author nes 20141229
	 *
	 * @param idStep
	 *            the step id
	 * @param idRole
	 *            the role id
	 * @param isAdmin
	 *            the is admin
	 * @param idCurrentUser
	 *            the id current user
	 * @return the w step role
	 * @throws WStepRoleException
	 *             the w step role exception
	 */
	public WStepRole addNewStepRoleX(Integer idStep, Integer idRole, boolean isAdmin, Integer idCurrentUser)
			throws WStepRoleException {

		logger.debug("WStepRoleBL:addNewStepRole() with idStep = " + idStep
				+ " and idRole = " + idRole);

		Integer idStepRole = this.addNewStepRole(idStep, idRole, isAdmin, idCurrentUser);
		if (idStepRole!=null) {
			return getStepRoleByPK(idStepRole);
		}
		return null;
	}
	
	/**
	 * Add a new relation between the Step and the Role and returns id of added object
	 * 
	 * @author dmuleiro 20141031 // nes 20141229
	 *
	 * @param idStep
	 *            the step id
	 * @param idRole
	 *            the role id
	 * @param isAdmin
	 *            the is admin
	 * @param idCurrentUser
	 *            the id current user
	 * @return the w step role
	 * @throws WStepRoleException
	 *             the w step role exception
	 */
	public Integer addNewStepRole(Integer idStep, Integer idRole, boolean isAdmin, Integer idCurrentUser)
			throws WStepRoleException {

		logger.debug("WStepRoleBL:addNewStepRole() with idStep = " + idStep
				+ " and idRole = " + idRole);

		WStepRole instance = new WStepRole();
		instance.setIdStep(idStep);
		instance.setRole(new WRoleDef(idRole));
		instance.setAdmin(isAdmin);
		
		Integer idStepRole = this.add(instance, idCurrentUser);
		

/*		if (idStepRole != null){
			return this.getStepRoleByPK(idStepRole);
		}
		
		return null;*/
		return idStepRole;
	}

	public Integer add(WStepRole instance, Integer idCurrentUser)
			throws WStepRoleException {

		logger.debug("WStepRoleBL:add()");

		_consistencyDataControl(instance);
		_redundancyDataControl(instance);

		instance.setInsertDate(new DateTime());
		instance.setInsertUser(idCurrentUser);

		WStepRoleDao wStepRoleDao = new WStepRoleDao();

		return wStepRoleDao.add(instance);

	}

	public void update(WStepRole instance, Integer idCurrentUser)
			throws WStepRoleException {

		logger.debug("WStepRoleBL:update()");

		WStepRoleDao wStepRoleDao = new WStepRoleDao();

		_consistencyDataControl(instance);

		wStepRoleDao.update(instance);

	}

	/**
	 * Deletes the WStepRole.
	 * 
	 * IMPORTANT: This should only be called from the method "deleteStepRelatedRole"
	 * of WStepDefBL because it is the correct way of deleting relations between roles
	 * and stepes
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param instance
	 *            the instance
	 * @param idCurrentUser
	 *            the id current user
	 * @throws WStepRoleException
	 *             the w step role exception
	 */
	public void delete(WStepRole instance, Integer idCurrentUser)
			throws WStepRoleException {

		logger.debug("WStepRoleBL:delete()");

		WStepRoleDao wStepRoleDao = new WStepRoleDao();
		wStepRoleDao.delete(instance);

	}

	public WStepRole getStepRoleByPK(Integer id)
			throws WStepRoleException {

		WStepRoleDao wStepRoleDao = new WStepRoleDao();
		return wStepRoleDao.getStepRoleByPK(id);

	}

    private void _consistencyDataControl(WStepRole instance)
			throws WStepRoleException {

		Integer idStepTmp = instance.getIdStep();
		Integer idRoleTmp = instance.getRole().getId();

		if (idStepTmp == null || idStepTmp.equals(0)) {
			throw new WStepRoleException(
					"The step role you are trying to add HAS NOT A VALID step id.");
		}

		if (idRoleTmp == null || idRoleTmp.equals(0)) {
			throw new WStepRoleException(
					"The step role you are trying to add HAS NOT A VALID role id.");
		}

	}

	public boolean existsStepRole(WStepRole instance)throws WStepRoleException {

		if (instance == null 
				|| instance.getIdStep() == null || instance.getIdStep().equals(0)
				|| instance.getRole() == null || instance.getRole().getId().equals(0)) {
			throw new WStepRoleException(
					"The step role is not valid!");
		}

		WStepRoleDao wStepRoleDao = new WStepRoleDao();
		return wStepRoleDao.existsStepRole(instance.getIdStep(), instance.getRole().getId());

	}
	
	private void _redundancyDataControl(WStepRole instance)
			throws WStepRoleException {

		boolean existStepRole = this.existsStepRole(instance);

		if (existStepRole) {
			throw new WStepRoleException(
					"The step role you are trying to add EXISTS YET");
		}

	}

}