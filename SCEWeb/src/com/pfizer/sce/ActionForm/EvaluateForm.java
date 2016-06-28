package com.pfizer.sce.ActionForm;

import java.io.File;

public class EvaluateForm {
	
	private Integer eventIdSrch;
	private Integer evalEventId;
	private String evalEvent;
	private String evaluatorEmplId;
	private Integer evalCourseId;
	

	private Integer selCourseId;
	private String filterClassroom;
	private String evalCourse;
	private String evalProductCode;
	private String selEmplIdSrch;
	private String isPassportSrch;
	private String territoryIdSrch;
	private String firstNameSrch;
	private String lastNameSrch;
	private String emplIdSrch;
	private String emplId;
	private String evalTable;
	private String evalClassroom;
	private String evalProduct;
	private String evalTrainingDate;

	private String evalEmplId;
	private String mode;
	private String evaluatorRole;
	
	
	/**
	 *  hidden fields
	 */
	private Integer selSceId;
	private String selEmplId;
	private Integer selEventId;
	private String selProduct;
	private String selProductCode;
	private String selCourse;
	private String selTable;	
	private String selClassroom;
	
	
	
	/*Evaluation Form Data*/
    private File contentUp;
    private String uploadBy;
    private String uploadTime;
    private byte[] content;
    private String score;
	private String evaluationTime;
    private String evaluationDate;
    private String evaluationVersion;
    private String evaluationTitle;    
    
    
    
    
    public File getContentUp() {
		return contentUp;
	}

	public void setContentUp(File contentUp) {
		this.contentUp = contentUp;
	}

	public String getUploadBy() {
		return uploadBy;
	}

	public void setUploadBy(String uploadBy) {
		this.uploadBy = uploadBy;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getEvaluationTime() {
		return evaluationTime;
	}

	public void setEvaluationTime(String evaluationTime) {
		this.evaluationTime = evaluationTime;
	}

	public String getEvaluationDate() {
		return evaluationDate;
	}

	public void setEvaluationDate(String evaluationDate) {
		this.evaluationDate = evaluationDate;
	}

	public String getEvaluationVersion() {
		return evaluationVersion;
	}

	public void setEvaluationVersion(String evaluationVersion) {
		this.evaluationVersion = evaluationVersion;
	}

	public String getEvaluationTitle() {
		return evaluationTitle;
	}

	public void setEvaluationTitle(String evaluationTitle) {
		this.evaluationTitle = evaluationTitle;
	}	
	
	public String getEvalEvent() {
		return evalEvent;
	}

	public void setEvalEvent(String evalEvent) {
		this.evalEvent = evalEvent;
	}

	public Integer getSelCourseId() {
		return selCourseId;
	}

	public void setSelCourseId(Integer selCourseId) {
		this.selCourseId = selCourseId;
	}

	public Integer getSelSceId() {
		return selSceId;
	}

	public void setSelSceId(Integer selSceId) {
		this.selSceId = selSceId;
	}

	public String getSelEmplId() {
		return selEmplId;
	}

	public void setSelEmplId(String selEmplId) {
		this.selEmplId = selEmplId;
	}

	public Integer getSelEventId() {
		return selEventId;
	}

	public void setSelEventId(Integer selEventId) {
		this.selEventId = selEventId;
	}

	public String getSelProduct() {
		return selProduct;
	}

	public void setSelProduct(String selProduct) {
		this.selProduct = selProduct;
	}

	public String getSelProductCode() {
		return selProductCode;
	}

	public void setSelProductCode(String selProductCode) {
		this.selProductCode = selProductCode;
	}

	public String getSelCourse() {
		return selCourse;
	}

	public void setSelCourse(String selCourse) {
		this.selCourse = selCourse;
	}

	public String getSelTable() {
		return selTable;
	}

	public void setSelTable(String selTable) {
		this.selTable = selTable;
	}

	public String getSelClassroom() {
		return selClassroom;
	}

	public void setSelClassroom(String selClassroom) {
		this.selClassroom = selClassroom;
	}
	
	
	

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return this.mode;
	}

