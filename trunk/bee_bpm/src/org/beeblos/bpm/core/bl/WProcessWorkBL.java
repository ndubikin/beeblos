package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WProcessWorkDao;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.noper.ProcessWorkLight;



public class WProcessWorkBL {
	
	private static final Log logger = LogFactory.getLog(WProcessWorkBL.class.getName());
	
	public WProcessWorkBL (){
		
	}
	
	public Integer add(WProcessWork process, Integer currentUser) throws WProcessWorkException {
		
		logger.debug("add() WProcessWork - Name: ["+process.getReference()+"]");
		
		// timestamp & trace info
		process.setInsertDate(new Date());
		process.setModDate( DEFAULT_MOD_DATE);
		process.setInsertUser(currentUser);
		process.setModUser(null);
		return new WProcessWorkDao().add(process);

	}
	
	
	public void update(WProcessWork process, Integer currentUser) throws WProcessWorkException {
		
		logger.debug("update() WProcessWork < id = "+process.getId()+">");
		
		if (!process.equals(new WProcessWorkDao().getWProcessWorkByPK(process.getId())) ) {

			// timestamp & trace info
			process.setModDate(new Date());
			process.setModUser(currentUser);
			new WProcessWorkDao().update(process);
			
		} else {
			
			logger.debug("WProcessWorkBL.update - nothing to do ...");
		}
			
	}
	
	
	public void delete(WProcessWork process, Integer currentUser) throws WProcessWorkException {

		logger.debug("delete() WProcessWork - Name: ["+process.getReference()+"]");
		
		new WProcessWorkDao().delete(process);

	}

	public WProcessWork getWProcessWorkByPK(Integer id, Integer currentUser) throws WProcessWorkException {

		return new WProcessWorkDao().getWProcessWorkByPK(id);
	}
	
	
	public WProcessWork getWProcessWorkByName(String name, Integer currentUser) throws WProcessWorkException {

		return new WProcessWorkDao().getWProcessWorkByName(name);
	}

	
	public List<WProcessWork> getWProcessWorks(Integer currentUser) throws WProcessWorkException {

		return new WProcessWorkDao().getWProcessWorks();
	
	}
	
	
	public List<ProcessWorkLight> getWorkingWorkListFinder(Integer idProcess, 
			boolean onlyActiveWorksFilter, Date initialStartedDateFilter, Date finalStartedDateFilter, 
			boolean estrictStartedDateFilter, Date initialFinishedDateFilter, Date finalFinishedDateFilter, 
			boolean estrictFinishedDateFilter, String action)
	throws WProcessWorkException {
		
		return new WProcessWorkDao().getWorkingWorkListFinder(idProcess, 
				onlyActiveWorksFilter, initialStartedDateFilter, finalStartedDateFilter, 
				estrictStartedDateFilter, initialFinishedDateFilter, finalFinishedDateFilter, 
				estrictFinishedDateFilter, action);
		
	}

}
	