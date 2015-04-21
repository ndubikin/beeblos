package org.beeblos.bpm.core.bl;

import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.StepTypeDefEmailDConfDao;
import org.beeblos.bpm.core.error.StepTypeDefEmailDConfException;
import org.beeblos.bpm.core.model.bpmn.StepTypeDefEmailDConf;
import org.joda.time.DateTime;

import com.sp.daemon.email.EmailDConf;

public class StepTypeDefEmailDConfBL {

	private static final Log logger = LogFactory
			.getLog(StepTypeDefEmailDConfBL.class);

	public StepTypeDefEmailDConfBL() {

	}

	/**
	 * Adds the new relation between the idStepTypeDef and the email daemon configuration id and returns the
	 * complete relation's object.
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param idStepTypeDef
	 *            the request type id
	 * @param idEmailDConf
	 *            the user profile id
	 * @param idCurrentUser
	 *            the id current user
	 * @return the request type user profile
	 * @throws StepTypeDefEmailDConfException
	 *             the request type user profile exception
	 */
	public StepTypeDefEmailDConf addNewRelation(
			Integer idStepTypeDef, Integer idEmailDConf, Integer idCurrentUser)
			throws StepTypeDefEmailDConfException {

		logger.debug("StepTypeDefEmailDConfBL:addNewRelation() with idStepTypeDef = " + idStepTypeDef
				+ " and idEmailDConf = " + idEmailDConf);

		StepTypeDefEmailDConf instance = new StepTypeDefEmailDConf();
		instance.setStepTypeDefId(idStepTypeDef);
		instance.setEmailDConf(new EmailDConf(idEmailDConf));
		
		Integer idRelation = this.add(instance, idCurrentUser);

		if (idRelation != null){
			return this.getStepTypeDefEmailDConfByPK(idRelation);
		}
		
		return null;
		
	}

	public Integer add(StepTypeDefEmailDConf instance, Integer idCurrentUser)
			throws StepTypeDefEmailDConfException {

		logger.debug("StepTypeDefEmailDConfBL:add()");

		_consistencyDataControl(instance);
		_redundancyDataControl(instance);

		instance.setAddDate(new DateTime());
		instance.setAddUser(idCurrentUser);
		instance.setModDate(DEFAULT_MOD_DATE_TIME);
		instance.setModUser(idCurrentUser);


		StepTypeDefEmailDConfDao rtupDao = new StepTypeDefEmailDConfDao();

		return rtupDao.add(instance);

	}

	/**
	 * Deletes the StepTypeDefEmailDConf.
	 * 
	 * IMPORTANT: This should only be called from the method "deleteStepRelatedRole"
	 * of WStepDefBL because it is the correct way of deleting relations between email daemon configuration
	 * and step type defs
	 * 
	 * @author dmuleiro 20141031
	 *
	 * @param instance
	 *            the instance
	 * @param idCurrentUser
	 *            the id current user
	 * @throws StepTypeDefEmailDConfException
	 *             the request type user profile exception
	 */
	public void delete(StepTypeDefEmailDConf instance, Integer idCurrentUser)
			throws StepTypeDefEmailDConfException {

		logger.debug("StepTypeDefEmailDConfBL:delete()");

		StepTypeDefEmailDConfDao rtupDao = new StepTypeDefEmailDConfDao();
		rtupDao.delete(instance);

	}

	public StepTypeDefEmailDConf getStepTypeDefEmailDConfByPK(Integer id)
			throws StepTypeDefEmailDConfException {

		StepTypeDefEmailDConfDao rtupDao = new StepTypeDefEmailDConfDao();
		return rtupDao.getStepTypeDefEmailDConfByPK(id);

	}

    private void _consistencyDataControl(StepTypeDefEmailDConf instance)
			throws StepTypeDefEmailDConfException {

		Integer idStepTypeDefTmp = instance.getStepTypeDefId();
		Integer idEmailDConfTmp = instance.getEmailDConf().getId();

		if (idStepTypeDefTmp == null || idStepTypeDefTmp.equals(0)) {
			throw new StepTypeDefEmailDConfException(
					"The email daemon configuration you are trying to add HAS NOT A VALID step type def id.");
		}

		if (idEmailDConfTmp == null || idEmailDConfTmp.equals(0)) {
			throw new StepTypeDefEmailDConfException(
					"The email daemon configuration you are trying to add HAS NOT A VALID email d conf id.");
		}

	}

	public boolean existsRelation(StepTypeDefEmailDConf instance)throws StepTypeDefEmailDConfException {

		if (instance == null 
				|| instance.getStepTypeDefId() == null || instance.getStepTypeDefId().equals(0)
				|| instance.getEmailDConf() == null || instance.getEmailDConf().getId() == null || instance.getEmailDConf().getId().equals(0)) {
			throw new StepTypeDefEmailDConfException(
					"The request type user profile is not valid!");
		}

		StepTypeDefEmailDConfDao rtupDao = new StepTypeDefEmailDConfDao();
		return rtupDao.existsRelation(instance.getStepTypeDefId(), instance.getEmailDConf().getId());

	}
	
	private void _redundancyDataControl(StepTypeDefEmailDConf instance)
			throws StepTypeDefEmailDConfException {

		boolean existStepRole = this.existsRelation(instance);

		if (existStepRole) {
			throw new StepTypeDefEmailDConfException(
					"The email daemon configuration you are trying to add ALREADY EXISTS");
		}

	}

}