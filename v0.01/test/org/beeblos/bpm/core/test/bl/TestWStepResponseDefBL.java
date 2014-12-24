package org.beeblos.bpm.core.test.bl;


import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WStepResponseDefBL;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.junit.Test;




public class TestWStepResponseDefBL extends TestCase{
		
		static WStepResponseDef response;
		static WStepResponseDefBL responseBL;
		static Integer iproc;


		
		protected void setUp() throws Exception {
			
			response = new WStepResponseDef();
			responseBL = new WStepResponseDefBL();
			
		}
		
		protected void tearDown() throws Exception {
			
			response = null;
			responseBL = null;
			
		}
		
		@Test
		
		public void testAgregarWStepResponseDef() throws Exception {
			
			response = new WStepResponseDef(1,null,"Respuesta1");

			iproc = responseBL.add(response,1000);
			
			assertEquals(iproc, responseBL.getWStepResponseDefByPK(iproc, 1000).getId());

		}
		
//		public void testConsultasWStepResponseDef() throws Exception{
//			
//
//			List<SelectItem> lc_combo = new ArrayList<SelectItem>();
//			lc_combo = new WStepResponseDefBL().obtenerWStepResponseDefsParaCombo("Seleccionar ...", "");
//			
//			for (SelectItem sic: lc_combo) {
//				System.out.println("---->>> response id:"+sic.getValue()+" nom:"+sic.getLabel());
//			}
//			
//			List<WStepResponseDefAmpliada> lca = new ArrayList<WStepResponseDefAmpliada>();
//			lca= new WStepResponseDefBL().finderWStepResponseDef(null,null); // trae todas las responses y sus proyectos relacionados
//
//			System.out.println("---------------- LISTA DE CONVOCATORIAS ( AMPLIADA ) -------------------------------------");
//			
//			for (WStepResponseDefAmpliada ca: lca){
//				System.out.println(ca.toString());
//				for (ProyectoResumido pr: ca.getListaProyectos()) {
//					System.out.println("--->"+pr.toString());
//				}
//			}
//			
//			System.out.println("---------------- ----------------------------- -------------------------------------");
//			
//		}
		
		
		public void testErrorBorrarWStepResponseDef() throws Exception {
		

			new WStepResponseDefBL().delete( responseBL.getWStepResponseDefByPK(iproc, 1000),1000) ;
			assertNull(responseBL.getWStepResponseDefByPK(iproc,100));
			
			

				
		}		

}