package org.beeblos.bpm.email.impl;

import static com.email.core.util.ConstantsEmailCore.AUTH_NTLM_DISABLE;
import static com.email.core.util.ConstantsEmailCore.AUTH_PLAIN_DISABLE;
import static com.email.core.util.ConstantsEmailCore.INPUT;
import static com.email.core.util.ConstantsEmailCore.MAIL;
import static com.email.core.util.ConstantsEmailCore.OUTPUT;
import static com.email.core.util.ConstantsEmailCore.POP;
import static com.email.core.util.ConstantsEmailCore.POP3;
import static com.email.core.util.ConstantsEmailCore.SAVE_IN_BEEBLOS;
import static com.email.core.util.ConstantsEmailCore.SAVE_IN_EMAIL_TRAY;
import static com.email.core.util.ConstantsEmailCore.TRUE_STRING;
import static com.sp.common.core.util.ConstantsSPC.BEEBLOS_DEFAULT_REPOSITORY_ID;
import static com.sp.common.util.ConstantsCommon.JAVA_IO_TEMPDIR;
import static com.sp.common.util.ConstantsCommon.MESSAGE_ID;
import static com.sp.common.util.ConstantsCommon.PASS_PHRASE;
import static com.sp.common.util.ConstantsCommon.POINT_SYMBOL;
import static com.sp.daemon.util.ConstantsED.STARTING_NEW_POLL;
import static com.sp.daemon.util.ConstantsED.STOP_THREAD;
import static com.sp.daemon.util.ConstantsED.THE_POLL_HAS_FINISHED_OK;
import static org.beeblos.bpm.core.util.Constants.BEEBPM_TMP_FOLDER;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.beeblos.wsapi.model.BeeblosException;
import com.email.core.bl.SendEmailBL;
import com.email.core.error.SendEmailException;
import com.email.core.error.UserEmailAccountException;
import com.email.core.model.Email;
import com.email.tray.core.bl.EmailTrayBL;
import com.email.tray.core.error.CreateEmailException;
import com.email.tray.core.error.EmailTrayAttachmentException;
import com.email.tray.core.error.EmailTrayException;
import com.email.tray.core.error.NullEmailTrayException;
import com.email.tray.core.model.EmailTray;
import com.email.tray.core.util.EmailPersonalizationUtilBL;
import com.email.tray.core.util.EmailUtilBL;
import com.email.tray.core.util.JavaxMailMessageUtilBL;
import com.sp.common.core.bl.BeeblosBL;
import com.sp.common.core.bl.UserBL;
import com.sp.common.core.error.BeeblosBLException;
import com.sp.common.core.error.UserException;
import com.sp.common.core.model.User;
import com.sp.common.util.Configuration;
import com.sp.common.util.DesEncrypter;
import com.sp.daemon.bl.DaemonLogBL;
import com.sp.daemon.bl.DaemonPollBL;
import com.sp.daemon.email.EmailDConf;
import com.sp.daemon.email.EmailDLog;
import com.sp.daemon.email.EmailDPoll;
import com.sp.daemon.error.DaemonLogException;
import com.sp.daemon.error.DaemonPollException;
import com.sp.daemon.error.DaemonPollerException;
import com.sp.daemon.exe.DaemonClassInterface;
import com.sp.daemon.model.DaemonConf;

public class EmailDaemonPollerClassImpl implements DaemonClassInterface {
	
	private static final Log logger = LogFactory.getLog(EmailDaemonPollerClassImpl.class.getName());

	EmailTrayBL emailTrayBL = new EmailTrayBL();

	private static final Integer EMAIL_TRAY_ID_NULL=null;
	
	private Session session;

	private Store store; 

	private Folder inboxFolder;
	private Folder validEmailFolder;
	private Folder invalidEmailFolder;
	private Folder errorEmailFolder;

	private Integer qtyInputEmail = 0;
	private Integer qtyValidEmail = 0;
	private Integer qtyInvalidEmail = 0;
	private Integer qtyErrorEmail = 0;
	
	public EmailDaemonPollerClassImpl() {

		logger.debug("PASA POR EL CONSTRUCTOR DE ProyectoActividadDaemonPollerClassImpl");
		_init();

	}

	private void _init() {
	}

