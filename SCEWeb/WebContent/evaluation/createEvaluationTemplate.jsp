<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.beans.Descriptor"%>
<%@ page import="com.pfizer.sce.beans.EvaluationFormScore"%>
<%@ page import="com.pfizer.sce.beans.LegalQuestion"%>
<%@ page import="com.pfizer.sce.beans.Question"%>
<%@ page import="com.pfizer.sce.beans.BusinessRule"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.*"%>
<%@ page import="com.pfizer.sce.beans.ScoringSystem"%>
<%@ page import="com.pfizer.sce.beans.TemplateVersion"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp"%>
<%
    TemplateVersion templateVersion = (TemplateVersion)request.getAttribute("templateVersion");
    List questions = null;
    List businessRules = null;
    List legalQuestions = null;
    List evaluationFormScores = null;
    String publishFlag = null;
    //added by saikat
    String scorecommentFlag = null;
    String templateTitle="Sales Call Evaluation";
    
    boolean callimage=templateVersion.isCallImageDisplay();
    
    if(templateVersion.getTemplatetitle() != null && (!templateVersion.getTemplatetitle().equals("")))
    {
    	templateTitle = templateVersion.getTemplatetitle();  
    }
    
    String legalSectionFlag = null;
    if(templateVersion != null)
    {
         questions = (ArrayList) templateVersion.getQuestions();
         businessRules = (ArrayList) templateVersion.getBusinessRules();
         legalQuestions = (ArrayList) templateVersion.getLegalQuestions();
         evaluationFormScores = (ArrayList) templateVersion.getEvaluationFormScores();
         publishFlag = templateVersion.getPublishFlag();
         legalSectionFlag = templateVersion.getLegalSectionFlag();
         //added by saikat
        // scorecommentFlag = templateVersion.getScorecommentFlag();
    }
    
    Map  scoringSystemMap = (HashMap)session.getAttribute("scoringSystemMap");
    List scoringSystemValues = null;
    if(session.getAttribute("scoringSystemValues") != null )
    {
       scoringSystemValues = (ArrayList) session.getAttribute("scoringSystemValues");
    }
    String   pageName = (String)request.getAttribute("pageName");
    String selectedScoringSystem = null;
    // System.out.println("*************templateVersion*****************"+templateVersion);
    String selectedAutoCredit = templateVersion.getAutoCreditValue();
    // System.out.println("*************selectedAutoCredit*****************"+selectedAutoCredit);
    if(request.getAttribute("selectedScoringSystem") != null){
    selectedScoringSystem = request.getAttribute("selectedScoringSystem").toString();
    // System.out.println("the value of the scoring system is"+selectedScoringSystem);
    }
    
    String questionsCount = null;
    String businessRulesCount = null;
    String legalQuestionsCount = null;
    String templateVersionId = null;
    int numOfBusinessRulesRemoved = 0;
    int numOfQuestionsRemoved = 0;
    int numOfLegalQuestionsRemoved = 0;
    
    int  numOfCriticalQuestions  = 0;
    int numOfScoringOptions = 0;
    if(request.getAttribute("questionsCount") != null)
    {
        questionsCount = request.getAttribute("questionsCount").toString();
    }
    if(request.getAttribute("businessRulesCount") != null){
        businessRulesCount = request.getAttribute("businessRulesCount").toString();
    }
    if(request.getAttribute("legalQuestionsCount") != null){
        legalQuestionsCount = request.getAttribute("legalQuestionsCount").toString();
    }
    if(request.getAttribute("numOfCriticalQuestions") != null){
        numOfCriticalQuestions = Integer.parseInt(request.getAttribute("numOfCriticalQuestions").toString());
    }
    if(request.getAttribute("numOfScoringOptions") != null){
        numOfScoringOptions = Integer.parseInt(request.getAttribute("numOfScoringOptions").toString());
    }
    if(request.getAttribute("numOfQuestionsRemoved") != null){
        numOfQuestionsRemoved = Integer.parseInt(request.getAttribute("numOfQuestionsRemoved").toString());
    }
    if(request.getAttribute("numOfBusinessRulesRemoved") != null){
        numOfBusinessRulesRemoved = Integer.parseInt(request.getAttribute("numOfBusinessRulesRemoved").toString());
    }
    if(request.getAttribute("numOfLegalQuestionsRemoved") != null){
        numOfLegalQuestionsRemoved = Integer.parseInt(request.getAttribute("numOfLegalQuestionsRemoved").toString());
    }
    
    
    

%>
<html>
<head>
<title>Create/Edit Evaluation Template</title>





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



