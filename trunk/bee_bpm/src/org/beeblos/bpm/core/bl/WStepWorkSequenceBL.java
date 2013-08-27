package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepWorkSequenceDao;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WStepWorkSequence;

import com.sp.common.util.StringPair;

public class WStepWorkSequenceBL {

	private static final Log logger = LogFactory.getLog(WStepWorkSequenceBL.class.getName());

	public WStepWorkSequenceBL() {

	}

	public Integer add(WStepWorkSequence stepWorkSequence, Integer currentUserId) throws WStepWorkSequenceException {

		logger.debug("add() WStepWorkSequence");

		_emptyFieldsControl(stepWorkSequence);

		// timestamp & trace info
		stepWorkSequence.setInsertDate(new Date());
		stepWorkSequence.setModDate(DEFAULT_MOD_DATE);
		stepWorkSequence.setInsertUser(currentUserId);
		stepWorkSequence.setModUser(currentUserId);
		return new WStepWorkSequenceDao().add(stepWorkSequence);

	}

	public void update(WStepWorkSequence stepWorkSequence, Integer currentUserId) throws WStepWorkSequenceException {

		logger.debug("update() WStepWorkSequence < id = " + stepWorkSequence.getId() + ">");
		
		if (!stepWorkSequence.equals(new WStepWorkSequenceDao().getWStepWorkSequenceByPK(stepWorkSequence.getId()))) {

			_emptyFieldsControl(stepWorkSequence);

			stepWorkSequence.setModDate(new Date());
			stepWorkSequence.setModUser(currentUserId);
			new WStepWorkSequenceDao().update(stepWorkSequence);

		} else {

			logger.debug("WStepWorkSequenceBL.update - nothing to do ...");
		}

	}

	public void delete(WStepWorkSequence stepWorkSequence) throws WStepWorkSequenceException {

		logger.debug("delete() WStepWorkSequence < id = " + stepWorkSequence.getId() + ">");

		new WStepWorkSequenceDao().delete(stepWorkSequence);

	}

	public WStepWorkSequence getWStepWorkSequenceByPK(Integer id) throws WStepWorkSequenceException {

		return new WStepWorkSequenceDao().getWStepWorkSequenceByPK(id);
	}

	public List<WStepWorkSequence> getWStepWorkSequences() throws WStepWorkSequenceException {

		return new WStepWorkSequenceDao().getWStepWorkSequences();

	}

	// dml 20130827
	public List<WStepWorkSequence> getWStepWorkSequencesByWorkingProcessId(Integer workingProcessId, Integer currentUserId) 
			throws WStepWorkSequenceException {

		return new WStepWorkSequenceDao().getWStepWorkSequencesByWorkingProcessId(workingProcessId);

	}

	public List<StringPair> getComboList(String firstLineText, String separation)
			throws WStepWorkSequenceException {

		return new WStepWorkSequenceDao().getComboList(firstLineText, separation);

	}

	private void _emptyFieldsControl(WStepWorkSequence stepWorkSequence) throws WStepWorkSequenceException {

		WStepWork tmpStepWork = stepWorkSequence.getStepWork();

		if (tmpStepWork == null || tmpStepWork.getId() == null || tmpStepWork.getId().equals(0)) {
			throw new WStepWorkSequenceException("The stepWorkSequence HAS NOT a valid WStepWork.");
		}

	}


}
