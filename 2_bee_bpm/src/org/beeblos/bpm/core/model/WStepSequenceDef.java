package org.beeblos.bpm.core.model;

import static com.sp.common.util.ConstantsCommon.EMPTY_OBJECT;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.beeblos.bpm.core.graph.MxCell;
import org.joda.time.DateTime;

// Generated Oct 30, 2010 12:25:05 AM by Hibernate Tools 3.3.0.GA

/**
 * Routes from the steps to another steps or to the end of a process
 * Routes are the connections between steps and build the enabled ways
 * in a process 
 * A route may only connect two steps. Routes from or to a non-step 
 * are invalid or not permitted 
 * Routes indicates possible ways in the process
 * 
 * WStepSequenceDef generated by hbm2java
 */
@XmlAccessorType(XmlAccessType.NONE)
public class WStepSequenceDef implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * sequence def id
	 */
	private Integer id;
	
	/**
	 * indicates belonged process version 
	 */
	private WProcessDef process;
	/**
	 * Sequence name / label
	 */
	private String name;
	
	/**
	 * to order sequences (principally for evaluation purpose)
	 * nes 20130913
	 */
	private Integer order; 
	
	/**
	 * 
	 */
	private WStepDef fromStep;
	private WStepDef toStep;
	
	/**
	 * indicates if this route is enabled or disabled
	 */
	private boolean enabled;

	/**
	 * Indicates always the step is executed must run this route
	 */
	private boolean afterAll;
	
	/**
	 * Deleted route - normally enabled will be turn at false...
	 * dml 20130829 - si se intenta borrar y tiene "w_step_work_sequence" 
	 * asociados se marca como deleted para hacer PURGE en caso de que este activo (SOLO DESARROLLO)
	 */
	private boolean deleted; 
	
	/**
	 * optionally indicates the list of idResponse comma separated
	 */
	private String validResponses; 
	
	// 
	/**
	 * rules to apply this route - 20141025: not implemented yet
	 * dml 20130727
	 */
	private String rules;

	/**
	 * nes 20140207
	 * External methods allowed to be executed or invoked by this process
	 * Designer/Programmer responsibility to allow context class and method reachable
	 * at execution time 
	 * Invoking external method must be linked with sequence (routes), step (before
	 * load step, after executing step), process (at start time or at end process time), etc. 
	 * 
	 */
	private Set<WExternalMethod> externalMethod = new HashSet<WExternalMethod>(0);
	
	/**
	 * Xml variables
	 * pab 04122014
	 */
	private String xmlDescription;
	private String xmlId;
	private String xmlMxCellString;
	
	private MxCell mxCell;
	
	
	/**
	 * timestamps
	 */
	private Integer insertUser;
	private DateTime insertDate;
	private Integer modUser;
	private DateTime modDate;

	public WStepSequenceDef() {
	}


	public WStepSequenceDef(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			this.process = new WProcessDef( EMPTY_OBJECT );
			this.fromStep = new WStepDef( EMPTY_OBJECT );
			this.toStep = new WStepDef( EMPTY_OBJECT );
			
		}	
	}

	/**
	 * Creates a new StepSequence def (route) beginning in fromStep and ending in toStep
	 * 
	 * @param id
	 * @param process
	 * @param order
	 * @param fromStep
	 * @param toStep
	 * @param enabled
	 * @param afterAll
	 * @param deleted
	 * @param validResponses
	 * @param name
	 */
	public WStepSequenceDef(Integer id, WProcessDef process, Integer order,
			WStepDef fromStep, WStepDef toStep, boolean enabled,
			boolean afterAll, boolean deleted, String validResponses, String name) {
		super();
		this.id = id;
		this.process = process;
		this.order = order;
		this.fromStep = fromStep;
		this.toStep = toStep;
		this.enabled = enabled;
		this.afterAll = afterAll;
		this.deleted = deleted;
		this.validResponses = validResponses;
		this.name = name;
	}

	public WStepSequenceDef(WProcessDef process, Integer order,
			WStepDef fromStep, WStepDef toStep, boolean enabled,
			boolean afterAll, boolean deleted, String validResponses, String name) {
		super();
	
		this.process = process;
		this.order = order;
		this.fromStep = fromStep;
		this.toStep = toStep;
		this.enabled = enabled;
		this.afterAll = afterAll;
		this.deleted = deleted;
		this.validResponses = validResponses;
		this.name = name;
	}

	public WStepSequenceDef(Integer id) {
		super();
		this.id = id;
	}


	@XmlAttribute(name="spId")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	@XmlAttribute(name="label")
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Integer getOrder() {
		return order;
	}


	public void setOrder(Integer order) {
		this.order = order;
	}


	/**
	 * ProcessDef (version) belonging this route
	 * @return the process
	 */
	public WProcessDef getProcess() {
		return process;
	}



	/**
	 * ProcessDef (version) belonging this route
	 * @param process the process to set
	 */
	public void setProcess(WProcessDef process) {
		this.process = process;
	}



	/**
	 * @return the fromStep
	 */
	public WStepDef getFromStep() {
		return fromStep;
	}



	/**
	 * @param fromStep the fromStep to set
	 */
	public void setFromStep(WStepDef fromStep) {
		this.fromStep = fromStep;
	}



	/**
	 * @return the toStep
	 */
	public WStepDef getToStep() {
		return toStep;
	}



	/**
	 * @param toStep the toStep to set
	 */
	public void setToStep(WStepDef toStep) {
		this.toStep = toStep;
	}






	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}



	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}



	/**
	 * @return the afterAll
	 */
	public boolean isAfterAll() {
		return afterAll;
	}



	/**
	 * @param afterAll the afterAll to set
	 */
	public void setAfterAll(boolean afterAll) {
		this.afterAll = afterAll;
	}



	public boolean isDeleted() {
		return deleted;
	}


	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}


	/**
	 * @return the validResponses
	 */
	@XmlAttribute(name="responses")
	public String getValidResponses() {
		return validResponses;
	}



	/**
	 * @param validResponses the validResponses to set
	 */
	public void setValidResponses(String validResponses) {
		this.validResponses = validResponses;
	}



	public Set<WExternalMethod> getExternalMethod() {
		return externalMethod;
	}


	public void setExternalMethod(Set<WExternalMethod> externalMethod) {
		this.externalMethod = externalMethod;
	}


	@XmlAttribute(name="rules")
	public String getRules() {
		return rules;
	}


	public void setRules(String rules) {
		this.rules = rules;
	}


	public Integer getInsertUser() {
		return insertUser;
	}


	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}


	public DateTime getInsertDate() {
		return insertDate;
	}


	public void setInsertDate(DateTime insertDate) {
		this.insertDate = insertDate;
	}


	public Integer getModUser() {
		return modUser;
	}


	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}


	public DateTime getModDate() {
		return modDate;
	}


	public void setModDate(DateTime modDate) {
		this.modDate = modDate;
	}

	/**
	 * @author rrl 20141104
	 * 
	 * nullates empty objects to persist
	 */
	public void nullateEmtpyObjects() {

		if (process!=null && process.empty()) process=null;
		if (fromStep!=null && fromStep.empty()) fromStep=null;
		if (toStep!=null && toStep.empty()) toStep=null;
		if (externalMethod!=null && externalMethod.isEmpty()) externalMethod=null;
	}
	
	/**
	  * @author rrl 20141104
	  * 
	  * recover empty objects to persist
	  */
	public void recoverEmtpyObjects() {

		if (process==null) process = new WProcessDef();
		if (fromStep==null) fromStep = new WStepDef();
		if (toStep==null) toStep = new WStepDef();
		if (externalMethod==null) externalMethod = new HashSet<WExternalMethod>();
	}

    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (afterAll ? 1231 : 1237);
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result
				+ ((externalMethod == null) ? 0 : externalMethod.hashCode());
		result = prime * result
				+ ((fromStep == null) ? 0 : fromStep.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result
				+ ((insertUser == null) ? 0 : insertUser.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
		result = prime * result + ((mxCell == null) ? 0 : mxCell.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result + ((process == null) ? 0 : process.hashCode());
		result = prime * result + ((rules == null) ? 0 : rules.hashCode());
		result = prime * result + ((toStep == null) ? 0 : toStep.hashCode());
		result = prime * result
				+ ((validResponses == null) ? 0 : validResponses.hashCode());
		result = prime * result
				+ ((xmlDescription == null) ? 0 : xmlDescription.hashCode());
		result = prime * result + ((xmlId == null) ? 0 : xmlId.hashCode());
		result = prime * result
				+ ((xmlMxCellString == null) ? 0 : xmlMxCellString.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WStepSequenceDef))
			return false;
		WStepSequenceDef other = (WStepSequenceDef) obj;
		if (afterAll != other.afterAll)
			return false;
		if (deleted != other.deleted)
			return false;
		if (enabled != other.enabled)
			return false;
		if (fromStep == null) {
			if (other.fromStep != null)
				return false;
		} else if (!fromStep.equals(other.fromStep))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (insertDate == null) {
			if (other.insertDate != null)
				return false;
		} else if (!insertDate.equals(other.insertDate))
			return false;
		if (insertUser == null) {
			if (other.insertUser != null)
				return false;
		} else if (!insertUser.equals(other.insertUser))
			return false;
		if (modDate == null) {
			if (other.modDate != null)
				return false;
		} else if (!modDate.equals(other.modDate))
			return false;
		if (modUser == null) {
			if (other.modUser != null)
				return false;
		} else if (!modUser.equals(other.modUser))
			return false;
		if (mxCell == null) {
			if (other.mxCell != null)
				return false;
		} else if (!mxCell.equals(other.mxCell))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (process == null) {
			if (other.process != null)
				return false;
		} else if (!process.equals(other.process))
			return false;
		if (rules == null) {
			if (other.rules != null)
				return false;
		} else if (!rules.equals(other.rules))
			return false;
		if (toStep == null) {
			if (other.toStep != null)
				return false;
		} else if (!toStep.equals(other.toStep))
			return false;
		if (validResponses == null) {
			if (other.validResponses != null)
				return false;
		} else if (!validResponses.equals(other.validResponses))
			return false;
		if (xmlDescription == null) {
			if (other.xmlDescription != null)
				return false;
		} else if (!xmlDescription.equals(other.xmlDescription))
			return false;
		if (xmlId == null) {
			if (other.xmlId != null)
				return false;
		} else if (!xmlId.equals(other.xmlId))
			return false;
		if (xmlMxCellString == null) {
			if (other.xmlMxCellString != null)
				return false;
		} else if (!xmlMxCellString.equals(other.xmlMxCellString))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WStepSequenceDef [id=" + id + ", process=" + process
				+ ", name=" + name + ", order=" + order + ", fromStep="
				+ fromStep + ", toStep=" + toStep + ", enabled=" + enabled
				+ ", afterAll=" + afterAll + ", deleted=" + deleted
				+ ", validResponses=" + validResponses + ", rules=" + rules
				+ ", externalMethod=" + externalMethod + ", insertUser="
				+ insertUser + ", insertDate=" + insertDate + ", modUser="
				+ modUser + ", modDate=" + modDate + "]";
	}


	/**
	 * @return the xmlDescription
	 */
	@XmlAttribute(name="description")
	public String getXmlDescription() {
		return xmlDescription;
	}


	/**
	 * @param xmlDescription the xmlDescription to set
	 */	
	public void setXmlDescription(String xmlDescription) {
		this.xmlDescription = xmlDescription;
	}


	/**
	 * @return the xmlId
	 */
	@XmlAttribute(name="id")
	public String getXmlId() {
		return xmlId;
	}


	/**
	 * @param xmlId the xmlId to set
	 */
	public void setXmlId(String xmlId) {
		this.xmlId = xmlId;
	}


	/**
	 * @return the xmlMxCellString
	 */
	public String getXmlMxCellString() {
		return xmlMxCellString;
	}


	/**
	 * @param xmlMxCellString the xmlMxCellString to set
	 */
	public void setXmlMxCellString(String xmlMxCellString) {
		this.xmlMxCellString = xmlMxCellString;
	}


	/**
	 * @return the mxCell
	 */
	@XmlElement(name="mxCell")
	public MxCell getMxCell() {
		return mxCell;
	}


	/**
	 * @param mxCell the mxCell to set
	 */
	public void setMxCell(MxCell mxCell) {
		this.mxCell = mxCell;
	}
}
