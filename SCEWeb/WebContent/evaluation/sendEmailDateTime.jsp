<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="com.pfizer.sce.beans.TrainerLearnerMapping"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.db.SCEManager"%>
<%@ page import="com.pfizer.sce.beans.EventsCreated"%>


<%-- <%@ page import="java.io.*,java.util.*,javax.mail.*"%> --%>
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
 		String mapId ="";
 		
 		//getMapidForTrainer
       emailList = request.getParameter("toEmail");
       eventName = request.getParameter("eventName");
 
       productName = request.getParameter("productName");

    SCEManagerImpl sceManagerImpl = new SCEManagerImpl();
	
  
  
 EventsCreated events = (EventsCreated)sceManagerImpl.getEventByName(eventName);
 
 String typeEvent = events.getTypeOfEval();
 String dates1=events.getEventStartDate();
 String eDate=events.getEventEndDate();
 String[] endDateArr=eDate.split(" ");
 String endDate=endDateArr[0];
 String[] x = dates1.split(" ");
 String dates = x[0];
 
 String formatteddates=dates.substring(5,7)+"-"+dates.substring(8,10)+"-"+dates.substring(0,4);
 endDate=endDate.substring(5,7)+"-"+endDate.substring(8,10)+"-"+endDate.substring(0,4);

 
 //added code by manish on 3/23/2016 to get trainer email
 
	String email1="";	
	List<String> ustartTime = new ArrayList<String>();
	
	List<String> uendTime = new ArrayList<String>();
	
	List<String> emailSeparatedList = Arrays.asList(emailList.split(";"));
	 
	
	if (emailSeparatedList != null && !emailSeparatedList.isEmpty())
	{
		email1=emailSeparatedList.get(emailSeparatedList.size()-1);
	}
	
	boolean flag;
	
	if(emailSeparatedList.size()==1)
	{
		flag=true;
	}
	//end code
 
	
	 mapId= sceManagerImpl.getMapidForTrainer(email1, productName, eventName);
	
	//added code by manish to get trainer first and last name
	 SCEManagerImpl sceManager = new SCEManagerImpl();
	String trainerFirstname="";
	String trainerLastname="";
	 TrainerLearnerMapping[] trainerLearner1 = sceManager.getTrainerName(email1);
	for (TrainerLearnerMapping t1 : trainerLearner1) {
		
		trainerFirstname=t1.getTrainerFname();
		trainerLastname=t1.getTrainerLname();
			}
	//end code
 
 
	
	//added code by manish to get trainer date and timeslot
	 String[] learnerEmail=emailList.split(";");
 int totalLearners=(learnerEmail.length-1);
	

 List<String> slotDate = new ArrayList<String>();

 List<String> slotTime = new ArrayList<String>();
 
 List<String> slotTime1 = new ArrayList<String>();
 	 TrainerLearnerMapping[] trainerLearner2 = sceManager.getTrainerslots(email1,eventName,mapId);
 	
Integer length=trainerLearner2.length; 	

for(int i =0;i<length;i++)
{
	TrainerLearnerMapping trainerLearner;
	slotDate.add(i, trainerLearner2[i].getSlotDate());
 		
 	System.out.println("slotDate:"+slotDate);
	
 		slotTime.add(i, trainerLearner2[i].getSlotTime());
 		System.out.println("slotTime:"+slotTime);
 		
 		String[] array = new String[slotTime.size()];
 		array = slotTime.toArray(array);
 		
 		slotTime1.add(i, array[i].replace("$", ","));
 		
}
 
 	
 

 	//end code
	
 
 %>


<script language="javascript">
  
  var emailingList = '<%=emailList%>';

  
