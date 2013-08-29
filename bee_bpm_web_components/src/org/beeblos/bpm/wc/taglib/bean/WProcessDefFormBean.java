package org.beeblos.bpm.wc.taglib.bean;

import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;
import static org.beeblos.bpm.core.util.Constants.FAIL;
import static org.beeblos.bpm.core.util.Constants.PROCESS_XML_MAP_LOCATION;
import static org.beeblos.bpm.core.util.Constants.SUCCESS_FORM_WPROCESSDEF;
import static org.beeblos.bpm.core.util.Constants.TEXT_DATA_TYPE;
import static org.beeblos.bpm.core.util.Constants.WORKFLOW_EDITOR_URI;
import static org.beeblos.bpm.core.util.Constants.WPROCESSDEF_QUERY;
import static org.beeblos.bpm.core.util.Resourceutil.getStringProperty;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.TableManagerBL;
import org.beeblos.bpm.core.bl.WDataTypeBL;
import org.beeblos.bpm.core.bl.WEmailAccountBL;
import org.beeblos.bpm.core.bl.WEmailTemplatesBL;
import org.beeblos.bpm.core.bl.WProcessDataFieldBL;
import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WProcessHeadBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepSequenceDefBL;
import org.beeblos.bpm.core.email.bl.SendEmailBL;
import org.beeblos.bpm.core.email.model.Email;
import org.beeblos.bpm.core.error.SendEmailException;
import org.beeblos.bpm.core.error.WDataTypeException;
import org.beeblos.bpm.core.error.WEmailAccountException;
import org.beeblos.bpm.core.error.WEmailTemplatesException;
import org.beeblos.bpm.core.error.WProcessDataFieldException;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessHeadException;
import org.beeblos.bpm.core.error.WRoleDefException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.error.XMLGenerationException;
import org.beeblos.bpm.core.model.SystemObject;
import org.beeblos.bpm.core.model.WEmailAccount;
import org.beeblos.bpm.core.model.WEmailTemplates;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessHead;
import org.beeblos.bpm.core.model.WProcessHeadManagedDataConfiguration;
import org.beeblos.bpm.core.model.WProcessRole;
import org.beeblos.bpm.core.model.WProcessUser;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.noper.BeeblosAttachment;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;
import org.beeblos.bpm.core.util.castor.UtilJavaToXML;
import org.beeblos.bpm.tm.exception.TableAlreadyExistsException;
import org.beeblos.bpm.tm.exception.TableHasRecordsException;
import org.beeblos.bpm.tm.exception.TableManagerException;
import org.beeblos.bpm.tm.model.Column;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.HelperUtil;
import org.beeblos.bpm.wc.taglib.util.ListUtil;
import org.beeblos.bpm.wc.taglib.util.WProcessDefUtil;

import com.sp.common.jsf.util.UtilsVs;
import com.sp.common.model.WDataType;

public class WProcessDefFormBean extends CoreManagedBean {

	private static final String _ROOT_MANAGED_TABLE_NAME = "wmt_";

	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory
			.getLog(WProcessDefFormBean.class.getName());

	private static final String MANAGED_BEAN_NAME = "wProcessDefFormBean";

	public static WProcessDefFormBean getCurrentInstance() {
		return (WProcessDefFormBean) FacesContext.getCurrentInstance()
				.getExternalContext().getRequestMap().get(MANAGED_BEAN_NAME);
	}

	private Integer currentUserId;
	private TimeZone timeZone;

	// main properties:

	private WProcessDef currentWProcessDef;
	private Integer currentId; // current object managed by this bb

	// nes 20130806
	// fields to present managed table information
	private List<Column> columnListTM;
	private Integer reccountTM;
	
	// auxiliar properties

	private BeeblosAttachment attachment;
	private String documentLink;

	private List<SelectItem> lStepCombo = new ArrayList<SelectItem>();

	private boolean readOnly;

	private WProcessRole currentWProcessRole;
	private WProcessUser currentWProcessUser;
	// dml 20120125
	private WStepSequenceDef currentStepSequence;
	
	private String strRoleList;
	private String strUserList;
	
	// dml 20120127
	private List<SelectItem> currentStepResponseList;
	private List<String> currentStepValidResponses;
	
	// dml 20120223
	private WEmailAccount currEmailAccount;
	private List<WEmailAccount> wEmailAccountList;
	private Integer wEmailAccountListResults;
	private String checkingEmailAccount;
	
	// dml 20120227
	private String messageStyle;
	
	// dml 20120305
	private String emailNameFilter;
	private String activeFilter;
	
	// dml 20120305
	private String returnStatement;
	
	// dml 20120306
	private WEmailTemplates arrivingUserNoticeTemplate;
	private WEmailTemplates arrivingAdminNoticeTemplate;
	
	private List<SelectItem> arrivingNoticeEmailTemplatesCombo = new ArrayList<SelectItem>();
	
	// dml 20120326
	private Integer currentStepId;
	private boolean stepOutgoings;
	private boolean stepIncomings;
	
	// dml 20130507
	private Integer currentProcessIdSelected;
	private List<SelectItem> wProcessComboList;
	
	// dml 20130508
	private List<WProcessDefLight> relatedProcessDefList;

	//rrl 20130729
	private boolean flagValidate;
//	private boolean refreshForm;
	private boolean visibleButtonNewDataField;
	private boolean visibleButtonAdvancedConfiguration;
	private WProcessDataField wProcessDataFieldSelected;
	private List<SelectItem> dataTypes;
	private List<WProcessDataField> dataFieldList;

	
	public WProcessDefFormBean() {
		super();
		init();
	}

	public void init() {
		super.init();
		setShowHeaderMessage(false);
		loadStepCombo();
		this._loadWProcessComboList(); // dml 20130430
		_reset();

	}

	// to add a new WProcessDef
	public void initEmptyWProcessDef() {

		currentId = null;
		currentWProcessDef = new WProcessDef();
		this.setReadOnly(false);
		recoverNullObjects(); // <<<<<<<<< IMPORTANT >>>>>>>>>>>
							  // to avoid access to null objects from view
	}

	public void _reset() {

		this.currentId = null;
		this.currentWProcessDef = null;
		this.currentWProcessRole = new WProcessRole();
		this.currentWProcessUser = new WProcessUser();
		this.readOnly = true;
		this.attachment = new BeeblosAttachment();
		this.documentLink = null;
		
		// dml 20120125
		this.currentStepSequence = new WStepSequenceDef();
		
		// dml 20120127
		this.currentStepResponseList = null;
		this.currentStepValidResponses = new ArrayList<String>();
		this.checkingEmailAccount = "";
		
		this.emailNameFilter = "";
		this.returnStatement = "";

		// dml 20120306
		this.arrivingUserNoticeTemplate = new WEmailTemplates();
		this.arrivingAdminNoticeTemplate = new WEmailTemplates();
		
		loadArrivingNoticeEmailTemplatesCombo();
		
		// dml 20120223
		this.setCurrEmailAccount(new WEmailAccount(EMPTY_OBJECT));

		recoverNullObjects();

		HelperUtil.recreateBean("documentacionBean",
				"com.softpoint.taglib.common.DocumentacionBean");
		
		this.stepOutgoings = false;
		this.stepIncomings = false;
		
		//rrl 20130729
//		refreshForm=false;
		visibleButtonNewDataField = true;
		visibleButtonAdvancedConfiguration = true;
		wProcessDataFieldSelected = new WProcessDataField(EMPTY_OBJECT); 
		
	}

	// load an Object in currentWProcessDef
	public void loadCurrentWProcessDef(Integer id) {

		this.currentId = id;
		this.loadCurrentWProcessDef();

	}

	public void loadCurrentWProcessDef() {

		WProcessDefBL wpdbl = new WProcessDefBL();

		try {

			currentWProcessDef = wpdbl.getWProcessDefByPK(this.currentId,
					getCurrentUserId());

			if (currentWProcessDef != null) {
				
				// DAVID: ESTO ESTABA EN EL METODO loadStepSequenceList() y no se bien para q es ...
				this.stepOutgoings = false;
				this.stepIncomings = false;
				
				//dml 20120223
				if (currentWProcessDef.getSystemEmailAccount() != null){
				
					this.currEmailAccount = currentWProcessDef.getSystemEmailAccount();

				}
			}

			recoverNullObjects(); // <<<<<<<< IMPORTANT >>>>>>>>>>>

			// dml 20120306
			loadEmailTemplateVariables();
			
			this.reloadRelatedProcessDefList(); // dml 20130508
			this.reloadDataFieldList();         // rrl 20130730
			
		} catch (WProcessDefException ex1) {

			String message = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = {
					message + ",",
					".Error loading current WProcessDef ..."
							+ currentWProcessDef.getId() };
			agregarMensaje("203", message, params, FGPException.ERROR);

			logger.error(message);
			
		} catch (WStepSequenceDefException e) {

			String message = e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", ".Error trying to load current WProcessDef ..." };
			agregarMensaje("203", message, params, FGPException.ERROR);
			logger.error(message);
			
		}

	}
	
	// dml 20130508
	public void reloadRelatedProcessDefList(){
		
		try {
			
 			this.relatedProcessDefList = new WProcessDefBL()
				.finderWProcessDefLight(false, null, null, null, false, null, null, 
						this.currentWProcessDef.getProcess().getId(), this.activeFilter, getCurrentUserId());
			
		} catch (WProcessDefException e) {

			this.relatedProcessDefList = new ArrayList<WProcessDefLight>();
			
			String message = e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", ".Error trying to load current WProcessDef ..." };
			agregarMensaje("203", message, params, FGPException.ERROR);
			logger.error(message);
			
		}
		
	}

