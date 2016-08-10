<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="com.pfizer.sce.beans.GuestTrainer"%>
<%@page import="java.util.Date"%>
<%
	//@page import="java.sql.Date"
%>
<%@page import="com.pfizer.sce.beans.EventsCreated"%>
<%@page import="com.pfizer.sce.beans.Events"%>
<%@page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="java.io.*,java.util.*,javax.mail.*"%>
<%@ page import="javax.mail.internet.*,javax.activation.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@include file="IAM_User_Auth.jsp"%>
<%-- <netui-data:declareControl type="SCEDBControls.SCEManager" controlId="SCEManager"></netui-data:declareControl>
 --%>
<%
 SCEManagerImpl sceManager = new SCEManagerImpl();
 boolean manualAcceptFlag=false;
 String name="";
 String[] emails;
 String email="";
 String errorFlag="N";
 String ntid="";
 String gtNtid="";
 String gtName="";
 String event=(String) request.getAttribute("eventName");
 System.out.println("EventName Catched: "+event);
 String datez=(String) request.getAttribute("startDate");
 System.out.println("StartDate Catched: "+datez);
 String product= (String) request.getAttribute("productName");
 System.out.println("Product Catched: "+product);
 String gtEmail= (String) request.getAttribute("GtEmail");
 if(gtEmail!= null && gtEmail!="")
 {
	    gtNtid=sceManager.getNtidByEmail(gtEmail);
	    System.out.println("Gt NTID fetched is :"+gtNtid);
	    GuestTrainer gt[]=sceManager.getGTByEmail(gtEmail);
	    gtName=gt[0].getFname();
 }
 String[] products;
 
 //Added by ankit on 8 june 
 EventsCreated myevent= new EventsCreated();
 myevent =sceManager.getEventByName(event);
 
 System.out.println("event end date"+(myevent.getEventEndDate()));
 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
 java.util.Date endDate=sdf.parse(myevent.getEventEndDate());
 java.util.Date startDate=sdf.parse(myevent.getEventStartDate());
 Calendar endCal = Calendar.getInstance();
 Calendar startCal = Calendar.getInstance();
 Calendar itratorCal = Calendar.getInstance();
 endCal.setTime(endDate);
 startCal.setTime(startDate);
 itratorCal.setTime(startDate);
 List<Date> evalDates = new ArrayList<Date>();
 
 while (itratorCal.getTime().before(endDate))
 {
     Date result = itratorCal.getTime();
     evalDates.add(result);
     itratorCal.add(Calendar.DATE, 1);
 }
 
 evalDates.add(endDate);
 
 Iterator j = evalDates.iterator();
 
 String formatteddates=datez.substring(5,7)+"-"+datez.substring(8,10)+"-"+datez.substring(0,4);
 String formattedEndDate=myevent.getEventEndDate().substring(5,7)+"-"+myevent.getEventEndDate().substring(8,10)+"-"+myevent.getEventEndDate().substring(0,4);
 System.out.println("event end date object created");
 
      
User u  = (User)session.getAttribute("user");

  if(u!=null)
  {
    name = u.getFirstName();
    ntid = u.getNtid();  
    emails = sceManager.getGTemailByNTID(ntid);    
    
    if(emails==null || emails.length!=1)
    {
        errorFlag = "Y";
    }
    else
    {
        email = emails[0];
        
    }
    
    if(gtEmail !=null && gtEmail!="")
    {
    	String userGroup=u.getUserGroup();
        if(userGroup.equalsIgnoreCase("SCE_OpsManager") || userGroup.equalsIgnoreCase("SCE_Administrators")) 
        {
        	if(gtNtid!= null && gtNtid!="")
        	 {
        		    ntid=gtNtid;
        		    email=gtEmail;
        		    manualAcceptFlag=true;
        	 }	
        }	
    }
   
          System.out.println("___________name______________" + name);
          System.out.println("___________ntid______________" + ntid);
          System.out.println("___________event______________" + event);
  }
          
 /* String ifAlreadyRecorded = sceManager.checkInviteResponse(ntid,event);
 System.out.println(ifAlreadyRecorded + " ---------ifAlreadyRecorded"); */
