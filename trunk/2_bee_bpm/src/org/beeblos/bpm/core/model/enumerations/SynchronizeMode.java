package org.beeblos.bpm.core.model.enumerations;

/**
 * external data synchronization mode availables
 * @author nestor
 *
 */
	public enum SynchronizeMode {
		
		 J(1,"JDBC"), 
		 A(2,"App"); 

		 private int code;
	     private final String modeName;
	    
		 private SynchronizeMode(int s, String c) {
		   code = s;
		   modeName= c;
		 }
		 
		 public String modeName(){  
			 return this.modeName;  
		 }  	 
		 
		 public int getCode() {
		   return code;
		 }
		 
		 public String getModeName(int code) {
			 return this.modeName;
		 }
		 
		 @Override public String toString() {
		   //Capitaliza solo la 1era letra
		   String s = super.toString();
		   return s.substring(0, 1) + s.substring(1).toLowerCase();
		 }
	}