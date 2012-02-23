package org.beeblos.security.st.bl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.security.st.dao.WUserEmailAccountsDao;
import org.beeblos.security.st.error.WUserEmailAccountsException;
import org.beeblos.security.st.model.WUserEmailAccounts;

public class WUserEmailAccountsBL {

	private static final Log logger = LogFactory
			.getLog(WUserEmailAccountsBL.class);

	public WUserEmailAccountsBL() {

	}

	public Integer add(WUserEmailAccounts instance, Integer idCurrentUser)
			throws WUserEmailAccountsException {

		logger.debug("WUserEmailAccountsBL:add()");

		_consistencyDataControl(instance);
		_noRedundancyControl(instance);

		WUserEmailAccountsDao wUserEmailAccountsDao = new WUserEmailAccountsDao();

		if (instance.isUserDefaultAccount()) {
			WUserEmailAccounts previusDefaultAccount = wUserEmailAccountsDao
					.getDefaultAccount(instance.getwUserDef().getId());
			if (previusDefaultAccount != null) {

				previusDefaultAccount.setUserDefaultAccount(false);

				wUserEmailAccountsDao.update(previusDefaultAccount);
			}
		}

		return wUserEmailAccountsDao.add(instance);

	}

	public void update(WUserEmailAccounts instance, Integer idCurrentUser)
			throws WUserEmailAccountsException {

		logger.debug("WUserEmailAccountsBL:update()");

		WUserEmailAccountsDao wUserEmailAccountsDao = new WUserEmailAccountsDao();

		_consistencyDataControl(instance);
		_noRedundancyUpdateControl(instance);

		if (instance.isUserDefaultAccount()) {
			WUserEmailAccounts previusDefaultAccount = wUserEmailAccountsDao
					.getDefaultAccount(instance.getwUserDef().getId());

			if (previusDefaultAccount != null
					&& instance.getId() != previusDefaultAccount.getId()) {

				previusDefaultAccount.setUserDefaultAccount(false);

				wUserEmailAccountsDao.update(previusDefaultAccount);
			}
		}

		new WUserEmailAccountsDao().update(instance);

	}

	public void delete(WUserEmailAccounts instance, Integer idCurrentUser)
			throws WUserEmailAccountsException {

		logger.debug("WUserEmailAccountsBL:delete()");

		WUserEmailAccountsDao wUserEmailAccountsDao = new WUserEmailAccountsDao();
		wUserEmailAccountsDao.delete(instance);

	}

	public List<WUserEmailAccounts> getWUserEmailAccountsList()
			throws WUserEmailAccountsException {

		WUserEmailAccountsDao wUserEmailAccountsDao = new WUserEmailAccountsDao();
		return wUserEmailAccountsDao.getWUserEmailAccountsList();

	}

	public List<StringPair> getWUserEmailAccountsComboList(
			Integer idSpeficicUser, String firstLineText,
			String blank) throws WUserEmailAccountsException {

		WUserEmailAccountsDao wUserEmailAccountsDao = new WUserEmailAccountsDao();
		return wUserEmailAccountsDao.getWUserEmailAccountsComboList(
				idSpeficicUser, firstLineText, blank);

	}

	public WUserEmailAccounts getWUserEmailAccountsByPK(Integer pk)
			throws WUserEmailAccountsException {
		WUserEmailAccountsDao wUserEmailAccountsDao = new WUserEmailAccountsDao();
		return wUserEmailAccountsDao.getWUserEmailAccountsByPK(pk);
	}

	public WUserEmailAccounts getWUserEmailAccountsByName(String name)
			throws WUserEmailAccountsException {
		WUserEmailAccountsDao wUserEmailAccountsDao = new WUserEmailAccountsDao();
		return wUserEmailAccountsDao.getWUserEmailAccountsByName(name);
	}

	public List<WUserEmailAccounts> getWUserEmailAccountsListByUser(
			Integer idSpecifiUser) throws WUserEmailAccountsException {
		WUserEmailAccountsDao wUserEmailAccountsDao = new WUserEmailAccountsDao();
		return wUserEmailAccountsDao
				.getWUserEmailAccountsListByUser(idSpecifiUser);
	}

	private void _consistencyDataControl(WUserEmailAccounts instance)
			throws WUserEmailAccountsException {

		String nombreTmp = instance.getName();

		if (nombreTmp == null || "".equals(nombreTmp) || " ".equals(nombreTmp)
				|| "".equals(nombreTmp.trim())) {
			throw new WUserEmailAccountsException(
					"The email you are trying to add HAS NOT A VALID NAME.");
		}

	}

	private void _noRedundancyControl(WUserEmailAccounts instance)
			throws WUserEmailAccountsException {

		WUserEmailAccountsDao wUserEmailAccountsDao = new WUserEmailAccountsDao();

		String nombreTmp = instance.getName();

		if ((wUserEmailAccountsDao.getWUserEmailAccountsByName(nombreTmp) != null)) {
			throw new WUserEmailAccountsException(
					"There are already an email account with name: "
							+ nombreTmp);
		}

	}

	private void _noRedundancyUpdateControl(WUserEmailAccounts instance)
			throws WUserEmailAccountsException {

		WUserEmailAccountsDao wUserEmailAccountsDao = new WUserEmailAccountsDao();

		String nombreTmp = instance.getName();

		if (wUserEmailAccountsDao.duplicatedNameVerification(
				instance.getName(), instance.getId())) {
			throw new WUserEmailAccountsException(
					"There are already an email account with name: "
							+ nombreTmp);
		}

	}

}
