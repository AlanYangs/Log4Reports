package com.yag.base;

import com.yag.utils.DataConfig;

public class Configurations {
	
	public static int logType = 0;//日志类型 ，默认为extentreports， 0 = extentreports， 1 = log4j
	public static int retryTimes = 0;
	
	public static String smtpHost;
	public static String sender;
	public static String sendPassword;
	
	public static String receivers;
	public static String subject;
	private static DataConfig dataCfg = new DataConfig(System.getProperty("user.dir") + "\\configs\\config.properties");

	public Configurations(){
		
	}
	
	public static void init(){
		if(dataCfg.read("logType").equals("")){
			logType = 0;
		}else{
			logType = Integer.parseInt(dataCfg.read("logType"));
		}
		
		if(! dataCfg.read("retryTimes").equals("")){
			retryTimes = Integer.parseInt(dataCfg.read("retryTimes"));
		}
		
		if(dataCfg.read("smtpHost").equals("")){
			smtpHost = "smtp.163.com";
		}else{
			smtpHost = dataCfg.read("smtpHost");
		}
		sender = dataCfg.read("sender");
		sendPassword = dataCfg.read("sendPassword");
		
		receivers = dataCfg.read("receivers");
		subject = dataCfg.read("subject").equals("") ? "No Title Test Report Generated By Log4Reports" : dataCfg.read("subject");
		
	}
}
