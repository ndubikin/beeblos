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

			iproc = responseBL.add(response,"juan");
			
			assertEquals(iproc, responseBL.getWStepResponseDefByPK(iproc, "juan").getId());

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
		

			new WStepResponseDefBL().delete( responseBL.getWStepResponseDefByPK(iproc, "juan"),"juan") ;
			assertNull(responseBL.getWStepResponseDefByPK(iproc,"Juancito"));
			
			

				
		}		
		
	
	
//		public void testAgregarTipoTerritorioInferior() throws Exception {
//			
//			PaisDao paisDao = new PaisDao();
//			Pais pais = paisDao.obtenerPaisPorPK("UY");
//			tipoTerritorio.setTipoTerritorioPais(pais);
//			
//			tipoTerritorio.setTipoTerritorioNombre("Localidad");
//			
//			// Asignacion explicita del nivel
//			tipoTerritorio.setTipoTerritorioNivel(1);
//
//			idTipoTerritorio = tipoTerritorioBL.agregar(tipoTerritorio);
//			
//			TipoTerritorio tipoTerritorioGuardado = tipoTerritorioBL.obtenerTipoTerritorioPorPK(idTipoTerritorio);
//			
//			assertEquals("Localidad", tipoTerritorioGuardado.getTipoTerritorioNombre());
//			//assertEquals("Uruguay", tipoTerritorioGuardado.getTipoTerritorioPais().getPaisNombre());
//			//assertEquals("Departamento", tipoTerritorioGuardado.getTipoTerritorioSuperior().getTipoTerritorioNombre());
//			
//		}
//		
//		public void testActualizarTipoTerritorio() throws Exception {
//			
//			List<TipoTerritorio> listaTiposTerritorio = tipoTerritorioBL.obtenerJerarquiaTerritorialPais("UY");
//			tipoTerritorio = listaTiposTerritorio.get(0);
//			
//			tipoTerritorio.setTipoTerritorioNombre("Departamento");
//			tipoTerritorio.setTipoTerritorioComentario("Este tipo esta Modificado");
//			
//			tipoTerritorioBL.actualizar(tipoTerritorio);
//			
//			TipoTerritorio tipoTerritorioGuardado = tipoTerritorioBL.obtenerTipoTerritorioPorNombreYPais(tipoTerritorio.getTipoTerritorioNombre(), "UY");
//			
//			assertEquals("Departamento", tipoTerritorioGuardado.getTipoTerritorioNombre());
//					
//		}
//		
//		
//		public void testErrorAgregarTipoTerritorio() throws Exception{
//			
//			PaisDao paisDao = new PaisDao();
//			Pais pais = paisDao.obtenerPaisPorPK("UY");
//			tipoTerritorio.setTipoTerritorioPais(pais);
//			
//			tipoTerritorio.setTipoTerritorioNombre("TipoErroneo");
//			
//			// Asignacion explicita del nivel
//			tipoTerritorio.setTipoTerritorioNivel(3);
//
//			try {
//				tipoTerritorioBL.agregar(tipoTerritorio);
//			} catch (TipoTerritorioException e) {
//				return;
//			}
//			
//		}
//		

//		
//	
//		public void testBorrarTipoTerritorio() throws Exception {
//			
//			Integer nNivel;
//			
//			List<TipoTerritorio> tipos = tipoTerritorioBL.obtenerJerarquiaTerritorialPais("UY");
//		
//			assertEquals(2, tipos.size());
//
//			nNivel = tipos.size()-1;
//			
//			TipoTerritorio tt = tipos.get(nNivel);
//			tipoTerritorioBL.borrar(tt);
//			
//			assertNull(tipoTerritorioBL.obtenerTipoTerritorioPorPK(tt.getIdTipoTerritorio()));
//
//			nNivel--;
//			
//			tt = tipos.get(nNivel);
//			tipoTerritorioBL.borrar(tt);
//
//			assertNull(tipoTerritorioBL.obtenerTipoTerritorioPorPK(tt.getIdTipoTerritorio()));
//					
//			
//		}
		
}