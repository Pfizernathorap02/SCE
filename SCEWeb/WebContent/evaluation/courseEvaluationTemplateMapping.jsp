<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.beans.CourseDetails"%>
<%@ page import="com.pfizer.sce.beans.TemplateVersion"%>
<%@ page import="com.pfizer.sce.beans.Template"%>
<%@ page import="com.pfizer.sce.beans.CourseEvalTemplateMapping"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.pfizer.sce.beans.Util"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp" %>

<%
Util chkNull = new Util();

Template[] evalTemplate = null;
Template[] templateDets=null;

CourseEvalTemplateMapping[] evalTemplateMapping = null;
CourseEvalTemplateMapping[] alreadyMapped = null;

Integer[] arrSelectedCourseIds = null;
Integer[] arrAlreadyMappedCourses = null;
String[] arrSelectedCourseNames = null;
String[] arrSelectedCourseCodes = null;

CourseDetails[] objUnmappedCourseDets = null;

String strSelectedEvalTemplate = ""; //String value of Template ID
String strSelectedEvalTempName = "";
Integer intSelectedId = null; //Integer value of Template ID
String strValidateString = "";
String validateAlertString="";

String sepCourseIds = "";
String sepCourseNames = "";
String sepCourseCodes = "";


//Integer[] alreadyMappedActPk = null;//Activity PK of already mapped course / (s)

String alreadyMappedActPk = "";
String alreadyMappedCrsNm = "";
String alreadyMappedCrsCd = "";
String delimiter = "";

//String strCourseIds = "";
//String strCourseNames = "";
//String strCourseCodes = "";

String strUnmappedCrsIds = "";
String strUnmappedCrsNames = "";
String strUnmappedCrsCodes = "";
String successMsg = "";

String strSavableRecs = "";
int intSavableRecs=0;
String strSavableCourseIds="";

CourseEvalTemplateMapping[] savedRecords = null; 
//CourseEvalTemplateMapping[] alreadyMappedCourses = null;

String strHiddenCourseIds="";
String strHiddenCourseNames="";
String strHiddenCourseCodes="";


