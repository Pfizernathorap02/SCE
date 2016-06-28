<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.beans.SCE"%>
<%@ page import="com.pfizer.sce.common.SCEConstants"%>


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
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/content_print.css" rel="stylesheet" type="text/css" media="print" />
        <!--[if IE 6]><LINK href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" type=text/css rel=stylesheet><![endif]-->                

    </head>
    <body  id="p_email_template_maintenance"  class="admin">
        <div id="wrap">
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
                
                
                <!-- end #top_head -->
            </div>
            <h3>Email Template </h3>
            <br><br><br><br><br>
            
            <!-- RUPINDER-->
<!--      <label>To:</label><div  align="center" STYLE=" border:.1em solid black; height: 50px; width: 600px; overflow-y: auto font-size:.8em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial'; overflow-y:auto;">
 -->          
    <table cellspacing="0" border="1" STYLE="font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';" align="center">
		<tr>
			<td><b>To:&nbsp;</b></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b>Cc:&nbsp;</b></td>
			<td><%=request.getParameter("emailCc")%>&nbsp;</td>
		</tr>
		<tr>
			<td><b>BCc:&nbsp;</b></td>
			<td ><%=request.getParameter("emailBcc")%>&nbsp;</td>
		</tr>
		<tr>
			<td><b>Subject:&nbsp;</b></td>
			<td><%=request.getParameter("emailSubject")%>&nbsp;</td>
		</tr>
		<tr>
			<td><b>Body:&nbsp;</b></td>
			<td ><span style="width:600px; size:200px  bordercolor=white;"><multicol><%=request.getParameter("emailBody")%>&nbsp;</td>
		</tr>
	</table>
          
    </body>
</html>