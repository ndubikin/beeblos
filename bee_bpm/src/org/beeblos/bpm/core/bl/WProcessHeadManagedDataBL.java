package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessHeadManagedDataDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessException;
import org.beeblos.bpm.core.model.WProcessHeadManagedData;
import org.beeblos.bpm.core.model.noper.StringPair;



public class WProcessHeadManagedDataBL {
	
	private static final Log logger = LogFactory.getLog(WProcessHeadManagedDataBL.class.getName());
	
	public WProcessHeadManagedDataBL (){
		
	}
	
	public Integer add(WProcessHeadManagedData managedTableDef, Integer currentUserId) 
			throws WProcessException, WProcessDefException {
		
		logger.debug("add() WProcessHeadManagedData - Name: ["+managedTableDef.getName()+"]");
		
		if(managedTableDef.getIgnoreCase()==null) {
			managedTableDef.setIgnoreCase(true);
		}

		// timestamp & trace info
		managedTableDef.setInsertDate(new Date());
		managedTableDef.setModDate( DEFAULT_MOD_DATE );
		managedTableDef.setInsertUser(currentUserId);
		managedTableDef.setModUser(currentUserId);

		return new WProcessHeadManagedDataDao().add(managedTableDef, currentUserId);

	}
		
	public void update(WProcessHeadManagedData managedTableDef, Integer currentUserId) throws WProcessException {
		
		logger.debug("update() WProcessHeadManagedData < id = "+managedTableDef.getHeadId()+">");
		
		if (!managedTableDef.equals(
				new WProcessHeadManagedDataDao()
							.getWProcessHeadManagedTableByPK(managedTableDef.getHeadId(), currentUserId)) ) {

			// timestamp & trace info
			managedTableDef.setModDate(new Date());
			managedTableDef.setModUser(currentUserId);
			new WProcessHeadManagedDataDao().update(managedTableDef, currentUserId);
			
		} else {
			
			logger.debug("WProcessHeadBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WProcessHeadManagedData managedTableDef, Integer currentUserId) throws WProcessException {

		logger.debug("delete() WProcessHeadManagedData - Name: ["+managedTableDef.getName()+"]");
		
		new WProcessHeadManagedDataDao().delete(managedTableDef, currentUserId);

	}

	public WProcessHeadManagedData getWProcessHeadManagedTableByPK(Integer id, Integer currentUserId) throws WProcessException {

		return new WProcessHeadManagedDataDao().getWProcessHeadManagedTableByPK(id, currentUserId);
	}
	
	
	public WProcessHeadManagedData getWProcessHeadManagedTableByName(String name, Integer currentUserId) throws WProcessException {

		return new WProcessHeadManagedDataDao().getWProcessHeadManagedTableByName(name, currentUserId);
	}

	public String getTableName(Integer id, Integer currentUserId) throws WProcessDefException {
		return new WProcessHeadManagedDataDao().getTableName(id, currentUserId);
	}
	
	
	public List<WProcessHeadManagedData> getTableDefList(Integer currentUserId) throws WProcessException {

		return new WProcessHeadManagedDataDao().getTableDefList(currentUserId);
	
	}
	
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion, Integer currentUserId )
	throws WProcessException {
		 
		return new WProcessHeadManagedDataDao().getComboList(textoPrimeraLinea, separacion, currentUserId);
		
	}
	
}