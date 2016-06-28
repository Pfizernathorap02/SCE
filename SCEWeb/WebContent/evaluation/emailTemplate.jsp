<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.pfizer.sce.beans.EmailTemplate"%>
<%@ page import="com.pfizer.sce.beans.EmailTemplateForm"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*" %>


<%  
    String message = (String)request.getAttribute("message");
    EmailTemplate[] emailtemplates = (EmailTemplate[])request.getAttribute("emailtemplates");
      String selectedScoringOption = "--Select--";
     //pageContext.setAttribute("defaultScoringOption",selectedScoringOption);  
    Integer selectedEvaluationTemplateId = new Integer(-1);
    if(session.getAttribute("scoringOptions")!=null && request.getAttribute("defaultScoringOption")!=null){
       selectedScoringOption = (String)request.getAttribute("defaultScoringOption");
        // System.out.println("sel"+selectedScoringOption);
       
    }
    if(request.getAttribute("evaluationTemplateId") != null){
        selectedEvaluationTemplateId = new Integer(request.getAttribute("evaluationTemplateId").toString());
        // System.out.println("if 2 sel"+selectedScoringOption);
        // System.out.println("selectedEvaluationTemplateId:"+selectedEvaluationTemplateId);
    } 
   
      pageContext.setAttribute("defaultScoringOption",selectedScoringOption);
    pageContext.setAttribute("evaluationTemplateId",selectedEvaluationTemplateId);
    
    //Integer stemplateid = new Integer((String)request.getAttribute("selEvaluationTemplateId"));
    //String soption = (String) request.getAttribute("selScoringSystemIdentifier");
 %>
<script language="javascript">


function checkSelection() { 

document.getElementById('pageName').value = "scoringOptions";
//alert(document.getElementById('pageName').value);
var evaluationTemplateId = document.getElementById('evaluationTemplateName').value;

window.document.getElementById('selEvaluationTemplateId').value = evaluationTemplateId;



//document.getElementById('scoringOption').value = null;
//alert("before submitting");
document.forms[0].action="getScoringOptions";
document.forms[0].submit();
}

function enableSearchButton(){
    var sel_template = document.getElementById('evaluationTemplateName');   
    var attendeeVal = sel_template.options[sel_template.selectedIndex].value;
    var evaluationTemplate = document.getElementById('evalTemplateName');  
    document.getElementById('evaluationTemplateName').value=<%=selectedEvaluationTemplateId%>
    
    var scoringOption = document.getElementById('scoringOption').value;
    if((evaluationTemplate != -1) && (scoringOption != '--Select--')){
    window.document.getElementById("imgSearch").src="<%=request.getContextPath()%>/evaluation/resources/_img/button_search.gif";
    window.document.getElementById("imgSearch").onclick=search;
}
}

function search(){
    //document.getElementById('pageName').value = "scoringOptions";
    var evaluationTemplate = document.getElementById('evaluationTemplateName').value;
  //  alert("1evaluationTemplate:"+evaluationTemplate);
    if(isNaN(evaluationTemplate)){
     //alert("selEvaluationTemplateId:"+window.document.getElementById('selEvaluationTemplateId').value);
    }else{
        window.document.getElementById('selEvaluationTemplateId').value = evaluationTemplate;
    }
    //alert("2selEvaluationTemplateId:"+window.document.getElementById('selEvaluationTemplateId').value);
    var scoringOption = document.getElementById('scoringOption').value;
    window.document.getElementById('selScoringSystemIdentifier').value = scoringOption;
    document.forms[0].action="searchEmailTemplates";                
    window.document.forms[0].submit();
}
function publishEmailTemplate(emailTemplateId,emailSubject,emailBody) 
{       
        window.document.getElementById('selEmailTemplateId').value = emailTemplateId;
        //alert(window.document.getElementById('selEmailTemplateId').value);
        var evaluationTemplateId =document.getElementById('evaluationTemplateName').value; 
        var scoringOption = document.getElementById('scoringOption').value;
        var subject = emailSubject;
        var body = emailBody;
        if (subject.length == 1 || body.length == 1){
        alert("Email Subject or body is blank. Please enter email subject and email body before publishing");
        return false;
       }
        window.document.getElementById('selEvaluationTemplateId').value = evaluationTemplateId;
        window.document.getElementById('selScoringSystemIdentifier').value = scoringOption;
   <%--  window.document.getElementById(getNetuiTagName("EmailTemplateSearchForm1")).action="<%=PageflowTagUtils.getRewrittenFormAction("publishEmailTemplate", pageContext)%>"; --%>
        //alert("pageName:"+document.getElementById('pageName').value);
        //document.getElementById('pageName').value = "scoringOptions";
        window.document.forms[1].action="publishEmailTemplate";
        window.document.forms[1].submit();        
}    
function editEmailTemplate(emailTemplateId) 
{
	//alert('ala re ala');
	//alert(emailTemplateId);
        window.document.getElementById('selEmailTemplateId').value = emailTemplateId;
      //  alert(window.document.getElementById('selEmailTemplateId').value );
        var evaluationTemplateId =document.getElementById('evaluationTemplateName').value; 
       // alert('1');
        var scoringOption = document.getElementById('scoringOption').value;
       // alert('2');
        window.document.getElementById('selEvaluationTemplateId').value = evaluationTemplateId;
      // alert('3');
        window.document.getElementById('selScoringSystemIdentifier').value = scoringOption;
        <%-- window.document.getElementById(getNetuiTagName("EmailTemplateSearchForm1")).action="<%=PageflowTagUtils.getRewrittenFormAction("", pageContext)%>";  --%>               
        //document.getElementById('pageName').value = "scoringOptions";
       // alert('4'); 
        window.document.forms[1].action="gotoEditEmailTemplate";
        window.document.forms[1].submit();        
    } 

