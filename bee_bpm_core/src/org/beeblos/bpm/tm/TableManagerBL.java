package org.beeblos.bpm.tm;

import java.util.List;

import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.tm.exception.TableAlreadyExistsException;
import org.beeblos.bpm.tm.exception.TableHasRecordsException;
import org.beeblos.bpm.tm.exception.TableManagerException;
import org.beeblos.bpm.tm.model.Column;

public interface TableManagerBL {

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
	public abstract void createManagedTable(String schemaName,
			String tableName, List<WProcessDataField> dataFieldList)
			throws TableAlreadyExistsException, TableManagerException;

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
	public abstract void recreateManagedTable(String schemaName,
			String tableName, List<WProcessDataField> dataFieldList)
			throws TableHasRecordsException, TableManagerException;

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
	public abstract boolean checkTableHasRecords(String schemaName,
			String tableName) throws TableManagerException;

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
	public abstract void removeManagedTable(String schemaName, String tableName)
			throws TableManagerException;

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
	public abstract void removeField(String schemaName, String tableName,
			String fieldName, boolean forceDeletion)
			throws TableManagerException;

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
	 * @throws TableManagerException 
	 * 
	 */
	public abstract void deleteRecord(String schemaName, String tableName,
			Integer processWorkId) throws TableManagerException;

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
	public abstract List<Column> getTableColumns(String schemaName,
			String tableName) throws TableManagerException;

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
	public abstract Integer countTableRecords(String schemaName,
			String tableName) throws TableManagerException;

}