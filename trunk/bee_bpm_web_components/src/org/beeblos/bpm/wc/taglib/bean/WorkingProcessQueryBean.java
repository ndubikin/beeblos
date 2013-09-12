package org.beeblos.bpm.wc.taglib.bean;

import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;
import static org.beeblos.bpm.core.util.Constants.FAIL;
import static org.beeblos.bpm.core.util.Constants.PENDING;
import static org.beeblos.bpm.core.util.Constants.PROCESSING;
import static org.beeblos.bpm.core.util.Constants.PROCESS_XML_MAP_LOCATION;
import static org.beeblos.bpm.core.util.Constants.WORKFLOW_VIEW_URI;
import static org.beeblos.bpm.core.util.Constants.WORKINGPROCESS_QUERY;
import static org.beeblos.bpm.core.util.Constants.WORKINGSTEPS_QUERY;
import static org.beeblos.bpm.core.util.Constants.WORKINGWORKS_QUERY;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WProcessWorkBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepWorkBL;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.bl.WorkflowEditorMxBL;
import org.beeblos.bpm.core.error.CantLockTheStepException;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepLockedByAnotherUserException;
import org.beeblos.bpm.core.error.WStepNotLockedException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.noper.ProcessWorkLight;
import org.beeblos.bpm.core.model.noper.StepWorkLight;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;
import org.beeblos.bpm.wc.taglib.bean.util.TareaWorkflowUtil;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.HelperUtil;
import org.beeblos.bpm.wc.taglib.util.WProcessDefUtil;
import org.beeblos.bpm.wc.taglib.util.WProcessWorkUtil;
import org.beeblos.bpm.wc.taglib.util.WStepDefUtil;
import org.beeblos.bpm.wc.taglib.util.WStepWorkUtil;
import org.xml.sax.SAXException;

import com.sp.common.jsf.util.UtilsVs;



public class WorkingProcessQueryBean extends CoreManagedBean {
	
	private static final long serialVersionUID = 1L;

	private static final Logger logger = 
			Logger.getLogger(WorkingProcessQueryBean.class);

	private Integer currentUserId;

	private List<WProcessDefLight> wProcessDefLightList;
	private List<ProcessWorkLight> processWorkLightList;
	private List<StepWorkLight> stepWorkLightList;

	private Integer nResults;
	private Integer nStepResults;
	private Integer nWorkResults;
	
	private boolean onlyActiveWorkingProcessesFilter;
	private String referenceFilter;
	
	// dml 20120124
	private Integer idWorkFilter;
	
	private Integer processIdFilter;
	private Integer stepIdFilter;
	
	private String stepTypeFilter;
	
	private String processNameFilter;

	private Date initialProductionDateFilter;
	private Date finalProductionDateFilter;
	private boolean strictProductionDateFilter;
	
	private Date initialStartedDateFilter;
	private Date finalStartedDateFilter;
	private boolean estrictStartedDateFilter;
	
	private Date initialFinishedDateFilter;
	private Date finalFinishedDateFilter;
	private boolean estrictFinishedDateFilter;
	
	private Date initialArrivingDateFilter;
	private Date finalArrivingDateFilter;
	private boolean estrictArrivingDateFilter;
	
	private Date initialOpenedDateFilter;
	private Date finalOpenedDateFilter;
	private boolean estrictOpenedDateFilter;
	
	private Date initialDeadlineDateFilter;
	private Date finalDeadlineDateFilter;
	private boolean estrictDeadlineDateFilter;
	
	private Date initialDecidedDateFilter;
	private Date finalDecidedDateFilter;
	private boolean estrictDecidedDateFilter;
	
	private Integer productionUserFilter;
	
	private String action; 
	
	private Integer currentProcessId;

	private TimeZone timeZone;
	
	// dml 20120123
	private Integer idStep;
	private Integer idStepWork;
	private boolean stepLocked;
	private Integer stepLocker;
	private WStepWork currentWStepWork;
	
	// dml 20120125
	private String workTypeFilter;
	
	// dml 20120130
	private Integer idWork;

	public WorkingProcessQueryBean() {
		super();

		_init();

	}

