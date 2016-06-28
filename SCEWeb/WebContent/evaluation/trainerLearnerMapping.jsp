<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
                        
<%@page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.beans.CourseDetails"%>
<%@ page import="com.pfizer.sce.beans.Event"%>
<%@ page import="com.pfizer.sce.beans.WebExDetails"%>
<%@ page import="com.pfizer.sce.beans.TrainerLearnerMapping"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.pfizer.sce.beans.Util"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
 <%@include file="IAM_User_Auth.jsp" %>
                    
          
 <% 
  String evalStartTime[]={"8.00 AM","9.00 AM","10.00 AM","11.00 AM","12.00 PM","1.00 PM","2.00 PM","3.00 PM","4.00 PM","5.00 PM"};
  String evalStartTimeVal[]={"8.00","9.00","10.00","11.00","12.00","13.00","14.00","15.00","16.00","17.00"};
  
  String evalStartTimeSel="8.00 AM";
  if(request.getAttribute("evalStartTime")!=null){
  evalStartTimeSel=(String)request.getAttribute("evalStartTime");
  }
  String[] eventCourseFetch = null;
  String[] fetchDate = null;
    String alreadySelectedEvent = "";
   String recordAlreadyPresent = "";
  String  alreadySelectedCourse = "";
   String  alreadySelectedCourseDate = "";
  
  String tLOC = "";
  String lLOC = "";
    TrainerLearnerMapping[] mapping = null; 
    TrainerLearnerMapping[] mappingLOC = null;    
    TrainerLearnerMapping[] mappingT = null; 
    TrainerLearnerMapping[] mappingL = null; 
    TrainerLearnerMapping[] trainerCount=null;
  SCEManagerImpl sceManager = new SCEManagerImpl();
  
  String[] eventNameFetch = sceManager.getActiveEventName();
   mapping = (TrainerLearnerMapping[])request.getAttribute("trainerLearner"); 
    mappingLOC = (TrainerLearnerMapping[])request.getAttribute("trainerMannualLOC"); 
        
      eventCourseFetch =(String[]) request.getAttribute("eventPCourses");   
      fetchDate =(String[]) request.getAttribute("eventPDate"); 
      
      
       alreadySelectedEvent =(String) request.getAttribute("selEventOption");    
       alreadySelectedCourse =(String) request.getAttribute("selCoursesID"); 
       alreadySelectedCourseDate =(String) request.getAttribute("selCourseDateId"); 
       
       recordAlreadyPresent =(String) request.getAttribute("recordDuplicate"); 
       mappingT = (TrainerLearnerMapping[])request.getAttribute("trainerMannual"); 
        mappingL = (TrainerLearnerMapping[])request.getAttribute("learnerMannual"); 
       
     Integer count=(Integer)request.getAttribute("evalPerTrainer");
     if(request.getAttribute("trainerCount")!=null){
         trainerCount=(TrainerLearnerMapping[])request.getAttribute("trainerCount");
     }
     int noOfWebExDetails=0;
     WebExDetails[] webexdetails=null;
     if(alreadySelectedEvent!=null&&!alreadySelectedEvent.equalsIgnoreCase("0")&&alreadySelectedEvent.length()!=0){
     webexdetails=(WebExDetails[]) sceManager.getWebExDetailsByEvent(eventNameFetch[Integer.parseInt(alreadySelectedEvent)-1]);
     noOfWebExDetails=webexdetails.length;
     }
      // System.out.println("noOfWebExDetails"+noOfWebExDetails);
      List mappedTrainerName=new ArrayList();
      List mappedTrainerEmail=new ArrayList();
      if(mapping!=null&&mapping.length>0){
         mappedTrainerName.add(mapping[0].getTrainerName());
         mappedTrainerEmail.add(mapping[0].getTrainerEmail());
      
      boolean flag=false;
      for(int i=1;i<mapping.length;i++){
         flag=false;
         for(int j=0;j<mappedTrainerEmail.size();j++){
             if(mapping[i].getTrainerEmail().equalsIgnoreCase((String)mappedTrainerEmail.get(j))){
                 flag=true;
             }
           }
       if(!flag){
         mappedTrainerName.add(mapping[i].getTrainerName());
         mappedTrainerEmail.add(mapping[i].getTrainerEmail());
       }
      }
      }
      String sendEmailTo=(String)request.getAttribute("sendEmailTo");
      
    
    //added code by manish to get no. of evaluations per day by trainer
      
		
    	
			
    
     //SCEManagerImpl sceManager1 = new SCEManagerImpl();
       
      // int numberOfEval=sceManager1.getNoOfEvaluations(fetchDate ,sendEmailTo);
      
       //code end
  %>  
                            
                            
 <script language="javascript">
             
var checkForFetch = "";
var trainerEmail = "" ;
var learnerEmail = "";
var emailList = "";

