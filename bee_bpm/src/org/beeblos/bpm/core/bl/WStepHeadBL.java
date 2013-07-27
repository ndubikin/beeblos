package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepHeadDao;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepHeadException;
import org.beeblos.bpm.core.model.WStepHead;
import org.beeblos.bpm.core.model.noper.StringPair;

public class WStepHeadBL {

	private static final Log logger = LogFactory.getLog(WStepHeadBL.class.getName());

	public WStepHeadBL() {

	}

	public Integer add(WStepHead stepHead, Integer currentUserId) throws WStepHeadException {

		logger.debug("add() WStepHead - Name: [" + stepHead.getName() + "]");

		// timestamp & trace info
		stepHead.setInsertDate(new Date());
		stepHead.setModDate(DEFAULT_MOD_DATE);
		stepHead.setInsertUser(currentUserId);
		stepHead.setModUser(currentUserId);
		return new WStepHeadDao().add(stepHead);

	}

	// dml 20130508
	public Integer addStepAndFirstWStepDef(WStepHead stepHead, Integer currentUserId) throws WStepHeadException, WStepDefException {
		
		logger.debug("addStepAndFirstWStepDef() WStepHead - Name: ["+stepHead.getName()+"]");
		
		Integer stepHeadId = this.add(stepHead, currentUserId);
		stepHead.setId(stepHeadId);
		
		new WStepDefBL().createFirstWStepDef(stepHeadId, null, null, null, currentUserId);
		
		return stepHeadId;

	}
		
	public void update(WStepHead stepHead, Integer currentUserId) throws WStepHeadException {

		logger.debug("update() WStepHead < id = " + stepHead.getId() + ">");

		if (!stepHead.equals(new WStepHeadDao().getStepDefByPK(stepHead.getId()))) {

			// timestamp & trace info
			stepHead.setModDate(new Date());
			stepHead.setModUser(currentUserId);
			new WStepHeadDao().update(stepHead);

		} else {

			logger.debug("WStepHeadBL.update - nothing to do ...");
		}

	}

	public void delete(WStepHead stepHead, Integer currentUserId) throws WStepHeadException {

		logger.info("delete() WStepHead - Name: [" + stepHead.getName() + "] id:["
				+ stepHead.getId() + "]");

		new WStepHeadDao().delete(stepHead);

	}

	public WStepHead getWStepHeadByPK(Integer id, Integer userId) throws WStepHeadException {

		return new WStepHeadDao().getStepDefByPK(id);
	}

	public WStepHead getWStepHeadByName(String name, Integer userId) throws WStepHeadException {

		return new WStepHeadDao().getStepDefByName(name);
	}

	public List<WStepHead> getStepDefs(Integer userId) throws WStepHeadException {

		// nota: falta revisar el tema de los permisos de usuario para esto ...
		return new WStepHeadDao().getWStepHeads();

	}

	public List<StringPair> getComboList(String firstLineText, String blank)
			throws WStepHeadException {

		return new WStepHeadDao().getComboList(firstLineText, blank);

	}

	public List<WStepHead> getStepListByFinder(String nameFilter, String commentFilter,
			Integer userId, boolean isAdmin, String action) throws WStepHeadException {

		return new WStepHeadDao().getStepListByFinder(nameFilter, commentFilter, userId, isAdmin,
				action);

	}

	// dml 20130129
	public boolean headStepHasWStepDef(Integer stepHeadId) throws WStepHeadException{
		
		return new WStepHeadDao().headStepHasWStepDef(stepHeadId);
		
	}

}
