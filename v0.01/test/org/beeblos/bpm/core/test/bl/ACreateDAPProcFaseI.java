package org.beeblos.bpm.core.test.bl;


import java.util.Date;

import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepSequenceDefBL;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.junit.Test;





public class ACreateDAPProcFaseI extends TestCase{
		
		static WStepDef step;
		static WStepDefBL stepBL;
		static Integer istep;

		static WProcessDef process;
		static WProcessDefBL processBL;
		static Integer iproc;

		static WStepSequenceDef route;
		static WStepSequenceDefBL routeBL;
		static Integer iroute;
		
		static Integer user = 1000;
		static String respuestasValidas = "";
		
		protected void setUp() throws Exception {
			
			step = new WStepDef();
			stepBL = new WStepDefBL();
			
			
			process = new WProcessDef();
			processBL = new WProcessDefBL();
			
			route = new WStepSequenceDef();
			routeBL = new WStepSequenceDefBL();
			
		}
		
		protected void tearDown() throws Exception {
			
			step = null;
			stepBL = null;
			
		}

		

		
		private void _testCreoElProceso() throws Exception {
		
			Date now = new Date();
			step = stepBL.getWStepDefByPK(10,null,  user);
			
			process.setBeginStep(step);
			process.setComments("Comienzo del workflow de DAP - Fase I ");

			process.setName("Proyectos - Fase I");

		
			iproc = processBL.add(process, 1000);
		
		}
		
		
		
		@Test
		
		public void testAgregarWStepDef() throws Exception {
			
			// CREA EL PROCESO
			_testCreoElProceso();
			
			// AGREGA LAS RESPUESTAS Y LAS ASIGNACIONES A LOS PASOS
			//-----------------------------------------------------------------------------------------------------			
			WStepDef paso = stepBL.getWStepDefByPK(10,null,  user);
			//-----------------------------------------------------------------------------------------------------
			
			// agrego respuestas
			paso.getResponse().add(new WStepResponseDef(1,"Para valorar"));
			paso.getResponse().add(new WStepResponseDef(2,"Propuesta incompleta"));
			paso.getResponse().add(new WStepResponseDef(3,"Propuesta denegada"));
			
			// agrego asignaciones
//			paso.getAssigned().add(new WStepAssignedDef("DAP","com.softpoint.wf.Role"));

			stepBL.update(paso,null,  user);
			System.out.println("----->>> actualizado paso:"+paso.getId()+" "+paso.getName());
			
			// agrega las rutas desde paso 10

			// Propuesta incompleta
			//-----------------------			
			WStepSequenceDef ruta = new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(false);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(14, null, user));
			ruta.setProcess(process);
			respuestasValidas = "";
			for (WStepResponseDef resp: paso.getResponse()) {
				respuestasValidas+= (resp.getName().contains("Propuesta incompleta")? resp.getId().toString()+"|":"");
			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);
			
			// Para valorar
			//-----------------------
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(false);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(20,null,  user));
			ruta.setProcess(process);
			respuestasValidas = "";
			for (WStepResponseDef resp: paso.getResponse()) {
				respuestasValidas+= (resp.getName().contains("Para valorar")? resp.getId().toString()+"|":"");
			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);			

			// Propuesta denegada
			//-----------------------			
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(false);
			ruta.setFromStep(paso);
			ruta.setToStep(null);
			ruta.setProcess(process);
			respuestasValidas = "";
			for (WStepResponseDef resp: paso.getResponse()) {
				respuestasValidas+= (resp.getName().contains("Propuesta denegada")? resp.getId().toString()+"|":"");
			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);	
			
			//-----------------------------------------------------------------------------------------------------
			paso = stepBL.getWStepDefByPK(14,null,  user);
			//-----------------------------------------------------------------------------------------------------
			
			// agrego respuestas
			paso.getResponse().add(new WStepResponseDef(1,"OK"));
			
			// agrego asignaciones
//			paso.getAssigned().add(new WStepAssignedDef("DAP","como.softpoint.wf.Rol"));

			stepBL.update(paso, null, user);			
			System.out.println("----->>> actualizado paso:"+paso.getId()+" "+paso.getName());
			
			// RUTA OK
			//--------			
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(true);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(10, null, user));
			ruta.setProcess(process);
			respuestasValidas = "";
