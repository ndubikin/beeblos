package org.beeblos.bpm.wc.taglib.bean;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WRoleDefBL;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WUserDef;
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
 
 <a4j:jsFunction name="loadRecord" action="#{wUserDefBean.loadRecord}" 
				reRender="name_id, description_id, delete_button, cancel_button, save_button" >
	 <a4j:actionparam name="param1" assignTo="#{wUserDefBean.id}"  />
</a4j:jsFunction>
 
 * 
 * 2) using a 2 js threaded functions, first of all synchronize the id property and second call load
 * method to load the object, as you can see next:
 * 
 *
 * 
< a4j:jsFunction name="loadParam" >
	<a4j:actionparam name="param1" assignTo="#{wUserDefBean.id}"  />
</a4j:jsFunction>

<a4j:jsFunction name="loadRecord" actionListener="#{wUserDefBean.loadRecord}" 
	reRender="name_id, description_id, delete_button, cancel_button, save_button" >
</a4j:jsFunction>

*
*
*/


public class WUserDefBean extends CoreManagedBean {

	private static final Log logger = LogFactory.getLog(WUserDefBean.class.getName());

	private static final long serialVersionUID = -3619314142932182990L;
	
	private Integer currentUserId;
	
	private Integer id;
	
	private WUserDef currentWUserDef;

	private List<WUserDef> userList;

	private Integer currentRow;
	
	private String valueBtn;
	
	private String messageStyle;
	
	private List<WRoleDef> userRelatedRoles;

	
	public WUserDefBean() {
		
		super();
		_init();
		reset();
		
	}
	
	// when load the backing bean
	private void _init() {
		userList = this.getwUserDefList(); // load object list
		this.setId(0);
	}
	
	public void reset(){
		initProperties();
	}
	
	private String initProperties() {
		logger.debug(" initProperties()");
		
		this.setId(0);
		this.currentWUserDef = new WUserDef();
		this.currentRow=0;
		userList = this.getwUserDefList(); 
		
		this.valueBtn="Save";
		
		return null;

	}


