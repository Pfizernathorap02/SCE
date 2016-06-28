package com.pfizer.sce.beans; 

import java.util.ArrayList;
import java.util.List;

public class Question 
{ 
    private Integer id;
    private Integer templateVersionId;
    private String title;
    private Integer displayOrder;
    private String description;
    private String critical;
    private Integer numOfDescriptors;

        
    
    /*P2L Fields*/
    private Double weight;
    private String type;
    /*END P2L Fields*/
    
    List descriptorList = new ArrayList();  
    
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public List getDescriptorList() {
        return this.descriptorList;
    }
    
    public void setDescriptorList(List descriptorList) {
        this.descriptorList = descriptorList;
    }
    
    public void addDescriptorList(Descriptor objDescriptor) {
        this.descriptorList.add(objDescriptor);
    }
    
    public String getCritical() {
        return critical;
    }
    
    public void setCritical(String critical) {
        this.critical = critical;
    }
    
    /*P2L Fields*/
    public Double getWeight() {
        return weight;
    }
    
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    /*END P2L Fields*/
    
    public void setNumOfDescriptors(Integer numOfDescriptors)
        {
            this.numOfDescriptors = numOfDescriptors;
        }

        public Integer getNumOfDescriptors()
        {
            return this.numOfDescriptors;
   }
    
} 
