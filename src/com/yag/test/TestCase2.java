package com.yag.test;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.yag.base.BaseCase;


public class TestCase2 extends BaseCase{
	
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
		Log.info("this is info log----1");
		Log.error("this is error log----2");
		Log.warn("this is warning log----3");
		Log.pass("this is pass log----4");
		Log.fail("this is fail log----5");
		
	}
	
	@Test
	public void test2(){
		if(Log.verifyEquals(flag, 2, true)){
			flag --;
		}
		Log.info("this is info log----6");
		Log.error("this is error log----7");
		Log.warn("this is warning log----8");
		Log.pass("this is pass log-----9");
		Log.fail("this is fail log----10");
	}

}