	@Override
	public void initialiceDaemonPoller(DaemonConf conf, Integer currentUserId) {

		if (conf instanceof EmailDConf){
			
			EmailDConf emailDConf = (EmailDConf) conf;

			this.configureSessionAndVariables(emailDConf);
			
		} else {
			//TODO THROW EXCEPTION
		}
		
	}

	/**
	 *  If all is ok returns true. If there is any problem returns false
	 *  
	 * @param conf
	 */
	private void configureSessionAndVariables(EmailDConf conf) {
		
		try {

			/**
			 * Set "session" Class property
			 */
			this.createEmailServerInstance(conf.getEmailAccount().getIngoingServerType()); 
			
			/**
			 * Open email server and check work folder existence
			 */
			this.connectEmailServer(conf);
			
			/**
			 * Load the folder names from the configuration
			 */
			this.loadEmailServerWorkFolders(conf);
			
		} catch (Exception e) {
			
			String mess = e.getClass().getSimpleName() + " stopping email daemon for account :" 
					+ conf.getEmailAccount().getEmail() + " EmailDConf id:("+conf.getId()+")"
					+" can't create session for email server";
			logger.error(mess);
			System.out.println(mess);
			e.printStackTrace();

		}

	}

	/**
	 * Checks work folders validity on email server
	 * 
	 * @param conf
	 */
	private void loadEmailServerWorkFolders(EmailDConf conf) {
		try {

			this.inboxFolder = store.getFolder(conf.getInputFolder());

			this.validEmailFolder = store.getFolder(conf.getValidEmailFolder());

			this.invalidEmailFolder = store.getFolder(conf.getInvalidEmailFolder());

			this.errorEmailFolder = store.getFolder(conf.getErrorEmailFolder());

		} catch (MessagingException e) {
			String mess = "[MessagingException: loadWorkFolders] Error checking working folders: "
					+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			System.out.println(mess);
			e.printStackTrace();
		} 
	}

	/**
	 * Connects with email server and load "store" property
	 * 
	 * @param conf
	 * 
	 * @throws DaemonPollerException
	 */
	private void connectEmailServer(EmailDConf conf)
			throws DaemonPollerException {

		try {
			String protocol = this._setProtocol(conf.getEmailAccount().getIngoingServerType());
	
			this.store = this.session.getStore(protocol);
			
			String decryptedPassword = this._decryptPassword(conf.getEmailAccount().getPassword());

			this.store.connect(
					conf.getEmailAccount().getIngoingServerName(), // host
					conf.getEmailAccount().getUser(), // username
					decryptedPassword); // pwd
			
		} catch (MessagingException e) {
			String mess="[MessagingException: connectEmailServer] Error trying to connect with the server:"
					+" - " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			System.out.println(mess);
			e.printStackTrace();
			throw new DaemonPollerException(e);
		} catch (SendEmailException e) {
			String mess="[MessagingException: connectEmailServer] Error trying to connect with the server:"
					+" - " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			System.out.println(mess);
			e.printStackTrace();
			throw new DaemonPollerException(e);
		}

	}

	private String _setProtocol(String serverType) {
		String protocol;
		if (serverType.equals(POP)) {
			protocol=POP3;
		} else {
			protocol=serverType;
		}
		return protocol;
	}
	
	private String _decryptPassword(String password) throws SendEmailException{
		
	    DesEncrypter encrypter = new DesEncrypter(PASS_PHRASE);
	    String pwd = null;
	    	pwd = encrypter.decrypt(password);
	    	if(pwd == null) {
	    		logger.debug("Error decrypting password");
	    		throw new SendEmailException("Error decrypting password");
	    	}
	    	
		return pwd;

	}

	private void createEmailServerInstance(String serverType) throws DaemonPollerException {
		
		String protocol = this._setProtocol(serverType);
		
		Properties props = new Properties();
		props.put(MAIL + POINT_SYMBOL + protocol + AUTH_PLAIN_DISABLE, TRUE_STRING);
		props.put(MAIL + POINT_SYMBOL + protocol + AUTH_NTLM_DISABLE, TRUE_STRING);

		this.session = Session.getInstance(props, null);
		
	}


