package org.beeblos.bpm.core.test.bl;

 
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.beeblos.bpm.tm.TableManager;
import org.junit.Test;

public class TestJDBC extends TestCase {

	static TableManager tm;

	protected void setUp() throws Exception {

	}

	protected void tearDown() throws Exception {

		tm=null;

	}

	@Test
	public void test1() {

		try {
			String tableName="wmt_124";
			String schema = "bee_bpm_dev";
			System.out.println("------------------------------------------------------------------");
			System.out.println("-------->  "+tableName);
			System.out.println("------------------------------------------------------------------");
			tm = new TableManager();
			List<org.beeblos.bpm.tm.model.Column> lc = new ArrayList<org.beeblos.bpm.tm.model.Column>(); 
			lc = tm.getTableColumns(schema,tableName);
			
			for (org.beeblos.bpm.tm.model.Column c: lc) {
				System.out.println(c.toString());
			}
			
//			tm.createTable(tableName, new List<WProcessDat>);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}