	public void setEvalEmplId(String evalEmplId) {
		this.evalEmplId = evalEmplId;
	}

	public String getEvalEmplId() {
		return this.evalEmplId;
	}

	public void setEvalTrainingDate(String evalTrainingDate) {
		this.evalTrainingDate = evalTrainingDate;
	}

	public String getEvalTrainingDate() {
		return this.evalTrainingDate;
	}

	public void setEvalProduct(String evalProduct) {
		this.evalProduct = evalProduct;
	}

	public String getEvalProduct() {
		return this.evalProduct;
	}

	public void setEvalClassroom(String evalClassroom) {
		this.evalClassroom = evalClassroom;
	}

	public String getEvalClassroom() {
		return this.evalClassroom;
	}

	public void setEvalTable(String evalTable) {
		this.evalTable = evalTable;
	}

	public String getEvalTable() {
		return this.evalTable;
	}

	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}

	public String getEmplId() {
		return this.emplId;
	}

	public void setEmplIdSrch(String emplIdSrch) {
		this.emplIdSrch = emplIdSrch;
	}

	public String getEmplIdSrch() {
		return this.emplIdSrch;
	}

	public void setLastNameSrch(String lastNameSrch) {
		this.lastNameSrch = lastNameSrch;
	}

	public String getLastNameSrch() {
		return this.lastNameSrch;
	}

	public void setFirstNameSrch(String firstNameSrch) {
		this.firstNameSrch = firstNameSrch;
	}

	public String getFirstNameSrch() {
		return this.firstNameSrch;
	}

	public void setTerritoryIdSrch(String territoryIdSrch) {
		this.territoryIdSrch = territoryIdSrch;
	}

	public String getTerritoryIdSrch() {
		return this.territoryIdSrch;
	}

	public void setIsPassportSrch(String isPassportSrch) {
		this.isPassportSrch = isPassportSrch;
	}

	public String getIsPassportSrch() {
		return this.isPassportSrch;
	}

	public void setSelEmplIdSrch(String selEmplIdSrch) {
		this.selEmplIdSrch = selEmplIdSrch;
	}

	public String getSelEmplIdSrch() {
		return this.selEmplIdSrch;
	}

	public void setEvalProductCode(String evalProductCode) {
		this.evalProductCode = evalProductCode;
	}

	public String getEvalProductCode() {
		return this.evalProductCode;
	}

	public void setEvalCourse(String evalCourse) {
		this.evalCourse = evalCourse;
	}

	public String getEvalCourse() {
		return this.evalCourse;
	}

	public void setFilterClassroom(String filterClassroom) {
		this.filterClassroom = filterClassroom;
	}

	public String getFilterClassroom() {
		return this.filterClassroom;
	}

	public void setEvalCourseId(Integer evalCourseId) {
		this.evalCourseId = evalCourseId;
	}

	public Integer getEvalCourseId() {
		return this.evalCourseId;
	}

	public void setEvaluatorEmplId(String evaluatorEmplId) {
		this.evaluatorEmplId = evaluatorEmplId;
	}

	public String getEvaluatorEmplId() {
		return this.evaluatorEmplId;
	}

	public void setEvalEventId(Integer evalEventId) {
		this.evalEventId = evalEventId;
	}

	public Integer getEvalEventId() {
		return this.evalEventId;
	}

	public void setEventIdSrch(Integer eventIdSrch) {
		this.eventIdSrch = eventIdSrch;
	}

	public Integer getEventIdSrch() {
		return this.eventIdSrch;
	}

	public void setEvaluatorRole(String evaluatorRole) {
		this.evaluatorRole = evaluatorRole;
	}

	public String getEvaluatorRole() {
		return this.evaluatorRole;
	}
}
