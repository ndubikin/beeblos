package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DELETED_BOOL;
import static org.beeblos.bpm.core.util.Constants.ALL;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.DevelopmentException;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepHeadException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.md.TableManagerBL;
import org.beeblos.bpm.core.md.impl.TableManagerBLImpl;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WStepWorkSequence;



public class DevelopmentBL {
	
	private static final Log logger = LogFactory.getLog(DevelopmentBL.class.getName());
	
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
	 * @throws WProcessDefException 
	 * 
	 */
	public boolean purgeProcessDefList(List<WProcessDef> processDefList, Integer currentUserId) 
			throws DevelopmentException {

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
	 * This method clean all work data and obsolete data (as deleted sequences or step def) for a given
	 * process definition (processDef)
	 * 
	 * @param WProcessDef processDef
	 * @param Integer currentUserId
	 * 
	 * @return boolean
	 * @throws WProcessDefException 
	 * @throws DevelopmentException 
	 * 
	 */
	public boolean purgeProcessDef(WProcessDef processDef, Integer currentUserId) throws DevelopmentException {
		
		logger.info("DevelopmentBL.purgeProcessDef() - Beginning purge of:"+(processDef!=null?processDef.getId():"null")); 
		
		if (processDef == null || processDef.getId() == null || processDef.getId().equals(0)){
			throw new DevelopmentException("Can't purge null process!!");
		}
		
		boolean returnValue = true;

		try {
			
			this._deleteWStepWorkSequenceList(processDef.getId(), currentUserId);
			
			this._deleteWStepWorkList(processDef.getId(), currentUserId);
			
			this._deleteWProcessWorkAndWMTList(processDef.getId(), currentUserId);
			
			this._deleteWStepSequenceDefsAndStepDefs(processDef.getId(), processDef.getProcess().getId(), currentUserId);

		} catch (WStepWorkSequenceException e) {
			throw new DevelopmentException("Error trying to purge processDef [id: " + processDef.getId() + "] -" + e.getMessage());
		} catch (WProcessDefException e) {
			throw new DevelopmentException("Error trying to purge processDef [id: " + processDef.getId() + "] -" + e.getMessage());
		} catch (WStepDefException e) {
			throw new DevelopmentException("Error trying to purge processDef [id: " + processDef.getId() + "] -" + e.getMessage());
		} catch (WStepWorkException e) {
			throw new DevelopmentException("Error trying to purge processDef [id: " + processDef.getId() + "] -" + e.getMessage());
		} catch (WProcessWorkException e) {
			throw new DevelopmentException("Error trying to purge processDef [id: " + processDef.getId() + "] -" + e.getMessage());
		} catch (WStepSequenceDefException e) {
			throw new DevelopmentException("Error trying to purge processDef [id: " + processDef.getId() + "] -" + e.getMessage());
		} catch (WStepHeadException e) {
			throw new DevelopmentException("Error trying to purge processDef [id: " + processDef.getId() + "] -" + e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new DevelopmentException("Error trying to purge processDef [id: " + processDef.getId() + "] -" + e.getMessage());
		} catch (SQLException e) {
			throw new DevelopmentException("Error trying to purge processDef [id: " + processDef.getId() + "] -" + e.getMessage());
		}
		
		return returnValue;
		
	}
	
	/**
	 * @author dmuleiro - 20130829
	 * 
	 * Deletes the List<WStepWorkSequence> related with the WProcessDef with "processId"
	 *
	 * @param  Integer processDefId
	 * @param  Integer currentUserId
	 * 
	 * @return void
	 * 
	 * @throws WStepWorkSequenceException
	 * 
	 */
	private void _deleteWStepWorkSequenceList(Integer processDefId, Integer currentUserId) throws WStepWorkSequenceException{
		
		logger.info("DevelopmentBL._deleteWStepWorkSequenceList() - deleting step work sequences related to process : " + processDefId); 

		WStepWorkSequenceBL wswsBL = new WStepWorkSequenceBL();
		
		List<WStepWorkSequence> stepWorkSequenceList = wswsBL.getWStepWorkSequencesByProcessId(processDefId, currentUserId);
		
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
	 * @param  Integer processDefId
	 * @param  Integer currentUserId
	 * 
	 * @return void
	 * 
	 * @throws WStepWorkException 
	 * @throws WStepDefException 
	 * @throws WProcessDefException 
	 * 
	 */
	private void _deleteWStepWorkList(Integer processDefId, Integer currentUserId) 
			throws WProcessDefException, WStepDefException, WStepWorkException {
		
		logger.info("DevelopmentBL._deleteWStepWorkSequenceList() - deleting step works related to process : " + processDefId); 

		WStepWorkBL wswBL = new WStepWorkBL();
		
		List<WStepWork> stepWorkList = wswBL.getWorkListByProcessAndStatus(processDefId, null, currentUserId);
		
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
	 * @param  Integer processDefId
	 * @param  Integer currentUserId
	 * 
	 * @return void
	 * 
	 * @throws WProcessWorkException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * 
	 */
	private void _deleteWProcessWorkAndWMTList(Integer processDefId, Integer currentUserId) throws WProcessWorkException, ClassNotFoundException, SQLException {
		
		logger.info("DevelopmentBL._deleteWStepWorkSequenceList() - deleting process works related to process : " + processDefId); 

		WProcessWorkBL wpwBL = new WProcessWorkBL();
		TableManagerBL tmBL = new TableManagerBLImpl();
		
		List<WProcessWork> processWorkList = wpwBL.getWProcessWorkListByProcessId(processDefId, currentUserId);
		
		if (processWorkList == null || processWorkList.isEmpty()){
			return;
		}

		for (WProcessWork processWork : processWorkList){
			
			if (processWork.getManagedTableConfiguration() != null){
				tmBL.deleteRecord(processWork.getManagedTableConfiguration().getSchema(),
						processWork.getManagedTableConfiguration().getName(),
								processWork.getId());
			}
			
			wpwBL.delete(processWork, currentUserId);
			
		}
		
	}
		
	/**
	 * @author dmuleiro - 20130829
	 * 
	 * Deletes the List<WStepSequenceDef> related with the WProcessDef with "processId"
	 *
	 * @param  Integer processDefId
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
	private void _deleteWStepSequenceDefsAndStepDefs(Integer processDefId, Integer processHeadId, Integer currentUserId) 
			throws WStepSequenceDefException, WStepWorkSequenceException, WStepDefException, WStepWorkException, WProcessDefException, WStepHeadException {
		
		logger.info("DevelopmentBL._deleteWStepSequenceDefAndStepDefsList() - deleting step sequence defs (checked as 'deleted') related to process : " + processDefId); 

		WStepSequenceDefBL wpsdBL = new WStepSequenceDefBL();
		WStepDefBL wpdBL = new WStepDefBL();
		
		// antes de borrar las rutas vemos los steps relacionados con el proceso para borrarlos despues de las secuencias		
		List<WStepDef> stepDefList = wpdBL.getStepDefs(processDefId, DELETED_BOOL, currentUserId);
		
		List<WStepSequenceDef> stepSequenceDefList = wpsdBL.getStepSequenceList(processDefId, ALL, currentUserId);// REVISAR NES 20130925
		
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
	