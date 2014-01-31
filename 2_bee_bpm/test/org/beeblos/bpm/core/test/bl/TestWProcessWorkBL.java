package org.beeblos.bpm.core.test.bl;


import java.util.List;

import junit.framework.TestCase;

import org.beeblos.bpm.core.bl.WProcessWorkBL;
import org.beeblos.bpm.core.dao.WProcessWorkDao;
import org.beeblos.bpm.core.model.WProcessWork;
import org.junit.Test;




public class TestWProcessWorkBL extends TestCase{
		
		static WProcessWork processWork;
		static WProcessWorkBL processWorkBL;
		static Integer iproc;


		
		protected void setUp() throws Exception {
			
			processWork = new WProcessWork();
			processWorkBL = new WProcessWorkBL();
			
		}
		
		protected void tearDown() throws Exception {
			
			processWork = null;
			processWorkBL = null;
			
		}
		
		@Test
		
		public void testCriteriaWProcessWork() throws Exception {
			
			List<WProcessWork> wpwList = new WProcessWorkDao()
				.getWProcessWorkList(null, null, "", "ALIVE", 1000, false);		
			
			if (wpwList != null){
				System.out.println("elementos: " + wpwList.size());
			}
			
		}
		
}