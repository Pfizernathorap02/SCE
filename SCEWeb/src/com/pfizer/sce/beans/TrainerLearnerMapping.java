package com.pfizer.sce.beans;

import java.util.Date;

public class TrainerLearnerMapping {
	private String mapId;
    private String learnerName;
    private String trainerName;
    private String learnerEmail;
    private String trainerEmail;
    private String product;
    private String eventName;
    private String learnerLoc;
    private String trainerLoc;
    private Integer trainerCount;
    private Date courseStartDate;
    private String emailSent;
    private String courseStartDate1;
    private String totalHours;
    private String timeSlots;
    private String trainerFname;
    private String trainerLname;
    private String slotDate;
    private String slotTime;
    
    public String getSlotDate() {
		return slotDate;
	}

	public void setSlotDate(String slotDate) {
		this.slotDate = slotDate;
	}

	public String getSlotTime() {
		return slotTime;
	}

	public void setSlotTime(String slotTime) {
		this.slotTime = slotTime;
	}

	public String getTrainerFname() {
		return trainerFname;
	}

	public void setTrainerFname(String trainerFname) {
		this.trainerFname = trainerFname;
	}

	public String getTrainerLname() {
		return trainerLname;
	}

	public void setTrainerLname(String trainerLname) {
		this.trainerLname = trainerLname;
	}

	public String getTimeSlots() {
		return timeSlots;
	}

	public void setTimeSlots(String timeSlots) {
		this.timeSlots = timeSlots;
	}

	public String getCourseStartDate1() {
		return courseStartDate1;
	}

	public void setCourseStartDate1(String courseStartDate1) {
		this.courseStartDate1 = courseStartDate1;
	}

	public String getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(String totalHours) {
		this.totalHours = totalHours;
	}
    public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Date getCourseStartDate() {
		return courseStartDate;
	}

	public void setCourseStartDate(Date courseStartDate) {
		this.courseStartDate = courseStartDate;
	}

	public String getEmailSent(){
        return emailSent;
    }
    
    public void setEmailSent(String emailSent){
        this.emailSent=emailSent;
    }
   
   public Integer getTrainerCount(){
   return trainerCount;
   }
   
   public void setTrainerCount(Integer trainerCount){
   this.trainerCount=trainerCount;
   }
   
    public String getLearnerLoc() {
		return learnerLoc;
	}
	public void setLearnerLoc(String learnerLoc) {
		this.learnerLoc = learnerLoc;
	}
    
     public String getTrainerLoc() {
		return trainerLoc;
	}
	public void setTrainerLoc(String trainerLoc) {
		this.trainerLoc = trainerLoc;
	}
   
   
    
      public String getLearnerName() {
		return learnerName;
	}
	public void setLearnerName(String learnerName) {
		this.learnerName = learnerName;
	}
    
    
      public String getTrainerName() {
		return trainerName;
	}
	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}
    
    
      public String getLearnerEmail() {
		return learnerEmail;
	}
	public void setLearnerEmail(String learnerEmail) {
		this.learnerEmail = learnerEmail;
	}
    
      public String getTrainerEmail() {
		return trainerEmail;
	}
	public void setTrainerEmail(String trainerEmail) {
		this.trainerEmail = trainerEmail;
	}
    
    
    
}
