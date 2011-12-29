package org.beeblos.security.auxiliar.bl;

import java.util.ArrayList;
import java.util.List;

import static org.beeblos.security.st.util.Constantes.ID_PAIS_GENERICO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.security.auxiliar.dao.TipoTerritorioDao;
import org.beeblos.security.auxiliar.error.TipoTerritorioException;
import org.beeblos.security.auxiliar.model.Pais;
import org.beeblos.security.auxiliar.model.TipoTerritorio;




public class TipoTerritorioBL {
	
	private static final Log logger = LogFactory.getLog(TipoTerritorioBL.class);

	public TipoTerritorioBL() {

	}

	public int agregar(TipoTerritorio tipoTerritorio) throws TipoTerritorioException {
		
		TipoTerritorioDao tDao = new TipoTerritorioDao();
		
		if (tipoTerritorio.getTipoTerritorioPais() == null){
			throw new TipoTerritorioException("Error en agregar tipo territorio: Debe definirse el Pais al que pertenece el Tipo de Territorio nombre:["+
					tipoTerritorio.getTipoTerritorioNombre()+
					"] - pais:["+tipoTerritorio.getTipoTerritorioPais()+"] "+
					"] - nivel:["+tipoTerritorio.getTipoTerritorioNivel()+"]");
		}
		
		String idPais = tipoTerritorio.getTipoTerritorioPais().getIdPais();
		
		try {
			// Si se ha declarado un nivel para este tipo de territorio se controla que no exista
			if (tipoTerritorio.getTipoTerritorioNivel() != null){
				
				if (tDao.obtenerTipoTerritorioPaisNivel( idPais, 
					tipoTerritorio.getTipoTerritorioNivel() ) != null ) {
					
					throw new TipoTerritorioException("Error en agregar tipo territorio:Ya existe un Tipo de Territorio en el nivel especificado :[ "+
								tipoTerritorio.getTipoTerritorioNivel() +"] "
								+" para este idPais:["+idPais+"] ");
				
					// martin - 20100716 - Comprobacion nivel asignado es correcto	
				} else if (tipoTerritorio.getTipoTerritorioNivel() != 
				
					this.obtenerNuevoNivelMinimo(idPais) ){
					
					throw new TipoTerritorioException("Error en agregar tipo territorio:El nivel especificado no es correcto nombre:["+
														tipoTerritorio.getTipoTerritorioNombre()+
														"] - pais:["+tipoTerritorio.getTipoTerritorioPais()+"] "+
					                                    "] - nivel:["+tipoTerritorio.getTipoTerritorioNivel()+"]");
				}
						
			// No se ha definido el nivel: se establace automáticamente como nuevo nivel inferior 	
			} else {
				tipoTerritorio.setTipoTerritorioNivel(this.obtenerNuevoNivelMinimo(idPais));
			}
	
		} catch (TipoTerritorioException e) {
			throw new TipoTerritorioException("Error en agregar tipo territorio:No pudo agregarse el nuevo tipo de Territorio nombre:["+
													tipoTerritorio.getTipoTerritorioNombre()+
													"] - pais:["+tipoTerritorio.getTipoTerritorioPais()+"] "+
			                                        "] - nivel:["+tipoTerritorio.getTipoTerritorioNivel()+"] \n" + e.getMessage());
		}
		
		//Comporbacion de nombre no repetido en Pais
		try {
			
			if (this.obtenerTipoTerritorioPorNombreYPais(
					tipoTerritorio.getTipoTerritorioNombre(),
					idPais) != null){
				
				throw new TipoTerritorioException("Error en agregar tipo territorio:Ya existe un tipo de territorio con ese nombre en este pais :["+
													tipoTerritorio.getTipoTerritorioNombre()+
													"] - pais:["+tipoTerritorio.getTipoTerritorioPais()+"] "+
			                                        "] - nivel:["+tipoTerritorio.getTipoTerritorioNivel()+"] \n");
			} 
			
			
		} catch (TipoTerritorioException e) {
			logger.warn("Error en agregar tipo territorio " + tipoTerritorio.getTipoTerritorioNombre()+
					"] - pais:["+tipoTerritorio.getTipoTerritorioPais()+"] "+
			        "] - nivel:["+tipoTerritorio.getTipoTerritorioNivel()+"] \n");
			e.printStackTrace();
		}
		
		

		return tDao.agregar(tipoTerritorio);

	}

