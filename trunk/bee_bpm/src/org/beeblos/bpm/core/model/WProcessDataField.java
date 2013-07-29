package org.beeblos.bpm.core.model;

// Generated Jul 29, 2013 1:15:55 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * WProcessDataField generated by hbm2java
 */
public class WProcessDataField implements java.io.Serializable {

	private Integer id;
	private WProcessHead WProcessHead;
	private WDataType WDataType;
	private String name;
	private Boolean required;
	private Integer dataType;
	private String comments;
	private Date insertDate;
	private int insertUser;
	private Date modDate;
	private int modUser;

	public WProcessDataField() {
	}

	public WProcessDataField(WProcessHead WProcessHead, WDataType WDataType,
			Date insertDate, int insertUser, Date modDate, int modUser) {
		this.WProcessHead = WProcessHead;
		this.WDataType = WDataType;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.modDate = modDate;
		this.modUser = modUser;
	}

	public WProcessDataField(WProcessHead WProcessHead, WDataType WDataType,
			String name, Boolean required, Integer dataType, String comments,
			Date insertDate, int insertUser, Date modDate, int modUser) {
		this.WProcessHead = WProcessHead;
		this.WDataType = WDataType;
		this.name = name;
		this.required = required;
		this.dataType = dataType;
		this.comments = comments;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.modDate = modDate;
		this.modUser = modUser;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public WProcessHead getWProcessHead() {
		return this.WProcessHead;
	}

	public void setWProcessHead(WProcessHead WProcessHead) {
		this.WProcessHead = WProcessHead;
	}

	public WDataType getWDataType() {
		return this.WDataType;
	}

	public void setWDataType(WDataType WDataType) {
		this.WDataType = WDataType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getRequired() {
		return this.required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Integer getDataType() {
		return this.dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public int getInsertUser() {
		return this.insertUser;
	}

	public void setInsertUser(int insertUser) {
		this.insertUser = insertUser;
	}

	public Date getModDate() {
		return this.modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	public int getModUser() {
		return this.modUser;
	}

	public void setModUser(int modUser) {
		this.modUser = modUser;
	}

}
