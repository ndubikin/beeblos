package org.beeblos.security.st.util;

import java.io.Serializable;
import java.sql.Connection;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


/**
 * @author roger
 * 
 */

public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static Session session;
	private static Transaction tx;

	public HibernateUtil() {

	}

	private static SessionFactory obtenerSessionFactory() {

		if (sessionFactory == null) {

			sessionFactory = new Configuration().configure()
					.buildSessionFactory();

		}

		return sessionFactory;

	}

	public static Session obtenerSession() {
		
		// nes - 20100809 - para que muestre el error correcto cuando no puede devolver
		//       la session por algún motivo ...
		try {
			return obtenerSessionFactory().getCurrentSession();
		} catch  (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	@SuppressWarnings("deprecation")
	public static Connection obtenerConnection(){
		
		Connection conex= null;
		tx = obtenerSession().getTransaction();
		tx.begin();
		conex = obtenerSession().connection();
		return conex;
		
	}
	
	public static Object obtenerPorPk(Object obj, Integer pk) throws HibernateException {

		try {
			Object item=null;
			session = obtenerSession();
			tx = session.getTransaction();
			
			tx.begin();
			Serializable res = null;
			if (obj != null)
				
				item = session.get(obj.getClass(), pk);

			tx.commit();

			 return item;
			 
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			throw ex; // nes 20100930 - hay q tirar el error tal cual viene hacia arriba si no se pierde info

		}

	}
	
	
	public static String guardar(Object obj) throws HibernateException {

		try {
			session = obtenerSession();
			tx = session.getTransaction();
			
			tx.begin();
			Serializable res = null;
			if (obj != null)
				res = session.save(obj);
			 tx.commit();

			 return res.toString();
			 
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			ex.printStackTrace();
			throw ex; // nes 20100930 - hay q tirar el error tal cual viene hacia arriba si no se pierde info

		} 

	}

	public static void borrar(Object obj) throws HibernateException {

		try {
			session = obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			session.delete(obj);

			tx.commit();

		} catch (HibernateException ex) {
			ex.printStackTrace();
			if (tx != null)
				tx.rollback();
			throw ex; // nes 20100930 - hay q tirar el error tal cual viene hacia arriba si no se pierde info

		}

	}

	public static void actualizar(Object obj) throws HibernateException {
		
		try {
			session = obtenerSession();
			tx = session.beginTransaction();

			if (obj != null) {
				
				session.update(obj);
			}
			tx.commit();

		} catch (HibernateException ex) {
			ex.printStackTrace();
			if (tx != null)
				tx.rollback();
			throw ex; // nes 20100930 - hay q tirar el error tal cual viene hacia arriba si no se pierde info

		}

	}

}
