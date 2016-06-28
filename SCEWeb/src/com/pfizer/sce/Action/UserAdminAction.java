package com.pfizer.sce.Action;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.sce.ActionForm.SearchForAttendeeForm;
import com.pfizer.sce.ActionForm.UserDataForm;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.EvaluationControllerHelper;
import com.pfizer.sce.helper.LegalConsentHelper;
import com.pfizer.sce.utils.SCEUtils;

public class UserAdminAction extends ActionSupport implements
		ServletRequestAware, ModelDriven {

	private static SCEManagerImpl sceManager = new SCEManagerImpl();
	private UserDataForm userDataForm = new UserDataForm();
	private static LegalConsentHelper legalConsentHelp = new LegalConsentHelper();

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
		this.request = request;

	}

	public String gotoUserAdmin() {
		HttpServletRequest req = getRequest();
		HttpSession session = req.getSession();

		String result = legalConsentHelp.checkLegalConsent(req, session);
		// System.out.println("*****result*****:" + result);
		if (result != null && result.equals("success")) {
			// System.out.println("*************Forwarding to legalConsent");
			String forwardToHomePage = "N";
			EvaluationControllerHelper.setBookMarkURL(session, req,
					forwardToHomePage);
			return new String("legalConsent");
		} else if (result != null && result.equals("exception")) {
			// System.out.println("**********Forwarding to exception");
			return new String("failure");
		}
		// Get All Users
		User[] users = sceManager.getUsersByStatus("ACTIVE");
		req.setAttribute("users", users);
		return new String("success");
	}

	public String gotoAddUser() {
		HttpServletRequest req = getRequest();
		HttpSession session = req.getSession();
		String result = legalConsentHelp.checkLegalConsent(req, session);
		// System.out.println("*****result*****:" + result);
		if (result != null && result.equals("success")) {
			// System.out.println("*************Forwarding to legalConsent");
			String forwardToHomePage = "N";
			EvaluationControllerHelper.setBookMarkURL(session, req,
					forwardToHomePage);
			return new String("legalConsent");
		} else if (result != null && result.equals("exception")) {
			// System.out.println("**********Forwarding to exception");
			return new String("failure");
		}
		return new String("success");
	}

	public String selectUserStatus() {
		HttpServletRequest req = getRequest();

		String userStatus = null;
		userStatus = req.getParameter("selUserStatus");

		if (userStatus.equalsIgnoreCase("All")) {

			// Get All Users
			User[] users = sceManager.getAllUsers();
			req.setAttribute("users", users);
		}
		if (userStatus.equalsIgnoreCase("ACTIVE")) {
			User[] users = sceManager.getUsersByStatus("ACTIVE");
			req.setAttribute("users", users);
		}
		if (userStatus.equalsIgnoreCase("INACTIVE")) {
			User[] users = sceManager.getUsersByStatus("INACTIVE");
			req.setAttribute("users", users);
		}

		return new String("success");
	}
	
    public String gotoUpdateUser()
    {
        HttpServletRequest req = getRequest();
        
        Integer userId = null;
        User user = null;
        
        try{
            if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("selUserId"))) {
                userId = new Integer(req.getParameter("selUserId")); 
                user = sceManager.getUserById(userId);
                
                
                userDataForm.setId(userId);
                userDataForm.setEmplId(user.getEmplId());
                userDataForm.setFirstName(user.getFirstName());
                userDataForm.setLastName(user.getLastName());
                userDataForm.setNtid(user.getNtid());
                userDataForm.setNtdomain(user.getNtdomain());
                userDataForm.setStatus(user.getStatus());
                userDataForm.setEmail(user.getEmail());
                userDataForm.setUserGroup(user.getUserGroup());
                /* Author: Mayank Date:07-Oct-2011 SCE Enhancement 2011 */
                userDataForm.setExpirationDate(user.getExpirationDate());
                
                // System.out.println("Expirattion Date"+userDataForm.getExpirationDate());
                req.setAttribute("expDate",user.getExpirationDate());
                return new String("success");
            }
            else {
                return new String("noUserId");
            }
        }catch(SCEException scee){
            req.setAttribute("errorMsg",scee.getErrorCode());
            return new String("failure");
        }catch(Exception e){
            req.setAttribute("errorMsg","error.sce.unknown");
            //sceLogger.error(LoggerHelper.getStackTrace(e));
            return new String("failure");
        }
        
    }
    
    public String updateUser()
    {
        HttpServletRequest req =getRequest();
        
        try 
        {
            User user = new User();
            
            
            // System.out.println("id"+userDataForm.getId());
            // System.out.println("First Name "+userDataForm.getFirstName());
            // System.out.println("Last Name"+userDataForm.getLastName());
            // System.out.println("Nt Domain "+userDataForm.getNtdomain());
            // System.out.println("User Group "+userDataForm.getUserGroup());
            // System.out.println("Expiration Date "+userDataForm.getExpirationDate());
            // System.out.println("Email "+userDataForm.getEmail());
            
        
            user.setId(userDataForm.getId());
            user.setEmplId(userDataForm.getEmplId());
            user.setFirstName(userDataForm.getFirstName());
            user.setLastName(userDataForm.getLastName());
            user.setNtdomain(userDataForm.getNtdomain());
            user.setUserGroup(userDataForm.getUserGroup());
            user.setStatus(userDataForm.getStatus());
            user.setEmail(userDataForm.getEmail());
            /* Author: Mayank Date:07-Oct-2011 SCE Enhancement 2011 */
            user.setExpirationDate(userDataForm.getExpirationDate());
        
            sceManager.updateUser(user);
            
        }        
        catch (Exception e)
        {
        	userDataForm.setMessage(e.getMessage());
            return new String("updateUserFailed");
        }
        req.setAttribute("message","User has been updated successfully");
        return new String("success");
    }
    
    public String removeUser()
    {
        HttpServletRequest req =getRequest();
        
        Integer userId = null;
        if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("selUserId"))) {
            userId = new Integer(req.getParameter("selUserId"));
            try {
                sceManager.removeUser(userId);
            }
            catch (Exception e) {
                return new String("success");
            }
        }
        req.setAttribute("message","User has been removed successfully");
        return new String("success");
    }
    
    public String addUser()
    {
        HttpServletRequest req = getRequest();
        //try{
        String actionType; 
        String role = null;
         if (SCEUtils.isFieldNotNullAndComplete(req.getParameter("actionStatus"))) {
            actionType = req.getParameter("actionStatus"); 
         }
         else{
            actionType = "";
                       
            }
        String expDate = null;        
        
        try 
        {
            User user = new User();
        
            user.setEmplId(userDataForm.getEmplId());
            user.setFirstName(userDataForm.getFirstName());
            user.setLastName(userDataForm.getLastName());
            user.setNtid(userDataForm.getNtid().trim());
            user.setNtdomain(userDataForm.getNtdomain());
            user.setUserGroup(userDataForm.getUserGroup());
            user.setStatus(userDataForm.getStatus());
            user.setEmail(userDataForm.getEmail());
            /* Author: Mayank Date:07-Oct-2011 SCE Enhancement 2011 */
            user.setExpirationDate(userDataForm.getExpirationDate());
            
            // System.out.println(userDataForm.getExpirationDate());
            req.setAttribute("addExpirationDate",userDataForm.getExpirationDate());
        
            User u = sceManager.getUserByNTId(user.getNtid());
            
            // System.out.println("user id:" +user.getId());
            String message="";
            
            if(u != null){
                if(u.getUserGroup()!=null){
                    role=u.getUserGroup();
                }
                else{
                    role="Undefined Role";
                }
                user.setId(u.getId());
                if (role.equals("SCE_TrainingTeam")){
                    role="Training Team";
                }
                else if(role.equals("SCE_Administrators")){
                    role="Admin";
                }
                else if(role.equals("SCE_GuestTrainers_NonMGR")){
                    role="Guest Trainer Non Manager";
                }
                else if(role.equals("SCE_GusetTrainer_MGR")){
                    role="Guest Trainer Manager";
                }

            }
            
                        
            //If user is present in SCE and is Active, display warning message.
            if (u != null && u.getStatus().equalsIgnoreCase("ACTIVE")&& (!actionType.equals("editUser") || actionType.equals(""))){
                //Set error message
                message = "The user '" + u.getNtid() + "' already exists in the system as '"+role+ "' and the Status of the user is 'Active'"  ;

                userDataForm.setMessage(message);
                return new String("createUserFailed");
            }
            
            //If user is present in SCE and is Inactive, populate existing data.
            else if(u != null && u.getStatus().equalsIgnoreCase("INACTIVE") && (!actionType.equals("editUser") || actionType.equals(""))){
                
                message = "The user '" + u.getNtid() + "' already exists in the system as '"+role+ "' and the Status of the user is 'Inactive'. Please click on 'Save' button to update the user details"  ;
                
                //get existing details of the user
               // user = sceManager.getUserByNTId(form.getNtid());
                
                userDataForm.setId(u.getId());
                userDataForm.setEmplId(u.getEmplId());
                userDataForm.setFirstName(u.getFirstName());
                userDataForm.setLastName(u.getLastName());
                userDataForm.setNtid(u.getNtid());
                userDataForm.setNtdomain(u.getNtdomain());
                userDataForm.setStatus(u.getStatus());
                userDataForm.setEmail(u.getEmail());
                //fetching user_id from dtabas eto update the user details:::Date: 08-Dec-2011
                
                //form.setUserGroup(u.getUserGroup());
                /* Author: Mayank Date:07-Oct-2011 SCE Enhancement 2011 */
            
                if(user.getExpirationDate()==null || user.getExpirationDate().equals("")){
                    expDate = "";
                }
                else{
                expDate=user.getExpirationDate();
                }
                userDataForm.setExpirationDate(user.getExpirationDate());
                req.setAttribute("actionStatus","refresh");
                userDataForm.setMessage(message);
                return new String("createUserRefresh");
                
            }
            
            else if(u != null && u.getStatus().equalsIgnoreCase("INACTIVE") && actionType.equals("editUser")){
                sceManager.updateUser(user);
                req.removeAttribute("actionStatus");
                req.setAttribute("message","User details have been saved successfully");
            }
            
            else {
                sceManager.createUser(user);
                req.setAttribute("message","User has been created successfully");
            }
            
       
        //}catch(SCEException scee){
            //req.setAttribute("errorMsg",scee.getErrorCode());
            //return new Forward("failure");
        }catch(Exception e){
            req.setAttribute("errorMsg","error.sce.unknown");
           // sceLogger.error(LoggerHelper.getStackTrace(e));
            return new String("failure");
        }       
        /*catch (Exception e)
        {
            form.setMessage(e.getMessage());
            req.setAttribute("message","User creation failed");
            return new Forward("createUserFailed");
        }*/
        
        return new String("success");
    }

}
