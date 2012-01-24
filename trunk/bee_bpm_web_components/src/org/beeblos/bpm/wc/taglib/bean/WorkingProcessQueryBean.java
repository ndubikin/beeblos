package org.beeblos.bpm.wc.taglib.bean;

import static org.beeblos.bpm.core.util.Constants.LOAD_WPROCESSDEF;
import static org.beeblos.bpm.core.util.Constants.LOAD_WSTEPDEF;
import static org.beeblos.bpm.core.util.Constants.WORKINGPROCESS_QUERY;
import static org.beeblos.bpm.core.util.Constants.WORKINGSTEPS_QUERY;
import static org.beeblos.bpm.core.util.Constants.WORKINGWORKS_QUERY;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.el.ValueExpression;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WProcessWorkBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepWorkBL;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.error.WStepLockedByAnotherUserException;
import org.beeblos.bpm.core.error.WStepNotLockedException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.noper.ProcessWorkLight;
import org.beeblos.bpm.core.model.noper.StepWorkLight;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.HelperUtil;
import org.beeblos.bpm.wc.taglib.util.UtilsVs;
import org.beeblos.bpm.wc.taglib.util.WProcessDefUtil;
import org.beeblos.bpm.wc.taglib.util.WStepWorkUtil;

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
	private boolean onlyActiveWorksFilter;
	private String referenceFilter;
	
	// dml 20120124
	private Integer idWorkFilter;
	
	private Integer processIdFilter;
	private Integer stepIdFilter;
	
	private String stepTypeFilter;
	
	private String processNameFilter;

	private Date initialProductionDateFilter;
	private Date finalProductionDateFilter;
	private boolean estrictProductionDateFilter;
	
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
		this.onlyActiveWorksFilter = true;
		
		this.referenceFilter = "";
		this.idWorkFilter = 0;

		this.processIdFilter = null;
		this.stepIdFilter = null;
		
		this.stepTypeFilter = "ALL";
		
		this.processNameFilter= "";

		this.initialProductionDateFilter = null;
		this.finalProductionDateFilter = null;
		this.estrictProductionDateFilter = false;
		
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

	public boolean isOnlyActiveWorksFilter() {
		return onlyActiveWorksFilter;
	}

	public void setOnlyActiveWorksFilter(boolean onlyActiveWorksFilter) {
		this.onlyActiveWorksFilter = onlyActiveWorksFilter;
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

	public boolean isEstrictProductionDateFilter() {
		return estrictProductionDateFilter;
	}

	public void setEstrictProductionDateFilter(boolean estrictProductionDateFilter) {
		this.estrictProductionDateFilter = estrictProductionDateFilter;
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
					.getWorkingProcessListFinder(onlyActiveWorkingProcessesFilter, processNameFilter, 
							initialProductionDateFilter, finalProductionDateFilter, 
							estrictProductionDateFilter, productionUserFilter, action);

			nResults = wProcessDefLightList.size();

		} catch (WProcessDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".searchWorkingProcesses() WProcessDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}

		return WORKINGPROCESS_QUERY;
	}

	public void reloadWorkingProcesses() {

		logger.debug("searchWorkingProcesses() - action: " + action);

		try {

			wProcessDefLightList = (ArrayList<WProcessDefLight>) new WProcessDefBL()
					.getWorkingProcessListFinder(onlyActiveWorkingProcessesFilter, processNameFilter, 
							initialProductionDateFilter, finalProductionDateFilter, 
							estrictProductionDateFilter, productionUserFilter, action);

			nResults = wProcessDefLightList.size();

		} catch (WProcessDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".reloadWorkingProcesses() WProcessDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
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

		String ret = "FAIL";

		if (this.currentProcessId != null){
			
			ValueExpression valueBinding = super
					.getValueExpression("#{wProcessDefFormBean}");

			if (valueBinding != null) {

				WProcessDefFormBean wpdfb = 
						(WProcessDefFormBean) valueBinding
							.getValue(super.getELContext());
				wpdfb.init();
				wpdfb.setCurrentId(currentProcessId);
				wpdfb.loadCurrentWProcessDef(currentProcessId);

				ret = LOAD_WPROCESSDEF;
			
			}
		}

		return ret;
	}
	
	// dml 20120123
	public String loadWStepDefForm() {

		String ret = "FAIL";

		if (this.idStep != null && this.idStep != 0){
			
			ValueExpression valueBinding = super
					.getValueExpression("#{wStepDefFormBean}");

			if (valueBinding != null) {

				WStepDefFormBean wpdfb = 
						(WStepDefFormBean) valueBinding
							.getValue(super.getELContext());
				wpdfb.init();
				wpdfb.setCurrObjId(idStep);
				wpdfb.loadObject(idStep);

				ret = LOAD_WSTEPDEF;
			
			}
		}

		return ret;
	}
	
	//rrl 20120117
	public String generateXmlWProcessDef() {

		if (this.currentProcessId != null){
			
			ValueExpression valueBinding = super
					.getValueExpression("#{wProcessDefFormBean}");

			if (valueBinding != null) {

				WProcessDefFormBean wpdfb = 
						(WProcessDefFormBean) valueBinding
							.getValue(super.getELContext());
				wpdfb.init();
				wpdfb.setCurrentId(currentProcessId);
				wpdfb.loadCurrentWProcessDef(currentProcessId);
				
				wpdfb.generateXMLCurrentWProcessDef();
			}
		}
		
		return null;
	}

	// dml 20120124
	public String createNewWProcessDef() {

		return new WProcessDefUtil().createNewWProcessDef();
	}
	
	public String searchProcessWork(){

		logger.debug("searchProcessWork() - action: " + action);

		try {

			processWorkLightList = (ArrayList<ProcessWorkLight>) new WProcessWorkBL()
					.getWorkingWorkListFinder(processIdFilter, onlyActiveWorksFilter, onlyActiveWorkingProcessesFilter, 
							initialStartedDateFilter, finalStartedDateFilter, estrictStartedDateFilter, 
							initialFinishedDateFilter, finalFinishedDateFilter, estrictFinishedDateFilter, 
							action);

			nWorkResults = processWorkLightList.size();

		} catch (WProcessWorkException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".searchProcessWork() WProcessDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}

		return WORKINGWORKS_QUERY;
		
	}

	public void reloadProcessWorkLightList(){

		logger.debug("reloadProcessWorkLightList() - action: " + action);

		try {

			
			
			processWorkLightList = (ArrayList<ProcessWorkLight>) new WProcessWorkBL()
					.getWorkingWorkListFinder(processIdFilter, onlyActiveWorksFilter, onlyActiveWorkingProcessesFilter,
							initialStartedDateFilter, finalStartedDateFilter, estrictStartedDateFilter, 
							initialFinishedDateFilter, finalFinishedDateFilter, estrictFinishedDateFilter, 
							action);

			nWorkResults = processWorkLightList.size();

		} catch (WProcessWorkException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".reloadProcessWorkLightList() WProcessDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}
		
	}

	// dml 20120124
	public String searchProcessWorkAliveSteps(){
		
		String ret = searchStepWork();
		
		idWorkFilter = 0;
	
		return ret;
		
	}
	
	public String searchStepWork(){
		
		logger.debug("searchStepWork() - action: " + action);

		try {	
			
			stepWorkLightList = (ArrayList<StepWorkLight>) new WStepWorkBL()
					.getWorkingStepListFinder(processIdFilter, stepIdFilter, 
							stepTypeFilter, referenceFilter, idWorkFilter, initialArrivingDateFilter, 
							finalArrivingDateFilter, estrictArrivingDateFilter, 
							initialOpenedDateFilter, finalOpenedDateFilter, estrictOpenedDateFilter,
							initialDeadlineDateFilter, finalDeadlineDateFilter, estrictDeadlineDateFilter, 
							initialDecidedDateFilter, finalDecidedDateFilter, estrictDecidedDateFilter, 
							action, onlyActiveWorkingProcessesFilter);

			nStepResults = stepWorkLightList.size();

		} catch (WStepWorkException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".searchStepWorkLights() WProcessDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}

		return WORKINGSTEPS_QUERY;
		
	}
	
	public void reloadStepWorkLightList(){
		
		logger.debug("reloadStepWorkLightList() - action: " + action);

		try {	
			
			stepWorkLightList = (ArrayList<StepWorkLight>) new WStepWorkBL()
					.getWorkingStepListFinder(processIdFilter, stepIdFilter, 
							stepTypeFilter, referenceFilter, idWorkFilter, initialArrivingDateFilter, 
							finalArrivingDateFilter, estrictArrivingDateFilter, 
							initialOpenedDateFilter, finalOpenedDateFilter, estrictOpenedDateFilter,
							initialDeadlineDateFilter, finalDeadlineDateFilter, estrictDeadlineDateFilter, 
							initialDecidedDateFilter, finalDecidedDateFilter, estrictDecidedDateFilter, 
							action, onlyActiveWorkingProcessesFilter);

			nStepResults = stepWorkLightList.size();

		} catch (WStepWorkException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".reloadStepWorkLightList() WProcessDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
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
			
		} catch (WProcessDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".desactivateWProcessDef() WProcessDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}

	}
	
	public List<SelectItem> getwUsersDef(){
		
		try {
			
			return UtilsVs
					.castStringPairToSelectitem(new WUserDefBL().getComboList("All users", ""));
			
		} catch (WUserDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".loadWDefUsers() WUserDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}
		
		return null;
		
	}
	
	public List<SelectItem> getwProcessesDef(){
		
		try {
			
			if (onlyActiveWorkingProcessesFilter){
				
				return UtilsVs
						.castStringPairToSelectitem(new WProcessDefBL().
								getComboActiveProcessList("All active processes", ""));
				
			} else {
				
				return UtilsVs
						.castStringPairToSelectitem(new WProcessDefBL().getComboList("All processes", ""));
				
			}
			
		} catch (WProcessDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".getwProcessesDef() WProcessDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}
		
		return null;
		
	}
	
	public List<SelectItem> getValidStepList(){
		
		try {
			
			String stepMessage = "All steps";
			if (processIdFilter != null && processIdFilter != 0){
				stepMessage += " of this process";
			} 
			
			return UtilsVs
					.castStringPairToSelectitem(new WStepDefBL().getComboList(processIdFilter, 1, stepMessage, ""));
			
		} catch (WProcessDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".getValidStepList() WProcessDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
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
		
		} catch (WStepNotLockedException ex1) {
			
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".lockUnlockStep() WStepNotLockedException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
			
		} catch (WStepLockedByAnotherUserException ex2) {
			
			String mensaje = ex2.getMessage() + " - " + ex2.getCause();
			String params[] = { mensaje + ",",
					".lockUnlockStep() WStepLockedByAnotherUserException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex2.printStackTrace();
			
		} catch (WStepWorkException ex3) {
			
			String mensaje = ex3.getMessage() + " - " + ex3.getCause();
			String params[] = { mensaje + ",",
					".lockUnlockStep() WStepWorkException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex3.printStackTrace();
			
		}
		
	}
	
	// dml 20120124
	public void loadWStepWorkForm() {

		new WStepWorkUtil().createNewWStepWorkFormBean(idStepWork);

	}

}
