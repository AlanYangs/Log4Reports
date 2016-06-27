package com.yag.logs;

import static com.yag.utils.CommonUtil.deleteDir;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.yag.base.Configurations;
import com.yag.utils.CommonUtil;
import com.yag.utils.DataConfig;
import com.yag.utils.DateUtil;

public class LogUtil {
	private static LogUtil Log;
	private static Class<?> clazz;
	private Logger logger;
	private static String baseFolder = new DataConfig(System.getProperty("user.dir") + 
			"\\configs\\config.properties").read("sharedFolder");
	public static String sharedFolder =  CommonUtil.getFileName(baseFolder);
	public static String log4jFolder = sharedFolder + "\\logs\\";
	public static String log4jPath = sharedFolder + "\\logs\\output.log";
	private static String caseName = "";
	public static String extentFolder = sharedFolder + "\\reports\\";
	public static String logPath = sharedFolder + "\\reports\\reports.html";
	public static String snapshotFolder = sharedFolder + "\\reports\\snapshot\\";
	private static ExtentReports report = new ExtentReports(logPath,false);
	private ExtentTest testLog;
	
	public int failCount = 0;
	public int errorCount = 0;
	public int warnCount = 0;
	public int skipCount = 0;

	public LogUtil(String name){
		caseName = name;
		File configFile = new File(System.getProperty("user.dir") + "\\configs\\extent-config.xml");
		if(configFile.exists()) {
			report.loadConfig(configFile);		
		}		
		testLog = report.startTest(name);
	}
	
