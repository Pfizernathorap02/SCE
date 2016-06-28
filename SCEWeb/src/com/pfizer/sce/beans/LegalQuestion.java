package com.pfizer.sce.beans; 

public class LegalQuestion {
    
	private Integer id;
	private Integer templateVersionId;
	private Integer displayOrder;
	private String legalQuestionLabel;
	private String legalQuestion;
	private String hlcCriticalValue;
	private Integer hlcCritical;
    //Added by Mayank 11/11/11
    private String legalQuestionValue;
    private Integer sceId;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getLegalQuestionLabel() {
		return legalQuestionLabel;
	}

	public void setLegalQuestionLabel(String legalQuestionLabel) {
		this.legalQuestionLabel = legalQuestionLabel;
	}

	public String getLegalQuestion() {
		return legalQuestion;
	}

	public void setLegalQuestion(String legalQuestion) {
		this.legalQuestion = legalQuestion;
	}

	public String getHlcCriticalValue() {
		return hlcCriticalValue;
	}

	public void setHlcCriticalValue(String hlcCriticalValue) {
		this.hlcCriticalValue = hlcCriticalValue;
	}

	public Integer getHlcCritical() {
		return hlcCritical;
	}

	public void setHlcCritical(Integer hlcCritical) {
		this.hlcCritical = hlcCritical;
	}

    //Added by Mayank 11/11/11
    public String getLegalQuestionValue() {
		return legalQuestionValue;
	}

	public void setLegalQuestionValue(String legalQuestionValue) {
		this.legalQuestionValue = legalQuestionValue;
	}
    
    public Integer getSCEId() {
		return sceId;
	}

	public void setSCEId(Integer sceId) {
		this.sceId = sceId;
	}
}
