package com.pfizer.sce.beans; 



public class Attendee implements Comparable
{ 
    private String firstName;    
    private String lastName;
    private String name;
    private String emplId;
    private String territoryId;
    private String roleCd;
    private String clusterCd;
    private String clusterDesc;
    private String teamCd;
    
     /* Added for RBU change */
    private String salesPositionId;
    private String bu;
    private String salesOrgDesc;
    /* End of addition */
    
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
    
    public String getClusterDesc() {
        return clusterDesc;
    }
    
    public void setClusterDesc(String clusterDesc) {
        this.clusterDesc = clusterDesc;
    }
    
    public String getTeamCd() {
        return teamCd;
    }
    
    public void setTeamCd(String teamCd) {
        this.teamCd = teamCd;
    }

    public int compareTo(Object o)
    {
        Attendee attendee = (Attendee)o;
        
        if (lastName.compareToIgnoreCase(attendee.getLastName()) > 0) {
            return 1;
        }
        else if (lastName.equalsIgnoreCase(attendee.getLastName()) && (firstName.compareToIgnoreCase(attendee.getFirstName()) >= 0)) {
            return 1;
        }
        else {
            return -1;
        }
    }
    
      /* Added for RBU */
     public String getSalesPositionId() {
        return salesPositionId;
    }
    
    public void setSalesPositionId(String salesPositionId) {
        this.salesPositionId = salesPositionId;
    }
    
     public String getBu() {
        return bu;
    }
    
    public void setBu(String bu) {
        this.bu = bu;
    }
    
     public String getSalesOrgDesc() {
        return salesOrgDesc;
    }
    
    public void setSalesOrgDesc(String salesOrgDesc) {
        this.salesOrgDesc = salesOrgDesc;
    }
} 
