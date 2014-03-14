package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessHeadManagedDataDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessHeadException;
import org.beeblos.bpm.core.model.WProcessHeadManagedDataConfiguration;
import org.joda.time.DateTime;

import com.sp.common.util.StringPair;



public class WProcessHeadManagedDataBL {
	
	private static final Log logger = LogFactory.getLog(WProcessHeadManagedDataBL.class.getName());
	
	public WProcessHeadManagedDataBL (){
		
	}
	
	public Integer add(WProcessHeadManagedDataConfiguration managedTableDef, Integer currentUserId) 
			throws WProcessHeadException, WProcessDefException {
		
		logger.debug("add() WProcessHeadManagedDataConfiguration - Name: ["+managedTableDef.getName()+"]");
		
		if(managedTableDef.getIgnoreCase()==null) {
			managedTableDef.setIgnoreCase(true);
		}

		// timestamp & trace info
		managedTableDef.setInsertDate(new DateTime());
		managedTableDef.setModDate( DEFAULT_MOD_DATE_TIME );
		managedTableDef.setInsertUser(currentUserId);
		managedTableDef.setModUser(currentUserId);

		return new WProcessHeadManagedDataDao().add(managedTableDef, currentUserId);

	}
		
	public void update(WProcessHeadManagedDataConfiguration managedTableDef, Integer currentUserId) throws WProcessHeadException {
		
		logger.debug("update() WProcessHeadManagedDataConfiguration < id = "+managedTableDef.getHeadId()+">");
		
		if (!managedTableDef.equals(
				new WProcessHeadManagedDataDao()
							.getWProcessHeadManagedTableByPK(managedTableDef.getHeadId(), currentUserId)) ) {

			// timestamp & trace info
			managedTableDef.setModDate(new DateTime());
			managedTableDef.setModUser(currentUserId);
			new WProcessHeadManagedDataDao().update(managedTableDef, currentUserId);
			
		} else {
			
			logger.debug("WProcessHeadBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WProcessHeadManagedDataConfiguration managedTableDef, Integer currentUserId) throws WProcessHeadException {

		logger.debug("delete() WProcessHeadManagedDataConfiguration - Name: ["+managedTableDef.getName()+"]");
		
		new WProcessHeadManagedDataDao().delete(managedTableDef);

	}

	public WProcessHeadManagedDataConfiguration getWProcessHeadManagedTableByPK(Integer id, Integer currentUserId) throws WProcessHeadException {

		return new WProcessHeadManagedDataDao().getWProcessHeadManagedTableByPK(id, currentUserId);
	}
	
	
	public WProcessHeadManagedDataConfiguration getWProcessHeadManagedTableByName(String name, Integer currentUserId) throws WProcessHeadException {

		return new WProcessHeadManagedDataDao().getWProcessHeadManagedTableByName(name, currentUserId);
	}

	public String getTableName(Integer id, Integer currentUserId) throws WProcessDefException {
		return new WProcessHeadManagedDataDao().getTableName(id, currentUserId);
	}
	
	
	public List<WProcessHeadManagedDataConfiguration> getTableDefList(Integer currentUserId) throws WProcessHeadException {

		return new WProcessHeadManagedDataDao().getTableDefList(currentUserId);
	
	}
	
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion, Integer currentUserId )
	throws WProcessHeadException {
		 
		return new WProcessHeadManagedDataDao().getComboList(textoPrimeraLinea, separacion, currentUserId);
		
	}
	
}