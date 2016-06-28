<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.beans.Attendee"%>
<%@ page import="com.pfizer.sce.beans.Event"%>
<%@ page import="com.pfizer.sce.beans.LegalConsentTemplate"%>
<%@ page import="com.pfizer.sce.beans.SCE"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@include file="IAM_User_Auth.jsp" %>
<script language="javascript">
function view(sce) {
        <%-- window.document.getElementById(getNetuiTagName("selSceId")).value = sceId;
        window.document.getElementById(getNetuiTagName("SearchForAttendeeForm")).action="<%=PageflowTagUtils.getRewrittenFormAction("viewEvaluationFromHistory", pageContext)%>"; --%>                
        
       // var url=window.document.getElementById(getNetuiTagName("SearchForAttendeeForm")).action
       // url=url+'?sceId'+'='+sceId 
        //alert(url);
     //window.document.forms[0].sce.value = "sce"; 
     document.forms[0].action = 'sceTRHistoryView?sce'+'='+sce 
            //alert(document.forms[0].action);
            document.forms[0].submit();
    //window.document.forms[0].submit(sce);
        //window.open(url,'view_window','status=yes,scrollbars=yes,height=380,width=500,resizable=yes,menubar=no')         
    }
    
function viewPDF(sceId){
alert("PDF");
window.document.getElementById("selSceId").value = sceId;
        window.document.getElementById("SearchForAttendeeForm").action="viewPDFEvaluationFromHistory";                
        
        var url=window.document.getElementById("SearchForAttendeeForm").action
        url=url+'?sceId'+'='+sceId 
        //alert(url);
        
        window.document.forms[0].submit();
}
    
</script>
<%-- <netui-data:declareControl type="SCEDBControls.SCEManager" controlId="SCEManager"></netui-data:declareControl>
<netui-data:callControl resultId="events" controlId="SCEManager" method="getAllEvents"/> --%>
<%
    Event[] events = (Event[])request.getAttribute("events");
    HashMap eventMap = new HashMap();
    if (events != null) {
        for (int i=0; i<events.length; i++) {
            eventMap.put(events[i].getId(), events[i]);
        }
    }  
              
    SCE[] sces = (SCE[])session.getAttribute("scesTR");    
    Attendee attendee = null;
    DateFormat dateTimeFormatter = new SimpleDateFormat(SCEUtils.DEFAULT_DATETIME_FORMAT);
    DateFormat dateFormatter = new SimpleDateFormat(SCEUtils.DEFAULT_DATE_FORMAT);    
    Event curEvent = null;        
    DecimalFormat scoreFormatter = new DecimalFormat("###0.##");
    boolean phasedTraining = false; 
    
      String content=(String)request.getAttribute("content");
      //LegalConsentTemplate objLC= (LegalConsentTemplate)request.getAttribute("forLcid"); 
      String linkName = (String)request.getAttribute("linkName");
       String loginUserNtId = (String)request.getAttribute("loginUserNtId");
       // System.out.println("Login user in trtHIstory.jsp is : "+loginUserNtId);
       String ntId = (String)request.getAttribute("ntId");
       String emplId = (String)request.getAttribute("emplId");
       String productCode = (String)request.getAttribute("productCode");
       //String streventid=(String)request.getAttribute("eventId");
      Integer eventId = (Integer)request.getAttribute("eventId");
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
                <H2>Training Reporting Tool</H2>            
               
            </DIV>
        
            <H3>Evaluation History</H3>
            <s:form action="sceTRHistoryView"  name="SearchForAttendeeForm" id="SearchForAttendeeForm">            
             <input type="hidden" name="loginuserntid" id="loginuserntid" value="<%=loginUserNtId%>"/>
             <input type="hidden" name="linkName" id="linkName" value="<%=linkName%>"/> 
             <input type="hidden" name="ntId" id="ntId" value="<%=ntId%>"/> 
             <input type="hidden" name="emplId" id="emplId" value="<%=emplId%>"/> 
             <input type="hidden" name="productCode" id="productCode" value="<%=productCode%>"/>
             <input type="hidden" name="eventId" id="eventId" value="<%=eventId%>"/>
                
    <br>
    <p><B><%=sces[0].getProduct()%></B></p>
    <%if(5<sces.length){%>
                <div STYLE=" height: 168px; width: 450px; font-size: 12px; overflow-x:auto; overflow-y: scroll;" align="left">
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
        <input type="hidden" name="sceId" id="sceId" value="<%=objSCE.getId()%>"/>
           <%}
               }
              %>             
          </table>
          </DIV>
          <img src="<%=request.getContextPath()%>/evaluation/resources/_img/btn_close.gif" alt="Close" width="38" height="19" onclick="window.close()"/>
         <!-- <input type="button" value="close" onclick="window.close()">-->
    </DIV> 
    </s:form>         
    </body>
</html>
<a href="/SCEWeb/evaluation/history.jsp">/SCEWeb/evaluation/history.jsp</a>