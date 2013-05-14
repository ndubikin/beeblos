package org.beeblos.bpm.core.bl;

import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.dao.HibernateConfigurationDao;
import org.beeblos.bpm.core.error.EnvironmentException;
import org.beeblos.bpm.core.model.EnvType;
import org.beeblos.bpm.core.model.Environment;
import org.beeblos.bpm.core.model.HibernateConfigurationParameters;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

public class HibernateConfigurationBL {

	private static final Log logger = LogFactory
			.getLog(HibernateConfigurationBL.class);

	public HibernateConfigurationBL() {

	}

	public List<HibernateConfigurationParameters> getConfigurationList()
			throws MarshalException, ValidationException, EnvironmentException, FileNotFoundException {

		logger.info("HibernateConfigurationBL.getConfigurationList()");

		List<HibernateConfigurationParameters> hcpList = 
				new HibernateConfigurationDao().getConfigurationList();
		
		if (hcpList != null){
			
			for (HibernateConfigurationParameters hcp : hcpList){
				
				this._loadEnvironment(hcp);

			}
			
		}
		
		return hcpList;

	}

	public void persistConfigurationList(List<HibernateConfigurationParameters> hcpl, Integer currentUserId)
			throws IOException, MarshalException, ValidationException, EnvironmentException {

		logger.info("HibernateConfigurationBL.persistConfigurationList()");

		new HibernateConfigurationDao().persistConfigurationList(hcpl);
		
		EnvironmentBL eBL = new EnvironmentBL();
		
		// persistimos los environments
		for (HibernateConfigurationParameters hcp : hcpl){
			
			if (hcp != null
					&& hcp.getEnvironment() != null){
				
				// si el envType esta vacio lo ponemos a null
				if (hcp.getEnvironment().getEnvType() != null
					&& (hcp.getEnvironment().getEnvType().getId() == null
						|| (hcp.getEnvironment().getEnvType().getId().intValue() == 0)
						|| hcp.getEnvironment().getEnvType().empty())){
					hcp.getEnvironment().setEnvType(null);
				}
				
				if (hcp.getEnvironment().getId() == null
						|| hcp.getEnvironment().equals(0)){
					
					eBL.add(hcp.getEnvironment(), currentUserId);
					
				} else {
					eBL.update(hcp.getEnvironment(), currentUserId);
				}
				
			}
			
		}

	}

	public HibernateConfigurationParameters getConfiguration(String sessionName)
			throws MarshalException, ValidationException, FileNotFoundException, EnvironmentException{

		logger.info("HibernateConfigurationBL.getConfiguration()");

		HibernateConfigurationParameters hcp = 
				new HibernateConfigurationDao().getConfiguration(sessionName);
		
		this._loadEnvironment(hcp);
		
		return hcp;

	}
	
	public HibernateConfigurationParameters getDefaultConfiguration()
			throws MarshalException, ValidationException, FileNotFoundException, EnvironmentException{

		logger.info("HibernateConfigurationBL.getDefaultConfiguration()");

		HibernateConfigurationParameters hcp = 
				new HibernateConfigurationDao().getDefaultConfiguration();
		
		this._loadEnvironment(hcp);

		return hcp;

	}
	
	private void _loadEnvironment(HibernateConfigurationParameters hcp) throws EnvironmentException{
		
		Environment e = null;
		if (hcp != null){
			e = new EnvironmentBL().getEnvironmentByName(hcp.getSessionName());

			if (e == null){
				
				e = new Environment(EMPTY_OBJECT);
				e.setName(hcp.getSessionName());
				
			} else if (e.getEnvType() == null){
				e.setEnvType(new EnvType());
			}

			hcp.setEnvironment(e);

		}		

	}
	
	public void addFirstConfiguration(HibernateConfigurationParameters hcp, Integer currentUserId)
			throws MarshalException, ValidationException, IOException, EnvironmentException {

		logger.info("HibernateConfigurationBL.addFirstConfiguration()");

		new HibernateConfigurationDao().addFirstConfiguration(hcp);
		
		// si el envType esta vacio lo ponemos a null
		if (hcp.getEnvironment().getEnvType() != null
				&& hcp.getEnvironment().getEnvType().empty()){
			hcp.getEnvironment().setEnvType(null);
		}

		new EnvironmentBL().add(hcp.getEnvironment(), currentUserId);

	}

	
	
}
