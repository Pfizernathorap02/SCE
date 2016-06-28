package com.pfizer.sce.beans; 

import java.util.Date;
public class Template 
{ 
    private Integer id;
    private String name;   
    private Date lastModifiedDate;
    private Integer currentVersion;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getName(Integer templateId){
      return name;  
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }
    
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    public Integer getCurrentVersion() {
        return currentVersion;
    }
    
    public void setCurrentVersion(Integer currentVersion) {
        this.currentVersion = currentVersion;
    }
} 
