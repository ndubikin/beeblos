package org.beeblos.bpm.tm;

//import org.hibernate.Hibernate;
import static org.beeblos.bpm.core.util.Constants.DEFAULT_VARCHAR_LENGHT;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.tm.exception.TableManagerException;
import org.beeblos.bpm.tm.model.Column;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import com.sp.common.model.ManagedDataField;

public class TableManager {
	
	private static final String ADD_COLUMN = "ADD_COLUMN";
	private static final String CHANGE_COLUMN = "CHANGE_COLUMN";
	private static final String DROP_COLUMN = "DROP_COLUMN";

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
	private Integer insert(ManagedData managedData) 
			throws ClassNotFoundException, SQLException, TableManagerException {
		logger.debug("TableManager:insert managedData");

		Integer qty=null, id=null;
		String mess=null;

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
			mess="insert: Error MySQLSyntaxErrorException: TableManager:insert "+e1.getMessage()+" - "+e1.getCause();
			logger.error(mess);
			throw new TableManagerException(mess);
			
		} catch (SQLException e) {
			mess="Error SQLException: TableManager:insert "+e.getMessage()+" - "+e.getCause();
			logger.error(mess);
			id=-1;
		}    finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		    	  mess+=" >> nested error:"+se.getMessage()+" - "+se.getCause()+"\n";
		      }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		    	  mess+=" >> nested error:"+se.getMessage()+" - "+se.getCause()+"\n";
		         se.printStackTrace();
		      }//end finally try
		}
		if (mess!=null && !"".equals(mess)) {
			throw new TableManagerException(mess);
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
			
			logger.error("update: Error MySQLSyntaxErrorException "+e1.getMessage()+" - "+e1.getCause());

			
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
			
			logger.error("load: Error MySQLSyntaxErrorException "+e1.getMessage()+" - "+e1.getCause());

			
		} catch (SQLException e) {
			logger.error("load: Error SQLException "+e.getMessage()+" - "+e.getCause());
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
				mdf.setValue(rs.getString(mdf.getColumnName()));
			} else if (dataType.equals("integer")) {
				mdf.setValue(String.valueOf(new Integer(rs.getInt(mdf.getColumnName()))));
			} else if (dataType.equals("boolean")) {
				Boolean b = rs.getBoolean(mdf.getColumnName());
				mdf.setValue(b.toString());
			} else {
				mdf.setValue(rs.getString(mdf.getColumnName()));
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
			sql+=", "+dataField.getColumnName();
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
			sql+=", "+dataField.getColumnName();
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
			sql+=" "+dataField.getColumnName();
			sql+="= '"+dataField.getValue()+"' ";
			i++;
		}
		sql+=" WHERE id = "+managedData.getPk();

		return sql;
	}
	
	/**
	 * @author nes
	 * 
	 * Check table existence. If does not exist return -1
	 * If table exists return qty records in table
	 * 
	 * TableManagerException is thrown if can't obtain connection with engine
	 * If exception line SQLException comes in the query execution, returns -1 and logs error
	 * 
	 * @param String schema
	 * @param String tableName
	 *
	 * @return Integer
	 * 
	 */		
	public Integer checkTableExists(String schema, String tableName) 
			throws TableManagerException {

		Integer qty=null;
				
		try {
			connect();
		} catch (ClassNotFoundException e2) {
			throw new TableManagerException("checkTableExists: ClassNotFoundException: can't connect with database. "
					+e2.getMessage()+" - "+e2.getCause());
		} catch (SQLException e2) {
			throw new TableManagerException("checkTableExists: SQLException : can't obtain database connection: "
					+e2.getMessage()+" - "+e2.getCause());
		}

		DatabaseMetaData dbm;
		try {
			dbm = conn.getMetaData();

			// check if tableName table is there
			ResultSet tables = dbm.getTables(null, schema, tableName, null);
			
			if ( tables.next() ) {

				// Table exists
				String sql ="SELECT COUNT(id) AS COUNT FROM "+tableName;
					
				stmt = conn.createStatement();
				
				ResultSet rs = stmt.executeQuery(sql);

				while(rs.next()) {
					qty = rs.getInt("COUNT");
				   System.out.println("The count is " + qty);
				}				
				
			}
			else {
			  // Table does not exist
				qty=-1;
			}			
		} catch (MySQLSyntaxErrorException e1) {
			
			throw new TableManagerException("checkTableExists: MySQLSyntaxErrorException: can't check table existence "
					+e1.getMessage()+" - "+e1.getCause());
			

		} catch (SQLException e) {
			throw new TableManagerException("checkTableExists: SQLException: can't check table existence "
					+e.getMessage()+" - "+e.getCause());
			
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
	

	/**
	 * @author nes
	 * 
	 * tries select count qty of records of the table and returns it.
	 * If fieldName has value count qty of records has value not null for this field
	 * If fieldName is null (or "") count all records in the table
	 * If the table doesn't exists db engine will throw exception and this method will
	 * return -1
	 * 
	 * TableManagerException is thrown if can't obtain connection with engine
	 * If exception line SQLException comes in the query execution, returns -1 and logs error
	 * 
	 * @param WProcessDataField processDataField
	 *
	 * @return Integer
	 * 
	 */	
	public Integer countNotNullRecords(String schema, String tableName, String fieldName) 
			throws TableManagerException {

		Integer qty=null;
				
		try {
			connect();
		} catch (ClassNotFoundException e2) {
			String mess="countNotNullRecords ClassNotFoundException: can't connect with database. "
							+e2.getMessage()+" - "+e2.getCause();
			logger.error(mess);
			throw new TableManagerException(mess);
		} catch (SQLException e2) {
			String mess="tableManager: countNotNullRecords SQLException : can't obtain database connection: "
							+e2.getMessage()+" - "+e2.getCause();
			logger.error(mess);
			throw new TableManagerException(mess);
		}
		
		String sql ="SELECT COUNT(";
		sql += fieldName;
		sql +=") AS COUNT FROM "+tableName ;
		sql+=(fieldName!=null?" WHERE "+fieldName.replace(" ", "_")+" IS NOT NULL":" ");
		System.out.println("------>>>"+sql);
		
		try {
			
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()) {
				qty = rs.getInt("COUNT");
			   System.out.println("The count is " + qty);
			}
			
			
		} catch (MySQLSyntaxErrorException e1) {
			
			logger.error("countNotNullRecords: Error MySQLSyntaxErrorException "+e1.getMessage()+" - "+e1.getCause());
			
			qty=-1;
			
		} catch (SQLException e) {
			logger.error("countNotNullRecords: Error SQLException "+e.getMessage()+" - "+e.getCause());
			qty=-1;
			
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
	
	public List<Column> getTableColumns(String schema, String tableName) 
			throws TableManagerException {

		List<Column> colList = new ArrayList<Column>();
		
		try {

			try {
				connect();
			} catch (ClassNotFoundException e2) {
				throw new TableManagerException("tableManager: getTableColumns ClassNotFoundException: can't connect with database. "
						+e2.getMessage()+" - "+e2.getCause());
			} catch (SQLException e2) {
				throw new TableManagerException("tableManager: getTableColumns SQLException : can't obtain database connection: "
						+e2.getMessage()+" - "+e2.getCause());
			}
			
			md = conn.getMetaData();
			rs = md.getColumns(null, schema, tableName, "%");
			
			System.out.println(rs==null);
			rs.last();
			System.out.println(rs.getRow());
			
			rs.beforeFirst();
			while(rs.next()) { 
			    String columnName = rs.getString("COLUMN_NAME");
			    String sqlTypeStr= rs.getString("DATA_TYPE");
			    String sqlTypeName= rs.getString("TYPE_NAME");
			    String columnSizeStr= rs.getString("COLUMN_SIZE");
			    String nullableStr= rs.getString("NULLABLE");
//			    String uniqueStr= rs.getString("UNIQUE");
			    String isNullableStr= rs.getString("IS_NULLABLE");
			    String ordinalPosition = rs.getString("ORDINAL_POSITION");
			    String comment = rs.getString("REMARKS");
			    String defaultValue = rs.getString("COLUMN_DEF");
			    
			    Boolean nullable = (nullableStr.equals("1")?true:false);
//			    Boolean unique = (uniqueStr.equals("1")?true:false);
			    sqlTypeName = (sqlTypeName.equals("INT")
			    					?"INTEGER"
			    				:sqlTypeName);
			    
			    Integer columnSize = (columnSizeStr!=null?new Integer(columnSizeStr):0);
			    Integer sqlType = (sqlTypeStr!=null?new Integer(sqlTypeStr):null);
			    
//				Column(int length, String name, boolean nullable, boolean unique,
//						String sqlType, Integer sqlTypeCode, String comment,
//						String defaultValue) 

			    colList.add(new Column(
			    		columnSize,
			    		columnName, 
			    		nullable, 
			    		false, 
			    		sqlTypeName, 
			    		sqlType, 
			    		comment, 
			    		defaultValue));
			    
			}
			
		} catch (SQLException e) {
			throw new TableManagerException("tableManager: getTableColumns SQLException : can't obtain table column list: "
					+e.getMessage()+" - "+e.getCause());
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
		
		return colList;
		
	}

	// syncrhonize managed table structure (update of a dataField definition)
	public Integer updateFieldSynchro(
			ManagedData managedData, WProcessDataField storedDataField, WProcessDataField newDataField) 
					throws TableManagerException {
		
		if (managedData==null) throw new TableManagerException("can't synchronize null managedData!");
		if (managedData.getManagedTableConfiguration().getName()==null 
				|| "".equals(managedData.getManagedTableConfiguration().getName()))  
			throw new TableManagerException("can't synchronize managedData: managed table is not defined (managedTableName:"
					+(managedData.getManagedTableConfiguration().getName()==null?"null":"-emtpy string-"));

		if (storedDataField!=null) {
			managedData.setOperation(CHANGE_COLUMN);
			generateAlterTableForColumnMgmt(CHANGE_COLUMN, managedData.getManagedTableConfiguration().getSchema(), 
					managedData.getManagedTableConfiguration().getName(), 
					storedDataField.getColumnName(), newDataField.getColumnName(), 
					newDataField.getDataType().getSqlType(), newDataField.getDataType().getSqlTypeName(), 
					newDataField.getLength(), newDataField.getDefaultValue(),
					managedData.getDataField().size());
		} else {
			managedData.setOperation(ADD_COLUMN);
			generateAlterTableForColumnMgmt(ADD_COLUMN, managedData.getManagedTableConfiguration().getSchema(), 
					managedData.getManagedTableConfiguration().getName(), 
					null, newDataField.getColumnName(), 
					newDataField.getDataType().getSqlType(), newDataField.getDataType().getSqlTypeName(), 
					newDataField.getLength(), newDataField.getDefaultValue(),
					(managedData.getDataField()!=null?managedData.getDataField().size():0));					
		}

		return null;
	}	

	public String deleteFieldSynchro(
			String schemaName, String tableName, String dataFieldName, boolean forceDeletion) 
					throws TableManagerException {

		if (tableName==null || "".equals(tableName) || "".equals(tableName.trim()) ) {
			throw new TableManagerException("can't synchronize null table name!");
		}


		if (countNotNullRecords(
				schemaName,tableName,dataFieldName)==0 || forceDeletion) {

			dropColumn(DROP_COLUMN, 
					schemaName, tableName, dataFieldName );	
			
		}

		
		return DROP_COLUMN;
	}
	
	/*
	 * ALGUNOS ALTER PARA TENER EN CUENTA:
	 * 
	 * Cambio un BIT(1) a un INT(1)
	 * ALTER TABLE `bee_bpm_dev`.`w_step_data_field` CHANGE COLUMN `active` `active` INT(1) NULL DEFAULT b'1'  ;
	 * ALTER TABLE `bee_bpm_dev`.`w_step_data_field` CHANGE COLUMN `active` `active` BIT(1) NULL DEFAULT 1  ;
	 * 
	 * Cambio un BIT(1) a un VARCHAR(1)
	 * ALTER TABLE `bee_bpm_dev`.`w_step_data_field` CHANGE COLUMN `active` `active` VARCHAR(1) NULL DEFAULT '1'  ;
	 * 
	 * Date a varchar
	 * ALTER TABLE `bee_bpm_dev`.`w_step_data_field` CHANGE COLUMN `insert_date` `insert_date` VARCHAR(20) NULL DEFAULT NULL  ;
	 * ALTER TABLE `bee_bpm_dev`.`w_step_data_field` CHANGE COLUMN `insert_date` `insert_date` DATETIME NULL DEFAULT NULL  ;
	 * 
	 * 
	 * 
	 */
	
	private void changeColumn(String operation, String schema, String tableName, 
			String origColName, String newColName, String sqlTypeName, Integer length, 
			String defaultValue) throws TableManagerException {
		
		String sql = buildModifyColumnStatement( operation,  schema,  tableName, 
									 origColName,  newColName,  sqlTypeName,  length, defaultValue);
		
		System.out.println("change column: "+sql);
		
		try {
			
			connect();
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private void addColumn(String operation, String schema, String tableName, 
			String newColName, String sqlTypeName, Integer sqlType, Integer length, 
			String defaultValue) throws TableManagerException {

		String sql = buildAddColumnStatement( operation, schema, tableName, 
				 		newColName, sqlTypeName, sqlType, length, defaultValue);
		
		System.out.println("add column: "+sql);
		
		try {
			
			connect();
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private void dropColumn(String operation, String schema, String tableName, 
			String colName) throws TableManagerException {

		String sql = buildDropColumnStatement( operation,  schema,  tableName, 
				colName);
		
		System.out.println("drop column: "+sql);
		
		try {
			
			connect();
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private void generateAlterTableForColumnMgmt(String operation, String schema, String tableName, 
			String origColName, String newColName, Integer sqlType, String sqlTypeName, Integer length, 
			String defaultValue, Integer qtyColumns) throws TableManagerException {
		
		System.out.println(qtyColumns);
		qtyColumns+=3; // id, process_work_id & process_id are not listed in field column list of managedData
		System.out.println(qtyColumns);
		
		Column columnToBeModified=null;
		List<Column> lc = new ArrayList<Column>();
		// load current col scheme
		try {
			lc = getTableColumns(schema,tableName);
		} catch (TableManagerException e) {
			throw new 
				TableManagerException("generateAlterTableForColumnMgmt: AlterTable exception: can't get current table definition for table:"
										+schema+"."+tableName
										+e.getMessage()+" - "+e.getCause());
		} 
		
		// check existence of old column to determine if must execute CHANGE COLUMN or ADD COLUMN
		for (Column c: lc) {
			if (c.getName().equalsIgnoreCase(origColName)) {
				columnToBeModified= new Column(c);
				break;
			}
		}

		// check if may be an error in column name or similar ...
		if (operation.equals(CHANGE_COLUMN) && columnToBeModified==null) {
			throw new 
			TableManagerException("AlterTable exception: error trying change column data: can't identify modified column in table. Please try to synchronize your managedTable."
									+schema+"."+tableName);
		} else if (operation.equals(CHANGE_COLUMN) && lc.size()!=qtyColumns) {
			throw new
			TableManagerException("AlterTable exception: error trying change column data: doesn't match # columns in definition with # columns in table. Please try to synchronize your managedTable."
					+schema+"."+tableName);
		}
		
		// change column
		if (operation.equals(CHANGE_COLUMN) && columnToBeModified!=null) {
			changeColumn(operation, schema, tableName, 
									origColName, newColName, sqlTypeName, length, defaultValue);
		} else {
		// add column
			addColumn(operation, schema, tableName, newColName, sqlTypeName, sqlType, length, defaultValue);
		}
		
	}
	


	//ALTER TABLE `bee_bpm_dev`.`w_step_data_field` CHANGE COLUMN `active` `active` INT(1) NULL DEFAULT b'1'  ;
	private String	buildModifyColumnStatement(String operation, String schema, String tableName, 
			String origColName, String newColName, String sqlTypeName, Integer length, 
			String defaultValue) throws TableManagerException {
		
		String sql = "ALTER TABLE ";
		
		sql+=schema+"."+tableName+" ";
		sql +=" CHANGE COLUMN ";
		sql += origColName.replace(" ", "_")+" "+newColName.replace(" ", "_")+" "+sqlTypeName;
		
		if (length != null && length > 0) {
			sql +="("+length+") ";
		}
		if (defaultValue != null && !"".equals(defaultValue)) {
			sql+=" DEFAULT '"+defaultValue+"'";
		}
		
		return sql;
	}
	
	//ALTER TABLE `bee_bpm_dev`.`w_step_data_field` CHANGE COLUMN `active` `active` INT(1) NULL DEFAULT b'1'  ;
	private String	buildAddColumnStatement(String operation, String schema, String tableName, 
			String newColName, String sqlTypeName, Integer sqlType, Integer length, String defaultValue) 
					throws TableManagerException {
		
		String sql = "ALTER TABLE ";
		
		sql+=schema+"."+tableName+" ";
		sql += " ADD COLUMN ";
		sql += newColName+" ";
		sql += sqlTypeName+" ";
		sql += getColumnSize(sqlType,length)+" ";

		if (defaultValue != null && !"".equals(defaultValue)) {
			sql+=" DEFAULT '"+defaultValue+"'";
		}
		
		return sql;
	}

	//ALTER TABLE `bee_bpm_dev`.`w_step_data_field` CHANGE COLUMN `active` `active` INT(1) NULL DEFAULT b'1'  ;
	private String	buildDropColumnStatement(String operation, String schema, String tableName, 
			String colName) throws TableManagerException {
		
		String sql = "ALTER TABLE ";
		
		sql+=schema+"."+tableName+" ";
		sql +=" DROP COLUMN ";
		sql += colName.replace(" ", "_")+" ";
		
		return sql;
	}
	
	private String buildCreateTableStatement(String tableName,List<WProcessDataField> columns) {

		String sql 	= "CREATE TABLE "+tableName+" ";
		sql 		+=" ( id INTEGER AUTO_INCREMENT NOT NULL, "; // mandatory field ...
		sql 		+=" process_work_id INTEGER NOT NULL, ";   // mandatory field ...
		sql 		+=" process_id INTEGER NOT NULL, ";   // mandatory field indicates map version
		for (WProcessDataField column: columns) {
			sql+=	column.getColumnName()+" "
					+ column.getDataType().getSqlTypeName()+" "
					+ getColumnSize(column)+", ";
		}

		// mandatory pk and foreign key
		sql += " PRIMARY KEY ( id ), "; 
		sql += " CONSTRAINT `fk_"+tableName+"_1` FOREIGN KEY (`process_work_id`) REFERENCES `w_process_work` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION ";
		sql += " ) ENGINE=InnoDB DEFAULT CHARSET=utf8";
		
		System.out.println("sql:"+sql);
		
		return sql;
	}
	
	/**
	 * @author nes
	 * 
	 * Returns a string with column name enclosed in brackets () if the sqlType
	 * accepts this parameter.
	 * If not returns ""
	 * 
	 * @param WProcessDataField column
	 *
	 * @return String
	 * 
	 */	
	private String getColumnSize(WProcessDataField column) {
	
		return getColumnSize(
					column.getDataType().getSqlType(),
					column.getLength() );
	}

	/**
	 * @author nes
	 * 
	 * Returns a string with column name enclosed in brackets () if the sqlType
	 * accepts this parameter.
	 * If not returns ""
	 * 
	 * @param Integer sqlType
	 * @param Integer length
	 *
	 * @return String
	 * 
	 */	
	private String getColumnSize(Integer sqlType, Integer length) {
		
		String colsize="";
		if (sqlType.equals(java.sql.Types.VARCHAR)) {
			if (length>0) { // nes 20130828
				colsize = "("+length+")";
			} else {
				// default length
				colsize = "("+DEFAULT_VARCHAR_LENGHT+")";
			}
		}
		return colsize;
	}

	/**
	 * @author nes
	 * 
	 * Creates a new table tableName in schema schemaName with list of given columns
	 * 
	 * There is responsability of caller checks table existence to avoid exception
	 * 
	 * 
	 * @param String schema
	 * @param String tableName
	 * @param List<WProcessDataField> columns
	 *
	 * @return Integer
	 * 
	 */		
	public void createTable( String schemaName, String tableName,  List<WProcessDataField> columns) 
			throws TableManagerException {

			try {
				connect();
			} catch (ClassNotFoundException e2) {
				throw new TableManagerException("createTable: ClassNotFoundException: can't connect with database. "
						+e2.getMessage()+" - "+e2.getCause());
			} catch (SQLException e2) {
				throw new TableManagerException("createTable: SQLException : can't obtain database connection: "
						+e2.getMessage()+" - "+e2.getCause());
			}

		try {
		
			String sql = buildCreateTableStatement(tableName,columns);
			
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
			throw new TableManagerException("tableManager:create SQLException "+e.getMessage()+" - "+e.getCause());
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
	

	public void removeTable(String schemaName, String tableName) 
			throws TableManagerException {

		String sql = "DROP TABLE "+schemaName+"."+tableName+" " ; 

		try {
			connect();
		} catch (ClassNotFoundException e2) {
			throw new TableManagerException("removeTable: ClassNotFoundException: can't connect with database. "
					+e2.getMessage()+" - "+e2.getCause());
		} catch (SQLException e2) {
			throw new TableManagerException("removeTable: SQLException : can't obtain database connection: "
					+e2.getMessage()+" - "+e2.getCause());
		}
		
		try {
			// check if table exists
			md = conn.getMetaData();
			rs = md.getColumns(null, schemaName, tableName, "%");
			if (rs==null) {
				System.out.println("table "+tableName+" doesn't exists ... can't drop it !");
				return;
			}
			
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			System.out.println("table:"+tableName+" was dropped ...");
			
		} catch (SQLException e) {
			throw new TableManagerException("removeTable: SQLException : Error removing table!! "
					+e.getMessage()+" - "+e.getCause());
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
}
