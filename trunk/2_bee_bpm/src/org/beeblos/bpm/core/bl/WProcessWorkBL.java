package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;
import static org.beeblos.bpm.core.util.Constants.FINISHED;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessWorkDao;
import org.beeblos.bpm.core.error.WProcessStatusException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.model.WProcessStatus;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.noper.ProcessWorkLight;
import org.joda.time.DateTime;



public class WProcessWorkBL {
	
	private static final Log logger = LogFactory.getLog(WProcessWorkBL.class.getName());
	
	public WProcessWorkBL (){
		
	}
	
	public Integer add(WProcessWork process, Integer currentUserId) throws WProcessWorkException {
		
		logger.debug("add() WProcessWork - Name: ["+process.getId()+"]");
		
		_setObjectType(process);
		// timestamp & trace info
		process.setInsertDate(new Date());
		process.setModDate( DEFAULT_MOD_DATE );
		process.setInsertUser(currentUserId);
		process.setModUser(currentUserId);
		if ( process.getStatus()==null ) {
			process.setStatus(new WProcessStatus(1));
		}
		return new WProcessWorkDao().add(process);

	}
	
	
	// nes 20130728 - por el tema de aceptar procesos genericos no relacionados con 1 objeto obligatoriamente
	private void _setObjectType(WProcessWork process){
		if (process.getIdObjectType()==null) {
			process.setIdObjectType(WProcessWork.class.getName());
		}
	}
	
	/**
	 * Finalize an instance of work: set status to finished...
	 * 
	 * @param process
	 * @param currentUserId
	 * @throws WProcessWorkException
	 */
	public void finalize(WProcessWork process, Integer currentUserId) throws WProcessWorkException {
		
		logger.debug("finalize() WProcessWork < id = "+process.getId()+">");
		
		try {
			process.setStatus(
					new WProcessStatusBL().getWProcessStatusByName(FINISHED, currentUserId) );
		} catch (WProcessStatusException e) {
			String mess="Cant't set status finished for process work id:"+process.getId();
			logger.error(mess);
			throw new WProcessWorkException(mess);
		}

		// ent time 
		process.setEndTime(new Date());
		
		// timestamp & trace info
		process.setModDate(new Date());
		process.setModUser(currentUserId);
		new WProcessWorkDao().update(process);
			

	}
	
	
	public void update(WProcessWork process, Integer currentUserId) throws WProcessWorkException {
		
		logger.debug("update() WProcessWork < id = "+process.getId()+">");
		
		if (!process.equals(new WProcessWorkDao().getWProcessWorkByPK(process.getId())) ) {

			// timestamp & trace info
			process.setModDate(new Date());
			process.setModUser(currentUserId);
			new WProcessWorkDao().update(process);
			
		} else {
			
			logger.debug("WProcessWorkBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WProcessWork process, Integer currentUserId) throws WProcessWorkException {

		logger.debug("delete() WProcessWork - Name: ["+process.getReference()+"]");
		
		new WProcessWorkDao().delete(process);

	}

	public WProcessWork getWProcessWorkByPK(Integer id, Integer currentUserId) throws WProcessWorkException {

		return new WProcessWorkDao().getWProcessWorkByPK(id);
	}
	
	
	public WProcessWork getWProcessWorkByName(String name, Integer currentUserId) throws WProcessWorkException {

		return new WProcessWorkDao().getWProcessWorkByName(name);
	}

	/**
	 * @author dmuleiro - 20130829
	 * 
	 * Returns the List<WProcessWork> related with a concrete WProcessDef.
	 *
	 * @param  Integer processId
	 * @param  Integer currentUserId
	 * 
	 * @return List<WProcessWork>

	 * @throws WProcessWorkException 
	 * 
	 */
	public List<WProcessWork> getWProcessWorkListByProcessId(Integer processId, Integer currentUserId) throws WProcessWorkException {

		return new WProcessWorkDao().getWProcessWorkListByProcessId(processId);
	}
	
	public List<WProcessWork> getWProcessWorks(Integer currentUser) throws WProcessWorkException {

		return new WProcessWorkDao().getWProcessWorks();
	
	}
	
	
	public List<ProcessWorkLight> finderWorkingWork(Integer idProcess, 
			String workTypeFilter, boolean onlyActiveWorkingProcessesFilter, 
			DateTime initialStartedDateFilter, DateTime finalStartedDateFilter, 
			boolean estrictStartedDateFilter, DateTime initialFinishedDateFilter, DateTime finalFinishedDateFilter, 
			boolean estrictFinishedDateFilter, String action)
	throws WProcessWorkException {
		
		return new WProcessWorkDao().finderWorkingWork(idProcess, workTypeFilter,
				onlyActiveWorkingProcessesFilter, initialStartedDateFilter, finalStartedDateFilter, 
				estrictStartedDateFilter, initialFinishedDateFilter, finalFinishedDateFilter, 
				estrictFinishedDateFilter, action);
		
	}

	/**
	 * returns qty of existing works in WStepWork
	 * default: ALL
	 * 
	 * @param processId
	 * @param mode: ALL / ALIVE / PROCESSED
	 * @return
	 * @throws WProcessWorkException
	 */
	public Integer getWorkCount (Integer processId, String mode) throws WProcessWorkException {
		return new WProcessWorkDao().getWorkCount(processId, mode);
	}

}
	