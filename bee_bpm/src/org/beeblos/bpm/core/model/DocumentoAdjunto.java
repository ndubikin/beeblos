package org.beeblos.bpm.core.model;


import java.io.Serializable;
import java.util.Date;

/**
 * @author nes
 *
 */
public class DocumentoAdjunto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// HZC 20101224: se añadió propiedades definidas basicas (repo)
	private String id_tipo_objeto;
	private String id_objeto;
	private String referencia;
	private String id_usuario;
	
	private Integer docId;
	private String documentoNombre;
	private Integer docclassId;
	private String docclassNombre;
	private String documentoDescripcion; // description to list content of document class
	private String mimeType;
	private String documentoVersionable;
	
	//mrico - 20110524 - Añado fecha de creacion y modificacion
	private Date documentoFechaCreacion;
	private Date documentoFechaMod;
	
	private boolean seleccionMultiple = false;   //rrl 20110729
	
	
	public DocumentoAdjunto() {
		super();

	}


	public DocumentoAdjunto(Integer docId, String documentoNombre,
			Integer docclassId, String docclassNombre,
			String documentoDescripcion, String mimeType,
			String documentoVersionable) {
		super();
		this.docId = docId;
		this.documentoNombre = documentoNombre;
		this.docclassId = docclassId;
		this.docclassNombre = docclassNombre;
		this.documentoDescripcion = documentoDescripcion;
		this.mimeType = mimeType;
		this.documentoVersionable = documentoVersionable;
	}
	
	//mrico - 20110524 - Añado fecha de creacion y modificacion
	public DocumentoAdjunto(Integer docId, String documentoNombre,
			Integer docclassId, String docclassNombre,
			String documentoDescripcion, String mimeType,
			String documentoVersionable, 
			Date documentofechaCreacion, Date documentoFechaMod) {
		super();
		this.docId = docId;
		this.documentoNombre = documentoNombre;
		this.docclassId = docclassId;
		this.docclassNombre = docclassNombre;
		this.documentoDescripcion = documentoDescripcion;
		this.mimeType = mimeType;
		this.documentoVersionable = documentoVersionable;
		
		this.documentoFechaCreacion = documentofechaCreacion;
		this.documentoFechaMod = documentoFechaMod;
		
	}


	public String getId_tipo_objeto() {
		return id_tipo_objeto;
	}


	public void setId_tipo_objeto(String idTipoObjeto) {
		id_tipo_objeto = idTipoObjeto;
	}


	public String getId_objeto() {
		return id_objeto;
	}


	public void setId_objeto(String idObjeto) {
		id_objeto = idObjeto;
	}


	public String getReferencia() {
		return referencia;
	}


	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}


	public String getId_usuario() {
		return id_usuario;
	}


	public void setId_usuario(String idUsuario) {
		id_usuario = idUsuario;
	}


	public Integer getDocId() {
		return docId;
	}


	public void setDocId(Integer docId) {
		this.docId = docId;
	}


	public String getDocumentoNombre() {
		return documentoNombre;
	}


	public void setDocumentoNombre(String documentoNombre) {
		this.documentoNombre = documentoNombre;
	}


	public Integer getDocclassId() {
		return docclassId;
	}


	public void setDocclassId(Integer docclassId) {
		this.docclassId = docclassId;
	}


	public String getDocclassNombre() {
		return docclassNombre;
	}


	public void setDocclassNombre(String docclassNombre) {
		this.docclassNombre = docclassNombre;
	}


	public String getDocumentoDescripcion() {
		return documentoDescripcion;
	}


	public void setDocumentoDescripcion(String documentoDescripcion) {
		this.documentoDescripcion = documentoDescripcion;
	}


	public String getMimeType() {
		return mimeType;
	}


	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}


	public String getDocumentoVersionable() {
		return documentoVersionable;
	}


	public void setDocumentoVersionable(String documentoVersionable) {
		this.documentoVersionable = documentoVersionable;
	}


	@Override
	public String toString() {
		return "DocumentoAdjunto [docId=" + docId + ", docclassId="
				+ docclassId + ", docclassNombre=" + docclassNombre
				+ ", documentoDescripcion=" + documentoDescripcion
				+ ", documentoNombre=" + documentoNombre
				+ ", documentoVersionable=" + documentoVersionable
				+ ", mimeType=" + mimeType + "]";
	}


	public void setDocumentoFechaCreacion(Date documentoFechaCreacion) {
		this.documentoFechaCreacion = documentoFechaCreacion;
	}


	public Date getDocumentoFechaCreacion() {
		return documentoFechaCreacion;
	}


	public void setDocumentoFechaMod(Date documentoFechaMod) {
		this.documentoFechaMod = documentoFechaMod;
	}


	public Date getDocumentoFechaMod() {
		return documentoFechaMod;
	}

	//rrl 20110729
	public boolean isSeleccionMultiple() {
		return seleccionMultiple;
	}

	public void setSeleccionMultiple(boolean seleccionMultiple) {
		this.seleccionMultiple = seleccionMultiple;
	}
}