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

<%@include file="IAM_User_Auth.jsp" %>

<%
    String message = (String)request.getAttribute("message");
    User[] users = (User[])request.getAttribute("users");
	HashMap userGroups = SCEUtils.getUserGroupsMap();
	HashMap statuses = SCEUtils.getStatuses();
%>

<script language="javascript">
    function editUser(userId) { 	
    	
        window.document.getElementById('selUserId').value = userId ;
        //alert(window.document.getElementById('selUserId').value);
        window.document.forms[0].action="gotoUpdateUser";                
        window.document.forms[0].submit();        
    }
    
    function removeUser(userId) {
        if (confirm('Are you sure you want to remove this user?')) {
            window.document.getElementById('selUserId').value = userId;
            window.document.forms[0].action="removeUser";                
            window.document.forms[0].submit();            
        }
    }
    
    function selectStatus() {
    
        var statusValue = document.getElementById('status').value;
        
       // alert(statusValue)
        
        window.document.getElementById('selUserStatus').value = statusValue;
        if (statusValue != '') {
        	//alert('in if status value');
            window.document.forms[0].action = "selectUserStatus"; 
            window.document.forms[0].submit();
        	
        }
        else {
        	//alert('in else status value');
        	return false;
        }
    } 
    
    function disable(){
        document.getElementById('remove').disabled=true;       
        return false;
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
        <!-- Sorting begin sanjeev-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/sorttable.js"></script>        
        <!-- Sorting end sanjeev-->
    </head>
    <body id="p_admin_users" class="admin">
        <div id="wrap">
        
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
                
                <%@include file="navbar.jsp" %>
                <!-- end #top_head -->
            </div>
        
            <div id="eval_sub_nav">
                <s:a action="admin"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoadmin.gif" alt="Back to main admin" width="119" height="18" /></s:a>
            </div>
        
            <h3>Admin: Users</h3>
        
            <div id="main_content">
                <%
                if (message != null && !"".equals(message.trim())) {
                %>
                <%=message%>
                <%
                }
                %>
                <div class="add_user">                      
                 <s:a action="gotoAddUser" ><img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_add_user.gif" alt="Add New User" width="85" height="18" /></s:a>
                </div>
                <s:form action="gotoUserAdmin" tagId="allUsersForm" >
                        User Status : <s:select list="%{filterStatuses}" name="status" onchange="return selectStatus()" id="status"></s:select>
                        
                        <%-- <s:select list="#{'1':'All', '2':'Active', '3':'Inactive'}" name="status" onchange="return selectStatus()" id="status" value="2"></s:select> --%>                                        
                
                <s:hidden name="selUserId" id="selUserId"/>
                <s:hidden name="selUserStatus" id="selUserStatus"/>
                
                <table cellspacing="0" class="sortable" id="unique_id">
                    <tr>
                    
                    <!-- Sorting begin sanjeev -->
                  
                  <!-- <th class="sort_up" onclick="ts_resortTable(this, 0);return false;">Last Name</th>
                    <th class="sort_up" onclick="ts_resortTable(this, 1);return false;">First Name</th>
                       --> 
                        <th class="sort_up" onclick="ts_resortTable(this, 0);return false;">Last Name</th>
                        <th class="sort_up" onclick="ts_resortTable(this, 1);return false;">First Name</th>
                     
                       <!-- Sorting end sanjeev -->
                        <th id="email_length">Email</th>
                        <th>Emplid</th>
                        <th>NT ID</th>
                        <th>NT Domain</th>
                        <th>User Type</th>
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
                        <td><%=SCEUtils.ifNull(statuses.get(objUser.getStatus()),"&nbsp;")%></td>
                        <td class="last">
                            <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_edit.gif" alt="Edit" width="40" height="18" onclick="return editUser(<%=objUser.getId()%>)"/>
                            <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_remove.gif" id="remove" alt="Remove" width="56" height="18" onclick="disable();"/>
                           
                        </td>
                    </tr>
                    <%
                        }
                    }
                    %>
                
                </table>
                </s:form>
                
                <div class="clear"></div>	
            </div> <!-- end #content -->
        
        </div><!-- end #wrap -->

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