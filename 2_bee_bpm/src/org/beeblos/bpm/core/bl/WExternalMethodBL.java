package org.beeblos.bpm.core.bl;


import static com.sp.common.util.ConstantsCommon.DEFAULT_MOD_DATE_TIME;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WExternalMethodDao;
import org.beeblos.bpm.core.error.WExternalMethodException;
import org.beeblos.bpm.core.model.WExternalMethod;
import org.joda.time.DateTime;

import com.sp.common.util.StringPair;

public class WExternalMethodBL {

	private static final Log logger = LogFactory
			.getLog(WExternalMethodBL.class);

	public WExternalMethodBL() {

	}

	public Integer add(WExternalMethod instance, Integer idCurrentUser)
			throws WExternalMethodException {

		logger.debug("WExternalMethodBL:add()");

		_consistencyDataControl(instance);

		//rrl 20141202 timestamp & trace info
		instance.setInsertDate(new DateTime());
		instance.setModDate( DEFAULT_MOD_DATE_TIME );
		instance.setInsertUser(idCurrentUser);
		instance.setModUser(idCurrentUser);
		
		WExternalMethodDao wExternalMethodDao = new WExternalMethodDao();

		return wExternalMethodDao.add(instance);

	}

	public void update(WExternalMethod instance, Integer idCurrentUser)
			throws WExternalMethodException {

		logger.debug("WExternalMethodBL:update()");

		//rrl 20141202 timestamp & trace info
		instance.setModDate(new DateTime());
		instance.setModUser(idCurrentUser);
		
		WExternalMethodDao wExternalMethodDao = new WExternalMethodDao();

		_consistencyDataControl(instance);

		wExternalMethodDao.update(instance);

	}

	public void delete(WExternalMethod instance, Integer idCurrentUser)
			throws WExternalMethodException {

		logger.debug("WExternalMethodBL:delete()");

		WExternalMethodDao wExternalMethodDao = new WExternalMethodDao();
		wExternalMethodDao.delete(instance);

	}

	public List<WExternalMethod> getWExternalMethods()
			throws WExternalMethodException {

		WExternalMethodDao wExternalMethodDao = new WExternalMethodDao();
		return wExternalMethodDao.getWExternalMethods();

	}

	public List<StringPair> getComboList(String firstLineText,
			String blank) throws WExternalMethodException {

		WExternalMethodDao wExternalMethodDao = new WExternalMethodDao();
		return wExternalMethodDao.getComboList( firstLineText, blank);

	}

	public WExternalMethod getExternalMethodByPK(Integer pk)
			throws WExternalMethodException {
		WExternalMethodDao wExternalMethodDao = new WExternalMethodDao();
		return wExternalMethodDao.getExternalMethodByPK(pk);
	}

    private void _consistencyDataControl(WExternalMethod instance)
			throws WExternalMethodException {

		String classnameTmp = instance.getClassname();

		if (classnameTmp == null || "".equals(classnameTmp) || " ".equals(classnameTmp)
				|| "".equals(classnameTmp.trim())) {
			throw new WExternalMethodException(
					"The external method you are trying to add HAS NOT A VALID CLASSNAME.");
		}

	}

}