package org.beeblos.bpm.core.util;

import java.util.ResourceBundle;

public class HibernateSessionParameters {

	private String connectionDriverClass;
	private String connectionPassword;
	private String connectionUsername;
	private String connectionUrl;
	private String connectionDefaultCatalog;

	public HibernateSessionParameters() {

	}

	public HibernateSessionParameters(String connectionDriverClass,
			String connectionPassword, String connectionUsername,
			String connectionUrl, String connectionDefaultCatalog) {
		super();
		this.connectionDriverClass = connectionDriverClass;
		this.connectionPassword = connectionPassword;
		this.connectionUsername = connectionUsername;
		this.connectionUrl = connectionUrl;
		this.connectionDefaultCatalog = connectionDefaultCatalog;
	}

	public static HibernateSessionParameters loadDefaultHibernateSessionParameters() {

		ResourceBundle rb = ResourceBundle
				.getBundle("hibernateDefaultConfiguration");

		return new HibernateSessionParameters(
				rb.getString("hibernate.connection.driver_class"),
				rb.getString("hibernate.connection.password"),
				rb.getString("hibernate.connection.username"),
				rb.getString("hibernate.connection.url"),
				rb.getString("hibernate.connection.default_catalog"));

	}

	public String getConnectionDriverClass() {
		return connectionDriverClass;
	}

	public void setConnectionDriverClass(String connectionDriverClass) {
		this.connectionDriverClass = connectionDriverClass;
	}

	public String getConnectionPassword() {
		return connectionPassword;
	}

	public void setConnectionPassword(String connectionPassword) {
		this.connectionPassword = connectionPassword;
	}

	public String getConnectionUsername() {
		return connectionUsername;
	}

	public void setConnectionUsername(String connectionUsername) {
		this.connectionUsername = connectionUsername;
	}

	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getConnectionDefaultCatalog() {
		return connectionDefaultCatalog;
	}

	public void setConnectionDefaultCatalog(String connectionDefaultCatalog) {
		this.connectionDefaultCatalog = connectionDefaultCatalog;
	}

	@Override
	public String toString() {
		return "HibernateSessionParameters [connectionDriverClass="
				+ connectionDriverClass + ", connectionPassword="
				+ connectionPassword + ", connectionUsername="
				+ connectionUsername + ", connectionUrl=" + connectionUrl
				+ ", connectionDefaultCatalog=" + connectionDefaultCatalog
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((connectionDefaultCatalog == null) ? 0
						: connectionDefaultCatalog.hashCode());
		result = prime
				* result
				+ ((connectionDriverClass == null) ? 0 : connectionDriverClass
						.hashCode());
		result = prime
				* result
				+ ((connectionPassword == null) ? 0 : connectionPassword
						.hashCode());
		result = prime * result
				+ ((connectionUrl == null) ? 0 : connectionUrl.hashCode());
		result = prime
				* result
				+ ((connectionUsername == null) ? 0 : connectionUsername
						.hashCode());
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
		HibernateSessionParameters other = (HibernateSessionParameters) obj;
		if (connectionDefaultCatalog == null) {
			if (other.connectionDefaultCatalog != null)
				return false;
		} else if (!connectionDefaultCatalog
				.equals(other.connectionDefaultCatalog))
			return false;
		if (connectionDriverClass == null) {
			if (other.connectionDriverClass != null)
				return false;
		} else if (!connectionDriverClass.equals(other.connectionDriverClass))
			return false;
		if (connectionPassword == null) {
			if (other.connectionPassword != null)
				return false;
		} else if (!connectionPassword.equals(other.connectionPassword))
			return false;
		if (connectionUrl == null) {
			if (other.connectionUrl != null)
				return false;
		} else if (!connectionUrl.equals(other.connectionUrl))
			return false;
		if (connectionUsername == null) {
			if (other.connectionUsername != null)
				return false;
		} else if (!connectionUsername.equals(other.connectionUsername))
			return false;
		return true;
	}

}
