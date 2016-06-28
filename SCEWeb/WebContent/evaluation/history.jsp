<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.beans.Attendee"%>
<%@ page import="com.pfizer.sce.beans.Event"%>
<%@ page import="com.pfizer.sce.beans.SCE"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.pfizer.sce.db.SCEControlImpl"%>
<%@ page import="com.pfizer.sce.db.SCEManagerImpl"%>


<%@ taglib prefix="s" uri="/struts-tags"%>


<%@include file="IAM_User_Auth.jsp" %>
<script language="javascript">
function view(sceId) {
//alert("NORMAL");
        window.document.getElementById("selSceId").value = sceId;
        window.document.forms[0].action="viewEvaluationFromHistory";                
        
        var url=window.document.forms[0].action
        //url=url+'?sceId'+'='+sceId 
        url=url+'?sceId'+'='+sceId+'&closeButton=active'
        //alert(url);
        //window.document.forms[0].submit();
         window.open(url,'view_window','status=yes,scrollbars=yes,height=1000,width=1000,screenX=0,left=0,screenY=0,top=0,resizable=yes,menubar=no')     
    }
function viewPDF(sceId){
//alert("PDF");
window.document.getElementById("selSceId").value = sceId;
        window.document.forms[0].action="viewPDFEvaluationFromHistory";                
        
        var url=window.document.forms[0].action
        url=url+'?sceId'+'='+sceId 
        //alert(url);
        window.document.forms[0].submit();
}
    
</script>
<%-- <netui-data:declareControl type="SCEDBControls.SCEManager" controlId="SCEManager"></netui-data:declareControl>
<netui-data:callControl resultId="events" controlId="SCEManager" method="getAllEvents"/> --%>
<%
	SCEManagerImpl sceManager = new SCEManagerImpl();
	Event[] events = sceManager.getAllEvents();
    HashMap eventMap = new HashMap();
    if (events != null) {
        for (int i=0; i<events.length; i++) {
            eventMap.put(events[i].getId(), events[i]);
        }
    }  
              
    SCE[] sces = (SCE[])session.getAttribute("sces1");    
    Attendee attendee = null;
    DateFormat dateTimeFormatter = new SimpleDateFormat(SCEUtils.DEFAULT_DATETIME_FORMAT);
    DateFormat dateFormatter = new SimpleDateFormat(SCEUtils.DEFAULT_DATE_FORMAT);    
    Event curEvent = null;        
    DecimalFormat scoreFormatter = new DecimalFormat("###0.##");
    boolean phasedTraining = false; 
%>
<html>
    <head>
        <title>Evaluation History</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="Content-Language" content="en-us" />        
        <meta name="ROBOTS" content="ALL" />
        <meta http-equiv="imagetoolbar" content="no" />
        <meta name="MSSmartTagsPreventParsing" content="true" />
        <meta name="Keywords" content="_KEYWORDS_" />
        <meta name="Description" content="_DESCRIPTION_" />
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css" rel="stylesheet" type="text/css" media="all" />
        <!--[if IE 6]><LINK href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" type=text/css rel=stylesheet><![endif]--> 
    </head>
    <body >
     <div id="wrap">
     <DIV id=top_head>
                <H1>Pfizer</H1>
                <H2>Sales Call Evaluation</H2>            
               
            </DIV>
        
            <H3>Evaluation History</H3>
            <s:form action="viewEvaluation" id="SearchForAttendeeForm">
            <!-- <netui:hidden dataSource="{actionForm.selSceId}" tagId="selSceId"/> -->
            <s:hidden name="selSceId" id="selSceId"></s:hidden>
    <p><B><%=sces[0].getProduct()%></B></p>
    
     <%if(sces.length>5){%>
                <div STYLE=" height: 170px; width: 450px; font-size: 12px; overflow-x:auto; overflow-y: auto;" align="left">
                <%}else{%>
                <div STYLE=" overflow-x: auto; overflow-y: auto;" align="left">
                <%}%>
        <table cellspacing="0" border="1">
                    <tr>
                    <th>Date and Time (EST)</th>
                        <th>Score</th>
                        <th>Action</th>                        
        </tr>
              <%
                    SCE objSCE = null;
                    String nextProduct = "";
                    
                    boolean isSubmitted = false; 
                    if (sces != null) {
                        for (int i=0; i<sces.length; i++) {
                            objSCE = sces[i];
                            isSubmitted = (objSCE.getId() != null && SCEConstants.ST_SUBMITTED.equalsIgnoreCase(objSCE.getStatus()));
                            curEvent = (Event)eventMap.get(objSCE.getEventId());
                            phasedTraining = (objSCE.getEventId() != null && objSCE.getEventId().intValue() >= 5 && objSCE.getEventId().intValue() <= 7);                                                     
                           
              %>
        <tr>
           <td><%=isSubmitted ? SCEUtils.ifNull(objSCE.getSubmittedDate(), dateTimeFormatter) : "&nbsp;"%></td>
           <%if (phasedTraining) {%>
           <td><%=SCEUtils.ifNull(objSCE.getOverallScore(), scoreFormatter, "&nbsp;")%></td>
           <%} else {%>
           <td><%=SCEUtils.ifNull(objSCE.getOverallRating(), "&nbsp;")%></td>
           <%}%>
           <%if(objSCE.getUploadedDate()!=null){%>
           <td class="button"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_view.gif" alt="View" width="38" height="19" onclick="return viewPDF(<%=objSCE.getId()%>)"/></td>
           <%}else{%>
           <td class="button"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_view.gif" alt="View" width="38" height="19" onclick="return view(<%=objSCE.getId()%>)"/></td>
           <%}%>
        </tr>
           <%}
               }
              %>             
          </table>
          </DIV>
        
          
      
          <img src="<%=request.getContextPath()%>/evaluation/resources/_img/btn_close.gif" alt="Close" width="38" height="19" onclick="window.close()"/>
         </Div>
    
    </s:form>         
    </body>
</html>