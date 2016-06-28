<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.utils.SCEUtils"%>

<%@ page import="com.pfizer.sce.beans.Attendee"%>
<%@ page import="com.pfizer.sce.beans.EventCourseProductMapping"%>

<%@ page import="com.pfizer.sce.beans.GuestTrainer"%>
<%@include file="IAM_User_Auth.jsp"%>


<%

//   <!-- code change begin for adding check box for accepting invitation by admin--->
User temp_user = (User)session.getAttribute("user");
String userGrp = temp_user.getUserGroup();
System.out.println(userGrp);
String opsMgrAccess = "N"; 
if(userGrp == null)
{
} 
else if(userGrp.equalsIgnoreCase("SCE_OpsManager") || userGrp.equalsIgnoreCase("SCE_Administrators")) 
{
  opsMgrAccess = "Y";
} 
//<!-- code change end for adding check box for accepting invitation by admin--->
SCEManagerImpl sceManagerImpl = new SCEManagerImpl()/* (SCEManagerImpl)pageContext.getAttribute("SCEManager") */;
EventCourseProductMapping mapping = null;
 String alreadySelectedEvent = ""; 
String alreadySelectedProduct ="";
String[] eventNameFetch = sceManagerImpl.getEventName();


GuestTrainer[] trainers=(GuestTrainer[])request.getAttribute("GTListByProduct");
alreadySelectedEvent =(String) request.getAttribute("alreadySelectedEvent");
alreadySelectedProduct =(String)request.getAttribute("alreadySelectedProduct");
String[] eventProducts;
if(alreadySelectedEvent != null && !alreadySelectedEvent.equals("") && request.getAttribute("eventProducts") == null)
{
	eventProducts=sceManagerImpl.getEventProducts((String)request.getAttribute("alreadySelectedEvent_1"));
}
else
eventProducts= (String[])request.getAttribute("eventProducts");

if(eventProducts!=null)
System.out.println("eventProducts: "+eventProducts.length);

  // mapping = sceManager.getMappingForEvent();
    %>

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
        
        <script language="javascript">
        var tempVal = '<%=request.getContextPath()%>';
var emailList="";
var reminderEmailList = "";
var acceptGTemailList="";


function viewTrainerList()
{
  var index = document.getElementById("eventValue").value;
  var eventName = document.getElementById("eventValue").options[index].innerHTML;
  
  if(eventName == "---Select---" )
 {
 var delMsg='Please select EVENT';
  confirmDel = confirm(delMsg);

}
else
{
//alert(eventName);
 window.open('eventViewAvlGT.jsp?eventName='+eventName,'cnt_window','status=yes,scrollbars=yes,height=500,width=800,resizable=yes');                    
}
            
      
}



function onScreenLoad()
{
//alert("in screen load");
var alreadySelectedEvent = <%=alreadySelectedEvent%>

var alreadySelectedProduct=<%=alreadySelectedProduct%>
 
 if(alreadySelectedEvent==null)
 {
 alreadySelectedEvent = "0";
 }
 
document.getElementById("eventValue").options[alreadySelectedEvent].selected = true;

if(alreadySelectedProduct==null)
{
	alreadySelectedProduct = "0";
}

document.getElementById("productValue").options[alreadySelectedProduct].selected = true;
 
}


//Added by ANkit on 25 June
function acceptGT()
{
	//alert(acceptGTemailList);
	if(acceptGTemailList == "" )
	 {
	 var delMsg='Please select atleast one Guest Trainer';
	  confirmDel = confirm(delMsg);

	}
	else
	{

	    var index = document.getElementById("eventValue").value;
	    var eventName = document.getElementById("eventValue").options[index].innerHTML;
	    var prodindex = document.getElementById("productValue").value;
	    var prodName = document.getElementById("productValue").options[prodindex].innerHTML;
	    
	    document.getElementById("product").value = prodName;
		document.getElementById("event").value=eventName;
		document.getElementById("index").value=index;
		document.getElementById("product_index").value=prodindex;
		document.getElementById("acceptGTEmailist").value=acceptGTemailList;
		document.getElementById("acceptGTEmailistChoice").value='Y';
	//	alert(document.getElementById("acceptGTEmailist").value);
		document.forms[0].action = "acceptGTinviteManual";
		document.forms[0].submit();
	    	    
	    }
}


