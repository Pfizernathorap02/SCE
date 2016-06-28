package com.pfizer.sce.utils; 

import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;


public class MailUtil {

    //protected static final Log log = LogFactory.getLog( MailUtil.class );
    
  //  public static String EMAIL_MODE =	Config.getStringProperty("MODE");
    
  /**
   * Send an email.
   *
   * @@param from The message sender
   * @@param to The message recipient
   * @@param subject The subject line for the message
   * @@param message The body of the message
   * @@param mimetype The MIME-type of the message; e.g. "text/plain"
   */
  public static void sendMessage(String email_from, String email_to, String email_cc,String email_bcc,String subject,
                                 String message, String mimetype, String mailJNDI) 
    throws MessagingException, AddressException {
    
    Session theSession = (Session)JNDIUtil.lookupS(mailJNDI);
    
    theSession.setDebug(false);
    
    Properties props = theSession.getProperties();
    LoggerHelper.logSystemDebug("Host  ###############" + props.getProperty("mail.smtp.host") + "Protocol >>> " + props.getProperty("protocol"));
    Message theMail = new MimeMessage(theSession);

	theMail.setFrom(new InternetAddress(email_from));

    theMail.setRecipients(Message.RecipientType.TO,
                          new InternetAddress[] { new InternetAddress(email_to) });

	
	if(email_cc != null && email_cc.trim().length() > 0)
      theMail.setRecipients(Message.RecipientType.CC,
                          new InternetAddress[] { new InternetAddress(email_cc) });
    
    if(email_bcc != null && email_bcc.trim().length() > 0)
      theMail.setRecipients(Message.RecipientType.BCC,
                          new InternetAddress[] { new InternetAddress(email_bcc) });
    
	
    theMail.setSubject(subject);
    theMail.setContent(message, mimetype);
    sendTransport(theMail);
    
  }

  /**
   * Send an email to a bunch of people and cc to other people.
   *
   * @@param from The message sender
   * @@param to The message recipients
   * @@param cc The message cc recipients   
   * @@param subject The subject line for the message
   * @@param message The body of the message
   * @@param mimetype The MIME-type of the message; e.g. "text/plain"
   */
  public static void sendMessage(String from, String[] to, String[] cc, String[] bcc, String subject, String message, String mimetype, String mailJNDI) 
    throws MessagingException, AddressException {
    
    Session theSession = (Session)JNDIUtil.lookupS(mailJNDI);
    theSession.setDebug(false);
    Message theMail = new MimeMessage(theSession);
    theMail.setFrom(new InternetAddress(from));
    
    InternetAddress[] toAddrs = new InternetAddress[to.length];
    for (int i = 0; i < to.length; i++) {
      toAddrs[i] = new InternetAddress(to[i]);
    }
    theMail.setRecipients(Message.RecipientType.TO, toAddrs);
    InternetAddress[] ccAddrs=null;
   
    if(cc!=null && cc[0]!=null){
    ccAddrs = new InternetAddress[cc.length];
    for (int i = 0; i < cc.length; i++) {
      ccAddrs[i] = new InternetAddress(cc[i]);
    }
    }
    theMail.setRecipients(Message.RecipientType.CC, ccAddrs);
    
    InternetAddress[] bccAddrs=null;
    if(bcc!=null && bcc[0]!=null){
    bccAddrs = new InternetAddress[bcc.length];
    for (int i = 0; i < bcc.length; i++) {   
        bccAddrs[i] = new InternetAddress(bcc[i]);
    }
    }    
    theMail.setRecipients(Message.RecipientType.BCC, bccAddrs);

    theMail.setSubject(subject);
    theMail.setContent(message, mimetype);

    // Log Here email Debugging 
    LoggerHelper.logSystemDebug("^^^^^ PDF & SPF(sendMessage) EMAIL DEBUG LOG START^^^^^^^^");
    LoggerHelper.logSystemDebug("^^^^^^^^ EMAIL EMail_FROM  :" + from );
    LoggerHelper.logSystemDebug("^^^^^^^^ EMAIL EMail_TO  :" + toAddrs[0] );
    //LoggerHelper.logSystemDebug("^^^^^^^^ EMAIL EMail_BCC  :" + bccAddrs[0] );
    LoggerHelper.logSystemDebug("^^^^^ PDF & SPF(sendMessage) EMAIL DEBUG LOG END^^^^^^^^");

    sendTransport(theMail);
  }
  
  public static void sendMultiPartMessage(String from, String[] to, String[] cc, String subject, String[] message, String[] mimetype, String mailJNDI) 
    throws MessagingException, AddressException {

    //log.debug(" In sendMultiPartMessage FROM: "+from+ " to "+to[0]+"cc "+cc[0]+" subject "+subject+" message "+message[0]);
    Session theSession = (Session)JNDIUtil.lookupS(mailJNDI);
    theSession.setDebug(false);
    Message theMail = new MimeMessage(theSession);
    theMail.setFrom(new InternetAddress(from));
    
    InternetAddress[] toAddrs = new InternetAddress[to.length];
    for (int i = 0; i < to.length; i++) {
        if(to[i]!=null)
            toAddrs[i] = new InternetAddress(to[i]);
    }
    theMail.setRecipients(Message.RecipientType.TO, toAddrs);

    InternetAddress[] ccAddrs = new InternetAddress[cc.length];
    for (int i = 0; i < cc.length; i++) {
        if(cc[i]!=null)
            ccAddrs[i] = new InternetAddress(cc[i]);
    }
    theMail.setRecipients(Message.RecipientType.CC, ccAddrs);
 
    MimeBodyPart mbp[] = new MimeBodyPart[mimetype.length];
    Multipart mp = new MimeMultipart();
    
    for (int i = 0; i < mbp.length; i++) {
        mbp[i] = new MimeBodyPart();
        mbp[i].setContent(message[i],mimetype[i]);
        mp.addBodyPart(mbp[i]);
    }
    theMail.setSubject(subject);
    theMail.setContent(mp);
    sendTransport(theMail);
  }  

    private static void sendTransport(Message theMail) throws MessagingException, AddressException  {
                // Log Here email Debugging 
                LoggerHelper.logSystemDebug("^^^^^ PDF & SPF(sendTransport) EMAIL DEBUG LOG START^^^^^^^^");
                LoggerHelper.logSystemDebug("^^^^^^^^ EMAILALL HEADERS :" + theMail.getAllHeaders() );
                LoggerHelper.logSystemDebug("^^^^^^^^ EMAILALL HEADERS :" + theMail.getAllRecipients() );

                LoggerHelper.logSystemDebug("^^^^^ PDF & SPF(sendTransport) EMAIL DEBUG LOG END^^^^^^^^");

                    Transport.send(theMail);
    }
}
