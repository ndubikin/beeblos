package org.beeblos.security.auxiliar.dao;
import static org.beeblos.security.st.util.Constantes.CODIGO_PAIS_DEFECTO;
import static org.beeblos.security.st.util.Constantes.NOMBRE_PAIS_DEFECTO;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.security.auxiliar.error.PaisException;
import org.beeblos.security.auxiliar.model.Pais;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sp.common.util.HibernateUtil;
import com.sp.common.util.StringPair;



public class PaisDao {

	private static final Log logger = LogFactory.getLog(PaisDao.class);
	
	public PaisDao (){
		
	}
	
	public void agregar(Pais pais) throws PaisException {
		
		try {
			// El id de Pais es String
			HibernateUtil.save(pais);
			
			logger.info("agregar() Pais: [nombre: "+ pais.getPaisNombre()+"]");

		} catch (HibernateException ex) {

			throw new PaisException(ex);

		}

	}
	
	
	public void actualizar(Pais pais) throws PaisException {
		
			
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		
		try {

			//HibernateUtil.update;
			
			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			// mrico - 20110118
			session.update(pais);
			
			tx.commit();

			logger.info("actualizar() Pais: [id: "+ pais.getIdPais()+"]");
						
		} catch (HibernateException ex) {

			throw new PaisException(ex);

		}
					
	}
	
	
	public void borrar(Pais pais) throws PaisException {

		try {

			pais = obtenerPaisPorPK(pais.getIdPais());

			HibernateUtil.delete(pais);
			
			logger.info("borrar() Pais: [id: "+ pais.getIdPais()+"]");

		} catch (HibernateException ex) {

			throw new PaisException(ex);

		} catch (PaisException e) {

			throw new PaisException(e);

		}

	}

	public Pais obtenerPaisPorPK(String pk) throws PaisException {

		Pais pais = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			pais = (Pais) session.get(Pais.class, pk);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new PaisException("No se pudo obtener el Pais: " + pk + " - "
					+ ex.getMessage());

		}

		return pais;
	}
	
	
	public Pais obtenerPaisPorNombre(String paisNombre) throws PaisException {

		Pais pais = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			pais = (Pais) session.createCriteria(Pais.class).add(
					Restrictions.naturalId().set("paisNombre", paisNombre))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new PaisException("No se pudo obtener la Pais: " + paisNombre
					+ " - " + ex.getMessage());

		}

		return pais;
	}
	
	
	
	public List<Pais> obtenerPaises() throws PaisException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Pais> paises = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			paises = session.createQuery("From Pais pa order by pa.paisNombre ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new PaisException("Error al recuperar lista de paises: "
					+ ex.getMessage());

		}

		return paises;
	}

	@SuppressWarnings("unchecked")
	public List<Pais> obtenerPaises(Integer idPais) throws PaisException {
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Pais> paises = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			paises = session.createQuery("From Pais pa where pa.idPais = ? order by idPais").setInteger(0, idPais).list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new PaisException("Error al recuperar lista de paises: "
					+ ex.getMessage());

		}

		return paises;
	}
	
//	@SuppressWarnings("unchecked")
//	public List<StringPair> obtenerPaisesParaCombo() throws PaisException {
//
//		List<Object[]> ltmp = null;
//		List<StringPair> retorno = new ArrayList<StringPair>(250);
//		String idPais="", paisNombre="";
//		
//		org.hibernate.Session session = null;
//		org.hibernate.Transaction tx = null;
//
//		try {
//
//			session = HibernateUtil.obtenerSession();
//			tx = session.getTransaction();
//			tx.begin();
//
//			ltmp = session
//					.createQuery(
//							"select p.idPais,p.paisNombre from Pais p order by p.paisNombre")
//					.list();
//			
//			tx.commit();

