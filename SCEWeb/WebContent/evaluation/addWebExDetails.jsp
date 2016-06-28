


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="com.pfizer.sce.utils.SCEUtils"%>
<%@page import="com.pfizer.sce.beans.WebExDetails"%>
<%@page import="com.pfizer.sce.beans.EventsCreated"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@include file="IAM_User_Auth.jsp"%>

<html>
<script language="javascript">
 function getList(temp)
 {
 
 var eventName=temp.value;
 document.forms[0].action = 'goToWebEx?eventName'+'='+eventName 
  window.document.forms[0].submit();  
 
 }
    function validateUserForm1() {
    var confCallNumber=document.getElementById('confCallNumber').value;
     var chairPersonPasscode=document.getElementById('chairPersonPasscode').value;
      var participantPasscode=document.getElementById('participantPasscode').value;
       var meetingLink=document.getElementById('meetingLink').value;
   		var Uploadfile=document.getElementById('Uploadfile').value;
      
      var numbers = /^\d{10}$/; 
       var onlynumber=/^[0-9]+$/;
      var letters = /http:\/\/[A-Za-z0-9\.-]{3,}\.[A-Za-z]{3}/;  
      
     if(confCallNumber==''||confCallNumber.length<10)
     {
      alert("Please enter 10 digit Conference Call Number");
            return false;
     }
     
     if(!(confCallNumber.match(numbers)))  
      {  
       alert("Please input numeric value of length 10 in the conference call number field");   
        return false;  
         }
        
         
      if(chairPersonPasscode=='')
     {
      alert("Please enter Chairperson Code");
            return false;
     }
     
     if(!(chairPersonPasscode.match(onlynumber)))  
      {  
       alert("Please input numeric value in the chairperson passcode field");   
        return false;  
         }
         if(chairPersonPasscode.length>10)
         {
         alert("The chairperson code cannot be greater than 10 numbers");   
        return false;
         }
         
         
       if(participantPasscode=='')
     {
      alert("Please enter Participant Code");
            return false;
     }
     else
    {
      if(!(participantPasscode.match(onlynumber)))  
      {  
       alert("Please input numeric value in the participant passcode field");   
        return false;  
         }
         if(participantPasscode.length>10)
         {
         alert("The participant code cannot be greater than 10 numbers");   
        return false;
         }
         }
     
     
       if(meetingLink=='')
     {
      alert("Please enter Meeting Link");
            return false;
     }
     if(!(meetingLink.indexOf(".com")>1) )
     {
     
      alert("Input appropriate link");   
        return false; 
     }
     
     window.document.forms[0].submit();                       
    } 
    
    
       function validateUserForm2() {

    	   var file=document.getElementById('Uploadfile');    	 
      var len=file.value.length;    
      var ext=file.value;  
       if(len<=0){
      alert("Please choose a file");
      return false;
   
     }
     if(ext.substr(len-3,len)!="xls" && ext.substr(len-4,len)!="xlsx"){
         alert("Please select file with extension .xls or .xlsx");
         return false;
         }
    
     window.document.forms[0].submit();                       
    } 
</script>
<%
 WebExDetails[] webex= (WebExDetails[])request.getAttribute("webex");
EventsCreated eventsList[] = (EventsCreated [])request.getAttribute("eventsList");
 
String message=(String)request.getAttribute("message");

   HashMap events = new HashMap();
  for(int i=0;i<eventsList.length;i++)
  {
   EventsCreated events2=eventsList[i];
  String name=events2.getEventName();
   
   events.put(name,name);
  }
  if(!(eventsList.length>0)){
   events.put("select","--Select--");
  } 
 

