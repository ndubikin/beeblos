package org.beeblos.bpm.core.util;

import java.util.ArrayList;
import java.util.List;

import org.beeblos.bpm.core.model.noper.DriverObject;

public class DriverObjectUtil {
	
	public static List<DriverObject> inicializeDriverArray(){
		
		List<DriverObject> driverList = new ArrayList<DriverObject>();
		
		driverList.add(new DriverObject("IBM DB2", "COM.ibm.db2.jdbc.app.DB2Driver"));
		driverList.add(new DriverObject("COM.ibm.db2.jdbc.app.DB2Driver", "COM.ibm.db2.jdbc.app.DB2Driver"));

		driverList.add(new DriverObject("JDBC-ODBC Bridge", "sun.jdbc.odbc.JdbcOdbcDriver"));
		driverList.add(new DriverObject("sun.jdbc.odbc.JdbcOdbcDriver", "sun.jdbc.odbc.JdbcOdbcDriver"));

		driverList.add(new DriverObject("Microsoft SQL Server", "weblogic.jdbc.mssqlserver4.Driver"));
		driverList.add(new DriverObject("weblogic.jdbc.mssqlserver4.Driver", "weblogic.jdbc.mssqlserver4.Driver"));

		driverList.add(new DriverObject("Oracle Thin", "oracle.jdbc.driver.OracleDriver"));
		driverList.add(new DriverObject("oracle.jdbc.driver.OracleDriver", "oracle.jdbc.driver.OracleDriver"));
		
		driverList.add(new DriverObject("PointBase Embedded Server", "com.pointbase.jdbc.jdbcUniversalDriver"));
		driverList.add(new DriverObject("com.pointbase.jdbc.jdbcUniversalDriver", "com.pointbase.jdbc.jdbcUniversalDriver"));
		
		driverList.add(new DriverObject("Cloudscape", "COM.cloudscape.core.JDBCDriver"));
		driverList.add(new DriverObject("COM.cloudscape.core.JDBCDriver", "COM.cloudscape.core.JDBCDriver"));
		
		driverList.add(new DriverObject("Cloudscape RMI", "RmiJdbc.RJDriver"));
		driverList.add(new DriverObject("RmiJdbc.RJDriver", "RmiJdbc.RJDriver"));
		
		driverList.add(new DriverObject("Firebird (JCA/JDBC Driver)", "org.firebirdsql.jdbc.FBDriver"));
		driverList.add(new DriverObject("org.firebirdsql.jdbc.FBDriver", "org.firebirdsql.jdbc.FBDriver"));
		
		driverList.add(new DriverObject("IDS Server", "ids.sql.IDSDriver"));
		driverList.add(new DriverObject("ids.sql.IDSDriver", "ids.sql.IDSDriver"));
		
		driverList.add(new DriverObject("Informix Dynamic Server", "com.informix.jdbc.IfxDriver"));
		driverList.add(new DriverObject("com.informix.jdbc.IfxDriver", "com.informix.jdbc.IfxDriver"));
		
		driverList.add(new DriverObject("InstantDB (v3.13 and earlier)", "jdbc.idbDriver"));
		driverList.add(new DriverObject("jdbc.idbDriver", "jdbc.idbDriver"));
		
		driverList.add(new DriverObject("InstantDB (v3.14 and later)", "org.enhydra.instantdb.jdbc.idbDriver"));
		driverList.add(new DriverObject("org.enhydra.instantdb.jdbc.idbDriver", "org.enhydra.instantdb.jdbc.idbDriver"));
		
		driverList.add(new DriverObject("Interbase (InterClient Driver)", "interbase.interclient.Driver"));
		driverList.add(new DriverObject("interbase.interclient.Driver", "interbase.interclient.Driver"));
		
		driverList.add(new DriverObject("Hypersonic SQL (v1.2 and earlier)", "hSql.hDriver"));
		driverList.add(new DriverObject("hSql.hDriver", "hSql.hDriver"));
		
		driverList.add(new DriverObject("Hypersonic SQL (v1.3 and later)", "org.hsql.jdbcDriver"));
		driverList.add(new DriverObject("org.hsql.jdbcDriver", "org.hsql.jdbcDriver"));
		
		driverList.add(new DriverObject("Microsoft SQL Server (JTurbo Driver)", "com.ashna.jturbo.driver.Driver"));
		driverList.add(new DriverObject("com.ashna.jturbo.driver.Driver", "com.ashna.jturbo.driver.Driver"));
		
		driverList.add(new DriverObject("Microsoft SQL Server (Sprinta Driver)", "com.inet.tds.TdsDriver"));
		driverList.add(new DriverObject("com.inet.tds.TdsDriver", "com.inet.tds.TdsDriver"));
		
		driverList.add(new DriverObject("Microsoft SQL Server 2000 (Microsoft Driver)", "com.microsoft.jdbc.sqlserver.SQLServerDriver"));
		driverList.add(new DriverObject("com.microsoft.jdbc.sqlserver.SQLServerDriver", "com.microsoft.jdbc.sqlserver.SQLServerDriver"));
		
		driverList.add(new DriverObject("MySQL (MM.MySQL Driver)", "org.gjt.mm.mysql.Driver"));
		driverList.add(new DriverObject("org.gjt.mm.mysql.Driver", "org.gjt.mm.mysql.Driver"));
		
		driverList.add(new DriverObject("Oracle OCI 8i", "oracle.jdbc.driver.OracleDriver"));
		driverList.add(new DriverObject("oracle.jdbc.driver.OracleDriver", "oracle.jdbc.driver.OracleDriver"));
		
		driverList.add(new DriverObject("Oracle OCI 9i", "oracle.jdbc.driver.OracleDriver"));
		driverList.add(new DriverObject("oracle.jdbc.driver.OracleDriver", "oracle.jdbc.driver.OracleDriver"));
		
		driverList.add(new DriverObject("PostgreSQL (v6.5 and earlier)", "postgresql.Driver"));
		driverList.add(new DriverObject("postgresql.Driver", "postgresql.Driver"));
		
		driverList.add(new DriverObject("PostgreSQL (v7.0 and later)", "org.postgresql.Driver"));
		driverList.add(new DriverObject("org.postgresql.Driver", "org.postgresql.Driver"));
		
		driverList.add(new DriverObject("Sybase (jConnect 4.2 and earlier)", "com.sybase.jdbc.SybDriver"));
		driverList.add(new DriverObject("", ""));
		
		driverList.add(new DriverObject("Sybase (jConnect 5.2)", "com.sybase.jdbc2.jdbc.SybDriver"));
		driverList.add(new DriverObject("com.sybase.jdbc2.jdbc.SybDriver", "com.sybase.jdbc2.jdbc.SybDriver"));
			
		return driverList;
	}
	
	public static List<DriverObject> searchDriverArray(String driverNameInput){
		
		List<DriverObject> result = new ArrayList<DriverObject>();
		
		for (DriverObject dObject : inicializeDriverArray()){
			
			if (dObject.getDriverOrDBValue().contains(driverNameInput)){
			
				result.add(dObject);
				
			}
		}

		return result;
		
	}

}
