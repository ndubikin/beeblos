package org.beeblos.security.auxiliar.model;
/*
 * 
 * nes 20101208 
 * 
 * modifique el toString que estaba mal ...
 * 
 */

public class Pais implements java.io.Serializable {

	private static final long serialVersionUID = -9104012070788219562L;
	
	private String idPais;
	private String paisNombre;
	private String paisComentario;
	
	// rrl 20110510 Nombre de la clase a importar/exportar XML <--> OBJECT
	public String getClassName() {
		return getClass().getName();
	}	
	
	public Pais() {
	}

	public Pais(String idPais) {
		this.idPais = idPais;
	}

	public Pais(String paisNombre, String paisComentario) {
		this.paisNombre = paisNombre;
		this.paisComentario = paisComentario;

	}
	
	public Pais(String idPais, String paisNombre, String paisComentario) {
		this.idPais = idPais;
		this.paisNombre = paisNombre;
		this.paisComentario = paisComentario;

	}

	public String getIdPais() {
		return this.idPais;
	}

	public void setIdPais(String idPais) {
		this.idPais = idPais;
	}

	public String getPaisNombre() {
		return this.paisNombre;
	}

	public void setPaisNombre(String paisNombre) {
		this.paisNombre = paisNombre;
	}

	public String getPaisComentario() {
		return this.paisComentario;
	}

	public void setPaisComentario(String paisComentario) {
		this.paisComentario = paisComentario;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPais == null) ? 0 : idPais.hashCode());
		result = prime * result
				+ ((paisComentario == null) ? 0 : paisComentario.hashCode());
		result = prime * result
				+ ((paisNombre == null) ? 0 : paisNombre.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Pais))
			return false;
		Pais other = (Pais) obj;
		if (idPais == null) {
			if (other.idPais != null)
				return false;
		} else if (!idPais.equals(other.idPais))
			return false;
		if (paisComentario == null) {
			if (other.paisComentario != null)
				return false;
		} else if (!paisComentario.equals(other.paisComentario))
			return false;
		if (paisNombre == null) {
			if (other.paisNombre != null)
				return false;
		} else if (!paisNombre.equals(other.paisNombre))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Pais ["
				+ (idPais != null ? "idPais=" + idPais + ", " : "")
				+ (paisComentario != null ? "paisComentario=" + paisComentario
						+ ", " : "")
				+ (paisNombre != null ? "paisNombre=" + paisNombre : "") + "]";
	}

	// nes 20110210
	public boolean empty() {
		
		if (idPais!=null && !"".equals(idPais) ) return false;
		if (paisNombre != null && !"".equals(paisNombre)) return false;

		return true;
	}
	
	
}
