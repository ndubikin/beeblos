package org.beeblos.bpm.wc.taglib.util;

import static org.beeblos.bpm.core.util.Constants.CREATE_NEW_WPROCESS;
import static org.beeblos.bpm.core.util.Constants.CREATE_NEW_WPROCESSDEF;
import static org.beeblos.bpm.core.util.Constants.FAIL;
import static org.beeblos.bpm.core.util.Constants.LOAD_WPROCESS;
import static org.beeblos.bpm.core.util.Constants.LOAD_WPROCESSDEF;
import static org.beeblos.bpm.core.util.Constants.WPROCESSDEF_QUERY;

import javax.el.ValueExpression;

import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.wc.taglib.bean.WProcessDefFormBean;

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
			wpdfb.setReturnStatement(WPROCESSDEF_QUERY);

			ret = CREATE_NEW_WPROCESSDEF;

		}

		return ret;
	}
	
	public String createNewWProcessDef(Integer processId, String returnStatement) throws WProcessDefException {

		String ret = FAIL;

		ValueExpression valueBinding = super
				.getValueExpression("#{wProcessDefFormBean}");

		if (valueBinding != null) {

			WProcessDefFormBean wpdfb = (WProcessDefFormBean) valueBinding
					.getValue(super.getELContext());
			wpdfb.init();
			wpdfb.initEmptyWProcessDef();
			
			wpdfb.setCurrentProcessIdSelected(processId);
			wpdfb.setProcessInWProcessDef();
			
			wpdfb.setReturnStatement(WPROCESSDEF_QUERY);

			ret = CREATE_NEW_WPROCESSDEF;

		}

		return ret;
	}
	
	public void cloneWProcessDef(Integer id) throws WProcessDefException {

		ValueExpression valueBinding = super
				.getValueExpression("#{wProcessDefFormBean}");

		if (valueBinding != null) {

			WProcessDefFormBean wpdfb = (WProcessDefFormBean) valueBinding
					.getValue(super.getELContext());
			wpdfb.init();
			wpdfb.loadCurrentWProcessDef(id);
			
			// ponemos a nulos los ids para crear uno nuevo con la misma info
			wpdfb.setCurrentId(null);
			wpdfb.getCurrentWProcessDef().setId(null);
			
			Integer lastProcessVersion = 
					new WProcessDefBL().getLastWProcessDefVersion(wpdfb.getCurrentWProcessDef().getProcess().getId());
			
			wpdfb.getCurrentWProcessDef().setVersion(lastProcessVersion + 1);
			wpdfb.getCurrentWProcessDef().setActive(true);
			
			wpdfb.save();

		}

	}
	
	public String createNewWProcess(String returnStatement) {

		String ret = FAIL;

		ValueExpression valueBinding = super
				.getValueExpression("#{wProcessDefFormBean}");

		if (valueBinding != null) {

			WProcessDefFormBean wpdfb = (WProcessDefFormBean) valueBinding
					.getValue(super.getELContext());
			wpdfb.init();
			wpdfb.initEmptyWProcessDef();
			wpdfb.setReturnStatement(WPROCESSDEF_QUERY);

			ret = CREATE_NEW_WPROCESS;

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
	
	public String loadWProcessFormBean(Integer idProcess) {

		String ret = FAIL;

		if (idProcess != null && idProcess != 0){
			
			ValueExpression valueBinding = super
					.getValueExpression("#{wProcessDefFormBean}");

			if (valueBinding != null) {

				WProcessDefFormBean wpfb = 
						(WProcessDefFormBean) valueBinding
							.getValue(super.getELContext());
				wpfb.init();
				wpfb.setCurrentId(idProcess);
				wpfb.loadCurrentWProcess(idProcess);

				ret = LOAD_WPROCESS;
			
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
