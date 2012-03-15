package org.beeblos.bpm.core.util;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Configuration {

	private static final Log logger = LogFactory.getLog(Configuration.class);
	
	public static ResourceBundle getConfigurationResourceBundle() {
		
		try {

			ResourceBundle rb =  ResourceBundle.getBundle("configuration");
			return rb;
		} catch (Exception e) {
			logger.error("---->>> ERROR : CAN'T READ RESOURCE BUNDLE [configuration.properties]");
			logger.error("error: "+e.getClass()+" -- "+e.getMessage()+" -- "+e.getLocalizedMessage()+" -- "+e.getCause());
		}
		return null;
	}
	public static ResourceBundle getConfigurationRepositoryResourceBundle() {
	
		try {
			
			ResourceBundle rb = ResourceBundle.getBundle("configuration");
			return rb;
		} catch (Exception e) {
			logger.error("---->>> ERROR : CAN'T READ RESOURCE BUNDLE [configurationRepository]");
			logger.error("error: "+e.getClass()+" -- "+e.getMessage()+" -- "+e.getLocalizedMessage()+" -- "+e.getCause());
		}
		return null;

		
	}

	public static ResourceBundle getEmailTemplatePropertiesHelperResourceBundle() {
		
		try {

			ResourceBundle rb =  ResourceBundle.getBundle("emailTemplatePropertiesHelper");
			return rb;
		} catch (Exception e) {
			logger.error("---->>> ERROR : CAN'T READ RESOURCE BUNDLE [emailTemplatePropertiesHelper.properties]");
			logger.error("error: "+e.getClass()+" -- "+e.getMessage()+" -- "+e.getLocalizedMessage()+" -- "+e.getCause());
		}
		return null;
	}
}
