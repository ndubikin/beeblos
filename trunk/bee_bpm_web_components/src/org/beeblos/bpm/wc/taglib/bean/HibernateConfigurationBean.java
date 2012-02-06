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
import org.beeblos.bpm.core.util.HibernateConfigurationParameters;
import org.beeblos.bpm.core.util.HibernateConfigurationUtil;
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

	private HibernateConfigurationParameters currentHibernateConfigurationParameters;
	private List<HibernateConfigurationParameters> hibernateConfigurationParametersList;
	
	private TimeZone timeZone;

	private BeeblosAttachment attachment;
	private String documentLink;
	
	// dml 20120202
	private String sessionName;
	private Integer currentRow;
	private String valueBtn;
	private String messageStyle;
	private String currentDriverName;
	
	// dml 20120203
	private boolean recordIsLoaded;

	
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
		this.currentHibernateConfigurationParameters = new HibernateConfigurationParameters();
		this.currentRow=0;
		
		this.hibernateConfigurationParametersList = new ArrayList<HibernateConfigurationParameters>();
		this.loadHibernateConfigurationParametersList(); 
		
		this.recordIsLoaded = false;
		
		this.valueBtn="Save";
		
		return null;

	}

	public String save() {
		logger.debug(" save() name:" +this.getSessionName() );
		
		String returnValue = null; // always returns null because calls here are ajax
		
		if (currentHibernateConfigurationParameters.getSessionName() != null &&
				!"".equals(currentHibernateConfigurationParameters.getSessionName())){

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
			agregarMensaje(message);
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
			
			HibernateConfigurationParameters removeElement = null;
			
			for (HibernateConfigurationParameters hs: hibernateConfigurationParametersList){
				
				if (hs.getSessionName().equals(currentHibernateConfigurationParameters.getSessionName())){		
					removeElement = hs;
					break;	
				}
				
			}
			
			if (removeElement != null) {
				
				this.hibernateConfigurationParametersList.remove(removeElement);
				this.hibernateConfigurationParametersList.add(this.currentHibernateConfigurationParameters);
				HibernateConfigurationUtil.persistConfigurationList(this.hibernateConfigurationParametersList);				
				
			} else {
				
				setMessageStyle(errorMessageStyle());
				setShowHeaderMessage(true);
				String message = "Element to update cannot be found: Method update in HibernateConfigurationBean: ";
				agregarMensaje(message);
				logger.error(message);				
				
			}
			
			
			String message = setUpdateOkMessage();
			agregarMensaje(message);
			setShowHeaderMessage(true);
			
			_reset();
			
		} catch (MarshalException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "MarshalException: Method update in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			agregarMensaje(message);
			logger.error(message);

		} catch (ValidationException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "ValidationException: Method update in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			agregarMensaje(message);
			logger.error(message);

		} catch (IOException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "IOException: Method update in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			agregarMensaje(message);
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
			
			 this.hibernateConfigurationParametersList.add(currentHibernateConfigurationParameters);
			 HibernateConfigurationUtil.persistConfigurationList(this.hibernateConfigurationParametersList);
			
			String message = setAddOkMessage(sessionName);
			agregarMensaje(message);
			setShowHeaderMessage(true);
			
			_reset();

			} catch (MarshalException e) {

				setMessageStyle(errorMessageStyle());
				setShowHeaderMessage(true);
				String message = "MarshalException: Method add in HibernateConfigurationBean: "
									+ e.getMessage() + " - " + e.getCause();
				agregarMensaje(message);
				logger.error(message);

			} catch (ValidationException e) {

				setMessageStyle(errorMessageStyle());
				setShowHeaderMessage(true);
				String message = "ValidationException: Method add in HibernateConfigurationBean: "
									+ e.getMessage() + " - " + e.getCause();
				agregarMensaje(message);
				logger.error(message);

			} catch (IOException e) {

				setMessageStyle(errorMessageStyle());
				setShowHeaderMessage(true);
				String message = "IOException: Method add in HibernateConfigurationBean: "
									+ e.getMessage() + " - " + e.getCause();
				agregarMensaje(message);
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
			 
			if (currentHibernateConfigurationParameters.isDefaultConfiguration()){
			
				setMessageStyle(errorMessageStyle());
				setShowHeaderMessage(true);
				String message = " It is not possible to delete the default configuration. ";
				agregarMensaje(message);
				logger.error(message);				
				
			} else {

				this.hibernateConfigurationParametersList.remove(this.currentHibernateConfigurationParameters);
			
				HibernateConfigurationUtil.persistConfigurationList(hibernateConfigurationParametersList);
				
				// set ok message 
				String message = getDeleteOkMessage(currentHibernateConfigurationParameters.getSessionName()); 
				logger.info(message);
				agregarMensaje(message);
				setShowHeaderMessage(true);
	
				_reset();

			}
			
		} catch (MarshalException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "MarshalException: Method delete in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			agregarMensaje(message);
			logger.error(message);

		} catch (ValidationException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "ValidationException: Method delete in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			agregarMensaje(message);
			logger.error(message);

		} catch (IOException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "IOException: Method delete in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			agregarMensaje(message);
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
				
				currentHibernateConfigurationParameters = HibernateConfigurationUtil.getConfiguration(sessionName);

				recordIsLoaded = true;
				
				modifyValueBtn();
				
			} catch (MarshalException e) {

				setMessageStyle(errorMessageStyle());
				setShowHeaderMessage(true);
				String message = "MarshalException: Method loadRecord in HibernateConfigurationBean: "
									+ e.getMessage() + " - " + e.getCause();
				agregarMensaje(message);
				logger.error(message);

			} catch (ValidationException e) {

				setMessageStyle(errorMessageStyle());
				setShowHeaderMessage(true);
				String message = "ValidationException: Method loadRecord in HibernateConfigurationBean: "
									+ e.getMessage() + " - " + e.getCause();
				agregarMensaje(message);
				logger.error(message);

			} catch (FileNotFoundException e) {

				setMessageStyle(errorMessageStyle());
				setShowHeaderMessage(true);
				String message = "FileNotFoundException: Method loadRecord in HibernateConfigurationBean: "
									+ e.getMessage() + " - " + e.getCause();
				agregarMensaje(message);
				logger.error(message);

			} 

		}

	}
	

	public void loadHibernateConfigurationParametersList() {
		
		setShowHeaderMessage(false);
		setMessageStyle(normalMessageStyle());

		try{
			
			this.hibernateConfigurationParametersList = 
					(List<HibernateConfigurationParameters>) 
						(HibernateConfigurationUtil.getConfigurationList());

		} catch (MarshalException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "MarshalException: Method loadHibernateConfigurationParametersList in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			agregarMensaje(message);
			logger.error(message);

		} catch (ValidationException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "ValidationException: Method loadHibernateConfigurationParametersList in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			agregarMensaje(message);
			logger.error(message);

		} catch (FileNotFoundException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "IOException: Method loadHibernateConfigurationParametersList in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			agregarMensaje(message);
			logger.error(message);

		} 
		
	}



	public void setHibernateConfigurationParametersList(List<HibernateConfigurationParameters> hibernateConfigurationParametersList) {
		this.hibernateConfigurationParametersList = hibernateConfigurationParametersList;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public HibernateConfigurationParameters getCurrentHibernateConfigurationParameters() {
		return currentHibernateConfigurationParameters;
	}

	public void setCurrentHibernateConfigurationParameters(
			HibernateConfigurationParameters currentHibernateConfigurationParameters) {
		this.currentHibernateConfigurationParameters = currentHibernateConfigurationParameters;
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

	public boolean isRecordIsLoaded() {
		return recordIsLoaded;
	}

	public void setRecordIsLoaded(boolean recordIsLoaded) {
		this.recordIsLoaded = recordIsLoaded;
	}

	public List<HibernateConfigurationParameters> getHibernateConfigurationParametersList() {
		return hibernateConfigurationParametersList;
	}

	public void checkConfiguration(){
		
		setShowHeaderMessage(false);
		setMessageStyle(normalMessageStyle());

		try {
			
			if(HibernateUtil.checkJDBCConnection(currentHibernateConfigurationParameters)) {
				
				String message = setGoodConfiguration();
				agregarMensaje(message);
				setShowHeaderMessage(true);

			} else {
				
				setMessageStyle(errorMessageStyle());
				String message = setWrongConfiguration();
				agregarMensaje(message);
				setShowHeaderMessage(true);
				
			}
			
		} catch (ClassNotFoundException e) {

			setMessageStyle(errorMessageStyle());
			String message = "ClassNotFoundException: Method checkConfiguration in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			agregarMensaje(message);
			logger.error(message);
			setShowHeaderMessage(true);

		} catch (SQLException e) {

			setMessageStyle(errorMessageStyle());
			String message= "SQLException: Method checkConfiguration in HibernateConfigurationBean: ";
			if (e.getErrorCode() == 0){
				message+="It is imposible to connect with this URL";
			} else if (e.getErrorCode() == 1045){
				message+="The user/password are incorrect";
			}
			agregarMensaje(message);
			logger.error(message);
			setShowHeaderMessage(true);

		}
		
	}
	
	private String setUpdateOkMessage() {
		return "HibernateConfigurationParameters name:[ "+this.sessionName+" ] was updated correctly";
	}
	
	private String setAddOkMessage(String name) {
		return "HibernateConfigurationParameters name:[ "+name+" ] was added correctly";
	}
	
	private String getDeleteOkMessage(String name) {
		return "HibernateConfigurationParameters name:[ "+name+" ] was deleted by user:[ " + this.getCurrentUserId() +" ]";
	}
	
	private String setGoodConfiguration() {
		return "This is a GOOD session configuration. The connection with the database is correct. ";
	}
	
	private String setWrongConfiguration() {
		return "This is a WRONG session configuration. It cannot connect with the database";
	}
	
	private String getUpdatedDefaultConfiguration() {
		return "This is now the default configuration";
	}
	
	public ArrayList<DriverObject> driverNameAutocomplete(
			Object input) {

		String driverNameInput = (String) input;
		ArrayList<DriverObject> result = null;

		result = (ArrayList<DriverObject>) DriverObjectUtil.searchDriverArray(driverNameInput);

		return result;
		
	}
	
	// dml 20120203
	public boolean getFormIsEmpty(){
		
		return this.currentHibernateConfigurationParameters.empty();
		
	}

	// dml 20120206
	public void setDefaultConfiguration() {

		setShowHeaderMessage(false);
		setMessageStyle(normalMessageStyle());

		for (HibernateConfigurationParameters hs: hibernateConfigurationParametersList){
			
			if (hs.getSessionName().equals(currentHibernateConfigurationParameters.getSessionName())){		
				hs.setDefaultConfiguration(true);
			}
			else {
				hs.setDefaultConfiguration(false);
			}
		}
			
		try{
				
			HibernateConfigurationUtil.persistConfigurationList(hibernateConfigurationParametersList);
				
			String message = getUpdatedDefaultConfiguration(); 
			logger.info(message);
			agregarMensaje(message);
			setShowHeaderMessage(true);

		} catch (MarshalException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "MarshalException: Method setDefaultConfiguration in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			agregarMensaje(message);
			logger.error(message);

		} catch (ValidationException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "ValidationException: Method setDefaultConfiguration in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			agregarMensaje(message);
			logger.error(message);

		} catch (IOException e) {

			setMessageStyle(errorMessageStyle());
			setShowHeaderMessage(true);
			String message = "IOException: Method setDefaultConfiguration in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			agregarMensaje(message);
			logger.error(message);

		} 

		
	}

}

