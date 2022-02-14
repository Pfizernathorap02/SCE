<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.beans.EmailTemplate"%>
<%@ page import="com.pfizer.sce.beans.EmailTemplateForm" %> 
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>


<%
    String message = (String)request.getAttribute("message");
EmailTemplateForm emailtemplates=new EmailTemplateForm();
       emailtemplates = (EmailTemplateForm) request.getAttribute("emailtemplates");
    
%>
<script language="javascript">

function validateInput(evaluationTemplateId,scoringOption)
{   
    frm=document.forms[0];
    var cc1=frm.email_cc.value;
    
    for(i=0;i<cc1.length;i++){
        if(cc1.charAt(i)==',' ||cc1.charAt(i)==';'){
            if(cc1.charAt(i+1)==',' ||cc1.charAt(i+1)==';'){
                alert('Invalid email address. Please enter valid email address');
                frm.email_cc.focus();
                return false;
            }
        }
    }
    var bcc1=frm.email_bcc.value;
    
    for(i=0;i<bcc1.length;i++){            
        if(bcc1.charAt(i)==',' || bcc1.charAt(i)==';' ){
            if(bcc1.charAt(i+1)==',' ||bcc1.charAt(i+1)==';'){
                alert('Invalid email address. Please enter valid email address');
                frm.email_bcc.focus();
                return false;
            }
        }
    }
    
    var cc = frm.email_cc.value.split(/[;,]+/);
    var bcc = frm.email_bcc.value.split(new RegExp("[,;]", "g"))
    var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
    if(cc !=''){
        for(i=0;i<cc.length;i++){
            cc[i]=cc[i].replace(/^\s+|\s+$/g, "");
            if (reg.test(cc[i])==false || cc[i]=='' ||cc[i]==null){
                alert('Invalid email address. Please enter valid email address');
                frm.email_cc.focus();
                return false;
            }
        }
    }
    
    if(bcc !=''){
        for(i=0;i<bcc.length;i++){
            bcc[i]=bcc[i].replace(/^\s+|\s+$/g, "");
            if (reg.test(bcc[i])==false|| bcc[i]=='' ||bcc[i]==null){
                alert('Invalid email address. Please enter valid email address');
                frm.email_bcc.focus();
                return false;
            }
        }
    }
    
    var subject = document.getElementById('email_subject');
    if(subject.value==null  || trim(subject.value)==''){
         alert("Please enter Email Subject");
         subject.focus();
         return false;
     }
     
    if(subject.value.match(/([\<])([^\>]{1,})*([\>])/i)!=null){
        alert("Email Subject contain HTML tags. Please enter valid email subject");
        subject.focus();
        return false;
    }
     
    var body = document.getElementById('txtmessage');
    if(body.value == null || trim(body.value)==''){
         alert("Please enter Email Body");
         body.focus();
         return false;
    }
    
    window.document.getElementById('selEvaluationTemplateId').value = evaluationTemplateId;
    window.document.getElementById('selScoringSystemIdentifier').value = scoringOption;
    
    window.document.forms[0].submit();
}

function trim(str) { 
    return str.replace(/^\s+|\s+$/g,"");
}



function cancelEdit(evaluationTemplateId,scoringOption)
{
if (confirm('Any changes to email template will be lost. Do you want to continue?'))
     { 
     window.document.getElementById('selEvaluationTemplateId').value = evaluationTemplateId;
     //window.document.getElementById('selEvaluationTemplateId').value = evaluationTemplateId;
     window.document.getElementById('selScoringSystemIdentifier').value = scoringOption;
     //window.document.getElementById('selScoringSystemIdentifier').value = scoringOption;
       window.document.forms[0].action="searchEmailTemplates"; 
       window.document.forms[0].submit(); 
     }
else {

     }
}

function preview() {
        frm=document.forms[0];
        var cc = frm.email_cc.value;
        var bcc = frm.email_bcc.value;
        var subject = frm.email_subject.value;
        var body= frm.txtmessage.value;
        window.open('evaluation/emailTemplatePreview.jsp?emailBody='+body+'&emailSubject='+subject+'&emailCc='+cc+'&emailBcc='+bcc,'preview_window','status=yes,scrollbars=yes,height=600,width=800,resizable=yes');                    
}

</script>

<html>
    <head>
    <script language="JavaScript" type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js"></script>
