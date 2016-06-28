<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="java.math.MathContext"%>
<%@page import="java.util.Date"%>
<%@page import="org.apache.struts2.components.If"%>
<%@page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.beans.EventsCreated"%>
<%@ page import="com.pfizer.sce.beans.GuestTrainer"%>
<%@ page import="com.pfizer.sce.beans.Learner"%>
<%@ page import="com.pfizer.sce.beans.WebExDetails"%>
<%@ page import="com.pfizer.sce.beans.TrainerLearnerMapping"%>
<%@ page import="com.pfizer.sce.beans.Util"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.io.*,java.util.*,javax.mail.*"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="javax.mail.internet.*,javax.activation.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>


<%@include file="IAM_User_Auth.jsp" %>
 <!-- <netui-data:declareControl type="SCEDBControls.SCEManager" controlId="SCEManager"></netui-data:declareControl> -->
<%
	String emailList = "";
 String emailBodyTest = "";
 String    eventName ="";
 String    product ="";
 String ifLearner = "L";
  String courseStartDate="";
 // double evalStartTime=8.00;
 
 SCEManagerImpl sceManager = new SCEManagerImpl();

 
 
 
 emailList = request.getParameter("toEmail");
 
 

 

 
 eventName = request.getParameter("eventName");
 product = request.getParameter("productName");
 courseStartDate=request.getParameter("courseStartDate");
 System.out.println(courseStartDate.substring(0,4)+""+courseStartDate.substring(8,10)+""+courseStartDate.substring(5,7));
 java.util.Date courseDate_1=new java.util.Date(Integer.parseInt(courseStartDate.substring(0,4)),Integer.parseInt(courseStartDate.substring(8,10)),Integer.parseInt(courseStartDate.substring(5,7)));
 courseStartDate=courseStartDate.substring(5,7)+"-"+courseStartDate.substring(8,10)+"-"+courseStartDate.substring(0,4);
 //java.util.Date courseDate_1=new java.util.Date(Integer.parseInt(courseStartDate.substring(0,4)),Integer.parseInt(courseStartDate.substring(8,10)),Integer.parseInt(courseStartDate.substring(5,7)));
 
 System.out.println(" courseStartDate"+ courseStartDate);
 //evalStartTime=(Double.parseDouble(request.getParameter("evalStartTime")));
 
 //System.out.println("evalStartTime::------------******----"+evalStartTime);
 System.out.println("toEmail::------------------------------------"+emailList); 
 System.out.println("courseStartDate::------------------------------------"+courseStartDate); 
 EventsCreated events = (EventsCreated)sceManager.getEventByName(eventName);
 
 
 
 
 
 
 
 //added code by manish on 3/4/2016
 
 	String email1="";	
 	List<String> ustartTime = new ArrayList<String>();
 	
 	List<String> uendTime = new ArrayList<String>();
 	
 	List<String> emailSeparatedList = Arrays.asList(emailList.split(";"));
 	 
 	
	if (emailSeparatedList != null && !emailSeparatedList.isEmpty()) {
		email1=emailSeparatedList.get(emailSeparatedList.size()-1);
		}
 	
 	List<TrainerLearnerMapping> list = new ArrayList<TrainerLearnerMapping>(); 
 	String timeSlots=sceManager.getTimeSlots(courseStartDate,email1).toString();
 	
 	System.out.println("Time Slots String:"+timeSlots);
 	
 	List<String> slotsSepration = Arrays.asList(timeSlots.split("\\$"));
 	System.out.println("Separated Slots:"+slotsSepration);
 	System.out.println("Separated Slots size:"+slotsSepration.size());
 	
 	 try{
 		for(int i=0;i<slotsSepration.size();i++)
 	 	{
 	 		
 	 		
 	 		String one =slotsSepration.get(i);
 	 		System.out.println("First value:"+one);
 	 		String[] startTime=null;
 	 		if(one.contains("am"))
 	 		{
 	 			startTime=one.split("am");
 	 		}
 	 		else
 	 		{
 	 			 startTime=one.split("pm");
 	 		}
 	 		
 	 		String updatedStartTime=startTime[0];
 	 		ustartTime.add(updatedStartTime+".0");
 			System.out.println("Start Time List:"+ustartTime);
 			String endTime[] = startTime[1].split("\\s|(?<=[-])");
 			String updatedEndTime=endTime[1];
 			uendTime.add(updatedEndTime+".0");
 			System.out.println("End Time:"+updatedEndTime);
 			System.out.println("End Time List:"+uendTime);
 	 	}
 	}
 	catch(Exception e)
 	{
 		System.out.println("Error:"+e);
 	} 
 	
 	
 
 	double evalStartTime=Double.parseDouble(ustartTime.get(0));
 	System.out.println("evalStartTime::------------******----"+evalStartTime);
 
 	//added code by manish on 3/4/2016
 
 
 	
 	//added code by manish to get trainer first and last name
 	String trainerFirstname="";
 	String trainerLastname="";
 	 TrainerLearnerMapping[] trainerLearner1 = sceManager.getTrainerName(email1);
 	for (TrainerLearnerMapping t1 : trainerLearner1) {
   		
 		trainerFirstname=t1.getTrainerFname();
 		trainerLastname=t1.getTrainerLname();
 			}
 	//end code
 
 
 
 
 String dates1=events.getEventStartDate();
 String[] x = dates1.split(" ");
 String dates = x[0];
 String formatteddates=dates.substring(5,7)+"-"+dates.substring(8,10)+"-"+dates.substring(0,4);

 
  String eDate=events.getEventEndDate();
 String[] e = eDate.split(" ");
 String endDate = e[0];
 endDate=endDate.substring(5,7)+"-"+endDate.substring(8,10)+"-"+endDate.substring(0,4);
 
 String trainerEmail=request.getParameter("trainerEmail");
 TrainerLearnerMapping[] noOfMailSentObj=  sceManager.getNoOfMailSentByEventTrainer(eventName,trainerEmail,courseStartDate);
 int noOfMailSent=noOfMailSentObj[0].getTrainerCount()!=null?noOfMailSentObj[0].getTrainerCount().intValue():0;
  WebExDetails[] webexdetails=(WebExDetails[]) sceManager.getWebExDetailsByEventAndUsedBy(eventName,trainerEmail);
 WebExDetails webexdetailObj=new WebExDetails();
 if(webexdetails!=null && webexdetails.length>0){
      webexdetailObj=webexdetails[0];
 }
 else{
    if(webexdetails!=null){
    sceManager.updateWebExDetailsByEventNotUsed(eventName,trainerEmail);
    webexdetails=(WebExDetails[]) sceManager.getWebExDetailsByEventAndUsedBy(eventName,trainerEmail);
       if(webexdetails!=null && webexdetails.length>0){
          webexdetailObj=webexdetails[0];
        }
    }
 }
 GuestTrainer[] trainers=(GuestTrainer[])sceManager.getGTByEmail(trainerEmail); 
 GuestTrainer trainer=trainers[0];
 
 //to determine the time zone of trainer
 String loc = trainer.getRepLocation();
 System.out.println("location____________"+loc);
 String zoneByLOC = sceManager.getTimeZone(loc);
 System.out.println("ZONE____________"+zoneByLOC);
 String timeZone = "";
 if(zoneByLOC == null)
 {   timeZone = ""; }
 else if(zoneByLOC.equalsIgnoreCase("CST"))
 {    timeZone = "CENTRAL TIME ZONE"; }
 else if(zoneByLOC.equalsIgnoreCase("PST"))
 {    timeZone = "PACIFIC TIME ZONE"; }
 else if(zoneByLOC.equalsIgnoreCase("EST"))
 {    timeZone = "EASTERN TIME ZONE"; }
 else if(zoneByLOC.equalsIgnoreCase("MST"))
 {    timeZone = "MOUNTAIN TIME ZONE"; }
 
 String[] learnerEmail=emailList.split(";");
 int totalLearners=(learnerEmail.length-1);
