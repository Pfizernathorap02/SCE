package com.pfizer.sce.beans; 
import java.util.Date;
public class Learner 
{ 
    private String empNo;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String location;
    private String course;
    private Date rdate;
    private String isEmailSent;
    private String isCourseComplete;
    private Date courseStartDate;
    private Date courseEndDate;
    private String courseCode;
    private String eventName;
    
    public String getCourseCode(){
        return this.courseCode;
    }
    
    public void setCourseCode(String courseCode){
        this.courseCode=courseCode;
    }
    
    public Date getCourseStartDate(){
        return this.courseStartDate;
    }
    public void setCourseStartDate(Date courseStartDate){
        this.courseStartDate=courseStartDate;
    }
    
    public void setCourseEndDate(Date courseEndDate){
        this.courseEndDate=courseEndDate;
    }
    
    public Date getCourseEndDate(){
        return this.courseEndDate;
    }

    public String getIsCourseComplete() {
		return isCourseComplete;
	}
	public void setIsCourseComplete(String isCourseComplete) {
		this.isCourseComplete = isCourseComplete;
	}

    public String getIsEmailSent() {
		return isEmailSent;
	}
	public void setIsEmailSent(String isEmailSent) {
		this.isEmailSent = isEmailSent;
	}


    
      public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
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
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public Date getRdate() {
		return rdate;
	}
	public void setRdate(Date rdate) {
		this.rdate = rdate;
	}
    
    public String getEventName(){
        return eventName;
    }
    public void setEventName(String eventName){
        this.eventName=eventName;
    }
} 
