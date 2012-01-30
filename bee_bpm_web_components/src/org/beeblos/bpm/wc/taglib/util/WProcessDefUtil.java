package org.beeblos.bpm.wc.taglib.util;

import static org.beeblos.bpm.core.util.Constants.CREATE_NEW_WPROCESSDEF;
import static org.beeblos.bpm.core.util.Constants.FAIL;
import static org.beeblos.bpm.core.util.Constants.LOAD_WPROCESSDEF;

import javax.el.ValueExpression;

import org.beeblos.bpm.wc.taglib.bean.WProcessDefFormBean;

public class WProcessDefUtil extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	public WProcessDefUtil() {

	}

	public String createNewWProcessDef() {

		String ret = FAIL;

		ValueExpression valueBinding = super
				.getValueExpression("#{wProcessDefFormBean}");

		if (valueBinding != null) {

			WProcessDefFormBean wpdfb = (WProcessDefFormBean) valueBinding
					.getValue(super.getELContext());
			wpdfb.init();
			wpdfb.initEmptyWProcessDef();

			ret = CREATE_NEW_WPROCESSDEF;

		}

		return ret;
	}
	
	public String loadWProcessDefFormBean(Integer idProcess) {

		String ret = FAIL;

		if (idProcess != null && idProcess != 0){
			
			ValueExpression valueBinding = super
					.getValueExpression("#{wProcessDefFormBean}");

			if (valueBinding != null) {

				WProcessDefFormBean wpdfb = 
						(WProcessDefFormBean) valueBinding
							.getValue(super.getELContext());
				wpdfb.init();
				wpdfb.setCurrentId(idProcess);
				wpdfb.loadCurrentWProcessDef(idProcess);

				ret = LOAD_WPROCESSDEF;
			
			}
		}

		return ret;
	}
	
	public String generateXmlWProcessDef(Integer processId) {

		if (processId != null){
			
			ValueExpression valueBinding = super
					.getValueExpression("#{wProcessDefFormBean}");

			if (valueBinding != null) {

				WProcessDefFormBean wpdfb = 
						(WProcessDefFormBean) valueBinding
							.getValue(super.getELContext());
				wpdfb.init();
				wpdfb.setCurrentId(processId);
				wpdfb.loadCurrentWProcessDef(processId);
				
				wpdfb.generateXMLCurrentWProcessDef();
				
			}
			
		}
		
		return null;
		
	}

}
