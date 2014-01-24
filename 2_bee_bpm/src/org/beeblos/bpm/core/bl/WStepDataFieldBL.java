package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepDataFieldDao;
import org.beeblos.bpm.core.error.WStepDataFieldException;
import org.beeblos.bpm.core.model.WStepDataField;



public class WStepDataFieldBL {
	
	private static final Log logger = LogFactory.getLog(WStepDataFieldBL.class.getName());
	
	public WStepDataFieldBL (){
		
	}
	
	public Integer add(WStepDataField processDataField, Integer currentUserId) throws WStepDataFieldException {
		
		logger.debug("add() WStepDataField - Name: ["+processDataField.getName()+"]");
		
		// timestamp & trace info
		processDataField.setInsertDate(new Date());
		processDataField.setModDate( DEFAULT_MOD_DATE );
		processDataField.setInsertUser(currentUserId);
		processDataField.setModUser(currentUserId);
		
		return new WStepDataFieldDao().add(processDataField);

	}
	
	
	public void update(WStepDataField processDataField, Integer currentUserId) throws WStepDataFieldException {
		
		logger.debug("update() WStepDataField < id = "+processDataField.getId()+">");
		
		if (!processDataField.equals(new WStepDataFieldDao().getWStepDataFieldByPK(processDataField.getId())) ) {

			// timestamp & trace info
			processDataField.setModDate(new Date());
			processDataField.setModUser(currentUserId);
			
			new WStepDataFieldDao().update(processDataField);
			
		} else {
			
			logger.debug("WStepDataFieldBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WStepDataField processDataField, Integer currentUserId) throws WStepDataFieldException {

		logger.debug("delete() WStepDataField - Name: ["+processDataField.getName()+"]");
		
		new WStepDataFieldDao().delete(processDataField);

	}

	public WStepDataField getWStepDataFieldByPK(Integer id, Integer currentUserId) throws WStepDataFieldException {

		return new WStepDataFieldDao().getWStepDataFieldByPK(id);
	}
	
	
	public WStepDataField getWStepDataFieldByName(String name, Integer currentUserId) throws WStepDataFieldException {

		return new WStepDataFieldDao().getWStepDataFieldByName(name);
	}
	
	public List<WStepDataField> getWStepDataFieldList(Integer currentUserId) throws WStepDataFieldException {

		return new WStepDataFieldDao().getWStepDataFieldList();
	
	}
	
	public Integer countWStepDataFieldList(Integer processHeadId, Integer currentUserId) 
			throws WStepDataFieldException {

		return new WStepDataFieldDao().countWStepDataFieldList(processHeadId);
	
	}
	
	public List<WStepDataField> getWStepDataFieldList(Integer processHeadId, Integer stepHeadId, Integer currentUserId) 
			throws WStepDataFieldException {

		return new WStepDataFieldDao().getWStepDataFieldList(processHeadId, stepHeadId);
	
	}
	
}
	