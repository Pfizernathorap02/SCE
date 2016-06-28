<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@include file="IAM_User_Auth.jsp"%>

<%
    pageContext.setAttribute("searchButton",request.getContextPath()+"/evaluation/resources/_img/button_search.gif");  
%>
<script language="javascript">
    function validateSearch() {
/*         var lastName = document.getElementById('lastName').value;
        var firstName = document.getElementById('firstName').value;
        var emplId = document.getElementById('emplId').value;
        var territoryId = document.getElementById('salesPositionId').value;
        if (lastName == '' && firstName == '' && emplId == '' && salesPositionId == '') {        
            alert('Please specify atleast one of the entries.');
            return false;
        } */    
        
        return true;               
    }
</script>
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
	href="<%=request.getContextPath()%>/evaluation/resources/_css/admin.css"
	type=text/css rel=stylesheet>
	<LINK media=all
		href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css"
		type=text/css rel=stylesheet>

		<!--[if IE 6]><LINK href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" type=text/css rel=stylesheet><![endif]-->

		<style type="text/css">
@import url(/SCEWeb/resources/jscalendar-1.0/calendar-win2k-cold-2.css);
</style>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/lang/calendar-en.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar-setup.js"></script>
		<script language="JavaScript"	src="<%=request.getContextPath()%>/evaluation/resources/js/DateValidator.js"></script>
</head>
<body id="p_search" class="search">
	<div id="wrap">
		<DIV id=top_head>
			<H1>Pfizer</H1>
			<H2>Sales Call Evaluation</H2>
			                <%@include file="navbar.jsp" %>
			<%-- <jsp:include page="navbar.jsp" /> --%>
			<!-- end #top_head -->
		</DIV>
        <%
      //User userAdmin = (User)session.getAttribute("user");
  //  boolean isAdmin = user != null && "SCE_Administrators".equalsIgnoreCase(user.getUserGroup()) || "SCE_OpsManager".equalsIgnoreCase(user.getUserGroup());
  String userGrp = user.getUserGroup();
  String opsMgrAccess = "N";
  if(userGrp == null)
  {
  }
  else if(userGrp.equalsIgnoreCase("SCE_OpsManager")) 
  {
    opsMgrAccess = "Y";
  } 


%>
		<H3>Search for Attendee</H3>

		<DIV id=main_content>

			<s:form action="searchAtd" id="searchForAttendeeForm">

				<input type="hidden" name="pageName" id="pageName"
					value="searchForAttendeeJSPForm" />
				<FIELDSET>
					<div>
						<label>Event:</label>
						 <s:select list="%{map}"  name="eventId" /> 
					</div>

					<div>
						<label>Last Name:</label>
						<s:textfield name="lastName" 
							cssStyle="width:200" id="lastName"></s:textfield>

					</div>

					<div>
						<label>First Name:</label>
						<s:textfield name="firstName" 
							cssStyle="width:200" id="lastName"></s:textfield>
					</div>

					<div>
						<p>
							<em>or</em>
						</p>
					</div>

					<div>
						<label>Emplid:</label>
						<s:textfield name="emplId" 
							cssStyle="width:200" id="emplId"></s:textfield>
					</div>

					<div>
						<p>
							<em>or</em>
						</p>
					</div>

					<div>
						<label>Sales Position ID:</label>
						<s:textfield name="salesPositionId"
							cssStyle="width:200"
							id="salesPositionId"></s:textfield>
					</div>



					<div>
						<label></label> <input class="radio_btn" type="radio"
							name="isPassport" value="Y" />Search passport attendees only
					</div>

					<div>
						<label></label> <input class="radio_btn" type="radio"
							name="isPassport" value="N" checked />Search all attendees
					</div>

                        <%if(opsMgrAccess.equalsIgnoreCase("Y")){%>
                        <div> <br> </div>
                        <%}else{%>
					<div class="add_buttons">
						<s:submit type="image"
							src="/SCEWeb/evaluation/resources/_img/button_search.gif"
							cssStyle="width:56px; height:19px"
							onclick="return validateSearch()" />



					</div>
					<%} %>
				</FIELDSET>
			</s:form>


			<DIV class=clear></DIV>
		</DIV>
		<!-- end #content -->
	</DIV>
	<!-- end #wrap -->
</BODY>
</html>