	@Override
	public void daemonAction(DaemonConf conf, Integer currentUserId) throws DaemonPollerException {

		if (conf instanceof EmailDConf){
			
			EmailDConf emailDConf = (EmailDConf) conf;

			/**
			 * Initialize email work folders
			 */
			this.openEmailServerWorkFolders();
			
			/**
			 * Check for new email in input folder and process it!
			 */
			this.processEmailInbox(emailDConf, currentUserId);
			
			this.closeEmailServerWorkFolders();

		} else {
			//TODO THROW EXCEPTION
		}
		
	}
	
	private void openEmailServerWorkFolders() {
		
		try {

			this.inboxFolder.open(Folder.READ_WRITE);

		} catch (MessagingException e) {
			String mess = "[MessagingException: openEmailServerWorkFolders] Can't open email server INPUT folder :"
					+ this.inboxFolder.getName() + ". " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			e.printStackTrace();
		}

		try {
			if (this.validEmailFolder != null) {
				this.validEmailFolder.open(Folder.READ_WRITE);
			}

		} catch (MessagingException e) {
			String mess = "[MessagingException: openEmailServerWorkFolders] Can't open email server VALID EMAILS folder :"
					+ this.validEmailFolder.getName() + ". " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			e.printStackTrace();
		}

		try {

			if (this.invalidEmailFolder != null) {
				this.invalidEmailFolder.open(Folder.READ_WRITE);
			}

		} catch (MessagingException e) {
			String mess = "[MessagingException: openEmailServerWorkFolders] Can't open email server INVALID EMAILS folder :"
					+ this.invalidEmailFolder.getName() + ". " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			e.printStackTrace();
		}

		try {

			if (this.errorEmailFolder != null) {
				this.errorEmailFolder.open(Folder.READ_WRITE);
			}

		} catch (MessagingException e) {
			String mess = "[MessagingException: openEmailServerWorkFolders] Can't open email server ERROR EMAILS folder :"
					+ this.errorEmailFolder.getName() + ". " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			e.printStackTrace();
		}


	}

	private void closeEmailServerWorkFolders() {
		
		try {

			this.inboxFolder.close(true);

		} catch (MessagingException e) {
			String mess ="[MessagingException: closeEmailServerWorkFolders] Can't close email server INPUT folder :"
					+inboxFolder.getName()
					+" - "+ e.getMessage()
					+" - "+e.getCause();
			logger.error(mess);
			e.printStackTrace();
		}

		try {
			if (this.validEmailFolder != null) {
				this.validEmailFolder.close(true);
			}
			
		} catch (MessagingException e) {
			String mess ="[MessagingException: closeEmailServerWorkFolders] Can't close email server VALID EMAILS folder :"
							+validEmailFolder.getName()
							+" - "+ e.getMessage()
							+" - "+e.getCause();
			logger.error(mess);
			e.printStackTrace();
		}
		
		try {

			if ( invalidEmailFolder != null) {
				this.invalidEmailFolder.close(true);			
			}
			
		} catch (MessagingException e) {
			String mess ="[MessagingException: closeEmailServerWorkFolders] Can't close email server INVALID EMAILS folder :"
					+invalidEmailFolder.getName()
					+" - "+ e.getMessage()
					+" - "+e.getCause();
			logger.error(mess);
			e.printStackTrace();
		}		

		try {

			if (this.errorEmailFolder != null) {
				this.errorEmailFolder.close(true);			
			}
			
		} catch (MessagingException e) {
			String mess ="[MessagingException: closeEmailServerWorkFolders] Can't close email server ERROR EMAILS folder :"
					+invalidEmailFolder.getName()
					+" - "+ e.getMessage()
					+" - "+e.getCause();
			logger.error(mess);
			e.printStackTrace();
		}		

		
	}	

	private void processEmailInbox(EmailDConf conf, Integer currentUserId) throws DaemonPollerException {
		
		/**
		 * Obtain inbox message list
		 */
		Message[] messages;
		try {
			
			messages = this.inboxFolder.getMessages();
			
		} catch (MessagingException e) {
			String mess="MessagingException: can't obtain message list from inbox folder "
					+" -- "+e.getMessage()+" "+e.getCause();
			logger.error(mess);
			
			throw new DaemonPollerException(mess);
		} 
		
		qtyInputEmail += messages.length;
		
		for (Message message: messages) {
			
			try {

				if (this.checkEmailIsValid(message)){
					
					this.processValidEmail(conf, message, currentUserId);

				} else {

					this.processInvalidEmail(conf, message, currentUserId);

				}

				// captures MessagingException, IOException, NullEmailTrayException and EmailTrayException
			} catch (Exception e) {

				String mess="ERROR processing message - It will be move to revision folder: "
						+e.getMessage()+" - "+e.getCause()+" - "+e.getClass();
				logger.error(mess);
				
				qtyErrorEmail++;

				this.processErrorEmail(conf, message);

			}

		}
	}
	
