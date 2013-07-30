package org.beeblos.bpm.core.test.bl;
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
			String tableName="xxw_step_defxx";
			System.out.println("------------------------------------------------------------------");
			System.out.println("-------->  "+tableName);
			System.out.println("------------------------------------------------------------------");
			tm = new TableManager();
//			tm.getTableColumns(tableName);
//			tm.createTable(tableName, new List<WProcessDat>);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}