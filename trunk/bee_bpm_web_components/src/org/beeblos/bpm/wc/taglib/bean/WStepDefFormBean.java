package org.beeblos.bpm.wc.taglib.bean;

import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;
import static org.beeblos.bpm.core.util.Constants.FAIL;
import static org.beeblos.bpm.core.util.Constants.SUCCESS_FORM_WSTEPDEF;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepResponseDefBL;
import org.beeblos.bpm.core.bl.WTimeUnitBL;
import org.beeblos.bpm.core.error.WRoleDefException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepResponseDefException;
import org.beeblos.bpm.core.error.WTimeUnitException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WStepUser;
import org.beeblos.bpm.core.model.WTimeUnit;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.noper.BeeblosAttachment;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.ListUtil;
import org.beeblos.bpm.wc.taglib.util.UtilsVs;

public class WStepDefFormBean extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(WStepDefFormBean.class);
	
	private static final String MANAGED_BEAN_NAME = "wStepDefFormBean";

    private Integer currentUserId;
    
	private WStepDef currentWStepDef; 
	
	private Integer currObjId; // current object managed by this bb

	private TimeZone timeZone;

	private BeeblosAttachment attachment;
	private String documentLink;
	
	// auxiliar fields
	private List<SelectItem> lWTimeUnit;
	
	// dml 20120111
	private WStepResponseDef stepResponse;

	// dml 20120112
	private boolean readOnly;
	
	// dml 20120113
	private WStepRole currentWStepRole;
	private WStepUser currentWStepUser;	
	
	// rrl 20120113
	private String strRoleList;
	private String strUserList;

	public static WStepDefFormBean getCurrentInstance() {
        return (WStepDefFormBean) FacesContext.getCurrentInstance().getExternalContext()
            .getRequestMap().get(MANAGED_BEAN_NAME);
    }

	public WStepDefFormBean() {		
		super();
		
		init();  
	}
    
	public void init(){
		super.init();
		
		setShowHeaderMessage(false);  
		
		lWTimeUnit=loadWTimeUnitForCombo();
		
		_reset();
	}

	// to add a new WStepDef
	public void initEmptyWStepDef() {

		currObjId = null;
		
		currentWStepDef = new WStepDef(EMPTY_OBJECT);
		
		recoverNullObjects();
		
		this.setReadOnly(false);
				
	}

	public void _reset() {
	
		this.currObjId=new Integer(0);
		this.currentWStepDef=null;
		
		// dml 20110111
		this.stepResponse = new WStepResponseDef();
		
		// dml 20120113
		this.currentWStepRole = new WStepRole();
		this.currentWStepUser = new WStepUser();

		attachment = new BeeblosAttachment();

		documentLink=null; 
		
		this.setReadOnly(true);

		//		HelperUtil.recreateBean("documentacionBean", "com.softpoint.taglib.common.DocumentacionBean");

	
	}
	
	
	// SET EMTPY OBJECTS OF CURRENT OBJECT TO NULL TO AVOID PROBLEMS
	// WITH HIBERNATE RELATIONS AND CASCADES
	private void setModel() {
		
		if(currentWStepDef != null){
			
			if ( currentWStepDef.getResponse()!=null && 
					currentWStepDef.getResponse().size()==0 ) {
				currentWStepDef.setResponse( null );
			}
			
			if ( currentWStepDef.getTimeUnit()!=null && 
					currentWStepDef.getTimeUnit().empty() ) {
				currentWStepDef.setTimeUnit( null );
			}

			if ( currentWStepDef.getReminderTimeUnit()!=null && 
					currentWStepDef.getReminderTimeUnit().empty() ) {
				currentWStepDef.setReminderTimeUnit( null );
			}

			if ( currentWStepDef.getRolesRelated()!=null && 
					currentWStepDef.getRolesRelated().size()==0 ) {
				currentWStepDef.setRolesRelated( null );
			}			
			
			if ( currentWStepDef.getUsersRelated()!=null && 
					currentWStepDef.getUsersRelated().size()==0 ) {
				currentWStepDef.setUsersRelated( null );
			}			
		}
	}
	
	// CREATE NULL PROPERTIES OF OBJECT TYPE TO AVOID PROBLEMS
	// WITH VIEW AND ITS REFERENES TO THESE OBJECTS ...
	private void recoverNullObjects(){
		
		if(currentWStepDef != null){
			
			if ( currentWStepDef.getTimeUnit() == null ) {
				currentWStepDef.setTimeUnit( new WTimeUnit() );
			}

			if ( currentWStepDef.getReminderTimeUnit() == null ) {
				currentWStepDef.setReminderTimeUnit( new WTimeUnit() );
			}

			if ( currentWStepDef.getRolesRelated() == null ) {
				currentWStepDef.setRolesRelated( new HashSet<WStepRole>() );
			}

			if ( currentWStepDef.getUsersRelated() == null ) {
				currentWStepDef.setUsersRelated( new HashSet<WStepUser>() );
			}

		}
		
		if (currentWStepRole != null) {

			if (currentWStepRole.getStep() == null) {
				currentWStepRole.setStep(new WStepDef(EMPTY_OBJECT));
			}

			if (currentWStepRole.getRole() == null) {
				currentWStepRole.setRole(new WRoleDef(EMPTY_OBJECT));
			}

		}
		
		if (currentWStepUser != null) {

			if (currentWStepUser.getStep() == null) {
				currentWStepUser.setStep(new WStepDef(EMPTY_OBJECT));
			}

			if (currentWStepUser.getUser() == null) {
				currentWStepUser.setUser(new WUserDef(EMPTY_OBJECT));
			}

		}

	}
	
	// checks input data before save or update
	private boolean checkInputData() {
		
		boolean result = false;
		
		if (this.currentWStepDef != null && this.currentWStepDef.getName() != null 
				&& !"".equals(this.currentWStepDef.getName())){
				
			result = true;
			
		} else {
			
			String message = "You must insert correct values";
			String params[] = {message + ",", ".Please confirm input values." };				
			agregarMensaje("205",message,params,FGPException.ERROR);			
			
		}

		return result;
	}
	
	

	public String cancel(){
		
		Integer id= this.currObjId;
		_reset();
		this.currObjId=id;
		this.loadObject();
		this.setReadOnly(true);
		return null;
		
	}

	
    public String save_continue() {

		String result = FAIL;

		if (checkInputData()) {

			try {

				result = add();
				recoverNullObjects(); // <<<<<<<<<<<<<<<<<<<<IMPORTANT>>>>>>>>>>>>>>>>>>

			} catch (WStepDefException e) {
				recoverNullObjects();
				result = FAIL;
			}
		}

		return result;
	}
	
	public String save() {
		
		String result =  null;
		
		if ( checkInputData() ) {
			
			try {
				
				result = add();
				_reset();
				
			} catch (WStepDefException e) {
				recoverNullObjects(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>
				result =  null;
			}
		}

		return result;
	}
	

	
	
	public String add() throws WStepDefException {		
		logger.debug("WStepDefFormBean: add: objectId:["+this.currObjId+"] ");
		
		setShowHeaderMessage(false);

		String ret = null;
		
		// if object already exists in db then update and return
		if(currentWStepDef!=null 
				&& currentWStepDef.getId()!=null && currentWStepDef.getId()!=0){ 

			// before update store document in repository ( if exists )
			if (attachment.getDocumentoNombre()!=null && 
					!"".equals(attachment.getDocumentoNombre())) {
				//storeInRepository(); 
			}
			
			return update();
		}
		
		WStepDefBL wsdBL = new WStepDefBL();
	
		try {	
			
			setModel(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>

			currObjId = wsdBL.add(currentWStepDef, this.getCurrentUserId() );
			
			// Manual process to store attachment in the repository ( if attachment exists ...
			if (attachment.getDocumentoNombre()!=null && !"".equals(attachment.getDocumentoNombre())) {
				//storeInRepository();
				update();
			}
			
			recoverNullObjects();
			
			this.setReadOnly(true);

			ret=SUCCESS_FORM_WSTEPDEF;
			
			setShowHeaderMessage(true);
			
		} catch (WStepDefException ex1) {

			String message = ex1.getMessage() + " - "+ ex1.getCause();
			String params[] = {message + ",", ".Please confirm input values." };				
			agregarMensaje("205",message,params,FGPException.ERROR);	
			
			throw new WStepDefException(message);			
			
		} catch (Exception e) {
			
			String message = e.getMessage() + " - "+ e.getCause();
			String params[] = {message + ",", ".Error inserting object ..." };
			agregarMensaje("205",message,params ,FGPException.ERROR);
			
			throw new WStepDefException(message);			
		}

		return ret;
	}	
	

	
	public String update() {
		logger.debug("update(): currentId:"
				+ currentWStepDef.getId() + " - "
				+ currentWStepDef.getName());
		
		String ret = null;

		WStepDefBL wsdBL = new WStepDefBL();

		try {
						
			setModel();
			
			wsdBL.update(currentWStepDef, this.getCurrentUserId() );
			
			recoverNullObjects();
			
			this.setReadOnly(true);

			ret=SUCCESS_FORM_WSTEPDEF;

			setShowHeaderMessage(true);

		} catch (WStepDefException ex1) {

			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".update() WStepDefException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();

		}
		
		return ret;
	}
	

	// load an Object in currentWStepDef
	public void loadObject(Integer objectId){
		
			this.currObjId=objectId;
			this.loadObject();
			
	}
	
	public void loadObject() {
		logger.debug("loadObject()");
		
		WStepDefBL wsdBL = new WStepDefBL();

		try {
			currentWStepDef = 
					wsdBL
						.getWStepDefByPK(this.currObjId, this.getCurrentUserId() );
			
			recoverNullObjects();
			
		} catch (WStepDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".loadObject() WStepDefException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}			
	}
	
	public void loadResponse(){
		
		if (stepResponse != null && stepResponse.getId() != null
				&& stepResponse.getId() != 0){
			
			try {
				
				stepResponse = new WStepResponseDefBL().getWStepResponseDefByPK(stepResponse.getId(), getCurrentUserId().toString());
			
			} catch (WStepResponseDefException ex1) {

				String mensaje = ex1.getMessage() + " - " + ex1.getCause();
				String params[] = { mensaje + ",",
						".loadResponse() WStepDefException ..." };
				agregarMensaje("205", mensaje, params, FGPException.ERROR);
				ex1.printStackTrace();

			}
		
		}
		
	}

	public WStepDef getCurrentWStepDef() {
		return currentWStepDef;
	}

	public void setCurrentWStepDef(WStepDef currentWStepDef) {
		this.currentWStepDef = currentWStepDef;
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

	
	public List<SelectItem> getlWTimeUnit() {
		return lWTimeUnit;
	}

	public void setlWTimeUnit(List<SelectItem> lWTimeUnit) {
		this.lWTimeUnit = lWTimeUnit;
	}

	public Integer getCurrentUserId() {
		if ( currentUserId== null ) {
			ContextoSeguridad cs = (ContextoSeguridad)
						getSession().getAttribute(SECURITY_CONTEXT);
			if (cs!=null) currentUserId=cs.getIdUsuario();
		}
		return currentUserId;
	}	

	public TimeZone getTimeZone() {
		//Si se pone GMT+1 pone mal el dia 
		return java.util.TimeZone.getDefault();
	}

	public List<WStepRole> getRoleRelatedList() {
		
		List<WStepRole> lsr = new ArrayList<WStepRole>();
		
		if (currentWStepDef != null && currentWStepDef.getRolesRelated() != null
				&& currentWStepDef.getRolesRelated().size() != 0){
			
			lsr= new ArrayList<WStepRole>( currentWStepDef.getRolesRelated() );

		}
		
		return lsr;
	}
	
	public Integer getRoleRelatedListSize() {
		
		return (currentWStepDef != null && currentWStepDef.getRolesRelated() != null ?
				currentWStepDef.getRolesRelated().size():
					0);
	}
	
	public List<WStepUser> getUserRelatedList() {
		
		List<WStepUser> lsu = new ArrayList<WStepUser>();
		
		if (currentWStepDef != null && currentWStepDef.getUsersRelated() != null
				&& currentWStepDef.getUsersRelated().size() != 0){
			
			lsu= new ArrayList<WStepUser>( currentWStepDef.getUsersRelated() );

		}
		
		return lsu;
	}
	
	public Integer getUserRelatedListSize() {
		
		return (currentWStepDef != null && currentWStepDef.getUsersRelated() != null ?
				currentWStepDef.getUsersRelated().size():
					0);
	}
	
	public WStepResponseDef getStepResponse() {
		return stepResponse;
	}

	public void setStepResponse(WStepResponseDef stepResponse) {
		this.stepResponse = stepResponse;
	}

	public ArrayList<WStepResponseDef> getResponseList() {
		
		

		if (currentWStepDef != null && currentWStepDef.getResponse() != null
				&& currentWStepDef.getResponse().size() != 0){
			
			return  new ArrayList<WStepResponseDef>(currentWStepDef.getResponse());
		
		}
		
		return null;
	}
	
	public Integer getResponseSize() {
		
		return (currentWStepDef != null && currentWStepDef.getResponse() != null ?
				currentWStepDef.getResponse().size():
					0);
	}
	
	// dml 20120111
	public void addStepResponse(){
		
		WStepResponseDefBL wsrdBL = new WStepResponseDefBL();
		
		try {

			if (stepResponse.getName() != null && !"".equals(stepResponse.getName())){
			
				// dml 20120112
				if (stepResponse.getRespOrder() == null || stepResponse.getRespOrder() == 0){
					
					Integer nextRespOrder = 0;
					
					if (getResponseList() != null){
						for (WStepResponseDef wsrd : getResponseList()){
							if (nextRespOrder < wsrd.getRespOrder()){
								nextRespOrder = wsrd.getRespOrder();
							}
						}
					}
					
					stepResponse.setRespOrder(nextRespOrder+1);
					
				}
				
				wsrdBL.add(stepResponse, this.getCurrentUserId().toString());
				
				this.currentWStepDef.addResponse(stepResponse);

				// dml 20120116
				updateCurrentObject();
				
				loadObject();
				
				//dml 20120112
				setStepResponse(new WStepResponseDef());
				
			}
		
			
		} catch (WStepResponseDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".addStepResponse() WStepDefException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		} catch (WStepDefException ex2) {
			String mensaje = ex2.getMessage() + " - " + ex2.getCause();
			String params[] = { mensaje + ",",
					".addStepResponse() WStepDefException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			ex2.printStackTrace();
		}
		
	}

	// dml 20120111
	public void deleteStepResponse(){
		
		WStepResponseDefBL wsrdBL = new WStepResponseDefBL();
		
		try {
			if (stepResponse != null && stepResponse.getId() != null){
			
				stepResponse = wsrdBL.getWStepResponseDefByPK(stepResponse.getId(), this.getCurrentUserId().toString());
				wsrdBL.delete(stepResponse, this.getCurrentUserId().toString());

				loadObject();
				
			}		
			
		} catch (WStepResponseDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".deleteStepResponse() WStepDefException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}
		
	}

	// dml 20120111
	public void editStepResponse(){
		
		WStepResponseDefBL wsrdBL = new WStepResponseDefBL();
		
		try {
			if (stepResponse != null){
			
				wsrdBL.update(stepResponse, this.getCurrentUserId().toString());

				loadObject();
				
			}		
			
		} catch (WStepResponseDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".editStepResponse() WStepDefException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}
		
	}

	private List<SelectItem> loadWTimeUnitForCombo(){
		
		List<SelectItem> ltu=null;
		
		try {
			ltu = UtilsVs
					.castStringPairToSelectitem(
					new WTimeUnitBL().getComboList("Select time unit","") );
		} catch (WTimeUnitException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".loadWTimeUnitForCombo() WStepDefException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}

		return ltu;
	}
	
	public String getStrRoleList() {
		
//		System.out.println(this.currentWStepDef.getRolesRelated().toString());
		
		String strRoleList="";
		if ( currentWStepDef.getRolesRelated()!=null ) {
			for ( WStepRole sr: this.currentWStepDef.getRolesRelated()) {
				strRoleList+=(strRoleList!=null && !"".equals(strRoleList)?",":"")+sr.getRole().getId();
			}
		}
		
		System.out.println("--------------->>>>>>>>> strRoleList ------------>>>>>>>>"+strRoleList);
		return strRoleList;
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
	
	public List<SelectItem> getTimeUnitComboList(){
		
		WTimeUnitBL wtuBL = new WTimeUnitBL();
		
		try {
			return UtilsVs.castStringPairToSelectitem(wtuBL.getComboList("Select unit ...", ""));
		} catch (WTimeUnitException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".getTimeUnitComboList() WStepDefException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}
		
		return null;
		
	}

	public WStepRole getCurrentWStepRole() {
		return currentWStepRole;
	}

	public void setCurrentWStepRole(WStepRole currentWStepRole) {
		this.currentWStepRole = currentWStepRole;
	}

	public WStepUser getCurrentWStepUser() {
		return currentWStepUser;
	}

	public void setCurrentStepUser(WStepUser currentWStepUser) {
		this.currentWStepUser = currentWStepUser;
	}
	
	// dml 20120113
	public List<WStepUser> getUsersRelatedList(){
		
		List<WStepUser> url= new ArrayList<WStepUser>();
		
		if (currentWStepDef != null && currentWStepDef.getUsersRelated() != null
				&& currentWStepDef.getUsersRelated().size() != 0){
			
			url= new ArrayList<WStepUser>(currentWStepDef.getUsersRelated());
			
		}
		
		return url;
		
	}

	public String getStrUserList() {
		
		strUserList="";
		if ( currentWStepDef.getUsersRelated()!=null ) {
			for ( WStepUser su: this.currentWStepDef.getUsersRelated()) {
				strUserList+=(strUserList!=null && !"".equals(strUserList)?",":"")+su.getUser().getId();
			}
		}
		
		System.out.println("--------------->>>>>>>>> strUserList ------------>>>>>>>>"+strUserList);

		return strUserList;
	}

	public void setStrUserList(String strUserList) {
		this.strUserList = strUserList;
	}

	public void setStrRoleList(String strRoleList) {
		this.strRoleList = strRoleList;
	}

	// dml 20120113
	public void deleteWStepUser(){
		
		if (currentWStepDef != null && currentWStepDef.getUsersRelated() != null
				&& !currentWStepDef.getUsersRelated().isEmpty()){
			
			for (WStepUser wsu : currentWStepDef.getUsersRelated()){
				
				if (wsu.getUser() != null && wsu.getUser().getId() != null 
						&& wsu.getUser().getId().equals(currentWStepUser.getUser().getId())){
					
					currentWStepDef.getUsersRelated().remove(wsu);
					break;
					
				}
					
			}
			
			// dml 20120116
			try{
				
				updateCurrentObject();
			
			strUserList="";
			for ( WStepUser su: this.currentWStepDef.getUsersRelated()) {
				strUserList+=(strUserList!=null && !"".equals(strUserList)?",":"")+su.getUser().getId();
			}

			} catch (WStepDefException e) {
				String mensaje = e.getMessage() + " - " + e.getCause();
				String params[] = { mensaje + ",",
				".deleteWStepUser() WStepDefException ..." };
				agregarMensaje("205", mensaje, params, FGPException.ERROR);
				e.printStackTrace();
			}
	
		}
				
	}
	
	// dml 20120113
	public void changeAdminPrivilegesWStepUser(){
		
		if (currentWStepDef != null && currentWStepDef.getUsersRelated() != null
				&& !currentWStepDef.getUsersRelated().isEmpty()){
			
			for (WStepUser wsr : currentWStepDef.getUsersRelated()){
				
				if (wsr.getUser() != null && wsr.getUser().getId() != null 
						&& wsr.getUser().getId().equals(currentWStepUser.getUser().getId())){
					
					if (wsr.isAdmin()){
						wsr.setAdmin(false);
					}else {
						wsr.setAdmin(true);
					}					
					break;
					
				}
				
			}
			
			// dml 20120116
			try{
				
				updateCurrentObject();
			
			} catch (WStepDefException e) {
				String mensaje = e.getMessage() + " - " + e.getCause();
				String params[] = { mensaje + ",",
				".changeAdminPrivilegesWStepUser() WStepDefException ..." };
				agregarMensaje("205", mensaje, params, FGPException.ERROR);
				e.printStackTrace();
			}
			
		}
		
	}
	
	// dml 20120113
	public List<WStepRole> getRolesRelatedList(){
		
		List<WStepRole> rrl= new ArrayList<WStepRole>();
		
		if (currentWStepDef != null && currentWStepDef.getRolesRelated() != null
				&& currentWStepDef.getRolesRelated().size() != 0){
			
			rrl= new ArrayList<WStepRole>(currentWStepDef.getRolesRelated());
			
		}
		
		return rrl;
		
	}

	// dml 20120113
	public void deleteWStepRole(){
		
		if (currentWStepDef != null && currentWStepDef.getRolesRelated() != null
				&& !currentWStepDef.getRolesRelated().isEmpty()){
			
			for (WStepRole wsr : currentWStepDef.getRolesRelated()){
				
				if (wsr.getRole() != null && wsr.getRole().getId() != null 
						&& wsr.getRole().getId().equals(currentWStepRole.getRole().getId())){
					
					currentWStepDef.getRolesRelated().remove(wsr);
					break;
					
				}
					
			}
			
			// dml 20120116
			try{
				
				updateCurrentObject();
			
				strRoleList="";
				for ( WStepRole sr: this.currentWStepDef.getRolesRelated()) {
					strRoleList+=(strRoleList!=null && !"".equals(strRoleList)?",":"")+sr.getRole().getId();
				}

			} catch (WStepDefException e) {
				String mensaje = e.getMessage() + " - " + e.getCause();
				String params[] = { mensaje + ",",
				".deleteWStepRole() WStepDefException ..." };
				agregarMensaje("205", mensaje, params, FGPException.ERROR);
				e.printStackTrace();
			}
			
		}
			
	}
	
	// dml 20120113
	public void changeAdminPrivilegesWStepRole(){
		
		if (currentWStepDef != null && currentWStepDef.getRolesRelated() != null
				&& !currentWStepDef.getRolesRelated().isEmpty()){
			
			for (WStepRole wsr : currentWStepDef.getRolesRelated()){
				
				if (wsr.getRole() != null && wsr.getRole().getId() != null 
						&& wsr.getRole().getId().equals(currentWStepRole.getRole().getId())){
					
					if (wsr.isAdmin()){
						wsr.setAdmin(false);
					}else {
						wsr.setAdmin(true);
					}					
					break;
					
				}
				
			}
			
			// dml 20120116
			try{
				
				updateCurrentObject();
			
			} catch (WStepDefException e) {
				String mensaje = e.getMessage() + " - " + e.getCause();
				String params[] = { mensaje + ",",
				".changeAdminPrivilegesWStepRole() WStepDefException ..." };
				agregarMensaje("205", mensaje, params, FGPException.ERROR);
				e.printStackTrace();
			}
		}
		
	}
	
	//rrl 20120112
	public String updateRolesRelated() {

		try{
			
			if ("".equals(strRoleList)){
				
				currentWStepDef.setRolesRelated(null);
				
			} else {

				// dml 20120117
				ListUtil.updateStepRoleRelatedList(strRoleList, currentWStepDef, getCurrentUserId());
			
			}
			
			updateCurrentObject();
			
		} catch (NumberFormatException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".updateRolesRelated() NumberFormatException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
		} catch (WRoleDefException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".updateRolesRelated() WRoleDefException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
		} catch (WStepDefException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".updateRolesRelated() WStepDefException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
		}
		
		return null;
	}
	
	//rrl 20120113
	public String updateUsersRelated() {
				
		try{
		
			if ("".equals(strUserList)){
				
				currentWStepDef.setUsersRelated(null);
				
			} else {

				// dml 20120117
				ListUtil.updateStepUserRelatedList(strUserList, currentWStepDef, getCurrentUserId());
				
			}
			
			updateCurrentObject();
			
		} catch (NumberFormatException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".updateUsersRelated() NumberFormatException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
		} catch (WUserDefException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".updateUsersRelated() WUserDefException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
		} catch (WStepDefException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".updateUsersRelated() WStepDefException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
		}
		
		return null;
	}

	private void updateCurrentObject() throws WStepDefException {
		WStepDefBL wsdBL = new WStepDefBL();
		this.setModel();
		wsdBL.update(currentWStepDef, getCurrentUserId());
		this.recoverNullObjects();
	}
	
}