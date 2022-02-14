package com.pfizer.sce.Action;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.sce.beans.CourseDetails;
import com.pfizer.sce.beans.CourseEvalTemplateMapping;
import com.pfizer.sce.beans.LegalConsentTemplate;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.Template;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.beans.UserLegalConsent;
import com.pfizer.sce.beans.Util;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.EvaluationControllerHelper;

public class CourseMappingAction extends ActionSupport implements ServletRequestAware,ModelDriven  
{
	private static SCEManagerImpl sceManager = new SCEManagerImpl();
	HttpServletRequest request;
	String pageURL; 

    
	public String getPageURL() {
		return pageURL;
	}

	public void setPageURL(String pageURL) {
		this.pageURL = pageURL;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	public HttpServletRequest getServletRequest() {
		return this.request;
	}

	public HttpSession getSession() {
		return request.getSession();
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


	 public String execute()
{
		 //System.out.println("inside the execute method of the coursemapping");
		 return "success";
}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	 public String gotoEvalTemplateMapping()
	 {
		 //System.out.println("inside the execute method");
		 HttpServletRequest req = getServletRequest();
         try{
           HttpSession session = req.getSession(false);
           //HttpSession session = req.getSession();
           String result = checkLegalConsent(req,session);
           //System.out.println("*****result*****:"+result);
           if(result != null && result.equals("success")  ){
             //System.out.println("*************Forwarding to legalConsent");
             String forwardToHomePage = "N";
             EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
             return "legalConsent";
           }else if(result != null && result.equals("exception")){
             // System.out.println("**********Forwarding to exception");
              return "failure";
           }
           Template[] evalTemplate = sceManager.getAllEvaluationTemplates();
          
           session.setAttribute("evalTemplateArr",evalTemplate);

         //System.out.println("gotoEvalTemplateMapping() method executed");
         }catch(SCEException scee){
             //req.setAttribute("errorMsg",scee.getErrorCode());
             return "failure";
         }catch(Exception e){
             req.setAttribute("errorMsg","error.sce.unknown");
             //sceLogger.error(LoggerHelper.getStackTrace(e));
             return "failure";
         }
          return "success";
	 } 
	 
	 

	 
	  public String retrieveMappingDetails(){
	        //System.out.println("Inside retrieveMappingDetails");
	        //HttpServletRequest req = ScopedServletUtils.getOuterRequest(getRequest());
	        HttpServletRequest req = getServletRequest();
	        try{
	        HttpSession session = req.getSession();
	        String result = checkLegalConsent(req,session);
	        //System.out.println("*****result*****:"+result);
	        if(result != null && result.equals("success")  ){
	            //System.out.println("*************Forwarding to legalConsent");
	            String forwardToHomePage = "Y";
	            EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
	            return "legalConsent";
	        }else if(result != null && result.equals("exception")){
	           // System.out.println("**********Forwarding to exception");
	            return "failure";
	        }
	        String pageName = req.getParameter("pageName");
	        if(pageName == null || !pageName.equals("retrieveMappingDetails")){
	            //System.out.println("This is Bookmark request");   
	            URL url = new URL(req.getRequestURL().toString());
	            String domain = url.getHost();
	            pageURL = "http://"+domain+"/SCEWeb/gotoEvalTemplateMapping.action";
	            //System.out.println("pageURL:"+pageURL);
	            URL forwardURL = new URL(pageURL);
	           // System.out.println("forwardURL::::::"+forwardURL);
	            return new String("forwardURL");
	            
	        } 
	        String strHiddenCourseNames="";
	        String strHiddenCourseCodes="";
	        String strHiddenCourseIds="";
	       
	        
	        String selEvalTemplate = req.getParameter("selEvalTemplate");
	        String selEvalTemplateName = req.getParameter("hdnSelectedTemplate");
	        
	        if(req.getParameter("hiddenCourseNames")!=null){
	           strHiddenCourseNames = req.getParameter("hiddenCourseNames"); 
	          // System.out.println("strHiddenCourseNames = "+strHiddenCourseNames);
	        }
	        if(req.getParameter("hiddenCourseCodes")!=null){
	           strHiddenCourseCodes = req.getParameter("hiddenCourseCodes");
	          // System.out.println("strHiddenCourseCodes = "+strHiddenCourseCodes);
	        }
	        if(req.getParameter("hiddenCourseIds")!=null){
	           strHiddenCourseIds = req.getParameter("hiddenCourseIds");
	           //System.out.println("strHiddenCourseIds = "+strHiddenCourseIds);
	        }
	        
	        //System.out.println("selEvalTemplate....inside retrieveMappingDetails() -->"+selEvalTemplate);
	        //System.out.println("hdnSelectedTemplate....inside retrieveMappingDetails() -->"+selEvalTemplateName);
	        
	        Integer evalTemplateId = new Integer(Integer.parseInt(selEvalTemplate));
	        CourseEvalTemplateMapping[] templateMapping = sceManager.getAllMappingsForTemplate(evalTemplateId);
	        //System.out.println("Template Size "+templateMapping.length);
	        
	       // req.setAttribute("selEvalTemplate",selEvalTemplate);   
	        req.setAttribute("templateMappingArr",templateMapping); 
	        req.setAttribute("selectedEvalTemplate", selEvalTemplate);	        
	        req.setAttribute("selectedEvalTemplateName", selEvalTemplateName);
	        req.setAttribute("retHdnCourseIds", strHiddenCourseIds);
	        req.setAttribute("hiddenCourseIds", strHiddenCourseIds);
	        req.setAttribute("retHdnCourseCodes", strHiddenCourseCodes);
	        req.setAttribute("retHdnCourseNames", strHiddenCourseNames);
	        //System.out.println("retrieveMappingDetails() method executed");
	        }catch(SCEException scee){
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return "failure";
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	            //sceLogger.error(LoggerHelper.getStackTrace(e));
	            return "failure";
	        }
	        return "success";    
	    }

	  public String searchCourseDetails()
	  {
	       
	        return "success";    
	    }
	  
	 public String getAllSearchedCourseDetails(){
	        //System.out.println("Inside searchCourseDetails()....");
	        
	        HttpServletRequest req = getServletRequest();
	        try{
	        String msg = "";
	        
	        //System.out.println("req object-->"+req);
	       
	        String searchCourseName = req.getParameter("courseName");
	        String searchCourseCode = req.getParameter("courseCode");
	        //System.out.println("entered Course Name --> "+searchCourseName+" ::: entered Course Code ---> "+searchCourseCode);
	        
	        CourseDetails[] modifiedCourseDets = null;
	        
	        CourseDetails[] courseDetails = sceManager.getAllSearchedCourseDetails(searchCourseName, searchCourseCode);
	        
	       /* CourseDetails courseDetail;
	        for (CourseDetails courseDetails2 : courseDetails) 
	        {
				courseDetail=new CourseDetails();
				req.setAttribute("courseIds", courseDetails2.getCourseId());
				System.out.println("course id "+courseDetails2.getCourseId());
			}*/
	        req.setAttribute("courseSearchArr", courseDetails);	        
	        req.setAttribute("searchCourseName", searchCourseName);
	        req.setAttribute("searchCourseCode", searchCourseCode);
	        
	        
	        
	       // System.out.println("CourseDetails[] Array Size --->"+courseDetails.length);
	        
	        if(courseDetails.length==0){
	           
	           msg = "There are no Courses matching the entered search criteria. Please try again."; 
	           //System.out.println("courseDetails.length==0-----msg = "+msg);
	           req.setAttribute("message", msg);
	        }
	        else if(courseDetails.length>=250){
	            msg = "250 records have been fetched so far. Please enter more specific search criteria to limit the search results";    
	           // System.out.println("courseDetails.length>250-----msg = "+msg);
	            req.setAttribute("message", msg);
	        }
	        
	        }catch(SCEException scee){
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return new String("failure");
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	            //sceLogger.error(LoggerHelper.getStackTrace(e));
	            return new String("failure");
	        }
	        return "success";    
	    }
	  
	 public String deleteLmsMappings(){
         //System.out.println("deleteLmsMappings()");
         HttpServletRequest req = getServletRequest();
         
        // System.out.println("passed parameter : " +req);
         //System.out.println("passed parameter : " +req.getParameter("mappedRecs"));
         String delMessage = "";
         String strLmsId="";
         Integer delLmsId = null;
         Integer selTemplateId = null;
         String templateName = null;
         CourseEvalTemplateMapping[] lmsMappings = null;
         
         
         
         try{   
        	 
             if(req.getParameter("selEvalTemplate")!=""){
               selTemplateId = new Integer(Integer.parseInt(req.getParameter("selEvalTemplate")));
              // System.out.println("Template Id = "+selTemplateId);
             }
             if(req.getParameter("hdnSelectedTemplate")!=null){
               templateName = req.getParameter("mappedRecs"); //String
              // System.out.println("Selected EvalTempName = "+templateName);     
             }
             /*if(req.getQueryString()!=null){
               strLmsId = req.getQueryString();
               System.out.println("Length of input string = "+strLmsId.length());
               strLmsId=(strLmsId.trim()).substring(4, strLmsId.length());
               System.out.println("strLmsId = "+strLmsId); 
               delLmsId = new Integer(strLmsId);
               System.out.println("delLmsId = "+delLmsId); 
             }
*/            
               delLmsId = new Integer(Integer.parseInt(req.getParameter("hdnDelete"))); //String
              // System.out.println("delLmsId = "+delLmsId);     
             
            // System.out.println("Call method [deleteSelLmsRecord(delRec)] from manager");
             lmsMappings=sceManager.deleteSelLmsRecord(delLmsId, selTemplateId);
             retrieveMappingDetails();
             //System.out.println("Size of lmsMappings array = "+lmsMappings.length);
             req.setAttribute("lmsCourseMapping", lmsMappings);
             req.setAttribute("selectedTempId", (""+selTemplateId));
             req.setAttribute("selectedTempName", templateName);
         }catch(SCEException scee){
           req.setAttribute("errorMsg",scee.getErrorCode());
           return "failure";
         }catch(Exception e){
           req.setAttribute("errorMsg","error.sce.unknown");
           //sceLogger.error(LoggerHelper.getStackTrace(e));
           return "failure";
       }
       
        return "success";   
   }
	 
	 public String saveCourseMappings(){
	     // System.out.println("Inside saveCourseMappings()");
	      HttpServletRequest req = getServletRequest();
	      try{
	      
	      String excludeActPk = "";
	      String strTemplateId = "";
	      String selectedTemplateName = "";
	      String selectedCourseIds = "";
	      String successMsg = "";
	      String[] alreadyMappedRecs = null;
	      String[] newlyAddedRecs = null;
	      String strUnmappedCrsIds = "";
	      String strUnmappedCrsNames = "";
	      String strUnmappedCrsCodes = "";
	      
	      String tempSelCourseIds = "";
	      
	      Util chkNull = new Util();
	      User usr = null;
	      String emplId = ""; 
	      
	      String[] savableRecs = null;
	      
	           
	      if(req.getParameter("alreadyMappedActPk")!=""){
	          excludeActPk = req.getParameter("alreadyMappedActPk"); //Comma delimited Activity PK for Courses that are already saved
	          alreadyMappedRecs =  excludeActPk.split(",");
	         // System.out.println("Request param : excludeActPk = "+excludeActPk);
	          //System.out.println("Arry of alreadyMappedRecs = "+alreadyMappedRecs.length);
	         } 
	         if(req.getParameter("hdnSelectedTemp")!=""){
	           strTemplateId = req.getParameter("hdnSelectedTemp");
	           //System.out.println("Request param : strTemplateId = "+strTemplateId); 
	         }
	         if(req.getParameter("hdnSelectedTemplate")!=""){
	           selectedTemplateName = req.getParameter("hdnSelectedTemplate"); 
	           //System.out.println("Request param : selectedTemplateName = "+selectedTemplateName);
	         }
	               
	         if(req.getParameter("hiddenCourseIds")!=""){
	           selectedCourseIds = req.getParameter("hiddenCourseIds");
	          // System.out.println("Request param : selectedCourseIds = "+selectedCourseIds);
	               if(!selectedCourseIds.equals("")){
	                   newlyAddedRecs =  selectedCourseIds.split(",");
	                  // System.out.println("Array of new added records = "+newlyAddedRecs.length);
	               }
	         }
	         if(req.getParameter("hdnSavableRecs")!=""){
	           if(!selectedCourseIds.equalsIgnoreCase("")){
	               selectedCourseIds=req.getParameter("hdnSavableRecs");
	               //System.out.println("Request param : tempSelCourseIds = "+selectedCourseIds);
	           }
	         }
	         
	      //usr = (User)req.getAttribute("user");
	      usr = (User) getSession().getAttribute("user");
	      //System.out.println("user "+usr);
	        emplId = usr.getEmplId();
	        //System.out.println("*************************************************usr = "+usr);
	        //System.out.println("*************************************************emplId = "+emplId);
	      
	     // System.out.println("Calling method from Manager...saveMappedRecords");
	      CourseEvalTemplateMapping[] savedRecords = sceManager.saveMappedRecords(emplId, excludeActPk, strTemplateId, selectedTemplateName, selectedCourseIds);
	      //System.out.println("saveMappedRecords() --All saved records = "+savedRecords.length);
	      
	      //System.out.println("saveMappedRecords() ---Length of alreadyMappedRecs req. param= "+alreadyMappedRecs.length);
	      
	      CourseDetails[] unmappedCourses = sceManager.getCourseDetailsForActPk(alreadyMappedRecs);
	      //System.out.println("saveMappedRecords() - Length of unmappedCourses JPF = "+unmappedCourses.length);
	      
	      if(unmappedCourses!=null && unmappedCourses.length!=0){
	        CourseDetails obj = new CourseDetails();
	        String delimiter=""; 
	        for(int i=0; i<unmappedCourses.length; i++) {
	            if(i==(unmappedCourses.length-1)){delimiter="";}else{delimiter=",";}
	            obj = unmappedCourses[i];
	            strUnmappedCrsIds = strUnmappedCrsIds+obj.getCourseId()+delimiter;
	            strUnmappedCrsNames = strUnmappedCrsNames+(chkNull.isNull(obj.getCourseName()))+delimiter;
	            strUnmappedCrsCodes = strUnmappedCrsCodes+(chkNull.isNull(obj.getCourseCode()))+delimiter;        
	        }   
	      }
	           
	      
	      //Set all request attributes 
	      if(savedRecords!=null && savedRecords.length!=0){
	         req.setAttribute("allMappingRecords", savedRecords); //All records mapped to this Template ID
	      }
	      
	      if(newlyAddedRecs!=null && newlyAddedRecs.length!=0){
	        successMsg = newlyAddedRecs.length+" out of "+(newlyAddedRecs.length+unmappedCourses.length)+" record(s) were inserted successfully into the database";
	      }
	      //System.out.println("successMsg = "+successMsg);
	      //validateString = validateString+lenSavableRecs+" out of "+lenSavableRecs+" record(s) were inserted successfully into the database";
	      
	      req.setAttribute("successMessage", successMsg);
	      req.setAttribute("templateId", strTemplateId);
	      req.setAttribute("templateName", selectedTemplateName);
	      req.setAttribute("strUnmappedCourseIds", strUnmappedCrsIds);
	      req.setAttribute("strUnmappedCrsNames", strUnmappedCrsCodes);
	      req.setAttribute("strUnmappedCrsCodes", strUnmappedCrsNames);
	      }catch(SCEException scee){
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return "failure";
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	           // sceLogger.error(LoggerHelper.getStackTrace(e));
	            return "failure";
	        }
	      return "success";   
	    }

	 public String validateAlreadyMapped(){
	      //System.out.println("Inside validateAlreadyMapped()");
	      HttpServletRequest req = getServletRequest();
	      try{
	              HttpSession session = req.getSession(); 
	              
	              String selCourseIds = "";
	              String selCourseCodes = "";
	              String selCourseNames = "";
	              Integer selEvalTempId = null;
	              String selEvalTempName = "";
	              String validateString = "";
	              CourseEvalTemplateMapping[] evalTempCourseMapping = null;
	              String validatedCourseCodes = "";
	              String validatedCourseNames = "";
	              Integer[] arrCrsIds = null;
	              User usr = null;
	              String emplId = "";
	              
	              Integer[] arrActivityPk=null;
	              Integer[] arrExcludedActPk=null;
	              Integer[] arrSavableRecs=null;
	              int lenSavableRecs=0;
	              String savableCourseIds="";
	              String delimiter="";
	              
	            String str1="";
	            String tempstr1;
	            boolean commaFlag = false;
	            boolean chkFlag=false;
	            String validateAlertString="";
	              
	              
	      
	              if(req.getParameter("hiddenCourseIds")!=null){    
	                selCourseIds = req.getParameter("hiddenCourseIds"); //Comma separated String
	              //  System.out.println("Selected CourseIds = "+selCourseIds);
	              }
	              if(req.getParameter("hiddenCourseCodes")!=null){
	                selCourseCodes = req.getParameter("hiddenCourseCodes"); //Comma separated String
	               // System.out.println("Selected CourseCodes = "+selCourseCodes);
	              }
	              if(req.getParameter("hiddenCourseNames")!=null){
	                selCourseNames = req.getParameter("hiddenCourseNames"); //Comma separated String
	               // System.out.println("Selected CourseNames = "+selCourseNames);
	              }
	              if(req.getParameter("hdnSelectedTemp")!=null){
	                selEvalTempId = new Integer(Integer.parseInt(req.getParameter("hdnSelectedTemp"))); //String
	               // System.out.println("Selected EvalTempId = "+selEvalTempId);  
	              }
	              if(req.getParameter("hdnSelectedTemplate")!=null){
	                selEvalTempName = req.getParameter("hdnSelectedTemplate"); //String
	                //System.out.println("Selected EvalTempName = "+selEvalTempName);     
	              }
	              
	            
	                if((User)session.getAttribute("user")!=null){
	                   // System.out.println("User from session");
	                    usr = (User)session.getAttribute("user");
	                    emplId = usr.getEmplId();
	                }
	                else{
	                    //System.out.println("No user from session. Hardcoding emplid");
	                    emplId = "111111";
	                }
	                
	               // System.out.println("*************************************************emplId = "+emplId);
	      
	                //FIRST Step -> Check if the sent Activity PK's are existing in the DB
	                //System.out.println("Calling method from Manager - getAlreadyMappedCourses() to check if the Activity PK's are existing");
	                CourseEvalTemplateMapping[] alreadyMappedCourses = sceManager.getAlreadyMappedCourses(selCourseIds, selCourseCodes, selEvalTempId); 
	                //System.out.println("Return from DB call......Length of alreadyMappedCourses ARRAY = "+alreadyMappedCourses.length);
	                  
	      
	                //SECOND STEP->Form an array of Activity PK, Integer, passed from front end
	                if (!selCourseIds.equalsIgnoreCase("") && selCourseIds!= null){
	                        //System.out.println("selectedCourseIds WILL BE split");
	                        String[] strArrActPk = selCourseIds.split(",");
	                       // System.out.println("Length of String array strArrActPk that have been sent from FE = " + strArrActPk.length);
	                        if(strArrActPk!=null){
	                            arrActivityPk = new Integer[strArrActPk.length];
	                            for(int i=0; i<strArrActPk.length; i++){
	                                //Integer array containing all Activity PK to be saved
	                               // System.out.println("strArrActPk["+i+"] = "+strArrActPk[i]);
	                                arrActivityPk[i] = new Integer(Integer.parseInt(strArrActPk[i]));
	                            }
	                        }
	               // System.out.println("Length of Integer array arrActivityPk that have been sent from FE =  " + arrActivityPk.length);
	                }
	        
	        
	                //THIRD STEP->Form an array of existing Activity PK, Integer (retrieved from DB in FIRST Step)
	                if(alreadyMappedCourses.length!=0){
	                    CourseEvalTemplateMapping objalreadyMapped = new CourseEvalTemplateMapping();
	                    arrExcludedActPk = new Integer[alreadyMappedCourses.length];
	                    //System.out.println("Length of Integer array arrExcludedActPk that have been retrieved from DB =  " + arrExcludedActPk.length);
	                    //System.out.println("Print all the ACTIVITY PK from the alreadyMappedCourses array");
	                    for(int z=0; z<alreadyMappedCourses.length; z++){
	                        objalreadyMapped = alreadyMappedCourses[z];
	                        arrExcludedActPk[z]=objalreadyMapped.getActivityPk();
	                        //System.out.println("arrExcludedActPk[z] = "+arrExcludedActPk[z]);
	                        
	                    }
	                //System.out.println("Length of Integer array arrExcludedActPk that was populated based on  alreadyMappedCourses[]=  " + arrExcludedActPk.length);
	                }
	        
	        
	                //FOURTH STEP->If existing Activity PK array is empty||null, set the length of savable records to length of activityID's sent from FE
	                if(arrActivityPk!=null && arrExcludedActPk==null){
	                   //Set the length of the savable records to length of activityID's sent from FE
	                   lenSavableRecs =  arrActivityPk.length;
	                   //System.out.println("KK: lenSavableRecs for (arrActivityPk!=null && arrExcludedActPk==null)" + lenSavableRecs);
	                }
	                else if(arrActivityPk!=null && arrExcludedActPk!=null){
	                   //Set the length of the savable records = total length received from FE-length of actPk's inside the excludedActPks 
	                   lenSavableRecs =  arrActivityPk.length-arrExcludedActPk.length;
	                    /*Piece of code will go here*/ 
	                    //System.out.println("KK: lenSavableRecs " + lenSavableRecs);
	                    arrSavableRecs = new Integer[lenSavableRecs];
	                    
	                    for(int z=0; z<arrActivityPk.length; z++){
	                       //System.out.println("KK: in loop z" + z); 
	                        chkFlag=false;
	                        
	                        for(int y=0; y<arrExcludedActPk.length; y++){
	                           //System.out.println("KK: in loop y" + y + "comparing " +arrActivityPk[z].intValue() + "and " + arrExcludedActPk[y].intValue() ); 
	                           if(arrActivityPk[z].intValue() == arrExcludedActPk[y].intValue()) {
	                                chkFlag=true;
	                               // System.out.println("KK: setting to true");
	                                break;
	                           }
	                            else
	                                continue;
	                    
	                        }
	                         if(chkFlag==false){
	                            //arrSavableRecs[z] = arrActivityPk[z];
	                            if(commaFlag == true){
	                                str1=str1+",";
	                               // System.out.println("KK: appended comma");
	                            }
	                            tempstr1 = "";
	                            tempstr1 = (String)arrActivityPk[z].toString();
	                           // System.out.println("KK: tempstr1 = "+tempstr1);
	                            str1+=tempstr1;
	                            commaFlag = true;
	                         }
	                        //End inner for :: y  
	                        //System.out.println("str1= "+str1); 
	                    }//End outer for :: z
	                  //  System.out.println("KK: str1 = "+str1);
	                  
	                }

	     
	     
	                //SETTING request attributes based on already mapped and selected records
	                //If there are no courses that are already mapped to any Template ID, then save the courses that have been selected
	                if(!selCourseIds.equals("") && alreadyMappedCourses.length==0){
	                  //  System.out.println("Inside if condition for to-be mapped selCourseIds is not empty but alreadyMappedCourses is NULL and length == 0");
	                    validateString = "";
	            
	                    //Since none of the records are already mapped, save the sent records and return ALL mappings for this template
	                    CourseEvalTemplateMapping[] savedRecords = sceManager.saveMappedRecords(emplId, "", (""+selEvalTempId), selEvalTempName, selCourseIds);   
	                   // System.out.println("JPF::ALL the records - savedRecords.length = "+savedRecords.length); 
	            
	                    validateString = validateString+lenSavableRecs+" out of "+lenSavableRecs+" record(s) were inserted successfully into the database";
	                   // System.out.println("validateString CONDITION 1 when no other records are mapped = "+validateString); 
	                    evalTempCourseMapping = sceManager.getAllMappingsForTemplate(selEvalTempId);
	                   // System.out.println("ALL records for this template ID = "+evalTempCourseMapping.length); 
	                    req.setAttribute("validateString", validateString);
	                    req.setAttribute("savedRecords", savedRecords);
	                    req.setAttribute("selectedCourseIds", selCourseIds);
	                    //req.setAttribute("selectedCourseCodes", selCourseCodes);
	                    //req.setAttribute("selectedCourseNames", selCourseNames);
	                    
	                    req.setAttribute("savableRecords", new Integer(lenSavableRecs));
	                }
	                else if(!selCourseIds.equals("") && alreadyMappedCourses.length!=0){//if some/all of the courses that were sent from FE, are mapped
	                   // System.out.println("Inside else-if condition for alreadyMappedCourses is not NULL and length != 0 but to-be-mapped selCourseIds is not empty");
	                    for(int z=0; z<alreadyMappedCourses.length; z++){
	                        CourseEvalTemplateMapping obj = new CourseEvalTemplateMapping();
	                        obj = alreadyMappedCourses[z];
	                        arrCrsIds = new Integer[alreadyMappedCourses.length];
	                        arrCrsIds[z]=obj.getActivityPk();
	                        if(z==(alreadyMappedCourses.length-1)){delimiter="";}else{delimiter=",";}
	                
	                        if(!obj.getCourseCode().equals("")){
	                            validatedCourseCodes = validatedCourseCodes+obj.getCourseCode()+delimiter;
	                        }else{validatedCourseCodes = validatedCourseCodes+obj.getCourseCode();}
	                        if(!obj.getCourseName().equals("")){
	                            validatedCourseNames = validatedCourseNames+obj.getCourseName()+delimiter;
	                        }else{validatedCourseNames = validatedCourseNames+obj.getCourseName();}
	                    }//End for
	              
	                  //  System.out.println("validatedCourseCodes = "+validatedCourseCodes);//List of course names that are already existing
	                  //  System.out.println("validatedCourseNames = "+validatedCourseNames);//List of course codes that are already existing
	                   // System.out.println("Length of arrCrsIds of already mapped courses = "+arrCrsIds.length);    
	                    validateString = "";
	      
	                    evalTempCourseMapping = sceManager.getAllMappingsForTemplate(selEvalTempId);
	                   // System.out.println("getAllMappingsForTemplate---Length of all mappings array = "+evalTempCourseMapping.length);  
	                    
	                    if(alreadyMappedCourses.length==arrActivityPk.length){
	                         validateAlertString = "0 out of "+arrActivityPk.length+" record(s) were inserted successfully into the database";
	                       //  System.out.println("validateString CONDITION 2 when all records are mapped = "+validateAlertString); 
	                    }else{
	                         validateAlertString = ""+validateAlertString+alreadyMappedCourses.length+" out of "+arrActivityPk.length+" record(s) are already mapped";
	                       //  System.out.println("validateString CONDITION 3 when some records are mapped = "+validateAlertString); 
	                    }
	                    
	                    req.setAttribute("validateAlertString", validateAlertString);
	                    req.setAttribute("existingEvalCourseMapping", alreadyMappedCourses);
	                    req.setAttribute("validatedCourseNames", validatedCourseNames);
	                    req.setAttribute("validatedCourseCodes", validatedCourseCodes);
	                    req.setAttribute("savableRecords", new Integer(lenSavableRecs));
	                    //req.setAttribute("savableCourseIds", savableCourseIds);
	                    req.setAttribute("savableCourseIds", str1);
	                    //req.setAttribute("alreadyMappedCourses", alreadyMappedCourses);
	                    req.setAttribute("selectedCourseIds", selCourseIds);
	                   
	                    
	        
	                }//End else
	              req.setAttribute("selectedTemplateId", selEvalTempId);
	              req.setAttribute("selectedTemplateName", selEvalTempName);
	              req.setAttribute("courseEvalTempMapping", evalTempCourseMapping);
	        }catch(SCEException scee){
	        	
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return "failure";
	        }catch(Exception e){
	        	e.printStackTrace();
	            req.setAttribute("errorMsg","error.sce.unknown");
	            //sceLogger.error(LoggerHelper.getStackTrace(e));
	            return "failure";
	        }
	      return "success";  
	    }//End method

	 
	 private String checkLegalConsent(HttpServletRequest req, HttpSession session) 
	 {

			//System.out.println("Entry in to Helper.checkLegalConsent method...");

			String ntid = "";
			try {
				User user = (User) session.getAttribute("user");
				//System.out.println(" User Object:" + user);
				if (user == null) {
					ntid = req.getHeader("IAMPFIZERUSERPFIZERNTLOGONNAME");
					System.out.println("Getting ntid from IAM Header ntid:" + ntid);
					String emplid = req.getHeader("IAMPFIZERUSERWORKFORCEID");
					System.out.println("Getting emplid from IAM Header emplid:"+ emplid);
					String domain = req
							.getHeader("IAMPFIZERUSERPFIZERNTDOMAINNAME");
					System.out.println("Getting domain from IAM Header domain:"+ domain);
					if (ntid == null || ntid.equals("")) {
						System.out.println("ntid//" + ntid + "//");
						System.out.println("User Object is not available in session.");
						
					}
					//System.out.println("ntid is ://" + ntid + "//");
				} else {
					System.out.println("Valid User Object:" + user);
					ntid = user.getNtid();
					System.out.println("User NTID IS:" + user.getNtid());
					System.out.println("User emplId IS:" + user.getEmplId());
				}

				UserLegalConsent userLegalConsent = new UserLegalConsent();
				userLegalConsent = sceManager.checkLegalConsentAcceptance(ntid);
				if (userLegalConsent == null) {
					LegalConsentTemplate legalConsentTemplate = new LegalConsentTemplate();
					
					legalConsentTemplate = sceManager.fetchLegalContent();
					//System.out.println("legalConsentTemplate.getContent() - "+ legalConsentTemplate.getContent());
					req.setAttribute("content", legalConsentTemplate.getContent());
					req.setAttribute("forLcid", legalConsentTemplate);
					System.out
							.println(" Exit from Helper.checkLegalConsent method before forwarding to legalConsent.jsp");
					return "success";

				} else {
					//System.out.println("Exit from Helper.checkLegalConsent method before forwarding to failure");
					return "failure";
				}
			} catch (Exception e) {
				req.setAttribute("errorMsg", "error.sce.unknown");
				// sceLogger.error(LoggerHelper.getStackTrace(e));
				//System.out.println("Exit from Helper.checkLegalConsent method before forwarding to exception");
				return "exception";
			}

		}
}
