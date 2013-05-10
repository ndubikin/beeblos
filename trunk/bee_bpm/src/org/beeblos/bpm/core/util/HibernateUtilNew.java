package org.beeblos.bpm.core.util;

import static org.beeblos.bpm.core.util.Constants.APP_NAME;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtilNew {

//	private static SessionFactory sessionFactory;
	
	private static List<UserSessionFactory> userSessionFactoryList;
	private static String defaultSessionName;
	
	private static Session session;
	private static Transaction tx;
	
	private static HibernateConfigurationParameters parameters;

	public HibernateUtilNew() {

	}

	private static SessionFactory obtenerSessionFactory(Integer loggedUserId)
			throws HibernateException, MarshalException, ValidationException, FileNotFoundException {

		SessionFactory sF = null;
		
		if (userSessionFactoryList == null) {
			
			Configuration conf = new Configuration().configure();
			
			// trying to load the configuration.properties configuration
			parameters = loadDefaultParameters();

			// if it is not possible, it is loaded the default config of the xml file
			if (parameters == null || parameters.hasEmptyFields()) {

				parameters = HibernateConfigurationUtil.getDefaultConfiguration();
				
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

			
			// dml 20130510 - la primera vez que entramos ponemos el nombre de la session por defecto
			defaultSessionName = parameters.getSessionName();
			
			// dml 20130510 - creamos la session factory por defecto y la metemos en la lista de clase
			sF = conf.buildSessionFactory();
			
			userSessionFactoryList = new ArrayList<UserSessionFactory>();
			
			userSessionFactoryList.add(new UserSessionFactory(sF, APP_NAME, 
					defaultSessionName, loggedUserId));

		// si el usuario esta en session en el if se inicializara el sF que se devolvera mas abajo, sino ya se mete dentro
		// del if e inicializa la variable sF apra devolver
		} else if ((sF = _userIsInSession(loggedUserId)) == null){
			
			sF = _setUserInDefaultSessionFactory(loggedUserId);
			
		} 

		return sF;
		
	}
	
	// dml 20130510
	private static SessionFactory _userIsInSession(Integer userId){
		
		if (userId != null
				&& !userId.equals(0)
				&& userSessionFactoryList != null){
			
			for (UserSessionFactory usF : userSessionFactoryList){
				
				if (usF.getUserIdList().contains(userId)){
					return usF.getSessionFactory();
				}
				
			}
			
		}
		
		return null;
		
	}
	
	// dml 20130510
	private static SessionFactory _setUserInDefaultSessionFactory(Integer userId){
		
		if (defaultSessionName != null
				&& !"".equals(defaultSessionName)
				&& userSessionFactoryList != null){
			
			for (UserSessionFactory usF : userSessionFactoryList){
				
				if (usF.getConnectionName().equals(defaultSessionName)){
					
					userSessionFactoryList.add(new UserSessionFactory(usF.getSessionFactory(), APP_NAME, 
							defaultSessionName, userId));
					
					return usF.getSessionFactory();
				
				}
				
			}
			
		}
		
		return null;
		
	}
	
	public static HibernateConfigurationParameters loadDefaultParameters(){
		
		ResourceBundle rb = org.beeblos.bpm.core.util.Configuration.getConfigurationRepositoryResourceBundle();
		
		HibernateConfigurationParameters parameters = new HibernateConfigurationParameters();
		
		try {
		
			parameters.setDriverName(rb.getString("bee_bpm_core.hibernate.connection.driver_class"));
			parameters.setPassword(rb.getString("bee_bpm_core.hibernate.connection.password"));
			parameters.setUrl(rb.getString("bee_bpm_core.hibernate.connection.url"));
			parameters.setUsername(rb.getString("bee_bpm_core.hibernate.connection.username"));
			parameters.setDefaultCatalog(rb.getString("bee_bpm_core.hibernate.connection.default_catalog"));
			parameters.setDialect(rb.getString("bee_bpm_core.hibernate.connection.dialect"));
			parameters.setShowSQL(rb.getString("bee_bpm_core.hibernate.showSQL").equals("true") ? true : false);
			parameters.setShowSQL(rb.getString("bee_bpm_core.hibernate.formatSQL").equals("true") ? true : false);
		
		} catch (Exception e){
			
			e.printStackTrace();
			return null;
			
		}
		
		return parameters;
	}

	private static SessionFactory _createNewSessionFactory(
			HibernateConfigurationParameters parameters, Integer loggedUserId){

		UserSessionFactory usF = null;
		
		if (_checkSessionFactoryExists(parameters.getSessionName())){
			
			usF = _insertUserInExistingSessionFactory(parameters.getSessionName(), loggedUserId);
			
		} else {
			
			_unboundUserFromSessionFactories(loggedUserId);
			usF = _createNewUserSessionFactory(parameters, loggedUserId);
			userSessionFactoryList.add(usF);
			
		}
		
		return usF.getSessionFactory();
	
	}	
	
	// dml 20130510
	private static boolean _checkSessionFactoryExists(String sessionName){
		
		if (sessionName != null
				&& !"".equals(sessionName)
				&& userSessionFactoryList != null){
			
			for (UserSessionFactory usF : userSessionFactoryList){
				
				if (usF.getConnectionName().equals(sessionName)){
					
					return true;
				
				}
				
			}
			
		}
		
		return false;
		
	}

	// dml 20130510
	private static UserSessionFactory _insertUserInExistingSessionFactory(String sessionName, Integer userId){
		
		UserSessionFactory returnValue = null;
		
		if (sessionName != null
				&& !"".equals(sessionName)
				&& userSessionFactoryList != null){
			
			for (UserSessionFactory usF : userSessionFactoryList){
			
				// si es la conexion donde queremos meter el usuario y este aun no esta dentro lo metemos
				if (usF.getConnectionName().equals(sessionName)){
					
					returnValue = usF;
					if (!usF.getUserIdList().contains(userId)){					
						usF.getUserIdList().add(userId);
					}
				
				// si no es la conexion vemos si el usuario esta y lo eliminamos (esto para el cambio de conexion)	
				} else if (usF.getUserIdList().contains(userId)){
					
					usF.getUserIdList().remove(userId);
					
				}
				
			}
			
		}
		
		return returnValue;

	}

	// dml 20130510
	private static void _unboundUserFromSessionFactories(Integer userId){
		
		if (userId != null
				&& !userId.equals(0)
				&& userSessionFactoryList != null){
			
			for (UserSessionFactory usF : userSessionFactoryList){
			
				if (usF.getUserIdList().contains(userId)){
					
					usF.getUserIdList().remove(userId);
					
				}
				
			}
			
		}
		
	}

	// dml 20130510
	private static UserSessionFactory _createNewUserSessionFactory(
			HibernateConfigurationParameters parameters, Integer userId){

		Configuration conf = new Configuration().configure();

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

		SessionFactory sF = conf.buildSessionFactory();
		
		return new UserSessionFactory(sF, parameters.getSessionName(), APP_NAME, userId);

	}
	
	public static Session obtenerSession(Integer userId) {

		// nes - 20100809 - para que muestre el error correcto cuando no puede
		// devolver
		// la session por algún motivo ...
		try {
			return obtenerSessionFactory(userId).getCurrentSession();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

/*	
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
*/
	
	// dml 20120510
	public static Session getNewSession(HibernateConfigurationParameters newParameters, Integer userId)
			throws ClassNotFoundException, SQLException {

		if (checkJDBCConnection(newParameters)) {

			try {
				return _createNewSessionFactory(newParameters, userId).getCurrentSession();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		} else {
			
			return null;
			
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
	public static Connection obtenerConnection(Integer userId) {

		Connection conex = null;
		tx = obtenerSession(userId).getTransaction();
		tx.begin();
		conex = obtenerSession(userId).connection();
		return conex;

	}

	public static Object obtenerPorPk(Object obj, Integer pk, Integer userId)
			throws HibernateException {

		try {
			Object item = null;
			session = obtenerSession(userId);
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

	public static String guardar(Object obj, Integer userId) throws HibernateException {

		try {
			session = obtenerSession(userId);
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

	public static void borrar(Object obj, Integer userId) throws HibernateException {

		try {
			session = obtenerSession(userId);
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

	public static void actualizar(Object obj, Integer userId) throws HibernateException {

		try {
			session = obtenerSession(userId);
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
