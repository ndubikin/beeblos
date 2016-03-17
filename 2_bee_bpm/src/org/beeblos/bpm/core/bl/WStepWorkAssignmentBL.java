package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepWorkAssignmentDao;
import org.beeblos.bpm.core.dao.WStepWorkDao;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.model.WStepWorkAssignment;
import org.joda.time.DateTime;

public class WStepWorkAssignmentBL {
	
	private static final String INSERT = "INSERT";
	private static final Log logger = LogFactory.getLog(WStepWorkAssignmentBL.class.getName());
	
	public WStepWorkAssignmentBL (){
		
	}
	

	public Integer add(WStepWorkAssignment instance, Integer currentUser) throws WStepWorkException {
		
		logger.debug(">> add() WStepWorkAssignmentBL");
		
		// timestamp & trace info
		instance.setInsertDate(new DateTime());
		instance.setInsertUser(currentUser);
		instance.setModDate( DEFAULT_MOD_DATE_TIME);
		instance.setModUser(currentUser);
		return new WStepWorkAssignmentDao().add(instance);

	}


	/**
	 * updates a step work assignment
	 * 
	 * nes 20140707 - added currentUserId as param
	 * 
	 * @param instance
	 * @param currentUserId
	 * @throws WStepWorkException
	 */
	public void update(WStepWorkAssignment instance, Integer currentUserId) throws WStepWorkException {
		logger.debug(">>> update WStepWorkAssignment id:" 
				+ (instance!=null && instance.getId()!=null?instance.getId():"null") );
		
		if ( !instance.equals(new WStepWorkDao().getWStepWorkByPK(instance.getId())) ) {

			// timestamp & trace info
			instance.setModDate(new DateTime());
			instance.setModUser(currentUserId);

			new WStepWorkAssignmentDao().update(instance,currentUserId);

		}

	}

	/**
	 * 
	 * @param stepWorkId
	 * @param currentUser
	 * @return
	 * @throws WStepWorkException
	 */
	public List<WStepWorkAssignment> getByWStepWorkId(
			Integer stepWorkId, Integer currentUser ) 
					throws WStepWorkException {
		
		return new WStepWorkAssignmentDao().getByWStepWorkId(stepWorkId, currentUser);
	}
	
	}