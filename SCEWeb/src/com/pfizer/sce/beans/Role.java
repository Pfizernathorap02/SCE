package com.pfizer.sce.beans; 

public class Role 
{ 
    
    private String roleCd;
    private String roleDesc;
    private String isSave;
    private String isSubmit;
    private String reportType;
    private String roleId;
        
    public String getRoleCd() {
        return roleCd;
    }
    
    public void setRoleCd(String roleCd) {
        this.roleCd = roleCd;
    }
    
    public String getRoleDesc() {
        return roleDesc;
    }
    
    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }
    
      public String getIsSave() {
        return isSave;
    }
    
    public void setIsSave(String isSave) {
        this.isSave = isSave;
    }  
    
     public String getIsSubmit() {
        return isSubmit;
    }
    
    public void setIsSubmit(String isSubmit) {
        this.isSubmit = isSubmit;
    }   
    
     public String getReportType() {
        return reportType;
    }
    
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}       
} 
