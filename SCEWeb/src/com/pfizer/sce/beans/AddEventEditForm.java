package com.pfizer.sce.beans;

import java.util.HashMap;

public class AddEventEditForm {

	private Integer eventId;
	private String eventName;
	private String eventDescription;
	private String eventStartDate;
	private String eventEndDate;
	private String eventStatus;
	private String product;
	private Integer evalDuration;
	private Integer numberOfEval;
	private String typeOfEval;
	private Integer numberOfLearners;
	private String businessUnit;

	private HashMap<String, String> eventstatuses;

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getEventStartDate() {
		return eventStartDate;
	}

	public void setEventStartDate(String eventStartDate) {
		this.eventStartDate = eventStartDate;
	}

	public String getEventEndDate() {
		return eventEndDate;
	}

	public void setEventEndDate(String eventEndDate) {
		this.eventEndDate = eventEndDate;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Integer getEvalDuration() {
		return evalDuration;
	}

	public void setEvalDuration(Integer evalDuration) {
		this.evalDuration = evalDuration;
	}

	public Integer getNumberOfEval() {
		return numberOfEval;
	}

	public void setNumberOfEval(Integer numberOfEval) {
		this.numberOfEval = numberOfEval;
	}

	public String getTypeOfEval() {
		return typeOfEval;
	}

	public void setTypeOfEval(String typeOfEval) {
		this.typeOfEval = typeOfEval;
	}

	public Integer getNumberOfLearners() {
		return numberOfLearners;
	}

	public void setNumberOfLearners(Integer numberOfLearners) {
		this.numberOfLearners = numberOfLearners;
	}

	public HashMap<String, String> getEventstatuses() {
		return eventstatuses;
	}

	public void setEventstatuses(HashMap<String, String> eventstatuses) {
		this.eventstatuses = eventstatuses;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

}
