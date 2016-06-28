<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>


<%@ page import="com.pfizer.sce.beans.Attendee"%>
<%@ page import="com.pfizer.sce.beans.Event"%>
<%@ page import="com.pfizer.sce.beans.Role"%>
<%@ page import="com.pfizer.sce.beans.SCE"%>
<%@ page import="com.pfizer.sce.beans.SCEDetail"%>
<%@ page import="com.pfizer.sce.beans.User"%>
<%@ page import="com.pfizer.sce.common.SCEConstants"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<style type="text/css">
@media print {
    .noprint { display: none; }
}

@media screen {
    .print { display: none; }
}
</style>


<!-- <netui-data:declareControl type="SCEDBControls.SCEManager" controlId="SCEManager"></netui-data:declareControl> -->
<%
    SCEManagerImpl sceManager = new SCEManagerImpl();
    boolean createMode = true;  
    // System.out.println("Inside evaluatePhase");
    SCE objSCE = (SCE)request.getAttribute("sceToDisplay");
    
    String evalEmplId = null;
    Attendee evalAttendee = null;
    
    pageContext.setAttribute("submitButton",request.getContextPath()+"/evaluation/resources/_img/button_submit.gif");    
    pageContext.setAttribute("cancelButton",request.getContextPath()+"/evaluation/resources/_img/btn_close.gif");    
    
    String allQ = "";
    
    // Formatters
    DateFormat dateFormatter = new SimpleDateFormat(SCEUtils.DEFAULT_DATE_FORMAT);
    DateFormat dateFormatter1 = new SimpleDateFormat("MMM d, yyyy");
    DecimalFormat weightFormatter = new DecimalFormat("###0.##");
    DecimalFormat scoreFormatter = new DecimalFormat("###0.##");
    /* Modified by Meenakshi for SCE Phase Form changes */
    String score1Header = "SCORE";
    String score2Header = "SCORE";
    String score3Header = "SCORE";
    /* End of modification */
    if (objSCE != null && objSCE.getEventId() != null && objSCE.getEventId().intValue() == 7) {
        // Speciality Markets
        score1Header = "SCORE";
        score2Header = "SCORE";
        score3Header = "SCORE";
    }
