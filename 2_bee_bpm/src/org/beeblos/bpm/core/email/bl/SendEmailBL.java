package org.beeblos.bpm.core.email.bl;

import static org.beeblos.bpm.core.util.Constants.PASS_PHRASE;
import static org.beeblos.bpm.core.util.Constants.TRAZA_ENVIO_EMAIL;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.PreencodedMimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WEmailAccountBL;
import org.beeblos.bpm.core.email.model.Email;
import org.beeblos.bpm.core.email.util.HtmlBodyModel;
import org.beeblos.bpm.core.email.util.ImageManagerModel;
import org.beeblos.bpm.core.email.util.LocalImageManager;
import org.beeblos.bpm.core.error.SendEmailException;
import org.beeblos.bpm.core.error.WEmailAccountException;
import org.beeblos.bpm.core.model.WEmailAccount;
import com.sp.common.model.FileSP;

import com.sp.common.util.DesEncrypter;

public class SendEmailBL{
	
	private static final Log logger = LogFactory.getLog(SendEmailBL.class);
	
	// NES 20110419 - PASADO A CONSTANTES
//	private static final String TRAZA_ENVIO_EMAIL = "TRAZA_ENVIO_EMAIL";
//	
//	//20110128,
//	private static final String REPORTE = "reporte";
//
//	//20110207,
//	public static final String EMAIL_MASIVO = "emailMasivo";
//	
//	//20110308,
////	private static final Integer NUM_MAX_HILOS_EMAIL = new Integer(5);
//	
//	//20110308, n=10 bloques de emails en conf
//	private static final Integer NUM_MAX_EMAIL_MASIVO = 
//		new Integer(Configuration.getConfigurationResourceBundle().getString("mail.masivo.num.maximo.bloque"));
	
	public static Long contadorEnvio = new Long("0");
	
	private String nombreFicheroFullPath;
	
	private Email email;	//HZC:20110314, 
	
	private Integer threadName;	// nes 20110420 - pasé a integer  
	
	
	
	private Properties properties = new Properties();//HZC:20110303
	
	static String password;

	Session javamailSession;	//HZC:20110407
	
	static WEmailAccount wEmailAccount;

	
	
	public SendEmailBL(){
	}
	
	
	// envio de 1 email
	//rrl 20111130 cambio el void por el Integer del docid de guardar en Beeblos 
	public Integer enviar(Email email) throws SendEmailException {

		Integer result = null;  //rrl 20111130
		
		createJavaMailSession(email);
		
		try{
			
			MimeMessage message = new MimeMessage(javamailSession);

			// datos comunes a todos los email ( from, subject, traza, sent date )
			seteaDatosComunes(email, message);	
			
	        for(String to: email.getListaTo()){
	        	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	        }
	        
	        componeBodyEInsertaAdjuntos(email, message);

			enviaEmail(email, message);
			
		}catch (MessagingException e){
			String mensaje = "MessagingException: enviar: Error ("+ e.getClass().getName()+"): "+ e.getMessage() +" - "+e.getCause();
			logger.error( mensaje );
			throw new SendEmailException( mensaje );
		} catch (Exception e) {
			String mensaje = "Exception; enviar: Error ("+ e.getClass().getName()+"): "+ e.getMessage() +" - "+e.getCause();
			logger.error( mensaje );
			throw new SendEmailException( mensaje );
		}
		
		return result;
	}



	// Compone el elemento de tipo MimeMessage ( javax.mail.internet ) a partir del objeto "email" nuestro
	// que tiene la info para componerlo
	public void envioMasivo(Email email) throws SendEmailException{
		
		logger.info("--->>> envioMasivo: cantidad:["+email.getListaTo().size()+"]");

		
		createJavaMailSession(email);
	
		try {
				MimeMessage message = new MimeMessage(javamailSession);

				// datos comunes a todos los email ( from, subject, traza, sent date )
				seteaDatosComunes(email, message);				
				
				// datos propios del email masivo ...
				
				// setea el to ( que en el caso del masivo va en el bcc para que no se vean el resto de los destinatarios )
				for(String to: email.getListaTo()){
		        	agregaDireccionAlTo(message, to);
		        }

		        // limpia el CC de nuestro objeto email para que solo se envie 1 copia ( porque si no en el masivo se enviarian tantas
		        // como la cantidad de veces que se llame a este método que se llama muchas ...
		        email.getListaCC().clear();
		        
		        componeBodyEInsertaAdjuntos(email, message);

				enviaEmail(email, message);
				
				grabaLoggerInfoEmailEnviado(email);
				
		} catch (MissingResourceException e) {
			String mensaje = "MissingResourceException: Error ("+ e.getClass().getName()+"): "+ e.getMessage() +" - "+e.getCause();
			logger.error( mensaje );
			throw new SendEmailException( mensaje );
		} catch (Exception e) {
			String mensaje = "SendEmailException: Error ("+ e.getClass().getName()+"): "+ e.getMessage() +" - "+e.getCause();
			logger.error( mensaje );
			throw new SendEmailException( mensaje );
		}
		
	}



