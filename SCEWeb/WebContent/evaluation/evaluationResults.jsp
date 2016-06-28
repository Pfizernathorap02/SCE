<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.beans.Attendee"%>
<%@ page import="com.pfizer.sce.beans.Event"%>
<%@ page import="com.pfizer.sce.beans.SCE"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="com.pfizer.sce.db.SCEControlImpl"%>
<%@ page import="com.pfizer.sce.db.SCEManagerImpl"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<%@include file="IAM_User_Auth.jsp" %>
<script language="javascript">
    function backToSearchResults() {
        window.document.forms[0].action="search2";                
        window.document.forms[0].submit();        
    }
    
    function view(sceId) {
    	//alert('sceId :'+sceId);
        window.document.getElementById("selSceId").value = sceId;
        window.document.forms[0].action="viewEvaluation";                
        window.document.forms[0].submit();        
    }
    
    function viewHistory(sceId,product, eventID,emplId) {
        
    	window.document.getElementById("selEmplId").value = emplId;
    	window.document.getElementById("selEventId").value = eventID;
        window.document.getElementById("selSceId").value = sceId;
        window.document.getElementById("selProduct").value = product;
        window.document.forms[0].action="viewEvaluationHistory";
        //var url=window.document.getElementById(getNetuiTagName("SearchForAttendeeForm")).action 
        //url=url+'?product'+'='+product+'&eventID'+'='+eventID+'&emplId'+'='+emplId+'&sceId'+'='+sceId
        //alert(url);
       // window.open(url,'history_window','status=yes,scrollbars=yes,height=380,width=500,resizable=yes,menubar=no')               
        window.document.forms[0].submit();        
    }
    
    function enterNewEvaluation(sceId,templateVersionId, eventId, productCode, product) {
    	
    	//alert('in SCE Id');
    	
        if(templateVersionId==null || templateVersionId=='0' ){
        alert("This evaluation is no longer active and we are currently making the adjustments");
        return false;
        }    
        window.document.getElementById("selSceId").value = sceId;
        window.document.getElementById("selEventId").value = eventId;
        window.document.getElementById("selProductCode").value = productCode;
        window.document.getElementById("selProduct").value = product;
        window.document.forms[0].action="enterNewEvaluation";                
        window.document.forms[0].submit();        
    }
    
