package org.beeblos.security.st.dao;

// Generado 25-nov-2010 12:52:05 con Hibernate Tools 3.3.0.GA


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.beeblos.security.st.error.UsuarioCuentasEmailException;
import org.beeblos.security.st.model.UsuarioCuentasEmail;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;


/**
 * Dao object for domain model class UsuarioCuentasEmail.
 * @see org.beeblos.security.st.model.bdc.model.UsuarioCuentasEmail
 */

public class UsuarioCuentasEmailDao {

    private static final Log logger = LogFactory.getLog(UsuarioCuentasEmailDao.class);

	
    
    public UsuarioCuentasEmailDao  (){
		
	}   
       
    public Integer agregar(UsuarioCuentasEmail instancia) throws UsuarioCuentasEmailException{
        logger.debug("agregar() uceNombre: ["+instancia.getUceNombre()+"]" );
        logger.info("agregar() uceNombre: ["+instancia.getUceNombre()+"]" );
        try {
            return Integer.valueOf(HibernateUtil.guardar(instancia));
        }
        catch (HibernateException ex) {
            logger.error("UsuarioCuentasEmailDao: agregar - No se pudo guardar UsuarioCuentasEmail "+ instancia.getUceNombre());
            throw new UsuarioCuentasEmailException(ex);
        }
    }
    
    public void actualizar(UsuarioCuentasEmail instancia) throws UsuarioCuentasEmailException {
		
		logger.debug("actualizar() UsuarioCuentasEmail < id = "+instancia.getIdUce()+">");
		
		try {

			HibernateUtil.actualizar(instancia);
			
			logger.info("actualizar() UsuarioCuentasEmail < id = "+instancia.getIdUce()+">");

		} catch (HibernateException ex) {
			logger.error("UsuarioCuentasEmailDao:actualizar - No se pudo actualizar UsuarioCuentasEmail "+ instancia.getUceNombre()  +
					" - id = "+instancia.getIdUce()+"\n - "+ex.getMessage()   );
			throw new UsuarioCuentasEmailException(ex);

		}
					
	}
	
	
	public void borrar(UsuarioCuentasEmail instancia) throws UsuarioCuentasEmailException {

		logger.debug("borrar() uceNombre: ["+instancia.getUceNombre()+"]");
		
		try {

			instancia = obtenerUsuarioCuentasEmailPorPK(instancia.getIdUce());

			HibernateUtil.borrar(instancia);
			
			logger.info("borrar() UsuarioCuentasEmail < id = "+instancia.getIdUce()+">");

		} catch (HibernateException ex) {
			logger.error("UsuarioCuentasEmailDao: borrar - No se pudo borrar la instancia "+ instancia.getUceNombre() +
					" <id = "+instancia.getIdUce()+ "> \n"+" - "+ex.getMessage() );
			throw new UsuarioCuentasEmailException(ex);

		} catch (UsuarioCuentasEmailException ex1) {
			logger.error("UsuarioCuentasEmailDao: borrar - La instancia "+ instancia.getUceNombre() +
					" <id = "+instancia.getIdUce()+ "> no esta almacenada \n"+" - "+ex1.getMessage() );
			throw new UsuarioCuentasEmailException(ex1);

		} 

	}
    
    public UsuarioCuentasEmail obtenerUsuarioCuentasEmailPorPK(Integer i) throws UsuarioCuentasEmailException {

		UsuarioCuentasEmail instancia = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			instancia = (UsuarioCuentasEmail) session.get(UsuarioCuentasEmail.class, i);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("UsuarioCuentasEmailDao: obtenerUsuarioCuentasEmailPorPK - No hay UsuarioCuentasEmail con id = "+ i + "]  almacenado - \n"+ex.getMessage() );
			throw new UsuarioCuentasEmailException("No se pudo obtener el UsuarioCuentasEmail: " + i + " - "
					+ ex.getMessage());

		}

