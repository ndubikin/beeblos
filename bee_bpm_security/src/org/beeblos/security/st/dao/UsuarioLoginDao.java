package org.beeblos.security.st.dao;

// Generado 16-dic-2010 9:15:03 con Hibernate Tools 3.3.0.GA



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sp.common.core.util.HibernateUtil;
import org.beeblos.security.st.error.UsuarioLoginException;
import org.beeblos.security.st.model.UsuarioLogin;
import org.beeblos.security.st.model.UsuarioLoginAmpliado;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

/**
 * Dao object for domain model class UsuarioLogin.
 * @see org.beeblos.security.st.model.bdc.model.UsuarioLogin
 */

public class UsuarioLoginDao {

    private static final Log logger = LogFactory.getLog(UsuarioLoginDao.class);

	
    
    public UsuarioLoginDao  (){
		
	}   
       
    public Integer agregar(UsuarioLogin instancia) throws UsuarioLoginException{
        logger.debug("agregar() UsuarioLogin para usuario: [id: "+instancia.getIdUsuario()+"]" );
        try {
            return Integer.valueOf(HibernateUtil.save(instancia));
        }
        catch (HibernateException ex) {
            logger.error("UsuarioLoginDao: agregar - No se pudo guardar UsuarioLogin para usuario: [id: "+instancia.getIdUsuario()+"]");
            throw new UsuarioLoginException(ex);
        }
    }
    
    public void actualizar(UsuarioLogin instancia) throws UsuarioLoginException {
		
		logger.debug("actualizar() UsuarioLogin < id = "+instancia.getId()+">");
		
		try {

			HibernateUtil.update(instancia);

		} catch (HibernateException ex) {
			logger.error("UsuarioLoginDao:actualizar - No se pudo actualizar UsuarioLogin de IdUsuario"+ instancia.getIdUsuario() +
					" - id = "+instancia.getId()+"\n - "+ex.getMessage()   );
			throw new UsuarioLoginException(ex);

		}
					
	}
	
	
	public void borrar(UsuarioLogin instancia) throws UsuarioLoginException {

		logger.debug("borrar() UsuarioLoginNombre: ["+instancia.getId()+"]");
		
		try {

			instancia = obtenerUsuarioLoginPorPK(instancia.getId());

			HibernateUtil.delete(instancia);

		} catch (HibernateException ex) {
			logger.error("UsuarioLoginDao: borrar - No se pudo borrar la instancia de UsuarioLogin para IdUsuario"+ instancia.getIdUsuario() +
					" <id = "+instancia.getId()+ "> \n"+" - "+ex.getMessage() );
			throw new UsuarioLoginException(ex);

		} catch (UsuarioLoginException e) {
			logger.error("UsuarioLoginDao: borrar - La instancia de UsuarioLogin para IdUsuario"+ instancia.getIdUsuario() +
					" <id = "+ instancia.getId() + "> no esta almacenada \n"+" - "+e.getMessage() );
			throw new UsuarioLoginException(e);

		} 

	}
    
    public UsuarioLogin obtenerUsuarioLoginPorPK(Integer i) throws UsuarioLoginException {

		UsuarioLogin instancia = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			instancia = (UsuarioLogin) session.get(UsuarioLogin.class, i);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("UsuarioLoginDao: obtenerUsuarioLoginPorPK - No hay UsuarioLogin con id = "+ i + "]  almacenado - \n"+ex.getMessage() );
			throw new UsuarioLoginException("No se pudo obtener el UsuarioLogin: " + i + " - "
					+ ex.getMessage());

		}

