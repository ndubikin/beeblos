package org.beeblos.bpm.core.model.bpmn;

import org.joda.time.DateTime;

import com.sp.daemon.email.EmailDConf;

/**
 * Defines the relation between a WStepTypeDef and a EmailDConf
 * 
 * @author dml 20150413
 *
 */
public class StepTypeDefEmailDConf {


	private Integer id;
	
	private Integer stepTypeDefId;
	
	private EmailDConf emailDConf;
	
	private DateTime addDate;
	private Integer addUser;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStepTypeDefId() {
		return stepTypeDefId;
	}
	public void setStepTypeDefId(Integer stepTypeDefId) {
		this.stepTypeDefId = stepTypeDefId;
	}
	public EmailDConf getEmailDConf() {
		return emailDConf;
	}
	public void setEmailDConf(EmailDConf emailDConf) {
		this.emailDConf = emailDConf;
	}
	public DateTime getAddDate() {
		return addDate;
	}
	public void setAddDate(DateTime addDate) {
		this.addDate = addDate;
	}
	public Integer getAddUser() {
		return addUser;
	}
	public void setAddUser(Integer addUser) {
		this.addUser = addUser;
	}
	@Override
	public String toString() {
		return "StepTypeDefEmailDConf [id=" + id 
				+ ", addDate=" + addDate + ", addUser=" + addUser + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((addDate == null) ? 0 : addDate.hashCode());
		result = prime * result + ((addUser == null) ? 0 : addUser.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		StepTypeDefEmailDConf other = (StepTypeDefEmailDConf) obj;
		if (addDate == null) {
			if (other.addDate != null)
				return false;
		} else if (!addDate.equals(other.addDate))
			return false;
		if (addUser == null) {
			if (other.addUser != null)
				return false;
		} else if (!addUser.equals(other.addUser))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
