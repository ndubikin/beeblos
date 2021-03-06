package org.beeblos.bpm.core.model;

import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;

import java.util.Date;

// Generated Oct 30, 2010 12:25:05 AM by Hibernate Endols 3.3.0.GA

/**
 * WStepSequenceDef generated by hbm2java
 */
public class WStepWorkSequence implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private WStepSequenceDef stepSequence;
	private WStepWork stepWork;
	
	private boolean sentBack;
	
	private WStepDef beginStep;
	private WStepDef endStep;

	private Date executionDate;

	private Integer insertUser;
	private Date insertDate;
	private Integer modUser;
	private Date modDate;

	public WStepWorkSequence() {
	}


	public WStepWorkSequence(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			this.stepSequence=new WStepSequenceDef( EMPTY_OBJECT );
			this.stepWork=new WStepWork( EMPTY_OBJECT );

			this.beginStep=new WStepDef( EMPTY_OBJECT );
			this.endStep=new WStepDef( EMPTY_OBJECT );

		}	
	}


	public WStepWorkSequence(Integer id, WStepSequenceDef stepSequence, WStepWork stepWork,
			boolean sentBack, WStepDef beginStep, WStepDef endStep, Date executionDate,
			Integer insertUser, Date insertDate, Integer modUser, Date modDate) {
		super();
		this.id = id;
		this.stepSequence = stepSequence;
		this.stepWork = stepWork;
		this.sentBack = sentBack;
		this.beginStep = beginStep;
		this.endStep = endStep;
		this.executionDate = executionDate;
		this.insertUser = insertUser;
		this.insertDate = insertDate;
		this.modUser = modUser;
		this.modDate = modDate;
	}

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public WStepSequenceDef getStepSequence() {
		return stepSequence;
	}


	public void setStepSequence(WStepSequenceDef stepSequence) {
		this.stepSequence = stepSequence;
	}


	public WStepWork getStepWork() {
		return stepWork;
	}


	public void setStepWork(WStepWork stepWork) {
		this.stepWork = stepWork;
	}


	public boolean isSentBack() {
		return sentBack;
	}


	public void setSentBack(boolean sentBack) {
		this.sentBack = sentBack;
	}


	public WStepDef getBeginStep() {
		return beginStep;
	}


	public void setBeginStep(WStepDef beginStep) {
		this.beginStep = beginStep;
	}


	public WStepDef getEndStep() {
		return endStep;
	}


	public void setEndStep(WStepDef endStep) {
		this.endStep = endStep;
	}


	public Date getExecutionDate() {
		return executionDate;
	}


	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}


	public Integer getInsertUser() {
		return insertUser;
	}


	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}


	public Date getInsertDate() {
		return insertDate;
	}


	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}


	public Integer getModUser() {
		return modUser;
	}


	public void setModUser(Integer modUser) {
		this.modUser = modUser;
	}


	public Date getModDate() {
		return modDate;
	}


	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((executionDate == null) ? 0 : executionDate.hashCode());
		result = prime * result + ((beginStep == null) ? 0 : beginStep.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result + ((insertUser == null) ? 0 : insertUser.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result + ((modUser == null) ? 0 : modUser.hashCode());
		result = prime * result + (sentBack ? 1231 : 1237);
		result = prime * result + ((stepSequence == null) ? 0 : stepSequence.hashCode());
		result = prime * result + ((stepWork == null) ? 0 : stepWork.hashCode());
		result = prime * result + ((endStep == null) ? 0 : endStep.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WStepWorkSequence other = (WStepWorkSequence) obj;
		if (executionDate == null) {
			if (other.executionDate != null)
				return false;
		} else if (!executionDate.equals(other.executionDate))
			return false;
		if (beginStep == null) {
			if (other.beginStep != null)
				return false;
		} else if (!beginStep.equals(other.beginStep))
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
		if (sentBack != other.sentBack)
			return false;
		if (stepSequence == null) {
			if (other.stepSequence != null)
				return false;
		} else if (!stepSequence.equals(other.stepSequence))
			return false;
		if (stepWork == null) {
			if (other.stepWork != null)
				return false;
		} else if (!stepWork.equals(other.stepWork))
			return false;
		if (endStep == null) {
			if (other.endStep != null)
				return false;
		} else if (!endStep.equals(other.endStep))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "WStepWorkSequence [id=" + id + ", stepSequence=" + stepSequence + ", stepWork="
				+ stepWork + ", sentBack=" + sentBack + ", beginStep=" + beginStep + ", endStep="
				+ endStep + ", executionDate=" + executionDate + ", insertUser=" + insertUser
				+ ", insertDate=" + insertDate + ", modUser=" + modUser + ", modDate=" + modDate
				+ "]";
	}

}
