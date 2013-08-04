package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.EnvironmentDao;
import org.beeblos.bpm.core.error.EnvironmentException;
import org.beeblos.bpm.core.model.Environment;
import com.sp.common.util.StringPair;

public class EnvironmentBL {

	private static final Log logger = LogFactory.getLog(EnvironmentBL.class.getName());

	public EnvironmentBL() {

	}

	public Integer add(Environment environment, Integer currentUserId) throws EnvironmentException {

		logger.debug("add() Environment - Name: [" + environment.getName() + "]");

		_emptyFieldsControl(environment);
		_noRedundancyInsertControl(environment);

		// timestamp & trace info
		environment.setAddDate(new Date());
		environment.setModDate(DEFAULT_MOD_DATE);
		environment.setAddUser(currentUserId);
		environment.setModUser(currentUserId);
		return new EnvironmentDao().add(environment);

	}

	public void update(Environment environment, Integer currentUserId) throws EnvironmentException {

		logger.debug("update() Environment < id = " + environment.getId() + ">");
		
		if (!environment.equals(new EnvironmentDao().getEnvironmentByPK(environment.getId()))) {

			_emptyFieldsControl(environment);
			_noRedundancyUpdateControl(environment);

			environment.setModDate(new Date());
			environment.setModUser(currentUserId);
			new EnvironmentDao().update(environment);

		} else {

			logger.debug("EnvironmentBL.update - nothing to do ...");
		}

	}

	public void delete(Environment environment) throws EnvironmentException {

		logger.debug("delete() Environment - Name: [" + environment.getName() + "]");

		new EnvironmentDao().delete(environment);

	}

	public Environment getEnvironmentByPK(Integer id) throws EnvironmentException {

		return new EnvironmentDao().getEnvironmentByPK(id);
	}

	public Environment getEnvironmentByName(String name) throws EnvironmentException {

		return new EnvironmentDao().getEnvironmentByName(name);
	}

	public List<Environment> getEnvironments() throws EnvironmentException {

		return new EnvironmentDao().getEnvironments();

	}

	public List<StringPair> getComboList(String firstLineText, String separation)
			throws EnvironmentException {

		return new EnvironmentDao().getComboList(firstLineText, separation);

	}

	private void _emptyFieldsControl(Environment environment) throws EnvironmentException {

		String tmpName = environment.getName();

		if (tmpName == null || "".equals(tmpName.trim())) {
			throw new EnvironmentException("The environment HAS NOT a valid name.");
		}

	}

	private void _noRedundancyInsertControl(Environment environment) throws EnvironmentException {

		EnvironmentDao environmentDao = new EnvironmentDao();

		String nameTmp = environment.getName();

		if ((environmentDao.getEnvironmentByName(nameTmp) != null)) {
			throw new EnvironmentException("An environment with name [" + nameTmp
					+ "] already exists.");
		}

	}

	private void _noRedundancyUpdateControl(Environment environment) throws EnvironmentException {

		EnvironmentDao environmentDao = new EnvironmentDao();

		String nameTmp = environment.getName();

		if (environmentDao.duplicatedNameVerification(environment.getName(), environment.getId())) {
			throw new EnvironmentException("An environment with name [" + nameTmp
					+ "] already exists.");
		}

	}

}
