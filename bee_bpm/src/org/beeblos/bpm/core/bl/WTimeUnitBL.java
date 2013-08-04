package org.beeblos.bpm.core.bl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WTimeUnitDao;
import org.beeblos.bpm.core.error.WTimeUnitException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WTimeUnit;
import org.beeblos.bpm.core.model.WUserDef;
import com.sp.common.util.StringPair;



public class WTimeUnitBL {
	
	private static final Log logger = LogFactory.getLog(WTimeUnitBL.class.getName());
	
	public WTimeUnitBL (){
		
	}
	
	public Integer add(WTimeUnit timeUnit, Integer user) throws WTimeUnitException {
		
		logger.debug("add() WTimeUnit - Name: ["+timeUnit.getName()+"]");
		
		return new WTimeUnitDao().add(timeUnit);

	}
	
	
	public void update(WTimeUnit timeUnit, Integer user) throws WTimeUnitException {
		
		logger.debug("update() WTimeUnit < id = "+timeUnit.getId()+">");
		
		if (!timeUnit.equals(new WTimeUnitDao().getWTimeUnitByPK(timeUnit.getId())) ) {

			new WTimeUnitDao().update(timeUnit);
			
		} else {
			
			logger.debug("WTimeUnitBL.update - nothing to do ...");
		}
		

					
	}
	
	public void delete(WTimeUnit timeUnit, Integer user) throws WTimeUnitException {

		logger.debug("delete() WTimeUnit - Name: ["+timeUnit.getName()+"]");
		
		new WTimeUnitDao().delete(timeUnit);

	}

	public WTimeUnit getWTimeUnitByPK(Integer id, Integer user) throws WTimeUnitException {

		return new WTimeUnitDao().getWTimeUnitByPK(id);
	}
	
	
	public WTimeUnit getWTimeUnitByName(String name, Integer user) throws WTimeUnitException {

		return new WTimeUnitDao().getWTimeUnitByName(name);
	}

	
	public List<WTimeUnit> getWTimeUnits(Integer user) throws WTimeUnitException {

		return new WTimeUnitDao().getWTimeUnits();
	
	}
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws WTimeUnitException {
		 
		return new WTimeUnitDao().getComboList(textoPrimeraLinea, separacion);


	}

}
	