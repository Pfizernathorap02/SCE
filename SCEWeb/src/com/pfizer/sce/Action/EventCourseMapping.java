package com.pfizer.sce.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.pfizer.sce.beans.CourseDetails;
import com.pfizer.sce.beans.EventCourseProductMapping;
import com.pfizer.sce.beans.EventsCreated;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.EvaluationControllerHelper;
import com.pfizer.sce.helper.LegalConsentHelper;
import com.pfizer.sce.utils.LoggerHelper;

public class EventCourseMapping extends ActionSupport implements
		ServletRequestAware {

	private static final long serialVersionUID = 1L;
	HttpServletRequest request;

	private static SCEManagerImpl sceManager = new SCEManagerImpl();
	private static LegalConsentHelper legalConsentHelp = new LegalConsentHelper();

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String gotoEventMapping() {

		HttpServletRequest req = getRequest();
		HttpSession session = req.getSession();

		
		String[] eventNameFetch = sceManager.getEventName();
		req.setAttribute("eventNameFetch", eventNameFetch);

		return new String("success");
	}

	public String gotoRetrieveEventMapping() {

		HttpServletRequest req = getRequest();
		HttpSession session = req.getSession();
		
		this.gotoEventMapping();

		try {

			String result = legalConsentHelp.checkLegalConsent(req, session);
			// System.out.println("*****result*****:" + result);

			if (result != null && result.equals("success")) {
				// System.out.println("*************Forwarding to legalConsent");
				String forwardToHomePage = "Y";
				EvaluationControllerHelper.setBookMarkURL(session, req,
						forwardToHomePage);

				return new String("legalConsent");

			} else if (result != null && result.equals("exception")) {
				// System.out.println("**********Forwarding to exception");
				return new String("failure");
			}

			String contentBody = req.getParameter("contentBody");
			String selEvalTemplate = req.getParameter("selEvalTemplate");
			String selEvalTemplateName = req
					.getParameter("hdnSelectedTemplate");
			String selEventOption = req.getParameter("selectedEventIndexName");

			

			Integer evalTemplateId = new Integer(
					Integer.parseInt(selEvalTemplate));
			String selectedEventName = selEvalTemplateName;
			

			if (!selEventOption.equalsIgnoreCase("0")) {

				

				EventCourseProductMapping[] courseProduct = sceManager
						.getMappingForEvent(selectedEventName);
				selEventOption = req.getParameter("selectedEventIndexName");
				req.setAttribute("courseProduct", courseProduct);
				req.setAttribute("selEventOption", selEventOption);

			

				String[] eventProductFetch = sceManager.getProductForEvent();
				req.setAttribute("eventProductFetch", eventProductFetch);

			

			} else {
				selEventOption = req.getParameter("selectedEventIndexName");
				req.setAttribute("courseProduct", null);
				req.setAttribute("selEventOption", selEventOption);
			}

		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");

	}

	// To retrieve the Course Name & Course Code from LMS database into the pop-up window
	public String searchCourseDetailsForEvent() {
		

		HttpServletRequest req = getRequest();

		try {
			String msg = "";

			
			String searchCourseName = req.getParameter("courseName");
			String searchCourseCode = req.getParameter("courseCode");
			

			CourseDetails[] modifiedCourseDets = null;

			CourseDetails[] courseDetails = sceManager
					.getAllSearchedCourseDetails(searchCourseName,
							searchCourseCode);

			req.setAttribute("courseSearchArr", courseDetails);
			req.setAttribute("searchCourseName", searchCourseName);
			req.setAttribute("searchCourseCode", searchCourseCode);
			// System.out.println("CourseDetails[] Array Size --->"+ courseDetails.length);

			if (courseDetails.length == 0) {

				msg = "There are no Courses matching the entered search criteria. Please try again.";
				// System.out.println("courseDetails.length==0-----msg = " + msg);
				req.setAttribute("message", msg);
			} else if (courseDetails.length >= 250) {
				msg = "250 records have been fetched so far. Please enter more specific search criteria to limit the search results";
				// System.out.println("courseDetails.length>250-----msg = " + msg);
				req.setAttribute("message", msg);
			}

		}
	
		catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
	
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");
	}

	public String gotoDeleteEventMapping() {

		HttpServletRequest req = getRequest();
		HttpSession session = req.getSession();

			this.gotoRetrieveEventMapping();

		try {

			String result = legalConsentHelp.checkLegalConsent(req, session);

			// System.out.println("*****result*****:" + result);
			if (result != null && result.equals("success")) {
				// System.out.println("*************Forwarding to legalConsent");
				String forwardToHomePage = "Y";
				EvaluationControllerHelper.setBookMarkURL(session, req,
						forwardToHomePage);
				return new String("legalConsent");
			} else if (result != null && result.equals("exception")) {
				// System.out.println("**********Forwarding to exception");
				return new String("failure");
			}

			String mapId = req.getParameter("delMapId");
			Integer mappingID = new Integer(Integer.parseInt(mapId));
			String selEventName = req.getParameter("hdnSelectedTemplate");
			String selEventOption = req.getParameter("selectedEventIndexName");
			String doEmpty = "";

			sceManager.gotoDeleteEventMapping(mappingID);
			gotoRetrieveEventMapping();

			req.setAttribute("hdnSelectedCourseName", doEmpty);
			req.setAttribute("hdnSelectedProductName", doEmpty);
			req.setAttribute("selEventOption", selEventOption);

		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");

	}

	public String gotoSaveEventMapping() {

		HttpServletRequest req = getRequest();
		HttpSession session = req.getSession();

	
		try {

			String result = legalConsentHelp.checkLegalConsent(req, session);

			// System.out.println("*****result*****:" + result);
			if (result != null && result.equals("success")) {
				// System.out.println("*************Forwarding to legalConsent");
				String forwardToHomePage = "Y";
				EvaluationControllerHelper.setBookMarkURL(session, req,
						forwardToHomePage);
				return new String("legalConsent");
			} else if (result != null && result.equals("exception")) {
				// System.out.println("**********Forwarding to exception");
				return new String("failure");
			}

	
			String strCourseNames = req.getParameter("strCoursesNames");
			String[] selCourseName = null;
			selCourseName = strCourseNames.split(",");

			String selProductName = req.getParameter("hdnSelectedProductName");
			String selEventName = req.getParameter("hdnSelectedTemplate");
			String selEventOption = req.getParameter("selectedEventIndexName");
			String doEmpty = "";
			String duplicateCheck = "";

			for (int i = 0; i < selCourseName.length; i++) {
				String courseSel = selCourseName[i];

				if (!(courseSel.equalsIgnoreCase(doEmpty))) {

					EventCourseProductMapping[] check = sceManager
							.gotoCheckDuplicate(courseSel, selEventName);
					if (check != null && check.length > 0) {
						duplicateCheck = "P";
						System.out
								.println("_______________Record already existing_______________");
						req.setAttribute("recordDuplicate", duplicateCheck);
					} else {

						if (!duplicateCheck.equalsIgnoreCase("P")) {
							duplicateCheck = "S";
						}

						System.out
								.println("___________________New Record Insert_______________");
						sceManager.gotoSaveEventMapping(selEventName,
								selProductName, courseSel);
						req.setAttribute("recordDuplicate", duplicateCheck);

					}

				}

			}
			gotoRetrieveEventMapping();

			req.setAttribute("hdnSelectedCourseName", doEmpty);
			req.setAttribute("hdnSelectedProductName", doEmpty);

		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");

	}

	public String gotoDeleteProductMapping() {

		HttpServletRequest req = getRequest();
		HttpSession session = req.getSession();

		try {

			String result = legalConsentHelp.checkLegalConsent(req, session);

			// System.out.println("*****result*****:" + result);
			if (result != null && result.equals("success")) {
				// System.out.println("*************Forwarding to legalConsent");
				String forwardToHomePage = "Y";
				EvaluationControllerHelper.setBookMarkURL(session, req,
						forwardToHomePage);
				return new String("legalConsent");
			} else if (result != null && result.equals("exception")) {
				// System.out.println("**********Forwarding to exception");
				return new String("failure");
			}

	
			String mapId = req.getParameter("delMapId");
			String productName = req.getParameter("whichPName");

			Integer mappingID = new Integer(Integer.parseInt(mapId));
			String selEventName = req.getParameter("hdnSelectedTemplate");
			String selEventOption = req.getParameter("selectedEventIndexName");
			String doEmpty = "";

			sceManager.gotoDeleteProductMapping(productName, selEventName);
			gotoRetrieveEventMapping();

			req.setAttribute("hdnSelectedCourseName", doEmpty);
			req.setAttribute("selProductIndex", null);
			req.setAttribute("selEventOption", selEventOption);

		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");

	}

}
