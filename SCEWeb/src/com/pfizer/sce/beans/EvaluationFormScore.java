package com.pfizer.sce.beans; 

public class EvaluationFormScore {
	private Integer Id;
	private Integer templateVersionId;
	private String scoringSystemIdentifier;
	private String scoreValue;
	private String scoreLegend;
	private String notificationFG;
	private String lmsFlag;
    private Integer templateId;
    private String emailPublishFlag;
    
    //added by saikat
    private String scorecommentFlag;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}
    public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	public Integer getTemplateVersionId() {
		return templateVersionId;
	}

	public void setTemplateVersionId(Integer templateVersionId) {
		this.templateVersionId = templateVersionId;
	}

	public String getScoringSystemIdentifier() {
		return scoringSystemIdentifier;
	}

	public void setScoringSystemIdentifier(String scoringSystemIdentifier) {
		this.scoringSystemIdentifier = scoringSystemIdentifier;
	}

	public String getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(String scoreValue) {
		this.scoreValue = scoreValue;
	}

	public String getScoreLegend() {
		return scoreLegend;
	}

	public void setScoreLegend(String scoreLegend) {
		this.scoreLegend = scoreLegend;
	}

	public String getNotificationFG() {
		return notificationFG;
	}

	public void setNotificationFG(String notificationFG) {
		this.notificationFG = notificationFG;
	}

	public String getLmsFlag() {
		return lmsFlag;
	}

	public void setLmsFlag(String lmsFlag) {
		this.lmsFlag = lmsFlag;
	}
    public String getEmailPublishFlag() {
		return emailPublishFlag;
	}

	public void setEmailPublishFlag(String emailPublishFlag) {
		this.emailPublishFlag = emailPublishFlag;
	}
     //added by saikat
    public String getScorecommentFlag() {
		return scorecommentFlag;
	}

	public void setScorecommentFlag(String scorecommentFlag) {
		this.scorecommentFlag = scorecommentFlag;
	}


}
