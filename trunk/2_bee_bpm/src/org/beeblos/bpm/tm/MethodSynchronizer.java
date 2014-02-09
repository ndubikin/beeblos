package org.beeblos.bpm.tm;


public interface MethodSynchronizer {

	public void invokeExternalMethod(
			String classToInvoke, String methodToInvoke, Class[] paramTypes, Object[] paramData) ;
	
	public Object invokeExternalMethod(String classToInvoke, String methodToInvoke, String paramType, Integer id );

}