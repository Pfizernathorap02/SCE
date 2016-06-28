<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.beans.TemplateVersion"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%@include file="IAM_User_Auth.jsp"%>
<%
	String message = (String) request.getAttribute("message");
	TemplateVersion[] templateVersions = (TemplateVersion[]) request
			.getAttribute("templateVersions");

	// Integer publishedTemplateVersion= new Integer((String)request.getAttribute("publishedTemplateVersion"));
	DateFormat dateFormatter = new SimpleDateFormat(
			SCEUtils.DEFAULT_DATE_FORMAT);
	DateFormat dateFormatterTime = new SimpleDateFormat(
			SCEUtils.DEFAULT_TIME_FORMAT);
	//MODIFIED ON 9 DEC starts
	session.setAttribute("uploadcomplete", null);
	//MODIFIED ON 9 DEC ends
%>

<script language="javascript">
    function editTemplateVersion(templateVersionId, templateName) {
     	//alert("inside the edit javascript")
       // window.document.getElementById('selTemplateVersionId').value = templateVersionId;
    	//alert("the value is"+templateVersionId);
       // window.document.getElementById('selTemplateName').value = templateName;
//alert("the name is"+templateName);
       document.getElementById("templateVersionId").value=templateVersionId;
       document.getElementById("templateName").value=templateName;
       
       window.document.forms[0].action="editEvaluationTemplate";
    //   alert("before the submit")
      // document.forms[0].submit();                                
       window.document.forms[0].submit();        
    }
    
    function uploadTemplate(templateVersionId, templateName) {
        window.document.getElementById('selTemplateVersionId').value = templateVersionId;
        window.document.getElementById('selTemplateName').value = templateName;
                        
        window.document.forms[0].action="uploadBlankForm";
        window.document.forms[0].submit();        
    }
    
    function openUploadWindow(templateName,templateVersion,templateVersionId,scoringSystemIdentifier) {

      //alert("inside the open upload window method");
        templateName = templateName.replace(/&/g, "%26");
       
        
        templateName = templateName.replace(/#/g, "%23");
      
        
        window.open('evaluation/uploadBlankForm.jsp?templateName='+templateName+'&templateVersion='+templateVersion+'&templateVersionId='+templateVersionId+'&scoringSystemIdentifier='+scoringSystemIdentifier,'upload_window','status=yes,scrollbars=yes,height=300,width=500,resizable=yes');                    
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

		<h3>Admin: Evaluation Template Maintenance</h3>

		<div id="main_content">
			<%
				if (message != null && !"".equals(message.trim())) {
			%>
			<%=message%>
			<%
				}
			%>
			<s:form action="templateAdmin" id="allTemplatesForm" method="post">
				<s:url action="createEvaluationTemplate" var="addNewEvalTemplate"></s:url>
				<s:a href="%{addNewEvalTemplate}">
					<img
						src="<%=request.getContextPath()%>/evaluation/resources/_img/button_add_new_eval_template.gif"
						alt="Add New Evaluation Template" width="169" height="19" />
				</s:a>
  <s:hidden name="templateVersionId" id="templateVersionId" value=""/>
               <s:hidden name="templateName" id="templateName" value=""/>

				<s:hidden name="pageName" value="adminTemplate" />
				<!--ADD NEW EVALUATION BUTTON HAS TO COME HERE -->


				<table cellspacing="0">
					<tr>
						<th >Template Name</th>
						<th>Version</th>
						<th>Date Modified</th>
						<th>Time Modified (EST)</th>
						<th>Modified By</th>
						<th class="last1"></th>
						<th class="last"></th>
					</tr>
					<%
						TemplateVersion objTemplateVersion = null;
					
							if (templateVersions != null) {
								for (int i = 0; i < templateVersions.length; i++) {
									Integer maxVersion = null;
									objTemplateVersion = templateVersions[i];
									
									if (0 == i) {
										maxVersion = templateVersions[i].getVersion();
									} else {
										if (i < templateVersions.length
												&& templateVersions[i].getTemplateName() != null) {
											if (templateVersions[i].getTemplateName()
													.equalsIgnoreCase(
															templateVersions[i - 1]
																	.getTemplateName())) {
												maxVersion = null;
											} else {
												maxVersion = templateVersions[i]
														.getVersion();
											}
										}

									}
									if (objTemplateVersion.getTemplateId().intValue() < 5
											|| objTemplateVersion.getTemplateId()
													.intValue() > 7) {
										
									}
					%>

					<tr>
						<td><%=SCEUtils.ifNull(
									objTemplateVersion.getTemplateName(),
									"&nbsp;")%></td>



						<td><%=objTemplateVersion.getVersion()%></td>
						<td><%=SCEUtils.ifNull(
									objTemplateVersion.getModifiedDate(),
									dateFormatter, "&nbsp;")%></td>
						<td><%=SCEUtils.ifNull(
									objTemplateVersion.getModifiedDate(),
									dateFormatterTime, "&nbsp;")%></td>
						<td><%=SCEUtils.ifNull(
									objTemplateVersion.getCreatedByNtid(),
									"&nbsp;")%></td>

						<%
							// if(maxVersion==null){
											//----above code in case publish flag has null values--// 

											if (maxVersion == null
													|| objTemplateVersion.getPublishFlag()
															.equals("Y")) {
						%>

						<td class="last1"><img
							src="resources/_img/disabled_upload_&_publish_button.gif"
							alt="upload&publish" width="100" height="19" /></td>


						<%
							} else {
								%>
						<td class="last1"><img
							src="resources/_img/upload_&_publish_button.gif"
							alt="upload&publish" width="100" height="19"
							onclick="openUploadWindow('<%=objTemplateVersion.getTemplateName()%>',<%=objTemplateVersion.getVersion()%>,<%=objTemplateVersion.getId()%>,'<%=objTemplateVersion
										.getScoringSystemIdentifier()%>')" /></td>
						<%
							}
						%>


						<td class="last">
							
							 <img
							src="<%=request.getContextPath()%>/evaluation/resources/_img/button_edit.gif"
							alt="Edit" width="40" height="18"
							onclick="return editTemplateVersion(<%=objTemplateVersion.getId()%>, '<%=SCEUtils.ifNull(
									objTemplateVersion.getTemplateName(),
									"&nbsp;")%>')" />
						

						</td>
					</tr>
					<%
						}
								
								}
								//  System.out.println(":::::::::::::IN THE JSP::::::::::::::::::");

							
					%>
				</table>
			</s:form>


			<div class="clear"></div>
		</div>
		<!-- end #content -->

	</div>
	<!-- end #wrap -->

</body>
</html>