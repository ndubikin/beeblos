package org.beeblos.bpm.core.bl;

import java.sql.SQLException;
import java.util.List;

import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.tm.TableManager;
import org.beeblos.bpm.tm.exception.TableAlreadyExistsException;
import org.beeblos.bpm.tm.exception.TableHasRecordsException;
import org.beeblos.bpm.tm.exception.TableManagerException;
import org.beeblos.bpm.tm.model.Column;

/**
 * @author nes
 * 
 * bridge with bussiness logic form work with managed tables (tables stores user
 * data fields for a process head and their versions (process-def) )
 * 
 * 
 */

public class TableManagerBL {


	TableManager tm = new TableManager();

	/**
	 * @author nes
	 * 
	 * Call TableManager.createTable method and creates a table in the schema indicated with the name indicated
	 * and with data field list
	 * 
	 * @param String schemaName
	 * @param String tableName 
	 * @param List<WProcessDataField> dataFieldList - data field list to create. This data field list must not
	 * include mandatory fields for a managed table like id, process_work_id and process_id
	 *
	 * @return void
	 * 
	 */
	public void createManagedTable( 
			String schemaName, String tableName, List<WProcessDataField> dataFieldList) 
					throws TableAlreadyExistsException, TableManagerException { 

//		String tableName = checkTableName(); // checks tablename and update currentWProcessDef if corresponds...
//		reloadDataFieldList(); // refresh dataFieldList
		
		if (checkTableExists(schemaName,tableName)) {
			throw new TableAlreadyExistsException("Table "+tableName+" in schema "+schemaName+" already exists!!");
		}
		
		tm.createTable( schemaName, tableName, dataFieldList);
			

	}
	
	/**
	 * @author nes
	 * 
	 * Drop indicated table in schema if exists and if don't have data. Call createManagedTable to recreate it. 
	 * If the table has data must be cleaned previously to drop it
	 * 
	 * @param String schemaName
	 * @param String tableName 
	 * @param List<WProcessDataField> dataFieldList - data field list to create. This data field list must not
	 * include mandatory fields for a managed table like id, process_work_id and process_id
	 *
	 * @return void
	 * 
	 */	
	public void recreateManagedTable( 
			String schemaName, String tableName, List<WProcessDataField> dataFieldList ) 
					throws TableHasRecordsException, TableManagerException {
		
		if (checkTableExists(schemaName,tableName)) {
		
			if (checkTableHasRecords(schemaName,tableName)) {
				throw new TableHasRecordsException("Table "+tableName+" in schema "+schemaName+" has records and must not recreate it!");
			} else {
				
				removeManagedTable(schemaName,tableName);

			}
		} 
		
		try {
			createManagedTable(schemaName,tableName,dataFieldList);
		} catch (TableAlreadyExistsException e) {
			// at this point, if table exists so there is a problem to remove it
			// because at beggining of this method the table must be deleted ...
			// or throws TableHasRecordsException ...
			throw new TableManagerException("recreateManagedTable:TableAlreadyExistsException "
					+" there is not possible to recreate given table "+tableName+" in schema "+schemaName
					+" please contact your database admin or Softpoint team. "+e.getMessage()+" - "+e.getCause());
		}
		
	}
	
	/**
	 * @author nes
	 * 
	 * Checks if table in schema has records and returns true or false
	 * 
	 * @param String schemaName
	 * @param String tableName 
	 *
	 * @return boolean
	 * 
	 */
	public boolean checkTableHasRecords( String schemaName, String tableName ) 
			throws TableManagerException{
		
		boolean returnValue = false;

		Integer qtyData = tm.countNotNullRecords(schemaName, tableName, null);

		if (qtyData != null
				&& qtyData>0 ){
			returnValue = true;
		}
		
		return returnValue;

	}

	/**
	 * @author nes
	 * 
	 * Removes managed table
	 * 
	 * @param String schemaName
	 * @param String tableName 
	 *
	 * @return void
	 * 
	 */
	public void removeManagedTable( String schemaName, String tableName ) 
			throws TableManagerException { 
		
		tm.removeTable(schemaName,tableName);

	}
	
	/**
	 * @author nes
	 * 
	 * Removes indicated field name in managed table
	 * 
	 * @param String schemaName
	 * @param String tableName
	 * @param String fieldName  
	 *
	 * @return void
	 * 
	 */	
	public void removeField( String schemaName, String tableName, String fieldName, boolean forceDeletion) 
			throws TableManagerException {

		tm.deleteFieldSynchro(schemaName, tableName, fieldName, false);
	
	}

	/**
	 * @author dml 20130909
	 * 
	 * Deletes indicated record
	 * 
	 * @param String schemaName
	 * @param String tableName
	 * @param Integer processWorkId  
	 *
	 * @return void
	 * 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * 
	 */	
	public void deleteRecord( String schemaName, String tableName, Integer processWorkId) 
			throws ClassNotFoundException, SQLException {

		tm.delete(schemaName, tableName, processWorkId);
	
	}

	/**
	 * @author nes
	 * 
	 * returns a list of a table (in indicated schema) columns
	 * Column list is obtained of table metadata provided by db engine by jdbc
	 * 
	 * @param String schemaName
	 * @param String tableName
	 *
	 * @return List<Column>
	 * 
	 */		
	public List<Column> getTableColumns( 
			String schemaName, String tableName ) throws TableManagerException {
		
		return tm.getTableColumns(schemaName,tableName );
		
	}
	
	/**
	 * @author nes
	 * 
	 * returns qty of records in indicated table / schema
	 * 
	 * @param String schemaName
	 * @param String tableName
	 *
	 * @return Integer
	 * 
	 */			
	public Integer countTableRecords(String schemaName, String tableName) 
			throws TableManagerException {
		Integer qtyRecords = 
				tm.checkTableExists( schemaName,tableName);
		return qtyRecords;
	}
	
	/**
	 * @author nes
	 * 
	 * Checks table existence in schema indicated.
	 * 
	 * @param String schemaName
	 * @param String tableName
	 *
	 * @return boolean
	 * 
	 */		
	private boolean checkTableExists( String schemaName, String tableName ) 
			throws TableManagerException {

		//currentWProcessDef.process.managedTableConfiguration.name
		Integer qtyRecords = 
				tm.checkTableExists( schemaName,tableName);


		System.out.println("qty records:"+qtyRecords);

		if (qtyRecords.equals(-1)) {
			return false;
		}  
		return true;
	}
	
}
