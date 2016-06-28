package com.pfizer.sce.Action;


import java.sql.Array;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.sce.beans.Event;
import com.pfizer.sce.beans.EventsCreated;
import com.pfizer.sce.beans.GuestTrainer;
import com.pfizer.sce.beans.Learner;
import com.pfizer.sce.beans.LegalConsentTemplate;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.beans.UserLegalConsent;
import com.pfizer.sce.beans.TrainerLearnerMapping;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.EvaluationControllerHelper;

public class TrainerLearningMappingAction extends ActionSupport implements ServletRequestAware,ModelDriven
{
	private static SCEManagerImpl sceManager = new SCEManagerImpl();
	HttpServletRequest req;

    
	public void setServletRequest(HttpServletRequest request) {
		this.req = request;

	}

	public HttpServletRequest getServletRequest() {
		return this.req;
	}

	public HttpSession getSession() {
		return req.getSession();
	}

	public HttpServletRequest getRequest() {
		return req;
	}

	public void setRequest(HttpServletRequest request) {
		this.req = request;
	}


	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	 public String gotoTrainerLearnerMapping() throws SCEException
	    {
	        
		    req = getServletRequest();
	       HttpSession session = req.getSession(false);
	       try
	       {
	       String result = checkLegalConsent(req,session);
	       // System.out.println("*****result*****:"+result);
	       if(result != null && result.equals("success")  ){
	        // System.out.println("*************Forwarding to legalConsent");
	        String forwardToHomePage = "N";
	        EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
	        return "legalConsent";
	       }else if(result != null && result.equals("exception")){
	         // System.out.println("**********Forwarding to exception");
	         return "failure";
	       }
	       Integer eventId = new Integer(-1);
	       Event[] eventsName=sceManager.getAllEvents();
	       req.setAttribute("eventsName",eventsName);
	       }
	       catch (Exception e) 
	       {
				req.setAttribute("errorMsg", "error.sce.unknown");
				// sceLogger.error(LoggerHelper.getStackTrace(e));
				// System.out.println("Exit from Helper.checkLegalConsent method before forwarding to exception");
	       }
	       return "success";        
	    }
	 
	 public String getEventCourses()
	    {
		  req = getServletRequest();	         
		 HttpSession session = req.getSession();    

	    try{

	        String result = checkLegalConsent(req,session);
	        // System.out.println("*****result*****:"+result);
	        if(result != null && result.equals("success")  ){
	            // System.out.println("*************Forwarding to legalConsent");
	            String forwardToHomePage = "Y";
	            EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
	            return "legalConsent";
	        }else if(result != null && result.equals("exception")){
	            // System.out.println("**********Forwarding to exception");
	            return "failure";
	        }

	       // String selProductIndex = req.getParameter("selProductIndex");
	       // String selProductName = req.getParameter("selProductName");
	        String eventIndex = req.getParameter("hdn1");
	        String eventName = req.getParameter("hdn4");
	        String evalStartTime=req.getParameter("evalStartTime");
	         req.setAttribute("evalStartTime",evalStartTime);

	       if(!eventIndex.equalsIgnoreCase("0")){
	        String[] eventPCourses = sceManager.getEventCourses(eventName);  
	       //     gotoRetrieveEventMapping();
	            
	         //   req.setAttribute("selProductIndex",selProductIndex);
	          //  req.setAttribute("selProductName",selProductName);
	             req.setAttribute("eventPCourses",eventPCourses);
	              req.setAttribute("selEventOption",eventIndex);
	       }
	       else
	       {
	          
	            req.setAttribute("eventPCourses",null);
	        //    req.setAttribute("selProductIndex",selProductIndex);
	             req.setAttribute("selEventOption",eventIndex);
	       }
	
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	           // sceLogger.error(LoggerHelper.getStackTrace(e));
	            return "failure";
	        }
	        return "success";    
	                
	    }
	 
	 
	 
