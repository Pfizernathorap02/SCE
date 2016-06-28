<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.beans.Attendee"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<%@include file="IAM_User_Auth.jsp" %>

<%
    Attendee[] attendees = (Attendee[])request.getAttribute("attendees");
    String lOrder = (request.getAttribute("lOrder") != null) ? (String)request.getAttribute("lOrder") : "asc";
    String fOrder = (request.getAttribute("fOrder") != null) ? (String)request.getAttribute("fOrder") : "asc";    
    SCEUtils sce= new SCEUtils();
%>
    
<script language="javascript">
    /*function selectAttendee(obj) {
        if (obj == undefined  || obj.id == undefined)
            return false;
        
        document.getElementById(getNetuiTagName("selEmplId")).value = obj.id;
        window.document.forms[0].submit();
    }*/
    


	function selectAttendee(emplId) {	
		//document.forms[0].fEmplid.value=emplId;
		
		document.getElementById("fEmplid").value=emplId;		
		//alert(document.forms[0].fEmplid.value);
		window.document.forms[0].submit();
	}

	function searchAgain() {
		window.document.forms[0].action = "searchAgain";
		window.document.forms[0].submit();
	}

	function changeOrder(field) {
		document.getElementById(field).value = '<%="asc".equals(lOrder)?"desc":"asc"%>';
        window.document.forms[0].action = "searchAtd";                 
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
        <!--[if IE 6]><LINK href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" type=text/css rel=stylesheet><![endif]-->        
        <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/sorttable.js"></script>        
    </head>
    
    <body id="p_search_results" class="search">
        <div id="wrap">
        
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
                <%@include file="navbar.jsp" %>
                <!-- end #top_head -->
            </div>
        
            <h3>Search Results</h3>
        
            <div id="main_content">
                <s:form action="evaluationResults" name="earchForAttendeeForm" id="earchForAttendeeForm">
<%--gadalp                 <netui-data:getData resultId="eventId" value="{actionForm.eventId}" />  gadalp--%> 
                 <input type="hidden" name="pageName" id="pageName" value="searchForAttendeeResults"/>              
                <!-- Preserve Search -->
               
                <s:hidden name="eventId"/>
                <s:hidden name="lastName"/>
                <s:hidden name="firstName"/>
                <s:hidden name="emplId"/>
                <s:hidden name="salesPositionId"/>
                <s:hidden name="isPassport"/>          
                <input type="hidden" name="lOrder" id = "lOrder" value="<%=lOrder%>"></input>
                <input type="hidden" name="fOrder" id="fOrder" value="<%=fOrder%>"> </input> 
                
                
       <%--gadalp          <netui:hidden dataSource="{actionForm.selEmplId}" tagId="selEmplId"/> gadalp --%>               
                <div class="top_table_buttonsL1">
                    <LABEL>Event:&nbsp;&nbsp;&nbsp;<s:property value="%{map[eventId]}"></s:property></LABEL>                   
                </div>
                <div class="top_table_buttonsR">
                    <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_searchagain.gif" alt="Search Again" width="95" height="18" onclick="return searchAgain()"/>
                    <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_print.gif" alt="Print" width="64" height="18" onclick="window.print();"/>
                </div>
                <table cellspacing="0" class="sortable" id="unique_id">
                    <tr>
                        <th>Emplid</th>
                        <th class="sort_up" onclick="ts_resortTable(this, 1);return false;">Last Name</th>
                        <th class="sort_up" onclick="ts_resortTable(this, 2);return false;">First Name</th>
                        <th>Business Unit</th>
                        <th>Sales Organization</th>
                        <th>Role</th>                                                    
                        <th>Sales Position ID</th>
                    </tr>
                    <%
                    if (attendees != null) {
                        for (int i=0; i<attendees.length; i++) {                                
                    %>
                    <tr onclick="" id="<%=attendees[i].getEmplId()%>">
                        <%-- <td><a  href="#" onclick="return selectAttendee('<%=attendees[i].getEmplId()%>')"><%=attendees[i].getEmplId()%></a></td> --%> 
                        <td><s:url action="evaluationResults" var="urlTag" ><s:param name="emplId"><%=attendees[i].getEmplId()%></s:param><s:param name ="eventId" ><s:property value="eventId"></s:property></s:param></s:url><s:a href="%{urlTag}"><%=attendees[i].getEmplId()%></s:a></td>                       
                        <td><%=attendees[i].getLastName()%></td>
                        <td><%=attendees[i].getFirstName()%></td>
                        <td><%=attendees[i].getBu()%></td>
                        <td><%=attendees[i].getSalesOrgDesc()%></td>                        
                        <td><%=attendees[i].getRoleCd()%></td>
                        <td class="last"><%=attendees[i].getSalesPositionId()%></td>                        
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
    request.removeAttribute("attendees");
    request.removeAttribute("lOrder");
    request.removeAttribute("fOrder");
%>