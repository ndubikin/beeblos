package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WUserDefDao;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.error.WUserDeviceException;
import org.beeblos.bpm.core.error.WUserRoleException;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.WUserDevice;
import org.beeblos.bpm.core.model.WUserRole;
import org.joda.time.DateTime;

import com.sp.common.util.StringPair;



public class WUserDefBL {
	
	private static final Log logger = LogFactory.getLog(WUserDefBL.class.getName());
	
	public WUserDefBL (){
		
	}
	
	public Integer add(WUserDef user, Integer currentUser) throws WUserDefException {
		
		logger.debug("add() WUserDef - Name: ["+user.getName()+"]");
		
		// timestamp & trace info
		user.setInsertDate(new DateTime());
		user.setModDate(DEFAULT_MOD_DATE_TIME);
		user.setInsertUser(currentUser);
		user.setModUser(currentUser);
		return new WUserDefDao().add(user);

	}
	
	
	public void update(WUserDef user, Integer currentUser) throws WUserDefException {
		
		logger.debug("update() WUserDef < id = "+user.getId()+">");
		
		if (!user.equals(new WUserDefDao().getWUserDefByPK(user.getId())) ) {

			// timestamp & trace info
			user.setModDate(new DateTime());
			user.setModUser(currentUser);
			new WUserDefDao().update(user);
			
		} else {
			
			logger.debug("WUserDefBL.update - nothing to do ...");
		}
		

					
	}
	
	public void delete(Integer userId, Integer currentUser) throws WUserDefException {

		logger.debug("delete() WUserDef - userId: ["+userId+"]");
		
		new WUserDefDao().delete(userId);

	}
	
	public void delete(WUserDef user, Integer currentUser) throws WUserDefException {

		logger.debug("delete() WUserDef - Name: ["+user.getName()+"]");
		
		new WUserDefDao().delete(user);

	}

	public WUserDef getWUserDefByPK(Integer id, Integer currentUser) throws WUserDefException {

		return new WUserDefDao().getWUserDefByPK(id);
	}

	//rrl 20111220
	public WUserDef getWUserDefByPK(Integer id) throws WUserDefException {

		return new WUserDefDao().getWUserDefByPK(id);
	}
	
	public WUserDef getWUserDefByName(String name, Integer currentUser) throws WUserDefException {

		return new WUserDefDao().getWUserDefByName(name);
	}

	public List<WUserDef> getWUserDefs(Integer currentUser) throws WUserDefException {

		return new WUserDefDao().getWUserDefs();
	
	}
	
	//rrl 20111220
	public List<WUserDef> getWUserDefs() throws WUserDefException {

		return new WUserDefDao().getWUserDefs();
	}
	
	
	 /**
	   * returns the user list for a given Role
	   * orderBy: id (user) or name
	   */
	public List<WUserDef> getWUserDefByRole( Integer idRole, String orderBy ) throws WUserDefException {
		
		try {
			return new WUserRoleBL().getUserDefListByRole(idRole, orderBy);
		} catch (WUserRoleException e) {
			throw new WUserDefException(e);
		}

	}

	 /**
	   * returns the ID user list for a given Role
	   * orderBy: id (user) or name
	   */
	public List<Integer> getWUserDefIdByRole( Integer idRole ) throws WUserDefException {

		try {
			return new WUserRoleBL().getUserDefIdsByRole(idRole);
		} catch (WUserRoleException e) {
			throw new WUserDefException(e);
		}
	}
	
	/**
	 * Gets the WUserDef list where its ids are into pkList
	 * 
	 * @author dmuleiro 20160525
	 * 
	 * @param pkList
	 * @param currentUserId
	 * @return
	 * @throws WUserDefException
	 *
	 */
	public List<WUserDef> getWUserDefByPkList(List<String> pkList, Integer currentUserId) throws WUserDefException {

		return new WUserDefDao().getWUserDefByPkList(pkList);

	}
	
