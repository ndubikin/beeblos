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
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;
import org.beeblos.bpm.core.model.noper.StepWorkLight;
import org.beeblos.bpm.core.model.noper.ProcessWorkLight;
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

	private List<WProcessDefLight> wProcessDefLightList;
	private List<ProcessWorkLight> processWorkLightList;
	private List<StepWorkLight> stepWorkLightList;

	// DAVID: NO INICIALICES COSAS EN LA DEFINICION - HACELO EN EL INIT O EN EL RESET SI CORRESPONDE HACERLO.
	// BORRA ESTO DESPUES
//	private Integer nResults = 0;
//	private Integer nStepResults = 0;
//	private Integer nWorkResults = 0;

	private Integer nResults;
	private Integer nStepResults;
	private Integer nWorkResults;
	
	private boolean onlyActiveWorkingProcessesFilter;
	private boolean onlyActiveWorksFilter;
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
	
	private Integer id;

	private TimeZone timeZone;

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

		this.processIdFilter = null;
		this.stepIdFilter = null;
		
		this.stepTypeFilter = "ALL";
		
		this.processNameFilter= "";

		this.initialProductionDateFilter = null;
		this.finalProductionDateFilter = null;
		this.estrictProductionDateFilter = false;
		
		this.productionUserFilter = 0;

		this.id = 0;
		
		// DAVID: ESTO ESTA MAL. ESTAS REINICIALIZANDO process o step form bean pero le pasás workingProcessQueryBean como parametro ..
		// hace un debug y fijate que hace, seguramente haga cualqueir cosa, te anoto abajo como es correcto hacerlo:

//		// reset session wProcessDefFormBean
//		HelperUtil
//			.recreateBean(
//					"workingProcessQueryBean", "org.beeblos.bpm.wc.taglib.bean.WProcessDefFormBean");
//		HelperUtil
//			.recreateBean(
//				"workingProcessQueryBean", "org.beeblos.bpm.wc.taglib.bean.WStepDefFormBean");
		
		// reset session wProcessDefFormBean
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
		if (processWorkLightList == null || processWorkLightList.isEmpty()) {
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

	// DAVID ESTO HAY QUE SACARLO A UN UTIL ( WProcessDefUtil.java )
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

			processWorkLightList = (ArrayList<ProcessWorkLight>) new WProcessDefBL()
					.getWorkingProcessWorkListByFinder(processIdFilter, onlyActiveWorksFilter, 
							initialStartedDateFilter, finalStartedDateFilter, estrictStartedDateFilter, 
							initialFinishedDateFilter, finalFinishedDateFilter, estrictFinishedDateFilter, 
							action);

			nWorkResults = processWorkLightList.size();

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

			
			
			processWorkLightList = (ArrayList<ProcessWorkLight>) new WProcessDefBL()
					.getWorkingProcessWorkListByFinder(processIdFilter, onlyActiveWorksFilter, 
							initialStartedDateFilter, finalStartedDateFilter, estrictStartedDateFilter, 
							initialFinishedDateFilter, finalFinishedDateFilter, estrictFinishedDateFilter, 
							action);

			nWorkResults = processWorkLightList.size();

		} catch (WProcessDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".searchWorkingProcessLiveWorks() WProcessDefException ..." };
			agregarMensaje("206", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();
		}
		
	}

	public String searchLiveSteps(){
		
		logger.debug("searchWorkingProcessLiveWorks() - action: " + action);

		try {
			
			stepWorkLightList = (ArrayList<StepWorkLight>) new WProcessDefBL()
					.getWorkingProcessStepListByFinder(processIdFilter, stepIdFilter, 
							stepTypeFilter, referenceFilter, initialArrivingDateFilter, 
							finalArrivingDateFilter, estrictArrivingDateFilter, 
							initialOpenedDateFilter, finalOpenedDateFilter, estrictOpenedDateFilter,
							initialDeadlineDateFilter, finalDeadlineDateFilter, estrictDeadlineDateFilter, 
							initialDecidedDateFilter, finalDecidedDateFilter, estrictDecidedDateFilter, 
							action);

			nStepResults = stepWorkLightList.size();

		} catch (WProcessDefException ex1) {
			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".searchLiveSteps() WProcessDefException ..." };
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
	
}