function deleteEmailTemplate(emailTemplateId)
{
	    
        var evaluationTemplateId =document.getElementById('evaluationTemplateName').value; 
        var scoringOption = document.getElementById('scoringOption').value;
        window.document.getElementById('selEvaluationTemplateId').value = evaluationTemplateId;
        window.document.getElementById('selEmailTemplateId').value = emailTemplateId;
        window.document.getElementById('selScoringSystemIdentifier').value = scoringOption;
        var email=window.document.getElementById('selEmailTemplateId').value;
        //document.getElementById('pageName').value = "scoringOptions";
        if (confirm('Are you sure you want to remove this template?'))
        {
            <%-- window.document.getElementById(getNetuiTagName("EmailTemplateSearchForm1")).action="<%=PageflowTagUtils.getRewrittenFormAction("deleteEmailTemplate", pageContext)%>";   --%>
            window.document.forms[1].action="deleteEmailTemplate";
            window.document.forms[1].submit(); 
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
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/admin.css" rel="stylesheet" type="text/css" media="all" />
        <!--[if IE 6]>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" />
        <![endif]-->
    </head>
    <body onload="enableSearchButton();" id="p_email_template_maintenance"  class="admin">   
        <div id="wrap" >
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
            <h3> Admin: Email Notification Template Maintenance </h3>
            <div id="main_content">
                <%
                if (message != null && !"".equals(message.trim())) {
                %> <%=message%>
                <%
                }
                %>
                <div align="center" STYLE=" border:0 solid grey;">
                    <s:form action="searchEmailTemplates" tagId="EmailTemplateSearchForm">
                        <input type="hidden" name="pageName" id="pageName" value="scoringOptions"/>
                        <input type="hidden" id="selEvaluationTemplateId" name="selEvaluationTemplateId" value = "<%=request.getAttribute("evaluationTemplateId")%>" />
                        <input type="hidden" id="selScoringSystemIdentifier" name="selScoringSystemIdentifier" value="<%=request.getAttribute("defaultScoringOption")%>"/>
                        
                        <div>  <table cellpadding="0" cellspacing="0" style="border:0;colour:'white';">
                          <tr>
                                <td style="border:0;colour:'white';">
                                <b>Evaluation Template </b></td>
                                <td  style="border:0;colour:'white';"> 
                                <b>Scoring Option </b></td>
                                </tr>
                                <tr>
<%--                                 <% --%>
<!-- //                                 HashMap evaluationTemplateTemp = new HashMap(); -->
<!-- //                                 evaluationTemplateTemp = (HashMap)session.getAttribute("evaluationTemplate"); -->
<!-- //                                 // System.out.println("The value of retrived has map set in sessino scope"+evaluationTemplateTemp ); -->
<%--                                 %> --%>
                                
                                <td style="border:0;colour:'white';">
<!--                                 <netui:select dataSource="{actionForm.evaluationTemplateName}" style="width:10" defaultValue="{pageContext.evaluationTemplateId}" optionsDataSource="{pageContext.templateMap}" tagId="evaluationTemplateName" onChange="checkSelection();"/></td> -->
                                
                                <s:select
										list="#session.evaluationTemplate"  name="evaluationTemplateName" id="evaluationTemplateName"  
										onchange="checkSelection();"   /></td>
										
								
                                
                                <%                             
                                //String[] scoringOptions = null;
                                HashMap scoringOptions = new HashMap();
                                scoringOptions = (HashMap)session.getAttribute("scoringOptions"); 
                                // System.out.println("IN JSP after getting the value of second list map");
                                // System.out.println("Value of scoringOptions"+ scoringOptions);
                                // System.out.println("Value of default"+ request.getAttribute("default") );
                                //scoringOptions = (HashMap)request.getAttribute("scoringOptions");                                                          
                                 //if (scoringOptions!=null && request.getAttribute("default") !=null){
                                	 if (scoringOptions!=null){
                                // if(request.getAttribute("default").equals("checkDefault") ||request.getAttribute("default")==null ){
                                    pageContext.setAttribute("scoringOptions",scoringOptions);                                                    
                                    
                                    // System.out.println("IN JSP wihtin IF");
                                 %>
                                 <td style="border:0;colour:'white';">                               
<!--                                 <netui:select dataSource="{actionForm.scoringOption}" style="width:10" optionsDataSource="{pageContext.scoringOptions}" onChange="validatesearch()" tagId="scoringOption"/></td> -->
								<s:select
										list ="#session.scoringOptions" name="scoringOption" id="scoringOption"
										onChange="validatesearch()"  /></td>
                                <%
                                 //}
                                 
                                 
                                 }
                                else {
                                	 // System.out.println("IN JSP wihtin ELSE");
//                                     if (scoringOptions!=null){
//                                         pageContext.setAttribute("scoringOptions",scoringOptions); 
//                                     }
                                %>
                                <td style="border:0;colour:'white';">
<!--                                 <netui:select dataSource="{actionForm.scoringOption}" defaultValue="{pageContext.defaultScoringOption}"  style="width:10" optionsDataSource="{pageContext.scoringOptions}" onChange="validatesearch()" tagId="scoringOption"/></td> -->
									<select>  
									<option value="0">---Select---</option> 
									</select></td>

                                <%
                                }                                             
                                %>
                                <td style="border:0;colour:'white';">
                                    <div STYLE="height: 19px; width: 1000px;"> 
                                        <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_search_disable.gif" id="imgSearch" name="imgSearch"/>
                                    </div>
                                </td>

                            </tr>
                        </table></div>
                        </s:form>
                    <%
                    if (emailtemplates != null && emailtemplates.length>0){
                    %>       
                    <div align="center" id="main_content">
                        <s:form action="searchEmailTemplates" tagId="EmailTemplateSearchForm1">
                        <input type="hidden" name="pageName" id="pageName" value="scoringOptions"/>
                        <input type="hidden" id="selEmailTemplateId" name="selEmailTemplateId" value=""/>
                        <input type="hidden" id="selEvaluationTemplateId" name="selEvaluationTemplateId" value="<%=request.getAttribute("evaluationTemplateId")%>"/> 
                        <input type="hidden" id="selScoringSystemIdentifier" name="selScoringSystemIdentifier" value="<%=request.getAttribute("defaultScoringOption")%>"/> 
                        <table cellspacing="0">
                        <tr>
                                    <th>Evaluation Template</th>
                                    <th>Overall Score</th>
                                    <th>Email Template Version</th>
                                    <th></th><th></th><th></th>
                                </tr>
                        <%
                                EmailTemplate objEmailTemplates=null;
                                 String publishFlag="\u0000";
                                Integer emailTemplateId=null;
                                Integer count = new Integer (0);                                                                
                                 if (emailtemplates != null) {
                                    
                                  for (int i=0; i<emailtemplates.length; i++) {                            
                                    objEmailTemplates = emailtemplates[i];
                                    objEmailTemplates.setEmailTemplateId(emailtemplates[i].getEmailTemplateId());
                                    objEmailTemplates.setEvaluationTemplateName(emailtemplates[i].getEvaluationTemplateName());
                                    objEmailTemplates.setScoringSystemIdentifier(emailtemplates[i].getScoringSystemIdentifier());
                                    objEmailTemplates.setOverallScore(emailtemplates[i].getOverallScore());
                                    objEmailTemplates.setEmailSubject(emailtemplates[i].getEmailSubject());
                                    objEmailTemplates.setEmailBody(emailtemplates[i].getEmailBody());
                                    count = new Integer(0);
                                    for(int j=0;j<emailtemplates.length; j++){
                                        if (emailtemplates[j].getEvaluationTemplateName().equalsIgnoreCase((objEmailTemplates.getEvaluationTemplateName())) && emailtemplates[j].getScoringSystemIdentifier().equalsIgnoreCase(objEmailTemplates.getScoringSystemIdentifier()) && emailtemplates[j].getOverallScore().equalsIgnoreCase(objEmailTemplates.getOverallScore())){
                                            count = new Integer(count.intValue()+1);
                                        }
                                     }                                     
                                    publishFlag = objEmailTemplates.getPublishFlag();
                                    // System.out.println("publish flag "+publishFlag);
                                    %>
                                <tr>                                   
                                    <td><%=emailtemplates[i].getEvaluationTemplateName()%></td>
                                    <td><%=emailtemplates[i].getOverallScore()%></td>
                                    <td><%=emailtemplates[i].getEmailTemplateVersion()%></td>
                                    <%
                                    if ((publishFlag.equalsIgnoreCase("Y") || publishFlag.equalsIgnoreCase("y"))){                                   
                                     %>
                                    <td><img src="resources/_img/disabled_publish_button.gif" width="52" height="19" alt="Disable_Publish"/></td>
                                        <%
                                        if(count.intValue()==1){
                                        %>
                                        <td><img src="resources/_img/edit.gif" alt="Edit" width="40" height="18" onclick="editEmailTemplate(<%=objEmailTemplates.getEmailTemplateId()%>);"/></td>                                   
                                        <%
                                        }
                                        else{
                                        %>
                                        <td><img src="resources/_img/disabled_edit.gif" alt="Edit" width="40" height="18"/></td>
                                        <%
                                        }%>
                                    <td><img src="resources/_img/disable_delete.gif" alt="Delete" width="44" height="19"/></td>  
                                    <%
                                    }
                                    else if ((publishFlag.equalsIgnoreCase("N") || publishFlag.equalsIgnoreCase("n"))){
                                       if(count.intValue()==1){
                                    %>
                                     <td><img src="resources/_img/enabled_publish_button.gif" width="50" height="19" alt="Publish" onclick="publishEmailTemplate(<%=objEmailTemplates.getEmailTemplateId()%>,'<%=SCEUtils.ifNull(objEmailTemplates.getEmailSubject(),"&nbsp;")%>','<%=SCEUtils.ifNull(objEmailTemplates.getEmailBody(),"&nbsp;")%>')"/></td>
                                    <td><img src="resources/_img/edit.gif" alt="Edit" width="40" height="18" onclick="editEmailTemplate(<%=objEmailTemplates.getEmailTemplateId()%>)"/></td>                                   
                                    <td><img src="resources/_img/disable_delete.gif" alt="Delete" width="44" height="19"/></td> 

                                    <%
                                       }
                                       else{
                                        %>
                                    <td><img src="resources/_img/enabled_publish_button.gif" width="50" height="19" alt="Publish" onclick="publishEmailTemplate(<%=objEmailTemplates.getEmailTemplateId()%>,'<%=SCEUtils.ifNull(objEmailTemplates.getEmailSubject(),"&nbsp;")%>','<%=SCEUtils.ifNull(objEmailTemplates.getEmailBody(),"&nbsp;")%>')"/></td>
                                    <td><img src="resources/_img/edit.gif" alt="Edit" width="40" height="18" onclick="editEmailTemplate(<%=objEmailTemplates.getEmailTemplateId()%>)"/></td>                                   
                                    <td><img src="resources/_img/button_delete.gif" alt="Delete" width="45" height="19" onclick="deleteEmailTemplate(<%=objEmailTemplates.getEmailTemplateId()%>)"/></td>                                   

                                    <%
                                       }
                                       
                                    }
                                       
                                    else{
                                  %>
                                    <td><img src="resources/_img/enabled_publish_button.gif" width="50" height="19" alt="Publish" onclick="publishEmailTemplate(<%=objEmailTemplates.getEmailTemplateId()%>,'<%=SCEUtils.ifNull(objEmailTemplates.getEmailSubject(),"&nbsp;")%>','<%=SCEUtils.ifNull(objEmailTemplates.getEmailBody(),"&nbsp;")%>')"/></td>
                                    <td><img src="resources/_img/edit.gif" alt="Edit" width="40" height="18" onclick="editEmailTemplate(<%=objEmailTemplates.getEmailTemplateId()%>)"/></td>                                   
                                    <td><img src="resources/_img/button_delete.gif" alt="Delete" width="45" height="19" onclick="deleteEmailTemplate(<%=objEmailTemplates.getEmailTemplateId()%>)"/></td>                                   

                                    <%
                                    
                                  }
                                  }
                                 }
                                %>                                                    
                                </tr>
                 
                            </table>
                        </s:form>
                        <div class="clear"></div>
                        <%
                    }
                    
                    %>
                    </div>
                    <!-- end #content -->
                </div>
                <!-- end #wrap -->
            </body>
        
</html>