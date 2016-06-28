<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.beans.LegalConsentTemplate"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp"%>
<%
	String message = (String) request.getAttribute("message");
	LegalConsentTemplate[] legalTemplates = (LegalConsentTemplate[]) request
			.getAttribute("legalTemplates");
	int publishedVersion;
	LegalConsentTemplate legalConsentTemplate = (LegalConsentTemplate) request
			.getAttribute("publishedVersion");
%>

<script language="javascript">
     function editLegalTemplate(lcId) {
    	 
         document.getElementById("selLcId").value=lcId;
       window.document.forms[0].action="edit";
     
        window.document.forms[0].submit();  
        
    } 
    
 
     function publishLegalTemplate(lcId) {

       document.getElementById("selLcId").value=lcId;
       document.forms[0].action="publish";
       document.forms[0].submit(); 
      
      <%--  window.document.getElementById(getNetuiTagName("allTemplatesForm")).action="<%=PageflowTagUtils.getRewrittenFormAction("publishLegalTemplate", pageContext)%>";   --%>
                
                    
     
  
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
<link
	href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css"
	rel="stylesheet" type="text/css" media="all" />
<link
	href="<%=request.getContextPath()%>/evaluation/resources/_css/admin.css"
	rel="stylesheet" type="text/css" media="all" />
<!--[if IE 6]>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" />
        <![endif]-->
</head>
<body id="p_template_maintenance" class="admin">
	<div id="wrap">

		<div id="top_head">
			<h1>Pfizer</h1>
			<h2>Sales Call Evaluation</h2>
			<%@include file="navbar.jsp"%>
			<!-- navBar.jsp TO BE INCLUDED-->
			<!-- end #top_head -->
		</div>

		<div id="eval_sub_nav">

			<s:url action="admin" var="adminUrl"></s:url>
			<s:a href="%{adminUrl}">
				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoadmin.gif"
					alt="Back to main admin" width="119" height="18" />
			</s:a>



		</div>

		<h3>Admin: Legal Consent Template Maintenance</h3>

		<div id="main_content">
			<%
				if (message != null && !"".equals(message.trim())) {
			%>
			<%=message%>
			<%
				}
			%>
			<s:form action="gotoLegalTemplate" id="allTemplatesForm">
				<!--ADD NEW EVALUATION BUTTON HAS TO COME HERE -->

				<input type="hidden" id="selLcId" name="selLcId" value="" />
				<!--  <div
					style="height: 450px; width: 850px; font-size: 12px; overflow: auto">-->

					<table cellspacing="0" cellpadding="0">
						<tr>
							<th>Legal Consent Template Name</th>
							<th>Version</th>

							<th class="last1"></th>
							<th class="last"></th> 
						</tr>
						<%
							LegalConsentTemplate objLegalTemplate = null;
								if (legalTemplates != null) {
									for (int i = 0; i < legalTemplates.length; i++) {
										objLegalTemplate = legalTemplates[i];
										if (objLegalTemplate.getLcId() < 11
												|| objLegalTemplate.getLcId() > 7) {
						%>
						<tr>
							<td>Legal Consent Template</td>
							<td><%=objLegalTemplate.getVersion()%></td>
							<td class="last1">
								<%
									if (objLegalTemplate.getVersion() <= legalConsentTemplate
															.getVersion()) {
								%>
								<img id="publish"
								src="<%=request.getContextPath()%>/evaluation/resources/_img/disabled_publish_button.gif"
								alt="disabled_publish" width="52" height="19" /> <%
 	} else {
 %> <img
								src="<%=request.getContextPath()%>/evaluation/resources/_img/enabled_publish_button.gif"
								alt="publish" width="52" height="19"
								onclick="publishLegalTemplate(<%=objLegalTemplate.getLcId()%>)" />
							</td>
							<%
								}
							%>
							<td class="last"><img
								src="<%=request.getContextPath()%>/evaluation/resources/_img/button_edit.gif"
								alt="Edit" width="40" height="18"
								onclick="editLegalTemplate(<%=objLegalTemplate.getLcId()%>)" />

							</td>
						</tr>
						<%
							}
									}
								}
						%>
					</table>
				<!-- </div> -->
			</s:form>


			<div class="clear"></div>
		</div>
		<!-- end #content -->

	</div>
	<!-- end #wrap -->

</body>
</html>