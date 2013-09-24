package org.beeblos.bpm.core.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.beeblos.bpm.core.model.HibernateConfigurationParameters;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Deprecated 
// dml 20130924 VER COMO CAMBIAR EL HIBERNATEUTIL PARA QUE FUNCIONE PERFECTAMENTE
public class HibernateUtilImpl {

	protected static SessionFactory sessionFactory;

	public HibernateUtilImpl() {

	}

	public static HibernateConfigurationParameters loadDefaultParameters() {

		ResourceBundle rb = org.beeblos.bpm.core.util.Configuration
				.getConfigurationRepositoryResourceBundle();

		HibernateConfigurationParameters parameters = new HibernateConfigurationParameters();

		try {

			parameters.setDriverName(rb.getString("bee_bpm_core.hibernate.connection.driver_class"));
			parameters.setPassword(rb.getString("bee_bpm_core.hibernate.connection.password"));
			parameters.setUrl(rb.getString("bee_bpm_core.hibernate.connection.url"));
			parameters.setUsername(rb.getString("bee_bpm_core.hibernate.connection.username"));
			parameters.setDefaultCatalog(rb
					.getString("bee_bpm_core.hibernate.connection.default_catalog"));
			parameters.setDialect(rb.getString("bee_bpm_core.hibernate.connection.dialect"));
			parameters
					.setShowSQL(rb.getString("bee_bpm_core.hibernate.showSQL").equals("true") ? true
							: false);
			parameters
					.setShowSQL(rb.getString("bee_bpm_core.hibernate.formatSQL").equals("true") ? true
							: false);

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		}

		return parameters;
	}
	/*
	private static SessionFactory obtenerSessionFactory()
			throws HibernateException, MarshalException, ValidationException, FileNotFoundException {

		if (sessionFactory == null) {
			
			Configuration conf = new Configuration().configure();
			
			// trying to load the configuration.properties configuration
			HibernateConfigurationParameters parameters = loadDefaultParameters();

			// if it is not possible, it is loaded the default config of the xml file
			if (parameters == null || parameters.hasEmptyFields()) {

				parameters = new HibernateConfigurationUtil().getDefaultConfiguration();
				
			}
			
			conf.setProperty("hibernate.connection.driver_class",
					parameters.getDriverName());
			conf.setProperty("hibernate.connection.password",
					parameters.getPassword());

			// url + default_catalog
			conf.setProperty("hibernate.connection.url", parameters.getUrl() + "/"
					+ parameters.getDefaultCatalog());
			conf.setProperty("hibernate.connection.username",
					parameters.getUsername());
			conf.setProperty("hibernate.default_catalog",
					parameters.getDefaultCatalog());
			
			conf.setProperty("hibernate.dialect",
					parameters.getDialect());

			conf.setProperty("show_sql",
					(parameters.isShowSQL()) ? "true":"false");
			conf.setProperty("format_sql",
					(parameters.isFormatSQL()) ? "true":"false");

//		    serviceRegistry = new ServiceRegistryBuilder()
//		    	.applySettings(conf.getProperties()).buildServiceRegistry();
		    sessionFactory = conf.buildSessionFactory();

		}

		return sessionFactory;

	}
	
	public static Session obtenerSession() {

		// nes - 20100809 - para que muestre el error correcto cuando no puede
		// devolver
		// la session por algún motivo ...
		try {
			return obtenerSessionFactory().getCurrentSession();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	*/
	private static SessionFactory createNewSessionFactory(
			HibernateConfigurationParameters parameters) {

		if (sessionFactory == null) {

			Configuration conf = new Configuration().configure();

			conf.setProperty("hibernate.connection.driver_class", parameters.getDriverName());
			conf.setProperty("hibernate.connection.password", parameters.getPassword());

			// url + default_catalog
			conf.setProperty("hibernate.connection.url",
					parameters.getUrl() + "/" + parameters.getDefaultCatalog());
			conf.setProperty("hibernate.connection.username", parameters.getUsername());
			conf.setProperty("hibernate.default_catalog", parameters.getDefaultCatalog());

			conf.setProperty("hibernate.dialect", parameters.getDialect());

			conf.setProperty("show_sql", (parameters.isShowSQL()) ? "true" : "false");
			conf.setProperty("format_sql", (parameters.isFormatSQL()) ? "true" : "false");

			sessionFactory = conf.buildSessionFactory();

		}

		return sessionFactory;

	}

	// dml 20120131
	public static boolean createNewSession(HibernateConfigurationParameters newParameters)
			throws ClassNotFoundException, SQLException {

		// nes - 20100809 - para que muestre el error correcto cuando no puede
		// devolver
		// la session por algún motivo ...
		sessionFactory = null;

		if (checkJDBCConnection(newParameters)) {

			try {

				Session session = createNewSessionFactory(newParameters).getCurrentSession();
				
				if (session != null){
					return true;
				} else {
					return false;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		} else {

			return false;

		}

	}

	public static boolean checkJDBCConnection(HibernateConfigurationParameters hSession)
			throws ClassNotFoundException, SQLException {

		boolean ret = false;

		Connection connection = null;

		// trying to create the driver class
		Class driverObject;

		driverObject = Class.forName(hSession.getDriverName());

		// trying to connect with database
		connection = DriverManager.getConnection(hSession.getUrl(), hSession.getUsername(),
				hSession.getPassword());

		DatabaseMetaData meta = connection.getMetaData();

		ResultSet catalogs = meta.getCatalogs();
		/*
		 * System.out.println("metadata: Maj / min Version:  " +
		 * meta.getDatabaseMajorVersion() + "/" +
		 * connection.getMetaData().getDatabaseMinorVersion());
		 * System.out.println("metadata: DatabaseProductName:" +
		 * meta.getDatabaseProductName());
		 * System.out.println("metadata: DatabaseProductVersion:" +
		 * meta.getDatabaseProductVersion()); System.err .println(
		 * "-----------------------------------------------------------------------------------------------------------"
		 * ); System.out.println("Driver Name : " + meta.getDriverName());
		 * System.out.println("Driver Version : " + meta.getDriverVersion());
		 */
		while (catalogs.next()) {
			String catalog = catalogs.getString(1); // "TABLE_CATALOG"
			// System.out.println("catalog: " + catalog);
			if (catalog.equals(hSession.getDefaultCatalog())) {
				ret = true; // si existe el catalogo ok
				// break;
			}
		}

		// si llega hasta aqui es que la conexion fue bien ...
		if (hSession.getDefaultCatalog() == null) {
			ret = true;
		}

		connection.close();
		return ret;
	}

}
