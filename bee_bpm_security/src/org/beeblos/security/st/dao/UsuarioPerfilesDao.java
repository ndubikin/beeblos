package org.beeblos.security.st.dao;

import java.util.List;

import org.beeblos.security.st.error.UsuarioPerfilesException;
import org.beeblos.security.st.model.UsuarioPerfiles;
import org.hibernate.Hibernate;

import com.sp.common.util.HibernateUtil;



public class UsuarioPerfilesDao {

	public UsuarioPerfiles obtenerPerfiles(
			Integer idUsuario,
			Integer idAplicacion
			
		) throws UsuarioPerfilesException {
		
			String query;
			query = " select id_perfil " +
					" from bbdd_seguridad.rperfil_usuario " +
					" where id_usuario= " + idUsuario;
			if ( idAplicacion!=null ) {
				query +=" and id_aplicacion=" + idAplicacion;
			}
			
			System.out.println(query);
		
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;
					
			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();
	
			List<Integer> resultado = null;
			
			Hibernate.initialize(resultado); 
			
			resultado = session
							.createSQLQuery(query)
							.list();
	
			tx.commit();
			
					
			UsuarioPerfiles perfiles = new UsuarioPerfiles();
			perfiles.setListaPerfiles(resultado);
			
			return perfiles;
			
	}	





}