String ifValidGt=new SCEControlImpl().checkValidGtForInvitation(ntid,event);
 System.out.println("___________ifValidGt______________" + ifValidGt);
%>

<script language="javascript">





function acceptInvite()
{
var emailSel = '<%=email%>';
var choiceSel = 'Y';
var eventSel = '<%=event%>';
var productSel='<%=product%>';
 document.forms[0].action="gotoConfirmInvitation.action?emailSel="+emailSel+"&choiceSel="+choiceSel+"&eventSel="+eventSel+"&productSel="+productSel;
 document.forms[0].submit();
}

function reject()
{
var emailSel = '<%=email%>';
var choiceSel = 'N';
var eventSel = '<%=event%>';
var productSel='<%=product%>';
 document.forms[0].action="gotoConfirmInvitation.action?emailSel="+emailSel+"&choiceSel="+choiceSel+"&eventSel="+eventSel+"&productSel="+productSel;
 document.forms[0].submit();
}




/* Added by Ankit on 3 july  */
 
//  Javascript array fro Date And Time Slots.
var dateTime = new Array(); 
var dateTimeArrayCounter=0;
var timeslots = "";






function pushDateTime()
{
	var alertText ="Do you wish to continue?";
	
	var r = confirm(alertText);
    if (r == true) {
    	
    	
    	var emailSel = '<%=email%>';
    	var eventSel = '<%=event%>';
    	var productSel='<%=product%>';
    	
    	
    	document.getElementById("DateTimeArray").value=dateTime;
    	document.getElementById("emailSel").value=emailSel;
    	document.getElementById("eventSel").value=eventSel;
    	document.getElementById("productSel").value=productSel;
    	document.getElementById("slotsArray").submit();
    	//slotsArray
    	
    	
    	
    } 
    else {
        return false;
    }
	
	

	
}

function selecttimeSlots_1(tempobj,state)
{
		console.info(tempobj+'  '+state);
}




function selecttimeSlots(tempobj)
{
	
	
		if (tempobj.checked == true) 
		{
				if(timeslots=="")
		      	{
					timeslots = tempobj.value;
		       	}
		   		else
		      	{
		   			timeslots = timeslots + "$" + tempobj.value;   
		      	}

	     }
		else 
		{
			   var pos = timeslots.indexOf(tempobj.value + "$");
			   var pos2 = timeslots.indexOf(tempobj.value);
			   var pos3 = timeslots.indexOf("$"+tempobj.value);
			   
			   
			   if(pos2==0) //If first time slot and it contains more than one timeslots
			   {	
				   if(pos==0)
					   {
					   	var txt = timeslots.replace(tempobj.value+"$","");
				    	timeslots = txt;
					   }
				   else
					   {
					   	var txt = timeslots.replace(tempobj.value,"");
				    	timeslots = txt;
					   }
			    	
			   }
			   else
			   {
				    var txt = timeslots.replace("$"+tempobj.value,"");
			    	timeslots = txt;   
			   }
		}
		
	}

function checkAlltimeSlots(tempobj)
{
	
	//added by manish
	
		if (document.getElementById("eventStartDate").value == null || document.getElementById("eventStartDate").value =="" ) 
	{
		alert("Please select a Date.");
		document.getElementById("pushButton").disabled = true;
		document.getElementById("selectalltimeslots").checked = false;
		return false;
	}
	
	//
		timeslots = "";
		if (tempobj.checked == true) 
		{
			for (var i = 0; i <10; i++) 
			{
				document.getElementById("timeslot_" + i).checked = true;
				if(timeslots=="")
		      	{
					timeslots = document.getElementById("timeslot_" + i).value;
		       	}
		   		else
		      	{
		   			timeslots = timeslots + "$" + document.getElementById("timeslot_" + i).value;   
		      	}

			}
		} 
		else 
		{
			for (var i = 0; i < 10; i++) 
			{
				document.getElementById("timeslot_" + i).checked = false;
				timeslots = "";

			}
		}
	}


