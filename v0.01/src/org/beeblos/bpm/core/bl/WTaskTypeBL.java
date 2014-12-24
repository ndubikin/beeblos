package org.beeblos.bpm.core.bl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WTaskTypeDao;
import org.beeblos.bpm.core.error.WTaskTypeException;
import org.beeblos.bpm.core.model.WTaskType;

import com.sp.common.util.StringPair;

public class WTaskTypeBL {

	private static final Log logger = LogFactory.getLog(WTaskTypeBL.class.getName());
	
	public WTaskTypeBL (){
		
	}
		
	
	public Integer add(WTaskType taskType, Integer currentUser) throws WTaskTypeException {
		
		logger.debug("add() WTaskType - Name: ["+taskType.getName()+"]");
		
		return new WTaskTypeDao().add(taskType);

	}
	
	
	public void update(WTaskType taskType, Integer currentUser) throws WTaskTypeException {
		
		logger.debug("update() WTaskType < id = "+taskType.getId()+">");
		
		if (!taskType.equals(new WTaskTypeDao().getWTaskTypeByPK(taskType.getId())) ) {

			new WTaskTypeDao().update(taskType);
			
		} else {
			
			logger.debug("WTaskTypeBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WTaskType taskType, Integer currentUser) throws WTaskTypeException {

		logger.debug("delete() WTaskType - Name: ["+taskType.getName()+"]");
		
		new WTaskTypeDao().delete(taskType);

	}

	public WTaskType getWTaskTypeByPK(Integer id, Integer currentUser) throws WTaskTypeException {

		return new WTaskTypeDao().getWTaskTypeByPK(id);
	}
	
	
	public WTaskType getWTaskTypeByName(String name, Integer currentUser) throws WTaskTypeException {

		return new WTaskTypeDao().getWTaskTypeByName(name);
	}

	
	public List<WTaskType> getWTaskTypeList(Integer currentUser) throws WTaskTypeException {

		return new WTaskTypeDao().getWTaskTypeList();
	
	}

	public List<StringPair> getComboList(
			String firstLineText, String blank )
	throws WTaskTypeException {
		 
		return new WTaskTypeDao()
						.getComboList(firstLineText, blank); 


	}
}
