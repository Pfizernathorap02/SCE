<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.beans.TemplateVersion"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp"%>


<%--
	
 	HashMap statuses = SCEUtils.getFormStatuses();
	List<String> statusList = new ArrayList<String>();
	Iterator<Map.Entry<String,String>> m = statuses.entrySet().iterator();
	while(m.hasNext()){
		String key = m.next().getKey();
		System.out.println(key+","+statuses.get(key));
		String formValue =(String) statuses.get(key);
		statusList.add(formValue);
	}
	
	System.out.println("Values in the list:"+statusList);
--%>
<%
    String message = (String)request.getAttribute("message");

	TemplateVersion[] templateVersions = (TemplateVersion[])request.getAttribute("templateVersions"); 
    TemplateVersion templateVersion = (TemplateVersion)request.getAttribute("templateVersion"); 
    
    String formStatus = (String)request.getAttribute("formStatus");
    System.out.println("****************Form Status:"+formStatus); 
    System.out.println("****************TemplateVersion length:"+templateVersions.length); 
    
    // System.out.println("no of records::"+templateVersions.length);
    //// System.out.println("templateVersions.length = "+templateVersions.length);
    
   // Integer publishedTemplateVersion= new Integer((String)request.getAttribute("publishedTemplateVersion"));

%>

