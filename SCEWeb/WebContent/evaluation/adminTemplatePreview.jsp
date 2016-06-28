<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.beans.SCE"%>
<%@ page import="com.pfizer.sce.beans.ScoringSystem"%>
<%@ page import="com.pfizer.sce.common.SCEConstants"%>
<%@ page import="java.util.*"%>

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
<script type="text/javascript" src="<%=request.getContextPath()%>/evaluation/resources/js/validate.js"></script>   
<%

	String CalllabelFlag= request.getParameter("CalllabelFlag")!=null?request.getParameter("CalllabelFlag"):"N";
	String callSectionLabel = "";
	if(CalllabelFlag.equals("Y"))
	{
		callSectionLabel = request.getParameter("callSectionLabel");
	}
	
	String precallImage = request.getParameter("precallImage")!=null?request.getParameter("precallImage"):"N";
	String postCallImage= request.getParameter("postCallImage")!=null?request.getParameter("postCallImage"):"N";
	
	String precallFlag= request.getParameter("preCallFlag")!=null?request.getParameter("preCallFlag"):"N";
	String postcallFlag= request.getParameter("postCallFlag")!=null?request.getParameter("postCallFlag"):"N";

    String tName = request.getParameter("tname");
	String templateTitle= "Sales Call Evaluation";
	if(request.getParameter("templatetitle") != null)
	{
		templateTitle= request.getParameter("templatetitle");
	}
    boolean ifRBU = false;
   
    
    if(tName.startsWith("P2L")||tName.startsWith("RBU")){
        ifRBU = true;
    }

%>     



<%  
    Map scoringSystemDetailsMap = null;
    List scoringSystemDetailsObjs = null;
    String scoringSystemIdentifier = request.getParameter("selectedScoringSystem");
    if(session.getAttribute("scoringSystemDetailsMap") != null ){
       scoringSystemDetailsMap = (HashMap) session.getAttribute("scoringSystemDetailsMap");
       scoringSystemDetailsObjs = (ArrayList) scoringSystemDetailsMap.get(scoringSystemIdentifier);
    }

    
