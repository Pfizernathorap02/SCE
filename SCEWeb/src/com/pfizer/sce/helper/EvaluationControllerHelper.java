package com.pfizer.sce.helper;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.pfizer.sce.beans.BusinessRule;
import com.pfizer.sce.beans.Descriptor;
import com.pfizer.sce.beans.EvaluationFormScore;
import com.pfizer.sce.beans.LegalQuestion;
import com.pfizer.sce.beans.Question;
import com.pfizer.sce.beans.ScoringSystem;
import com.pfizer.sce.beans.TemplateVersion;
import com.pfizer.sce.beans.User;

/**
 * @author gadalp
 *
 */
public class EvaluationControllerHelper 
{ 
    //private static NonCatalogLogger sceLogger = new NonCatalogLogger("EvaluationControllerHelper.class");
    
     public static void setFormData(boolean IsScoringSystemChanged, HttpSession session, HttpServletRequest request) 
     {
       // sceLogger.debug("Starts setFormData method...");
        //Uncomment after integration
        User user = (User)session.getAttribute("user");
        //User user = new User();//(User)session.getAttribute("user");
        //user.setNtdomain("AMER"); //Need to remove after integration
        //user.setNtid("velays");//Need to remove after integration
        //user.setId(new Integer(1000));//Need to remove after integration
        // System.out.println("******************************"+user.getNtid());
        String pageName = request.getParameter("pageName");
        String formTitle = request.getParameter("form_title").trim();
        String numOfQuestions = request.getParameter("questionsCount").trim();
        String numOfBusinessRules = request.getParameter("b_value").trim();//businessRulesCount
        String numOfLegalQuestions = request.getParameter("l_value").trim();
        String numOfCriticalQuestions = request.getParameter("numOfCriticalQuestions");
        String scoringSystemIdentifier = request.getParameter("selectedScoringSystem");
        String autoCreditValue = request.getParameter("autoCredit");
        List scoringSystemValues = null;
        int scoringSystemsCount = -1;
        int questionsCount = Integer.parseInt(request.getParameter("questionsCount"));
        int businessRulesCount = Integer.parseInt(request.getParameter("b_value"));
        int legalQuestionsCount = Integer.parseInt(request.getParameter("l_value"));
        Integer templateVersionId = new Integer(0);
        TemplateVersion templateVersion = new TemplateVersion();
        List questions = new ArrayList();
        List businessRules = new ArrayList();
        List legalQuestions = new ArrayList();
        List evaluationFormScores = new ArrayList();
        String publishFlag = request.getParameter("publishFlag");
        
        
        if(request.getParameter("templateVersionId") != null && !request.getParameter("templateVersionId").equals(""))
        {
            templateVersionId = new Integer(request.getParameter("templateVersionId"));
        }
        
        int numOfQuestionsRemoved = Integer.parseInt(request.getParameter("numOfQuestionsRemoved"));
        
        int totalQuestionsCount = questionsCount+numOfQuestionsRemoved;
        
        for(int questionNum = 0;questionNum<totalQuestionsCount;questionNum++)
        {
            String displayOrder = request.getParameter("displayOrder_"+questionNum);
            if(displayOrder != null)
            {
                Question question = new Question();
                question.setTemplateVersionId(templateVersionId);
                
                try
                {
                    question.setDisplayOrder(new Integer(displayOrder));
                } 
                catch(NumberFormatException nfe) 
                {
                    question.setDisplayOrder(null);
                }
                
                question.setTitle(request.getParameter("title_"+questionNum));
                
                if(request.getParameter("critical_"+questionNum) != null)
                {
                    question.setCritical(request.getParameter("critical_"+questionNum));
                }
                else
                {
                    question.setCritical("N");
                }
                
                if(request.getParameter("description_"+questionNum) != null)
                {
                    question.setDescription(request.getParameter("description_"+questionNum));
                }
                else
                {
                    question.setDescription("");
                }
                int numOfDescriptors = Integer.parseInt(request.getParameter("d_value_"+questionNum));
                List descriptors = new ArrayList();
                for(int descriptorNum = 0;descriptorNum <= numOfDescriptors;descriptorNum++){
                 String description = request.getParameter("descriptor_"+questionNum+"_"+descriptorNum);
                 if((description != null && !description.trim().equals("")) || IsScoringSystemChanged == true){
                     Descriptor descriptor = new Descriptor();   
                     descriptor.setDescription(description);  
                     descriptors.add(descriptor);
                 }
                }
                question.setDescriptorList(descriptors);
                questions.add(question);
            
            }
        } 
          int removedBusinessRules = Integer.parseInt(request.getParameter("numOfBusinessRulesRemoved"));
          int totalBusinessRulesCount = businessRulesCount+removedBusinessRules;
          int actualCount = 0;
          for(int businessRuleNum = 0;businessRuleNum <= totalBusinessRulesCount;businessRuleNum++)
          {
            String businessRuleDisplayOrder = request.getParameter("businessRule"+businessRuleNum+"_displayOrder");
            if(businessRuleDisplayOrder != null){
                actualCount++;
                BusinessRule businessRule = new BusinessRule();
                businessRule.setTemplateVersionId(templateVersionId);
                String businessRuleNumOfCriticalQuestions = request.getParameter("businessRule"+businessRuleNum+"_numOfCriticalQuestions");
                String businessRuleScore = request.getParameter("businessRule"+businessRuleNum+"_score");
                String businessRuleOverallScore = request.getParameter("businessRule"+businessRuleNum+"_overallScore");
                businessRule.setDisplayOrder(new Integer(actualCount));
                if(businessRuleNumOfCriticalQuestions != null && !businessRuleNumOfCriticalQuestions.equals("")){
                    businessRule.setCriticalQuestions(new Integer(businessRuleNumOfCriticalQuestions));
                }
                if(businessRuleScore != null && !businessRuleScore.equals("")){
                    businessRule.setScore(businessRuleScore);
                }
                if(businessRuleOverallScore != null && !businessRuleOverallScore.equals("")){
                    businessRule.setOverallScore(businessRuleOverallScore);
                }
                businessRules.add(businessRule);
            }
        }    
        
        
        int numOfLegalQuestionsRemoved = Integer.parseInt(request.getParameter("numOfLegalQuestionsRemoved"));
        int totalLegalQuestionsCount = legalQuestionsCount+numOfLegalQuestionsRemoved;
        for(int legalQuestionNum = 0;legalQuestionNum <= totalLegalQuestionsCount;legalQuestionNum++){
            String legalQuestionDisplayOrder = request.getParameter("legal"+legalQuestionNum+"_displayOrder");
            if(legalQuestionDisplayOrder != null){
                LegalQuestion legalQuestion = new LegalQuestion();
                legalQuestion.setTemplateVersionId(templateVersionId);
                String legalQuestionLabel = request.getParameter("legal"+legalQuestionNum+"_question_label");
                String legalQuestionText = request.getParameter("legal"+legalQuestionNum+"_question_text");
                try{
                    legalQuestion.setDisplayOrder(new Integer(legalQuestionDisplayOrder));
                } catch(NumberFormatException nfe) {
                    legalQuestion.setDisplayOrder(null);
                }
                if(legalQuestionLabel != null && !legalQuestionLabel.equals("")){
                legalQuestion.setLegalQuestionLabel(legalQuestionLabel);
                }
                if(legalQuestionText != null && !legalQuestionText.equals("")){
                legalQuestion.setLegalQuestion(legalQuestionText);
                }
                legalQuestions.add(legalQuestion);
            }
        }
        
        if(session.getAttribute("scoringSystemValues") != null ){
            scoringSystemValues = (ArrayList) session.getAttribute("scoringSystemValues");
            scoringSystemsCount = scoringSystemValues.size();
        }
        
        for(int scoreOptionsCount = 0;scoreOptionsCount < scoringSystemsCount;scoreOptionsCount++){
        EvaluationFormScore evaluationFormScore = new EvaluationFormScore();
        evaluationFormScore.setTemplateVersionId(templateVersionId);
        evaluationFormScore.setScoringSystemIdentifier(scoringSystemIdentifier);
        evaluationFormScore.setScoreValue(request.getParameter("evaluationForm"+scoreOptionsCount+"_ScoreValue"));
        if(IsScoringSystemChanged){
            evaluationFormScore.setLmsFlag("Y");
            //Added by saikat
            evaluationFormScore.setScorecommentFlag(request.getParameter("selected_evaluationFormScore"+scoreOptionsCount+"_commentFlag"));
        }else{
            evaluationFormScore.setLmsFlag(request.getParameter("selected_evaluationFormScore"+scoreOptionsCount+"_lmsFlag"));
            //Added by saikat
            evaluationFormScore.setScorecommentFlag(request.getParameter("selected_evaluationFormScore"+scoreOptionsCount+"_commentFlag"));
           /* if(publishFlag != null && publishFlag.equals("Y")){
                evaluationFormScore.setLmsFlag("Y");
            }*/
        }
        
        if(pageName != null && pageName.equals("create")){
            evaluationFormScore.setNotificationFG("N");
        }else{
            if(request.getParameter("evaluationFormScore"+scoreOptionsCount+"_notificationFG") != null ){
                evaluationFormScore.setNotificationFG(request.getParameter("evaluationFormScore"+scoreOptionsCount+"_notificationFG"));
                if(!evaluationFormScore.getNotificationFG().equals("Y")){
                    evaluationFormScore.setNotificationFG("N");
                }
                if(IsScoringSystemChanged){
                    evaluationFormScore.setNotificationFG("N");
                }
            
            }else{
                evaluationFormScore.setNotificationFG("N");
            }
        }
        evaluationFormScores.add(evaluationFormScore);
        } 
         
        templateVersion.setId(templateVersionId); //this is templateVersionId
        templateVersion.setFormTitle(formTitle);
        templateVersion.setVersion(new Integer(request.getParameter("version")));
        if(request.getParameter("templateId") != null && !request.getParameter("templateId").equals(""))
        {
            templateVersion.setTemplateId(new Integer(request.getParameter("templateId")));
        }
        templateVersion.setTemplateName(request.getParameter("templateName"));
        templateVersion.setHlcCritical(request.getParameter("hlcCriticalFlag"));
        templateVersion.setHlcCriticalValue(request.getParameter("hlcValue"));
        templateVersion.setScoringSystemIdentifier(scoringSystemIdentifier);
        templateVersion.setAutoCreditValue(autoCreditValue);
        templateVersion.setPublishFlag(publishFlag);
        templateVersion.setLegalSectionFlag(request.getParameter("legalSectionFlag"));
        templateVersion.setConflictOverallScore(request.getParameter("conflictOverallScore"));
        if(request.getParameter("comments") != null)
        {
            templateVersion.setComments(request.getParameter("comments").trim());
        }
        else
        {
            templateVersion.setComments("");
        }
        templateVersion.setPrecallComments(request.getParameter("precall_comments"));
        templateVersion.setPostcallComments(request.getParameter("postcall_comments"));
        templateVersion.setOverallComments(request.getParameter("overall_comments"));
        templateVersion.setPreCallFlag(request.getParameter("preCallFlag"));
        templateVersion.setPostCallFlag(request.getParameter("postCallFlag"));
        
        templateVersion.setPreCallImage(request.getParameter("preCallFlag_image"));
        templateVersion.setPostCallImage(request.getParameter("postCallFlag_image"));
        
        templateVersion.setCallLabelDisplay(request.getParameter("CalllabelFlag"));
        if(templateVersion.getCallLabelDisplay().trim().equals("Y"))
        	templateVersion.setCallLabelValue(request.getParameter("callSectionLabel"));
        else
        	templateVersion.setCallLabelValue("");
        
        System.out.println("printing values:----");
        System.out.println("templateVersion.getCallLabelDisplay():-"+templateVersion.getCallLabelDisplay());
        System.out.println("templateVersion.getCallLabelValue():---"+templateVersion.getCallLabelValue());
        System.out.println("templateVersion.getPreCallImage():--"+templateVersion.getPreCallImage());
        System.out.println("templateVersion.getPostCallImage():--"+templateVersion.getPostCallImage());
        
        System.out.println("request.getParameter('callImageDisplay_hidden').toLowerCase().trim():--"+request.getParameter("callImageDisplay_hidden").toLowerCase().trim());
        
        System.out.println();
        
        
        
        if(request.getParameter("preCallFlag") != null && request.getParameter("preCallFlag").equals("Y"))
        {
            templateVersion.setPreCallLabel(request.getParameter("preCallLabel").trim());
        }
        else
        {
            templateVersion.setPreCallLabel("Set specific call objective");
        }
        if(request.getParameter("postCallFlag") != null && request.getParameter("postCallFlag").equals("Y"))
        {
            templateVersion.setPostCallLabel(request.getParameter("postCallLabel").trim());
        }
        else
        {
            templateVersion.setPostCallLabel("Analyzed call execution");
        }
        templateVersion.setCreatedBy(user.getId());
        templateVersion.setModifiedBy(user.getId());
        
        templateVersion.setQuestions(questions);
        templateVersion.setBusinessRules(businessRules);
        templateVersion.setEvaluationFormScores(evaluationFormScores);
        templateVersion.setLegalQuestions(legalQuestions);
        System.out.println("Inside Set Form data:=");
        System.out.println("Setting Template Title as:="+request.getParameter("templettitle_hidden"));
        
        /*Added by Ankit*/
        templateVersion.setTemplatetitle(request.getParameter("templettitle_hidden"));
        if(request.getParameter("callImageDisplay_hidden").toLowerCase().trim().equals("true"))
        {
        templateVersion.setCallImageDisplay(true);
        }
        else
        templateVersion.setCallImageDisplay(false);
        
        templateVersion.setOverallEvaluationLable(request.getParameter("overallEvaluationLable"));
        
        /**/
        
        request.setAttribute("templateVersion",templateVersion);
        request.setAttribute("pageName",request.getParameter("pageName"));
        request.setAttribute("numOfCriticalQuestions",numOfCriticalQuestions);
        request.setAttribute("questionsCount",numOfQuestions);
        request.setAttribute("businessRulesCount",numOfBusinessRules);
        request.setAttribute("legalQuestionsCount",numOfLegalQuestions);
        request.setAttribute("selectedScoringSystem",scoringSystemIdentifier);
        request.setAttribute("numOfBusinessRulesRemoved","0");
        request.setAttribute("numOfQuestionsRemoved","0");
        request.setAttribute("numOfLegalQuestionsRemoved","0");
        //sceLogger.debug("Ends setFormData method...");
        
        }
        
