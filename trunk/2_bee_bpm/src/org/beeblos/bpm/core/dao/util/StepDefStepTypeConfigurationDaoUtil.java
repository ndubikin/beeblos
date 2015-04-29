package org.beeblos.bpm.core.dao.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.WStepTypeDefDao;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepTypeDefException;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepTypeDef;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sp.daemon.dao.DaemonConfDao;
import com.sp.daemon.email.EmailDConf;
import com.sp.daemon.error.DaemonConfException;
import com.sp.daemon.util.EmailDaemonConfigurationList;

/**
 * NOTE dml 20150429: As it is a DAO UTIL, it could only has references to other DAOS, never BLS!!
 * 
 * This class contains the methods to manage the "stepTypeConfiguration" XML string
 * into the WStepDef. 
 * 
 * It loads the information from the XML into the object class hierarchy when we get a WStepDef from
 * the DataBase and saves the information into the XML when the WStepDef is persisted
 * 
 * @author dmuleiro 20150424
 *
 */
public class StepDefStepTypeConfigurationDaoUtil {

	private static final Log logger = LogFactory.getLog(StepDefStepTypeConfigurationDaoUtil.class.getName());
	
	/**
	 * This is called when we are adding a new "WStepDef". Here comes the WStepDef with a
	 * "GenericStepType" and we have to decide if it is correct or if it has another type.
	 * After this, we have to fill the CORRECT "StepType" with all its "default" param values
	 * to persist this information.
	 * 
	 * @author dmuleiro 20150429
	 * 
	 * @param wsd
	 * @throws WStepTypeDefException 
	 */
	public static void createAndFillStepTypeConfigurationXml(
			WStepDef wsd) throws WStepDefException{
		
		if (wsd != null){
			
			if (wsd.getStepTypeDef() == null 
				|| wsd.getStepTypeDef().getId() == null 
				|| wsd.getStepTypeDef().getId().equals(0)){
				String mess = "The step def has not a valid step type!";
				logger.error(mess);
				throw new WStepDefException(mess);
			}
		
			/**
			 * Obtaining WStepTypeDef information in order to know what kind does it have.
			 */
			WStepTypeDef stepTypeDef = null;
			try {
				stepTypeDef = new WStepTypeDefDao().getWStepTypeDefByPK(
						wsd.getStepTypeDef().getId());
			} catch (WStepTypeDefException e) {
				String mess = "Error trying to get the COMPLETE step type def. "
						+ (e.getMessage()!=null?". "+e.getMessage():"")
						+ (e.getCause()!=null?". "+e.getCause():"");
				logger.error(mess);
				throw new WStepDefException(mess);
			}
			
			if (stepTypeDef == null || stepTypeDef.getId() == null
					|| stepTypeDef.getRelatedClass() == null 
					|| "".equals(stepTypeDef.getRelatedClass().trim())){
				String mess = "The step def's step type is not valid!";
				logger.error(mess);
				throw new WStepDefException(mess);
			}
			
			/**
			 * Creating the CORRECT type of the WStepTypeDef (the type is inside the "relatedClass"
			 * param
			 */
			WStepTypeDef relatedStepTypeDef = _getRelatedStepType(stepTypeDef.getRelatedClass());
			
			/**
			 * Using the generic "stepTypeDef" information to fill the new "relatedStepTypeDef" 
			 * (it will have the correct type of WStepTypeDef (MessageBegin, GenericStepType, ...)
			 */
			relatedStepTypeDef.setObj(stepTypeDef);
			
			/**
			 * Marshaling the "relatedStepTypeDef"
			 */
			String relatedStepTypeXml = relatedStepTypeDef.marshal();
			
			/**
			 * Adding the correct StepType marshaled xml into the "stepTypeConfiguration" to 
			 * persist it in the database
			 */
			wsd.setStepTypeConfiguration(relatedStepTypeXml);
			
			/**
			 * Deleting the StepTypeDef from the WStepDef to add it to the database
			 */
			wsd.setStepTypeDef(null);
			
		}
		
	}
		
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
		
			String relatedClassName = _getRelatedClassName(wsd);
			
			WStepTypeDef relatedStepType = _getRelatedStepType(relatedClassName);
			