String alreadyMappedString="";
String escAlreadyMapStr = "";

 /**************Get all request parameters on everytime page load from different methods**************/   

 
 
 //Return values from retrieveMappingDetails()
 
 //Array of ALL Evaluation Templates maintained in session for easy retrieval
 if(session.getAttribute("evalTemplateArr") !=null){
   evalTemplate = (Template[])session.getAttribute("evalTemplateArr");
   // // // System.out.println("JSP:::Template[] Array Size-->"+evalTemplate.length);
 }
 //Array of courses already mapped to selected Evaluation Template
 if(request.getAttribute("templateMappingArr") !=null){    
   evalTemplateMapping = (CourseEvalTemplateMapping[])request.getAttribute("templateMappingArr");  
   // // // System.out.println("JSP:::CourseEvalTemplateMapping[] Array Size-->"+evalTemplateMapping.length);
 }
 //Selected Evaluation Template ID
 if(request.getAttribute("selectedEvalTemplate")!=null){
   strSelectedEvalTemplate = (String)request.getAttribute("selectedEvalTemplate");
   intSelectedId = new Integer(Integer.parseInt(strSelectedEvalTemplate));
   // // System.out.println("JSP::::strSelectedEvalTemplate--->"+strSelectedEvalTemplate);
   // // System.out.println("JSP::::intSelectedId--->"+intSelectedId);
 }
 
 //Selected Evaluation Template Name
 if(request.getAttribute("selectedEvalTemplateName")!=null){
   strSelectedEvalTempName = (String)request.getAttribute("selectedEvalTemplateName");
   // // System.out.println("JSP::::strSelectedEvalTempName--->"+strSelectedEvalTempName);
 }
 /* Comma separated string of Course Id's, Course Names and Course Codes */
 if(request.getAttribute("retHdnCourseIds")!=null){
   strHiddenCourseIds = (String)request.getAttribute("retHdnCourseIds");
   // // System.out.println("JSP::::strHiddenCourseIds--->"+strHiddenCourseIds);
 }
 if(request.getAttribute("retHdnCourseCodes")!=null){
   strHiddenCourseCodes = (String)request.getAttribute("retHdnCourseCodes");
   // // System.out.println("JSP::::strHiddenCourseCodes--->"+strHiddenCourseCodes);
 }
 if(request.getAttribute("retHdnCourseNames")!=null){
   strHiddenCourseNames = (String)request.getAttribute("retHdnCourseNames");
   // // System.out.println("JSP::::strHiddenCourseNames--->"+strHiddenCourseNames);
 }
 
 
 
 
 //Return values from validateAlreadyMapped()

 if(request.getAttribute("validateString")!=null){
    strValidateString = (String)request.getAttribute("validateString");
    // // System.out.println("JSP:::validateAlreadyMapped() - strValidateString--->"+strValidateString);
    //out.println("JSP:::validateAlreadyMapped() - strValidateString--->"+strValidateString);  
 }
 
 //validateAlertString
 if(request.getAttribute("validateAlertString")!=null){
    validateAlertString = (String)request.getAttribute("validateAlertString");
    // // System.out.println("JSP:::validateAlreadyMapped() - validateAlertString--->"+validateAlertString); 
    //out.println("validateAlertString = "+validateAlertString);
 }
 
 if(request.getAttribute("savedRecords")!=null){
    savedRecords = (CourseEvalTemplateMapping[])request.getAttribute("savedRecords");
    // // System.out.println("JSP:::validateAlreadyMapped() - savedRecords--->"+savedRecords.length);
 }       
 
 if(request.getAttribute("selectedTemplateId")!=null){
     if(strSelectedEvalTemplate.equals("")){
         intSelectedId = (Integer)request.getAttribute("selectedTemplateId"); //Integer value of Template ID
         // // System.out.println("validateAlreadyMapped() - Integer intSelectedId--->"+intSelectedId); 
         strSelectedEvalTemplate = strSelectedEvalTemplate+(Integer)request.getAttribute("selectedTemplateId"); //String value of Template ID
         // // System.out.println("JSP:::validateAlreadyMapped() - strSelectedEvalTemplate--->"+strSelectedEvalTemplate);
    }
 }
 
 if(request.getAttribute("selectedTemplateName")!=null){
    if(strSelectedEvalTempName.equals("")){
         strSelectedEvalTempName = (String)request.getAttribute("selectedTemplateName");
         // // System.out.println("JSP:::validateAlreadyMapped() - strSelectedEvalTempName--->"+strSelectedEvalTempName);
    }
 }
 
 if(request.getAttribute("existingEvalCourseMapping")!=null){
    alreadyMapped = (CourseEvalTemplateMapping[])request.getAttribute("existingEvalCourseMapping");
    // // System.out.println("JSP:::validateAlreadyMapped() - Length of alreadyMapped array --->"+alreadyMapped.length);
    //out.println("JSP:::validateAlreadyMapped() - Length of alreadyMapped array --->"+alreadyMapped.length);
    
    if(alreadyMapped!=null){
     for(int i=0; i<alreadyMapped.length; i++){
       CourseEvalTemplateMapping obj = new CourseEvalTemplateMapping();
         obj = alreadyMapped[i];
         if(i==(alreadyMapped.length-1)){delimiter="";}else{delimiter=",";}
         
         Integer activityPk = obj.getActivityPk();
         //out.println("Integer activityPk of already mapped --->"+activityPk);
         alreadyMappedActPk = alreadyMappedActPk+activityPk.intValue()+delimiter;
         alreadyMappedCrsNm = alreadyMappedCrsNm+chkNull.isNull(obj.getCourseName())+delimiter;
         alreadyMappedCrsCd = alreadyMappedCrsCd+chkNull.isNull(obj.getCourseCode())+delimiter;
      }//End for
      // // System.out.println("JSP:::validateAlreadyMapped() - alreadyMappedActPk == "+alreadyMappedActPk+"  &&  alreadyMappedCrsNm == "+alreadyMappedCrsNm+"  &&  alreadyMappedCrsCd == "+alreadyMappedCrsCd);
     //out.println("JSP:::validateAlreadyMapped() - alreadyMappedActPk == "+alreadyMappedActPk+"  &&  alreadyMappedCrsNm == "+alreadyMappedCrsNm+"  &&  alreadyMappedCrsCd == "+alreadyMappedCrsCd);
    }
    
    
    //Form the warning String with below as example:
    /*The following courses are already mapped:
      Template 1 - Course Code 1
      Template 1 - Course Code 2 
      Template 2 - Course Code 3*/
    if(alreadyMapped!=null){
     alreadyMappedString="";
     for(int j=0; j<alreadyMapped.length; j++){
       CourseEvalTemplateMapping objMapping = new CourseEvalTemplateMapping();
       objMapping = alreadyMapped[j];
         String strTemplateName = objMapping.getName();
         String strCourseCode = objMapping.getCourseCode();
         alreadyMappedString=alreadyMappedString+""+(j+1)+"."+" "+strTemplateName+" - "+strCourseCode+"\n";
      }//End for
     // // System.out.println("JSP::::alreadyMappedString--->"+alreadyMappedString);
     escAlreadyMapStr = alreadyMappedString.replaceAll("\n","%0A");
     //out.println("JSP::::escAlreadyMapStr--->"+escAlreadyMapStr);
    }
    //End of form the warning String
    
 }
 
 if(request.getAttribute("courseEvalTempMapping")!=null){
    if(evalTemplateMapping==null){
         evalTemplateMapping = (CourseEvalTemplateMapping[])request.getAttribute("courseEvalTempMapping");
         // // System.out.println("JSP::::Length of array of all mapped courses to selected template--->"+evalTemplateMapping.length);
    }
    
 }
 
 if(request.getAttribute("selectedCourseIds")!=null){
      sepCourseIds = (String)request.getAttribute("selectedCourseIds");
      //strCourseIds = sepCourseIds.replaceAll(",", "\n"); 
      //out.println("sepCourseIds = "+sepCourseIds + "&& strCourseIds = "+strCourseIds);
      // // System.out.println("JSP:::sepCourseIds = "+sepCourseIds);
      //out.println("JSP:::sepCourseIds = "+sepCourseIds);
 }
 
 if(request.getAttribute("selectedCourseNames")!=null){
      sepCourseNames = (String)request.getAttribute("selectedCourseNames");
      //strCourseNames = sepCourseNames.replaceAll(",", "\n"); 
      //out.println("sepCourseNames = "+sepCourseNames + "&& strCourseNames = "+strCourseNames);
      // // System.out.println("JSP:::sepCourseNames = "+sepCourseNames);
      //out.println("sepCourseNames = "+sepCourseNames);        
 }
 
 if(request.getAttribute("selectedCourseCodes")!=null){
      sepCourseCodes = (String)request.getAttribute("selectedCourseCodes");
      //strCourseCodes = sepCourseCodes.replaceAll(",", "\n"); 
      // // System.out.println("JSP:::sepCourseCodes = "+sepCourseCodes);
      //out.println("JSP:::sepCourseCodes = "+sepCourseCodes);  
 }  
 
 //savableRecords
 if(request.getAttribute("savableRecords")!=null){
      intSavableRecs = ((Integer)request.getAttribute("savableRecords")).intValue();
      // // System.out.println("JSP:::intSavableRecs = "+intSavableRecs);
      //out.println("JSP:::intSavableRecs = "+intSavableRecs); 
 } 
 //savableCourseIds  
 if(request.getAttribute("savableCourseIds")!=null){
      strSavableCourseIds = (String)request.getAttribute("savableCourseIds");
      // // System.out.println("JSP:::strSavableCourseIds = "+strSavableCourseIds);
      //out.println("JSP:::strSavableCourseIds = "+strSavableCourseIds); 
 }     
 //End return values from method validateAlreadyMapped()
 
 
 
 
 //Return values from method saveCourseMappings()
 if(request.getAttribute("allMappingRecords")!=null){
   if(evalTemplateMapping==null){
     evalTemplateMapping = (CourseEvalTemplateMapping[])request.getAttribute("allMappingRecords");
     // // System.out.println("JSP:::CourseEvalTemplateMapping[] Array Size from saveCourseMappings() method-->"+evalTemplateMapping.length);
     //out.println("JSP:::saveCourseMappings() method ALL records-->"+evalTemplateMapping.length);
   }  
 }
 if(request.getAttribute("successMessage")!=null){
     successMsg = (String)request.getAttribute("successMessage");
     // // System.out.println("JSP:::successMessage from saveCourseMappings() method-->"+successMsg);
     //out.println("JSP:::successMessage from saveCourseMappings() method-->"+successMsg);
 }
 
 if(request.getAttribute("templateId")!=null){
     if(strSelectedEvalTemplate.equalsIgnoreCase("")){
       strSelectedEvalTemplate =  (String)request.getAttribute("templateId");//String value of Template Id 
       intSelectedId = new Integer(Integer.parseInt(strSelectedEvalTemplate));
       // // System.out.println(":::strSelectedEvalTemplate from saveCourseMappings() method-->"+strSelectedEvalTemplate);
       // // System.out.println(":::intSelectedId from saveCourseMappings() method-->"+intSelectedId);  
     }
 }
 if(request.getAttribute("templateName")!=null){
     if(strSelectedEvalTempName.equalsIgnoreCase("")){
       strSelectedEvalTempName =  (String)request.getAttribute("templateName");//String - Template Name 
       // // System.out.println("JSP:::strSelectedEvalTempName from saveCourseMappings() method-->"+strSelectedEvalTempName);
       //out.println("JSP:::strSelectedEvalTempName from saveCourseMappings() method-->"+strSelectedEvalTempName);
     }
 }
 if(request.getAttribute("strUnmappedCourseIds")!=null){
   strUnmappedCrsIds = (String)request.getAttribute("strUnmappedCourseIds");  
   // // System.out.println("JSP:::strUnmappedCrsIds -->"+strUnmappedCrsIds);
   
 }
 if(request.getAttribute("strUnmappedCrsNames")!=null){
   strUnmappedCrsNames = (String)request.getAttribute("strUnmappedCrsNames");  
   // // System.out.println("JSP:::strUnmappedCrsNames -->"+strUnmappedCrsNames);
 }
 if(request.getAttribute("strUnmappedCrsCodes")!=null){
   strUnmappedCrsCodes = (String)request.getAttribute("strUnmappedCrsCodes");  
   // // System.out.println("JSP:::strUnmappedCrsCodes -->"+strUnmappedCrsCodes);
 }
 //End return values from method saveCourseMappings()

 
 
 //Return values from deleteLmsMappings
 if(request.getAttribute("lmsCourseMapping")!=null){
   if(evalTemplateMapping==null){
         evalTemplateMapping = (CourseEvalTemplateMapping[])request.getAttribute("lmsCourseMapping");
         // // System.out.println("JSP:::CourseEvalTemplateMapping[] Array Size from deleteLmsMappings() method-->"+evalTemplateMapping.length);      
 }
 }
 if(request.getAttribute("selectedTempId")!=null){
     if(strSelectedEvalTemplate.equalsIgnoreCase("")){
       strSelectedEvalTemplate =  (String)request.getAttribute("selectedTempId");//String value of Template Id 
       intSelectedId = new Integer(Integer.parseInt(strSelectedEvalTemplate));
       // // System.out.println(":::strSelectedEvalTemplate from deleteLmsMappings() method-->"+strSelectedEvalTemplate);
       // // System.out.println("JSP:::intSelectedId from deleteLmsMappings() method-->"+intSelectedId);  
     }
 }
 if(request.getAttribute("selectedTempName")!=null){
     if(strSelectedEvalTempName.equalsIgnoreCase("")){
       strSelectedEvalTempName =  (String)request.getAttribute("selectedTempName");//String - Template Name 
       // // System.out.println("JSP:::strSelectedEvalTempName from deleteLmsMappings() method-->"+strSelectedEvalTempName); 
     }
 }
 //End return values from deleteLmsMappings
 
 
