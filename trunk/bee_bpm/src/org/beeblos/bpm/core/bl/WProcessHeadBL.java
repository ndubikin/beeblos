package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessHeadDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.model.WProcessHead;
import org.beeblos.bpm.core.model.noper.StringPair;



public class WProcessHeadBL {
	
	private static final Log logger = LogFactory.getLog(WProcessHeadBL.class.getName());
	
	public WProcessHeadBL (){
		
	}
	
	public Integer add(WProcessHead processHead, Integer currentUserId) throws WProcessException, WProcessDefException {
		
		logger.debug("add() WProcessHead - Name: ["+processHead.getName()+"]");
		
		// timestamp & trace info
		processHead.setInsertDate(new Date());
		processHead.setModDate( DEFAULT_MOD_DATE );
		processHead.setInsertUser(currentUserId);
		processHead.setModUser(currentUserId);

		return new WProcessHeadDao().add(processHead, currentUserId);

	}
		
	// dml 20130506
	public Integer addProcessAndFirstWProcessDef(WProcessHead processHead, Integer currentUserId) throws WProcessException, WProcessDefException, WStepSequenceDefException {
		
		logger.debug("addProcessAndFirstWProcessDef() WProcessHead - Name: ["+processHead.getName()+"]");
		
		Integer processHeadId = this.add(processHead, currentUserId);
		processHead.setId(processHeadId);
		
		new WProcessDefBL().createFirstWProcessDef(processHeadId, currentUserId);
		
		return processHeadId;

	}
		
	public void update(WProcessHead processHead, Integer currentUserId) throws WProcessException {
		
		logger.debug("update() WProcessHead < id = "+processHead.getId()+">");
		
		if (!processHead.equals(new WProcessHeadDao().getWProcessByPK(processHead.getId(), currentUserId)) ) {

			// timestamp & trace info
			processHead.setModDate(new Date());
			processHead.setModUser(currentUserId);
			new WProcessHeadDao().update(processHead, currentUserId);
			
		} else {
			
			logger.debug("WProcessHeadBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WProcessHead processHead, Integer currentUserId) throws WProcessException {

		logger.debug("delete() WProcessHead - Name: ["+processHead.getName()+"]");
		
		new WProcessHeadDao().delete(processHead, currentUserId);

	}

	public WProcessHead getProcessByPK(Integer id, Integer currentUserId) throws WProcessException {

		return new WProcessHeadDao().getWProcessByPK(id, currentUserId);
	}
	
	
	public WProcessHead getProcessByName(String name, Integer currentUserId) throws WProcessException {

		return new WProcessHeadDao().getWProcessByName(name, currentUserId);
	}

	public String getProcessName(Integer id, Integer currentUserId) throws WProcessDefException {
		return new WProcessHeadDao().getProcessName(id, currentUserId);
	}
	
	
	public List<WProcessHead> getProcessList(Integer currentUserId) throws WProcessException {

		return new WProcessHeadDao().getWProcessList(currentUserId);
	
	}
	
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion, Integer currentUserId )
	throws WProcessException {
		 
		return new WProcessHeadDao().getComboList(textoPrimeraLinea, separacion, currentUserId);
		
	}

	// dml 20130129
	public boolean headProcessHasWProcessDef(Integer processHeadId, Integer currentUserId) throws WProcessException{
		
		return new WProcessHeadDao().headProcessHasWProcessDef(processHeadId, currentUserId);
		
	}
	
}