package com.pfizer.sce.Action;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.sce.ActionForm.EvaluateForm;
import com.pfizer.sce.ActionForm.SCEEvaluateForm;
import com.pfizer.sce.beans.EmailTemplate;
import com.pfizer.sce.beans.EvaluationFormScore;
import com.pfizer.sce.beans.Event;
import com.pfizer.sce.beans.LegalConsentTemplate;
import com.pfizer.sce.beans.LegalQuestion;
import com.pfizer.sce.beans.LegalQuestionDetail;
import com.pfizer.sce.beans.SCE;
import com.pfizer.sce.beans.SCEComment;
import com.pfizer.sce.beans.SCEDetail;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.SCEinput;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.beans.UserLegalConsent;
import com.pfizer.sce.common.SCEConstants;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.utils.MailUtil;
import com.pfizer.sce.utils.SCEUtils;

public class EvaluateAttendeeAction extends ActionSupport implements
		ServletRequestAware, ModelDriven {

	private static SCEManagerImpl sceManager = new SCEManagerImpl();
	HttpServletRequest request;
	EvaluateForm evaluateForm = new EvaluateForm();

	SCEEvaluateForm sceEvaluateForm = new SCEEvaluateForm();

	public SCEEvaluateForm getSceEvaluateForm() {
		return sceEvaluateForm;
	}

	public void setSceEvaluateForm(SCEEvaluateForm sceEvaluateForm) {
		this.sceEvaluateForm = sceEvaluateForm;
	}

	public EvaluateForm getEvaluateForm() {
		return evaluateForm;
	}

	public void setEvaluateForm(EvaluateForm evaluateForm) {
		this.evaluateForm = evaluateForm;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	public HttpServletRequest getServletRequest() {
		return this.request;
	}

	public HttpSession getSession() {
		return request.getSession();
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String goToLegalCheckTr() {
		// System.out.println("Inside goToLegalCheckTr()...JPF");
		HttpServletRequest req = getServletRequest();
		HttpSession session = req.getSession();

		String linkName = "";
		String ntId = "";
		String loginUserNtId = "";
		String emplId = "";
		String productCode = "";
		String product = "";
		Integer eventId = null;

		User user = null;

		try {

			linkName = req.getParameter("linkName");
			ntId = req.getParameter("ntId");
			emplId = req.getParameter("emplId");
			productCode = req.getParameter("productCode");
			// product=req.getParameter("product");
			// System.out.println("SCE JSP:::actual product passed = "+product);
			eventId = new Integer(req.getParameter("eventId"));
			loginUserNtId = req.getParameter("loginUserNtId");

			if (loginUserNtId != null) {
				// System.out.println("Calling manager method to get the USER by NTID"+loginUserNtId);
				user = sceManager.getUserByNTId(loginUserNtId);
				if (user != null) {
					// System.out.println("User not null");
					// System.out.println("user id = "+user.getId());//This line
					// is for teting only
					if ((user.getStatus()).equals("ACTIVE")) {
						// Check for User Legal Consent acceptance
						// System.out.println("Inside ACTIVE");
						UserLegalConsent userLegalConsent = new UserLegalConsent();
						userLegalConsent = sceManager
								.checkLegalConsentAcceptance(loginUserNtId);
						product = sceManager.getProductName(productCode);

						if (userLegalConsent == null) {// USer has not yet
														// accepted the Legal
														// Consent
							// System.out.println("Inside if(userLegalConsent==null)....");
							LegalConsentTemplate legalConsentTemplate = new LegalConsentTemplate();

							legalConsentTemplate = sceManager
									.fetchLegalContent();

							req.setAttribute("content",
									legalConsentTemplate.getContent());
							req.setAttribute("forLcid", legalConsentTemplate);
							req.setAttribute("linkName", linkName);
							req.setAttribute("loginUserNtId", loginUserNtId);
							req.setAttribute("ntId", ntId);
							req.setAttribute("emplId", emplId);
							req.setAttribute("productCode", productCode);
							req.setAttribute("product", product);
							req.setAttribute("eventId", eventId);
							req.setAttribute("evaluatorEmplId",
									user.getEmplId());

							return "redirectToLc";
						} else { // userLegalConsent!=null => User has already
									// accepted legal consent
							req.setAttribute("linkName", linkName);
							req.setAttribute("loginUserNtId", loginUserNtId);
							req.setAttribute("product", product);
							req.setAttribute("eventId", eventId);
							req.setAttribute("productCode", productCode);
							req.setAttribute("eventId", eventId);
							req.setAttribute("evaluatorEmplId",
									user.getEmplId());

							return "redirectToEvaluate";
						}// End if-else for Legal Consent
					} else {
						return "unauthorizedSCEUser";
					}// End if for user status ACTIVE
				} else {// End if for user!=null
					return "unauthorizedSCEUser";
				}
			}// End if(ntId!=null)
			else {// (ntId==null)
				return new String("unauthorizedSCEUser");
			}

		}// End try
		catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		} // End catch

	}// End method

	public String evaluateSCE() {
		HttpServletRequest req = getServletRequest();

		String emplId = req.getParameter("emplId");
		String activityId = req.getParameter("productCode");
		String evaluatorEmplId = req.getParameter("evaluatorEmplId");
		String empName = req.getParameter("EmplName");
		String evaluatorEmplName = req.getParameter("evaluatorEmplName");
		String isFlag = req.getParameter("flag");
		String isSAdmin = req.getParameter("superAdmin");
		String trackId = req.getParameter("trackId");
		HttpSession session = getSession();

		/* Getting the SCE ID */
		String sceId = null;

		try {
			sceId = sceManager.getSceFeedbackId(trackId);
		} catch (SCEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println("SCE ID"+sceId);

		if (emplId != null && activityId != null && evaluatorEmplId != null
				&& empName != null && evaluatorEmplName != null
				&& isFlag != null && isSAdmin != null) {
			SCEinput sceinput = new SCEinput();

			sceinput.setEmplId(emplId);
			sceinput.setEmpName(empName);
			sceinput.setEvalId(evaluatorEmplId);
			sceinput.setEvalName(evaluatorEmplName);
			sceinput.setActivityId(activityId);
			sceinput.setFlag(isFlag);
			sceinput.setIsSAdmin(isSAdmin);
			sceinput.setTrackId(trackId);
			sceinput.setSceId(sceId);

			req.setAttribute("Input", sceinput);
			session.setAttribute("Input", sceinput);
		}

		if (emplId != null && trackId != null) {
			SCEComment[] SCEcomment = null;

			try {
				SCEcomment = sceManager.getSCEComment(trackId, emplId);
			} catch (SCEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// System.out.println("Submitted"+SCEcomment[0].getSubmittedBY());
			req.setAttribute("ObjectComment", SCEcomment);
			session.setAttribute("ObjectComment", SCEcomment);
			req.setAttribute("sceToDisplay", SCEcomment);
		}
		session.setAttribute("successmessage", "");

		return new String("success");
	}

	public String submitSCEEvaluate() {
		System.out.println("Inside function ##");
		HttpServletRequest req = getServletRequest();
		DateFormat dateFormatter = new SimpleDateFormat(
				SCEUtils.DEFAULT_DATE_FORMAT);
		DateFormat dateFormatter1 = new SimpleDateFormat("MMM d, yyyy");
		String currentDate = null;
		currentDate = dateFormatter1.format(new java.util.Date());
		System.out.println("System date" + currentDate);
		HttpSession session1 = getSession();
		session1.setAttribute("successmessage", "");

		/*
		 * String activityId = sceEvaluateForm.getActivityId(); String repId =
		 * sceEvaluateForm.getReprenstativeName(); String phaseNo=
		 * sceEvaluateForm.getPhaseNo(); String comment1 =
		 * sceEvaluateForm.getComment1(); String comment2 =
		 * sceEvaluateForm.getComment2(); String comment3 =
		 * sceEvaluateForm.getComment3(); String trackId=
		 * sceEvaluateForm.getTrackId(); String
		 * sceId=sceEvaluateForm.getSceId();
		 */

		String activityId = req.getParameter("activityId");
		String repId = req.getParameter("reprenstativeName");
		String phaseNo = req.getParameter("phaseNo");
		String comment1 = req.getParameter("comment1");
		String comment2 = req.getParameter("comment2");
		String comment3 = req.getParameter("comment3");
		String trackId = req.getParameter("trackId");
		String sceId = req.getParameter("sceId");

		String submitted_by = null;
		String submitted_date = null;
		String flag = "N";

		/* This is to set submitted by information */
		flag = req.getParameter("submitFlag");
		;
		System.out.println("Flag =====" + flag);
		if (flag.equals("Y")) {
			/* submitted_by = sceEvaluateForm.getEvaluatorName(); */
			submitted_by = req.getParameter("evaluatorName");
			submitted_date = dateFormatter1.format(new java.util.Date());
		}

		Integer phaseCount = 0;
		try {
			phaseCount = sceManager.isSCEComment(phaseNo, sceId, repId);
		} catch (SCEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Phase Count==" + phaseCount);
		SCEComment sceExistingcomment = null;
		/* Added to fix the Admin bug */
		if (phaseCount.intValue() == 1) {

			try {
				sceExistingcomment = sceManager.getExistingSCEComment(phaseNo,
						sceId, repId);
			} catch (SCEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String date1 = req.getParameter("date1");
		/*
		 * if(date1 !=null && !date1.equalsIgnoreCase(currentDate)) {
		 * date1=dateFormatter1.format(new Date()); }
		 */
		String date2 = req.getParameter("date2");
		/*
		 * if(date2 !=null && !date2.equalsIgnoreCase(currentDate)) {
		 * date2=dateFormatter1.format(new Date()); }
		 */
		String date3 = req.getParameter("date3");
		/*
		 * if(date3 !=null && !date3.equalsIgnoreCase(currentDate)) {
		 * date3=dateFormatter1.format(new Date()); }
		 */
		String enteredby1 = req.getParameter("enteredBy1");
		/*
		 * if(enteredby1 !=null &&
		 * !enteredby1.equalsIgnoreCase(form.getEvaluatorName())) {
		 * enteredby1=form.getEvaluatorName(); }
		 */
		String enteredby2 = req.getParameter("enteredBy2");
		/*
		 * if(enteredby2 !=null &&
		 * !enteredby2.equalsIgnoreCase(form.getEvaluatorName())) {
		 * enteredby2=form.getEvaluatorName(); }
		 */
		String enteredby3 = req.getParameter("enteredBy3");
		/*
		 * if(enteredby3 !=null &&
		 * !enteredby3.equalsIgnoreCase(form.getEvaluatorName())) {
		 * enteredby3=form.getEvaluatorName(); }
		 */

		if (comment1.equals("Comments") && comment2.equals("Comments")
				&& comment3.equals("Comments")) {
			System.out.println("No Updates to the database");
		} else {
			if (comment1.equals("Comments")) {
				comment1 = null;
				date1 = null;
				enteredby1 = null;
				System.out.println("First Comment not entered");
			} else {
				if (((date1 == null || date1.equals("null")) && (enteredby1 == null || enteredby1
						.equals("null")))
						|| (!comment1.equalsIgnoreCase(sceExistingcomment
								.getComment1()))) {
					date1 = dateFormatter1.format(new java.util.Date());
					enteredby1 = req.getParameter("evaluatorName");

				} else {
					System.out.println("Comment3 already exist");
				}
			}
			if (comment2.equals("Comments")) {
				comment2 = null;
				enteredby2 = null;
				date2 = null;
				System.out.println("Second Comment not entered");
			} else {
				if (((date2 == null || date2.equals("null")) && (enteredby2 == null || enteredby2
						.equals("null")))
						|| (!comment2.equalsIgnoreCase(sceExistingcomment
								.getComment2()))) {
					date2 = dateFormatter1.format(new java.util.Date());
					enteredby2 = req.getParameter("evaluatorName");

				} else {
					System.out.println("Comment3 already exist");
				}
			}
			if (comment3.equals("Comments")) {
				comment3 = null;
				enteredby3 = null;
				date3 = null;
				System.out.println("Third Comment not entered");
			} else {

				if (((date3 == null || date3.equals("null")) && (enteredby3 == null || enteredby3
						.equals("null")))
						|| (!comment3.equalsIgnoreCase(sceExistingcomment
								.getComment3()))) {
					date3 = dateFormatter1.format(new java.util.Date());
					enteredby3 = req.getParameter("evaluatorName");

				} else {
					System.out.println("Comment3 already exist");
				}
			}

			if (phaseCount.intValue() == 1) {
				System.out.println("1");
				try {
					sceManager.updateSCEComment(sceId, activityId, phaseNo,
							repId, comment1, comment2, comment3, enteredby1,
							enteredby2, enteredby3, date1, date2, date3,
							submitted_by, submitted_date);
				} catch (SCEException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				session1.setAttribute("successmessage",
						"Data updated successfully");
			} else {
				try {
					sceManager.insertSCEComment(sceId, activityId, phaseNo,
							repId, comment1, comment2, comment3, enteredby1,
							enteredby2, enteredby3, date1, date2, date3,
							submitted_by, submitted_date);
				} catch (SCEException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				session1.setAttribute("successmessage",
						"Data updated successfully");
			}

		}
		if (repId != null && activityId != null) {
			SCEComment[] SCEcomment = null;
			try {
				SCEcomment = sceManager.getSCEComment(trackId, repId);
			} catch (SCEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpSession session = getSession();
			session.setAttribute("ObjectComment", SCEcomment);
			req.setAttribute("sceToDisplay", SCEcomment);
		}
		return new String("success");

	}

	public String goToHistoryCheckTr() {

		// System.out.println("Inside goToHistoryCheckTr()...JPF");
		HttpServletRequest req = getServletRequest();
		HttpSession session = req.getSession();

		User user = null;

		String linkName = "";
		String ntId = "";
		String loginUserNtId = "";
		String emplId = "";
		String productCode = "";
		String product = "";
		Integer eventId = null;
		SCE[] sce = null;

		try {
			Event[] events = sceManager.getAllEvents();
			req.setAttribute("events", events);
			linkName = req.getParameter("linkName");
			ntId = req.getParameter("ntId");
			emplId = req.getParameter("emplId");
			productCode = req.getParameter("productCode");
			product = sceManager.getProductName(productCode);
			eventId = new Integer(Integer.parseInt(req.getParameter("eventId")));
			loginUserNtId = req.getParameter("loginUserNtId");

			/* For DEBUGGING */
			// System.out.println("linkName = "+linkName);
			// System.out.println("ntId = "+ntId);
			// System.out.println("emplId = "+emplId);
			// System.out.println("productCode = "+productCode);
			// System.out.println("eventId = "+eventId);
			// System.out.println("loginUserNtId = "+loginUserNtId);
			/* End DEBUGGING */

			// Call method to get the SCE history object
			sce = sceManager.getSCEHistoryForTRT(eventId, emplId, productCode);

			// Legal Consent fetch
			UserLegalConsent userLegalConsent = new UserLegalConsent();
			userLegalConsent = sceManager
					.checkLegalConsentAcceptance(loginUserNtId);
			user = sceManager.getUserByNTId(loginUserNtId);

			if (sce != null) {// The history object is not null
				// System.out.println("sce object not null - Length of History record [] sce = "+sce.length);

				if (sce.length == 1) { // If there is only 1 history record
					if (user != null && user.getStatus().equals("ACTIVE")) {
						if (userLegalConsent == null) {// User legal consent
														// check
							LegalConsentTemplate legalConsentTemplate = new LegalConsentTemplate();
							legalConsentTemplate = sceManager
									.fetchLegalContent();
							// //
							// System.out.println("legalConsentTemplate.getContent() - "+
							// legalConsentTemplate.getContent());

							req.setAttribute("content",
									legalConsentTemplate.getContent());
							req.setAttribute("forLcid", legalConsentTemplate);
							req.setAttribute("linkName", linkName);
							req.setAttribute("ntId", ntId);
							req.setAttribute("productCode", productCode);
							req.setAttribute("eventId", eventId);
							req.setAttribute("loginUserNtId", loginUserNtId);
							req.setAttribute("emplId", emplId);
							req.setAttribute("sce", sce[0].getId());
							// System.out.println("sce in ="+sce[0].getId());
							return new String("checkForLcAcceptance");
						} else if (userLegalConsent != null) {
							req.setAttribute("sce", sce[0].getId());
							// System.out.println("sce out ="+sce[0].getId());
							return new String("redirectToSingleView");
						}
					} else {
						return new String("unauthorizedSCEUser");
					}

				}

				else if (sce.length > 1) {

					// LegalConsentTemplate legalConsentTemplate=new
					// LegalConsentTemplate();
					// legalConsentTemplate=sceManager.fetchLegalContent();
					// //
					// System.out.println("legalConsentTemplate.getContent() - "+
					// legalConsentTemplate.getContent());
					session.setAttribute("scesTR", sce);
					// req.setAttribute("content",legalConsentTemplate.getContent());
					// req.setAttribute("forLcid",legalConsentTemplate);
					req.setAttribute("linkName", linkName);
					req.setAttribute("loginUserNtId", loginUserNtId);
					req.setAttribute("ntId", ntId);
					req.setAttribute("emplId", emplId);
					req.setAttribute("productCode", productCode);
					req.setAttribute("eventId", eventId);
					return new String("redirectToHistory");
				}
			}// End if(sce!=null)
			return new String("failure");
		}// End try
		catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		} // End catch

	}// End method

	public String enterNewEvaluationReportfromTRT() {
		HttpServletRequest req = getServletRequest();
		try {
			String evaluatorEmplId = null;
			String emplId = req.getParameter("emplId");
			String strEventId = req.getParameter("eventId");
			// String product = req.getParameter("product");
			String product = (String) req.getAttribute("product");
			String productCode = req.getParameter("productCode");
			if (req.getParameter("evaluatorEmplId") != null) {
				evaluatorEmplId = req.getParameter("evaluatorEmplId");
			} else {
				evaluatorEmplId = (String) req.getAttribute("evaluatorEmplId");
			}
			// String action = req.getParameter("action");
			String strExamScore = req.getParameter("examScore");
			String evaluatorRole = req.getParameter("evaluatorRole");

			Integer eventId = new Integer(strEventId);
			boolean phasedTraining = false;
			Integer rowNum = null;
			Double examScore = null;

			if (eventId != null) {
				rowNum = sceManager.isPhaseTraining(eventId);
				if (rowNum != null && rowNum.intValue() > 0) {
					phasedTraining = true;
				}
			}

			// System.out.println("phasedTraining"+phasedTraining);

			if (phasedTraining) {
				examScore = new Double(strExamScore);
			}

			// EvaluateForm evaluateForm = new EvaluateForm();

			// evaluateForm
			evaluateForm.setEvalProductCode(productCode);
			evaluateForm.setEvalEmplId(emplId);
			evaluateForm.setEvalEventId(eventId);
			evaluateForm.setEvaluatorRole(evaluatorRole);

			SCE objSCE = null;

			evaluateForm.setMode(SCEConstants.CREATE_MODE);
			req.setAttribute("mode", SCEConstants.CREATE_MODE);

			if (phasedTraining) {
				// P2L Phased Training
				objSCE = sceManager.getLatestSCE(emplId, eventId, productCode,
						SCEConstants.ST_DRAFT);
				if (objSCE == null) {
					objSCE = sceManager.getNewSCE(eventId,
							SCEConstants.EVENT_COURSE_ALL, product);
					objSCE.setStatus(SCEConstants.ST_NEW);
				} else {
					objSCE.setStatus(SCEConstants.ST_DRAFT);
				}
				// Set Exam Score
				setExamScore(objSCE, examScore);
			} else {
				objSCE = sceManager.getNewSCE(eventId,
						SCEConstants.EVENT_COURSE_ALL, product);
				objSCE.setSubmittedByEmplId(evaluatorEmplId);
			}
			evaluateForm.setEvalProduct(product);
			evaluateForm.setEvaluatorEmplId(evaluatorEmplId);

			if (objSCE == null) {
				return new String("failure");
			}
			req.setAttribute("evalEmplId", evaluateForm.getEvalEmplId());
			req.setAttribute("sceToDisplay", objSCE);
			req.setAttribute("evalProduct", product);

			if (phasedTraining) {
				return new String("success2");
			} else {
				return new String("success1");
			}
		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
	}

	public String sceTRHistoryView() {
		// System.out.println("Inside sceTRHistoryView...JPF");
		HttpServletRequest req = getRequest();
		String linkName = req.getParameter("linkName");
		String loginUserNtId = req.getParameter("loginuserntid");
		String ntId = req.getParameter("ntId");
		String productCode = req.getParameter("productCode");
		// System.out.println("productCode ="+productCode);
		Integer eventId = new Integer(req.getParameter("eventId").trim());
		// System.out.println("eventId ="+eventId);
		Integer sce = new Integer(req.getParameter("sce").trim());
		// System.out.println("sce ="+sce);
		String emplId = req.getParameter("emplId");
		// System.out.println("emplId ="+emplId);
		String fwdPath = "";
		// System.out.println("loginUserNtId ="+loginUserNtId);
		// System.out.println("linkName ="+linkName);
		// System.out.println("ntId ="+ntId);
		// System.out.println("productCode ="+productCode);

		User user = null;

		try {
			user = sceManager.getUserByNTId(loginUserNtId);
			if (user != null && user.getStatus().equals("ACTIVE")) {
				UserLegalConsent userLegalConsent = new UserLegalConsent();
				userLegalConsent = sceManager
						.checkLegalConsentAcceptance(loginUserNtId);

				if (userLegalConsent == null) {// User legal consent check
					// System.out.println("Inside else if(userLegalConsent==null)...");
					LegalConsentTemplate legalConsentTemplate = new LegalConsentTemplate();
					legalConsentTemplate = sceManager.fetchLegalContent();
					// System.out.println("legalConsentTemplate.getContent() - "+
					// legalConsentTemplate.getContent());

					req.setAttribute("content",
							legalConsentTemplate.getContent());
					// System.out.println("content = "+legalConsentTemplate.getContent());
					req.setAttribute("forLcid", legalConsentTemplate);
					// System.out.println("legalConsentTemplate ="+legalConsentTemplate);
					req.setAttribute("linkName", linkName);
					// System.out.println("linkName ="+linkName);
					req.setAttribute("ntId", ntId);
					// System.out.println("ntId ="+ntId);
					req.setAttribute("productCode", productCode);
					// System.out.println("productCode ="+productCode);

					req.setAttribute("eventId", eventId);
					// System.out.println("eventId ="+eventId);
					req.setAttribute("loginUserNtId", loginUserNtId);
					req.setAttribute("emplId", emplId);
					// return new String("checkForLcAcceptance");
					// System.out.println("eventId ="+eventId);
					fwdPath = "checkForLcAcceptance";
					// System.out.println("eventId ="+eventId);

				} else if (userLegalConsent != null) {
					// System.out.println("Inside else if(userLegalConsent!=null)...");
					// return new String("redirectToSingleView");
					fwdPath = "redirectToSingleView";
				}
			} else {
				return new String("unauthorizedSCEUser");
			}

		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}

		return new String(fwdPath);
	}

	public String viewEvaluationTR() {
		HttpServletRequest req = getServletRequest();
		HttpSession session = req.getSession(false);
		// EvaluateForm evaluateForm = new EvaluateForm();
		try {
			// Integer sceId = new
			// Integer(Integer.parseInt(req.getParameter("sceid")));
			Integer sceId = new Integer(0);
			if (req.getParameter("sce") != null) {
				sceId = new Integer(req.getParameter("sce"));
				// System.out.println("sce ="+sceId);
			}

			if (req.getAttribute("sce") != null) {
				sceId = (Integer) req.getAttribute("sce");
			}
			// System.out.println("sce ="+sceId);

			evaluateForm.setMode(SCEConstants.VIEW_MODE);
			// System.out.println("HEy***n view mode");
			// Get The SCE
			SCE objSCE = sceManager.getSCEById(sceId);

			session.setAttribute("sceID", sceId);
			if (objSCE.getUploadedDate() != null) {
				return new String("success2");
			}
			session.removeAttribute("sceID");

			// System.out.println("HEy***" +objSCE.getTemplateVersionId());

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
			evaluateForm.setEvalProduct(objSCE.getProduct());
			evaluateForm.setEvaluatorEmplId(objSCE.getSubmittedByEmplId());
			evaluateForm.setEvaluatorRole("DM");

			req.setAttribute("evalEmplId", evaluateForm.getEvalEmplId());
			req.setAttribute("sceToDisplay", objSCE);
			req.setAttribute("from", "search");
			req.setAttribute("closeButtonStatus", "active");
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

	public String evaluateTR() {
		HttpServletRequest req = getServletRequest();
		try {
			String emplId = req.getParameter("emplId");
			String strEventId = req.getParameter("eventId");
			String product = req.getParameter("product");
			String productCode = req.getParameter("productCode");
			String evaluatorEmplId = req.getParameter("evaluatorEmplId");
			String action = req.getParameter("action");
			String strExamScore = req.getParameter("examScore");
			String evaluatorRole = req.getParameter("evaluatorRole");

			// System.out.println("EMPLID"+emplId);
			// System.out.println("EVALUATOR EMPLID"+evaluatorRole);

			/* Validation */
			if ("create".equalsIgnoreCase(action)) {
				if (!SCEUtils.isFieldNotNullAndComplete(emplId)
						|| !SCEUtils.isFieldNotNullAndComplete(strEventId)
						|| !SCEUtils.isFieldNotNullAndComplete(product)
						|| !SCEUtils.isFieldNotNullAndComplete(productCode)) {
					return new String("failure");
				}
			}

			if ("view".equalsIgnoreCase(action)) {
				if (!SCEUtils.isFieldNotNullAndComplete(emplId)
						|| !SCEUtils.isFieldNotNullAndComplete(strEventId)
						|| !SCEUtils.isFieldNotNullAndComplete(productCode)) {
					return new String("failure");
				}
			}

			Integer eventId = new Integer(strEventId);
			boolean phasedTraining = false;
			Integer rowNum = null;
			Double examScore = null;
			// boolean phasedTraining = (eventId != null && eventId.intValue()
			// >= 5 && eventId.intValue() <= 7);
			if (eventId != null) {
				rowNum = sceManager.isPhaseTraining(eventId);
				// System.out.println("------ROWNUM------"+rowNum);
				if (rowNum != null && rowNum.intValue() > 0) {
					phasedTraining = true;
				}
			}
			// Validation For Exam Score
			/*
			 * if (phasedTraining &&
			 * !SCEUtils.isFieldNotNullAndComplete(strExamScore)) { return new
			 * String("failure"); }
			 */
			// System.out.println("phasedTraining"+phasedTraining);

			if (phasedTraining) {
				try {
					examScore = new Double(strExamScore);
				} catch (Exception e) {
				}
			}
			// EvaluateForm evaluateForm = new EvaluateForm();

			// evaluateForm
			evaluateForm.setEvalProductCode(productCode);
			evaluateForm.setEvalEmplId(emplId);
			evaluateForm.setEvalEventId(eventId);
			evaluateForm.setEvaluatorRole(evaluatorRole);

			SCE objSCE = null;

			if ("view".equalsIgnoreCase(action)) {
				evaluateForm.setMode(SCEConstants.VIEW_MODE);
				// Get The Last Filled SCE
				// objSCE = sceManager.getLastFilledSCE(emplId, eventId,
				// productCode);
				objSCE = sceManager.getLatestSCE(emplId, eventId, productCode,
						SCEConstants.ST_SUBMITTED);

				// Get and Set the Details
				if (objSCE != null) {
					/*
					 * if (phasedTraining) { // Set Exam Score
					 * setExamScore(objSCE, examScore); }
					 */
					evaluateForm.setEvalProduct(objSCE.getProduct());
					evaluateForm.setEvaluatorEmplId(objSCE
							.getSubmittedByEmplId());
					evaluateForm.setEvaluatorRole(evaluatorRole);

				}
			} else {
				evaluateForm.setMode(SCEConstants.CREATE_MODE);
				if (phasedTraining) {
					// P2L Phased Training
					objSCE = sceManager.getLatestSCE(emplId, eventId,
							productCode, SCEConstants.ST_DRAFT);
					if (objSCE == null) {
						objSCE = sceManager.getNewSCEPhase(eventId,
								SCEConstants.EVENT_COURSE_ALL);
						objSCE.setStatus(SCEConstants.ST_NEW);
					} else {
						objSCE.setStatus(SCEConstants.ST_DRAFT);
					}
					// Set Exam Score
					setExamScore(objSCE, examScore);
				} else {
					objSCE = sceManager.getNewSCE(eventId,
							SCEConstants.EVENT_COURSE_ALL, objSCE.getProduct());
					objSCE.setSubmittedByEmplId(evaluatorEmplId);
				}

				evaluateForm.setEvalProduct(product);
				evaluateForm.setEvaluatorEmplId(evaluatorEmplId);
			}

			if (objSCE == null) {
				return new String("failure");
			}

			req.setAttribute("sceToDisplay", objSCE);

			if (phasedTraining) {
				return new String("success2");
			} else {
				return new String("success1");
			}
		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
	}

	public String acceptLegalConsentTr() {

		// System.out.println("Inside acceptLegalConsentTr()....JPF");

		HttpServletRequest req = getServletRequest();
		HttpSession session = req.getSession();

		UserLegalConsent userLegalConsent = new UserLegalConsent();
		String acceptedBy = "";
		int acceptedId = 0;
		User user = null;
		String linkName = "";
		String fwdPath = "";
		String loginUserNtId = "";
		String productCode = "";
		String product = "";
		String strEventId = "";
		Integer eventId = null;
		String emplId = "";
		String evaluatorEmplId = null;
		SCE[] sce = null;
		try {
			user = (User) getSession().getAttribute("user");
			/* Code to differentiate between localhost and other environments */
			/*
			 * if(req.getParameter("loginUserNtId")!=null){ //
			 * System.out.println
			 * ("Inside check for req. param - loginUserNtId"); loginUserNtId =
			 * req.getParameter("loginUserNtId"); }
			 */

			if (user != null) {
				acceptedBy = user.getNtid();
				// System.out.println("acceptedBy = "+acceptedBy);
			} else {
				acceptedBy = "bakhsr";
			}

			// Get the name of the link which was clicked to land into this page

			if (req.getParameter("linkName") != null) {
				linkName = (String) req.getParameter("linkName");
			}
			if (req.getParameter("loginUserNtId") != null) {
				loginUserNtId = (String) req.getParameter("loginUserNtId");
			}
			if (req.getParameter("productCode") != null) {
				productCode = (String) req.getParameter("productCode");
			}
			if (req.getParameter("eventId") != null) {
				strEventId = (String) req.getParameter("eventId");
				eventId = new Integer(Integer.parseInt(strEventId));
			}
			if (req.getParameter("emplId") != null) {
				emplId = (String) req.getParameter("emplId");
			}
			if (req.getParameter("product").trim() != null) {
				product = (String) req.getParameter("product");
			}

			if (req.getParameter("evaluatorEmplId").trim() != null) {
				evaluatorEmplId = (String) req.getParameter("evaluatorEmplId");
			}

			// Call method to get the SCE history object
			sce = sceManager.getSCEHistoryForTRT(eventId, emplId, productCode);

			if (sce != null) {
				// System.out.println("sce history Array length = "+sce.length);
			}

			acceptedId = sceManager.fetchMaxAcceptedId();// Fetch maximum ID
			// System.out.println("Fetched Max Accepted ID = "+acceptedId);
			acceptedId = acceptedId + 1;
			// System.out.println("Newly generated Accepted ID for insert= "+acceptedId);

			userLegalConsent.setAcceptedId(acceptedId);
			userLegalConsent.setAcceptedBy(acceptedBy);
			Integer temp_lcId = new Integer(req.getParameter("LC_ID"));
			int lcId = temp_lcId.intValue();
			// System.out.println("acceptLegalConsentTr() - lcId = "+lcId);
			userLegalConsent.setLcId(lcId);
			userLegalConsent.setNtDomain(user.getNtdomain());
			userLegalConsent.setSceRole(user.getUserGroup());

			// System.out.println("Going to call acceptLegalConsent....");
			sceManager.acceptLegalConsent(userLegalConsent);

			if (linkName.equals("evaluate") || linkName.equals("reEvaluate")) {
				// System.out.println("Inside Link Name check :::: Evaluate/ReEvaluate....");

				/*
				 * SCE[] objSCEArr = sceManager.getSCESByEventEmplId(eventId,
				 * emplId); if(objSCEArr!=null){ //
				 * System.out.println("SCE Objects length"+objSCEArr.length);
				 * 
				 * } req.setAttribute("sces",objSCEArr);
				 */
				// return new String("successEvaluate");
				req.setAttribute("emplId", emplId);
				req.setAttribute("eventId1", eventId);
				req.setAttribute("productCode", productCode);
				req.setAttribute("product", product);
				req.setAttribute("evaluatorEmplId", evaluatorEmplId);

				fwdPath = "successEvaluate";
			} else if (linkName.equals("viewEvaluations")) {
				// System.out.println("Inside Link Name check :::: viewEvaluations....");
				// return new String("successView");
				session.setAttribute("scesTR", sce);
				fwdPath = "successView";
				// System.out.println("fwdPath = "+fwdPath);
			}

		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}

		return new String(fwdPath);
	}

	private void setExamScore(SCE objSCE, Double examScore) {
		if (objSCE != null && examScore != null) {
			SCEDetail objSCEDetail = null;
			for (Iterator iter = objSCE.getSceDetailList().iterator(); iter
					.hasNext();) {
				objSCEDetail = (SCEDetail) iter.next();
				if (SCEConstants.QT_SCORE_FETCH.equalsIgnoreCase(objSCEDetail
						.getType())) {
					objSCEDetail.setAverageScore(new Double(
							evaluateExamScore(examScore)));
					if (objSCE.getOverallScore() == null) {
						objSCE.setOverallScore(new Double(objSCEDetail
								.getWeightedScore().doubleValue()));
					} else {
						objSCE.setOverallScore(new Double(objSCE
								.getOverallScore().doubleValue()
								+ (objSCEDetail.getWeightedScore()
										.doubleValue())));
					}
				}
			}
		}
	}

	private int evaluateExamScore(Double examScore) {
		if (examScore != null) {
			double tmp = examScore.doubleValue();

			if (tmp < 70.0) {
				return 1;
			} else if (tmp >= 70.0 && tmp < 80.0) {
				return 2;
			} else if (tmp >= 80.0 && tmp < 90.0) {
				return 3;
			} else if (tmp >= 90.0 && tmp < 95.0) {
				return 4;
			} else if (tmp >= 95.0) {
				return 5;
			}
		}
		return 0;
	}

	public String submitSCETRPhase() {
		// sceLogger.debug("IN submitSCE");

		HttpServletRequest req = getServletRequest();

		SCE objSCE = new SCE();

		// Get The SCE ID
		Integer sceId = null;
		if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("sce_id"))) {
			sceId = new Integer(req.getParameter("sce_id"));
		}

		// Get The Template Version
		Integer templateVersionId = new Integer(
				req.getParameter("template_version_id"));

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
			precall_comments = req.getParameter("precall_comments_def").trim();
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
			postcall_comments = req.getParameter("postcall_comments").trim();
		} else if (SCEUtils.isFieldNotNullAndComplete(req
				.getParameter("postcall_comments_def"))) {
			postcall_comments = req.getParameter("postcall_comments_def")
					.trim();
		}

		String overall_rating = null;
		if (SCEUtils.isFieldNotNullAndComplete(req
				.getParameter("overall_rating"))) {
			overall_rating = req.getParameter("overall_rating").trim();
		}

		String overall_comments = null;
		if (SCEUtils.isFieldNotNullAndComplete(req
				.getParameter("overall_comments"))) {
			overall_comments = req.getParameter("overall_comments").trim();
		}

		String comments = null;
		if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("comments"))) {
			comments = req.getParameter("comments").trim();
		}

		String hlcCompliant = null;
		if (SCEUtils.isFieldNotNullAndComplete(req
				.getParameter("healthcare_compliant"))) {
			hlcCompliant = req.getParameter("healthcare_compliant").trim();
		}

		String reviewed = null;
		if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("reviewed"))) {
			reviewed = req.getParameter("reviewed").trim();
		}

		String status = null;
		if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("status"))) {
			status = req.getParameter("status").trim();
		}

		objSCE.setId(sceId);
		objSCE.setEventId(evaluateForm.getEvalEventId());
		objSCE.setEmplId(evaluateForm.getEvalEmplId());
		objSCE.setProductCode(evaluateForm.getEvalProductCode());
		objSCE.setProduct(evaluateForm.getEvalProduct());
		objSCE.setCourse(null);
		objSCE.setClassroom(null);
		objSCE.setTableName(null);
		objSCE.setTemplateVersionId(templateVersionId);
		objSCE.setPrecallRating(precall_rating);
		objSCE.setPrecallComments(precall_comments);
		objSCE.setPostcallRating(postcall_rating);
		objSCE.setPostcallComments(postcall_comments);
		objSCE.setOverallRating(overall_rating);
		objSCE.setOverallComments(overall_comments);
		objSCE.setComments(comments);
		objSCE.setHlcCompliant(hlcCompliant);
		objSCE.setReviewed(reviewed);

		if (SCEConstants.ST_DRAFT.equalsIgnoreCase(status)) {
			objSCE.setPreparedByEmplId(evaluateForm.getEvaluatorEmplId());
			objSCE.setOverallScore(null);
		} else if (SCEConstants.ST_SUBMITTED.equalsIgnoreCase(status)) {
			Double overall_score = null;
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("overall_score"))) {
				try {
					overall_score = new Double(
							req.getParameter("overall_score"));
				} catch (Exception e) {
				}
			}
			objSCE.setSubmittedByEmplId(evaluateForm.getEvaluatorEmplId());
			objSCE.setOverallScore(overall_score);
		}
		objSCE.setStatus(status);

		// Get Questions and Iterate To Get The Values
		SCEDetail[] objSCEDetailArr = sceManager
				.getSCEDetailsByTemplateVersionId(templateVersionId);
		String question_rating = null;
		String question_comments = null;
		Double question_score1 = null;
		Double question_score2 = null;
		Double question_score3 = null;
		for (int i = 0; i < objSCEDetailArr.length; i++) {
			question_rating = null;
			question_comments = null;
			question_score1 = null;
			question_score2 = null;
			question_score3 = null;

			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("question_rating_"
							+ objSCEDetailArr[i].getQuestionId()))) {
				question_rating = req
						.getParameter(
								"question_rating_"
										+ objSCEDetailArr[i].getQuestionId())
						.trim();
			}
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("question_comments_"
							+ objSCEDetailArr[i].getQuestionId()))) {
				question_comments = req.getParameter(
						"question_comments_"
								+ objSCEDetailArr[i].getQuestionId()).trim();
			}

			/* P2L Fields */
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("question_score1_"
							+ objSCEDetailArr[i].getQuestionId()))) {
				question_score1 = new Double(req
						.getParameter(
								"question_score1_"
										+ objSCEDetailArr[i].getQuestionId())
						.trim());
			}
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("question_score2_"
							+ objSCEDetailArr[i].getQuestionId()))) {
				question_score2 = new Double(req
						.getParameter(
								"question_score2_"
										+ objSCEDetailArr[i].getQuestionId())
						.trim());
			}
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("question_score3_"
							+ objSCEDetailArr[i].getQuestionId()))) {
				question_score3 = new Double(req
						.getParameter(
								"question_score3_"
										+ objSCEDetailArr[i].getQuestionId())
						.trim());
			}
			/* END P2L Fields */

			objSCEDetailArr[i].setQuestionRating(question_rating);
			objSCEDetailArr[i].setQuestionComments(question_comments);
			/* P2L Fields */
			objSCEDetailArr[i].setQuestionScore1(question_score1);
			objSCEDetailArr[i].setQuestionScore2(question_score2);
			objSCEDetailArr[i].setQuestionScore3(question_score3);
			/* END P2L Fields */
		}

		try {
			sceManager.saveSCEPhase(objSCE, objSCEDetailArr);
		} catch (Exception e) {
			// sceLogger.error(e.getMessage());
			return new String("failure");
		}

		// sceLogger.debug("EmplId:" + evaluateForm.getEvalEmplId() +
		// " ProductCode:" + evaluateForm.getEvalProductCode() + " Product:" +
		// evaluateForm.getEvalProduct());
		req.setAttribute("evalEmplId", evaluateForm.getEvalEmplId());
		req.setAttribute("evalEventId", evaluateForm.getEvalEventId());
		req.setAttribute("status", status);
		return new String("success");
	}

	public String submitSCETR() {
		// sceLogger.debug("IN submitSCE");

		HttpServletRequest req = getServletRequest();
		try {
			SCE objSCE = new SCE();

			// Get The SCE ID
			Integer sceId = null;
			if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("sce_id"))) {
				sceId = new Integer(req.getParameter("sce_id"));
			}

			// Get The Template Version
			Integer templateVersionId = new Integer(
					req.getParameter("template_version_id"));

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

			String overall_rating = null;
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("overall_rating_H"))) {
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

			String hlcCompliant = null;
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("healthcare_compliant"))) {
				hlcCompliant = req.getParameter("healthcare_compliant").trim();
			}

			String reviewed = null;
			if (SCEUtils
					.isFieldNotNullAndComplete(req.getParameter("reviewed"))) {
				reviewed = req.getParameter("reviewed").trim();
			}

			String status = null;
			if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("status"))) {
				status = req.getParameter("status").trim();
			}

			/* Added by Mayank 22-Nov-2011 for SCE Enhancement 2011 */
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
				formTitle = "{evaluateForm Title Unavailable}";
			}

			String evaluatorName = null;
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("evaluator_name"))) {
				evaluatorName = req.getParameter("evaluator_name").trim();
			} else {
				evaluatorName = "{Evaluator Name Unavailable}";
			}
			/* End SCE Enhancement 2011 */

			objSCE.setId(sceId);
			objSCE.setEventId(evaluateForm.getEvalEventId());
			objSCE.setEmplId(evaluateForm.getEvalEmplId());
			objSCE.setProductCode(evaluateForm.getEvalProductCode());
			objSCE.setProduct(evaluateForm.getEvalProduct());
			// System.out.println("product "+evaluateForm.getEvalProduct());
			objSCE.setCourse(null);
			objSCE.setClassroom(null);
			objSCE.setTableName(null);

			/* Added by Mayank 22-Nov-2011 for SCE Enhancement 2011 */
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
			Date trainingDate = null;
			String sysdate = formatter.format(currentDate.getTime());
			try {
				if (SCEUtils.isFieldNotNullAndComplete(evaluateForm
						.getEvalTrainingDate())) {
					trainingDate = (Date) formatter.parse(evaluateForm
							.getEvalTrainingDate());
				}
			} catch (Exception e) {
				// sceLogger.error("Training Date Parse:" +
				// evaluateForm.getEvalTrainingDate());
			}
			/* End SCE Enhancement 2011 */
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
			objSCE.setPostcallNA(postcall_na);
			objSCE.setPrecallNA(precall_na);

			if (SCEConstants.ST_DRAFT.equalsIgnoreCase(status)) {
				objSCE.setPreparedByEmplId(evaluateForm.getEvaluatorEmplId());
				objSCE.setOverallScore(null);
			} else if (SCEConstants.ST_SUBMITTED.equalsIgnoreCase(status)) {
				Double overall_score = null;
				if (SCEUtils.isFieldNotNullAndComplete(req
						.getParameter("overall_score"))) {
					try {
						overall_score = new Double(
								req.getParameter("overall_score"));
					} catch (Exception e) {
					}
				}
				objSCE.setSubmittedByEmplId(evaluateForm.getEvaluatorEmplId());
				objSCE.setOverallScore(overall_score);
			}
			objSCE.setStatus(status);

			/* 2020 Q3: Start of muzees for duplicate evaluation */
			SCE checkSCE = sceManager.getLatestSCE(
					evaluateForm.getEvalEmplId(),
					evaluateForm.getEvalEventId(),
					evaluateForm.getEvalProductCode(),
					SCEConstants.ST_SUBMITTED);
			if (checkSCE != null && sceManager.checkduplicate(checkSCE, objSCE)) {
				status = "duplicate";
				req.setAttribute("evalEmplId", evaluateForm.getEvalEmplId());
				req.setAttribute("evalEventId", evaluateForm.getEvalEventId());
				req.setAttribute("status", status);
				return new String("success");
			}// end

			// Get Questions and Iterate To Get The Values
			SCEDetail[] objSCEDetailArr = sceManager
					.getSCEDetailsByTemplateVersionId(templateVersionId);
			String question_rating = null;
			String question_comments = null;
			Double question_score1 = null;
			Double question_score2 = null;
			Double question_score3 = null;
			String question_fg = null;
			for (int i = 0; i < objSCEDetailArr.length; i++) {
				question_rating = null;
				question_comments = null;
				question_score1 = null;
				question_score2 = null;
				question_score3 = null;

				if (SCEUtils.isFieldNotNullAndComplete(req
						.getParameter("question_rating_"
								+ objSCEDetailArr[i].getQuestionId()))) {
					question_rating = req.getParameter(
							"question_rating_"
									+ objSCEDetailArr[i].getQuestionId())
							.trim();
				}
				if (SCEUtils.isFieldNotNullAndComplete(req
						.getParameter("question_comments_"
								+ objSCEDetailArr[i].getQuestionId()))) {
					question_comments = req.getParameter(
							"question_comments_"
									+ objSCEDetailArr[i].getQuestionId())
							.trim();
				}

				/* P2L Fields */
				if (SCEUtils.isFieldNotNullAndComplete(req
						.getParameter("question_score1_"
								+ objSCEDetailArr[i].getQuestionId()))) {
					question_score1 = new Double(req.getParameter(
							"question_score1_"
									+ objSCEDetailArr[i].getQuestionId())
							.trim());
				}
				if (SCEUtils.isFieldNotNullAndComplete(req
						.getParameter("question_score2_"
								+ objSCEDetailArr[i].getQuestionId()))) {
					question_score2 = new Double(req.getParameter(
							"question_score2_"
									+ objSCEDetailArr[i].getQuestionId())
							.trim());
				}
				if (SCEUtils.isFieldNotNullAndComplete(req
						.getParameter("question_score3_"
								+ objSCEDetailArr[i].getQuestionId()))) {
					question_score3 = new Double(req.getParameter(
							"question_score3_"
									+ objSCEDetailArr[i].getQuestionId())
							.trim());
				}
				/* END P2L Fields */

				/* Added by Mayank 22-Nov-2011 for SCE Enhancement 2011 */
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
				/* End SCE Enhancement 2011 */

				objSCEDetailArr[i].setQuestionRating(question_rating);
				objSCEDetailArr[i].setQuestionComments(question_comments);
				/* P2L Fields */
				objSCEDetailArr[i].setQuestionScore1(question_score1);
				objSCEDetailArr[i].setQuestionScore2(question_score2);
				objSCEDetailArr[i].setQuestionScore3(question_score3);
				/* END P2L Fields */

				objSCEDetailArr[i].setQuestionFg(question_fg);
			}

			/* Added by Mayank 22-Nov-2011 for SCE Enhancement 2011 */
			// Update Legal Question Values
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

			try {
				sceManager.saveSCE(objSCE, objSCEDetailArr, legalDetailArr);
			} catch (Exception e) {
				// sceLogger.error(e.getMessage());
				return new String("failure");
			}

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
				// System.out.println("*******templateVersionId:"+templateVersionId);
				// System.out.println("*******overall_rating://"+overall_rating+"//");
				// EvaluationFormScore evaluationFormValues =
				// sceManager.getEvaluationFormValues(templateVersionId,overall_rating);

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

				// System.out.println("*******notificationFG://"+notificationFG+"//");
				String[] emailBCC = null;
				String[] emailCC = null;

				boolean isLocal = location.indexOf("localhost") > -1;
				boolean isDev = location.indexOf("sce-dev.pfizer.com") > -1;
				boolean isIntegration = location.indexOf("sceint.pfizer.com") > -1;
				boolean isStaging = location.indexOf("scestg.pfizer.com") > -1;
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
					if (isLocal || isDev) {

						emailFrom = "pankaj.gadale@pfizer.com";
						emailTo[0] = "pankaj.gadale@pfizer.com";
						emailTemplate.setEmailCc("Apoorva.Tijare@pfizer.com");
						// System.out.println("EmailCC "+emailTemplate.getEmailCc());
						// emailCC[0]="pankaj.gadale@pfizer.com";
						// emailBCC[0]="pankaj.gadale@pfizer.com";
						// emailBCC="";
					}

					MailUtil.sendMessage(emailFrom, emailTo, emailCC, emailBCC,
							emailSubject, emailMessage, mimetype, mailJNDI);
					// System.out.println("Email Method Called");
				}

				catch (AddressException ae) {
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

			// sceLogger.debug("EmplId:" + evaluateForm.getEvalEmplId() +
			// " ProductCode:" + evaluateForm.getEvalProductCode() + " Product:"
			// + evaluateForm.getEvalProduct());
			req.setAttribute("evalEmplId", evaluateForm.getEvalEmplId());
			req.setAttribute("evalEventId", evaluateForm.getEvalEventId());
			req.setAttribute("status", status);

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

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return evaluateForm;
	}

}