			if (relatedStepType == null){
				String mess = "Error trying to get real object from WStepTypeDef relatedClass: "
						+ wsd.getStepTypeDef().getRelatedClass();
				logger.error(mess);
				throw new WStepDefException(mess);
			}
			
			
			try {
				/**
				 * Unmarshaling WStepTypeDef... (it could be MessageBegin, GenericStepType...
				 */
				relatedStepType.unmarshal(wsd.getStepTypeConfiguration());
				
				/**
				 * Associates StepType to the current WStepTypeDef
				 */
				wsd.setStepTypeDef(relatedStepType);
				
				/**
				 * Si el "StepType" es de un tipo que tiene un set de "EmailDConf", tras cargar el XML dentro de
				 * la jerarquía de clases, como del XML solo vendrán los "ids" tenemos que recuperar el objeto
				 * entero
				 */ 
				_getEmailDConfListById(wsd);
				
				/**
				 * Borramos el valor del stepTypeConfiguration porque en la carga debe estar en null
				 * (solo lo debemos tener en la jerarquia de clases correspondientes)
				 */
				wsd.setStepTypeConfiguration(null);
				
			} catch (Exception e){
				String mess = "Error trying to unmarshal StepTypeDef. "
						+ (e.getMessage()!=null?". "+e.getMessage():"")
						+ (e.getCause()!=null?". "+e.getCause():"");
				logger.error(mess);
				throw new WStepDefException(mess);
			}
			
		}
		
	}
	
	/**
	 * Gets from the Xml the "relatedClass" attribute. Then we are going to use it to
	 * unmarshal the complete Xml into the hierarchy "relatedClass".
	 * 
	 * @author dmuleiro 20150429
	 * 
	 * @param wsd
	 * @return
	 * @throws WStepDefException 
	 */
	private static String _getRelatedClassName(WStepDef wsd) throws WStepDefException{
		
		try {
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
		    InputSource is = new InputSource();
		    is.setCharacterStream(new StringReader(wsd.getStepTypeConfiguration()));
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("relatedClass");

			if (nodeLst != null && nodeLst.getLength() == 1
					&& nodeLst.item(0).getFirstChild() != null){
				return nodeLst.item(0).getFirstChild().getNodeValue();
			}
			
		} catch (IOException e){
			String mess = "Error trying to get StepTypeDef relatedClass. "
					+ (e.getMessage()!=null?". "+e.getMessage():"")
					+ (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new WStepDefException(mess);
		} catch (SAXException e) {
			String mess = "Error trying to get StepTypeDef relatedClass. "
					+ (e.getMessage()!=null?". "+e.getMessage():"")
					+ (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new WStepDefException(mess);
		} catch (ParserConfigurationException e) {
			String mess = "Error trying to get StepTypeDef relatedClass. "
					+ (e.getMessage()!=null?". "+e.getMessage():"")
					+ (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new WStepDefException(mess);
		}

		return null;

	}

	private static WStepTypeDef _getRelatedStepType(String relatedClassName) throws WStepDefException{
		
		/**
		 * If related class is null we cannot continue unmarshaling because we don't know
		 * what class of object we must fill
		 */
		if (relatedClassName == null){
			String mess = "Step def related class is null";
			logger.error(mess);
			throw new WStepDefException(mess);
		}
		
		try {
			
			Class<?> relatedClass = Class.forName(relatedClassName);
			
			/**
			 * If the class is correct, we create a new instance
			 */
			if (relatedClass == null 
					|| WStepTypeDef.class.isAssignableFrom(relatedClass)){
				
				return (WStepTypeDef) relatedClass.newInstance();
				
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
		
		return null;

	}
	
	/**
	 * Si el "StepType" es de un tipo que tiene un set de "EmailDConf", tras cargar el XML dentro de
	 * la jerarquía de clases, como del XML solo vendrán los "ids" tenemos que recuperar el objeto
	 * entero
	 * 
	 * @author dmuleiro 20150428
	 * 
	 * @param wsd
	 * @throws DaemonConfException 
	 */
	private static void _getEmailDConfListById(WStepDef wsd) throws DaemonConfException{
		
		if (wsd.getStepTypeDef() instanceof EmailDaemonConfigurationList){
			
			EmailDaemonConfigurationList edcl = (EmailDaemonConfigurationList) wsd.getStepTypeDef();
			
			if (edcl != null && edcl.getEmailDaemonConfiguration() != null
					&& !edcl.getEmailDaemonConfiguration().isEmpty()){
				
				Set<EmailDConf> auxList = new HashSet<EmailDConf>();
				for (Integer idEmailDConf : ((EmailDaemonConfigurationList) wsd.getStepTypeDef()).getEmailDaemonConfigurationIdList()) {
					
					try {
						
						auxList.add((EmailDConf) new DaemonConfDao()
							.getDaemonConfSubObjectByPK(idEmailDConf, EmailDConf.class));
					
					} catch (DaemonConfException e) {
						String mess = "Error trying to get EmailDaemonConfiguration by id when filling class hierarchy by StepTypeConfiguration Xml map"
								+ (e.getMessage()!=null?"."+e.getMessage():"")
								+ (e.getCause()!=null?"."+e.getCause():"");
						logger.error(mess);
						throw new DaemonConfException(mess);
					}
					
				}
				
				((EmailDaemonConfigurationList) wsd.getStepTypeDef()).setEmailDaemonConfiguration(auxList);
				
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