	private boolean checkEmailIsValid(Message message){
		
		// NESTOR: TODO IMPLEMENTAR COMPROBACION DE SI EMAIL ES VALIDO
		
		return true;
	}

	private void processInvalidEmail(EmailDConf conf, Message message, Integer currentUserId) {
		
		String messageId[]={};
		try {
			
			messageId = message.getHeader(MESSAGE_ID); 
			
			message.setFlag(Flag.SEEN, true);

			inboxFolder.copyMessages(
					new Message[] { message }, invalidEmailFolder);

			message.setFlag(Flag.DELETED, true);

			this.createEmailDLogRegister(
					conf, EMAIL_TRAY_ID_NULL, 
					message, conf.getInvalidEmailFolder());
		
			// dml 20121025 - if it is an INPUT account we do the same, and if it is not we redirect the email to the correct account with the default input account
			if (conf.getType().equals(INPUT)){
				
				String className = null; // NESTOR: TODO OBJETO AL QUE VAMOS A ASOCIAR EL EMAILTRAY
				Integer objectId = null; // NESTOR: TODO IDENTIFICADOR DEL OBJETO AL QUE VAMOS A ASOCIAR EL EMAIL

				//NESTOR TODO: ESTO SE USARÍA SI POR EJEMPLO VAMOS A GUARDAR UNA TRAZA DE EMAILS DEL MISMO TEMA, 
				// CON ESTO GUARDARIAMOS EL CAMPO DE EMAIL_TRAY "original_subject" CORRECTAMENTE
				String VALID_SUBJECT_STRING_IDENTIFIER = null;
				
				// inserts email in email_tray table
//				Integer emailTrayId = emailTrayBL
				emailTrayBL
					.createInputEmailTrayRegister(
							message, objectId, className, 
							currentUserId, JAVA_IO_TEMPDIR, BEEBPM_TMP_FOLDER,
							VALID_SUBJECT_STRING_IDENTIFIER); 

				//this.processNotifications(conf, emailTrayId, appUrl);
				
			}
			
			qtyInvalidEmail++;
			
		} catch (Exception e) {
			String mess="ERROR processing message from inbox - trying move to revision folder: ["
					+ conf.getInvalidEmailFolder()
					+"] id:"
					+messageId[0] + " - "
					+message.getMessageNumber();
			logger.error(mess);
			System.out.println(mess);
			e.printStackTrace();
			
			qtyErrorEmail++;

			this.processErrorEmail(conf, message); // dml 20121009

		}
	}

	private void processErrorEmail(EmailDConf conf, Message message) {
		
		try {

			// update message status in email server
			message.setFlag(Flag.SEEN, true);

			inboxFolder.copyMessages(
					new Message[] { message }, errorEmailFolder);

			message.setFlag(Flag.DELETED, true);


		} catch (MessagingException e) {
			String mess="ERROR processing message from inbox - trying move to error folder: ["
					+ e.getMessage()+" - "+e.getCause();
			logger.error(mess);
		}
	}

