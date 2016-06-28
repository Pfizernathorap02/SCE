<%@ page language="java" contentType="application/pdf;charset=UTF-8"%>

<%@ page import="java.sql.Blob"%>
<%@ page import="com.pfizer.sce.db.SCEManagerImpl"%>


               <!--
The database control returns the image data as a java.sql.Blob object.
Convert the Blob into a byte[] and place the byte[] on the request object.
-->
<%
//Blob blob = (Blob) request.getAttribute("blob");
String templateName=(String)request.getAttribute("templateName");

// System.out.println("templateName is :::"+templateName);
Integer templateVersionId=new Integer (request.getAttribute("templateVersionId").toString());

// System.out.println("templateVersionId is :::"+templateVersionId);

//int templateVersionId=new Integer(request.getAttribute("templateVersionId")).intValue();
SCEManagerImpl sceManager = new SCEManagerImpl();
Blob blob =sceManager.getBlankForm(templateVersionId);
//Blob blob = (Blob) request.getAttribute("blob");
byte[] rgb = blob.getBytes(1, (int) blob.length());
request.setAttribute("byArr", rgb);
%>

<jsp:forward page="/showBlankPDF" />


