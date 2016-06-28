package com.pfizer.sce.beans; 


//import com.pfizer.sce.beans.CourseDetails;

public class CourseEvalTemplateMapping 
{ 
    private Integer mappingId;
    private String name;
    private Integer version;
    private String courseName;
    private String courseCode;
    private Integer activityPk;
    private String createdDate;
    private Integer evalTemplateId;
    
    
    
    public Integer getMappingId(){
        return mappingId;    
    }
    
    public void setMappingId(Integer mappingId){
       this.mappingId = mappingId; 
    }
    
    public String getName(){
        return name;    
    }
    
    public void setName(String name){
        this.name = name;    
    }
    
    public Integer getVersion(){
        return version;
    }
    
    public void setVersion(Integer version){
        this.version = version;    
    }
    
     
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
    
    public Integer getActivityPk(){
        return activityPk; 
    }
   
    public void setActivityPk(Integer activityPk){
        this.activityPk = activityPk;
    }
    
    public String getCreatedDate(){
        return createdDate;    
    }
    
    public void setCreatedDate(String createdDate){
        this.createdDate = createdDate;
    }
    
    public Integer getEvalTemplateId(){
        return evalTemplateId;    
    }
    
    public void setEvalTemplateId(Integer evalTemplateId){
        this.evalTemplateId = evalTemplateId;
    }
} 