	private void processValidEmail(EmailDConf conf, Message message, Integer currentUserId) 
			throws MessagingException, EmailTrayException, IOException, NullEmailTrayException, 
			UserEmailAccountException, EmailTrayAttachmentException {
		
		message.setFlag(Flag.SEEN, true);

		inboxFolder.copyMessages(
				new Message[] { message }, validEmailFolder);

		message.setFlag(Flag.DELETED, true);

		Integer emailTrayId = null;
		
		// dml 20121025 - if it is an INPUT account we save the received email message
		if (conf.getType().equals(INPUT)){
			
			String className = null; // NESTOR: TODO OBJETO AL QUE VAMOS A ASOCIAR EL EMAILTRAY
			Integer objectId = null; // NESTOR: TODO IDENTIFICADOR DEL OBJETO AL QUE VAMOS A ASOCIAR EL EMAIL

			//NESTOR TODO: ESTO SE USARÍA SI POR EJEMPLO VAMOS A GUARDAR UNA TRAZA DE EMAILS DEL MISMO TEMA, 
			// CON ESTO GUARDARIAMOS EL CAMPO DE EMAIL_TRAY "original_subject" CORRECTAMENTE
			String VALID_SUBJECT_STRING_IDENTIFIER = null;

			// Inserts email message into email_tray table
			emailTrayId = emailTrayBL.createInputEmailTrayRegister(
						message, objectId, className, currentUserId, 
						JAVA_IO_TEMPDIR, BEEBPM_TMP_FOLDER, VALID_SUBJECT_STRING_IDENTIFIER); 

			this.notifyNewEmailReception(conf, emailTrayId, currentUserId);
			
		// If it is an output account...
		} else if (conf.getType().equals(OUTPUT)) {
		
			
			// IMPLEMENTAR DE SER NECESARIO, DE MOMENTO SOLO VAMOS A USAR AQUÍ UNA CUENTA QUE
			// RECIBA EMAILS Y LOS GESTIONE, NO QUEREMOS ENVIAR NADA...
			

		}

		if (emailTrayId != null){
			this.writeLogAndBeeblosBackup(conf, message, emailTrayId);
		} else {
			logger.error("Impossible to create email tray register and save a email copy in beeblos");
		}

		qtyValidEmail++;
		
	}

	private void writeLogAndBeeblosBackup(EmailDConf conf, Message message,
			Integer emailTrayId) {
		try {
			
			
			this.createEmailDLogRegister(conf, emailTrayId, message, conf.getValidEmailFolder());
			
		} catch (DaemonLogException e) {
			String mess="error EmailDLogException trying to save emailDLog : ["
			+ e.getMessage()+" - "+e.getCause()
			+"] ";
			logger.warn(mess);
		}
		
		try {

			this.saveValidEmailInBeeblosAndInFileSystem(
					message, emailTrayId,
					BEEBLOS_DEFAULT_REPOSITORY_ID, 
					conf.getEmailAccount().getName());


		} catch (BeeblosException e) {
			String mess="error BeeblosException trying to save message to beeblos/fs : ["
			+ e.getMessage()+" - "+e.getCause()
			+"] ";
			logger.error(mess);
		} catch (IOException e) {
			String mess="error IOException trying to save message to beeblos/fs : ["
			+ e.getMessage()+" - "+e.getCause()
			+"] ";
			logger.error(mess);				
		} catch (MessagingException e) {
			String mess="error MessagingException trying convert email message to save message to beeblos/fs : ["
			+ e.getMessage()+" - "+e.getCause()
			+"] ";
			logger.error(mess);	
		}
	}

