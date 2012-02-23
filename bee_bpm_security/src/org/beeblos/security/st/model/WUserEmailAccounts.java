package org.beeblos.security.st.model;

import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;

import org.beeblos.bpm.core.model.WTimeUnit;
import org.beeblos.bpm.core.model.WUserDef;

public class WUserEmailAccounts implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id = 0;
	private WUserDef wUserDef;
	private String name;
	private boolean userDefaultAccount;
	private String email;
	private String replyTo;
	private String signatureText;
	private String signatureTxt;
	private String signatureHtml;
	private String signatureFile;
	private String inputServerType;
	private String inputServerName;
	private Integer inputPort;
	private String connectionSecurity;
	private String identificationMethod;
	private String format;
	private String outputServerName;
	private String outputServer;
	private Integer outputPort;
	private String outputConnectionSecurity;
	private String outputIdentificationMethod;
	private String outputUserName;
	private String outputPassword;
	private String idExchange;
 
	public WUserEmailAccounts() {

	}

	public WUserEmailAccounts(boolean createEmtpyObjects ){
		super();
		if ( createEmtpyObjects ) {
			this.wUserDef = new WUserDef();
			
		}	
	}

	
	
	public WUserEmailAccounts(Integer id, WUserDef wUserDef, String name,
			boolean userDefaultAccount, String email, String replyTo,
			String signatureText, String signatureTxt, String signatureHtml,
			String signatureFile, String inputServerType,
			String inputServerName, Integer inputPort,
			String connectionSecurity, String identificationMethod,
			String format, String outputServerName, String outputServer,
			Integer outputPort, String outputConnectionSecurity,
			String outputIdentificationMethod, String outputUserName,
			String outputPassword, String idExchange) {
		super();
		this.id = id;
		this.wUserDef = wUserDef;
		this.name = name;
		this.userDefaultAccount = userDefaultAccount;
		this.email = email;
		this.replyTo = replyTo;
		this.signatureText = signatureText;
		this.signatureTxt = signatureTxt;
		this.signatureHtml = signatureHtml;
		this.signatureFile = signatureFile;
		this.inputServerType = inputServerType;
		this.inputServerName = inputServerName;
		this.inputPort = inputPort;
		this.connectionSecurity = connectionSecurity;
		this.identificationMethod = identificationMethod;
		this.format = format;
		this.outputServerName = outputServerName;
		this.outputServer = outputServer;
		this.outputPort = outputPort;
		this.outputConnectionSecurity = outputConnectionSecurity;
		this.outputIdentificationMethod = outputIdentificationMethod;
		this.outputUserName = outputUserName;
		this.outputPassword = outputPassword;
		this.idExchange = idExchange;
	}

	

	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public WUserDef getwUserDef() {
		return wUserDef;
	}



	public void setwUserDef(WUserDef wUserDef) {
		this.wUserDef = wUserDef;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public boolean isUserDefaultAccount() {
		return userDefaultAccount;
	}



	public void setUserDefaultAccount(boolean userDefaultAccount) {
		this.userDefaultAccount = userDefaultAccount;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getReplyTo() {
		return replyTo;
	}



	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}



	public String getSignatureText() {
		return signatureText;
	}



	public void setSignatureText(String signatureText) {
		this.signatureText = signatureText;
	}



	public String getSignatureTxt() {
		return signatureTxt;
	}



	public void setSignatureTxt(String signatureTxt) {
		this.signatureTxt = signatureTxt;
	}



	public String getSignatureHtml() {
		return signatureHtml;
	}



	public void setSignatureHtml(String signatureHtml) {
		this.signatureHtml = signatureHtml;
	}



	public String getSignatureFile() {
		return signatureFile;
	}



	public void setSignatureFile(String signatureFile) {
		this.signatureFile = signatureFile;
	}



	public String getInputServerType() {
		return inputServerType;
	}



	public void setInputServerType(String inputServerType) {
		this.inputServerType = inputServerType;
	}



	public String getInputServerName() {
		return inputServerName;
	}



	public void setInputServerName(String inputServerName) {
		this.inputServerName = inputServerName;
	}



	public Integer getInputPort() {
		return inputPort;
	}



	public void setInputPort(Integer inputPort) {
		this.inputPort = inputPort;
	}



	public String getConnectionSecurity() {
		return connectionSecurity;
	}



	public void setConnectionSecurity(String connectionSecurity) {
		this.connectionSecurity = connectionSecurity;
	}



	public String getIdentificationMethod() {
		return identificationMethod;
	}



	public void setIdentificationMethod(String identificationMethod) {
		this.identificationMethod = identificationMethod;
	}



	public String getFormat() {
		return format;
	}



	public void setFormat(String format) {
		this.format = format;
	}



	public String getOutputServerName() {
		return outputServerName;
	}



	public void setOutputServerName(String outputServerName) {
		this.outputServerName = outputServerName;
	}



	public String getOutputServer() {
		return outputServer;
	}



	public void setOutputServer(String outputServer) {
		this.outputServer = outputServer;
	}



	public Integer getOutputPort() {
		return outputPort;
	}



	public void setOutputPort(Integer outputPort) {
		this.outputPort = outputPort;
	}



	public String getOutputConnectionSecurity() {
		return outputConnectionSecurity;
	}



	public void setOutputConnectionSecurity(String outputConnectionSecurity) {
		this.outputConnectionSecurity = outputConnectionSecurity;
	}



	public String getOutputIdentificationMethod() {
		return outputIdentificationMethod;
	}



	public void setOutputIdentificationMethod(String outputIdentificationMethod) {
		this.outputIdentificationMethod = outputIdentificationMethod;
	}



	public String getOutputUserName() {
		return outputUserName;
	}



	public void setOutputUserName(String outputUserName) {
		this.outputUserName = outputUserName;
	}



	public String getOutputPassword() {
		return outputPassword;
	}



	public void setOutputPassword(String outputPassword) {
		this.outputPassword = outputPassword;
	}



	public String getIdExchange() {
		return idExchange;
	}



	public void setIdExchange(String idExchange) {
		this.idExchange = idExchange;
	}

	

	@Override
	public String toString() {
		return "WUserEmailAccounts [id=" + id + ", wUserDef=" + wUserDef
				+ ", name=" + name + ", userDefaultAccount="
				+ userDefaultAccount + ", email=" + email + ", replyTo="
				+ replyTo + ", signatureText=" + signatureText
				+ ", signatureTxt=" + signatureTxt + ", signatureHtml="
				+ signatureHtml + ", signatureFile=" + signatureFile
				+ ", inputServerType=" + inputServerType + ", inputServerName="
				+ inputServerName + ", inputPort=" + inputPort
				+ ", connectionSecurity=" + connectionSecurity
				+ ", identificationMethod=" + identificationMethod
				+ ", format=" + format + ", outputServerName="
				+ outputServerName + ", outputServer=" + outputServer
				+ ", outputPort=" + outputPort + ", outputConnectionSecurity="
				+ outputConnectionSecurity + ", outputIdentificationMethod="
				+ outputIdentificationMethod + ", outputUserName="
				+ outputUserName + ", outputPassword=" + outputPassword
				+ ", idExchange=" + idExchange + "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((connectionSecurity == null) ? 0 : connectionSecurity
						.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((idExchange == null) ? 0 : idExchange.hashCode());
		result = prime * result + ((wUserDef == null) ? 0 : wUserDef.hashCode());
		result = prime
				* result
				+ ((identificationMethod == null) ? 0 : identificationMethod
						.hashCode());
		result = prime * result
				+ ((inputPort == null) ? 0 : inputPort.hashCode());
		result = prime * result
				+ ((inputServerName == null) ? 0 : inputServerName.hashCode());
		result = prime * result
				+ ((inputServerType == null) ? 0 : inputServerType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((outputConnectionSecurity == null) ? 0
						: outputConnectionSecurity.hashCode());
		result = prime
				* result
				+ ((outputIdentificationMethod == null) ? 0
						: outputIdentificationMethod.hashCode());
		result = prime * result
				+ ((outputPassword == null) ? 0 : outputPassword.hashCode());
		result = prime * result
				+ ((outputPort == null) ? 0 : outputPort.hashCode());
		result = prime * result
				+ ((outputServer == null) ? 0 : outputServer.hashCode());
		result = prime
				* result
				+ ((outputServerName == null) ? 0 : outputServerName.hashCode());
		result = prime * result
				+ ((outputUserName == null) ? 0 : outputUserName.hashCode());
		result = prime * result + ((replyTo == null) ? 0 : replyTo.hashCode());
		result = prime * result
				+ ((signatureFile == null) ? 0 : signatureFile.hashCode());
		result = prime * result
				+ ((signatureHtml == null) ? 0 : signatureHtml.hashCode());
		result = prime * result
				+ ((signatureTxt == null) ? 0 : signatureTxt.hashCode());
		result = prime * result
				+ ((signatureText == null) ? 0 : signatureText.hashCode());
		result = prime * result + (userDefaultAccount ? 1231 : 1237);
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WUserEmailAccounts other = (WUserEmailAccounts) obj;
		if (connectionSecurity == null) {
			if (other.connectionSecurity != null)
				return false;
		} else if (!connectionSecurity.equals(other.connectionSecurity))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (format == null) {
			if (other.format != null)
				return false;
		} else if (!format.equals(other.format))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idExchange == null) {
			if (other.idExchange != null)
				return false;
		} else if (!idExchange.equals(other.idExchange))
			return false;
		if (wUserDef == null) {
			if (other.wUserDef != null)
				return false;
		} else if (!wUserDef.equals(other.wUserDef))
			return false;
		if (identificationMethod == null) {
			if (other.identificationMethod != null)
				return false;
		} else if (!identificationMethod.equals(other.identificationMethod))
			return false;
		if (inputPort == null) {
			if (other.inputPort != null)
				return false;
		} else if (!inputPort.equals(other.inputPort))
			return false;
		if (inputServerName == null) {
			if (other.inputServerName != null)
				return false;
		} else if (!inputServerName.equals(other.inputServerName))
			return false;
		if (inputServerType == null) {
			if (other.inputServerType != null)
				return false;
		} else if (!inputServerType.equals(other.inputServerType))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (outputConnectionSecurity == null) {
			if (other.outputConnectionSecurity != null)
				return false;
		} else if (!outputConnectionSecurity
				.equals(other.outputConnectionSecurity))
			return false;
		if (outputIdentificationMethod == null) {
			if (other.outputIdentificationMethod != null)
				return false;
		} else if (!outputIdentificationMethod
				.equals(other.outputIdentificationMethod))
			return false;
		if (outputPassword == null) {
			if (other.outputPassword != null)
				return false;
		} else if (!outputPassword.equals(other.outputPassword))
			return false;
		if (outputPort == null) {
			if (other.outputPort != null)
				return false;
		} else if (!outputPort.equals(other.outputPort))
			return false;
		if (outputServer == null) {
			if (other.outputServer != null)
				return false;
		} else if (!outputServer.equals(other.outputServer))
			return false;
		if (outputServerName == null) {
			if (other.outputServerName != null)
				return false;
		} else if (!outputServerName.equals(other.outputServerName))
			return false;
		if (outputUserName == null) {
			if (other.outputUserName != null)
				return false;
		} else if (!outputUserName.equals(other.outputUserName))
			return false;
		if (replyTo == null) {
			if (other.replyTo != null)
				return false;
		} else if (!replyTo.equals(other.replyTo))
			return false;
		if (signatureFile == null) {
			if (other.signatureFile != null)
				return false;
		} else if (!signatureFile.equals(other.signatureFile))
			return false;
		if (signatureHtml == null) {
			if (other.signatureHtml != null)
				return false;
		} else if (!signatureHtml.equals(other.signatureHtml))
			return false;
		if (signatureTxt == null) {
			if (other.signatureTxt != null)
				return false;
		} else if (!signatureTxt.equals(other.signatureTxt))
			return false;
		if (signatureText == null) {
			if (other.signatureText != null)
				return false;
		} else if (!signatureText.equals(other.signatureText))
			return false;
		if (userDefaultAccount != other.userDefaultAccount)
			return false;
		return true;
	}



	public boolean empty(){
				
		if (id != null && !id.equals(0)) return false;
		if (!wUserDef.empty()) return false;
		if (name != null && !name.equals("")) return false;
		if (email != null && !email.equals("")) return false;
		if (replyTo != null && !replyTo.equals("")) return false;
		if (signatureText != null && !signatureText.equals("")) return false;
		if (signatureTxt != null && !signatureTxt.equals("")) return false;
		if (signatureHtml != null && !signatureHtml.equals("")) return false;
		if (signatureFile != null && !signatureFile.equals("")) return false;
		if (inputServerType != null && !inputServerType.equals("")) return false;
		if (inputServerName != null && !inputServerName.equals("")) return false;
		if (inputPort != null && !inputPort.equals(0)) return false;
		if (connectionSecurity != null && !connectionSecurity.equals("")) return false;
		if (identificationMethod != null && !identificationMethod.equals("")) return false;
		if (format != null && !format.equals("")) return false;
		if (outputServerName != null && !outputServerName.equals("")) return false;
		if (outputServer != null && !outputServer.equals("")) return false;
		if (outputPort != null && !outputPort.equals(0)) return false;
		if (outputConnectionSecurity != null && !outputConnectionSecurity.equals("")) return false;
		if (outputIdentificationMethod != null && !outputIdentificationMethod.equals("")) return false;
		if (outputUserName != null && !outputUserName.equals("")) return false;
		if (outputPassword != null && !outputPassword.equals("")) return false;
		if (idExchange != null && !idExchange.equals("")) return false;
		
		return false;
		
	}
	
}