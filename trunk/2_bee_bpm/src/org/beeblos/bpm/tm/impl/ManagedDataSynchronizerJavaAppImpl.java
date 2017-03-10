package org.beeblos.bpm.tm.impl;

import static org.beeblos.bpm.core.model.enumerations.ProcessStage.END;
import static org.beeblos.bpm.core.model.enumerations.ProcessStage.STARTUP;
import static org.beeblos.bpm.core.model.enumerations.ProcessStage.STEP_WORK_IS_INVOKED;
import static org.beeblos.bpm.core.model.enumerations.ProcessStage.STEP_WORK_WAS_PROCESSED;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.ManagedDataSynchronizerException;
import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.enumerations.ProcessDataSynchroWithType;
import org.beeblos.bpm.core.model.enumerations.ProcessStage;
import org.beeblos.bpm.tm.ManagedDataSynchronizer;
import org.beeblos.bpm.tm.TableManager;
import org.beeblos.bpm.tm.exception.TableManagerException;

import com.sp.common.model.ManagedDataField;
/**
 * Main class to synchronize managed data with external fields.
 * Possibles sources are: JDBC, App (in the scope of this application at runtime)
 * 
 * @author nestor
 *
 */
public class ManagedDataSynchronizerJavaAppImpl implements ManagedDataSynchronizer {

	private static final Log logger = LogFactory.getLog(ManagedDataSynchronizerJavaAppImpl.class.getName());
	
