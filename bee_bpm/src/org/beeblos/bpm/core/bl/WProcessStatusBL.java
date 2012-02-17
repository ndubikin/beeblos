package org.beeblos.bpm.core.bl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessStatusDao;
import org.beeblos.bpm.core.error.WProcessStatusException;
import org.beeblos.bpm.core.model.WProcessStatus;



public class WProcessStatusBL {
	
	private static final Log logger = LogFactory.getLog(WProcessStatusBL.class.getName());
	
	public WProcessStatusBL (){
		
	}
	
	public Integer add(WProcessStatus status, Integer currentUser) throws WProcessStatusException {
		
		logger.debug("add() WProcessStatus - Name: ["+status.getName()+"]");
		
		return new WProcessStatusDao().add(status);

	}
	
	
	public void update(WProcessStatus status, Integer currentUser) throws WProcessStatusException {
		
		logger.debug("update() WProcessStatus < id = "+status.getId()+">");
		
		if (!status.equals(new WProcessStatusDao().getWProcessStatusByPK(status.getId())) ) {

			new WProcessStatusDao().update(status);
			
		} else {
			
			logger.debug("WProcessStatusBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WProcessStatus status, Integer currentUser) throws WProcessStatusException {

		logger.debug("delete() WProcessStatus - Name: ["+status.getName()+"]");
		
		new WProcessStatusDao().delete(status);

	}

	public WProcessStatus getWProcessStatusByPK(Integer id, Integer currentUser) throws WProcessStatusException {

		return new WProcessStatusDao().getWProcessStatusByPK(id);
	}
	
	
	public WProcessStatus getWProcessStatusByName(String name, Integer currentUser) throws WProcessStatusException {

		return new WProcessStatusDao().getWProcessStatusByName(name);
	}

	
	public List<WProcessStatus> getWProcessStatusList(Integer currentUser) throws WProcessStatusException {

		return new WProcessStatusDao().getWProcessStatusList();
	
	}
	
}
	