    public static Map getMappedScoringSystem(ScoringSystem[] scoringSystems, HttpServletRequest request) {
        //sceLogger.debug("");
    	// System.out.println("Inside EvaluationControllerHelper's getMappedScoringSystem method...");
        HttpSession session = request.getSession();
		Map scoringSystemMap = new HashMap();
        Map scoringSystemDetailsMap = new HashMap();
		ScoringSystem[] scoringSystemsArray1 = (ScoringSystem[]) scoringSystems;
		ScoringSystem[] scoringSystemsArray2 = (ScoringSystem[]) scoringSystems;

		for (int recordsCount = 0; recordsCount < scoringSystemsArray1.length; recordsCount++) {
			ScoringSystem scoringSystemObj1 = scoringSystemsArray1[recordsCount];
			String scoringSystemIdentifier = scoringSystemObj1
					.getScoringSystemIdentifier();
            List autoCreditValues = new ArrayList();  
            List scoringSystemDetailsObjs = new ArrayList();
            for (int recordsNum = 0; recordsNum < scoringSystemsArray2.length; recordsNum++) {
				ScoringSystem scoringSystemObj2 = scoringSystemsArray2[recordsNum];
                
				if (scoringSystemIdentifier != null
						&& scoringSystemObj2.getScoringSystemIdentifier() != null) {

					   if (scoringSystemIdentifier.trim().equals(
								scoringSystemObj2.getScoringSystemIdentifier()
										.trim())) {
                                            
							String scoreValue = scoringSystemObj2
									.getScoreValue();
                            String scoreLegend = scoringSystemObj2.getScoreLegend();        
                            if (scoreValue != null) {
								scoreValue = scoreValue.trim();
							}
                            autoCreditValues.add(scoreValue);
                            scoringSystemDetailsObjs.add(scoringSystemObj2);
                            scoringSystemMap.put(scoringSystemIdentifier.trim(), autoCreditValues);
						
                        
                        }
                }
			}
            scoringSystemDetailsMap.put(scoringSystemIdentifier.trim(), scoringSystemDetailsObjs);
							
            
		}
        session.setAttribute("scoringSystemDetailsMap",scoringSystemDetailsMap);
        return scoringSystemMap;
	}

