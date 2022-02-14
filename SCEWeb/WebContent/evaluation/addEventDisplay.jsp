<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.beans.Event"%>
<%@ page import="com.pfizer.sce.beans.EventsCreated"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.lang.Math.*"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.pfizer.sce.beans.Util"%>
<%@include file="IAM_User_Auth.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%
	EventsCreated events = (EventsCreated) request
			.getAttribute("events1");
	String eventStartDate = (String) events.getEventStartDate();
	String eventEndDate = (String) events.getEventEndDate();
							
	Integer id = (Integer) (events.getEventId());
	pageContext.setAttribute("id", id);
%>


<style type="text/css">
@import
url(<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar-win2k-cold-2.css)
;
</style>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/lang/calendar-en.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar-setup.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/js/DateValidator.js"></script>

<html>

<script language="javascript">
	function validateUserFormTwo() {
			

		<%-- document.getElementById("h_eventStatus").value = document.getElementById("eventStatus").value;
		document.getElementById("h_eventStartDate").value ="<%=events.getEventStartDate()%>";
		document.getElementById("h_eventEndDate").value ="<%=events.getEventEndDate()%>";
		document.getElementById("h_eventDescription").value ="<%=events.getEventDescription()%>";
		document.getElementById("h_numberOfLearners").value =<%=events.getNumberOfLearners()%>;
		document.getElementById("h_numberOfEval").value =<%=events.getNumberOfEval()%>;
		document.getElementById("h_typeOfEval").value ="<%=events.getTypeOfEval()%>"; --%>
		document.getElementById("h_eventId").value =<%=id%>;

		/* document.getElementById("h_eventStatus").value = document
				.getElementById("eventStatus").value;
		
		document.getElementById("h_eventStartDate").value = document
				.getElementById("eventStartDate").value;
		
		document.getElementById("h_eventEndDate").value = document
				.getElementById("eventEndDate").value;
		
		document.getElementById("h_eventDescription").value = document
				.getElementById("eventDescription").value;
		
		document.getElementById("h_numberOfEval").value = document
				.getElementById("numberOfEval").value;
		
		document.getElementById("h_typeOfEval").value = document
				.getElementById("typeOfEval").value;
		
		document.getElementById("h_numberOfLearners").value = document
				.getElementById("numberOfLearners").value; */

		window.document.forms[0].action = "gotoUpdateEvent";
		window.document.forms[0].submit();
	}

	function validateUserForm() {
		//  alert("inside one");
		var today = new Date();

		var evalDuration = document.getElementById('evalDuration').value
		var numberOfEval = document.getElementById('numberOfEval').value
		var numberOfLearners = document.getElementById('numberOfLearners').value
		var onlynumber = /^[0-9]+$/;
		var eventStartDate = document.getElementById('eventStartDate').value;
		var eventEndDate = document.getElementById('eventEndDate').value;
		today.setHours(00);
		today.setMinutes(00);
		today.setSeconds(00);
		today.setMilliseconds(00);

		var arrDate = eventStartDate.split("/");
		var arrDate2 = eventEndDate.split("/");
		var useDate = new Date(arrDate[2], arrDate[0] - 1, arrDate[1]);
		var useDate2 = new Date(arrDate2[2], arrDate2[0] - 1, arrDate2[1]);

		var validformat = /^\d{2}\/\d{2}\/\d{4}$/ //Basic check for format validity
		if (eventStartDate != '') {
			if (!validformat.test(eventStartDate)) {
				alert("Invalid Date Format. Please correct and submit again.");
				return false;
			}
		}
		if (eventEndDate != '') {
			if (!validformat.test(eventEndDate)) {
				alert("Invalid Date Format. Please correct and submit again.");
				return false;
			}
		}
		if (evalDuration.length > 0) {
			if (!(evalDuration.match(onlynumber))) {
				alert("Please enter only whole numbers for Evaluation Duration field");
				return false;
			}
		}
		/*Sanjeev: removed condition: Evaluation per day in 1-10 range while editing event*/
		 if (numberOfEval.length > 0) {
			/* if(numberOfEval<1 || numberOfEval>10){
		         alert("Please enter Evaluations per day value in the range 1-10 ");
		         return false;
		         }  */
			if (!(numberOfEval.match(onlynumber))) {
				alert("Please enter only whole numbers for Evaluations per day field");
				return false;
			}
		}

		if (numberOfLearners.length > 0) {
			if (!(numberOfLearners.match(onlynumber))) {
				alert("Please enter only whole numbers for Expected Number of Learners field");
				return false;
			}
		}

		if (eventStartDate == '') {
			alert("The Start Date is a mandatory field. Please provide a future Date");
			return false;
		}

		if (eventEndDate != '') {

			if (useDate2 < useDate) {
				alert("The End Date cannot be a date earlier to Start date. Please provide another Date");
				return false;
			}

		} else {
			alert("The End Date is a mandatory field. Please provide a future Date");
			return false;
		}

		/* document.getElementById("h_eventStatus").value = document
				.getElementById("eventStatus").value;
		document.getElementById("h_eventStartDate").value = document
				.getElementById("eventStartDate").value;
		document.getElementById("h_eventEndDate").value = document
				.getElementById("eventEndDate").value;
		document.getElementById("h_eventDescription").value = document
				.getElementById("eventDescription").value;
		document.getElementById("h_numberOfEval").value = document
				.getElementById("numberOfEval").value;
		document.getElementById("h_typeOfEval").value = document
				.getElementById("typeOfEval").value;
		document.getElementById("h_numberOfLearners").value = document
				.getElementById("numberOfLearners").value; */

				<%-- document.getElementById("h_eventStatus").value = document.getElementById("eventStatus").value;
				document.getElementById("h_eventStartDate").value ="<%=events.getEventStartDate()%>";
				document.getElementById("h_eventEndDate").value ="<%=events.getEventEndDate()%>";
				document.getElementById("h_eventDescription").value ="<%=events.getEventDescription()%>";
				document.getElementById("h_numberOfLearners").value =<%=events.getNumberOfLearners()%>;
				document.getElementById("h_numberOfEval").value =<%=events.getNumberOfEval()%>;
				document.getElementById("h_typeOfEval").value ="<%=events.getTypeOfEval()%>"; --%>
				document.getElementById("h_eventId").value =<%=id%>;
		window.document.forms[0].action = "gotoUpdateEvent";
		window.document.forms[0].submit();

	}
