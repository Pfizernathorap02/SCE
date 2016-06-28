package com.pfizer.sce.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.sce.ActionForm.AddGuestTrainerForm;
import com.pfizer.sce.beans.GuestTrainer;
import com.pfizer.sce.beans.LegalConsentTemplate;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.beans.UserLegalConsent;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.EvaluationControllerHelper;

public class FieldFacultyMaintainanceAction extends ActionSupport implements ServletRequestAware,ModelDriven
{
	private static SCEManagerImpl sceManager = new SCEManagerImpl();
	HttpServletRequest request;
	
     
    private AddGuestTrainerForm addGuestTrainerForm=new AddGuestTrainerForm();
    
	

	public AddGuestTrainerForm getAddGuestTrainerForm() {
		return addGuestTrainerForm;
	}

	public void setAddGuestTrainerForm(AddGuestTrainerForm addGuestTrainerForm) {
		this.addGuestTrainerForm = addGuestTrainerForm;
	}

	HashMap<Object, Object> productList=new HashMap<Object, Object>();
	HashMap<Object, Object> locationList=new HashMap<Object, Object>();
    
	  public HashMap<Object, Object> getLocationList() {
			return locationList;
		}

		public void setLocationList(HashMap<Object, Object> locationList) {
			this.locationList = locationList;
		}
	public HashMap<Object, Object> getProductList() {
		return productList;
	}

	public void setProductList(HashMap<Object, Object> productList) {
		this.productList = productList;
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

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return getAddGuestTrainerForm();
	}
	
	public String gototFieldFacultyMaintenance(){
		HttpServletRequest req = getServletRequest();
      HttpSession session=req.getSession();
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
        try{
      String[] products=getProductForEvent();
      String[] location=sceManager.getAllHomeStatesArr();
      session.setAttribute("locationArr",location);
      session.setAttribute("products",products);
      session.removeAttribute("gtByProduct");
        }
        catch(Exception e){
        }
      
      return "success";
  }
 public String[] getProductForEvent() throws SCEException
	 {
	        return sceManager.getProductForEvent();
	   }
 public String gotoViewGTList(){
	 
	 HttpServletRequest req = getServletRequest();
     HttpSession session=req.getSession();
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
       List filteredList=null;
     try{
         String lastName=req.getParameter("hdnLastNameFilter").trim();
         String location=req.getParameter("hdnLocFilter").trim();
         String role=req.getParameter("hdnRoleFilter").trim();
         String toggle=req.getParameter("hdnfilter");
          String prevProduct=req.getParameter("hdnPrevProduct");
          String currentProduct=req.getParameter("uploadGTProduct");
          GuestTrainer[] trainer=(GuestTrainer[])session.getAttribute("gtByProduct");
             
             if(!currentProduct.equalsIgnoreCase(prevProduct)){
                     trainer=sceManager.getAllGTForProduct(currentProduct);
              /*   trainer=sceManager.getGTByProduct(currentProduct);
                 if(trainer!=null && trainer.length<=0){
                 trainer=sceManager.getGTFromAtlasByProduct(currentProduct);
             }*/
             }
   
 
     GuestTrainer objTrainer=new GuestTrainer();
         
         //creating list  
         if((lastName!=null && lastName.length()>0) ||(role!=null && role.length()>0) ||(location!=null && location.length()>0)){
             filteredList=new ArrayList();
       for(int i=0;i<trainer.length;i++){
         objTrainer=trainer[i];
         filteredList.add(objTrainer);
         }
         // System.out.println("List size"+filteredList.size());
         }
     
     
     //GuestTrainer[] filTrainer=new GuestTrainer[trainer.length];
     
    /* if(lastName!=null && lastName.length()>0){
     for(int i=0;i<trainer.length;i++){
         objTrainer=trainer[i];
         if(objTrainer.getRepName().equalsIgnoreCase(lastName)){
             filTrainer[k]=objTrainer;
             k++;
         }
     }
     }*/
   if(lastName!=null && lastName.length()>0){
     for(int i=0;i<filteredList.size();i++){
         objTrainer=(GuestTrainer)filteredList.get(i);
         if(objTrainer.getLname()!=null &&!objTrainer.getLname().equalsIgnoreCase(lastName)){
             filteredList.remove(i);
             i--;
         }
     }
     // System.out.println("List size"+filteredList.size());
     }
     
   
       if(location!=null && location.length()>0){
     for(int i=0;i<filteredList.size();i++){
         objTrainer=(GuestTrainer)filteredList.get(i);
         if(objTrainer.getRepLocation()!=null&&!objTrainer.getRepLocation().equalsIgnoreCase(location)){
             filteredList.remove(i);
             i--;
         }
     }
     // System.out.println("List size"+filteredList.size());
     }
   
     if(role!=null && role.length()>0){
     for(int i=0;i<filteredList.size();i++){
         objTrainer=(GuestTrainer)filteredList.get(i);
         
      if(objTrainer.getRepRole()!=null){
         String roleUp = role.toUpperCase();
       int roleFilter =  (objTrainer.getRepRole().toUpperCase()).indexOf(roleUp);
         if(roleFilter==-1 )
         {
             filteredList.remove(i);
             i--;
         }
      }
     }
     // System.out.println("List size"+filteredList.size());
     }
     
     
     /*if(k>0){
         filteredTrainer=new GuestTrainer[k];
         for(int i=0;i<k;i++){
             filteredTrainer[i]=filTrainer[i];
         }
     }*/
     GuestTrainer[] filteredTrainer=null;
     if(filteredList!=null&&filteredList.size()!=trainer.length){
         filteredTrainer=new GuestTrainer[filteredList.size()];
     for(int i=0;i<filteredList.size();i++){
         filteredTrainer[i]=(GuestTrainer)filteredList.get(i);
     }
     }
     String[] gtToRemove=sceManager.getGtToRemove(currentProduct);
     session.setAttribute("gtByProduct",trainer);
     req.setAttribute("filteredGT",filteredTrainer);
     req.setAttribute("product",currentProduct);
     req.setAttribute("lastName",lastName);
     req.setAttribute("location",location);
     req.setAttribute("role",role);
     req.setAttribute("toggle",toggle);
     req.setAttribute("gtToRemove",gtToRemove);

     if(!(filteredList.size()>0)){
         // System.out.println("No GT present for applied filter");
     }

     if(!(trainer.length>0)){
         // System.out.println("No GT present for selected product");
     }

     }
     catch(Exception e){
     }
     return "success";
 }
 
