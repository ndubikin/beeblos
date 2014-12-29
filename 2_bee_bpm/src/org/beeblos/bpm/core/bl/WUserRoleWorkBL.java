package org.beeblos.bpm.core.bl;


import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WUserRoleWorkDao;
import org.beeblos.bpm.core.error.WUserRoleWorkException;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.WUserRoleWork;
import org.joda.time.DateTime;

/**
 * manages user/role/processwork runtime roles
 * 
 * @author nes 20141215
 *
 */
public class WUserRoleWorkBL {

	private static final Log logger = LogFactory
			.getLog(WUserRoleWorkBL.class);

	public WUserRoleWorkBL() {

	}

	/**
	 * Adds a new relation between the user / role / processWork for a runtime role
	 * @author nes 20141215
	 *
	 */
	public WUserRoleWork addNewUserRole(Integer idUser, Integer idRole, Integer idProcessWork, Integer idCurrentUser)
			throws WUserRoleWorkException {

		logger.debug("WUserRoleWorkBL() add with idUser:" + (idUser!=null?idUser:"null")
				+ " and idRole:" + (idRole!=null?idRole:"null")
				+ " and idProcessWork:" + (idProcessWork!=null?idProcessWork:"null"));

		WUserRoleWork instance = new WUserRoleWork(null,idUser,idRole,idProcessWork);

		
		Integer id = this.add(instance, idCurrentUser);

		if (id != null){
			return this.getUserRoleWorkByPK(id);
		}
		
		return null;
		
	}

	/**
	 * add a new WUserRoleWork object...
	 * 
	 * @param instance
	 * @param idCurrentUser
	 * @return
	 * @throws WUserRoleWorkException
	 */
	public Integer add(WUserRoleWork instance, Integer idCurrentUser)
			throws WUserRoleWorkException {

		logger.debug(">>> add() instance:"+(instance!=null?instance.toString():"null"));


		instance.setInsertDate(new DateTime());
		instance.setInsertUser(idCurrentUser);
		instance.setModDate( DEFAULT_MOD_DATE_TIME );

		WUserRoleWorkDao wUserRoleWorkDao = new WUserRoleWorkDao();

		return wUserRoleWorkDao.add(instance);

	}

	/**
	 * add a list of WUserRoleWork objects...
	 * 
	 * @param instance[]
	 * @param idCurrentUser
	 * @return
	 * @throws WUserRoleWorkException
	 */
	public Integer add(List<WUserRoleWork> instanceList, Integer idCurrentUser)
			 {

		logger.debug(">>> add() list of WUserRoleWork qty:"+(instanceList!=null?instanceList.size():"null"));

		Integer qty=0;

		for (WUserRoleWork instance: instanceList) {
			try {
				this.add(instance, idCurrentUser);
				qty++;
			} catch (WUserRoleWorkException e) {
				String mess = "Can't add instance of WUserRoleWork:" + (instance!=null?instance.toString():"null")
						+e.getMessage()+" "
						+(e.getCause()!=null?e.getCause():"null");
				logger.error(mess);
			}
			
		}

		/**
		 * returns qty of records added ...
		 */
		return qty;

	}
	
	/**
	 * no deberia ser necesario hacer update sobre estos registros de relacion
	 * solo alta o baja ...
	 * nes 20141215
	 * 
	 * @param instance
	 * @param idCurrentUser
	 * @throws WUserRoleWorkException
	 */
	public void update(WUserRoleWork instance, Integer idCurrentUser)
			throws WUserRoleWorkException {

		logger.debug("WUserRoleBL:update()");

		WUserRoleWorkDao wUserRoleWorkDao = new WUserRoleWorkDao();

		wUserRoleWorkDao.update(instance);

	}

	/**
	 * Deletes the WUserRoleWork.
	 */
	public void delete(WUserRoleWork instance, Integer idCurrentUser)
			throws WUserRoleWorkException {

		logger.debug(">>> delete() instance:"+(instance!=null?instance.toString():"null"));

		WUserRoleWorkDao wUserRoleWorkDao = new WUserRoleWorkDao();
		wUserRoleWorkDao.delete(instance);

	}

	public WUserRoleWork getUserRoleWorkByPK(Integer id)
			throws WUserRoleWorkException {

		WUserRoleWorkDao wUserRoleWorkDao = new WUserRoleWorkDao();
		return wUserRoleWorkDao.getUserRoleWorkByPK(id);

	}
	
	/**
	 * Returns a list of users with permissions for given ProcessWork and Role
	 * @param idRole
	 * @param idProcessWork
	 * @param currentUserId
	 * @return
	 * @throws WUserRoleWorkException 
	 */
	public List<WUserDef> getUserDefListByRole(Integer idRole, Integer idProcessWork, Integer currentUserId) 
			throws WUserRoleWorkException{
		WUserRoleWorkDao wUserRoleWorkDao = new WUserRoleWorkDao();
		return wUserRoleWorkDao.getUserDefListByRole(idRole, idProcessWork);
	}

}