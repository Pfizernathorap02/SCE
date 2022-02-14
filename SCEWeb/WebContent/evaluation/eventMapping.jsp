<%-- 
  JSP page has been updated  by MUZEES for BU segregation 
  --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.pfizer.sce.db.SCEManagerImpl"%>
<%@page import="com.pfizer.sce.beans.Event"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.pfizer.sce.beans.Attendee"%>
<%@ page import="com.pfizer.sce.beans.Template"%>
<%@ page import="com.pfizer.sce.beans.EventCourseProductMapping"%>
<%@ page import="com.pfizer.sce.beans.CourseDetails"%>
<%@ page import="com.pfizer.sce.beans.EventsCreated"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@include file="IAM_User_Auth.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<%
	// 	Added By sanjeev 
	System.out.println("Started: Ankit: ");

	// End
	String strSelectedEvalTempName = "";
	String alreadySelectedEvent = "";
	String recordAlreadyPresent = "";
	String alreadySelectedProduct = "";
	String strValidateString = "";
	String alreadySelectedBUIndex = "";
	String alreadySelectedEventName = "";
	alreadySelectedBUIndex = (String) request
			.getAttribute("alreadySelectedBUIndex");
	int numCourseNames = 0;
	SCEManager sceManager = (SCEManager) pageContext
			.getAttribute("SCEManager");
	SCEManagerImpl sceManagerImpl = new SCEManagerImpl();
	SCEControlImpl sceControllerImpl = new SCEControlImpl();

	EventCourseProductMapping[] mapping = null;
	CourseDetails[] courses_mapped = null;
	String[] buList = sceManagerImpl.getBUList();
	String[] eventNameFetch;
	if (alreadySelectedEvent != null
			&& !alreadySelectedEvent.equals("")
			&& request.getAttribute("eventNameFetch") == null) {
		eventNameFetch = sceManagerImpl
				.getEventNameByBU((String) request
						.getAttribute("alreadySelectedBU"));
	} else
		eventNameFetch = (String[]) request
				.getAttribute("eventNameFetch");

	String[] eventProductFetch = (String[]) request
			.getAttribute("eventProductFetch");

	alreadySelectedEvent = (String) request
			.getAttribute("selEventOption");
	alreadySelectedEventName = (String) request
			.getAttribute("selectedEventName");//added by muzees for PBG and UpJOHN
	//     Added By sanjeev
	EventsCreated event = new EventsCreated();
	Calendar eventStartDateCal = Calendar.getInstance();
	Calendar CurrentDateCal = Calendar.getInstance();
	boolean deleteFlag = false;
	if (alreadySelectedEvent != null && alreadySelectedEvent != "") {
		System.out.println("Already Selected event"
				+ alreadySelectedEvent);
		//alert("Already Selected event"+alreadySelectedEvent);
		//event = sceControllerImpl.getEventByName(eventNameFetch[Integer
		//	.parseInt(alreadySelectedEvent) - 1]);commented by muzees for PBG and UpJOHN
		event = sceControllerImpl
				.getEventByName(alreadySelectedEventName);//added by muzees for PBG and UpJOHN

		// System.out.println("start date captured");
		System.out.println("Sanjeev: Event  startDate captured: "
				+ event.getEventStartDate());

		SimpleDateFormat courseSDF = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date eventStartDate = courseSDF.parse(event
				.getEventStartDate());
		eventStartDateCal.setTime(eventStartDate);
		deleteFlag = CurrentDateCal.before(eventStartDateCal);
		System.out.println("deleteFlag: " + deleteFlag);
	}

	//     End
	recordAlreadyPresent = (String) request
			.getAttribute("recordDuplicate");
	mapping = (EventCourseProductMapping[]) request
			.getAttribute("courseProduct");
	courses_mapped = (CourseDetails[]) request
			.getAttribute("eventPCourses");

	alreadySelectedProduct = (String) request
			.getAttribute("selProductIndex");
%>


<script type="text/javascript">
	 var tempVal = '<%=request.getContextPath()%>';

