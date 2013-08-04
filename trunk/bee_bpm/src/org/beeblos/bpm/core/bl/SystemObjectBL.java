package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE_TIME;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import org.beeblos.bpm.core.dao.SystemObjectDao;
import org.beeblos.bpm.core.error.SystemObjectException;
import org.beeblos.bpm.core.model.SystemObject;
import com.sp.common.util.StringPair;

public class SystemObjectBL {

	private static final Log logger = LogFactory.getLog(SystemObjectBL.class.getName());

	public SystemObjectBL() {

	}

	public Integer add(SystemObject systemObject, Integer user) throws SystemObjectException {

		logger.debug("add() SystemObject - Name: [" + systemObject.getName() + "]");

		_emptyFieldsControl(systemObject);
		_noRedundancyInsertControl(systemObject);

		// timestamp & trace info
		systemObject.setAddDate(new DateTime());
		systemObject.setModDate(DEFAULT_MOD_DATE_TIME);
		systemObject.setAddUser(user);
		systemObject.setModUser(user);
		return new SystemObjectDao().add(systemObject);

	}

	public void update(SystemObject systemObject, Integer user) throws SystemObjectException {

		logger.debug("update() SystemObject < id = " + systemObject.getId() + ">");
		
		if (!systemObject.equals(new SystemObjectDao().getSystemObjectByPK(systemObject.getId()))) {

			_emptyFieldsControl(systemObject);
			_noRedundancyUpdateControl(systemObject);

			systemObject.setModDate(new DateTime());
			systemObject.setModUser(user);
			new SystemObjectDao().update(systemObject);

		} else {

			logger.debug("SystemObjectBL.update - nothing to do ...");
		}

	}

	public void delete(SystemObject systemObject) throws SystemObjectException {

		logger.debug("delete() SystemObject - Name: [" + systemObject.getName() + "]");

		new SystemObjectDao().delete(systemObject);

	}

	public SystemObject getSystemObjectByPK(Integer id) throws SystemObjectException {

		return new SystemObjectDao().getSystemObjectByPK(id);
	}

	public SystemObject getSystemObjectByName(String name) throws SystemObjectException {

		return new SystemObjectDao().getSystemObjectByName(name);
	}

	public List<SystemObject> getSystemObjects() throws SystemObjectException {

		return new SystemObjectDao().getSystemObjects();

	}

	public List<StringPair> getComboList(String firstLineText, String separation)
			throws SystemObjectException {

		return new SystemObjectDao().getComboList(firstLineText, separation);

	}

	private void _emptyFieldsControl(SystemObject systemObject) throws SystemObjectException {

		String tmpName = systemObject.getName();

		if (tmpName == null || "".equals(tmpName.trim())) {
			throw new SystemObjectException("The systemObject HAS NOT a valid name.");
		}

	}

	private void _noRedundancyInsertControl(SystemObject systemObject) throws SystemObjectException {

		SystemObjectDao systemObjectDao = new SystemObjectDao();

		String nameTmp = systemObject.getName();

		if ((systemObjectDao.getSystemObjectByName(nameTmp) != null)) {
			throw new SystemObjectException("An systemObject with name [" + nameTmp
					+ "] already exists.");
		}

	}

	private void _noRedundancyUpdateControl(SystemObject systemObject) throws SystemObjectException {

		SystemObjectDao systemObjectDao = new SystemObjectDao();

		String nameTmp = systemObject.getName();

		if (systemObjectDao.duplicatedNameVerification(systemObject.getName(), systemObject.getId())) {
			throw new SystemObjectException("An systemObject with name [" + nameTmp
					+ "] already exists.");
		}

	}

}
