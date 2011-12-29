/**
 * 
 */
package org.beeblos.bpm.web.common;

import static org.beeblos.bpm.web.util.ConstantsWeb.SUCCESS_WELCOME;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author wil
 *
 */
public class IndicadoresGeneralesBean {

	private static final Log logger = LogFactory.getLog(IndicadoresGeneralesBean.class);
	
	
	public IndicadoresGeneralesBean() {
		super();
		_init();
	}

	private void _init()  {
		

	}

	
	public String setIndicadores() {
		_init();
		return SUCCESS_WELCOME;
	}

	
	
	
}