 public String gotoUploadGTList() throws SCEException{
	 HttpServletRequest req = getServletRequest();
	 HttpSession session=req.getSession();
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
      String message="Please fill all the fields.";
       try{
           String product = req.getParameter("product");
        /*// System.out.println(addGuestTrainerForm.getUploadGTList().getContentType());
       Workbook w=Workbook.getWorkbook(addGuestTrainerForm.getUploadGTList().getInputStream());*/
           Workbook w=Workbook.getWorkbook(addGuestTrainerForm.getUploadGTList());
       Sheet s=w.getSheet(0);
       int rows=s.getRows();
       int cols=s.getColumns();
       boolean flag=true;
       if(rows<=1){
           message="File is Empty";
           flag=false;
       }
       if(cols!=9){
           if(cols>9){
           message="File is invalid as number of columns are greater than required columns";
           }
           else{
           message="File is invalid as number of columns are less than required columns";
           }
           flag=false;
       }
       String regexOnlyAlpha="[\\p{Alpha}][\\p{Alpha}\\s]*";
       String regex="[\\p{Alpha}][-\\p{Alpha}\\s]*";
       String regexEmail="[.@\\p{Alnum}]+";
       String regexNTID="[\\p{Alnum}]+";
       String[] name=new String[rows];
       String[] email=new String[rows];
       String[] role=new String[rows];
       String[] manager=new String[rows];
       String[] location=new String[rows];
       String[] mgrRole=new String[rows];
       String[] eventHistory=new String[rows];
       String[] fName=new String [rows];
       String[] lName=new String [rows];
       String[] ntid=new String [rows];
       String[] mgrEmail=new String[rows];
       
       // System.out.println("Rows"+s.getRows());
       // System.out.println("Columns"+s.getColumns());
       // System.out.println("In testUploadExcel+s1+"+name.length);
      
       for(int i=0;i<cols && flag;i++){
           for(int j=1;j<rows && flag;j++){
               Cell cell=s.getCell(i,j);
               
               if(i==0){
                   name[j]=cell.getContents();
                   if(name[j].length()==0){
                   // System.out.println("Invalid File");
                   req.setAttribute("message",message);
                   flag=false;
                   }
               }
            if(i==0){
                   fName[j]=cell.getContents();
                   if(fName[j].length()==0){
                   // System.out.println("Invalid File");
                   req.setAttribute("message",message);
                   flag=false;
                   }
                   else{
                       // System.out.println("fName[j].matches(regexOnlyAlpha)"+fName[j].matches(regexOnlyAlpha));
                       if(!fName[j].matches(regexOnlyAlpha)){
                           // System.out.println("Invalid File");
                           message="Only alphabets are allowed for First Name";
                           req.setAttribute("message",message);
                           flag=false;
                       }
                   }
               }
                if(i==1){
                   lName[j]=cell.getContents();
                   if(lName[j].length()==0){
                   // System.out.println("Invalid File");
                   req.setAttribute("message",message);
                   flag=false;
                   }  else{
                       if(!lName[j].matches(regexOnlyAlpha)){
                           // System.out.println("Invalid File");
                           message="Only alphabets are allowed for Last Name";
                           req.setAttribute("message",message);
                           flag=false;
                       }
                   }
               }
                if(i==2){
                   ntid[j]=cell.getContents();
                   if(ntid[j].length()==0){
                   // System.out.println("Invalid File");
                   req.setAttribute("message",message);
                   flag=false;
                   }else{
                       if(!ntid[j].matches(regexNTID)){
                       // System.out.println("Invalid NTID.Only alphanumeric value is allowed");
                       message="Invalid NTID.Only alphanumeric value is allowed";
                       req.setAttribute("message",message);
                       flag=false;
                       }
                   }
               }
               //i==3
               if(i==3){
                   email[j]=cell.getContents();
                   if(email[j].length()==0){
                   // System.out.println("Invalid File");
                   req.setAttribute("message",message);
                   flag=false;
                   }
                   else{
                       //email[j].substring(email[j].length()-11,email[j].length()));
                          if((email[j].length()<11)||!(email[j].substring(email[j].length()-11,email[j].length()).equalsIgnoreCase("@pfizer.com"))){
                           message="Email entered is not valid.Please try again";
                           req.setAttribute("message",message);
                           flag=false;
                   }else{
                       if(!email[j].matches(regexEmail)){
                           message="Email entered is not valid.Please try again";
                           req.setAttribute("message",message);
                           flag=false;
                       }
                   }
                   }
               }
               //i==4
               if(i==4){
                   role[j]=cell.getContents();
                   if(role[j].length()==0){
                   // System.out.println("Invalid File");
                   req.setAttribute("message",message);
                   flag=false;
                   } else{
                       if(!role[j].matches(regex)){
                           // System.out.println("Invalid File");
                           message="Only alphabets are allowed for Role";
                           req.setAttribute("message",message);
                           flag=false;
                       }
                   }
               }
               //i==5
               if(i==5){
                   location[j]=cell.getContents();
                   if(location[j].length()==0){
                   // System.out.println("Invalid File");
                   req.setAttribute("message",message);
                   flag=false;
                   } else{
                       if(!location[j].matches(regexOnlyAlpha)){
                           // System.out.println("Invalid File");
                           message="Only alphabets are allowed for Location";
                           req.setAttribute("message",message);
                           flag=false;
                       }
                   }
               }
               //i==6
             /*
              * Start code change done for removing the mandatory Manager field while uploading GTList
              */
               if(i==6){
                   manager[j]=cell.getContents();   
                   if(manager[j].length()!=0){
                       if(!manager[j].matches(regexOnlyAlpha)){
                           // System.out.println("Invalid File");
                           message="Only alphabets are allowed for Manager Name";
                           req.setAttribute("message",message);
                           flag=false;
                       }                   
               		}else{
               			System.out.println("manager name skipped");
                    }
               }//end of if i==6
               
               if(i==7){
                   mgrEmail[j]=cell.getContents();
                   if(mgrEmail[j].length()!=0){                       
                           if((mgrEmail[j].length()<11)||!(mgrEmail[j].substring(mgrEmail[j].length()-11,mgrEmail[j].length()).equalsIgnoreCase("@pfizer.com"))){
                           message="Manager Email entered is not valid.Please try again";
                           req.setAttribute("message",message);
                           flag=false;
                       if(!mgrEmail[j].matches(regexEmail)){
                           message="Manager Email entered is not valid.Please try again";
                           req.setAttribute("message",message);
                           flag=false;
                       }                   
                   }
               }else{
            	   System.out.println("manager email skipped");
               }
           	}//end of if i==7

           	//i==8           	  
           		if(i==8){
                   mgrRole[j]=cell.getContents(); 
                   if(mgrRole[j].length()!=0){
                       if(!mgrRole[j].matches(regex)){
                           // System.out.println("Invalid File");
                           message="Only alphabets are allowed for Manager Role";
                           req.setAttribute("message",message);
                           flag=false;
                       }                   
               }else{
            	   System.out.println("manager role skipped");
               }
           	}//end of if i==8
           }           
       }
       /*
        * End code change done for removing the mandatory Manager field while uploading GTList
        */
       
         if(flag){
           GuestTrainer trainers[]=sceManager.getGTByProduct(product);
           //GuestTrainer trainers=sceManager.getGTByEmailAndProduct(email[k],product);
           if(trainers!=null && trainers.length>0){
           for(int k=1;k<rows && flag;k++){
               for(int i=0;i<trainers.length && flag;i++){
                   if(email[k].equalsIgnoreCase(trainers[i].getRepEmail())){
                       flag=false;
                       message="Guest Trainer already available.";
                       req.setAttribute("message",message);
                       }
                   }
               }
           }
        }
       if(flag){
        for(int k=1;k<rows;k++){
           GuestTrainer trainer=new GuestTrainer();
           trainer.setRepEmail(email[k]);
           trainer.setRepLocation(location[k]);
           //trainer.setRepName(name[k]);
           trainer.setRepRole(role[k]);
           trainer.setRepManagerRole(mgrRole[k]);
           trainer.setRepManager(manager[k]);
           trainer.setEventHistory(eventHistory[k]);
           trainer.setAssociatedProduct(product);
           trainer.setIsAccepted("N");
           trainer.setIsSelected("Y");
           trainer.setFname(fName[k]);
           trainer.setLname(lName[k]);
           trainer.setNtid(ntid[k]);
           trainer.setMgrEmail(mgrEmail[k]);
           sceManager.addNewGT(trainer);
       }
       message="File Uploaded Successfully.";
      
       }
       
       }
       catch(Exception e){
           // System.out.println("In exception");
           e.printStackTrace();
       }
   String[] products=getProductForEvent();
    req.setAttribute("message",message);
   req.setAttribute("products",products);
   req.setAttribute("product",req.getParameter("product"));
   return "success";
}
 public String gotoSaveGT() throws SCEException
 {
	 HttpServletRequest req = getServletRequest();
     HttpSession session=req.getSession();
	 String fname=req.getParameter("hdnfname");
	 String lname=req.getParameter("hdnlname");
	 String email=req.getParameter("hdnemail");
	 String location=req.getParameter("hdnlocation");
	 String product=req.getParameter("hdnproduct");
	 String role=req.getParameter("hdnrole");
	 String manager=req.getParameter("hdnmanager");
	 String managerEmail=req.getParameter("hdnmanagerEmail");
	 String managerRole=req.getParameter("hdnmanagerRole");
	 String ntid=req.getParameter("hdnntid");
	 
	 System.out.println("managerEmail:"+managerEmail);
	 System.out.println("manager name:"+manager);
	 System.out.println("manager role:"+managerRole);
    
     // System.out.println("gotoSaveGT");
     String message="";
     String[] products;
     List<String> list=new ArrayList<String>();
     
     list.add(product);
     
     products = list.toArray(new String[list.size()]);
     
     
     
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
     try{
     GuestTrainer[] trainer=sceManager.getGTByEmail(email);
     if(trainer!=null){
         for(int i=0;i<trainer.length;i++){
             for(int j=0;j<products.length;j++){
             if(trainer[i].getAssociatedProduct().equalsIgnoreCase(products[j]))
            		 {
                 message="Trainer with Email  \" "+email+" \" and product \" "+products[j]+" \" already exist.";
                 req.setAttribute("message", message);
                 
                 addGuestTrainerForm.setRepManager(manager);
                 addGuestTrainerForm.setRepManagerRole(managerRole);
                 addGuestTrainerForm.setRepRole(role);
                 addGuestTrainerForm.setRepFname(fname);                              
                 addGuestTrainerForm.setRepLname(lname);
                 addGuestTrainerForm.setRepNTID(ntid);
                 addGuestTrainerForm.setRepManagerEmail(managerEmail); 
                 gotoAddGT();
                 addGuestTrainerForm.setRepProduct(product);
                 addGuestTrainerForm.setRepLocation(location);
                 
                 return "alreadyExist";
         }
         }
         
     }
         req.setAttribute("message", message);
     
     GuestTrainer guestTrainer=new GuestTrainer();
     //guestTrainer.setRepName(form.getRepFname());
     guestTrainer.setRepEmail(email);
     
     guestTrainer.setRepLocation(location);
     guestTrainer.setRepManager(manager);
     guestTrainer.setRepManagerRole(managerRole);
     guestTrainer.setRepRole(role);
     guestTrainer.setFname(fname);
     guestTrainer.setIsAccepted("N");
         /*Sanjeev CHAnge IsSelected from 'Y' to 'N'*/
     guestTrainer.setIsSelected("Y");
     guestTrainer.setLname(lname);
     guestTrainer.setNtid(ntid);
     guestTrainer.setMgrEmail(managerEmail);
     
     for(int i=0;i<products.length;i++){
         guestTrainer.setAssociatedProduct(products[i]);
         sceManager.addNewGT(guestTrainer);
     }
     

     
     }
     }
             catch(SCEException scee){
         req.setAttribute("errorMsg",scee.getErrorCode());
         return "failure";
     }catch(Exception e){
         req.setAttribute("errorMsg","error.sce.unknown");
        // sceLogger.error(LoggerHelper.getStackTrace(e));
         return "failure";
     }
     String[] productss=getProductForEvent();
     session.setAttribute("products",productss);
     // System.out.println("gotoSaveGT");
     message="Trainer added successfully.";
     req.setAttribute("message",message);
    
     session.removeAttribute("gtByProduct");
     return "success";
 }
 public String gotoUploadGT() throws SCEException
 {
	 HttpServletRequest req = getServletRequest();
	 HttpSession session=req.getSession();
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
    
     String[] products=getProductForEvent();
     req.setAttribute("products",products);
	 }
     catch(Exception e){
         req.setAttribute("errorMsg","error.sce.unknown");
         //sceLogger.error(LoggerHelper.getStackTrace(e));
         return "failure";
     }
     return "success";
	 
 }

 
  public String gotoAddGT()
 {
	  HttpServletRequest req = getServletRequest();  
      HttpSession session=req.getSession();
	  try
	  {
		  productList=sceManager.getAllProducts();
		  String[] location=sceManager.getAllHomeStatesArr();
		  HashMap locations = new LinkedHashMap();;
          if (locations != null && location.length > 0) {
           for (int i=0; i<location.length; i++){
             // products.put("select","--Select--");
        	   locations.put(location[i],location[i]);
           }
         
         }
          locationList=locations;
	  }
	  catch(Exception e){
          req.setAttribute("errorMsg","error.sce.unknown");
          //sceLogger.error(LoggerHelper.getStackTrace(e));
          return "failure";
      }
     return "success";
 }
  
  public String downloadGTTemplate() 
  {
    return "success";
  }

