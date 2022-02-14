<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="com.pfizer.sce.beans.TrainerLearnerMapping"%>
<%@page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.db.SCEManager"%>
<%@ page import="com.pfizer.sce.beans.EventsCreated"%>
<%@ page import="java.io.*,java.util.*,javax.mail.*"%>
<%@ page import="javax.mail.internet.*,javax.activation.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*"%>
<%-- <%@ page import="utils.system"%> --%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@include file="IAM_User_Auth.jsp"%>

<%
String location = request.getRequestURL().toString().toLowerCase();
URL url=new URL(location);
String host=null;
if(url.getPort()==-1)
{
host=url.getHost();
}
else
{
	
	host=url.getHost()+":"+url.getPort();
}

 String emailList = "";
 String emailBodyTest = "";

       String    eventName ="";
       String productName="";
 
 emailList = request.getParameter("toEmail");
  eventName = request.getParameter("eventName");
  productName = request.getParameter("productName");

SCEManagerImpl sceManagerImpl =new SCEManagerImpl();
 EventsCreated events = (EventsCreated)sceManagerImpl.getEventByName(eventName);
 
 String typeEvent = events.getTypeOfEval();
 String dates1=events.getEventStartDate();
 String eDate=events.getEventEndDate();
 String[] endDateArr=eDate.split(" ");
 String endDate=endDateArr[0];
 String[] x = dates1.split(" ");
 String dates = x[0];
 
 String condition = "";
 String con1 = "have to";
 String con2 = "not have to";
 
 String formatteddates=dates.substring(5,7)+"-"+dates.substring(8,10)+"-"+dates.substring(0,4);
 endDate=endDate.substring(5,7)+"-"+endDate.substring(8,10)+"-"+endDate.substring(0,4);
 
 if(typeEvent.equalsIgnoreCase("Virtual"))
 {condition = con2;
 }else
 {condition = con1;
 }
  // System.out.println("toEmail::1 "+emailList);
  // System.out.println("eventName::2 "+eventName);

  
 //added code by manish on 3/23/2016 to get trainer email
 
	String email1="";	
	List<String> ustartTime = new ArrayList<String>();
	
	List<String> uendTime = new ArrayList<String>();
	
	List<String> emailSeparatedList = Arrays.asList(emailList.split(";"));
	 
	
	if (emailSeparatedList != null && !emailSeparatedList.isEmpty()) {
		email1=emailSeparatedList.get(emailSeparatedList.size()-1);
		}
	
	//end code
 
	//added code by manish to get trainer first and last name
	 SCEManagerImpl sceManager = new SCEManagerImpl();
	String trainerFirstname="";
	String trainerLastname="";
	 TrainerLearnerMapping[] trainerLearner1 = sceManager.getTrainerName(email1);
	for (TrainerLearnerMapping t1 : trainerLearner1) {
		
		trainerFirstname=t1.getTrainerFname();
		trainerLastname=t1.getTrainerLname();
			}
	
	int flag=0;


	if(emailSeparatedList.size()==1)

	{

	flag = 1;

	}

	
	
	
	
	//end code
 
 %>


<script language="javascript">
  
  var emailingList = '<%=emailList%>';

//alert (emailingList);
  
  
function submitEmailByJava()
{
//alert("submitEmailByJava");

var toEmail = document.getElementById("to_ID").value;

var toCC = document.getElementById("cc_ID").value;

var toSub = document.getElementById("sub_ID").value;
//alert(toSub);
var contentBodyN = document.getElementById("body_ID").value;

var eventSel = '<%=eventName%>';

var productSel='<%=productName%>';
//alert (eventSel);
//alert(contentBodyN.indexOf( "\n" ));
/*Jan release begin- Changes done to resolve the content issue*/
/* Comented begin */
/* var contentBodyP = contentBodyN.replace(/&/g,"%26");
var contentBody = contentBodyP.replace(/\r\n|\n/g,"%20%0d%0a"); */
/* Comented end  */
//alert(contentBody);
var contentBody = contentBodyN; 
/*Jan release end- Changes done to resolve the content issue*/

//alert(contentBody + "  --------in temp one");

/*Jan release begin simi- Changes done to resolve the content issue*/
//window.open('sendEmail.jsp?toEmail='+toEmail+'&contentBody='+ contentBody+'&eventSel='+ eventSel+'&toCC='+ toCC+'&subject='+ toSub,'cnt_window','status=yes,scrollbars=yes,height=500,width=900,resizable=yes');                    
document.getElementById('toEmail').value=toEmail;
document.getElementById('contentBody').value=contentBody;
document.getElementById('eventSel').value=eventSel;
document.getElementById('subject').value=toSub;
document.getElementById('toCC').value=toCC;

document.getElementById('productSel').value=productSel;

document.getElementById("sendEmailForm").submit();
/* window.close(); shindo same window used for success message Edge release*/
/*Jan release end simi- Changes done to resolve the content issue*/

}

