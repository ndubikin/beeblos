package org.beeblos.bpm.wc.taglib.bean;

import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;
import static com.sp.common.util.ConstantsCommon.OK_MESSAGE;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.model.noper.BeeblosAttachment;
import org.beeblos.bpm.core.model.noper.DialectObject;
import org.beeblos.bpm.core.model.noper.DriverObject;
import org.beeblos.bpm.core.util.DialectObjectUtil;
import org.beeblos.bpm.core.util.DriverObjectUtil;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.HelperUtil;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import com.sp.common.jsf.util.UtilsVs;
import com.sp.hb4util.core.EnvTypeBL;
import com.sp.hb4util.core.EnvTypeException;
import com.sp.hb4util.core.EnvironmentException;
import com.sp.hb4util.core.HibernateConfigurationBL;
import com.sp.hb4util.core.model.Environment;
import com.sp.hb4util.core.model.HibernateConfigurationParameters;

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
	private String valueBtn;
	
	// dml 20120203
	private boolean recordIsLoaded;
	
	// dml 20120207
	private boolean validHibernateConfiguration;
	
	// dml 20130514
	private List<SelectItem> envTypeComboList;
		
	public static HibernateConfigurationBean getCurrentInstance() {
		return (HibernateConfigurationBean) FacesContext
				.getCurrentInstance().getExternalContext().getRequestMap()
				.get(MANAGED_BEAN_NAME);
	}

	public HibernateConfigurationBean() {
		super();

		_reset();
		init();

	}

	public void init() {
		
		super.init_core();
		setShowHeaderMessage(false);
		
		this.loadHibernateConfigurationParametersList(); 

		this._loadEnvTypeComboList();

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
		this.currentHibernateConfigurationParameters = new HibernateConfigurationParameters(true);// si no no crea el obj environment
		
		this.hibernateConfigurationParametersList = new ArrayList<HibernateConfigurationParameters>();
		
		this.recordIsLoaded = false;
			
		this.validHibernateConfiguration = false;
		
		this.valueBtn="Save";
		
		return null;

	}
	
	// CREATE NULL PROPERTIES OF OBJECT TYPE TO AVOID PROBLEMS
	// WITH VIEW AND ITS REFERENES TO THESE OBJECTS ...
	private void recoverNullObjects(){
		
		if(currentHibernateConfigurationParameters != null){
			
			if (currentHibernateConfigurationParameters.getEnvironment()==null) {
				currentHibernateConfigurationParameters.setEnvironment(new Environment());
			}
		}
		
	}
	
	private void _loadEnvTypeComboList(){
		
		try {
			
			this.envTypeComboList = UtilsVs.castStringPairToSelectitem(
					new EnvTypeBL().getComboList("Select one...", ""));
		
		} catch (EnvTypeException e) {
			
			this.envTypeComboList = new ArrayList<SelectItem>();			
			
			String message = "EnvTypeException: Method _loadEnvTypeComboList in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			
		}
		
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
			
			String message = "Cannot insert an empty session name ";
			super.createWindowMessage(ERROR_MESSAGE, message, null);
			
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
				new HibernateConfigurationBL().persistConfigurationList(
						this.hibernateConfigurationParametersList, getCurrentUserId());				
				
			} else {
				
				String message = "Element to update cannot be found: Method update in HibernateConfigurationBean: ";
				super.createWindowMessage(ERROR_MESSAGE, message, null);
				
			}
			
			String message = setUpdateOkMessage();
			super.createWindowMessage(OK_MESSAGE, message, null);
			
			_reset();
			this.loadHibernateConfigurationParametersList();
			
		} catch (MarshalException e) {

			String message = "MarshalException: Method update in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);

		} catch (ValidationException e) {

			String message = "ValidationException: Method update in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);

		} catch (IOException e) {

			String message = "IOException: Method update in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);

		} catch (EnvironmentException e) {

			String message = "EnvironmentException: Method update in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);

		} 

		return returnValue;

	}

	public String add() {
		logger.debug(" add() role name:" + this.getSessionName());

		setShowHeaderMessage(false);
		setMessageStyle(normalMessageStyle());

		String returnValue = null; // always returns null because calls are ajax

		try {

			this.hibernateConfigurationParametersList.add(currentHibernateConfigurationParameters);
			new HibernateConfigurationBL().persistConfigurationList(
							this.hibernateConfigurationParametersList, getCurrentUserId());

			String message = setAddOkMessage(sessionName);
			super.createWindowMessage(OK_MESSAGE, message, null);

			_reset();
			this.loadHibernateConfigurationParametersList();

		} catch (MarshalException e) {

			String message = "MarshalException: Method add in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (ValidationException e) {

			String message = "ValidationException: Method add in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (IOException e) {

			String message = "IOException: Method add in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (EnvironmentException e) {

			String message = "EnvironmentException: Method add in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
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
			
				String message = " It is not possible to delete the default configuration. ";
				super.createWindowMessage(ERROR_MESSAGE, message, null);
			} else {

				this.hibernateConfigurationParametersList.remove(this.currentHibernateConfigurationParameters);
			
				new HibernateConfigurationBL().persistConfigurationList(
						hibernateConfigurationParametersList, getCurrentUserId());
				
				// set ok message 
				String message = getDeleteOkMessage(currentHibernateConfigurationParameters.getSessionName()); 
				logger.info(message);
				super.createWindowMessage(OK_MESSAGE, message, null);
	
				_reset();
				this.loadHibernateConfigurationParametersList();

			}
			
		} catch (MarshalException e) {

			String message = "MarshalException: Method delete in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (ValidationException e) {

			String message = "ValidationException: Method delete in HibernateConfigurationBean: "
								+ e.getMessage() + " - " + e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (IOException e) {

			String message = "IOException: Method delete in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (EnvironmentException e) {

			String message = "EnvironmentException: Method delete in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
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
				
				currentHibernateConfigurationParameters = new HibernateConfigurationBL()
						.getConfiguration(sessionName);
				
				recoverNullObjects();
				
				recordIsLoaded = true;
				
				modifyValueBtn();
				
			} catch (MarshalException e) {

				String message = "MarshalException: Method loadRecord in HibernateConfigurationBean: ";
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (ValidationException e) {

				String message = "ValidationException: Method loadRecord in HibernateConfigurationBean: ";
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (FileNotFoundException e) {

				String message = "FileNotFoundException: Method loadRecord in HibernateConfigurationBean: ";
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (EnvironmentException e) {

				String message = "EnvironmentException: Method loadRecord in HibernateConfigurationBean: ";
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} 

		}

	}
	
	public void loadHibernateConfigurationParametersList() {
		
		setShowHeaderMessage(false);
		setMessageStyle(normalMessageStyle());

		try{
			
			this.hibernateConfigurationParametersList = 
					(List<HibernateConfigurationParameters>) 
						new HibernateConfigurationBL().getConfigurationList();

		} catch (MarshalException e) {

			String message = "MarshalException: Method loadHibernateConfigurationParametersList in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (ValidationException e) {

			String message = "ValidationException: Method loadHibernateConfigurationParametersList in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (EnvironmentException e) {

			String message = "EnvironmentException: Method loadHibernateConfigurationParametersList in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (FileNotFoundException e) {

			String message = "FileNotFoundException: Method loadHibernateConfigurationParametersList in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
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

	public List<SelectItem> getEnvTypeComboList() {
		return envTypeComboList;
	}

	public void setEnvTypeComboList(List<SelectItem> envTypeComboList) {
		this.envTypeComboList = envTypeComboList;
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

	public boolean isRecordIsLoaded() {
		return recordIsLoaded;
	}

	public void setRecordIsLoaded(boolean recordIsLoaded) {
		this.recordIsLoaded = recordIsLoaded;
	}

	public boolean isValidHibernateConfiguration() {
		return validHibernateConfiguration;
	}

	public void setValidHibernateConfiguration(boolean validHibernateConfiguration) {
		this.validHibernateConfiguration = validHibernateConfiguration;
	}

	public List<HibernateConfigurationParameters> getHibernateConfigurationParametersList() {
		return hibernateConfigurationParametersList;
	}
/*
	public void checkConfiguration(){
		
		setShowHeaderMessage(false);
		setMessageStyle(normalMessageStyle());

		try {
			
			if(HibernateSwitchBL.checkJDBCConnection(currentHibernateConfigurationParameters)) {
				
				String message = setGoodConfiguration();
				super.createWindowMessage(OK_MESSAGE, message, null);

			} else {
				
				String message = setWrongConfiguration();
				super.createWindowMessage(ERROR_MESSAGE, message, null);
				
			}
			
		} catch (ClassNotFoundException e) {

			String message = "ClassNotFoundException: Method checkConfiguration in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (SQLException e) {

			String message= "SQLException: Method checkConfiguration in HibernateConfigurationBean: ";
			if (e.getErrorCode() == 0){
				message+="It is imposible to connect with this URL";
			} else if (e.getErrorCode() == 1045){
				message+="The user/password are incorrect";
			}
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
	}
*/	
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
	
	// dml 20120323
	public ArrayList<DialectObject> dialectNameAutocomplete(
			Object input) {

		String dialectNameInput = (String) input;
		ArrayList<DialectObject> result = null;

		result = (ArrayList<DialectObject>) DialectObjectUtil.searchDialectArray(dialectNameInput);

		return result;
		
	}
	
	// dml 20120203
	public boolean getFormIsEmpty(){
		
		return this.currentHibernateConfigurationParameters.empty();
		
	}
/*	
	// dml 20120206
	public void isValidConfiguration(){

		setShowHeaderMessage(false);
		setMessageStyle(normalMessageStyle());
		
		if (recordIsLoaded){
		
			validHibernateConfiguration = false;
			
			try {
	
				if (validHibernateConfiguration = HibernateSwitchBL.checkJDBCConnection(currentHibernateConfigurationParameters)) {
					
					changeDefaultConfiguration();
					
				}
	
			} catch (ClassNotFoundException e) {
	
				String message = "ClassNotFoundException: Method isValidConfiguration in HibernateConfigurationBean: ";
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (SQLException e) {
	
				String message= "SQLException: Method isValidConfiguration in HibernateConfigurationBean: ";
				if (e.getErrorCode() == 0){
					message+="It is imposible to connect with this URL";
				} else if (e.getErrorCode() == 1045){
					message+="The user/password are incorrect";
				}
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			}
			
		}
			
	}
*/
	// dml 20120206
	public void changeDefaultConfiguration() {

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
			
			new HibernateConfigurationBL().persistConfigurationList(
					hibernateConfigurationParametersList, getCurrentUserId());
				
			String message = getUpdatedDefaultConfiguration(); 
			logger.info(message);
			super.createWindowMessage(OK_MESSAGE, message, null);

		} catch (MarshalException e) {

			String message = "MarshalException: Method setDefaultConfiguration in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (ValidationException e) {

			String message = "ValidationException: Method setDefaultConfiguration in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (IOException e) {

			String message = "IOException: Method setDefaultConfiguration in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (EnvironmentException e) {

			String message = "EnvironmentException: Method setDefaultConfiguration in HibernateConfigurationBean: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
			
		
	}

}

