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
		
		logger.debug("add() WStepSequenceDef - Name: [ProcId:"+
				route.getProcess().getId()+"-ver:"+route.getVersion()+"-fromStepId:"+route.getFromStep().getId()+"]");
		
		// timestamp & trace info
		route.setInsertDate(new Date());
		route.setModDate(DEFAULT_MOD_DATE);
		route.setInsertUser(currentUser);
		route.setModUser(currentUser);
		return new WStepSequenceDefDao().add(route);

	}
	
	
	public void update(WStepSequenceDef route, Integer currentUser) throws WStepSequenceDefException {
		
		logger.debug("update() WStepSequenceDef < id = "+route.getId()+">");
		
		if (!route.equals(new WStepSequenceDefDao().getWStepSequenceDefByPK(route.getId())) ) {

			// timestamp & trace info
			route.setModDate(new Date());
			route.setModUser(currentUser);
			new WStepSequenceDefDao().update(route);
			
		} else {
			
			logger.debug("WStepSequenceDefBL.update - nothing to do ...");
		}
		

					
	}
	
	
	public void deleteRoute(WStepSequenceDef route, Integer currentUser) throws WStepSequenceDefException {

		logger.debug("delete() WStepSequenceDef - ProcId: [" +
					route.getProcess().getId()+"-ver:"+route.getVersion()
					+"-fromStepId:"+route.getFromStep().getId()+"]");
		
		new WStepSequenceDefDao().deleteRoute(route);

	}
	
	// deletes all map (routes) for a given process and version ...
	public void deleteProcess(WProcessDef process, Integer version, Integer currentUser) throws WStepSequenceDefException {

		logger.debug("delete() WStepSequenceDef - ProcId: [" +
					process.getId()+"-ver:"+version	+"]");
		
		new WStepSequenceDefDao().deleteProcess(process, version);

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
	public List<WStepSequenceDef> getWStepSequenceDefs(
			Integer idProcess, Integer version, Integer idFromStep , Integer currentUser  ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao().getWStepSequenceDefs(idProcess, version, idFromStep);
	 
	}	

	// dml 20120125
	public List<WStepSequenceDef> getWStepSequenceDefList(
			Integer idProcess, Integer version , Integer currentUser ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao().getWStepSequenceDefList(idProcess, version);
		
	}
	
	// dml 20120323
	public List<WStepSequenceDef> getWStepSequenceDefListByFromStep(
			Integer idFromStep, Integer idProcess, Integer currentUser ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao().getWStepSequenceDefListByFromStep(idFromStep, idProcess);
		
	}
	
	// dml 20120323
	public List<WStepSequenceDef> getWStepSequenceDefListByToStep(
			Integer idToStep, Integer idProcess, Integer currentUser ) 
	throws WStepSequenceDefException {

		return new WStepSequenceDefDao().getWStepSequenceDefListByToStep(idToStep, idProcess);
		
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
	