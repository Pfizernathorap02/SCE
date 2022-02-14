<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.pfizer.sce.beans.Util"%>
<%@ page import="java.util.Date"%>
<%@page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@include file="IAM_User_Auth.jsp"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<%  
	 String actionStatus = "";
	 String eventStartDate = "";
	 String eventEndDate = "";
	 String message= (String)request.getAttribute("message");
	 
		String[] buList = null;
		SCEManagerImpl sceManager = new SCEManagerImpl();	
	
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

<script language="javascript">
function fetchBUList(){
	<%buList = sceManager.getBUList();%>
	}
    function validateUserForm() {
  	 var today = new Date();
  	 var eventName = document.getElementById('eventName').value;
 	 var evalDuration=document.getElementById('evalDuration').value
 	 var businessUnit = document.getElementById('businessUnit').value;//added by muzees for PBG and UpJOHN
 		var numberOfEval=document.getElementById('numberOfEval').value
		 var numberOfLearners=document.getElementById('numberOfLearners').value
 		 var onlynumber= /^[0-9]+$/;
      var eventStartDate = document.getElementById('eventStartDate').value;
      var eventEndDate = document.getElementById('eventEndDate').value;
     today.setHours(00);
      today.setMinutes(00);
      today.setSeconds(00);
     today.setMilliseconds(00);
     
     var arrDate= eventStartDate.split("/");
     var arrDate2= eventEndDate.split("/");     
     var useDate = new Date(arrDate[2], arrDate[0]-1, arrDate[1]);
     var useDate2 = new Date(arrDate2[2], arrDate2[0]-1, arrDate2[1]);
     
      var validformat=/^\d{2}\/\d{2}\/\d{4}$/ //Basic check for format validity
    	  if (businessUnit == 0) {
    	         alert('Please select Business Unit');
    	         return false;
    	     } 
        if (eventStartDate!=''){
        if (!validformat.test(eventStartDate)){
        alert("Invalid Date Format. Please correct and submit again.");
        return false;
        }
        }
         if (eventEndDate!=''){
        if (!validformat.test(eventEndDate)){
        alert("Invalid Date Format. Please correct and submit again.");
        return false;
        }
        }
        if(evalDuration.length>0){
         if(!(evalDuration.match(onlynumber)))  
      {  
       alert("Please enter only whole numbers for Evaluation Duration field");   
        return false;  
         }}
         /*Sanjeev: removed condition: Evaluation per day in 1-10 range while adding event*/
         if(numberOfEval.length>0){
            /*  if(numberOfEval<1 || numberOfEval>10){
                 alert("Please enter Evaluations per day value in the range 1-10 ");
                 return false;
                 }  */
          if(!(numberOfEval.match(onlynumber)))  
      {  
       alert("Please enter only whole numbers for Evaluations per day field");   
        return false;  
         }}
         
         if(numberOfLearners.length>0){
         if(!(numberOfLearners.match(onlynumber)))  
      {  
       alert("Please enter only whole numbers for Expected Number of Learners field");   
        return false;  
         }}
        
     if(eventName=='')
     {
      alert("Please enter event Name");
            return false;
     }
     
   
        if (eventStartDate!=''){
           if (useDate < today)
           {
            alert("The Start Date cannot be a past date. Please provide a future Date");
            return false;
            }
        }
        else
        {
        alert("The Start Date is a mandatory field. Please provide a future Date");
            return false;
        }
        
       
       if (eventEndDate!=''){
       
      if (useDate2 < useDate){
            alert("The End Date cannot be a date earlier to Start date. Please provide another Date");
            return false;
            }
        
       }
       else
        {
        alert("The End Date is a mandatory field. Please provide a future Date");
            return false;
        }
     
     window.document.forms[0].submit();                       
    }
    
   
