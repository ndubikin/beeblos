/**
 * 
 */
package org.beeblos.security.st.dao;

/**
 * @author rr
 *
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sp.common.util.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.beeblos.security.st.error.DepartamentoException;
import org.beeblos.security.st.model.Departamento;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

public class DepartamentoDao {

	private static final Log logger = LogFactory.getLog(DepartamentoDao.class);
	
	public DepartamentoDao() {

	}

	public Integer agregar(Departamento departamento) throws DepartamentoException {
		
		logger.info("agregar() Departamento < nombre = "+departamento.getDepartamentoNombre()+">");
		
		try {
			// martin - 20100714 - A�adido retorno id
			return Integer.parseInt(HibernateUtil.save(departamento));

		} catch (HibernateException ex) {

			throw new DepartamentoException(ex);

		}

	}

	public void actualizar(Departamento departamento) throws DepartamentoException {
				
		//martin - 20101221
		// Corregido el uso del HibernateUtil
		try {
	
			HibernateUtil.update(departamento);
			
			logger.info("actualizar() Departamento < id = "+departamento.getIdDepartamento()+">");

		} catch (HibernateException ex) {

			throw new DepartamentoException(ex);

		}
	}

	public void borrar(Departamento d) throws DepartamentoException {

		
		//martin - 20101221
		// Corregido el uso del HibernateUtil
		try {
			d = this.obtenerDepartamentoPorPK(d.getIdDepartamento());
			HibernateUtil.delete(d);
			
			logger.info("borrar() Departamento < id = "+d.getIdDepartamento()+">");

		} catch (HibernateException ex) {

			throw new DepartamentoException(ex);

		} 

	}

	public Departamento obtenerDepartamentoPorPK(
			Integer pk ) 
	throws DepartamentoException {

		Departamento d = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			d = (Departamento) session.get(Departamento.class, pk);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new DepartamentoException("No se pudo obtener el Departamento: " + pk + " - "
					+ ex.getMessage());

		}

		return d;
	}

	@SuppressWarnings("unchecked")
	public Departamento obtenerDepartamentoPorNombre(String departamentoNombre) throws DepartamentoException {

		Departamento d = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			d = (Departamento) session.createCriteria(Departamento.class).add(
					Restrictions.naturalId().set("departamentoNombre", departamentoNombre))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new DepartamentoException("No se pudo obtener el Departamento: " + departamentoNombre
					+ " - " + ex.getMessage());

		}

		return d;
	}

	@SuppressWarnings("unchecked")
	public List<Departamento> obtenerDepartamentos() throws DepartamentoException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Departamento> departamentos = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			departamentos = session.createQuery("From Departamento order by idDepartamento").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new DepartamentoException("Error al recuperar lista de departamentos: "
					+ ex.getMessage());

		}

		return departamentos;
	}
	
	@SuppressWarnings("unchecked")
	public List<Departamento> obtenerDepartamentos(Integer idDepartamento) throws DepartamentoException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Departamento> departamentos = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			departamentos = session.createQuery("From Departamento ar where ar.idDepartamento = ? order by idDepartamento").setInteger(0, idDepartamento).list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new DepartamentoException("Error al recuperar lista de departamentos: "
					+ ex.getMessage());

		}

		return departamentos;
	}

	public List<Departamento> obtenerDepartamentosOrdenados (
			String order )
	throws DepartamentoException {
		 

		if (order==null || "".equals(order)) {
			order=" departamentoNombre ";
		} else {
			if (!order.equals("departamentoNombre") && !order.equals("departamentoAbreviatura") ) {
				order=" departamentoNombre ";
			}
		}

			List<Departamento> ltmp = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				ltmp = session
				.createQuery("From Departamento order by "+order)
				.list();
	
				tx.commit();

			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new DepartamentoException(
						"Ocurrio un error al intentar obtener la lista de Departamentos para combo"+ex.getMessage());
			} catch (Exception e) {}

			return ltmp;


	}
	
	public List<StringPair> obtenerDepartamentosAbreviadosParaCombo(
			String textoPrimeraLinea, String separacion )
	throws DepartamentoException {
		 
			List<Departamento> ltmp = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);

			ltmp=obtenerDepartamentosOrdenados("departamentoAbreviatura");

			if (ltmp!=null) {
				
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
				
			
				
				for (Departamento d: ltmp) {
					retorno.add(new StringPair( d.getIdDepartamento(),d.getDepartamentoAbreviatura()));
				}
				
			} else {

				retorno=null;
			
			}
				
				
			return retorno;

	}

	public List<StringPair> obtenerDepartamentosNombreParaCombo(
			String textoPrimeraLinea, String separacion )
	throws DepartamentoException {
		 


			List<Departamento> ltmp = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);

			ltmp=obtenerDepartamentosOrdenados("departamentoNombre");

			if (ltmp!=null) {
				
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
				
			
				
				for (Departamento d: ltmp) {
					retorno.add(new StringPair((int) d.getIdDepartamento(),d.getDepartamentoNombre()));
				}
				
			} else {

				retorno=null;
			
			}
				
				
			return retorno;

	}
	

	//rrl 20111216
	public List<StringPair> obtenerDepartamentosNombreYAbreviaturaParaCombo(
			String textoPrimeraLinea, String separacion )
	throws DepartamentoException {

		List<Departamento> ltmp = null;
		List<StringPair> retorno = new ArrayList<StringPair>(10);

		ltmp=obtenerDepartamentosOrdenados("departamentoNombre");

		if (ltmp!=null) {
			
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
			
			for (Departamento d: ltmp) {
				retorno.add(new StringPair(d.getIdDepartamento(), d.getDepartamentoNombre() + " (" + d.getDepartamentoAbreviatura() + ")" ));
			}
			
		} else {

			retorno=null;
		
		}
			
		return retorno;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<Departamento> obtenerDepartamentosPorNombre() throws DepartamentoException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Departamento> departamentos = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			departamentos = session.createQuery("From Departamento order by departamentoNombre").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new DepartamentoException("Error al recuperar lista de departamentos: "
					+ ex.getMessage());

		}

		return departamentos;
	}
	

}

