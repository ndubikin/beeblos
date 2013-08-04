package org.beeblos.bpm.web.ws.resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepHeadBL;
import org.beeblos.bpm.core.bl.WStepResponseDefBL;
import org.beeblos.bpm.core.bl.WStepSequenceDefBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepHeadException;
import org.beeblos.bpm.core.error.WStepResponseDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepHead;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import com.sp.common.util.StringPair;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Path("/wf")
public class WorkflowEditorAction extends CoreManagedBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final static String DEFAULT_STEP_NAME = "STEP";
	private final static String DEFAULT_ERROR = "ERROR";
	private final static String RED = "red";
	
	private Integer currentUserId = 1000;
	private WProcessDef process = null;
	
	Document xmlParsed = null;

	@Path("/Save")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response save(@FormParam("xml") String inputMap) {

		try {

			System.out.println("------- INIT WS /Save -------");

			long tiempoInicio = System.currentTimeMillis();
			
			String xml = URLDecoder.decode(inputMap, "UTF-8").replace("\n", "&#xa;");

			xmlParsed = XmlConverterUtil.loadXMLFromString(xml);

			this.parseXmlString();
			
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
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

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

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("WS getSpObjectList returnValue: " + returnValue);
		
		System.out.println("------- END WS /getSpObjectList -------");
		return Response.ok(returnValue, MediaType.TEXT_XML).build();

	}
	
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

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("------- END WS /Notify -------");

	}

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

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("------- END WS /NotifyWithResponse -------");
		return Response.ok(returnValue, MediaType.TEXT_XML).build();

	}

	// dml 20130726
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
					
					WStepDef step = new WStepDefBL().getWStepDefByPK(Integer.valueOf(newSpObjectId), currentUserId);
					
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
	
	// dml 20130726
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
	
	@Path("/Poll")
	@GET
	@Consumes(MediaType.TEXT_HTML)
	public Response poll() {
		
		System.out.println("------- INIT WS /Poll -------");
		System.out.println("WS Poll inputMap: ");
		System.out.println("------- END WS /Poll -------");

		return 	Response.ok("WS Poll SIN IMPLEMENTAR",MediaType.TEXT_XML).build();

	}
	

	private boolean parseXmlString() {

		if (xmlParsed == null){
			return false;
		}
		
		WStepHeadBL stepHeadBL = new WStepHeadBL();
		WStepDefBL stepBL = new WStepDefBL();
		WStepSequenceDefBL routeBL = new WStepSequenceDefBL();
		
		try {

			String xmlId = "";
			String spId = "";
			String spName = "";
			
			// 1. capturo el tag Workflow que será el equivalente en nuestra BD a un WProcessDef
			NodeList workflowList = xmlParsed.getElementsByTagName("Workflow");

			// este for iterara una única vez ya que solo hay un elemento "Workflow"
			for (int i = 0; i < workflowList.getLength(); i++) {

				Element workflow = (Element) workflowList.item(i);
				
				spId = workflow.getAttribute("spId");
				
				if (spId != null
						&& !spId.isEmpty()){
					
					process = new WProcessDefBL().getWProcessDefByPK(Integer.valueOf(spId), currentUserId);
					
					if (process == null){
						return false;
					} 
					
					break;
					
				} else {
					
					// GRABAR ERROR Y SALIR
				
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
				
					// añadimos el nuevo paso a la lista de pasos del proceso para actualizarlo despues con sus
					// responses
					WStepDef step = stepBL.getWStepDefByPK(Integer.valueOf(spId), currentUserId);
					
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
					
					// si el "edge" sale del begin symbol no lo persistimos
					if (xmlFromStepId != null
							&& beginSymbolId != null
							&& beginSymbolId.equals(xmlFromStepId)){
						System.out.println("WS Save La ruta no se persiste ya que sale del 'Begin' xmlId: " + xmlId);
						continue;
					}
					
					// el nombre SI puede ser vacío, pero si la ruta va a ningun sitio se le pone "ERROR"
					if (xmlToStepId == null
						|| xmlToStepId.isEmpty()){
						spName = DEFAULT_ERROR;
						edge = this._setXmlElementDefaultNameAndColor(edge, DEFAULT_ERROR, RED);
					} else {
						if (spName != null
								&& spName.equals(DEFAULT_ERROR)){
							spName = "";
							edge = this._setXmlElementDefaultNameAndColor(edge, "", null);
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
						// como ya existe le ponemos el id para luego no borrarlo cuando se borren las que no
						// se usan
						route.setId(Integer.valueOf(spId)); 
					}
					
					xmlMapStepSequenceList.add(route);
					
					
				}

			}
			
			
			// una a una vamos a ver si tenemos las secuencias ya en BD para ver si lo único que tenemos que
			// hacer es actualizarla (cambiarle las responses que tiene la misma), añadirla nueva o borrarla
			// si dejamos de usarla
			this._deleteUnusuedStepSequenceList(xmlMapStepSequenceList);

			// una a una vamos a ver si tenemos las secuencias ya en BD para ver si lo único que tenemos que
			// hacer es actualizarla (cambiarle las responses que tiene la misma), añadirla nueva o borrarla
			// si dejamos de usarla
			this._manageStepResponseList(xmlMapStepList, process.getlSteps());
			
			// hay que hacer update de todos los steps que tengamos en el mapa para añadirles si es necesario
			// los nuevos responses que se han puesto en el mapa
			// dml 20130727 - tambien vemos si los steps que ya no estan los podemos borrar o no si no estan en otro
			// proceso o en el mismo mapa repetidos
			this._manageStepList(process.getlSteps(), xmlMapStepList);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;

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
	private void _deleteUnusuedStepSequenceList(List<WStepSequenceDef> xmlMapStepSequenceList
			) throws WStepSequenceDefException {
		
		WStepSequenceDefBL stepSequenceDefBL = new WStepSequenceDefBL();
		
		List<WStepSequenceDef> deleteUnusedWssdList = new ArrayList<WStepSequenceDef>();
		
		boolean existWssdInXml = false;
		for (WStepSequenceDef bdWssd : process.getStepSequenceList()){
			
			existWssdInXml = false;
			for (WStepSequenceDef xmlWssd: xmlMapStepSequenceList){
				
				// si la secuencia la encontramos entre las que teniamos en la BD simplemente le ponemos
				// los nuevos responses del mapa para actualizarlos 
				// REVISAR: COMPROBAR SI SE REALIZA AL HACER EL UPDATE DEL PROCESO
				if (bdWssd.getId().equals(xmlWssd.getId())
						&& bdWssd.getId().equals(xmlWssd.getId())){
					
					bdWssd.setValidResponses(xmlWssd.getValidResponses());
					
					existWssdInXml = true;
					break;
				}
				
			}
			
			if (!existWssdInXml){
				
				deleteUnusedWssdList.add(bdWssd);
				
			}
			
		}
		
		// primero borramos las secuencias que ya no existen, despues actualizamos las que si existian y 
		// por último añadimos las nuevas
		for (WStepSequenceDef wssd: process.getStepSequenceList()){
			System.out.println("WS Save stepSequenceDefBL.update(): " + wssd.getName());
			stepSequenceDefBL.update(wssd, currentUserId);
		}
		
		for (WStepSequenceDef wssd: deleteUnusedWssdList){
			System.out.println("WS Save stepSequenceDefBL.deleteRoute(): " + wssd.getName());
			stepSequenceDefBL.deleteRoute(wssd, currentUserId);
		}
		
	}
		
	// los steps que ya no estan relacionados con este process se borrarán
	private void _manageStepResponseList(List<WStepDef> xmlStepList,
			List<WStepDef> bdStepList) throws WStepResponseDefException {
		
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
			wsrdBL.delete(removeResponse, currentUserId);
		}
		
	}
	
	// actualizamos los steps que tenemos en el xml y que estan tambien en la bd (además por la relación tambien
	// se actualizan los responses).
	private void _manageStepList(List<WStepDef> bdStepList, List<WStepDef> xmlStepList) 
			throws WStepDefException, WStepWorkException, WProcessDefException, WStepSequenceDefException, WStepHeadException {
		
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
				new WStepDefBL().update(step, currentUserId);
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
		
		for (Integer removeStepId : removeStepIdList){
			boolean deletedOk = new WStepDefBL().delete(removeStepId, process.getId(), currentUserId);
			if (deletedOk){
				System.out.println("WS Save. WStepDefBL().delete() It is not possible to delete the step, it is used in other place. id: " + removeStepId);
			} else {
				System.out.println("WS Save. WStepDefBL().delete() Step deleted. id: " + removeStepId);
			}
		}
		
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
		
		String path = CONTEXTPATH + "/bee_bpm_web/processXmlMapTmp.xml";
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
	
	@Path("/ShowImageMap")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response showImageMap(@FormParam("xml") String inputMap) {

		return Response.ok("WS SIN IMPLEMENTAR",MediaType.TEXT_PLAIN).build();

	}

	@Path("/blMethodParser")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Object blMethodParser(@QueryParam("className") @Encoded String className,
			@QueryParam("params") @Encoded List<String> params,
			@QueryParam("values") @Encoded List<String> values) {

		Object returnValue = null;

		// METHOD PARSER

		return returnValue;

	}
	
}