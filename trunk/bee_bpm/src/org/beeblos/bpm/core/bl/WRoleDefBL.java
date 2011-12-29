package org.beeblos.bpm.core.bl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WRoleDefDao;
import org.beeblos.bpm.core.error.WRoleDefException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.noper.StringPair;



public class WRoleDefBL {
	
	private static final Log logger = LogFactory.getLog(WRoleDefBL.class);
	
	public WRoleDefBL (){
		
	}
	
	public Integer add(WRoleDef role, Integer user) throws WRoleDefException {
		
		logger.debug("add() WRoleDef - Name: ["+role.getName()+"]");
		
		// timestamp & trace info
//		role.setInsertDate(new Date());
//		role.setModDate(new Date());
//		role.setInsertUser(user);
//		role.setModUser(null);
		return new WRoleDefDao().add(role);

	}
	
	
	public void update(WRoleDef role, Integer user) throws WRoleDefException {
		
		logger.debug("update() WRoleDef < id = "+role.getId()+">");
		
		if (!role.equals(new WRoleDefDao().getWRoleDefByPK(role.getId())) ) {

			// timestamp & trace info
//			role.setModDate(new Date());
//			role.setModUser(null);
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
	public List<WUserDef> getWUserDefByRole( Integer idRole, String orderBy ) throws WUserDefException {
		
		return new WRoleDefDao().getWUserDefByRole(idRole, orderBy);

	}

	 /**
	   * returns the ID user list for a given Role
	   * orderBy: id (user) or name
	   */
	public List<Integer> getWUserDefIdByRole( Integer idRole ) throws WUserDefException {

		return new WRoleDefDao().getWUserDefIdByRole(idRole);
	}
	
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WRoleDefException {
		 
		return new WRoleDefDao().getComboList(textoPrimeraLinea, separacion);


	}

}
	