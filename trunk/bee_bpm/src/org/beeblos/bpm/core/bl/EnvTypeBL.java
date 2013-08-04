package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.EnvTypeDao;
import org.beeblos.bpm.core.error.EnvTypeException;
import org.beeblos.bpm.core.model.EnvType;
import com.sp.common.util.StringPair;

public class EnvTypeBL {

	private static final Log logger = LogFactory.getLog(EnvTypeBL.class.getName());

	public EnvTypeBL() {

	}

	public Integer add(EnvType envType, Integer currentUserId) throws EnvTypeException {

		logger.debug("add() EnvType - Name: [" + envType.getName() + "]");

		_emptyFieldsControl(envType);
		_noRedundancyInsertControl(envType);

		// timestamp & trace info
		envType.setAddDate(new Date());
		envType.setModDate(DEFAULT_MOD_DATE);
		envType.setAddUser(currentUserId);
		envType.setModUser(currentUserId);
		return new EnvTypeDao().add(envType);

	}

	public void update(EnvType envType, Integer currentUserId) throws EnvTypeException {

		logger.debug("update() EnvType < id = " + envType.getId() + ">");
		
		if (!envType.equals(new EnvTypeDao().getEnvTypeByPK(envType.getId()))) {

			_emptyFieldsControl(envType);
			_noRedundancyUpdateControl(envType);

			envType.setModDate(new Date());
			envType.setModUser(currentUserId);
			new EnvTypeDao().update(envType);

		} else {

			logger.debug("EnvTypeBL.update - nothing to do ...");
		}

	}

	public void delete(EnvType envType, Integer currentUserId) throws EnvTypeException {

		logger.debug("delete() EnvType - Name: [" + envType.getName() + "]");

		new EnvTypeDao().delete(envType);

	}

	public EnvType getEnvTypeByPK(Short id, Integer currentUserId) throws EnvTypeException {

		return new EnvTypeDao().getEnvTypeByPK(id);
	}

	public EnvType getEnvTypeByName(String name) throws EnvTypeException {

		return new EnvTypeDao().getEnvTypeByName(name);
	}

	public List<EnvType> getEnvTypes(Integer currentUserId) throws EnvTypeException {

		return new EnvTypeDao().getEnvTypes();

	}

	public List<StringPair> getComboList(String firstLineText, String separation)
			throws EnvTypeException {

		return new EnvTypeDao().getComboList(firstLineText, separation);

	}

	private void _emptyFieldsControl(EnvType envType) throws EnvTypeException {

		String tmpName = envType.getName();

		if (tmpName == null || "".equals(tmpName.trim())) {
			throw new EnvTypeException("The envType HAS NOT a valid name.");
		}

	}

	private void _noRedundancyInsertControl(EnvType envType) throws EnvTypeException {

		EnvTypeDao envTypeDao = new EnvTypeDao();

		String nameTmp = envType.getName();

		if ((envTypeDao.getEnvTypeByName(nameTmp) != null)) {
			throw new EnvTypeException("An envType with name [" + nameTmp
					+ "] already exists.");
		}

	}

	private void _noRedundancyUpdateControl(EnvType envType) throws EnvTypeException {

		EnvTypeDao envTypeDao = new EnvTypeDao();

		String nameTmp = envType.getName();

		if (envTypeDao.duplicatedNameVerification(envType.getName(), envType.getId())) {
			throw new EnvTypeException("An envType with name [" + nameTmp
					+ "] already exists.");
		}

	}

}
