package com.yag.base;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.yag.utils.DateUtil;
import com.yag.logs.LogUtil;
import com.yag.logs.TestReport;

/**
 * Created by yangangui on 2017/1/5.
 * @author yangangui
 *
 */
public abstract class BaseCase {
	public static LogConfig logConfig;
	public static LogUtil Log ;	
	
	private int retryCounter = 0;
	
	@BeforeSuite
	public void beforeSuite(){
		System.out.println("BaseCase: beforeSuite");
		initConfig();
		TestReport.startTime = DateUtil.getNow("yyyy-MM-dd HH:mm:ss");
		TestReport.startMsTime = DateUtil.getNow("yyyy-MM-dd HH:mm:ss.SSS");
	}
	
	@AfterSuite
	public void afterSuite(){
		System.out.println("BaseCase: afterSuite");
		Log.close();
		TestReport.endTime = DateUtil.getNow("yyyy-MM-dd HH:mm:ss");
		TestReport.endMsTime = DateUtil.getNow("yyyy-MM-dd HH:mm:ss.SSS");
		System.out.println("case����--->" + TestReport.caseCount);
		System.out.println("fail����--->"+TestReport.failureCount);
		System.out.println("success����--->"+TestReport.successCount);
		System.out.println("skip����--->"+TestReport.skipedCount);
		if(! LogConfig.receivers.equals("")){
			new TestReport(LogConfig.receivers, LogConfig.subject).sendReport();
		}
		
	}
	
	@BeforeClass
	public void beforeClass(){	
		System.out.println("BaseCase: beforeClass");
		if(logConfig == null){
			System.err.println("init log config failed, pls overwrite initConfig().");
			return;
		}
		if(LogConfig.logType == 0){
			Log = new LogUtil(logConfig, this.getClass().getSimpleName());
		}else{
			Log = new LogUtil(logConfig, this.getClass());
		}
	}
	
	@AfterClass
	public void afterClass(){
		System.out.println("BaseCase: afterClass");
		Log.commit();
	}
	
	@BeforeMethod
	public void beforeMethod(){	
		System.out.println("BaseCase: beforeMethod");
		Log.initStat();
		TestReport.caseCount++;
		Log.info("==============����ִ�п�ʼ==============");
	}
	
	@AfterMethod
	public void afterMethod(ITestResult result) {
		System.out.println("BaseCase: afterMethod");
		String testClassName = String.format("%s.%s", result.getMethod()
                .getRealClass().getName().toString(), result.getMethod().getMethodName());
		System.out.println("---->"+testClassName);
		if (!result.isSuccess()) {
			Log.fail(result.getThrowable());
		}
		Log.info("==============����ִ�����==============");
		if(Log.isPass()){
			TestReport.successCount ++;
		}else{
			if(LogConfig.retryTimes > 0){
				
				try {
					Class<?> c = Class.forName(result.getMethod().getRealClass().getName());
					Method m =   c.getMethod(result.getMethod().getMethodName());
					while(retryCounter < LogConfig.retryTimes){
						Log.flush();
						Log.info("<<<<<<<<<<<<<<<<<< ��ʼ����ִ��\""+ result.getMethod().getMethodName() +"\"���� >>>>>>>>>>>>>>>>>>");
						m.invoke(c.newInstance(), (Object[])null);
						Log.info("<<<<<<<<<<<<<<<<<< ����ִ��\""+ result.getMethod().getMethodName() +"\"������� >>>>>>>>>>>>>>>>>>");
						retryCounter ++;
					}
				} catch (Exception e) {						
					e.printStackTrace();
				}
				if(retryCounter >= LogConfig.retryTimes) {
					retryCounter = 0;//��ʼ��
					TestReport.failureCount ++;
				}
			}else{
				TestReport.failureCount ++;
			}
		}
		
		Log.commit();
	}

	
	public abstract void initConfig();
	
	
	public boolean assertEquals(Object actual, Object expected, boolean isPrintLog){
		try {
			Assert.assertEquals(actual, expected);
			if(isPrintLog){			
				Log.info("ʵ��ֵ��\""+actual+"\" ��������ֵ��\""+expected+"\" ��ƥ��");				
			}
			return true;
		} catch (Error e) {
			Log.error("ʵ��ֵ��\""+actual+"\" ��������ֵ��\""+expected+"\" ��ƥ��");
			Log.errorCount++;
			return false;
		}
	}
	
	public boolean assertEquals(Object actual, Object expected, String msg){
		try {
			Assert.assertEquals(actual, expected);
			Log.info("ʵ��"+ msg +"��\""+actual+"\" ��������"+ msg +"��\""+expected+"\" ��ƥ��");
			return true;
		} catch (Error e) {
			Log.error("ʵ��"+ msg +"��\""+actual+"\" ��������"+ msg +"��\""+expected+"\" ��ƥ��");
			Log.errorCount++;
			return false;
		}
	}
	
	
}