</script>

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

			<s:a action="admin">
				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoadmin.gif"
					alt="Back to main admin" width="119" height="18" />
			</s:a>

		</div>
		<!-- end #eval_sub_nav -->

		<H3>Admin:Event Maintenance</H3>

		<div id="main_content">
			<div style="float: right">



				<s:a style="width:120px;  font-size:0.9em; border=0;"
					action="gotoEventManagement">Back to EventsList</s:a>

			</div>


			<s:form action="gotoAddEventDisplay" id="AddEventForm">

				<div>
					<%-- <font color="red">
					<s:label name="message" id="message"></s:label> </font>
					 --%>
					<font color="red"> 
						<%if(message!=null){ %>
							<label name="msg" id="msg" > </label><%=message %>
						<%} %>
 					</font>
				</div>

				<table cellpadding="0" cellspacing="0" style="border: 0">
				<tr>
						 <td width="15%" valign="top" style="border: 0"><label>Business Unit<font color="Red">*</font></label></td> 
						 <td style="border: 0"><select id="businessUnit" name="addEventForm.businessUnit">
							<option value="0">---Select---</option>

							<%
								if (buList != null) {
									for (int i = 0; i < buList.length; i++) {
							%>

							<option value="<%=buList[i]%>"><%=buList[i]%></option>
							<%
								}
								}
							%>
						</select>
					</td>
					</tr>
					<tr>
						<td width="15%" valign="top" style="border: 0">Event name<font
							id="expDtMandatory" color="Red">*</font></td>
						<td style="border: 0"><s:textfield name="addEventForm.eventName"
								id="eventName"></s:textfield></td>
					</tr>
					<tr>
						<td width="15%" valign="top" style="border: 0">Description</td>
						<td style="border: 0"><s:textfield name="addEventForm.eventDescription"
								id="eventDescription"></s:textfield></td>

					</tr>

					<tr>

						<td style="border: 0"><label>Start Date<font
								id="expDtMandatory" color="Red">*</font></label></td>
						<td style="border: 0"><INPUT type="text" size="11"
							maxlength="10" value="<%=eventStartDate%>" name="addEventForm.eventStartDate"
							id="eventStartDate"
							onKeyUp="DateFormat('eventStartDate',event,false,'1','EventStart Date')">

								<IMG id="calImg_startDate"
								src="<%=request.getContextPath()%>/evaluation/resources/images/icon_calendar.gif"
								border="0" name="as_of_date_UP"> <script
										type="text/javascript">
                                Calendar.setup({
                                    inputField     :    "eventStartDate",
                                    ifFormat       :    "%m/%d/%Y",
                                    button         :    "calImg_startDate",
                                    align          :    "Tl"                            
                                });
                            </script></td>
					</tr>
					<tr>

						<td style="border: 0"><label>End Date<font
								id="expDtMandatory" color="Red">*</font></label></td>
						<td style="border: 0"><INPUT type="text" size="11"
							maxlength="10" value="<%=eventEndDate%>" name="addEventForm.eventEndDate"
							id="eventEndDate"
							onKeyUp="DateFormat('eventEndDate',event,false,'1','EventEnd Date')">

								<IMG id="calImg_endDate"
								src="<%=request.getContextPath()%>/evaluation/resources/images/icon_calendar.gif"
								border="0" name="as_of_date_UP"> <script
										type="text/javascript">
                                Calendar.setup({
                                    inputField     :    "eventEndDate",
                                    ifFormat       :    "%m/%d/%Y",
                                    button         :    "calImg_endDate",
                                    align          :    "Tl"                            
                                });
                            </script></td>
					</tr>



					<tr>
						<td width="15%" valign="top" style="border: 0">Evaluation
							Duration(min)</td>
						<td style="border: 0"><s:textfield name="addEventForm.evalDuration"
								id="evalDuration"></s:textfield>
					</tr>
					<tr>

						<td width="15%" valign="top" style="border: 0">Evaluations
							per day</td>

						<td style="border: 0"><s:textfield name="addEventForm.numberOfEval"
								id="numberOfEval"></s:textfield>
					</tr>

					<tr>



						<td width="15%" valign="top" style="border: 0">Evaluation
							Type</td>
						<td style="border: 0"><s:select list="%{typeOfEvals}"
								name="addEventForm.typeOfEval" id="typeOfEval"></s:select>
					</tr>
					<tr>
						<td width="15%" valign="top" style="border: 0">Expected
							number of Learners</td>
						<td style="border: 0"><s:textfield name="addEventForm.numberOfLearners"
								id="numberOfLearners"></s:textfield>
					</tr>
				</table>

				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save.gif"
					alt="Add" width="27" height="19" onclick="validateUserForm()" />
		</div>
		<!-- end #wrap -->

		</s:form>
	</div>
</body>

</html>