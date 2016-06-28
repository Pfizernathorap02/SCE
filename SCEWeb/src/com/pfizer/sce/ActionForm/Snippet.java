package com.pfizer.sce.ActionForm;

import com.pfizer.sce.beans.Attendee;

public class Snippet {
	private Attendee[] removeFirstAttendee(Attendee[] attendees) {
	        Attendee[] attendeesNew = new Attendee[attendees.length - 1];
	        
	        for (int i=0; i<attendees.length-1; i++) {
	            attendeesNew[i] = attendees[i+1];
	        }
	        
	        return attendeesNew;
	    }
}

