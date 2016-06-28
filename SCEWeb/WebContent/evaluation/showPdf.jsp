<%@ page language="java" contentType="application/pdf;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@ page import="java.sql.Blob"%>

<!--
	The database control returns the image data as a java.sql.Blob object.
	Convert the Blob into a byte[] and place the byte[] on the request object.
-->

<%
//Blob blob = (Blob) request.getAttribute("blob");
Integer sceid=(Integer)session.getAttribute("sceID");
SCEManagerImpl sceManager = new SCEManagerImpl();
 Blob blob =sceManager.getByteArray(sceid);
//Blob blob = (Blob) request.getAttribute("blob");
byte[] rgb = blob.getBytes(1, (int) blob.length());
request.setAttribute("byArr", rgb);
%>

<jsp:forward page="/showPdf" />


