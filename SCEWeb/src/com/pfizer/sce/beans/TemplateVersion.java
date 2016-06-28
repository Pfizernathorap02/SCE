package com.pfizer.sce.beans; 
import java.util.Date;
import java.util.List;

public class TemplateVersion {
	private Integer id;
	private Integer templateId;
	private Integer version;
	private String formTitle;
	private Date createDate;
	private Integer createdBy;
	private String templateName;
	private String createdByNtid;
	private String precallComments;
	private String postcallComments;
	private String overallComments;
	private String hlcCritical;
	private String uploadedBy;
	private Date uploadedDate;
	private Integer modifiedBy;
	private Date modifiedDate;
	private String comments;
	private String conflictOverallScore;
	private String hlcCriticalValue;
	private String legalSectionFlag;
	private String scoringSystemIdentifier;
	private String publishFlag;
	private String autoCreditValue;    
    private Integer uploadedBlankFileId;    
    private String emailPublishFlag;    
    private String preCallFlag;    
    private String postCallFlag;    
    private String preCallLabel;    
    private String postCallLabel;    
	private List questions;
	private List businessRules;
	private List legalQuestions;    
    private List evaluationFormScores;
    
    
    /*Added by ankit*/
    private boolean callImageDisplay;
    
    private String templatetitle;
    
    private String overallEvaluationLable;
    
    private String preCallImage;
    private String postCallImage;
    private String callLabelDisplay;
    private String callLabelValue;
    /*End*/
    //private String legalQuestionFlag;
    

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getFormTitle() {
		return formTitle;
	}

	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getCreatedByNtid() {
		return createdByNtid;
	}

	public void setCreatedByNtid(String createdByNtid) {
		this.createdByNtid = createdByNtid;
	}

	public String getPrecallComments() {
		return precallComments;
	}

	public void setPrecallComments(String precallComments) {
		this.precallComments = precallComments;
	}

	public String getPostcallComments() {
		return postcallComments;
	}

	public void setPostcallComments(String postcallComments) {
		this.postcallComments = postcallComments;
	}

	public String getOverallComments() {
		return overallComments;
	}

	public void setOverallComments(String overallComments) {
		this.overallComments = overallComments;
	}

	public String getHlcCritical() {
		return hlcCritical;
	}

	public void setHlcCritical(String hlcCritical) {
		this.hlcCritical = hlcCritical;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public Date getUploadedDate() {
		return uploadedDate;
	}

	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getConflictOverallScore() {
		return conflictOverallScore;
	}

	public void setConflictOverallScore(String conflictOverallScore) {
		this.conflictOverallScore = conflictOverallScore;
	}

	public String getHlcCriticalValue() {
		return hlcCriticalValue;
	}

	public void setHlcCriticalValue(String hlcCriticalValue) {
		this.hlcCriticalValue = hlcCriticalValue;
	}

	public String getLegalSectionFlag() {
		return legalSectionFlag;
	}

	public void setLegalSectionFlag(String legalSectionFlag) {
		this.legalSectionFlag = legalSectionFlag;
	}

	public String getScoringSystemIdentifier() {
		return scoringSystemIdentifier;
	}

	public void setScoringSystemIdentifier(String scoringSystemIdentifier) {
		this.scoringSystemIdentifier = scoringSystemIdentifier;
	}
	public String getPublishFlag() {
		return publishFlag;
	}

	public void setPublishFlag(String publishFlag) {
		this.publishFlag = publishFlag;
	}

	public String getAutoCreditValue() {
		return autoCreditValue;
	}

	public void setAutoCreditValue(String autoCreditValue) {
		this.autoCreditValue = autoCreditValue;
	}

	public List getQuestions() {
		return questions;
	}

	public void setQuestions(List questions) {
		this.questions = questions;
	}

	public List getBusinessRules() {
		return businessRules;
	}

	public void setBusinessRules(List businessRules) {
		this.businessRules = businessRules;
	}

	public List getLegalQuestions() {
		return legalQuestions;
	}

	public void setLegalQuestions(List legalQuestions) {
		this.legalQuestions = legalQuestions;
	}
    public List getEvaluationFormScores() {
		return evaluationFormScores;
	}

	public void setEvaluationFormScores(List evaluationFormScores) {
		this.evaluationFormScores = evaluationFormScores;
	}
    
    public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
    
    public Integer getUploadedBlankFileId() {
		return uploadedBlankFileId;
	}

	public void setUploadedBlankFileId(Integer uploadedBlankFileId) {
		this.uploadedBlankFileId = uploadedBlankFileId;
	}
    public String getEmailPublishFlag() {
		return emailPublishFlag;
	}

	public void setEmailPublishFlag(String emailPublishFlag) {
		this.emailPublishFlag = emailPublishFlag;
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

    public String getPreCallLabel() {
		return preCallLabel;
	}

	public void setPreCallLabel(String preCallLabel) {
		this.preCallLabel = preCallLabel;
	}

	public String getPostCallLabel() {
		return postCallLabel;
	}

	public void setPostCallLabel(String postCallLabel) {
		this.postCallLabel = postCallLabel;
	}

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

	public String getCallLabelValue() {
		return callLabelValue;
	}

	public void setCallLabelValue(String callLabelValue) {
		this.callLabelValue = callLabelValue;
	}

	public String getCallLabelDisplay() {
		return callLabelDisplay;
	}

	public void setCallLabelDisplay(String callLabelDisplay) {
		this.callLabelDisplay = callLabelDisplay;
	}

	public String getPostCallImage() {
		return postCallImage;
	}

	public void setPostCallImage(String postCallImage) {
		this.postCallImage = postCallImage;
	}

	public String getPreCallImage() {
		return preCallImage;
	}

	public void setPreCallImage(String preCallImage) {
		this.preCallImage = preCallImage;
	}
}
