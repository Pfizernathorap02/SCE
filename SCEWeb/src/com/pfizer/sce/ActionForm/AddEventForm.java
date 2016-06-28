package com.pfizer.sce.ActionForm;

import java.util.HashMap;

public class AddEventForm {
	private Integer eventId;
	private String eventName;
	private String eventDescription;
	private String eventStartDate;
	private String eventEndDate;
	private String eventStatus;
	// private String product;
	private Integer evalDuration;
	private Integer numberOfEval;
	private String typeOfEval;
	private Integer numberOfLearners;
	private String message;

	// added for getStatuts Map
	private HashMap eventStatuses;

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

	/*
	 * public void setProduct(String product) { this.product = product;
	 * 
	 * }
	 * 
	 * public String getProduct() { return this.product; }
	 */

	public void setEventId(Integer eventId) {
		this.eventId = eventId;

	}

	public Integer getEventId() {
		return this.eventId;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;

	}

	public String getEventStatus() {
		return this.eventStatus;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;

	}

	public String getEventName() {
		return this.eventName;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;

	}

	public String getEventDescription() {
		return this.eventDescription;
	}

	public void setEventStartDate(String eventStartDate) {
		this.eventStartDate = eventStartDate;

	}

	public String getEventStartDate() {
		return this.eventStartDate;
	}

	public void setEventEndDate(String eventEndDate) {
		this.eventEndDate = eventEndDate;

	}

	public String getEventEndDate() {
		return this.eventEndDate;
	}

	public HashMap getEventStatuses() {
		return eventStatuses;
	}

	public void setEventStatuses(HashMap eventStatuses) {
		this.eventStatuses = eventStatuses;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}