<script language="JavaScript" type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.5.2/jquery-ui.min.js"></script>
<script language="JavaScript" type="text/javascript">
$(document).ready( function()
{
	$("#firstname").draggable({helper: 'clone'});
    $(".txtDropTarget").droppable({
		accept: "#firstname",
        drop: function(ev, ui) {
			$(this).insertAtCaret(ui.draggable.text());
        }
    });
    $("#lastname").draggable({helper: 'clone'});
    $(".txtDropTarget").droppable({
		accept: "#lastname",
        drop: function(ev, ui) {
			$(this).insertAtCaret(ui.draggable.text());
        }
    });
    $("#overallscore").draggable({helper: 'clone'});
    $(".txtDropTarget").droppable({
		accept: "#overallscore",
        drop: function(ev, ui) {
			$(this).insertAtCaret(ui.draggable.text());
        }
    });
    $("#date").draggable({helper: 'clone'});
    $(".txtDropTarget").droppable({
		accept: "#date",
        drop: function(ev, ui) {
			$(this).insertAtCaret(ui.draggable.text());
        }
    });
     $("#managername").draggable({helper: 'clone'});
    $(".txtDropTarget").droppable({
		accept: "#managername",
        drop: function(ev, ui) {
			$(this).insertAtCaret(ui.draggable.text());
        }
    });
     $("#coursename").draggable({helper: 'clone'});
    $(".txtDropTarget").droppable({
		accept: "#coursename",
        drop: function(ev, ui) {
			$(this).insertAtCaret(ui.draggable.text());
        }
    });
     $("#templatename").draggable({helper: 'clone'});
    $(".txtDropTarget").droppable({
		accept: "#templatename",
        drop: function(ev, ui) {
			$(this).insertAtCaret(ui.draggable.text());
        }
    });
     $("#evaluator").draggable({helper: 'clone'});
    $(".txtDropTarget").droppable({
		accept: "#evaluator",
        drop: function(ev, ui) {
			$(this).insertAtCaret(ui.draggable.text());
        }
    });
});
/* shindo modified this.form.textmessage for Edge */
$.fn.insertAtCaret = function (myValue) {
	return this.each(function(){
			
			if (document.selection) {
					this.form.txtmessage.focus();
					sel = document.selection.createRange();
					sel.text = myValue;
					this.form.txtmessage.focus();
			}
			
			else if (this.form.email_bodyselectionStart || this.form.txtmessage.selectionStart == '0') {
					var startPos = this.form.txtmessage.selectionStart;
					var endPos = this.form.txtmessage.selectionEnd;
					var scrollTop = this.form.txtmessage.scrollTop;
					this.form.txtmessage.value = this.form.txtmessage.value.substring(0, startPos)+ myValue+ this.form.txtmessage.value.substring(endPos,txtmessage.value.length);
					this.form.txtmessage.focus();
					this.form.txtmessage.selectionStart = startPos + myValue.length;
					this.form.txtmessage.selectionEnd = startPos + myValue.length;
					this.form.txtmessage.scrollTop = scrollTop;
			} else {
					this.form.txtmessage.value += myValue;
					this.form.txtmessage.focus();
			}
	});
};



                  
</script>



                  
</script>
<style type="text/css" media="Screen">
	#tags label{cursor:pointer;}
