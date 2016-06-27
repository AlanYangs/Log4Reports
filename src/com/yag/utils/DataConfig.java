package com.yag.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class DataConfig {
	
	public static String filePath = System.getProperty("user.dir") + "/configs/config.properties";
	private static Properties pps = new Properties();
	
	public DataConfig(String path){
		filePath = path;
	}
	
	/**
	 * 根据Key读取Value
	 * @param keyName
	 * @return
	 */
	public String read(String keyName) {
        if(new File(filePath).exists()){
		    try {
		        InputStream in = new FileInputStream(filePath);  
		        BufferedReader bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		        pps.load(bf);
		        in.close();
		        return pps.getProperty(keyName);
		    }catch (IOException e) {
		        e.printStackTrace();
		        return "";
		    }
        }else{
        	return "";
        }
    }
	
    public static String readProperties(String keyName) {
        if(new File(filePath).exists()){
		    try {
		        InputStream in = new FileInputStream(filePath);  
		        BufferedReader bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		        pps.load(bf);
		        in.close();
		        return pps.getProperty(keyName);
		    }catch (IOException e) {
		        e.printStackTrace();
		        return "";
		    }
        }else{
        	return "";
        }
    }
    
    /**
     * 写入/更新key-value 到Properties文件中
     * @param pKey
     * @param pValue
     */
    public static void writeProperties (String pKey, String pValue) {

        try {
        	InputStream in = new FileInputStream(filePath);
            //从输入流中读取属性列表（键和元素对） 
            pps.load(in);
            
            OutputStream out = new FileOutputStream(filePath);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            
            pps.setProperty(pKey, pValue);  
            pps.store(bw, "Write a new key:" + pKey);
            in.close();
            out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
