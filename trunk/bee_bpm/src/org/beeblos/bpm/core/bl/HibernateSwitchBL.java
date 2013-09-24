package org.beeblos.bpm.core.bl;

import java.sql.SQLException;

import org.beeblos.bpm.core.model.HibernateConfigurationParameters;
import org.beeblos.bpm.core.util.HibernateUtilImpl;

public class HibernateSwitchBL {

	public HibernateSwitchBL() {

	}

	public static HibernateConfigurationParameters loadDefaultParameters() {

		return HibernateUtilImpl.loadDefaultParameters();

	}

	public static boolean createNewSession(HibernateConfigurationParameters newParameters)
			throws ClassNotFoundException, SQLException {

		return HibernateUtilImpl.createNewSession(newParameters);
		
	}

	public static boolean checkJDBCConnection(HibernateConfigurationParameters hSession)
			throws ClassNotFoundException, SQLException {

		return HibernateUtilImpl.checkJDBCConnection(hSession);
				
	}
}