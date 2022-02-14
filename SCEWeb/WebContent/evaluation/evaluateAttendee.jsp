<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.db.SCEManagerImpl"%>

<%@ page import="com.pfizer.sce.beans.Attendee"%>
<%@ page import="com.pfizer.sce.beans.BusinessRule"%>
<%@ page import="com.pfizer.sce.beans.Descriptor"%>
<%@ page import="com.pfizer.sce.beans.EvaluationFormScore"%>
<%@ page import="com.pfizer.sce.beans.Event"%>
<%@ page import="com.pfizer.sce.beans.LegalQuestion"%>
<%@ page import="com.pfizer.sce.beans.LegalQuestionDetail"%>
<%@ page import="com.pfizer.sce.beans.SCE"%>
<%@ page import="com.pfizer.sce.beans.SCEDetail"%>
<%@ page import="com.pfizer.sce.common.SCEConstants"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="s" uri="/struts-tags"%>




<%@include file="IAM_User_Auth.jsp" %>
<style type="text/css">
@media print {
    .noprint { display: none; }
}

@media screen {
    .print { display: none; }
}
</style>

<%
    SCEManagerImpl sceManager = new SCEManagerImpl();
    String from = (String)request.getAttribute("from"); 
    String isSubmitted = (String)request.getAttribute("isSubmitted"); 
    boolean createMode = true;  
    // System.out.println("Inside evaluateAttendee");
    SCE objSCE = (SCE)request.getAttribute("sceToDisplay");
    
    String templateTitle = objSCE.getTemplatetitle();
    
    String overallEvaluationLable = objSCE.getOverallEvaluationLable();
    
    
    /* For Evaluate */
    Attendee[] attendees = (Attendee[])request.getAttribute("attendees");  
    
    String evalEmplId = null;
    Attendee evalAttendee = null;
    
    pageContext.setAttribute("submitButton",request.getContextPath()+"/evaluation/resources/_img/button_submit.gif");    
    pageContext.setAttribute("cancelButton",request.getContextPath()+"/evaluation/resources/_img/button_cancel.gif");    
    DateFormat dateFormatter = new SimpleDateFormat(SCEUtils.DEFAULT_DATE_FORMAT);    

    boolean hasPrecall = true;
    boolean hasPostcall = true;
    String precall_text = null;
    String postcall_text = null;
 
    if (objSCE != null) {        
        hasPrecall = objSCE.getPrecallComments() == null || "".equals(objSCE.getPrecallComments());
        hasPostcall = objSCE.getPostcallComments() == null || "".equals(objSCE.getPostcallComments());
        precall_text = objSCE.getPrecallComments();
        postcall_text = objSCE.getPostcallComments();
    }
    
    boolean ifRBU = false;
    
     if (objSCE != null && objSCE.getEventId() != null) {  
        if(objSCE.getEventId().intValue() == 8 || objSCE.getEventId().intValue() == 9 || objSCE.getEventId().intValue() == 4 ){
            ifRBU = true;
            hasPrecall = true;
            hasPostcall = true;
        }

     }
    
    boolean showNA = true;
    if (objSCE != null && objSCE.getEventId() != null && objSCE.getEventId().intValue() == 1) {
        showNA = false;
    }
    String closeButtonStatus = null;
    if(request.getAttribute("closeButtonStatus") != null){
        closeButtonStatus = request.getAttribute("closeButtonStatus").toString();
    }
    // shinoy added 31st DEC
    
%>
<%if (objSCE != null && objSCE.getId() != null) {%>
<object id="factory" viewastext  style="display:none"
  classid="clsid:1663ed61-23eb-11d2-b92f-008048fdd814"
  codebase="<%=request.getContextPath()%>/evaluation/smsx.cab#Version=6,3,434,26">
</object>
<%}%>

