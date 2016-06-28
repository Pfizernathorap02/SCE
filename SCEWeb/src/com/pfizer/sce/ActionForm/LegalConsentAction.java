package com.pfizer.sce.ActionForm;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
//import com.pfizer.sce.ActionForm.LegalConsentTemplateForm;
import com.pfizer.sce.beans.LegalConsentTemplate;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.beans.UserLegalConsent;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.EvaluationControllerHelper;
import com.pfizer.sce.helper.LegalConsentHelper;

public class LegalConsentAction extends ActionSupport implements
ServletRequestAware {
	
	HttpServletRequest request;
	private static LegalConsentHelper legalConsentHelper = new LegalConsentHelper();
	private static SCEManagerImpl sceManager = new SCEManagerImpl();
	LegalConsentTemplate legalConsentTemplate = null;
    public String acceptLegalConsent() {  	
    	
    	
    	HttpServletRequest req = getServletRequest();
           
        UserLegalConsent userLegalConsent=new UserLegalConsent();
    
        String url=req.getRequestURL().toString();
       
        String acceptedBy="";
        int acceptedId=0;
        String ntDomain="";
        String sceRole="";
        User user=null;
        try{
            HttpSession session = req.getSession();
            if(req.getParameter("accepted_flag") == null){
                String result = legalConsentHelper.checkLegalConsent(req,session);
               // // System.out.println("*****result*****:"+result);
                if(result != null && result.equals("success")  ){
                //    // System.out.println("*************Stringing to legalConsent from acceptLegalConsent");
                    String StringToHomePage = "Y";
                    EvaluationControllerHelper.setBookMarkURL(session,req,StringToHomePage);
                    return new String("legalConsent");
                }else if(result != null && result.equals("exception")){
                  //  // System.out.println("**********Stringing to exception from acceptLegalConsent");
                    return new String("failure");
                }else{
                    return new String("success");
                }    
            }
           user= (User)getSession().getAttribute("user");
           /*Code to differentiate between localhost and other environments*/
           if(user!=null){
             acceptedBy=user.getNtid();
             ntDomain=user.getNtdomain();
             sceRole=user.getUserGroup();
           }
           else{
             acceptedBy="bakhsr";
             ntDomain="amer";
             sceRole="SCE_Administrators";
           }
           
           acceptedId=sceManager.fetchMaxAcceptedId();
           acceptedId=acceptedId+1;
           
        
           userLegalConsent.setAcceptedId(acceptedId);
           
           userLegalConsent.setAcceptedBy(acceptedBy);
           userLegalConsent.setNtDomain(ntDomain);
           userLegalConsent.setSceRole(sceRole);
           Integer temp_lcId =  new Integer(req.getParameter("LC_ID"));
           int lcId= temp_lcId.intValue();
           
           userLegalConsent.setLcId(lcId);
           sceManager.acceptLegalConsent(userLegalConsent);
        }catch(SCEException scee){
            req.setAttribute("errorMsg",scee.getErrorCode());
            return new String("failure");
        }catch(Exception e){
            req.setAttribute("errorMsg","error.sce.unknown");
            //sceLogger.error(LoggerHelper.getStackTrace(e));
            return new String("failure");
        }
        
        String bookMarkURL = null;
        if(getSession().getAttribute("bookMarkURL") != null){
            bookMarkURL = (String)getSession().getAttribute("bookMarkURL");
            getSession().removeAttribute("bookMarkURL");
            try{
                URL StringURL = new URL(bookMarkURL);
              //  // System.out.println("StringURL::::::"+StringURL);
                return new String(""+StringURL);
            }catch(MalformedURLException me){
                req.setAttribute("errorMsg","BookMarked URL is Malformed");
                //sceLogger.error(LoggerHelper.getStackTrace(me));
                return new String("failure");
            }
            
        }
      return new String("success");
       
    }

    @Override
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
	
	public int lcId;
	public String content;
	public int selLcId;
	public String newContent;
	public String pageName;
	public int lc_id;
	
	
	 public int getLc_id() {
		return lc_id;
	}

	public void setLc_id(int lc_id) {
		this.lc_id = lc_id;
	}

	public String getNewContent() {
		return newContent;
	}

	public void setNewContent(String newContent) {
		this.newContent = newContent;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public int getSelLcId() {
		return selLcId;
	}

	public void setSelLcId(int selLcId) {
		this.selLcId = selLcId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getLcId() {
		return lcId;
	}

	public void setLcId(int lcId) {
		this.lcId = lcId;
	}

	/**
     * @jpf:action
     * @jpf:forward name="success" path="legalTemplateMaintenance.jsp"
     * @jpf:forward name="failure" path="/errorPage.jsp"
     * @jpf:forward name="legalConsent" path="legalConsent.jsp"
     */
	 public String gotoLegalTemplate(){
	       HttpServletRequest req = getServletRequest();
	       
	       try{
	        HttpSession session = req.getSession();
	          String result = checkLegalConsent(req,session);
	        //  // System.out.println("*****result*****:"+result);
	          if(result != null && result.equals("success")  ){
	         //   // System.out.println("*************Forwarding to legalConsent");
	            String forwardToHomePage = "N";
	            EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
	            return new String("legalConsent");
	          }else if(result != null && result.equals("exception")){
	           //  // System.out.println("**********Forwarding to exception");
	             return new String("failure");
	          }
	       LegalConsentTemplate legalConsentTemplate=null;
	       LegalConsentTemplate[]  legalTemplates =sceManager.getAllLegalTemplates();
	          legalConsentTemplate=sceManager.getPublishedVersion();
	          
	        req. setAttribute("legalTemplates",legalTemplates); 
	        req.setAttribute("publishedVersion",legalConsentTemplate);
	        
	      //  // System.out.println("published version:"+legalConsentTemplate.getVersion());
	      
	        }catch(SCEException scee){
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return new String("failure");
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	          //  sceLogger.error(LoggerHelper.getStackTrace(e));
	            return new String("failure");
	        }
	        return new String("success");
	    }
	
	 public String checkLegalConsent(HttpServletRequest req,HttpSession session){
	        
	        
	      //  // System.out.println("Entry in to Helper.checkLegalConsent method..."); 
	       
	        String ntid = "";
	         try{
	             User user= (User)session.getAttribute("user");
	             // System.out.println(" User Object:"+user); 
	             if(user==null){
	                 ntid = req.getHeader("IAMPFIZERUSERPFIZERNTLOGONNAME");
	             //    // System.out.println("Getting ntid from IAM Header ntid:"+ntid);
	                 String emplid = req.getHeader("IAMPFIZERUSERWORKFORCEID");
	               //  // System.out.println("Getting emplid from IAM Header emplid:"+emplid);
	                 String domain= req.getHeader("IAMPFIZERUSERPFIZERNTDOMAINNAME");
	               //  // System.out.println("Getting domain from IAM Header domain:"+domain);
	                 if(ntid == null || ntid.equals("")){
	                   //  // System.out.println("ntid//"+ntid+"//");
	                   //  // System.out.println("User Object is not available in session."); 
	                 }
	              //   // System.out.println("ntid is ://"+ntid+"//");
	             }else{
	              //   // System.out.println("Valid User Object:"+user); 
	                 ntid= user.getNtid();
	              //   // System.out.println("User NTID IS:"+user.getNtid());
	             //    // System.out.println("User emplId IS:"+user.getEmplId());
	             }
	       
	             UserLegalConsent userLegalConsent=new UserLegalConsent();
	             userLegalConsent=sceManager.checkLegalConsentAcceptance(ntid);
	             if(userLegalConsent==null){
	                  LegalConsentTemplate legalConsentTemplate=new LegalConsentTemplate();
	                  int nlcId=0;
	                  legalConsentTemplate=sceManager.fetchLegalContent();
	                //  // System.out.println("legalConsentTemplate.getContent() - "+ legalConsentTemplate.getContent());
	                  req.setAttribute("content",legalConsentTemplate.getContent());
	                  req.setAttribute("forLcid",legalConsentTemplate);  
	                //  // System.out.println(" Exit from Helper.checkLegalConsent method before forwarding to legalConsent.jsp");                            
	                  return "success";
	                                                
	             }else{ 
	                //  // System.out.println("Exit from Helper.checkLegalConsent method before forwarding to failure");
	                  return "failure";
	             }   
	         } catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	           // sceLogger.error(LoggerHelper.getStackTrace(e));
	          //  // System.out.println("Exit from Helper.checkLegalConsent method before forwarding to exception");
	            return  "exception";
	        }    
	         
	      
	    }
	
	 public String publishLegalTemplate(){
	        HttpServletRequest req = getServletRequest();
	        try{
	        User user= (User)getSession().getAttribute("user");
	      
	            int lcId = new Integer(req.getParameter("selLcId")).intValue(); 
	      
	            LegalConsentTemplate legalConsentTemplate=new LegalConsentTemplate();
	            legalConsentTemplate.setLcId(lcId );
	        
	            legalConsentTemplate.setPublishFlag("Y");
	            legalConsentTemplate.setPublishedBy(user.getNtid());
	            
	            
	           sceManager.unpublishOlderVersion();
	           sceManager.publishLegalTemplate(legalConsentTemplate);
	            
	          
	           legalConsentTemplate=sceManager.getPublishedVersion();
	                req.setAttribute("publishedVersion",legalConsentTemplate);
	                legalConsentTemplate=null;
	            LegalConsentTemplate[]  legalTemplates =sceManager.getAllLegalTemplates();
	           
	        req.setAttribute("legalTemplates",legalTemplates); 
	      }catch(SCEException scee){
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return new String("failure");
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	           // sceLogger.error(LoggerHelper.getStackTrace(e));
	            return new String("failure");
	        }
	        
	        return new String("success");
	    }
	 
	    public String editLegalTemplate()
	    {
	     //   // System.out.println("Inside editLegalTemplate()");
	        HttpServletRequest req = getServletRequest();
	        try{
	        int lcId = 0;
	        String draftVersion="";
	      
	         if(req.getParameter("selLcId")!=null){
	          lcId = new Integer(req.getParameter("selLcId")).intValue();
	       //   // System.out.println("lcId-->"+lcId);  
	         }
	            
	           legalConsentTemplate=sceManager.getPublishedVersion();
	           int publishedVersion=legalConsentTemplate.getVersion();
	           int maxVersion=sceManager.getMaxVersion();
	           legalConsentTemplate =sceManager.getLegalTemplateById(lcId);
	           legalConsentTemplate.setLcId(lcId);
	          
	              //   // System.out.println("version iss:::"+ legalConsentTemplate.getVersion());
	            
	                   if(publishedVersion!=maxVersion&&maxVersion!=legalConsentTemplate.getVersion()){
	                  draftVersion="Y";
	                 }
	              //   // System.out.println("draft version:::"+ draftVersion);
	           
	                  req.setAttribute("draftVersion",draftVersion);
	                  req.setAttribute("legalConsentTemplate", legalConsentTemplate);
	                  req.setAttribute("content",legalConsentTemplate.getContent());
	                  	                  
	               //   // System.out.println("Executed method editLegalTemplate"); 
	        }catch(SCEException scee){
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return new String("failure");
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	           // sceLogger.error(LoggerHelper.getStackTrace(e));
	            return new String("failure");
	        }
	                  return new String("success");
	                        
	    }
	    
	     
	    public String saveLegalTemplate(){
	        HttpServletRequest req = getServletRequest();
	        try{
	        User user= (User)getSession().getAttribute("user");
	
	        int lcId= new Integer(req.getParameter("lc_id")).intValue();
	     //   // System.out.println("newwww lcid"+lcId);
	            LegalConsentTemplate legalConsentTemplate=new LegalConsentTemplate();
	            LegalConsentTemplate legalConsentTemplate2=new LegalConsentTemplate();
	            
	       legalConsentTemplate =sceManager.getLegalTemplateById(lcId);
	       String content=req.getParameter("newContent");
	       
	     //  // System.out.println("Content: "+content);
	       
	       legalConsentTemplate.setContent(content);
	       
	    //   // System.out.println("Set content into legalConsentTemplate: "+legalConsentTemplate.getContent());
	       legalConsentTemplate.setModifiedBy(user.getNtid());
	  
	         
	        int version=legalConsentTemplate.getVersion();
	        int maxVersion=sceManager.getMaxVersion();
	        int maxLcId=sceManager.getMaxLcId();
	        legalConsentTemplate2=sceManager.getPublishedVersion();
	        int publishedVersion=legalConsentTemplate2.getVersion();
	        if(version<=publishedVersion&&publishedVersion==maxVersion){                              
	             version=maxVersion+1; 
	             legalConsentTemplate.setVersion(version);
	             lcId=maxLcId+1;
	             legalConsentTemplate.setLcId(lcId);
	             sceManager.createNewVersion(legalConsentTemplate);
	        }else{
	             legalConsentTemplate.setVersion(maxVersion);
	      //       // System.out.println("in over write version");
	             sceManager.overWriteVersion(legalConsentTemplate);
	        //     // System.out.println("changes done in over write version");
	       
	     }
	                           
	       
	     //  --------------to display d templates---------
	       
	        legalConsentTemplate=null;
	            LegalConsentTemplate[]  legalTemplates =sceManager.getAllLegalTemplates();
	           legalConsentTemplate=sceManager.getPublishedVersion();
	     
	        req.setAttribute("legalTemplates",legalTemplates); 
	        req.setAttribute("publishedVersion",legalConsentTemplate);
	        }catch(SCEException scee){
	            req.setAttribute("errorMsg",scee.getErrorCode());
	            return new String("failure");
	        }catch(Exception e){
	            req.setAttribute("errorMsg","error.sce.unknown");
	           // sceLogger.error(LoggerHelper.getStackTrace(e));
	            return new String("failure");
	        }
	        return new String("success");
	        
	    }	    
	


}
