package org.beeblos.bpm.core.bl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepDefDao;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.noper.StringPair;



public class WStepDefBL {
	
	private static final Log logger = LogFactory.getLog(WStepDefBL.class.getName());
	
	public WStepDefBL (){
		
	}
	
	public Integer add(WStepDef step, Integer currentUser) throws WStepDefException {
		
		logger.debug("add() WStepDef - Name: ["+step.getName()+"]");
		
		// timestamp & trace info
//		step.setFechaAlta(new Date());
//		step.setFechaModificacion(new Date());
//		step.setUsuarioAlta(user);
//		step.setUsuarioModificacion(user);
		return new WStepDefDao().add(step);

	}
	
	
	public void update(WStepDef step, Integer currentUser) throws WStepDefException {
		
		logger.debug("update() WStepDef < id = "+step.getId()+">");
		
		if (!step.equals(new WStepDefDao().getWStepDefByPK(step.getId())) ) {

			// timestamp & trace info
//			step.setFechaModificacion(new Date());
//			step.setUsuarioModificacion(user);
			new WStepDefDao().update(step);
			
		} else {
			
			logger.debug("WStepDefBL.update - nothing to do ...");
		}
		

					
	}
	
	
	public void delete(WStepDef step, Integer currentUser) throws WStepDefException {

		logger.debug("delete() WStepDef - Name: ["+step.getName()+"]");
		
		new WStepDefDao().delete(step);

	}

	public WStepDef getWStepDefByPK(Integer id, Integer currentUser) throws WStepDefException {

		return new WStepDefDao().getWStepDefByPK(id);
	}
	
	
	public WStepDef getWStepDefByName(String name, Integer currentUser) throws WStepDefException {

		return new WStepDefDao().getWStepDefByName(name);
	}

	
	public List<WStepDef> getWStepDefs(Integer currentUser) throws WStepDefException {

		return new WStepDefDao().getWStepDefs();
	
	}
	
	
	public List<StringPair> getComboList(
			String firstLineText, String blank )
	throws WStepDefException {
		 
		return new WStepDefDao().getComboList(firstLineText, blank);


	}
	
	public List<StringPair> getComboList(
			Integer idProcess, Integer version,
			String firstLineText, String blank )
	throws WProcessDefException {
		
		return new WStepDefDao().getComboList(idProcess, version, firstLineText, blank);
		
	}

}
	