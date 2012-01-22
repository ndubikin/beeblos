package org.beeblos.bpm.core.model;

// Generated Jan 20, 2012 7:08:40 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * WProcessWork generated by hbm2java
 */
public class WProcessWork implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private int version;
	private WProcessStatus status;
	private int idProcess;
	private Date startingTime;
	private Integer startingType;
	private Date endTime;
	private String reference;
	private String comments;
	private int insertUser;
	private Date insertDate;
	private int modUser;
	private Date modDate;
	
	private Set<WStepWork> steps = new HashSet<WStepWork>();
	

	public WProcessWork() {
	}

	public WProcessWork(WProcessStatus status, int idProcess,
			Date startingTime, String reference, int insertUser,
			Date insertDate, int modUser, Date modDate) {
		this.status = status;
		this.idProcess = idProcess;
		this.startingTime = startingTime;
		this.reference = reference;
		this.insertUser = insertUser;
		this.insertDate = insertDate;
		this.modUser = modUser;
		this.modDate = modDate;
	}

	public WProcessWork(WProcessStatus status, int idProcess,
			Date startingTime, Integer startingType, Date endTime,
			String reference, String comments, int insertUser, Date insertDate,
			int modUser, Date modDate) {
		this.status = status;
		this.idProcess = idProcess;
		this.startingTime = startingTime;
		this.startingType = startingType;
		this.endTime = endTime;
		this.reference = reference;
		this.comments = comments;
		this.insertUser = insertUser;
		this.insertDate = insertDate;
		this.modUser = modUser;
		this.modDate = modDate;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public WProcessStatus getStatus() {
		return status;
	}

	public void setStatus(WProcessStatus status) {
		this.status = status;
	}

	public int getIdProcess() {
		return this.idProcess;
	}

	public void setIdProcess(int idProcess) {
		this.idProcess = idProcess;
	}

	public Date getStartingTime() {
		return this.startingTime;
	}

	public void setStartingTime(Date startingTime) {
		this.startingTime = startingTime;
	}

	public Integer getStartingType() {
		return this.startingType;
	}

	public void setStartingType(Integer startingType) {
		this.startingType = startingType;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getInsertUser() {
		return this.insertUser;
	}

	public void setInsertUser(int insertUser) {
		this.insertUser = insertUser;
	}

	public Date getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public int getModUser() {
		return this.modUser;
	}

	public void setModUser(int modUser) {
		this.modUser = modUser;
	}

	public Date getModDate() {
		return this.modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	public Set<WStepWork> getSteps() {
		return steps;
	}

	public void setSteps(Set<WStepWork> steps) {
		this.steps = steps;
	}

	
	
}