public String removeGuestTrainer()
  {
	  HttpServletRequest req = getServletRequest();  
          HttpSession session=req.getSession();

      try{
      String email = req.getParameter("selGTEmail");
      String product = req.getParameter("selGTProd");
      sceManager.removeGT(email,product);
      req.setAttribute("message","User has been removed successfully");
      req.setAttribute("product",product);
    GuestTrainer[]  trainer=sceManager.getAllGTForProduct(product);
      /*
      
      GuestTrainer[] trainer=sceManager.getGTByProduct(product);
                  if(trainer!=null && trainer.length<=0){
                  trainer=sceManager.getGTFromAtlasByProduct(product);
                  }*/
      String[] gtToRemove=sceManager.getGtToRemove(product);
                       session.setAttribute("gtByProduct",trainer);
                       req.setAttribute("gtToRemove",gtToRemove);
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
  
  
  public String gotoSelectAllGT(){
      
    	  HttpServletRequest req = getServletRequest();
    	  HttpSession session=req.getSession();
    	  try
    	  {
    		  System.out.println("in gotoSelectAllGT method" );
        String product=req.getParameter("hdnproduct");
        req.setAttribute("product",product);
        System.out.println("product:"+product );
        
        String[] gtEmail=req.getParameterValues("selectGTChkBox");
        
        for(int i=0;i<gtEmail.length;i++){
       GuestTrainer trainer= sceManager.getGTFromAtlasByEmailProduct(gtEmail[i],product);
       trainer.setIsAccepted("N");
       trainer.setIsSelected("Y");
       System.out.println("IsSelected:"+trainer.getIsSelected());
       sceManager.addNewGT(trainer);
       
        }
       session.removeAttribute("gtByProduct");
       //GuestTrainer[] trainers=sceManager.getGTByProduct(req.getParameter("product"));
       GuestTrainer[] trainers=sceManager.getAllGTForProduct(req.getParameter("hdnproduct"));
       String[] gtToRemove=sceManager.getGtToRemove(req.getParameter("hdnproduct"));
       session.setAttribute("gtByProduct",trainers);
       String message="Selection saved successfully.";
       req.setAttribute("message",message);
       req.setAttribute("gtToRemove",gtToRemove);
       }
       catch(Exception e){
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