	private void _init() {
		
		logger.debug("WorkingProcessQueryBean._init()");

		this.wProcessDefLightList = new ArrayList<WProcessDefLight>();
		this.stepWorkLightList = new ArrayList<StepWorkLight>();
		this.processWorkLightList = new ArrayList<ProcessWorkLight>();

		this.nResults = 0;
		this.nStepResults = 0;
		this.nWorkResults = 0;
				
		this.onlyActiveWorkingProcessesFilter = true;
		
		this.referenceFilter = "";
		this.idWorkFilter = 0;

		this.processIdFilter = null;
		this.stepIdFilter = null;
		
		this.stepTypeFilter = PENDING;
		
		// dml 20120125
		this.workTypeFilter = PROCESSING;
		
		this.processNameFilter= "";

		this.initialProductionDateFilter = null;
		this.finalProductionDateFilter = null;
		this.strictProductionDateFilter = false;
		
		this.initialStartedDateFilter = null;
		this.finalStartedDateFilter = null;
		this.estrictStartedDateFilter = false;
		
		this.initialFinishedDateFilter = null;
		this.finalFinishedDateFilter = null;
		this.estrictFinishedDateFilter = false;
		
		this.initialArrivingDateFilter = null;
		this.finalArrivingDateFilter = null;
		this.estrictArrivingDateFilter = false;
		
		this.initialOpenedDateFilter = null;
		this.finalOpenedDateFilter = null;
		this.estrictOpenedDateFilter = false;
		
		this.initialDeadlineDateFilter = null;
		this.finalDeadlineDateFilter = null;
		this.estrictDeadlineDateFilter = false;
		
		this.initialDecidedDateFilter = null;
		this.finalDecidedDateFilter = null;
		this.estrictDecidedDateFilter = false;
		
		this.productionUserFilter = 0;

		this.currentProcessId = 0;
		
		// dml 20120123
		this.idStep = 0;
		this.idStepWork = 0;
		this.stepLocked = false;
		this.stepLocker = 0;
		
		HelperUtil
			.recreateBean(
					"wProcessDefFormBean", "org.beeblos.bpm.wc.taglib.bean.WProcessDefFormBean");
		HelperUtil
			.recreateBean(
				"wStepDefFormBean", "org.beeblos.bpm.wc.taglib.bean.WStepDefFormBean");
	}

	public List<WProcessDefLight> getwProcessDefLightList() {

		if (wProcessDefLightList == null || wProcessDefLightList.isEmpty()) {
			nResults = 0;
		}
		return wProcessDefLightList;
	}

	public void setwProcessDefLightList(List<WProcessDefLight> wProcessDefLightList) {
		this.wProcessDefLightList = wProcessDefLightList;
	}

	public List<ProcessWorkLight> getProcessWorkLightList() {
		
		if (processWorkLightList == null || processWorkLightList.isEmpty()) {
			nWorkResults = 0;
		}
		
		return processWorkLightList;
	}

	public void setProcessWorkLightList(List<ProcessWorkLight> processWorkLightList) {
		this.processWorkLightList = processWorkLightList;
	}

	public List<StepWorkLight> getStepWorkLightList() {
		
		if (stepWorkLightList == null || stepWorkLightList.isEmpty()) {
			nStepResults = 0;
		}
		
		return stepWorkLightList;
	}

	public void setStepWorkLightList(List<StepWorkLight> stepWorkLightList) {
		this.stepWorkLightList = stepWorkLightList;
	}

	public Integer getnResults() {
		return nResults;
	}

	public void setnResults(Integer nResults) {
		this.nResults = nResults;
	}

	public Integer getnStepResults() {
		return nStepResults;
	}

	public void setnStepResults(Integer nStepResults) {
		this.nStepResults = nStepResults;
	}

	public Integer getnWorkResults() {
		return nWorkResults;
	}

	public void setnWorkResults(Integer nWorkResults) {
		this.nWorkResults = nWorkResults;
	}

	public boolean isOnlyActiveWorkingProcessesFilter() {
		return onlyActiveWorkingProcessesFilter;
	}

	public void setOnlyActiveWorkingProcessesFilter(
			boolean onlyActiveWorkingProcessesFilter) {
		this.onlyActiveWorkingProcessesFilter = onlyActiveWorkingProcessesFilter;
	}

	public String getReferenceFilter() {
		return referenceFilter;
	}

