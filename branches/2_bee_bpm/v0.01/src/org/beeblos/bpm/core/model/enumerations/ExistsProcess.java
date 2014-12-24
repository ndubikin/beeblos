package org.beeblos.bpm.core.model.enumerations;

/**
 * Enumeration to categorize different situations in BEE_BPM related with an
 * object / object id: doesn't exist any process, exists alive process, 
 * exist processes but it already was finished
 * 
 * @author nestor
 *
 */
public enum ExistsProcess {
	
	 NO_PROCESS_INFO(0,"noProcessInfo","there was no information about processes"), 
	 NO_PROCESS_EXISTS(1,"noProcessExists","there i no process related with object id and object type id"), 
	 ALREADY_FINISHED_PROCESSES(2,"alreadyFinishedProcesses","there's no process related with object id and object type id"),
	 EXISTS_ALIVE_PROCESSES(3,"existsAliveProcesses","exists almost one alive process"),; 

	 private int code;
     private final String situation;
     private final String comments;
    
	 private ExistsProcess(int c, String s, String n) {
	   code = c;
	   situation = s;
	   comments = n;
	 }
	 
	 public String getSituation(){  
		 return this.situation;  
	 }
	 
	 public int getCode() {
	   return code;
	 }

	 public String getComments() {
		 return this.comments;
	 }

	 public String getSituationName(int code) {
		 return this.situation;
	 }
	 
 
	 @Override public String toString() {
	   //Capitaliza solo la 1era letra
	   String s = super.toString();
	   return s.substring(0, 1) + s.substring(1).toLowerCase();
	 }
}