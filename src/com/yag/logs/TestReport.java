
package com.yag.logs;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.yag.base.Configurations;
import com.yag.utils.DateUtil;
import com.yag.utils.EmailUtil;

public class TestReport {
	
	public static int failureCount = 0;
	public static int skipedCount = 0;
	public static int successCount = 0;
	public static int caseCount = 0;
	public static String startTime = "";
	public static String endTime = "";
	public static String startMsTime = "";
	public static String endMsTime = "";

	private static String recipients = "yangangui@meizu.com";
	private static String subject = "自动化测试报告_" + DateUtil.getNow("yyyy-MM-dd");
	private static String content ="";
	
	public TestReport(String recivers){
		recipients = recivers;
	}
	
	public TestReport(String recivers, String subjects){
		if(!recivers.equals("") && !subjects.equals("")){
			recipients = recivers;
			subject = subjects + "_" +DateUtil.getNow("yyyy-MM-dd");
		}
	}
	
	public void sendReport(){
		
		String detail = "";
		if(Configurations.logType == 0){
			detail = "<iframe src=\"" + LogUtil.logPath + "\" height=\"80%\" width=\"100%\" frameborder=\"0\" scrolling =\"no\">" + 
				"<a href=\"" + LogUtil.logPath + "\">您的浏览器版本太低，请点击这里访问页面内容</a>" +
				"</iframe>";
		}else{
			detail = "<iframe src=\"" + LogUtil.log4jPath + "\" height=\"80%\" width=\"100%\" frameborder=\"0\" scrolling =\"no\">" + 
					"<a href=\"" + LogUtil.log4jPath + "\">您的浏览器版本太低，请点击这里查看详情</a>" +
					"</iframe>" ;
		}
				
		content = "<html>" +
				"<head>" +
				"<title>"+ subject +"</title>" +
				"<style  type=\"text/css\">" +
				"body{" +
					"font-family:Arial;" +
					"font-size:14px;" +
					"font-color:#484848;" +
					"margin:0px;" +
					"border:0px;}" +
				"#header{" +
					"width:100%;" +
					"height:100px;" +
					"float:top;" +
					"background-color:#02a8f3;" +
					"border:0px;}" +	
				".tag{" +
					"height:70px;" +
					"width:180px;" +
					"background-color:"+ testStatusColor() +";" +
					"text-align:center;" +
					"border:2px solid #FFFFFF;" +
					"border-radius:8px;" +
					"-webkit-border-radius:8px;" +
		            "-moz-border-radius:8px;}" +
				".circle{" +
					"width:10px;" +
					"height:10px;" +
					"background-color:#FFFFFF;" + 
					"border-radius:5px;" +
					"-webkit-border-radius:5px;" +
					"-moz-border-radius:5px;}" +
				"th{" +
					"padding-bottom:5px;" +
					"padding-top:5px;" +
					"border-style:hidden;" +
					"border-collapse:collapse;}" +
				"td{" +
					"padding-bottom:10px;" +
					"padding-top:10px;" +
					"font-size:20pt;" +
					"border-style:hidden;" +
					"border-collapse:collapse;}" +
				".noborder{" +
					"border-bottom:0px;" +
					"border-top:0px;" +
					"border-right:0px;}" +		
				".startTime{" +
					"background-color:#53d192;" +
					"border:0px;" +
					"border-color:#53d192;}" +
				".endTime{" +
					"background-color:#f96868;" +
					"border:0px;" +
					"border-color:#f96868;}" +	
				".totalTime{" +
					"background-color:#b0bec5;" +
					"border:0px;" +
					"border-color:#b0bec5;}" +
				"a:link,a:visited{" +
					"text-decoration:none;" +
					"color:#02a8f3;}" +
					"</style>" +
				"</head>" +
				"<body>" +
					//"<div id=\"header\"><h2><font color=\"#ffffff\">测试结果: "+ testResult() +"</font></h2></div>" +
					"<div id=\"header\">" +
						"<table style=\"background-color:#02a8f3;\"　width=\"100%\"　height=\"70px\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">" +
						"<tr width=\"100%\" height=\"70px\" style=\"background-color:#02a8f3;\">" +
							"<td width=\"15%\" height=\"70px\" align=\"left\"><font color=\"#FFFFFF\" size=5><b>测试结果</b>:<font></td>" +
							"<td width=\"20%\" height=\"70px\" align=\"left\"><div class=\"tag\"><div class=\"circle\"></div><font color=\"#FFFFFF\" size=6>&nbsp;"+ testResult() +"<font></div></td>" +
							"<td width=\"65%\" height=\"70px\"></td>" +
						"</tr></table>" +
					"</div>" +
					"<h3>测试结果统计：</h3>" +
					  "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin:4px; border-color: #EFEFEF;\" border = \"1\">" +
						"<th width=\"12%\">用例总数</th>" +
						"<th width=\"12%\">Success</th>" +
						"<th width=\"12%\">Failure</th>" +
						"<th width=\"12%\">Skiped</th>" +
						"<th width=\"4%\" rowspan=\"3\" class=\"noborder\"></th>" +
						"<th width=\"16%\" align=\"left\" class=\"totalTime\" ><font color=\"#ffffff\" size=\"3\">测试用时</font></th>" +
						"<th width=\"16%\" align=\"left\" class=\"startTime\" ><font color=\"#ffffff\" size=\"3\">开始时间</font></th>" +
						"<th width=\"16%\"align=\"left\" class=\"endTime\" ><font color=\"#ffffff\" size=\"3\">结束时间</font></th>" +
				        "<tr align=\"center\" valign=\"middle\" >" +
							"<td><font color=\"#C1C1C1\">"+ caseCount +"</font></td>" +
				            "<td><font color=\"#32CD32\">"+ successCount +"</font></td>" +
							"<td><font color=\"#E57373\">"+ failureCount +"</font></td>" +
							"<td><font color=\"#FFAA00\">"+ skipedCount +"</font></td>" +		
							"<td rowspan=\"2\" align=\"right\" valign=\"bottom\" class=\"totalTime\"><font color=\"#ffffff\" size=\"2\"><b>"+ testUsedTime() +"</b></font></td>" +
							"<td rowspan=\"2\" align=\"right\" valign=\"bottom\" class=\"startTime\"><font color=\"#ffffff\" size=\"2\"><b>"+ startTime +"</b></font></td>" +
							"<td rowspan=\"2\" align=\"right\" valign=\"bottom\" class=\"endTime\"><font color=\"#ffffff\" size=\"2\"><b>"+ endTime +"</b></font></td>" +
				        "</tr>" +
				        "<tr align=\"center\" valign=\"middle\">" +
							"<td><font color=\"#565656\" size=\"3\"><b>通过率</b></font></td>" +
							"<td colspan=\"3\" ><font color=\"#565656\">"+ testPassRate() +"</font></td>" +
						"</tr>" +
					"</table>" +
					"<h3>测试结果详情：</h3>" + detail +	
				"</body>" +
				"</html>";
		
		if(Configurations.logType == 1){
			EmailUtil.sendMail(subject, content, recipients, LogUtil.log4jFolder + "\\output.log");
		}else{
			EmailUtil.sendMail(subject, content, recipients);
		}
		
	}
	
