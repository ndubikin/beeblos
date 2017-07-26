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
public class EmailDConfBeeBPM extends EmailDConf implements Serializable {

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
	
	public EmailDConfBeeBPM() {
		
	}
	
	/**
	 * This constructor is built like this because the query which is related with
	 * it is very easy ("WStepDefDao.getEmailDConfListByProcessAndStep")
	 * 
	 * @author dmuleiro 20150422
	 * 
	 * @param idProcessDef
	 * @param processDefName
	 * @param idStepDef
	 * @param stepDefName
	 * @param emailDConf
	 */
	public EmailDConfBeeBPM(Integer idProcessDef, String processDefName, Integer idStepDef, String stepDefName,
			EmailDConf emailDConf) {
		super();
		this.idStepDef = idStepDef;
		this.stepDefName = stepDefName;
		this.idProcessDef = idProcessDef;
		this.processDefName = processDefName;
		
		/**
		 * If EmailDConf is not null, we get all the super() fields and fill them.
		 * 
		 * This constructor is built like this because the query which is related with
		 * it is very easy ("WStepDefDao.getEmailDConfListByProcessAndStep")
		 * 
		 * @author dmuleiro 20150422
		 */
		super.setId(emailDConf!=null?emailDConf.getId():null);
		
		super.setEnabled(emailDConf!=null?emailDConf.isEnabled():null); 

		super.setEmailAccount(emailDConf!=null?emailDConf.getEmailAccount():null);

		super.setEmailTemplate(emailDConf!=null?emailDConf.getEmailTemplate():null);

		super.setPollingFrequency(emailDConf!=null?emailDConf.getPollingFrequency():null);
		
		super.setDaemonClassImplementationName(emailDConf!=null?emailDConf.getDaemonClassImplementationName():null);

		super.setDocClassId(emailDConf!=null?emailDConf.getDocClassId():null);
		super.setDocClassName(emailDConf!=null?emailDConf.getDocClassName():null);
		super.setBeeblosServerId(emailDConf!=null?emailDConf.getBeeblosServerId():null);
		
		super.setAddDate(emailDConf!=null?emailDConf.getAddDate():null);
		super.setAddUser(emailDConf!=null?emailDConf.getAddUser():null);
		super.setModDate(emailDConf!=null?emailDConf.getModDate():null);
		super.setModUser(emailDConf!=null?emailDConf.getModUser():null);

		super.setInputFolder(emailDConf!=null?emailDConf.getInputFolder():null);
		super.setMarkAsRead(emailDConf!=null?emailDConf.isMarkAsRead():null);
		super.setLeaveOnInputFolder(emailDConf!=null?emailDConf.isLeaveOnInputFolder():null);
		
		super.setValidEmailFolder(emailDConf!=null?emailDConf.getValidEmailFolder():null);
		super.setMoveToValidFolder(emailDConf!=null?emailDConf.isMoveToValidFolder():null);
		
		super.setInvalidEmailFolder(emailDConf!=null?emailDConf.getInvalidEmailFolder():null);
		super.setMoveToInvalidFolder(emailDConf!=null?emailDConf.isMoveToInvalidFolder():null);
		
		super.setErrorEmailFolder(emailDConf!=null?emailDConf.getErrorEmailFolder():null);
		super.setMoveToErrorFolder(emailDConf!=null?emailDConf.isMoveToErrorFolder():null);

		super.setCheckingFieldName(emailDConf!=null?emailDConf.getCheckingFieldName():null);
		super.setCheckingFieldValue(emailDConf!=null?emailDConf.getCheckingFieldValue():null);
		
		super.setType(emailDConf!=null?emailDConf.getType():null);

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

	@Override
	public String toString() {
		return "EmailDConfBeeBPM [idStepDef=" + idStepDef + ", stepDefName=" + stepDefName + ", processDefName=" + processDefName
				+ ", idProcessDef=" + idProcessDef + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
