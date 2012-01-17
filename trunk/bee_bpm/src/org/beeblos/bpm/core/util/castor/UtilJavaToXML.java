package org.beeblos.bpm.core.util.castor;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

public class UtilJavaToXML {
	
	private static final Log logger = LogFactory.getLog(UtilJavaToXML.class);
	
	public static String toXML(Object objeto, String template) {
		
    	logger.debug("Procesando UtilJavaToXML -> toXML()");
    	
		String retorno = null;
		
		try {
			
            StringWriter writer = new StringWriter();
            Marshaller marshaller = new Marshaller(writer);			

            if (template!=null && !"".equals(template)) {
    	        Mapping mapping = new Mapping();
    	        mapping.loadMapping(template);
                marshaller.setMapping(mapping);
            }
            
            marshaller.marshal(objeto);
            
            retorno = writer.toString();
            
		} catch (Exception e) {
			System.out.println( e );
			
			logger.error("Error al intentar generar el XML del objeto "+objeto.getClass().getName()+ "\n" + e.getMessage() + " - " + e.getCause());
			
		}
		
		return retorno;
	}
	
	public static Object fromXML(Class clazz, String template, File ficheroXML) {
		
    	logger.debug("Procesando fichero XML. UtilJavaToXML -> fromXML()");
		
		Object retorno = null;
		
		try {

			Unmarshaller un = new Unmarshaller(clazz);
			
            if (template!=null && !"".equals(template)) {
    	        Mapping mapping = new Mapping();
    	        mapping.loadMapping(template);
    	        un.setMapping( mapping );
            }
			
			FileReader in = new FileReader(ficheroXML);
			retorno = (Object) un.unmarshal(in);
			in.close();
			
		} catch (Exception e) {
			logger.error("Error al intentar rellenar el objeto "+clazz.getName()+" con el fichero XML " + ficheroXML.getAbsolutePath() + "\n" + e.getMessage() + " - " + e.getCause());
		}
		
		return retorno;
	}
	
	public static Object fromXML(Class clazz, String template, String contenidoXML) {
		
    	logger.debug("Procesando contenido XML. UtilJavaToXML -> fromXML()");
		
		Object retorno = null;
		
		try {
			
			Unmarshaller un = new Unmarshaller(clazz);
			
            if (template!=null && !"".equals(template)) {
    	        Mapping mapping = new Mapping();
    	        mapping.loadMapping(template);
    			un.setMapping( mapping );
            }

			StringReader reader = new StringReader(contenidoXML); 			
			retorno = (Object) un.unmarshal(reader);
			
		} catch (Exception e) {
			logger.error("Error al intentar rellenar el objeto "+clazz.getName()+" con el contenido XML \n" + e.getMessage() + " - " + e.getCause());
		}
		
		return retorno;
	}
	
	
	
}
