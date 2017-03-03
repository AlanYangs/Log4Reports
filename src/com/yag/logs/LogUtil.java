package com.yag.logs;

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

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.yag.base.LogConfig;
import com.yag.utils.DateUtil;
/**
 * Created by yangangui on 2017/1/5.
 * @author yangangui
 *
 */
public class LogUtil {
	private Logger logger;
	private ExtentReports report;
	private ExtentTest testLog;
	
	private LogConfig logConfig;
	
	public int failCount = 0;
	public int errorCount = 0;
	public int warnCount = 0;
	public int skipCount = 0;

	public LogUtil(LogConfig config, Object obj){
		this.logConfig = config;
		if(LogConfig.logType == 0){
			File configFile = new File(System.getProperty("user.dir") + "\\configs\\extent-config.xml");
			if(configFile.exists()) {
				report = new ExtentReports(config.getExtentLogPath(),false);
				report.loadConfig(configFile);		
			}		
			testLog = report.startTest(String.valueOf(obj));
		}else{
			logger = Logger.getLogger(String.valueOf(obj));
			Layout layout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} [%p] %c:%L ： %m%n");  
			try {
				Appender appender = new FileAppender(layout, config.getLog4jPath());
				logger.addAppender(appender);
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	//info
	public void info(String info){
		if(LogConfig.logType == 0){
			testLog.log(LogStatus.INFO, info);
			System.out.println(info);
		}else{
			logger.info(info);
		}
	}
	
	//warning
	public void warn(String warning){
		if(LogConfig.logType == 0){
			testLog.log(LogStatus.WARNING, warning);
			System.out.println(warning);
		}else{
			logger.warn(warning);
		}
		warnCount++;
	}
	
	//error
	public void error(String error){
		if(LogConfig.logType == 0){
			testLog.log(LogStatus.ERROR, error + snapshot());
			System.out.println(error);
		}else{
			logger.error(error);
		}
		errorCount++;
	}
	
	//skip
	public void skip(String skip){
		if(LogConfig.logType == 0){
			testLog.log(LogStatus.SKIP, skip);
			System.out.println(skip);
		}
		skipCount++;
	}
	
	//unknow
	public void unknow(String unknow){
		if(LogConfig.logType == 0){
			testLog.log(LogStatus.UNKNOWN, unknow);
			System.out.println(unknow);
		}
	}
	
	//fatal
	public void fatal(String fatal){
		if(LogConfig.logType == 0){
			testLog.log(LogStatus.FATAL, fatal + snapshot());
			System.out.println(fatal);
		}else{
			logger.fatal(fatal);
		}
		failCount++;
	}
	
	//fail
	public void fail(Throwable throwable){
		if(LogConfig.logType == 0){
			testLog.log(LogStatus.FAIL, snapshot() + throwable);
			System.out.println(throwable);
		}else{
			logger.error(throwable);
		}
		failCount++;
	}
	
	//fail
	public void fail(String fail){
		if(LogConfig.logType == 0){
			testLog.log(LogStatus.FAIL, fail + snapshot());
			System.out.println(fail);
		}else{
			logger.error(fail);
		}
		failCount++;
	}
	
	//pass
	public void pass(String pass){
		if(LogConfig.logType == 0){
			testLog.log(LogStatus.PASS, pass);
			System.out.println(pass);
		}else{
			logger.info(pass);
		}
	}
	
	//commit the logs
	public void commit(){
		if(LogConfig.logType == 0){
			report.endTest(testLog);
			report.flush();
		}
	}
	
	public void flush(){
		if(report != null){
			report.flush();
		}
	}
	
	public void close(){
		if(LogConfig.logType == 0){
			report.close();
		}
	}
	
	public String getPngName(){
		return DateUtil.getTimestamp() + ".jpg";
	}

	public String snapshot(){
		File snapFolder = new File(logConfig.getSnapshotFolder());
		if(!snapFolder.exists()){
			snapFolder.mkdirs();
		}
	    String file = logConfig.getSnapshotFolder() + getPngName();
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