	//rrl 20120228 Bermuda Triangle mystery on the loss of source code Castor XML the process Marshall
	public void generateXMLCurrentWProcessDef() {
		
		logger.debug(">>>>>>>>>> Castor XML starting the process Marshall");
		
		String xmlTemplatesPath = "/home/u097/workspace/bee_bpm/src/org/beeblos/bpm/core/xml/castor/WProcessDef_castor.xml";
		
		// path pointing to folder "castor" in webapps/appname/castor ( or in WebContent/castor in eclipse environment ) 
//		String xmlTemplatesPath = getSession().getServletContext().getRealPath("/")+"xmlTemplates";
//		xmlTemplatesPath+="castor/"+currentWProcessDef.getClass().getSimpleName()+"_castor.xml";		

    	HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			
		try{
			String contenidoXML = UtilJavaToXML.toXML(currentWProcessDef, xmlTemplatesPath);
			
			if (contenidoXML != null) {
				
				byte[] buffer =  contenidoXML.getBytes();
				
				response.setContentLength(buffer.length);
				response.setContentType("text/xml"); 
				// response.setLocale( new Locale(DEFAULT_ENCODING) );
				response.setLocale( new Locale("UTF-8") );

				response.addHeader("Content-Disposition",  "attachment; filename=\""  + "WProcessDef_id_" + currentWProcessDef.getId() + ".xml"  +  "\"");						
				response.setHeader("Cache-Control","no-cache");
				
				ServletOutputStream outStream = response.getOutputStream();
				outStream.write(buffer,0,buffer.length);
				outStream.flush();
				outStream.close();
				FacesContext.getCurrentInstance().responseComplete();
				
		        logger.debug(contenidoXML);
				
			} else {
				logger.debug(">>>>>>>>>> Castor XML the content XML is null");
			}
			
		}catch(XMLGenerationException e){

			String message = e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", ".Error generate current WProcessDef to XML ..." };
			agregarMensaje("203", message, params, FGPException.ERROR);
			logger.error(message);			
			
		}catch(IOException e){
			
			String message = e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", ".Error produced by failed or interrupted I/O operations current WProcessDef to XML ..." };
			agregarMensaje("203", message, params, FGPException.ERROR);
			logger.error(message);
			
		}
		
		logger.debug(">>>>>>>>>> Castor XML process complete Marshall");
		
	}
	
	private void setModel() {

		if (currentWProcessDef != null) {
			if (currentWProcessDef.getBeginStep() != null
					&& (currentWProcessDef.getBeginStep().empty()
							|| currentWProcessDef.getBeginStep().getId() == 0)) {
				currentWProcessDef.setBeginStep(null);
			}

			// dml 20120306
			if (currentWProcessDef.getArrivingUserNoticeTemplate() != null
					&& (currentWProcessDef.getArrivingUserNoticeTemplate().empty()
							|| currentWProcessDef.getArrivingUserNoticeTemplate().getId() == 0)) {
				currentWProcessDef.setArrivingUserNoticeTemplate(null);
			}

			// dml 20120306
			if (currentWProcessDef.getArrivingAdminNoticeTemplate() != null
					&& (currentWProcessDef.getArrivingAdminNoticeTemplate().empty()
							|| currentWProcessDef.getArrivingAdminNoticeTemplate().getId() == 0)) {
				currentWProcessDef.setArrivingAdminNoticeTemplate(null);
			}

			// dml 20130430
			if (currentWProcessDef.getSystemEmailAccount() != null
					&& (currentWProcessDef.getSystemEmailAccount().empty()
							|| currentWProcessDef.getSystemEmailAccount().getId() == 0)) {
				currentWProcessDef.setSystemEmailAccount(null);
			}

			// dml 20130430
			if (currentWProcessDef.getTotalTimeUnit() != null
					&& (currentWProcessDef.getTotalTimeUnit().empty()
							|| currentWProcessDef.getTotalTimeUnit().getId() == 0)) {
				currentWProcessDef.setTotalTimeUnit(null);
			}
			
			// dml 20130820 - si el processDataField es nulo lo vacio para que no de problemas de consistencia
			if (currentWProcessDef.getProcess() != null
					&& (this.dataFieldList == null
						|| this.dataFieldList.isEmpty())){
					currentWProcessDef.getProcess().setProcessDataFieldDef(null);
			}
			
		}
		
		if (currentStepSequence != null) {

			if ((currentStepSequence.getToStep() != null &&
				currentStepSequence.getToStep().empty()) ||
				currentStepSequence.getToStep().getId().equals(0)) {
			
				currentStepSequence.setToStep(null);
			
			}
			
		}

		// dml 20120606 - if this field is an empty string, it will be set null in the DB
		if ( currentWProcessDef.getIdListZone()!=null
				&& "".equals(currentWProcessDef.getIdListZone().trim())) {
			currentWProcessDef.setIdListZone( null );
		}			

		// dml 20120606 - if this field is an empty string, it will be set null in the DB
		if ( currentWProcessDef.getIdWorkZone()!=null
				&& "".equals(currentWProcessDef.getIdWorkZone().trim()) ) {
			currentWProcessDef.setIdWorkZone( null );
		}			

		// dml 20120606 - if this field is an empty string, it will be set null in the DB
		if ( currentWProcessDef.getIdAdditionalZone()!=null
				&& "".equals(currentWProcessDef.getIdAdditionalZone().trim()) ) {
			currentWProcessDef.setIdAdditionalZone( null );
		}			

	}

	private void recoverNullObjects() {

		if (currentWProcessDef != null) {

			if (currentWProcessDef.getBeginStep() == null) {
				currentWProcessDef.setBeginStep(new WStepDef(EMPTY_OBJECT));
			}

			if (currentWProcessDef.getRolesRelated() == null) {
				currentWProcessDef.setRolesRelated(new HashSet<WProcessRole>());
			}

			if (currentWProcessDef.getUsersRelated() == null) {
				currentWProcessDef.setUsersRelated(new HashSet<WProcessUser>());
			}

			// dml 20120306
			if (currentWProcessDef.getArrivingUserNoticeTemplate() == null) {
				currentWProcessDef.setArrivingUserNoticeTemplate(new WEmailTemplates(EMPTY_OBJECT));
			}

			// dml 20120306
			if (currentWProcessDef.getArrivingAdminNoticeTemplate() == null) {
				currentWProcessDef.setArrivingAdminNoticeTemplate(new WEmailTemplates(EMPTY_OBJECT));
			}

			// dml 20130506
			if (currentWProcessDef.getProcess() == null) {
				currentWProcessDef.setProcess(new WProcessHead());
			}

		}

		if (currentWProcessRole != null) {

			if (currentWProcessRole.getProcess() == null) {
				currentWProcessRole.setProcess(new WProcessDef(EMPTY_OBJECT));
			}

			if (currentWProcessRole.getRole() == null) {
				currentWProcessRole.setRole(new WRoleDef(EMPTY_OBJECT));
			}

		}

		if (currentWProcessUser != null) {

			if (currentWProcessUser.getProcess() == null) {
				currentWProcessUser.setProcess(new WProcessDef(EMPTY_OBJECT));
			}

			if (currentWProcessUser.getUser() == null) {
				currentWProcessUser.setUser(new WUserDef(EMPTY_OBJECT));
			}

		}
		
		if (currentStepSequence != null) {
			
			if (currentStepSequence.getProcess() == null) {
				currentStepSequence.setProcess(new WProcessDef(EMPTY_OBJECT));
			}
			
			if (currentStepSequence.getFromStep() == null) {
				currentStepSequence.setFromStep(new WStepDef(EMPTY_OBJECT));
			}
			
			if (currentStepSequence.getToStep() == null) {
				currentStepSequence.setToStep(new WStepDef(EMPTY_OBJECT));
			}
			
		}

	}

	// checks input data before save or update
	private boolean checkInputData() {

		boolean result = false;

		if (this.currentWProcessDef != null
				&& this.currentWProcessDef.getName() != null
				&& !"".equals(this.currentWProcessDef.getName())
				&& this.currentWProcessDef.getVersion() != null
				&& !"".equals(this.currentWProcessDef.getVersion())) {

			result = true;

		}

		// dml 20130506 - si estamos modificando el proceso interno tenemos que ver que el nombre no sea vacio 
		if (this.currentWProcessDef.getProcess() != null
				&& this.currentWProcessDef.getProcess().getName() != null
				&& !"".equals(this.currentWProcessDef.getProcess().getName())) {

			result = true;

		}

		return result;
	}

	public String cancel() {

		if (returnStatement.equals(WPROCESSDEF_QUERY)){
			
			_reset();
			return WPROCESSDEF_QUERY;
			
		}
		
		Integer id = this.currentId;
		_reset();
		this.currentId = id;
		this.loadCurrentWProcessDef();
		this.setReadOnly(true);
		return null;
	}

	public String save_continue() {

		String result = FAIL;

		if (checkInputData()) {

			try {

				result = add();
				recoverNullObjects(); // <<<<<<<<<<<<<<<<<<<<IMPORTANT>>>>>>>>>>>>>>>>>>

			} catch (WProcessDefException e) {
				recoverNullObjects();
				result = FAIL;
			}
		}

		return result;
	}

