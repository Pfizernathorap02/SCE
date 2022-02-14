package com.pfizer.sce.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
//import org.hibernate.validator.constraints.Length;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.sce.ActionForm.UserDataForm;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.EvaluationControllerHelper;
import com.pfizer.sce.helper.LegalConsentHelper;
import com.pfizer.sce.utils.MailUtil;
import com.pfizer.sce.utils.SCEUtils;
//imports added

//imports ended

public class UserAdminAction extends ActionSupport implements
		ServletRequestAware, ModelDriven {

	private static SCEManagerImpl sceManager = new SCEManagerImpl();
	private UserDataForm userDataForm = new UserDataForm();
	private static LegalConsentHelper legalConsentHelp = new LegalConsentHelper();
	// private String[] bussinessUnitList=SCEManagerImpl.getBU();
	private HashMap userGroups = SCEUtils.getUserGroupsMap();
	private HashMap statuses = SCEUtils.getStatuses();

	private LinkedHashMap filterStatuses = SCEUtils.getFilterStatuses();
	private LinkedHashMap filterUserGroups = SCEUtils.getFilterUserGroups();// added
																			// by
																			// muzees

	// Mapping of hidden parameter is done
	private String selUserId;
	private String selBusinessUnit;

	public String getSelBusinessUnit() {
		return selBusinessUnit;
	}

	public void setSelBusinessUnit(String selBusinessUnit) {
		this.selBusinessUnit = selBusinessUnit;
	}

	public String getSelUserId() {
		return selUserId;
	}

	public void setSelUserId(String selUserId) {
		this.selUserId = selUserId;
	}

	public HashMap getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(HashMap userGroups) {
		this.userGroups = userGroups;
	}

	public HashMap getStatuses() {
		return statuses;
	}

	public void setStatuses(HashMap statuses) {
		this.statuses = statuses;
	}

	public LinkedHashMap getFilterStatuses() {
		return filterStatuses;
	}

	public void setFilterStatuses(LinkedHashMap filterStatuses) {
		this.filterStatuses = filterStatuses;
	}

	// static Logger log =
	// Logger.getLogger(SearchAttendeeAction.class.getName());

	HttpServletRequest request;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public UserDataForm getUserDataForm() {
		return userDataForm;
	}

	public void setUserDataForm(UserDataForm userDataForm) {
		this.userDataForm = userDataForm;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return userDataForm;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	public String gotoUserAdmin() {
		HttpServletRequest req = getRequest();
		HttpSession session = req.getSession();

		String result = legalConsentHelp.checkLegalConsent(req, session);
		// System.out.println("*****result*****:" + result);
		if (result != null && result.equals("success")) {
			// System.out.println("*************Forwarding to legalConsent");
			String forwardToHomePage = "N";
			EvaluationControllerHelper.setBookMarkURL(session, req,
					forwardToHomePage);
			return new String("legalConsent");
		} else if (result != null && result.equals("exception")) {
			// System.out.println("**********Forwarding to exception");
			return new String("failure");
		}
		// Get All Users
		if (userDataForm.getSelUserType() == null) {
			userDataForm.setSelUserType("SCE_Administrators");// added by muzees
																// to fetch
																// users based
																// on usergroup
																// too
		}
		if (userDataForm.getSelUserStatus() == null) {
			userDataForm.setSelUserStatus("Active");
		}
		User[] users = sceManager.getUsersByStatus(userDataForm
				.getSelUserStatus().toUpperCase(), userDataForm
				.getSelUserType());
		req.setAttribute("users", users);
		return new String("success");
	}

	public String gotoAddUser() {
		HttpServletRequest req = getRequest();
		HttpSession session = req.getSession();
		String result = legalConsentHelp.checkLegalConsent(req, session);
		if (req.getParameter("isAccessRequest") != null
				&& req.getParameter("isAccessRequest").equalsIgnoreCase("true")) {

			return new String("success");
		}

		// System.out.println("*****result*****:" + result);
		if (result != null && result.equals("success")) {
			// System.out.println("*************Forwarding to legalConsent");
			String forwardToHomePage = "N";
			EvaluationControllerHelper.setBookMarkURL(session, req,
					forwardToHomePage);
			return new String("legalConsent");
		} else if (result != null && result.equals("exception")) {
			// System.out.println("**********Forwarding to exception");
			return new String("failure");
		}
		return new String("success");
	}

	// below method updated by muzees for filtering user based on usergroup too
	public String selectUserStatus() {
		HttpServletRequest req = getRequest();

		String selUserStatus = null;
		String selUserType = null;
		selUserStatus = req.getParameter("selUserStatus");
		selUserType = req.getParameter("selUserType");

		if (selUserStatus.equalsIgnoreCase("All")) {

			// Get All Users
			User[] users = sceManager.getAllUsers(selUserType);
			req.setAttribute("users", users);
		}
		if (selUserStatus.equalsIgnoreCase("ACTIVE")) {
			User[] users = sceManager.getUsersByStatus("ACTIVE", selUserType);
			req.setAttribute("users", users);
		}
		if (selUserStatus.equalsIgnoreCase("INACTIVE")) {
			User[] users = sceManager.getUsersByStatus("INACTIVE", selUserType);
			req.setAttribute("users", users);
		}

		return new String("success");
	}

	// end of muzees
	public String gotoUpdateUser() {
		HttpServletRequest req = getRequest();

		Integer userId = null;
		User user = null;

		try {
			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("selUserId"))) {
				userId = new Integer(req.getParameter("selUserId"));

				/*
				 * String selUserStatus = req.getParameter("selUserStatus");
				 * String selUserGroup = req.getParameter("selUserGroup");
				 * userDataForm.setSelUserStatus(selUserStatus);
				 * userDataForm.setSelUserType(selUserGroup);
				 */
				String[] buList = sceManager.getAllBUList();// added by muzees
															// for PGB and
															// UpJOHN to fetch
															// BU

				user = sceManager.getUserById(userId);

				userDataForm.setId(userId);
				userDataForm.setEmplId(user.getEmplId());
				userDataForm.setFirstName(user.getFirstName());
				userDataForm.setLastName(user.getLastName());
				userDataForm.setNtid(user.getNtid());
				userDataForm.setNtdomain(user.getNtdomain());
				userDataForm.setStatus(user.getStatus());
				userDataForm.setEmail(user.getEmail());
				userDataForm.setUserGroup(user.getUserGroup());
				/* Author: Mayank Date:07-Oct-2011 SCE Enhancement 2011 */
				userDataForm.setExpirationDate(user.getExpirationDate());

				// ADDED BY MUZEES
				if (user.getUserGroup()
						.equalsIgnoreCase("SCE_GuestTrainer_MGR")) {
					userGroups.put("SCE_GuestTrainer_MGR", "Guest Trainer MGR");
				}
				userDataForm.setBusinessUnit(user.getBusinessUnit());
				userDataForm.setSel_bu_id(0);
				for (int i = 0; i < buList.length; i++) {
					if (buList[i].equalsIgnoreCase(user.getBusinessUnit())) {
						userDataForm.setSel_bu_id(i + 1);
						break;
					}
				}

				req.setAttribute("buList", buList);
				// end
				req.setAttribute("expDate", user.getExpirationDate());
				return new String("success");
			} else {
				return new String("noUserId");
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

	public String updateUser() {
		HttpServletRequest req = getRequest();

		try {
			String selUserGroup = req.getParameter("selUserGroup");
			User user = new User();

			user.setId(userDataForm.getId());
			user.setEmplId(userDataForm.getEmplId());
			user.setFirstName(userDataForm.getFirstName());
			user.setLastName(userDataForm.getLastName());
			user.setNtdomain(userDataForm.getNtdomain());
			user.setUserGroup(userDataForm.getUserGroup());
			user.setStatus(userDataForm.getStatus());
			user.setEmail(userDataForm.getEmail());
			user.setBusinessUnit(userDataForm.getBusinessUnit());// added by
																	// muzees
																	// for PBG
																	// and
																	// UpJOHN
			/* Author: Mayank Date:07-Oct-2011 SCE Enhancement 2011 */
			user.setExpirationDate(userDataForm.getExpirationDate());
			if (selUserGroup.equalsIgnoreCase("SCE_GuestTrainer_MGR")) {
				sceManager.updateOverNightGT(user);
			} else
				sceManager.updateUser(user);

		} catch (Exception e) {
			userDataForm.setMessage(e.getMessage());
			return new String("updateUserFailed");
		}
		req.setAttribute("message", "User has been updated successfully");
		return new String("success");
	}

	public String removeUser() {
		HttpServletRequest req = getRequest();

		Integer userId = null;
		if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("selUserId"))) {
			userId = new Integer(req.getParameter("selUserId"));
			try {
				sceManager.removeUser(userId);
			} catch (Exception e) {
				return new String("success");
			}
		}
		// 2020 Q4:Start of MUZEES to show users full name
		String firstName = (String) req.getParameter("selUserFirstName");
		String lastName = (String) req.getParameter("selUserLastName");
		req.setAttribute("message", firstName + " " + lastName
				+ " has been removed successfully");
		//end of muzees
		return new String("success");
	}

	// code add by manish to reject access

	public String rejectAccess() {
		// check urls

		final String TRTDEV = "sce-dev.pfizer.com/";

		final String TRTTST = "sce-tst.pfizer.com/";

		final String TRTSTG = "sce-stg.pfizer.com/";

		final String TRTPROD = "sce.pfizer.com/";

		final String TRTLOCL = "localhost:8080/";

		String mailURL = "";
		String requestURL = ((HttpServletRequest) request).getRequestURL()
				.toString();

		if (requestURL != null && requestURL.toString().contains(TRTPROD))

			mailURL = TRTPROD;

		else if (requestURL != null && requestURL.toString().contains(TRTSTG))

			mailURL = TRTSTG;

		else if (requestURL != null && requestURL.toString().contains(TRTTST))

			mailURL = TRTTST;

		else if (requestURL != null && requestURL.toString().contains(TRTLOCL))

			mailURL = TRTLOCL;

		else if (requestURL != null && requestURL.toString().contains(TRTDEV))

			mailURL = TRTDEV;

		// end

		String result = "";
		String ifSent = "";
		String lastName = request.getParameter("lastName");
		String firstName = request.getParameter("firstName");
		String email = request.getParameter("email");
		String emplId = request.getParameter("emplId");
		String ntid = request.getParameter("ntid");
		String ntdomain = request.getParameter("ntdomain");
		String comments = request.getParameter("comments");

		String subEmail = "Access Rejected";

		User user = new User();

		user.setLastName(lastName);
		user.setFirstName(firstName);
		user.setEmail(email);
		user.setEmplId(emplId);
		user.setNtid(ntid.trim());
		user.setNtdomain(ntdomain);

		String gotNtId = " ";
		String status = " ";

		if (ntid != null) {
			try {
				gotNtId = sceManager.getRequestUser(ntid);
				status = sceManager.requestUserStatus(ntid);
			} catch (SCEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("req user object :" + gotNtId);
		System.out.println("req user status: " + status);

		if (gotNtId != null
				&& (status.trim().equalsIgnoreCase("REJECTED") || status.trim()
						.equalsIgnoreCase("APPROVED"))) {
			System.out.println("inside if");
			result = "rejected";
			System.out.println("result is:" + result);
			return result;
		}

		else {

			sceManager.rejectRequestUserStatus(user);

			String emailBody = "\n"
					+ "Dear User"
					+ "\n\n"
					+ "Your Access request has been rejected for below details:"
					+ "\n\n" + "Email:" + " "
					+ email
					+ "\n"
					+ "Emplid:"
					+ " "
					+ emplId
					+ "\n"
					+ "NTID:"
					+ " "
					+ ntid
					+ "\n"
					+ "NT Domain:"
					+ " "
					+ ntdomain
					+ "\n"
					+ "Comments:"
					+ "\n"
					+ ""
					+ "<div style='width:500px;height:400px;background:#b3d9ff'>"
					+ "<h4>"
					+ comments
					+ "</h4>"
					+ "</div>"
					+ "\n\n\n\n"
					+ "Thank You" + "\n" + "SCE Team.";

			// http://sce-tst.pfizer.com/SCEWeb/gotoAddUser.action

			String from = "DL-SAMSSCESupport@pfizer.com";

			String mimetype = "text/html";
			String mailJNDI = "java:jboss/SCEMailSession";

			String host = "mailhub.pfizer.com";
			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.host", host);

			Session mailSession = Session.getDefaultInstance(properties);

			try {

				MimeMessage message = new MimeMessage(mailSession);

				message.setFrom(new InternetAddress(from));
				message.setSubject(subEmail);
				String emailBodyFinal = emailBody.replaceAll("\n", "</br>");
				message.setContent(emailBodyFinal, "text/html");

				// code to get approvers

				List<String> email1 = new ArrayList<String>();

				User[] userEmail = sceManager.getApprovers();

				Integer length = userEmail.length;

				System.out.println("Length:" + length);

				for (int i = 0; i < length; i++) {

					email1.add(i, userEmail[i].getRequestApprovers());

				}

				if (email.isEmpty()) {
					email1.add(0, "DL-SAMSSCESupport@pfizer.com");
					email1.add(1, "DL-SAMSSCESupport@pfizer.com");
					email1.add(2, "DL-SAMSSCESupport@pfizer.com");

				}

				String approver1 = email1.get(0);
				String approver2 = email1.get(1);
				String approver3 = email1.get(2);

				// code end to get approvers

				// String toMail =email;

				// String ccMail = "manish.kumar2@pfizer.com";

				// String bccMail="";

				String[] toMail = new String[] { email };

				String[] ccMail = new String[] { approver1, approver2,
						approver3, "manish.kumar2@pfizer.com" };

				String[] bccMail = new String[] { "manish.kumar2@pfizer.com" };

				MailUtil mailUtil1 = new MailUtil();

				mailUtil1.sendMessage(from, toMail, ccMail, bccMail, subEmail,
						emailBodyFinal, mimetype, mailJNDI);
				result = "Sent";
				ifSent = "Y";

			} catch (MessagingException mex) {
				ifSent = "N";

				mex.printStackTrace();
				result = "failure";
			} catch (SCEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Result is:" + result);

		}

		return result;

	}

	// code end

	// code added by manish to add request

	public String requestAccess() {

		// check urls

		final String TRTDEV = "sce-dev.pfizer.com/";

		final String TRTTST = "sce-tst.pfizer.com/";

		final String TRTSTG = "sce-stg.pfizer.com/";

		final String TRTPROD = "sce.pfizer.com/";

		final String TRTLOCL = "localhost:8080/";

		String mailURL = "";
		String requestURL = ((HttpServletRequest) request).getRequestURL()
				.toString();

		if (requestURL != null && requestURL.toString().contains(TRTPROD))

			mailURL = TRTPROD;

		else if (requestURL != null && requestURL.toString().contains(TRTSTG))

			mailURL = TRTSTG;

		else if (requestURL != null && requestURL.toString().contains(TRTTST))

			mailURL = TRTTST;

		else if (requestURL != null && requestURL.toString().contains(TRTLOCL))

			mailURL = TRTLOCL;

		else if (requestURL != null && requestURL.toString().contains(TRTDEV))

			mailURL = TRTDEV;

		System.out.println("Mail url is:" + mailURL);
		// end

		String result = "";
		String ifSent = "";
		String lastName = request.getParameter("lastName");
		String firstName = request.getParameter("firstName");
		String email = request.getParameter("email");
		String emplId = request.getParameter("emplId");
		String ntid = request.getParameter("ntid");
		String ntdomain = request.getParameter("ntdomain");
		String subEmail = "Request for access";

		// method added to insert data into request access
		User user = new User();

		user.setLastName(lastName);
		user.setFirstName(firstName);
		user.setEmail(email);
		user.setEmplId(emplId);
		user.setNtid(ntid.trim());
		user.setNtdomain(ntdomain);
		user.setStatus("REQUESTED");

		String gotNtId = " ";
		String status = " ";

		if (ntid != null) {
			try {
				gotNtId = sceManager.getRequestUser(ntid);
				status = sceManager.requestUserStatus(ntid);
			} catch (SCEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("req user object :" + gotNtId);
		System.out.println("req user status: " + status);
		// System.out.println("NTID :"+requser.getNtid());
		// System.out.println("First Name:"+requser.getFirstName());

		if (gotNtId != null && status.trim().equalsIgnoreCase("REQUESTED")) {

			// child's playground

			request.setAttribute("email", email);
			request.setAttribute("ntid", ntid);
			request.setAttribute("fname", firstName);
			request.setAttribute("lname", lastName);
			request.setAttribute("ntdomain", ntdomain);
			request.setAttribute("emplid", emplId);

			// end

			System.out.println("inside if");
			result = "requested";
			System.out.println("result is:" + result);
			return result;
		}

		else if (gotNtId != null
				&& (status.trim().equalsIgnoreCase("APPROVED") || status.trim()
						.equalsIgnoreCase("REJECTED"))) {
			System.out.println("inside else if");
			sceManager.updateRequest(user);

			// end method to insert data in request access

			String emailBody = "\n"
					+ "Dear Approver"
					+ "\n\n"
					+ "Please approve and add, the access request by the user with below details:"
					+ "\n\n" + "Name:"
					+ firstName
					+ " "
					+ lastName
					+ "\n"
					+ "Email:"
					+ " "
					+ email
					+ "\n"
					+ "Emplid:"
					+ " "
					+ emplId
					+ "\n"
					+ "NTID:"
					+ " "
					+ ntid
					+ "\n"
					+ "NT Domain:"
					+ " "
					+ ntdomain
					+ "\n\n"
					+ "<div><a href='http://"
					+ mailURL
					+ "SCEWeb/gotoAddUser.action?lastName="
					+ lastName
					+ "&firstName="
					+ firstName
					+ "&email="
					+ email
					+ "&emplId="
					+ emplId
					+ "&ntid="
					+ ntid
					+ "&ntdomain="
					+ ntdomain
					+ "&isAccessRequest=true'>Approve</a></div>"
					+ "\n\n"
					+ "<div><a href='http://"
					+ mailURL
					+ "SCEWeb/evaluation/rejectAccess.jsp?lastName="
					+ lastName
					+ "&firstName="
					+ firstName
					+ "&email="
					+ email
					+ "&emplId="
					+ emplId
					+ "&ntid="
					+ ntid
					+ "&ntdomain="
					+ ntdomain
					+ "&isAccessRequest=true'>Reject</a></div>"
					+ "\n\n\n\n" + "Thank You" + "\n" + "SCE Team.";

			// http://sce-tst.pfizer.com/SCEWeb/gotoAddUser.action

			String from = "DL-SAMSSCESupport@pfizer.com";

			String mimetype = "text/html";
			String mailJNDI = "java:jboss/SCEMailSession";

			String host = "mailhub.pfizer.com";
			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.host", host);

			Session mailSession = Session.getDefaultInstance(properties);

			try {

				MimeMessage message = new MimeMessage(mailSession);

				message.setFrom(new InternetAddress(from));
				message.setSubject(subEmail);
				String emailBodyFinal = emailBody.replaceAll("\n", "</br>");
				message.setContent(emailBodyFinal, "text/html");

				// code to get approvers
				List<String> email1 = new ArrayList<String>();
				String[] strings = null;

				User[] userEmail = sceManager.getApprovers();

				Integer length = userEmail.length;

				System.out.println("Length:" + length);

				for (int i = 0; i < length; i++) {

					email1.add(i, userEmail[i].getRequestApprovers());

				}

				if (email.isEmpty()) {
					email1.add(0, "DL-SAMSSCESupport@pfizer.com");
					email1.add(1, "DL-SAMSSCESupport@pfizer.com");
					email1.add(2, "DL-SAMSSCESupport@pfizer.com");

				}

				String approver1 = email1.get(0);
				String approver2 = email1.get(1);
				String approver3 = email1.get(2);

				String[] toMail = new String[] { approver1, approver2,
						approver3 };

				String[] ccMail = new String[] { "manish.kumar2@pfizer.com" };

				String[] bccMail = new String[] { "manish.kumar2@pfizer.com" };

				// code end to get approvers

				// String toMail = "manish.kumar2@pfizer.com";

				// String ccMail = "DL-SAMSSCESupport@pfizer.com";

				// String bccMail="";

				MailUtil mailUtil1 = new MailUtil();

				mailUtil1.sendMessage(from, toMail, ccMail, bccMail, subEmail,
						emailBodyFinal, mimetype, mailJNDI);
				result = "Sent";
				ifSent = "Y";

			} catch (MessagingException mex) {
				ifSent = "N";

				mex.printStackTrace();
				result = "failure";
			} catch (SCEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Result is:" + result);

			return result;
		}

		else {
			System.out.println("else");

			sceManager.insertRequest(user);

			// end method to insert data in request access

			String emailBody = "\n"
					+ "Dear Approver"
					+ "\n\n"
					+ "Please approve and add, the access request by the user with below details:"
					+ "\n\n" + "Name:"
					+ firstName
					+ " "
					+ lastName
					+ "\n"
					+ "Email:"
					+ " "
					+ email
					+ "\n"
					+ "Emplid:"
					+ " "
					+ emplId
					+ "\n"
					+ "NTID:"
					+ " "
					+ ntid
					+ "\n"
					+ "NT Domain:"
					+ " "
					+ ntdomain
					+ "\n\n"
					+ "<div><a href='http://"
					+ mailURL
					+ "SCEWeb/gotoAddUser.action?lastName="
					+ lastName
					+ "&firstName="
					+ firstName
					+ "&email="
					+ email
					+ "&emplId="
					+ emplId
					+ "&ntid="
					+ ntid
					+ "&ntdomain="
					+ ntdomain
					+ "&isAccessRequest=true'>Approve</a></div>"
					+ "\n\n"
					+ "<div><a href='http://"
					+ mailURL
					+ "SCEWeb/evaluation/rejectAccess.jsp?lastName="
					+ lastName
					+ "&firstName="
					+ firstName
					+ "&email="
					+ email
					+ "&emplId="
					+ emplId
					+ "&ntid="
					+ ntid
					+ "&ntdomain="
					+ ntdomain
					+ "&isAccessRequest=true'>Reject</a></div>"
					+ "\n\n\n\n" + "Thank You" + "\n" + "SCE Team.";

			// http://sce-tst.pfizer.com/SCEWeb/gotoAddUser.action

			String from = "DL-SAMSSCESupport@pfizer.com";

			String mimetype = "text/html";
			String mailJNDI = "java:jboss/SCEMailSession";

			String host = "mailhub.pfizer.com";
			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.host", host);

			Session mailSession = Session.getDefaultInstance(properties);

			try {

				MimeMessage message = new MimeMessage(mailSession);

				message.setFrom(new InternetAddress(from));
				message.setSubject(subEmail);
				String emailBodyFinal = emailBody.replaceAll("\n", "</br>");
				message.setContent(emailBodyFinal, "text/html");

				// code to get approvers
				List<String> email1 = new ArrayList<String>();
				String[] strings = null;

				User[] userEmail = sceManager.getApprovers();

				Integer length = userEmail.length;

				System.out.println("Length:" + length);

				for (int i = 0; i < length; i++) {

					email1.add(i, userEmail[i].getRequestApprovers());

				}

				// code end to get approvers

				if (email.isEmpty()) {
					email1.add(0, "DL-SAMSSCESupport@pfizer.com");
					email1.add(1, "DL-SAMSSCESupport@pfizer.com");
					email1.add(2, "DL-SAMSSCESupport@pfizer.com");

				}

				String approver1 = email1.get(0);
				String approver2 = email1.get(1);
				String approver3 = email1.get(2);

				// String toMail = approver1+";"+approver2+";"+approver3;

				String[] toMail = new String[] { approver1, approver2,
						approver3 };

				String[] ccMail = new String[] { "manish.kumar2@pfizer.com" };

				String[] bccMail = new String[] { "manish.kumar2@pfizer.com" };

				// String toMail = "manish.kumar2@pfizer.com";

				// String ccMail = "DL-SAMSSCESupport@pfizer.com";

				// String bccMail="";

				MailUtil mailUtil1 = new MailUtil();

				mailUtil1.sendMessage(from, toMail, ccMail, bccMail, subEmail,
						emailBodyFinal, mimetype, mailJNDI);
				result = "Sent";
				ifSent = "Y";

			} catch (MessagingException mex) {
				ifSent = "N";

				mex.printStackTrace();
				result = "failure";
			} catch (SCEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Result is:" + result);

			return result;
		}

	}

	// end code

	// start code for reminder email

	public String reminderMail() {

		// check urls

		final String TRTDEV = "sce-dev.pfizer.com/";

		final String TRTTST = "sce-tst.pfizer.com/";

		final String TRTSTG = "sce-stg.pfizer.com/";

		final String TRTPROD = "sce.pfizer.com/";

		final String TRTLOCL = "localhost:8080/";

		String mailURL = "";
		String requestURL = ((HttpServletRequest) request).getRequestURL()
				.toString();

		if (requestURL != null && requestURL.toString().contains(TRTPROD))

			mailURL = TRTPROD;

		else if (requestURL != null && requestURL.toString().contains(TRTSTG))

			mailURL = TRTSTG;

		else if (requestURL != null && requestURL.toString().contains(TRTTST))

			mailURL = TRTTST;

		else if (requestURL != null && requestURL.toString().contains(TRTLOCL))

			mailURL = TRTLOCL;

		else if (requestURL != null && requestURL.toString().contains(TRTDEV))

			mailURL = TRTDEV;

		System.out.println("Mail url is:" + mailURL);
		// end

		String result = "";
		String ifSent = "";

		String ntid = request.getParameter("ntid");
		System.out.println("Got ntid in reminder email function:" + ntid);
		String fname = request.getParameter("firstname");
		String lname = request.getParameter("lastname");
		String ntdomain = request.getParameter("ntdomain");
		String emplid = request.getParameter("emplid");
		String comments = request.getParameter("comments");
		String email = request.getParameter("email");

		String subEmail = "Reminder:Request for access";

		String emailBody = "\n"
				+ "Dear Approver,"
				+ "\n\n"
				+ "--Reminder--"
				+ "\n"
				+ "Please approve and add, the access request by the user with below details:"
				+ "\n\n" + "Name:"
				+ fname
				+ " "
				+ lname
				+ "\n"
				+ "Email:"
				+ " "
				+ email
				+ "\n"
				+ "Emplid:"
				+ " "
				+ emplid
				+ "\n"
				+ "NTID:"
				+ " "
				+ ntid
				+ "\n"
				+ "NT Domain:"
				+ " "
				+ ntdomain
				+ "\n"
				+ "Requester Comments:"
				+ "\n"
				+ ""
				+ "<div style='width:500px;height:400px;background:#b3d9ff'>"
				+ "<h4>"
				+ comments
				+ "</h4>"
				+ "</div>"
				+ "\n\n"
				+ "<div><a href='http://"
				+ mailURL
				+ "SCEWeb/gotoAddUser.action?lastName="
				+ lname
				+ "&firstName="
				+ fname
				+ "&email="
				+ email
				+ "&emplId="
				+ emplid
				+ "&ntid="
				+ ntid
				+ "&ntdomain="
				+ ntdomain
				+ "&isAccessRequest=true'>Approve</a></div>"
				+ "\n\n"
				+ "<div><a href='http://"
				+ mailURL
				+ "SCEWeb/evaluation/rejectAccess.jsp?lastName="
				+ lname
				+ "&firstName="
				+ fname
				+ "&email="
				+ email
				+ "&emplId="
				+ emplid
				+ "&ntid="
				+ ntid
				+ "&ntdomain="
				+ ntdomain
				+ "&isAccessRequest=true'>Reject</a></div>"
				+ "\n\n\n\n"
				+ "Thank You" + "\n" + "SCE Team.";

		// http://sce-tst.pfizer.com/SCEWeb/gotoAddUser.action

		String from = "DL-SAMSSCESupport@pfizer.com";

		String mimetype = "text/html";
		String mailJNDI = "java:jboss/SCEMailSession";

		String host = "mailhub.pfizer.com";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);

		Session mailSession = Session.getDefaultInstance(properties);

		try {

			MimeMessage message = new MimeMessage(mailSession);

			message.setFrom(new InternetAddress(from));
			message.setSubject(subEmail);
			String emailBodyFinal = emailBody.replaceAll("\n", "</br>");
			message.setContent(emailBodyFinal, "text/html");

			// code to get approvers

			List<String> email1 = new ArrayList<String>();

			User[] userEmail = sceManager.getApprovers();

			Integer length = userEmail.length;

			System.out.println("Length:" + length);

			for (int i = 0; i < length; i++) {

				email1.add(i, userEmail[i].getRequestApprovers());

			}

			if (email.isEmpty()) {
				email1.add(0, "DL-SAMSSCESupport@pfizer.com");
				email1.add(1, "DL-SAMSSCESupport@pfizer.com");
				email1.add(2, "DL-SAMSSCESupport@pfizer.com");

			}

			String approver1 = email1.get(0);
			String approver2 = email1.get(1);
			String approver3 = email1.get(2);

			// code end to get approvers

			// String toMail =email;

			// String ccMail = "manish.kumar2@pfizer.com";

			// String bccMail="";

			String[] toMail = new String[] { approver1, approver2, approver3 };

			String[] ccMail = new String[] { "manish.kumar2@pfizer.com" };

			String[] bccMail = new String[] { "manish.kumar2@pfizer.com" };

			MailUtil mailUtil1 = new MailUtil();

			mailUtil1.sendMessage(from, toMail, ccMail, bccMail, subEmail,
					emailBodyFinal, mimetype, mailJNDI);
			result = "Sent";
			ifSent = "Y";

		} catch (MessagingException mex) {
			ifSent = "N";

			mex.printStackTrace();
			result = "failure";
		} catch (SCEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Result is:" + result);

		return result;
	}

	// end code

	// code start to add approvers
	public String goToApprovers() {

		String message = "";
		try {

			List<String> email = new ArrayList<String>();
			String[] strings = null;

			User[] userEmail = sceManager.getApprovers();
			Integer length = userEmail.length;

			System.out.println("Length:" + length);

			for (int i = 0; i < length; i++) {

				email.add(i, userEmail[i].getRequestApprovers());

			}

			System.out.println("email 1 " + email);

			if (email.isEmpty()) {
				email.add(0, null);
				email.add(1, null);
				email.add(2, null);

				message = "No approvers mapped !";

			}
			System.out.println(email.get(0));
			System.out.println(email.get(1));
			System.out.println(email.get(2));

			request.setAttribute("message", message);
			request.setAttribute("length", length);
			request.setAttribute("email1", email.get(0));
			request.setAttribute("email2", email.get(1));
			request.setAttribute("email3", email.get(2));

			System.out.println("Length of approvers:" + length);

		} catch (SCEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String("success");
	}

	// end code
	/*
	 * 
	 */
	// code added by shaikh07 for Role Mapping for version 3.4 RFC#1136920
	public String goToRoleMapping() {

		try {
			String roles[] = sceManager.getRoles();
			int length = roles.length;

			HttpServletRequest req = getRequest();

			String[] rolesList = sceManager.getAllRoles();
			req.setAttribute("rolesList", rolesList);

			if (length == 0) {
				request.setAttribute("message",
						"No Roles available for mapping !");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String("success");
	}

	// code added by shaikh07 for Role Mapping for version 3.4 RFC#1136920
	public String saveRole() {
		int status = 0;
		String message = "";

		try {
			String roleDescription = request.getParameter("selRoleForGroup");

			String roleCd = roleDescription.substring(0,
					roleDescription.indexOf("-"));

			status = sceManager.saveRoles(roleCd, roleDescription);
			if (status == 1) {
				message = "Selected role is mapped successfully";
			} else {
				message = "Error in mapping !";
			}
			request.setAttribute("message", message);

		} catch (Exception e) {

		}

		return new String("success");
	}

	// end codes by hussain

	// code added by KUMAM1234 for Role Mapping for version 3.4 RFC#1136920
	// updated by muzees for error in deletion
	public String deleteRole() {

		// int status = 0;
		String message = "";

		try {

			String delRoleDesc = request.getParameter("delRoleId");
			String delRoleCd = delRoleDesc.substring(0,
					delRoleDesc.indexOf("-"));
			// status =
			sceManager.deleteRole(delRoleCd);
			/* if (status == 1) { */
			message = delRoleCd + " is deleted successfully";
			/*
			 * } else { message = "Error in deletion !"; }
			 */
			request.setAttribute("message", message);

		} catch (Exception e) {

		}

		return new String("success");
	}

	// code added by KUMAM1234 for update approval [Functionality not enabled]
	public String updateApprovers() {
		String message = "";
		message = "";

		String email1 = request.getParameter("email1");
		String email2 = request.getParameter("email2");
		String email3 = request.getParameter("email3");

		System.out.println("email1 by H " + email1);
		sceManager.removeApprovers();

		User user = new User();

		user.setRequestApprover1(email1);
		user.setRequestApprover2(email2);
		user.setRequestApprover3(email3);

		try {
			sceManager.insertApprover(user);
		} catch (Exception e) {
			System.out
					.println("Exception occured while inserting request approvers:"
							+ e);
		}

		try {

			List<String> email = new ArrayList<String>();
			String[] strings = null;

			User[] userEmail = sceManager.getApprovers();
			Integer length = userEmail.length;

			System.out.println("Length:" + length);

			for (int i = 0; i < length; i++) {

				email.add(i, userEmail[i].getRequestApprovers());

			}

			if (email.isEmpty()) {
				email.add(0, null);
				email.add(1, null);
				email.add(2, null);

				message = "Error in updation !";

			} else {
				message = "Approvers are updated successfully !";
			}

			System.out.println("email 1 " + email);
			System.out.println(email.get(0));
			System.out.println(email.get(1));
			System.out.println(email.get(2));

			request.setAttribute("message", message);
			request.setAttribute("length", length);
			request.setAttribute("email1", email.get(0));
			request.setAttribute("email2", email.get(1));
			request.setAttribute("email3", email.get(2));

		} catch (SCEException e) {
			e.printStackTrace();
		}
		return new String("success");
	}

	// code end

	public String addUser() {

		// check urls

		final String TRTDEV = "sce-dev.pfizer.com/";

		final String TRTTST = "sce-tst.pfizer.com/";

		final String TRTSTG = "sce-stg.pfizer.com/";

		final String TRTPROD = "sce.pfizer.com/";

		final String TRTLOCL = "localhost:8080/";

		String mailURL = "";
		String requestURL = ((HttpServletRequest) request).getRequestURL()
				.toString();

		if (requestURL != null && requestURL.toString().contains(TRTPROD))

			mailURL = TRTPROD;

		else if (requestURL != null && requestURL.toString().contains(TRTSTG))

			mailURL = TRTSTG;

		else if (requestURL != null && requestURL.toString().contains(TRTTST))

			mailURL = TRTTST;

		else if (requestURL != null && requestURL.toString().contains(TRTLOCL))

			mailURL = TRTLOCL;

		else if (requestURL != null && requestURL.toString().contains(TRTDEV))

			mailURL = TRTDEV;

		System.out.println("Mail url is:" + mailURL);
		// end

		System.out.println("Inside add user method");

		HttpServletRequest req = getRequest();
		// try{
		String actionType;
		String role = null;
		if (SCEUtils
				.isFieldNotNullAndComplete(req.getParameter("actionStatus"))) {
			actionType = req.getParameter("actionStatus");
		} else {
			actionType = "";

		}
		String expDate = null;

		try {
			String[] buList = sceManager.getAllBUList();// added by muzees for
														// PGB and UpJOHN

			User user = new User();
			user.setEmplId(userDataForm.getEmplId());
			user.setFirstName(userDataForm.getFirstName());
			user.setLastName(userDataForm.getLastName());
			user.setNtid(userDataForm.getNtid().trim());
			user.setNtdomain(userDataForm.getNtdomain());
			user.setUserGroup(userDataForm.getUserGroup());
			user.setStatus(userDataForm.getStatus());
			user.setEmail(userDataForm.getEmail());
			user.setBusinessUnit(userDataForm.getBusinessUnit());// added by
																	// muzees
																	// for PBG
																	// and
																	// UpJOHN
			String value = request.getParameter("isAccessRequest");

			userDataForm.setIsAccessRequest(value);
			// user.setIsAccessRequested(value);

			user.setIsAccessRequest(userDataForm.getIsAccessRequest());

			/* Author: Mayank Date:07-Oct-2011 SCE Enhancement 2011 */
			user.setExpirationDate(userDataForm.getExpirationDate());

			// System.out.println(userDataForm.getExpirationDate());
			req.setAttribute("addExpirationDate",
					userDataForm.getExpirationDate());

			User u = sceManager.getUserByNTId(user.getNtid());

			// System.out.println("user id:" +user.getId());
			String message = "";

			if (u != null) {
				if (u.getUserGroup() != null) {
					role = u.getUserGroup();
				} else {
					role = "Undefined Role";
				}
				user.setId(u.getId());
				if (role.equals("SCE_TrainingTeam")) {
					role = "Training Team";
				} else if (role.equals("SCE_Administrators")) {
					role = "Admin";
				} else if (role.equals("SCE_GuestTrainer_NonMGR")) {
					role = "Guest Trainer Non Manager";
				} else if (role.equals("SCE_GuestTrainer_MGR")) {
					role = "Guest Trainer Manager";// spelling mistake updated
													// by muzees
				}

			}

			// If user is present in SCE and is Active, display warning message.
			if (u != null
					&& u.getStatus().equalsIgnoreCase("ACTIVE")
					&& (!actionType.equals("editUser") || actionType.equals(""))) {
				// Set error message
				message = "The user '" + u.getNtid()
						+ "' already exists in the system as '" + role
						+ "' and the Status of the user is 'Active'";

				userDataForm.setMessage(message);
				return new String("createUserFailed");
			}

			// If user is present in SCE and is Inactive, populate existing
			// data.
			else if (u != null
					&& u.getStatus().equalsIgnoreCase("INACTIVE")
					&& (!actionType.equals("editUser") || actionType.equals(""))) {

				message = "The user '"
						+ u.getNtid()
						+ "' already exists in the system as '"
						+ role
						+ "' and the Status of the user is 'Inactive'. Please click on 'Save' button to update the user details";

				// get existing details of the user
				// user = sceManager.getUserByNTId(form.getNtid());

				userDataForm.setId(u.getId());
				userDataForm.setEmplId(u.getEmplId());
				userDataForm.setFirstName(u.getFirstName());
				userDataForm.setLastName(u.getLastName());
				userDataForm.setNtid(u.getNtid());
				userDataForm.setNtdomain(u.getNtdomain());
				userDataForm.setStatus(u.getStatus());
				userDataForm.setEmail(u.getEmail());
				// added by muzees for PBG and UpJOHN 2019
				userDataForm.setBusinessUnit(u.getBusinessUnit());
				userDataForm.setUserGroup(u.getUserGroup());
				userDataForm.setSel_bu_id(0);
				for (int i = 0; i < buList.length; i++) {
					if (buList[i].equalsIgnoreCase(userDataForm
							.getBusinessUnit())) {
						userDataForm.setSel_bu_id(i + 1);
					}
				}// end of muzees
					// fetching user_id from dtabas eto update the user
					// details:::Date: 08-Dec-2011

				// form.setUserGroup(u.getUserGroup());
				/* Author: Mayank Date:07-Oct-2011 SCE Enhancement 2011 */

				if (user.getExpirationDate() == null
						|| user.getExpirationDate().equals("")) {
					expDate = "";
				} else {
					expDate = user.getExpirationDate();
				}
				userDataForm.setExpirationDate(user.getExpirationDate());
				req.setAttribute("actionStatus", "refresh");
				userDataForm.setMessage(message);
				return new String("createUserRefresh");

			}

			else if (u != null && u.getStatus().equalsIgnoreCase("INACTIVE")
					&& actionType.equals("editUser")) {
				sceManager.updateUser(user);
				req.removeAttribute("actionStatus");
				req.setAttribute("message",
						"User details have been saved successfully");

				// start by manish

				System.out.println("request parameter:"
						+ req.getParameter("isAccessRequest") + "user value:"
						+ user.getIsAccessRequest());

				if (req.getParameter("isAccessRequest") != null
						&& req.getParameter("isAccessRequest")
								.equalsIgnoreCase("true")) {

					System.out
							.println("inside method where mail was meant to be sent");

					String result = "";
					String ifSent = "";
					String subEmail = "Access Approved";
					String emailBody = "\n"
							+ "Dear User"
							+ "\n\n"
							+ "Your access has been approved with below details:"
							+ "\n\n" + "Name:" + " "
							+ user.getFirstName()
							+ " "
							+ user.getLastName()
							+ "\n"
							+ "Email:"
							+ " "
							+ user.getEmail()
							+ "\n"
							+ "Emplid:"
							+ " "
							+ user.getEmplId()
							+ "\n"
							+ "NTID:"
							+ " "
							+ user.getNtid()
							+ "\n"
							+ "NT Domain:"
							+ " "
							+ user.getNtdomain()
							+ "\n\n"
							+ "<div><a href='http://"
							+ mailURL
							+ "SCEWeb/'>Click here to access</a></div>"
							+ "\n\n\n\n" + "Thank You" + "\n" + "SCE Team.";

					String from = "DL-SAMSSCESupport@pfizer.com";

					String mimetype = "text/html";
					String mailJNDI = "java:jboss/SCEMailSession";

					String host = "mailhub.pfizer.com";
					Properties properties = System.getProperties();
					properties.setProperty("mail.smtp.host", host);

					Session mailSession = Session
							.getDefaultInstance(properties);

					try {

						MimeMessage message1 = new MimeMessage(mailSession);

						message1.setFrom(new InternetAddress(from));
						message1.setSubject(subEmail);
						String emailBodyFinal = emailBody.replaceAll("\n",
								"</br>");
						message1.setContent(emailBodyFinal, "text/html");

						// code to get approvers
						List<String> email = new ArrayList<String>();
						String[] strings = null;

						User[] userEmail = sceManager.getApprovers();
						Integer length = userEmail.length;

						System.out.println("Length:" + length);

						for (int i = 0; i < length; i++) {

							email.add(i, userEmail[i].getRequestApprovers());

						}

						// code end to get approvers

						if (email.isEmpty()) {
							email.add(0, "DL-SAMSSCESupport@pfizer.com");
							email.add(1, "DL-SAMSSCESupport@pfizer.com");
							email.add(2, "DL-SAMSSCESupport@pfizer.com");

						}

						String approver1 = email.get(0);
						String approver2 = email.get(1);
						String approver3 = email.get(2);

						String toMailto = user.getEmail();

						String[] toMail = new String[] { toMailto };

						String[] ccMail = new String[] { approver1, approver2,
								approver3, "manish.kumar2@pfizer.com" };

						String[] bccMail = new String[] { "manish.kumar2@pfizer.com" };

						MailUtil mailUtil1 = new MailUtil();

						mailUtil1.sendMessage(from, toMail, ccMail, bccMail,
								subEmail, emailBodyFinal, mimetype, mailJNDI);
						result = "Sent";
						ifSent = "Y";

					} catch (MessagingException mex) {
						ifSent = "N";

						mex.printStackTrace();
						result = "failure";
					}
					System.out.println("Result is:" + result);
					sceManager.updateRequestUserStatus(user);

				}

				// end

			}

			else {

				System.out.println("In else befor executing create user");

				/*
				 * String present=""; present=
				 * sceManager.getRequestUser(u.getNtid());
				 * 
				 * System.out.println("Is Present user"+present);
				 */
				sceManager.createUser(user);

				req.setAttribute("message",
						"User has been created successfully");

				/*
				 * String present=""; String status="";
				 * 
				 * present= sceManager.getRequestUser(u.getNtid());
				 * 
				 * System.out.println("Is Present user"+present);
				 * 
				 * if(present !=null) { status=
				 * sceManager.requestUserStatus(u.getNtid()); }
				 * 
				 * System.out.println("Status of request user: "+status);
				 * 
				 * if(status=="REQUESTED")
				 */
				System.out.println("request parameter:"
						+ req.getParameter("isAccessRequest") + "user value:"
						+ user.getIsAccessRequest());

				if (req.getParameter("isAccessRequest") != null
						&& req.getParameter("isAccessRequest")
								.equalsIgnoreCase("true")) {

					System.out
							.println("inside method where mail was meant to be sent");

					String result = "";
					String ifSent = "";
					String subEmail = "Access Approved";
					String emailBody = "\n"
							+ "Dear User"
							+ "\n\n"
							+ "Your access has been approved with below details:"
							+ "\n\n" + "Name:" + " "
							+ user.getFirstName()
							+ " "
							+ user.getLastName()
							+ "\n"
							+ "Email:"
							+ " "
							+ user.getEmail()
							+ "\n"
							+ "Emplid:"
							+ " "
							+ user.getEmplId()
							+ "\n"
							+ "NTID:"
							+ " "
							+ user.getNtid()
							+ "\n"
							+ "NT Domain:"
							+ " "
							+ user.getNtdomain()
							+ "\n\n"
							+ "<div><a href='http://"
							+ mailURL
							+ "SCEWeb/'>Click here to access</a></div>"
							+ "\n\n\n\n" + "Thank You" + "\n" + "SCE Team.";

					String from = "DL-SAMSSCESupport@pfizer.com";

					String mimetype = "text/html";
					String mailJNDI = "java:jboss/SCEMailSession";

					String host = "mailhub.pfizer.com";
					Properties properties = System.getProperties();
					properties.setProperty("mail.smtp.host", host);

					Session mailSession = Session
							.getDefaultInstance(properties);

					try {

						MimeMessage message1 = new MimeMessage(mailSession);

						message1.setFrom(new InternetAddress(from));
						message1.setSubject(subEmail);
						String emailBodyFinal = emailBody.replaceAll("\n",
								"</br>");
						message1.setContent(emailBodyFinal, "text/html");

						// code to get approvers
						List<String> email = new ArrayList<String>();
						String[] strings = null;

						User[] userEmail = sceManager.getApprovers();
						Integer length = userEmail.length;

						System.out.println("Length:" + length);

						for (int i = 0; i < length; i++) {

							email.add(i, userEmail[i].getRequestApprovers());

						}

						// code end to get approvers

						if (email.isEmpty()) {
							email.add(0, "DL-SAMSSCESupport@pfizer.com");
							email.add(1, "DL-SAMSSCESupport@pfizer.com");
							email.add(2, "DL-SAMSSCESupport@pfizer.com");

						}

						String approver1 = email.get(0);
						String approver2 = email.get(1);
						String approver3 = email.get(2);

						String toMailto = user.getEmail();

						String[] toMail = new String[] { toMailto };

						String[] ccMail = new String[] { approver1, approver2,
								approver3, "Shaik.Muzeeb@pfizer.com" };

						String[] bccMail = new String[] { "Shaik.Muzeeb@pfizer.com" };

						MailUtil mailUtil1 = new MailUtil();

						mailUtil1.sendMessage(from, toMail, ccMail, bccMail,
								subEmail, emailBodyFinal, mimetype, mailJNDI);
						result = "Sent";
						ifSent = "Y";

					} catch (MessagingException mex) {
						ifSent = "N";

						mex.printStackTrace();
						result = "failure";
					}
					System.out.println("Result is:" + result);
					sceManager.updateRequestUserStatus(user);

				}

			}

		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}

		return new String("success");
	}

	/*
	 * START OF MUZEES FOR PBG AND UPJOHN BU MAPPING
	 */
	public String goToBUMapping() {

		try {
			System.out.println("in gotobumapping");

			String[] salesOrgList = sceManager.getSalesOrg();
			int length = salesOrgList.length;
			if (length == 0) {
				request.setAttribute("message",
						"No Sales Organizations are available for mapping!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String("success");
	}

	public String saveBUMapping() {
		int status = 0;
		String message = "";

		try {
			String salesOrganisation = request
					.getParameter("salesOrganisation");
			String businessUnit = request.getParameter("businessUnit");

			status = sceManager.saveBUMapping(businessUnit, salesOrganisation);
			if (status == 1) {
				message = salesOrganisation + " is mapped successfully with "
						+ businessUnit;
			} else {
				message = "Error in mapping !";
			}
			request.setAttribute("message", message);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String("success");
	}

	// for deleting Bu Mapping
	public String deleteSalesOrg() {

		// int status = 0;
		String message = "";
		try {

			String salesOrganisation = request.getParameter("delSalesOrg");

			sceManager.deleteSalesOrg(salesOrganisation);
			/* if (status == 1) { */
			message = salesOrganisation + " is deleted successfully";
			/*
			 * } else { message = "Error in deletion !"; }
			 */
			// System.out.println(message+status);
			request.setAttribute("message", message);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String("success");
	}

	// end of muzees

	public LinkedHashMap getFilterUserGroups() {
		return filterUserGroups;
	}

	public void setFilterUserGroups(LinkedHashMap filterUserGroups) {
		this.filterUserGroups = filterUserGroups;
	}

}