	public void setReferenceFilter(String referenceFilter) {
		this.referenceFilter = referenceFilter;
	}

	public Integer getIdWorkFilter() {
		return idWorkFilter;
	}

	public void setIdWorkFilter(Integer idWorkFilter) {
		this.idWorkFilter = idWorkFilter;
	}

	public Integer getProcessIdFilter() {
		return processIdFilter;
	}

	public void setProcessIdFilter(Integer processIdFilter) {
		this.processIdFilter = processIdFilter;
	}

	public Integer getStepIdFilter() {
		return stepIdFilter;
	}

	public void setStepIdFilter(Integer stepIdFilter) {
		this.stepIdFilter = stepIdFilter;
	}

	public String getStepTypeFilter() {
		return stepTypeFilter;
	}

	public void setStepTypeFilter(String stepTypeFilter) {
		this.stepTypeFilter = stepTypeFilter;
	}

	public String getProcessNameFilter() {
		return processNameFilter;
	}

	public void setProcessNameFilter(String processNameFilter) {
		this.processNameFilter = processNameFilter;
	}

	public Date getInitialProductionDateFilter() {
		return initialProductionDateFilter;
	}

	public void setInitialProductionDateFilter(Date initialProductionDateFilter) {
		this.initialProductionDateFilter = initialProductionDateFilter;
	}

	public Date getFinalProductionDateFilter() {
		return finalProductionDateFilter;
	}

	public void setFinalProductionDateFilter(Date finalProductionDateFilter) {
		this.finalProductionDateFilter = finalProductionDateFilter;
	}

	public boolean isStrictProductionDateFilter() {
		return strictProductionDateFilter;
	}

	public void setStrictProductionDateFilter(boolean strictProductionDateFilter) {
		this.strictProductionDateFilter = strictProductionDateFilter;
	}

	public Date getInitialStartedDateFilter() {
		return initialStartedDateFilter;
	}

	public void setInitialStartedDateFilter(Date initialStartedDateFilter) {
		this.initialStartedDateFilter = initialStartedDateFilter;
	}

	public Date getFinalStartedDateFilter() {
		return finalStartedDateFilter;
	}

	public void setFinalStartedDateFilter(Date finalStartedDateFilter) {
		this.finalStartedDateFilter = finalStartedDateFilter;
	}

	public boolean isEstrictStartedDateFilter() {
		return estrictStartedDateFilter;
	}

	public void setEstrictStartedDateFilter(boolean estrictStartedDateFilter) {
		this.estrictStartedDateFilter = estrictStartedDateFilter;
	}

	public Date getInitialFinishedDateFilter() {
		return initialFinishedDateFilter;
	}

	public void setInitialFinishedDateFilter(Date initialFinishedDateFilter) {
		this.initialFinishedDateFilter = initialFinishedDateFilter;
	}

	public Date getFinalFinishedDateFilter() {
		return finalFinishedDateFilter;
	}

	public void setFinalFinishedDateFilter(Date finalFinishedDateFilter) {
		this.finalFinishedDateFilter = finalFinishedDateFilter;
	}

	public boolean isEstrictFinishedDateFilter() {
		return estrictFinishedDateFilter;
	}

	public void setEstrictFinishedDateFilter(boolean estrictFinishedDateFilter) {
		this.estrictFinishedDateFilter = estrictFinishedDateFilter;
	}

	public Date getInitialArrivingDateFilter() {
		return initialArrivingDateFilter;
	}

	public void setInitialArrivingDateFilter(Date initialArrivingDateFilter) {
		this.initialArrivingDateFilter = initialArrivingDateFilter;
	}

	public Date getFinalArrivingDateFilter() {
		return finalArrivingDateFilter;
	}

	public void setFinalArrivingDateFilter(Date finalArrivingDateFilter) {
		this.finalArrivingDateFilter = finalArrivingDateFilter;
	}

	public boolean isEstrictArrivingDateFilter() {
		return estrictArrivingDateFilter;
	}

	public void setEstrictArrivingDateFilter(boolean estrictArrivingDateFilter) {
		this.estrictArrivingDateFilter = estrictArrivingDateFilter;
	}

	public Date getInitialOpenedDateFilter() {
		return initialOpenedDateFilter;
	}