<script type="text/javascript">
        
        
         function setScoreValues(obj)    
        { 
        
        	//alert("inside the set score values1");
       
      /*     var scoringSystemIdentifier = obj.value;
        alert("the value of the scoringSystemIdentifier "+scoringSystemIdentifier);
      selectedScoringSystem.value = scoringSystemIdentifier;
       alert("the value of selectedScoringSystem"+selectedScoringSystem.value);
       document.forms[0].action="selectScoringSystem";
       document.forms[0].submit();  */  
       
       var templettitle = document.getElementById("templettitle").value;
       if(templettitle!=null && templettitle!="")
       {document.getElementById("templettitle_hidden").value=templettitle;}

       document.getElementById("callImageDisplay_hidden").value=document.getElementById("callImageDisplay").checked;
       
        
       var frm = document.forms[0];
       var scoringSystemIdentifier = frm.scoringSystem.options[obj.selectedIndex].value;
       frm.selectedScoringSystem.value = scoringSystemIdentifier;
       frm.action = "selectScoringSystem";
       frm.submit();
        
        }
        
        function addQuestion()
    {  
    
        	//alert("inside the add question1");
        	 var ni = document.getElementById('sce_div');
        var numi = document.getElementById('q_value');
        var num = (document.getElementById('q_value').value - 1) + 2;
        numi.value = num; 
       // alert("num before adding removed questions:"+num);
        var numOfQuestionsRemoved = document.getElementById('numOfQuestionsRemoved').value;
        num = parseInt(num) + parseInt(numOfQuestionsRemoved);//alert("num after adding removed questions:"+num);
        var divIdName = "q_" + num;
       // alert("divIdName:"+divIdName);
        var newdiv = document.createElement('div');
        newdiv.setAttribute("id",divIdName);
        newdiv.innerHTML = getQuestionHtml(num);
       // alert("newdiv.innerHTML:"+newdiv.innerHTML);
        ni.appendChild(newdiv);
        var questionsCount = num+1;
        document.getElementById('questionsCount').value = questionsCount;
        return false;
    }

    function removeElement(divNum)
    {
    	//alert("inside the remove element1");
        var d = document.getElementById('myDiv');
        var olddiv = document.getElementById(divNum);
        d.removeChild(olddiv);
    }
    
    function getQuestionHtml(num) {
    	//alert("inside the get question html");
        var html = '';
        //var numOfQuestionsRemoved = document.getElementById('numOfQuestionsRemoved').value;
        //num = parseInt(num) + parseInt(numOfQuestionsRemoved);
        /* Display Order */
        html += '<div>' + '\n';
        html += '<label>Question number:<font color="red">*</font></label>' + '\n';
        html += '<input class="question_number" name="displayOrder_' + num + '" id="displayOrder_' + num + '" type="text"  value="" />' + '\n';
        html += '</div>' + '\n';
        
        /* Question Title */
        html += '<div>' + '\n';
        html += '<label>Title:<font color="red">*</font></label>' + '\n';
        html += '<input class="q_title" name="title_' + num + '" id="title_' + num + '" type="text" maxlength="256" value="" />' + '\n';
        /* Critical */
        html += '<label class="critical" style="float: right;">Critical</label>';
        html += '<input class="critical" style="float: right;" name="critical_' + num + '" id="critical_' + num + '" type="checkbox" style="overflow:hidden;width:14px;padding:0;margin:0;height:13;" onclick="setCritical(this)"/>';
        html += '</div>' + '\n';
        
        /* Question Description */
        html += '<div>' + '\n';
        html += '<label>Question:<font color="red">*</font></label>' + '\n';
        html += '<input class="question" name="description_' + num + '" id="description_' + num + '" type="text" maxlength="4000" value="" />' + '\n';
        html += '</div>' + '\n';
        
        /* Descriptor Counter */
        html += '<input type="hidden" value="0" id="d_value_' + num + '" name="d_value_' + num + '" />';
        
        /* Question Descriptor */
        /*html += '<div><div>' + '\n';
        html += '<label>Question descriptor:</label>' + '\n';
        html += '<input class="descriptor" name="descriptor_' + num + '_0" type="text" maxlength="4000" value="" />' + '\n';
        html += '</div></div>' + '\n';*/
        html += '<div id="q_d_' + num + '"' + '\n';
        html += '<div>' + '\n';
        html += getDescriptorHtml(num, 0);
        html += '</div>' + '\n';
        html += '</div>' + '\n';
        
        /* Critical */
        /*html += '<div>';			
        html += '<label style="float: right;">Critical:</label>';
        html += '<input class="critical"  style="float: right;" name="critical_' + num + '" id="critical_' + num + '" type="checkbox" />';
        html += '</div>';*/
        
        /* Buttons */
        html += '<div class="add_buttons">' + '\n';
        html += '<img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_add_descriptor.gif" alt="Add Descriptor" width="83" height="19" onclick="addDescriptor(' + num + ')"/>' + '\n';
        html += '<img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_add_question.gif" alt="Add Question" width="81" height="19" onclick="addQuestion()"/>' + '\n';
        html += '<img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_preview_white.gif" alt="Preview" width="50" height="19" onclick="openPreviewWindow()"/>' + '\n';
        html += '<img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_delete_white.gif" alt="Delete Question" width="42" height="19" onclick="deleteQuestion(' + num + ')"/>' + '\n';
        html += '</div>' + '\n';
        
        
        return html;
    }
    
    function addDescriptor(qDivNum)
    {  // alert("inside the add descriptor");
        var ni = document.getElementById('q_d_' + qDivNum);
        var numi = document.getElementById('d_value_' + qDivNum); 
        var num = (document.getElementById('d_value_' + qDivNum).value - 1) + 2;
        numi.value = num;
        var newdiv = document.createElement('div'); //alert("qDivNum,num:"+qDivNum+","+num);
        newdiv.innerHTML = getDescriptorHtml(qDivNum,num);
        ni.appendChild(newdiv);       
        return false;
    }

    function getDescriptorHtml(qDivNum, num) {
    	//alert("inside the get descriptor html");
        var html = '';
        var descriptorNum = num+1; 
        /* Question Descriptor */
        html += '<label>Descriptor'+ descriptorNum +':</label>' + '\n';
        html += '<input class="descriptor" name="descriptor_' + qDivNum + '_' + num + '" id="descriptor_' + qDivNum + '_' + num + '" type="text" value="" />' + '\n';
        return html;
    }
    
    
    function deleteQuestion(divNum)
        {   //alert("deleteQuestion divNum:"+divNum);
            var questionsCount = document.getElementById('q_value').value;
            if(document.getElementById('q_value').value == 0){
                alert("At least one question is required in the Question section.");
                return false;
            }
            var d = document.getElementById('sce_div');
            var olddiv = document.getElementById("q_" + divNum); //alert("q_" + divNum); //alert("olddiv:"+olddiv);
            var numOfCriticalQuestions = document.getElementById('numOfCriticalQuestions').value;
            var removeOpt = 0;
            var criticalObjName = "critical_"+divNum;
            var obj = document.getElementById(criticalObjName);
            if(obj.checked == true){ 
               document.getElementById('numOfCriticalQuestions').value = --numOfCriticalQuestions;
               removeOpt = 1;
            }
            adjustBusinessRules(numOfCriticalQuestions,removeOpt);
            d.removeChild(olddiv);
            document.getElementById('q_value').value = --questionsCount;
            //var numOfQuestionsRemoved = document.getElementById('numOfBusinessRulesRemoved').value;
            var numOfQuestionsRemoved = document.getElementById('numOfQuestionsRemoved').value;
            document.getElementById('numOfQuestionsRemoved').value = ++numOfQuestionsRemoved;
            return false;
        }
    
    function setCritical(obj){
    	//alert("inside the set crticial");
           var numOfCriticalQuestions = document.getElementById('numOfCriticalQuestions').value;
           var removeOpt = 0;
           if(obj.checked == true){
               document.getElementById('numOfCriticalQuestions').value = ++numOfCriticalQuestions;
               obj.value = "Y";
            }else{
               document.getElementById('numOfCriticalQuestions').value = --numOfCriticalQuestions;
               removeOpt = 1;
               obj.value = "N";
            }
            adjustBusinessRules(numOfCriticalQuestions,removeOpt);
       return false;
    }
    
    function adjustBusinessRules(numOfCriticalQuestions,removeOpt)
     {
    //	alert("inside the adjust business rule");
        if(numOfCriticalQuestions > 0)
            { 
                 var businessRulesCount = document.getElementById('b_value').value;
                 if(businessRulesCount == -1 ){
                    addBusinessRule();
                 }
                 businessRulesCount = document.getElementById('b_value').value;
                 var numOfBusinessRulesRemoved = document.getElementById('numOfBusinessRulesRemoved').value;
                 var totalNumOfBusinessRules = parseInt(businessRulesCount)+parseInt(numOfBusinessRulesRemoved);
                
                for(count=0;count<=totalNumOfBusinessRules;count++){ 
                     
                     var businessRuleObjName = "businessRule"+count+"_numOfCriticalQuestions";
                     if(removeOpt == 1){ //alert("remove"+businessRuleObjName);
                        if(document.getElementById(businessRuleObjName) != null){
                            document.getElementById(businessRuleObjName).remove(numOfCriticalQuestions);
                        }
                        //if(selectedIndex == numOfCriticalQuestions){alert("Inside selectedIndex:"+selectedIndex);
                            //document.getElementById(businessRuleObjName).options['0'].selected = true;
                        //}
                     }else{
                         for(cq=0;cq<numOfCriticalQuestions;cq++){ 
                            val = cq+1;
                            if(document.getElementById(businessRuleObjName) != null){
                                var selectedIndex = document.getElementById(businessRuleObjName).selectedIndex;
                                //myselect.options[myselect.length]=new Option("Brand new option", "what")
                                //Ref http://www.javascriptkit.com/jsref/select.shtml
                                document.getElementById(businessRuleObjName).options[cq] = new Option(val, val); 
                                document.getElementById(businessRuleObjName).options[selectedIndex].selected = true;
                             }
                         }
                     
                     }
                }
            }
            if(numOfCriticalQuestions == 0)
            {
             deleteAllBusinessRules();
            }
            
     }
      
        function deleteAllBusinessRules()
        {
        	//alert("inside the delete all business rule");
        var cell = document.getElementById("business_div");

            if ( cell.hasChildNodes() )
            {
                while ( cell.childNodes.length >= 1 )
                {
                    cell.removeChild( cell.firstChild );       
                } 
            }
            
            if(document.getElementById('business_add_div1') != null){
            document.getElementById('business_add_div1').removeNode(true);
            }
            document.getElementById('b_value').value = -1;
            if(document.getElementById('business_conflict_div1') != null){
            document.getElementById('business_conflict_div1').removeNode(true);
            }
            document.getElementById('numOfBusinessRulesRemoved').value = 0;
            
        }
        
        function unCheckAllCritical(){
        	//alert("inside the uncheck all critical");
         var num = document.getElementById('q_value').value;
         var numOfQuestionsRemoved = document.getElementById('numOfQuestionsRemoved').value;
         num = parseInt(num) + parseInt(numOfQuestionsRemoved);
         for(i=0;i<=num;i++){ //alert("critical_:"+document.getElementById('critical_'+i));
             if(document.getElementById('critical_'+i) != null){//alert("Inside");
                document.getElementById('critical_'+i).checked = false;
             }
         }
         document.getElementById('numOfCriticalQuestions').value = 0;
        }
        
        function deleteBusinessRule(divNum)
        {   
        	//alert("inside the delete business rule");
            var businessRulesCount = document.getElementById('b_value').value;
            var d = document.getElementById('business_div');//alert(d);
            var olddiv = document.getElementById("b_" + divNum);
            d.removeChild(olddiv);
            document.getElementById('b_value').value = --businessRulesCount;
            if(document.getElementById('b_value').value < 0){
                if(document.getElementById('business_add_div1') != null){
                document.getElementById('business_add_div1').removeNode(true);
                }
                unCheckAllCritical();
            }
            if(document.getElementById('b_value').value < 1){ //alert("business_add_div1:"+document.getElementById('business_add_div1'));
                if(document.getElementById('business_add_div1') != null){//alert("business_add_div1 not null");
                    document.getElementById('business_conflict_div1').removeNode(true);
                }
                
            }
            var numOfBusinessRulesRemoved = document.getElementById('numOfBusinessRulesRemoved').value;
            document.getElementById('numOfBusinessRulesRemoved').value = ++numOfBusinessRulesRemoved;
        }
        
        function addBusinessRule()
        {
        	//alert("inside the add business rule");
        var ni = document.getElementById('business_div');
        var numi = document.getElementById('b_value');
        var num = (document.getElementById('b_value').value - 1) + 2;
        numi.value = num;
        
        var numOfBusinessRulesRemoved = document.getElementById('numOfBusinessRulesRemoved').value;
        //num = parseInt(num) + parseInt(numOfBusinessRulesRemoved);
        var divIdNumber = parseInt(num) + parseInt(numOfBusinessRulesRemoved);
        var divIdName = "b_" + divIdNumber;
        
        //var divIdName = "b_" + num;
        var newdiv = document.createElement('div');//alert("divIdName:"+divIdName);
        newdiv.setAttribute("id",divIdName);
        newdiv.innerHTML = getBusinessRuleHtml(num); //alert(newdiv);
        ni.appendChild(newdiv);
        
        if(num == 0){
          var nia = document.getElementById('business_add_div'); 
          var newdiv = document.createElement('div');
          newdiv.setAttribute("id","business_add_div1");
          newdiv.innerHTML = getBusinessRuleButtonHtml();//alert("newdiv:"+newdiv);
          nia.appendChild(newdiv);
        }
          
        if(num == 1){
           var nic = document.getElementById('business_conflict_div');
           var newdiv = document.createElement('div');
           newdiv.setAttribute("id","business_conflict_div1");
           newdiv.innerHTML = getBusinessRuleConflictHtml();
           nic.appendChild(newdiv);
         }
        
        var businessRulesCount = num;
        document.getElementById('b_value').value = businessRulesCount;
        return false;
        } 
        
        
        function getBusinessRuleConflictHtml() {
        	//alert("inside the getBusinessRuleConflictHtml");
        
        var html = '';
        
        html += '<div> <label></label><h4>Conflict Overwrite:</h4><label></label>' + '\n';
        html += 'In case of a conflict between two or more business rules, the overall evaluated score shall be ' + '\n';
        html += '<select name="conflictOverallScore" style="width: 90px" onChange="setValue(this)">' + '\n';
        
        <% 
         if(scoringSystemValues != null ){
        
        Iterator sitr = scoringSystemValues.iterator();
        String value = null;
              while(sitr.hasNext()){
               value = (String)sitr.next();
                %>
        
        html += '<option value="<%=value%>"><%=value%></option>' + '\n';
        <% }
        
          }%>
        
        
        html += '</select>' + '\n';
        html += '</div>' + '\n';
        return html;
        }
        
        function getBusinessRuleButtonHtml() {
        	//alert("inside the getBusinessRuleButtonHtml");
        
        var html = '';
        
        html += '<div>' + '\n';
        html += '<img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_add_rule.gif" alt="Add Rule" width="55" height="19" onclick="addBusinessRule()"/>' + '\n';
        html += '</div>' + '\n';
        return html;
        }
        
        function getBusinessRuleHtml(num) {
        	//alert("inside the getBusinessRuleHtml");	
        var numOfBusinessRulesRemoved = document.getElementById('numOfBusinessRulesRemoved').value;
        num = parseInt(num) + parseInt(numOfBusinessRulesRemoved);
        var numOfCriticalQuestions = document.getElementById('numOfCriticalQuestions').value;
        var html = '';
        
        /* Display Order */
        html += '<div>' + '\n';
        if(num == 0){
            html += '<hr>' + '\n';
        }
        html += '<label>Business Rule:</label>' + '\n';
        html += '<input class="question_number" name="businessRule'+num+'_displayOrder" id="businessRule'+num+'_displayOrder" type="hidden"  value="" />' + '\n';
        //html += '</div>' + '\n';
        
        /* NumOfCriticalQuestions  */
        //html += '<div>' + '\n';
        //html += '<label></label>' + '\n';
        html += '<select name="businessRule'+num+'_numOfCriticalQuestions" style="width: 35px" onChange="setValue(this)">' + '\n';
        
        
        for(count=1;count<=numOfCriticalQuestions;count++){ 
         html += '<option value="'+count+'">'+count+ '\n';
         }
        
        
        html += '</option></select>' + '\n';
        html += 'or more Critical questions with a score of ' + '\n';
        html += '<select name="businessRule'+num+'_score" style="width: 90px" onChange="setValue(this)">' + '\n';
        
        <% 
         if(scoringSystemValues != null ){
        
        Iterator sitr = scoringSystemValues.iterator();
        String value = null;
              while(sitr.hasNext()){
               value = (String)sitr.next();
                %>
        
        html += '<option value="<%=value%>"><%=value%></option>' + '\n';
        <% }
        
          }%>
        
        html += '</select> equals ' + '\n';
        //html += '</div>' + '\n';
        
        
        //html += '<div>' + '\n';
        //html += '<label></label>' + '\n';
        html += '<select name="businessRule'+num+'_overallScore" style="width: 90px" onChange="setValue(this)">' + '\n';
        
         <% 
         if(scoringSystemValues != null ){
        
        Iterator sitr = scoringSystemValues.iterator();
        String value = null;
              while(sitr.hasNext()){
               value = (String)sitr.next();
                %>
        
        html += '<option value="<%=value%>"><%=value%></option>' + '\n';
        <% }
        
          }%>
        
        
        html += '</select> overall <img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_delete_rule.gif" style="padding-bottom:0cm;" alt="Delete Rule" width="83" height="19" onclick="deleteBusinessRule('+num+')"/>' + '\n';
        html += '</div>' + '\n';
       return html;
    }
        
        
        
        function deleteLegalQuestion(divNum)
        {   
        	//alert("inside the deleteLegalQuestion");
            var deleteQuestionsCount = document.getElementById('l_value').value;
            if(document.getElementById('l_value').value == 0){
                alert("You have selected Legal Section to be applied for this form, please provide at least one question in the Legal section.");
                return false;
            }
            
            
            var d = document.getElementById('legal_div');
            var olddiv = document.getElementById("l_" + divNum);
            //alert("about to delete the child");
            d.removeChild(olddiv);
           // alert("deleted the child");
            document.getElementById('l_value').value = --deleteQuestionsCount;
            var numOfLegalQuestionsRemoved = document.getElementById('numOfLegalQuestionsRemoved').value;
            document.getElementById('numOfLegalQuestionsRemoved').value = ++numOfLegalQuestionsRemoved;
            return false;
            
        }
        
        
        
         function addLegalQuestion(){
        	// alert("inside the addLegalQuestion");
        var ni = document.getElementById('legal_div');
        var numi = document.getElementById('l_value');
        var num = (document.getElementById('l_value').value - 1) + 2;
        numi.value = num;
        
        var numOfLegalQuestionsRemoved = document.getElementById('numOfLegalQuestionsRemoved').value;
        var divIdNumber = parseInt(num) + parseInt(numOfLegalQuestionsRemoved);
        var divIdName = "l_" + divIdNumber;
        
        //var divIdName = "l_" + num;
        var newdiv = document.createElement('div');
        newdiv.setAttribute("id",divIdName);
        newdiv.innerHTML = getLegalQuestionHtml(num);
        ni.appendChild(newdiv);
        
        if(num == 0){
          var nia = document.getElementById('legal_add_div');
          var newdiv = document.createElement('div');
          newdiv.setAttribute("id","legal_add_div1");
          newdiv.innerHTML = getLegalQuestionAddButton();
          nia.appendChild(newdiv);
        }
          
        var legalQuestionsCount = num;
        
        document.getElementById('l_value').value = legalQuestionsCount;
        return false;
       }
        
        function getLegalQuestionHtml(num)
        {
       	 //alert("inside the getLegalQuestionHtml");
        var html = '';
        var numOfLegalQuestionsRemoved = document.getElementById('numOfLegalQuestionsRemoved').value;
        num = parseInt(num) + parseInt(numOfLegalQuestionsRemoved);

        /* LegalQuestionHtml */
        html += '<div>' + '\n';
        html += '<br /><label>Legal Question:<font color="red">*</font></label>' + '\n';
        html += '<input class="question_number" name="legal' + num + '_displayOrder" id="legal' + num + '_displayOrder" type="text" value="" />' + '\n';
        html += '<label style="width:25px;"></label>' + '\n';
        html += '<input  name="legal' + num + '_question_label" id="legal' + num + '_question_label" type="text"  maxlength="100" style="width:180px;" value="Enter Label for Legal Question here"/>' + '\n';
        html += '<label style="width:25px;"></label>' + '\n';
        html += '<textarea rows="2" cols="25" name="legal' + num + '_question_text" id="legal' + num + '_question_text">Enter the Legal Question here</textarea>' + '\n';
        html += '<label style="width:25px;"></label>' + '\n';
        html += '<img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_delete_white.gif" alt="Delete Legal Question" width="42" height="19" onclick="deleteLegalQuestion('+num+')"/>' + '\n';
        html += '</div>' + '\n';
        return html;
    }
        
        function getLegalQuestionAddButton(){
        	 //alert("inside the getLegalQuestionAddButton");
        var html = '';
        
        html += '<div>' + '\n';
        html += '<img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_add.gif" alt="Add Legal question" width="55" height="19" align ="top" style="padding-top: 0pt;" onclick="addLegalQuestion()"/>' + '\n';
        html += '</div>' + '\n';
        return html;
       
    }
   
        
    function setValue(obj){
    	 //alert("inside the setValue");
        obj.value = obj.options[obj.selectedIndex].value;
        return false;
    }    
        
    function setNotificationFG(obj){   
    	 //alert("inside the setNotificationFG");
        if(obj.checked == true){
            obj.value = "Y";
        }else{ 
            obj.value = "N";
        }
        return false;
    } 
    
    function setcommentFG(scoreOptionsCount,obj){
      // added by saikat
       //alert("inside the setcommentFG");
    selectedElementName1 = "selected_evaluationFormScore"+scoreOptionsCount+"_commentFlag";
      //alert("selectedElementName1:"+selectedElementName1);
     
      if(obj.checked == true){
         document.getElementById(selectedElementName1).value = "Y";
        // alert(document.getElementById(selectedElementName1).value);
      }else{ 
            document.getElementById(selectedElementName1).value = "N";
           // alert('inside n '+document.getElementById(selectedElementName1).value);
       }
       
       return false;
     
    }   
        
   function setLMSFlag(scoreOptionsCount,val){
	   //alert("inside the setLMSFlag");
       selectedElementName = "selected_evaluationFormScore"+scoreOptionsCount+"_lmsFlag";
       //alert("selectedElementName:"+selectedElementName);
       document.getElementById(selectedElementName).value = val;
       return false;
    }      
    
    function setHLCFlag(obj){
    	 //alert("inside the setHLCFlag");
        var hlcDiv = document.getElementById("show_HLC_value"); 
        if(obj.checked == true){
               obj.value = "Y";
               document.getElementById("hlcCriticalFlag").value= "Y";
               hlcDiv.style.display = 'block';
            }else{
               obj.value = "N";
               document.getElementById("hlcCriticalFlag").value= "N";
               hlcDiv.style.display = 'none';
            }
            return false;
    }    
    
    
        
       function validateLegalSection(legalSection)    
        {   
    	   //alert("inside the validateLegalSection");
    	   var sectionElement = document.getElementById("show_legal_section"); 
            if(legalSection.checked == true){
               sectionElement.style.display = 'block';
               if(document.getElementById("hlcCriticalFlag").value== "Y")
               {
               document.getElementById("hlcCritical").checked = true;
               document.getElementById("hlcCritical").value= "Y";
               document.getElementById("hlcCriticalFlag").value= "Y";
               }
               document.getElementById("legalSectionFlag").value = "Y";
            }else{
               sectionElement.style.display = 'none';
               document.getElementById("legalSectionFlag").value = "N";
            }
            return false;
        } 
        
        
        function setDisplay(obj,divName,callFlag)    
        {   //alert("divName:"+divName);
            var sectionElement = document.getElementById(divName); 
            //alert("sectionElement:"+sectionElement);
            if(obj.checked == true){
                sectionElement.style.display = 'block'; 
                document.getElementById(callFlag).value = "Y";
            }else{
               sectionElement.style.display = 'none';
               document.getElementById(callFlag).value = "N";
            }
            return false;
        } 
        
        
        
        
        
 
        function cancel() {
        	//alert("inside the cancel");
            var frm = document.forms[0];
            var pageName = frm.pageName.value;
            if(pageName != null && pageName == "create"){
                var r=confirm("Do you want to cancel the changes to Evaluation Template?");
            }else if(pageName != null && pageName == "edit"){
                var r=confirm("Any changes to the Evaluation Template will be lost. Do you want to continue?");
            }
            if (r==true)
              { 
                frm.action = "cancel";
                frm.submit();
              }
            else
              {
                 return false;
              }
         }
         
         
