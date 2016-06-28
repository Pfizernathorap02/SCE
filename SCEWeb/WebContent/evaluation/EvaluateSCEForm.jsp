<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>
 

<%@ page import="com.pfizer.sce.beans.Attendee"%>
<%@ page import="com.pfizer.sce.beans.Event"%>
<%@ page import="com.pfizer.sce.beans.Role"%>
<%@ page import="com.pfizer.sce.beans.SCE"%>
<%@ page import="com.pfizer.sce.beans.SCEDetail"%>
<%@ page import="com.pfizer.sce.beans.User"%>
<%@ page import="com.pfizer.sce.beans.SCEinput"%>
<%@ page import="com.pfizer.sce.common.SCEConstants"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.pfizer.sce.beans.SCEComment"%>
<%@ page import="com.pfizer.sce.beans.Util"%>

<%@ page import= "javax.servlet.http.HttpServletRequest"%>
<%@ page import= "javax.servlet.http.HttpSession"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<style type="text/css">
@media print {
    .noprint { display: none; }
}

@media screen {
    .print { display: none; }
}
</style>

<% SCEComment[] objSCE = (SCEComment[])request.getAttribute("sceToDisplay"); %>

<%if (objSCE != null) {%>
<object id="factory" viewastext  style="display:none"
  classid="clsid:1663ed61-23eb-11d2-b92f-008048fdd814"
  codebase="<%=request.getContextPath()%>/evaluation/smsx.cab#Version=6,3,434,26">
</object>
<%}%>

 <script type="text/javascript">
 function cleanForm() {
        alert("1");   
        var the_form = document.forms[0];    
        for ( var x in the_form ) {        
            if ( ! the_form[x] ) continue;                    
            if( typeof the_form[x].rows != "number" ) continue;    
            alert(the_form[x].name);
            if (the_form[x].name.indexOf("print_") == 0) {
                the_form[x].rows = countLines(the_form[x].value,the_form[x].cols) + 1;    
            }
        }            
    }
