package org.beeblos.bpm.web.ws.resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepHeadBL;
import org.beeblos.bpm.core.bl.WStepResponseDefBL;
import org.beeblos.bpm.core.bl.WStepSequenceDefBL;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepResponseDefException;
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
import org.xml.sax.InputSource;

@Path("/wf")
public class WorkflowEditorAction extends CoreManagedBean {

	private Integer currentUserId = 1000;
	private WProcessDef process = null;
	
	Document xmlParsed = null;

	@Path("/Save")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String save(@FormParam("xml") String inputMap) {

		try {

			String xml = URLDecoder.decode(inputMap, "UTF-8").replace("\n", "&#xa;");

			System.out.println("El xml HA LLEGADO: " + xml);

			xmlParsed = this._loadXMLFromString(xml);

			this.parseXmlString();
			
			xml = this._loadStringFromXml();
			
			if (process != null
					&& process.getId() != null
					&& !process.getId().equals(0)){
				
				process.setProcessMap(xml);

				new WProcessDefBL().updateProcessXmlMap(process.getId(), xml, currentUserId);
				
			}
			
			this._publishChanges(xml);

			return "Este es el mapa del WProcessDef id:" + process.getId();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	private Document _loadXMLFromString(String xml) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		
		return builder.parse(is);
		
	}
	
	private String _loadStringFromXml() {
		
		try {
		
			DOMSource domSource = new DOMSource(xmlParsed);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			
			return writer.toString();
	
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private boolean parseXmlString() {

		if (xmlParsed == null){
			return false;
		}
		
		WStepHeadBL stepHeadBL = new WStepHeadBL();
		WStepDefBL stepBL = new WStepDefBL();
		WStepResponseDefBL stepResponseBL = new WStepResponseDefBL();
		
		try {

			String xmlId = "";
			String spId = "";
			String spName = "";
			
			// 1. capturo el tag Workflow que será el equivalente en nuestra BD a un WProcessDef
			NodeList workflowList = xmlParsed.getElementsByTagName("Workflow");

			// este for iterara una única vez ya que solo hay un elemento "Workflow"
			for (int i = 0; i < workflowList.getLength(); i++) {

				Element workflow = (Element) workflowList.item(i);
				
				spId = workflow.getAttribute("description");
				
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
			
			NodeList taskList = xmlParsed.getElementsByTagName("Task");

			// iterate the tasks (steps)
			for (int i = 0; i < taskList.getLength(); i++) {
				xmlId = "";
				spId = "";
				spName = "";
				
				Element task = (Element) taskList.item(i);

				xmlId = task.getAttribute("id");
				spId = task.getAttribute("description");
				spName = task.getAttribute("label");
				
				// si el paso no tiene id lo creamos en nuestra BD y se lo añadimos al xml para guardar
				if (spId == null
						|| spId.isEmpty()
						&& (spName != null
						&& !spName.isEmpty())){
					
					WStepHead stepHead = new WStepHead();
					stepHead.setName(spName);
					Integer stepHeadId = stepHeadBL.add(stepHead, currentUserId); // creo el stepHead
					
					spId = stepBL.createFirstWStepDef(stepHeadId, currentUserId).toString(); // creo la versión WStepDef
										
					task.setAttribute("description", spId); // le añadimos el spId nuevo al xml map para persistirlo con el
					
				}
				
				if (spId != null
						&& !spId.isEmpty()
						&& spName != null
						&& !spName.isEmpty()){
					
					// añadimos el nuevo paso a la lista de pasos del proceso para actualizarlo despues con sus
					// responses
					WStepDef step = stepBL.getWStepDefByPK(Integer.valueOf(spId), currentUserId);
					xmlMapStepList.add(step);

					// esta lista auxiliar será para posteriormente asociar los nuevos responses al step correcto
					stepXmlIdsList.add(new StringPair(xmlId, spId));

				}

			}
			
			// 2. Ahora parseo los "Edge" que serán los WStepResponseDef de nuestro proceso además de crear los WStepSequenceDef

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
				spId = edge.getAttribute("description");
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
				
				// si no tiene Task tanto en "source" como en "target" no se guarda como un WStepResponse valido
				if (spFromStepId != null
						&& !spFromStepId.isEmpty()
						&& spToStepId != null
						&& !"".equals(spToStepId)){
					
					// AÑADIR NUEVO WStepResponseDef
					// si el paso no tiene id quiere decir que es un nuevo WStepResponseDef por lo tanto
					// lo creamos en nuestra BD y le añadimos nuestro "id" generado al xml para guardarlo
					if (spId == null
							|| spId.isEmpty()
							&& (spName != null
							&& !spName.isEmpty())){
						
						WStepResponseDef stepResponseDef = new WStepResponseDef();
						stepResponseDef.setName(spName);
						stepResponseDef.setId(stepResponseBL.add(stepResponseDef, currentUserId)); // persistimos el WStepSequenceDef
						spId = stepResponseDef.getId().toString(); 
						
						edge.setAttribute("description", spId); // le añadimos el spId nuevo al xml map para persistirlo

						// busco el step dentro de los que tiene el proceso (lo he metido arriba si no estaba
						// ya anteriormente) y le meto esta nueva respuesta en su lista para al final de todo
						// el proceso persistir las nuevas respuestas de los steps
						for (WStepDef step : xmlMapStepList){
							
							if (spFromStepId.equals(step.getId().toString())){
								step.getResponse().add(stepResponseDef);
							}
							
						}
						
					}
					
					boolean stepSequenceAlreadyExist = true;

					// AÑADIR NUEVO WStepSequenceDef
					// recorremos la lista de secuencias del mapa xml, si ya la tenemos le añadimos la respuesta, 
					// de lo contrario creamos una secuencia nueva
					for (WStepSequenceDef wssd : xmlMapStepSequenceList) {
						
						// si tenemos ya la secuencia en la lista le añadimos el valid response,
						// de lo contrario la creamos (lo comprobamos viendo si son iguales los fromStep y toStep

						
						// COMPROBAR QUE SEA EL MISMO OBJECTO EL QUE SE ACTUALIZA
						if (wssd.getFromStep().getId().equals(Integer.valueOf(spFromStepId))
								&& wssd.getToStep().getId().equals(Integer.valueOf(spToStepId))){
							
							wssd.setValidResponses(wssd.getValidResponses() + "|" + spId);
							stepSequenceAlreadyExist = false;
							break;
						}
																		
					}

					// ademas de añadirlo en la lista de las secuencias del mapa xml lo añadimos en otra lista
					// donde solo estarán las nuevas secuencias para añadir posteriormente
					if (stepSequenceAlreadyExist){
						
						WStepSequenceDef newWssd = new WStepSequenceDef(process, null, 
								new WStepDef(Integer.valueOf(spFromStepId)),
								new WStepDef(Integer.valueOf(spToStepId)), 
								true, false, spId, null);
						
						xmlMapStepSequenceList.add(newWssd);

					}
					
					
				}

			}
			
			
			// una a una vamos a ver si tenemos las secuencias ya en BD para ver si lo único que tenemos que
			// hacer es actualizarla (cambiarle las responses que tiene la misma), añadirla nueva o borrarla
			// si dejamos de usarla
			this._updateStepSequenceList(xmlMapStepSequenceList);

			// una a una vamos a ver si tenemos las secuencias ya en BD para ver si lo único que tenemos que
			// hacer es actualizarla (cambiarle las responses que tiene la misma), añadirla nueva o borrarla
			// si dejamos de usarla
			this._deleteUnusedStepResponseList(xmlMapStepList, process.getlSteps());
			
			// hay que hacer update de todos los steps que tengamos en el mapa para añadirles si es necesario
			// los nuevos responses que se han puesto en el mapa
			this._updateStepList(xmlMapStepList);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;

	}
	
	// los steps que ya no estan relacionados con este process se borrarán
	private void _updateStepSequenceList(List<WStepSequenceDef> xmlMapStepSequenceList
			) throws WStepSequenceDefException {
		
		WStepSequenceDefBL stepSequenceDefBL = new WStepSequenceDefBL();
		
		List<WStepSequenceDef> deleteUnusedWssdList = new ArrayList<WStepSequenceDef>();
		List<WStepSequenceDef> newStepSequenceList = new ArrayList<WStepSequenceDef>();
		
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
		
		boolean existWssdInBd = false;
		for (WStepSequenceDef xmlWssd: xmlMapStepSequenceList){
			
			existWssdInBd = false;
			for (WStepSequenceDef bdWssd : process.getStepSequenceList()){

				if (bdWssd.getFromStep().getId().equals(xmlWssd.getFromStep().getId())
						&& bdWssd.getToStep().getId().equals(xmlWssd.getToStep().getId())){
					
					existWssdInBd = true;
					break;
				}

			}
			
			if (!existWssdInBd){
				newStepSequenceList.add(xmlWssd);
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
		
		for (WStepSequenceDef wssd: newStepSequenceList){
			stepSequenceDefBL.add(wssd, currentUserId);
		}
		
	}
		
	// los steps que ya no estan relacionados con este process se borrarán
	private void _deleteUnusedStepResponseList(List<WStepDef> xmlStepList,
			List<WStepDef> bdStepList) throws WStepResponseDefException {
		
		WStepResponseDefBL wsrdBL = new WStepResponseDefBL();
		
		boolean existWssdInXml = false;
		for (WStepDef bdStep : bdStepList){
			
			existWssdInXml = false;
			for (WStepResponseDef bdResponse : bdStep.getResponse()){
				
				for (WStepDef xmlStep : xmlStepList){
					
					for (WStepResponseDef xmlResponse : xmlStep.getResponse()){
						
						if (bdResponse.getId().equals(xmlResponse.getId())){
							
							existWssdInXml = true;
							break;
							
						}
						
					}
					
					if (existWssdInXml){
						break;
					}
					
				}
				
				if (!existWssdInXml){
					wsrdBL.delete(bdResponse, currentUserId);
				}
				

			}
		}
	}
	
	// los steps que ya no estan relacionados con este process se borrarán
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