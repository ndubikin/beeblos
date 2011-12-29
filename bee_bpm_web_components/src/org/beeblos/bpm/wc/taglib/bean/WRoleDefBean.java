package org.beeblos.bpm.wc.taglib.bean;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WRoleDefBL;
import org.beeblos.bpm.core.error.WRoleDefException;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;

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
	
	// when load backing bean
	private void _init() {
		roleList = this.getwRoleDefList(); // load object list
		this.setId(0);
	}
	
	public void reset(){
		initProperties();
	}
	
	private String initProperties() {

		this.setId(0);
		this.currentWRoleDef = new WRoleDef();
		this.currentRow=0;
		roleList = this.getwRoleDefList(); 
		
		this.valueBtn="Save";
		
		return null;

	}


	public String save() {
		logger.debug(" save() id:" +this.getId()+" name:"+this.currentWRoleDef.getName() );
		
		String returnValue = null; // always returns null because calls are ajax
		
		if (this.id!=null && this.id!=0) {
			returnValue = update();
		} else {
			returnValue = add();
		}
		reset();
		
		return returnValue;
	}

	public String update(){
		logger.debug(" update() :" +this.getId() );
		
		String returnValue = null; // always returns null because calls are ajax

		try {
		
			new WRoleDefBL().update(currentWRoleDef, this.getCurrentUserId());
			
			reset();

		} catch (WRoleDefException e) {

			logger.error("WRoleDefException: Error trying to update a Role: "
					+ e.getMessage()+" - "+e.getCause());

		} catch (Exception e) {

			logger.error("Exception: Error trying to update a Role: "
					+ e.getMessage()+" - "+e.getCause()+" - "+e.getClass());

		} 

		return returnValue;

	}

	public String add() {
		logger.debug(" add() role name:" +this.currentWRoleDef.getName() );
		
		String returnValue = null; // always returns null because calls are ajax
		
		 try {
			
			new WRoleDefBL().add(this.currentWRoleDef, this.getCurrentUserId());

			reset();

		} catch (WRoleDefException e) {

			logger.error("WRoleDefException: Error adding a Role: "
					+ e.getMessage()+" - "+e.getCause());

		} catch (Exception e) {

			logger.error("Exception: Error adding a Role: "
					+ e.getMessage()+" - "+e.getCause()+" - "+e.getClass());

		} 

		return returnValue;

	}

	public String delete() {
		logger.debug(" delete() :" +this.getId() );

		String returnValue = null; // always returns null because calls are ajax

		try {
			
			new WRoleDefBL().delete(this.id, this.getCurrentUserId());
			
			logger.info("WRoleDef id:["+this.id+"] deleted by user:[" + this.getCurrentUserId() +"]");

			reset();

		} catch (WRoleDefException e) {

			logger.error("WRoleDefException: Error trying to delete a Role: "
					+ e.getMessage()+" - "+e.getCause());

		} catch (Exception e) {

			logger.error("Exception: Error trying to delete a Role: "
					+ e.getMessage()+" - "+e.getCause()+" - "+e.getClass());

		} 

		return returnValue;

	}
	
	public List<WRoleDef> getwRoleDefList() {
		
		List<WRoleDef> objectList;
		
		try {

			objectList = 
					new WRoleDefBL()
						.getWRoleDefs(this.getCurrentUserId());
			
		} catch (WRoleDefException e) {
			
			objectList=null;
			
			logger
				.warn("Error trying to load role list "
					+ e.getMessage()+" - "+e.getCause());
		}
		return objectList;
	}


	public void loadRecord() {
		logger.debug(" loadRecord() :" +this.getId() );
		
		if (this.id!=null && this.id!=0){
			try {
				
				this.currentWRoleDef = 
						new WRoleDefBL()
							.getWRoleDefByPK( this.id, this.getCurrentUserId() );

				modifyValueBtn();
				
			} catch (WRoleDefException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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

	
}
