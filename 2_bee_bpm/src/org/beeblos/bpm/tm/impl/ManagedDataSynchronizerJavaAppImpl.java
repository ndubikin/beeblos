package org.beeblos.bpm.tm.impl;

import static org.beeblos.bpm.core.model.enumerations.ProcessStage.END;
import static org.beeblos.bpm.core.model.enumerations.ProcessStage.STARTUP;
import static org.beeblos.bpm.core.model.enumerations.ProcessStage.STEP_WORK_IS_INVOKED;
import static org.beeblos.bpm.core.model.enumerations.ProcessStage.STEP_WORK_WAS_PROCESSED;
import static org.beeblos.bpm.core.model.enumerations.SynchronizeMode.A;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.ManagedDataSynchronizerException;
import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.enumerations.ProcessStage;
import org.beeblos.bpm.tm.ManagedDataSynchronizer;

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
	
	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.md.ManagedDataSynchronizer#syncrhonizeField(org.beeblos.bpm.core.model.WProcessWork, org.beeblos.bpm.core.model.WProcessDataField)
	 */
	@Override
	public Object syncrhonizeField(
			WProcessWork work, WProcessDataField mdf, ProcessStage stage, ManagedData md) 
					throws ManagedDataSynchronizerException {
		logger.debug(">> syncrhonizeField : "+(mdf!=null?mdf.getName():"null"));
		
		// if there is not APP syncrhonized then throws exception
		if (!mdf.getSynchroWith().equals("A")) {
			String mess=":syncrhonizeField was called with Syncrhonize Mode nos APP...";
			logger.info(mess);
			throw new ManagedDataSynchronizerException(mess);
		}
		
//		if (stage.equals(STARTUP) || stage.equals(STEP_WORK_IS_INVOKED)) {
			if (mdf.isAtProcessStartup() && stage.equals(STARTUP)) {
				logger.debug(">> syncrho at STARTUP");
				_setMDF(md, mdf.getName(), fromExternalDataSynchro(work.getIdObject(),mdf,stage));
			} else if(mdf.isWhenStepWorkIsInvoked() && stage.equals(STEP_WORK_IS_INVOKED)) {
				logger.debug(">> syncrho at STEP_WORK_IS_INVOKED");
				_setMDF(md, mdf.getName(), fromExternalDataSynchro(work.getIdObject(),mdf,stage));
			} else if (mdf.isAtProcessEnd() && stage.equals(END)) {
				logger.debug(">> syncrho at END");
				toExternalDataSynchro(work,mdf,stage,md);
			} else if (mdf.isWhenStepWorkIsProcessed() && stage.equals(STEP_WORK_WAS_PROCESSED)) {
				logger.debug(">> syncrho at STEP_WORK_WAS_PROCESSED");
				toExternalDataSynchro(work,mdf,stage,md);
			}
//		} else if (stage.equals(END) || stage.equals(STEP_WORK_WAS_PROCESSED)) {
//			toExternalDataSynchro(work,mdf,stage,md);
//		}
		
		
		return null;
		
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
				
				
				m.setValue((value!=null?value.toString():""));
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
			Integer idObject, WProcessDataField mdf, ProcessStage stage) throws ManagedDataSynchronizerException {
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
											mdf.getClassName(), 
											mdf.getGetMethod(), 
											mdf.getParamType(), 
											idObject ); 
		
		logger.debug("returnedValue:"+(returnedValue!=null?returnedValue:"null"));
		return returnedValue;
	}
	
	/**
	 * put external data in external data source ... OJO MAL NO FUNCIONA AÚN!!!
	 * 
	 * @param WProcessWork work
	 * @param WProcessDataField mdf
	 * @param ProcessStage stage
	 * @return
	 * @throws ManagedDataSynchronizerException
	 */	
	private Object toExternalDataSynchro(
			WProcessWork work, WProcessDataField mdf, ProcessStage stage, ManagedData md) 
					throws ManagedDataSynchronizerException {
		logger.debug(":toExternalDataSynchro sending external data... ");

		if (mdf.getClassName()==null || "".equals(mdf.getClassName())) {
			throw new ManagedDataSynchronizerException(":toExternalDataSynchro no classname provided for synchronize with external APP...");
		}
		if (mdf.getGetMethod()==null || "".equals(mdf.getGetMethod())) {
			throw new ManagedDataSynchronizerException(":toExternalDataSynchro no get-method provided for synchronize with external APP...");
		}
		
//		Object returnedValue = new MethodSynchronizerImpl()
//									.invokeExternalMethodGet(
//											mdf.getClassName(), 
//											mdf.getGetMethod(), 
//											mdf.getParamType(), 
//											idObject ); 
		

		
		return null;
	}

}
