/**
 * 
 */
package org.beeblos.bpm.core.util;

/**
 * @author nestor
 *
 */
public class Resourceutil {

	public static String getStringProperty(String pk, String defaultValue) {
		String ret = null;
		ret = getStringProperty(pk);
		if (ret == null && defaultValue != null)
			return defaultValue;
		return ret;
	}

	public static String getStringProperty(String pk) {
		String ret = null;
		try {

			ret = Configuration.getConfigurationResourceBundle().getString(pk);

		} catch (Exception e) {

			System.out
					.println(">>>>>>>>>>>>>>>>>>>>>> ERROR LEYENDO PROPIEDAD : ["
							+ pk
							+ "] del configuration.properties ...!!!!!!!!!!!!!");
			e.printStackTrace();

		}
		return ret;
	}

	// dml 20120315
	public static String getStringPropertyEmailTemplate(String pk, String defaultValue) {
		String ret = null;
		ret = getStringPropertyEmailTemplate(pk);
		if (ret == null && defaultValue != null)
			return defaultValue;
		return ret;
	}

	// dml 20120315
	public static String getStringPropertyEmailTemplate(String pk) {
		String ret = null;
		try {

			ret = Configuration.getEmailTemplatePropertiesHelperResourceBundle().getString(pk);

		} catch (Exception e) {

			System.out
					.println(">>>>>>>>>>>>>>>>>>>>>> ERROR LEYENDO PROPIEDAD : ["
							+ pk
							+ "] del email_field_mapping.properties ...!!!!!!!!!!!!!");
			e.printStackTrace();

		}
		return ret;
	}

	public static Integer getIntegerProperty(String pk, Integer defaultValue) {
		Integer ret = null;
		ret = getIntegerProperty(pk);
		if (ret == null && defaultValue != null)
			return defaultValue;
		return ret;
	}
	
	public static Integer getIntegerProperty(String pk) {
		Integer ret = null;
		try {

			ret = new Integer(Configuration.getConfigurationResourceBundle()
					.getString(pk));

		} catch (Exception e) {

			System.out
					.println(">>>>>>>>>>>>>>>>>>>>>> ERROR LEYENDO PROPIEDAD : ["
							+ pk
							+ "] del configuration.properties ...!!!!!!!!!!!!!");
			e.printStackTrace();

		}
		return ret;
	}
	
	public static Double getDoubleProperty(String pk, Double defaultValue) {
		Double ret = null;
		ret = getDoubleProperty(pk);
		if (ret == null && defaultValue != null)
			return defaultValue;
		return ret;
	}
	
	public static Double getDoubleProperty(String pk) {
		Double ret = null;
		try {

			ret = new Double(Configuration.getConfigurationResourceBundle()
					.getString(pk));

		} catch (Exception e) {

			System.out
					.println(">>>>>>>>>>>>>>>>>>>>>> ERROR LEYENDO Double PROPIEDAD : ["
							+ pk
							+ "] del configuration.properties ...!!!!!!!!!!!!!");
			e.printStackTrace();

		}
		return ret;
	}	
}
