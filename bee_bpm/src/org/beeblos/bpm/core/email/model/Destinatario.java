package org.beeblos.bpm.core.email.model;

import java.io.Serializable;


public class Destinatario implements Serializable{
	
	private String email;
	private String nombre;
	private String apellidos;

	private Integer idObjeto1;
	private Integer idObjeto2;
	private String idTipoObjeto;
	
	private String idBL;
	private String metodoGet;
	
	private boolean actualizar;
	private String campoAActualizar;
	private String valor;
	private String tipoDeDato;

	private String metodoUpdate;
	
	private Integer tipoDestinatario;  //rrl 20110720

	
	public Destinatario(String email, String nombre, String apellidos,
			Integer idObjeto1, Integer idObjeto2, String idTipoObjeto,
			String idBL, String metodoGet, boolean actualizar,
			String campoAActualizar, String valor, String tipoDeDato,
			String metodoUpdate,
			Integer tipoDestinatario
			) {
		this.email = email;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.idObjeto1 = idObjeto1;
		this.idObjeto2 = idObjeto2;
		this.idTipoObjeto = idTipoObjeto;
		this.idBL = idBL;
		this.metodoGet = metodoGet;
		this.actualizar = actualizar;
		this.campoAActualizar = campoAActualizar;
		this.valor = valor;
		this.tipoDeDato = tipoDeDato;
		this.metodoUpdate = metodoUpdate;
		this.tipoDestinatario = tipoDestinatario;   //rrl 20110720
	}
	public String getMetodoGet() {
		return metodoGet;
	}
	public void setMetodoGet(String metodoGet) {
		this.metodoGet = metodoGet;
	}
	public String getMetodoUpdate() {
		return metodoUpdate;
	}
	public void setMetodoUpdate(String metodoUpdate) {
		this.metodoUpdate = metodoUpdate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public Integer getIdObjeto1() {
		return idObjeto1;
	}
	public void setIdObjeto1(Integer idObjeto1) {
		this.idObjeto1 = idObjeto1;
	}
	public Integer getIdObjeto2() {
		return idObjeto2;
	}
	public void setIdObjeto2(Integer idObjeto2) {
		this.idObjeto2 = idObjeto2;
	}
	public String getIdTipoObjeto() {
		return idTipoObjeto;
	}
	public void setIdTipoObjeto(String idTipoObjeto) {
		this.idTipoObjeto = idTipoObjeto;
	}
	public boolean isActualizar() {
		return actualizar;
	}
	public void setActualizar(boolean actualizar) {
		this.actualizar = actualizar;
	}
	public String getCampoAActualizar() {
		return campoAActualizar;
	}
	public void setCampoAActualizar(String campoAActualizar) {
		this.campoAActualizar = campoAActualizar;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getTipoDeDato() {
		return tipoDeDato;
	}
	public void setTipoDeDato(String tipoDeDato) {
		this.tipoDeDato = tipoDeDato;
	}

	public String getIdBL() {
		return idBL;
	}
	public void setIdBL(String idBL) {
		this.idBL = idBL;
	}

	//rrl 20110720
	public Integer getTipoDestinatario() {
		return tipoDestinatario;
	}
	public void setTipoDestinatario(Integer tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}
	
	public Destinatario() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (actualizar ? 1231 : 1237);
		result = prime * result
				+ ((apellidos == null) ? 0 : apellidos.hashCode());
		result = prime
				* result
				+ ((campoAActualizar == null) ? 0 : campoAActualizar.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((idBL == null) ? 0 : idBL.hashCode());
		result = prime * result
				+ ((idObjeto1 == null) ? 0 : idObjeto1.hashCode());
		result = prime * result
				+ ((idObjeto2 == null) ? 0 : idObjeto2.hashCode());
		result = prime * result
				+ ((idTipoObjeto == null) ? 0 : idTipoObjeto.hashCode());
		result = prime * result
				+ ((metodoGet == null) ? 0 : metodoGet.hashCode());
		result = prime * result
				+ ((metodoUpdate == null) ? 0 : metodoUpdate.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result
				+ ((tipoDeDato == null) ? 0 : tipoDeDato.hashCode());
		result = prime
				* result
				+ ((tipoDestinatario == null) ? 0 : tipoDestinatario.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Destinatario))
			return false;
		Destinatario other = (Destinatario) obj;
		if (actualizar != other.actualizar)
			return false;
		if (apellidos == null) {
			if (other.apellidos != null)
				return false;
		} else if (!apellidos.equals(other.apellidos))
			return false;
		if (campoAActualizar == null) {
			if (other.campoAActualizar != null)
				return false;
		} else if (!campoAActualizar.equals(other.campoAActualizar))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (idBL == null) {
			if (other.idBL != null)
				return false;
		} else if (!idBL.equals(other.idBL))
			return false;
		if (idObjeto1 == null) {
			if (other.idObjeto1 != null)
				return false;
		} else if (!idObjeto1.equals(other.idObjeto1))
			return false;
		if (idObjeto2 == null) {
			if (other.idObjeto2 != null)
				return false;
		} else if (!idObjeto2.equals(other.idObjeto2))
			return false;
		if (idTipoObjeto == null) {
			if (other.idTipoObjeto != null)
				return false;
		} else if (!idTipoObjeto.equals(other.idTipoObjeto))
			return false;
		if (metodoGet == null) {
			if (other.metodoGet != null)
				return false;
		} else if (!metodoGet.equals(other.metodoGet))
			return false;
		if (metodoUpdate == null) {
			if (other.metodoUpdate != null)
				return false;
		} else if (!metodoUpdate.equals(other.metodoUpdate))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (tipoDeDato == null) {
			if (other.tipoDeDato != null)
				return false;
		} else if (!tipoDeDato.equals(other.tipoDeDato))
			return false;
		if (tipoDestinatario == null) {
			if (other.tipoDestinatario != null)
				return false;
		} else if (!tipoDestinatario.equals(other.tipoDestinatario))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Destinatario [actualizar=" + actualizar + ", apellidos="
				+ apellidos + ", campoAActualizar=" + campoAActualizar
				+ ", email=" + email + ", idBL=" + idBL + ", idObjeto1="
				+ idObjeto1 + ", idObjeto2=" + idObjeto2 + ", idTipoObjeto="
				+ idTipoObjeto + ", metodoGet=" + metodoGet + ", metodoUpdate="
				+ metodoUpdate + ", nombre=" + nombre + ", tipoDeDato="
				+ tipoDeDato + ", tipoDestinatario=" + tipoDestinatario
				+ ", valor=" + valor + "]";
	}

}
