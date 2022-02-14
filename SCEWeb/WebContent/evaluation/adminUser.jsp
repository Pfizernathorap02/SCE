<%-- 
  JSP page has been updated  by MUZEES for PBg and Upjohn BU segregation 
  --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", -1); 
%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.beans.User"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@include file="IAM_User_Auth.jsp"%>

<%
    String message = (String)request.getAttribute("message");
    User[] users = (User[])request.getAttribute("users");
	HashMap userGroups = SCEUtils.getUserGroupsMapwithGTMGR();
	HashMap statuses = SCEUtils.getStatuses();
%>

<script language="javascript">
    function editUser(userId) { 	
        window.document.getElementById('selUserId').value = userId ;
        window.document.forms[0].action="gotoUpdateUser";                
        window.document.forms[0].submit();        
    }
    /*  2020 Q4:start of MUZEES to add full name in confirmation pop-up while removing user  */
    function removeUser(userId,firstName,lastName) {
        if (confirm('Are you sure you want to remove '+firstName+' '+lastName+'?')) {
            window.document.getElementById('selUserId').value = userId;
            window.document.getElementById('selUserFirstName').value = firstName;
            window.document.getElementById('selUserLastName').value = lastName;
            window.document.forms[0].action="removeUser";                
            window.document.forms[0].submit();            
        }
    }
    /* end of muzees */
      function onScreenLoad() {
    	  var selUserStatus=window.document.getElementById('selUserStatus').value;
    	    var selUserGroup=window.document.getElementById('selUserType').value;
    	    window.document.getElementById("status").value=selUserStatus;
    	    window.document.getElementById("userGroup").value=selUserGroup;
    	  if (document.all){
              document.all["layer1"].style.visibility="hidden";
              document.all["layer2"].style.visibility="visible";
          } else if (document.getElementById){
              node = document.getElementById("layer1").style.visibility='hidden';
              node = document.getElementById("layer2").style.visibility='visible';
          }
    } 
    function selectStatus() {
    
        var statusValue = document.getElementById('status').value;
        var userGroupValue = document.getElementById('userGroup').value; 
        window.document.getElementById('selUserStatus').value = statusValue;
        window.document.getElementById('selUserType').value = userGroupValue;
        
        if (statusValue != '' && userGroupValue!='') {
            window.document.forms[0].action = "selectUserStatus"; 
            window.document.forms[0].submit();
        	
        }
        else {
        	//alert('in else status value');
        	return false;
        }
    } 
    
    /* function disable(){
        document.getElementById('remove').disabled=true;       
        return false;
    } 2020 Q4:commented by muzees*/   
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
<!-- Sorting begin sanjeev-->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/js/sorttable.js"></script>
<!-- Sorting end sanjeev-->
<style media="screen" type="text/css">
      .layer1_class { visibility: visible; }
      .layer2_class { visibility: hidden; }
    </style>
