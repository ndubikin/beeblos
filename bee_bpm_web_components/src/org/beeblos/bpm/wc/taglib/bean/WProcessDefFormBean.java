package org.beeblos.bpm.wc.taglib.bean;

import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;
import static org.beeblos.bpm.core.util.Constants.FAIL;
import static org.beeblos.bpm.core.util.Constants.SUCCESS_FORM_WPROCESSDEF;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessRole;
import org.beeblos.bpm.core.model.WProcessUser;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.noper.BeeblosAttachment;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.HelperUtil;
import org.beeblos.bpm.wc.taglib.util.UtilsVs;


public class WProcessDefFormBean extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	private static final Log logger = 
			LogFactory
				.getLog(WProcessDefFormBean.class.getName());

	private static final String MANAGED_BEAN_NAME = "wProcessDefFormBean";

	private Integer currentUserId;

	private WProcessDef currentWProcessDef;

	private Integer currentId; // current object managed by this bb

	private TimeZone timeZone;

	private BeeblosAttachment attachment;
	private String documentLink;
	
	private List<SelectItem> stepList = new ArrayList<SelectItem>();
	
	public static ComplexObjectManagementBean getCurrentInstance() {
		return (ComplexObjectManagementBean) FacesContext.getCurrentInstance()
				.getExternalContext().getRequestMap().get(MANAGED_BEAN_NAME);
	}

	public WProcessDefFormBean() {
		super();

		init();
	}

	public void init() {
		super.init();

		setShowHeaderMessage(false);
		
		loadStepList();

		_reset();
	}

	public void _reset() {

		this.currentId = null;
		this.currentWProcessDef = null;

		attachment = new BeeblosAttachment();

		documentLink = null;

		HelperUtil.recreateBean("documentacionBean",
				"com.softpoint.taglib.common.DocumentacionBean");

	}

	private void loadStepList() {
		
		try {
			
			setStepList(UtilsVs.castStringPairToSelectitem(new WStepDefBL().getComboList("Select...", null)));
			
		} catch (WStepDefException e) {
			e.printStackTrace();
		}
	}

	// load an Object in currentWProcessDef
	public void loadCurrentWProcessDef(Integer currentWProcessDefId) {

		WProcessDefBL wpdbl = new WProcessDefBL();

		try {

			currentWProcessDef = wpdbl.getWProcessDefByPK(currentWProcessDefId,
					getCurrentUserId());
			
			if (currentWProcessDef != null) {

				// xxxxx

			}

			createNullObjectTypeProperties(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>

		} catch (WProcessDefException ex1) {

			logger.error("An error has happened trying to load the WProcessDef: "
					+ currentWProcessDefId + " : " + ex1.getMessage());
		}

	}
	
	private void setModel() {

		if (currentWProcessDef != null) {
			if (currentWProcessDef.getBeginStep() != null
					&& currentWProcessDef.getBeginStep().empty()) {
				currentWProcessDef.setBeginStep(null);
			}

		}
	}

	private void createNullObjectTypeProperties() {

		if (currentWProcessDef != null) {

			if (currentWProcessDef.getBeginStep() == null) {
				currentWProcessDef.setBeginStep(new WStepDef(EMPTY_OBJECT));
			}

		}

	}

	// checks input data before save or update
	private boolean checkInputData() {

		boolean result = false;

		return result;
	}

	public String cancel() {

		_reset();
		return null;
	}

	public String save_continue() {

		String result = FAIL;

		if (checkInputData()) {

			try {

				result = add();
				createNullObjectTypeProperties(); // <<<<<<<<<<<<<<<<<<<<IMPORTANT>>>>>>>>>>>>>>>>>>

			} catch (WProcessDefException e) {
				createNullObjectTypeProperties();
				result = FAIL;
			}
		}

		return result;
	}

	public String save() {

		String result = null;

		if (checkInputData()) {

			try {

				result = add();
				_reset();

			} catch (WProcessDefException e) {
				createNullObjectTypeProperties(); // <<<<<<<<<<<<<<<<<<<<IMPORTANT>>>>>>>>>>>>>>>>>>
				result = null;
			}
		}

		return result;
	}

	public String add() throws WProcessDefException {

		logger.debug("WProcessDefFormBean: add: currentWProcessDefId:["
				+ this.currentId + "] ");

		setShowHeaderMessage(false);

		String ret = null;

		// if object already exists in db then update and return
		if (currentWProcessDef != null && currentWProcessDef.getId() != null
				&& currentWProcessDef.getId() != 0) {

			// before update store document in repository ( if exists )
			if (attachment.getDocumentoNombre() != null
					&& !"".equals(attachment.getDocumentoNombre())) {
				// storeInRepository();
			}

			return update();
		}

		WProcessDefBL wpdBL = new WProcessDefBL();

		try {

			setModel(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>

			currentId = wpdBL.add(currentWProcessDef,
					this.getCurrentUserId());

			// Manual process to store attachment in the repository ( if
			// attachment exists ...
			if (attachment.getDocumentoNombre() != null
					&& !"".equals(attachment.getDocumentoNombre())) {
				// storeInRepository();
				update();
			}

			createNullObjectTypeProperties();

			ret = SUCCESS_FORM_WPROCESSDEF;
			setShowHeaderMessage(true);

		} catch (WProcessDefException ex1) {

			String message = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { message + ",", ".Please confirm input values." };
			agregarMensaje("203", message, params, FGPException.ERROR);

			throw new WProcessDefException(message);

		} catch (Exception e) {

			String message = e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", ".Error inserting object ..." };
			agregarMensaje("203", message, params, FGPException.ERROR);

			throw new WProcessDefException(message);

		}

		return ret;
	}

	public String update() {
		logger.debug("update(): currentId:" + currentWProcessDef.getId()
				+ " - " + currentWProcessDef.getName());

		String ret = null;

		WProcessDefBL wpdBL = new WProcessDefBL();

		try {

			setModel();

			wpdBL.update(currentWProcessDef, this.getCurrentUserId());

			createNullObjectTypeProperties();

			setShowHeaderMessage(true);
			ret = SUCCESS_FORM_WPROCESSDEF;

		} catch (WProcessDefException ex1) {

			String message = "Error updating object: "
					+ currentWProcessDef.getId() + " - "
					+ currentWProcessDef.getName() + "\n" + ex1.getMessage()
					+ "\n" + ex1.getCause();

			logger.error(message);

			String params[] = { message + ",", ".Please confirm input values." };
			agregarMensaje("203", message, params, FGPException.ERROR);

			logger.error(message);

		}

		return ret;
	}

	private void loadCurrentWProcessDef() {
		logger.debug("loadCurrentWProcessDef()");

		WProcessDefBL wpdBL = new WProcessDefBL();

		try {
			WProcessDef wpd = wpdBL.getWProcessDefByPK(
					currentWProcessDef.getId(), this.getCurrentUserId());

			if (wpd != null) {

				currentId = wpd.getId();
				// etc etc
			}

		} catch (WProcessDefException ex1) {
			logger.error("Error retrieving object: "
					+ currentWProcessDef.getId() + " : " + ex1.getMessage()
					+ " - " + ex1.getCause());
		}
	}

	public WProcessDef getCurrentWProcessDef() {
		return currentWProcessDef;
	}

	public void setCurrentWProcessDef(WProcessDef currentWProcessDef) {
		this.currentWProcessDef = currentWProcessDef;
	}

	public Integer getCurrentId() {
		return currentId;
	}

	public void setCurrentId(Integer currentId) {
		this.currentId = currentId;
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

	public List<SelectItem> getStepList() {
		return stepList;
	}

	public void setStepList(List<SelectItem> stepList) {
		this.stepList = stepList;
	}

	// dml 20120105
	public List<WProcessRole> getRolesRelatedList(){
		
		List<WProcessRole> rrl= new ArrayList<WProcessRole>();
		
		if (currentWProcessDef != null && currentWProcessDef.getRolesRelated() != null
				&& currentWProcessDef.getRolesRelated().size() != 0){
			
			rrl= new ArrayList<WProcessRole>(currentWProcessDef.getRolesRelated());
			
		}
		
		return rrl;
		
	}

	// dml 20120105
	public List<WProcessUser> getUsersRelatedList(){
		
		List<WProcessUser> url = new ArrayList<WProcessUser>();
		
		if (currentWProcessDef != null && currentWProcessDef.getUsersRelated() != null
				&& currentWProcessDef.getUsersRelated().size() != 0){
			
			url= new ArrayList<WProcessUser>(currentWProcessDef.getUsersRelated());

		}
		
		return url;
		
	}

}
