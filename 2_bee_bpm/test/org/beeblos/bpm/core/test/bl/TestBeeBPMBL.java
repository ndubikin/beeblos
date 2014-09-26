package org.beeblos.bpm.core.test.bl;

import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.BeeBPMBL;
import org.beeblos.bpm.core.bl.WProcessWorkBL;
import org.beeblos.bpm.core.model.WProcessWork;
import org.junit.Test;

import com.sp.common.util.HibernateUtil;

public class TestBeeBPMBL extends TestCase {

	static BeeBPMBL beeBPMBL;
	static Integer iproc;

	protected void setUp() throws Exception {

		beeBPMBL = new BeeBPMBL();
		
		// limpia hibernate session que para test queda pillada ...
		new HibernateUtil(true);

	}

	protected void tearDown() throws Exception {

		beeBPMBL = null;

	}

	@Test
	public void testLanzarWorkflow() throws Exception {

		// nota: debemos conocer el id del wProcessDef a lanzar ...

		iproc = beeBPMBL.injector(1, // idProcess,
				null, // idStep,
				19, // idObject,
				"com.softpoint.bdc.model.OrdenPago", // idObjectType,
				"orden de pago de test ...", // objReference,
				"", // objComments,
				1000); // userId);

		System.out.println("Proceso dado de alta ...:" + iproc);

		WProcessWorkBL wpwBL = new WProcessWorkBL();
		WProcessWork process = wpwBL.getWProcessWorkByPK(iproc, 1000); 
		wpwBL.delete(process, 1000);
	}

	@Test
	public void testBorrarInstanciaCreada() throws Exception {
		


	}

}