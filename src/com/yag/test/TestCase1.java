package com.yag.test;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.yag.base.BaseCase;


public class TestCase1 extends BaseCase{
	
	private int flag = 0;
	
	@Override
	@BeforeClass
	public void beforeEachClass() {
		super.beforeEachClass();
		flag = 1;
	}
	
	@Override
	@AfterMethod
	public void afterEachTest(ITestResult result) {
		super.afterEachTest(result);
		flag = flag >1 ? 1 : flag;
	}
	
	@Test
	public void test(){
		if(Log.verifyEquals(flag, 1, true)){
			flag ++;
		}
		Log.info("this is info log");
		//Log.error("this is error log");
		//Log.warn("this is warning log");
		Log.pass("this is pass log");
		//Log.fail("this is fail log");
		
	}
	
	@Test
	public void test2(){
		if(Log.verifyEquals(flag, 2, true)){
			flag --;
		}
		Log.info("this is info log----A");
		Log.error("this is error log---B");
		Log.warn("this is warning log----C");
		Log.pass("this is pass log----D");
		Log.fail("this is fail log----E");
	}

}
