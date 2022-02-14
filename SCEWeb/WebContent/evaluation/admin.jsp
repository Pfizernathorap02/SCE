<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", -1);
%>

<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@include file="IAM_User_Auth.jsp"%>
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
<body id="p_admin_users" class="admin">
	<div id="wrap">

		<div id="top_head">
			<h1>Pfizer</h1>
			<h2>Sales Call Evaluation</h2>

			<%@include file="navbar.jsp"%>
			<!-- end #top_head -->
		</div>
		<%
			//User userAdmin = (User)session.getAttribute("user");
			//  boolean isAdmin = user != null && "SCE_Administrators".equalsIgnoreCase(user.getUserGroup()) || "SCE_OpsManager".equalsIgnoreCase(user.getUserGroup());
			String userGrp = user.getUserGroup();
			String opsMgrAccess = "N";
			if (userGrp == null) {
			} else if (userGrp.equalsIgnoreCase("SCE_OpsManager")) {
				opsMgrAccess = "Y";
			}
		%>

		<h3>Admin: Main</h3>
		<div id="main_content">

			<s:form action="gotoSCEReport" tagId="SearchForAttendeeForm">
				<fieldset>
					<%
						if (opsMgrAccess.equalsIgnoreCase("Y")) {
					%>
					<div>
						<s:url action="gototFieldFacultyMaintenance" var="gotETemp"></s:url>
						<s:a href="%{gotETemp}">Field Faculty Maintenance</s:a>
					</div>
					<%
						} else {
					%>
					<div>
						<s:url action="gotoUserAdmin" var="adminUrl"></s:url>
						<s:a href="%{adminUrl}">User Admin</s:a>
					</div>
					<div>
						<s:url action="templateAdmin" var="schUrl"></s:url>
						<s:a href="%{schUrl}">Evaluation Template Maintenance</s:a>
					</div>
					<div>
						<s:url action="gotoEmailTemplateAdmin" var="gotETemp"></s:url>
						<s:a href="%{gotETemp}">Email Template Maintenance</s:a>
					</div>
					<div>
						<s:url action="gotoLegalTemplate" var="gotETemp"></s:url>
						<s:a href="%{gotETemp}">Legal Consent Template Maintenance</s:a>
					</div>

					<div>
						<s:url action="gotoEvalTemplateMapping" var="gotETemp"></s:url>
						<s:a href="%{gotETemp}">LMS Course to Evaluation Template Mapping</s:a>
					</div>

					<div>
						<s:url action="gotoEventManagement" var="gotETemp"></s:url>
						<s:a href="%{gotETemp}">Evaluation Event Management</s:a>
					</div>
					<!--    <div><s:url action="gotoAddEvent">Add a New Event</s:url></div>   -->

					<div>
						<s:url action="gototFieldFacultyMaintenance" var="gotETemp"></s:url>
						<s:a href="%{gotETemp}">Field Faculty Maintenance</s:a>
					</div>
					<!-- Changes done for SCE Enancement by SHARMM61 -->

					<!--     <div><s:url action="gotoEventMapping">Event: Course-Product Mapping</s:url></div>      -->

					<div>
						<s:url action="gotoFetchGTs" var="EventInvitationUrl"></s:url>
						<s:a href="%{EventInvitationUrl}">Event Invitation</s:a>
					</div>
					<!-- Changes end for SCE Enancement by SHARMM61 -->
					<div>
						<s:url action="gotovirtualtrainingstatus" var="virStat"></s:url>
						<s:a href="%{virStat}">Mark the status of Virtual trainings as complete</s:a>
					</div>
					<div>
						<s:url action="trainerLearnerMapping" var="gotETemp"></s:url>
						<s:a href="%{gotETemp}">Learner Trainer Mapping</s:a>
					</div>
					<%-- <div>
						<s:url action="gotoselectgt">Selection of Guest Trainer for Evaluation </s:url>
					</div>  --%>
					<div>
						<s:url action="goToWebEx" var="webExUrl">
						</s:url>
						<s:a href="%{webExUrl}">Event WebEx Maintenance</s:a>
					</div>

					<!-- ADDED BY MANISH FOR UPDATING APPROVERS -->
					<%-- <div>
						<s:url action="goToApprovers" var="gotETemp">
						</s:url>
						<s:a href="%{gotETemp}">Approvers</s:a>
					</div> --%>

					<!--End Code  -->
					
					<!-- code added by shaikh07 for Role Mapping for version 3.4 RFC#1136920 -->
					<div>
						<s:url action="goToRoleMapping" var="gotETemp">
						</s:url>
						<s:a href="%{gotETemp}">Role Mapping</s:a>
					</div>
					<!--End Code for Role Mapping for version 3.4 RFC#1136920 -->
					<!-- code added by thorap02 for BU Mapping  -->
					<div>
						<s:url action="goToBUMapping" var="gotETemp">
						</s:url>
						<s:a href="%{gotETemp}">BU Mapping</s:a>
					</div>
					<!--End Code for BU Mapping  -->
					<%
						}
					%>
				</fieldset>
			</s:form>

		</div>
		<!-- end #content -->

	</div>
	<!-- end #wrap -->

</body>

</html>
