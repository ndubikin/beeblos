package org.beeblos.security.auxiliar.model;

import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;

public class Territorio implements java.io.Serializable {

	private static final long serialVersionUID = 7989841469300041052L;

	
	private Integer idTerritorio=0;
	
	private String territorioNombre;
	private String territorioComentario;
	private String territorioCodigo;
	private String territorioCodigopostal;
	private Territorio territorioPadre;  // Se almacena solo el territorio m�s especifico 
											// (el nivel m�s bajo)
	private Pais territorioPais;
	
	private TipoTerritorio tipoTerritorio;
	//private Set direccions = new HashSet(0);

	// rrl 20110510 Nombre de la clase a importar/exportar XML <--> OBJECT
	public String getClassName() {
		return getClass().getName();
	}	
	
	public Territorio() {
	}

	public Territorio(boolean crearObjetosVacios ){
		super();
		if ( crearObjetosVacios ) {

			this.territorioPadre=new Territorio( EMPTY_OBJECT );
			this.territorioPais=new Pais();
			this.tipoTerritorio=new TipoTerritorio( EMPTY_OBJECT );
		}	
	}
	
	
	public Territorio(
			String territorioNombre, 
			String territorioComentario,
			String territorioCodigo,
			String territorioCodigopostal
				) {
		
		this.territorioNombre = territorioNombre;
		this.territorioComentario = territorioComentario;
		this.territorioCodigo = territorioCodigo;
		this.territorioCodigopostal = territorioCodigopostal;
	}

	public Territorio(Integer idTerritorio, String territorioNombre,
			String territorioComentario, String territorioCodigo,
			String territorioCodigopostal, Territorio territorioPadre,
			Pais territorioPais, TipoTerritorio tipoTerritorio) {
		super();
		this.idTerritorio = idTerritorio;
		this.territorioNombre = territorioNombre;
		this.territorioComentario = territorioComentario;
		this.territorioCodigo = territorioCodigo;
		this.territorioCodigopostal = territorioCodigopostal;
		this.territorioPadre = territorioPadre;
		this.territorioPais = territorioPais;
		this.tipoTerritorio = tipoTerritorio;
	}

	public void setIdTerritorio(Integer idTerritorio) {
		this.idTerritorio = idTerritorio;
	}

	public Integer getIdTerritorio() {
		return idTerritorio;
	}

	public void setTerritorioNombre(String territorioNombre) {
		this.territorioNombre = territorioNombre;
	}

	public String getTerritorioNombre() {
		return territorioNombre;
	}

	public void setTerritorioComentario(String territorioComentario) {
		this.territorioComentario = territorioComentario;
	}

	public String getTerritorioComentario() {
		return territorioComentario;
	}

	public void setTerritorioCodigo(String territorioCodigo) {
		this.territorioCodigo = territorioCodigo;
	}

	public String getTerritorioCodigo() {
		return territorioCodigo;
	}

	public void setTerritorioCodigopostal(String territorioCodigopostal) {
		this.territorioCodigopostal = territorioCodigopostal;
	}

	public String getTerritorioCodigopostal() {
		return territorioCodigopostal;
	}

	public void setTerritorioPadre(Territorio territorioPadre) {
		this.territorioPadre = territorioPadre;
	}

	public Territorio getTerritorioPadre() {
		return territorioPadre;
	}

	public void setTerritorioPais(Pais territorioPais) {
		this.territorioPais = territorioPais;
	}

	public Pais getTerritorioPais() {
		return territorioPais;
	}

	public void setTipoTerritorio(TipoTerritorio tipoTerritorio) {
		this.tipoTerritorio = tipoTerritorio;
	}

	public TipoTerritorio getTipoTerritorio() {
		return tipoTerritorio;
	}

