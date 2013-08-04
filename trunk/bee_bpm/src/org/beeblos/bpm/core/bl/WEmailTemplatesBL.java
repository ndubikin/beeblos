package org.beeblos.bpm.core.bl;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WEmailTemplatesDao;
import org.beeblos.bpm.core.error.WEmailTemplatesException;
import org.beeblos.bpm.core.model.WEmailTemplates;
import com.sp.common.util.StringPair;

public class WEmailTemplatesBL {

	private static final Log logger = LogFactory
			.getLog(WEmailTemplatesBL.class);

	public WEmailTemplatesBL() {

	}

	public Integer add(WEmailTemplates instance, Integer idCurrentUser)
			throws WEmailTemplatesException {

		logger.debug("WEmailTemplatesBL:add()");

		_consistencyDataControl(instance);
		_noRedundancyControl(instance);

		WEmailTemplatesDao wEmailTemplatesDao = new WEmailTemplatesDao();

		return wEmailTemplatesDao.add(instance);

	}

	public void update(WEmailTemplates instance, Integer idCurrentUser)
			throws WEmailTemplatesException {

		logger.debug("WEmailTemplatesBL:update()");

		WEmailTemplatesDao wEmailTemplatesDao = new WEmailTemplatesDao();

		_consistencyDataControl(instance);
		_noRedundancyUpdateControl(instance);

		wEmailTemplatesDao.update(instance);

	}

	public void delete(WEmailTemplates instance, Integer idCurrentUser)
			throws WEmailTemplatesException {

		logger.debug("WEmailTemplatesBL:delete()");

		WEmailTemplatesDao wEmailTemplatesDao = new WEmailTemplatesDao();
		wEmailTemplatesDao.delete(instance);

	}

	public List<WEmailTemplates> getWEmailTemplatesList()
			throws WEmailTemplatesException {

		WEmailTemplatesDao wEmailTemplatesDao = new WEmailTemplatesDao();
		return wEmailTemplatesDao.getWEmailTemplatesList();

	}

	public List<StringPair> getComboList(String firstLineText,
			String blank) throws WEmailTemplatesException {

		WEmailTemplatesDao wEmailTemplatesDao = new WEmailTemplatesDao();
		return wEmailTemplatesDao.getComboList( firstLineText, blank);

	}

	public WEmailTemplates getWEmailTemplatesByPK(Integer pk)
			throws WEmailTemplatesException {
		WEmailTemplatesDao wEmailTemplatesDao = new WEmailTemplatesDao();
		return wEmailTemplatesDao.getWEmailTemplatesByPK(pk);
	}

	public WEmailTemplates getWEmailTemplatesByName(String name)
			throws WEmailTemplatesException {
		WEmailTemplatesDao wEmailTemplatesDao = new WEmailTemplatesDao();
		return wEmailTemplatesDao.getWEmailTemplatesByName(name);
	}


	public List<WEmailTemplates> wEmailTemplatesFinder(String nameFilter, String typeFilter)
			throws WEmailTemplatesException {
		WEmailTemplatesDao wEmailTemplatesDao = new WEmailTemplatesDao();
		return wEmailTemplatesDao.wEmailTemplatesFinder(nameFilter, typeFilter);
	}

    private void _consistencyDataControl(WEmailTemplates instance)
			throws WEmailTemplatesException {

		String nombreTmp = instance.getName();

		if (nombreTmp == null || "".equals(nombreTmp) || " ".equals(nombreTmp)
				|| "".equals(nombreTmp.trim())) {
			throw new WEmailTemplatesException(
					"The email you are trying to add HAS NOT A VALID NAME.");
		}

	}

	private void _noRedundancyControl(WEmailTemplates instance)
			throws WEmailTemplatesException {

		WEmailTemplatesDao wEmailTemplatesDao = new WEmailTemplatesDao();

		String nombreTmp = instance.getName();

		if ((wEmailTemplatesDao.getWEmailTemplatesByName(nombreTmp) != null)) {
			throw new WEmailTemplatesException(
					"There are already an email account with name: "
							+ nombreTmp);
		}

	}

	private void _noRedundancyUpdateControl(WEmailTemplates instance)
			throws WEmailTemplatesException {

		WEmailTemplatesDao wEmailTemplatesDao = new WEmailTemplatesDao();

		String nombreTmp = instance.getName();

		if (wEmailTemplatesDao.duplicatedNameVerification(
				instance.getName(), instance.getId())) {
			throw new WEmailTemplatesException(
					"There are already an email account with name: "
							+ nombreTmp);
		}

	}

}
