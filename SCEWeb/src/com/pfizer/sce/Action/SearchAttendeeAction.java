package com.pfizer.sce.Action;

import java.net.URL;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.sce.ActionForm.SearchForAttendeeForm;
import com.pfizer.sce.beans.Attendee;
import com.pfizer.sce.beans.LegalConsentTemplate;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.beans.UserLegalConsent;
import com.pfizer.sce.db.SCEControlImpl;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.EvaluationControllerHelper;
import com.pfizer.sce.helper.LegalConsentHelper;
import com.pfizer.sce.utils.SCEUtils;

/**
 * @author gadalp SearchAttendeeAction will act as Action class for
 *         seachforAttendee.jsp
 * 
 */
//@SuppressWarnings("serial")
public class SearchAttendeeAction extends ActionSupport implements
		ServletRequestAware, ModelDriven<Object> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fEmplid;
	private String lOrder;
	private String fOrder;
	private String pageName;
	
	private String pageURL;


	public String getPageURL() {
		return pageURL;
	}

	public void setPageURL(String pageURL) {
		this.pageURL = pageURL;
	}

	public String getfEmplid() {
		return fEmplid;
	}

	public void setfEmplid(String fEmplid) {
		this.fEmplid = fEmplid;
	}

	public String getlOrder() {
		return lOrder;
	}

	public void setlOrder(String lOrder) {
		this.lOrder = lOrder;
	}

	public String getfOrder() {
		return fOrder;
	}

	public void setfOrder(String fOrder) {
		this.fOrder = fOrder;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public static SCEManagerImpl getSceManager() {
		return sceManager;
	}

	public static void setSceManager(SCEManagerImpl sceManager) {
		SearchAttendeeAction.sceManager = sceManager;
	}

	HttpServletRequest request;
	// static Logger log =
	// Logger.getLogger(SearchAttendeeAction.class.getName());

	private static SCEManagerImpl sceManager = new SCEManagerImpl();

	private SearchForAttendeeForm searchForAttendeeForm = new SearchForAttendeeForm();
	private LegalConsentHelper legalConsentHelper = new LegalConsentHelper(); 
	
	public SearchForAttendeeForm getSearchForAttendeeForm() {
		return searchForAttendeeForm;
	}

	public void setSearchForAttendeeForm(
			SearchForAttendeeForm searchForAttendeeForm) {
		this.searchForAttendeeForm = searchForAttendeeForm;
	}

	private static HashMap map = sceManager.getAllEventMap();

	public HashMap getMap() {
		return map;
	}

	public void setMap(HashMap map) {
		this.map = map;
	}

	public String begin() {
		// System.out.println("Entry in to  begin method...");

		// log.debug("Entry in to  begin method...");
		HttpServletRequest req = getServletRequest();
		
		// sceManager = new SCEManagerImpl();
		
		String ntid = "";
		String domain="";
		String emplid="";
		try {
			User user = (User) getSession().getAttribute("user");
			// System.out.println(" User Object:" + user);
			// log.debug(" User Object:" + user);
			if (user == null) {
				/*ntid = req.getHeader("IAMPFIZERUSERPFIZERNTLOGONNAME");
				System.out
						.println("Inside begin method.Getting ntid from IAM Header ntid:"
								+ ntid);*/
				// log.debug("Inside begin method.Getting ntid from IAM Header ntid:"+
				// ntid);
				/*String emplid = req.getHeader("IAMPFIZERUSERWORKFORCEID");

				System.out
						.println("begin method.Getting emplid from IAM Header emplid:"
								+ emplid);*/
				// log.debug("begin method.Getting emplid from IAM Header emplid:"
				// + emplid);

				/*String domain = req
						.getHeader("IAMPFIZERUSERPFIZERNTDOMAINNAME");

				System.out
						.println("begin method.Getting domain from IAM Header domain:"
								+ domain);*/
				// log.debug("begin method.Getting domain from IAM Header domain:"
				// + domain);
				//Oauth start
				
				
				SCEControlImpl sceControl= new SCEControlImpl();
				HttpServletResponse response= ServletActionContext.getResponse();

				
				
				String userDetails= sceControl.getAuthenticatedUserID(req, response);
				System.out.println(userDetails);
				String[] userDetailsArray = userDetails.split(",");
				
				if (userDetails!=null && userDetails.length()>0){
					userDetailsArray = userDetails.split(",");
					ntid = userDetailsArray[0];
					domain = userDetailsArray[1];
					emplid = userDetailsArray[2];
				}
				//Oauth end
				
				/* NT id hard coded for testing purpose**/
				
				if (emplid == null || ntid == null || domain == null) {

					/*emplid = "148324";
					ntid = "gadalp";
					domain = "AMER";*/
				}
				

				if (ntid == null || ntid.equals("")) {
					// System.out.println("begin method.ntid//" + ntid + "//");
					// log.debug("begin method.ntid//" + ntid + "//");
					System.out
							.println("begin method.User Object is not available in session.");

					// log.debug("begin method.User Object is not available in session.");

				}
			} else {
				// System.out.println("Valid User Object:" + user);
				// log.debug("Valid User Object:" + user);
				ntid = user.getNtid();
				// // System.out.println("User NTID IS :" + user.getNtid());
				// log.debug("User NTID IS :" + user.getNtid());
				// log.debug("User emplId IS :" + user.getEmplId());
			}

			UserLegalConsent userLegalConsent = new UserLegalConsent();
			userLegalConsent = sceManager.checkLegalConsentAcceptance(ntid);
			if (userLegalConsent == null) {
				LegalConsentTemplate legalConsentTemplate = new LegalConsentTemplate();

				legalConsentTemplate = sceManager.fetchLegalContent();
				// System.out.println("legalConsentTemplate.getContent() - "+ legalConsentTemplate.getContent());

				// log.debug("legalConsentTemplate.getContent() - " +
				// legalConsentTemplate.getContent());
				req.setAttribute("content", legalConsentTemplate.getContent());
				req.setAttribute("forLcid", legalConsentTemplate);
				System.out
						.println("Exit from begin method before forwarding to legalConsent.jsp...");
				// log.debug("Exit from begin method before forwarding to legalConsent.jsp...");
				return new String("success");
			} else {

				//System.out.println("exit from begin method before forwarding to searchForAttendee.jsp...");
				// log.debug("exit from begin method before forwarding to searchForAttendee.jsp...");

				// request.getSession().setAttribute("eventMap", map);
				return new String("failure");
			}
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			// System.out.println("Exception in begin method");
			e.printStackTrace();
			
			// log.error("Exception in begin method", e);
			System.out
					.println("exit from begin method before forwarding to (exception)errorPage.jsp...");
			// log.debug("Exit from begin method before forwarding to (exception)errorPage.jsp...");
			return new String("failureOther");
		}

	}

	public String gotoSearchForAttendee() {
		HttpServletRequest req = getServletRequest();
		HttpSession session = req.getSession(false);
		String result = legalConsentHelper.checkLegalConsent(req, session);
		// System.out.println("*****result*****:" + result);
		// log.debug("*****result*****:"+result);
		if (result != null && result.equals("success")) {
			System.out
					.println("*************Forwarding to legalConsent from gotoAdmin");
			// log.debug("*************Forwarding to legalConsent from gotoAdmin");
			String forwardToHomePage = "N";
			EvaluationControllerHelper.setBookMarkURL(session, req,
					forwardToHomePage);
			return new String("legalConsent");
		} else if (result != null && result.equals("exception")) {
			System.out
					.println("**********Forwarding to exception from gotoAdmin");
			// log.debug("**********Forwarding to exception from gotoAdmin");
			return new String("failure");
		}
		// System.out.println(" searchAttendeeForm  " + searchForAttendeeForm);

		if (searchForAttendeeForm.getEventId() == null) {
			searchForAttendeeForm.setEventId(sceManager.getDefaultEventId());
		}
		// return new Forward("success", form);

		return new String("success");
	}



	public String searchAttendee() {
		HttpServletRequest req = getServletRequest();
		try {
			HttpSession session = req.getSession();
			String result = legalConsentHelper.checkLegalConsent(req, session);
			 //System.out.println("*****result*****:" + result);
			// log.debug("*****result*****:"+result);
			if (result != null && result.equals("success")) {
				// System.out.println("*************Forwarding to legalConsent");
				// log.debug("*************Forwarding to legalConsent");
				String forwardToHomePage = "Y";
				EvaluationControllerHelper.setBookMarkURL(session, req,
						forwardToHomePage);
				return new String("legalConsent");
			} else if (result != null && result.equals("exception")) {
				// System.out.println("**********Forwarding to exception");
				// log.debug("**********Forwarding to exception");
				return new String("failure");
			}
			String pageName = req.getParameter("pageName");
			if (pageName == null
					|| !pageName.equals("searchForAttendeeJSPForm")) {
				// System.out.println("This is Bookmark request");
				URL url = new URL(req.getRequestURL().toString());
				String domain = url.getHost();
				pageURL = "http://" + domain
						+ "/SCEWeb/gotoSearchForAttendee.action";
				// System.out.println("pageURL:" + pageURL);
				URL forwardURL = new URL(pageURL);
				// System.out.println("forwardURL::::::" + forwardURL);

				/*
				 * This code is for bookmarking the request. This will be
				 * handelled seperately.
				 */
				 return new String("forwardURL");

			}

			String isPassport = "N";

			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("isPassport"))) {
				isPassport = req.getParameter("isPassport");
			}

			/* Added for CSO requirements */
			User user = (User) getSession().getAttribute("user");
			// System.out.println("User in Evaluation Controller" + user);
			// log.debug("User in Evaluation Controller"+user);

			String isVisible = "";
			
			isVisible = user.getSceVisibility();
			
			//updated by muzees for PGB and UpJOHN segregation 2019
			//start
			String salesPositionTypeCd = "";
			salesPositionTypeCd = user.getSalesPositionTypeCd();
			String userGroup=user.getUserGroup();
			String bussinessUnit=user.getBusinessUnit();
			System.out.println("isVisible:"+isVisible);
			System.out.println("salesPositionTypeCd:"+salesPositionTypeCd);
			
			Attendee[] attendees = sceManager.getAttendeesBySearchCriteria(
					searchForAttendeeForm.getEventId(),
					searchForAttendeeForm.getLastName(),
					searchForAttendeeForm.getFirstName(),
					searchForAttendeeForm.getEmplId(),
					searchForAttendeeForm.getSalesPositionId(), isPassport,
					isVisible, salesPositionTypeCd,userGroup,bussinessUnit);
			//end
			req.setAttribute("attendees", attendees);
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));

			// log.error("Exception in SearchAttendee", e);
			
			// System.out.println("Exception in SearchAttendee");
			e.printStackTrace();
			return new String("failure");
		}
		return new String("success");
	}

	
	
	
	public String searchAgain() {
		HttpServletRequest req = getServletRequest();
		try {
			HttpSession session = req.getSession();
			String result = legalConsentHelper.checkLegalConsent(req, session);
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
			/*
			 * String pageName = req.getParameter("pageName"); if(pageName ==
			 * null || !pageName.equals("searchForAttendeeForm")){
			 * // System.out.println("This is Bookmark request"); URL url = new
			 * URL(req.getRequestURL().toString()); String domain =
			 * url.getHost(); String pageURL =
			 * "http://"+domain+"/SCEWeb/evaluation/gotoSearchForAttendee.do";
			 * // System.out.println("pageURL:"+pageURL); URL forwardURL = new
			 * URL(pageURL); // System.out.println("forwardURL::::::"+forwardURL);
			 * return new Forward(forwardURL);
			 * 
			 * }
			 */
			if (searchForAttendeeForm.getEventId() == null) {
				searchForAttendeeForm
						.setEventId(sceManager.getDefaultEventId());
			}
			// Reset Other Fields
			searchForAttendeeForm.setLastName(null);
			searchForAttendeeForm.setFirstName(null);
			searchForAttendeeForm.setEmplId(null);
			// form.setTerritoryId(null);
			searchForAttendeeForm.setSalesPositionId(null);
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			// System.out.println("inside searchAgain Exception ");
			e.printStackTrace();
			return new String("failure");
		}
		return new String("success");
	}
    
    
	public String search2() {
		HttpServletRequest req = getRequest();
		try {
			HttpSession session = req.getSession();
			String result = legalConsentHelper.checkLegalConsent(req, session);
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

			String pageName = req.getParameter("pageName");
			if (pageName == null || !pageName.equals("searchForAttendeeForm")) {
				// System.out.println("This is Bookmark request");
				URL url = new URL(req.getRequestURL().toString());
				String domain = url.getHost();

				pageURL = "http://" + domain + "/SCEWeb/searchHome.action";
				if (domain != null && domain.equalsIgnoreCase("localhost")) {
					pageURL = "http://" + domain
							+ ":8080/SCEWeb/searchHome.action";
				}
				// System.out.println("pageURL:" + pageURL);

				// System.out.println("forwardURL::::::");
				return new String("forwardURL");

			}

			String isPassport = "N";

			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("isPassport"))) {
				isPassport = req.getParameter("isPassport");
			}

			/* Added for CSO requirements */
			User user = (User) getSession().getAttribute("user");
			// System.out.println("User in Evaluation Controller" + user);
			String isVisible = "";
			String salesPositionTypeCd = "";
			isVisible = user.getSceVisibility();
			salesPositionTypeCd = user.getSalesPositionTypeCd();
			String userGroup=user.getUserGroup();
			String bussinessUnit=user.getBusinessUnit();
			Attendee[] attendees = sceManager.getAttendeesBySearchCriteria(
					searchForAttendeeForm.getEventId(),
					searchForAttendeeForm.getLastName(),
					searchForAttendeeForm.getFirstName(),
					searchForAttendeeForm.getEmplId(),
					searchForAttendeeForm.getSalesPositionId(), isPassport,
					isVisible, salesPositionTypeCd,userGroup,bussinessUnit);

			req.setAttribute("attendees", attendees);

		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");
	} 
    
	public String downloadBlankForm() {
		HttpServletRequest req = getServletRequest();
		Integer templateVersionId = null;
		String templateName = req.getParameter("selTemplateName");
		// System.out.println("templateName:" + templateName);
		String templateId = req.getParameter("templateId");
		// System.out.println("templateId:" + templateId);
		String selTemplateVersionId = req.getParameter("selTemplateVersionId");
		// System.out.println("selTemplateVersionId:" + selTemplateVersionId);
		try {

			// // System.out.println((String)req.getParameter("selTemplateName"));
			// // System.out.println(new
			// Integer(req.getParameter("selTemplateVersionId")));

			if (SCEUtils.isFieldNotNullAndComplete(selTemplateVersionId)) {

				templateVersionId = new Integer(selTemplateVersionId);
				// System.out.println("templateVersionId ====="+ templateVersionId);
			} else {
				if (selTemplateVersionId == null
						|| selTemplateVersionId.equals("")) {
					if (templateId != null && (!templateId.equals(""))) {
						templateVersionId = sceManager
								.getTemplateVersionId(new Integer(templateId));
						// System.out.println("templateVersionId ::::::"+ templateVersionId);
					}
				}
			}

			// req.setAttribute("templateVersionId",templateVersionId.toString());
			req.setAttribute("templateVersionId", templateVersionId);
			req.setAttribute("templateName", templateName);
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");
	}
    
    

	@Override
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

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return searchForAttendeeForm;
	}

}