if(typeof String.prototype.trim !== 'function') {
	  String.prototype.trim = function() {
	    return this.replace(/^\s+|\s+$/g, ''); 
	  }
}



     function getCourseDates(tempobj)
{
      //alert("in coursedates");
        var index = tempobj.selectedIndex;           
        var proId = tempobj.options[index].value;  
        var proName = tempobj.options[index].innerHTML; 
        var which = document.getElementById("eventValue").selectedIndex;
        var sel = document.getElementById("eventValue").options[which].innerHTML; 
      //Changed 22nd aug
        var evalStartTime=document.getElementById("evaluationStartTime");
        var evaluationStartTimeTxt=evalStartTime.options[evalStartTime.selectedIndex].text;
        
        
        document.getElementById("hdn1").value=which;
        document.getElementById("hdn4").value=sel;
        document.getElementById("hdn6").value=proName;
        document.getElementById("hdn7").value=proId;
        document.getElementById("evalStartTime").value=evaluationStartTimeTxt;
        
         document.forms[0].action="getCourseDates";
           //alert('Action-->'+document.forms[0].action);
         document.forms[0].submit(); 
} 
     
     
      
             
  function getCourse(tempobj)
   {     
           
        var index = tempobj.selectedIndex;           
        var which = tempobj.options[index].value;  
        var sel = tempobj.options[index].innerHTML; 
        var evalStartTime=document.getElementById("evaluationStartTime");
        var evaluationStartTimeTxt=evalStartTime.options[evalStartTime.selectedIndex].text;
        document.getElementById("hdn1").value=which;
        document.getElementById("hdn4").value=sel;
        document.getElementById("evalStartTime").value=evaluationStartTimeTxt;
        
        
    document.forms[0].action="getEventCourses";
            //alert('Action-->'+document.forms[0].action);
    document.forms[0].submit(); 
   
        }
             
             
             
 function onScreenLoad()
{
//alert("in screen load");
var alreadySelectedEvent = <%=alreadySelectedEvent%>;
var alreadySelectedCourse =<%=alreadySelectedCourse%>; 
var alreadySelectedCourseDate = <%=alreadySelectedCourseDate%>;
var noOfCourseDates=0;
<%if(fetchDate!=null){%>
    noOfCourseDates=<%=fetchDate.length%>
<%}%>

//alert(alreadySelectedCourseDate);
         if(alreadySelectedEvent==null)
         {
         alreadySelectedEvent = "0";
         }
         
        if(alreadySelectedCourse==null)
         {
        alreadySelectedCourse = "0";
         }
         if(alreadySelectedCourseDate==null)
         {
        alreadySelectedCourseDate = "0";
         }



      
       

  document.getElementById("eventValue").options[alreadySelectedEvent].selected = true;

         if(alreadySelectedEvent!="0")
           {
        	 
           document.forms[0].eventCourseSelectID.disabled = false;
           
           document.getElementById("eventCourseSelectID").options[alreadySelectedCourse].selected = true;
           
           document.getElementById("courseDateSelected").options[alreadySelectedCourseDate].selected = true;
           }
           if(alreadySelectedCourse!="0" && noOfCourseDates>0)
           {
       
           document.forms[0].courseDateSelected.disabled = false;
         
           }         

}
 

 function getMappingBody(tempobj)
 {      
// alert("in getMappingBody")  
  checkForFetch = "N";
  var index = tempobj.selectedIndex; 
  var which = tempobj.options[index].value;
  var sel = tempobj.options[index].innerHTML;        
  var indexE = document.getElementById("eventValue").selectedIndex;
  var whichE = document.getElementById("eventValue").options[indexE].innerHTML;
  var proId = document.getElementById("eventCourseSelectID").selectedIndex;
  var proSel = document.getElementById("eventCourseSelectID").options[proId].innerHTML;
   
   if(sel=="---Select---"){
   alert("Please select Course Start Date");
   return ;
   
   }
   var evalStartTime=document.getElementById("evaluationStartTime");
   var evaluationStartTimeTxt=evalStartTime.options[evalStartTime.selectedIndex].text;
   document.getElementById("hdn1").value=indexE;
   document.getElementById("hdn2").value=which;
   document.getElementById("hdn3").value=sel;
   document.getElementById("hdn4").value=whichE;
   document.getElementById("hdn5").value=checkForFetch;
   document.getElementById("hdn6").value=proSel;
   document.getElementById("hdn7").value=proId;
   document.getElementById("evalStartTime").value=evaluationStartTimeTxt;
   
 document.forms[0].action="getMappedTrainerLearners";
             // alert('Action-->'+document.forms[0].action);
 document.forms[0].submit();   
  
   }  
              
             
 function checkSelectAll(tempobj)
 {
    emailList="";
    var e=document.getElementById("selectToSendEmail");
    var selectedTrainerToSendEmail=e.options[e.selectedIndex].value;
    var found="false";
    if(selectedTrainerToSendEmail=="select"){
    alert("Please select Trainer from Send Email To dropdown");
     document.getElementById("selectAllCheckBox").checked=false;
    return ;
    }
 //alert("checkSelectAll");
 if(tempobj.checked==true)
    {
    //alert("in if");
    <%if(mapping!=null){%>
    
    var map = <%=mapping.length%>
     for (var i=0; i<map; i++)
     {
   if(document.getElementById("mapRow_"+i+i)!=null){
     if(document.getElementById("mapRow_"+i+i).value==selectedTrainerToSendEmail){
     found="true";
     document.getElementById("mapRow_"+i+i).checked = true;
     document.getElementById("mapRow_"+i).checked = true;
     
     var emailing = document.getElementById("mapRow_"+i).value;
   
     if(emailList=="")
       {
           emailList = emailing;  
            //alert(emailList);
        }
    else
       {
           emailList = emailList + ";" + emailing;  
              //alert(emailList);
       }
     
     }
     }
        }
         if(found=="false"){
                alert("No mapping found for selected Trainer");
                document.getElementById("selectAllCheckBox").checked=false;
        }
 
        <%}%>
     }
    
 else
          {
          //alert("in else");
          <%if(mapping!=null){%>
    
    var map = <%=mapping.length%>
     for (var i=0; i<map; i++)
     {
     if(document.getElementById("mapRow_"+i+i)!=null){
     document.getElementById("mapRow_"+i).checked = false;
     document.getElementById("mapRow_"+i+i).checked = false;
     }
     emailList = "";
     // alert(emailList);
     }
        
        <%}%>
          
          }
          

             
 }
 
 
 
 function mannualMap()
 {
// alert("in manual");
 checkForFetch = "Y";
 
  var indexE = document.getElementById("eventValue").selectedIndex;
  var whichE = document.getElementById("eventValue").options[indexE].innerHTML;
  var indexC = document.getElementById("eventCourseSelectID").selectedIndex;
  var whichC = document.getElementById("eventCourseSelectID").options[indexC].innerHTML;
  var dateId = document.getElementById("courseDateSelected").selectedIndex;
  var dateName = document.getElementById("courseDateSelected").options[dateId].innerHTML;
  
  var evalStartTime=document.getElementById("evaluationStartTime");
        var evaluationStartTimeTxt=evalStartTime.options[evalStartTime.selectedIndex].text;
  
  document.getElementById("hdn1").value=indexE;
  document.getElementById("hdn4").value=whichE;
  document.getElementById("hdn7").value=indexC;
  document.getElementById("hdn6").value=whichC;
  document.getElementById("hdn5").value=checkForFetch;
  document.getElementById("hdn2").value=dateId;
  document.getElementById("hdn3").value=dateName;
  document.getElementById("evalStartTime").value=evaluationStartTimeTxt;
  
  document.forms[0].action="getMappedTrainerLearners"; /* .do?selEvalTemplate="+indexE+"&proId="+indexC+"&proSel="+whichC+"&hdnSelectedTemplate="+whichE+"&checkForFetch="+checkForFetch+"&courseId="+dateId+"&courseName="+dateName; */
  //alert('Action-->'+document.forms[0].action);
  document.forms[0].submit();
 
 }
         
 function saveMapping()
 {

//alert('save me');
 var indexE = document.getElementById("eventValue").selectedIndex;
 var whichE = document.getElementById("eventValue").options[indexE].innerHTML;
 var indexC = document.getElementById("eventCourseSelectID").selectedIndex;
 var whichC = document.getElementById("eventCourseSelectID").options[indexC].innerHTML;
 var indexD = document.getElementById("courseDateSelected").selectedIndex;
 var whichD = document.getElementById("courseDateSelected").options[indexD].innerHTML;
 var indexT = document.getElementById("trainer_Loc").selectedIndex;  
 var tLoc =  document.getElementById("trainer_Loc").options[indexT].value; 
 var indexTr = document.getElementById("trainerName").selectedIndex;  
 var tEmail =  document.getElementById("trainerName").options[indexTr].value; 
 var whichTrainer = document.getElementById("trainerName").options[indexTr].innerHTML;
//alert(whichTrainer);

 //alert('save me');
 
 whichTrainer=whichTrainer.trim();

 var TrainerFirstName = whichTrainer.split(",");

 TrainerFirstName=TrainerFirstName[1];
 
 
 var indexL = document.getElementById("learnerName").selectedIndex;
 
 var y =  document.getElementById("learnerName").options[indexL].value; 
 var arrLVal = y.split("/");
 var lEmail = arrLVal[0];
 var lLoc   = arrLVal[1];
// alert("lLoc---"+ lLoc);
 var whichL = document.getElementById("learnerName").options[indexL].innerHTML;
 
 whichL=whichL.trim();

 var LearnerFirstName = whichL.split(",");

 LearnerFirstName = LearnerFirstName[1];


if(TrainerFirstName == "---Select---" || LearnerFirstName=="---Select---" )
{
var delMsg='Please select both TRAINER and LEARNER';
 confirmDel = confirm(delMsg);
}
else{

var trainerEmail="";
var countJS=0;
<%if(count!=null){%>
countJS=<%=count.intValue()%>;
<%}%>
var trainerCountJS=0;
var particularTrainerCountJS=0;
var flag="false";
<%

	 if(trainerCount!=null){
	  if(trainerCount.length>0){
	 	for(int i=0;i<trainerCount.length;i++){
			TrainerLearnerMapping trainerCountObj=trainerCount[i];%>
          //alert("Temail"+tEmail);
           <%if(trainerCountObj!=null){%>
           trainerEmail='<%=trainerCountObj.getTrainerEmail()%>';
           trainerCountJS=<%=trainerCountObj.getTrainerCount().intValue()%>;
           <%
           if(mappingT!=null && mappingT.length>0){
               for(int j=0;j<mappingT.length;j++){
               	if(mappingT[j].getTrainerEmail().equalsIgnoreCase(trainerCountObj.getTrainerEmail())){
               
               %>
               particularTrainerCountJS=<%=mappingT[j].getTrainerCount().intValue()%>;
               <%
               	break;
   	 			}
   	 		}
               }
    	%>
          
           <%}%>
           
          // alert(trainerCountJS);
          // alert(countJS);
			if(tEmail!='' && tEmail==trainerEmail){
               flag="true";
               if((countJS<=trainerCountJS) || (particularTrainerCountJS<=trainerCountJS)){
					alert('Trainer is already mapped to maximum number of learners');
                   return;
				}
			}
           
		<%}%>
       //alert(flag);
		if(!(flag=="true")){
			if(!(countJS>0)){
				alert('Trainer is already mapped to maximum number of learners');
               return false;
			}
		}
	
<%
	}
}
 %>
 var evalStartTime=document.getElementById("evaluationStartTime");
       var evaluationStartTimeTxt=evalStartTime.options[evalStartTime.selectedIndex].text;
 document.getElementById("hdn1").value=indexE;
 document.getElementById("hdn4").value=whichE;
 document.getElementById("hdn7").value=indexC;
 document.getElementById("hdn6").value=whichC;
 document.getElementById("hdn5").value=indexD;
 document.getElementById("hdn2").value=whichD;
 document.getElementById("selTrainer").value=TrainerFirstName;
 document.getElementById("selLearner").value=LearnerFirstName;
 document.getElementById("trainerEmail").value=tEmail;
 document.getElementById("learnerEmail").value=lEmail;
 document.getElementById("lLoc").value=lLoc;
 document.getElementById("tLoc").value=tLoc;
 document.getElementById("evalStartTime").value=evaluationStartTimeTxt;
 
 document.forms[0].action="saveTrainerLearnerMap";/* ?selEvalTemplate="+indexE+"&proId="+indexC+"&proSel="+whichC+"&hdnSelectedTemplate="+whichE+"&selTrainer="+whichTrainer+"&selLearner="+whichL+"&trainerEmail="+tEmail+"&learnerEmail="+lEmail+"&lLoc="+lLoc+"&tLoc="+tLoc+"&courseId="+indexD+"&courseName="+whichD; */
  // alert('Action-->'+document.forms[0].action);
 document.forms[0].submit();
  }
 
 }                                      
         
      
      
