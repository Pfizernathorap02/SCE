<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8" isErrorPage="true"%>

<%@ page import="java.util.ResourceBundle"%>

<% 

ResourceBundle rBundle = ResourceBundle.getBundle("MessageResources");
String errKey = null;
String errMsg = "";
if(request.getAttribute("errorMsg") != null){
    errKey = request.getAttribute("errorMsg").toString();
    errMsg = rBundle.getString(errKey);
}

%>

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
            <h3>SCE Error</h3>
            <div id="main_content">
                 <p>
            
                    <%= errMsg%>
                   
                </p>
               
            </div> <!-- end #content -->


        </div><!-- end #wrap -->
    
    </body>
</html>


<% response.setStatus(200); %>

       
    