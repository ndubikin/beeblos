


import static org.beeblos.bpm.core.util.Constants.ALIVE;
import static org.beeblos.bpm.core.util.Constants.PROCESSED;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WProcessWorkBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepSequenceDefBL;
import org.beeblos.bpm.core.bl.WStepWorkBL;
import org.beeblos.bpm.core.dao.WTimeUnitDao;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepUser;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WStepWorkAssignment;
import org.beeblos.bpm.core.model.WTimeUnit;
import org.beeblos.bpm.core.model.WUserDef;
import org.junit.Test;





public class TestWStepWorkBL extends TestCase{
		
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
		
		// dml 20120125
		static WProcessWork processWork;
		static WProcessWorkBL processWorkBL;
		static Integer iProcessWork;
		
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
			
			processWork = new WProcessWork();
			processWorkBL = new WProcessWorkBL();
			
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
			
			// precondiciones: doy de alta un proceso, un mapa y utilizo los pasos 10,20,24,40,50
			// doy de alta un proceso para que la ruta se pueda referir a él ...
			process.setBeginStep(stepBL.getWStepDefByPK(10, null, 1001));
			process.setComments("mis comentarios");

			process.setName("Mi primero worflo");
						
			iproc = processBL.add(process,1000);
			
			processWork.setProcessDef(process.getAsProcessDefThin());
//			processWork.setVersion(1);
			processWork.setStartingTime(new Date());
			processWork.setReference("process work reference");
			processWork.setIdObject(750);
			processWork.setIdObjectType("object type");
			
			iProcessWork = processWorkBL.add(processWork, 1000);
			
			
			WStepDef paso10 = stepBL.getWStepDefByPK(10, null, 1001);
			WStepDef paso20 = stepBL.getWStepDefByPK(20, null, 1001);
			WStepDef paso24 = stepBL.getWStepDefByPK(24, null, 1001);
			WStepDef paso30 = stepBL.getWStepDefByPK(30, null, 1001);
			WStepDef paso40 = stepBL.getWStepDefByPK(40, null, 1001);
			WStepDef paso50 = stepBL.getWStepDefByPK(50, null, 1001);
			WStepDef paso90 = stepBL.getWStepDefByPK(90, null, 1001);
			
			// cargo la ruta
			// presupongo que existen los step con id 800 y 20
			WStepSequenceDef route1 = new WStepSequenceDef(process,1,paso10,paso20, true, true, false, null, "");
			
			WStepSequenceDef route2 = new WStepSequenceDef(process,2,paso20,paso24, true, false, false, null, "");
			WStepSequenceDef route3 = new WStepSequenceDef(process,3,paso20,paso30, true, true , false, null, "");

			WStepSequenceDef route4 = new WStepSequenceDef(process,4,paso24,paso40, true, true, false, null, "");
			WStepSequenceDef route5 = new WStepSequenceDef(process,5,paso30,paso50, true, false, false, null, "");

			WStepSequenceDef route6 = new WStepSequenceDef(process,6,paso40,null, true, false, false, null, "");
			WStepSequenceDef route7 = new WStepSequenceDef(process,7,paso40,paso90, true, false, false, null, "");
			WStepSequenceDef route8 = new WStepSequenceDef(process,8,paso50,paso90, true, true, false, null, "");			
			

			iroute = routeBL.add(route3,1000);
			iroute = routeBL.add(route7,1000);
			iroute = routeBL.add(route5,1000);
			iroute = routeBL.add(route8,1000);
			iroute = routeBL.add(route4,1000);
			iroute = routeBL.add(route1,1000);
			iroute = routeBL.add(route6,1000);
			iroute = routeBL.add(route2,1000);
			
			// hago unas verificaciones mínimas para asegurarme que mas o menos quedaron bien
			// las precondiciones:
			
//			assertEquals(8, routeBL.getWStepSequenceDefs(1001).size());
			
			assertEquals(2, routeBL.getStepSequenceList(iproc, 40,1001).size());
			assertEquals(2, routeBL.getStepSequenceList(iproc, 20,1001).size());
			assertEquals(1, routeBL.getStepSequenceList(iproc, 10,1001).size());
			
			
			
			Date now = new Date();
			SimpleDateFormat fmtDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			SimpleDateFormat fmtDate 	 = new SimpleDateFormat("yyyy-MM-dd");
//			SimpleDateFormat fmtTime 	 = new SimpleDateFormat("hh:mm:ss");
			
			WTimeUnit tu = new WTimeUnitDao().getWTimeUnitByPK(1);
			
			
			
//			public WStepWork( id,  process,  version,
//					WStepDef previousStep, WStepDef currentStep,  idObject,
//					String idObjectType,  arrivingDate,  openedDate,
//					String openerUser, Date decidedDate, String performer,
//					WTimeUnit timeUnit,  assignedTime, Date deadlineDate,
//					Date deadlineTime, WTimeUnit reminderTimeUnit,
//					 reminderTime, boolean adminProcess, boolean locked,
//					String lockedBy, Date lockedSince)
			
			// agrego registro step w
			
