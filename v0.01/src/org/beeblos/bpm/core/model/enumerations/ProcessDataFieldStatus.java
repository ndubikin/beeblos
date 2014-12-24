package org.beeblos.bpm.core.model.enumerations;

public enum ProcessDataFieldStatus {
	
	 ALL(0,"All process data fields") 
	 , ACTIVE(1,"All current active process data fields")
	 , INACTIVE(2,"All current inactive process data fields")
	 , REQUIRED(2,"All current inactive process data fields")
	 ;  

	 private int code;
    private final String description;
    
	 private ProcessDataFieldStatus(int s, String c) {
	   code = s;
	   description= c;
	 }
	 
	 public String clase(){  
		 return this.description;  
	 }  	 
	 
	 public int getCode() {
	   return code;
	 }
	 
	 public String getClase(int code) {
		 return this.description;
	 }
	 
	 @Override public String toString() {
	   //Capitaliza solo la 1era letra
	   String s = super.toString();
	   return s.substring(0, 1) + s.substring(1).toLowerCase();
	 }
}