function  deleteSelMapping()
{
 checkForFetch = "D"; 
 var delEmails = emailList;
  //alert(delEmails);
 document.getElementById("trainerEmail").value=delEmails;
  if(delEmails == "" )
    {
   var delMsg='Please select at least one record.';
  confirmDel = confirm(delMsg);
    }
 else{
  
  var indexE = document.getElementById("eventValue").selectedIndex;
  var whichE = document.getElementById("eventValue").options[indexE].innerHTML;
  var indexC = document.getElementById("eventCourseSelectID").selectedIndex;
  var whichC = document.getElementById("eventCourseSelectID").options[indexC].innerHTML;
  var indexD = document.getElementById("courseDateSelected").selectedIndex;
  var whichD = document.getElementById("courseDateSelected").options[indexD].innerHTML;
  
  var evalStartTime=document.getElementById("evaluationStartTime");
        var evaluationStartTimeTxt=evalStartTime.options[evalStartTime.selectedIndex].text;
  
  document.getElementById("hdn1").value=indexE;
  document.getElementById("hdn4").value=whichE;
  document.getElementById("hdn7").value=indexC;
  document.getElementById("hdn6").value=whichC;
  document.getElementById("hdn5").value=checkForFetch;
  document.getElementById("hdn2").value=indexD;
  document.getElementById("hdn3").value=whichD;
  document.getElementById("evalStartTime").value=evaluationStartTimeTxt;
  
  document.forms[0].action="getMappedTrainerLearners";//?selEvalTemplate="+indexE+"&proId="+indexC+"&proSel="+whichC+"&hdnSelectedTemplate="+whichE+"&checkForFetch="+checkForFetch+"&delEmails="+delEmails+"&courseId="+indexD+"&courseName="+whichD;
  //alert('Action-->'+document.forms[0].action);
  document.forms[0].submit();
     }

}   

    function uncheckChkBox(){
            var e=document.getElementById("selectToSendEmail");
            var selectedTrainerToSendEmail=e.options[e.selectedIndex].value;
            document.getElementById("selectAllCheckBox").checked=false;
            var checks=document.getElementsByName('sendMappingEmailChkBox1');
            for(var i=0;i<checks.length;i++){
            document.getElementById(checks[i].id).checked=false;
            document.getElementById(checks[i].id+checks[i].id.substring(7)).checked=false;
            }
            var indexE = document.getElementById("eventValue").selectedIndex;
            var index = document.getElementById("courseDateSelected"); 
            var which = index.options[index.selectedIndex].value;
            var sel = index.options[index.selectedIndex].text;        
            var indexE = document.getElementById("eventValue").selectedIndex;
            var whichE = document.getElementById("eventValue").options[indexE].innerHTML;
            var proId = document.getElementById("eventCourseSelectID").selectedIndex;
            var proSel = document.getElementById("eventCourseSelectID").options[proId].innerHTML;
        
            //Changed 22nd aug
            var evalStartTime=document.getElementById("evaluationStartTime");
            var evaluationStartTimeTxt=evalStartTime.options[evalStartTime.selectedIndex].text; 
            document.getElementById("hdn1").value=indexE;
            document.getElementById("hdn2").value=which;
            document.getElementById("hdn3").value=sel;
            document.getElementById("hdn4").value=whichE;
            document.getElementById("sendEmailTo").value=selectedTrainerToSendEmail;
            document.getElementById("hdn6").value=proSel;
            document.getElementById("hdn7").value=proId;
            document.getElementById("evalStartTime").value=evaluationStartTimeTxt;
           
            document.forms[0].action="getMappingsBasedOnTrainer"; /* .do?selEvalTemplate="+indexE+"&courseId="+which+"&courseName="+sel+"&hdnSelectedTemplate="+whichE+"&proSel="+proSel+"&proId="+proId+"&sendEmailTo="+selectedTrainerToSendEmail+"&evalStartTime="+evaluationStartTimeTxt; */
            document.forms[0].submit();
}
      
      function enableDisableEmailChkBox()
{
var e=document.getElementById("selectToSendEmail");
var selectedTrainerToSendEmail=e.options[e.selectedIndex].value;

   var found="false";
    var checks=document.getElementsByName('sendMappingEmailChkBox');
      for(var i=0;i<checks.length;i++){
            if(document.getElementById(checks[i].id).checked){
            found="true";
            if(selectedTrainerToSendEmail=="select"){
                alert("Please select Trainer from Send Email To dropdown");
                    return checks[i].id;
            }else{
                if(!(checks[i].value==selectedTrainerToSendEmail)){
                alert("Please select correct Guest Trainer");
                return checks[i].id;
                }
               
                }
                }
                 }
                
                 return "true";
                
        }
      
      
         
 function addCheckForEmail(tempObj) 
{
var checks=document.getElementsByName('sendMappingEmailChkBox1');
      for(var i=0;i<checks.length;i++){
      var id=checks[i].id+checks[i].id.substring(7);
                  if(document.getElementById(checks[i].id).checked){
              document.getElementById(id).checked=true;
              }else{
              document.getElementById(id).checked=false;
              }
      }

var result=enableDisableEmailChkBox();
if(!(result=="true")){
document.getElementById(result.substring(0,8)).checked=false;
document.getElementById(result).checked=false;
return ;
}

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
//    alert(emailList);
     }
  }

 else
 { 
     var pos = emailList.indexOf(email + ";");
   var pos2 = emailList.indexOf(email);
   

   
   if(pos2 != -1 && pos == -1)
   {
    var txt = emailList.replace(";"+email,"");
    emailList = txt;
 //  alert("pos2 != -1......" + emailList);
   }
   else if(pos != -1)
   {
   var replaceEmail = email + ";";
    var txt = emailList.replace(replaceEmail,"");
     emailList = txt;
  //  alert("pos != -1......" + emailList);
   }
   else
   {
  // alert("something fishi");
   }
 
 }
 }