	public void setInitialOpenedDateFilter(Date initialOpenedDateFilter) {
		this.initialOpenedDateFilter = initialOpenedDateFilter;
	}

	public Date getFinalOpenedDateFilter() {
		return finalOpenedDateFilter;
	}

	public void setFinalOpenedDateFilter(Date finalOpenedDateFilter) {
		this.finalOpenedDateFilter = finalOpenedDateFilter;
	}

	public boolean isEstrictOpenedDateFilter() {
		return estrictOpenedDateFilter;
	}

	public void setEstrictOpenedDateFilter(boolean estrictOpenedDateFilter) {
		this.estrictOpenedDateFilter = estrictOpenedDateFilter;
	}

	public Date getInitialDeadlineDateFilter() {
		return initialDeadlineDateFilter;
	}

	public void setInitialDeadlineDateFilter(Date initialDeadlineDateFilter) {
		this.initialDeadlineDateFilter = initialDeadlineDateFilter;
	}

	public Date getFinalDeadlineDateFilter() {
		return finalDeadlineDateFilter;
	}

	public void setFinalDeadlineDateFilter(Date finalDeadlineDateFilter) {
		this.finalDeadlineDateFilter = finalDeadlineDateFilter;
	}

	public boolean isEstrictDeadlineDateFilter() {
		return estrictDeadlineDateFilter;
	}

	public void setEstrictDeadlineDateFilter(boolean estrictDeadlineDateFilter) {
		this.estrictDeadlineDateFilter = estrictDeadlineDateFilter;
	}

	public Date getInitialDecidedDateFilter() {
		return initialDecidedDateFilter;
	}

	public void setInitialDecidedDateFilter(Date initialDecidedDateFilter) {
		this.initialDecidedDateFilter = initialDecidedDateFilter;
	}

	public Date getFinalDecidedDateFilter() {
		return finalDecidedDateFilter;
	}

	public void setFinalDecidedDateFilter(Date finalDecidedDateFilter) {
		this.finalDecidedDateFilter = finalDecidedDateFilter;
	}

	public boolean isEstrictDecidedDateFilter() {
		return estrictDecidedDateFilter;
	}

	public void setEstrictDecidedDateFilter(boolean estrictDecidedDateFilter) {
		this.estrictDecidedDateFilter = estrictDecidedDateFilter;
	}

	public Integer getProductionUserFilter() {
		return productionUserFilter;
	}