    public static void cleanUp(HttpSession session){
        
        session.removeAttribute("scoringSystemValues");
        session.removeAttribute("scoringSystemMap");
        session.removeAttribute("templateVersion");//for FR 179
        session.removeAttribute("scoringSystemDetailsMap");
        session.removeAttribute("editTimeLoadedSSI");
        
    }
    
   public static  boolean isModified(TemplateVersion existingTemplateVersion,TemplateVersion newTemplateVersion) 
   {

		boolean isModified = false;
		
			if (existingTemplateVersion.getAutoCreditValue().trim().equals(newTemplateVersion.getAutoCreditValue().trim())) 
			{
				if (existingTemplateVersion.getComments().equals(newTemplateVersion.getComments())) 
				{
					if (existingTemplateVersion.getLegalSectionFlag().equals(newTemplateVersion.getLegalSectionFlag())) 
					{
						if (existingTemplateVersion.getLegalSectionFlag().equals("Y")) 
						{
							if (existingTemplateVersion.getHlcCritical().equals(newTemplateVersion.getHlcCritical())) 
							{
								if (existingTemplateVersion.getHlcCriticalValue().equals(newTemplateVersion.getHlcCriticalValue())) 
								{
								
								} 
								else 
								{
									isModified = true;
									return isModified;
								}
	
							} 
							else 
							{
								isModified = true;
								return isModified;
							}
						}
						if ((existingTemplateVersion.getConflictOverallScore() == null && newTemplateVersion.getConflictOverallScore() != null)|| (existingTemplateVersion.getConflictOverallScore() != null && newTemplateVersion.getConflictOverallScore() == null)) 
						{
							isModified = true;
							return isModified;
	
						}
						if ((existingTemplateVersion.getConflictOverallScore() != null && newTemplateVersion.getConflictOverallScore() != null)) 
						{
	
							if (!existingTemplateVersion.getConflictOverallScore().equals(newTemplateVersion.getConflictOverallScore())) 
							{
	
								isModified = true;
								return isModified;
	
							}
						}
						if (existingTemplateVersion.getTemplatetitle() != null && newTemplateVersion.getTemplatetitle() != null) 
						{
	
							if (!existingTemplateVersion.getTemplatetitle().trim().equals(newTemplateVersion.getTemplatetitle().trim())) 
							{
	
								isModified = true;
								return isModified;
	
							}
						}
						else
						{
							isModified = true;
							return isModified;
						}
						if(existingTemplateVersion.isCallImageDisplay() != newTemplateVersion.isCallImageDisplay())
						{

							isModified = true;
							return isModified;
						}
						
						if (existingTemplateVersion.getOverallEvaluationLable() != null && newTemplateVersion.getOverallEvaluationLable() != null) 
						{
	
							if (!existingTemplateVersion.getOverallEvaluationLable().trim().equals(newTemplateVersion.getOverallEvaluationLable().trim())) 
							{
	
								isModified = true;
								return isModified;
	
							}
						}
						else
						{
							isModified = true;
							return isModified;
						}
						
						if (existingTemplateVersion.getCallLabelValue() != null && newTemplateVersion.getCallLabelValue() != null) 
						{
	
							if (!existingTemplateVersion.getCallLabelValue().trim().equals(newTemplateVersion.getCallLabelValue().trim())) 
							{
	
								isModified = true;
								return isModified;
	
							}
						}
						else
						{
							isModified = true;
							return isModified;
						}
						
						if (!existingTemplateVersion.getCallLabelDisplay().trim().equals(newTemplateVersion.getCallLabelDisplay().trim())) 
						{

							isModified = true;
							return isModified;

						}
						
						if (!existingTemplateVersion.getPreCallImage().trim().equals(newTemplateVersion.getPreCallImage().trim())) 
						{

							isModified = true;
							return isModified;

						}
						
						if (!existingTemplateVersion.getPostCallImage().trim().equals(newTemplateVersion.getPostCallImage().trim())) 
						{

							isModified = true;
							return isModified;

						}


	
						
						
						
						if (!isModified) 
						{
							isModified = isQuestionsModified(existingTemplateVersion, newTemplateVersion);
							if (!isModified) 
							{
								isModified = isBusinessRulesModified(existingTemplateVersion, newTemplateVersion);
								if (!isModified)
								{
									isModified = isLegalQuestionsModified(existingTemplateVersion,newTemplateVersion);
								}
	
							}
						} 
						else 
						{
							isModified = true;
						}
	
					} else {
						isModified = true;
					}
				} 
				else 
				{
					isModified = true;
				}
			} 
			else 
			{
				isModified = true;
			}
		
        // System.out.println("isModified before precall/postcall modification check.isModified = "+isModified);
        if(!isModified)
        {
            isModified = isPreCallSectionModified(existingTemplateVersion, newTemplateVersion);
            // System.out.println("isPreCallSectionModified.isModified = "+isModified);
        }
        if(!isModified)
        {
            isModified = isPostCallSectionModified(existingTemplateVersion, newTemplateVersion);
            // System.out.println("isPostCallSectionModified.isModified = "+isModified);
        }
        
		return isModified;

	}
    
    
    public static  boolean isPreCallSectionModified(
			TemplateVersion existingTemplateVersion,
			TemplateVersion newTemplateVersion) {
		boolean isModified = false;
        if(existingTemplateVersion.getPreCallFlag() != null && newTemplateVersion.getPreCallFlag() != null){
            
            if(existingTemplateVersion.getPreCallFlag().equals("Y") && newTemplateVersion.getPreCallFlag().equals("N")){
                isModified = true;
            }
             if(existingTemplateVersion.getPreCallFlag().equals("N") && newTemplateVersion.getPreCallFlag().equals("Y")){
                isModified = true;
            }
            
            if(existingTemplateVersion.getPreCallFlag().equals("Y") && newTemplateVersion.getPreCallFlag().equals("Y")){
                
                if(existingTemplateVersion.getPreCallLabel().trim().equals(newTemplateVersion.getPreCallLabel().trim())){
                    isModified = false;
                }else{
                    isModified = true;
                }
            }
         }
        
        return isModified;
   }
    
