package com.pfizer.sce.beans; 

import java.util.Date;

public class SCEReport 
{ 
    private Integer eventId;
    private Integer courseId;
    private String course;
    private String productCode;
    private String product;
    private String classroom;
    private String tableName;
    private Date startDate;
    private Date endDate;
    private String sceCompleted;
    
    private String firstName;    
    private String lastName;
    private String name;
    private String emplId;
    private String territoryId;
    private String roleCd;
    private String clusterCd;
    private String teamCd;
    
    private Integer totalAssigned;
    private Integer totalCompleted;
    
    private String overallRating;
    private Integer cnt;
    
    private	String salesPositionId;
    private String futureRole;
    private String bu;
    private String rbu;
    
    
    public Integer getEventId() {
        return eventId;
    }
    
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
    
    public Integer getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
    
    public String getProduct() {
        return product;
    }
    
    public void setProduct(String product) {
        this.product = product;
    }
    
    public String getCourse() {
        return course;
    }
    
    public void setCourse(String course) {
        this.course = course;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public String getClassroom() {
        return classroom;
    }
    
    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }    
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public String getSceCompleted() {
        return sceCompleted;
    }
    
    public void setSceCompleted(String sceCompleted) {
        this.sceCompleted = sceCompleted;
    }    
    
    public Integer getTotalAssigned() {
        return totalAssigned;
    }
    
    public void setTotalAssigned(Integer totalAssigned) {
        this.totalAssigned = totalAssigned;
    }
    
    public Integer getTotalCompleted() {
        return totalCompleted;
    }
    
    public void setTotalCompleted(Integer totalCompleted) {
        this.totalCompleted = totalCompleted;
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
    
    public String getName() {
        return firstName + " " + lastName;
    }
    
    public String getNameComma() {
        return lastName + ", " + firstName;
    }
    
    public String getEmplId() {
        return emplId;
    }
    
    public void setEmplId(String emplId) {
        this.emplId = emplId;
    }
    
    public String getTerritoryId() {
        return territoryId;
    }
    
    public void setTerritoryId(String territoryId) {
        this.territoryId = territoryId;
    }
    
    public String getRoleCd() {
        return roleCd;
    }
    
    public void setRoleCd(String roleCd) {
        this.roleCd = roleCd;
    }
    
    public String getClusterCd() {
        return clusterCd;
    }
    
    public void setClusterCd(String clusterCd) {
        this.clusterCd = clusterCd;
    }
    
    public String getTeamCd() {
        return teamCd;
    }
    
    public void setTeamCd(String teamCd) {
        this.teamCd = teamCd;
    }
    
    public String getOverallRating() {
        return overallRating;
    }
    
    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }
    
    public Integer getCnt() {
        return cnt;
    }
    
    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }
    
    public String getSalesPositionId() {
		return salesPositionId;
	}
	public void setSalesPositionId(String salesPositionId) {
		this.salesPositionId = salesPositionId;
	}
	public String getFutureRole() {
		return futureRole;
	}
	public void setFutureRole(String futureRole) {
		this.futureRole = futureRole;
	}
	public String getBu() {
		return bu;
	}
	public void setBu(String bu) {
		this.bu = bu;
	}
	public String getRbu() {
		return rbu;
	}
	public void setRbu(String rbu) {
		this.rbu = rbu;
	}
} 
