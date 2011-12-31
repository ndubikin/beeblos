package org.beeblos.bpm.wc.taglib.bean;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WRoleDefBL;
import org.beeblos.bpm.core.error.WRoleDefException;
import org.beeblos.bpm.core.model.WRoleDef;
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
 
 <a4j:jsFunction name="loadRecord" action="#{wRoleDefBean.loadRecord}" 
				reRender="name_id, description_id, delete_button, cancel_button, save_button" >
	 <a4j:actionparam name="param1" assignTo="#{wRoleDefBean.id}"  />
</a4j:jsFunction>
 
 * 
 * 2) using a 2 js threaded functions, first of all synchronize the id property and second call load
 * method to load the object, as you can see next:
 * 
 *
 * 
< a4j:jsFunction name="loadParam" >
	<a4j:actionparam name="param1" assignTo="#{wRoleDefBean.id}"  />
</a4j:jsFunction>

<a4j:jsFunction name="loadRecord" actionListener="#{wRoleDefBean.loadRecord}" 
	reRender="name_id, description_id, delete_button, cancel_button, save_button" >
</a4j:jsFunction>

*
*
*/


public class WRoleDefBean extends CoreManagedBean {

	private static final Log logger = LogFactory.getLog(UsuarioBean.class);

	private static final long serialVersionUID = -3619314142932182990L;
	
	private Integer currentUserId;
	
	private Integer id;
	
	private WRoleDef currentWRoleDef;

	private List<WRoleDef> roleList;

	private Integer currentRow;
	
	private String valueBtn;

	
	public WRoleDefBean() {
		
		super();
		_init();
		reset();
		
	}
	
	// when load the backing bean
	private void _init() {
		roleList = this.getwRoleDefList(); // load object list
		this.setId(0);
	}
	
	public void reset(){
		initProperties();
	}
	
	private String initProperties() {
		logger.debug(" initProperties()");
		
		this.setId(0);
		this.currentWRoleDef = new WRoleDef();
		this.currentRow=0;
		roleList = this.getwRoleDefList(); 
		
		this.valueBtn="Save";
		
		return null;

	}


	public String save() {
		logger.debug(" save() id:" +this.getId()+" name:"+this.currentWRoleDef.getName() );
		
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
		
		String returnValue = null; // always returns null because calls are ajax

		try {
			
			new WRoleDefBL().update(currentWRoleDef, this.getCurrentUserId());
			
			String message = setUpdateOkMessage();
			agregarMensaje(message);
			setShowHeaderMessage(true);
			
			reset();
			
		} catch (WRoleDefException e) {
			
			String message = "WRoleDefException: Method update in WRoleDefBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WRoleDefException" };
			agregarMensaje("200", message, params, FGPException.WARN);
			logger.error(message);

		} catch (Exception e) {

			String message = "Exception: Method update in WRoleDefBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WRoleDefException" };
			agregarMensaje("200", message, params, FGPException.WARN);
			logger.error(message);

		} 

		return returnValue;

	}


	public String add() {
		logger.debug(" add() role name:" +this.currentWRoleDef.getName() );
		
		setShowHeaderMessage(false);
		
		String returnValue = null; // always returns null because calls are ajax
		
		 try {
			
			Integer newId = new WRoleDefBL().add(this.currentWRoleDef, this.getCurrentUserId());
			
			String message = setAddOkMessage(newId);
			agregarMensaje(message);
			setShowHeaderMessage(true);
			
			reset();

		 } catch (WRoleDefException e) {

			String message = "WRoleDefException: Method add in WRoleDefBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WRoleDefException" };
			agregarMensaje("200", message, params, FGPException.WARN);
			logger.error(message);

		} catch (Exception e) {

			String message = "Exception: Method add in WRoleDefBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WRoleDefException" };
			agregarMensaje("200", message, params, FGPException.WARN);
			logger.error(message);

		} 

		return returnValue;

	}



	public String delete() {
		logger.debug(" delete() :" +this.getId() );
		
		setShowHeaderMessage(false);

		String returnValue = null; // always returns null because calls are ajax

		try {
			
			String deletedRoleName = this.currentWRoleDef.getName();
			
			new WRoleDefBL().delete(this.id, this.getCurrentUserId());
			
			// set ok message 
			String message = getDeleteOkMessage(deletedRoleName); 
			logger.info(message);
			agregarMensaje(message);
			setShowHeaderMessage(true);

			reset();

		} catch (WRoleDefException e) {

			String message = "WRoleDefException: Method delete in WRoleDefBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WRoleDefException" };
			agregarMensaje("200", message, params, FGPException.WARN);
			logger.error(message);

		} catch (Exception e) {

			String message = "Exception: Method delete in WRoleDefBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WRoleDefException" };
			agregarMensaje("200", message, params, FGPException.WARN);
			logger.error(message);

		} 

		return returnValue;

	}

	// called from view
	public void loadRecord() {
		logger.debug(" loadRecord() :" +this.getId() );
		
		setShowHeaderMessage(false);
		
		if (this.id!=null && this.id!=0){
			try {
				
				this.currentWRoleDef = 
						new WRoleDefBL()
							.getWRoleDefByPK( this.id, this.getCurrentUserId() );

				modifyValueBtn();
				
			} catch (WRoleDefException e) {

				String message = "WRoleDefException: Method loadRecord in WRoleDefBean: "
										+ e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", "WRoleDefException" };
				agregarMensaje("200", message, params, FGPException.WARN);
				logger.error(message);

			}
		
		}

	}
	
	public List<WRoleDef> getwRoleDefList() {
		
		setShowHeaderMessage(false);

		List<WRoleDef> objectList;
		
		try {

			objectList = 
					new WRoleDefBL()
						.getWRoleDefs(this.getCurrentUserId());
			
		} catch (WRoleDefException e) {
			
			objectList=null;
			
			logger
				.warn("WRoleDefException: Error trying to load role list "
						+ e.getMessage()+" - "+e.getCause());

		}
		
		return objectList;
	}

	public void setwRoleDefList(List<WRoleDef> wRoleDefList) {
		this.roleList = wRoleDefList;
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


	public WRoleDef getCurrentWRoleDef() {
		return currentWRoleDef;
	}

	public void setCurrentWRoleDef(WRoleDef currentWRoleDef) {
		this.currentWRoleDef = currentWRoleDef;
	}

	public List<WRoleDef> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<WRoleDef> roleList) {
		this.roleList = roleList;
	}



	public void setCurrentUserId(){
		
		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);

		if (cs.getIdUsuario() != null){
			this.currentUserId = cs.getIdUsuario();
		}
		
	}


	private String setUpdateOkMessage() {
		return "WRoleDef id:[ "+this.id+" ] with name:[ "+this.currentWRoleDef.getName()+" ] was updated correctly";
	}
	
	private String setAddOkMessage(Integer newId) {
		return "WRoleDef id:["+newId+"] with name:["+this.currentWRoleDef.getName()+"] was added correctly";
	}
	
	private String getDeleteOkMessage(String name) {
		return "WRoleDef id:[ "+this.id+" ] with name:[ "+ name +" ] was deleted by user:[ " + this.getCurrentUserId() +" ]";
	}
	
	
}