function trim(str) {
	//alert("inside the trim");//alert("//"+str+"//");
        return str.replace(/^\s+|\s+$/g,"");
}

        
        function save() { 
        	//alert("inside the save");
               var frm = document.forms[0]; 
               var pageName = frm.pageName.value;
               
               var templettitle = document.getElementById("templettitle").value;
               
               //templatetitle_button
               
               if(document.getElementById('overallEvaluationLable').value == null || document.getElementById('overallEvaluationLable').value == "")
    			{
    	   			alert("Please enter Overall Evaluation Lable."); 
    	  			 return;
    			}
               
               if(document.getElementById("templatetitle_button").value.toLowerCase()=="save")
            	   {
            	   alert("Please enter template title.");
            	   return;
            	   }
               
               if(templettitle == null||templettitle == "")
            	   {
            	   alert("Please enter template title.");
            	   return;
            	   }
               
               document.getElementById("templettitle_hidden").value=templettitle;

               document.getElementById("callImageDisplay_hidden").value=document.getElementById("callImageDisplay").checked;
            	
               
               
               //alert(templettitle.trim());
                var formTitle = frm.form_title;
                var scoringSystem = frm.scoringSystem;
                var numOfQuestions = frm.q_value.value;
                var numOfBusinessRules = frm.b_value.value;
                var legalSectionFlag = frm.legalSectionFlag.value;
                var scoringOptionsCount = frm.numOfScoringOptions.value;
                var numOfBusinessRulesRemoved = frm.numOfBusinessRulesRemoved.value;
                var numOfQuestionsRemoved = frm.numOfQuestionsRemoved.value;
                var numOfLegalQuestionsRemoved = frm.numOfLegalQuestionsRemoved.value;
                formTitleVal  = trim(formTitle.value);
                if(formTitleVal == "")
                {
                    formTitle.value = "";
                    alert("Please Enter Evaluation Form Title."); 
                    formTitle.focus();       
                    return false;
                }
                else if(scoringSystem.value == "")
                {
                    alert("Please Select A Scoring System."); 
                    scoringSystem.focus();       
                    return false;
                }
                else if(!validatePreCallSection())
                {
                	   return false;	
                }
                else if(!validatePostCallSection())
                {
                    return false;
                }
                else if(document.getElementById("callSectionLabelDisplay").checked && document.getElementById("callSectionLabel").value == "")
                {
                	alert('Please enter Call section label');
                	return false;
                }
                else if(numOfQuestions != -1)
                {
                            var totalNumOfQuestions = parseInt(numOfQuestions)+parseInt(numOfQuestionsRemoved);
                            for(count=0;count<=totalNumOfQuestions;count++){ 
                                var questionNumObjName = "displayOrder_"+count; 
                                var questionTitleObjName = "title_"+count; 
                                var questionObjName = "description_"+count;
                                var questionNumObj = document.getElementById(questionNumObjName);
                                var questionTitleObj = document.getElementById(questionTitleObjName);
                                var questionObj = document.getElementById(questionObjName);
                                var criticalObjName = "critical_"+count;
                                var criticalObj = document.getElementById(criticalObjName);
                                if(questionNumObj != null && questionObj != null && criticalObj != null){
                                    if(questionNumObj.value == null || trim(questionNumObj.value) == ""){
                                        alert("Please Enter a Question Number."); 
                                        questionNumObj.value = "";
                                        questionNumObj.focus();
                                        return false;
                                    }else if(!isNumeric(questionNumObj.value)){
                                      alert("Please Enter numeric value for a Question Number.");
                                      questionNumObj.focus();
                                      return false;
                                    }else if(questionTitleObj.value == null || trim(questionTitleObj.value) == ""){
                                      alert("Please Enter A Question Title.");
                                      questionTitleObj.value = "";
                                      questionTitleObj.focus();
                                      return false;
                                    }else if(questionObj.value == null || trim(questionObj.value) == ""){
                                      alert("Please Enter the Question.");
                                      questionObj.value = "";
                                      questionObj.focus();
                                      return false;
                                    }
                               
                                    if(criticalObj.checked == true){
                                        criticalObj.value = "Y";
                                    }else{
                                        criticalObj.value = "N";
                                    }
                                   
                                }
                            }
                            
                            if(isValidDisplayOrder("q",numOfQuestions,totalNumOfQuestions) == false){
                                return false;
                            }
                            
                            if(numOfBusinessRules != -1){
                                var totalNumOfBusinessRules = parseInt(numOfBusinessRules)+parseInt(numOfBusinessRulesRemoved);
                                var actualCount = -1;
                                var conflictFlag = 'N';
                                for(count=0;count<=totalNumOfBusinessRules;count++){ 
                                   
                                    var numOfCriticalQuestionsObjName = "businessRule"+count+"_numOfCriticalQuestions";
                                    var numOfCriticalQuestionsObj = document.getElementById(numOfCriticalQuestionsObjName);
                                    if(numOfCriticalQuestionsObj != null){
                                        actualCount++;
                                        var numOfCriticalQuestionsObjValue = numOfCriticalQuestionsObj.options[numOfCriticalQuestionsObj.selectedIndex].value;
                                        numOfCriticalQuestionsObj.value = numOfCriticalQuestionsObjValue;
                                    }
                                    var scoreObjName = "businessRule"+count+"_score";
                                    var scoreObj = document.getElementById(scoreObjName);
                                    if(scoreObj != null){
                                        var scoreObjValue = scoreObj.options[scoreObj.selectedIndex].value;
                                        scoreObj.value = scoreObjValue;
                                    }
                                    var overallScoreObjName = "businessRule"+count+"_overallScore";
                                    var overallScoreObj = document.getElementById(overallScoreObjName);
                                    if(overallScoreObj != null){
                                        var overallScoreObjvalue = overallScoreObj.options[overallScoreObj.selectedIndex].value;
                                        overallScoreObj.value = overallScoreObjvalue;
                                    }
                                    if(actualCount == 1 && conflictFlag == 'N'){ //alert("inside Conflict actualCount:"+actualCount);
                                        var conflictOverallScoreObj = document.getElementById("conflictOverallScore");
                                        var conflictOverallScoreObjValue = conflictOverallScoreObj.options[conflictOverallScoreObj.selectedIndex].value;
                                        conflictOverallScoreObj.value = conflictOverallScoreObjValue;
                                        conflictFlag = 'Y';
                                    }
                                    
                                    
                                    
                                }
                                
                               
                            }
                }
                
                if(!validatePostCallSection())
                {
                    return false;
                }
                var publishFlagObj = document.getElementById("publishFlag");
                var publishFlag = "N";
                    if(publishFlagObj != null){
                        publishFlag = publishFlagObj.value;
                    }
                var scoringSectionFlag = "N";
                
                for(count=0;count<=scoringOptionsCount;count++){ 
                    var scoringSectionObjName = "selected_evaluationFormScore"+count+"_lmsFlag";
                    var scoringSectionObj = document.getElementById(scoringSectionObjName); 
                    
                    //alert("scoringSectionObj.value:"+scoringSectionObj.value);
                    //alert("scoringSectionObj:"+scoringSectionObj);
                   
                    //if(scoringSectionObj == null ){alert("scoringSectionObj null:");
                   // }
                    if(scoringSectionObj != null && scoringSectionObj.value == "Y"){
                        scoringSectionFlag = "Y";
                    }//else{
                       // scoringSectionObj.value = "N";
                    //} 
                    if(scoringSectionObj != null && scoringSectionObj.value == ""){
                         scoringSectionObj.value = "N";
                    }
                    //alert("scoringSectionFlag:"+scoringSectionFlag);
                    
                    //Added by saikat.......................................
                    
                    
                   var scoringSectionObjName1 = "selected_evaluationFormScore"+count+"_commentFlag"
                   var scoringSectionObj1 = document.getElementById(scoringSectionObjName1);           
                    // alert(scoringSectionObjName1+scoringSectionObj1.value);    
                    
                    
                   
                    
                    if(scoringSectionObj1.value != 'Y' && (scoringSectionObj1 != null || scoringSectionObj1.value=='')){
                          //  alert("HI");
                              scoringSectionObj1.value = "N";
                          }
                    
                                    
                     
                            
                     
                    
                      
                   //if(scoringSectionObj1.value== "")
                         //  {
                           //    scoringSectionObj1.value = "N";
                          //  }
                
                      
                   
                              
                    
                    
                    
                    
                    if(pageName != null && pageName == "edit" && publishFlag == "Y"){
                        var notificationFGObjName = "evaluationFormScore"+count+"_notificationFG";
                        var notificationFGObj = document.getElementById(notificationFGObjName);
                       //alert("notificationFGObj:"+notificationFGObj);
                       if(notificationFGObj != null){
                             notificationFGObj.value = "Y";
                            if(notificationFGObj.checked == true){
                                notificationFGObj.value = "Y";
                            }else{
                                notificationFGObj.value = "N";
                            }
                        }
                    }
                    
                }
                if(scoringOptionsCount != null && scoringOptionsCount > -1 && scoringSectionFlag == "N"){
                    var scoringSectionObjName = "selected_evaluationFormScore"+0+"_lmsFlag";
                    var scoringSectionObj = document.getElementById(scoringSectionObjName); 
                    alert("Please select at least one status for LMS communication."); 
                    //scoringSectionObj.focus(); //  does not accept focus
                    return false;
                }
                
               if(legalSectionFlag == "Y"){ 
                
                        var numOfLegalQuestions = frm.l_value.value;
                        var totalNumOfLegalQuestions = parseInt(numOfLegalQuestions)+parseInt(numOfLegalQuestionsRemoved);
                        if(numOfLegalQuestions != -1){
                            for(count=0;count<=totalNumOfLegalQuestions;count++){ 
                                        var legalQuestionObjName = "legal"+count+"_displayOrder";
                                        var legalQuestionObj = document.getElementById(legalQuestionObjName);
                                        var legalQuestionLabelName = "legal"+count+"_question_label";
                                        var legalQuestionLabelObj = document.getElementById(legalQuestionLabelName);
                                        var legalQuestionTextName = "legal"+count+"_question_text";
                                        var legalQuestionTextObj = document.getElementById(legalQuestionTextName);
                                        
                                        if(legalQuestionObj != null && legalQuestionLabelObj != null && legalQuestionTextObj != null){
                                            if(legalQuestionObj.value == null || trim(legalQuestionObj.value) == ""){
                                                alert("Please Enter a Legal Question Number."); 
                                                legalQuestionObj.value = "";
                                                legalQuestionObj.focus();
                                                return false;
                                            }else if(!isNumeric(legalQuestionObj.value)){
                                                alert("Please Enter numeric value for a Legal Question Number.");
                                                legalQuestionObj.focus();
                                                return false;
                                            }else  if(legalQuestionLabelObj.value == null || trim(legalQuestionLabelObj.value) == ""){
                                                alert("Please Enter a Legal Question Label."); 
                                                legalQuestionLabelObj.value = "";
                                                legalQuestionLabelObj.focus();
                                                return false;
                                            }else  if(legalQuestionTextObj.value == null || trim(legalQuestionTextObj.value) == ""){
                                                alert("Please Enter a Legal Question Text."); 
                                                legalQuestionTextObj.value = "";
                                                legalQuestionTextObj.focus();
                                                return false;
                                            }
                                        
                                        }
                                        
                              }
                             if(isValidDisplayOrder("l",numOfLegalQuestions,totalNumOfLegalQuestions) == false){
                                return false;  
                             }        
                             
                         }
                        
                        if(document.getElementById("hlcCritical").checked == true){
                             var hlcValue = frm.hlcValue.options[frm.hlcValue.selectedIndex].value;
                             frm.hlcValue.value = hlcValue;
                             frm.hlcCriticalFlag.value = "Y";
                        }else{
                             frm.hlcCriticalFlag.value = "N";
                        }
                             
                }else{
                    frm.hlcCriticalFlag.value = "Y";
                }
                
             var autoCreditValue = frm.autoCredit.options[frm.autoCredit.selectedIndex].value;
             frm.autoCredit.value = autoCreditValue;
             frm.submit();   
     }
        
            function isNumeric(str) {
            	
                if(str.match(/^\d+$/) != null){
                    return true;
                }else{
                    return false;
                }
            }

      function isValidDisplayOrder(name,num,totalNum){
    	  
                    lastNumber = parseInt(num)+1; 
                    for(number=1;number<=lastNumber;number++){ 
                        var flag = "N";
                       for(count=0;count<=totalNum;count++){ 
                            var obj;
                            if(name == "q"){
                                var questionNumObjName = "displayOrder_"+count;
                                obj =  document.getElementById(questionNumObjName);
                            }else  if(name == "b"){
                                var businessRuleObjName = "businessRule"+count+"_displayOrder";
                                obj = document.getElementById(businessRuleObjName);
                            }else  if(name == "l"){  //legal section
                                var legalQuestionObjName = "legal"+count+"_displayOrder";
                                obj = document.getElementById(legalQuestionObjName);
                            }
                            
                            if(obj != null && number == obj.value ){
                                flag = "Y";
                            }
                            
                        }
                        if(flag == "N"){
                            if(name == "q"){
                                alert("Please Enter Question Numbers in order."); 
                            }else  if(name == "b"){
                                alert("Please Enter BusinessRule Numbers in order."); 
                            }else  if(name == "l"){  //legal section
                                alert("Please Enter Legal Question Numbers in order."); 
                            }else  if(name == "pq"){  //preview Question section
                                alert("Could not show preview without having valid display order in question section."); 
                            }else  if(name == "pl"){  //preview Legal section
                                alert("Could not show preview without having valid display order in legal section."); 
                            }
                            return false;
                        }
                    }
      }



    function setAutoCreditValue(obj){
    	//alert("inside setAutoCreditValue");
        var frm = document.forms[0]; 
        var autoCreditValue = frm.autoCredit.options[obj.selectedIndex].value;
        frm.autoCredit.value = autoCreditValue;
        return false;
    }

    
   function openPreviewWindow() 
   { 
       var frm = document.forms[0]; 
       var selectedScoringSystem = frm.scoringSystem.options[frm.scoringSystem.selectedIndex].value;
       frm.selectedScoringSystem.value = selectedScoringSystem;
       
       var callImageDisplay =document.getElementById("callImageDisplay").checked ;
       var CalllabelFlag  = document.getElementById("CalllabelFlag").value;
       var callSectionLabel = document.getElementById("callSectionLabel").value;
       
       
       
       var precallFlag = document.getElementById('preCallFlag').value;
       var preCallLabel = document.getElementById('preCallLabel').value;
       var precallImage = document.getElementById('preCallFlag_image').value;
       
       var postCallFlag = document.getElementById('postCallFlag').value;
       var postCallLabel = document.getElementById('postCallLabel').value;
       var postCallImage = document.getElementById('postCallFlag_image').value;;
       
       var templatetitle= document.getElementById("templettitle").value;
       
       if(document.getElementById('overallEvaluationLable').value == null || document.getElementById('overallEvaluationLable').value == "")
    	{
    	   alert("Please enter Overall Evaluation Lable."); 
    	   return;
    	}
       
       var overallEvaluationLable = document.getElementById('overallEvaluationLable').value;
       
       
       if(document.getElementById("templatetitle_button").value.toLowerCase()=="save")
	   {
	   alert("Please enter template title.");
	   return;
	   }
       
       if(templatetitle == "" || templatetitle == null)
       {
    	   alert("Please enter Template Title.");
    	   return;
       }
       
       if(!validatePreCallSection())
    	   return;
       else if(document.getElementById("callSectionLabelDisplay").checked && document.getElementById("callSectionLabel").value == "")
       {
       	alert('Please enter Call section label');
       	return false;
       }
       else if(!validatePostCallSection())
    	   return;
       
     var url = 'evaluation/adminTemplatePreview.jsp?tname= Sales call&selectedScoringSystem='+selectedScoringSystem+'&preCallFlag='+ encodeURIComponent(precallFlag)+'&preCallLabel='+encodeURIComponent(preCallLabel)+'&postCallFlag='+ encodeURIComponent(postCallFlag)+'&postCallLabel='+encodeURIComponent(postCallLabel)+'&templatetitle='+encodeURIComponent(templatetitle)+'&callImageDisplay='+encodeURIComponent(callImageDisplay)+'&overallEvaluationLable='+encodeURIComponent(overallEvaluationLable)+'&CalllabelFlag='+encodeURIComponent(CalllabelFlag)+'&callSectionLabel='+encodeURIComponent(callSectionLabel)+'&precallImage='+encodeURIComponent(precallImage)+'&postCallImage='+encodeURIComponent(postCallImage);
       
      var numQ = document.getElementById('q_value').value;
      var maxDisplayQueNum = parseInt(numQ)+1;
      var numOfQuestionsRemoved = document.getElementById('numOfQuestionsRemoved').value;
      var numQ = parseInt(numQ) + parseInt(numOfQuestionsRemoved);
      for (i=0; i<=numQ; i++) 
      {
          if(document.getElementById('displayOrder_' + i) != null)
          {
               dispOrder = trim(document.getElementById('displayOrder_' + i).value);
               if(dispOrder == "")
               {
                  alert("Could not show preview screen without having numeric value in Question number field."); 
                  document.getElementById('displayOrder_' + i).focus();
                  return false;
              }
               if(isNaN(dispOrder))
               {
                     alert("Could not show preview screen without having numeric value in Question number field."); 
                     document.getElementById('displayOrder_' + i).focus();
                     return false;
               }
               if(dispOrder > maxDisplayQueNum)
               {
                      alert("Could not show preview without having valid Question number order in question section."); 
                      document.getElementById('displayOrder_' + i).focus();
                      return false;
               }
          }
      }
      
     
     if(document.getElementById('legalSectionFlag').value == "Y")
     { 
          var numOfLegalQuestions = document.getElementById('l_value').value;
          var numOfLegalQuestionsRemoved = document.getElementById('numOfLegalQuestionsRemoved').value;
          var totalNumOfLegalQuestions = parseInt(numOfLegalQuestions) + parseInt(numOfLegalQuestionsRemoved);
          if(totalNumOfLegalQuestions >= 0){
               var maxDisplayNum = parseInt(numOfLegalQuestions)+1;
               for(j=0; j<=totalNumOfLegalQuestions; j++) { 
                      if(document.getElementById('legal'+j+'_displayOrder') != null){
                           var displayOrder = document.getElementById('legal'+j+'_displayOrder').value;
                           if(document.getElementById('legal'+j+'_displayOrder') != null && document.getElementById('legal'+j+'_displayOrder').value == ""){
                                  alert("Could not show preview screen without having numeric value in Legal Question number field."); 
                                  document.getElementById('legal'+j+'_displayOrder').focus();
                                  return false;
                           }
                     
                          if(isNaN(displayOrder)){
                              alert("Could not show preview screen without having numeric value in Legal Question number field."); 
                              document.getElementById('legal'+j+'_displayOrder').focus();
                              return false;
                           }
                          if(displayOrder > maxDisplayNum){
                              alert("Could not show preview without having valid Legal Question number order in Legal section."); 
                              document.getElementById('legal'+j+'_displayOrder').focus();
                              return false;
                          }
                    
                      }   
                   }
             } 
          
      }
     
     window.open(url,'preview_window','status=yes,scrollbars=yes,height=600,width=800,resizable=yes');
}
   
