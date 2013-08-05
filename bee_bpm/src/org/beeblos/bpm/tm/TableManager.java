package org.beeblos.bpm.tm;

//import org.hibernate.Hibernate;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.tm.exception.TableManagerException;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import com.sp.common.model.ManagedDataField;

public class TableManager {
	
	private static final Log logger = LogFactory
			.getLog(TableManager.class.getName());
	
	private static final String INSERT = "INSERT";
	private static final String UPDATE = "UPDATE";

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
	
	public Integer process(ManagedData managedData) throws TableManagerException {
		
		if (managedData==null) throw new TableManagerException("can't process null managedData!");
		if (managedData.getCurrentWorkId()==null 
				|| managedData.getCurrentWorkId()==0) 
				throw new TableManagerException("can't process managedData: current work is not defined (currentWorkId:"
													+(managedData.getCurrentWorkId()==null?"null":"0"));
		if (managedData.getManagedTableConfiguration().getName()==null 
				|| "".equals(managedData.getManagedTableConfiguration().getName()))  
			throw new TableManagerException("can't process managedData: managed table is not defined (managedTableName:"
					+(managedData.getManagedTableConfiguration().getName()==null?"null":"-emtpy string-"));

		if (managedData.getOperation().equals(INSERT)) {
			try {
				return insert(managedData);
			} catch (ClassNotFoundException e) {
				throw new TableManagerException("Can't insert managed record: ClassNotFoundException:"+e.getMessage()+" - "+e.getCause());
			} catch (SQLException e) {
				throw new TableManagerException("Can't insert managed record: SQLException:"+e.getMessage()+" - "+e.getCause());
			}
		}
		
		return null;
	}
	
	public Integer loadRecord(ManagedData managedData) throws TableManagerException {
		
		if (managedData==null) throw new TableManagerException("can't process null managedData!");
		if (managedData.getManagedTableConfiguration()==null) throw new TableManagerException("can't process null managedTableConfiguration!!!");
		if (managedData.getCurrentWorkId()==null 
				|| managedData.getCurrentWorkId()==0) 
				throw new TableManagerException("can't process managedData: current work is not defined (currentWorkId:"
													+(managedData.getCurrentWorkId()==null?"null":"0"));
		if (managedData.getManagedTableConfiguration().getName()==null 
				|| "".equals(managedData.getManagedTableConfiguration().getName()))  
			throw new TableManagerException("can't process managedData: managed table is not defined (managedTableName:"
					+(managedData.getManagedTableConfiguration().getName()==null?"null":"-emtpy string-"));


		try {
			return load(managedData);
		} catch (ClassNotFoundException e) {
			throw new TableManagerException("Can't insert managed record: ClassNotFoundException:"+e.getMessage()+" - "+e.getCause());
		} catch (SQLException e) {
			throw new TableManagerException("Can't insert managed record: SQLException:"+e.getMessage()+" - "+e.getCause());
		}

	}
	
