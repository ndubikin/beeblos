package org.beeblos.security.auxiliar.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.beeblos.security.auxiliar.error.TipoTerritorioException;
import org.beeblos.security.auxiliar.model.TipoTerritorio;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;



public class TipoTerritorioDao {
	
	private static final Log logger = LogFactory.getLog(TipoTerritorioDao.class);

	public TipoTerritorioDao (){
		
	}
	
	public int agregar(TipoTerritorio tipoTerritorio) throws TipoTerritorioException {
		
		logger.info("agregar() TipoTerritorio: ["+tipoTerritorio.getTipoTerritorioNombre()+"]");

		try {

			return Integer.valueOf(HibernateUtil.guardar(tipoTerritorio));

		} catch (HibernateException ex) {

			throw new TipoTerritorioException(ex);

		}

	}
	
	
	public void actualizar(TipoTerritorio tipoTerritorio) throws TipoTerritorioException {
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		
		try {

			//HibernateUtil.actualizar;
			
			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			TipoTerritorio tt = (TipoTerritorio) session.get(TipoTerritorio.class,
					tipoTerritorio.getIdTipoTerritorio());

			tt.setTipoTerritorioNombre(tipoTerritorio.getTipoTerritorioNombre());
			tt.setTipoTerritorioComentario(tipoTerritorio.getTipoTerritorioComentario());
			tt.setTipoTerritorioNivel(tipoTerritorio.getTipoTerritorioNivel());
			tt.setTipoTerritorioPais(tipoTerritorio.getTipoTerritorioPais());
			
			session.save(tt);
			
			tx.commit();
			
			logger.info("actualizar() TipoTerritorio < id = "+tipoTerritorio.getIdTipoTerritorio()+">");

		} catch (HibernateException ex) {

			throw new TipoTerritorioException(ex);

		}
					
	}
	
	
	public void borrar(TipoTerritorio tipoTerritorio) throws TipoTerritorioException {

		try {

			tipoTerritorio = obtenerTipoTerritorioPorPK(tipoTerritorio.getIdTipoTerritorio());

			HibernateUtil.borrar(tipoTerritorio);
			
			logger.info("borrar() TipoTerritorio < id = "+tipoTerritorio.getIdTipoTerritorio()+">");

		} catch (HibernateException ex) {

			throw new TipoTerritorioException(ex);

		} catch (TipoTerritorioException ex1) {

			throw new TipoTerritorioException(ex1);

		}

	}

