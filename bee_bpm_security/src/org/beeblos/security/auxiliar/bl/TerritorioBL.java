package org.beeblos.security.auxiliar.bl;

import java.util.ArrayList;
import java.util.List;

import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.security.auxiliar.dao.TerritorioDao;
import org.beeblos.security.auxiliar.dao.TipoTerritorioDao;
import org.beeblos.security.auxiliar.error.TerritorioException;
import org.beeblos.security.auxiliar.error.TipoTerritorioException;
import org.beeblos.security.auxiliar.model.Territorio;
import org.beeblos.security.auxiliar.model.TipoTerritorio;


public class TerritorioBL {

	private Integer NIVEL_0 = 0; 
	
	public TerritorioBL() {

	}

	public Integer agregar(Territorio territorio) throws TerritorioException {

		// Un territorio debe pertencer a un pais
		if (territorio.getTerritorioPais() == null){
			throw new TerritorioException("El territorio debe pertenecer a un pais");
		}
	

		try {
			
			// si no trae el nivel se lo pongo a partir del padre ...
			if (territorio.getTipoTerritorio()==null || 
				territorio.getTipoTerritorio().getIdTipoTerritorio()==0) {
			
				Integer nivel = NIVEL_0; // 0 es el primer nivel, el mas alto en la jerarquía de un país
				
				if (territorio.getTerritorioPadre() != null){ // si tiene un padre tocará el nivel siguiente al padre
					
					nivel = territorio.getTerritorioPadre().getTipoTerritorio().getTipoTerritorioNivel()+1;
				
				} 
				
				// Aqui se puede estar asignado null: luego hay que comprobarlo
				territorio.setTipoTerritorio(
						new TipoTerritorioDao()
							.obtenerTipoTerritorioPaisNivel(
									territorio.getTerritorioPais().getIdPais(), nivel));
				
				
			}
		} catch (TipoTerritorioException e) {
			throw new TerritorioException("agregar;Error al recuperar al tipo de territorio para asociar el nivel al nuevo territorio:["
					+territorio.getTerritorioNombre()+"]");
		}
			
	
		
		
		// el nivel del territorio ingresado y el del padre (si lo tiene) deben diferir en 1
		// si no tiene padre entonces el nivel debe ser 0
		// no c si vale la pena hacer este control ....
		
		if (territorio.getTerritorioPadre()==null) {
			// al no tener padre, si el nivel es mayor que cero entonces es una contradicción ...
			if (territorio.getTipoTerritorio().getTipoTerritorioNivel()!=0) {
				throw new TerritorioException("Al no depender de nadie el tipo de territorio debe ser de nivel 0");
			}
		
		// martin - 20100715 - Re-comprobacion	
		}else if (territorio.getTipoTerritorio()==null) {	
			// Si en este punto no tiene asignado tipo de territorio, es que es no exite el tipo 
			throw new TerritorioException("El territorio "+ territorio.getTerritorioNombre() + " no pertenece a ningun tipo de territorio valido");
			
		} else {
			
			// los niveles del del padre y el del nuevo registro solo pueden diferir en 1 
			if ((territorio.getTipoTerritorio().getTipoTerritorioNivel()-
					territorio.getTerritorioPadre().getTipoTerritorio().getTipoTerritorioNivel()) !=1) {
				throw new TerritorioException("Los niveles del padre y del territorio que va a insertar deberían se contiguos y no lo son: "+
						"\n nivel del padre:"+territorio.getTerritorioPadre().getTipoTerritorio().getTipoTerritorioNivel()+
						"\n nivel del que intenta agregar:"+territorio.getTipoTerritorio().getTipoTerritorioNivel()+
						"\n Asegúrese de agregar niveles contiguos ...");
			}
				
		}
			
		try {
			if (this.obtenerTerritorioPorNombreYTipo( 
					territorio.getTerritorioNombre(),
					territorio.getTipoTerritorio() ) != null){
				
				throw new TerritorioException("Ya existe un tipo de territorio con ese nombre y ese nivel en este pais");
			}
		} catch (TerritorioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		TerritorioDao tDao = new TerritorioDao();
		return tDao.agregar(territorio);

	}

	public void actualizar(Territorio territorio) throws TerritorioException {
		new TerritorioDao().actualizar(territorio);
		//aDao.actualizar(Territorio);
	}


	public void borrar(Territorio territorio) throws TerritorioException {

		TerritorioDao tDao = new TerritorioDao();
		if (!tDao.tieneHijos(territorio)) {
				tDao.borrar(territorio);
		} else {
			throw new TerritorioException(
					"No se puede borrar el territorio :"+territorio.getIdTerritorio()+ 
					" - " + territorio.getTerritorioNombre()
					+" porque tiene hijos ... ");
		}

	}

	public List<Territorio> obtenerTerritorios() throws TerritorioException {

		TerritorioDao tDao = new TerritorioDao();
		return tDao.obtenerTerritorios();

	}

	/*
	public List<Object[]> consterritoriosTratamientoParaCombo() throws TerritorioException {

		TerritorioDao tDao = new TerritorioDao();
		return tDao.obtenerTerritorioPorPK(i);

	}
	*/

	public List<Territorio> obtenerTerritorioPorNombre(String nombre) throws TerritorioException {

		TerritorioDao tDao = new TerritorioDao();
		return tDao.obtenerTerritorioPorNombre(nombre);

	}
	
	
	public Territorio obtenerTerritorioPorNombreYTipo(String nombre, TipoTerritorio tipo) throws TerritorioException {
		
		TerritorioDao tDao = new TerritorioDao();
		List<Territorio> listaTerritorios = tDao.obtenerTerritorioPorNombre(nombre);

		for (Territorio t: listaTerritorios){
			if (t.getTipoTerritorio().getTipoTerritorioNombre() == tipo.getTipoTerritorioNombre()){
				return t;
			}
		}
		return null;

	}
	
	public List<Territorio> obtenerTerritorioPorPaisYTipo(String idPais, TipoTerritorio tipo) throws TerritorioException {
		
		TerritorioDao tDao = new TerritorioDao();
		if(idPais != null && !idPais.equals("") &&  tipo!=null){
			return tDao.obtenerTerritoriosPorPaisYTipo(idPais,tipo.getIdTipoTerritorio());
		} else { 
			return null;
		}

	}
	

	public Territorio obtenerTerritorioPorPK(Integer pk) throws TerritorioException {
		TerritorioDao tDao = new TerritorioDao();
		return tDao.obtenerTerritorioPorPK(pk);
	}
	
	

	public List<Territorio> obtenerTerritoriosSupremos(String idPais) throws TerritorioException {
		

		try {
			return new TerritorioDao()
						.obtenerTerritoriosPorPaisYTipo(idPais,new TipoTerritorioDao().obtenerTipoTerritorioPaisNivel(idPais, NIVEL_0).getIdTipoTerritorio());
		} catch (TipoTerritorioException e) {
			throw new TerritorioException("No se pudo obtener el Tipo de Territorio supremo para idPais:["+
					idPais+"] \n"+
					e.getMessage());
		}
	}
	
	
	public List<Object[]> obtenerTerritoriosSupremosParaCombo(Integer idTerritorio) throws TerritorioException {
		
		TerritorioDao tDao = new TerritorioDao();
		Territorio territorio = tDao.obtenerTerritorioPorPK(idTerritorio);
		
		return tDao.obtenerTerritoriosSupremosParaCombo(territorio);
		
	}
	

	public List<Territorio> obtenerTerritoriosHijos(Territorio territorio) throws TerritorioException {
		
		TerritorioDao tDao = new TerritorioDao();
		
		List<Territorio> listaTerritorios = tDao.obtenerTerritoriosHijos(territorio);
		return listaTerritorios;

	}
	
	public List<StringPair> obtenerTerritoriosHijosParaCombo(Integer idTerritorio) throws TerritorioException {
		
		TerritorioDao tDao = new TerritorioDao();
		Territorio territorio = tDao.obtenerTerritorioPorPK(idTerritorio);
		
		return tDao.obtenerTerritoriosHijosParaCombo(territorio);
		
	}

	// nes 20110103
	public List<StringPair> obtenerListaTerritoriosParaCombo(
		String idPais, Integer idTipoTerritorio
	) throws TerritorioException {
		
		TerritorioDao tDao = new TerritorioDao();
		
		List<StringPair> lt = tDao.obtenerListaTerritoriosParaCombo(idPais, idTipoTerritorio);
		return lt;
		
	}
	
	public List<Territorio> obtenerTerritoriosPorPais(String idPais) throws TerritorioException {
		 
		TerritorioDao tDao = new TerritorioDao();
		
		List<Territorio> listaTerritorios = tDao.obtenerTerritoriosPorPais(idPais);
		return listaTerritorios;
		 
	}
	
	
	// martin - 20100715 
	/*
	 * Funcion que devuelve una lista con los territorios de sucesivos niveles
	 * partiendo del territorio m�s especifico (de nivel n a 0)
	 */
	
	public List<Territorio> obtenerTerritoriosDireccion(Territorio territorio) throws TerritorioException {
		
		List<Territorio> listaTerritoriosDireccion = new ArrayList<Territorio>();
		listaTerritoriosDireccion.add(0, territorio);
		
		Territorio padre = territorio.getTerritorioPadre();
		Territorio territorioActual;
		
		while (padre != null){
			listaTerritoriosDireccion.add(0, padre);
			
			territorioActual = padre;
			padre = territorioActual.getTerritorioPadre();
			
			if (listaTerritoriosDireccion.size() > 9){
				throw new TerritorioException("La jerarquia territorial de " + territorio.getTerritorioNombre() + " es demasiado grande");
			}
		}
		
		return listaTerritoriosDireccion;
	
	}
		
	// martin - 20100715 
	
	public List<Territorio> obtenerTerritoriosDireccion(Integer idTerritorio) throws TerritorioException {
		
		TerritorioDao tDao = new TerritorioDao();
		Territorio territorio = tDao.obtenerTerritorioPorPK(idTerritorio);
		
		return this.obtenerTerritoriosDireccion(territorio);
		
	}

	// martin - 20100716 
	// Igual que el anterior pero solo devuelve id y nombre
	public List<Object[]> obtenerTerritoriosDireccionParaCombo(Territorio territorio) throws TerritorioException {
		
		List<Object[]> listaTerritoriosDireccion = new ArrayList<Object[]>();
		
		String tipo = territorio.getTipoTerritorio().getTipoTerritorioNombre();
		
		Object[] item = {territorio.getIdTerritorio(),territorio.getTerritorioNombre(), tipo};
		listaTerritoriosDireccion.add(0, item);
				
		Territorio padre = territorio.getTerritorioPadre();
		Territorio territorioActual;
			
		while (padre != null){
			
			tipo = padre.getTipoTerritorio().getTipoTerritorioNombre();
			
			Object[] itemPadre = {padre.getIdTerritorio(),padre.getTerritorioNombre(), tipo};
			listaTerritoriosDireccion.add(0, itemPadre);
			
			territorioActual = padre;			
			padre = territorioActual.getTerritorioPadre();
			
			if (listaTerritoriosDireccion.size() > 9){
				throw new TerritorioException("La jerarquia territorial de " + territorio.getTerritorioNombre() + " es demasiado grande");
			}
		}
		
		return listaTerritoriosDireccion;
	
	}
		
	public List<Object[]> obtenerTerritoriosDireccionParaCombo(Integer idTerritorio) throws TerritorioException {
		
		TerritorioDao tDao = new TerritorioDao();
		Territorio territorio = tDao.obtenerTerritorioPorPK(idTerritorio);
		
		return this.obtenerTerritoriosDireccionParaCombo(territorio);
		
	}
	
	
	public List<Territorio> obtenerLocalizacion(
			Integer idTerritorio ) 
	throws TerritorioException {
		
		return new TerritorioDao().obtenerLocalizacion(idTerritorio);
	
	}
		
}
