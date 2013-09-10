package org.beeblos.bpm.core.bl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
		
		Document xmlParsedDoc = mxXmlUtils.parseXml(processXmlMap);
		mxCodec dec = new mxCodec(xmlParsedDoc.getDocumentElement().getOwnerDocument());
		dec.decode(xmlParsedDoc.getDocumentElement(), graph.getModel());

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
	 * changes the "Workflow" map name (associated with the processHead.name) and return the new map
	 *
	 * @param String processXmlMap
	 * @param String newProcessName
	 * 
	 * @return String
	 */
	public static String getXmlMapWithNewProcessName(String processXmlMap, String newProcessName){

		logger.info("mxGraphMapManagerBL.getXmlMapWithNewProcessName");

		mxGraph graph = decodeXmlMapIntoMxGraph(processXmlMap);
		
		((mxCell) graph.getModel().getRoot()).setAttribute("label", newProcessName);

		String newXmlMap = encodeMxGraphIntoXmlMap(graph);
		
		// lo codificamos para devolverlo
		return newXmlMap;

	}
	
}
