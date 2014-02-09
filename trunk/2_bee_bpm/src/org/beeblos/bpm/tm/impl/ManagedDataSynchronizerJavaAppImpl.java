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
		logger.debug("ManagedDataSynchronizerJavaAppImpl:syncrhonizeField starting... ");
		
		// if there is not APP syncrhonized then throws exception
		if (!mdf.getSynchroWith().equals("A")) {
			throw new ManagedDataSynchronizerException("ManagedDataSynchronizerJavaAppImpl:syncrhonizeField was called with Syncrhonize Mode nos APP...");
		}
		
		if (stage.equals(STARTUP) || stage.equals(STEP_WORK_IS_INVOKED)) {
			if (mdf.isAtProcessStartup()) {
				_setMDF(md, mdf.getName(), fromExternalDataSynchro(work.getIdObject(),mdf,stage));
			}
		} else if (stage.equals(END) || stage.equals(STEP_WORK_WAS_PROCESSED)) {
			toExternalDataSynchro(work,mdf,stage);
		}
		
		
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
		logger.debug("ManagedDataSynchronizerJavaAppImpl:syncrhonizeFromExternalData starting... ");

		// si va a recuperar datos de 1 fuente externa via app, tendrá que tener nombre de clase y método a invocar
		// y el método deberá devolver el valor requerido ...
		
		if (mdf.getClassName()==null || "".equals(mdf.getClassName())) {
			throw new ManagedDataSynchronizerException("ManagedDataSynchronizerJavaAppImpl:fromExternalDataSynchro no classname provided for synchronize with external APP...");
		}
		if (mdf.getGetMethod()==null || "".equals(mdf.getGetMethod())) {
			throw new ManagedDataSynchronizerException("ManagedDataSynchronizerJavaAppImpl:fromExternalDataSynchro no get-method provided for synchronize with external APP...");
		}
		
		Object returnedValue = new MethodSynchronizerImpl()
									.invokeExternalMethod(
											mdf.getClassName(), 
											mdf.getGetMethod(), 
											mdf.getParamType(), 
											idObject ); 
		
		return returnedValue;
	}
	
	/**
	 * put external data in external data source ...
	 * 
	 * @param WProcessWork work
	 * @param WProcessDataField mdf
	 * @param ProcessStage stage
	 * @return
	 * @throws ManagedDataSynchronizerException
	 */	
	private Object toExternalDataSynchro(
			WProcessWork work, WProcessDataField mdf, ProcessStage stage) throws ManagedDataSynchronizerException {
		logger.debug("ManagedDataSynchronizerJavaAppImpl:syncrhonizeFromExternalData starting... ");

		
		return null;
	}

}
