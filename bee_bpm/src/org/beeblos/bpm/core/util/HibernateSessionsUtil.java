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

public class HibernateSessionsUtil {

	private static String hibernateConfigurationListXML = "hibernateConfigurationList.xml";

	public HibernateSessionsUtil() {

	}

	@SuppressWarnings("unchecked")
	public static List<HibernateSession> getConfigurationList()
			throws MarshalException, ValidationException, FileNotFoundException {
		
		FileReader reader = new FileReader(hibernateConfigurationListXML);

		List<HibernateSession> hsl = new ArrayList<HibernateSession>();

		hsl = (List<HibernateSession>) Unmarshaller.unmarshal(hsl.getClass(),
				reader);

		return hsl;

	}

	public static void persistConfigurationList(List<HibernateSession> hsl)
			throws IOException, MarshalException, ValidationException {

		FileWriter writer = new FileWriter(hibernateConfigurationListXML);

		Marshaller.marshal(hsl, writer);

		writer.close();

	}

//	public static void addConfiguration(HibernateSession hs)
//			throws MarshalException, ValidationException, IOException {
//
//		// LA LISTA YA TENES QUE TENERLA LEÍDA ...
////		List<HibernateSession> hsl = getConfigurationList();
//		
//		hsl.add(hs);
//
//		persistConfigurationList(hsl);
//
//	}

	public static void updateConfiguration(HibernateSession hs)
			throws MarshalException, ValidationException, IOException {

		List<HibernateSession> hsl = getConfigurationList();
		HibernateSession removeElement = null;

		for (HibernateSession hibSess : hsl) {

			if (hibSess.getSessionName().equals(hs.getSessionName())) {

				removeElement = hibSess;

			}

		}

		hsl.remove(removeElement);

		hsl.add(hs);

		persistConfigurationList(hsl);

	}

	public static void deleteConfiguration(HibernateSession hs)
			throws MarshalException, ValidationException, IOException {

		List<HibernateSession> hsl = getConfigurationList();

		for (HibernateSession hibSess : hsl) {

			if (hibSess.getSessionName().equals(hs.getSessionName())) {

				hsl.remove(hibSess);
				break;

			}

		}

		persistConfigurationList(hsl);

	}


	public static HibernateSession getConfiguration(String sessionName)
			throws MarshalException, ValidationException, FileNotFoundException{

		List<HibernateSession> hsl = getConfigurationList();

		for (HibernateSession hibSess : hsl) {

			if (hibSess.getSessionName().equals(sessionName)) {

				return hibSess;

			}

		}

		return null;

	}
	
	public static void addFirstConfiguration(HibernateSession hs)
			throws MarshalException, ValidationException, IOException {

		List<HibernateSession> hsl = new ArrayList<HibernateSession>();

		hsl.add(hs);

		persistConfigurationList(hsl);

	}

	
	
}
