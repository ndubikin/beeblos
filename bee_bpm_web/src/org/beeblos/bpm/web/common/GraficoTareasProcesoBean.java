package org.beeblos.bpm.web.common;

import static org.beeblos.bpm.web.util.ConstantsWeb.DEFAULT_ENCODING;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;


public class GraficoTareasProcesoBean extends CoreManagedBean {

	private static final long serialVersionUID = 6367534440104091636L;
	
	static List<WStepWork> listaTareasProceso = new ArrayList<WStepWork>();
	
//	UploadedFile file = null;
	 ByteArrayOutputStream out = new ByteArrayOutputStream();
	
	public GraficoTareasProcesoBean(){
		
	}

	public void generarGraficoTareasProceso() {
		
        JFreeChart chart = createGanttChart();
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setDataset(createDataset());

        //HZC:20110211, exporta a fichero
        BufferedImage img = chart.createBufferedImage(1024, 760, null);
        
        //HZC:20110211, genere un byteArray para presentar en la pantalla
       
        try {
			ImageIO.write(img, "png", out);
		} catch (IOException e) {
		}
		
	}
	


    private static JFreeChart createGanttChart() {

        // create the chart...
        return ChartFactory.createGanttChart(
            "Tareas Procesos",
            "Tareas", "",
            createDataset(),
            true,     // include legend
            true,
            true
        );

    }
    
    public static IntervalCategoryDataset createDataset() {

    	//HZC: Considerando si la fecha de getDecidedDate== null --> en proceso
    	//caso contrario terminados o finalizado
    	TaskSeries tsf = new TaskSeries("Finalizado");
        TaskSeries tsp = new TaskSeries("Pendiente");

        for(WStepWork l: listaTareasProceso){
        	if(l.getDecidedDate()!=null){
            	tsf.add(new Task(l.getCurrentStep().getName(), new SimpleTimePeriod(l.getArrivingDate(), l.getDecidedDate())));
        	}else{
            	tsp.add(new Task(l.getCurrentStep().getName(), new SimpleTimePeriod(l.getArrivingDate(), l.getArrivingDate())));
        	}
        }

        TaskSeriesCollection collection = new TaskSeriesCollection();
        
        if(!tsf.isEmpty()){
        	collection.add(tsf);
        }
        if(!tsp.isEmpty()){
        	collection.add(tsp);
        }
        return collection;
    }

    /**
     * Utility method for creating <code>Date</code> objects.
     *
     * @param day  the date.
     * @param month  the month.
     * @param year  the year.
     *
     * @return a date.
     */
//    private static Date date(int day, int month, int year) {
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, month, day);
//        Date result = calendar.getTime();
//        return result;
//
//    }
    
	//HZC:20110211, generar imagen de la tareas
	//si es por disposicion no es ncesario este metodo
    public void paint(OutputStream stream, Object object) throws IOException { 
        stream.write(out.toByteArray());
    }	
	
	public List<WStepWork> getListaTareasProceso() {
		return listaTareasProceso;
	}

	public void setListaTareasProceso(List<WStepWork> listaTareasProceso) {
		GraficoTareasProcesoBean.listaTareasProceso = listaTareasProceso;
	}
	
	
	
}
