package org.beeblos.bpm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.noper.StringPair;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;






public class WProcessDefDao {
	
	private static final Log logger = LogFactory.getLog(WProcessDefDao.class.getName());
	
	public WProcessDefDao (){
		
	}
	
	public Integer add(WProcessDef process) throws WProcessDefException {
		
		logger.debug("add() WProcessDef - Name: ["+process.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.guardar(process));

		} catch (HibernateException ex) {
			logger.error("WProcessDefDao: add - Can't store process definition record "+ 
					process.getName()+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao: add - Can't store process definition record "+ 
					process.getName()+" - "+ex.getMessage()+"\n"+ex.getCause());

		}

	}
	
	
	public void update(WProcessDef process) throws WProcessDefException {
		
		logger.debug("update() WProcessDef < id = "+process.getId()+">");
		
		try {

			HibernateUtil.actualizar(process);


		} catch (HibernateException ex) {
			logger.error("WProcessDefDao: update - Can't update process definition record "+ 
					process.getName()  +
					" - id = "+process.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause()   );
			throw new WProcessDefException("WProcessDefDao: update - Can't update process definition record "+ 
					process.getName()  +
					" - id = "+process.getId()+"\n - "+ex.getMessage()+"\n"+ex.getCause());

		}
					
	}
	
	
	public void delete(WProcessDef process) throws WProcessDefException {

		logger.debug("delete() WProcessDef - Name: ["+process.getName()+"]");
		
		try {

			//process = getWProcessDefByPK(process.getId());

			HibernateUtil.borrar(process);

		} catch (HibernateException ex) {
			logger.error("WProcessDefDao: delete - Can't delete proccess definition record "+ process.getName() +
					" <id = "+process.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao:  delete - Can't delete proccess definition record  "+ process.getName() +
					" <id = "+process.getId()+ "> \n"+" - "+ex.getMessage()+"\n"+ex.getCause() );

//		} catch (WProcessDefException ex1) {
//			logger.error("WProcessDefDao: delete - Exception in deleting process rec "+ process.getName() +
//					" <id = "+process.getId()+ "> no esta almacenada \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );
//			throw new WProcessDefException("WProcessDefDao: delete - Exception in deleting process rec "+ process.getName() +
//					" <id = "+process.getId()+ "> not stored \n"+" - "+ex1.getMessage()+"\n"+ex1.getCause() );

		} 

	}

	public WProcessDef getWProcessDefByPK(Integer id) throws WProcessDefException {

		WProcessDef process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			process = (WProcessDef) session.get(WProcessDef.class, id);

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDefDao: getWProcessDefByPK - we can't obtain the required id = "+
					id + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao: getWProcessDefByPK - we can't obtain the required id : " + 
					id + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}
	
	
	public WProcessDef getWProcessDefByName(String name) throws WProcessDefException {

		WProcessDef  process = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			process = (WProcessDef) session.createCriteria(WProcessDef.class).add(
					Restrictions.naturalId().set("name", name))
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDefDao: getWProcessDefByName - can't obtain process name = " +
					name + "]  almacenada - \n"+ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("getWProcessDefByName;  can't obtain process name: " + 
					name + " - " + ex.getMessage()+"\n"+ex.getCause());

		}

		return process;
	}

	
	public List<WProcessDef> getWProcessDefs() throws WProcessDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WProcessDef> processs = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processs = session.createQuery("From WProcessDef order by name ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDefDao: getWProcessDefs() - can't obtain process list - " +
					ex.getMessage()+"\n"+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao: getWProcessDefs() - can't obtain process list: "
					+ ex.getMessage()+"\n"+ex.getCause());

		}

		return processs;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String blank )
	throws WProcessDefException {
		 
			List<WProcessDef> lwpd = null;
			List<StringPair> retorno = new ArrayList<StringPair>(10);
			
			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;

			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();
				tx.begin();

				lwpd = session
						.createQuery("From WProcessDef order by name ")
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
					
				
					
					for (WProcessDef wpd: lwpd) {
						retorno.add(new StringPair(wpd.getId(),wpd.getName()));
					}
				} else {
					// nes  - si el select devuelve null entonces devuelvo null
					retorno=null;
				}
				
				
			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				throw new WProcessDefException(
						"Can't obtain WProcessDefs combo list "
						+ex.getMessage()+"\n"+ex.getCause());
			} catch (Exception e) {}

			return retorno;


	}

//	// nes 20101001 - hasta el final ...
//	public List<WProcessDefAmpliada> finderWProcessDef(
//			String filtroNombreWProcessDef, Integer processAnio
//	) throws WProcessDefException {
//		
//		
//		String estado="";
//		String filtro="";
//
//		
//		if (filtroNombreWProcessDef!=null && ! "".equals(filtroNombreWProcessDef)){
//			filtro=" c.process_nombre LIKE '%"+filtroNombreWProcessDef.trim()+"%' ";
//		} else 	{ 
//			filtro="";
//		}
//
//		if (processAnio!=null && processAnio>0){
//			if (!"".equals(filtro)) {
//				filtro+=" AND c.process_anio="+processAnio;
//			} else {
//				filtro+=" c.process_anio="+processAnio;
//			}
//		}
//		
//		
////		// el año de la process
////		if (processAnio!=null && !"".equals(processAnio)){
////			if (!"".equals(filtro)) {
////				filtro+=" AND c.process_anio='"+processAnio+"' ";
////			} else {
////				filtro+=" c.process_anio='"+processAnio+"' ";
////			}
////		}
//		
//		if (filtro!=null && !"".equals(filtro)) {
//			filtro= " WHERE "+filtro;
//		}
//
//		logger.debug("002 ------>> finderWProcessDef -> filtro:"+filtro+"<<-------");
//		
//		String query = _armaQuery(filtro);
//
//		return finderWProcessDef(query);
//
//	}
	

	