		return instancia;
	}
	
	
	public UsuarioCuentasEmail obtenerUsuarioCuentasEmailPorNombre(String instanciaNombre) throws UsuarioCuentasEmailException {

		UsuarioCuentasEmail  instancia = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instancia = (UsuarioCuentasEmail) session.createCriteria(UsuarioCuentasEmail.class).add(
					Restrictions.naturalId().set("uceNombre", instanciaNombre))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("UsuarioCuentasEmailDao: obtenerUsuarioCuentasEmailPorNombre -No se pudo obtener UsuarioCuentasEmail por nombre: " + instanciaNombre
					+ " - " + ex.getMessage());
			throw new UsuarioCuentasEmailException("No se pudo obtener UsuarioCuentasEmail por nombre: " + instanciaNombre
					+ " - " + ex.getMessage());

		}

		return instancia;
	}
    
    public List<UsuarioCuentasEmail> obtenerListaUsuarioCuentasEmail() throws UsuarioCuentasEmailException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<UsuarioCuentasEmail> instancias = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instancias = session.createQuery("From UsuarioCuentasEmail order by idUce").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("UsuarioCuentasEmailDao: obtenerListaUsuarioCuentasEmail() - No pudo recuperarse la lista de instancias almacenadas - "+ex.getMessage() );
			
			throw new UsuarioCuentasEmailException("Error al recuperar lista UsuarioCuentasEmail: "
					+ ex.getMessage());

		}

		return instancias;
	}
	
	