	 public String getMappedTrainerLearners()
	 {
		 
		  req = getServletRequest();
	        try{
	        String whichEvent=req.getParameter("hdn1");	       
	         String selCoursesID=req.getParameter("hdn7");  	        
	         String courseStartDate=req.getParameter("hdn3");	       
	         String product=req.getParameter("hdn6");	      
	         String checkForFunctionality= req.getParameter("hdn5");	      
	         String eventName= req.getParameter("hdn4");   
	         // System.out.println(eventName);
	         String selCourseDateId = req.getParameter("hdn2");
	         req.setAttribute("selCourseDateId", selCourseDateId);
	         String evalStartTime=req.getParameter("evalStartTime");
	         req.setAttribute("evalStartTime",evalStartTime);
	          EventsCreated event=new EventsCreated();
	        event.setEventName(eventName);//Change
	        event=sceManager.getEvent(event);
	        String sdate=event.getEventStartDate();
	        String edate=event.getEventEndDate();
	        //// System.out.println("SDate"+sdate);
	        String startDate=sdate.substring(5,7)+"-"+sdate.substring(8,10)+"-"+sdate.substring(0,4);
	        String endDate=edate.substring(5,7)+"-"+edate.substring(8,10)+"-"+edate.substring(0,4);
	        //// System.out.println("SDate"+ssdate);
	        SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yyyy");
	        Date d1= sdf.parse(startDate);
	        Date d2=sdf.parse(endDate);
	        // System.out.println("SDate"+d1);
	        // System.out.println("EDate"+d2);
	        int diff=(int)(d2.getTime()-d1.getTime())/(1000*60*60*24);
	        diff=diff+1;
	        // System.out.println("DIFF"+diff);
	        //// System.out.println("SDate"+sdf.format(date));
	        //***********************************************************************************
	        
	        System.out.println("CourseStartDAte:"+courseStartDate);
	        System.out.println("Start date:"+startDate);
	        System.out.println("enddate:"+endDate);
	       //added by manish on 2/11/2016 for getting total hours avail. for each trainer 
	        
	       TrainerLearnerMapping[] trainerLearner1 = sceManager.getevaluationHours(courseStartDate);
	       		
	      Array values[] = null;
	      
	      HashMap hm  = new HashMap();
	       	
	  	//ongoing code to calculate number of evaluations for each trainer
	      	
	      List<Integer> calculateEvaluations = new ArrayList<Integer>();
	      	Integer hours;
	      	Integer evaluationCount;
	       	for (TrainerLearnerMapping t1 : trainerLearner1) {
	       		
	       		System.out.println("courseStartDate:"+t1.getCourseStartDate1());
	       		System.out.println("Event_name:"+t1.getEventName());
	       		System.out.println("Product:"+t1.getProduct());
	       		System.out.println("trainers_email:"+t1.getTrainerEmail());
	       		System.out.println("total_hours:"+t1.getTotalHours());
	       		
	       		hours = Integer.parseInt(t1.getTotalHours());
	       		Integer hoursTomin= hours*60;
	       		
	       		
	       		Integer evalTime=event.getEvalDuration();
	       		evaluationCount=hoursTomin/evalTime;
	       		//**************************
	       			
	       			hm.put(t1.getTrainerEmail(),evaluationCount );
	       			
	       		
	       		//***************************
	       		calculateEvaluations.add(evaluationCount);
	       	
			}
	       	System.out.println("No. of evaluations:"+calculateEvaluations);
	       
	        
	        
	        System.out.println("Check for funtionality:"+checkForFunctionality);
	        
	        
	        
	        
	      //**************************************************************************************
	        int numberOfLearners=(event.getNumberOfLearners()==null)?0:event.getNumberOfLearners().intValue();
	        int numberOfEval=(event.getNumberOfEval()==null)?0:event.getNumberOfEval().intValue();
	        //commented
	        //int evalPerTrainer=diff;
	        int evalPerTrainer=0;
	    	System.out.println("No. of evaluations which is set for all:"+numberOfEval);
	        System.out.println("Diff:"+diff);
	        System.out.println("No. of learners:"+numberOfLearners);
	        
	        //commented
	       /* if(numberOfLearners!=0 && numberOfEval!=0)
	        {
	            //evalPerTrainer=((numberOfLearners)/(diff*numberOfEval));
	            //evalPerTrainer=diff*numberOfEval;
	            //Changed 22nd Aug
	            evalPerTrainer=numberOfEval;
	        }
	         
	        System.out.println("evaluation Per trainer:"+evalPerTrainer);*/
	        
	        
	         if(checkForFunctionality.equalsIgnoreCase("N"))
	        {
	        	 System.out.println("Entered in case with N");
	       TrainerLearnerMapping[] trainerLearner = sceManager.getTrainerLearner(courseStartDate,product,eventName);
	       
	       System.out.println("Trainer Learner Length:"+trainerLearner.length);
	       //changes start
	       HashMap trainerAlreadyMappedMap=new HashMap();
	       
	       //method to focus on
	       GuestTrainer[] trainers = sceManager.getTrainerForAuto(product,eventName,courseStartDate);
	        //right above
	       	System.out.println("Trainer Length via getTrainerForAuto:"+trainers.length);
	      //changed 22nd Aug
	        TrainerLearnerMapping[] trainerAlreadyMapped = sceManager.getAlreadyMappedTrainer(eventName,courseStartDate);
	        System.out.println("trainer Already mapped length:"+trainerAlreadyMapped.length);
	        if(trainerAlreadyMapped!=null && trainerAlreadyMapped.length>0){
	        for(int i=0;i<trainerAlreadyMapped.length;i++){
	            trainerAlreadyMappedMap.put(trainerAlreadyMapped[i].getTrainerEmail(),trainerAlreadyMapped[i].getTrainerCount());
	        }
	        }
	        
	         Learner[] learners= sceManager.getLearnerForAuto(courseStartDate,eventName);
	        //if(trainerLearner==null || trainerLearner.length==0){
	         System.out.println("Learner length:"+learners.length);
	         if(learners!=null && learners.length>0){  
	         /*   EventsCreated event=new EventsCreated();
	        event.setEventName(eventName);//Change
	        event=sceManager.getEvent(event);
	        String sdate=event.getEventStartDate();
	        String edate=event.getEventEndDate();
	        //// System.out.println("SDate"+sdate);
	        String startDate=sdate.substring(5,7)+"-"+sdate.substring(8,10)+"-"+sdate.substring(0,4);
	        String endDate=edate.substring(5,7)+"-"+edate.substring(8,10)+"-"+edate.substring(0,4);
	        //// System.out.println("SDate"+ssdate);
	        SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yyyy");
	        Date d1=sdf.parse(startDate);
	        Date d2=sdf.parse(endDate);
	        // System.out.println("SDate"+d1);
	        // System.out.println("EDate"+d2);
	        int diff=(int)(d2.getTime()-d1.getTime())/(1000*60*60*24);
	        diff=diff+1;
	        // System.out.println("DIFF"+diff);
	        //// System.out.println("SDate"+sdf.format(date));
	        int numberOfLearners=(event.getNumberOfLearners()==null)?0:event.getNumberOfLearners().intValue();
	        int numberOfEval=(event.getNumberOfEval()==null)?0:event.getNumberOfEval().intValue();
	        int evalPerTrainer=diff;
	        if(numberOfLearners!=0 && numberOfEval!=0)
	        {
	        	 System.out.println("Entered if condition");
	            evalPerTrainer=diff*numberOfEval;
	        }*/
	        //Learner data change
	        //Learner[] learners=sceManager.getLearnersByCourseAndDate("2010 Field Based Medical (FBM) National Meeting","4/19/2010","5/19/2010");
	        //Trainer data change
	        //GuestTrainer[] trainers=sceManager.getGTByProduct("CHANTIX");
	        HashMap locZone=sceManager.getLocationForTimeZone();
	        // System.out.println(locZone.size());
	        List trainerPST=new ArrayList();
	        List trainerCST=new ArrayList();
	        List trainerEST=new ArrayList();
	        List trainerMST=new ArrayList();
	        List trainerNoLoc=new ArrayList();
	        List learnerNotMapped=new ArrayList();
	        List learnerNoLoc=new ArrayList();
	        String timeZone="";
	    for(int i=0;i<trainers.length;i++){
			//System.out.print(i);
			//// System.out.println(trainers[i].getRepLocation());
	    	System.out.println("Trainers length in for loop:"+trainers.length);
			if(trainers[i].getRepLocation()==null || trainers[i].getRepLocation().length()==0){
				trainerNoLoc.add(trainers[i]);
			}
			else{
				timeZone=(String)locZone.get((String)trainers[i].getRepLocation());
				//// System.out.println(timeZone);
				if(timeZone!=null){
					if(timeZone.equals("PST")){
						trainerPST.add(trainers[i]);
					}
					else if(timeZone.equals("CST")){
						trainerCST.add(trainers[i]);
					}
					else if(timeZone.equals("EST")){
						trainerEST.add(trainers[i]);
					}
					else if(timeZone.equals("MST")){
						trainerMST.add(trainers[i]);
					}
					else{
						trainerNoLoc.add(trainers[i]);
					}
				}
				else{
					trainerNoLoc.add(trainers[i]);
				}
			}
		}

	    // System.out.println("eval/trainer"+evalPerTrainer);
		// System.out.println("TOTAL"+trainers.length);
		// System.out.println("PST"+trainerPST.size());
		// System.out.println("CST"+trainerCST.size());
		// System.out.println("MST"+trainerMST.size());
		// System.out.println("EST"+trainerEST.size());
		// System.out.println("No Loc"+trainerNoLoc.size());
		// System.out.println("TOTAL LEARNERS"+learners.length);
	    System.out.println("Trainers length:"+trainers.length);
		if(trainers.length>0){
	    int countPST=-2;
		int countMST=-2;
		int countCST=-2;
		int countEST=-2;
		int count=0;
		boolean isMapped=false;
		for(int i=0;i<learners.length;i++){
			isMapped=false;
			GuestTrainer t=new GuestTrainer();
			Learner learner=learners[i];
			/*    FOR TESTING
	        if(learner.getLocation()==null){
	            learner.setLocation("MN");
	        }*/
			if(learner.getLocation()!=null){
				timeZone=(String)locZone.get(learner.getLocation());
	           
				if(timeZone!=null)
				{
					if(timeZone.equals("PST"))
					{
						//call PST
	                    countPST=removeIfMapped(trainerPST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countPST);
						count=mapTrainerLearnerPST(trainerPST,countPST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
						if(count!=0)
						{
							
							if(count>countPST )
							{
								isMapped=true;
							}
							
							evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerPST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerPST.get(0)).getEvalDuration());
							if(evalPerTrainer>event.getNumberOfEval())
							{
								evalPerTrainer=event.getNumberOfEval();
								System.out.println("Number of eval for event:"+evalPerTrainer);
							}
							System.out.println("Number of eval for availibility:"+evalPerTrainer);
							
							if(count==evalPerTrainer)
							{
								countPST=0;
	                            trainerPST.remove(0);
							}
							else
							{
								countPST=count;
							}
						}
						else
						{
							countPST=-2;
						}
						//callMST
	                    countMST=removeIfMapped(trainerMST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countMST);
						count=mapTrainerLearnerMST(trainerMST,countMST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
						if(count!=-1 && count!=0)
						{
							if(count>countMST)
							{
								isMapped=true;
							}
							evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerMST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerMST.get(0)).getEvalDuration());
							if(evalPerTrainer>event.getNumberOfEval())
							{
								evalPerTrainer=event.getNumberOfEval();
								System.out.println("Number of eval for event:"+evalPerTrainer);
							}
							System.out.println("Number of eval for availibility:"+evalPerTrainer);
							
							if(count==evalPerTrainer)
							{
								countMST=0;
	                            trainerMST.remove(0);
							}else
							{
								countMST=count;
							}
						}
						//CALL CST
	                    countCST=removeIfMapped(trainerCST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countCST);
						count=mapTrainerLearnerCST(trainerCST,countCST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
						if(count!=-1 && count!=0)
						{
							if(count>countCST)
							{
								isMapped=true;
							}
							evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerCST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerCST.get(0)).getEvalDuration());
							if(evalPerTrainer>event.getNumberOfEval())
							{
								evalPerTrainer=event.getNumberOfEval();
								System.out.println("Number of eval for event:"+evalPerTrainer);
							}
							System.out.println("Number of eval for availibility:"+evalPerTrainer);
							
							if(count==evalPerTrainer)
							{
								countCST=0;
	                            trainerCST.remove(0);
							}
							else
							{
								countCST=count;
							}
						}
						//CALL EST
	                    countEST=removeIfMapped(trainerEST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countEST);
						count=mapTrainerLearnerEST(trainerEST,countEST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
						if(count!=-1 && count!=0)
						{
							if(count>countEST)
							{
								isMapped=true;
							}
							evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerEST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerEST.get(0)).getEvalDuration());
							if(evalPerTrainer>event.getNumberOfEval())
							{
								evalPerTrainer=event.getNumberOfEval();
								System.out.println("Number of eval for event:"+evalPerTrainer);
							}
							System.out.println("Number of eval for availibility:"+evalPerTrainer);
							
							if(count==evalPerTrainer)
							{
								countEST=0;
	                            trainerEST.remove(0);
							}
							else
							{
								countEST=count;
							}
						}else if(count==0){countEST=-2;}
					}else
					{
						if(timeZone.equals("CST"))
						{
							//CALL CST
	                     countCST=removeIfMapped(trainerCST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countCST);
							count=mapTrainerLearnerCST(trainerCST,countCST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
							if(count!=0)
							{
								if(count>countCST)
								{
									isMapped=true;
								}
								evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerCST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerCST.get(0)).getEvalDuration());
								if(evalPerTrainer>event.getNumberOfEval())
								{
									evalPerTrainer=event.getNumberOfEval();
									System.out.println("Number of eval for event:"+evalPerTrainer);
								}
								System.out.println("Number of eval for availibility:"+evalPerTrainer);
								
								if(count==evalPerTrainer)
								{
									countCST=0;
	                                trainerCST.remove(0);
								}
								else
								{
									countCST=count;
								}
							}
							else{countCST=-2;}
							//CALL EST
	                        countEST=removeIfMapped(trainerEST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countEST);
							count=mapTrainerLearnerEST(trainerEST,countEST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
							if(count!=-1 && count!=0)
							{
								if(count>countEST)
								{
									isMapped=true;
								}
								evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerEST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerEST.get(0)).getEvalDuration());
								if(evalPerTrainer>event.getNumberOfEval())
								{
									evalPerTrainer=event.getNumberOfEval();
									System.out.println("Number of eval for event:"+evalPerTrainer);
								}
								System.out.println("Number of eval for availibility:"+evalPerTrainer);
								if(count==evalPerTrainer)
								{
									countEST=0;
	                                trainerEST.remove(0);
								}
								else
								{
									countEST=count;
								}
							}
							else if(count==0){countEST=-2;}
							//CALL MST
	                        countMST=removeIfMapped(trainerMST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countMST);
							count=mapTrainerLearnerMST(trainerMST,countMST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
							if(count!=-1 && count!=0)
							{
								if(count>countMST)
								{
									isMapped=true;
								}
								evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerMST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerMST.get(0)).getEvalDuration());
								if(evalPerTrainer>event.getNumberOfEval())
								{
									evalPerTrainer=event.getNumberOfEval();
									System.out.println("Number of eval for event:"+evalPerTrainer);
								}
								System.out.println("Number of eval for availibility:"+evalPerTrainer);
								if(count==evalPerTrainer)
								{
									countMST=0;
	                                trainerMST.remove(0);
								}
								else
								{
									countMST=count;
								}
							}else if(count==0){countMST=-2;}
							//CALL PST
	                        countPST=removeIfMapped(trainerPST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countPST);
							count=mapTrainerLearnerPST(trainerPST,countPST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
							if(count!=-1 && count!=0)
							{
								if(count>countPST)
								{
									isMapped=true;
								}
								evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerPST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerPST.get(0)).getEvalDuration());
								if(evalPerTrainer>event.getNumberOfEval())
								{
									evalPerTrainer=event.getNumberOfEval();
									System.out.println("Number of eval for event:"+evalPerTrainer);
								}
								System.out.println("Number of eval for availibility:"+evalPerTrainer);
								if(count==evalPerTrainer)
								{
									countPST=0;
	                                trainerPST.remove(0);
								}else{
									countPST=count;
								}
							}else if(count==0){countPST=-2;}

						}
						else{
							if(timeZone.equals("MST"))
							{
								//CALL MST
	                            countMST=removeIfMapped(trainerMST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countMST);
								count=mapTrainerLearnerMST(trainerMST,countMST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
								if(count!=0){
									if(count>countMST)
									{
										isMapped=true;
									}
									evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerMST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerMST.get(0)).getEvalDuration());

									if(evalPerTrainer>event.getNumberOfEval())
									{
										evalPerTrainer=event.getNumberOfEval();
										System.out.println("Number of eval for event:"+evalPerTrainer);
									}
									System.out.println("Number of eval for availibility:"+evalPerTrainer);
									if(count==evalPerTrainer)
									{
										countMST=0;
	                                    trainerMST.remove(0);
									}else
									{
										countMST=count;
									}
								}else{countMST=-2;}


								//CALL CST
	                            countCST=removeIfMapped(trainerCST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countCST);
								count=mapTrainerLearnerCST(trainerCST,countCST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
								if(count!=-1 && count!=0)
								{
									if(count>countCST)
									{
										isMapped=true;
									}
									evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerCST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerCST.get(0)).getEvalDuration());
									
									if(evalPerTrainer>event.getNumberOfEval())
									{
										evalPerTrainer=event.getNumberOfEval();
										System.out.println("Number of eval for event:"+evalPerTrainer);
									}
									System.out.println("Number of eval for availibility:"+evalPerTrainer);
									if(count==evalPerTrainer)
									{
										countCST=0;
	                                    trainerCST.remove(0);
									}
									else
									{
										countCST=count;
									}
								}else if(count==0){countCST=-2;}
								//CALL PST
	                            countPST=removeIfMapped(trainerPST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countPST);
								count=mapTrainerLearnerPST(trainerPST,countPST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
								if(count!=-1 && count!=0)
								{
									if(count>countPST)
									{
										isMapped=true;
									}
									evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerPST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerPST.get(0)).getEvalDuration());
									
									if(evalPerTrainer>event.getNumberOfEval())
									{
										evalPerTrainer=event.getNumberOfEval();
										System.out.println("Number of eval for event:"+evalPerTrainer);
									}
									System.out.println("Number of eval for availibility:"+evalPerTrainer);
									if(count==evalPerTrainer)
									{
										countPST=0;
	                                    trainerPST.remove(0);
									}
									else
									{
										countPST=count;
									}
								}else if(count==0){countPST=-2;}
								//CALL EST
	                            countEST=removeIfMapped(trainerEST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countEST);
								count=mapTrainerLearnerEST(trainerEST,countEST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
								if(count!=-1 && count!=0)
								{
									if(count>countEST)
									{
										isMapped=true;
									}
									evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerEST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerEST.get(0)).getEvalDuration());
									if(evalPerTrainer>event.getNumberOfEval())
									{
										evalPerTrainer=event.getNumberOfEval();
										System.out.println("Number of eval for event:"+evalPerTrainer);
									}
									System.out.println("Number of eval for availibility:"+evalPerTrainer);
									if(count==evalPerTrainer)
									{
										countEST=0;
	                                    trainerEST.remove(0);
									}
									else
									{
										countEST=count;
									}
								}else if(count==0){countEST=-2;}

							}
							else{
								if(timeZone.equals("EST"))
								{
									//CALL EST
	                                countEST=removeIfMapped(trainerEST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countEST);
									count=mapTrainerLearnerEST(trainerEST,countEST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
									if(count!=0)
									{
										if(count>countEST)
										{
											isMapped=true;
										}
										evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerEST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerEST.get(0)).getEvalDuration());
										
										if(evalPerTrainer>event.getNumberOfEval())
										{
											evalPerTrainer=event.getNumberOfEval();
											System.out.println("Number of eval for event:"+evalPerTrainer);
										}
										System.out.println("Number of eval according to availability:"+evalPerTrainer);
										if(count==evalPerTrainer)
										{
											countEST=0;
	                                        trainerEST.remove(0);
										}
										else
										{
											countEST=count;
										}
									}else {countEST=-2;}

									//CST
	                                countCST=removeIfMapped(trainerCST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countCST);
									count=mapTrainerLearnerCST(trainerCST,countCST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
									if(count!=-1 && count!=0)
									{
										if(count>countCST)
										{
											isMapped=true;
										}
										evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerCST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerCST.get(0)).getEvalDuration());
										
										if(evalPerTrainer>event.getNumberOfEval())
										{
											evalPerTrainer=event.getNumberOfEval();
										}
										
										if(count==evalPerTrainer)
										{
											countCST=0;
	                                        trainerCST.remove(0);
										}
										else
										{
											countCST=count;
										}
									}else if(count==0){countCST=-2;}
									//callMST
	                                countMST=removeIfMapped(trainerMST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countMST);
									count=mapTrainerLearnerMST(trainerMST,countMST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
									if(count!=-1 && count!=0)
									{
										if(count>countMST)
										{
											isMapped=true;
										}
										evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerMST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerMST.get(0)).getEvalDuration());
										if(evalPerTrainer>event.getNumberOfEval())
										{
											evalPerTrainer=event.getNumberOfEval();
										}
										
										if(count==evalPerTrainer)
										{
											countMST=0;
	                                        trainerMST.remove(0);
										}
										else
										{
											countMST=count;
										}
									}else if(count==0){countMST=-2;}
									//CALL PST
	                                countPST=removeIfMapped(trainerPST,trainerAlreadyMappedMap,evalPerTrainer,isMapped,countPST);
									count=mapTrainerLearnerPST(trainerPST,countPST,learner,isMapped,evalPerTrainer,eventName,courseStartDate,product);
									if(count!=-1 && count!=0)
									{
										if(count>countPST)
										{
											isMapped=true;
										}
										evalPerTrainer=(Integer.parseInt(((GuestTrainer)trainerPST.get(0)).getTotalHours())*60)/Integer.parseInt(((GuestTrainer)trainerPST.get(0)).getEvalDuration());
										if(evalPerTrainer>event.getNumberOfEval())
										{
											evalPerTrainer=event.getNumberOfEval();
											
											System.out.println("Number of eval for event:"+evalPerTrainer);
										}
										System.out.println("Number of eval according to availability:"+evalPerTrainer);
										if(count==evalPerTrainer)
										{
											countPST=0;
	                                        trainerPST.remove(0);
										}
										else
										{
											countPST=count;
										}
									}else if(count==0){countPST=-2;}
								}
							}
						}
					}
				}
	            else
	            {
				learnerNotMapped.add(learner);
			}
			}
	        else
	        {
	            	learnerNoLoc.add(learner);
	        }
		}
	    Learner[] toBeMapped=null;
	    if(learnerNotMapped.size()>0){
	    int size=learnerNotMapped.size();
	    toBeMapped=new Learner[size];
	            for(int i=0;i<size;i++){
	                toBeMapped[i]=(Learner)learnerNotMapped.get(i);
	                }
	    }

	        // System.out.println("TOTAL"+trainers.length);
	        // System.out.println("PST"+trainerPST.size());
	        // System.out.println("CST"+trainerCST.size());
	        // System.out.println("MST"+trainerMST.size());
	        // System.out.println("EST"+trainerEST.size());
	        // System.out.println("No Loc"+trainerNoLoc.size());
	        // System.out.println("Learner Not Mapped"+learnerNotMapped.size());
	        // System.out.println("Learner No Loc"+learnerNoLoc.size());
	    }  
	        }
	       trainerLearner = sceManager.getTrainerLearner(courseStartDate,product,eventName);
	       //changes end
	     //changed 22nd Aug
	       TrainerLearnerMapping[]  trainerCount=sceManager.getCountOfEvalsPerTrainer(eventName,courseStartDate);
	        getEventCourses();
	        getCourseDates();
	        req.setAttribute("selCoursesID",selCoursesID); 
	        req.setAttribute("selCourseName",product);
	        req.setAttribute("selEventOption",whichEvent);
	        req.setAttribute("trainerLearner",trainerLearner);
	        req.setAttribute("courseStartDate",courseStartDate);
	        req.setAttribute("selCourseDateId",selCourseDateId);
	        req.setAttribute("trainerCount",trainerCount);
	        
	      
	        }
	        else if(checkForFunctionality.equalsIgnoreCase("Y"))
	        {
	        	/*Important code flow for  mapping*/
	         TrainerLearnerMapping[] trainerMannualLOC = sceManager.getTrainerLocForMannual(product,eventName);
	          TrainerLearnerMapping[] trainerMannual = sceManager.getTrainerForMannual(product,eventName);
	         TrainerLearnerMapping[] learnerMannual = sceManager.getLearnerForMannual(courseStartDate,eventName);
	        TrainerLearnerMapping[] trainerLearner = sceManager.getTrainerLearner(courseStartDate,product,eventName);
	        TrainerLearnerMapping[]  trainerCount=sceManager.getCountOfEvalsPerTrainer(eventName,courseStartDate);
	        TrainerLearnerMapping[] trainerLearnerToCalEvals = sceManager.getevaluationHours(courseStartDate);
	        //(Integer.parseInt(t.getTotalHours())*60)/Integer.parseInt(t.getEvalDuration());event
	        int evalDur=event.getEvalDuration()==null?20:event.getEvalDuration();
	        
	        for(int i=0;i<trainerLearnerToCalEvals.length;i++){
	        	for (int j = 0; j < trainerMannual.length; j++) {
					if(trainerLearnerToCalEvals[i].getTrainerEmail().equalsIgnoreCase(trainerMannual[j].getTrainerEmail()))
						trainerMannual[j].setTrainerCount(new Integer((Integer.parseInt(trainerLearnerToCalEvals[i].getTotalHours())*60)/evalDur));
						break;
				}
	        }
	        getEventCourses();
	        getCourseDates();
	        req.setAttribute("selCoursesID",selCoursesID); 
	        req.setAttribute("selCourseName",product);
	        req.setAttribute("selEventOption",whichEvent);
	        req.setAttribute("trainerLearner",trainerLearner);
	        req.setAttribute("trainerMannualLOC",trainerMannualLOC);
	        req.setAttribute("courseStartDate",courseStartDate);
	        req.setAttribute("selCourseDateId",selCourseDateId);
	        req.setAttribute("trainerMannual",trainerMannual);
	        req.setAttribute("learnerMannual",learnerMannual);
	        req.setAttribute("trainerLearner",trainerLearner);
	        req.setAttribute("trainerCount",trainerCount);
	      
	        
	        int evalPerTrainer1=diff;
	        
	        if(numberOfLearners!=0 && numberOfEval!=0)
	        {
	           /* evalPerTrainer1=((numberOfLearners)/(diff*numberOfEval));
	           evalPerTrainer1=diff*numberOfEval;*/
	           
	            evalPerTrainer1=numberOfEval;
	        }
	         
	        System.out.println("evaluation Per trainer:"+evalPerTrainer1);
	        
	        	Integer e=new Integer(evalPerTrainer1);
		        
		        
		        req.setAttribute("evalPerTrainer",e);
	        
	        
	        
	        }
	     else if(checkForFunctionality.equalsIgnoreCase("D"))
	     {
	        String deleteMapping= req.getParameter("trainerEmail");
	        
	               String[] delMapping = null;
	           delMapping =  deleteMapping.split(";");
	        
	         String doEmpty = "";
	     
	    
	        
	         for(int i=0; i<delMapping.length; i++)
	        {
	            String emailSel =delMapping[i];
	            
	            if(!(emailSel.equalsIgnoreCase(doEmpty)))
	            {
	            	 System.out.println("product:"+product);
	            	 System.out.println("courseStartDate"+courseStartDate);
	            	 
	            	 /*	       Sanjeev delete mapping issue*/ 
	            	 
	            	 /* sceManager.gotoDeleteLTMapping(emailSel); */
         
	                 sceManager.gotoDeleteLTMapping(emailSel,product,courseStartDate); 
	            }
	    
	        }
	        
	        TrainerLearnerMapping[] trainerLearner = sceManager.getTrainerLearner(courseStartDate,product,eventName);
	        getEventCourses();
	        getCourseDates();
	        req.setAttribute("selCoursesID",selCoursesID); 
	        req.setAttribute("selCourseName",product);
	        req.setAttribute("selEventOption",whichEvent);
	        req.setAttribute("trainerLearner",trainerLearner);
	        req.setAttribute("courseStartDate",courseStartDate);
	        req.setAttribute("selCourseDateId",selCourseDateId);
	        req.setAttribute("selCoursesID",selCoursesID);
	      //  req.setAttribute("selCourseName",selCourseName);
	      
	  //      req.setAttribute("trainerLearner",trainerLearner);
	        
	        
	     }
	        else
	        {}
	        
	        }
	        catch(SCEException scee){
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return "failure";
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	            //sceLogger.error(LoggerHelper.getStackTrace(e));
	            return "failure";
	        }
	        return "success";
	    }
	  
	  
	 private int mapTrainerLearnerEST(List trainerEST,int countEST,Learner learner,boolean isMapped,int evalPerTrainer,String eventName,String courseStartDate,String product) throws SQLException, SCEException{
		   if(!isMapped){

		    if(countEST==-2){
		        countEST=0;
		    }
		    if(trainerEST.size()>0){
		    GuestTrainer t=(GuestTrainer)trainerEST.get(0);
		    evalPerTrainer=(Integer.parseInt(t.getTotalHours())*60)/Integer.parseInt(t.getEvalDuration());
		    countEST++;
		    isMapped=true;
		    sceManager.gotosaveTrainerLearnerMap(product,(t.getFname()==null)?"":t.getFname(),learner.getFirstName(),t.getRepEmail(),learner.getEmailAddress(),eventName,learner.getLocation(),t.getRepLocation(),courseStartDate);
		    }
		    return countEST;
		   }
		   return -1;
		  }
	 private int mapTrainerLearnerCST(List trainerCST,int countCST,Learner learner,boolean isMapped,int evalPerTrainer,String eventName,String courseStartDate,String product) throws SQLException, SCEException{
		    if(!isMapped){

		    if(countCST==-2){
		        countCST=0;
		    }
		    if(trainerCST.size()>0){
		    GuestTrainer t=(GuestTrainer)trainerCST.get(0);
		    evalPerTrainer=(Integer.parseInt(t.getTotalHours())*60)/Integer.parseInt(t.getEvalDuration());
		    countCST++;
		    isMapped=true;
		    sceManager.gotosaveTrainerLearnerMap(product,(t.getFname()==null)?"":t.getFname(),learner.getFirstName(),t.getRepEmail(),learner.getEmailAddress(),eventName,learner.getLocation(),t.getRepLocation(),courseStartDate);
		    }
		    return countCST;
		    }
		    return -1;
		  }
		  
	 private int mapTrainerLearnerPST(List trainerPST,int countPST,Learner learner,boolean isMapped,int evalPerTrainer,String eventName,String courseStartDate,String product) throws SQLException, SCEException{
		    if(!isMapped){
		 
		    if(countPST==-2){
		        countPST=0;
		    }
		    if(trainerPST.size()>0){
		    GuestTrainer t=(GuestTrainer)trainerPST.get(0);
			evalPerTrainer=(Integer.parseInt(t.getTotalHours())*60)/Integer.parseInt(t.getEvalDuration());
		    countPST++;
		    isMapped=true;
		    sceManager.gotosaveTrainerLearnerMap(product,(t.getFname()==null)?"":t.getFname(),learner.getFirstName(),t.getRepEmail(),learner.getEmailAddress(),eventName,learner.getLocation(),t.getRepLocation(),courseStartDate);
		    }
		    return countPST;
		    }
		    return -1;
		  }	  
	  
	 
	 private int removeIfMapped(List trainer,HashMap trainerAlreadyMappedMap,int evalPerTrainer,boolean isMapped,int countBasedOnZone)
	 {
		 if(!isMapped && trainer.size()>0)
		 {
			 GuestTrainer checkTrainer=(GuestTrainer)trainer.get(0);
			 evalPerTrainer=(Integer.parseInt(checkTrainer.getTotalHours())*60)/Integer.parseInt(checkTrainer.getEvalDuration());
			 Integer trainerCount=(Integer)trainerAlreadyMappedMap.get((String)checkTrainer.getRepEmail());
			 if(trainerCount!=null)
			 {
				 /* if(countBasedOnZone<=trainerCount.intValue()){
		                countBasedOnZone=trainerCount.intValue();
		            }*/
				 countBasedOnZone=trainerCount.intValue();
				 trainerAlreadyMappedMap.remove(checkTrainer.getRepEmail());
				 if(evalPerTrainer==countBasedOnZone)
				 {
		                trainer.remove(0);
		                countBasedOnZone=0;
		                countBasedOnZone=removeIfMapped(trainer, trainerAlreadyMappedMap, evalPerTrainer, isMapped, countBasedOnZone);
		            }
		            
		        }
		    }
		        return countBasedOnZone;
		    //return countBasedOnZone;
		  } 
	   
	 private int mapTrainerLearnerMST(List trainerMST,int countMST,Learner learner,boolean isMapped,int evalPerTrainer,String eventName,String courseStartDate,String product) throws SQLException, SCEException{
		   // System.out.println("Before remove"+trainerMST.size());
		 if(!isMapped)
		 {
			 if(countMST==-2)
			 {
				 countMST=0;
			 }

			 if(trainerMST.size()>0)
			 {
				 GuestTrainer t=(GuestTrainer)trainerMST.get(0);
				 evalPerTrainer=(Integer.parseInt(t.getTotalHours())*60)/Integer.parseInt(t.getEvalDuration());
				 countMST++;
				 isMapped=true;
				 sceManager.gotosaveTrainerLearnerMap(product,(t.getFname()==null)?"":t.getFname(),learner.getFirstName(),t.getRepEmail(),learner.getEmailAddress(),eventName,learner.getLocation(),t.getRepLocation(),courseStartDate);
			 }
			 return countMST;
		 }
		    return -1;
		  }
	 
	 public String getCourseDates()
	    {
		  req = getServletRequest();	         
		 HttpSession session = req.getSession();    

	   try{    
	        String result = checkLegalConsent(req,session);
	        // System.out.println("*****result*****:"+result);
	        if(result != null && result.equals("success")  ){
	            // System.out.println("*************Forwarding to legalConsent");
	            String forwardToHomePage = "Y";
	            EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
	            return "legalConsent";
	        }else if(result != null && result.equals("exception")){
	            // System.out.println("**********Forwarding to exception");
	            return "failure";
	        }
	        
	        String eventIndex = req.getParameter("hdn1");
	       
	     	String eventName = req.getParameter("hdn4");
	       
	        String prodId =req.getParameter("hdn7");
	       
	        String prodName =req.getParameter("hdn6");
	        String evalStartTime=req.getParameter("evalStartTime");
	         req.setAttribute("evalStartTime",evalStartTime);
	        
	        req.setAttribute("selCoursesID",prodId);
	     
	       if(!eventIndex.equalsIgnoreCase("0")){
	    	   String[] eventDates = sceManager.getCourseDates(eventName,prodName);  
	                String[] eventPCourses = sceManager.getEventCourses(eventName);  
	       //     gotoRetrieveEventMapping();
	            
	         req.setAttribute("selCoursesID",prodId);
	         req.setAttribute("selProductName",prodName);
	             req.setAttribute("eventPDate",eventDates);    
	                req.setAttribute("eventPCourses",eventPCourses);           
	              req.setAttribute("selEventOption",eventIndex);
	       }
	       else
	       {
	          
	            req.setAttribute("eventPCourses",null);
	        //    req.setAttribute("selProductIndex",selProductIndex);
	             req.setAttribute("selEventOption",eventIndex);
	       }
	        
	        
	        
	        
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	            //sceLogger.error(LoggerHelper.getStackTrace(e));
	            return "failure";
	        }
	        return "success";    
    
	    }
	
	 public String saveTrainerLearnerMap(){
		 req = getServletRequest();
	        try{
	         String whichEvent=req.getParameter("hdn1");
	         String whichEventName=req.getParameter("hdn4");        
	         String selCoursesID=req.getParameter("hdn7");
	         String selCourseName=req.getParameter("hdn6");
	         String selCoursesDateID=req.getParameter("hdn5");
	         String selCourseDateName=req.getParameter("hdn2");
	         String selTrainer=req.getParameter("selTrainer");
	         String selLearner=req.getParameter("selLearner");
	         String trainerEmail=req.getParameter("trainerEmail");
	         String learnerEmail=req.getParameter("learnerEmail");
	         String learnerLOC=req.getParameter("lLoc");
	         String trainerLOC=req.getParameter("tLoc");
	         String duplicateCheck = "" ;
	         
	         System.out.println("whichEvent"+whichEvent);
	         System.out.println("whichEventName"+whichEventName);
	         System.out.println("selCoursesID"+selCoursesID);
	         System.out.println("selCourseName"+selCourseName);
	         System.out.println("selCoursesDateID"+selCoursesDateID);
	         System.out.println("selCourseDateName"+selCourseDateName);
	         System.out.println("selTrainer"+selTrainer);
	         System.out.println("selLearner"+selLearner);
	         System.out.println("trainerEmail"+trainerEmail);
	         System.out.println("learnerEmail"+learnerEmail);
	         System.out.println("learnerLOC"+learnerLOC);
	         System.out.println("trainerLOC"+trainerLOC);
	         
	         
	         
	         String evalStartTime=req.getParameter("evalStartTime");
	         req.setAttribute("evalStartTime",evalStartTime);
	     
	         /* ------------- sanjeev begin trainer learner mapping issue------------------*/	
	        /* TrainerLearnerMapping[] check =sceManager.gotoCheck(learnerEmail,whichEventName); */
	         
	         TrainerLearnerMapping[] check =sceManager.gotoCheck(learnerEmail,whichEventName,selCourseName,selCourseDateName); 
	        
	         /* ------------- sanjeev end trainer learner mapping issue------------------*/	 
	         if (check != null && check.length > 0)
	           {
	              duplicateCheck = "P";
	                // System.out.println("_______________Record already existing_______________");
	                req.setAttribute("recordDuplicate",duplicateCheck);
	            }
	         else
	         {
	            
	            duplicateCheck = "S";
	            // System.out.println("___________________New Record Insert_______________");
	            sceManager.gotosaveTrainerLearnerMap(selCourseName,selTrainer,selLearner,trainerEmail,learnerEmail,whichEventName,learnerLOC,trainerLOC,selCourseDateName); 
	            req.setAttribute("recordDuplicate",duplicateCheck);
	         }
	         
	         
	      
	       
	      
	      TrainerLearnerMapping[] trainerLearner = sceManager.getTrainerLearner(selCourseDateName,selCourseName,whichEventName);
	      TrainerLearnerMapping[]  trainerCount=sceManager.getCountOfEvalsPerTrainer(whichEventName,selCourseDateName);
	          getEventCourses();
	          getCourseDates();
	           EventsCreated event=new EventsCreated();
	        event.setEventName(whichEventName);//Change
	        event=sceManager.getEvent(event);
	        String sdate=event.getEventStartDate();
	        String edate=event.getEventEndDate();
	        //// System.out.println("SDate"+sdate);
	        String startDate=sdate.substring(5,7)+"-"+sdate.substring(8,10)+"-"+sdate.substring(0,4);
	        String endDate=edate.substring(5,7)+"-"+edate.substring(8,10)+"-"+edate.substring(0,4);
	        //// System.out.println("SDate"+ssdate);
	        SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yyyy");
	        Date d1=sdf.parse(startDate);
	        Date d2=sdf.parse(endDate);
	        // System.out.println("SDate"+d1);
	        // System.out.println("EDate"+d2);
	        int diff=(int)(d2.getTime()-d1.getTime())/(1000*60*60*24);
	        diff=diff+1;
	        // System.out.println("DIFF"+diff);
	        //// System.out.println("SDate"+sdf.format(date));
	        int numberOfLearners=(event.getNumberOfLearners()==null)?0:event.getNumberOfLearners().intValue();
	        int numberOfEval=(event.getNumberOfEval()==null)?0:event.getNumberOfEval().intValue();
	        int evalPerTrainer=diff;
	        if(numberOfLearners!=0 && numberOfEval!=0)
	        {
	            //evalPerTrainer=((numberOfLearners)/(diff*numberOfEval));
	            evalPerTrainer=diff*numberOfEval;
	        }
	         
	          
	        req.setAttribute("selCoursesID",selCoursesID); 
	        req.setAttribute("selCourseName",selCourseName);
	        req.setAttribute("selEventOption",whichEvent);
	        req.setAttribute("trainerLearner",trainerLearner);
	        req.setAttribute("courseStartDate",selCourseDateName);
	        req.setAttribute("selCourseDateId",selCoursesDateID);
	        req.setAttribute("trainerCount",trainerCount);
	        Integer e=new Integer(evalPerTrainer);
	       req.setAttribute("evalPerTrainer",e);
	   
	   
	        
	        //String[] test=req.getParameterValues("n_chk");
	        }
	        catch(SCEException scee){
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return "failure";
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	            //sceLogger.error(LoggerHelper.getStackTrace(e));
	            return "failure";
	        }
	        return "success";
	    }
	 
	 public String gotoConfirmEmailSent()
	    {
	        
		 req = getServletRequest();	             
		 HttpSession session = req.getSession();
	  //       return new Forward("success");      

	        try
	        {     
	        String result = checkLegalConsent(req,session);
	        // System.out.println("*****result*****:"+result);
	        if(result != null && result.equals("success")  ){
	            // System.out.println("*************Forwarding to legalConsent");
	            String forwardToHomePage = "Y";
	            EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
	            return "legalConsent";
	        }else if(result != null && result.equals("exception")){
	            // System.out.println("**********Forwarding to exception");
	            return "failure";
	        }

	      String mapId=req.getParameter("toSent");
	        String event=req.getParameter("eventName");
	        String productName= req.getParameter("productName");
	   // System.out.println(mapId);
	   // System.out.println(event); 
	      String[] selEmail = null;
	           selEmail =  mapId.split(";");
	     
	         for(int i=0; i<selEmail.length; i++)
	        {
	            String email= selEmail[i];
	   sceManager.gotoConfirmEmailSent(email,event,productName); 
	        }
	         
	        }
	        catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	           // sceLogger.error(LoggerHelper.getStackTrace(e));
	            return "failure";
	        }
	        return "success";   
	           
	    }
	  
	   public String getMappingsBasedOnTrainer(){
		   req = getServletRequest();	
	        HttpSession session=req.getSession();
	        try{
	         String whichEvent=req.getParameter("hdn1");
	         String selCoursesID=req.getParameter("hdn7");         
	         String courseStartDate=req.getParameter("hdn3");
	         String product=req.getParameter("hdn6");
	         String eventName= req.getParameter("hdn4");   
	         String selCourseDateId = req.getParameter("hdn2"); 
	         String sendEmailTo=req.getParameter("sendEmailTo");
	         
	         String evalStartTime=req.getParameter("evalStartTime");
	        
	         req.setAttribute("evalStartTime",evalStartTime);
	         
	         TrainerLearnerMapping[] trainerLearner = sceManager.getTrainerLearner(courseStartDate,product,eventName);
	          getEventCourses();
	          getCourseDates();
	        req.setAttribute("sendEmailTo",sendEmailTo);
	        
	        req.setAttribute("selCoursesID",selCoursesID); 
	        req.setAttribute("selCourseName",product);
	        req.setAttribute("selEventOption",whichEvent);
	        req.setAttribute("trainerLearner",trainerLearner);
	        req.setAttribute("courseStartDate",courseStartDate);
	        req.setAttribute("selCourseDateId",selCourseDateId);
	        req.setAttribute("selCoursesID",selCoursesID);
	        }
	           catch(SCEException scee){
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return "failure";
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	            //sceLogger.error(LoggerHelper.getStackTrace(e));
	            return "failure";
	        }
	        return "success";
	    }
	 
	 
	 private String checkLegalConsent(HttpServletRequest req, HttpSession session) 
	 {

			// System.out.println("Entry in to Helper.checkLegalConsent method...");

			String ntid = "";
			try {
				User user = (User) session.getAttribute("user");
				// System.out.println(" User Object:" + user);
				if (user == null) {
					ntid = req.getHeader("IAMPFIZERUSERPFIZERNTLOGONNAME");
					// System.out.println("Getting ntid from IAM Header ntid:" + ntid);
					String emplid = req.getHeader("IAMPFIZERUSERWORKFORCEID");
					// System.out.println("Getting emplid from IAM Header emplid:"+ emplid);
					String domain = req
							.getHeader("IAMPFIZERUSERPFIZERNTDOMAINNAME");
					// System.out.println("Getting domain from IAM Header domain:"+ domain);
					if (ntid == null || ntid.equals("")) {
						// System.out.println("ntid//" + ntid + "//");
						System.out
								.println("User Object is not available in session.");
					}
					// System.out.println("ntid is ://" + ntid + "//");
				} else {
					// System.out.println("Valid User Object:" + user);
					ntid = user.getNtid();
					// System.out.println("User NTID IS:" + user.getNtid());
					// System.out.println("User emplId IS:" + user.getEmplId());
				}

				UserLegalConsent userLegalConsent = new UserLegalConsent();
				userLegalConsent = sceManager.checkLegalConsentAcceptance(ntid);
				if (userLegalConsent == null) {
					LegalConsentTemplate legalConsentTemplate = new LegalConsentTemplate();
					
					legalConsentTemplate = sceManager.fetchLegalContent();
					// System.out.println("legalConsentTemplate.getContent() - "+ legalConsentTemplate.getContent());
					req.setAttribute("content", legalConsentTemplate.getContent());
					req.setAttribute("forLcid", legalConsentTemplate);
					System.out
							.println(" Exit from Helper.checkLegalConsent method before forwarding to legalConsent.jsp");
					return "success";

				} else {
					System.out
							.println("Exit from Helper.checkLegalConsent method before forwarding to failure");
					return "failure";
				}
			} catch (Exception e) {
				req.setAttribute("errorMsg", "error.sce.unknown");
				// sceLogger.error(LoggerHelper.getStackTrace(e));
				System.out
						.println("Exit from Helper.checkLegalConsent method before forwarding to exception");
				return "exception";
			}

		}
}
