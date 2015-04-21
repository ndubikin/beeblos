package org.beeblos.bpm.core.model.noper;

import java.io.Serializable;

import com.sp.daemon.email.EmailDConf;

/**
 * This object stores in memory (it doesn't have a table in DB) the relation between EmailDConfs
 * and process/step.
 * 
 * It is used in the "Daemon thread" view
 * 
 * @author dmuleiro 20150421
 */
public class EmailDConfBeeBPM implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * WStepDef id for this EmailDConf
	 */
	private Integer idStepDef;

	/**
	 * WStepDef name for this EmailDConf
	 */
	private String stepDefName;

	/**
	 * WProcessDef id for this EmailDConf
	 */
	private Integer idProcessDef;

	/**
	 * WProcessDef name for this EmailDConf
	 */
	private String processDefName;
	
	private EmailDConf emailDConf;

	
	public EmailDConfBeeBPM() {
		
	}
	
	public EmailDConfBeeBPM(Integer idProcessDef, String processDefName, Integer idStepDef, String stepDefName,
			EmailDConf emailDConf) {
		super();
		this.idStepDef = idStepDef;
		this.stepDefName = stepDefName;
		this.idProcessDef = idProcessDef;
		this.processDefName = processDefName;
		this.emailDConf = emailDConf;
	}



	/**
	 * @return the processDefName
	 */
	public String getProcessDefName() {
		return processDefName;
	}

	/**
	 * @param processDefName the processDefName to set
	 */
	public void setProcessDefName(String processDefName) {
		this.processDefName = processDefName;
	}

	public Integer getIdProcessDef() {
		return idProcessDef;
	}

	public void setIdProcessDef(Integer idProcessDef) {
		this.idProcessDef = idProcessDef;
	}

	public Integer getIdStepDef() {
		return idStepDef;
	}

	public void setIdStepDef(Integer idStepDef) {
		this.idStepDef = idStepDef;
	}

	public String getStepDefName() {
		return stepDefName;
	}

	public void setStepDefName(String stepDefName) {
		this.stepDefName = stepDefName;
	}

	public EmailDConf getEmailDConf() {
		return emailDConf;
	}

	public void setEmailDConf(EmailDConf emailDConf) {
		this.emailDConf = emailDConf;
	}

	@Override
	public String toString() {
		return "EmailDConfBeeBPM [idStepDef=" + idStepDef + ", stepDefName=" + stepDefName + ", processDefName=" + processDefName
				+ ", idProcessDef=" + idProcessDef + ", emailDConf=" + emailDConf + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emailDConf == null) ? 0 : emailDConf.hashCode());
		result = prime * result + ((idProcessDef == null) ? 0 : idProcessDef.hashCode());
		result = prime * result + ((idStepDef == null) ? 0 : idStepDef.hashCode());
		result = prime * result + ((processDefName == null) ? 0 : processDefName.hashCode());
		result = prime * result + ((stepDefName == null) ? 0 : stepDefName.hashCode());
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
		EmailDConfBeeBPM other = (EmailDConfBeeBPM) obj;
		if (emailDConf == null) {
			if (other.emailDConf != null)
				return false;
		} else if (!emailDConf.equals(other.emailDConf))
			return false;
		if (idProcessDef == null) {
			if (other.idProcessDef != null)
				return false;
		} else if (!idProcessDef.equals(other.idProcessDef))
			return false;
		if (idStepDef == null) {
			if (other.idStepDef != null)
				return false;
		} else if (!idStepDef.equals(other.idStepDef))
			return false;
		if (processDefName == null) {
			if (other.processDefName != null)
				return false;
		} else if (!processDefName.equals(other.processDefName))
			return false;
		if (stepDefName == null) {
			if (other.stepDefName != null)
				return false;
		} else if (!stepDefName.equals(other.stepDefName))
			return false;
		return true;
	}

}
