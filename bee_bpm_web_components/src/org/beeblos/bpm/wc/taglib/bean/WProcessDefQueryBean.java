package org.beeblos.bpm.wc.taglib.bean;


import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;
import static com.sp.common.util.ConstantsCommon.OK_MESSAGE;
import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;
import static org.beeblos.bpm.core.util.Constants.PROCESS_XML_MAP_LOCATION;
import static org.beeblos.bpm.core.util.Constants.WORKFLOW_EDITOR_URI;
import static org.beeblos.bpm.core.util.Constants.WORKFLOW_VIEW_URI;
import static org.beeblos.bpm.core.util.Constants.WPROCESSDEF_QUERY;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.beeblos.bpm.core.bl.DevelopmentBL;
import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.error.DevelopmentException;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessHeadException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepHeadException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.HelperUtil;
import org.beeblos.bpm.wc.taglib.util.WProcessDefUtil;

public class WProcessDefQueryBean extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = 
			Logger.getLogger(WProcessDefQueryBean.class);

	private Date initialInsertDateFilter;
	private Date finalInsertDateFilter;
	private boolean strictInsertDateFilter;
	
	private String nameFilter;
	private String commentsFilter;
	private String listZoneFilter;
	private String workZoneFilter;
	private String additionalZoneFilter;
	
	private Integer currentUserId;

	private List<WProcessDef> wProcessDefList = new ArrayList<WProcessDef>();

	private Integer nResults = 0;

	private String action; // la action de consulta p.e: altas mas recientes o
							// ultimas modificaciones

	private Integer id;
	private WProcessDef currentWProcessDef; // dml 20130507 - object used to show information in the delete wprocessdef popup (currently, but it would be used by other methods)
	private Integer processHeadId; // dml 20130506 - id from the "WProcessHead" which is inside the current "WProcessDef" (id=WProcessDef.id)
	private boolean tmpDeletingWProcessDefPopup;
	
	private TimeZone timeZone;
	
	private boolean tmpDeleteRelatedStepsPopup;

	private String messageStyle;
	
	// nes 20130903 
	/**
	 * properties to parametrize creation of new cloned process
	 */
	boolean startsNewVersionAt1=false; // if false new verion number will start at the same value of currentProcessDef (cloned process)
	boolean emptyRoleList=false; // if false the user and role list will be the same as cloned process
	boolean emptyUserList=false; 
	Integer createNewClonedSteps=0; // if 0 will reuse current steps, 1 will create a clone of each involved step, and 2 will clean step list
	boolean emptyWorkflowMap=false; // if false then carry current map, if false then will create an empty map
	
	public WProcessDefQueryBean() {
		super();

		_init();

	}

	private void _init() {
		
		logger.debug("WProcessDefQueryBean._init()");

		// dml 20120416
		this.searchWProcessDefs();

		this.nResults = 0;
		
		this.initialInsertDateFilter = null;
		this.finalInsertDateFilter = null;
		this.strictInsertDateFilter = false;

		this.nameFilter = "";
		this.commentsFilter = "";
		this.listZoneFilter = "";
		this.workZoneFilter = "";
		this.additionalZoneFilter = "";
		
		this.id = 0;
		this.processHeadId = 0;
		
		this.currentWProcessDef = new WProcessDef(EMPTY_OBJECT);
		
		this.tmpDeleteRelatedStepsPopup = false;
		
		// reset session wProcessDefFormBean
		HelperUtil
			.recreateBean(
					"wProcessDefFormBean", "org.beeblos.bpm.wc.taglib.bean.WProcessDefFormBean");
		HelperUtil
			.recreateBean(
				"wStepDefFormBean", "org.beeblos.bpm.wc.taglib.bean.WStepDefFormBean");
	}

	public List<WProcessDef> getwProcessDefList() {

		if (wProcessDefList == null || wProcessDefList.isEmpty()) {
			nResults = 0;
		}
		return wProcessDefList;
	}

	public void setwProcessDefList(List<WProcessDef> wProcessDefList) {
		this.wProcessDefList = wProcessDefList;
	}


	public Integer getnResults() {
		return nResults;
	}

	public void setnResults(Integer nResults) {
		this.nResults = nResults;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProcessHeadId() {
		return processHeadId;
	}

	public void setProcessHeadId(Integer processHeadId) {
		this.processHeadId = processHeadId;
	}

	public TimeZone getTimeZone() {
		// Si se pone GMT+1 pone mal el dia
		return java.util.TimeZone.getDefault();
	}

	public String searchWProcessDefs() {

		logger.debug("searchWProcessDefs() - action: " + action);

		try {

			wProcessDefList = (ArrayList<WProcessDef>) new WProcessDefBL()
					.finderWProcessDefLight(initialInsertDateFilter, finalInsertDateFilter, strictInsertDateFilter, 
							nameFilter, commentsFilter, listZoneFilter, workZoneFilter, additionalZoneFilter, 
							getCurrentUserId(), true, action, getCurrentUserId());

			nResults = wProcessDefList.size();

		} catch (WProcessDefException e) {
			String message = "WProcessDefQueryBean.searchWProcessDefs() WProcessDefException: ";
					
			super.createWindowMessage(ERROR_MESSAGE, message, e);			
		}

		return WPROCESSDEF_QUERY;
	}

	public Date getInitialInsertDateFilter() {
		return initialInsertDateFilter;
	}

	public void setInitialInsertDateFilter(Date initialInsertDateFilter) {
		this.initialInsertDateFilter = initialInsertDateFilter;
	}

	public Date getFinalInsertDateFilter() {
		return finalInsertDateFilter;
	}

	public void setFinalInsertDateFilter(Date finalInsertDateFilter) {
		this.finalInsertDateFilter = finalInsertDateFilter;
	}

	public boolean isStrictInsertDateFilter() {
		return strictInsertDateFilter;
	}

	public void setStrictInsertDateFilter(boolean strictInsertDateFilter) {
		this.strictInsertDateFilter = strictInsertDateFilter;
	}

	public String getNameFilter() {
		return nameFilter;
	}

	public void setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
	}

	public String getCommentsFilter() {
		return commentsFilter;
	}

	public void setCommentsFilter(String commentsFilter) {
		this.commentsFilter = commentsFilter;
	}

	public String getListZoneFilter() {
		return listZoneFilter;
	}

	public void setListZoneFilter(String listZoneFilter) {
		this.listZoneFilter = listZoneFilter;
	}

	public String getWorkZoneFilter() {
		return workZoneFilter;
	}

	public void setWorkZoneFilter(String workZoneFilter) {
		this.workZoneFilter = workZoneFilter;
	}

	public String getAdditionalZoneFilter() {
		return additionalZoneFilter;
	}

	public void setAdditionalZoneFilter(String additionalZoneFilter) {
		this.additionalZoneFilter = additionalZoneFilter;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	

	public boolean isTmpDeleteRelatedStepsPopup() {
		return tmpDeleteRelatedStepsPopup;
	}

	public void setTmpDeleteRelatedStepsPopup(boolean tmpDeleteRelatedStepsPopup) {
		this.tmpDeleteRelatedStepsPopup = tmpDeleteRelatedStepsPopup;
	}

	public WProcessDef getCurrentWProcessDef() {
		return currentWProcessDef;
	}

	public void setCurrentWProcessDef(WProcessDef currentWProcessDef) {
		this.currentWProcessDef = currentWProcessDef;
	}

	public Integer getCurrentUserId() {
		if ( currentUserId== null ) {
			ContextoSeguridad cs = (ContextoSeguridad)
						getSession().getAttribute(SECURITY_CONTEXT);
			if (cs!=null) currentUserId=cs.getIdUsuario();
		}
		return currentUserId;
	}	

	// dml 20120104
	public String loadWProcessDefForm() {

		return new WProcessDefUtil().loadWProcessDefFormBean(id);

	}
	
	// dml 20120104
	public String loadWProcessHeadForm() {

		if (this.processHeadId != null
				&& !this.processHeadId.equals(0)){
			return new WProcessDefUtil().loadWProcessHeadFormBean(this.processHeadId, WPROCESSDEF_QUERY);
		}
		
		return null;

	}
	
	public boolean isTmpDeletingWProcessDefPopup() {
		return tmpDeletingWProcessDefPopup;
	}

	public void setTmpDeletingWProcessDefPopup(boolean tmpDeletingWProcessDefPopup) {
		this.tmpDeletingWProcessDefPopup = tmpDeletingWProcessDefPopup;
	}

	public String getMessageStyle() {
		return messageStyle;
	}

	public void setMessageStyle(String messageStyle) {
		this.messageStyle = messageStyle;
	}

	//rrl 20120117
	public String generateXmlWProcessDef() {

		return new WProcessDefUtil().generateXmlWProcessDef(this.id);
		
	}

	// dml 20120110
	public String createNewProcessVersion() {

		try {
			
			return new WProcessDefUtil().createNewProcessDef(
					this.processHeadId, WPROCESSDEF_QUERY);
			
		} catch (WProcessDefException e) {
			String message = "WProcessDefQueryBean.createNewProcessVersion() WProcessDefException: ";
					
			super.createWindowMessage(ERROR_MESSAGE, message, e);			
		}
		
		return null;
		
	}

	// nes 20130502
	public void cloneWProcessDef() {

		try {
			
			Integer newId = 
					new WProcessDefBL().cloneWProcessDef(
								"",						// new process name (null > 'cloned'+current name)
								this.id, 
								this.processHeadId,
								startsNewVersionAt1, 	// default true  
								emptyRoleList,			// default false
								emptyUserList,	  
								createNewClonedSteps,	// default 0  
								emptyWorkflowMap,		// default false
								getCurrentUserId());
			
			this.searchWProcessDefs();
			
			String message = "Process version id:"+this.id+" has a new cloned version with id:"+newId;
			this.messageStyle = normalMessageStyle();
			super.createWindowMessage(OK_MESSAGE, message, null);			
			logger.info(message);

		} catch (WProcessDefException e) {
			String message = "WProcessDefQueryBean.cloneWProcessDef() WProcessDefException: ";
					
			super.createWindowMessage(ERROR_MESSAGE, message, e);			
		} catch (WStepSequenceDefException e) {
			String message = "WProcessDefQueryBean.cloneWProcessDef() WStepSequenceDefException: ";
					
			super.createWindowMessage(ERROR_MESSAGE, message, e);			
		} catch (WProcessHeadException e) {
			String message = "WProcessDefQueryBean.cloneWProcessDef() WProcessDefException: ";
					
			super.createWindowMessage(ERROR_MESSAGE, message, e);			
		}
		
	}

	// dml 20120110
	public String createNewWProcessHead() {

		return new WProcessDefUtil().createNewWProcessHead(WPROCESSDEF_QUERY);
		
	}
	
	// dml 20130507
	public String deleteWProcessDef(){
		
		setShowHeaderMessage(true);

		if (this.id != null
				&& !this.id.equals(0)){
			
			try {
				String processName = this.currentWProcessDef.getProcess().getName();

				new WProcessDefBL().delete(this.id, this.tmpDeleteRelatedStepsPopup, getCurrentUserId());
				
				this.tmpDeleteRelatedStepsPopup = false;
				this.tmpDeletingWProcessDefPopup = false;
				this.searchWProcessDefs();
				
				String message = "The process '" + processName + "' has been correctly deleted";
				this.messageStyle = normalMessageStyle();
				super.createWindowMessage(OK_MESSAGE, message, null);			
				logger.info(message);
				
			} catch (WProcessWorkException e) {
				String message = "WProcessDefQueryBean.deleteWProcessDef() WProcessWorkException: ";
						
				super.createWindowMessage(ERROR_MESSAGE, message, e);			
			} catch (WProcessDefException e) {
				String message = "WProcessDefQueryBean.deleteWProcessDef() WProcessDefException: ";
						
				super.createWindowMessage(ERROR_MESSAGE, message, e);			
			} catch (WStepSequenceDefException e) {
				String message = "WProcessDefQueryBean.deleteWProcessDef() WStepSequenceDefException: ";
						
				super.createWindowMessage(ERROR_MESSAGE, message, e);			
			} catch (WStepWorkException e) {
				String message = "WProcessDefQueryBean.deleteWProcessDef() WStepWorkException: ";
						
				super.createWindowMessage(ERROR_MESSAGE, message, e);			
			} catch (WProcessHeadException e) {
				String message = "WProcessDefQueryBean.deleteWProcessDef() WProcessHeadException: ";
						
				super.createWindowMessage(ERROR_MESSAGE, message, e);			
			} catch (WStepDefException e) {
				String message = "WProcessDefQueryBean.deleteWProcessDef() WStepDefException: ";
						
				super.createWindowMessage(ERROR_MESSAGE, message, e);			
			} catch (WStepHeadException e) {
				String message = "WProcessDefQueryBean.deleteWProcessDef() WStepHeadException: ";
						
				super.createWindowMessage(ERROR_MESSAGE, message, e);			
			}
			
		}
		
		return null;
		
	}
	
	// dml 20130507
	public void activateDelete(){
		
		tmpDeletingWProcessDefPopup = true;
		
	}
	
	// dml 20130507
	public void loadWProcessDefObject(){
		
		if (this.id != null
				&& !this.id.equals(0)){
			
			try {
				
				this.tmpDeletingWProcessDefPopup = false;
				this.currentWProcessDef = new WProcessDefBL().getWProcessDefByPK(this.id, getCurrentUserId());
				
			} catch (WProcessDefException e) {
				String message = "WProcessDefQueryBean.loadWProcessDefObject() WProcessDefException: ";
						
				super.createWindowMessage(ERROR_MESSAGE, message, e);			
			} catch (WStepSequenceDefException e) {
				String message = "WProcessDefQueryBean.loadWProcessDefObject() WStepSequenceDefException: ";
						
				super.createWindowMessage(ERROR_MESSAGE, message, e);			
			}
			
		}
		
	}
	
	public void loadXmlMapAsTmp() {
		
		if (this.id != null
				&& !this.id.equals(0)){
			try {
				
				String xmlMapTmp = new WProcessDefBL().getProcessDefXmlMap(this.id, getCurrentUserId());
				
				String path = super.getContextPath() + super.getRequestContextPath() + PROCESS_XML_MAP_LOCATION;
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
				String message = "WProcessDefQueryBean.loadXmlMapAsTmp() IOException: ";
						
				super.createWindowMessage(ERROR_MESSAGE, message, e);			
			} catch (WProcessDefException e) {
				String message = "WProcessDefQueryBean.loadXmlMapAsTmp() WProcessDefException: ";
						
				super.createWindowMessage(ERROR_MESSAGE, message, e);			
			}
		}
		
	}

	public String getWorkflowEditorUrl(){
		return super.getRequestContextPath() + WORKFLOW_EDITOR_URI;
	}
	
	public String getWorkflowViewXmlMapUrl(){
		return super.getRequestContextPath() + WORKFLOW_VIEW_URI;
	}
	
	/**
	 * @author dmuleiro - 20130830
	 * 
	 * Deletes all the process information like "process works", "step works", "step_sequence_works" and the obsolete
	 * steps and step sequences related. 
	 *
	 * @return void
	 * 
	 */
	public void purgeWProcessDef() {
		
		if (this.id != null
				&& !this.id.equals(0)){

			try {
				
				WProcessDef processDef = 
						new WProcessDefBL().getWProcessDefByPK(this.id, this.currentUserId);

				new DevelopmentBL().purgeProcessDef(processDef, currentUserId);

			} catch (WProcessDefException e) {
				String message = "WProcessDefQueryBean.purgeWProcessDef() WProcessDefException: ";
						
				super.createWindowMessage(ERROR_MESSAGE, message, e);			
			} catch (WStepSequenceDefException e) {
				String message = "WProcessDefQueryBean.purgeWProcessDef() WStepSequenceDefException: ";
						
				super.createWindowMessage(ERROR_MESSAGE, message, e);			
			} catch (DevelopmentException e) {
				String message = "WProcessDefQueryBean.purgeWProcessDef() DevelopmentException: ";
						
				super.createWindowMessage(ERROR_MESSAGE, message, e);			
			}
		
		}
		
	}

}
