package org.beeblos.bpm.core.model.noper;

import com.sp.daemon.email.EmailDPoll;

/**
 * This object is an specialization of "EmailDaemonInfo" because in BeeBPM we are going to use EmailDConfBeeBPM
 * 
 * It is used in the "Daemon thread" view
 * 
 * @author dmuleiro 20150421
 */
public class EmailDaemonInfoBeeBPM implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5542377299723895973L;

	private EmailDConfBeeBPM emailDConf;
	private EmailDPoll emailDPoll;
	
	private boolean refresh;
	
	public EmailDaemonInfoBeeBPM() {}

	public EmailDaemonInfoBeeBPM(boolean createEmptyObjects) {
		
		if (createEmptyObjects){
			this.setEmailDConf(new EmailDConfBeeBPM());
			this.setEmailDPoll(new EmailDPoll());
		}
		
	}

	public EmailDaemonInfoBeeBPM(EmailDConfBeeBPM emailDConf,
			EmailDPoll emailDPoll, boolean refresh) {
		super();
		this.emailDConf = emailDConf;
		this.emailDPoll = emailDPoll;
		this.refresh = refresh;
	}

	public EmailDConfBeeBPM getEmailDConf() {
		return emailDConf;
	}

	public void setEmailDConf(EmailDConfBeeBPM emailDConf) {
		this.emailDConf = emailDConf;
	}

	public EmailDPoll getEmailDPoll() {
		return emailDPoll;
	}

	public void setEmailDPoll(EmailDPoll emailDPoll) {
		this.emailDPoll = emailDPoll;
	}

	public boolean isRefresh() {
		return refresh;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

	@Override
	public String toString() {
		return "EmailDaemonInfoBeeBPM [emailDConf=" + emailDConf + ", emailDPoll=" + emailDPoll + ", refresh="
				+ refresh + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emailDConf == null) ? 0 : emailDConf.hashCode());
		result = prime * result + ((emailDPoll == null) ? 0 : emailDPoll.hashCode());
		result = prime * result + (refresh ? 1231 : 1237);
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
		EmailDaemonInfoBeeBPM other = (EmailDaemonInfoBeeBPM) obj;
		if (emailDConf == null) {
			if (other.emailDConf != null)
				return false;
		} else if (!emailDConf.equals(other.emailDConf))
			return false;
		if (emailDPoll == null) {
			if (other.emailDPoll != null)
				return false;
		} else if (!emailDPoll.equals(other.emailDPoll))
			return false;
		if (refresh != other.refresh)
			return false;
		return true;
	}


}