	public LogUtil(Class<?> newClazz){
		clazz = newClazz;
		logger = Logger.getLogger(newClazz);
		Layout layout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} [%p] %c:%L - %m%n");  
		try {
			Appender appender = new FileAppender(layout, log4jPath);
			logger.addAppender(appender);
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	public static LogUtil getInstance(){
		if(Log == null){
			if(Configurations.logType == 0){
				Log = new LogUtil(caseName);
			}else{
				Log = new LogUtil(clazz);
			}
		}
		return Log;
	}
	
	public static void initLog(){
		System.out.println(sharedFolder);
		File file;
		if(Configurations.logType == 0){	
			file = new File(extentFolder);
			if(!file.exists()) file.mkdir();
		}else{
			file = new File(log4jFolder);
			if(!file.exists())file.mkdir();
		}
		
		if(Configurations.logType == 0 && new File(logPath).exists() || 
				Configurations.logType == 1 && new File(log4jPath).exists()){
			if(deleteDir(file)) file.mkdir();
			if(file.exists() && file.list().length == 0){
				System.out.println("初始化日志文件夹【成功】");
			}else{
				System.out.println("初始化日志文件夹【失败】");
			}
		}
	}
	
	//info
	public void info(String info){
		if(Configurations.logType == 0){
			testLog.log(LogStatus.INFO, info);
			System.out.println(info);
		}else{
			logger.info(info);
		}
	}
	
	//warning
	public void warn(String warning){
		if(Configurations.logType == 0){
			testLog.log(LogStatus.WARNING, warning);
			System.out.println(warning);
		}else{
			logger.warn(warning);
		}
		warnCount++;
	}
	
	//error
	public void error(String error){
		if(Configurations.logType == 0){
			testLog.log(LogStatus.ERROR, error + snapshot());
			System.out.println(error);
		}else{
			logger.error(error);
		}
		errorCount++;
	}
	
	//skip
	public void skip(String skip){
		if(Configurations.logType == 0){
			testLog.log(LogStatus.SKIP, skip);
			System.out.println(skip);
		}
		skipCount++;
	}
	
	//unknow
	public void unknow(String unknow){
		if(Configurations.logType == 0){
			testLog.log(LogStatus.UNKNOWN, unknow);
			System.out.println(unknow);
		}
	}
	
	//fatal
	public void fatal(String fatal){
		if(Configurations.logType == 0){
			testLog.log(LogStatus.FATAL, fatal + snapshot());
			System.out.println(fatal);
		}else{
			logger.fatal(fatal);
		}
		failCount++;
	}
	
	//fail
	public void fail(Throwable throwable){
		if(Configurations.logType == 0){
			testLog.log(LogStatus.FAIL, snapshot() + throwable);
			System.out.println(throwable);
		}else{
			logger.error(throwable);
		}
		failCount++;
	}
	
	//fail
	public void fail(String fail){
		if(Configurations.logType == 0){
			testLog.log(LogStatus.FAIL, fail + snapshot());
			System.out.println(fail);
		}else{
			logger.error(fail);
		}
		failCount++;
	}
	
	//pass
	public void pass(String pass){
		if(Configurations.logType == 0){
			testLog.log(LogStatus.PASS, pass);
			System.out.println(pass);
		}else{
			logger.info(pass);
		}
	}
	
	public boolean verifyEquals(Object actual, Object expected, boolean isPrintLog){
		try {
			Assert.assertEquals(actual, expected);
			if(isPrintLog){
				if(Configurations.logType == 0){
					testLog.log(LogStatus.INFO, "实际值：\""+actual+"\" ，跟期望值：\""+expected+"\" 相匹配");
				}else{
					logger.info("实际值：\""+actual+"\" ，跟期望值：\""+expected+"\" 相匹配");
				}
			}
			return true;
		} catch (Error e) {
			if(Configurations.logType == 0){
				testLog.log(LogStatus.ERROR, "实际值：\""+actual+"\" ，跟期望值：\""+expected+"\" 不匹配");
			}else{
				logger.error("实际值：\""+actual+"\" ，跟期望值：\""+expected+"\" 不匹配");
			}
			errorCount++;
			return false;
		}
	}
	
	public boolean verifyEquals(Object actual, Object expected, String msg){
		try {
			Assert.assertEquals(actual, expected);
			if(Configurations.logType == 0){
				testLog.log(LogStatus.INFO, "实际"+ msg +"：\""+actual+"\" ，跟期望"+ msg +"：\""+expected+"\" 相匹配");
			}else{
				logger.info("实际"+ msg +"：\""+actual+"\" ，跟期望"+ msg +"：\""+expected+"\" 相匹配");
			}
			return true;
		} catch (Error e) {
			if(Configurations.logType == 0){
				testLog.log(LogStatus.ERROR, "实际"+ msg +"：\""+actual+"\" ，跟期望"+ msg +"：\""+expected+"\" 不匹配");
			}else{
				logger.error("实际"+ msg +"：\""+actual+"\" ，跟期望"+ msg +"：\""+expected+"\" 相匹配");
			}
			errorCount++;
			return false;
		}
	}
	
	//commit the logs
	public void commit(){
		if(Configurations.logType == 0){
			report.endTest(testLog);
			report.flush();
		}
	}
	
	public void flush(){
		report.flush();
	}
	
	public void close(){
		if(Configurations.logType == 0){
			report.close();
		}
	}
	
	public String getPngName(){
		return DateUtil.getTimestamp() + ".jpg";
	}

	public String snapshot(){
		File snapFolder = new File(snapshotFolder);
		if(!snapFolder.exists()){
			snapFolder.mkdirs();
		}
	    String file = snapshotFolder + getPngName();
	    snapshotDesk(file);   
        return "<br><a href=\"" + file + "\">点击查看详情</a>";	      
	}
	
	public void snapshotDesk(String file){
		try {
			Robot robot = new Robot();  
		    Dimension d = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());  
			int width = (int) d.getWidth();  
		    int height = (int) d.getHeight();
		    Image image = robot.createScreenCapture(new Rectangle(0, 0, width,  
		            height));  
		    BufferedImage bi = new BufferedImage(width, height,  
		            BufferedImage.TYPE_INT_RGB);  
		    Graphics g = bi.createGraphics();  
		    g.drawImage(image, 0, 0, width, height, null);  
		    //保存图片  
		    try {
				ImageIO.write(bi, "jpg", new File(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initStat(){
		failCount = 0;
		errorCount = 0;
		warnCount = 0;
		skipCount = 0;
	}
	
	public boolean isPass(){
		return failCount == 0 && errorCount == 0 && warnCount == 0;
	}
}
