package org.beeblos.bpm.core.bl;

import java.util.Date;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessDefDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.noper.StringPair;



public class WProcessDefBL {
	
	private static final Log logger = LogFactory.getLog(WProcessDefBL.class.getName());
	
	public WProcessDefBL (){
		
	}
	
	public Integer add(WProcessDef process, Integer currentUser) throws WProcessDefException {
		
		logger.debug("add() WProcessDef - Name: ["+process.getName()+"]");
		
		// timestamp & trace info
		process.setInsertDate(new Date());
		process.setModDate(new Date());
		process.setInsertUser(currentUser);
		process.setModUser(currentUser);
		return new WProcessDefDao().add(process);

	}
	
	
	public void update(WProcessDef process, Integer currentUser) throws WProcessDefException {
		
		logger.debug("update() WProcessDef < id = "+process.getId()+">");
		
		if (!process.equals(new WProcessDefDao().getWProcessDefByPK(process.getId())) ) {

			// timestamp & trace info
			process.setModDate(new Date());
			process.setModUser(currentUser);
			new WProcessDefDao().update(process);
			
		} else {
			
			logger.debug("WProcessDefBL.update - nothing to do ...");
		}
		

					
	}
	
	
	public void delete(WProcessDef process, Integer currentUser) throws WProcessDefException {

		logger.debug("delete() WProcessDef - Name: ["+process.getName()+"]");
		
		new WProcessDefDao().delete(process);

	}

	public WProcessDef getWProcessDefByPK(Integer id, Integer currentUser) throws WProcessDefException {

		return new WProcessDefDao().getWProcessDefByPK(id);
	}
	
	
	public WProcessDef getWProcessDefByName(String name, Integer currentUser) throws WProcessDefException {

		return new WProcessDefDao().getWProcessDefByName(name);
	}

	
	public List<WProcessDef> getWProcessDefs(Integer currentUser) throws WProcessDefException {

		return new WProcessDefDao().getWProcessDefs();
	
	}
	
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WProcessDefException {
		 
		return new WProcessDefDao().getComboList(textoPrimeraLinea, separacion);


	}

}
	