<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@ page import="com.pfizer.sce.beans.Attendee"%>
<%@ page import="com.pfizer.sce.beans.SCEReport"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>


<%@ taglib prefix="s" uri="/struts-tags"%>

<%@include file="IAM_User_Auth.jsp" %>

<netui-data:declareControl type="SCEDBControls.SCEManager" controlId="SCEManager"></netui-data:declareControl>
<netui-data:callControl resultId="eventMap" controlId="SCEManager" method="getEventMap"/>
<%
    SCEReport[] sceReports = (SCEReport[])request.getAttribute("sceReports");
    SCEManager sceManager = (SCEManager)pageContext.getAttribute("SCEManager");
    HashMap eventMap = (HashMap)pageContext.getAttribute("eventMap");
%>
    
<script language="javascript">
    function backToSceReport() {        
        window.document.forms[0].action="gotoSCEReport";                
        window.document.forms[0].submit();        
    }  
    
    function enterEvaluation(eventId, emplId, productCode, product, classroom, table) {
        
    	window.document.getElementById(getNetuiTagName("selEventId")).value = eventId;
        window.document.getElementById(getNetuiTagName("selEmplId")).value = emplId;
        window.document.getElementById(getNetuiTagName("selProductCode")).value = productCode;
        window.document.getElementById(getNetuiTagName("selProduct")).value = product;
        window.document.getElementById(getNetuiTagName("selClassroom")).value = classroom;
        window.document.getElementById(getNetuiTagName("selTable")).value = table;
        
        
        window.document.forms[0].action="enterNewEvaluationReport";                
        window.document.forms[0].submit();        
    }  
    
    function openExcelWindow(sceId) {
        window.open('scePendingReportExcel.jsp','print_window','status=yes,scrollbars=yes,height=600,width=800,resizable=yes,menubar=no');                    
    }
    
    function filterByClassroom() {
        window.document.forms[0].action="gotoPendingReport";                
        window.document.forms[0].submit();        
    }
