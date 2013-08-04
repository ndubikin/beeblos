package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.ObjectException;
import org.beeblos.bpm.core.model.ObjectM;
import com.sp.common.util.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;






public class ObjectDao {
	
	private static final Log logger = LogFactory.getLog(ObjectDao.class.getName());
	
	public ObjectDao (){
		
	}
	
	public Integer add(ObjectM object) throws ObjectException {
		
		logger.debug("add() ObjectM - Name: ["+object.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(object));

		} catch (HibernateException ex) {
			logger.error("ObjectMDao: add - Can't store object definition record "+ 
					object.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new ObjectException("ObjectMDao: add - Can't store object definition record "+ 
					object.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(ObjectM object) throws ObjectException {
		
		logger.debug("update() ObjectM < id = "+object.getId()+">");
		
		try {

			HibernateUtil.actualizar(object);


		} catch (HibernateException ex) {
			logger.error("ObjectMDao: update - Can't update object definition record "+ 
					object.getName()  +
					" - id = "+object.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new ObjectException("ObjectMDao: update - Can't update object definition record "+ 
					object.getName()  +
					" - id = "+object.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(ObjectM object) throws ObjectException {

		logger.debug("delete() ObjectM - Name: ["+object.getName()+"]");
		
		try {

			//object = getObjectMByPK(object.getId());

			HibernateUtil.borrar(object);

		} catch (HibernateException ex) {
			logger.error("ObjectMDao: delete - Can't delete proccess definition record "+ object.getName() +
					" <id = "+object.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new ObjectException("ObjectMDao:  delete - Can't delete proccess definition record  "+ object.getName() +
					" <id = "+object.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

//		} catch (ObjectException ex1) {
//			logger.error("ObjectMDao: delete - Exception in deleting object rec "+ object.getName() +
//					" <id = "+object.getId()+ "> no esta almacenada \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );
//			throw new ObjectException("ObjectMDao: delete - Exception in deleting object rec "+ object.getName() +
//					" <id = "+object.getId()+ "> not stored \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );

		} 

	}

	public ObjectM getObjectMByPK(Integer id) throws ObjectException {

		ObjectM object = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			object = (ObjectM) session.get(ObjectM.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("ObjectMDao: getObjectMByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new ObjectException("ObjectMDao: getObjectMByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return object;
	}
	
	
	public ObjectM getObjectMByName(String name) throws ObjectException {

		ObjectM  object = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			object = (ObjectM) session.createCriteria(ObjectM.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("ObjectMDao: getObjectMByName - can't obtain object name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new ObjectException("getObjectMByName;  can't obtain object name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return object;
	}

	
	public List<ObjectM> getObjectMs() throws ObjectException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<ObjectM> objects = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			objects = session.createQuery("From ObjectM order by name ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("ObjectMDao: getObjectMs() - can't obtain object list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new ObjectException("ObjectMDao: getObjectMs() - can't obtain object list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return objects;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String blank )
	throws ObjectException {
		 
			List<ObjectM> lwpd = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwpd = session
						.createQuery("From ObjectM order by name ")
						.list();
		
				if (lwpd!=null) {
					
					// inserta los extras
					if ( firstLineText!=null && !"".equals(firstLineText) ) {
						if ( !firstLineText.equals("WHITESPACE") ) {
							retorno.add(new StringPair(null,firstLineText));  // deja la primera línea con lo q venga
						} else {
							retorno.add(new StringPair(null," ")); // deja la primera línea en blanco ...
						}
					}
					
					if ( blank!=null && !"".equals(blank) ) {
						if ( !blank.equals("WHITESPACE") ) {
							retorno.add(new StringPair(null,blank));  // deja la separación línea con lo q venga
						} else {
							retorno.add(new StringPair(null," ")); // deja la separacion con linea en blanco ...
						}
					}
					
				
					
					for (ObjectM wpd: lwpd) {
						retorno.add(new StringPair(wpd.getId(),wpd.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new ObjectException(
						"Can't obtain ObjectMs combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;


	}

//	// nes 20101001 - hasta el final ...
//	public List<ObjectMAmpliada> finderObjectM(
//			String filtroNombreObjectM, Integer objectAnio
//	) throws ObjectException {
//		
//		
//		String estado="";
//		String filtro="";
//
//		
//		if (filtroNombreObjectM!=null && ! "".equals(filtroNombreObjectM)){
//			filtro=" c.object_nombre LIKE '%"+filtroNombreObjectM.trim()+"%' ";
//		} else 	{ 
//			filtro="";
//		}
//
//		if (objectAnio!=null && objectAnio>0){
//			if (!"".equals(filtro)) {
//				filtro+=" AND c.object_anio="+objectAnio;
//			} else {
//				filtro+=" c.object_anio="+objectAnio;
//			}
//		}
//		
//		
////		// el año de la object
////		if (objectAnio!=null && !"".equals(objectAnio)){
////			if (!"".equals(filtro)) {
////				filtro+=" AND c.object_anio='"+objectAnio+"' ";
////			} else {
////				filtro+=" c.object_anio='"+objectAnio+"' ";
////			}
////		}
//		
//		if (filtro!=null && !"".equals(filtro)) {
//			filtro= " WHERE "+filtro;
//		}
//
//		logger.debug("002 ------>> finderObjectM -> filtro:"+filtro+"<<-------");
//		
//		String query = _armaQuery(filtro);
//
//		return finderObjectM(query);
//
//	}
	

	

//	public List<ObjectMAmpliada> finderObjectM(
//			String query
//	) throws ObjectException {
//		
//		
//		org.hibernate.Session session = null;
//		org.hibernate.Transaction tx = null;
//		
//		List<Object[]> resultado = null;
//		List<ObjectMAmpliada> retorno = new ArrayList<ObjectMAmpliada>();
//		List<ProyectoResumido> lpr = new ArrayList<ProyectoResumido>();
//
//		ProyectoDao pdao = new ProyectoDao();
//		
//		Integer idObjectM, objectAnio;
//		String objectNombre, objectComentarios;
//		Date objectFechaApertura, objectFechaCierre = new Date();
//		
//		
//		try {
//
//			session = HibernateUtil.obtenerSession();
//			tx = session.getTransaction();
//			tx.begin();
//
//			Hibernate.initialize(resultado); // nes 20100821
//			
//			resultado = session
//					.createSQLQuery(query)
//					.list();
//
//			tx.commit();
//			
//			if (resultado!=null) {
//				for (Object irObj: resultado) {
//					
//					Object [] cols= (Object []) irObj;
//
////					c.id_object, c.object_anio, c.object_nombre, 
////						0					1					2					
////					c.object_fecha_apertura, c.object_fecha_cierre, c.object_comentarios
////						3									4							5
//					
//					
//
//					
//					idObjectM 			= (cols[0]!=null ? new Integer(cols[0].toString()):null); 
//					objectAnio		= (cols[1]!=null ? new Integer(cols[1].toString()):null);
//					objectNombre		= (cols[2]!=null ? cols[2].toString():"");
//
//					objectFechaApertura= (cols[3]!=null ? (Date)cols[3]:null);
//					objectFechaCierre = (cols[4]!=null ? (Date)cols[4]:null);
//					
//					objectComentarios	= (cols[5]!=null ? cols[5].toString():"");
//					
//					if (idObjectM!=null && idObjectM!=0) {
//						lpr = pdao.finderProyecto(idObjectM);
//					} else {
//						lpr=null;
//					}
//					
//					retorno.add( new ObjectMAmpliada(idObjectM, objectNombre, objectComentarios,
//							objectAnio,  objectFechaApertura, objectFechaCierre,    
//							lpr		));
//				
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
//			logger.warn("ProyectoDAO: finderObjectM() - No pudo recuperarse la lista de proyectos almacenadas - "+
//					ex.getMessage()+ "\n"+ex.getLocalizedMessage()+" \n"+ex.getCause());
//			throw new ObjectException(ex);
//		
//		} catch (Exception e) {
//			logger.warn("Exception ProyectoDAO: finderObjectM() - error en el manejo de la lista de proyectos almacenadas - "+
//					e.getMessage()+ "\n"+e.getLocalizedMessage()+" \n"+e.getCause());
//			e.getStackTrace();
//			throw new ObjectException(e);
//		}
//
//		return retorno;
//		
//	}
//	
////
////			  id_object 
////			  object_anio
////			  object_nombre
////			  object_fecha_apertura
////			  object_fecha_cierre
////			  id_tipo_object
////			  object_comentarios
////			  fecha_alta
////			  usuario_alta
////			  fecha_modificacion
////			  usuario_modificacion
//
//
//	private String _armaQuery(String filtro){
//		
//		String tmpQuery = "SELECT c.id_object, c.object_anio, c.object_nombre,  ";
//		tmpQuery += "  c.object_fecha_apertura, c.object_fecha_cierre, c.object_comentarios  ";
//		tmpQuery += " FROM object c ";
////		tmpQuery += " LEFT OUTER JOIN  proyecto p   ON  p.id_object=c.id_object ";
////		tmpQuery += " LEFT OUTER JOIN  institucion 	i   ON  p.id_institucion=i.id_institucion ";
////		tmpQuery += " LEFT OUTER JOIN  persona 		per ON  p.id_persona_solicitante=per.id_persona";
////		tmpQuery += " LEFT OUTER JOIN  estado_proyecto ep ON  p.id_estado_proyecto=ep.id_estado_proyecto ";
//		tmpQuery += filtro;
//		tmpQuery += " ORDER by c.object_anio, c.object_nombre ASC;";
//		
//		logger.debug("------>> finderObjectM -> query:"+tmpQuery+"<<-------");
//	
//		return tmpQuery;
//	}
//	
//	public boolean verificaNombreRepetido(
//			String objectNombre, Integer idObjectM) 
//	throws ObjectException {
//
//		ObjectM object = null;
//		org.hibernate.Session session = null;
//		org.hibernate.Transaction tx = null;
//
//		try {
//
//			session = HibernateUtil.obtenerSession();
//			tx = session.getTransaction();
//
//			tx.begin();
//
//			object = (ObjectM) session
//							.createQuery("From ObjectM WHERE  objectNombre= ? AND idObjectM != ?")
//								.setString(0, objectNombre)
//								.setInteger(1,	idObjectM)
//								.uniqueResult();
//
//			tx.commit();
//
//		} catch (HibernateException ex) {
//			if (tx != null)
//				tx.rollback();
//			logger.warn("ObjectMDao: obtenerObjectMPorNombre - No se pudo recuperar un ObjectM "+ objectNombre +" - "+ex.getMessage()  );
//			throw new ObjectException("No se pudo obtener el ObjectM: " + objectNombre
//					+ " - " + ex.getMessage() + " ; " + ex.getCause());
//
//		}
//		if (object!=null) return true;
//		else return false;
//	}

}
	