function saveDate()
{
	
	
	if (document.getElementById("eventStartDate").value == null || document.getElementById("eventStartDate").value =="" ) 
	{
		alert("Please select a Date.");
		document.getElementById("pushButton").disabled = true;
		return false;
	}
	
	if (timeslots== null || timeslots =="" ) 
	{
		alert("Please select a time Slot.");
		document.getElementById("pushButton").disabled = true;
		return false;
	}
	
	document.getElementById("pushButton").disabled = false;
	
	var allDatesSelected = document.getElementById("selectallDates").checked;
	if (!allDatesSelected) 
	{
		var i=checkDateExist();
		if (i ==-99) 
		{   
			dateTime[dateTimeArrayCounter] = new Array(2);
			dateTime[dateTimeArrayCounter][0]=document.getElementById("eventStartDate").value;
			dateTime[dateTimeArrayCounter][1]=timeslots;
			dateTimeArrayCounter++;
			document.getElementById("eventStartDate").value="";
			clearSlots();
			alert("Slots for Current Date are successfully saved.");
		}
		else
		{
			dateTime[i][1]=timeslots;
			clearSlots();
			alert("Slots for Current Date are successfully Updated.");
		}

		
	} else 
	{
		for (var i = 0; i < dateTime.length; i++) 
		{
			dateTime[i][1]=timeslots;
		}
		alert("Slots for All Date are successfully saved.");
	}
	
	
	document.getElementById("eventStartDate").value="";
	refreshSummary();
	disableTimeSlots();
}


function clearSlots()
{
	
	
	
	if(document.getElementById("locktimeslots").checked)
	{
		return 
	}
	
	if(document.getElementById("selectalltimeslots").checked)
	{
		document.getElementById("selectalltimeslots").checked=false; 
	}
	
	for (var i = 0; i <10; i++) 
	{
		document.getElementById("timeslot_" + i).checked = false;
	}
	timeslots="";
}


function checkDateExist() 
{
	var j;
	for ( j= 0; j < dateTime.length; j++) 
	{
		if(dateTime[j][0]==document.getElementById("eventStartDate").value.toString())
		{
			return j;
		}
			
	}
	return -99;
}

function checkTimeSlots(index) 
{
	
	var slots=dateTime[index][1];
	
	if(slots==null||slots=="")
	{
		return false;
	}
	var slotArray = slots.split("$");
	
	for (var k = 0; k < slotArray.length; k++) 
	{
		
		var array_element = slotArray[k];
		
		if (array_element!=null && array_element != "") 
		{
			for (var i = 0; i <10; i++) 
			{
				if (document.getElementById("timeslot_" + i).value==array_element) 
				{
					document.getElementById("timeslot_" + i).checked=true;
					if(timeslots=="")
			      	{
						timeslots = document.getElementById("timeslot_" + i).value;
			       	}
			   		else
			      	{
			   			timeslots = timeslots + "$" + document.getElementById("timeslot_" + i).value;   
			      	}
				}
				 
			}
			
		}
		
	}
	
	
}


function onDateChange()
{
	document.getElementById("pushButton").disabled = true;
	var dateIndex = checkDateExist();
	if (dateIndex!=-99) 
	{
		checkTimeSlots(dateIndex);
	}
}

