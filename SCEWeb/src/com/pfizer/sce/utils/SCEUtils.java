package com.pfizer.sce.utils; 

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.pfizer.sce.beans.EventsCreated;

public class SCEUtils 
{ 
    public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
    public static final String DEFAULT_DATETIME_FORMAT = "MM/dd/yyyy hh:mm a";
    public static final String DEFAULT_TIME_FORMAT = "hh:mm a";
    public static String ifNull(Object o, String ifNullStr)
    {
        if (o != null)
        {
            return o.toString();
        }
    
        return ifNullStr;
    }
    
    public static String ifNull(Object o)
    {
        return ifNull(o, "");
    }
    
    public static String ifNull(Object date, DateFormat formatter, String ifNullStr)
    {
        if (date != null)
        {
            if (date instanceof Date)
            {
                return formatter.format((Date)date);
            }
            return date.toString();
        }
    
        return ifNullStr;
    }
    
    public static String ifNull(Object date, DateFormat formatter)
    {
        return ifNull(date, formatter, "");
    }
    
    public static String ifNull(Object number, NumberFormat formatter, String ifNullStr)
    {
        if (number != null)
        {
            if (number instanceof Number)
            {
                return formatter.format((Number)number);
            }
            return number.toString();
        }
        
        return ifNullStr;
    }
    
    public static String ifNull(Object number, NumberFormat formatter)
    {
        return ifNull(number, formatter, "");
    }
    
    public static boolean isFieldNotNullAndComplete(String field)
    {
        return ((field!=null && !field.trim().equals("")) ? true : false);
    }
    
    public static HashMap getUserGroupsMap()
    {
        HashMap userGroups = new HashMap();
        userGroups.put("SCE_Administrators", "Admin");
        userGroups.put("SCE_GuestTrainer_NonMGR", "Guest Trainer");
        userGroups.put("SCE_TrainingTeam", "Training Team");   
        /*userGroups.put("SCE_OpsManager", "OPS Manager");2020 Q4:commented by muzees to remove ops manager option from add user functionality*/
        return userGroups;
    }
    //Start of Muzees for PBG and Upjohn changes 2019
    public static HashMap getUserGroupsMapwithGTMGR()//used in update user page and user admin page
    {
        HashMap userGroups = new HashMap();
        userGroups.put("SCE_Administrators", "Admin");
        userGroups.put("SCE_TrainingTeam", "Training Team");
        userGroups.put("SCE_GuestTrainer_NonMGR", "Guest Trainer Non-MGR");
        userGroups.put("SCE_GuestTrainer_MGR", "Guest Trainer MGR");
        userGroups.put("SCE_OpsManager", "OPS Manager");
        return userGroups;
    }
    public static LinkedHashMap getFilterUserGroups()
    {
        LinkedHashMap filterUserGroups = new LinkedHashMap();   
        
        filterUserGroups.put("SCE_Administrators", "Admin");
        filterUserGroups.put("SCE_TrainingTeam", "Training Team");
        filterUserGroups.put("SCE_GuestTrainer_NonMGR", "Guest Trainer Non-MGR"); 
        filterUserGroups.put("SCE_GuestTrainer_MGR", "Guest Trainer MGR");
    
        return filterUserGroups;
    }
    //end of muzees
    public static HashMap getStatuses()
    {
        HashMap statuses = new HashMap();
        statuses.put("ACTIVE", "Active");
        statuses.put("INACTIVE", "Inactive");        
        return statuses;
    }
     //Mayank Singh : 14-Nov-2011 : SCE-TRT Enhancement 2011
    public static LinkedHashMap getFilterStatuses()
    {
        LinkedHashMap filterStatuses = new LinkedHashMap();   
        
        filterStatuses.put("All", "All");
        filterStatuses.put("Active", "Active");
        filterStatuses.put("Inactive", "Inactive"); 
    
        return filterStatuses;
    }
    //Adbhut
    
