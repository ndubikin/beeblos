package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessException;
import org.beeblos.bpm.core.model.WProcess;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;



public class WProcessBL {
	
	private static final Log logger = LogFactory.getLog(WProcessBL.class.getName());
	
	public WProcessBL (){
		
	}
	
	public Integer add(WProcess process, Integer currentUser) throws WProcessException, WProcessDefException {
		
		logger.debug("add() WProcess - Name: ["+process.getName()+"]");
		
		// timestamp & trace info
		process.setInsertDate(new Date());
		process.setModDate( DEFAULT_MOD_DATE );
		process.setInsertUser(currentUser);
		process.setModUser(currentUser);
		Integer processId = new WProcessDao().add(process);
		process.setId(processId);
		
		new WProcessDefBL().createFirstWProcessDef(process, currentUser);
		
		return processId;

	}
		
	public void update(WProcess process, Integer currentUser) throws WProcessException {
		
		logger.debug("update() WProcess < id = "+process.getId()+">");
		
		if (!process.equals(new WProcessDao().getWProcessByPK(process.getId())) ) {

			// timestamp & trace info
			process.setModDate(new Date());
			process.setModUser(currentUser);
			new WProcessDao().update(process);
			
		} else {
			
			logger.debug("WProcessBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WProcess process, Integer currentUser) throws WProcessException {

		logger.debug("delete() WProcess - Name: ["+process.getName()+"]");
		
		new WProcessDao().delete(process);

	}

	public WProcess getWProcessByPK(Integer id, Integer currentUser) throws WProcessException {

		return new WProcessDao().getWProcessByPK(id);
	}
	
	
	public WProcess getWProcessByName(String name, Integer currentUser) throws WProcessException {

		return new WProcessDao().getWProcessByName(name);
	}

	public String getProcessName(Integer id) throws WProcessDefException {
		return new WProcessDao().getProcessName(id);
	}
	
	
	public List<WProcess> getWProcessList(Integer currentUser) throws WProcessException {

		return new WProcessDao().getWProcessList();
	
	}
	
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WProcessException {
		 
		return new WProcessDao().getComboList(textoPrimeraLinea, separacion);
		
	}

	// dml 20120120
	public List<StringPair> getComboActiveProcessList(String firstLineText, String blank )
	throws WProcessException {
	
		return new WProcessDao().getComboActiveProcessList(firstLineText, blank);
	
	}	
	
	public List<WProcess> getProcessListByFinder (Date initialInsertDateFilter, Date finalInsertDateFilter, 
			boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
			String listZoneFilter, String workZoneFilter, String additinalZoneFilter,
			Integer userId, boolean isAdmin, String action ) 
	throws WProcessException {
		
		return new WProcessDao().getProcessListByFinder(initialInsertDateFilter, finalInsertDateFilter, 
				strictInsertDateFilter, nameFilter, commentFilter, listZoneFilter, 
				workZoneFilter, additinalZoneFilter, userId, isAdmin, action);

	}

	public List<WProcessDefLight> getWorkingProcessListFinder(boolean onlyWorkingProcessListFilter, 
			String processNameFilter, Date initialProductionDateFilter, Date finalProductionDateFilter, 
			boolean estrictProductionDateFilter, Integer productionUserFilter, String action)
	throws WProcessException {
		
		return new WProcessDao().getWorkingProcessListFinder(onlyWorkingProcessListFilter, 
				processNameFilter, initialProductionDateFilter, finalProductionDateFilter,
				estrictProductionDateFilter, productionUserFilter, action);
		
	}

	// dml 20130129
	public boolean userIsProcessAdmin(Integer userId, Integer processId) throws WProcessException{
		
		return new WProcessDao().userIsProcessAdmin(userId, processId);
		
	}
}
	