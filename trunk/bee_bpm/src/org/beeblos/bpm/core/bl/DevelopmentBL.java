package org.beeblos.bpm.core.bl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepHeadException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WStepWorkSequence;



public class DevelopmentBL {
	
	private static final Log logger = LogFactory.getLog(DevelopmentBL.class.getName());
	
	private final static Boolean DELETED = true;
	
	public DevelopmentBL (){
		
	}
	
	/**
	 * @author dmuleiro - 20130829
	 * 
	 * This method has to delete old information from a WProcessDef list.
	 * 
	 * @param List<WProcessDef> processDefList
	 * @param Integer currentUserId
	 * 
	 * @return boolean
	 * 
	 */
	public boolean purgeProcessDefList(List<WProcessDef> processDefList, Integer currentUserId) {

		logger.info("DevelopmentBL.purgeProcessDef() - Begin Purge"); 
		
		boolean returnValue = true;

		if (processDefList == null || processDefList.isEmpty()){
			return false;
		}

		for (WProcessDef process : processDefList){
			
			this.purgeProcessDef(process, currentUserId);
		}
			
		return returnValue;
		
	}
	
	/**
	 * @author dmuleiro - 20130829
	 * 
	 * This method has to delete old information from a WProcessDef.
	 * 
	 * @param WProcessDef processDef
	 * @param Integer currentUserId
	 * 
	 * @return boolean
	 * 
	 */
	public boolean purgeProcessDef(WProcessDef processDef, Integer currentUserId) {
		
		logger.info("DevelopmentBL.purgeProcessDef() - Begin Purge"); 
		
		boolean returnValue = true;

		if (processDef == null || processDef.getId() == null || processDef.getId().equals(0)){
			return false;
		}

		try {
			
			this._deleteWStepWorkSequenceList(processDef.getId(), currentUserId);
			
			this._deleteWStepWorkList(processDef.getId(), currentUserId);
			
			this._deleteWProcessWorkList(processDef.getId(), currentUserId);
			
			this._deleteWStepSequenceDefsAndStepDefs(processDef.getId(), processDef.getProcess().getId(), currentUserId);

		} catch (WStepWorkSequenceException e) {
			e.printStackTrace();
		} catch (WProcessDefException e) {
			e.printStackTrace();
		} catch (WStepDefException e) {
			e.printStackTrace();
		} catch (WStepWorkException e) {
			e.printStackTrace();
		} catch (WProcessWorkException e) {
			e.printStackTrace();
		} catch (WStepSequenceDefException e) {
			e.printStackTrace();
		} catch (WStepHeadException e) {
			e.printStackTrace();
		}
		
		return returnValue;
		
	}
	
	/**
	 * @author dmuleiro - 20130829
	 * 
	 * Deletes the List<WStepWorkSequence> related with the WProcessDef with "processId"
	 *
	 * @param  Integer processId
	 * @param  Integer currentUserId
	 * 
	 * @return void
	 * 
	 * @throws WStepWorkSequenceException
	 * 
	 */
	private void _deleteWStepWorkSequenceList(Integer processId, Integer currentUserId) throws WStepWorkSequenceException{
		
		logger.info("DevelopmentBL._deleteWStepWorkSequenceList() - deleting step work sequences related to process : " + processId); 

		WStepWorkSequenceBL wswsBL = new WStepWorkSequenceBL();
		
		List<WStepWorkSequence> stepWorkSequenceList = wswsBL.getWStepWorkSequencesByProcessId(processId, currentUserId);
		
		if (stepWorkSequenceList == null || stepWorkSequenceList.isEmpty()){
			return;
		}

		for (WStepWorkSequence stepWorkSequence : stepWorkSequenceList){
			
			wswsBL.delete(stepWorkSequence, currentUserId);
			
		}
		
	}
	
