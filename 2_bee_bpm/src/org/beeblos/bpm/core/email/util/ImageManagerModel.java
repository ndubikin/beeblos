package org.beeblos.bpm.core.email.util;


public class ImageManagerModel {
	
	private String id;
	private String fileName; // absolute path and file name
	
	//rrl 20110503 embed the image data inside the HTML <img src="data:image/png;base64,iVBORw0KGgoAAAANSUh...
	private String mimeType;
	private String charsetEncoding;
	private String codification;
	private String imagen; 
	
	public ImageManagerModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ImageManagerModel(String idImage, String fileName, String mimeType, String charsetEncoding, String codification, String imagen) {
		this.id = idImage;
		this.fileName = fileName;
		this.mimeType = mimeType;
		this.charsetEncoding = charsetEncoding;
		this.codification = codification;
		this.imagen = imagen; 
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getCharsetEncoding() {
		return charsetEncoding;
	}
	public void setCharsetEncoding(String charsetEncoding) {
		this.charsetEncoding = charsetEncoding;
	}
	public String getCodification() {
		return codification;
	}
	public void setCodification(String codification) {
		this.codification = codification;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	@Override
	public String toString() {
		return "ImageManagerModel ["
				+ (id != null ? "idImage=" + id + ", " : "")
				+ (fileName != null ? "fileName=" + fileName + ", " : "")
				+ (mimeType != null ? "mimeType=" + mimeType + ", " : "")
				+ (charsetEncoding != null ? "charsetEncoding=" + charsetEncoding + ", " : "")
				+ (codification != null ? "codification=" + codification + ", " : "")
				+ (imagen != null ? "imagen=" + imagen : "") + "]";
	}

}
