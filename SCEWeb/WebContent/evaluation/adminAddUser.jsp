<%-- 
  JSP page has been updated  by MUZEES for PBg and Upjohn BU segregation 
  --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@page import="com.pfizer.sce.db.SCEManagerImpl"%>

<%@ taglib prefix="s" uri="/struts-tags"%>


<%@include file="IAM_User_Auth.jsp" %>
<%
    int userFlag=0;
    boolean editUser = false;
    String actionStatus = "";
    String expirationDate = "";
    
    if (SCEUtils.isFieldNotNullAndComplete((String)request.getAttribute("actionStatus"))) {  
        if(request.getAttribute("actionStatus").equals("refresh")){
            actionStatus = "editUser";
            editUser = true;
        }
    }
    
    if (SCEUtils.isFieldNotNullAndComplete((String)request.getAttribute("addExpirationDate"))){
        expirationDate = (String)request.getAttribute("addExpirationDate");
    }
%>



<style type="text/css">@import url(<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar-win2k-cold-2.css);</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar.js"></script>        
<script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/lang/calendar-en.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar-setup.js"></script>        
<script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/DateValidator.js"></script>   

<%
	String[] buList = null;
	SCEManagerImpl sceManager = new SCEManagerImpl();	
%>
     
<script language="javascript">
	function start(){
		fetchBUList();
	    getSelectedItem();
	    
	}
	function fetchBUList(){
		<%buList = sceManager.getAllBUList();%>
    	var sel_bu_index=document.getElementById('sel_bu_id').value;
        document.getElementById("businessUnit").options[sel_bu_index].selected = true;//to set existing business unit if the user is exist and inactive
	}
    function validateUserForm() {
    	
        var ntid = document.getElementById('ntid').value;
        var ntdomain = document.getElementById('ntdomain').value;
        var expirationDate = document.getElementById('expirationDate').value;
        var userGroup = document.getElementById('userGroup').value;
        var businessUnit = document.getElementById('businessUnit').value;//added by muzees for PBG and UpJOHN
        var arrDate= expirationDate.split("/");
        var useDate = new Date(arrDate[2], arrDate[0]-1, arrDate[1]);
        var today = new Date();
       // alert(businessUnit);
        // RUPINDER ADDED 8 DEC: STARTS
          // var time= today.getTime();
          // var hours= today.getHours();
          // alert(hours);
             today.setHours(00);
          // var Seconds= today.getSeconds();
          // alert(Seconds)
             today.setMinutes(00);
             today.setSeconds(00);
          // var miliseconds=today.getMilliseconds();
          // alert(miliseconds);
             today.setMilliseconds(00);
          // alert("today new is:::"+today)
          // ENDS
        
        if (ntid == '') {
            alert('NT ID is a mandatory field');
            return false;
        } 
        
        if (ntdomain == '') {
            alert('NT Domain is a mandatory field');
            return false;
        }
        
        if(userGroup=='SCE_GuestTrainer_NonMGR'&& expirationDate == ''){
            alert('Expiration Date is a mandatory field');
            return false;
        }
        if (userGroup=='SCE_GuestTrainer_NonMGR'&& businessUnit == "" ) {
            alert('Please select Business Unit');
            return false;
        } 
        
        //if (useDate < today){
            //alert("The Expiration Date cannot be a past date. Please provide a future Date");
            //return false;
      // }
        
        var validformat=/^\d{2}\/\d{2}\/\d{4}$/ //Basic check for format validity
        if (expirationDate!=''){
        if (!validformat.test(expirationDate)){
        alert("Invalid Date Format. Please correct and submit again.");
        return false;
        }
        if (useDate < today){
            alert("The Expiration Date cannot be a past date. Please provide a future Date");
            return false;
            }
        else{ //Detailed check for valid date ranges
        var monthfield=expirationDate.split("/")[0];
        var dayfield=expirationDate.split("/")[1];
        var yearfield=expirationDate.split("/")[2];
        var dayobj = new Date(yearfield, monthfield-1, dayfield);
        if ((dayobj.getMonth()+1!=monthfield)||(dayobj.getDate()!=dayfield)||(dayobj.getFullYear()!=yearfield)){
            alert("Invalid Day, Month, or Year range detected. Please enter a valid date in mm/dd/yyyy format.");
            return false;
        }
        }
       } 
        window.document.forms[0].submit();                       
    }
    
    
    function getSelectedItem() {
    var userGroup = document.getElementById('userGroup').value;
    document.forms[0].businessUnit.disabled = true;
	   if(userGroup!='SCE_GuestTrainer_NonMGR'&& userGroup!='SCE_OpsManager'){
		   document.getElementById('businessUnit').value='';
	    }
    if(userGroup=='SCE_GuestTrainer_NonMGR'|| userGroup=='SCE_OpsManager'){
    	document.forms[0].businessUnit.disabled = false;
    }
    if(userGroup=='SCE_GuestTrainer_NonMGR'){
        document.getElementById('expDtMandatory').style.visibility='visible';
        document.getElementById('buMandatory').style.visibility='visible';
        }
    else{
        document.getElementById('expDtMandatory').style.visibility='hidden';
        document.getElementById('buMandatory').style.visibility='hidden';
        }
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
   
     <!-- changed the css class from admin.css to admin_updateUser.css for correcting the calendar format in Add user page  -->
      
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/admin_updateUser.css" rel="stylesheet" type="text/css" media="all" />
        <!--[if IE 6]>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" />
        <![endif]-->
    </head>
    <body id="p_add_user" class="admin" onLoad="start()">
        <div id="wrap">
        
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
                
                <%@include file="navbar.jsp" %>
                <!-- end #top_head -->
            </div>
            <div id="eval_sub_nav">
               <s:a action="admin" ><img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoadmin.gif" alt="Back to main admin" width="119" height="18" /></s:a>
            </div>
            
            <h3>Admin: Add New User</h3>
            
            <div id="main_content">
                <s:form action="addUser" Id="addUserForm">
                	<s:hidden name="sel_bu_id" id="sel_bu_id"></s:hidden>
                	<s:hidden name="selUserType" id="selUserType" value="%{selUserType}" />
                	<s:hidden name="selUserStatus" id="selUserStatus" value="%{selUserStatus}" />
<%--                 <font color="red"><s:label name="message" id="message"></s:label> </font> --%>
                <fieldset>
					<div>
						<font color="red">
						<s:label name="message" id="message"></s:label> </font>
					</div>
					<div>			
                        <label>Last Name:</label>                        
                        <s:textfield name="lastName" id="lastName"></s:textfield>                                              
                    </div>
                    
                    <div>			
                        <label>First Name:</label> 
                        <s:textfield name="firstName" id="firstName"></s:textfield>                        
                    </div>
                    
                    <div>			
                        <label>Email:</label>                    
                        <s:textfield name="email" id="email"></s:textfield>                         
                    </div>
                    
                    <div>			
                        <label>Emplid:</label>                     
                        <s:textfield name="emplId" id="emplId"></s:textfield>                        
                    </div>
                    
                    <div>			
                        <label>NT ID:<font color="Red">*</font></label>
                        <%if(editUser){%>                        
                         <s:textfield name="ntid" id="ntid" disabled="true"></s:textfield>                    
                         <s:hidden name="ntid" id="ntid"></s:hidden>                        
                        <%}else{%>                        
                        <s:textfield name="ntid" id="ntid" ></s:textfield>
                        <%}%>                     
                    </div>
                    
                    <div>			
                        <label>NT Domain:<font color="Red">*</font></label>
                        <%if(editUser){%>                      
                         <s:textfield name="ntdomain" id="ntdomain" disabled="true"></s:textfield>
                         <s:hidden name="ntdomain" id="ntdomain"></s:hidden>
                         
                          
                         <%}else{%>                         
                         <s:textfield name="ntdomain" id="ntdomain" ></s:textfield> 
                          <%}%>                      
                    </div>
                    
                    <div style="display: none">
                    	<label>isAccessRequest</label>
                    	<s:textfield name="isAccessRequest" id="isAccessRequest" ></s:textfield>
                    </div>
                    
                    
                    <div>			
                        <label>User Type:</label>                       
                       <s:select list="%{userGroups}" name="userGroup"  id="userGroup"  onchange="getSelectedItem()"></s:select>
                    </div>
                    
                    <div>
						<label>Business Unit:<font id="buMandatory" color="Red">*</font></label> <select id="businessUnit" name="businessUnit" disabled>
							<option value="">---Select---</option>

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
                        <label>Status:</label>
                        <s:select list = "%{statuses}" name="status" id="status"></s:select>
                    </div>
                    
                    <div>			
                        <label>Expiration Date:<font id="expDtMandatory" color="Red">*</font></label>
                        <INPUT type="text" size="11" maxlength="10" value="<%=expirationDate%>" name="expirationDate" id="expirationDate" onKeyUp="DateFormat('expirationDate',event,false,'1','Expiration Date ')">
                        
                        <IMG id="calImg_trainingDate" src="<%=request.getContextPath()%>/evaluation/resources/images/icon_calendar.gif" border="0"  name="as_of_date_UP">               
                        
                    <script type="text/javascript">
                                Calendar.setup({
                                    inputField     :    "expirationDate",
                                    ifFormat       :    "%m/%d/%Y",
                                    button         :    "calImg_trainingDate",
                                    align          :    "Tl"                            
                                });
                            </script>
                            
                        <input type="hidden" name="actionStatus" value= "<%=actionStatus%>"> 
                    
                    </div>
                    
                    <div class="add_buttons">
                        <%if(editUser){%>
                            <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save.gif" alt="Save" width="38" height="19" onclick="validateUserForm()"/>
                        <%}else{%>
                            <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_add.gif" alt="Add" width="27" height="19" onclick="validateUserForm()"/>
                        <%}%> 
                    </div>
                </fieldset>
                </s:form>
                
                
                <div class="clear"></div>
            </div> <!-- end #content -->
        
        </div><!-- end #wrap -->
    
    </body>

</html>