	/**
	 * Save ingoing message in beeblos
	 * 
	 * @author dmuleiro 20120926
	 * 
	 * @param message
	 * @param emailTrayId
	 * @param repositoryId
	 * @param userLogin
	 * @return
	 * @throws MessagingException
	 * @throws BeeblosException
	 * @throws IOException
	 */
	private Integer saveValidEmailInBeeblosAndInFileSystem(Message message, 
			Integer emailTrayId, Integer repositoryId , String userLogin)
			throws MessagingException, BeeblosException, IOException {

		Integer returnValue = null;

		String fileName;
		String fileNameFullPath;
		String description;

		try {

			String reference = null; //NESTOR TODO: COMPLETAR PARA GUARDAR
			Integer referenceId = null; //NESTOR TODO: COMPLETAR PARA GUARDAR
			String referenceTypeId = null; //NESTOR TODO: COMPLETAR PARA GUARDAR
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			message.writeTo(out);

			// HZC: 20110128, no subject
			String body = JavaxMailMessageUtilBL.getContentAsString(message);
			description = ("".equals(message.getSubject()) || message.getSubject() == null) 
					? (body.length() > 100) 
							? body.substring(1, 100) + "..."
									: body
									: message.getSubject();

			// HZC: 20110110, permitir numeros en el nombre del fichero
			// rrl 20111230 Agregado tambien ESPACIO BLANCO como caracter valido
			fileName = description.replaceAll("[^a-zA-Z0-9 ]", "_")
					+ Configuration.getConfigurationResourceBundle().getString(
							"mail.smtp.output.ext");

			// Guardar a Repositorio, segun configuracion en el properties
			if (TRUE_STRING.equalsIgnoreCase(Configuration.getConfigurationResourceBundle().getString(
					"store.email.beeblos"))) {

				logger.debug("SendEmailBL.archivaEnBeeblosYEnFileSystem() - store.email.beeblos = TRUE");


				try {

					if (BeeblosBL.statusServidor()) {

						BeeblosBL bl = new BeeblosBL();
						bl.loadProperties();
						Long ret;
						HashMap<String, String> properties = new HashMap<String, String>();

						properties.put(bl.REPOSITORY_PROP_ID_TIPO_OBJETO, referenceTypeId);
						properties.put(bl.REPOSITORY_PROP_ID_OBJETO, (referenceId!=null?referenceId.toString():""));
						properties.put(bl.REPOSITORY_PROP_USUARIO, userLogin);
						properties.put(bl.REPOSITORY_PROP_REFERENCIA, reference);

						ret = bl.addFile(
								Long.valueOf(repositoryId), 
								fileName, message.getContentType(), description,
								Configuration.getConfigurationRepositoryResourceBundle()
										.getString("beeblos.default.docclassid.email"),
								properties, out.toByteArray());

						// rrl 20111130 comprobar que es un docid valido
						if (ret > 0) {
							returnValue = ret.intValue();
						}

						logger.debug("email was saved in repository(): docId created: "
								+ ret);
					} else {
						throw new BeeblosException(
								"No connnection established with Beeblos...");
					}
				} catch (BeeblosBLException e) {
					String mensaje = "BeeblosBLException: Error, saveValidEmailInBeeblosAndInFileSystem(): "
							+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
					logger.error(mensaje);
					throw new BeeblosException(mensaje);
				} catch (NumberFormatException e) {
					String mensaje = "NumberFormatException: Error, saveValidEmailInBeeblosAndInFileSystem(): "
							+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
					throw new BeeblosException(mensaje);
				}
			}

			// Guardamos en fs, segun configuracion en el properties
			if (TRUE_STRING.equals(Configuration.getConfigurationResourceBundle().getString(
					"store.email.fs"))) {
				fileNameFullPath = Configuration.getConfigurationResourceBundle().getString(
						"mail.smtp.output.path")
						+ fileName;
				FileOutputStream fos = new FileOutputStream(fileNameFullPath);
				out.writeTo(fos);
			}
			out.flush();

		} catch (IOException e) {
			String mensaje = "IOException: Error, saveValidEmailInBeeblosAndInFileSystem(): " + e.getMessage();
			e.printStackTrace();
			throw new IOException(mensaje);
		}

		return returnValue;
	}