function selectAllDate()
{
	
	var status= document.getElementById("selectallDates").checked;
	
	if(dateTime != null && dateTime.length !=0 && status)
	{
		var flag=confirm("Your previously selected date and timeslots will no longer be available.Do you wish to continue?");
		if(!flag)
		{
			document.getElementById("selectallDates").checked= false;
			return;
		}
	}
	
	document.getElementById("pushButton").disabled = true;
	
	var nodes = document.getElementById("calendar-container").getElementsByTagName('*');
	for(var i = 0; i < nodes.length; i++)
	{
	     nodes[i].disabled = status;
	}
	if (status) 
	{
		
		document.getElementById("saveDateTime").value="Save All Dates";
		
		document.getElementById("eventStartDate").value="All Dates";
		
		var eventStartDatestring = '<%=new SimpleDateFormat("MM/dd/yyyy").format(startDate)%>';
		datestring= eventStartDatestring.split("/");
		var eventStartDate = new Date(datestring[2],datestring[0]-1,datestring[1],0,0,0,0);
		
		var eventEndDatestring =   '<%=new SimpleDateFormat("MM/dd/yyyy").format(endDate)%>';
    	datestring= eventEndDatestring.split("/");
		var eventEndDate = new Date(datestring[2],datestring[0]-1,datestring[1],0,0,0,0);
		
		var bufferDate= new Date(eventStartDate);
		dateTimeArrayCounter=0;
		for (; bufferDate <=eventEndDate; bufferDate.setDate(bufferDate.getDate()+1)) 
		{
			dateTime[dateTimeArrayCounter] = new Array(2);
			dateTime[dateTimeArrayCounter][0]=new Date(bufferDate).toLocaleDateString();
			dateTime[dateTimeArrayCounter][0]=""+(new Date(bufferDate).getMonth()+1)+"/"+new Date(bufferDate).getDate()+"/"+new Date(bufferDate).getFullYear()+"";
			dateTimeArrayCounter++;	
		}
		enableTimeSlots();
		
	}
	else
	{
		document.getElementById("eventStartDate").value="";
		document.getElementById("saveDateTime").value="Save Current Date";
		clearEmptyValues();
		disableTimeSlots();
		clearSlots();
	}
	
	
}

function showHideSelectionSection()
{
	if(document.getElementById("selectionTableDiv").style.display=='block')
		document.getElementById("selectionTableDiv").style.display='none';
	else
		document.getElementById("selectionTableDiv").style.display='block';
	
	if(document.getElementById("slotTable").style.display=='block')
		document.getElementById("slotTable").style.display='none';
	else
		document.getElementById("slotTable").style.display='block';
	
	if(document.getElementById("saveButton").style.display=='block')
		document.getElementById("saveButton").style.display='none';
	else
		document.getElementById("saveButton").style.display='block';
	
	if(document.getElementById("buttonDiv").style.display=='block')
		document.getElementById("buttonDiv").style.display='none';
	else
		document.getElementById("buttonDiv").style.display='block';
	
	if(document.getElementById("summaryDiv").style.display=='block')
		document.getElementById("summaryDiv").style.display='none';
	else
		document.getElementById("summaryDiv").style.display='block';
	
	
	document.getElementById("showHideSections").style.width="190px";

	
	if(document.getElementById("showHideSections").value=='Hide date and time Slots selection')
		{
		document.getElementById("showHideSections").value='Select date and time Slots';
		}
	else
		{
		document.getElementById("showHideSections").value='Hide date and time Slots selection';
		}
}

	
function showSummary() {
	
	
	if(document.getElementById("showHideSummary").value=='Show Summary')
		{
		 document.getElementById("showHideSummary").value='Hide Summary';
		 document.getElementById("summaryDiv").style.display='block';
		 
		    var myTableDiv = document.getElementById("summaryDiv");
		      
		    var table = document.createElement('TABLE');
		    table.border='1';
		    
		    var tableBody = document.createElement('TBODY');
		    table.appendChild(tableBody);
		    
		    var tr = document.createElement('TR');
		    tableBody.appendChild(tr);
		    
		    var td = document.createElement('TD');
	       
	        td.className="sort_up";
	        td.appendChild(document.createTextNode('Date'));
	        tr.appendChild(td);
	           
	        td = document.createElement('TD');
	       
	        td.className="sort_up";
	        td.appendChild(document.createTextNode('Selected Time Slots'));
	        tr.appendChild(td);
		       
		      
		    
	for (var i = 0; i < dateTime.length; i++) 
	{
				var tr = document.createElement('TR');
				tableBody.appendChild(tr);

				for (var j = 0; j < 2; j++) 
				{
					var td = document.createElement('TD');
					
					if(dateTime[i][j] != null)
					{
						td.appendChild(document.createTextNode(dateTime[i][j].split('$')));	
					}
					else
					{
						td.appendChild(document.createTextNode(""));
					}
					
					tr.appendChild(td);
				}
			}
			myTableDiv.appendChild(table);
		} else 
		{
			document.getElementById("showHideSummary").value = 'Show Summary';

			var nodes = document.getElementById("summaryDiv").childNodes;
			var j = nodes.length;
			for (var i = 0; i < j; i++) 
			{
				document.getElementById("summaryDiv").removeChild(nodes[0]);
			}
			document.getElementById("summaryDiv").style.display = 'none';
		}
	}
	
	
