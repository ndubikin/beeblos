package org.beeblos.bpm.core.md.impl;

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
import org.beeblos.bpm.core.md.ManagedDataSynchronizer;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.enumerations.ProcessStage;
/**
 * Main class to synchronize managed data with external fields.
 * Possibles sources are: JDBC, App (in the scop of this application at runtime)
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
			WProcessWork work, WProcessDataField mdf, ProcessStage stage) throws ManagedDataSynchronizerException {
		logger.debug("ManagedDataSynchronizerJavaAppImpl:syncrhonizeField starting... ");
		
		// if there is not APP syncrhonized then throws exception
		if (!mdf.getSynchroWith().equals("A")) {
			throw new ManagedDataSynchronizerException("ManagedDataSynchronizerJavaAppImpl:syncrhonizeField was called with Syncrhonize Mode nos APP...");
		}
		
		if (stage.equals(STARTUP) || stage.equals(STEP_WORK_IS_INVOKED)) {
			if (mdf.isAtProcessStartup()) {
				fromExternalDataSynchro(work,mdf,stage);
			}
		} else if (stage.equals(END) || stage.equals(STEP_WORK_WAS_PROCESSED)) {
			toExternalDataSynchro(work,mdf,stage);
		}
		
		
		return null;
		
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
			WProcessWork work, WProcessDataField mdf, ProcessStage stage) throws ManagedDataSynchronizerException {
		logger.debug("ManagedDataSynchronizerJavaAppImpl:syncrhonizeFromExternalData starting... ");

		// si va a recuperar datos de 1 fuente externa via app, tendrá que tener nombre de clase y método a invocar
		// y el método deberá devolver el valor requerido ...
		
		if (mdf.getClassName()==null || "".equals(mdf.getClassName())) {
			throw new ManagedDataSynchronizerException("ManagedDataSynchronizerJavaAppImpl:fromExternalDataSynchro no classname provided for synchronize with external APP...");
		}
		if (mdf.getGetMethod()==null || "".equals(mdf.getGetMethod())) {
			throw new ManagedDataSynchronizerException("ManagedDataSynchronizerJavaAppImpl:fromExternalDataSynchro no get-method provided for synchronize with external APP...");
		}
		
		Object returnedValue = invokeExternalMethod(mdf.getClassName(), mdf.getGetMethod(), work.getIdObject() ); 
		
		return null;
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
	
	/**
	 * reflection invoked method to obtain looked value ...
	 * 
	 * @param classToInvoke
	 * @param methodToInvoke
	 * @param id
	 * @return
	 */
	private Object invokeExternalMethod(String classToInvoke, String methodToInvoke, Integer id ) {
		
		Object obj=null;

		try {

			Class<?> cls;
			Object instance = new Object();
			
			cls = Class.forName(classToInvoke);
			instance = cls.newInstance();

//			instance = getObject(cls);

			Method m = null;
			Object res = null;
			
			m = instance.getClass().getMethod(methodToInvoke,new Class[]{Integer.class});
			
			res = m.invoke(res, id);
									
		} catch (ClassNotFoundException e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod ClassNotFoundException class:"+classToInvoke+"  method:"+methodToInvoke+" id:"+id+" error:"+e.getMessage()+" - "+e.getCause());
			e.printStackTrace();
		} catch (SecurityException e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod SecurityException class:"+classToInvoke+"  method:"+methodToInvoke+" id:"+id+" error:"+e.getMessage()+" - "+e.getCause());
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod NoSuchMethodException class:"+classToInvoke+"  method:"+methodToInvoke+" id:"+id+" error:"+e.getMessage()+" - "+e.getCause());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod IllegalArgumentException class:"+classToInvoke+"  method:"+methodToInvoke+" id:"+id+" error:"+e.getMessage()+" - "+e.getCause());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod IllegalAccessException class:"+classToInvoke+"  method:"+methodToInvoke+" id:"+id+" error:"+e.getMessage()+" - "+e.getCause());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod InvocationTargetException class:"+classToInvoke+"  method:"+methodToInvoke+" id:"+id+" error:"+e.getMessage()+" - "+e.getCause());
			e.printStackTrace();
		} catch (InstantiationException e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod InstantiationException class:"+classToInvoke+"  method:"+methodToInvoke+" id:"+id+" error:"+e.getMessage()+" - "+e.getCause());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod Exception class:"+classToInvoke+"  method:"+methodToInvoke+" id:"+id+" error:"+e.getMessage()+" - "+e.getCause()+" - "+e.getClass());
			e.printStackTrace();
		}
		
		return obj;
		
	}
}
