
<%@page import="java.math.BigDecimal"%>
<%@ page import="com.pfizer.sce.db.SCEManager"%>
<%@ page import="com.pfizer.sce.beans.User"%>
<%@ page import="com.pfizer.sce.common.SCEConstants"%>
<%@ page import="com.pfizer.sce.db.SCEControlImpl"%>

<%@ page import="com.pfizer.sce.utils.LoggerHelper"%>

<%@page import="java.util.*"%>
<%@page import="java.net.*"%>
<%@page import="java.io.*"%>
<%@page import="java.sql.*"%>


<%
	response.setHeader("Pragma", "public");
	response.setHeader("Cache-Control", "max-age=30");
%>
<%!	public String UNAUTHORIZED_URL = "Unauthorized.jsp";
	public String THE_nonSceUser_URL = "printBlankFormLimited.jsp";
	public String THE_sceUser_URL = "PrintBlankForm.jsp";

	/**
	 * Some functions had been removed and some functions had been modified by 
	 * Jeevitha Pothini as part of IAM Implementation.
	 * 
	 * The code creates the User object by taking the ntid,domain and employee id 
	 * from the request headers passed by IAM system.It also checks if the user is
	 * authorized to access the Application.
	 * 
	 * The code also does the session handling.
	 */

	public String getAuthUserId(String domain, String ntid) {

		String result = "";

		try {
			if (domain.length() > 0 && ntid.length() > 0) {
				domain.toUpperCase();
				ntid.toUpperCase();
				result = domain + "\\" + ntid;
			} else {
				result = "";
			}
		} catch (Exception ex) {
			result = "";

		}
		return result;
	}%>
