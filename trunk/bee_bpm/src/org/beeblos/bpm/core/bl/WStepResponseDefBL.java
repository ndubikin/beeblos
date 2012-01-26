package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepResponseDefDao;
import org.beeblos.bpm.core.error.WStepResponseDefException;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.noper.StringPair;



public class WStepResponseDefBL {
	
	private static final Log logger = LogFactory.getLog(WStepResponseDefBL.class.getName());
	
	public WStepResponseDefBL (){
		
	}
	
	public Integer add(WStepResponseDef response, Integer user) throws WStepResponseDefException {
		
		logger.debug("add() WStepResponseDef - Name: ["+response.getName()+"]");
		
		// timestamp & trace info
		response.setInsertDate(new Date());
		response.setModDate(DEFAULT_MOD_DATE);
		response.setInsertUser(user);
		response.setModUser(user);
		return new WStepResponseDefDao().add(response);

	}
	
	
	public void update(WStepResponseDef response, Integer user) throws WStepResponseDefException {
		
		logger.debug("update() WStepResponseDef < id = "+response.getId()+">");
		
		if (!response.equals(new WStepResponseDefDao().getWStepResponseDefByPK(response.getId())) ) {

			// timestamp & trace info
			response.setModDate(new Date());
			response.setModUser(user);
			new WStepResponseDefDao().update(response);
			
		} else {
			
			logger.debug("WStepResponseDefBL.update - nothing to do ...");
		}
		

					
	}
	
	
	public void delete(WStepResponseDef response, Integer user) throws WStepResponseDefException {

		logger.debug("delete() WStepResponseDef - Name: ["+response.getName()+"]");
		
		new WStepResponseDefDao().delete(response);

	}

	public WStepResponseDef getWStepResponseDefByPK(Integer id, Integer user) throws WStepResponseDefException {

		return new WStepResponseDefDao().getWStepResponseDefByPK(id);
	}
	
	
	public WStepResponseDef getWStepResponseDefByName(String name, Integer user) throws WStepResponseDefException {

		return new WStepResponseDefDao().getWStepResponseDefByName(name);
	}

	
	public List<WStepResponseDef> getWStepResponseDefs(Integer user) throws WStepResponseDefException {

		return new WStepResponseDefDao().getWStepResponseDefs();
	
	}
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WStepResponseDefException {
		 
		return new WStepResponseDefDao().getComboList(textoPrimeraLinea, separacion);


	}

}
	