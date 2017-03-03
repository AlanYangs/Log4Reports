package com.yag.test;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.yag.base.BaseCase;
import com.yag.base.LogConfig;

/**
 * Created by yangangui on 2017/1/5.
 * @author yangangui
 *
 */
public class TestCase1 extends BaseCase{
	
	private int flag = 0;
	
	@BeforeClass
	public void beforeClass() {
		super.beforeClass();
		flag = 1;
	}
	
	@AfterMethod
	public void afterMethod(ITestResult result) {
		super.afterMethod(result);
		flag = flag >1 ? 1 : flag;
	}
	
	@Test
	public void test(){
		if(assertEquals(flag, 1, true)){
			flag ++;
		}
		Log.info("this is info log");
		Log.pass("this is pass log");	
	}
	
	@Test
	public void test2(){
		if(assertEquals(flag, 2, true)){
			flag --;
		}
		Log.info("this is info log----A");
		Log.error("this is error log---B");
		Log.warn("this is warning log----C");
		Log.pass("this is pass log----D");
		Log.fail("this is fail log----E");
	}

	@Override
	public void initConfig() {
		// TODO Auto-generated method stub
		logConfig = new LogConfig.Builder("\\\\172.0.0.1\\SharedFolder")
		.setLogType(0)
		.setRetryTimes(1)
		.setSmtpHost("smtp.163.com")
		.setSender("automation@163.com")
		.setSendPassword("xxxx")
		.setReceivers("alany@xx.com")
		.setSubject("Test Log4Reports")
		.build();
		System.out.println("init log config finished.");
	}

	

}
