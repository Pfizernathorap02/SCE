<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.db.SCEManager"%>
<%@ page import="com.pfizer.sce.beans.CourseDetails"%>
<%@ page import="com.pfizer.sce.beans.GuestTrainer"%>
<%@ page import="com.pfizer.sce.beans.Learner"%>
<%@ page import="com.pfizer.sce.db.SCEControlImpl"%>
<%@ page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp"%>

<%
	String[] events = (String[]) request.getAttribute("events");
    session.setAttribute("events",events);
  
%>

<style type="text/css">
@import
url(<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar-win2k-cold-2.css);
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
<!--[if IE 6]><LINK href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" type=text/css rel=stylesheet><![endif]-->
 <!-- Sorting begin sanjeev-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/sorttable.js"></script>        
        <!-- Sorting end sanjeev-->

<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/js/sorttable.js"></script>

<%
    String[] course=null;
int totalLearnerCourseCompleted = 0;
    if(request.getAttribute("courses")!=null){
        course=(String[])request.getAttribute("courses");
        session.setAttribute("courses",course);
    }
    String courseSel=null;
    if(request.getAttribute("course")!=null){
        courseSel=(String)request.getAttribute("course");
    }
    String eventSel=null;
    if(request.getAttribute("event")!=null){
        eventSel=(String)request.getAttribute("event");
    }
    Learner[] learners=null;
    if(request.getAttribute("learners")!=null){
        learners=(Learner[])request.getAttribute("learners");
        
        /*Start-Simi Sudhakaran- Provide the total count of learners and the count of learners marked as complete.*/		
        for (int i = 0; i < learners.length; i++) {
			Learner learner = learners[i];
			if(learner.getIsCourseComplete().equalsIgnoreCase("Y")){
				totalLearnerCourseCompleted++;				
			}			
		}
		System.out.println("totalLearnerCourseCompleted- "+totalLearnerCourseCompleted);
		/*End-Simi Sudhakaran- Provide the total count of learners and the count of learners marked as complete.*/
        
    }
    String toDate="";
    if(request.getAttribute("toDate")!=null){
        toDate=(String)request.getAttribute("toDate");
    }
    String frmDate="";
    if(request.getAttribute("frmDate")!=null){
        frmDate=(String)request.getAttribute("frmDate");
    }
    String message=null;
    if(request.getAttribute("message")!=null){
        message=(String)request.getAttribute("message");
    }
    %>
<style>
.view_learner table {
	border: 0px;
}

