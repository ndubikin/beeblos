package org.beeblos.bpm.core.dao;

import static org.beeblos.bpm.core.util.Constants.HIBERNATE_CONFIGURATION_LIST_XML;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.beeblos.bpm.core.model.HibernateConfigurationParameters;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

public class HibernateConfigurationDao {

	public HibernateConfigurationDao() {

	}

	@SuppressWarnings("unchecked")
	public List<HibernateConfigurationParameters> getConfigurationList() 
			throws FileNotFoundException, MarshalException, ValidationException {
		
		FileReader reader;
		List<HibernateConfigurationParameters> hcpl = new ArrayList<HibernateConfigurationParameters>();
		try {
			
			reader = new FileReader(HIBERNATE_CONFIGURATION_LIST_XML);

			hcpl = (List<HibernateConfigurationParameters>) Unmarshaller.unmarshal(hcpl.getClass(),
				reader);

		} catch (FileNotFoundException e) {
			
			new File(HIBERNATE_CONFIGURATION_LIST_XML);
			throw new FileNotFoundException(e.toString());
			
		}

		return hcpl;

	}

	public void persistConfigurationList(List<HibernateConfigurationParameters> hcpl)
			throws IOException, MarshalException, ValidationException {

		FileWriter writer = new FileWriter(HIBERNATE_CONFIGURATION_LIST_XML);

		Marshaller.marshal(hcpl, writer);

		writer.close();

	}

	public HibernateConfigurationParameters getConfiguration(String sessionName)
			throws MarshalException, ValidationException, FileNotFoundException{

		List<HibernateConfigurationParameters> hcpl = getConfigurationList();

		for (HibernateConfigurationParameters hibSess : hcpl) {

			if (hibSess.getSessionName().equals(sessionName)) {

				return hibSess;

			}

		}

		return null;

	}
	
	public HibernateConfigurationParameters getDefaultConfiguration()
			throws MarshalException, ValidationException, FileNotFoundException{

		List<HibernateConfigurationParameters> hcpl = getConfigurationList();

		for (HibernateConfigurationParameters hibSess : hcpl) {

			if (hibSess.isDefaultConfiguration()) {

				return hibSess;

			}

		}

		return null;

	}
	
	public void addFirstConfiguration(HibernateConfigurationParameters hcp)
			throws MarshalException, ValidationException, IOException {

		List<HibernateConfigurationParameters> hcpl = new ArrayList<HibernateConfigurationParameters>();

		hcpl.add(hcp);

		persistConfigurationList(hcpl);

	}

	
	
}
