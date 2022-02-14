<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 -->
  <!--  Meta type added for HTML5 Struts 2.3 to 2.5 migration work by : Dhananjay -->   
 <meta charset="utf-8">
 
         <meta http-equiv="Content-Language" content="en-us" />
        <title>Pfizer Sales Call Evaluation</title>
        <meta name="ROBOTS" content="ALL" />
        <meta http-equiv="imagetoolbar" content="no" />
        <meta name="MSSmartTagsPreventParsing" content="true" />
        <meta name="Keywords" content="_KEYWORDS_" />
        <meta name="Description" content="_DESCRIPTION_" />
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css" rel="stylesheet" type="text/css" media="all" />
   
    
      
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/admin_updateUser.css" rel="stylesheet" type="text/css" media="all" />
          
          
    <script>

	function CloseMe() 
	{
		window.open('javascript:window.open("", "_self", "");window.close();', '_self');
	}
	
	

    function getSelectedItem() {
    var value = document.getElementById('reminder').value;
    if(value=='SCE_GuestTrainer_NonMGR'){
        document.getElementById('expDtMandatory').style.visibility='visible';
        }
    else{
        document.getElementById('expDtMandatory').style.visibility='hidden';
        }
    }
    
    function check() {
    	
    	document.getElementById("reminder").disabled = true;
		document.getElementById("reminder").checked = false;
    }
	
	</script> 
	
	
	<style type="text/css">
	button {
     background:none!important;
     border:none; 
     padding:0!important;
     font: inherit;
     color:olive;
     cursor: pointer;
     align: center;
		}

</style>

<title>Success</title>


 <%
 	String email = (String)request.getAttribute("email");
 		System.out.println("email 1 :"+email);
	String ntid = (String)request.getAttribute("ntid");
		System.out.println("ntid 1 :"+ntid);
	String firstname = (String)request.getAttribute("fname");
		System.out.println("firstname :"+firstname);
	
	String lastname = (String)request.getAttribute("lname");
		System.out.println("last name :"+lastname);
	String ntdomain = (String)request.getAttribute("ntdomain");
	String  emplid = (String)request.getAttribute("emplid");
	
 %>

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



</head>
<body onload="check()">

<div id="wrap">
        
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
                
             
            
            </div>
     <h4>Hi <%=firstname %>!  your request has already been recorded. You will get a confirmation mail shortly.</h4>
     
     <div class="below" style="border:none; background-color:white">
      <table border="0">
     	<tr>
     		<td>
     		<h4><a onClick="CloseMe();" style="cursor: pointer; cursor: hand;"><font color="Red">Close</font></a></h4>
     		</td>
     		<td>
     			<form action="reminderMail" Id="reminderMail" method="post">
     		
     		<table>
     		<tr>
     			<td>
     			<div style="display: none">
     				<input type="text" name="email" value="<%= email%>">
     			</div>
     			
     			<div style="display: none">
     				<input type="text" name="ntid" value="<%= ntid %>">
     			</div>
     			
     			<div style="display: none">
     				<input type="text" name="firstname" value="<%= firstname %>">
     			</div>
     			
     			<div style="display: none">
     				<input type="text" name="lastname" value="<%= lastname %>">
     			</div>
     			
     			<div style="display: none">
     				<input type="text" name="ntdomain" value="<%= ntdomain %>">
     			</div>
     			
     			<div style="display: none">
     				<input type="text" name="emplid" value="<%= emplid %>">
     			</div>
     			<div>
               	<label>Comments:<font color="Red">*</font></label>
             	  <s:textarea  name="comments" id="comments" ></s:textarea>                       
            	</div> 
     			</td>
     		</tr>
     		
     		</table>
     		
     	
     		
     		
     		
                    
     		
			<button onclick="validateUserForm()">Reminder Email</button>                                             
     		 <form>
     		</td>
     	</tr>
     
     </table>
     </div>
     
    
     
     
     
          
          
</div>            

</body>
</html>