/*Added by Ankit 28April2016*/
 function saveEdittemplateTitle(button)
	{
		if(button.value == 'Edit')
		{
			button.value='Save';
		document.getElementById("templatetitle_textbox").style.display="block";
		document.getElementById("templatetitle_lable").style.display="none";
		document.getElementById("cancel_button").style.display="block";
		
		}
		else if(button.value == 'Save')
		{
			if( document.getElementById("templatetitle_textbox").value == "")
			{
				alert("Please enter Template Title!");
				return;
			}
			button.value='Edit';
			document.getElementById("templatetitle_textbox").style.display="none";
			document.getElementById("templatetitle_lable").style.display="block";
			document.getElementById("templatetitle_lable").innerHTML=document.getElementById("templatetitle_textbox").value;
			document.getElementById("templettitle").value=document.getElementById("templatetitle_textbox").value;
			document.getElementById("cancel_button").style.display="none";
		}
	}
	
 function cancel_Edit(button)
	{
		document.getElementById("templatetitle_textbox").style.display="none";
		document.getElementById("templatetitle_lable").style.display="block";
		document.getElementById("cancel_button").style.display="none";
		document.getElementById("templatetitle_button").value="Edit";
	}
 
 function showHIdeDiv(obj,divName)    
 {   
     if(obj.checked == true)
		{
    	 document.getElementById(divName).style.display = 'block';     
     }else
		{
    	 document.getElementById(divName).style.display = 'none';
     }
     return false;
 } 
	