	private void agregaDireccionAlTo(MimeMessage message, String to) {
		
		String[] lista;
		if ( to.contains(",") ) {
			lista = to.split(",");
		} else if ( to.contains(";")) {
			lista = to.split(";");
		} else {
			lista=null;
		}
		
		
		try {
			
			if ( lista == null ) {
			
				message.addRecipient(Message.RecipientType.BCC, new InternetAddress(to));
			
			} else {
				
				for ( String s: lista ) {
					try {

						message.addRecipient(Message.RecipientType.BCC, new InternetAddress(s));
					
					} catch (Exception e) {
						String mensaje = "Exception: agregaDireccionAlTo: Error al intentar agregar el email ["+ s +"] a la lista de 'para' ("+ e.getClass().getName()+"): "+ e.getMessage() +" - "+e.getCause();
						logger.error( mensaje );
					}
				}
				
			}
			
		} catch (AddressException e) {
			
			String mensaje = "MessagingException: agregaDireccionAlTo: Error al intentar agregar un email a la lista de 'para' ("+ e.getClass().getName()+"): "+ e.getMessage() +" - "+e.getCause();
			logger.error( mensaje );
			e.printStackTrace();
			logger.error("Sigue con la próxima dirección ..." );

		}catch (MessagingException e){
			
			String mensaje = "MessagingException: agregaDireccionAlTo: Error al intentar agregar un email a la lista de 'para'  ("+ e.getClass().getName()+"): "+ e.getMessage() +" - "+e.getCause();
			logger.error( mensaje );
			e.printStackTrace();
			logger.error("Sigue con la próxima dirección ..." );
		}

	}

	// crea el java mail session a partir de los parámetros de la cuenta de email 
	// elegida por el usuario ( cuenta, server, puerto, tipo de autenticación, usuario, passwd, etc )
	private void createJavaMailSession(Email email) throws SendEmailException {
		
		//instanciamos WEmailAccountBL(usuario de app)
		WEmailAccountBL wEmailAccountBL = new WEmailAccountBL();
		
		try {
			//obtenemos propiedades de wEmailAccount
			wEmailAccount = wEmailAccountBL.getWEmailAccountByPK(email.getIdFrom());
		} catch (WEmailAccountException ucee) {
			logger.error("init(): No se pudo obtener WEmailAccount");
		}		
		
		properties.put("mail.smtp.host", wEmailAccount.getOutputServer());
        properties.put("mail.smtp.starttls.enable", wEmailAccount.getOutputConnectionSecurity().equalsIgnoreCase("starttls")?"true":"false");
		properties.put("mail.smtp.port", wEmailAccount.getOutputPort());
		properties.put("mail.smtp.mail.sender", wEmailAccount.getEmail());//q puede ser cualquier email
		properties.put("mail.smtp.user", wEmailAccount.getOutputUserName()); //usuario validado para enviar mail
		properties.put("mail.smtp.auth", "true");
		javamailSession = Session.getInstance(properties);//HZC:20110407
	}
	
