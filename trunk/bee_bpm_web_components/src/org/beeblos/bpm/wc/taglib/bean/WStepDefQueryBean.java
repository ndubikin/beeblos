package org.beeblos.bpm.wc.taglib.bean;


import static org.beeblos.bpm.core.util.Constants.WSTEPDEF_QUERY;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
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

	private TimeZone timeZone;

	public WStepDefQueryBean() {
		super();

		_init();

	}

	private void _init() {
		
		logger.debug("WStepDefQueryBean._init()");

		this.wStepDefList = new ArrayList<WStepDef>();

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
					.getStepListByFinder(nameFilter, commentsFilter, 
							instructionsFilter, getCurrentUserId(), true, action);

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

	// dml 20120110
	public String createNewWStepDef() {

		return new WStepDefUtil().createNewWStepDef(WSTEPDEF_QUERY);
		
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
	
}