<%
	// *********
	// switch condition between DEV and PROD - Added on 4/18/2005 by FGE 
	// The block within the IF will be executed only if the codebase is running on DEV at TGIX 
	// The session variable 'user_id" is only set in DEV 
	// The session variable 'UserId" is only set in PROD 
	try {
		String location = request.getRequestURL().toString()
				.toLowerCase();

		//boolean isLocal = location.indexOf("localhost") > -1;
		boolean isLocal = (location.indexOf("localhost") > -1
				|| location.indexOf("ASPX") > -1 || location
				.indexOf("aspx") > -1);
		//isLocal = true;
		if (isLocal) {
			LoggerHelper.logSystemDebug("Inside local");

			if (session.getAttribute("user") == null) {
				User user = new User();
				user.setFirstName("Manish");
				user.setLastName("Kumar");
				user.setNtid("KUMAM102");
				//user.setNtid("SINGHM24");
				user.setNtdomain("apac");
				user.setEmail("manish.kumar2@pfizer.com");
				user.setStatus("ACTIVE");
				//user.setUserGroup("SCE_OpsManager");
				user.setUserGroup("SCE_Administrators");
				//System.out.println("usegroup "+user.getUserGroup());
				//user.setUserGroup("SCE_GuestTrainer_NonMGR");
				
				System.out.println("User Ntid"+user.getNtid());
				System.out.println("user Group"+user.getUserGroup());
				System.out.println("Working ");
				System.out.println("Working ");
				
				//user.setUserGroup("SCE_TrainingTeam");
				// user.setEmplId("-2");
				//user.setEmplId("BAKHSR");
				user.setEmplId("68221000");
				user.setId(new Integer(1));
				session.setAttribute("user", user);
				//System.out.println("The ntid is "+user.getNtid());
				//System.out.println("The domain is "+user.getNtdomain());
				System.out.println("Working ");
			}
		} else {
			SCEControlImpl sceCtl = new SCEControlImpl();

			//(SCEControl)pageContext.getAttribute("SCEControl");

			User user = null;
			String sceVisible = "";

			user = (User) session.getAttribute("user");

			String userid = (String) session.getAttribute("UserID");
			String emplid;
			String ntid;
			String domain;

			if (userid == null) {
				Enumeration e1 = request.getHeaderNames();
				if (!e1.hasMoreElements()) {
					//gadalp response.sendRedirect("..\\sessionExpired.jsp");
					response.sendRedirect(request.getContextPath()
							+ "/sessionExpired.jsp");
				} else {
					emplid = request
							.getHeader("IAMPFIZERUSERWORKFORCEID");
					ntid = request
							.getHeader("IAMPFIZERUSERPFIZERNTLOGONNAME");
					domain = request
							.getHeader("IAMPFIZERUSERPFIZERNTDOMAINNAME");

					System.out.println("The ntid is " + ntid);
					LoggerHelper.logSystemDebug("The ntid is "+ntid);
					System.out.println("The domain is " + domain);
					LoggerHelper.logSystemDebug("The domain is " + domain);
					System.out.println("The empid is " + emplid);
					LoggerHelper.logSystemDebug("The empid is " + emplid);

					// Hardcoding for testing purpose

					if (emplid == null || ntid == null
							|| domain == null) {

						emplid = "148324";
						ntid = "gadalp";
						domain = "AMER";
					}

					userid = getAuthUserId(domain, ntid);

					System.out.println("The userid is " + userid);
					LoggerHelper.logSystemDebug("The userid is " + userid);
					
					if (userid.trim().length() > 0) {

						if (domain.length() > 0 && ntid.length() > 0) {
							System.out.println("Came inside IF");
							user = sceCtl.getUserByNTIdAndDomain(ntid,
									domain);
							/* Added for CSO requirements */
							if (user != null) {
								String sType = "";
								sType = sceCtl
										.getSalesPositionTypeCd(ntid);
								if (sType != null) {
									user.setSalesPositionTypeCd(sType);
								}
								System.out
										.println("Sales Position Type Code is "
												+ user.getSalesPositionTypeCd());
								sceVisible = sceCtl
										.getSceVisibility(user
												.getSalesPositionTypeCd());
								System.out.println("SCE Visibility is "
										+ sceVisible);
								if (sceVisible == null
										|| sceVisible
												.equalsIgnoreCase("Y")
										|| sceVisible.equals("")) {
									user.setSceVisibility("Y");
								} else {
									user.setSceVisibility("N");
								}
							}
							/*End of Addition*/
							System.out.println("Got User");
						} else {
							user = null;
						}
						
						System.out.println("The user is " + user);
						LoggerHelper.logSystemDebug("The user is " + user);
						
						if (user == null || !user.isActive()) {

							/* ------------------------DEXTER's LAB EXPERIMENT PART ONE STARTS ----------------------      */

							String url = request.getRequestURL()
									.toString();

							if (url != null) {

								System.out.println("the url is:::"
										+ url);
								//if(url.equals("http://sce.pfizer.com/SCEWeb/PrintBlankForm")){
								if (url.equals("http://localhost:7001/SCEWeb/printBlankFormLimited.jsp")) {
									response.sendRedirect(THE_nonSceUser_URL);
								} else {

								}
							} else {//url==null  

								response.sendRedirect(UNAUTHORIZED_URL);
							}

							/* ------------------------DEXTER's LAB EXPERIMENT PART ONE ENDS----------------------      */

							//out.print("no user found from DB");
							response.sendRedirect(UNAUTHORIZED_URL);
						} else {
							// out.print(" user found from DB. set session with userid " + userid);
							user.setEmplId(emplid);
							session.setAttribute("user", user);
							session.setAttribute("UserID", userid
									.trim().toLowerCase());

							/* ------------------------DEXTER's LAB EXPERIMENT PART TWO STARTS----------------------      */

							String url = request.getRequestURL()
									.toString();
							if (url != null) {

								System.out.println("the url is:::"
										+ url);
								if (url.equals("http://localhost:7001/SCEWeb/PrintBlankForm")) {
									response.sendRedirect(THE_sceUser_URL);
								} //End if(url  

								/*-----------------------------------DEXTER's LAB EXPERIMENT PART TWO ENDS----------------------      */
							}
						}
					} else {
						// response.sendRedirect("..\\sessionExpired.jsp");
						response.sendRedirect(request.getContextPath()
								+ "/sessionExpired.jsp");
					}
				}
			}
		}

	} catch (Exception ex) {
		// LoggerHelper.logSystemDebug("IAM exception:" + ex.toString());
		//insert log 

		//log.debug("IAM exception: ");

		LoggerHelper.logSystemError("", ex);
		System.out.println("IAM exception: ");
	}
%>