	public void setProductionUserFilter(Integer productionUserFilter) {
		this.productionUserFilter = productionUserFilter;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getCurrentProcessId() {
		return currentProcessId;
	}

	public void setCurrentProcessId(Integer currentProcessId) {
		this.currentProcessId = currentProcessId;
	}

	public TimeZone getTimeZone() {
		// Si se pone GMT+1 pone mal el dia
		return java.util.TimeZone.getDefault();
	}

	public boolean isStepLocked() {
		return stepLocked;
	}

	public void setStepLocked(boolean stepLocked) {
		this.stepLocked = stepLocked;
	}

	public Integer getStepLocker() {
		return stepLocker;
	}

	public void setStepLocker(Integer stepLocker) {
		this.stepLocker = stepLocker;
	}

	public WStepWork getCurrentWStepWork() {
		return currentWStepWork;
	}

	public void setCurrentWStepWork(WStepWork currentWStepWork) {
		this.currentWStepWork = currentWStepWork;
	}

	public String getWorkTypeFilter() {
		return workTypeFilter;
	}

	public void setWorkTypeFilter(String workTypeFilter) {
		this.workTypeFilter = workTypeFilter;
	}

	public Integer getIdWork() {
		return idWork;
	}

	public void setIdWork(Integer idWork) {
		this.idWork = idWork;
	}

	public Integer getIdStepWork() {
		return idStepWork;
	}

	public void setIdStepWork(Integer idStepWork) {
		this.idStepWork = idStepWork;
	}

	public String searchWorkingProcesses() {

		logger.debug("searchWorkingProcesses() - action: " + action);

		try {

			wProcessDefLightList = (ArrayList<WProcessDefLight>) new WProcessDefBL()
					.finderWProcessDefLight(onlyActiveWorkingProcessesFilter, processNameFilter, 
							initialProductionDateFilter, finalProductionDateFilter, 
							strictProductionDateFilter, productionUserFilter, action, null, null, getCurrentUserId());

			nResults = wProcessDefLightList.size();

		} catch (WProcessDefException e) {
			String message = "WorkingProcessQueryBean.searchWorkingProcesses() WProcessDefException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

		return WORKINGPROCESS_QUERY;
	}

	public void reloadWorkingProcesses() {

		logger.debug("searchWorkingProcesses() - action: " + action);

		try {

			wProcessDefLightList = (ArrayList<WProcessDefLight>) new WProcessDefBL()
					.finderWProcessDefLight(onlyActiveWorkingProcessesFilter, processNameFilter, 
							initialProductionDateFilter, finalProductionDateFilter, 
							strictProductionDateFilter, productionUserFilter, action, null, null, getCurrentUserId());

			nResults = wProcessDefLightList.size();

		} catch (WProcessDefException e) {
			String message = "WorkingProcessQueryBean.reloadWorkingProcesses() WProcessDefException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

	}


	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	

	public Integer getIdStep() {
		return idStep;
	}

	public void setIdStep(Integer idStep) {
		this.idStep = idStep;
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

		// dml 20120130
		return new WProcessDefUtil().loadWProcessDefFormBean(currentProcessId);
		
	}
	
	// dml 20120123
	public String loadWStepDefForm() {

		return new WStepDefUtil().loadWStepDefFormBean(idStep);
		
	}
	
	//rrl 20120117
	public String generateXmlWProcessDef() {

		return new WProcessDefUtil().generateXmlWProcessDef(currentProcessId);
		
	}

	// dml 20120124
	public String createNewWProcessDef() {

		return new WProcessDefUtil().createNewWProcessDef("");
	}
	
	public String searchProcessWork(){

		logger.debug("searchProcessWork() - action: " + action);

		try {

			processWorkLightList = (ArrayList<ProcessWorkLight>) new WProcessWorkBL()
					.finderWorkingWork(processIdFilter, workTypeFilter, onlyActiveWorkingProcessesFilter, 
							initialStartedDateFilter, finalStartedDateFilter, estrictStartedDateFilter, 
							initialFinishedDateFilter, finalFinishedDateFilter, estrictFinishedDateFilter, 
							action);

			nWorkResults = processWorkLightList.size();

		} catch (WProcessWorkException e) {
			String message = "WorkingProcessQueryBean.searchProcessWork() WProcessWorkException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

		return WORKINGWORKS_QUERY;
		
	}

	// dml 20120124
	public String searchProcessWorkAliveSteps(){
		
		String ret = searchStepWork();
		
		this.idWorkFilter = 0;
	
		return ret;
		
	}
	
	public String searchStepWork(){
		
		logger.debug("searchStepWork() - action: " + action);

		try {	
			
			stepWorkLightList = (ArrayList<StepWorkLight>) new WStepWorkBL()
					.finderStepWork(processIdFilter, stepIdFilter, 
							stepTypeFilter, referenceFilter, idWorkFilter, initialArrivingDateFilter, 
							finalArrivingDateFilter, estrictArrivingDateFilter, 
							initialOpenedDateFilter, finalOpenedDateFilter, estrictOpenedDateFilter,
							initialDeadlineDateFilter, finalDeadlineDateFilter, estrictDeadlineDateFilter, 
							initialDecidedDateFilter, finalDecidedDateFilter, estrictDecidedDateFilter, 
							action, onlyActiveWorkingProcessesFilter);

			nStepResults = stepWorkLightList.size();

		} catch (WStepWorkException e) {
			String message = "WorkingProcessQueryBean.searchStepWork() WStepWorkException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

		return WORKINGSTEPS_QUERY;
		
	}
	
	public void reloadStepWorkLightList(){
		
		logger.debug("reloadStepWorkLightList() - action: " + action);

		try {	
			
			stepWorkLightList = (ArrayList<StepWorkLight>) new WStepWorkBL()
					.finderStepWork(processIdFilter, stepIdFilter, 
							stepTypeFilter, referenceFilter, idWorkFilter, initialArrivingDateFilter, 
							finalArrivingDateFilter, estrictArrivingDateFilter, 
							initialOpenedDateFilter, finalOpenedDateFilter, estrictOpenedDateFilter,
							initialDeadlineDateFilter, finalDeadlineDateFilter, estrictDeadlineDateFilter, 
							initialDecidedDateFilter, finalDecidedDateFilter, estrictDecidedDateFilter, 
							action, onlyActiveWorkingProcessesFilter);

			nStepResults = stepWorkLightList.size();

		} catch (WStepWorkException e) {
			String message = "WorkingProcessQueryBean.reloadStepWorkLightList() WStepWorkException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

	}
	
	public void desactivateWProcessDef(){
		
		WProcessDefBL wpdBL = new WProcessDefBL();
		
		WProcessDef wpd;

		try {
			
			wpd = wpdBL.getWProcessDefByPK(currentProcessId, getCurrentUserId());
			
			if (wpd != null){
				
				wpd.setActive(false);
				wpdBL.update(wpd, getCurrentUserId());
				
			}
			
		} catch (WProcessDefException e) {
			String message = "WorkingProcessQueryBean.desactivateWProcessDef() WProcessDefException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (WStepSequenceDefException e) {
			String message = "WorkingProcessQueryBean.desactivateWProcessDef() WStepSequenceDefException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}

	}
	
	public List<SelectItem> getwUsersDef(){
		
		try {
			
			return UtilsVs
					.castStringPairToSelectitem(new WUserDefBL().getComboList("All users", ""));
			
		} catch (WUserDefException e) {
			String message = "WorkingProcessQueryBean.getwUsersDef() WUserDefException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
		return null;
		
	}
	
	public List<SelectItem> getwProcessesDef(){
		
		try {
			
			if (onlyActiveWorkingProcessesFilter){
				
				return UtilsVs
						.castStringPairToSelectitem(new WProcessDefBL().
								getComboActiveProcessList("All active processes", "", getCurrentUserId()));
				
			} else {
				
				return UtilsVs
						.castStringPairToSelectitem(new WProcessDefBL().getComboList("All processes", "", getCurrentUserId()));
				
			}
			
		} catch (WProcessDefException e) {
			String message = "WorkingProcessQueryBean.getwProcessesDef() WProcessDefException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
		return null;
		
	}
	
	public List<SelectItem> getValidStepList(){
		
		try {
			
			String stepMessage = "All steps";
			if (processIdFilter != null && processIdFilter != 0){
				stepMessage += " of this process";
			} 
			
			List <SelectItem> l = UtilsVs
					.castStringPairToSelectitem(new WStepDefBL().getComboList(processIdFilter, stepMessage, ""));
			
			return l;
			
		} catch (WProcessDefException e) {
			String message = "WorkingProcessQueryBean.getValidStepList() WProcessDefException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
		return null;
		
	}
	
	public void lockUnlockStep() {
		
		WStepWorkBL wswBL = new WStepWorkBL();
		
		try {
			
			if (idStepWork != null && idStepWork != 0) {
				if (stepLocked){
					wswBL.unlockStep(idStepWork, getCurrentUserId(), true);
				} else {
					wswBL.lockStep(idStepWork, null, getCurrentUserId(), true);
				}
			}
			
			reloadStepWorkLightList();
		
		} catch (WStepNotLockedException e) {
			String message = "WorkingProcessQueryBean.lockUnlockStep() WProcessDefException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (WStepLockedByAnotherUserException e) {
			String message = "WorkingProcessQueryBean.lockUnlockStep() WStepLockedByAnotherUserException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		} catch (WStepWorkException e) {
			String message = "WorkingProcessQueryBean.lockUnlockStep() WStepWorkException: ";
			super.createWindowMessage(ERROR_MESSAGE, message, e);
		}
		
	}
	
	// dml 20120124
	public void loadWStepWorkForm() {

		new WStepWorkUtil().loadWStepWorkFormBean(idStepWork);

	}

	// dml 20120130
	public String loadWProcessWorkForm() {

		return new WProcessWorkUtil().loadWProcessWorkFormBean(idWork);

	}

	public void processWStepWork(){
		
		try {
			currentWStepWork = new WStepWorkBL().getWStepWorkByPK(this.idStepWork, getCurrentUserId());
		} catch (WStepWorkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		if ( currentWStepWork.getCurrentStep().isEmailNotification()  ) {
//			try {
//				new WStepWorkBL()._emailNotificationArrivingStep(currentWStepWork,"");
//			} catch (SendEmailException e) {
//				logger.info("SendEmailException: there is not possible sending email notification to users involved");
//			} catch (Exception e) {
//				logger.info("Exception: there is not possible sending email notification to users involved");
//			}
//		}
	}

	public String loadStepWork(){

		String ret=FAIL;
		
		try {

			ret = new TareaWorkflowUtil()
							.cargarPasoWorkflow(idStepWork, null, null);

			
			// nes 20110220 - obligo a que se recargue la lista porque si no no refresca bien ...
			wProcessDefLightList=null;
			processWorkLightList=null;
			stepWorkLightList=null;
			nResults=0;
			nStepResults=0;
			nWorkResults=0;
			
		} catch (CantLockTheStepException e) {
			
			String message = "No se puede reservar la tarea indicada ....\n";
			message +="error:"+e.getMessage()+" - "+ e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);			

			ret=FAIL;
			
		} catch (WStepLockedByAnotherUserException e) {
			
			String message = "La tarea indicada está bloqueada por otro usuario ....\n";
			message +="error:"+e.getMessage()+" - "+ e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);			

			ret=FAIL;
			
		} catch (WStepWorkException e) {
			
			String message = "No se puede cargar la tarea indicada ....\n";
			message +="error:"+e.getMessage()+" - "+ e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);			

			ret=FAIL;
			
		} catch (WUserDefException e) {
			String message = "No se puede cargar la tarea indicada ....\n";
			message +="error:"+e.getMessage()+" - "+ e.getCause();
			super.createWindowMessage(ERROR_MESSAGE, message, e);
			logger.info(message);			

			ret=FAIL;
		}

		return ret;

	}
	
	public void loadXmlMapAsTmp() {
		
		if (currentProcessId != null
				&& !currentProcessId.equals(0)
				&& idStepWork != null
				&& !idStepWork.equals(0)){
			try {
				
				this.currentWStepWork = new WStepWorkBL().getWStepWorkByPK(idStepWork, getCurrentUserId());
				
				String xmlMapTmp = new WProcessDefBL().getProcessDefXmlMap(this.currentProcessId, getCurrentUserId());
				
				if (this.currentWStepWork != null
						&& this.currentWStepWork.getwProcessWork() != null
						&& this.currentWStepWork.getwProcessWork().getId() != null
						&& !this.currentWStepWork.getwProcessWork().getId().equals(0)
						&& xmlMapTmp != null){
					
					xmlMapTmp = new WorkflowEditorMxBL().paintXmlMap(
							xmlMapTmp, this.currentWStepWork.getwProcessWork().getId(), currentUserId);
					
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
					
				}

			} catch (IOException e) {
				String message = "Error trying to create the xml map temp file for process: id=" + this.currentProcessId;
				message +="error:"+e.getMessage()+" - "+ e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (WProcessDefException e) {
				String message = "Error trying to create the xml map temp file for process: id=" + this.currentProcessId;
				message +="error:"+e.getMessage()+" - "+ e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (ParserConfigurationException e) {
				String message = "Error trying to create the xml map temp file for process: id=" + this.currentProcessId;
				message +="error:"+e.getMessage()+" - "+ e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (SAXException e) {
				String message = "Error trying to create the xml map temp file for process: id=" + this.currentProcessId;
				message +="error:"+e.getMessage()+" - "+ e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (WStepWorkException e) {
				String message = "Error trying to create the xml map temp file for process: id=" + this.currentProcessId;
				message +="error:"+e.getMessage()+" - "+ e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (WStepDefException e) {
				String message = "Error trying to create the xml map temp file for process: id=" + this.currentProcessId;
				message +="error:"+e.getMessage()+" - "+ e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			} catch (WStepWorkSequenceException e) {
				String message = "Error trying to create the xml map temp file for process: id=" + this.currentProcessId;
				message +="error:"+e.getMessage()+" - "+ e.getCause();
				super.createWindowMessage(ERROR_MESSAGE, message, e);
			}
		}
		
	}

	public String getWorkflowViewXmlMapUrl(){
		return super.getRequestContextPath() + WORKFLOW_VIEW_URI;
	}
	
}
