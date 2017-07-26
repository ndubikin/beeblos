package org.beeblos.bpm.core.test.bl;

import java.text.SimpleDateFormat;

import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.bl.WStepDefBL;
import org.beeblos.bpm.core.bl.WStepSequenceDefBL;
import org.beeblos.bpm.core.bl.WTrackWorkBL;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WTrackWork;
import org.joda.time.DateTime;
import org.junit.Test;


public class TestWTrackWorkBL extends TestCase {

	static WTrackWork trackw;
	static WTrackWorkBL trackwBL;
	static Integer iTrackW;

	// para las precondiciones:
	static WStepSequenceDef route;
	static WStepSequenceDefBL routeBL;
	static Integer iroute;

	static WProcessDef process;
	static WProcessDefBL processBL;
	static Integer iproc;

	static Integer version1 = 1;

	static WStepDefBL stepBL;

	protected void setUp() throws Exception {

		trackw = new WTrackWork();
		trackwBL = new WTrackWorkBL();

		// para las precondiciones
		route = new WStepSequenceDef();
		routeBL = new WStepSequenceDefBL();

		process = new WProcessDef();
		processBL = new WProcessDefBL();

		stepBL = new WStepDefBL();

	}

	protected void tearDown() throws Exception {

		trackw = null;
		trackwBL = null;

		// para las precondiciones
		route = null;
		routeBL = null;

		process = null;
		processBL = null;

		stepBL = null;

	}

	@Test
	public void testAgregarWTrackWork() throws Exception {

		// precondiciones: doy de alta un proceso, un mapa y utilizo los pasos
		// 10,20,24,40,50,90
		// doy de alta un proceso para que la ruta se pueda referir a él ...
		process.setBeginStep(stepBL.getWStepDefByPK(10, null, 1001));
		process.setComments("mis comentarios");

		process.setName("Mi proceso");

		iproc = processBL.add(process, 1000);

		WStepDef paso10 = stepBL.getWStepDefByPK(10, null, 1001);
		WStepDef paso20 = stepBL.getWStepDefByPK(20, null, 1001);
		WStepDef paso24 = stepBL.getWStepDefByPK(24, null, 1001);
		WStepDef paso30 = stepBL.getWStepDefByPK(30, null, 1001);
		WStepDef paso40 = stepBL.getWStepDefByPK(40, null, 1001);
		WStepDef paso50 = stepBL.getWStepDefByPK(50, null, 1001);
		WStepDef paso90 = stepBL.getWStepDefByPK(90, null, 1001);

		// cargo la ruta
		// presupongo que existen los step con id 800 y 20
//		WStepSequenceDef route1 = new WStepSequenceDef(process, version1,
//				paso10, paso20, true, true, null);
//
//		WStepSequenceDef route2 = new WStepSequenceDef(process, version1,
//				paso20, paso24, true, false, null);
//		WStepSequenceDef route3 = new WStepSequenceDef(process, version1,
//				paso20, paso30, true, true, null);
//
//		WStepSequenceDef route4 = new WStepSequenceDef(process, version1,
//				paso24, paso40, true, true, null);
//		WStepSequenceDef route5 = new WStepSequenceDef(process, version1,
//				paso30, paso50, true, false, null);
//
//		WStepSequenceDef route6 = new WStepSequenceDef(process, version1,
//				paso40, null, true, false, null);
//		WStepSequenceDef route7 = new WStepSequenceDef(process, version1,
//				paso40, paso90, true, false, null);
//		WStepSequenceDef route8 = new WStepSequenceDef(process, version1,
//				paso50, paso90, true, true, null);

//		iroute = routeBL.add(route3, 1000);
//		iroute = routeBL.add(route7, 1000);
//		iroute = routeBL.add(route5, 1000);
//		iroute = routeBL.add(route8, 1000);
//		iroute = routeBL.add(route4, 1000);
//		iroute = routeBL.add(route1, 1000);
//		iroute = routeBL.add(route6, 1000);
//		iroute = routeBL.add(route2, 1000);

		// hago unas verificaciones mínimas para asegurarme que mas o menos
		// quedaron bien
		// las precondiciones:

		assertEquals(2, routeBL.getStepSequenceList(iproc,  40, 1001)
				.size());
		assertEquals(2, routeBL.getStepSequenceList(iproc,  20, 1001)
				.size());
		assertEquals(1, routeBL.getStepSequenceList(iproc,  10, 1001)
				.size());

		DateTime now = new DateTime();
		SimpleDateFormat fmtDateTime = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");

		WTrackWork trackWork = new WTrackWork();

		trackWork.setInsertUser(1003);
		trackWork.setInsertDate(now);
		trackWork.setIdObject(111);
		trackWork.setIdObjectType("com.softpoint.bdc.model.Proyecto.class");
		trackWork.setPreviousStep(paso10);
		trackWork.setCurrentStep(paso20);
		trackWork.setVersion(1);
		trackWork.setProcess(process);
		trackWork.setUserNotes("Notas de insertado");

		iTrackW = trackwBL.add(trackWork, 1003);

		// lo leo
		trackw = trackwBL.getWTrackWorkByPK(iTrackW, 1001);

		// verifico campos cargados
		assertEquals(iTrackW, trackw.getId());

		assertEquals(paso10, trackw.getPreviousStep());
		assertEquals(paso20, trackw.getCurrentStep());

		assertEquals(new Integer(111), trackw.getIdObject());
		assertEquals("com.softpoint.bdc.model.Proyecto.class",
				trackw.getIdObjectType());

		assertEquals(fmtDateTime.format(now),
				fmtDateTime.format(trackw.getInsertDate()));

		assertEquals(new Integer(1003), trackw.getInsertUser());

		assertFalse(trackw.isAdminProcess());

		trackwBL.update(trackw, 1000);

		WTrackWork sw = trackwBL.getWTrackWorkByPK(trackw.getId(), 1000);

		// tests para los getList
		assertEquals(
				1,
				trackwBL.getTrackListByIdObject(111,
						"com.softpoint.bdc.model.Proyecto.class", 1000).size());
		assertEquals(
				trackw,
				trackwBL.getTrackListByIdObject(111,
						"com.softpoint.bdc.model.Proyecto.class", 1000).get(0));
		assertEquals(1, trackwBL.getTrackListByProcess(process.getId()).size());
		assertEquals(trackw, trackwBL.getTrackListByProcess(process.getId())
				.get(0));
		assertEquals(1, trackwBL.getTrackListByCurrentStep(paso20.getId())
				.size());
		assertEquals(trackw, trackwBL.getTrackListByCurrentStep(paso20.getId())
				.get(0));
		assertEquals(1, trackwBL.getTrackListByInsertUser(1003).size());
		assertEquals(trackw, trackwBL.getTrackListByInsertUser(1003).get(0));
		assertEquals(1, trackwBL.getTrackListByUserNotes("Notas de insertado")
				.size());
		
		//Solo ponemos una parte de las notas para ver si hace bien el LIKE %notes%
		assertEquals(trackw,
				trackwBL.getTrackListByUserNotes("de").get(0));

		// borro todo antes de irme ...
		trackwBL.delete(trackw, 1000);
		assertNull(trackwBL.getWTrackWorkByPK(iTrackW, 1000));

	}

}