function checkAcceptGTinviteSelectAll(tempobj)
{
var emailing = "";
if(tempobj.checked==true)
   {
   <%if(trainers!=null){%>
   
   var map = <%=trainers.length%>
  
    for (var i=0; i<map; i++)
    {
    if(document.getElementById("chkAcceptInviteChkBox_"+i).disabled)
    {
    
    }
    else
    {
    
    	document.getElementById("chkAcceptInviteChkBox_"+i).checked = true;     
    	emailing = document.getElementById("chkAcceptInviteChkBox_"+i).value;
 
    	if(acceptGTemailList=="")
      	{
    		acceptGTemailList = emailing;  
          
       	}
   	else
      	{
   		acceptGTemailList = acceptGTemailList + ";" + emailing;  
              
      	}
    }
    }
       
       <%}%>
    }
else
         {
         
         <%if(trainers!=null){%>
   
   var map = <%=trainers.length%>
    for (var i=0; i<map; i++)
    {
    document.getElementById("chkAcceptInviteChkBox_"+i).checked = false;
    acceptGTemailList = "";
     
    }
       
       <%}%>
         
         }            
}




function addCheckForAcceptGTEmail(tempObj) 
{
 debugger
 document.getElementById("selectAllCheckBox").checked=false;
 var email =  tempObj.value;
 


 if(tempObj.checked==true)
 {
  
  if(acceptGTemailList=="")
  {
	  acceptGTemailList = email;  
  }
  else
  {
	  acceptGTemailList = acceptGTemailList + ";" + email;  
     }
  }

 else
 { 
   var pos = acceptGTemailList.indexOf(email + ";");
   var pos2 = acceptGTemailList.indexOf(email);
      
   if(pos2 != -1 && pos == -1)
    {
    var txt = acceptGTemailList.replace(email,"");
    emailList = txt;
    //   alert("pos2 != -1......" + emailList);
    }
    else if(pos != -1)
    {
     var replaceEmail = email + ";";
     var txt = acceptGTemailList.replace(replaceEmail,"");
     acceptGTemailList = txt;
//    alert("pos != -1......" + emailList);
   }
   else
   {
//   alert("something fishi");
   }
 
 }
 
 //alert(acceptGTemailList);
}


//End by ANkit on 25 June



function addCheckForEmail(tempObj) 
{
 debugger
 document.getElementById("selectAllCheckBox").checked=false;
 var email =  tempObj.value;
 


 if(tempObj.checked==true)
 {
  
  if(emailList=="")
  {
   emailList = email;  
  }
  else
  {
   emailList = emailList + ";" + email;  
     }
  }

 else
 { 
   var pos = emailList.indexOf(email + ";");
   var pos2 = emailList.indexOf(email);
      
   if(pos2 != -1 && pos == -1)
    {
    var txt = emailList.replace(email,"");
    emailList = txt;
    //   alert("pos2 != -1......" + emailList);
    }
    else if(pos != -1)
    {
     var replaceEmail = email + ";";
     var txt = emailList.replace(replaceEmail,"");
     emailList = txt;
//    alert("pos != -1......" + emailList);
   }
   else
   {
//   alert("something fishi");
   }
 
 }
}

function addCheckForReminderEmail(tempObj) 
{
 
 document.getElementById("selectAllRemCheckBox").checked=false;
 var email =  tempObj.value;
 

 if(tempObj.checked==true)
 {
  
  if(reminderEmailList=="")
  {
   reminderEmailList = email;  
  }
  else
  {
   reminderEmailList = reminderEmailList + ";" + email;  
     }
  }

 else
 { 
     var pos = reminderEmailList.indexOf(email + ";");
   var pos2 = reminderEmailList.indexOf(email);
   

   
   if(pos2 != -1 && pos == -1)
   {
    var txt = reminderEmailList.replace(email,"");
    reminderEmailList = txt;
 //   alert("pos2 != -1......" + emailList);
   }
   else if(pos != -1)
   {
   var replaceEmail = email + ";";
    var txt = reminderEmailList.replace(replaceEmail,"");
     reminderEmailList = txt;
//    alert("pos != -1......" + reminderEmailList);
   }
   else
   {
//   alert("something fishi");
   }
 
 }
}