	private void seteaDatosComunes(Email email, MimeMessage message) throws SendEmailException
			 {
		/*HZC:20110209, el tema de la lista o Alias es propio de quien envia el correo o mailList */
		/*en caso de tratarse de lista setear alias al from*/
		
		try {
		message.setFrom(
				new InternetAddress(
						wEmailAccount.getReplyTo(),
						//rrl 20110721 Mostrar el "from" del email con este formato: nombre de cuenta [email] - nes 20110721: getUceDireccionDeRespuesta
//						wEmailAccount.getUceNombre() + "["+ wEmailAccount.getUceDireccionDeRespuesta() +"]") ); 
					
						//mrico 20110831 - Se pone solo el nombre, ya que el gestor de correo ya pondra la direccion a contituacion
						wEmailAccount.getName()) ); 
						
//						"".equals(email.getTo())? wEmailAccount.getUceEmail():email.getTo()) );
						//mrico 20110608 No hay que poner el TO sino el remitente
//						"".equals(email.getFrom())? wEmailAccount.getUceEmail():email.getFrom()) );
		

        for(String cc: email.getListaCC()){
        	message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
        }
		
		//HZC: 20110207, Aqui agregaremos un objeto en la cabecera de email x cuestiones de trazabilidad
		message.addHeader(TRAZA_ENVIO_EMAIL, email.getIdEnvio());
		
		message.setSentDate( new Date() );//HZC:20110407
		
		//mrico 20110607 - Direccion para la respuesta
//		InternetAddress replyAdress = new InternetAddress(email.getFrom());
//		InternetAddress[] replyList = {replyAdress};
//		message.setReplyTo(replyList); 
		
		
		message.setSubject( email.getSubject() );
		}catch (MessagingException e){
			String mensaje = "seteaDatosComunes: Error ("+ e.getClass().getName()+"): "+ e.getMessage() +" - "+e.getCause();
			logger.error( mensaje );
			throw new SendEmailException( mensaje );
		} catch (UnsupportedEncodingException e) {
			String mensaje = "seteaDatosComunes: Error ("+ e.getClass().getName()+"): "+ e.getMessage() +" - "+e.getCause();
			logger.error( mensaje );
			throw new SendEmailException( mensaje );
		}
	}

	private void grabaLoggerInfoEmailEnviado(Email email) {
		//HZC:20110308, informar en background sobre el estatus del envio
		//HZC:20110308, por el momento se informara de los destinatarios(solo email) enviados
//						logger.info("SendEmailBL: envio masivo sincrono: destinatarios enviados:" +messagePart.toString());
		
		StringBuffer sb = new StringBuffer();
		logger.info("inicio destinatarios enviados:" + sb.toString());
		for(String destinatarioEmail : email.getListaTo()){
			sb.append(destinatarioEmail).append(";");
		}
		logger.info("fin destinatarios enviados:" + sb.toString());
	}

	private void componeBodyEInsertaAdjuntos(Email email, MimeMessage message) 
	throws SendEmailException	{
		// composición del mensaje en sí. Dependiendo de si es HTML o solo texto se configura de una forma u otra

		try {
		
			if(email.isHtmlFormatted()){
	
				MimeMultipart multipart = new MimeMultipart("related");  // define el email como multipart para html
				
				// revisa el email html y convierte las imágenes locales o extrae el data si están autocontenidas ( HTML5 )
				// para eso se apoya en el objeto de tipo HtmlBodyModel que devuelve el body y una lista con pares identificador / imagen en fs
				// para componer el email
				LocalImageManager lim = new LocalImageManager();
				
				HtmlBodyModel convertedBody = 
					lim.convertHtmlBodyToEmailBody( email.getBodyHtml(), email.getContextPath() ); 
			    
				BodyPart bp = new MimeBodyPart();
				bp.setContent( convertedBody.getBody(), "text/html" );
				  
			    multipart.addBodyPart(bp); // agrega el body al email
			    
				//rrl 20110726 agrega la firma al pie del body
				if (email.isUsarFirmaEmail()) {
					addFirmaEmail(multipart, email.isHtmlFormatted());
				}
			    
				if ( convertedBody.getImageList()!= null ) {
					
					for ( ImageManagerModel imageData: convertedBody.getImageList() ) {

						// Add part to multi-part
					    multipart.addBodyPart( seteaImagen(email, imageData) );	
					
					}
				}
				
				agregaFicherosAdjuntos(email, multipart);
				
				message.setContent(multipart); // inserta el html y sus imágenes asociadas Y ADJUNTOS al mensaje
	
			} else {
	
				MimeMultipart multipart = new MimeMultipart();  // define el email solo texto
				MimeBodyPart mbp = new MimeBodyPart(); 
				mbp.setText(email.getBodyText()==null?"":email.getBodyText());
				
				multipart.addBodyPart(mbp);
				
				//rrl 20110726 agrega la firma al pie del body
				if (email.isUsarFirmaEmail()) {
					addFirmaEmail(multipart, email.isHtmlFormatted());
				}
				
				agregaFicherosAdjuntos(email, multipart);
				
				message.setContent(multipart); // inserta el html y sus imágenes asociadas Y ADJUNTOS al mensaje
				
			}
		
		} catch (MessagingException e){
			String mensaje = "componeBodyEInsertaAdjuntos: Error ("+ e.getClass().getName()+"): "+ e.getMessage() +" - "+e.getCause();
			logger.error( mensaje );
			throw new SendEmailException( mensaje );
		} catch (Exception e){
			String mensaje = "componeBodyEInsertaAdjuntos: Exception: Error ("+ e.getClass().getName()+"): "+ e.getMessage() +" - "+e.getCause();
			e.printStackTrace();
			logger.error( mensaje );
			throw new SendEmailException( mensaje );

		}
	}

