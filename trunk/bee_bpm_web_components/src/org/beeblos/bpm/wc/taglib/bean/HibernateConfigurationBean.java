package org.beeblos.bpm.wc.taglib.bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.model.noper.BeeblosAttachment;
import org.beeblos.bpm.core.model.noper.DriverObject;
import org.beeblos.bpm.core.util.DriverObjectUtil;
import org.beeblos.bpm.core.util.HibernateSession;
import org.beeblos.bpm.core.util.HibernateSessionsUtil;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.HelperUtil;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

public class HibernateConfigurationBean extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory
			.getLog(HibernateConfigurationBean.class);

	private static final String MANAGED_BEAN_NAME = "hibernateConfigurationBean";

	private Integer currentUserId;

	private HibernateSession currentHibernateSession;

	private TimeZone timeZone;

	private BeeblosAttachment attachment;
	private String documentLink;
	
	// dml 20120202
	private String sessionName;
	private List<HibernateSession> hibernateSessionList;
	private Integer currentRow;
	private String valueBtn;
	private String messageStyle;
	private String currentDriverName;

	
	public static HibernateConfigurationBean getCurrentInstance() {
		return (HibernateConfigurationBean) FacesContext
				.getCurrentInstance().getExternalContext().getRequestMap()
				.get(MANAGED_BEAN_NAME);
	}

	public HibernateConfigurationBean() {
		super();

		init();
	}

	public void init() {
		super.init();

		setShowHeaderMessage(false);

		_reset();

	}

	public void _reset() {

		attachment = new BeeblosAttachment();

		documentLink = null;
		
		initProperties();

		HelperUtil.recreateBean("documentacionBean",
				"com.softpoint.taglib.common.DocumentacionBean");

	}

	private String initProperties() {
		logger.debug(" initProperties()");
		
		this.setSessionName("");
		this.currentHibernateSession = new HibernateSession();
		this.currentRow=0;
		
		hibernateSessionList = this.gethibernateSessionList(); 
		
		this.valueBtn="Save";
		
		return null;

	}

	public String save() {
		logger.debug(" save() name:" +this.getSessionName() );
		
		String returnValue = null; // always returns null because calls here are ajax
		
		if (currentHibernateSession.getSessionName() != null &&
				!"".equals(currentHibernateSession.getSessionName())){

			if (this.getSessionName() != null && 
					!"".equals(this.getSessionName())) {
				returnValue = update();
			} else {
				returnValue = add();
			}

		} else  {
			
			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "Cannot insert an empty session name ";
			String params[] = { message + ",", "FileNotFoundException" };
			agregarMensaje("209", message, params, FGPException.WARN);
			logger.error(message);
			
		}
		return returnValue;
	}

	public String update() {
		logger.debug(" update() name:" +this.getSessionName() );
		
		setShowHeaderMessage(false);
		setMessageStyle(normalMessageStyle());
		
		String returnValue = null; // always returns null because calls are ajax

		try {
			
			HibernateSessionsUtil.updateConfiguration(currentHibernateSession);
			
			String message = setUpdateOkMessage();
			agregarMensaje(message);
			setShowHeaderMessage(true);
			
			_reset();
			
		} catch (MarshalException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "MarshalException: Method update in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "MarshalException" };
			agregarMensaje("209", message, params, FGPException.WARN);
			logger.error(message);

		} catch (ValidationException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "ValidationException: Method update in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "ValidationException" };
			agregarMensaje("209", message, params, FGPException.WARN);
			logger.error(message);

		} catch (IOException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "IOException: Method update in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "IOException" };
			agregarMensaje("209", message, params, FGPException.WARN);
			logger.error(message);

		} 

		return returnValue;

	}


	public String add() {
		logger.debug(" add() role name:" +this.getSessionName() );
		
		setShowHeaderMessage(false);
		setMessageStyle(normalMessageStyle());
		
		String returnValue = null; // always returns null because calls are ajax
		
		 try {
			
			HibernateSessionsUtil.addConfiguration(currentHibernateSession);
			
			String message = setAddOkMessage(sessionName);
			agregarMensaje(message);
			setShowHeaderMessage(true);
			
			_reset();

			} catch (MarshalException e) {

				setMessageStyle(errorMessageStyle());
				setShowHeaderMessage(true);
				String message = "MarshalException: Method add in HibernateConfigurationBean: "
									+ e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", "MarshalException" };
				agregarMensaje("209", message, params, FGPException.WARN);
				logger.error(message);

			} catch (ValidationException e) {

				setMessageStyle(errorMessageStyle());
				setShowHeaderMessage(true);
				String message = "ValidationException: Method add in HibernateConfigurationBean: "
									+ e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", "ValidationException" };
				agregarMensaje("209", message, params, FGPException.WARN);
				logger.error(message);

			} catch (IOException e) {

				setMessageStyle(errorMessageStyle());
				setShowHeaderMessage(true);
				String message = "IOException: Method add in HibernateConfigurationBean: "
									+ e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", "IOException" };
				agregarMensaje("209", message, params, FGPException.WARN);
				logger.error(message);

			} 

		return returnValue;

	}



	public String delete() {
		logger.debug(" delete() :" +this.getSessionName() );
		
		setShowHeaderMessage(false);
		setMessageStyle(normalMessageStyle());

		String returnValue = null; // always returns null because calls are ajax

		try {
			
			String deletedHibernateSessionName = this.currentHibernateSession.getSessionName();
			
			HibernateSessionsUtil.deleteConfiguration(currentHibernateSession);
			
			// set ok message 
			String message = getDeleteOkMessage(deletedHibernateSessionName); 
			logger.info(message);
			agregarMensaje(message);
			setShowHeaderMessage(true);

			_reset();

		} catch (MarshalException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "MarshalException: Method delete in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "MarshalException" };
			agregarMensaje("209", message, params, FGPException.WARN);
			logger.error(message);

		} catch (ValidationException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "ValidationException: Method delete in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "ValidationException" };
			agregarMensaje("209", message, params, FGPException.WARN);
			logger.error(message);

		} catch (IOException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "IOException: Method delete in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "IOException" };
			agregarMensaje("209", message, params, FGPException.WARN);
			logger.error(message);

		} 

		return returnValue;

	}

	// called from view
	public void loadRecord() {
		logger.debug(" loadRecord() :" +this.getSessionName() );
		
		setShowHeaderMessage(false);
		setMessageStyle(normalMessageStyle());
		
		if (this.getSessionName()!=null && !"".equals(this.getSessionName())){
				
			try{
				
				currentHibernateSession = HibernateSessionsUtil.getConfiguration(sessionName);

				modifyValueBtn();
				
			} catch (MarshalException e) {

				setMessageStyle(errorMessageStyle());
				setShowHeaderMessage(true);
				String message = "MarshalException: Method loadRecord in HibernateConfigurationBean: "
									+ e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", "MarshalException" };
				agregarMensaje("209", message, params, FGPException.WARN);
				logger.error(message);

			} catch (ValidationException e) {

				setMessageStyle(errorMessageStyle());
				setShowHeaderMessage(true);
				String message = "ValidationException: Method loadRecord in HibernateConfigurationBean: "
									+ e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", "ValidationException" };
				agregarMensaje("209", message, params, FGPException.WARN);
				logger.error(message);

			} catch (FileNotFoundException e) {

				setMessageStyle(errorMessageStyle());
				setShowHeaderMessage(true);
				String message = "IOException: Method loadRecord in HibernateConfigurationBean: "
									+ e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", "FileNotFoundException" };
				agregarMensaje("209", message, params, FGPException.WARN);
				logger.error(message);

			} 

		}

	}
	
	@SuppressWarnings("unchecked")
	public List<HibernateSession> gethibernateSessionList() {
		
		setShowHeaderMessage(false);
		setMessageStyle(normalMessageStyle());

		List<HibernateSession> objectList = null;
		
		try{
			
			objectList = (List<HibernateSession>) (HibernateSessionsUtil.getConfigurations());

		} catch (MarshalException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "MarshalException: Method gethibernateSessionList in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "MarshalException" };
			agregarMensaje("209", message, params, FGPException.WARN);
			logger.error(message);

		} catch (ValidationException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "ValidationException: Method gethibernateSessionList in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "ValidationException" };
			agregarMensaje("209", message, params, FGPException.WARN);
			logger.error(message);

		} catch (FileNotFoundException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "IOException: Method gethibernateSessionList in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "FileNotFoundException" };
			agregarMensaje("209", message, params, FGPException.WARN);
			logger.error(message);

		} 
		
		return objectList;

	}



	public void setHibernateSessionList(List<HibernateSession> hibernateSessionList) {
		this.hibernateSessionList = hibernateSessionList;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public HibernateSession getCurrentHibernateSession() {
		return currentHibernateSession;
	}

	public void setCurrentHibernateSession(
			HibernateSession currentHibernateSession) {
		this.currentHibernateSession = currentHibernateSession;
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
		if (!"".equals(sessionName)){
			this.valueBtn = "Update" ;
		} else {
			this.valueBtn = "Save";
		}
	}

	public String getMessageStyle() {
		return messageStyle;
	}

	public void setMessageStyle(String messageStyle) {
		this.messageStyle = messageStyle;
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
		if (currentUserId == null) {
			ContextoSeguridad cs = (ContextoSeguridad) getSession()
					.getAttribute(SECURITY_CONTEXT);
			if (cs != null)
				currentUserId = cs.getIdUsuario();
		}
		return currentUserId;
	}

	public TimeZone getTimeZone() {
		// Si se pone GMT+1 pone mal el dia
		return java.util.TimeZone.getDefault();
	}

	public String getCurrentDriverName() {
		return currentDriverName;
	}

	public void setCurrentDriverName(String currentDriverName) {
		this.currentDriverName = currentDriverName;
	}

	public List<HibernateSession> getHibernateSessionList() {
		return hibernateSessionList;
	}

	public void applyNewConfiguration() {

		if (currentHibernateSession != null) {

			if (currentHibernateSession.getSessionName() != null &&
					!"".equals(currentHibernateSession.getSessionName())){

				try {
					HibernateUtil.getNewSession(currentHibernateSession);

					ContextoSeguridad cs = (ContextoSeguridad) getSession()
							.getAttribute(SECURITY_CONTEXT);
					
					cs.setHibernateParameters(currentHibernateSession);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {

				setMessageStyle(errorMessageStyle());
				setShowHeaderMessage(true);
				String message = "Cannot insert an empty session name ";
				String params[] = { message + ",", "FileNotFoundException" };
				agregarMensaje("209", message, params, FGPException.WARN);
				logger.error(message);

			}
					
		}

	}

	public void setDefaultConfiguration() {

		new HibernateSession();
		this.currentHibernateSession = HibernateSession.loadDefaultHibernateSessionParameters();

	}
	
	public void checkConfiguration(){
		
		try {
			
			if(HibernateUtil.checkJDBCConnection(currentHibernateSession)) {
				
				String message = setGoodConfiguration();
				agregarMensaje(message);
				setShowHeaderMessage(true);

			} else {
				
				String message = setWrongConfiguration();
				agregarMensaje(message);
				setShowHeaderMessage(true);
				
			}
			
		} catch (ClassNotFoundException e) {

			setMessageStyle(errorMessageStyle());
			String message = "ClassNotFoundException: Method checkConfiguration in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "ClassNotFoundException" };
			agregarMensaje("209", message, params, FGPException.WARN);
			logger.error(message);
			setShowHeaderMessage(true);

		} catch (SQLException e) {

			setMessageStyle(errorMessageStyle());
			String message = "SQLException: Method checkConfiguration in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "SQLException" };
			agregarMensaje("209", message, params, FGPException.WARN);
			logger.error(message);
			setShowHeaderMessage(true);

		}
		
	}
	
	private String setUpdateOkMessage() {
		return "HibernateSession name:[ "+this.sessionName+" ] was updated correctly";
	}
	
	private String setAddOkMessage(String name) {
		return "HibernateSession name:[ "+name+" ] was added correctly";
	}
	
	private String getDeleteOkMessage(String name) {
		return "HibernateSession name:[ "+name+" ] was deleted by user:[ " + this.getCurrentUserId() +" ]";
	}
	
	private String setGoodConfiguration() {
		return "This is a GOOD session configuration. The connection with the database is correct. ";
	}
	
	private String setWrongConfiguration() {
		return "This is a WRONG session configuration. It cannot connect with the database";
	}
	
	public ArrayList<DriverObject> driverNameAutocomplete(
			Object input) {

		String driverNameInput = (String) input;
		ArrayList<DriverObject> result = null;

		result = (ArrayList<DriverObject>) DriverObjectUtil.searchDriverArray(driverNameInput);

		return result;
		
	}
	
}