function setCallFlag(obj,callFlag,textbox)
{
		if(obj.checked == true)
			{
				document.getElementById(callFlag).value = "Y";
				document.getElementById(textbox).disabled = false;
          }else
			{
             document.getElementById(callFlag).value = "N";
             document.getElementById(textbox).disabled = true; 
			 document.getElementById(textbox).value = "";
          }
		
		if(callFlag=='preCallFlag')
		{
			enableDisablePreCallSectionChkbox();
		}
		else if(callFlag=='postCallFlag')
		{
			enableDisablePostCallSectionChkbox();
		}
}


function setCallImageFlag(obj,callFlag)
{
		if(obj.checked == true)
			{
				document.getElementById(callFlag).value = "Y";
          }
		  else
		  {
             document.getElementById(callFlag).value = "N";
          }
		
		if(callFlag=='preCallFlag_image')
		{
			enableDisablePreCallSectionChkbox();
		}
		else if(callFlag=='postCallFlag_image')
		{
			enableDisablePostCallSectionChkbox();
		}
}




function validatePreCallSection()
{
	if(document.getElementById("preCallDisplaySection").checked)
	{
		if(document.getElementById("preCallDisplay_image").checked== false && document.getElementById("preCallDisplay").checked== false )
		{
			alert('Please make a selection for Pre-Call display');
			return false;
		}
		else if(document.getElementById("preCallDisplay").checked== true && document.getElementById("preCallLabel").value == "")
		{
			alert('Please enter a label for Pre-Call Section');
			return false;
		}
	}
	return true;
}

function validatePostCallSection()
{
	if(document.getElementById("postCallDisplaySection").checked)
	{
		if(document.getElementById("postCallDisplay_image").checked== false && document.getElementById("postCallDisplay").checked== false )
		{
			alert('Please make a selection for Post-Call display');
			return false;
		}
		else if(document.getElementById("postCallDisplay").checked== true && document.getElementById("postCallLabel").value == "")
		{
			alert('Please enter a label for Post-Call Section');
			return false;
		}
	}
	return true;
}


function enableDisablePreCallSectionChkbox()
{
	if(document.getElementById("preCallDisplay_image").checked== true || document.getElementById("preCallDisplay").checked== true)
	{
		document.getElementById("preCallDisplaySection").disabled=true;
	}
	else
	{
		document.getElementById("preCallDisplaySection").disabled=false;
	}
}


function enableDisablePostCallSectionChkbox()
{
	if(document.getElementById("postCallDisplay_image").checked== true || document.getElementById("postCallDisplay").checked== true)
	{
		document.getElementById("postCallDisplaySection").disabled=true;
	}
	else
	{
		document.getElementById("postCallDisplaySection").disabled=false;
	}
}
 
 
function setCallSectionValues()
{
	/**/
	document.getElementById("preCallFlag_image").value="<%=templateVersion.getPreCallImage()%>" ;
	document.getElementById("preCallFlag").value="<%=templateVersion.getPreCallFlag()%>" ;
	
	document.getElementById("callSectionLabel").value='<%=templateVersion.getCallLabelValue()%>';
	
	document.getElementById("postCallFlag_image").value="<%=templateVersion.getPostCallImage()%>" ;
	document.getElementById("postCallFlag").value="<%=templateVersion.getPostCallFlag()%>" ;
	/**/
	//precall section
	if('<%=templateVersion.getPreCallImage()%>'=='Y')
	{
		document.getElementById("preCallDisplay_image").checked=true;
		
		document.getElementById("preCallDisplaySection").checked=true;
	}

	if('<%=templateVersion.getPreCallFlag()%>'=='Y')
	{
		document.getElementById("preCallDisplay").checked=true;
		
		document.getElementById("preCallDisplaySection").checked=true;
		document.getElementById("preCallLabel").disabled=false;
	}
	//precall section end
	
	//Call section
	if('<%=templateVersion.getCallLabelDisplay()%>'=='Y')
	{
		document.getElementById("callSectionLabelDisplay").checked=true;
		
		document.getElementById("callSectionLabel").disabled=false;
	}
	
	//Call section end
	
	//Post call section
	if('<%=templateVersion.getPostCallImage()%>'=='Y')
	{
		document.getElementById("postCallDisplay_image").checked=true;
		
		document.getElementById("postCallDisplaySection").checked=true;
	}
	
	if('<%=templateVersion.getPostCallFlag()%>'=='Y')
	{
		document.getElementById("postCallDisplay").checked=true;
		
		document.getElementById("postCallDisplaySection").checked=true;
		document.getElementById("postCallLabel").disabled=false;
	}
	//post call section end
	
	
	<% System.out.println("Legal section flag below:"+legalSectionFlag); %>
	//legal section display start
	
	  var sectionElement = document.getElementById("show_legal_section"); 
	   
	   
      if('<%=legalSectionFlag%>'=='Y')
      {
         sectionElement.style.display = 'block';
         if(document.getElementById("hlcCriticalFlag").value== "Y"){
         document.getElementById("hlcCritical").checked = true;
         document.getElementById("hlcCritical").value= "Y";
         document.getElementById("hlcCriticalFlag").value= "Y";
         }
         document.getElementById("legalSectionFlag").value = "Y";
      }else{
         sectionElement.style.display = 'none';
         document.getElementById("legalSectionFlag").value = "N";
      }
	
	
	//legal section display end
	
	
	showHIdeDiv(document.getElementById("preCallDisplaySection"),'preCall_label_div');
	showHIdeDiv(document.getElementById("postCallDisplaySection"), 'postCall_label_div');
	enableDisablePostCallSectionChkbox();
	enableDisablePreCallSectionChkbox();
	
} 

 
 /*end*/


        
    </script>
<style type="text/css">
	
	.templatetitle
	{
		width: auto !important;
		margin-bottom: 5px !important;
	}
	
	.buttontable 
	{
		border: none !important;
		width: auto !important;
	}
	
	.buttontable td 
	{
		border: none !important;
		border-bottom: none !important;
	}
	


</style>
</head>
<script language="JavaScript" type="text/JavaScript">
    <!--
    
    function load()    
        {
    	
           if(document.getElementById("legalSectionFlag").value == "Y")
           {
                document.getElementById("legalSection").checked = true;
            }
            var legalSection = document.getElementById("legalSection");
            validateLegalSection('legalSection'); 
            
            var form_title_duplicated = "<%=request.getAttribute("form_title_duplicated")%>";
           
            var form_title = "<%=request.getAttribute("form_title")%>";
            
            if(form_title_duplicated == "Y")
            {
                alert("A template with '"+form_title+"' is already in the system. Please provide a different name");
                document.forms[0].form_title.focus();       
           }
           var hlcCriticalFlag = document.getElementById("hlcCriticalFlag").value;
            if(hlcCriticalFlag != null && hlcCriticalFlag == 'N'){
                var hlcDiv = document.getElementById("show_HLC_value"); 
                hlcDiv.style.display = 'none';

           }
           
           setCallSectionValues();
       } 
    
    //-->
</script>