function onScreenLoad()
{

 document.getElementById("to_ID").value = '<%=emailList%>';    
 
  
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
<s:form action="gotoSearchForAttendee">
	<body id="p_admin_users" onload="onScreenLoad()" class="admin">
		<div id="wrap">

			<div id="top_head">
				<h1>Pfizer</h1>
				<h2>Sales Call Evaluation</h2>


				<!-- end #top_head -->
			</div>


			<h3>SEND EMAIL</h3>
			<div id="main_content">

				<div class="Page">
					<div>
						<br> <font class="text">Please verify the contents of
							the letter and then click the Send Email button.</font><br />
						<br />
							<table style="width: 600px;" width="30%">
								<tr>
									<td style="width: 5%; border: 0;"><font class=text>To:</font></td>
									<td style="width: 40%; border: 0;"><input
										style="width: 490px;" id="to_ID" name="toAddress" type=text
										size=50 value=""></input></td>
								</tr>
								<tr>
									<td style="border: 0;"><font class=text>CC:</font></td>
									<td style="width: 40%; border: 0;"><input
										style="width: 490px;" id="cc_ID" name="ccAddress" type=text
										size=50 value=""></input></td>
								</tr>
								<tr>
									<td style="width: 5%; border: 0;"><font class=text>Subject:</font></td>
									<td style="width: 40%; border: 0;"><input
										style="width: 490px;" id="sub_ID" name="ccAddress" type=text
										size=50
										value="Reminder Email Guest Trainer Invitation for <%=eventName%>"></input></td>
								</tr>
								<tr>
									<td colspan=2 style="border: 0;"><textarea type="text"
											id="body_ID" style="width: 650px; height: 600px;" value="">
											
											<font style ="font-family: 'Arial'; font-size:10pt">
<%if(flag==1){ %>

<font style ="font-family: 'Arial'; font-size:10pt">

Dear <%=trainerFirstname %> <%=trainerLastname %>

<%}else{ %>

<font style ="font-family: 'Arial'; font-size:10pt">

Dear All,

<%} %>
 

This is reminder Email

First off, congratulations for being selected by your leadership team to be a Guest Trainer for the upcoming <b><%=eventName%>!</b> 

Your role will be to assist the Training Department by completing all Sales Call Evaluations for the <%=eventName%> event. 

The Sales Call Evaluations will be <%=typeEvent%>, meaning you will <%=condition%>  travel to the Pfizer Learning Center!. The number of evaluations you will be asked to complete is limited to a maximum of 10 per event. 
 
Please confirm your availability for the <%=eventName%> between <%=formatteddates%> and <%=endDate%> by clicking below 

<%-- <a href ="http://<%=host %>/SCEWeb/acceptRejectInvite?whichEvent=<%=eventName%>&productName=<%=productName%>&strDate=<%=dates%>"><%=eventName%> Training</a> --%>
 <!-- Added for respond button-->
<table>
<tr>
<td style="width:3%"></td>
<td style="width:3%"></td>
<td style="width:3%"></td>
<td style="width:10%">
<a href ="http://<%=host %>/SCEWeb/acceptRejectInvite?whichEvent=<%=eventName%>&productName=<%=productName%>&strDate=<%=dates%>">
<div style="background-color: #90c140;height:10%x;width: 30%;text-align: center;font-size: 30;font-family: sans-serif;color: white;border-radius: 10px;border: #3D9E69 3px solid;align-self: center;margin-left: 30%;text-decoration: none;">Click Here to Respond</div>
</a>
</td>
<td style="width:3%"></td>
<td style="width:3%"></td>
<td style="width:3%"></td>
</tr>
</table>
 <!-- Added for respond button-->
There will be a mandatory Guest Trainer Orientation .  During the orientation, we will review your roles and responsibilities as a Guest Trainer, as well as the schedule.  You will receive a meeting invitation via Microsoft Outlook shortly that will have all the log-in information.
 
Please do not hesitate to reach out to traininglogistics@pfizer.com if you have any questions. 

Again thanks for your partnership and support of training, and we look forward to working with each of you!   
 

Thanks,

Training Logistics Team 
</font>
</textarea></td>
								</tr>
								<tr>
									<td style=" border:0;" colspan="2"><input type="button"
										class="buttonStyles" align="middle" value="Send Email "
										onclick="submitEmailByJava()"
										style="width: 135px; font-size: 0.9em;"></td>
								</tr>
							</table>
					</div>
				</div>



			</div>
			<!-- end #content -->

		</div>
		<!-- end #wrap -->

	</body>
</s:form>

<!--  Jan release- Changes done to resolve the content issue-->
 <!-- shindo changed target T_SendEmail to self Edge browser -->
 <!-- action="sendEmail.jsp" method="post" id="sendEmailForm" target="T_SendEmail" -->
    <form  action="sendEmail.jsp" method="post" id="sendEmailForm" target="_self">   
    <input type="hidden" name="contentBody" id="contentBody" value=""/>
    <input type="hidden" name="toEmail" id="toEmail" value=""/>
    <input type="hidden" id="eventSel" name="eventSel" value=""/>
    <input type="hidden" id="toCC" name="toCC" value=""/>
    <input type="hidden" id="subject" name="subject" value=""/>
    <input type="hidden" id="productSel" name="productSel" value=""/>
    </form>


</html>
