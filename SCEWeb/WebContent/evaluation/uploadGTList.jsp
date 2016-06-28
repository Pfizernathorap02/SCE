<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.beans.Events"%>
<%@ page import="com.pfizer.sce.beans.GuestTrainer"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp" %>


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
        
        <!--[if IE 6]>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" />
        <![endif]-->
        <script language="javascript">
        function download()
        {
        document.forms[1].action="downloadGTTemplate";
            document.forms[1].submit();
        }
        
        function checkFilePathAndProduct()
        {
            var product=document.getElementById("uploadGTProduct").value;
            if(product =="select"){
                alert("Please Select Product");
                return false;
            }
            var file=document.getElementById("Uploadfile");
                var len=file.value.length;    
                var ext=file.value;  
                 if(len<=0){
                alert("Please choose a file");
                return false;
            }
             if(ext.substr(len-3,len)!="xls"){
            alert("Please select file with extension .xls");
            return false;
            }
             document.getElementById("product").value=product;
            document.forms[0].submit();
        }

        </script>
        <style>
       .uploadGT table{border:0px}
       .uploadGT td{border:0px}
        </style>
        
        <style >
        #uploadGTProduct {
    left: 0px;
    position: relative;
    top: 0px;
}
        </style>
    </head>
    <%
  
    String[] products=null;
    if(request.getAttribute("products")!=null){
        products=(String[])request.getAttribute("products");
    }
    String selectedProduct=null;
     if(request.getAttribute("product")!=null){
        selectedProduct=(String)request.getAttribute("product");
    }
    String message=null;
    if(request.getAttribute("message")!=null){
        message=(String)request.getAttribute("message");
    }
%>
    <body id="p_inputandmaintenance" class="inputandmaintenance">
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
        
            <h3>Admin: Upload List Of Guest Trainers</h3>
             <!-- end #content -->
             <div id="main_content">
            <%if(message!=null){%>
            <font color="Red">
            <p><%=message%></p>
            </font>
            <%}%>
            <s:form action="gotoUploadGTList" method="post" enctype="multipart/form-data" >
            <div class="uploadGT">
            <table>
                <tr>
                    <td width="100">Product</td>
                    <td>
                    <select name="uploadGTProduct" id="uploadGTProduct">
                    <option value="select">--Select--</option>
                    <%  if(products!=null){
                    for(int i=0;i<products.length;i++){
                        if(selectedProduct!=null&&selectedProduct.equalsIgnoreCase(products[i])){
                    %>
                    <option value="<%=selectedProduct%>" selected><%=selectedProduct%></option>
                    <%}else{%>
                    <option value="<%=products[i]%>"><%=products[i]%></option>
                    <%}}}%>
                    </select>
                    </td>
                </tr>
                <tr>
                    <td>Upload File</td>
                    <td>
                    <s:file  accept="application/vnd.ms-excel" id="Uploadfile" size="10000" style="width:300px;" name="addGuestTrainerForm.uploadGTList" />
                    <!--<input type="file" name="uploadGTList" id="uploadGTList" style="width:300px;">-->
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
              <td></td>     
                    <td>
                        <img src="<%=request.getContextPath()%>/evaluation/resources/_img/Upload.gif" alt="Upload" width="47" height="19" onclick="checkFilePathAndProduct()" id="uploadbutton"/>
                    </td>
                </tr>
            </table>
            </div>
            <input type="hidden" id="product" name="product" value=""/>
            </s:form>
            <div class="uploadGT">
            <s:form action="downloadGTTemplate">
            <div style="position:relative;top:-65px;left:40px"> 
            <table style="border:0px">
            <tr>
                    <td style="border:0px"/>
                    <td style="border:0px">
                        <s:a href="#" onClick="download()">Download Template</s:a>     <%-- Submit replaced by download --%>
                         
                    </td>
                </tr>
            </table>
            </div>
            </s:form>
            </div>
              <div class="clear"></div>
             </div>
        </div><!-- end #wrap -->
    </body>
    </html>