	public void synchronizeProcessWorkManagedData(
			WProcessWork processWork, ManagedData md, ProcessStage stage, Integer externalUserId ) 
			throws ManagedDataSynchronizerException {
			logger.debug(">>> synchronizeProcessWorkManagedData ....");
			
			if (processWork==null || processWork.getId()==null || processWork.getId()==0) {
				logger.error(">>Error._synchronizeProcessWorkManagedData: arrived work has no id...");
				throw new ManagedDataSynchronizerException("Error trying to synchronize new processWork: invalid arrived work is empy!!! ");
			}
			
			if (processWork.getProcessDef()==null || processWork.getProcessDef().getId()==null || processWork.getProcessDef().getId()==0) {
				logger.error("Error._synchronizeProcessWorkManagedData: arrived work has no process def ...");
				throw new ManagedDataSynchronizerException("Error trying to synchronize new processWork: invalid arrived work has not ProcessDef info!!! ");
			}
			
			if (processWork.getProcessDef().getProcessHead()==null || processWork.getProcessDef().getProcessHead().getId()==null || processWork.getProcessDef().getProcessHead().getId()==0) {
				logger.error(">>Error._synchronizeProcessWorkManagedData: arrived work has no processDef.processHead ...");
				throw new ManagedDataSynchronizerException("Error trying to synchronize new processWork: invalid arrived work has not ProcessHead info!!! ");
			}

			// no managed table defined -> don't have managed data....
			if (processWork.getProcessDef().getProcessHead().getManagedTableConfiguration()==null || processWork.getProcessDef().getProcessHead().getManagedTableConfiguration().getName()==null || "".equals(processWork.getProcessDef().getProcessHead().getManagedTableConfiguration().getName())) {
				logger.debug("Warning._synchronizeProcessWorkManagedData has empty tablename >> no managed data defined ...");
				return;
			}

			// no managed data fields defined (or loaded...)
			if (processWork.getProcessDef().getProcessHead().getProcessDataFieldDef().size()<1) {
				logger.debug("Warning._synchronizeProcessWorkManagedData has no managed data fields defined (or loaded...)");
				return;			
			}

			logger.debug(">> _synchronizeProcessWorkManagedData: synchro managed data for ProcessDefId:"
					+processWork.getProcessDef().getId()+" for created processWork id:"+processWork.getId()+" >> "+processWork.getIdObjectType());
			
			// obtain a list of managed data fields to synchronize ( dftos - Jdbc/App )
			List<WProcessDataField> dftosJdbc = new ArrayList<WProcessDataField>();
			List<WProcessDataField> dftosApp = new ArrayList<WProcessDataField>();
			for (WProcessDataField pdf: processWork.getProcessDef().getProcessHead().getProcessDataFieldDef()) {
				if ( pdf.isSynchronize() && _checkAtSynchroStage(pdf,stage) ) {
					if (pdf.getSynchroWith().equals(ProcessDataSynchroWithType.JDBC)) { // dml 20170201 - pasado el 'synchroWith' a ENUM
						dftosJdbc.add(pdf);
					} else {
						dftosApp.add(pdf);
					}
				}
			}
			
			// if no managed data fields to synchronize at startup, return...
			if (dftosJdbc.size()<1 && dftosApp.size()<1) {
				logger.debug("synchronizeProcessWorkManagedData(): has no managed data fields to synchronize at "
						+stage.stageName()+" ...");
				return;			
			}
			
			// synchronize jdbc fields ....
			if (dftosJdbc.size()>0) {
				
				logger.info("JDBC synchronization coming soon ....");
				
				logger.debug("synchronizeProcessWorkManagedData(): has "+dftosJdbc.size()+" for JDBC synchro ...");
			}
			
			// synchronize app fields ...
			if (dftosApp.size()>0) {
				logger.debug(">> synchronizeProcessWorkManagedData has "+dftosApp.size()
						+" for APP synchro ... at stage "+stage.stageName());
				
				for (WProcessDataField pdf: dftosApp) {
					try {
						synchronizeField(processWork, pdf, stage, md,externalUserId );
					} catch (ManagedDataSynchronizerException e) {
						logger.error("ManagedDataSynchronizerException: Can't sinchronize field: " 
								+ (pdf!=null?pdf.getName():"null")
								+ (e.getMessage()!=null?". "+e.getMessage():"")
								+ (e.getCause()!=null?". "+e.getCause():""));
					} catch (Exception e) {
						logger.error("Exception: Can't sinchronize field: " + pdf.getName()
								+ (e.getMessage()!=null?". "+e.getMessage():"")
								+ (e.getCause()!=null?". "+e.getCause():"")
								+ (e.getClass()!=null?". "+e.getClass():""));
					}	
				}
			}
			
			logger.debug(">>> and now persist tablemanager ...");
			
			// NOTA: ESTO LO TENIA EN EL ADD DE UN PROCESS-WORK NUEVO ASI QUE LO DEJO PARA EL STAGE DE STARTUP
			// NO TENGO CLARO QUE PARA LAS OTRAS INSTANCIAS DEBA HACERLO ...
			if (stage.equals(STARTUP) ) {
				// persist data in managed table
				TableManager tm = new TableManager();
				try {
					logger.debug(">>> TableManager was created, now tm.persist ...");	
					tm.persist(md);
				
				} catch (TableManagerException e) {
					String message = "TableManagerException: can't persist custom data at managed table:"
							+ (md.getManagedTableConfiguration()!=null
									?(md.getManagedTableConfiguration().getName()!=null
										?md.getManagedTableConfiguration().getName()
										:"null")
									: "managed table data is null")
							+ e.getMessage() + " - "
							+ e.getCause();
		
					logger.warn(message);
	
					throw new ManagedDataSynchronizerException(message);
	
				} catch (Exception e) {
					String message = "Exception: can't persist custom data at managed table:"
							+ (md.getManagedTableConfiguration()!=null
									?(md.getManagedTableConfiguration().getName()!=null
										?md.getManagedTableConfiguration().getName()
										:"null")
									: "managed table data is null")
							+ e.getMessage() + " - "
							+ e.getCause() +" - " 
							+ e.getClass();
		
					logger.warn(message);
	
					throw new ManagedDataSynchronizerException(message);				
				}
			}
//			for (WProcessDataField pdf: dftosApp ) {
//				System.out.println("name:"+pdf.getFieldName()+" value:"+pdf.g);
//			}

			
		}
	
	
	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.md.ManagedDataSynchronizer#synchronizeField(org.beeblos.bpm.core.model.WProcessWork, org.beeblos.bpm.core.model.WProcessDataField)
	 */
	@Override
	public Object synchronizeField(
			WProcessWork work, WProcessDataField pdf, ProcessStage stage, ManagedData md, Integer externalUserId ) 
					throws ManagedDataSynchronizerException {
		
		logger.debug("synchronizeField():  "+(pdf!=null?pdf.getName():"null"));
		
		// if there is not APP synchronized then throws exception
		if (pdf.getSynchroWith() == null || !pdf.getSynchroWith().equals(ProcessDataSynchroWithType.APP)) { // dml 20170102 - añadido control de no nulidad y pasado el synchroWith a ENUM
			String mess="synchronizeField(): parameter with name: " + pdf.getName() 
			+ " is trying to synchronize with mode: " + pdf.getSynchroWith()
			+ " and the type to synchronize must be " + ProcessDataSynchroWithType.APP;
			logger.info(mess);
			throw new ManagedDataSynchronizerException(mess);
		}
		
//		if (stage.equals(STARTUP) || stage.equals(STEP_WORK_IS_INVOKED)) {
			if (pdf.isAtProcessStartup() && stage.equals(STARTUP)) {
				logger.debug("synchronizeField(): synchro at STARTUP");
				_setMDF(md, pdf.getName(), fromExternalDataSynchro(work.getIdObject(),pdf,stage,externalUserId));
			} else if(pdf.isWhenStepWorkIsInvoked() && stage.equals(STEP_WORK_IS_INVOKED)) {
				logger.debug("synchronizeField(): synchro at STEP_WORK_IS_INVOKED");
				_setMDF(md, pdf.getName(), fromExternalDataSynchro(work.getIdObject(),pdf,stage,externalUserId));
			} else if (pdf.isAtProcessEnd() && stage.equals(END)) {
				logger.debug("synchronizeField(): synchro at END");
				toExternalDataSynchro(work,pdf,stage,md,externalUserId );
			} else if (pdf.isWhenStepWorkIsProcessed() && stage.equals(STEP_WORK_WAS_PROCESSED)) {
				logger.debug("synchronizeField(): synchro at STEP_WORK_WAS_PROCESSED");
				toExternalDataSynchro(work,pdf,stage,md,externalUserId );
			}
//		} else if (stage.equals(END) || stage.equals(STEP_WORK_WAS_PROCESSED)) {
//			toExternalDataSynchro(work,mdf,stage,md);
//		}
		
		
		return null;
		
	}