//	public List<WProcessDefAmpliada> finderWProcessDef(
//			String query
//	) throws WProcessDefException {
//		
//		
//		org.hibernate.Session session = null;
//		org.hibernate.Transaction tx = null;
//		
//		List<Object[]> resultado = null;
//		List<WProcessDefAmpliada> retorno = new ArrayList<WProcessDefAmpliada>();
//		List<ProyectoResumido> lpr = new ArrayList<ProyectoResumido>();
//
//		ProyectoDao pdao = new ProyectoDao();
//		
//		Integer idWProcessDef, processAnio;
//		String processNombre, processComentarios;
//		Date processFechaApertura, processFechaCierre = new Date();
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
////					c.id_process, c.process_anio, c.process_nombre, 
////						0					1					2					
////					c.process_fecha_apertura, c.process_fecha_cierre, c.process_comentarios
////						3									4							5
//					
//					
//
//					
//					idWProcessDef 			= (cols[0]!=null ? new Integer(cols[0].toString()):null); 
//					processAnio		= (cols[1]!=null ? new Integer(cols[1].toString()):null);
//					processNombre		= (cols[2]!=null ? cols[2].toString():"");
//
//					processFechaApertura= (cols[3]!=null ? (Date)cols[3]:null);
//					processFechaCierre = (cols[4]!=null ? (Date)cols[4]:null);
//					
//					processComentarios	= (cols[5]!=null ? cols[5].toString():"");
//					
//					if (idWProcessDef!=null && idWProcessDef!=0) {
//						lpr = pdao.finderProyecto(idWProcessDef);
//					} else {
//						lpr=null;
//					}
//					
//					retorno.add( new WProcessDefAmpliada(idWProcessDef, processNombre, processComentarios,
//							processAnio,  processFechaApertura, processFechaCierre,    
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
//			logger.warn("ProyectoDAO: finderWProcessDef() - No pudo recuperarse la lista de proyectos almacenadas - "+
//					ex.getMessage()+ "\n"+ex.getLocalizedMessage()+" \n"+ex.getCause());
//			throw new WProcessDefException(ex);
//		
//		} catch (Exception e) {
//			logger.warn("Exception ProyectoDAO: finderWProcessDef() - error en el manejo de la lista de proyectos almacenadas - "+
//					e.getMessage()+ "\n"+e.getLocalizedMessage()+" \n"+e.getCause());
//			e.getStackTrace();
//			throw new WProcessDefException(e);
//		}
//
//		return retorno;
//		
//	}
//	
////
////			  id_process 
////			  process_anio
////			  process_nombre
////			  process_fecha_apertura
////			  process_fecha_cierre
////			  id_tipo_process
////			  process_comentarios
////			  fecha_alta
////			  usuario_alta
////			  fecha_modificacion
////			  usuario_modificacion
//
//
//	private String _armaQuery(String filtro){
//		
//		String tmpQuery = "SELECT c.id_process, c.process_anio, c.process_nombre,  ";
//		tmpQuery += "  c.process_fecha_apertura, c.process_fecha_cierre, c.process_comentarios  ";
//		tmpQuery += " FROM process c ";
////		tmpQuery += " LEFT OUTER JOIN  proyecto p   ON  p.id_process=c.id_process ";
////		tmpQuery += " LEFT OUTER JOIN  institucion 	i   ON  p.id_institucion=i.id_institucion ";
////		tmpQuery += " LEFT OUTER JOIN  persona 		per ON  p.id_persona_solicitante=per.id_persona";
////		tmpQuery += " LEFT OUTER JOIN  estado_proyecto ep ON  p.id_estado_proyecto=ep.id_estado_proyecto ";
//		tmpQuery += filtro;
//		tmpQuery += " ORDER by c.process_anio, c.process_nombre ASC;";
//		
//		logger.debug("------>> finderWProcessDef -> query:"+tmpQuery+"<<-------");
//	
//		return tmpQuery;
//	}
//	
//	public boolean verificaNombreRepetido(
//			String processNombre, Integer idWProcessDef) 
//	throws WProcessDefException {
//
//		WProcessDef process = null;
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
//			process = (WProcessDef) session
//							.createQuery("From WProcessDef WHERE  processNombre= ? AND idWProcessDef != ?")
//								.setString(0, processNombre)
//								.setInteger(1,	idWProcessDef)
//								.uniqueResult();
//
//			tx.commit();
//
//		} catch (HibernateException ex) {
//			if (tx != null)
//				tx.rollback();
//			logger.warn("WProcessDefDao: obtenerWProcessDefPorNombre - No se pudo recuperar un WProcessDef "+ processNombre +" - "+ex.getMessage()  );
//			throw new WProcessDefException("No se pudo obtener el WProcessDef: " + processNombre
//					+ " - " + ex.getMessage() + " ; " + ex.getCause());
//
//		}
//		if (process!=null) return true;
//		else return false;
//	}

}
	