    //added by manish to hide form statuses
    public static LinkedHashMap getFormStatuses()
    {
    	LinkedHashMap<String, String> filterFormStatuses = new LinkedHashMap<String, String>();
    	
      filterFormStatuses.put("Visible", "Visible");
        filterFormStatuses.put("Hidden", "Hidden"); 
    
        return filterFormStatuses;
    }
    //manish
public static HashMap getEventStatuses(EventsCreated events) {
		
		HashMap eventStatuses = new HashMap();
        Date d =new Date();

        // for default status  - Start    
        
        String Status = events.getEventStatus().toUpperCase();
        eventStatuses.put(Status, events.getEventStatus());
        
        // for default status - End
        
        int index=events.getEventStartDate().indexOf("");
		int index2=events.getEventEndDate().indexOf("");
        int number;
  
        String start=events.getEventStartDate().substring(index);
        String end=events.getEventEndDate().substring(index2);
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");

        try {
			Date startd = format1.parse(start.trim());
			Date endd = format1.parse(end.trim());
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
  
        if("InProgress".equalsIgnoreCase(events.getEventStatus()))
		   {
			   eventStatuses.put("ACTIVE", "Active");
			   eventStatuses.put("COMPLETE", "Complete");   
   	   }
 		  else
 		  if("ACTIVE".equalsIgnoreCase(events.getEventStatus()))
 		  {
 			  eventStatuses.put("COMPLETE", "Complete");   
 
 		  }else
 			  if("COMPLETE".equalsIgnoreCase(events.getEventStatus()))
 			  {
 				  eventStatuses.put("COMPLETE", "Complete");   
 			  }else
 			  {eventStatuses.put("COMPLETE", "Complete"); 
 			  eventStatuses.put("ACTIVE", "Active");  
 			  eventStatuses.put("INPROGRESS","InProgress");
 			  }
   
         return eventStatuses;
           /*pageContext.setAttribute("eventStatuses", eventStatuses);   
           String status=SCEUtils.ifNull(events.getEventStatus());
           pageContext.setAttribute("status", status);*/
     
	}  
    
public static HashMap<String,String> getTypeOfEvals() {
		
		HashMap<String, String> typeOfEval =new HashMap<String,String>();
        typeOfEval.put("Virtual","Virtual");
        typeOfEval.put("Face to Face","Face to Face");
        
        return typeOfEval;

	}
    
    public static String ifDateNull(Object o, String ifNullStr) {
		if (o != null) {

			String date = (String) o;
			DateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
			Date dateParsed;

			try {

				dateParsed = (Date) parser.parse(date);
				DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				String dateFormatted = formatter.format(dateParsed);
				// System.out.println(dateFormatted);
				return dateFormatted.toString();

			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ifNullStr;
	}
    
    //end Adbhut
    public static final String replace(String srcStr, String oldString, String newString)
    {
        if (srcStr == null) {
            return null;
        }
        int i=0;
        if ( ( i=srcStr.indexOf( oldString, i ) ) >= 0 ) {
            char [] srcStr2 = srcStr.toCharArray();
            char [] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(srcStr2.length);
            buf.append(srcStr2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while( ( i=srcStr.indexOf( oldString, i ) ) > 0 ) {
                buf.append(srcStr2, j, i-j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(srcStr2, j, srcStr2.length - j);
            return buf.toString();
        }
        return srcStr;
    }
    
  
   /*Method added for RBU changes */
    public String toNotNullString(String rStr)
        {
            if(rStr==null)
            {
                return "";
            }
            return rStr;        
        }
        
    /*Added by Neha for SCE/TRT Enhancement 2011*/
   /* public static HashMap getScoreMap()
    {
        HashMap scoreMap = new HashMap();
        scoreMap.put("DC", "DC-DemonstratesCompetance");
        scoreMap.put("NI", "NI-Needs Improvement");  
        scoreMap.put("UN", "UN-Unexceptable");              
        return scoreMap;
    }*/
    
    //End Neha's Code

} 
