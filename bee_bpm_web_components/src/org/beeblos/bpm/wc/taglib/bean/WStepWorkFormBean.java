package org.beeblos.bpm.wc.taglib.bean;

import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;
import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;

import java.util.TimeZone;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WStepWorkBL;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WTimeUnit;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.noper.BeeblosAttachment;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.HelperUtil;

public class WStepWorkFormBean extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory
			.getLog(WStepWorkFormBean.class);

	private static final String MANAGED_BEAN_NAME = "wStepWorkFormBean";

	private Integer currentUserId;

	private WStepWork currentWStepWork;

	private Integer currObjId; // current object managed by this bb

	private TimeZone timeZone;

	private BeeblosAttachment attachment;
	private String documentLink;

	public static WStepWorkFormBean getCurrentInstance() {
		return (WStepWorkFormBean) FacesContext.getCurrentInstance()
				.getExternalContext().getRequestMap().get(MANAGED_BEAN_NAME);
	}

	public WStepWorkFormBean() {
		super();

		init();
	}

	public void init() {
		super.init_core();

		setShowHeaderMessage(false);

		_reset();
	}

	public void _reset() {

		this.currObjId = null;
		this.currentWStepWork = null;

		attachment = new BeeblosAttachment();

		documentLink = null;

		HelperUtil.recreateBean("documentacionBean",
				"com.softpoint.taglib.common.DocumentacionBean");

	}

	// SET EMTPY OBJECTS OF CURRENT OBJECT TO NULL TO AVOID PROBLEMS
	// WITH HIBERNATE RELATIONS AND CASCADES
	private void setModel() {

		if (currentWStepWork != null) {

			if (currentWStepWork.getCurrentStep() != null
					|| currentWStepWork.getCurrentStep().empty()) {
				currentWStepWork.setCurrentStep(null);
			}

			if (currentWStepWork.getInsertUser() != null
					|| currentWStepWork.getInsertUser().empty()) {
				currentWStepWork.setInsertUser(null);
			}

			if (currentWStepWork.getLockedBy() != null
					|| currentWStepWork.getLockedBy().empty()) {
				currentWStepWork.setLockedBy(null);
			}

			if (currentWStepWork.getPreviousStep() != null
					|| currentWStepWork.getPreviousStep().empty()) {
				currentWStepWork.setPreviousStep(null);
			}

			// nes 20130830 quité el process de aqui porque ya hay uno adentro del wProcessWork
//			if (currentWStepWork.getwProcessWork() != null
//					|| currentWStepWork.getProcess().empty()) {
//				currentWStepWork.setProcess(null);
//			}

			if (currentWStepWork.getwProcessWork() != null
					|| currentWStepWork.getwProcessWork().empty()) {
				currentWStepWork.setwProcessWork(null);
			}

			if (currentWStepWork.getTimeUnit() != null
					|| currentWStepWork.getTimeUnit().empty()) {
				currentWStepWork.setTimeUnit(null);
			}

			if (currentWStepWork.getReminderTimeUnit() != null
					|| currentWStepWork.getReminderTimeUnit().empty()) {
				currentWStepWork.setReminderTimeUnit(null);
			}

			if (currentWStepWork.getOpenerUser() != null
					|| currentWStepWork.getOpenerUser().empty()) {
				currentWStepWork.setOpenerUser(null);
			}

			if (currentWStepWork.getPerformer() != null
					|| currentWStepWork.getPerformer().empty()) {
				currentWStepWork.setPerformer(null);
			}

		}
	}

	// CREATE NULL PROPERTIES OF OBJECT TYPE TO AVOID PROBLEMS
	// WITH VIEW AND ITS REFERENES TO THESE OBJECTS ...
	private void recoverNullObjects() {

		if (currentWStepWork != null) {

			if (currentWStepWork.getCurrentStep() == null) {
				currentWStepWork.setCurrentStep(new WStepDef(EMPTY_OBJECT));
			}

			if (currentWStepWork.getInsertUser() == null) {
				currentWStepWork.setInsertUser(new WUserDef(EMPTY_OBJECT));
			}

			if (currentWStepWork.getLockedBy() == null) {
				currentWStepWork.setLockedBy(new WUserDef(EMPTY_OBJECT));
			}

			if (currentWStepWork.getPreviousStep() == null) {
				currentWStepWork.setPreviousStep(new WStepDef(EMPTY_OBJECT));
			}

			// nes 20130830 quité el process de aqui porque ya hay uno adentro del wProcessWork
//			if (currentWStepWork.getProcess() == null) {
//				currentWStepWork.setProcess(new WProcessDef(EMPTY_OBJECT));
//			}

			if (currentWStepWork.getwProcessWork() == null) {
				currentWStepWork
						.setwProcessWork(new WProcessWork(EMPTY_OBJECT));
			}

			if (currentWStepWork.getTimeUnit() == null) {
				currentWStepWork.setTimeUnit(new WTimeUnit());
			}

			if (currentWStepWork.getReminderTimeUnit() == null) {
				currentWStepWork.setReminderTimeUnit(new WTimeUnit());
			}

			if (currentWStepWork.getOpenerUser() == null) {
				currentWStepWork.setOpenerUser(new WUserDef(EMPTY_OBJECT));
			}

			if (currentWStepWork.getPerformer() == null) {
				currentWStepWork.setPerformer(new WUserDef(EMPTY_OBJECT));
			}

		}

	}

	// checks input data before save or update
	private boolean checkInputData() {

		boolean result = false;

		return result;
	}

	private void persistCurrentObject() throws WStepWorkException {
		WStepWorkBL wswBL = new WStepWorkBL();
		this.setModel();
		wswBL.update(currentWStepWork, getCurrentUserId());
		this.recoverNullObjects();
	}

	public String cancel() {

		_reset();
		return null;
	}

	public String save_continue() {

		String result = null;

		// if ( checkInputData() ) {
		//
		// try {
		//
		// result = add();
		// createNullObjectTypeProperties(); //<<<<<<<<<<<<<<<<<<<< IMPORTANT
		// >>>>>>>>>>>>>>>>>>
		//
		// } catch (ObjectException e) {
		// createNullObjectTypeProperties();
		// result = null;
		// }
		// }

		return result;
	}

	public String save() {

		String result = null;

		if (checkInputData()) {

			try {

				result = add();
				_reset();

			} catch (WStepWorkException e) {
				recoverNullObjects(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT
										// >>>>>>>>>>>>>>>>>>
				result = null;
			}
		}

		return result;
	}

	public String add() throws WStepWorkException {
		logger.debug("ComplexObjectManagementBean: add: objectId:["
				+ this.currObjId + "] ");

		setShowHeaderMessage(false);

		String ret = null;

		// if object already exists in db then update and return
		if (currentWStepWork != null
		// && currentObject.getId()!=null && currentObject.getId()!=0
		) {

			// before update store document in repository ( if exists )
			if (attachment.getDocumentoNombre() != null
					&& !"".equals(attachment.getDocumentoNombre())) {
				// storeInRepository();
			}

			return update();
		}

		WStepWorkBL wswBL = new WStepWorkBL();

		try {

			setModel(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>

			currObjId = wswBL.add(currentWStepWork, this.getCurrentUserId());

			// Manual process to store attachment in the repository ( if
			// attachment exists ...
			if (attachment.getDocumentoNombre() != null
					&& !"".equals(attachment.getDocumentoNombre())) {
				// storeInRepository();
				update();
			}

			recoverNullObjects();

			ret = "OK";
			setShowHeaderMessage(true);

		} catch (WStepWorkException e) {
			String message = "WStepWorkFormBean.add() WStepWorkException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);

			throw new WStepWorkException(message);

		} catch (Exception e) {
			String message = "WStepWorkFormBean.add() Exception: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);

			throw new WStepWorkException(message);

		}

		return ret;
	}

	public String update() {
		logger.debug("update(): currentId:" + currentWStepWork.getId());

		String ret = null;

		WStepWorkBL objBL = new WStepWorkBL();

		try {

			setModel();

			objBL.update(currentWStepWork, this.getCurrentUserId());

			recoverNullObjects();

			setShowHeaderMessage(true);
			ret = "OK";

		} catch (WStepWorkException e) {

			String message = "WStepWorkFormBean.update() WStepWorkException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);

		}

		return ret;
	}

	// load an WStepWork in currentWStepWork
	public void loadWStepWork(Integer objectId) {

		this.currObjId = objectId;
		this.loadWStepWork();

	}

	public void loadWStepWork() {
		logger.debug("loadWStepWork()");

		WStepWorkBL wswBL = new WStepWorkBL();

		try {
			currentWStepWork = wswBL.getWStepWorkByPK(this.currObjId,
					this.getCurrentUserId());

			if (currentWStepWork != null) {

				currObjId = currentWStepWork.getId();
				// etc etc
			}

			recoverNullObjects();

		} catch (WStepWorkException e) {
			logger.error("Error retrieving object: " + currentWStepWork.getId()
					+ " : " + e.getMessage() + " - " + e.getCause());
		}
	}

	public WStepWork getCurrentWStepWork() {
		return currentWStepWork;
	}

	public void setCurrentWStepWork(WStepWork currentWStepWork) {
		this.currentWStepWork = currentWStepWork;
	}

	public Integer getCurrObjId() {
		return currObjId;
	}

	public void setCurrObjId(Integer currObjId) {
		this.currObjId = currObjId;
	}

	public BeeblosAttachment getAttachment() {
		return attachment;
	}

	public void setAttachment(BeeblosAttachment attachment) {
		this.attachment = attachment;
	}

	public String getDocumentLink() {
		return documentLink;
	}

	public void setDocumentLink(String documentLink) {
		this.documentLink = documentLink;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public Integer getCurrentUserId() {
		if (currentUserId == null) {
			ContextoSeguridad cs = (ContextoSeguridad) getSession()
					.getAttribute(SECURITY_CONTEXT);
			if (cs != null)
				currentUserId = cs.getIdUsuario();
		}
		return currentUserId;
	}

	public TimeZone getTimeZone() {
		// Si se pone GMT+1 pone mal el dia
		return java.util.TimeZone.getDefault();
	}

}
