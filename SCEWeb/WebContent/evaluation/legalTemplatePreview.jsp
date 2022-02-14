<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.beans.LegalConsentTemplate"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp"%>

<%
String content = request.getParameter("cont");

// System.out.println("cont:::"+content);   

%>

<html>
<head>
<TITLE>Pfizer Sales Call Evaluation</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Content-Language" content="en-us" />
<meta name="ROBOTS" content="ALL" />
<meta http-equiv="imagetoolbar" content="no" />
<meta name="MSSmartTagsPreventParsing" content="true" />
<meta name="Keywords" content="_KEYWORDS_" />
<meta name="Description" content="_DESCRIPTION_" />
<LINK media=all
	href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css"
	type=text/css rel=stylesheet>
<!--[if IE 6]><LINK href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" type=text/css rel=stylesheet><![endif]-->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/js/DateValidator.js"></script>


</head>


<body id="p_legal" class="legal">
	<div id="wrap">
		<DIV id="top_head">
			<H1>Pfizer</H1>
			<H2>Sales Call Evaluation</H2>

			<!-- end #top_head -->
		</DIV>

		<H3>Legal Consent</H3>
		<DIV id=main_content>
			<s:form action="acceptLegalConsent" tagId="legalConsentTemplateForm">






				<div align="center"
					style="border: .2em solid grey;height: 300px; width: 785px; overflow-y: auto font-size:.8em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial'; overflow-y: auto;">
					<p align="center"
						style="font-size: 1.2em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';">
						<b>Legal Consent for Sales Call Evaluation</b>
					</p>
					<br>
					<p align="left"
						style="font-size: .9em; margin-left: 7px; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';"><%=content%></p>

				</div>
				<br>



				<div STYLE="height: 10px; width: 816px;" align="left">
					<input id="chkAccept" class="checkbox" type="checkbox"
						disabled="disabled" name="accept" /><b>I Accept</b>
				</div>
				<div STYLE="height: 10px; width: 786px;" align="right">
					<!--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
					<!--<img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_Continue_disable.gif" name ="continue1" alt="continue" width="60" height="20" onclick="document.forms[0].submit()"/>-->

					   <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_Continue_disable.gif" id="imgDisableContinue" name ="continue1"  width="62" height="19"/>
     <img src="<%=request.getContextPath()%>/evaluation/resources/_img/btn_close.gif" id="imgClose" name="close1"  width="42" height="19" />
				</div>

			</s:form>
		</div>
</body>
</html>