%>
<%if (objSCE != null && objSCE.getId() != null) {%>
<object id="factory" viewastext  style="display:none"
  classid="clsid:1663ed61-23eb-11d2-b92f-008048fdd814"
  codebase="<%=request.getContextPath()%>/evaluation/smsx.cab#Version=6,3,434,26">
</object>
<%}%>
<script language="javascript">
    function countLines(strtocount, cols) {
        var hard_lines = 1;
        var last = 0;
        while ( true ) {
            last = strtocount.indexOf("\n", last+1);
            hard_lines ++;
            if ( last == -1 ) break;
        }
        var soft_lines = Math.round(strtocount.length / (cols-1));
        var hard = eval("hard_lines  " + unescape("%3e") + "soft_lines;");
        if ( hard ) soft_lines = hard_lines;
        return soft_lines;
    }
    
    function cleanForm() {    
        var the_form = document.forms[0];    
        for ( var x in the_form ) {        
            if ( ! the_form[x] ) continue;                    
            if( typeof the_form[x].rows != "number" ) continue;    
            //alert(the_form[x].name);
            if (the_form[x].name.indexOf("print_") == 0) {
                the_form[x].rows = countLines(the_form[x].value,the_form[x].cols) + 1;    
            }
        }            
    }

    function zoomScaleBeforePrint() {
    	cleanForm();			
        var originalHeader, originalFooter;
        originalHeader = factory.printing.header;
        originalFooter = factory.printing.footer;			
        factory.printing.header = "";			
        //factory.printing.footer = "&w&bPage &p of &P&b&D";
        factory.printing.footer = "&bPage &p of &P&b&D"	
        factory.printing.portrait = true;  						
        document.focus();
        document.body.style.zoom='1';
        factory.printing.leftMargin = 0.167;
        factory.printing.topMargin = 0.167;
        factory.printing.rightMargin = 0.18;
        factory.printing.bottomMargin = 0.18;
        window.print();
        //reset the header and footer, and zoom back
        factory.printing.header = originalHeader;
        factory.printing.footer = originalFooter;
        document.body.style.zoom='1';        
    } 
    
    function onlyDigits(e) {	
        var _ret = true;
        var reg = /\./g;    
        
        
        // make sure only digit number, negative sign (45) and one dot at most can be entered.		
        if (window.event.keyCode < 45 || window.event.keyCode > 57 || (window.event.keyCode == 46 && reg.test(window.event.srcElement.value))) {
            window.event.keyCode = 0;
            _ret = false;
        }                
        return (_ret);
    }
    

    
    function cleanNumber(num) {
        //get rid of the comma in the number string
        var i, A, B, C, D;
        D = "0";
        if(num != null && num != '') {
            B = num.replace(/\,/g,"");		
            C = B.replace(/\$ /g,"");		
            D = C.replace(/€ /g,"");				
        }
        if (isNaN(D)) {
            return 0;
        } else {
            return eval(D);
        }
     }
     
     function round(number,X) {
        // rounds number to X decimal places, defaults to 2
        X = (!X ? 2 : X);
        return Math.round(number*Math.pow(10,X))/Math.pow(10,X);
     }
     
     
     function validateSave() {        
        //alert('Save');
        return validate('Save');
     }
     
     function validateSubmit() {
        //alert('Submit');
        return validate('Submit');
     }
     
     function validate(action) {
        if (action == 'Submit') {
            document.getElementById('overall_score').value = document.getElementById("total_score").innerHTML;
            document.getElementById('status').value = '<%=SCEConstants.ST_SUBMITTED%>';
        }
        else {
            document.getElementById('status').value = '<%=SCEConstants.ST_DRAFT%>';;
        }
        //alert(1);
        var score1;
        var score2;
        var score3;
        
        var score1_Elem;
        var score2_Elem;
        var score3_Elem;
        
        
        var commentsText;
        <%
        for (int i=0; i<objSCE.getSceDetailList().size(); i++) {
            SCEDetail aDetail = (SCEDetail)objSCE.getSceDetailList().get(i);
            if (SCEConstants.QT_SCORE.equalsIgnoreCase(aDetail.getType())) {
        %>
            //alert(2);
            score1_Elem = document.getElementById("question_score1_" + '<%=aDetail.getQuestionId()%>');
            score2_Elem = document.getElementById("question_score2_" + '<%=aDetail.getQuestionId()%>');
            score3_Elem = document.getElementById("question_score3_" + '<%=aDetail.getQuestionId()%>');
            
            score1 = score1_Elem.value;
            score2 = score2_Elem.value;
            score3 = score3_Elem.value;
            
            //alert(score1);
            if (action == 'Submit') {
                if (score1 == '') {
                    alert('Please provide all scores for the following skill\n' + '<%=aDetail.getDescription().replaceAll("'","\\\\u0027")%>');
                    score1_Elem.focus();
                    return false;
                }
                if (score2 == '') {
                    alert('Please provide all scores for the following skill\n' + '<%=aDetail.getDescription().replaceAll("'","\\\\u0027")%>');
                    score2_Elem.focus();
                    return false;
                }
                if (score3 == '') {
                    alert('Please provide all scores for the following skill\n' + '<%=aDetail.getDescription().replaceAll("'","\\\\u0027")%>');
                    score3_Elem.focus();
                    return false;
                }
            }
            if (!validRange(score1)) {
                alert('Please provide valid scores for the following skill\n' + '<%=aDetail.getDescription().replaceAll("'","\\\\u0027")%>');
                score1_Elem.focus();
                return false;
            }  
            if (!validRange(score2)) {
                alert('Please provide valid scores for the following skill\n' + '<%=aDetail.getDescription().replaceAll("'","\\\\u0027")%>');
                score2_Elem.focus();
                return false;
            } 
            if (!validRange(score3)) {
                alert('Please provide valid scores for the following skill\n' + '<%=aDetail.getDescription().replaceAll("'","\\\\u0027")%>');
                score3_Elem.focus();
                return false;
            }                
            
        <%
            }
            if (SCEConstants.QT_COMMENTS.equalsIgnoreCase(aDetail.getType())) {
        %>
                commentsText = document.getElementById("question_comments_" + '<%=aDetail.getQuestionId()%>');
                if (commentsText.value.length > 4000) {
                    alert('<%=aDetail.getDescription().replaceAll("'","\\\\u0027")%>: Max length allowed 4000 char');
                    commentsText.focus();
                    return false;
                }       
        <%
            }
        }        
        %>
        return true;
     }
     
     function validRange(score) {
        // Score Range Validation 1 <= x <= 5
        if (score != '') {
            //alert(score);
            scoreF = parseFloat(score);
            //alert('scoreF:' + scoreF);
            if (scoreF < 1 || scoreF > 5) {
                //alert('false');
                return false;
            }
        }
        return true;
     }
     
    function showDocument(name) {
      window.open('<%=request.getContextPath()%>/evaluation/resources/pdf/'+name,'newDocWin','height=720,width=616,resizable,scrollbars');
    }
</script>

<%!
   private String formatQuestionNumStr(String str) {    
    if (str == null || str.equals("")) {
        return "";
    }
    else { 
        // Remove Last Comma       
        String tmp = str.substring(0, str.length() - 2);        
        if (tmp.indexOf(", ") > -1) {
            int indx = tmp.lastIndexOf(", ");
            return tmp.substring(0, indx) + " AND " + tmp.substring(indx + 1, tmp.length());
        } else {
            return tmp;
        }
    }    
   } 
%>

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
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/reporting.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/reporting_print.css" rel="stylesheet" type="text/css" media="print" />        
    </head>
    
    <BODY>
        <div id="wrap">
            <s:form action="submitSCETRPhase" tagId="evaluateForm">
            
            <!-- <netui-data:getData resultId="mode" value="{actionForm.mode}" /> -->
            <%
                createMode = (SCEConstants.CREATE_MODE).equalsIgnoreCase((String)pageContext.getAttribute("mode"));                        
            %>
            
            <!-- <netui-data:getData resultId="evalEmplId" value="{actionForm.evalEmplId}" />
            <netui-data:callControl resultId="evalAttendee" controlId="SCEManager" method="getAttendeeByEmplId">
                <netui-data:methodParameter value="{pageContext.evalEmplId}"></netui-data:methodParameter> 
            </netui-data:callControl> -->
            <%
                evalAttendee = (Attendee)pageContext.getAttribute("evalAttendee");                
            %>
            
            <%
                Attendee evaluatorS = sceManager.getAttendeeByEmplId(objSCE.getSubmittedByEmplId());
                String evaluatorNameS = "";                
                if (evaluatorS != null) {
                    evaluatorNameS = evaluatorS.getName();
                }                            
                else {
                    User evalUser = sceManager.getUserByEmplId(objSCE.getSubmittedByEmplId());
                    evaluatorNameS = evalUser != null ? evalUser.getName() : "Anonymous User";
                }  
                
                Attendee evaluatorP = sceManager.getAttendeeByEmplId(objSCE.getPreparedByEmplId());
                String evaluatorNameP = "";                
                if (evaluatorP != null) {
                    evaluatorNameP = evaluatorP.getName();
                }                            
                else {
                    User evalUser = sceManager.getUserByEmplId(objSCE.getPreparedByEmplId());
                    evaluatorNameP = evalUser != null ? evalUser.getName() : "Anonymous User";
                }          
            %>
            <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="headerTD">
                        <div class="noprint">
                        <h1 class="logoh1">Pfizer</h1>
                        <h2 class="logoh2">Training Reports</h2>
                        <div> 
                        <div class="date"><strong>Today is:</strong> <%=dateFormatter1.format(new Date())%></div>
                        <div class="buttons">                                    
                        <%if (!createMode) {%>
                        <img src="<%=request.getContextPath()%>/evaluation/resources/_img/buttons_print.gif" alt="Print" width="64" height="18" onclick="zoomScaleBeforePrint();return false;"/>
                        <%} else {%>  
                        <%if (objSCE != null && SCEConstants.ST_DRAFT.equalsIgnoreCase(objSCE.getStatus())) {%>
                        <img src="<%=request.getContextPath()%>/evaluation/resources/_img/buttons_print.gif" alt="Print" width="64" height="18" onclick="zoomScaleBeforePrint();return false;"/>
                        <%}%>
                        <img src="<%=request.getContextPath()%>/evaluation/resources/_img/b_download3.gif" alt="Download Blank Form" width="80" height="18" onclick="showDocument('<%=objSCE.getFormTitle()%>.pdf');"/>
                        <%}%>
                        </div>  
                        </div>
                        </div>
                        
                        <div class="print">
                        <table id="logo">
                            <tr>
                                <td style="text-align:left" width="40%"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/pfizer_logo.gif" alt="" style="width:88px"/></td>
                                <td style="text-align:left" width="60%"><b><%=SCEUtils.ifNull(objSCE.getFormTitle(),"")%></b></td>                                
                            </tr>
                        </table>
                        </div>
                    </td>
                </tr>
                
        
                <tr>
                    <TD align=left valign="top" bgcolor="#FFFFFF" class="ContentPadding">
                        <!-- put the following div in a wrapper -->
                        <!-- set the wrapper witdh to control width of elements inside-->
                        <div class="page_content_wrapper">
                            <h1><%=SCEUtils.ifNull(objSCE.getFormTitle(),"")%></h1>
                            <div class="clear"><img src="<%=request.getContextPath()%>/evaluation/resources/images/spacer.gif"></div>
                        
                            <%--<table class="datagrid" style="width:850px;">--%>
                            <%--<table class="datagrid" style="width:600px;">--%>
                            <table class="datagrid">
                            <tr>
                                <th colspan="4" align="left">EVALUATION FORM</th>
                            </tr>
                            
                            <tr>
                                <td width="25%" align="right"><strong>REPRESENTATIVE:</strong></td>
                                <td width="25%"><input name="repName" type="text" id="repName" style="width:175px;text-align:center;" value="<%=evalAttendee.getNameComma()%>" readonly></td>
                                <td width="25%" align="right" valign="top"><strong>CLASS DATE:</strong></td>
                                <td width="25%" align="left" valign="top"><input name="classDate" type="text" id="classDate" style="width:175px;text-align:center;" value="<%=SCEUtils.ifNull(createMode ? new java.util.Date() : objSCE.getSubmittedDate(), dateFormatter)%>" readonly></td>
                            </tr>
                            
                            <tr>
                                <td align="right"><strong>BU:</strong></td>
                                <td>
                                    <input name="repCluster" type="text" id="repCluster" style="width:175px;text-align:center;" value="<%=SCEUtils.ifNull(evalAttendee.getBu(),"")%>" readonly>
                                </td>
                                <td align="right" valign="top"><strong>SALES POSITION:</strong></td>
                                <td align="left" valign="top"><input name="repTerr" type="text" id="repTerr" style="width:175px;text-align:center;" value="<%=evalAttendee.getSalesPositionId()%>" readonly></td>
                            </tr>
                            <tr>
                                <td align="right"><strong>Draft Prepared By:</strong></td>
                                <td>
                                    <input name="preparedBy" type="text" id="preparedBy" style="width:175px;text-align:center;" value="<%=(objSCE != null && (SCEConstants.ST_SUBMITTED.equalsIgnoreCase(objSCE.getStatus()) || SCEConstants.ST_DRAFT.equalsIgnoreCase(objSCE.getStatus()))) ? evaluatorNameP : ""%>" readonly>
                                </td>
                                <td align="right" valign="top"><strong>Submitted By:</strong></td>
                                <td align="left" valign="top"><input name="submittedBy" type="text" id="submittedBy" style="width:175px;text-align:center;" value="<%=(objSCE != null && SCEConstants.ST_SUBMITTED.equalsIgnoreCase(objSCE.getStatus())) ? evaluatorNameS : ""%>" readonly></td>
                            </tr>
                            </table>
                        
                        
                            <h1><%=SCEUtils.ifNull(objSCE.getFormTitle(),"")%> ACHIEVEMENT SCORES<em><br>
                            Reference tables I and II for each of the categories listed below</em></h1>
                            
                            <%--<table class="datagrid" style="width:1150px;">--%>
                            <table class="datagrid" style="">
                            <tr>
                                <th width="35%" align="left">SKILL CATEGORY</th>
                                <th width="10%" align="center"><%=score1Header%></th>
                                <th width="10%" align="center"><%=score2Header%></th>
                                <th width="10%" align="center"><%=score3Header%></th>
                                <th width="10%" align="center">AVERAGE SCORE</th>
                                <th width="10%" align="center">WIEGHTING<br>
                                (Multiply by)</th>
                                <th align="center">WEIGHTED SCORE</th>
                            </tr>
                            
                            <% 
                            SCEDetail objSCEDetail = null;
                            int questionNum = 0;                            
                            String strQ1 = "";
                            String strQ2 = "";
                            if (objSCE != null) {
                                for (Iterator iter =  objSCE.getSceDetailList().iterator(); iter.hasNext();) {
                                    questionNum++;
                                    objSCEDetail =  (SCEDetail)iter.next();
                                    if (SCEConstants.QT_SCORE.equalsIgnoreCase(objSCEDetail.getType()) || SCEConstants.QT_SCORE_FETCH.equalsIgnoreCase(objSCEDetail.getType())) {                                    
                                        allQ = allQ + "_" + objSCEDetail.getQuestionId();
                            %>
                            <tr>
                                <td><%=questionNum%>. <%=SCEUtils.ifNull(objSCEDetail.getDescription(), "")%></td>
                                
                                <%if (SCEConstants.QT_SCORE.equalsIgnoreCase(objSCEDetail.getType())) {
                                    strQ1 += questionNum + ", ";
                                %>
                                <td align="center">
                                <input type="text" name="question_score1_<%=objSCEDetail.getQuestionId()%>" id="question_score1_<%=objSCEDetail.getQuestionId()%>" style="width:75px;text-align:center;" value="<%=SCEUtils.ifNull(objSCEDetail.getQuestionScore1(), scoreFormatter, "")%>" <%=createMode ? "" : "readonly"%> onkeypress="onlyDigits()" onblur="updateRowData('<%=objSCEDetail.getQuestionId()%>')">
                                </td>
                                <td align="center">
                                    <input type="text" name="question_score2_<%=objSCEDetail.getQuestionId()%>" id="question_score2_<%=objSCEDetail.getQuestionId()%>" style="width:75px;text-align:center;" value="<%=SCEUtils.ifNull(objSCEDetail.getQuestionScore2(), scoreFormatter, "")%>" <%=createMode ? "" : "readonly"%> onkeypress="onlyDigits()" onblur="updateRowData('<%=objSCEDetail.getQuestionId()%>')">
                                </td>
                                <td align="center">
                                    <input type="text" name="question_score3_<%=objSCEDetail.getQuestionId()%>" id="question_score3_<%=objSCEDetail.getQuestionId()%>" style="width:75px;text-align:center;" value="<%=SCEUtils.ifNull(objSCEDetail.getQuestionScore3(), scoreFormatter, "")%>" <%=createMode ? "" : "readonly"%> onkeypress="onlyDigits()" onblur="updateRowData('<%=objSCEDetail.getQuestionId()%>')">
                                </td>
                                <%} else if (SCEConstants.QT_SCORE_FETCH.equalsIgnoreCase(objSCEDetail.getType())) {
                                    strQ2 += questionNum + ", ";
                                %>
                                <td align="center" valign="middle" style="background-color:#DDDDDD;">&nbsp;</td>                                
                                <td align="center" valign="middle" style="background-color:#DDDDDD;">&nbsp;</td>
                                <td align="center" valign="middle" style="background-color:#DDDDDD;">&nbsp;</td>
                                <input type="hidden" name="question_score1_<%=objSCEDetail.getQuestionId()%>" id="question_score1_<%=objSCEDetail.getQuestionId()%>" value="<%=SCEUtils.ifNull(objSCEDetail.getAverageScore(), scoreFormatter, "")%>">
                                <input type="hidden" name="question_score2_<%=objSCEDetail.getQuestionId()%>" id="question_score2_<%=objSCEDetail.getQuestionId()%>" value="<%=SCEUtils.ifNull(objSCEDetail.getAverageScore(), scoreFormatter, "")%>">
                                <input type="hidden" name="question_score3_<%=objSCEDetail.getQuestionId()%>" id="question_score3_<%=objSCEDetail.getQuestionId()%>" value="<%=SCEUtils.ifNull(objSCEDetail.getAverageScore(), scoreFormatter, "")%>">
                                <%}%>                                
                                
                                <td align="center">
                                    <span id="question_average_score_<%=objSCEDetail.getQuestionId()%>"><%=SCEUtils.ifNull(objSCEDetail.getAverageScore(), scoreFormatter, "")%></span>                                    
                                </td>
                                <td align="center" valign="middle">
                                    x<span id="question_weight_<%=objSCEDetail.getQuestionId()%>"><%=SCEUtils.ifNull(objSCEDetail.getWeight(), weightFormatter, "1")%></span>                                    
                                </td>
                                <td align="center" valign="middle">
                                    <span style="float:left">=</span><span style="" id="question_weighted_score_<%=objSCEDetail.getQuestionId()%>"><%=SCEUtils.ifNull(objSCEDetail.getWeightedScore(), scoreFormatter, "")%></span>
                                </td>
                            </tr>
                            <%
                                    }
                                }
                            }
                            %>
                            
                            <tr>
                                <td colspan="6" align="right">
                                    <strong>REPRESENTATIVE'S TOTAL SCORE<br>Minimum Successful Score is 75</strong>
                                </td>
                                <td align="center" valign="middle" style="background-color:#DDDDDD;"><span style="float:left">&nbsp;</span><strong><span id="total_score"><%=SCEUtils.ifNull(objSCE.getOverallScore(), scoreFormatter, "")%></span></strong></td>
                                <input type="hidden" name="overall_score" id="overall_score" value="">
                            </tr>
                            </table>
                        
                            <h1 style="margin-bottom:0px;"><strong>*<%=SCEUtils.ifNull(objSCE.getFormTitle(),"")%> Impact Behaviors to Evaluate:</strong></h1>
                            <p>Participation in <%=SCEUtils.ifNull(objSCE.getFormTitle(),"")%>, Constructive Feedback to Peers, Leadership, Integrity, Interpersonal Credibility, etc...</p>
                            
                            <h1><strong>**Exam: Passing 80%. To determine evaluation score refer to Table II</strong></h1>
                            
                            
                            <table class="datagrid" style="width:500px;">
                                <tr>
                                    <th align="left" colspan="10">TABLE I. EVALUATION FOR SKILL CATEGORIES <%=formatQuestionNumStr(strQ1)%></th>
                                </tr>                                
                                <tr>
                                    <td align="center"><strong>1</strong></td>
                                    <td align="center"><strong>1.5</strong></td>
                                    <td align="center"><strong>2</strong></td>
                                    <td align="center"><strong>2.5</strong></td>
                                    <td align="center"><strong>3</strong></td>
                                    <td align="center"><strong>3.5</strong></td>
                                    <td align="center"><strong>4</strong></td>
                                    <td align="center"><strong>4.5</strong></td>
                                    <td align="center" colspan="2"><strong>5</strong></td>                                    
                                </tr>
                                <tr>
                                    <td colspan="2" align="center">Unacceptable</td>
                                    <td colspan="2" align="center">Needs Improvement</td>
                                    <td colspan="2" align="center">Successful</td>
                                    <td colspan="2" align="center">Outstanding</td>
                                    <td colspan="2" align="center">Exceptional</td>                                    
                                </tr>
                            </table>
                            
                            <table class="datagrid" style="width:500px;">
                                <tr>
                                    <th align="left" colspan="5">TABLE II. EVALUATION SCORE FOR EXAM RESULTS IN CATEGORY <%=formatQuestionNumStr(strQ2)%></th>
                                </tr>
                                <tr>
                                    <td align="center"><strong>1</strong></td>
                                    <td align="center"><strong>2</strong></td>
                                    <td align="center"><strong>3</strong></td>
                                    <td align="center"><strong>4</strong></td>
                                    <td align="center"><strong>5</strong></td>                                                                       
                                </tr>
                                <tr>
                                    <td align="center">0% - 69%</td>
                                    <td align="center">70% - 79%</td>
                                    <td align="center">80% - 89%</td>
                                    <td align="center">90% - 94%</td>
                                    <td align="center">95% - 100%</td>                                     
                                </tr>
                            </table>
                            
                            <table class="datagrid" style="width:500px;">
                                <tr>
                                    <th align="left" colspan="5">WEIGHTED TOTAL SCORE RANKING</th>
                                </tr>
                                <tr>
                                    <td align="center"><strong>0 - 49</strong></td>
                                    <td align="center"><strong>50 - 74</strong></td>
                                    <td align="center"><strong>75 - 94</strong></td>
                                    <td align="center"><strong>95 - 109</strong></td>
                                    <td align="center"><strong>110 - 125</strong></td> 
                                </tr>
                                <tr>
                                    <td align="center">Unacceptable</td>
                                    <td align="center">Needs Improvement</td>
                                    <td align="center">Successful</td>
                                    <td align="center">Outstanding</td>
                                    <td align="center">Exceptional</td>                                    
                                </tr>
                            </table>
                                                       
                            <%
                            if (objSCE != null) {
                                for (Iterator iter =  objSCE.getSceDetailList().iterator(); iter.hasNext();) {
                                    objSCEDetail =  (SCEDetail)iter.next();
                                    if (SCEConstants.QT_COMMENTS.equalsIgnoreCase(objSCEDetail.getType())) {                                    
                            %>
                        
                            <h1><strong><%=SCEUtils.ifNull(objSCEDetail.getDescription(), "")%>:</strong></h1>
                            
                            <p><strong>
                                <div class="noprint">
                                <textarea class="comments" name="question_comments_<%=objSCEDetail.getQuestionId()%>" id="question_comments_<%=objSCEDetail.getQuestionId()%>" cols="100" rows="3" <%=createMode ? "" : "readonly"%>><%=SCEUtils.ifNull(objSCEDetail.getQuestionComments(),"")%></textarea>
                                </div>
                                <div class="print">
                                <textarea class="comments" name="print_question_comments_<%=objSCEDetail.getQuestionId()%>" id="print_question_comments_<%=objSCEDetail.getQuestionId()%>" <%=createMode ? "" : "readonly"%>><%=SCEUtils.ifNull(objSCEDetail.getQuestionComments(),"")%></textarea>
                                </div>
                                <label></label>
                            </strong></p>
                            <%
                                    }
                                }
                            }
                            %>                          
                        
                            <hr>
                            <input type="hidden" dataSource="{actionForm.evalEmplId}" />
                            <input type="hidden" dataSource="{actionForm.evalEventId}" />
                            <input type="hidden" dataSource="{actionForm.evalProduct}" />
                            <input type="hidden" dataSource="{actionForm.evalProductCode}" />
                            <input type="hidden" dataSource="{actionForm.evaluatorEmplId}" />
                            <input type="hidden" dataSource="{actionForm.evaluatorRole}" />
                            <input type="hidden" resultId="evaluatorEmplId" value="{actionForm.evaluatorEmplId}" />
                            <input type="hidden" resultId="evaluatorRole" value="{actionForm.evaluatorRole}" />
                            <!-- <netui-data:getData resultId="evalEventId" value="{actionForm.evalEventId}" /> -->
                            <%
                                boolean showSubmit = false;
                                boolean showSave= false;
                                // Get The Role of Evaluator
                                Attendee evaluatorCurr = sceManager.getAttendeeByEmplId((String)pageContext.getAttribute("evaluatorEmplId"));
                                User evalUserCurr = sceManager.getUserByEmplId((String)pageContext.getAttribute("evaluatorEmplId"));                          
                                // System.out.println("EVALUSERCURR"+evalUserCurr);
                                String role= (String)pageContext.getAttribute("evaluatorRole");
                                Integer eventId= (Integer)pageContext.getAttribute("evalEventId");
                             
                                // System.out.println("Actual role"+role);
                             
                               showSubmit = (evalUserCurr != null && ("SCE_Administrators".equalsIgnoreCase(evalUserCurr.getUserGroup()) || SCEConstants.TR_SUPERADMIN.equalsIgnoreCase(evalUserCurr.getUserGroup())));                      
                               showSave = (evalUserCurr != null && ("SCE_Administrators".equalsIgnoreCase(evalUserCurr.getUserGroup()) || SCEConstants.TR_SUPERADMIN.equalsIgnoreCase(evalUserCurr.getUserGroup())));                                                  
                              /* Adding code for RBU Phase 2 */
                               if(role !=null && eventId!=null)
                               {                           
                                 Role roleBean=sceManager.checkRoleAccess(role,eventId);
                                 if(roleBean !=null)
                                 {
                                    if(roleBean.getIsSubmit().equalsIgnoreCase("Y"))
                                    {
                                        showSubmit = true;
                                        showSave = true;
                                    }
                                    else if (roleBean.getIsSave().equalsIgnoreCase("Y"))
                                    {
                                        showSave = true;
                                    }
                                 }
                               }
                                // System.out.println("Show Submit-----"+showSubmit);                                                            
                            %>
                            <div class="noprint">
                            <%if (createMode) {%>
                            <input type="hidden" name="sce_id" id="sce_id" value="<%=objSCE.getId() != null ? objSCE.getId().intValue() : SCEConstants.SCE_INVALID_ID.intValue()%>" />
                            <input type="hidden" name="status" id="status" value=""/>
                            <input type="hidden" name="template_version_id" id="template_version_id" value="<%=objSCE.getTemplateVersionId()%>">
                            <%if (showSave) {%>
                            <input type="submit" value="Save" onclick="return validateSave();"> 
                            <%}%>                           
                            <%if (showSubmit) {%>
                            <input type="submit" value="Submit" onclick="return validateSubmit();">                            
                            <%}%>
                            <%}%>
                            <input type="submit" value="Close" onclick="window.close();return false;">      
                            </div>                      
                        </div>
                        <!--end page content wrapper -->
                    
                    
                    </TD>        
                </tr>
                
                <tr>
                    <td height="5" colspan="3" bgcolor="#092D7B"></td>
                </tr>
            </table>
            
            </s:form>
        </div>
    <!--end wrap -->
    </BODY>
</html>

<script>
    function updateRowData(questionId) {
        //alert('In updateRowData');
        var allQ = '<%=allQ%>';
        // All Spans
        var avgScoreSPAN = document.getElementById("question_average_score_" + questionId);
        var wtScoreSPAN = document.getElementById("question_weighted_score_" + questionId);
        var wtSPAN = document.getElementById("question_weight_" + questionId);
        var totalSPAN = document.getElementById("total_score");
        
        var wt = cleanNumber(wtSPAN.innerHTML);
        var score1 = document.getElementById("question_score1_" + questionId).value;
        var score2 = document.getElementById("question_score2_" + questionId).value;
        var score3 = document.getElementById("question_score3_" + questionId).value;
        
        if (score1 != '' || score2 != '' || score3 != '') {
            var divider = 0;
            var totalRowScore = 0;
            if (score1 != '') {
                //alert('totalRowScore:' + totalRowScore);
                totalRowScore = totalRowScore + cleanNumber(score1);
                divider++;
            }
            if (score2 != '') {
                //alert('totalRowScore:' + totalRowScore);
                totalRowScore = totalRowScore + cleanNumber(score2);
                divider++;
            }
            if (score3 != '') {
                //alert('totalRowScore:' + totalScore);
                totalRowScore = totalRowScore + cleanNumber(score3);
                divider++;
            }
            //alert('totalRowScore:' + totalRowScore);
            avgScoreSPAN.innerHTML = round(totalRowScore/divider, 2);
            wtScoreSPAN.innerHTML = round((totalRowScore/divider) * wt), 2;
        }
        else {
            avgScoreSPAN.innerHTML = '';
            wtScoreSPAN.innerHTML = '';
        }  
        
        var totalScore = 0;
        var showTotal = false;
        if (allQ != null && allQ.length > 0) {  
            var qIds = allQ.substring(1,allQ.length).split("_");
            
            for(var i=0; i<qIds.length; i++) {
                var qId = qIds[i];  
                if(qId != null && qId.length > 0) {                            
                    var wtSpanElem = document.getElementById('question_weighted_score_' + qId + '');                            
                    //alert('wtSpanElem.innerHTML:' + wtSpanElem.innerHTML);
                    if(wtSpanElem != null && wtSpanElem.innerHTML.length > 0) {
                        showTotal = true;
                        totalScore = totalScore + cleanNumber(wtSpanElem.innerHTML);                                                                                     
                    }
                }
            }
        }     
        if (showTotal) {
            totalSPAN.innerHTML = round(totalScore, 2);
        }
        else {
            totalSPAN.innerHTML = '';
        }
        
    }
</script>