    public static  boolean isPostCallSectionModified(
			TemplateVersion existingTemplateVersion,
			TemplateVersion newTemplateVersion) {
		boolean isModified = false;
        if(existingTemplateVersion.getPostCallFlag() != null && newTemplateVersion.getPostCallFlag() != null){
            
            if(existingTemplateVersion.getPostCallFlag().equals("Y") && newTemplateVersion.getPostCallFlag().equals("N")){
                isModified = true;
            }
             if(existingTemplateVersion.getPostCallFlag().equals("N") && newTemplateVersion.getPostCallFlag().equals("Y")){
                isModified = true;
            }
            
            if(existingTemplateVersion.getPostCallFlag().equals("Y") && newTemplateVersion.getPostCallFlag().equals("Y")){
                
                if(existingTemplateVersion.getPostCallLabel().trim().equals(newTemplateVersion.getPostCallLabel().trim())){
                    isModified = false;
                }else{
                    isModified = true;
                }
            }
         }
        
        return isModified;
   }
    
	public static  boolean isBusinessRulesModified(
			TemplateVersion existingTemplateVersion,
			TemplateVersion newTemplateVersion) {
		boolean isModified = false;
		int numBusinessRulesExisting = existingTemplateVersion
				.getBusinessRules().size();
		int numBusinessRulesNew = newTemplateVersion.getBusinessRules().size();
		List existingBusinessRules = existingTemplateVersion.getBusinessRules();
		List newBusinessRules = newTemplateVersion.getBusinessRules();

		if (numBusinessRulesExisting == numBusinessRulesNew) {
			int count = 0;
			for (int businessRuleNumExisting = 0; businessRuleNumExisting < numBusinessRulesExisting; businessRuleNumExisting++) {
				BusinessRule existingBusinessRule = (BusinessRule) existingBusinessRules
						.get(businessRuleNumExisting);
				boolean isPresent = false;
				for (int businessRuleNumNew = 0; businessRuleNumNew < numBusinessRulesNew; businessRuleNumNew++) {
					BusinessRule newBusinessRule = (BusinessRule) newBusinessRules
							.get(businessRuleNumNew);
                    if ((existingBusinessRule.getCriticalQuestions().intValue() == newBusinessRule
							.getCriticalQuestions().intValue())

							&& (existingBusinessRule.getScore()
									.equals(newBusinessRule.getScore()))

							&& (existingBusinessRule.getOverallScore()
									.equals(newBusinessRule.getOverallScore()))) {
						if(isPresent == false){
                            count++;
                        }
						isPresent = true;
					}

				}
				if (isPresent == false) {
					isModified = true;
					break;
				}

			}
			if (count != numBusinessRulesExisting) {
				isModified = true;
			}
		} else {
			isModified = true;
		}
		return isModified;
	}

