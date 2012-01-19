package org.beeblos.bpm.wc.taglib.bean;

import static org.beeblos.bpm.core.util.Constants.CREATE_NEW_WPROCESSDEF;
import static org.beeblos.bpm.core.util.Constants.LOAD_WPROCESSDEF;
import static org.beeblos.bpm.core.util.Constants.WORKINGPROCESSSTEPS_QUERY;
import static org.beeblos.bpm.core.util.Constants.WORKINGPROCESSWORKS_QUERY;
import static org.beeblos.bpm.core.util.Constants.WORKINGPROCESS_QUERY;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.el.ValueExpression;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;
import org.beeblos.bpm.core.model.noper.WorkingProcessStep;
import org.beeblos.bpm.core.model.noper.WorkingProcessWork;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.HelperUtil;
import org.beeblos.bpm.wc.taglib.util.UtilsVs;

public class WorkingProcessQueryBean extends CoreManagedBean {
	
	private static final long serialVersionUID = 1L;

	private static final Logger logger = 
			Logger.getLogger(WorkingProcessQueryBean.class);

	
	private Integer currentUserId;
	

	private List<WProcessDefLight> wProcessDefLightList = new ArrayList<WProcessDefLight>();
	private List<WorkingProcessWork> workingProcessWorkList = new ArrayList<WorkingProcessWork>();
	private List<WorkingProcessStep> workingProcessStepList = new ArrayList<WorkingProcessStep>();

	private Integer nResults = 0;
	private Integer nStepResults = 0;
	private Integer nWorkResults = 0;
	
	private boolean onlyActiveWorkingProcessesFilter;
	private boolean onlyActiveWorksFilter;
	private boolean onlyActiveStepsFilter;
	private String referenceFilter;
	
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
	
	private Integer productionUserFilter;
	
	private String action; 
	
	private Integer id;

	private TimeZone timeZone;

	public WorkingProcessQueryBean() {
		super();

		_init();

	}

