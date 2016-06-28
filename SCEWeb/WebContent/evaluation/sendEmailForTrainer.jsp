


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="com.pfizer.sce.utils.MailUtil"%>
<%@page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="java.io.*,java.util.*,javax.mail.*"%>
<%@ page import="javax.mail.internet.*,javax.activation.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ page import="com.pfizer.sce.beans.Util"%>
<%@ page import="com.pfizer.sce.utils.MailUtil"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp" %>
<!-- <netui-data:declareControl type="SCEDBControls.SCEManager" controlId="SCEManager"></netui-data:declareControl> -->
 <%
 String toEmail = request.getParameter("toEmail");
 String ccEmail = request.getParameter("toCC");
 String subEmail = request.getParameter("subject");
 String emailBody = request.getParameter("contentBody");
 String event = request.getParameter("eventSel");
 String product = request.getParameter("productSel");
  
 if(request.getParameter("h_ifFromLearner")!=null && request.getParameter("h_ifFromLearner").equalsIgnoreCase("L")){
  toEmail = request.getParameter("h_toEmail");
  ccEmail = request.getParameter("h_toCC");
  subEmail = request.getParameter("h_toSub");
  emailBody = request.getParameter("h_contentBody");
  event = request.getParameter("h_eventSel");
 }
 
 //this string is to check if mail request is for learner or trainerInvite
  
 System.out.println("toEmail:: 1:"+toEmail);
 System.out.println("ccEmail:: 2:"+ccEmail);
  System.out.println("emailSubject:: 3:"+subEmail);
  System.out.println("emailBody:: 4:"+emailBody);
  System.out.println("EventName:: 5:"+event);
  
  System.out.println("productName :: 7:" +product);
 
 SCEManagerImpl sceManager = new SCEManagerImpl();


  for(int i=0;i<15;i++)
  {
 //  // System.out.println("values at "+ i + "-- " + emailBody.charAt(i));
  }
 //String subEmail = request.getParameter("subEmail");
 //String textEmail = request.getParameter("textEmail"); 

 
 String[] arrVal = toEmail.split(";");
 String to;
 String cc = ccEmail;
 String ifSent ="";
 String[] arrValToMail = toEmail.split(";");
 String[] arrValCcMail = ccEmail.split(";");
 String[] toMail= new String[arrValToMail.length];
 String[] ccMail=new String[arrValCcMail.length];
 String[] bccMail=new String[1];
 String mimetype = "text/html";
 String mailJNDI = "java:jboss/SCEMailSession"; 
  
    

 
   String result = "";
   // Recipient's email ID needs to be mentioned.
  //   String to = "monika.sharma@pfizer.com";


   // Sender's email ID needs to be mentioned
  String from = "traininglogistics@pfizer.com";
 //  String from = "sanjeev.verma@pfizer.com";
 //String from = "manish.kumar2@pfizer.com";

   // Assuming you are sending email from localhost
   String host = "mailhub.pfizer.com";

   // Get system properties object
   Properties properties = System.getProperties();

   // Setup mail server
   properties.setProperty("mail.smtp.host", host);

   // Get the default Session object.
   
   Session mailSession = Session.getDefaultInstance(properties);
   
   try{
	   MailUtil mailUtil=new MailUtil();
      // Create a default MimeMessage object.
      
      
      MimeMessage message = new MimeMessage(mailSession);
      // Set From: header field of the header.
      message.setFrom(new InternetAddress(from));
      // Set To: header field of the header.
      
          /* for(int i=0; i<arrVal.length; i++)
   {
    to = arrVal[i];    
      message.addRecipient(Message.RecipientType.TO,
                               new InternetAddress(to));
          
   } */
   //adding email in CC
   /* if(cc==null || cc=="" || cc.equalsIgnoreCase(""))
   {
	   
   }
   else
   {
        message.addRecipient(Message.RecipientType.CC,new InternetAddress(cc));      
   } */                     
                            
                
      // Set Subject: header field
        message.setSubject(subEmail);
      // Now set the actual message
   //   message.setText(emailBody);
        String  emailBodyFinal =emailBody.replaceAll( "\n","</br>");
        message.setContent(emailBodyFinal,"text/html");
      // Send message
       //Transport.send(message);
      
       for(int i=0; i<arrValToMail.length; i++)
   {
    	   toMail[i] = arrValToMail[i];  
   }
       
       if(cc==null || cc=="" || cc.equalsIgnoreCase(""))
       {
    	   ccMail[0]=null;
       }
       else
       {
    	   for(int i=0; i<arrValCcMail.length; i++)
    	   {
    		   ccMail[i] = arrValCcMail[i];  
    	   }
       
       }
       
      bccMail[0]=null; 
       
  //    String productName=(String)session.getAttribute("productName");

      mailUtil.sendMessage(from, toMail, ccMail, bccMail, subEmail, emailBodyFinal, mimetype, mailJNDI);
        result = "Sent message successfully....";
       ifSent = "Y";
   }catch (MessagingException mex)
    {
        ifSent = "N";
        // System.out.println("exception name------------" + mex);
        mex.printStackTrace();
        result = "Error: unable to send message....";
    }
        session.removeAttribute("productName");
        session.removeAttribute("courseStartDate");

 %>
 
 
<script language="javascript">
function storeEmailSent()
{

var toSent = '<%=toEmail%>';        
var eventSel = '<%=event%>';
var productSel='<%=product%>';
document.forms[0].action="gotoConfirmEmailSent.action?toSent="+toSent+"&eventName="+eventSel"&productSel="+productSel;
document.forms[0].submit();
}


</script>



<html>
 <s:form action="gotoConfirmEmailSent" tagId="emailConfirmation">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="Content-Language" content="en-us" />
        <title>Pfizer Sales Call Evaluation</title>
        <meta name="ROBOTS" content="ALL" />
        <meta http-equiv="imagetoolbar" content="no" />
        <meta name="MSSmartTagsPreventParsing" content="true" />
        <meta name="Keywords" content="_KEYWORDS_" />
        <meta name="Description" content="_DESCRIPTION_" />
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css" rel="stylesheet" type="text/css" media="all" />
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/admin.css" rel="stylesheet" type="text/css" media="all" />
        <!--[if IE 6]>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" />
        <![endif]-->
        
        <!-- methods added by manish to close the window -->
        
        <script>
	function loaded()
	{
    	window.setTimeout(CloseMe, 1000);
   
	}

	function CloseMe() 
	{
		window.open('javascript:window.open("", "_self", "");window.close();', '_self');
	}
	</script>
        
        
        <!-- methods added by manish to close the window -->
        
        
        
        
    </head>
    <body id="inemail" class="inemailC" onLoad="loaded()">
        <div id="wrap">
        
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
            
               
            <!-- end #top_head -->
            </div>
        
        
            <h3>SEND EMAIL</h3>
            <div id="main_content">
            
            
            <div>
               <%  out.println("  Result: " + result + "\n"); %>
             <br>
               
               <%if( ifSent =="N" || ifSent==null || ifSent=="0"){%>            
               <br>
               <%}else{%>
             <input   id="confirmation"   class="buttonStylesWhite" align="left"  style="width:145px; align:left; border:1px; padding:0px; font-size:0.9em;" type="hidden" value=" Click to Confirm Email Sent >" onclick="storeEmailSent()">                    
               <%}%>

               </div>
            </div> <!-- end #content -->
        
        </div><!-- end #wrap -->

    </body>
</s:form>
</html>