	public void actualizar(TipoTerritorio tipoTerritorio) throws TipoTerritorioException {
		
		//Comporbacion de nombre no repetido en Pais
		try {
			if (this.obtenerTipoTerritorioPorNombreYPais(
					tipoTerritorio.getTipoTerritorioNombre(),
					tipoTerritorio.getTipoTerritorioPais().getIdPais() ) != null){

				// martín: no terminé de entender este control aquí ... o mejor dicho no entiendo el mensaje
				// si es "actualizar" y ya existe el territorio deberia poder actualizarlo ...
				
				throw new TipoTerritorioException("Error en ACTUALIZAR tipo territorio: Ya existe un tipo de territorio con ese nombre en este pais :["+
						tipoTerritorio.getTipoTerritorioNombre()+
						"] - pais:["+tipoTerritorio.getTipoTerritorioPais()+"] "+
						"] - ID:["+tipoTerritorio.getIdTipoTerritorio()+"] "+
                        "] - nivel:["+tipoTerritorio.getTipoTerritorioNivel()+"] \n");
			}
			
			// martín: agregué esto aqui ...
		} catch (TipoTerritorioException e) {
			logger.warn("Error en ACTUALIZAR tipo territorio - nombre :["+
						tipoTerritorio.getTipoTerritorioNombre()+
						"] - pais:["+tipoTerritorio.getTipoTerritorioPais()+"] "+
						"] - ID:["+tipoTerritorio.getIdTipoTerritorio()+"] "+
                        "] - nivel:["+tipoTerritorio.getTipoTerritorioNivel()+"] \n");
			e.printStackTrace();
		}
		
		new TipoTerritorioDao().actualizar(tipoTerritorio);
		//aDao.actualizar(tipoTerritorio);
	}

	
	
	public void borrar(TipoTerritorio tipoTerritorio) throws TipoTerritorioException {

		TipoTerritorioDao tDao = new TipoTerritorioDao();
		
		//martin - 20100715 - si existe tipos de niveles inferiores no se borra
		
		String idPais = tipoTerritorio.getTipoTerritorioPais().getIdPais();
		TipoTerritorio tMinimo = tDao.obtenerTipoTerritorioUltimoNivel(idPais);
		
		String nombre = tipoTerritorio.getTipoTerritorioNombre();
		
		//Solo se borra si es el nivel minimo en la jerarquia
		if (tMinimo.getTipoTerritorioNombre().compareTo(nombre) == 0){
			tDao.borrar(tipoTerritorio);
		} else {
			throw new TipoTerritorioException(
					"No se puede borrar el tipo de territorio :"+tipoTerritorio.getIdTipoTerritorio()+ 
					" - " + tipoTerritorio.getTipoTerritorioNombre() +
					"] - pais:["+tipoTerritorio.getTipoTerritorioPais()+"] "+
					" porque tiene hijos ... ");
		}
	}

	
	public List<TipoTerritorio> obtenerTipoTerritorios() throws TipoTerritorioException {

		TipoTerritorioDao tDao = new TipoTerritorioDao();
		return tDao.obtenerTipoTerritorios();

	}

	/*
	public List<Object[]> consTiposTratamientoParaCombo() throws TipoTerritorioException {

		TipoTerritorioDao tDao = new TipoTerritorioDao();
		return tDao.obtenerTipoTerritorioPorPK(i);

	}
	*/

	public List<TipoTerritorio> obtenerTipoTerritorioPorNombre(String nombre) throws TipoTerritorioException {

		TipoTerritorioDao tDao = new TipoTerritorioDao();
		return tDao.obtenerTipoTerritorioPorNombre(nombre);

	}
	
	
	/*
	 * Funcion que devuelve un tipo de terreno de un pais en particular
	 * con un nombre determinado 
	 */
	public TipoTerritorio obtenerTipoTerritorioPorNombreYPais(String nombre, String idPais) throws TipoTerritorioException {

		TipoTerritorioDao tDao = new TipoTerritorioDao();

		// nes 20101002 - como agregamos id pais en null en los datos hay q controlar ...
		for (TipoTerritorio tt: tDao.obtenerTipoTerritorioPorNombre(nombre)) {

			if (tt.getTipoTerritorioPais()!=null &&
					tt.getTipoTerritorioPais()
						.getIdPais()
						.equals(idPais )) { 
				return tt;
			}
		}
		
		return null;

	}
	

	public TipoTerritorio obtenerTipoTerritorioPorPK(Integer pk) throws TipoTerritorioException {
		TipoTerritorioDao tDao = new TipoTerritorioDao();
		return tDao.obtenerTipoTerritorioPorPK(pk);
	}
	
	
	
