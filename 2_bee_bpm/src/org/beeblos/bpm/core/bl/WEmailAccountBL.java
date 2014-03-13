package org.beeblos.bpm.core.bl;


import static com.email.core.util.ConstantsEmailCore.COPY_OF;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WEmailAccountDao;
import org.beeblos.bpm.core.error.WEmailAccountException;
import org.beeblos.bpm.core.model.WEmailAccount;

import com.sp.common.util.StringPair;

public class WEmailAccountBL {

	private static final Log logger = LogFactory
			.getLog(WEmailAccountBL.class);

	public WEmailAccountBL() {

	}

	public Integer add(WEmailAccount instance, Integer idCurrentUser)
			throws WEmailAccountException {

		logger.debug("WEmailAccountBL:add()");

		_consistencyDataControl(instance);
		_noRedundancyControl(instance);

		if (instance.isUserDefaultAccount()) {
			_checkDefaultEmailAccountConsistency(instance);
		}
		
		return new WEmailAccountDao().add(instance);

	}

	// dml 20130109
	public WEmailAccount createNewUEACopyingData(WEmailAccount uea, Integer userId) throws WEmailAccountException {

		if (uea != null){
			
			uea.setId(null);
			uea.setName(COPY_OF + " " + uea.getName());
			
			Integer newUEAId = this.add(uea, userId);
			
			uea.setId(newUEAId);
			
		}
		
		return uea;
		
	}
	public void update(WEmailAccount instance, Integer idCurrentUser)
			throws WEmailAccountException {

		logger.debug("WEmailAccountBL:update()");

		_consistencyDataControl(instance);
		_noRedundancyUpdateControl(instance);

		if (instance.isUserDefaultAccount()) {
			_checkDefaultEmailAccountConsistency(instance);
		}

		new WEmailAccountDao().update(instance);

	}

	private void _checkDefaultEmailAccountConsistency(WEmailAccount instance)
			throws WEmailAccountException {
		
		WEmailAccountDao wea = new WEmailAccountDao();
		
		List<WEmailAccount> previusDefaultAccountList = wea
				.getDefaultAccountList(instance.getwUserDef().getId());

		for (WEmailAccount pea: previusDefaultAccountList) {
			
			if ( pea.isUserDefaultAccount() ) {

				pea.setUserDefaultAccount(false);

				wea.update(pea);
			}
			
		}
		

		
	}

	// dml 20130111
	public void updateUEAPassword(Integer ueaId, String newPassword, Integer userId) throws WEmailAccountException {

		logger.debug("UserEmailAccountBL:updateUEAPassword()");

		WEmailAccount uea = this.getWEmailAccountByPK(ueaId);

		uea.setOutputPassword(newPassword);

		new WEmailAccountDao().update(uea);

	}

	public void delete(WEmailAccount instance, Integer idCurrentUser)
			throws WEmailAccountException {

		logger.debug("WEmailAccountBL:delete()");

		WEmailAccountDao wEmailAccountDao = new WEmailAccountDao();
		wEmailAccountDao.delete(instance);

	}

	public List<WEmailAccount> getWEmailAccountList()
			throws WEmailAccountException {

		WEmailAccountDao wEmailAccountDao = new WEmailAccountDao();
		return wEmailAccountDao.getWEmailAccountList();

	}

	public List<StringPair> getWEmailAccountComboList(
			Integer idSpeficicUser, String firstLineText,
			String blank) throws WEmailAccountException {

		WEmailAccountDao wEmailAccountDao = new WEmailAccountDao();
		return wEmailAccountDao.getWEmailAccountComboList(
				idSpeficicUser, firstLineText, blank);

	}

	public WEmailAccount getWEmailAccountByPK(Integer pk)
			throws WEmailAccountException {
		WEmailAccountDao wEmailAccountDao = new WEmailAccountDao();
		return wEmailAccountDao.getWEmailAccountByPK(pk);
	}

	public WEmailAccount getWEmailAccountByName(String name)
			throws WEmailAccountException {
		WEmailAccountDao wEmailAccountDao = new WEmailAccountDao();
		return wEmailAccountDao.getWEmailAccountByName(name);
	}

	public List<WEmailAccount> getWEmailAccountListByUser(
			Integer idSpecifiUser) throws WEmailAccountException {
		WEmailAccountDao wEmailAccountDao = new WEmailAccountDao();
		return wEmailAccountDao
				.getWEmailAccountListByUser(idSpecifiUser);
	}

	public List<WEmailAccount> finderWEmailAccount(String nameFilter, Integer userId, String action, String searchOrder)
			throws WEmailAccountException {

		return new WEmailAccountDao().finderWEmailAccount(nameFilter, userId, action, searchOrder);

	}

	public List<WEmailAccount> wEmailAccountFinder(String nameFilter, String emailFilter)
			throws WEmailAccountException {
		WEmailAccountDao wEmailAccountDao = new WEmailAccountDao();
		return wEmailAccountDao.wEmailAccountFinder(nameFilter, emailFilter);
	}

    private void _consistencyDataControl(WEmailAccount instance)
			throws WEmailAccountException {

		String nombreTmp = instance.getName();

		if (nombreTmp == null || "".equals(nombreTmp) || " ".equals(nombreTmp)
				|| "".equals(nombreTmp.trim())) {
			throw new WEmailAccountException(
					"The email you are trying to add HAS NOT A VALID NAME.");
		}

	}

	private void _noRedundancyControl(WEmailAccount instance)
			throws WEmailAccountException {

		WEmailAccountDao wEmailAccountDao = new WEmailAccountDao();

		String nombreTmp = instance.getName();

		if ((wEmailAccountDao.getWEmailAccountByName(nombreTmp) != null)) {
			throw new WEmailAccountException(
					"There are already an email account with name: "
							+ nombreTmp);
		}

	}

	private void _noRedundancyUpdateControl(WEmailAccount instance)
			throws WEmailAccountException {

		WEmailAccountDao wEmailAccountDao = new WEmailAccountDao();

		String nombreTmp = instance.getName();

		if (wEmailAccountDao.duplicatedNameVerification(
				instance.getName(), instance.getId())) {
			throw new WEmailAccountException(
					"There are already an email account with name: "
							+ nombreTmp);
		}

	}

}
