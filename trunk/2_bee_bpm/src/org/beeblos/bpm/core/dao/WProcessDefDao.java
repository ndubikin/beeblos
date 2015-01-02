package org.beeblos.bpm.core.dao;

import static com.sp.common.util.ConstantsCommon.DATE_FORMAT;
import static com.sp.common.util.ConstantsCommon.DATE_HOUR_COMPLETE_FORMAT;
import static com.sp.common.util.ConstantsCommon.LAST_ADDED;
import static com.sp.common.util.ConstantsCommon.LAST_MODIFIED;
import static org.beeblos.bpm.core.util.Constants.ACTIVE;
import static org.beeblos.bpm.core.util.Constants.ALIVE;
import static org.beeblos.bpm.core.util.Constants.INACTIVE;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.graph.ElementWrapper;
import org.beeblos.bpm.core.graph.Layer;
import org.beeblos.bpm.core.graph.MxCell;
import org.beeblos.bpm.core.graph.MxGeometry;
import org.beeblos.bpm.core.graph.MxGraphModel;
import org.beeblos.bpm.core.graph.Symbol;
import org.beeblos.bpm.core.graph.Workflow;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.noper.WProcessDefLight;
import org.beeblos.bpm.core.util.Constants;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jadira.usertype.dateandtime.joda.columnmapper.TimestampColumnDateTimeMapper;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.sp.common.util.HibernateUtil;
import com.sp.common.util.StringPair;


public class WProcessDefDao {
	
	private static final Log logger = LogFactory.getLog(WProcessDefDao.class.getName());
	
	public WProcessDefDao (){
		
	}
	
	public Integer add(WProcessDef process, Integer currentUserId) throws WProcessDefException {
		
		logger.debug("add() WProcessDef - Name: ["+process.getName()+"]");
		
		try {

			return Integer.valueOf(HibernateUtil.save(process));

		} catch (HibernateException ex) {
			logger.error("WProcessDefDao: add - Can't store process definition record "+ 
					process.getName()+" - "+ex.getMessage()+" "+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao: add - Can't store process definition record "+ 
					process.getName()+" - "+ex.getMessage()+" "+ex.getCause());

		}

	}
	
	
	public void update(WProcessDef process, Integer currentUserId) throws WProcessDefException {
		
		logger.debug("update() WProcessDef < id = "+process.getId()+">");
		
		try {

			HibernateUtil.update(process);


		} catch (HibernateException ex) {
			String mess = "WProcessDefDao: update - Can't update process definition record " 
					+ (process.getName()!=null?process.getName():"null name")
					+" id:"+(process.getId()!=null?process.getId():"null id")
					+" "
					+ex.getMessage()+" "
					+(ex.getCause()!=null?ex.getCause():" "); 
			logger.error( mess );
			throw new WProcessDefException(mess);

		}
					
	}
	
	// dml 20130703
	public void updateProcessXmlMap(Integer processId, String processMap, Integer modUserId,
			DateTime modDate) throws WProcessDefException {

		logger.debug("updateProcessXmlMap() WProcessDef < id = " + processId + ">");

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;
		
		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			session.createQuery(
					"UPDATE WProcessDef SET processMap = :processMap, modUser = :modUserId, modDate = :modDate WHERE id = :processId")
					.setString("processMap", processMap)
					.setInteger("modUserId", modUserId)
					.setInteger("processId", processId)
					.setParameter("modDate", modDate)
					.executeUpdate();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String message = "HibernateException: update - Can't update xml map for process " + processId
					+ " - and processMap = " + processMap + "  - " + ex.getMessage() + " "
					+ ex.getCause();
			logger.error(message);
			throw new WProcessDefException(message);
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();			
			String message = "Exception: update - Can't update xml map for process " + processId
					+ " - and processMap = " + processMap + "  - " + ex.getMessage() + " "
					+ ex.getCause();
			logger.error(message);
			throw new WProcessDefException(message);			

		}

	}

	public void delete(WProcessDef process, Integer currentUserId) throws WProcessDefException {

		logger.debug("delete() WProcessDef - Name: ["+process.getName()+"]");
		
		try {

			//process = getWProcessDefByPK(process.getId());

			HibernateUtil.delete(process);

		} catch (HibernateException ex) {
			logger.error("WProcessDefDao: delete - Can't delete proccess definition record "+ process.getName() +
					" <id = "+process.getId()+ "> "+" - "+ex.getMessage()+" "+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao:  delete - Can't delete proccess definition record  "+ process.getName() +
					" <id = "+process.getId()+ "> "+" - "+ex.getMessage()+" "+ex.getCause() );

//		} catch (WProcessDefException e) {
//			logger.error("WProcessDefDao: delete - Exception in deleting process rec "+ process.getName() +
//					" <id = "+process.getId()+ "> no esta almacenada "+" - "+e.getMessage()+" "+e.getCause() );
//			throw new WProcessDefException("WProcessDefDao: delete - Exception in deleting process rec "+ process.getName() +
//					" <id = "+process.getId()+ "> not stored "+" - "+e.getMessage()+" "+e.getCause() );

		} 

	}

	public WProcessDef getWProcessDefByPK(Integer id, Integer currentUserId) throws WProcessDefException {

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
			String mess = "HibernateException: getWProcessDefByPK - can't obtain WProcessDef with id:"
							+ (id!=null?id:"null") 
							+ " - "+ex.getMessage()+" "
							+(ex.getCause()!=null?ex.getCause():" "); 
			logger.error(mess);
			throw new WProcessDefException(mess);
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getWProcessDefByPK - can't obtain WProcessDef with id:"
					+ (id!=null?id:"null") 
					+ " - "+ex.getMessage()+" "
					+(ex.getCause()!=null?ex.getCause():" ")+" "
					+ ex.getClass(); 
			logger.error(mess);
			throw new WProcessDefException(mess);	

		}
		
		/**
		 * Load alive step sequence list for process...
		 * nes 20141217 - pasado aquí que lo teníamos fuera ... 
		 * 
		 */
		if (process!=null && process.getId()!=null) {
			try {
				process
					.setStepSequenceList(
							new WStepSequenceDefDao()
								.getStepSequenceList(process.getId(), ALIVE) );
				
			} catch (WStepSequenceDefException ex) {
				String mess = "WStepSequenceDefException: can't load process sequence list for wProcessDef id:"
						+ process.getId()
						+ " - "+ex.getMessage()+" "
						+(ex.getCause()!=null?ex.getCause():" "); 
				logger.error(mess);
				throw new WProcessDefException(mess);	
			}			
		}



		return process;
	}
	
