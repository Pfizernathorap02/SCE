
<%-- 
  - Author(s):MUZEES
  - Description: JSP page created by MUZEES for BU Mapping 
  --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.pfizer.sce.beans.BU"%>
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
	String[] buList=null;
	BU[] mappedsalesOrgBUList = null;
	String[] salesOrgList = null;
	String status = null;
	SCEManagerImpl sceManager = new SCEManagerImpl();
	String message = (String) request.getAttribute("message");
%>
<style media="screen" type="text/css">
      .layer1_class { visibility: visible; }
    </style>

<script type="text/javascript">
	var sel = null;

	function fetchBU() {
		<%buList=sceManager.getBUList();
		mappedsalesOrgBUList=sceManager.getAllMappedSalesOrg();
		salesOrgList=sceManager.getSalesOrg();%>
	}
	
	function removeMsg() {
	 document.all["layer1"].style.visibility="hidden";
	 }
	function verify() {
		
	 	var bu_id=document.getElementById("businessUnit").value;
	 	
	 	var sales_org=document.getElementById("salesOrganisation").value;
	 	
		if (bu_id == "0") {
			alert("Please select business unit");
			return false;
		}
	 	
		if (sales_org == "0") {
			alert("Please select sales organization");
			return false;
		}
			if (confirm("Are you sure you want to map "+sales_org+" with "+bu_id+" ?") == true) {
				window.document.forms[0].submit();
			}

		}

	

	function deleteSelectedSalesOrg(salesorg) 
	{

		if (confirm("Are you sure you want to delete " +salesorg+ " ?") == true)
		{	
			document.getElementById("delSalesOrg").value = salesorg;
			document.getElementById("deleteSalesOrg").submit();
		}

	}
</script>

</head>

<body onload="fetchBU()">

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
		<h3>Business Unit Mapping</h3>
		<div id="main_content">
			<form action="saveBUMapping" id="saveBUMapping" method="post">
				<fieldset>
				<div id="layer1" class="layer1_class">
					<font color="#3355ff"> 
						<%if(message!=null){ %>
							<label name="msg" id="msg" ></label><%=message%>
						<%} %>
 					</font>
				</div>
					<div>
						<label>Business Unit:</label> <select id="businessUnit" name="businessUnit" onclick="removeMsg()">
							<option value="0">---Select---</option>

							<%
								if (buList != null) {
									for (int i = 0; i < buList.length; i++) {
							%>

							<option value="<%=buList[i]%>"><%=buList[i]%></option>
							<%
								}
								}
							%>
						</select>
					</div>
					<div>
						<label>Sales Organization:</label> <select id="salesOrganisation" name="salesOrganisation" onclick="removeMsg()">
							<option value="0">---Select---</option>

							<%
								if (salesOrgList != null) {
									for (int i = 0; i < salesOrgList.length; i++) {
							%>

							<option value="<%=salesOrgList[i]%>"><%=salesOrgList[i]%></option>
							<%
								}
								}
							%>
						</select>
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
		<form action="deleteSalesOrg" id="deleteSalesOrg" method="post">
			<div id="main_content">
				<table style="width: 450px;">
					<tr>
						<th><b>Sales Organization</b></th>
						<th> <b>Business Unit</b></th>
						<th> <b>Actions</b></th>
					</tr>
					<%
                    BU objBU = new BU();
                    if (mappedsalesOrgBUList != null) {
                        for (int i=0; i<mappedsalesOrgBUList.length; i++) {   
                       
                        	 objBU = mappedsalesOrgBUList[i];
                                                                                
                    %>
                    
					<tr>
						<td ><b><%=objBU.getSalesOrg()%></b></td>
						<td ><b><%=objBU.getMappedBU()%></b></td>

						<td width="25%"><input type="hidden" id="delSalesOrg"
							name="delSalesOrg" value=""><div
									style="text-align: center;">
									<button onclick="deleteSelectedSalesOrg('<%=objBU.getSalesOrg()%>')">Delete</button>
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