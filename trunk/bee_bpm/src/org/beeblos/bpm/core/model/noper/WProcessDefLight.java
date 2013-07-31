package org.beeblos.bpm.core.model.noper;

import java.io.Serializable;
import java.util.Date;

public class WProcessDefLight implements Serializable {

	private Integer id;
	private String name;
	private String comments;
	private Date productionDate;
	private Integer productionUser;
	
	private Integer liveWorks;
	private Integer liveSteps;
	
	private boolean status;
	private Integer version;

	public WProcessDefLight () {
		
	}

	public WProcessDefLight(Integer id, String name, String comments, Date productionDate,
			Integer productionUser, Integer liveWorks, Integer liveSteps, boolean status,
			Integer version) {
		super();
		this.id = id;
		this.name = name;
		this.comments = comments;
		this.productionDate = productionDate;
		this.productionUser = productionUser;
		this.liveWorks = liveWorks;
		this.liveSteps = liveSteps;
		this.status = status;
		this.setVersion(version);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public Integer getProductionUser() {
		return productionUser;
	}

	public void setProductionUser(Integer productionUser) {
		this.productionUser = productionUser;
	}

	public Integer getLiveWorks() {
		return liveWorks;
	}

	public void setLiveWorks(Integer liveWorks) {
		this.liveWorks = liveWorks;
	}

	public Integer getLiveSteps() {
		return liveSteps;
	}

	public void setLiveSteps(Integer liveSteps) {
		this.liveSteps = liveSteps;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}	
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((liveSteps == null) ? 0 : liveSteps.hashCode());
		result = prime * result + ((liveWorks == null) ? 0 : liveWorks.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + ((productionUser == null) ? 0 : productionUser.hashCode());
		result = prime * result + (status ? 1231 : 1237);
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		WProcessDefLight other = (WProcessDefLight) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (liveSteps == null) {
			if (other.liveSteps != null)
				return false;
		} else if (!liveSteps.equals(other.liveSteps))
			return false;
		if (liveWorks == null) {
			if (other.liveWorks != null)
				return false;
		} else if (!liveWorks.equals(other.liveWorks))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (productionUser == null) {
			if (other.productionUser != null)
				return false;
		} else if (!productionUser.equals(other.productionUser))
			return false;
		if (status != other.status)
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WProcessDefLight [id=" + id + ", name=" + name + ", comments=" + comments
				+ ", productionDate=" + productionDate + ", productionUser=" + productionUser
				+ ", liveWorks=" + liveWorks + ", liveSteps=" + liveSteps + ", status=" + status
				+ ", version=" + version + "]";
	}
	
}
