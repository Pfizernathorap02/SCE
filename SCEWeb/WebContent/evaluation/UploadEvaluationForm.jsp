<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@include file="IAM_User_Auth.jsp" %>
<%
Integer templateVersionId=new Integer(0);

if(request.getParameter("templateVersionId")!=null){
templateVersionId=new Integer(request.getParameter("templateVersionId"));


pageContext.setAttribute("templateVersionId",templateVersionId);

}
%>


<%

SCEManagerImpl sceManagerImpl = new SCEManagerImpl();
DateFormat dateFormatter = new SimpleDateFormat(SCEUtils.DEFAULT_DATE_FORMAT);
HashMap scoreList = new HashMap(); 
scoreList = sceManagerImpl.getAllScores(templateVersionId); 
request.setAttribute("scoreList",scoreList);

%>
<script language="javascript">

        function CheckFile(){ 
            var evaluationDate = document.getElementById('evaluationDate').value; 
            if (evaluationDate == '') { 
                alert('Please select the evaluation date');
                document.getElementById('evaluationDate').focus();
                return false;
            }     
            var file = document.getElementById('Uploadfile');       
            var len=file.value.length;    
            var ext=file.value;        
            if(len <= 0){
                alert('Please select a file to upload');        
                return false;
            }
            else if(ext.substr(len-3,len)!="pdf" ){
                alert("Please select a pdf file ");
                return false;
            }
        
            var check= validateTimeFormat();
            if(check==true){
            	validateDate();
            }
        }
        
        function validateDate(){
        	 
            var evaluationDateMandatory = document.getElementById('evaluationDate').value;   
            var arrDate= evaluationDateMandatory.split("/");
            var useDate = new Date(arrDate[2], arrDate[0]-1, arrDate[1]);
            var today = new Date();
            var today1 = new Date(); 
            today.setHours(00);
            today.setMinutes(00);
            today.setSeconds(00);
            today.setMilliseconds(00);
          
            if (useDate > today ){
                alert("The Evaluation Date cannot be a future date. Please provide todays's Date or a past Date");
                return false;
            }    
            
            if(useDate < today){
            	//do nothing
            }
            else{	
            	var strval = document.getElementById("evaluationTime").value;
                var strval1 = trimAllSpace(strval);
                var length=strval1.length;                                              
                strval =  strval1.substring(0,strval1.length - 2);
                strval = strval + ' ' + strval1.substring(strval1.length - 2,strval1.length)
                strvall = strval;
                var pos1 = strvall.indexOf(':');
				var horval =  trimString(strval1.substring(0,pos1));
                //Checking minutes.
                var minval =  trimString(strval1.substring(pos1+1,pos1 + 3));
                        if( strval1.charAt(strval1.length - 2) == 'p' || strval1.charAt(strval1.length - 2) == 'P'){
                        	if(horval=='12'){
                         		var format='N';
                          	}
                        	else{
                        		var format='Y';
                        	}
                        }
                        else{
                        	if(horval=='12'){
                        	horval='00';
                        	}
                         	var format='N';
                         
                        }
                         // jus added strts
                        if(format=='Y'){
                       		var horVal= parseInt(horval,10);
                       		var hours=horVal+12;
                        }
                        else if (format=='N'){ 
                        	var hours=horval;
                        }
                    	var todayHours=today1.getHours();
                     	if(hours>todayHours){
                       		alert("Future time cannot be entered.Please enter another time");
                       		return false;
                       }else if(hours==todayHours){
                       		var todayMinutes=today1.getMinutes();
                         	if(minval>todayMinutes){
                          		alert("Future time cannot be entered.Please enter another time");
                          		return false;
                            }
                       }                           
            }
            
            //2020:Q3,Q4:MUZEES: Registration, Previous Evaluation Date&Time validations
          	var evaluationDate= evaluationDateMandatory.split("/");
           	var evaluationTime = document.getElementById("evaluationTime").value;
            var trimEvalTime = trimAllSpace(evaluationTime);
            var hoursMin =  trimEvalTime.substring(0,trimEvalTime.length - 2);
            var position = hoursMin.indexOf(':');
            var evalHours =  hoursMin.substring(0,position);
            var evalminutues =  hoursMin.substring(position+1,hoursMin.length);
            if( trimEvalTime.charAt(trimEvalTime.length - 2) == 'p' || trimEvalTime.charAt(trimEvalTime.length - 2) == 'P'){
                    if(evalHours!=12){
                    	evalHours=parseInt(evalHours,10)+12;
                    }
            }
            else{
                    if(evalHours=='12'){
                    	evalHours='00';
                    }
                    
            }
            var evalDateTime = new Date(evaluationDate[2], evaluationDate[0]-1, evaluationDate[1],evalHours,evalminutues,00,00);
            var regDateTimeString = document.getElementById('registrationDate').value;
            var arrRegDateTime= regDateTimeString.split(" ");
           	var arrRegDate=arrRegDateTime[0].split("-");
           	var arrRegTime=arrRegDateTime[1].split(":");
           	var regDateTime=new Date(arrRegDate[0],arrRegDate[1]-1,arrRegDate[2],arrRegTime[0],arrRegTime[1],00,00);
           	if(evalDateTime<=regDateTime){
           		alert("Evaluation Date and Time should be greater than Registration Date and Time");
           		return false;
           	}
           	var prevEvalDateTimeString = document.getElementById('previousEvaluationDate').value;
           	if(prevEvalDateTimeString!=""){
            	var arrPrevDateTime= prevEvalDateTimeString.split(" ");
               	var arrPrevDate=arrPrevDateTime[0].split("-");
               	var arrPrevTime=arrPrevDateTime[1].split(":");
               	var prevDateTime=new Date(arrPrevDate[0],arrPrevDate[1]-1,arrPrevDate[2],arrPrevTime[0],arrPrevTime[1],00,00);
               	if(evalDateTime<=prevDateTime){
               		alert("Evaluation Date and Time should be greater than Previous Evaluation Date and Time");
               		return false;
               	}
            }
            //end of Muzees
            window.document.forms[0].submit();
            return true;
                                                          
        }
        
   function  validateTimeFormat(){
  		var strval = document.getElementById("evaluationTime").value;   
  		var strval1;
    	//minimum lenght is 6. example 1:2 AM
  		if(strval.length < 6){
   			alert("Invalid time. Time format should be HH:MM AM/PM.");
   			return false;
  		}
  
  		//Maximum length is 8. example 10:45 AM
  		if(strval.length > 8){
   			alert("Invalid time. Time format should be HH:MM AM/PM.");
   			return false;
  		}
   		//Removing all space
  		strval = trimAllSpace(strval); 
  		
   		//Checking AM/PM
  		if(strval.charAt(strval.length - 1) != "M" && strval.charAt(strval.length - 1) != "m"){
  			 alert("Invalid time. Time should be end with AM or PM.");
   			 return false;
   		}
		else if(strval.charAt(strval.length - 2) != 'A' && strval.charAt(strval.length - 2) != 'a' && strval.charAt(strval.length - 2) != 'p' && strval.charAt(strval.length - 2) != 'P')
  		{
   			alert("Invalid time. Time should be end with AM or PM.");
   			return false;   
  		}
		//Give one space before AM/PM
  
  		strval1 =  strval.substring(0,strval.length - 2);
  		strval1 = strval1 + ' ' + strval.substring(strval.length - 2,strval.length);
  
  		strval = strval1;
  		
  		document.getElementById("evaluationTime").value = strval;
  		var pos1 = strval.indexOf(':');
  		
  		if(pos1 < 0 ){
   			alert("Invlalid time. A colon(:) is missing between hour and minute.");
   			return false;
  		}
  		else if(pos1 > 2 || pos1 < 1){
   			alert("Invalid time. Time format should be HH:MM AM/PM.");
   			return false;
  		}

		//Checking hours
  		var horval =  trimString(strval.substring(0,pos1));
   
  		if(horval == -100)
  		{
  			alert("Invalid time. Hour should contain only integer value (0-11).");
   			return false;
  		}
  		
  		if(horval > 12){
  			alert("Invalid time. Hour cannot be greater that 12.");
   			return false;
  		}
  		else if(horval < 0){
   			alert("Invalid time. Hour cannot be hours less than 0.");
  			return false;
  		}
  		//Completes checking hours
   		//Checking minutes.
  		var minval =  trimString(strval.substring(pos1+1,pos1 + 3));
  
  		if(minval == -100){
  			alert("Invalid time. Minute should have only integer value (0-59).");
   			return false;
  		}
    
  		if(minval > 59){
     		alert("Invalid time. Minute cannot be more than 59.");
     		return false;
  		}   
  		else if(minval < 0){
   			alert("Invalid time. Minute cannot be less than 0.");
   			return false;
  		}
   
  		//Checking minutes completed.
	
 		//Checking one space after the mintues 
  		minpos = pos1 + minval.length + 1;
  		if(strval.charAt(minpos) != ' '){
   			alert("Invalid time. Space missing after minute.Time should have HH:MM AM/PM format.");
   			return false;
  		}
  		return true;
 
}

