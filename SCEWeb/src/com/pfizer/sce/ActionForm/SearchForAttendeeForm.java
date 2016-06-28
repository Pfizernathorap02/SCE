package com.pfizer.sce.ActionForm;

/**
 * @author gadalp
 * This will contain form data for the searchForAttendee.jsp
 *
 */
public class SearchForAttendeeForm {
	private Integer eventId;
	private Integer filterEventId;
	private Integer selEventId;
	private Integer selCourseId;
	private String filterClassroom;
	private String selCourse;
	private String selTable;
	private String selClassroom;
	private String selTrainingDate;
	private String selProductCode;
	private String selProduct;
	private Integer selSceId;
	private String selEmplId;
	private String isPassport;
	// private String territoryId;
	private String salesPositionId;
	private String emplId;
	private String firstName;
	private String lastName;
	
	public SearchForAttendeeForm(){
		// System.out.println("******in SeachForAttendeeForm bean");
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public Integer getFilterEventId() {
		return filterEventId;
	}
	public void setFilterEventId(Integer filterEventId) {
		this.filterEventId = filterEventId;
	}
	public Integer getSelEventId() {
		return selEventId;
	}
	public void setSelEventId(Integer selEventId) {
		this.selEventId = selEventId;
	}
	public Integer getSelCourseId() {
		return selCourseId;
	}
	public void setSelCourseId(Integer selCourseId) {
		this.selCourseId = selCourseId;
	}
	public String getFilterClassroom() {
		return filterClassroom;
	}
	public void setFilterClassroom(String filterClassroom) {
		this.filterClassroom = filterClassroom;
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
	public String getSelTrainingDate() {
		return selTrainingDate;
	}
	public void setSelTrainingDate(String selTrainingDate) {
		this.selTrainingDate = selTrainingDate;
	}
	public String getSelProductCode() {
		return selProductCode;
	}
	public void setSelProductCode(String selProductCode) {
		this.selProductCode = selProductCode;
	}
	public String getSelProduct() {
		return selProduct;
	}
	public void setSelProduct(String selProduct) {
		this.selProduct = selProduct;
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
	public String getIsPassport() {
		return isPassport;
	}
	public void setIsPassport(String isPassport) {
		this.isPassport = isPassport;
	}
	public String getSalesPositionId() {
		return salesPositionId;
	}
	public void setSalesPositionId(String salesPositionId) {
		this.salesPositionId = salesPositionId;
	}
	public String getEmplId() {
		return emplId;
	}
	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
	

}