%>

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
            <div id="top_head_eval">
                <h1>Pfizer</h1>
                <h2><%=templateTitle%></h2>
                <!-- end #top_head -->
            </div>
            
         
         <!--commented on 5/5/2016 -->
            
          <%--   <div id="top_head_eval_1">
                <div id="eval_breadcrumbs_1">
               <font color="white">     Product > 
                    Classroom >
                    Table >
                    <span class="active">John Porter</span>     </font>               
                </div>
            </div> --%>
            
            
         <table id="logo">
                <tr>
                    <td style="text-align:left" width="20%"></td>
                    <td width="80%"><b><div id="form_title"></div></b></td>
                    <td  id="logo" width="20%"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/icon_compliance.gif" alt="" width="53" height="56" /></td>
                </tr>
            </table>
            
            
            <div id="main_content">
                <div id="eval_legend1">
                    Legend: 
                     <%
                     
                            if(scoringSystemDetailsObjs != null ){
                                 Iterator itr = scoringSystemDetailsObjs.iterator();
                                      while(itr.hasNext()){
                                       
                                       ScoringSystem scoringSystem = (ScoringSystem)itr.next();
                                       
                    %>
                    
                    
                    <b><%=scoringSystem.getScoreLegend()%></b> = <%=scoringSystem.getScoreValue()%>
                    
                    <%              
                                    }
                             }
                        %>
                   <b>N/A</b> = Not Applicable
                </div>
                
                <div id="rep_name">
                    <p><b>Representative name:</b> John Porter<span class="spacer"></span><b>Submitted By:</b> Bob Harrison<span class="spacer"></span><b>Date:</b> 01/14/07</p>		
                </div>
               
			<%if(precallImage.equals("Y")||precallFlag.equals("Y"))
			{%>
                <div class="arrows_content">
                <%if(precallImage.equals("Y")) {%>
                    <img class="arrows" src="<%=request.getContextPath()%>/evaluation/resources/_img/arrow_pre.gif" alt="Pre Call" width="78" height="27" />
                 <%}%>
                 <%if(precallFlag.equals("Y")){ %>
                    <label><%= request.getParameter("preCallLabel")%>:</label>
                  <%} %>
                <div>
                    <table cellspacing="2">
                        <tr>
                        <%int count=0;
                            if(scoringSystemDetailsObjs != null ){
                                // System.out.println("length"+scoringSystemDetailsObjs.size());
                                 Iterator itr = scoringSystemDetailsObjs.iterator();
                                 
                                      while(itr.hasNext()){
                                         count=count+1;
                                       ScoringSystem scoringSystem = (ScoringSystem)itr.next();
                                      // System.out.println("count::"+count);
                                       if(5>count){
                                       
                         %>
                                <td><input class="radio_btn" name="precall_rating" type="radio" disabled style="overflow:hidden;width:14px;padding:-1;margin:0;height:15px;" value="<%=scoringSystem.getScoreLegend()%>" /><b><%=scoringSystem.getScoreLegend()%></b></td>
                         <%              
                                       }else{
                                        %>
                                <td><input class="radio_btn" name="precall_rating" type="radio" disabled style="overflow:hidden;width:14px;padding:-1;margin:0px;height:15px;" value="<%=scoringSystem.getScoreLegend()%>" /><b><%=scoringSystem.getScoreLegend()%></b></td>
                         <% 
                                       }    
                                    }
                             }
                        %>
                                 
                            <td><input  class="radio_btn" type="checkbox" disabled style="overflow:hidden;width:14px;padding:-1;margin-left:0px;height:15;" value=""><b>N/A</b></td>
                            
                            <%if (ifRBU){%>
                            <td><input class="radio_btn" name="precall_rating" type="radio" disabled value="<%=SCEConstants.NA%>" /><b><%=SCEConstants.NA%></b></td>
                            <%}%>
                        </tr>
                        <tr>
                            <%if (ifRBU){%>
                            <td colspan="<%=scoringSystemDetailsObjs.size()%>">
                            <%}else{%>
                             <td colspan="<%=scoringSystemDetailsObjs.size()%>">
                            <%}%>
                            <textarea class="eval_comments" rows="3" cols="1" name="precall_comments" id="precall_comments" disabled></textarea></td>        
                        </tr>
                    </table>
                </div>
                </div> <!--End arrows content-->
                
                <hr />
                
                <% } %>
                
                 
			
			
                <div class="arrows_content">
                	<%if(request.getParameter("callImageDisplay") != null && request.getParameter("callImageDisplay").toLowerCase().equals("true"))
					{%>
                    <img class="arrows" src="<%=request.getContextPath()%>/evaluation/resources/_img/arrow_call.gif" alt="Call" width="78" height="27" />
               		<%} %>
               <%if(CalllabelFlag.equals("Y")){ %>
                    <p class="call_p"><b><%=callSectionLabel%></b><br /></p>
                    <%} %>
                </div> <!--End arrows content-->
            
                
                <div id="questions">
                </div>
                
                <div class="clear"></div>
                
			
            
            <%if(postCallImage.equals("Y")||postcallFlag.equals("Y")){%>
               <hr />
                <div class="arrows_content">
                	<%if(postCallImage.equals("Y")){ %>
                    <img class="arrows" src="<%=request.getContextPath()%>/evaluation/resources/_img/arrow_post.gif" alt="Post Call" width="78" height="27" />
                    <%} %>
                    <%if(postcallFlag.equals("Y")){%>
                    <label><%=request.getParameter("postCallLabel")%>:</label>
                	<%} %>
                    <table cellspacing="0">
                        <tr>
                        
                                <%
                                int count=0;
                                if(scoringSystemDetailsObjs != null ){
                                     Iterator itr = scoringSystemDetailsObjs.iterator();
                                          while(itr.hasNext()){
                                            count=count+1;
                                           ScoringSystem scoringSystem = (ScoringSystem)itr.next();
                                           if(5>count){
                               %>
                                <td><input class="radio_btn" name="postcall_rating" type="radio" disabled style="overflow:hidden;width:14px;padding:-1;margin:0px;height:15px;" value="<%=scoringSystem.getScoreLegend()%>"  /><b><%=scoringSystem.getScoreLegend()%></b></td>
                               <%              
                                           }else{
                                             %>
                                <td><input class="radio_btn" name="postcall_rating" type="radio" disabled style="overflow:hidden;width:14px;padding:-1;margin:0px;height:15px;" value="<%=scoringSystem.getScoreLegend()%>"  /><b><%=scoringSystem.getScoreLegend()%></b></td>
                               <% 
                                           }     
                                        }
                                 }
                              %>
                            <td><input  type="checkbox" disabled style="overflow:hidden;width:14px;padding:-1;margin:0;height:15;" value=""><b>N/A</b></td>     

                            <%if(ifRBU){%>
                            <td><input class="radio_btn"  name="postcall_rating" type="radio"  disabled  value="<%=SCEConstants.NA%>" /><b><%=SCEConstants.NA%></b></td>
                            <%}%>
                        </tr>
                        <tr><!--take care1-->
                             <%if (ifRBU){%>
                            <td colspan="<%=scoringSystemDetailsObjs.size()%>">
                            <%}else{%>
                             <td colspan="<%=scoringSystemDetailsObjs.size()%>">
                            <%}%>
                            <textarea class="eval_comments" rows="3" cols="1" name="postcall_comments" id="postcall_comments" disabled></textarea></td>
                        </tr>
                    </table>			                    
                </div> <!--End arrows content-->		
			
                <div class="comments">
                    <label>Comments:</label>	
                    <table cellspacing="0">				
                        <tr>
                            <td colspan="3"><textarea class="eval_comments" rows="3" cols="1" name="comments" id="comments" disabled ></textarea></td>
                        </tr>
                    </table>
                </div>
                
                <% } %>
                
                <!--legal section here-->
                    <div id="legal_questions" >
                    <hr/>
                </div>
                
                <div class="clear"></div>
                
                 
			
    			<div class="arrows_content overall">
        			<label><%=request.getParameter("overallEvaluationLable") %>:</label>
			
                    <table cellspacing="0">
                        <tr>
                            <%
                            int count=0;
                            if(scoringSystemDetailsObjs != null ){
                                 Iterator itr = scoringSystemDetailsObjs.iterator();
                                      while(itr.hasNext()){
                                        count=count+1;
                                       ScoringSystem scoringSystem = (ScoringSystem)itr.next();
                                       if(5>count){
                         %>
                                <td><input class="radio_btn" name="overall_rating" type="radio" disabled style="overflow:hidden;width:14px;padding:-1;margin:0;height:15px;" value="<%=scoringSystem.getScoreLegend()%>" /><b><%=scoringSystem.getScoreLegend()%></b></td>
                         <%              
                                       }else{
                                                    %>
                                <td><input class="radio_btn" name="postcall_rating" type="radio" disabled style="overflow:hidden;width:14px;padding:-1;margin:0px;height:15px;" value="<%=scoringSystem.getScoreLegend()%>"  /><b><%=scoringSystem.getScoreLegend()%></b></td>
                               <% 
                                        
                                       }   
                                    }
                             }
                        %>                             <%if (ifRBU){%>
                            <td><input class="radio_btn" name="overall_rating" type="radio" disabled style="overflow:hidden;width:14px;padding:-1;margin:0;height:15px;" value="<%=SCEConstants.NA%>" /><b><%=SCEConstants.NA%></b></td>
                            <%}%>
                        </tr>
                        <tr>
                            <%if (ifRBU){%>
                            <td colspan="<%=scoringSystemDetailsObjs.size()%>">
                            <%}else{%>
                             <td colspan="<%=scoringSystemDetailsObjs.size()%>">
                            <%}%>
                            <textarea class="eval_comments" rows="3" cols="1" name="overall_comments" id="overall_comments" disabled ></textarea></td>        
                        </tr>
                    </table>			
                </div> <!--End arrows content-->
			
                <div class="clear"></div>	
            </div> <!-- end #content -->
            
        </div><!-- end #wrap -->

    </body>