function checkReminderSelectAll(tempobj)
 {
// alert("checkSelectAll");
 var emailing = "";
 if(tempobj.checked==true)
    {
    //alert("in if");
    <%if(trainers!=null){%>
    
    var map = <%=trainers.length%>
   // alert(map);
     for (var i=0; i<map; i++)
     {
     if(document.getElementById("chkReminderChkBox_"+i).disabled)
     {
     //alert("disabled");
     }
     else{
     //alert("in if jjhik");
     document.getElementById("chkReminderChkBox_"+i).checked = true;     
     emailing = document.getElementById("chkReminderChkBox_"+i).value;
  
     if(reminderEmailList=="")
       {
           reminderEmailList = emailing;  
           // alert(i + "*******" +  reminderEmailList);
        }
    else
       {
           reminderEmailList = reminderEmailList + ";" + emailing;  
               //alert(i + "^^^^^" + reminderEmailList);
       }
     }
     }
        
        <%}%>
     }
 else
          {
          //alert("in else");
          <%if(trainers!=null){%>
    
    var map = <%=trainers.length%>
     for (var i=0; i<map; i++)
     {
     document.getElementById("chkReminderChkBox_"+i).checked = false;
     reminderEmailList = "";
      //alert(emailList);
     }
        
        <%}%>
          
          }
 
 
             
 }
 
 

function checkSelectAll(tempobj)
 {
 //alert("checkSelectAll");
 if(tempobj.checked==true)
    {
    //alert("in if");
    <%if(trainers!=null){%>
    
    var map = <%=trainers.length%>
    //alert(map);
     for (var i=0; i<map; i++)
     {
     
       if(document.getElementById("chkEmailChkBox_"+i).disabled)
     {
     //alert("disabled");
     }
     else{
     document.getElementById("chkEmailChkBox_"+i).checked = true;     
     var emailing = document.getElementById("chkEmailChkBox_"+i).value;
   
     if(emailList=="")
       {
           emailList = emailing;  
           // alert(i + "*******" +  emailList);
        }
    else
       {
           emailList = emailList + ";" + emailing;  
             //  alert(i + "^^^^^" + emailList);
       }}
     
     }
        
        <%}%>
     }
 else
          {
          //alert("in else");
          <%if(trainers!=null){%>
    
    var map = <%=trainers.length%>
     for (var i=0; i<map; i++)
     {
     document.getElementById("chkEmailChkBox_"+i).checked = false;
     emailList = "";
     // alert(emailList);
     }
        
        <%}%>
          
          }
 
 
             
 }
 
 

function submitEmail() 
{

if(emailList == "" )
 {
 var delMsg='Please select atleast one Guest Trainer';
  confirmDel = confirm(delMsg);

}
else
{

   var emailToStr = 'mailto:';	
	var ccString = '';
	var subjectStr = '&subject=Invitation for the Event';	
	var sendToStr = '?subject=';
    var index = document.getElementById("eventValue").value;
    var eventName = document.getElementById("eventValue").options[index].innerHTML;
    
    var prodindex = document.getElementById("productValue").value;
    var prodName = document.getElementById("productValue").options[prodindex].innerHTML;
  
    //  var product=document.getElementById("productValue").value;
    
    
    var toEmail = emailList;
 //  var contentBody = 'Hi,\\n You have been selected for Event: ' + eventName +  '.\nPlease Click on link below to accept:\nhttp://sceint.pfizer.com  \n\nThanks and Regards, \nSCE TEAM';              
 //   var contentBody = ' Hi,%20%0d%0a%20%0d%0aYou have been selected for Event: ' + eventName +  '.%20%0d%0aPlease Click on link below to accept:%20%0d%0ahttp://sceint.pfizer.com  %20%0d%0a%20%0d%0aThanks and Regards, %20%0d%0aSCE TEAM';
     //alert(contentBody);
    window.open('evaluation/sendEmailTemplateOne.jsp?toEmail='+toEmail+'&eventName='+ eventName+'&productName='+ prodName,'cnt_window','status=yes,scrollbars=yes,height=500,width=900,resizable=yes');    
    
 // window.open('evaluation/sendEmailTemplateOne.jsp?toEmail='+toEmail+'&eventName='+ eventName+'&product='+product,'cnt_window','status=yes,scrollbars=yes,height=500,width=900,resizable=yes');                 
   // alert("product:"+product);
    
    
   // var contentBody = '&body= Hi,%20%0d%0a%20%0d%0aYou have been selected for Event: ' + eventName +  '.%20%0d%0aPlease Click on link below to accept:%20%0d%0ahttp://sceint.pfizer.com  %20%0d%0a%20%0d%0aThanks and Regards, %20%0d%0aSCE TEAM';
   //sendToStr = emailToStr + emailList +  subjectStr + contentBody;
  //alert("sendToStr  -- " + sendToStr)	
   // window.location = sendToStr;
    
    }
  }


