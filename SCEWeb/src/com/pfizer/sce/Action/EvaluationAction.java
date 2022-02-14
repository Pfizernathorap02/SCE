package com.pfizer.sce.Action;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import com.pfizer.sce.db.*;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.sce.ActionForm.EvaluateForm;
import com.pfizer.sce.ActionForm.SearchForAttendeeForm;
import com.pfizer.sce.ActionForm.SearchForm;
import com.pfizer.sce.beans.Attendee;
import com.pfizer.sce.beans.EmailTemplate;
import com.pfizer.sce.beans.EvaluationForm;
import com.pfizer.sce.beans.EvaluationFormScore;
import com.pfizer.sce.beans.LegalQuestion;
import com.pfizer.sce.beans.LegalQuestionDetail;
import com.pfizer.sce.beans.SCE;
import com.pfizer.sce.beans.SCEDetail;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.SCEReport;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.common.SCEConstants;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.utils.MailUtil;
import com.pfizer.sce.utils.SCEUtils;

public class EvaluationAction extends ActionSupport implements
		ServletRequestAware, ModelDriven {

	private static SCEManagerImpl sceManager = new SCEManagerImpl();
	private String evalAttendee;

	private Integer version = new Integer(-1);

	private EvaluateForm evaluateForm = new EvaluateForm();
	HttpServletRequest request;

	private static HashMap eventMap = sceManager.getAllEventMap();

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public Object getModel() {
		return evaluateForm;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	public HttpServletRequest getServletRequest() {
		return this.request;

	}

	public String viewEvaluation() {

		HttpServletRequest req = getServletRequest();

		try {

			Integer sceId = evaluateForm.getSelSceId();
			// System.out.println("chekc doe");

			evaluateForm.setMode(SCEConstants.VIEW_MODE);
			// Get The SCE
			SCE objSCE = sceManager.getSCEById(sceId);

			if (objSCE.getUploadedDate() != null) {
				return new String("success2");
			}

			// Get and Set the Details
			SCEDetail[] objSCEDetailArr = sceManager
					.getSCEDetailsBySCEId(objSCE.getId());
			// Mayank : 10-Nov-2011 : SCE Enhancement 2011
			EvaluationFormScore[] objEvaluationFormScoreArr = sceManager
					.getEvaluationFormScore(objSCE.getTemplateVersionId());
			LegalQuestion[] legalQuestionArr = sceManager
					.getLegalQuestion(objSCE.getTemplateVersionId());
			LegalQuestionDetail[] legalQuestionDetailArr = sceManager
					.getLegalQuestionDetail(sceId);

			if (objSCEDetailArr != null) {
				for (int i = 0; i < objSCEDetailArr.length; i++) {
					objSCE.addSceDetailList(objSCEDetailArr[i]);
				}
			}

			if (objEvaluationFormScoreArr != null) {
				for (int i = 0; i < objEvaluationFormScoreArr.length; i++) {
					objSCE.addEvaluationFormScoreList(objEvaluationFormScoreArr[i]);
				}
			}

			if (legalQuestionArr != null) {
				for (int i = 0; i < legalQuestionArr.length; i++) {
					objSCE.addLegalQuestionList(legalQuestionArr[i]);
				}
			}

			if (legalQuestionDetailArr != null) {
				for (int i = 0; i < legalQuestionDetailArr.length; i++) {
					objSCE.addLegalQuestionDetailList(legalQuestionDetailArr[i]);
				}
			}

			evaluateForm.setEvalEmplId(objSCE.getEmplId());

			/* Preserve Search For Back */
			/*
			 * evaluateForm.setEvalEventId(form.getSelEventId());
			 * evaluateForm.setLastNameSrch(form.getLastName());
			 * evaluateForm.setFirstNameSrch(form.getFirstName());
			 * evaluateForm.setEmplIdSrch(form.getEmplId());
			 * evaluateForm.setEventIdSrch(form.getEventId());
			 * evaluateForm.setTerritoryIdSrch(form.getSalesPositionId());
			 * evaluateForm.setIsPassportSrch(form.getIsPassport());
			 * evaluateForm.setSelEmplIdSrch(form.getSelEmplId());
			 */

			req.setAttribute("sceToDisplay", objSCE);
			req.setAttribute("from", "search");

			req.setAttribute("evaluateForm", evaluateForm);

		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");
	}

	public String viewEvaluationHistory() {

		HttpServletRequest req = getServletRequest();

		String checkLength = "";
		try {
			HttpSession session = req.getSession(false);

			String emplId = evaluateForm.getSelEmplId();
			Integer eventId = evaluateForm.getSelEventId();
			String productName = evaluateForm.getSelProduct();

			Integer sceId = evaluateForm.getSelSceId();

			// String productName = (String)req.getParameter("product");
			// Integer eventId = new Integer(req.getParameter("eventID"));
			// String emplId = (String)req.getParameter("emplId");
			// Integer sceId = new Integer(req.getParameter("sceId"));

			// form.setSelSceId(sceId);

			session.setAttribute("sceID", sceId);

			SCE[] objSCEArr = sceManager.getSCESHistoryByEventEmplId(eventId,
					emplId, productName);

			String GT_NOT_LMS1 = (String) session.getAttribute("GT_NOT_LMS");
			session.removeAttribute("GT_NOT_LMS");
			// System.out.println("GT_NOT_LMS1" + GT_NOT_LMS1);
			if (objSCEArr.length == 1 || GT_NOT_LMS1 != null) {

				return new String("success1");

			}

			// for(int i=0;i<objSCEArr.length;i++){

			// if(objSCEArr[0].getUploadedDate()!=null){

			// return new Forward("success2");

			// }

			// }
			// RUPINDER MODIFIED ON 10 DEC starts remember to copy declaration
			// while copying dis
			if (objSCEArr.length > 1) {
				checkLength = "Yes";
			}

			// RUPINDER MODIFIED ON 10 DEC ends

			// System.out.println("SCE Objects" + objSCEArr);

			session.setAttribute("test", "test");
			session.setAttribute("sces1", objSCEArr);
			// System.out.println("Session ID ***" + session.getId());
			// System.out.println(objSCEArr);
			// System.out.println("**********LENGTH " + objSCEArr.length);
			SCE[] sces = (SCE[]) session.getAttribute("sces");
			req.setAttribute("sces", sces);

			req.setAttribute("history", "history");
			req.setAttribute("checkLength", checkLength);
			req.setAttribute("selProduct", productName);
			req.setAttribute("eventID", eventId);
			req.setAttribute("emplId", emplId);

			// req.setAttribute("selProduct",sceId);

			// System.out.println("Session ID 1***" + session.getId());
		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");

	}

	public String gotoEvaluationResults() {
		HttpServletRequest req = getServletRequest();

		HttpSession session = req.getSession(false);
		try {
			Integer eventId = null;
			String emplId = req.getParameter("emplId");
			if (req.getParameter("eventId") != null) {
				eventId = Integer
						.parseInt((String) req.getParameter("eventId"));
			}
			System.out.print("ch");

			// System.out.println("in goto EvaluationResults");

			// System.out.println("Emplid : "+emplId+"EventId :"+eventId);

			// emplId="1428991";
			// eventId=new Integer(-1);
			session.setAttribute("emplId", emplId);
			session.setAttribute("eventId", eventId);

			evaluateForm.setEmplId((String) session.getAttribute("emplId"));

			// System.out.println("Emplid is : "+evaluateForm.getEmplId());

			// Get The SCES For This Empl Id
			// System.out.println("Event ID in gotoEvaluationResults" +
			// eventId);
			// System.out.println("Empl ID in gotoEvaluationResults" + emplId);

			// log.debug("Event ID in gotoEvaluationResults"+eventId);
			// log.debug("Empl ID in gotoEvaluationResults"+emplId);

			SCE[] objSCEArr = sceManager.getSCESByEventEmplId(eventId, emplId);
			// System.out.println("SCE Objects" + objSCEArr);

			// log.debug("SCE Objects"+objSCEArr);
			req.setAttribute("sces", objSCEArr);

		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			// log.debug("SCEException in gotoEvaluationResults");
			// System.out.println("SCEException in gotoEvaluationResults");
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			// log.error("Exception in gotoEvaluationResults",e);
			// System.out.println("Exception in gotoEvaluationResults");
			e.printStackTrace();
			return new String("failure");
		}
		return new String("success");
	}

	public String enterNewEvaluation() {
		HttpServletRequest req = getServletRequest();
		HttpSession session = req.getSession(false);

		Integer sceId = evaluateForm.getSelSceId();
		// System.out.println(session.getAttribute("emplId"));

		evaluateForm.setEvalEmplId((String) session.getAttribute("emplId"));

		// Integer sceId= (Integer)req.getParameter("selSceId");
		/*
		 * Integer sceId =null; if(req.getParameter("selSceId") != null){
		 * 
		 * // System.out.println("Get Parameter :::: "+(String)req.getParameter(
		 * "selSceId")); sceId =
		 * Integer.parseInt((String)req.getParameter("selSceId"));}
		 */

		// System.out.println(session.getAttribute("emplId"));
		try {

			evaluateForm.setMode(SCEConstants.CREATE_MODE);
			SCE objSCE = sceManager
					.getNewSCE(evaluateForm.getSelEventId(),
							SCEConstants.EVENT_COURSE_ALL,
							evaluateForm.getSelProduct());
			// Get The SCE

			// evaluateForm.setEvalEmplId(evaluateForm.getSelEmplId());
			evaluateForm.setEvalEventId(evaluateForm.getSelEventId());
			evaluateForm.setEvalProductCode(evaluateForm.getSelProductCode());
			evaluateForm.setEvalProduct(evaluateForm.getSelProduct());

			if (sceId == null) {
				if ("".equals(evaluateForm.getSelCourse()))
					evaluateForm.setEvalCourse(null);
				else
					evaluateForm.setEvalCourse(evaluateForm.getSelCourse());

				if ("".equals(evaluateForm.getSelClassroom()))
					evaluateForm.setEvalClassroom(null);
				else
					evaluateForm.setEvalClassroom(evaluateForm
							.getSelClassroom());

				if ("".equals(evaluateForm.getSelTable()))
					evaluateForm.setEvalTable(null);
				else
					evaluateForm.setEvalTable(evaluateForm.getSelTable());

				req.setAttribute("isSubmitted", "No");
			} else {
				req.setAttribute("isSubmitted", "Yes");
			}

			/* Preserve Search For Back */
			/*
			 * evaluateForm.setLastNameSrch(evaluateForm.getLastName());
			 * evaluateForm.setFirstNameSrch(evaluateForm.getFirstName());
			 * evaluateForm.setEmplIdSrch(evaluateForm.getEmplId());
			 * evaluateForm.setEventIdSrch(evaluateForm.getEventId());
			 * evaluateForm
			 * .setTerritoryIdSrch(evaluateForm.getSalesPositionId());
			 * evaluateForm.setIsPassportSrch(evaluateForm.getIsPassport());
			 * evaluateForm.setSelEmplIdSrch(evaluateForm.getSelEmplId());
			 */

			req.setAttribute("sceToDisplay", objSCE);
			req.setAttribute("from", "search");

			req.setAttribute("evaluateForm", evaluateForm);

		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");
	}

	public String giveAutoCredit() {
		HttpServletRequest req = getServletRequest();
		Integer sceId = evaluateForm.getSelSceId();
		User user = (User) req.getSession().getAttribute("user");
		// evaluateForm.setEvalEmplId(evaluateForm.getSelEmplId());
		String emplId = evaluateForm.getSelEmplId();
		Integer eventId = evaluateForm.getSelEventId();
		evaluateForm.setEvalEventId(eventId);
		evaluateForm.setEvalEmplId(emplId);
		String productCode = evaluateForm.getSelProductCode();
		String product = evaluateForm.getSelProduct();
		String course = null;
		String classroom = null;
		String table = null;
		boolean isDuplicate = false;
		if (sceId == null) {
			course = evaluateForm.getSelCourse();
			classroom = (evaluateForm.getSelClassroom() == null || ""
					.equals(evaluateForm.getSelClassroom())) ? null
					: evaluateForm.getSelClassroom();
			table = (evaluateForm.getSelTable() == null || ""
					.equals(evaluateForm.getSelTable())) ? null : evaluateForm
					.getSelTable();
		}
		/* Start: AGARWN21: For retrieving employee id*/
		//Start of Nishtha
		String submittedByEmplId = user != null ? sceManager.getempbyNTID1(user.getNtid()) : null;
		//End of Nishtha

		try {
			/* 2020 Q3: Start of MUZEES for multiple evalution */
			Attendee evalAttendee = sceManager.getAttendeeByEmplId(emplId);
			SCE checkSCE = sceManager.getLatestSCE(emplId, eventId,
					productCode, SCEConstants.ST_SUBMITTED);
			if (checkSCE != null && sceManager.checkduplicate(checkSCE, null)) {
				req.setAttribute("msg",
						"Your Evaluation for " + evalAttendee.getName()
								+ " is already submitted.");
				isDuplicate = true;
			} else {
				req.setAttribute("msg",
						"Auto Credit for " + evalAttendee.getName()
								+ " has been submitted successfully.");
				sceManager.giveAutoCredit(emplId, eventId, productCode,
						product, course, classroom, table, submittedByEmplId);
			}// end of MUZEES
			req.setAttribute("highlightSelProd", productCode);
			evaluateForm.setEvalEmplId(emplId);

		} catch (Exception e) {
			return new String("failure");
		}
		/* Preserve Search For Back */
		/*
		 * evaluateForm.setLastNameSrch(evaluateForm.getLastName());
		 * evaluateForm.setFirstNameSrch(evaluateForm.getFirstName());
		 * evaluateForm.setEmplIdSrch(evaluateForm.getEmplId());
		 * evaluateForm.setEventIdSrch(evaluateForm.getEventId());
		 * evaluateForm.setTerritoryIdSrch(evaluateForm.getSalesPositionId());
		 * evaluateForm.setIsPassportSrch(evaluateForm.getIsPassport());
		 * evaluateForm.setSelEmplIdSrch(evaluateForm.getSelEmplId());
		 */
		req.setAttribute("isAutoCredit", "Yes");
		req.setAttribute("from", "search");
		req.setAttribute("isDuplicate", isDuplicate);
		req.setAttribute("evaluateForm", evaluateForm);
		return new String("success");

	}

	public String deleteSCE() {
		HttpServletRequest req = getServletRequest();
		Integer sceId = evaluateForm.getSelSceId();

		try {

			sceManager.deleteSCE(sceId);
			String emplId = evaluateForm.getSelEmplId();
			Integer eventId = (Integer) req.getSession()
					.getAttribute("eventId");
			evaluateForm.setEmplId(emplId);
			// Get The SCES For This Empl Id
			SCE[] objSCEArr = sceManager.getSCESByEventEmplId(eventId, emplId);

			req.setAttribute("msg", "SCE has been deleted.");
			req.setAttribute("sces", objSCEArr);

			evaluateForm.setSelSceId(null);
		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			return new String("failure");
		}
		return new String("success");
	}

	public String cancelEvaluation() {
		HttpServletRequest req = getServletRequest();

		SearchForm searchForm = new SearchForm();

		searchForm.setAttendeeTrainingDate(evaluateForm.getEvalTrainingDate());
		searchForm.setAttendeeEventId(evaluateForm.getEvalEventId());

		req.setAttribute("searchClicked", "N");
		req.setAttribute("course", evaluateForm.getEvalCourse());
		req.setAttribute("classroom", evaluateForm.getEvalClassroom());
		req.setAttribute("table", evaluateForm.getEvalTable());

		req.setAttribute("searchForm", searchForm);
		return new String("success");
	}

	public String backToEvaluationResults() {
		HttpServletRequest req = getServletRequest();
		SearchForAttendeeForm searchForAttendeeForm = new SearchForAttendeeForm();
		try {
			searchForAttendeeForm.setEventId(evaluateForm.getEventIdSrch());
			searchForAttendeeForm.setLastName(evaluateForm.getLastNameSrch());
			searchForAttendeeForm.setFirstName(evaluateForm.getFirstNameSrch());
			searchForAttendeeForm.setEmplId(evaluateForm.getEmplIdSrch());
			searchForAttendeeForm.setSalesPositionId(evaluateForm
					.getTerritoryIdSrch());
			searchForAttendeeForm.setIsPassport(evaluateForm
					.getIsPassportSrch());
			searchForAttendeeForm.setSelEmplId(evaluateForm.getSelEmplIdSrch());
			String emplId = evaluateForm.getEvalEmplId();
			/* 2020 Q3: Start of MUZEES for navigational issues */
			if (emplId == null || emplId == "") {
				emplId = evaluateForm.getEmplId();
			}
			if (emplId == null || emplId == "") {
				emplId = evaluateForm.getSelEmplId();
				evaluateForm.setEvalEmplId(emplId);
				evaluateForm.setEmplId(emplId);
			}// end
			Integer eventId = (Integer) req.getSession()
					.getAttribute("eventId");
			// Get The SCES For This Empl Id
			SCE[] objSCEArr = sceManager.getSCESByEventEmplId(eventId, emplId);
			req.setAttribute("sces", objSCEArr);
			req.setAttribute("searchForAttendeeForm", searchForAttendeeForm);

		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			return new String("failure");
		}
		return new String("success");

	}

	public String backToSelectAttendee() {
		HttpServletRequest req = getServletRequest();

		SearchForm sform = new SearchForm();
		req.setAttribute("searchClicked", "N");
		sform.setAttendeeTrainingDate(evaluateForm.getEvalTrainingDate());
		sform.setAttendeeEventId(evaluateForm.getEvalEventId());
		req.setAttribute("course", evaluateForm.getEvalCourse());
		req.setAttribute("classroom", evaluateForm.getEvalClassroom());
		req.setAttribute("table", evaluateForm.getEvalTable());

		req.setAttribute("sform", sform);

		return new String("success");
	}

	public String backToScePendingReport() {
		HttpServletRequest req = getServletRequest();

		SearchForAttendeeForm searchForAttendeeForm = new SearchForAttendeeForm();

		searchForAttendeeForm.setSelEventId(evaluateForm.getEvalEventId());
		searchForAttendeeForm.setSelCourse(evaluateForm.getEvalCourse());
		searchForAttendeeForm.setSelCourseId(evaluateForm.getEvalCourseId());
		searchForAttendeeForm.setFilterClassroom(evaluateForm
				.getFilterClassroom());

		Integer eventId = evaluateForm.getEvalEventId();
		Integer courseId = evaluateForm.getEvalCourseId();
		String filterClassroom = evaluateForm.getFilterClassroom();
		// Get Pending SCES/Attendees For This Course
		// SCEReport[] objSCEReportArr =
		// sceManager.getPendingAttendeesByCourseAndClass(course,
		// filterClassroom);
		SCEReport[] objSCEReportArr = sceManager
				.getPendingAttendeesByCourseAndClass(eventId, courseId,
						filterClassroom);
		req.setAttribute("sceReports", objSCEReportArr);
		req.setAttribute("searchForAttendeeForm", searchForAttendeeForm);
		return new String("success");
	}

	public String selectAnotherAttendee() {
		HttpServletRequest req = getServletRequest();

		evaluateForm.setEvalEmplId(req.getParameter("emplId"));
		try {
			setData(req, evaluateForm, false);
		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			return new String("failure");
		}
		return new String("success");
	}

	private void setData(HttpServletRequest req, EvaluateForm form,
			boolean setAttendee) throws SCEException {
		SCE objSCE = null;

		if (objSCE == null) {
			form.setMode(SCEConstants.CREATE_MODE);
			objSCE = sceManager.getNewSCE(form.getEvalEventId(),
					SCEConstants.EVENT_COURSE_ALL, form.getEvalProduct());

		} else {
			form.setMode(SCEConstants.VIEW_MODE);
		}

		// Get All The Other Attendees Based on Search Criteria
		String trainingDateStr = form.getEvalTrainingDate();

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date trainingDate = null;
		try {
			trainingDate = (Date) formatter.parse(trainingDateStr);
		} catch (ParseException e) {

		}

		// Attendee[] attendees = sceManager.getPendingAttendees(trainingDate,
		// form.getEvalProductCode(), form.getEvalCourse(),
		// form.getEvalClassroom(), form.getEvalTable(), form.getEvalEmplId());
		// Attendee[] attendees =
		// sceManager.getPendingAttendees(form.getEvalEventId(), trainingDate,
		// form.getEvalProductCode(), form.getEvalCourse(),
		// form.getEvalEmplId());
		Attendee[] attendees = sceManager.getPendingAttendeesByFullcriteria(
				form.getEvalEventId(), trainingDate, form.getEvalProductCode(),
				form.getEvalCourse(), form.getEvalClassroom(),
				form.getEvalTable(), form.getEvalEmplId());
		// System.out.println("attendees" + attendees.length);
		if (setAttendee) {
			// Get The First Attendee
			if (attendees != null && attendees.length > 0) {
				form.setEvalEmplId(attendees[0].getEmplId());
				attendees = removeFirstAttendee(attendees);
			}
		}
		req.setAttribute("sceToDisplay", objSCE);
		req.setAttribute("from", "evaluate");
		req.setAttribute("attendees", attendees);
	}

	private Attendee[] removeFirstAttendee(Attendee[] attendees) {
		Attendee[] attendeesNew = new Attendee[attendees.length - 1];

		for (int i = 0; i < attendees.length - 1; i++) {
			attendeesNew[i] = attendees[i + 1];
		}
		return attendeesNew;
	}

	public String backToSCEForm() {
		HttpServletRequest req = getServletRequest();
		try {
			setData(req, evaluateForm, true);
		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			return new String("failure");
		}

		if (evaluateForm.getEvalEmplId() != null) {
			return new String("success");
		} else {
			return new String("noAttendees");
		}

	}

	public String enterNewEvaluationReport() {
		HttpServletRequest req = getServletRequest();

		evaluateForm.setMode(SCEConstants.CREATE_MODE);
		try {
			SCE objSCE = sceManager.getNewSCE(evaluateForm.getEvalEventId(),
					SCEConstants.EVENT_COURSE_ALL,
					evaluateForm.getEvalProduct());

			/*
			 * evaluateForm.setEvalEmplId(form.getSelEmplId());
			 * evaluateForm.setEvalEventId(form.getSelEventId());
			 * evaluateForm.setEvalProductCode(form.getSelProductCode());
			 * evaluateForm.setEvalProduct(form.getSelProduct());
			 * evaluateForm.setEvalCourse(form.getSelCourse());
			 * evaluateForm.setEvalCourseId(form.getSelCourseId());
			 * 
			 * if ("".equals(form.getSelClassroom()))
			 * evaluateForm.setEvalClassroom(null); else
			 * evaluateForm.setEvalClassroom(form.getSelClassroom());
			 * 
			 * if ("".equals(form.getSelTable()))
			 * evaluateForm.setEvalTable(null); else
			 * evaluateForm.setEvalTable(form.getSelTable());
			 * 
			 * evaluateForm.setFilterClassroom(form.getFilterClassroom());
			 */

			req.setAttribute("isSubmitted", "No");
			req.setAttribute("sceToDisplay", objSCE);
			req.setAttribute("from", "report");
		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");

			return new String("failure");
		}
		return new String("success");
	}

	public String submitSCE() {
		HttpServletRequest req = getServletRequest();
		try {
			User user = (User) req.getSession().getAttribute("user");
			SCE objSCE = new SCE();
			 //SCEControlImpl sceControl = new SCEControlImpl();
			 String emplid=null;

			String from = req.getParameter("from");
			System.out.println("user's emplid:"+user.getEmplId());

			// Get The Template Version
			Integer templateVersionId = new Integer(
					req.getParameter("template_version_id"));
			// String overallScore = new
			// String(req.getParameter("overall_score"));

			/* Precall */
			String precall_rating = null;
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("precall_rating"))) {
				precall_rating = req.getParameter("precall_rating").trim();
			}

			String precall_comments = null;
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("precall_comments"))) {
				precall_comments = req.getParameter("precall_comments").trim();
			} else if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("precall_comments_def"))) {
				precall_comments = req.getParameter("precall_comments_def")
						.trim();
			}

			/* Precall */
			String postcall_rating = null;
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("postcall_rating"))) {
				postcall_rating = req.getParameter("postcall_rating").trim();
			}
			String postcall_comments = null;
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("postcall_comments"))) {
				postcall_comments = req.getParameter("postcall_comments")
						.trim();
			} else if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("postcall_comments_def"))) {
				postcall_comments = req.getParameter("postcall_comments_def")
						.trim();
			}
			// System.out.println("overall_rating**********************:"+req.getParameter("overall_rating_H"));
			String overall_rating = null;
			if (req.getParameter("overall_rating_H") != null) {
				overall_rating = req.getParameter("overall_rating_H").trim();
			} else {
				overall_rating = req.getParameter("overall_rating_H").trim();
			}
			String overall_comments = null;
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("overall_comments"))) {
				overall_comments = req.getParameter("overall_comments").trim();
			}

			String comments = null;
			if (SCEUtils
					.isFieldNotNullAndComplete(req.getParameter("comments"))) {
				comments = req.getParameter("comments").trim();
			}

			String precall_na = null;
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("precall_checkbox"))) {
				precall_na = req.getParameter("precall_checkbox").trim();
			}

			String postcall_na = null;
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("postcall_checkbox"))) {
				postcall_na = req.getParameter("postcall_checkbox").trim();
			}

			String attendeeFirstName = null;
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("attendee_firstName"))) {
				attendeeFirstName = req.getParameter("attendee_firstName")
						.trim();
			} else {
				attendeeFirstName = "{First Name Unavailable}";
			}

			String attendeeLastName = null;
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("attendee_lastName"))) {
				attendeeLastName = req.getParameter("attendee_lastName").trim();
			} else {
				attendeeLastName = "{Last Name Unavailable}";
			}

			String formTitle = null;
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("form_title"))) {
				formTitle = req.getParameter("form_title").trim();
			} else {
				formTitle = "{Form Title Unavailable}";
			}

			String evaluatorName = null;
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("evaluator_name"))) {
				evaluatorName = req.getParameter("evaluator_name").trim();
			} else {
				evaluatorName = "{Evaluator Name Unavailable}";
			}

			objSCE.setEventId(evaluateForm.getEvalEventId());
			objSCE.setEmplId(evaluateForm.getEvalEmplId());
			objSCE.setProductCode(evaluateForm.getEvalProductCode());
			objSCE.setProduct(evaluateForm.getEvalProduct());
			objSCE.setCourse(evaluateForm.getEvalCourse());
			objSCE.setClassroom(evaluateForm.getEvalClassroom());
			objSCE.setTableName(evaluateForm.getEvalTable());

			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
			Date trainingDate = null;
			String sysdate = formatter.format(currentDate.getTime());
			try {
				trainingDate = (Date) formatter.parse(evaluateForm
						.getEvalTrainingDate());
			} catch (ParseException e) {
				// sceLogger.error("Training Date Parse:" +
				// form.getEvalTrainingDate());
			} catch (Exception e) {
				// sceLogger.error("Training Date Parse:" +
				// form.getEvalTrainingDate());
			}

			objSCE.setTemplateVersionId(templateVersionId);
			objSCE.setPrecallRating(precall_rating);
			objSCE.setPrecallComments(precall_comments);
			objSCE.setPostcallRating(postcall_rating);
			objSCE.setPostcallComments(postcall_comments);
			objSCE.setOverallRating(overall_rating);
			objSCE.setOverallComments(overall_comments);
			objSCE.setComments(comments);
			// objSCE.setHlcCompliant(hlcCompliant);
			// objSCE.setReviewed(reviewed);
			/* Start: AGARWN21: For retrieving employee id 2021 Bug fix release*/
			//Start of Nishtha
			emplid= sceManager.getempbyNTID1(user.getNtid() );
			objSCE.setSubmittedByEmplId(emplid);
			//End of Nishtha
			objSCE.setStatus(SCEConstants.ST_SUBMITTED);
			objSCE.setPostcallNA(postcall_na);
			objSCE.setPrecallNA(precall_na);
			/* 2020 Q3: Start of MUZEES for multiple evalution */
			Attendee evalAttendee = sceManager.getAttendeeByEmplId(evaluateForm
					.getEvalEmplId());
			SCE checkSCE = sceManager.getLatestSCE(
					evaluateForm.getEvalEmplId(),
					evaluateForm.getEvalEventId(),
					evaluateForm.getEvalProductCode(),
					SCEConstants.ST_SUBMITTED);
			if (checkSCE != null && sceManager.checkduplicate(checkSCE, objSCE)) {
				req.setAttribute("from", from);
				req.setAttribute("highlightSelProd", objSCE.getProductCode());
				req.setAttribute("msg",
						"Your Evaluation for " + evalAttendee.getName()
								+ " is already submitted.");
				return new String("success");
			}// end of MUZEES

			// Get Questions and Iterate To Get The Values
			SCEDetail[] objSCEDetailArr = sceManager
					.getSCEDetailsByTemplateVersionId(templateVersionId);
			String question_rating = null;
			String question_comments = null;
			String question_fg = null;
			for (int i = 0; i < objSCEDetailArr.length; i++) {
				if (SCEUtils.isFieldNotNullAndComplete(req
						.getParameter("question_rating_"
								+ objSCEDetailArr[i].getQuestionId()))) {
					question_rating = req.getParameter(
							"question_rating_"
									+ objSCEDetailArr[i].getQuestionId())
							.trim();
				} else {
					question_rating = null;
				}
				if (SCEUtils.isFieldNotNullAndComplete(req
						.getParameter("question_comments_"
								+ objSCEDetailArr[i].getQuestionId()))) {
					question_comments = req.getParameter(
							"question_comments_"
									+ objSCEDetailArr[i].getQuestionId())
							.trim();
				} else {
					question_comments = null;
				}

				if (SCEUtils.isFieldNotNullAndComplete(req
						.getParameter("precall_checkbox_"
								+ objSCEDetailArr[i].getQuestionId()))) {
					question_fg = req.getParameter(
							"precall_checkbox_"
									+ objSCEDetailArr[i].getQuestionId())
							.trim();
				} else {
					question_fg = null;
				}

				objSCEDetailArr[i].setQuestionRating(question_rating);
				objSCEDetailArr[i].setQuestionComments(question_comments);
				objSCEDetailArr[i].setQuestionFg(question_fg);
			}

			// Updale Legal Question Values
			LegalQuestion[] legalDetailArr = sceManager
					.getLegalQuestion(templateVersionId);
			String legalQuestionValue = null;

			for (int i = 0; i < legalDetailArr.length; i++) {
				if (SCEUtils.isFieldNotNullAndComplete(req
						.getParameter("healthcare_compliant_"
								+ legalDetailArr[i].getDisplayOrder()))) {
					legalQuestionValue = req.getParameter(
							"healthcare_compliant_"
									+ legalDetailArr[i].getDisplayOrder())
							.trim();
				} else {
					legalQuestionValue = null;
				}

				legalDetailArr[i].setLegalQuestionValue(legalQuestionValue);
			}

			sceManager.saveSCE(objSCE, objSCEDetailArr, legalDetailArr);

			Attendee[] attendees = null;
			if ("evaluate".equalsIgnoreCase(from)) {
				// Get Pending Attendees
				attendees = sceManager.getPendingAttendeesByFullcriteria(
						evaluateForm.getEvalEventId(), trainingDate,
						evaluateForm.getEvalProductCode(),
						evaluateForm.getEvalCourse(),
						evaluateForm.getEvalClassroom(),
						evaluateForm.getEvalTable(),
						evaluateForm.getEvalEmplId());
			}
			req.setAttribute("highlightSelProd", objSCE.getProductCode());
			req.setAttribute("from", from);
			req.setAttribute("attendees", attendees);
			req.setAttribute("msg",
					"Your Evaluation for " + evalAttendee.getName()
							+ " has been submitted successfully.");
			/* Author: Mayank Date:27-Oct-2011 SCE Enhancement 2011 */
			/* Send Email Start */

			String location = req.getRequestURL().toString().toLowerCase();

			EmailTemplate emailTemplate = sceManager.getEmailTemplate(
					templateVersionId, overall_rating);
			EvaluationFormScore evaluationFormValues = sceManager
					.getEvaluationFormValues(templateVersionId, overall_rating);

			String notificationFG = "N";
			String lmsFG = "";
			if (evaluationFormValues != null) {
				notificationFG = evaluationFormValues.getNotificationFG();
				lmsFG = evaluationFormValues.getLmsFlag();
			}

			if (emailTemplate != null && notificationFG.equals("Y")) {
				if (emailTemplate.getEmailCc() != null) {
					emailTemplate.setEmailCc(emailTemplate.getEmailCc().concat(
							",SCEAdmin@pfizer.com"));
				} else {
					emailTemplate.setEmailCc("SCEAdmin@pfizer.com");
				}

				User managerDetails = sceManager.getManagerDetail(objSCE
						.getEmplId());
				String managerName = null;
				String[] emailTo = new String[1];

				if (managerDetails != null) {
					if (SCEUtils.isFieldNotNullAndComplete(managerDetails
							.getFirstName())
							&& SCEUtils
									.isFieldNotNullAndComplete(managerDetails
											.getLastName())) {
						managerName = managerDetails.getFirstName() + " "
								+ managerDetails.getLastName();
					} else if (SCEUtils
							.isFieldNotNullAndComplete(managerDetails
									.getLastName())) {
						managerName = managerDetails.getLastName();
					} else if (SCEUtils
							.isFieldNotNullAndComplete(managerDetails
									.getFirstName())) {
						managerName = managerDetails.getFirstName();
					} else if (SCEUtils
							.isFieldNotNullAndComplete(managerDetails.getNtid())) {
						managerName = managerDetails.getNtid();
					} else {
						managerName = "Manager";
					}

					if (SCEUtils.isFieldNotNullAndComplete(managerDetails
							.getEmail())) {
						emailTo[0] = managerDetails.getEmail();
					} else {
						emailTo[0] = "SCEAdmin@Pfizer.Com";
						managerName = "Admin";
					}
				} else {
					managerName = "Admin";
					emailTo[0] = "SCEAdmin@Pfizer.Com";
				}

				// Hardcoding 'To_Email' so that email is not sent to business
				// users
				// Remove comments if running code in environments other than
				// production
				// emailTo[0] = emailTo[0].concat("Test");

				String[] emailBCC = null;
				String[] emailCC = null;

				boolean isLocal = location.indexOf("localhost") > -1;
				boolean isDev = location.indexOf("sce-dev.pfizer.com") > -1;
				boolean isIntegration = location.indexOf("sce-tst.pfizer.com") > -1;
				boolean isStaging = location.indexOf("sce-stg.pfizer.com") > -1;
				boolean isProduction = location.indexOf("sce.pfizer.com") > -1;
				// if (notificationFG.equals("Y")){
				String emailFrom = "SCEAdmin@pfizer.com";

				if (emailTemplate.getEmailCc() != null) {
					emailCC = emailTemplate.getEmailCc().split("[,;]");
					// trim CC
					for (int i = 0; i < emailCC.length; i++) {
						emailCC[i] = emailCC[i].trim();
					}
				} else {
					emailTemplate.setEmailCc("SCEAdmin@pfizer.com");
				}

				if (emailTemplate.getEmailBCc() != null) {
					emailBCC = emailTemplate.getEmailBCc().split("[,;]");
					for (int i = 0; i < emailBCC.length; i++) {
						emailBCC[i] = emailBCC[i].trim();
					}
				}

				String emailSubject = emailTemplate.getEmailSubject();
				String emailMessage = emailTemplate.getEmailBody();
				emailSubject = emailSubject.replaceAll("%27", "\'");
				emailSubject = emailSubject.replaceAll("&quote;", "\"");
				emailMessage = emailMessage.replaceAll("%0D%0A", "\r\n");
				emailMessage = emailMessage.replaceAll("%27", "\'");
				emailMessage = emailMessage.replaceAll("&quote;", "\"");
				String mimetype = "text/html";
				String mailJNDI = "java:jboss/SCEMailSession";

				// replace emial tags with the actual values.
				emailMessage = emailMessage.replaceAll("\\$\\(First Name\\)",
						attendeeFirstName);
				emailMessage = emailMessage.replaceAll("\\$\\(Last Name\\)",
						attendeeLastName);
				emailMessage = emailMessage.replaceAll(
						"\\$\\(Overall Score\\)", overall_rating);
				emailMessage = emailMessage.replaceAll("\\$\\(Course Name\\)",
						objSCE.getProduct());
				emailMessage = emailMessage.replaceAll(
						"\\$\\(Evaluation Template Name\\)", formTitle);
				emailMessage = emailMessage.replaceAll("\\$\\(Evaluator\\)",
						evaluatorName);
				emailMessage = emailMessage.replaceAll(
						"\\$\\(Date of Evaluation\\)", sysdate);
				emailMessage = emailMessage.replaceAll(
						"\\$\\(Manager's Name\\)", managerName);

				try {
					if (isLocal || isDev || isIntegration || isStaging) {

						emailFrom = "Divya.Menon@pfizer.com";
						emailTo[0] = "Sanjeev.Verma@pfizer.com";
						// emailTemplate.setEmailCc(null);

						if (emailCC != null) {
							for (int i = 0; i < emailCC.length; i++) {
								if (emailCC[i]
										.equalsIgnoreCase("SCEAdmin@pfizer.com")) {
									emailCC[i] = "Simi.Sudhakaran@pfizer.com";
								}
							}

							// emailCC[0]="Sanjeev.Verma@pfizer.com";
							// emailCC[1]="Simi.Sudhakaran@pfizer.com";
						}
						// emailBCC[0]="pankaj.gadale@pfizer.com";
						// emailBCC="";
					}

					MailUtil.sendMessage(emailFrom, emailTo, emailCC, emailBCC,
							emailSubject, emailMessage, mimetype, mailJNDI);
					// System.out.println("Email Method Called");
				} catch (AddressException ae) {
					req.setAttribute("errorMsg", "error.sce.unknown");
					// sceLogger.error(LoggerHelper.getStackTrace(e));
					// Modified by Mahua 28Nov2011
					// sceLogger.error(LoggerHelper.getStackTrace(ae));
					return new String("failure");
				} catch (MessagingException me) {
					req.setAttribute("errorMsg", "error.sce.unknown");
					// sceLogger.error(LoggerHelper.getStackTrace(e));
					// Modified by Mahua 28Nov2011
					// sceLogger.error(LoggerHelper.getStackTrace(me));
					return new String("failure");
				}
			}
			/* Send Email End */

		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");
	}

	public String viewPDFEvaluationFromHistory() {
		HttpServletRequest req = getServletRequest();
		// EvaluateForm evaluateForm = new EvaluateForm();
		try {
			HttpSession session = req.getSession(false);
			// Integer sceId = form.getSelSceId();
			// String sceId=(String)req.getParameter("sceId");
			// Integer sceId = (Integer)session.getAttribute("sceID");
			Integer sceId = evaluateForm.getSelSceId();

			SCE objSCE = sceManager.getSCEById(sceId);
			session.setAttribute("sceID", sceId);
			if (sceId == null) {
				sceId = new Integer(req.getParameter("sceId"));
			}
			if (objSCE.getUploadedDate() != null) {
				return new String("success2");
			}

			req.setAttribute("view", "view");

			evaluateForm.setMode(SCEConstants.VIEW_MODE);
			// Get The SCE

			// Get and Set the Details
			SCEDetail[] objSCEDetailArr = sceManager
					.getSCEDetailsBySCEId(objSCE.getId());

			if (objSCEDetailArr != null) {

				for (int i = 0; i < objSCEDetailArr.length; i++) {
					objSCE.addSceDetailList(objSCEDetailArr[i]);
				}
			}

			evaluateForm.setEvalEmplId(objSCE.getEmplId());

			/* Preserve Search For Back */
			/*
			 * evaluateForm.setEvalEventId(form.getSelEventId());
			 * evaluateForm.setLastNameSrch(form.getLastName());
			 * evaluateForm.setFirstNameSrch(form.getFirstName());
			 * evaluateForm.setEmplIdSrch(form.getEmplId());
			 * evaluateForm.setEventIdSrch(form.getEventId());
			 * evaluateForm.setTerritoryIdSrch(form.getSalesPositionId());
			 * evaluateForm.setIsPassportSrch(form.getIsPassport());
			 * evaluateForm.setSelEmplIdSrch(form.getSelEmplId());
			 */

			req.setAttribute("sceToDisplay", objSCE);
			req.setAttribute("from", "search");
			// }catch(SCEException scee){
			// req.setAttribute("errorMsg",scee.getErrorCode());
			// return new Forward("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");
	}

	public static void main(String args[]) {
		System.out.print("Evaluation Action");
	}

	public String uploadEvaluation() {
		HttpServletRequest req = getRequest();
		// System.out.println("Inside upload**********: ");
		try {
			User user = (User) req.getSession().getAttribute("user");
			String product = req.getParameter("product");

			EvaluationForm evaluationForm = new EvaluationForm();

			evaluationForm.setScore(evaluateForm.getScore());
			// evaluateForm.setEmplid(Integer.parseInt(user.getEmplId()));
			evaluationForm.setUploadTime(evaluateForm.getEvaluationTime());
			String evaluationDate = evaluateForm.getEvaluationDate();

			String repEmplid = req.getParameter("repEmplid");
			// System.out.println("repEmplid****"+repEmplid);
			String eventid = req.getParameter("eventid");
			String templateVersionId = req.getParameter("templateVersionId");

			/*
			 * if(templateVersionId != null){
			 * 
			 * Integer tempVersionId = Integer.parseInt(templateVersionId);
			 * scoreList =
			 * sceManager.getAllScores(Integer.parseInt(templateVersionId));
			 * version = sceManager.fetchVersionNumber(tempVersionId);
			 * 
			 * }
			 */

			String productcode = req.getParameter("productcode");
			product = sceManager.getProductName(productcode);
			evaluationForm.setProduct(product.trim());

			Integer sceId = null;
			// if
			// (SCEUtils.isFieldNotNullAndComplete(req.getParameter("sceId"))) {
			// sceId = new Integer(req.getParameter("sceId"));
			// }
			sceId = evaluateForm.getSelSceId();

			SimpleDateFormat formatter = new SimpleDateFormat(
					SCEUtils.DEFAULT_DATETIME_FORMAT);
			Date evaluatedDate = null;
			Calendar date = Calendar.getInstance();
			Date uploadDate = date.getTime();
			// String dateNow = formatter.format(currentDate.getTime());

			evaluationDate = evaluationDate + " "
					+ evaluateForm.getEvaluationTime();
			try {
				evaluatedDate = (Date) formatter.parse(evaluationDate);
				// System.out.println("evaluatedDate*****"+evaluatedDate);

			} catch (ParseException e) {
				// sceLogger.error("Training Date Parse:" + evaluationDate);
			}
			Integer eventId = new Integer(Integer.parseInt(eventid.trim()));
			Integer intTemplateVersionId = new Integer(
					Integer.parseInt(templateVersionId.trim()));
			// eventId=Integer.parseInt(eventid);

			evaluationForm.setEvaluationDate(evaluatedDate);
			evaluationForm.setUploadDate(uploadDate);
			evaluationForm.setRepEmplid(repEmplid.trim());
			evaluationForm.setEventId(eventId);
			evaluationForm.setTemplateVersionId(intTemplateVersionId);
			evaluationForm.setProductCode(productcode.trim());
			evaluationForm.setStatus(SCEConstants.SUBMITTED);

			// Get the size of the uploaded file.
			// int filesize = (evaluateForm.getContentUp());
			File myFile = evaluateForm.getContentUp();

			// String contentType = myFile.getContentType();

			// byte[] byteArr = new byte[filesize];

			byte[] fileData = new byte[(int) myFile.length()];
			try {
				FileInputStream fileInputStream = new FileInputStream(myFile);
				fileInputStream.read(fileData);
				fileInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			evaluationForm.setContent(fileData);

			// Blob blob = new SerialBlob(fileData);

			// Blob blob = null;
			// blob.setBytes(1, fileData)
			// System.out.println("contentType**********: ");

			// // System.out.println("contentType: " + contentType);
			// // System.out.println("File Name: " + fileName);
			// // System.out.println("File Size: " + fileSize);
			HttpSession session = req.getSession(false);
			/* 2020 Q3: Start of MUZEES for multiple evalution */
			boolean isDuplicate = false;
			SCE newSCE = new SCE();
			newSCE.setTemplateVersionId(evaluationForm.getTemplateVersionId());
			newSCE.setOverallRating(evaluationForm.getScore());
			newSCE.setSubmittedDate(evaluationForm.getEvaluationDate());
			SCE checkSCE = sceManager.getLatestSCE(
					evaluationForm.getRepEmplid(), evaluationForm.getEventId(),
					evaluationForm.getProductCode(), SCEConstants.ST_SUBMITTED);
			if (checkSCE != null && sceManager.checkduplicate(checkSCE, newSCE)) {
				isDuplicate = true;
			} else {
				sceManager.uploadForm(evaluationForm, sceId);
			}
			session.setAttribute("isDuplicate", isDuplicate);// end of MUZEES
			session.setAttribute("upload", "upload");
			session.setAttribute("product", product.trim());

			// session.setAttribute("msg1",
			// "File has been uploaded Successfully");
			// Blob blobData1 =sceManager.getByteArray();

			// try{
			// HttpSession session=req.getSession(false); //----
			// int length =(int)blobData1.length();
			// byte[] fileData1=blobData1.getBytes(1,length);
			// req.setAttribute("pen","pencil");
			// req.setAttribute("blob",blobData1);

			// --------
			// String contentType1 = fileData1.getContentType();
			// FileOutputStream fileOuputStream =
			// new FileOutputStream("C:\\testing5.pdf");
			// FileOutputStream fileOuputStream =
			// new FileOutputStream("C:\\testing5.pdf");
			// fileOuputStream.write(fileData1);

			// /File file = new File("C:\\testing5.pdf");

			// fileOuputStream.close();
			// }
			// catch(Exception e){

			// }

			// req.setAttribute("msg", "File has been uploaded successfully");
			// return new Forward("success1");
		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");
	}

	public String gotoEvaluationResults1() {
		HttpServletRequest req = getRequest();
		SearchForAttendeeForm form = new SearchForAttendeeForm();
		try {
			HttpSession session = req.getSession(false);
			// String emplId = form.getSelEmplId();
			// Integer eventId = form.getEventId();
			// String emplId =req.getParameter("emplId");
			String emplId = (String) session.getAttribute("emplId");
			Integer eventId = (Integer) session.getAttribute("eventId");
			Attendee evalAttendee = sceManager.getAttendeeByEmplId(emplId);
			if (session.getAttribute("isDuplicate") != null) {
				boolean isDuplicate = (Boolean) session
						.getAttribute("isDuplicate");
				if (isDuplicate) {
					req.setAttribute("msg", "Your Evaluation for "
							+ evalAttendee.getName() + " is already submitted.");
				} else
					req.setAttribute("msg", "Your Evaluation for "
							+ evalAttendee.getName()
							+ " has been submitted successfully.");
				session.removeAttribute("isDuplicate");
			}
			session.setMaxInactiveInterval(-1);

			form.setSelEmplId(emplId);
			form.setEventId(eventId);

			// Integer eventId = new Integer(req.getParameter("eventId"));
			// Get The SCES For This Empl Id
			// System.out.println("Event ID in gotoEvaluationResults"+eventId);
			// System.out.println("Empl ID in gotoEvaluationResults"+emplId);

			SCE[] objSCEArr = sceManager.getSCESByEventEmplId(eventId, emplId);
			// System.out.println("SCE Objects"+objSCEArr);
			req.setAttribute("sces", objSCEArr);
		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");
	}

	public String viewEvaluationFromHistory() {
		HttpServletRequest req = getRequest();
		// EvaluateForm evaluateForm = new EvaluateForm();
		try {
			HttpSession session = req.getSession(false);
			// Integer sceId = form.getSelSceId();
			// String sceId=(String)req.getParameter("sceId");
			// Integer sceId = (Integer)session.getAttribute("sceID");
			// Integer sceId =form.getSelSceId();

			Integer sceId = new Integer(req.getParameter("sceId"));
			String closeButtonStatus = req.getParameter("closeButton");// shinoy
																		// added
																		// 31st
																		// DEC
			SCE objSCE = sceManager.getSCEById(sceId);
			session.setAttribute("sceID", sceId);

			req.setAttribute("view", "view");
			evaluateForm.setMode(SCEConstants.VIEW_MODE);

			// Get The SCE

			// Get and Set the Details
			SCEDetail[] objSCEDetailArr = sceManager
					.getSCEDetailsBySCEId(objSCE.getId());
			// Mayank : 10-Nov-2011 : SCE Enhancement 2011
			EvaluationFormScore[] objEvaluationFormScoreArr = sceManager
					.getEvaluationFormScore(objSCE.getTemplateVersionId());
			LegalQuestion[] legalQuestionArr = sceManager
					.getLegalQuestion(objSCE.getTemplateVersionId());
			LegalQuestionDetail[] legalQuestionDetailArr = sceManager
					.getLegalQuestionDetail(sceId);

			if (objSCEDetailArr != null) {
				for (int i = 0; i < objSCEDetailArr.length; i++) {
					objSCE.addSceDetailList(objSCEDetailArr[i]);
				}
			}

			if (objEvaluationFormScoreArr != null) {
				for (int i = 0; i < objEvaluationFormScoreArr.length; i++) {
					objSCE.addEvaluationFormScoreList(objEvaluationFormScoreArr[i]);
				}
			}

			if (legalQuestionArr != null) {
				for (int i = 0; i < legalQuestionArr.length; i++) {
					objSCE.addLegalQuestionList(legalQuestionArr[i]);
				}
			}

			if (legalQuestionDetailArr != null) {
				for (int i = 0; i < legalQuestionDetailArr.length; i++) {
					objSCE.addLegalQuestionDetailList(legalQuestionDetailArr[i]);
				}
			}

			evaluateForm.setEvalEmplId(objSCE.getEmplId());

			/* Preserve Search For Back */
			/*
			 * evaluateForm.setEvalEventId(form.getSelEventId());
			 * evaluateForm.setLastNameSrch(form.getLastName());
			 * evaluateForm.setFirstNameSrch(form.getFirstName());
			 * evaluateForm.setEmplIdSrch(form.getEmplId());
			 * evaluateForm.setEventIdSrch(form.getEventId());
			 * evaluateForm.setTerritoryIdSrch(form.getSalesPositionId());
			 * evaluateForm.setIsPassportSrch(form.getIsPassport());
			 * evaluateForm.setSelEmplIdSrch(form.getSelEmplId());
			 */

			req.setAttribute("sceToDisplay", objSCE);
			req.setAttribute("from", "search");
			req.setAttribute("closeButtonStatus", closeButtonStatus);// shinoy
																		// added
																		// 31st
																		// DEC
		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");
	}

}
