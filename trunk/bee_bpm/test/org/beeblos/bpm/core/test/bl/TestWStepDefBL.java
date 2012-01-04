package org.beeblos.bpm.core.test.bl;


import java.util.Date;

import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WRoleDefBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WUserDef;
import org.junit.Test;





public class TestWStepDefBL extends TestCase{
		
		static WStepDef step;
		static WStepDefBL stepBL;
		static Integer iproc;


		
		protected void setUp() throws Exception {
			
			step = new WStepDef();
			stepBL = new WStepDefBL();
			
		}
		
		protected void tearDown() throws Exception {
			
			step = null;
			stepBL = null;
			
		}
		
		@Test
		
		public void testAgregarWStepDef() throws Exception {
			
			WRoleDefBL roleBl = new WRoleDefBL();
			Integer idRol1 = roleBl.add(new WRoleDef("rol s1", "descrip rol 1", null, "1..2..probando..."), 1000);
			Integer idRol2 = roleBl.add(new WRoleDef("rol s2", "desc rol 2", 1, "pepe"), 1000	);
			
			WUserDefBL userBl = new WUserDefBL();
			Integer idUser1 = userBl.add(new WUserDef( "juan ss", "jn", true, 1000, new Date()), 1000);
			Integer idUser2 = userBl.add(new WUserDef( "maria ss", "mr", true, 1000, new Date()), 1000);
			
			step = new WStepDef(null,"paso 1",2,3,"ejecute este paso plis","sincomentarios ...",null,null,null);
			step.getResponse().add(new WStepResponseDef(null,"Respuesta1"));
//			step.getAssigned().add(new WStepAssignedDef("pepe","user"));
			
			step.addRole( roleBl.getWRoleDefByPK(idRol1, 1000), false, 55, "tipo-objeto", 1000);
			step.addRole(roleBl.getWRoleDefByPK(idRol2, 1000), true, 55, "tipo-objeto", 1000);
			
			step.addUser(userBl.getWUserDefByPK(idUser1, 1000), false, 44, "hola", 1000);

			iproc = stepBL.add(step,1000);
			
			WStepDef sd = stepBL.getWStepDefByPK(iproc, 1000);
			
			System.out.println("---------------- ----------------------------- -------------------------------------");
			System.out.println("------->>> "+sd.toString());
			System.out.println("---------------- ----------------------------- -------------------------------------");
			
			assertEquals(iproc, stepBL.getWStepDefByPK(iproc, 1000).getId());
			

		}
		
//		public void testConsultasWStepDef() throws Exception{
//			
//
//			List<SelectItem> lc_combo = new ArrayList<SelectItem>();
//			lc_combo = new WStepDefBL().obtenerWStepDefsParaCombo("Seleccionar ...", "");
//			
//			for (SelectItem sic: lc_combo) {
//				System.out.println("---->>> step id:"+sic.getValue()+" nom:"+sic.getLabel());
//			}
//			
//			List<WStepDefAmpliada> lca = new ArrayList<WStepDefAmpliada>();
//			lca= new WStepDefBL().finderWStepDef(null,null); // trae todas las steps y sus proyectos relacionados
//
//			System.out.println("---------------- LISTA DE CONVOCATORIAS ( AMPLIADA ) -------------------------------------");
//			
//			for (WStepDefAmpliada ca: lca){
//				System.out.println(ca.toString());
//				for (ProyectoResumido pr: ca.getListaProyectos()) {
//					System.out.println("--->"+pr.toString());
//				}
//			}
//			
//			System.out.println("---------------- ----------------------------- -------------------------------------");
//			
//		}
		
		
		public void testErrorBorrarWStepDef() throws Exception {
		

			new WStepDefBL().delete( stepBL.getWStepDefByPK(iproc, 1000),1000) ;
			assertNull(stepBL.getWStepDefByPK(iproc,1001));
			
			

				
		}		
		
	
		
}