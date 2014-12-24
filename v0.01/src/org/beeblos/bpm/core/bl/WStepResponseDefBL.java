package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepResponseDefDao;
import org.beeblos.bpm.core.error.WStepResponseDefException;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.joda.time.DateTime;

import com.sp.common.util.StringPair;



public class WStepResponseDefBL {
	
	private static final Log logger = LogFactory.getLog(WStepResponseDefBL.class.getName());
	
	public WStepResponseDefBL (){
		
	}
	
	public Integer add(WStepResponseDef response, Integer user) throws WStepResponseDefException {
		
		logger.debug("add() WStepResponseDef - Name: ["+response.getName()+"]");
		
		// timestamp & trace info
		response.setInsertDate(new DateTime());
		response.setModDate(DEFAULT_MOD_DATE_TIME);
		response.setInsertUser(user);
		response.setModUser(user);
		return new WStepResponseDefDao().add(response);

	}
	
	
	public void update(WStepResponseDef response, Integer user) throws WStepResponseDefException {
		
		logger.debug("update() WStepResponseDef < id = "+response.getId()+">");
		
		if (!response.equals(new WStepResponseDefDao().getWStepResponseDefByPK(response.getId())) ) {

			// timestamp & trace info
			response.setModDate(new DateTime());
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
	
	// dml 20130704 - este método no se podrá usar ya que no el nombre de un WStepResponseDef no tiene porque ser único
	@Deprecated
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
	