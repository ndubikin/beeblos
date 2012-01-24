package org.beeblos.bpm.wc.taglib.util;

import static org.beeblos.bpm.core.util.Constants.CREATE_NEW_WPROCESSDEF;

import javax.el.ValueExpression;

import org.beeblos.bpm.wc.taglib.bean.WProcessDefFormBean;

public class WProcessDefUtil extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	public WProcessDefUtil() {

	}

	public String createNewWProcessDef() {

		String ret = "FAIL";

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

}
