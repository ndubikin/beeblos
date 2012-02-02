package org.beeblos.bpm.core.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * @author roger
 * 
 */

public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static Session session;
	private static Transaction tx;

	public HibernateUtil() {

	}

	private static SessionFactory obtenerSessionFactory()
			throws HibernateException {

		if (sessionFactory == null) {

			ResourceBundle rb = ResourceBundle
					.getBundle("hibernateDefaultConfiguration");

			Configuration conf = new Configuration().configure();

			conf.setProperty("hibernate.connection.driver_class",
					rb.getString("hibernate.connection.driver_class"));

			conf.setProperty("hibernate.connection.password",
					rb.getString("hibernate.connection.password"));

			// url + default_catalog
			conf.setProperty(
					"hibernate.connection.url",
					rb.getString("hibernate.connection.url")
							+ rb.getString("hibernate.connection.default_catalog"));

			conf.setProperty("hibernate.connection.username",
					rb.getString("hibernate.connection.username"));

			conf.setProperty("hibernate.connection.default_catalog",
					rb.getString("hibernate.connection.default_catalog"));

			sessionFactory = conf.buildSessionFactory();

		}

		return sessionFactory;

	}

	private static SessionFactory createNewSessionFactory(
			HibernateSession parameters) throws Exception {

		if (sessionFactory == null) {

			Configuration conf = new Configuration().configure();

			conf.setProperty("hibernate.connection.driver_class",
					parameters.getDriverName());
			conf.setProperty("hibernate.connection.password",
					parameters.getPassword());

			// url + default_catalog
			conf.setProperty("hibernate.connection.url", parameters.getUrl()
					+ parameters.getDefaultCatalog());
			conf.setProperty("hibernate.connection.username",
					parameters.getUsername());
			conf.setProperty("hibernate.connection.default_catalog",
					parameters.getDefaultCatalog());

			try {
				sessionFactory = conf.buildSessionFactory();

			} catch (Throwable e) {

				System.out.println("por fin");

			}
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

	// dml 20120131
	public static Session getDefaultSession() {

		// nes - 20100809 - para que muestre el error correcto cuando no puede
		// devolver
		// la session por algún motivo ...
		try {
			// sessionFactory is inicialized NULL value to bind the session to
			// take the default one
			sessionFactory = null;

			return obtenerSessionFactory().getCurrentSession();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// dml 20120131
	public static Session getNewSession(HibernateSession newParameters)
			throws Exception {

		// nes - 20100809 - para que muestre el error correcto cuando no puede
		// devolver
		// la session por algún motivo ...
		sessionFactory = null;
		return createNewSessionFactory(newParameters).getCurrentSession();

	}

	public static boolean checkJDBCConnection(HibernateSession hSession)
			throws ClassNotFoundException, SQLException {

		boolean ret = false;

		Connection connection = null;

		// trying to create the driver class
		Class driverObject;

		driverObject = Class.forName(hSession.getDriverName());

		// trying to connect with database
		connection = DriverManager.getConnection(hSession.getUrl(),
				hSession.getUsername(), hSession.getPassword());

		DatabaseMetaData meta = connection.getMetaData();

		ResultSet catalogs = meta.getCatalogs();

		System.out.println("metadata: Maj / min Version:  "
				+ meta.getDatabaseMajorVersion() + "/"
				+ connection.getMetaData().getDatabaseMinorVersion());
		System.out.println("metadata: DatabaseProductName:"
				+ meta.getDatabaseProductName());
		System.out.println("metadata: DatabaseProductVersion:"
				+ meta.getDatabaseProductVersion());
		System.err
				.println("-----------------------------------------------------------------------------------------------------------");
		System.out.println("Driver Name : " + meta.getDriverName());
		System.out.println("Driver Version : " + meta.getDriverVersion());

		while (catalogs.next()) {
			String catalog = catalogs.getString(1); // "TABLE_CATALOG"
			System.out.println("catalog: " + catalog);
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

	@SuppressWarnings("deprecation")
	public static Connection obtenerConnection() {

		Connection conex = null;
		tx = obtenerSession().getTransaction();
		tx.begin();
		conex = obtenerSession().connection();
		return conex;

	}

	public static Object obtenerPorPk(Object obj, Integer pk)
			throws HibernateException {

		try {
			Object item = null;
			session = obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			Serializable res = null;
			if (obj != null)

				item = session.get(obj.getClass(), pk);

			tx.commit();

			return item;

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			throw ex; // nes 20100930 - hay q tirar el error tal cual viene
						// hacia arriba si no se pierde info

		}

	}

	public static String guardar(Object obj) throws HibernateException {

		try {
			session = obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			Serializable res = null;
			if (obj != null)
				res = session.save(obj);
			tx.commit();

			return res.toString();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			throw ex; // nes 20100930 - hay q tirar el error tal cual viene
						// hacia arriba si no se pierde info

		}

	}

	public static void borrar(Object obj) throws HibernateException {

		try {
			session = obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			session.delete(obj);

			tx.commit();

		} catch (HibernateException ex) {
			ex.printStackTrace();
			if (tx != null)
				tx.rollback();
			throw ex; // nes 20100930 - hay q tirar el error tal cual viene
						// hacia arriba si no se pierde info

		}

	}

	public static void actualizar(Object obj) throws HibernateException {

		try {
			session = obtenerSession();
			tx = session.beginTransaction();

			if (obj != null) {

				session.update(obj);
			}
			tx.commit();

		} catch (HibernateException ex) {
			ex.printStackTrace();
			if (tx != null)
				tx.rollback();
			throw ex; // nes 20100930 - hay q tirar el error tal cual viene
						// hacia arriba si no se pierde info

		}

	}

}
