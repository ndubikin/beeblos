package org.beeblos.bpm.core.test.bl;


import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WRoleDefBL;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WUserDef;
import org.junit.Test;





public class TestWUserDefBL extends TestCase{
		
		static WUserDef user;
		static WUserDefBL userBL;
		static Integer idUser;


		
		protected void setUp() throws Exception {
			
			user = new WUserDef();
			userBL = new WUserDefBL();
			
		}
		
		protected void tearDown() throws Exception {
			
			user = null;
			userBL = null;
			
		}
		
		@Test
		
		public void testAgregarWUserDef() throws Exception {
			

			
//			WUserDefBL userBl = new WUserDefBL();
//			Integer idUser1 = userBl.add(new WUserDef( "juan ss", "jn", true, 1000, new Date()), 1000);
//			Integer idUser2 = userBl.add(new WUserDef( "maria ss", "mr", true, 1000, new Date()), 1000);
//			

			
			List<WUserDef> luser = new ArrayList<WUserDef>();
			List<WRoleDef> lrole = new ArrayList<WRoleDef>();
			
			lrole = new WRoleDefBL().getWRoleDefs(1000); // trae la lista de roles

			System.out.println("===================================================================================");
			System.out.println("       metodo: getWUserDefByRole");
			System.out.println("===================================================================================");
			
			for ( WRoleDef role: lrole) {

				luser = userBL.getWUserDefByRole(role.getId(), "name" );
				
				if ( luser.size()>0 ) {
					
					System.out.println("Rol: id:"+role.getId()+" name:"+role.getName());

					for (WUserDef user: luser) {
						System.out.println("  "+user.toString());
					}
					System.out.println("");	
					
				} else {
					
					System.out.println(" Rol: id:"+role.getId()+" name:"+role.getName()+" ROL SIN USUARIOS ASOCIADOS ...");
					System.out.println("");	
					
				}

			}
			System.out.println(" FIN DE LA LISTA DE ROLES Y USUARIOS ASOCIADOS ... ");

			
			List<Integer> luserid = new ArrayList<Integer>();
			
			System.out.println("===================================================================================");
			System.out.println("       metodo: getWUserDefByRole");
			System.out.println("===================================================================================");
			
			for ( WRoleDef role: lrole) {

				luserid = userBL.getWUserDefIdByRole(role.getId());

				if ( luserid.size()>0 ) {
				
					System.out.println(" Rol: id:"+role.getId()+" name:"+role.getName());

					for (Integer id: luserid) {
						System.out.println("  "+id);
					}
					System.out.println("");
					
				} else {
					System.out.println("Rol: id:"+role.getId()+" name:"+role.getName() + "ROL SIN USUARIOS ASOCIADOS ...");
					System.out.println("");
				}

			}
			
			System.out.println(" FIN DE LA LISTA DE ROLES Y USUARIOS ASOCIADOS ... ");
			
			
		}

		
		public void testErrorBorrarWUserDef() throws Exception {
		

			

				
		}		
		
	
		
}