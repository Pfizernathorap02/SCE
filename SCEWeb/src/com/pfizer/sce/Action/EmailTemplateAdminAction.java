package com.pfizer.sce.Action;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.sce.beans.EmailTemplate;
import com.pfizer.sce.beans.EmailTemplateForm;
import com.pfizer.sce.beans.LegalConsentTemplate;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.beans.UserLegalConsent;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.EvaluationControllerHelper;
import com.pfizer.sce.utils.SCEUtils;

public class EmailTemplateAdminAction extends ActionSupport implements
		ServletRequestAware {
	String pageURL;
	public String getPageURL() {
		return pageURL;
	}

	public void setPageURL(String pageURL) {
		this.pageURL = pageURL;
	}

	private HashMap initial;
	private EmailTemplateForm emailTemplateForm=new EmailTemplateForm();
	
	public EmailTemplateForm getEmailTemplateForm() {
		return emailTemplateForm;
	}

	public void setEmailTemplateForm(EmailTemplateForm emailTemplateForm) {
		this.emailTemplateForm = emailTemplateForm;
	}

	public HashMap getInitial() {
		return initial;
	}

	public void setInitial(HashMap initial) {
		this.initial = initial;
	}

	HttpServletRequest request;
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	public HttpServletRequest getServletRequest() {
		return this.request;
	}
	
	private static SCEManagerImpl sceManager = new SCEManagerImpl();
	
	private String evaluationTemplateName;
	
	public String getEvaluationTemplateName() {
		return evaluationTemplateName;
	}

	public void setEvaluationTemplateName(String evaluationTemplateName) {
		this.evaluationTemplateName = evaluationTemplateName;
	}



	private String scoringOption;

	public String getScoringOption() {
		return scoringOption;
	}

	public void setScoringOption(String scoringOption) {
		this.scoringOption = scoringOption;
	}



    private String pageName;

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	
	private String selEvaluationTemplateId;
	
	
	
	public String getSelEvaluationTemplateId() {
		return selEvaluationTemplateId;
	}

	public void setSelEvaluationTemplateId(String selEvaluationTemplateId) {
		this.selEvaluationTemplateId = selEvaluationTemplateId;
	}

	public String getSelScoringSystemIdentifier() {
		return selScoringSystemIdentifier;
	}

	public void setSelScoringSystemIdentifier(String selScoringSystemIdentifier) {
		this.selScoringSystemIdentifier = selScoringSystemIdentifier;
	}

	private String selScoringSystemIdentifier;
	
	
	//private HashMap evaluationTemplateTemp = new HashMap();
//	private HashMap scoringOptionMapTemp = new HashMap();
//	public HashMap getScoringOptionMap() {
//		return scoringOptionMapTemp;
//	}
//
//	
//
//	public HashMap getScoringOptionMapTemp() {
//		return scoringOptionMapTemp;
//	}
//
//	public void setScoringOptionMapTemp(HashMap scoringOptionMapTemp) {
//		this.scoringOptionMapTemp = scoringOptionMapTemp;
//	}
//
//	
//
//	public HashMap getEvaluationTemplateTemp() {
//		return evaluationTemplateTemp;
//	}
//
//	public void setEvaluationTemplateTemp(HashMap evaluationTemplateTemp) {
//		this.evaluationTemplateTemp = evaluationTemplateTemp;
//	}

	



	public String gotoEmailTemplateAdmin() {
		// EmailTemplate emailtemplate =
		// (EmailTemplate)getSession().getAttribute("emailtemplates");
		// System.out.println("inside my action class");
		HttpServletRequest req = getServletRequest();
		HttpSession session = req.getSession(true);
		String result = checkLegalConsent(req, session);
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
		Integer evaluationTemplateId = new Integer(-1);
		req.setAttribute("evaluationTemplateId", evaluationTemplateId);
		req.setAttribute("defaultScoringOption", "--Select--");
		session.removeAttribute("scoringOptions");
		try {
			HashMap evaluationTemplate = null;
			// System.out.println("Calling the scemanager getEvaluationTemplate Method");
			evaluationTemplate =sceManager.getAllEvaluationTemplatesMap();
			//setEvaluationTemplateTemp(evaluationTemplate);
			session.setAttribute("evaluationTemplate", evaluationTemplate);
			// System.out.println("set the value in session scope");
		} catch (Exception e) {
			// System.out.println("we got an exception");
		}
		return new String("success");
	}

	 public String getScoringOptions() 
	    {
	        //HttpServletRequest req = ScopedServletUtils.getOuterRequest(getRequest());
		   
		 try{
			// System.out.println("inside getScoringOptions method");
			HttpServletRequest req = getServletRequest(); 
	        HttpSession session = req.getSession(true);
	        String result = checkLegalConsent(req,session);
	        // System.out.println("*****result*****:"+result);
	        if(result != null && result.equals("success")  ){
	            // System.out.println("*************Forwarding to legalConsent");
	            String forwardToHomePage = "Y";
	            EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
	            return new String("legalConsent");
	        }else if(result != null && result.equals("exception")){
	            // System.out.println("**********Forwarding to exception");
	            return new String("failure");
	        }
	        // System.out.println("value of pagename is"+ req.getParameter("pageName"));
	        String pageName = req.getParameter("pageName");
	        if(pageName == null || !pageName.equals("scoringOptions")){
	            // System.out.println("This is Bookmark request");   
	            URL url = new URL(req.getRequestURL().toString());
	            String domain = url.getHost();
	            pageURL = "http://"+domain+"/SCEWeb/gotoAdmin.action";
	            // System.out.println("pageURL:"+pageURL);
	            URL forwardURL = new URL(pageURL);
	            // System.out.println("forwardURL::::::"+forwardURL);
	            return new String(forwardURL+"");
	            
	        } 
	        Integer evaluationTemplateId = null;
	        HashMap evaluationTemplate = null;
			// System.out.println("Calling the scemanager getEvaluationTemplate Method");
			evaluationTemplate =sceManager.getAllEvaluationTemplatesMap();
			//setEvaluationTemplateTemp(evaluationTemplate);
			session.setAttribute("evaluationTemplate", evaluationTemplate);
			// System.out.println("set the value in session scope");
	       HashMap scoringOptionsMap = null;
	        if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("selEvaluationTemplateId"))) {
	            evaluationTemplateId = new Integer(req.getParameter("selEvaluationTemplateId"));
	        }
	        // System.out.println("value of evaluationTemplateId is"+evaluationTemplateId);
	        req.removeAttribute("scoringOptions");
	        scoringOptionsMap = sceManager.getScoringOptions(evaluationTemplateId);
	        //req.setAttribute("scoringOptions",scoringOptionsMap);
	        session.setAttribute("scoringOptions",scoringOptionsMap);
	        //setScoringOptionMapTemp(scoringOptionMap);
	        req.setAttribute("default","checkDefault");
	       /*if (scoringOptionsMap!=null){
	        
	            Collection c = scoringOptionsMap.values();
	            Iterator itr = c.iterator();
	            String defaultOption = null;
	            defaultOption = (String)itr.next();
	            // System.out.println("default" +defaultOption);
	            //req.setAttribute("defaultScoringOption",defaultOption);
	        }*/
	        req.setAttribute("evaluationTemplateId",evaluationTemplateId);
	        }catch(SCEException scee){
	            //req.setAttribute("errorMsg",scee.getErrorCode());
	            return new String("failure");
	        }catch(Exception e){
	            //req.setAttribute("errorMsg","error.sce.unknown");
	            //sceLogger.error(LoggerHelper.getStackTrace(e));
	            return new String("failure");
        }
	        return new String("success");
	    }
	
	
	 
	 
	 public String searchEmailTemplates() 
	    {
		 HttpServletRequest req = getServletRequest(); 	        
	        // System.out.println("Inside search action");
	        String scoringOption = "";
	        String body = "";
	        String subject = "";
	        try{
	        //scoringOption = "SMC";
	        Integer evaluationTemplateId =null;
	        // System.out.println("selEvaluationTemplateId from request:"+req.getParameter("selEvaluationTemplateId"));
	        if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("selEvaluationTemplateId"))) {
	            evaluationTemplateId = new Integer(req.getParameter("selEvaluationTemplateId"));
	        }
	        if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("selScoringSystemIdentifier"))) {
	            scoringOption = (String)(req.getParameter("selScoringSystemIdentifier"));
	        }
	         // System.out.println("value of evaluationTemplateId"+evaluationTemplateId);
	        // Get All Template Versions
	        EmailTemplate[] emailTemplates = sceManager.searchEmailTemplates(evaluationTemplateId,scoringOption);
	        // System.out.println("feteched the emailTemplates");
	        for(int i=0;i<emailTemplates.length;i++){
	            if(emailTemplates[i].getEmailBody()!=null){
	                body= emailTemplates[i].getEmailBody();
	                //body=body.replaceAll("%0D%0A","\r\n");
	                //body = body.replaceAll("%27","\'");
	                //body = body.replaceAll("&quote;","\"");
	                emailTemplates[i].setEmailBody(body);
	            }
	            if(emailTemplates[i].getEmailSubject()!=null){
	                subject = emailTemplates[i].getEmailSubject();
	                //subject = subject.replaceAll("%27","\'");
	                //subject = subject.replaceAll("&quote;","\"");
	                emailTemplates[i].setEmailSubject(subject);
	            }
	        }


	        //req.setAttribute("selEvaluationTemplateId",evaluationTemplateId);
	       // req.setAttribute("selScoringSystemIdentifier",scoringOption);
	        req.setAttribute("emailtemplates",emailTemplates);
	        req.setAttribute("evaluationTemplateId",evaluationTemplateId);
	        // System.out.println("default"+scoringOption);
	        req.setAttribute("defaultScoringOption",scoringOption);
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
	 
	 
	 public String publishEmailTemplate() 
	    {
		 HttpServletRequest req = getServletRequest(); 
		 Integer emailTemplateId=null;
	        Integer evaluationTemplateId=null;
	        String scoringOption=null;
	        try{
	        String pageName = req.getParameter("pageName");    
	        HttpSession session = req.getSession();
	        String result = checkLegalConsent(req,session);
	        // System.out.println("*****result*****:"+result);
	        if(result != null && result.equals("success")  ){
	            // System.out.println("*************Forwarding to legalConsent");
	            String forwardToHomePage = "Y";
	            EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
	            return "legalConsent";
	        }else if(result != null && result.equals("exception")){
	            // System.out.println("**********Forwarding to exception");
	            return "failure";
	        } 
	         
	        if(pageName == null || !pageName.equals("scoringOptions")){
	            // System.out.println("This is Bookmark request");   
	            URL url = new URL(req.getRequestURL().toString());
	            String domain = url.getHost();
	            pageURL = "http://"+domain+"/SCEWeb/gotoAdmin.action";
	            // System.out.println("pageURL:"+pageURL);
	            URL forwardURL = new URL(pageURL);	           
	            // System.out.println("forwardURL::::::"+forwardURL);
	            
	            
	        } 
	        if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("selEmailTemplateId"))) {
	        emailTemplateId = new Integer(req.getParameter("selEmailTemplateId"));}
	        if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("selEvaluationTemplateId"))) {
	        evaluationTemplateId = new Integer(req.getParameter("selEvaluationTemplateId"));}
	        if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("selScoringSystemIdentifier"))) {
	        scoringOption = (String)(req.getParameter("selScoringSystemIdentifier"));}
	        // System.out.println("Inside publish method"); 
	        String message="";  
	        
	              message = sceManager.publishEmailTemplate(emailTemplateId);
	              EmailTemplate[] emailTemplates = sceManager.searchEmailTemplates(evaluationTemplateId,scoringOption);
	              req.setAttribute("emailtemplates",emailTemplates);
	              req.setAttribute("evaluationTemplateId",evaluationTemplateId);
	              req.setAttribute("defaultScoringOption",scoringOption);
	              req.setAttribute("message",message); 
	        }catch(SCEException scee){
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return "failure";
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	            //sceLogger.error(LoggerHelper.getStackTrace(e));
	            return "failure";
	        }
	        
	        return "success";
	    }

	 public String gotoEditEmailTemplate() 
	    {
		 HttpServletRequest req = getServletRequest(); 
		 com.pfizer.sce.beans.EmailTemplateForm emailtemplates=null;
	        Integer emailTemplateId = null;
	        Integer evaluationTemplateId = null;
	        String scoringOption  = "";
	        String body="";
	        String subject="";
	        req.getAttribute("selEvaluationTemplateId");
	        try{        
	        if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("selEmailTemplateId"))) {
	            emailTemplateId = new Integer(req.getParameter("selEmailTemplateId")); }
	        if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("selEvaluationTemplateId"))) {
	        evaluationTemplateId = new Integer(req.getParameter("selEvaluationTemplateId"));}
	        if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("selScoringSystemIdentifier"))) {
	        scoringOption = (String)(req.getParameter("selScoringSystemIdentifier"));
	        }
	            emailtemplates = sceManager.getSelectedEmailTemplate(emailTemplateId);
	            subject=emailtemplates.getEmailSubject();
	          body=emailtemplates.getEmailBody();
	          //// System.out.println("emailbody edit:"+body);
	          if (subject!=null){
	          subject=subject.replaceAll("%27","\'");
	          subject = subject.replaceAll("&quote;","\"");
	          }
	          if(body!=null){
	          body = body.replaceAll("%27","\'");
	          body=body.replaceAll("%0D%0A","\r\n");
	          body = body.replaceAll("&quote;","\"");   
	          }
	            emailtemplates.setEmailSubject(subject);
	             emailtemplates.setEmailBody(body);
	            emailtemplates.setEmailTemplateId(emailTemplateId);
	            emailtemplates.setEvaluationTemplateId(evaluationTemplateId);
	            emailtemplates.setScoringSystemIdentifier(scoringOption);
	            req.setAttribute("emailtemplates",emailtemplates);
	            req.setAttribute("evaluationTemplateId",evaluationTemplateId);
	            req.setAttribute("defaultScoringOption",scoringOption);
	           
	        
	        }catch(SCEException scee){
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return "failure";
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	           // sceLogger.error(LoggerHelper.getStackTrace(e));
	            return "failure";
	        }
	         return "success";
	         
	    }
	 
	 public String saveEmailTemplate(){
	        
		 HttpServletRequest req = getServletRequest();
		 HttpSession session = req.getSession(true);
		 Integer evaluationTemplateId = null;
		 String evaluationTemplateName="";
	        String scoringOption = "";
	        String message="";
	        String body ="";
	        String subject ="";
	        try{
	            
	            EmailTemplate emailTemplate = null;
	            emailTemplate = new EmailTemplate();
	            emailTemplate.setEmailTemplateId(new Integer(req.getParameter("emailTemplateId")));
	            emailTemplate.setEvaluationTemplateName(req.getParameter("evaluationTemplateName"));
	            evaluationTemplateName=req.getParameter("evaluationTemplateName");
	            evaluationTemplateId = new Integer(req.getParameter("selEvaluationTemplateId"));
	            emailTemplate.setEvaluationTemplateId(new Integer(req.getParameter("selEvaluationTemplateId")));
	            scoringOption = req.getParameter("selScoringSystemIdentifier");
	            emailTemplate.setEmailTemplateVersion(req.getParameter("emailTemplateVersion"));
	            emailTemplate.setOverallScore(req.getParameter("overallScore"));
	            emailTemplate.setEvaluationTemplateVersionId(new Integer(req.getParameter("templateVersionId")));
	            emailTemplate.setScoringSystemIdentifier(req.getParameter("selScoringSystemIdentifier"));
	            emailTemplate.setPublishFlag( req.getParameter("publishFlag"));
	            emailTemplate.setEmailCc(req.getParameter("email_cc"));
	            emailTemplate.setEmailBCc(req.getParameter("email_bcc"));
	            
	            // System.out.println("email body before replace " +body);
	            subject = (String)(req.getParameter("email_subject"));
	            body = (String)(req.getParameter("txtmessage"));
	            //body = body.replaceAll("\\s"," ");
	            if (subject!=null){
	            subject = subject.replaceAll("\'","%27");
	            subject = subject.replaceAll("\"","&quote;");
	            }
	           // if (body!=null){
	            //body=body.replaceAll("\r\n","%0D%0A");
	            //body = body.replaceAll("\'","%27");
	           // }  
	           if (body!=null){
	            body=body.replaceAll("\r\n","%0D%0A");
	            body = body.replaceAll("\'","%27");
	            body = body.replaceAll("\"","&quote;");
	            }     
	                
	                //emailTemplates[i].setEmailBody(body1);      
	            // System.out.println("email body " +body);
	            emailTemplate.setEmailSubject(subject);
	            emailTemplate.setEmailBody(body);
	            HashMap evaluationTemplate=null;
	            evaluationTemplate =sceManager.getAllEvaluationTemplatesMap();
	            session.setAttribute("evaluationTemplate", evaluationTemplate);
	            message = sceManager.saveEmailTemplate(emailTemplate);
	            EmailTemplate[] emailTemplates = sceManager.searchEmailTemplates(evaluationTemplateId,scoringOption);
	 
	            req.setAttribute("evaluationTemplateId",evaluationTemplateId);
	            req.setAttribute("defaultScoringOption",scoringOption);       
	            req.setAttribute("emailtemplates",emailTemplates);
	            req.setAttribute("evaluationTemplateName",evaluationTemplateName);
	        
	       /* catch (Exception e)
	        {
	            message = "Template could not be modified";
	            return new Forward("failure");
	        }*/
	        req.setAttribute("message",message);
	        }catch(SCEException scee){
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return "failure";
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	            //sceLogger.error(LoggerHelper.getStackTrace(e));
	            return "failure";
	        }
	        return "success";
	        }
	 
	 public String deleteEmailTemplate() 
     {
		 HttpServletRequest req = getServletRequest();
       Integer emailTemplateId = null;
       Integer evaluationTemplateId = null;
       String scoringOption="";
       try{
       if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("selEmailTemplateId"))) {
       emailTemplateId = new Integer(req.getParameter("selEmailTemplateId"));}
       if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("selEvaluationTemplateId"))) {
       evaluationTemplateId = new Integer(req.getParameter("selEvaluationTemplateId"));}
       if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("selScoringSystemIdentifier"))) {
       scoringOption = (String)(req.getParameter("selScoringSystemIdentifier"));}
       // System.out.println("Inside delete method");        
       
           sceManager.deleteEmailTemplate(emailTemplateId);
           EmailTemplate[] emailTemplates = sceManager.searchEmailTemplates(evaluationTemplateId,scoringOption);
           req.setAttribute("emailtemplates",emailTemplates);
           req.setAttribute("evaluationTemplateId",evaluationTemplateId);
           req.setAttribute("defaultScoringOption",scoringOption);
        
       
      /* catch (Exception e) {
           req.setAttribute("message","Error: Template could not be deleted");
           return new Forward("success");
           } */
            req.setAttribute("message","Template has been deleted successfully");
          
       }catch(SCEException scee){
           req.setAttribute("errorMsg",scee.getErrorCode());
           return "failure";
       }catch(Exception e){
           req.setAttribute("errorMsg","error.sce.unknown");
           //sceLogger.error(LoggerHelper.getStackTrace(e));
           return "failure";
       }   
            return "success";
     }
	 
	 
	private String checkLegalConsent(HttpServletRequest req, HttpSession session) {

		// System.out.println("Entry in to Helper.checkLegalConsent method...");

		String ntid = "";
		try {
			User user = (User) session.getAttribute("user");
			// System.out.println(" User Object:" + user);
			if (user == null) {
				ntid = req.getHeader("IAMPFIZERUSERPFIZERNTLOGONNAME");
				// System.out.println("Getting ntid from IAM Header ntid:" + ntid);
				String emplid = req.getHeader("IAMPFIZERUSERWORKFORCEID");
				// System.out.println("Getting emplid from IAM Header emplid:"+ emplid);
				String domain = req
						.getHeader("IAMPFIZERUSERPFIZERNTDOMAINNAME");
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
	
	public static void main(String[] args) {

	      // create a new ResourceBundle with default locale
	      ResourceBundle bundle =
	              ResourceBundle.getBundle("MessageResources");

	      // print the text assigned to key "hello"
	      System.out.println("" + bundle.getString("error.sce.database.communication"));

	   }

}