function getMappingBody(templateObj) {

    
     var index = templateObj.selectedIndex; 
     var which = templateObj.options[index].value;
 
    

     var sel = templateObj.options[index].innerHTML;

     var bu_index = document.getElementById("businessUnit").value;
 	var bu_id=document.getElementById("businessUnit").options[bu_index].innerHTML;
	  
		
		document.getElementById("bu_id").value = bu_id;
		document.getElementById("bu_index").value=bu_index;

     document.getElementById("hdnSelectedTempId").value = which;
     document.getElementById("hdnSelectedTemplateId").value = sel;
     document.getElementById("selectedEventIndex").value = index;
     document.forms[0].action="gotoRetrieveEventMapping.action?bu_index="+bu_index+"&bu_id="+bu_id+"&selEvalTemplate="+which+"&hdnSelectedTemplate="+sel;

    

     document.forms[0].submit();
   
}
function checkSelectedBU()
{
	var bu_index = document.getElementById("businessUnit").value;
 	var bu_id=document.getElementById("businessUnit").options[bu_index].innerHTML;
	
	   
	  	document.getElementById("bu_index").value = bu_index;
		document.getElementById("bu_id").value = bu_id;
	
	
	document.forms[0].action = "gotoEventMappingtoFetchEvents?bu_index="+bu_index+"&bu_id="+bu_id;
  
    document.forms[0].submit();
 
    }
function onScreenLoad() {

	
	var alreadySelectedBUIndex = <%=alreadySelectedBUIndex%>

	var alreadySelectedEvent = <%=alreadySelectedEvent%>
	var alreadySelectedProduct = <%=alreadySelectedProduct%>
	document.getElementById("hdnSelectedProduct").value = "";
	document.getElementById("hdnSelectedCourse").value = "";
	
	if(alreadySelectedBUIndex==null)
	{
		alreadySelectedBUIndex = "0";
	}
	
	document.getElementById("businessUnit").options[alreadySelectedBUIndex].selected = true;
	
	if(alreadySelectedEvent==null)
	 {
		 alreadySelectedEvent = "0";
	 }
	
	if(alreadySelectedProduct==null)
	 {
 	alreadySelectedProduct = "0";
	 }
	
	
	document.getElementById("eventValue").options[alreadySelectedEvent].selected = true;

	document.getElementById("eventProductSelectID").options[alreadySelectedProduct].selected = true;
	if(alreadySelectedBUIndex!="0")
	{
	document.forms[0].eventValue.disabled = false;
	
	
	}
	
	 if(alreadySelectedEvent!="0")
	{
	document.forms[0].eventProductSelectID.disabled = false;
	
	
	}
	 
	 
	 
 	if(alreadySelectedProduct!="0")
	{
	
 	document.forms[0].eventProductSelectID.disabled = false;
	
	 document.getElementById("imgSaveMapping").src = tempVal+"/evaluation/resources/_img/button_save.gif";
	}
 
}

