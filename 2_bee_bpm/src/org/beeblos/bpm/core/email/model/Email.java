package org.beeblos.bpm.core.email.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.sp.common.model.FileSP;

public class Email implements Serializable{
	
	private static final long serialVersionUID = 7952503078448718936L;
	
	private String idTipoReferencia;
	private Integer idReferencia;
	private String referencia;
	private Integer idFrom;//idUce
	private String from;
	private String pwd;
	private String to;
	private ArrayList<String> listaTo = new ArrayList<String>();
	private String cc;
	private ArrayList<String> listaCC = new ArrayList<String>();
	private String subject;
	private String bodyText;
	private ArrayList<FileSP> files = new ArrayList<FileSP>();
	
	private String idEnvio;		//HZC:20110207
	private Date fechaEnvio;	//HZC:20110208
	
	private boolean htmlFormatted;	//HZC:20110301
	private String bodyHtml;		//HZC:20110301
	
	private String contextPath; // nes 20110429 - path del server si está trabajando dentro de 1 contexto web.
	
	private boolean guardarEnBeeblos; // nes 20110429 - indica si se guarda o no en Beeblos el email enviado ...
	
	private String replyTo;

	private boolean usarFirmaEmail; // rrl 20110722 - indica si usa y adjunta la firma al email


	
	public String getIdTipoReferencia() {
		return idTipoReferencia;
	}
	public void setIdTipoReferencia(String idTipoReferencia) {
		this.idTipoReferencia = idTipoReferencia;
	}
	public Integer getIdReferencia() {
		return idReferencia;
	}
	public void setIdReferencia(Integer idReferencia) {
		this.idReferencia = idReferencia;
	}

	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public Integer getIdFrom() {
		return idFrom;
	}
	public void setIdFrom(Integer idFrom) {
		this.idFrom = idFrom;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public ArrayList<String> getListaTo() {
		return listaTo;
	}
	public void setListaTo(ArrayList<String> listaTo) {
		this.listaTo = listaTo;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public ArrayList<String> getListaCC() {
		return listaCC;
	}
	public void setListaCC(ArrayList<String> listaCC) {
		this.listaCC = listaCC;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBodyText() {
		return bodyText;
	}
	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}
	public ArrayList<FileSP> getFiles() {
		return files;
	}
	public void setFiles(ArrayList<FileSP> files) {
		this.files = files;
	}
	public String getIdEnvio() {
		return idEnvio;
	}
	public void setIdEnvio(String idEnvio) {
		this.idEnvio = idEnvio;
	}
	public Date getFechaEnvio() {
		return fechaEnvio;
	}
	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}
	public boolean isHtmlFormatted() {
		return htmlFormatted;
	}
	public void setHtmlFormatted(boolean htmlFormatted) {
		this.htmlFormatted = htmlFormatted;
	}
	public String getBodyHtml() {
		return bodyHtml;
	}
	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}
	
	
	
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	@Override
	public String toString() {
		return "Email [bodyText=" + bodyText + ", cc=" + cc + ", files="
				+ files + ", from=" + from + ", idFrom=" + idFrom
				+ ", idReferencia=" + idReferencia + ", subject=" + subject
				+ ", to=" + to + "]";
	}
	public boolean isGuardarEnBeeblos() {
		return guardarEnBeeblos;
	}
	public void setGuardarEnBeeblos(boolean guardarEnBeeblos) {
		this.guardarEnBeeblos = guardarEnBeeblos;
	}
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	public String getReplyTo() {
		return replyTo;
	}
	
	//rrl 20110722
	public boolean isUsarFirmaEmail() {
		return usarFirmaEmail;
	}
	public void setUsarFirmaEmail(boolean usarFirmaEmail) {
		this.usarFirmaEmail = usarFirmaEmail;
	}


}
