package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.ObjectDao;
import org.beeblos.bpm.core.error.ObjectException;
import org.beeblos.bpm.core.model.ObjectM;
import org.joda.time.DateTime;

import com.sp.common.util.StringPair;


public class ObjectBL {
	
	private static final Log logger = LogFactory.getLog(ObjectBL.class.getName());
	
	public ObjectBL (){
		
	}
	
	public Integer add(ObjectM object, Integer currentUser) throws ObjectException {
		
		logger.debug("add() Object - Name: ["+object.getName()+"]");
		
		// timestamp & trace info
		object.setInsertDate(new DateTime());
		object.setModDate(DEFAULT_MOD_DATE_TIME);
		object.setInsertUser(currentUser);
		object.setModUser(currentUser);
		return new ObjectDao().add(object);

	}
	
	
	public void update(ObjectM object, Integer currentUser) throws ObjectException {
		
		logger.debug("update() Object < id = "+object.getId()+">");
		
		if (!object.equals(new ObjectDao().getObjectMByPK(object.getId())) ) {

			// timestamp & trace info
			object.setModDate(new DateTime());
			object.setModUser(currentUser);
			new ObjectDao().update(object);
			
		} else {
			
			logger.debug("ObjectBL.update - nothing to do ...");
		}
		

					
	}
	
	
	public void delete(ObjectM object, Integer currentUser) throws ObjectException {

		logger.debug("delete() Object - Name: ["+object.getName()+"]");
		
		new ObjectDao().delete(object);

	}

	public ObjectM getObjectByPK(Integer id, Integer currentUser) throws ObjectException {

		return new ObjectDao().getObjectMByPK(id);
	}
	
	
	public ObjectM getObjectByName(String name, Integer currentUser) throws ObjectException {

		return new ObjectDao().getObjectMByName(name);
	}

	
	public List<ObjectM> getObjects(Integer currentUser) throws ObjectException {

		return new ObjectDao().getObjectMs();
	
	}
	
	
	public List<StringPair> getComboList(
			String textoPrimeraLinea, String separacion )
	throws ObjectException {
		 
		return new ObjectDao().getComboList(textoPrimeraLinea, separacion);


	}

}
	