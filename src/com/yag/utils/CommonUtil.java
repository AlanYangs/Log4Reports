package com.yag.utils;

import java.io.File;
import java.util.Random;

public class CommonUtil {
	
	public static void sleep(double second){
		try {
			Thread.sleep((long) (second*1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean deleteDir(File dir) {
        if(dir.isFile()){  
        	dir.delete();       
        }else{
        	File[] files = dir.listFiles();
        	for (int i = 0; i < files.length; i++) {  
        		deleteDir(files[i]);  
                files[i].delete();      
            } 
        }
        if(! dir.exists()) dir.mkdir();
        return dir.exists() && dir.list().length == 0;
    }
	
	public static boolean removeDir(File dir) {
		if (dir.isDirectory()) {
	      	String[] children = dir.list();
	      	//递归删除目录中的子目录
	      	for (int i=0; i<children.length; i++) {
	          	boolean success = deleteDir(new File(dir, children[i]));
	          	if (!success) {
	              	return false;
	          	}
	      	}
	  	}
	  	return dir.delete();
	}
	  
    public static String subZeroAndDot(String s){    
        if(s.indexOf(".") > 0){    
            s = s.replaceAll("0+?$", "");  
            s = s.replaceAll("[.]$", "");
        }    
        return s;    
    }

    public static int[] getRandom(int max, int length){
    	int[] arr = new int[length];
    	Random random = new Random();
    	arr[0] = random.nextInt(max) + 1;//[1,max]
    	for (int i = 1; i < arr.length; i++) {
			arr[i] = random.nextInt(max) + 1;
			for (int j = 0; j < i; j++) {
				if (arr[i] == arr[j]) {
					i--;
				}
			}
    	}
    	return arr;
    }
    
    public static String getJsonValueByTag(String json, String tag){
		if(! json.contains(tag)){
			return "";
		}
		int startPos = json.indexOf(tag);
		int colonPos = json.substring(startPos).indexOf(":");
		int commaPos = json.substring(startPos).indexOf(",");
		String value = json.substring(startPos).substring(colonPos + 1, commaPos);
		return filterStr(value);
	}
    
	public static final String filterStr(String str) {
		str = str.replaceAll(" ", "");
		if(str.indexOf("\"") == 0 || str.lastIndexOf("\"") == str.length()-1){
			str = str.replaceAll("\"", "");
		}		
		return str;
	}
	
	public static String getFileName(String basePath){
		removeFolder(basePath, -7);
		String name = DateUtil.getNow("yyyyMMdd");
		File file = new File(basePath + File.separator + name);
		if(! file.exists()){
			return file.getAbsolutePath();
		}
		File[] tempList = new File(basePath).listFiles();
		int index = 1;
		int flag = 0;
		for (int i = 0; i < tempList.length; i++) {
			if(tempList[i].isDirectory() && tempList[i].getName().startsWith(name)){
				if(tempList[i].getName().contains("_")){
					int pos = tempList[i].getName().indexOf("_");
					pos = Integer.parseInt(tempList[i].getName().substring(pos+1));
					if(pos > index){
						index = pos;
					}
					flag++;
				}
			}
		}
		if(flag > 0) index++;
		return basePath + File.separator + name + "_" + index;
	}
	
	public static void removeFolder(String basePath, int day){
		String endDate = DateUtil.getNewDate(day);
		File baseFile = new File(basePath);
		if(baseFile.exists()){
			for (File file : baseFile.listFiles()) {
				if(file.isDirectory() && file.getName().startsWith(endDate)){
					removeDir(file);
				}
			}
		}
	}
}
