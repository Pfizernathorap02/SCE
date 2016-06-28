<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.sce.beans.Events"%>
<%@ page import="com.pfizer.sce.beans.GuestTrainer"%>
<%@ page import="com.pfizer.sce.utils.SCEUtils"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="IAM_User_Auth.jsp"%>
<%
	GuestTrainer[] trainers = null;
	int totalGts = 0;
	int totalFilteredGts = 0;
	if (session.getAttribute("gtByProduct") != null) {
		trainers = (GuestTrainer[]) session.getAttribute("gtByProduct");
		totalGts = trainers.length;
		totalFilteredGts = totalGts;
	}
	GuestTrainer[] filteredGT = null;

	if (request.getAttribute("filteredGT") != null) {
		filteredGT = (GuestTrainer[]) request
				.getAttribute("filteredGT");
		// System.out.println("Filtered List"+filteredGT.length);
		totalFilteredGts = filteredGT.length;
		trainers = filteredGT;
	}
	String[] products = null;
	if (session.getAttribute("products") != null) {
		products = (String[]) session.getAttribute("products");
	}
	String selectedProduct = null;
	if (request.getAttribute("product") != null) {
		selectedProduct = (String) request.getAttribute("product");
	}
	String invalidFile = null;
	if (request.getAttribute("invalidFile") != null) {
		invalidFile = (String) request.getAttribute("invalidFile");
	}
	String message = null;
	if (request.getAttribute("message") != null) {
		message = (String) request.getAttribute("message");
	}
	String[] gtToRemove = null;
	if (request.getAttribute("gtToRemove") != null) {
		gtToRemove = (String[]) request.getAttribute("gtToRemove");
	}
	String lastName = "";
	String location = "";
	String role = "";
	String toggle = "";

	if (request.getAttribute("lastName") != null) {
		lastName = (String) request.getAttribute("lastName");
	}
	if (request.getAttribute("location") != null) {
		location = (String) request.getAttribute("location");
	}
	if (request.getAttribute("role") != null) {
		role = (String) request.getAttribute("role");
	}
	if (request.getAttribute("toggle") != null) {
		toggle = (String) request.getAttribute("toggle");
	}
	String[] locArr = null;
	if (session.getAttribute("locationArr") != null) {
		locArr = (String[]) session.getAttribute("locationArr");
	}
	// System.out.println("toggle"+toggle);
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
<link
	href="<%=request.getContextPath()%>/evaluation/resources/_css/content.css"
	rel="stylesheet" type="text/css" media="all" />
<link
	href="<%=request.getContextPath()%>/evaluation/resources/_css/admin.css"
	rel="stylesheet" type="text/css" media="all" />

<!--[if IE 6]>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" />
        <![endif]-->
<!--[if IE 8]>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-8.0.css" />
        <![endif]-->
<!-- Sorting begin sanjeev-->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/evaluation/resources/js/sorttable.js"></script>
<!-- Sorting end sanjeev-->

