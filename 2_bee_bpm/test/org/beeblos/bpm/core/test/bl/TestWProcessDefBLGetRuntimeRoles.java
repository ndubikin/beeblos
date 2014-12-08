package org.beeblos.bpm.core.test.bl;


import java.util.List;

import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WRoleDef;
import org.junit.Test;



/**
 * verifica las queries programadas para recuperar la lista de roles ( o lista de id de roles) 
 * de los runtimeRoles de un proceso (processDefId)
 * 
 * En este caso en particular resolvimos con named queries (nes 20141208)
 * 
 * @author nestor
 *
 */
public class TestWProcessDefBLGetRuntimeRoles extends TestCase{
		
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
		public final void testGetRuntimeRoles() throws Exception {

			WProcessDef process = new WProcessDefBL().getWProcessDefByPK(1, 1000);
			
			List<Integer> roleIdList;
			
			roleIdList = processBL.getProcessRoles(process.getId(), 1000);
			
			if (roleIdList!=null && roleIdList.size()>0){
				for (Integer i: roleIdList) {
					System.out.println("role id:"+i);
				}
			} else {
				System.out.println("No runtimeRoles found...");
			}
			
			List<WRoleDef> roleList = processBL.getProcessRuntimeRoles(process.getId());
			
			if (roleList!=null && roleList.size()>0){
				for (WRoleDef role: roleList) {
					System.out.println("role id:"+role.toString());
				}
			} else {
				System.out.println("No runtimeRoles found...");
			}			
			
			System.out.println("Done");
			
		}
		
}