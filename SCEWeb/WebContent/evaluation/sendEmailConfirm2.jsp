<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="java.io.*,java.util.*,javax.mail.*"%>
<%@ page import="javax.mail.internet.*,javax.activation.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp" %>


<%
String email = request.getAttribute("Email").toString();
String product = request.getAttribute("Product").toString();
String event = request.getAttribute("Event").toString();
%>


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
     alert("Your response has been recorded.");
     submitEmail();
     window.close();
        }

        function CloseMe() 
        {
               window.open('javascript:window.open("", "_self", "");window.close();', '_self');
        }
        
        function submitEmail() 
        {
               var eventName = '<%=event%>';
            var prodName = '<%=product%>';
          
            var emailSel = '<%=email%>';
            var toEmail = emailSel;
               window.open('evaluation/sendEmailDateTime.jsp?toEmail='+toEmail+'&eventName='+ eventName+'&productName='+ prodName,'cnt_window','status=yes,scrollbars=yes,height=500,width=900,resizable=yes'); 
         }

	</script>
        
        
        <!-- methods added by manish to close the window -->
         
        
    </head>
    <body id="inemail" class="inemailC" onLoad="loaded()" >
    
    
    
 
    
	
       <div id="wrap">
        
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
            
               
           
            </div>
        
        
            <h3>Confirmation</h3>
            <div id="main_content">
            
            Thank you for your help and confirming the time slots that you are available on. You will receive an email shortly confirming you submission.
            
            </div> 
        
        </div>

   </body>
</s:form>
</html>