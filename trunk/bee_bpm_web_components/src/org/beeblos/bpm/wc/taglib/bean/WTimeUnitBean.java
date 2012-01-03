package org.beeblos.bpm.wc.taglib.bean;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WTimeUnitBL;
import org.beeblos.bpm.core.error.WTimeUnitException;
import org.beeblos.bpm.core.model.WTimeUnit;
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
 
 <a4j:jsFunction name="loadRecord" action="#{wTimeUnitBean.loadRecord}" 
				reRender="name_id, description_id, delete_button, cancel_button, save_button" >
	 <a4j:actionparam name="param1" assignTo="#{wTimeUnitBean.id}"  />
</a4j:jsFunction>
 
 * 
 * 2) using a 2 js threaded functions, first of all synchronize the id property and second call load
 * method to load the object, as you can see next:
 * 
 *
 * 
< a4j:jsFunction name="loadParam" >
	<a4j:actionparam name="param1" assignTo="#{wTimeUnitBean.id}"  />
</a4j:jsFunction>

<a4j:jsFunction name="loadRecord" actionListener="#{wTimeUnitBean.loadRecord}" 
	reRender="name_id, description_id, delete_button, cancel_button, save_button" >
</a4j:jsFunction>

*
*
*/


public class WTimeUnitBean extends CoreManagedBean {

	private static final Log logger = LogFactory.getLog(WTimeUnitBean.class.getName());

	private static final long serialVersionUID = -3619314142932182990L;
	
	private Integer currentUserId;
	
	private Integer id;
	
	private WTimeUnit currentWTimeUnit;

	private List<WTimeUnit> timeUnitList;

	private Integer currentRow;
	
	private String valueBtn;
	
	private String messageStyle;

	
	public WTimeUnitBean() {
		
		super();
		_init();
		reset();
		
	}
	
	// when load the backing bean
	private void _init() {
		timeUnitList = this.getwTimeUnitList(); // load object list
		this.setId(0);
	}
	
	public void reset(){
		initProperties();
	}
	
	private String initProperties() {
		logger.debug(" initProperties()");
		
		this.setId(0);
		this.currentWTimeUnit = new WTimeUnit();
		this.currentRow=0;
		timeUnitList = this.getwTimeUnitList(); 
		
		this.valueBtn="Save";
		
		return null;

	}


	public String save() {
		logger.debug(" save() id:" +this.getId()+" name:"+this.currentWTimeUnit.getName() );
		
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
			
			new WTimeUnitBL().update(currentWTimeUnit, this.getCurrentUserId());
			
			String message = setUpdateOkMessage();
			agregarMensaje(message);
			setShowHeaderMessage(true);
			
			reset();
			
		} catch (WTimeUnitException e) {

			messageStyle=errorMessageStyle();
			String message = "WTimeUnitException: Method update in WTimeUnitBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WTimeUnitException" };
			agregarMensaje("202", message, params, FGPException.WARN);
			logger.error(message);

		} catch (Exception e) {

			messageStyle=errorMessageStyle();
			String message = "Exception: Method update in WTimeUnitBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WTimeUnitException" };
			agregarMensaje("202", message, params, FGPException.WARN);
			logger.error(message);

		} 

		return returnValue;

	}


	public String add() {
		logger.debug(" add() time unit name:" +this.currentWTimeUnit.getName() );
		
		setShowHeaderMessage(false);
		messageStyle=normalMessageStyle();
		
		String returnValue = null; // always returns null because calls are ajax
		
		 try {
			
			Integer newId = new WTimeUnitBL().add(this.currentWTimeUnit, this.getCurrentUserId());
			
			String message = setAddOkMessage(newId);
			agregarMensaje(message);
			setShowHeaderMessage(true);
			
			reset();

		 } catch (WTimeUnitException e) {

			messageStyle=errorMessageStyle();
			String message = "WTimeUnitException: Method add in WTimeUnitBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WTimeUnitException" };
			agregarMensaje("202", message, params, FGPException.WARN);
			logger.error(message);

		} catch (Exception e) {

			messageStyle=errorMessageStyle();
			String message = "Exception: Method add in WTimeUnitBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WTimeUnitException" };
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
			
			String deletedTimeUnitName = this.currentWTimeUnit.getName();
			
			new WTimeUnitBL().delete(this.currentWTimeUnit, this.getCurrentUserId());
			
			// set ok message 
			String message = getDeleteOkMessage(deletedTimeUnitName); 
			logger.info(message);
			agregarMensaje(message);
			setShowHeaderMessage(true);

			reset();

		} catch (WTimeUnitException e) {

			messageStyle=errorMessageStyle();
			String message = "WTimeUnitException: Method delete in WTimeUnitBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WTimeUnitException" };
			agregarMensaje("202", message, params, FGPException.WARN);
			logger.error(message);

		} catch (Exception e) {

			messageStyle=errorMessageStyle();
			String message = "Exception: Method delete in WTimeUnitBean: "
					+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WTimeUnitException" };
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
				
				this.currentWTimeUnit = 
						new WTimeUnitBL()
							.getWTimeUnitByPK( this.id, this.getCurrentUserId() );

				modifyValueBtn();
				
			} catch (WTimeUnitException e) {

				messageStyle=errorMessageStyle();
				String message = "WTimeUnitException: Method loadRecord in WTimeUnitBean: "
										+ e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", "WTimeUnitException" };
				agregarMensaje("202", message, params, FGPException.WARN);
				logger.error(message);

			}
		
		}

	}
	
	public List<WTimeUnit> getwTimeUnitList() {
		
		setShowHeaderMessage(false);
		messageStyle=normalMessageStyle();

		List<WTimeUnit> objectList;
		
		try {

			objectList = 
					new WTimeUnitBL()
						.getWTimeUnits(this.getCurrentUserId());
			
		} catch (WTimeUnitException e) {
			
			objectList=null;
			
			logger
				.warn("WTimeUnitException: Error trying to load time unit list "
						+ e.getMessage()+" - "+e.getCause());

		}
		
		return objectList;
	}

	public void setwTimeUnitList(List<WTimeUnit> wTimeUnitList) {
		this.timeUnitList = wTimeUnitList;
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


	public WTimeUnit getCurrentWTimeUnit() {
		return currentWTimeUnit;
	}

	public void setCurrentWTimeUnit(WTimeUnit currentWTimeUnit) {
		this.currentWTimeUnit = currentWTimeUnit;
	}

	public List<WTimeUnit> getTimeUnitList() {
		return timeUnitList;
	}

	public void setTimeUnitList(List<WTimeUnit> timeUnitList) {
		this.timeUnitList = timeUnitList;
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
		return "WTimeUnit id:[ "+this.id+" ] with name:[ "+this.currentWTimeUnit.getName()+" ] was updated correctly";
	}
	
	private String setAddOkMessage(Integer newId) {
		return "WTimeUnit id:["+newId+"] with name:["+this.currentWTimeUnit.getName()+"] was added correctly";
	}
	
	private String getDeleteOkMessage(String name) {
		return "WTimeUnit id:[ "+this.id+" ] with name:[ "+ name +" ] was deleted by user:[ " + this.getCurrentUserId() +" ]";
	}
	
	
}
