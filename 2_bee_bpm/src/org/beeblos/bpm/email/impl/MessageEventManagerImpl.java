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
import org.beeblos.bpm.core.bl.BeeBPMBL;
import org.beeblos.bpm.core.error.AlreadyExistsRunningProcessException;
import org.beeblos.bpm.core.error.InjectorException;
import org.beeblos.bpm.core.error.WProcessDataFieldException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WStepWorkSequenceException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.noper.EmailDConfBeeBPM;
import org.beeblos.bpm.tm.exception.TableManagerException;
import org.joda.time.DateTime;

import com.beeblos.wsapi.model.BeeblosException;
import com.email.core.bl.SendEmailBL;
import com.email.core.error.SendEmailException;
import com.email.core.error.UserEmailAccountException;
import com.email.core.model.Email;
import com.email.tray.core.bl.EmailTrayBL;
import com.email.tray.core.error.CreateEmailException;
import com.email.tray.core.error.EmailTrayException;
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
import com.sp.daemon.email.EmailDLog;
import com.sp.daemon.email.EmailDPoll;
import com.sp.daemon.error.DaemonJobRunningException;
import com.sp.daemon.error.DaemonLogException;
import com.sp.daemon.error.DaemonPollException;
import com.sp.daemon.exe.DaemonExecutor;
import com.sp.daemon.model.DaemonConf;

public class MessageEventManagerImpl implements DaemonExecutor {
	
	private static final Log logger = LogFactory.getLog(MessageEventManagerImpl.class.getName());

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
	
	public MessageEventManagerImpl() {

		logger.debug("PASA POR EL CONSTRUCTOR DE ProyectoActividadDaemonPollerClassImpl");
		_init();

	}

	private void _init() {
	}

	@Override
	public void initializeDaemonExecutorImpl(DaemonConf conf, Integer currentUserId) throws DaemonJobRunningException {

		if (conf instanceof EmailDConfBeeBPM){
			
			try {

				EmailDConfBeeBPM emailDConf = (EmailDConfBeeBPM) conf;

				this.configureSessionAndVariables(emailDConf);
				
			} catch (Exception e) {
				
				String mess = e.getClass().getSimpleName() + " initializing dameon poller for account '" 
						+ conf.getEmailAccount().getEmail() + "'. EmailDConfBeeBPM id:("+conf.getId()+")"
						+" can't create session for email server. "
						+ (e.getMessage()!=null?". "+e.getMessage():"")
						+ (e.getCause()!=null?". "+e.getCause():"");
				logger.error(mess);
				throw new DaemonJobRunningException(mess);
			}
			
		} else {
			String mess = "Error initializing dameon poller: DaemonConf is not an EmailDConfBeeBPM instance";
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		}
		
	}

	/**
	 *  If all is ok returns true. If there is any problem returns false
	 *  
	 * @param conf
	 * @throws DaemonJobRunningException 
	 */
	private void configureSessionAndVariables(EmailDConfBeeBPM conf) throws DaemonJobRunningException {
		
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
		
	}

