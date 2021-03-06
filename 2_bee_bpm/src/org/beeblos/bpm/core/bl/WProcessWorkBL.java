package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;
import static org.beeblos.bpm.core.util.Constants.FINISHED;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessWorkDao;
import org.beeblos.bpm.core.error.WProcessStatusException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.model.WProcessStatus;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.enumerations.ProcessWorkStatus;
import org.beeblos.bpm.core.model.noper.ProcessWorkLight;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;



public class WProcessWorkBL {
	
	private static final Log logger = LogFactory.getLog(WProcessWorkBL.class.getName());
	
	public WProcessWorkBL (){
		
	}
	
	/**
	 * Adds a new ProcessWork - new process is launched ...
	 * 
	 * @param process
	 * @param currentUserId
	 * @return
	 * @throws WProcessWorkException
	 */
	public Integer add(WProcessWork process, Integer currentUserId) throws WProcessWorkException {
		
		logger.debug("add() WProcessWork - Name: ["+process.getId()+"]");
		
		Integer externalUserId=currentUserId; // TODO IMPLEMENTAR: new WUseridmapperBL().getExternalUser(currentUserId);
		
		_setObjectType(process);
		// timestamp & trace info
		process.setInsertDate(new DateTime());
		process.setModDate( DEFAULT_MOD_DATE_TIME );
		process.setInsertUser(currentUserId);
		process.setModUser(currentUserId);
		if ( process.getStatus()==null ) {
			process.setStatus(new WProcessStatus(1));
		}
		return new WProcessWorkDao().add(process,externalUserId);

	}
	
	
	/**
	 * nes 20130728 - por el tema de aceptar procesos genericos no relacionados con 1 objeto obligatoriamente
	 * 
	 * @param process
	 */
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
		process.setEndTime(new DateTime());
		
