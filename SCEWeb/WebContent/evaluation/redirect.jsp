<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.beans.TemplateVersion"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%-- <%@include file="IAM_User_Auth.jsp"%>   --%>

<!-- added for OAUTH shindo -->
<%@ page import="com.pfizer.sce.common.SCEConstants"%>


 
<html>
<script language="javascript">
   
   function redirectFunction(){  
	   console.log("started...");
	   
	   var INVITE_URL=window.location.href;
	
	   var OAUTH_URL = '<%=SCEConstants.OAUTH_CODE_URL+ SCEConstants.OAUTH_REDIRECT_URI%>';
	  
	  console.log(OAUTH_URL);
	  
	  inviteFlag='<%=SCEConstants.INVITE_FLAG%>';
	  console.log("flag is="+inviteFlag);
	  
		window.location.href = OAUTH_URL;

		var code = window.location.search.split('code=')[1];
		
		console.log("in home " +window.location);
		console.log("code is  "+code);
		
		console.log("if condition is  "+ inviteFlag.match("Y"));
		if (inviteFlag.match("Y")){
			document.forms[0].action = "acceptRejectInvite1"; 
			console.log(" inside acceptreject ");
	
			
		}
		else{
			document.forms[0].action = "oauth";  
		}
		
		if (code != null) {
		
			document.getElementById("authcode").value = code;	
			
			
			document.forms[0].submit();
		}
		
		
	}
</script>

<body onload="redirectFunction()">
	<form method="post">
		<input id="authcode" type="hidden" name="authcode" value="" />
	</form> 
</body>
</html>
