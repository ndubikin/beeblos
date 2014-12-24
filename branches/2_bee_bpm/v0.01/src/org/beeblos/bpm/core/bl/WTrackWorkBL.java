package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WTrackWorkDao;
import org.beeblos.bpm.core.error.WTrackWorkException;
import org.beeblos.bpm.core.model.WTrackWork;
import org.joda.time.DateTime;

import com.sp.common.util.StringPair;


public class WTrackWorkBL {

	private static final Log logger = LogFactory.getLog(WTrackWorkBL.class.getName());

	public WTrackWorkBL() {

	}

	public Integer add(WTrackWork trackw, Integer currentUser)
			throws WTrackWorkException {

		logger.debug("add() WTrackWork - Name: [" + trackw.getIdObjectType()
				+ "-" + trackw.getIdObjectType() + "]");

		// timestamp & trace info
		trackw.setInsertDate(new DateTime());
		trackw.setInsertUser(currentUser);
		trackw.setModDate(DEFAULT_MOD_DATE_TIME);
		trackw.setModUser(currentUser);
		return new WTrackWorkDao().add(trackw);

	}

	public void update(WTrackWork trackw, Integer currentUser)
			throws WTrackWorkException {

		logger.debug("update() WTrackWork < id = " + trackw.getId() + ">");

		trackw.setModDate( new DateTime() );
		trackw.setModUser(currentUser);

		if (!trackw
				.equals(new WTrackWorkDao().getWTrackWorkByPK(trackw.getId()))) {

			new WTrackWorkDao().update(trackw);

		} else {

			logger.debug("WTrackWorkBL.update - nothing to do ...");
		}

	}

	public void delete(WTrackWork trackw, Integer currentUser)
			throws WTrackWorkException {

		logger.info("delete() WTrackWork - Name: [" + trackw.getId()
				+ "] user:" + currentUser);

		new WTrackWorkDao().delete(trackw);

	}

	public WTrackWork getWTrackWorkByPK(Integer id, Integer currentUser)
			throws WTrackWorkException {

		return new WTrackWorkDao().getWTrackWorkByPK(id);
	}

	public WTrackWork getWTrackWorkByName(String name, Integer currentUser)
			throws WTrackWorkException {

		return new WTrackWorkDao().getWTrackWorkByName(name);
	}

	public List<WTrackWork> getWTrackWorks(Integer currentUser)
			throws WTrackWorkException {

		return new WTrackWorkDao().getWTrackWorks();

	}

	public List<StringPair> getComboList(String textoPrimeraLinea,
			String separacion) throws WTrackWorkException {

		return new WTrackWorkDao().getComboList(textoPrimeraLinea, separacion);

	}

	public List<WTrackWork> getTrackListByIdObject(Integer idObject,
			String idObjectType, Integer currentUser)
			throws WTrackWorkException {

		return new WTrackWorkDao().getTrackListByIdObject(idObject,
				idObjectType, currentUser);

	}

	public List<WTrackWork> getTrackListByProcess(Integer idProcess)
			throws WTrackWorkException {

		return new WTrackWorkDao().getTrackListByProcess(idProcess);

	}

	public List<WTrackWork> getTrackListByCurrentStep(Integer idCurrentStep)
			throws WTrackWorkException {

		return new WTrackWorkDao().getTrackListByCurrentStep(idCurrentStep);

	}

	public List<WTrackWork> getTrackListByInsertUser(Integer idUser)
			throws WTrackWorkException {

		return new WTrackWorkDao().getTrackListByInsertUser(idUser);

	}

	public List<WTrackWork> getTrackListByUserNotes(String userNotes)
			throws WTrackWorkException {

		return new WTrackWorkDao().getTrackListByUserNotes(userNotes);

	}

}
