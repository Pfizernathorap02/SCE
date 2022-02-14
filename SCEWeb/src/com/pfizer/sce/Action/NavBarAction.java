package com.pfizer.sce.Action;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.pfizer.sce.ActionForm.SearchForAttendeeForm;
import com.pfizer.sce.beans.LegalConsentTemplate;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.TemplateVersion;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.beans.UserLegalConsent;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.EvaluationControllerHelper;
//import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
import com.pfizer.sce.utils.SCEUtils;

public class NavBarAction implements ServletRequestAware {
	
	HttpServletRequest request;	
	
	LinkedHashMap statuses = SCEUtils.getFormStatuses();
	
	private static SCEManagerImpl sceManager = new SCEManagerImpl();
	
	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String selUserStatus) {
		this.status = selUserStatus;
	}

	public LinkedHashMap getStatuses() {
		return statuses;
	}

	public void setStatuses(LinkedHashMap statuses) {
		this.statuses = statuses;
	}

	

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
		
	public String gotoAdmin(){
        //To remove the previously sselected scoring option in the dropdown in Email Template Maintenance screen
        HttpServletRequest req = getRequest();
        HttpSession session = req.getSession(false);
        if (session==null){
        	return "failure";
        }
        session.removeAttribute("scoringOptions");
       
        String result = checkLegalConsent(req,session);
        // System.out.println("*****result*****:"+result);
        if(result != null && result.equals("success")  ){
            // System.out.println("*************Forwarding to legalConsent from gotoAdmin");
            String forwardToHomePage = "N";
            EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
            return new String("legalConsent");
        }else if(result != null && result.equals("exception")){
             // System.out.println("**********Forwarding to exception from gotoAdmin");
             return new String("failure");
        }
        return new String("success");
	}
	
	public String printBlankForm(){
		//sceManager = new SCEManagerImpl();
		TemplateVersion[] templateVersions = null;
        TemplateVersion templateVersion = null;
        // System.out.println("PrintBlankForm() called...");
        HttpServletRequest req = getRequest();
       try{
        HttpSession session = req.getSession();
        String result = checkLegalConsent(req,session);
        // System.out.println("*****result*****:"+result);
        if(result != null && result.equals("success")  ){
          // System.out.println("*************Forwarding to legalConsent");
          String forwardToHomePage = "N";
          EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
          return new String("legalConsent");
        }else if(result != null && result.equals("exception")){
           // System.out.println("**********Forwarding to exception");
           return new String("failure");
        }
        templateVersion=sceManager.retrieveFirstEvalForm("N");
        templateVersions=sceManager.retrieveEvalForm("N");
        req.setAttribute("templateVersion",templateVersion);
        //if(templateVersions!=null){
        
        req.setAttribute("templateVersions",templateVersions);
        //}
        
        req.setAttribute("formStatus", "Visible");
        
        }catch(SCEException scee){
          req.setAttribute("errorMsg",scee.getErrorCode());
          return new String("failure");
      }catch(Exception e){
          
          req.setAttribute("errorMsg","error.sce.unknown");
          //sceLogger.error(LoggerHelper.getStackTrace(e));
          return new String("failure");
      }
   return new String("success");
	}
	
	
//code added by mainsh to hide the template
	public String goToHideTemplate(){
		
		int status = 0;
		String message="";

		String hideRoleId = request.getParameter("toHideTemplateId");
		String hideRoleName = request.getParameter("toHideTemplateName");
		
		System.out.println("hideroleid"+hideRoleId);
		System.out.println("hiderolename"+hideRoleName);
		//sceManager = new SCEManagerImpl();
		TemplateVersion[] templateVersions = null;
        TemplateVersion templateVersion = null;
        // System.out.println("PrintBlankForm() called...");
        HttpServletRequest req = getRequest();
       try{
        HttpSession session = req.getSession();
        String result = checkLegalConsent(req,session);
        // System.out.println("*****result*****:"+result);
        if(result != null && result.equals("success")  ){
          // System.out.println("*************Forwarding to legalConsent");
          String forwardToHomePage = "N";
          EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
          return new String("legalConsent");
        }else if(result != null && result.equals("exception")){
           // System.out.println("**********Forwarding to exception");
           return new String("failure");
        }
        
        //added by manish
        status = sceManager.hideForm(hideRoleId);
		if(status==1)
		{
			message = "Status updated!";
		}
		else
		{
			message = "Error !";
		}
		request.setAttribute("message", message);
        //added by manish
        
        templateVersion=sceManager.retrieveFirstEvalForm("N");
        templateVersions=sceManager.retrieveEvalForm("N");
        req.setAttribute("templateVersion",templateVersion);
        //if(templateVersions!=null){
        
        req.setAttribute("templateVersions",templateVersions);
        //}
        
        req.setAttribute("formStatus", "Visible");
        
        }catch(SCEException scee){
          req.setAttribute("errorMsg",scee.getErrorCode());
          return new String("failure");
      }catch(Exception e){
          
          req.setAttribute("errorMsg","error.sce.unknown");
          //sceLogger.error(LoggerHelper.getStackTrace(e));
          return new String("failure");
      }
   return new String("success");
	}
	
	