	/**
	 * to check if the process stage is considered in the process data field (pdf)
	 * @param pdf
	 * @param stage
	 * @return
	 */
	private boolean _checkAtSynchroStage(WProcessDataField pdf,ProcessStage stage) {
		if (stage.equals(STARTUP) ) {
			return pdf.isAtProcessStartup();	
		} else if (stage.equals(END) ) {
			return pdf.isAtProcessEnd();
		} else if (stage.equals(STEP_WORK_IS_INVOKED) ) {
			return pdf.isWhenStepWorkIsInvoked();
		} else if (stage.equals(STEP_WORK_WAS_PROCESSED) ) {
			return pdf.isWhenStepWorkIsProcessed();
		}
		return false;
	}
	
	
	// load obtained data in managed data element
	private void _setMDF(ManagedData md, String managedDataFieldName, Object value) {
		for (ManagedDataField m: md.getDataField()) {
			if (m.getName().equals(managedDataFieldName)) {
	
				// TODO: HAY QUE CONTROLAR CADA TIPO DE DATO Y PONERLE EL CORRESPONDIENTE
				// NULL SI CORRESPONDE ( EN STRING VA "", EN boolean VA false, etc)
//				if (value.getClass().equals("[Ljava.lang.String")) {
//					
//				}
				
//				m.setValue((value!=null?value.toString():""));
				m.setValue((value!=null?value:null)); // dml 20161028 - ITS: 1995 - puede aceptar cualquier dato
			}
		}
	}
	
