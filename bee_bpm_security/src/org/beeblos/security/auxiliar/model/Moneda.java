package org.beeblos.security.auxiliar.model;

// Generated Aug 10, 2010 10:35:06 PM by Hibernate Tools 3.3.0.GA
// Modified Oct 3, 2010 - Nes

/**
 * Moneda generated by hbm2java
 */
public class Moneda implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idMoneda=0;
	private String monedaNombre;
	private String monedaSimbolo;

	public Moneda() {
	}

	public Moneda(String monedaNombre, String monedaSimbolo) {
		this.monedaNombre = monedaNombre;
		this.monedaSimbolo = monedaSimbolo;
	}

	public Integer getIdMoneda() {
		return this.idMoneda;
	}

	public void setIdMoneda(Integer idMoneda) {
		this.idMoneda = idMoneda;
	}

	public String getMonedaNombre() {
		return this.monedaNombre;
	}

	public void setMonedaNombre(String monedaNombre) {
		this.monedaNombre = monedaNombre;
	}

	public String getMonedaSimbolo() {
		return this.monedaSimbolo;
	}

	public void setMonedaSimbolo(String monedaSimbolo) {
		this.monedaSimbolo = monedaSimbolo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((monedaNombre == null) ? 0 : monedaNombre.hashCode());
		result = prime * result
				+ ((monedaSimbolo == null) ? 0 : monedaSimbolo.hashCode());
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
		Moneda other = (Moneda) obj;
		if (monedaNombre == null) {
			if (other.monedaNombre != null)
				return false;
		} else if (!monedaNombre.equals(other.monedaNombre))
			return false;
		if (monedaSimbolo == null) {
			if (other.monedaSimbolo != null)
				return false;
		} else if (!monedaSimbolo.equals(other.monedaSimbolo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Moneda [idMoneda=" + idMoneda + ", monedaNombre="
				+ monedaNombre + ", monedaSimbolo=" + monedaSimbolo + "]";
	}

	
	
	
}