	/**
	 * Sends a notification to recipient, indicating that a new email has arrived, or forwards 
	 * the new email to the recipient's configured account
	 * 
	 * @param conf
	 * @param emailTrayId
	 * @param currentUserId
	 * 
	 * @throws EmailTrayException
	 */
	private void notifyNewEmailReception(EmailDConf conf, Integer emailTrayId, Integer currentUserId)
			throws EmailTrayException {

		logger.debug("notifyNewEmailReception(): email reception will be notified");
		
		EmailUtilBL euBL = new EmailUtilBL();
		
		try {
			
			if (conf != null
					&& conf.getEmailTemplate() != null
					&& conf.getEmailTemplate().getHtmlTemplate() != null){

				EmailTray emailTray = new EmailTrayBL().getEmailTrayByPK(emailTrayId);
				User recipientUser = this.getRecipientUser(emailTray);

				if (recipientUser != null
						&& recipientUser.getUserSettings() != null){
					
					// send arrival notification
					if (recipientUser.getUserSettings().isNotifyNewEmailReception()){					

						logger.debug("notifyNewEmailReception(): creaeting email personalization");

						
						List<Object> objectList = new ArrayList<Object>();
						objectList.add(recipientUser);
						//NESTOR TODO: AGREGAR MAS OBJETOS A LOS QUE QUEREMOS NOTIFICAR
						
						String subject = EmailPersonalizationUtilBL.personalizeEmailPart(
								conf.getEmailTemplate().getSubject(), objectList);

						String body = EmailPersonalizationUtilBL.personalizeEmailPart(
								conf.getEmailTemplate().getHtmlTemplate(), objectList);

						// NESTOR TODO: si vamos a notificar deberemos tener una cuenta default para ello
						Integer EMAIL_ACCOUNT_SUPPORT_TEAM = null;
						
						Email notificationEmail = euBL
								.createEmailMessage(recipientUser.getEmail(), EMAIL_ACCOUNT_SUPPORT_TEAM, 
										!SAVE_IN_BEEBLOS, !SAVE_IN_EMAIL_TRAY, subject, body);
						
						new SendEmailBL().sendEmail(notificationEmail, null, null, currentUserId);// nes 20131028 agregado docclassid como param
										
						logger.info("notifyNewEmailReception(): email notification was correctly sent...");

					}

				}
			}
		
		} catch (UserException e) {
			e.printStackTrace();
			String mess="notifyNewEmailReception(): ERROR creating notification email: [" + e.getMessage()+" - "+e.getCause() + "]";
			logger.error(mess);
		} catch (SendEmailException e) {
			e.printStackTrace();
			String mess="notifyNewEmailReception(): ERROR creating notification email: [" + e.getMessage()+" - "+e.getCause() + "]";
			logger.error(mess);
		} catch (UserEmailAccountException e) {
			e.printStackTrace();
			String mess="notifyNewEmailReception(): ERROR creating notification email: [" + e.getMessage()+" - "+e.getCause() + "]";
			logger.error(mess);
		} catch (BeeblosBLException e) {
			e.printStackTrace();
			String mess="notifyNewEmailReception(): ERROR creating notification email: [" + e.getMessage()+" - "+e.getCause() + "]";
			logger.error(mess);
		} catch (CreateEmailException e) {
			e.printStackTrace();
			String mess="notifyNewEmailReception(): ERROR creating notification email: [" + e.getMessage()+" - "+e.getCause() + "]";
			logger.error(mess);
		}
		
	}

	private User getRecipientUser(EmailTray emailTray) throws UserException {
		
		User user = null;
		if (emailTray != null
				&& emailTray.getUser() != null
				&& emailTray.getUser().getId() != null
				&& !emailTray.getUser().getId().equals(0)){
			
			user = new UserBL().getUserByPK(emailTray.getUser().getId());
			
		}
		return user;
	}
	
	/**
	 *  Open a "session" control record in email_d_poll table to control
	 *  status of thread polling for this account
	 * @throws DaemonPollException 
	 */
	@Override
	public Integer createProcessControlRecord(DaemonConf conf, Integer currentUserId) {
		
		try {
			
			if (conf instanceof EmailDConf){
				
				EmailDConf emailDConf = (EmailDConf) conf;

				return this.addControlRecord(emailDConf, STARTING_NEW_POLL, currentUserId);
				
			} else {
				//NESTOR TODO: THROW EXCEPTION
			}
			
		} catch (DaemonPollException e) {
			e.printStackTrace();
			String mess="ProyectoActividadDaemonPollerClassImpl.notificarProximaActividad(): ERROR al obtener el proyecto: [" + e.getMessage()+" - "+e.getCause() + "]";
			logger.error(mess);
		}
		
		return null;
		
	}

	private Integer addControlRecord(EmailDConf conf, String status, Integer currentUserId) throws DaemonPollException {
		
		EmailDPoll edp = new EmailDPoll();
		
		edp.setDaemonConfId(conf.getId());
		edp.setBeginDate(new DateTime());
		
		edp.setQtyInputEmail(0);
		edp.setQtyValidEmail(0);
		edp.setQtyInvalidEmail(0);
		edp.setQtyErrorEmail(0);
		
		edp.setComment(status);
		
		edp.setThreadName(Thread.currentThread().getName()); // dml 20120921

		return new DaemonPollBL().add(edp, EmailDPoll.class, currentUserId);
		
	}

	@Override
	public Integer updateControlRecord(Integer daemonPollId, DaemonConf conf, 
			String status, Integer currentUserId) throws DaemonPollerException {

		if (conf instanceof EmailDConf){
			
			EmailDConf emailDConf = (EmailDConf) conf;

			return this._updateControlRecord(daemonPollId, emailDConf, status, currentUserId);
			
		} else {
			throw new DaemonPollerException("[EmailDaemonPollerClassImpl] Error actualizando el registro de poll");
		}
		

	}
	
