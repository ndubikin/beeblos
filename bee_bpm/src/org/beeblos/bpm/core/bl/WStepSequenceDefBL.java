package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepSequenceDefDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.noper.StringPair;



public class WStepSequenceDefBL {
	
	private static final Log logger = LogFactory.getLog(WStepSequenceDefBL.class.getName());
	
	public WStepSequenceDefBL (){
		
	}
	
	public Integer add(WStepSequenceDef route, Integer currentUser) throws WStepSequenceDefException {
		
		logger.debug("add() WStepSequenceDef - Name: [Proc Version Id:"+
				route.getProcess().getId()+"-fromStepId:"
				+((route!=null && route.getFromStep()!=null)?route.getFromStep().getId():"xxxx")+"]");
		
		// timestamp & trace info
		route.setInsertDate(new Date());
		route.setModDate(DEFAULT_MOD_DATE);
		route.setInsertUser(currentUser);
		route.setModUser(currentUser);
		return new WStepSequenceDefDao().add(route);

	}
	
	
	public void update(WStepSequenceDef route, Integer currentUser) throws WStepSequenceDefException {
		
		logger.debug("update() WStepSequenceDef < id = "+(route!=null?route.getId():"xx.xx")+">");
		
		if (!route.equals(new WStepSequenceDefDao().getWStepSequenceDefByPK(route.getId())) ) {

			// timestamp & trace info
			route.setModDate(new Date());
			route.setModUser(currentUser);
			new WStepSequenceDefDao().update(route);
			
		} else {
			
			logger.debug("WStepSequenceDefBL.update - nothing to do ...");
		}
		

					
	}
	
	
	public void deleteRoute(WStepSequenceDef route, Integer userId) throws WStepSequenceDefException {

		logger.info("delete() WStepSequenceDef - VersionId: [" +
					route.getProcess().getId()
					+"-fromStepId:"
					+((route!=null && route.getFromStep()!=null)?route.getFromStep().getId():"xx..xx")+"]");
		
		new WStepSequenceDefDao().deleteRoute(route);

	}
	
	// deletes all map (routes) for a given process and version ...
	public void deleteRoutesFromProcess(WProcessDef process, Integer currentUser) throws WStepSequenceDefException {

		logger.debug("delete() WStepSequenceDef - ProcId: [" +
					process.getId() + "]");
		
		new WStepSequenceDefDao().deleteRoutesFromProcess(process);

	}

	public WStepSequenceDef getWStepSequenceDefByPK(Integer id, Integer currentUser) throws WStepSequenceDefException {

		return new WStepSequenceDefDao().getWStepSequenceDefByPK(id);
	}
	
	
//	public WStepSequenceDef getWStepSequenceDefByName(String name, Integer currentUser) throws WStepSequenceDefException {
//
//		return new WStepSequenceDefDao().getWStepSequenceDefByName(name);
//	}

	
	public List<WStepSequenceDef> getWStepSequenceDefs(Integer currentUser) throws WStepSequenceDefException {

		return new WStepSequenceDefDao().getWStepSequenceDefs();
	
	}
	
	// retrieves all routes from passed step  ( enabled and disabled )
	public List<WStepSequenceDef> getStepSequenceDefs(
			Integer processId, Integer fromStepId , Integer userId  ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao().getStepSequenceDefs(processId, fromStepId);
	 
	}	

	public List<WStepSequenceDef> getStepSequenceList(
			Integer processId , Integer userId ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao().getStepSequenceList(processId);
		
	}
	
	public List<WStepSequenceDef> getOutgoingRoutes(
			Integer stepId, Integer processId, Integer userId ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao()
			.getOutgoingRoutes(stepId, processId);
		
	}
	
	public Integer countOutgoingRoutes(
			Integer stepId, Integer processId, Integer userId ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao()
			.countOutgoingRoutes(stepId, processId);
		
	}
	
	// returns a list with incoming routes pointing to a step
	public List<WStepSequenceDef> getIncomingRoutes(
			Integer stepId, Integer processId, Integer userId ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao()
				.getIncomingRoutes(stepId, processId);
		
	}
	
	// returns qty routes incoming to this step. If processId=null will return all # of incoming routes
	public Integer countIncomingRoutes(
			Integer stepId, Integer processId, Integer userId ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao()
				.countIncomingRoutes(stepId, processId);
		
	}
	
	public List<StringPair> getComboList(
			Integer idProcess, Integer version,
			String firstLineText, String blank )
	throws WProcessDefException {
		
		return new WStepSequenceDefDao().getComboList(idProcess, version, firstLineText, blank);
		
	}
	
	// nes 20101217
	public Integer getLastVersionWStepSequenceDef (
			Integer idProcess ) 
	throws WStepSequenceDefException {
		
		return new WStepSequenceDefDao().getLastVersionWStepSequenceDef( idProcess );
		
	}

}
	