	/**
	 * @author dmuleiro - 20130829
	 * 
	 * Deletes the List<WStepWork> related with the WProcessDef with "processId"
	 *
	 * @param  Integer processId
	 * @param  Integer currentUserId
	 * 
	 * @return void
	 * 
	 * @throws WStepWorkException 
	 * @throws WStepDefException 
	 * @throws WProcessDefException 
	 * 
	 */
	private void _deleteWStepWorkList(Integer processId, Integer currentUserId) 
			throws WProcessDefException, WStepDefException, WStepWorkException {
		
		logger.info("DevelopmentBL._deleteWStepWorkSequenceList() - deleting step works related to process : " + processId); 

		WStepWorkBL wswBL = new WStepWorkBL();
		
		List<WStepWork> stepWorkList = wswBL.getWorkListByProcessAndStatus(processId, null, currentUserId);
		
		if (stepWorkList == null || stepWorkList.isEmpty()){
			return;
		}

		for (WStepWork stepWork : stepWorkList){
			
			wswBL.delete(stepWork, currentUserId);
			
		}
		
	}
	
	/**
	 * @author dmuleiro - 20130829
	 * 
	 * Deletes the List<WProcessWork> related with the WProcessDef with "processId"
	 *
	 * @param  Integer processId
	 * @param  Integer currentUserId
	 * 
	 * @return void
	 * 
	 * @throws WProcessWorkException 
	 * 
	 */
	private void _deleteWProcessWorkList(Integer processId, Integer currentUserId) throws WProcessWorkException {
		
		logger.info("DevelopmentBL._deleteWStepWorkSequenceList() - deleting process works related to process : " + processId); 

		WProcessWorkBL wpwBL = new WProcessWorkBL();
		
		List<WProcessWork> processWorkList = wpwBL.getWProcessWorkListByProcessId(processId, currentUserId);
		
		if (processWorkList == null || processWorkList.isEmpty()){
			return;
		}

		for (WProcessWork processWork : processWorkList){
			
			wpwBL.delete(processWork, currentUserId);
			
		}
		
	}
		
	/**
	 * @author dmuleiro - 20130829
	 * 
	 * Deletes the List<WStepSequenceDef> related with the WProcessDef with "processId"
	 *
	 * @param  Integer processId
	 * @param  Integer currentUserId
	 * 
	 * @return void
	 * 
	 * @throws WStepWorkSequenceException 
	 * @throws WStepSequenceDefException 
	 * @throws WStepDefException 
	 * @throws WStepHeadException 
	 * @throws WProcessDefException 
	 * @throws WStepWorkException 
	 * 
	 */
	private void _deleteWStepSequenceDefsAndStepDefs(Integer processId, Integer processHeadId, Integer currentUserId) 
			throws WStepSequenceDefException, WStepWorkSequenceException, WStepDefException, WStepWorkException, WProcessDefException, WStepHeadException {
		
		logger.info("DevelopmentBL._deleteWStepSequenceDefAndStepDefsList() - deleting step sequence defs (checked as 'deleted') related to process : " + processId); 

		WStepSequenceDefBL wpsdBL = new WStepSequenceDefBL();
		WStepDefBL wpdBL = new WStepDefBL();
		
		// antes de borrar las rutas vemos los steps relacionados con el proceso para borrarlos despues de las secuencias		
		List<WStepDef> stepDefList = wpdBL.getStepDefs(processId, DELETED, currentUserId);
		
		List<WStepSequenceDef> stepSequenceDefList = wpsdBL.getStepSequenceList(processId, DELETED, currentUserId);
		
		if (stepSequenceDefList != null && !stepSequenceDefList.isEmpty()){
			for (WStepSequenceDef stepSequenceDef : stepSequenceDefList){				
				wpsdBL.deleteRoute(stepSequenceDef, currentUserId);				
			}
		}
		
		if (stepDefList != null && !stepDefList.isEmpty()){
			for (WStepDef stepDef : stepDefList){				
				wpdBL.delete(stepDef.getId(), processHeadId, currentUserId);				
			}
		}

	}
				
}
	