package org.beeblos.bpm.wc.taglib.bean;

import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;
import static com.sp.common.util.ConstantsCommon.OK_MESSAGE;
import static org.beeblos.bpm.core.util.Constants.FAIL;
import static org.beeblos.bpm.core.util.Constants.SUCCESS_FORM_WPROCESSDEF;
import static org.beeblos.bpm.core.util.Constants.WPROCESSDEF_QUERY;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WProcessHeadBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessHeadException;
import org.beeblos.bpm.core.model.WProcessHead;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.WProcessDefUtil;

public class WProcessHeadFormBean extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory
			.getLog(WProcessHeadFormBean.class.getName());

	private static final String MANAGED_BEAN_NAME = "wProcessHeadFormBean";

	public static WProcessHeadFormBean getCurrentInstance() {
		return (WProcessHeadFormBean) FacesContext.getCurrentInstance()
				.getExternalContext().getRequestMap().get(MANAGED_BEAN_NAME);
	}

	private Integer currentUserId;
	private TimeZone timeZone;

	// main properties:

	private WProcessHead currentWProcessHead;
	private Integer currentId; // current object (it is used to load a processDef from the dataTable too)

	private boolean readOnly;

	// dml 20120305
	private String returnStatement;
		
	private String activeFilter;

	// dml 20130508 - Esta lista "relatedProcessDefList" es la lista de WProcessHead relacionados con un WProcessHead. 
	// O sea, este bean además de para la ficha de WProcessHead también está para los WProcessHead (o por lo menos lo 
	// reutilizamos en su momento para ello para no crear otro nuevo) y en la ficha de ese WProcessHead tenemos una 
	// grilla donde tenemos todos los WProcessHead de ese head y es en esa lista del bean donde los tenemos todos
	// TENEMOS QUE CREAR UN BEAN PROPIO PARA LA GESTIÓN DE LOS WPROCESSHEAD
	private List<WProcessDefLight> relatedProcessDefList;

	public WProcessHeadFormBean() {
		super();
		_reset();
		init();
	}

	public void init() {
		super.init_core();
		setShowHeaderMessage(false);

	}

	// to add a new WProcessHead
	public void initEmptyWProcessHead() {

		currentId = null;
		currentWProcessHead = new WProcessHead();
		this.setReadOnly(false);
		recoverNullObjects(); // <<<<<<<<< IMPORTANT >>>>>>>>>>>
							  // to avoid access to null objects from view
	}

	public void _reset() {

		this.currentId = null;
		this.currentWProcessHead = null;
		this.readOnly = true;

		recoverNullObjects();
		
	}

	// load an Object in currentWProcessHead
	public void loadCurrentWProcessHead(Integer id) {

		this.currentId = id;
		this.loadCurrentWProcessHead();

	}

	public void loadCurrentWProcessHead() {

		WProcessHeadBL wpdbl = new WProcessHeadBL();

		try {

			currentWProcessHead = wpdbl.getProcessHeadByPK(this.currentId,
					getCurrentUserId());

			recoverNullObjects(); // <<<<<<<< IMPORTANT >>>>>>>>>>>

			this.reloadRelatedProcessDefList(); // dml 20130508
			
		} catch (WProcessHeadException e) {

			String message = "WProcessHeadFormBean.loadCurrentWProcessHead() WProcessHeadException: " + 
					e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} 

	}
	
	// dml 20130508
	public void reloadRelatedProcessDefList(){
		
		try {
			
 			this.setRelatedProcessDefList(new WProcessDefBL()
				.finderWProcessDefLight(false, null, null, null, false, null, "", 
						this.currentWProcessHead.getId(), this.activeFilter, getCurrentUserId()));
			
		} catch (WProcessDefException e) {

			this.setRelatedProcessDefList(new ArrayList<WProcessDefLight>());
			
			String message = "WProcessHeadFormBean.reloadRelatedProcessDefList() WProcessHeadException: " + 
					e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
	}

	
	private void setModel() {

	}

	private void recoverNullObjects() {


	}

	// checks input data before save or update
	private boolean checkInputData() {

		boolean result = false;

		if (this.currentWProcessHead != null
				&& this.currentWProcessHead.getName() != null
				&& !"".equals(this.currentWProcessHead.getName()) ) {

			result = true;

		}

		return result;

	}
	
	public String returnTo(){
		
		if (returnStatement != null){
			_reset();
			return returnStatement;
		}
		
		return null;
		
	}

	public String cancel() {

		Integer id = this.currentId;
		_reset();
		this.currentId = id;
		this.loadCurrentWProcessHead();
		this.setReadOnly(true);
		
		return null;
		
	}

	public String save_continue() {

		String result = FAIL;

		if (checkInputData()) {

			try {

				result = add();
				recoverNullObjects(); // <<<<<<<<<<<<<<<<<<<<IMPORTANT>>>>>>>>>>>>>>>>>>

			} catch (WProcessHeadException e) {
				recoverNullObjects();
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
				result = _defineReturn();
				_reset();

			} catch (WProcessHeadException e) {
				recoverNullObjects(); // <<<<<<<<<<<<<<<<<<<<IMPORTANT>>>>>>>>>>>>>>>>>>
				result = null;
			}
		}

		return result;
	}

	// nes 20120203
	private String _defineReturn() {

		String ret = returnStatement;
		returnStatement="";
		
		return ret;
		
	}

	public String add() throws WProcessHeadException {

		logger.debug("WProcessHeadFormBean: add: currentProcessDefId:["
				+ this.currentId + "] ");

		setShowHeaderMessage(false);
		String message = "";
		
		String ret = null;

		// if object already exists in db then update and return
		if (currentWProcessHead != null && currentWProcessHead.getId() != null
				&& currentWProcessHead.getId() != 0) {

			ret = update();

			message = "The process head '" + this.currentWProcessHead.getName()+ "' has been correctly updated";

			return ret;

		}

		WProcessHeadBL wpdBL = new WProcessHeadBL();

		try {

			setModel(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>

			this.currentId = wpdBL.add(currentWProcessHead,
					this.getCurrentUserId());

			message = "The process head '" + this.currentWProcessHead.getName()+ "' has been correctly added";

			loadCurrentWProcessHead(); // reload object from db

			recoverNullObjects();

			this.setReadOnly(true);

			ret = SUCCESS_FORM_WPROCESSDEF;

			super.createWindowMessage(OK_MESSAGE, message, null);

		} catch (WProcessHeadException e) {

			message = "WProcessHeadFormBean.add() WProcessHeadException: " + 
					e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);

			throw new WProcessHeadException(message);

		} catch (Exception e) {

			message = "WProcessHeadFormBean.add() Exception: " + 
					e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);

			throw new WProcessHeadException(message);

		}

		return ret;
	}

	public String update() {
		logger.debug("update(): currentId:" + currentWProcessHead.getId()
				+ " - " + currentWProcessHead.getName());

		String ret = null;

		WProcessHeadBL wpdBL = new WProcessHeadBL();

		try {
			
			setModel();

			wpdBL.update(currentWProcessHead, this.getCurrentUserId());

			loadCurrentWProcessHead(); // reload object from db

			recoverNullObjects();

			setShowHeaderMessage(true);
			ret = SUCCESS_FORM_WPROCESSDEF;
			this.setReadOnly(true);

		} catch (WProcessHeadException e) {

			String message = "Error updating object: "
					+ currentWProcessHead.getId() + " - "
					+ currentWProcessHead.getName() + "\n" + e.getMessage()
					+ "\n" + e.getCause();

			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

		return ret;
	}
	
	// dml 20130909
	public String loadWProcessDefForm() {

		return new WProcessDefUtil().loadWProcessDefFormBean(this.currentId);

	}

		
	public WProcessHead getCurrentWProcessHead() {
		return currentWProcessHead;
	}

	public void setCurrentWProcessHead(WProcessHead currentWProcessHead) {
		this.currentWProcessHead = currentWProcessHead;
	}

	public Integer getCurrentId() {
		return currentId;
	}

	public void setCurrentId(Integer currentId) {
		this.currentId = currentId;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public String getActiveFilter() {
		return activeFilter;
	}

	public void setActiveFilter(String activeFilter) {
		this.activeFilter = activeFilter;
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

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public List<WProcessDefLight> getRelatedProcessDefList() {
		return relatedProcessDefList;
	}

	public void setRelatedProcessDefList(List<WProcessDefLight> relatedProcessDefList) {
		this.relatedProcessDefList = relatedProcessDefList;
	}

	public void changeReadOnlyToFalse() {
		this.readOnly = false;
	}

	public String getReturnStatement() {
		return returnStatement;
	}

	public void setReturnStatement(String returnStatement) {
		this.returnStatement = returnStatement;
	}

}