<%-- already commented !!    function enterEvaluation(productCode, product, course, trainingDate, classroom, table) {
        window.document.getElementById(getNetuiTagName("selProductCode")).value = productCode;
        window.document.getElementById(getNetuiTagName("selProduct")).value = product;
        window.document.getElementById(getNetuiTagName("selCourse")).value = course;
        window.document.getElementById(getNetuiTagName("selTrainingDate")).value = trainingDate;
        window.document.getElementById(getNetuiTagName("selClassroom")).value = classroom;
        window.document.getElementById(getNetuiTagName("selTable")).value = table;
        window.document.getElementById(getNetuiTagName("SearchForAttendeeForm")).action="<%=PageflowTagUtils.getRewrittenFormAction("enterNewEvaluation", pageContext)%>";                
        window.document.forms[0].submit();        
    } --%>
    
    function enterEvaluation(eventId,templateVersionId, productCode, product, course, classroom, table) {
        if(templateVersionId==null){
        alert("This evaluation is no longer active and we are currently making the adjustments");
        return false;
        }
        
        //alert('in enter evaluation withour sceID');
        window.document.getElementById("selEventId").value = eventId;        
        //alert('selEventID : '+window.document.getElementById("selEventId").value);
        
        
        window.document.getElementById("selProductCode").value = productCode;
        //alert('selProductCode : '+window.document.getElementById("selProductCode").value);
        
        
        window.document.getElementById("selProduct").value = product;
        window.document.getElementById("selCourse").value = course;
        window.document.getElementById("selClassroom").value = classroom;
        window.document.getElementById("selTable").value = table;
        
        window.document.forms[0].action="enterNewEvaluation";                
        window.document.forms[0].submit();        
    }
    
    function giveAutoCreditNew(sceId, eventId, productCode, product,templateVersionId,emplId) {
        if(templateVersionId==null || templateVersionId=='0' ){
            alert("This evaluation is no longer active and we are currently making the adjustments");
            return false;
        }
            if (confirm('Are you sure you want to give auto credit to this Representative for product ' + product + '?')) {
            	window.document.getElementById("selEmplId").value = emplId;
            	window.document.getElementById("selSceId").value = sceId;        
                window.document.getElementById("selEventId").value = eventId;
                window.document.getElementById("selProductCode").value = productCode;
                window.document.getElementById("selProduct").value = product;
                window.document.forms[0].action="giveAutoCredit";                
                window.document.forms[0].submit();        
            }
    }
    
    function giveAutoCredit(eventId, productCode, product, course, classroom, table,templateVersionId,emplId) {
        if(templateVersionId==null || templateVersionId=='0' ){
            alert("This evaluation is no longer active and we are currently making the adjustments");
            return false;
        }
            if (confirm('Are you sure you want to give auto credit to this Representative for product ' + product + '?')) {
                
            	window.document.getElementById("selEmplId").value = emplId;
            	window.document.getElementById("selEventId").value = eventId;
                window.document.getElementById("selProductCode").value = productCode;
                window.document.getElementById("selProduct").value = product;
                window.document.getElementById("selCourse").value = course;
                window.document.getElementById("selClassroom").value = classroom;
                window.document.getElementById("selTable").value = table;
                
                window.document.forms[0].action="giveAutoCredit";                
                window.document.forms[0].submit();        
            }
    }
    
    function deleteSCE(sceId, emplId, eventId, eventName, productCode, product) {
        if (confirm('Are you sure you want to delete SCE for this Representative : ' + eventName + ' - ' + product + '?')) {
        
        	window.document.getElementById("selSceId").value = sceId;        
        	window.document.getElementById("selEmplId").value = emplId;
        	window.document.getElementById("selEventId").value = eventId;
        
        	window.document.forms[0].action="deleteSCE";                
        	window.document.forms[0].submit();                    
        }        
    }
    
    function openPrintWindow(sceId) {
        window.open('evaluation/evaluateAttendee_print.jsp?sceId='+sceId ,'print_window','status=yes,scrollbars=yes,height=600,width=800,resizable=yes,menubar=no');                    
    }
    
    function openUploadWindow(product,templateVersionId,emplid,eventid,productcode){
    if(templateVersionId==null || templateVersionId=='0' ){
    alert("This evaluation is no longer active and we are currently making the adjustments");
    return false;
    }
    //alert(emplid); 
    //alert(product);
    //alert(templateVersionId);
      window.open('evaluation/UploadEvaluationForm.jsp?product='+product+'&templateVersionId'+'='+templateVersionId+'&emplid'+'='+emplid+'&eventid'+'='+eventid+'&productcode'+'='+productcode,'upload_window','status=yes,scrollbars=no,height=475,width=550,resizable=no,menubar=no');
      //window.document.getElementById(getNetuiTagName("selProduct")).value = product;
    }
    function load(){ 
    //alert("hello");      
    var history="<%=request.getAttribute("history")%>";
    var product="<%=request.getAttribute("selProduct")%>";
    var eventID="<%=request.getAttribute("eventID")%>";
    var emplId="<%=request.getAttribute("emplId")%>";
    var checkLength="<%=request.getAttribute("checkLength")%>"; 
    //var sceId="<%=request.getAttribute("sceId")%>";
    //alert(history);
    if(history!= null){
    if(  history == 'history'){  
    //modified on 10 dec by rupinder starts
    
    //alert(checkLength)
    if(checkLength) {
   // alert(checkLength)
       
    window.open('evaluation/history.jsp?product='+product+'&eventID'+'='+eventID+'&emplId'+'='+emplId,'history_window','status=yes,scrollbars=no,height=400,width=550,resizable=yes,menubar=no'); 
   }else{
    window.open('evaluation/history.jsp?product='+product+'&eventID'+'='+eventID+'&emplId'+'='+emplId,'history_window','status=yes,scrollbars=no,height=379,width=500,resizable=yes,menubar=no'); 
   }
//ends
     return false;
    }
    
    }
    }
</script>
<%
    // System.out.println("Before Event");
	/*This code needs modification*/
	
	SCEManagerImpl sceImpl = new SCEManagerImpl();
    Event[] events = sceImpl.getAllEvents();
    // System.out.println("After Event");
    HashMap eventMap = new HashMap();
    if (events != null) {
        for (int i=0; i<events.length; i++) {
            eventMap.put(events[i].getId(), events[i]);
        }
    }    
    SCE[] sces = (SCE[])request.getAttribute("sces");
    session.setAttribute("sces",sces);
    Attendee attendee = null;
    DateFormat dateTimeFormatter = new SimpleDateFormat(SCEUtils.DEFAULT_DATETIME_FORMAT);
    DateFormat dateFormatter = new SimpleDateFormat(SCEUtils.DEFAULT_DATE_FORMAT);    
    Event curEvent = null;        
    DecimalFormat scoreFormatter = new DecimalFormat("###0.##");
    boolean phasedTraining = false; 
    // System.out.println("Session ID4 ***"+session.getId());