//			for (WStepResponseDef resp: paso.getResponse()) {
//				respuestasValidas+= (resp.getName().contains("OK")? resp.getId().toString()+"|":"");
//			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);	
			
			
			
			//-----------------------------------------------------------------------------------------------------
			paso = stepBL.getWStepDefByPK(20,null,  user);
			//-----------------------------------------------------------------------------------------------------
			
			// agrego respuestas
			paso.getResponse().add(new WStepResponseDef(1,"OK"));
			paso.getResponse().add(new WStepResponseDef(2,"Proyecto Editorial"));
			paso.getResponse().add(new WStepResponseDef(3,"Actividad de Musica"));
			
			// agrego asignaciones
//			paso.getAssigned().add(new WStepAssignedDef("DAP_D","como.softpoint.wf.Rol"));

			stepBL.update(paso, null, user);			
			System.out.println("----->>> actualizado paso:"+paso.getId()+" "+paso.getName());
			
			
			// RUTA OK
			//--------
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(false);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(30, null, user));
			ruta.setProcess(process);
			respuestasValidas = "";
			for (WStepResponseDef resp: paso.getResponse()) {
				respuestasValidas+= (resp.getName().contains("OK")? resp.getId().toString()+"|":"");
			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);	

			// RUTA PROYECTO EDITORIAL
			//-------------------------
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(false);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(710, null, user));
			ruta.setProcess(process);
			respuestasValidas = "";
			for (WStepResponseDef resp: paso.getResponse()) {
				respuestasValidas+= (resp.getName().contains("Proyecto Editorial")? resp.getId().toString()+"|":"");
			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);	
			
			// RUTA Actividad de Musica
			//-------------------------
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(false);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(320,null,  user));
			ruta.setProcess(process);
			respuestasValidas = "";
			for (WStepResponseDef resp: paso.getResponse()) {
				respuestasValidas+= (resp.getName().contains("Actividad de Musica")? resp.getId().toString()+"|":"");
			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);	
			
			//-----------------------------------------------------------------------------------------------------
			paso = stepBL.getWStepDefByPK(320,null,  user);
			//-----------------------------------------------------------------------------------------------------
			
			// agrego respuestas
			paso.getResponse().clear(); // no hay respuestas solo 1 camino ....
			
			// agrego asignaciones
//			paso.getAssigned().add(new WStepAssignedDef("GTD_M","como.softpoint.wf.Rol"));

			stepBL.update(paso,null,  user);			
			System.out.println("----->>> actualizado paso:"+paso.getId()+" "+paso.getName());

			// RUTA OK (AFTERALL)
			//--------			
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(true);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(30,null,  user));
			ruta.setProcess(process);
			respuestasValidas = null;
//			for (WStepResponseDef resp: paso.getResponse()) {
//				respuestasValidas+= (resp.getName().contains("OK")? resp.getId().toString()+"|":"");
//			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);	
			
			//-----------------------------------------------------------------------------------------------------
			paso = stepBL.getWStepDefByPK(710, null, user);
			//-----------------------------------------------------------------------------------------------------
			
			// agrego respuestas
			paso.getResponse().clear(); // no hay respuestas solo 1 camino ....
			
			// agrego asignaciones
//			paso.getAssigned().add(new WStepAssignedDef("DE","como.softpoint.wf.Rol"));

			stepBL.update(paso, null, user);			
			System.out.println("----->>> actualizado paso:"+paso.getId()+" "+paso.getName());

			// RUTA OK (AFTERALL)
			//--------			
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(true);
			ruta.setFromStep(paso);
			ruta.setToStep(null);
			ruta.setProcess(process);
			respuestasValidas = null;
//			for (WStepResponseDef resp: paso.getResponse()) {
//				respuestasValidas+= (resp.getName().contains("OK")? resp.getId().toString()+"|":"");
//			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);				
			//-----------------------------------------------------------------------------------------------------
			paso = stepBL.getWStepDefByPK(30,null,  user);
			//-----------------------------------------------------------------------------------------------------
			
			// agrego respuestas
			paso.getResponse().clear(); // no hay respuestas solo 1 camino ....
			
			// agrego asignaciones
