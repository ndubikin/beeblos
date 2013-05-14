package org.beeblos.bpm.wc.taglib.bean;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.EnvTypeBL;
import org.beeblos.bpm.core.error.EnvTypeException;
import org.beeblos.bpm.core.model.EnvType;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;



/*
 * 
 * NOTE FROM VIEW ( XHTML ) AND AJAX CALLS:
 * 
 * In the grid we fire an ajax operation with the user clicks a row. The onRowClic method provided 
 * for RichFaces 3.3.x rich:dataTable is used to call a a4j:jsFunction ( or a js traditional function)
 * and the id of object clicked is sent.
 * 
 * The js function is defined with Richfaces a4j:jsFunction and a a4j:actionparam sycnhronize the
 * id with the backing bean id property to load de objet to work with ( with the actionListener
 * provided by a4j:jsFunction )
 * 
 * The behavior observed with the view and the controller ( backing bean ) is not always convenient
 * because the richfaces components first fire the actionListener and then in second place syncrhonize
 * the id indicated in the a4j:actionparam, so the id property is set after the object is loaded
 * 
 * Two solutions were encountered:
 * 
 * 1) using "action" property instead of actionparam forces synchronization of all jsf tree and the id
 * is set before the load occurs
 * 
 
 <a4j:jsFunction name="loadRecord" action="#{envTypeBean.loadRecord}" 
				reRender="name_id, description_id, delete_button, cancel_button, save_button" >
	 <a4j:actionparam name="param1" assignTo="#{envTypeBean.id}"  />
</a4j:jsFunction>
 
 * 
 * 2) using a 2 js threaded functions, first of all synchronize the id property and second call load
 * method to load the object, as you can see next:
 * 
 *
 * 
< a4j:jsFunction name="loadParam" >
	<a4j:actionparam name="param1" assignTo="#{envTypeBean.id}"  />
</a4j:jsFunction>

<a4j:jsFunction name="loadRecord" actionListener="#{envTypeBean.loadRecord}" 
	reRender="name_id, description_id, delete_button, cancel_button, save_button" >
</a4j:jsFunction>

*
*
*/


public class EnvTypeBean extends CoreManagedBean {

	private static final Log logger = LogFactory.getLog(EnvTypeBean.class.getName());

	private static final long serialVersionUID = -3619314142932182990L;
	
	private Integer currentUserId;
	
	private Short id;
	
	private EnvType currentEnvType;

	private List<EnvType> envTypeList;

	private Integer currentRow;
	
	private String valueBtn;
	
	private String messageStyle;

	
	public EnvTypeBean() {
		
		super();
		_init();
		reset();
		
	}
	
	// when load the backing bean
	private void _init() {
		envTypeList = this.getEnvTypeList(); // load object list
		this.id = 0;
	}
	
	public void reset(){
		initProperties();
	}
	
	private String initProperties() {
		logger.debug(" initProperties()");
		
		this.id = 0;
		this.currentEnvType = new EnvType();
		this.currentRow=0;
		envTypeList = this.getEnvTypeList(); 
		
		this.valueBtn="Save";
		
		return null;

	}


	public String save() {
		logger.debug(" save() id:" +this.getId()+" name:"+this.currentEnvType.getName() );
		
		String returnValue = null; // always returns null because calls here are ajax
		
		if (this.id!=null && this.id!=0) {
			returnValue = update();
		} else {
			returnValue = add();
		}
		
		return returnValue;
	}

	public String update() {
		logger.debug(" update() :" +this.getId() );
		
		setShowHeaderMessage(false);
		messageStyle=normalMessageStyle();
		
		String returnValue = null; // always returns null because calls are ajax

		try {
			
			new EnvTypeBL().update(currentEnvType, this.getCurrentUserId());
			
			String message = setUpdateOkMessage();
			agregarMensaje(message);
			setShowHeaderMessage(true);
			
			reset();
			
		} catch (EnvTypeException e) {

			messageStyle=errorMessageStyle();
			String message = "EnvTypeException: Method update in EnvTypeBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "EnvTypeException" };
			agregarMensaje("202", message, params, FGPException.WARN);
			logger.error(message);

		} catch (Exception e) {

			messageStyle=errorMessageStyle();
			String message = "Exception: Method update in EnvTypeBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "EnvTypeException" };
			agregarMensaje("202", message, params, FGPException.WARN);
			logger.error(message);

		} 

		return returnValue;

	}


	public String add() {
		logger.debug(" add() time unit name:" +this.currentEnvType.getName() );
		
		setShowHeaderMessage(false);
		messageStyle=normalMessageStyle();
		
		String returnValue = null; // always returns null because calls are ajax
		
		 try {
			
			Integer newId = new EnvTypeBL().add(this.currentEnvType, this.getCurrentUserId());
			
			String message = setAddOkMessage(newId);
			agregarMensaje(message);
			setShowHeaderMessage(true);
			
			reset();

		 } catch (EnvTypeException e) {

			messageStyle=errorMessageStyle();
			String message = "EnvTypeException: Method add in EnvTypeBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "EnvTypeException" };
			agregarMensaje("202", message, params, FGPException.WARN);
			logger.error(message);

		} catch (Exception e) {

			messageStyle=errorMessageStyle();
			String message = "Exception: Method add in EnvTypeBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "EnvTypeException" };
			agregarMensaje("202", message, params, FGPException.WARN);
			logger.error(message);

		} 

		return returnValue;

	}



