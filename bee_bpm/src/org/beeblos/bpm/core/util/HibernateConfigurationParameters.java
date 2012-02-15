package org.beeblos.bpm.core.util;


public class HibernateConfigurationParameters {

	private String sessionName;
	private String driverName;
	private String password;
	private String username;
	private String url;
	private String defaultCatalog;
	private boolean defaultConfiguration;

	public HibernateConfigurationParameters() {

	}

	public HibernateConfigurationParameters(String driverName,
			String password, String username, String url, String defaultCatalog) {
		super();
		this.driverName = driverName;
		this.password = password;
		this.username = username;
		this.url = url;
		this.defaultCatalog = defaultCatalog;
	}

	public HibernateConfigurationParameters(String sessionName, String driverName,
			String password, String username, String url, String defaultCatalog,
			boolean defaultConfiguration) {
		super();
		this.sessionName = sessionName;
		this.driverName = driverName;
		this.password = password;
		this.username = username;
		this.url = url;
		this.defaultCatalog = defaultCatalog;
		this.defaultConfiguration = defaultConfiguration;
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

	public boolean isDefaultConfiguration() {
		return defaultConfiguration;
	}

	public void setDefaultConfiguration(boolean defaultConfiguration) {
		this.defaultConfiguration = defaultConfiguration;
	}

	@Override
	public String toString() {
		return "HibernateConfigurationParameters [sessionName=" + sessionName
				+ ", driverName=" + driverName + ", password=" + password
				+ ", username=" + username + ", url=" + url
				+ ", defaultCatalog=" + defaultCatalog
				+ ", defaultConfiguration=" + defaultConfiguration + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((defaultCatalog == null) ? 0 : defaultCatalog.hashCode());
		result = prime * result + (defaultConfiguration ? 1231 : 1237);
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
		HibernateConfigurationParameters other = (HibernateConfigurationParameters) obj;
		if (defaultCatalog == null) {
			if (other.defaultCatalog != null)
				return false;
		} else if (!defaultCatalog.equals(other.defaultCatalog))
			return false;
		if (defaultConfiguration != other.defaultConfiguration)
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

	public boolean empty() {

		if (sessionName!=null && ! "".equals(sessionName)) return false;
		if (driverName!=null && ! "".equals(driverName)) return false;
		if (password!=null && ! "".equals(password)) return false;
		if (username!=null && ! "".equals(username)) return false;
		if (url!=null && ! "".equals(url)) return false;
		if (defaultCatalog!=null && ! "".equals(defaultCatalog)) return false;
		
		return true;
	}
	
	// dml 20120215
	public boolean hasEmptyFields() {
		
		if (driverName==null || "".equals(driverName)) return true;
		if (password==null || "".equals(password)) return true;
		if (username==null || "".equals(username)) return true;
		if (url==null || "".equals(url)) return true;
		if (defaultCatalog==null || "".equals(defaultCatalog)) return true;		
		
		return false;
	}

}
