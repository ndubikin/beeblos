package org.beeblos.bpm.tm.impl;

import java.sql.SQLException;
import java.util.List;

import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.tm.TableManager;
import org.beeblos.bpm.tm.TableManagerBL;
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

public class TableManagerBLImpl implements TableManagerBL {


	TableManager tm = new TableManager();

	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.md.TableManagerBL#createManagedTable(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.md.TableManagerBL#recreateManagedTable(java.lang.String, java.lang.String, java.util.List)
	 */	
	@Override
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
	
	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.md.TableManagerBL#checkTableHasRecords(java.lang.String, java.lang.String)
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.md.TableManagerBL#removeManagedTable(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeManagedTable( String schemaName, String tableName ) 
			throws TableManagerException { 
		
		tm.removeTable(schemaName,tableName);

	}
	
	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.md.TableManagerBL#removeField(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */	
	@Override
	public void removeField( String schemaName, String tableName, String fieldName, boolean forceDeletion) 
			throws TableManagerException {

		tm.deleteFieldSynchro(schemaName, tableName, fieldName, false);
	
	}

	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.md.TableManagerBL#deleteRecord(java.lang.String, java.lang.String, java.lang.Integer)
	 */	
	@Override
	public void deleteRecord( String schemaName, String tableName, Integer processWorkId) 
			throws ClassNotFoundException, SQLException {

		tm.delete(schemaName, tableName, processWorkId);
	
	}

	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.md.TableManagerBL#getTableColumns(java.lang.String, java.lang.String)
	 */		
	@Override
	public List<Column> getTableColumns( 
			String schemaName, String tableName ) throws TableManagerException {
		
		return tm.getTableColumns(schemaName,tableName );
		
	}
	
	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.md.TableManagerBL#countTableRecords(java.lang.String, java.lang.String)
	 */			
	@Override
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