		return instancia;
	}
	
	
	public List<UsuarioLogin> obtenerUsuarioLoginsPorIdUsuario(Integer idUsuario) throws UsuarioLoginException {

		List<UsuarioLogin>  instancias = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instancias = session.createQuery("From UsuarioLogin where idUsuario =? order by id")
						.setInteger(0, idUsuario)
						.list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("UsuarioLoginDao: obtenerUsuarioLoginPorNombre -No se pudo obtener UsuarioLogin por idUsuario: " + idUsuario
					+ " - " + ex.getMessage());
			throw new UsuarioLoginException("No se pudo obtener UsuarioLogin por idUsuario: " + idUsuario
					+ " - " + ex.getMessage());

		}

		return instancias;
	}
    
    public List<UsuarioLogin> obtenerListaUsuarioLogin() throws UsuarioLoginException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<UsuarioLogin> instancias = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			instancias = session.createQuery("From UsuarioLogin order by id").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("UsuarioLoginDao: obtenerListaUsuarioLogin() - No pudo recuperarse la lista de instancias almacenadas - "+ex.getMessage() );
			
			throw new UsuarioLoginException("Error al recuperar lista UsuarioLogin: "
					+ ex.getMessage());

		}

		return instancias;
	}
    
    //mrico 20110629
    public List<UsuarioLoginAmpliado> finderUsuarioLogin(
    		String filtroNombreUsuarioLogin, 
    		String filtroApellidosUsuario,
    		Date fechaIngresoInicial, 
    		Date fechaIngresoFinal, 
    		String aplicacion 
	) throws UsuarioLoginException {
		
				String estado="";
		String filtro="";

		
		filtro=" ul.login_exitoso = true ";
		
		if (filtroNombreUsuarioLogin!=null && !"".equals(filtroNombreUsuarioLogin)){
			if (!"".equals(filtro)) {
				filtro+=" AND u.usuario_login LIKE '%"+filtroNombreUsuarioLogin+"%' ";
			} else {
				filtro+=" u.usuario_login LIKE '%"+filtroNombreUsuarioLogin+"%' ";
			}
		}
		
		if (filtroApellidosUsuario!=null && !"".equals(filtroApellidosUsuario)){
			if (!"".equals(filtro)) {
				filtro+=" AND u.apellidos LIKE '%"+filtroApellidosUsuario+"%' ";
			} else {
				filtro+=" u.apellidos LIKE '%"+filtroApellidosUsuario+"%' ";
			}
		}
		
		if (aplicacion!=null && !"".equals(aplicacion)){
			if (!"".equals(filtro)) {
				filtro+=" AND ul.origen LIKE '"+aplicacion+"' ";
			} else {
				filtro+=" ul.origen LIKE '"+aplicacion+"' ";
			}
		}
		
				
		if (fechaIngresoInicial!=null){
					
			java.sql.Date fechaIngresoInicialSQL=new java.sql.Date(fechaIngresoInicial.getTime());
			
			if (!"".equals(filtro)) {
				filtro+=" AND ul.fecha >= '"+fechaIngresoInicialSQL+"' ";
			} else {
				filtro+=" ul.fecha >= '"+fechaIngresoInicialSQL+"' ";
			}
		}
		
		if (fechaIngresoFinal!=null){
			
			java.sql.Date fechaIngresoFinalSQL=new java.sql.Date(fechaIngresoFinal.getTime());
			
			if (!"".equals(filtro)) {
				filtro+=" AND ul.fecha < '"+fechaIngresoFinalSQL+"' ";
			} else {
				filtro+=" ul.fecha < '"+fechaIngresoFinalSQL+"' ";
			}
		}	
    			
				
		if (filtro!=null && !"".equals(filtro)) {
			filtro= " WHERE "+filtro;
		}

		logger.debug("002 ------>> finderUsuarioLogin -> filtro:"+filtro+"<<-------");
		
		String query = _armaQuery(filtro);
		
		System.out.println(query);

		return finderUsuarioLogin(query);

	}
    
    
    public List<UsuarioLoginAmpliado> finderUsuarioLogin(
			String query
	) throws UsuarioLoginException {
		
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		
		List<Object[]> resultado = null;
		List<UsuarioLoginAmpliado> retorno = new ArrayList<UsuarioLoginAmpliado>();
		
		Integer idUsuarioLogin;
		String usuarioLogin, login_ip, usuarioNombre, usuarioApellidos;
		Date fecha = new Date();
		Boolean login_exitoso;
		
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			Hibernate.initialize(resultado); // nes 20100821
			
			resultado = session
					.createSQLQuery(query)
					.list();

			tx.commit();
			
			if (resultado!=null) {
				for (Object irObj: resultado) {
					
					Object [] cols= (Object []) irObj;
					
					idUsuarioLogin 	= (cols[0]!=null ? new Integer(cols[0].toString()):null); 
					usuarioLogin	= (cols[1]!=null ? cols[1].toString():"");
					usuarioNombre	= (cols[2]!=null ? cols[2].toString():"");
					usuarioApellidos= (cols[3]!=null ? cols[3].toString():"");
					login_exitoso 	= (cols[4]!=null ? (Boolean)cols[4]:null);
					login_ip		= (cols[5]!=null ? cols[5].toString():"");
					fecha			= (cols[6]!=null ? (Date)cols[6]:null);
					
				
					
					retorno.add( new UsuarioLoginAmpliado(idUsuarioLogin, usuarioLogin, 
							usuarioNombre, usuarioApellidos,
							login_exitoso, login_ip, fecha));
				
				}
			} else {
				// nes  - si el select devuelve null entonces devuelvo null
				retorno=null;
			}
		
		
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("ProyectoDAO: finderUsuarioLogin() - No pudo recuperarse la lista de proyectos almacenadas - "+
					ex.getMessage()+ "\n"+ex.getLocalizedMessage()+" \n"+ex.getCause());
			throw new UsuarioLoginException(ex);
		
		} catch (Exception e) {
			logger.warn("Exception ProyectoDAO: finderUsuarioLogin() - error en el manejo de la lista de proyectos almacenadas - "+
					e.getMessage()+ "\n"+e.getLocalizedMessage()+" \n"+e.getCause());
			e.getStackTrace();
			throw new UsuarioLoginException(e);
		}

		return retorno;
		
	}
    

	private String _armaQuery(String filtro){
		
		String tmpQuery = "SELECT u.id_usuario, u.usuario_login, " ;
		tmpQuery += " u.nombres, u.apellidos, ";
		tmpQuery += " ul.login_exitoso, ul.login_ip, ";
		tmpQuery += " ul.fecha ";
		tmpQuery += " FROM usuario_login ul ";
		tmpQuery += " LEFT OUTER JOIN usuario u ON u.id_usuario = ul.id_usuario ";
		tmpQuery += filtro;
		tmpQuery += " ORDER by ul.fecha ASC;";
		
		logger.debug("------>> finderUsuarioLogin -> query:"+tmpQuery+"<<-------");
	
		return tmpQuery;
	}
    
    
	
}