	/**
	 * Checks work folders validity on email server
	 * 
	 * @param conf
	 */
	private void loadEmailServerWorkFolders(EmailDConfBeeBPM conf) {
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
	 * @throws DaemonJobRunningException
	 */
	private void connectEmailServer(EmailDConfBeeBPM conf)
			throws DaemonJobRunningException {

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
			throw new DaemonJobRunningException(mess);
		} catch (SendEmailException e) {
			String mess="[MessagingException: connectEmailServer] Error trying to connect with the server:"
					+" - " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
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

	private void createEmailServerInstance(String serverType) throws DaemonJobRunningException {
		
		String protocol = this._setProtocol(serverType);
		
		Properties props = new Properties();
		props.put(MAIL + POINT_SYMBOL + protocol + AUTH_PLAIN_DISABLE, TRUE_STRING);
		props.put(MAIL + POINT_SYMBOL + protocol + AUTH_NTLM_DISABLE, TRUE_STRING);

		this.session = Session.getInstance(props, null);
		
	}


	@Override
	public void daemonAction(DaemonConf conf, Integer currentUserId) {

		if (conf instanceof EmailDConfBeeBPM){

			try {
				
				EmailDConfBeeBPM emailDConf = (EmailDConfBeeBPM) conf;

				/**
				 * Initialize email work folders
				 */
				this.openEmailServerWorkFolders();
				
				/**
				 * Check for new email in input folder and process it!
				 */
				this.processEmailInbox(emailDConf, currentUserId);
				
				this.closeEmailServerWorkFolders();

			} catch (Exception e) {
				
				String mess = e.getClass().getSimpleName() + " executing daemon action for account '" 
						+ conf.getEmailAccount().getEmail() + "'. EmailDConfBeeBPM id:("+conf.getId()+")"
						+" can't create session for email server. "
						+ (e.getMessage()!=null?". "+e.getMessage():"")
						+ (e.getCause()!=null?". "+e.getCause():"");
				logger.error(mess);

			}
			
		} else {
			logger.error("Error executing daemon action: DaemonConf is not an EmailDConfBeeBPM instance");
		}
		
	}
	
	private void openEmailServerWorkFolders() throws DaemonJobRunningException {
		
		try {

			this.inboxFolder.open(Folder.READ_WRITE);

		} catch (MessagingException e) {
			String mess = "[MessagingException: openEmailServerWorkFolders] Can't open email server INPUT folder: "
					+ this.inboxFolder.getName() + ". " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		}

		try {
			if (this.validEmailFolder != null) {
				this.validEmailFolder.open(Folder.READ_WRITE);
			}

		} catch (MessagingException e) {
			String mess = "[MessagingException: openEmailServerWorkFolders] Can't open email server VALID EMAILS folder: "
					+ this.validEmailFolder.getName() + ". " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		}

		try {

			if (this.invalidEmailFolder != null) {
				this.invalidEmailFolder.open(Folder.READ_WRITE);
			}

		} catch (MessagingException e) {
			String mess = "[MessagingException: openEmailServerWorkFolders] Can't open email server INVALID EMAILS folder: "
					+ this.invalidEmailFolder.getName() + ". " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		}

		try {

			if (this.errorEmailFolder != null) {
				this.errorEmailFolder.open(Folder.READ_WRITE);
			}

		} catch (MessagingException e) {
			String mess = "[MessagingException: openEmailServerWorkFolders] Can't open email server ERROR EMAILS folder: "
					+ this.errorEmailFolder.getName() + ". " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		}


	}

	private void closeEmailServerWorkFolders() throws DaemonJobRunningException {
		
		try {

			this.inboxFolder.close(true);

		} catch (MessagingException e) {
			String mess = "[MessagingException: closeEmailServerWorkFolders] Can't close email server INPUT folder: "
					+ this.inboxFolder.getName() + ". " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		}

		try {
			if (this.validEmailFolder != null) {
				this.validEmailFolder.close(true);
			}
			
		} catch (MessagingException e) {
			String mess = "[MessagingException: closeEmailServerWorkFolders] Can't close email server VALID EMAILS folder: "
					+ this.validEmailFolder.getName() + ". " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		}
		
		try {

			if ( invalidEmailFolder != null) {
				this.invalidEmailFolder.close(true);			
			}
			
		} catch (MessagingException e) {
			String mess = "[MessagingException: closeEmailServerWorkFolders] Can't close email server INVALID EMAILS folder: "
					+ this.invalidEmailFolder.getName() + ". " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		}		

		try {

			if (this.errorEmailFolder != null) {
				this.errorEmailFolder.close(true);			
			}
			
		} catch (MessagingException e) {
			String mess = "[MessagingException: closeEmailServerWorkFolders] Can't close email server ERROR folder: "
					+ this.errorEmailFolder.getName() + ". " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		}		

		
	}	

	private void processEmailInbox(EmailDConfBeeBPM conf, Integer currentUserId) throws DaemonJobRunningException {
		
		/**
		 * Obtain inbox message list
		 */
		Message[] messages;
		try {
			
			messages = this.inboxFolder.getMessages();
			
		} catch (MessagingException e) {
			String mess = "[MessagingException: processEmailInbox] Can't obtain messages from INBOX folder: "
					+ this.invalidEmailFolder.getName() + ". " + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
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

				String mess = "[MessagingException: processEmailInbox] Error processing message - It will be move to revision folder: "
						+ this.errorEmailFolder.getName() + ". " + e.getMessage() 
						+ (e.getCause()!=null?". "+e.getCause():"") + (e.getClass()!=null?". "+e.getClass():"");
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

	private void processInvalidEmail(EmailDConfBeeBPM conf, Message message, Integer currentUserId) {
		
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
			String mess = "[MessagingException: processEmailInbox] Error processing message - It will be move to revision folder: "
					+ this.errorEmailFolder.getName() + ". MessageID: " + messageId
					+ ". " + e.getMessage() 
					+ (e.getCause()!=null?". "+e.getCause():"") + (e.getClass()!=null?". "+e.getClass():"");
			logger.error(mess);
			
			qtyErrorEmail++;

			this.processErrorEmail(conf, message); // dml 20121009

		}
	}

	private void processErrorEmail(EmailDConfBeeBPM conf, Message message) {
		
		try {

			// update message status in email server
			message.setFlag(Flag.SEEN, true);

			inboxFolder.copyMessages(
					new Message[] { message }, errorEmailFolder);

			message.setFlag(Flag.DELETED, true);


		} catch (MessagingException e) {
			String mess = "[MessagingException: processEmailInbox] Error processing message - It will be move to revision folder: "
					+ this.errorEmailFolder.getName() + ". " + e.getMessage() 
					+ (e.getCause()!=null?". "+e.getCause():"") + (e.getClass()!=null?". "+e.getClass():"");
			logger.error(mess);
		}
	}

	private void processValidEmail(EmailDConfBeeBPM conf, Message message, Integer currentUserId) throws DaemonJobRunningException  {
		
		try {
			
			message.setFlag(Flag.SEEN, true);
			
			inboxFolder.copyMessages(
					new Message[] { message }, validEmailFolder);
	
			message.setFlag(Flag.DELETED, true);
	
			Integer emailTrayId = null;
			
			/**
			 *  dml 20121025 - if it is an INPUT account we manage the income email
			 */
			if (conf.getType().equals(INPUT)){
				
				/**
				 * Creates the email tray to have this email message logged
				 */
				emailTrayId = this._createEmailTray(conf, message, currentUserId);
	
				this._injectEmailTrayAsProcess(conf, emailTrayId, currentUserId);
				
				/**
				 * Notifies the email reception if it is indicated
				 */
				this.notifyNewEmailReception(conf, emailTrayId, currentUserId);
				
			/**
			 *  If it is an output account...
			 */
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

		} catch (MessagingException e) {
			String mess = "[MessagingException: processValidEmail] processing valid email: "
					+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		} catch (DaemonJobRunningException e){
			throw e;
		}
		
		
	}
	
	/**
	 * Creates the email tray in order to have the email message logged
	 * 
	 * @author dmuleiro 20150423
	 * 
	 * @param conf
	 * @param message
	 * @param currentUserId
	 * @return
	 * @throws DaemonJobRunningException 
	 */
	private Integer _createEmailTray(EmailDConfBeeBPM conf, Message message, 
			Integer currentUserId) throws DaemonJobRunningException{
		
		String className = WProcessDef.class.getName();
		Integer objectId = conf.getIdProcessDef();

		/**
		 * NESTOR TODO: ESTO SE USARÍA SI POR EJEMPLO VAMOS A GUARDAR UNA TRAZA DE EMAILS DEL MISMO TEMA,
		 * CON ESTO GUARDARIAMOS EL CAMPO DE EMAIL_TRAY "original_subject" CORRECTAMENTE, ELIMINANDO
		 * LOS "Fwd:", "Re:", etc
		 */
		String VALID_SUBJECT_STRING_IDENTIFIER = null;

		/**
		 *  Inserts email message into email_tray table
		 */
		try {
			
			return emailTrayBL.createInputEmailTrayRegister(
						message, objectId, className, currentUserId, 
						JAVA_IO_TEMPDIR, BEEBPM_TMP_FOLDER, VALID_SUBJECT_STRING_IDENTIFIER);
			
		} catch (EmailTrayException e) {
			String mess = "[EmailTrayException: _createEmailTray] creating email tray: "
					+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess, e);
		} 

	}
	
	/**
	 * Injects the created EmailTray into the process workflow. This is the main action of the algorithm.
	 * 
	 * @author dmuleiro 20150423
	 * 
	 * @throws DaemonJobRunningException 
	 */
	private void _injectEmailTrayAsProcess(EmailDConfBeeBPM conf, Integer emailTrayId, Integer currentUserId) throws DaemonJobRunningException{
		
		if (emailTrayId != null && !emailTrayId.equals(0)){
			
			EmailTray et = null;
			try {
				et = new EmailTrayBL().getEmailTrayByPK(emailTrayId);
			} catch (EmailTrayException e) {
				String mess = "[EmailTrayException: _injectEmailTrayProcess] getting email tray: "
						+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
				logger.error(mess);
				throw new DaemonJobRunningException(mess, e);
			}

			if (et != null && et.getId() != null && !et.getId().equals(0)){
				
				String objComments = "Process injected by MessageBegin event with id '" + conf.getIdStepDef()
						+ "' and MessageId= " + et.getMessageId();
				
				try {
					
					Integer idStepWork = new BeeBPMBL().injector(
							conf.getIdProcessDef(), //idProcessDef
							conf.getIdStepDef(), //idStepDef
							et.getId(), //idObject
							et.getClass().getName(), //idObjectType
							et.getSubject(), // objReference
							objComments, // Comentarios del objeto?
							currentUserId);
					
				} catch (InjectorException e) {
					String mess = "[InjectorException: _injectEmailTrayProcess] injecting email tray: "
							+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
					logger.error(mess);
					throw new DaemonJobRunningException(mess, e);
				} catch (AlreadyExistsRunningProcessException e) {
					String mess = "[AlreadyExistsRunningProcessException: _injectEmailTrayProcess] injecting email tray: "
							+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
					logger.error(mess);
					throw new DaemonJobRunningException(mess, e);
				} catch (WStepWorkException e) {
					String mess = "[WStepWorkException: _injectEmailTrayProcess] injecting email tray: "
							+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
					logger.error(mess);
					throw new DaemonJobRunningException(mess, e);
				} catch (WProcessWorkException e) {
					String mess = "[WProcessWorkException: _injectEmailTrayProcess] injecting email tray: "
							+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
					logger.error(mess);
					throw new DaemonJobRunningException(mess, e);
				} catch (TableManagerException e) {
					String mess = "[TableManagerException: _injectEmailTrayProcess] injecting email tray: "
							+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
					logger.error(mess);
					throw new DaemonJobRunningException(mess, e);
				} catch (WStepWorkSequenceException e) {
					String mess = "[WStepWorkSequenceException: _injectEmailTrayProcess] injecting email tray: "
							+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
					logger.error(mess);
					throw new DaemonJobRunningException(mess, e);
				} catch (WProcessDataFieldException e) {
					String mess = "[WProcessDataFieldException: _injectEmailTrayProcess] injecting email tray: "
							+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
					logger.error(mess);
					throw new DaemonJobRunningException(mess, e);
				}
				
			}
		}
		
	}

	private void writeLogAndBeeblosBackup(EmailDConfBeeBPM conf, Message message,
			Integer emailTrayId) throws DaemonJobRunningException {
		try {
			
			
			this.createEmailDLogRegister(conf, emailTrayId, message, conf.getValidEmailFolder());
			
		} catch (DaemonLogException e) {
			String mess = "[BeeblosException: writeLogAndBeeblosBackup] creating daemon log: "
					+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.warn(mess);
		}
		
		this.saveValidEmailInBeeblosAndInFileSystem(message, emailTrayId,
				BEEBLOS_DEFAULT_REPOSITORY_ID, conf.getEmailAccount().getName());

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
	 * @throws DaemonJobRunningException 
	 * @throws MessagingException
	 * @throws BeeblosException
	 * @throws IOException
	 */
	private Integer saveValidEmailInBeeblosAndInFileSystem(Message message, 
			Integer emailTrayId, Integer repositoryId , String userLogin) throws DaemonJobRunningException {

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
						throw new DaemonJobRunningException(
								"[BeeblosBLException: saveValidEmailInBeeblosAndInFileSystem] No connnection established with Beeblos...");
					}
				} catch (BeeblosBLException e) {
					String mess = "[BeeblosBLException: saveValidEmailInBeeblosAndInFileSystem] saving email in beeblos: "
							+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
					logger.error(mess);
					throw new DaemonJobRunningException(mess);
				} catch (NumberFormatException e) {
					String mess = "[NumberFormatException: saveValidEmailInBeeblosAndInFileSystem] saving email in beeblos: "
							+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
					logger.error(mess);
					throw new DaemonJobRunningException(mess);
				} catch (MessagingException e) {
					String mess = "[MessagingException: saveValidEmailInBeeblosAndInFileSystem] saving email in beeblos: "
							+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
					logger.error(mess);
					throw new DaemonJobRunningException(mess);
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
			String mess = "[IOException: saveValidEmailInBeeblosAndInFileSystem] saving email in beeblos: "
					+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		} catch (MessagingException e) {
			String mess = "[MessagingException: saveValidEmailInBeeblosAndInFileSystem] saving email in beeblos: "
					+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
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
	 * @throws DaemonJobRunningException 
	 */
	private void notifyNewEmailReception(EmailDConfBeeBPM conf, Integer emailTrayId, Integer currentUserId)
			throws DaemonJobRunningException {

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
			String mess = "[MessagingException: notifyNewEmailReception] creating notification: "
					+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		} catch (SendEmailException e) {
			String mess = "[SendEmailException: notifyNewEmailReception] creating notification: "
					+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		} catch (UserEmailAccountException e) {
			String mess = "[UserEmailAccountException: notifyNewEmailReception] creating notification: "
					+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		} catch (BeeblosBLException e) {
			String mess = "[BeeblosBLException: notifyNewEmailReception] creating notification: "
					+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		} catch (CreateEmailException e) {
			String mess = "[CreateEmailException: notifyNewEmailReception] creating notification: "
					+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		} catch (EmailTrayException e) {
			String mess = "[EmailTrayException: notifyNewEmailReception] creating notification: "
					+ e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
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
	 * @throws DaemonJobRunningException 
	 * @throws DaemonPollException 
	 */
	@Override
	public Integer createProcessControlRecord(DaemonConf conf, String controllerName, Integer currentUserId) throws DaemonJobRunningException {
		
		if (conf instanceof EmailDConfBeeBPM){
			
			try {
				
				EmailDConfBeeBPM emailDConf = (EmailDConfBeeBPM) conf;

				return this.addControlRecord(emailDConf, controllerName, STARTING_NEW_POLL, currentUserId);
				
			} catch (Exception e) {
				
				String mess = e.getClass().getSimpleName() + " creating process control record for account '" 
						+ conf.getEmailAccount().getEmail() + "'. EmailDConfBeeBPM id:("+conf.getId()+")"
						+" can't create session for email server. "
						+ (e.getMessage()!=null?". "+e.getMessage():"")
						+ (e.getCause()!=null?". "+e.getCause():"");
				logger.error(mess);
				throw new DaemonJobRunningException(mess);
			}
			
		} else {
			String mess = "Error creating process control record: DaemonConf is not an EmailDConfBeeBPM instance";
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		}
		
	}

	private Integer addControlRecord(EmailDConfBeeBPM conf, String controllerName, String status, Integer currentUserId) throws DaemonPollException {
		
		EmailDPoll edp = new EmailDPoll();
		
		edp.setDaemonConfId(conf.getId());
		edp.setBeginDate(new DateTime());
		
		edp.setQtyInputEmail(0);
		edp.setQtyValidEmail(0);
		edp.setQtyInvalidEmail(0);
		edp.setQtyErrorEmail(0);
		
		edp.setComment(status);
		
		edp.setThreadName(controllerName!=null?
				controllerName:Thread.currentThread().getName()); // dml 20120921

		return new DaemonPollBL().add(edp, EmailDPoll.class, currentUserId);
		
	}

	@Override
	public Integer updateControlRecord(Integer daemonPollId, DaemonConf conf, 
			String status, Integer currentUserId) {

		if (conf instanceof EmailDConfBeeBPM){
			
			try {
				
				EmailDConfBeeBPM emailDConf = (EmailDConfBeeBPM) conf;

				return this._updateControlRecord(daemonPollId, emailDConf, status, currentUserId);
				
			} catch (Exception e) {
				
				String mess = e.getClass().getSimpleName() + " updating control record for account '" 
						+ conf.getEmailAccount().getEmail() + "'. EmailDConfBeeBPM id:("+conf.getId()+")"
						+" can't create session for email server. "
						+ (e.getMessage()!=null?". "+e.getMessage():"")
						+ (e.getCause()!=null?". "+e.getCause():"");
				logger.error(mess);

			}

		} else {
			logger.error("Error updating control record: DaemonConf is not an EmailDConfBeeBPM instance");
		}
		
		return null;

	}
	
	private Integer _updateControlRecord(Integer daemonPollId, EmailDConfBeeBPM emailDConf, String status, Integer currentUserId) throws DaemonJobRunningException{
		
		EmailDPoll edp = new EmailDPoll();
		DaemonPollBL edpBL = new DaemonPollBL();

		try {
			
			edp = (EmailDPoll) edpBL.getDaemonPollSubObjectByPK(daemonPollId, EmailDPoll.class);
			
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
			String mess = "[DaemonPollException: _updateControlRecord] trying update control record in email_dpoll tabla control record id: "
					+ daemonPollId + e.getMessage() + (e.getCause()!=null?". "+e.getCause():"");
			logger.error(mess);
			throw new DaemonJobRunningException(mess);
		}
		
		return null;

	}
	
	private void createEmailDLogRegister(
			EmailDConfBeeBPM conf, Integer emailTrayId, Message message, String destinationFolder )
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
			emailDLogRegister.setSubject("Error reading javax.mail.message subject");
		}

		try {
			emailDLogRegister
				.setMessageId( 
						emailTrayBL.getMessageId(message.getHeader(MESSAGE_ID)) );
		} catch (MessagingException e) {
			emailDLogRegister.setMessageId("Error reading javax.mail.message "+MESSAGE_ID);
		}
		

		emailDLogRegister.setStoreFolder(destinationFolder);
		
		EmailTray et=null;
		try {
			if (emailTrayId!=null) {
				et = emailTrayBL.getEmailTrayByPK(emailTrayId);
			}
		} catch (EmailTrayException e) {
			et=null;
			emailDLogRegister.setMessageId("Error reading email_tray while saving email_d_log");
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