<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.utils.SCEUtils"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

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
     
<script type="text/javascript">
	
	
    
	function validateUserForm()
            {
				var valid = true;	
			
            	if (document.getElementById("comments").value == null || document.getElementById("comments").value =="" ) 
            	{
            		alert("Please enter your comments.");
            		
            		valid= false;
            	}
            
                if(valid == true)
                    {
                        alert("working");
                       
                        window.document.forms[0].submit();      
        				
                    }
                   
                
               
            }
            
           
            
            </script>
            
            
            
             <%
             
            System.out.println("request query string"+request.getQueryString()); 
            String lastName = request.getParameter("lastName");
            
            System.out.println("lastname:   "+lastName); 
        	String firstName = request.getParameter("firstName");
        	String email = request.getParameter("email");
        	String emplId = request.getParameter("emplId");
        	String ntid = request.getParameter("ntid");
        	String ntdomain = request.getParameter("ntdomain");
            
            %>

</head>

<body>


	<div id="wrap">
        
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
                
             
            
            </div>
            
           
            
            <h3>Reject Access</h3>
            <div id="main_content">
                <form action="rejectAccess" id="rejectAccess" method="post">

                <fieldset>
					
					<div style="display:none">			
                        <label>Last Name:<font color="Red">*</font></label>                        
                        <input type="text" name="lastname" value="<%= lastName%>"/>                                              
                    </div>
                    
                    <div style="display:none">			
                        <label>First Name:<font color="Red">*</font></label> 
                        <input type="text" name="firstName" value="<%= firstName%>"/>  
                                               
                    </div>
                    
                    <div style="display:none">			
                        <label>Email:<font color="Red">*</font></label>                    
                        <input type="text" name="email" value="<%= email%>"/>                         
                    </div>
                    
                    <div style="display:none">			
                        <label>Emplid:<font color="Red">*</font></label>                     
                       
                        <input type="text" name="emplId" value="<%= emplId%>"/>                         
                    </div>
                    
                    <div style="display:none">			
                        <label>NT ID:<font color="Red">*</font></label>
                                               
                        <input type="text" name="ntid" value="<%= ntid%>"/> 
                                           
                    </div>
                    
                    <div style="display:none">			
                        <label>NT Domain:<font color="Red">*</font></label>
                                                
                         <input type="text" name="ntdomain" value="<%= ntdomain%>"/> 
                                             
                    </div>
                    
                   <!-- style="display:none"  -->
                   	<div>
                    	<label>Comments:<font color="Red">*</font></label>
                          
                         <s:textarea  name="comments" id="comments" ></s:textarea>                       
                         
                    
                    </div> 
                    
                 
                    
                    <div class="add_buttons">
                      
                            <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_finish.gif" alt="Add" width="47" height="19" onclick="validateUserForm()"/>
                      
                    </div>
            
                    
                </fieldset>
                
                </form>
                
                
                <div class="clear"></div>
                
            </div> <!-- end #content -->
        
        </div><!-- end #wrap -->



</body>
</html>