//	public List<StringPair> obtenerListaUsuarioCuentasEmailParaCombo(
//			String textoPrimeraLinea, String separacion )
//	throws UsuarioCuentasEmailException {
//		 
//			List<UsuarioCuentasEmail> ltmp = null;
//			List<StringPair> retorno = new ArrayList<StringPair>(10);
//			
//			org.hibernate.Session session = null;
//			org.hibernate.Transaction tx = null;
//
//			try {
//
//				session = HibernateUtil.obtenerSession();
//				tx = session.getTransaction();
//				tx.begin();
//
//				ltmp = session
//				.createQuery("From UsuarioCuentasEmail order by uceNombre")
//				.list();
//		
//				if (ltmp!=null) {
//					
//					// inserta los extras
//					if ( textoPrimeraLinea!=null && !"".equals(textoPrimeraLinea) ) {
//						if ( !textoPrimeraLinea.equals("BLANCO") ) {
//							retorno.add(new StringPair(null,textoPrimeraLinea));  // deja la primera línea con lo q venga
//						} else {
//							retorno.add(new StringPair(null," ")); // deja la primera línea en blanco ...
//						}
//					}
//					
//					if ( separacion!=null && !"".equals(separacion) ) {
//						if ( !separacion.equals("BLANCO") ) {
//							retorno.add(new StringPair(null,separacion));  // deja la separación línea con lo q venga
//						} else {
//							retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
//						}
//					}
//					
//				
//					
//					for (UsuarioCuentasEmail ee: ltmp) {
//						retorno.add(new StringPair(ee.getIdUce(),ee.getUceNombre()));
//					}
//				} else {
//					// nes  - si el select devuelve null entonces devuelvo null
//					retorno=null;
//				}
//				
//				
//			} catch (HibernateException ex) {
//				if (tx != null)
//					tx.rollback();
//				throw new UsuarioCuentasEmailException(
//						"Ocurrio un error al intentar obtener la lista de UsuarioCuentasEmail para combo");
//			} catch (Exception e) {}
//
//			return retorno;
//
//
//	}

	@SuppressWarnings("unchecked")
	public List<StringPair> obtenerListaUsuarioCuentasEmailParaCombo(
			Integer idUsuarioEspecifico, String textoPrimeraLinea, String separacion )
	throws UsuarioCuentasEmailException {
		 
			List<UsuarioCuentasEmail> ltmp = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				if (idUsuarioEspecifico!=null) {
					
					ltmp = session
								.createQuery("From UsuarioCuentasEmail uce WHERE uce.idUsuario= ? order by  uce.preferida desc, uce.uceEmail ")
								.setInteger(0,  idUsuarioEspecifico)
								.list();
					
				}
				tx.commit();
		
				if (ltmp!=null) {
					if (! ltmp.get(0).isPreferida() && ltmp.size()>1 ) {
	 					// inserta los extras
						if ( textoPrimeraLinea!=null && !"".equals(textoPrimeraLinea) ) {
							if ( !textoPrimeraLinea.equals("BLANCO") ) {
								retorno.add(new StringPair(null,textoPrimeraLinea));  // deja la primera línea con lo q venga
							} else {
								retorno.add(new StringPair(null," ")); // deja la primera línea en blanco ...
							}
						}
						
						if ( separacion!=null && !"".equals(separacion) ) {
							if ( !separacion.equals("BLANCO") ) {
								retorno.add(new StringPair(null,separacion));  // deja la separación línea con lo q venga
							} else {
								retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
							}
						}
					
					}				
					
//					HZC:20110302,formateando la lista de emails Juan Perez <jperez@miserver.com> 
					for (UsuarioCuentasEmail uce: ltmp) {
						retorno.add(new StringPair(uce.getIdUce(), uce.getUceNombre() + " <" +uce.getUceEmail() + "> "));
					}
										
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				String mensaje="Ocurrio un error al intentar obtener la lista de UsuarioCuentasEmail para combo"+" - "
								+ex.getMessage()+ " ; " + ex.getCause();
				logger.error(mensaje);
				throw new UsuarioCuentasEmailException( mensaje);
			} catch (Exception e) {}

			return retorno;


	}
	
	// martin - 20101214
	@SuppressWarnings("unchecked")
	public List<UsuarioCuentasEmail> obtenerListaUsuarioCuentasEmailPorUsuario(
			Integer idUsuarioEspecifico
				) throws UsuarioCuentasEmailException {
		 
			List<UsuarioCuentasEmail> retorno = null;
			//List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				if (idUsuarioEspecifico!=null) {
					
					retorno = session
								.createQuery("From UsuarioCuentasEmail uce WHERE uce.idUsuario= ? order by  uce.preferida desc, uce.uceEmail ")
								.setInteger(0,  idUsuarioEspecifico)
								.list();
					
				}
				tx.commit();
		
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				String mensaje="Ocurrio un error al intentar obtener la lista de UsuarioCuentasEmail para combo"+" - "
								+ex.getMessage()+ " ; " + ex.getCause();
				logger.error(mensaje);
				throw new UsuarioCuentasEmailException( mensaje);
			} catch (Exception e) {}

			return retorno;


	}
	
	
	public boolean verificarNombreDuplicado(
			String instanciaNombre, Integer idUce) 
	throws UsuarioCuentasEmailException {

		UsuarioCuentasEmail instancia = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instancia = (UsuarioCuentasEmail) session
							.createQuery("From UsuarioCuentasEmail WHERE  uceNombre= ? AND idUce != ?")
								.setString(0,  instanciaNombre)
								.setInteger(1,	idUce)
								.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("UsuarioCuentasEmailDao: verificaNombreRepetido - No se pudo recuperar un UsuarioCuentasEmail "+ instanciaNombre +" - "+ex.getMessage()  );
			throw new UsuarioCuentasEmailException("No se pudo obtener el UsuarioCuentasEmail: " + instanciaNombre
					+ " - " + ex.getMessage() + " ; " + ex.getCause());

		}
		if (instancia!=null) return true;
		else return false;
	} 

	public UsuarioCuentasEmail getCuentaPreferida(Integer idUsuario) 
			throws UsuarioCuentasEmailException {
 
		UsuarioCuentasEmail retorno = null;
		//List<StringPair> retorno = new ArrayList<StringPair>(10);
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
	
		try {
	
			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();
	
			if (idUsuario!=null) {
				
				retorno = (UsuarioCuentasEmail) session
							.createQuery("From UsuarioCuentasEmail uce WHERE uce.idUsuario= ? AND uce.preferida = true order by  uce.preferida desc, uce.uceEmail asc")
							.setInteger(0,  idUsuario)
							.uniqueResult();
				
			}
			tx.commit();
	
			
			
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mensaje="Ocurrio un error al intentar obtener la lista de UsuarioCuentasEmail para combo"+" - "
							+ex.getMessage()+ " ; " + ex.getCause();
			logger.error(mensaje);
			throw new UsuarioCuentasEmailException( mensaje);
		} catch (Exception e) {}
	
		return retorno;

		
	}

	
}



