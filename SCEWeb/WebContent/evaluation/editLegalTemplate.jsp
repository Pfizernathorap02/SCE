s<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.beans.Descriptor"%>
<%@ page import="com.pfizer.sce.beans.Question"%>

<%@ page import="com.pfizer.sce.beans.LegalConsentTemplate"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp"%>

<%
   LegalConsentTemplate  legalConsentTemplate = (LegalConsentTemplate)request.getAttribute("legalConsentTemplate");
  // System.out.println("publish flag iss:::"+legalConsentTemplate.getPublishFlag());
  int lcId= legalConsentTemplate.getLcId();
 
   request.setAttribute("lcId",new Integer(legalConsentTemplate.getLcId()).toString());
   // System.out.println("lcid iss:::"+legalConsentTemplate.getLcId());
   String content=legalConsentTemplate.getContent();
   //System.out.println("content::::"+content);
    String draftVersion=(String)request.getAttribute("draftVersion");
     // System.out.println("draftversion::::"+draftVersion);
      
    %>
<script type="text/javascript">
  
   
  
function logoutYesNo(){
    if ( confirm("Any changes to the Legal Consent Template will be lost. Do you want to continue?")){
        
        window.location="gotoLegalTemplate";
    }

}

 function saveTemplate() {
    frm=document.forms[0];
    var content= trim(frm.newContent.value);
    var draftVersion='<%=draftVersion%>';
    
    if(content){
    	
        if(draftVersion){
        	
            if ( confirm("A draft version of the legal template already exists.Do you want to replace the existing draft version?")){
             
       window.document.forms[0].action="save"; 
      
	   window.document.forms[0].submit(); 
            }
        
        }
        else{
       
  	   window.document.forms[0].action="save";
  	 
	   window.document.forms[0].submit();  
        }
    }
    else{
        alert("Please enter details in Legal Text Area");
        document.forms[0].newContent.focus();    
    }
} 

      
    
 function openPreviewWindow() {
	    frm=document.forms[0];
	    var content= frm.newContent.value;
	    window.open('evaluation/legalTemplatePreview.jsp?cont='+content,'preview_window','status=yes,scrollbars=yes,height=500,width=750,resizable=yes');                    
	}
    


function trim(str) { 
    return str.replace(/^\s+|\s+$/g,"");
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
<link
	href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css"
	rel="stylesheet" type="text/css" media="all" />
<link
	href="<%=request.getContextPath()%>/evaluation/resources/_css/admin.css"
	rel="stylesheet" type="text/css" media="all" />
<!--[if IE 6]>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" />
        <![endif]-->
</head>
<body id="p_create_form" class="admin">
	<div id="wrap">
		<div id="top_head">
			<h1>Pfizer</h1>
			<h2>Sales Call Evaluation</h2>
			<%@include file="navbar.jsp"%>

			<!-----navbar.jsp to be included---->
			<!-- end #top_head -->
		</div>

		<div id="eval_sub_nav">
			<s:url action="admin" var="adminUrl"></s:url>
			<s:a href="%{adminUrl}">
				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoadmin.gif"
					alt="Back to main admin" width="119" height="18" />
			</s:a>
		</div>

		<h3>Admin: Edit Legal Consent Template</h3>

		<div id="main_content">
			<s:form action="saveLegalTemplate" id="legalConsentTemplateForm">
				<input type="hidden" name="pageName" id="pageName"
					value="scoringOptions" />
				<input type="hidden" value="<%=legalConsentTemplate.getLcId()%>"
					id="lc_id" name="lc_id" />
				<div>
					<textarea
						style="border: .1em solid grey; height: 400px; width: 800px; overflow-y: scroll;"
						name="newContent" cols="500" rows="25"><%=content%></textarea>
				</div>
				<input type="hidden" id="content" name="content" />



				<div class="add_buttons">

					<img
						src="<%=request.getContextPath()%>/evaluation/resources/_img/button_preview.gif"
						alt="Preview" width="52" height="19" onclick="openPreviewWindow()" />


					<img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save.gif" alt="save"
						width="38" height="19" onclick="saveTemplate()" /> <img
						src="<%=request.getContextPath()%>/evaluation/resources/_img/button_cancel.gif" alt="cancel"
						width="55" height="19" onclick="logoutYesNo()" />

				</div>

				<div class="clear"></div>
			</s:form>






			<div class="clear"></div>
		</div>
		<!-- end #content -->

	</div>
	<!-- end #wrap -->

</body>

</html>


