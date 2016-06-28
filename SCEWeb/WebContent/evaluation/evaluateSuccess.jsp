<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.beans.Attendee"%>
<%@ page import="com.pfizer.sce.beans.Event"%>
<%@ page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp" %>

<%
	SCEManagerImpl sceManager = new SCEManagerImpl();
	Attendee evalAttendee = null;
    String from = (String)request.getAttribute("from");    
    String isAutoCredit = (String)request.getAttribute("isAutoCredit");
%>

<script language="javascript">
    function backToSCEForm() {
        window.document.forms[0].action="backToSCEForm";                
        window.document.forms[0].submit();        
    }
    
    function backToEvaluationResults() {
        window.document.forms[0].action="backToEvaluationResults";                
        window.document.forms[0].submit();        
    }
    
    function backToScePendingReport() {
        window.document.forms[0].action="backToScePendingReport";                
        window.document.forms[0].submit();        
    }
    
    function backToSelectAttendee() {
        window.document.forms[0].action="backToSelectAttendee";                
        window.document.forms[0].submit();         
    }
    
    function selectAnotherAttendee() {
        var sel_attendee = document.getElementById('attendee_ddl');
        var attendeeVal = sel_attendee.options[sel_attendee.selectedIndex].value;
        if (attendeeVal != '') {
            window.document.forms[0].action="selectAnotherAttendee";                
            window.document.forms[0].submit();
        }
        else {
            return false;
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
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css" rel="stylesheet" type="text/css" media="all" />
        <!--[if IE 6]><LINK href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" type=text/css rel=stylesheet><![endif]-->        
    </head>
    <body id="p_evaluation_form">
    	<div id="wrap">
            <s:form action="backToEvaluationResults" id="evaluateForm">
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
                
                <%@include file="navbar.jsp" %>                
                 <!-- end #top_head -->
            </div>            
                         
            <% 
                Event event = sceManager.getEventById((Integer)pageContext.findAttribute("evalEventId"));
                String eventName = event.getName();                    
            %>              
            <h3><%=eventName%> Sales Call Evaluation</h3>
            
            <div id="main_content">
                <p><br><br></p>
<!--                <netui-data:getData resultId="evalEmplId" value="{actionForm.evalEmplId}" />
                	<netui-data:callControl resultId="evalAttendee" controlId="SCEManager" method="getAttendeeByEmplId">
                    <netui-data:methodParameter value="{pageContext.evalEmplId}"></netui-data:methodParameter> 
                	</netui-data:callControl>    -->             
                <%
                    
                 evalAttendee = sceManager.getAttendeeByEmplId((String)pageContext.findAttribute("evalEmplId"));
                	                    
                %>              
                <p align="center">
                
                    <table>
                        <tr>
                            <td>
                                <%if (!"Yes".equalsIgnoreCase(isAutoCredit)) {%>
                                Your evaluation for <b><%=evalAttendee.getName()%></b> has been submitted successfully.</p>
                                <%} else {%>
                                Auto Credit for <b><%=evalAttendee.getName()%></b> has been submitted successfully.</p>
                                <%}%>
                            </td>
                        </tr>
                
                    <%
                    if ("evaluate".equalsIgnoreCase(from)) {
                        Attendee[] attendees = (Attendee[])request.getAttribute("attendees");  
                    %>
                    <%--<netui:anchor action="backToEventInfo"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoinfo.gif" alt="Back to Event Info" width="119" height="18"></netui:anchor>--%>
                    <!-- Preserve Eval Search -->
<%--                <netui:hidden dataSource="{actionForm.evalEmplId}" />
                    <netui:hidden dataSource="{actionForm.evalEventId}" />
                    <netui:hidden dataSource="{actionForm.evalTrainingDate}" />
                    <netui:hidden dataSource="{actionForm.evalProduct}" />
                    <netui:hidden dataSource="{actionForm.evalProductCode}" />
                    <netui:hidden dataSource="{actionForm.evalCourse}" />
                    <netui:hidden dataSource="{actionForm.evalClassroom}" />
                    <netui:hidden dataSource="{actionForm.evalTable}" /> --%>
                    
                    <s:hidden name="evalEmplId" />
                    <s:hidden name="evalEventId" />
                    <s:hidden name="evalTrainingDate" />
                    <s:hidden name="evalProduct" />
                    <s:hidden name="evalProductCode" />
                    <s:hidden name="evalCourse" />
                    <s:hidden name="evalClassroom" />
                    <s:hidden name="evalTable" />
                    
                    
                        <%if (attendees != null && attendees.length != 0) {%>   
                        <tr>
                            <td>
                                <ul class="bullets">
                                    <li>
                                        <b>Select another Representative for Evaluation.</b>
                                        <br><br>
                                        <%--<netui:label value="{actionForm.evalCourse}"/> > Classroom (<netui:label value="{actionForm.evalClassroom}"/>) > Table (<netui:label value="{actionForm.evalTable}"/>)--%>
                                        <s:label value="evalCourse"/>
                                        <br>
                                        <select id="attendee_ddl" name="emplId" onchange="return selectAnotherAttendee()">
                                            <option label="Select Another Attendee" value="" selected="selected">Select Another Attendee</option>
                                            <%
                                            for (int i=0; i<attendees.length; i++) {
                                            %>
                                            <OPTION label="<%=attendees[i].getNameComma()%>" value="<%=attendees[i].getEmplId()%>"><%=attendees[i].getNameComma()%></OPTION>
                                            <%
                                            }
                                            %>                    
                                        </select>
                                    </li>
                                </ul>
                            </td>
                        </tr>
                        <tr><td><br></td></tr>  
                        <%}%>                      
                        <tr>
                            <td>
                                <ul class="bullets">
                                    <li>
                                        <b>Go Back To Select Attendee.</b>&nbsp;&nbsp;&nbsp;<img align="middle" src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoselectattendee.gif" alt="Back to Select Attendee" width="148" height="19" onclick="return backToSelectAttendee()">
                                    </li>
                                </ul>
                            </td>
                        </tr>
                    
                    <%} else if ("search".equalsIgnoreCase(from)) {%>
                    <!-- Preserve Search -->
<%--                     <netui:hidden dataSource="{actionForm.evalEventId}" />
                    <netui:hidden dataSource="{actionForm.lastNameSrch}" />
                    <netui:hidden dataSource="{actionForm.firstNameSrch}" />
                    <netui:hidden dataSource="{actionForm.emplIdSrch}" />
                    <netui:hidden dataSource="{actionForm.eventIdSrch}" />
                    <netui:hidden dataSource="{actionForm.territoryIdSrch}" />
                    <netui:hidden dataSource="{actionForm.isPassportSrch}" />
                    <netui:hidden dataSource="{actionForm.selEmplIdSrch}" /> --%>
                    
                    <s:hidden name="evalEmplId" />
                    <s:hidden name="evalEventId" />
                    <s:hidden name="lastNameSrch" />
                    <s:hidden name="firstNameSrch" />
                    <s:hidden name="emplIdSrch" />
                    <s:hidden name="eventIdSrch" />
                    <s:hidden name="territoryIdSrch" />
                    <s:hidden name="isPassportSrch" />
                    <s:hidden name="selEmplIdSrch" /> 
                        <tr>
                            <td>
                                <img src="<%=request.getContextPath()%>/evaluation/resources/_img/buttons_backtoeval.gif" alt="Back To Evaluation Results" width="159" height="20" onclick="return backToEvaluationResults()"/>                    
                            </td>
                        </tr>
                    
                    <%} else {%>
<%--                 <netui:hidden dataSource="{actionForm.evalEventId}" />
                    <netui:hidden dataSource="{actionForm.evalCourse}" />
                    <netui:hidden dataSource="{actionForm.evalCourseId}" />
                    <netui:hidden dataSource="{actionForm.filterClassroom}" /> --%>
                    
                    <s:hidden name="evalEmplId" />
                    <s:hidden name="evalEventId" />
                    <s:hidden name="evalCourse" />
                    <s:hidden name="evalCourseId" />
                    <s:hidden name="filterClassroom" />
                    
                    
                        <tr>
                            <td>
                                <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_back.gif" alt="Back To SCE Report" width="53" height="17" onclick="return backToScePendingReport()"/>                    
                            </td>
                        </tr>
                    <%}%>
                    </table> 
                </p>               
                
            </div> <!-- end #content -->
            </s:form>
            
        </div><!-- end #wrap -->
        
    </body>
</html>
<%
    request.removeAttribute("from");
    request.removeAttribute("attendees");
    request.removeAttribute("isAutoCredit");
%>