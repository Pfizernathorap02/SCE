<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.utils.SCEUtils"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<%@include file="IAM_User_Auth.jsp" %>
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
   
    
      
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/admin_updateUser.css" rel="stylesheet" type="text/css" media="all" />
     
<title>Insert title here</title>

<%
	String email1 =	(String)request.getAttribute("email1");
	String email2 = (String)request.getAttribute("email2");
	String email3 = (String)request.getAttribute("email3");
	String message = (String)request.getAttribute("message");
%>


<script type="text/javascript">
	
	
    
	function validateUserForm()
            {
				var valid = true;	
			
            	if (document.getElementById("email1").value == null || document.getElementById("email1").value =="" || document.getElementById("email1").value =="null") 
            	{
            		alert("Please enter Approver1 email Id.");
            		
            		valid= false;
            	}
            	else if (document.getElementById("email2").value == null || document.getElementById("email2").value =="" || document.getElementById("email2").value =="null") 
            	{
            		alert("Please enter Approver2 email Id.");
            		
            		valid= false;
            	}
            	else if (document.getElementById("email3").value == null || document.getElementById("email3").value =="" || document.getElementById("email3").value =="null") 
            	{
            		alert("Please enter Approver3 email Id.");
            		
            		valid= false;
            	}
            
                if(valid == true)
                    {
                        alert("confirm");
                       
                        window.document.forms[0].submit();      
        				
                    }
                   
                
               
            }
            
           
            
            </script>

</head>


<body>


	<div id="wrap">
        
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
                
             
            
            </div>
            
            <div id="eval_sub_nav">
               <s:a action="admin" ><img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoadmin.gif" alt="Back to main admin" width="119" height="18" /></s:a>
            </div>
            
            <h3>Approvers</h3>
            <div id="main_content">
                <form action="updateApprovers" id="updateApprovers" method="post">

                <fieldset>
                
                <% if(message != null) {%>
                	
                	<div>
                	<label><font color="Red"><%=message %></font></label>     
                	</div>
                	
                <%}%>
					
					<div>			
                        <label>Approver1:<font color="Red">*</font></label>                        
                        <input type="text" name="email1" style="width: 200px;"  value="<%= email1%>"/>                                              
                    </div>
                    
                    <div >			
                        <label>Approver2:<font color="Red">*</font></label> 
                        <input type="text" name="email2"  style="width: 200px;" value="<%= email2%>"/>  
                                               
                    </div>
                    
                    <div>			
                        <label>Approver3:<font color="Red">*</font></label>                    
                        <input type="text" name="email3" style="width: 200px;" value="<%= email3%>"/>                         
                    </div>
                    
                   
                 
                    
                    <div class="add_buttons">
                      
                            <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save.gif" alt="Add" width="30" height="19" onclick="validateUserForm()"/>
                      
                    </div>
            
                    
                </fieldset>
                
                </form>
                
                
                <div class="clear"></div>
                
            </div> <!-- end #content -->
        
        </div><!-- end #wrap -->



</body>
</html>