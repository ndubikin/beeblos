package org.beeblos.bpm.wc.taglib.bean;

import java.util.TimeZone;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.ObjectBL;
import org.beeblos.bpm.core.error.ObjectException;
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
	
	private Integer currentObjectId; // current object managed by this bb

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
		super.init();
		
		setShowHeaderMessage(false);  
		
		_reset();
	}

	public void _reset() {
	
		this.currentObjectId=null;
		this.currentObject=null;
		
		attachment = new BeeblosAttachment();

		documentLink=null; 
		
		HelperUtil.recreateBean("documentacionBean", "com.softpoint.taglib.common.DocumentacionBean");

	
	}



	// load an Object in currentObject
	public void loadObject(Integer objectId){
		
		
//		ObjectBL obl = new ObjectBL();
//		
//		
//		try {			
//	
//			currentObject = obl.getObjectByPk(objectId);
//			
//			if( currentObject != null ) {
//				
//				//xxxxx
//				
//			}
//
//			createNullObjectTypeProperties();  <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>
//			
//		} catch (ObjectException ex1) {
//
//			logger.error("Ocurrio Un Error al tratar de recuperar la factura: " + idFacprov + " : "
//			+ ex1.getMessage());
//		}				
			
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
	private void createNullObjectTypeProperties(){
		
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
		logger.debug("ComplexObjectManagementBean: add: objectId:["+this.currentObjectId+"] ");
		
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

			currentObjectId = objBL.add(currentObject, this.getCurrentUserId() );
			
			// Manual process to store attachment in the repository ( if attachment exists ...
			if (attachment.getDocumentoNombre()!=null && !"".equals(attachment.getDocumentoNombre())) {
				//storeInRepository();
				update();
			}
			
			createNullObjectTypeProperties();
			
			ret="OK";
			setShowHeaderMessage(true);
			
		} catch (ObjectException ex1) {

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
				+ currentObject.getId() + " - "
				+ currentObject.getName());
		
		String ret = null;

		ObjectBL objBL = new ObjectBL();

		try {
			
			setModel();
			
			objBL.update(currentObject, this.getCurrentUserId() );
			
			createNullObjectTypeProperties();
			
			setShowHeaderMessage(true);
			ret="OK";

		} catch (ObjectException ex1) {

			String message = "Error updating object: "
					+ currentObject.getId()
					+ " - "
					+ currentObject.getName()
					+ "\n"
					+ ex1.getMessage() + "\n" + ex1.getCause();
			
			logger.error(message);
			
			String params[] = {message + ",", ".Please confirm input values." };				
			agregarMensaje("1001",message,params,FGPException.ERROR);	

			logger.error(message);
			
		}
		
		return ret;
	}
	

	private void loadObject() {
		logger.debug("loadObject()");
		
		ObjectBL objBL = new ObjectBL();

		try {
			ObjectM obj = 
					objBL
						.getObjectByPK(currentObject.getId(), this.getCurrentUserId() );
			
			if (obj!=null) {
				
				currentObjectId=obj.getId();
				// etc etc
			}

		} catch (ObjectException ex1) {
			logger.error("Error retrieving object: "
							+ currentObject.getId()
							+ " : "
							+ ex1.getMessage()
							+ " - "
							+ ex1.getCause());
		}			
	}
	


		

	public ObjectM getCurrentObject() {
		return currentObject;
	}

	public void setCurrentObject(ObjectM currentObject) {
		this.currentObject = currentObject;
	}

	public Integer getCurrentObjectId() {
		return currentObjectId;
	}

	public void setCurrentObjectId(Integer currentObjectId) {
		this.currentObjectId = currentObjectId;
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