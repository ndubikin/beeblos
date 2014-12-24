package org.beeblos.bpm.core.test.bl;


import static org.beeblos.bpm.core.util.Constants.ALIVE;
import static org.beeblos.bpm.core.util.Constants.PROCESSED;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WProcessWorkBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepSequenceDefBL;
import org.beeblos.bpm.core.bl.WStepWorkBL;
import org.beeblos.bpm.core.dao.WTimeUnitDao;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WStepWorkAssignment;
import org.beeblos.bpm.core.model.WTimeUnit;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.noper.WStepWorkCheckObject;
import org.junit.Test;





public class TestWStepWorkCheckObject extends TestCase{
		
		static WStepWorkCheckObject swco;
		static WStepWorkBL stepwBL;
		static Integer iStepW;


		protected void setUp() throws Exception {

			stepwBL = new WStepWorkBL();

		}
		
		protected void tearDown() throws Exception {

			stepwBL = null;
	
		}
		
		@Test
		
		public void testReadWStepWorkCheckObject() throws Exception {
			
			
		

		}

		
}