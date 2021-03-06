package com.pfizer.sce.Action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.pfizer.sce.beans.GuestTrainer;
import com.pfizer.sce.beans.LegalConsentTemplate;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.beans.UserLegalConsent;
import com.pfizer.sce.common.SCEConstants;
import com.pfizer.sce.db.SCEControlImpl;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.EvaluationControllerHelper;
import com.pfizer.sce.helper.LegalConsentHelper;


public class EventInvitationAction extends ActionSupport implements
ServletRequestAware{
	
	HttpServletRequest request;
	private static SCEManagerImpl sceManager = new SCEManagerImpl();
	private static LegalConsentHelper legalConsentHelper = new LegalConsentHelper();
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
		
	}
	private HttpServletRequest getServletRequest() {
		// TODO Auto-generated method stub
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
	
	  public String gotoFetchGTs()
	    {
	       return new String("success");        
	    }
	
	  public String gotoConfirmInvitation()
	    {        
		  HttpServletRequest req = getServletRequest(); 
	                HttpSession session = req.getSession();
	  //       return new Forward("success");   
	                               
	                try{      
	            
	        String result = checkLegalConsent(req,session);
	        System.out.println("*****result*****:"+result);
	        if(result != null && result.equals("success")  )
	        {
	            System.out.println("*************Forwarding to legalConsent");
	            String forwardToHomePage = "Y";
	            EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
	            return new String("success"); 
	        }else if(result != null && result.equals("exception")){
	            System.out.println("**********Forwarding to exception");
	            return new String("success"); 
	        }
	        System.out.println("*****before Dates Array*****");
	      String dates[]= req.getParameterValues("selectDateforEvent");
	      //System.out.println(dates[0]+','+dates[1]+','+dates[2]);
	      String selEmail=req.getParameter("emailSel"); 
	      String choice = req.getParameter("choiceSel"); 
	      String selEvent = req.getParameter("eventSel");
	      String product = req.getParameter("productSel");
	      
	      sceManager.gotoConfirmInvitation(selEmail,choice,selEvent,product);    
	      //added if user accepts invite
	      if(choice.equalsIgnoreCase("Y")){
	        String[] products=sceManager.getProductsForEvent(selEvent);
	        if(products!=null){
	            for(int i=0;i<products.length;i++){
	                String updateDate=sceManager.getDateToUpdate(selEvent,products[i],selEmail);
	                sceManager.updateLastEventDate(products[i],selEmail,updateDate);
	            }
	        }
	        
	      sceManager.updateUserAcceptInvite(selEvent,selEmail);
	      }
	        
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	            //sceLogger.error(LoggerHelper.getStackTrace(e));
	            return new String("success"); 
	        }
	        return new String("success");   
	           
	    }
	  
	  public String acceptGTinviteManual()
	    {        
		HttpServletRequest req = getServletRequest();
		HttpSession session = req.getSession();

		try {

			String result = checkLegalConsent(req, session);
			System.out.println("*****result*****:" + result);
			/*
			 * if(result != null && result.equals("success") ) {
			 * System.out.println("*************Forwarding to legalConsent");
			 * String forwardToHomePage = "Y";
			 * EvaluationControllerHelper.setBookMarkURL
			 * (session,req,forwardToHomePage); return new String("success");
			 * }else if(result != null && result.equals("exception")){
			 * System.out.println("**********Forwarding to exception"); return
			 * new String("success"); }
			 */
			System.out.println("*****before Dates Array*****");
			String whichEvent = req.getParameter("event");
			String whichIndex = req.getParameter("index");
			String whichproduct = req.getParameter("product");
			String prodIndex = req.getParameter("product_index");

			req.setAttribute("alreadySelectedEvent", whichIndex);
			req.setAttribute("alreadySelectedEvent_1", whichEvent);
			req.setAttribute("alreadySelectedProduct", prodIndex);

			String dates[] = req.getParameterValues("selectDateforEvent");
			String selEmail = req.getParameter("acceptGTEmailist");
			String choice = req.getParameter("acceptGTEmailistChoice");
			String selEvent = req.getParameter("event");
			String product = req.getParameter("product");

			String[] emails = selEmail.split(";");
			StringBuilder bufferMail = new StringBuilder("");

			for (int i = 0; i < emails.length; i++) {
				if (emails[i].toString() != "" && emails[i].toString() != null) {
					if (i == 0) {
						bufferMail.append("'" + emails[i] + "'");
					} else
						bufferMail.append(',').append("'" + emails[i] + "'");
				}

			}

			System.out.println(bufferMail);

			sceManager.acceptGTinviteManual(bufferMail.toString(), choice,
					selEvent, product);
			// added if user accepts invite

			for (int j = 0; j < emails.length; j++) {
				String email = emails[j];

				if (choice.equalsIgnoreCase("Y")) {
					String[] products = sceManager
							.getProductsForEvent(selEvent);
					if (products != null) {
						for (int i = 0; i < products.length; i++) {
							String updateDate = sceManager.getDateToUpdate(
									selEvent, products[i], email);
							sceManager.updateLastEventDate(products[i], email,
									updateDate);
						}
					}

					sceManager.updateUserAcceptInvite(selEvent, email);
				}
			}
			
			GuestTrainer[] trainers=sceManager.getGTByEvent(whichEvent,whichproduct);
	        req.setAttribute("GTListByProduct",trainers);
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("success");
		}
		
		return new String("success");

	}
	/*
	 * End
	 */
	  
	  
	  
	  /*
	   *Added By Ankit on 7 July
	   */
	  
	  public String pushDateTime() 
	  {
		  System.out.println("Inside Method");
		  HttpServletRequest req = getServletRequest();
		  HttpSession session = req.getSession();
		  String dateTime = req.getParameter("DateTimeArray").toString();
		  String email = req.getParameter("emailSel");
		  String event = req.getParameter("eventSel");
		  String product = req.getParameter("productSel");
		  System.out.println("Array Catched"+dateTime);
		  String[] bufferArray= dateTime.split(",");
		  //********************************************************************
		// added by manish on 2/3/2016 to add total hours in pushDateTime() method

			String timeSplit =null;
			String[] timeSplitArray;
			int totalHours=0;
			List<Integer> hoursList= new ArrayList<Integer>();
			
			try{
			System.out.println("buffer.length"+bufferArray.length);
			
			for(int i=0;i<bufferArray.length-(bufferArray.length/2);i++)
			{
				timeSplit = bufferArray[i+i+1];
				timeSplitArray = timeSplit.split("\\$");
				
				totalHours = timeSplitArray.length;
				
				hoursList.add(totalHours);
				
				
				Iterator<Integer> itr= hoursList.iterator();
				
				System.out.println("Displaying Total hour List Elements,");

				while(itr.hasNext())
				{
					  System.out.println(itr.next());

				}
				
				System.out.println("finalHourList1:"+hoursList);
			}
			
			

			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		  //********************************************************************
		  
		  
		  
		  String[][] dateTimeArray= new String[bufferArray.length/2][2];
		  try 
		  {
			  
			  String MapId= sceManager.getMapidForTrainer(email, product, event);
			  
			  System.out.println("MapId is "+MapId);
			  
		for (int i = 0,j=0; i < bufferArray.length&&j<dateTimeArray.length; i++) 
			  {
				String string = bufferArray[i];
				if (i%2==0) 
				{
					dateTimeArray[j][0]=string;
				} else 
				{
					dateTimeArray[j][1]=string;
					j++;
				}
				
			  }
		
			System.out.println("Inserting Records");
			sceManager.saveDateTimeSlots(dateTimeArray, MapId, hoursList,email, event, product);
			System.out.println("records inserted");
			System.out.println("Accepting Invite");
			sceManager.gotoConfirmInvitation(email,"Y", event, product);
			System.out.println("Invite Accepted");

			req.setAttribute("Event",event);
			req.setAttribute("Product", product);
			req.setAttribute("Email", email);

			  return new String("success");
			
			  
		  } catch (Exception e) 
		  { 
			System.out.println("Exception occured: "+e);
			System.out.println(e.getCause());
			req.setAttribute("errorMsg","error.sce.unknown");
	        return new String("failure");
		  }
	}
	  
	  
	  /*
	   * End
	   */
	  public String acceptRejectInvite() throws IOException{
		  
		 	  
		HttpServletRequest req = getServletRequest();
		/*String code=request.getParameter("authcode");*/
		HttpSession session = req.getSession();
		String queryString = null;
		
		
		// authenticating invite
		String ntid = null;
		String domain = null;
		String emplid = null;
		SCEControlImpl sceControl = new SCEControlImpl();
		HttpServletResponse response = ServletActionContext.getResponse();
		String userDetails = sceControl.getAuthenticatedUserID(request,
				response);
		System.out.println("Oauth details  : " + userDetails);

		if (!userDetails.startsWith("null") && userDetails.length() > 0) {
			String[] userDetailsArray = userDetails.split(",");
			ntid = userDetailsArray[0];
			domain = userDetailsArray[1];
			emplid = userDetailsArray[2];
		} else {
			// shindo added session expired
			response.sendRedirect(request.getContextPath()
					+ "/sessionExpired.jsp");

		}

		String userid = getAuthUserId(domain, ntid);

		User user = new User();

		if (userid.trim().length() > 0) {
			System.out.println("The ntid is " + ntid);

			System.out.println("The domain is " + domain);

			System.out.println("The empid is " + emplid);

			System.out.println("The userid is " + userid);

			if (ntid.length() > 0) {
				System.out.println("Came inside IF");
				/*
				 * user = sceCtl.getUserByNTIdAndDomain(ntid,domain); 2020
				 * Q2:domain required logic condition removed
				 */
				user = sceControl.getUserByNTIdAndDomain(ntid);
				
				/*shindo added for acceptinvite oauth*/
				if (user.getStatus().equals("INACTIVE")){
					SCEConstants.INVITE_FLAG="N";
					response.sendRedirect("Unauthorized.jsp");
				}
				
				
				/* Added for CSO requirements */
				if (user != null) {
					String sType = "";
					sType = sceControl.getSalesPositionTypeCd(ntid);
					if (sType != null) {
						user.setSalesPositionTypeCd(sType);
					}
					System.out.println("Sales Position Type Code is "
							+ user.getSalesPositionTypeCd());
					String sceVisible = sceControl.getSceVisibility(user
							.getSalesPositionTypeCd());
					System.out.println("SCE Visibility is " + sceVisible);
					if (sceVisible == null || sceVisible.equalsIgnoreCase("Y")
							|| sceVisible.equals("")) {
						user.setSceVisibility("Y");
					} else {
						user.setSceVisibility("N");
					}
				}
				/* End of Addition */
				System.out.println("Got User");
			} else {
				user = null;
			}

			System.out.println("The user is " + user);

			if (user == null || !user.isActive()) {

				/*
				 * ------------------------DEXTER's LAB EXPERIMENT PART ONE
				 * STARTS ----------------------
				 */

				String url = request.getRequestURL().toString();

				if (url != null) {

					System.out.println("the url is:::" + url);
					// if(url.equals("http://sce.pfizer.com/SCEWeb/PrintBlankForm")){
					if (url.equals("http://localhost:7001/SCEWeb/printBlankFormLimited.jsp")) {
						response.sendRedirect("printBlankFormLimited.jsp");
					} else {

					}
				} else {// url==null

					response.sendRedirect("Unauthorized.jsp");
				}

				/*
				 * ------------------------DEXTER's LAB EXPERIMENT PART ONE
				 * ENDS----------------------
				 */

				// out.print("no user found from DB");
				// shindo oauth redirected after session expired
				/*response.sendRedirect(request.getContextPath()
						+ "/sessionExpired.jsp");*/

				response.sendRedirect("Unauthorized.jsp");
			} else {
				// out.print(" user found from DB. set session with userid " +
				// userid);
				user.setEmplId(emplid);
				session.setAttribute("user", user);
				session.setAttribute("UserID", userid.trim().toLowerCase());

				/*
				 * ------------------------DEXTER's LAB EXPERIMENT PART TWO
				 * STARTS----------------------
				 */

				String url = request.getRequestURL().toString();
				if (url != null) {

					System.out.println("the url is:::" + url);
					if (url.equals("http://localhost:7001/SCEWeb/PrintBlankForm")) {
						response.sendRedirect("PrintBlankForm.jsp");
					} // End if(url

					/*-----------------------------------DEXTER's LAB EXPERIMENT PART TWO ENDS----------------------      */
				}
			}
		} else {
			// response.sendRedirect("..\\sessionExpired.jsp");
			String str = request.getRequestURL().toString();
			System.out.println("url is " + str);
			response.sendRedirect(request.getContextPath()
					+ "/sessionExpired.jsp");
		}

		/*
		 * String eventN = req.getParameter("whichEvent"); String startDate =
		 * req.getParameter("strDate"); String productName =
		 * req.getParameter("productName"); String gtEmail =
		 * req.getParameter("GtEmail");
		 */
		// shindo retrieved invite attributes from session 
		// session OAUTH release

		String eventN = (String) session.getAttribute("whichEvent");
		String startDate = (String) session.getAttribute("strDate");
		String productName = (String) session.getAttribute("productName");
		String gtEmail = (String) session.getAttribute("GtEmail");

		System.out.println(eventN + ".." + startDate + ".." + ".."
				+ productName + ".." + gtEmail);

		String bookMarkURL = null;
		String redirectTo = null;
		if (gtEmail != null && gtEmail != "")
			queryString = "whichEvent=" + eventN + "&productName="
					+ productName + "&" + "strDate=" + startDate + "&GtEmail"
					+ gtEmail;
		else
			queryString = "whichEvent=" + eventN + "&productName="
					+ productName + "&" + "strDate=" + startDate;
		try {
			String result = checkLegalConsent(req, session);
			System.out.println("*****result*****:" + result);
			if (result != null && result.equals("success")) {
				System.out.println("*************Forwarding to legalConsent");
				String forwardToHomePage = "N";
				EvaluationControllerHelper.setBookMarkURL(session, req,
						forwardToHomePage);
				if (queryString != null) {
					bookMarkURL = (String) session.getAttribute("bookMarkURL");
					redirectTo = bookMarkURL + "?" + queryString;
					session.setAttribute("bookMarkURL", redirectTo);
				}
				return new String("legalConsent");
			} else if (result != null && result.equals("exception")) {
				System.out.println("**********Forwarding to exception");
				return new String("failure");
			}
			System.out.println("EventName set As: " + eventN);
			System.out.println("StartDate set As: " + startDate);
			System.out.println("Product set As: " + productName);

			req.setAttribute("eventName", eventN);
			req.setAttribute("productName", productName);
			req.setAttribute("startDate", startDate);
			req.setAttribute("startDate", startDate);
			if (gtEmail != null && gtEmail != "") {
				req.setAttribute("GtEmail", gtEmail);
			}

		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		SCEConstants.INVITE_FLAG = "N";
		session.removeAttribute("whichEvent");
		session.removeAttribute("productName");
		session.removeAttribute("strDate");
		session.removeAttribute("GtEmail");
		return new String("success");

	}
	  
	  
	    public String gotoviewGTByEvent(){
	        HttpServletRequest req = getServletRequest();
	        try{
	        String whichEvent=req.getParameter("event");
	        String whichIndex=req.getParameter("index");
	        String whichproduct=req.getParameter("product");
	        String prodIndex= req.getParameter("product_index");
	        String whichBusinessUnit = req.getParameter("bu_id");
			String whichBUIndex = req.getParameter("bu_index");
			String[] eventNameFetch = sceManager.getEventNameByBU(whichBusinessUnit);
			String[] eventProducts=sceManager.getEventProducts(whichEvent);
	        System.out.println("Event: "+whichEvent+" Index: "+prodIndex+" product: "+whichproduct+" request_object: ");
	        GuestTrainer[] trainers=sceManager.getGTByEvent(whichEvent,whichproduct);
	        req.setAttribute("eventNameFetch", eventNameFetch);
	        req.setAttribute("eventProducts",eventProducts);
	        req.setAttribute("GTListByProduct",trainers);
	        req.setAttribute("alreadySelectedEvent",whichIndex);
	        req.setAttribute("alreadySelectedEvent_1",whichEvent);
	        req.setAttribute("alreadySelectedProduct",prodIndex);
	        req.setAttribute("alreadySelectedBUIndex",whichBUIndex);
			req.setAttribute("alreadySelectedBU",whichBusinessUnit);
	        }
	        catch(SCEException scee){
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return new String("failure");
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	            return new String("failure");
	        }
	        return new String("success");
	        
	    }
	    //added by muzees for PBG and UpJOHN to fetch events based on business unit 2019
	    public String getBUEvents() {
	    	try{
			HttpServletRequest req = getRequest();
			String whichBusinessUnit = req.getParameter("bu_id");
			String whichBUIndex = req.getParameter("bu_index");
			String[] eventNameFetch = sceManager.getEventNameByBU(whichBusinessUnit);
			req.setAttribute("eventNameFetch", eventNameFetch);
			req.setAttribute("alreadySelectedBUIndex",whichBUIndex);
			req.setAttribute("alreadySelectedBU",whichBusinessUnit);
	    	} catch (Exception e) {
				e.printStackTrace();
			}
			return new String("success");
		}//end of MUZEES
	    /**Added By ankit on 4 june**/
	  //The below method has been updated by muzees for PGB and UpJOHN 
	    public String getEventProduct(){
	        HttpServletRequest req = getServletRequest();
	        try{
	        String whichEvent=req.getParameter("event");
	        String whichIndex=req.getParameter("index");
	        String whichBusinessUnit = req.getParameter("bu_id");
			String whichBUIndex = req.getParameter("bu_index");
			String[] eventNameFetch= sceManager.getEventNameByBU(whichBusinessUnit);
	        String[] eventProducts=sceManager.getEventProducts(whichEvent);
	        req.setAttribute("eventProducts",eventProducts);
	        req.setAttribute("eventNameFetch",eventNameFetch);
	        req.setAttribute("alreadySelectedEvent",whichIndex);
	        req.setAttribute("alreadySelectedBUIndex",whichBUIndex);
			req.setAttribute("alreadySelectedBU",whichBusinessUnit);
			req.setAttribute("alreadySelectedEvent_1",whichEvent);
	        }
	        catch(SCEException scee){
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return new String("failure");
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	            return new String("failure");
	        }
	        return new String("success");
	    }
	  
	    /**end by ankit**/
	    /**
	     * @jpf:action
	     * @jpf:forward name="success" path="sendEmailConfirm.jsp"
	     * @jpf:forward name="failure" path="/errorPage.jsp"
	     * @jpf:forward name="legalConsent" path="legalConsent.jsp"
	     */
	    public String gotoConfirmEmailSent()
	    {
	        
	               HttpServletRequest req = getServletRequest();
	                 HttpSession session = req.getSession();
   
	        
	        try{
	       
	        String result = legalConsentHelper.checkLegalConsent(req,session);
	      //  // System.out.println("*****result*****:"+result);
	        result="failure";
	        if(result != null && result.equals("success")  ){
	        //    // System.out.println("*************Forwarding to legalConsent");
	            String forwardToHomePage = "Y";
	            EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
	            return new String("legalConsent");
	        }else if(result != null && result.equals("exception")){
	          //  // System.out.println("**********Forwarding to exception");
	            return new String("failure");
	        }
	      /*  
	        sanjeev begin added for landing the page on after email confirmation sent
	         String whichEvent = req.getParameter("event");
			   String whichIndex = req.getParameter("index");
			   String whichproduct = req.getParameter("product");
			   String prodIndex = req.getParameter("product_index");
			   
			   req.setAttribute("alreadySelectedEvent", whichIndex);
			   req.setAttribute("alreadySelectedEvent_1", whichEvent);
			   req.setAttribute("alreadySelectedProduct", prodIndex);  
			   sanjeev end added for landing the page on after email confirmation sent*/
	        
	      String mapId=req.getParameter("toSent");
	        String event=req.getParameter("eventSel");
	        String productName=req.getParameter("productName");
	   
	      
	      String[] selEmail = null;
	           selEmail =  mapId.split(";");
	     
	         for(int i=0; i<selEmail.length; i++)
	        {
	            String email= selEmail[i];
	     
	   sceManager.gotoConfirmEmailSent(email,event,productName);    /*sanjeev send product*/
	   
	   
	/*   sanjeev begin added for landing the page on after email confirmation sent
	 GuestTrainer[] trainers=sceManager.getGTByEvent(whichEvent,whichproduct);
	         req.setAttribute("GTListByProduct",trainers);
 
	         sanjeev end added for landing the page on after email confirmation sent*/
	   
	        }
	          
	        
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	           // sceLogger.error(LoggerHelper.getStackTrace(e));
	            return new String("failure");
	        }
	        return new String("success");   
	           
	    }
	
	    private String checkLegalConsent(HttpServletRequest req, HttpSession session) 
		 {

				//System.out.println("Entry in to Helper.checkLegalConsent method...");

				String ntid = "";
				try {
					User user = (User) session.getAttribute("user");
					//System.out.println(" User Object:" + user);
					if (user == null) {
						ntid = req.getHeader("IAMPFIZERUSERPFIZERNTLOGONNAME");
						//System.out.println("Getting ntid from IAM Header ntid:" + ntid);
						String emplid = req.getHeader("IAMPFIZERUSERWORKFORCEID");
						//System.out.println("Getting emplid from IAM Header emplid:"+ emplid);
						String domain = req
								.getHeader("IAMPFIZERUSERPFIZERNTDOMAINNAME");
						//System.out.println("Getting domain from IAM Header domain:"+ domain);
						if (ntid == null || ntid.equals("")) {
							//System.out.println("ntid//" + ntid + "//");
							//System.out.println("User Object is not available in session.");
						}
						//System.out.println("ntid is ://" + ntid + "//");
					} else {
						//System.out.println("Valid User Object:" + user);
						ntid = user.getNtid();
						//System.out.println("User NTID IS:" + user.getNtid());
						//System.out.println("User emplId IS:" + user.getEmplId());
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
	    
	    public String getAuthUserId(String domain, String ntid) {

			String result = "";

			try {
				if (ntid.length() > 0) {
					//domain.toUpperCase();
					ntid.toUpperCase();
					if (domain.length() > 0) {
						domain.toUpperCase();
						result = domain + "\\" + ntid;
					} else
						result = ntid;
				} else {
					result = "";
				}
			} catch (Exception ex) {
				result = "";

			}
			return result;
		}

}