<script language="javascript">
        var flag="block";//window.document.getElementById('hdnfilter').value;
          
            function toggleFilter(){
                var hdnFilter=document.getElementById('hdnfilter').value;
                
                if(hdnFilter!=""){
                if(document.getElementById('hdnfilter').value=="block"){
                flag="none";
                }
                else{
                flag="block";
                }
                document.getElementById('hdnfilter').value="";
                }
               
                if(flag=="block"){
                document.getElementById('filterTable').style.display="none";
                document.getElementById('hideFilter').style.display="none";
                document.getElementById('showFilter').style.display="block";
                flag="none";
              
                /*rmvStyleOnButton();
				rmvStyleOnDropdown();*/
				
               
                }
                else{
                	/* --------begin Ipad issue- Pratik------------*/
                document.getElementById('filterTable').style.display="table-row";  
                    /* --------end Ipad issue- Pratik--------------*/
                document.getElementById('showFilter').style.display="none";
                document.getElementById('hideFilter').style.display="block";
                flag="block";
                /*addStyleOnDropdown();
                addStyleOnButton();*/
              
                
                
                }
                }
            
        function checkProduct(){
         var product=document.getElementById('uploadGTProduct').value;
         var lastName=document.getElementById('lastNameFilter').value;
         var location=document.getElementById('locFilter').value;
         var role=document.getElementById('roleFilter').value;
            if(product =="select"){
                alert("Please Select Product");
                return false;
            }
            document.getElementById('hdnLastNameFilter').value=lastName;
            document.getElementById('hdnLocFilter').value=location;
            document.getElementById('hdnRoleFilter').value=role;
            document.getElementById('hdnfilter').value=flag;
            document.forms[0].action="gotoViewGTList";
            document.forms[0].submit();
        }
        
      function editGuestTrainer(userEmail,userProd)
      {
        window.document.getElementById('selGTEmail').value = userEmail;
        window.document.getElementById('selGTProd').value = userProd;
        document.forms[1].submit();
      }
      
      function removeGT(userEmail,userProd)
      {
      if (confirm('Are you sure you want to remove this trainer?')) 
      {
        window.document.getElementById('selGTEmail').value = userEmail;
        window.document.getElementById('selGTProd').value = userProd;
        document.forms[1].action="removeGuestTrainer";
        window.document.forms[1].submit();            
        }

    } 
     
    function checkGTSelected(){
    	
        var product=document.getElementById('uploadGTProduct').value;
        document.getElementById("hdnproduct").value=product;
        
             if(!(product=='<%=selectedProduct%>')) {
			alert("Selected product not same as Associated Product. Please click on 'View GT List' button again");
			return false;
		}
		if (product == "select") {
			alert("Please Select Product");
			return false;
		}
		var num = 0;
		var checks = document.getElementsByName('selectGTChkBox');

		for (var i = 0; i < checks.length; i++) {
			if (checks[i].checked) {
				num++;
				break;
			}
		}
		if (num == 0) {
			alert("Select Atleast One Trainer");
			return false;
		}
		//   alert("going to action");
		document.forms[1].action = "gotoSelectAllGT";
		//   alert("after action");
		document.forms[1].submit();
		//  alert("after submit");
	}

	function selectAllGT() {
		var selectAll = document.getElementById('iselectAllGTChkBox').checked;
		var checks = document.getElementsByName('selectGTChkBox');
		if (selectAll) {
			for (var i = 0; i < checks.length; i++) {
				checks[i].checked = true;
			}
		} else {
			for (var i = 0; i < checks.length; i++) {
				checks[i].checked = false;
			}
		}
	}

	function checkSelectAllChkBox() {
		var num = 0;
		var checks = document.getElementsByName('selectGTChkBox');
		for (var i = 0; i < checks.length; i++) {
			if (checks[i].checked) {
				num++;
			}
		}
		if (num == checks.length) {
			document.getElementById('iselectAllGTChkBox').checked = true;
		} else {
			document.getElementById('iselectAllGTChkBox').checked = false;
		}
	}
</script>
<style>
.uploadGT table {
	border: 0px
}

.uploadGT td {
	border: 0px
}
</style>
</head>

