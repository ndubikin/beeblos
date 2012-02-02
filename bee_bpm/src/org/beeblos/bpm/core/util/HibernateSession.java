package org.beeblos.bpm.core.util;

import java.util.ResourceBundle;

public class HibernateSession {

	private String sessionName;
	private String driverName;
	private String password;
	private String username;
	private String url;
	private String defaultCatalog;

	public HibernateSession() {

	}

	public HibernateSession(String sessionName, String driverName,
			String password, String username,
			String url, String defaultCatalog) {
		super();
		this.sessionName = sessionName;
		this.driverName = driverName;
		this.password = password;
		this.username = username;
		this.url = url;
		this.defaultCatalog = defaultCatalog;
	}

	public static HibernateSession loadDefaultHibernateSessionParameters() {

		ResourceBundle rb = ResourceBundle
				.getBundle("hibernateDefaultConfiguration");

		return new HibernateSession("default",
				rb.getString("hibernate.connection.driver_class"),
				rb.getString("hibernate.connection.password"),
				rb.getString("hibernate.connection.username"),
				rb.getString("hibernate.connection.url"),
				rb.getString("hibernate.connection.default_catalog"));

	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDefaultCatalog() {
		return defaultCatalog;
	}

	public void setDefaultCatalog(String defaultCatalog) {
		this.defaultCatalog = defaultCatalog;
	}

	@Override
	public String toString() {
		return "HibernateSession [sessionName=" + sessionName + ", driverName="
				+ driverName + ", password=" + password + ", username="
				+ username + ", url=" + url + ", defaultCatalog="
				+ defaultCatalog + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((defaultCatalog == null) ? 0 : defaultCatalog.hashCode());
		result = prime * result
				+ ((driverName == null) ? 0 : driverName.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((sessionName == null) ? 0 : sessionName.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		HibernateSession other = (HibernateSession) obj;
		if (defaultCatalog == null) {
			if (other.defaultCatalog != null)
				return false;
		} else if (!defaultCatalog.equals(other.defaultCatalog))
			return false;
		if (driverName == null) {
			if (other.driverName != null)
				return false;
		} else if (!driverName.equals(other.driverName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (sessionName == null) {
			if (other.sessionName != null)
				return false;
		} else if (!sessionName.equals(other.sessionName))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}


}