</script>

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

		<H3>Admin:Edit Event</H3>
		<div id="main_content">


			<s:form action="gotoUpdateEvent" id="AddEventForm">
<!-- Hidden fields  -->

	<%-- <s:hidden name="h_eventStatus" id="h_eventStatus" value=""></s:hidden>
	<s:hidden name="h_eventStartDate" id="h_eventStartDate" value=""></s:hidden>
	<s:hidden name="h_eventEndDate" id="h_eventEndDate" value=""></s:hidden>
	<s:hidden name="h_eventDescription" id="h_eventDescription" value=""></s:hidden>
	<s:hidden name="h_evalDuration" id="h_evalDuration" value=""></s:hidden>
	<s:hidden name="h_numberOfEval" id="h_numberOfEval" value=""></s:hidden>
	<s:hidden name="h_typeOfEval" id="h_typeOfEval" value=""></s:hidden>
	<s:hidden name="h_numberOfLearners" id="h_numberOfLearners" value=""></s:hidden> --%>
	
	<%-- <s:hidden name="eventStatus" id="h_eventStatus" ></s:hidden> --%>
	
	
	
				<table cellpadding="0" cellspacing="0"
					style="border: 0; colour: 'white';">
					<tr>
						<td width="5%" valign="top" style="border: 0">Business Unit</td>
						<td style="border: 0">
							 <s:label
								name="businessUnit" id="businessUnit"></s:label>

						</td>
					</tr>
					<tr>
						<td width="15%" valign="top" style="border: 0">Event Id</td>
				<%-- 		<%
							Integer id = (Integer) (events.getEventId());
								pageContext.setAttribute("id", id);
						%>
 --%>
						<td style="border: 0"><s:label name="eventId" id="eventId"></s:label>
							<s:hidden name="h_eventId" id="h_eventId" value="%{eventId}" />

							<%-- <%=events.getEventId()%> --%>
					</tr>

					<tr>
						<td width="5%" valign="top" style="border: 0">Event name</td>
						<td style="border: 0">
							<%-- <%=SCEUtils.ifNull(events.getEventName(), "&nbsp;")%> --%> <s:label
								name="eventName" id="eventName"></s:label>

						</td>
					</tr>

					<tr>
						<td width="15%" valign="top" style="border: 0">Event
							Description</td>
						<%
							if (("ACTIVE".equalsIgnoreCase(events.getEventStatus()))
										|| ("COMPLETE"
												.equalsIgnoreCase(events.getEventStatus()))) {
						%>


						<td style="border: 0">
							<%-- <%=SCEUtils.ifNull(events.getEventDescription(),"&nbsp;")%> --%>
							<s:label name="eventDescription" id="eventDescription"></s:label>

						</td>
						<s:hidden name="eventDescription" id="h_eventDescription" ></s:hidden>
						<%
							} else {
						%>
						<td style="border: 0"><s:textfield name="eventDescription"
								id="eventDescription"></s:textfield> <%
 	}
 %>
					</tr>

					<tr>
						<td width="15%" valign="top" style="border: 0">Start Date</td>
						<%
							if (("ACTIVE".equalsIgnoreCase(events.getEventStatus()))
										|| ("COMPLETE"
												.equalsIgnoreCase(events.getEventStatus()))) {
						%>


						<s:hidden name="eventStartDate" id="h_eventStartDate" ></s:hidden>
						<td style="border: 0"><s:label name="eventStartDate"
								id="eventStartDate">
							</s:label> <%-- <%=SCEUtils.ifNull(events.getEventStartDate(),"&nbsp;")%> --%>

						</td>
						<%
							} else {
						%>
						<td style="border: 0"><INPUT type="text" size="11"
							maxlength="10" value="<%=eventStartDate%>" name="eventStartDate"
							id="eventStartDate"
							onKeyUp="DateFormat('eventStartDate',event,false,'1','EventStart Date')">
								<IMG id="calImg_startDate"
								src="<%=request.getContextPath()%>/evaluation/resources/images/icon_calendar.gif"
								border="0" name="as_of_date_UP"> <script
										type="text/javascript">
									Calendar.setup({
										inputField : "eventStartDate",
										ifFormat : "%m/%d/%Y",
										button : "calImg_startDate",
										align : "Tl"
									});
								</script></td>

						<%
							}
						%>
					</tr>

					<tr>
						<td width="15%" valign="top" style="border: 0">End Date</td>
						<%
							if (("ACTIVE".equalsIgnoreCase(events.getEventStatus()))
										|| ("COMPLETE"
												.equalsIgnoreCase(events.getEventStatus()))) {
						%>
						<td style="border: 0"><s:label name="eventEndDate"
								id="eventEndDate"></s:label> <%-- <%=SCEUtils.ifNull(events.getEventEndDate(),"&nbsp;")%> --%>

						</td>
					<s:hidden name="eventEndDate" id="h_eventEndDate" ></s:hidden>



						<%
							} else {
						%>
						<td style="border: 0"><INPUT type="text" size="11"
							maxlength="10" value="<%=eventEndDate%>" name="eventEndDate"
							id="eventEndDate"
							onKeyUp="DateFormat('eventEndDate',event,false,'1','EventEnd Date ')">
								<IMG id="calImg_endDate"
								src="<%=request.getContextPath()%>/evaluation/resources/images/icon_calendar.gif"
								border="0" name="as_of_date_UP"> <script
										type="text/javascript">
									Calendar.setup({
										inputField : "eventEndDate",
										ifFormat : "%m/%d/%Y",
										button : "calImg_endDate",
										align : "Tl"
									});
								</script></td>
						<%
							}
						%>
					</tr>

					<tr>
						<td width="15%" valign="top" style="border: 0">Event Status</td>
						<%
							Date d = new Date();

								int index = events.getEventStartDate().indexOf("");
								int index2 = events.getEventEndDate().indexOf("");
								int number;

								String start = events.getEventStartDate().substring(index);
								String end = events.getEventEndDate().substring(index2);
								SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");

								Date startd = format1.parse(start.trim());
								Date endd = format1.parse(end.trim());
						%>
						<td style="border: 0"><s:select list="%{eventstatuses}"
								name="eventStatus" id="eventStatus"></s:select>
					</tr>

					<tr>
						<td width="15%" valign="top" style="border: 0">Number of days
							of event</td>
						<td style="border: 0"><%=((endd.getTime() - startd.getTime()) / (1000 * 60 * 60 * 24)) + 1%>
						</td>
					</tr>
					<%
						int reqGT = 0;
							double one = events.getNumberOfLearners() != null ? events
									.getNumberOfLearners().intValue() : 0;
							double two = (events.getNumberOfEval() != null ? events
									.getNumberOfEval().intValue() : 0)
									* (((endd.getTime() - startd.getTime()) / (1000 * 60 * 60 * 24)) + 1);

							if (events.getNumberOfLearners() != null
									&& (events.getNumberOfEval() != null && events
											.getEvalDuration() != null)) {
								// System.out.println("1************");
								if (events.getNumberOfEval().intValue() != 0
										&& events.getEvalDuration().intValue() != 0) {
									// System.out.println("2************");
									double three = one / two;
									double calGT = Math.ceil(three);

									reqGT = (int) calGT;

								}
							}
					%>
					<tr>
						<td width="15%" valign="top" style="border: 0">Evaluation
							Duration(min)</td>
						<%
							if ("ACTIVE".equalsIgnoreCase(events.getEventStatus())) {
						%>
						<td style="border: 0">
							<%-- <%=SCEUtils.ifNull(events.getEvalDuration(),"&nbsp;")%> --%>
							<s:label name="evalDuration" id="evalDuration"></s:label>

						</td>
						<s:hidden name="evalDuration" id="h_evalDuration" ></s:hidden>

						<%
							} else {
						%>
						<td style="border: 0"><s:textfield name="evalDuration"
								id="evalDuration"></s:textfield></td>
						<%
							}
						%>
					</tr>
					<tr>
						<tr>
							<td width="15%" valign="top" style="border: 0">Evaluations
								per day</td>
							<%
								if ("ACTIVE".equalsIgnoreCase(events.getEventStatus())) {
							%>



							<td style="border: 0">
								<%-- <%=SCEUtils.ifNull(events.getNumberOfEval(),"&nbsp;")%> --%>
								<s:label name="numberOfEval" id="numberOfEval"></s:label>

							</td>
							<s:hidden name="numberOfEval" id="h_numberOfEval" ></s:hidden>
							<%
								} else {
							%>
							<td style="border: 0"><s:textfield name="numberOfEval"
									id="numberOfEval"></s:textfield></td>
							<%
								}
							%>
						</tr>
						<tr>

							<td width="15%" valign="top" style="border: 0">Evaluation
								Type</td>
							<%
								if ("ACTIVE".equalsIgnoreCase(events.getEventStatus())) {
							%>



							<td style="border: 0">
								<%-- <%=SCEUtils.ifNull(events.getTypeOfEval(), "&nbsp;")%> --%>
								<s:label name="typeOfEval" id="typeOfEval"></s:label>

							</td>
								<s:hidden name="typeOfEval" id="h_typeOfEval" ></s:hidden>
							
							<%
								} else {
							%>
							<td style="border: 0"><s:select list="%{typeOfEvals}"
									name="typeOfEval" id="typeOfEval"></s:select> <%
 							}
 						 %>
						</tr>
						<tr>
							<td width="15%" valign="top" style="border: 0">Expected
								number of Learners</td>
							<%
								if ("ACTIVE".equalsIgnoreCase(events.getEventStatus())) {
							%>




							<td style="border: 0">
								<%-- <%=SCEUtils.ifNull(events.getNumberOfLearners(),"&nbsp;")%> --%>
								<s:label name="numberOfLearners" id="numberOfLearners"></s:label>
							</td>
								<s:hidden name="numberOfLearners" id="h_numberOfLearners" ></s:hidden> 
							
							<%
								} else {
							%>
							<td style="border: 0"><s:textfield name="numberOfLearners"
									id="numberOfLearners"></s:textfield> <%
 	}
 %>
						</tr>
						<tr>
							<td width="15%" valign="top" style="border: 0">Expected
								number of Guest Trainers</td>

							<td style="border: 0"><%=reqGT%></td>
						</tr>
				</table>
				<%
					if ("ACTIVE".equalsIgnoreCase(events.getEventStatus())) {
				%>
				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save.gif"
					width="27" height="19" onclick="validateUserFormTwo()" />
				<%
					} else {
				%>
				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save.gif"
					width="27" height="19" onclick="validateUserForm()" />
				<%
					}
				%>

			</s:form>
		</div>
		<!-- end #wrap -->

	</div>

</body>

</html>
<%
	request.removeAttribute("events1");
%>