.view_learner td {
	border: 0px;
}
</style>
<script language="javascript">
      
       function checkSelectedEvent(){
       var event=document.getElementById('i_mark_status_vt_event').value;
       if(event== "select"){
        alert("Please select Event");
        }
        document.forms[0].action="viewCourseForProduct?event="+event;
       // alert(document.forms[0].action);
        document.forms[0].submit();
       
       }
       function validateCourseAndDate()
       {
            var course=document.getElementById('i_mark_status_vt').value;
        if(course == "select"){
        alert("Please select Course");
        return false;
        }
        var regDateFrm = document.getElementById('regDateFrm').value;
        var regDateTo = document.getElementById('regDateTo').value;
        //alert("frm"+regDateFrm);
        //alert("TO"+regDateTo);
         var validformat=/^\d{2}\/\d{2}\/\d{4}$/ //Basic check for format validity
        if (regDateFrm==''){
        alert("Please Enter Course Start Date.");
        return false;
        }
         if (regDateTo==''){
        alert("Please Enter Course End Date.");
        return false;
        }
        if (regDateFrm!=''){
        if (!validformat.test(regDateFrm)){
        alert("Invalid Course Start Date Format. Please correct and submit again.");
        return false;
        }
         var monthfield=regDateFrm.split("/")[0];
        var dayfield=regDateFrm.split("/")[1];
        var yearfield=regDateFrm.split("/")[2];
        var dayobj = new Date(yearfield, monthfield-1, dayfield);
        if ((dayobj.getMonth()+1!=monthfield)||(dayobj.getDate()!=dayfield)||(dayobj.getFullYear()!=yearfield)){
            alert("Invalid Day, Month, or Year range detected. Please enter a valid Course Start date in mm/dd/yyyy format.");
            return false;
        }
        }
          if (regDateTo!=''){
        if (!validformat.test(regDateTo)){
        alert("Invalid Course End Date Format. Please correct and submit again.");
        return false;
        }
         var monthfield=regDateTo.split("/")[0];
        var dayfield=regDateTo.split("/")[1];
        var yearfield=regDateTo.split("/")[2];
        var dayobj = new Date(yearfield, monthfield-1, dayfield);
        if ((dayobj.getMonth()+1!=monthfield)||(dayobj.getDate()!=dayfield)||(dayobj.getFullYear()!=yearfield)){
            alert("Invalid Day, Month, or Year range detected. Please enter a valid Course End date in mm/dd/yyyy format.");
            return false;
        }
        }
        
        var arrDateFrm= regDateFrm.split("/");
        var start = new Date(arrDateFrm[2], arrDateFrm[0]-1, arrDateFrm[1]);
        var arrDateTo= regDateTo.split("/");
        var end = new Date(arrDateTo[2], arrDateTo[0]-1, arrDateTo[1]);
        if(end<start){
        alert("Course End date cannot be less than Course Start date");
        return false;
        }
        return true;
       }
       
       
        function checkSelectedCourse(){
        if(!validateCourseAndDate()){
        return false;
        }
        var course=document.getElementById('i_mark_status_vt').value;
        var regDateFrm = document.getElementById('regDateFrm').value;
        var regDateTo = document.getElementById('regDateTo').value;
        document.getElementById('hdnDateFrm').value=regDateFrm;
        document.getElementById('hdnDateTo').value=regDateTo;
        //alert(regDate);
        var event=document.getElementById('i_mark_status_vt_event').value;
        document.getElementById('hdnEvent').value=event;
 
        document.forms[0].action="gotoViewLearner?course="+course;
        document.forms[0].submit();
        }
        
        function checkLearnerSelected(){
        if(!validateCourseAndDate()){
        return false;
        }
        var event=document.getElementById('i_mark_status_vt_event').value;
        var regDateFrm = document.getElementById('regDateFrm').value;
        var regDateTo = document.getElementById('regDateTo').value;
        var course=document.getElementById('i_mark_status_vt').value;
        if(event !='<%=eventSel%>'){
        alert("Learner Details not valid.Please click in 'View Learners' button");
        return false;
        }
        if(course != '<%=courseSel%>'){
        alert("Learner Details not valid.Please click in 'View Learners' button");
        return false;
        }
        if(regDateFrm !='<%=frmDate%>'){
        alert("Learner Details not valid.Please click in 'View Learners' button");
        return false;
        }
        if(regDateTo !='<%=toDate%>') {
			alert("Learner Details not valid.Please click in 'View Learners' button");
			return false;
		}
		document.getElementById('hdnEvent1').value = event;
		document.getElementById('hdnDateFrm1').value = regDateFrm;
		document.getElementById('hdnDateTo1').value = regDateTo;
		var course = document.getElementById('i_mark_status_vt').value;
		var num = 0;
		var checks = document.getElementsByName('iselectLearnerChkBox');
		for (var i = 0; i < checks.length; i++) {
			if (checks[i].checked) {
				num++;
				break;
			}
		}
		if (num == 0) {
			alert("Select Atleast One Learner");
			return false;
		}
		document.forms[1].action = "gotoSelectLearner?course=" + course;
		document.forms[1].submit();
	}

	function selectAllLearner() {
		var selectAll = document.getElementById('iselectAllLearnerChkBox').checked;
		var checks = document.getElementsByName('iselectLearnerChkBox');
		if (selectAll) {

			for (var i = 0; i < checks.length; i++) {
				checks[i].checked = true;
			}
		} else {
			for (var i = 0; i < checks.length; i++) {
				checks[i].checked = false;
			}
		}
	}

	function checkSelectAllChkBox() {
		var num = 0;
		var checks = document.getElementsByName('iselectLearnerChkBox');
		for (var i = 0; i < checks.length; i++) {
			if (checks[i].checked) {
				num++;
			}
		}
		if (num == checks.length) {
			document.getElementById('iselectAllLearnerChkBox').checked = true;
		} else {
			document.getElementById('iselectAllLearnerChkBox').checked = false;
		}
	}
</script>

</head>

