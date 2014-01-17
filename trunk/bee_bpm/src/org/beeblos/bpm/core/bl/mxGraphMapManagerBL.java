package org.beeblos.bpm.core.bl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;

public class mxGraphMapManagerBL {

	private static final Log logger = LogFactory.getLog(mxGraphMapManagerBL.class.getName());
	
	public mxGraphMapManagerBL() {

	}
	
	/**
	 * @author dmuleiro 20130910
	 * 
	 * decodes the processXmlMap String into a mxGraph and returns it.
	 *
	 * @param String processXmlMap
	 * 
	 * @return mxGraph
	 */
	public static mxGraph decodeXmlMapIntoMxGraph(String processXmlMap){
		
		logger.info("mxGraphMapManagerBL.decodeXmlMapIntoMxGraph");

		mxGraph graph = new mxGraph();
		
		if (processXmlMap != null && !"".equals(processXmlMap)){
			
			Document xmlParsedDoc = mxXmlUtils.parseXml(processXmlMap);
			mxCodec dec = new mxCodec(xmlParsedDoc.getDocumentElement().getOwnerDocument());
			dec.decode(xmlParsedDoc.getDocumentElement(), graph.getModel());

		}
		
		return graph;
		
	}
	
	/**
	 * @author dmuleiro 20130910
	 * 
	 * encodes the processXmlMap mxGraph into a String and returns it.
	 *
	 * @param String mxGraph
	 * 
	 * @return String
	 */
	public static String encodeMxGraphIntoXmlMap(mxGraph graph){

		logger.info("mxGraphMapManagerBL.encodeMxGraphIntoXmlMap");

		return mxXmlUtils.getXml(new mxCodec().encode(graph.getModel()));

		
	}
	
	/**
	 * @author dmuleiro 20130910
	 * 
	 * changes the "Workflow" map name (associated with the processHead.name) and return the new map. if we cant we
	 * create an empty new processXmlMap
	 *
	 * @param String processXmlMap
	 * @param String newProcessName
	 * 
	 * @return String
	 * @throws WStepSequenceDefException 
	 * @throws WProcessDefException 
	 */
	public static String getXmlMapWithNewProcessName(String processXmlMap, 
			String newProcessName, Integer processId, Integer currentUserId) 
					throws WProcessDefException, WStepSequenceDefException{

		logger.info("mxGraphMapManagerBL.getXmlMapWithNewProcessName");

		if (processXmlMap == null || "".equals(processXmlMap)){
			
			logger.debug("mxGraphMapManagerBL.getXmlMapWithNewProcessName() The process has not a valid xmlMap. Proceding to create an empty one for the new process.");
			processXmlMap = createEmptyProcessXmlMap(processId, currentUserId);

		} 
						
		mxGraph graph = decodeXmlMapIntoMxGraph(processXmlMap);
		
		((mxCell) graph.getModel().getRoot()).setAttribute("label", newProcessName);

		String newXmlMap = encodeMxGraphIntoXmlMap(graph);
		
		
		// lo codificamos para devolverlo
		return newXmlMap;

	}
	
	/**
	 * PENDIENTE DE CAMBIAR Y PONER CON LIBRERIA jGRAPH
	 * 
	 * @author dmuleiro 20130910
	 * 
	 * creates an empty new processXmlMap
	 *
	 * @param Integer newProcessId
	 * @param Integer currentUserId
	 * 
	 * @return String
	 */
	public static String createEmptyProcessXmlMap(Integer newProcessId, Integer currentUserId) 
			throws WProcessDefException, WStepSequenceDefException{
		
		String returnValue = "";
		
		// lo recargo para tener el process.getBeginStep().getName() que me hará falta para crear el mapa
		WProcessDef process = null;
		if (newProcessId != null){
			process = new WProcessDefBL().getWProcessDefByPK(newProcessId, currentUserId);
		} else {
			return null;
		}
		
		if (process == null){
			return null;
		}
		
		returnValue += "<mxGraphModel><root>";
		returnValue += "<Workflow label=\"" + process.getName() + "\" id=\"0\" description=\"\" spId=\"" + process.getId() + "\"><mxCell/></Workflow>";
		returnValue += "<Layer label=\"Default Layer\" description=\"\" id=\"1\"><mxCell parent=\"0\"/></Layer>";
		
		returnValue += "<Symbol label=\"Begin\" description=\"\" href=\"\" id=\"3\"><mxCell style=\"symbol;image=images/symbols/event.png\" vertex=\"1\" parent=\"1\"><mxGeometry x=\"320\" y=\"230\" width=\"32\" height=\"32\" as=\"geometry\"/></mxCell></Symbol>";
		
		if (process.getBeginStep() != null){
			String responses = "responses=\"";
			if (process.getBeginStep().getResponse() != null
				&& !process.getBeginStep().getResponse().isEmpty()){
				for (WStepResponseDef response : process.getBeginStep().getResponse()){
					
					responses += response.getName() + "|"; 
					
				}
			} 
			responses += "\"";
			
			returnValue += "<Edge label=\"\" description=\"\" id=\"4\"><mxCell style=\"straightEdge\" edge=\"1\" parent=\"1\" source=\"3\" target=\"2\"><mxGeometry relative=\"1\" as=\"geometry\"/></mxCell></Edge>";
			
			returnValue += "<Task description=\"" + process.getBeginStep().getStepComments() + 
					"\" href=\"\" id=\"2\" label=\"" + process.getBeginStep().getName() 
					+ "\" spId=\"" + process.getBeginStep().getId() + "\" " 
					+ responses + "><mxCell parent=\"1\" vertex=\"1\">"
					+ "<mxGeometry as=\"geometry\" height=\"32\" width=\"72\" x=\"430\" y=\"230\"/></mxCell></Task>";
		}
		
		returnValue += "</root></mxGraphModel>";
		
		return returnValue;
		
	}

	
}
