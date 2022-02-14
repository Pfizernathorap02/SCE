package com.pfizer.sce.Action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;





import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.sce.ActionForm.WebExFormData;
import com.pfizer.sce.beans.EventsCreated;
import com.pfizer.sce.beans.LegalConsentTemplate;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.beans.UserLegalConsent;
import com.pfizer.sce.beans.WebExDetails;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.EvaluationControllerHelper;
import com.pfizer.sce.helper.LegalConsentHelper;
import com.pfizer.sce.utils.SCEUtils;

/**
 * 
 * @author KHATED
 * Action used to add single web-ex detail and upload list of web-ex details
 *
 */
public class WebExAction extends ActionSupport implements ServletRequestAware,
		ModelDriven {
	
	private InputStream inputStream;
	private long contentLength;
	
	
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	private static SCEManagerImpl sceManager = new SCEManagerImpl();
	private WebExFormData webExFormData = new WebExFormData();
	private  HashMap<String, String> events = new HashMap<String, String>();
	private static LegalConsentHelper legalConsentHelp = new LegalConsentHelper();
	
	public static SCEManagerImpl getSceManager() {
		return sceManager;
	}

	public static void setSceManager(SCEManagerImpl sceManager) {
		WebExAction.sceManager = sceManager;
	}

	HttpServletRequest request;
	// static Logger log =
	//static Logger log = Logger.getLogger(WebExAction.class.getName());
	
	/** The log. added for struts Migration from 2.3 to 2.5.17 : by Dhananjay */
	static Logger log=LogManager.getLogger(WebExAction.class.getName());

	public WebExFormData getWebExFormData() {
		return webExFormData;
	}

	public void setWebExFormData(WebExFormData webExFormData) {
		this.webExFormData = webExFormData;
	}

	public HashMap<String, String> getEvents() {
		return events;
	}

	public void setEvents(HashMap<String, String> events) {
		this.events = events;
	}

	public static LegalConsentHelper getLegalConsentHelp() {
		return legalConsentHelp;
	}

	public static void setLegalConsentHelp(LegalConsentHelper legalConsentHelp) {
		WebExAction.legalConsentHelp = legalConsentHelp;
	}

	public String begin() {
		// System.out.println("Entry in to  begin method...");

		// log.debug("Entry in to  begin method...");
		HttpServletRequest req = getServletRequest();
		// sceManager = new SCEManagerImpl();

		String ntid = "";
		try {
			User user = (User) getSession().getAttribute("user");
			// System.out.println(" User Object:" + user);
			// log.debug(" User Object:" + user);
			if (user == null) {
				ntid = req.getHeader("IAMPFIZERUSERPFIZERNTLOGONNAME");
				System.out
						.println("Inside begin method.Getting ntid from IAM Header ntid:"
								+ ntid);
				// log.debug("Inside begin method.Getting ntid from IAM Header ntid:"+
				// ntid);
				String emplid = req.getHeader("IAMPFIZERUSERWORKFORCEID");

				System.out
						.println("begin method.Getting emplid from IAM Header emplid:"
								+ emplid);
				// log.debug("begin method.Getting emplid from IAM Header emplid:"
				// + emplid);

				String domain = req
						.getHeader("IAMPFIZERUSERPFIZERNTDOMAINNAME");

				System.out
						.println("begin method.Getting domain from IAM Header domain:"
								+ domain);
				// log.debug("begin method.Getting domain from IAM Header domain:"
				// + domain);

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

				System.out
						.println("exit from begin method before forwarding to searchForAttendee.jsp...");
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
		return webExFormData;
	}

	public String goToWebEx() {
		// webExFormData=new WebExFormData();
		webExFormData.setChairPersonPasscode("");
		webExFormData.setConfCallNumber("");
		webExFormData.setMeetingLink("");
		webExFormData.setParticipantPasscode("");
		// System.out.println("In goToWebEx method");
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
		try {
			String eventName = null;
			/* WebExFormData form = new WebExFormData(); */
			EventsCreated[] eventsList = null;
			Integer eventId = null;
			events.clear();
			eventsList = sceManager.getAllActiveInprogressEvents();
			for (int i = 0; i < eventsList.length; i++) {
				EventsCreated events2 = eventsList[i];
				String name = events2.getEventName();

				events.put(name, name);
			}
			if (!(eventsList.length > 0)) {
				events.put("select", "--Select--");
			}

			setEvents(events);
			req.setAttribute("eventsList", eventsList);
			eventName = req.getParameter("eventName");

			if (eventsList != null && eventsList.length > 0
					&& !(SCEUtils.isFieldNotNullAndComplete(eventName))) {

				EventsCreated event = (EventsCreated) eventsList[0];
				eventName = event.getEventName();
			}

			// System.out.println("the event selected is" + eventName);
			webExFormData.setEventName(eventName);
			WebExDetails[] webex = sceManager.getWebExDetailsByEvent(eventName);
			req.setAttribute("webex", webex);

		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");
	}

	public String goToAddWebEx() {
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
		String message = "";
		WebExDetails webExDetails = new WebExDetails();
		EventsCreated[] eventsList = null;
		Integer eventId = null;
		events.clear();
		eventsList = sceManager.getAllActiveInprogressEvents();
		for (int i = 0; i < eventsList.length; i++) {
			EventsCreated events2 = eventsList[i];
			String name = events2.getEventName();

			events.put(name, name);
		}
		if (!(eventsList.length > 0)) {
			events.put("select", "--Select--");
		}
		setEvents(events);
		req.setAttribute("eventsList", eventsList);
		// System.out.println("In goToAddWebEx method");
		try {
			int filesize = 0;
			if (webExFormData.getWebexFile() != null) {
				filesize = (int) (webExFormData.getWebexFile().length());
			}
			File myFile = webExFormData.getWebexFile();
			/* // System.out.println("the contenttype"+myFile.getContentType()); */
			if (filesize == 0) {
				// System.out.println("inside the upperpart");
				webExDetails.setConfCallNumber(webExFormData
						.getConfCallNumber());
				webExDetails.setChairPersonPasscode(webExFormData
						.getChairPersonPasscode());
				webExDetails.setParticipantPasscode(webExFormData
						.getParticipantPasscode());
				webExDetails.setMeetingLink(webExFormData.getMeetingLink());
				webExDetails.setEventName(webExFormData.getEventName());
				sceManager.addWebExDetails(webExDetails);
			} else {
				// System.out.println("inside the lowerpart");

				Workbook workbook = Workbook.getWorkbook(myFile);
				// Workbook workbook =
				// Workbook.getWorkbook(myFile.getInputStream());

				Sheet sheet = workbook.getSheet(0);

				int numberOfRows = sheet.getRows();
				int numberOfColumns = sheet.getColumns();
				// System.out.println("Number of rows=" + numberOfRows);
				// System.out.println("Number of columns=" + numberOfColumns);

				if (numberOfRows <= 1) {
					message = message
							+ "The file doesnot have any details.Please upload appropriate file. ";
					req.setAttribute("message", message);
					return new String("refresh");
				}

				for (int row = 1; row < numberOfRows; row++) {
					for (int column = 0; column < numberOfColumns; column++) {
						Cell cell = sheet.getCell(column, row);

						if (column == 0) {
							int columnnumber = column + 1;
							try {
								// System.out.println("the number is "+ cell.getContents());
								Double.parseDouble(cell.getContents());

								if (!(cell.getContents().length() == 10)) {
									message = message
											+ "The detail in the row " + row
											+ " and column " + columnnumber
											+ " should be of length 10";
									req.setAttribute("message", message);
									return new String("refresh");
								} else {
									webExDetails.setConfCallNumber(cell
											.getContents());
								}

							} catch (NumberFormatException e) {
								// System.out.println("Inside the catch block");
								message = message + "The detail in the row "
										+ row + " and column " + columnnumber
										+ " should be a number";
								req.setAttribute("message", message);
								return new String("refresh");
							}
						}
						if (column == 1) {
							int columnnumber = column + 1;
							try {
								Double.parseDouble(cell.getContents());
								if (cell.getContents().length() > 10) {
									message = message
											+ "The detail in the row " + row
											+ " and column " + (columnnumber)
											+ " should not be greater than 10";
									req.setAttribute("message", message);
									return new String("refresh");
								} else {
									webExDetails.setChairPersonPasscode(cell
											.getContents());
								}
							} catch (NumberFormatException e) {
								message = message + "The detail in the row "
										+ row + " and column " + columnnumber
										+ " should be a number ";
								req.setAttribute("message", message);
								return new String("refresh");
							}
						}
						if (column == 2) {
							int columnnumber = column + 1;
							try {
								Double.parseDouble(cell.getContents());
								if (cell.getContents().length() > 10) {
									message = message
											+ "The detail in the row "
											+ row
											+ " and column"
											+ column
											+ "is not appropriate.It should not be greater than 10";
									req.setAttribute("message", message);
									return new String("refresh");
								} else {
									webExDetails.setParticipantPasscode(cell
											.getContents());
								}
							} catch (NumberFormatException e) {
								message = message + "The detail in the row "
										+ row + " and column " + columnnumber
										+ " should be a number ";
								req.setAttribute("message", message);
								return new String("refresh");
							}
						}
						if (column == 3) {
							int columnnumber = column + 1;
							if (cell.getContents().indexOf(".com") > 1) {

								webExDetails.setMeetingLink(cell.getContents());
							} else {

								message = message + "The detail in the row "
										+ row + " and column " + columnnumber
										+ " should be a link ";
								req.setAttribute("message", message);
								return new String("refresh");
							}

						}

						if (column == 3) {
							webExDetails.setEventName(webExFormData
									.getEventName());
							sceManager.addWebExDetails(webExDetails);
							break;
						}

					}

				}
			}

			WebExDetails[] webex = sceManager
					.getWebExDetailsByEvent(webExFormData.getEventName());
			req.setAttribute("webex", webex);
			return new String("success");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			/* sceLogger.error(LoggerHelper.getStackTrace(e)); */
			return new String("failure");
		}
	}

	public String goToDownloadWebExTemplate(){
		HttpServletRequest req=getServletRequest(); 
		//File file=new File("evaluation/resources/xls/webex.xls");
		try {
			// System.out.println(req.getRealPath("evaluation/resources/xls/webex.xls"));
			File file=new File(req.getRealPath("evaluation/resources/xls/webex.xls"));
					
//					req.getContextPath()+"/WebContent/evaluation/resources/xls/webex.xls");
/*			File file = new File(
					"C:/Documents and Settings/KHATED/Desktop/webex.xls");*/
			// Workbook workbook = Workbook.getWorkbook(file);
			// Sheet sheet=workbook.getSheet(0);

			inputStream = new FileInputStream(file);
			contentLength = file.length();

		} catch (IOException io) {
			io.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String("success");
	}
}
