package org.beeblos.bpm.core.model.noper;

import java.util.Date;

/**
 * thin object to check current step work conditions ...
 * 
 * @author nes 20140208
 * 
 */
public class WStepWorkCheckObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	/**
	 * id to header process work
	 * 
	 */
	private Integer idProcessWork;

	private Integer idPreviousStep;
	private Integer idCurrentStep;

	private Date arrivingDate;
	private Date openedDate;

	private Integer idOpenerUser;

	private Date decidedDate;
	private Integer idPerformer;

	private String response;

	private boolean adminProcess;

	private boolean locked;
	private Integer lockedByUserId; // id del usuario que lo bloqueó
	private Date lockedSince;

	private boolean sentBack;

	private Integer idInsertUser; // user has processed previous step ( and
									// inserted current step )
	private Date modDate;
	private Integer idModUser;

	public WStepWorkCheckObject() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdProcessWork() {
		return idProcessWork;
	}

	public void setIdProcessWork(Integer idProcessWork) {
		this.idProcessWork = idProcessWork;
	}

	public Integer getIdPreviousStep() {
		return idPreviousStep;
	}

	public void setIdPreviousStep(Integer idPreviousStep) {
		this.idPreviousStep = idPreviousStep;
	}

	public Integer getIdCurrentStep() {
		return idCurrentStep;
	}

	public void setIdCurrentStep(Integer idCurrentStep) {
		this.idCurrentStep = idCurrentStep;
	}

	public Date getArrivingDate() {
		return arrivingDate;
	}

	public void setArrivingDate(Date arrivingDate) {
		this.arrivingDate = arrivingDate;
	}

	public Date getOpenedDate() {
		return openedDate;
	}

	public void setOpenedDate(Date openedDate) {
		this.openedDate = openedDate;
	}

	public Integer getIdOpenerUser() {
		return idOpenerUser;
	}

	public void setIdOpenerUser(Integer idOpenerUser) {
		this.idOpenerUser = idOpenerUser;
	}

	public Date getDecidedDate() {
		return decidedDate;
	}

	public void setDecidedDate(Date decidedDate) {
		this.decidedDate = decidedDate;
	}

	public Integer getIdPerformer() {
		return idPerformer;
	}

	public void setIdPerformer(Integer idPerformer) {
		this.idPerformer = idPerformer;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public boolean isAdminProcess() {
		return adminProcess;
	}

	public void setAdminProcess(boolean adminProcess) {
		this.adminProcess = adminProcess;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public Integer getLockedByUserId() {
		return lockedByUserId;
	}

	public void setLockedByUserId(Integer lockedByUserId) {
		this.lockedByUserId = lockedByUserId;
	}

	public Date getLockedSince() {
		return lockedSince;
	}

	public void setLockedSince(Date lockedSince) {
		this.lockedSince = lockedSince;
	}

	public boolean isSentBack() {
		return sentBack;
	}

	public void setSentBack(boolean sentBack) {
		this.sentBack = sentBack;
	}

	public Integer getIdInsertUser() {
		return idInsertUser;
	}

	public void setIdInsertUser(Integer idInsertUser) {
		this.idInsertUser = idInsertUser;
	}

	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	public Integer getIdModUser() {
		return idModUser;
	}

	public void setIdModUser(Integer idModUser) {
		this.idModUser = idModUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (adminProcess ? 1231 : 1237);
		result = prime * result
				+ ((arrivingDate == null) ? 0 : arrivingDate.hashCode());
		result = prime * result
				+ ((decidedDate == null) ? 0 : decidedDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((idCurrentStep == null) ? 0 : idCurrentStep.hashCode());
		result = prime * result
				+ ((idInsertUser == null) ? 0 : idInsertUser.hashCode());
		result = prime * result
				+ ((idModUser == null) ? 0 : idModUser.hashCode());
		result = prime * result
				+ ((idOpenerUser == null) ? 0 : idOpenerUser.hashCode());
		result = prime * result
				+ ((idPerformer == null) ? 0 : idPerformer.hashCode());
		result = prime * result
				+ ((idPreviousStep == null) ? 0 : idPreviousStep.hashCode());
		result = prime * result
				+ ((idProcessWork == null) ? 0 : idProcessWork.hashCode());
		result = prime * result + (locked ? 1231 : 1237);
		result = prime * result
				+ ((lockedByUserId == null) ? 0 : lockedByUserId.hashCode());
		result = prime * result
				+ ((lockedSince == null) ? 0 : lockedSince.hashCode());
		result = prime * result + ((modDate == null) ? 0 : modDate.hashCode());
		result = prime * result
				+ ((openedDate == null) ? 0 : openedDate.hashCode());
		result = prime * result
				+ ((response == null) ? 0 : response.hashCode());
		result = prime * result + (sentBack ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WStepWorkCheckObject))
			return false;
		WStepWorkCheckObject other = (WStepWorkCheckObject) obj;
		if (adminProcess != other.adminProcess)
			return false;
		if (arrivingDate == null) {
			if (other.arrivingDate != null)
				return false;
		} else if (!arrivingDate.equals(other.arrivingDate))
			return false;
		if (decidedDate == null) {
			if (other.decidedDate != null)
				return false;
		} else if (!decidedDate.equals(other.decidedDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idCurrentStep == null) {
			if (other.idCurrentStep != null)
				return false;
		} else if (!idCurrentStep.equals(other.idCurrentStep))
			return false;
		if (idInsertUser == null) {
			if (other.idInsertUser != null)
				return false;
		} else if (!idInsertUser.equals(other.idInsertUser))
			return false;
		if (idModUser == null) {
			if (other.idModUser != null)
				return false;
		} else if (!idModUser.equals(other.idModUser))
			return false;
		if (idOpenerUser == null) {
			if (other.idOpenerUser != null)
				return false;
		} else if (!idOpenerUser.equals(other.idOpenerUser))
			return false;
		if (idPerformer == null) {
			if (other.idPerformer != null)
				return false;
		} else if (!idPerformer.equals(other.idPerformer))
			return false;
		if (idPreviousStep == null) {
			if (other.idPreviousStep != null)
				return false;
		} else if (!idPreviousStep.equals(other.idPreviousStep))
			return false;
		if (idProcessWork == null) {
			if (other.idProcessWork != null)
				return false;
		} else if (!idProcessWork.equals(other.idProcessWork))
			return false;
		if (locked != other.locked)
			return false;
		if (lockedByUserId == null) {
			if (other.lockedByUserId != null)
				return false;
		} else if (!lockedByUserId.equals(other.lockedByUserId))
			return false;
		if (lockedSince == null) {
			if (other.lockedSince != null)
				return false;
		} else if (!lockedSince.equals(other.lockedSince))
			return false;
		if (modDate == null) {
			if (other.modDate != null)
				return false;
		} else if (!modDate.equals(other.modDate))
			return false;
		if (openedDate == null) {
			if (other.openedDate != null)
				return false;
		} else if (!openedDate.equals(other.openedDate))
			return false;
		if (response == null) {
			if (other.response != null)
				return false;
		} else if (!response.equals(other.response))
			return false;
		if (sentBack != other.sentBack)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WStepWorkCheckObject ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (idProcessWork != null ? "idProcessWork=" + idProcessWork
						+ ", " : "")
				+ (idPreviousStep != null ? "idPreviousStep=" + idPreviousStep
						+ ", " : "")
				+ (idCurrentStep != null ? "idCurrentStep=" + idCurrentStep
						+ ", " : "")
				+ (arrivingDate != null ? "arrivingDate=" + arrivingDate + ", "
						: "")
				+ (openedDate != null ? "openedDate=" + openedDate + ", " : "")
				+ (idOpenerUser != null ? "idOpenerUser=" + idOpenerUser + ", "
						: "")
				+ (decidedDate != null ? "decidedDate=" + decidedDate + ", "
						: "")
				+ (idPerformer != null ? "idPerformer=" + idPerformer + ", "
						: "")
				+ (response != null ? "response=" + response + ", " : "")
				+ "adminProcess="
				+ adminProcess
				+ ", locked="
				+ locked
				+ ", "
				+ (lockedByUserId != null ? "lockedByUserId=" + lockedByUserId
						+ ", " : "")
				+ (lockedSince != null ? "lockedSince=" + lockedSince + ", "
						: "")
				+ "sentBack="
				+ sentBack
				+ ", "
				+ (idInsertUser != null ? "idInsertUser=" + idInsertUser + ", "
						: "")
				+ (modDate != null ? "modDate=" + modDate + ", " : "")
				+ (idModUser != null ? "idModUser=" + idModUser : "") + "]";
	}

}
