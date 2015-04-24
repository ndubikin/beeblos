package org.beeblos.bpm.core.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepTypeDefException;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepTypeDef;

public class StepDefStepTypeConfigurationUtil {

	private static final Log logger = LogFactory.getLog(StepDefStepTypeConfigurationUtil.class.getName());
	
	/**
	 * Updates the "stepTypeConfiguration" field before updating it into DB
	 * 
	 * @author dmuleiro 20150424
	 * 
	 * @param wsd
	 * @throws WStepTypeDefException 
	 */
	public static void updateStepTypeConfigurationXml(WStepDef wsd) throws WStepDefException{
		
		if (wsd != null && wsd.getStepTypeDef() != null){
		
			String newStepTypeConfiguration = null;
			try {
				newStepTypeConfiguration = wsd.getStepTypeDef().marshal();
			} catch (Exception e){
				String mess = "Error trying to marshal StepTypeDef. "
						+ (e.getMessage()!=null?". "+e.getMessage():"")
						+ (e.getCause()!=null?". "+e.getCause():"");
				logger.error(mess);
				throw new WStepDefException(mess);
			}
			
			wsd.setStepTypeConfiguration(newStepTypeConfiguration);
			
		}
		
	}
	
	/**
	 * Recovers the WStepTypeDef field from the xml "stepTypeConfiguration" 
	 * 
	 * @author dmuleiro 20150424
	 * 
	 * @param wsd
	 * @throws WStepTypeDefException 
	 */
	public static void recoverStepTypeConfigurationFromXml(WStepDef wsd) throws WStepDefException{
		
		if (wsd != null && wsd.getStepTypeConfiguration() != null
				&& !"".equals(wsd.getStepTypeConfiguration().trim())){
		
			/**
			 * If related class is null we cannot continue unmarshaling because we don't know
			 * what class of object we must fill
			 */
			if (wsd.getStepTypeDef() == null || wsd.getStepTypeDef().getRelatedClass() == null){
				String mess = "Step def related class is null";
				logger.error(mess);
				throw new WStepDefException(mess);
			}
			
			WStepTypeDef relatedStepType = null;
			try {
				
				Class<?> relatedClass = Class.forName(wsd.getStepTypeDef().getRelatedClass());
				
				/**
				 * If the class is correct, we create a new instance
				 */
				if (relatedClass == null 
						|| WStepTypeDef.class.isAssignableFrom(relatedClass)){
					relatedStepType = (WStepTypeDef) relatedClass.newInstance();
				}

			} catch (ClassNotFoundException e) {
				String mess = "ClassNotFoundException: " + e.getMessage()
						+ (e.getCause() != null?". "+e.getCause():"");
				logger.error(mess);
				throw new WStepDefException(mess);
			} catch (SecurityException e) {
				String mess = "SecurityException: " + e.getMessage()
						+ (e.getCause() != null?". "+e.getCause():"");
				logger.error(mess);
				throw new WStepDefException(mess);
			} catch (IllegalArgumentException e) {
				String mess = "IllegalArgumentException: " + e.getMessage()
						+ (e.getCause() != null?". "+e.getCause():"");
				logger.error(mess);
				throw new WStepDefException(mess);
			} catch (InstantiationException e) {
				String mess = "InstantiationException: " + e.getMessage()
						+ (e.getCause() != null?". "+e.getCause():"");
				logger.error(mess);
				throw new WStepDefException(mess);
			} catch (IllegalAccessException e) {
				String mess = "IllegalAccessException: " + e.getMessage()
						+ (e.getCause() != null?". "+e.getCause():"");
				logger.error(mess);
				throw new WStepDefException(mess);
			}
			
			if (relatedStepType == null){
				String mess = "Error trying to get real object from WStepTypeDef relatedClass: "
						+ wsd.getStepTypeDef().getRelatedClass();
				logger.error(mess);
				throw new WStepDefException(mess);
			}
			
			try {
				relatedStepType.unmarshal(wsd.getStepTypeConfiguration());
				
				wsd.setStepTypeDef(relatedStepType);
			} catch (Exception e){
				String mess = "Error trying to marshal StepTypeDef. "
						+ (e.getMessage()!=null?". "+e.getMessage():"")
						+ (e.getCause()!=null?". "+e.getCause():"");
				logger.error(mess);
				throw new WStepDefException(mess);
			}
			
		}
		
	}
	
	/**
	 * Recovers the WStepTypeDef field from the xml "stepTypeConfiguration" of an WStepDef list
	 * 
	 * @author dmuleiro 20150424
	 * 
	 * @param wsd
	 * @throws WStepTypeDefException 
	 */
	public static void recoverStepTypeConfigurationFromXml(List<WStepDef> wsdList) throws WStepDefException{
		
		if (wsdList != null && !wsdList.isEmpty()){
			for (WStepDef wStepDef : wsdList) {
				recoverStepTypeConfigurationFromXml(wStepDef);
			}
		}
		
	}
	
}
