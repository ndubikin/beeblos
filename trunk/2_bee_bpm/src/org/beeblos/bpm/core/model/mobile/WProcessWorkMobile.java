package org.beeblos.bpm.core.model.mobile;

public class WProcessWorkMobile {

	private Integer id;
	private String ref;
	private String name;
	private Integer taskNumberForUser;
	private String status;
	private String date;
	
	private Integer qtyTasks;
	private Integer qtyTasksHighPriority;
	private Integer qtyTasksLowPriority;
	
	public WProcessWorkMobile() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public WProcessWorkMobile(Integer id, String ref, String name, Integer taskNumberForUser, String status,
			String date, Integer qtyTasks, Integer qtyTasksHighPriority, Integer qtyTasksLowPriority) {
		super();
		this.id = id;
		this.ref = ref;
		this.name = name;
		this.taskNumberForUser = taskNumberForUser;
		this.status = status;
		this.date = date;
		this.qtyTasks = qtyTasks;
		this.qtyTasksHighPriority = qtyTasksHighPriority;
		this.qtyTasksLowPriority = qtyTasksLowPriority;
	}



	public WProcessWorkMobile(Integer id, String name, Integer taskNumberForUser, Integer qtyTasks,
			Integer qtyTasksHighPriority, Integer qtyTasksLowPriority) {
		super();
		this.id = id;
		this.name = name;
		this.taskNumberForUser = taskNumberForUser;
		this.qtyTasks = qtyTasks;
		this.qtyTasksHighPriority = qtyTasksHighPriority;
		this.qtyTasksLowPriority = qtyTasksLowPriority;
	}

	@Override
	public String toString() {
		return "WProcessWorkMobile [id=" + id + ", ref=" + ref + ", name=" + name + ", taskNumberForUser="
				+ taskNumberForUser + ", status=" + status + ", date=" + date + ", qtyTasks=" + qtyTasks
				+ ", qtyTasksHighPriority=" + qtyTasksHighPriority + ", qtyTasksLowPriority=" + qtyTasksLowPriority
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((qtyTasks == null) ? 0 : qtyTasks.hashCode());
		result = prime * result + ((qtyTasksHighPriority == null) ? 0 : qtyTasksHighPriority.hashCode());
		result = prime * result + ((qtyTasksLowPriority == null) ? 0 : qtyTasksLowPriority.hashCode());
		result = prime * result + ((ref == null) ? 0 : ref.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((taskNumberForUser == null) ? 0 : taskNumberForUser.hashCode());
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
		WProcessWorkMobile other = (WProcessWorkMobile) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (qtyTasks == null) {
			if (other.qtyTasks != null)
				return false;
		} else if (!qtyTasks.equals(other.qtyTasks))
			return false;
		if (qtyTasksHighPriority == null) {
			if (other.qtyTasksHighPriority != null)
				return false;
		} else if (!qtyTasksHighPriority.equals(other.qtyTasksHighPriority))
			return false;
		if (qtyTasksLowPriority == null) {
			if (other.qtyTasksLowPriority != null)
				return false;
		} else if (!qtyTasksLowPriority.equals(other.qtyTasksLowPriority))
			return false;
		if (ref == null) {
			if (other.ref != null)
				return false;
		} else if (!ref.equals(other.ref))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (taskNumberForUser == null) {
			if (other.taskNumberForUser != null)
				return false;
		} else if (!taskNumberForUser.equals(other.taskNumberForUser))
			return false;
		return true;
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

	public Integer getTaskNumberForUser() {
		return taskNumberForUser;
	}

	public void setTaskNumberForUser(Integer taskNumberForUser) {
		this.taskNumberForUser = taskNumberForUser;
	}

	public Integer getQtyTasks() {
		return qtyTasks;
	}
	public void setQtyTasks(Integer qtyTasks) {
		this.qtyTasks = qtyTasks;
	}
	public Integer getQtyTasksHighPriority() {
		return qtyTasksHighPriority;
	}
	public void setQtyTasksHighPriority(Integer qtyTasksHighPriority) {
		this.qtyTasksHighPriority = qtyTasksHighPriority;
	}
	public Integer getQtyTasksLowPriority() {
		return qtyTasksLowPriority;
	}
	public void setQtyTasksLowPriority(Integer qtyTasksLowPriority) {
		this.qtyTasksLowPriority = qtyTasksLowPriority;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
