package org.beeblos.bpm.core.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

public class HibernateConfigurationUtil {

	private static String hibernateConfigurationListXML = "hibernateConfigurationList.xml";

	public HibernateConfigurationUtil() {

	}

	@SuppressWarnings("unchecked")
	public static List<HibernateConfigurationParameters> getConfigurationList()
			throws MarshalException, ValidationException, FileNotFoundException {
		
		FileReader reader = new FileReader(hibernateConfigurationListXML);

		List<HibernateConfigurationParameters> hcpl = new ArrayList<HibernateConfigurationParameters>();

		hcpl = (List<HibernateConfigurationParameters>) Unmarshaller.unmarshal(hcpl.getClass(),
				reader);

		return hcpl;

	}

	public static void persistConfigurationList(List<HibernateConfigurationParameters> hcpl)
			throws IOException, MarshalException, ValidationException {

		FileWriter writer = new FileWriter(hibernateConfigurationListXML);

		Marshaller.marshal(hcpl, writer);

		writer.close();

	}

	public static HibernateConfigurationParameters getConfiguration(String sessionName)
			throws MarshalException, ValidationException, FileNotFoundException{

		List<HibernateConfigurationParameters> hcpl = getConfigurationList();

		for (HibernateConfigurationParameters hibSess : hcpl) {

			if (hibSess.getSessionName().equals(sessionName)) {

				return hibSess;

			}

		}

		return null;

	}
	
	public static void addFirstConfiguration(HibernateConfigurationParameters hcp)
			throws MarshalException, ValidationException, IOException {

		List<HibernateConfigurationParameters> hcpl = new ArrayList<HibernateConfigurationParameters>();

		hcpl.add(hcp);

		persistConfigurationList(hcpl);

	}

	
	
}
