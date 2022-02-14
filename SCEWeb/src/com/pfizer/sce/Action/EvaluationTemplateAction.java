
package com.pfizer.sce.Action;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;




import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.pfizer.sce.beans.BusinessRule;
import com.pfizer.sce.beans.Descriptor;
import com.pfizer.sce.beans.EvaluationFormScore;
import com.pfizer.sce.beans.LegalConsentTemplate;
import com.pfizer.sce.beans.LegalQuestion;
import com.pfizer.sce.beans.Question;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.ScoringSystem;
import com.pfizer.sce.beans.TemplateVersion;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.beans.UserLegalConsent;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.EvaluationControllerHelper;
import com.pfizer.sce.utils.SCEUtils;

public class EvaluationTemplateAction extends ActionSupport implements
		ServletRequestAware {

	HttpServletRequest request;

	String templateVersionId;
	String templateName;
	String pageName;
	String selectedScoringSystem;
	String selectedAutoCredit;
	String autoCredit;
	String questionsCount;
	String businessRulesCount;
	String legalQuestionsCount;
	String version;
	String templateId;
	String numOfCriticalQuestions;
	String legalSectionFlag;
	String numOfScoringOptions;
	String precall_comments;
	String postcall_comments;
	String overall_comments;
	String publishFlag;
	String numOfBusinessRulesRemoved;
	String numOfQuestionsRemoved;
	String numOfLegalQuestionsRemoved;
	String hlcCriticalFlag;
	String preCallFlag;
	String postCallFlag;
	String scorecomments;
	String scoringSystem;	
	String pageURL;
	
	public String getPageURL() {
		return pageURL;
	}

	public void setPageURL(String pageURL) {
		this.pageURL = pageURL;
	}

	// Logger to create logs
	static Logger  log = LogManager.getLogger(EvaluationTemplateAction.class);
	 
	public String getAutoCredit() {
		return autoCredit;
	}

	public void setAutoCredit(String autoCredit) {
		this.autoCredit = autoCredit;
	}

	public String getSelectedScoringSystem() {
		return selectedScoringSystem;
	}

	public void setSelectedScoringSystem(String selectedScoringSystem) {
		this.selectedScoringSystem = selectedScoringSystem;
	}

	public String getSelectedAutoCredit() {
		return selectedAutoCredit;
	}

	public void setSelectedAutoCredit(String selectedAutoCredit) {
		this.selectedAutoCredit = selectedAutoCredit;
	}

	public String getQuestionsCount() {
		return questionsCount;
	}

	public void setQuestionsCount(String questionsCount) {
		this.questionsCount = questionsCount;
	}

	public String getBusinessRulesCount() {
		return businessRulesCount;
	}

	public void setBusinessRulesCount(String businessRulesCount) {
		this.businessRulesCount = businessRulesCount;
	}

	public String getLegalQuestionsCount() {
		return legalQuestionsCount;
	}

	public void setLegalQuestionsCount(String legalQuestionsCount) {
		this.legalQuestionsCount = legalQuestionsCount;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getNumOfCriticalQuestions() {
		return numOfCriticalQuestions;
	}

	public void setNumOfCriticalQuestions(String numOfCriticalQuestions) {
		this.numOfCriticalQuestions = numOfCriticalQuestions;
	}

	public String getLegalSectionFlag() {
		return legalSectionFlag;
	}

	public void setLegalSectionFlag(String legalSectionFlag) {
		this.legalSectionFlag = legalSectionFlag;
	}

	public String getNumOfScoringOptions() {
		return numOfScoringOptions;
	}

	public void setNumOfScoringOptions(String numOfScoringOptions) {
		this.numOfScoringOptions = numOfScoringOptions;
	}

	public String getPrecall_comments() {
		return precall_comments;
	}

	public void setPrecall_comments(String precall_comments) {
		this.precall_comments = precall_comments;
	}

	public String getPostcall_comments() {
		return postcall_comments;
	}

	public void setPostcall_comments(String postcall_comments) {
		this.postcall_comments = postcall_comments;
	}

	public String getOverall_comments() {
		return overall_comments;
	}

	public void setOverall_comments(String overall_comments) {
		this.overall_comments = overall_comments;
	}

	public String getPublishFlag() {
		return publishFlag;
	}

	public void setPublishFlag(String publishFlag) {
		this.publishFlag = publishFlag;
	}

	public String getNumOfBusinessRulesRemoved() {
		return numOfBusinessRulesRemoved;
	}

	public void setNumOfBusinessRulesRemoved(String numOfBusinessRulesRemoved) {
		this.numOfBusinessRulesRemoved = numOfBusinessRulesRemoved;
	}

	public String getNumOfQuestionsRemoved() {
		return numOfQuestionsRemoved;
	}

	public void setNumOfQuestionsRemoved(String numOfQuestionsRemoved) {
		this.numOfQuestionsRemoved = numOfQuestionsRemoved;
	}

	public String getNumOfLegalQuestionsRemoved() {
		return numOfLegalQuestionsRemoved;
	}

	public void setNumOfLegalQuestionsRemoved(String numOfLegalQuestionsRemoved) {
		this.numOfLegalQuestionsRemoved = numOfLegalQuestionsRemoved;
	}

	public String getHlcCriticalFlag() {
		return hlcCriticalFlag;
	}

	public void setHlcCriticalFlag(String hlcCriticalFlag) {
		this.hlcCriticalFlag = hlcCriticalFlag;
	}

	public String getPreCallFlag() {
		return preCallFlag;
	}

	public void setPreCallFlag(String preCallFlag) {
		this.preCallFlag = preCallFlag;
	}

	public String getPostCallFlag() {
		return postCallFlag;
	}

	public void setPostCallFlag(String postCallFlag) {
		this.postCallFlag = postCallFlag;
	}

	public String getScorecomments() {
		return scorecomments;
	}

	public void setScorecomments(String scorecomments) {
		this.scorecomments = scorecomments;
	}

	public String getScoringSystem() {
		return scoringSystem;
	}

	public void setScoringSystem(String scoringSystem) {
		this.scoringSystem = scoringSystem;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getTemplateVersionId() {
		return templateVersionId;
	}

	public void setTemplateVersionId(String templateVersionId) {
		this.templateVersionId = templateVersionId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	private static SCEManagerImpl sceManager = new SCEManagerImpl();

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

	private String checkLegalConsent(HttpServletRequest req, HttpSession session) {

		// System.out.println("Inside EvaluateTemplateAction's checkLegalConsent method...");
		// sceManager = new SCEManagerImpl();

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

	public String gotoTemplateAdmin() {
		// System.out.println("Inside the EvaluationTemplateAction - TemplateAdmin method ...");
		HttpServletRequest req = getRequest();
		HttpSession session = req.getSession();
		String result = checkLegalConsent(req, session);
		
		// System.out.println("*****Result of checkLegalConsent*****: " + result);
		
		if (result != null && result.equals("success")) {
			// System.out.println("*****Forwarding to legalConsent*****");
			String forwardToHomePage = "N";
			EvaluationControllerHelper.setBookMarkURL(session, req,
					forwardToHomePage);
			return new String("legalConsent");
		} else if (result != null && result.equals("exception")) {
			// System.out.println("*****Forwarding to exception*****");
			return new String("failure");
		}
		TemplateVersion[] templateVersions = null;
		// Get All Template Versions
		try {

			templateVersions = sceManager.getAllTemplateVersions();
			System.out
					.println("Size of the template version in EvaluationTemplateAction class is: "
							+ templateVersions.length);
			req.setAttribute("templateVersions", templateVersions);
		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");

			return new String("failure");
		}
		return new String("success");
	}

	public String gotoCreateEvaluationTemplate() 
	{
		// System.out.println("inside the gotocreateevaluation template");
		// sceLogger.debug("Starts gotoCreateEvaluationTemplate method...");
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("scoringSystemValues");
		try {
			String result = checkLegalConsent(request, session);
			// System.out.println("*****result*****:" + result);
			if (result != null && result.equals("success")) {
				System.out
						.println("*************Forwarding to legalConsent from gotoCreateEvaluationTemplate");
				String forwardToHomePage = "N";
				EvaluationControllerHelper.setBookMarkURL(session, request,
						forwardToHomePage);
				return new String("legalConsent");
			} else if (result != null && result.equals("exception")) {
				System.out
						.println("**********Forwarding to exception from gotoCreateEvaluationTemplate");
				return new String("failure");
			}
			
			ScoringSystem[] scoringSystems = sceManager.getScoringSystem();
			
			Map scoringSystemMap = EvaluationControllerHelper.getMappedScoringSystem(scoringSystems, request);
			
			session.setAttribute("scoringSystemMap", scoringSystemMap);
			request.setAttribute("pageName", "create");
			TemplateVersion templateVersion = new TemplateVersion();
			
			List questions = new ArrayList();
			
			Question question = new Question();
			
			question.setDisplayOrder(new Integer(1));
			
			List descriptors = new ArrayList();
			
			Descriptor descriptor = new Descriptor();
			
			descriptors.add(descriptor);
			
			question.setDescriptorList(descriptors);
			
			questions.add(question);
			
			templateVersion.setQuestions(questions);
			
			List legalQuestions = new ArrayList();
			
			LegalQuestion legalQuestion = new LegalQuestion();
			
			legalQuestion.setDisplayOrder(new Integer(1));
			legalQuestion.setLegalQuestionLabel("Enter Label for Legal Question here");
			legalQuestion.setLegalQuestion("Enter the Legal Question here");
			legalQuestions.add(legalQuestion);
			templateVersion.setLegalQuestions(legalQuestions);
			templateVersion.setQuestions(questions);
			templateVersion.setLegalSectionFlag("N");
			templateVersion.setHlcCritical("Y");
			templateVersion.setTemplateName("P2L Sales Call");
			templateVersion.setVersion(new Integer(1));
			templateVersion.setPreCallLabel("Set specific call objective");
			templateVersion.setPostCallLabel("Analyzed call execution");
			templateVersion.setPreCallFlag("N");
			templateVersion.setPostCallFlag("N");
			request.setAttribute("templateVersion", templateVersion);
			request.setAttribute("formTitle", "");
			request.setAttribute("questionsCount", "1");
			request.setAttribute("businessRulesCount", "0");
			request.setAttribute("legalQuestionsCount", "1");
			request.setAttribute("numOfCriticalQuestions", "0");
			request.setAttribute("numOfBusinessRulesRemoved", "0");
			request.setAttribute("numOfQuestionsRemoved", "0");
			request.setAttribute("numOfLegalQuestionsRemoved", "0");
			session.removeAttribute("editTimeLoadedSSI");
		} catch (Exception e) {
			request.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		// sceLogger.debug("Ends gotoCreateEvaluationTemplate method...");
		return new String("success");
	}

	
	 
	public String gotoEditEvaluationTemplate() {
		// System.out.println("Inside the EvaluateTemplateAction's gotoEditEvaluationTemplate method ...");
		log.debug("Starts EvaluateTemplateAction's gotoEditEvaluationTemplate method ...");
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("scoringSystemValues");
		String pageName = getPageName();
		// System.out.println("Pagename is: " + pageName);
		String templateoooo = getTemplateVersionId();
		// System.out.println("Template Id is: " + getTemplateVersionId());
		String name = getTemplateName();
		// System.out.println("Template Name is: " + getTemplateName());

		try {
			String result = checkLegalConsent(request, session);
			// System.out.println("***Result of checkLegalConsent***: " + result);
			if (result != null && result.equals("success")) 
			{
				System.out.println("*****Forwarding to legalConsent from gotoEditEvaluationTemplate*****");
				String forwardToHomePage = "Y";
				EvaluationControllerHelper.setBookMarkURL(session, request,forwardToHomePage);
				return new String("legalConsent");
			} 
			else if (result != null && result.equals("exception")) 
			{
				System.out.println("*****Forwarding to exception from gotoEditEvaluationTemplate*****");
				return new String("failure");
			}
			
			// System.out.println("pageName: " + pageName);
			
			if (pageName == null || ((!pageName.equals("adminTemplate")) && (!pageName.equals("edit")) && (!pageName.equals("create")))) 
			{
				// System.out.println("This is Bookmark request");
				URL url = new URL(request.getRequestURL().toString());
				String domain = url.getHost();
				pageURL = "http://" + domain
						+ "/SCEWeb/gotoTemplateAdmin.action";
				// System.out.println("pageURL:" + pageURL);
				URL forwardURL = new URL(pageURL);
				// System.out.println("forwardURL:  " + forwardURL);
				 return new String("forwardURL");

			}
			
			ScoringSystem[] scoringSystems = sceManager.getScoringSystem();
			
			// System.out.println("ScoringSystems length: " + scoringSystems.length);
			
			Map scoringSystemMap = EvaluationControllerHelper.getMappedScoringSystem(scoringSystems, request);
			
			// System.out.println("Size of scoringSystemMap:  "+scoringSystemMap.size());
			
			session.setAttribute("scoringSystemMap", scoringSystemMap);
			
			TemplateVersion templateVersion = null;
			String selTemplateVersionId = getTemplateVersionId();
			if (request.getAttribute("selectedExistingSSI") != null&& request.getAttribute("selectedExistingSSI").toString().equals("Y")) 
			{
				selTemplateVersionId = request.getAttribute("loadedTemplateVersionId").toString();
			}
			if (SCEUtils.isFieldNotNullAndComplete(selTemplateVersionId))
			{
				Integer templateVersionId = new Integer(selTemplateVersionId);
				System.out.print("TemplateVersionId: "+ templateVersionId);
				// templateVersion =
				// sceManager.getTemplateVersionById(templateVersionId);

				templateVersion = sceManager.getTemplateVersionByIdNew(templateVersionId);
				
				System.out.print("TemplateVersion: " + templateVersion);
				
				if (templateVersion.getHlcCritical() != null) 
				{
					templateVersion.setHlcCritical(templateVersion.getHlcCritical().trim());
				}
				if (templateVersion.getComments() == null) 
				{
					templateVersion.setComments("");
				}
				String scoringSystemIdentifier = templateVersion.getScoringSystemIdentifier().trim();
				
				List scoringSystemValues = (ArrayList) scoringSystemMap.get(scoringSystemIdentifier);
				
				session.setAttribute("scoringSystemValues", scoringSystemValues);
				
				request.setAttribute("numOfScoringOptions", new Integer(scoringSystemValues.size() - 1));
				
				Question[] questions = sceManager.getQuestionsByTemplateVersionId(templateVersionId);
				
				BusinessRule[] businessRules = sceManager.getBusinessRulesByTemplateVersionId(templateVersionId);
				
				LegalQuestion[] legalQuestions = sceManager.getLegalQuestionsByTemplateVersionId(templateVersionId);
				
				if (legalQuestions != null && legalQuestions.length == 0) 
				{
					legalQuestions = new LegalQuestion[1];
					LegalQuestion legalQuestion = new LegalQuestion();
					legalQuestion.setDisplayOrder(new Integer(1));
					legalQuestion.setLegalQuestionLabel("Enter Label for Legal Question here");
					legalQuestion.setLegalQuestion("Enter the Legal Question here");
					legalQuestions[0] = legalQuestion;
				}
				
				EvaluationFormScore[] evaluationFormScores = sceManager.getEvaluationFormScoresByTemplateVersionId(templateVersionId);
				
				templateVersion.setEmailPublishFlag("N");
				
				for (int count = 0; count < evaluationFormScores.length; count++) 
				{
					EvaluationFormScore evaluationFormScore = evaluationFormScores[count];
					
					String emailPublishFlag = evaluationFormScore.getEmailPublishFlag();
					
					if (emailPublishFlag != null&& emailPublishFlag.equals("Y")) 
					{
						templateVersion.setEmailPublishFlag("Y");
					}
				}
				
				// System.out.println("templateVersion score-emailPublishFlag:"+ templateVersion.getEmailPublishFlag());

				int numOfCriticalQuestions = 0;
				for (int questionNum = 0; questionNum < questions.length; questionNum++) 
				{
					Question question = questions[questionNum];
					
					if (question.getCritical().trim().equals("Y")) 
					{
						numOfCriticalQuestions++;
					}
				}
				String publishFlag = templateVersion.getPublishFlag();
				/*
				 * if(publishFlag != null && publishFlag.equals("Y")){ for(int
				 * num = 0;num<evaluationFormScores.length;num++){
				 * EvaluationFormScore evaluationFormScore =
				 * evaluationFormScores[num];
				 * evaluationFormScore.setLmsFlag("Y"); } }
				 */
				templateVersion.setQuestions(new ArrayList(Arrays.asList(questions)));
				
				templateVersion.setBusinessRules(new ArrayList(Arrays.asList(businessRules)));
				
				templateVersion.setLegalQuestions(new ArrayList(Arrays.asList(legalQuestions)));
				
				templateVersion.setEvaluationFormScores(new ArrayList(Arrays.asList(evaluationFormScores)));
				
				request.setAttribute("templateVersion", templateVersion);
				
				request.setAttribute("pageName", "edit");
				
				request.setAttribute("questionsCount", new Integer(questions.length));
				
				request.setAttribute("numOfCriticalQuestions",String.valueOf(numOfCriticalQuestions));
				
				request.setAttribute("selectedScoringSystem",scoringSystemIdentifier);
				
				session.setAttribute("templateVersion", templateVersion);// for
																			// FR
																			// 179
				request.setAttribute("numOfBusinessRulesRemoved", "0");
				
				request.setAttribute("numOfQuestionsRemoved", "0");
				
				request.setAttribute("numOfLegalQuestionsRemoved", "0");
				
				session.setAttribute("editTimeLoadedSSI",templateVersion.getScoringSystemIdentifier());
			}
		} catch (SCEException scee) {
			request.setAttribute("errorMsg", scee.getErrorCode().toString());
			return new String("failure");
		} catch (Exception e) {
			request.setAttribute("errorMsg", "error.sce.unknown");
			log.error(e.getStackTrace());
			return new String("failure");
		}
		log.debug("Ends gotoEditEvaluationTemplate method...");
		return new String("success");

	}
	
	public String preview()
	{
		// System.out.println("ala re");
		return "success";
	}
	
	
	
    
  
   public String saveEvaluationTemplate()
   { // System.out.println("inside the save evaluation template");
       //sceLogger.debug("Starts saveEvaluationTemplate method...");
       HttpServletRequest request = getRequest();
       HttpSession session = request.getSession();
     //  // System.out.println("*****result*****:"+request.getAttribute());
       
       String pageName = request.getParameter("pageName");
       try{
           
       String result = checkLegalConsent(request,session);
       // System.out.println("*****result*****:"+result);
       if(result != null && result.equals("success")  ){
           // System.out.println("*************Forwarding to legalConsent");
           String forwardToHomePage = "Y";
           EvaluationControllerHelper.setBookMarkURL(session,request,forwardToHomePage);
           return new String("legalConsent");
       }else if(result != null && result.equals("exception")){
           System.out.println("**********Forwarding to exception");
           return new String("failure");
       }
      System.out.println("pageName:"+pageName);
       if(pageName == null || ((!pageName.equals("create")&& (!pageName.equals("edit"))))){
           // System.out.println("This is Bookmark request");   
           URL url = new URL(request.getRequestURL().toString());
           String domain = url.getHost();
           pageURL = "http://"+domain+"/SCEWeb/gotoTemplateAdmin.action";
           // System.out.println("pageURL:"+pageURL);
           URL forwardURL = new URL(pageURL);
           System.out.println("forwardURL::::::"+forwardURL);
          // return new Forward(forwardURL);
           
       }       
       
       boolean isModified = false;
       boolean isAlreadyMapped = false;
       String formTitle = request.getParameter("form_title").trim();
       
       	/*Added by Ankit om 21 April for Templet title*/
       
       String templettitle = request.getParameter("templettitle_hidden").trim();
       
       System.out.println("templettitle is :="+templettitle);
       
       
      System.out.println("call image selection:-"+request.getParameter("callImageDisplay_hidden"));
       
       
       /**/
       
       
       EvaluationControllerHelper.setFormData(false, session, request);
       
       TemplateVersion templateVersion = (TemplateVersion)request.getAttribute("templateVersion");
       
       System.out.println("Template Version is:"+templateVersion);
       
       System.out.println("Page Name is:"+pageName);
       
       if(pageName != null && pageName.equals("create"))
       {
           if(sceManager.getNumOfFormTitles(formTitle.trim().toLowerCase()).intValue() != 0 )
           {
               request.setAttribute("form_title_duplicated","Y");
               request.setAttribute("form_title",formTitle);
               return new String("createEvaluationTemplate");
           }
           
           templateVersion.setVersion(new Integer(1));
           
           templateVersion.setPublishFlag("N");
       }
       else if(pageName != null && pageName.equals("edit"))
       {
    	   System.out.println("Inside edit");
           
           Integer countOfLmsMappingIds = new Integer(0);
           
           countOfLmsMappingIds = sceManager.findLMSCourseMappingId(templateVersion.getTemplateId());
           
           System.out.println("countOfLmsMappingIds"+countOfLmsMappingIds);
           
           if(countOfLmsMappingIds.intValue() > 0)
           {
               isAlreadyMapped = true; // System.out.println("mapped");
           }   
           
           System.out.println("isAlreadyMapped:"+isAlreadyMapped);
           
           TemplateVersion existingTemplateVersion = (TemplateVersion) session.getAttribute("templateVersion");

           
           if(isAlreadyMapped == true)
           {
               if(EvaluationControllerHelper.isModified(existingTemplateVersion, templateVersion))
               {
                   isModified = true;
               }
               
           }
           else if(isAlreadyMapped == false)
           {
               if(EvaluationControllerHelper.isModified(existingTemplateVersion, templateVersion))
               {
                   isModified = true;
               }
           
            }
       }
       
       System.out.println("Is modified:-"+isModified);
       
       sceManager.saveTemplateVersionNew(templateVersion, pageName, isModified, isAlreadyMapped);
       
       
       request.setAttribute("message","Template has been saved successfully");
      
       EvaluationControllerHelper.cleanUp(session);
       
       }
       catch(SCEException scee)
       {
    	   scee.printStackTrace();
           request.setAttribute("errorMsg",scee.getErrorCode());
           return new String("failure");
       }
       catch(Exception e)
       {
    	   e.printStackTrace();
           request.setAttribute("errorMsg","error.sce.unknown");
          // sceLogger.error(LoggerHelper.getStackTrace(e));
           return new String("failure");
       }
       String stat=gotoTemplateAdmin();
       //sceLogger.debug("Ends saveEvaluationTemplate method...");
       return stat;
   }

	public String selectScoringSystem() {
		// System.out.println("inside the selectscoring system");
		// sceLogger.debug("Starts selectScoringSystem method...");
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		try {
			String result = checkLegalConsent(request, session);
			// System.out.println("*****result*****:" + result);
			if (result != null && result.equals("success")) {
				System.out
						.println("*************Forwarding to legalConsent from selectScoringSystem");
				String forwardToHomePage = "Y";
				EvaluationControllerHelper.setBookMarkURL(session, request,
						forwardToHomePage);
				return new String("legalConsent");
			} else if (result != null && result.equals("exception")) {
				System.out
						.println("**********Forwarding to exception from selectScoringSystem");
				return new String("failure");
			}
			String pageName = request.getParameter("pageName");
			// System.out.println("pageName:" + pageName);
			if (pageName == null
					|| ((!pageName.equals("create") && (!pageName
							.equals("edit"))))) {
				// System.out.println("This is Bookmark request");
				URL url = new URL(request.getRequestURL().toString());
				String domain = url.getHost();
				pageURL = "http://" + domain
						+ "/SCEWeb/gotoTemplateAdmin.action";
				// System.out.println("pageURL:" + pageURL);
				URL forwardURL = new URL(pageURL);
				// System.out.println("forwardURL::::::" + forwardURL);
				 return new String("forwardURL");

			}
			String scoringSystemIdentifier = request
					.getParameter("selectedScoringSystem");
			// // System.out.println("scoringSystemIdentifier:::"+scoringSystemIdentifier);
			if (scoringSystemIdentifier == null
					|| scoringSystemIdentifier.equals("")) {
				return new String("gotoCreateEvaluationTemplate");
			}
			// String pageName = request.getParameter("pageName");
			if (pageName != null && pageName.equals("edit")) {
				String publishFlag = request.getParameter("publishFlag");
				if (publishFlag != null && publishFlag.equals("Y")) {
					if (session.getAttribute("editTimeLoadedSSI") != null) {
						String editTimeLoadedSSI = session.getAttribute(
								"editTimeLoadedSSI").toString();
						if (editTimeLoadedSSI != null
								&& editTimeLoadedSSI
										.equals(scoringSystemIdentifier)) {
							if (request.getParameter("templateVersionId") != null
									&& !request.getParameter(
											"templateVersionId").equals("")) {
								Integer templateVersionId = new Integer(
										request.getParameter("templateVersionId"));
								request.setAttribute("selectedExistingSSI", "Y");
								request.setAttribute("loadedTemplateVersionId",
										templateVersionId);
								gotoEditEvaluationTemplate();
								return new String("success");
							}
						}
					}
				}
			}
			request.setAttribute("selectedScoringSystem",
					scoringSystemIdentifier);
			Map scoringSystemMap = (HashMap) session
					.getAttribute("scoringSystemMap");
			List scoringSystemValues = (ArrayList) scoringSystemMap
					.get(scoringSystemIdentifier);
			session.setAttribute("scoringSystemValues", scoringSystemValues);
			request.setAttribute("numOfScoringOptions", new Integer(
					scoringSystemValues.size() - 1));
			boolean IsScoringSystemChanged = true;
			EvaluationControllerHelper.setFormData(IsScoringSystemChanged,
					session, request);
		} catch (Exception e) {
			request.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		// sceLogger.debug("Ends selectScoringSystem method...");
		return new String("success");
	}

	
	  public String cancel()
	    {
	       // sceLogger.debug("Starts cancel method...");
	        HttpServletRequest request = getRequest();
	        HttpSession session = request.getSession();
	        EvaluationControllerHelper.cleanUp(session);
	      //  sceLogger.debug("Ends cancel method...");
	        String status=gotoTemplateAdmin();
	        return status;
	    }
}
