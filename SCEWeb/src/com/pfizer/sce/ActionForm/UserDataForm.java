package com.pfizer.sce.ActionForm;

public class UserDataForm {
	
	private String expirationDate;
	private String emplId;
	private String userGroup;
	private String message;
	private Integer id;
	private String email;
	private String status="Active";
	private String filterStatus;
	
	private String ntdomain;
	private String ntid;
	private String firstName;
	private String lastName;

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setNtid(String ntid) {
		this.ntid = ntid;
	}

	public String getNtid() {
		return this.ntid;
	}

	public void setNtdomain(String ntdomain) {
		this.ntdomain = ntdomain;
	}

	public String getNtdomain() {
		return this.ntdomain;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	public void setFilterStatus(String filterstatus) {
		this.filterStatus = filterstatus;
	}

	public String getFilterStatus() {
		return this.filterStatus;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public String getUserGroup() {
		return this.userGroup;
	}

	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}

	public String getEmplId() {
		return this.emplId;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getExpirationDate() {
		// For data binding to be able to post data back, complex types and
		// arrays must be initialized to be non-null. This type doesn't have
		// a default constructor, so Workshop cannot initialize it for you.

		// TODO: Initialize expirationDate if it is null.
		// if(this.expirationDate == null)
		// {
		// this.expirationDate = new Date(?);
		// }

		return this.expirationDate;
	}
}
