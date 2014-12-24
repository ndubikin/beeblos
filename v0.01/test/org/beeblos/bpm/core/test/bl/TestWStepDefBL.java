package org.beeblos.bpm.core.test.bl;


import java.util.List;

import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WRoleDefBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WStepUser;
import org.beeblos.bpm.core.model.WUserDef;
import org.joda.time.DateTime;
import org.junit.Test;

import com.sp.common.util.StringPair;





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
		public final void testCrudStepRelatedRole() throws Exception {

			WStepDef step = new WStepDefBL().getWStepDefByPK(1, 1, 1000);
			
			WStepDef step2 = new WStepDefBL().addStepRelatedRole(step, 2, true, 1000);
			
			if (step.getRolesRelated() != null){
				for (WStepRole role : step.getRolesRelated()){
					if (role.getRole().getId().equals(2)){
						
						role.setAdmin(false);
						role.setIdObject(1000);

						break;
						
					}
				}
			}

			new WStepDefBL().update(step, 1, 1000);
			
			new WStepDefBL().deleteStepRelatedRole(step2, 2, 1000);
			
			System.out.println("Done");
			
		}

		@Test
		public final void testCrudStepRelatedUser() throws Exception {

			WStepDef step = new WStepDefBL().getWStepDefByPK(1, 1, 1000);
			
			WStepDef step2 = new WStepDefBL().addStepRelatedUser(step, 666, true, 1000);
			
			if (step.getUsersRelated() != null){
				for (WStepUser user : step.getUsersRelated()){
					if (user.getUser().getId().equals(666)){
						
						user.setAdmin(false);
						user.setIdObject(1000);

						break;
						
					}
				}
			}

			new WStepDefBL().update(step, 1, 1000);
			
			new WStepDefBL().deleteStepRelatedUser(step, 666, 1000);
			
			System.out.println("Done");
			
		}

		@Test
		public void testAgregarWStepDef() {
			
			try {
			WRoleDefBL roleBl = new WRoleDefBL();
			Integer idRol1 = roleBl.add(new WRoleDef("rol s1", "descrip rol 1", null, "1..2..probando..."), 1000);
			Integer idRol2 = roleBl.add(new WRoleDef("rol s2", "desc rol 2", 1, "pepe"), 1000	);
		
			WUserDefBL userBl = new WUserDefBL();
			Integer idUser1 = userBl.add(new WUserDef( "juan ss", "jn", true, 1000, new DateTime()), 1000);
			Integer idUser2 = userBl.add(new WUserDef( "maria ss", "mr", true, 1000, new DateTime()), 1000);
			

			step = new WStepDef(null,1,2,3,"ejecute este paso plis","sincomentarios ...",null,null,null);

			step.getResponse().add(new WStepResponseDef(null,"Respuesta1"));
//			step.getAssigned().add(new WStepAssignedDef("pepe","user"));
			
//			step.addRole( roleBl.getWRoleDefByPK(idRol1, 1000), false, 55, "tipo-objeto", 1000);
//			step.addRole(roleBl.getWRoleDefByPK(idRol2, 1000), true, 55, "tipo-objeto", 1000);
			
//			step.addUser(userBl.getWUserDefByPK(idUser1, 1000), false, 44, "hola", 1000);

			iproc = stepBL.add(step,1000);
			
			WStepDef sd = stepBL.getWStepDefByPK(iproc, null, 1000);
			
			System.out.println("---------------- ----------------------------- -------------------------------------");
			System.out.println("------->>> "+sd.toString());
			System.out.println("---------------- ----------------------------- -------------------------------------");
			
			assertEquals(iproc, stepBL.getWStepDefByPK(iproc, null, 1000).getId());
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			

			
			try {
				
				WStepDef sd1 = new WStepDef(); 
				sd1 = new WStepDefBL().getWStepDefByPK(iproc, null, 1000);

				for ( WStepRole wsr: sd1.getRolesRelated() ) {
					stepBL.deleteStepRole(sd1, wsr);			
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
//			WStepRole wsr = sd.getRolesRelated().iterator().next();
			
//			System.out.println("WStepRole a borrar role:"+wsr.getRole().getId()+" step:"+wsr.getStep().getId());
			
	
			

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
		
		
//		public void testErrorBorrarWStepDef() throws Exception {
//		
//
//			new WStepDefBL().delete( iproc, null, 1000) ;
//			assertNull(stepBL.getWStepDefByPK(iproc, null, 1001));
//			
//			
//
//				
//		}		
		
		@Test
		public void testSearchAdminProcessUserSteps() {
			
			try {
				
				List<StringPair> list = new WStepDefBL().getComboList(69, 1, 1000, false, null, null);
				
				System.out.println("CORRECT");
			
			} catch (WProcessDefException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}

		
}