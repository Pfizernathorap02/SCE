<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib prefix="s" uri="/struts-tags"%>
	<%@include file="IAM_User_Auth.jsp" %>
    
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 -->
 
 <!--  Doctype added for HTML5 Struts 2.3 to 2.5 migration work by : Dhananjay -->
 <!DOCTYPE html>
 <html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 -->    
 <!--  Meta type added for HTML5 Struts 2.3 to 2.5 migration work by : Dhananjay -->   
 <meta charset="utf-8">
 
  <meta http-equiv="Content-Language" content="en-us" />
        <title>Pfizer Sales Call Evaluation</title>
        <meta name="ROBOTS" content="ALL" />
        <meta http-equiv="imagetoolbar" content="no" />
        <meta name="MSSmartTagsPreventParsing" content="true" />
        <meta name="Keywords" content="_KEYWORDS_" />
        <meta name="Description" content="_DESCRIPTION_" />
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css" rel="stylesheet" type="text/css" media="all" />
   
    
      
        <link href="<%=request.getContextPath()%>/evaluation/resources/_css/admin_updateUser.css" rel="stylesheet" type="text/css" media="all" />
          
          
    <script>

	function CloseMe() 
	{
		window.open('javascript:window.open("", "_self", "");window.close();', '_self');
	}
	</script> 

<title>Sales Call Evaluation</title>


</head>
<body>

<div id="wrap">
        
            <div id="top_head">
                <h1>Pfizer</h1>
                <h2>Sales Call Evaluation</h2>
                
             
            
            </div>
      <div id="eval_sub_nav">
               <s:a action="admin" ><img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoadmin.gif" alt="Back to main admin" width="119" height="18" /></s:a>
            </div>
          
          
</div>            

</body>
</html>