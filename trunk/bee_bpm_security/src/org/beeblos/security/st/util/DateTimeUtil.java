package org.beeblos.security.st.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeUtil {

	// Devuelve el datetime de la primera hora del dia
	public static Date getDayFirstDateTime(Date actualDate){
		
		// La fecha inicial desde las 00:00
		Calendar gregDate = new GregorianCalendar() ;
		gregDate.setTimeInMillis(actualDate.getTime());
		
		gregDate.set(Calendar.HOUR_OF_DAY, gregDate.getActualMinimum(Calendar.HOUR_OF_DAY));
		gregDate.set(Calendar.MINUTE, gregDate.getActualMinimum(Calendar.MINUTE));
		gregDate.set(Calendar.SECOND, gregDate.getActualMinimum(Calendar.SECOND));
		
		Date date = new Date();
		date.setTime(gregDate.getTimeInMillis());
		
		return date;
	}
	
//	public static Date getDayFirstDateTime(Date actualDate){
//		
//		Date d = new Date();
//		
//		d.setTime(actualDate.getTime());
//		// La fecha inicial desde las 00:00
//		d.setHours(0);
//		d.setMinutes(0);
//		d.setSeconds(0);
//				
//		return d;
//	}
	
//	// Devueve el datetime de la ultima hora del dia
//	public static Date getDayLastDateTime(Date actualDate){
//		
//		Calendar gregDate = new GregorianCalendar() ;
//		gregDate.setTimeInMillis(actualDate.getTime());
//		
//		gregDate.set(Calendar.HOUR_OF_DAY, gregDate.getActualMaximum(Calendar.HOUR_OF_DAY));
//		gregDate.set(Calendar.MINUTE, gregDate.getActualMaximum(Calendar.MINUTE));
//		gregDate.set(Calendar.SECOND, gregDate.getActualMaximum(Calendar.SECOND));
//		
//		Date date = new Date();
//		date.setTime(gregDate.getTimeInMillis());
//	
//		
//		return date;
//	}
//	
	// Devueve el datetime de la ultima hora del dia
	public static Date getDayLastDateTime(Date actualDate){
		
		Date d = new Date();
		
		d.setTime(actualDate.getTime());
		// La fecha inicial desde las 00:00
		d.setHours(0);
		d.setMinutes(0);
		d.setSeconds(0);
		long time = d.getTime() + (24 * 3600000);
		d.setTime(time);
		
		return d;
		
	}
	
	
	// Devueve el datetime de la primera hora del primer dia de la semana
	public static Date getWeekFirstDateTime(Date actualDate){
		
		Calendar gregDate = new GregorianCalendar() ;
		gregDate.setTime(actualDate);
		
		gregDate.set(Calendar.HOUR_OF_DAY, 0);
		gregDate.set(Calendar.MINUTE, 0);
		gregDate.set(Calendar.SECOND, 0);
		
		//rrl 20110929 NOTA:... Creo que deberia de ser "getActualMinimum", he implementado y utilizado getFirstDayThisWeekDateTime
		gregDate.set(Calendar.DAY_OF_WEEK, gregDate.getActualMaximum(GregorianCalendar.DAY_OF_WEEK));
		Date date = new Date();
		date.setTime(gregDate.getTimeInMillis());
		
		return date;
				
	}
	
	// Devueve el datetime de la ultima hora del ultimo dia de la semana
	public static Date getWeekLastDateTime(Date actualDate){
		
		Calendar gregDate = new GregorianCalendar() ;
		gregDate.setTime(actualDate);
		
		gregDate.set(Calendar.HOUR_OF_DAY, gregDate.getActualMaximum(GregorianCalendar.HOUR_OF_DAY));
		gregDate.set(Calendar.MINUTE, gregDate.getActualMaximum(GregorianCalendar.MINUTE));
		gregDate.set(Calendar.SECOND, gregDate.getActualMaximum(GregorianCalendar.SECOND));
		
		//TODO Cuidado que esta semana va de domingo a sabado
		int lastDayWeek = gregDate.getActualMaximum(GregorianCalendar.DAY_OF_WEEK);
		gregDate.set(Calendar.DAY_OF_WEEK, lastDayWeek);
		return gregDate.getTime();
				
	}
	
	
	// Devueve el datetime de la primera hora del primer dia del mes
	public static Date getMonthFirstDateTime(Date actualDate){
		
		Calendar gregDate = new GregorianCalendar() ;
		gregDate.setTime(actualDate);
		
		gregDate.set(Calendar.HOUR_OF_DAY, 0);
		gregDate.set(Calendar.MINUTE, 0);
		gregDate.set(Calendar.SECOND, 0);
		
		gregDate.set(Calendar.DAY_OF_MONTH, 1);
		return gregDate.getTime();
				
	}
	
	// Devueve el datetime de la ultima hora del ultimo dia del mes
	public static Date getMonthLastDateTime(Date actualDate){
		
		Calendar gregDate = new GregorianCalendar() ;
		gregDate.setTime(actualDate);
		
		gregDate.set(Calendar.HOUR_OF_DAY, gregDate.getActualMaximum(GregorianCalendar.HOUR_OF_DAY));
		gregDate.set(Calendar.MINUTE, gregDate.getActualMaximum(GregorianCalendar.MINUTE));
		gregDate.set(Calendar.SECOND, gregDate.getActualMaximum(GregorianCalendar.SECOND));
		
		int lastDayMonth = gregDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		gregDate.set(Calendar.DAY_OF_MONTH, lastDayMonth);
		return gregDate.getTime();
				
	}
	
	// Devueve string con la fecha para comparar en MySQL
	public static String getSimpleDate(Date actualDate){
		
		String simpleDate= "";
		Integer year = actualDate.getYear();
		Integer mounth = actualDate.getMonth();
		Integer day = actualDate.getDay();
		
		simpleDate = year.toString() + "-" +mounth.toString()+"-"+ day.toString();
		
		return simpleDate;
		
	}
	
	//rrl 20110929
	// Devueve el datetime de la primera hora del Lunes de esta semana
	public static Date getFirstDayThisWeekDateTime(Date actualDate){
		
		Calendar gregDate = Calendar.getInstance();
		gregDate.setTime(actualDate);

	    // Si hoy es domingo ajuste para obtener el pasado lunes
        if (gregDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
        	gregDate.add(Calendar.DATE, Calendar.MONDAY - gregDate.get(Calendar.DAY_OF_WEEK));
        	gregDate.add(Calendar.DATE, -7);
        } else {
        	gregDate.add(Calendar.DATE, Calendar.MONDAY - gregDate.get(Calendar.DAY_OF_WEEK));
        }                        
        
		gregDate.set(Calendar.HOUR_OF_DAY, 0);
		gregDate.set(Calendar.MINUTE, 0);
		gregDate.set(Calendar.SECOND, 0);
        
		// Esta es la fecha del lunes de la actual semana  
		return gregDate.getTime();
	}
	
	//rrl 20110929
	// Devueve el datetime de la primera hora del Domingo de esta semana
	public static Date getLastDayThisWeekDateTime(Date actualDate) {
		
		Calendar gregDate = Calendar.getInstance();
	    gregDate.setTime(actualDate);
	    
		int dayofweek = gregDate.get(Calendar.DAY_OF_WEEK);
	    
	    // Si hoy es domingo ajuste para obtener el proximo domingo
//		if (dayofweek == Calendar.SUNDAY) {
//		    gregDate.add(Calendar.DATE, 2);
//		    dayofweek = gregDate.get(Calendar.DAY_OF_WEEK);
//		}
		
	    // Si hoy es lunes ajuste para el calculo
		if (dayofweek == Calendar.MONDAY) {
		    gregDate.add(Calendar.DATE, 1);
		    dayofweek = gregDate.get(Calendar.DAY_OF_WEEK);
		}
		
		// Calculo cuanto se debo añadir, 1 es la diferencia entre el domingo y el lunes				
	    int days = (Calendar.SATURDAY - dayofweek + 1) % 7;
	    gregDate.add(Calendar.DAY_OF_YEAR, days);
		    
		gregDate.set(Calendar.HOUR_OF_DAY, 0);
		gregDate.set(Calendar.MINUTE, 0);
		gregDate.set(Calendar.SECOND, 0);
	    
		// Esta es la fecha del domingo que viene  
		return gregDate.getTime();
	}

	//rrl 20110929
	// Devueve el datetime de la primera hora de mañana
	public static Date getTomorrowDayDateTime(Date actualDate) {
		
		Calendar gregDate = Calendar.getInstance();
		gregDate.setTime(actualDate);
		gregDate.add(Calendar.DATE, 1);
		
		gregDate.set(Calendar.HOUR_OF_DAY, 0);
		gregDate.set(Calendar.MINUTE, 0);
		gregDate.set(Calendar.SECOND, 0);
		
		// Esta es la fecha de mañana  
		return gregDate.getTime();
	}

	
	
}
