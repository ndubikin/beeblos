package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;
import static org.beeblos.bpm.core.util.Constants.DELETED_BOOL;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepSequenceDefDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.joda.time.DateTime;

import com.sp.common.util.StringPair;



public class WStepSequenceDefBL {
	
	private static final Log logger = LogFactory.getLog(WStepSequenceDefBL.class.getName());
	
	public WStepSequenceDefBL (){
		
	}
	
	/**
	 * Adds a new route for given processDef
	 * 
	 * @param route
	 * @param currentUserId
	 * @return
	 * @throws WStepSequenceDefException
	 */
	public Integer add(WStepSequenceDef route, Integer currentUserId) throws WStepSequenceDefException {
		
		logger.debug("add() WStepSequenceDef - Name: [Proc Version Id:["+
				( route!=null && route.getProcess()!=null && route.getProcess().getId()!=null ? 
						route.getProcess().getId():"null")
						+"]");
		
		route.nullateEmtpyObjects();// nes 20150121
		
		// timestamp & trace info
		route.setInsertDate(new DateTime());
		route.setModDate(DEFAULT_MOD_DATE_TIME);
		route.setInsertUser(currentUserId);
		route.setModUser(currentUserId);
		return new WStepSequenceDefDao().add(route);

	}
	
	
	public void update(WStepSequenceDef route, Integer currentUserId) throws WStepSequenceDefException {
		
		logger.debug("update() WStepSequenceDef id:"+(route!=null?route.getId():"null")+"");
		
		if (!route.equals(new WStepSequenceDefDao().getWStepSequenceDefByPK(route.getId())) ) {

			route.nullateEmtpyObjects();// nes 20150121
			
			// timestamp & trace info
			route.setModDate(new DateTime());
			route.setModUser(currentUserId);
			new WStepSequenceDefDao().update(route);
			
		} else {
			
			logger.debug("WStepSequenceDefBL.update - nothing to do ...");
		}
		

					
	}
	
	/**
	 * @author dmuleiro - 20130830
	 * 
	 * Updates the step's sequence "logic delete" putting the field "delete" as the user wants. 
	 *
	 * @param  Integer stepSequenceId
	 * @param  boolean deleted
	 * @param  Integer currentUserId
	 * 
	 * @return void
	 * 
	 * @throws WStepSequenceDefException 
	 * 
	 */
	private void _updateStepSequenceDeletedField(Integer stepSequenceId, boolean deleted, Integer currentUserId)
			throws WStepSequenceDefException {

		logger.debug("updateStepSequenceDeletedField() WStepDef < id = " + stepSequenceId + ">");

		if (stepSequenceId != null 
				&& !stepSequenceId.equals(0)) {

			new WStepSequenceDefDao().updateStepSequenceDeletedField(stepSequenceId, deleted, currentUserId, new DateTime());

		}

	}

	
	/**
	 * @author dmuleiro - 20130829
	 * 
	 * Deletes the route from the database if it has not related "w_step_work_sequences". In this case
	 * it makes a "logic delete" putting the field "delete" as "true". 
	 * 
	 * nes: 20150102 - changed route for idRoute
	 *
	 * @param  WStepSequenceDef route
	 * @param  Integer currentUserId
	 * 
	 * @return void
	 * 
	 * @throws WStepSequenceDefException 
	 * @throws WStepWorkSequenceException 
	 * 
	 */
	public void deleteRoute(Integer idRoute, Integer currentUserId) 
			throws WStepSequenceDefException, WStepWorkSequenceException {

		logger.info("delete() WStepSequenceDef - routeId: [" +
					(idRoute!=null?idRoute:"null")+"]");
		
		Integer workingRoutes = new WStepWorkSequenceBL()
				.countRouteRelatedStepWorkSequences(idRoute, currentUserId);
		
		if (workingRoutes != null
				&& workingRoutes > 0){
			this._updateStepSequenceDeletedField(idRoute, DELETED_BOOL, currentUserId);
		} else {
			new WStepSequenceDefDao().deleteRoute(idRoute);
		}
		

	}
	
	/**
	 * @author dmuleiro - 20130829
	 * 
	 * Deletes the route from the database if it has not related "w_step_work_sequences". In this case
	 * it makes a "logic delete" putting the field "delete" as "true". 
	 *
	 * @param  WStepSequenceDef route
	 * @param  Integer currentUserId
	 * 
	 * @return void
	 * 
	 * @throws WStepSequenceDefException 
	 * @throws WStepWorkSequenceException 
	 * 
	 */
	public void delete(Integer idRoute, Integer currentUserId) 
			throws WStepSequenceDefException, WStepWorkSequenceException {

		logger.info("delete() WStepSequenceDef - VersionId: [" +
					(idRoute!=null?idRoute:"null")+"]");
		
		Integer workingRoutes = new WStepWorkSequenceBL()
				.countRouteRelatedStepWorkSequences(idRoute, currentUserId);
		
		if (workingRoutes != null
				&& workingRoutes > 0){
			this._updateStepSequenceDeletedField(idRoute, DELETED_BOOL, currentUserId);
		} else {
			new WStepSequenceDefDao().deleteRoute(idRoute);
		}
		

	}	
	