	public TipoTerritorio obtenerTipoTerritorioPorPK(int i) throws TipoTerritorioException {

		TipoTerritorio tipoTerritorio = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			tipoTerritorio = (TipoTerritorio) session.get(TipoTerritorio.class, i);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TipoTerritorioException("No se pudo obtener el TipoTerritorio: " + i + " - "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return tipoTerritorio;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TipoTerritorio> obtenerTipoTerritorioPorNombre(String tipoTerritorioPorNombre) throws TipoTerritorioException {

		List<TipoTerritorio> tiposTerritorio = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			tiposTerritorio = session.createCriteria(TipoTerritorio.class).add(
					Restrictions.naturalId().set("tipoTerritorioNombre", tipoTerritorioPorNombre))
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TipoTerritorioException("No se pudo obtener la TipoTerritorio: " + tipoTerritorioPorNombre
					+ " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return tiposTerritorio;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TipoTerritorio> obtenerTipoTerritorios() throws TipoTerritorioException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<TipoTerritorio> tiposTerritorio = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			tiposTerritorio = session.createQuery("From TipoTerritorio order by idTipoTerritorio").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TipoTerritorioException("Error al recuperar lista de tipoTerritorioes: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return tiposTerritorio;
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoTerritorio> obtenerJerarquiaTerritorialPais(String idPais) throws TipoTerritorioException {
	
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<TipoTerritorio> TiposTerritorio = null;
		
		String query;
		if (idPais!=null) {
			query = "From TipoTerritorio where tipoTerritorioPais.idPais = ? order by tipoTerritorioNivel";
		} else {
			query = "From TipoTerritorio where tipoTerritorioPais.idPais IS NULL order by tipoTerritorioNivel";
		}

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			if (idPais!=null){
				TiposTerritorio = session.createQuery(query).setString(0, idPais).list();
			} else {
				TiposTerritorio = session.createQuery(query).list();
			}

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TipoTerritorioException("Error al recuperar la jerarquia territorial para : "+idPais+" \n"
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return TiposTerritorio;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerJerarquiaTerritorialPaisParaCombo(String idPais) throws TipoTerritorioException{

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Object[]> TiposTerritorio = null;
		
		// nes 20101002
		String query;
		if (idPais!=null) {
			query = "Select idTipoTerritorio, tipoTerritorioNombre From TipoTerritorio where tipoTerritorioPais.idPais = ? order by tipoTerritorioNivel";
		} else {
			query = "Select idTipoTerritorio, tipoTerritorioNombre From TipoTerritorio where tipoTerritorioPais.idPais IS NULL order by tipoTerritorioNivel";
		}

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			// nes 20101002
			if (idPais!=null) {
				TiposTerritorio = session.createQuery(query)
										 .setString(0, idPais)
										 .list(); 
			}
			else {
				TiposTerritorio = session.createQuery(query).list(); 
			}

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TipoTerritorioException("Error al recuperar lista de Tipos de Territorio: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return TiposTerritorio;
	}
	
	
	public TipoTerritorio obtenerTipoTerritorioPaisNivel(String idPais, Integer nivel) throws TipoTerritorioException{
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		TipoTerritorio tipoTerritorio = null;
		
		// nes 20101002
		String query;
		if (idPais!=null) {
			query = "From TipoTerritorio tt where tt.tipoTerritorioPais.idPais = ? and tt.tipoTerritorioNivel = ? order by tipoTerritorioNombre";
		} else {
			query = "From TipoTerritorio tt where tt.tipoTerritorioPais.idPais IS NULL and tt.tipoTerritorioNivel = ? order by tipoTerritorioNombre";
		}
			
			
			
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			// nes 20101002
			if (idPais!=null) {
				tipoTerritorio = (TipoTerritorio) session
									.createQuery(query)
									.setString(0, idPais)
									.setInteger(1, nivel)
									.uniqueResult(); }
			else {
				tipoTerritorio = (TipoTerritorio) session
										.createQuery(query)
										.setInteger(1, nivel)
										.uniqueResult();
			}
		
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TipoTerritorioException("Error al recuperar Tipo de territorio inferior : " // martin: corregido texto del error ...
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return tipoTerritorio;
	}
	
	// nes - query para sacar el ultimo tipo de territorio existente para un país
	public TipoTerritorio obtenerTipoTerritorioUltimoNivel(String idPais) throws TipoTerritorioException {
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		TipoTerritorio tipoTerritorio = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();
			
			// nes 20101002
			if (idPais!=null) {
				tipoTerritorio = 
					(TipoTerritorio) session
						.createQuery("From TipoTerritorio tt where tipoTerritorioPais = ? and tipoTerritorioNivel=(Select max(tipoTerritorioNivel) from TipoTerritorio where tipoTerritorioPais = ?)")
							.setString(0, idPais)
							.setString(1, idPais)
							 	.uniqueResult();

			} else {
				tipoTerritorio = 
					(TipoTerritorio) session
						.createQuery("From TipoTerritorio tt where tipoTerritorioPais IS NULL and tipoTerritorioNivel=(Select max(tipoTerritorioNivel) from TipoTerritorio where tipoTerritorioPais IS NULL)")
						 	.uniqueResult();
				
			}
			
			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new TipoTerritorioException("Error al recuperar Tipo de territorio inferior : " // martin: corregido texto del error ...
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return tipoTerritorio;
	}

	
}
