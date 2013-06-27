package org.beeblos.bpm.web.ws.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.beeblos.bpm.core.bl.WProcessHeadBL;
import org.beeblos.bpm.core.error.WProcessException;
import org.beeblos.bpm.core.model.WProcessHead;

@Path("/wf")
public class WorkflowEditorAction {
/*
	@Context
	HttpServletRequest request;
	
	@Context
	HttpServletResponse servletResponse;
*/
	@Path("/Save")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String save(@FormParam("xml") String inputMap) {

		try {
			
			String xml = URLDecoder.decode(inputMap, "UTF-8").replace("\n", "&#xa;");
		
			System.out.println("El xml HA LLEGADO: " + xml);
			
			return xml;

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
		
	}
		
	@Path("/getProcessByPk({id:.+},{userId:.+})")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public WProcessHead getProcessByPk(@PathParam("id") Integer id, @PathParam("userId") Integer userId) {

		WProcessHead returnValue = null;
		
		String message = "";
		
		if (id != null
				&& !id.equals(0)
				&& userId != null
				&& !userId.equals(0)){
			
			try {
				
				returnValue = new WProcessHeadBL().getProcessByPK(id, userId);
			
				if (returnValue != null){
					
					
					
				}
				
				message = "SERVICIO WEB OK!";

			} catch (WProcessException e) {
				message = "SERVICIO WEB ERROR!:\n" + e.getMessage() + " - " + e.getCause();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		System.out.println(message);

		return returnValue;

	}
		
}