package org.beeblos.bpm.web.ws.resources;

import static org.beeblos.bpm.core.util.Constants.NOT_DELETED_BOOL;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WProcessHeadBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepHeadBL;
import org.beeblos.bpm.core.bl.WStepResponseDefBL;
import org.beeblos.bpm.core.bl.WStepSequenceDefBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessHeadException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepHeadException;
import org.beeblos.bpm.core.error.WStepResponseDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.error.WorkflowEditorActionException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepHead;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.util.XmlConverterUtil;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sp.common.util.StringPair;

@Path("/wf")
public class WorkflowEditorAction extends CoreManagedBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final static String DEFAULT_STEP_NAME = "STEP";
	private final static String RED = "red";
	private final static String ORANGE = "orange";

	private final static String STEP_NOT_EXIST = "ERROR(1)";
	private final static String ROUTE_NOT_EXIST = "ERROR(2)";
	private final static String ROUTE_HAS_NO_END_STEP = "ERROR(3)";
	
	private final static String ROUTE_HAS_NOT_RESPONSES_NEITHER_AFTER_ALL = "WARNING(1)";
	
	private Integer currentUserId = 1000;
	private WProcessDef process = null;
	
	Document xmlParsed = null;

	/**
	 * @author dmuleiro - 20130829
	 * 
	 * WS in charge of reading a xml map (String format), processing it and saving its information in the database.
	 *
	 * @param String inputMap
	 * 
	 * @return javax.ws.rs.core.Response
	 * 
	 */
	@Path("/Save")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response save(@FormParam("xml") String inputMap) {

		try {

			System.out.println("------- INIT WS /Save -------");

			long tiempoInicio = System.currentTimeMillis();
			
			String xml = URLDecoder.decode(inputMap, "UTF-8").replace("\n", "&#xa;");

			xmlParsed = XmlConverterUtil.loadXMLFromString(xml);
			
			this._parseAndPersistXmlMap();
			
			xml = XmlConverterUtil.loadStringFromXml(xmlParsed);
			
			if (process != null
					&& process.getId() != null
					&& !process.getId().equals(0)){
				
				process.setProcessMap(xml);

				System.out.println("WS Save WProcessDefBL.updateProcessXmlMap(): " + process.getId());
				new WProcessDefBL().updateProcessXmlMap(process.getId(), xml, currentUserId);
				
			}
			
			this._publishChanges(xml);

			long totalTiempo = System.currentTimeMillis() - tiempoInicio;

			System.out.println("TIEMPO DE WS /Save:" + totalTiempo + " miliseg");
			System.out.println("------- END WS /Save -------");

			return Response.ok(xml, MediaType.TEXT_XML).build();
			
		} catch (Exception e) {
			e.printStackTrace();
			String message = "WS /Save EXCEPTION: " + e.getMessage();
			return Response.ok(message, MediaType.TEXT_XML).build();
		}

	}

	/**
	 * @author dmuleiro - 20130829
	 * 
	 * WS in charge of checking if a xml map (String format) is correctly built.
	 *
	 * @param String inputMap
	 * 
	 * @return javax.ws.rs.core.Response
	 * 
	 */
	@Path("/CheckMapIntegrity")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response checkMapIntegrity(@FormParam("xml") String inputMap) {

		try {

			System.out.println("------- INIT WS /CheckMapIntegrity -------");

			long tiempoInicio = System.currentTimeMillis();
			
			String xml = URLDecoder.decode(inputMap, "UTF-8").replace("\n", "&#xa;");

			xmlParsed = XmlConverterUtil.loadXMLFromString(xml);

			this._checkXmlMapIntegrity();
			
			xml = XmlConverterUtil.loadStringFromXml(xmlParsed);
			
			if (process != null
					&& process.getId() != null
					&& !process.getId().equals(0)){
				
				process.setProcessMap(xml);

				System.out.println("WS CheckMapIntegrity WProcessDefBL.updateProcessXmlMap(): " + process.getId());
				new WProcessDefBL().updateProcessXmlMap(process.getId(), xml, currentUserId);
				
			}
			
			this._publishChanges(xml);

			long totalTiempo = System.currentTimeMillis() - tiempoInicio;

			System.out.println("TIEMPO DE WS /CheckMapIntegrity:" + totalTiempo + " miliseg");
			System.out.println("------- END WS /CheckMapIntegrity -------");

			return Response.ok(xml, MediaType.TEXT_XML).build();
			
		} catch (Exception e) {
			e.printStackTrace();
			String message = "WS /CheckMapIntegrity EXCEPTION: " + e.getMessage();
			return Response.ok(message, MediaType.TEXT_XML).build();
		}

	}

	/**
	 * @author dmuleiro - 20130829
	 * 
	 * 
	 * WS NOT IMPLEMENTED AND CALLED.
	 * This WS has to receive a process id, it has to search and load it from the database and return it to the
	 * WS caller. 
	 *
	 * @param String inputMap
	 * 
	 * @return javax.ws.rs.core.Response
	 * 
	 */
	@Path("/InicialiceSession/{processId}")
	@GET
	@Consumes(MediaType.TEXT_HTML)
	public Response inicialiceSession(@PathParam("processId") Integer processId) {
		
		Response returnValue = null;
		
		if (processId != null){
			
			System.out.println("------- INIT WS /InicialiceSession -------");
			System.out.println("WS InicialiceSession queremos el mapa del proceso: " + processId);
			
			try {
				
				String processXmlMap = new WProcessDefBL().getProcessDefXmlMap(processId, null);
				
				if (processXmlMap != null){
					returnValue = Response.ok(processXmlMap,MediaType.TEXT_XML).build();
				} else {
					returnValue = Response.ok("ERROR WorkflowEditorAction.poll(): WProcessDef id: " + processId 
							+ " has not a valid xml map", MediaType.TEXT_XML).build();
				}

			} catch (WProcessDefException e) {
	
				returnValue = Response.ok("ERROR WorkflowEditorAction.poll(): with processId: " + processId 
						+ " returned a not valid result",MediaType.TEXT_XML).build();

			}
			
		} else {
			returnValue = Response.ok("ERROR WorkflowEditorAction.poll(): invalid processId", 
					MediaType.TEXT_XML).build();
		}
		
		System.out.println("------- END WS /InicialiceSession -------");
		return returnValue;

	}
	
	/**
	 * @author dmuleiro - 20130829
	 * 
	 * This WS has to return the Softpoint WStepDef object list related with the step id required. 
	 *
	 * @param String data
	 * 
	 * @return javax.ws.rs.core.Response
	 * 
	 */
	@Path("/getSpObjectList")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response getSpObjectList(@FormParam("data") String data) {

		String returnValue = "";
		
		try{
		
			long tiempoInicio = System.currentTimeMillis();

			String decodedChanges = URLDecoder.decode(data, "UTF-8").replace("\n", "&#xa;");
			
			System.out.println("------- INIT WS /getSpObjectList -------");
			System.out.println("WS getSpObjectList data: " + data);

			xmlParsed = XmlConverterUtil.loadXMLFromString(decodedChanges);
	
			// Si tiene el tag <Task> es que se intenta añadir un nuevo task asi que le devolvemos toda la lista 
			// para que elija si quiere alguno de los que tenemos o si quiere crear uno nuevo
			NodeList taskList = xmlParsed.getElementsByTagName("Task");
			
			if (taskList != null
					&& taskList.getLength() > 0){

				List<StringPair> bdStepComboList = new WStepDefBL().getComboList(null, null);
				if (bdStepComboList != null
						&& !bdStepComboList.isEmpty()){
					for (StringPair step : bdStepComboList){
						returnValue += step.getString2() + " (id:" + step.getString1() + "),";
					}
					
					returnValue = returnValue.substring(0, returnValue.lastIndexOf(","));
				}

			}


		long totalTiempo = System.currentTimeMillis() - tiempoInicio;
		System.out.println("TIEMPO DE WS /getSpObjectList:" + totalTiempo + " miliseg");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("WS getSpObjectList returnValue: " + returnValue);
		
		System.out.println("------- END WS /getSpObjectList -------");
		return Response.ok(returnValue, MediaType.TEXT_XML).build();

	}
	
	/**
	 * @author dmuleiro - 20130829
	 * 
	 * WS NOT IMPLEMENTED
	 * This WS has to process the change petitions from the caller
	 *
	 * @param String changes
	 * @param String newMap
	 * 
	 * @return javax.ws.rs.core.Response
	 * 
	 */
	@Path("/Notify")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void notify(@FormParam("changes") String changes, @FormParam("newMap") String newMap) {
		
		// dml 20130724
		// IMPORTANTE: NOTIFY ADEMAS DE LOS CAMBIOS "changes" QUE SE RECIBEN Y QUE HAY QUE METER EN LA BASE DE DATOS
		// SEA CUAL SEA LA NATURALEZA DEL MISMO TAMBIEN TIENE QUE PERSISTIR EL "newMap" EN EL WPROCESSDEF
		// CORRESPONDIENTE. ESTO QUEDA ANOTADO PARA HACERLO CUANDO SE HAGA TODA LA FUNCIONALIDAD DEL "NOTIFY".
		
		try{
		
			long tiempoInicio = System.currentTimeMillis();

			String decodedChanges = URLDecoder.decode(changes, "UTF-8").replace("\n", "&#xa;");
			
			System.out.println("------- INIT WS /Notify -------");

			xmlParsed = XmlConverterUtil.loadXMLFromString(decodedChanges);
	
			// si tenemos el <Workflow> no hacemos nada porque o se trata de la primera carga del mapa en el editor
			// que no tendremos que hacer nada o se trata de un /Save que se tratará por otro WS
			NodeList workflowList = xmlParsed.getElementsByTagName("Workflow");
			if (workflowList != null
					&& workflowList.getLength() > 0){
				System.out.println("WS Notify: no se hace nada porque es la primera notificacion de carga (se eliminara cuando se emplee el nuevo save creo): " + changes);				
			} else{
				System.out.println("WS Notify changes: " + changes);				
			}

			long totalTiempo = System.currentTimeMillis() - tiempoInicio;
			System.out.println("TIEMPO DE WS /Notify:" + totalTiempo + " miliseg");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("------- END WS /Notify -------");

	}

	/**
	 * @author dmuleiro - 20130829
	 * 
	 * This WS has to process the change petitions from the caller, process and save theme and return the new map to the WS caller
	 *
	 * @param String changes
	 * @param String newMap
	 * 
	 * @return javax.ws.rs.core.Response
	 * 
	 */
	@Path("/NotifyWithResponse")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response notifyWithResponse(@FormParam("changes") String changes, @FormParam("newMap") String newMap) {
		
		String returnValue = "";
		
		try{
		
			long tiempoInicio = System.currentTimeMillis();

			String decodedChanges = URLDecoder.decode(changes, "UTF-8").replace("\n", "&#xa;");
			String decodedNewMap = URLDecoder.decode(newMap, "UTF-8").replace("\n", "&#xa;");
			
			System.out.println("------- INIT WS /NotifyWithResponse -------");

			xmlParsed = XmlConverterUtil.loadXMLFromString(decodedChanges);
			
			Document newMapParsed = XmlConverterUtil.loadXMLFromString(decodedNewMap);
			
			// VAMOS A HACER UN PARSEO DE LA ENTRADA DE MOMENTO SOLO NOS QUEDAMOS CON LOS "CHANGES" DE TASK
			// 1. si lo que entra en el "notify" es un cambio de spObject
			NodeList changeStepList = xmlParsed.getElementsByTagName("changeSpObject");
			if (changeStepList != null
					&& changeStepList.getLength() > 0){
				
				newMapParsed = this._changeSpObject(newMapParsed);
				
				returnValue = this._persistXmlMapAndPublishChanges(newMapParsed);
				
			
			}
				
			
			long totalTiempo = System.currentTimeMillis() - tiempoInicio;
			System.out.println("TIEMPO DE WS /NotifyWithResponse:" + totalTiempo + " miliseg");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("------- END WS /NotifyWithResponse -------");
		return Response.ok(returnValue, MediaType.TEXT_XML).build();

	}

	/**
	 * @author dmuleiro - 20130829
	 * 
	 * WS NOT IMPLEMENTED
	 *
	 * @param String changes
	 * @param String newMap
	 * 
	 * @return javax.ws.rs.core.Response
	 * 
	 */
	@Path("/Poll")
	@GET
	@Consumes(MediaType.TEXT_HTML)
	public Response poll() {
		
		System.out.println("------- INIT WS /Poll -------");
		System.out.println("WS Poll inputMap: ");
		System.out.println("------- END WS /Poll -------");

		return 	Response.ok("WS /Poll NOT IMPLEMENTED",MediaType.TEXT_XML).build();

	}
	
	/**
	 * @author dmuleiro - 20130726
	 * 
	 * It has to receive a xml map and search in database the related object list, format it and return it
	 *
	 * @param org.w3c.dom.Document newXmlMapParsed
	 * 
	 * @return org.w3c.dom.Document
	 * 
	 * @throws NumberFormatException
	 * @throws WStepDefException
	 * @throws WStepSequenceDefException
	 * 
	 */
	private Document _changeSpObject(Document newXmlMapParsed) 
			throws NumberFormatException, WStepDefException, WStepSequenceDefException{
		
		String cellToChangeId = "";
		NodeList cellToChangeList = xmlParsed.getElementsByTagName("cellId");
		if (cellToChangeList != null
				&& cellToChangeList.getLength() > 0){
			
			for (int i = 0; i < cellToChangeList.getLength(); i++) {

				Element cellToChange = (Element) cellToChangeList.item(i);
				
				cellToChangeId = cellToChange.getAttribute("value");
				
			}
				
		}
			
		String newSpObjectId = "";
		String newSpObjectName = "";
		NodeList newSpObjectList = xmlParsed.getElementsByTagName("newSpObject");
		if (newSpObjectList != null
				&& newSpObjectList.getLength() > 0){
			
			// este for iterara una única vez ya que solo hay un elemento "Workflow"
			for (int i = 0; i < newSpObjectList.getLength(); i++) {

				Element newSpObject = (Element) newSpObjectList.item(i);
				
				String newSpObjectValue = newSpObject.getAttribute("value");
				
				if (newSpObjectValue != null && !"".equals(newSpObjectValue)){
					newSpObjectId = newSpObjectValue.substring(
							newSpObjectValue.indexOf("(id:") + 4, newSpObjectValue.indexOf(")"));
					newSpObjectName = newSpObjectValue.substring(0, newSpObjectValue.indexOf("(id:"));
				}
				
			}
				
		}
		
		if (cellToChangeId != null && !"".equals(cellToChangeId)
			&& newSpObjectId != null && !"".equals(newSpObjectId)
			&& newSpObjectName != null && !"".equals(newSpObjectName)){
			
			// recorro los "task" buscando el cellToChangeId que tenemos para cambiarle el "spId" y el "nombre".
			NodeList taskList = newXmlMapParsed.getElementsByTagName("Task");
			String xmlId = "";
			// iterate the tasks (steps)
			for (int i = 0; i < taskList.getLength(); i++) {
				
				Element task = (Element) taskList.item(i);

				xmlId = task.getAttribute("id");
				
				// si encontramos el task a cambiar hacemos todos los cambios necesarios y salimos del algoritmo.
				if (xmlId != null
					&& !xmlId.isEmpty()
					&& xmlId.equals(cellToChangeId)){
					
					WStepDef step = 
							new WStepDefBL().getWStepDefByPK(
													Integer.valueOf(newSpObjectId),
													null, //process.getProcess().getId(), // nes 20130808 por agregado de filter para step-data-field
													currentUserId);
					
					task.setAttribute("spId", newSpObjectId);
					task.setAttribute("label", newSpObjectName);
					
					if (step != null){
						task.setAttribute("description", step.getStepComments());
						task.setAttribute("rules", step.getRules());
						task.setAttribute("instructions", step.getInstructions());
					}
					
				
					String responses = "";
					if (step.getResponse() != null
							&& !step.getResponse().isEmpty()){
						for (WStepResponseDef response : step.getResponse()){
							responses += response.getName() + "|";
						}
					}
					task.setAttribute("responses", responses);
					
					break;
				
				}

			}
			
			// recorro los "edge" buscando si alguno tiene como "source" o "target" el step cambiado para reajustarlo
			NodeList edgeList = newXmlMapParsed.getElementsByTagName("Edge");
			xmlId = "";
			// iterate the tasks (steps)
			for (int i = 0; i < edgeList.getLength(); i++) {
				
				Element edge = (Element) edgeList.item(i);

				NodeList mxCellList = edge.getElementsByTagName("mxCell");
				
				// vamos a obtener el "source" y el "target" para comparar si hay alguno con el antiguo para cambiarlo
				if (mxCellList != null
						&& mxCellList.getLength() == 1){
					String xmlFromStepId = ((Element) mxCellList.item(0)).getAttribute("source");
					String xmlToStepId = ((Element) mxCellList.item(0)).getAttribute("target");

					WStepSequenceDefBL routeBL = new WStepSequenceDefBL();
					// vemos el source
					if (cellToChangeId != null && !"".equals(cellToChangeId)
							&& xmlFromStepId != null && !"".equals(xmlFromStepId)
							&& cellToChangeId.equals(xmlFromStepId)){
						
						String spId = edge.getAttribute("spId");
						if (spId != null
								&& !"".equals(spId)){
							WStepSequenceDef route = routeBL.getWStepSequenceDefByPK(
									Integer.valueOf(spId), currentUserId);
							route.setFromStep(new WStepDef(Integer.valueOf(newSpObjectId)));
							route.setValidResponses(null); // le vaciamos las responses porque ya no podrá tener las del step antiguo
							routeBL.update(route, currentUserId);
							
						}
						
						
					}
					
					// vemos el target
					if (cellToChangeId != null && !"".equals(cellToChangeId)
							&& xmlToStepId != null && !"".equals(xmlToStepId)
							&& cellToChangeId.equals(xmlToStepId)){
						
						String spId = edge.getAttribute("spId");
						if (spId != null
								&& !"".equals(spId)){
							WStepSequenceDef route = routeBL.getWStepSequenceDefByPK(
									Integer.valueOf(spId), currentUserId);
							route.setToStep(new WStepDef(Integer.valueOf(newSpObjectId)));
							routeBL.update(route, currentUserId);
						}						
					}
									
				}

			}
			
		}
		
		
		return newXmlMapParsed; 
		
	}
	
	/**
	 * @author dmuleiro - 20130726
	 * 
	 * It has to receive a xml map and persist all involved in it in database and complete the map with the new information.
	 * Then it returns the new map.
	 *
	 * @param org.w3c.dom.Document newXmlMapParsed
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws WProcessDefException
	 * @throws WStepSequenceDefException
	 * 
	 */
	private String _persistXmlMapAndPublishChanges(Document newXmlMapParsed) 
			throws IOException, NumberFormatException, WProcessDefException, WStepSequenceDefException{

		String returnValue = XmlConverterUtil.loadStringFromXml(newXmlMapParsed);
		
		// 1. capturo el tag Workflow que será el equivalente en nuestra BD a un WProcessDef
		NodeList workflowList = newXmlMapParsed.getElementsByTagName("Workflow");

		// este for iterara una única vez ya que solo hay un elemento "Workflow"
		for (int i = 0; i < workflowList.getLength(); i++) {

			Element workflow = (Element) workflowList.item(i);
			
			String processId = workflow.getAttribute("spId");
			
			if (processId != null
					&& !processId.isEmpty()
					&& !"".equals(processId)){
				
				System.out.println("WS Save WProcessDefBL.updateProcessXmlMap(): " + processId);
				new WProcessDefBL().updateProcessXmlMap(Integer.valueOf(processId), returnValue, currentUserId);
				
				this._publishChanges(returnValue);

			} else {
				
				// GRABAR ERROR Y SALIR
			
			}

		}

		return returnValue;
		
	}
	
	/**
	 * @author dmuleiro - 20130828
	 * 
	 * It has to receive a xml map and check if all the involved objets in the map are correct
	 *
	 * @return boolean
	 * 
	 * @throws NumberFormatException
	 * @throws DOMException
	 * @throws WStepDefException
	 * @throws WStepSequenceDefException
	 * 
	 */
	private boolean _checkXmlMapIntegrity() 
			throws NumberFormatException, DOMException, WStepDefException, WStepSequenceDefException {

		boolean returnValue = true;
		
		WStepDefBL stepBL = new WStepDefBL();
		WStepSequenceDefBL routeBL = new WStepSequenceDefBL();
		
		if (xmlParsed == null){
			return false;
		}
		
		String xmlId = "";
		String spId = "";
		String spName = "";
		
		// 1. Primero de todo parseo los "Task" que serán los WStepDef de nuestro proceso
		
		// lista de pares <xmlId, spId> de las Tasks para despues asociar los WStepResponses con el
		// correspondiente spId del WStepDef
		List<StringPair> stepXmlIdsList = new ArrayList<StringPair>();
		
		NodeList taskList = xmlParsed.getElementsByTagName("Task");

		// iterate the tasks (steps)
		for (int i = 0; i < taskList.getLength(); i++) {
			
			Element task = (Element) taskList.item(i);

			xmlId = task.getAttribute("id");
			spId = task.getAttribute("spId");
			spName = task.getAttribute("label");
			
			// el nombre no puede ser vacío, por lo tanto le ponemos el que tenemos por defecto
			if (spName == null
				|| spName.isEmpty()){
				spName = DEFAULT_STEP_NAME;
				task = this._setXmlElementDefaultNameAndColor(task, DEFAULT_STEP_NAME, RED);
			}

			// si el paso no tiene id lo creamos en nuestra BD y se lo añadimos al xml para guardar
			// El "id" y el "name" serán obligatorios pero los "stepComments" no
			if (spId != null
					&& !spId.isEmpty()){
				
				if (!stepBL.existsStep(Integer.valueOf(spId))){
				
					task.setAttribute("spId", "");
					task = this._setXmlElementDefaultNameAndColor(task, STEP_NOT_EXIST, RED);
					returnValue = false;
				
				} else {
					
					stepXmlIdsList.add(new StringPair(xmlId, spId));
					
				}
				
			}
		
		}
		
		// 2. Ahora parseo los "Edge" que serán los WStepResponseDef de nuestro proceso además de crear los WStepSequenceDef

		// variables auxiliares que nos harán falta para comprobaciones
		String xmlToStepId = "";

		NodeList edgeList = xmlParsed.getElementsByTagName("Edge");

		// iterate the edges (sequences)
		for (int i = 0; i < edgeList.getLength(); i++) {
			xmlId = "";
			spId = "";
			spName = "";
			
			Element edge = (Element) edgeList.item(i);

			xmlId = edge.getAttribute("id");
			spId = edge.getAttribute("spId");
			spName = edge.getAttribute("label");
			
			NodeList mxCellList = edge.getElementsByTagName("mxCell");
			
			// vamos a obtener el id del step del que saldrá la respuesta
			if (mxCellList != null
					&& mxCellList.getLength() == 1){
				xmlToStepId = ((Element) mxCellList.item(0)).getAttribute("target");
				
				
				// el nombre SI puede ser vacío, pero si la ruta va a ningun sitio se le pone "ERROR"
				if (xmlToStepId == null
					|| xmlToStepId.isEmpty()){
					spName = ROUTE_HAS_NO_END_STEP;
					edge = this._setXmlElementDefaultNameAndColor(edge, ROUTE_HAS_NO_END_STEP, RED);
				} else {
					if (spName != null
							&& spName.equals(ROUTE_HAS_NO_END_STEP)){
						spName = "";
						edge = this._setXmlElementDefaultNameAndColor(edge, "", null);
					}
				}

				// AÑADIR NUEVO WStepSequenceDef
				// si la ruta no tiene id quiere decir que es un nuevo WStepSequenceDef por lo tanto
				// lo creamos en nuestra BD y le añadimos nuestro "id" generado al xml para guardarlo
				if (spId != null
						&& !spId.isEmpty()){
					
					
					if (!routeBL.existsRoute(Integer.valueOf(spId))){
						
						edge.setAttribute("spId", "");
						edge = this._setXmlElementDefaultNameAndColor(edge, ROUTE_NOT_EXIST, RED);
						returnValue = false;
					
					} else {

						WStepSequenceDef route = 
								routeBL.getWStepSequenceDefByPK(Integer.valueOf(spId), currentUserId);
						
						if (route != null
								&& (route.getValidResponses() == null
								|| "".equals(route.getValidResponses()))
								&& !route.isAfterAll()){
							
							edge = this._setXmlElementDefaultNameAndColor(
									edge, ROUTE_HAS_NOT_RESPONSES_NEITHER_AFTER_ALL, ORANGE);
							
						}
						

					}
				}
				
			}

		}
		
		return returnValue;

	}

	/**
	 * @author dmuleiro - 20130829
	 * 
	 * It has to process the xml map "xmlParsed" and persist all involved in it in database. 
	 * Then it has to complete the map with the new information.
	 * Then it returns the new map.
	 *
	 * @return boolean
	 * 
	 * @throws WStepDefException
	 * @throws WStepWorkException
	 * @throws WProcessDefException
	 * @throws WStepSequenceDefException
	 * @throws WStepHeadException
	 * @throws WStepResponseDefException
	 * @throws WorkflowEditorActionException
	 * @throws WProcessHeadException 
	 * 
	 */
	private boolean _parseAndPersistXmlMap() 
			throws WStepDefException, WStepWorkException, WProcessDefException, 
			WStepSequenceDefException, WStepHeadException, WStepResponseDefException, 
			WorkflowEditorActionException, WProcessHeadException {

		boolean returnValue = true;
		
		if (xmlParsed == null){
			throw new WorkflowEditorActionException(
					"ERROR WS: parseAndPersistXmlMap() - el mapa no existe");
		}
		
		WStepHeadBL stepHeadBL = new WStepHeadBL();
		WStepDefBL stepBL = new WStepDefBL();
		WStepSequenceDefBL routeBL = new WStepSequenceDefBL();
		
		String xmlId = "";
		String spId = "";
		String spName = "";
		
		// 1. capturo el tag Workflow que será el equivalente en nuestra BD a un WProcessDef
		NodeList workflowList = xmlParsed.getElementsByTagName("Workflow");

		// este for iterara una única vez ya que solo hay un elemento "Workflow"
		for (int i = 0; i < workflowList.getLength(); i++) {

			Element workflow = (Element) workflowList.item(i);
			
			spId = workflow.getAttribute("spId");
			spName = workflow.getAttribute("label");
			
			if (spId != null
					&& !spId.isEmpty()){
				
				process = new WProcessDefBL().getWProcessDefByPK(Integer.valueOf(spId), currentUserId);
				
				if (process == null){
					throw new WorkflowEditorActionException(
							"WS /Save Exception: The map has not a valid process associated [id:" + spId + "]");
				} else {
					// dml 20130820 - si cambiamos el nombre del mapa cambiamos el del proceso
					if (process.getProcess() != null
							&& process.getProcess().getName() != null
							&& !process.getProcess().getName().equals(spName)){
						
						// hacemos update de
						process.getProcess().setName(spName);
						new WProcessDefBL().update(process, currentUserId);
						
						new WProcessHeadBL().updateProcessHeadSonsMapNames(
								process.getProcess().getId(), spName, currentUserId);
						
					}
				}
				
				break;
				
			} else {
				
				throw new WorkflowEditorActionException("WS /Save Exception: The map has not an associated process.");
			
			}

		}

		
		// 2. Primero de todo parseo los "Task" que serán los WStepDef de nuestro proceso
		
		// lista de pares <xmlId, spId> de las Tasks para despues asociar los WStepResponses con el
		// correspondiente spId del WStepDef
		List<StringPair> stepXmlIdsList = new ArrayList<StringPair>();
		// lista para guardar los pasos que tendremos en el mapa para posteriormente actualizarlos
		List<WStepDef> xmlMapStepList = new ArrayList<WStepDef>();
		
		String spStepComments = "";
		String spResponses = "";
		String spRules = "";
		String spInstructions = "";

		NodeList taskList = xmlParsed.getElementsByTagName("Task");

		// iterate the tasks (steps)
		for (int i = 0; i < taskList.getLength(); i++) {
			
			Element task = (Element) taskList.item(i);

			xmlId = task.getAttribute("id");
			spId = task.getAttribute("spId");
			spName = task.getAttribute("label");
			spStepComments = task.getAttribute("description");
			spResponses = task.getAttribute("responses");
			spRules = task.getAttribute("rules");
			spInstructions = task.getAttribute("instructions");
			
			// el nombre no puede ser vacío, por lo tanto le ponemos el que tenemos por defecto
			if (spName == null
				|| spName.isEmpty()){
				spName = DEFAULT_STEP_NAME;
				task = this._setXmlElementDefaultNameAndColor(task, DEFAULT_STEP_NAME, RED);
			}

			// si el paso no tiene id lo creamos en nuestra BD y se lo añadimos al xml para guardar
			// El "id" y el "name" serán obligatorios pero los "stepComments" no
			if (spId == null
					|| spId.isEmpty()){
				
				WStepHead stepHead = new WStepHead();
				stepHead.setName(spName);

				System.out.println("WS Save WStepHead.add(): " + spName);
				Integer stepHeadId = stepHeadBL.add(stepHead, currentUserId); // creo el stepHead
				
				System.out.println("WS Save stepBL.createFirstWStepDef(): " + spName);
				spId = stepBL.createFirstWStepDef(stepHeadId, spRules, spStepComments, spInstructions, currentUserId).toString(); // creo la versión WStepDef
				
				task.setAttribute("spId", spId); // le añadimos el spId nuevo al xml map para persistirlo con el
				
			}
			
			if (spId != null
				&& !spId.isEmpty()
				&& spName != null
				&& !spName.isEmpty()){
			
				if (!stepBL.existsStep(Integer.valueOf(spId))){
					
					task.setAttribute("spId", "");
					task = this._setXmlElementDefaultNameAndColor(task, STEP_NOT_EXIST, RED);
					
				} else 	{
					
					// añadimos el nuevo paso a la lista de pasos del proceso para actualizarlo despues con sus
					// responses
					WStepDef step = stepBL.getWStepDefByPK(Integer.valueOf(spId), process.getProcess().getId(), currentUserId); // nes 20130808 por agregado de filter para step-data-field
					
					step.setName(spName);
					step.setRules(spRules);
					step.setStepComments(spStepComments);
					step.setInstructions(spInstructions);
					
					Set<WStepResponseDef> xmlMapResponses = new HashSet<WStepResponseDef>();
					// ahora vemos las responses que hay y si hay alguno q no existe lo metemos
					if (spResponses != null
						&& !"".equals(spResponses)){
						
						String[] responses = spResponses.split("\\|");
						
						for(int j =0; j < responses.length ; j++){
							 
							boolean existStep = false; 
							for (WStepResponseDef bdResponse : step.getResponse()){
								 
								if (bdResponse.getName().equals(responses[j])){
									existStep = true;
									xmlMapResponses.add(bdResponse);
									break;
								}
							}
							 
							// si es un nuevo WStepSequenceDef lo persistimos
							if (!existStep){
								
								WStepResponseDef newResponse = new WStepResponseDef();
								newResponse.setName(responses[j]);
								newResponse.setRespOrder(_nextResponseOrder(step.getResponse()));
								System.out.println("WS Save WStepResponseDefBL.add(): " + responses[j]);
								newResponse.setId(new WStepResponseDefBL().add(newResponse, currentUserId));
								xmlMapResponses.add(newResponse);
								
							}
							 
						}
						
					}
					
					step.setResponse(xmlMapResponses);
					
					xmlMapStepList.add(step);

					// esta lista auxiliar será para posteriormente asociar los nuevos responses al step correcto
					stepXmlIdsList.add(new StringPair(xmlId, spId));
						
				}
			}

		}
		
		// 3. Ahora parseo los "Symbol" para ver si tenemos "Begin"
		NodeList symbolList = xmlParsed.getElementsByTagName("Symbol");
		String beginSymbolId = null;
		// iterate the symbols
		for (int i = 0; i < symbolList.getLength(); i++) {
			Element symbol = (Element) symbolList.item(i);
			spName = symbol.getAttribute("label");
			if (spName != null
					&& spName.equals("Begin")){
				beginSymbolId = symbol.getAttribute("id");
				break;
			}
			
		}
		
		// 4. Ahora parseo los "Edge" que serán los WStepResponseDef de nuestro proceso además de crear los WStepSequenceDef

		// variables auxiliares que nos harán falta para comprobaciones
		String xmlFromStepId = "";
		String xmlToStepId = "";
		String spFromStepId = "";
		String spToStepId = "";
		
		// lista para guardar las secuencias que tendremos en el mapa y posteriormente crearlas/actualizarlas
		List<WStepSequenceDef> xmlMapStepSequenceList = new ArrayList<WStepSequenceDef>();

		NodeList edgeList = xmlParsed.getElementsByTagName("Edge");

		// iterate the edges (sequences)
		for (int i = 0; i < edgeList.getLength(); i++) {
			xmlId = "";
			spId = "";
			spName = "";
			xmlFromStepId = "";
			spFromStepId = "";
			
			Element edge = (Element) edgeList.item(i);

			xmlId = edge.getAttribute("id");
			spId = edge.getAttribute("spId");
			spName = edge.getAttribute("label");
			spResponses = edge.getAttribute("responses");
			spRules = edge.getAttribute("rules");
			
			NodeList mxCellList = edge.getElementsByTagName("mxCell");
			
			// vamos a obtener el id del step del que saldrá la respuesta
			if (mxCellList != null
					&& mxCellList.getLength() == 1){
				xmlFromStepId = ((Element) mxCellList.item(0)).getAttribute("source");
				xmlToStepId = ((Element) mxCellList.item(0)).getAttribute("target");
				
				// comprobamos que viene de un step valido, si no es así no se va a guardar como WStepSequenceDef
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
				
				for(StringPair stepPair : stepXmlIdsList){
					
					// comparamos el xmlId del "target" del Edge con los que tenemos guardados
					// del bucle anterior para ver si se corresponde a un Task valido o el Edge
					// llega a un elemento que no es un Task
					if (xmlToStepId.equals(stepPair.getString1())){
					
						spToStepId = stepPair.getString2();
						break;
						
					}
					
				}

				if (beginSymbolId != null
						&& beginSymbolId.equals(xmlFromStepId)){

					// si el xmlFromStepId es el Symbol "Begin" le persistimos al processDef el "spToStepId" como "beginStep"
					if (spToStepId != null
							&& !"".equals(spToStepId)
							&& (process.getBeginStep() == null
								|| process.getBeginStep().getId() == null
								|| !process.getBeginStep().getId().equals(Integer.valueOf(spToStepId)))){
						process.setBeginStep(new WStepDef(Integer.valueOf(spToStepId)));
						new WProcessDefBL().update(process, currentUserId);
					}

					// si el "edge" sale del begin symbol no lo persistimos
					System.out.println("WS Save La ruta no se persiste ya que sale del 'Begin' xmlId: " + xmlId);
					continue;
				}
				
				// el nombre SI puede ser vacío, pero si la ruta va a ningun sitio se le pone "ERROR"
				if (xmlToStepId == null
					|| xmlToStepId.isEmpty()){
					spName = ROUTE_HAS_NO_END_STEP;
					edge = this._setXmlElementDefaultNameAndColor(edge, ROUTE_HAS_NO_END_STEP, RED);
				} else {
					if (spName != null
							&& spName.equals(ROUTE_HAS_NO_END_STEP)){
						spName = "";
						edge = this._setXmlElementDefaultNameAndColor(edge, "", null);
					}
				}
				
			}
			
			// si tiene o bien fromStep o bien toStep se guarda como un WStepSequence Valido
			if ((spFromStepId != null
				&& !spFromStepId.isEmpty())
				|| (spToStepId != null
				&& !"".equals(spToStepId))){
				
				WStepSequenceDef route = new WStepSequenceDef();
				route.setEnabled(true);
				route.setAfterAll(false);
				route.setProcess(process);
				route.setRules(spRules); // dml 20130727
				route.setName(spName);
				
				if (spFromStepId != null
					&& !spFromStepId.isEmpty()){					
					route.setFromStep(new WStepDef(Integer.valueOf(spFromStepId)));
				}
				
				if (spToStepId != null
					&& !"".equals(spToStepId)){
					route.setToStep(new WStepDef(Integer.valueOf(spToStepId)));
				} 
					
				// comprobamos que tiene responses y si tiene si son del from step valido
				if (spResponses != null
					&& !"".equals(spResponses)){
					
					if (spFromStepId != null
						&& !"".equals(spFromStepId)){
					
						// comprobamos si son correctas, si falla alguna ponemos el nombre de la ruta en rojo
						for (WStepDef step : xmlMapStepList){
														
							if (step.getId().toString().equals(spFromStepId)){
								
								String[] responses = spResponses.split("\\|");
								String responseIdList = "";
								spResponses = "";
								
								for(int j =0; j < responses.length ; j++){
									 
									for (WStepResponseDef response : step.getResponse()){
										 
										if (response.getName().equals(responses[j])){
											responseIdList += response.getId() + "|";
											spResponses += responses[j] + "|";
											break;
										}
									}

								}
								// ponemos las respuestas en la ruta y en el mapa
								route.setValidResponses(responseIdList);
								edge.setAttribute("responses", spResponses);
								
								break;
							}
						}
					} 
					
				// dml 20130829 - si la ruta no tiene responses le ponemos el "afterAll=true"	
				} else {
					route.setAfterAll(true);
				}
					
				// AÑADIR NUEVO WStepSequenceDef
				// si la ruta no tiene id quiere decir que es un nuevo WStepSequenceDef por lo tanto
				// lo creamos en nuestra BD y le añadimos nuestro "id" generado al xml para guardarlo
				if (spId == null
						|| spId.isEmpty()){
										
					System.out.println("WS Save routeBL.add(): " + route.getName());
					route.setId(routeBL.add(route, currentUserId)); // persistimos el WStepSequenceDef
					spId = route.getId().toString(); 
					
					edge.setAttribute("spId", spId); // le añadimos el spId nuevo al xml map para persistirlo
				
				} else {
					if (!routeBL.existsRoute(Integer.valueOf(spId))){
						
						edge.setAttribute("spId", "");
						edge = this._setXmlElementDefaultNameAndColor(edge, ROUTE_NOT_EXIST, RED);
						
					} else 	{
						// como ya existe le ponemos el id para luego no borrarlo cuando se borren las que no se usan
						route.setId(Integer.valueOf(spId)); 
					}
				}
				
				xmlMapStepSequenceList.add(route);
				
			}

		}
		
		String errorValue = "";
		
		// una a una vamos a ver si tenemos las secuencias ya en BD para ver si lo único que tenemos que
		// hacer es actualizarla (cambiarle las responses que tiene la misma), añadirla nueva o borrarla
		// si dejamos de usarla
		errorValue += this._managedStepSequenceList(xmlMapStepSequenceList);

		// una a una vamos a ver si tenemos las secuencias ya en BD para ver si lo único que tenemos que
		// hacer es actualizarla (cambiarle las responses que tiene la misma), añadirla nueva o borrarla
		// si dejamos de usarla
		errorValue += this._manageStepResponseList(xmlMapStepList, process.getlSteps());
		
		// hay que hacer update de todos los steps que tengamos en el mapa para añadirles si es necesario
		// los nuevos responses que se han puesto en el mapa
		// dml 20130727 - tambien vemos si los steps que ya no estan los podemos borrar o no si no estan en otro
		// proceso o en el mismo mapa repetidos
		errorValue += this._manageStepList(process.getlSteps(), xmlMapStepList);

		// dml 20130828 - para controlar los errores individuales y no romper el BUCLE entero
		if (errorValue != null
				&& !"".equals(errorValue)){
			throw new WStepSequenceDefException(errorValue);
		}

		return returnValue;

	}

	private Element _setXmlElementDefaultNameAndColor(Element element, String defaultName, String color){

		element.setAttribute("label", defaultName); // le corregimos el name al step para que el usuario se de cuenta del fallo

		NodeList mxCellList = element.getElementsByTagName("mxCell");
		
		// vamos a obtener la celta del xmlElement para marcar en el mapa en rojo la misma para avisar al
		// usuario que este nombre lo ha rellenado el propio sistema ya que estaba vacío
		if (mxCellList != null
				&& mxCellList.getLength() == 1){
			Element mxCell = ((Element) mxCellList.item(0));
			String style = mxCell.getAttribute("style");
			
			String fontColor = "";
			if (color != null){
				fontColor = "fontColor=" + color + ";";			
			}
			
			if (style.isEmpty()){
				mxCell.setAttribute("style", mxCell.getAttribute("style") + fontColor);
			} else {
				// si el style ya tiene fontColor elimino el que tenia y lo pongo en rojo pero 
				// dejo el resto de estilos que pudiera tener la celda
				if (style.contains("fontColor")){
					String newStyle = "";
					String[] items = style.split(";");
					for(int i = 0; i < items.length ; i++){
						if (items[i].contains("fontColor")){
							  newStyle += fontColor;
						} else {
							  newStyle += items[i]+";";
						}
					}
					mxCell.setAttribute("style", newStyle);
				} else {
					if (color != null){
						mxCell.setAttribute("style", mxCell.getAttribute("style") + ";" + fontColor);
					}
				}
			}
			
		}
		
		return element; 
		
	}
	
	// los steps que ya no estan relacionados con este process se borrarán
	private String _managedStepSequenceList(List<WStepSequenceDef> xmlMapStepSequenceList) {
		
		String returnValue = "";
		
		WStepSequenceDefBL stepSequenceDefBL = new WStepSequenceDefBL();
		
		List<WStepSequenceDef> deleteUnusedWssdList = new ArrayList<WStepSequenceDef>();
		
		boolean existWssdInXml = false;
		for (WStepSequenceDef bdWssd : process.getStepSequenceList()){
			
			existWssdInXml = false;
			for (WStepSequenceDef xmlWssd: xmlMapStepSequenceList){
				
				// si la secuencia la encontramos entre las que teniamos en la BD simplemente le ponemos
				// los nuevos responses del mapa para actualizarlos 
				if (bdWssd.getId().equals(xmlWssd.getId())){
					
					bdWssd.setName(xmlWssd.getName());
					
					bdWssd.setEnabled(xmlWssd.isEnabled());
					bdWssd.setAfterAll(xmlWssd.isAfterAll());

					bdWssd.setFromStep((xmlWssd.getFromStep() != null)?new WStepDef(xmlWssd.getFromStep().getId()):null);
					bdWssd.setToStep((xmlWssd.getToStep() != null)?new WStepDef(xmlWssd.getToStep().getId()):null);

					bdWssd.setRules(xmlWssd.getRules());

					bdWssd.setValidResponses(xmlWssd.getValidResponses());
					
					existWssdInXml = true;
					break;
					
				}
				
			}
			
			if (!existWssdInXml){
				
				deleteUnusedWssdList.add(bdWssd);
				
			}
			
		}
		
		// primero actualizamos las secuencias que si existen, despues borramos las que no existian
		for (WStepSequenceDef bdWssd: process.getStepSequenceList()){
			System.out.println("WS Save stepSequenceDefBL.update(): " + bdWssd.getName());
			try {
				
				stepSequenceDefBL.update(bdWssd, currentUserId);
			
			} catch (WStepSequenceDefException e) {
				e.printStackTrace();
				returnValue += "WS _deleteUnusuedStepSequenceList.update() ERROR: " + e.getMessage() + "\n";
			}
		}
		
		for (WStepSequenceDef deletedRoute: deleteUnusedWssdList){
			System.out.println("WS Save stepSequenceDefBL.deleteRoute(): " + deletedRoute.getName());
			try {
				stepSequenceDefBL.deleteRoute(deletedRoute, currentUserId);
			} catch (WStepSequenceDefException e) {
				e.printStackTrace();
				returnValue += "WS _deleteUnusuedStepSequenceList.deleteRoute() ERROR: " + e.getMessage() + "\n";
			} catch (WStepWorkSequenceException e) {
				e.printStackTrace();
				returnValue += "WS _deleteUnusuedStepSequenceList.deleteRoute() ERROR: " + e.getMessage() + "\n";
			}
		}
		
		return returnValue;
		
	}
		
	// los steps que ya no estan relacionados con este process se borrarán
	private String _manageStepResponseList(List<WStepDef> xmlStepList, List<WStepDef> bdStepList) {
		
		String returnValue = "";
		
		WStepResponseDefBL wsrdBL = new WStepResponseDefBL();
		
		List<WStepResponseDef> removeList = new ArrayList<WStepResponseDef>();
				
		// recorremos las listas y borramos solo los responses borrados individuales, no si se borra el step
		for (WStepDef bdStep : bdStepList){

			for (WStepDef xmlStep : xmlStepList){
				
				// si tenemos el paso en la bd y en el xml es que no se borro. si falta alguna response la borramos
				if (bdStep.getId().equals(xmlStep.getId())){
					
					for (WStepResponseDef bdResponse : bdStep.getResponse()){
						
						boolean existResponse = false;
						for (WStepResponseDef xmlResponse : xmlStep.getResponse()){
							
							if (bdResponse.getId().equals(xmlResponse.getId())){
								existResponse = true;
							}
							
						}
						
						if (!existResponse){
							removeList.add(bdResponse);
						}
					}
					
					
				}
				
				
			}
		}
		
		// eliminamos la lista de responses obsoletas
		for (WStepResponseDef removeResponse : removeList){
			System.out.println("WS Save wsrdBL.deleteRoute(): " + removeResponse.getName());
			try {
				wsrdBL.delete(removeResponse, currentUserId);
			} catch (WStepResponseDefException e) {
				e.printStackTrace();
				returnValue += "WS _deleteUnusuedStepSequenceList.delete() ERROR: " + e.getMessage() + "\n";
			}
		}
		
		// dml 20130828 - para controlar los errores individuales y no romper el BUCLE entero
		return returnValue;
		
	}
	
	// actualizamos los steps que tenemos en el xml y que estan tambien en la bd (además por la relación tambien
	// se actualizan los responses).
	private String _manageStepList(List<WStepDef> bdStepList, List<WStepDef> xmlStepList) {
		
		WStepSequenceDefBL routeBL = new WStepSequenceDefBL();
		WStepDefBL stepBL = new WStepDefBL();

		String returnValue = "";
		
		// bucle para persistir los "id_step" en los WStepResponseDef nuevos
		if (xmlStepList != null){
			for (WStepDef step : xmlStepList){
				
				// si tenemos algun response con "respOrder" nulo se lo añadimos
				for (WStepResponseDef response : step.getResponse()){
					if (response.getRespOrder() == null){
						response.setRespOrder(_nextResponseOrder(step.getResponse()));
					}
				}
				
				System.out.println("WS Save wsrdBL.update(): " + step.getName());
				try {
					stepBL.update(step,
							process.getProcess().getId(), // nes 20130808 por agregado de filter para step-data-field
							currentUserId);
				} catch (WStepDefException e) {
					e.printStackTrace();
					returnValue += "WS _manageStepList.update() ERROR: " + e.getMessage() + "\n";
				}
			}
		}
		
		List<Integer> removeStepIdList = new ArrayList<Integer>();
		// dml 20130727 - si el step ya no existe se lo mandamos a la BL a borrar y esta decidira si no se usa en 
		// ningun otro sitio para borrarlo (tanto el como sus responses)
		for (WStepDef bdStep : bdStepList){
			
			boolean existStep = false;
			for (WStepDef xmlStep : xmlStepList){
				
				if (bdStep.getId().equals(xmlStep.getId())){
					existStep = true;
					break;
				}
				
			}
			if (!existStep){
				removeStepIdList.add(bdStep.getId());
			}
			
		}
		
		// dml 20130830 - borramos (o marcamos como deleted) los steps de la lista asi como sus secuencias asociadas
		for (Integer removeStepId : removeStepIdList){

			List<WStepSequenceDef> stepRoutesList = null;
			try {

				// vemos si tiene tanto rutas entrantes y salientes
				stepRoutesList = routeBL.getIncomingRoutes(removeStepId, NOT_DELETED_BOOL, null, currentUserId);
				stepRoutesList.addAll(routeBL.getOutgoingRoutes(removeStepId, NOT_DELETED_BOOL, null, currentUserId));
				
				if (stepRoutesList != null && !stepRoutesList.isEmpty()) {
					// dml 20130830 - si tiene rutas se borran (el "deleteRoute" ya tiene implementado el marcar como "deleted" si se usa en otros sitios
					for (WStepSequenceDef route : stepRoutesList){
						routeBL.deleteRoute(route, currentUserId);
					}

				}
					
				// dml 20130830 - solo se intenta borrar si no lo usa ningún otro proceso
				if (!stepBL.isSharedStep(removeStepId, currentUserId)){
					stepBL.delete(removeStepId, process.getProcess().getId(), currentUserId);
				}

				System.out.println("WS Save. WStepDefBL().delete() Step deleted. id: " + removeStepId);

			} catch (WStepDefException e) {
				e.printStackTrace();
				returnValue += "WS _manageStepList.delete() ERROR: " + e.getMessage() + "\n";
			} catch (WStepWorkException e) {
				e.printStackTrace();
				returnValue += "WS _manageStepList.delete() ERROR: " + e.getMessage() + "\n";
			} catch (WProcessDefException e) {
				e.printStackTrace();
				returnValue += "WS _manageStepList.delete() ERROR: " + e.getMessage() + "\n";
			} catch (WStepSequenceDefException e) {
				e.printStackTrace();
				returnValue += "WS _manageStepList.delete() ERROR: " + e.getMessage() + "\n";
			} catch (WStepHeadException e) {
				e.printStackTrace();
				returnValue += "WS _manageStepList.delete() ERROR: " + e.getMessage() + "\n";
			} catch (WStepWorkSequenceException e) {
				e.printStackTrace();
				returnValue += "WS _manageStepList.delete() ERROR: " + e.getMessage() + "\n";
			} 
		}
		
		return returnValue;
		
	}

	private Integer _nextResponseOrder(Set<WStepResponseDef> responseList) {
		
		Integer nextRespOrder = 0;
		
		if (responseList != null){
			for (WStepResponseDef wsrd : responseList){
				if (wsrd.getRespOrder() != null
						&& nextRespOrder < wsrd.getRespOrder()){
					nextRespOrder = wsrd.getRespOrder();
				}
			}
		}
		
		return nextRespOrder + 1;
	}

	private void _publishChanges(String xml) throws IOException{
		
		String path = super.getContextPath() + "/bee_bpm_web/processXmlMapTmp.xml";
		File temp = new File(path);
		
		// if file doesnt exists, then create it
		if (!temp.exists()) {
			temp.createNewFile();
		}
		
		FileWriter fw = new FileWriter(temp.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(xml);
		bw.close();

	}
	
}