	// persists (update or insert) a record in managed table
	public Integer persist(ManagedData managedData) throws TableManagerException {
		
		if (managedData==null) throw new TableManagerException("can't process null managedData!");
		if (managedData.getCurrentWorkId()==null 
				|| managedData.getCurrentWorkId()==0) 
				throw new TableManagerException("can't process managedData: current work is not defined (currentWorkId:"
													+(managedData.getCurrentWorkId()==null?"null":"0"));
		if (managedData.getManagedTableConfiguration().getName()==null 
				|| "".equals(managedData.getManagedTableConfiguration().getName()))  
			throw new TableManagerException("can't process managedData: managed table is not defined (managedTableName:"
					+(managedData.getManagedTableConfiguration().getName()==null?"null":"-emtpy string-"));


		try {
			if (managedData.getPk()!=null && managedData.getPk()!=0) {
				managedData.setOperation(UPDATE);
				return update(managedData);
			} else {
				managedData.setOperation(INSERT);
				return insert(managedData);					
			}

		} catch (ClassNotFoundException e) {
			throw new TableManagerException("Can't insert managed record: ClassNotFoundException:"+e.getMessage()+" - "+e.getCause());
		} catch (SQLException e) {
			throw new TableManagerException("Can't insert managed record: SQLException:"+e.getMessage()+" - "+e.getCause());
		}

		
//		return null;
	}
	
	
	// only can arrives here if all required data was previously checked!
	private Integer insert(ManagedData managedData) throws ClassNotFoundException, SQLException {
		logger.debug("TableManager:insert managedData");

		Integer qty=null, id=null;

		String sql = buildInsertDataQuery(managedData);
		
		logger.debug("-----> insert data query:"+sql);

		// begin database insert
		connect();
		
		try {
			
			stmt = conn.createStatement();
			
			qty = (Integer) stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			
			logger.debug("--------->> affected rows:"+qty);
			
			if (qty>0) {
				ResultSet rs = stmt.getGeneratedKeys();
				rs.next();
				id = rs.getInt(1);
			
			}
			
			logger.debug("--------->> generated id:"+id);
			
		} catch (MySQLSyntaxErrorException e1) {
			
			logger.error("Error MySQLSyntaxErrorException "+e1.getMessage()+" - "+e1.getCause());

			
		} catch (SQLException e) {
			logger.error("Error SQLException "+e.getMessage()+" - "+e.getCause());
			id=-1;
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
			
		return id;
	}
	
	// only can arrives here if all required data was previously checked!
	private Integer update(ManagedData managedData) throws ClassNotFoundException, SQLException {
		logger.debug("TableManager:update managedData pk:"+managedData.getPk());

		Integer qty=null;

		String sql = buildUpdateDataQuery(managedData);
		
		logger.debug("-----> insert data query:"+sql);

		// begin database insert
		connect();
		
		try {
			
			stmt = conn.createStatement();
			
			qty = (Integer) stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			
			logger.debug("--------->> affected rows:"+qty);
			
		} catch (MySQLSyntaxErrorException e1) {
			
			logger.error("Error MySQLSyntaxErrorException "+e1.getMessage()+" - "+e1.getCause());

			
		} catch (SQLException e) {
			logger.error("Error SQLException "+e.getMessage()+" - "+e.getCause());
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
			
		return qty;
	}

	// only can arrives here if all required data was previously checked!
	private Integer load(ManagedData managedData) throws ClassNotFoundException, SQLException {
		logger.debug("TableManager:insert managedData");

		Integer qty=null, id=null;

		String sql = buildLoadDataQuery(managedData);
		
		logger.debug("-----> load data query:"+sql);

		// begin database select
		connect();
		
		try {
			
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			
			if (rs!=null) {
				rs.last();
				int qtyRows = rs.getRow();
				logger.debug("--------->> # rows recovered (ok=1):"+qtyRows);
				rs.first();
				loadRecord(rs, managedData);
			}

		} catch (MySQLSyntaxErrorException e1) {
			
			logger.error("Error MySQLSyntaxErrorException "+e1.getMessage()+" - "+e1.getCause());

			
		} catch (SQLException e) {
			logger.error("Error SQLException "+e.getMessage()+" - "+e.getCause());
			id=-1;
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
			
		return id;
	}	

	// IMPLEMENTAR
	private void loadRecord(ResultSet rs, ManagedData managedData) throws SQLException{
		
		rs.first();
		
		// get mandatory field info
		managedData.setPk(rs.getInt(1));
		managedData.setCurrentWorkId(rs.getInt(2));
		managedData.setCurrentStepWorkId(rs.getInt(3));
		String dataType;
		for (ManagedDataField mdf: managedData.getDataField()){

			dataType=mdf.getDataType().getName().toLowerCase();
			if (dataType.equals("text")) {
				mdf.setValue(rs.getString(mdf.getName()));
			} else if (dataType.equals("integer")) {
				mdf.setValue(String.valueOf(new Integer(rs.getInt(mdf.getName()))));
			} else if (dataType.equals("boolean")) {
				Boolean b = rs.getBoolean(mdf.getName());
				mdf.setValue(b.toString());
			} else {
				mdf.setValue(rs.getString(mdf.getName()));
			}
		}
	}

	/*
	 *  select from query format:
	 *  "SELECT  {mandatoryFieldName} {userFieldNameList} FROM {shema}.{tableName} WHERE process_work_id={processWorkId}"
	 *	
	 *	consider each field name (less last) must have your comma (,) separator   
	 *  
	 */
	
	private String buildLoadDataQuery(ManagedData managedData){
		String sql="SELECT ";
		sql+= "id, process_work_id, process_id "; // mandatory fields		

		Iterator<ManagedDataField> it = managedData.getDataField().iterator();
		while(it.hasNext()) {
			ManagedDataField dataField = it.next();
			sql+=", "+dataField.getName();
		}
		
		sql+=" FROM ";
		sql+=managedData.getManagedTableConfiguration().getSchema()+"."+managedData.getManagedTableConfiguration().getName()+" ";
		sql+=" WHERE process_work_id = "+managedData.getCurrentWorkId();

		return sql;
	}

	/*
	 *  insert query format:
	 *  "INSERT INTO {shema}.{tableName} ( {mandatoryFieldName} {userFieldNameList} ) VALUES ( {mandatoryFieldData} {userFieldDataList} )"
	 *	
	 *	consider each field name and value  (less last) must have your comma (,) separator   
	 *  
	 */
	private String buildInsertDataQuery(ManagedData managedData) {
		
		String sql="INSERT INTO ";
		sql+=managedData.getManagedTableConfiguration().getSchema()+"."+managedData.getManagedTableConfiguration().getName()+" ";
		sql+= "( process_work_id, process_id "; // mandatory fields
		
		Iterator<ManagedDataField> it = managedData.getDataField().iterator();
		String sqlValues=managedData.getCurrentWorkId()+", "+managedData.getProcessId()+" ";
		while(it.hasNext()) {
			ManagedDataField dataField = it.next();
			sql+=", "+dataField.getName();
			sqlValues+=", '"+dataField.getValue()+"' ";
		}
		sql+=" ) VALUES ( ";
		sql+=sqlValues+" )";

		return sql;
	}

	/*
	 *  update query format:
	 *  "UPDATE {shema}.{tableName} SET ( {mandatoryFieldName} {userFieldNameList} ) VALUES ( {mandatoryFieldData} {userFieldDataList} )"
	 *	
	 *	consider each field name and value  (less last) must have your comma (,) separator   
	 *  
	 */
	private String buildUpdateDataQuery(ManagedData managedData) {
		
		String sql="UPDATE ";
		sql+=managedData.getManagedTableConfiguration().getSchema()+"."+managedData.getManagedTableConfiguration().getName()+" ";
		
		sql+=" SET ";
		//UPDATE `bee_bpm_dev`.`w_process_def` SET `comments`='KAJAJA' WHERE `id`='138';
		
		Iterator<ManagedDataField> it = managedData.getDataField().iterator();
		int i=0;
		while(it.hasNext()) {
			if (i>0)  {sql+=", ";} // comma (field separator)
			ManagedDataField dataField = it.next();
			sql+=" "+dataField.getName();
			sql+="= '"+dataField.getValue()+"' ";
			i++;
		}
		sql+=" WHERE id = "+managedData.getPk();

		return sql;
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

		String sql 	= "CREATE TABLE "+tableName+" ";
		sql 		+=" ( id INTEGER AUTO_INCREMENT NOT NULL, "; // mandatory field ...
		sql 		+=" process_work_id INTEGER NOT NULL, ";   // mandatory field ...
		sql 		+=" process_id INTEGER NOT NULL, ";   // mandatory field indicates map version
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
		
		// mandatory pk and foreign key
		sql += " PRIMARY KEY ( id ), "; 
		sql += " CONSTRAINT `fk_"+tableName+"_1` FOREIGN KEY (`process_work_id`) REFERENCES `w_process_work` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION ";
		sql += " ) ENGINE=InnoDB DEFAULT CHARSET=utf8";
		
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