	/**
	 * get external data from external data source ...
	 * 
	 * @param WProcessWork work
	 * @param WProcessDataField mdf
	 * @param ProcessStage stage
	 * @return
	 * @throws ManagedDataSynchronizerException
	 */
	private Object fromExternalDataSynchro(
			Integer idObject, WProcessDataField mdf, ProcessStage stage, Integer externalUserId ) 
					throws ManagedDataSynchronizerException {
		logger.debug(":fromExternalDataSynchro obtaining external data... ");

		// si va a recuperar datos de 1 fuente externa via app, tendrá que tener nombre de clase y método a invocar
		// y el método deberá devolver el valor requerido ...
		
		if (mdf.getClassName()==null || "".equals(mdf.getClassName())) {
			String mess=":fromExternalDataSynchro no classname provided for synchronize with external APP...";
			logger.info(mess);
			throw new ManagedDataSynchronizerException(mess);
		}
		if (mdf.getGetMethod()==null || "".equals(mdf.getGetMethod())) {
			String mess=":fromExternalDataSynchro no get-method provided for synchronize with external APP...";
			logger.info(mess);
			throw new ManagedDataSynchronizerException(mess);
		}
		
		Object returnedValue = new MethodSynchronizerImpl()
											.invokeExternalMethodGet(
													mdf.getClassName() 
													,mdf.getGetMethod() 
//													,mdf.getParamType() 
													,mdf.getDataType().getJavaType() // dml 20161102 - ITS: 1995 - cambiado para usar el nuevo enum DataType 
													,idObject
													,externalUserId // nes 20140707
												); 
		
		logger.debug("returnedValue:"+(returnedValue!=null?returnedValue:"null"));
		return returnedValue;
	}
	
	/**
	 * put the pdf data in external data source ... OJO MAL NO FUNCIONA AÚN!!!
	 * 
	 * @param WProcessWork work
	 * @param WProcessDataField mdf
	 * @param ProcessStage stage
	 * @return
	 * @throws ManagedDataSynchronizerException
	 */	
	private Object toExternalDataSynchro(
			WProcessWork work, WProcessDataField pdf, ProcessStage stage, ManagedData md, Integer externalUserId ) 
					throws ManagedDataSynchronizerException {
		logger.debug(":toExternalDataSynchro sending data to another app... ");

		if (pdf.getClassName()==null || "".equals(pdf.getClassName())) {
			throw new ManagedDataSynchronizerException(":toExternalDataSynchro no classname provided for synchronize with external APP...");
		}
		if (pdf.getPutMethod()==null || "".equals(pdf.getPutMethod())) {
			throw new ManagedDataSynchronizerException(":toExternalDataSynchro no get-method provided for synchronize with external APP...");
		}
		
		// obtains data fied with current value (in BPM)
		ManagedDataField dataField = _getCurrentManagedDataValues(md,pdf);
		
		new MethodSynchronizerImpl()
									.invokeExternalMethodPut( //classToInvoke, methodToInvoke, id, paramType, value)
											pdf.getClassName()
											,pdf.getPutMethod()
											,work.getIdObject() //idObject es el único vínculo entre el BPM y la app externa...
//											,pdf.getParamType() 
											,pdf.getDataType().getJavaType() // dml 20161102 - ITS: 1995 - cambiado para usar el nuevo enum DataType 
											,dataField.getValue()
											,externalUserId  // nes 20140707
											); 
		

		
		return null;
	}

	private ManagedDataField _getCurrentManagedDataValues(ManagedData md, WProcessDataField pdf) {
		for (ManagedDataField mdf: md.getDataField()) {
			if (mdf.getColumnName().equals(pdf.getColumnName())) {
				return mdf;
			}
		}
		// no managed data matches
		return null;
	}
}