			// TODO ESTO NO FUNCIONA ... QUEDÓ ROTO DESPUES DE AJUSTAR EL MODEL
			// NES 20110107
			
			WStepWork stepWork = new WStepWork();
					
			stepWork.setInsertUser(new WUserDef(1003));
			stepWork.setModDate(now);
			stepWork.setModUser(1003);
			
			iStepW = stepwBL.add(stepWork,1001);
					
			
			// lo leo
			stepw = stepwBL.getWStepWorkByPK(iStepW, 1001);
			
			// verifico campos cargados
			assertEquals(iStepW, stepw.getId());

			assertEquals(paso10, stepw.getPreviousStep());
			assertEquals(paso20, stepw.getCurrentStep());

			assertEquals(fmtDateTime.format(now), fmtDateTime.format(stepw.getArrivingDate()));

			assertEquals(fmtDateTime.format(now), fmtDateTime.format(stepw.getOpenedDate()));
			assertEquals(new Integer(1000), stepw.getOpenerUser());

			assertEquals(fmtDateTime.format(now), fmtDateTime.format(stepw.getDecidedDate()));
			assertEquals("nina", stepw.getPerformer());

			assertEquals(tu, stepw.getTimeUnit());
			assertEquals(new Integer(2345), stepw.getAssignedTime());

			System.out.println("--------->>>>"+stepw.getDeadlineDate());
			assertEquals(fmtDate.format(now), fmtDate.format(stepw.getDeadlineDate()));
			assertEquals(null, stepw.getDeadlineTime());
			
			assertFalse(stepw.isAdminProcess());
			assertTrue(stepw.isLocked());
			assertEquals(new Integer(1000), stepw.getLockedBy().getId()); // nes 20111218
			assertEquals(fmtDateTime.format(now), fmtDateTime.format(stepw.getLockedSince()));
			
			assertEquals(0, stepw.getAssignedTo().size()); // no hay asignaciones en este momento ...
			
			
			Date aDate = new Date();

			WStepWorkAssignment swa1 = new WStepWorkAssignment(1,1001,true,false,null,aDate,false,1000,aDate);
			
			
			swa1.setInsertDate(aDate);
			swa1.setInsertUser(1000);
			swa1.setModDate(aDate);
			swa1.setModUser(1000);
			
			stepw.getAssignedTo().add(swa1);
			
			// agrega 1 registro a la tabla de asignados (asigna el work item a 1 usuario o rol)
			stepwBL.update(stepw, 1000);
			
			WStepWork sw = stepwBL.getWStepWorkByPK(stepw.getId(), 1000);
			
			assertEquals(1, sw.getAssignedTo().size()); // no hay asignaciones en este momento ...
			
			System.out.println("------------------------------------------------------------------------------");
			
			System.out.println("----->>> step:"+sw.toString());
			
			for (WStepWorkAssignment swa: sw.getAssignedTo()) {
				System.out.println("===========>>>>>>> assigned to:"+swa.toString());
			}
			
			System.out.println("------------------- RECUPERO LISTA DE WORKITEMS ( TOTAL ) ---------------------------");
			
			List<WStepWork> lsw = new ArrayList<WStepWork>();
			lsw = stepwBL.getWorkListByProcessAndStatus(69, "", 1000);
			for (WStepWork wsw: lsw) {
				System.out.println(wsw);
			}
			
			System.out.println("------------------- RECUPERO LISTA DE WORKITEMS ( VIVOS ) ---------------------------");
			
			lsw = new ArrayList<WStepWork>();
			lsw = stepwBL.getWorkListByProcessAndStatus(69, ALIVE, 1000);
			for (WStepWork wsw: lsw) {
				System.out.println(wsw);
			}
			
			System.out.println("-------------- RECUPERO LISTA DE WORKITEMS ( VIVOS DEL USUARIO ( O ROL ) ) ---------------------------");
			
			lsw = new ArrayList<WStepWork>();
			lsw = stepwBL.getWorkListByProcessAndStatus(69, PROCESSED, 1000);
			for (WStepWork wsw: lsw) {
				System.out.println(wsw);
			}
			
			
			
			System.out.println("--------------- RECUPERO LISTA DE WORKITEMS DE UN OBJETO ---------------------------");
			
			lsw = new ArrayList<WStepWork>();
			lsw = stepwBL.getWorkListByProcess(stepw.getwProcessWork().getProcessDef().getId(), stepw.getwProcessWork().getIdObject(), stepw.getwProcessWork().getIdObjectType(), 1000);
			for (WStepWork wsw: lsw) {
				System.out.println(wsw);
			}
			
			System.out.println("------------------------------------------------------------------------------");

			
			
			
			// borro todo antes de irme ...
			stepwBL.delete(stepw, 1003);
//			assertNull(stepwBL.getWStepWorkByPK(iStepW,"Juancito"));
			
			// all process map delete
			routeBL.deleteProcessRoutes(process, 1002);
//			assertEquals(0, routeBL.getWStepSequenceDefs(1001).size());
			
			
			// ahora borro el proceso
			new WProcessDefBL().delete( iproc, false,1000) ;
//			assertNull(processBL.getWProcessDefByPK(iproc,"Juancito"));
		

		}

		
}