function saveMapping() {
        <%if (courses_mapped != null) {%>
        aLen = '<%=courses_mapped.length%>'; //This is the total number of records fetched
<%}%>
	var strCourseNames = '';
		var sendCourseNames = '';
		var arrCourseName = new Array();
		var totalsChks = 0;

		var strCourseNamesP = document.getElementById("courseName").value;

		if (strCourseNamesP == '') {
			var delMsg = 'Please select COURSE.';
			confirmDel = confirm(delMsg);
		} else {
			strCourseNames = strCourseNamesP.replace(/\r\n|\n/g, ",");

			var indexE = document.getElementById("eventValue").selectedIndex;
			var whichE = document.getElementById("eventValue").options[indexE].innerHTML;
			var indexP = document.getElementById("eventProductSelectID").selectedIndex;
			var whichP = document.getElementById("eventProductSelectID").options[indexP].innerHTML;
			var bu_index = document.getElementById("businessUnit").value;
			var bu_id = document.getElementById("businessUnit").options[bu_index].innerHTML;
				document.getElementById("bu_index").value = bu_index;
				document.getElementById("bu_id").value = bu_id;
				document.getElementById("hdnSelectedTemplateId").value = whichE;
				document.getElementById("selectedEventIndex").value = indexE;
				document.getElementById("hdnSelectedProduct").value = whichP;
				document.forms[0].action = "gotoSaveEventMapping.action?bu_index="
						+ bu_index
						+ "&bu_id="
						+ bu_id
						+ "&selProduct="
						+ indexP
						+ "&hdnSelectedProductName="
						+ whichP
						+ "&selEvalTemplate="
						+ indexE
						+ "&hdnSelectedTemplate=" 
						+ whichE
						+ "&strCoursesNames="
						+ strCourseNames;

				document.forms[0].submit();
			
		}
	}

	function deleteMapping(delMapId) {

		//alert("in deletemapping");

		var delMappingId = delMapId;
		var indexE = document.getElementById("eventValue").selectedIndex;
		var whichE = document.getElementById("eventValue").options[indexE].innerHTML;
		var bu_index = document.getElementById("businessUnit").value;
		var bu_id = document.getElementById("businessUnit").options[bu_index].innerHTML;

		document.getElementById("bu_index").value = bu_index;
		document.getElementById("bu_id").value = bu_id;
		document.getElementById("selectedEventIndex").value = indexE;

		//alert("Values to be deleted" + indexE + "    "  + whichE + "Mapping ID     " + delMappingId );
		var delMsg = 'This mapping will be deleted. Do you wish to continue?';
		confirmDel = confirm(delMsg);
		if (confirmDel == true) {

			document.forms[0].action = "gotoDeleteEventMapping.action?bu_index="
				+ bu_index
				+ "&bu_id="
				+ bu_id
				+"&delMapId="
				+ delMappingId
				+ "&selEvalTemplate="
				+ indexE
				+ "&hdnSelectedTemplate=" + whichE;

			document.forms[0].submit();
		} else {
			window.focus;
		}

	}

	function fetchCourses(templateObj)

	{

		var deleteFlag =
<%=deleteFlag%>
	;
		var bu_index = document.getElementById("businessUnit").value;
		var bu_id = document.getElementById("businessUnit").options[bu_index].innerHTML;
		
		document.getElementById("bu_index").value = bu_index;
		document.getElementById("bu_id").value = bu_id;
		var indexE = document.getElementById("eventValue").selectedIndex;
		var whichE = document.getElementById("eventValue").options[indexE].innerHTML;

		var index = templateObj.selectedIndex;
		var which = templateObj.options[index].value;
		
		var sel = templateObj.options[index].innerHTML;

		if (sel == "---Select---") {
			document.getElementById("imgSearchCourse").src = tempVal
					+ "/evaluation/resources/_img/button_search_course_disabled.gif";
			document.getElementById("imgSaveMapping").src = tempVal
					+ "/evaluation/resources/_img/button_save_disabled.gif";
			if (document.getElementById("deleteProductID") != null) {
				document.getElementById("deleteProductID").disabled = true;
			}

		} else {
			document.getElementById("imgSaveMapping").src = tempVal
					+ "/evaluation/resources/_img/button_save.gif";
			document.getElementById("imgSearchCourse").src = tempVal
					+ "/evaluation/resources/_img/button_search_course.gif";
			if (document.getElementById("deleteProductID") != null) {
				document.getElementById("deleteProductID").disabled = false;
				if (!deleteFlag) {
					document.getElementById("deleteProductID").disabled = true;
				}
			}
		}

	}

	function deleteAllProductMapping() {
		var bu_index = document.getElementById("businessUnit").value;
		var bu_id = document.getElementById("businessUnit").options[bu_index].innerHTML;

		document.getElementById("bu_index").value = bu_index;
		document.getElementById("bu_id").value = bu_id;
		var indexE = document.getElementById("eventValue").selectedIndex;
		var whichE = document.getElementById("eventValue").options[indexE].innerHTML;
		var indexP = document.getElementById("eventProductSelectID").selectedIndex;
		var whichP = document.getElementById("eventProductSelectID").options[indexP].innerHTML;
		var delMsg = 'All mappings related to this Product will be deleted. Do you wish to continue?';
		confirmDel = confirm(delMsg);
		if (confirmDel == true) {

			document.forms[0].action = "gotoDeleteProductMapping.action?bu_index="
				+ bu_index
				+ "&bu_id="
				+ bu_id
				+"&delMapId="
					+ indexP
					+ "&selectedEventIndexName="
					+ indexE
					+ "&hdnSelectedTemplate="
					+ whichE
					+ "&whichPName="
					+ whichP + "&selEvalTemplate=" + indexE;

			document.forms[0].submit();
		} else {
			window.focus;
		}
	}
	//adding new
	function openCourseSearch() {

		var newWinDir = tempVal + "/evaluation/searchCourseForEvent.jsp";
		var newwindow = window.open(newWinDir, 'CourseSearch',
				'status=1, height=500, width=700,scrollbars=yes');
		if (window.focus) {
			newwindow.focus()
		}
		return false;
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

<body class="admin" onload="onScreenLoad()">

	<div id="wrap">

		<input type="hidden" id="hdnSelectedTempId" name="hdnSelectedTemp"
			value="" /> <input type="hidden" id="hdnSelectedTemplateId"
			name="hdnSelectedTemplate" value="" /> <input type="hidden"
			id="hdnSelectedProduct" name="hdnSelectedProductName" value="" /> <input
			type="hidden" id="hdnSelectedCourse" name="hdnSelectedCourseName"
			value="" /> <input type="hidden" id="eventMappingID"
			name="eventMappingIDName" value="" /> <input type="hidden"
			id="bu_id" name="bu_id" value="" /> <input type="hidden"
			id="bu_index" name="bu_index" value="" />


		<div id="top_head">
			<h1>Pfizer</h1>
			<h2>Sales Call Evaluation</h2>
			<%@include file="navbar.jsp"%>
			<!-- end #top_head -->

		</div>


		<div id="eval_sub_nav">


			<s:a action="admin" id="eventDisplayForm">
				<img
					src="<%=request.getContextPath()%>/evaluation/resources/_img/button_backtoadmin.gif"
					alt="Back to main admin" width="119" height="18" />
			</s:a>

		</div>

		<H3>Event - Product Course Mapping</H3>

		<DIV id=main_content>



			<s:form action="gotoSearchForAttendee" id="evalTemplLmsCourseMapping">

				<input type="hidden" id="selectedEventIndex"
					name="selectedEventIndexName" value="">

					<table id="eventDropDown" align="center"
						style="border: 0 solid red">
						<tr>
							<td class="evalmappingtd" style="width: 15%;"><b>Business
									Unit </b></td>
							<td class="evalmappingtd"><select id="businessUnit"
								style="border: .07em solid grey; margin: 0; padding: 0; font-size: 0.9em;"
								name="businessUnit" onchange="checkSelectedBU();">
									<option value="0">---Select---</option>
									<%
										if (buList != null) {
												for (int i = 0; i < buList.length; i++) {
									%>
									<option value="<%=i + 1%>"><%=buList[i]%></option>
									<%
										}
											}
									%>
							</select></td>
						</tr>
						<tr>
							<td class="evalmappingtd" style="width: 15%;"><b>Event </b></td>
							<td class="evalmappingtd"><select id="eventValue"
								style="border: .07em solid grey; margin: 0; padding: 0; font-size: 0.9em;"
								name="eventValueName" onchange="getMappingBody(this);" disabled>
									<option value="0">---Select---</option>
									<%
										if (eventNameFetch != null) {
												for (int i = 0; i < eventNameFetch.length; i++) {
									%>
									<option value="<%=i + 1%>"><%=eventNameFetch[i]%></option>
									<%
										}
											}
									%>
							</select></td>
						</tr>
					</table>

					<table id="productDropDown" align="center"
						style="border: 0 solid red">
						<tr>
							<td width="35%" class="evalmappingtd"><b>Product</b></td>
							<td width="35%" class="evalmappingtd"
								style="position: absolute; left: 200px;"><b>Course Name</b></td>
							<td width="35%" class="evalmappingtd"
								style="position: absolute; left: 492px;"><b>Course Code</b></td>
						</tr>

						<tr>
							<td valign="top" class="evalmappingtd"><select
								id="eventProductSelectID"
								style="border: .07em solid grey; margin: 0; padding: 0; font-size: 0.9em;"
								name="eventProductSelectName" onchange="fetchCourses(this);"
								disabled>
									<option value="0">---Select---</option>

									<%
										if (eventProductFetch != null) {
												for (int i = 0; i < eventProductFetch.length; i++) {
									%>

									<option value="<%=i + 1%>"><%=eventProductFetch[i]%></option>

									<%
										}
											}
									%>

							</select></td>



							<td width="35%" valign="top" class="evalmappingtd"><textarea
									readonly
									style="position: absolute; left: 200px; width: 280px; height: 100px; border: .07em solid grey; white-space: pre; overflow: auto;"
									wrap="off" name="courseName" class="course_name_mapping"
									id="courseName" value=""></textarea> <input type="hidden"
								id="hdnSelectedCourseNames" name="hiddenCourseNames" value=""></td>

							<td width="35%" valign="top" class="evalmappingtd"><textarea
									readonly
									style="position: absolute; left: 492px; width: 280px; height: 100px; border: .07em solid grey; white-space: pre; overflow: auto;"
									wrap="off" name="courseCode" class="course_code_mapping"
									id="courseCode" value=""></textarea> <input type="hidden"
								id="hdnSelectedCourseCodes" name="hiddenCourseCodes" value=""></td>

							<td width="8%" valign="top" class="evalmappingtd"><img
								id="imgSearchCourse"
								src="<%=request.getContextPath()%>/evaluation/resources/_img/button_search_course_disabled.gif"
								style="left: 780px; position: absolute; width: 89px; height: 19px"
								alt="Search Course" onclick="openCourseSearch()"></td>

						</tr>

						<tr>
							<td colspan="1" width="5%" class="evalmappingtd" valign="top">
								<img id="imgSaveMapping" name="imgSaveMappingName"
								src="<%=request.getContextPath()%>/evaluation/resources/_img/button_save_disabled.gif"
								align="left"
								style="left: 47px; position: absolute; top: 340px; width: 36px; height: 19px"
								onclick="saveMapping()" alt="Save Mapping"> <%
 	if (recordAlreadyPresent == null
 				|| recordAlreadyPresent.equalsIgnoreCase("0")) {
 %>

									<td style="border: 0;"></td> <%
 	} else if ((recordAlreadyPresent.equalsIgnoreCase("P"))) {
 %>

									<td style="weight: bold;" class="evalmappingtd"><font
										style="color:red; font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial' position: absolute; left: 100px;">
										</br>
										</br>
										</br>
										</br>
										</br>Selected Mapping is already present. </font></td> <%
 	} else {
 %>

									<td style="border: 0;"></td> <%
 	}
 %>
						</tr>


					</table> <%
 	if ((mapping == null)
 				&& (alreadySelectedEvent == null ||alreadySelectedEvent
 						.equals("0"))) {
 %>
					<table id="tblSearchResults" cellspacing="0" cellpadding="0"
						style="display: none;" class="evalmappingtable" align="center"></table>

					<%
						} else if (mapping != null && mapping.length != 0) {
					%>

					<div align="center" style="margin-top: 130px;">
						<hr>
					</div>

					<div>
							<!-- added color for disabled delete mappings shindo -->
						<input id="deleteProductID" class="buttonStylesWhite" align="left"
							style="width: 195px; align: left; border: 0px; padding: 0px; font-size: 0.9em;color: gray;"
							type="button" value="  Delete Mappings of Selected Product"
							disabled onclick="deleteAllProductMapping()">
					</div>

					<table id="tblSearchCriteria" class="evalmappingtable"
						align="center" style="border: 0 solid red">

						<tr>
							<td
								style="weight: bold; font-size: 1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';"
								class="evalmappingtd">
								Following Course(s) are mapped to following Product(s):</td>
						</tr>
					</table>

					<table id="tblSearchCriteria" class="evalmappingtable"
						align="center" style="border: 0 solid red">

						<tr>
							<th width="40%" align="left" class="sort_up"><b>Course
									Name</b></th>
							<th width="40%" align="left" class="sort_up"><b>Product</b></th>
							<th width="20%"></th>
						</tr>

						<%
							EventCourseProductMapping objUser = null;
									if (mapping != null) {
										for (int i = 0; i < mapping.length; i++) {
											objUser = mapping[i];
						%>

						<tr id="<%=objUser.getMapping_id()%>">
							<td width="25%" style="border-left: 1px solid #CCCCCC;"
								align="left" id="eventRow"><%=objUser.getCourse_Code()%></td>
							<td width="25%" align="left"><%=objUser.getProduct_Name()%></td>
							<td width="25%" align="left">
								<%
									if (deleteFlag) {
								%> <img
								src="<%=request.getContextPath()%>/evaluation/resources/_img/button_delete.gif"
								align="left" style="width: 45px; height: 19px"
								onClick="deleteMapping('<%=objUser.getMapping_id()%>')"
								alt="Delete Mapping"> <%
 	} else {
 %> <img
									src="<%=request.getContextPath()%>/evaluation/resources/_img/disable_delete.gif"
									align="left" style="width: 45px; height: 19px"
									alt="Delete Mapping"> <%
 	}
 %>
							</td>
						</tr>

						<%
							}
									}
						%>

					</table> <%
 	} else {
 %>

					<div align="center">
						<hr>
					</div>

					<table width="90%" id="tblSearchResults"
						style="cellpadding: 0; cellspacing: 0; border-right: 0; border-bottom: 0; border-left: 0; border-top: 0; display: block;"
						class="evalmappingtable" align="center">

						<tr>
							<td class="evalmappingtd"><font
								style="color:red; font-size:1.1em; font-family: 'Lucida Grande', 'Trebuchet MS', 'Arial';">
								</br>
								</br>
								</br>
								</br>
								</br>
								No Product-Courses have been mapped to the selected Event </font></td>
						</tr>

					</table> <%
 	}
 %>
				
			</s:form>

		</DIV>

	</div>
	<!--end wrap -->
	;
</body>

</html>
