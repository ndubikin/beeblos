package org.beeblos.bpm.core.model.enumerations;

/**
 * For ManagedDataSynchronizer use ...
 * @author nestor
 *
 */
public enum ProcessStage {
	
	 STARTUP(1,"startup"), 
	 END(2,"end"), 
	 STEP_WORK_IS_INVOKED(3,"step_work_is_invoked"), 
	 STEP_WORK_WAS_PROCESSED(4,"step_work_was_processed"); 

	 private int code;
     private final String stageName;
    
	 private ProcessStage(int s, String c) {
	   code = s;
	   stageName= c;
	 }
	 
	 public String stageName(){  
		 return this.stageName;  
	 }  	 
	 
	 public int getCode() {
	   return code;
	 }
	 
	 public String getStageName(int code) {
		 return this.stageName;
	 }
	 
	 @Override public String toString() {
	   //Capitaliza solo la 1era letra
	   String s = super.toString();
	   return s.substring(0, 1) + s.substring(1).toLowerCase();
	 }
}