function lockTimeSlots() 
{
	if (checkSlotStatus()) 
	{
		var status= document.getElementById("locktimeslots").checked;
		
		if(document.getElementById("selectalltimeslots").checked)
		{
			document.getElementById("selectalltimeslots").disabled=true;
		}
			
		if(status==false && document.getElementById("selectalltimeslots").disabled == true)
		{
			document.getElementById("selectalltimeslots").disabled=false;
		}
		
		var nodes = document.getElementById("slotTable").getElementsByTagName('*');
		var j = nodes.length;
		for(var i = 0; i < nodes.length; i++)
		{
		     nodes[i].disabled = status;
		}
	}
	else
	{
		alert("Please select atleast one time slot");
		document.getElementById("locktimeslots").checked=false;
		return false;
	}	
}


function refreshSummary()
{
	if (document.getElementById("showHideSummary").value == 'Hide Summary') 
	{
		var nodes = document.getElementById("summaryDiv").childNodes;
		var j = nodes.length;
		for (var i = 0; i < j; i++) 
		{
			document.getElementById("summaryDiv").removeChild(nodes[0]);
		}
		
		var myTableDiv = document.getElementById("summaryDiv");
	    
	    var table = document.createElement('TABLE');
	    table.border='1';
	    
	    var tableBody = document.createElement('TBODY');
	    table.appendChild(tableBody);
	    
	    var tr = document.createElement('TR');
	    tableBody.appendChild(tr);
	    
	    var td = document.createElement('TD');
	   
	    td.className="sort_up";
	    td.appendChild(document.createTextNode('Date'));
	    tr.appendChild(td);
	       
	    td = document.createElement('TD');
	    
	    td.className="sort_up";
	    td.appendChild(document.createTextNode('Selected Time Slots'));
	    tr.appendChild(td);
	    
		for (var i = 0; i < dateTime.length; i++) 
		{
				
				var tr = document.createElement('TR');
				tableBody.appendChild(tr);
		
				for (var j = 0; j < 2; j++) 
				{
					var td = document.createElement('TD');
						
					td.appendChild(document.createTextNode(dateTime[i][j].split('$')));
						
					tr.appendChild(td);
					
				}
		}
		myTableDiv.appendChild(table);
	}
	
}


function checkSlotStatus()
{
	var flag;
	for (var i= 0; i <10; i++) 
	{
		if(document.getElementById("timeslot_" + i).checked == false)
			{
			 flag=false;   
			}
		else
		{
			flag=true;
			break;
		}
	}
	return flag;
}


function disableTimeSlots() 
{	
		var nodes = document.getElementById("slotTable").getElementsByTagName('*');
		var j = nodes.length;
		for(var i = 0; i < nodes.length; i++)
		{
		     nodes[i].disabled = true;
		}	
}

function enableTimeSlots() 
{	
	if(!document.getElementById("locktimeslots").checked)
		{
			var nodes = document.getElementById("slotTable").getElementsByTagName('*');
			var j = nodes.length;
			for(var i = 0; i < nodes.length; i++)
			{
			     nodes[i].disabled = false;
			}
		}
}

