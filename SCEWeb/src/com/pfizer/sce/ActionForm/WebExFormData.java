package com.pfizer.sce.ActionForm;

import java.io.File;

public class WebExFormData {
	
	private String eventName;
    
    private String confCallNumber;
    
    private String chairPersonPasscode;
    
    private String participantPasscode;
    
    private String meetingLink;

   private File webexFile;
    
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getConfCallNumber() {
		return confCallNumber;
	}

	public void setConfCallNumber(String confCallNumber) {
		this.confCallNumber = confCallNumber;
	}

	public String getChairPersonPasscode() {
		return chairPersonPasscode;
	}

	public void setChairPersonPasscode(String chairPersonPasscode) {
		this.chairPersonPasscode = chairPersonPasscode;
	}

	public String getParticipantPasscode() {
		return participantPasscode;
	}

	public void setParticipantPasscode(String participantPasscode) {
		this.participantPasscode = participantPasscode;
	}

	public String getMeetingLink() {
		return meetingLink;
	}

	public void setMeetingLink(String meetingLink) {
		this.meetingLink = meetingLink;
	}
	
	public File getWebexFile() {
		return webexFile;
	}

	public void setWebexFile(File webexFile) {
		this.webexFile = webexFile;
	}

}
