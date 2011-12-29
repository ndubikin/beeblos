package org.beeblos.bpm.core.bl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WUserDefDao;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.noper.StringPair;



public class WUserDefBL {
	
	private static final Log logger = LogFactory.getLog(WUserDefBL.class);
	
	public WUserDefBL (){
		
	}
	
	public Integer add(WUserDef user, Integer currentUser) throws WUserDefException {
		
		logger.debug("add() WUserDef - Name: ["+user.getName()+"]");
		
		// timestamp & trace info
//		user.setInsertDate(new Date());
//		user.setModDate(new Date());
//		user.setInsertUser(currentUser);
//		user.setModUser(null);
		return new WUserDefDao().add(user);

	}
	
	
	public void update(WUserDef user, Integer currentUser) throws WUserDefException {
		
		logger.debug("update() WUserDef < id = "+user.getId()+">");
		
		if (!user.equals(new WUserDefDao().getWUserDefByPK(user.getId())) ) {

			// timestamp & trace info
//			user.setModDate(new Date());
//			user.setModUser(currentUser);
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
		
		return new WUserDefDao().getWUserDefByRole(idRole, orderBy);

	}

	 /**
	   * returns the ID user list for a given Role
	   * orderBy: id (user) or name
	   */
	public List<Integer> getWUserDefIdByRole( Integer idRole ) throws WUserDefException {

		return new WUserDefDao().getWUserDefIdByRole(idRole);
	}
	
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WUserDefException {
		 
		return new WUserDefDao().getComboList(textoPrimeraLinea, separacion);


	}

}
	