function submitReminderEmail() 
{

if(reminderEmailList == "" )
 {
 var delMsg='Please select atleast one Guest Trainer';
  confirmDel = confirm(delMsg);

}
else
{

   var emailToStr = 'mailto:';	
	var ccString = '';
	var subjectStr = '&subject=Reminder-Invitation for the Event';	
	var sendToStr = '?subject=';
    var index = document.getElementById("eventValue").value;
    var eventName = document.getElementById("eventValue").options[index].innerHTML;
    //var product=document.getElementById("productValue");
    
    var prodindex = document.getElementById("productValue").value;
    var prodName = document.getElementById("productValue").options[prodindex].innerHTML;
	
    
     var toEmail = reminderEmailList;
    
    window.open('evaluation/sendEmailTemplateTwo.jsp?toEmail='+toEmail+'&eventName='+ eventName+'&productName='+prodName,'cnt_window','status=yes,scrollbars=yes,height=500,width=900,resizable=yes');   
  //window.open('evaluation/sendEmailTemplateTwo.jsp?toEmail='+toEmail+'&eventName='+ eventName+'productValue='+product,'cnt_window','status=yes,scrollbars=yes,height=500,width=900,resizable=yes');                    
    // alert("product:"+product);
    
      // var contentOfBody = 'Hi,%20%0d%0a%20%0d%0aThis is to remind you that you have been selected for Event: '+ eventName  + '.%20%0d%0aPlease Click on link below to accept:%20%0d%0ahttp://sceint.pfizer.com  %20%0d%0a%20%0d%0aThanks and Regards, %20%0d%0aSCE TEAM';
     // var contentBody = '&body=' + contentOfBody ;
	//sendToStr = emailToStr + reminderEmailList +  subjectStr + contentBody;
//alert("sendToStr  -- " + sendToStr)
	
   // window.location = sendToStr;
    
  }
}

 
    function checkSelectedEvent(templateObj)
    {
     var index = templateObj.selectedIndex; 
   //  alert("index" + index);
     
       var which = templateObj.options[index].innerHTML;
    //  alert("which" + which);
       
      		document.getElementById("index").value = index;
		document.getElementById("event").value = which;

		//document.forms[0].action = "getEventProduct";
		
		//document.forms[0].action = "gotoviewGTByEvent";
		document.forms[0].action = "getEventProduct";
        //document.forms[0].action="gotoviewGTByEvent?whichEvent="+which+"&whichIndex="+index;
     //  alert(document.forms[0].action="gotoviewGTByEvent.do?whichEvent="+which+"&whichIndex="+index);
        document.forms[0].submit();
        }
    
   
    
    function checkSelectedProduct(templateObj)
    {
     var index = templateObj.selectedIndex; 
   //  alert("index" + index);
  
     	var ddl = document.getElementById("eventValue");
   	   var event_index = document.getElementById("eventValue").selectedIndex;
   	   var event_name= ddl.options[event_index].text;
       var whichproduct = templateObj.options[index].innerHTML;
    //  alert("which" + which);
       
      		
		document.getElementById("product").value = whichproduct;
		document.getElementById("event").value=event_name;
		document.getElementById("index").value=event_index;
		document.getElementById("product_index").value=index;
		//document.forms[0].action = "getEventProduct";
		
		document.forms[0].action = "getEventProduct";
		document.forms[0].action = "gotoviewGTByEvent";
		
        //document.forms[0].action="gotoviewGTByEvent?whichEvent="+which+"&whichIndex="+index;
     //  alert(document.forms[0].action="gotoviewGTByEvent.do?whichEvent="+which+"&whichIndex="+index);
        document.forms[0].submit();
        }
    
        </script>
    </head>
   
    <body  class="admin" onload="onScreenLoad()">
        <div id="wrap">
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
            
                <%@include file="navbar.jsp" %>
            <!-- end #top_head -->
            </div>
           <div id="eval_sub_nav">
			<s:url action="admin" var="adminUrl"></s:url>
			<s:a href="%{adminUrl}">
				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoadmin.gif"
					alt="Back to main admin" width="119" height="18" />
			</s:a>
		</div>
        
            <h3>Event Invitation</h3>
             <!-- end #content -->

             <div id="main_content">
         <s:form action="gotoSCEReport" tagId="eventInvitation">     
         <input type="hidden" id="event" name="event" value="" />
				<input type="hidden" id="index" name="index" value="" />
		<input type="hidden" id="product" name="product" value="" />
		<input type="hidden" id="product_index" name="product_index" value="" />
		<input type="hidden" id="acceptGTEmailist" name="acceptGTEmailist" value="" />
		<input type="hidden" id="acceptGTEmailistChoice" name="acceptGTEmailistChoice" value="Y" />
           <table width="width:500px;"   id="eventTableMapping" class="evalmappingtable" align="center">
           <tr>
           <td class="evalmappingtd">Event </td>
               <td class="evalmappingtd"><select id="eventValue" style="position:absolute; left:130px; top:160px; margin:0;padding:0;" name="selEventForGTName" onchange="checkSelectedEvent(this)">
                     <option value="0">---Select---</option>
                     
                           
