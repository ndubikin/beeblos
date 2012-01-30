package org.beeblos.bpm.wc.taglib.util;

import static org.beeblos.bpm.core.util.Constants.LOAD_WPROCESSWORK;
import static org.beeblos.bpm.core.util.Constants.FAIL;

import javax.el.ValueExpression;

import org.beeblos.bpm.wc.taglib.bean.WProcessWorkFormBean;

public class WProcessWorkUtil extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	public WProcessWorkUtil() {

	}

	public String loadWProcessWorkFormBean(Integer idWork) {

		String ret = FAIL;

		if (idWork != null && idWork != 0){

			ValueExpression valueBinding = super
					.getValueExpression("#{wProcessWorkFormBean}");
	
			if (valueBinding != null) {
	
				WProcessWorkFormBean wpwfb = (WProcessWorkFormBean) valueBinding
						.getValue(super.getELContext());
				wpwfb.init();
				wpwfb.setCurrObjId(idWork);
				wpwfb.loadWProcessWork(idWork);
	
				ret = LOAD_WPROCESSWORK;
	
			}
		
		}

		return ret;
	}

}
