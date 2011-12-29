package org.beeblos.security.auxiliar.model;
/*
 * 
 */


public class TipoTerritorio implements java.io.Serializable {

	private static final long serialVersionUID = -3017869225239812045L;
	
	private Integer idTipoTerritorio=0;
		
	private String tipoTerritorioNombre;
	private String tipoTerritorioComentario;
	
	private Pais tipoTerritorioPais;
	private Integer tipoTerritorioNivel;
	//private TipoTerritorio tipoTerritorioSuperior;
	
	// rrl 20110510 Nombre de la clase a importar/exportar XML <--> OBJECT
	public String getClassName() {
		return getClass().getName();
	}	
	
	/*
	 * version 1.01 
	 * date: 20101002
	 */
	
	public TipoTerritorio(){
		
	}

	public TipoTerritorio(boolean crearObjetosVacios ){
		super();
		if ( crearObjetosVacios ) {

			this.tipoTerritorioPais=new Pais();

		}	
	}
	
	public void setIdTipoTerritorio(Integer idTipoTerritorio) {
		this.idTipoTerritorio = idTipoTerritorio;
	}
	public Integer getIdTipoTerritorio() {
		return idTipoTerritorio;
	}
	public void setTipoTerritorioNombre(String tipoTerritorioNombre) {
		this.tipoTerritorioNombre = tipoTerritorioNombre;
	}
	public String getTipoTerritorioNombre() {
		return tipoTerritorioNombre;
	}
	public void setTipoTerritorioComentario(String tipoTerritorioComentario) {
		this.tipoTerritorioComentario = tipoTerritorioComentario;
	}
	public String getTipoTerritorioComentario() {
		return tipoTerritorioComentario;
	}
	public void setTipoTerritorioPais(Pais tipoTerritorioPais) {
		this.tipoTerritorioPais = tipoTerritorioPais;
	}
	public Pais getTipoTerritorioPais() {
		return tipoTerritorioPais;
	}

	
	
	public void setTipoTerritorioNivel(Integer tipoTerritorioNivel) {
		this.tipoTerritorioNivel = tipoTerritorioNivel;
	}

	public Integer getTipoTerritorioNivel() {
		return tipoTerritorioNivel;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((tipoTerritorioComentario == null) ? 0
						: tipoTerritorioComentario.hashCode());
		result = prime
				* result
				+ ((tipoTerritorioNivel == null) ? 0 : tipoTerritorioNivel
						.hashCode());
		result = prime
				* result
				+ ((tipoTerritorioNombre == null) ? 0 : tipoTerritorioNombre
						.hashCode());
		result = prime
				* result
				+ ((tipoTerritorioPais == null) ? 0 : tipoTerritorioPais
						.hashCode());
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
		TipoTerritorio other = (TipoTerritorio) obj;
		if (tipoTerritorioComentario == null) {
			if (other.tipoTerritorioComentario != null)
				return false;
		} else if (!tipoTerritorioComentario
				.equals(other.tipoTerritorioComentario))
			return false;
		if (tipoTerritorioNivel == null) {
			if (other.tipoTerritorioNivel != null)
				return false;
		} else if (!tipoTerritorioNivel.equals(other.tipoTerritorioNivel))
			return false;
		if (tipoTerritorioNombre == null) {
			if (other.tipoTerritorioNombre != null)
				return false;
		} else if (!tipoTerritorioNombre.equals(other.tipoTerritorioNombre))
			return false;
		if (tipoTerritorioPais == null) {
			if (other.tipoTerritorioPais != null)
				return false;
		} else if (!tipoTerritorioPais.equals(other.tipoTerritorioPais))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "TipoTerritorio [idTipoTerritorio=" + idTipoTerritorio
				+ ", tipoTerritorioComentario=" + tipoTerritorioComentario
				+ ", tipoTerritorioNivel=" + tipoTerritorioNivel
				+ ", tipoTerritorioNombre=" + tipoTerritorioNombre
				+ ", tipoTerritorioPais=" + tipoTerritorioPais + "]";
	}

	
	
	
	
}