function trimAllSpace(str) 
{ 
    var str1 = ''; 
    var i = 0; 
    while(i != str.length) 
    { 
        if(str.charAt(i) != ' ') {
            str1 = str1 + str.charAt(i); 
        }
        	i ++; 
    } 
    return str1; 
}

function trimString(str) 
{ 
     var str1 = ''; 
     var i = 0; 
     while ( i != str.length) 
     { 
         if(str.charAt(i) != ' ') str1 = str1 + str.charAt(i); i++; 
     }
     var retval = IsNumeric(str1); 
     if(retval == false) 
         return -100; 
     else 
         return str1; 
}
function IsNumeric(strString) 
{ 
    var strValidChars = "0123456789"; 
    var strChar; 
    var blnResult = true; 
    //var strSequence = document.frmQuestionDetail.txtSequence.value; 
    //test strString consists of valid characters listed above 
    if (strString.length == 0) 
        return false; 
    for (i = 0; i < strString.length && blnResult == true; i++) 
    { 
        strChar = strString.charAt(i); 
        if (strValidChars.indexOf(strChar) == -1) 
        { 
            blnResult = false; 
        } 
     }
     return blnResult; 
}

function reloadParentAndClose()
{
  
  window.close();

}
  
function load(){

//document.getElementById(getNetuiTagName('up')).disable=true;   

var upload="<%=session.getAttribute("upload")%>";

if(upload!= null){
    if(  upload == 'upload'){
    var url='gotoEvaluationResults1.action?';
    window.opener.document.forms[0].action='doGotoEvaluationResults';
    window.opener.document.forms[0].submit();   
    window.close();
    }
    }
}  

  
function checkFilled() {
    var tempVal = '<%=request.getContextPath()%>';
    
    var filled = 0;
    var evaluationDate = document.getElementById('evaluationDate').value;
    
    evaluationDate = evaluationDate.replace(/^\s+/,""); // strip leading spaces
    
    if (evaluationDate.length > 0) {
    	filled=filled +1;
    }
    
    var score=document.getElementById('score');
    
    if(score.value.length>0)  {
    	filled=filled +1;
    }
    var file = document.getElementById('Uploadfile').value;   
    var len=file.length;  
    if(len > 0){
    	filled=filled +1;
    }
    if (filled == 3) {
        document.getElementById("uploadbut").src= tempVal+"/evaluation/resources/_img/Upload.gif";
        document.getElementById("uploadbut").onclick=CheckFile;
    }
    else{
        document.getElementById("uploadbut").src= tempVal+"/evaluation/resources/_img/Upload_disabled.gif";
    }
}
</script>

