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
			
            	if (document.getElementById("lastName").value == null || document.getElementById("lastName").value =="" ) 
            	{
            		alert("Please enter your last name.");
            		
            		valid= false;
            	}
            	else if (document.getElementById("firstName").value == null || document.getElementById("firstName").value =="" ) 
            	{
            		alert("Please enter your first name.");
            		
            		valid= false;
            	}
            	else if (document.getElementById("email").value == null || document.getElementById("email").value =="" ) 
            	{
            		alert("Please enter your email.");
            		
            		valid= false;
            	}
            	else if (document.getElementById("ntId").value == null || document.getElementById("ntId").value =="" ) 
            	{
            		alert("Please enter your NTID.");
            		
            		valid= false;
            	}
            	else if (document.getElementById("ntDomain").value == null || document.getElementById("ntDomain").value =="" ) 
            	{
            		alert("Please enter your NtDomain.");
            		
            		valid= false;
            	}

                
                if(valid == true)
                    {
                       // alert("working");
                       
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
            <h3>Request Access</h3>
            <div id="main_content">
                <s:form action="requestAccess" Id="requestAccess" method="POST">

                <fieldset>
					
					<div>			
                        <label>Last Name:<font color="Red">*</font></label>                        
                        <s:textfield name="lastName" id="lastName"></s:textfield>                                              
                    </div>
                    
                    <div>			
                        <label>First Name:<font color="Red">*</font></label> 
                        <s:textfield name="firstName" id="firstName"></s:textfield>                        
                    </div>
                    
                    <div>			
                        <label>Email:<font color="Red">*</font></label>                    
                        <s:textfield name="email" id="email"></s:textfield>                         
                    </div>
                    
                    <div>			
                        <label>Emplid:</label>                     
                        <s:textfield name="emplId" id="emplId"></s:textfield>                        
                    </div>
                    
                    <div>			
                        <label>NT ID:<font color="Red">*</font></label>
                                               
                        <s:textfield name="ntid" id="ntid" ></s:textfield>
                                           
                    </div>
                    
                    <div>			
                        <label>NT Domain:<font color="Red">*</font></label>
                                                
                         <s:textfield name="ntdomain" id="ntdomain" ></s:textfield> 
                                             
                    </div>
                    
                  <%--   <div style="display:none">
                    	<label>Link:<font color="Red">*</font></label>
                                                
                         <s:textfield name="ntdomain" id="ntdomain" value=""></s:textfield> 
                    
                    </div> --%>
                    
                 
                    
                    <div class="add_buttons">
                      
                            <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_submit.gif" alt="Add" width="47" height="19" onclick="validateUserForm()"/>
                      
                    </div>
            
                    
                </fieldset>
                </s:form>
                
                
                <div class="clear"></div>
                
            </div> <!-- end #content -->
        
        </div><!-- end #wrap -->



</body>
</html>