	//rrl 20120130 Descomentado el ELSE de if (isHtmlFormatted)
    private void addFirmaEmail(MimeMultipart multipart, boolean isHtmlFormatted) throws MessagingException, IOException {

    	boolean anyFirma = false;
        String insertaFirma = null;
        

		if (isHtmlFormatted) {

//	        if (wEmailAccount.getUceFirmaAdjuntaHtml()!=null && !"".equals(wEmailAccount.getUceFirmaAdjuntaHtml())) {
//	        	insertaFirma = "<tbody><br><br>" + wEmailAccount.getUceFirmaAdjuntaHtml() + "</tbody>";
//	        	anyFirma = true;
//	        } else if (wEmailAccount.getUceFirmaAdjuntaTxt()!=null && !"".equals(wEmailAccount.getUceFirmaAdjuntaTxt())) {
//	        	insertaFirma = "<tbody><br><br>" + wEmailAccount.getUceFirmaAdjuntaTxt().replaceAll("(\r\n|\n)", "<br>") + "</tbody>";
//	        	anyFirma = true;
//	        }
//	        
//	        if (anyFirma = true) {
				BodyPart bp = new MimeBodyPart();
				bp.setContent( wEmailAccount.getSignatureHtml(), "text/html" );
			    multipart.addBodyPart(bp); // agrega el body al email
//	        }
	        
		} else {

	        insertaFirma = "";
			if (multipart.getCount()>0) {
				MimeBodyPart mbpAnt = (MimeBodyPart) multipart.getBodyPart(multipart.getCount()-1);
		        String content = (String) mbpAnt.getContent();
		        
		        if (!content.endsWith("\n")) {
		        	insertaFirma = "\r\n\r\n";
		        }
			}
	        
			
	        if (wEmailAccount.getSignatureTxt()!=null && !"".equals(wEmailAccount.getSignatureTxt())) {
	        	insertaFirma += wEmailAccount.getSignatureTxt();
	        	anyFirma = true;
	        } else {
	        	insertaFirma += wEmailAccount.getSignatureHtml();				
	        	anyFirma = true;
	        }
			
	        if (anyFirma = true) {
				MimeBodyPart mbp = new MimeBodyPart(); 
				mbp.setText(insertaFirma);
				multipart.addBodyPart(mbp);
	        }
			
		}
    }	
	
	
	// nes 20110503
	private BodyPart seteaImagen(Email email, ImageManagerModel imageData)
			throws MessagingException {

		try {
		
			// si la imagen viene como link al file system, la leo, codifico ( base64 ) y cargo
			if ( imageData.getFileName()!=null && !"".equals( imageData.getFileName() ) ) {
				
				BodyPart embedImage=new MimeBodyPart();
			
				String imagenLocal =  email.getContextPath()+imageData.getFileName();
	
				DataSource ds=new FileDataSource( imagenLocal );
				
				embedImage.setDataHandler(new DataHandler(ds));
				embedImage.setHeader( "Content-ID", imageData.getId() );
				embedImage.setHeader("Content-Type",ds.getContentType() );
				
				return embedImage;
				// si la imagen ya está embebida en el html ( html5 ) la cargo directamente
			} else if ( imageData.getId()!=null && !"".equals(imageData.getId() ) ) {
				
				PreencodedMimeBodyPart pembp = new PreencodedMimeBodyPart(imageData.getCodification()); 
	
				pembp.setContent(imageData.getImagen().getBytes(), imageData.getMimeType() );
				pembp.setHeader( "Content-ID", imageData.getId() );
				pembp.setHeader("Content-Type",imageData.getMimeType() );
				
				return pembp;
				
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("seteaImagen: Error al intentar cargar imagen local:"+e.getClass()+" - "+e.getMessage()+" - "+e.getCause());
			e.printStackTrace();
		}
		return null;
	}

	private void enviaEmail(Email email, MimeMessage message) throws SendEmailException
			 {

		try {
			Transport t = javamailSession.getTransport("smtp");
			//HZC:20110407, desencriptar pwd Usuario Autenticado obtenido de la app
			DesEncrypter encrypter = new DesEncrypter(PASS_PHRASE);
			String pwd = null;
			try{
				pwd = encrypter.decrypt(email.getPwd());	
				if(pwd == null) {
					logger.debug("Error en las credenciales cifradas de la cuenta de correo");
					throw new SendEmailException("Error en las credenciales cifradas de la cuenta de correo");
				}
			} catch (Exception e) {
			}
			
			t.connect(properties.get("mail.smtp.user").toString(), pwd);	//HZC:20110407				
			t.sendMessage(message, message.getAllRecipients());
			t.close();

		} catch (Exception e) {
			String mensaje = "enviaEmail: Error al intentar enviar el email :"+e.getClass()+" - "+e.getMessage()+" - "+e.getCause();
			logger.error( mensaje );
			e.printStackTrace();
			throw new SendEmailException( mensaje );
		}

	}

	private void agregaFicherosAdjuntos(Email email, MimeMultipart multipart)
			throws MessagingException {
		// Seteamos los ficheros adjuntos
		for(FileSP file : email.getFiles()){
			MimeBodyPart attachmentPart = setAttachment(file);
			multipart.addBodyPart(attachmentPart);
		}
	}

	
	// nes 20110428 - adjunta un fichero 
	// nes 20111103 - catch por si no puede recuperar el fichero
	private MimeBodyPart setAttachment(FileSP file) throws MessagingException {

		MimeBodyPart attachmentPart = new MimeBodyPart();
		
		try {

			ByteArrayDataSource byteArrayDataSource;
	
			byteArrayDataSource = new ByteArrayDataSource( file.getData(), file.getMime() );
			
			attachmentPart.setDataHandler(new DataHandler(byteArrayDataSource));
			attachmentPart.setFileName(file.getName());
			attachmentPart.setHeader("Content-Type" , file.getMime());

		} catch (IOException e) {
			String mensaje="SendEmailBL: setAttachment:Ocurrio Un Error al intentar recuperar el fichero: ["+file.getAbsolutePath()+"] " 
			+ e.getMessage() + " : " + e.getCause()+" "+e.getClass().toString();

			logger.error(mensaje);
			e.printStackTrace();
			throw new MessagingException(mensaje);
			
		}

		
		return attachmentPart;
	}	
	

	
	//HZC:20110407, desencriptar la clave para testear la cuenta email
	public void testCredenciales(Email email) throws SendEmailException{
		createJavaMailSession(email);
		try{
		    DesEncrypter encrypter = new DesEncrypter(PASS_PHRASE);
		    String pwd = null;
		    try{
		    	pwd = encrypter.decrypt(email.getPwd());	//HZC:20110407
		    	if(pwd == null) {
		    		logger.debug("Error en las credenciales cifradas de la cuenta de correo");
		    		throw new SendEmailException("Error en las credenciales cifradas de la cuenta de correo");
		    	}
			} catch (Exception e) {
			}
			
			
			javamailSession.getProperties().put("mail.smtp.pwd", pwd);
			Transport t = javamailSession.getTransport("smtp");
			t.connect(properties.get("mail.smtp.user").toString(), pwd);////HZC:20110407:pwd desencriptado
			t.close();
			
		} catch (NoSuchProviderException nspe) {
			logger.error("Error: "+ nspe.getMessage());
			throw new SendEmailException("Error: "+ nspe.getMessage());
		} catch (MessagingException me) {
			logger.error("Error: "+ me.getMessage());
			throw new SendEmailException("Error: "+ me.getMessage());
		} catch (Exception e) {
			logger.error("Error: "+ e.getMessage());
			throw new SendEmailException("Error: "+ e.getMessage());
		}
	}			

	public String getNombreFicheroFullPath() {
		return nombreFicheroFullPath;
	}

	public void setNombreFicheroFullPath(String nombreFicheroFullPath) {
		this.nombreFicheroFullPath = nombreFicheroFullPath;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public Integer getThreadName() {
		return threadName;
	}

	public void setThreadName(Integer threadName) {
		this.threadName = threadName;
	}



}
