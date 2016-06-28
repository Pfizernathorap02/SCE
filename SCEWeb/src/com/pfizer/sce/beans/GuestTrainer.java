package com.pfizer.sce.beans;

import java.util.Date;

public class GuestTrainer {
	private String repId;
	// private String repName;
	private String repEmail;
	private String repLocation;
	private String repManager;
	private String repManagerRole;
	private String repRole;
	private String eventHistory;
	private String isSelected;
	private String isAccepted;
	private String associatedProduct;

	private String fname;
	private String lname;
	private String ntid;
	private String mgrEmail;
	private Date lastEventDate;

	private String repEmailIsSent;
	private String totalHours;
	private String evalDuration;

	public String getEvalDuration() {
		return evalDuration;
	}

	public void setEvalDuration(String evalDuration) {
		this.evalDuration = evalDuration;
	}

	public String getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(String totalHours) {
		this.totalHours = totalHours;
	}

	public String getRepEmailIsSent() {
		return repEmailIsSent;
	}

	public void setRepEmailIsSent(String repEmailIsSent) {
		this.repEmailIsSent = repEmailIsSent;
	}

	public String getRepId() {
		return repId;
	}

	public void setRepId(String repId) {
		this.repId = repId;
	}

	/*
	 * public String getRepName() { return repName; } public void
	 * setRepName(String repName) { this.repName = repName; }
	 */
	public String getRepEmail() {
		return repEmail;
	}

	public void setRepEmail(String repEmail) {
		this.repEmail = repEmail;
	}

	public String getRepLocation() {
		return repLocation;
	}

	public void setRepLocation(String repLocation) {
		this.repLocation = repLocation;
	}

	public String getRepManager() {
		return repManager;
	}

	public void setRepManager(String repManager) {
		this.repManager = repManager;
	}

	public String getRepManagerRole() {
		return repManagerRole;
	}

	public void setRepManagerRole(String repManagerRole) {
		this.repManagerRole = repManagerRole;
	}

	public String getRepRole() {
		return repRole;
	}

	public void setRepRole(String repRole) {
		this.repRole = repRole;
	}

	public String getEventHistory() {
		return eventHistory;
	}

	public void setEventHistory(String eventHistory) {
		this.eventHistory = eventHistory;
	}

	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}

	public String getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(String isAccepted) {
		this.isAccepted = isAccepted;
	}

	public String getAssociatedProduct() {
		return associatedProduct;
	}

	public void setAssociatedProduct(String associatedProduct) {
		this.associatedProduct = associatedProduct;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getNtid() {
		return ntid;
	}

	public void setNtid(String ntid) {
		this.ntid = ntid;
	}

	public String getMgrEmail() {
		return mgrEmail;
	}

	public void setMgrEmail(String mgrEmail) {
		this.mgrEmail = mgrEmail;
	}

	public void setLastEventDate(Date lastEventDate) {
		this.lastEventDate = lastEventDate;
	}

	public Date getLastEventDate() {
		return lastEventDate;
	}
}