function clearEmptyValues()
{
	
	for (var i = 0; i < dateTime.length; i++) 
	{
			if(dateTime[i][1]==null ||dateTime[i][1].toString()=="")
			{
				dateTime.splice(i,1);
				dateTimeArrayCounter--;
				i=i-1;
			}
			
	}
}

	//  End
	
	//function to send mail to trainers ****************************************************
	
		function submitEmail() 
{



   var emailToStr = 'mailto:';	
	var ccString = '';
	var subjectStr = '&subject=Notification for selected TimeSlots';	
	var sendToStr = '?subject=';
   
    var eventName = '<%=event%>';
    var prodName = '<%=product%>';
  
    var emailSel = '<%=email%>';
    var toEmail = emailSel;

    window.open('evaluation/sendEmailDateTime.jsp?toEmail='+toEmail+'&eventName='+ eventName+'&productName='+ prodName,'cnt_window','status=yes,scrollbars=yes,height=500,width=900,resizable=yes');    

    
    }
	//end function to send mail
	
</script>

<!-- Added by Ankit on 3 july -->

<style type="text/css">
@import
url(<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar-win2k-cold-2.css)
;
</style>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/lang/calendar-en.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar-setup.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/js/DateValidator.js"></script>



<!-- End -->



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

</head>
<body id="inemail" class="inemailC">
	<div id="wrap">

		<div id="top_head">
			<h1>Pfizer</h1>
			<h2>Sales Call Evaluation</h2>


			<!-- end #top_head -->
		</div>
		<s:form action="gotoConfirmEmailSent"  tagId="emailConfirmation" method="POST">
		 
			<h3>Invitation Invite</h3>

			<div id="main_content">


				<!-- begin  sanjeev -->
				<%
					if(ifValidGt!=null){
				%>
				<%
					String ifAlreadyRecorded = sceManager.checkInviteResponse(ntid,event,product);
				              System.out.println(ifAlreadyRecorded + " ---------ifAlreadyRecorded");
				%>
				<!-- end  sanjeev -->

				<%
					if(errorFlag.equalsIgnoreCase("N")){
				%>
				<table width="90%" cellpadding="0" cellspacing="0"
					style="border-right: 0; border-bottom: 0; border-left: 0; border-top: 0; display: block; font-size: 1.1em;"
					align="center">
					<tr>
						<td style="font-size: 0.9em; border: 0;">Hi <%=name%>,
						</td>
					</tr>
					<%
						if(ifAlreadyRecorded==null){
					%>
					<tr>
						<td style="font-size: 0.9em; border: 0;">Please either Accept or Reject the invite
							<%
							 if(manualAcceptFlag)
							 {
							%>
								on behalf of <%=gtName%>,
							<%}%> 
							for the event <%=event%> associated
							to product <%=product%> starting on <%=formatteddates%> and ending on <%=formattedEndDate %> !!
						</td>
					</tr>
					<tr>
						<td class="evalmappingtd"></td>
					</tr>
					<tr>
						<%
							if (ifAlreadyRecorded == null) {
						%>
						<td style="font-size: 0.9em; border: 0;">
						<input type="button" id="showHideSections"
							class="buttonStyles" align="middle" value="Accept"
							onclick="showHideSelectionSection()" style="width: 135px; font-size: 0.9em;">
						<input type="button" class="buttonStyles" value="Reject"
								style="width: 135px; font-size: 0.9em;" onclick="reject()">
					
							
					   </td>

						<%
							} else {
						%>
						<td style="font-size: 0.9em; border: 0;"></td>
						<%
							}
						%>
					</tr>
				
					<tr>
					
						<td class="evalmappingtd"    >
					
						<div id="selectionTableDiv" style="display: none;">
							<table style="border: 1px solid #ccc; cellspacing: 0">
								
								<tr>
									<td colspan="3" style="font-weight: bold;">
									
									        Thank you for your acceptance. Below please provide the time slots that you will be available to help for each of the dates during the event. 
									        Event dates are between <%=formatteddates %> and <%=formattedEndDate %>
									
									</td>
								</tr>
								
								<tr>
							
								<td rowspan="2" class="evalmappingtd" style="border: 1px solid #ccc; padding: 2px; padding-top: 5px; text-align: center;">
								<div
								style="float: left; margin-left: 1em; margin-bottom: 1em; width: 270px"
								id="calendar-container"></div> <script type="text/javascript">
																										
						function dateChanged(calendar) 
						{
							
						if (calendar.dateClicked) 
						{
							var datebox=document.getElementById("eventStartDate");
							datebox.value=""+(calendar.date.getMonth()+1)+"/"+calendar.date.getDate()+"/"+calendar.date.getFullYear()+"";
							var datestring= datebox.value.split("/");
							clearSlots();
                        	onDateChange();
                        	var SelectedDate = new Date(datestring[2],datestring[0]-1,datestring[1],0,0,0,0);
                        	
                        	var eventStartDatestring = '<%=new SimpleDateFormat("MM/dd/yyyy").format(startDate)%>';
							datestring= eventStartDatestring.split("/");
							var eventStartDate = new Date(datestring[2],datestring[0]-1,datestring[1],0,0,0,0);
							
							var eventEndDatestring =   '<%=new SimpleDateFormat("MM/dd/yyyy").format(endDate)%>';
                        	datestring= eventEndDatestring.split("/");
							var eventEndDate = new Date(datestring[2],datestring[0]-1,datestring[1],0,0,0,0);
							
                        	if(!(eventStartDate<=SelectedDate&&SelectedDate<=eventEndDate))
                        	{
                        		alert("Please select a date in range between <%= formatteddates%> and  <%= formattedEndDate%>! ");
                        		datebox.value="";
                        	}
                        	else
                        	{
                        		enableTimeSlots();
                        	}
						
						}
						};

						Calendar.setup({
						flat : "calendar-container",
						flatCallback : dateChanged,
						ifFormat : "%m/%d/%Y"
						});
						</script>
								
								</td>
									
								 <td class="evalmappingtd"
										style="border: 1px solid #ccc; padding: 2px; padding-top: 5px; text-align: center;">Select
										all Dates&nbsp;<input type="checkbox" id="selectallDates"
										onclick="selectAllDate()" style="width: auto; text-align: center;">
									</td> 
								</tr>
								
								<tr>
										
									<td class="evalmappingtd"
										style="border: 1px solid #ccc; padding: 2px; padding-top: 5px; text-align: center;">Select
										all time Slots&nbsp;<input type="checkbox" id="selectalltimeslots"
										onclick="checkAlltimeSlots(this)"
										style="text-align: center; width: auto">
										<br></br>
										Lock time slots selection&nbsp;
										<input type="checkbox" id="locktimeslots"
										onclick="lockTimeSlots()"
										style="text-align: center; width: auto">
									</td>
									
								</tr>

								<tr>
									<td class="evalmappingtd" colspan="2"
										style="border: 1px solid #ccc; padding: 2px; padding-top: 5px; text-align: left;">
										Selected Date&nbsp;:&nbsp;<INPUT type="text" size="11" maxlength="10"
										name="addEventForm.eventStartDate" id="eventStartDate"
										disabled onchange="onDateChange()">
									</td>
								</tr>


							</table>
						</div>
						</td>


					</tr>
					<tr>
						<td class="evalmappingtd">
						<div id="slotTable" style="display: none;">
							<table style="border: 1px solid #ccc; cellspacing: 0">
								<tr>
									<%
										for (int counter = 0; counter < 10; counter++) {
									%>
									<td class="evalmappingtd"
										style="border: 1px solid #ccc; padding: 2px; padding-top: 5px; text-align: center;">
										<%
											String fromtime = ((counter + 8) < 12 ? (counter + 8)
																		+ " am"
																		: ((counter + 8) == 12 ? (counter + 8)
																				+ " pm" : (counter + 8) % 12
																				+ " pm")).toString();
																String endtime = ((counter + 8 + 1) < 12 ? (counter + 8 + 1)
																		+ " am"
																		: ((counter + 8 + 1) == 12 ? (counter + 8 + 1)
																				+ " pm"
																				: (counter + 8 + 1) % 12
																						+ " pm")).toString();
										%> <label style="width: auto; text-align: center;"><%=fromtime + " to " + endtime%></label>
										<input type="checkbox" id="timeslot_<%=counter%>"
										value='<%=fromtime.replace(" ", "") + "-"
										+ endtime.replace(" ", "")%>'
										onclick="selecttimeSlots(this)"
										style="width: auto; text-align: center;" disabled=true>
									</td>
									<%
										if (counter == 4) {
									%>
								</tr>
								<tr>
									<%
										}
									%>
									<%
										}
									%>
								</tr>
								
								
							</table>
							</div>
							
							<div id="saveButton" style="display:none;">
										<table>
								<tr>
									<td class="evalmappingtd"
										style="padding: 2px; padding-top: 5px; text-align: left;"><input
										type="button" class="buttonStyles" align="middle" id="saveDateTime"
										value="Save Current Date" onclick="saveDate()"
										style="width: 135px; font-size: 0.9em;">
										</td>
								
								</tr>
								
							</table>
							
							</div>
						
							
						</td>
					</tr>
					<%
						} else {
					%>
					<tr>
						<td style="font-size: 0.9em; border: 0;">Your response has
							already been recorded for the event <%=event%> associated to
							product <%=product%> starting on <%=formatteddates%>!!
						</td>
					</tr>
					<%
						}
					%>
					
					
					
						
					<tr>
						<%
							if (ifAlreadyRecorded == null) {
						%>
						
						<td style="font-size: 0.9em; border: 0;">
						<div id="buttonDiv" style="display: none">
<!-- 						<input type="button" -->
<!-- 							class="buttonStyles" align="middle" value="Accept" -->
<!-- 							onclick="acceptInvite()" style="width: 135px; font-size: 0.9em;"> -->
<!-- 								<input type="button" class="buttonStyles" value="Reject" -->
<!-- 								style="width: 135px; font-size: 0.9em;" onclick="reject()"> -->
							<input type="button"
							class="buttonStyles" align="middle" value="Save And Accept Invite." id="pushButton"
							onclick="pushDateTime()" disabled  style="width: 135px; font-size: 0.9em;">
							
							<input type="button"
							class="buttonStyles" align="middle" value="Show Summary" id="showHideSummary"
							onclick="showSummary()" style="width: 135px; font-size: 0.9em;">
							
						</div>
					   </td>
					   

						<%
							} else {
						%>
						<td style="font-size: 0.9em; border: 0;"></td>
						<%
							}
						%>
					</tr>
					<!-- *************** -->
						<tr>
							<td class="evalmappingtd">
							<div id="summaryDiv" style="display: none;width: 50%;">
						
							</div>
							</td>
						</tr>
					
						<!-- *************** -->
				
				</table>
				<%
					} else {
				%>
				<table width="90%" cellpadding="0" cellspacing="0"
					style="border-right: 0; border-bottom: 0; border-left: 0; border-top: 0; display: block; font-size: 1.1em;"
					align="center">
					<tr>
						<td style="font-size: 0.9em; border: 0;">Sorry your NTID does
							not have a match in our system !!</td>
					</tr>
				</table>
				<%
					}
				%>

				<!-- begin  sanjeev -->
				<%
					} else {
				%>
				<table width="90%" cellpadding="0" cellspacing="0"
					style="border-right: 0; border-bottom: 0; border-left: 0; border-top: 0; display: block; font-size: 1.1em;"
					align="center">
					<tr>
						<td style="font-size: 0.9em; border: 0;">Sorry you are not
							authorized to view this page!!</td>
					</tr>
				</table>
				<%
					}
				%>

				<!--  end sanjeev -->
				<!-- 	Hidden fiedls Added By ankit -->
				<!-- end -->
			</div>
			<!-- end #content -->
		</s:form>
		<s:form action="pushDateTime" method="post" id="slotsArray">
		<input type="hidden" id="DateTimeArray" name="DateTimeArray" value="">
		<input type="hidden" id="emailSel" name="emailSel" value="">
		<input type="hidden" id="eventSel" name="eventSel" value="">
		<input type="hidden" id="productSel" name="productSel" value="">
		</s:form>
	</div>
	<!-- end #wrap -->

</body>
</html>
