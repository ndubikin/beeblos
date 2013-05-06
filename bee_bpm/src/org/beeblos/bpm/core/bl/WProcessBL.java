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
	
	public Integer add(WProcess processHead, Integer currentUserId) throws WProcessException, WProcessDefException {
		
		logger.debug("add() WProcess - Name: ["+processHead.getName()+"]");
		
		// timestamp & trace info
		processHead.setInsertDate(new Date());
		processHead.setModDate( DEFAULT_MOD_DATE );
		processHead.setInsertUser(currentUserId);
		processHead.setModUser(currentUserId);

		return new WProcessDao().add(processHead);

	}
		
	// dml 20130506
	public Integer addProcessAndFirstWProcessDef(WProcess processHead, Integer currentUserId) throws WProcessException, WProcessDefException {
		
		logger.debug("addProcessAndFirstWProcessDef() WProcess - Name: ["+processHead.getName()+"]");
		
		Integer processHeadId = this.add(processHead, currentUserId);
		processHead.setId(processHeadId);
		
		new WProcessDefBL().createFirstWProcessDef(processHeadId, currentUserId);
		
		return processHeadId;

	}
		
	public void update(WProcess processHead, Integer currentUserId) throws WProcessException {
		
		logger.debug("update() WProcess < id = "+processHead.getId()+">");
		
		if (!processHead.equals(new WProcessDao().getWProcessByPK(processHead.getId())) ) {

			// timestamp & trace info
			processHead.setModDate(new Date());
			processHead.setModUser(currentUserId);
			new WProcessDao().update(processHead);
			
		} else {
			
			logger.debug("WProcessBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WProcess processHead, Integer currentUserId) throws WProcessException {

		logger.debug("delete() WProcess - Name: ["+processHead.getName()+"]");
		
		new WProcessDao().delete(processHead);

	}

	public WProcess getProcessByPK(Integer id, Integer currentUserId) throws WProcessException {

		return new WProcessDao().getWProcessByPK(id);
	}
	
	
	public WProcess getProcessByName(String name, Integer currentUserId) throws WProcessException {

		return new WProcessDao().getWProcessByName(name);
	}

	public String getProcessName(Integer id) throws WProcessDefException {
		return new WProcessDao().getProcessName(id);
	}
	
	
	public List<WProcess> getProcessList(Integer currentUserId) throws WProcessException {

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
	