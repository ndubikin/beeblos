package org.beeblos.bpm.core.bl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WDataTypeDao;
import org.beeblos.bpm.core.error.WDataTypeException;

import com.sp.common.model.WDataType;
import com.sp.common.util.StringPair;



public class WDataTypeBL {
	
	private static final Log logger = LogFactory.getLog(WDataTypeBL.class.getName());
	
	public WDataTypeBL (){
		
	}
	
	public Integer add(WDataType dataType, Integer currentUser) throws WDataTypeException {
		
		logger.debug("add() WDataType - Name: ["+dataType.getName()+"]");
		
		return new WDataTypeDao().add(dataType);

	}
	
	
	public void update(WDataType dataType, Integer currentUser) throws WDataTypeException {
		
		logger.debug("update() WDataType < id = "+dataType.getId()+">");
		
		if (!dataType.equals(new WDataTypeDao().getWDataTypeByPK(dataType.getId())) ) {

			new WDataTypeDao().update(dataType);
			
		} else {
			
			logger.debug("WDataTypeBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WDataType dataType, Integer currentUser) throws WDataTypeException {

		logger.debug("delete() WDataType - Name: ["+dataType.getName()+"]");
		
		new WDataTypeDao().delete(dataType);

	}

	public WDataType getWDataTypeByPK(Integer id, Integer currentUser) throws WDataTypeException {

		return new WDataTypeDao().getWDataTypeByPK(id);
	}
	
	
	public WDataType getWDataTypeByName(String name, Integer currentUser) throws WDataTypeException {

		return new WDataTypeDao().getWDataTypeByName(name);
	}

	
	public List<WDataType> getWDataTypeList(Integer currentUser) throws WDataTypeException {

		return new WDataTypeDao().getWDataTypeList();
	
	}

	public List<StringPair> getComboList(
			String firstLineText, String blank )
	throws WDataTypeException {
		 
		return new WDataTypeDao()
						.getComboList(firstLineText, blank); 


	}
	
}
	
