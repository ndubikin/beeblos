package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;
import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessDefDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessException;
import org.beeblos.bpm.core.model.WProcess;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;
import org.beeblos.bpm.core.model.noper.StepWorkLight;
import org.beeblos.bpm.core.model.noper.ProcessWorkLight;



public class WProcessDefBL {
	
	private static final Log logger = LogFactory.getLog(WProcessDefBL.class.getName());
	
	public WProcessDefBL (){
		
	}
	
	public Integer add(WProcessDef process, Integer currentUser) throws WProcessDefException, WProcessException {
		
		logger.debug("add() WProcessDef - Name: ["+process.getName()+"]");
		
		// dml 20130430 - si es un nuevo WProcess se guarda antes de guardar el WProcessDef
		if (process.getProcess() != null
				&& (process.getProcess().getId() == null
				|| process.getProcess().getId().equals(0))){
			
			Integer processId = new WProcessBL().add(process.getProcess(), currentUser);
			process.getProcess().setId(processId);
			
		}
		
		// timestamp & trace info
		process.setInsertDate(new Date());
		process.setModDate( DEFAULT_MOD_DATE );
		process.setInsertUser(currentUser);
		process.setModUser(currentUser);
		return new WProcessDefDao().add(process);

	}
	
	// dml 20130430
	public void createFirstWProcessDef(WProcess process, Integer currentUser) throws WProcessDefException, WProcessException{
		
		WProcessDef wpd = new WProcessDef();
		wpd.setActive(true);

		wpd.setProcess(process);
		wpd.setVersion(1);
		
		this.add(wpd, currentUser);
		
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
	
	// dml 20130430
	public Integer getLastWProcessDefVersion(Integer processId) throws WProcessDefException {

		return new WProcessDefDao().getLastWProcessDefVersion(processId);
	
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
	
	public List<WProcessDef> getProcessListByFinder (Date initialInsertDateFilter, Date finalInsertDateFilter, 
			boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
			String listZoneFilter, String workZoneFilter, String additinalZoneFilter,
			Integer userId, boolean isAdmin, String action ) 
	throws WProcessDefException {
		
		return new WProcessDefDao().getProcessListByFinder(initialInsertDateFilter, finalInsertDateFilter, 
				strictInsertDateFilter, nameFilter, commentFilter, listZoneFilter, 
				workZoneFilter, additinalZoneFilter, userId, isAdmin, action);

	}

	public List<WProcessDefLight> getWorkingProcessListFinder(boolean onlyWorkingProcessesFilter, 
			String processNameFilter, Date initialProductionDateFilter, Date finalProductionDateFilter, 
			boolean estrictProductionDateFilter, Integer productionUserFilter, String action)
	throws WProcessDefException {
		
		return new WProcessDefDao().getWorkingProcessListFinder(onlyWorkingProcessesFilter, 
				processNameFilter, initialProductionDateFilter, finalProductionDateFilter,
				estrictProductionDateFilter, productionUserFilter, action);
		
	}

	// dml 20130129
	public boolean userIsProcessAdmin(Integer userId, Integer processId) throws WProcessDefException{
		
		return new WProcessDefDao().userIsProcessAdmin(userId, processId);
		
	}
}
	