/*	
	public WProcessDef getWProcessDefByName(String name, Integer currentUserId) throws WProcessDefException {

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
					name + "]  almacenada - "+ex.getMessage()+" "+ex.getCause() );
			throw new WProcessDefException("getWProcessDefByName;  can't obtain process name: " + 
					name + " - " + ex.getMessage()+" "+ex.getCause());

		}

		return process;
	}
*/
	
	// versionId = id from WProcessDef table
	public String getProcessNameByVersionId(Integer versionId, Integer currentUserId) throws WProcessDefException {

		String name = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			name = (String) session
					.createQuery("Select WProcessDef.process.name from WProcessDef where WProcessDef.id = :id")
						.setInteger("id",versionId)
						.uniqueResult();


			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("HibernateException: getWProcessDefByPK - we can't obtain the required id = "+
					versionId + "]  almacenada - "+ex.getMessage()+" "+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao: getWProcessDefByPK - we can't obtain the required id : " + 
					versionId + " - " + ex.getMessage()+" "+ex.getCause());
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("Exception: getWProcessDefByPK - we can't obtain the required id = "+
					versionId + "]  almacenada - "+ex.getMessage()+" "+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao: getWProcessDefByPK - we can't obtain the required id : " + 
					versionId + " - " + ex.getMessage()+" "+ex.getCause());
		}

		return name;
	}
	
	// dml 20130430
	public Integer getLastVersionNumber(Integer processHeadId, Integer currentUserId) throws WProcessDefException {
	
		Integer version = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			version = (Integer) session.createSQLQuery("SELECT MAX(version) FROM w_process_def WHERE head_id = " + processHeadId)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("HibernateException: getLastWProcessDefVersion - can't obtain process last version = " +
					processHeadId + "]  almacenada - "+ex.getMessage()+" "+ex.getCause() );
			throw new WProcessDefException("getLastWProcessDefVersion;  can't obtain process last version: " + 
					processHeadId + " - " + ex.getMessage()+" "+ex.getCause());
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("Exception: getLastWProcessDefVersion - can't obtain process last version = " +
					processHeadId + "]  almacenada - "+ex.getMessage()+" "+ex.getCause() );
			throw new WProcessDefException("getLastWProcessDefVersion;  can't obtain process last version: " + 
					processHeadId + " - " + ex.getMessage()+" "+ex.getCause());
		}

		if (version == null){
			return 0;
		}
		
		return version;
	}	
	/**
	 * Arma el objeto XML para editar el mapa con mxGraph
	 * 
	 * pab 20141218
	 * 
	 * @param processDefId
	 * @param currentUserId
	 * @return
	 * @throws WProcessDefException
	 * @throws WStepSequenceDefException
	 */
	public String getProcessDefXmlMap2(Integer processDefId, Integer currentUserId) throws WProcessDefException, WStepSequenceDefException {
		try {

			WProcessDef pro = new WProcessDefBL().getWProcessDefByPK(processDefId, 1000);
			/**
			 * creo el obj workflow dinamicamente porque no necesito guardarlo en la base de datos
			 */
			pro.setWorkflowObj(new Workflow(
										pro.getComments() == null ? "" : pro.getComments(),
										pro.getXmlId() == null ? "0" : pro.getXmlId(),
										pro.getId().toString(),
										pro.getProcess().getName()));
			
			JAXBContext jaxbContext2 = JAXBContext.newInstance(org.beeblos.bpm.core.graph.ElementWrapper.class);
			Unmarshaller jaxbUnmarshaller2 = jaxbContext2.createUnmarshaller();
	
			/**
			 * Cojo todos los layer y todos los symbol que tengo guardado como string en la bd y los convierto a objeto java
			 * para poder marshalizar y des-marshalizar
			 * Le pongo el encoding y le pongo el ElementWrapper dinamicamente porque no lo guardo en la bd
			 */
			StringReader sr = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><ElementWrapper>"
								+pro.getXmlSymbolsString() + pro.getXmlLayersString()
								+"</ElementWrapper>");
			ElementWrapper sw = (ElementWrapper)jaxbUnmarshaller2.unmarshal(sr);
			
			/**
			 * le agrego el string convertido a objeto java al proceso
			 */
			pro.setSymbolObjectList(sw.getsList());
			
			/**
			 * Cojo los layers (son obligatorios
			 * Si no hay guardado ningun layer, como lo necesitamos, creo uno x default
			 */
			if(sw.getlList() != null &&
					sw.getlList().size() > 0){
				pro.setLayerObjectList(sw.getlList());
			} else {
				
				Layer l = (Layer)JAXBContext.newInstance(org.beeblos.bpm.core.graph.Layer.class)
										.createUnmarshaller()
										.unmarshal(new StringReader(Constants.DEFAULT_LAYER_XML));
				sw.setlList(new ArrayList<Layer>());
				sw.getlList().add(l);
				pro.setLayerObjectList(sw.getlList());
			}
			
			/**
			 * Ahora vamos con los steps (task)
			 */
			JAXBContext jaxbContext = JAXBContext.newInstance(org.beeblos.bpm.core.graph.MxGraphModel.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			Iterator<WStepDef> i = pro.getSteps().iterator();
			
			/**
			 * Este while le mete el mxcell a cada step (porque el wStepDef no tiene mxcell)
			 */
			while(i.hasNext()){
				
				WStepDef wsd = i.next();
				
				/**
				 * si el step no tiene xmlId le seteo el próximo xmlid
				 */
				if(wsd.getXmlId() == null
						|| wsd.getXmlId().equals("")){
					
					_putXmlIdToJavaStep(wsd, pro);
				}
				
				/**
				 * si tenemos mxcell guardado como string en la bd usamos ese, si no usamos uno por defecto
				 */
				String xmlString = wsd.getMxCellString() != null && !wsd.getMxCellString().equals("") ? wsd.getMxCellString() : Constants.DEFAULT_MXCELL_XML;
				
				StringReader reader = new StringReader(xmlString);
				MxCell m = (MxCell)jaxbUnmarshaller.unmarshal(reader);
				wsd.setMxCellObject(m);
			}

			Iterator<WStepSequenceDef> i2 = pro.getStepSequenceList().iterator();
			
			/**
			 * Este while le mete los mxcell a cada stepSequence
			 */
			while(i2.hasNext()){
				
				WStepSequenceDef ssd = i2.next();
				
				JAXBContext jc = JAXBContext.newInstance(org.beeblos.bpm.core.graph.MxCell.class);
				Unmarshaller ju = jc.createUnmarshaller();

				/**
				 * si tenemos mxcell guardado como string en la bd usamos ese, si no usamos uno por defecto
				 */
				StringReader r = new StringReader(ssd.getXmlMxCellString() == null || ssd.getXmlMxCellString().equals("") ? 
															Constants.DEFAULT_EDGE_MXCELL_XML :
															ssd.getXmlMxCellString() );
				
				MxCell m = (MxCell)ju.unmarshal(r);
				
				ssd.setMxCell(m);
				
				if(ssd.getXmlId() == null
						|| ssd.getXmlId().equals("")){
					_putXmlIdToJavaStepSequence(ssd,pro);
				}
				_putXmlIdToStepsFromAndTo(ssd,pro);
				
			}
			
			
			/**
			 * Una vez ajustados todos los detalles y convertidos los datos que tengo
			 * en String a objeto, creo el xml completo para todo el proceso
			 */
			MxGraphModel m = new MxGraphModel(pro);
		
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			StringWriter stringWriter = new StringWriter();
			
			jaxbMarshaller.marshal(m, stringWriter);
			
			System.out.println("xml map:["+stringWriter.toString()+"]");
			
			/**
			 * devuelve el xml con el proceso completo para mxGraph
			 */
			return stringWriter.toString();

			
		} catch (WProcessDefException e) {

			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private void _putXmlIdToJavaStep(WStepDef wsd, WProcessDef pro){
		
		Integer lastXmlId = 0;
		
		if(pro.getSteps() != null){
		for(WStepDef w : pro.getSteps()){
			if(w.getXmlId() != null
					&& Integer.valueOf(w.getXmlId().trim()) > lastXmlId){
				lastXmlId = Integer.valueOf(w.getXmlId().trim());
			}
		}
		}
		if(pro.getStepSequenceList() != null){
		for(WStepSequenceDef wssd : pro.getStepSequenceList()){
			if(wssd.getXmlId() != null
					&& Integer.valueOf(wssd.getXmlId()) > lastXmlId){
				lastXmlId = Integer.valueOf(wssd.getXmlId());
			}
		}
		}
		if(pro.getSymbolObjectList() != null){
		for(Symbol s : pro.getSymbolObjectList()){
			if(s.getXmlId() != null
					&& Integer.valueOf(s.getXmlId()) > lastXmlId){
				lastXmlId = Integer.valueOf(s.getXmlId());
			}
		}
		}
		// this list (layerobjectlist) cant be null at this point because at least one layer is obligatory. so either we already created it or the xml wont work
		for(Layer l : pro.getLayerObjectList()){
			if(l.getId() != null
					&& Integer.valueOf(l.getId().trim()) > lastXmlId){
				lastXmlId = Integer.valueOf(l.getId().trim());
			}
		}
		
		wsd.setXmlId(String.valueOf(lastXmlId+1));
	}
	
	private void _putXmlIdToJavaStepSequence(WStepSequenceDef wsd, WProcessDef pro){
		
		Integer lastXmlId = 0;
		
		if(pro.getSteps() != null){
		for(WStepDef w : pro.getSteps()){
			if(w.getXmlId() != null
					&& Integer.valueOf(w.getXmlId().trim()) > lastXmlId){
				lastXmlId = Integer.valueOf(w.getXmlId().trim());
			}
		}
		}
		if(pro.getStepSequenceList() != null){
		for(WStepSequenceDef wssd : pro.getStepSequenceList()){
			if(wssd.getXmlId() != null
					&& Integer.valueOf(wssd.getXmlId()) > lastXmlId){
				lastXmlId = Integer.valueOf(wssd.getXmlId());
			}
		}
		}
		if(pro.getSymbolObjectList() != null){
		for(Symbol s : pro.getSymbolObjectList()){
			if(s.getXmlId() != null
					&& Integer.valueOf(s.getXmlId()) > lastXmlId){
				lastXmlId = Integer.valueOf(s.getXmlId());
			}
		}
		}
		// this list (layerobjectlist) cant be null at this point because at least one layer is obligatory. so either we already created it or the xml wont work
		for(Layer l : pro.getLayerObjectList()){
			if(l.getId() != null
					&& Integer.valueOf(l.getId().trim()) > lastXmlId){
				lastXmlId = Integer.valueOf(l.getId().trim());
			}
		}
		
		wsd.setXmlId(String.valueOf(lastXmlId+1));
	}
	
	private void _putXmlIdToStepsFromAndTo(WStepSequenceDef ssd, WProcessDef pro) {

		if(ssd.getFromStep() != null){
			_setSequenceFromStep(ssd,pro);
			ssd.getMxCell().setSource(ssd.getFromStep().getXmlId());
		} else {
			if(ssd.getProcess().getSymbolObjectList() != null){
				for(Symbol s : ssd.getProcess().getSymbolObjectList()){
					if(s.getLabel() != null
							&& s.getLabel().equalsIgnoreCase("begin")){
						ssd.getMxCell().setSource(s.getXmlId());
					}
				}
			} else {
				
				ssd.getMxCell().setSource(null);
				ssd.getMxCell().setMxGeometry(new MxGeometry(
															null,
															"1",
															"geometry",
															null,
															null,
															ssd.getMxCell().getMxGeometry().getX() + 0,
															ssd.getMxCell().getMxGeometry().getY()+0));
			}
		}
		if(ssd.getToStep() != null){
			_setSequenceToStep(ssd,pro);
			ssd.getMxCell().setTarget(ssd.getToStep().getXmlId());
		} else {
			if(ssd.getProcess().getSymbolObjectList() != null){
				for(Symbol s : ssd.getProcess().getSymbolObjectList()){
					if(s.getLabel() != null
							&& s.getLabel().equalsIgnoreCase("end")){
						ssd.getMxCell().setSource(s.getXmlId());
					}
				}
			} else {
				
				ssd.getMxCell().setTarget(null);
				ssd.getMxCell().setMxGeometry(new MxGeometry(
															null,
															"1",
															"geometry",
															null,
															null,
															ssd.getMxCell().getMxGeometry().getX() + 0,
															ssd.getMxCell().getMxGeometry().getY()+0));
			}
		}
	}
	
	/**
	 * setea correctamente el fromStep a la sequence porque lo vamos modificando en 
	 * el proceso de carga
	 * @param sequence
	 * @param processDef
	 */
	private void _setSequenceFromStep(WStepSequenceDef sequence, WProcessDef processDef){
		
		for (WStepDef currentStepDef: processDef.getSteps()) {
			if (sequence.getFromStep().getId().equals(currentStepDef.getId())){
				sequence.setFromStep(currentStepDef);
				break;
			}
		}
	}
	
	/**
	 * setea correctamente el toStep a la sequence porque lo vamos modificando en 
	 * el proceso de carga
	 * @param sequence
	 * @param processDef
	 */
	private void _setSequenceToStep(WStepSequenceDef sequence, WProcessDef processDef){
		
		for (WStepDef currentStepDef: processDef.getSteps()) {
			if (sequence.getToStep().getId().equals(currentStepDef.getId())){
				sequence.setToStep(currentStepDef);
				break;
			}
		}
	}


	// dml 20130710
	public String getProcessDefXmlMap(Integer processDefId, Integer currentUserId) throws WProcessDefException {
	
		String processMap = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processMap = (String) session.createQuery("Select processMap From WProcessDef Where id = :processDefId")
					.setInteger("processDefId", processDefId)
					.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("HibernateException: getProcessDefXmlMap - can't obtain process xml map = " +
					processDefId + "]  almacenada - "+ex.getMessage()+" "+ex.getCause() );
			throw new WProcessDefException("getLastWProcessDefVersion;  can't obtain process xml map: " + 
					processDefId + " - " + ex.getMessage()+" "+ex.getCause());
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("Exception: getProcessDefXmlMap - can't obtain process xml map = " +
					processDefId + "]  almacenada - "+ex.getMessage()+" "+ex.getCause() );
			throw new WProcessDefException("getLastWProcessDefVersion;  can't obtain process xml map: " + 
					processDefId + " - " + ex.getMessage()+" "+ex.getCause());
		}

		return processMap;
	}	
	
	public List<WProcessDef> getWProcessDefs(Integer currentUserId) throws WProcessDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WProcessDef> processList = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			processList = session.createQuery("From WProcessDef Order By process.name ").list();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("HibernateException: getWProcessDefs() - can't obtain process list - " +
					ex.getMessage()+" "+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao: getWProcessDefs() - can't obtain process list: "
					+ ex.getMessage()+" "+ex.getCause());
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("Exception: getWProcessDefs() - can't obtain process list - " +
					ex.getMessage()+" "+ex.getCause() );
			throw new WProcessDefException("WProcessDefDao: getWProcessDefs() - can't obtain process list: "
					+ ex.getMessage()+" "+ex.getCause());
		}

		return processList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<StringPair> getComboList(
			String firstLineText, String blank, Integer currentUserId )
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
						.createQuery("From WProcessDef Order By process.name ")
						.list();
		
				tx.commit();

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
						+ex.getMessage()+" "+ex.getCause());
			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				throw new WProcessDefException(
						"Can't obtain WProcessDefs combo list "
						+e.getMessage()+" "+e.getCause());				
			}

			return retorno;


	}

	@SuppressWarnings("unchecked")
	public List<StringPair> getComboActiveProcessList(String firstLineText, String blank, Integer currentUserId )
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
						.createQuery("From WProcessDef Where active IS TRUE order by process.name")
						.list();
		
				tx.commit();

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
						+ex.getMessage()+" - "+ex.getCause());
			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				throw new WProcessDefException(
						"Can't obtain WProcessDefs combo list "
						+e.getMessage()+" - "+e.getCause());				
			}

			return retorno;


	}
	
	
	/**
	 * Return a role id list with permissions for given process
	 * For each step belonging the given process recovers the role list with  permissions
	 * unifying it by role Id
	 * 
	 * 			
	 * de la lista de steps del proceso (w_process_step_def) obtengo la lista de roles utilizados,
	 * y filtro por lo que son runtimeRole....3
	 * 
	 * Atención: resuelto con named query 'findProcessRuntimeRoleId'
	 * 
	 * nes 20141206
	 * 
	 * @param idProcess - wProcessDef id (version)
	 * @return - list inteber roles with runtimeRole=true
	 * @throws WProcessDefException 
	 */
	public List<Integer> getProcessRuntimeRoleIds(Integer idProcessDef) throws WProcessDefException {
		logger.debug(">>> getProcessRuntimeRoleIds >> idProcess:"
				+(idProcessDef!=null?idProcessDef:"null"));
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<Integer> retorno=null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			Query query = session.getNamedQuery("findProcessRuntimeRoleId")
					.setInteger("idProcessDef", idProcessDef);
			
			retorno = query.list();

			tx.commit();
			


		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getProcessRuntimeRoleIds() error recovering process runtime role id list... " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():""); 
			logger.error( mess );
			throw new WProcessDefException( mess );
			
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getProcessRuntimeRoleIds() error recovering process runtime role id list... " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
					+ex.getClass(); 
			logger.error( mess );
			throw new WProcessDefException( mess );			

		}
		
		return retorno;		
		
	}
	
	/**
	 * Return a role list with permissions for given process
	 * For each step belonging the given process recovers the role list with  permissions
	 * unifying it by role Id
	 *
	 * de la lista de steps del proceso (w_process_step_def) obtengo la lista de roles utilizados,
	 * y filtro por lo que son runtimeRole....3
	 * 
	 * Atención: resuelto con named query 'findProcessRuntimeRoleId'
	 * 
	 * nes 20141208
	 * 
	 * @param idProcess - wProcessDef id (version)
	 * @return - list WRoleDef roles with runtimeRole=true
	 * @throws WProcessDefException 
	 */
	public List<WRoleDef> getProcessRuntimeRoles(Integer idProcessDef) throws WProcessDefException {
		logger.debug(">>> getProcessRuntimeRoles >> idProcess:"
				+(idProcessDef!=null?idProcessDef:"null"));
		
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		List<WRoleDef> retorno=null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			Query query = session.getNamedQuery("findProcessRuntimeRole")
					.setInteger("idProcessDef", idProcessDef);
			
			retorno = query.list();

			tx.commit();
			


		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: getProcessRuntimeRoles() error recovering process runtime role id list... " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():""); 
			logger.error( mess );
			throw new WProcessDefException( mess );
			
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: getProcessRuntimeRoles() error recovering process runtime role id list... " +
					ex.getMessage()+" "+(ex.getCause()!=null?ex.getCause():"")+" "
					+ex.getClass(); 
			logger.error( mess );
			throw new WProcessDefException( mess );			

		}
		
		return retorno;		
		
	}	

	@SuppressWarnings("unchecked")
	public List<WProcessDef> finderWProcessDef (LocalDate initialInsertDateFilter, LocalDate finalInsertDateFilter, 
			boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
			String listZoneFilter, String workZoneFilter, String additinalZoneFilter,
			Integer userId, boolean isAdmin, String searchOrder, Integer currentUserId ) 
					throws WProcessDefException {

			org.hibernate.Session session = null;
			org.hibernate.Transaction tx = null;
			org.hibernate.Query q = null;
						
			List<WProcessDef> lprocess = null;
			
			// build filter from user params. String and Integer values will be added to
			// the String directly in the string filter.
			// Date parameters must be added to hibernate query in the try / catch clause below
			String userFilter = getSQLFilter(initialInsertDateFilter, finalInsertDateFilter, 
										strictInsertDateFilter, nameFilter, commentFilter, 
										listZoneFilter, workZoneFilter, additinalZoneFilter);
			
			String requiredFilter = "";//getRequiredFilter(userId, isAdmin);
			
			String filter = userFilter + ( userFilter!=null && !"".equals(userFilter) 
								&& requiredFilter!=null && !"".equals(requiredFilter)?" AND ":"" ) 
								+ requiredFilter ;
			
			filter = (( filter != null && !"".equals(filter)) ? " WHERE ":"") + filter;
			
			logger.debug(" ---->>>>>>>>>> userFilter:["+userFilter+"]");
			logger.debug(" ---->>>>>>>>>> requiredFilter:["+requiredFilter+"]");
			logger.debug(" ---->>>>>>>>>> filter:["+filter+"]");
			
			// load base query phrase
			String query = getBaseQuery( isAdmin );
			
			logger.debug(" ---->>>>>>>>>> base query:["+query+"]");

			// builds full query phrase
			query += filter+getSQLOrder(searchOrder);

			logger.debug(" ---->>>>>>>>>> FULL query:["+query+"] ---->>>>>>>>>> userId: "+userId);
			
			try {

				session = HibernateUtil.obtenerSession();
				tx = session.getTransaction();

				tx.begin();
				
				q = session
						.createSQLQuery(query)
						.addEntity("WProcessDef", WProcessDef.class);

				//rrl 20141021 No existen estos parametros insertdateFrom/insertdateTo y da errores
				// setting date parameters
//				try {
//					
//					if (strictInsertDateFilter) {
//
//						DateTimeFormatter fmtShortDate = DateTimeFormat.forPattern(DATE_FORMAT);
//						DateTimeFormatter fmtLongDate = DateTimeFormat.forPattern(DATE_HOUR_COMPLETE_FORMAT);
//						
//						DateTime from = null;
//						DateTime to = null;
//						
//		                from = fmtLongDate.parseDateTime(fmtShortDate.print(initialInsertDateFilter)+" 00:00:00");                
//		                to = fmtLongDate.parseDateTime(fmtShortDate.print(initialInsertDateFilter)+" 23:59:59");                
//						q.setParameter("insertdateFrom", from);
//						q.setParameter("insertdateTo", to);
//					}
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//					logger.error("Error setting date fields to hibernate SQL Query: "
//							+ e.getMessage()+" "+e.getCause());	
//				}
				
				// set userId
				//q.setInteger("userId",userId);
				
				// retrieve list
				lprocess = q.list();
				
				tx.commit();

			} catch (HibernateException ex) {
				if (tx != null)
					tx.rollback();
				String message="WProcessDefDao: 002 getWProcessDefs() - can't obtain process list - " +
						ex.getMessage()+" "+ex.getCause();
				logger.warn(message );
				throw new WProcessDefException(message);

			} catch (Exception ex) {
				if (tx != null)
					tx.rollback();
				String message="WProcessDefDao: 002B getWProcessDefs() - can't obtain process list - " +
						ex.getMessage()+" "+ex.getCause();
				logger.warn(message );
				throw new WProcessDefException(message);
			}
			
			return lprocess;
		}

	private String getRequiredFilter ( Integer userId, boolean isAdmin ) {
		
		String reqFilter = " ( ( wsr.id_role in ";
		reqFilter +="(select wur.id_role from w_user_def wud, w_user_role wur where wur.id_user=:userId ) OR  ";
		reqFilter +=" ( wsu.id_user =:userId ) OR  ";
		reqFilter +=" ( wswa.id_role in ";
		reqFilter +="(select wur.id_role from w_user_def wud, w_user_role wur where wur.id_user=:userId )) OR  ";
		reqFilter +=" ( wswa.id_user =:userId )  ) ) ";
	
		return reqFilter;
	
	}

	private String getBaseQuery(boolean isAdmin) {
	
		String baseQueryTmp="SELECT * FROM w_process_def wpd ";
		baseQueryTmp += " LEFT OUTER JOIN w_process_head wph ON wpd.head_id = wph.id ";
		baseQueryTmp +=" LEFT JOIN w_step_def wsd ON wpd.id_begin=wsd.id ";
	
		return baseQueryTmp;

	}

	private String getFilter(DateTime initialInsertDateFilter, DateTime finalInsertDateFilter, 
		boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
		String listZoneFilter, String workZoneFilter, String additinalZoneFilter ) {

		String filter="";
	
		if ( nameFilter!=null && ! "".equals(nameFilter) ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" wph.name = "+nameFilter;
		}

		if ( commentFilter!=null && ! "".equals(commentFilter) ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" comments = "+commentFilter;
		}

		if ( listZoneFilter!=null && ! "".equals(listZoneFilter) ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" idListZone = "+listZoneFilter;
		}

		if ( workZoneFilter!=null && ! "".equals(workZoneFilter) ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" idWorkZone = "+workZoneFilter;
		}

		if ( additinalZoneFilter!=null && ! "".equals(additinalZoneFilter) ) {
			if ( filter ==null || !"".equals(filter)) {
				filter +=" AND ";
			}
			filter +=" idAdditionalZone = "+additinalZoneFilter;
		}

		if (initialInsertDateFilter!=null){

			if (strictInsertDateFilter) {
				if (!"".equals(filter)) {
					filter+=" AND ";
				}
				filter+=" wpd.insertDate >= :insertdateFrom ";
			} else {
				if (finalInsertDateFilter!=null){
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" (wpd.insertDate >= :insertdateFrom AND wpd.insertDate <= :insertdateTo) ";
				} else {
					if (!"".equals(filter)) {
						filter+=" AND ";
					}
					filter+=" wpd.insertDate <= :insertdateTo ";
				}
			}
		}

		return filter;
	
	}

	private String getSQLFilter(LocalDate initialInsertDateFilter, LocalDate finalInsertDateFilter, 
			boolean strictInsertDateFilter, String nameFilter, String commentFilter, 
			String listZoneFilter, String workZoneFilter, String additinalZoneFilter ) {

		String filter="";

	
		if (nameFilter!=null && ! "".equals(nameFilter)){
			if (filter == ""){
				filter+=" wph.name LIKE '%"+nameFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wph.name LIKE '%"+nameFilter.trim()+"%' ";
			}
		}
		
		if (commentFilter!=null && ! "".equals(commentFilter)){
			if (filter == ""){
				filter+=" wpd.comments LIKE '%"+commentFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wpd.comments LIKE '%"+commentFilter.trim()+"%' ";
			}
		}
	
		if (listZoneFilter!=null && ! "".equals(listZoneFilter)){
			if (filter == ""){
				filter+=" wpd.id_list_zone LIKE '%"+listZoneFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wpd.id_list_zone LIKE '%"+listZoneFilter.trim()+"%' ";
			}
		}
		
		if (workZoneFilter!=null && ! "".equals(workZoneFilter)){
			if (filter == ""){
				filter+=" wpd.id_work_zone LIKE '%"+workZoneFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wpd.id_work_zone LIKE '%"+workZoneFilter.trim()+"%' ";
			}
		}
		
		if (additinalZoneFilter!=null && ! "".equals(additinalZoneFilter)){
			if (filter == ""){
				filter+=" wpd.id_additional_zone LIKE '%"+additinalZoneFilter.trim()+"%' ";
			}
			else { 
				filter+=" AND wpd.id_additional_zone LIKE '%"+additinalZoneFilter.trim()+"%' ";
			}
		}
		
		DateTimeFormatter fmtShortDate = DateTimeFormat.forPattern(DATE_FORMAT);
		DateTimeFormatter fmtLongDate = DateTimeFormat.forPattern(DATE_HOUR_COMPLETE_FORMAT);

		DateTime from = null;
		DateTime to = null;

		if (initialInsertDateFilter != null) {
			if (strictInsertDateFilter) {
				if (!"".equals(filter)) {
					filter += " AND ";
				}
				from = fmtLongDate.parseDateTime(fmtShortDate.print(initialInsertDateFilter)
						+ " 00:00:00");
				to = fmtLongDate.parseDateTime(fmtShortDate.print(initialInsertDateFilter)
						+ " 23:59:59");
				filter += " wpd.insert_date >= '" + from + "' AND wpd.insert_date <= '" + to + "' ";
			} else {
				if (finalInsertDateFilter != null) {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " (wpd.insert_date >= '" + initialInsertDateFilter
							+ "' AND wpd.insert_date <= '" + finalInsertDateFilter + "') ";
				} else {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " wpd.insert_date >= '" + initialInsertDateFilter + "' ";
				}
			}
		}
		
		return filter;
	}


	
	private String getOrder() {
	
		return " ORDER BY insertDate ";
		
	}

	// dml 20120416
	private String getSQLOrder(String searchOrder) {
		
		String ret = "";
		
		if (searchOrder!=null && searchOrder.equals(LAST_ADDED)) {
			ret += " ORDER by wpd.insert_date DESC;";
		} else if (searchOrder!=null && searchOrder.equals(LAST_MODIFIED)) {
			ret += " ORDER by wpd.mod_date DESC;"; 
		} else {
			ret += " ORDER by wpd.id ASC;";
		}
		
		return ret;
		
	}

	public List<WProcessDefLight> finderWProcessDefLight(boolean onlyActiveWorkingProcessesFilter, 
			String processNameFilter, LocalDate initialProductionDateFilter, LocalDate finalProductionDateFilter, 
			boolean strictProductionDateFilter, Integer productionUserFilter, String action, 
			Integer processHeadId, String activeFilter, Integer currentUserId) 
	throws WProcessDefException {
		logger.debug(">>> finderWProcessDefLight");
		
		String filter = "";
		
		filter = buildFinderSQLFilter(onlyActiveWorkingProcessesFilter,
				processNameFilter, initialProductionDateFilter,
				finalProductionDateFilter, strictProductionDateFilter,
				productionUserFilter, filter, processHeadId, activeFilter);
		
		
		if (filter != null && !"".equals(filter)){
			filter = "WHERE " + filter;
		}

		String query = buildFinderSQLQuery(filter, action);
		

		logger.debug("------>> getWorkingProcessListFinder -> query:" + query
				+ "<<-------");

		return getWStepDefLightBySQLQuery(query, currentUserId);
	}

	private String buildFinderSQLFilter(
			boolean onlyActiveWorkingProcessesFilter, String processNameFilter,
			LocalDate initialProductionDateFilter, LocalDate finalProductionDateFilter,
			boolean strictProductionDateFilter, Integer productionUserFilter,
			String filter, Integer processHeadId, String activeFilter) {
		
		if (onlyActiveWorkingProcessesFilter) {
			if (!"".equals(filter)) {
				filter += " AND wpd.active IS TRUE ";
			} else {
				filter += " wpd.active IS TRUE ";

			}
		}
		
		if (processNameFilter != null && !"".endsWith(processNameFilter)){
			if (!"".equals(filter)) {
				filter += " AND wph.name LIKE '%"+processNameFilter+"%'";
			} else {
				filter += " wph.name LIKE '%"+processNameFilter+"%'";

			}			
		}
		
		DateTimeFormatter fmtShortDate = DateTimeFormat.forPattern(DATE_FORMAT);
		DateTimeFormatter fmtLongDate = DateTimeFormat.forPattern(DATE_HOUR_COMPLETE_FORMAT);

		DateTime from = null;
		DateTime to = null;

		if (initialProductionDateFilter != null) {
			if (strictProductionDateFilter) {
				if (!"".equals(filter)) {
					filter += " AND ";
				}
				from = fmtLongDate.parseDateTime(fmtShortDate.print(initialProductionDateFilter)
						+ " 00:00:00");
				to = fmtLongDate.parseDateTime(fmtShortDate.print(initialProductionDateFilter)
						+ " 23:59:59");
				filter += " wpd.production_date >= '" + from + "' AND wpd.production_date <= '" + to + "' ";
			} else {
				if (finalProductionDateFilter != null) {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " (wpd.production_date >= '" + initialProductionDateFilter
							+ "' AND wpd.production_date <= '" + finalProductionDateFilter + "') ";
				} else {
					if (!"".equals(filter)) {
						filter += " AND ";
					}
					filter += " wpd.production_date >= '" + initialProductionDateFilter + "' ";
				}
			}
		}

		if (productionUserFilter != null && productionUserFilter != 0) {
			if (!"".equals(filter)) {
				filter += " AND wpd.production_user = "+productionUserFilter;
			} else {
				filter += " wpd.production_user = "+productionUserFilter;

			}
		}

		// dml 20130508
		if (activeFilter != null
				&& (activeFilter.equals(ACTIVE)
				|| activeFilter.equals(INACTIVE))) {
			if (!"".equals(filter)) {
				filter += " AND ";
			}
			if (activeFilter.equals(ACTIVE)) {
				filter += " wpd.active IS TRUE ";
			} else if (activeFilter.equals(INACTIVE)){
				filter += " wpd.active IS FALSE ";

			}
		}
		
		// dml 20130508
		if (processHeadId != null
				&& !processHeadId.equals(0)) {
			if (!"".equals(filter)) {
				filter += " AND wpd.head_id = " + processHeadId + " ";
			} else {
				filter += " wpd.head_id = " + processHeadId + " ";

			}
		}
		
		logger.debug("QUERY FILTER:" + filter);

		return filter;
	}

	private String buildFinderSQLQuery(String filter, String searchOrder) {

		String tmpQuery = "SELECT ";
		tmpQuery += " wpd.id, ";
		tmpQuery += " wph.name, ";
		tmpQuery += " wpd.comments, ";
		tmpQuery += " wpd.production_date, ";
		tmpQuery += " wpd.production_user, ";
		tmpQuery += " (SELECT COUNT(pw.id) FROM w_process_work pw WHERE pw.end_time IS NULL AND pw.id_process = wpd.id) AS liveWorks, ";
		// nes 20130830 - agregado left join porque quité el campo id_process de la tabla w_step_work
		tmpQuery += " (SELECT COUNT(sw.id) FROM w_step_work sw LEFT OUTER JOIN w_process_work wpw ON sw.id_work = wpw.id WHERE sw.decided_date IS NULL AND wpw.id_process = wpd.id) AS liveSteps, ";
		tmpQuery += " wpd.active, ";
		tmpQuery += " wpd.version, ";
		tmpQuery += " wpd.process_map ";

		tmpQuery += " FROM w_process_def wpd ";
		tmpQuery += " LEFT OUTER JOIN w_process_head wph ON wpd.head_id = wph.id ";

		tmpQuery += filter;

		if (searchOrder!=null && searchOrder.equals(LAST_ADDED)) {
			tmpQuery += " ORDER by wpd.insert_date DESC;";
		} else if (searchOrder!=null && searchOrder.equals(LAST_MODIFIED)) {
			tmpQuery += " ORDER by wpd.mod_date DESC;";  //rrl 20130206 Ajuste antes estaba con i.fecha_modificacion
		} else {
			tmpQuery += " ORDER by wpd.head_id AND wpd.version AND wpd.id ASC;"; // nes 20141021
		}

		logger.debug("QUERY:" + tmpQuery);

		return tmpQuery;
	}

	private List<WProcessDefLight> getWStepDefLightBySQLQuery(String query, Integer currentUserId)
			throws WProcessDefException {

		Integer id;
		String name;
		String comments;
		DateTime productionDate;
		Integer productionUser;
		
		Integer liveWorks;
		Integer liveSteps;
		
		boolean status;
		Integer version;

		String processMap;
		
		Session session = null;
		Transaction tx = null;

		List<Object[]> result = null;
		List<WProcessDefLight> returnList = new ArrayList<WProcessDefLight>();

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();
			tx.begin();

			Hibernate.initialize(result);

			result = session.createSQLQuery(query).list();

			tx.commit();

			if (result != null) {

				for (Object irObj : result) {

					Object[] cols = (Object[]) irObj;


					id = (cols[0] != null ? new Integer(
							cols[0].toString()) : null);
					name = (cols[1] != null ? cols[1].toString() : "");
					comments = (cols[2] != null ? cols[2].toString() : "");
					
					//productionDate = (cols[3] != null ? (Date) cols[3] : null);
					productionDate = (cols[3] != null ? new TimestampColumnDateTimeMapper().fromNonNullValue((Timestamp) cols[3]) : null);
					
					productionUser = (cols[4] != null ? new Integer(
							cols[4].toString()) : null);
					liveWorks = (cols[5] != null ? new Integer(
							cols[5].toString()) : null);
					liveSteps= (cols[6] != null ? new Integer(
							cols[6].toString()) : null);
					status = (cols[7] != null ? (Boolean) cols[7] : false);
					version= (cols[8] != null ? new Integer(cols[8].toString()) : null);
					processMap = (cols[9] != null ? cols[9].toString() : "");

					returnList.add(new WProcessDefLight(id, name, comments, productionDate, 
							productionUser, liveWorks, liveSteps, status, version, processMap));
				}

			} else {

				returnList = null;
			}

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.error("HibernateException: getWorkingProcessListByFinder() - Can't get the WProcessDefLight list - "
					+ ex.getMessage()
					+ " "
					+ ex.getLocalizedMessage()
					+ " "
					+ ex.getCause());
			throw new WProcessDefException(ex);
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			logger.error("Exception: getWorkingProcessListByFinder() - Can't get the WProcessDefLight list - "
					+ ex.getMessage()
					+ " "
					+ ex.getLocalizedMessage()
					+ " "
					+ ex.getCause());
			throw new WProcessDefException(ex);
		}

		return returnList;
	}
	
	// dml 20130129
	public boolean userIsProcessAdmin(Integer userId, Integer processId, Integer currentUserId)
			throws WProcessDefException {

		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		String result = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			String query = " SELECT ";
			query += " IF (" + userId + " IN ";
			query += " (SELECT id_user FROM w_process_user WHERE id_process = " + processId
					+ " AND admin=true )";
			query += " OR ";
			query += " (SELECT COUNT(*) FROM w_process_role pr LEFT JOIN w_user_role ur ON pr.id_role=ur.id_role";
			query += " WHERE pr.admin = true AND ur.id_user= " + userId
					+ " ), 'YES','NO' ) as ADMINISTRATOR ";

			result = (String) session.createSQLQuery(query).uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			logger.warn("WProcessDefDao: userIsProcessAdmin() - can't obtain process list - "
					+ ex.getMessage() + " " + ex.getCause());
			throw new WProcessDefException(
					"WProcessDefDao: userIsProcessAdmin() - can't obtain process list: "
							+ ex.getMessage() + " " + ex.getCause());

		}

		if (result != null && !"".equals(result)) {
			if (result.equals("YES")) {
				return true;
			} else if (result.equals("NO")) {
				return false;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	/**
	 * returns true if there is 1 or more process versions for this process head id
	 * or false if doesn't have any version
	 *
	 * @param  Integer processHeadId
	 * @return boolean
	 */
	public boolean hasVersions(Integer processHeadId) throws WProcessDefException {

		BigInteger qtyExistingProcesses = null;
		org.hibernate.Session session = null;
		org.hibernate.Transaction tx = null;

		try {

			session = HibernateUtil.obtenerSession();
			tx = session.getTransaction();

			tx.begin();

			qtyExistingProcesses = 
					(BigInteger) session.createSQLQuery("SELECT COUNT(id) as count FROM w_process_def WHERE head_id = " + processHeadId)
											.uniqueResult();

			tx.commit();

		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			String mess = "HibernateException: existsProcessVersions - can't obtain count of process for process head id = " +
								processHeadId + "]  "+ex.getMessage()+" "+ex.getCause();
			logger.warn( mess );
			throw new WProcessDefException(mess);
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			String mess = "Exception: existsProcessVersions - can't obtain count of process for process head id = " +
								processHeadId + "]  "+ex.getMessage()+" "+ex.getCause();
			logger.warn( mess );
			throw new WProcessDefException(mess);
		}

		if (qtyExistingProcesses == null || qtyExistingProcesses.intValue()==0){
			return false;
		}
		else {
			return true;
		}
	
	}

}