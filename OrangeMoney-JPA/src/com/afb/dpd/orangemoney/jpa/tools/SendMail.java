package com.afb.dpd.orangemoney.jpa.tools;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.afb.dpd.orangemoney.jpa.entities.Parameters;

/**
 * Class that implements the sending of emails to clients
 * @author Yves LABO
 * @version 2.0
 *
 */
public class SendMail {

	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;


	public static void sendMail(List<String> filesNames, List<String> filesPath, List<String> mailsTo, String subject, String messageCorps, Parameters parametre) throws MessagingException, UnsupportedEncodingException, Exception {

		// Get system properties
		//Properties properties = System.getProperties();

		//Parametre parametre = getParametre();
		// Setup mail server
		if(mailServerProperties == null){
			mailServerProperties = new Properties();
			mailServerProperties.put("mail.smtp.port", parametre.getPortEnvoiMail());
			//mailServerProperties.put("mail.smtp.port", 25);
			mailServerProperties.put("mail.smtp.auth", "true");
			mailServerProperties.put("mail.smtp.starttls.enable", "true");
		}

		// Get the default Session object.
		Session mailSession = Session.getInstance(mailServerProperties);
		mailSession.setDebug(false);
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
		mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822"); 

		// Create a default MimeMessage object.
		MimeMessage mailMessage = new MimeMessage(mailSession);

		// Set From: header field of the header.
		// mailMessage.setFrom(new InternetAddress(parametre.getEmailEnvoi(),"Afriland First Bank"));
		//*** mailMessage.setFrom(new InternetAddress("serviceapresvente@afrilandfirstbank.com","Entrée en Relation"));
		mailMessage.setFrom(new InternetAddress(parametre.getEmail_sav(),"MAC Orange"));

		// Set To: header field of the header.
		for (String mal : mailsTo) {
			try{
				mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mal.trim()));
			}
			catch(Exception ex){

			}
		}

		if(mailMessage.getAllRecipients()== null || mailMessage.getAllRecipients().length == 0)
			throw new Exception("Aucune adresse email pour cet envoi");

		// mailMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(parametre.getEmailEnvoi()));

		// Set Subject: header field
		mailMessage.setSubject(subject);

		// Create the message part 
		BodyPart messageBodyPart = new MimeBodyPart();
		// Fill the message
		messageBodyPart.setText(messageCorps);
		// Create a multipar message
		Multipart multipart = new MimeMultipart();

		// Set text message part
		multipart.addBodyPart(messageBodyPart);

		if(filesPath!=null && filesNames!=null){
			int size = filesPath.size();
			for(int i=0;i<size;i++){
				// Part two is attachment
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(new File(filesPath.get(i)));
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(filesNames.get(i));
				multipart.addBodyPart(messageBodyPart);
			}
		}
		// Send the complete message parts
		mailMessage.setContent(multipart);

		// Send message
		Transport transport = mailSession.getTransport("smtp");

		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password

		//transport.connect(parametre.getHostServer(), parametre.getEmailEnvoi(), parametre.getMotDePass());
		//transport.connect("172.21.10.91", "serviceapresvente@afrilandfirstbank.com", "sav00001");
		transport.connect(parametre.getSmtpServerName(), parametre.getEmail_sav(), parametre.getPwd_sav());
		transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
		transport.close();
		System.out.println("-------------------------Sent email successfully-----------------------------");

		//return true;
	}
	
	
	
	public static void sendMailCC(List<String> filesNames, List<String> filesPath, List<String> mailsTo, List<String> mailsCC, String subject, String messageCorps, Parameters parametre) throws MessagingException, UnsupportedEncodingException, Exception {

		try{

			// Setup mail server
			if(mailServerProperties == null){
				mailServerProperties = new Properties();
				// mailServerProperties.put("mail.smtp.port", parametre.getPort());
				//mailServerProperties.put("mail.smtp.port", 25);
				mailServerProperties.put("mail.smtp.port", parametre.getPortEnvoiMail());
				mailServerProperties.put("mail.smtp.auth", "true");
				mailServerProperties.put("mail.smtp.starttls.enable", "false");  ///////
				
			}

			// Get the default Session object.
			Session mailSession = Session.getInstance(mailServerProperties);
			mailSession.setDebug(false);
			MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
			mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
			mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
			mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
			mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
			mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822"); 

			// Create a default MimeMessage object.
			MimeMessage mailMessage = new MimeMessage(mailSession);

			// Set From: header field of the header.

			//mailMessage.setFrom(new InternetAddress("serviceapresvente@afrilandfirstbank.com","Entrée en Relation")); //Afriland First Bank
			mailMessage.setFrom(new InternetAddress(parametre.getEmail_sav(),"MAC Orange"));

			// Set To: header field of the header.
			for (String mal : mailsTo) {
				try{
					mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mal.trim()));
				}
				catch(Exception ex){

				}
			}

			//Set Cc
			for(String mal:mailsCC){
				try{
					mailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(mal.trim()));
				}
				catch(Exception ex){

				}
			}

			if(mailMessage.getAllRecipients()== null || mailMessage.getAllRecipients().length == 0)
				throw new Exception("Aucune adresse email pour cet envoi");

			// mailMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(parametre.getEmailEnvoi()));

			// Set Subject: header field
			mailMessage.setSubject(subject);

			// Create the message part 
			BodyPart messageBodyPart = new MimeBodyPart();
			// Fill the message
			messageBodyPart.setText(messageCorps);
			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			if(filesPath!=null && filesNames!=null){
				int size = filesPath.size();
				for(int i=0;i<size;i++){
					// Part two is attachment
					messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(new File(filesPath.get(i)));
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(filesNames.get(i));
					multipart.addBodyPart(messageBodyPart);
				}
			}
			// Send the complete message parts
			mailMessage.setContent(multipart);

			// Send message
			Transport transport = mailSession.getTransport("smtp");

			// Enter your correct gmail UserID and Password
			// if you have 2FA enabled then provide App Specific Password

			//transport.connect(parametre.getHostServer(), parametre.getEmailEnvoi(), parametre.getMotDePass());
			transport.connect(parametre.getSmtpServerName(), parametre.getEmail_sav(), parametre.getPwd_sav());
			//transport.connect("172.21.10.91", "serviceapresvente@afrilandfirstbank.com", "sav00001");
			transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
			transport.close();
			System.out.println("-------------------------Sent email successfully-----------------------------");


		}
		catch(Exception e){

			e.printStackTrace();
		}

		//return true;
	}
	
	
	
}