	public String save() {

		String result = null;

		if (checkInputData()) {

			try {

				result = add();
				result = _defineReturn();
				_reset();

			} catch (WProcessDefException e) {
				recoverNullObjects(); // <<<<<<<<<<<<<<<<<<<<IMPORTANT>>>>>>>>>>>>>>>>>>
				result = null;
			}
		}

		return result;
	}

	public String add() throws WProcessDefException {

		logger.debug("WProcessDefFormBean: add: currentProcessDefId:["
				+ this.currentId + "] ");

		setShowHeaderMessage(false);
		String message = "";
		
		String ret = null;

		// if object already exists in db then update and return
		if (currentWProcessDef != null && currentWProcessDef.getId() != null
				&& currentWProcessDef.getId() != 0) {

			// before update store document in repository ( if exists )
			if (attachment.getDocumentoNombre() != null
					&& !"".equals(attachment.getDocumentoNombre())) {
				// storeInRepository();
			}

			ret = update();

			message = "The process '" + this.currentWProcessDef.getProcess().getName()+ "' has been correctly updated";

			return ret;

		}

		WProcessDefBL wpdBL = new WProcessDefBL();

		try {

			// dml 20120306
			setEmailTemplatesInObject();

			setModel(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>

			this.currentId = wpdBL.add(currentWProcessDef,
					this.getCurrentUserId());

			message = "The process '" + this.currentWProcessDef.getProcess().getName()+ "' has been correctly added";

			loadCurrentWProcessDef(); // reload object from db

			// Manual process to store attachment in the repository ( if
			// attachment exists ...
			if (attachment.getDocumentoNombre() != null
					&& !"".equals(attachment.getDocumentoNombre())) {
				// storeInRepository();

				update();
			
				message = "The process '" + this.currentWProcessDef.getProcess().getName()+ "' has been correctly updated";

			}

			recoverNullObjects();

			this.setReadOnly(true);

			ret = SUCCESS_FORM_WPROCESSDEF;

			setShowHeaderMessage(true);
			this.messageStyle = this.normalMessageStyle();
			agregarMensaje(message);

		} catch (WProcessDefException ex1) {

			message = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { message + ",", ".Please confirm input values." };
			agregarMensaje("203", message, params, FGPException.ERROR);

			throw new WProcessDefException(message);

		} catch (Exception e) {

			message = e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", ".Error inserting object ..." };
			agregarMensaje("203", message, params, FGPException.ERROR);

			throw new WProcessDefException(message);

		}

		return ret;
	}

	public String update() {
		logger.debug("update(): currentId:" + currentWProcessDef.getId()
				+ " - " + currentWProcessDef.getName());

		String ret = null;

		WProcessDefBL wpdBL = new WProcessDefBL();

		try {
			
			// dml 20120306
			setEmailTemplatesInObject();

			setModel();

			wpdBL.update(currentWProcessDef, this.getCurrentUserId());

			loadCurrentWProcessDef(); // reload object from db

			recoverNullObjects();

			setShowHeaderMessage(true);
			ret = SUCCESS_FORM_WPROCESSDEF;
			this.setReadOnly(true);

		} catch (WProcessDefException ex1) {

			String message = "Error updating object: "
					+ currentWProcessDef.getId() + " - "
					+ currentWProcessDef.getName() + "\n" + ex1.getMessage()
					+ "\n" + ex1.getCause();

			logger.error(message);

			String params[] = { message + ",", ".Please confirm input values." };
			agregarMensaje("203", message, params, FGPException.ERROR);

			logger.error(message);

		}

		return ret;
	}
	
	
	private void loadStepCombo() {

		try {
			
			setlStepCombo(
					UtilsVs.castStringPairToSelectitem(
							new WStepDefBL()
									.getComboList("Select step...", null)));

		} catch (WStepDefException ex1) {

			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".loadStepCombo() WStepDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();

		}
	}

	private void loadDataTypes() {
		try {
			setDataTypes(
					UtilsVs.castStringPairToSelectitem(
							new WDataTypeBL().getComboList(null, null)));
			
		} catch (WDataTypeException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".loadDataTypes() WDataTypeException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);

		} 
	}
	
	public WProcessDef getCurrentWProcessDef() {
		return currentWProcessDef;
	}

	public void setCurrentWProcessDef(WProcessDef currentWProcessDef) {
		this.currentWProcessDef = currentWProcessDef;
	}

	public Integer getCurrentId() {
		return currentId;
	}

	public void setCurrentId(Integer currentId) {
		this.currentId = currentId;
	}

	public BeeblosAttachment getAttachment() {
		return attachment;
	}

	public void setAttachment(BeeblosAttachment attachment) {
		this.attachment = attachment;
	}

	public List<Column> getColumnListTM() {
		return columnListTM;
	}

	public void setColumnListTM(List<Column> columnListTM) {
		this.columnListTM = columnListTM;
	}

	public Integer getReccountTM() {
		return reccountTM;
	}

	public void setReccountTM(Integer reccountTM) {
		this.reccountTM = reccountTM;
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

	public List<SelectItem> getlStepCombo() {
		return lStepCombo;
	}

	public void setlStepCombo(List<SelectItem> lStepCombo) {
		this.lStepCombo = lStepCombo;
	}

	// nes 20130502 dejo por compatibilidad pero hay que quitarlo en cuanto podamos
	@Deprecated 
	public List<WStepDef> getlSteps() {
		return currentWProcessDef.getlSteps();
	}
	// nes 20130502 dejo por compatibilidad pero hay que quitarlo en cuanto podamos
	@Deprecated
	public void setlSteps(List<WStepDef> lSteps) {
		currentWProcessDef.setlSteps(lSteps);
	}

	public List<WProcessRole> getRolesRelatedList() {

		List<WProcessRole> rrl = new ArrayList<WProcessRole>();

		if (currentWProcessDef != null
				&& currentWProcessDef.getRolesRelated() != null
				&& currentWProcessDef.getRolesRelated().size() != 0) {

			rrl = new ArrayList<WProcessRole>(
					currentWProcessDef.getRolesRelated());

		}

		return rrl;

	}

	public List<WProcessUser> getUsersRelatedList() {

		List<WProcessUser> url = new ArrayList<WProcessUser>();

		if (currentWProcessDef != null
				&& currentWProcessDef.getUsersRelated() != null
				&& currentWProcessDef.getUsersRelated().size() != 0) {

			url = new ArrayList<WProcessUser>(
					currentWProcessDef.getUsersRelated());

		}

		return url;

	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public void changeReadOnlyToFalse() {
		this.readOnly = false;
	}

	//rrl 20130729 When click "Refresh" button reloads the form by loadWProcessForm()
	public void refreshWProcessDef() {
//		refreshForm=false;
		loadWProcessForm();
	}
	
	public String getStrRoleList() {
		strRoleList = "";
		for (WProcessRole pr : this.currentWProcessDef.getRolesRelated()) {
			strRoleList += (strRoleList != null && !"".equals(strRoleList) ? ","
					: "")
					+ pr.getRole().getId();
		}

		logger.debug("--------------->>>>>>>>> strRoleList ------------>>>>>>>>"
						+ strRoleList);
		return strRoleList;
	}

	public void setStrRoleList(String strRoleList) {
		this.strRoleList = strRoleList;
	}

	public String getStrUserList() {
		strUserList = "";

		for (WProcessUser pu : this.currentWProcessDef.getUsersRelated()) {
			strUserList += (strUserList != null && !"".equals(strUserList) ? ","
					: "")
					+ pu.getUser().getId();
		}

		System.out
				.println("--------------->>>>>>>>> strUserList ------------>>>>>>>>"
						+ strUserList);
		return strUserList;
	}

	public void setStrUserList(String strUserList) {
		this.strUserList = strUserList;
	}

	@Deprecated
	public List<WStepSequenceDef> getStepSequenceList() {
		return currentWProcessDef.getStepSequenceList();
	}

	@Deprecated
	public void setStepSequenceList(List<WStepSequenceDef> stepSequenceList) {
		currentWProcessDef.setStepSequenceList(stepSequenceList);
	}

	public WStepSequenceDef getCurrentStepSequence() {
		return currentStepSequence;
	}

	public void setCurrentStepSequence(WStepSequenceDef currentStepSequence) {
		this.currentStepSequence = currentStepSequence;
	}

	public List<SelectItem> getCurrentStepResponseList() {
		return currentStepResponseList;
	}

	public void setCurrentStepResponseList(List<SelectItem> currentStepResponseList) {
		this.currentStepResponseList = currentStepResponseList;
	}

	public List<String> getCurrentStepValidResponses() {
		return currentStepValidResponses;
	}

	public void setCurrentStepValidResponses(List<String> currentStepValidResponses) {
		this.currentStepValidResponses = currentStepValidResponses;
	}

	public WProcessRole getCurrentWProcessRole() {
		return currentWProcessRole;
	}

	public void setCurrentWProcessRole(WProcessRole currentWProcessRole) {
		this.currentWProcessRole = currentWProcessRole;
	}

	public WProcessUser getCurrentWProcessUser() {
		return currentWProcessUser;
	}

	public void setCurrentWProcessUser(WProcessUser currentWProcessUser) {
		this.currentWProcessUser = currentWProcessUser;
	}

	public WEmailAccount getCurrEmailAccount() {
		return currEmailAccount;
	}

	public void setCurrEmailAccount(WEmailAccount currEmailAccount) {
		this.currEmailAccount = currEmailAccount;
	}

	public List<WEmailAccount> getwEmailAccountList() {
		return wEmailAccountList;
	}

	public void setwEmailAccountList(List<WEmailAccount> wEmailAccountList) {
		this.wEmailAccountList = wEmailAccountList;
	}

	public Integer getwEmailAccountListResults() {
		return wEmailAccountListResults;
	}

	public void setwEmailAccountListResults(
			Integer wEmailAccountListResults) {
		this.wEmailAccountListResults = wEmailAccountListResults;
	}

	public String getCheckingEmailAccount() {
		return checkingEmailAccount;
	}

	public void setCheckingEmailAccount(String checkingEmailAccount) {
		this.checkingEmailAccount = checkingEmailAccount;
	}

	public String getMessageStyle() {
		return messageStyle;
	}

	public void setMessageStyle(String messageStyle) {
		this.messageStyle = messageStyle;
	}

	public String getEmailNameFilter() {
		return emailNameFilter;
	}

	public void setEmailNameFilter(String emailNameFilter) {
		this.emailNameFilter = emailNameFilter;
	}

	public String getActiveFilter() {
		return activeFilter;
	}

	public void setActiveFilter(String activeFilter) {
		this.activeFilter = activeFilter;
	}

	public String getReturnStatement() {
		return returnStatement;
	}

	public void setReturnStatement(String returnStatement) {
		this.returnStatement = returnStatement;
	}

	public WEmailTemplates getArrivingUserNoticeTemplate() {
		return arrivingUserNoticeTemplate;
	}

	public void setArrivingUserNoticeTemplate(
			WEmailTemplates arrivingUserNoticeTemplate) {
		this.arrivingUserNoticeTemplate = arrivingUserNoticeTemplate;
	}

	public WEmailTemplates getArrivingAdminNoticeTemplate() {
		return arrivingAdminNoticeTemplate;
	}

	public void setArrivingAdminNoticeTemplate(
			WEmailTemplates arrivingAdminNoticeTemplate) {
		this.arrivingAdminNoticeTemplate = arrivingAdminNoticeTemplate;
	}

	public List<SelectItem> getArrivingNoticeEmailTemplatesCombo() {
		return arrivingNoticeEmailTemplatesCombo;
	}

	public void setArrivingNoticeEmailTemplatesCombo(
			List<SelectItem> arrivingNoticeEmailTemplatesCombo) {
		this.arrivingNoticeEmailTemplatesCombo = arrivingNoticeEmailTemplatesCombo;
	}

	public Integer getCurrentStepId() {
		return currentStepId;
	}

	public void setCurrentStepId(Integer currentStepId) {
		this.currentStepId = currentStepId;
	}

	public boolean isStepOutgoings() {
		return stepOutgoings;
	}

	public void setStepOutgoings(boolean stepOutgoings) {
		this.stepOutgoings = stepOutgoings;
	}

	public boolean isStepIncomings() {
		return stepIncomings;
	}

	public void setStepIncomings(boolean stepIncomings) {
		this.stepIncomings = stepIncomings;
	}

	public void deleteWProcessRole() {

		if (currentWProcessDef != null
				&& currentWProcessDef.getRolesRelated() != null
				&& !currentWProcessDef.getRolesRelated().isEmpty()) {

			for (WProcessRole wpr : currentWProcessDef.getRolesRelated()) {

				if (wpr.getRole() != null
						&& wpr.getRole().getId() != null
						&& wpr.getRole().getId()
								.equals(currentWProcessRole.getRole().getId())) {

					currentWProcessDef.getRolesRelated().remove(wpr);
					break;

				}

			}

			try {

				persistCurrentObject();
				strRoleList = "";
				for (WProcessRole pr : this.currentWProcessDef
						.getRolesRelated()) {
					strRoleList += (strRoleList != null
							&& !"".equals(strRoleList) ? "," : "")
							+ pr.getRole().getId();
				}

			} catch (WProcessDefException ex1) {

				String message = ex1.getMessage() + " - " + ex1.getCause();
				String params[] = { message + ",",
						".Error deleting WProcessRole ..." };
				agregarMensaje("203", message, params, FGPException.ERROR);

			}

		}

	}

	public void changeAdminPrivilegesWProcessRole() {

		if (currentWProcessDef != null
				&& currentWProcessDef.getRolesRelated() != null
				&& !currentWProcessDef.getRolesRelated().isEmpty()) {

			for (WProcessRole wpr : currentWProcessDef.getRolesRelated()) {

				if (wpr.getRole() != null
						&& wpr.getRole().getId() != null
						&& wpr.getRole().getId()
								.equals(currentWProcessRole.getRole().getId())) {

					if (wpr.isAdmin()) {
						wpr.setAdmin(false);
					} else {
						wpr.setAdmin(true);
					}
					break;

				}

			}

			try {

				persistCurrentObject();

			} catch (WProcessDefException ex1) {

				String message = ex1.getMessage() + " - " + ex1.getCause();
				String params[] = { message + ",",
						".Error changing admin privileges ..." };
				agregarMensaje("203", message, params, FGPException.ERROR);

			}

		}

	}

	public void deleteWProcessUser() {

		if (currentWProcessDef != null
				&& currentWProcessDef.getUsersRelated() != null
				&& !currentWProcessDef.getUsersRelated().isEmpty()) {

			for (WProcessUser wpu : currentWProcessDef.getUsersRelated()) {

				if (wpu.getUser() != null
						&& wpu.getUser().getId() != null
						&& wpu.getUser().getId()
								.equals(currentWProcessUser.getUser().getId())) {

					currentWProcessDef.getUsersRelated().remove(wpu);
					break;

				}

			}

			try {

				persistCurrentObject();
				strUserList = "";
				for (WProcessUser pu : this.currentWProcessDef
						.getUsersRelated()) {
					strUserList += (strUserList != null
							&& !"".equals(strUserList) ? "," : "")
							+ pu.getUser().getId();
				}

			} catch (WProcessDefException ex1) {

				String message = ex1.getMessage() + " - " + ex1.getCause();
				String params[] = { message + ",",
						".Error deleting WProcessUser ..." };
				agregarMensaje("203", message, params, FGPException.ERROR);

			}

		}

	}

	public void changeAdminPrivilegesWProcessUser() {

		if (currentWProcessDef != null
				&& currentWProcessDef.getUsersRelated() != null
				&& !currentWProcessDef.getUsersRelated().isEmpty()) {

			for (WProcessUser wpu : currentWProcessDef.getUsersRelated()) {

				if (wpu.getUser() != null
						&& wpu.getUser().getId() != null
						&& wpu.getUser().getId()
								.equals(currentWProcessUser.getUser().getId())) {

					if (wpu.isAdmin()) {
						wpu.setAdmin(false);
					} else {
						wpu.setAdmin(true);
					}
					break;

				}

			}

			try {

				persistCurrentObject();

			} catch (WProcessDefException ex1) {

				String message = ex1.getMessage() + " - " + ex1.getCause();
				String params[] = { message + ",",
						".Error changing admin privileges ..." };
				agregarMensaje("203", message, params, FGPException.ERROR);

			}

		}

	}

	public String updateRolesRelated() {

		try {

			if ("".equals(strRoleList)) {

				currentWProcessDef.setRolesRelated(null);

			} else {

				ListUtil.updateProcessRoleRelatedList(strRoleList,
						currentWProcessDef, getCurrentUserId());

			}

			persistCurrentObject();

		} catch (NumberFormatException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".updateRolesRelated() NumberFormatException ..." };
			agregarMensaje("203", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
		} catch (WRoleDefException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".updateRolesRelated() WRoleDefException ..." };
			agregarMensaje("203", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
		} catch (WProcessDefException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".updateRolesRelated() WProcessDefException ..." };
			agregarMensaje("203", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
		}

		return null;
	}

	public String updateUsersRelated() {

		try {

			if ("".equals(strUserList)) {

				currentWProcessDef.setUsersRelated(null);

			} else {

				ListUtil.updateProcessUserRelatedList(strUserList,
						currentWProcessDef, getCurrentUserId());

			}

			persistCurrentObject();

		} catch (NumberFormatException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".updateUsersRelated() NumberFormatException ..." };
			agregarMensaje("203", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
		} catch (WUserDefException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".updateUsersRelated() WUserDefException ..." };
			agregarMensaje("203", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
		} catch (WProcessDefException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".updateUsersRelated() WProcessDefException ..." };
			agregarMensaje("203", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
		}

		return null;
	}

	private void persistCurrentObject() throws WProcessDefException {
		WProcessDefBL wpdBL = new WProcessDefBL();
		this.setModel();
		wpdBL.update(this.currentWProcessDef, getCurrentUserId());
		this.recoverNullObjects();
	}
	
	// dml 20120125
	public void addAndUpdateStepSequence(){
		
		WStepSequenceDefBL wssdBL = new WStepSequenceDefBL();
		
		try {

			setModel();
			
			currentStepSequence.setValidResponses(UtilsVs.castStringListToString(currentStepValidResponses, "|"));

			if (currentStepSequence.getId() != null && 
					currentStepSequence.getId() != 0) {
			
				wssdBL.update(currentStepSequence, getCurrentUserId());

			} else {
	
				currentStepSequence.setProcess(currentWProcessDef);
				wssdBL.add(currentStepSequence, getCurrentUserId());
	
			}
		
			cleanStepSequence();
			
			loadStepFromSequence();
			
		} catch (WStepSequenceDefException e) {

			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".addAndUpdateStepSequence() WStepSequenceDefException ..." };
			agregarMensaje("203", mensaje, params, FGPException.ERROR);
			e.printStackTrace();

		}
		
	}

	public void deleteStepFromSequence(){
		
		WStepSequenceDefBL wssdBL = new WStepSequenceDefBL();
		
		try {
			
			if (currentStepSequence != null && currentStepSequence.getId() != null){
			
				currentStepSequence = wssdBL.getWStepSequenceDefByPK(currentStepSequence.getId(), getCurrentUserId());
						
				wssdBL.deleteRoute(currentStepSequence, getCurrentUserId() );

				cleanStepSequence();
				
			}		
			
		} catch (WStepSequenceDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".deleteStepFromSequence() WStepSequenceDefException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}
		
	}

	public void cleanStepSequence(){
		
		setCurrentStepSequence(new WStepSequenceDef(EMPTY_OBJECT));
		
		if (currentStepValidResponses != null && currentStepValidResponses.size() != 0) {
			currentStepValidResponses.clear();
		}
		if (currentStepResponseList != null && currentStepResponseList.size() != 0) {
			currentStepResponseList.clear();
		}
		
		try {

			currentWProcessDef
				.setStepSequenceList(
						new WStepSequenceDefBL()
							.getStepSequenceList(currentWProcessDef.getId(), this.getCurrentUserId() ) );

		} catch (WStepSequenceDefException e) {

			String message = e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", ".Error reloading step sequence list." };
			agregarMensaje("203", message, params, FGPException.ERROR);
			logger.error(message);
			
		}
				
		
	}
	
	// dml 20120326
	public void loadInfoForNewStepSequenceOutgoing(){
		
		this.stepOutgoings = true;
		
		this.currentStepSequence.getFromStep().setId(currentStepId);
		
	}
	
	// dml 20120326
	public void loadInfoForNewStepSequenceIncoming(){
		
		this.stepIncomings = true;
		
		this.currentStepSequence.getToStep().setId(currentStepId);
		
	}
	
	// dml 20120326
	public void loadStepFromSequenceProcessOutgoing(){
		
		this.stepOutgoings = true;
		
		loadStepFromSequence();
		
	}
	
	// dml 20120326
	public void loadStepFromSequenceProcessIncoming(){
		
		this.stepIncomings = true;
		
		loadStepFromSequence();
		
	}
	
	public void loadStepFromSequence(){
		
		WStepSequenceDefBL wssdBL = new WStepSequenceDefBL();
		
		try {

			if (currentStepSequence != null && currentStepSequence.getId() != null && 
					currentStepSequence.getId() != 0) {
			
				currentStepSequence = wssdBL.getWStepSequenceDefByPK(currentStepSequence.getId(), getCurrentUserId());

				recoverNullObjects();
				
				loadStepResponses();
								
				setCurrentStepValidResponses(UtilsVs.castStringToStringList(currentStepSequence.getValidResponses(), "|"));
				
			}
			
		} catch (WStepSequenceDefException e) {

			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".loadStepFromSequence() WStepSequenceDefException ..." };
			agregarMensaje("203", mensaje, params, FGPException.ERROR);
			e.printStackTrace();

		}
		
		
	}
	
	// dml 20120127
	public void changeFromStep(){
		
		if (currentStepResponseList != null && currentStepResponseList.size() != 0) {
			currentStepResponseList.clear();
		}

		if (currentStepValidResponses != null && currentStepValidResponses.size() != 0) {
			currentStepValidResponses.clear();
		}
		
		loadStepResponses();	
		
	}
	
	// dml 20120127
	public void loadStepResponses(){
		
		WStepDefBL wsdBL = new WStepDefBL();
		WStepDef wsd = new WStepDef();
		
		try {
			
			if (currentStepSequence != null && currentStepSequence.getFromStep() != null && 
					currentStepSequence.getFromStep().getId() != null &&
					currentStepSequence.getFromStep().getId() != 0) {
			
				wsd = wsdBL.getWStepDefByPK(
								currentStepSequence.getFromStep().getId(),
								currentWProcessDef.getProcess().getId(), // nes 2013088 - por agregado de step-data-field
								getCurrentUserId());
				
				currentStepResponseList = setToSelectItemConversor(wsd.getResponse());
				
			}
			
		} catch (WStepDefException e) {

			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".loadStepResponses() WStepDefException ..." };
			agregarMensaje("203", mensaje, params, FGPException.ERROR);
			e.printStackTrace();

		}

	}
	
	// dml 20120127
	public Integer getCurrentStepResponseListSize() { 
		
		return currentStepResponseList.size();
		
	}
	
	// dml 20120127
	private List<SelectItem> setToSelectItemConversor(Set<WStepResponseDef> responseSet){
		
		List<SelectItem> outList = new ArrayList<SelectItem>();
		WStepResponseDef wsrd = new WStepResponseDef();
		
		try {

			Iterator<WStepResponseDef> responseIt = responseSet.iterator();
			
			while (responseIt.hasNext()) {
				
				wsrd = responseIt.next();
				
				outList.add(new SelectItem(wsrd.getId(), wsrd.getName()));
				
			}
			
		} catch (Exception e) {
			logger.debug("Error trying to convert Set<WStepResponseDef> to SelectItem:"
								+e.getMessage()+" "+e.getCause()+" - "+e.getClass());
			outList=null;
		}
		
		return outList;
		
	}
		
	// dml 20120127
	public String selectValidStepResponse(){
		
		// No eliminar este método, asi hace submit en Ajax y guarda selectedWRoleDefList
		logger.debug("Item selected from selectedWRoleDefList");
		
		return null;
		
		
	}
	
	// PENDIENTE: metele un check que diga: "Show only selected roles"
	// PENDIENTE: checkEmail de CASTOR

	// public void setNewBeginStep() {
	// if ( this.currentWProcessDef!=null ) {
	// if ( this.currentWProcessDef.getBeginStep()!=null ) {
	// Integer id=this.currentWProcessDef.getBeginStep().getId();
	// this.currentWProcessDef.setBeginStep(new WStepDef(true));
	// this.currentWProcessDef.getBeginStep().setId(id);
	// }
	// }
	// }
	
	// dml 20120223
	public void detachEmail() {

		setShowHeaderMessage(false);
		messageStyle=normalMessageStyle();
		
		this.setCurrEmailAccount(new WEmailAccount(EMPTY_OBJECT));
		
		this.currentWProcessDef.setSystemEmailAccount(null);
		
		try {
			
			persistCurrentObject();
			
		} catch (WProcessDefException e) {

			messageStyle=errorMessageStyle();
			setShowHeaderMessage(true);
			String message = "WProcessDefException: Method detachEmail in WRoleDefBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WProcessDefException" };
			agregarMensaje("203", message, params, FGPException.WARN);
			logger.error(message);

		}		

	}
	
	
	public void addEmailAccount(){
		
		setShowHeaderMessage(false);
		messageStyle=normalMessageStyle();
		
		if (currEmailAccount.getId() != null) {
			
			try {
				currEmailAccount = new WEmailAccountBL().getWEmailAccountByPK(currEmailAccount.getId());

				this.currentWProcessDef.setSystemEmailAccount(currEmailAccount);
				
				persistCurrentObject();

			} catch (WEmailAccountException e) {

				messageStyle=errorMessageStyle();
				setShowHeaderMessage(true);
				String message = "WEmailAccountException: Method addEmailAccount in WRoleDefBean: "
									+ e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", "WEmailAccountException" };
				agregarMensaje("203", message, params, FGPException.WARN);
				logger.error(message);

			} catch (WProcessDefException e) {

				messageStyle=errorMessageStyle();
				setShowHeaderMessage(true);
				String message = "WProcessDefException: Method addEmailAccount in WRoleDefBean: "
									+ e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", "WProcessDefException" };
				agregarMensaje("203", message, params, FGPException.WARN);
				logger.error(message);

			}
			
		}
			
	}
	