	public String delete() {
		logger.debug(" delete() :" +this.getId() );
		
		setShowHeaderMessage(false);
		messageStyle=normalMessageStyle();

		String returnValue = null; // always returns null because calls are ajax

		try {
			
			String deletedTimeUnitName = this.currentEnvType.getName();
			
			new EnvTypeBL().delete(this.currentEnvType, this.getCurrentUserId());
			
			// set ok message 
			String message = getDeleteOkMessage(deletedTimeUnitName); 
			logger.info(message);
			agregarMensaje(message);
			setShowHeaderMessage(true);

			reset();

		} catch (EnvTypeException e) {

			messageStyle=errorMessageStyle();
			String message = "EnvTypeException: Method delete in EnvTypeBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "EnvTypeException" };
			agregarMensaje("202", message, params, FGPException.WARN);
			logger.error(message);

		} catch (Exception e) {

			messageStyle=errorMessageStyle();
			String message = "Exception: Method delete in EnvTypeBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "EnvTypeException" };
			agregarMensaje("202", message, params, FGPException.WARN);
			logger.error(message);

		} 

		return returnValue;

	}

	// called from view
	public void loadRecord() {
		logger.debug(" loadRecord() :" +this.getId() );
		
		setShowHeaderMessage(false);
		messageStyle=normalMessageStyle();
		
		if (this.id!=null && this.id!=0){
			try {
				
				this.currentEnvType = 
						new EnvTypeBL()
							.getEnvTypeByPK( this.id, this.getCurrentUserId() );

				modifyValueBtn();
				
			} catch (EnvTypeException e) {

				messageStyle=errorMessageStyle();
				String message = "EnvTypeException: Method loadRecord in EnvTypeBean: "
										+ e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", "EnvTypeException" };
				agregarMensaje("202", message, params, FGPException.WARN);
				logger.error(message);

			}
		
		}

	}
	
	public List<EnvType> getEnvTypeList() {
		
		setShowHeaderMessage(false);
		messageStyle=normalMessageStyle();

		List<EnvType> objectList;
		
		try {

			objectList = 
					new EnvTypeBL()
						.getEnvTypes(this.getCurrentUserId());
			
		} catch (EnvTypeException e) {
			
			objectList=null;
			
			logger
				.warn("EnvTypeException: Error trying to load time unit list "
						+ e.getMessage()+" - "+e.getCause());

		}
		
		return objectList;
	}

	public void setEnvTypeList(List<EnvType> envTypeList) {
		this.envTypeList = envTypeList;
	}

	public void setCurrentRow(Integer currentRow) {
		this.currentRow = currentRow;
	}

	public Integer getCurrentRow() {
		return currentRow;
	}


	public void setValueBtn(String valueBtn) {
		this.valueBtn = valueBtn;
	}

	public String getValueBtn() {
		return valueBtn;
	}
	
	public void modifyValueBtn() {
		if (id != 0){
			this.valueBtn = "Update" ;
		} else {
			this.valueBtn = "Save";
		}
	}

	public Integer getCurrentUserId() {
		
		if ( this.currentUserId==null || this.currentUserId==0 ) {
			this.setCurrentUserId();
		}
		
		return currentUserId;
	}


	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}


	public EnvType getCurrentEnvType() {
		return currentEnvType;
	}

	public void setCurrentEnvType(EnvType currentEnvType) {
		this.currentEnvType = currentEnvType;
	}

	public List<EnvType> getTimeUnitList() {
		return envTypeList;
	}

	public void setTimeUnitList(List<EnvType> envTypeList) {
		this.envTypeList = envTypeList;
	}
	
	public String getMessageStyle() {
		return messageStyle;
	}

	public void setMessageStyle(String messageStyle) {
		this.messageStyle = messageStyle;
	}

	public void setCurrentUserId(){
		
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);

		if (cs.getIdUsuario() != null){
			this.currentUserId = cs.getIdUsuario();
		}
		
	}

	private String setUpdateOkMessage() {
		return "EnvType id:[ "+this.id+" ] with name:[ "+this.currentEnvType.getName()+" ] was updated correctly";
	}
	
	private String setAddOkMessage(Integer newId) {
		return "EnvType id:["+newId+"] with name:["+this.currentEnvType.getName()+"] was added correctly";
	}
	
	private String getDeleteOkMessage(String name) {
		return "EnvType id:[ "+this.id+" ] with name:[ "+ name +" ] was deleted by user:[ " + this.getCurrentUserId() +" ]";
	}
	
	
}