	private void _init() {
		
		logger.debug("WorkingProcessQueryBean._init()");

		this.wProcessDefLightList = new ArrayList<WProcessDefLight>();
		this.workingProcessStepList = new ArrayList<WorkingProcessStep>();
		this.workingProcessWorkList = new ArrayList<WorkingProcessWork>();

		this.nResults = 0;
		this.nStepResults = 0;
		this.nWorkResults = 0;
				
		this.onlyActiveWorkingProcessesFilter = true;
		this.onlyActiveStepsFilter = true;
		this.onlyActiveWorksFilter = true;

		this.processIdFilter = null;
		this.stepIdFilter = null;
		
		this.stepTypeFilter = "ALL";
		
		this.processNameFilter= "";

		this.initialProductionDateFilter = null;
		this.finalProductionDateFilter = null;
		this.estrictProductionDateFilter = false;
		
		this.productionUserFilter = 0;

		this.id = 0;
		
		// reset session wProcessDefFormBean
		HelperUtil
			.recreateBean(
					"workingProcessQueryBean", "org.beeblos.bpm.wc.taglib.bean.WProcessDefFormBean");
		HelperUtil
			.recreateBean(
				"workingProcessQueryBean", "org.beeblos.bpm.wc.taglib.bean.WStepDefFormBean");
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


	public List<WorkingProcessWork> getWorkingProcessWorkList() {
		if (workingProcessWorkList == null || workingProcessWorkList.isEmpty()) {
			nWorkResults = 0;
		}
		return workingProcessWorkList;
	}

	public void setWorkingProcessWorkList(List<WorkingProcessWork> workingProcessWorkList) {
		this.workingProcessWorkList = workingProcessWorkList;
	}

	public List<WorkingProcessStep> getWorkingProcessStepList() {
		if (workingProcessStepList == null || workingProcessStepList.isEmpty()) {
			nWorkResults = 0;
		}
		return workingProcessStepList;
	}

	public void setWorkingProcessStepList(List<WorkingProcessStep> workingProcessStepList) {
		this.workingProcessStepList = workingProcessStepList;
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

	public boolean isOnlyActiveStepsFilter() {
		return onlyActiveStepsFilter;
	}

	public void setOnlyActiveStepsFilter(boolean onlyActiveStepsFilter) {
		this.onlyActiveStepsFilter = onlyActiveStepsFilter;
	}

	public String getReferenceFilter() {
		return referenceFilter;
	}

	public void setReferenceFilter(String referenceFilter) {
		this.referenceFilter = referenceFilter;
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

	public Integer getId() {

		return id;

	}

	public void setId(Integer id) {

		this.id = id;
	}

	public TimeZone getTimeZone() {
		// Si se pone GMT+1 pone mal el dia
		return java.util.TimeZone.getDefault();
	}

	public String searchWorkingProcesses() {

		logger.debug("searchWorkingProcesses() - action: " + action);

		try {

			wProcessDefLightList = (ArrayList<WProcessDefLight>) new WProcessDefBL()
					.getWorkingProcessListByFinder(onlyActiveWorkingProcessesFilter, processNameFilter, 
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
					.getWorkingProcessListByFinder(onlyActiveWorkingProcessesFilter, processNameFilter, 
							initialProductionDateFilter, finalProductionDateFilter, 
							estrictProductionDateFilter, processIdFilter, action);

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

		if (this.id != null){
			
			ValueExpression valueBinding = super
					.getValueExpression("#{wProcessDefFormBean}");

			if (valueBinding != null) {

				WProcessDefFormBean wpdfb = 
						(WProcessDefFormBean) valueBinding
							.getValue(super.getELContext());
				wpdfb.init();
				wpdfb.setCurrentId(id);
				wpdfb.loadCurrentWProcessDef(id);

				ret = LOAD_WPROCESSDEF;
			
			}
		}

		return ret;
	}
	
	//rrl 20120117
	public String generateXmlWProcessDef() {

		if (this.id != null){
			
			ValueExpression valueBinding = super
					.getValueExpression("#{wProcessDefFormBean}");

			if (valueBinding != null) {

				WProcessDefFormBean wpdfb = 
						(WProcessDefFormBean) valueBinding
							.getValue(super.getELContext());
				wpdfb.init();
				wpdfb.setCurrentId(id);
				wpdfb.loadCurrentWProcessDef(id);
				
				wpdfb.generateXMLCurrentWProcessDef();
			}
		}
		
		return null;
	}

	// dml 20120110
	public String createNewWProcessDef() {

		String ret = "FAIL";

		ValueExpression valueBinding = super
				.getValueExpression("#{wProcessDefFormBean}");

		if (valueBinding != null) {

			WProcessDefFormBean wpdfb = 
					(WProcessDefFormBean) valueBinding
						.getValue(super.getELContext());
			wpdfb.init();
			wpdfb.initEmptyWProcessDef();
			
			ret = CREATE_NEW_WPROCESSDEF;
		
		}

		return ret;
	}
	
	public String searchWorkingProcessLiveWorks(){

		logger.debug("searchWorkingProcessLiveWorks() - action: " + action);

		try {

			
			
			workingProcessWorkList = (ArrayList<WorkingProcessWork>) new WProcessDefBL()
					.getWorkingProcessWorkListByFinder(processIdFilter, onlyActiveWorksFilter, 
							initialStartedDateFilter, finalStartedDateFilter, estrictStartedDateFilter, 
							initialFinishedDateFilter, finalFinishedDateFilter, estrictFinishedDateFilter, 
							action);

			nWorkResults = workingProcessWorkList.size();

		} catch (WProcessDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".searchWorkingProcessLiveWorks() WProcessDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}

		return WORKINGPROCESSWORKS_QUERY;
		
	}

	public void reloadWorkingProcessWorks(){

		logger.debug("searchWorkingProcessLiveWorks() - action: " + action);

		try {

			
			
			workingProcessWorkList = (ArrayList<WorkingProcessWork>) new WProcessDefBL()
					.getWorkingProcessWorkListByFinder(processIdFilter, onlyActiveWorksFilter, 
							initialStartedDateFilter, finalStartedDateFilter, estrictStartedDateFilter, 
							initialFinishedDateFilter, finalFinishedDateFilter, estrictFinishedDateFilter, 
							action);

			nWorkResults = workingProcessWorkList.size();

		} catch (WProcessDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".searchWorkingProcessLiveWorks() WProcessDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}
		
	}

	public String searchWorkingProcessLiveSteps(){
		
		logger.debug("searchWorkingProcessLiveWorks() - action: " + action);

		try {
			
			workingProcessStepList = (ArrayList<WorkingProcessStep>) new WProcessDefBL()
					.getWorkingProcessStepListByFinder(processIdFilter, stepIdFilter, 
							stepTypeFilter, onlyActiveStepsFilter, referenceFilter, action);

			nStepResults = workingProcessStepList.size();

		} catch (WProcessDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".searchWorkingProcessLiveSteps() WProcessDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}

		return WORKINGPROCESSSTEPS_QUERY;
		
	}
	
	public void desactivateWProcessDef(){
		
		WProcessDefBL wpdBL = new WProcessDefBL();
		
		WProcessDef wpd;

		try {
			
			wpd = wpdBL.getWProcessDefByPK(id, getCurrentUserId());
			
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
					.castStringPairToSelectitem(new WUserDefBL().getComboList("Select an user ...", ""));
			
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
			
			return UtilsVs
					.castStringPairToSelectitem(new WProcessDefBL().getComboList("Select a process ...", ""));
			
		} catch (WProcessDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".getwProcessesDef() WProcessDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}
		
		return null;
		
	}
	
}