	/**
	 * Gets the WUserDef list where its ids are into pkArray
	 * 
	 * @author dmuleiro 20160525
	 * 
	 * @param pkList
	 * @param currentUserId
	 * @return
	 * @throws WUserDefException
	 *
	 */
	public List<WUserDef> getWUserDefByPkArray(Integer[] pkArray, Integer currentUserId) throws WUserDefException {

		if (pkArray == null){
			return null;
		}
		
		List<String> idList = new ArrayList<String>();
		for (Integer id: pkArray) {
			idList.add(id.toString());
		}
		
		return new WUserDefDao().getWUserDefByPkList(idList);

	}

	
	/**
	 * Adds the user related role to the user "rolesRelated" list and returns the same user
	 * with the new value added.
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param userDef
	 *            the user def
	 * @param idRole
	 *            the role id
	 * @param active
	 *            the is active
	 * @param idCurrentUser
	 *            the id current user
	 * @throws WUserDefException
	 *             the w user def exception
	 */
	public WUserDef addUserRelatedRole(WUserDef userDef, Integer idRole, boolean active, Integer idCurrentUser) throws WUserDefException{
		
		if (userDef == null || userDef.getId() == null || userDef.getId().equals(0)){
			throw new WUserDefException("addUserRelatedRole(): The user is not valid!");
		}
		
		if (idRole == null || idRole.equals(0)){
			throw new WUserDefException("addUserRelatedRole(): The role id is not valid!");
		}
		
		try {
			WUserRole userRole = 
					new WUserRoleBL().addNewUserRole(userDef.getId(), idRole, active, idCurrentUser);
			
			if (userRole != null){
				
				if (userDef.getRolesRelated() == null){
					userDef.setRolesRelated(new HashSet<WUserRole>());
				}
				userDef.getRolesRelated().add(userRole);
				
			}
			
		} catch (WUserRoleException e) {
			String mess = "addUserRelatedRole(): Error trying to create the user role. "
					+ e.getMessage() + (e.getCause()!=null?e.getCause():"");
			logger.error(mess);
			throw new WUserDefException(mess);
		}
		
		return userDef;
		
	}
	
	/**
	 * Deletes the user related role from the user "rolesRelated" list and returns the same user
	 * without the new value added.
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param userDef
	 *            the user def
	 * @param idRole
	 *            the role id
	 * @param isAdmin
	 *            the is admin
	 * @param idCurrentUser
	 *            the id current user
	 * @throws WUserDefException
	 *             the w user def exception
	 */
	public WUserDef deleteUserRelatedRole(WUserDef userDef, Integer idRole, Integer idCurrentUser) throws WUserDefException{
		
		if (userDef == null || userDef.getId() == null || userDef.getId().equals(0)){
			throw new WUserDefException("addUserRelatedRole(): The user is not valid!");
		}
		
		if (idRole == null || idRole.equals(0)){
			throw new WUserDefException("addUserRelatedRole(): The role id is not valid!");
		}
		
		WUserRole userRoleToDelete = null;
		if (userDef != null && userDef.getRolesRelated() != null){
			for (WUserRole userRole : userDef.getRolesRelated()){
				if (userRole != null && userRole.getRole() != null
						&& userRole.getRole() != null
						&& userRole.getRole().getId().equals(idRole)){
					userRoleToDelete = userRole;
					break;
				}
			}
		}
		
		if (userRoleToDelete == null){
			throw new WUserDefException("The relation between the role with id: " + idRole
					+ " and the user: " + userDef.getId() + " does not exist.");
		}
		
		userDef.getRolesRelated().remove(userRoleToDelete);

		try {
			
			new WUserRoleBL().delete(userRoleToDelete, idCurrentUser);
			
		} catch (WUserRoleException e) {
			String mess = "deleteUserRelatedRole(): Error trying to delete the user role. "
					+ e.getMessage() + (e.getCause()!=null?e.getCause():"");
			logger.error(mess);
			throw new WUserDefException(mess);
		}
		
		return userDef;
		
	}

	// dml 20120423
	public List<WUserDef> getWUserDefList( WUserDef wUserDef ) throws WUserDefException {

		return new WUserDefDao().getWUserDefList( wUserDef );
	}
	
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WUserDefException {
		 
		return new WUserDefDao().getComboList(textoPrimeraLinea, separacion);
	}
	
	/**
	 * Get all the devices for the user id.
	 * 
	 * pab 20160322
	 * 
	 * @param userId
	 * @return
	 * @throws WUserDeviceException
	 */
	public List<WUserDevice> getUserDevicesList(String userId)
		throws WUserDeviceException {
		return new WUserDeviceBL().getUserDeviceListByUserId(userId); 
	}

}
	