System.out.println("totalLearners_________________"+totalLearners);
 String[] fname=new String[totalLearners];
 String[] lname=new String[totalLearners];
 String[] email=new String[totalLearners];
 String[] location=new String[totalLearners];
 double[] evalTime=new double[totalLearners];
 BigDecimal[] evalTimeBD=new BigDecimal[totalLearners];
 BigDecimal[] evalEndTimeBD = new BigDecimal[totalLearners];
 String[] period=new String[totalLearners];
 for(int i=0;i<totalLearners;i++){
    Learner[] learners=(Learner[])sceManager.getLearnerByEmail(learnerEmail[i]);
    Learner learner=learners[0];
    fname[i]=learner.getFirstName();
    lname[i]=learner.getLastName();
    email[i]=learner.getEmailAddress();
    location[i]=learner.getLocation();
 }
 Integer evalDurationInt =events.getEvalDuration();
int evalDur=(evalDurationInt!=null)?evalDurationInt.intValue():30;

String[] eventDay=new String[totalLearners];

Integer evalPerDayInt=events.getNumberOfEval();
int evalPerDay=(evalPerDayInt!=null)?evalPerDayInt.intValue():1;
 SimpleDateFormat courseSDF=new SimpleDateFormat("MM-dd-yyyy");
    double orgTime=evalStartTime;
    double time=evalStartTime;
    double diff=0.00;
    String days=dates;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    java.util.Date d=sdf.parse(days);
    long dateMS=d.getTime();
    System.out.println(courseStartDate.toString());
    java.util.Date courseDate=courseSDF.parse(courseStartDate);
    
    //Added By ankit
    System.out.println("courseStartDate"+courseDate);
    Calendar c= Calendar.getInstance();
    c.setTime(courseDate);
    System.out.println("c created");
    System.out.println(c.get(Calendar.DAY_OF_WEEK));
    if(c.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY)
    {
    	c.add(Calendar.DATE, 1);
    }
    if(c.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY)
    {
    	c.add(Calendar.DATE, 2);
    }
    courseDate=c.getTime();
  
    //End By ankit
    
    long courseDateMS=courseDate.getTime();
    System.out.println("courseDateMS"+courseDateMS);
    courseDateMS=courseDateMS+(1000*60*60*24);
    System.out.println("courseDateMS"+courseDateMS);
     String courseStartDatePlusOne=courseSDF.format(new java.util.Date(courseDateMS));
    
    int k=0;
    System.out.println("noOfMailSent-----------"+noOfMailSent);
    while(noOfMailSent>0){
      
    	System.out.println("Value of k:"+k);
    diff=k*evalDur;
    System.out.println("Diff:"+diff);
        while(diff>=60){
            time++;
            diff=diff-60.00;
            System.out.println("Value of time inside while:"+time);
            System.out.println("Value of diff inside while:"+diff);
        }
        time=time+(diff/100); 
        System.out.println("value of time when diff in <60 after while:"+time);
        System.out.println("original Time:"+orgTime);
         time=orgTime;
         System.out.println("Value of time after orignal time:"+time);
         k++;
         System.out.println("Value of K after changing value of time:"+k);
         System.out.println("Value of evaluation per day:"+evalPerDay);
         if(evalPerDay==k){
        	 System.out.println("Value of evaluation start time:"+evalStartTime);
                orgTime=evalStartTime;
                System.out.println("Value of original time inside if:"+orgTime);
                time=evalStartTime;
                System.out.println("Value of time inside if:"+time);
                diff=0.00;
                //days++;
                dateMS=dateMS+(1000*60*60*24);
                System.out.println("Value of dateMs:"+dateMS);
                k=0;
            }
            noOfMailSent--;
            System.out.println("Value of no of mail sent:"+noOfMailSent);
    }
 for(int i=0;i<(totalLearners+noOfMailSent);i++){
    
	 System.out.println("Entering for loop,value of i:"+i);
	 System.out.println("Value of total Learners:"+totalLearners);
	 System.out.println("Value of no of mail sent:"+noOfMailSent);
	 System.out.println("Value of totalLearners+noOfMailSent:"+(totalLearners+noOfMailSent));
	 
    eventDay[i]=sdf.format(new java.util.Date(dateMS));
    
    System.out.println("Value of eventDay for:"+i+":element"+eventDay[i]);
    
    System.out.println("Value of k:"+k);
    System.out.println("Value of evalDuration:"+evalDur);
    diff=k*evalDur;
    
    System.out.println("Value of diff:"+diff);
    System.out.println("Value of Time before while:"+time);
   
    
        while(diff>=60)
        {
            time++;
            period[i]="AM";
            
            if(time>=1.0 && time<=6.0)
            {
            
            	period[i]="PM";
            }
            
            if(time>=12.0)
            {
                if(time>=13.0)
                {
                	time=time-12.0;
                }
                period[i]="PM";
            }
            Boolean flag =false;
            String checkTime = Double.toString(time);
            System.out.println("Value of checkTime:"+checkTime);
            
            System.out.println("Value of time before for:"+time);
           
            
            //added code by manish on 3/4/2016
            
            while(flag!=true)
            {
            	
            	int count=0;
            	 System.out.println("***************Inside while loop*****************"+time);
            	
            	   for(String str:ustartTime)
                   {	
                   	 System.out.println("Value of checkTime inside for loop:"+checkTime);
                   	System.out.println("Value of time:"+str);
                   	if(str.contentEquals(checkTime))
                   	{
                   		flag= true;
                   		
                   		
                   	}
                 	
                   	
                   }
                   
                 if(flag!=true)
                 {
                	 time++;
                 }
                 
                 if(time>=1.0 && time<=6.0)
                 {
                 
                 	period[i]="PM";
                 }
                 
            	   if(time>=12.0){
                       if(time>=13.0){
                       time=time-12.0;
                       }
                       period[i]="PM";
            	   }
                   checkTime=Double.toString(time);
                   System.out.println("Value of checktime at the end of while loop:"+checkTime);
                   System.out.println("Value of flag at the end of while loop:"+flag);
                  	System.out.println("Value of time at the end of ehile loop:"+time);
                  	
            }
            
           
            
            
           
            
            
            //end code by manish on 3/4/2016
            	diff=diff-60.00;
                System.out.println("Value of time:"+time);
                System.out.println("Value of diff if diff>=60:"+diff);
            
            	
            
           
           
            
        }
        
        System.out.println("If value of diff is <60:"+diff);
        System.out.println("Value of Time before calculating:"+time);
        time=time+(diff/100);
        System.out.println("Value of time:"+time);
        evalTime[i]=time;
        System.out.println("Value of eval time for:"+i+":element:"+evalTime[i]);
        period[i]="AM";
        
        if(time>=1.0 && time<=6.0)
        {
        
        	period[i]="PM";
        }
        
        if(time>=12.0){
            if(time>=13.0){
            evalTime[i]=evalTime[i]-12.0;
            }
            period[i]="PM";
        }
        
        
        evalTimeBD[i]=new BigDecimal(evalTime[i]);
        System.out.println("Value of evaluation period:"+i+":element:"+period[i]);
        System.out.println("Value of eval time BD for:"+i+":element:"+evalTimeBD[i]);
        evalTimeBD[i]=evalTimeBD[i].setScale(2,BigDecimal.ROUND_HALF_UP);
        System.out.println("Value of eval time BD for:"+i+":element after set scale:"+evalTimeBD[i]);
        
        
        //code added by manish to add end time
        SimpleDateFormat df = new SimpleDateFormat("HH.mm");
    	
    	String convert = "0" + evalTimeBD[i] + "";
		System.out.println("convert:" + convert);
		Date d1 = df.parse(convert);
		System.out
				.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("Value of date:" + d1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		c1.add(Calendar.MINUTE, evalDur);
		String newEndTime = df.format(c1.getTime());
		System.out.println("New End time:" +newEndTime);
		try
		{
			if(Double.parseDouble(newEndTime)>=12.00){
				System.out.println("Inside If newEndTime:" +newEndTime);
	            if(Double.parseDouble(newEndTime)>=13.00){
	            	System.out.println("Inside second If newEndTime:" +newEndTime);
	            	
	            	
	            	// Double sub=12.00;
	            	BigDecimal b = new BigDecimal("12.00");
	            	System.out.println("Inside If value of b:" +b);
	            	evalEndTimeBD[i] = new BigDecimal(13.00);
	            	evalEndTimeBD[i]=evalEndTimeBD[i].subtract(b);
	            	System.out.println("Value of eval end time BD inside if:"+ evalEndTimeBD[i]); 
	            BigDecimal newValue = evalEndTimeBD[i];
	            newEndTime = newValue.toString();
	            	
	            }
			}
		}
		catch(Exception ex)
		{
			System.out.println("Error:"+ex);
		}
		
		
		evalEndTimeBD[i] = new BigDecimal(newEndTime);
		
		System.out.println("Value of eval end time BD for:" + i
				+ ":element after set scale:" + evalEndTimeBD[i]);
		System.out
				.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		//end code 

		/*  Double cstartTime= Double.parseDouble(ustartTime.get(i+1)) ;
			Double cendTime =Double.parseDouble(uendTime.get(i));
		 if(cstartTime!=cendTime)
		 {	
		 	evalTime[i]=cstartTime;
		 	
		 } */
		System.out.println("Value of original Time at last:" + orgTime);
		time = orgTime;
		System.out.println("Value of Time at last:" + time);
		k++;

		System.out.println("Value of K:" + k);
		if (evalPerDay == k) {
			System.out.println("Value of evalStartTime inside if:"
					+ evalStartTime);
			orgTime = evalStartTime;
			System.out.println("Value of original time:" + orgTime);
			System.out.println("Value of evalstart time:"
					+ evalStartTime);
			time = evalStartTime;
			System.out.println("Value of Time inside if:" + time);
			diff = 0.00;
			//days++;
			dateMS = dateMS + (1000 * 60 * 60 * 24);
			System.out.println("Value of dateMS" + dateMS);
			k = 0;
		}
	}
	session.setAttribute("productName", product);
	session.setAttribute("courseStartDate", courseStartDate);
%> 
 
<script language="javascript">
  
  var emailingList = '<%=emailList%>';

  

  
  

  
function submitEmailByJava()
{
//alert("submitEmailByJava");

var toEmail = document.getElementById("to_ID").value;
//alert(toEmail);
var contentBodyOne = document.getElementById("body_ID_One").value;
var contentBodyTwo = contentBodyOne.replace(/"/g,"&quot");
var contentBody = contentBodyTwo;
//var contentBodyTableL = document.getElementById("learner_content_table").value;
//var contentBodyTableT = document.getElementById("trainer_content_table").value;
//var contentBodyTwo = document.getElementById("body_ID_Two").value;
//var contentBody = contentBodyOne + contentBodyTableT +  contentBodyTableL + contentBodyTwo;

//alert(contentBodyN);
var toCC = document.getElementById("cc_ID").value;
//alert(toCC);
var toSub = document.getElementById("sub_ID").value;
//alert(toSub);
var eventSel = '<%=eventName%>';
//alert(eventSel);
var ifFromLearner = '<%=ifLearner%>';
//alert(ifFromLearner);
//alert(contentBodyN.indexOf( "\n" ));
//var contentBodyP = contentBodyN.replace(/#/g,"%23");
//alert(contentBodyP);
//var contentBody = contentBodyP.replace(/\r\n|\n/g,"%20%0d%0a");
//alert(contentBody);
//alert(contentBody + "  --------in temp one");

//alert(contentBody);
document.getElementById('h_toEmail').value=toEmail;
document.getElementById('h_contentBody').value=contentBody;
document.getElementById('h_eventSel').value=eventSel;
document.getElementById('h_toSub').value=toSub;
document.getElementById('h_toCC').value=toCC;
document.getElementById('h_ifFromLearner').value=ifFromLearner;
//window.open('','T_SendEmail');
//document.forms[0].action="sendEmail.jsp";
//document.forms[0].submit

document.getElementById("sendEmailForm").submit();
window.close();


}

function onScreenLoad()
{

 document.getElementById("to_ID").value = '<%=email1%>';

 document.getElementById("cc_ID").value = '<%=emailList%>';
 
  
 
 
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
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/admin.css" rel="stylesheet" type="text/css" media="all" />
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
        <br> 
        <font class="text">Please verify the contents of the letter and then click the Send Email button.</font><br/><br/>
	<table style="width:600px;" width="30%" >
		<tr> 
			<td style="width:5%; border:0;"><font class=text>To:</font></td><td style="width:40%; border:0;"><input style="width:490px;"  id="to_ID" name="toAddress" type=text size=50 value=""></input></td>
		</tr>
		<tr>
			<td style=" border:0;"><font class=text>CC:</font></td><td style="width:40%; border:0;"><input style="width:490px;" id="cc_ID"  name="ccAddress" type=text size=50 value=""></input></td>
		</tr>
<tr><td style="width:5%; border:0;"><font class=text>Subject:</font></td><td style="width:40%; border:0;"><input style="width:490px;" id="sub_ID"  name="ccAddress" type=text size=50 value="Sales Call Evaluation Event:  <%=eventName%>"></input></td></tr>
<tr><td colspan=2 style=" border:0;">	
<textarea  type="text" id="body_ID_One"  style=" font-family: 'Arial'; font-size:10pt; width:650px; border-bottom:0px; height:300px;" value="" >
<font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial;'>
Dear <%=trainerFirstname %> <%=trainerLastname %>

Below you will find a list of colleagues which you are being asked to evaluate in order to complete his/her product certification.  We have tried to assign colleagues that are in same time zone as your are, however there may be some exceptions. 
Please schedule a WebEx meeting for each of the time slots listed below and send it to the individual representative. Because this is a critical step for acquiring product certification we ask that you follow the schedule outlined below to allow timely completion.

<a href='http://ecf12.pfizer.com/sites/natraining/USA/product/Shared Documents/WebEx_Meeting_Center_for_GT_SCEs_Collaborate.pdf'>Instructions for Setting up a WebEx</a>   

Before starting your evaluations, please make sure you have access to the Sales Call Evaluation application:
						<a href='http://sce.pfizer.com/'>http://sce.pfizer.com</a>   
						
<u>Please note that you as an evaluator and representative must have their cameras on.</u>

We thank you for assisting the training department complete this very important task. 
Should you have any questions regarding the SCE application, please reach out to <a href='mailto:traininglogistics@pfizer.com'>traininglogistics@pfizer.com.</a> For any WebEx issues please reach out to Help Desk.

<B>NA Training Team</B>

<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0 style='border-collapse:collapse;border:none'><tr><td width=91 valign=bottom style='width:.95in;border:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt'><p class=MsoNormal align=center style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto;text-align:center'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><br><b><span style='font-weight:bold'>Evaluation Date</span></b><o:p></o:p></span></font></p></td>
<td width=91 valign=bottom style='width:.95in;border:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt'><p class=MsoNormal align=center style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto;text-align:center'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><br><b><span style='font-weight:bold'>Evaluation Start Time</span></b><o:p></o:p></span></font></p></td><td width=91 valign=bottom style='width:.95in;border:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt'><p class=MsoNormal align=center style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto;text-align:center'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><br><b><span style='font-weight:bold'>Evaluation End Time</span></b><o:p></o:p></span></font></p></td><td width=91 valign=bottom style='width:.95in;border:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt'><p class=MsoNormal align=center style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto;text-align:center'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><br><b><span style='font-weight:bold'>Time Zone</span></b><o:p></o:p></span></font></p></td><td width=91 valign=bottom style='width:.95in;border:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt'><p class=MsoNormal align=center style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto;text-align:center'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><br><b><span style='font-weight:bold'>Last Name</span></b><o:p></o:p></span></font></p></td><td width=91 valign=bottom style='width:.95in;border:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt'><p class=MsoNormal align=center style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto;text-align:center'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><br><b><span style='font-weight:bold'>First Name</span></b><o:p></o:p></span></font></p></td><td width=91 valign=bottom style='width:.95in;border:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt'><p class=MsoNormal align=center style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto;text-align:center'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><br><b><span style='font-weight:bold'>State</span></b><o:p></o:p></span></font></p></td><td width=91 valign=bottom style='width:.95in;border:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt'><p class=MsoNormal align=center style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto;text-align:center'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><br><b><span style='font-weight:bold'>Email</span></b><o:p></o:p></span></font></p></td></tr>
<%for(int i=0;i<totalLearners;i++){%> <tr height=26 style='height:19.75pt'> <td width=91 height=26 style='width:.95in;border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:19.75pt'><p class=MsoNormal style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><%=courseStartDatePlusOne%><o:p></o:p></span></font><u1:p></u1:p></p></td> <td width=91 height=26 style='width:.95in;border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:19.75pt'><p class=MsoNormal style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><%=evalTimeBD[i]%> <%=period[i]%><o:p></o:p></span></font><u1:p></u1:p></p></td> <td width=91 height=26 style='width:.95in;border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:19.75pt'><p class=MsoNormal style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><%=evalEndTimeBD[i]%> <%=period[i]%><o:p></o:p></span></font><u1:p></u1:p></p></td> <td width=91 height=26 style='width:.95in;border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:19.75pt'><p class=MsoNormal style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'> <%=zoneByLOC%><o:p></o:p></span></font><u1:p></u1:p></p></td><td width=91 height=26 style='width:.95in;border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:19.75pt'><p class=MsoNormal style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><%=lname[i]%><o:p></o:p></span></font><u1:p></u1:p></p></td> <td width=91 height=26 style='width:.95in;border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:19.75pt'><p class=MsoNormal style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><%=fname[i]%><o:p></o:p></span></font><u1:p></u1:p></p></td> <td width=91 height=26 style='width:.95in;border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:19.75pt'><p class=MsoNormal style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><%=location[i]%><o:p></o:p></span></font><u1:p></u1:p></p></td> <td width=91 height=26 style='width:.95in;border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:19.75pt'><p class=MsoNormal style='mso-margin-top-alt:auto;mso-margin-bottom-alt:auto'><font size=2 face=Arial><span style='font-size:10.0pt;font-family:Arial'><%=email[i]%><o:p></o:p></span></font><u1:p></u1:p></p></td></tr><%}%></table>

<!-- commented on 2/26/2016 -->
<%-- Please adhere to the schedule below as to what time you need to call in and join your Guest Trainer virtually by clicking on the link below. Should you need to change a time, please contact the Guest Trainer directly and make suitable arrangements. <pre style='font-size:10.0pt;font-family:Arial;'>
Guest Trainer:              <%=trainer.getFname()%> <%=trainer.getLname()%>         
Conference Call Number:     <%=webexdetailObj.getConfCallNumber()%> 
Chairperson Passcode:       <%=webexdetailObj.getChairPersonPasscode()%> then #
Participant Passcode:       <%=webexdetailObj.getParticipantPasscode()%> then #
Adobe Link:                 <%=webexdetailObj.getMeetingLink()%></pre>
 --%>

<o:p></o:p></span></font>
</textarea>

</td></tr>
<tr>
<td style=" border:0;" colspan="2">
<input type="button"  class="buttonStyles"  align="middle" value="Send Email " onclick="submitEmailByJava()"    style="width:135px; font-size:0.9em;">
</td>
</tr>
	</table>
      </div> 
      </div>
      </div> <!-- end #content -->        
      </div><!-- end #wrap -->
    </body>
    </s:form>     
    <s:form action="sendEmail.jsp" method="post" id="sendEmailForm" target="T_SendEmail">   
    <input type="hidden" name="h_contentBody" id="h_contentBody" value=""/>
    <input type="hidden" name="h_toEmail" id="h_toEmail" value=""/>
    <input type="hidden" id="h_eventSel" name="h_eventSel" value=""/>
    <input type="hidden" id="h_toCC" name="h_toCC" value=""/>
    <input type="hidden" id="h_toSub" name="h_toSub" value=""/>
    <input type="hidden" id="h_ifFromLearner" name="h_ifFromLearner" value=""/>
    <input type="hidden" id="learner_table" name="h_ifFromLearner" value=""/>
    </s:form>
   </html>