//			paso.getAssigned().add(new WStepAssignedDef("DAP_D","como.softpoint.wf.Rol"));

			stepBL.update(paso,null,  user);			
			System.out.println("----->>> actualizado paso:"+paso.getId()+" "+paso.getName());

			// RUTA OK (AFTERALL)
			//---------------------			
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(true);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(200,null,  user));
			ruta.setProcess(process);
			respuestasValidas = null;
//			for (WStepResponseDef resp: paso.getResponse()) {
//				respuestasValidas+= (resp.getName().contains("OK")? resp.getId().toString()+"|":"");
//			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);	
			
			//-----------------------------------------------------------------------------------------------------
			paso = stepBL.getWStepDefByPK(200,null,  user);
			//-----------------------------------------------------------------------------------------------------
			
			// agrego respuestas
			paso.getResponse().add(new WStepResponseDef(1,"A estudio"));
			paso.getResponse().add(new WStepResponseDef(2,"Aprobada"));
			paso.getResponse().add(new WStepResponseDef(3,"Denegada - aviso email"));
			paso.getResponse().add(new WStepResponseDef(4,"Denegada - envio postal"));
			
			// agrego asignaciones
//			paso.getAssigned().add(new WStepAssignedDef("D","como.softpoint.wf.Rol"));

			stepBL.update(paso, null, user);			
			System.out.println("----->>> actualizado paso:"+paso.getId()+" "+paso.getName());

			// RUTA A estudio
			//-------------------------
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(false);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(22,null,  user));
			ruta.setProcess(process);
			respuestasValidas = "";
			for (WStepResponseDef resp: paso.getResponse()) {
				respuestasValidas+= (resp.getName().contains("A estudio")? resp.getId().toString()+"|":"");
			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);			

			// RUTA Aprobada
			//-------------------------
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(false);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(220, null, user));
			ruta.setProcess(process);
			respuestasValidas = "";
			for (WStepResponseDef resp: paso.getResponse()) {
				respuestasValidas+= (resp.getName().contains("Aprobada")? resp.getId().toString()+"|":"");
			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);	
			
			// RUTA Denegada - aviso email
			//-------------------------
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(false);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(42,null,  user));
			ruta.setProcess(process);
			respuestasValidas = "";
			for (WStepResponseDef resp: paso.getResponse()) {
				respuestasValidas+= (resp.getName().contains("Denegada - aviso email")? resp.getId().toString()+"|":"");
				respuestasValidas+= (resp.getName().contains("Denegada - envio postal")? resp.getId().toString()+"|":"");
			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);	
			
			// RUTA Denegada - envio postal
			//-------------------------
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(false);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(222, null, user));
			ruta.setProcess(process);
			respuestasValidas = "";
			for (WStepResponseDef resp: paso.getResponse()) {
				respuestasValidas+= (resp.getName().contains("Denegada - envio postal")? resp.getId().toString()+"|":"");
			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);	
			
			
			//-----------------------------------------------------------------------------------------------------
			paso = stepBL.getWStepDefByPK(22, null, user); // GESTION DE EVALUADORES
			//-----------------------------------------------------------------------------------------------------
			
			// agrego respuestas
			paso.getResponse().clear(); // no hay respuestas solo 1 camino ....
			
			// agrego asignaciones
//			paso.getAssigned().add(new WStepAssignedDef("DAP_D","como.softpoint.wf.Rol"));

			stepBL.update(paso, null, user);			
			System.out.println("----->>> actualizado paso:"+paso.getId()+" "+paso.getName());

			// RUTA OK (AFTERALL)
			//---------------------			
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(true);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(24, null, user));
			ruta.setProcess(process);
			respuestasValidas = null;
//			for (WStepResponseDef resp: paso.getResponse()) {
//				respuestasValidas+= (resp.getName().contains("OK")? resp.getId().toString()+"|":"");
//			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);
			
			//-----------------------------------------------------------------------------------------------------
			paso = stepBL.getWStepDefByPK(24,null,  user); // PDTE EVALUACION EXTERNA
			//-----------------------------------------------------------------------------------------------------
			
			// agrego respuestas
			paso.getResponse().clear(); // no hay respuestas solo 1 camino ....
			
			// agrego asignaciones
