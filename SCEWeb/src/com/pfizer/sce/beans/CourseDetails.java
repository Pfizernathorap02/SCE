package com.pfizer.sce.beans; 

public class CourseDetails 
{ 
    private String courseName;
    private String courseCode;
    private Long courseId;
    
    public String getCourseName(){
        return courseName;    
    }
    
    public void setCourseName(String courseName){
        this.courseName = courseName;    
    }
    
    public String getCourseCode(){
        return courseCode;    
    }
    
    public void setCourseCode(String courseCode){
        this.courseCode = courseCode;    
    }
    
    public Long getCourseId(){
        return courseId;    
    }
    
    public void setCourseId(Long courseId){
        this.courseId = courseId;    
    }
} 
