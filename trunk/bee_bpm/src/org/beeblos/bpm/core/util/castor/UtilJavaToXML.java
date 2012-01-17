package org.beeblos.bpm.core.util.castor;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.XMLGenerationException;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

public class UtilJavaToXML {

	private static final Log logger = LogFactory.getLog(UtilJavaToXML.class);

	public static String toXML(Object userObject, String template) throws XMLGenerationException {

		logger.debug("Processing UtilJavaToXML -> toXML()");

		String ret = null;

		try {

			StringWriter writer = new StringWriter();
			Marshaller marshaller = new Marshaller(writer);

			if (template != null && !"".equals(template)) {
				Mapping mapping = new Mapping();
				mapping.loadMapping(template);
				marshaller.setMapping(mapping);
			}

			marshaller.marshal(userObject);

			ret = writer.toString();

		} catch (Exception e) {

			String message = "Error trying generate xml from object: "
					+ userObject.getClass().getName() + "\n" + e.getMessage()
					+ " - " + e.getCause() + " - " + e.getClass(); 
			logger.error(message);
			throw new XMLGenerationException(message);

		}

		return ret;
	}

	public static Object fromXML(Class clazz, String template, File xmlFile) {
		logger.debug("Processing xml file... UtilJavaToXML -> fromXML():["
				+ xmlFile.getName() + "]");

		Object ret = null;

		try {

			Unmarshaller un = new Unmarshaller(clazz);

			if (template != null && !"".equals(template)) {
				Mapping mapping = new Mapping();
				mapping.loadMapping(template);
				un.setMapping(mapping);
			}

			FileReader in = new FileReader(xmlFile);
			ret = (Object) un.unmarshal(in);
			in.close();

		} catch (Exception e) {
			logger.error("Error trying to load object: " + clazz.getName()
					+ " con el fichero XML " + xmlFile.getAbsolutePath() + "\n"
					+ e.getMessage() + " - " + e.getCause());
		}

		return ret;
	}

	public static Object fromXML(Class clazz, String template,
			String contenidoXML) {

		logger.debug("Procesando contenido XML. UtilJavaToXML -> fromXML()");

		Object retorno = null;

		try {

			Unmarshaller un = new Unmarshaller(clazz);

			if (template != null && !"".equals(template)) {
				Mapping mapping = new Mapping();
				mapping.loadMapping(template);
				un.setMapping(mapping);
			}

			StringReader reader = new StringReader(contenidoXML);
			retorno = (Object) un.unmarshal(reader);

		} catch (Exception e) {
			logger.error("Error al intentar rellenar el objeto "
					+ clazz.getName() + " con el contenido XML \n"
					+ e.getMessage() + " - " + e.getCause());
		}

		return retorno;
	}

}
