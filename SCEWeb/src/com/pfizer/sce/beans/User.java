package com.pfizer.sce.beans;

import java.math.BigDecimal;
import java.util.Date;




public class User {

	private Integer id;

	private String firstName;

	private String lastName;
	private String name;
	private String email;
	private String ntid;
	private String emplId;
	private String ntdomain;
	private String status;
	private String userGroup;
	private Date createDate;
	private Date lastModifiedDate;
	private Date lastLogin;
	//added by muzees for PBG and UpJOHN
	private String businessUnit; 
	
	private String isAccessRequest;
	
	private String requestApprovers;
	private String requestApprover1;
	private String requestApprover2;
	private String requestApprover3;

	/* Added for CSO Enhancement */
	private String salesPositionTypeCd = "";
	private String sceVisibility = "";
	/* End of Addition */

	public static final String DEFAULT_PASSWORD = "sce";

	/* Author: Mayank Date:07-oct-2011 SCE Enhancement 2011 */
	private String expirationDate;
	
	private String atlasRoleCode;

	public String getAtlasRoleCode() {
		return atlasRoleCode;
	}

	public void setAtlasRoleCode(String atlasRoleCode) {
		this.atlasRoleCode = atlasRoleCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public void setName(String name) {
		this.name = name;
	}

	public String getNtid() {
		return ntid;
	}

	public void setNtid(String ntid) {
		this.ntid = ntid;
	}

	public String getEmplId() {
		return emplId;
	}

	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}

	public String getNtdomain() {
		return ntdomain;
	}

	public void setNtdomain(String val) {
		this.ntdomain = val;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isActive() {
		return "ACTIVE".equals(this.status);
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getSalesPositionTypeCd() {
		return salesPositionTypeCd;
	}

	public void setSalesPositionTypeCd(String salesPositionTypeCd) {
		this.salesPositionTypeCd = salesPositionTypeCd;
	}

	public String getSceVisibility() {
		return sceVisibility;
	}

	public void setSceVisibility(String sceVisibility) {
		this.sceVisibility = sceVisibility;
	}

	public int hashCode() {
		if (getId() == null)
			return 0;
		else
			return getId().hashCode();
	}

	public boolean equals(Object o) {
		if ((o == null) || !(o instanceof User)) {
			return false;
		} else {
			return getId().equals(((User) o).getId());
		}
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getIsAccessRequest() {
		return isAccessRequest;
	}

	public void setIsAccessRequest(String isAccessRequest) {
		this.isAccessRequest = isAccessRequest;
	}

	public String getRequestApprover1() {
		return requestApprover1;
	}

	public void setRequestApprover1(String requestApprover1) {
		this.requestApprover1 = requestApprover1;
	}

	public String getRequestApprover2() {
		return requestApprover2;
	}

	public void setRequestApprover2(String requestApprover2) {
		this.requestApprover2 = requestApprover2;
	}

	public String getRequestApprover3() {
		return requestApprover3;
	}

	public void setRequestApprover3(String requestApprover3) {
		this.requestApprover3 = requestApprover3;
	}

	public String getRequestApprovers() {
		return requestApprovers;
	}

	public void setRequestApprovers(String requestApprovers) {
		this.requestApprovers = requestApprovers;
	}


	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}


}
