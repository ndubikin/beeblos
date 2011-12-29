package org.beeblos.security.st.dao;

import java.util.ArrayList;
import java.util.List;

import org.beeblos.security.st.error.UsuarioFuncionesException;
import org.beeblos.security.st.model.UsuarioFunciones;
import org.beeblos.security.st.util.HibernateUtil;
import org.hibernate.Hibernate;



public class UsuarioFuncionesDao {

	public UsuarioFunciones obtenerFunciones(
			Integer idUsuario,
			Integer idAplicacion
			
		) throws UsuarioFuncionesException {
		
			String query;
			query = "select distinct id_funcion "; 
			query+= " from bbdd_seguridad.rusuario_aplicacionfuncion ";
			query+= " where id_aplicacion=" + idAplicacion;
			query+= " and otorgado='s' ";
			query+= " and id_usuario= " + idUsuario;
			query+= " UNION SELECT distinct id_funcion ";
			query+= " FROM bbdd_seguridad.rperfil_aplicacionfuncion ";
			query+= " where id_aplicacion= " + idAplicacion;
			query+= " and otorgado='s' ";
			query+= " and id_perfil in ";
			query+= " (select id_perfil from bbdd_seguridad.rperfil_usuario ";
			query+= " where id_usuario=" + idUsuario;
			query+= " and id_aplicacion=" + idAplicacion;
			query+= " ) ";
			query+= " order by id_funcion asc ";
			
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
			
			ArrayList<Integer> res = new ArrayList<Integer>();
			
//			if (resultado!=null) {
//				
//				for (Object irObj: resultado) {
//									
//					Object [] cols= (Object []) irObj;
//					res.add(new Integer(cols[0].toString()));
//				}
//			}
			
			UsuarioFunciones funciones = new UsuarioFunciones();
			funciones.setListaFunciones(resultado);
			
			return funciones;
			
	}	

	
	public UsuarioFunciones obtenerFuncionesGeneral(
			Integer idUsuario
					
		) throws UsuarioFuncionesException {
		
			String query;
			query = "select distinct id_funcion "; 
			query+= " from bbdd_seguridad.rusuario_aplicacionfuncion ";
			query+= " where otorgado='s' ";
			query+= " and id_usuario= " + idUsuario;
			query+= " UNION SELECT distinct id_funcion ";
			query+= " FROM bbdd_seguridad.rperfil_aplicacionfuncion ";
			query+= " where otorgado='s' ";
			query+= " and id_perfil in ";
			query+= " (select id_perfil from bbdd_seguridad.rperfil_usuario ";
			query+= " where id_usuario=" + idUsuario;
			query+= " ) ";
			query+= " order by id_funcion asc ";
			
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
			
			ArrayList<Integer> res = new ArrayList<Integer>();
			
//			if (resultado!=null) {
//				
//				for (Object irObj: resultado) {
//									
//					Object [] cols= (Object []) irObj;
//					res.add(new Integer(cols[0].toString()));
//				}
//			}
			
			UsuarioFunciones funciones = new UsuarioFunciones();
			funciones.setListaFunciones(resultado);
			
			return funciones;
			
	}	




}
