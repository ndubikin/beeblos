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
import org.beeblos.bpm.core.bl.WStepSequenceDefBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepHead;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.noper.StringPair;
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
	private final static String DEFAULT_RESPONSE_NAME = "RESPONSE";
	private final static String DEFAULT_ROUTE_NAME = "ROUTE";
	
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
			
//			xml = xml.substring(xml.indexOf("<mxGraphModel>"));
			
			if (process != null
					&& process.getId() != null
					&& !process.getId().equals(0)){
				
				process.setProcessMap(xml);

				new WProcessDefBL().updateProcessXmlMap(process.getId(), xml, currentUserId);
				
			}
			
			this._publishChanges(xml);

			long totalTiempo = System.currentTimeMillis() - tiempoInicio;

			System.out.println("TIEMPO DE WS /Save:" + totalTiempo + " miliseg");
			System.out.println("------- END WS /Save -------");

//			xml = URLEncoder.encode(inputMap, "UTF-8").replace("&#xa;", "\n");

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

		String returnValue = null;
		
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
				if (bdStepComboList != null){
					for (StringPair step : bdStepComboList){
						returnValue += step.getString2() + " (id:" + step.getString1() + "),";
					}
				}
				returnValue = returnValue.substring(0, returnValue.lastIndexOf(","));
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
	private Document _changeSpObject(Document newXmlMapParsed){
		
		String cellToChangeId = "";
		NodeList cellToChangeList = xmlParsed.getElementsByTagName("cellId");
		if (cellToChangeList != null
				&& cellToChangeList.getLength() > 0){
			
			// este for iterara una única vez ya que solo hay un elemento "Workflow"
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
					
					task.setAttribute("spId", newSpObjectId);
					task.setAttribute("label", newSpObjectName);
					break;
				
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
			
			String spStepComment = "";

			NodeList taskList = xmlParsed.getElementsByTagName("Task");

			// iterate the tasks (steps)
			for (int i = 0; i < taskList.getLength(); i++) {
				
				Element task = (Element) taskList.item(i);

				xmlId = task.getAttribute("id");
				spId = task.getAttribute("spId");
				spName = task.getAttribute("label");
				spStepComment = task.getAttribute("description");
				
				// el nombre no puede ser vacío, por lo tanto le ponemos el que tenemos por defecto
				if (spName == null
					|| spName.isEmpty()){
					spName = DEFAULT_STEP_NAME;
					task = this._setXmlElementDefaultName(task, DEFAULT_STEP_NAME);
				}

				// si el paso no tiene id lo creamos en nuestra BD y se lo añadimos al xml para guardar
				// El "id" y el "name" serán obligatorios pero los "stepComments" no
				if (spId == null
						|| spId.isEmpty()){
					
					WStepHead stepHead = new WStepHead();
					stepHead.setName(spName);
					stepHead.setComments(spStepComment);
					Integer stepHeadId = stepHeadBL.add(stepHead, currentUserId); // creo el stepHead
					
					spId = stepBL.createFirstWStepDef(stepHeadId, currentUserId).toString(); // creo la versión WStepDef
					
					task.setAttribute("spId", spId); // le añadimos el spId nuevo al xml map para persistirlo con el
					
				}
				
				if (spId != null
						&& !spId.isEmpty()
						&& spName != null
						&& !spName.isEmpty()){
					
					// añadimos el nuevo paso a la lista de pasos del proceso para actualizarlo despues con sus
					// responses
					WStepDef step = stepBL.getWStepDefByPK(Integer.valueOf(spId), currentUserId);
					
					// le vaciamos la lista de responses de la BD para actualizarla con lo que tenemos en el
					// mapa xml en el siguiente bucle (paso 2 del algoritmo)
					step.setResponse(new HashSet<WStepResponseDef>());
					step.setName(spName);
					xmlMapStepList.add(step);

					// esta lista auxiliar será para posteriormente asociar los nuevos responses al step correcto
					stepXmlIdsList.add(new StringPair(xmlId, spId));

				}

			}
			
			// 3. Ahora parseo los "Edge" que serán los WStepResponseDef de nuestro proceso además de crear los WStepSequenceDef

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
				
				NodeList mxCellList = edge.getElementsByTagName("mxCell");
				
				// vamos a obtener el id del step del que saldrá la respuesta
				if (mxCellList != null
						&& mxCellList.getLength() == 1){
					xmlFromStepId = ((Element) mxCellList.item(0)).getAttribute("source");
					xmlToStepId = ((Element) mxCellList.item(0)).getAttribute("target");
					
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

					// el nombre SI puede ser vacío, pero le ponemos uno por defecto (REVISAR NESTOR)
					if (spName == null
						|| spName.isEmpty()){
						spName = DEFAULT_ROUTE_NAME;
						edge = this._setXmlElementDefaultName(edge, DEFAULT_ROUTE_NAME);
					}
					
					if (spFromStepId != null
						&& !spFromStepId.isEmpty()){
						route.setFromStep(new WStepDef(Integer.valueOf(spFromStepId)));
					}
					
					if (spToStepId != null
						&& !"".equals(spToStepId)){
						route.setToStep(new WStepDef(Integer.valueOf(spToStepId)));
					}
					
					route.setProcess(process);

					// AÑADIR NUEVO WStepSequenceDef
					// si la ruta no tiene id quiere decir que es un nuevo WStepSequenceDef por lo tanto
					// lo creamos en nuestra BD y le añadimos nuestro "id" generado al xml para guardarlo
					if (spId == null
							|| spId.isEmpty()){
						
						
						route.setName(spName);
						route.setId(routeBL.add(route, currentUserId)); // persistimos el WStepSequenceDef
						spId = route.getId().toString(); 
						
						edge.setAttribute("spId", spId); // le añadimos el spId nuevo al xml map para persistirlo
					
					} else {
						// como ya existe, buscamos el route con su id para meterlo en el xmlMapStepList
						// para despues tenerlo a la hora de hacer la comprobación de los nuevos responses
						route = 
								routeBL.getWStepSequenceDefByPK(Integer.valueOf(spId), currentUserId);
						route.setName(spName);
					}
					
					xmlMapStepSequenceList.add(route);
					
					
				}

			}
			
			
			// una a una vamos a ver si tenemos las secuencias ya en BD para ver si lo único que tenemos que
			// hacer es actualizarla (cambiarle las responses que tiene la misma), añadirla nueva o borrarla
			// si dejamos de usarla
			this._deleteUnusuedStepSequenceList(xmlMapStepSequenceList);

			// hay que hacer update de todos los steps que tengamos en el mapa para añadirles si es necesario
			// los nuevos responses que se han puesto en el mapa
			this._updateStepList(xmlMapStepList);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;

	}
	
	private Element _setXmlElementDefaultName(Element element, String defaultName){

		element.setAttribute("label", defaultName); // le corregimos el name al step para que el usuario se de cuenta del fallo

		NodeList mxCellList = element.getElementsByTagName("mxCell");
		
		// vamos a obtener la celta del xmlElement para marcar en el mapa en rojo la misma para avisar al
		// usuario que este nombre lo ha rellenado el propio sistema ya que estaba vacío
		if (mxCellList != null
				&& mxCellList.getLength() == 1){
			Element mxCell = ((Element) mxCellList.item(0));
			String style = mxCell.getAttribute("style");
			
			String redFontColor = "fontColor=red;";			
			
			if (style.isEmpty()){
				mxCell.setAttribute("style", mxCell.getAttribute("style") + redFontColor);
			} else {
				// si el style ya tiene fontColor elimino el que tenia y lo pongo en rojo pero 
				// dejo el resto de estilos que pudiera tener la celda
				if (style.contains("fontColor")){
					String newStyle = "";
					String[] items = style.split(";");
					for(int i = 0; i < items.length ; i++){
						if (items[i].contains("fontColor")){
							  newStyle += redFontColor;
						} else {
							  newStyle += items[i]+";";
						}
					}
					mxCell.setAttribute("style", newStyle);
				} else {
					mxCell.setAttribute("style", mxCell.getAttribute("style") + ";" + redFontColor);
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
				if (bdWssd.getFromStep().getId().equals(xmlWssd.getFromStep().getId())
						&& bdWssd.getToStep().getId().equals(xmlWssd.getToStep().getId())){
					
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
			stepSequenceDefBL.update(wssd, currentUserId);
		}
		
		for (WStepSequenceDef wssd: deleteUnusedWssdList){
			stepSequenceDefBL.deleteRoute(wssd, currentUserId);
		}
		
	}
		
	// actualizamos los steps que tenemos en el xml y que estan tambien en la bd
	private void _updateStepList(List<WStepDef> stepList) throws WStepDefException {
		
		// bucle para persistir los "id_step" en los WStepResponseDef nuevos
		if (stepList != null){
			for (WStepDef step : stepList){
				
				new WStepDefBL().update(step, currentUserId);
			}
		}
		
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