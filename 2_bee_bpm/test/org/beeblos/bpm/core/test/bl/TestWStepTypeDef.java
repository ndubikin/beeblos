package org.beeblos.bpm.core.test.bl;

// Generado 11-nov-2010 12:14:48 


import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WStepTypeDefBL;
import org.beeblos.bpm.core.model.WStepTypeDef;
import org.junit.Test;

public class TestWStepTypeDef extends TestCase {

	
	@Test
	public final void test() throws Exception {
		
		WStepTypeDef step = new WStepTypeDefBL().getWStepTypeDefByPK(22, 1000);
		
		System.out.println(step.toString());
		
			
	}
}