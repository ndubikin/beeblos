package org.beeblos.bpm.wc.taglib.bean.util;

import static org.beeblos.bpm.core.util.Constants.FAIL;
import static org.beeblos.bpm.core.util.Constants.PASO_DESBLOQUEADO;
import static org.beeblos.bpm.core.util.Constants.PROCESS_TASK;

import java.lang.reflect.InvocationTargetException;

import javax.el.ValueExpression;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bee.bpm.wbt.error.InjectorException;
import org.beeblos.bpm.core.bl.WStepWorkBL;
import org.beeblos.bpm.core.error.AlreadyExistsRunningProcessException;
import org.beeblos.bpm.core.error.CantLockTheStepException;
import org.beeblos.bpm.core.error.WStepLockedByAnotherUserException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.error.WUserDefException;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.wc.taglib.bean.InyectorBean;
import org.beeblos.bpm.wc.taglib.bean.PasoBean;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;



public class TareaWorkflowUtil extends CoreManagedBean{

	/**
	 * Clase de utilidad que permite cargar un paso de workflow y el objeto relacionado
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(TareaWorkflowUtil.class);
	
	public String cargarPasoWorkflow(
			Integer idPasoProceso, Integer idObject, String idObjectType ) 
	throws CantLockTheStepException, WStepLockedByAnotherUserException, WStepWorkException, WUserDefException{

		String retorno=FAIL;


			
		ValueExpression valueBinding = super
						.getValueExpression("#{pasoBean}");

		if (valueBinding != null) {

			PasoBean pb = (PasoBean) valueBinding
								.getValue(super.getELContext());
			
			// controla si el bean ya está "vivo" y tiene info de algún paso cargado
			// quiere decir que podrian haber salido del paso sin dar click en los botones del workitem
			// asi que lo desbloquea para que no quede cogido por ese usuario ( siempre q sea el mismo usuario ... )
			if (pb.getIdStepWork()!=null && pb.getIdStepWork()!=0) {
				pb.cancelar();
			}
			pb.init();
			pb.setIdStepWork(idPasoProceso);
			pb.setIdObject(idObject);
			pb.setIdObjectType(idObjectType);
			pb.setPaso();
			
			pb.setNombreBeanEL(cargarObjetoRelacionado(idObject, idObjectType)); // carga el objeto relacionado y setea el bean ( EL ) en el bean que gestiona el paso ( PasoBean ) ...
	
			retorno = PROCESS_TASK;
		}

		return retorno;

	}
	
	public String desbloquearPasoWorkflow(
			Integer idPasoProceso, Integer idObject, String idObjectType ) 
	throws CantLockTheStepException, WStepLockedByAnotherUserException, WStepWorkException, WUserDefException{

		String retorno=FAIL;


			
		ValueExpression valueBinding = super
						.getValueExpression("#{pasoBean}");

		if (valueBinding != null) {

			PasoBean pb = (PasoBean) valueBinding
								.getValue(super.getELContext());
			
			// controla si el bean ya está "vivo" y tiene info de algún paso cargado
			// quiere decir que podrian haber salido del paso sin dar click en los botones del workitem
			// asi que lo desbloquea para que no quede cogido por ese usuario ( siempre q sea el mismo usuario ... )
			if (pb.getIdStepWork()!=null && pb.getIdStepWork()!=0) {
				pb.cancelar();
			}
			pb.init();
			pb.setIdStepWork(idPasoProceso);
			pb.setIdObject(idObject);
			pb.setIdObjectType(idObjectType);
			pb.setPaso();
			
			pb.desbloquear();
	
			retorno = PASO_DESBLOQUEADO;
		}

		return retorno;

	}
	
	// nes 20101025
	public String cargarPasoWorkflow(
			Integer idObject, String idObjectType ) 
	throws CantLockTheStepException, WStepLockedByAnotherUserException, WStepWorkException, WUserDefException {

		String retorno=FAIL;
		
		ValueExpression valueBinding = super.getValueExpression("#{pasoBean}");
		
		if (valueBinding != null) {

			PasoBean pasob = 
					(PasoBean) valueBinding
							.getValue(super.getELContext()); 
			
			pasob.setIdStepWork(null);
			pasob.setIdObject(idObject);
			pasob.setIdObjectType(idObjectType);

			pasob.setPaso();
			
			retorno = PROCESS_TASK;
		
		}
		
		return retorno;
	}	
	
	
	public String cargarObjetoRelacionado( Integer idObject, String idObjectType ){
		
		if (idObject==null || idObject.equals(0)
				|| idObjectType==null || "".equals(idObjectType)
				|| idObjectType.equals(WStepWork.class.getName()) ) {
			logger.info("object is itself...");
			return null;
			
		}
		
		String retorno=null;
		
		Object objBase = new Object();
		Object objBean = new CoreManagedBean();
	   	Class<?> item;
	   	Class<?> itemBean;
		Class<?>[] cls = new Class[1];
		cls[0] = Integer.class;
	   	String nombreBean, nombreBeanEL;

//		Object objNulo = new Object();
//		Class<?>[] clsNula = new Class[0];

		try {
			
//			String raiz = idObjectType.substring(0,idObjectType.lastIndexOf(".")+1);
//			String entidad = idObjectType.substring(idObjectType.lastIndexOf(".")+1);
			
			item = Class.forName(idObjectType);
			objBase = item.newInstance();
			
			itemBean = Class.forName(objBase.getClass().getField("BEAN_RELACIONADO").get(objBase).toString());
			objBean = itemBean.newInstance();
			
			nombreBean =  objBean.getClass().getName();
			nombreBean = nombreBean.substring(nombreBean.lastIndexOf(".")+1);
			nombreBeanEL = "#{" + nombreBean.substring(0,1).toLowerCase()+nombreBean.substring(1)+"}";
			retorno=nombreBeanEL;
			
			System.out.println(" --------------->>>> clase creada:"+objBean.getClass().getName()+"  nombreBeanEL:"+nombreBeanEL);

			try {
				
				
				ValueExpression valueBinding = super.getValueExpression(nombreBeanEL);
				
				if (valueBinding != null) {

					objBean = 
							 valueBinding
									.getValue(super.getELContext()); 

					// nes 20101222
					objBean.getClass().getMethod("init", null).invoke(objBean, null);
					objBean.getClass().getMethod("setId", cls).invoke(objBean, idObject);
					objBean.getClass().getMethod("load", null).invoke(objBean, null);
					

				}
	


				
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return retorno;
		
	}
	
	// nes 20101025
	public Integer inyectarPaso(
			Integer processId, Integer idStep, 
			Integer idObject, String idObjectType,
			String objReference, String objComments
			) 
	throws InjectorException {

		Integer idStepWork = null;
		
		if ( processId==null || processId==0  
				|| idStep==null || idStep==0 ) {
			throw new InjectorException("No se puede lanzar el workflow porque viene en cero o null el idProcess o IdStep ...");
		}
		
		if ( idObject==null || idObject==0 ) {
			throw new InjectorException("No se puede lanzar el workflow porque no está definido ningún id object ...");
		}
		
		if ( idObjectType==null || "".equals(idObjectType) ) {
			throw new InjectorException("No se puede lanzar el workflow porque no está definido ningún id object type ...");
		}
		
		ValueExpression valueBinding = super.getValueExpression("#{inyectorBean}");
		
		if (valueBinding != null) {

			InyectorBean ib = 
					(InyectorBean) valueBinding
							.getValue(super.getELContext()); 

			ib.init();		
			
			ib.setIdObject(idObject);
			ib.setIdObjectType(idObjectType);
			ib.setObjReference(objReference);
			ib.setObjComments(objComments);

			// setea el workflow y el paso que necesitamos ...
			ib.setSelectedProcessId(processId);
			ib.setSelectedStepDefId(idStep);
			
			ib.launchWork();
			
			idStepWork = ib.getIdStepWork();
	
		}

		return idStepWork;

	}
	
	public boolean instanciarInyectorBean(
			Integer idObject, String idObjectType,
			String objReference, String objComments ) {

		ValueExpression valueBinding = super.getValueExpression("#{inyectorBean}");
		
		if (valueBinding != null) {

			InyectorBean ib = 
					(InyectorBean) valueBinding
							.getValue(super.getELContext()); 

			ib.init();		
			
			ib.setIdObject(idObject);
			ib.setIdObjectType(idObjectType);
			ib.setObjReference(objReference);
			ib.setObjComments(objComments);
	
			return true;
		}

		return false;

	}	
	

	public boolean controlExistenciaPasosVivos( 
			Integer idProcess, Integer idObject, String idObjectType, Integer idUsuario ) 
	throws AlreadyExistsRunningProcessException, WStepWorkException {
		
		boolean ret = false;
		
		try {
			if ( new WStepWorkBL().existsActiveProcess(idProcess, idObject, idObjectType, idUsuario )) {
				
				String mensaje = "Ya existe un wofkflow en ejecución para este elemento :["+idObject+"] ["+idObjectType+"]";
				logger.info("inyectar: "+mensaje);
				throw new AlreadyExistsRunningProcessException(mensaje);
			}
		} catch (WStepWorkException e) {
			String mensaje = "El sistema arrojó un error al intentar verificar si existen procesos activos para este objeto :["
								+idObject+"] ["+idObjectType+"]";
			logger.error(mensaje);
			throw new WStepWorkException(mensaje);
		}
		
		return true;
		
	}
}