	public String save() {
		logger.debug(" save() id:" +this.getId()+" name:"+this.currentWUserDef.getName() );
		
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
			
			new WUserDefBL().update(currentWUserDef, this.getCurrentUserId());
			
			String message = setUpdateOkMessage();
			agregarMensaje(message);
			setShowHeaderMessage(true);
			
			reset();
			
		} catch (WUserDefException e) {

			messageStyle=errorMessageStyle();
			String message = "WUserDefException: Method update in WUserDefBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WUserDefException" };
			agregarMensaje("201", message, params, FGPException.WARN);
			logger.error(message);

		} catch (Exception e) {

			messageStyle=errorMessageStyle();
			String message = "Exception: Method update in WUserDefBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WUserDefException" };
			agregarMensaje("201", message, params, FGPException.WARN);
			logger.error(message);

		} 

		return returnValue;

	}


	public String add() {
		logger.debug(" add() user name:" +this.currentWUserDef.getName() );
		
		setShowHeaderMessage(false);
		messageStyle=normalMessageStyle();
		
		String returnValue = null; // always returns null because calls are ajax
		
		 try {
			
			Integer newId = new WUserDefBL().add(this.currentWUserDef, this.getCurrentUserId());
			
			String message = setAddOkMessage(newId);
			agregarMensaje(message);
			setShowHeaderMessage(true);
			
			reset();

		 } catch (WUserDefException e) {

			messageStyle=errorMessageStyle();
			String message = "WUserDefException: Method add in WUserDefBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WUserDefException" };
			agregarMensaje("201", message, params, FGPException.WARN);
			logger.error(message);

		} catch (Exception e) {

			messageStyle=errorMessageStyle();
			String message = "Exception: Method add in WUserDefBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WUserDefException" };
			agregarMensaje("201", message, params, FGPException.WARN);
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
			
			String deletedUserName = this.currentWUserDef.getName();
			
			new WUserDefBL().delete(this.id, this.getCurrentUserId());
			
			// set ok message 
			String message = getDeleteOkMessage(deletedUserName); 
			logger.info(message);
			agregarMensaje(message);
			setShowHeaderMessage(true);

			reset();

		} catch (WUserDefException e) {

			messageStyle=errorMessageStyle();
			String message = "WUserDefException: Method delete in WUserDefBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WUserDefException" };
			agregarMensaje("201", message, params, FGPException.WARN);
			logger.error(message);

		} catch (Exception e) {

			messageStyle=errorMessageStyle();
			String message = "Exception: Method delete in WUserDefBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WUserDefException" };
			agregarMensaje("201", message, params, FGPException.WARN);
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
				
				this.currentWUserDef = 
						new WUserDefBL()
							.getWUserDefByPK( this.id, this.getCurrentUserId() );

				modifyValueBtn();
				
			} catch (WUserDefException e) {

				messageStyle=errorMessageStyle();
				String message = "WUserDefException: Method loadRecord in WUserDefBean: "
										+ e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", "WUserDefException" };
				agregarMensaje("201", message, params, FGPException.WARN);
				logger.error(message);

			}
		
		}

	}
	
	public List<WUserDef> getwUserDefList() {
		
		setShowHeaderMessage(false);
		messageStyle=normalMessageStyle();

		List<WUserDef> objectList;
		
		try {

			objectList = 
					new WUserDefBL()
						.getWUserDefs(this.getCurrentUserId());
			
		} catch (WUserDefException e) {
			
			objectList=null;
			
			logger
				.warn("WUserDefException: Error trying to load user list "
						+ e.getMessage()+" - "+e.getCause());

		}
		
		return objectList;
	}

	public void search() {
		
		setShowHeaderMessage(false);
		messageStyle=normalMessageStyle();

		try {

			userList = 
					new WUserDefBL()
						.getWUserDefListByFinder(this.getCurrentWUserDef());
			
		} catch (WUserDefException e) {
			
			userList=null;
			
			logger
				.warn("WUserDefException: Error trying to load user list "
						+ e.getMessage()+" - "+e.getCause());

		}
		
	}

	public void setwUserDefList(List<WUserDef> wUserDefList) {
		this.userList = wUserDefList;
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


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public WUserDef getCurrentWUserDef() {
		return currentWUserDef;
	}

	public void setCurrentWUserDef(WUserDef currentWUserDef) {
		this.currentWUserDef = currentWUserDef;
	}

	public List<WUserDef> getUserList() {
		return userList;
	}

	public void setUserList(List<WUserDef> userList) {
		this.userList = userList;
	}
	
	public String getMessageStyle() {
		return messageStyle;
	}

	public void setMessageStyle(String messageStyle) {
		this.messageStyle = messageStyle;
	}

	public List<WRoleDef> getUserRelatedRoles() {
		return userRelatedRoles;
	}

	public void setUserRelatedRoles(List<WRoleDef> userRelatedRoles) {
		this.userRelatedRoles = userRelatedRoles;
	}

	public void setCurrentUserId(){
		
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);

		if (cs.getIdUsuario() != null){
			this.currentUserId = cs.getIdUsuario();
		}
		
	}

	private String setUpdateOkMessage() {
		return "WUserDef id:[ "+this.id+" ] with name:[ "+this.currentWUserDef.getName()+" ] was updated correctly";
	}
	
	private String setAddOkMessage(Integer newId) {
		return "WUserDef id:["+newId+"] with name:["+this.currentWUserDef.getName()+"] was added correctly";
	}
	
	private String getDeleteOkMessage(String name) {
		return "WUserDef id:[ "+this.id+" ] with name:[ "+ name +" ] was deleted by user:[ " + this.getCurrentUserId() +" ]";
	}
	
	// dml 20120425
	public void loadWRoleDefList(){
		
		if (this.id != null
				&& !this.id.equals(0)){
			
			try {
				
				userRelatedRoles = new WRoleDefBL().getWRoleDefListByUser(this.id);
			
			} catch (WUserDefException e) {

				userRelatedRoles=null;
				
				logger
					.warn("WUserDefException: Error trying to load role list "
							+ e.getMessage()+" - "+e.getCause());

			}
			
			
		}
		
	}
	
	// dml 20120425
	public boolean isUserRelatedRolesEmpty(){
		
		if (userRelatedRoles != null
				&& userRelatedRoles.size() == 0){
			return true;
		} else {
			return false;
		}
		
	}
	
	
}
