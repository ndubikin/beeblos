package org.beeblos.bpm.core.model.mobile;

import java.io.Serializable;

public class WStepResponseDefMobile implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer respOrder;
	private String name;
	
	public WStepResponseDefMobile() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WStepResponseDefMobile(Integer id, Integer respOrder, String name) {
		super();
		this.id = id;
		this.respOrder = respOrder;
		this.name = name;
	}
	@Override
	public String toString() {
		return "WStepResponseWorkMobile [id=" + id + ", respOrder=" + respOrder + ", name=" + name + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((respOrder == null) ? 0 : respOrder.hashCode());
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
		WStepResponseDefMobile other = (WStepResponseDefMobile) obj;
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
		if (respOrder == null) {
			if (other.respOrder != null)
				return false;
		} else if (!respOrder.equals(other.respOrder))
			return false;
		return true;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRespOrder() {
		return respOrder;
	}
	public void setRespOrder(Integer respOrder) {
		this.respOrder = respOrder;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
