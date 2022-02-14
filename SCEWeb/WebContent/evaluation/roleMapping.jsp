
<%-- 
  - Author(s):shaikh07
  - Description: JSP page created by shaikh07 for Role Mapping for version 3.4 RFC#1136920 
  --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.pfizer.sce.beans.Role"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp"%>

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
	href="<%=request.getContextPath()%>/evaluation/resources/_css/admin_updateUser.css"
	rel="stylesheet" type="text/css" media="all" />
<title>Insert title here</title>

<%
	String[] eventProducts = {"Guest Trainer Manager"};
	String[] roles = null;
	String[] mappedRoles = null;
	String role = null;
	String status = null;
	SCEManagerImpl sceManager = new SCEManagerImpl();
	String message = (String) request.getAttribute("message");
%>

<script type="text/javascript">
	var sel = null;

	function fetchRole() {
<%roles = sceManager.getRoles();%>
	
<%mappedRoles = sceManager.getAllRoles();%>
	}

	function checkSelectedRole(tempobj) {
		var index = tempobj.selectedIndex;
		sel = tempobj.options[index].innerHTML;
	}

	function verify() {

		var valid = true;

		if (sel == null) {
			alert("Please select valid value for Role");
			valid = false;
		}

		if (valid == true) {

			if (confirm("Please confirm role addition!") == true) {
				window.document.forms[0].submit();
			}

		}

	}

	function deleteRole(role) 
	{

		if (confirm("Please confirm delete " + role + " !") == true)
		{
			document.getElementById("delRoleId").value = role;
			document.getElementById("deleteRole").submit();

		}

	}
</script>

</head>

<body onload="fetchRole()">

	<div id="wrap">
		<div id="top_head">
			<h1>Pfizer</h1>
			<h2>Sales Call Evaluation</h2>
		</div>
		<div id="eval_sub_nav">
			<s:a action="admin">
				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoadmin.gif"
					alt="Back to main admin" width="119" height="18" />
			</s:a>
		</div>
		<h3>Role Mapping</h3>
		<div id="main_content">
			<form action="saveRole" id="saveRole" method="post">
				<fieldset>
					<div>
					<font color="#3355ff"> 
						<%if(message!=null){ %>
							<label name="msg" id="msg" ></label><%=message%>
						<%} %>
 					</font>
				</div>
					<div>
						<label>Role:</label> <select id="roleValue" name="selRoleForGroup"
							onchange="checkSelectedRole(this)">
							<option value="0">---Select---</option>

							<%
								if (roles != null) {
									for (int i = 0; i < roles.length; i++) {
							%>

							<option value="<%=roles[i]%>"><%=roles[i]%></option>
							<%
								}
								}
							%>
						</select>
					</div>
					<div>
						<label>Group:</label> <input type="text" name="group"
							style="width: 200px;" value="Guest Trainer Manager" disabled />
					</div>
					<div>
						<img
							src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save.gif"
							alt="Add" width="30" height="19" onclick="verify()" />
					</div>
				</fieldset>
			</form>
			<div class="clear"></div>
		</div>


		<!-- code added by KUMAM1234 for Role Mapping for version 3.4 RFC#1136920 -->
		<form action="deleteRole" id="deleteRole" method="post">
			<div id="main_content">
				<table style="width: 450px;">
					<tr>
						<td colspan='2' align="center">
							<h3 style="text-align: center;">Roles Mapped</h3>
						</td>
					</tr>
					<%
						if (mappedRoles != null) {
							for (int i = 0; i < mappedRoles.length; i++) {
					%>

					<tr>

						<td width="85%"><b><%=mappedRoles[i]%></b></td>

						<td width="25%"><input type="hidden" id="delRoleId"
							name="delRoleId" value=""><div
									style="text-align: center;">
									<button onclick="deleteRole('<%=mappedRoles[i]%>')">Delete</button>
								</div></td>
						<%
							}
							}
						%>
					</tr>
				</table>
			</div>
		</form>
	</div>
</body>
</html>