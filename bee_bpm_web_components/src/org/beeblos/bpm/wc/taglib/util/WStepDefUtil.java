package org.beeblos.bpm.wc.taglib.util;

import static org.beeblos.bpm.core.util.Constants.CREATE_NEW_WSTEPDEF;
import static org.beeblos.bpm.core.util.Constants.CREATE_NEW_WSTEPHEAD;
import static org.beeblos.bpm.core.util.Constants.FAIL;
import static org.beeblos.bpm.core.util.Constants.LOAD_WPROCESS;
import static org.beeblos.bpm.core.util.Constants.LOAD_WSTEPDEF;
import static org.beeblos.bpm.core.util.Constants.LOAD_WSTEPHEAD;

import javax.el.ValueExpression;

import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.wc.taglib.bean.WProcessDefFormBean;
import org.beeblos.bpm.wc.taglib.bean.WStepDefFormBean;

public class WStepDefUtil extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	public WStepDefUtil() {

	}

	public String createNewWStepDef(String returnStatement) {

		String ret = FAIL;

		ValueExpression valueBinding = super
				.getValueExpression("#{wStepDefFormBean}");

		if (valueBinding != null) {

			WStepDefFormBean wsdfb = (WStepDefFormBean) valueBinding
					.getValue(super.getELContext());
			wsdfb.init();
			wsdfb.initEmptyWStepDef();
			wsdfb.setReturnStatement(returnStatement);

			ret = CREATE_NEW_WSTEPDEF;

		}

		return ret;
	}
	
	// dml 20130508
	public String createNewWStepDef(Integer stepHeadId, String returnStatement) throws WProcessDefException {

		String ret = FAIL;

		ValueExpression valueBinding = super
				.getValueExpression("#{wStepDefFormBean}");

		if (valueBinding != null) {

			WStepDefFormBean wsdfb = (WStepDefFormBean) valueBinding
					.getValue(super.getELContext());
			wsdfb.init();
			wsdfb.initEmptyWStepDef();
			
			wsdfb.setCurrentStepHeadIdSelected(stepHeadId);
			wsdfb.setStepInWStepDef();
			
			wsdfb.setReturnStatement(returnStatement);

			ret = CREATE_NEW_WSTEPDEF;

		}

		return ret;
	}
		
	// dml 20130508
	public String createNewWStepHead(String returnStatement) {

		String ret = FAIL;

		ValueExpression valueBinding = super
				.getValueExpression("#{wStepDefFormBean}");

		if (valueBinding != null) {

			WStepDefFormBean wsdfb = (WStepDefFormBean) valueBinding
					.getValue(super.getELContext());
			wsdfb.init();
			wsdfb.initEmptyWStepDef();
			wsdfb.setReturnStatement(returnStatement);

			ret = CREATE_NEW_WSTEPHEAD;

		}

		return ret;
	}
	
	public String loadWStepDefFormBean(Integer idStep) {

		String ret = FAIL;

		if (idStep != null && idStep != 0){
			
			ValueExpression valueBinding = super
					.getValueExpression("#{wStepDefFormBean}");

			if (valueBinding != null) {

				WStepDefFormBean wsdfb = 
						(WStepDefFormBean) valueBinding
							.getValue(super.getELContext());
				wsdfb.init();
				wsdfb.setCurrObjId(idStep);
				wsdfb.loadObject(idStep);

				ret = LOAD_WSTEPDEF;
			
			}
		}

		return ret;
	}

	public String loadWStepHeadFormBean(Integer stepId) {

		String ret = FAIL;

		if (stepId != null && stepId != 0){
			
			ValueExpression valueBinding = super
					.getValueExpression("#{wStepDefFormBean}");

			if (valueBinding != null) {

				WStepDefFormBean wsdfb = 
						(WStepDefFormBean) valueBinding
							.getValue(super.getELContext());
				wsdfb.init();
				wsdfb.setCurrObjId(stepId);
				wsdfb.loadObject(stepId);

				ret = LOAD_WSTEPHEAD;
			
			}
		}

		return ret;
	}
	
}