%>
<style>
#tblSearchResults2
{
position:relative\0/;
left:4px\0/ !important;
}
</style>
<style>
#tblSearchResults2
{
position: relative; 
left: 42px;
}

</style>

<script language="javascript">
    
    var tempVal = '<%=request.getContextPath()%>';
    var selId = '<%=intSelectedId%>';
    var strSelId ='<%=strSelectedEvalTemplate%>'
    var strSelName = '<%=strSelectedEvalTempName%>'
    var sepCourseNames='';
    var alreadyMappedCrsNm='';
    var unmappedCrsNames='';
    var strHiddenCourseNames='';
    
    
    var parentFunction = validateMappedValues; //To serve as a reference for the Child window
    
    //Populated in hidden fields
    var sepCourseIds = '<%=sepCourseIds%>'; 
    var sepCourseNames = '';
    <%if(!sepCourseNames.equals("")){%>
            sepCourseNames = '<%=sepCourseNames.replaceAll("\'", "%27")%>';
    <%}%>
    
    var sepCourseCodes = '<%=sepCourseCodes%>';
    
    
    var taCourseNames = '';
    var taCourseCodes = '';
        
    var validateString = '<%=strValidateString%>';
    var validateAlertString = '<%=validateAlertString%>';
    var alreadyMapped = '';
    
    var confirmation = false;
    var submit=false;
    
        
    <%if(alreadyMapped!=null){%>
        alreadyMapped = '<%=alreadyMapped.length%>'; //length of array
    <%}%>
    
    var alreadyMappedActPk = '<%=alreadyMappedActPk%>'; //Comma delimited string
    
    <%if(!alreadyMappedCrsNm.equals("")){%>
        alreadyMappedCrsNm = '<%=alreadyMappedCrsNm.replaceAll("\'", "%27")%>'; //Comma delimited string
    <%}%>
    
    var alreadyMappedCrsCd = '<%=alreadyMappedCrsCd%>'; //Comma delimited string
    var unmappedCrsIds = '<%=strUnmappedCrsIds%>';
    
    <%if(!strUnmappedCrsNames.equals("")){%>
        unmappedCrsNames = '<%=strUnmappedCrsNames.replaceAll("\'", "%27")%>';
    <%}%>
    
    var unmappedCrsCodes = '<%=strUnmappedCrsCodes%>';
    var intSavableRecs = '<%=intSavableRecs%>';
    var confirmString='';
    var strSavableCourseIds='<%=strSavableCourseIds%>';
    var strHiddenCourseIds='<%=strHiddenCourseIds%>';
    
    <%if(!strHiddenCourseNames.equals("")){%>
        strHiddenCourseNames = '<%=strHiddenCourseNames.replaceAll("\'", "%27")%>';
    <%}%>
    
    
    var strHiddenCourseCodes='<%=strHiddenCourseCodes%>';
    var warnMessage = '';
    <%if(!escAlreadyMapStr.equals("")){%>
        warnMessage = '<%=escAlreadyMapStr%>';
     <%}%>
     
     
    var retCourseIds='';
    var retCourseNames='';
    var retCourseCodes='';
    
    
    <%if(!strHiddenCourseIds.equals("")){%>
        retCourseIds = '<%=strHiddenCourseIds%>';
    <%}%>
    <%if(!strHiddenCourseCodes.equals("")){%>
        retCourseCodes = '<%=strHiddenCourseCodes%>';
    <%}%>
    <%if(!strHiddenCourseNames.equals("")){%>
        retCourseNames = '<%=strHiddenCourseNames.replaceAll("\'", "%27")%>';
        
    <%}%>
    
    function validateMappedValues(){
        //alert('Inside validateMappedValues()');
        /*if(validateString=='N' || validateString=="N"){
            document.forms[0].action = "saveCourseMappings.do?";
            //alert(document.forms[0].action);
            document.forms[0].submit();  
        }
        else if(validateString=='' || validateString==""){
            document.forms[0].action="validateAlreadyMapped.do?"; //Function to validate if any of the evaluation template are already mapped to the selected courses
            //alert('Action on click of Save - first-->'+document.forms[0].action);
            document.forms[0].submit();
        }*/
        
        var courseId= document.getElementById("hdnSelectedCourseIds").value;
        document.getElementById("hdnSelectedCourseIds").value = courseId;
        document.forms[0].action="validateAlreadyMapped"; //Function to validate if any of the evaluation template are already mapped to the selected courses
        //alert('Action on click of Save - first-->'+document.forms[0].action);
        document.forms[0].submit();
        
    }
    

    
    function toggleButtonAndRetainValues(selectedId, selectedName){
        
        //alert('Inside toggleButtonAndRetainValues()');
       // alert('selectedId = '+selectedId);
       // document.getElementById("hdnSelectedCourseIds").value =  selectedId; 
        //alert('selectedName = '+selectedName);
        //alert('Comma separated Values--->'+sepCourseIds+' ::: '+sepCourseNames+' ::: '+sepCourseCodes);
        //alert('Validate String = '+validateString);
        //alert('unmappedCrsIds = '+unmappedCrsIds+' unmappedCrsNames = '+unmappedCrsNames+' unmappedCrsCodes = '+unmappedCrsCodes);
        
        //alert('Array of unmapped courses = '+objUnmappedCourseDets.length);
        //alert('alreadyMappedActPk - '+alreadyMappedActPk);
        //alert('alreadyMappedCrsNm - '+alreadyMappedCrsNm);
        //alert('alreadyMappedCrsCd - '+alreadyMappedCrsCd);
        //alert('intSavableRecs - '+intSavableRecs);
        //alert('validateAlertString - '+validateAlertString);
        //alert('warnMessage - '+warnMessage);
        
       
        /* var objDropDown = document.getElementById("selEvalTemp");
        objDropDown.value=""; */
        
        if(intSavableRecs=='0' && (strSavableCourseIds=="" || strSavableCourseIds=='') && (alreadyMappedActPk!="" || alreadyMappedActPk!='') && (alreadyMappedCrsCd!="" || alreadyMappedCrsCd!='')){
            document.getElementById("hdnSelectedCourseIds").value=alreadyMappedActPk;
            //alert(document.getElementById("hdnSelectedCourseIds").value);
            document.getElementById("hdnSelectedCourseNames").value=replaceAll(alreadyMappedCrsNm, '%27', '\'');
            document.getElementById("hdnSelectedCourseCodes").value=alreadyMappedCrsCd;
            //document.getElementById("courseName").value=replaceAll(alreadyMappedCrsNm, '%27', '\'');
            //document.getElementById("courseCode").value=alreadyMappedCrsCd;
            var replacedCrsName = replaceAll(alreadyMappedCrsNm, '%27', '\'');
            document.getElementById("courseName").value = formatString(replacedCrsName);
            document.getElementById("courseCode").value = formatString(alreadyMappedCrsCd);
            
        }
        
        //CHECK 1 -- Enable / Disable the 'Search Course' AND 'Reset' buttons whenever a Template selection is made 
        if(selId=="null" || selId=="" || selId=="0"){
            //alert('selId - '+selId);
            document.getElementById("imgSearchCourse").src= tempVal+"/evaluation/resources/_img/button_search_course_disabled.gif";                
            
        }
        else{
            document.getElementById("imgSearchCourse").src = tempVal+"/evaluation/resources/_img/button_search_course.gif";
            document.getElementById("imgSearchCourse").onclick = openCourseSearch;
        }
        document.getElementById("imgReset").src = tempVal+"/evaluation/resources/_img/button_reset.gif";
        document.getElementById("imgReset").onclick = clearFields;
        //End CHECK 1
        
        
        //CHECK 2 -- Retain the values of the hidden fields (for TemplateId and TemplateName) every time this page is hit
        if((selectedId!="" || selectedId!='') && (selectedName!="" || selectedName!='')){
            //alert('selectedId '+selectedId);
            document.getElementById("hdnSelectedTempId").value = selectedId; 
            document.getElementById("hdnSelectedTemplateId").value = selectedName; 
            selId = selectedId;
            
        }
        //End CHECK 2
        
        
        //CHECK 10
        if(retCourseIds!='' || retCourseIds!=""){
          //alert('retCourseIds - '+retCourseIds);
          document.getElementById("hdnSelectedCourseIds").value =  retCourseIds; 
        }
        if(retCourseCodes!='' || retCourseCodes!=""){
          //alert('retCourseCodes - '+retCourseCodes);
          document.getElementById("hdnSelectedCourseCodes").value =  retCourseCodes; 
          //taCourseCodes = retCourseCodes.replaceAll(',', '\n');
          document.getElementById("courseCode").value = formatString(retCourseCodes);         
        }
         if(retCourseNames!='' || retCourseNames!=""){
          //alert('retCourseNames - '+retCourseNames);
          var replacedRetCourseNames = replaceAll(retCourseNames, '%27', '\'');
          document.getElementById("courseName").value = formatString(replacedRetCourseNames);          
          document.getElementById("hdnSelectedCourseNames").value =  retCourseNames; 
          //taCourseNames = retCourseNames.replaceAll(',', '\n');
          //document.getElementById("courseName").value = formatString(retCourseNames);    
        }
        //End CHECK 10
        
        
        //CHECK 3 - Retain the values of Course ID, Course Name and Course Code fields (and corresponding hidden fields when page refreshes after a Save action and DB call)
        if(sepCourseIds!=""  || sepCourseIds!=''){
        	//alert('sepCourseIds '+sepCourseIds);
            document.getElementById("hdnSelectedCourseIds").value = sepCourseIds;
        }
        if(sepCourseNames!=""  || sepCourseNames!=''){
            document.getElementById("hdnSelectedCourseNames").value = replaceAll(sepCourseNames, '%27', '\'');
            //taCourseNames = sepCourseNames.replace(',', '\n');
            //alert('taCourseNames = '+taCourseNames);
            if(taCourseNames!="" || taCourseNames!=''){
                document.getElementById("courseName").value = taCourseNames;
            }
        }
        if(sepCourseCodes!=""  || sepCourseCodes!=''){
            document.getElementById("hdnSelectedCourseCodes").value = sepCourseCodes;
            //taCourseCodes = sepCourseCodes.replace(',', '\n');
            //alert('taCourseCodes = '+taCourseCodes);
            if(taCourseCodes!="" || taCourseCodes!=''){
                document.getElementById("courseCode").value = taCourseCodes;
            }
        }
        //End CHECK 3
        
        
        
        //CHECK 4 - Enable the Save button on refreshing the page after validating the already mapped courses
        
        if((selectedId!="" || selectedId!='')&&(selectedName!="" || selectedName!='')&&((taCourseNames!='' || taCourseNames!="")||(document.getElementById("courseName").value!='' || document.getElementById("courseName").value!=""))){
            //alert('Enable Save button after refresh depending on CC and CN drop boxes');
             document.getElementById("imgSaveMapping").src = tempVal+"/evaluation/resources/_img/button_save.gif";
            document.getElementById("imgSaveMapping").onclick = validateMappedValues; 
        }
        //End CHECK 4
        
        
        //CHECK 5 - Show confirmation box when an already existing record is fetched from the DB call
        if((validateString!="" || validateString!='') || (validateAlertString!="" || validateAlertString!='')){
          
            //CHECK 6 - Populate hidden field containing comma delimited Activity PK
            if(alreadyMappedActPk!="" || alreadyMappedActPk!=''){
              document.getElementById("hdnAlMapActPk").value = alreadyMappedActPk; 
              
              if(intSavableRecs==0){
                //alert('No records to save. No need to submit');
                if(alreadyMappedCrsCd!="" || alreadyMappedCrsCd!=''){
                    //DO NOT remove this alert. Its part of functionality
                    //alert('The following courses are already mapped:\n'+replaceAll(warnMessage, '%0A', '\n'));
                    confirm('The following courses are already mapped:\n'+replaceAll(warnMessage, '%0A', '\n'));
                }
                //else if(alreadyMappedActPk!='' || alreadyMappedActPk!=""){
                    //confirmString = 'Warning :: The courses with Course Code(s) '+alreadyMappedCrsCd +' already exists in the database';
                //}
                //confirmation = confirm(confirmString);
                //alert(confirmString);
               }
              else if(intSavableRecs>0){
                //alert('One or more records to save. Set the confirmation string. Need to submit page');
                if((validateAlertString!='' || validateAlertString!="") && (alreadyMappedCrsCd!="" || alreadyMappedCrsCd!='') && (strSavableCourseIds!="" || strSavableCourseIds!='')){
                    //alert('strSavableCourseIds is not null - '+strSavableCourseIds);
                    document.getElementById("hdnSelectedCourseIds").value = strSavableCourseIds;
                    document.getElementById("hdnSavableRecsId").value=strSavableCourseIds;
                    //confirmString = 'Warning :: The courses with Course Code(s) '+alreadyMappedCrsCd+' are already mapped. Do you want to continue saving the rest?'
                    //confirmString = validateAlertString+'. Do you want to continue saving the rest?';
                    confirmString = 'The following courses are already mapped:\n'+replaceAll(warnMessage, '%0A', '\n')+'\nDo you want to continue saving the rest?';
                    confirmation = confirm(confirmString);
                }
              }
              //alert('confirmation = '+confirmation);
                if(confirmation==true){
                    submit = true;
                    <%-- window.document.getElementById(getNetuiTagName("evalTemplLmsCourseMapping")).action="<%=PageflowTagUtils.getRewrittenFormAction("saveCourseMappings", pageContext)%>"; --%>                
                    document.getElementById("hdnSelectedCourseIds").value = strSavableCourseIds;
                    document.getElementById("hdnSavableRecsId").value=strSavableCourseIds;
                   
                    //alert('course IDS from hdnSelectedCourseIds - '+document.getElementById("hdnSelectedCourseIds").value);
                    //alert('course IDS from hdnSavableRecsId- '+document.getElementById("hdnSavableRecsId").value);
                    window.document.forms[0].submit(); 
                }else if(confirmation==false){
                    //alert('Cancel');
                    submit = false;
                    
                    document.getElementById("hdnSelectedCourseIds").value=alreadyMappedActPk;
                    document.getElementById("hdnSelectedCourseNames").value=replaceAll(alreadyMappedCrsNm, '%27', '\'');
                    document.getElementById("hdnSelectedCourseCodes").value=alreadyMappedCrsCd;
                    //document.getElementById("courseName").value=replaceAll(alreadyMappedCrsNm, '%27', '\'');
                    //document.getElementById("courseCode").value=alreadyMappedCrsCd;
                    var replaceCrsName = replaceAll(alreadyMappedCrsNm, '%27', '\'');
                    document.getElementById("courseName").value = formatString(replaceCrsName);
                    document.getElementById("courseCode").value = formatString(alreadyMappedCrsCd);                    
                    //return false;
                }
            }
            //else if(alreadyMappedActPk=="" || alreadyMappedActPk==''){
              //alert('Scenario 3 - Save action has happened and there were NO existing mappings');
            //}
            //End CHECK 6   
            
         }else if((validateString=="" || validateString=='') && (selId!="null" || selId!="" || selId!="0") && (sepCourseIds!=""  || sepCourseIds!='')){
            //alert('Scenario 4 - Save action has not happened yet');
            validateMappedValues();
         }
      
          //End CHECK 7 
         
          
          //CHECK 8 
          
          if(unmappedCrsIds!="" || unmappedCrsIds!=''){
            document.getElementById("hdnSelectedCourseIds").value = unmappedCrsIds;
          }
          if(unmappedCrsNames!="" || unmappedCrsNames!=''){
            var replacedUnmappedCrsNames = replaceAll(unmappedCrsNames, '%27', '\'');
            document.getElementById("courseName").value = formatString(replacedUnmappedCrsNames);
            //document.getElementById("courseName").value = formatString(unmappedCrsNames);
            document.getElementById("hdnSelectedCourseNames").value = replaceAll(unmappedCrsNames, '%27', '\'');
          }
          if(unmappedCrsCodes!="" || unmappedCrsCodes!=''){
            //document.getElementById("courseCode").value = unmappedCrsCodes;
            document.getElementById("courseCode").value = formatString(unmappedCrsCodes);
            document.getElementById("hdnSelectedCourseCodes").value = unmappedCrsCodes;
          }
          if((unmappedCrsIds!="" || unmappedCrsIds!='') && (unmappedCrsNames!="" || unmappedCrsNames!='') && (unmappedCrsCodes!="" || unmappedCrsCodes!='')){
              document.getElementById("imgSaveMapping").src = tempVal+"/evaluation/resources/_img/button_save.gif";
             document.getElementById("imgSaveMapping").onclick = validateMappedValues;
          }
          
          //End CHECK 8 
          
          //CHECK 9
          if(strSavableCourseIds!="" || strSavableCourseIds!=''){
         
              document.getElementById("hdnSelectedCourseIds").value = strSavableCourseIds;
          }
         //End CHECK 9 
    }//End function toggleButtonAndRetainValues()
    



    //Function to replace occurrences of comma with a newline
    function formatString(toBeFormatted){
        //alert('toBeFormatted = '+toBeFormatted)
        var strReplaceAll = toBeFormatted;
        var intIndexOfMatch = strReplaceAll.indexOf(",");
        
        while (intIndexOfMatch != -1){
        // Relace out the current instance.
        strReplaceAll = strReplaceAll.replace(",", "\n")
         
        // Get the index of any next matching substring.
        intIndexOfMatch = strReplaceAll.indexOf( "," );
        }
         
     return strReplaceAll;
    }

  
     