//			paso.getAssigned().add(new WStepAssignedDef("DAP_D","como.softpoint.wf.Rol"));

			stepBL.update(paso,null,  user);			
			System.out.println("----->>> actualizado paso:"+paso.getId()+" "+paso.getName());

			// RUTA OK (AFTERALL)
			//---------------------			
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(true);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(200, null, user));
			ruta.setProcess(process);
			respuestasValidas = null;
//			for (WStepResponseDef resp: paso.getResponse()) {
//				respuestasValidas+= (resp.getName().contains("OK")? resp.getId().toString()+"|":"");
//			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);
			
			//-----------------------------------------------------------------------------------------------------
			paso = stepBL.getWStepDefByPK(220, null, user); // GTD - PROCESAMIENTO DE PROPUESTA APROBADA
			//-----------------------------------------------------------------------------------------------------
			
			// agrego respuestas
			paso.getResponse().clear(); // no hay respuestas solo 1 camino ....
			
			// agrego asignaciones
//			paso.getAssigned().add(new WStepAssignedDef("GTD","como.softpoint.wf.Rol"));

			stepBL.update(paso,null,  user);			
			System.out.println("----->>> actualizado paso:"+paso.getId()+" "+paso.getName());

			// RUTA OK (AFTERALL)
			//---------------------			
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(true);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(50, null, user));
			ruta.setProcess(process);
			respuestasValidas = null;
//			for (WStepResponseDef resp: paso.getResponse()) {
//				respuestasValidas+= (resp.getName().contains("OK")? resp.getId().toString()+"|":"");
//			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);

			// RUTA OK (AFTERALL)
			//---------------------			
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(true);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(400, null, user));
			ruta.setProcess(process);
			respuestasValidas = null;
//			for (WStepResponseDef resp: paso.getResponse()) {
//				respuestasValidas+= (resp.getName().contains("OK")? resp.getId().toString()+"|":"");
//			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);	
			
			
			//-----------------------------------------------------------------------------------------------------
			paso = stepBL.getWStepDefByPK(222, null, user); // GTD - ENVIO CARTA POSTAL AVISO DENEGACIÓN PROPUESTA
			//-----------------------------------------------------------------------------------------------------
			
			// agrego respuestas
			paso.getResponse().add(new WStepResponseDef(1,"OK"));
			
			// agrego asignaciones
//			paso.getAssigned().add(new WStepAssignedDef("GTD","como.softpoint.wf.Rol"));

			stepBL.update(paso, null, user);			
			System.out.println("----->>> actualizado paso:"+paso.getId()+" "+paso.getName());

			// RUTA OK (AFTERALL)
			//---------------------			
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(true);
			ruta.setFromStep(paso);
			ruta.setToStep(null);
			ruta.setProcess(process);
			respuestasValidas = null;
//			for (WStepResponseDef resp: paso.getResponse()) {
//				respuestasValidas+= (resp.getName().contains("OK")? resp.getId().toString()+"|":"");
//			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);
			
			//-----------------------------------------------------------------------------------------------------
			paso = stepBL.getWStepDefByPK(42, null, user); // AVISO DENEGACION POR EMAIL Y ARCHIVO
			//-----------------------------------------------------------------------------------------------------
			
			// agrego respuestas
			paso.getResponse().add(new WStepResponseDef(1,"OK"));
			
			// agrego asignaciones
//			paso.getAssigned().add(new WStepAssignedDef("DAP_D","como.softpoint.wf.Rol"));

			stepBL.update(paso, null, user);			
			System.out.println("----->>> actualizado paso:"+paso.getId()+" "+paso.getName());

			// RUTA OK (AFTERALL)
			//---------------------			
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(true);
			ruta.setFromStep(paso);
			ruta.setToStep(null);
			ruta.setProcess(process);
			respuestasValidas = null;