<body id="p_inputandmaintenance" class="inputandmaintenance"
	onLoad="toggleFilter()">
	<div id="wrap">
		<div id="top_head">
			<h1>Pfizer</h1>
			<h2>Sales Call Evaluation</h2>

			<%@include file="navbar.jsp"%>
			<!-- end #top_head -->
		</div>
		<div id="eval_sub_nav">
			<s:a action="admin">
				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoadmin.gif"
					alt="Back to main admin" width="119" height="18" />
			</s:a>

		</div>

		<h3>Admin: Field Faculty Maintenance</h3>
		<!-- end #content -->
		
		

		<div id="main_content">
			<div class="add_user">
				<!-- <netui:anchor action="gotoAddGT">Add Guest Trainer</netui:anchor> -->
				<div>
					<s:a action="gotoAddGT">Add Guest Trainer</s:a>
				</div>

				<pre>Total number of GTs <%=totalGts%></pre>
				<pre>Total matching criteria <%=totalFilteredGts%></pre>
			</div>

			<%
				if (message != null) {
			%>
			<font color="Red">
			<p><%=message%></p>
			</font>
			<%
				}
			%>
			<s:form action="gotoUploadGTList" enctype="multipart/form-data">
				<input type="hidden" value="" name="hdnLastNameFilter"
					id="hdnLastNameFilter"> <input type="hidden" value=""
					name="hdnLocFilter" id="hdnLocFilter"> <input type="hidden"
						value="" name="hdnRoleFilter" id="hdnRoleFilter"> <input
							type="hidden" value="<%=selectedProduct%>" name="hdnPrevProduct"
							id="hdnPrevProduct">
								<div class="uploadGT">
									<table>
										<tr>
											<td width="100">Product</td>
											<td><select name="uploadGTProduct" id="uploadGTProduct">
													<option value="select">--Select--</option>
													<%
														if (products != null) {
																for (int i = 0; i < products.length; i++) {
																	if (selectedProduct != null
																			&& selectedProduct
																					.equalsIgnoreCase(products[i])) {
													%>
													<option value="<%=selectedProduct%>" selected><%=selectedProduct%></option>
													<%
														} else {
													%>
													<option value="<%=products[i]%>"><%=products[i]%></option>
													<%
														}
																}
															}
													%>
											</select></td>
										</tr>
										<tr id="filterTable">
											<td>Guest Trainer Last Name</td>
											<td><input type="text" id="lastNameFilter"
												value="<%=lastName%>" />

												<td>Guest Trainer Location</td>
												<td><select id="locFilter">
														<option value="">--Select--</option>
														<%
															for (int i = 0; i < locArr.length; i++) {
																	if (location.length() > 0
																			&& location.equalsIgnoreCase(locArr[i])) {
														%>
														<option value="<%=locArr[i]%>" selected><%=locArr[i]%></option>
														<%
															} else {
														%>
														<option value="<%=locArr[i]%>"><%=locArr[i]%></option>
														<%
															}
																}
														%>
												</select></td>

												<td>Guest Trainer Role</td>
											<td><input type="text" id="roleFilter" value="<%=role%>" /></td>
										</tr>
										<tr>
											<td />
											<td><input type="button" value="VIEW GT LIST"
												class="buttonStyles" id='getGTList' onClick="checkProduct()"></td>
										</tr>
									</table>
									<div
										style="position: absolute; text-align: right; right: 50px; top: 220px">
										<a href="#" id="showFilter" onClick="toggleFilter()">Show
											Filter</a>
									</div>
									<div
										style="position: absolute; text-align: right; right: 50px; top: 220px">
										<a href="#" id="hideFilter" onClick="toggleFilter()">Hide
											Filter</a>

									</div>

								</div> <input type="hidden" id="product" name="product" value="" /> <input
								type="hidden" id="hdnfilter" name="hdnfilter"
								value="<%=toggle%>" />
			</s:form>


			<%
				if (trainers != null && trainers.length != 0) {
					// System.out.println("In jsp"+trainers.length);
			%>
			<h3>GT List</h3>
			<s:form action="gotoUpdateGT">
				<input type="hidden" name="selGTEmail" id="selGTEmail" value="" />
				<input type="hidden" name="selGTProd" id="selGTProd" value="" />
				<table cellspacing="0" cellpadding="0" width="auto"
					style="overflow: hidden; table-layout: fixed">
					<tr>
						<!-- Sorting begin sanjeev -->
						<th style="overflow: hidden" class="sort_up"
							onclick="ts_resortTable(this, 0);return false;">Rep First
							Name</th>
						<th style="overflow: hidden" class="sort_up"
							onclick="ts_resortTable(this, 1);return false;">Rep Last
							Name</th>
						<!-- Sorting end sanjeev -->
						<th style="overflow: hidden">Rep NTID</th>
						<th style="overflow: hidden" width="160">Rep Email</th>
						<th style="overflow: hidden" width="30">Rep Role</th>
						<th style="overflow: hidden" width="20">Rep Location</th>
						<th style="overflow: hidden">Associated Product</th>
						<th style="overflow: hidden">Rep Manager</th>
						<th style="overflow: hidden" width="160">Rep Manager Email</th>
						<th style="overflow: hidden" width="30">Rep Manager Role</th>
						<th style="overflow: hidden" align="center">Select GT for
							evaluations <input style="float: left; width: 90%"
							type="checkbox" name="selectAllGTChkBox" id="iselectAllGTChkBox"
							onclick="selectAllGT()" />
						</th>
						<th></th>


					</tr>
					<%
						GuestTrainer objGT = null;
								for (int i = 0; i < trainers.length; i++) {
									boolean removeFlag = true;
									objGT = trainers[i];
					%>

					<tr>
						<td style="overflow: hidden"><%=SCEUtils.ifNull(objGT.getFname(), "&nbsp;")%></td>
						<td style="overflow: hidden"><%=SCEUtils.ifNull(objGT.getLname(), "&nbsp;")%></td>
						<td style="overflow: hidden"><%=SCEUtils.ifNull(objGT.getNtid(), "&nbsp;")%></td>
						<td style="overflow: hidden"><%=SCEUtils.ifNull(objGT.getRepEmail(), "&nbsp;")%></td>
						<td style="overflow: hidden"><%=SCEUtils.ifNull(objGT.getRepRole(), "&nbsp;")%></td>
						<td style="overflow: hidden"><%=SCEUtils.ifNull(objGT.getRepLocation(),
								"&nbsp;")%></td>
						<td style="overflow: hidden"><%=SCEUtils.ifNull(objGT.getAssociatedProduct(),
								"&nbsp;")%></td>
						<td style="overflow: hidden"><%=SCEUtils.ifNull(objGT.getRepManager(),
								"&nbsp;")%></td>
						<td style="overflow: hidden"><%=SCEUtils.ifNull(objGT.getMgrEmail(), "&nbsp;")%></td>
						<td style="overflow: hidden"><%=SCEUtils.ifNull(objGT.getRepManagerRole(),
								"&nbsp;")%></td>
						<%
							if (objGT.getIsSelected().equalsIgnoreCase("Y")) {
						%>
						<td>Approved</td>
						<%
							} else {
						%>
						<td><input style="float: left; width: 90%" type="checkbox"
							name="selectGTChkBox" id="iselectGTChkBox"
							value="<%=objGT.getRepEmail()%>" onclick="checkSelectAllChkBox()" /></td>
						<%
							}
						%>
						<%
							if (objGT.getIsSelected().equalsIgnoreCase("Y")
												&& gtToRemove != null && gtToRemove.length > 0) {
						%>
						<%
							for (int k = 0; k < gtToRemove.length; k++) {
												if (objGT.getRepEmail().equalsIgnoreCase(
														gtToRemove[k])) {
													removeFlag = false;
													break;
												}
											}
										}
							/* 		if (removeFlag
												&& objGT.getIsSelected().equalsIgnoreCase("Y")) { */
										%>
						<td class="last">
							<!--<img src="<%=request.getContextPath()%>/evaluation/resources/_img/button_edit.gif" alt="Edit" width="40" height="18" onClick="editGuestTrainer('<%=objGT.getRepEmail()%>','<%=objGT.getAssociatedProduct()%>')" />-->
							<img
							src="<%=request.getContextPath()%>/evaluation/resources/_img/button_remove.gif"
							id="remove" alt="Remove" width="40" height="18"
							onClick="removeGT('<%=objGT.getRepEmail()%>','<%=objGT.getAssociatedProduct()%>')" />
						</td>
		<!-- 	Sanjeev begin	 -->	<%-- 	<%
							}  else { 
						%>
						<td 
						class="last"><img
							src="<%=request.getContextPath()%>/evaluation/resources/_img/disable_remove.gif"
							id="remove" alt="Remove" width="40" height="18" />
							</td>
						
							 --%>
					<!--		<td class="last">
						
							<img
							src="<%=request.getContextPath()%>/evaluation/resources/_img/button_remove.gif"
							id="remove" alt="Remove" width="40" height="18"
							onClick="removeGT('<%=objGT.getRepEmail()%>','<%=objGT.getAssociatedProduct()%>')" />
							
							</td>-->
					<%-- 	<%
							}
						%> --%>
					</tr>
					<%
						}
					%>
				</table>
				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save.gif"
					alt="Save" width="38" height="19" onClick="checkGTSelected()" />
				<input type="hidden" id="hdnproduct" name="hdnproduct" value="" />
			</s:form>
			<%
				} else if (trainers != null && trainers.length == 0) {
			%>
			<table style="border: 0">
				<tr>
					<td style="border: 0"><font
						style="color:red; font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';">No
						Trainers are present for selected product</font></td>
				</tr>
			</table>
			<%
				}
			%>


			<div class="clear"></div>
		</div>
	</div>
	<!-- end #wrap -->
</body>

</html>