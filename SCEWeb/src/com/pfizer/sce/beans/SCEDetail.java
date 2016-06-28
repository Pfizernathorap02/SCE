package com.pfizer.sce.beans; 

import com.pfizer.sce.common.SCEConstants;
import java.util.ArrayList;
import java.util.List;

public class SCEDetail 
{ 
    private Integer id;
    private Integer sceId;
    private Integer questionId;
    private String questionRating;
    private String questionComments;
    /*P2L Fields*/
    private Double questionScore1;
    private Double questionScore2;
    private Double questionScore3;
    /*END P2L Fields*/
    
    private String title;
    private Integer displayOrder;
    private String description;
    private String critical;
    private Integer templateVersionId;
    
    private String precallComments;
    private String postcallComments;
    private String overallComments;
    
    private String hlcCritical;
    
    /*P2L Fields*/
    private Double weight;
    private String type;
    private Double averageScore;
    /*END P2L Fields*/
    
    List descriptorList = new ArrayList();    
    private String formTitle;
    
     /* Author: Mayank Date:17-Oct-2011 SCE Enhancement 2011 */
    private String legalFG;
    private String hlcCriticalValue;
    private String conflictOverallScore;
    private String questionFg;
    private String precallLabel;
    private String postcallLabel;
    private String precallFlag;
    private String postcallFlag;
    private String precallNA;
    private String postcallNA;
    
    
    /* End : SCE Enhancement 2011 */
    
    /*Added by Ankit on 27April 2016*/
    private String templatetitle;
    private boolean callImageDisplay;
    private String overallEvaluationLable;
    
    private String preCallImage;
    private String postCallImage;
    private String callLabelDisplay;
    private String callLabelValue;
    
    /*End*/
    
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getSceId() {
        return sceId;
    }
    
    public void setSceId(Integer sceId) {
        this.sceId = sceId;
    }
    
    public Integer getQuestionId() {
        return questionId;
    }
    
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
    
    public String getQuestionRating() {
        return questionRating;
    }
    
    public void setQuestionRating(String questionRating) {
        this.questionRating = questionRating;
    }
    
    public String getQuestionComments() {
        return questionComments;
    }
    
    public void setQuestionComments(String questionComments) {
        this.questionComments = questionComments;
    }
    
    /*P2L Fields*/
    public Double getQuestionScore1() {
        return questionScore1;
    }
    
    public void setQuestionScore1(Double questionScore1) {
        this.questionScore1 = questionScore1;
    }
    
    public Double getQuestionScore2() {
        return questionScore2;
    }
    
    public void setQuestionScore2(Double questionScore2) {
        this.questionScore2 = questionScore2;
    }
    
    public Double getQuestionScore3() {
        return questionScore3;
    }
    
    public void setQuestionScore3(Double questionScore3) {
        this.questionScore3 = questionScore3;
    }
    
    public Double computeAverageScore() {        
        Double totalScore = null;
        double tmpTotal = 0.0;
        if (SCEConstants.QT_SCORE.equalsIgnoreCase(type) || SCEConstants.QT_SCORE_FETCH.equalsIgnoreCase(type)) {
            if (questionScore1 == null && questionScore2 == null && questionScore3 == null) {
                totalScore = null;
            }
            else {
                int divider = 0;
                if (questionScore1 != null) {
                    tmpTotal += questionScore1.doubleValue();
                    divider++;
                }
                if (questionScore2 != null) {
                    tmpTotal += questionScore2.doubleValue();
                    divider++;
                }
                if (questionScore3 != null) {
                    tmpTotal += questionScore3.doubleValue();
                    divider++;
                }
                totalScore = new Double(tmpTotal/divider);
            } 
        }
        return totalScore;
    }
    
    public Double getAverageScore() {
        return this.averageScore;
    }
    
    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }
    
    public Double getWeightedScore() {
        Double weightedScore = null;
        double tmpWtScore = 0.0;        
        if (averageScore != null) {
            if (weight != null) {
                tmpWtScore = averageScore.doubleValue() * weight.doubleValue();
            } else {
                tmpWtScore = averageScore.doubleValue() * 1.0;
            }
            weightedScore =  new Double(tmpWtScore);            
        }
        return weightedScore;
    }
    /*END P2L Fields*/
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCritical() {
        return critical;
    }
    
    public void setCritical(String critical) {
        this.critical = critical;
    }
    
    public List getDescriptorList() {
        return this.descriptorList;
    }
    
    public void setDescriptorList(List descriptorList) {
        this.descriptorList = descriptorList;
    }
    
    public void addDescriptorList(Descriptor objDescriptor) {
        this.descriptorList.add(objDescriptor);
    }
    
    public Integer getTemplateVersionId() {
        return templateVersionId;
    }
    
    public void setTemplateVersionId(Integer templateVersionId) {
        this.templateVersionId = templateVersionId;
    }
    
    public String getFormTitle() {
        return formTitle;
    }
    
    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
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
    
    /*P2L Fields*/
    public Double getWeight() {
        return weight;
    }
    
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    /*END P2L Fields*/
    
    /* Author: Mayank Date:17-Oct-2011 SCE Enhancement 2011 */
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
    public void setQuestionFg( String questionFg){
        this.questionFg=questionFg;
    }
    
    public String getQuestionFg(){
        return questionFg;
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
    
    public void setPrecallNA( String precallNA){
        this.precallNA=precallNA;
    }
    
    public String getPrecallNA(){
        return precallNA;
    }
    
    public void setPostcallNA( String postcallNA){
        this.postcallNA=postcallNA;
    }
    
    public String getPostcallNA(){
        return postcallNA;
    }

    
    /*Added by Ankit on 27April2016*/
    
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
    /* End : SCE Enhancement 2011 */
} 
