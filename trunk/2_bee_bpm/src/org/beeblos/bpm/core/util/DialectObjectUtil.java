package org.beeblos.bpm.core.util;

import java.util.ArrayList;
import java.util.List;

import org.beeblos.bpm.core.model.noper.DialectObject;

public class DialectObjectUtil {
	
	public static List<DialectObject> inicializeDialectArray(){
		
		List<DialectObject> dialectList = new ArrayList<DialectObject>();
		
		dialectList.add(new DialectObject("DB2", "org.hibernate.dialect.DB2Dialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.DB2Dialect", "org.hibernate.dialect.DB2Dialect"));

		dialectList.add(new DialectObject("DB2 AS/400", "org.hibernate.dialect.DB2400Dialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.DB2400Dialect", "org.hibernate.dialect.DB2400Dialect"));

		dialectList.add(new DialectObject("DB2 OS390", "org.hibernate.dialect.DB2390Dialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.DB2390Dialect", "org.hibernate.dialect.DB2390Dialect"));

		dialectList.add(new DialectObject("PostgreSQL", "org.hibernate.dialect.PostgreSQLDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.PostgreSQLDialect", "org.hibernate.dialect.PostgreSQLDialect"));

		dialectList.add(new DialectObject("MySQL", "org.hibernate.dialect.MySQLDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.MySQLDialect", "org.hibernate.dialect.MySQLDialect"));

		dialectList.add(new DialectObject("MySQL con InnoDB", "org.hibernate.dialect.MySQLInnoDBDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.MySQLInnoDBDialect", "org.hibernate.dialect.MySQLInnoDBDialect"));

		dialectList.add(new DialectObject("MySQL con MyISAM", "org.hibernate.dialect.MySQLMyISAMDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.MySQLMyISAMDialect", "org.hibernate.dialect.MySQLMyISAMDialect"));

		dialectList.add(new DialectObject("Oracle (cualquier versión)", "org.hibernate.dialect.OracleDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.OracleDialect", "org.hibernate.dialect.OracleDialect"));

		dialectList.add(new DialectObject("Oracle 9i", "org.hibernate.dialect.Oracle9iDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.Oracle9iDialect", "org.hibernate.dialect.Oracle9iDialect"));

		dialectList.add(new DialectObject("Oracle 10g", "org.hibernate.dialect.Oracle10gDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.Oracle10gDialect", "org.hibernate.dialect.Oracle10gDialect"));

		dialectList.add(new DialectObject("Sybase", "org.hibernate.dialect.SybaseDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.SybaseDialect", "org.hibernate.dialect.SybaseDialect"));

		dialectList.add(new DialectObject("Sybase Anywhere", "org.hibernate.dialect.SybaseAnywhereDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.SybaseAnywhereDialect", "org.hibernate.dialect.SybaseAnywhereDialect"));

		dialectList.add(new DialectObject("Microsoft SQL Server", "org.hibernate.dialect.SQLServerDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.SQLServerDialect", "org.hibernate.dialect.SQLServerDialect"));

		dialectList.add(new DialectObject("SAP DB", "org.hibernate.dialect.SAPDBDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.SAPDBDialect", "org.hibernate.dialect.SAPDBDialect"));

		dialectList.add(new DialectObject("Informix", "org.hibernate.dialect.InformixDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.InformixDialect", "org.hibernate.dialect.InformixDialect"));

		dialectList.add(new DialectObject("HypersonicSQL", "org.hibernate.dialect.HSQLDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.HSQLDialect", "org.hibernate.dialect.HSQLDialect"));

		dialectList.add(new DialectObject("Ingres", "org.hibernate.dialect.IngresDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.IngresDialect", "org.hibernate.dialect.IngresDialect"));

		dialectList.add(new DialectObject("Progress", "org.hibernate.dialect.ProgressDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.ProgressDialect", "org.hibernate.dialect.ProgressDialect"));

		dialectList.add(new DialectObject("Mckoi SQL", "org.hibernate.dialect.MckoiDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.MckoiDialect", "org.hibernate.dialect.MckoiDialect"));

		dialectList.add(new DialectObject("Interbase", "org.hibernate.dialect.InterbaseDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.InterbaseDialect", "org.hibernate.dialect.InterbaseDialect"));

		dialectList.add(new DialectObject("Pointbase", "org.hibernate.dialect.PointbaseDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.PointbaseDialect", "org.hibernate.dialect.PointbaseDialect"));

		dialectList.add(new DialectObject("FrontBase", "org.hibernate.dialect.FrontbaseDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.FrontbaseDialect", "org.hibernate.dialect.FrontbaseDialect"));

		dialectList.add(new DialectObject("Firebird", "org.hibernate.dialect.FirebirdDialect"));
		dialectList.add(new DialectObject("org.hibernate.dialect.FirebirdDialect", "org.hibernate.dialect.FirebirdDialect"));

		
		return dialectList;
	}
	
	public static List<DialectObject> searchDialectArray(String dialectNameInput){
		
		List<DialectObject> result = new ArrayList<DialectObject>();
		
		for (DialectObject dObject : inicializeDialectArray()){
			
			if (dObject.getDialectValue().contains(dialectNameInput)){
			
				result.add(dObject);
				
			}
		}

		return result;
		
	}

}
