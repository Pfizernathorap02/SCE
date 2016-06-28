<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.StringWriter" %>

<%@ taglib prefix="s" uri="/struts-tags"%>



<%
    String errorSessionKey = Long.toString(System.currentTimeMillis());
    StringWriter stackTrace = new StringWriter();
    session.setAttribute(errorSessionKey + ".error.request", request.getRequestURL().toString());
    if (exception != null)
    {
        exception.printStackTrace();
        //session.setAttribute(errorSessionKey + ".error.message", exception.getMessage());
        //StringWriter stackTrace = new StringWriter();
        PrintWriter stackTracePrinter = new PrintWriter(stackTrace);
        exception.printStackTrace(stackTracePrinter);
        stackTracePrinter.close();
        //session.setAttribute(errorSessionKey + ".error.stacktrace", stackTrace.toString());
    }
%>

<script language="javascript">
    function toggleDetail() {
        var viewDetailDiv = window.document.getElementById('viewDetail');
        
        if (viewDetailDiv.style.display == 'block') {
            viewDetailDiv.style.display = 'none';
        }
        else {
            viewDetailDiv.style.display = 'block';
        }
    }
</script>
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
    
                <h4>An error has occurred</h4>
                <p>
                    <ul class="bullets">
                        <li>
                            Please contact SCE Support Team
                            <br>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Email : <a href="mailto:DL-SAMSSCESupport@pfizer.com">DL-SAMSSCESupport@pfizer.com</a>
                            <%--<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Phone : (xxx) xxx-xxxx--%>
                        </li>
                        <li>
                            <a href="#" onclick="toggleDetail()">Click here</a>
                            <font color="#ff0000"> to view error detail.</font>
                        </li> 
                    </ul>
                </p>
                <div id="viewDetail" style="display:none">
                    <div>
                        <h4>Request:</h4><%=request.getRequestURL().toString()%>
                    </div>
                    
                    <div>                    
                        <h4>Exception Message:</h4><%=exception != null ? exception.getMessage() : ""%>
                    </div>
                                        
                    <div>
                        <h4>Exception Stacktrace:</h4><%=stackTrace.toString()%>
                    </div>                    
                </div>
                <div class="clear"></div>	
            </div> <!-- end #content -->


        </div><!-- end #wrap -->
    
    </body>
</html>


<% response.setStatus(200); %>
