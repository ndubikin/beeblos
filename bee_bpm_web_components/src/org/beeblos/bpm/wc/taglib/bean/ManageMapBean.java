package org.beeblos.bpm.wc.taglib.bean;

import static com.sp.common.util.ConstantsCommon.ERROR_MESSAGE;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WorkflowEditorMxBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.xml.sax.SAXException;

public class ManageMapBean extends CoreManagedBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5536403590292026631L;

	private static final Log logger = LogFactory.getLog(ManageMapBean.class);

	private String xmlMap;
	
	private Integer currentProcessId;
	private Integer currentProcessWorkId;
	
	
	public ManageMapBean() {

		logger.debug(">>> ingresando constructor PintaMapaBean ...");
		
		this._reset();
		this.init(); 
	}		

	private void _reset() {
		
		this.xmlMap = "";
		
		this.currentProcessId = null;
		this.currentProcessWorkId = null;

	}
	
	public void init() {
		
	}
	
	/**
	 * Pinta en mapa y lo guarda en la variable "xmlMap" para usarlo desde la vista "workflowviewxmlmap.xhtml"
	 * 
	 * @author dmuleiro 20140122
	 * 
	 * @param currentProcessId
	 * @param currentProcessWorkId
	 */
	public void paintXmlMap(Integer currentProcessId, Integer currentProcessWorkId, Integer currentUserId) {
		
		if (this.currentProcessId != null
				&& !this.currentProcessId.equals(0)
				&& this.currentProcessWorkId != null
				&& !this.currentProcessWorkId.equals(0)){
			
			logger.info("<DEBUG> ManageMapBean._paintXmlMap(): currentProcessId: " + this.currentProcessId
					+ " currentProcessWorkId: " + this.currentProcessWorkId);
	
			if (this.currentProcessId != null
					&& !this.currentProcessId.equals(0)){
				try {
					
					String xmlMapTmp = new WProcessDefBL().getProcessDefXmlMap(this.currentProcessId, currentUserId);
					logger.info("<DEBUG> Cargado mapa del proceso: " + xmlMapTmp);
	
					
					this.xmlMap = new WorkflowEditorMxBL().paintXmlMap(
							xmlMapTmp, this.currentProcessWorkId, currentUserId);
					logger.info("<DEBUG> Mapa xml del proceso pintado: " + xmlMap);
									
				} catch (IOException e) {
					String message = "Error intentando pintar el mapa para el proceso con id=" + currentProcessId;
					super.createWindowMessage(ERROR_MESSAGE, message, e);
				} catch (WProcessDefException e) {
					String message = "Error intentando pintar el mapa para el proceso con id=" + currentProcessId;
					super.createWindowMessage(ERROR_MESSAGE, message, e);
				} catch (ParserConfigurationException e) {
					String message = "Error intentando pintar el mapa para el proceso con id=" + currentProcessId;
					super.createWindowMessage(ERROR_MESSAGE, message, e);
				} catch (SAXException e) {
					String message = "Error intentando pintar el mapa para el proceso con id=" + currentProcessId;
					super.createWindowMessage(ERROR_MESSAGE, message, e);
				} catch (WStepWorkException e) {
					String message = "Error intentando pintar el mapa para el proceso con id=" + currentProcessId;
					super.createWindowMessage(ERROR_MESSAGE, message, e);
				} catch (WStepDefException e) {
					String message = "Error intentando pintar el mapa para el proceso con id=" + currentProcessId;
					super.createWindowMessage(ERROR_MESSAGE, message, e);
				} catch (WStepWorkSequenceException e) {
					String message = "Error intentando pintar el mapa para el proceso con id=" + currentProcessId;
					super.createWindowMessage(ERROR_MESSAGE, message, e);
				}
			}
			
		}

	}

	public String getXmlMap() {
	
		return xmlMap;
	}

	public void setXmlMap(String xmlMap) {
	
		this.xmlMap = xmlMap;
	}

	
	public Integer getCurrentProcessId() {
	
		return currentProcessId;
	}

	
	public void setCurrentProcessId(Integer currentProcessId) {
	
		this.currentProcessId = currentProcessId;
	}

	
	public Integer getCurrentProcessWorkId() {
	
		return currentProcessWorkId;
	}

	
	public void setCurrentProcessWorkId(Integer currentProcessWorkId) {
	
		this.currentProcessWorkId = currentProcessWorkId;
	}
	
}