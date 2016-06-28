<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<html>
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
    </head>
    <body id="p-contact">
        <div id="wrap">
    
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
                <!-- end #top_head -->
            </div>
    
    
            <h3>Session Expired</h3>
            
            <%
            String reqURL = request.getRequestURL().toString().toLowerCase();
            
            String thisURL="";
            //if((reqURL.toLowerCase().indexOf("sceint.pfizer.com")) > -1) thisURL="http://upint.pfizer.com/auth.cfm?Appid=2516";
            if((reqURL.indexOf("sceint.pfizer.com")) > -1) 
                thisURL="http://sceint.pfizer.com";
            //else if((reqURL.indexOf("wls") > -1))  thisURL="http://upint.pfizer.com/auth.cfm?Appid=3001";
            else if(reqURL.indexOf("scestg.pfizer.com") > -1)  
                thisURL="http://scestg.pfizer.com";
            else if(reqURL.indexOf("scedev.pfizer.com") > -1)  
                thisURL="http://scedev.pfizer.com";
            else if(reqURL.indexOf("sce.pfizer.com") > -1)
                thisURL="http://sce.pfizer.com";
   
            %>
    
            <div id="main_content">
                <h4>Your session got expired, please click here <A HREF="<%=thisURL%>">Login</A> to login again.</h4>
                <div class="clear"></div>	
            </div> <!-- end #content -->
    
        </div><!-- end #wrap -->
    
    </body>

</html>