%>
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


		<h3>Add the Conference details</h3>
		 <div  style="float:right"></div>
		<div style="color: red">
		<%if(message!=null){ %>
		<p><%=message %></p>
		<%} %>
		</div>
			<s:form action="goToAddWebEx" tagId="webExDetails" enctype="multipart/form-data">
			<%-- <font color="red"><s:label value="<%=message%>"/></font> --%>
			
			<table cellpadding="0" cellspacing="0"
				style="border: 0; colour: 'white';">
				<%-- <%if(eventsList.length>0){%> --%>
				<%if(eventsList.length>0){%>
				<tr style="width: auto">
					<td style="border: 0;width: 150px">Event Name<font style="color:#ff0000;">*</font></td>
					<td style="border: 0"><s:select list="%{events}"
							onChange="getList(this);" id="event_Name" name="webExFormData.eventName">
						</s:select></td>
				</tr>
				<tr>

					<td style="border: 0">Conference Call Number</td>
					<td style="border: 0"><s:textfield
							name="webExFormData.confCallNumber" id="confCallNumber" /></td>
				</tr>
				<tr>
					<td style="border: 0">Chairperson Passcode</td>
					<td style="border: 0"><s:textfield
							name="webExFormData.chairPersonPasscode"
							id="chairPersonPasscode" /></td>
				</tr>
				<tr>

					<td style="border: 0">Participant Passcode</td>
					<td style="border: 0"><s:textfield
							name="webExFormData.participantPasscode"
							id="participantPasscode" /></td>
				</tr>
				<tr>
					<td style="border: 0">Meeting Link</td>
					<td style="border: 0"><s:textfield
							name="webExFormData.meetingLink" id="meetingLink" size="30" /></td>
				</tr>
				<%}else{%>
				<tr style="width: auto">
					<td style="border: 0;width: 150px">Event Name<font style="color:#ff0000;">*</font></td>
					<td style="border: 0"><s:select list="%{events}"
							disabled="true" onChange="getList(this);" id="event_Name" name="webExFormData.eventName">
						</s:select></td>
				</tr>
				<tr>

					<td style="border: 0">Conference Call Number</td>
					<td style="border: 0"><s:textfield
							name="webExFormData.confCallNumber" disabled="true"
							id="confCallNumber" /></td>
				</tr>
				<tr>
					<td style="border: 0">Chairperson Passcode</td>
					<td style="border: 0"><s:textfield
							name="webExFormData.chairPersonPasscode" disabled="true"
							id="chairPersonPasscode" /></td>
				</tr>
				<tr>

					<td style="border: 0">Participant Passcode</td>
					<td style="border: 0"><s:textfield
							name="webExFormData.participantPasscode" disabled="true"
							id="participantPasscode" /></td>
				</tr>
				<tr>
					<td style="border: 0">Meeting Link</td>
					<td style="border: 0"><s:textfield
							name="webExFormData.meetingLink" disabled="true"
							id="meetingLink" size="30" /></td>
				</tr>
				<%}%>
			</table>
			<%if(eventsList.length>0){%>
			<img
				src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save.gif"
				alt="Save" width="27" height="19" onclick="validateUserForm1()" />
			<%}else{%>
			<img
				src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save_disabled.gif"
				alt="Save" width="27" height="19" />
			<%}%>
			<h3>
                   BULK UPLOAD
            </h3>
               
                 <div>                     			
                            <label>Upload File:<font id="uploadFileMandatory" style="color:#ff0000;">*</font></label>                      
                            <s:file accept="application/vnd.ms-excel" id="Uploadfile" size="10000" style="width:300px;" name="webExFormData.webexFile"/>
                            
                               
                        </div>
				<div>
					<s:url action="goToDownloadWebExTemplate" var="downloadWebExTemplate"></s:url>
					<s:a href="%{downloadWebExTemplate}">Download the template</s:a>
				</div>
				<%
					if (eventsList.length > 0) {
				%>
                <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save.gif" alt="Save" width="27" height="19" onclick="validateUserForm2()"/>
                <%}else{%>
                <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save_disabled.gif" alt="Save" width="27" height="19" />
                <%}%>
             
             
              <table cellspacing="0">
                    <tr>
                        <th>Event Name</th>
                        <th>Conference Call Number</th>
                        <th>Chair Person Passcode</th>
                        <th>Participant Passcode</th>
                        <th>Meeting Link</th>
                    
                        
                    </tr>
                    <%
                    WebExDetails webex1 = null;
                    if (webex != null) {
                        for (int i=0; i<webex.length; i++) {   
                            webex1 = webex[i];                                                      
                    %>
                    <tr>
                        <td><%=SCEUtils.ifNull(webex1.getEventName(),"&nbsp;")%></td>
                         <td><%=SCEUtils.ifNull(webex1.getConfCallNumber(),"&nbsp;")%></td>
                        <td><%=SCEUtils.ifNull(webex1.getChairPersonPasscode(),"&nbsp;")%></td>
                        <td><%=SCEUtils.ifNull(webex1.getParticipantPasscode(),"&nbsp;")%></td>
                        <td><%=SCEUtils.ifNull(webex1.getMeetingLink(),"&nbsp;")%></td>
                        
                       
                    </tr>
                    <%
                        }
                    }
                    %>
                
                </table>
			</s:form>
		
		<!-- end #content -->

	</div>
	<!-- end #wrap -->
</body>

</html>
