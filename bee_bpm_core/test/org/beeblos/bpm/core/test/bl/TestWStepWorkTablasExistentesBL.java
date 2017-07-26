package org.beeblos.bpm.core.test.bl;

/*
 * TEST SENCILLO QUE DEVUELVE LA LISTA DE PASOS DE UN USUARIO Y DEVUELVE O BIEN TODOS
 * O BIEN SOLO LOS VIVOS
 * 
 * ( BASADO EN LOS ROLES QUE TIENE ASIGNADOS EL USUARIO )
 */

import static org.beeblos.bpm.core.util.Constants.ALIVE;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepSequenceDefBL;
import org.beeblos.bpm.core.bl.WStepWorkBL;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.junit.Test;





public class TestWStepWorkTablasExistentesBL extends TestCase{
		
		static WStepWork stepw;
		static WStepWorkBL stepwBL;
		static Integer iStepW;

		// para las precondiciones:
		static WStepSequenceDef route;
		static WStepSequenceDefBL routeBL;
		static Integer iroute;
		
		static WProcessDef process;
		static WProcessDefBL processBL;
		static Integer iproc;
		
		static Integer version1=1;

		static WStepDefBL stepBL;
		
		protected void setUp() throws Exception {
			
			stepw = new WStepWork();
			stepwBL = new WStepWorkBL();
			
			// para las precondiciones
			route = new WStepSequenceDef();
			routeBL = new WStepSequenceDefBL();
			
			process = new WProcessDef();
			processBL = new WProcessDefBL();
			
			stepBL = new WStepDefBL();
			
		}
		
		protected void tearDown() throws Exception {
			
			stepw = null;
			stepwBL = null;
			
			// para las precondiciones
			route = null;
			routeBL = null;
			
			process = null;
			processBL = null;
			
			stepBL = null;
			
		}
		
		@Test
		
		public void testAgregarWStepWork() throws Exception {
			

			
			System.out.println("------------------- RECUPERO LISTA DE WORKITEMS ( TOTAL ) ---------------------------");
			
			List<WStepWork> lsw = new ArrayList<WStepWork>();
			lsw = stepwBL.getStepListByUser(1001, null, null, 1001);
			Integer tamanio, total=0;
			for (WStepWork wsw: lsw) {
				tamanio=wsw.toString().length();
				System.out.println("-->> id:"+wsw.getId()+" paso:"+wsw.getCurrentStep().getName()+"  ["+(wsw.getLockedBy().getId()!=null?wsw.getLockedBy().getId():"")+"] -->["+tamanio+"]");// nes 20111218
				total+=tamanio;
			}
			System.out.println("Total pasos recuperados: "+lsw.size());
			System.out.println("peso total de la lista en bytes: ["+total+"]");
			System.out.println("\n");
			
			System.out.println("------------------- RECUPERO LISTA DE WORKITEMS ( VIVOS ) ---------------------------");
			
			lsw = new ArrayList<WStepWork>();

			lsw = stepwBL.getStepListByUser(1001, null, ALIVE, 1001);
			
			total=0;
			for (WStepWork wsw: lsw) {
				tamanio=wsw.toString().length();
				System.out.println("-->> id:"+wsw.getId()+" paso:"+wsw.getCurrentStep().getName()+"  ["+(wsw.getLockedBy()!=null?wsw.getLockedBy():"")+"] -->["+tamanio+"]");
				total+=tamanio;
			}
			System.out.println("Total pasos recuperados: "+lsw.size());
			System.out.println("peso total de la lista en bytes: ["+total+"]");
			System.out.println("\n");			
			
//			
//			System.out.println("-------------- RECUPERO LISTA DE WORKITEMS ( VIVOS DEL USUARIO ( O ROL ) ) ---------------------------");
//			
//			lsw = new ArrayList<WStepWork>();
//			lsw = stepwBL.getStepListByProcess(69, PROCESSED, 1000);
//			for (WStepWork wsw: lsw) {
//				System.out.println(wsw);
//			}
//			
//			
			
//			System.out.println("--------------- RECUPERO LISTA DE WORKITEMS DE UN OBJETO ---------------------------");
//			
//			lsw = new ArrayList<WStepWork>();
//			lsw = stepwBL.getStepListByProcess(swco.getProcess().getId(), swco.getIdObject(), swco.getIdObjectType(), 1000);
//			for (WStepWork wsw: lsw) {
//				System.out.println(wsw);
//			}
//			
			System.out.println("------------------------------------------------------------------------------");
//
//			
//			


		}
		
}