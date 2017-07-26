package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepTypeDefDao;
import org.beeblos.bpm.core.error.WStepTypeDefException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.error.WUserRoleException;
import org.beeblos.bpm.core.model.WStepTypeDef;
import org.joda.time.DateTime;

import com.sp.common.util.StringPair;

public class WStepTypeDefBL {
	
	
	private static final Log logger = LogFactory.getLog(WStepTypeDefBL.class.getName());
	
	public WStepTypeDefBL (){
		
	}
	
	public Integer add(WStepTypeDef wStepTypeDef, Integer user) throws WStepTypeDefException {
		
		logger.debug("add() WStepTypeDef - Name: ["+wStepTypeDef.getName()+"]");
		
		// timestamp & trace info
		wStepTypeDef.setInsertDate(new DateTime());
		wStepTypeDef.setModDate( DEFAULT_MOD_DATE_TIME );
		wStepTypeDef.setInsertUser(user);
		wStepTypeDef.setModUser(user);
		return new WStepTypeDefDao().add(wStepTypeDef);

	}
	
	
	public void update(WStepTypeDef wStepTypeDef, Integer user) throws WStepTypeDefException {
		
		logger.debug("update() WStepTypeDef < id = "+wStepTypeDef.getId()+">");
		
		if (!wStepTypeDef.equals(new WStepTypeDefDao().getWStepTypeDefByPK(wStepTypeDef.getId())) ) {

			// timestamp & trace info
			wStepTypeDef.setModDate(new DateTime());
			wStepTypeDef.setModUser(user);
			new WStepTypeDefDao().update(wStepTypeDef);
			
		} else {
			
			logger.debug("WStepTypeDefBL.update - nothing to do ...");
		}
		

					
	}
	
	public void delete(Integer wStepTypeDefId, Integer user) throws WStepTypeDefException {

		logger.debug("delete() WStepTypeDef - wStepTypeDefId: ["+wStepTypeDefId+"]");
		
		new WStepTypeDefDao().delete(wStepTypeDefId);

	}
	
	public void delete(WStepTypeDef wStepTypeDef, Integer user) throws WStepTypeDefException {

		logger.debug("delete() WStepTypeDef - Name: ["+wStepTypeDef.getName()+"]");
		
		new WStepTypeDefDao().delete(wStepTypeDef);

	}

	public WStepTypeDef getWStepTypeDefByPK(Integer id, Integer user) throws WStepTypeDefException {

		return new WStepTypeDefDao().getWStepTypeDefByPK(id);
	}
	
	
	public WStepTypeDef getWStepTypeDefByName(String name, Integer user) throws WStepTypeDefException {

		return new WStepTypeDefDao().getWStepTypeDefByName(name);
	}

	
	public List<WStepTypeDef> getWStepTypeDefs(Integer user) throws WStepTypeDefException {

		return new WStepTypeDefDao().getWStepTypeDefs();
	
	}
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WStepTypeDefException {
		 
		return new WStepTypeDefDao().getComboList(textoPrimeraLinea, separacion);


	}
	
	public List<StringPair> getComboListWithType(
			String textoPrimeraLinea, String separacion,
			String type)
	throws WStepTypeDefException {
		 
		return new WStepTypeDefDao().getComboListWithType(textoPrimeraLinea, separacion, type);


	}
}
