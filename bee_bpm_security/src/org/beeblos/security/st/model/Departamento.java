package org.beeblos.security.st.model;

import java.io.Serializable;

public class Departamento implements Serializable {

	private static final long serialVersionUID = 2102844061960525981L;
	
	private Integer idDepartamento = 0;
	private java.lang.String departamentoNombre;
	private java.lang.String departamentoComentario;
	private java.lang.String departamentoAbreviatura;
	private java.lang.String departamentoTitulo;  //rrl 20111108
	
	// rrl 20110510 Nombre de la clase a importar/exportar XML <--> OBJECT
	public String getClassName() {
		return getClass().getName();
	}	

	public Departamento() {
		super();
	}

	public Departamento(String departamentoNombre,
			String departamentoComentario, String departamentoAbreviatura, String departamentoTitulo) {
		super();

		this.departamentoNombre = departamentoNombre;
		this.departamentoAbreviatura = departamentoAbreviatura;
		this.departamentoComentario = departamentoComentario;
		this.departamentoTitulo = departamentoTitulo;    //rrl 20111108

	}

	public Departamento(Integer idDepartamento, String departamentoNombre,
			String departamentoComentario, String departamentoAbreviatura, String departamentoTitulo) {
		super();
		this.idDepartamento = idDepartamento;
		this.departamentoNombre = departamentoNombre;
		this.departamentoAbreviatura = departamentoAbreviatura;
		this.departamentoComentario = departamentoComentario;
		this.departamentoTitulo = departamentoTitulo;    //rrl 20111108

	}

	public Integer getIdDepartamento() {
		return idDepartamento;
	}

	public void setIdDepartamento(Integer idDepartamento) {
		this.idDepartamento = idDepartamento;
	}

	public java.lang.String getDepartamentoNombre() {
		return departamentoNombre;
	}

	public void setDepartamentoNombre(java.lang.String departamentoNombre) {
		this.departamentoNombre = departamentoNombre;
	}

	public java.lang.String getDepartamentoComentario() {
		return departamentoComentario;
	}

	public void setDepartamentoComentario(java.lang.String departamentoComentario) {
		this.departamentoComentario = departamentoComentario;
	}

	public java.lang.String getDepartamentoAbreviatura() {
		return departamentoAbreviatura;
	}

	public void setDepartamentoAbreviatura(java.lang.String departamentoAbreviatura) {
		this.departamentoAbreviatura = departamentoAbreviatura;
	}
	
	public java.lang.String getDepartamentoTitulo() {
		return departamentoTitulo;
	}

	public void setDepartamentoTitulo(java.lang.String departamentoTitulo) {
		this.departamentoTitulo = departamentoTitulo;
	}

	public Integer compareTo(Object obj) {
		if (obj == null) {
			return -1;
		}

		Departamento dp = (Departamento) obj;
		Integer pk = dp.getIdDepartamento();
		return ( (getIdDepartamento().compareTo(pk) == 1 &&
				 this.getDepartamentoAbreviatura().compareTo(dp.getDepartamentoAbreviatura()) == 1)?1:-1 );
	}

	//rrl 20110829
	public boolean empty() {
		if (idDepartamento!=0 ) return false;
		if (departamentoNombre != null && !"".equals(departamentoNombre)) return false;
		if (departamentoComentario != null && !"".equals(departamentoComentario)) return false;
		if (departamentoAbreviatura != null && !"".equals(departamentoAbreviatura)) return false;
		if (departamentoTitulo != null && !"".equals(departamentoTitulo)) return false;  // rrl 20111108
		  
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((departamentoAbreviatura == null) ? 0
						: departamentoAbreviatura.hashCode());
		result = prime
				* result
				+ ((departamentoComentario == null) ? 0
						: departamentoComentario.hashCode());
		result = prime
				* result
				+ ((departamentoNombre == null) ? 0 : departamentoNombre
						.hashCode());
		result = prime
				* result
				+ ((departamentoTitulo == null) ? 0 : departamentoTitulo
						.hashCode());
		result = prime * result
				+ ((idDepartamento == null) ? 0 : idDepartamento.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Departamento))
			return false;
		Departamento other = (Departamento) obj;
		if (departamentoAbreviatura == null) {
			if (other.departamentoAbreviatura != null)
				return false;
		} else if (!departamentoAbreviatura
				.equals(other.departamentoAbreviatura))
			return false;
		if (departamentoComentario == null) {
			if (other.departamentoComentario != null)
				return false;
		} else if (!departamentoComentario.equals(other.departamentoComentario))
			return false;
		if (departamentoNombre == null) {
			if (other.departamentoNombre != null)
				return false;
		} else if (!departamentoNombre.equals(other.departamentoNombre))
			return false;
		if (departamentoTitulo == null) {
			if (other.departamentoTitulo != null)
				return false;
		} else if (!departamentoTitulo.equals(other.departamentoTitulo))
			return false;
		if (idDepartamento == null) {
			if (other.idDepartamento != null)
				return false;
		} else if (!idDepartamento.equals(other.idDepartamento))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Departamento [departamentoAbreviatura="
				+ departamentoAbreviatura + ", departamentoComentario="
				+ departamentoComentario + ", departamentoNombre="
				+ departamentoNombre + ", departamentoTitulo="
				+ departamentoTitulo + ", idDepartamento=" + idDepartamento
				+ "]";
	}
	
}
