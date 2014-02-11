package org.beeblos.bpm.tm;


public interface MethodSynchronizer {

	public void invokeExternalMethod(
			String classToInvoke, String methodToInvoke, Class[] paramTypes, Object[] paramData) ;
	
	public Object invokeExternalMethodGet(String classToInvoke, String methodToInvoke, String paramType, Integer id );
	
	public Object invokeExternalMethodPut(
			String classToInvoke, String methodToInvoke, Integer id, String paramType, String value );

}