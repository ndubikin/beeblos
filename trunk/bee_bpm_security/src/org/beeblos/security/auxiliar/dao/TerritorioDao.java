package org.beeblos.security.auxiliar.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.beeblos.security.auxiliar.error.TerritorioException;
import org.beeblos.security.auxiliar.model.Territorio;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

public class TerritorioDao {
	
	private static final Log logger = LogFactory.getLog(TerritorioDao.class);
	
	
	public TerritorioDao (){
		
	}
	
	public Integer agregar(Territorio territorio) throws TerritorioException {

		logger.info("agregar() Territorio: ["+territorio.getTerritorioNombre()+"]" );
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(territorio));

		} catch (HibernateException ex) {

			throw new TerritorioException(ex);

		}

	}
	
	
	public void actualizar(Territorio territorio) throws TerritorioException {
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		
		try {

			//HibernateUtil.actualizar;
			
			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			Territorio ter = (Territorio) session.get(Territorio.class,
					territorio.getIdTerritorio());

			ter.setTerritorioNombre(territorio.getTerritorioNombre());
			ter.setTerritorioCodigo(territorio.getTerritorioCodigo());
			ter.setTerritorioCodigopostal(territorio.getTerritorioCodigopostal());
			ter.setTerritorioComentario(territorio.getTerritorioComentario());
			ter.setTerritorioPadre(territorio.getTerritorioPadre());
			ter.setTerritorioPais(territorio.getTerritorioPais());
			ter.setTipoTerritorio(territorio.getTipoTerritorio());
			
			session.save(ter);
			
			tx.commit();
			
			logger.info("actualizar Territorio < id = "+territorio.getIdTerritorio()+">");

		} catch (HibernateException ex) {

			throw new TerritorioException(ex);

		}
					
	}
	
	
	public void borrar(Territorio territorio) throws TerritorioException {

		try {

			System.out.println("borrando ...:"+territorio.getIdTerritorio());
			
			HibernateUtil.borrar(territorio);
			
			logger.info("borrar Territorio < id = "+territorio.getIdTerritorio()+">");

		} catch (HibernateException ex) {

			throw new TerritorioException(ex);

		} 

	}

	public Territorio obtenerTerritorioPorPK(int i) throws TerritorioException {

		Territorio territorio = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			territorio = (Territorio) session.get(Territorio.class, i);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TerritorioException("No se pudo obtener el Territorio: " + i + " - "
					+ ex.getMessage());

		}

		return territorio;
	}
	
	
	public List<Territorio> obtenerTerritorioPorNombre(String territorioNombre) throws TerritorioException {

		List<Territorio>  territorios = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			territorios = session.createCriteria(Territorio.class).add(
					Restrictions.naturalId().set("territorioNombre", territorioNombre))
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TerritorioException("No se pudo obtener la lista de Territorios por nombre: " + territorioNombre
					+ " - " + ex.getMessage());

		}

		return territorios;
	}
	
	public Territorio obtenerTerritorioPorNombreCorto(String territorioNombreCorto) throws TerritorioException {

		Territorio territorio = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			territorio = (Territorio) session.createCriteria(Territorio.class).add(
					Restrictions.naturalId().set("territorioNombreCorto", territorioNombreCorto))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TerritorioException("No se pudo obtener la Territorio: " + territorioNombreCorto
					+ " - " + ex.getMessage());

		}

		return territorio;
	}
	
	public List<Territorio> obtenerTerritorios() throws TerritorioException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Territorio> territorios = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			territorios = session.createQuery("From Territorio order by idTerritorio").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TerritorioException("Error al recuperar lista de territorios: "
					+ ex.getMessage());

		}

		return territorios;
	}

	public List<Territorio> obtenerTerritoriosPorPaisYTipo(
			String idPais, Integer idTipoTerritorio) 
	throws TerritorioException {
	
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Territorio> lt = new ArrayList<Territorio>();

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			lt = session.createQuery("From Territorio ter where ter.territorioPais.idPais = ? AND ter.tipoTerritorio.idTipoTerritorio = ? order by ter.territorioNombre")
						.setString(0, idPais)
						.setInteger(1, idTipoTerritorio)
						.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TerritorioException("Error al recuperar lista de Territorios por idPais: "+
					idPais+"] - "+
					" para el idTipoTerritorio: ["+
					idTipoTerritorio+"] \n" +
					ex.getMessage());

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			throw new TerritorioException("Exception - Error al recuperar lista de Territorios por idPais: "+
					idPais+"] - "+
					" para el idTipoTerritorio: ["+
					idTipoTerritorio+"] \n" +
					ex.getMessage());
		}

		return lt;
	}
	
	// nota: devuelve lista de territorios de un país ordenados por nombre
	public List<Territorio> obtenerTerritoriosPorPais(String idPais) throws TerritorioException {
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Territorio> territorios = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			territorios = session.createQuery("From Territorio ter where ter.territorioPais.idPais = ? order by ter.territorioNombre")
								 .setString(0, idPais)
								 .list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TerritorioException("Error al recuperar lista de Territorios por pais: ["+
					idPais+"] \n"+
					ex.getMessage());

		}

		return territorios;
	}

	public List<Territorio> obtenerTerritoriosHijos(Territorio territorio) throws TerritorioException {
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Territorio> listaTerritorio = null;
		Integer idTerritorio = territorio.getIdTerritorio();
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			listaTerritorio = session.createQuery("From Territorio t where t.territorioPadre = ? order by territorioNombre")
									 .setInteger(0, idTerritorio)
									 .list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TerritorioException("Error al recuperar lista de Territorios hijos para el territorio : ["+
					idTerritorio+"] \n"+
					ex.getMessage());

		}

		return listaTerritorio;
	}
	
	// Igual pero para manejar combos
	@SuppressWarnings("unchecked")
	public List<StringPair> obtenerTerritoriosHijosParaCombo(Territorio territorio) throws TerritorioException {
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Territorio> ltmp = null;
		List<StringPair> retorno = new ArrayList<StringPair>();
		Integer idTerritorio = territorio.getIdTerritorio();
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			ltmp = session.createQuery("From Territorio t where t.territorioPadre.idTerritorio = ? order by territorioNombre")
									 .setInteger(0, idTerritorio)
									 .list();

			tx.commit();

			retorno.add(new StringPair(null,"Seleccionar ..."));
			for ( Territorio t: ltmp) {
				retorno.add(new StringPair(t.getIdTerritorio(),t.getTerritorioNombre()));
			}
			
			
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TerritorioException("Error al recuperar lista de Territorios hijos para el territorio : ["+
					idTerritorio+"] \n"+
					ex.getMessage());

		}

		return retorno;
	}
	
	
	// nes 20110103
	@SuppressWarnings("unchecked")
	public List<StringPair> obtenerListaTerritoriosParaCombo(
				// String idPais, Integer idTerritorioTipo - martin: ojo tenias al reves el idTipoTerritorio
			String idPais, Integer idTipoTerritorio
		) throws TerritorioException {

		List<Territorio> ltmp = null;
		List<StringPair> retorno = new ArrayList<StringPair>();
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();
			
			//martin - 20100716 - Modificacion Query
			
			ltmp = session
							.createQuery("From Territorio ter where ter.territorioPais.idPais = ? AND ter.tipoTerritorio.idTipoTerritorio = ? order by ter.territorioNombre")
							.setString(0, idPais)
							.setInteger(1, idTipoTerritorio)
							.list();

			retorno.add(new StringPair(null,"Seleccionar ..."));			
			for ( Territorio t: ltmp) {
				retorno.add(new StringPair(t.getIdTerritorio(),t.getTerritorioNombre()));
			}
			
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TerritorioException(
					"Error al intentar recuperar la lista de territorios PARA COMBO tipo:"
					+idTipoTerritorio+" para el país: "+idPais +" : "+ ex.getMessage()+" - "+ex.getCause());
		}

		return retorno;

	}


	public boolean tieneHijos(Territorio t) throws TerritorioException {
		
		return tieneHijos(t.getIdTerritorio());
		
	}
	
	public boolean tieneHijos(Integer idTerritorio) throws TerritorioException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		Integer result = 0;
	
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			result = session.createQuery("From Territorio t where t.territorioPadre = ?").setInteger(0, idTerritorio).list().size();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TerritorioException("Error al recuperar lista de Territorios en el método tieneHijos para el idTerritorio: ["+
					idTerritorio+"] \n"+
					ex.getMessage());

		}
		
		if (result>0) return true;
		else return false;
	}

	public List<Object[]> obtenerTerritoriosSupremosParaCombo(Territorio territorio) throws TerritorioException {

		List<Object[]> retorno = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();
					
			//martin - 20100716 - Modificacion Query
			
			retorno = session.createQuery("Select ter.idTerritorio, ter.territorioNombre From Territorio ter where ter.territorioPadre.idTerritorio = ?  order by ter.territorioNombre")
				.setInteger(0, territorio.getIdTerritorio())
				.list();
					
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TerritorioException(
				"Error al intentar recuperar la lista de territorios hijos");
		}

		return retorno;

	}
	
	
	// nes 20100928 - a partir del id de menor nivel de 1 territorio, devuelve
	// la lista con la localización completa, en orden por nivel de territorio
	public List<Territorio> obtenerLocalizacion(
			Integer idTerritorio ) 
	throws TerritorioException {
		
		if (idTerritorio==null || idTerritorio==0) {
			logger.debug("Se intenta recupera localizacion con idTerritorio inválido (0 ó null)");
			return null;
		}

		List<Territorio> retorno = new ArrayList<Territorio>();
		Territorio t = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();
			t = (Territorio) session.createQuery("From Territorio terr where terr.idTerritorio=? ")
			.setInteger(0, idTerritorio)
			.uniqueResult();	
			
			if (t!=null) {
				retorno.add(t);
				
				while (t.getTerritorioPadre()!=null) {
					t=t.getTerritorioPadre();
					retorno.add(t);
				}
				Collections.reverse(retorno);
			}
			 
					
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();

			
			logger.warn("Error al intentar recuperar la localización territorial para idTerritorio:"+
					idTerritorio+" \n"+ ex.getMessage());
			
			throw new TerritorioException(
				"Error al intentar recuperar la localización territorial para idTerritorio:"+
				idTerritorio+" \n"+ ex.getMessage());
		}

		return retorno;

	}
	
}
	