function enableSearchAndGetEvalDetails(templateObj){
        //alert('Inside enableSearchAndGetEvalDetails(templateObj)');
        var index = templateObj.selectedIndex;
        //alert('Selected Index-->'+index);
        var which = templateObj.options[index].value;
        //alert('Selected Index int value-->'+which);
        var sel = templateObj.options[index].innerHTML;
        //alert('Selected Index string value -->'+sel);
         
         
        if(which!="0"){
            document.getElementById("imgSearchCourse").src = tempVal+"/evaluation/resources/_img/button_search_course.gif";
           // document.getElementById("imgSearchCourse").onclick = openCourseSearch;
           
        }
        else if(which=="0"){
            var functionParam = document.getElementById("selEvalTemp");
            //alert('Select element object'+functionParam);
            document.getElementById("imgSearchCourse").src= tempVal+"/evaluation/resources/_img/button_search_course_disabled.gif";            
            //document.getElementById("imgSearchCourse").onclick = validateSearchCourse;
            //document.getElementById("imgSearchCourse").onclick = new Function('validateSearchCourse('+functionParam+')');
        }
        
        document.getElementById("hdnSelectedTempId").value = which;
        document.getElementById("hdnSelectedTemplateId").value = sel;
        document.getElementById("imgReset").src = tempVal+"/evaluation/resources/_img/button_reset.gif";
        document.getElementById("imgReset").onclick = clearFields;
        //alert('course id'+strHiddenCourseIds);
        var courseId= document.getElementById("hdnSelectedCourseIds").value;
        document.getElementById("hdnSelectedCourseIds").value = courseId;
        //alert(document.getElementById("hdnSelectedCourseIds").value); 
        document.forms[0].action="retrieveMappingDetails";
        document.forms[0].submit();
        
        
    }