//			// nes 20100821
//			if (ltmp!=null) {
//				retorno.add(new StringPair(null,"- TODOS -"));
//				retorno.add(new StringPair("ES","ESPAÑA"));
//				retorno.add(new StringPair(null," "));
//				for (Object p: ltmp) {
//					Object [] cols= (Object []) p;
//					paisNombre=(cols[1]!=null?cols[1].toString():"");
//					if (cols[0]!=null) {
//						retorno.add( new StringPair(cols[0].toString(),paisNombre));
//					}
//				}
//			} else {
//				// nes  - si el select devuelve null entonces devuelvo null
//				retorno=null;
//			}
//			
//			
//		} catch (HibernateException ex) {
//			if (tx != null)
//				tx.rollback();
//			throw new PaisException(
//					"Ocurrio un error la descripcion de los Paises");
//		} catch (Exception e) {}
//
//		return retorno;
//
//	}

	
	// nes 20100915
	@SuppressWarnings("unchecked")
	public List<StringPair> obtenerPaisesParaCombo(
			String textoPrimeraLinea, String separacion ) 
	throws PaisException {

		List<Pais> ltmp = null;
		List<StringPair> retorno = new ArrayList<StringPair>(250);
		String idPais="", paisNombre="";
			
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			ltmp = session
							.createQuery(
									"From Pais order by paisNombre")
							.list();
	
			tx.commit();

			// nes 20100821
			if (ltmp!=null) {
				
				// inserta los extras
				if ( textoPrimeraLinea!=null && !"".equals(textoPrimeraLinea) ) {
					if ( !textoPrimeraLinea.equals("BLANCO") ) {
						retorno.add(new StringPair(null,textoPrimeraLinea));  // deja la primera línea con lo q venga
					} else {
						retorno.add(new StringPair(null," ")); // deja la primera línea en blanco ...
					}
				}

				retorno.add(new StringPair(CODIGO_PAIS_DEFECTO,NOMBRE_PAIS_DEFECTO));				
				
				if ( separacion!=null && !"".equals(separacion) ) {
					if ( !separacion.equals("BLANCO") ) {
						retorno.add(new StringPair(null,separacion));  // deja la separación línea con lo q venga
					} else {
						retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
					}
				}
				
			
				
				for (Pais p: ltmp) {
					retorno.add(new StringPair(p.getIdPais(),p.getPaisNombre()));
				}
			} else {
				// nes  - si el select devuelve null entonces devuelvo null
				retorno=null;
			}
			
			
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			throw new PaisException(
					"Ocurrio un PaisException al intentar obtener la lista de Paises para combo "+ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new PaisException(
					"Ocurrio un Exception al intentar obtener la lista de Paises para combo "+e.getMessage());
		}

		return retorno;
	}
	
	
	
	//mrico - 20110118
	public List<Pais> finderPais(
			String filtroId, String filtroNombre
	) throws PaisException {

		String filtro="";
		
		if (filtroId!=null && ! "".equals(filtroId)){
			filtro+=" elem.id_pais LIKE '%"+filtroId.trim()+"%' ";
		} else 	{ 
			filtro+="";
		}

		if (filtroNombre!=null && ! "".equals(filtroNombre)){
			if (!"".equals(filtro)) {
				filtro+="AND elem.pais_nombre LIKE '%"+filtroNombre.trim()+"%' ";
			} else {
				filtro+="elem.pais_nombre LIKE '%"+filtroNombre.trim()+"%' ";
			}
		} else 	{ 
			filtro+="";
		}
	
		if (filtro!=null && !"".equals(filtro)) {
			filtro= " WHERE "+filtro;
		}

		System.out.println("002 ------>> finderPais -> filtro:"+filtro+"<<-------");
		
		logger.debug("002 ------>> finderPais -> filtro:"+filtro+"<<-------");
		
		String query = _armaQuery(filtro);

		return finderPais(query);

	}
	

	
	//mrico - 20110118
	public List<Pais> finderPais(
			String query
	) throws PaisException {
		
		
		Session session = null;
		Transaction tx = null;
		
		List<Object[]> resultado = null;
		List<Pais> retorno = new ArrayList<Pais>();
		List<Pais> lpr = new ArrayList<Pais>();
				
					
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			Hibernate.initialize(resultado); 
			
			resultado = session
					.createSQLQuery(query)
					.list();

			tx.commit();
			
			if (resultado!=null) {
				for (Object irObj: resultado) {
					
					Object [] cols= (Object []) irObj;
	
				String idPaisValue = (cols[0]!=null ? cols[0].toString():"");
				String paisNombreValue = (cols[1]!=null ? cols[1].toString():"");
				String paisComentarioValue = (cols[2]!=null ? cols[2].toString():"");
						
				retorno.add( new Pais(
					idPaisValue 
					, paisNombreValue 
					, paisComentarioValue 
					));
				}
			} else {
				// Si el select devuelve null entonces devuelvo null
				retorno=null;
			}
		
		
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("PaisDAO: finderPais() - No pudo recuperarse la lista de proyectos almacenadas - "+
					ex.getMessage()+ "\n"+ex.getLocalizedMessage()+" \n"+ex.getCause());
			throw new PaisException(ex);
		
		} catch (Exception e) {
			logger.warn("Exception PaisDAO: finderPais() - error en el manejo de la lista de proyectos almacenadas - "+
					e.getMessage()+ "\n"+e.getLocalizedMessage()+" \n"+e.getCause());
			e.getStackTrace();
			throw new PaisException(e);
		}

		return retorno;
		
	}
	
	private String _armaQuery(String filtro){
		
		String tmpQuery = "SELECT ";
		
			tmpQuery +=" elem.id_pais";
			tmpQuery +=",  elem.pais_nombre";
			tmpQuery +=",  elem.pais_comentario";

			tmpQuery += " FROM pais elem "; //TODO: Poner nombre
			tmpQuery += filtro;
			tmpQuery += " ORDER by elem.id_pais, elem.pais_nombre ASC;";
		
		System.out.println("------>> finderPais -> query:"+tmpQuery+"<<-------");
		
		logger.debug("query:"+tmpQuery);
		
		return tmpQuery;
	}
	
	
	
	
}