//code end by manish to hide the template
	
	
	//code added by mainsh to unhide the template
		public String goToUnHideTemplate(){
			
			int status = 0;
			String message="";

			String unhideRoleId = request.getParameter("toUnHideTemplateId");
			String unhideRoleName = request.getParameter("toUnHideTemplateId");
			
			System.out.println("unhideroleid"+unhideRoleId);
			System.out.println("unhiderolename"+unhideRoleName);
			//sceManager = new SCEManagerImpl();
			TemplateVersion[] templateVersions = null;
	        TemplateVersion templateVersion = null;
	        // System.out.println("PrintBlankForm() called...");
	        HttpServletRequest req = getRequest();
	       try{
	        HttpSession session = req.getSession();
	        String result = checkLegalConsent(req,session);
	        // System.out.println("*****result*****:"+result);
	        if(result != null && result.equals("success")  ){
	          // System.out.println("*************Forwarding to legalConsent");
	          String forwardToHomePage = "N";
	          EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
	          return new String("legalConsent");
	        }else if(result != null && result.equals("exception")){
	           // System.out.println("**********Forwarding to exception");
	           return new String("failure");
	        }
	        
	        //added by manish
	        status = sceManager.unHideForm(unhideRoleId);
			if(status==1)
			{
				message = "Status updated!";
			}
			else
			{
				message = "Error !";
			}
			request.setAttribute("message", message);
	        //added by manish
	        
	        templateVersion=sceManager.retrieveFirstEvalForm("Y");
	        templateVersions=sceManager.retrieveEvalForm("Y");
	        req.setAttribute("templateVersion",templateVersion);
	        //if(templateVersions!=null){
	        
	        req.setAttribute("templateVersions",templateVersions);
	        //}
	        
	        req.setAttribute("formStatus", "Hidden");
	        
	        }catch(SCEException scee){
	          req.setAttribute("errorMsg",scee.getErrorCode());
	          return new String("failure");
	      }catch(Exception e){
	          
	          req.setAttribute("errorMsg","error.sce.unknown");
	          //sceLogger.error(LoggerHelper.getStackTrace(e));
	          return new String("failure");
	      }
	   return new String("success");
		}
		
		
	//code end by manish to unhide the template	
	
	public String selectFormStatus(){
	

		String userStatus = null;
		

		TemplateVersion[] templateVersions = null;
        TemplateVersion templateVersion = null;
        HttpServletRequest req = getRequest();
        
        userStatus = req.getParameter("selFormStatus");
       try{
        HttpSession session = req.getSession();
        String result = checkLegalConsent(req,session);
        // System.out.println("*****result*****:"+result);
        if(result != null && result.equals("success")  ){
          // System.out.println("*************Forwarding to legalConsent");
          String forwardToHomePage = "N";
          EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
          return new String("legalConsent");
        }else if(result != null && result.equals("exception")){
           // System.out.println("**********Forwarding to exception");
           return new String("failure");
        }
        if (userStatus.equalsIgnoreCase("Visible")) {

        	templateVersion=sceManager.retrieveFirstEvalForm("N");
            templateVersions=sceManager.retrieveEvalForm("N");
            req.setAttribute("templateVersion",templateVersion);
            req.setAttribute("templateVersions",templateVersions);
		}
		if (userStatus.equalsIgnoreCase("Hidden")) {
			templateVersion=sceManager.retrieveFirstEvalForm("Y");
	        templateVersions=sceManager.retrieveEvalForm("Y");
	        req.setAttribute("templateVersion",templateVersion);
	        req.setAttribute("templateVersions",templateVersions);
		}
		
		req.setAttribute("formStatus", userStatus);
        }catch(SCEException scee){
          req.setAttribute("errorMsg",scee.getErrorCode());
          return new String("failure");
      }catch(Exception e){
          
          req.setAttribute("errorMsg","error.sce.unknown");
          //sceLogger.error(LoggerHelper.getStackTrace(e));
          return new String("failure");
      }
   return new String("success");
	}
	
//added by manish to change ststus of the hide template
		
	
//code end
	
	
	private String checkLegalConsent(HttpServletRequest req, HttpSession session) {

		// System.out.println("Entry in to Helper.checkLegalConsent method...");
		//sceManager = new SCEManagerImpl();

		String ntid = "";
		try {
			User user = (User) session.getAttribute("user");
			// System.out.println(" User Object:" + user);
			if (user == null) {
				/*ntid = req.getHeader("IAMPFIZERUSERPFIZERNTLOGONNAME");*/
				// System.out.println("Getting ntid from IAM Header ntid:" + ntid);
				/*String emplid = req.getHeader("IAMPFIZERUSERWORKFORCEID");*/
				// System.out.println("Getting emplid from IAM Header emplid:"+ emplid);
				/*String domain = req
						.getHeader("IAMPFIZERUSERPFIZERNTDOMAINNAME");*/
				// System.out.println("Getting domain from IAM Header domain:"+ domain);
				
				if (ntid == null || ntid.equals("")) {
					// System.out.println("ntid//" + ntid + "//");
					System.out
							.println("User Object is not available in session.");
				}
				// System.out.println("ntid is ://" + ntid + "//");
			} else {
				// System.out.println("Valid User Object:" + user);
				ntid = user.getNtid();
				// System.out.println("User NTID IS:" + user.getNtid());
				// System.out.println("User emplId IS:" + user.getEmplId());
			}

			UserLegalConsent userLegalConsent = new UserLegalConsent();
			userLegalConsent = sceManager.checkLegalConsentAcceptance(ntid);
			if (userLegalConsent == null) {
				LegalConsentTemplate legalConsentTemplate = new LegalConsentTemplate();
				int nlcId = 0;
				legalConsentTemplate = sceManager.fetchLegalContent();
				// System.out.println("legalConsentTemplate.getContent() - "+ legalConsentTemplate.getContent());
				req.setAttribute("content", legalConsentTemplate.getContent());
				req.setAttribute("forLcid", legalConsentTemplate);
				System.out
						.println(" Exit from Helper.checkLegalConsent method before forwarding to legalConsent.jsp");
				return "success";

			} else {
				System.out
						.println("Exit from Helper.checkLegalConsent method before forwarding to failure");
				return "failure";
			}
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			System.out
					.println("Exit from Helper.checkLegalConsent method before forwarding to exception");
			return "exception";
		}

	}

}
