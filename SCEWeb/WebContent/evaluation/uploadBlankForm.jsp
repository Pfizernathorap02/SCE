<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp"%>
<%
	String templateName = request.getParameter("templateName");
	String templateVersion = request.getParameter("templateVersion");
	String templateVersionId = request
			.getParameter("templateVersionId");
	String scoringSystemIdentifier = request
			.getParameter("scoringSystemIdentifier");
%>

<script language="javascript">
function CheckFile()
{ 
    var file = document.getElementById('Uploadfile');        
    var len=file.value.length;    
    var ext=file.value;        
    if(len <= 0)
    {
        alert('Please select a file to upload');        
        return false;
    }
        else if(ext.substr(len-3,len)!="pdf" )
    {
        alert("Please select a pdf file ");
        return false;
    }
    else
    {
       
        document.forms[0].submit();
        
     
        return false;
    }
}


function load()
{
    //document.getElementById(getNetuiTagName('up')).disable=true;       
    var uploadComplete="<%=session.getAttribute("uploadcomplete")%>";
    if(uploadComplete!= null)
    {
        if(uploadComplete == 'uploadcomplete')
        {
            window.opener.document.forms[0].action='gotoTemplateAdmin.action';
            window.opener.document.forms[0].submit();   
            window.close();
        }
    }
}  

        
function checkFilled()
{
	//	alert("inside check filled1");
    var tempVal = '<%=request.getContextPath()%>';
    //	alert("inside check filled2");
		var filled = 0
		var file = document.getElementById("Uploadfile").value;
	//	alert("inside check filled3");
		var len = file.length;
		
		if (len > 0) {
			filled++
			
		}
		if (filled != 0) {
		//	alert("inside the upload ");
			document.getElementById("uploadbut").src = tempVal
					+ "/evaluation/resources/_img/upload_&_publish_button.gif";
			document.getElementById("uploadbut").onclick = CheckFile;
		} else {
		//	alert("inside the disabled upload ");
			document.getElementById("uploadbut").src = tempVal
					+ "/evaluation/resources/_img/disabled_upload_&_publish_button.gif";
		}
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
<link
	href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css"
	rel="stylesheet" type="text/css" media="all" />
<!--[if IE 6]><LINK href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" type=text/css rel=stylesheet><![endif]-->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/js/sorttable.js"></script>
<style type="text/css">
@import
url(<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar-win2k-cold-2.css)
;
</style>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/lang/calendar-en.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar-setup.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/js/DateValidator.js"></script>
</head>
<!-- <body id="p_search" class="search" onload="load();" onUnload="parent.window.opener.location.reload(true);" >-->
<body id="p_search" class="search" onload="load();">
	<div id="wrap">
		<DIV id=top_head>
			<H1>Pfizer</H1>
			<H2>Sales Call Evaluation</H2>
		</DIV>

		<H3>Upload Blank Evaluation Form</H3>
		<DIV id=main_content>
			<s:form action="uploadBlankForm" tagId="UploadBlankFormForm"
				enctype="multipart/form-data">
				<input type="hidden" name="templateVersionId"
					value="<%=templateVersionId%>" />
				<input type="hidden" name="scoringSystemIdentifier"
					value="<%=scoringSystemIdentifier%>" />
				<input type="hidden" name="templateName" value="<%=templateName%>" />
				<FIELDSET>
					<!-- <font color="red"><%=SCEUtils.ifNull(request.getAttribute("msg"), "")%></font>-->
					<div>
						<LABEL>Template Name </LABEL>
						<%=templateName%>
					</div>
					<div>
						<label>Template Version</label><%=templateVersion%>
					</div>
					<div>
						<label>Upload File:<font id="uploadFileMandatory"
							style="color:#ff0000;">*</font></label>
						<s:file name="fileUpload" label="User Image" onChange="checkFilled();" id="Uploadfile"  />
						

					</div>
					<div>
						<B><font style="color:#ff0000;">Upload a PDF file only</font></B>
					</div>
				</FIELDSET>
				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/disabled_upload_&_publish_button.gif"
					id="uploadbut" alt="upload&publish" width="100" height="19"
					onclick="CheckFile()" />
				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/btn_close.gif"
					id="close" alt="close" width="42" height="19"
					onclick="window.close();" />
			</s:form>
			<DIV class=clear></DIV>
		</DIV>
		<!-- end #content -->
	</DIV>
	<!-- end #wrap -->
</BODY>
</html>


