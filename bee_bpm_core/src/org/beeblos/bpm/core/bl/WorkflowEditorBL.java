package org.beeblos.bpm.core.bl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WStepWorkSequence;
import org.beeblos.bpm.core.util.XmlConverterUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sp.common.util.StringPair;

public class WorkflowEditorBL {

	private static final Log logger = LogFactory.getLog(WorkflowEditorBL.class.getName());

	public static String LABEL = "label";
	public static String TASK = "Task";
	public static String SYMBOL = "Symbol";
	public static String EDGE = "Edge";
	
	public static String STROKE_COLOR_PROPERTY = "strokeColor";
	public static String FONT_COLOR_PROPERTY = "fontColor";
	
	public static String IMAGE_PROPERTY = "image";
	public static String BEGIN_SYMBOL_STYLE = "images/symbols/event-green.png";

	private final static String DEFAULT_ERROR = "ERROR";
	
	private final static String RED = "red";
	private final static String GREEN = "green";
	private final static String BROWN = "brown";
	
	public WorkflowEditorBL() {

	}
	
	public String paintXmlMap(String processXmlMap, Integer workId, Integer currentUserId) 
			throws ParserConfigurationException, SAXException, IOException {
		
		String returnValue = null;
		
		if (processXmlMap == null){
			return returnValue;
		}
		
		try {

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

			
			Document xmlParsedDoc = XmlConverterUtil.loadXMLFromString(processXmlMap);

			String spId = "";
			String xmlId = "";
			
			// lista de pares <xmlId, spId> de las Tasks para despues asociar los WStepResponses con el
			// correspondiente spId del WStepDef
			List<StringPair> stepXmlIdsList = new ArrayList<StringPair>();

			// 2. Primero parseo y pinto los tasks
			NodeList taskList = xmlParsedDoc.getElementsByTagName(TASK);

			// iterate the tasks (steps)
			for (int i = 0; i < taskList.getLength(); i++) {
				
				Element task = (Element) taskList.item(i);

				xmlId = task.getAttribute("id");
				spId = task.getAttribute("spId");
				
				// el nombre no puede ser vacío, por lo tanto le ponemos el que tenemos por defecto
				if (spId != null
					&& !spId.isEmpty()){
					for (WStepWork wsw : wswList){
						
						if (wsw.getPreviousStep() != null
								&& wsw.getPreviousStep().getId() != null
								&& wsw.getPreviousStep().getId().equals(Integer.valueOf(spId))){
							
							task = _setXmlElementStylePropertyValue(task, null, false, STROKE_COLOR_PROPERTY, GREEN);								
							
						}

						if (wsw.getCurrentStep() != null
								&& wsw.getCurrentStep().getId() != null
								&& wsw.getCurrentStep().getId().equals(Integer.valueOf(spId))){
							
							// si tiene decided date se pone como acabado
							if (wsw.getDecidedDate() != null){
								task = _setXmlElementStylePropertyValue(task, null, false, STROKE_COLOR_PROPERTY, GREEN);
							} else {
								task = _setXmlElementStylePropertyValue(task, null, false, STROKE_COLOR_PROPERTY, RED);
							}
							
						}

					}
					
					// esta lista auxiliar será para pintar los "edge"
					stepXmlIdsList.add(new StringPair(xmlId, spId));
			
				}

			}
			
			String spName = "";
			// 3. Ahora parseo los "Symbol" para ver si tenemos "Begin"
			NodeList symbolList = xmlParsedDoc.getElementsByTagName(SYMBOL);
			String beginSymbolId = null;
			// iterate the symbols
			for (int i = 0; i < symbolList.getLength(); i++) {
				Element symbol = (Element) symbolList.item(i);
				spName = symbol.getAttribute(LABEL);
				if (spName != null
						&& spName.equals("Begin")){
					beginSymbolId = symbol.getAttribute("id");
					symbol = _setXmlElementStylePropertyValue(symbol, null, false, IMAGE_PROPERTY, BEGIN_SYMBOL_STYLE);
					break;
				}
				
			}
			
			for (WStepWorkSequence wsws : wswsList){
				
				// 4. Ahora parseo los "Edge" que serán los WStepResponseDef de nuestro proceso
	
				// variables auxiliares que nos harán falta para comprobaciones
				String xmlFromStepId = "";
				String xmlToStepId = "";
				String spFromStepId = "";
				String spToStepId = "";
				
				NodeList edgeList = xmlParsedDoc.getElementsByTagName(EDGE);
				
				List<Element> paintedEdgeList = new ArrayList<Element>();
				List<Element> notPaintedEdgeList = new ArrayList<Element>();
				boolean actualEdgeWasPainted = false;
	
				// iterate the edges (sequences)
				for (int i = 0; i < edgeList.getLength(); i++) {
					spId = "";
					xmlFromStepId = "";
					spFromStepId = "";
					
					actualEdgeWasPainted = false;
					
					Element edge = (Element) edgeList.item(i);
	
					spId = edge.getAttribute("spId");
					
					NodeList mxCellList = edge.getElementsByTagName("mxCell");
					
					// vamos a obtener el id del step del que saldrá la respuesta
					if (mxCellList != null
							&& mxCellList.getLength() == 1){
						xmlFromStepId = ((Element) mxCellList.item(0)).getAttribute("source");
						xmlToStepId = ((Element) mxCellList.item(0)).getAttribute("target");
						
						// si el "edge" sale del begin symbol no lo persistimos
						if (xmlFromStepId != null
								&& beginSymbolId != null
								&& beginSymbolId.equals(xmlFromStepId)){
							edge = _setXmlElementStylePropertyValue(edge, null, false, STROKE_COLOR_PROPERTY, GREEN);
							actualEdgeWasPainted = true;
							paintedEdgeList.add(edge);
							continue;
						}
						
						// el nombre SI puede ser vacío, pero si la ruta va a ningun sitio se le pone "ERROR"
						if (xmlToStepId == null
							|| xmlToStepId.isEmpty()){
							spName = DEFAULT_ERROR;
							edge = this._setXmlElementStylePropertyValue(edge, FONT_COLOR_PROPERTY, false, DEFAULT_ERROR, RED);
						} else {
							if (spName != null
									&& spName.equals(DEFAULT_ERROR)){
								spName = "";
								edge = this._setXmlElementStylePropertyValue(edge, FONT_COLOR_PROPERTY, false, "", null);
							}
						}
	
						// comprobamos que viene de un step valido, si no es así no se va a guardar como WStepResponseDef
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
									
								edge = _setXmlElementStylePropertyValue(edge, null, false, STROKE_COLOR_PROPERTY, GREEN);
								actualEdgeWasPainted = true;
								paintedEdgeList.add(edge);
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
							
							edge = _setXmlElementStylePropertyValue(edge, edge.getAttribute(LABEL), true, STROKE_COLOR_PROPERTY, BROWN);
							actualEdgeWasPainted = true;
							paintedEdgeList.add(edge);
							break;
						
						}
	
					}
					
					if (!actualEdgeWasPainted){
						notPaintedEdgeList.add(edge);
					}
					
				}
				
				// elimino todos los nodos para colocar primero los no pintados y despues los pintados
/*				for (int i = 0; i < edgeList.getLength(); i++) {
					xmlParsedDoc.removeChild(edgeList.item(i));
				}
				// añado los nodos no pintados
				for (Element notPaintedNode : notPaintedEdgeList){
					xmlParsedDoc.appendChild((Node) notPaintedNode);
				}
				// añado los nodos pintados
				for (Element paintedNode : paintedEdgeList){
					xmlParsedDoc.appendChild((Node) paintedNode);
				}
*/				
			}
			
			returnValue = XmlConverterUtil.loadStringFromXml(xmlParsedDoc);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return returnValue;

	}

