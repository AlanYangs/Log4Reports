package com.yag.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.yag.base.Configurations;

public class EmailUtil {
	private  Properties prop = new Properties();
	private static  Session session;
	private static  Transport ts;
	private  static String host = Configurations.smtpHost;
	private static  String username = Configurations.sender;
	private  String password = Configurations.sendPassword;
	private static String recipients = "";//收件人列表
	private static String subject = "this is maill subject";//邮件主题
	private static String content = "this is maill content";//邮件内容

    public static void main(String[] args) throws Exception {
        new EmailUtil();
        Message message = createSimpleMail(subject, content);
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();
    }
    
    public EmailUtil(){
    	connect(EmailUtil.host, EmailUtil.username, this.password);
        
    }
    
    public EmailUtil(String host, String name, String pwd){
    	EmailUtil.host = host;
    	EmailUtil.username = name;
    	this.password = pwd;
    	connect(EmailUtil.host, EmailUtil.username, this.password);
    	
    }
    
    public static void sendMail(String subject, String content, String receivers){
    	recipients = receivers;
    	new EmailUtil();
        Message message;
		try {
			message = createSimpleMail(subject, content);
			ts.sendMessage(message, message.getAllRecipients());		
	        ts.close();
		} catch (Exception e) {
			e.printStackTrace();
		}      
    }
    
    public static void sendMail(String subject, String content, String receivers, String path){
    	recipients = receivers;
    	new EmailUtil();
        Message message;
		try {
			String suffix = new File(path).getName();
			if(suffix.endsWith(".jpg") || suffix.endsWith(".png") || suffix.endsWith(".gif")){
				message = createImageMail(subject, content, path);
			}else{
				message = createAttachMail(subject, content, path);
			}
			
			ts.sendMessage(message, message.getAllRecipients());
	        ts.close();
		} catch (Exception e) {
			e.printStackTrace();
		}      
    }
    
    public static void sendMail(String subject, String content, String receivers, String filePath, String imagePath){
    	recipients = receivers;
    	new EmailUtil();
        Message message;
		try {
			message = createMixedMail(subject, content, imagePath, filePath);
			ts.sendMessage(message, message.getAllRecipients());
	        ts.close();
		} catch (Exception e) {
			e.printStackTrace();
		}      
    }
    
    public void connect(String host, String name, String pwd){
    	prop.setProperty("mail.host", host);
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");
        session = Session.getInstance(prop);
        session.setDebug(true);
		try {
			ts = session.getTransport();
	        ts.connect(host, name, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
    * @Method: createSimpleMail
    * @Description: 创建一封只包含文本内容的邮件
    * @param session
    * @return
    * @throws Exception
    */ 
    public static MimeMessage createSimpleMail(String subject, String content) throws Exception {
    	
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress(username));
        if(recipients.contains(";")){
			List<InternetAddress> list = new ArrayList<InternetAddress>();
			String []median=recipients.split(";");
			for(int i=0;i<median.length;i++){
				list.add(new InternetAddress(median[i]));
			}
			InternetAddress[] address =list.toArray(new InternetAddress[list.size()]);
			message.setRecipients(Message.RecipientType.TO,address);
		}else{
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
		}
        //邮件的标题
        message.setSubject(subject);
        //邮件的文本内容
        message.setContent(content, "text/html;charset=UTF-8");
        //返回创建好的邮件对象
        return message;
    }
    
    /**
    * @Method: createAttachMail
    * @Description: 创建一封带附件的邮件
    * @param session
    * @return
    * @throws Exception
    */ 
    public static MimeMessage createAttachMail(String subject, String content, String filePath) throws Exception{
        MimeMessage message = new MimeMessage(session);
        
        message.setFrom(new InternetAddress(username));
        if(recipients.contains(";")){
			List<InternetAddress> list = new ArrayList<InternetAddress>();
			String []median=recipients.split(";");
			for(int i=0;i<median.length;i++){
				list.add(new InternetAddress(median[i]));
			}
			InternetAddress[] address =list.toArray(new InternetAddress[list.size()]);
			message.setRecipients(Message.RecipientType.TO,address);
		}else{
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
		}
        message.setSubject(subject);
        MimeBodyPart text = new MimeBodyPart();
        text.setContent(content, "text/html;charset=UTF-8");
        
        MimeBodyPart attach = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource(filePath));
        attach.setDataHandler(dh);
        attach.setFileName(dh.getName());

        MimeMultipart mp = new MimeMultipart();
        mp.addBodyPart(text);
        mp.addBodyPart(attach);
        mp.setSubType("mixed");
        
        message.setContent(mp);
        message.saveChanges();
        return message;
    }
    
    
    /**
     * @Method: createImageMail
     * @Description: 生成一封邮件正文带图片的邮件
     * @param session
     * @return
     * @throws Exception
     */ 
     public static MimeMessage createImageMail(String subject, String content, String imagePath) throws Exception {

         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress(username));
         if(recipients.contains(";")){
 			List<InternetAddress> list = new ArrayList<InternetAddress>();
 			String []median=recipients.split(";");
 			for(int i=0;i<median.length;i++){
 				list.add(new InternetAddress(median[i]));
 			}
 			InternetAddress[] address =list.toArray(new InternetAddress[list.size()]);
 			message.setRecipients(Message.RecipientType.TO,address);
 		}else{
 			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
 		}
         message.setSubject(subject);
         MimeBodyPart text = new MimeBodyPart();
         text.setContent(content, "text/html;charset=UTF-8");
         MimeBodyPart image = new MimeBodyPart();
         DataHandler dh = new DataHandler(new FileDataSource(imagePath));
         image.setDataHandler(dh);
         image.setContentID("xxx.jpg");
         MimeMultipart mm = new MimeMultipart();
         mm.addBodyPart(text);
         mm.addBodyPart(image);
         mm.setSubType("related");

         message.setContent(mm);
         message.saveChanges();
         return message;
     }
     
    /**
     * @Method: createMixedMail
     * @Description: 生成一封带附件和带图片的邮件
     * @param session
     * @return
     * @throws Exception
     */ 
     public static MimeMessage createMixedMail(String subject, String content, String imagePath, String filePath) throws Exception {
         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress(username));
         if(recipients.contains(";")){
 			List<InternetAddress> list = new ArrayList<InternetAddress>();
 			String []median=recipients.split(";");
 			for(int i=0;i<median.length;i++){
 				list.add(new InternetAddress(median[i]));
 			}
 			InternetAddress[] address =list.toArray(new InternetAddress[list.size()]);
 			message.setRecipients(Message.RecipientType.TO,address);
 		}else{
 			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
 		}
         message.setSubject(subject);
         
         MimeBodyPart text = new MimeBodyPart();
         text.setContent(content,"text/html;charset=UTF-8");
         
         MimeBodyPart image = new MimeBodyPart();
         image.setDataHandler(new DataHandler(new FileDataSource(imagePath)));
         image.setContentID("aaa.jpg");
         
         MimeBodyPart attach = new MimeBodyPart();
         DataHandler dh = new DataHandler(new FileDataSource(filePath));
         attach.setDataHandler(dh);
         attach.setFileName(dh.getName());

         MimeMultipart mp = new MimeMultipart();
         mp.addBodyPart(text);
         mp.addBodyPart(image);
         mp.setSubType("related");
         
         MimeBodyPart bodyContent = new MimeBodyPart();
         bodyContent.setContent(mp);
         message.saveChanges();
         return message;
     }
}