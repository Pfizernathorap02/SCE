<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
<%
    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", -1); 
%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>


<%@ page import="com.pfizer.sce.beans.Event"%>
<%@ page import="com.pfizer.sce.beans.EventsCreated"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.pfizer.sce.beans.Util"%>
<%@include file="IAM_User_Auth.jsp"%>

<%@ taglib prefix="s" uri="/struts-tags"%>


<%
	EventsCreated eventsList[] = (EventsCreated[]) request
			.getAttribute("eventsList");
%>

<script language="javascript">
    function editEvent(eventId) {
    	
       

        window.document.getElementById('selEventId').value = eventId ;
        //alert(window.document.getElementById('selEventId').value);
        window.document.forms[0].action="gotoEditEvent.action";                
        window.document.forms[0].submit();    }
     
     
        
     function removeEvent(eventId) {
            if (confirm('Are you sure you want to remove this event?')) {
        window.document.getElementById('selEventId').value = eventId ;
        window.document.forms[0].action="gotoRemoveEvent";                
        window.document.forms[0].submit();   
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
<link
	href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css"
	rel="stylesheet" type="text/css" media="all" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/js/validate.js"></script>
<!--<link href="<%=request.getContextPath()%>/evaluation/resources/_css/admin.css" rel="stylesheet" type="text/css" media="all" />-->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/js/sorttable.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/js/validate.js"></script>
</head>

<body>

	<s:hidden name="pageName" id="pageName" value="retrieveMappingDetails"></s:hidden>
	<s:hidden name="hdnSelectedTemp" id="hdnSelectedTempId" value=""></s:hidden>
	<s:hidden name="hdnSelectedTemplate" id="hdnSelectedTemplateId" value=""></s:hidden>
	<s:hidden name="alreadyMappedActPk" id="hdnAlMapActPk" value=""></s:hidden>
	<s:hidden name="hdnSavableRecs" id="hdnSavableRecsId" value=""></s:hidden>

	<div id="wrap">

		<div id="top_head">
			<h1>Pfizer</h1>
			<h2>Sales Call Evaluation</h2>

			<%@include file="navbar.jsp"%>

			<!-- end #top_head -->
		</div>

		<div id="eval_sub_nav">

			<s:a action="admin" id="eventDisplayForm">
				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoadmin.gif"
					alt="Back to main admin" width="119" height="18" />
			</s:a>

		</div>

		<!-- end #eval_sub_nav -->

		<s:form action="gotoEditEvent" id="allEventsForm">

			<H3>Admin : Event Maintenance</H3>

			<div id="main_content">
				<div style="float: right">

					

					<s:url action="gotoAddEvent" var="urlTag"></s:url>
					<s:a href="%{urlTag}">Add a New Event</s:a>

				</div>
				<br>

					<div style="float: right">

						

						<s:url action="gotoEventMapping" var="urlTag"></s:url>
						<s:a href="%{urlTag}">Manage Event-Product-Course Mapping</s:a>

					</div>
					
					<s:hidden name="selEventId" id="selEventId" value=""></s:hidden>

					<table cellspacing="0">
						<tr>
							<th>Event Name</th>
							<th>Event Description</th>
							<th>Event Start Date</th>
							<th>Event End Date</th>
							<th>Event Status</th>
							<th>Evaluation Duration</th>
							<th>Evaluations/day</th>
							<th>Evaluation Type</th>
							<th>Learners</th>

							<th class="last"></th>
						</tr>
						<%
							EventsCreated event = null;
								if (eventsList != null) {
									for (int i = 0; i < eventsList.length; i++) {
										event = eventsList[i];
						%>
						<tr>
							<td><%=SCEUtils.ifNull(event.getEventName(),
								"&nbsp;")%></td>
							<td><%=SCEUtils.ifNull(event.getEventDescription(),
								"&nbsp;")%></td>
							<td><%=SCEUtils.ifDateNull(event.getEventStartDate(),
								"&nbsp;")%></td>
							<td><%=SCEUtils.ifDateNull(event.getEventEndDate(),
								"&nbsp;")%></td>
							<td><%=SCEUtils.ifNull(event.getEventStatus(),
								"&nbsp;")%></td>
							<td><%=SCEUtils.ifNull(event.getEvalDuration(),
								"&nbsp;")%></td>
							<td><%=SCEUtils.ifNull(event.getNumberOfEval(),
								"&nbsp;")%></td>
							<td><%=SCEUtils.ifNull(event.getTypeOfEval(),
								"&nbsp;")%></td>
							<td><%=SCEUtils.ifNull(event.getNumberOfLearners(),
								"&nbsp;")%></td>

							<td class="last">
								<%
									if ("COMPLETE".equalsIgnoreCase(event.getEventStatus())) {
								%> <img
								src="<%=request.getContextPath()%>/evaluation/resources/_img/disable_edit.gif"
								alt="Edit" width="40" height="18" /> <img
								src="<%=request.getContextPath()%>/evaluation/resources/_img/disable_remove.gif"
								id="remove" alt="Remove" width="56" height="18" /> <%
 	} else if ("ACTIVE".equalsIgnoreCase(event
 						.getEventStatus())) {
 %> <img
								src="<%=request.getContextPath()%>/evaluation/resources/_img/button_edit.gif"
								alt="Edit" width="40" height="18"
								onclick="return editEvent(<%=event.getEventId()%>)" /> <img
								src="<%=request.getContextPath()%>/evaluation/resources/_img/disable_remove.gif"
								id="remove" alt="Remove" width="56" height="18" /> <%
 	} else {
 %> <img
								src="<%=request.getContextPath()%>/evaluation/resources/_img/button_edit.gif"
								alt="Edit" width="40" height="18"
								onclick="return editEvent(<%=event.getEventId()%>)" /> <img
								src="<%=request.getContextPath()%>/evaluation/resources/_img/button_remove.gif"
								id="remove" alt="Remove" width="56" height="18"
								onclick="return removeEvent(<%=event.getEventId()%>);" /> <%
 	}
 %>
							</td>
						</tr>
						<%
							}
								}
						%>

					</table>
		</s:form>


	</div>
	<!-- end #content -->



</body>
</html>
