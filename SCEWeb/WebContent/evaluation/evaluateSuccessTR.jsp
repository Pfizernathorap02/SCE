<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.beans.Attendee"%>
<%@ page import="com.pfizer.sce.common.SCEConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!-- <netui-data:declareControl type="SCEDBControls.SCEManager" controlId="SCEManager"></netui-data:declareControl> -->

<%
    SCEManagerImpl sceManager = new SCEManagerImpl();
    Attendee evalAttendee = sceManager.getAttendeeByEmplId((String)request.getAttribute("evalEmplId"));    
    String eventName = sceManager.getEventById((Integer)request.getAttribute("evalEventId")).getName();
    String status = (String)request.getAttribute("status");
    String msg = "submitted successfully";
    if (SCEConstants.ST_DRAFT.equalsIgnoreCase(status)) {
        msg = "saved";
    }
%>

<script language="javascript">
    function refreshParent() {
        try {
            document.domain = 'pfizer.com';
            top.window.opener.update();
        }
        catch(e) {            
        }
        window.close();    
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
        <!--[if IE 6]><LINK href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" type=text/css rel=stylesheet><![endif]-->        
    </head>
    <body id="p_evaluation_form">
    	<div id="wrap">
            
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
                
                <!-- end #top_head -->
            </div>
            
            <%--<h3><%=eventName%> Sales Call Evaluation</h3>--%>
            
            <div id="main_content">
                <p><br><br></p>
                
                <p align="center">
                
                    <table>
                        <tr>
                            <td>
                                Your evaluation for <b><%=evalAttendee.getName()%></b> has been <%=msg%>.
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <img src="<%=request.getContextPath()%>/evaluation/resources/_img/btn_close.gif" alt="Close Window" width="51" height="19" onclick="return refreshParent()"/> 
                            </td>
                        </tr>                
                    </table> 
                </p>               
                
            </div> <!-- end #content -->
            
            
        </div><!-- end #wrap -->
        
    </body>
</html>
<%
    request.removeAttribute("evalEmplId");
    request.removeAttribute("evalEventId");
    request.removeAttribute("status");
%>
