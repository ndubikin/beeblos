package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WRoleDefDao;
import org.beeblos.bpm.core.error.WRoleDefException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.error.WUserRoleException;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WUserRole;
import org.joda.time.DateTime;

import com.sp.common.util.StringPair;



public class WRoleDefBL {
	
	private static final Log logger = LogFactory.getLog(WRoleDefBL.class.getName());
	
	public WRoleDefBL (){
		
	}
	
	public Integer add(WRoleDef role, Integer user) throws WRoleDefException {
		
		logger.debug("add() WRoleDef - Name: ["+role.getName()+"]");
		
		// timestamp & trace info
		role.setInsertDate(new DateTime());
		role.setModDate( DEFAULT_MOD_DATE_TIME );
		role.setInsertUser(user);
		role.setModUser(user);
		return new WRoleDefDao().add(role);

	}
	
	
	public void update(WRoleDef role, Integer user) throws WRoleDefException {
		
		logger.debug("update() WRoleDef < id = "+role.getId()+">");
		
		if (!role.equals(new WRoleDefDao().getWRoleDefByPK(role.getId())) ) {

			// timestamp & trace info
			role.setModDate(new DateTime());
			role.setModUser(user);
			new WRoleDefDao().update(role);
			
		} else {
			
			logger.debug("WRoleDefBL.update - nothing to do ...");
		}
		

					
	}
	
	public void delete(Integer roleId, Integer user) throws WRoleDefException {

		logger.debug("delete() WRoleDef - roleId: ["+roleId+"]");
		
		new WRoleDefDao().delete(roleId);

	}
	
	public void delete(WRoleDef role, Integer user) throws WRoleDefException {

		logger.debug("delete() WRoleDef - Name: ["+role.getName()+"]");
		
		new WRoleDefDao().delete(role);

	}

	public WRoleDef getWRoleDefByPK(Integer id, Integer user) throws WRoleDefException {

		return new WRoleDefDao().getWRoleDefByPK(id);
	}
	
	
	public WRoleDef getWRoleDefByName(String name, Integer user) throws WRoleDefException {

		return new WRoleDefDao().getWRoleDefByName(name);
	}

	
	public List<WRoleDef> getWRoleDefs(Integer user) throws WRoleDefException {

		return new WRoleDefDao().getWRoleDefs();
	
	}
	
	 /**
	   * returns the user list for a given Role
	   * orderBy: id (user) or name
	   */
	public List<WRoleDef> getWRoleDefByUser( Integer idUser, String orderBy ) throws WRoleDefException {
		
		try {
			return new WUserRoleBL().getRoleDefListByUser(idUser, orderBy);
		} catch (WUserRoleException e) {
			throw new WRoleDefException(e);
		}

	}

	 /**
	   * returns the ID user list for a given Role
	   * orderBy: id (user) or name
	   */
	public List<Integer> getWRoleDefIdByUser( Integer idUser ) throws WRoleDefException {

		try {
			return new WUserRoleBL().getRoleDefIdsByUser(idUser);
		} catch (WUserRoleException e) {
			throw new WRoleDefException(e);
		}
	}
	
	/**
	 * Adds the role related user to the role "usersRelated" list and returns the same role
	 * with the new value added.
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param roleDef
	 *            the role def
	 * @param idUser
	 *            the user id
	 * @param active
	 *            the is active
	 * @param idCurrentUser
	 *            the id current user
	 * @throws WRoleDefException
	 *             the w role def exception
	 */
	public WRoleDef addRoleRelatedUser(WRoleDef roleDef, Integer idUser, boolean active, Integer idCurrentUser) throws WRoleDefException{
		
		if (roleDef == null || roleDef.getId() == null || roleDef.getId().equals(0)){
			throw new WRoleDefException("addRoleRelatedUser(): The role is not valid!");
		}
		
		if (idUser == null || idUser.equals(0)){
			throw new WRoleDefException("addRoleRelatedUser(): The user id is not valid!");
		}
		
		try {
			WUserRole roleUser = 
					new WUserRoleBL().addNewUserRole(idUser, roleDef.getId(), active, idCurrentUser);
			
			if (roleUser != null){
				
				if (roleDef.getUsersRelated() == null){
					roleDef.setUsersRelated(new HashSet<WUserRole>());
				}
				roleDef.getUsersRelated().add(roleUser);
				
			}
			
		} catch (WUserRoleException e) {
			String mess = "addRoleRelatedUser(): Error trying to create the role user. "
					+ e.getMessage() + (e.getCause()!=null?e.getCause():"");
			logger.error(mess);
			throw new WRoleDefException(mess);
		}
		
		return roleDef;
		
	}
	
	/**
	 * Deletes the role related user from the role "usersRelated" list and returns the same role
	 * without the new value added.
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param roleDef
	 *            the role def
	 * @param idUser
	 *            the user id
	 * @param isAdmin
	 *            the is admin
	 * @param idCurrentUser
	 *            the id current user
	 * @throws WRoleDefException
	 *             the w role def exception
	 */
	public WRoleDef deleteRoleRelatedUser(WRoleDef roleDef, Integer idUser, Integer idCurrentUser) throws WRoleDefException{
		
		if (roleDef == null || roleDef.getId() == null || roleDef.getId().equals(0)){
			throw new WRoleDefException("addRoleRelatedUser(): The role is not valid!");
		}
		
		if (idUser == null || idUser.equals(0)){
			throw new WRoleDefException("addRoleRelatedUser(): The user id is not valid!");
		}
		
		WUserRole roleUserToDelete = null;
		if (roleDef != null && roleDef.getUsersRelated() != null){
			for (WUserRole roleUser : roleDef.getUsersRelated()){
				if (roleUser != null && roleUser.getUser() != null
						&& roleUser.getUser() != null
						&& roleUser.getUser().getId().equals(idUser)){
					roleUserToDelete = roleUser;
					break;
				}
			}
		}
		
		if (roleUserToDelete == null){
			throw new WRoleDefException("The relation between the user with id: " + idUser
					+ " and the role: " + roleDef.getId() + " does not exist.");
		}
		
		roleDef.getUsersRelated().remove(roleUserToDelete);

		try {
			
			new WUserRoleBL().delete(roleUserToDelete, idCurrentUser);
			
		} catch (WUserRoleException e) {
			String mess = "deleteRoleRelatedUser(): Error trying to delete the role user. "
					+ e.getMessage() + (e.getCause()!=null?e.getCause():"");
			logger.error(mess);
			throw new WRoleDefException(mess);
		}
		
		return roleDef;
		
	}
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WRoleDefException {
		 
		return new WRoleDefDao().getComboList(textoPrimeraLinea, separacion);


	}

	// dml 20120425
	public List<WRoleDef> getWRoleDefListByUser( Integer idUser ) throws WUserDefException {
		
		return new WRoleDefDao().getWRoleDefListByUser(idUser);

	}

}
	