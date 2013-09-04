package org.beeblos.bpm.wc.taglib.bean;

import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;
import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;
import static org.beeblos.bpm.core.util.Constants.FAIL;
import static org.beeblos.bpm.core.util.Constants.NOT_DELETED;
import static org.beeblos.bpm.core.util.Constants.SUCCESS_FORM_WSTEPDEF;
import static org.beeblos.bpm.core.util.Constants.WSTEPDEF_QUERY;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WProcessDataFieldBL;
import org.beeblos.bpm.core.bl.WStepDataFieldBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepHeadBL;
import org.beeblos.bpm.core.bl.WStepResponseDefBL;
import org.beeblos.bpm.core.bl.WStepSequenceDefBL;
import org.beeblos.bpm.core.bl.WTimeUnitBL;
import org.beeblos.bpm.core.error.WProcessDataFieldException;
import org.beeblos.bpm.core.error.WRoleDefException;
import org.beeblos.bpm.core.error.WStepDataFieldException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepHeadException;
import org.beeblos.bpm.core.error.WStepResponseDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WTimeUnitException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WStepDataField;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepHead;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepUser;
import org.beeblos.bpm.core.model.WTimeUnit;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.noper.BeeblosAttachment;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.ListUtil;
import org.beeblos.bpm.wc.taglib.util.WStepDefUtil;

import com.sp.common.jsf.util.UtilsVs;

