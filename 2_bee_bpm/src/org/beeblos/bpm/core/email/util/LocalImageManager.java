package org.beeblos.bpm.core.email.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LocalImageManager {

	private static final Log logger = LogFactory.getLog(LocalImageManager.class);
	
	private static final String TAG_IMG = "<img ";
	private static final String FINAL_TAG = ">";

	//rrl 20110503
//	private static final String ATRIBUTO_SRC = "src=\"";
//	private static final String FINAL_ATRIBUTO = "\"";
	
	// el atributo SRC del tag IMG puede llevar comillas dobles o simples
	private static final String ATRIBUTO_SRC_COMILLA_DOBLE = "src=\"";
	private static final String FINAL_ATRIBUTO_COMILLA_DOBLE = "\"";
	private static final String ATRIBUTO_SRC_COMILLA_SIMPLE = "src=\'";
	private static final String FINAL_ATRIBUTO_COMILLA_SIMPLE = "\'";
	
	private static final String ATRIBUTO_SRC[] = {ATRIBUTO_SRC_COMILLA_DOBLE, ATRIBUTO_SRC_COMILLA_SIMPLE};
	private static final String FINAL_ATRIBUTO[] = {FINAL_ATRIBUTO_COMILLA_DOBLE, FINAL_ATRIBUTO_COMILLA_SIMPLE};

	// codificacion embebida en el propio html (solo para html5)
	private static final String DATA_URI_IMG = "data:";
	//private static final String ENCODING_BASE64 = ";base64";
	private static final String SEPARATOR_SYNTAX = ";";
	private static final String FINAL_SYNTAX = ",";
	
	private static final String PREFIX_URL_HTTP = "http";

	
	// esto tiene q sustituir lo q dice en el src y crearse la lista de imagenes
	public HtmlBodyModel convertHtmlBodyToEmailBody( String htmlBody, String contextPath ) {

		HtmlBodyModel convertedBody = new HtmlBodyModel();
		List<ImageManagerModel> listImageManagerModel = new ArrayList<ImageManagerModel>();
		
		StringBuffer htmlBodyAjustadoSrc = new StringBuffer(htmlBody);
		
		// este método devolverá correctamente ajustado el htmlBodyAjustadoSrc y la lista con ids cargada
		detectaYExtraeImagenesLocales( htmlBodyAjustadoSrc, contextPath, listImageManagerModel );

		// metemos lo que nos devuelven en el objeto convertedBody y lo devolvemos
		convertedBody.setBody(htmlBodyAjustadoSrc.toString());
		
		if ( listImageManagerModel.size()>0 ) {
			convertedBody.setImageList( listImageManagerModel );
		}
		
		return convertedBody;
	}
	
	public void detectaYExtraeImagenesLocales(StringBuffer textoHtml, String contextPath, List<ImageManagerModel> listImageManagerModel ) {

		Integer idImagen = 1;
		Integer nuevoIdImagen = null;
		int inicioIndex = 0;
		int buscaIndex = 0;
		int finalIndex = 0;
		boolean haySustituir = false;
		int longitudAnteriorTagImg = 0;

//		List<String> listaImagen = new ArrayList(); 
		
		// la base path para buscar las imagenes del tipo /fbbva_gp/userfiles/image/imagen.png
		String basePathServidor = contextPath;
		
		// encuentras un tag con TAG_IMG
		buscaIndex = textoHtml.indexOf(TAG_IMG, inicioIndex);
		while (buscaIndex >= 0) {
			
			haySustituir = false;

			finalIndex = textoHtml.indexOf(FINAL_TAG, buscaIndex + TAG_IMG.length() + 1);
			
			// si en el src de ese tag la imagen es local (no se encuentra en internet y no empieza por "http")
			haySustituir = verificaSustituir(textoHtml.substring(buscaIndex, finalIndex), basePathServidor);
			
			if (haySustituir) {
				
				StringBuffer textoTagImg = new StringBuffer(textoHtml.substring(buscaIndex, finalIndex));
				longitudAnteriorTagImg = textoTagImg.length();
				
				// reemplaza el actual src por algo asi src="cid:part1.09020006.04030807@softpoint.org"
				nuevoIdImagen = reemplazaImagenSRC (textoTagImg, idImagen, listImageManagerModel);
				if (nuevoIdImagen != null) {
					
					idImagen = nuevoIdImagen; 
					
					// reemplaza el tag img con el nuevo src
					textoHtml.delete(buscaIndex, finalIndex);
					textoHtml.insert(buscaIndex, textoTagImg);
					
					// finalIndex ha cambiado al reemplazar, hacemos el ajuste de longitud del tag de antes por el nuevo tag
					inicioIndex = finalIndex - longitudAnteriorTagImg + textoTagImg.length()+1;

				} else {
					inicioIndex = finalIndex+1;
				}
			} else {
				inicioIndex = finalIndex+1;
			}
			
			buscaIndex = textoHtml.indexOf(TAG_IMG, inicioIndex);
		}

	}
	

	private boolean verificaSustituir(String textoTagImg, String basePathServidor) {
		
		boolean result = false;
		
		int inicioIndex = 0;
		int buscaIndex = 0;
		int finalIndex = 0;
		
		//rrl 20110503
		// Comprobar si tiene comilla doble o comilla simple el atributo src (comilla doble por defecto)
		int i = 0;
		for(int iComilla=0; iComilla<ATRIBUTO_SRC.length; iComilla++) {
			if (textoTagImg.indexOf(ATRIBUTO_SRC[iComilla], inicioIndex) >= 0) {
				i = iComilla;
			}
		}
		
		// encuentras el src de ese tag
		buscaIndex = textoTagImg.indexOf(ATRIBUTO_SRC[i], inicioIndex);
		if (buscaIndex >= 0) {

			// y encuentras el final de src de ese tag
			finalIndex = textoTagImg.indexOf(FINAL_ATRIBUTO[i], buscaIndex + ATRIBUTO_SRC[i].length() + 1);

			// la imagen no se encuentra en internet
			String imagenSRC = textoTagImg.substring(buscaIndex + ATRIBUTO_SRC[i].length(), finalIndex);
			if (!imagenSRC.toLowerCase().startsWith(PREFIX_URL_HTTP)) {
				
				// Imagen incrustada en el mismo HTML (solo en tag para HTML 5)
				if (imagenSRC.toLowerCase().startsWith(DATA_URI_IMG)) {
					
		        	result = true;
					
				} else {
					// y la imagen local existe
					String imagenLocal = basePathServidor+imagenSRC;
					File fichero = new File(imagenLocal);
			        if (fichero.exists()) {
			        	result = true;
			        } else {
			        	logger.error("No se agrega la imagen, no existe la imagen local indicada el el atributo src del tag:\n" + textoTagImg); // nes 20110429
			        }
				}
			}
		}
		
		return result;
	}
	
	private Integer reemplazaImagenSRC (StringBuffer textoTagImg, Integer idImagen, List<ImageManagerModel> listImageManagerModel ) {
		// src="cid:part1.09020006.04030807@softpoint.org"
		// cid:part1      - seria part"n" y si son 3 imagenes que encontra se pone part1, part2, part3 (diferente cada id de imagen)
		// .09020006. - luego del punto va la fecha en formato asi normal ddmmaaaa
		// .04030807  - luego del punto la hora hasta milesimas asi como 1 nro
		// @softpoint.org va fijo
		
		Integer result = null;
		
		int inicioIndex = 0;
		int buscaIndex = 0;
		int finalIndex = 0;
		
		//rrl 20110503
		// Comprobar si tiene comilla doble o comilla simple el atributo src (comilla doble por defecto)
		int i = 0;
		for(int iComilla=0; iComilla<ATRIBUTO_SRC.length; iComilla++) {
			if (textoTagImg.indexOf(ATRIBUTO_SRC[iComilla], inicioIndex) >= 0) {
				i = iComilla;
			}
		}
		
		// encuentras el src de ese tag
		buscaIndex = textoTagImg.indexOf(ATRIBUTO_SRC[i], inicioIndex);
		if (buscaIndex >= 0) {
			
			// y encuentras el final de src de ese tag
			finalIndex = textoTagImg.indexOf(FINAL_ATRIBUTO[i], buscaIndex + ATRIBUTO_SRC[i].length() + 1);
			
			// la nueva parte a reemplazar
			// nes 20110429 - quité el "cid:" del id de la imagen porque el cid va solo en el html, en la lista va sin cid
			String nuevaImagenSRC = "part"+idImagen +
						            "."+ new SimpleDateFormat("ddMMyyyy").format(new Date()) +
						            "."+ new SimpleDateFormat("HHmmssSSS").format(new Date()) +
						            "@softpoint.org";
			
			// rrl 20110503
			// Imagen incrustada en el mismo HTML (solo en tag para HTML 5)
			if (textoTagImg.substring(buscaIndex + ATRIBUTO_SRC[i].length(), finalIndex).toLowerCase().startsWith(DATA_URI_IMG)) {
				
				// extrae las partes de la sintaxis: data:[<MIME-type>][;charset=<encoding>][;base64],<data> 
				// p.e.. <img src="data:image/png;base64,iVBORw0KGgoAAAANSUh...
				int buscaIndexDataUri = buscaIndex + ATRIBUTO_SRC[i].length() + DATA_URI_IMG.length();
				int finalIndexDataUri = textoTagImg.indexOf(FINAL_SYNTAX, inicioIndex);
				
				if (finalIndexDataUri >= 0) {
					
					String mimeType = null;
					String charsetEncoding = null;
					String codification = null;
					
					// extrae las partes con el SEPARATOR_SYNTAX... image/png;base64
					String parameterDataUri = textoTagImg.substring(buscaIndexDataUri, finalIndexDataUri); 
					String[] temp = parameterDataUri.split("\\"+SEPARATOR_SYNTAX);
					
					if (temp.length > 2) {
						
						mimeType = temp[0];
						charsetEncoding = temp[1];
						codification = temp[2];

					} else {
						
						mimeType = temp[0];
						codification = temp[1];
					}
					
					String imagen = textoTagImg.substring(finalIndexDataUri + FINAL_SYNTAX.length(), finalIndex);					
					
					// en la lista al id hay que rodearlo por <> ( convención par conformar correctamente 1 email )
					listImageManagerModel.add(
							new ImageManagerModel(
									"<"+nuevaImagenSRC+">",
									null,
									mimeType, charsetEncoding, codification, imagen));
					
					// reemplaza el src
					textoTagImg.delete(buscaIndex + ATRIBUTO_SRC[i].length(), finalIndex);
					textoTagImg.insert(buscaIndex + ATRIBUTO_SRC[i].length(), "cid:"+nuevaImagenSRC);
					
					result = idImagen + 1;
				}
				
			} else {
				
				// nes 20110429 - en la lista al id hay que rodearlo por <> ( convención par conformar correctamente 1 email )
				listImageManagerModel.add(
						new ImageManagerModel(
								"<"+nuevaImagenSRC+">",  
								textoTagImg.substring(buscaIndex + ATRIBUTO_SRC[i].length(), finalIndex),
								null, null, null, null) );
				
				// reemplaza el src
				textoTagImg.delete(buscaIndex + ATRIBUTO_SRC[i].length(), finalIndex);
				textoTagImg.insert(buscaIndex + ATRIBUTO_SRC[i].length(), "cid:"+nuevaImagenSRC);
				
				result = idImagen + 1;
			}
			
		}
		
		return result;
	}
	
}