function getTLocation(temp)
{
var index = temp.selectedIndex;  
var which = temp.options[index].value;      

var i=1;
 //alert("out loop");
 <%
  TrainerLearnerMapping objUser6 = null;
                    if (mappingT != null) {
                        for (int i=0; i<mappingT.length; i++) {   
                             // System.out.println("___________"+i);   
                            objUser6 = mappingT[i];   %>
  if(which=='<%=objUser6.getTrainerLoc()%>')
  {
 // alert("in if  i=" + i);
  document.getElementById("trainerName").disabled = false;  
      document.getElementById("trainerName").options[i].value = '<%=objUser6.getTrainerEmail()%>';
  document.getElementById("trainerName").options[i].innerHTML = '<%=objUser6.getTrainerName()%>';  
 
//  alert("___innerHTml"+ document.getElementById("trainerName").options[i].innerHTML); 
 // alert("___value"+ document.getElementById("trainerName").options[i].value); 
  i=i+1;
  }
  else{
  //alert(which + "in else");
  }
 <%}}%>
    
}
function getlLocation(temp)
{
 var index = temp.selectedIndex;  
 var which = temp.options[index].value;      
 var arrVal= which.split("/");  
 document.getElementById("learner_Loc").value = arrVal[1];    
}

function submitEmail()
{
if(document.getElementById("selectToSendEmail")!=null){
var e=document.getElementById("selectToSendEmail");
var selectedTrainerToSendEmail=e.options[e.selectedIndex].value;
var flag="false";

<%-- var noOfWebEx=<%=noOfWebExDetails%>;
<%for(int i=0;i<noOfWebExDetails;i++){
    if(webexdetails!=null){
    WebExDetails webobj=webexdetails[i];%>
    var selectedTrainer='<%=webobj.getUsedby()%>';
   // alert(selectedTrainer);
    if(selectedTrainerToSendEmail!=""){
    if(selectedTrainer==selectedTrainerToSendEmail){
    flag="true";
    }
    else{
    if(selectedTrainer !="null"&&selectedTrainer!="")
    {
    noOfWebEx=noOfWebEx-1;
    //alert(noOfWebEx);
    }
    }
    }
    <%}
}%> --%>

}
var evalTime=document.getElementById("evaluationStartTime");
var evalTimeVal=evalTime.options[evalTime.selectedIndex].value;
var toEmail = emailList;

/*Begin DEEPAK For removing error of last check box  */
 if(toEmail.charAt(toEmail.length-1)==";"){
	toEmail=toEmail.substring(0,toEmail.length-1);
}
 /*END DEEPAK For removing error of last check box  */
var indexC = document.getElementById("eventCourseSelectID").selectedIndex;
var proName = document.getElementById("eventCourseSelectID").options[indexC].innerHTML;
if(toEmail == "" )
    {
   var delMsg='Please select at least one record.';
  confirmDel = confirm(delMsg);
    }
 else
 {
	 
/*  if(flag=="false" && noOfWebEx==0){
alert("All WebEx details for this event are used.Please add more WebEx Details");
return ;
} */

var e=document.getElementById("courseDateSelected");
var selectedCourseStartDate=e.options[e.selectedIndex].text;
toEmail=toEmail+";"+selectedTrainerToSendEmail;
var indexE = document.getElementById("eventValue").selectedIndex;
  var eventName = document.getElementById("eventValue").options[indexE].innerHTML;
   window.open('evaluation/sendEmailTemplateThree.jsp?toEmail='+toEmail+'&eventName='+ eventName +'&productName='+ proName+'&trainerEmail='+ selectedTrainerToSendEmail+'&courseStartDate='+ selectedCourseStartDate+'&evalStartTime='+ evalTimeVal,'cnt_window','status=yes,scrollbars=yes,height=500,width=900,resizable=yes');                    
}
}
<%-- function submitEmail()
{
if(document.getElementById("selectToSendEmail")!=null){
var e=document.getElementById("selectToSendEmail");
var selectedTrainerToSendEmail=e.options[e.selectedIndex].value;
//alert(selectedTrainerToSendEmail);
var flag="false";
var noOfWebEx=<%=noOfWebExDetails%>;
//alert(noOfWebEx);
<%for(int i=0;i<noOfWebExDetails;i++){
    if(webexdetails!=null){
    WebExDetails webobj=webexdetails[i];%>
    var selectedTrainer='<%=webobj.getUsedby()%>';
    //alert(selectedTrainer);
    if(selectedTrainerToSendEmail!=""){
    if(selectedTrainer==selectedTrainerToSendEmail)
    {
    flag="true";
   // alert(flag);
    }
    else{
    if(selectedTrainer !="null"&&selectedTrainer!="")
    {
    alert('inside else');
    noOfWebEx=noOfWebEx-1;
    //alert(noOfWebEx);
    }
    }
    }
    <%}
}%>
}

var toEmail = emailList;
var indexC = document.getElementById("eventCourseSelectID").selectedIndex;
var proName = document.getElementById("eventCourseSelectID").options[indexC].innerHTML;
if(toEmail == "" )
    {
   var delMsg='Please select at least one record.';
  confirmDel = confirm(delMsg);
    }
 else
 {
 if(flag=="false" && noOfWebEx==0){
alert("All WebEx details for this event are used.Please add more WebEx Details");
return ;
}
var e=document.getElementById("courseDateSelected");
var selectedCourseStartDate=e.options[e.selectedIndex].text;
toEmail=toEmail+";"+selectedTrainerToSendEmail;
var indexE = document.getElementById("eventValue").selectedIndex;
  var eventName = document.getElementById("eventValue").options[indexE].innerHTML;
  window.open('sendEmailTemplateThree.jsp?toEmail='+toEmail+'&eventName='+ eventName +'&productName='+ proName+'&trainerEmail='+ selectedTrainerToSendEmail+'&courseStartDate='+ selectedCourseStartDate+'&evalStartTime='+ evalTimeVal,'cnt_window','status=yes,scrollbars=yes,height=500,width=900,resizable=yes');                     
}
} --%>

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
    <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/validate.js"></script>
    <!--<link href="<%=request.getContextPath()%>/evaluation/resources/_css/admin.css" rel="stylesheet" type="text/css" media="all" />--> 
    <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/sorttable.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/validate.js"></script>  