<body id="p_add_GT" class="addGT">
	<div id="wrap">

		<div id="top_head">
			<h1>Pfizer</h1>
			<h2>Sales Call Evaluation</h2>

			<%@include file="navbar.jsp"%>
			<!-- end #top_head -->
		</div>
		<div id="eval_sub_nav">
			<s:a action="admin">
				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoadmin.gif"
					alt="Back to main admin" width="119" height="18" />
			</s:a>
		</div>

		<h3>Admin: Virtual Training Status</h3>

		<div id="main_content">
			<%
				if (message != null) {
			%>

			<font color="Red">
			<p><%=message%></p>
			</font>
			<%
				}
			%>
		<s:form action="">
				<div class="view_learner">
					<input type="hidden" value="" id="hdnEvent" name="hdnEvent" /> <input
						type="hidden" value="" id="hdnCourse" name="hdnCourse" /> <input
						type="hidden" value="" id="hdnDateFrm" name="hdnDateFrm">
						<input type="hidden" value="" id="hdnDateTo" name="hdnDateTo">
							<table>
								<tr>
									<td width="100">Event:*</td>
									<td><select name="mark_status_vt_event"
										id="i_mark_status_vt_event" onChange="checkSelectedEvent()">
											<option value="select">--Select--</option>
											<%
												if (events != null) {
														for (int i = 0; i < events.length; i++) {
															if (eventSel != null
																	&& eventSel.equalsIgnoreCase(events[i])) {
											%>
											<option value="<%=events[i]%>" selected="selected" ><%=events[i]%></option>
											<%
												} else {
											%>
											<option value="<%=events[i]%>"><%=events[i]%></option>
											<%
												}
														}
													}
											%>
									</select></td>
								</tr>
								<tr>
									<%
										if (eventSel != null && (course != null && course.length <= 0)) {
									%>
									<td width="100"></td>
									<td><font
										style="color:red; font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';">No
										courses present for selected event</font></td>
									<%
										} else {
									%>
									<td width="100">Course:*</td>
									<%
										if (eventSel == null || eventSel.equalsIgnoreCase("select")) {
									%>
									<td><select name="mark_status_vt" id="i_mark_status_vt"
										disabled>
											<option value="select">--Select--</option>
									</select></td>
									<%
										} else {
									%>
									<td><select name="mark_status_vt" id="i_mark_status_vt"
										style="width: auto !important">
											<option value="select">--Select--</option>
											<%
												if (course != null) {
																for (int i = 0; i < course.length; i++) {
																	if (courseSel != null
																			&& courseSel
																					.equalsIgnoreCase(course[i])) {
											%>
											<option value="<%=course[i]%>" selected><%=course[i]%></option>
											<%
												} else {
											%>
											<option value="<%=course[i]%>"><%=course[i]%></option>
											<%
												}
																}
															}
											%>
									</select></td>
									<%
										}
											}
									%>
								</tr>
								<%
									if (course == null || course.length > 0) {
								%>
								<tr>
									<td width="100">Course Start Date* (mm/dd/yyyy)</td>
									<%
										if (eventSel == null || eventSel.equalsIgnoreCase("select")) {
									%>
									<td><INPUT type="text" size="11" maxlength="10"
										value="<%=frmDate%>" name="regDateFrm" id="regDateFrm"
										disabled> <IMG id="calImg_regDateFrm"
											src="<%=request.getContextPath()%>/evaluation/resources/images/icon_calendar.gif"
											border="0" name="as_of_date_UP"></td>
									<%
										} else {
									%>
									<td><INPUT type="text" size="11" maxlength="10"
										value="<%=frmDate%>" name="regDateFrm" id="regDateFrm">
											 <IMG id="calImg_regDateFrm"
											src="<%=request.getContextPath()%>/evaluation/resources/images/icon_calendar.gif"
											border="0" name="as_of_date_UP"></td>

									<script type="text/javascript">
										Calendar.setup({
											inputField : "regDateFrm",
											ifFormat : "%m/%d/%Y",
											button : "calImg_regDateFrm",
											align : "Tl"
										});
									</script>
									<%
										}
									%>
								</tr>
								<tr>
									<td width="100">Course End Date* (mm/dd/yyyy)</td>
									<%
										if (eventSel == null || eventSel.equalsIgnoreCase("select")) {
									%>
									<td><INPUT type="text" size="11" maxlength="10"
										value="<%=toDate%>" name="regDateTo" id="regDateTo" disabled>
											<img id="calImg_regDateTo"
											src="<%=request.getContextPath()%>/evaluation/resources/images/icon_calendar.gif"
											border="0" name="as_of_date_UP"></td>
									<%
										} else {
									%>
									<td><INPUT type="text" size="11" maxlength="10"
										value="<%=toDate%>" name="regDateTo" id="regDateTo"> <img
											id="calImg_regDateTo"
											src="<%=request.getContextPath()%>/evaluation/resources/images/icon_calendar.gif"
											border="0" name="as_of_date_UP"></td>

									<script type="text/javascript">
										Calendar.setup({
											inputField : "regDateTo",
											ifFormat : "%m/%d/%Y",
											button : "calImg_regDateTo",
											align : "Tl"
										});
									</script>
									<%
										}
									%>
								</tr>
								<tr>

									<%
										if (eventSel == null || eventSel.equalsIgnoreCase("select")) {
									%>
									<td><input type="button" disabled value="View Learners"
										class="buttonStyles" onClick="checkSelectedCourse()" /></td>
									<%
										} else {
									%>
									<td><input type="button" value="View Learners"
										class="buttonStyles" onClick="checkSelectedCourse()" /></td>
									<%
										}
									%>
								</tr>
								<%
									}
								%>
								</div>

							</table>
				</div>
			</s:form>
			<div class="clear"></div>
			<%
				if (learners != null && learners.length > 0) {
			%>
			<s:form action="gotoSelectLearner">
				<input type="hidden" value="" id="hdnEvent1" name="hdnEvent1" />
				<input type="hidden" value="" id="hdnCourse1" name="hdnCourse1" />
				<input type="hidden" value="" id="hdnDateFrm1" name="hdnDateFrm1">
					<input type="hidden" value="" id="hdnDateTo1" name="hdnDateTo1">
					
					<div class="add_user"><pre>Total(Learners)- <%=learners.length%></pre></div>
						<div class="add_user"><pre>Total(Learners with Training status-'Complete')- <%=totalLearnerCourseCompleted%></pre></div>
					
						<h3>Learner Details</h3>

						<table cellspacing="0">
							<tr>
								<th class="sort_up" onclick="ts_resortTable(this, 0);return false;" >First Name</th>
								<th class="sort_up" onclick="ts_resortTable(this, 1);return false;" >Last Name</th>
								<th>Email Address</th>
								<th>Location</th>
								<th>Course Name</th>
								<th>Course Code</th>
								<th>Training Status <input style="float: left; width: 90%"
									type="checkbox" name="selectAllLearnerChkBox"
									id="iselectAllLearnerChkBox" onclick="selectAllLearner()" />
								</th>
							</tr>
							<%
								for (int i = 0; i < learners.length; i++) {
											Learner learner = learners[i];
							%>
							<tr>
								<td><%=SCEUtils.ifNull(learner.getFirstName(),
								"&nbsp")%></td>
								<td><%=SCEUtils.ifNull(learner.getLastName(),
								"&nbsp")%></td>
								<td><%=SCEUtils.ifNull(learner.getEmailAddress(),
								"&nbsp")%></td>
								<td><%=SCEUtils.ifNull(learner.getLocation(),
								"&nbsp")%></td>
								<td><%=SCEUtils.ifNull(learner.getCourse(), "&nbsp")%></td>
								<td><%=SCEUtils.ifNull(learner.getCourseCode(),
								"&nbsp")%></td>
								<%
									if (learner.getIsCourseComplete().equalsIgnoreCase("Y")) {
								%>
								<td>Complete</td>
								<%
									} else {
								%>
								<td><input type="checkbox" name="iselectLearnerChkBox"
									id="iselectLearnerChkBox"
									value="<%=learner.getEmailAddress()%>"
									onclick="checkSelectAllChkBox()" /></td>
								<%
									}
								%>
							</tr>
							<%
								}
							%>
						</table> <!--<input type="button" value="Save"/>--> <img
						src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save.gif"
						alt="Save" width="38" height="19" onClick="checkLearnerSelected()" />
			</s:form>
			<%
				} else if (learners != null && learners.length == 0
						&& !courseSel.equalsIgnoreCase("select")) {
			%>
			<table style="border: 0">
				<tr>
					<td style="border: 0"><font
						style="color:red; font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';">No
						Learners are present for selected Course</font></td>
				</tr>
			</table>
			<%
				}
			%>
		</div>
		<!-- end #content -->

	</div>
	<!-- end #wrap -->

</body>

</html>