public class WStepDefFormBean extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(WStepDefFormBean.class);
	
	private static final String MANAGED_BEAN_NAME = "wStepDefFormBean";
	
	public static WStepDefFormBean getCurrentInstance() {
        return (WStepDefFormBean) FacesContext.getCurrentInstance().getExternalContext()
            .getRequestMap().get(MANAGED_BEAN_NAME);
    }

	private TimeZone timeZone;
	
    private Integer currentUserId; // session user

    // main properties:
    
	private WStepDef currentWStepDef; 
	private Integer currObjId; // current object id managed by this backing bean
	// dml 20120526
	private Integer currentProcessDefId;
	//rrl 20130801
	private Integer currentProcessHeadId;
	
	// auxiliar properties

	private BeeblosAttachment attachment;
	private String documentLink;
	
	private List<SelectItem> lWTimeUnit; // to show time unit list

			// properties to handle (edit, update, etc) step's roles, users and responses 
	private WStepResponseDef currentStepResponse;
	private WStepRole currentWStepRole;
	private WStepUser currentWStepUser;	
	
	private boolean readOnly; // help to view to know when is in edit mode and when in display mode
	private String strRoleList; // property to pass current role list ids to popup
	private String strUserList; // property to pass current role list ids to popup
	
	//rrl 20130801 
	private String strDataFieldList; // property to pass current role list ids to popup
	
	// dml 20120505
	private String returnStatement;
	
	// dml 20120523
	private List<WStepSequenceDef> outgoingRoutes;
	private Integer outgoingRoutesSize;
	private List<WStepSequenceDef> incomingRoutes;
	private Integer incomingRoutesSize;

	// dml 20130507
	private Integer currentStepHeadIdSelected;
	private List<SelectItem> wStepHeadComboList;
	
	// dml 20130508
	private List<WStepDef> relatedStepDefList; // lista de steps para 1 step-head (o sea las versiones de 1 step)
	private String activeFilter;
	
	//rrl 20130805
	private boolean visibleButtonEditDataField;
	private WStepDataField wStepDataFieldSelected;
	
	
	public WStepDefFormBean() {		
		super();
		
		init();  
	}
    
	public void init(){
		super.init_core();
		
		setShowHeaderMessage(false);  
		
		this.loadWTimeUnitForCombo();
		
		// nes 20130722
		if (currObjId!=null && !currObjId.equals(0)) {
			// dml 20120523
			this.loadOutgoingRouteList();
			this.loadIncomingRouteList();
		}
		
		this._loadWStepHeadComboList(); // dml 20130508

		_reset();
	}

	// to add a new WStepDef
	public void initEmptyWStepDef() {

		currObjId = null;
		currentProcessDefId = null;
		
		currentWStepDef = new WStepDef(EMPTY_OBJECT);
		
		recoverNullObjects();
		
		this.setReadOnly(false);
				
	}

	public void _reset() {
	
		this.currObjId=new Integer(0);
		this.currentWStepDef=null;
		
		this.currentStepResponse = new WStepResponseDef();
		this.currentWStepRole = new WStepRole();
		this.currentWStepUser = new WStepUser();

		attachment = new BeeblosAttachment();
		documentLink=null; 
		returnStatement= "";
		
		this.setReadOnly(true); // backing bean handle object in read-only mode by default

		//		HelperUtil.recreateBean("documentacionBean", "com.softpoint.taglib.common.DocumentacionBean");

		//rrl 20130805
		visibleButtonEditDataField = true;
		wStepDataFieldSelected = new WStepDataField(EMPTY_OBJECT); 

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
			
			// dml 20120606 - if this field is an empty string, it will be set null in the DB
			if ( currentWStepDef.getIdListZone()!=null
					&& "".equals(currentWStepDef.getIdListZone().trim())) {
				currentWStepDef.setIdListZone( null );
			}			

			// dml 20120606 - if this field is an empty string, it will be set null in the DB
			if ( currentWStepDef.getIdWorkZone()!=null
					&& "".equals(currentWStepDef.getIdWorkZone().trim()) ) {
				currentWStepDef.setIdWorkZone( null );
			}			

			// dml 20120606 - if this field is an empty string, it will be set null in the DB
			if ( currentWStepDef.getIdAdditionalZone()!=null
					&& "".equals(currentWStepDef.getIdAdditionalZone().trim()) ) {
				currentWStepDef.setIdAdditionalZone( null );
			}			

			// dml 20120622 - if this field is an empty string, it will be set null in the DB
			if ( currentWStepDef.getIdDefaultProcessor()!=null
					&& "".equals(currentWStepDef.getIdDefaultProcessor().trim()) ) {
				currentWStepDef.setIdDefaultProcessor( null );
			}			

			// dml 20120622 - if this field is an empty string, it will be set null in the DB
			if ( currentWStepDef.getCustomSaveMethod()!=null
					&& "".equals(currentWStepDef.getCustomSaveMethod().trim()) ) {
				currentWStepDef.setCustomSaveMethod( null );
			}			

			// dml 20120622 - if this field is an empty string, it will be set null in the DB
			if ( currentWStepDef.getCustomSaveRefClass()!=null
					&& "".equals(currentWStepDef.getCustomSaveRefClass().trim()) ) {
				currentWStepDef.setCustomSaveRefClass( null );
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

			// dml 20130508
			if (currentWStepDef.getStepHead() == null) {
				currentWStepDef.setStepHead(new WStepHead());
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
				&& !"".equals(this.currentWStepDef.getName())
				&& this.currentWStepDef.getVersion() != null
				&& !"".equals(this.currentWStepDef.getVersion())){
				
			result = true;
			
		// dml 20130508 - si estamos modificando el steo interno tenemos que ver que el nombre no sea vacio	
		} else if (this.currentWStepDef.getStepHead() != null
			&& this.currentWStepDef.getStepHead().getName() != null
			&& !"".equals(this.currentWStepDef.getStepHead().getName())) {

		result = true;

	} else {
			
			String message = "You must insert correct values";
			super.createWindowMessage(ERROR_MESSAGE, message, null);
		}

		return result;
	}
	
	

	public String cancel(){
		
		if (returnStatement.equals(WSTEPDEF_QUERY)){
			
			_reset();
			return WSTEPDEF_QUERY;
			
		}
		
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
				result = _defineReturn();
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
		String message = "";

		String ret = null;
		
		// if object already exists in db then update and return
		if(currentWStepDef!=null 
				&& currentWStepDef.getId()!=null && currentWStepDef.getId()!=0){ 

			// before update store document in repository ( if exists )
			if (attachment.getDocumentoNombre()!=null && 
					!"".equals(attachment.getDocumentoNombre())) {
				//storeInRepository(); 
			}
			
			message = "The step '" + this.currentWStepDef.getStepHead().getName()+ "' has been correctly updated";

			return update();
		}
		
		WStepDefBL wsdBL = new WStepDefBL();
	
		try {	
			
			setModel(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>

			currObjId = wsdBL.add(currentWStepDef, this.getCurrentUserId() );
			
			message = "The step '" + this.currentWStepDef.getStepHead().getName()+ "' has been correctly added";

			// Manual process to store attachment in the repository ( if attachment exists ...
			if (attachment.getDocumentoNombre()!=null && !"".equals(attachment.getDocumentoNombre())) {
				//storeInRepository();
				update();

				message = "The step '" + this.currentWStepDef.getStepHead().getName()+ "' has been correctly updated";

			}
			
			recoverNullObjects();
			
			this.setReadOnly(true);

			ret=SUCCESS_FORM_WSTEPDEF;
			
			setShowHeaderMessage(true);
			
		} catch (WStepDefException e) {

			message = "WStepDefFormBean.add() WStepDefException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			
			throw new WStepDefException(message);			
			
		} catch (Exception e) {
			
			message = "WStepDefFormBean.add() Exception: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			
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
			
			wsdBL.update(currentWStepDef, currentProcessHeadId, this.getCurrentUserId() ); // nes 20130808 - por agregado de filtro en step-data-field
			
			recoverNullObjects();
			
			this.setReadOnly(true);

			ret=SUCCESS_FORM_WSTEPDEF;

			setShowHeaderMessage(true);

		} catch (WStepDefException e) {
			String message = "WStepDefFormBean.update() WStepDefException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
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
						.getWStepDefByPK(this.currObjId, currentProcessHeadId, this.getCurrentUserId() ); // nes 20130808 - por agregado de filtro en step-data-field
			
			recoverNullObjects();//new org.beeblos.bpm.core.bl.WStepDataFieldBL().getWStepDataFieldByPK(1, 1000);
			
			// dml 20120523
			this.loadOutgoingRouteList();

			// dml 20120523
			this.loadIncomingRouteList();
			
			this.reloadRelatedStepDefList(); // dml 20130508
			
		} catch (WStepDefException e) {
			String message = "WStepDefFormBean.loadObject() WStepDefException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}			
	}
	
	// dml 20130508
	public void reloadRelatedStepDefList(){
		
		try {
			
 			this.relatedStepDefList = new WStepDefBL()
				.finderWStepDef(null, null, null, null, false, null, 
						this.currentWStepDef.getStepHead().getId(), this.activeFilter);
				
		} catch (WStepDefException e) {

			this.relatedStepDefList = new ArrayList<WStepDef>();

			String message = "WStepDefFormBean.reloadRelatedStepDefList() WStepDefException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
	}

	public void loadResponse(){
		
		if (currentStepResponse != null && currentStepResponse.getId() != null
				&& currentStepResponse.getId() != 0){
			
			try {
				
				currentStepResponse = 
						new WStepResponseDefBL()
								.getWStepResponseDefByPK(currentStepResponse.getId(), getCurrentUserId() );
			
			} catch (WStepResponseDefException e) {
				String message = "WStepDefFormBean.loadResponse() WStepResponseDefException: " + e.getMessage() + " - " + e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
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
		return currentStepResponse;
	}

	public void setStepResponse(WStepResponseDef stepResponse) {
		this.currentStepResponse = stepResponse;
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
	
	public void addStepResponse(){
		
		WStepResponseDefBL wsrdBL = new WStepResponseDefBL();
		
		try {

			if (currentStepResponse.getName() != null && 
					!"".equals(currentStepResponse.getName())) {
			
				if ( currentStepResponse.getRespOrder() == null || 
						currentStepResponse.getRespOrder().equals(0) ) {
					
					currentStepResponse.setRespOrder( getNextResponseOrder() );
					
				}
				
				wsrdBL.add( currentStepResponse, this.getCurrentUserId() );
				
				this.currentWStepDef.addResponse(currentStepResponse);

				persistCurrentObject();
				
				loadObject();
				
				setStepResponse(new WStepResponseDef());
				
			} else {
				
				String message = "You should put a correct name value.";
				super.createWindowMessage(ERROR_MESSAGE, message, null);
			}
			
		} catch (WStepResponseDefException e) {
			String message = "WStepDefFormBean.addStepResponse() WStepResponseDefException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (WStepDefException e) {
			String message = "WStepDefFormBean.addStepResponse() WStepDefException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
	}

	private Integer getNextResponseOrder() {
		
		Integer nextRespOrder = 0;
		
		if (getResponseList() != null){
			for (WStepResponseDef wsrd : getResponseList()){
				if (nextRespOrder < wsrd.getRespOrder()){
					nextRespOrder = wsrd.getRespOrder();
				}
			}
		}
		
		return nextRespOrder+1;
	}

	public void deleteStepResponse(){
		
		WStepResponseDefBL wsrdBL = new WStepResponseDefBL();
		
		try {
			
			if (currentStepResponse != null && currentStepResponse.getId() != null){
			
				currentStepResponse = wsrdBL.getWStepResponseDefByPK(currentStepResponse.getId(), this.getCurrentUserId() );
				wsrdBL.delete(currentStepResponse, this.getCurrentUserId() );

				loadObject();
				
			}		
			
		} catch (WStepResponseDefException e) {
			String message = "WStepDefFormBean.deleteStepResponse() WStepResponseDefException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
	}

	public void editStepResponse(){
		
		WStepResponseDefBL wsrdBL = new WStepResponseDefBL();
		
		try {
			
			if (currentStepResponse != null){
			
				wsrdBL.update(currentStepResponse, this.getCurrentUserId() );

				loadObject();
				
			}		
			
		} catch (WStepResponseDefException e) {
			String message = "WStepDefFormBean.editStepResponse() WStepResponseDefException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
	}

	// dml 20120523
	private ArrayList<WStepSequenceDef> loadOutgoingRouteList() {
		
		WStepSequenceDefBL wssBL = new WStepSequenceDefBL();
		
		try {
			/*
			 * Nota Nestor: 20130722 - en la tabla sequence tenemos previstas las rutas de entrada y salida a un paso para 1 proceso:
			 * O sea el campo process_id indica a que proceso corresponde esa ruta, id_origin_step y id_dest_step indican desde
			 * que paso y hasta cual va la ruta.
			 * 
			 * Ahora bien: parados en 1 paso, no tenemos el idProcess para obtener solamente las rutas de ese paso para ese proceso.
			 * Por ese motivo, desde el punto de vista del paso en si, deberíamos traer todas las rutas independientemente
			 * del proceso al que estén relacionadas, y desde el punto de vista del proceso/paso, deberíamos traer solaemente las
			 * de ese proceso ...
			 * 
			 * Por ese motivo en este método seteo process_id a null porque no hay nada que indique que estamos parados en 1 proceso...
			 */
			outgoingRoutes = wssBL.getOutgoingRoutes(this.currObjId, NOT_DELETED, currentProcessDefId, getCurrentUserId());

			if (outgoingRoutes != null){
				outgoingRoutesSize = outgoingRoutes.size();
			} else {
				outgoingRoutesSize = 0;
			}
			
		} catch (WStepSequenceDefException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return null;
	}
	
	// dml 20120523
	// nes 20130722 - ver comentario en el método anterior que vale también para este ...
	private ArrayList<WStepSequenceDef> loadIncomingRouteList() {
		
		WStepSequenceDefBL wssBL = new WStepSequenceDefBL();
		
		try {

			incomingRoutes = wssBL.getIncomingRoutes(this.currObjId, NOT_DELETED, currentProcessDefId, getCurrentUserId());

			if (incomingRoutes != null){
				incomingRoutesSize = incomingRoutes.size();
			} else {
				incomingRoutesSize = 0;
			}
			
		} catch (WStepSequenceDefException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return null;
	}
	
	private void loadWTimeUnitForCombo(){
		
		try {
			
			lWTimeUnit = UtilsVs
					.castStringPairToSelectitem(
					new WTimeUnitBL().getComboList("Select time unit","") );
			
		} catch (WTimeUnitException e) {
			String message = "WStepDefFormBean.loadWTimeUnitForCombo() WTimeUnitException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

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
		} catch (WTimeUnitException e) {
			String message = "WStepDefFormBean.getTimeUnitComboList() WTimeUnitException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
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
		
		logger.debug("--------------->>>>>>>>> strUserList ------------>>>>>>>>"+strUserList);

		return strUserList;
	}

	public void setStrUserList(String strUserList) {
		this.strUserList = strUserList;
	}

	//rrl 20130805
	public String getStrDataFieldList() {

		strDataFieldList="";
		if ( currentWStepDef != null
				&& currentWStepDef.getDataFieldDef()!=null ) {
			for ( WStepDataField su: this.currentWStepDef.getDataFieldDef()) {
				if (su.getDataField()!=null) {
					strDataFieldList+=(strDataFieldList!=null && !"".equals(strDataFieldList)?",":"")+su.getDataField().getId();
				}
			}
		}
		
		logger.debug("--------------->>>>>>>>> strDataFieldList ------------>>>>>>>>"+strDataFieldList);

		return strDataFieldList;
	}
	
	public void setStrDataFieldList(String strDataFieldList) {
		this.strDataFieldList = strDataFieldList;
	}

	public String getStrRoleList() {
		
		String strRoleList="";
		if ( currentWStepDef.getRolesRelated()!=null ) {
			for ( WStepRole sr: this.currentWStepDef.getRolesRelated()) {
				strRoleList+=(strRoleList!=null && !"".equals(strRoleList)?",":"")+sr.getRole().getId();
			}
		}
		
		logger.debug("--------------->>>>>>>>> strRoleList ------------>>>>>>>>"+strRoleList);
		return strRoleList;
	}

	public void setStrRoleList(String strRoleList) {
		this.strRoleList = strRoleList;
	}

	public String getReturnStatement() {
		return returnStatement;
	}

	public void setReturnStatement(String returnStatement) {
		this.returnStatement = returnStatement;
	}

	public String getActiveFilter() {
		return activeFilter;
	}

	public void setActiveFilter(String activeFilter) {
		this.activeFilter = activeFilter;
	}

	public List<WStepSequenceDef> getOutgoingRoutes() {
		return outgoingRoutes;
	}

	public void setOutgoingRoutes(List<WStepSequenceDef> outgoingRoutes) {
		this.outgoingRoutes = outgoingRoutes;
	}

	public Integer getOutgoingRoutesSize() {
		return outgoingRoutesSize;
	}

	public void setOutgoingRoutesSize(Integer outgoingRoutesSize) {
		this.outgoingRoutesSize = outgoingRoutesSize;
	}

	public List<WStepSequenceDef> getIncomingRoutes() {
		return incomingRoutes;
	}

	public void setIncomingRoutes(List<WStepSequenceDef> incomingRoutes) {
		this.incomingRoutes = incomingRoutes;
	}

	public Integer getIncomingRoutesSize() {
		return incomingRoutesSize;
	}

	public void setIncomingRoutesSize(Integer incomingRoutesSize) {
		this.incomingRoutesSize = incomingRoutesSize;
	}

	public Integer getCurrentStepHeadIdSelected() {
		return currentStepHeadIdSelected;
	}

	public void setCurrentStepHeadIdSelected(Integer currentStepHeadIdSelected) {
		this.currentStepHeadIdSelected = currentStepHeadIdSelected;
	}

	public List<SelectItem> getwStepHeadComboList() {
		return wStepHeadComboList;
	}

	public void setwStepHeadComboList(List<SelectItem> wStepHeadComboList) {
		this.wStepHeadComboList = wStepHeadComboList;
	}

	public List<WStepDef> getRelatedStepDefList() {
		return relatedStepDefList;
	}

	public void setRelatedStepDefList(List<WStepDef> relatedStepDefList) {
		this.relatedStepDefList = relatedStepDefList;
	}

	public Integer getCurrentProcessDefId() {
		return currentProcessDefId;
	}

	public void setCurrentProcessDefId(Integer currentProcessDefId) {
		this.currentProcessDefId = currentProcessDefId;
	}

	//rrl 20130801
	public Integer getCurrentProcessHeadId() {
		return currentProcessHeadId;
	}

	public void setCurrentProcessHeadId(Integer currentProcessHeadId) {
		this.currentProcessHeadId = currentProcessHeadId;
	}
	
	//rrl 20130805
	public boolean isVisibleButtonEditDataField() {
		return visibleButtonEditDataField;
	}

	public void setVisibleButtonEditDataField(boolean visibleButtonEditDataField) {
		this.visibleButtonEditDataField = visibleButtonEditDataField;
	}
	
	public WStepDataField getwStepDataFieldSelected() {
		return wStepDataFieldSelected;
	}
	
	public void setwStepDataFieldSelected(
			WStepDataField wStepDataFieldSelected) {
		this.wStepDataFieldSelected = wStepDataFieldSelected;
	}
	
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
			
			try{
				
				persistCurrentObject();
			
			strUserList="";
			for ( WStepUser su: this.currentWStepDef.getUsersRelated()) {
				strUserList+=(strUserList!=null && !"".equals(strUserList)?",":"")+su.getUser().getId();
			}

			} catch (WStepDefException e) {
				String message = "WStepDefFormBean.deleteWStepUser() WStepDefException: " + e.getMessage() + " - " + e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			}
	
		}
				
	}
	
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
			
			try{
				
				persistCurrentObject();
			
			} catch (WStepDefException e) {
				String message = "WStepDefFormBean.changeAdminPrivilegesWStepUser() WStepDefException: " + e.getMessage() + " - " + e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			}
			
		}
		
	}
	
	public List<WStepRole> getRolesRelatedList(){
		
		List<WStepRole> rrl= new ArrayList<WStepRole>();
		
		if (currentWStepDef != null && currentWStepDef.getRolesRelated() != null
				&& currentWStepDef.getRolesRelated().size() != 0){
			
			rrl= new ArrayList<WStepRole>(currentWStepDef.getRolesRelated());
			
		}
		
		return rrl;
		
	}

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
			
			try{
				
				persistCurrentObject();
			
				strRoleList="";
				for ( WStepRole sr: this.currentWStepDef.getRolesRelated()) {
					strRoleList+=(strRoleList!=null && !"".equals(strRoleList)?",":"")+sr.getRole().getId();
				}

			} catch (WStepDefException e) {
				String message = "WStepDefFormBean.deleteWStepRole() WStepDefException: " + e.getMessage() + " - " + e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			}
			
		}
			
	}
	
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
			
			try{
				
				persistCurrentObject();
			
			} catch (WStepDefException e) {
				String message = "WStepDefFormBean.changeAdminPrivilegesWStepRole() WStepDefException: " + e.getMessage() + " - " + e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			}
		}
		
	}
	
	public String updateRolesRelated() {

		try {
			
			if ("".equals(strRoleList)) {
				
				currentWStepDef.setRolesRelated(null);
				
			} else {

				ListUtil.updateStepRoleRelatedList(strRoleList, currentWStepDef, getCurrentUserId());
			
			}
			
			persistCurrentObject();
			
		} catch (NumberFormatException e) {
			String message = "WStepDefFormBean.updateRolesRelated() NumberFormatException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (WRoleDefException e) {
			String message = "WStepDefFormBean.updateRolesRelated() WRoleDefException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (WStepDefException e) {
			String message = "WStepDefFormBean.updateRolesRelated() WStepDefException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
		return null;
	}
	
	public String updateUsersRelated() {
				
		try {
		
			if ("".equals(strUserList)) {
				
				currentWStepDef.setUsersRelated(null);
				
			} else {

				ListUtil.updateStepUserRelatedList( strUserList, currentWStepDef, getCurrentUserId() );
				
			}
			
			persistCurrentObject();
			
		} catch (NumberFormatException e) {
			String message = "WStepDefFormBean.updateUsersRelated() NumberFormatException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (WUserDefException e) {
			String message = "WStepDefFormBean.updateUsersRelated() WUserDefException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (WStepDefException e) {
			String message = "WStepDefFormBean.updateUsersRelated() WStepDefException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
		return null;
	}

	private void persistCurrentObject() throws WStepDefException {
		WStepDefBL wsdBL = new WStepDefBL();
		this.setModel();
		wsdBL.update(currentWStepDef, currentProcessHeadId, getCurrentUserId()); // nes 20130808 - por agregado de filtro en step-data-field
		this.recoverNullObjects();
	}

	// dml 20120222
	public void resetCustomValidation(){

		if (!currentWStepDef.isCustomValidation()) {
			
			currentWStepDef.setCustomValidationRefClass(null);
			currentWStepDef.setCustomValidationMethod(null);
			currentWStepDef.setBackingBean(false);
			
		}

	}

	// nes 20120205
	private String _defineReturn() {

		String ret = returnStatement;
		returnStatement="";
		
		return ret;
		
	}
	
	// dml 20130508
	public void _loadWStepHeadComboList(){
		
		try {
			
			this.wStepHeadComboList = UtilsVs.castStringPairToSelectitem(
					new WStepHeadBL().getComboList("Select ...", null));
			
		} catch (WStepHeadException e) {
			
			this.wStepHeadComboList = new ArrayList<SelectItem>();
			
			String message = "WStepDefFormBean._loadWStepHeadComboList() WStepHeadException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
	}
	
	// dml 20130508
	public void setStepInWStepDef(){
		
		if (this.currentStepHeadIdSelected != null
				&& !this.currentStepHeadIdSelected.equals(0)){
			
			try {
				
				WStepHead stepHead = new WStepHeadBL().getWStepHeadByPK(this.currentStepHeadIdSelected, null);
			
				Integer lastVersion = new WStepDefBL().getLastVersionNumber(this.currentStepHeadIdSelected);
				
				this.currentWStepDef = new WStepDef(EMPTY_OBJECT);

				this.currentWStepDef.setStepHead(stepHead);
				
				this.currentWStepDef.setVersion(lastVersion + 1);
				
			} catch (WStepHeadException e) {
				String message = "WStepDefFormBean.setStepInWStepDef() WStepHeadException: " + e.getMessage() + " - " + e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (WStepDefException e) {
				String message = "WStepDefFormBean.setStepInWStepDef() WStepDefException: " + e.getMessage() + " - " + e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			}
			
		} else {
			
			this.currentWStepDef = new WStepDef(EMPTY_OBJECT);

		}

	}

	// dml 20120108
	public String loadWStepHeadForm() {

		return new WStepDefUtil().loadWStepHeadFormBean(this.currObjId);

	}
	
	// dml 20120108
	public String loadWStepDefForm() {

		return new WStepDefUtil().loadWStepDefFormBean(this.currObjId);

	}

	//rrl 20130801 - nes 20130803 
	public List<WStepDataField> getStepDataFieldList() {

		List<WStepDataField> stepDataFieldList = new ArrayList<WStepDataField>();
		
		//rrl 20130805 verified before currentWStepDef is not null
		if (currentWStepDef !=null) {
			stepDataFieldList = currentWStepDef.getStepDataFieldList();
		}

		return stepDataFieldList;
	}

	public Integer getStepDataFieldListSize() {
		
		return (currentWStepDef!=null 
					&& currentWStepDef.getDataFieldDef() != null 
							? currentWStepDef.getDataFieldDef().size()
							: 0);
	}

	//rrl 20130805
	public String updateDataFieldsRelated() {
		
		try {
		
			if ("".equals(strDataFieldList)) {
				
				WStepDataFieldBL wStepDataFieldBL = new WStepDataFieldBL();
				Set<WStepDataField> dataFieldRelated = 
						new WStepDataFieldBL()
								.getWStepDataFieldSet(currentProcessHeadId, currentWStepDef.getStepHead().getId(), null);
				if (dataFieldRelated != null) {
					for (WStepDataField wsdf : dataFieldRelated) {
						wStepDataFieldBL.delete(wsdf, null);
					}
				}
				
			} else {

				updateStepDataFieldRelatedList();
				
			}
			
			loadObject();
			
		} catch (NumberFormatException e) {
			String message = "WStepDefFormBean.updateDataFieldsRelated() NumberFormatException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (WProcessDataFieldException e) {
			String message = "WStepDefFormBean.updateDataFieldsRelated() WProcessDataFieldException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (WStepDataFieldException e) {
			String message = "WStepDefFormBean.updateDataFieldsRelated() WStepDataFieldException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
		return null;
	}
	
	//rrl 20130805
	private void updateStepDataFieldRelatedList() 
			throws NumberFormatException, WStepDataFieldException, WProcessDataFieldException {
				
		WStepDataFieldBL wStepDataFieldBL = new WStepDataFieldBL();

		boolean isInList = false;
		
		Set<WStepDataField> dataFieldRelated = 
				new WStepDataFieldBL()
						.getWStepDataFieldSet(currentProcessHeadId, currentWStepDef.getStepHead().getId(), null);
		
		if (dataFieldRelated != null) {

			for (String s : strDataFieldList.split(",")) {

				isInList = false;
				for (WStepDataField wsdf : dataFieldRelated) {
					if (wsdf.getDataField()!=null && wsdf.getDataField().getId().equals(Integer.parseInt(s))) {
						isInList = true;
						break;
					}
				}

				if (!isInList) {
					
					WStepDataField stepDataField = new WStepDataField();
					
					stepDataField.setStepHeadId(currentWStepDef.getStepHead().getId());
					stepDataField.setProcessHeadId(currentProcessHeadId);// nes 201030808 por incorporacion del processHeaId en step-data-field
					stepDataField.setDataField(new WProcessDataFieldBL().getWProcessDataFieldByPK(Integer.parseInt(s), null));
					wStepDataFieldBL.add(stepDataField, getCurrentUserId());
				}
			}

			ArrayList<WStepDataField> removeList = new ArrayList<WStepDataField>();
			for (WStepDataField wsdf : dataFieldRelated) {

				isInList = false;

				for (String s : strDataFieldList.split(",")) {

					if (wsdf.getDataField().getId().equals(Integer.parseInt(s))) {
						isInList = true;
						break;
					}
				}

				if (!isInList) {
					removeList.add(wsdf);
				}
			}

			for (WStepDataField wsdf : removeList) {
				wStepDataFieldBL.delete(wsdf, null);
			}
		}
	}

	//rrl 20130805
	public void initializeDataFieldsAddNew() {
		
		this.wStepDataFieldSelected = new WStepDataField(EMPTY_OBJECT);
		visibleButtonEditDataField = false;
		
	}
	
	public void initializeDataFieldsCloseAddNew() {
	
		this.wStepDataFieldSelected = new WStepDataField(EMPTY_OBJECT);
		visibleButtonEditDataField = true;
		
	}

	public String saveEditedDataField() {
		
		WStepDataFieldBL wdfBL = new WStepDataFieldBL();

		try {
		
			if (validationSave()) {
				wdfBL.update(wStepDataFieldSelected, this.getCurrentUserId());
				this.loadObject();
				initializeDataFieldsCloseAddNew();
			}
			
		} catch (WStepDataFieldException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//rrl 20130807
	private boolean validationSave() {
		
		boolean result = true;

		//rrl 20130806 Length is greater than allowable maximum
		if (wStepDataFieldSelected.getDataField()!=null &&
			wStepDataFieldSelected.getDataField().getLength()!=null &&
			wStepDataFieldSelected.getLength()!=null &&
			wStepDataFieldSelected.getLength() > wStepDataFieldSelected.getDataField().getLength()) {
			result = false;
		}

		// if length is ZERO then assign the value of processDataField
		if (wStepDataFieldSelected.getLength()!=null &&
			wStepDataFieldSelected.getLength().equals(0)) {
			wStepDataFieldSelected.setLength( wStepDataFieldSelected.getDataField().getLength() );
		}
		
		return result;
	}
	
	
	public String cancelEditDataField() {

		initializeDataFieldsCloseAddNew();
		
		return null;
	}
	
	public void loadDataField(){
		WStepDataFieldBL wdfBL = new WStepDataFieldBL();
		
		try {

			if (wStepDataFieldSelected != null && wStepDataFieldSelected.getId() != null && 
					wStepDataFieldSelected.getId() != 0) {
			
				wStepDataFieldSelected = 
						wdfBL.getWStepDataFieldByPK(wStepDataFieldSelected.getId(), getCurrentUserId());

				visibleButtonEditDataField = false;
				
			}
			
		} catch (WStepDataFieldException e) {
			String message = "WStepDefFormBean.loadDataField() WStepDataFieldException: " + e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
	}
	
	
}