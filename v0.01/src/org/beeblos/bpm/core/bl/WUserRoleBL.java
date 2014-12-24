package org.beeblos.bpm.core.bl;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WUserRoleDao;
import org.beeblos.bpm.core.error.WUserRoleException;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.WUserRole;
import org.joda.time.DateTime;

public class WUserRoleBL {

	private static final Log logger = LogFactory
			.getLog(WUserRoleBL.class);

	public WUserRoleBL() {

	}

	/**
	 * Adds the new relation between the idUser and the role id and returns the
	 * complete relation's object.
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param idUser
	 *            the user id
	 * @param idRole
	 *            the role id
	 * @param isAdmin
	 *            the is admin
	 * @param idCurrentUser
	 *            the id current user
	 * @return the w user role
	 * @throws WUserRoleException
	 *             the w user role exception
	 */
	public WUserRole addNewUserRole(Integer idUser, Integer idRole, boolean active, Integer idCurrentUser)
			throws WUserRoleException {

		logger.debug("WUserRoleBL:addNewUserRole() with idUser = " + idUser
				+ " and idRole = " + idRole);

		WUserRole instance = new WUserRole();
		instance.setUser(new WUserDef(idUser));
		instance.setRole(new WRoleDef(idRole));
		instance.setActive(active);
		
		Integer idUserRole = this.add(instance, idCurrentUser);

		if (idUserRole != null){
			return this.getUserRoleByPK(idUserRole);
		}
		
		return null;
		
	}

	public Integer add(WUserRole instance, Integer idCurrentUser)
			throws WUserRoleException {

		logger.debug("WUserRoleBL:add()");

		_consistencyDataControl(instance);
		_redundancyDataControl(instance);

		instance.setInsertDate(new DateTime());
		instance.setInsertUser(idCurrentUser);

		WUserRoleDao wUserRoleDao = new WUserRoleDao();

		return wUserRoleDao.add(instance);

	}

	public void update(WUserRole instance, Integer idCurrentUser)
			throws WUserRoleException {

		logger.debug("WUserRoleBL:update()");

		WUserRoleDao wUserRoleDao = new WUserRoleDao();

		_consistencyDataControl(instance);

		wUserRoleDao.update(instance);

	}

	/**
	 * Deletes the WUserRole.
	 * 
	 * IMPORTANT: This should only be called from the method "deleteUserRelatedRole"
	 * of WUserDefBL because it is the correct way of deleting relations between roles
	 * and users
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param instance
	 *            the instance
	 * @param idCurrentUser
	 *            the id current user
	 * @throws WUserRoleException
	 *             the w user role exception
	 */
	public void delete(WUserRole instance, Integer idCurrentUser)
			throws WUserRoleException {

		logger.debug("WUserRoleBL:delete()");

		WUserRoleDao wUserRoleDao = new WUserRoleDao();
		wUserRoleDao.delete(instance);

	}

	public WUserRole getUserRoleByPK(Integer id)
			throws WUserRoleException {

		WUserRoleDao wUserRoleDao = new WUserRoleDao();
		return wUserRoleDao.getUserRoleByPK(id);

	}

	public List<WUserDef> getUserDefListByRole( Integer idRole, String orderBy )
			throws WUserRoleException {

		WUserRoleDao wUserRoleDao = new WUserRoleDao();
		return wUserRoleDao.getUserDefListByRole(idRole, orderBy);

	}

	public List<Integer> getUserDefIdsByRole( Integer idRole )
			throws WUserRoleException {

		WUserRoleDao wUserRoleDao = new WUserRoleDao();
		return wUserRoleDao.getUserDefIdsByRole(idRole);

	}

	public List<WRoleDef> getRoleDefListByUser( Integer idUser, String orderBy )
			throws WUserRoleException {

		WUserRoleDao wUserRoleDao = new WUserRoleDao();
		return wUserRoleDao.getRoleDefListByUser(idUser, orderBy);

	}

	public List<Integer> getRoleDefIdsByUser( Integer idUser )
			throws WUserRoleException {

		WUserRoleDao wUserRoleDao = new WUserRoleDao();
		return wUserRoleDao.getRoleDefIdsByUser(idUser);

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

	public boolean existsUserRole(WUserRole instance)throws WUserRoleException {

		if (instance == null || instance.getRole() == null || instance.getUser() == null 
				|| instance.getRole().getId() == null || instance.getRole().getId().equals(0)
				|| instance.getUser().getId() == null || instance.getUser().getId().equals(0)) {
			throw new WUserRoleException(
					"The user role is not valid!");
		}

		WUserRoleDao wUserRoleDao = new WUserRoleDao();
		return wUserRoleDao.existsUserRole(instance.getUser().getId(), instance.getRole().getId());

	}
	
	private void _redundancyDataControl(WUserRole instance)
			throws WUserRoleException {

		boolean existUserRole = this.existsUserRole(instance);

		if (existUserRole) {
			throw new WUserRoleException(
					"The user role you are trying to add EXISTS YET");
		}

	}

}