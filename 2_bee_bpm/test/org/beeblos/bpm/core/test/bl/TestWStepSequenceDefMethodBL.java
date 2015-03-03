package org.beeblos.bpm.core.test.bl;


import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WExternalMethodBL;
import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepSequenceDefBL;
import org.beeblos.bpm.core.model.WExternalMethod;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.junit.Test;




public class TestWStepSequenceDefMethodBL extends TestCase{
		
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
		
		/**
		 * Leo simplemente 1 registro step-sequence que se que tiene 1 metodo para 
		 * ver si lo carga correctamente.
		 * Queda pendiente armar el test completo.
		 * Nes 20140207
		 * 
		 * @throws Exception
		 */
		@Test
		public void testAgregarWStepSequenceDef() throws Exception {
			
			route = routeBL.getWStepSequenceDefByPK(7, 1000);
			System.out.println("-------------------------Original--------------------------");
			System.out.println("-----------------------------------------------------------");
			System.out.println(route);
			System.out.println("-----------------------------------------------------------");
			
			WExternalMethod newMethod = new WExternalMethodBL().getExternalMethodByPK(101);
			System.out.println("-------------------------newMethod-------------------------");
			System.out.println("-----------------------------------------------------------");
			System.out.println(newMethod);
			System.out.println("-----------------------------------------------------------");
			
			route.getExternalMethod().add(newMethod);
			
			routeBL.update(route, 1000);
			System.out.println("-----------------------routeAfterUpdate--------------------");
			System.out.println("-----------------------------------------------------------");
			System.out.println(route);
			System.out.println("-----------------------------------------------------------");
			
			route = routeBL.getWStepSequenceDefByPK(route.getId(), 1000);
			System.out.println("-------------------------routeBBDD-------------------------");
			System.out.println("-----------------------------------------------------------");
			System.out.println(route);
			System.out.println("-----------------------------------------------------------");
			
		}

		
		
}