<script language="javascript">

	/*Added by Ankit 27April2016*/
	
	/*End*/

    var overriden = false;
    var prevObj;
    var questionMap = new Object();
    
    <%
    for (int i=0; i<objSCE.getSceDetailList().size(); i++) {
        SCEDetail aDetail = (SCEDetail)objSCE.getSceDetailList().get(i);
        
    %>
        questionMap[<%=aDetail.getQuestionId()%>] = '<%=aDetail.getTitle().replaceAll("'","\\\\u0027")%>';
    <% 
    }
    %>
    //Start:2020 Q3:added by muzees to avoid duplicate evaluation
    function disableSubmit(){
    	
    	document.getElementById("submiteval").disabled = true;
    	if(validateSubmit()){
    		return true;
    	}
    	else {
    		
    		document.getElementById("submiteval").disabled = false;
    		return false;
    	}
    }//end of MUZEES
    function validateSubmit() {
    
        // Precall Rating
        <%if (objSCE.getPrecallFlag().equals("Y")){ %>
            var precallRadio = document.forms[0].precall_rating;
            var precallRating = get_radio_value(precallRadio);
            var precallText = document.getElementById('precall_comments');
          /*  if (precallRating == undefined) {
                alert('Please provide a rating for Precall');
                precallRadio[0].focus();
                return false;
            }
            */
            if (precallText.value.length > 4000){
                alert('Precall Comments: Max length allowed 4000 char');
                precallText.focus();
                return false;
            }
        <%}%>
        
        // Question Rating
        var questionRadio;
        var questionRating;
        var questionText;
        var notApplicableCount=1;
        var questionLength = eval('<%=objSCE.getSceDetailList().size()%>');
        <%
        for (int i=0; i<objSCE.getSceDetailList().size(); i++) {
            SCEDetail aDetail = (SCEDetail)objSCE.getSceDetailList().get(i);
        %>
        questionRadio = eval('document.forms[0].question_rating_' + '<%=aDetail.getQuestionId() %>');
        questionRating = get_radio_value(questionRadio);
        questionText = eval('document.forms[0].question_comments_' + '<%=aDetail.getQuestionId()%>');
        isNotApplicable = eval('document.forms[0].precall_checkbox_' + '<%=aDetail.getQuestionId()%>').checked;
        //added by saikat
        <% 
        EvaluationFormScore objEvaluationFormScore = null;                                        
        if (objSCE != null) {
        for (Iterator scoreItr =  objSCE.getEvaluationFormScoreList().iterator(); scoreItr.hasNext();) {                                
        objEvaluationFormScore =  (EvaluationFormScore)scoreItr.next();
        %>
        var eleName="score_comments_<%=aDetail.getQuestionId()%>_<%=objEvaluationFormScore.getScoreLegend()%>";
        
        //alert("value of eleName:"+<%=aDetail.getQuestionId()%>+"||" + <%=objEvaluationFormScore.getScoreLegend()%>+ "||"+ document.getElementById(eleName).value);
        if (questionText.value.length == 0 && questionRating=='<%=objEvaluationFormScore.getScoreLegend()%>' && document.getElementById(eleName).value=='Y' ){
            alert('Please fill out comments for the following skill :\n' + '<%=aDetail.getTitle().replaceAll("'","\\\\u0027")%>');
         //   alert('saikat2');
            questionText.focus();
            return false;
       }
       
        <% }
        }%>

        if(!isNotApplicable){
            if (questionRating == undefined) {        
                alert('Please provide a rating for the following skill :\n' + '<%=aDetail.getTitle().replaceAll("'","\\\\u0027")%>');
                questionRadio[0].focus();
                return false;
            }
        
           // if (questionText.value.length == 0 && questionRating!='DC' ) {        
           //     alert('Please fill out comments for the following skill :\n' + '<%=aDetail.getTitle().replaceAll("'","\\\\u0027")%>');
                 
            //    questionText.focus();
           //     return false;
          //  }
        
            if (questionText.value.length > 4000){                 
                alert('<%=aDetail.getTitle().replaceAll("'","\\\\u0027")%> Comments: Max length allowed 4000 char');
                
                questionText.focus();
                return false;
            }
            
            
        }
        else{
            if(notApplicableCount==questionLength){
                alert('At least one of the questions must not be marked as N/A');
                return false;
            }
        notApplicableCount++; 
        }
        <%
        }
        %>
                
        // Postcall Rating
        <%if (objSCE.getPostcallFlag().equals("Y")){ %>
            var postcallRadio = document.forms[0].postcall_rating;
            var postcallRating = get_radio_value(postcallRadio);
            var postcallText = document.getElementById('postcall_comments');
            /*if (postcallRating == undefined) {
                alert('Please provide a rating for Postcall');
                postcallRadio[0].focus();
                return false;
            }*/
            if (postcallText.value.length > 4000){
                alert('Postcall Comments: Max length allowed 4000 char');
                postcallText.focus();
                return false;
            }
            
            // Comments
            var commentsText = document.getElementById('comments');
            if (commentsText.value.length > 4000){
                alert('Comments: Max length allowed 4000 char');
                commentsText.focus();
                return false;
            }
        <%}%>
        
        <%  
            int legalQuestionNum1 = 0;
            LegalQuestion legalQuestion1 = null; 
            LegalQuestionDetail legalQuestionDetail1 = null;  
              if (objSCE != null && objSCE.getLegalFG() != null && objSCE.getLegalFG().equals("Y")) {
                    for (Iterator legalItr =  objSCE.getLegalQuestionList().iterator(); legalItr.hasNext();) {
                    legalQuestionNum1++;
                    legalQuestion1 =  (LegalQuestion)legalItr.next();
                    for(Iterator legalQuesValueItr = objSCE.getLegalQuestionDetailList().iterator(); legalQuesValueItr.hasNext();){  
                       legalQuestionDetail1 = (LegalQuestionDetail)legalQuesValueItr.next();
                       if(legalQuestion1.getId().equals(legalQuestionDetail1.getLegalQuestionId())){
                    legalQuestion1.setLegalQuestionValue(legalQuestionDetail1.getLegalQuestionValue());
                        }
                    }                             

             %>             

      legalradioyes=document.getElementById('healthcare_compliant_yes_<%=legalQuestion1.getDisplayOrder()%>');
      legalradiono=document.getElementById('healthcare_compliant_no_<%=legalQuestion1.getDisplayOrder()%>');
      if(!legalradioyes.checked && !legalradiono.checked )
      {

      alert("Please select Legal Question value for: "+"<%=legalQuestion1.getLegalQuestionLabel()%>");
      legalradioyes.focus();
      return false;
      }
      <% 
        }                         
     }

       %>


        
        // Overall Rating
        var overallRadio = document.forms[0].overall_rating;
        var overallRating = get_radio_value(overallRadio); //alert("overallRating:"+overallRating);
        document.forms[0].overall_rating_H.value = overallRating;
        var overallText = document.getElementById('overall_comments');
        if (overallRating == undefined) {
            alert('Please provide Overall rating');
            return false;
        }
        
        if (overallText.value.length == 0 && overallRating!='DC') {
            alert('Please fill out comments for Overall Sales Call Evaluation');
            overallText.focus();
            return false;
        }

        if (overallText.value.length > 4000){
            alert('Overall Comments: Max length allowed 4000 char');
            overallText.focus();
            return false;
        }
        
        
        document.forms[0].action="submitSCE";
        document.forms[0].submit();
        
        return true;   
    }
    
    function validateNotApplicable(){
        
        // Precall Rating
        <%if (objSCE.getPrecallFlag().equals("Y")){ %>
            var precallRadio = document.forms[0].precall_rating;
            var precallRating = get_radio_value(precallRadio);
            var precallText = document.getElementById('precall_comments');
            isPrecallNotApplicable = eval('document.forms[0].precall_checkbox').checked;
            
            if(isPrecallNotApplicable){
                setCheckedValue(precallRadio, '');
                precallText.value='';
                for(var j=0;j<precallRadio.length;j++){
                    precallRadio[j].disabled = true;
                }          
                precallText.disabled = true;
            }           
            else{
                for(var j=0;j<precallRadio.length;j++){
                    precallRadio[j].disabled = false;
                }
                precallText.disabled = false;
            }
        <%}%>
        
        // Postcall Rating
        <%if (objSCE.getPostcallFlag().equals("Y")){ %>
            var postcallRadio = document.forms[0].postcall_rating;
            var postcallRating = get_radio_value(postcallRadio);
            var postcallText = document.getElementById('postcall_comments');
            var comments = document.getElementById('comments');
            isPostcallNotApplicable = eval('document.forms[0].postcall_checkbox').checked;
            
            if(isPostcallNotApplicable){
                setCheckedValue(postcallRadio, '');
                postcallText.value='';
                comments.value='';
                for(var j=0;j<postcallRadio.length;j++){
                    postcallRadio[j].disabled = true;
                }          
                postcallText.disabled = true;
                comments.disabled = true;
            }           
            else{
                for(var j=0;j<postcallRadio.length;j++){
                    postcallRadio[j].disabled = false;
                }
                postcallText.disabled = false;
                comments.disabled = false;
            }
        <%}%>                        
    }
    
    function get_radio_value(elem)
    {
        var rad_val;        
        for (var i=0; i<elem.length; i++)
        {
            if (elem[i].checked)
            {
                rad_val = elem[i].value;
                break;
            }
        }
        return(rad_val);
    }
    
    function setCheckedValue(radioObj, newValue) {
        if(!radioObj)
            return;
        var radioLength = radioObj.length;
        if(radioLength == undefined ) {
            radioObj.checked = (radioObj.value == newValue.toString());
            return;
        }
        for (var i = 0; i < radioLength; i++) {
            radioObj[i].checked = false;
            if(radioObj[i].value == newValue.toString()) {
                radioObj[i].checked = true;
            }
        }
    }
        
    
    function resetComments(obj) {
        var isQuestion = false;
        if (prevObj == undefined || prevObj == null) {
            return true;
        }
        else {
            prevName = prevObj.name;
            prevId = prevName.replace('question_rating_','');
        
            curName = obj.name;
            
            if (curName.indexOf('question_comments_') > -1) {
                // This is a Question
                isQuestion = true;
                curId = curName.replace('question_comments_','');
                
                if (prevId == curId) {
                    // Same Group
                    return true;
                } 
            }
                                
            
            prevText = document.getElementById('question_comments_' + prevId);
            if (prevText.value.length == 0) {
                if (isQuestion) {
                    hComments = document.getElementById('question_comments_H_' + curId).value;
                }
                else {
                    hComments = document.getElementById(curName + '_H').value;
                }
                obj.value = hComments;
                alert('Please fill out comments for the following skill\n' + questionMap[prevId]);
                prevText.focus();
                return false; 
            }
        }  
        return true;
    }
    
    function checkCommentsFilled(obj) {
        var isQuestion = false;                 
        curName = obj.name;
            
        if (curName.indexOf('question_rating_') > -1) {
            // This is a Question
            isQuestion = true;
            curId = curName.replace('question_rating_','');
        }
        var prevId = curId-1;    
        prevText = document.getElementById('question_comments_' + prevId);
        if (prevText.value.length == 0) {
            if (isQuestion) {
                hRating = document.getElementById('question_rating_H_' + curId).value;
                curRadio = eval('document.forms[0].question_rating_' + curId);
                setCheckedValue(curRadio,hRating);
            }
            else {
                hRating = document.getElementById(curName + '_H').value;
                curRadio = eval('document.forms[0].' + curName);
                setCheckedValue(curRadio,hRating);
            }
            alert('Please fill out comments for the following skill\n' + questionMap[prevId]);
            prevText.focus();
            return false; 
        }
            return true;    
    }
    
   

    function setOverallRatingHLC(obj) {
    
        var questionRadio;
        var questionRating;
        var bussRuleScore='';
        
        //get all scores for the template
        var scoreLength= '<%=objSCE.getEvaluationFormScoreList().size()%>';
        
        //create map to store count of question who got a score
        var formScoreMap = new Object();
        var formScoreArr = new Array();
        
        <%
        for (int j=0; j<objSCE.getEvaluationFormScoreList().size(); j++) {
            EvaluationFormScore score = (EvaluationFormScore)objSCE.getEvaluationFormScoreList().get(j);
        %>
        formScoreMap['<%=score.getScoreLegend()%>']= 0;
        formScoreArr['<%=j%>']='<%=score.getScoreLegend()%>';
        <%}%>
        
        <%
        for (int i=0; i<objSCE.getSceDetailList().size(); i++) {
            SCEDetail aDetail = (SCEDetail)objSCE.getSceDetailList().get(i);
        %>
        
        questionRadio = eval('document.forms[0].question_rating_' + '<%=aDetail.getQuestionId()%>');
        questionRating = get_radio_value(questionRadio);
        questionText = eval('document.forms[0].question_comments_' + '<%=aDetail.getQuestionId()%>');
        isCritical = <%="Y".equalsIgnoreCase(aDetail.getCritical())%>;
        isNotApplicable = eval('document.forms[0].precall_checkbox_' + '<%=aDetail.getQuestionId()%>').checked;
        
        for(var k=0; k<scoreLength; k++){        
            if (!isNotApplicable && isCritical && questionRating == formScoreArr[k]) {
                formScoreMap[formScoreArr[k]]++;
            }
        }
        
        if(isNotApplicable){
            setCheckedValue(questionRadio, '');
            questionText.value='';
           // document.forms[0].question_comments_' + '<%=aDetail.getQuestionId()%> = '';
            for(var j=0;j<questionRadio.length;j++){
                questionRadio[j].disabled = true;
            }          
            questionText.disabled = true;           
        }
        else{
            for(var j=0;j<questionRadio.length;j++){
                questionRadio[j].disabled = false;
            }
            questionText.disabled = false;
        }
        <%
        }
        %>
        //get business rules for template
        var busOrderArr         = new Array();
        var busCriticalQuesArr  = new Array();
        var busScoreArr         = new Array();
        var busOverallScoreArr  = new Array();
        var overallScoreArr     = new Array();
        var busRuleLength       = '<%=objSCE.getBusinessRulesList().size()%>';
        
        var k=0;

        var legalQuestionLength = <%=objSCE.getLegalQuestionList().size()%>

        // Overall Rating
        var overallRadio = document.forms[0].overall_rating;
        
        // Health Care Compliance
        var radioCheck=0;
        var isHlcCritical = <%="Y".equalsIgnoreCase(objSCE.getHlcCritical())%>;
      
        <%if(objSCE.getLegalFG() != null && objSCE.getLegalFG().equals("Y")){%> 
            for(var i=1; i<=legalQuestionLength; i++){ 
                     
                var hlcRadio = eval('document.forms[0].healthcare_compliant_'+i);
                if(isHlcCritical){
                    if (get_radio_value(hlcRadio) == 'N') {
                        setCheckedValue(overallRadio, '<%=objSCE.getHLCCriticalValue()%>');
                        document.forms[0].overall_rating_H.value = '<%=objSCE.getHLCCriticalValue()%>';
                        for(var j=0;j<overallRadio.length;j++){
                            document.forms[0].overall_rating[j].disabled = true;
                        }            
                        return false;
                    }
                    else{
                        setCheckedValue(overallRadio, '');
                        document.forms[0].overall_rating_H.value ='';
                        for(var j=0;j<overallRadio.length;j++){
                        document.forms[0].overall_rating[j].disabled = false;
                        } 
                    }
                }
            }
       <% }%>

        
        <%
        for (int i=0; i<objSCE.getBusinessRulesList().size(); i++) {
            BusinessRule businessRules = (BusinessRule)objSCE.getBusinessRulesList().get(i);
        %>
        busOrderArr       ['<%=i%>'] = '<%=businessRules.getDisplayOrder()%>';
        busCriticalQuesArr['<%=i%>'] = '<%=businessRules.getCriticalQuestions()%>';
        busScoreArr       ['<%=i%>'] = '<%=businessRules.getScore()%>';
        busOverallScoreArr['<%=i%>'] = '<%=businessRules.getOverallScore()%>';
        
        <%}%>
        //flag added by pankaj to disable overall rating
        var overFlag = false;
        
        for(var i=0; i<scoreLength; i++){
       
            for(var j=0; j<busRuleLength; j++){
             //alert(busCriticalQuesArr[j]+busScoreArr[j]+' '+formScoreMap[formScoreArr[i]]+formScoreArr[i]);
                if(busCriticalQuesArr[j]<=formScoreMap[formScoreArr[i]] && busScoreArr[j] == formScoreArr[i] ){
                    overallScoreArr[k]= busOverallScoreArr[j]; 
                    overFlag = true;
                    //alert(overallScoreArr[k]);
                    k++;
                }  
            }
        }
        
        //alert(''+k+busRuleLength+busCriticalQuesArr[0]+formScoreMap[formScoreArr[0]]+overallScoreArr[0]+overallScoreArr[1]+overallScoreArr[2])
        var isConflict = false;
        var i=0;
        
        //check if there is conflict in overall score
        //for(var i=0; i<overallScoreArr.length; i++){
        while(i<overallScoreArr.length){
            if(overallScoreArr[0] != overallScoreArr[i] ){
                isConflict = true;
            }
            i++;
        }
        
        //set overall score based on conflict overall score
        if(overallScoreArr.length>0){
            if(isConflict){
                setCheckedValue(overallRadio, '<%=objSCE.getConflictOverallScore()%>');
                
                document.forms[0].overall_rating_H.value ='<%=objSCE.getConflictOverallScore()%>';
                bussRuleScore='<%=objSCE.getConflictOverallScore()%>';
                
                 for(var j=0;j<overallRadio.length;j++){
                   document.forms[0].overall_rating[j].disabled = true;
                } 
            }
            
            else{
                setCheckedValue(overallRadio, overallScoreArr[0]);
                
                document.forms[0].overall_rating_H.value =overallScoreArr[0];
                bussRuleScore=overallScoreArr[0];
                if(overFlag){
                 for(var j=0;j<overallRadio.length;j++){
                  document.forms[0].overall_rating[j].disabled = true;
                } 
               }       
              }      
            return false;
        }
        else if(overallScoreArr.length<=0) {
              for(var j=0;j<overallRadio.length;j++){
              document.forms[0].overall_rating[j].disabled = false;
        } 
                setCheckedValue(overallRadio, '');
                return false;
               }
        else if(get_radio_value(overallRadio)==null){
            setCheckedValue(overallRadio, '');
        }        
    }
    
    
    function selectAnotherAttendee() {
        var sel_attendee = document.getElementById('attendee_dropdown');
        var attendeeVal = sel_attendee.options[sel_attendee.selectedIndex].value;
        if (attendeeVal != '') {
            window.document.getElementById.forms[0].action="selectAnotherAttendee";                
            window.document.forms[0].submit();
        }
        else {
            return false;
        }
    }
    
    function backToEvaluationResults() {
        window.document.forms[0].action="backToEvaluationResults";                
        window.document.forms[0].submit();        
    }
    
    function backToScePendingReport() {
        window.document.forms[0].action="backToScePendingReport";                
        window.document.forms[0].submit();        
    }
    
    function backToSelectAttendee() {
        window.document.forms[0].action="backToSelectAttendee";                
        window.document.forms[0].submit();         
    }
    
    function cancel() {
        window.document.forms[0].action="cancelEvaluation";                
        window.document.forms[0].submit();         
    }
    
    function zoomScaleBeforePrint() {			
        var originalHeader, originalFooter;
        originalHeader = factory.printing.header;
        originalFooter = factory.printing.footer;			
        factory.printing.header = "";			
        //factory.printing.footer = "&w&bPage &p of &P&b&D";
        factory.printing.footer = "&bPage &p of &P&b&D"	
        factory.printing.portrait = true;  						
        document.focus();
        document.body.style.zoom='1';
        factory.printing.leftMargin = 0.167;
        factory.printing.topMargin = 0.167;
        factory.printing.rightMargin = 0.18;
        factory.printing.bottomMargin = 0.18;
        window.print();
        //reset the header and footer, and zoom back
        factory.printing.header = originalHeader;
        factory.printing.footer = originalFooter;
        document.body.style.zoom='1';        
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
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/content_print.css" rel="stylesheet" type="text/css" media="print" />
        <!--[if IE 6]><LINK href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" type=text/css rel=stylesheet><![endif]-->        
    </head>
    <body id="p_evaluation_form">
    	<div id="wrap">
            <s:form action="submitSCE"   Id="evaluateForm">         
            
            <% 	createMode = (SCEConstants.CREATE_MODE).equalsIgnoreCase((String)pageContext.findAttribute("mode")); 
            	// System.out.println("create Mode **************************"+ createMode); 
            %>
              	       

            <%
                
            	evalAttendee= sceManager.getAttendeeByEmplId((String)pageContext.findAttribute("evalEmplId"));            	  
            	// System.out.println("Evaluate Attende******************"+evalAttendee);
            %>
            
            
            <div class="noprint">
            <div id="top_head_eval">
                <h1>Pfizer</h1>
                <h2><%=templateTitle%></h2>
                
                <%@include file="navbar.jsp" %>
                <!-- end #top_head -->
            </div>
            
            
            <div id="top_head_eval_1">
              <div align="right" style="float: right;">
                    <% if(closeButtonStatus != null && closeButtonStatus.equals("active")){%>
                    <img src="<%=request.getContextPath()%>/evaluation/resources/_img/btn_close.gif" alt="Close" width="38" height="19" onclick="window.close()"/>
                <%}else {%>
                    <img src="<%=request.getContextPath()%>/evaluation/resources/_img/buttons_backtoeval.gif" alt="Back To Evaluation Results" width="159" height="20" onclick="return backToEvaluationResults()"/>                    
                <% }%>                    
                    </div>
                <div id="eval_breadcrumbs_1"> <font color="white">
                    <!-- <netui-data:getData resultId="productName" value="{actionForm.evalProduct}" /> -->
                    
                    <%                    
                        if (createMode) {
                            if ("evaluate".equalsIgnoreCase(from)) {
                    %>
                        <%--<netui:label value="{actionForm.evalCourse}"/> > Classroom (<netui:label value="{actionForm.evalClassroom}"/>) > Table (<netui:label value="{actionForm.evalTable}"/>) > <span class="active"><%=evalAttendee.getName()%></span>--%>
                        <%=SCEUtils.ifNull(pageContext.findAttribute("evalCourse"))%> > <span class="active"><%=evalAttendee.getName()%></span>
                    <%
                            }
                            else {
                                if ("No".equalsIgnoreCase(isSubmitted)) {
                    %>
<!--                         <netui-data:getData resultId="evalClassroom" value="{actionForm.evalClassroom}" />
                        <netui-data:getData resultId="evalTable" value="{actionForm.evalTable}" /> -->

                        
                        <%--<netui:label value="{actionForm.evalCourse}"/> > Classroom (<netui:label value="{actionForm.evalClassroom}"/>) > Table (<netui:label value="{actionForm.evalTable}"/>) > <span class="active"><%=evalAttendee.getName()%></span> --%>
                        <%if (pageContext.findAttribute("evalCourse") != null) {%>                        
                        <%=SCEUtils.ifNull(pageContext.findAttribute("evalCourse"))%> > 
                        <%} else {%>                     
                        <%=SCEUtils.ifNull(pageContext.findAttribute("evalProduct"))%> > 
                        <%}%>
                        <%=(pageContext.findAttribute("evalClassroom") != null) ? ("Classroom (" + pageContext.findAttribute("evalClassroom") + ") > ") : "" %>
                        <%=(pageContext.findAttribute("evalTable") != null) ? ("Table (" + pageContext.findAttribute("evalTable") + ") > ") : "" %>
                        <span class="active"><%=evalAttendee.getName()%></span>                                               
                    <% 
                                }
                                else {
                    %>
                        <%=SCEUtils.ifNull(pageContext.findAttribute("evalProduct"))%> > <span class="active"><%=evalAttendee.getName()%></span>
                        <%
                                }           
                            }
                        } 
                        else {
                    %>
                    <%=SCEUtils.ifNull(objSCE.getCourse(),objSCE.getProduct())%> > 
                    <%=(objSCE.getClassroom() != null && !objSCE.getClassroom().trim().equals("")) ? ("Classroom (" + objSCE.getClassroom().trim() + ") > "): "" %>
                    <%=(objSCE.getTableName() != null && !objSCE.getTableName().trim().equals("")) ? ("Table (" + objSCE.getTableName().trim() + ") > "): "" %>
                    <span class="active"><%=evalAttendee.getName()%></span>
                    <%
                        }
                    %>
                 </font>   
                </div>
                <div id="eval_sub_nav1">                
                    <%if ("evaluate".equalsIgnoreCase(from)) {%>
                    <%--<netui:anchor action="backToEventInfo"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoinfo.gif" alt="Back to Event Info" width="119" height="18"></netui:anchor>--%>
                    <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoselectattendee.gif" alt="Back to Select Attendee" width="148" height="19" onclick="return backToSelectAttendee()">
                    <select id="attendee_dropdown" name="emplId" onchange="return selectAnotherAttendee()">
                        <option label="Select Another Attendee" value="" selected="selected">Select Another Attendee</option>
                        <%
                        if (attendees != null && attendees.length != 0) {
                            for (int i=0; i<attendees.length; i++) {
                        %>
                        <OPTION label="<%=attendees[i].getNameComma()%>" value="<%=attendees[i].getEmplId()%>"><%=attendees[i].getNameComma()%></OPTION>
                        <%
                            }
                        }
                        %>                    
                    </select>
                    <%} else if ("search".equalsIgnoreCase(from)) {%>
                    <!-- Preserve Search -->
                  
<%--                 
					Gadalp : This code needs to be modified
					<netui:hidden dataSource="{actionForm.lastNameSrch}" />
                    <netui:hidden dataSource="{actionForm.firstNameSrch}" />
                    <netui:hidden dataSource="{actionForm.emplIdSrch}" />
                    <netui:hidden dataSource="{actionForm.eventIdSrch}" />
                    <netui:hidden dataSource="{actionForm.territoryIdSrch}" />
                    <netui:hidden dataSource="{actionForm.isPassportSrch}" />
                    <netui:hidden dataSource="{actionForm.selEmplIdSrch}" /> --%>

						<s:hidden name="lastNameSrch" />
						<s:hidden name="firstNameSrch" />
						<s:hidden name="emplIdSrch" />
						<s:hidden name="eventIdSrch" />
						<s:hidden name="territoryIdSrch" />
						<s:hidden name="isPassportSrch" />
						<s:hidden name="selEmplIdSrch" />


						<%
							} else {
						%>
                    <s:hidden name="filterClassroom" />
                    <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_back.gif" alt="Back To SCE Report" width="53" height="17" onclick="return backToScePendingReport()"/>                    
                    <%}%>
                    <%if (!createMode && isGuestTrainer ) {%>
                    <img src="<%=request.getContextPath()%>/evaluation/resources/_img/buttons_print.gif" alt="Print" width="64" height="18" style="visibility:hidden;" onclick="window.print();"/>
                    <%} else{%> 
                    <img src="<%=request.getContextPath()%>/evaluation/resources/_img/buttons_print.gif" alt="Print" width="64" height="18" onclick="window.print();"/>
                    <%}%>
                    
                    </div>      
                      
                </div>
                
                    <input type="hidden" name="attendee_firstName" id="attendee_firstName" value="<%=evalAttendee.getFirstName()%>"/>
                    <input type="hidden" name="attendee_lastName" id="attendee_lastName" value="<%=evalAttendee.getLastName()%>"/>
                    <input type="hidden" name="form_title" id="form_title" value="<%=objSCE.getFormTitle()%>"/>
                    <input type="hidden" name="evaluator_name" id="form_title" value="<%=user.getName()%>"/>
                    
            
         
        </div>
           <!-- <div style="float: right;">
            -->
            <div class="noprint">
             <table id="logo">
               <tr><!--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                    <td style="float: right;" width="20%"></td>
                    <td width="80%" style="float: none;" align="center"><b><%=SCEUtils.ifNull(objSCE.getFormTitle(),"")%></b></td>
                    
                    <td style="margin-right: 0px" width="20%"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/icon_compliance.gif" alt="" width="53" height="56" style="float: right;" /></td>
                </tr>
            </table></div> <!--</div>-->
            
            
            <div class="print">
            <table id="logo">
                <tr>
                    <td style="text-align:left" width="20%"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/pfizer_logo.gif" alt="" style="width:88px"/></td>
                    <td width="80%"><b><%=SCEUtils.ifNull(objSCE.getFormTitle(),"")%></b></td>
                    <td id="logo" width="20%"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/icon_compliance.gif" alt="" width="53" height="56" /></td>
                </tr>
            </table>
            </div>
        	
            <div id="main_content" >
			    <div id="eval_legend1" >
             <!--<table id="footnote_box"><tr class="shade_back" >-->Legend:
                            <%  
                            EvaluationFormScore objEvaluationFormScore = null;                                   
                            if (objSCE != null) {
                            for (Iterator scoreItr =  objSCE.getEvaluationFormScoreList().iterator(); scoreItr.hasNext();) {                                
                                objEvaluationFormScore =  (EvaluationFormScore)scoreItr.next();
                            %>
                               <b><%=objEvaluationFormScore.getScoreLegend()%></b> = <%=objEvaluationFormScore.getScoreValue() %>                       
                            <%}}%>
                </div>
				
                <%
                    String evaluatorName = "";
                %>
                <div class="noprint">
                <div id="rep_name">
                    <p>
                        <b>Representative name:</b> <%=evalAttendee.getName()%>
                        <span class="spacer"></span>
                        <b>Submitted By:</b>
                        <%
                        if (createMode) {
                            evaluatorName = user.getName();
                        } 
                        else {
                            Attendee evaluator = sceManager.getAttendeeByEmplId(objSCE.getSubmittedByEmplId());
                            evaluatorName = evaluator != null ? evaluator.getName() : "Anonymous User";
                            if (evaluator != null) {
                                evaluatorName = evaluator.getName();
                            }                            
                            else {
                                User evalUser = sceManager.getUserByEmplId(objSCE.getSubmittedByEmplId());
                                evaluatorName = evalUser != null ? evalUser.getName() : "Anonymous User";
                            }
                        }
                        %>
                        <%=evaluatorName%>
                        <span class="spacer"></span>
                        <b>Date:</b> <%=SCEUtils.ifNull(createMode ? new java.util.Date() : objSCE.getSubmittedDate(), dateFormatter)%>                                                
                        <span class="spacer"></span>
                        <b>Product:</b> 
                        <%=createMode ? pageContext.findAttribute("evalProduct") : objSCE.getProduct()%>                       
                    </p>		
                </div>
                </div>

                <div class="print">                	
                <div id="rep_name">
                    <p>
                        <b>Representative name:</b> <%=evalAttendee.getName()%>
                        <span class="spacer"></span>
                        <b>Evaluator Name:</b>
                        <%=evaluatorName%>
                        <span class="spacer"></span>
                        <b>Date:</b> <%=SCEUtils.ifNull(createMode ? new java.util.Date() : objSCE.getSubmittedDate(), dateFormatter)%>                                                
                        <span class="spacer"></span>
                        <b>Product:</b> 
                        <%=createMode ? pageContext.findAttribute("evalProduct") : objSCE.getProduct()%>                       
                    </p>		
                </div>
                </div>
			<%if (objSCE.getPrecallFlag().equals("Y")||objSCE.getPreCallImage().equals("Y"))
			{ %>
                <div class="arrows_content">
                	<%if(objSCE.getPreCallImage().equals("Y")){ %>
                    <img class="arrows" src="<%=request.getContextPath()%>/evaluation/resources/_img/arrow_pre.gif" alt="Pre Call" width="78" height="27" />
                    <%} %>
                    <%if(objSCE.getPrecallFlag().equals("Y")){ %>
                    <label><%=objSCE.getPrecallLabel()%>:</label>
                	<%} %>
                    <table cellspacing="0">
                        <tr>
                            <%                                       
                            if (objSCE != null) {
                            for (Iterator scoreItr =  objSCE.getEvaluationFormScoreList().iterator(); scoreItr.hasNext();) {                                
                                objEvaluationFormScore =  (EvaluationFormScore)scoreItr.next();
                            %>                                
                                <td><input class="radio_btn" name="precall_rating" type="radio" value="<%=objEvaluationFormScore.getScoreLegend()%>" <%=(objEvaluationFormScore.getScoreLegend()).equals(objSCE.getPrecallRating()) ? "checked" : ""%> <%=createMode ? "" : "disabled"%>/><b><%=objEvaluationFormScore.getScoreLegend()%></b></td>
                            <%}}%>
                            <td><input class="checkbox" align="right"  name="precall_checkbox" type="checkbox" value="N" <%=("N").equals(objSCE.getPrecallNA()) ? "checked ='yes' " : ""%> <%=createMode ? "" : "disabled"%> onClick="validateNotApplicable(this)"><b>N/A </b>  </td> 
                        </tr>
                        <tr>
                            <td colspan="<%=objSCE.getEvaluationFormScoreList().size()%>">
                            <div class="noprint">
                            <textarea class="eval_comments" rows="3" cols="1" name="precall_comments" id="precall_comments" <%=(hasPrecall && createMode) ? "" : "readonly"%> <%=hasPrecall ? "" : "disabled"%>><%=(!hasPrecall && createMode) ? precall_text : SCEUtils.ifNull(objSCE.getPrecallComments(),"")%></textarea>
                            </div>
            
                            <div class="print">
                            <p><%=SCEUtils.replace((!hasPrecall && createMode) ? precall_text : SCEUtils.ifNull(objSCE.getPrecallComments(),""),"\r\n","<br>")%></p>
                            </div>
                            </td>        
                            <%if (createMode && !hasPrecall) {%>
                            <input type="hidden" name="precall_comments_def" id="precall_comments_def" value="<%=precall_text%>"/>                            
                            <%}%>
                        </tr>
                    </table>
                
                </div> <!--End arrows content-->
			 <hr/>
             <%}%>
                <div class="print"><hr /></div>
                
                
                <div class="arrows_content">
                <%if(objSCE.isCallImageDisplay()) {%>
                    <img class="arrows" src="<%=request.getContextPath()%>/evaluation/resources/_img/arrow_call.gif" alt="Call" width="78" height="27" />
                <%} %>
                <%if(objSCE.getCallLabelDisplay().equals("Y")) {%>
                    <p class="call_p"><b><%=objSCE.getCallLabelValue() %>:</b><br /></p>
                 <%} %>
                    <!--  <p class="footnote">(The bulleted points are just some of the points to consider when making your evaluation.)</p>-->
                </div> <!--End arrows content-->
				
                <div id="questions">	
                    <% 
                    SCEDetail objSCEDetail = null;
                    List objDescriptorList = null;
                    int questionNum = 0;
                    if (objSCE != null) {
                        for (Iterator iter =  objSCE.getSceDetailList().iterator(); iter.hasNext();) {
                            questionNum++;
                            objSCEDetail =  (SCEDetail)iter.next();
                            objDescriptorList = objSCEDetail.getDescriptorList(); 
                    %>        
                    <!-- Question 1 -->
                    
                    <div class="question_title" style="page-break-after: avoid">
                    
         <!-- rupu-->              <h5><%=SCEUtils.ifNull(objSCEDetail.getTitle(), "")%></h5>                     
                        <img class="question_right" src="<%=request.getContextPath()%>/evaluation/resources/_img/question_img_right.gif" alt="" width="14" height="17" />
                           
                    </div>
                    
                    
                    <div class="question_content" style="page-break-before: avoid">
                        <h6><%=questionNum%>. <%=SCEUtils.ifNull(objSCEDetail.getDescription(), "")%></h6>
                        <ul class="bullets">
                            <%
                            if (objDescriptorList != null && objDescriptorList.size() > 0) {
                                for (int i=0; i<objDescriptorList.size(); i++) {
                            %>
                            <li><%=SCEUtils.ifNull(((Descriptor)objDescriptorList.get(i)).getDescription(), "")%></li>                            
                            <%
                                }
                            }
                            %>
                        </ul>
                    </div> <!-- End #question_content" -->
			
                    <div class="question_eval_content">
                        <table cellspacing="0">
                            <tr>
                            <%                                     
                            if (objSCE != null) {
                            for (Iterator scoreItr =  objSCE.getEvaluationFormScoreList().iterator(); scoreItr.hasNext();) {                                
                                objEvaluationFormScore =  (EvaluationFormScore)scoreItr.next();
                            %>
                                <td><input class="radio_btn" name="question_rating_<%=objSCEDetail.getQuestionId()%>" type="radio"  value="<%=objEvaluationFormScore.getScoreLegend()%>" <%=(objEvaluationFormScore.getScoreLegend()).equals(objSCEDetail.getQuestionRating()) ? "checked" : ""%> <%=createMode ? "" : "disabled"%> onClick="setOverallRatingHLC(this)"/><b><%=objEvaluationFormScore.getScoreLegend()%></b></td>
                                <input type="hidden" name="score_comments_<%=objSCEDetail.getQuestionId()%>_<%=objEvaluationFormScore.getScoreLegend()%>" id="score_comments_<%=objSCEDetail.getQuestionId()%>_<%=objEvaluationFormScore.getScoreLegend()%>" value="<%=objEvaluationFormScore.getScorecommentFlag()%>"> 
                                <input type="hidden" name="score_legend_<%=objSCEDetail.getQuestionId()%>" id="score_legend_<%=objSCEDetail.getQuestionId()%>" value="<%=objEvaluationFormScore.getScoreLegend()%>"> 
                            <%}}%>
                            <td><input class="checkbox" align="right"  name="precall_checkbox_<%=objSCEDetail.getQuestionId()%>" type="checkbox" value="N" <%=("N").equals(objSCEDetail.getQuestionFg()) ? "checked ='yes' " : ""%> <%=createMode ? "" : "disabled"%> onClick="setOverallRatingHLC(this)"><b>N/A </b>  </td> 
                            </tr>
                            <tr>
                                <td colspan="<%=objSCE.getEvaluationFormScoreList().size()%>">
                                <div class="noprint">
                                <textarea rows="3" cols="1" name="question_comments_<%=objSCEDetail.getQuestionId()%>" id="question_comments_<%=objSCEDetail.getQuestionId()%>" <%=createMode ? "" : "readonly"%> onchange="" onkeyup="resetComments(this)"><%=SCEUtils.ifNull(objSCEDetail.getQuestionComments(),"")%></textarea>
                                </div>
                                <div class="print">
                                <p class="prnt"><%=SCEUtils.replace(SCEUtils.ifNull(objSCEDetail.getQuestionComments(),""),"\r\n","<br>")%></p>
                                </div>
                                </td>                                
                            </tr>
                        </table>
                    </div> <!-- End #question_eval_content -->
                    
                    <input type="hidden" name="question_critical_<%=objSCEDetail.getQuestionId()%>" value="<%=SCEUtils.ifNull(objSCEDetail.getCritical(),"N")%>">
                    <input type="hidden" name="question_rating_H_<%=objSCEDetail.getQuestionId()%>" value="">
                    <input type="hidden" name="question_comments_H_<%=objSCEDetail.getQuestionId()%>" value="">
                    
                    <div class="clear"></div>
                    <%
                        }
                    }
                    %>
                </div> <!-- End #questions -->
				
                <div class="clear"></div>
                <hr />
			<%if (objSCE.getPostcallFlag().equals("Y")||objSCE.getPostCallImage().equals("Y")){ %>
                <div class="arrows_content">
                	<%if (objSCE.getPostCallImage().equals("Y")){ %>
                    <img class="arrows" src="<%=request.getContextPath()%>/evaluation/resources/_img/arrow_post.gif" alt="Post Call" width="78" height="27" />
                    <%}%>
                    <%if (objSCE.getPostcallFlag().equals("Y")){ %>
                    <label>
                    <%=objSCE.getPostcallLabel()%>:</label>
                    <%}%>
                
                    <table cellspacing="0">
                    
                        <tr>
                            <%                                      
                            if (objSCE != null) {
                            for (Iterator scoreItr =  objSCE.getEvaluationFormScoreList().iterator(); scoreItr.hasNext();) {                                
                                objEvaluationFormScore =  (EvaluationFormScore)scoreItr.next();
                            %>                                
                                <td><input class="radio_btn" name="postcall_rating" type="radio" value="<%=objEvaluationFormScore.getScoreLegend()%>" <%=(objEvaluationFormScore.getScoreLegend()).equals(objSCE.getPostcallRating()) ? "checked" : ""%> <%=createMode ? "" : "disabled"%>/><b><%=objEvaluationFormScore.getScoreLegend()%></b></td>
                            <%}}%>
                            <td><input class="checkbox" align="right"  name="postcall_checkbox" type="checkbox" value="N" <%=("N").equals(objSCE.getPostcallNA()) ? "checked ='yes' " : ""%> <%=createMode ? "" : "disabled"%> onClick="validateNotApplicable(this)"><b>N/A </b>  </td> 
                            </tr>
                        <tr><!--take care1-->                        
                            <td colspan="<%=objSCE.getEvaluationFormScoreList().size()%>">                            
                            <div class="noprint">
                            <textarea class="eval_comments" rows="3" cols="1" name="postcall_comments" id="postcall_comments" <%=(hasPostcall && createMode) ? "" : "readonly"%> <%=hasPostcall ? "" : "disabled"%>><%=(!hasPostcall && createMode) ? postcall_text : SCEUtils.ifNull(objSCE.getPostcallComments(),"")%></textarea>
                            </div>
                            <div class="print">
                            <p><%=SCEUtils.replace((!hasPostcall && createMode) ? postcall_text : SCEUtils.ifNull(objSCE.getPostcallComments(),""),"\r\n","<br>")%></p>
                            </div>
                            </td>
                            <%if (createMode && !hasPostcall) {%>
                            <input type="hidden" name="postcall_comments_def" id="postcall_comments_def" value="<%=postcall_text%>"/>                            
                            <%}%>
                        </tr>
                    </table>			                    
                </div> <!--End arrows content-->	
               

			
                <div class="comments">
                    <label>Comments:</label>	
                    <table cellspacing="0">				
                        <tr>
                            <td colspan="3">
                            <div class="noprint">
                            <textarea class="eval_comments" rows="3" cols="1" name="comments" id="comments" <%=createMode ? "" : "readonly"%> onkeyup="resetComments(this)"><%=SCEUtils.ifNull(objSCE.getComments(),"")%></textarea>
                            </div>
                            <div class="print">
                            <p><%=SCEUtils.replace(SCEUtils.ifNull(objSCE.getComments(),""),"\r\n","<br>")%></p>
                            </div>
                            </td>
                        </tr>
                    </table>
                    <input type="hidden" name="comments_H" id="comments_H" value="">
                </div>
                <hr /> 
			 <%}%>	
			

              <!--Start Legal Section-->	

                       
                    <%  
                    	int legalQuestionNum = 0;
                        LegalQuestion legalQuestion = null; 
                        LegalQuestionDetail legalQuestionDetail = null;  
                                                          
                        if (objSCE != null && objSCE.getLegalFG() != null && objSCE.getLegalFG().equals("Y")) {
                            for (Iterator legalItr =  objSCE.getLegalQuestionList().iterator(); legalItr.hasNext();) {
                                legalQuestionNum++;
                                legalQuestion =  (LegalQuestion)legalItr.next();
                                for(Iterator legalQuesValueItr = objSCE.getLegalQuestionDetailList().iterator(); legalQuesValueItr.hasNext();){  
                                    legalQuestionDetail = (LegalQuestionDetail)legalQuesValueItr.next();
                                    if(legalQuestion.getId().equals(legalQuestionDetail.getLegalQuestionId())){
                                       legalQuestion.setLegalQuestionValue(legalQuestionDetail.getLegalQuestionValue()); 
                                    }
                                }                                  
                    %> 
                    <div id="questions">                                                  
                        <div class="question_title" style="page-break-after: avoid">
                            <h5><%=legalQuestion.getLegalQuestionLabel()%></h5>
                            <img class="question_right" src="<%=request.getContextPath()%>/evaluation/resources/_img/question_img_right.gif" alt="" width="14" height="17" />
                        </div>                                                                        
                        <div class="question_content" style="page-break-before: avoid">
                            <h6><i><%=legalQuestionNum%>. <%=legalQuestion.getLegalQuestion()%></i></h6>                                      
                        </div>                          
                        <div class="question_eval_content">
                            <table cellspacing="0">
                                <tr>
                                    <td> 
                                        <input class="radio_btn" name="healthcare_compliant_<%=legalQuestion.getDisplayOrder()%>" id="healthcare_compliant_yes_<%=legalQuestion.getDisplayOrder()%>"  type="radio" value="Y" <%=("Y").equals(legalQuestion.getLegalQuestionValue()) ? "checked" : ""%> <%=createMode ? "" : "disabled"%> onClick="setOverallRatingHLC(this)"/>Yes                                 
                                        <input class="radio_btn" name="healthcare_compliant_<%=legalQuestion.getDisplayOrder()%>" id="healthcare_compliant_no_<%=legalQuestion.getDisplayOrder()%>"  type="radio" value="N" <%=("N").equals(legalQuestion.getLegalQuestionValue()) ? "checked" : ""%> <%=createMode ? "" : "disabled"%> onClick="setOverallRatingHLC(this)"/>No
                                        <input type="hidden" name="healthcare_compliant_H" value="" >
                                    </td>
                                </tr>
                            </table>
                        </div>                                                             
                     </div>
                     <%}%>
                    <div class="clear"></div>
                    <hr />                           
                    <%}%> 
               
                
                 <!--Start Legal Section-->	
			
    			<div class="arrows_content overall">
        			<label><%=overallEvaluationLable %>:</label>
			
                    <table cellspacing="0">        
                        <tr>
                            <%                                     
                            if (objSCE != null) {
                                for (Iterator scoreItr =  objSCE.getEvaluationFormScoreList().iterator(); scoreItr.hasNext();) {                                
                                    objEvaluationFormScore =  (EvaluationFormScore)scoreItr.next();
                            %>                                
                                <td><input class="radio_btn" name="overall_rating" type="radio" value="<%=objEvaluationFormScore.getScoreLegend()%>" <%=(objEvaluationFormScore.getScoreLegend()).equals(objSCE.getOverallRating()) ? "checked" : ""%> <%=createMode ? "" : "disabled"%>/><b><%=objEvaluationFormScore.getScoreLegend()%></b></td>
                            <%  }
                            }
                            %>
                        </tr>
                        <tr>
                            <td colspan="<%=objSCE.getEvaluationFormScoreList().size()%>">
                            <div class="noprint">
                            <textarea class="eval_comments" rows="3" cols="1" name="overall_comments" id="overall_comments" <%=createMode ? "" : "readonly"%> onkeyup="resetComments(this)"><%=SCEUtils.ifNull(objSCE.getOverallComments(),"")%></textarea>
                            </div>
                            <div class="print">
                            <p class="prnt"><%=SCEUtils.replace(SCEUtils.ifNull(objSCE.getOverallComments(),""),"\r\n","<br>")%></p>
                            </div>
                            </td>        
                        </tr>
                    </table>
                    
                    <input type="hidden" name="overall_rating_H" value="">
                    <input type="hidden" name="overall_comments_H" id="overall_comments_H" value="">
                    			
                </div> <!--End arrows content-->
			
                <div class="noprint">
                <div id="submit_form">
                    <input type="hidden" name="from" value="<%=from%>"/>
                    
                    <s:hidden name="evalEmplId" />
                    <s:hidden name="evalEventId" />
                    <s:hidden name="evalTrainingDate" />
                    <s:hidden name="evalProduct" />
                    <s:hidden name="evalProductCode" />
                    <s:hidden name="evalCourse" />
                    <s:hidden name="evalCourseId" />
                    <s:hidden name="evalClassroom" />
                    <s:hidden name="evalTable" />
                    <s:hidden name="productName" />
                                 
                    <input type="hidden" name="template_version_id" id="template_version_id" value="<%=objSCE.getTemplateVersionId()%>">
                    <%if (createMode) {%>  
                    <%-- <img
							src="<%=request.getContextPath()%>/evaluation/resources/_img/button_submit.gif"
							style="width: 55px; height: 19px"
							onClick="return validateSubmit()" alt="Submit"> 2020 Q3:commented by muzees to avoid duplicate evaluation--%> 
					<input type="image" id="submiteval" alt="Submit" onClick="return disableSubmit()" src="<%=request.getContextPath()%>/evaluation/resources/_img/button_submit.gif" style="width: 55px; height: 19px"> <!-- 2020 Q3:added by muzees to avoid duplicate evaluation- -->                                                         
        			<%if ("evaluate".equalsIgnoreCase(from)) {%>                    
                    <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_cancel.gif" style="width:55px; height:19px" onClick="return cancel()" alt="Cancel">
                    <%} else if ("search".equalsIgnoreCase(from)) {%>
                    <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_cancel.gif" style="width:55px; height:19px" onClick="return backToEvaluationResults()" alt="Cancel">
                    <%} else {%>                    
                    <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_cancel.gif" style="width:55px; height:19px" onClick="return backToScePendingReport()" alt="Cancel">                    
                    <%}%>
                    <%}%>
                </div>
                </div>
			
                <div class="clear"></div>	
            </div> <!-- end #content -->
            </s:form>
        </div><!-- end #wrap -->


    </body>
</html>
<%
    
	request.setAttribute("attendeeFirstName",evalAttendee.getFirstName());
    request.setAttribute("attendeeLastName",evalAttendee.getLastName());
    request.setAttribute("attendeeName",evalAttendee.getName());
    
    request.removeAttribute("sceToDisplay");
    request.removeAttribute("from");
    request.removeAttribute("isSubmitted");
    request.removeAttribute("attendees");

%>