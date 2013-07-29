package org.beeblos.bpm.core.bl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessDataFieldDao;
import org.beeblos.bpm.core.error.WProcessDataFieldException;
import org.beeblos.bpm.core.model.WProcessDataField;



public class WProcessDataFieldBL {
	
	private static final Log logger = LogFactory.getLog(WProcessDataFieldBL.class.getName());
	
	public WProcessDataFieldBL (){
		
	}
	
	public Integer add(WProcessDataField processDataField, Integer currentUser) throws WProcessDataFieldException {
		
		logger.debug("add() WProcessDataField - Name: ["+processDataField.getName()+"]");
		
		return new WProcessDataFieldDao().add(processDataField);

	}
	
	
	public void update(WProcessDataField processDataField, Integer currentUser) throws WProcessDataFieldException {
		
		logger.debug("update() WProcessDataField < id = "+processDataField.getId()+">");
		
		if (!processDataField.equals(new WProcessDataFieldDao().getWProcessDataFieldByPK(processDataField.getId())) ) {

			new WProcessDataFieldDao().update(processDataField);
			
		} else {
			
			logger.debug("WProcessDataFieldBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WProcessDataField processDataField, Integer currentUser) throws WProcessDataFieldException {

		logger.debug("delete() WProcessDataField - Name: ["+processDataField.getName()+"]");
		
		new WProcessDataFieldDao().delete(processDataField);

	}

	public WProcessDataField getWProcessDataFieldByPK(Integer id, Integer currentUser) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getWProcessDataFieldByPK(id);
	}
	
	
	public WProcessDataField getWProcessDataFieldByName(String name, Integer currentUser) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getWProcessDataFieldByName(name);
	}

	
	public List<WProcessDataField> getWProcessDataFieldList(Integer currentUser) throws WProcessDataFieldException {

		return new WProcessDataFieldDao().getWProcessDataFieldList();
	
	}
	
}
	