function openCourseSearch()
{
    
    var selectedTemplateId = document.getElementById("hdnSelectedTempId").value;
    var selectedTemplateName = document.getElementById("hdnSelectedTemplateId").value;
    
     //alert('selectedTemplateId-->'+selectedTemplateId);
    //alert('selectedTemplateName-->'+selectedTemplateName);  
    //alert('ala re');
    /* window.o
    document.forms[0].action="searchCourse";
    document.forms[0].submit(); */
     var newWinDir = "searchCourse";
    var newwindow = window.open(newWinDir, 'CourseSearch', 'status=1, height=500, width=700,scrollbars=yes');
       
    if (window.focus) {
     newwindow.focus();
    } 
    //return false;
} 

function clearFields(){
	
    var objDropDown = document.getElementById("selEvalTemp");
    var objCourseNameBox = document.getElementById("courseName");
    var objCourseCodeBox = document.getElementById("courseCode");
    var objHdnCourseId = document.getElementById("hdnSelectedCourseIds");
    var objHdnCourseNm = document.getElementById("hdnSelectedCourseNames");
    var objHdnCourseCd = document.getElementById("hdnSelectedCourseCodes");
    var objHeaderTable = document.getElementById("tblSearchResults1");
    var objDataTable = document.getElementById("tblSearchResults2");
    var btnSearchCourse = document.getElementById("imgSearchCourse");
    var btnSave = document.getElementById("imgSaveMapping");
    
    objDropDown.selectedIndex = 0;
    objCourseNameBox.value = "";
    objCourseCodeBox.value = ""; 
    objHdnCourseId.value="";
    objHdnCourseNm.value="";
    objHdnCourseCd.value="";
   // alert('3');
    btnSearchCourse.src = tempVal+"/evaluation/resources/_img/button_search_course_disabled.gif";
    btnSave.src = tempVal+"/evaluation/resources/_img/button_save_disabled.gif";
    //alert('4'+btnSearchCourse.src);
   // alert('5'+btnSave.src);
    btnSearchCourse.disabled= true;
    btnSave.disabled= true;
    //alert('3'+btnSave.disabled);
    document.getElementById("message").style.display = 'none';
    
    //alert('objHeaderTable = '+objHeaderTable+" && objDataTable = "+objDataTable);
    if(objHeaderTable!=null){
        //alert('objHeaderTable not null');
        objHeaderTable.style.display = "none";
    }
    if(objDataTable!=null){
       //alert('objDataTable not null');
       objDataTable.style.display = "none";
    }

 }  