	private Element _setXmlElementStylePropertyValue(
			Element element, String defaultName, boolean sentBack, String property, String value){

		if (sentBack){
			
			if (defaultName == null){
				defaultName = "";
			}
			
			if (defaultName.contains("(")
					&& defaultName.contains(")")){
				String n = defaultName.substring(defaultName.indexOf("("), defaultName.indexOf(")")+1);
				String number = n.substring(1, n.length()-1);
				Integer newNumber = Integer.valueOf(number) + 1;
				defaultName = defaultName.replace(n, "(" + newNumber + ")");
			} else {
				defaultName += "(1)";
			}
			
		}

		if (defaultName != null
				&& !"".equals(defaultName)){
			
			element.setAttribute(LABEL, defaultName); // le corregimos el name al step para que el usuario se de cuenta del fallo
		
		}

		NodeList mxCellList = element.getElementsByTagName("mxCell");
		
		// vamos a obtener la celta del xmlElement para marcar en el mapa en rojo la misma para avisar al
		// usuario que este nombre lo ha rellenado el propio sistema ya que estaba vacío
		if (mxCellList != null
				&& mxCellList.getLength() == 1){
			
			Element mxCell = ((Element) mxCellList.item(0));
			String style = mxCell.getAttribute("style");
			
			String propertyAndValue = "";
			if (value != null){
				propertyAndValue = property+"=" + value + ";";			
			}
			
			if (style.isEmpty()){
				mxCell.setAttribute("style", mxCell.getAttribute("style") + propertyAndValue);
			} else {
				// si el style ya tiene fontColor elimino el que tenia y lo pongo en rojo pero 
				// dejo el resto de estilos que pudiera tener la celda
				if (style.contains(property)){
					String newStyle = "";
					String[] items = style.split(";");
					for(int i = 0; i < items.length ; i++){
						if (items[i].contains(property)){
							  newStyle += propertyAndValue;
						} else {
							  newStyle += items[i]+";";
						}
					}
					mxCell.setAttribute("style", newStyle);
				} else {
					if (value != null){
						mxCell.setAttribute("style", mxCell.getAttribute("style") + ";" + propertyAndValue);
					}
				}
			}
			
		}
		
		return element; 
		
	}
	
}
