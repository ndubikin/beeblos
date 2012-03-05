package org.beeblos.bpm.wc.taglib.util;

import static org.beeblos.bpm.core.util.Constants.CREATE_NEW_WSTEPDEF;
import static org.beeblos.bpm.core.util.Constants.FAIL;
import static org.beeblos.bpm.core.util.Constants.LOAD_WSTEPDEF;

import javax.el.ValueExpression;

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

}