</head>
                         
            <body  onload="onScreenLoad()">
          
                <s:form action="trainerLearnerMapping" tagId="TrainerLearnerMapping">
                <input type="hidden" name="pageName" id="pageName" value="retrieveMappingDetails"/>
                <input type="hidden" id="hdnSelectedTempId" name="hdnSelectedTemp" value=""/>
                <input type="hidden" id="hdn1" name="hdn1" value=""/>
                <input type="hidden" id="hdn2" name="hdn2" value=""/>
                <input type="hidden" id="hdn3" name="hdn3" value=""/>
                <input type="hidden" id="hdn4" name="hdn4" value=""/>
                 <input type="hidden" id="hdn5" name="hdn5" value=""/>
                  <input type="hidden" id="hdn6" name="hdn6" value=""/>
                   <input type="hidden" id="hdn7" name="hdn7" value=""/>
                   <input type="hidden" id="sendEmailTo" name="sendEmailTo" value=""/>
                   <input type="hidden" id="evalStartTime" name="evalStartTime" value=""/>
                   <input type="hidden" id="selTrainer" name="selTrainer" value=""/>
                   <input type="hidden" id="selLearner" name="selLearner" value=""/>
                   <input type="hidden" id="trainerEmail" name="trainerEmail" value=""/>
                   <input type="hidden" id="learnerEmail" name="learnerEmail" value=""/>
                   <input type="hidden" id="lLoc" name="lLoc" value=""/>
                   <input type="hidden" id="tLoc" name="tLoc" value=""/>                  
                <input type="hidden" id="hdnSelectedTemplateId" name="hdnSelectedTemplate" value=""/>
                <input type="hidden" id="hdnAlMapActPk" name="alreadyMappedActPk" value=""/>
                <input type="hidden" id="hdnSavableRecsId" name="hdnSavableRecs" value=""/>
                <!--<input type="text" id="hdnAlMapCrsNm" name="alreadyMappedCrsNm" value="hdnCN">
                <input type="text" id="hdnAlMapCrsCd" name="alreadyMappedCrsCd" value="hdnCC">-->
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
                        <!-- end #eval_sub_nav -->
			                    
			                    <H3>Admin: Trainer to Learner Mapping</H3>
			                    
			                    
			                    <br>
			                    
			                    <br>
			                    <table cellpadding="0" cellspacing="0" style="border:0;colour:'white';">
			                                  <tr>
			                                    <td style="border:0;"></td>
			                                        <td style="border:0;colour:'white';">
			                                        <b>Event Name </b></td>
			                                          <td style="border:0;"></td>
			                                        <td  style="border:0;colour:'white';"> 
			                                        <b>Product</b></td>
			                                         <td style="border:0;"></td>
			                                        <td  style="border:0;colour:'white';"> 
			                                        <b>Course Start Date</b></td>
			                                        <td style="border:0;"></td>
			                                        <td  style="border:0;colour:'white';"> 
			                                        <b>Evaluation Start Time</b></td>
			                                        </tr>
			                          <tr>  
			                            <td style="border:0;"></td>
			                          <td  width="15%" valign="top" style="border:0px; padding:0px;" >
			                            <select id="eventValue" style="border:.07em solid grey;margin:0;padding:0;" onchange="getCourse(this);" >
			                             <option value="0">---Select---</option>
			                            <% if (eventNameFetch != null) {
			                        for (int i=0; i<eventNameFetch.length; i++) {                                
			                                     %>
			                               
			                              <option value="<%=i+1%>"><%=eventNameFetch[i]%></option>
			                              <%  
			                                 }}
			                               %>
			
			                                   </select>
			                        
			                        </td>
			                        <td style="border:0;"></td>
			               <td valign="top" width="15%" class="evalmappingtd">
			               <select id="eventCourseSelectID" style="border:.07em solid grey;margin:0;padding:0;   font-size:0.9em; "  name="eventCourseSelectName"   onchange="getCourseDates(this);"  disabled >
			                            <option value="0">---Select---</option>
			                     
			 
			                           
			                            <% if (eventCourseFetch != null) {
			                             for (int i=0; i<eventCourseFetch.length; i++) {                                
			                         %>
			                               
			                            <option value="<%=i+1%>"><%=eventCourseFetch[i]%></option>
			                              <%  
			                              }}
			                               %>
			
			                                   </select>
			               </td>  <td style="border:0;"></td>
			                <td  width="15%" valign="top" style="border:0px; padding:0px;" >
			                            <select id="courseDateSelected" style="border:.07em solid grey;margin:0;padding:0;"  onchange="getMappingBody(this);" disabled>
			                             <option value="0">---Select---</option>
			                            <% if (fetchDate != null) {
			                        for (int i=0; i<fetchDate.length; i++) {  
			                                  String[] x =     fetchDate[i].split(" ");
			                                  String dateVal  = x[0];        
			                                  // System.out.println("!!!!!!!!!!!!!!!!!!!!!!"+dateVal);                              
			                                     %>
			                               
			                              <option value="<%=i+1%>"><%=dateVal%></option>
			                              <%  
			                                 }}
			                               %>
			
			                                   </select>
			                        
			                        </td>
			                </td>  <td style="border:0;"></td>
			               <td style="border:0px; padding:0px;">
			               <select id="evaluationStartTime" style="border:.07em solid grey;margin:0;padding:0;">               
			               <%for(int i=0;i<evalStartTime.length;i++){
			                if(evalStartTime[i].equalsIgnoreCase(evalStartTimeSel)){%>
			                    <option value="<%=evalStartTimeVal[i]%>" selected><%=evalStartTime[i]%></option>
			                <%}else{%>
			               <option value="<%=evalStartTimeVal[i]%>"><%=evalStartTime[i]%></option>
			               <%}}%>
			               </select>
			               </td>
			               
			           <td style="border:0;" width="45%"></td>    </tr>  
			                         </table>
			                 
			                 
			                 
			                 <div>
			            <%if(fetchDate!=null&&fetchDate.length==0 && !alreadySelectedCourse.equalsIgnoreCase("0")){%>
			          <br>
			          <font style="color:red; font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';">No Learners available for selected Event and Product combination</font>
			           <%}%>
			            <%if( alreadySelectedCourseDate==null){%>
			            <br>
			            <%}else{%>
			            
			           <br>
			             <table cellpadding="0" cellspacing="0" style="border:0; colour:'white';">
			             <tr> <td width="2%" style="border:0; "></td>   <td style="border:0;">
			             <input align="right" type="button" class="buttonStylesWhite" style="width:160px; align:left; border:0px; padding:0px; font-size:0.9em;" value="Manually Map Trainer-Learner > " onclick="mannualMap()">
			             
			             </td></tr>
			             </table>
			             
			             
			    
			                    <%}%>
			                   
			           <%if(mappingT==null || mappingL==null  ){%>
			           <br>
			           <%}else{%>
			            <table cellpadding="0" cellspacing="0" style="border:0;colour:'white';">
			           <tr>
			                       <td width="2.8%" style="border:0; "></td>          
			                                        <td  style="border:0;colour:'white';">
			                                        <b>Trainers Location </b></td>
			                                            <td width="2.8%"  style="border:0;"></td>
			                                             <td  style="border:0;colour:'white';">
			                                        <b>Trainers </b></td>
			                                            <td width="2.8%"  style="border:0;"></td>
			                                        <td  style="border:0;colour:'white';"> 
			                                        <b>Learners</b></td>
			                                         <td width="2.8%"  style="border:0;"></td>
			                                             <td  style="border:0;colour:'white';">
			                                        <b>Learner Location </b></td>
			                                        </tr>
			           <tr>
			             <td width="2.8%"  style="border:0;"></td>
			           <td   width="15%" valign="top" style="border:0px; padding:0px;" >
			                            <select id="trainer_Loc"  style="border:.07em solid grey;margin:0;padding:0; width:105px;" onchange="getTLocation(this);" >
			                             <option value="0">---Select---</option>
			                             <%
			                    TrainerLearnerMapping objUser1 = null;
			                    if (mappingLOC != null) {
			                        for (int i=0; i<mappingLOC.length; i++) {   
			                            objUser1 = mappingLOC[i];                                                      
			                    %>
			                               
			           <option  value="<%=objUser1.getTrainerLoc()%>"> <%=objUser1.getTrainerLoc()%></option>
			           
			            
			                              <%  
			                        }}
			                               %>
			                                   </select>
			                        
			                        </td>
			                         <td style="border:0;"></td>
			                         <td style="border:0;">
			                         <select id="trainerName"  style="border:.07em solid grey;margin:0;padding:0; " disabled  >
			                             <option value="0">---Select---</option>
			                        
			                                <%                       
			                    if (mappingT != null) {
			                        for (int i=0; i<mappingT.length; i++) {   
			                            %>
			                             <option  value=""> </option>
			           
			                                <%}}%>
			                                   </select>
			                          
			                         
			                         
			                         
			          
			                         
			                         
			                         </td>
			                             <td style="border:0;"></td>
			                        <td  width="15%" valign="top" style="border:0px; padding:0px;" >
			                            <select id="learnerName" style="border:.07em solid grey;margin:0;padding:0;" onchange="getlLocation(this);" >
			                             <option value="0">---Select---</option>
			                            <%
			                    TrainerLearnerMapping objUser2 = null;
			                    if (mappingL != null) {
			                        for (int i=0; i<mappingL.length; i++) {   
			                            objUser2 = mappingL[i];                                                      
			                    %>
			                               
			           <option  value="<%=objUser2.getLearnerEmail()%>/<%=objUser2.getLearnerLoc()%>"> <%=objUser2.getLearnerName()%></option>
			           
			           
			                              <%  
			                        }}
			                               %>
			                                   </select>
			                        
			                        </td>
			                        <td style="border:0;"></td>
			                         <td style="border:0;"><input readonly id="learner_Loc" type="text"  value=""></td>
			                         <td width="4%" style="border:0;"></td>
			                        <td style="border:0px; padding:0px;"> <img id="imgSaveMapping" name="imgSaveMappingName" src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save.gif" align="left"  style="width:36px; height:19px" onclick="saveMapping()" alt="Save Mapping"></td>
			           </tr>
			           </table>
			           <%}%>
			           
			           <%if(recordAlreadyPresent == null || recordAlreadyPresent.equalsIgnoreCase("0")){%>
			           <br>
			           <%}else if(recordAlreadyPresent.equalsIgnoreCase("P")){%>
			            <table cellpadding="0" cellspacing="0" style="border:0;colour:'white';">
			                        <tr>
			                        <td width="2.8%"  style="border:0;"></td>    <td style="weight:bold; " class="evalmappingtd">
			                        <font style="color:red; font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';">Selected Trainer-Learner Mapping is already present. </font> </td>                    
			                        </tr>
			                    </table>
			           <%}else{%>
			           <br>
			           <%}%>
			            </div>
			                    
			                <%if(mapping!=null) 
			                {%>
			                     <div align="center">
			                        <hr>
			                    </div>
			                 
			                <table cellpadding="0" cellspacing="0" style="border:0;colour:'white';">
			                        <tr>
			                            <td style="weight:bold;font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';"class="evalmappingtd">Following Trainer(s) and Learner(s) are mapped based on location </td>
			                            <%if(mappedTrainerName!=null && mappedTrainerName.size()>0){%>
			                            <td style="weight:bold;font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';"class="evalmappingtd">Send Email To</td>
			                            <td style="weight:bold;font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';"class="evalmappingtd">
			                            <select id="selectToSendEmail" name="selectToSendEmail" onchange="uncheckChkBox()">
			                            <option value="select">--Select--</option>
			                            <%for(int i=0;i<mappedTrainerName.size();i++){
			                                if(mappedTrainerEmail.get(i).toString().equalsIgnoreCase(sendEmailTo))    {
			                            %>
			                            <option value="<%=mappedTrainerEmail.get(i)%>" selected><%=mappedTrainerName.get(i)%></option>
			                            <%}else{%>
			                            <option value="<%=mappedTrainerEmail.get(i)%>"><%=mappedTrainerName.get(i)%></option>
			                            <%}}%>
			                            </select>
			                            </td>
			                            <%}%>
			                        </tr>
			                    </table>
			                    <table cellpadding="0" cellspacing="0" style="border:0;colour:'white';">
			                        <tr>
			                            <th width="10%" align="left" class="sort_up" onclick="ts_resortTable(this, 0);return false;"><b>Trainer Name</b></th>
			                            <th width="10%" align="left" ><b>Trainer Location</b></th>
			                            <th width="30%" align="left" ><b>Trainer Email</b></th>
			                            <th width="10%" align="left" class="sort_up" onclick="ts_resortTable(this, 3);return false;"><b>Learner Name</b></th>
			                            <th width="10%" align="left" ><b>Learner Location</b></th>
			                            <th width="30%" align="left" ><b>Learner Email</b></th>
			                            <th width="5%" align="left" ><b> <input id="selectAllCheckBox" align="middle" type="checkbox" onclick="checkSelectAll(this)" ></b></th>
			                      </tr>
			                        <%
			                    TrainerLearnerMapping objUser = null;
			                    if (mapping != null) {
			                        for (int i=0; i<mapping.length; i++) {   
			                            objUser = mapping[i];                                                      
			                            if(sendEmailTo==null ||sendEmailTo.equalsIgnoreCase("select")){
			                    %>
			                    
			                      <tr>
			                        <td  align="left" id="eventRow"><%=objUser.getTrainerName()%></td>
			                        <td  align="left" id="eventRow"><%=objUser.getTrainerLoc()%></td>
			                           <td  align="left" id="eventRow"><%=objUser.getTrainerEmail()%></td>
			                              <td  align="left" id="eventRow"><%=objUser.getLearnerName()%></td>
			                                <td  align="left" id="eventRow"><%=objUser.getLearnerLoc()%></td>
			                                 <td  align="left" id="eventRow"><%=objUser.getLearnerEmail()%></td>
			                                 <%if(objUser.getEmailSent()!=null&&objUser.getEmailSent().equalsIgnoreCase("Y")){%>
			                                 <td  align="left" ><input id="mapRow_<%=i%>" align="middle" type="checkbox" value="<%=objUser.getLearnerEmail()%>" disabled>
			                                 </td>
			                                 <%}else{%>
			                                    <td  align="left" ><input id="mapRow_<%=i%>" align="middle" type="checkbox" value="<%=objUser.getLearnerEmail()%>" onClick="addCheckForEmail(this)" name="sendMappingEmailChkBox1">
			                                                        <input id="mapRow_<%=i%><%=i%>" align="middle" type="checkbox" value="<%=objUser.getTrainerEmail()%>" onClick="addCheckForEmail(this)" name="sendMappingEmailChkBox" style="display:none"/>
			                                    </td>
			                       <%}%>
			                       
			                     
			                        </tr>
			                         <%
			                        }else if(sendEmailTo!=null && sendEmailTo.equalsIgnoreCase(objUser.getTrainerEmail())){%>
			                                             <tr>
			                        <td  align="left" id="eventRow"><%=objUser.getTrainerName()%></td>
			                        <td  align="left" id="eventRow"><%=objUser.getTrainerLoc()%></td>
			                           <td  align="left" id="eventRow"><%=objUser.getTrainerEmail()%></td>
			                              <td  align="left" id="eventRow"><%=objUser.getLearnerName()%></td>
			                                <td  align="left" id="eventRow"><%=objUser.getLearnerLoc()%></td>
			                                 <td  align="left" id="eventRow"><%=objUser.getLearnerEmail()%></td>
			                                 <%if(objUser.getEmailSent()!=null&&objUser.getEmailSent().equalsIgnoreCase("Y")){%>
			                                 <td  align="left" ><input id="mapRow_<%=i%>" align="middle" type="checkbox" value="<%=objUser.getLearnerEmail()%>" disabled>
			                                 </td>
			                                 <%}else{%>
			                                    <td  align="left" ><input id="mapRow_<%=i%>" align="middle" type="checkbox" value="<%=objUser.getLearnerEmail()%>" onClick="addCheckForEmail(this)" name="sendMappingEmailChkBox1">
			                                                        <input id="mapRow_<%=i%><%=i%>" align="middle" type="checkbox" value="<%=objUser.getTrainerEmail()%>" onClick="addCheckForEmail(this)" name="sendMappingEmailChkBox" style="display:none"/>
			                                    </td>
			                       <%}%>
			                       
			                     
			                        </tr>
			                        <%
			                        }
			                        }
			                    }
			                    %> 
			                     </table>  
			                     
			                     <input type="button" class="buttonStyles"  align="middle" value="Send Email" onclick="submitEmail()"    style="width:100px; font-size:0.9em;">
			           
			                        <input type="button" class="buttonStyles" value="Delete Mapping" style="width:100px; font-size:0.9em;" onclick="deleteSelMapping()" >
			                        
			             <%}else{%>
			                   <br>
			                     <%}%>
			            
			            
			            
			            
			            
			                
                </div><!-- end #wrap -->
                </s:form>
            </body>
        </html>