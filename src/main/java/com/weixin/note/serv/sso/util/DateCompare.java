package com.weixin.note.serv.sso.util;

import java.util.Calendar;
import java.util.Date;

public class DateCompare {
	/**
	 * 比较两个日期相
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean sameDate(Date d1, Date d2) {  
	    if(null == d1 || null == d2)  
	        return false;  
	    Calendar cal1 = Calendar.getInstance();  
	    cal1.setTime(d1);  
/*	    cal1.set(Calendar.HOUR_OF_DAY, 0);  
	    cal1.set(Calendar.MINUTE, 0);  
	    cal1.set(Calendar.SECOND, 0);  */
	    cal1.set(Calendar.MILLISECOND, 0);  
	    Calendar cal2 = Calendar.getInstance();  
	    cal2.setTime(d2);  
/*	    cal2.set(Calendar.HOUR_OF_DAY, 0);  
	    cal2.set(Calendar.MINUTE, 0);  
	    cal2.set(Calendar.SECOND, 0); */ 
	    cal2.set(Calendar.MILLISECOND, 0);  
	    return  cal1.getTime().equals(cal2.getTime());  
	}  
}
