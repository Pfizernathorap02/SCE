package com.pfizer.sce.beans; 

public class BusinessRule {
	private Integer businessRuleId;
	private Integer templateVersionId;
	private Integer displayOrder;
	private Integer criticalQuestions;
	private String score;
	private String overallScore;

	public Integer getBusinessRuleId() {
		return businessRuleId;
	}

	public void setBusinessRuleId(Integer businessRuleId) {
		this.businessRuleId = businessRuleId;
	}

	public Integer getTemplateVersionId() {
		return templateVersionId;
	}

	public void setTemplateVersionId(Integer templateVersionId) {
		this.templateVersionId = templateVersionId;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Integer getCriticalQuestions() {
		return criticalQuestions;
	}

	public void setCriticalQuestions(Integer criticalQuestions) {
		this.criticalQuestions = criticalQuestions;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getOverallScore() {
		return overallScore;
	}

	public void setOverallScore(String overallScore) {
		this.overallScore = overallScore;
	}
}
