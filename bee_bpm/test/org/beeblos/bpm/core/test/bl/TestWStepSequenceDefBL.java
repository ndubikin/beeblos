package org.beeblos.bpm.core.test.bl;


import java.util.Date;

import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepSequenceDefBL;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.junit.Test;




public class TestWStepSequenceDefBL extends TestCase{
		
		static WStepSequenceDef route;
		static WStepSequenceDefBL routeBL;
		static Integer iroute;
		
		static WProcessDef process;
		static WProcessDefBL processBL;
		static Integer iproc;
		
		static Integer version1=1;

		static WStepDefBL stepBL;
		
		protected void setUp() throws Exception {
			
			route = new WStepSequenceDef();
			routeBL = new WStepSequenceDefBL();
			
			process = new WProcessDef();
			processBL = new WProcessDefBL();
			
			stepBL = new WStepDefBL();
			
		}
		
		protected void tearDown() throws Exception {
			
			route = null;
			routeBL = null;
			
			process = null;
			processBL = null;
			
			stepBL = null;
			
		}
		
		@Test
		
		public void testAgregarWStepSequenceDef() throws Exception {
			
			// doy de alta un proceso para que la ruta se pueda referir a él ...
			process.setBeginStep(null);
			process.setComments("mis comentarios");

			process.setName("Mi primero worflo");
			
			
			iproc = processBL.add(process,1000);
			
			
			// cargo la ruta
			// presupongo que existen los step con id 800 y 20
			route.setProcess(process);
			route.setFromStep(stepBL.getWStepDefByPK(800, 1002));
			route.setToStep(stepBL.getWStepDefByPK(20, 1002));
			route.setVersion(version1);
			route.setEnabled(true);
			
			iroute = routeBL.add(route,1000);
			
			WStepSequenceDef wss = routeBL.getWStepSequenceDefByPK(iroute, 1000);
			
			assertEquals(iroute, wss.getId());
			assertEquals(new Integer(800), wss.getFromStep().getId());
			assertEquals(new Integer(20), wss.getToStep().getId());
			assertEquals(version1, wss.getVersion());
			assertTrue(wss.isEnabled());

			
			
			new WStepSequenceDefBL().deleteRoute(route,1000) ;
			assertNull(routeBL.getWStepSequenceDefByPK(iroute,1002));
			
			// ahora borro el proceso
			new WProcessDefBL().delete( process,1000) ;
			assertNull(processBL.getWProcessDefByPK(iproc,1000));
			
		
		}
		
		@Test
		
		public void testSecuenciaCompleta() throws Exception {
			
			// doy de alta un proceso para que la ruta se pueda referir a él ...
			process.setBeginStep(stepBL.getWStepDefByPK(10, 1000));
			process.setComments("mis comentarios");

			process.setName("Mi primero worflo");
			
			iproc = processBL.add(process,1000);
			

//			public WStepSequenceDef(WProcessDef process, Integer version,
//					WStepDef fromStep, WStepDef toStep, boolean enabled,
//					boolean afterAll, String validResponses) {
			// cargo la ruta
			// presupongo que existen los step con id 800 y 20
			
			
			WStepDef paso10 = stepBL.getWStepDefByPK(10, 1000);
			WStepDef paso20 = stepBL.getWStepDefByPK(20, 1000);
			WStepDef paso24 = stepBL.getWStepDefByPK(24, 1000);
			WStepDef paso30 = stepBL.getWStepDefByPK(30, 1000);
			WStepDef paso40 = stepBL.getWStepDefByPK(40, 1000);
			WStepDef paso50 = stepBL.getWStepDefByPK(50, 1000);
			WStepDef paso90 = stepBL.getWStepDefByPK(90, 1000);
			
//			WStepSequenceDef route1 = new WStepSequenceDef(process,version1,paso10,paso20, true, true, null);
//			
//			WStepSequenceDef route2 = new WStepSequenceDef(process,version1,paso20,paso24, true, false, null);
//			WStepSequenceDef route3 = new WStepSequenceDef(process,version1,paso20,paso30, true, true , null);
//
//			WStepSequenceDef route4 = new WStepSequenceDef(process,version1,paso24,paso40, true, true, null);
//			WStepSequenceDef route5 = new WStepSequenceDef(process,version1,paso30,paso50, true, false, null);
//
//			WStepSequenceDef route6 = new WStepSequenceDef(process,version1,paso40,null, true, false, null);
//			WStepSequenceDef route7 = new WStepSequenceDef(process,version1,paso40,paso90, true, false, null);
//			WStepSequenceDef route8 = new WStepSequenceDef(process,version1,paso50,paso90, true, true, null);			
			

//			iroute = routeBL.add(route3,1000);
//			iroute = routeBL.add(route7,1000);
//			iroute = routeBL.add(route5,1000);
//			iroute = routeBL.add(route8,1000);
//			iroute = routeBL.add(route4,1000);
//			iroute = routeBL.add(route1,1000);
//			iroute = routeBL.add(route6,1000);
//			iroute = routeBL.add(route2,1000);
			
			assertEquals(8, routeBL.getWStepSequenceDefs(1001).size());
			
			assertEquals(2, routeBL.getWStepSequenceDefs(iproc,version1, 40,1001).size());
			assertEquals(2, routeBL.getWStepSequenceDefs(iproc,version1, 20,1001).size());
			assertEquals(1, routeBL.getWStepSequenceDefs(iproc,version1, 10,1001).size());
			
			// single route delete
//			routeBL.deleteRoute(route4, 1002);
			assertEquals(7, routeBL.getWStepSequenceDefs(1001).size());
			
			// all process map delete
			routeBL.deleteProcess(process, 1, 1002);
			assertEquals(0, routeBL.getWStepSequenceDefs(1001).size());
			
			
			// ahora borro el proceso
			new WProcessDefBL().delete( process,1000) ;
			assertNull(processBL.getWProcessDefByPK(iproc,1000));
			
		
		}
	
		
		public void testErrorBorrarWStepSequenceDef() throws Exception {
		
			
			

				
		}		
		
}