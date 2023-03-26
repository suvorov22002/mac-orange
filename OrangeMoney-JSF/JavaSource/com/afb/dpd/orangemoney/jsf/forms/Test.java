package com.afb.dpd.orangemoney.jsf.forms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Test
 * @author Owner
 * @version 1.0
 */
public class Test {

	public static void InitDeploy(){
		
		// TODO Auto-generated method stub
		String from = "francis_konchou@afrilandfirstbank.com";
		List<String> mailsTo = new ArrayList<String>();
		mailsTo.add("francis_konchou@afrilandfirstbank.com");
		mailsTo.add("kfarmel24@gmail.com");
		String subject = "Init Orange Money";
		String messageCorps = "Init Orange Money Service";
		SendFileEmail.SendMail("","", from, mailsTo,null, subject, messageCorps);

	}
	

	public static void InitMain(){

		// TODO Auto-generated method stub
		String from = "francis_konchou@afrilandfirstbank.com";
		List<String> mailsTo = new ArrayList<String>();
		mailsTo.add("francis_konchou@afrilandfirstbank.com");
		mailsTo.add("kfarmel24@gmail.com");
		String subject = "Init Orange Money";
		String messageCorps = "Init Orange Money Service";
		SendFileEmail.SendMail("","", from, mailsTo,null, subject, messageCorps);
		//SendFileEmail.SendMail("","", from, mailsTo,null, subject, messageCorps);
		//String KEY_DIR = System.getProperty("jboss.server.data.dir", ".") + File.separator + "keystore";
		//System.setProperty("javax.net.ssl.trustStore", KEY_DIR+"\\keystore.ks");
		//System.setProperty("javax.net.ssl.trustStorePassword", "b2wafriland");

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
			
		
		//String nsp = "02979551051";
		//System.out.println("---main---"+nsp.subSequence(7,10));
		
		String javaHomePath = System.getProperty("java.home");
		String keystore = javaHomePath + "\\lib\\security\\cacerts";
		String storepass= "changeit";
		String storetype= "JKS";	
		System.getProperties().setProperty("javax.net.ssl.trustStore","C:\\jboss-4.2.1\\server\\portal\\data\\keystore\\keystore.ks");
		System.getProperties().setProperty("javax.net.ssl.keyStore",keystore);
		System.getProperties().setProperty("javax.net.ssl.keyStorePassword",storepass);
		System.getProperties().setProperty("javax.net.ssl.keyStoreType", storetype);
		System.getProperties().setProperty("javax.net.ssl.trustStorePassword", "b2wafriland");
				
		
		//System.clearProperty("javax.net.ssl.trustStore");
		//System.clearProperty("javax.net.ssl.trustStorePassword");
		
		System.out.println("---main---"+System.getProperty("javax.net.ssl.keyStore"));
		System.out.println("---main---"+System.getProperty("javax.net.ssl.keyStorePassword"));
		
		// System.setProperty("javax.net.ssl.keyStore","C:\\jboss-4.2.1\\server\\portal\\data\\keystore.ks");
		// System.setProperty("javax.net.ssl.keyStorePassword", "b2wafriland");
				
		// TODO Auto-generated method stub
		String from = "francis_konchou@afrilandfirstbank.com";
		List<String> mailsTo = new ArrayList<String>();
		mailsTo.add("francis_konchou@afrilandfirstbank.com");
		mailsTo.add("kfarmel24@gmail.com");
		mailsTo.add("kfarmel@yahoo.fr");
		String subject = "Test";
		String messageCorps = "Mail is OK";
		SendFileEmail.SendMail("FIle Name","E:\\Soft\\VMware-workstation-full-Key.txt", from, mailsTo,null, subject, messageCorps);
		
		
		System.out.println("---main---"+System.getProperty("javax.net.ssl.trustStore"));
		System.out.println("---main---"+System.getProperty("javax.net.ssl.trustStorePassword"));
		
		// TODO Auto-generated method stub
		// SendFileEmail.SendMail("FIle Name","E:\\Soft\\VMware-workstation-full-Key.txt", from, mailsTo,null, subject, messageCorps);
		
	}

}