		// timestamp & trace info
		process.setModDate(new DateTime());
		process.setModUser(currentUserId);
		new WProcessWorkDao().update(process);
			

	}
	
	
	public void update(WProcessWork process, Integer currentUserId) throws WProcessWorkException {
		
		logger.debug("update() WProcessWork < id = "+process.getId()+">");
		
		if (!process.equals(new WProcessWorkDao().getWProcessWorkByPK(process.getId())) ) {

			// timestamp & trace info
			process.setModDate(new DateTime());
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
	 * Returns the List<WProcessWork> related with a given WProcessDef.
	 * NOTA: ESTO SOLO LO USAMOS DESDE DEVELOPMENT PARA BORRAR STEPS
	 * TODO: ESTO HAY QUE REVISARLO Y DEJARLO BLOQUEADO PARA USO EN PRODUCCION. NESTOR 20140131
	 *
	 * @param  Integer processId
	 * @param  Integer currentUserId
	 * 
	 * @return List<WProcessWork>

	 * @throws WProcessWorkException 
	 * 
	 */
	public List<WProcessWork> getWProcessWorkListByProcessId(
			Integer processId, Integer currentUserId) throws WProcessWorkException {

		return new WProcessWorkDao().getWProcessWorkListByProcessId(processId);
	}
	
	/**
	 * Returns a list of processWork by the given filters
	 * 
	 * @param processId
	 * @param idObject
	 * @param idObjectType
	 * @param mode: ALL / ALIVE / PROCESSED
	 * @param currentUserId
	 * boolean isAdminMode -> indica que se está trabajando en modo admin y que no hay que filtrar por el usuario ...
	 * @return
	 * @throws WProcessWorkException
	 */
	public List<WProcessWork> getWProcessWorkList(
			Integer processId, Integer idObject, String idObjectType, ProcessWorkStatus processWorkStatus, 
			Integer currentUserId, boolean isAdminMode) 
		throws WProcessWorkException {

		return new WProcessWorkDao().getWProcessWorkList(
				processId, idObject, idObjectType, processWorkStatus, currentUserId, isAdminMode);

	}
	
	/**
	 * Returns a list of processWork by the given filters
	 * 
	 * @param processId
	 * @param idObject
	 * @param idObjectType
	 * @param ProcessWorkStatus: ALL / ALIVE / PROCESSED ProcessWorkStatus enum
	 * @param currentUserId
	 * boolean isAdminMode -> indica que se está trabajando en modo admin y que no hay que filtrar por el usuario ...
	 * @return
	 * @throws WProcessWorkException
	 */
	public List<WProcessWork> getWProcessWorkList(
			Integer idObject, String idObjectType, ProcessWorkStatus processWorkStatus, 
			Integer currentUserId, boolean isAdminMode) 
		throws WProcessWorkException {

		return new WProcessWorkDao().getWProcessWorkList(
				null, idObject, idObjectType, processWorkStatus, currentUserId, isAdminMode);

	}	
	
	/**
	 * Returns all processWork for a single user ...
	 * 
	 * @param currentUser
	 * @return
	 * @throws WProcessWorkException
	 */
	public List<WProcessWork> getWProcessWorks(Integer currentUser) throws WProcessWorkException {

		return new WProcessWorkDao().getWProcessWorks();
	
	}
	
	
	/**
	 * Finds process works instances ...
	 * 
	 * @param idProcess
	 * @param ProcessWorkStatus - processWorkStatus enum
	 * @param onlyActiveProcessWorkFilter
	 * @param initialStartedDateFilter
	 * @param finalStartedDateFilter
	 * @param estrictStartedDateFilter
	 * @param initialFinishedDateFilter
	 * @param finalFinishedDateFilter
	 * @param estrictFinishedDateFilter
	 * @param action
	 * @return
	 * @throws WProcessWorkException
	 */
	public List<ProcessWorkLight> finderProcessWork(Integer idProcess, 
			ProcessWorkStatus processWorkStatus, boolean onlyActiveProcessWorkFilter, 
			LocalDate initialStartedDateFilter, LocalDate finalStartedDateFilter, 
			boolean estrictStartedDateFilter, LocalDate initialFinishedDateFilter, LocalDate finalFinishedDateFilter, 
			boolean estrictFinishedDateFilter, String action)
	throws WProcessWorkException {
		
		return new WProcessWorkDao().finderProcessWork(idProcess, processWorkStatus,
				onlyActiveProcessWorkFilter, initialStartedDateFilter, finalStartedDateFilter, 
				estrictStartedDateFilter, initialFinishedDateFilter, finalFinishedDateFilter, 
				estrictFinishedDateFilter, action);
		
	}

	/**
	 * returns qty of existing process work for a given processId
	 * default: ALL
	 * 
	 * @param processId
	 * @param ProcessWorkStatus: ALL / ALIVE / PROCESSED - ProcessWorkStatus enum
	 * @return
	 * @throws WProcessWorkException
	 */
	public Integer getWorkCount (Integer processId, ProcessWorkStatus processWorkStatus) throws WProcessWorkException {
		return new WProcessWorkDao().getWorkCount(processId, null, null, processWorkStatus);
	}
	
	/**
	 * returns qty of existing process work for a given processId, idObject && objectType
	 * default: ALL
	 *
	 * @param processId
	 * @param idObject
	 * @param idObjectType
	 * @param mode: ALL / ALIVE / PROCESSED
	 * @return
	 * @throws WProcessWorkException
	 */
	public Integer getWorkCount (Integer processId, Integer idObject, String idObjectType, ProcessWorkStatus processWorkStatus) 
			throws WProcessWorkException {
		return new WProcessWorkDao().getWorkCount(processId, idObject, idObjectType, processWorkStatus);
	}
	
	/**
	 * returns qty of existing process work for a given idObject && objectType for all processes
	 * default: ALL
	 *
	 * @param idObject
	 * @param idObjectType
	 * @param ProcessWorkStatus: ALL / ALIVE / PROCESSED - ProcessWorkStatus enum
	 * @return
	 * @throws WProcessWorkException
	 */
	public Integer getWorkCount (Integer idObject, String idObjectType, ProcessWorkStatus processWorkStatus) 
			throws WProcessWorkException {
		return new WProcessWorkDao().getWorkCount(null, idObject, idObjectType, processWorkStatus);
	}
	
	

}
	