	// dml 20120224
	public void searchEmails(){
		
		try {
			
			wEmailAccountList = new WEmailAccountBL().wEmailAccountFinder(emailNameFilter, emailNameFilter);

			setwEmailAccountListResults(wEmailAccountList.size());
		} catch (WEmailAccountException e) {

			e.printStackTrace();
		}
		
	}
	
	// dml 20120224
	public void resetCheckAccount(){
		
		this.checkingEmailAccount = "";
		
	}
	
	//rrl 20130729 clear the ExceptionList to display the last message (if no error)
	public void clearDisplayMessage(){
		this.setExceptionList(new ArrayList<FGPException>());
	}
	
	// dml 20120227
	public void checkEmailAccount(){
		
		flagValidate = true;

		setShowHeaderMessage(false);
		messageStyle=normalMessageStyle();
		
		Email email = new Email();
		
		if (currEmailAccount != null){
			
			flagValidate = false;
			
			email = setEmailParameters();
			
			try {
		
				enviar(email);
				
				String message = "The email has been sent";
				agregarMensaje(message);
				setShowHeaderMessage(true);
				
				flagValidate = true;
		
			} catch (SendEmailException e) {
		
				messageStyle=errorMessageStyle();
				setShowHeaderMessage(true);
				String message = "SendEmailException: Method checkEmailAccount in WRoleDefBean: "
									+ e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", "SendEmailException" };
				agregarMensaje("203", message, params, FGPException.WARN);
				logger.error(message);
		
			}
			
		}

		this.checkingEmailAccount = "";
		
	}
	