</style> 
<style type="text/css">
#sidebar img {
margin-left:5px;
margin-right:5px;
}
</style>       
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="Content-Language" content="en-us" />
        <title>Pfizer Sales Call Evaluation</title>
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

    </head>
    <body id="p_edit_email_template"  class="admin">
        <div id="wrap">
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2> <%@include file="navbar.jsp" %>
                <!-- end #top_head -->
            </div>
                       <div id="eval_sub_nav">
                <div id="eval_sub_nav">
                <s:a action="admin"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoadmin.gif" alt="Back to main admin" width="119" height="18" /></s:a>
            
            </div>
            </div>
            <h3> Edit Email Notification Template </h3>
            <div id="main_content">
                <%
                if (message != null && !"".equals(message.trim())) {
                %> <%=message%>
                <%
                }
                %>
                <div>
                    <s:form action="saveEmailTemplate">
                    <input type="hidden" value="<%=emailtemplates.getEmailTemplateId()%>" id="emailTemplateId" name="emailTemplateId"/>
                    <input type="hidden" value="<%=emailtemplates.getEvaluationTemplateName()%>" id="evaluationTemplateName" name="evaluationTemplateName"/>
                    <input type="hidden" value="<%=emailtemplates.getEvaluationTemplateId()%>" id="selEvaluationTemplateId" name="selEvaluationTemplateId"/>
                    <input type="hidden" value="<%=emailtemplates.getEmailTemplateVersion()%>" id="emailTemplateVersion" name="emailTemplateVersion"/>
                    <input type="hidden" value="<%=emailtemplates.getEvaluationTemplateVersionId()%>" id="templateVersionId" name="templateVersionId"/>
                    <input type="hidden" value="<%=emailtemplates.getOverallScore()%>" id="overallScore" name="overallScore"/>
                    <input type="hidden" value="<%=emailtemplates.getScoringSystemIdentifier()%>" id="selScoringSystemIdentifier" name="selScoringSystemIdentifier"/>
                    <input type="hidden" value="<%=emailtemplates.getPublishFlag()%>" id="publishFlag" name="publishFlag"/>
                    <!-- <input type="hidden" value="" id="emailBody" name="emailBody"/> -->     
               <h4>Available Tags</h4>
                 
                  <fieldset> 
                  <div id="tags">
                  <table>
                  <tr><td><label id="firstname">$(First Name)</label></td><td><label id="lastname">$(Last Name)</label></td><td><label id="overallscore">$(Overall Score) </label></td><td><label id="date">$(Date of Evaluation)</label></td></tr>
                   <tr><td><label id="managername">$(Manager's Name)</label></td><td><label id="coursename">$(Course Name)</label></td><td><label id="templatename">$(Evaluation Template Name)</label></td><td><label id="evaluator">$(Evaluator)</label></td></tr>
                  </table>
                  </div>
                  </fieldset>
                  <br>
                  <br>
                  <fieldset>
                    <div>
                    <label id="evaluationTemplateName">Template Name: </label><%=emailtemplates.getEvaluationTemplateName()%>	
                  	</div>
                    <div>
                    <label id="emailTemplateVersion">Template Version: </label><%=emailtemplates.getEmailTemplateVersion()%>
                     </div>
                    <div>
                    <label id="overallScore">Overall Score: </label><%=emailtemplates.getOverallScore()%>
                    </div>
  
                    <div>
                   <label>Cc: </label><input style="width:625px; size:100px" class="email_cc" name="email_cc" id="email_cc" type="text" value="<%=SCEUtils.replace(SCEUtils.ifNull(emailtemplates.getEmailCc(),""),"\"","&quot;")%>"/>
                    </div>
                    <div>
                    <label>Bcc: </label>
                    <input class="email_bcc" style="width:625px; size:100px" name="email_bcc" id="email_bcc" type="text" value="<%=SCEUtils.replace(SCEUtils.ifNull(emailtemplates.getEmailBCc(),""),"\"","&quot;")%>"/>
                    </div>
                    <div>
                    <label>Subject:<font color="red">* </font></label>
                   
                    <input class="email_subject" style="width:625px; size:100px" name="email_subject" id="email_subject" type="text" value="<%=SCEUtils.replace(SCEUtils.ifNull(emailtemplates.getEmailSubject(),""),"\"","&quot;")%>"/>
                    </div>
                    <div class="text">
                    
                    <label>Email Body:<font color="red">* </font></label>
                      <textarea class="txtDropTarget" id="txtmessage" dir="ltr" name="txtmessage" rows="20" style="width:625px; size:800px"><%=SCEUtils.replace(SCEUtils.ifNull(emailtemplates.getEmailBody(),""),"\"","&quot;")%></textarea>
                    </div>
                    <!-- shindo added style -->
                 <div id="sidebar" class="add_buttons" style="float: left;" >
                    
                   <img src="resources/_img/button_cancel.gif" width="55" height="19" alt="Cancel" align="right" onclick="cancelEdit(<%=emailtemplates.getEvaluationTemplateId()%>,'<%=emailtemplates.getScoringSystemIdentifier()%>')"/>
                  <img src="resources/_img/button_save.gif" width="38" height="19" name="Save" align="right" alt="Save" onclick="return validateInput(<%=emailtemplates.getEvaluationTemplateId()%>,'<%=emailtemplates.getScoringSystemIdentifier()%>');">
                    <img src="resources/_img/button_preview.gif" width="52" height="19" alt="Preview" align="right" onclick="preview()"/>
                </div>
                                    </s:form>
    </body>
</html>