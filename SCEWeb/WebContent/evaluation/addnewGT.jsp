<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp" %>
<%
 String message=null;
    if(request.getAttribute("message")!=null){
        message=(String)request.getAttribute("message");
    }%>
<style type="text/css">@import url(<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar-win2k-cold-2.css);</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar.js"></script>        
<script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/lang/calendar-en.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar-setup.js"></script>        


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
        <script language="javascript">
        function validateInput()
        {
        //alert("in validate");
        var fname=document.getElementById('repFname').value;
        var lname=document.getElementById('repLname').value;
        var email=document.getElementById('repEmail').value;
        var location=document.getElementById('repLocation').value;
        var product=document.getElementById('repProduct').value;
        var role=document.getElementById('repRole').value;
        var manager=document.getElementById('repManager').value;
        var managerEmail=document.getElementById('repManagerEmail').value;
        var managerRole=document.getElementById('repManagerRole').value;
        var ntid=document.getElementById('repNTID').value;
  
        name=name.replace(/^\s+|\s+$/gm,'');
        email=email.replace(/^\s+|\s+$/gm,'');
        location=location.replace(/^\s+|\s+$/gm,'');
       // product=product.replace(/^\s+|\s+$/gm,'');
        role=role.replace(/^\s+|\s+$/gm,'');
        //manager=manager.replace(/^\s+|\s+$/gm,'');
        managerRole=managerRole.replace(/^\s+|\s+$/gm,'');
        managerEmail=managerEmail.replace(/^\s+|\s+$/gm,'');
        ntid=ntid.replace(/^\s+|\s+$/gm,'');
     
        document.getElementById("hdnfname").value=fname;
        //alert(document.getElementById("hdnfname").value);
        document.getElementById("hdnlname").value=lname;
        document.getElementById("hdnemail").value=email;
        document.getElementById("hdnlocation").value=location;
        document.getElementById("hdnproduct").value=product;
        document.getElementById("hdnrole").value=role;
        document.getElementById("hdnmanager").value=manager;
        document.getElementById("hdnmanagerEmail").value=managerEmail;
        document.getElementById("hdnmanagerRole").value=managerRole;
        document.getElementById("hdnntid").value=ntid;
        
        var regex=/^[a-zA-Z][a-zA-Z ]*$/; 
        var regexAndDash=/^[a-zA-Z][a-zA-Z- ]*$/; 
        var regexEmail=/^[a-zA-Z0-9@.]*$/;  
        var regexNTID=/^[a-zA-Z0-9]*$/; 
        //var regex=\\w+\\.?;
        if(fname ==''){
        alert("Please Enter First Name");
        return false;
        }else if(!regex.test(fname)){
        alert("Only Alphabets are allowed in First Name.");
        return false;
        }
        else if(lname ==''){
        alert("Please Enter Last Name");
        return false;
        }else if(!regex.test(lname)){
        alert("Only Alphabets are allowed in Last Name.");
        return false;
        }
        else if(!validateEmail(email)){
        return false;
        }else if(!regexEmail.test(email)){
        alert("Please enter valid Email address.");
        return false;
        }
        else if(ntid ==''){
        alert("Please Enter NTID");
        return false;
        }else if(ntid.length>20){
        alert("Please Enter NTID less than 20 characters");
        return false;
        }else if(!regexNTID.test(ntid)){
        alert("Only alphanumeric value is allowed for NTID");
        return false;
        }
        else if(location ==''){
        alert("Please Enter Location");
        return false;
        }
        else if(product.length<=0){
        alert("Please Select Atleast One Product");
        return false;
        }
        else if(role ==''){
        alert("Please Enter Role");
        return false;
        }else if(!regexAndDash.test(role)){
        alert("Only Alphabets are allowed in Role.");
        return false;
        }

        
        /*Sanjeev Remove mandatory field as Manager Name  */
       
           /*   else if(manager ==''){
            alert("Please Enter Manager Name");
            return false;
            } else if(!regex.test(manager)){
            alert("Only Alphabets are allowed in Manager Name.");
            return false;
            }
            else if(!validateEmail(managerEmail)){
            return false;
            }else if(!regexEmail.test(managerEmail)){
            alert("Please enter valid Email address.");
            return false;
            }
            else if(managerRole ==''){
            alert("Please Enter Manager Role");
            return false;
            }
            else if(!regexAndDash.test(managerRole)){
            alert("Only Alphabets are allowed in Manager Role.");
            return false;
            } */
   
        window.document.forms[0].submit(); 
        }
        
        function validateEmail(email)
        {
        if(email ==''){
        alert("Please Enter Email");
        return false;
        }
        else if(email.indexOf("@")<1 || email.indexOf(".")<1 || email.indexOf("@")>email.lastIndexOf(".") || ((email.lastIndexOf("."))-(email.indexOf("@")))<3 || email.lastIndexOf(".")+2>=email.length){
        alert("Not a valid Email address");
        return false;
        }
        return true;
        }
        </script>
        <style>
        table,td{border:0px;}
        .textboxwidth{
        width:150px;
        }
        </style>
    </head>
    <body id="p_add_GT" class="addGT">
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
            
            <h3>Admin: Add New GT</h3>
            
            
            <div id="main_content">
            <div class="add_user">
            <s:a action="gotoUploadGT">Upload List Of Guest Trainers</s:a>
            </div>
            <s:form action="gotoSaveGT">
             <input type="hidden" id="hdnfname" name="hdnfname" value=""/>
             <input type="hidden" id="hdnlname" name="hdnlname" value=""/>
             <input type="hidden" id="hdnemail" name="hdnemail" value=""/>
             <input type="hidden" id="hdnlocation" name="hdnlocation" value=""/>
             <input type="hidden" id="hdnproduct" name="hdnproduct" value=""/>
             <input type="hidden" id="hdnrole" name="hdnrole" value=""/>
             <input type="hidden" id="hdnmanager" name="hdnmanager" value=""/>
             <input type="hidden" id="hdnmanagerEmail" name="hdnmanagerEmail" value=""/>
             <input type="hidden" id="hdnmanagerRole" name="hdnmanagerRole" value=""/>
             <input type="hidden" id="hdnntid" name="hdnntid" value=""/>
                  
         <%if(message!=null){%>
            <font color="Red">
            <p><%=message%></p>
            </font>
            <%}%>
            <%-- <font color="red"><s:label name="message"></s:label></font> --%>
            <table>
            <tr>
            <td width="150">Guest Trainer First Name<font color="Red">*</font></td>
            <td><s:textfield  name="repFname" cssClass="textboxwidth" id="repFname"  /></td>
            </tr>
            <tr>
            <td width="150">Guest Trainer Last Name<font color="Red">*</font></td>
            <td><s:textfield name="repLname" cssClass="textboxwidth" id="repLname"  /></td>
            </tr>
            <tr>
            <td width="150">Guest Trainer Email<font color="Red">*</font></td>
            <td><s:textfield name="repEmail" cssClass="textboxwidth" id="repEmail"/></td>
            </tr>
            <tr>
            <td width="150">Guest Trainer NTID<font color="Red">*</font></td>
            <td><s:textfield name="repNTID" cssClass="textboxwidth" id="repNTID"/></td>
            </tr>
            <tr>
            <td width="150">Guest Trainer Location<font color="Red">*</font></td>
           <td><s:select list="locationList" name="repLocation" styleClass="textboxwidth" id="repLocation"/></td>  
            </tr>
            <tr>
            <td width="150">Associated Product<font color="Red">*</font></td>
            <td><s:select list="productList" name="repProduct" multiple="true" styleClass="textboxwidth"  id="repProduct"/></td> 
            </tr>
            <tr>
            <td width="150">Guest Trainer Role<font color="Red">*</font></td>
            <td><s:textfield name="repRole" cssClass="textboxwidth" id="repRole"/></td>
            </tr>
            <tr>
            <td width="150">Guest Trainer Manager Name<font color="Red"></font></td>
            <td><s:textfield name="repManager" cssClass="textboxwidth" id="repManager"/></td>
            </tr>
            <tr>
            <td width="150">Guest Trainer Manager Email<font color="Red"></font></td>
            <td><s:textfield name="repManagerEmail" cssClass="textboxwidth"  id="repManagerEmail"/></td>
            </tr>
            <tr>
            <td width="150">Guest Trainer Manager Role<font color="Red"></font></td>
            <td><s:textfield name="repManagerRole" cssClass="textboxwidth"  id="repManagerRole"/></td>
            </tr>
            </table>
            <div class="add_buttons">
            <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_add.gif" alt="Add" width="27" height="20" onClick=" return validateInput()" />
            <!--<netui:button value="Save" onClick=" return validateInput()"/>-->
            </div>
        </s:form>                
                
                <div class="clear"><br/>
                Fields marked with <font color="Red">*</font> are mandatory.
                </div>
                
                
                
                
                
            </div> <!-- end #content -->
        
        </div><!-- end #wrap -->
    
    </body>

</html>