<script language="javascript">
    
    
       function downloadBlankForm(templateName,templateVersionId) {
      
     // alert("template name is::"+templateName);
        window.document.getElementById('selTemplateName').value = templateName;
       // alert(window.document.getElementById('selTemplateName').value);
       // alert("template Version id is::"+templateVersionId);
         window.document.getElementById('selTemplateVersionId').value = templateVersionId;
         // alert(window.document.getElementById('selTemplateVersionId').value);
         // alert("cherry");
        window.document.forms[0].action="downloadBlankForm";                
     //  alert("bomb");
        window.document.forms[0].submit();        
    }
       
       
       function hideMapping(id,name) 
   	{

   		if (confirm("Hide " + name + " !") == true)
   		{
   			//alert(id);
   			document.getElementById("toHideTemplateId").value = id;
   			document.getElementById("toHideTemplateName").value=name;
   			
   			window.document.forms[0].action="goToHideTemplate";                
   	    
   	        window.document.forms[0].submit(); 

   		}

   	}
 
       function unhideMapping(id,name) 
      	{
    	   //alert("in unhide mapping");

      		if (confirm("Unhide " + name + " !") == true)
      		{
      			//alert(id);
      			document.getElementById("toUnHideTemplateId").value = id;
      			document.getElementById("toUnHideTemplateName").value=name;
      			
      			window.document.forms[0].action="goToUnHideTemplate";                
      	    
      	        window.document.forms[0].submit(); 

      		}

      	}      
       

       function selectFormStatus() {
    	    
         	var statusValue = document.getElementById('formStatus').value;
           
           //alert(statusValue);
           
           window.document.getElementById('selFormStatus').value = statusValue;
           if (statusValue != '') {
           	//alert('in if status value');
               window.document.forms[0].action = "selectFormStatus"; 
              window.document.forms[0].submit();
           	
           }
           else {
           	//alert('in else status value');
           	return false;
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
<body id="p_template_maintenance" class="admin">


	<div id="wrap">

		<div id="top_head">
			<h1>Pfizer</h1>
			<h2>Sales Call Evaluation</h2>

			<%@include file="navbar.jsp"%>
			<!-- end #top_head -->
		</div>

		<%  
                session.getAttribute("admin");
				session.getAttribute("administrator");
               System.out.println("************************************check if user is really a admininstrator ::"+administrator);
                if (admin=="admin") {
                %>
		<div id="eval_sub_nav">

			<s:url action="admin" var="adminUrl"></s:url>
			<s:a href="%{adminUrl}">
				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoadmin.gif"
					alt="Back to main admin" width="119" height="18" />
			</s:a>
		</div>
		<%}%>


		<h3>Print Blank Form</h3>

		<div id="main_content">

			<s:form action="" id="allTemplatesForm">

				<%
						if (message != null) 
						{
					%>
				<div>
					<label><font color="#3355ff"><%=message%></font></label>
				</div>

				<%
						}
					%>
				<br />
				<!--ADD NEW EVALUATION BUTTON HAS TO COME HERE -->

				<input type="hidden" id="selTemplateVersionId"
					name="selTemplateVersionId" value="3" />
				<input type="hidden" id="selTemplateName" name="selTemplateName"
					value="fft" />
				<div>
					<br> Click on 'Download' to open and print a form
				</div>
				<br> <s:hidden name="selFormStatus" id="selFormStatus" /> <%--            Form Status : <s:select list="%{statuses}" name="formStatus" onchange="return selectFormStatus()" id="formStatus"></s:select>        
 --%>
				<%
					if (administrator == "administrator") {
				%>
  					Form Status : <s:select list="statuses" name="status"
					onchange="return selectFormStatus()" id="formStatus"></s:select>
				<%} %>

				<%--        	 Form Status : <s:select list="#{'Visible':'Visible', 'Hidden':'Hidden'}" name="formStatus" onchange="return selectFormStatus()" id="formStatus"></s:select>  
 --%> <%if(15<templateVersions.length){%>
					<div
						STYLE="height: 354px; width: 800px; font-size: 12px; overflow-x: hidden; overflow-y: scroll;"
						align="center">
						<%}else{%>
						<div
							STYLE="height: 354px; width: 800px; font-size: 12px; overflow-x: hidden; overflow-y: auto;"
							align="center">
							<%}%>


							<table cellspacing="0" width="60%" align="center">
								<tr>
									<th>Evaluation Form Name</th>
									<th>Template Id</th>
									<th>Version</th>
									<%if (administrator=="administrator") { %>
									<th>Status : <%=formStatus %></th>
									<%} %>
									<th class="last"></th>
								</tr>
								<%if(templateVersion!=null){%>
								<tr>
									<td style="overflow-wrap:anywhere;"><%=SCEUtils.ifNull(templateVersion.getFormTitle(),"&nbsp;")%>
										<td><%=templateVersion.getTemplateId()%></td>
										<td><%=templateVersion.getVersion()%></td> <%if (administrator=="administrator") { %>
										<%if(formStatus.equalsIgnoreCase("Visible")) {%>
										<td width="25%"><input type="hidden"
											id="toHideTemplateId" name="toHideTemplateId" value="">
												<input type="hidden" id="toHideTemplateName"
												name="toHideTemplateName" value="">
													<div style="text-align: center;">
														<button
															onclick="hideMapping('<%=templateVersion.getTemplateId()%>','<%=templateVersion.getFormTitle()%>')">Hide</button>
													</div></td> <%}else{ %>
										<td width="25%"><input type="hidden"
											id="toUnHideTemplateId" name="toUnHideTemplateId" value="">
												<input type="hidden" id="toUnHideTemplateName"
												name="toUnHideTemplateName" value="">
													<div style="text-align: center;">
														<button
															onclick="unhideMapping('<%=templateVersion.getTemplateId()%>','<%=templateVersion.getFormTitle()%>')">Show</button>
													</div></td> <%}} %>


										<td class="last1">
											<div>
												<a href="#"
													onClick="downloadBlankForm('<%=templateVersion.getTemplateName()%>',<%=templateVersion.getId()%>)"><u>Download</u></a>
												<%--<netui:anchor action="downloadBlankForm" formSubmit="true" onClick="downloadBlankForm('<%=objTemplateVersion.getTemplateName()%>',<%=objTemplateVersion.getId()%>)"><u>Download</u>  </netui:anchor>--%>

											</div>
									</td>
								</tr>
								<%}%>
								<%
                    TemplateVersion objTemplateVersion = null;
                    if (templateVersions != null) {
                        for (int i=0; i<templateVersions.length; i++) {                                 
                            Integer maxVersion=null; 
                            objTemplateVersion = templateVersions[i];                                                 
                            if (objTemplateVersion.getTemplateId().intValue() < 5 || objTemplateVersion.getTemplateId().intValue() > 7) {                                                   
                    %>
								<tr>
									<td><%=SCEUtils.ifNull(objTemplateVersion.getTemplateName(),"&nbsp;")%>
										<td><%=objTemplateVersion.getTemplateId()%></td>
										<td><%=objTemplateVersion.getVersion()%></td> <%if (administrator=="administrator") { %>
										<%if(formStatus.equalsIgnoreCase("Visible")) {%>
										<td width="25%"><input type="hidden"
											id="toHideTemplateId" name="toHideTemplateId" value="">
												<input type="hidden" id="toHideTemplateName"
												name="toHideTemplateName" value="">
													<div style="text-align: center;">
														<button
															onclick="hideMapping('<%=objTemplateVersion.getTemplateId()%>','<%=objTemplateVersion.getTemplateName()%>')">Hide</button>
													</div></td> <%}else{ %>
										<td width="25%"><input type="hidden"
											id="toUnHideTemplateId" name="toUnHideTemplateId" value="">
												<input type="hidden" id="toUnHideTemplateName"
												name="toUnHideTemplateName" value="">
													<div style="text-align: center;">
														<button
															onclick="unhideMapping('<%=objTemplateVersion.getTemplateId()%>','<%=objTemplateVersion.getTemplateName()%>')">Show</button>
													</div></td> <%} } %>



										<td class="last1">
											<div>
												<a href="#"
													onClick="downloadBlankForm('<%=objTemplateVersion.getTemplateName()%>',<%=objTemplateVersion.getId()%>)"><u>Download</u></a>
												<%--<netui:anchor action="downloadBlankForm" formSubmit="true" onClick="downloadBlankForm('<%=objTemplateVersion.getTemplateName()%>',<%=objTemplateVersion.getId()%>)"><u>Download</u>  </netui:anchor>--%>


											</div>




									</td>
								</tr>
								<%
                            }
                        }
                      //  // System.out.println(":::::::::::::IN THE JSP::::::::::::::::::");
                       
                    }
                    %>
							</table>
						</div>
			</s:form>


			<div class="clear"></div>
		</div>
		<!-- end #content -->

	</div>
	<!-- end #wrap -->

</body>
</html>