	private Email setEmailParameters(){

		Email checkingEmail = new Email();
		
		checkingEmail.setFrom(currEmailAccount.getEmail());
		checkingEmail.setIdFrom(currEmailAccount.getId());
		
		checkingEmail.setTo(checkingEmailAccount);
		checkingEmail.setCc(currEmailAccount.getEmail());
		checkingEmail.setSubject("Checking email account");
		checkingEmail.setBodyText("The email account "+currEmailAccount.getEmail()+" has a valid configuration.");
		checkingEmail.setPwd(currEmailAccount.getOutputPassword());
		
		return checkingEmail;	
		

		
	}
	
	public void enviar(Email email) throws SendEmailException{
		
		setShowHeaderMessage(false);
		messageStyle=normalMessageStyle();
		
				//valida la lista de correos destinatarios separados [,;]
			email.getListaTo().clear();
			email.getListaCC().clear();
			
			Pattern p = Pattern.compile("[,;]");
	        // Split input with the pattern
			
	        String[] resultTo= p.split(email.getTo());
	        String[] resultCC = p.split(email.getCc());
	        
	        for(String to: resultTo){
	        	if(!"".equals(to)){
	        		email.getListaTo().add(to);
	        	}
	        }
	        for(String cc: resultCC){
	        	if(!"".equals(cc)){
	        	email.getListaCC().add(cc);
	        	}
	        }
			
			SendEmailBL bl = new SendEmailBL();	
			//Mail contendra ficheros adjuntos
			//Limpiar ficheros del email 
			email.getFiles().clear();
			
			email.setContextPath( CONTEXTPATH ); // setea el path de la app por si el email es html para obtener los ficheros locales

			//rrl 20110803 por defecto TRUE pero no siempre p.e: en Ficha Proyecto en la pestaña Documentacion el usuario elije (Si / No) 
//			email.setGuardarEnBeeblos(true); // nes 20110429
			bl.enviar(email);
			
		// implementar exception propio	de envio de correo
	
	}

