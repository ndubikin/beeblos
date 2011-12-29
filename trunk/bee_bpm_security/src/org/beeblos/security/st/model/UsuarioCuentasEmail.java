package org.beeblos.security.st.model;

// Generado 25-nov-2010 12:05:17 



/**
 * Domain model class UsuarioCuentasEmail.
 */

public class UsuarioCuentasEmail implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private Integer idUce = 0;
	private Integer idUsuario;
	private boolean preferida;
	private String uceNombre;
	private String uceEmail;
	private String uceDireccionDeRespuesta;
	private String uceTextoDeLaFirma;
//	private String uceArchivoDeFirmaAdjunto;   //rrl 20110722
	private String tipoServidorEntrada;
	private String nombreDelServidorEntrada;
	private Integer puertoEntrada;
	private String seguridadConexion;
	private String metodoIdentificacion;
	private String formato;
	private String nombreServidorDeSalida;
	private String servidorDeSalida;
	private Integer puertoSalida;
	private String seguridadDeLaConexionSalida;
	private String metodoIdentificacionSalida;
	private String nombreUsuarioSalida;
	private String contraseniaSalida;
	private String idExchange;
	//rrl 20110722
	private String uceFirmaAdjuntaTxt;
	private String uceFirmaAdjuntaHtml; 
 
	public UsuarioCuentasEmail() {

	}

	public UsuarioCuentasEmail(
		 		Integer idUsuario
		 		,String uceNombre
		 		,String uceEmail
		 		,String uceDireccionDeRespuesta
		 		,String uceTextoDeLaFirma
		 		,String tipoServidorEntrada
		 		,String nombreDelServidorEntrada
		 		,Integer puertoEntrada
		 		,String seguridadConexion
		 		,String metodoIdentificacion
		 		,String formato
		 		,String nombreServidorDeSalida
		 		,String servidorDeSalida
		 		,Integer puertoSalida
		 		,String seguridadDeLaConexionSalida
		 		,String metodoIdentificacionSalida
		 		,String nombreUsuarioSalida
		 		,String contraseniaSalida
		 		,String idExchange
		 		,String uceFirmaAdjuntaTxt
		 		,String uceFirmaAdjuntaHtml 
		) {
		super();
		
		this.idUsuario = idUsuario;
		this.uceNombre = uceNombre;
		this.uceEmail = uceEmail;
		this.uceDireccionDeRespuesta = uceDireccionDeRespuesta;
		this.uceTextoDeLaFirma = uceTextoDeLaFirma;
//		this.uceArchivoDeFirmaAdjunto = uceArchivoDeFirmaAdjunto;  //rrl 20110722
		this.tipoServidorEntrada = tipoServidorEntrada;
		this.nombreDelServidorEntrada = nombreDelServidorEntrada;
		this.puertoEntrada = puertoEntrada;
		this.seguridadConexion = seguridadConexion;
		this.metodoIdentificacion = metodoIdentificacion;
		this.formato = formato;
		this.nombreServidorDeSalida = nombreServidorDeSalida;
		this.servidorDeSalida = servidorDeSalida;
		this.puertoSalida = puertoSalida;
		this.seguridadDeLaConexionSalida = seguridadDeLaConexionSalida;
		this.metodoIdentificacionSalida = metodoIdentificacionSalida;
		this.nombreUsuarioSalida = nombreUsuarioSalida;
		this.contraseniaSalida = contraseniaSalida;
		this.idExchange = idExchange;
		//rrl 20110722
 		this.uceFirmaAdjuntaTxt = uceFirmaAdjuntaTxt;
 		this.uceFirmaAdjuntaHtml = uceFirmaAdjuntaHtml;
	}

	public UsuarioCuentasEmail(
		 	 Integer idUce
		 	  ,Integer idUsuario
		 	  ,String uceNombre
		 	  ,String uceEmail
		 	  ,String uceDireccionDeRespuesta
		 	  ,String uceTextoDeLaFirma
		 	  ,String tipoServidorEntrada
		 	  ,String nombreDelServidorEntrada
		 	  ,Integer puertoEntrada
		 	  ,String seguridadConexion
		 	  ,String metodoIdentificacion
		 	  ,String formato
		 	  ,String nombreServidorDeSalida
		 	  ,String servidorDeSalida
		 	  ,Integer puertoSalida
		 	  ,String seguridadDeLaConexionSalida
		 	  ,String metodoIdentificacionSalida
		 	  ,String nombreUsuarioSalida
		 	  ,String contraseniaSalida
		 	  ,String idExchange
		 	  ,String uceFirmaAdjuntaTxt
		 	  ,String uceFirmaAdjuntaHtml 
		) {
		super();
		this.idUce = idUce;
		this.idUsuario = idUsuario;
		this.uceNombre = uceNombre;
		this.uceEmail = uceEmail;
		this.uceDireccionDeRespuesta = uceDireccionDeRespuesta;
		this.uceTextoDeLaFirma = uceTextoDeLaFirma;
		this.tipoServidorEntrada = tipoServidorEntrada;
		this.nombreDelServidorEntrada = nombreDelServidorEntrada;
		this.puertoEntrada = puertoEntrada;
		this.seguridadConexion = seguridadConexion;
		this.metodoIdentificacion = metodoIdentificacion;
		this.formato = formato;
		this.nombreServidorDeSalida = nombreServidorDeSalida;
		this.servidorDeSalida = servidorDeSalida;
		this.puertoSalida = puertoSalida;
		this.seguridadDeLaConexionSalida = seguridadDeLaConexionSalida;
		this.metodoIdentificacionSalida = metodoIdentificacionSalida;
		this.nombreUsuarioSalida = nombreUsuarioSalida;
		this.contraseniaSalida = contraseniaSalida;
		this.idExchange = idExchange;
		//rrl 20110722
 		this.uceFirmaAdjuntaTxt = uceFirmaAdjuntaTxt;
 		this.uceFirmaAdjuntaHtml = uceFirmaAdjuntaHtml;
	}

	public Integer getIdUce(){
		
		return idUce;
	
	}
		
	public void setIdUce (Integer idUce){
	
		this.idUce = idUce;
	}
		
	public Integer getIdUsuario(){
		
		return idUsuario;
	
	}
		
	public void setIdUsuario (Integer idUsuario){
	
		this.idUsuario = idUsuario;
	}
	
	
	
	
	/**
	 * @return the preferida
	 */
	public boolean isPreferida() {
		return preferida;
	}

	/**
	 * @param preferida the preferida to set
	 */
	public void setPreferida(boolean preferida) {
		this.preferida = preferida;
	}

	public String getUceNombre(){
		
		return uceNombre;
	
	}

	
	
	public void setUceNombre (String uceNombre){
	
		this.uceNombre = uceNombre;
	}
		
	public String getUceEmail(){
		
		return uceEmail;
	
	}
		
	public void setUceEmail (String uceEmail){
	
		this.uceEmail = uceEmail;
	}
		
	public String getUceDireccionDeRespuesta(){
		
		return uceDireccionDeRespuesta;
	
	}
		
	public void setUceDireccionDeRespuesta (String uceDireccionDeRespuesta){
	
		this.uceDireccionDeRespuesta = uceDireccionDeRespuesta;
	}
		
	public String getUceTextoDeLaFirma(){
		
		return uceTextoDeLaFirma;
	
	}
		
	public void setUceTextoDeLaFirma (String uceTextoDeLaFirma){
	
		this.uceTextoDeLaFirma = uceTextoDeLaFirma;
	}
		
	public String getTipoServidorEntrada(){
		
		return tipoServidorEntrada;
	
	}
		
	public void setTipoServidorEntrada (String tipoServidorEntrada){
	
		this.tipoServidorEntrada = tipoServidorEntrada;
	}
		
	public String getNombreDelServidorEntrada(){
		
		return nombreDelServidorEntrada;
	
	}
		
	public void setNombreDelServidorEntrada (String nombreDelServidorEntrada){
	
		this.nombreDelServidorEntrada = nombreDelServidorEntrada;
	}
		
	public Integer getPuertoEntrada(){
		
		return puertoEntrada;
	
	}
		
	public void setPuertoEntrada (Integer puertoEntrada){
	
		this.puertoEntrada = puertoEntrada;
	}
		
	public String getSeguridadConexion(){
		
		return seguridadConexion;
	
	}
		
	public void setSeguridadConexion (String seguridadConexion){
	
		this.seguridadConexion = seguridadConexion;
	}
		
	public String getMetodoIdentificacion(){
		
		return metodoIdentificacion;
	
	}
		
	public void setMetodoIdentificacion (String metodoIdentificacion){
	
		this.metodoIdentificacion = metodoIdentificacion;
	}
		
	public String getFormato(){
		
		return formato;
	
	}
		
	public void setFormato (String formato){
	
		this.formato = formato;
	}
		
	public String getNombreServidorDeSalida(){
		
		return nombreServidorDeSalida;
	
	}
		
	public void setNombreServidorDeSalida (String nombreServidorDeSalida){
	
		this.nombreServidorDeSalida = nombreServidorDeSalida;
	}
		
	public String getServidorDeSalida(){
		
		return servidorDeSalida;
	
	}
		
	public void setServidorDeSalida (String servidorDeSalida){
	
		this.servidorDeSalida = servidorDeSalida;
	}
		
	public Integer getPuertoSalida(){
		
		return puertoSalida;
	
	}
		
	public void setPuertoSalida (Integer puertoSalida){
	
		this.puertoSalida = puertoSalida;
	}
		
	public String getSeguridadDeLaConexionSalida(){
		
		return seguridadDeLaConexionSalida;
	
	}
		
	public void setSeguridadDeLaConexionSalida (String seguridadDeLaConexionSalida){
	
		this.seguridadDeLaConexionSalida = seguridadDeLaConexionSalida;
	}
		
	public String getMetodoIdentificacionSalida(){
		
		return metodoIdentificacionSalida;
	
	}
		
	public void setMetodoIdentificacionSalida (String metodoIdentificacionSalida){
	
		this.metodoIdentificacionSalida = metodoIdentificacionSalida;
	}
		
	public String getNombreUsuarioSalida(){
		
		return nombreUsuarioSalida;
	
	}
		
	public void setNombreUsuarioSalida (String nombreUsuarioSalida){
	
		this.nombreUsuarioSalida = nombreUsuarioSalida;
	}
		
	public String getContraseniaSalida(){
		
		return contraseniaSalida;
	
	}
		
	public void setContraseniaSalida (String contraseniaSalida){
	
		this.contraseniaSalida = contraseniaSalida;
	}
		
	public String getIdExchange(){
		
		return idExchange;
	
	}
		
	public void setIdExchange (String idExchange){
	
		this.idExchange = idExchange;
	}
	
	//rrl 20110722
	public String getUceFirmaAdjuntaTxt() {
		return uceFirmaAdjuntaTxt;
	}

	public void setUceFirmaAdjuntaTxt(String uceFirmaAdjuntaTxt) {
		this.uceFirmaAdjuntaTxt = uceFirmaAdjuntaTxt;
	}

	public String getUceFirmaAdjuntaHtml() {
		return uceFirmaAdjuntaHtml;
	}

	public void setUceFirmaAdjuntaHtml(String uceFirmaAdjuntaHtml) {
		this.uceFirmaAdjuntaHtml = uceFirmaAdjuntaHtml;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((contraseniaSalida == null) ? 0 : contraseniaSalida
						.hashCode());
		result = prime * result + ((formato == null) ? 0 : formato.hashCode());
		result = prime * result
				+ ((idExchange == null) ? 0 : idExchange.hashCode());
		result = prime * result + ((idUce == null) ? 0 : idUce.hashCode());
		result = prime * result
				+ ((idUsuario == null) ? 0 : idUsuario.hashCode());
		result = prime
				* result
				+ ((metodoIdentificacion == null) ? 0 : metodoIdentificacion
						.hashCode());
		result = prime
				* result
				+ ((metodoIdentificacionSalida == null) ? 0
						: metodoIdentificacionSalida.hashCode());
		result = prime
				* result
				+ ((nombreDelServidorEntrada == null) ? 0
						: nombreDelServidorEntrada.hashCode());
		result = prime
				* result
				+ ((nombreServidorDeSalida == null) ? 0
						: nombreServidorDeSalida.hashCode());
		result = prime
				* result
				+ ((nombreUsuarioSalida == null) ? 0 : nombreUsuarioSalida
						.hashCode());
		result = prime * result + (preferida ? 1231 : 1237);
		result = prime * result
				+ ((puertoEntrada == null) ? 0 : puertoEntrada.hashCode());
		result = prime * result
				+ ((puertoSalida == null) ? 0 : puertoSalida.hashCode());
		result = prime
				* result
				+ ((seguridadConexion == null) ? 0 : seguridadConexion
						.hashCode());
		result = prime
				* result
				+ ((seguridadDeLaConexionSalida == null) ? 0
						: seguridadDeLaConexionSalida.hashCode());
		result = prime
				* result
				+ ((servidorDeSalida == null) ? 0 : servidorDeSalida.hashCode());
		result = prime
				* result
				+ ((tipoServidorEntrada == null) ? 0 : tipoServidorEntrada
						.hashCode());
		result = prime
				* result
				+ ((uceDireccionDeRespuesta == null) ? 0
						: uceDireccionDeRespuesta.hashCode());
		result = prime * result
				+ ((uceEmail == null) ? 0 : uceEmail.hashCode());
		result = prime
				* result
				+ ((uceFirmaAdjuntaHtml == null) ? 0 : uceFirmaAdjuntaHtml
						.hashCode());
		result = prime
				* result
				+ ((uceFirmaAdjuntaTxt == null) ? 0 : uceFirmaAdjuntaTxt
						.hashCode());
		result = prime * result
				+ ((uceNombre == null) ? 0 : uceNombre.hashCode());
		result = prime
				* result
				+ ((uceTextoDeLaFirma == null) ? 0 : uceTextoDeLaFirma
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UsuarioCuentasEmail))
			return false;
		UsuarioCuentasEmail other = (UsuarioCuentasEmail) obj;
		if (contraseniaSalida == null) {
			if (other.contraseniaSalida != null)
				return false;
		} else if (!contraseniaSalida.equals(other.contraseniaSalida))
			return false;
		if (formato == null) {
			if (other.formato != null)
				return false;
		} else if (!formato.equals(other.formato))
			return false;
		if (idExchange == null) {
			if (other.idExchange != null)
				return false;
		} else if (!idExchange.equals(other.idExchange))
			return false;
		if (idUce == null) {
			if (other.idUce != null)
				return false;
		} else if (!idUce.equals(other.idUce))
			return false;
		if (idUsuario == null) {
			if (other.idUsuario != null)
				return false;
		} else if (!idUsuario.equals(other.idUsuario))
			return false;
		if (metodoIdentificacion == null) {
			if (other.metodoIdentificacion != null)
				return false;
		} else if (!metodoIdentificacion.equals(other.metodoIdentificacion))
			return false;
		if (metodoIdentificacionSalida == null) {
			if (other.metodoIdentificacionSalida != null)
				return false;
		} else if (!metodoIdentificacionSalida
				.equals(other.metodoIdentificacionSalida))
			return false;
		if (nombreDelServidorEntrada == null) {
			if (other.nombreDelServidorEntrada != null)
				return false;
		} else if (!nombreDelServidorEntrada
				.equals(other.nombreDelServidorEntrada))
			return false;
		if (nombreServidorDeSalida == null) {
			if (other.nombreServidorDeSalida != null)
				return false;
		} else if (!nombreServidorDeSalida.equals(other.nombreServidorDeSalida))
			return false;
		if (nombreUsuarioSalida == null) {
			if (other.nombreUsuarioSalida != null)
				return false;
		} else if (!nombreUsuarioSalida.equals(other.nombreUsuarioSalida))
			return false;
		if (preferida != other.preferida)
			return false;
		if (puertoEntrada == null) {
			if (other.puertoEntrada != null)
				return false;
		} else if (!puertoEntrada.equals(other.puertoEntrada))
			return false;
		if (puertoSalida == null) {
			if (other.puertoSalida != null)
				return false;
		} else if (!puertoSalida.equals(other.puertoSalida))
			return false;
		if (seguridadConexion == null) {
			if (other.seguridadConexion != null)
				return false;
		} else if (!seguridadConexion.equals(other.seguridadConexion))
			return false;
		if (seguridadDeLaConexionSalida == null) {
			if (other.seguridadDeLaConexionSalida != null)
				return false;
		} else if (!seguridadDeLaConexionSalida
				.equals(other.seguridadDeLaConexionSalida))
			return false;
		if (servidorDeSalida == null) {
			if (other.servidorDeSalida != null)
				return false;
		} else if (!servidorDeSalida.equals(other.servidorDeSalida))
			return false;
		if (tipoServidorEntrada == null) {
			if (other.tipoServidorEntrada != null)
				return false;
		} else if (!tipoServidorEntrada.equals(other.tipoServidorEntrada))
			return false;
		if (uceDireccionDeRespuesta == null) {
			if (other.uceDireccionDeRespuesta != null)
				return false;
		} else if (!uceDireccionDeRespuesta
				.equals(other.uceDireccionDeRespuesta))
			return false;
		if (uceEmail == null) {
			if (other.uceEmail != null)
				return false;
		} else if (!uceEmail.equals(other.uceEmail))
			return false;
		if (uceFirmaAdjuntaHtml == null) {
			if (other.uceFirmaAdjuntaHtml != null)
				return false;
		} else if (!uceFirmaAdjuntaHtml.equals(other.uceFirmaAdjuntaHtml))
			return false;
		if (uceFirmaAdjuntaTxt == null) {
			if (other.uceFirmaAdjuntaTxt != null)
				return false;
		} else if (!uceFirmaAdjuntaTxt.equals(other.uceFirmaAdjuntaTxt))
			return false;
		if (uceNombre == null) {
			if (other.uceNombre != null)
				return false;
		} else if (!uceNombre.equals(other.uceNombre))
			return false;
		if (uceTextoDeLaFirma == null) {
			if (other.uceTextoDeLaFirma != null)
				return false;
		} else if (!uceTextoDeLaFirma.equals(other.uceTextoDeLaFirma))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UsuarioCuentasEmail [contraseniaSalida=" + contraseniaSalida
				+ ", formato=" + formato + ", idExchange=" + idExchange
				+ ", idUce=" + idUce + ", idUsuario=" + idUsuario
				+ ", metodoIdentificacion=" + metodoIdentificacion
				+ ", metodoIdentificacionSalida=" + metodoIdentificacionSalida
				+ ", nombreDelServidorEntrada=" + nombreDelServidorEntrada
				+ ", nombreServidorDeSalida=" + nombreServidorDeSalida
				+ ", nombreUsuarioSalida=" + nombreUsuarioSalida
				+ ", preferida=" + preferida + ", puertoEntrada="
				+ puertoEntrada + ", puertoSalida=" + puertoSalida
				+ ", seguridadConexion=" + seguridadConexion
				+ ", seguridadDeLaConexionSalida="
				+ seguridadDeLaConexionSalida + ", servidorDeSalida="
				+ servidorDeSalida + ", tipoServidorEntrada="
				+ tipoServidorEntrada + ", uceDireccionDeRespuesta="
				+ uceDireccionDeRespuesta + ", uceEmail=" + uceEmail
				+ ", uceFirmaAdjuntaHtml=" + uceFirmaAdjuntaHtml
				+ ", uceFirmaAdjuntaTxt=" + uceFirmaAdjuntaTxt + ", uceNombre="
				+ uceNombre + ", uceTextoDeLaFirma=" + uceTextoDeLaFirma + "]";
	}
	
}




