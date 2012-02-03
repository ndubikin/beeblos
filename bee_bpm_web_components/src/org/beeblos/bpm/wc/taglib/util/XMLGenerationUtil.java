package org.beeblos.bpm.wc.taglib.util;

import static org.beeblos.bpm.core.util.Constants.TEXT_XML;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.XMLGenerationException;
import org.beeblos.bpm.core.util.castor.UtilJavaToXML;
import org.beeblos.bpm.wc.taglib.bean.WProcessDefFormBean;

public class XMLGenerationUtil {

	private static final Log logger = LogFactory
			.getLog(WProcessDefFormBean.class.getName());

	public XMLGenerationUtil() {
	}

	public static void generateXMLObject(Object obj, Integer id,
			String userTemplate) throws IOException, XMLGenerationException {
		logger.debug(">>>>>>>>>> Castor XML starting the process Marshalling");

		String templateWProcessDef = null, xmlContent = null;

		if (userTemplate != null && !"".equals(userTemplate)) {
			templateWProcessDef = userTemplate;
		} else {
			templateWProcessDef = "org/beeblos/bpm/core/util/castor/WProcessDef_castor.xml";
		}

		// runtime this point to WEB-INF/classes/castor
		// eclipse environment points to ~/castor
		templateWProcessDef = "org/beeblos/bpm/core/util/castor/" + obj.getClass().getSimpleName()
				+ "_castor.xml";

		try {

			xmlContent = UtilJavaToXML.toXML(obj, templateWProcessDef);

		} catch (XMLGenerationException e) {

			throw e;
			
		} catch (Exception e) {
			e.printStackTrace();
			String message = "Error genrating xml content: Error:"
					+ e.getClass() + " - " + e.getMessage() + " - "
					+ e.getCause();
			throw new XMLGenerationException(message);

		}

		if (xmlContent != null) {

			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			try {

				byte[] buffer = xmlContent.getBytes();

				response.setContentLength(buffer.length);
				response.setContentType(TEXT_XML);
				// response.setLocale( new Locale(DEFAULT_ENCODING) );
				response.setLocale(new Locale("UTF-8"));

				response.addHeader("Content-Disposition",
						"attachment; filename=\"" + "WProcessDef_id_" + id
								+ ".xml" + "\"");
				response.setHeader("Cache-Control", "no-cache");

				ServletOutputStream outStream = response.getOutputStream();
				outStream.write(buffer, 0, buffer.length);
				outStream.flush();
				outStream.close();
				FacesContext.getCurrentInstance().responseComplete();

				logger.debug("--->>> xml generated content:[" + xmlContent
						+ "]");

			} catch (IOException e) {

				e.printStackTrace();
				throw e;

			}

		} else {
			logger.info(">>>>>>>>>> Castor XML the content XML is null");
		}

		logger.debug(">>>>>>>>>> Castor XML process complete Marshalling");

	}

}