package org.beeblos.bpm.wc.taglib.util;

import static org.beeblos.bpm.core.util.Constants.CREATE_NEW_WPROCESSHEAD;
import static org.beeblos.bpm.core.util.Constants.CREATE_NEW_WPROCESSDEF;
import static org.beeblos.bpm.core.util.Constants.FAIL;
import static org.beeblos.bpm.core.util.Constants.LOAD_WPROCESSDEF;
import static org.beeblos.bpm.core.util.Constants.LOAD_WPROCESSHEAD;
import static org.beeblos.bpm.core.util.Constants.WPROCESSDEF_QUERY;

import javax.el.ValueExpression;

import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.wc.taglib.bean.WProcessDefFormBean;
import org.beeblos.bpm.wc.taglib.bean.WProcessHeadFormBean;

public class WProcessDefUtil extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	public WProcessDefUtil() {

	}

	public String createNewWProcessDef(String returnStatement) {

		String ret = FAIL;

		ValueExpression valueBinding = super
				.getValueExpression("#{wProcessDefFormBean}");

		if (valueBinding != null) {

			WProcessDefFormBean wpdfb = (WProcessDefFormBean) valueBinding
					.getValue(super.getELContext());
			wpdfb.init();
			wpdfb.initEmptyWProcessDef();
			wpdfb.setReturnStatement(returnStatement);

			ret = CREATE_NEW_WPROCESSDEF;

		}

		return ret;
	}
	
	public String createNewProcessDef(Integer processId, String returnStatement) throws WProcessDefException {

		String ret = FAIL;

		ValueExpression valueBinding = super
				.getValueExpression("#{wProcessDefFormBean}");

		if (valueBinding != null) {

			WProcessDefFormBean wpdfb = (WProcessDefFormBean) valueBinding
					.getValue(super.getELContext());

			wpdfb.init();
			wpdfb.initEmptyWProcessDef();
			
			wpdfb.setCurrentProcessHeadId(processId);
			
			wpdfb.createEmptyNewProcessDefVersion();
			
			wpdfb.setReturnStatement(returnStatement);

			ret = CREATE_NEW_WPROCESSDEF;

		}

		return ret;
	}
		
	public String createNewWProcessHead(String returnStatement) {

		String ret = FAIL;

		ValueExpression valueBinding = super
				.getValueExpression("#{wProcessDefFormBean}");

		if (valueBinding != null) {

			WProcessDefFormBean wpdfb = (WProcessDefFormBean) valueBinding
					.getValue(super.getELContext());
			wpdfb.init();
			wpdfb.initEmptyWProcessDef();
			wpdfb.setReturnStatement(WPROCESSDEF_QUERY);

			ret = CREATE_NEW_WPROCESSHEAD;

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
	
	public String loadWProcessHeadFormBean(Integer processHeadId, String returnStatement) {

		String ret = FAIL;

		if (processHeadId != null && processHeadId != 0){
			
			ValueExpression valueBinding = super
					.getValueExpression("#{wProcessHeadFormBean}");

			if (valueBinding != null) {

				WProcessHeadFormBean wpfb = (WProcessHeadFormBean) valueBinding.getValue(super.getELContext());
				wpfb.init();
				wpfb.setCurrentId(processHeadId);
				wpfb.loadCurrentWProcessHead(processHeadId);
				wpfb.setReturnStatement(returnStatement);

				ret = LOAD_WPROCESSHEAD;
			
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