%>
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
        <!--[if IE 6]>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" />
        <![endif]-->
    </head>
    <body id="p_representative_list" class="search" onload="load()">
        <div id="wrap">
        
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
                
                <%@include file="navbar.jsp" %>
            <!-- end #top_head -->
            </div>
        <%request.setAttribute("history","mystry");
     // System.out.println("history attribute removed");
     %>
            <h3>Representative Evaluation List</h3>
        
            <div id="main_content">
            <s:form action="viewEvaluation" id="SearchForAttendeeForm">
            <input type="hidden" name="pageName" id="pageName" value="evaluationResults"/>

                <!-- Preserve Search -->
<!--      gadalp           <netui:hidden dataSource="{actionForm.lastName}"/>
                <netui:hidden dataSource="{actionForm.firstName}"/>
                <netui:hidden dataSource="{actionForm.emplId}"/>
                <netui:hidden dataSource="{actionForm.salesPositionId}"/>
                <netui:hidden dataSource="{actionForm.isPassport}"/>
                <netui:hidden dataSource="{actionForm.eventId}"/> gadalp -->
                
<%--            gadalp     <s:hidden name="firstName"/>
                <s:hidden name="emplId"/>
                <s:hidden name="salesPositionId"/>
                <s:hidden name="isPassport"/>
                <s:hidden name="eventId"/> --%>
                
                
                
<!--      gadalp           <netui:hidden dataSource="{actionForm.selEmplId}" tagId="selEmplId"/>
                <netui:hidden dataSource="{actionForm.selEventId}" tagId="selEventId"/>
                <netui:hidden dataSource="{actionForm.selProduct}" tagId="selProduct"/>
                <netui:hidden dataSource="{actionForm.selProductCode}" tagId="selProductCode"/>
                <netui:hidden dataSource="{actionForm.selCourse}" tagId="selCourse"/>  gadalp -->
                
                
                <s:hidden name="selEmplId" id="selEmplId" />
                <s:hidden name="selEventId" id="selEventId"/>
                <s:hidden name="selProduct" id="selProduct"/>
                <s:hidden name="selProductCode" id="selProductCode"/>
                <s:hidden name="selCourse" id="selCourse"/>
                
                <s:hidden name="emplId"/>
                <s:hidden name="eventId"/>
                
                <%--<netui:hidden dataSource="{actionForm.selTrainingDate}" tagId="selTrainingDate"/>--%>
                
<!--     gadalp  <netui:hidden dataSource="{actionForm.selClassroom}" tagId="selClassroom"/>
                <netui:hidden dataSource="{actionForm.selTable}" tagId="selTable"/>
                <netui:hidden dataSource="{actionForm.selSceId}" tagId="selSceId"/>
                
                <netui-data:getData resultId="selEmplId" value="{actionForm.selEmplId}" />
                <netui-data:getData resultId="eventId" value="{actionForm.eventId}" /> gadalp-->
                
                
                <s:hidden name="selClassroom" id="selClassroom"/>
                <s:hidden name="selTable" id="selTable"/>
                <s:hidden name="selSceId" id="selSceId"/>
            
