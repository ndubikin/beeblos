package org.beeblos.bpm.wc.taglib.bean;

import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;

import java.util.TimeZone;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.ObjectBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.error.ObjectException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.model.ObjectM;
import org.beeblos.bpm.core.model.noper.BeeblosAttachment;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.HelperUtil;

/**
 * Empty backing bean 
 *
 * @author nes
 *
 */

public class ComplexObjectManagementBean extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(ComplexObjectManagementBean.class);
	
	private static final String MANAGED_BEAN_NAME = "complexObjectManagementBean";

    private Integer currentUserId;
    
	private ObjectM currentObject; 
	
	private Integer currObjId; // current object managed by this bb

	private TimeZone timeZone;

	private BeeblosAttachment attachment;
	private String documentLink;

	
	
    public static ComplexObjectManagementBean getCurrentInstance() {
        return (ComplexObjectManagementBean) FacesContext.getCurrentInstance().getExternalContext()
            .getRequestMap().get(MANAGED_BEAN_NAME);
    }

	public ComplexObjectManagementBean() {		
		super();
		
		init();  
	}
    
	public void init(){
		super.init_core();
		
		setShowHeaderMessage(false);  
		
		_reset();
	}

	public void _reset() {
	
		this.currObjId=null;
		this.currentObject=null;
		
		attachment = new BeeblosAttachment();

		documentLink=null; 
		
		HelperUtil.recreateBean("documentacionBean", "com.softpoint.taglib.common.DocumentacionBean");

	
	}
	
	
	// SET EMTPY OBJECTS OF CURRENT OBJECT TO NULL TO AVOID PROBLEMS
	// WITH HIBERNATE RELATIONS AND CASCADES
	private void setModel() {
		
		if(currentObject != null){
//			if ( currentObject.getPropertyObjectType1()!=null && 
//					currentObject.getPropertyObjectType1().empty() ) {
//				currentObject.SetPropertyObjectType1( null );
//			}
			
		}
	}
	
	// CREATE NULL PROPERTIES OF OBJECT TYPE TO AVOID PROBLEMS
	// WITH VIEW AND ITS REFERENES TO THESE OBJECTS ...
	private void recoverNullObjects(){
		
		if(currentObject != null){
			
//			if ( currentObject.getPropertyObjectType1 == null) {
//				currentObject.setPropertyObjectType1(new ObjectType1(VACIO));
//			}
			
		}
		
	}
	
	// checks input data before save or update
	private boolean checkInputData() {
		
		boolean result = false;
		
		return result;
	}
	
	private void persistCurrentObject() throws ObjectException {
		ObjectBL womBL = new ObjectBL();
		this.setModel();
		womBL.update(currentObject, getCurrentUserId());
		this.recoverNullObjects();
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
				recoverNullObjects(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>
				result =  null;
			}
		}

		return result;
	}
	

	
	
	public String add() throws ObjectException {		
		logger.debug("ComplexObjectManagementBean: add: objectId:["+this.currObjId+"] ");
		
		setShowHeaderMessage(false);

		String ret = null;
		
		// if object already exists in db then update and return
		if(currentObject!=null 
//				&& currentObject.getId()!=null && currentObject.getId()!=0 
				){ 

			// before update store document in repository ( if exists )
			if (attachment.getDocumentoNombre()!=null && 
					!"".equals(attachment.getDocumentoNombre())) {
				//storeInRepository(); 
			}
			
			return update();
		}
		
		ObjectBL objBL = new ObjectBL();
	
		try {	
			
			setModel(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>

			currObjId = objBL.add(currentObject, this.getCurrentUserId() );
			
			// Manual process to store attachment in the repository ( if attachment exists ...
			if (attachment.getDocumentoNombre()!=null && !"".equals(attachment.getDocumentoNombre())) {
				//storeInRepository();
				update();
			}
			
			recoverNullObjects();
			
			ret="OK";
			setShowHeaderMessage(true);
			
		} catch (ObjectException e) {
			String message = "ComplexObjectManagementBean:Exception: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);

			throw new ObjectException(message);			
			
		} catch (Exception e) {
			
			String message = "ComplexObjectManagementBean:Exception: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			
			throw new ObjectException(message);			
			
		}

		return ret;
	}	
	

	
	public String update() {
		logger.debug("update(): currentId:"
				+ currentObject.getId() + " - "
				+ currentObject.getName());
		
		String ret = null;

		ObjectBL objBL = new ObjectBL();

		try {
			
			setModel();
			
			objBL.update(currentObject, this.getCurrentUserId() );
			
			recoverNullObjects();
			
			setShowHeaderMessage(true);
			ret="OK";

		} catch (ObjectException e) {

			String message = "Error updating object: "
					+ currentObject.getId()
					+ " - "
					+ currentObject.getName()
					+ "\n"
					+ e.getMessage() + "\n" + e.getCause();
			
			super.createWindowMessage(ERROR_MESSAGE, message, e);			
		}
		
		return ret;
	}
	

	// load an Object in currentObject
	public void loadObject(Integer objectId){
		
			this.currObjId=objectId;
			this.loadObject();
			
	}
	
	public void loadObject() {
		logger.debug("loadObject()");
		
		ObjectBL objBL = new ObjectBL();

		try {
			currentObject = 
					objBL
						.getObjectByPK(this.currObjId, this.getCurrentUserId() );
			
			if (currentObject!=null) {
				
				currObjId=currentObject.getId();
				// etc etc
			}

		} catch (ObjectException e) {
			logger.error("Error retrieving object: "
							+ currentObject.getId()
							+ " : "
							+ e.getMessage()
							+ " - "
							+ e.getCause());
		}			
	}
	


		

	public ObjectM getCurrentObject() {
		return currentObject;
	}

	public void setCurrentObject(ObjectM currentObject) {
		this.currentObject = currentObject;
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
	

	
}