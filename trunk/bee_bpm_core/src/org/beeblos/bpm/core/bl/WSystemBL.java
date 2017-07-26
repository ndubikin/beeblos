package org.beeblos.bpm.core.bl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WSystemDao;
import org.beeblos.bpm.core.error.WSystemException;
import org.beeblos.bpm.core.model.WSystem;

import com.sp.common.util.StringPair;

public class WSystemBL {
private static final Log logger = LogFactory.getLog(WSystemBL.class.getName());
	
	public WSystemBL (){
		
	}
	
	public Integer add(WSystem system, Integer currentUser) throws WSystemException {
		
		logger.debug("add() WSystem - Name: ["+system.getName()+"]");
		
		return new WSystemDao().add(system);

	}
	
	
	public void update(WSystem system, Integer currentUser) throws WSystemException {
		
		logger.debug("update() WSystem < id = "+system.getId()+">");
		
		if (!system.equals(new WSystemDao().getWSystemByPK(system.getId())) ) {

			new WSystemDao().update(system);
			
		} else {
			
			logger.debug("WSystemBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WSystem system, Integer currentUser) throws WSystemException {

		logger.debug("delete() WSystem - Name: ["+system.getName()+"]");
		
		new WSystemDao().delete(system);

	}

	public WSystem getWSystemByPK(Integer id, Integer currentUser) throws WSystemException {

		return new WSystemDao().getWSystemByPK(id);
	}
	
	
	public WSystem getWSystemByName(String name, Integer currentUser) throws WSystemException {

		return new WSystemDao().getWSystemByName(name);
	}

	
	public List<WSystem> getWSystemList(Integer currentUser) throws WSystemException {

		return new WSystemDao().getWSystemList();
	
	}

	public List<StringPair> getComboList(
			String firstLineText, String blank )
	throws WSystemException {
		 
		return new WSystemDao()
						.getComboList(firstLineText, blank); 


	}
}
