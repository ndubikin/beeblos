package org.beeblos.bpm.wc.taglib.util;

import javax.el.ValueExpression;

import org.beeblos.bpm.wc.taglib.bean.WStepWorkFormBean;

public class WStepWorkUtil extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	public WStepWorkUtil() {

	}

	public void loadWStepWorkFormBean(Integer idStepWork) {

		if (idStepWork != null && idStepWork != 0){
			
			ValueExpression valueBinding = super
					.getValueExpression("#{wStepWorkFormBean}");

			if (valueBinding != null) {

				WStepWorkFormBean wswfb = 
						(WStepWorkFormBean) valueBinding
							.getValue(super.getELContext());
				wswfb.init();
				wswfb.setCurrObjId(idStepWork);
				wswfb.loadWStepWork(idStepWork);

			}
		}
	}

}
