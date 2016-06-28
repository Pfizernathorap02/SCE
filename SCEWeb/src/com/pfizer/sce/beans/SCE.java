package com.pfizer.sce.beans; 

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class SCE 
{ 
    private Integer id;
    private Integer eventId;
    private String emplId;
    private String productCode;
    private String product;
    private String course;
    private String classroom;
    private String tableName;
    //private Date trainingDate;
    private Integer templateVersionId;
    private String precallRating;
    private String precallComments;
    private String postcallRating;
    private String postcallComments;
    private String comments;
    private String hlcCompliant;
    private String reviewed;
    private String overallRating;
    private String overallComments;
    private Date submittedDate;
    /*Added by Neha*/
    private Date uploadedDate;
    private Blob uploadedForm;
    public Blob getUploadedForm() {
		return uploadedForm;
	}

	public void setUploadedForm(Blob uploadedForm) {
		this.uploadedForm = uploadedForm;
	}

	private String submittedByEmplId;
    /*Added by Neha*/
    private String lmsFlag;
    
    /* P2L Fields */
    private String status;
    private String preparedByEmplId;
    private Double overallScore;
    /* End P2L Fields */
    
    private String formTitle;
    private String hlcCritical;
    List sceDetailList = new ArrayList();        
    
    /* Author: Mayank Date:11-Oct-2011 SCE Enhancement 2011 */
    List legalQuestionList = new ArrayList();
    List evaluationFormScore = new ArrayList();
    List emailTemplateList = new ArrayList();
    List businessRulesList = new ArrayList();
    private String legalFG ;
    private String hlcCriticalValue ;
    private String conflictOverallScore ;   
    private List legalQuestionDetailList = new ArrayList();
    private String precallNA ;
    private String postcallNA ;
    private String precallLabel;
    private String postcallLabel;
    private String precallFlag;
    private String postcallFlag;
    
    
    /*Added by Ankit 27April2016*/
    private String templatetitle;
    private boolean callImageDisplay;
    private String overallEvaluationLable;
    
    private String preCallImage;
    private String postCallImage;
    private String callLabelDisplay;
    private String callLabelValue;
    
    /*End*/
     
    /* End : SCE Enhancement 2011 */   
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getEventId() {
        return eventId;
    }
    
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
    
    public String getEmplId() {
        return emplId;
    }
    
    public void setEmplId(String emplId) {
        this.emplId = emplId;
    }
    
    public String getProduct() {
        return product;
    }
    
    public void setProduct(String product) {
        this.product = product;
    }
    
    public String getCourse() {
        return course;
    }
    
    public void setCourse(String course) {
        this.course = course;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public String getClassroom() {
        return classroom;
    }
    
    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    /*public Date getTrainingDate() {
        return trainingDate;
    }
    
    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }*/
    
    public Integer getTemplateVersionId() {
        return templateVersionId;
    }
    
    public void setTemplateVersionId(Integer templateVersionId) {
        this.templateVersionId = templateVersionId;
    }
    
    public String getPrecallRating() {
        return precallRating;
    }
    
    public void setPrecallRating(String precallRating) {
        this.precallRating = precallRating;
    }
    
    public String getPrecallComments() {
        return precallComments;
    }
    
    public void setPrecallComments(String precallComments) {
        this.precallComments = precallComments;
    }
    
    public String getPostcallRating() {
        return postcallRating;
    }
    
    public void setPostcallRating(String postcallRating) {
        this.postcallRating = postcallRating;
    }

    public String getPostcallComments() {
        return postcallComments;
    }
    
    public void setPostcallComments(String postcallComments) {
        this.postcallComments = postcallComments;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public String getHlcCompliant() {
        return hlcCompliant;
    }
    
    public void setHlcCompliant(String hlcCompliant) {
        this.hlcCompliant = hlcCompliant;
    }
    
    public boolean isHlcCompliantBy() {
        return "Y".equals(this.hlcCompliant);
    }        
    
    public String getReviewed() {
        return reviewed;
    }
    
    public void setReviewed(String reviewed) {
        this.reviewed = reviewed;
    }
    
    public boolean isReviewedBy() {
        return "Y".equals(this.reviewed);
    }        
    
    public String getOverallRating() {
        return overallRating;
    }
    
    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }
    
    public String getOverallComments() {
        return overallComments;
    }
    
    public void setOverallComments(String overallComments) {
        this.overallComments = overallComments;
    }
    
    public Date getSubmittedDate() {
        return submittedDate;
    }
    
    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }
    
    public String getSubmittedByEmplId() {
        return submittedByEmplId;
    }
    
    public void setSubmittedByEmplId(String submittedByEmplId) {
        this.submittedByEmplId = submittedByEmplId;
    }
    
    public String getFormTitle() {
        return formTitle;
    }
    
    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }
    
    public String getHlcCritical() {
        return hlcCritical;
    }
    
    public void setHlcCritical(String hlcCritical) {
        this.hlcCritical = hlcCritical;
    }    
    
    public List getSceDetailList() {
        return this.sceDetailList;
    }
    
    public void setSceDetailList(List sceDetailList) {
        this.sceDetailList = sceDetailList;
    }
    
    public void addSceDetailList(SCEDetail objSCEDetail) {
        this.sceDetailList.add(objSCEDetail);
    }
    
    /* P2L Fields */
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    } 
    
    public String getPreparedByEmplId() {
        return preparedByEmplId;        
    }
    
    public void setPreparedByEmplId(String preparedByEmplId) {
        this.preparedByEmplId = preparedByEmplId;        
    }   
    
    public Double getOverallScore() {
        return this.overallScore;
    }
    
    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }
    /* END P2L Fields */
    
    
    /* Author: Mayank Date:11-Oct-2011 SCE Enhancement 2011 */
      public List getLegalQuestionList() {
        return this.legalQuestionList;
    }
    
    public void addLegalQuestionList(LegalQuestion objLegalQuestion) {
        this.legalQuestionList.add(objLegalQuestion);
    }
    
      public List getEvaluationFormScoreList() {
        return this.evaluationFormScore;
    }
    
    public void addEvaluationFormScoreList(EvaluationFormScore objEvaluationFormScore) {
        this.evaluationFormScore.add(objEvaluationFormScore);
    }
    
      public List getEmailTemplateList() {
        return this.emailTemplateList;
    }
    
    public void addEmailTemplateList(EmailTemplate objEmailTemplate) {
        this.emailTemplateList.add(objEmailTemplate);
    }
    
      public List getBusinessRulesList() {
        return this.businessRulesList;
    }
    
    public void addBusinessRulesList(BusinessRule objBusinessRules) {
        this.businessRulesList.add(objBusinessRules);
    }
    
    public void setLegalFG( String legalFG){
        this.legalFG=legalFG;
    }
    
    public String getLegalFG(){
        return legalFG;
    }
    
     public void setHLCCriticalValue( String hlcCriticalValue){
        this.hlcCriticalValue=hlcCriticalValue;
    }
    
    public String getHLCCriticalValue(){
        return hlcCriticalValue;
    }
    
     public void setConflictOverallScore( String conflictOverallScore){
        this.conflictOverallScore=conflictOverallScore;
    }
    
    public String getConflictOverallScore(){
        return conflictOverallScore;
    }
    
    public List getLegalQuestionDetailList() {
        return this.legalQuestionDetailList;
    }
    
    public void addLegalQuestionDetailList(LegalQuestionDetail objLegalQuestionDetail) {
        this.legalQuestionDetailList.add(objLegalQuestionDetail);
    }
    
    /*Added by Neha*/
    public Date getUploadedDate() {
        return uploadedDate;
    }
    
    public void setUploadedDate(Date uploadedDate) {
        this.uploadedDate = uploadedDate;
    }
    public String getLmsFlag() {
        return lmsFlag;
    }
    
    public void setLmsFlag(String lmsFlag) {
        this.lmsFlag = lmsFlag;
    }
    
    public void setPostcallNA( String postcallNA){
        this.postcallNA=postcallNA;
    }
    
    public String getPostcallNA(){
        return postcallNA;
    }
    
    public void setPrecallNA( String precallNA){
        this.precallNA=precallNA;
    }
    
    public String getPrecallNA(){
        return precallNA;
    }
    
    public void setPrecallLabel( String precallLabel){
        this.precallLabel=precallLabel;
    }
    
    public String getPrecallLabel(){
        return precallLabel;
    }
    
    public void setPostcallLabel( String postcallLabel){
        this.postcallLabel=postcallLabel;
    }
    
    public String getPostcallLabel(){
        return postcallLabel;
    }
    
    public void setPrecallFlag( String precallFlag){
        this.precallFlag=precallFlag;
    }
    
    public String getPrecallFlag(){
        return precallFlag;
    }
    
    public void setPostcallFlag( String postcallFlag){
        this.postcallFlag=postcallFlag;
    }
    
    public String getPostcallFlag(){
        return postcallFlag;
    }
    
    /*Added by Ankit*/

	public String getTemplatetitle() {
		return templatetitle;
	}

	public void setTemplatetitle(String templatetitle) {
		this.templatetitle = templatetitle;
	}

	public boolean isCallImageDisplay() {
		return callImageDisplay;
	}

	public void setCallImageDisplay(boolean callImageDisplay) {
		this.callImageDisplay = callImageDisplay;
	}

	public String getOverallEvaluationLable() {
		return overallEvaluationLable;
	}

	public void setOverallEvaluationLable(String overallEvaluationLable) {
		this.overallEvaluationLable = overallEvaluationLable;
	}
	
	public String getPreCallImage() {
		return preCallImage;
	}

	public void setPreCallImage(String preCallImage) {
		this.preCallImage = preCallImage;
	}

	public String getPostCallImage() {
		return postCallImage;
	}

	public void setPostCallImage(String postCallImage) {
		this.postCallImage = postCallImage;
	}

	public String getCallLabelDisplay() {
		return callLabelDisplay;
	}

	public void setCallLabelDisplay(String callLabelDisplay) {
		this.callLabelDisplay = callLabelDisplay;
	}

	public String getCallLabelValue() {
		return callLabelValue;
	}

	public void setCallLabelValue(String callLabelValue) {
		this.callLabelValue = callLabelValue;
	}
	
	/*End*/

    //End Neha's code
    /* End : SCE Enhancement 2011 */
} 