	// nes 20120203
	private String _defineReturn() {

		String ret = returnStatement;
		returnStatement="";
		
		return ret;
		
	}

	// dml 20120306
	private void loadArrivingNoticeEmailTemplatesCombo() {
		
		try {
			
			arrivingNoticeEmailTemplatesCombo = UtilsVs.castStringPairToSelectitem(new WEmailTemplatesBL().getComboList("Select a template", null));
		
		} catch (WEmailTemplatesException e) {
			
			messageStyle=errorMessageStyle();
			setShowHeaderMessage(true);
			String message = "WEmailTemplatesException: Method loadArrivingNoticeEmailTemplatesCombo in WProcessDefFormBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WEmailTemplatesException" };
			agregarMensaje("203", message, params, FGPException.WARN);
			logger.error(message);
	
		}
		
	}
	
	// dml 20120306
	private void setEmailTemplatesInObject(){
		
		// primero para comparar si hay cambios en alguno de los campos tengo q ver si son null y ponerlos a cero
		// para que no salte en caso de que vengan vacios de BD
		if (this.currentWProcessDef.getArrivingUserNoticeTemplate().getId() == null) {
			this.currentWProcessDef.getArrivingUserNoticeTemplate().setId(new Integer(0));
		}
		
		if (this.currentWProcessDef.getArrivingAdminNoticeTemplate().getId() == null) {
			this.currentWProcessDef.getArrivingAdminNoticeTemplate().setId(new Integer(0));
		}
		
		// para que no de problemas al cambiar directamente el id del objeto tengo que crear uno nuevo
		// y volverle a pasar el de la vista para que funcione bien 
		if ( !this.currentWProcessDef.getArrivingUserNoticeTemplate().getId()
				.equals(arrivingUserNoticeTemplate.getId())){
			this.currentWProcessDef.setArrivingUserNoticeTemplate(new WEmailTemplates());
			this.currentWProcessDef.getArrivingUserNoticeTemplate().setId(arrivingUserNoticeTemplate.getId());
		}
		
		if (!this.currentWProcessDef.getArrivingAdminNoticeTemplate().getId()
				.equals(arrivingAdminNoticeTemplate.getId())) {
			this.currentWProcessDef.setArrivingAdminNoticeTemplate(new WEmailTemplates());
			this.currentWProcessDef.getArrivingAdminNoticeTemplate().setId(arrivingAdminNoticeTemplate.getId());
		}
		
	}

	// dml 20120306
	private void loadEmailTemplateVariables(){
		
		WEmailTemplatesBL wetBL = new WEmailTemplatesBL();
		
		try {
			if (this.currentWProcessDef != null
					&& this.currentWProcessDef.getArrivingUserNoticeTemplate() != null
					&& this.currentWProcessDef.getArrivingUserNoticeTemplate().getId() != null) {
				arrivingUserNoticeTemplate = wetBL.getWEmailTemplatesByPK(this.currentWProcessDef.getArrivingUserNoticeTemplate().getId());
			} else {
				arrivingUserNoticeTemplate = new WEmailTemplates();
			}

			if (this.currentWProcessDef != null
					&& this.currentWProcessDef.getArrivingAdminNoticeTemplate() != null
					&& this.currentWProcessDef.getArrivingAdminNoticeTemplate().getId() != null) {
				arrivingAdminNoticeTemplate = wetBL.getWEmailTemplatesByPK(this.currentWProcessDef.getArrivingAdminNoticeTemplate().getId());
			} else {
				arrivingAdminNoticeTemplate = new WEmailTemplates();
			}
			
		} catch (WEmailTemplatesException e) {
			
			messageStyle=errorMessageStyle();
			setShowHeaderMessage(true);
			String message = "WEmailTemplatesException: Method loadEmailTemplateVariables in WProcessDefFormBean: "
								+ e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", "WEmailTemplatesException" };
			agregarMensaje("203", message, params, FGPException.WARN);
			logger.error(message);
	
		}

	}

	public Integer getCurrentProcessIdSelected() {
		return currentProcessIdSelected;
	}

	public void setCurrentProcessIdSelected(Integer currentProcessIdSelected) {
		this.currentProcessIdSelected = currentProcessIdSelected;
	}

	public List<SelectItem> getwProcessComboList() {
		return wProcessComboList;
	}

	public void setwProcessComboList(List<SelectItem> wProcessComboList) {
		this.wProcessComboList = wProcessComboList;
	}
	
	public List<WProcessDefLight> getRelatedProcessDefList() {
		return relatedProcessDefList;
	}

	public void setRelatedProcessDefList(List<WProcessDefLight> relatedProcessDefList) {
		this.relatedProcessDefList = relatedProcessDefList;
	}

	// dml 20130430
	public void _loadWProcessComboList(){
		
		try {
			
			this.wProcessComboList = UtilsVs.castStringPairToSelectitem(
					new WProcessHeadBL().getComboList("Select ...", null, getCurrentUserId()));
			
		} catch (WProcessHeadException e) {
			
			this.wProcessComboList = new ArrayList<SelectItem>();
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// dml 20130430
	public void setProcessInWProcessDef(){
		
		if (this.currentProcessIdSelected != null
				&& !this.currentProcessIdSelected.equals(0)){
			
			try {
				
				WProcessHead process = new WProcessHeadBL().getProcessHeadByPK(this.currentProcessIdSelected, null);
			
				Integer lastVersion = new WProcessDefBL().getLastVersionNumber(this.currentProcessIdSelected, getCurrentUserId());
				
				this.currentWProcessDef = new WProcessDef(EMPTY_OBJECT);

				this.currentWProcessDef.setProcess(process);
				
				this.currentWProcessDef.setVersion(lastVersion + 1);
				
			} catch (WProcessHeadException e) {
				
				messageStyle=errorMessageStyle();
				setShowHeaderMessage(true);
				String message = "WProcessDefException: Method setProcessInWProcessDef in WProcessDefFormBean: "
									+ e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", "WProcessDefException" };
				agregarMensaje("203", message, params, FGPException.WARN);
				logger.error(message);
		
			} catch (WProcessDefException e) {
				
				messageStyle=errorMessageStyle();
				setShowHeaderMessage(true);
				String message = "WProcessDefException: Method setProcessInWProcessDef in WProcessDefFormBean: "
									+ e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", "WProcessDefException" };
				agregarMensaje("203", message, params, FGPException.WARN);
				logger.error(message);
		
			}
			
		} else {
			
			this.currentWProcessDef = new WProcessDef(EMPTY_OBJECT);

		}

	}

	// dml 20120104
	public String loadWProcessForm() {

		return new WProcessDefUtil().loadWProcessFormBean(this.currentId);

	}
	
	// dml 20120104
	public String loadWProcessDefForm() {

		return new WProcessDefUtil().loadWProcessDefFormBean(this.currentId);

	}
	
	public void loadXmlMapAsTmp() {
		
		//rrl 20130729 When click "Refresh" button reloads the form by loadWProcessForm()
//		refreshForm=true;
		
		if (currentId != null
				&& !currentId.equals(0)){
			try {
				
				String xmlMapTmp = new WProcessDefBL().getProcessDefXmlMap(this.currentId, getCurrentUserId());
				
				String path = CONTEXTPATH + this._getRequestContextPath() + PROCESS_XML_MAP_LOCATION;
				File temp = new File(path);
				
				// if file doesnt exists, then create it
				if (!temp.exists()) {
					temp.createNewFile();
				}
				
				FileWriter fw = new FileWriter(temp.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(xmlMapTmp);
				bw.flush();
				bw.close();
				
			} catch (IOException e) {

				String message = e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", ".Error trying to create the xml map temp file for process: id=" + this.currentId};
				agregarMensaje("220", message, params, FGPException.ERROR);
				logger.error(message);
				
			} catch (WProcessDefException e) {

				String message = e.getMessage() + " - " + e.getCause();
				String params[] = { message + ",", ".Error trying to load the process xml map for id=" + this.currentId};
				agregarMensaje("220", message, params, FGPException.ERROR);
				logger.error(message);
				
			}
		}
		
	}

	public String getWorkflowEditorUrl(){
		return this._getRequestContextPath() + WORKFLOW_EDITOR_URI;
	}
	
	public String _getRequestContextPath() {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestContextPath().trim().replaceAll("\\\\", "/");
	}

	//rrl 20130729
	public boolean isFlagValidate() {
		return flagValidate;
	}

	public void setFlagValidate(boolean flagValidate) {
		this.flagValidate = flagValidate;
	}
/* dml 20130820 BORRAR
	public boolean isRefreshForm() {
		return refreshForm;
	}

	public void setRefreshForm(boolean refreshForm) {
		this.refreshForm = refreshForm;
	}
*/
	public boolean isVisibleButtonNewDataField() {
		return visibleButtonNewDataField;
	}

	public void setVisibleButtonNewDataField(boolean visibleButtonNewDataField) {
		this.visibleButtonNewDataField = visibleButtonNewDataField;
	}

	public boolean isVisibleButtonAdvancedConfiguration() {
		return visibleButtonAdvancedConfiguration;
	}

	public void setVisibleButtonAdvancedConfiguration(
			boolean visibleButtonAdvancedConfiguration) {
		this.visibleButtonAdvancedConfiguration = visibleButtonAdvancedConfiguration;
	}

	public WProcessDataField getwProcessDataFieldSelected() {
		return wProcessDataFieldSelected;
	}

	public void setwProcessDataFieldSelected(
			WProcessDataField wProcessDataFieldSelected) {
		this.wProcessDataFieldSelected = wProcessDataFieldSelected;
	}

	
	public void initializeDataFieldsAddNew() {
		
		this.wProcessDataFieldSelected = new WProcessDataField(EMPTY_OBJECT);
		this.wProcessDataFieldSelected.setActive(true); //rrl 20130807 Default TRUE to add a data fields
		
		this.wProcessDataFieldSelected.setDataType(new WDataType(TEXT_DATA_TYPE));
		visibleButtonNewDataField = false;
		
	}
	
	//rrl 20130730
	public List<WProcessDataField> getDataFieldList() {
		return dataFieldList;
	}

	public void setDataFieldList(List<WProcessDataField> dataFieldList) {
		this.dataFieldList = dataFieldList;
	}
	
	public void initNewDataFieldFormObjects() {
		
		this.wProcessDataFieldSelected = new WProcessDataField(EMPTY_OBJECT);
		this.wProcessDataFieldSelected.setActive(true); //rrl 20130807 Default TRUE to add a data field
		visibleButtonNewDataField = true;
		
	}

	public List<SelectItem> getDataTypes() {
		if (dataTypes==null) {
			loadDataTypes();
		}
		return dataTypes;
	}

	public void setDataTypes(List<SelectItem> dataTypes) {
		this.dataTypes = dataTypes;
	}
	
	//rrl 20130730
	public String saveNewDataField() throws TableManagerException {

		WProcessDataFieldBL wdfBL = new WProcessDataFieldBL();

		try {
			
			if (wProcessDataFieldSelected.getId() == null || 
					wProcessDataFieldSelected.getId().equals(0)) {
				
				wProcessDataFieldSelected
					.setProcessHeadId(currentWProcessDef.getProcess().getId());
				wProcessDataFieldSelected
					.setDataType(new WDataTypeBL()
											.getWDataTypeByPK(
													wProcessDataFieldSelected.getDataType().getId(), 
													this.getCurrentUserId()));

				wdfBL.add(wProcessDataFieldSelected, this.getCurrentUserId());
				
			} else {

				// update existing dataField
				wProcessDataFieldSelected
					.setDataType(new WDataTypeBL()
											.getWDataTypeByPK(
													wProcessDataFieldSelected.getDataType().getId(), 
													this.getCurrentUserId()));
				
				wdfBL.update(wProcessDataFieldSelected, this.getCurrentUserId());
			
			}
			
			reloadDataFieldList();
			// dml 20130822 - recargamos el proceso para tener los cambios en la lista
			this.loadCurrentWProcessDef();
			
			initNewDataFieldFormObjects();
					
			this.createWindowMessage("OK_MESSAGE", 
					"Campo correctamente guardado.");

		} catch (WProcessDataFieldException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".saveNewDataField() WProcessDataFieldException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			
			this.createWindowMessage("ERROR_MESSAGE", 
					"Error al guardar el campo: " + e.getMessage());
			
		} catch (WDataTypeException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".saveNewDataField() WDataTypeException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			
			this.createWindowMessage("ERROR_MESSAGE", 
					"Error al guardar el campo: " + e.getMessage());
		}

		return null;
	}
	
	public String cancelNewDataField() {

		initNewDataFieldFormObjects();
		
		return null;
	}

	public void reloadDataFieldList(){
		
		try {
			
 			this.dataFieldList = new WProcessDataFieldBL()
				.getWProcessDataFieldList(currentWProcessDef.getProcess().getId(), getCurrentUserId());
			
		} catch (WProcessDataFieldException e) {

			this.dataFieldList = new ArrayList<WProcessDataField>();
			
			String message = e.getMessage() + " - " + e.getCause();
			String params[] = { message + ",", ".Error trying to load current WProcessDef ..." };
			agregarMensaje("203", message, params, FGPException.ERROR);
			logger.error(message);
			
		}
		
	}
	
	public void loadDataField(){
		
		WProcessDataFieldBL wdfBL = new WProcessDataFieldBL();
		
		try {

			if (wProcessDataFieldSelected != null && wProcessDataFieldSelected.getId() != null && 
					wProcessDataFieldSelected.getId() != 0) {
			
				wProcessDataFieldSelected = wdfBL.getWProcessDataFieldByPK(wProcessDataFieldSelected.getId(), getCurrentUserId());

				visibleButtonNewDataField = false;
				
			}
			
		} catch (WProcessDataFieldException e) {

			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".loadDataField() WProcessDataFieldException ..." };
			agregarMensaje("203", mensaje, params, FGPException.ERROR);
			e.printStackTrace();

		}
		
	}
	
	public void deleteDataField(){
		
		WProcessDataFieldBL wdfBL = new WProcessDataFieldBL();
		
		try {
			
			if (wProcessDataFieldSelected != null && wProcessDataFieldSelected.getId() != null){
			
				wProcessDataFieldSelected = wdfBL.getWProcessDataFieldByPK(wProcessDataFieldSelected.getId(), getCurrentUserId());
						
				wdfBL.delete(wProcessDataFieldSelected, getCurrentUserId() );

				reloadDataFieldList();
				// dml 20130822 - recargamos el proceso para tener los cambios en la lista
				this.loadCurrentWProcessDef();
				
				initNewDataFieldFormObjects();
				
			}		
			
			this.createWindowMessage("OK_MESSAGE", 
					"Campo borrado correctamente.");

		} catch (WProcessDataFieldException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".deleteDataField() WStepSequenceDefException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
			this.createWindowMessage("ERROR_MESSAGE", ex1.getMessage());
			this.createWindowMessage("ERROR_MESSAGE", 
					"El campo indicado no se puede eliminar porque contiene datos. Se le ha colocado el atributo 'active'=false para evitar su uso en los procesos de aquí en adelante.");
			

			// try to set the datafield to "inactive" to avoid drop it because may be contains data
			try {
				wProcessDataFieldSelected.setActive(false);
				this.saveNewDataField();
			} catch (TableManagerException e) {
				logger.warn("deleteDataField: call to saveNewDataField in CATCH block says error:"+e.getMessage()+" - "+e.getCause());
				e.printStackTrace();
			}
			
		} 
		
		
	}

	// dml 20130821
	private void createWindowMessage(String messageType, String message) {

		if (messageType.equals("ERROR_MESSAGE")) {
			this.messageStyle = errorMessageStyle();
			logger.error(message);
		} else if (messageType.equals("OK_MESSAGE")) {
			this.messageStyle = normalMessageStyle();
			logger.info(message);
		}

		setShowHeaderMessage(true);
		this.agregarMensaje(message);

	}

	public void switchButtonAdvancedConfiguration() {
		visibleButtonAdvancedConfiguration=!visibleButtonAdvancedConfiguration;
		
		if (currentWProcessDef!=null
				&& currentWProcessDef.getProcess()!=null
				&& currentWProcessDef.getProcess().getManagedTableConfiguration()!=null
				&& currentWProcessDef.getProcess().getManagedTableConfiguration().getSchema()!=null
				&& currentWProcessDef.getProcess().getManagedTableConfiguration().getName()!=null ){


			try {
				
				TableManagerBL tmBL = new TableManagerBL();
				reccountTM = tmBL.countTableRecords(
								currentWProcessDef.getProcess().getManagedTableConfiguration().getSchema(),
								currentWProcessDef.getProcess().getManagedTableConfiguration().getName() );
				
				columnListTM = new ArrayList<org.beeblos.bpm.tm.model.Column>();
				// if table exists
				if (reccountTM>=0) {

					columnListTM = tmBL.getTableColumns(
											currentWProcessDef.getProcess().getManagedTableConfiguration().getSchema(),
											currentWProcessDef.getProcess().getManagedTableConfiguration().getName() );
				}				

			} catch (TableManagerException e) {
				String mensaje = e.getMessage() + " - " + e.getCause();
				String params[] = { mensaje + ",",
						".switchButtonAdvancedConfiguration() TableManagerException ..." };
				agregarMensaje("205", mensaje, params, FGPException.ERROR);
				e.printStackTrace();
				this.createWindowMessage("ERROR_MESSAGE", 
						"switchButtonAdvancedConfiguration() TableManagerException: " + e.getMessage());
			}
			

		} else {	
			columnListTM=null;
			reccountTM=null;
		}
		
	}

	// generates managed table for custom properties
	public void createManagedTable() {
		this.setShowHeaderMessage(false);
		
		String errors = _checkValidProcessConfiguration();
		if ( errors != null ) {
			this.createWindowMessage("ERROR_MESSAGE", errors);
			return;
		}
		
		String tableName = checkTableName(); // checks tablename and update currentWProcessDef if corresponds...
		reloadDataFieldList(); // refresh dataFieldList

		try {
			new TableManagerBL()
					.createManagedTable(
							currentWProcessDef.getProcess().getManagedTableConfiguration().getSchema(), 
							tableName, 
							dataFieldList);
		} catch (TableAlreadyExistsException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".createManagedTable() TableAlreadyExistsException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
			this.createWindowMessage("ERROR_MESSAGE", 
					"createManagedTable() TableAlreadyExistsException: " + e.getMessage());		
			
		} catch (TableManagerException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".createManagedTable() TableManagerException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
			this.createWindowMessage("ERROR_MESSAGE", 
					"createManagedTable() TableManagerException: " + e.getMessage());				
		}
	}
	
//	public void createManagedTable( TableManager tm ) { 
//		this.setShowHeaderMessage(false);
//
//		String errors = _checkValidProcessConfiguration();
//		if ( errors != null ) {
//			this.createWindowMessage("ERROR_MESSAGE", errors);
//			return;
//		}
//
//		String tableName = checkTableName(); // checks tablename and update currentWProcessDef if corresponds...
//		reloadDataFieldList(); // refresh dataFieldList
//		
//		try {
//			
//			tm.createTable(tableName,dataFieldList);
//			
//			System.out.println("table: "+tableName+" was created ...");
//			
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public void recreateManagedTable() {
		this.setShowHeaderMessage(false);

		String errors = _checkValidTableConfiguration();
		if ( errors != null ) {
			this.createWindowMessage("ERROR_MESSAGE", errors);
			return;
		}

		try {
			new TableManagerBL()
					.recreateManagedTable(
							currentWProcessDef.getProcess().getManagedTableConfiguration().getSchema(), 
							currentWProcessDef.getProcess().getManagedTableConfiguration().getName(), 
							dataFieldList);
		} catch (TableHasRecordsException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".recreateManagedTable() TableHasRecordsException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
			this.createWindowMessage("ERROR_MESSAGE", 
					"recreateManagedTable() TableHasRecordsException: " + e.getMessage());
		} catch (TableManagerException e) {
			String mensaje = e.getMessage() + " - " + e.getCause();
			String params[] = { mensaje + ",",
					".recreateManagedTable() TableManagerException ..." };
			agregarMensaje("205", mensaje, params, FGPException.ERROR);
			e.printStackTrace();
			this.createWindowMessage("ERROR_MESSAGE", 
					"recreateManagedTable() TableManagerException: " + e.getMessage());
		}
	}
	
//	public boolean checkTableHasRecords(TableManager tm){
//		
//		boolean returnValue = false;
//		
//		if (currentWProcessDef.getProcess().getId() != null
//				&& !currentWProcessDef.getProcess().getId().equals(0)){
//			
//			Integer qtyData;
//			try {
//
//				qtyData = tm.countNotNullRecords(
//						null, currentWProcessDef.getProcess().getManagedTableConfiguration().getName(), null);
//
//				if (qtyData != null
//						&& !qtyData.equals(0)){
//					returnValue = true;
//				}
//				
//			} catch (ClassNotFoundException ex1) {
//				String mensaje = ex1.getMessage() + " - " + ex1.getCause();
//				String params[] = { mensaje + ",",
//						".checkTableHasRecords() WStepSequenceDefException ..." };
//				agregarMensaje("205", mensaje, params, FGPException.ERROR);
//				ex1.printStackTrace();
//				this.createWindowMessage("ERROR_MESSAGE", 
//						"checkTableHasRecords() WStepSequenceDefException: " + ex1.getMessage());
//			} catch (SQLException ex1) {
//				String mensaje = ex1.getMessage() + " - " + ex1.getCause();
//				String params[] = { mensaje + ",",
//						".checkTableHasRecords() WStepSequenceDefException ..." };
//				agregarMensaje("205", mensaje, params, FGPException.ERROR);
//				ex1.printStackTrace();
//				this.createWindowMessage("ERROR_MESSAGE", 
//						"checkTableHasRecords() WStepSequenceDefException: " + ex1.getMessage());
//			}
//						
//		}
//		
//		return returnValue;
//
//	}

//	public void removeManagedTable( TableManager tm, String tableName ) { 
//		
//		String errors = _checkValidTableConfiguration();
//		if ( errors != null ) {
//			this.createWindowMessage("ERROR_MESSAGE", errors);
//			return;
//		}
//
//		try {
//			tm.removeTable(tableName);
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//	private boolean checkTableExists(TableManager tm) {
//		//currentWProcessDef.process.managedTableConfiguration.name
//		try {
//			Integer qtyRecords = 
//					tm.checkTableExists(
//							currentWProcessDef.getProcess().getManagedTableConfiguration().getSchema(),
//							currentWProcessDef.getProcess().getManagedTableConfiguration().getName() );
//
//			System.out.println("qty records:"+qtyRecords);
//			if (qtyRecords.equals(-1)) {
//				return false;
//			} 
//			return true;
//		} catch (ClassNotFoundException e) {
//			System.out.println("Error ClassNotFoundException "+e.getMessage()+" - "+e.getCause());
//		} catch (SQLException e) {
//			System.out.println("Error SQLException:"+e.getMessage()+" - "+e.getCause());
//		}
//		return false;
//	}

	
	private String _checkValidProcessConfiguration() {

		String returnValue = null;
		if (currentWProcessDef == null) {
			System.out.println("Error no hay un proceso cargado ...");
			returnValue = "Error no hay un proceso cargado ...";
		} else if (currentWProcessDef.getProcess() == null) {
			returnValue = "Error el proceso corriente con el id:"
					+ (currentWProcessDef.getId() != null ? currentWProcessDef.getId() : "null")
					+ " está inconsistente y no tiene su correspondiente head o no sepuede cargar ...";		
		} 
		System.out.println(returnValue);
		return returnValue;
	}

	
	private String _checkValidTableConfiguration() {
		
		String returnValue = _checkValidProcessConfiguration();
		if (returnValue == null
				&& currentWProcessDef.getProcess().getManagedTableConfiguration() == null) {
			returnValue = "Error managed table has no valid data ....";
		}
		System.out.println(returnValue);
		return returnValue;
	}

	private String checkTableName() {
		
		// if no managedTableData record exist then create it!
		if (currentWProcessDef!=null 
				&& currentWProcessDef.getProcess()!=null) {
			if (currentWProcessDef.getProcess().getManagedTableConfiguration()==null) {
				WProcessHeadManagedDataConfiguration managedTable = new WProcessHeadManagedDataConfiguration();
				managedTable.setHeadId(currentWProcessDef.getProcess().getId());
				managedTable.setCatalog(getStringProperty("bee_bpm_core.hibernate.connection.default_catalog"));
				managedTable.setSchema(getStringProperty("bee_bpm_core.hibernate.connection.default_catalog"));
				managedTable.setName(_ROOT_MANAGED_TABLE_NAME+currentWProcessDef.getProcess().getId());
//				managedTable.setWProcessHead(currentWProcessDef.getProcess());
				currentWProcessDef.getProcess().setManagedTableConfiguration(managedTable);
				
				// if exists managedTableData record check table name
			} else {
				if (currentWProcessDef.getProcess().getManagedTableConfiguration()==null 
						|| "".equals(currentWProcessDef.getProcess().getManagedTableConfiguration()) ) {
					logger.warn("TableName for process_head:"+currentWProcessDef.getProcess().getId()
							+" was null or not valid value ...");
					currentWProcessDef
							.getProcess()
							.getManagedTableConfiguration()
							.setName(_ROOT_MANAGED_TABLE_NAME+currentWProcessDef.getProcess().getId());
				}
			}
		}

		// update currentWProcessDef record
		this.update();
		return currentWProcessDef
				.getProcess()
				.getManagedTableConfiguration()
				.getName();
	}

	public List<SystemObject> getSystemObjectList() {

		List<SystemObject> sol = new ArrayList<SystemObject>();

		if (currentWProcessDef != null
				&& currentWProcessDef.getSystemObject() != null
				&& currentWProcessDef.getSystemObject().size() != 0) {

			sol = new ArrayList<SystemObject>(
					currentWProcessDef.getSystemObject());
		}

		return sol;

	}
	
}