	@Deprecated 
	// DAVID - CAMBIAR Y SI TIENE WSTEPWORKSEQUENCES MARCARLAS COMO DELETED EN LUGAR DE BORRARLAS
	public void deleteProcessRoutes(WProcessDef process, Integer currentUserId) throws WStepSequenceDefException {

		logger.debug("delete() WStepSequenceDef - ProcId: [" +
					process.getId() + "]");
		
		new WStepSequenceDefDao().deleteProcessRoutes(process);

	}

	// dml 20130827
	public boolean existsRoute(Integer routeId) throws WStepSequenceDefException {

		return new WStepSequenceDefDao().existsRoute(routeId);
	
	}
	
	public WStepSequenceDef getWStepSequenceDefByPK(Integer id, Integer currentUserId) 
			throws WStepSequenceDefException {

		return new WStepSequenceDefDao().getWStepSequenceDefByPK(id);
	}
	
	
//	public WStepSequenceDef getWStepSequenceDefByName(String name, Integer currentUserId) throws WStepSequenceDefException {
//
//		return new WStepSequenceDefDao().getWStepSequenceDefByName(name);
//	}

	
	public List<WStepSequenceDef> getWStepSequenceDefs(Integer currentUserId) throws WStepSequenceDefException {

		return new WStepSequenceDefDao().getWStepSequenceDefs();
	
	}
	

	/**
	 * WORK METHOD
	 * Returns all outgoing routes from referred step  ( enabled and disabled )
	 *  
	 * @param processId
	 * @param fromStepId
	 * @param currentUserId
	 * @return
	 * @throws WStepSequenceDefException
	 */
	public List<WStepSequenceDef> getStepSequenceList(
			Integer processId, Integer fromStepId , Integer currentUserId  ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao().getStepSequenceList(processId, fromStepId);
	 
	}	

	/**
	 * ADMIN METHOD!!
	 * @author dmuleiro 20130829
	 * 
	 * Returns the List<WStepSequenceDef> related with a WProcessDef.
	 *
	 * @param Integer processId
	 * @param String deleted: null or "ALL": returns all routes, "DELETED": returns deleted routes, "ALIVE": returns alive routes
	 * @param Integer currentUserId
	 * 
	 * @return List<WStepDef>
	 * 
	 * @throws WStepDefException
	 * 
	 */
	public List<WStepSequenceDef> getStepSequenceList(
			Integer processId , String deleted, Integer currentUserId ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao().getStepSequenceList(processId, deleted);
		
	}
	
	public List<WStepSequenceDef> getOutgoingRoutes(
			Integer stepId, Boolean deleted, Integer processId, Integer currentUserId ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao()
			.getOutgoingRoutes(stepId, deleted, processId);
		
	}
	
	public Integer countOutgoingRoutes(
			Integer stepId, Boolean deleted, Integer processId, Integer currentUserId ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao()
			.countOutgoingRoutes(stepId, processId);
		
	}
	
	// returns a list with incoming routes pointing to a step
	public List<WStepSequenceDef> getIncomingRoutes(
			Integer stepId, Boolean deleted, Integer processId, Integer currentUserId ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao()
				.getIncomingRoutes(stepId, deleted, processId);
		
	}
	
	// returns qty routes incoming to this step. If processId=null will return all # of incoming routes
	public Integer countIncomingRoutes(
			Integer stepId, Boolean deleted, Integer processId, Integer currentUserId ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao()
				.countIncomingRoutes(stepId, processId);
		
	}
	
	public List<StringPair> getComboList(
			Integer idProcess, Integer version,
			String firstLineText, String blank )
	throws WStepSequenceDefException {
		
		return new WStepSequenceDefDao().getComboList(idProcess, version, firstLineText, blank);
		
	}
	/* dml 20130926 NO EXISTEN VERSION EN EL WSTEPSEQUENCEDEF	
	
	// nes 20101217
	public Integer getLastVersionWStepSequenceDef (
			Integer idProcess ) 
	throws WStepSequenceDefException {
		
		return new WStepSequenceDefDao().getLastVersionWStepSequenceDef( idProcess );
		
	}
	*/
}
	