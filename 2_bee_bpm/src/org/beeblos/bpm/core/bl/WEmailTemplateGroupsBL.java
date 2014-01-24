package org.beeblos.bpm.core.bl;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WEmailTemplateGroupsDao;
import org.beeblos.bpm.core.error.WEmailTemplateGroupsException;
import org.beeblos.bpm.core.model.WEmailTemplateGroups;

import com.sp.common.util.StringPair;

public class WEmailTemplateGroupsBL {

	private static final Log logger = LogFactory
			.getLog(WEmailTemplateGroupsBL.class);

	public WEmailTemplateGroupsBL() {

	}

	public Integer add(WEmailTemplateGroups instance, Integer idCurrentUser)
			throws WEmailTemplateGroupsException {

		logger.debug("WEmailTemplateGroupsBL:add()");

		_consistencyDataControl(instance);
		_noRedundancyControl(instance);

		WEmailTemplateGroupsDao wEmailTemplateGroupsDao = new WEmailTemplateGroupsDao();

		return wEmailTemplateGroupsDao.add(instance);

	}

	public void update(WEmailTemplateGroups instance, Integer idCurrentUser)
			throws WEmailTemplateGroupsException {

		logger.debug("WEmailTemplateGroupsBL:update()");

		WEmailTemplateGroupsDao wEmailTemplateGroupsDao = new WEmailTemplateGroupsDao();

		_consistencyDataControl(instance);
		_noRedundancyUpdateControl(instance);

		wEmailTemplateGroupsDao.update(instance);

	}

	public void delete(WEmailTemplateGroups instance, Integer idCurrentUser)
			throws WEmailTemplateGroupsException {

		logger.debug("WEmailTemplateGroupsBL:delete()");

		WEmailTemplateGroupsDao wEmailTemplateGroupsDao = new WEmailTemplateGroupsDao();
		wEmailTemplateGroupsDao.delete(instance);

	}

	public List<WEmailTemplateGroups> getWEmailTemplateGroupsList()
			throws WEmailTemplateGroupsException {

		WEmailTemplateGroupsDao wEmailTemplateGroupsDao = new WEmailTemplateGroupsDao();
		return wEmailTemplateGroupsDao.getWEmailTemplateGroupsList();

	}

	public List<StringPair> getComboList(String firstLineText,
			String blank) throws WEmailTemplateGroupsException {

		WEmailTemplateGroupsDao wEmailTemplateGroupsDao = new WEmailTemplateGroupsDao();
		return wEmailTemplateGroupsDao.getComboList( firstLineText, blank);

	}

	public WEmailTemplateGroups getWEmailTemplateGroupsByPK(Integer pk)
			throws WEmailTemplateGroupsException {
		WEmailTemplateGroupsDao wEmailTemplateGroupsDao = new WEmailTemplateGroupsDao();
		return wEmailTemplateGroupsDao.getWEmailTemplateGroupsByPK(pk);
	}

	public WEmailTemplateGroups getWEmailTemplateGroupsByName(String name)
			throws WEmailTemplateGroupsException {
		WEmailTemplateGroupsDao wEmailTemplateGroupsDao = new WEmailTemplateGroupsDao();
		return wEmailTemplateGroupsDao.getWEmailTemplateGroupsByName(name);
	}

/*
	public List<WEmailTemplateGroups> wEmailTemplateGroupsFinder(String nameFilter, String emailFilter)
			throws WEmailTemplateGroupsException {
		WEmailTemplateGroupsDao wEmailTemplateGroupsDao = new WEmailTemplateGroupsDao();
		return wEmailTemplateGroupsDao.wEmailTemplateGroupsFinder(nameFilter, emailFilter);
	}
*/
    private void _consistencyDataControl(WEmailTemplateGroups instance)
			throws WEmailTemplateGroupsException {

		String nombreTmp = instance.getName();

		if (nombreTmp == null || "".equals(nombreTmp) || " ".equals(nombreTmp)
				|| "".equals(nombreTmp.trim())) {
			throw new WEmailTemplateGroupsException(
					"The email you are trying to add HAS NOT A VALID NAME.");
		}

	}

	private void _noRedundancyControl(WEmailTemplateGroups instance)
			throws WEmailTemplateGroupsException {

		WEmailTemplateGroupsDao wEmailTemplateGroupsDao = new WEmailTemplateGroupsDao();

		String nombreTmp = instance.getName();

		if ((wEmailTemplateGroupsDao.getWEmailTemplateGroupsByName(nombreTmp) != null)) {
			throw new WEmailTemplateGroupsException(
					"There are already an email account with name: "
							+ nombreTmp);
		}

	}

	private void _noRedundancyUpdateControl(WEmailTemplateGroups instance)
			throws WEmailTemplateGroupsException {

		WEmailTemplateGroupsDao wEmailTemplateGroupsDao = new WEmailTemplateGroupsDao();

		String nombreTmp = instance.getName();

		if (wEmailTemplateGroupsDao.duplicatedNameVerification(
				instance.getName(), instance.getId())) {
			throw new WEmailTemplateGroupsException(
					"There are already an email account with name: "
							+ nombreTmp);
		}

	}

}