	public static  boolean isLegalQuestionsModified(
			TemplateVersion existingTemplateVersion,
			TemplateVersion newTemplateVersion) {
		boolean isModified = false;

		int numLegalQuestionsExisting = existingTemplateVersion
				.getLegalQuestions().size();
		int numLegalQuestionsNew = newTemplateVersion.getLegalQuestions()
				.size();
        if(existingTemplateVersion.getLegalSectionFlag().equals("N")){
            existingTemplateVersion.getLegalQuestions().clear();
            numLegalQuestionsExisting = 0;
        }         
        if(newTemplateVersion.getLegalSectionFlag().equals("N")){
            newTemplateVersion.getLegalQuestions().clear();
            numLegalQuestionsNew = 0;
        } 
		List existingLegalQuestions = existingTemplateVersion
				.getLegalQuestions();
		List newLegalQuestions = newTemplateVersion.getLegalQuestions();
		if (numLegalQuestionsExisting == numLegalQuestionsNew) {
			for (int legalQuestionNumExisting = 0; legalQuestionNumExisting < numLegalQuestionsExisting; legalQuestionNumExisting++) {
				LegalQuestion existingLegalQuestion = (LegalQuestion) existingLegalQuestions
						.get(legalQuestionNumExisting);
				int existingLegalQuestionDisplayOrder = existingLegalQuestion
						.getDisplayOrder().intValue();
				boolean isPresent = false;
				for (int legalQuestionNumNew = 0; legalQuestionNumNew < numLegalQuestionsNew; legalQuestionNumNew++) {
					LegalQuestion newLegalQuestion = (LegalQuestion) newLegalQuestions
							.get(legalQuestionNumNew);
					int newLegalQuestionDisplayOrder = newLegalQuestion
							.getDisplayOrder().intValue();

					if (existingLegalQuestionDisplayOrder == newLegalQuestionDisplayOrder) {
						isPresent = true;
						if (!(existingLegalQuestion.getLegalQuestionLabel()
								.equals(
										newLegalQuestion
												.getLegalQuestionLabel())

						&& existingLegalQuestion.getLegalQuestion().equals(
								newLegalQuestion.getLegalQuestion())))

						{
							isModified = true;
							break;
						}
					}
				}

				if (isPresent == false) {
					isModified = true;
					break;
				}

			}

		} else {
			isModified = true;
		}

		return isModified;
	}

