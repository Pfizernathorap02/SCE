package com.pfizer.sce.beans;

import java.math.BigDecimal;
import java.util.Date;

public class UserLegalConsent {
	private Date lcAcceptanceDate;

	private int lcId;

	private String acceptedBy;

	private Integer acceptedId;

	private String ntDomain;
	private String sceRole;

	public void setAcceptedId(Integer acceptedId) {
		this.acceptedId = acceptedId;
	}

	public Integer getAcceptedId() {
		return this.acceptedId;
	}

	public void setAcceptedBy(String acceptedBy) {
		this.acceptedBy = acceptedBy;
	}

	public String getAcceptedBy() {
		return this.acceptedBy;
	}

	public void setLcId(int lcId) {
		this.lcId = lcId;
	}

	public int getLcId() {
		return this.lcId;
	}

	public void setLcAcceptanceDate(Date lcAcceptanceDate) {
		this.lcAcceptanceDate = lcAcceptanceDate;
	}

	public Date getLcAcceptanceDate() {
		// For data binding to be able to post data back, complex types and
		// arrays must be initialized to be non-null. This type doesn't have
		// a default constructor, so Workshop cannot initialize it for you.

		// TODO: Initialize lcAcceptanceDate if it is null.
		// if(this.lcAcceptanceDate == null)
		// {
		// this.lcAcceptanceDate = new Date(?);
		// }

		return this.lcAcceptanceDate;
	}

	public void setNtDomain(String ntDomain) {

		this.ntDomain = ntDomain;

	}

	public String getNtDomain() {

		return ntDomain;
	}

	public void setSceRole(String sceRole) {

		this.sceRole = sceRole;
	}

	public String getSceRole() {

		return sceRole;
	}

}
