package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessDefDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;
import org.beeblos.bpm.core.model.noper.StepWorkLight;
import org.beeblos.bpm.core.model.noper.ProcessWorkLight;



public class WProcessDefBL {
	
	private static final Log logger = LogFactory.getLog(WProcessDefBL.class.getName());
	
	public WProcessDefBL (){
		
	}
	
	public Integer add(WProcessDef process, Integer currentUser) throws WProcessDefException {
		
		logger.debug("add() WProcessDef - Name: ["+process.getName()+"]");
		
		// timestamp & trace info
		process.setInsertDate(new Date());
		process.setModDate( DEFAULT_MOD_DATE );
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

	// dml 20120120
	public List<StringPair> getComboActiveProcessList(String firstLineText, String blank )
	throws WProcessDefException {
	
		return new WProcessDefDao().getComboActiveProcessList(firstLineText, blank);
	
	}	
	
	/*	
	public List<WProcessDef> finder(Date initialInsertDateFilter, Date finalInsertDateFilter, 
			boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
			String listZoneFilter, String workZoneFilter, String additinalZoneFilter)
	throws WProcessDefException {
		 
		return new WProcessDefDao().finder(initialInsertDateFilter, finalInsertDateFilter, strictInsertDateFilter, 
				nameFilter, commentFilter, listZoneFilter, workZoneFilter, additinalZoneFilter);

	}
*/	
	public List<WProcessDef> getProcessListByFinder (Date initialInsertDateFilter, Date finalInsertDateFilter, 
			boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
			String listZoneFilter, String workZoneFilter, String additinalZoneFilter,
			Integer userId, boolean isAdmin ) 
	throws WProcessDefException {
		
		return new WProcessDefDao().getProcessListByFinder(initialInsertDateFilter, finalInsertDateFilter, 
				strictInsertDateFilter, nameFilter, commentFilter, listZoneFilter, 
				workZoneFilter, additinalZoneFilter, userId, isAdmin);

	}

	public List<WProcessDefLight> getWorkingProcessListFinder(boolean onlyWorkingProcessesFilter, 
			String processNameFilter, Date initialProductionDateFilter, Date finalProductionDateFilter, 
			boolean estrictProductionDateFilter, Integer productionUserFilter, String action)
	throws WProcessDefException {
		
		return new WProcessDefDao().getWorkingProcessListFinder(onlyWorkingProcessesFilter, 
				processNameFilter, initialProductionDateFilter, finalProductionDateFilter,
				estrictProductionDateFilter, productionUserFilter, action);
		
	}

}
	