	public static  boolean isQuestionsModified(
			TemplateVersion existingTemplateVersion,
			TemplateVersion newTemplateVersion) {
		boolean isModified = false;
		int numQuestionsExisting = existingTemplateVersion.getQuestions()
				.size();
		int numQuestionsNew = newTemplateVersion.getQuestions().size();
		List existingQuestions = existingTemplateVersion.getQuestions();
		List newQuestions = newTemplateVersion.getQuestions();
		if (numQuestionsExisting == numQuestionsNew) {
			for (int questionNumExisting = 0; questionNumExisting < numQuestionsExisting; questionNumExisting++) {
				Question existingQuestion = (Question) existingQuestions
						.get(questionNumExisting);
				int existingQuestionDisplayOrder = existingQuestion
						.getDisplayOrder().intValue();
				boolean isPresent = false;
				for (int questionNumNew = 0; questionNumNew < numQuestionsNew; questionNumNew++) {
					Question newQuestion = (Question) newQuestions
							.get(questionNumExisting);
					int newQuestionDisplayOrder = newQuestion
							.getDisplayOrder().intValue();

					if (existingQuestionDisplayOrder == newQuestionDisplayOrder) {
						isPresent = true;
						if (existingQuestion.getTitle().equals(
								newQuestion.getTitle())) {
							if (existingQuestion.getDescription().equals(
									newQuestion.getDescription())) {

								List existingDescriptors = existingQuestion
										.getDescriptorList();
								List newDescriptors = newQuestion
										.getDescriptorList();
								int numEexistingDescriptors = existingDescriptors
										.size();
								int numNewDescriptors = newDescriptors.size();
								if (numEexistingDescriptors == numNewDescriptors) {
									int count = 0;
									for (int descriptorNumExisting = 0; descriptorNumExisting < numEexistingDescriptors; descriptorNumExisting++) {
										Descriptor existingDescriptor = (Descriptor) existingDescriptors
												.get(descriptorNumExisting);
                                        boolean counterFlag = false;        
										for (int descriptorNumNew = 0; descriptorNumNew < numNewDescriptors; descriptorNumNew++) {
											Descriptor newDescriptor = (Descriptor) newDescriptors
													.get(descriptorNumNew);
											if (existingDescriptor
													.getDescription()
													.equals(
															newDescriptor
																	.getDescription())) {
												if(counterFlag == false){
                                                    count++;
                                                    counterFlag = true;
                                                }
											}

										}

									}
									if (count != numEexistingDescriptors) {
										isModified = true;
									}
								} else {
									isModified = true;

								}

							} else {
								isModified = true;
							}
						} else {

							isModified = true;
						}

					}

				}
				if (isPresent == false) {
					isModified = true;
					break;
				}
			}

		} else {
			isModified = true;
		}
		return isModified;
	}
    /*---MODIFIED---*/
   
  public static void setBookMarkURL(HttpSession session, HttpServletRequest request, String forwardToHomePage){
        
        try{
            String bookMarkURL = null;
            if(forwardToHomePage != null && forwardToHomePage.equals("Y")){
                URL url = new URL(request.getRequestURL().toString());
                String domain = url.getHost();
                String homePageUrl = "http://"+domain+"/SCEWeb/gotoSearchForAttendee.action";
                bookMarkURL = homePageUrl;
                // System.out.println("homePageUrl:"+bookMarkURL);
            }else{
                bookMarkURL = request.getRequestURL().toString();
            }
            if(bookMarkURL != null){
                bookMarkURL.toLowerCase();
            }
            // System.out.println("forwardToHomePage:"+forwardToHomePage);
            // System.out.println("bookMarkURL:"+bookMarkURL);
            session.setAttribute("bookMarkURL",bookMarkURL);
         }catch (Exception e){
            // System.out.println("Exception while setting BookMarkURL. "+e.getMessage());
         }
   }

    
} 