<!--                 <netui-data:callControl resultId="attendee" controlId="SCEManager" method="getAttendeeByEmplId">
                    <netui-data:methodParameter value="{pageContext.selEmplId}"></netui-data:methodParameter> 
                </netui-data:callControl> -->
                <%
                    // gadalp SCEControl sceCtl = (SCEControl)pageContext.getAttribute("SCEControl");               
                	/*Created instance for accessing Control layer*/
                	
                	SCEControlImpl sceCtl = new SCEControlImpl();
                   // attendee = (Attendee)pageContext.getAttribute("attendee"); 
                		
                	//attendee = sceCtl.getAttendeeByEmplId(request.getParameter("emplId"));
                	
                	attendee = sceCtl.getAttendeeByEmplId((String)pageContext.findAttribute("emplId"));
                     
                    String eventName = "";
                    Integer eventId = (Integer)pageContext.findAttribute("eventId");
                    eventId = -1;
                    if (eventId != null) {
                        eventName = eventId.toString().equals(SCEConstants.ALL_EVENT_ID) ? "All" : ((Event)eventMap.get(eventId)).getName();
                    }
                %>
                <font color="red"><%=SCEUtils.ifNull(request.getAttribute("msg"),"")%></font>
                <font color="red"><%=SCEUtils.ifNull(session.getAttribute("msg1"),"")%></font>
                <%session.removeAttribute("msg1");%>
                <h4><%=eventName%> - Evaluation Results for <%=attendee.getName()%></h4>                
                <div class="top_table_buttonsR" >
                    <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtosearch.gif" alt="Back To Search Results" width="125" height="19" onclick="return backToSearchResults()"/>                    
                </div>
                <table cellspacing="0" >
                    <tr>
                        
                        <th>Evaluation </th>
                        <th>Evaluation Date (EST)</th>
                        <th>Score</th>
                        <th>Uploaded Date</th>                       
                        <th></th>
                        <th></th>                                              
                        <%if (isTrainingTeam || isGuestTrainer) {%>
                        <th class="last"></th>
                        <%} else {%>
                        <th></th>
                        <th></th>                        
                        <th class="last"></th>
                        <%}%>
                    </tr>
                    
                    <%
                     // System.out.println("Before SCE");
                    SCE objSCE = null;
                    String nextProduct = "";
                    boolean showEnterEvaluation = false;
                    boolean isSubmitted = false;                    
                    if (sces != null) {
                        for (int i=0; i<sces.length; i++) {                            
                            objSCE = sces[i];  
                             // System.out.println("Employee in SCE "+objSCE.getEmplId());                         
                            isSubmitted = (objSCE.getId() != null && SCEConstants.ST_SUBMITTED.equalsIgnoreCase(objSCE.getStatus()));
                            curEvent = (Event)eventMap.get(objSCE.getEventId()); 
                            phasedTraining = (objSCE.getEventId() != null && objSCE.getEventId().intValue() >= 5 && objSCE.getEventId().intValue() <= 7);                                                     
                            if (i == sces.length-1) {
                                showEnterEvaluation = true;
                            }
                            else {
                                showEnterEvaluation = !objSCE.getEventId().equals(sces[i+1].getEventId()) || !objSCE.getProductCode().equals(sces[i+1].getProductCode());
                            }
                            
                             // System.out.println("isSubmitted : "+isSubmitted);
                             // System.out.println("showEnterEvaluation : "+showEnterEvaluation);
                             
                            if( ((isSubmitted || showEnterEvaluation) &&!isGuestTrainer )||((isGuestTrainer && !isSubmitted )|| ((SCEConstants.DONOTSENDTOLMS).equalsIgnoreCase(objSCE.getLmsFlag().trim()) && isGuestTrainer ))) {
                                
                    %>
                    <%if(objSCE.getProduct().equals(session.getAttribute("product"))){%>
                    <tr bgcolor="#A1C5E6">
                    <%}
                    else{%>
                    <tr>
                    <%}
                     
                    %>
                    
                        
                        <td><%=objSCE.getProduct()%></td>
                        <td><%=isSubmitted ? SCEUtils.ifNull(objSCE.getSubmittedDate(), dateTimeFormatter) : "&nbsp;"%></td>
                        <%if (phasedTraining) {%>
                        <td><%=SCEUtils.ifNull(objSCE.getOverallScore(), scoreFormatter, "&nbsp;")%></td>
                        <%} else {%>
                        <td><%=SCEUtils.ifNull(objSCE.getOverallRating(), "&nbsp;")%></td>
                        <%}%>
                        <%if (isSubmitted && objSCE.getUploadedDate()!= null) {%>
                        <td><%=SCEUtils.ifNull(objSCE.getUploadedDate(), dateTimeFormatter)%></td>
                         <%}else{%>
                         <td class="button">&nbsp;</td>
                         <%}%>
                         
                        <!--rupinder added on 2nd january-->
                         <% 
                             if(isGuestTrainer){ 
                                if (isSubmitted && !phasedTraining) {
                                       // System.out.println("objSCE.getLmsFlag()::"+objSCE.getLmsFlag());
                                       if((SCEConstants.DONOTSENDTOLMS).equalsIgnoreCase(objSCE.getLmsFlag().trim()))
                                         {
                                          String check="yes";
                                          session.setAttribute("GT_NOT_LMS",check);
                      
                        %>
                                            <td class="button" style="margin-top: 120px;"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_view.gif" alt="View" width="38" height="19" onclick="return viewHistory(<%=objSCE.getId()%>,'<%=objSCE.getProduct()%>','<%=objSCE.getEventId()%>','<%=objSCE.getEmplId()%>')"/></td>
                        
                                        <%}
                                                                      }else{%>
                                                                          <td class="button">&nbsp;</td>
                                                                         <%}
                                                }else{%>                    
                                                    <% if (isSubmitted && !phasedTraining) {%>
                                                          <td class="button" style="margin-top: 120px;"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_view.gif" alt="View" width="38" height="19" onclick="return viewHistory(<%=objSCE.getId()%>,'<%=objSCE.getProduct()%>','<%=objSCE.getEventId()%>','<%=objSCE.getEmplId()%>')"/></td>
                        
                                                                                         <%}else{%>
                                                                                              <td class="button">&nbsp;</td>
                        
                                                                                              <%}
                                                     }%>
                        <% if (isAdmin){%>
                            
                        <td class="button" style=" margin-top: 120px;">
                            <%
                            if (showEnterEvaluation && !phasedTraining) 
                            {
                                if (isSubmitted) 
                                {
                                    // System.out.println("LMSflag" +objSCE.getLmsFlag().trim());
                                    if ((SCEConstants.DONOTSENDTOLMS).equalsIgnoreCase(objSCE.getLmsFlag().trim()) ) 
                                    {
                                        // System.out.println("objSCE.getEmplId()"+objSCE.getEmplId());
                                        Integer tempverID=sceCtl.fetchTemplateVersionId(objSCE.getProduct()); 
                                       // System.out.println("tempverID"+tempverID);
                            %>
                            <img src="<%=request.getContextPath()%>/evaluation/resources/_img/Upload.gif" alt="Upload" width="47" height="19" onclick="openUploadWindow('<%=objSCE.getProduct()%>',<%=tempverID%>,'<%=objSCE.getEmplId()%>',<%=objSCE.getEventId()%>,'<%=objSCE.getProductCode()%>')"/>
                            <%
                                    }
                                    else {
                            %>
                            &nbsp;
                            <%
                            }
                                }
                                else {
                                    // System.out.println("objSCE.getEmplId()"+objSCE.getEmplId());
                                    Integer tempverID=sceCtl.fetchTemplateVersionId(objSCE.getProduct()); 
                                      // System.out.println("tempverID"+tempverID);

                            %>
                            <img src="<%=request.getContextPath()%>/evaluation/resources/_img/Upload.gif" alt="Upload" width="47" height="19" onclick="openUploadWindow('<%=objSCE.getProduct()%>',<%=tempverID%>,'<%=objSCE.getEmplId()%>',<%=objSCE.getEventId()%>,'<%=objSCE.getProductCode()%>')"/>
                            <%
                                }
                            }
                            else {
                            %>                            
                            &nbsp;
                            <%
                            }
                            %>                        
                        </td>
                        <%} else {%>
                        <td class="button">&nbsp;</td>
                        
                        <%}%>
                        
                       
                      
                       <td class="<%=isAdmin ? "button" : "last button"%>" style=" margin-top: 120px;">
                            
                            <%
                            if (showEnterEvaluation && !phasedTraining) {
                                if (isSubmitted) {
                                   if ((SCEConstants.DONOTSENDTOLMS).equalsIgnoreCase(objSCE.getLmsFlag().trim()) ) {
                                	   Integer tempverID=sceCtl.fetchTemplateVersionId(objSCE.getProduct()); 
                                      // System.out.println("tempverID"+tempverID);
                            %>
                            <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_re_evaluate.gif" alt="Enter New Evaluation" width="72" height="19" onclick="return enterNewEvaluation(<%=objSCE.getId()%>,<%=tempverID%>,<%=objSCE.getEventId()%>,'<%=objSCE.getProductCode()%>','<%=objSCE.getProduct()%>')"/>
                            <%
                                    }
                                    else {
                            %>
                            &nbsp;
                            <%
                            }
                                }
                                else {
                            %>
                            <%--<img src="<%=request.getContextPath()%>/evaluation/resources/_img/buttons_enter_eval.gif" alt="Enter Evaluation" width="130" height="20" onclick="return enterEvaluation('<%=objSCE.getProductCode()%>','<%=objSCE.getProduct()%>','<%=objSCE.getCourse()%>','<%=SCEUtils.ifNull(objSCE.getTrainingDate(),dateFormatter)%>','<%=objSCE.getClassroom()%>','<%=objSCE.getTableName()%>')"/>--%>
                            <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_enter_neweval.gif" alt="Enter Evaluation" width="55" height="19" onclick="return enterEvaluation(<%=objSCE.getEventId()%>,<%=objSCE.getTemplateVersionId()%>,'<%=objSCE.getProductCode()%>','<%=objSCE.getProduct()%>','<%=SCEUtils.ifNull(objSCE.getCourse(),"")%>','<%=SCEUtils.ifNull(objSCE.getClassroom(),"")%>','<%=SCEUtils.ifNull(objSCE.getTableName(),"")%>')"/>
                            <%
                                }
                            }
                            else {
                            %>                            
                            &nbsp;
                            <%
                            }
                            %>
                        </td>
                        <%
                        if (isAdmin) {%>
                        <td class="button" style=" margin-top: 120px;">
                            <%
                            if (showEnterEvaluation && !phasedTraining) {
                                if (isSubmitted) {
                                    if ((SCEConstants.DONOTSENDTOLMS).equalsIgnoreCase(objSCE.getLmsFlag().trim()) ) {
                                    	Integer tempverID=sceCtl.fetchTemplateVersionId(objSCE.getProduct());
                            %>
                            <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_autocredit.gif" alt="Auto Credit" width="70" height="19" onclick="return giveAutoCreditNew(<%=objSCE.getId()%>,<%=objSCE.getEventId()%>,'<%=objSCE.getProductCode()%>','<%=objSCE.getProduct()%>','<%=tempverID%>','<%=objSCE.getEmplId()%>')"/>
                            <%
                                    }
                                    else {
                            %>
                            &nbsp;
                            <%
                            }
                                }
                                else {
                                	Integer tempverID=sceCtl.fetchTemplateVersionId(objSCE.getProduct());
                            %>
                            <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_autocredit.gif" alt="Auto Credit" width="75" height="19" onclick="return giveAutoCredit(<%=objSCE.getEventId()%>,'<%=objSCE.getProductCode()%>','<%=objSCE.getProduct()%>','<%=SCEUtils.ifNull(objSCE.getCourse(),"")%>','<%=SCEUtils.ifNull(objSCE.getClassroom(),"")%>','<%=SCEUtils.ifNull(objSCE.getTableName(),"")%>','<%=tempverID%>','<%=objSCE.getEmplId()%>')"/>
                            <%
                                }
                            }
                            else {
                            %>                            
                            &nbsp;
                            <%
                            }
                            %>                        
                        </td>                                              
                        <td class="last button" style=" margin-top: 120px;">
                        <%if (isSubmitted) {%>
                        <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_delete.gif" alt="Delete" width="45" height="19" onclick="return deleteSCE(<%=objSCE.getId()%>,'<%=objSCE.getEmplId()%>',<%=objSCE.getEventId()%>,'<%=curEvent != null ? curEvent.getName() : ""%>','<%=objSCE.getProductCode()%>','<%=objSCE.getProduct()%>')"/>
                        <%} else {%>
                        &nbsp;
                        <%}%>
                        </td>
                        <%}%>
                    </tr>                    
                    <%
                    
                            }
                        }
                    } 
                    session.removeAttribute("product");
                    %>
                    <tr>
                        <!--<td></td>
                        <td></td>
                        <td>POP2</td>
                        <td>POP1</td>
                        <td>POP</td>-->
                        <%if (isTrainingTeam || isGuestTrainer) {%>
                         <!--<td class="last">happy</td>-->
                        <%} else {%>
                        <!--<td>HAppo</td>
                        <!--<td>popa</td>
                        <!--<td>popa1</td>
                        <!--<td>popa2</td>
                        <!--<td class="last"></td>-->
                        <%}%>
                    </tr>
                    <tr>
                        <!--<td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>-->
                        <%if (isTrainingTeam || isGuestTrainer) {%>                        
                        <%} else {%>
                        <!--<td></td>
                        <td></td>
                        <td class="last"></td>-->
                        <%}%>
                    </tr>
                </table>
                <div class="clear"></div>


			</s:form>
            </div> <!-- end #content -->
            
        </div><!-- end #wrap -->
        
        </body>

</html>

<%
    request.removeAttribute("msg");
	
%>