	private Integer _updateControlRecord(Integer daemonPollId, EmailDConf emailDConf, String status, Integer currentUserId){
		
		EmailDPoll edp = new EmailDPoll();
		DaemonPollBL edpBL = new DaemonPollBL();

		try {
			
			edp = (EmailDPoll) edpBL.getDaemonPollSubObjectByPK(daemonPollId, EmailDPoll.class);
			
			/**
			 * Si el status indica que ya esta finalizado OK entonces no comprobamos porque es que ya se esta parando
			 * y no hace falta saber que dice el usuario...
			 */
			if (status != null && !status.equals(THE_POLL_HAS_FINISHED_OK)){
				Integer killerUserId = this._checkDaemonStoppedByUser(emailDConf, edp);
				
				if (killerUserId != null){
					return killerUserId;
				}
			}
			
			edp.setQtyInputEmail(this.qtyInputEmail);
			edp.setQtyValidEmail(this.qtyValidEmail);
			edp.setQtyInvalidEmail(this.qtyInvalidEmail);
			edp.setQtyErrorEmail(this.qtyErrorEmail);

			edp.setComment(status);			
			
			if (status.equals(THE_POLL_HAS_FINISHED_OK)){
				edp.setEndDate(new DateTime());
			}
			
			edpBL.update(edp, EmailDPoll.class, currentUserId);
			
		} catch (DaemonPollException e) {
			String mess="[DaemonPollException: updateControlRecord] Error trying update control record in email_dpoll tabla control record id:"
					+ daemonPollId + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			System.out.println(mess);
			e.printStackTrace();
		}
		
		return null;

	}
	
	/**
	 * Checks if the daemon was stopped by the user (by the EmailDPoll) and if it is, returns the "killer user id"
	 * 
	 * @author dmuleiro 20140507
	 * 
	 * @param emailDConf
	 * @param edp
	 * @return
	 */
	private Integer _checkDaemonStoppedByUser(EmailDConf emailDConf, EmailDPoll edp){
		
		if (edp != null && edp.getId() != null && !edp.getId().equals(0)){
			
			if (edp.getComment() != null && edp.getComment().equals(STOP_THREAD)){
				logger.debug("[DaemonPoller] The daemon thread with account '" 
						+  emailDConf.getEmailAccount().getName() 
						+ "' is been finished by user '" + edp.getModUser() + "'");
				return edp.getModUser();
				
			}
			
		}
		
		return null;
		
	}

	private void createEmailDLogRegister(
			EmailDConf conf, Integer emailTrayId, Message message, String destinationFolder )
			throws DaemonLogException{
		
		logger.debug("EmailDaemonPoller:createEmailDLogRegister ");

		EmailDLog emailDLogRegister = new EmailDLog();
		
		emailDLogRegister.setDaemonConfId(conf.getId());

		emailDLogRegister.setProcessed(true);
		emailDLogRegister.setProcessedDate(new DateTime());;

		emailDLogRegister.setFrom(emailTrayBL.getFrom(message));

		try {
			emailDLogRegister.setSubject(message.getSubject());
		} catch (MessagingException e) {
			emailDLogRegister.setSubject("error reading javax.mail.message subject");
		}

		try {
			emailDLogRegister
				.setMessageId( 
						emailTrayBL.getMessageId(message.getHeader(MESSAGE_ID)) );
		} catch (MessagingException e) {
			emailDLogRegister.setMessageId("error reading javax.mail.message "+MESSAGE_ID);
		}
		

		emailDLogRegister.setStoreFolder(destinationFolder);
		
		EmailTray et=null;
		try {
			if (emailTrayId!=null) {
				et = emailTrayBL.getEmailTrayByPK(emailTrayId);
			}
		} catch (EmailTrayException e) {
			et=null;
			emailDLogRegister.setMessageId("error reading email_tray while saving email_d_log");
		}
		
		if (et != null){

			emailDLogRegister.setObjectId(et.getObjectId());
			emailDLogRegister.setObjectClass(et.getObjectClass());
//			emailDLogRegister.setUserId(ticket.getAuthorUserId()); // DAVID HAY QUE VER BIEN QUE LLEVA ESTE CAMPO ...
			
		} else {
			emailDLogRegister.setObjectId(null);
			emailDLogRegister.setObjectClass(null);
		}

		new DaemonLogBL().add(emailDLogRegister);

	}

}