function zoomScaleBeforePrint() {
    	//cleanForm();			
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
    
function limitText(limitField) {
fldValue = limitField.value;
var limitNum = 4000;
var chars = limitNum - fldValue.length;
//alert(chars);
if (chars <= 0) {
alert ("You are trying to enter more than the limit of " + limitNum + " characters! ");
fldValue = fldValue.substring(0,limitNum-1)
document.phase1.comment1.value = fldValue;
}
}


    
    function showDocument(name) {
      window.open('<%=request.getContextPath()%>/evaluation/resources/pdf/'+name,'newDocWin','height=720,width=616,resizable,scrollbars');
    }
</script>

<%
    Util util = new Util();
    DateFormat dateFormatter = new SimpleDateFormat(SCEUtils.DEFAULT_DATE_FORMAT);
    DateFormat dateFormatter1 = new SimpleDateFormat("MMM d, yyyy");
    DecimalFormat weightFormatter = new DecimalFormat("###0.##");
    DecimalFormat scoreFormatter = new DecimalFormat("###0.##");
    String url = request.getRequestURL().toString();
    System.out.println("URL###"+url);
    
    
        
    String enc= request.getCharacterEncoding();
    SCEinput objSCEin = (SCEinput)request.getAttribute("Input");    
    SCEComment[] objSCECom = (SCEComment[])request.getAttribute("ObjectComment");
    
    
    if(objSCEin==null)
    {
       objSCEin =  (SCEinput)session.getAttribute("Input");
       System.out.println("HEYYYY");
    }
    if(objSCECom==null)
    {
       objSCECom =  (SCEComment[])session.getAttribute("ObjectComment");
    }
    
    System.out.println("printing messages***&&&&&&**");
    System.out.println(enc);
    System.out.println(objSCEin.toString());
    String evalName = "None";
    evalName = objSCEin.getEvalName();
    String trackId= objSCEin.getTrackId();
    String activityId = objSCEin.getActivityId();
    String repId = objSCEin.getEmplId();
    String flag = objSCEin.getFlag(); 
    String sceId= objSCEin.getSceId();
    String phaseNo = null;
    String submitFlag = null;
    String empName=objSCEin.getEmpName();         
    empName=empName.replaceAll("\\<", "&#39;");  
    
    if(evalName == null)
    {
        evalName="None";
    }
    
    System.out.println("EvalName"+objSCEin.getActivityId());
  %>

<html>
     <head>         
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
            <meta http-equiv="Content-Language" content="en-us" />
            <title>Pfizer Sales Call Evaluation</title>
            <meta name="ROBOTS" content="ALL" />
            <meta http-equiv="imagetoolbar" content="no" />
            <meta name="MSSmartTagsPreventParsing" content="true" />
            <meta name="Keywords" content="_KEYWORDS_" />
            <meta name="Description" content="_DESCRIPTION_" />
            <link href="<%=request.getContextPath()%>/evaluation/resources/_css/reporting.css" rel="stylesheet" type="text/css" media="screen" />
            <link href="<%=request.getContextPath()%>/evaluation/resources/_css/reporting_print.css" rel="stylesheet" type="text/css" media="print" />        
     </head>
     
     <body>     
     <div id="wrap">
        <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="headerTD">
                        <div class="noprint">
                        <h1 class="logoh1">Pfizer</h1>
                        <h2 class="logoh2">Training Reports</h2>
                        <div> 
                        <div class="date"><strong>Today is:</strong> <%=dateFormatter1.format(new Date())%></div>
                        <div class="buttons">                                    
                        <img src="<%=request.getContextPath()%>/evaluation/resources/_img/buttons_print.gif" alt="Print" width="64" height="18" onclick="zoomScaleBeforePrint();return false;"/>
                        <img src="<%=request.getContextPath()%>/evaluation/resources/_img/b_download3.gif" alt="Download Blank Form" width="80" height="18" onclick="showDocument('Representative Feedback Form.pdf');"/>
                        </div>  
                        </div>
                        </div>
                        
                        <div class="print">
                        <table id="logo">
                            <tr>
                                <td style="text-align:left" width="40%"><img src="<%=request.getContextPath()%>/evaluation/resources/_img/pfizer_logo.gif" alt="" style="width:88px"/></td>
                                <td style="text-align:left" width="60%"><b></b></td>                                
                            </tr>
                        </table>
                        </div>
                    </td>
                </tr>
                
                  <tr>
               
                    <TD align=left valign="top" bgcolor="#FFFFFF" class="ContentPadding">
                        <!-- put the following div in a wrapper -->
                        <!-- set the wrapper witdh to control width of elements inside
                        <div class="page_content_wrapper">-->
                        <br>
                            <h2>Phase 1, 2 & 4 Representative Feedback Form</h2>
                            <div class="clear"><img src="<%=request.getContextPath()%>/evaluation/resources/images/spacer.gif"></div>
                        
                            <%--<table class="datagrid" style="width:850px;">--%>
                            <%--<table class="datagrid" style="width:600px;">--%>
                            </TD></tr>
                            <tr><td>
                          <table class="datagrid">
                            <tr>
                                <th colspan="4" align="left">EVALUATION FORM</th>
                            </tr>
                            
                            <tr>
                                <td width="15%" align="left"><strong>REPRESENTATIVE:</strong></td>
                                <td width="75%"><input name="repName" type="text" id="repName" style="width:175px;text-align:left;" value="<%=empName%>" readonly></td>
                                </tr>
                             </table>  </td></tr>
                            
                             </table>
                              <%String message= (String)session.getAttribute("successmessage"); 
                             %>
                             <%if(message!=null){%>
                             <label><h5><%=message%></h5></label>
                             <%}%>
    <%
        
        
        int Phase1 = 100;
        int Phase2 = 100;
        int Phase4 = 100;
        
        System.out.println("indsie");
        int size =  objSCECom.length;
        
        System.out.println("size"+size);
        for(int i=0;i<size;i++)
        {
            if(objSCECom[i].getPhaseNo().equalsIgnoreCase("1"))
            {
                System.out.println("i:::"+i);
                Phase1 = i;
            }
            if(objSCECom[i].getPhaseNo().equalsIgnoreCase("2"))
            {
                Phase2 = i;
            }
            if(objSCECom[i].getPhaseNo().equalsIgnoreCase("4"))
            {
                Phase4 = i;
            }
        }
     
    %>
    <form action="submitSCEEvaluate"  name="phase1" method="post"> 

        <table class="datagrid">
             <tr>
                <th colspan="4" align="left">Phase 1 </th>
            </tr>
        </table>
            
    
    <%if(Phase1<3)
        {
           phaseNo="1"; 
    %>
            
        
            <table class="datagrid">
                           
                <tr class="greybox">
                    <td width="12%" align="left"><b>Team Lead:</b>  </td>
                    <td width="40%" align="left"><b><%=util.isNull(objSCECom[Phase1].getSubmittedBY())%></b> </td>
                    <td width="15%" align="left"><b>Date Submitted:</b> </td> 
                    <td width="60%" align="left"><b><%=util.isNull(objSCECom[Phase1].getSubmittedDate())%></b></td>
                </tr>
            </table>
    
            <table class="datagrid">
                    <tr>
                    <td width="25%"><b>Entered by</b></td>
                    <td width="25%"><b>Date</b></td>
                    <td width="50%"><b>Comments</b></td></tr>
                        
            <% String Comment1=objSCECom[Phase1].getComment1();
                String Comment2=objSCECom[Phase1].getComment2();
                String Comment3=objSCECom[Phase1].getComment3();
                String EnteredBy1=objSCECom[Phase1].getEnteredBy1();
                String EnteredBy2=objSCECom[Phase1].getEnteredBy2();
                String EnteredBy3=objSCECom[Phase1].getEnteredBy3();
                String Date1=objSCECom[Phase1].getDate1();
                String Date2=objSCECom[Phase1].getDate2();
                String Date3=objSCECom[Phase1].getDate3();
                String SubmittedBy = objSCECom[Phase1].getSubmittedBY();
                System.out.println(SubmittedBy+"submitted this form");
                System.out.println(EnteredBy3);
                System.out.println("Is Admin in jsp"+objSCEin.getIsSAdmin());
                %>
                        <input type="hidden" name="enteredBy1" value='<%=EnteredBy1%>'/>
                        <input type="hidden" name="enteredBy2" value='<%=EnteredBy2%>'/>
                        <input type="hidden" name="enteredBy3" value='<%=EnteredBy3%>'/>
                        <input type="hidden" name="date1" value='<%=Date1%>'/>
                        <input type="hidden" name="date2" value='<%=Date2%>'/>
                        <input type="hidden" name="date3" value='<%=Date3%>'/>
                <%
        if(SubmittedBy != null && objSCEin.getIsSAdmin().equalsIgnoreCase("false"))
            {
            %>
            
                        <tr>
                        <td width="25%"><b><%=util.isNull(EnteredBy1)%></b></td>
                        <td width="25%"><%=util.isNull(Date1)%></td>
                        <td width="50%"><textarea class="comments" name="phase1_comment1" readonly><%=util.isNull(Comment1)%></textarea></td>
                        </tr>
                        
                        <tr>
                        <td width="25%"><b><%=util.isNull(EnteredBy2)%></b></td>
                        <td width="25%"><%=util.isNull(Date2)%></td>
                        <td width="50%"><textarea class="comments" name="phase1_comment2" readonly><%=util.isNull(Comment2)%></textarea></td>
                        </tr>
                        
                        <tr>
                        <td width="25%"><b><%=util.isNull(EnteredBy3)%></b></td>
                        <td width="25%"><%=util.isNull(Date3)%></td>
                        <td width="50%"><textarea class="comments" name="phase1_comment3" readonly><%=util.isNull(Comment3)%></textarea></td>
                        </tr>
                        </table>
                        
                        <table align="right">
                            <input type="submit" value="Save" name="phase1Save" disabled> 
                            <input type="submit" value="Submit" name="phase1Submit" disabled>                           
                        </table>
          <%}
       else
         {
            if (Comment1 !=null)
                            {
                                if((EnteredBy1!=null && EnteredBy1.equals(evalName)) || (objSCEin.getIsSAdmin().equalsIgnoreCase("true")))
                                {%>
                            <tr>
                            
                            <td width="25%"><b><%=util.isNull(EnteredBy1)%></b></td>
                            <td width="25%"><%=util.isNull(Date1)%></td>
                            <td width="55%"><textarea class="comments" name="comment1" onkeydown="limitText(comment1)" onkeyup="limitText(comment1)" ><%=util.isNull(objSCECom[Phase1].getComment1())%></textarea></td>
                            </tr>
                            <%                               
                                }
                                else{%>
                                <tr>                        
                                <td width="25%"><b><%=util.isNull(EnteredBy1)%></b></td>
                                <td width="25%"><%=util.isNull(Date1)%></td>
                                <td width="55%"><textarea class="comments" name="comment1" readonly><%=util.isNull(objSCECom[Phase1].getComment1())%></textarea></td>
                                </tr>
                        <%}
                        }                      
                        else
                        { %>
                            <tr>                        
                           <td width="25%"></td>
                            <td width="25%"></td>
                            <td width="55%"><textarea class="comments" name="comment1"  onkeydown="limitText(comment1)" onkeyup="limitText(comment1)" >Comments</textarea></td>
                            </tr>
                        <%}
                        if (Comment2 !=null)
                        {
                            if((EnteredBy2!=null && EnteredBy2.equals(evalName)) || (objSCEin.getIsSAdmin().equalsIgnoreCase("true")))
                            {%>
                        <tr>                      
                        <td width="25%"><b><%=util.isNull(EnteredBy2)%></b></td>
                        <td width="25%"><%=util.isNull(Date2)%></td>
                        <td width="55%"><textarea class="comments" name="comment2" onkeydown="limitText(comment2)" onkeyup="limitText(comment2)"><%=util.isNull(Comment2)%></textarea></td>
                        </tr>
                        <%                                
                           }
                            else{
                        %>
                        <tr>
                        
                        <td width="25%"><b><%=util.isNull(EnteredBy2)%></b></td>
                        <td width="25%"><%=util.isNull(Date2)%></td>
                        <td width="55%"><textarea class="comments" name="comment2" readonly><%=util.isNull(Comment2)%></textarea></td>
                        </tr>
                        <%}
                        }                      
                        else
                        {%>
                        <tr>
                        
                       <td width="25%"></td>
                        <td width="25%"></td>
                        <td width="55%"><textarea class="comments" name="comment2" onkeydown="limitText(comment2)" onkeyup="limitText(comment2)" >Comments</textarea></td>
                        </tr>
                        <%}
                         if (Comment3 !=null)
                        {
                            if((EnteredBy3!=null && EnteredBy3.equals(evalName)) || (objSCEin.getIsSAdmin().equalsIgnoreCase("true")))
                            {%>
                        <tr>
                        
                        <td width="25%"><b><%=util.isNull(EnteredBy3)%></b></td>
                        <td width="25%"><%=util.isNull(Date3)%></td>
                        <td width="55%"><textarea class="comments" name="comment3" onkeydown="limitText(comment3)" onkeyup="limitText(comment3)"><%=util.isNull(Comment3)%></textarea></td>
                        </tr>
                        </table>
                        <table align="right">
                            <%if(flag.equals("isSave"))
                            {%>
                            <input type="submit" value="Save" name="phase1Save" onclick="save()"> 
                            <input type="submit" value="Submit" name="phase1Submit" disabled onclick="return Submit1()">
                          <%}
                          else if(flag.equals("isSubmit"))
                            {%>                          
                               <input type="submit" value="Save" name="phase1Save" onclick="save()"> 
                              <input type="submit" value="Submit" name="phase1Submit" onclick="return Submit1()">
                          <%}
                        else
                         {%>
                              <input type="submit" value="Save" name="phase1Save" disabled onclick="save()"> 
                              <input type="submit" value="Submit" name="phase1Submit" disabled onclick="return Submit1()">
                          <%}%>
                        </table>
                        <%
                                
                            }
                            else{%>
                        <tr>
                        
                        <td width="25%"><b><%=util.isNull(EnteredBy3)%></b></td>
                        <td width="25%"><%=util.isNull(Date3)%></td>
                        <td width="55%"><textarea class="comments" name="comment3" readonly><%=util.isNull(Comment3)%></textarea></td>
                        </tr>
                        </table>
                        <table align="right">
                            <%if(flag.equals("isSave"))
                            {%>
                            <input type="submit" value="Save" name="phase1Save" onclick="save()"> 
                            <input type="submit" value="Submit" name="phase1Submit" disabled onclick="return Submit1()">
                          <%}
                          else if(flag.equals("isSubmit"))
                            {%>                          
                               <input type="submit" value="Save" name="phase1Save" onclick="save()"> 
                              <input type="submit" value="Submit" name="phase1Submit" onclick="return Submit1()">
                          <%}
                        else
                         {%>
                              <input type="submit" value="Save" name="phase1Save" disabled onclick="save()"> 
                              <input type="submit" value="Submit" name="phase1Submit" disabled onclick="return Submit1()">
                          <%}%>
                        </table>
                        <%}
                        }
                        else
                        {%>
                        <tr>
                        
                        <td width="25%"></td>
                        <td width="25%"></td>
                        <td width="55%"><textarea class="comments" name="comment3" onkeydown="limitText(comment3)" onkeyup="limitText(comment3)" >Comments</textarea></td>
                        </tr>
                        <table align="right">
                            <%if(flag.equals("isSave"))
                            {%>
                            <input type="submit" value="Save" name="phase1Save" onclick="save()"> 
                            <input type="submit" value="Submit" name="phase1Submit" disabled onclick="return Submit1()">
                          <%}
                          else if(flag.equals("isSubmit"))
                            {%>                          
                               <input type="submit" value="Save" name="phase1Save" onclick="save()"> 
                              <input type="submit" value="Submit" name="phase1Submit" onclick="return Submit1()">
                          <%}
                        else
                         {%>
                              <input type="submit" value="Save" name="phase1Save" disabled onclick="save()"> 
                              <input type="submit" value="Submit" name="phase1Submit" disabled onclick="return Submit1()">
                          <%}%>
                        </table>
                        <%}
                           
                        }
        }
        else
         { %>
                        <table class="datagrid">
                             <tr class="greybox">
                                <td width="12%" align="left"><b>Team Lead:</b>  </td>
                                <td width="40%" align="left"></td>
                                <td width="15%" align="left"><b>Date Submitted:</b> </td> 
                                <td width="60%" align="left"></td>
                            </tr>
                            </table>
                        <table class="datagrid">
                        <tr>
                        <td width="25%"></td>
                        <td width="25%"></td>
                        <td width="50%"><textarea class="comments" name="comment1" onkeydown="limitText(comment1)" onkeyup="limitText(comment1)">Comments</textarea></td>
                        </tr>
                        
                        <tr>
                        <td width="25%"></td>
                        <td width="25%"></td>
                        <td width="50%"><textarea class="comments" name="comment2" onkeydown="limitText(comment2)" onkeyup="limitText(comment2)">Comments</textarea></td>
                        </tr>
                        
                        <tr>
                        <td width="25%"></td>
                        <td width="25%"></td>
                        <td width="50%"><textarea class="comments" name="comment3" onkeydown="limitText(comment3)" onkeyup="limitText(comment3)">Comments</textarea></td>
                        </tr>
                        </table>
                       <table align="right">
                            <%if(flag.equals("isSave"))
                            {%>
                            <input type="submit" value="Save" name="phase1Save" onclick="save()"> 
                            <input type="submit" value="Submit" name="phase1Submit" disabled onclick="return Submit1()">
                          <%}
                          else if(flag.equals("isSubmit"))
                            {%>                          
                               <input type="submit" value="Save" name="phase1Save" onclick="save()"> 
                              <input type="submit" value="Submit" name="phase1Submit" onclick="return Submit1()">
                          <%}
                        else
                         {%>
                              <input type="submit" value="Save" name="phase1Save" disabled onclick="save()"> 
                              <input type="submit" value="Submit" name="phase1Submit" disabled onclick="return Submit1()">
                          <%}%>
                        </table>
                                                 
                    
        <%}%>
   
    
    <script language="javascript">
            function save(){
             document.phase1.submit();
            return true;
            }
            function Submit1() 
            {

                var confirmmessage = "Are you sure you want to continue?";
                
                if (confirm(confirmmessage)) {
                                document.getElementById('submitFlag1').value="Y";
                                document.phase1.submit();
                                return true;
                } 
                return false;
            }
     </script>
            <input type="hidden" name="activityId" value='<%=activityId%>'/>
            <input type="hidden" name="trackId" value='<%=trackId%>'/> 
            <input type="hidden" name="sceId" value='<%=sceId%>'/> 
            <input type="hidden" name="phaseNo" value='<%=1%>'/> 
            <input type="hidden" name="reprenstativeName" value='<%=repId%>'/> 
            <input type="hidden" name="evaluatorName" value='<%=evalName%>'/>
            <input type="hidden" id="submitFlag1" name="submitFlag" value='N'/>
            
    </form>
   </div>
   <br>
   <br>
   <div id="wrap">
       
    <form action="submitSCEEvaluate" name="phase2"> 

    <table class="datagrid">
         <tr>
            <th colspan="4" align="left">Phase 2</th>
        </tr>
    </table>
    
    <% // Start of Phase 2 section 
        if(Phase2<3)
        {
    %>
            <table class="datagrid">
                           
                <tr class="greybox">
                    <td width="12%" align="left"><b>Team Lead:</b>  </td>
                    <td width="40%" align="left"><b><%=util.isNull(objSCECom[Phase2].getSubmittedBY())%></b> </td>
                    <td width="15%" align="left"><b>Date Submitted:</b> </td> 
                    <td width="60%" align="left"><b><%=util.isNull(objSCECom[Phase2].getSubmittedDate())%></b></td>
                </tr>
            </table>
    
            <table class="datagrid">
                    <tr>
                    <td width="25%"><b>Entered by</b></td>
                    <td width="25%"><b>Date</b></td>
                    <td width="50%"><b>Comments</b></td></tr>
                        
            <% String Comment1=objSCECom[Phase2].getComment1();
                String Comment2=objSCECom[Phase2].getComment2();
                String Comment3=objSCECom[Phase2].getComment3();
                String EnteredBy1=objSCECom[Phase2].getEnteredBy1();
                String EnteredBy2=objSCECom[Phase2].getEnteredBy2();
                String EnteredBy3=objSCECom[Phase2].getEnteredBy3();
                String Date1=objSCECom[Phase2].getDate1();
                String Date2=objSCECom[Phase2].getDate2();
                String Date3=objSCECom[Phase2].getDate3();
                String SubmittedBy = objSCECom[Phase2].getSubmittedBY();
                System.out.println(SubmittedBy+"submitted this form");
                System.out.println(EnteredBy2);
                %>
                    <input type="hidden" name="enteredBy1" value='<%=EnteredBy1%>'/>
                    <input type="hidden" name="enteredBy2" value='<%=EnteredBy2%>'/>
                    <input type="hidden" name="enteredBy3" value='<%=EnteredBy3%>'/>
                    <input type="hidden" name="date1" value='<%=Date1%>'/>
                    <input type="hidden" name="date2" value='<%=Date2%>'/>
                    <input type="hidden" name="date3" value='<%=Date3%>'/>
                <%
        if(SubmittedBy != null && objSCEin.getIsSAdmin().equalsIgnoreCase("false"))
            {
            %>
            
                        <tr>
                        <td width="25%"><b><%=util.isNull(EnteredBy1)%></b></td>
                        <td width="25%"><%=util.isNull(Date1)%></td>
                        <td width="50%"><textarea class="comments" name="comment1" readonly><%=util.isNull(Comment1)%></textarea></td>
                        </tr>
                        
                        <tr>
                        <td width="25%"><b><%=util.isNull(EnteredBy2)%></b></td>
                        <td width="25%"><%=util.isNull(Date2)%></td>
                        <td width="50%"><textarea class="comments" name="comment2" readonly><%=util.isNull(Comment2)%></textarea></td>
                        </tr>
                        
                        <tr>
                        <td width="25%"><b><%=util.isNull(EnteredBy3)%></b></td>
                        <td width="25%"><%=util.isNull(Date3)%></td>
                        <td width="50%"><textarea class="comments" name="comment3" readonly><%=util.isNull(Comment3)%></textarea></td>
                        </tr>
                        </table>
                        
                        <table align="right">
                            <input type="submit" value="Save" name="phase2Save"  disabled> 
                            <input type="submit" value="Submit"  name="phase2Submit"  disabled>                            
                        </table>
          <%}
       else
         {
            if (Comment1 !=null)
                            {
                                if((EnteredBy1!=null && EnteredBy1.equals(evalName)) || (objSCEin.getIsSAdmin().equalsIgnoreCase("true")))
                                {%>
                            <tr>
                            
                            <td width="25%"><b><%=util.isNull(EnteredBy1)%></b></td>
                            <td width="25%"><%=util.isNull(Date1)%></td>
                            <td width="55%"><textarea class="comments" name="comment1" onkeydown="limitText(comment1)" onkeyup="limitText(comment1)"><%=util.isNull(Comment1)%></textarea></td>
                            </tr>
                            <%                               
                                }
                                else{%>
                                <tr>                        
                                <td width="25%"><b><%=util.isNull(EnteredBy1)%></b></td>
                                <td width="25%"><%=util.isNull(Date1)%></td>
                                <td width="55%"><textarea class="comments" name="comment1" readonly><%=util.isNull(Comment1)%></textarea></td>
                                </tr>
                        <%}
                        }                      
                        else
                        { %>
                            <tr>                        
                           <td width="25%"></td>
                            <td width="25%"></td>
                            <td width="55%"><textarea class="comments" name="comment1" onkeydown="limitText(comment1)" onkeyup="limitText(comment1)" >Comments</textarea></td>
                            </tr>
                        <%}
                        if (Comment2 !=null)
                        {
                            if((EnteredBy2!=null && EnteredBy2.equals(evalName)) || (objSCEin.getIsSAdmin().equalsIgnoreCase("true")))
                            {%>
                        <tr>                      
                        <td width="25%"><b><%=util.isNull(EnteredBy2)%></b></td>
                        <td width="25%"><%=util.isNull(Date2)%></td>
                        <td width="55%"><textarea class="comments" name="comment2" onkeydown="limitText(comment2)" onkeyup="limitText(comment2)"><%=util.isNull(Comment2)%></textarea></td>
                        </tr>
                        <%                                
                           }
                            else{
                        %>
                        <tr>
                        
                        <td width="25%"><b><%=util.isNull(EnteredBy2)%></b></td>
                        <td width="25%"><%=util.isNull(Date2)%></td>
                        <td width="55%"><textarea class="comments" name="comment2" readonly><%=util.isNull(Comment2)%></textarea></td>
                        </tr>
                        <%}
                        }                      
                        else
                        {%>
                        <tr>
                        
                       <td width="25%"></td>
                        <td width="25%"></td>
                        <td width="55%"><textarea class="comments" name="comment2" onkeydown="limitText(comment2)" onkeyup="limitText(comment2)">Comments</textarea></td>
                        </tr>
                        <%}
                         if (Comment3 !=null)
                        {
                            if((EnteredBy3!=null && EnteredBy3.equals(evalName))|| (objSCEin.getIsSAdmin().equalsIgnoreCase("true")))
                            {%>
                        <tr>
                        
                        <td width="25%"><b><%=util.isNull(EnteredBy3)%></b></td>
                        <td width="25%"><%=util.isNull(Date3)%></td>
                        <td width="55%"><textarea class="comments" name="comment3" onkeydown="limitText(comment3)" onkeyup="limitText(comment3)"><%=util.isNull(Comment3)%></textarea></td>
                        </tr>
                        </table>
                        <table align="right">
                            <%if(flag.equals("isSave"))
                            {%>
                             <input type="submit" value="Save" name="phase2Save" onclick="save2()"> 
                             <input type="submit" value="Submit"  name="phase2Submit" onclick="return Submit2()" disabled>                            
                        <%}
                          else if(flag.equals("isSubmit"))
                            {%>                          
                               <input type="submit" value="Save" name="phase2Save" onclick="save2()"> 
                               <input type="submit" value="Submit"  name="phase2Submit" onclick="return Submit2()">                            
                        <%}
                        else
                         {%>
                              <input type="submit" value="Save" name="phase2Save" onclick="save2()" disabled> 
                              <input type="submit" value="Submit"  name="phase2Submit" onclick="return Submit2()" disabled>                            
                         <%}%>
                        </table>
                        
                        <%
                                
                            }
                            else{%>
                        <tr>
                        
                        <td width="25%"><b><%=util.isNull(EnteredBy3)%></b></td>
                        <td width="25%"><%=util.isNull(Date3)%></td>
                        <td width="55%"><textarea class="comments" name="comment3" readonly><%=util.isNull(Comment3)%></textarea></td>
                        </tr>
                        </table>
                        <table align="right">
                            <%if(flag.equals("isSave"))
                            {%>
                             <input type="submit" value="Save" name="phase2Save" onclick="save2()"> 
                             <input type="submit" value="Submit"  name="phase2Submit" onclick="return Submit2()" disabled>                            
                        <%}
                          else if(flag.equals("isSubmit"))
                            {%>                          
                               <input type="submit" value="Save" name="phase2Save" onclick="save2()"> 
                               <input type="submit" value="Submit"  name="phase2Submit" onclick="return Submit2()">                            
                        <%}
                        else
                         {%>
                              <input type="submit" value="Save" name="phase2Save" onclick="save2()" disabled> 
                              <input type="submit" value="Submit"  name="phase2Submit" onclick="return Submit2()" disabled>                            
                         <%}%>
                        </table>
                        <%}
                        }
                        else
                        {%>
                        <tr>
                        
                        <td width="25%"></td>
                        <td width="25%"></td>
                        <td width="55%"><textarea class="comments" name="comment3" onkeydown="limitText(comment3)" onkeyup="limitText(comment3)" >Comments</textarea></td>
                        </tr>
                        </table>
                        <table align="right">
                            <%if(flag.equals("isSave"))
                            {%>
                             <input type="submit" value="Save" name="phase2Save" onclick="save2()"> 
                             <input type="submit" value="Submit"  name="phase2Submit" onclick="return Submit2()" disabled>                            
                        <%}
                          else if(flag.equals("isSubmit"))
                            {%>                          
                               <input type="submit" value="Save" name="phase2Save" onclick="save2()"> 
                               <input type="submit" value="Submit"  name="phase2Submit" onclick="return Submit2()">                            
                        <%}
                        else
                         {%>
                              <input type="submit" value="Save" name="phase2Save" onclick="save2()" disabled> 
                              <input type="submit" value="Submit"  name="phase2Submit" onclick="return Submit2()" disabled>                            
                         <%}%>
                        </table>
                        <%}
                           
                        }
        }
        else
         { %>
                        <table class="datagrid">
                             <tr class="greybox">
                                <td width="12%" align="left"><b>Team Lead:</b>  </td>
                                <td width="40%" align="left"></td>
                                <td width="15%" align="left"><b>Date Submitted:</b> </td> 
                                <td width="60%" align="left"></td>
                            </tr>
                            </table>
                        <table class="datagrid">
                        <tr>
                        <td width="25%"></td>
                        <td width="25%"></td>
                        <td width="50%"><textarea class="comments" name="comment1" onkeydown="limitText(comment1)" onkeyup="limitText(comment1)">Comments</textarea></td>
                        </tr>
                        
                        <tr>
                        <td width="25%"></td>
                        <td width="25%"></td>
                        <td width="50%"><textarea class="comments" name="comment2" onkeydown="limitText(comment2)" onkeyup="limitText(comment2)">Comments</textarea></td>
                        </tr>
                        
                        <tr>
                        <td width="25%"></td>
                        <td width="25%"></td>
                        <td width="50%"><textarea class="comments" name="comment3" onkeydown="limitText(comment3)" onkeyup="limitText(comment3)">Comments</textarea></td>
                        </tr>
                        </table>
                        <table align="right">
                            <%if(flag.equals("isSave"))
                            {%>
                             <input type="submit" value="Save" name="phase2Save" onclick="save2()"> 
                             <input type="submit" value="Submit"  name="phase2Submit" onclick="return Submit2()" disabled>                            
                        <%}
                          else if(flag.equals("isSubmit"))
                            {%>                          
                               <input type="submit" value="Save" name="phase2Save" onclick="save2()"> 
                               <input type="submit" value="Submit"  name="phase2Submit" onclick="return Submit2()">                            
                        <%}
                        else
                         {%>
                              <input type="submit" value="Save" name="phase2Save" onclick="save2()" disabled> 
                              <input type="submit" value="Submit"  name="phase2Submit" onclick="return Submit2()" disabled>                            
                         <%}%>
                        </table>
                        
                          
        
        <%}%>
       <script language="javascript">
            function save2(){
             document.phase2.submit();
            return true;
            }
            function Submit2() 
            {

                var confirmmessage = "Are you sure you want to continue?";
                
                if (confirm(confirmmessage)) {
                                document.getElementById('submitFlag2').value="Y";
                                document.phase2.submit();
                                return true;
                } 
                return false;
            }
     </script>
            <input type="hidden" name="activityId" value='<%=activityId%>'/> 
            <input type="hidden" name="trackId" value='<%=trackId%>'/>
            <input type="hidden" name="sceId" value='<%=sceId%>'/>
            <input type="hidden" name="phaseNo" value='<%=2%>'/> 
            <input type="hidden" name="reprenstativeName" value='<%=repId%>'/> 
            <input type="hidden" name="evaluatorName" value='<%=evalName%>'/>
            <input type="hidden" id="submitFlag2" name="submitFlag" value='N'/>
    </form>               
    </div>
     </table>
     
     <br>
   <br>
   <div id="wrap">
        <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
        </table>
    <form action="submitSCEEvaluate" name="phase4"> 

    <table class="datagrid">
         <tr>
            <th colspan="4" align="left">Phase 4</th>
        </tr>
    </table>
    
    <%if(Phase4<3)
        {
    %>
            <table class="datagrid">
                           
                <tr class="greybox">
                    <td width="12%" align="left"><b>Team Lead:</b>  </td>
                    <td width="40%" align="left"><b><%=util.isNull(objSCECom[Phase4].getSubmittedBY())%></b> </td>
                    <td width="15%" align="left"><b>Date Submitted:</b> </td> 
                    <td width="60%" align="left"><b><%=util.isNull(objSCECom[Phase4].getSubmittedDate())%></b></td>
                </tr>
            </table>
    
            <table class="datagrid">
                    <tr>
                    <td width="25%"><b>Entered by</b></td>
                    <td width="25%"><b>Date</b></td>
                    <td width="50%"><b>Comments</b></td></tr>
                        
            <% String Comment1=objSCECom[Phase4].getComment1();
                String Comment2=objSCECom[Phase4].getComment2();
                String Comment3=objSCECom[Phase4].getComment3();
                String EnteredBy1=objSCECom[Phase4].getEnteredBy1();
                String EnteredBy2=objSCECom[Phase4].getEnteredBy2();
                String EnteredBy3=objSCECom[Phase4].getEnteredBy3();
                String Date1=objSCECom[Phase4].getDate1();
                String Date2=objSCECom[Phase4].getDate2();
                String Date3=objSCECom[Phase4].getDate3();
                String SubmittedBy = objSCECom[Phase4].getSubmittedBY();
                System.out.println(SubmittedBy+"submitted this form");
                System.out.println(EnteredBy2);
                %>
                    <input type="hidden" name="enteredBy1" value='<%=EnteredBy1%>'/>
                    <input type="hidden" name="enteredBy2" value='<%=EnteredBy2%>'/>
                    <input type="hidden" name="enteredBy3" value='<%=EnteredBy3%>'/>
                    <input type="hidden" name="date1" value='<%=Date1%>'/>
                    <input type="hidden" name="date2" value='<%=Date2%>'/>
                    <input type="hidden" name="date3" value='<%=Date3%>'/>
                <%
        if(SubmittedBy != null && objSCEin.getIsSAdmin().equalsIgnoreCase("false"))
            {
            %>
            
                        <tr>
                        <td width="25%"><b><%=util.isNull(EnteredBy1)%></b></td>
                        <td width="25%"><%=util.isNull(Date1)%></td>
                        <td width="50%"><textarea class="comments" name="comment1" readonly><%=util.isNull(Comment1)%></textarea></td>
                        </tr>
                        
                        <tr>
                        <td width="25%"><b><%=util.isNull(EnteredBy2)%></b></td>
                        <td width="25%"><%=util.isNull(Date2)%></td>
                        <td width="50%"><textarea class="comments" name="comment2" readonly><%=util.isNull(Comment2)%></textarea></td>
                        </tr>
                        
                        <tr>
                        <td width="25%"><b><%=util.isNull(EnteredBy3)%></b></td>
                        <td width="25%"><%=util.isNull(Date3)%></td>
                        <td width="50%"><textarea class="comments" name="comment3" readonly><%=util.isNull(Comment3)%></textarea></td>
                        </tr>
                        </table>
                        <table align="right">
                            <input type="submit" value="Save"  name="phase4Save"  disabled> 
                            <input type="submit" value="Submit"  name="phase4Submit" disabled>                            
                        </table>
          <%}
       else
         {
            if (Comment1 !=null)
                            {
                                if((EnteredBy1!=null && EnteredBy1.equals(evalName)) || (objSCEin.getIsSAdmin().equalsIgnoreCase("true")))
                                {%>
                            <tr>
                            
                            <td width="25%"><b><%=util.isNull(EnteredBy1)%></b></td>
                            <td width="25%"><%=util.isNull(Date1)%></td>
                            <td width="55%"><textarea class="comments" name="comment1" onkeydown="limitText(comment1)" onkeyup="limitText(comment1)"><%=util.isNull(Comment1)%></textarea></td>
                            </tr>
                            <%                               
                                }
                                else{%>
                                <tr>                        
                                <td width="25%"><b><%=util.isNull(EnteredBy1)%></b></td>
                                <td width="25%"><%=util.isNull(Date1)%></td>
                                <td width="55%"><textarea class="comments" name="comment1" readonly><%=util.isNull(Comment1)%></textarea></td>
                                </tr>
                        <%}
                        }                      
                        else
                        { %>
                            <tr>                        
                           <td width="25%"></td>
                            <td width="25%"></td>
                            <td width="55%"><textarea class="comments" name="comment1" onkeydown="limitText(comment1)" onkeyup="limitText(comment1)" >Comments</textarea></td>
                            </tr>
                        <%}
                        if (Comment2 !=null)
                        {
                            if((EnteredBy2!=null && EnteredBy2.equals(evalName)) || (objSCEin.getIsSAdmin().equalsIgnoreCase("true")))
                            {%>
                        <tr>                      
                        <td width="25%"><b><%=util.isNull(EnteredBy2)%></b></td>
                        <td width="25%"><%=util.isNull(Date2)%></td>
                        <td width="55%"><textarea class="comments" name="comment2" onkeydown="limitText(comment2)" onkeyup="limitText(comment2)"><%=util.isNull(Comment2)%></textarea></td>
                        </tr>
                        <%                                
                           }
                            else{
                        %>
                        <tr>
                        
                        <td width="25%"><b><%=util.isNull(EnteredBy2)%></b></td>
                        <td width="25%"><%=util.isNull(Date2)%></td>
                        <td width="55%"><textarea class="comments" name="comment2" readonly><%=util.isNull(Comment2)%></textarea></td>
                        </tr>
                        <%}
                        }                      
                        else
                        {%>
                        <tr>
                        
                       <td width="25%"></td>
                        <td width="25%"></td>
                        <td width="55%"><textarea class="comments" name="comment2" onkeydown="limitText(comment2)" onkeyup="limitText(comment2)" >Comments</textarea></td>
                        </tr>
                        <%}
                         if (Comment3 !=null)
                        {
                            if((EnteredBy3!=null && EnteredBy3.equals(evalName)) || (objSCEin.getIsSAdmin().equalsIgnoreCase("true")))
                            {%>
                        <tr>
                        
                        <td width="25%"><b><%=util.isNull(EnteredBy3)%></b></td>
                        <td width="25%"><%=util.isNull(Date3)%></td>
                        <td width="55%"><textarea class="comments" name="comment3" onkeydown="limitText(comment3)" onkeyup="limitText(comment3)"><%=util.isNull(Comment3)%></textarea></td>
                        </tr>
                        </table>
                        <table align="right">
                            <%if(flag.equals("isSave"))
                            {%>
                               <input type="submit" value="Save" name="phase4Save" onclick="save4()"> 
                               <input type="submit" value="Submit"  name="phase4Submit" onclick="return Submit4()" disabled>                            
                        <%}
                          else if(flag.equals("isSubmit"))
                            {%>                          
                               <input type="submit" value="Save" name="phase4Save" onclick="save4()"> 
                               <input type="submit" value="Submit"  name="phase4Submit" onclick="return Submit4()">                            
                        <%}
                        else
                         {%>
                               <input type="submit" value="Save" name="phase4Save" onclick="save4()" disabled> 
                               <input type="submit" value="Submit"  name="phase4Submit" onclick="return Submit4()" disabled>                            
                        <%}%>
                        </table>
                        
                        <%
                                
                            }
                            else{%>
                        <tr>
                        
                        <td width="25%"><b><%=util.isNull(EnteredBy3)%></b></td>
                        <td width="25%"><%=util.isNull(Date3)%></td>
                        <td width="55%"><textarea class="comments" name="comment3" readonly><%=util.isNull(Comment3)%></textarea></td>
                        </tr>
                        </table>
                         <table align="right">
                            <%if(flag.equals("isSave"))
                            {%>
                               <input type="submit" value="Save" name="phase4Save" onclick="save4()"> 
                               <input type="submit" value="Submit"  name="phase4Submit" onclick="return Submit4()" disabled>                            
                        <%}
                          else if(flag.equals("isSubmit"))
                            {%>                          
                               <input type="submit" value="Save" name="phase4Save" onclick="save4()"> 
                               <input type="submit" value="Submit"  name="phase4Submit" onclick="return Submit4()">                            
                        <%}
                        else
                         {%>
                               <input type="submit" value="Save" name="phase4Save" onclick="save4()" disabled> 
                               <input type="submit" value="Submit"  name="phase4Submit" onclick="return Submit4()" disabled>                            
                        <%}%>
                        </table>
                        <%}
                        }
                        else
                        {%>
                        <tr>
                        
                        <td width="25%"></td>
                        <td width="25%"></td>
                        <td width="55%"><textarea class="comments" name="comment3" onkeydown="limitText(comment3)" onkeyup="limitText(comment3)" >Comments</textarea></td>
                        </tr>
                        </table>
                        <table align="right">
                            <%if(flag.equals("isSave"))
                            {%>
                               <input type="submit" value="Save" name="phase4Save" onclick="save4()"> 
                               <input type="submit" value="Submit"  name="phase4Submit" onclick="return Submit4()" disabled>                            
                        <%}
                          else if(flag.equals("isSubmit"))
                            {%>                          
                               <input type="submit" value="Save" name="phase4Save" onclick="save4()"> 
                               <input type="submit" value="Submit"  name="phase4Submit" onclick="return Submit4()">                            
                        <%}
                        else
                         {%>
                               <input type="submit" value="Save" name="phase4Save" onclick="save4()" disabled> 
                               <input type="submit" value="Submit"  name="phase4Submit" onclick="return Submit4()" disabled>                            
                        <%}%>
                        </table>
                        <%}
                           
                        }
        }
        else
         { %>
                        <table class="datagrid">
                             <tr class="greybox">
                                <td width="12%" align="left"><b>Team Lead:</b></td>
                                <td width="40%" align="left"></td>
                                <td width="15%" align="left"><b>Date Submitted:</b> </td> 
                                <td width="60%" align="left"></td>
                            </tr>
                            </table>
                        <table class="datagrid">
                        <tr>
                        <td width="25%"></td>
                        <td width="25%"></td>
                        <td width="50%"><textarea class="comments" name="comment1" onkeydown="limitText(comment1)" onkeyup="limitText(comment1)">Comments</textarea></td>
                        </tr>
                        
                        <tr>
                        <td width="25%"></td>
                        <td width="25%"></td>
                        <td width="50%"><textarea class="comments" name="comment2" onkeydown="limitText(comment2)" onkeyup="limitText(comment2)">Comments</textarea></td>
                        </tr>
                        
                        <tr>
                        <td width="25%"></td>
                        <td width="25%"></td>
                        <td width="50%"><textarea class="comments" name="comment3" onkeydown="limitText(comment3)" onkeyup="limitText(comment3)">Comments</textarea></td>
                        </tr>
                        </table>
                          <table align="right">
                            <%if(flag.equals("isSave"))
                            {%>
                               <input type="submit" value="Save" name="phase4Save" onclick="save4()"> 
                               <input type="submit" value="Submit"  name="phase4Submit" onclick="return Submit4()" disabled>                            
                        <%}
                          else if(flag.equals("isSubmit"))
                            {%>                          
                               <input type="submit" value="Save" name="phase4Save" onclick="save4()"> 
                               <input type="submit" value="Submit"  name="phase4Submit" onclick="return Submit4()">                            
                        <%}
                        else
                         {%>
                               <input type="submit" value="Save" name="phase4Save" onclick="save4()" disabled> 
                               <input type="submit" value="Submit"  name="phase4Submit" onclick="return Submit4()" disabled>                            
                        <%}%>
                        </table>
                    
        <%}%>
      <script language="javascript">
            function save4(){
             document.phase4.submit();
            return true;
            }
            function Submit4() 
            {

                var confirmmessage = "Are you sure you want to continue?";
                
                if (confirm(confirmmessage)) {
                                document.getElementById('submitFlag4').value="Y";
                                document.phase4.submit();
                                return true;
                } 
                return false;
            }
     </script>
            <input type="hidden" name="activityId" value='<%=activityId%>'/> 
            <input type="hidden" name="trackId" value='<%=trackId%>'/>
            <input type="hidden" name="sceId" value='<%=sceId%>'/>
            <input type="hidden" name="phaseNo" value='<%=4%>'/> 
            <input type="hidden" name="reprenstativeName" value='<%=repId%>'/> 
            <input type="hidden" name="evaluatorName" value='<%=evalName%>'/>
            <input type="hidden"  id="submitFlag4" name="submitFlag" value='N'/>
    </form>               
    </div>
     </table>
     
       <table align="center">
                            <tr>
                                <td>
                                <br>
                                <br>
                                <input type="submit" value="Close" onclick="window.close();return false;">      
                                </td>
                            </tr>
                        </table> 
                        
                        
 </body>
 </html>
<%
    request.removeAttribute("sceToDisplay");  
%>