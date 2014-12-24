package org.beeblos.bpm.core.util;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

public class UserSessionFactory implements Serializable{
	
	private static final long serialVersionUID = 1027098774809408140L;

	private static final Log logger = LogFactory.getLog(UserSessionFactory.class);
	
	private SessionFactory sessionFactory;
	private String connectionName;
	
	private String appName;
	
	private Set<Integer> userIdList = new HashSet<Integer>();
	
	
	public UserSessionFactory() {
		super();

	}

	public UserSessionFactory(SessionFactory sessionFactory, String connectionName, 
			String appName, Set<Integer> userIdList) {
		super();
		this.sessionFactory = sessionFactory;
		this.connectionName = connectionName;
		this.appName = appName;
		this.userIdList = userIdList;
	}

	public UserSessionFactory(SessionFactory sessionFactory, String connectionName, 
			String appName, Integer userId) {
		super();
		this.sessionFactory = sessionFactory;
		this.connectionName = connectionName;
		this.appName = appName;
		this.userIdList = new HashSet<Integer>();
		this.userIdList.add(userId);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public Set<Integer> getUserIdList() {
		return userIdList;
	}


	public void setUserIdList(Set<Integer> userIdList) {
		this.userIdList = userIdList;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public String toString() {
		return "UserSessionFactory [sessionFactory=" + sessionFactory + ", connectionName="
				+ connectionName + ", appName=" + appName + ", userIdList=" + userIdList + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appName == null) ? 0 : appName.hashCode());
		result = prime * result + ((connectionName == null) ? 0 : connectionName.hashCode());
		result = prime * result + ((sessionFactory == null) ? 0 : sessionFactory.hashCode());
		result = prime * result + ((userIdList == null) ? 0 : userIdList.hashCode());
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
		UserSessionFactory other = (UserSessionFactory) obj;
		if (appName == null) {
			if (other.appName != null)
				return false;
		} else if (!appName.equals(other.appName))
			return false;
		if (connectionName == null) {
			if (other.connectionName != null)
				return false;
		} else if (!connectionName.equals(other.connectionName))
			return false;
		if (sessionFactory == null) {
			if (other.sessionFactory != null)
				return false;
		} else if (!sessionFactory.equals(other.sessionFactory))
			return false;
		if (userIdList == null) {
			if (other.userIdList != null)
				return false;
		} else if (!userIdList.equals(other.userIdList))
			return false;
		return true;
	}

}