package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessDataFieldDao;
import org.beeblos.bpm.core.error.WProcessDataFieldException;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.noper.StringPair;



public class WProcessDataFieldBL {
	
	private static final Log logger = LogFactory.getLog(WProcessDataFieldBL.class.getName());
	
	public WProcessDataFieldBL (){
		
	}
	
	public Integer add(WProcessDataField processDataField, Integer currentUserId) throws WProcessDataFieldException {
		
		logger.debug("add() WProcessDataField - Name: ["+processDataField.getName()+"]");
		
		// timestamp & trace info
		processDataField.setInsertDate(new Date());
		processDataField.setModDate( DEFAULT_MOD_DATE );
		processDataField.setInsertUser(currentUserId);
		processDataField.setModUser(currentUserId);
		
		return new WProcessDataFieldDao().add(processDataField);

	}
	
	
	public void update(WProcessDataField processDataField, Integer currentUserId) throws WProcessDataFieldException {
		
		logger.debug("update() WProcessDataField < id = "+processDataField.getId()+">");
		
		if (!processDataField.equals(new WProcessDataFieldDao().getWProcessDataFieldByPK(processDataField.getId())) ) {

			// timestamp & trace info
			processDataField.setModDate(new Date());
			processDataField.setModUser(currentUserId);
			
			new WProcessDataFieldDao().update(processDataField);
			
		} else {
			
			logger.debug("WProcessDataFieldBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WProcessDataField processDataField, Integer currentUserId) throws WProcessDataFieldException {

		logger.debug("delete() WProcessDataField - Name: ["+processDataField.getName()+"]");
		
		new WProcessDataFieldDao().delete(processDataField);

	}

	public WProcessDataField getWProcessDataFieldByPK(Integer id, Integer currentUserId) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getWProcessDataFieldByPK(id);
	}
	
	
	public WProcessDataField getWProcessDataFieldByName(String name, Integer currentUserId) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getWProcessDataFieldByName(name);
	}

	
	public List<WProcessDataField> getWProcessDataFieldList(Integer currentUserId) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getWProcessDataFieldList();
	
	}

	public List<WProcessDataField> getWProcessDataFieldList(Integer processHeadId, Integer currentUserId) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getWProcessDataFieldList(processHeadId);
	
	}
	
	//rrl 20130801
	public List<StringPair> getComboList(String firstLineText, String separationLine, Integer processHeadId ) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getComboList(firstLineText, separationLine, processHeadId);
	}
	
	
}
	