	public static String testResult(){
		if(caseCount == 0 || successCount == 0) return "Failed";
		if(successCount == caseCount || successCount + skipedCount == caseCount){
			return "Successful";
		}else{
			return "Failed";
		}
	}
	
	public static String testStatusColor(){
		return testResult().equals("Successful") ? "#53d192" : "#f96868";
	}
	
	public static String testPassRate(){
		if(caseCount == 0) return "0%";
		if(successCount == caseCount || successCount + skipedCount == caseCount){
			return "100%";
		}else{
			return new DecimalFormat("0.00%").format(successCount*1.0/caseCount);
		}
	}
	
	public static String testUsedTime(){
		if(startMsTime.equals("") && endMsTime.equals("")){
			return "0小时  0分  0秒+0毫秒";
		}
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		long between = 0;
		try {
			java.util.Date begin = dfs.parse(startMsTime);
			java.util.Date end = dfs.parse(endMsTime);
			between = (end.getTime() - begin.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
        long hour = (between / (60 * 60 * 1000));
        long min = ((between / (60 * 1000)) - hour * 60);
        long s = (between / 1000 - hour * 60 * 60 - min * 60);
        long ms = (between - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
		return hour + "小时  " + min + "分  " + s + "秒+" + ms+ "毫秒";
	}
}
