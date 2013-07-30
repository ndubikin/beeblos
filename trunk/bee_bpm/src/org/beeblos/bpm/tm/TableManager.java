package org.beeblos.bpm.tm;

//import org.hibernate.Hibernate;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.WProcessHeadManagedData;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;

public class TableManager {
	
	private static final String _SLASH = "/";

	static ResourceBundle rb = org.beeblos.bpm.core.util.Configuration.getConfigurationRepositoryResourceBundle();

	static public final String driver = rb.getString("bee_bpm_core.hibernate.connection.driver_class"); //"com.mysql.jdbc.Driver";
	static public final String connection = rb.getString("bee_bpm_core.hibernate.connection.url"); //"jdbc:mysql://localhost:3306/";
	static public final String schema = rb.getString("bee_bpm_core.hibernate.connection.default_catalog"); //"bee_bpm_dev";
	static public final String user = rb.getString("bee_bpm_core.hibernate.connection.username"); // "root";
	static public final String password = rb.getString("bee_bpm_core.hibernate.connection.password"); //"root";
	
	static public  Integer qtyRecords=-1;
	
	Connection conn;
	Statement stmt = null;
	DatabaseMetaData md;
	ResultSet rs;
	
	public TableManager() {
		
	}
	
	public void connect() throws ClassNotFoundException, SQLException {

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.out.println("class "+driver+" not found ...");
			throw e;
		}

		try {
			conn = DriverManager.getConnection(connection+_SLASH+schema, user, password);
		} catch (SQLException e) {
			System.out.println("can't connect with "+connection+" with schema:"+schema);
			throw e;
		}
		
	}
	
	public Integer checkTableExists(String tableName) throws ClassNotFoundException, SQLException {

		Integer qty=null;
				
		connect();
		
		String sql ="SELECT COUNT(id) AS COUNT FROM "+tableName;
		
		
		try {
			
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()) {
				qty = rs.getInt("COUNT");
			   System.out.println("The count is " + qty);
			}
			
			
		} catch (MySQLSyntaxErrorException e1) {
			
			System.out.println("Error MySQLSyntaxErrorException "+e1.getMessage()+" - "+e1.getCause());
			
			qty=-1;
			
		} catch (SQLException e) {
			System.out.println("Error SQLException "+e.getMessage()+" - "+e.getCause());
		}    finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		}
	
		this.qtyRecords=qty;
		return qty;
	}
	
	
	public void getTableColumns(String tableName) throws ClassNotFoundException {

		try {

			connect();
			
			md = conn.getMetaData();
			rs = md.getColumns(null, null, tableName, "%");
			
			System.out.println(rs==null);
			rs.last();
			System.out.println(rs.getRow());
			
			while(rs.next()) { 
			    String column = rs.getString("COLUMN_NAME");
			    String dataType= rs.getString("DATA_TYPE");
			    String typeName= rs.getString("TYPE_NAME");
			    String columnSize= rs.getString("COLUMN_SIZE");
			    String nullable= rs.getString("NULLABLE");
			    String isNullable= rs.getString("IS_NULLABLE");
			    
			    
			    String ordinalPosition = rs.getString("ORDINAL_POSITION");
			    System.out.println(column 
			    						+" type:"+ dataType
			    						+" typeName:"+ typeName
			    						+" colsize:"+ columnSize
			    						+" nullable:"+ nullable
			    						+" isNullable:"+ isNullable
			    						+" ordpos:"+ordinalPosition); 
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}    finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		}
	}
	

	public void createTable(String tableName, List<WProcessDataField> columns) throws ClassNotFoundException {

		String sql = buildCreateTableStatement(tableName,columns);

		try {

			connect();
			
			// check if table exists
			md = conn.getMetaData();
			rs = md.getColumns(null, null, tableName, "%");
			if (rs!=null && rs.last()) {
				if (rs.getRow()>0) {
					System.out.println("table "+tableName+" exists ... can't create it !");
					return;
				}
			}

			
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			System.out.println("table:"+tableName+" was created ...");
			
			md = conn.getMetaData();
			rs = md.getColumns(null, null, tableName, "%");


			
			while(rs.next()) { 
			    String column = rs.getString("COLUMN_NAME");
			    String dataType= rs.getString("DATA_TYPE");
			    String typeName= rs.getString("TYPE_NAME");
			    String columnSize= rs.getString("COLUMN_SIZE");
			    String nullable= rs.getString("NULLABLE");
			    String isNullable= rs.getString("IS_NULLABLE");
			    
			    
			    String ordinalPosition = rs.getString("ORDINAL_POSITION");
			    System.out.println(column 
			    						+" type:"+ dataType
			    						+" typeName:"+ typeName
			    						+" colsize:"+ columnSize
			    						+" nullable:"+ nullable
			    						+" isNullable:"+ isNullable
			    						+" ordpos:"+ordinalPosition); 
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}    finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		}
 
	}
	

	public void removeTable(String tableName) throws ClassNotFoundException {

		String sql = "DROP TABLE "+tableName+" " ; 

		try {

			connect();
			
			// check if table exists
			md = conn.getMetaData();
			rs = md.getColumns(null, null, tableName, "%");
			if (rs==null) {
				System.out.println("table "+tableName+" doesn't exists ... can't drop it !");
				return;
			}
			
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			System.out.println("table:"+tableName+" was dropped ...");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}    finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		}
 
	}

	private String buildCreateTableStatement(String tableName,List<WProcessDataField> columns) {

		String sql = "CREATE TABLE "+tableName+" ";
		sql +=" ( id INTEGER AUTO_INCREMENT NOT NULL, "; // mandatory field ...
		
		for (WProcessDataField column: columns) {
			sql+=	column.getName()+" "
					+ column.getDataType().getSqlTypeName()
					+ getColumnSize(column)+", ";
		}

//		"CREATE TABLE "+tableName+" " +
//		"(id INTEGER not NULL, " +
//		" first VARCHAR(255), " + 
//		" last VARCHAR(255), " + 
//		" age INTEGER, " + 
//		" PRIMARY KEY ( id ))"; 		
		
		
		sql +=" PRIMARY KEY ( id ))"; 

		System.out.println("sql:"+sql);
		
		return sql;
	}
	
	// return length formatted for sql syntax if corresponds to dataType ...
	private String getColumnSize(WProcessDataField column) {
		
		String colsize="";
		if (column.getDataType().getSqlType().equals(java.sql.Types.VARCHAR)) {
			if (column.getLength()>0) {
				colsize = "("+column.getLength()+")";
			} 
		}
		return colsize;
	}

}
