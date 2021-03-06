package org.beeblos.bpm.wc.taglib.bean;


import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;
import static com.sp.common.util.ConstantsCommon.OK_MESSAGE;
import static org.beeblos.bpm.core.util.Constants.WSTEPDEF_QUERY;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepHeadException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.HelperUtil;
import org.beeblos.bpm.wc.taglib.util.WStepDefUtil;

public class WStepDefQueryBean extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = 
			Logger.getLogger(WStepDefQueryBean.class);

	private String nameFilter;
	private String commentsFilter;
	private String instructionsFilter;
	
	private Integer currentUserId;
	

	private List<WStepDef> wStepDefList = new ArrayList<WStepDef>();

	private Integer nResults = 0;

	private String action;
	
	private Integer id;
	private WStepDef currentWStepDef; // dml 20130507 - object used to show information in the delete wprocessdef popup (currently, but it would be used by other methods)
	private Integer stepHeadId; // dml 20130506 - id from the "WStepHead" which is inside the current "WStepDef" (id=WStepDef.id)
	private boolean tmpDeletingWStepDefPopup;

	private TimeZone timeZone;

	public WStepDefQueryBean() {
		super();

		_init();

	}

	private void _init() {
		
		logger.debug("WStepDefQueryBean._init()");

		wStepDefList = new ArrayList<WStepDef>();

		this.nResults = 0;
		
		this.nameFilter = "";
		this.commentsFilter = "";
		this.instructionsFilter = "";
		
		this.id = 0;
		
		// reset session wStepDefFormBean
		HelperUtil
			.recreateBean(
					"wStepDefFormBean", "org.beeblos.bpm.wc.taglib.bean.WStepDefFormBean");
	}
	
	public String searchWStepDefs() {

		logger.debug("searchWStepDefs() - action: " + action);

		try {

			wStepDefList = (ArrayList<WStepDef>) new WStepDefBL()
					.finderWStepDef(nameFilter, commentsFilter, 
							instructionsFilter, getCurrentUserId(), true, action, null, null);

			nResults = wStepDefList.size();

		} catch (WStepDefException e) {
			e.printStackTrace();
		}

		return WSTEPDEF_QUERY;
	}


	public Integer getCurrentUserId() {
		if ( currentUserId== null ) {
			ContextoSeguridad cs = (ContextoSeguridad)
						getSession().getAttribute(SECURITY_CONTEXT);
			if (cs!=null) currentUserId=cs.getIdUsuario();
		}
		return currentUserId;
	}	

	public String loadWStepDefForm() {

		return new WStepDefUtil().loadWStepDefFormBean(id);

	}

	// dml 20130508
	public String loadWStepForm() {

		return new WStepDefUtil().loadWStepHeadFormBean(id);

	}
	

	// dml 20120110
	public String createNewWStepDef() {

		try {
			
			return new WStepDefUtil().createNewWStepDef(this.stepHeadId, WSTEPDEF_QUERY);
		
		} catch (WProcessDefException e) {
			String message = "WStepDefQueryBean.createNewWStepDef() WProcessDefException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
		return null;
		
	}

	public TimeZone getTimeZone() {
		// Si se pone GMT+1 pone mal el dia
		return java.util.TimeZone.getDefault();
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
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

	public String getInstructionsFilter() {
		return instructionsFilter;
	}

	public void setInstructionsFilter(String instructionsFilter) {
		this.instructionsFilter = instructionsFilter;
	}

	public List<WStepDef> getwStepDefList() {
		return wStepDefList;
	}

	public void setwStepDefList(List<WStepDef> wStepDefList) {
		this.wStepDefList = wStepDefList;
	}

	public Integer getnResults() {
		return nResults;
	}

	public void setnResults(Integer nResults) {
		this.nResults = nResults;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setCurrentUserId(Integer currentUserId) {
		this.currentUserId = currentUserId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public WStepDef getCurrentWStepDef() {
		return currentWStepDef;
	}

	public void setCurrentWStepDef(WStepDef currentWStepDef) {
		this.currentWStepDef = currentWStepDef;
	}

	public Integer getStepHeadId() {
		return stepHeadId;
	}

	public void setStepHeadId(Integer stepHeadId) {
		this.stepHeadId = stepHeadId;
	}

	public boolean isTmpDeletingWStepDefPopup() {
		return tmpDeletingWStepDefPopup;
	}

	public void setTmpDeletingWStepDefPopup(boolean tmpDeletingWStepDefPopup) {
		this.tmpDeletingWStepDefPopup = tmpDeletingWStepDefPopup;
	}

	// nes 20130508
	// force step cloning with all routes for all process ...
	public void cloneWStepDef() {
		Integer processId=null, processHeadId=null;
		cloneWStepDef(processId, processHeadId);
	}
	
	// nes 20130508
	public void cloneWStepDef(Integer processId, Integer processHeadId) {

		try {
			
			// cloning a step involves process in incoming and outgoing routes: each route (w_step_sequence_def)
			// must have your process_id. IF process_id is null then clone clones all routes for all process involving
			// cloned step ...
			Integer newId = 
					new WStepDefBL()
							.cloneWStepDef(this.id, this.stepHeadId, processId, processHeadId, getCurrentUserId());// nes 20130808 - por agregado de filtro
			
			this.searchWStepDefs();
			
			String message = "Step version id:"+this.id+" has a new cloned version with id:"+newId;
			super.createWindowMessage(OK_MESSAGE, message, null);
			logger.info(message);

		} catch (WStepDefException e) {
			String message = "WStepDefQueryBean.cloneWStepDef() WStepDefException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (WStepSequenceDefException e) {
			String message = "WStepDefQueryBean.cloneWStepDef() WStepSequenceDefException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (WStepHeadException e) {
			String message = "WStepDefQueryBean.cloneWStepDef() WStepHeadException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
	}

	// dml 20130508
	public String createNewWStepHead() {

		return new WStepDefUtil().createNewWStepHead(WSTEPDEF_QUERY);
		
	}
	
	// dml 20130508
	public String deleteWStepDef(){
		
		setShowHeaderMessage(true);

		if (this.id != null
				&& !this.id.equals(0)){
			
			try {
				
				new WStepDefBL().delete(this.id, null, getCurrentUserId()); // nes 20130808 - por agregado del filtro para step-data-field
				
				this.tmpDeletingWStepDefPopup = false;
				this.searchWStepDefs();
				
				String message = "The step '" + this.currentWStepDef.getStepHead().getName() 
						+ "' version: '" + this.currentWStepDef.getVersion() + "' has been correctly deleted";
				super.createWindowMessage(OK_MESSAGE, message, null);
				logger.info(message);
				
			} catch (WStepDefException e) {
				String message = e.getMessage() + " - " + e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (WStepWorkException e) {
				String message = e.getMessage() + " - " + e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (WStepHeadException e) {
				String message = e.getMessage() + " - " + e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (WProcessDefException e) {
				String message = e.getMessage() + " - " + e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (WStepSequenceDefException e) {
				String message = e.getMessage() + " - " + e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (WStepWorkSequenceException e) {
				String message = e.getMessage() + " - " + e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} 
			
		}
		
		return null;
		
	}
	
	// dml 20130508
	public void activateDelete(){
		
		tmpDeletingWStepDefPopup = true;
		
	}
	
	// dml 20130508
	public void loadWStepDefObject(){
		
		if (this.id != null
				&& !this.id.equals(0)){
			
			try {
				
				this.tmpDeletingWStepDefPopup = false;
				this.currentWStepDef = new WStepDefBL().getWStepDefByPK(this.id, null, getCurrentUserId());// nes 20130808 - por agregado del filtro para step-data-field
				
			} catch (WStepDefException e) {
				String message = "WStepDefQueryBean.loadWStepDefObject() WStepDefException: ";
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} 
			
		}
		
	}

}