</script>
<html>
    <head>
        <title>Pfizer Sales Call Evaluation</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="Content-Language" content="en-us" />        
        <meta name="ROBOTS" content="ALL" />
        <meta http-equiv="imagetoolbar" content="no" />
        <meta name="MSSmartTagsPreventParsing" content="true" />
        <meta name="Keywords" content="_KEYWORDS_" />
        <meta name="Description" content="_DESCRIPTION_" />
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css" rel="stylesheet" type="text/css" media="all" />
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/admin.css" rel="stylesheet" type="text/css" media="all" />
        <!--[if IE 6]>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" />
        <![endif]-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/sorttable.js"></script>        
    </head>
    
    <body id="p_sce_report" class="admin">
        <div id="wrap">
        
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
                <%@include file="navbar.jsp" %>
                <!-- end #top_head -->
            </div>
        
            <h3>Pending Attendees</h3>
        
            <div id="main_content">
                <s:form action="enterNewEvaluationReport" >
                
                <s:hidden name="evalEmplId" Id="evalEmplId"/>                                     
                <s:hidden name="selEventId" id="selEventId"></s:hidden>
                <s:hidden name="selProduct" id="selProduct"></s:hidden>     
                <s:hidden name="selProductCode" id="selProductCode"></s:hidden>
                <s:hidden name="selCourse" id="selCourse"></s:hidden>          
               	<s:hidden name="selCourseId" id="selCourseId"></s:hidden>
               	<s:hidden name="selClassroom" id="selClassroom" ></s:hidden>
                <s:hidden name="selTable" id="selTable"></s:hidden>
                
                
                <netui-data:getData resultId="selCourse" value="{actionForm.selCourse}" />                
                <netui-data:getData resultId="selCourseId" value="{actionForm.selCourseId}" />
                <netui-data:getData resultId="selEventId" value="{actionForm.selEventId}" />                
                
                
                <h4>Pending Attendees for <%=SCEUtils.ifNull(pageContext.findAttribute("selCourse"),"")%> (<%=sceReports != null ? sceReports.length : 0%>)</h4>
                <h4>Event:&nbsp;&nbsp #{map[pageContext.findAttribute("selEventId")]}<%-- <%=eventMap.get(pageContext.findAttribute("selEventId"))%> --%></h4>
                <%
                    //String[] classrooms = sceManager.getClassroomsByCourse((String)pageContext.getAttribute("selCourse"));
                    String[] classrooms = sceManager.getClassroomsByCourseId((Integer)pageContext.findAttribute("selEventId"), (Integer)pageContext.findAttribute("selCourseId"));
                    
                    LinkedHashMap classroomsHash = new LinkedHashMap();
                    classroomsHash.put("0","All");
                    if (classrooms != null) {
                        for (int i=0; i<classrooms.length; i++) {
                            classroomsHash.put(classrooms[i],classrooms[i]);
                        }
                    }
                    classroomsHash.put("-1","None");
                    pageContext.setAttribute("classroomsHash",classroomsHash);
                %>
                
                <div class="top_table_buttonsL1">
                <LABEL>Classroom:</LABEL>                
                <%-- <netui:select dataSource="{actionForm.filterClassroom}" optionsDataSource="{pageContext.classroomsHash}" tagId="filter_classroom" onChange="return filterByClassroom()"/> --%>
                <s:select list="%{classroomsHash}" name="filterClassroom"  id="filterClassroom"  onchange="return filterByClassroom()"></s:select>
                </div>
                
                <div class="top_table_buttonsR">
                    <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_back.gif" alt="Back To SCE Report" width="53" height="17" onclick="return backToSceReport()"/>
                    <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_print.gif" alt="Print" width="64" height="18" onclick="window.print();"/>
                </div>
                
                
                <table cellspacing="0" class="sortable" id="unique_id">
                    <tr>
                        <th>Emplid</th>
                        <th class="sort_up" onclick="ts_resortTable(this, 1);return false;">Last Name</th>
                        <th class="sort_up" onclick="ts_resortTable(this, 2);return false;">First Name</th>
                        <th>Classroom</th>
                        <th>Table</th>
                        <th>BU</th>
                        <th>RBU</th>
                        <th>Future Role</th>                                                    
                        <th>Sales Position</th>
                        <th class="last"></th>
                    </tr>
                    <%
                    if (sceReports != null) {
                        for (int i=0; i<sceReports.length; i++) {                                
                    %>
                    <tr onclick="" id="<%=sceReports[i].getEmplId()%>">
                        <td><%=sceReports[i].getEmplId()%></td>                        
                        <td><%=sceReports[i].getLastName()%></td>
                        <td><%=sceReports[i].getFirstName()%></td>
                        <td><%=SCEUtils.ifNull(sceReports[i].getClassroom(),"&nbsp;")%></td>
                        <td><%=SCEUtils.ifNull(sceReports[i].getTableName(),"&nbsp;")%></td>
                         <td><%=SCEUtils.ifNull(sceReports[i].getBu(),"&nbsp;")%></td>
                         <td><%=SCEUtils.ifNull(sceReports[i].getRbu(),"&nbsp;")%></td>                      
                         <td><%=SCEUtils.ifNull(sceReports[i].getFutureRole(),"&nbsp;")%></td>
                         <td><%=SCEUtils.ifNull(sceReports[i].getSalesPositionId(),"&nbsp;")%></td>
                        <td class="last button">
                            <img src="<%=request.getContextPath()%>/evaluation/resources/_img/buttons_enter_eval.gif" alt="Enter Evaluation" width="130" height="20" onclick="return enterEvaluation(<%=sceReports[i].getEventId()%>,'<%=sceReports[i].getEmplId()%>','<%=sceReports[i].getProductCode()%>','<%=sceReports[i].getProduct()%>','<%=SCEUtils.ifNull(sceReports[i].getClassroom(),"")%>','<%=SCEUtils.ifNull(sceReports[i].getTableName(),"")%>')"/>                            
                        </td>                        
                    </tr>                    
                    <%
                        }
                    }
                    %>
                </table>
                </s:form>
                <div class="clear"></div>	
            </div> <!-- end #content -->
        
        </div><!-- end #wrap -->
    
    </body>

</html>
<%
    request.removeAttribute("sceReports");    
%>