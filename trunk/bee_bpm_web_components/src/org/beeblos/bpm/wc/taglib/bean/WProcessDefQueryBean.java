package org.beeblos.bpm.wc.taglib.bean;


import static org.beeblos.bpm.core.util.Constants.WPROCESSDEF_QUERY;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.error.WProcessDefException;
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

	private TimeZone timeZone;

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

	public TimeZone getTimeZone() {
		// Si se pone GMT+1 pone mal el dia
		return java.util.TimeZone.getDefault();
	}

	public String searchWProcessDefs() {

		logger.debug("searchWProcessDefs() - action: " + action);

		try {

			wProcessDefList = (ArrayList<WProcessDef>) new WProcessDefBL()
					.getProcessListByFinder(initialInsertDateFilter, finalInsertDateFilter, strictInsertDateFilter, 
							nameFilter, commentsFilter, listZoneFilter, workZoneFilter, additionalZoneFilter, 
							getCurrentUserId(), true, action);

			nResults = wProcessDefList.size();

		} catch (WProcessDefException e) {
			e.printStackTrace();
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
	
	//rrl 20120117
	public String generateXmlWProcessDef() {

		return new WProcessDefUtil().generateXmlWProcessDef(this.id);
		
	}

	// dml 20120110
	public String createNewWProcessDef() {

		return new WProcessDefUtil().createNewWProcessDef(WPROCESSDEF_QUERY);
		
	}

}
