package org.beeblos.bpm.core.model;

// Generated Feb 7, 2014 8:42:28 AM by Hibernate Tools 3.4.0.CR1

import java.util.Arrays;
import java.util.Date;

/**
 * WExternalMethod generated by hbm2java 
 * 
 * nes 20140207
 * 
 * This class represents any external method to be executed from bee-bpm.
 * Designer/Programmer responsibility to allow context class and method reachable
 * at execution time 
 * Invoking external method must be linked with sequence (routes), step (before
 * load step, after executing step), process (at start time or at end process time), etc.
 * 
 */
public class WExternalMethod implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private WProcessHead processHead;
	
	/**
	 * External class to be invoked
	 * BeeBPM does not check for availability or right behavior of this
	 * external methods
	 * 
	 */
	private String classname;
	
	/**
	 * External method to be invoked/executed
 	 * BeeBPM does not check for availability or right behavior of this
	 * external methods

	 */
	private String methodname;
	/**
	 * type of execution: 
	 * P: put method (send info to external class/objects)
	 * G: get method (retrieve information from external class/object)
	 * 
	 */
	private String type;
	
	/**
	 * Param list values to send to external method. 
	 * This field it's not persisted to database and is intended only to load
	 * the field list values to execute external method
	 * 
	 * BeeBPM does not check for availability or right behavior of this
	 * external methods
	 */
	private Object[] paramlist;
	
	/**
	 * Param list names to send to external method. Properties must exist and
	 * must be reachable at runtime stage.
	 * BeeBPM does not check for availability or right behavior of this
	 * external methods
	 */
	private String[] paramlistName;
	
	/**
	 * Param list types to send to external method. Properties must exist and
	 * must be reachable at runtime stage.
	 * BeeBPM does not check for availability or right behavior of this
	 * external methods
	 */
	private Class[] paramlistType;	
	
	/**
	 * indicates this method must be invoked / executed when process
	 * is starting (at creation time of a new instance of the process)
	 */
	private boolean atProcessStartup;

	/**
	 * indicates this method must be invoked / executed when process
	 * is finishing. An instance of process finishes when no new step-work is
	 * created and all current step-work are processed...
	 */
	private boolean atProcessEnd;

	/**
	 * indicates this method must be invoked / executed when each step
	 * is starting. The step must be loaded in "process" mode (or with lock)
	 */
	private boolean whenStepWorkIsInvoked;

	/**
	 * indicates this method must be invoked / executed when each step
	 * is being processed. This stage includes turn-back situation
	 */
	private boolean whenStepWorkIsProcessed;
	
	/**
	 * implementation pending: logical expression to evaluate for execute
	 * external method...
	 */
	private String logicalConditionExecution;
	
	/**
	 * to run time test purposes. If container checks if target ir reachable
	 * then this test time can be stored here.
	 */
	private String tested;
	
	private Date insertDate;
	private java.lang.Integer insertUser;
	private Date modDate;
	private java.lang.Integer modUser;


	public WExternalMethod() {
	}

	public WExternalMethod(Integer id) {
		this.id = id;
	}

	public WExternalMethod(boolean createEmptyObject) {
		
		if (createEmptyObject){
			this.processHead = new WProcessHead(createEmptyObject);
		}
		
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public WProcessHead getProcessHead() {
		return this.processHead;
	}

	public void setProcessHead(WProcessHead processHead) {
		this.processHead = processHead;
	}

	public String getClassname() {
		return this.classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getMethodname() {
		return this.methodname;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getParamlistName() {
		return paramlistName;
	}


	public void setParamlistName(String[] paramlistName) {
		this.paramlistName = paramlistName;
	}


	public Object[] getParamlist() {
		return this.paramlist;
	}

	public void setParamlist(Object[] paramlist) {
		this.paramlist = paramlist;
	}

	public Class[] getParamlistType() {
		return paramlistType;
	}


	public void setParamlistType(Class[] paramlistType) {
		this.paramlistType = paramlistType;
	}


	public Boolean getAtProcessStartup() {
		return this.atProcessStartup;
	}

	public void setAtProcessStartup(Boolean atProcessStartup) {
		this.atProcessStartup = atProcessStartup;
	}

	public Boolean getAtProcessEnd() {
		return this.atProcessEnd;
	}

	public void setAtProcessEnd(Boolean atProcessEnd) {
		this.atProcessEnd = atProcessEnd;
	}

	public Boolean getWhenStepWorkIsInvoked() {
		return this.whenStepWorkIsInvoked;
	}

	public void setWhenStepWorkIsInvoked(Boolean whenStepWorkIsInvoked) {
		this.whenStepWorkIsInvoked = whenStepWorkIsInvoked;
	}

	public Boolean getWhenStepWorkIsProcessed() {
		return this.whenStepWorkIsProcessed;
	}

	public void setWhenStepWorkIsProcessed(Boolean whenStepWorkIsProcessed) {
		this.whenStepWorkIsProcessed = whenStepWorkIsProcessed;
	}

	public String getLogicalConditionExecution() {
		return this.logicalConditionExecution;
	}

	public void setLogicalConditionExecution(String logicalConditionExecution) {
		this.logicalConditionExecution = logicalConditionExecution;
	}

	public String getTested() {
		return this.tested;
	}

	public void setTested(String tested) {
		this.tested = tested;
	}

	public Date getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public Integer getInsertUser() {
		return this.insertUser;
	}

	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}

	public Date getModDate() {
		return this.modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	public Integer getModUser() {
		return this.modUser;
	}

	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((processHead == null) ? 0 : processHead.hashCode());
		result = prime * result + (atProcessEnd ? 1231 : 1237);
		result = prime * result + (atProcessStartup ? 1231 : 1237);
		result = prime * result
				+ ((classname == null) ? 0 : classname.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((logicalConditionExecution == null) ? 0
						: logicalConditionExecution.hashCode());
		result = prime * result
				+ ((methodname == null) ? 0 : methodname.hashCode());
		result = prime * result + Arrays.hashCode(paramlist);
		result = prime * result + Arrays.hashCode(paramlistName);
		result = prime * result + ((tested == null) ? 0 : tested.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + (whenStepWorkIsInvoked ? 1231 : 1237);
		result = prime * result + (whenStepWorkIsProcessed ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WExternalMethod))
			return false;
		WExternalMethod other = (WExternalMethod) obj;
		if (processHead == null) {
			if (other.processHead != null)
				return false;
		} else if (!processHead.equals(other.processHead))
			return false;
		if (atProcessEnd != other.atProcessEnd)
			return false;
		if (atProcessStartup != other.atProcessStartup)
			return false;
		if (classname == null) {
			if (other.classname != null)
				return false;
		} else if (!classname.equals(other.classname))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (logicalConditionExecution == null) {
			if (other.logicalConditionExecution != null)
				return false;
		} else if (!logicalConditionExecution
				.equals(other.logicalConditionExecution))
			return false;
		if (methodname == null) {
			if (other.methodname != null)
				return false;
		} else if (!methodname.equals(other.methodname))
			return false;
		if (!Arrays.equals(paramlist, other.paramlist))
			return false;
		if (!Arrays.equals(paramlistName, other.paramlistName))
			return false;
		if (tested == null) {
			if (other.tested != null)
				return false;
		} else if (!tested.equals(other.tested))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (whenStepWorkIsInvoked != other.whenStepWorkIsInvoked)
			return false;
		if (whenStepWorkIsProcessed != other.whenStepWorkIsProcessed)
			return false;
		return true;
	}

	@Override
	public String toString() {
		final int maxLen = 2;
		return "WExternalMethod ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (processHead != null ? "processHead=" + processHead + ", "
						: "")
				+ (classname != null ? "classname=" + classname + ", " : "")
				+ (methodname != null ? "methodname=" + methodname + ", " : "")
				+ (type != null ? "type=" + type + ", " : "")
				+ (paramlist != null ? "paramlist="
						+ Arrays.asList(paramlist).subList(0,
								Math.min(paramlist.length, maxLen)) + ", " : "")
				+ (paramlistName != null ? "paramlistName="
						+ Arrays.asList(paramlistName).subList(0,
								Math.min(paramlistName.length, maxLen)) + ", "
						: "")
				+ (paramlistType != null ? "paramlistType="
						+ Arrays.asList(paramlistType).subList(0,
								Math.min(paramlistType.length, maxLen)) + ", "
						: "")
				+ "atProcessStartup="
				+ atProcessStartup
				+ ", atProcessEnd="
				+ atProcessEnd
				+ ", whenStepWorkIsInvoked="
				+ whenStepWorkIsInvoked
				+ ", whenStepWorkIsProcessed="
				+ whenStepWorkIsProcessed
				+ ", "
				+ (logicalConditionExecution != null ? "logicalConditionExecution="
						+ logicalConditionExecution + ", "
						: "")
				+ (tested != null ? "tested=" + tested + ", " : "")
				+ (insertDate != null ? "insertDate=" + insertDate + ", " : "")
				+ (insertUser != null ? "insertUser=" + insertUser + ", " : "")
				+ (modDate != null ? "modDate=" + modDate + ", " : "")
				+ (modUser != null ? "modUser=" + modUser : "") + "]";
	}

	
	
}