//			for (WStepResponseDef resp: paso.getResponse()) {
//				respuestasValidas+= (resp.getName().contains("OK")? resp.getId().toString()+"|":"");
//			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);
			
			//-----------------------------------------------------------------------------------------------------
		
			
			//-----------------------------------------------------------------------------------------------------
			paso = stepBL.getWStepDefByPK(50,null,  user); // AVISO APROBACION - DAP COMUNICA
			//-----------------------------------------------------------------------------------------------------
			
			// agrego respuestas
			paso.getResponse().add(new WStepResponseDef(1,"Requiere publicacion"));
			paso.getResponse().add(new WStepResponseDef(2,"No requiere publicacion"));
			
			// agrego asignaciones
//			paso.getAssigned().add(new WStepAssignedDef("DAP_D","como.softpoint.wf.Rol"));

			stepBL.update(paso, null, user);			
			System.out.println("----->>> actualizado paso:"+paso.getId()+" "+paso.getName());


			// RUTA Requiere publicacion
			//-------------------------
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(false);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(700,null,  user));
			ruta.setProcess(process);
			respuestasValidas = "";
			for (WStepResponseDef resp: paso.getResponse()) {
				respuestasValidas+= (resp.getName().contains("Requiere publicacion")? resp.getId().toString()+"|":"");
			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);	
			
			// RUTA No requiere publicacion
			//-------------------------
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(false);
			ruta.setFromStep(paso);
			ruta.setToStep(null);
			ruta.setProcess(process);
			respuestasValidas = "";
			for (WStepResponseDef resp: paso.getResponse()) {
				respuestasValidas+= (resp.getName().contains("No requiere publicacion")? resp.getId().toString()+"|":"");
			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);	
			
			
			//-----------------------------------------------------------------------------------------------------
			paso = stepBL.getWStepDefByPK(400,null,  user); // DFAG PREPARA BORRADOR
			//-----------------------------------------------------------------------------------------------------
			
			// agrego respuestas
			paso.getResponse().add(new WStepResponseDef(1,"Requiere publicacion"));
			paso.getResponse().add(new WStepResponseDef(2,"No requiere publicacion"));
			
			// agrego asignaciones
//			paso.getAssigned().add(new WStepAssignedDef("DFAG","como.softpoint.wf.Rol"));

			stepBL.update(paso, null, user);			
			System.out.println("----->>> actualizado paso:"+paso.getId()+" "+paso.getName());

			// RUTA OK (AFTERALL)
			//---------------------			
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(true);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(250,null,  user));
			ruta.setProcess(process);
			respuestasValidas = null;
//			for (WStepResponseDef resp: paso.getResponse()) {
//				respuestasValidas+= (resp.getName().contains("OK")? resp.getId().toString()+"|":"");
//			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);
			
			// RUTA OK (AFTERALL)
			//---------------------			
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(true);
			ruta.setFromStep(paso);
			ruta.setToStep(stepBL.getWStepDefByPK(56,null,  user));
			ruta.setProcess(process);
			respuestasValidas = null;
//			for (WStepResponseDef resp: paso.getResponse()) {
//				respuestasValidas+= (resp.getName().contains("OK")? resp.getId().toString()+"|":"");
//			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);			

			
			//-----------------------------------------------------------------------------------------------------
			paso = stepBL.getWStepDefByPK(700,null,  user); // DE PUBLICA
			//-----------------------------------------------------------------------------------------------------
			
			// no tiene respuestas ...
			paso.getResponse().clear(); // no hay respuestas solo 1 camino ....

			
			// agrego asignaciones
//			paso.getAssigned().add(new WStepAssignedDef("DE","como.softpoint.wf.Rol"));

			stepBL.update(paso,null,  user);			
			System.out.println("----->>> actualizado paso:"+paso.getId()+" "+paso.getName());

			
			// RUTA publica y fin
			//-------------------------
			ruta=new WStepSequenceDef();
			ruta.setEnabled(true);
			ruta.setAfterAll(true);
			ruta.setFromStep(paso);
			ruta.setToStep(null);
			ruta.setProcess(process);
			respuestasValidas = "";
//			for (WStepResponseDef resp: paso.getResponse()) {
//				respuestasValidas+= (resp.getName().contains("No requiere publicacion")? resp.getId().toString()+"|":"");
//			}
			ruta.setValidResponses(respuestasValidas);
			routeBL.add(ruta, user);	
						
			
			//-----------------------------------------------------------------------------------------------------
	
			
		}
		

	
		
}