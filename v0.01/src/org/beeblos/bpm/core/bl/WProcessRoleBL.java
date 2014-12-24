package org.beeblos.bpm.core.bl;


import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessRoleDao;
import org.beeblos.bpm.core.error.WProcessRoleException;
import org.beeblos.bpm.core.model.WProcessRole;
import org.beeblos.bpm.core.model.WRoleDef;
import org.joda.time.DateTime;

public class WProcessRoleBL {

	private static final Log logger = LogFactory
			.getLog(WProcessRoleBL.class);

	public WProcessRoleBL() {

	}

	/**
	 * Adds the new relation between the idProcess and the role id and returns the
	 * complete relation's object.
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param idProcess
	 *            the process id
	 * @param idRole
	 *            the role id
	 * @param isAdmin
	 *            the is admin
	 * @param idCurrentUser
	 *            the id current user
	 * @return the w process role
	 * @throws WProcessRoleException
	 *             the w process role exception
	 */
	public WProcessRole addNewProcessRole(Integer idProcess, Integer idRole, boolean isAdmin, Integer idCurrentUser)
			throws WProcessRoleException {

		logger.debug("WProcessRoleBL:addNewProcessRole() with idProcess = " + idProcess
				+ " and idRole = " + idRole);

		WProcessRole instance = new WProcessRole();
		instance.setIdProcess(idProcess);
		instance.setRole(new WRoleDef(idRole));
		instance.setAdmin(isAdmin);
		
		Integer idProcessRole = this.add(instance, idCurrentUser);

		if (idProcessRole != null){
			return this.getProcessRoleByPK(idProcessRole);
		}
		
		return null;
		
	}

	public Integer add(WProcessRole instance, Integer idCurrentUser)
			throws WProcessRoleException {

		logger.debug("WProcessRoleBL:add()");

		_consistencyDataControl(instance);
		_redundancyDataControl(instance);

		instance.setInsertDate(new DateTime());
		instance.setInsertUser(idCurrentUser);

		WProcessRoleDao wProcessRoleDao = new WProcessRoleDao();

		return wProcessRoleDao.add(instance);

	}

	public void update(WProcessRole instance, Integer idCurrentUser)
			throws WProcessRoleException {

		logger.debug("WProcessRoleBL:update()");

		WProcessRoleDao wProcessRoleDao = new WProcessRoleDao();

		_consistencyDataControl(instance);

		wProcessRoleDao.update(instance);

	}

	/**
	 * Deletes the WProcessRole.
	 * 
	 * IMPORTANT: This should only be called from the method "deleteProcessRelatedRole"
	 * of WProcessDefBL because it is the correct way of deleting relations between roles
	 * and processes
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param instance
	 *            the instance
	 * @param idCurrentUser
	 *            the id current user
	 * @throws WProcessRoleException
	 *             the w process role exception
	 */
	public void delete(WProcessRole instance, Integer idCurrentUser)
			throws WProcessRoleException {

		logger.debug("WProcessRoleBL:delete()");

		WProcessRoleDao wProcessRoleDao = new WProcessRoleDao();
		wProcessRoleDao.delete(instance);

	}

	public WProcessRole getProcessRoleByPK(Integer id)
			throws WProcessRoleException {

		WProcessRoleDao wProcessRoleDao = new WProcessRoleDao();
		return wProcessRoleDao.getProcessRoleByPK(id);

	}

    private void _consistencyDataControl(WProcessRole instance)
			throws WProcessRoleException {

		Integer idProcessTmp = instance.getIdProcess();
		Integer idRoleTmp = instance.getRole().getId();

		if (idProcessTmp == null || idProcessTmp.equals(0)) {
			throw new WProcessRoleException(
					"The process role you are trying to add HAS NOT A VALID process id.");
		}

		if (idRoleTmp == null || idRoleTmp.equals(0)) {
			throw new WProcessRoleException(
					"The process role you are trying to add HAS NOT A VALID role id.");
		}

	}

	public boolean existsProcessRole(WProcessRole instance)throws WProcessRoleException {

		if (instance == null 
				|| instance.getIdProcess() == null || instance.getIdProcess().equals(0)
				|| instance.getRole() == null || instance.getRole().getId().equals(0)) {
			throw new WProcessRoleException(
					"The process role is not valid!");
		}

		WProcessRoleDao wProcessRoleDao = new WProcessRoleDao();
		return wProcessRoleDao.existsProcessRole(instance.getIdProcess(), instance.getRole().getId());

	}
	
	private void _redundancyDataControl(WProcessRole instance)
			throws WProcessRoleException {

		boolean existProcessRole = this.existsProcessRole(instance);

		if (existProcessRole) {
			throw new WProcessRoleException(
					"The process role you are trying to add EXISTS YET");
		}

	}

}