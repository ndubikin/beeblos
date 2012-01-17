import java.util.Date;

import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WRoleDefBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WUserDefBL;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WUserDef;
import org.junit.Test;

public class TestWStepDefBL extends TestCase {

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
	public void testAgregarWStepDef() {

		try {
			WRoleDefBL roleBl = new WRoleDefBL();
			Integer idRol1 = roleBl.add(roleBl.getWRoleDefByPK(1, 1000), 1000);
			Integer idRol2 = roleBl.add(roleBl.getWRoleDefByPK(2, 1000), 1000);
			Integer idRol3 = roleBl.add(roleBl.getWRoleDefByPK(3, 1000), 1000);
			Integer idRol4 = roleBl.add(roleBl.getWRoleDefByPK(4, 1000), 1000);

			WUserDefBL userBl = new WUserDefBL();
			Integer idUser1 = userBl.add(userBl.getWUserDefByPK(1000, 1000), 1000);
			Integer idUser2 = userBl.add(userBl.getWUserDefByPK(1001, 1000), 1000);

			step = new WStepDef(null, "paso 1", 2, 3, "ejecute este paso plis",
					"sincomentarios ...", null, null, null);
			step.getResponse().add(new WStepResponseDef(null, "Respuesta1"));
			// step.getAssigned().add(new WStepAssignedDef("pepe","user"));

			step.addRole(roleBl.getWRoleDefByPK(idRol1, 1000), false, 55,
					"tipo-objeto", 1000);
			step.addRole(roleBl.getWRoleDefByPK(idRol2, 1000), true, 55,
					"tipo-objeto", 1000);

			step.addUser(userBl.getWUserDefByPK(idUser1, 1000), false, 44,
					"hola", 1000);

			iproc = stepBL.add(step, 1000);

			WStepDef sd = stepBL.getWStepDefByPK(iproc, 1000);

			System.out
					.println("---------------- ----------------------------- -------------------------------------");
			System.out.println("------->>> " + sd.toString());
			System.out
					.println("---------------- ----------------------------- -------------------------------------");

			assertEquals(iproc, stepBL.getWStepDefByPK(iproc, 1000).getId());

			assertEquals(2, stepBL.getWStepDefByPK(iproc, 1000)
					.getRolesRelated().size()); // tiene q tener 2 WStepRole
			assertEquals(1, stepBL.getWStepDefByPK(iproc, 1000)
					.getUsersRelated().size()); // tiene q tener 1 WStepUser

			WStepDef sd1 = new WStepDefBL().getWStepDefByPK(iproc, 1000);

			sd1.addRole(roleBl.getWRoleDefByPK(idRol3, 1000), false, 55,
					"tipo-objeto", 1000);

			new WStepDefBL().update(sd1, 1000);

			sd1 = stepBL.getWStepDefByPK(iproc, 1000);
			assertEquals(3, sd1.getRolesRelated().size()); // tiene q tener 3
															// WStepRole

			WStepRole wsr = sd1.getRolesRelated().iterator().next();
			stepBL.deleteStepRole(sd1, wsr);
			sd1 = stepBL.getWStepDefByPK(iproc, 1000);
			assertEquals(2, sd1.getRolesRelated().size()); // vuelve a tener 2
															// WStepRole ...

			while (sd1.getRolesRelated().iterator().hasNext()){
				sd1.getRolesRelated().remove(sd1.getRolesRelated().iterator().next());
			}
			
			while (sd1.getUsersRelated().iterator().hasNext()){
				sd1.getUsersRelated().remove(sd1.getUsersRelated().iterator().next());
			}
			
			new WStepDefBL().update(sd1, 1000);

			assertEquals(0, stepBL.getWStepDefByPK(iproc, 1000)
					.getRolesRelated().size()); // ahora debe tener 0 ...
			assertEquals(0, stepBL.getWStepDefByPK(iproc, 1000)
					.getUsersRelated().size()); // ahora debe tener 0 ...

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void testErrorBorrarWStepDef() throws Exception {

		new WStepDefBL().delete(stepBL.getWStepDefByPK(iproc, 1000), 1000);
		assertNull(stepBL.getWStepDefByPK(iproc, 1001));

	}

}