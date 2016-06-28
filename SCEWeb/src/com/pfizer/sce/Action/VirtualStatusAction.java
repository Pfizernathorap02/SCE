package com.pfizer.sce.Action;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.sce.ActionForm.UserDataForm;
import com.pfizer.sce.beans.Learner;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.EvaluationControllerHelper;
import com.pfizer.sce.helper.LegalConsentHelper;
import com.pfizer.sce.utils.LoggerHelper;
import com.pfizer.sce.utils.SCEUtils;


public class VirtualStatusAction extends ActionSupport implements
ServletRequestAware, ModelDriven {

private static SCEManagerImpl sceManager = new SCEManagerImpl();
private UserDataForm userDataForm = new UserDataForm();
private static LegalConsentHelper legalConsentHelp = new LegalConsentHelper();
//private transient NonCatalogLogger sceLogger = new NonCatalogLogger(this.getClass().getName());
private HashMap userGroups = SCEUtils.getUserGroupsMap();
private HashMap statuses = SCEUtils.getStatuses();
private LinkedHashMap filterStatuses = SCEUtils.getFilterStatuses();

// Mapping of hidden parameter is done
private String selUserId;
private String selUserStatus;


public String getSelUserId() {
return selUserId;
}

public void setSelUserId(String selUserId) {
this.selUserId = selUserId;
}

public String getSelUserStatus() {
return selUserStatus;
}

public void setSelUserStatus(String selUserStatus) {
this.selUserStatus = selUserStatus;
}
public HashMap getUserGroups() {
return userGroups;
}

public void setUserGroups(HashMap userGroups) {
this.userGroups = userGroups;
}

public HashMap getStatuses() {
return statuses;
}

public void setStatuses(HashMap statuses) {
this.statuses = statuses;
}

public LinkedHashMap getFilterStatuses() {
return filterStatuses;
}

public void setFilterStatuses(LinkedHashMap filterStatuses) {
this.filterStatuses = filterStatuses;
}

// static Logger log =
// Logger.getLogger(SearchAttendeeAction.class.getName());

HttpServletRequest request;

public HttpServletRequest getRequest() {
return request;
}

public void setRequest(HttpServletRequest request) {
this.request = request;
}

public UserDataForm getUserDataForm() {
return userDataForm;
}

public void setUserDataForm(UserDataForm userDataForm) {
this.userDataForm = userDataForm;
}

@Override
public Object getModel() {
// TODO Auto-generated method stub
return userDataForm;
}

@Override
public void setServletRequest(HttpServletRequest request) {
this.request = request;}

public String gotovirtualtrainingstatus()
{
	HttpServletRequest req = getRequest();
	String[] events=null;
	events = sceManager.getEventName();	
	req.setAttribute("events", events);
    return ("success");
    }

public String gotoViewLearner(){
	HttpServletRequest req = getRequest();
    HttpSession session=req.getSession();
    String[] events =   (String[]) session.getAttribute("events");
    String[] courses =   (String[]) session.getAttribute("courses");
    
    req.setAttribute("events", events);
    req.setAttribute("courses", courses);
      
   String course=req.getParameter("course");
   String event=req.getParameter("hdnEvent");
   String frmDate=req.getParameter("hdnDateFrm");
   String toDate=req.getParameter("hdnDateTo");
   // System.out.println(course);
   try{
  
   Learner[] learners=sceManager.getLearnersByCourseAndDate(course,frmDate,toDate);
   req.setAttribute("learners",learners);
   req.setAttribute("course",course);
   req.setAttribute("event",event);
   }
    catch(SCEException scee){
       req.setAttribute("errorMsg",scee.getErrorCode());
       return ("failure");
   }catch(Exception e){
       req.setAttribute("errorMsg","error.sce.unknown");
      // sceLogger.error(LoggerHelper.getStackTrace(e));
       return ("failure");
   }
   req.setAttribute("frmDate",frmDate);
   req.setAttribute("toDate",toDate);
   return ("success");
}



public String gotoSelectLearner()
{
	
	HttpServletRequest req = getRequest();
    HttpSession session=req.getSession();
    String[] events =   (String[]) session.getAttribute("events");
    String[] courses =   (String[]) session.getAttribute("courses");
    req.setAttribute("events", events);
    req.setAttribute("courses", courses);
      
    
try{
   
    String course=req.getParameter("course");
    String event=req.getParameter("hdnEvent1");
    String frmDate=req.getParameter("hdnDateFrm1");
    String toDate=req.getParameter("hdnDateTo1");
    String[] learnerEmail=req.getParameterValues("iselectLearnerChkBox");

    
    for(int i=0;i<learnerEmail.length;i++){
   Learner learner= sceManager.getLearnerByEmailCourse(learnerEmail[i],course);
   learner.setEventName(event);
   // System.out.println(learner.getCourse());
   sceManager.addNewLearner(learner);
    }
   Learner[] learners=sceManager.getLearnersByCourseAndDate(course,frmDate,toDate);
   req.setAttribute("learners",learners);
  
   String message="Selection saved successfully.";
   req.setAttribute("message",message);
   req.setAttribute("frmDate",frmDate);
   req.setAttribute("toDate",toDate);
   req.setAttribute("course",course);
   req.setAttribute("event",event);
   }
   catch(Exception e){
   }
   
   return ("success");
}





public String viewCourseForProduct(){
	HttpServletRequest req = getRequest();
    HttpSession session=req.getSession();
    try{
        String event=req.getParameter("event");
        req.setAttribute("event",event);
        String[] events =   (String[]) session.getAttribute("events");
        req.setAttribute("events", events);
        if(!event.equalsIgnoreCase("select")){
        String[] courses=sceManager.getCoursesForEvent(event);
        req.setAttribute("courses",courses);
     //   // System.out.println(""+courses[0]);
        }
    }
     catch(SCEException scee){
        req.setAttribute("errorMsg",scee.getErrorCode());
        return ("failure");
    }catch(Exception e){
        req.setAttribute("errorMsg","error.sce.unknown");
        //sceLogger.error(LoggerHelper.getStackTrace(e));
        return ("failure");
    }
         return ("success");   
        }
}