</head>
<body id="p_admin_users" class="admin" onLoad="onScreenLoad()">
	<div id="wrap">

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

		<h3>Admin: Users</h3>

		<div id="main_content">
			<%
                if (message != null && !"".equals(message.trim())) {
                %>
			<font color="red"><%=message%></font>
			<%
                }
                %>
			<div class="add_user">
			<s:url action="gotoAddUser.action" var="addUser" escapeAmp="false">
												<s:param name="selUserType">
													<s:property value="selUserType" />
												</s:param>
												<s:param name="selUserStatus">
													<s:property value="selUserStatus" />
												</s:param>
											</s:url>
											<a href="<s:property value="#addUser" />" ><img
						src="<%=request.getContextPath()%>/evaluation/resources/_img/button_add_user.gif"
						alt="Add New User" width="85" height="18" /></a>
			</div>
			<s:form action="gotoUserAdmin" tagId="allUsersForm">
                        User Status : <s:select list="%{filterStatuses}"
					name="status" onchange="return selectStatus()" id="status"></s:select>
                        &nbsp
                        &nbsp
                        &nbsp
                        &nbsp
                        User Type : <s:select list="%{filterUserGroups}"
					name="userGroup" onchange="return selectStatus()" id="userGroup"></s:select>

				<%-- <s:select list="#{'1':'All', '2':'Active', '3':'Inactive'}" name="status" onchange="return selectStatus()" id="status" value="2"></s:select> --%>

				<s:hidden name="selUserId" id="selUserId" />
				<s:hidden name="selUserStatus" id="selUserStatus" />
				<s:hidden name="selUserType" id="selUserType" />
				<s:hidden name="selUserFirstName" id="selUserFirstName" />
				<s:hidden name="selUserLastName" id="selUserLastName" />
				<div id="layer1" class="layer1_class">
					<table width="100%" border="0">
						<tr align="center" border="0" >
							<th align="center" border="0"><strong><em>Loading data. Please wait...</em></strong>
							</th>
						</tr>
					</table>
				</div>
				<div id="layer2" class="layer2_class">
					<table cellspacing="0" class="sortable" id="unique_id">
						<tr>

							<!-- Sorting begin sanjeev -->

							<!-- <th class="sort_up" onclick="ts_resortTable(this, 0);return false;">Last Name</th>
                    <th class="sort_up" onclick="ts_resortTable(this, 1);return false;">First Name</th>
                       -->
							<th class="sort_up"
								onclick="ts_resortTable(this, 0);return false;">Last Name</th>
							<th class="sort_up"
								onclick="ts_resortTable(this, 1);return false;">First Name</th>

							<!-- Sorting end sanjeev -->
							<th id="email_length">Email</th>
							<th>Emplid</th>
							<th>NT ID</th>
							<th>NT Domain</th>
							<th>User Type</th>
							<th>Business Unit</th>
							<th id="status_length">Status</th>
							<th class="last last_length"></th>
						</tr>
						<%
                    User objUser = null;
                    if (users != null) {
                        for (int i=0; i<users.length; i++) {   
                            objUser = users[i];                                                      
                    %>
						<tr>
							<td style="word-wrap: break-word;"><%=SCEUtils.ifNull(objUser.getLastName(),"&nbsp;")%></td>
							<td style="word-wrap: break-word;"><%=SCEUtils.ifNull(objUser.getFirstName(),"&nbsp;")%></td>
							<td style="word-wrap: break-word;"><%=SCEUtils.ifNull(objUser.getEmail(),"&nbsp;")%></td>
							<td><%=SCEUtils.ifNull(objUser.getEmplId(),"&nbsp;")%></td>
							<td style="word-wrap: break-word;"><%=SCEUtils.ifNull(objUser.getNtid(),"&nbsp;")%></td>
							<td><%=SCEUtils.ifNull(objUser.getNtdomain(),"&nbsp;")%></td>
							<td style="word-wrap: break-word;"><%=SCEUtils.ifNull(userGroups.get(objUser.getUserGroup()),"&nbsp;")%></td>
							<td style="word-wrap: break-word;"><%=SCEUtils.ifNull(objUser.getBusinessUnit(),"&nbsp;")%></td>
							<td><%=SCEUtils.ifNull(statuses.get(objUser.getStatus()),"&nbsp;")%></td>
							<!-- 2020 Q4 start of MUZEES to enable remove button for admin,Training Team & GT Non-MGR and disable remove button for GT MGR -->
							<td class="last"><img
								src="<%=request.getContextPath()%>/evaluation/resources/_img/button_edit.gif"
								alt="Edit" width="40" height="18"
								onclick="return editUser(<%=objUser.getId()%>)" /> 
								<%if(userGroups.get(objUser.getUserGroup())!="Guest Trainer MGR"){ %><img
								src="<%=request.getContextPath()%>/evaluation/resources/_img/button_remove.gif"
								id="remove" alt="Remove" width="56" height="18"
								onclick="removeUser(<%=objUser.getId()%>,'<%=objUser.getFirstName()%>','<%=objUser.getLastName()%>');" />
								<%}else{%>
								<input type="button" value="Remove" disabled style="border-radius: 4px; width: 56px; height: 18px; font-size: 11px;border: none;"/>
								<%} %>
								</td>
								<!-- End of MUZEES -->
						</tr>
						<%
                        }
                    }
                    %>

					</table>
				</div>
			</s:form>

			<div class="clear"></div>
		</div>
		<!-- end #content -->

	</div>
	<!-- end #wrap -->

</body>
<!--[if IE]>
<head> 
    <meta http-equiv="pragma" content="no-cache"/> 
    <meta http-equiv="Expires" content="-1"/> 
</head> 
<![endif]-->
</html>
<%
    request.removeAttribute("message");
    request.removeAttribute("users");
%>