<body id="p_create_form" class="admin" onload="load()">
	<div id="wrap">
		<div id="top_head">
			<h1>Pfizer</h1>
			<h2>
			<input type="text"  class="templatetitle" style="display:none; width: 200px" id="templatetitle_textbox" placeholder="Enter Template title.">
				<%
					if (pageName != null && pageName.equals("create") && templateTitle.trim().equals("Sales Call Evaluation")) 
					{
				%>
				<label class="templatetitle" style="Display:block width: 200px" id="templatetitle_lable">Sales Call Evaluation</label>
				  <%}
					else
					{%>
				  <label class="templatetitle" style="Display:block; line-height: 1.4em; margin-top: -1%;" id="templatetitle_lable"><%=templateTitle%></label>
				  <%} %>
				  <input type="text"  style=" display: none;" name="templettitle" id="templettitle" value='<%=templateTitle%>'>
			 		<br>
			 		<br>
			 		<div style="margin-top: -7px;padding-bottom: 6px;">
					<input type="button" style="display:block;background-color: #2263AA;color: white;width: 40px;" id="templatetitle_button" onclick="saveEdittemplateTitle(this);" value="Edit">
					<input type="button" style="display:none; background-color: #2263AA;color: white;width: 50px; margin-left: 1%;" id="cancel_button" onclick="cancel_Edit(this);" value="Cancel">
					</div>
			</h2>


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


		<%
			if (pageName != null && pageName.equals("create")) {
		%>

		<h3>Admin: Create New Evaluation Form</h3>

		<%
			} else {
		%>

		<h3>Admin: Edit Evaluation Form</h3>

		<%
			}
		%>


		<div id="main_content">
			<form action="saveEvaluationTemplate" method="post">
			
				<input type="hidden" name="templettitle_hidden" id="templettitle_hidden" />
				
				<input type="hidden" name="callImageDisplay_hidden" id="callImageDisplay_hidden" value='<%=callimage%>'/>
				
				<input type="hidden" name="selectedScoringSystem"
					id="selectedScoringSystem" value="<%=selectedScoringSystem%>" /> <input
					type="hidden" name="selectedAutoCredit" id="selectedAutoCredit"
					value="<%=selectedAutoCredit%>" /> <input type="hidden"
					name="questionsCount" id="questionsCount"
					value="<%=questionsCount%>" /> <input type="hidden"
					name="businessRulesCount" id="businessRulesCount"
					value="<%=businessRulesCount%>" /> <input type="hidden"
					name="legalQuestionsCount" id="legalQuestionsCount"
					value="<%=legalQuestionsCount%>" /> <input type="hidden"
					name="templateVersionId" id="templateVersionId"
					value="<%=SCEUtils.replace(
					SCEUtils.ifNull(templateVersion.getId(), ""), "\"",
					"&quot;")%>" />
				<input type="hidden" name="version" id="version"
					value="<%=SCEUtils.replace(
					SCEUtils.ifNull(templateVersion.getVersion(), ""), "\"",
					"&quot;")%>" />
				<input type="hidden" name="templateId" id="templateId"
					value="<%=SCEUtils.replace(
					SCEUtils.ifNull(templateVersion.getTemplateId(), ""), "\"",
					"&quot;")%>" />
				<input type="hidden" name="templateName" id="templateName"
					value="<%=SCEUtils.replace(
					SCEUtils.ifNull(templateVersion.getTemplateName(), ""),
					"\"", "&quot;")%>" />
				<input type="hidden" name="numOfCriticalQuestions"
					id="numOfCriticalQuestions" value="<%=numOfCriticalQuestions%>" />
				<input type="hidden" name="legalSectionFlag" id="legalSectionFlag"
					value="<%=legalSectionFlag%>" /> <input type="hidden"
					name="pageName" id="pageName" value="<%=pageName%>" /> <input
					type="hidden" name="numOfScoringOptions" id="numOfScoringOptions"
					value="<%=numOfScoringOptions%>" /> <input type="hidden"
					name="precall_comments" id="precall_comments"
					value="<%=SCEUtils.replace(
					SCEUtils.ifNull(templateVersion.getPrecallComments(), ""),
					"\"", "&quot;")%>" />
				<input type="hidden" name="postcall_comments" id="postcall_comments"
					value="<%=SCEUtils.replace(
					SCEUtils.ifNull(templateVersion.getPostcallComments(), ""),
					"\"", "&quot;")%>" />
				<input type="hidden" name="overall_comments" id="overall_comments"
					value="<%=SCEUtils.replace(
					SCEUtils.ifNull(templateVersion.getOverallComments(), ""),
					"\"", "&quot;")%>" />
				<input type="hidden" name="publishFlag" id="publishFlag"
					value="<%=publishFlag%>" /> <input type="hidden"
					name="numOfBusinessRulesRemoved" id="numOfBusinessRulesRemoved"
					value="<%=numOfBusinessRulesRemoved%>" /> <input type="hidden"
					name="numOfQuestionsRemoved" id="numOfQuestionsRemoved"
					value="<%=numOfQuestionsRemoved%>" /> <input type="hidden"
					name="numOfLegalQuestionsRemoved" id="numOfLegalQuestionsRemoved"
					value="<%=numOfLegalQuestionsRemoved%>" /> <input type="hidden"
					name="hlcCriticalFlag" id="hlcCriticalFlag"
					value="<%=templateVersion.getHlcCritical()%>" />
					
					<input type="hidden" name="preCallFlag" id="preCallFlag"   value="<%=templateVersion.getPreCallFlag()%>" /> 
					<input type="hidden" name="postCallFlag" id="postCallFlag" value="<%=templateVersion.getPostCallFlag()%>" />
					
					<input type="hidden" name="CalllabelFlag" id="CalllabelFlag" value="<%=templateVersion.getCallLabelDisplay()%>" />
					<input type="hidden" name="preCallFlag_image" id="preCallFlag_image"   /> 
					<input type="hidden" name="postCallFlag_image" id="postCallFlag_image" />
										
				
				<!-- added by saikat -->
				<input type="hidden" name="scorecomments" id="scorecomments"
					value="<%=scorecommentFlag%>" />

				<fieldset>

					<div>
						<label>Evaluation Form Title:<font color="red">*</font></label>
						<%
							if (pageName != null && pageName.equals("create")) 
							{
						%>
						<input class="form_title" name="form_title" id="form_title"
							type="text" maxlength="100"
							value="<%=SCEUtils.replace(
						SCEUtils.ifNull(templateVersion.getFormTitle(), ""),
						"\"", "&quot;")%>" />
						<%
							} 
							else 
							{
						%>
						<input class="form_title" name="form_title" id="form_title"
							type="text"  maxlength="100"
							value="<%=SCEUtils.replace(
						SCEUtils.ifNull(templateVersion.getFormTitle(), ""),
						"\"", "&quot;")%>" />
						<%
							}
						%>
					</div>


					<div>

								<!-- value="" added for form reset and scoring system popup on select Edge release shindo -->
						<label>Scoring System:<font color="red">*</font></label> <select
							name="scoringSystem" width="300" style="width: 450px"
							onChange="setScoreValues(this)">
							<option value="">Select</option>

							<%
								Set keys = scoringSystemMap.keySet();
								Iterator itr = keys.iterator();
								while (itr.hasNext()) {
									String key = (String) itr.next();
									List autoCreditValues = (ArrayList) scoringSystemMap.get(key);
									String scoringsystemValue = "";
									for (int scoreCount = 0; scoreCount < autoCreditValues.size(); scoreCount++) {
										if (scoreCount != 0) {
											scoringsystemValue = scoringsystemValue + ",";
										}
										scoringsystemValue = scoringsystemValue
												+ autoCreditValues.get(scoreCount).toString();

									}
							%>
							<option value="<%=key%>"><%=scoringsystemValue%></option>
							<%
								}
							%>


						</select>


						<script language="JavaScript" type="text/JavaScript">
                                var frm = document.forms[0]; 
                                for(var count=0; count<frm.scoringSystem.options.length; count++) {//alert(frm.scoringSystem.options[count].value);
                                    if (frm.scoringSystem.options[count].value ==  frm.selectedScoringSystem.value) {
                                        frm.scoringSystem.options[count].selected = true;
                                    }   
                                }
                           </script>



					</div>


					<%
						if (scoringSystemValues != null) {
					%>

					<div>
						<label>Auto-Credit Value:<font color="red">*</font></label> <select
							name="autoCredit" onChange="setAutoCreditValue(this)">


							<%
								itr = scoringSystemValues.iterator();
									while (itr.hasNext()) {
										String scoringSystemValue = (String) itr.next();
							%>
							<option value="<%=scoringSystemValue%>"><%=scoringSystemValue%></option>

							<%
								}
							%>


						</select>

						<script language="JavaScript" type="text/JavaScript">
                                 
                                 var frm = document.forms[0]; 
                                 var pageName = frm.pageName.value;
                                 if(pageName != null && pageName == "edit"){
                                    for(var count=0; count<frm.autoCredit.options.length; count++) {
                                        if (frm.autoCredit.options[count].value ==  frm.selectedAutoCredit.value) {
                                            frm.autoCredit.options[count].selected = true;
                                        }   
                                    }
                                 }
                              </script>

					</div>


					<%
						}
					%>




					<hr>

					<div id="preCall_section_div">
						<div>
							<label> 
							<input type="checkbox" id="preCallDisplaySection" name="preCallDisplaySection" style="overflow: hidden; width: 14px; padding: 0; margin: 0; height: 13;" onclick="showHIdeDiv(this, 'preCall_label_div')" />
								Display Pre-Call Section
							</label>
						</div>
						<div>

							<div id="preCall_label_div" style="display: none">
								<input type="checkbox" id="preCallDisplay_image"
									style="overflow: hidden; width: 14px; padding: 0; margin: 0; height: 13;" onclick="setCallImageFlag(this,'preCallFlag_image')"/>
								<label>Display Pre Call Image</label>
								<div></div>
								<input type="checkbox" id="preCallDisplay"
									style="overflow: hidden; width: 14px; padding: 0; margin: 0; height: 13;"
									onclick="setCallFlag(this, 'preCallFlag','preCallLabel')" /> <label
									id="preCallLabelId">Pre-Call Label:<font color="red">*</font></label>
								<input class="form_title" name="preCallLabel" id="preCallLabel"
									type="text" maxlength="256"
									value="<%=SCEUtils.replace(
					SCEUtils.ifNull(templateVersion.getPreCallLabel(), ""),
					"\"", "&quot;")%>"
									disabled="disabled" />
							</div>
							<div></div>
							<hr>
							<div id="CallimageCheckbox">
							<label>Call Section</label>
							<div></div> 
								<%
									if (callimage) {
								%>
								<input type="checkbox" id="callImageDisplay"
									name="callImageDisplay"
									style="overflow: hidden; width: 14px; padding: 0; margin: 0; height: 13;"
									checked />
								<%
									} else {
								%><input type="checkbox" id="callImageDisplay"
									name="callImageDisplay"
									style="overflow: hidden; width: 14px; padding: 0; margin: 0; height: 13;" />
								<%
									}
								%>
								<label> Display Image </label>
								<div></div>
								<input type="checkbox" id="callSectionLabelDisplay"
									name="callSectionLabelDisplay"
									style="overflow: hidden; width: 14px; padding: 0; margin: 0; height: 13;" onclick="setCallFlag(this, 'CalllabelFlag','callSectionLabel')"/>
								<label> Display Label </label>
								<input class="form_title" name="callSectionLabel" id="callSectionLabel" type="text" maxlength="256" disabled="disabled"/>
								
							</div>
							<div></div>
							<hr>

							<div id="sce_div">





								<input type="hidden"
									value="<%=(questions != null ? questions.size() - 1 : -1)%>"
									id="q_value" name="q_value" />
								<%
									Question question = null;
									List objDescriptorList = null;
									Descriptor objDescriptor = null;
									if (questions != null) {
										for (int i = 0; i < questions.size(); i++) {
											question = (Question) questions.get(i);
											objDescriptorList = question.getDescriptorList();
											;
								%>
								<div id="q_<%=i%>">
									<div>
										<label>Question number:<font color="red">*</font></label> <input
											class="question_number" name="displayOrder_<%=i%>"
											id="displayOrder_<%=i%>" type="text"
											value="<%=SCEUtils.replace(
							SCEUtils.ifNull(question.getDisplayOrder(), ""),
							"\"", "&quot;")%>" />
									</div>

									<div>
										<label>Title:<font color="red">*</font></label> <input
											class="q_title" name="title_<%=i%>" id="title_<%=i%>"
											type="text" style="overflow: hidden;; padding: 0; margin: 0;"
											maxlength="256"
											value="<%=SCEUtils.replace(
							SCEUtils.ifNull(question.getTitle(), ""), "\"",
							"&quot;")%>" />
										<label style="float: right;">Critical</label>
										<%
											if (question.getCritical() != null
															&& (question.getCritical().equals("on") || question
																	.getCritical().equals("Y"))) {
										%>
										<input class="critical" style="float: right;"
											name="critical_<%=i%>" checked="checked" id="critical_<%=i%>"
											type="checkbox"
											style="overflow:hidden;width:14px;padding:0;margin:0;height:13;"
											onclick="setCritical(this)" />
										<%
											} else {
										%>
										<input class="critical" style="float: right;"
											name="critical_<%=i%>" id="critical_<%=i%>" type="checkbox"
											style="overflow:hidden;width:14px;padding:-1;margin:0;height:13;"
											onclick="setCritical(this)" />
										<%
											}
										%>
									</div>

									<div>
										<label>Question:<font color="red">*</font></label> <input
											class="question" name="description_<%=i%>"
											id="description_<%=i%>" type="text" maxlength="4000"
											value="<%=SCEUtils.replace(
							SCEUtils.ifNull(question.getDescription(), ""),
							"\"", "&quot;")%>" />
									</div>
									<input type="hidden"
										value="<%=(objDescriptorList != null ? objDescriptorList
							.size() - 1 : -1)%>"
										id="d_value_<%=i%>" name="d_value_<%=i%>" />
									<div id="q_d_<%=i%>">

										<%
											if (objDescriptorList != null
															&& objDescriptorList.size() > 0) {
														for (int j = 0; j < objDescriptorList.size(); j++) {
															objDescriptor = (Descriptor) objDescriptorList
																	.get(j);
										%>

										<div>
											<label>Descriptor<%=j + 1%>:
											</label> <input class="descriptor" name="descriptor_<%=i%>_<%=j%>"
												id="descriptor_<%=i%>_<%=j%>" type="text" maxlength="4000"
												value="<%=SCEUtils.replace(
									SCEUtils.ifNull(
											objDescriptor.getDescription(), ""),
									"\"", "&quot;")%>" />
										</div>

										<%
											}
													}
										%>


									</div>

									<div class="add_buttons">
										<img
											src="<%=request.getContextPath()%>/evaluation/resources/_img/button_add_descriptor.gif"
											alt="Add Descriptor" width="83" height="19"
											onclick="addDescriptor(<%=i%>)" /> <img
											src="<%=request.getContextPath()%>/evaluation/resources/_img/button_add_question.gif"
											alt="Add Question" width="81" height="19"
											onclick="addQuestion()" /> <img
											src="<%=request.getContextPath()%>/evaluation/resources/_img/button_preview_white.gif"
											alt="Preview" width="50" height="19"
											onclick="openPreviewWindow()" /> <img
											src="<%=request.getContextPath()%>/evaluation/resources/_img/button_delete_white.gif"
											alt="Delete Question" width="42" height="19"
											onclick="deleteQuestion(<%=i%>)" />
									</div>
									<div class="clear"></div>
								</div>


								<%
									}
									}
								%>



							</div>


							<!--  <hr>       -->

							<input type="hidden"
								value="<%=(businessRules != null ? businessRules.size() - 1 : -1)%>"
								id="b_value" name="b_value" />
							<div id="business_div">

								<%
									BusinessRule businessRule = null;
									if (businessRules != null) {
										for (int m = 0; m < businessRules.size(); m++) {
											businessRule = (BusinessRule) businessRules.get(m);
								%>

								<div id="b_<%=m%>">
									<%
										if (m == 0) {
									%>

									<hr>

									<%
										}
									%>
									<div>
										<label>Business Rule:</label> <input class="question_number"
											name="businessRule<%=m%>_displayOrder"
											id="businessRule<%=m%>_displayOrder" type="hidden"
											value="<%=SCEUtils.replace(
							SCEUtils.ifNull(businessRule.getDisplayOrder(), ""),
							"\"", "&quot;")%>" />
<!--  Start: AGARWN21: For fixing the business rule issue Bug fix release 2021 -->
										<select name="businessRule<%=m%>_numOfCriticalQuestions"
											id="businessRule<%=m%>_numOfCriticalQuestions"
											style="width: 35px" onChange="setValue(this)">
											<!-- End of AGARWN21 -->

											<%
												for (int c = 1; c <= numOfCriticalQuestions; c++) {
											%>

											<option value="<%=c%>"><%=c%></option>
											<%
												}
											%>
										</select>
										<script language="JavaScript" type="text/JavaScript">
                                 
                                 var frm = document.forms[0]; 
                                 var pageName = frm.pageName.value;
                                 var number = "<%=m%>";
                                 var selectedCritical = "<%=businessRule.getCriticalQuestions()%>";
                                 if(pageName != null && pageName == "edit"){
                                    var objName = "businessRule"+number+"_numOfCriticalQuestions";
                                    for(var count=0; count<document.getElementById(objName).options.length; count++) {
                                        if (document.getElementById(objName).options[count].value ==  selectedCritical) {
                                            document.getElementById(objName).options[count].selected = true;
                                        }   
                                    }
                                 }
                              </script>
<!-- Start: AGARWN21: For fixing the business rule issue Bug fix release 2021 -->
										or more Critical questions with a score of <select
											name="businessRule<%=m%>_score" id="businessRule<%=m%>_score" style="width: 90px"
											onChange="setValue(this)">
											<!-- End of AGARWN21 -->

											<%
												if (scoringSystemValues != null) {

															itr = scoringSystemValues.iterator();
															while (itr.hasNext()) {
																String scoringSystemValue = (String) itr.next();
											%>
											<option value="<%=scoringSystemValue%>"><%=scoringSystemValue%></option>
											<%
												}
														}
											%>



										</select>
										<script language="JavaScript" type="text/JavaScript">
                                 
                                 var frm = document.forms[0]; 
                                 var pageName = frm.pageName.value;
                                 var number = "<%=m%>";
                                 var selectedScore = "<%=businessRule.getScore()%>";
                                 if(pageName != null && pageName == "edit"){
                                    var objName = "businessRule"+number+"_score";
                                    for(var count=0; count<document.getElementById(objName).options.length; count++) {
                                        if (document.getElementById(objName).options[count].value ==  selectedScore) {
                                            document.getElementById(objName).options[count].selected = true;
                                        }   
                                    }
                                 }
                                 </script>
                                 <!--  Start: AGARWN21: For fixing the Business rule issue Bug fix release 2021 -->

										equals <select name="businessRule<%=m%>_overallScore"
											id="businessRule<%=m%>_overallScore"
											style="width: 90px" onChange="setValue(this)">
											<!-- End of AGARWN21 -->

											<%
												if (scoringSystemValues != null) {

															itr = scoringSystemValues.iterator();
															while (itr.hasNext()) {
																String scoringSystemValue = (String) itr.next();
											%>
											<option value="<%=scoringSystemValue%>"><%=scoringSystemValue%></option>

											<%
												}
														}
											%>

										</select> overall
										<script language="JavaScript" type="text/JavaScript">
                                 
                                         var frm = document.forms[0]; 
                                         var pageName = frm.pageName.value;
                                         var number = "<%=m%>";
                                         var selectedOverallScore = "<%=businessRule.getOverallScore()%>";
                                         if(pageName != null && pageName == "edit"){
                                            var objName = "businessRule"+number+"_overallScore";
                                            for(var count=0; count<document.getElementById(objName).options.length; count++) {
                                                if (document.getElementById(objName).options[count].value ==  selectedOverallScore) {
                                                    document.getElementById(objName).options[count].selected = true;
                                                }   
                                            }
                                         }
                                    </script>





										<img
											src="<%=request.getContextPath()%>/evaluation/resources/_img/button_delete_rule.gif"
											style="padding-bottom: 0cm;" alt="Delete Rule" width="72"
											height="19" onclick="deleteBusinessRule(<%=m%>)" />
									</div>
								</div>
								<!-- b_m div ends here   -->
								<%
									}
									}
								%>
							</div>
							<!--  business_div   ends here    -->

							<div class="add_buttons" id="business_add_div">

								<%
									if (businessRules != null && businessRules.size() > 0) {
								%>
								<div id="business_add_div1">
									<div>
										<img
											src="<%=request.getContextPath()%>/evaluation/resources/_img/button_add_rule.gif"
											alt="Add Rule" width="55" height="19"
											onclick="addBusinessRule()" />
									</div>
								</div>
								<!--  business_add_div1   ends here    -->
								<%
									}
								%>

							</div>


							<div id="business_conflict_div">

								<%
									if (businessRules != null && businessRules.size() > 1) {
								%>
								<div id="business_conflict_div1">
									<div>
										<label></label>
										<h4>Conflict Overwrite:</h4>
										<label></label> In case of a conflict between two or more
										business rules, the overall evaluated score shall be <select
											name="conflictOverallScore" style="width: 90px"
											onChange="setValue(this)">
											<%
												if (scoringSystemValues != null) {

														itr = scoringSystemValues.iterator();
														while (itr.hasNext()) {
															String scoringSystemValue = (String) itr.next();
											%>
											<option value="<%=scoringSystemValue%>"><%=scoringSystemValue%></option>

											<%
												}
													}
											%>
										</select>
										<script language="JavaScript" type="text/JavaScript">
                                 
                                 var frm = document.forms[0]; 
                                 var pageName = frm.pageName.value;
                                 if(pageName != null && pageName == "edit"){
                                    var selectedConflictOverallScore = "<%=templateVersion.getConflictOverallScore()%>";
                                    var objName = "conflictOverallScore";
                                    for(var count=0; count<document.getElementById(objName).options.length; count++) {
                                        if (document.getElementById(objName).options[count].value ==  selectedConflictOverallScore) {
                                            document.getElementById(objName).options[count].selected = true;
                                        }   
                                    }
                                 }
                              </script>



									</div>
								</div>
								<!--  business_conflict_div1   ends here    -->
								<%
									}
								%>

							</div>
							<!--  business_conflict_div   ends here    -->


							<div>
								<hr>
							</div>

							<div id="postCall_section_div">
								<div>
									<label> 
									<input type="checkbox" id="postCallDisplaySection"name="postCallDisplaySection" style="overflow: hidden; width: 14px; padding: 0; margin: 0; height: 13;" onclick="showHIdeDiv(this, 'postCall_label_div')" />
									Display Post-Call Section
									</label>
								</div>
								<div>
								

									<div id="postCall_label_div" style="display:none;">
									<input type="checkbox" id="postCallDisplay_image" style="overflow: hidden; width: 14px; padding: 0; margin: 0; height: 13;" onclick="setCallImageFlag(this,'postCallFlag_image')"/>
								<label>Display Post Call Image</label>
								<div></div>
								<input type="checkbox" id="postCallDisplay" style="overflow: hidden; width: 14px; padding: 0; margin: 0; height: 13;" onclick="setCallFlag(this, 'postCallFlag','postCallLabel')"/>
										<label id="postCallLabelId">Post-Call Label:<font
											color="red">*</font></label> <input class="form_title"
											name="postCallLabel" id="postCallLabel" type="text"
											maxlength="256"
											value="<%=SCEUtils.replace(
					SCEUtils.ifNull(templateVersion.getPostCallLabel(), ""),
					"\"", "&quot;")%>" disabled="disabled"/>
									</div>
									<div></div>







									<!--  <hr>       -->

									<div id="lms_div">




										<%
											if (scoringSystemValues != null) {
												EvaluationFormScore evaluationFormScore = null;

												itr = scoringSystemValues.iterator();
												String labelFlag = "Y";
												int scoreOptionsCount = 0;
												while (itr.hasNext()) {
													String scoringSystemValue = (String) itr.next();
										%>
										<%
											if (labelFlag.equals("Y")) {
										%>
										<div>
											<hr>
										</div>
										<label></label> <label
											style="border: 1; width: 90px; margin: 0; padding: 0;">&nbsp;Send
											to LMS</label>



										<%
											if (pageName != null && pageName.equals("edit")
																&& templateVersion.getPublishFlag().equals("Y")) {
										%>
										<%
											if (templateVersion.getEmailPublishFlag() != null
																	&& templateVersion.getEmailPublishFlag()
																			.equals("Y")) {
										%>
										<label style="border: 1; width: 45px; margin: 0; padding: 0;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Notification</label>
										<%
											}
										%>
										<%
											}
										%>

										<!-- added by saikat -->

										<label style="border: 1; width: 90px; margin: 0; padding: 0;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Comments</label>

										<!-- End by saikat -->
										<%
											labelFlag = "N";
													}
										%>

										<!-- added by saikat -->



										</script>





										<div>
											<label><%=scoringSystemValue%></label> <input type="hidden"
												id="evaluationForm<%=scoreOptionsCount%>_ScoreValue"
												name="evaluationForm<%=scoreOptionsCount%>_ScoreValue"
												value="<%=scoringSystemValue%>" />
											<%
												if (evaluationFormScores != null) {
															evaluationFormScore = (EvaluationFormScore) evaluationFormScores
																	.get(scoreOptionsCount);
															System.out
																	.println("evaluationFormScore.getLmsFlag()**********"
																			+ evaluationFormScore.getLmsFlag());
															System.out
																	.println("Saikat.getScorecommentFlag()**********"
																			+ evaluationFormScore
																					.getScorecommentFlag());
															if (evaluationFormScore.getScorecommentFlag() == null) {
																evaluationFormScore.setScorecommentFlag("N");
															}
														}
											%>
											<%
												if (evaluationFormScore.getLmsFlag() != null
																&& (evaluationFormScore.getLmsFlag().equals("on") || evaluationFormScore
																		.getLmsFlag().equals("Y"))) {
											%>
											<label
												for="evaluationFormScore<%=scoreOptionsCount%>_lmsFlag_y"
												style="border: 0; width: 45px; margin: 0; padding: 0;"><input
												type="radio"
												name="evaluationFormScore<%=scoreOptionsCount%>_lmsFlag"
												id="evaluationFormScore<%=scoreOptionsCount%>_lmsFlag_y"
												checked="checked"
												style="border: 0; width: 14px; margin: 0; padding: 0;"
												value="Y" onClick="setLMSFlag('<%=scoreOptionsCount%>','Y')" />Yes</label>
											<label
												for="evaluationFormScore<%=scoreOptionsCount%>_lmsFlag_n"
												style="border: 0; width: 45px; margin: 0; padding: 0;"><input
												type="radio"
												name="evaluationFormScore<%=scoreOptionsCount%>_lmsFlag"
												id="evaluationFormScore<%=scoreOptionsCount%>_lmsFlag_n"
												style="border: 0; width: 14px; margin: 0; padding: 0;"
												onClick="setLMSFlag('<%=scoreOptionsCount%>','N')" />No</label> <input
												type="hidden"
												id="selected_evaluationFormScore<%=scoreOptionsCount%>_lmsFlag"
												name="selected_evaluationFormScore<%=scoreOptionsCount%>_lmsFlag"
												value="Y" />
											<%
												} else {
											%>
											<label
												for="evaluationFormScore<%=scoreOptionsCount%>_lmsFlag_y"
												style="border: 0; width: 45px; margin: 0; padding: 0;"><input
												type="radio"
												name="evaluationFormScore<%=scoreOptionsCount%>_lmsFlag"
												id="evaluationFormScore<%=scoreOptionsCount%>_lmsFlag_y"
												style="border: 0; width: 14px; margin: 0; padding: 0;"
												value="Y" onClick="setLMSFlag('<%=scoreOptionsCount%>','Y')" />Yes</label>
											<label
												for="evaluationFormScore<%=scoreOptionsCount%>_lmsFlag_n"
												style="border: 0; width: 45px; margin: 0; padding: 0;"><input
												type="radio"
												name="evaluationFormScore<%=scoreOptionsCount%>_lmsFlag"
												id="evaluationFormScore<%=scoreOptionsCount%>_lmsFlag_n"
												checked="checked"
												style="border: 0; width: 14px; margin: 0; padding: 0;"
												onClick="setLMSFlag('<%=scoreOptionsCount%>','N')" />No</label> <input
												type="hidden"
												id="selected_evaluationFormScore<%=scoreOptionsCount%>_lmsFlag"
												name="selected_evaluationFormScore<%=scoreOptionsCount%>_lmsFlag" />
											<%
												}
											%>
											<%
												if (pageName != null && pageName.equals("edit")
																&& templateVersion.getPublishFlag().equals("Y")) {
											%>
											<%
												if (templateVersion.getEmailPublishFlag() != null
																	&& templateVersion.getEmailPublishFlag()
																			.equals("Y")) {
											%>

											<%
												if (evaluationFormScore.getEmailPublishFlag() != null
																		&& evaluationFormScore
																				.getEmailPublishFlag().equals("Y")) {
											%>

											<%
												if (evaluationFormScore.getNotificationFG() != null
																			&& (evaluationFormScore
																					.getNotificationFG().equals(
																							"on") || evaluationFormScore
																					.getNotificationFG()
																					.equals("Y"))) {
											%>
											<input class="critical"
												name="evaluationFormScore<%=scoreOptionsCount%>_notificationFG"
												id="evaluationFormScore<%=scoreOptionsCount%>_notificationFG"
												checked="checked" type="checkbox" value="Y"
												onclick="setNotificationFG(this)" />
											<%
												} else {
											%>
											<input class="critical"
												name="evaluationFormScore<%=scoreOptionsCount%>_notificationFG"
												id="evaluationFormScore<%=scoreOptionsCount%>_notificationFG"
												type="checkbox" value="N" onclick="setNotificationFG(this)" />
											<%
												}
											%>
											<%
												} else {
											%>
											<!--<div id="divCheckbox" style="visibility: hidden;"> -->
											<input class="critical"
												name="evaluationFormScore<%=scoreOptionsCount%>_notificationFG"
												id="evaluationFormScore<%=scoreOptionsCount%>_notificationFG"
												type="checkbox" value="N" disabled="disabled" />
											<!-- </div> -->
											<%
												}

															}
														}
											%>

											<!-- added by saikat-->
											<%
												if (evaluationFormScore.getScorecommentFlag() != null
																&& evaluationFormScore.getScorecommentFlag()
																		.equals("on")
																|| evaluationFormScore.getScorecommentFlag()
																		.equals("Y")) {
											%>
											<input class="critical"
												name="evaluationFormScore<%=scoreOptionsCount%>_scorecommentFG"
												id="evaluationFormScore<%=scoreOptionsCount%>_scorecommentFG"
												checked="checked" type="checkbox" value="Y"
												onclick="setcommentFG('<%=scoreOptionsCount%>',this)" /> <input
												type="hidden"
												id="selected_evaluationFormScore<%=scoreOptionsCount%>_commentFlag"
												name="selected_evaluationFormScore<%=scoreOptionsCount%>_commentFlag" />
											<Script language="JavaScript">
                                              //alert("If: evaluationFormScore<%=scoreOptionsCount%>_scorecommentFG :: " + evaluationFormScore<%=scoreOptionsCount%>_scorecommentFG);
                                              //alert("If");
                                              var eleName="evaluationFormScore"+<%=scoreOptionsCount%>+"_scorecommentFG";
                                              document.getElementById(eleName).value = 'Y';
                                              //alert("Amit1 "+  document.getElementById(eleName).value);
                                              
                                              var eleName1="selected_evaluationFormScore"+<%=scoreOptionsCount%>+"_commentFlag";
                                              document.getElementById(eleName1).value = 'Y';
                                             // alert("Amit2 "+  document.getElementById(eleName1).value);
                                              
                                             </script>
											<%
												} else {
											%>
											<input class="critical"
												name="evaluationFormScore<%=scoreOptionsCount%>_scorecommentFG"
												id="evaluationFormScore<%=scoreOptionsCount%>_scorecommentFG"
												type="checkbox" value="N"
												onclick="setcommentFG('<%=scoreOptionsCount%>',this)" /> <input
												type="hidden"
												id="selected_evaluationFormScore<%=scoreOptionsCount%>_commentFlag"
												name="selected_evaluationFormScore<%=scoreOptionsCount%>_commentFlag" />
											<Script language="JavaScript">
                                              //alert(" Else: evaluationFormScore<%=scoreOptionsCount%>_scorecommentFG :: " + evaluationFormScore<%=scoreOptionsCount%>_scorecommentFG);
                                              //alert("ELSE");
                                              var eleName="evaluationFormScore"+<%=scoreOptionsCount%>+"_scorecommentFG";
                                              document.getElementById(eleName).value = 'N';
                                              //alert("Amit3 "+ document.getElementById(eleName).value);
                                              
                                              
                                              var eleName1="selected_evaluationFormScore"+<%=scoreOptionsCount%>+"_commentFlag";
                                              document.getElementById(eleName1).value = 'N';
                                             // alert("Amit4 "+  document.getElementById(eleName1).value);
                                              
                                             </script>
											<%
												}
											%>
										</div>


										<%
											scoreOptionsCount++;
												}

											}
										%>




									</div>
									<!--  lms div ends here       -->



									<div>
										<hr>
									</div>
									<div id="legal_section_div">
										<div>
											<label> <input type="checkbox" name="legalSection" id="legalSection" 
												style="overflow: hidden; width: 14px; padding: 0; margin: 0; height: 13;"
												onclick="validateLegalSection(this)" />Display Legal
												Section
											</label>
										</div>
										<div></div>
										<div id="show_legal_section">
											<h4>Health Care Law Compliance (HLC):</h4>
											<div style="display: inline">
												<label><input type="checkbox" name="hlcCritical"
													id="hlcCritical"
													style="overflow: hidden; width: 14px; padding: 0; margin: 0; height: 13;"
													value="<%=templateVersion.getHlcCritical()%>"
													onclick="setHLCFlag(this)" />HLC Critical</label>
												<div id="show_HLC_value" style="display: inline">
													<label>HLC Value:<font color="red">*</font></label> <select
														name="hlcValue" id="hlcValue"
														style="width: 90px; margin: 0; paddingLeft: 0"
														onChange="setValue(this)">
														<%
															if (scoringSystemValues != null) {

																ListIterator litr = scoringSystemValues
																		.listIterator(scoringSystemValues.size());
																while (litr.hasPrevious()) {
																	String scoringSystemValue = (String) litr.previous();
														%>
														<option value="<%=scoringSystemValue%>"><%=scoringSystemValue%></option>

														<%
															}
															}
														%>
													</select>
													<script language="JavaScript" type="text/JavaScript">
                                 
                                                 var frm = document.forms[0]; 
                                                 var pageName = frm.pageName.value;
                                                 if(pageName != null && pageName == "edit"){
                                                    var selectedHlcValue = "<%=templateVersion.getHlcCriticalValue()%>";
                                                    var objName = "hlcValue";
                                                    for(var count=0; count<document.getElementById(objName).options.length; count++) {
                                                        if (document.getElementById(objName).options[count].value ==  selectedHlcValue) {
                                                            document.getElementById(objName).options[count].selected = true;
                                                        }   
                                                    }
                                                 }
                                              </script>

												</div>
												<!-- show_HLC_value div ends here   -->

											</div>





											<input type="hidden"
												value="<%=(legalQuestions != null ? legalQuestions.size() - 1 : -1)%>"
												id="l_value" name="l_value" />
											<div id="legal_div">
												<%
													LegalQuestion legalQuestion = null;
													if (legalQuestions != null) {
														for (int n = 0; n < legalQuestions.size(); n++) {
															legalQuestion = (LegalQuestion) legalQuestions.get(n);
												%>
												<div id="l_<%=n%>">

													<div>
														<label>Legal Question:<font color="red">*</font></label> <input
															class="question_number" name="legal<%=n%>_displayOrder"
															id="legal<%=n%>_displayOrder" type="text" maxlength="2"
															value="<%=SCEUtils.replace(SCEUtils.ifNull(
							legalQuestion.getDisplayOrder(), ""), "\"",
							"&quot;")%>" />
														<label style="width: 25px;"></label><input
															name="legal<%=n%>_question_label"
															id="legal<%=n%>_question_label" type="text"
															maxlength="100" style="width: 180px;"
															value="<%=SCEUtils.replace(
							SCEUtils.ifNull(
									legalQuestion.getLegalQuestionLabel(), ""),
							"\"", "&quot;")%>" />
														<label style="width: 25px;"></label>
														<textarea rows="2" cols="25"
															name="legal<%=n%>_question_text"
															id="legal<%=n%>_question_text"><%=SCEUtils.replace(SCEUtils.ifNull(
							legalQuestion.getLegalQuestion(), ""), "\"",
							"&quot;")%></textarea>
														<label style="width: 25px;"></label> <img
															src="<%=request.getContextPath()%>/evaluation/resources/_img/button_delete_white.gif"
															alt="Delete Legal Question" width="42" height="19"
															align="top" style="padding-top: 0pt;"
															onclick="deleteLegalQuestion(<%=n%>)" />
													</div>
												</div>
												<!-- l_n div ends here   -->
												<%
													}

													}
												%>



											</div>
											<!-- legal_div ends here-->



											<div class="add_buttons" id="legal_add_div">
												<%
													if (legalQuestions != null && legalQuestions.size() > 0) {
												%>
												<div>
													<img
														src="<%=request.getContextPath()%>/evaluation/resources/_img/button_add.gif"
														alt="Add Legal Questions" width="27" height="19"
														onclick="addLegalQuestion()" />
												</div>

												<%
													}
												%>
											</div>



										</div>
										<!-- show_legal_section ends here-->
									</div>
									<div>
										<hr>
									</div>
									<!-- legal_section_div ends here-->
									<!-- Final Sales Call lable -->
									<div>
									<label>Overall Evaluation Label:<font color="red">*</font></label>
											
									<input class="form_title" name="overallEvaluationLable" id="overallEvaluationLable" type="text" value="<%=SCEUtils.replace( SCEUtils.ifNull(templateVersion.getOverallEvaluationLable(),""), "\"", "&quot;")%>"/>
									</div>
									<div>
										<hr>
									</div>
									
									<!-- end -->

									
									<div id="comments_div">
										<div>
											<label>Comments for the Template:</label>
											<textarea rows="8" cols="55" name="comments" id="comments"
												style="border: .1em solid grey; display: block; width: 467px; height: 200px; float: left"><%=SCEUtils.replace(
					SCEUtils.ifNull(templateVersion.getComments(), ""), "\"",
					"&quot;")%></textarea>
										</div>


										<div align="center" style="height: 0; margin-top: 50px;">
											<img
												src="<%=request.getContextPath()%>/evaluation/resources/_img/button_preview.gif"
												alt="Preview" width="52" height="19"
												onclick="openPreviewWindow()" /> <img
												src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save.gif"
												alt="Save Evaluation Template" width="38" height="19"
												onclick="save()" /> <img
												src="<%=request.getContextPath()%>/evaluation/resources/_img/button_cancel.gif"
												alt="Cancel" width="55" height="19" onclick="cancel()" />
										</div>

									</div>
									<!-- comments_div ends here-->
				</fieldset>
			</form>

			<div class="clear"></div>
		</div>
		<!-- end #content -->

	</div>
	<!-- end #wrap -->


</body>
</html>