<%
                    if (eventNameFetch != null) {
                        for (int i=0; i<eventNameFetch.length; i++) {                                
                    %>
                               
                                <option value="<%=i+1%>"><%=eventNameFetch[i]%></option>
                              <%  
                        }}
                               %>

                    </select>
                    </td>
                    
              <td class="evalmappingtd">Products </td>
               <td class="evalmappingtd"><select id="productValue" style="position:absolute; top:160px; margin:0;padding:0;" name="productforevent" onchange="checkSelectedProduct(this)">
                     <option value="0">---Select---</option>
                     
                           
<%
                    if (eventProducts != null) {
                        for (int i=0; i<eventProducts.length; i++) {                                
                    %>
                               
                                <option value="<%=i+1%>"><%=eventProducts[i]%></option>
                              <%  
                        }}
                               %>

                    </select>
                    </td>

           </tr>
       
          
           
           
           </table>
                
               
                      
              <br/>       
                
                       
                   
            <%if(trainers!=null){%>
          
             <table  cellpadding="0" cellspacing="0" style="table-layout:fixed;border-right:0; border-bottom: 0; border-left: 1; border-top: 0;display:block;" align="center">
                    <tr>
                           <!-- Sorting begin sanjeev -->
                         <th width="60px" align="left" class="sort_up" onclick="ts_resortTable(this, 0);return false;" ><b>GT First Name</b></th>
                         <th width="60px" align="left" class="sort_up" onclick="ts_resortTable(this, 1);return false;"><b>GT Last Name</b></th>
                           <!-- Sorting end sanjeev -->
                           <th width="160px" align="left" class="sort_up" ><b>GT Email</b></th>
                             <th width="50px" align="left" class="sort_up" ><b>GT Role</b></th>
                               <th width="40px" align="left" class="sort_up" ><b>GT Location</b></th>
                                 <th width="60px" align="left" class="sort_up" ><b>Associated Product</b></th>
                                 <th width="60px"  align="left" class="sort_up" ><b>Last Event End Date</b></th>
                                   <th width="60px" align="left" class="sort_up" ><b>Invite Status</b></th>
                                     <th width="60px"  align="left" class="sort_up" ><b><input id="selectAllCheckBox" style="width:14px;" align="left" type="checkbox" onclick="checkSelectAll(this)">Email Invite</b></th>
                                      <th width="70px"  align="left" class="sort_up" ><b><input id="selectAllRemCheckBox"  style="width:14px;" align="left" type="checkbox" onclick="checkReminderSelectAll(this)">Reminder Email</b></th>
                         
                                   <!-- code change begin for adding check box for accepting invitation by admin--->
                                   <!-- comment begin--->
                            <!--             <%if(opsMgrAccess.equalsIgnoreCase("Y"))
                                    	  {
                                    	  %>
                                      <th  align="left" class="sort_up checkbox" style="text-align: center;"><b>Accept Invite<br><input  class="checkbox"  id="selectAllAcceptCheckBox"  style="width:14px;" align="left" type="checkbox" onclick="checkAcceptGTinviteSelectAll(this)"></b></th>
                                   	  <%} %>
                                   	  --->
                                   	   <!-- code change end for adding check box for accepting invitation by admin-->
                                       <!-- comment end --->
                    </tr>
                    <%
                        GuestTrainer objGT=null;
                            for(int i=0;i<trainers.length;i++){
                                objGT=trainers[i];
                               
                    %>
                    <tr>
                        <td><%=objGT.getFname()%></td>
                        <td><%=objGT.getLname()%></td>
                        <td><%=objGT.getRepEmail()%></td>
                        <td><%=objGT.getRepRole()%></td>
                        <td><%=objGT.getRepLocation()%></td>
                        <td><%=objGT.getAssociatedProduct()%></td>
                        <%if(objGT.getLastEventDate()!=null){%>
                        <td><%=SCEUtils.ifNull((objGT.getLastEventDate().getMonth()+1)+"-"+(objGT.getLastEventDate().getDate())+"-"+(objGT.getLastEventDate().getYear()+1900), "&nbsp;")%></td>
                        <%}else{%>
                        <td><%=SCEUtils.ifNull(objGT.getLastEventDate(), "&nbsp;")%></td>
                        <%}%>
                        
                        
                         <% if(objGT.getIsAccepted()==null) {%>
                           <td align="center">-- </td>
                         <% }else if(objGT.getIsAccepted().equalsIgnoreCase("Y")) {%>
                        <td>Accepted</td>
                        <%}else if(objGT.getIsAccepted().equalsIgnoreCase("N")){%>
                        <td>Rejected</td>
                        <%}else { %>                      
                        
                        <td align="center">-- </td>
                        <%}%>
                        
               
                        <%
                        if(objGT.getIsAccepted()==null)
                        {
                        if(objGT.getRepEmailIsSent()==null ){%>
                        <td><input  id="chkEmailChkBox_<%=i%>" type="checkbox" style="width:14px;" align="left"   name="selectGTChkBox" value="<%=objGT.getRepEmail()%>" onClick="addCheckForEmail(this)" /></td>
                       <% } else if(objGT.getRepEmailIsSent().equalsIgnoreCase("Y")) {%>
                        <td ><input  id="chkEmailChkBox_<%=i%>" style="width:14px;" align="left"  type="checkbox" name="selectGTChkBox" value="<%=objGT.getRepEmail()%>" onClick="addCheckForEmail(this)" disabled /></td>
                        <%} else {%>
                        <td><input  id="chkEmailChkBox_<%=i%>" type="checkbox" style="width:14px;" align="left"  name="selectGTChkBox" value="<%=objGT.getRepEmail()%>" onClick="addCheckForEmail(this)" /></td>
                        <%}} else{%>
                        <td><input  id="chkEmailChkBox_<%=i%>" type="checkbox" style="width:14px;" align="left"  name="selectGTChkBox" value="<%=objGT.getRepEmail()%>" onClick="addCheckForEmail(this)" disabled /></td>
                        <%}%>
                        
                        
                        <% if(objGT.getIsAccepted()==null)
                        {
                        if(objGT.getRepEmailIsSent()==null){%>
                          <td ><input id="chkReminderChkBox_<%=i%>" type="checkbox" style="width:14px;" align="left"  name="selectGTChkBox" value="<%=objGT.getRepEmail()%>" onClick="addCheckForReminderEmail(this)" disabled /></td>
                         <% } else if(objGT.getRepEmailIsSent().equalsIgnoreCase("Y")) {%>
                         <td ><input id="chkReminderChkBox_<%=i%>" type="checkbox" style="width:14px;" align="left"  name="selectGTChkBox" value="<%=objGT.getRepEmail()%>" onClick="addCheckForReminderEmail(this)" /></td>
                        <%} else {%>
                         <td><input  id="chkReminderChkBox_<%=i%>" type="checkbox" style="width:14px;" align="left"  name="selectGTChkBox" value="<%=objGT.getRepEmail()%>" onClick="addCheckForReminderEmail(this)" disabled /></td>
                          <%}}else{%>
                      <td ><input id="chkReminderChkBox_<%=i%>" type="checkbox" style="width:14px;" align="left"  name="selectGTChkBox" value="<%=objGT.getRepEmail()%>" onClick="addCheckForReminderEmail(this)" disabled /></td>
                        <%}%>
                        
                       <!-- code change begin Release2 for adding check box for accepting invitation by admin--->
                       
                        <% 
                        
                  /*   
                  * Sanjeev Added false condition so that Checkbox for accepting invite should not be visible.
                  *
                  */
                        
                    //    if(opsMgrAccess.equalsIgnoreCase("Y"))
                    	 if(false)
                  	    {
                  	  
                        	System.out.println("Email Sent["+i+"] "+objGT.getRepEmailIsSent());
                        System.out.println("IsAccepted["+i+"] "+objGT.getIsAccepted());	
                         if(objGT.getRepEmailIsSent() != null)
                        {
                        
                        if(objGT.getIsAccepted()==null)
                        {%>
                          <td ><input class="checkbox" id="chkAcceptInviteChkBox_<%=i%>" type="checkbox" style="width:14px;" align="left"  name="selectGTChkBox" value="<%=objGT.getRepEmail()%>" onClick="addCheckForAcceptGTEmail(this)" /></td>
                         <% } 
                        else if(objGT.getIsAccepted().equalsIgnoreCase("Y") ) {%>
                         <td ><input  class="checkbox" id="chkAcceptInviteChkBox_<%=i%>" type="checkbox" style="width:14px;" align="left"  name="selectGTChkBox" value="<%=objGT.getRepEmail()%>" onClick="addCheckForAcceptGTEmail(this)"  disabled /></td>
                        <%} 
                        else {%>
                         <td><input   class="checkbox" id="chkAcceptInviteChkBox_<%=i%>" type="checkbox" style="width:14px;" align="left"  name="selectGTChkBox" value="<%=objGT.getRepEmail()%>" onClick="addCheckForAcceptGTEmail(this)"  /></td>
                          <%}
                        }else{%>
                      <td ><input class="checkbox"  id="chkAcceptInviteChkBox_<%=i%>" type="checkbox" style="width:14px;" align="left"  name="selectGTChkBox" value="<%=objGT.getRepEmail()%>" onClick="addCheckForAcceptGTEmail(this)" disabled /></td>
                        <%}
                         }%>
                        
                       
                    <!-- code change end Release2 for adding check box for accepting invitation by admin--->
                        
                    </tr>
                    <%}%>
            </table>
            <!--<input type="button" value="Save"/>-->
                        <%}%>
           
            
            <% if(alreadySelectedProduct=="0" || alreadySelectedProduct==null)
            
            {
            %>
                    <div></div> 
                      <%}else
                      {%>
                        
                        <input type="button" class="buttonStyles"  align="middle" value="Send Email Invite" onclick="submitEmail()"    style="width:135px; font-size:0.9em;">
           
                        <input type="button" class="buttonStyles" value="Reminder Email " style="width:135px; font-size:0.9em;" onclick="submitReminderEmail()" >
                        
                  <!--      <input type="button" class="buttonStyles" align="left"   value="View Available Guest Trainers"  onclick="viewTrainerList()" style="width:160px;  font-size:0.9em;"> -->
                    <!--added button for accept Invitaion manualy SCE Release-2 1 july 2015 -->     
                 <!--      <input type="button" class="buttonStyles" value="Accept GT invite " style="width:135px; font-size:0.9em;" onclick="acceptGT()" >  --> 
                  
                  
                  <%}%> 
            </s:form>
              <div class="clear"></div>
             </div>
        </div>
        <!-- end #wrap -->
    </body>
    
    </html>