function submitEmailByJava()
{


var toEmail = document.getElementById("to_ID").value;
var toCC = document.getElementById("cc_ID").value;
var toSub = document.getElementById("sub_ID").value;
var contentBodyN = document.getElementById("body_ID").value;
var eventSel = '<%=eventName%>';
var productSel='<%=productName%>';



var contentBody = contentBodyN; 



//alert(contentBody + "  --------in temp one");

/*Jan release begin simi - Changes done to resolve the content issue*/
//window.open('sendEmail.jsp?toEmail='+toEmail+'&contentBody='+ contentBody+'&eventSel='+ eventSel+'&toCC='+ toCC+'&subject='+ toSub,'cnt_window','status=yes,scrollbars=yes,height=500,width=900,resizable=yes');                    
document.getElementById('toEmail').value=toEmail;
document.getElementById('contentBody').value=contentBody;
document.getElementById('eventSel').value=eventSel;
document.getElementById('subject').value=toSub;
//alert(document.getElementById('subject').value);
document.getElementById('toCC').value=toCC;
document.getElementById('productSel').value=productSel;
document.getElementById("sendEmailForm").submit();
//document.getElementById("productSel").submit();
window.close();
/*Jan release end simi - Changes done to resolve the content issue*/

}

function onScreenLoad()
{

 document.getElementById("to_ID").value = '<%=emailList%>';
 submitEmailByJava();
 window.close();
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
			<div id="main_content" >
					
					
				<div class="Page" style="display: none;">
					<div>
						
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
										size=50 value="Selected Time Slots for <%=eventName%>"></input></td>
								</tr>
								<tr>
									<td colspan=2 style="border: 0;"><textarea type="text"
											id="body_ID" style="width: 650px; height: 600px;" value="">
											
											<font style ="font-family: 'Arial'; font-size:10pt">
Dear <%=trainerFirstname %> <%=trainerLastname %>
 
Please find your selected time slots for <%=eventName%> associated to product <%=productName %>

<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0 style='border-collapse:collapse;border:none'><tr><td  valign=bottom style='border:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt'><p class=MsoNormal align=center style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto;text-align:center'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><br><b><span style='font-weight:bold'>Evaluation Date</span></b><o:p></o:p></span></font></p></td>
<td  valign=bottom style='white-space: nowrap;border:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt'><p class=MsoNormal align=center style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto;text-align:center'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><br><b><span style='font-weight:bold'>Time Slots</span></b><o:p></o:p></span></font></p></td>
<%for(int j=0;j<length;j++){%><tr height=26 style='height:19.75pt'> <td  height=26 style='white-space: nowrap;border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:19.75pt'><p class=MsoNormal style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial;white-space: nowrap'><%=slotDate.get(j) %><o:p></o:p></span></font><u1:p></u1:p></p></td> <td  height=26 style='white-space: nowrap;border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:19.75pt'><p class=MsoNormal style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial;white-space: nowrap'><%=slotTime1.get(j)%> <o:p></o:p></span></font><u1:p></u1:p></p></td></tr><%}%></table>


Thanks,

Training Logistics Team 
</font>

</textarea></td>
								</tr>
								<!-- <tr>
									<td style="border: 0;"><input type="button"
										class="buttonStyles" align="middle" value="Send Email "
										onclick="submitEmailByJava()"
										style="width: 135px; font-size: 0.9em;"></td>
								</tr> -->
							</table>
					</div>
				</div>


			</div>
			<!-- end #content -->

		</div>
		<!-- end #wrap -->

	</body>
</s:form>


    <form action="sendEmailForTrainer.jsp" method="post" id="sendEmailForm" target="T_SendEmail">   
    <input type="hidden" name="contentBody" id="contentBody" value=""/>
    <input type="hidden" name="toEmail" id="toEmail" value=""/>
    <input type="hidden" id="eventSel" name="eventSel" value=""/>
    <input type="hidden" id="toCC" name="toCC" value=""/>
    <input type="hidden" id="subject" name="subject" value=""/>
    <input type="hidden" id="productSel" name="productSel" value=""/>
    
    </form>


</html>
