package org.beeblos.bpm.web.bean;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.SystemObjectBL;
import org.beeblos.bpm.core.error.SystemObjectException;
import org.beeblos.bpm.core.model.SystemObject;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;


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

 <a4j:jsFunction name="loadRecord" action="#{systemObjectBean.loadRecord}" 
 reRender="name_id, description_id, delete_button, cancel_button, save_button" >
 <a4j:actionparam name="param1" assignTo="#{systemObjectBean.id}"  />
 </a4j:jsFunction>

 * 
 * 2) using a 2 js threaded functions, first of all synchronize the id property and second call load
 * method to load the object, as you can see next:
 * 
 *
 * 
 < a4j:jsFunction name="loadParam" >
 <a4j:actionparam name="param1" assignTo="#{systemObjectBean.id}"  />
 </a4j:jsFunction>

 <a4j:jsFunction name="loadRecord" actionListener="#{systemObjectBean.loadRecord}" 
 reRender="name_id, description_id, delete_button, cancel_button, save_button" >
 </a4j:jsFunction>

 *
 *
 */

public class SystemObjectBean extends CoreManagedBean {

	private static final Log logger = LogFactory.getLog(SystemObjectBean.class.getName());

	private static final long serialVersionUID = -3619314142932182990L;

	private Integer id;

	private SystemObject currentSystemObject;

	private List<SystemObject> systemObjectList;

	private Integer currentRow;

	private String valueBtn;

	private String messageStyle;

	private boolean showAnyMessage;

	public SystemObjectBean() {

		super();
		_init();
		reset();

	}

	// when load the backing bean
	private void _init() {
		systemObjectList = this.loadSystemObjectList(); // load object list
		this.setId(0);
	}

	public void reset() {
		this.showAnyMessage = false;
		initProperties();
	}

	private String initProperties() {
		logger.debug(" initProperties()");

		this.setId(0);
		this.currentSystemObject = new SystemObject();
		this.currentRow = 0;

		systemObjectList = this.loadSystemObjectList();

		this.valueBtn = "Save";

		return null;

	}

	public String save() {
		logger.debug(" save() id:" + this.getId() + " name:" + this.currentSystemObject.getName());

		String returnValue = null; // always returns null because calls here are
									// ajax

		if (this.id != null && this.id != 0) {
			returnValue = update();
		} else {
			returnValue = add();
		}

		return returnValue;
	}

	public String update() {
		logger.debug(" update() :" + this.getId());

		setShowHeaderMessage(false);

		String returnValue = null; // always returns null because calls are ajax

		this.initProperties();

		return returnValue;

	}

	public String add() {
		logger.debug(" add() role name:" + this.currentSystemObject.getName());

		setShowHeaderMessage(false);

		String returnValue = null; // always returns null because calls are ajax

		try {

			Integer newId = new SystemObjectBL().add(this.currentSystemObject,
					1000);


			this.initProperties();

		} catch (SystemObjectException e) {

			e.printStackTrace();

		}

		return returnValue;

	}

	public String delete() {
		logger.debug(" delete() :" + this.getId());

		setShowHeaderMessage(false);

		String returnValue = null; // always returns null because calls are ajax

		try {

			String deletedName = this.currentSystemObject.getName();

			new SystemObjectBL().delete(this.currentSystemObject);

			this.initProperties();

		} catch (SystemObjectException e) {

			e.printStackTrace();

		}

		return returnValue;

	}

	// called from view
	public void loadRecord() {
		logger.debug(" loadRecord() :" + this.getId());

		setShowHeaderMessage(false);
		this.setShowAnyMessage(false);

		if (this.id != null && this.id != 0) {
			try {

				this.currentSystemObject = new SystemObjectBL().getSystemObjectByPK(this.id);

				modifyValueBtn();

			} catch (SystemObjectException e) {

				e.printStackTrace();
			}

		}

	}

	private List<SystemObject> loadSystemObjectList() {

		setShowHeaderMessage(false);

		List<SystemObject> objectList = null;

		try {

			objectList = new SystemObjectBL().getSystemObjects();

		} catch (SystemObjectException e) {

			e.printStackTrace();

		}

		return objectList;
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
		if (id != 0) {
			this.valueBtn = "Update";
		} else {
			this.valueBtn = "Save";
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SystemObject getCurrentSystemObject() {
		return currentSystemObject;
	}

	public void setCurrentSystemObject(SystemObject currentSystemObject) {
		this.currentSystemObject = currentSystemObject;
	}

	public List<SystemObject> getSystemObjectList() {
		return systemObjectList;
	}

	public void setSystemObjectList(List<SystemObject> systemObjectList) {
		this.systemObjectList = systemObjectList;
	}

	public String getMessageStyle() {
		return messageStyle;
	}

	public void setMessageStyle(String messageStyle) {
		this.messageStyle = messageStyle;
	}

	public boolean isShowAnyMessage() {
		return showAnyMessage;
	}

	public void setShowAnyMessage(boolean showAnyMessage) {
		this.showAnyMessage = showAnyMessage;
	}




}