</html>
        

<script language="javascript">

    var questionNum = 0;
    
    function addQuestion(i) {
        questionNum++;
        //alert('In addQuestionTitle');
        var html = '';
        var ni = document.getElementById('questions');
        var titlediv = document.createElement('div');
        
        titlediv.setAttribute('className','question_title');
        html += '<h5>' + arrQTitle[i] + '</h5>';
        html += '<img class="question_right" src="<%=request.getContextPath()%>/evaluation/resources/_img/question_img_right.gif" alt="" width="14" height="17" />';

        titlediv.innerHTML = html;
        //alert('html:' +   html)                  
        ni.appendChild(titlediv);
        
        
        html = '';
        var descDiv = document.createElement('div');
        
        descDiv.setAttribute('className','question_content');
        html += '<h6>' + questionNum + '. ' + arrQDesc[i] + '</h6>';
        //alert("objDesc[i].length:"+objDesc[i].length);
        html += '<ul class="bullets">';
        for (j=0; j<objDesc[i].length; j++) {
            if(objDesc[i][j] != null && objDesc[i][j] != ""){
                html += '<li>' + objDesc[i][j] + '</li>';
            }
        }               
        html += '</ul>';
                    
        descDiv.innerHTML = html;
        ni.appendChild(descDiv);
        
        
        html = '';
        var ratingDiv = document.createElement('div');
        
        ratingDiv.setAttribute('className','question_eval_content');
        
        html += '<table cellspacing="10" >';
        html += '<tr>';
        
         <%
            if(scoringSystemDetailsObjs != null ){
                 Iterator itr = scoringSystemDetailsObjs.iterator();
                      while(itr.hasNext()){
                       ScoringSystem scoringSystem = (ScoringSystem)itr.next();
                       
           %>
            html += '<td><input class="radio_btn" type="radio" disabled value=""><b><%=scoringSystem.getScoreLegend()%></b></td>';
            
           <%              
                       
                    }
             }
          %>
        //rupu
        html += '<td><input  type="checkbox" disabled style="overflow:hidden;width:14px;padding:-1;margin:0;height:15;" value=""><b>N/A</b></td>';
        html += '</tr>';
        html += '<tr>';
        html += '<td colspan="<%=scoringSystemDetailsObjs.size()%>"><textarea rows="3" cols="1" disabled></textarea></td>';                                
        html += '</tr>';
        html += '</table>';
                    
        ratingDiv.innerHTML = html;
        ni.appendChild(ratingDiv);
    
        html = '';
        var clDiv = document.createElement('div');
        clDiv.setAttribute('className','clear');
        clDiv.innerHTML = html;
        ni.appendChild(ratingDiv);
    }

    
    function sortQuestionsByOrder() {
        Quicksort(arrQOrder, 0, arrQOrder.length-1);
    }
    
    function Quicksort(vec, loBound, hiBound)
	{

		var pivot, loSwap, hiSwap, temp;
        
		// Two items to sort
		if (hiBound - loBound == 1)
		{
			if (parseInt(vec[loBound]) > parseInt(vec[hiBound]))
			{
				temp = vec[loBound];
				vec[loBound] = vec[hiBound];
				vec[hiBound] = temp;
                
                temp1 = arrQTitle[loBound];
				arrQTitle[loBound] = arrQTitle[hiBound];
				arrQTitle[hiBound] = temp1;
                //alert(arrQTitle[loBound]);
                //alert(arrQTitle[hiBound]);
                
                temp2 = arrQDesc[loBound];
				arrQDesc[loBound] = arrQDesc[hiBound];
				arrQDesc[hiBound] = temp2;
                
                temp3 = objDesc[loBound];
				objDesc[loBound] = objDesc[hiBound];
				objDesc[hiBound] = temp3;                
			}
			return;
		}

		// Three or more items to sort
		pivot = vec[parseInt((loBound + hiBound) / 2)];
		vec[parseInt((loBound + hiBound) / 2)] = vec[loBound];
		vec[loBound] = pivot;
        
        pivot1 = arrQTitle[parseInt((loBound + hiBound) / 2)];
		arrQTitle[parseInt((loBound + hiBound) / 2)] = arrQTitle[loBound];
		arrQTitle[loBound] = pivot1;
        
        pivot2 = arrQDesc[parseInt((loBound + hiBound) / 2)];
		arrQDesc[parseInt((loBound + hiBound) / 2)] = arrQDesc[loBound];
		arrQDesc[loBound] = pivot2;
        
        pivot3 = objDesc[parseInt((loBound + hiBound) / 2)];
		objDesc[parseInt((loBound + hiBound) / 2)] = objDesc[loBound];
		objDesc[loBound] = pivot3;
        
        loSwap = loBound + 1;
		hiSwap = hiBound;

		do {
			// Find the right loSwap
			while (loSwap <= hiSwap && parseInt(vec[loSwap]) <= parseInt(pivot))
				loSwap++;

			// Find the right hiSwap
			while (parseInt(vec[hiSwap]) > parseInt(pivot))
				hiSwap--;

			// Swap values if loSwap is less than hiSwap
			if (loSwap < hiSwap)
			{
				temp = vec[loSwap];
				vec[loSwap] = vec[hiSwap];
				vec[hiSwap] = temp;
                //alert('vec[loSwap]:' + vec[loSwap]);
                //alert('vec[hiSwap]:' + vec[hiSwap]);
                
                temp1 = arrQTitle[loSwap];
				arrQTitle[loSwap] = arrQTitle[hiSwap];
				arrQTitle[hiSwap] = temp1;
                
                temp2 = arrQDesc[loSwap];
				arrQDesc[loSwap] = arrQDesc[hiSwap];
				arrQDesc[hiSwap] = temp2;
                
                temp3 = objDesc[loSwap];
				objDesc[loSwap] = objDesc[hiSwap];
				objDesc[hiSwap] = temp3;                
			}
		} while (loSwap < hiSwap);

		vec[loBound] = vec[hiSwap];
		vec[hiSwap] = pivot;

        arrQTitle[loBound] = arrQTitle[hiSwap];
		arrQTitle[hiSwap] = pivot1;
        
        arrQDesc[loBound] = arrQDesc[hiSwap];
		arrQDesc[hiSwap] = pivot2;
        
        objDesc[loBound] = objDesc[hiSwap];
		objDesc[hiSwap] = pivot3;

		// Recursively call function...  the beauty of quicksort

		// 2 or more items in first section		
		if (loBound < hiSwap - 1)
			Quicksort(vec, loBound, hiSwap - 1);


		// 2 or more items in second section
		if (hiSwap + 1 < hiBound)
			Quicksort(vec, hiSwap + 1, hiBound);
	}

    function addLegalQuestions(num,maxDisplayNum) {
        var legalQuestionNum = 0;
        
        var displayOrderArray = new Array();
        //var orderCount = 0;
        for(j=0; j<=num; j++) {  //alert("start"+j);//alert(parent.document.getElementById('legal'+j+'_displayOrder'));
        //alert("end"+j);
        if(parent.document.getElementById('legal'+j+'_displayOrder') != null){
                var displayOrder = parent.document.getElementById('legal'+j+'_displayOrder').value; //alert("displayOrder:"+displayOrder);
                if(isNaN(displayOrder)){
                    alert("Legal Question Display Order should be a number.");
                    window.close();
                }
               // var maxDisplayNum = parseInt(num)+1; //numOfLegalQuestions
                if(displayOrder > maxDisplayNum){
                    alert("Please Enter Legal Question Numbers in order.");
                    window.close();
                }
                displayOrderArray[displayOrder-1] = j;
                //displayOrderArray[displayOrder-1] = orderCount;
                //orderCount++;
          }
        }
        
        for(j=0; j<=num; j++) {
            legalQuestionNum++;
            
            //var displayOrder = parent.document.getElementById('legal'+j+'_displayOrder').value;
            displayOrder = displayOrderArray[j];
            if(displayOrder != null){
                //displayOrder--;
                var label = parent.document.getElementById('legal'+displayOrder+'_question_label').value;
                var question = parent.document.getElementById('legal'+displayOrder+'_question_text').value;
                
                var html = '';
                var ni = document.getElementById('legal_questions');
                var legal_titlediv = document.createElement('div');
                legal_titlediv.setAttribute('className','legal_title');
                if(j == 0){//alert(" start point j:"+j+" num:"+num);
                  // html += '<label>Legal Section:</label>';
                }
                //html += '<h6>' + legalQuestionNum+ '. Q'+legalQuestionNum+'</h6>';
                html += '<h5>' + label + '</h5>';
                html += '<img  src="<%=request.getContextPath()%>/evaluation/resources/_img/question_img_right.gif" alt="" width="14" height="17" /> <br />';
                legal_titlediv.innerHTML = html;
                ni.appendChild(legal_titlediv);
                
                var html = '';
                var qni = document.getElementById('legal_questions');
                var legal_questiondiv = document.createElement('div');
                legal_questiondiv.setAttribute('className','legal_question');
                html += '<table  id="legalnote_box" style="width:95%" cellspacing="0">';
                html += '<tr>';    
                //html += '<td width ="25%">'+label+':</td>';
                html += '<td width ="85%"><i>'+question+'</i></td>';
                html += '<td width ="15%" style="align:right">';
                html += '<input class="radio_btn" name="healthcare_compliant" type="radio" disabled value="Y" />Yes';
                html += '&nbsp;<input class="radio_btn" name="healthcare_compliant" type="radio" disabled value="N" />No';
                html += '</td>';
                html += '</tr>';
                html += '</table>';
                if(j == num){ //alert("j:"+j+" num:"+num);
                    html += '<hr />';
                    
                }
                legal_questiondiv.innerHTML = html;
                //alert('html:' +   html)                  
                ni.appendChild(legal_questiondiv);
            }
        }
    }    
    
    var parent = window.opener;
    
    var arrQOrder = new Array();
    var arrQTitle = new Array();
    var arrQDesc = new Array();
    var objDesc = new Object();
    var arrDesc;
    var numQD = 0;
    
    var numQ = window.opener.document.getElementById('q_value').value;
    var maxDisplayQueNum = parseInt(numQ)+1;
    var numOfQuestionsRemoved = parent.document.getElementById('numOfQuestionsRemoved').value;
    var numQ = parseInt(numQ) + parseInt(numOfQuestionsRemoved);
    //alert('numQ:' + numQ.value);
    var dispOrder, qTitle, qDesc, desc;
    var numDesc = 0;
    var numQues = 0;
    
    for (i=0; i<=numQ; i++) {
    
        if(window.opener.document.getElementById('displayOrder_' + i) != null){
            numDesc = 0;
            arrDesc = new Array();
         
            dispOrder = trim(window.opener.document.getElementById('displayOrder_' + i).value);
             if(isNaN(dispOrder)){
                    alert("Question Display Order should be a number.");
                    window.close();
             }
             if(dispOrder > maxDisplayQueNum){
                    alert("Please Enter Question Numbers in order.");
                    window.close();
             }
            qTitle = trim(window.opener.document.getElementById('title_' + i).value);
            qDesc = trim(window.opener.document.getElementById('description_' + i).value);
            
            numQD = window.opener.document.getElementById('d_value_' + i);
            //alert('numQD.value:' + numQD.value);
            for (j=0; j<=numQD.value; j++) {
                //alert('i:' + i + ' j:' + j);
                desc = trim(window.opener.document.getElementById('descriptor_' + i + '_' + j).value);
                if (desc != '') {
                    arrDesc[numDesc] = desc;
                }
                numDesc++;
            }
            
            if (qTitle != '' && qDesc != '') {
                arrQOrder[numQues] = dispOrder;
                arrQTitle[numQues] = qTitle;
                arrQDesc[numQues] = qDesc;
                
                objDesc[numQues] = new Array();
                
                for (j=0; j<numDesc; j++) {
                    objDesc[numQues][j] = arrDesc[j];
                }
                numQues++;
            }
            
        }
    }
    
    sortQuestionsByOrder();
    /* Form Title */
    document.getElementById('form_title').innerHTML = parent.document.getElementById('form_title').value;
    
    //var numQuestions = window.opener.document.getElementById('q_value').value; ////added for SCE-TRT Enhancement 2011
    for (i=0; i<numQues; i++) {  //removed for SCE-TRT Enhancement 2011
    //for (i=0; i<=numQuestions; i++) {   //added for SCE-TRT Enhancement 2011
        addQuestion(i);
    }
    //alert("legalSectionFlag:"+parent.document.getElementById('legalSectionFlag').value);
    if(parent.document.getElementById('legalSectionFlag').value == "Y"){ 
    var numOfLegalQuestions = parent.document.getElementById('l_value').value;
    var numOfLegalQuestionsRemoved = parent.document.getElementById('numOfLegalQuestionsRemoved').value;
    var totalNumOfLegalQuestions = parseInt(numOfLegalQuestions) + parseInt(numOfLegalQuestionsRemoved);
        //alert("numOfLegalQuestions:"+numOfLegalQuestions);
        if(totalNumOfLegalQuestions >= 0){
        //alert('legal');
        var maxDisplayNum = parseInt(numOfLegalQuestions)+1;
        addLegalQuestions(totalNumOfLegalQuestions,maxDisplayNum);
        }
    }
    
    
    
</script>