	public TipoTerritorio obtenerTipoTerritorioHijo( TipoTerritorio tipoTerritorio) throws TipoTerritorioException {

		// Un territorio solo puede tener un territorio de grado inferior
		Pais pais = tipoTerritorio.getTipoTerritorioPais();
		String idPais = pais.getIdPais();
		
		
		return new TipoTerritorioDao().obtenerTipoTerritorioPaisNivel( idPais, 
				tipoTerritorio.getTipoTerritorioNivel()+1);

	}

	public TipoTerritorio obtenerTipoTerritorioHijo( Integer idTipoTerritorio ) throws TipoTerritorioException {
		
		TipoTerritorioDao tDao = new TipoTerritorioDao();
		TipoTerritorio tipo = tDao.obtenerTipoTerritorioPorPK(idTipoTerritorio);
		
		return this.obtenerTipoTerritorioHijo(tipo);

	}
	
	public TipoTerritorio obtenerTipoTerritorioPadre( TipoTerritorio tipoTerritorio) throws TipoTerritorioException {

		// Un territorio solo puede tener un territorio de grado inferior
		if (tipoTerritorio.getTipoTerritorioNivel() > 0){
			return new TipoTerritorioDao().obtenerTipoTerritorioPaisNivel(
					tipoTerritorio.getTipoTerritorioPais().getIdPais(), 
					tipoTerritorio.getTipoTerritorioNivel()-1);
		} else {
			return null;
		}
	
	}
	
	
	public TipoTerritorio obtenerTipoTerritorioPadre( Integer idTipoTerritorio ) throws TipoTerritorioException {
		
		TipoTerritorioDao tDao = new TipoTerritorioDao();
		TipoTerritorio tipo = tDao.obtenerTipoTerritorioPorPK(idTipoTerritorio);
		
		return this.obtenerTipoTerritorioPadre(tipo);

	}
	
	/*
	 * Funcion para conocer el tipo de nivel mas alto en un pais
	 */
	public TipoTerritorio obtenerTipoTerritorioNivelMaximo( String idPais ) throws TipoTerritorioException {
		
		TipoTerritorioDao tDao = new TipoTerritorioDao();
		TipoTerritorio tipo = tDao.obtenerTipoTerritorioPaisNivel(idPais, 0);
		
		return tipo;

	}


	/*
	 * Funcion para conocer el nivel más bajo definido para un pais determinado
	 */
	public Integer obtenerNuevoNivelMinimo( String idPais) throws TipoTerritorioException {
		
		TipoTerritorio t = new TipoTerritorioDao().obtenerTipoTerritorioUltimoNivel(idPais);
		
		// martin - 20100715- Atrapar el null e incrementar el nivel
		if (t != null){
			return t.getTipoTerritorioNivel() + 1;
		} else {
			return 0;
		}
			 
	}

	
	/*
	 * Funcion que devuelve los distintos tipos de territorios 
	 * correspondientes aun pais, ordenados de mas generales a mas especificos 
	 */
	public List<TipoTerritorio> obtenerJerarquiaTerritorialPais(String idPais) throws TipoTerritorioException {
		 
		TipoTerritorioDao tDao = new TipoTerritorioDao();
		List<TipoTerritorio> listaTipos = new ArrayList<TipoTerritorio>() ;
		
		listaTipos = tDao.obtenerJerarquiaTerritorialPais(idPais);
		
		// nes 20100928
		if (listaTipos==null || listaTipos.size()==0) {
			listaTipos = tDao.obtenerJerarquiaTerritorialPais(null);	
		}
		
		return listaTipos;
		 
	}
	
	
	/*
	 * Funcion que devuelve los distintos tipos de territorios 
	 * correspondientes aun pais, ordenados de mas generales a mas especificos 
	 */
	public List<Object[]> obtenerJerarquiaTerritorialPaisParaCombo(String idPais) throws TipoTerritorioException {
		 
		TipoTerritorioDao tDao = new TipoTerritorioDao();
		List<Object[]> listaTipos=null;
		
		listaTipos = tDao.obtenerJerarquiaTerritorialPaisParaCombo(idPais);
		
		
		// nes 20100928
		if (listaTipos==null || listaTipos.size()==0) {
			listaTipos = tDao.obtenerJerarquiaTerritorialPaisParaCombo(ID_PAIS_GENERICO);	
		}
			
		return listaTipos;
		 
	}

}
