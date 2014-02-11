package org.beeblos.bpm.tm.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.model.WExternalMethod;
import org.beeblos.bpm.tm.MethodSynchronizer;
/**
 * Main class for execute external java methods (reflection) 
 * 
 * 
 * @author nestor
 *
 */
public class MethodSynchronizerImpl implements MethodSynchronizer {

	private static final Log logger = LogFactory.getLog(MethodSynchronizerImpl.class.getName());
	
	/* (non-Javadoc)
	 * @see org.beeblos.bpm.core.md.ManagedDataSynchronizer#syncrhonizeField(org.beeblos.bpm.core.model.WProcessWork, org.beeblos.bpm.core.model.WProcessDataField)
	 */

	public void invokeExternalMethod(WExternalMethod method, Integer currentId) {
		logger.debug(">>>invokeExternalMethod 1");
		
		invokeExternalMethod(
				method.getClassname(), 
				method.getMethodname(),
				method.getParamlistType(), 
				method.getParamlist()	
			);
	}
	
	/**
	 * Invokes a method(methodToInvoke) from a class (classToInvoke) with passed params
	 */
	public void invokeExternalMethod(
			String classToInvoke, String methodToInvoke, Class[] paramTypes, Object[] paramData) {
		logger.debug(">>>invokeExternalMethod 2");
		
		Object obj=null;

		try {

			Class<?> cls;
			Object instance = new Object();
			
			cls = Class.forName(classToInvoke);
			instance = cls.newInstance();

//			instance = getObject(cls);

			Method m = null;
			Object res = null;
			
			m = instance.getClass().getMethod(methodToInvoke,paramTypes);
			
			res = m.invoke(instance, paramData);
			
			logger.debug(">>>invokeExternalMethod: method was executed ...");
			
		} catch (ClassNotFoundException e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod ClassNotFoundException class:"
							+classToInvoke+"  method:"+methodToInvoke
							+" paramType:"+paramTypes+" error:"+e.getMessage()+" - "+e.getCause());
			e.printStackTrace();
		} catch (SecurityException e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod SecurityException class:"
							+classToInvoke+"  method:"+methodToInvoke
							+" paramType:"+paramTypes+" error:"+e.getMessage()+" - "+e.getCause());
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod NoSuchMethodException class:"
							+classToInvoke+"  method:"+methodToInvoke
							+" paramType:"+paramTypes+" error:"+e.getMessage()+" - "+e.getCause());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod IllegalArgumentException class:"
							+classToInvoke+"  method:"+methodToInvoke
							+" paramType:"+paramTypes+" error:"+e.getMessage()+" - "+e.getCause());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod IllegalAccessException class:"
							+classToInvoke+"  method:"+methodToInvoke
							+" paramType:"+paramTypes+" error:"+e.getMessage()+" - "+e.getCause());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod InvocationTargetException class:"
							+classToInvoke+"  method:"+methodToInvoke
							+" paramType:"+paramTypes+" error:"+e.getMessage()+" - "+e.getCause());
			e.printStackTrace();
		} catch (InstantiationException e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod InstantiationException class:"
							+classToInvoke+"  method:"+methodToInvoke
							+" paramType:"+paramTypes+" error:"+e.getMessage()+" - "+e.getCause());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("ManagedDataSynchronizerJavaAppImpl:invokeExternalMethod Exception class:"
							+classToInvoke+"  method:"+methodToInvoke
							+" paramType:"+paramTypes+" error:"+e.getMessage()+" - "+e.getCause()+" - "+e.getClass());
			e.printStackTrace();
		}
		
//		return obj;
		
	}
	
	/**
	 * reflection invoked method to obtain desired value ...
	 * To put a value in external app please see invokeExternalMethodPut
	 * 
	 * @param classToInvoke
	 * @param methodToInvoke
	 * @param paramType - java type of parameter syncrhonized...
	 * @param id
	 * @return
	 */
	@Override
	public Object invokeExternalMethodGet(
			String classToInvoke, String methodToInvoke, String paramType, Integer id ) {
		
		Object obj=null;
		Object res = null;
		
		try {

			Class<?> cls;
			Object instance = new Object();
			
			cls = Class.forName(classToInvoke);
			instance = cls.newInstance();

//			instance = getObject(cls);

			Method m = null;

			Class[] paramTypes = new Class[]{ Class.forName(paramType)};
					
			m = instance.getClass().getMethod(methodToInvoke,paramTypes);
			
			res = m.invoke(instance, new Object[] { id });
									
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
		
		return res;
		
	}

	/**
	 * reflection invoked method to write data in external java app...
	 * 
	 * To get a value from external app please see invokeExternalMethodGet
	 * 
	 * @param classToInvoke
	 * @param methodToInvoke
	 * @param id
	 * @param paramType - java type of parameter syncrhonized...
	 * @param value
	 * @return
	 */
	@Override
	public Object invokeExternalMethodPut(
			String classToInvoke, String methodToInvoke, Integer id, String paramType, String value ) {
		
		Object obj=null;
		Object res = null;
		
		try {

			Class<?> cls;
			Object instance = new Object();
			
			cls = Class.forName(classToInvoke);
			instance = cls.newInstance();

//			instance = getObject(cls);

			Method m = null;

			Class[] paramTypes = new Class[]{ Class.forName(paramType)};
					
			m = instance.getClass().getMethod(methodToInvoke,paramTypes);
			
			res = m.invoke(instance, new Object[] { id });
									
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
		
		return res;
		
	}
}