<%--
  Map scores = SCEUtils.getScoreMap();
  pageContext.setAttribute("scores", scores); 
  Map scoreList = (HashMap)pageContext.getAttribute("scoreList");
--%>
<html>
    <head>
        <title>Pfizer Sales Call Evaluation</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="Content-Language" content="en-us" />        
        <meta name="ROBOTS" content="ALL" />
        <meta http-equiv="imagetoolbar" content="no" />
        <meta name="MSSmartTagsPreventParsing" content="true" />
        <meta name="Keywords" content="_KEYWORDS_" />
        <meta name="Description" content="_DESCRIPTION_" />
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css" rel="stylesheet" type="text/css" media="all" />
        <!--[if IE 6]><LINK href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" type=text/css rel=stylesheet><![endif]-->        
        <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/sorttable.js"></script> 
        <style type="text/css">@import url(<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar-win2k-cold-2.css);</style>
        <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar.js"></script>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/lang/calendar-en.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/jscalendar-1.0/calendar-setup.js"></script>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/DateValidator.js"></script>               
    </head>
    <body id="p_search" class="search" onload="load()">
        <div id="wrap">
            <div id=top_head>
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>            
               
            </div>
            
            <h3>Upload Evaluation Form</h3>
        
            <%
            session.removeAttribute("upload");
            String product="";
            String productcode="";
            String eventid="";
            String  repEmplid="";
            Integer sceID=new Integer(0);
            DateFormat dateTimeFormatter = new SimpleDateFormat(
        			SCEUtils.DEFAULT_DATETIME_FORMAT);
            if(request.getParameter("product")!=null){
            	product = (String)request.getParameter("product");
            }
            if(request.getParameter("emplid")!=null){
              repEmplid = (String)request.getParameter("emplid");
            }
            if(request.getParameter("eventid")!=null){
              eventid=(String)request.getParameter("eventid");
            }
            if(request.getParameter("productcode")!=null){
              productcode=(String)request.getParameter("productcode"); 
            }
            Integer version= sceManagerImpl.fetchVersionNumber((Integer)pageContext.getAttribute("templateVersionId"));
            //2020:Q3,Q4:MUZEES: Registration, Previous Evaluation Date&Time fields
            String previousEvaluationDate=null;
            String formatPrevEval=null;
            Date registrationDate=null;
            if(request.getParameter("prevEvalDate")!=null){
            	previousEvaluationDate=(String)request.getParameter("prevEvalDate");
            }
            if(request.getParameter("formatPrevEval")!=null){
            	formatPrevEval=(String)request.getParameter("formatPrevEval");
            }
            if(repEmplid!="" && product!=""){
                registrationDate=sceManagerImpl.minRegistrationDate(repEmplid,eventid,productcode);}
            //end of muzees
           
            
           
            //Integer sceID=new Integer(request.getParameter("sceId"));           
            //Integer templateVersionId=new Integer(request.getParameter("templateVersionId"));
            
            %>
            <div id=main_content>
                <s:form action="uploadEvaluation" Id="EvaluationFormData"   enctype="multipart/form-data">
                
                <!--<netui:hidden dataSource="{actionForm.product}" tagId="product"/>-->
                <input type="hidden" name="product" id="product" value="<%=product%>"/>
                <input type="hidden" name="repEmplid" id="repEmplid" value="<%=repEmplid%>"/>
                <input type="hidden" name="eventid" id="eventid" value="<%=eventid%>"/>
                <input type="hidden" name="templateVersionId" id="templateVersionId" value="<%=templateVersionId%>"/>
                <input type="hidden" name="productcode" id="productcode" value="<%=productcode%>"/>
                <input type="hidden" name="registrationDate" id="registrationDate" value="<%=registrationDate%>"/> 
                <input type="hidden" name="previousEvaluationDate" id="previousEvaluationDate" value="<%=previousEvaluationDate%>"/>                  
                    <fieldset>
                    <font color="red"><%=SCEUtils.ifNull(request.getAttribute("msg"),"")%></font>
                        <div>	
                            <label>Evaluation Name:</label> <%=product%>
                            <!-- <netui-data:getData resultId="evaluationTitle" value="{actionForm.evaluationTitle}" /> -->
                            <s:property value="evaluationTitle"></s:property>
                               
                                                 </div>
                        <div>			
                            <label>Version No:</label><%=version%>
                        </div> 
                         <!-- 2020:Q3,Q4:MUZEES: Registration, Previous Evaluation Date fields -->
                        <div>			
                            <label>Registration Date:</label><%=SCEUtils.ifNull(registrationDate,dateTimeFormatter)%>
                        </div>
                         <%if(formatPrevEval!=null&&formatPrevEval!=""){%>
                        <div>
                            <label>Previous Evaluation Date:</label><%=formatPrevEval%>
                        </div>
                        <%}%>
                        <!-- end of muzees -->
                        <div>			
                            <label>Evaluation Date:<font id="evaluationDateMandatory" style="color:#ff0000;">*</font></label>
                            <!-- <netui-data:getData resultId="evaluationDateStr" value="{actionForm.evaluationDate}" /> -->
                            <s:property value="evaluationDate" ></s:property>
                            <%
                                String evaluationDateStr="";
                                //String attendeeTrainingDateStr = (String)pageContext.getAttribute("attendeeTrainingDateStr");
                            %>
                            <input type="text" readonly="readonly" size="11" maxlength="10" value="<%=evaluationDateStr != null ? evaluationDateStr : dateFormatter.format(new Date())%>" name="evaluationDate" id="evaluationDate" onchange="checkFilled()"  >
                            <img id="calImg_trainingDate" src="<%=request.getContextPath()%>/evaluation/resources/images/icon_calendar.gif" border="0"  name="as_of_date_UP">
                            <script type="text/javascript">
                                Calendar.setup({
                                    inputField     :    "evaluationDate",
                                    ifFormat       :    "%m/%d/%Y",
                                    button         :    "calImg_trainingDate",
                                    align          :    "T1"                            
                                });
                            </script>
                            <p></p>
                        </div>
                        
                        <div>			
                            <label>Evaluation Time:<font id="evaluationTimeMandatory" style="color:#ff0000;">*</font></label>                            
                            <input type="text" size="11" maxlength="10" value="00:00 AM" name="evaluationTime" id="evaluationTime" >
                        </div>
                        <div>			
                            <label>Score:<font id="scoreMandatory" style="color:#ff0000;">*</font></label>                            
                            <s:select list="#request.scoreList" name="score"  id="score" onChange="checkFilled()"></s:select>
                        </div>                                                          
                        
                        
                    <div>                     			
                            <label>Upload File:<font id="uploadFileMandatory" style="color:#ff0000;">*</font></label>                      
                            <s:file accept="application/pdf" id="Uploadfile" size="10000" style="width:300px;" onChange="checkFilled()" name="contentUp"/>
                            
                               
                        </div>   
                        <div><b><font style="color:#ff0000;">Upload a PDF file only</font></b> </div>                    
                    </fieldset> 
                  
                <img src="<%=request.getContextPath()%>/evaluation/resources/_img/Upload_disabled.gif" id="uploadbut" alt="Upload" width="47" height="19" name="up"/>
                <img src="<%=request.getContextPath()%>/evaluation/resources/_img/btn_close.gif" alt="close" width="42" height="19"  onclick="reloadParentAndClose() " />
                </s:form>
                
    
                <div class=clear></div>
            </div>
            <!-- end #content -->
        </div>
        <!-- end #wrap -->
    </body>
</html>