	public String toString () {
		String ret = " id territorio:["+idTerritorio+"]";
		ret += " territorioNombre:["+territorioNombre+"]";
		ret += " territorioComentario:["+territorioComentario+"]";
		ret += " territorioCodigo:["+territorioCodigo+"]";
		ret += " territorioCodigopostal:["+territorioCodigopostal+"]";
		ret += " id territorioPadre:["+ (territorioPadre==null?"":territorioPadre.getIdTerritorio())+"]"; 
		ret += " id Pais:["+(territorioPais==null?"":territorioPais.getIdPais())+"]";
		
		ret += " id tipoTerritorio:"+tipoTerritorio.getIdTipoTerritorio()+"]";
		return ret;
	}
	

	// nes 20110928 - para temas de indexación ...
	public String toStringForIndex () {
		String ret = " id territorio="+idTerritorio+" ";
		ret +=  territorioNombre+" ";
		ret +=  territorioComentario+" ";
		ret +=  territorioCodigo+" ";
		ret +=  territorioCodigopostal+" ";
		ret += (territorioPais!=null?territorioPais.getPaisNombre():" " )+" ";
		ret += ( tipoTerritorio.getIdTipoTerritorio()!=null?tipoTerritorio.getTipoTerritorioNombre():" ")+" " ;

		ret += ( territorioPadre!=null?territorioPadre.toStringForIndex():" ")+" ";
		
		return ret;
	}

	
	
//	anulo porque no se usa: nes 20110928
//	public boolean Equals(Territorio t) {
//		
//		if (	this.territorioNombre.equals(t.getTerritorioNombre()) &&
//				this.territorioComentario.equals(t.getTerritorioComentario()) &&
//				this.territorioCodigo.equals(t.getTerritorioCodigo()) &&
//				this.territorioCodigopostal.equals(t.getTerritorioCodigopostal()) &&
//				this.territorioPadre.getIdTerritorio()==t.getTerritorioPadre().getIdTerritorio() &&
//				this.territorioPais.getIdPais().equals(t.getTerritorioPais().getIdPais()) 
//				) {
//				//this.tipoTerritorio;)
//			return true;
//		
//		} else {
//			return false;
//		}
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((territorioCodigo == null) ? 0 : territorioCodigo.hashCode());
		result = prime
				* result
				+ ((territorioCodigopostal == null) ? 0
						: territorioCodigopostal.hashCode());
		result = prime
				* result
				+ ((territorioComentario == null) ? 0 : territorioComentario
						.hashCode());
		result = prime
				* result
				+ ((territorioNombre == null) ? 0 : territorioNombre.hashCode());
		result = prime * result
				+ ((territorioPadre == null) ? 0 : territorioPadre.hashCode());
		result = prime * result
				+ ((territorioPais == null) ? 0 : territorioPais.hashCode());
		result = prime * result
				+ ((tipoTerritorio == null) ? 0 : tipoTerritorio.hashCode());
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
		Territorio other = (Territorio) obj;
		if (territorioCodigo == null) {
			if (other.territorioCodigo != null)
				return false;
		} else if (!territorioCodigo.equals(other.territorioCodigo))
			return false;
		if (territorioCodigopostal == null) {
			if (other.territorioCodigopostal != null)
				return false;
		} else if (!territorioCodigopostal.equals(other.territorioCodigopostal))
			return false;
		if (territorioComentario == null) {
			if (other.territorioComentario != null)
				return false;
		} else if (!territorioComentario.equals(other.territorioComentario))
			return false;
		if (territorioNombre == null) {
			if (other.territorioNombre != null)
				return false;
		} else if (!territorioNombre.equals(other.territorioNombre))
			return false;
		if (territorioPadre == null) {
			if (other.territorioPadre != null)
				return false;
		} else if (!territorioPadre.equals(other.territorioPadre))
			return false;
		if (territorioPais == null) {
			if (other.territorioPais != null)
				return false;
		} else if (!territorioPais.equals(other.territorioPais))
			return false;
		if (tipoTerritorio == null) {
			if (other.tipoTerritorio != null)
				return false;
		} else if (!tipoTerritorio.equals(other.tipoTerritorio))
			return false;
		return true;
	}
	
}
