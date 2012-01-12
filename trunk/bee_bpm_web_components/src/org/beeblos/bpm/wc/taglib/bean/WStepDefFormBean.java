package org.beeblos.bpm.wc.taglib.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepResponseDefBL;
import org.beeblos.bpm.core.bl.WTimeUnitBL;
import org.beeblos.bpm.core.error.ObjectException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepResponseDefException;
import org.beeblos.bpm.core.error.WTimeUnitException;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WStepUser;
import org.beeblos.bpm.core.model.noper.BeeblosAttachment;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.UtilsVs;

/**
 * Empty backing bean 
 *
 * @author nes
 *
 */

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

	public void _reset() {
	
		this.currObjId=new Integer(0);
		this.currentWStepDef=null;
		
		// dml 20110111
		this.stepResponse = new WStepResponseDef();
		
		attachment = new BeeblosAttachment();

		documentLink=null; 
		
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
	private void createNullObjectTypeProperties(){
		
		if(currentWStepDef != null){
			
//			if ( currentWStepDef.getPropertyObjectType1 == null) {
//				currentWStepDef.setPropertyObjectType1(new ObjectType1(VACIO));
//			}
			
		}
		
	}
	
	// checks input data before save or update
	private boolean checkInputData() {
		
		boolean result = false;
		
		return result;
	}
	
	

	public String cancel(){
		
		_reset();
		return null;
	}

	
    public String save_continue() {

    	String result =  null;
				
//		if ( checkInputData() ) {
//			
//			try {
//				
//				result = add();
//				createNullObjectTypeProperties(); //<<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>
//				
//			} catch (ObjectException e) {
//				createNullObjectTypeProperties();
//				result =  null;
//			}
//		}

		return result;
	}
	
	public String save() {
		
		String result =  null;
		
		if ( checkInputData() ) {
			
			try {
				
				result = add();
				_reset();
				
			} catch (ObjectException e) {
				createNullObjectTypeProperties(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>
				result =  null;
			}
		}

		return result;
	}
	

	
	
	public String add() throws ObjectException {		
		logger.debug("ComplexWStepDefanagementBean: add: objectId:["+this.currObjId+"] ");
		
		setShowHeaderMessage(false);

		String ret = null;
		
		// if object already exists in db then update and return
		if(currentWStepDef!=null 
//				&& currentWStepDef.getId()!=null && currentWStepDef.getId()!=0 
				){ 

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
			
			createNullObjectTypeProperties();
			
			ret="OK";
			setShowHeaderMessage(true);
			
		} catch (WStepDefException ex1) {

			String message = ex1.getMessage() + " - "+ ex1.getCause();
			String params[] = {message + ",", ".Please confirm input values." };				
			agregarMensaje("1001",message,params,FGPException.ERROR);	
			
			throw new ObjectException(message);			
			
		} catch (Exception e) {
			
			String message = e.getMessage() + " - "+ e.getCause();
			String params[] = {message + ",", ".Error inserting object ..." };
			agregarMensaje("1001",message,params ,FGPException.ERROR);
			
			throw new ObjectException(message);			
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
			
			createNullObjectTypeProperties();
			
			setShowHeaderMessage(true);
			ret="OK";

		} catch (WStepDefException ex1) {

			String message = "Error updating object: "
					+ currentWStepDef.getId()
					+ " - "
					+ currentWStepDef.getName()
					+ "\n"
					+ ex1.getMessage() + "\n" + ex1.getCause();
			
			logger.error(message);
			
			String params[] = {message + ",", ".Please confirm input values." };				
			agregarMensaje("1001",message,params,FGPException.ERROR);	

			logger.error(message);
			
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
			
		} catch (WStepDefException ex1) {
			logger.error("Error retrieving object: "
							+ currentWStepDef.getId()
							+ " : "
							+ ex1.getMessage()
							+ " - "
							+ ex1.getCause());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	public void loadResponse(){
		
		if (stepResponse != null && stepResponse.getId() != null
				&& stepResponse.getId() != 0){
			
			try {
				
				stepResponse = new WStepResponseDefBL().getWStepResponseDefByPK(stepResponse.getId(), getCurrentUserId().toString());
			
			} catch (WStepResponseDefException ex1) {

				logger.error("Error retrieving object: "
						+ stepResponse.getId()
						+ " : "
						+ ex1.getMessage()
						+ " - "
						+ ex1.getCause());

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

	public List<WStepResponseDef> getResponseList() {
		
		List<WStepResponseDef> lresp = new ArrayList<WStepResponseDef>();
		
		if (currentWStepDef != null && currentWStepDef.getResponse() != null
				&& currentWStepDef.getResponse().size() != 0){
			
			lresp= new ArrayList<WStepResponseDef>( currentWStepDef.getResponse() );

		}
		
		return lresp;
	}
	
	public Integer getResponseSize() {
		
		return (currentWStepDef != null && currentWStepDef.getResponse() != null ?
				currentWStepDef.getResponse().size():
					0);
	}
	
	// dml 20120111
	public void addStepResponse(){
		
		WStepResponseDefBL wsrdBL = new WStepResponseDefBL();
		WStepDefBL wsdBL = new WStepDefBL();
		
		try {

			if (stepResponse.getName() != null && !"".equals(stepResponse.getName())){
			
				// dml 20120112
				if (stepResponse.getRespOrder() == null || stepResponse.getRespOrder() == 0){
					
					Integer nextRespOrder = 0;
					
					for (WStepResponseDef wsrd : getResponseList()){
						
						if (nextRespOrder < wsrd.getRespOrder()){
							
							nextRespOrder = wsrd.getRespOrder();
							
						}
						
					}
					
					stepResponse.setRespOrder(nextRespOrder+1);
					
				}
				
				wsrdBL.add(stepResponse, this.getCurrentUserId().toString());
				
				this.currentWStepDef.addResponse(stepResponse);
				wsdBL.update(currentWStepDef, this.getCurrentUserId());
		
				loadObject();
				getResponseList();
				getResponseSize();
				
				//dml 20120112
				setStepResponse(new WStepResponseDef());
				
			}
		
			
		} catch (WStepResponseDefException ex1) {
			logger.warn("WStepResponseDefException: Error trying to add response "
					+ ex1.getMessage()+" - "+ex1.getCause());
		} catch (WStepDefException ex2) {
			logger.warn("WStepDefException: Error trying to add response "
					+ ex2.getMessage()+" - "+ex2.getCause());
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
				getResponseList();
				getResponseSize();
				
			}		
			
		} catch (WStepResponseDefException ex1) {
			logger.warn("WStepResponseDefException: Error trying to delete response "
					+ ex1.getMessage()+" - "+ex1.getCause());
		}
		
	}

	// dml 20120111
	public void editStepResponse(){
		
		WStepResponseDefBL wsrdBL = new WStepResponseDefBL();
		
		try {
			if (stepResponse != null){
			
				wsrdBL.update(stepResponse, this.getCurrentUserId().toString());

				loadObject();
				getResponseList();
				getResponseSize();
				
			}		
			
		} catch (WStepResponseDefException ex1) {
			logger.warn("WStepResponseDefException: Error trying to edit response "
					+ ex1.getMessage()+" - "+ex1.getCause());
		}
		
	}

	private List<SelectItem> loadWTimeUnitForCombo(){
		
		List<SelectItem> ltu=null;
		
		try {
			ltu = UtilsVs
					.castStringPairToSelectitem(
					new WTimeUnitBL().getComboList("Select time unit","") );
		} catch (WTimeUnitException e) {
			logger.warn("Can't retrieve time unit list for combo: "+e.getMessage()+" "+e.getCause());
			e.printStackTrace();
		}

		return ltu;
	}
	
	public String getStrRoleList() {
		
//		System.out.println(this.currentWStepDef.getRolesRelated().toString());
		
		String strRoleList="";
		for ( WStepRole sr: this.currentWStepDef.getRolesRelated()) {
			strRoleList+=(strRoleList!=null && !"".equals(strRoleList)?",":"")+sr.getRole().getId();
		}
		
		System.out.println("--------------->>>>>>>>> strRoleList ------------>>>>>>>>"+strRoleList);
		return strRoleList;
	}
}