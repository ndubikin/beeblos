package org.beeblos.bpm.core.test.bl;


import java.util.Date;

import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WRoleDefBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WUserDef;
import org.junit.Test;




public class TestWProcessDefBL extends TestCase{
		
		static WProcessDef process;
		static WProcessDefBL processBL;
		static Integer iproc;


		
		protected void setUp() throws Exception {
			
			process = new WProcessDef();
			processBL = new WProcessDefBL();
			
		}
		
		protected void tearDown() throws Exception {
			
			process = null;
			processBL = null;
			
		}
		
		@Test
		
		public void testAgregarWProcessDef() throws Exception {
			
			WRoleDefBL roleBl = new WRoleDefBL();
			Integer idRol1 = roleBl.add(new WRoleDef("rol 1", "descrip rol 1", null, "1..2..probando..."), 1000);
			Integer idRol2 = roleBl.add(new WRoleDef("rol 2", "desc rol 2", 1, "pepe"), 1000	);
			
			WUserDefBL userBl = new WUserDefBL();
			Integer idUser1 = userBl.add(new WUserDef( "juan", "jn", true, 1000, new Date()), 1000);
			Integer idUser2 = userBl.add(new WUserDef( "maria", "mr", true, 1000, new Date()), 1000);
			
			WStepDefBL stepBL = new WStepDefBL();
			Integer idStep = stepBL.add(new WStepDef(null,2,3,"ejecute este paso plis","sincomentarios ...",null,null,null),1000);
			
			process.setBeginStep(stepBL.getWStepDefByPK(idStep, process.getProcess().getId(), 1000));
			process.setComments("mis comentarios");


			process.setName("Mi primero worflo");
			
			iproc = processBL.add(process,1000);
			
			Integer istep = process.getBeginStep().getId();
			
			assertEquals(iproc, processBL.getWProcessDefByPK(iproc, 1000).getId());
			
			process.addRole( roleBl.getWRoleDefByPK(idRol1, 1000), false, 55, "tipo-ojeto", 1000);
			process.addRole(roleBl.getWRoleDefByPK(idRol2, 1000), true, 55, "tipo-ojeto", 1000);
			
			process.addUser(userBl.getWUserDefByPK(idUser1, 1000), false, 44, "hola", 1000);

			processBL.update(process, 1000); // persist relation tables ( roles y users )
			
			// pruebo a borrar el paso de la tabla de definición de pasos para verificar que no lo pueda
			// borrar porque si no queda mal la referencia ... ( restriccion por fk en tabla WStepDef )
			
			//WStepDefBL stepBL = new WStepDefBL();
			WStepDef step = stepBL.getWStepDefByPK(istep, process.getProcess().getId(), 1000);
			try {
				stepBL.delete(step.getId(), process.getProcess().getId(), 1001);
				assertNotNull(step);
			} catch (WStepDefException e) {
				System.out.println("Sale correctamente por WStepDefException porque no debe dejar borrar el paso si está el proceso apuntando a él ...");
			}
			

			// prints process object to know if they came with all fields loaded ...
			WProcessDef readedProcess = new WProcessDef(); 
			readedProcess = processBL.getWProcessDefByPK(process.getId(), 1000);
			System.out.println(readedProcess.toString());
			
			
			// ahora si si borro primero el proceso, luego podré borrar el paso que di de alta
			// como paso de comienzo ...
			new WProcessDefBL().delete( iproc, false,1000) ; // con las cascadas esto borra el proceso y las 2 tablas de relacion asociadas ...
			assertNull(processBL.getWProcessDefByPK(iproc,1001));
			
			stepBL.delete(step.getId(), readedProcess.getProcess().getId(), 1001);
			assertNull(stepBL.getWStepDefByPK(istep, readedProcess.getProcess().getId(), 1000));
			
			roleBl.delete(idRol1, 1000);
			roleBl.delete(idRol2, 1000);
			
			userBl.delete(idUser1, 1000);
			userBl.delete(idUser2, 1000);
		
		}
		
//		public void testConsultasWProcessDef() throws Exception{
//			
//
//			List<SelectItem> lc_combo = new ArrayList<SelectItem>();
//			lc_combo = new WProcessDefBL().obtenerWProcessDefsParaCombo("Seleccionar ...", "");
//			
//			for (SelectItem sic: lc_combo) {
//				System.out.println("---->>> process id:"+sic.getValue()+" nom:"+sic.getLabel());
//			}
//			
//			List<WProcessDefAmpliada> lca = new ArrayList<WProcessDefAmpliada>();
//			lca= new WProcessDefBL().finderWProcessDef(null,null); // trae todas las processs y sus proyectos relacionados
//
//			System.out.println("---------------- LISTA DE CONVOCATORIAS ( AMPLIADA ) -------------------------------------");
//			
//			for (WProcessDefAmpliada ca: lca){
//				System.out.println(ca.toString());
//				for (ProyectoResumido pr: ca.getListaProyectos()) {
//					System.out.println("--->"+pr.toString());
//				}
//			}
//			
//			System.out.println("---------------- ----------------------------- -------------------------------------");
//			
//		}
		
		
		public void testErrorBorrarWProcessDef() throws Exception {
		
			
			

				
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