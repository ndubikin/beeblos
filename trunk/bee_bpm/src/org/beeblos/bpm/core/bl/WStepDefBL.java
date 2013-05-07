package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepDefDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.noper.StringPair;



public class WStepDefBL {
	
	private static final Log logger = LogFactory.getLog(WStepDefBL.class.getName());
	
	public WStepDefBL (){
		
	}
	
	public Integer add(WStepDef step, Integer currentUser) throws WStepDefException {
		
		logger.debug("add() WStepDef - Name: ["+step.getName()+"]");
		
		// timestamp & trace info
		step.setInsertDate(new Date());
		step.setModDate( DEFAULT_MOD_DATE );
		step.setInsertUser(currentUser);
		step.setModUser(currentUser);
		return new WStepDefDao().add(step);

	}
	
	
	public void update(WStepDef step, Integer currentUser) throws WStepDefException {
		
		logger.debug("update() WStepDef < id = "+step.getId()+">");
		
		if (!step.equals(new WStepDefDao().getStepDefByPK(step.getId())) ) {

			// timestamp & trace info
			step.setModDate(new Date());
			step.setModUser(currentUser);
			new WStepDefDao().update(step);
			
		} else {
			
			logger.debug("WStepDefBL.update - nothing to do ...");
		}
		

					
	}
	
	
	public void delete(WStepDef step, Integer currentUser) throws WStepDefException {

		logger.info("delete() WStepDef - Name: ["+step.getName()+"] id:["+step.getId()+"]");
		
		new WStepDefDao().delete(step);

	}

	public void deleteStepRole(WStepDef step, WStepRole wsr) throws WStepDefException {

//		logger.debug("delete() WStepDef - Name: ["+step.getName()+"] id:["+step.getId()+"] wsteprole.role:"+wsr.getRole().getId()+" wsteprolw.step:"+wsr.getStep().getId());
		
//		if ( wsr.getStep().getId() != step.getId() ) {
//			throw new WStepDefException("can't delete wsteprole id:"+wsr.getStep().getId()+ " for this step with id:"+step.getId() );
//		}
		
		new WStepDefDao().deleteStepRole(step,wsr);

	}
	
	public WStepDef getWStepDefByPK(Integer id, Integer userId) throws WStepDefException {

		return new WStepDefDao().getStepDefByPK(id);
	}
	
	
	public WStepDef getWStepDefByName(String name, Integer userId) throws WStepDefException {

		return new WStepDefDao().getStepDefByName(name);
	}

	
	public List<WStepDef> getStepDefs(Integer userId) throws WStepDefException {

		// nota: falta revisar el tema de los permisos de usuario para esto ...
		return new WStepDefDao().getWStepDefs();
	
	}
	
	public List<WStepDef> getStepDefs(Integer processId, Integer userId) throws WStepDefException {

		return new WStepDefDao().getStepDefs(processId);
	
	}
	
	// dml 20130507
	public List<Integer> getProcessIdList(Integer stepId, Integer userId) throws WStepDefException{
		
		return new WStepDefDao().getProcessIdList(stepId);
		
	}
	
	// dml 20130507
	public boolean isAnotherProcessUsingStep(Integer stepId, Integer processId, Integer userId) 
			throws WStepDefException, WProcessDefException {

		if (stepId == null
				|| stepId.equals(0)
				|| processId == null
				|| processId.equals(0)){
			String mess = "stepId and processId cannot be null or zero";
			logger.error(mess);
			throw new WProcessDefException(mess);
		}

		List<Integer> processIdList = this.getProcessIdList(stepId, userId);
		
		if (processIdList == null){
			String mess = "Error trying to retrieve the processIdList:"+processId;
			logger.error(mess);
			throw new WProcessDefException(mess);
		}
			
		for (Integer pid : processIdList){
			
			if (!pid.equals(processId)){
				return true;
			}
			
		} 

		return false;
	
	}	
	
	public List<StringPair> getComboList(
			String firstLineText, String blank )
	throws WStepDefException {
		 
		return new WStepDefDao().getComboList(firstLineText, blank);


	}
	
	public List<StringPair> getComboList(
			Integer processId, String firstLineText, String blank )
	throws WProcessDefException {
		
		return new WStepDefDao().getComboList(processId, firstLineText, blank);
		
	}

	// dml 20130129 - new combo method with userId and allItems
	public List<StringPair> getComboList(
			Integer processId, Integer versionId, Integer userId, boolean allItems, 
			String firstLineText, String blank )
	throws WProcessDefException {
		
		// dml 20130129 - checking if the user is process admin
		boolean userIsProcessAdmin = new WProcessDefBL().userIsProcessAdmin(userId, processId);

		return new WStepDefDao().getComboList(processId, versionId, userId, userIsProcessAdmin, allItems, 
				firstLineText, blank);
		
	}

	public List<WStepDef> getStepListByFinder (String nameFilter, String commentFilter, 
			String instructionsFilter, Integer userId, boolean isAdmin, String action ) 
	throws WStepDefException {
		
		return new WStepDefDao().getStepListByFinder(nameFilter, commentFilter, instructionsFilter, 
				userId, isAdmin, action);

	}


}
	