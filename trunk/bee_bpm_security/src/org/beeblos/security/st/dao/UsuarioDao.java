package org.beeblos.security.st.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.beeblos.security.st.error.UsuarioException;
import org.beeblos.security.st.model.Usuario;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


public class UsuarioDao {
	
	  private static final Log logger = LogFactory.getLog(UsuarioDao.class);

	public void actualizar(Usuario usuario) throws UsuarioException {

		try {

			HibernateUtil.actualizar(usuario);
			
			logger.info("actualizar() Usuario < id = "+usuario.getIdUsuario()+">");

		} catch (HibernateException ex) {

			throw new UsuarioException(ex);

		}
	}

	// nes 20080109
	public void actualizarPwd(Usuario usuario) throws UsuarioException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		Usuario u = new Usuario();
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			u = (Usuario) session.get(Usuario.class, usuario.getIdUsuario());

			u.setPassword(usuario.getPassword());

			session.update(u);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new UsuarioException("Error al cambiar el pasword del usuario "
					+ ex.getMessage());

		}
	}

	public void agregar(Usuario usuario) throws UsuarioException {

		try {

			HibernateUtil.guardar(usuario);
			
			logger.info("agregar() Usuario < nombre = "+usuario.getUsuarioLogin()+">");

		} catch (HibernateException ex) {

			throw new UsuarioException(ex);

		}

	}

	public Usuario autenticarUsuario(Usuario usuario) throws UsuarioException {

		String usuarioLogin = usuario.getUsuarioLogin();

		Usuario retorno = null;

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			retorno = (Usuario) session.createCriteria(Usuario.class).add(
					Restrictions.naturalId().set("usuarioLogin", usuarioLogin))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {

			throw new UsuarioException(
					"No se encontro el usuario digitado en el sistema: "
							+ ex.getMessage());

		}

		if (retorno != null) {
			if (!retorno.getPassword().equals(usuario.getPassword())) {

				retorno = null;
				throw new UsuarioException("Error: password incorrecto");

			}
		}

		return retorno;

	}

	public void borrar(Usuario usuario) throws UsuarioException {

		try {

			usuario = obtenerUsuarioPorPK(usuario.getIdUsuario());

			HibernateUtil.borrar(usuario);
			
			logger.info("borrar() Usuario < id = "+usuario.getIdUsuario()+">");

		} catch (HibernateException ex) {

			throw new UsuarioException(ex);

		} catch (UsuarioException ex1) {

			throw new UsuarioException(ex1);

		}

	}

	public Usuario obtenerUsuarioPorNombre(String nombres) throws UsuarioException {

		Usuario usuario = null;

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			usuario = (Usuario) session.createCriteria(Usuario.class).add(
					Restrictions.naturalId().set("nombres", nombres))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new UsuarioException("No se pudo obtener el Usuario: " + nombres
					+ " - " + ex.getMessage());

		}

		return usuario;
	}

	public Usuario obtenerUsuarioLoginPorNombre(String usuarioLogin)
			throws UsuarioException {

		Usuario retorno = null;

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			retorno = (Usuario) session.createCriteria(Usuario.class).add(
					Restrictions.naturalId().set("usuarioLogin", usuarioLogin))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new UsuarioException("No se pudo obtener el Login: " + retorno
					+ " - " + ex.getMessage());
		}

		return retorno;
	}

	public Usuario obtenerUsuarioPorPK(Integer pk) throws UsuarioException {

		Usuario usuario = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			usuario = (Usuario) session.get(Usuario.class, pk);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new UsuarioException("No se pudo obtener el Usuario: " + pk
					+ " - " + ex.getMessage());

		}

		return usuario;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> obtenerUsuarios() throws UsuarioException {
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Usuario> usuarios = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			usuarios = session.createQuery("From Usuario order by idUsuario")
					.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new UsuarioException("Error al recuperar lista de Usuarios: "
					+ ex.getMessage());

		}

		return usuarios;
	}
	
	public List<StringPair> obtenerUsuariosParaCombo(
			String textoPrimeraLinea, String separacion )
	throws UsuarioException {
		 
			List<Usuario> ltmp = null;
			List<StringPair> retorno = new ArrayList<StringPair>();

			ltmp=obtenerUsuarios();

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
							
				
				for (Usuario u: ltmp) {
					retorno.add(new StringPair(u.getIdUsuario(),u.getUsuarioLogin()));
				}
				
			} else {

				retorno=null;
			
			}
				
				
			return retorno;

	}
	
	public boolean verificarNombreDuplicado(
			String usuarioLogin, Integer idUsuario) 
	throws UsuarioException {

		Usuario instancia = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instancia = (Usuario) session
							.createQuery("From Usuario WHERE  usuarioLogin= ? AND idUsuario != ?")
								.setString(0,  usuarioLogin)
								.setInteger(1,	idUsuario)
								.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("UsuarioDao: verificaNombreRepetido - No se pudo recuperar un Usuario "+ usuarioLogin +" - "+ex.getMessage()  );
			throw new UsuarioException("No se pudo obtener el Usuario: " + usuarioLogin
					+ " - " + ex.getMessage() + " ; " + ex.getCause());

		}
		if (instancia!=null) return true;
		else return false;
	}
	
	
	public List<Usuario> finderUsuario(
			String filtroNombre, String filtroApellidos, String filtroLogin
	) throws UsuarioException {

		String filtro="";
		
		if (filtroNombre!=null && ! "".equals(filtroNombre)){
			filtro+="  elem.NOMBRES LIKE '%"+filtroNombre.trim()+"%' ";
		} else 	{ 
			filtro+="";
		}

		if (filtroApellidos!=null && ! "".equals(filtroApellidos)){
			if (!"".equals(filtro)) {
				filtro+="AND elem.APELLIDOS LIKE '%"+filtroApellidos.trim()+"%' ";
			} else {
				filtro+="elem.APELLIDOS LIKE '%"+filtroApellidos.trim()+"%' ";
			}
		} else 	{ 
			filtro+="";
		}
		
		if (filtroLogin!=null && ! "".equals(filtroLogin)){
			if (!"".equals(filtro)) {
				filtro+="AND elem.USUARIO_LOGIN LIKE '%"+filtroLogin.trim()+"%' ";
			} else {
				filtro+="elem.USUARIO_LOGIN LIKE '%"+filtroLogin.trim()+"%' ";
			}
		} else 	{ 
			filtro+="";
		}
	
		if (filtro!=null && !"".equals(filtro)) {
			filtro= " WHERE "+filtro;
		}

		System.out.println("002 ------>> finderUsuario -> filtro:"+filtro+"<<-------");
		
		logger.debug("002 ------>> finderUsuario -> filtro:"+filtro+"<<-------");
		
		String query = _armaQuery(filtro);

		return finderUsuario(query);

	}
	

	

	public List<Usuario> finderUsuario(
			String query
	) throws UsuarioException {
				
		Session session = null;
		Transaction tx = null;
		
		List<Object[]> resultado = null;
		List<Usuario> retorno = new ArrayList<Usuario>();
						
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
	
				Integer idUsuarioValue =  (cols[0]!=null ? new Integer(cols[0].toString()):null);
				String nombresValue = (cols[1]!=null ? cols[1].toString():"");
				String apellidosValue = (cols[2]!=null ? cols[2].toString():"");
				String emailValue = (cols[3]!=null ? cols[3].toString():"");
				String usuarioLoginValue = (cols[4]!=null ? cols[4].toString():"");
				String passwordValue = (cols[5]!=null ? cols[5].toString():"");
				String admonValue = (cols[6]!=null ? cols[6].toString():"");
				String suLecturaValue = (cols[7]!=null ? cols[7].toString():"");
				Integer idDeptoValue =  (cols[8]!=null ? new Integer(cols[8].toString()):null);
						
				retorno.add( new Usuario(
					idUsuarioValue 
					, nombresValue 
					, apellidosValue 
					, emailValue 
					, usuarioLoginValue 
					, passwordValue 
					, admonValue 
					, suLecturaValue 
					, idDeptoValue 
					));
				}
			} else {
				// Si el select devuelve null entonces devuelvo null
				retorno=null;
			}
		
		
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("UsuarioDAO: finderUsuario() - No pudo recuperarse la lista almacenada de Usuario - "+
					ex.getMessage()+ "\n"+ex.getLocalizedMessage()+" \n"+ex.getCause());
			throw new UsuarioException(ex);
		
		} catch (Exception e) {
			logger.warn("Exception UsuarioDAO: finderUsuario() - error en el manejo de la lista almacenada de Usuario - "+
					e.getMessage()+ "\n"+e.getLocalizedMessage()+" \n"+e.getCause());
			e.getStackTrace();
			throw new UsuarioException(e);
		}

		return retorno;
		
	}
	
	private String _armaQuery(String filtro){
		
		String tmpQuery = "SELECT ";
		
			tmpQuery +=" elem.ID_USUARIO";
			tmpQuery +=",  elem.NOMBRES";
			tmpQuery +=",  elem.APELLIDOS";
			tmpQuery +=",  elem.EMAIL";
			tmpQuery +=",  elem.USUARIO_LOGIN";
			tmpQuery +=",  elem.PASSWORD";
			tmpQuery +=",  elem.ADMON";
			tmpQuery +=",  elem.SU_LECTURA";
			tmpQuery +=",  elem.ID_DEPTO";

			tmpQuery += " FROM usuario elem "; 
			tmpQuery += filtro;
			tmpQuery += " ORDER by elem.NOMBRES, elem.APELLIDOS ASC;";
		
		System.out.println("------>> finderUsuario -> query:"+tmpQuery+"<<-------");
		
		logger.debug("query:"+tmpQuery);
		
		return tmpQuery;
	}
}



