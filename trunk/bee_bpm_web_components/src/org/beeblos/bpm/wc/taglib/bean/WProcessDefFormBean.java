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
import org.beeblos.bpm.core.model.WRoleDef;
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
	
	private List<SelectItem> lStepCombo = new ArrayList<SelectItem>();
	
	private List<WStepDef> lSteps = new ArrayList<WStepDef>();
	
	private boolean readOnly;
	
	private List<String> selectedWRoleDefList = new ArrayList<String>();
	private List<String> selectedWUserDefList = new ArrayList<String>();
	
	// dml 20120111
	private WProcessRole currentWProcessRole;
	private WProcessUser currentWProcessUser;

	public static WProcessDefFormBean getCurrentInstance() {
		return (WProcessDefFormBean) FacesContext.getCurrentInstance()
				.getExternalContext().getRequestMap().get(MANAGED_BEAN_NAME);
	}

	public WProcessDefFormBean() {
		super();

		init();
	}

	public void init() {
		super.init();

		setShowHeaderMessage(false);
		
		loadStepCombo();
		
		_reset();
		
	}

	// to add a new WProcessDef
	public void initEmptyWProcessDef() {

		currentId = null;
		
		currentWProcessDef = new WProcessDef();
				
		this.setReadOnly(false);
		
		createNullObjectTypeProperties(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>
										  // to avoid access to null objects from view

	}
	
	public void _reset() {

		this.currentId = null;
		this.currentWProcessDef = null;
		
		// dml 20120111
		this.currentWProcessRole = new WProcessRole();
		this.currentWProcessUser = new WProcessUser();
		createNullObjectTypeProperties();
		
		this.readOnly=true;

		attachment = new BeeblosAttachment();

		documentLink = null;

		HelperUtil.recreateBean("documentacionBean",
				"com.softpoint.taglib.common.DocumentacionBean");

	}


	// load an Object in currentWProcessDef
	public void loadCurrentWProcessDef(Integer id) {
		
		this.currentId=id;
		this.loadCurrentWProcessDef();
		
	}
	
	
	public void loadCurrentWProcessDef() {

		WProcessDefBL wpdbl = new WProcessDefBL();

		try {

			currentWProcessDef = 
					wpdbl
						.getWProcessDefByPK( this.currentId, getCurrentUserId() );
			
			if (currentWProcessDef != null) {

				loadLSteps();

			}

			createNullObjectTypeProperties(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>

		} catch (WProcessDefException ex1) {

			logger.error("An error has happened trying to load the WProcessDef: "
					+ this.currentId + " : " + ex1.getMessage() + " -" + ex1.getCause());
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
		
		if (currentWProcessRole != null) {

			if (currentWProcessRole.getProcess() == null) {
				currentWProcessRole.setProcess(new WProcessDef(EMPTY_OBJECT));
			}

			if (currentWProcessRole.getRole() == null) {
				currentWProcessRole.setRole(new WRoleDef(EMPTY_OBJECT));
			}

		}
		
		

	}

	// checks input data before save or update
	private boolean checkInputData() {

		boolean result = false;
		
		if (this.currentWProcessDef != null && this.currentWProcessDef.getName() != null 
				&& !"".equals(this.currentWProcessDef.getName())){
				
			result = true;
			
		}

		return result;
	}

	public String cancel() {

		Integer id= this.currentId;
		_reset();
		this.currentId=id;
		this.loadCurrentWProcessDef();
		this.setReadOnly(true);
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

			this.currentId = wpdBL.add(currentWProcessDef,
					this.getCurrentUserId());
			
			loadCurrentWProcessDef(); // reload object from db

			// Manual process to store attachment in the repository ( if
			// attachment exists ...
			if (attachment.getDocumentoNombre() != null
					&& !"".equals(attachment.getDocumentoNombre())) {
				// storeInRepository();
				update();
			}

			createNullObjectTypeProperties();
			
			this.setReadOnly(true);
			
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
			
			loadCurrentWProcessDef(); // reload object from db

			createNullObjectTypeProperties();

			setShowHeaderMessage(true);
			ret = SUCCESS_FORM_WPROCESSDEF;
			this.setReadOnly(true);

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
	
	private void loadStepCombo() {
		
		try {
			
			setStepCombo(UtilsVs.castStringPairToSelectitem(new WStepDefBL().getComboList("Select...", null)));
			
		} catch (WStepDefException e) {
			e.printStackTrace();
		}
	}

	// dml 20120105
	private void loadLSteps() {
		
		try {
			
			if (this.currentId != null 
					&& this.currentId != 0){
			
				setlSteps(new WStepDefBL().getWStepDefs(this.currentId, 1));
			
			}
		} catch (WStepDefException e) {
			e.printStackTrace();
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

	public List<SelectItem> getStepCombo() {
		return lStepCombo;
	}

	public void setStepCombo(List<SelectItem> stepCombo) {
		this.lStepCombo = stepCombo;
	}

	public List<WStepDef> getlSteps() {
		return lSteps;
	}

	public void setlSteps(List<WStepDef> lSteps) {
		this.lSteps = lSteps;
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

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public void changeReadOnlyToFalse() {
		this.readOnly=false;
	}

	public List<String> getSelectedWRoleDefList() {
		return selectedWRoleDefList;
	}

	public void setSelectedWRoleDefList(List<String> selectedWRoleDefList) {
		this.selectedWRoleDefList = selectedWRoleDefList;
	}

	public List<String> getSelectedWUserDefList() {
		return selectedWUserDefList;
	}

	public void setSelectedWUserDefList(List<String> selectedWUserDefList) {
		this.selectedWUserDefList = selectedWUserDefList;
	}

	public String getStrRoleList() {
		
//		System.out.println(this.currentWStepDef.getRolesRelated().toString());
		
		String strRoleList="";
		for ( WProcessRole pr: this.currentWProcessDef.getRolesRelated()) {
			strRoleList+=(strRoleList!=null && !"".equals(strRoleList)?",":"")+pr.getRole().getId();
		}
		
		System.out.println("--------------->>>>>>>>> strRoleList ------------>>>>>>>>"+strRoleList);
		return strRoleList;
	}

	public WProcessRole getCurrentWProcessRole() {
		return currentWProcessRole;
	}

	public void setCurrentWProcessRole(WProcessRole currentWProcessRole) {
		this.currentWProcessRole = currentWProcessRole;
	}	
	
	public WProcessUser getCurrentWProcessUser() {
		return currentWProcessUser;
	}

	public void setCurrentWProcessUser(WProcessUser currentWProcessUser) {
		this.currentWProcessUser = currentWProcessUser;
	}

	// dml 20120111
	public void deleteWProcessRole(){
		
		WProcessDefBL wpdBL = new WProcessDefBL();
		
		if (currentWProcessDef != null && currentWProcessDef.getRolesRelated() != null
				&& !currentWProcessDef.getRolesRelated().isEmpty()){
			
			for (WProcessRole wpr : currentWProcessDef.getRolesRelated()){
				
				if (wpr.getRole() != null && wpr.getRole().getId() != null 
						&& wpr.getRole().getId().equals(currentWProcessRole.getRole().getId())){
					
					currentWProcessDef.getRolesRelated().remove(wpr);
					break;
					
				}
					
			}
			
			try {
				
				wpdBL.update(currentWProcessDef, getCurrentUserId());
				
			} catch (WProcessDefException ex1) {

				String message = ex1.getMessage() + " - " + ex1.getCause();
				String params[] = { message + ",", ".Error deleting WProcessRole ..." };
				agregarMensaje("203", message, params, FGPException.ERROR);

			}
			
		}
		
	}
	
	// dml 20120111
	public void changeAdminPrivilegesWProcessRole(){
		
		WProcessDefBL wpdBL = new WProcessDefBL();
		
		if (currentWProcessDef != null && currentWProcessDef.getRolesRelated() != null
				&& !currentWProcessDef.getRolesRelated().isEmpty()){
			
			for (WProcessRole wpr : currentWProcessDef.getRolesRelated()){
				
				if (wpr.getRole() != null && wpr.getRole().getId() != null 
						&& wpr.getRole().getId().equals(currentWProcessRole.getRole().getId())){
					
					if (wpr.isAdmin()){
						wpr.setAdmin(false);
					}else {
						wpr.setAdmin(true);
					}					
					break;
					
				}
				
			}
			
			try {
				
				wpdBL.update(currentWProcessDef, getCurrentUserId());
				
			} catch (WProcessDefException ex1) {

				String message = ex1.getMessage() + " - " + ex1.getCause();
				String params[] = { message + ",", ".Error deleting admin privileges ..." };
				agregarMensaje("203", message, params, FGPException.ERROR);

			}
			
		}
		
	}
	
//	public void setNewBeginStep() {
//		if ( this.currentWProcessDef!=null ) {
//			if ( this.currentWProcessDef.getBeginStep()!=null ) {
//				Integer id=this.currentWProcessDef.getBeginStep().getId();
//				this.currentWProcessDef.setBeginStep(new WStepDef(true));
//				this.currentWProcessDef.getBeginStep().setId(id);
//			}
//		}
//	}
}
