<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.beans.CourseDetails"%>
<%@ page import="com.pfizer.sce.beans.Util"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp" %>

<%-- <netui-data:declareControl type="SCEDBControls.SCEManager" controlId="SCEManager"></netui-data:declareControl> --%>
<%
       CourseDetails[] courseDetails = null;
       Util chkNull = new Util();
       String srchCourseName = "";
       String srchCourseCode = ""; 
       String message = "";
       
       String chkCourseName = "";
       String escCourseName = "";             
              
        if(request.getAttribute("courseSearchArr") !=null){
          courseDetails = (CourseDetails[])request.getAttribute("courseSearchArr"); 
          //out.println("Template[] Array Size-->"+courseDetails.length); 
        }
        if(request.getAttribute("searchCourseName")!=null){
          srchCourseName = (String)request.getAttribute("searchCourseName");
          //out.println("srchCourseName = "+srchCourseName);
        }
        if(request.getAttribute("searchCourseCode")!=null){
          srchCourseCode = (String)request.getAttribute("searchCourseCode");
          //out.println("srchCourseCode = "+srchCourseCode);
        }
         if(request.getAttribute("message")!=null){
          message = (String)request.getAttribute("message");
          //out.println("message = "+message);
        }
        
        
        
%>
<script language="javascript">
        var tempVal = '<%=request.getContextPath()%>';
        var aLen = '';
        var arrActivityPk = new Array();
        var arrCourseName = new Array();
        var arrCourseCode = new Array();
        var msg='';
        //var srchCourseName='';
        //var srchCourseCode ='';
        
       
        
    function retainValues(){
        <%if(!srchCourseName.equals("")){%>
            srchCourseName = '<%=srchCourseName.replaceAll("\'", "%27")%>';
        <%}else{%>
            srchCourseName = '';
        <%}%>
        <%if(!srchCourseCode.equals("")){%>
            srchCourseCode = '<%=srchCourseCode%>';
        <%}else{%>
            srchCourseCode = '';
        <%}%>
        
        
        if(srchCourseName!='' || srchCourseName!=""){
            document.getElementById("txtCourseName").value=replaceAll(srchCourseName, '%27', '\'');
        }
        if(srchCourseCode!='' || srchCourseCode!=""){
            document.getElementById("txtCourseCode").value=srchCourseCode;
        }
        
    }
       
    function searchCourse(){        
        var courseName = document.getElementById("txtCourseName").value;
        var courseCode = document.getElementById("txtCourseCode").value;
        var submit=false;
        //alert('Course Name -->'+courseName);
        //alert('Course Code-->'+courseCode);
        //alert(courseName.length);
        //alert(courseCode.length);
        if(courseName=="" && courseCode=="" ){
            //alert('Please enter either a Course Name or a Course Code to search');
            alert('Please enter at least 3 characters of either Course Name or a Course Code to search');
            submit=false;
           
        }
        else if(courseName.length<3 && courseCode.length<3){
            alert('Please enter at least 3 characters of either Course Name or a Course Code to search');
            submit=false;
        }else{
           submit=true; 
        }
        //alert(submit);
        if(submit==true){
        clearSelectedValues();
        document.forms[0].action="searchCourseDetails";
        //alert('Action --->'+document.forms[0].action);
        document.forms[0].submit();
        }
       }
    
    
    
    
    //Function to Check / Uncheck all checkboxes based on the main selection
    function toggleSelection(obj){
        //alert('Main checkbox - '+obj);
        //alert('Main checkbox status - '+obj.checked);
        var el = '';
        
        <%if(courseDetails!=null){%>
            aLen = '<%=courseDetails.length%>';
            //alert('aLen = '+aLen);
        <%}%>
        if(obj.checked==true && aLen!=0){
            //alert('Check All');
            for(i=0; i<aLen; i++){
                el = 'chkCourseMapping_'+i;
                //alert('el = '+el);
                document.getElementById(el).checked = true;
                el = '';
                
            }
        }
        else if(obj.checked==false && aLen!=0){
            //alert('UnCheck All');
            for(i=0; i<aLen; i++){
                el = 'chkCourseMapping_'+i;
                //alert('el = '+el);
                document.getElementById(el).checked = false;
                el = '';
            }
        }
        
        
    }//End function
    
    
    //This function is called when the individual checkboxes are checked / unchecked
    function setSelectedValues(objChkBox, crsId, escCrsName, crsCode, index){
        //alert('escCrsName - '+escCrsName);
        var crsName = replaceAll(escCrsName, '%27', '\''); //Function called from validate.js
        //alert('crsName - '+crsName);
        
        //alert(objChkBox.checked+" -- "+crsId+" -- "+crsName+" -- "+crsCode+" -- "+index);
        <%if(courseDetails!=null){%>
            aLen = '<%=courseDetails.length%>';
            //alert('aLen = '+aLen);
        <%}%>
        var el = '';
        var unchkMain = false;
        var numChk=0;
        
        if(objChkBox.checked==true){
            //alert('Check the main checkbox only if all other checkboxes are checked');
            for(i=0; i<aLen; i++){
                el = 'chkCourseMapping_'+i;
                //alert(el);
                if(document.getElementById(el).checked==true){
                    //alert(document.getElementById(el).checked);
                    numChk+=1; //Number of boxes checked
                }
            }
            //alert('numChk - '+numChk);
            if(aLen==numChk){//If the number of boxes checked is equal to the length of fetched array
                //alert('Check main');
                document.getElementById("checkUnchkAllId").checked=true;
            }
            document.getElementById("hdnActivityPk_"+index).value=crsId;
            document.getElementById("hdnCourseName_"+index).value=crsName;
            document.getElementById("hdnCourseCode_"+index).value=crsCode;
        }
        else if(objChkBox.checked==false){
            //alert('Uncheck the main checkbox');
            document.getElementById("checkUnchkAllId").checked=false;
            document.getElementById("hdnActivityPk_"+index).value="";
            document.getElementById("hdnCourseName_"+index).value="";
            document.getElementById("hdnCourseCode_"+index).value="";       
        }
    } //End function
       
    
    
    //This function clears all the hidden fields
    function clearSelectedValues(){
        //alert("Inside clearSelectedValues");
        <%if(courseDetails!=null){%>
            aLen = '<%=courseDetails.length%>';
            //alert('aLen = '+aLen);
        <%}%>
        //alert("Number of elements : "+aLen);
        for(index=0; index<aLen; index++){
            document.getElementById("hdnActivityPk_"+index).value="";
            document.getElementById("hdnActivityPk_"+index).disabled=true;
            document.getElementById("hdnCourseName_"+index).value="";
            document.getElementById("hdnCourseName_"+index).disabled=true;
            document.getElementById("hdnCourseCode_"+index).value="";
            document.getElementById("hdnCourseCode_"+index).disabled=true;  
        }
    } //End function
    
    
    
    //Function used to insert all the selected courses to the main page
    function validateAndInsert(){
        //alert('Inside validate and insert');
        <%if(courseDetails!=null){%>
            aLen = '<%=courseDetails.length%>'; //This is the total number of records fetched
            //alert('aLen = '+aLen);
        <%}%>
        var el = ''; //Checkbox ID element for individual course details
        var totalsChks=0;
        
        var strCourseIds='';
        var strCourseNames='';
        var strCourseCodes='';
        var delimiter='';
        
        var sendCourseIds='';
        var sendCourseNames='';
        var sendCourseCodes='';
        
        var arrActivityPk = new Array();
        var arrCourseName = new Array();
        var arrCourseCode = new Array();
        
        
        for(i=0; i<aLen; i++){
            el = 'chkCourseMapping_'+i;
            //alert('el = '+el);
            if(document.getElementById(el).checked == true){
                totalsChks+=1;
                //alert('CBox '+i+ ' = '+document.getElementById(el).checked);
                //alert(document.getElementById("hdnActivityPk_"+i).value);
            }
        }//End for
        if(totalsChks==0){
           alert('Please Select at least one Course');
            window.focus();
        }
        else {
            //alert('totalsChks -->'+totalsChks);
            //alert('aLen --> '+aLen);
            for(j=0; j<aLen; j++){
                el='chkCourseMapping_'+j;
                               
                if(document.getElementById(el).checked == true){
                                 
                 strCourseIds = strCourseIds+document.getElementById("hdnActivityPk_"+j).value+",";
                 sendCourseIds = strCourseIds+document.getElementById("hdnActivityPk_"+j).value+"\n";
                 
                 if((document.getElementById("hdnCourseName_"+j).value)!=' ' || (document.getElementById("hdnCourseName_"+j).value)!=" "){
                    strCourseNames = strCourseNames+document.getElementById("hdnCourseName_"+j).value+",";
                    sendCourseNames = sendCourseNames+document.getElementById("hdnCourseName_"+j).value+"\n";
                 }
                 else if((document.getElementById("hdnCourseName_"+j).value)==' ' || (document.getElementById("hdnCourseName_"+j).value)==" "){
                    strCourseNames = strCourseNames+"";
                    sendCourseNames = sendCourseNames+"";
                 }
                
                 if((document.getElementById("hdnCourseCode_"+j).value)!=' ' || (document.getElementById("hdnCourseCode_"+j).value)!=" "){
                    strCourseCodes = strCourseCodes+document.getElementById("hdnCourseCode_"+j).value+",";
                    sendCourseCodes = sendCourseCodes+document.getElementById("hdnCourseCode_"+j).value+"\n";
                 }
                 else if((document.getElementById("hdnCourseCode_"+j).value)==' ' || (document.getElementById("hdnCourseCode_"+j).value)==" "){
                    strCourseCodes = strCourseCodes+"";
                    sendCourseCodes = sendCourseCodes+"";
                 }
                
                }//End if checked==true
        }//End for(j=0; j<aLen; j++)
          
                
        //alert('strCourseIds = '+strCourseIds);
        //alert('strCourseNames = '+strCourseNames);
        //alert('strCourseCodes = '+strCourseCodes);
        
        if(strCourseIds.substr(strCourseIds.length-1)==','){
            
            strCourseIds = strCourseIds.substring(0, (strCourseIds.length-1));
        }
        if(strCourseNames.substr(strCourseNames.length-1)==','){
            
            strCourseNames = strCourseNames.substring(0, (strCourseNames.length-1));
        }
        if(strCourseCodes.substr(strCourseCodes.length-1)==','){
           
            strCourseCodes = strCourseCodes.substring(0, (strCourseCodes.length-1));
        }
        
        
        //alert('Final strCourseIds = '+strCourseIds);
        //alert('Final strCourseNames = '+strCourseNames);
        //alert('Final strCourseCodes = '+strCourseCodes);
        var myVal4=document.getElementById("hdCourseIds").value=strCourseIds;
        window.opener.getValueFromChildId(myVal4);
        var myVal=document.getElementById("hdCourseName").value=sendCourseNames;
        var myVal1=document.getElementById("hdnSrchCourseName").value=strCourseNames;
        window.opener.getValueFromChildName(myVal,myVal1); 
        
        var myVal2=document.getElementById("hdCourseCode").value=sendCourseCodes;
        var myVal3=document.getElementById("hdnSrchCourseCode").value=strCourseCodes;
        window.opener.getValueFromChildCode(myVal2,myVal3);
        window.opener.saveButton(); 
        window.close();
        return false;
        /* document.getElementById("hdCourseName").value=strCourseCodes; */
        /*document.forms[0].action="ParentPage";
        
        document.forms[0].submit();
        window.opener.location.reload(true); */
       
        //self.close();
        
        //Set selected values of CourseId, CourseCode and CourseName on the Parent Window (Text Area and Hidden Fields)
        /* window.opener.evalTemplLmsCourseMapping.courseCode.value = sendCourseCodes;
        window.opener.evalTemplLmsCourseMapping.courseName.value = sendCourseNames;
        window.opener.evalTemplLmsCourseMapping.hiddenCourseIds.value = strCourseIds;
        window.opener.evalTemplLmsCourseMapping.hiddenCourseNames.value = strCourseNames; */
       
       /*  window.opener.evalTemplLmsCourseMapping.hiddenCourseCodes.value = strCourseCodes;
        window.opener.evalTemplLmsCourseMapping.imgSaveMappingName.src = tempVal+"/evaluation/resources/_img/button_save.gif";
        window.opener.evalTemplLmsCourseMapping.imgSaveMappingName.onclick = window.opener.parentFunction; */
       
     }
     }
    
    
    
    //Function called on click of the Close button in the pop-up    
    function closePopUp(){
        //alert('closePopUp()');
        self.close();
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
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/sorttable.css" rel="stylesheet" type="text/css" media="all" />
        <!--[if IE 6]><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" /><![endif]-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/sorttable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/validate.js"></script>
        
        
        <style type="text/css">

        
        
        </style>
        
    </head>
    <!--body id="search_course" class="admin" onload="retainValues()"-->
    <body id="p_search_results" class="search" onload="retainValues()">
        <div id="wrap">
        
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
                <%-- @include file="navbar.jsp" --%>
            </div>
            <!-- end #top_head -->
        
            <h3>Search Course</h3>
            
            <div id="main_content">
                <s:form name="searchLmsCourse" id="searchLmsCourse">
                
                <!-- Search Criteria block -->
                <table width="90%" style="border:0; cellpadding:0; cellspacing:0; align:left" class="evalmappingtable" >
                    <tr>
                        <td width="40%" class="evalmappingtd" align="left" style="font-weight:bold; font-color:black">Course Name</td>
                        <td width="5%" class="evalmappingtd" align="left">&nbsp;</td>
                        <td width="40%" class="evalmappingtd" align="left" style="font-weight:bold; font-color:black">Course Code</td>
                        <td width="15%" class="evalmappingtd" align="left">&nbsp;</td>
                    </tr>
                    <tr>
                        <td width="40%" class="evalmappingtd">
                            <input  type="text" id="txtCourseName" size="60" style="width: 99%;" name="courseName" align="left" border="1" value="">
                            <input  type="hidden" id="hdnSrchCourseName" name="hdnSrchCourseName" align="left" border="1" value="">
                        </td>
                        <td width="5%" class="evalmappingtd"><b>&nbsp;&nbsp;OR&nbsp;&nbsp;</b></td>
                        <td width="40%" class="evalmappingtd" align="left">
                            <input type="text" id="txtCourseCode" size="60" style="width: 99%;" name="courseCode" align="left" value="">
                            <input  type="hidden" id="hdnSrchCourseCode" name="hdnSrchCourseCode" align="left" border="1" value="">
                        </td>
                        <td width="15%" class="evalmappingtd" align="left">
                          <img id="imgSearchCourse" src="<%=request.getContextPath()%>/evaluation/resources/_img/button_search.gif" style="width:56px; height:19px" onclick="searchCourse()" alt="Search Course">
                        </td>
                    </tr>
                    
                </table>
                <!-- End Search Criteria block -->
                
                <br>
                
               
               
               <!-- Search Results block -->
                
                <!-- Message block when records are fetched -->
                <% if(courseDetails!=null && courseDetails.length!=0){
                    CourseDetails objCourseDetails = null;
                        if(!message.equals("")){%>
                          <table width="90%" id="tblmessage" style="align:center" border="0" cellspacing="0" cellpadding="0" style="display:block;" class="evalmappingtable">  
                            <tr>
                            <td width="100%" class="evalmappingtd">
                                <font style="color:red; font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';"><%=message%></font>
                            </td>
                            </tr> 
                          </table>  
                        <%} //End of if(!message.equals(""))
                        %>
                
                <br>
                <hr>
                
                <!-- Results block when records are fetched -->
                 <table class="sortable"  width="90%" id="tblSearchResults" border="0" cellspacing="0" cellpadding="0" style="display:block;">
                <!--<table class="sortable" width="90%" id="tblSearchResults" border="0" cellspacing="0" cellpadding="0" style="display:block;" align="center">-->
                   <tr>
                        <th>
                            <input type="checkbox" style="height:12px; width:12px; align:center; valign:bottom" id="checkUnchkAllId" name="checkUnchkAll" onclick="toggleSelection(this)">   
                        </th>
                        <!--<th width="15%"><b>Select All</b></th>-->
                        <th onclick="ts_resortTable(this, 1);return false;"><b><u>Course Name</u></b></th>
                        <th onclick="ts_resortTable(this, 2);return false;"><b><u>Course Code<u></b></th>
                       
                    </tr> 
                  
                    <%
                        for(int i=0; i<courseDetails.length; i++) {
                         objCourseDetails = courseDetails[i];
                              chkCourseName = chkNull.isNull((courseDetails[i].getCourseName())); 
                              
                                if(!chkCourseName.equals(" ")){
                                     escCourseName = chkCourseName.replaceAll("\'", "%27");
                                }
                              
                    %>
                            <tr>
                                <td width="10%">
                                    <input type="checkbox" style="height:12px; width:12px; align:center; valign:bottom" name="chkCourseMapping" id="chkCourseMapping_<%=i%>" onclick="setSelectedValues(this, '<%=courseDetails[i].getCourseId()%>', '<%=escCourseName%>', '<%=chkNull.isNull((courseDetails[i].getCourseCode()))%>', '<%=i%>')">   
                                </td>
                                <!--<td width="15%">&nbsp;</td>-->
                                <td width="40%"> <%=escCourseName.replaceAll("%27", "\'")%> </td>
                                <td width="40%"><%=chkNull.isNull((courseDetails[i].getCourseCode()))%></td>
                                <input type="hidden" id="hdnActivityPk_<%=i%>" name="hdnActivityPk" value="<%=courseDetails[i].getCourseId()%>">
                                <input type="hidden" id="hdnCourseName_<%=i%>" name="hdnCourseName" value="<%=escCourseName.replaceAll("%27", "\'")%>">
                                <input type="hidden" id="hdnCourseCode_<%=i%>" name="hdnCourseCode" value="<%=chkNull.isNull((courseDetails[i].getCourseCode()))%>">
                            <input type="hidden" id="hdCourseName" name="hdCourseName" value="">
                            <input type="hidden" id="hdCourseCode" name="hdCourseCode" value="">
                            <input type="hidden" id="hdCourseIds" name="hdCourseIds" value="">
                            </tr> 
                             
                        <%}%><!--End for loop-->
                            
                 </table>
                 <table width="100%" style="border:0;align:left; display:block;">
                        <tr>
                                <td width="100%" colspan="3" class="evalmappingtd">
                                    <img id="insertSel" name="insertSelected" src="<%=request.getContextPath()%>/evaluation/resources/_img/button_insert_selected.gif" style="width:89px; height:19px" onclick="validateAndInsert()" alt="Insert Selected">
                                    <!--<input type="button" name="insertSelected" id="insertSel" value="Insert Selected" onclick="validateAndInsert()">-->
                                    &nbsp;&nbsp;
                                    <!--<input type="button" name="closeWindow" id="closeWin" value="Close" onclick="closePopUp()">-->
                                    <img id="closeWin" name="closeWindow" src="<%=request.getContextPath()%>/evaluation/resources/_img/btn_close.gif" style="width:42px; height:19px" onclick="closePopUp()" alt="Close">
                                </td>
                        
                            </tr> 
                      
                 </table>
                 <%                   
                  }else if(courseDetails!=null){
                    if(courseDetails.length==0 && !message.equals("")){
                 %>
                 <br>
                 <hr>
                
                    <table width="90%" id="tblNoResults" border="0" cellspacing="0" cellpadding="0" style="display:block;" class="evalmappingtable" align="center">  
                        <tr>
                            <td width="100%">
                                <font style="color:red; font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';"><%=message%></font>
                            </td>
                        </tr> 
                    </table>
                 <%}}%>
               </s:form>  
            </div> <!-- end #main content --> 
        </div> <!-- end #wrap -->

    </body>

</html>