package org.beeblos.bpm.wc.taglib.bean;

import static org.beeblos.bpm.core.util.Constants.DEFAULT_MOD_DATE;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.bl.WProcessWorkBL;
import org.beeblos.bpm.core.bl.WStepWorkBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WProcessWorkException;
import org.beeblos.bpm.core.error.WStepDefException;
import org.beeblos.bpm.core.error.WStepWorkException;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.noper.BeeblosAttachment;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.HelperUtil;
import org.beeblos.bpm.wc.taglib.util.WStepDefUtil;
import org.beeblos.bpm.wc.taglib.util.WStepWorkUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;


public class WProcessWorkFormBean extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(WProcessWorkFormBean.class);
	
	private static final String MANAGED_BEAN_NAME = "wProcessWorkFormBean";

    private Integer currentUserId;
    
	private WProcessWork currentWProcessWork; 
	
	private Integer currObjId; // current object managed by this bb

	private TimeZone timeZone;

	private BeeblosAttachment attachment;
	private String documentLink;
	
	private List<WStepWork> wStepWorkList;
	
	// dml 20120201
	private Integer idStep;
	private Integer idStepWork;
	
	// dml 20120202
	private Date initialDateFilter;
	private Date finalDateFilter;

    public static WProcessWorkFormBean getCurrentInstance() {
        return (WProcessWorkFormBean) FacesContext.getCurrentInstance().getExternalContext()
            .getRequestMap().get(MANAGED_BEAN_NAME);
    }

	public WProcessWorkFormBean() {		
		super();
		
		init();  
	}
    
	public void init(){
		super.init();
		
		setShowHeaderMessage(false);  
		
		_reset();
	}

	public void _reset() {
	
		this.currObjId=null;
		this.currentWProcessWork=null;
		
		this.initialDateFilter = null;
		this.finalDateFilter = null;
		
		attachment = new BeeblosAttachment();

		documentLink=null; 
		
		HelperUtil.recreateBean("documentacionBean", "com.softpoint.taglib.common.DocumentacionBean");
		
		wStepWorkList = new ArrayList<WStepWork>();

	
	}
	
	
	// SET EMTPY OBJECTS OF CURRENT OBJECT TO NULL TO AVOID PROBLEMS
	// WITH HIBERNATE RELATIONS AND CASCADES
	private void setModel() {
		
		if(currentWProcessWork != null){
//			if ( currentObject.getPropertyObjectType1()!=null && 
//					currentObject.getPropertyObjectType1().empty() ) {
//				currentObject.SetPropertyObjectType1( null );
//			}
			
		}
	}
	
	// CREATE NULL PROPERTIES OF OBJECT TYPE TO AVOID PROBLEMS
	// WITH VIEW AND ITS REFERENES TO THESE OBJECTS ...
	private void recoverNullObjects(){
		
		if(currentWProcessWork != null){
			
//			if ( currentObject.getPropertyObjectType1 == null) {
//				currentObject.setPropertyObjectType1(new ObjectType1(VACIO));
//			}
			
		}
		
	}
	
	// checks input data before save or update
	private boolean checkInputData() {
		
		boolean result = false;
		
		return result;
	}
	
	private void persistCurrentWProcessWork() throws WProcessWorkException {
		WProcessWorkBL wpwBL = new WProcessWorkBL();
		this.setModel();
		wpwBL.update(currentWProcessWork, getCurrentUserId());
		this.recoverNullObjects();
	}


	public String cancel(){
		
		_reset();
		return null;
	}

	
    public String save_continue() {

    	String result =  null;
				
//		if ( checkInputData() ) {
//			
//			try {
//				
//				result = add();
//				createNullObjectTypeProperties(); //<<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>
//				
//			} catch (ObjectException e) {
//				createNullObjectTypeProperties();
//				result =  null;
//			}
//		}

		return result;
	}
	
	public String save() {
		
		String result =  null;
		
		if ( checkInputData() ) {
			
			try {
				
				result = add();
				_reset();
				
			} catch (WProcessWorkException e) {
				recoverNullObjects(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>
				result =  null;
			}
		}

		return result;
	}
	

	
	
	public String add() throws WProcessWorkException {		
		logger.debug("WProcessWorkFormBean: add: wProcessWorkId:["+this.currObjId+"] ");
		
		setShowHeaderMessage(false);

		String ret = null;
		
		// if object already exists in db then update and return
		if(currentWProcessWork!=null 
//				&& currentObject.getId()!=null && currentObject.getId()!=0 
				){ 

			// before update store document in repository ( if exists )
			if (attachment.getDocumentoNombre()!=null && 
					!"".equals(attachment.getDocumentoNombre())) {
				//storeInRepository(); 
			}
			
			return update();
		}
		
		WProcessWorkBL wpwBL = new WProcessWorkBL();
	
		try {	
			
			setModel(); // <<<<<<<<<<<<<<<<<<<< IMPORTANT >>>>>>>>>>>>>>>>>>

			currObjId = wpwBL.add(currentWProcessWork, this.getCurrentUserId() );
			
			// Manual process to store attachment in the repository ( if attachment exists ...
			if (attachment.getDocumentoNombre()!=null && !"".equals(attachment.getDocumentoNombre())) {
				//storeInRepository();
				update();
			}
			
			recoverNullObjects();
			
			ret="OK";
			setShowHeaderMessage(true);
			
		} catch (WProcessWorkException ex1) {

			String message = ex1.getMessage() + " - "+ ex1.getCause();
			String params[] = {message + ",", ".Please confirm input values." };				
			agregarMensaje("208",message,params,FGPException.ERROR);	
			
			throw new WProcessWorkException(message);			
			
		} catch (Exception e) {
			
			String message = e.getMessage() + " - "+ e.getCause();
			String params[] = {message + ",", ".Error inserting object ..." };
			agregarMensaje("208",message,params ,FGPException.ERROR);
			
			throw new WProcessWorkException(message);			
			
		}

		return ret;
	}	
	

	
	public String update() {
		logger.debug("update(): currentId:"
				+ currentWProcessWork.getId() + " - "
				+ currentWProcessWork.getReference());
		
		String ret = null;

		WProcessWorkBL wpwBL = new WProcessWorkBL();

		try {
			
			setModel();
			
			wpwBL.update(currentWProcessWork, this.getCurrentUserId() );
			
			recoverNullObjects();
			
			setShowHeaderMessage(true);
			ret="OK";

		} catch (WProcessWorkException ex1) {

			String message = "Error updating object: "
					+ currentWProcessWork.getId()
					+ " - "
					+ currentWProcessWork.getReference()
					+ "\n"
					+ ex1.getMessage() + "\n" + ex1.getCause();
			
			String params[] = {message + ",", ".Please confirm input values." };				
			agregarMensaje("208",message,params,FGPException.ERROR);	

			logger.error(message);
			
		}
		
		return ret;
	}
	

	// load an Object in currentObject
	public void loadWProcessWork(Integer objectId){
		
			this.currObjId=objectId;
			this.loadWProcessWork();
			
	}
	
	public void loadWProcessWork() {
		logger.debug("loadWProcessWork()");
		
		WProcessWorkBL wpwBL = new WProcessWorkBL();

		try {
			currentWProcessWork= 
					wpwBL
						.getWProcessWorkByPK(this.currObjId, this.getCurrentUserId() );
			
			if (currentWProcessWork!=null) {
				
				currObjId=currentWProcessWork.getId();
				
				loadWStepWorkList();
				// etc etc
			}

		} catch (WProcessWorkException ex1) {

			String message = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = {
					message + ",",
					".Error loading current WProcessWork ..."
							+ currentWProcessWork.getId() };
			agregarMensaje("208", message, params, FGPException.ERROR);

			logger.error(message);

		}			
	}
	
	private void loadWStepWorkList(){
		
		try {

			if (this.currObjId != null && this.currObjId != 0) {

				setwStepWorkList(new WStepWorkBL().getStepListByIdWork(currObjId, getCurrentUserId()));

			}
			
		} catch (WStepWorkException ex1) {

			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".loadWStepWorkList() WStepSequenceDefException ..." };
			agregarMensaje("208", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();

		} catch (WProcessDefException ex1) {

			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".loadWStepWorkList() WProcessDefException ..." };
			agregarMensaje("208", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();

		} catch (WStepDefException ex1) {

			String mensaje = ex1.getMessage() + " - " + ex1.getCause();
			String params[] = { mensaje + ",",
					".loadWStepWorkList() WStepDefException ..." };
			agregarMensaje("208", mensaje, params, FGPException.ERROR);
			ex1.printStackTrace();

		}
		
	}


		

	public WProcessWork getCurrentWProcessWork() {
		return currentWProcessWork;
	}

	public void setCurrentWProcessWork(WProcessWork currentWProcessWork) {
		this.currentWProcessWork = currentWProcessWork;
	}

	public Integer getCurrObjId() {
		return currObjId;
	}

	public void setCurrObjId(Integer currObjId) {
		this.currObjId = currObjId;
	}

	public BeeblosAttachment getAttachment() {
		return attachment;
	}

	public void setAttachment(BeeblosAttachment attachment) {
		this.attachment = attachment;
	}

	public String getDocumentLink() {
		return documentLink;
	}

	public void setDocumentLink(String documentLink) {
		this.documentLink = documentLink;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public Integer getCurrentUserId() {
		if ( currentUserId== null ) {
			ContextoSeguridad cs = (ContextoSeguridad)
						getSession().getAttribute(SECURITY_CONTEXT);
			if (cs!=null) currentUserId=cs.getIdUsuario();
		}
		return currentUserId;
	}	

	public TimeZone getTimeZone() {
		//Si se pone GMT+1 pone mal el dia 
		return java.util.TimeZone.getDefault();
	}
	
	public List<WStepWork> getwStepWorkList() {
		return wStepWorkList;
	}

	public void setwStepWorkList(List<WStepWork> wStepWorkList) {
		this.wStepWorkList = wStepWorkList;
	}

	public Integer getIdStep() {
		return idStep;
	}

	public void setIdStep(Integer idStep) {
		this.idStep = idStep;
	}

	public Integer getIdStepWork() {
		return idStepWork;
	}

	public void setIdStepWork(Integer idStepWork) {
		this.idStepWork = idStepWork;
	}

	public Date getInitialDateFilter() {
		return initialDateFilter;
	}

	public void setInitialDateFilter(Date initialDateFilter) {
		this.initialDateFilter = initialDateFilter;
	}

	public Date getFinalDateFilter() {
		return finalDateFilter;
	}

	public void setFinalDateFilter(Date finalDateFilter) {
		this.finalDateFilter = finalDateFilter;
	}

	// HZC:20110215, generar imagen de la tareas
	// HZC:20110216, esto es diagrama gantt, se muestra tal como esta en la
	// grilla de datos
	@SuppressWarnings("unchecked")
	public void paint(OutputStream stream, Object object) throws IOException {
		
		List<WStepWork> workProcesses = applyFilters((List<WStepWork>) object);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// int contNombreRepetidoFin = 1;
		// int contNombreRepetidoPen = 1;
		int countIndex = 1;

		// Ini:Dataset
		TaskSeries tsf = new TaskSeries("Processed");
		TaskSeries tsp = new TaskSeries("Pending");

		// //HZC:20110216, generar nombre de tarea único
		for (WStepWork l : workProcesses) {
			if (l.getDecidedDate() != null) {
				// nombre unicos
				// Existe tareas q vuelven a realizar(ojo) q no se estan
				// añadiendo al grafico
				String currentStepName = countIndex + "-"
						+ l.getCurrentStep().getName();
				tsf.add(new Task(currentStepName, new SimpleTimePeriod(l
						.getArrivingDate(), l.getDecidedDate())));
				// contNombreRepetidoFin++;
			} else {
				// nombre unico
				// Existe tareas q vuelven a realizar(ojo) q no se estan
				// añadiendo al grafico
				String currentStepName = countIndex + "-"
						+ l.getCurrentStep().getName();
				tsp.add(new Task(currentStepName, new SimpleTimePeriod(l
						.getArrivingDate(), new Date())));
				// contNombreRepetidoPen++;
			}
			countIndex++;
		}

		TaskSeriesCollection collection = new TaskSeriesCollection();

		if (!tsf.isEmpty()) {
			collection.add(tsf);
		}
		if (!tsp.isEmpty()) {
			collection.add(tsp);
		}
		// Fin:Dataset

		// 20110217:titulo del proceso, dado q todos los pasos pertenence al
		// mismo proceso
		// he cogido el primer elemento de la lista
		String tituloChart = workProcesses.get(0).getProcess().getName();

		JFreeChart chart = ChartFactory.createGanttChart(tituloChart, // 20110217:Nombre
																		// del
																		// proceso
				"Steps", "", null, true, true, true);
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setDataset(collection);

		CategoryItemRenderer renderer = plot.getRenderer();
		renderer.setSeriesPaint(0, Color.blue);
		renderer.setSeriesPaint(1, Color.red);

		BufferedImage img = chart.createBufferedImage(640, 450, null);
		;

		try {
			ImageIO.write(img, "png", out);
		} catch (IOException e) {
		}

		stream.write(out.toByteArray());
		stream.flush();
		stream.close();
	}
	
	public List<WStepWork> getProcessedStepList() throws WProcessDefException, WStepDefException, WStepWorkException{
		
		List<WStepWork> processedStepList = new ArrayList<WStepWork>();

		for (WStepWork wsw : wStepWorkList){
		
			if (wsw.getDecidedDate() != null ){
				processedStepList.add(wsw);
			}
		
		}
		
		return processedStepList;
		
	}
	
	public List<WStepWork> getPendingStepList() throws WProcessDefException, WStepDefException, WStepWorkException{

		List<WStepWork> pendingStepList = new ArrayList<WStepWork>();

		for (WStepWork wsw : wStepWorkList){
		
			if (wsw.getDecidedDate() == null ){
				pendingStepList.add(wsw);
			}
		
		}
		
		return pendingStepList;

	}

	public boolean isPendingStepListEmpty(){
		
		for (WStepWork wsw : wStepWorkList){
			
			if (wsw.getDecidedDate() == null ){
				
				return false;
			
			}
		
		}
		
		return true;
		
	}
	
	// dml 20120201
	public void loadWStepWorkForm() {

		new WStepWorkUtil().loadWStepWorkFormBean(idStepWork);

	}

	// dml 20120201
	public String loadWStepDefForm() {

		return new WStepDefUtil().loadWStepDefFormBean(idStep);
		
	}
	
	// dml 20120202
	public List<WStepWork> applyFilters(List<WStepWork> graphicList){
		
		List<WStepWork> workProcesses = new ArrayList<WStepWork>();
		
		boolean onlyFinalDateFilter = false;
		
		if (initialDateFilter != null || finalDateFilter != null){
		
			if (initialDateFilter == null){
				initialDateFilter = DEFAULT_MOD_DATE;
				onlyFinalDateFilter = true;
			}
			
			if (finalDateFilter == null){
				finalDateFilter = new Date();
			}
			
			for (WStepWork wsw : graphicList){
				
				if (wsw.getDecidedDate() == null && !onlyFinalDateFilter){
					if (initialDateFilter.before(wsw.getArrivingDate())){
						workProcesses.add(wsw);
						continue;
					}
				} else if (wsw.getDecidedDate() == null && onlyFinalDateFilter) {
					continue;
				}
				
				if (initialDateFilter.before(wsw.getArrivingDate())){
					
					if (finalDateFilter.after(wsw.getDecidedDate())){
						
						workProcesses.add(wsw);
						
					}
				
				}
			
			}
					
			return workProcesses;

		} else {
			
			return graphicList;
			
		}
	}


}