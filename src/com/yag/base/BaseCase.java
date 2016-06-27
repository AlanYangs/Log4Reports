package com.yag.base;

import java.lang.reflect.Method;

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

public class BaseCase{
	
	public static LogUtil Log ;
	
	private int retryCounter = 0;
	
	@BeforeSuite
	public void beforeSuite(){
		Configurations.init();
		LogUtil.initLog();
		
		TestReport.startTime = DateUtil.getNow("yyyy-MM-dd HH:mm:ss");
		TestReport.startMsTime = DateUtil.getNow("yyyy-MM-dd HH:mm:ss.SSS");
	}
	
	@AfterSuite
	public void afterSuite(){
		Log.close();
		TestReport.endTime = DateUtil.getNow("yyyy-MM-dd HH:mm:ss");
		TestReport.endMsTime = DateUtil.getNow("yyyy-MM-dd HH:mm:ss.SSS");
		System.out.println("case总数--->" + TestReport.caseCount);
		System.out.println("fail个数--->"+TestReport.failureCount);
		System.out.println("success个数--->"+TestReport.successCount);
		System.out.println("skip个数--->"+TestReport.skipedCount);
		if(! Configurations.receivers.equals("")){
			new TestReport(Configurations.receivers, Configurations.subject).sendReport();
		}
		
	}
	
	@BeforeClass
	public void beforeEachClass(){	
		if(Configurations.logType == 0){
			Log = new LogUtil(this.getClass().getSimpleName());
		}else{
			Log = new LogUtil(this.getClass());
		}
	}
	
	@AfterClass
	public void afterEachClass(){
		//Log.commit();
	}
	
	@BeforeMethod
	public void beforeEachTest(){		
		Log.initStat();
		TestReport.caseCount++;
		Log.info("==============测试执行开始==============");
	}
	
	@AfterMethod
	public void afterEachTest(ITestResult result) {
		String testClassName = String.format("%s.%s", result.getMethod()
                .getRealClass().getName().toString(), result.getMethod().getMethodName());
		System.out.println("---->"+testClassName);
		if (!result.isSuccess()) {
			Log.fail(result.getThrowable());
		}
		Log.info("==============测试执行完毕==============");
		if(Log.isPass()){
			TestReport.successCount ++;
		}else{
			if(Configurations.retryTimes > 0){
				
				try {
					Class<?> c = Class.forName(result.getMethod().getRealClass().getName());
					Method m =   c.getMethod(result.getMethod().getMethodName(), null);
					while(retryCounter < Configurations.retryTimes){
						Log.flush();
						Log.info("<<<<<<<<<<<<<<<<<< 开始重新执行\""+ result.getMethod().getMethodName() +"\"方法 >>>>>>>>>>>>>>>>>>");
						m.invoke(c.newInstance(), (Object[])null);
						Log.info("<<<<<<<<<<<<<<<<<< 重新执行\""+ result.getMethod().getMethodName() +"\"方法完毕 >>>>>>>>>>>>>>>>>>");
						retryCounter ++;
					}
				} catch (Exception e) {						
					e.printStackTrace();
				}
				if(retryCounter >= Configurations.retryTimes) {
					retryCounter = 0;//初始化
					TestReport.failureCount ++;
				}
			}else{
				TestReport.failureCount ++;
			}
		}
		
		Log.commit();
	}
	
}
