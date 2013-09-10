package org.beeblos.bpm.core.bl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WStepWorkSequence;
import org.xml.sax.SAXException;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxStyleUtils;
import com.mxgraph.view.mxGraph;
import com.sp.common.util.StringPair;

public class WorkflowEditorMxBL {

	private static final Log logger = LogFactory.getLog(WorkflowEditorMxBL.class.getName());

	public static String TASK = "Task";
	public static String SYMBOL = "Symbol";
	public static String EDGE = "Edge";
	
	public static String BEGIN_SYMBOL_STYLE = "images/symbols/event-green.png";

	private final static String DEFAULT_ERROR = "ERROR";
	
	private final static String RED = "red";
	private final static String GREEN = "green";
	private final static String BROWN = "brown";
	
	public WorkflowEditorMxBL() {

	}
	
	public String paintXmlMap(String processXmlMap, Integer workId, Integer currentUserId) 
			throws ParserConfigurationException, SAXException, IOException, WProcessDefException, 
			WStepDefException, WStepWorkException, WStepWorkSequenceException {
		
		String returnValue = null;
		
		if (processXmlMap == null){
			return returnValue;
		}
		
		// lista para pintar los "Task"
		List<WStepWork> wswList = 
				new WStepWorkBL().getWorkListByIdWorkAndStatus(workId, null, currentUserId);
		
		if (wswList == null
				|| wswList.isEmpty()){
			return returnValue;
		}

		// lista para pintar los "Edge" ( no devuelve null si no hay porque puede que no haya ninguna ruta procesada aun)
		List<WStepWorkSequence> wswsList = 
				new WStepWorkSequenceBL().getWStepWorkSequencesByWorkingProcessId(workId, currentUserId);

		// decodificamos nuestro xml map en un graph con nuestra BL
		mxGraph graph = mxGraphMapManagerBL.decodeXmlMapIntoMxGraph(processXmlMap);
		
		List<Object> xmlMapVertexList = new ArrayList<Object>(
				Arrays.asList(mxGraphModel.getChildCells(graph.getModel(), graph.getDefaultParent(), true, false)));
		String spId = "";
		String xmlId = "";
		String spName = "";
		String beginSymbolId = "";
		
		// lista de pares <xmlId, spId> de las Tasks para despues asociar los WStepResponses con el
		// correspondiente spId del WStepDef
		List<StringPair> stepXmlIdsList = new ArrayList<StringPair>();

		mxCell xmlMapVertex = null;
		for (Object xmlMapVertexObject : xmlMapVertexList){
			
			xmlMapVertex = (mxCell) xmlMapVertexObject;
			
			if (xmlMapVertex.isVertex()){
				
				if (xmlMapVertex.getValue().toString().indexOf(SYMBOL) >= 0){
					
					spName = xmlMapVertex.getAttribute(mxConstants.SHAPE_LABEL);
					if (spName != null
							&& spName.equals("Begin")){
						beginSymbolId = xmlMapVertex.getId();
						xmlMapVertex.setStyle(mxStyleUtils.setStyle(xmlMapVertex.getStyle(), mxConstants.STYLE_IMAGE, BEGIN_SYMBOL_STYLE));
					}
					
				} else if (xmlMapVertex.getValue().toString().indexOf(TASK) >= 0){
					
					xmlId = xmlMapVertex.getId();
					spId = xmlMapVertex.getAttribute("spId");
					
					// el nombre no puede ser vacío, por lo tanto le ponemos el que tenemos por defecto
					if (spId != null
						&& !spId.isEmpty()){
						for (WStepWork wsw : wswList){
							
							if (wsw.getPreviousStep() != null
									&& wsw.getPreviousStep().getId() != null
									&& wsw.getPreviousStep().getId().equals(Integer.valueOf(spId))){
								
								xmlMapVertex.setStyle(mxStyleUtils.setStyle(xmlMapVertex.getStyle(), mxConstants.STYLE_STROKECOLOR, GREEN));
								
							}

							if (wsw.getCurrentStep() != null
									&& wsw.getCurrentStep().getId() != null
									&& wsw.getCurrentStep().getId().equals(Integer.valueOf(spId))){
								
								// si tiene decided date se pone como acabado
								if (wsw.getDecidedDate() != null){
									xmlMapVertex.setStyle(mxStyleUtils.setStyle(xmlMapVertex.getStyle(), mxConstants.STYLE_STROKECOLOR, GREEN));
								} else {
									xmlMapVertex.setStyle(mxStyleUtils.setStyle(xmlMapVertex.getStyle(), mxConstants.STYLE_STROKECOLOR, RED));
								}									
							}
						}							
						// esta lista auxiliar será para pintar los "edge"
						stepXmlIdsList.add(new StringPair(xmlId, spId));					
					}						
				}
			} 				
		}
		
		
		List<Object> xmlMapEdgeList = new ArrayList<Object>(
				Arrays.asList(mxGraphModel.getChildCells(graph.getModel(), graph.getDefaultParent(), false, true)));

		List<mxCell> paintedEdgeList = new ArrayList<mxCell>();
		List<mxCell> turnBackEdgeList = new ArrayList<mxCell>();
		
		String xmlFromStepId;
		String xmlToStepId;
		String spFromStepId;
		String spToStepId;

		for (WStepWorkSequence wsws : wswsList) {

			xmlFromStepId = "";
			xmlToStepId = "";
			spFromStepId = "";
			spToStepId = "";
			
			mxCell xmlMapEdge = null;
			for (Object xmlMapEdgeObject : xmlMapEdgeList){
				
				xmlMapEdge = (mxCell) xmlMapEdgeObject;
				
				if (xmlMapEdge.isEdge()){

					if (xmlMapEdge.getValue().toString().indexOf(EDGE) >= 0){
						
						spId = "";
						xmlFromStepId = "";
						spFromStepId = "";
						
						spId = xmlMapEdge.getAttribute("spId");
						
						xmlFromStepId = xmlMapEdge.getSource().getId();
						xmlToStepId = xmlMapEdge.getTarget().getId();
						
						// si el "edge" sale del begin symbol lo pintamos
						if (xmlFromStepId != null
								&& beginSymbolId != null
								&& beginSymbolId.equals(xmlFromStepId)){
							xmlMapEdge.setStyle(mxStyleUtils.setStyle(xmlMapEdge.getStyle(), mxConstants.STYLE_STROKECOLOR, GREEN));
							paintedEdgeList.add(xmlMapEdge);
							beginSymbolId = null; // como solo puede haber un "beginSymbol" evitamos volver a entrar en este if
							break;
						}
						
						// el nombre SI puede ser vacío, pero si la ruta va a ningun sitio se le pone "ERROR"
						if (xmlToStepId == null
							|| xmlToStepId.isEmpty()){
							spName = DEFAULT_ERROR;
							xmlMapEdge.setStyle(mxStyleUtils.setStyle(xmlMapEdge.getStyle(), mxConstants.STYLE_FONTCOLOR, RED));
						} else {
							if (spName != null
									&& spName.equals(DEFAULT_ERROR)){
								spName = "";
								xmlMapEdge.setStyle(mxStyleUtils.removeStylename(xmlMapEdge.getStyle(), mxConstants.STYLE_FONTCOLOR));
							}
						}
	
						// comprobamos que viene de un step valido
						spFromStepId = "";
						spToStepId = "";
						for(StringPair stepPair : stepXmlIdsList){
							
							// comparamos el xmlId del "source" del Edge con los que tenemos guardados
							// del bucle anterior para ver si se corresponde a un Task valido o el Edge
							// sale de un elemento que no es un Task
							if (xmlFromStepId.equals(stepPair.getString1())){
							
								spFromStepId = stepPair.getString2();
								break;
								
							}
							
						}
						
						// si viene de un step valido vemos si va a un step valido sino tampoco lo podremos guardar
						if (spFromStepId != null
								&& !"".equals(spFromStepId)){
							
							for(StringPair stepPair : stepXmlIdsList){
								
								// comparamos el xmlId del "target" del Edge con los que tenemos guardados
								// del bucle anterior para ver si se corresponde a un Task valido o el Edge
								// llega a un elemento que no es un Task
								if (xmlToStepId.equals(stepPair.getString1())){
								
									spToStepId = stepPair.getString2();
									break;
									
								}
								
							}
							
						}
						
						// si tiene fromStep y toStep vemos si es una ruta procesada para pintar
						if (spFromStepId != null
							&& !spFromStepId.isEmpty()
							&& spToStepId != null
							&& !"".equals(spToStepId)){
							
							// si es distinto de null es que es un PROCESS hacia adelante
							if (wsws.getStepSequence() != null){
		
								if (wsws.getStepSequence().getFromStep() != null
									&& wsws.getStepSequence().getFromStep().getId() != null
									&& wsws.getStepSequence().getFromStep().getId().equals(Integer.valueOf(spFromStepId))
									&& wsws.getStepSequence().getToStep() != null
									&& wsws.getStepSequence().getToStep().getId() != null
									&& wsws.getStepSequence().getToStep().getId().equals(Integer.valueOf(spToStepId))
									&& !wsws.isSentBack()){
										
									xmlMapEdge.setStyle(mxStyleUtils.setStyle(xmlMapEdge.getStyle(), mxConstants.STYLE_STROKECOLOR, GREEN));
									paintedEdgeList.add(xmlMapEdge);
									break;
									
								}			
								
							// si es null es que es un TURNBACK hacia atras
							} else if (wsws.getBeginStep() != null
								&& wsws.getBeginStep().getId() != null
								&& wsws.getBeginStep().getId().equals(Integer.valueOf(spToStepId))
								&& wsws.getEndStep() != null
								&& wsws.getEndStep().getId() != null
								&& wsws.getEndStep().getId().equals(Integer.valueOf(spFromStepId))
								&& wsws.isSentBack()){
								
//								Object newTurnBackEdge = graph.createEdge(graph.getDefaultParent(), null, xmlMapEdge.getValue(), 
//										(Object) xmlMapEdge.getTarget(), (mxCell) xmlMapEdge.getSource(), 
//										mxStyleUtils.setStyle(xmlMapEdge.getStyle(), mxConstants.STYLE_STROKECOLOR, BROWN));
//								((mxCell) newTurnBackEdge).setAttribute(mxConstants.SHAPE_LABEL, this._getNewNameTurnBackCounts(xmlMapEdge.getAttribute(mxConstants.SHAPE_LABEL)));
//								paintedEdgeList.add((mxCell) newTurnBackEdge);
								xmlMapEdge.setStyle(mxStyleUtils.setStyle(xmlMapEdge.getStyle(), mxConstants.STYLE_STROKECOLOR, BROWN));
								xmlMapEdge.setAttribute(mxConstants.SHAPE_LABEL, this._getNewNameTurnBackCounts(xmlMapEdge.getAttribute(mxConstants.SHAPE_LABEL)));
								paintedEdgeList.add((mxCell) xmlMapEdge);
								break;
							
							}
		
						}							
					}					
				}			
			}
		}
		
		// dml 20130905 - PARA PINTAR POR ENCIMA LAS QUE TIENEN COLOR:
		
		// eliminamos todos los edge del grago para meter los que esten pintados por encima de todo
		graph.removeCells(xmlMapEdgeList.toArray());

		// eliminamos del total las que estan pintadas
		xmlMapEdgeList.removeAll(paintedEdgeList);
		
		// añadimos al grafo primero las sin pintar
		graph.addCells(xmlMapEdgeList.toArray());

		// añadimos por ultimo las pintadas para que vayan por encima
		graph.addCells(paintedEdgeList.toArray());
		
		// lo codificamos para devolverlo con nuestra BL
		returnValue = mxGraphMapManagerBL.encodeMxGraphIntoXmlMap(graph);
			
		return returnValue;

	}

	private String _getNewNameTurnBackCounts(String edgeName){

		if (edgeName == null){
			edgeName = "";
		}
		
		if (edgeName.contains("(")
				&& edgeName.contains(")")){
			String n = edgeName.substring(edgeName.indexOf("("), edgeName.indexOf(")")+1);
			String number = n.substring(1, n.length()-1);
			Integer newNumber = Integer.valueOf(number) + 1;
			edgeName = edgeName.replace(n, "(" + newNumber + ")");
		} else {
			edgeName += "(1)";
		}
			
		return edgeName; 
		
	}
	
	
}