function getValueFromChildId(myVal4)
{
	
	document.getElementById("hdnSelectedCourseIds").value=myVal4;
	
}
 function getValueFromChildName(myVal,myVal1)
 {
 	document.getElementById("courseName").value=myVal;
 	
 	document.getElementById("hdnSelectedCourseNames").value=myVal1;
 	
 }
function getValueFromChildCode(myVal2,myVal3)
{
	document.getElementById("courseCode").value=myVal2;
	document.getElementById("hdnSelectedCourseCodes").value=myVal3;
}

function saveButton()
{	
	document.getElementById("imgSaveMapping").onclick = validateMappedValues;
	document.getElementById("imgSaveMapping").src=tempVal+"/evaluation/resources/_img/button_save.gif";
	
}


 function deleteMapping(mappingId){//Mapping ID is the ID of the input box
    
    var delMsg='This mapping will be deleted. Do you wish to continue?';
    document.getElementById("hdnDelete").value=mappingId;  
    confirmDel = confirm(delMsg);
    if(confirmDel==true){   	
    	document.forms[0].action="delete";
        document.forms[0].submit();     
    }else{
      window.focus;
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
        <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/validate.js"></script>
        <!--<link href="<%=request.getContextPath()%>/evaluation/resources/_css/admin.css" rel="stylesheet" type="text/css" media="all" />--> 
        <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/sorttable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/validate.js"></script>  
    </head>
     
    <body id="p_template_mapping" onload="toggleButtonAndRetainValues('<%=strSelectedEvalTemplate%>','<%=strSelectedEvalTempName%>')">
   <!-- <form name="evalTemplLmsCourseMapping" id="evalTempLmsCourseMapping"> -->
        <form name="courseDetail" action="saveCourseMappings" tagId="evalTemplLmsCourseMapping" >
        <input type="hidden" name="pageName" id="pageName" value="retrieveMappingDetails"/>
        <input type="hidden" id="hdnSelectedTempId" name="hdnSelectedTemp" value=""/>
        <input type="hidden" id="hdnSelectedTemplateId" name="hdnSelectedTemplate" value=""/>
        <input type="hidden" id="hdnAlMapActPk" name="alreadyMappedActPk" value=""/>
        <input type="hidden" id="hdnCourseIds" name="hdnCourseIds" value=""/>
        <input type="hidden" id="hdnCourseName" name="hdnCourseName" value=""/>
        <input type="hidden" id="hdnCourseCode" name="hdnCourseCode" value=""/>
        <input type="hidden" id="hdnSavableRecsId" name="hdnSavableRecs" value=""/>
        <!--<input type="text" id="hdnAlMapCrsNm" name="alreadyMappedCrsNm" value="hdnCN">
        <input type="text" id="hdnAlMapCrsCd" name="alreadyMappedCrsCd" value="hdnCC">-->
         <div id="wrap" style="position:absolute; width: 90%;">
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
            
            <H3>Admin: LMS Course to Evaluation Template Mapping</H3>
            
            
            <br>
            
            <br>
            <table id="message" class="evalmappingtable" align="center" style="border:0 solid red">
                <%if(!successMsg.equals("")){%>
               <tr>
                <td colspan="6" class="evalmappingtd"><font style="color:red; font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';"><%=successMsg%></font></td>
               </tr>
               <tr>
                <td colspan="6" class="evalmappingtd"><font style="font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';">&nbsp;</font></td>
               </tr>
               <%}%>
               <%if(!strValidateString.equals("")){%>
               <tr>
                <td colspan="6" class="evalmappingtd"><font style="color:red; font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';"><%=strValidateString%></font></td>
               </tr>
               <tr>
                <td colspan="6" class="evalmappingtd"><font style="font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';">&nbsp;</font></td>
               </tr>
               <%}%>
               
             <%if(intSavableRecs==0 && strSavableCourseIds.equalsIgnoreCase("") && !validateAlertString.equalsIgnoreCase("")){%>
               <tr>
                <td colspan="6" class="evalmappingtd"><font style="color:red; font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';"><%=validateAlertString%></font></td>
               </tr>
               <tr>
                <td colspan="6" class="evalmappingtd"><font style="font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';">&nbsp;</font></td>
               </tr>
               <%}%>
            </table>
            
            <table id="tblSearchCriteria" class="evalmappingtable" align="center" style="border:0 solid red; position: relative; left: 18px;">                                      
              
               <tr>
                    <td width="15%" class="evalmappingtd"><b>Evaluation Template</b>
                    </td>
                    <td width="35%" class="evalmappingtd"><b>Course Name</b>
                    </td>
                    <td width="35%" class="evalmappingtd"><b>Course Code</b>
                    </td>
                    <td width="8%" class="evalmappingtd">&nbsp;</td>
                    <td width="7%" class="evalmappingtd">&nbsp;</td>
                    <td width="0%" class="evalmappingtd">&nbsp;</td>
               </tr>
               <tr> 
                <td width="15%" valign="top" class="evalmappingtd">
                 <select style="border:.07em solid grey;margin:0;padding:0;" label="Evaluation"  name="selEvalTemplate" id="selEvalTemp"   onchange="enableSearchAndGetEvalDetails(this);" >
                      <option value="0">---Select---</option>  
                      <%
                      
                     if(evalTemplate != null){
                    	 Template objEvalTemp = null;
                        for(int i=0; i<evalTemplate.length; i++){
                            objEvalTemp = evalTemplate[i];
                     
                            if(objEvalTemp.getName()!=null && !objEvalTemp.getName().equals("")){
                            	
                            	
                                if((objEvalTemp.getId()).equals(intSelectedId)){
                                	
                    %>
                                <option value="<%=objEvalTemp.getId()%>" selected><%=strSelectedEvalTempName%></option>
                                
                             
                                <%}else{
                                
                                %>
                                 
                                    <option value="<%=objEvalTemp.getId()%>"><%=chkNull.isNull((objEvalTemp.getName()))%></option>
                                    
                                <%}}}%>
                                
                    </select> 
                                      
                <input type="hidden" id="hdnSelectedCourseIds" name="hiddenCourseIds" value=""></input> 
                </td>
                            
                
                <!--Text Area containing the list of Course Names cols="100" rows="5" -->
                
                    <td width="35%" valign="top" class="evalmappingtd">
                        <!--<textarea readonly style="width:300px; height:100px; border:.07em solid grey; white-space: pre; overflow:auto;" name="courseName" id="courseName" value=""></textarea>-->
                        
                        <textarea readonly="readonly"  style="width:280px; height:100px; border:.07em solid grey; white-space: pre; overflow:auto;" wrap = "off" name="courseName" id="courseName" value=""></textarea>
                        <input type="hidden" id="hdnSelectedCourseNames" name="hiddenCourseNames" value="">
                    </td>   
               
                <!--Text Area containing the list of Course Codes-->        
                 
                 <td width="35%" valign="top" class="evalmappingtd">
                     <!--<textarea readonly style="width:300px; height:100px; border:.07em solid grey; overflow-x:auto; overflow-y:auto;" name="courseCode" id="courseCode" value=""></textarea>-->
                     <textarea readonly="readonly" style="width:280px; height:100px; border:.07em solid grey; white-space: pre; overflow:auto;" wrap = "off" name="courseCode" id="courseCode" value=""></textarea>
                     <input type="hidden" id="hdnSelectedCourseCodes" name="hiddenCourseCodes" value="">
                     <input type="hidden" id="hdnDelete" name="hdnDelete" value="">
                 </td>
              
                
                      
                  
                
                <td width="8%" valign="top" class="evalmappingtd">
                    <!--<img id="imgSearchCourse" src="/evaluation/resources/_img/button_search_course_disabled.gif" style="width:65px; height:21px" onClick="validateSearchCourse(this)" alt="Search Course">-->
                   
                    <img id="imgSearchCourse" src="<%=request.getContextPath()%>/evaluation/resources/_img/button_search_course_disabled.gif" style="width:89px; height:19px"  alt="Search Course"></img>
                   
                </td>
                <td width="7%" valign="top" class="evalmappingtd">
                    <img id="imgReset" src="<%=request.getContextPath()%>/evaluation/resources/_img/button_reset.gif" style="width:40px; height:19px" onclick="clearFields()" alt="Reset"></img>
                    <!--<input type="button" name="btnResetFields" id="resetFields" value="Reset" onClick="clearFields()">-->
                </td>
                 <td width="0%" class="evalmappingtd">&nbsp;</td>
                
               </tr>
               <tr>
                    <td colspan="6" class="evalmappingtd">&nbsp;</td>
               </tr>
               <tr>
                    <td colspan="6" class="evalmappingtd" valign="top">
                        <!--<img id="imgSaveMapping" name="imgSaveMappingName" src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save_old.gif" style="width:40px; height:15px" onClick="validateSave()" alt="Save Mapping">-->
                       

                            <img id="imgSaveMapping" name="imgSaveMappingName" src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save_disabled.gif" style="width:36px; height:19px" onclick=""  alt="Save Mapping"></img>		
                   
                    </td>
               </tr>
               <!--<tr>
                    <td colspan="6" class="evalmappingtd">
                        <hr style="height:.07em; color:grey;" />
                    </td>
               </tr>-->
               <tr>
               <td style="border-right:0; border-bottom: 0; border-left: 0; border-top: 0;" colspan="6">
             
                <hr>
                </td>
               </tr>
            </table>
            
            
            <!--Search Results Block-->
            
            <%--<%if((intSelectedId==null && strSelectedEvalTempName.equalsIgnoreCase("")) || (intSelectedId.equals("0") && strSelectedEvalTempName.equalsIgnoreCase("---Blank---"))){--%>
            <%if((intSelectedId==null && strSelectedEvalTempName.equalsIgnoreCase("")) || (strSelectedEvalTempName.equalsIgnoreCase("---Select---"))){ //out.println("intSelectedId = "+intSelectedId+"::::strSelectedEvalTempName = "+strSelectedEvalTempName);%>
                <table id="tblSearchResults" cellspacing="0" cellpadding="0" style="display:none;" class="evalmappingtable" align="center"></table>
            <%}else if(evalTemplateMapping!=null && evalTemplateMapping.length!=0){%>
                    <table width="90%" class="evalmappingtable" id="tblSearchResults1" cellpadding="0" cellspacing="0" style="border-right:1; border-bottom: 1; border-left: 1; border-top: 0;display:block; " align="center">
                        <tr>
                            <td style="position: relative;left: 65px;weight:bold;font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';"class="evalmappingtd">Following Course(s) are mapped to this Evaluation Template: </td>
                        </tr>
                    </table>
                    <table width="90%" class="sortable" id="tblSearchResults2" cellpadding="0" cellspacing="0" style="width:90%; border-left:1px solid gray; border-right: medium none; display:block;" align="center">
                        <tr>
                            <th width="15%" align="left"><b>Evaluation Template</b></th>
                            <th width="35%" align="left" class="sort_up" onclick="ts_resortTable(this, 1);return false;"><b>Course Name</b></th>
                            <th width="35%" align="left" class="sort_up" onclick="ts_resortTable(this, 2);return false;"><b>Course Code</b></th>
                            <th width="15%"></th>
                           
                        </tr>
                    <%for(int j=0; j<evalTemplateMapping.length; j++){%>
                        <tr id="<%=evalTemplateMapping[j].getMappingId()%>">
    
                            
                            <td width="15%"><%=request.getParameter("hdnSelectedTemplate")%></td>
                            <td width="35%"><%=chkNull.isNull((evalTemplateMapping[j].getCourseName()))%></td>
                            <td width="35%"><%=chkNull.isNull((evalTemplateMapping[j].getCourseCode()))%></td>
                            <td width="15%"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_delete.gif" align="left" style="width:45px; height:19px" onClick="deleteMapping('<%=evalTemplateMapping[j].getMappingId()%>')" alt="Delete Mapping">
                            <!--<input type="text" id="lmsMappingId_<!--%=j--%>" name="mappedRecs_<!%--=j--%>" value=""> -->
                             <%-- <input type="hidden" id="lmsMappingId" name="mappedRecs" value="">
                             <input type="hidden" id="mappingIndex_<%=j%>" value="<%=j%>"> --%>
                            </td>
                        </tr>
                    <%}%>
                  
                </table> 
                
            <!--Added by Mahua on 26/11/2011-->
            <%}else if((savedRecords!=null && savedRecords.length!=0) && evalTemplateMapping==null){%>
                
                    <table width="90%" class="evalmappingtable" id="tblSearchResults1" cellpadding="0" cellspacing="0" style="border-right:1; border-bottom: 1; border-left: 1; border-top: 0;display:block; " align="center">
                        <tr>
                            <td style="position: relative;left: 65px;weight:bold;font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';"class="evalmappingtd">Following Course(s) are mapped to this Evaluation Template: </td>
                        </tr>
                    </table>
                    <table width="90%" class="sortable" id="tblSearchResults2" cellpadding="0" cellspacing="0" style="border: medium none;display:block; " align="center">
                    <tr>
                        <th width="15%" align="left"><b>Evaluation Template</b></th>
                        <th width="35%" align="left" class="sort_up" onclick="ts_resortTable(this, 1);return false;"><b>Course Name</b></th>
                        <th width="35%" align="left" class="sort_up" onclick="ts_resortTable(this, 2);return false;"><b>Course Code</b></th>
                        <th width="15%"></th>
                       
                    </tr>
                    <%for(int j=0; j<savedRecords.length; j++){%>
                    <tr id="<%=savedRecords[j].getMappingId()%>">

                        
                        <td width="15%"><%=request.getParameter("hdnSelectedTemplate")%></td>
                        <td width="35%"><%=chkNull.isNull((savedRecords[j].getCourseName()))%></td>
                        <td width="35%"><%=chkNull.isNull((savedRecords[j].getCourseCode()))%></td>

                        <td width="15%"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_delete.gif" align="right" style="width:45px; height:19px" onClick="deleteMapping('<%=savedRecords[j].getMappingId()%>')" alt="Delete Mapping">
                        <!-- <input type="hidden" id="lmsMappingId" name="mappedRecs" value=""> -->
                        <!--<input type="text" id="lmsMappingId_<!%--=j--%>" name="mappedRecs_<%=j%>" value="">-->
                        <%-- <input type="hidden" id="mappingIndex_<%=j%>" value="<%=j%>"> --%>

                        </td>
                    </tr>
                <%}%>
                </table> 
              
            
            <!--End added by Mahua on 26/11/2011-->
            
            <%}else{%>
            <table width="90%" id="tblSearchResults" style="cellpadding:0; cellspacing:0;border-right:0; border-bottom: 0; border-left: 0; border-top: 0;display:block;" class="evalmappingtable" align="center">
                <tr>
                    <td class="evalmappingtd">
                      <font style="color:red; font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';">No courses have been mapped to the selected Evaluation Template</font>                     
                    </td>
                </tr>
            </table>
            <%}}%>
        </div><!-- end #wrap -->
        </form>
    </body>
</html>