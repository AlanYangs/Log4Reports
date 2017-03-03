package com.yag.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * Created by yangangui on 2017/1/5.
 * @author yangangui
 *
 */
public class DateUtil {
	
	public static void main(String[] args){
		System.out.println(getNewDate(-7));
	}
	
	public static String getNow(String format){
		Date date = new Date();
		return new SimpleDateFormat(format).format(date);
	}
	
	public static String getNewDate(int days){
		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, days); 
        return (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
	}
	
	public static String getTimestamp(){
		String curDate = getNow("yyyy-MM-dd");
		Calendar curCalendar = Calendar.getInstance();
		int hour = curCalendar.get(Calendar.HOUR_OF_DAY);//24H
		int minute = curCalendar.get(Calendar.MINUTE);
		int second = curCalendar.get(Calendar.SECOND);
		
		return curDate + "_" + hour + "_" + minute + "_" + second;
	}
}
