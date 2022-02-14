package com.pfizer.sce.Action;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.common.SCEConstants;
import com.pfizer.sce.db.SCEControlImpl;
import com.pfizer.sce.utils.LoggerHelper;

//shindo
//@SuppressWarnings("serial")
public class OauthAction extends ActionSupport implements
ServletRequestAware, ModelDriven<Object>{
	
	HttpServletRequest request;

	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getAuthUserId(String domain, String ntid) {

		String result = "";

		try {
			if (ntid.length() > 0) {
				//domain.toUpperCase();
				ntid.toUpperCase();
				if (domain.length() > 0) {
					domain.toUpperCase();
					result = domain + "\\" + ntid;
				} else
					result = ntid;
			} else {
				result = "";
			}
		} catch (Exception ex) {
			result = "";

		}
		return result;
	}
	
	public String authorise () {
		/*HttpServletRequest request = getServletRequest();*/
	
		HttpSession session = request.getSession();
		String location = request.getRequestURL().toString()
				.toLowerCase();
		
				//boolean isLocal = location.indexOf("localhost") > -1;
				boolean isLocal = (location.indexOf("localhost") > -1
				|| location.indexOf("ASPX") > -1 || location
				.indexOf("aspx") > -1);
				isLocal=false; //shindo oauth
		try {
			
		
				if (isLocal) {
			LoggerHelper.logSystemDebug("Inside local");

			if (session.getAttribute("user") == null) {
				User user = new User();
				/* user.setFirstName("Shaik");
				user.setLastName("Muzeeb");
				user.setNtid("FREEMA02"); */
				//user.setNtid("SINGHM24");
				/*  user.setFirstName("Omkar");
				user.setLastName("Shinde");
				user.setNtid("SHINDO");
				user.setNtdomain("APAC");
				user.setEmail("Omkar.Shinde@pfizer.com"); */
				//user.setStatus("ACTIVE"); 
				//user.setBusinessUnit("PBG");
				//user.setBusinessUnit("Non-USField");
				//user.setBusinessUnit("PBG");
				//System.out.println("usegroup "+user.getUserGroup());
				//user.setUserGroup("SCE_GuestTrainer_NonMGR");
				/* user.setUserGroup("SCE_Administrators"); */
				//user.setUserGroup("SCE_TrainingTeam");
				//user.setUserGroup("SCE_GuestTrainer_MGR");
				//user.setUserGroup("SCE_OpsManager");
				/* user.setBusinessUnit("ALL"); */
				//user.setUserGroup("SCE_TrainingTeam");
				// user.setEmplId("-2");
				//user.setEmplId("BAKHSR");
				/* user.setEmplId("2070402"); */
				user.setId(new Integer(1));
				session.setAttribute("user", user);
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
					System.out.println("returning...failureOther");
					return "failureOther";
				} else {
					/* emplid = request
							.getHeader("IAMPFIZERUSERWORKFORCEID");
					ntid = request
							.getHeader("IAMPFIZERUSERPFIZERNTLOGONNAME");
					domain = request
							.getHeader("IAMPFIZERUSERPFIZERNTDOMAINNAME"); oauth*/
					
					
							SCEControlImpl sceControl = new SCEControlImpl();
							HttpServletResponse response= ServletActionContext.getResponse();
							String userDetails = sceControl
									.getAuthenticatedUserID(request,response);
							System.out.println("Oauth details  : "+userDetails);
							
							if (userDetails!=null && userDetails.length()>0){
								String[] userDetailsArray = userDetails.split(",");
								ntid = userDetailsArray[0];
								domain = userDetailsArray[1];
								emplid = userDetailsArray[2];
							} else{
								System.out.println("returning...unauthorised1");
								return "unauthorised";
							}

							// Hardcoding for testing purpose

							/* if (emplid == null || ntid == null
									|| domain == null) {

								emplid = "148324";
								ntid = "gadalp";
								domain = "AMER";
							} */

							userid = getAuthUserId(domain, ntid);

							if (userid.trim().length() > 0) {
								System.out.println("The ntid is " + ntid);
								LoggerHelper.logSystemDebug("The ntid is "
										+ ntid);
								System.out.println("The domain is " + domain);
								LoggerHelper.logSystemDebug("The domain is "
										+ domain);
								System.out.println("The empid is " + emplid);
								LoggerHelper.logSystemDebug("The empid is "
										+ emplid);
								System.out.println("The userid is " + userid);
								LoggerHelper.logSystemDebug("The userid is "
										+ userid);
								if (ntid.length() > 0) {
									System.out.println("Came inside IF");
									/* user = sceCtl.getUserByNTIdAndDomain(ntid,domain); 2020 Q2:domain required logic condition removed*/
									user = sceCtl.getUserByNTIdAndDomain(ntid);
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
								LoggerHelper.logSystemDebug("The user is "
										+ user);

								if (user == null || !user.isActive()) {

									/* ------------------------DEXTER's LAB EXPERIMENT PART ONE STARTS ----------------------      */

									String url = request.getRequestURL()
											.toString();

									if (url != null) {

										System.out.println("the url is:::"
												+ url);
										//if(url.equals("http://sce.pfizer.com/SCEWeb/PrintBlankForm")){
										if (url.equals("http://localhost:7001/SCEWeb/printBlankFormLimited.jsp")) {
											System.out.println("returning...nonsce");
											return "nonsce";
										} else {

										}
									} else {//url==null  
										System.out.println("returning...unauthorised2");
										return "unauthorised";
									}

									/* ------------------------DEXTER's LAB EXPERIMENT PART ONE ENDS----------------------      */

									//out.print("no user found from DB");
									return "unauthorised";
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
											return "sce";
										} //End if(url  

										/*-----------------------------------DEXTER's LAB EXPERIMENT PART TWO ENDS----------------------      */
									}
								}
							} else {
								// response.sendRedirect("..\\sessionExpired.jsp");
							return "session";
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
			
		return "failure";
	}
	
	public HttpServletRequest getRequest() {
		return this.request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public String invite () {
		HttpSession session = request.getSession();
		String bookMarkURL = null;
		
		return "";
	}


}


