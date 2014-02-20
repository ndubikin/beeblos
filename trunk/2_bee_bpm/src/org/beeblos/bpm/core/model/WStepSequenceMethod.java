package org.beeblos.bpm.core.model;

// Generated Nov 9, 2011 1:15:47 PM by Hibernate Tools 3.4.0.CR1

import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;

import java.util.Date;

/**
 * WStepSequenceMethod generated by hbm2java
 * 
 */
public class WStepSequenceMethod implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private WStepSequenceDef stepSequenceDef;
	private WExternalMethod externalMethod;
	private boolean enabled;
	private Integer insertUser;
	private Date insertDate;

	public WStepSequenceMethod() {
		super();
	}
	
	public WStepSequenceMethod(Integer idStepSequence, Integer idWExternalMethod) {
		this.stepSequenceDef=new WStepSequenceDef( idStepSequence );
		this.externalMethod = new WExternalMethod( idWExternalMethod );
	}
	
	public WStepSequenceMethod(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			this.stepSequenceDef=new WStepSequenceDef( EMPTY_OBJECT );
			this.externalMethod = new WExternalMethod( EMPTY_OBJECT );

		}	
	}

	public WStepSequenceMethod(WStepSequenceDef stepSequenceDef, WExternalMethod externalMethod, boolean enabled, Integer insertUser,
			Date insertDate) {
		this.stepSequenceDef = stepSequenceDef;
		this.externalMethod = externalMethod;
		this.enabled = enabled;
		this.insertUser = insertUser;
		this.insertDate = insertDate;
	}

	public WStepSequenceMethod(boolean enabled, Integer insertUser, Date insertDate) {
		this.enabled = enabled;
		this.insertUser = insertUser;
		this.insertDate = insertDate;
	}

	public WStepSequenceDef getStepSequenceDef() {
		return stepSequenceDef;
	}

	public void setStepSequenceDef(WStepSequenceDef StepSequenceDefSequenceDef) {
		this.stepSequenceDef = stepSequenceDef;
	}

	public WExternalMethod getExternalMethod() {
		return externalMethod;
	}

	public void setExternalMethod(WExternalMethod externalMethod) {
		this.externalMethod = externalMethod;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getInsertUser() {
		return this.insertUser;
	}

	public void setInsertUser(Integer insertUser) {
		this.insertUser = insertUser;
	}

	public Date getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (enabled ? 1231 : 1237);
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
		WStepSequenceMethod other = (WStepSequenceMethod) obj;
		if (enabled != other.enabled)
			return false;
		return true;
	}

	public boolean empty() {

		if (stepSequenceDef!=null && stepSequenceDef.getId() != null) return false;
		if (externalMethod!=null && externalMethod.getId() != null) return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "WStepSequenceMethod [" + "enabled="
				+ enabled + ", insertStepSequenceDef=" + insertUser + ", "
				+ (insertDate != null ? "insertDate=" + insertDate : "") + "]";
	}
	
	
}