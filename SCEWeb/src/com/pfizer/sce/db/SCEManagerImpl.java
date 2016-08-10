package com.pfizer.sce.db;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import com.pfizer.sce.beans.Attendee;
import com.pfizer.sce.beans.BusinessRule;
import com.pfizer.sce.beans.CourseDetails;
import com.pfizer.sce.beans.CourseEvalTemplateMapping;
import com.pfizer.sce.beans.Descriptor;
import com.pfizer.sce.beans.EmailTemplate;
import com.pfizer.sce.beans.EmailTemplateForm;
import com.pfizer.sce.beans.EvaluationFormScore;
import com.pfizer.sce.beans.Event;
import com.pfizer.sce.beans.EventCourseProductMapping;
import com.pfizer.sce.beans.EventsCreated;
import com.pfizer.sce.beans.GuestTrainer;
import com.pfizer.sce.beans.Learner;
import com.pfizer.sce.beans.LegalConsentTemplate;
import com.pfizer.sce.beans.LegalQuestion;
import com.pfizer.sce.beans.LegalQuestionDetail;
import com.pfizer.sce.beans.Question;
import com.pfizer.sce.beans.Role;
import com.pfizer.sce.beans.SCE;
import com.pfizer.sce.beans.SCEComment;
import com.pfizer.sce.beans.SCEDetail;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.SCEReport;
import com.pfizer.sce.beans.ScoringSystem;
import com.pfizer.sce.beans.Template;
import com.pfizer.sce.beans.TemplateVersion;
import com.pfizer.sce.beans.TrainerLearnerMapping;
import com.pfizer.sce.beans.UploadBlankForm;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.beans.UserLegalConsent;
import com.pfizer.sce.beans.WebExDetails;
import com.pfizer.sce.common.SCEConstants;
import com.pfizer.sce.utils.LoggerHelper;
import com.pfizer.sce.utils.SCEUtils;

public class SCEManagerImpl {
	
	private SCEControlImpl sceControl = new SCEControlImpl();
	// Logger to create logs
	static Logger log = Logger.getLogger(SCEManagerImpl.class);
	/*
	 * public User getUserByNTIdAndDomain(String ntid, String ntdomain) {
	 * // System.out.println("test 1"); Session session =
	 * HibernateUtils.getHibernateSession(); // System.out.println("test 2");
	 * 
	 * 
	 * // String queryString = null; List<User> user = new ArrayList<User>();
	 * try { //
	 * getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY, //
	 * "start of getAdhocDistributionListMember");
	 * 
	 * // System.out.println("quer1" + queryString);
	 * 
	 * queryString .append(
	 * "select  u.user_id as id, u.emplId as emplId,u.last_name as lastName, u.first_name as firstName,"
	 * ) .append(
	 * "u.email as email, u.ntid as ntid, u.ntdomain as ntdomain, u.status as status,u.usergroup as userGroup,"
	 * ) .append(
	 * "u.create_date as createDate,u.last_modified_date as lastModifiedDate  from users u where upper(u.ntid) = :ntid and upper(u.ntdomain) = :ntdomain "
	 * );
	 * 
	 * String q1 = queryString.toString(); // System.out.println("quer2" +
	 * queryString);
	 * 
	 * // Query q = session.createQuery(queryString); Query q =
	 * session.createSQLQuery(q1);
	 * 
	 * q.setParameter("ntid", ntid); q.setParameter("ntdomain", ntdomain);
	 * 
	 * // q.setString("pfizerNtId", pfizerNtId);
	 * 
	 * // getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
	 * // "getDistributionListFilters --> Execute Query : " + // q.toString());
	 * user = q.list();
	 * 
	 * 
	 * } catch (HibernateException e) { //
	 * getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY, //
	 * "getDistributionListFilters --> HibernateException : ", e);
	 * e.printStackTrace();
	 * // System.out.println("HelloBean Hibernatate Exception"); } finally {
	 * HibernateUtils.closeHibernateSession(session); } // return ;
	 * 
	 * return user.get(0); }
	 */

	public HashMap getAllEventMap() {
		Event[] events = sceControl.getEvents();
		HashMap eventMap = new LinkedHashMap();
		eventMap.put(new Integer(SCEConstants.ALL_EVENT_ID), "All");
		if (events != null && events.length > 0) {
			for (int i = 0; i < events.length; i++)
				eventMap.put(events[i].getId(), events[i].getName());
		}
		return eventMap;
	}

	public UserLegalConsent checkLegalConsentAcceptance(String ntid)
			throws SCEException {
		UserLegalConsent userLegalConsent = null;
		try {
			userLegalConsent = sceControl.checkLegalConsentAcceptance(ntid);
		} catch (SQLException sqle) {
			//log.error("SQLEXCEPTION in checkLegalConsentAcceptance", sqle);
			// System.out.println("SQLEXCEPTION in checkLegalConsentAcceptance");
			sqle.printStackTrace();
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			//log.error("EXCEPTION in checkLegalConsentAcceptance", e);
			// System.out.println("EXCEPTION in checkLegalConsentAcceptance");
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		}
		return userLegalConsent;
		// return sceControl.checkLegalConsentAcceptance(ntid);

	}

	public LegalConsentTemplate fetchLegalContent() throws SCEException {

		LegalConsentTemplate legalConsentTemplate = null;
		try {
			legalConsentTemplate = sceControl.fetchLegalContent();
		} catch (SQLException sqle) {
			//log.error("SQLEXCEPTION in fetchLegalContent", sqle);
			// System.out.println("SQLEXCEPTION in fetchLegalContent");
			sqle.printStackTrace();
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			//log.error("EXCEPTION in fetchLegalContent", e);
			// System.out.println("EXCEPTION in fetchLegalContent");
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		}
		return legalConsentTemplate;
		// return sceControl.fetchLegalContent();

	}

	public Integer getDefaultEventId() {
		return sceControl.getDefaultEventId();
	}

	public TemplateVersion retrieveFirstEvalForm() throws SCEException {
		TemplateVersion tv = null;
		try {
			tv = sceControl.retrieveFirstEvalForm();
		} catch (SQLException sqle) {
			//log.error("SQLEXCEPTION in retrieveFirstEvalForm", sqle);
			// System.out.println("SQLEXCEPTION in retrieveFirstEvalForm");
			sqle.printStackTrace();
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			//log.error("EXCEPTION in retrieveFirstEvalForm", e);
			// System.out.println("EXCEPTION in retrieveFirstEvalForm");
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		}

		return tv;

	}

	public TemplateVersion[] retrieveEvalForm() throws SCEException {
		TemplateVersion[] tv = null;
		try {
			tv = sceControl.retrieveEvalForm();
		} catch (SQLException sqle) {
			//log.error("SQLEXCEPTION in retrieveFirstEvalForm", sqle);
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			//log.error("EXCEPTION in retrieveFirstEvalForm", e);
			throw new SCEException("error.sce.unknown", e);
		}

		return tv;
	}

	public Attendee[] getAttendeesBySearchCriteria(Integer eventId,
			String lastName, String firstName, String emplId,
			String salesPositionId, String isPassport, String isVisible,
			String salesPositionTypeCd) {
		System.out.println("eventId:"+eventId);
		System.out.println("lastName:"+lastName);
		System.out.println("firstName:"+firstName);
		System.out.println("emplId:"+emplId);
		System.out.println("salesPositionId:"+salesPositionId);
		System.out.println("isPassport:"+isPassport);
		System.out.println("isVisible:"+isVisible);
		System.out.println("salesPositionTypeCd:"+salesPositionTypeCd);
		
		
		String strEventId = "";
		if (eventId != null) {
			strEventId = eventId.toString().equals(SCEConstants.ALL_EVENT_ID) ? "event_id"
					: eventId.toString();
		}

		/* Added for CSO requirements */

		String sql_cso = " and sales_position_type_cd = '"
				+ salesPositionTypeCd + "'";

		/* End of addition */
		/*
		 * String sql = "select "; sql += "v.emplId as emplId, "; sql +=
		 * "v.first_name as firstName, "; sql += "v.last_name as lastName, ";
		 * sql += "v.territory_id as territoryId, "; sql +=
		 * "v.territory_role_cd as roleCd, "; sql +=
		 * "v.cluster_cd as clusterCd, "; sql += "v.team_cd as teamCd "; sql +=
		 * "from "; sql += "mv_field_employee v "; sql += "where "; sql +=
		 * "v.emplId in "; sql += "( "; sql +=
		 * "select distinct emplId from v_sce_source vs where event_id = " +
		 * strEventId + " ";
		 */
		/* Modified for RBU */
		String sql = "select ";
		sql += "v.emplId as emplId, ";
		sql += "v.first_name as firstName, ";
		sql += "v.last_name as lastName, ";
		sql += "v.sales_position_id as salesPositionId, ";
		sql += "v.role_cd as roleCd, ";
		sql += "v.bu as bu, ";
		sql += "v.sales_group as salesOrgDesc ";
		sql += "from ";
		sql += "mv_field_employee_rbu v ";
		sql += "where ";
		sql += "v.emplId in ";
		sql += "( ";
		sql += "select distinct emplId from v_sce_source vs where event_id = "
				+ strEventId + " ";
		/* End of addition */
		if ("Y".equalsIgnoreCase(isPassport)) {
			sql += "and isPassport = '" + isPassport + "' ";
		} else {
			sql += "union ";
			sql += "select distinct emplId from sce_fft vs where event_id = "
					+ strEventId + " ";
			sql += "union ";
			sql += "select distinct emplId from v_sce_p2l_source vs where event_id = "
					+ strEventId + " ";
		}
		sql += ") ";

	//	String orderBy = " order by last_name asc, first_name asc";
		String orderBy = " order by lastName asc, firstName asc";

		if (SCEUtils.isFieldNotNullAndComplete(lastName)
				&& SCEUtils.isFieldNotNullAndComplete(firstName)) {

			sql += "and ";
			sql += "(";
			sql += "upper(v.last_name) like '"
					+ lastName.replaceAll("'", "''").toUpperCase().trim()
					+ "%'";
			sql += " and ";
			sql += "upper(v.first_name) like '"
					+ firstName.replaceAll("'", "''").toUpperCase().trim()
					+ "%'";
			sql += ")";
			/* Added for CSO requirements */
			if (isVisible.equalsIgnoreCase("N")) {
				sql = sql + sql_cso;
			}
			/* End of addition */
			
			/* start code change done for displaying CSO while searching through firstName and lastName 
			 * 	BY Sanjeev
			 */
            sql += " union";
            sql += " select ";
            sql += " l.EMPL_ID as emplId,";
            sql += " l.FIRST_NAME as firstName, ";
            sql += " l.LAST_NAME as lastName, ";
            sql += " l.SALES_POSITION_ID as salesPositionId, ";
            sql += " l.ROLE as roleCd, ";
            sql += " l.BUSINESS_UNIT as bu, ";
            sql += " l.SALES_ORGANISATION as salesOrgDesc ";
            sql += " from ";
            sql += " sales_call.p2l_learners l "; 
            sql += " where ";
            sql += "(";
            sql += "upper(l.last_name) like '" + lastName.replaceAll("'","''").toUpperCase().trim() + "%'";
            sql += " and ";
            sql += "upper(l.first_name) like '" + firstName.replaceAll("'","''").toUpperCase().trim() + "%'";
            sql += ")";
            /* end code change done for displaying CSO while searching through firstName and lastName 
			 * 	BY Sanjeev
			 */
			
			
			
			sql += orderBy;
			// return
			// sceControl.getAttendeesByLastAndFirstName(lastName.replaceAll("'","''").toUpperCase().trim()+"%",
			// firstName.replaceAll("'","''").toUpperCase().trim()+"%",
			// isPassport, lOrder, fOrder);
			return sceControl.getAttendeesBySearchCriteria(sql);
		} else if (SCEUtils.isFieldNotNullAndComplete(lastName)) {

			sql += "and ";
			sql += "upper(v.last_name) like '"
					+ lastName.replaceAll("'", "''").toUpperCase().trim()
					+ "%'";
			/* Added for CSO requirements */
			if (isVisible.equalsIgnoreCase("N")) {
				sql = sql + sql_cso;
			}
			/* End of addition */
			
			/* start code change done for displaying CSO while searching through lastName 
			 * 	BY Sanjeev
			 */
            sql += " union";
            sql += " select ";
            sql += " l.EMPL_ID as emplId,";
            sql += " l.FIRST_NAME as firstName, ";
            sql += " l.LAST_NAME as lastName, ";
            sql += " l.SALES_POSITION_ID as salesPositionId, ";
            sql += " l.ROLE as roleCd, ";
            sql += " l.BUSINESS_UNIT as bu, ";
            sql += " l.SALES_ORGANISATION as salesOrgDesc ";
            sql += " from ";
            sql += " sales_call.p2l_learners l ";
            sql += " where ";
            sql += "upper(l.last_name) like upper('" + lastName.replaceAll("'","''").toUpperCase().trim() + "%')";
       
            /* end code change done for displaying CSO while searching through lastName 
			 * 	BY Sanjeev
			 */  		
			
			sql += orderBy;
			// return
			// sceControl.getAttendeesByLastName(lastName.replaceAll("'","''").toUpperCase().trim()+"%",
			// isPassport, lOrder, fOrder);
			System.out.println("sql:"+sql);
			return sceControl.getAttendeesBySearchCriteria(sql);
		} else if (SCEUtils.isFieldNotNullAndComplete(firstName)) {

			sql += "and ";
			sql += "upper(v.first_name) like '"
					+ firstName.replaceAll("'", "''").toUpperCase().trim()
					+ "%'";
			/* Added for CSO requirements */
			if (isVisible.equalsIgnoreCase("N")) {
				sql = sql + sql_cso;
			}
			/* End of addition */
			
			/* Start code change done for displaying CSO while searching through firstName 
			 * 	BY Sanjeev
			 */  
            sql += " union";
            sql += " select ";
            sql += " l.EMPL_ID as emplId,";
            sql += " l.FIRST_NAME as firstName, ";
            sql += " l.LAST_NAME as lastName, ";
            sql += " l.SALES_POSITION_ID as salesPositionId, ";
            sql += " l.ROLE as roleCd, ";
            sql += " l.BUSINESS_UNIT as bu, ";
            sql += " l.SALES_ORGANISATION as salesOrgDesc ";
            sql += " from ";
            sql += " sales_call.p2l_learners l ";
            sql += " where ";
            sql += "upper(l.first_name) like '" + firstName.replaceAll("'","''").toUpperCase().trim() + "%'";
         
            /* end code change done for displaying CSO while searching through firstName 
			 * 	BY Sanjeev
			 */  
			
			sql += orderBy;
			// return
			// sceControl.getAttendeesByFirstName(firstName.replaceAll("'","''").toUpperCase().trim()+"%",
			// isPassport, lOrder, fOrder);
			return sceControl.getAttendeesBySearchCriteria(sql);
		} else if (SCEUtils.isFieldNotNullAndComplete(emplId)) {

			sql += "and ";
			sql += "v.emplId like '" + emplId.trim() + "'";
			/* Added for CSO requirements */
			if (isVisible.equalsIgnoreCase("N")) {
				sql = sql + sql_cso;
			}
			/* End of addition */
			
			/* Start code change done for displaying CSO while searching through emplId 
			 * 	BY Sanjeev
			 */
            sql += " union";
            sql += " select ";
            sql += " l.EMPL_ID as emplId,";
            sql += " l.FIRST_NAME as firstName, ";
            sql += " l.LAST_NAME as lastName, ";
            sql += " l.SALES_POSITION_ID as salesPositionId, ";
            sql += " l.ROLE as roleCd, ";
            sql += " l.BUSINESS_UNIT as bu, ";
            sql += " l.SALES_ORGANISATION as salesOrgDesc ";
            sql += " from ";
            sql += " sales_call.p2l_learners l ";
            sql += "where ";
            sql += "l.EMPL_ID like '" + emplId.trim() + "'";
            //like '%" + emplId + "%'";
          
            
            
            /* end code change done for displaying CSO while searching through emplId 
			 * 	BY Sanjeev
			 */
			
			
			sql += orderBy;
			
			System.out.println("query running:"+sql);
			// return sceControl.getAttendeesByEmplId(emplId.trim(), isPassport,
			// lOrder, fOrder);
			return sceControl.getAttendeesBySearchCriteria(sql);
		} else if (SCEUtils.isFieldNotNullAndComplete(salesPositionId)) {

			sql += "and ";
			sql += "v.sales_position_id like '%" + salesPositionId + "%'";
			if (isVisible.equalsIgnoreCase("N")) {
				sql = sql + sql_cso;
			}
			/* End of addition */
			
			/* Start code change done for displaying CSO while searching through salesPositionId 
			 * 	BY Sanjeev
			 */
           sql += " union";
           sql += " select ";
           sql += " l.EMPL_ID as emplId,";
           sql += " l.FIRST_NAME as firstName, ";
           sql += " l.LAST_NAME as lastName, ";
           sql += " l.SALES_POSITION_ID as salesPositionId, ";
           sql += " l.ROLE as roleCd, ";
           sql += " l.BUSINESS_UNIT as bu, ";
           sql += " l.SALES_ORGANISATION as salesOrgDesc ";
           sql += " from ";
           sql += " sales_call.p2l_learners l ";
           sql += "where "; 
           sql += "l.sales_position_id like '%" + salesPositionId + "%'";
         
           /* End code change done for displaying CSO while searching through salesPositionId 
			 * 	BY Sanjeev
			 */

			sql += orderBy;
			// return sceControl.getAttendeesByTerritoryId("%" +
			// territoryId.trim() + "%", isPassport, lOrder, fOrder);
			return sceControl.getAttendeesBySearchCriteria(sql);
		} else {
			return null;
		}
	}

	public SCE[] getSCESByEventEmplId(Integer eventId, String emplId)
			throws SCEException {

		SCE[] sce = null;
		try {
			sce = sceControl.getSCESByEventEmplId(eventId, emplId);
		} catch (SQLException sqle) {
			//log.error("SqlException in getSCESByEventEmplId", sqle);
			// System.out.println("SqlException in getSCESByEventEmplId");
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			//log.error("Exception in getSCESByEventEmplId", e);
			// System.out.println("Exception in getSCESByEventEmplId");
			throw new SCEException("error.sce.unknown", e);
		}
		return sce;
		// return null;
	}
	
    public Event[] getEvents()
    {
        return sceControl.getEvents();
    }
    

    
    public Attendee getAttendeeByEmplId(String emplId)
    {
        return sceControl.getAttendeeByEmplId(emplId);
    }
    
	public int fetchMaxAcceptedId() throws SCEException {
		int maxAcceptedId = 0;
		try {
			maxAcceptedId = sceControl.fetchMaxAcceptedId();

		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			sqle.printStackTrace();
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		}
		return maxAcceptedId;
	}
	
    public void acceptLegalConsent(UserLegalConsent userLegalConsent) throws SCEException{
        
        try{   
           sceControl.acceptLegalConsent(userLegalConsent);
        }catch (SQLException sqle) {
              //log.error(LoggerHelper.getStackTrace(sqle));
        	sqle.printStackTrace();
              throw new SCEException("error.sce.database.communication",sqle);
  		}catch (Exception e) {
              //log.error(LoggerHelper.getStackTrace(e));
  			e.printStackTrace();
              throw new SCEException("error.sce.unknown",e);
          }
      }
    
	public User[] getUsersByStatus(String userStatus) {	
		return sceControl.getUsersByStatus(userStatus);
	}
	
	public User[] getAllUsers()
    {
        return sceControl.getAllUsers();
    } 
	
    public User getUserById(Integer id) throws SCEException
    {
        User user = null;
        try{
        user = sceControl.getUserById(id);
        }catch (SQLException sqle) {
        	sqle.printStackTrace();
            //log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
            //log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
            throw new SCEException("error.sce.unknown",e);
        }
        return user;
    }
    
    public void updateUser(User user)
    {
        sceControl.updateUser(user);
    }
    
    public void removeUser(Integer userId)
    {
        sceControl.removeUser(userId);
    }
    
    public User getUserByNTId(String ntid)
    {
        return sceControl.getUserByNTId(ntid);
    }
    
    public void createUser(User user)
    {
        sceControl.createUser(user);
    }
    
    public SCE getSCEById(Integer sceId)
    {
        return sceControl.getSCEById(sceId);
    }
    
    public SCEDetail[] getSCEDetailsBySCEId(Integer sceId) 
    {
        SCEDetail[] objSCEDetailArr =  sceControl.getSCEDetailsBySCEId(sceId);
        
        
        // Get The Descriptors For All The Questions and Assign Them
        Descriptor[] objDescriptorArr = sceControl.getDescriptorsBySCEId(sceId);
        if (objDescriptorArr != null && objSCEDetailArr != null) 
        {
            for (int i=0; i<objDescriptorArr.length; i++) 
            {
                for (int j=0; j<objSCEDetailArr.length; j++) 
                {
                    if (objDescriptorArr[i].getQuestionId().equals(objSCEDetailArr[j].getQuestionId())) 
                    {
                        objSCEDetailArr[j].addDescriptorList(objDescriptorArr[i]);
                        break;
                    }
                }
            }
        }
        return objSCEDetailArr;
    }
    
    public LegalQuestion[] getLegalQuestion(Integer templateVersionId) throws SCEException
    {   LegalQuestion[] legalQuestions = null;
        try{
        legalQuestions = sceControl.getLegalQuestion(templateVersionId);
        }catch (SQLException sqle) {
        //log.error(LoggerHelper.getStackTrace(sqle));
        throw new SCEException("error.sce.database.communication",sqle);
        }catch (Exception e) {
        //log.error(LoggerHelper.getStackTrace(e));
        throw new SCEException("error.sce.unknown",e);
        }   
        return legalQuestions;
    }
    
    public EvaluationFormScore[] getEvaluationFormScore(Integer templateVersionId) throws SCEException
    {   EvaluationFormScore[] evaluationFormScores = null;
        try{
            evaluationFormScores = sceControl.getEvaluationFormScore(templateVersionId);
        }catch (SQLException sqle) {
        //log.error(LoggerHelper.getStackTrace(sqle));
        throw new SCEException("error.sce.database.communication",sqle);
        }catch (Exception e) {
        //log.error(LoggerHelper.getStackTrace(e));
        throw new SCEException("error.sce.unknown",e);
        }    
        return evaluationFormScores;
        
    }
    
    public LegalQuestionDetail[] getLegalQuestionDetail(Integer sceId) throws SCEException
    {
       LegalQuestionDetail[] legalQuestionDetails = null;
       try{
            legalQuestionDetails = sceControl.getLegalQuestionDetail(sceId);
        }catch (SQLException sqle) {
        //log.error(LoggerHelper.getStackTrace(sqle));
        throw new SCEException("error.sce.database.communication",sqle);
        }catch (Exception e) {
        //log.error(LoggerHelper.getStackTrace(e));
        throw new SCEException("error.sce.unknown",e);
        }   
         return legalQuestionDetails;
    }
    
    public Blob getByteArray(Integer sceId) throws SCEException
    {    Blob byteArray = null;
        try{
            
            byteArray = sceControl.getByteArray(sceId);
       }catch (SQLException sqle) {
            //log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
            //log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
        return byteArray;
    }
    
    public SCE[] getSCESHistoryByEventEmplId(Integer eventId, String emplId, String productName) throws SCEException
    {  
        SCE[] SCESHistory = null;
        try{
            SCESHistory = sceControl.getSCESHistoryByEventEmplId(eventId, emplId,productName);
       }catch (SQLException sqle) {
            //log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
            //log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        } 
        return SCESHistory;
    }
    
    public void deleteSCE(Integer sceId)
    {
        if (sceId != null) {            
            sceControl.deleteSCE(sceId);
        }
    }
    
    public SCE getNewSCE(Integer eventId, String course,String productName) throws SCEException
    {
        Integer evaluationTemplateId;
        System.out.print("lest22");
        LegalQuestion[] objLegalQuestionArr = null;
        BusinessRule[] objBusinessRulesArr = null;
        EvaluationFormScore[] objEvaluationFormScoreArr = null;
        
        SCE objSCE = new SCE();        
        
        SCEDetail[] objSCEDetailArr =  sceControl.getSCEDetailsLatest(eventId, course,productName);
        evaluationTemplateId= objSCEDetailArr[0].getTemplateVersionId();
        
        //Added try/catch clause by Shinoy, 28Nov2011
        try{
        objBusinessRulesArr =  sceControl.getBusinessRules(evaluationTemplateId);
        objLegalQuestionArr =  sceControl.getLegalQuestion(evaluationTemplateId);  
       
       // EmailTemplate[] objEmailTemplateArr =  sceControl.getEmailTemplate(evaluationTemplateId);
        
        objEvaluationFormScoreArr =  sceControl.getEvaluationFormScore(evaluationTemplateId);
        
        // Get The Descriptors For All The Questions and Assign Them
        Descriptor[] objDescriptorArr = sceControl.getDescriptorsLatest(eventId, course);        
        
        if (objDescriptorArr != null && objSCEDetailArr != null) 
        {
            for (int i=0; i<objDescriptorArr.length; i++) 
            {
                for (int j=0; j<objSCEDetailArr.length; j++) 
                {
                    if (objDescriptorArr[i].getQuestionId().equals(objSCEDetailArr[j].getQuestionId())) 
                    {
                        
                        objSCEDetailArr[j].addDescriptorList(objDescriptorArr[i]);
                        break;
                    }
                }
            }
        }
        
        for (int i=0; i<objSCEDetailArr.length; i++) 
        {
            if (i==0) 
            {
                objSCE.setTemplateVersionId(objSCEDetailArr[i].getTemplateVersionId());
                objSCE.setFormTitle(objSCEDetailArr[i].getFormTitle());
                objSCE.setEventId(eventId);
                objSCE.setPrecallComments(objSCEDetailArr[i].getPrecallComments());
                objSCE.setPostcallComments(objSCEDetailArr[i].getPostcallComments());                
                objSCE.setHlcCritical(objSCEDetailArr[i].getHlcCritical());
                objSCE.setLegalFG(objSCEDetailArr[i].getLegalFG());
                objSCE.setConflictOverallScore(objSCEDetailArr[i].getConflictOverallScore());
                objSCE.setHLCCriticalValue(objSCEDetailArr[i].getHLCCriticalValue());
                objSCE.setPrecallFlag(objSCEDetailArr[i].getPrecallFlag());
                objSCE.setPostcallFlag(objSCEDetailArr[i].getPostcallFlag());
                objSCE.setPrecallLabel(objSCEDetailArr[i].getPrecallLabel());
                objSCE.setPostcallLabel(objSCEDetailArr[i].getPostcallLabel());
                
                /*Added by Ankit 27April2016*/
                objSCE.setTemplatetitle(objSCEDetailArr[i].getTemplatetitle());
                objSCE.setCallImageDisplay(objSCEDetailArr[i].isCallImageDisplay());
                objSCE.setOverallEvaluationLable(objSCEDetailArr[i].getOverallEvaluationLable());
                System.out.println("Added template title :"+objSCEDetailArr[i].getTemplatetitle()+" to objSCE object in getNewSCE()");
                
                objSCE.setPreCallImage(objSCEDetailArr[i].getPreCallImage());
                objSCE.setPostCallImage(objSCEDetailArr[i].getPostCallImage());
                objSCE.setCallLabelDisplay(objSCEDetailArr[i].getCallLabelDisplay());
                objSCE.setCallLabelValue(objSCEDetailArr[i].getCallLabelValue());
                
                
                
                /*End*/
            }
            
            objSCE.addSceDetailList(objSCEDetailArr[i]);
        }
        
        for (int i=0; i<objBusinessRulesArr.length; i++) 
        {
            objSCE.addBusinessRulesList(objBusinessRulesArr[i]);            
        }
        
        /*
        for (int i=0; i<objEmailTemplateArr.length; i++) {
            objSCE.addEmailTemplateList(objEmailTemplateArr[i]);
        }
        */
        
        for (int i=0; i<objEvaluationFormScoreArr.length; i++) {
            objSCE.addEvaluationFormScoreList(objEvaluationFormScoreArr[i]);
        }
        
        
        for (int i=0; i<objLegalQuestionArr.length; i++) {
            objSCE.addLegalQuestionList(objLegalQuestionArr[i]);
        }
        }catch (SQLException sqle) {
            //log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
            //log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
        
        return objSCE;
        
        
    }
    
    public void giveAutoCredit(String emplId, Integer eventId, String productCode, String product, String course, String classroom, String tableName, String submittedByEmplId)
    {
        // System.out.println("emplId = "+emplId+"; eventId = "+eventId+"; productCode = "+productCode+"; product = "+product+"; course = "+course+"; classroom = "+classroom+"; tableName = "+tableName+"; submittedByEmplId = "+submittedByEmplId);
        if ((emplId != null && !emplId.trim().equals("")) && (productCode != null && !productCode.trim().equals(""))) {
            //// System.out.println("Length of params array = "+params.length);
            sceControl.giveAutoCredit(emplId, eventId, productCode,  product,  course,  classroom, tableName,  submittedByEmplId);
        }
    }
    
    
    public SCEReport[] getPendingAttendeesByCourseAndClass(Integer eventId, Integer courseId, String classroom)
    {
        String sql = "select ";
        sql += "v.EMPLID as emplId, ";
        sql += "v.event_id as eventId, ";
        sql += "v.FIRST_NAME as firstName, ";
        sql += "v.LAST_NAME as lastName, ";
       // sql += "v.TERRITORY_ID as territoryId, ";
       // sql += "v.ROLE_CD as roleCd, ";
       // sql += "v.CLUSTER_CD as clusterCd, ";
       // sql += "v.TEAM_CD as teamCd, ";
        sql += "v.BU as bu, ";
        sql += "v.RBU as rbu, ";
        sql += "v.FUTUREROLE as FUTUREROLE, ";
        sql += "v.SALESPOSITIONID as SALESPOSITIONID, ";
        sql += "v.PRODUCT_CD as productCode, ";
        sql += "v.PRODUCT_NAME as product, ";
        sql += "v.COURSE_ID as courseId, ";
        sql += "v.COURSE_DESCRIPTION as course, ";
        sql += "v.sce_completed as sceCompleted, ";
        sql += "v.CLASSROOM as classroom, ";
        sql += "v.TABLE_NAME as tableName, ";
        sql += "v.START_TIME as startDate,  ";
        sql += "v.END_TIME as endDate ";
        sql += "from ";
        sql += "v_sce_report_data v ";
        sql += "where ";
        sql += "event_id = " + eventId + " AND ";
        sql += "course_id = " + courseId + " AND ";
        sql += "sce_completed = 'N' ";
        
        if (classroom == null || "0".equals(classroom)) {            
        }
        else if ("-1".equals(classroom)) {
            sql += "AND (classroom is null or classroom = '') ";            
        }
        else {
            sql += "AND classroom = '" + classroom + "' ";
        }
            
        
        sql += "order by ";
        sql += "classroom, table_name, last_name, first_name";
        
        /*
         * Wrapper For Eliminating Duplicates
        */
        SCEReport[] sceReportAll = sceControl.getPendingAttendeesByCourseAndClass(sql);
        
        ArrayList emplIdList = new ArrayList();
        ArrayList sceReportUniqList = new ArrayList();
        
        if (sceReportAll != null && sceReportAll.length > 0) {
            for (int i=0; i < sceReportAll.length; i++) {
                if (!emplIdList.contains(sceReportAll[i].getEmplId())) {
                    emplIdList.add(sceReportAll[i].getEmplId());
                    sceReportUniqList.add(sceReportAll[i]);
                }
            }
        }
        SCEReport[] sceReportUniq = new SCEReport[sceReportUniqList.size()];
        sceReportUniq = (SCEReport[])sceReportUniqList.toArray(sceReportUniq);
        return sceReportUniq;
    }
    
    
    public Event getEventById(Integer eventId)
    {
        return sceControl.getEventById(eventId);
    }
    
    public Attendee[] getPendingAttendeesByFullcriteria(Integer eventId, Date trainingDate, String productCode, String course, String classroom, String table, String excludeEmplId)
    {
        Attendee[] attendees = null;
        Attendee[] attendees_sort = null;
        if (excludeEmplId != null) {
            
            //attendees = sceControl.getOtherPendingAttendees(eventId, trainingDate, productCode, course, excludeEmplId);
            attendees = sceControl.getOtherPendingAttendeesByFullCriteria(eventId, trainingDate, productCode, course, classroom, table, excludeEmplId);
            List attendeesList = Arrays.asList(attendees);
            Collections.sort(attendeesList);
            attendees_sort = (Attendee[])attendeesList.toArray(new Attendee[attendees.length]);
        }
        else {
            attendees = sceControl.getPendingAttendeesByFullCriteria(eventId, trainingDate, productCode, course, classroom, table);
            List attendeesList = Arrays.asList(attendees);
            Collections.sort(attendeesList);
            attendees_sort = (Attendee[])attendeesList.toArray(new Attendee[attendees.length]);
        }   
        return attendees_sort;    
    } 
    
    public SCEDetail[] getSCEDetailsByTemplateVersionId(Integer templateVersionId)
    {
        return sceControl.getSCEDetailsByTemplateVersionId(templateVersionId);
    }
    
    
    public User getUserByEmplId(String emplId)
    {
        User user = null;
        // Get User From SCE Users Table
        user = sceControl.getUserByEmplId(emplId);
        if (user == null) {
            // Get By EmplId
            user = sceControl.getTRUserByEmplId(emplId);
            if (user == null) {
                // Still Null....check for NtId
                user = sceControl.getTRUserByNTID(emplId);
            }
        }
        return user;
    }
    
    public User getManagerDetail(String emplid) throws SCEException
    {   
        User managerDetail = null;
        try{
            managerDetail = sceControl.getManagerDetail(emplid);
        }catch (SQLException sqle) {
        //log.error(LoggerHelper.getStackTrace(sqle));
        throw new SCEException("error.sce.database.communication",sqle);
        }catch (Exception e) {
       // log.error(LoggerHelper.getStackTrace(e));
        throw new SCEException("error.sce.unknown",e);
        }    
        return  managerDetail;
    }
    
    public EmailTemplate getEmailTemplate(Integer templateVersionId, String overallScore) throws SCEException
    {    EmailTemplate emailTemplate = null;
        try{
            
        emailTemplate = sceControl.getEmailTemplate(templateVersionId,overallScore);
        
        }catch (SQLException sqle) {
        //log.error(LoggerHelper.getStackTrace(sqle));
        throw new SCEException("error.sce.database.communication",sqle);
        }catch (Exception e) {
        //log.error(LoggerHelper.getStackTrace(e));
        throw new SCEException("error.sce.unknown",e);
   
    } 
    
        return emailTemplate;
    }
    
    public void saveSCE(SCE objSCE, SCEDetail[] objSCEDetailArr,LegalQuestion[] legalDetailArr) throws SCEException
    {
        
        try{
            
        boolean isInsert = false;
        Integer sceId = null;
        if (objSCE != null) {
            if (objSCE.getId() == null || objSCE.getId().intValue() == SCEConstants.SCE_INVALID_ID.intValue()) {
                isInsert = true;
            }
            
            if (isInsert) {
                if (!checkIfDuplicate(objSCE, objSCEDetailArr)) {
                    // Get New SCE ID
                    sceId = sceControl.getNextSCEId();
                    objSCE.setId(sceId);                
                    sceControl.insertSCE(objSCE);
                    if (objSCEDetailArr != null) {
                        for (int i=0; i<objSCEDetailArr.length; i++) {
                            SCEDetail objSCEDetail = objSCEDetailArr[i];
                            objSCEDetail.setSceId(objSCE.getId());
                            sceControl.insertSCEDetail(objSCEDetail);
                        }          
                    }  
                    
                    if (legalDetailArr != null) {
                        for (int i=0; i<legalDetailArr.length; i++) {
                            LegalQuestion legalDetail = legalDetailArr[i];
                            legalDetail.setSCEId(objSCE.getId());
                            Integer id = legalDetail.getId();
                            String legalQuestionValue = legalDetail.getLegalQuestionValue();
                            sceControl.insertLegalDetail(sceId,id,legalQuestionValue);
                        }          
                    }        
                }
            }
            else {
                sceControl.updateSCE(objSCE);                
                if (objSCEDetailArr != null) {
                    for (int i=0; i<objSCEDetailArr.length; i++) {
                        SCEDetail objSCEDetail = objSCEDetailArr[i];
                        objSCEDetail.setSceId(objSCE.getId());
                        sceControl.updateSCEDetail(objSCEDetail);                                                    
                    }          
                }
                
                if (legalDetailArr != null) {
                    for (int i=0; i<legalDetailArr.length; i++) {
                        LegalQuestion legalDetail = legalDetailArr[i];
                        legalDetail.setSCEId(objSCE.getId());
                        Integer id = legalDetail.getId();
                        String legalQuestionValue = legalDetail.getLegalQuestionValue();
                        sceControl.updateLegalDetail(sceId,id,legalQuestionValue);
                        }          
                    }
            }            
        }
    
    }catch (SQLException sqle) {
            //log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
            //log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
        
    }
    
    public boolean checkIfDuplicate(SCE objSCE, SCEDetail[] objSCEDetailArr) throws SCEException
    {
        if (objSCE != null) {
            // Get Last Submitted SCE
            SCE oSCE = getLatestSCE(objSCE.getEmplId(), objSCE.getEventId(), objSCE.getProductCode(), objSCE.getStatus());
            if (oSCE != null) {
               // Check SCE
                
               if (
                    ( (oSCE.getTemplateVersionId() == null && objSCE.getTemplateVersionId() == null) || (oSCE.getTemplateVersionId() != null && oSCE.getTemplateVersionId().equals(objSCE.getTemplateVersionId())) )
                    && ( (oSCE.getPrecallRating() == null && objSCE.getPrecallRating() == null) || (oSCE.getPrecallRating() != null && oSCE.getPrecallRating().equals(objSCE.getPrecallRating())) )
                    && ( (oSCE.getPrecallComments() == null && objSCE.getPrecallComments() == null) || (oSCE.getPrecallComments() != null && oSCE.getPrecallComments().equals(objSCE.getPrecallComments())) )
                    && ( (oSCE.getPostcallRating() == null && objSCE.getPostcallRating() == null) || (oSCE.getPostcallRating() != null && oSCE.getPostcallRating().equals(objSCE.getPostcallRating())) )
                    && ( (oSCE.getPostcallComments() == null && objSCE.getPostcallComments() == null) || (oSCE.getPostcallComments() != null && oSCE.getPostcallComments().equals(objSCE.getPostcallComments())) )
                    && ( (oSCE.getComments() == null && objSCE.getComments() == null) || (oSCE.getComments() != null && oSCE.getComments().equals(objSCE.getComments())) )
                    && ( (oSCE.getOverallRating() == null && objSCE.getOverallRating() == null) || (oSCE.getOverallRating() != null && oSCE.getOverallRating().equals(objSCE.getOverallRating())) )
                    && ( (oSCE.getOverallComments() == null && objSCE.getOverallComments() == null) || (oSCE.getOverallComments() != null && oSCE.getOverallComments().equals(objSCE.getOverallComments())) )
                    /*&& ( (oSCE.getOverallScore() == null && objSCE.getOverallScore() == null) || (oSCE.getOverallScore() != null && oSCE.getOverallScore().equals(objSCE.getOverallScore())) )*/
                    )
                {
                    // Might Be A Duplicate                    
                }
                else {
                    return false;
                }
                SCEDetail[] oSCEDetailArr =  sceControl.getSCEDetailsBySCEId(oSCE.getId());
                if (oSCEDetailArr != null && objSCEDetailArr != null) {
                    if (oSCEDetailArr.length != objSCEDetailArr.length) { 
                        return false;
                    }
                    for (int i=0; i<oSCEDetailArr.length; i++) {
                        for (int j=0; j<oSCEDetailArr.length; j++) {
                            if (oSCEDetailArr[i].getQuestionId() != null && oSCEDetailArr[i].getQuestionId().equals(objSCEDetailArr[j].getQuestionId())) {
                                // Compare Now
                            	if (
                                    ( (oSCEDetailArr[i].getQuestionRating() == null && objSCEDetailArr[j].getQuestionRating() == null) || (oSCEDetailArr[i].getQuestionRating() != null && oSCEDetailArr[i].getQuestionRating().equals(objSCEDetailArr[j].getQuestionRating())) )
                                    && ( (oSCEDetailArr[i].getQuestionComments() == null && objSCEDetailArr[j].getQuestionComments() == null) || (oSCEDetailArr[i].getQuestionComments() != null && oSCEDetailArr[i].getQuestionComments().equals(objSCEDetailArr[j].getQuestionComments())) )
                                    && ( (oSCEDetailArr[i].getQuestionScore1() == null && objSCEDetailArr[j].getQuestionScore1() == null) || (oSCEDetailArr[i].getQuestionScore1() != null && oSCEDetailArr[i].getQuestionScore1().equals(objSCEDetailArr[j].getQuestionScore1())) )
                                    && ( (oSCEDetailArr[i].getQuestionScore2() == null && objSCEDetailArr[j].getQuestionScore2() == null) || (oSCEDetailArr[i].getQuestionScore2() != null && oSCEDetailArr[i].getQuestionScore2().equals(objSCEDetailArr[j].getQuestionScore2())) )
                                    && ( (oSCEDetailArr[i].getQuestionScore3() == null && objSCEDetailArr[j].getQuestionScore3() == null) || (oSCEDetailArr[i].getQuestionScore3() != null && oSCEDetailArr[i].getQuestionScore3().equals(objSCEDetailArr[j].getQuestionScore3())) )
                                    )
                                {
                                }
                                else {
                                    return false;
                                }
                                
                                break; 
                            }
                            
                        }    
                    }
                }
            }
            else {
                return false;
            }
        }
        return true;
    }
    
    
    public SCE getLatestSCE(String emplId, Integer eventId, String productCode, String status) throws SCEException
    {
        try{
        Integer evaluationTemplateId;
        
        SCE objSCE = sceControl.getLatestSCE(emplId, eventId, productCode, status);
        
        if (objSCE != null) {
        evaluationTemplateId= objSCE.getTemplateVersionId();
        
        BusinessRule[] objBusinessRulesArr =  sceControl.getBusinessRules(evaluationTemplateId);
        
        LegalQuestion[] objLegalQuestionArr =  sceControl.getLegalQuestion(evaluationTemplateId);  
        
       // EmailTemplate[] objEmailTemplateArr =  sceControl.getEmailTemplate(evaluationTemplateId);
        
        EvaluationFormScore[] objEvaluationFormScoreArr =  sceControl.getEvaluationFormScore(evaluationTemplateId);

        
            SCEDetail[] objSCEDetailArr =  sceControl.getSCEDetailsBySCEId(objSCE.getId());
            if (objSCEDetailArr != null) {
                Double totalScore = null;
                boolean hasTotal = false;
                double tmp = 0.0;
                for (int i=0; i<objSCEDetailArr.length; i++) {
                    if (SCEConstants.QT_SCORE.equalsIgnoreCase(objSCEDetailArr[i].getType()) || (SCEConstants.QT_SCORE_FETCH.equalsIgnoreCase(objSCEDetailArr[i].getType()) && SCEConstants.ST_SUBMITTED.equalsIgnoreCase(objSCE.getStatus()))) {
                        objSCEDetailArr[i].setAverageScore(objSCEDetailArr[i].computeAverageScore());                    
                        if (objSCEDetailArr[i].getWeightedScore() != null) {
                            tmp += objSCEDetailArr[i].getWeightedScore().doubleValue();
                            hasTotal = true;
                        }
                    }
                }
                if (hasTotal) {
                    totalScore = new Double(tmp);
                }
                if (SCEConstants.ST_SUBMITTED.equalsIgnoreCase(objSCE.getStatus())) {
                    
                }
                else {
                    objSCE.setOverallScore(totalScore);
                }
            }
            
            
            // Get The Descriptors For All The Questions and Assign Them
            Descriptor[] objDescriptorArr = sceControl.getDescriptorsBySCEId(objSCE.getId());
            if (objDescriptorArr != null && objSCEDetailArr != null) {
                for (int i=0; i<objDescriptorArr.length; i++) {
                    for (int j=0; j<objSCEDetailArr.length; j++) {
                        if (objDescriptorArr[i].getQuestionId().equals(objSCEDetailArr[j].getQuestionId())) {
                            objSCEDetailArr[j].addDescriptorList(objDescriptorArr[i]);
                            break;
                        }
                    }
                }
            }
            
            if (objSCEDetailArr != null) {
                for (int i=0; i<objSCEDetailArr.length; i++) {
                    if (i==0) {
                        objSCE.setTemplateVersionId(objSCEDetailArr[i].getTemplateVersionId());
                        objSCE.setFormTitle(objSCEDetailArr[i].getFormTitle());
                        objSCE.setEventId(eventId);
                        objSCE.setPrecallComments(objSCEDetailArr[i].getPrecallComments());
                        objSCE.setPostcallComments(objSCEDetailArr[i].getPostcallComments());                
                        objSCE.setHlcCritical(objSCEDetailArr[i].getHlcCritical());
                        objSCE.setLegalFG(objSCEDetailArr[i].getLegalFG());
                        objSCE.setConflictOverallScore(objSCEDetailArr[i].getConflictOverallScore());
                        objSCE.setHLCCriticalValue(objSCEDetailArr[i].getHLCCriticalValue());
                    }
                    objSCE.addSceDetailList(objSCEDetailArr[i]); 
                }
                
                for (int i=0; i<objBusinessRulesArr.length; i++) {
                    objSCE.addBusinessRulesList(objBusinessRulesArr[i]);            
                }
                
                for (int i=0; i<objEvaluationFormScoreArr.length; i++) {
                    objSCE.addEvaluationFormScoreList(objEvaluationFormScoreArr[i]);
                }
                
                
                for (int i=0; i<objLegalQuestionArr.length; i++) {
                    objSCE.addLegalQuestionList(objLegalQuestionArr[i]);
                }
            }
        }
         return objSCE;
        
        }catch (SQLException sqle) {
            //log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
            //log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
       
    }
    
    public EvaluationFormScore getEvaluationFormValues(Integer templateVersionId, String overallScore) throws SCEException
    {    
         EvaluationFormScore evaluationFormScore = null;
        
        try{
            evaluationFormScore = sceControl.getEvaluationFormValues(templateVersionId,overallScore);
        
        }catch (SQLException sqle) {
        //log.error(LoggerHelper.getStackTrace(sqle));
        throw new SCEException("error.sce.database.communication",sqle);
        }catch (Exception e) {
        //log.error(LoggerHelper.getStackTrace(e));
        throw new SCEException("error.sce.unknown",e);
        }
        return evaluationFormScore;
    }
    
    
    public  Blob getBlankForm(Integer templateVersionId)  throws SCEException
    {   Blob blankForm = null;
      
      try{
            blankForm = sceControl.getBlankForm(templateVersionId);
      }catch (SQLException sqle) {
            //log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
            //log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
   return blankForm;
      
    } 
    
    public  Integer getTemplateVersionId(Integer templateId)  throws SCEException
    {   Integer templateVersionId = null;
      
      try{
            templateVersionId = sceControl.getTemplateVersionId(templateId);
      }catch (SQLException sqle) {
           // log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
	  }catch (Exception e) {
            //log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
      }
      return templateVersionId;
      
    }  
    
    public HashMap getAllScores(Integer templateVersionId) throws SCEException
    {   HashMap scoreList = null;
        try{
       // templateVersionId=new Integer(10);
        ScoringSystem[] scoringSytem=sceControl.getAllScores(templateVersionId);
        scoreList = new LinkedHashMap();
        if (scoringSytem != null && scoringSytem.length > 0) {
         for (int i=0; i<scoringSytem.length; i++){
            scoreList.put("","");
            scoreList.put(scoringSytem[i].getScoreLegend(),scoringSytem[i].getScoreLegend()+" "+scoringSytem[i].getScoreValue());
         }
        }
        }catch (SQLException sqle) {
            //log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
            //log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
        return scoreList;
    }
    
    public Integer fetchVersionNumber(Integer templateVersionId) throws SCEException{
        int versionNumber = 0;
        try{ 
        versionNumber = sceControl.fetchVersionNumber(templateVersionId);
       
       }catch (SQLException sqle) {
            //log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
            //log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
        return versionNumber;
    }
    
    public String getProductName(String productCode) throws SCEException
    {
        String product=null;
        try{
        product=sceControl.getProductName(productCode);
        }catch (SQLException sqle) {
            //log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
            //log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
        return product;
    }
    
    public void uploadForm(com.pfizer.sce.beans.EvaluationForm evalForm,java.lang.Integer sceId) throws SCEException{
        // System.out.println("Inside IMPL");
        try{
            sceControl.uploadForm(evalForm,sceId);
        }catch (SQLException sqle) {
            //log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
            //log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
        // System.out.println("outside IMPL");
    }

    
    /* GADALP ENDS HERE */
    
    
    
    //monika
   
    public String[] getCoursesForEvent(String event)throws SCEException{
        try{
             return sceControl.getCoursesForEvent(event);
           
          }catch (SQLException sqle) {
            //   log.error(LoggerHelper.getStackTrace(sqle));
               throw new SCEException("error.sce.database.communication",sqle);
   		}catch (Exception e) {
          //     log.error(LoggerHelper.getStackTrace(e));
               throw new SCEException("error.sce.unknown",e);
           }
    }
    public Learner[] getLearnersByCourseAndDate(String course,String frmDate,String toDate)throws SCEException{
    	  try{
    	         return sceControl.getLearnersByCourseAndDate(course,frmDate,toDate);
    	        
    	       }catch (SQLException sqle) {
    	           // log.error(LoggerHelper.getStackTrace(sqle));
    	            throw new SCEException("error.sce.database.communication",sqle);
    			}catch (Exception e) {
    	          //  log.error(LoggerHelper.getStackTrace(e));
    	            throw new SCEException("error.sce.unknown",e);
    	        }
    	     }
   
    public void addNewLearner(Learner learner)throws SCEException{
        try{
            sceControl.addNewLearner(learner);
          
         }catch (Exception e) {
          //    log.error(LoggerHelper.getStackTrace(e));
              throw new SCEException("error.sce.unknown",e);
          }
  }

    
    
    public com.pfizer.sce.beans.Learner getLearnerByEmailCourse(String email,String course) throws SCEException{
       try{
         return sceControl.getLearnerByEmailCourse(email,course);
        
       }catch (SQLException sqle) {
           // log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
           // log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }  
    }
    //end monika
    
    /*CHANGES KHATED START*/
	public EventsCreated[] getAllActiveInprogressEvents() {
		 EventsCreated[] events= sceControl.getAllActiveInprogressEvents();
	      return events;
	}




	public void addWebExDetails(WebExDetails webExDetails) throws SCEException{
		try{
	        sceControl.addWebExDetails(webExDetails);
	        }
	        catch (SQLException sqle) {
	           // log.error(LoggerHelper.getStackTrace(sqle));
	            throw new SCEException("error.sce.database.communication",sqle);
			}
	         catch(Exception e)
	         {
	            //log.error(LoggerHelper.getStackTrace(e));
	            throw new SCEException("error.sce.unknown",e);
	        }
		
	}
	
	/*CHANGES KHATED END*/
	
	/*Megha*/
	
	public void updateUserAcceptInvite(String eventName,String emailSel)throws SCEException{
	    try{
	    GuestTrainer trainers = sceControl.getGTforAccess(emailSel);
	    String ntID = trainers.getNtid(); 
	                                      /* SQLParameter[] params = new SQLParameter[4];
	                                     params[0] = new SQLParameter(ntID, Types.VARCHAR, SQLParameter.IN);
	                                     params[1] = new SQLParameter(eventName, Types.VARCHAR, SQLParameter.IN);    
	                                     params[2] = new SQLParameter(emailSel, Types.VARCHAR, SQLParameter.IN);  
	                                     params[3] = new SQLParameter("A", Types.VARCHAR, SQLParameter.IN); */ 
	                                     
	                                    sceControl.gotoCallProcToRevoke(ntID,eventName,emailSel,"A");
	    }
	      catch (SQLException sqle) {
	            log.error(LoggerHelper.getStackTrace(sqle));
	            throw new SCEException("error.sce.database.communication",sqle);
			}catch (Exception e) {
	            log.error(LoggerHelper.getStackTrace(e));
	            throw new SCEException("error.sce.unknown",e);
	        }
	 }
	
	public void updateLastEventDate(String product,String trainerEmail,String updateDate) throws SCEException 
	{
	    try {
			sceControl.updateLastEventDate(product,trainerEmail,updateDate);
		}   catch (SQLException sqle) {
		      log.error(LoggerHelper.getStackTrace(sqle));
		      throw new SCEException("error.sce.database.communication",sqle);
			}catch (Exception e) {
		      log.error(LoggerHelper.getStackTrace(e));
		      throw new SCEException("error.sce.unknown",e);
		  }
	 }
	
	public String getDateToUpdate(String eventName,String product,String trainerEmail)throws SCEException{
	    
        try{
      return sceControl.getDateToUpdate(eventName,product,trainerEmail);
   }
    catch (SQLException sqle) {
      log.error(LoggerHelper.getStackTrace(sqle));
      throw new SCEException("error.sce.database.communication",sqle);
	}catch (Exception e) {
      log.error(LoggerHelper.getStackTrace(e));
      throw new SCEException("error.sce.unknown",e);
  }
}
	
	public String[] getProductsForEvent(String event)throws SCEException{
        try{
         return sceControl.getProductsForEvent(event);
      }
       catch (SQLException sqle) {
         log.error(LoggerHelper.getStackTrace(sqle));
         throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
         log.error(LoggerHelper.getStackTrace(e));
         throw new SCEException("error.sce.unknown",e);
     }
}

/*Changes done begin Release2	*/
	/*public void gotoConfirmInvitation(String email,String choice,String selEvent) throws SCEException
	{
	    String selEmail = email;
	    String ifChoice = choice;
	    String event = selEvent;   
	    String sql ="";
	    sql += "update guest_trainer_event SET REP_ISACCEPTED = '"+ ifChoice +"' where REP_EMAIL = '"+ email + "'  and EVENT_NAME ='" + event + "'";;
	   
	    
	    //if GT has rejected the invite its temporary acces is to be invoked
	    if(ifChoice.equalsIgnoreCase("N"))
	    {
	        
	                                 try {     
	                                     String sqlQ = "";
	                                     GuestTrainer trainers = sceControl.getGTforAccess(selEmail);                                   
	                                     String ntID = trainers.getNtid();           
	                                    
	                                     SQLParameter[] params = new SQLParameter[4];
	                                     params[0] = new SQLParameter(ntID, Types.VARCHAR, SQLParameter.IN);
	                                     params[1] = new SQLParameter(event, Types.VARCHAR, SQLParameter.IN);    
	                                     params[2] = new SQLParameter(selEmail, Types.VARCHAR, SQLParameter.IN);  
	                                     params[3] = new SQLParameter("R", Types.VARCHAR, SQLParameter.IN);  
	                                     //This procedure is called for revoking temporary access to GT's who are not in SCE system to enable them to accept or reject invite 
	                                    sceControl.gotoCallProcToRevoke(ntID,event,selEmail,"R");
	             
	                                     } catch (SQLException sqle) {
	                                     log.error(LoggerHelper.getStackTrace(sqle));
	                                     throw new SCEException("error.sce.database.communication",sqle);
	                                     }catch (Exception e) {
	                                     log.error(LoggerHelper.getStackTrace(e));
	                                     throw new SCEException("error.sce.unknown",e);
	                                     } 
	    }
	    
	     sceControl.gotoConfirmEmailSent(sql);
	}*/
	
	
	
	
	public void gotoConfirmInvitation(String email,String choice,String selEvent,String productSel) throws SCEException
	{
	    String selEmail = email;
	    String ifChoice = choice;
	    String event = selEvent;  
	    
	    String product="";
	    product=productSel;
	    String sql ="";
	    sql += "update guest_trainer_event SET REP_ISACCEPTED = '"+ ifChoice +"' where REP_EMAIL = '"+ email + "'  and EVENT_NAME ='" + event + "' and PRODUCT='"+product+"'";
	   
	   /* sql +=  " update guest_trainer_event SET REP_ISACCEPTED = '"+ ifChoice +"',PRODUCT='"+product+"' " 
                +" where REP_EMAIL = '"+ email + "'  "
                +" and EVENT_NAME ='" + event + "' "
                +" and ( PRODUCT is null  or PRODUCT = '') "
                +" and ( REP_ISACCEPTED is null  or REP_ISACCEPTED = '')"
                +" and MAP_ID = ("
                +"                select max(MAP_ID) from guest_trainer_event where REP_EMAIL = '"+ email + "'"  
                +"                and EVENT_NAME ='" + event + "' "
                +"                and ( PRODUCT is null  or PRODUCT = '') "
                +"                and ( REP_ISACCEPTED is null or REP_ISACCEPTED = '')"
                +"              )" ;
*/
	   System.out.println(sql);
	    //if GT has rejected the invite its temporary acces is to be invoked
	    if(ifChoice.equalsIgnoreCase("N"))
	    {
	        
	                                 try {     
	                                     String sqlQ = "";
	                                     GuestTrainer trainers = sceControl.getGTforAccess(selEmail);                                   
	                                     String ntID = trainers.getNtid();           
	                                    
	                                     /*SQLParameter[] params = new SQLParameter[4];
	                                     params[0] = new SQLParameter(ntID, Types.VARCHAR, SQLParameter.IN);
	                                     params[1] = new SQLParameter(event, Types.VARCHAR, SQLParameter.IN);    
	                                     params[2] = new SQLParameter(selEmail, Types.VARCHAR, SQLParameter.IN);  
	                                     params[3] = new SQLParameter("R", Types.VARCHAR, SQLParameter.IN);  */
	                                     //This procedure is called for revoking temporary access to GT's who are not in SCE system to enable them to accept or reject invite 
	                                    sceControl.gotoCallProcToRevoke(ntID,event,selEmail,"R");
	             
	                                     } catch (SQLException sqle) {
	                                     log.error(LoggerHelper.getStackTrace(sqle));
	                                     throw new SCEException("error.sce.database.communication",sqle);
	                                     }catch (Exception e) {
	                                     log.error(LoggerHelper.getStackTrace(e));
	                                     throw new SCEException("error.sce.unknown",e);
	                                     } 
	    }
	    
	     sceControl.gotoConfirmEmailSent(sql);
	}

	/*Changes done end Release2	*/
	
	
	/*
	 * Added by ankit on 17 june 
	 */
			
	public void acceptGTinviteManual(String email,String choice,String selEvent,String productSel) throws SCEException
	{
	    String selEmail = email;
	    String ifChoice = choice;
	    String event = selEvent;  
	    String product="";
	    product=productSel;
	    String sql ="";
	    
	    
//	    sql += "update guest_trainer_event SET REP_ISACCEPTED = '"+ ifChoice +"' where REP_EMAIL = '"+ email + "'  and EVENT_NAME ='" + event + "' and PRODUCT='"+product+"'";
	    
	    
	    sql += "update guest_trainer_event SET REP_ISACCEPTED = '"+ ifChoice +"' where REP_EMAIL IN (" + email + ") and EVENT_NAME ='" + event + "' and PRODUCT='"+product+"'";

	    
	  /*  sql +=  " update guest_trainer_event SET REP_ISACCEPTED = '"+ ifChoice +"',PRODUCT='"+product+"' " 
	    	    +" where REP_EMAIL IN (" + email + ") "
	    	    +" and EVENT_NAME ='" + event + "' "
	    	    +" and ( PRODUCT is null  or PRODUCT = '') "
	    	    +" and ( REP_ISACCEPTED is null  or REP_ISACCEPTED != '"+ ifChoice +"') "
	    	    +" and MAP_ID IN ("
	    	    +"                select max(MAP_ID) from guest_trainer_event where REP_EMAIL IN (" + email + ") "  
	    	    +"                and EVENT_NAME ='" + event + "' "
	    	    +"                and ( PRODUCT is null  or PRODUCT = '') "
	    	    +"                and ( REP_ISACCEPTED is null or REP_ISACCEPTED != '"+ ifChoice +"') group by REP_EMAIL   " 
	    	    +"              )" ;*/

	   System.out.println(sql);
	     sceControl.gotoConfirmEmailSent(sql);
	}
	
	
	/*
	 * End 
	 */
	
	
	
	
	
	 public String[] getGTemailByNTID(String ntid)
	 {
	     return sceControl.getGTemailByNTID(ntid);
	 }
	 public String getNtidByEmail(String email)
	 {
	     return sceControl.getNtidByEmail(email);
	 }
	 /*
	  * Added By Ankit on 4 September 2015 for fetching Map_id from GUEST_TRAINER_EVENT
	  */
	 
	 public String getMapidForTrainer(String email,String product,String event)
	 {
	     return sceControl.getMapidForTrainer(email,product,event);
	 }
	 
	 /*
	  * End
	  */
	 public void saveDateTimeSlots(String[][] dateTimeArray,String MapId,List<Integer> hourList,String email,String event,String product) throws Exception
	 {
	     try
	     {
			sceControl.saveDateTimeSlots(dateTimeArray,MapId,hourList,email,event,product);
		}
	     catch (Exception e) 
	     {
			// TODO Auto-generated catch block
			System.out.println("Exception Occured...."+e.getCause());
			throw e;
		}
	 }
	 
	 
	 public String checkInviteResponse(String ntid,String eventName,String product)
	 {
	     return sceControl.checkInviteResponse(ntid,eventName,product);
	 }
	
	public void gotoConfirmEmailSent(String email,String event,String productName) throws SCEException      
	{ 
		  
	   String selEmail = email;
	   String eventName = event;
	   String[] x = null;
	   x =  selEmail.split(";");
	    
	           for(int i=0; i<x.length; i++)
	                        {
	           
	                          String emailSel= x[i];
	                          String sql ="";
	                          //This is for mapping GT to a particular event and store its statuses corresponding to it
	                          /*sanjeev send product*/
	                          sql += "INSERT INTO guest_trainer_event (REP_EMAIL_ISSENT,REP_EMAIL,PRODUCT,EVENT_NAME,MAP_ID) VALUES ('Y','"+emailSel+"', '"+productName+ "','"+eventName+ "' ,trainer_event_mapping_id.nextval)";
	                          sceControl.gotoConfirmEmailSent(sql);
	   
	                                try {     
	                                    String sqlQ = "";
	                                    GuestTrainer trainers = sceControl.getGTforAccess(emailSel);
	                                    String ntID = trainers.getNtid();           
	                                    String fName = trainers.getFname();
	                                    String lName = trainers.getLname();
	                                    Date startDate = sceControl.getEventDate(eventName);  
	          
	                                    /*SQLParameter[] params = new SQLParameter[5];
	                                    params[0] = new SQLParameter(fName, Types.VARCHAR, SQLParameter.IN);
	                                    params[1] = new SQLParameter(lName, Types.VARCHAR, SQLParameter.IN);    
	                                    params[2] = new SQLParameter(emailSel, Types.VARCHAR, SQLParameter.IN);  
	                                    params[3] = new SQLParameter(ntID, Types.VARCHAR, SQLParameter.IN);  
	                                    params[4] = new SQLParameter(startDate, Types.DATE, SQLParameter.IN); */      
	                                    //This procedure is called for giving temporary access to GT's who are not in SCE system to enable them to accept or reject invite 
	                                    sceControl. gotoCallProcToAccess(fName,lName,emailSel,ntID,startDate);;
	            
	                                    } catch (SQLException sqle) {
	                                    //log.error(LoggerHelper.getStackTrace(sqle));
	                                    throw new SCEException("error.sce.database.communication",sqle);
	                                    }catch (Exception e) {
	                                    //log.error(LoggerHelper.getStackTrace(e));
	                                    throw new SCEException("error.sce.unknown",e);
	                                    }                      
	                      }      //end for loop
	   }
	   
	   
	   
	public void updateTrainerLearnerMappingEmailSent(String eventName,String courseStartDate,String learnerEmailList,String productName) throws SQLException, SCEException
	{
		    String[] learnerEmail=learnerEmailList.split(";");
		    try
		    {
		    for(int i=0;i<(learnerEmail.length-1);i++)
		    {
		        sceControl.updateTrainerLearnerMappingEmailSent(eventName,courseStartDate,learnerEmail[i],productName);
		    }
		    }
		   catch (Exception e) {
	         //log.error(LoggerHelper.getStackTrace(e));
	         throw new SCEException("error.sce.unknown",e);
		    } 

	}
	
/*Changes begin Release2*/
	/*public GuestTrainer[] getGTByEvent(String event) throws SCEException {
		try {
			return sceControl.getGTByEvent(event);

		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
	}*/
	
	public GuestTrainer[] getGTByEvent(String event,String product) throws SCEException {
		try {
			return sceControl.getGTByEvent(event,product);

		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
	}

	/*Changes end Release2*/
	
	/**Added by ankit on 4 june**/
	public String[] getEventProducts(String event) throws SCEException {
		try {
			return sceControl.getEventProducts(event);

		
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
	}
	/**End by ankit**/

	public EventsCreated getEventByName(String name) throws SCEException {
		EventsCreated events1 = null;
		try {

			events1 = sceControl.getEventByName(name);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		return events1;
	}

	public void unpublishOlderVersion() throws SCEException {
		try {
			sceControl.unpublishOlderVersion();
		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
	}

	public void publishLegalTemplate(LegalConsentTemplate legalConsentTemplate)
			throws SCEException {
		try {
			sceControl.publishLegalTemplate(legalConsentTemplate);
		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}

	}

	public LegalConsentTemplate getPublishedVersion() throws SCEException {
		LegalConsentTemplate publishedVersion = null;
		try {
			publishedVersion = sceControl.getPublishedVersion();

		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		return publishedVersion;
	}

	public LegalConsentTemplate[] getAllLegalTemplates() throws SCEException {
		LegalConsentTemplate[] allLegalTemplates = null;
		try {
			allLegalTemplates = sceControl.getAllLegalTemplates();
		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		return allLegalTemplates;
	}

	public int getMaxVersion() throws SCEException {
		int maxVersion = 0;
		try {
			maxVersion = sceControl.getMaxVersion();
		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		return maxVersion;
	}

	public LegalConsentTemplate getLegalTemplateById(int lcId)
			throws SCEException {
		LegalConsentTemplate legalConsentTemplate = null;
		try {
			legalConsentTemplate = sceControl.getLegalTemplateById(lcId);
			// System.out.println("publish flag"+ legalConsentTemplate.getPublishFlag());

		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		return legalConsentTemplate;
	}

	public int getMaxLcId() throws SCEException {
		int maxLcId = 0;
		try {
			maxLcId = sceControl.getMaxLcId();
		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		return maxLcId;
	}

	public void createNewVersion(LegalConsentTemplate legalConsentTemplate)
			throws SCEException {
		try {
			sceControl.createNewVersion(legalConsentTemplate);
		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
	}

	public void overWriteVersion(LegalConsentTemplate legalConsentTemplate)
			throws SCEException {
		try {
			sceControl.overWriteVersion(legalConsentTemplate);
		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
	}
	
	/*Megha end*/
	
	//madhuri start
	

	public TemplateVersion[] getAllTemplateVersions() throws SCEException {
		// System.out.println("inside the get all template versions");
		TemplateVersion[] tv = null;
		try {
			tv = sceControl.getAllTemplateVersions();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		}
		return tv;
	}
	public ScoringSystem[] getScoringSystem() {
		
		// System.out.println("Inside SCEManagerImpl's getScoringSystem method...");

		ScoringSystem[] scoringSystem = null;

		scoringSystem = sceControl.getScoringSystem();

		return scoringSystem;
	}

	public TemplateVersion getTemplateVersionByIdNew(Integer templateVersionId)
			throws SCEException {
		TemplateVersion templateVersion = null;
		try {
			// System.out.println("Inside SCEManagerImpl - getTemplateVersionByIdNew method ...");
			templateVersion = sceControl.getTemplateVersionByIdNew(templateVersionId);
			
		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		return templateVersion;

	}

	public Question[] getQuestionsByTemplateVersionId(Integer templateVersionId) {
		Question[] objQuestionArr = sceControl
				.getQuestionsByTemplateVersionId(templateVersionId);

		// Get The Descriptors For All The Questions and Assign Them
		Descriptor[] objDescriptorArr = sceControl
				.getDescriptorsByTemplateVersionId(templateVersionId);
		if (objDescriptorArr != null && objQuestionArr != null) {
			for (int i = 0; i < objDescriptorArr.length; i++) {
				for (int j = 0; j < objQuestionArr.length; j++) {

					if (objDescriptorArr[i].getQuestionId().equals(
							objQuestionArr[j].getId())) {
						objQuestionArr[j]
								.addDescriptorList(objDescriptorArr[i]);
						break;
					}
				}
			}
		}
		return objQuestionArr;
	}

	public BusinessRule[] getBusinessRulesByTemplateVersionId(
			Integer templateVersionId) throws SCEException {
		BusinessRule[] businessRules = null;
		try {
			businessRules = sceControl
					.getBusinessRulesByTemplateVersionId(templateVersionId);
		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		return businessRules;
	}

	public LegalQuestion[] getLegalQuestionsByTemplateVersionId(
			Integer templateVersionId) throws SCEException {
		LegalQuestion[] legalQuestions = null;
		try {
			legalQuestions = sceControl
					.getLegalQuestionsByTemplateVersionId(templateVersionId);
		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		return legalQuestions;
	}

	public EvaluationFormScore[] getEvaluationFormScoresByTemplateVersionId(
			Integer templateVersionId) throws SCEException {
		EvaluationFormScore[] evaluationFormScores = null;
		try {
			evaluationFormScores = sceControl
					.getEvaluationFormScoresByTemplateVersionId(templateVersionId);
			for (int count = 0; count < evaluationFormScores.length; count++) {
				EvaluationFormScore evaluationFormScore = evaluationFormScores[count];
				String scoreValue = evaluationFormScore.getScoreValue();
				// System.out.println("the score value is " + scoreValue);
				String scoringSystemIdentifier = evaluationFormScore
						.getScoringSystemIdentifier();
				// System.out.println("the scoring system identifier is"+ scoringSystemIdentifier);
				String emailPublishFlag = sceControl.getEmailPublishFlag(
						templateVersionId, scoreValue, scoringSystemIdentifier);
				// System.out.println("the email publish flag is"+ emailPublishFlag);
				evaluationFormScore.setEmailPublishFlag(emailPublishFlag);
			}
		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			sqle.printStackTrace();
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		}
		return evaluationFormScores;
	}

	public Integer getNumOfFormTitles(String formTitle) throws SCEException {
		Integer numOfFormTitles = null;
		try {
			numOfFormTitles = sceControl.getNumOfFormTitles(formTitle);
		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		return numOfFormTitles;

	}

	public Integer findLMSCourseMappingId(Integer templateId)
			throws SCEException {
		Integer countOfLmsMappingIds = new Integer(0);
		try {
			countOfLmsMappingIds = sceControl
					.findLMSCourseMappingId(templateId);
		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		return countOfLmsMappingIds;
	}

	public void saveTemplateVersionNew(TemplateVersion templateVersion,
			String pageName, boolean isModified, boolean isAlreadyMapped) throws SCEException {

		Integer templateVersionId = null;
		Integer templateId = null;

		try {
			if (pageName.equals("edit")) 
			{
				templateVersionId = templateVersion.getId();
				// System.out.println("templateVersion.getPublishFlag:"+ templateVersion.getPublishFlag());
				
				if (isAlreadyMapped == true && isModified == true && templateVersion.getPublishFlag().equals("Y")) 
				{
					// Get New Template Version Id

					templateVersionId = sceControl.getNextTemplateVersionId();

					templateVersion.setId(templateVersionId);

					Integer currentTemplateVersion = sceControl.getCurrentVersion(templateVersion.getTemplateId());

					int version = currentTemplateVersion.intValue();

					version++;

					templateVersion.setPublishFlag("N");

					templateVersion.setVersion(new Integer(version));

					sceControl.saveTemplateVersionNew(templateVersion);

				} 
				else 
				{

					// Remove all template related records from database

					sceControl.deleteBusinessRules(templateVersionId);

					sceControl.deleteEvaluationFormScores(templateVersionId);

					if (!(isAlreadyMapped == true && isModified == false)) {

						sceControl.deleteLegalQuestions(templateVersionId);

						sceControl.deleteDescriptors(templateVersionId);

						sceControl.deleteQuestions(templateVersionId);

					}

				}

			} 
			else if (pageName.equals("create")) 
			{

				// Get New Template Version Id

				templateVersionId = sceControl.getNextTemplateVersionId();

				templateVersion.setId(templateVersionId);

				// Get New Template Id

				templateId = sceControl.getNextTemplateId();

				templateVersion.setTemplateId(templateId);

				sceControl.saveTemplate(templateVersion);

				sceControl.saveTemplateVersionNew(templateVersion);

				sceControl.saveTemplateToEventCourse(templateId);

			}

			List questions = (ArrayList) templateVersion.getQuestions();

			List businessRules = (ArrayList) templateVersion.getBusinessRules();

			List legalQuestions = (ArrayList) templateVersion
					.getLegalQuestions();

			List evaluationFormScores = (ArrayList) templateVersion

			.getEvaluationFormScores();

			List descriptorList = null;

			ListIterator litr = null;

			if (!(isAlreadyMapped == true && isModified == false)) {

				litr = questions.listIterator();

				templateVersionId = templateVersion.getId();

				templateId = templateVersion.getTemplateId();

				while (litr.hasNext()) {

					Question question = (Question) litr.next();

					question.setTemplateVersionId(templateVersionId);

					Integer questionId = sceControl.getNextQuestionId();

					question.setId(questionId);

					sceControl.saveQuestion(question);

					descriptorList = question.getDescriptorList();

					ListIterator descriptorListItr = descriptorList
							.listIterator();

					while (descriptorListItr.hasNext()) {

						Descriptor descriptor = (Descriptor) descriptorListItr

						.next();

						descriptor.setQuestionId(questionId);

						sceControl.saveDescriptor(descriptor);

					}

				}

			}

			litr = businessRules.listIterator();

			while (litr.hasNext()) {

				BusinessRule businessRule = (BusinessRule) litr.next();

				businessRule.setTemplateVersionId(templateVersionId);

				Integer businessRuleId = sceControl.getNextBusinessRuleId();

				businessRule.setBusinessRuleId(businessRuleId);

				sceControl.saveBusinessRule(businessRule);

			}

			if (templateVersion.getLegalSectionFlag().equals("N")) {

				legalQuestions.clear();

			}

			if (!(isAlreadyMapped == true && isModified == false)) {

				litr = legalQuestions.listIterator();

				while (litr.hasNext()) {

					LegalQuestion legalQuestion = (LegalQuestion) litr.next();

					legalQuestion.setTemplateVersionId(templateVersionId);

					Integer legalQuestionId = sceControl
							.getNextLegalQuestionId();

					legalQuestion.setId(legalQuestionId);

					sceControl.saveLegalQuestion(legalQuestion);

					// System.out.println("after save");

				}

			}

			litr = evaluationFormScores.listIterator();

			while (litr.hasNext()) {

				EvaluationFormScore evaluationFormScore = (EvaluationFormScore) litr

				.next();

				evaluationFormScore.setTemplateVersionId(templateVersionId);

				Integer evaluationFormScoreId = sceControl

				.getNextEvaluationFormScoreId();

				evaluationFormScore.setId(evaluationFormScoreId);

				String scoreValue = sceControl
						.getScoreLegend(evaluationFormScore.getScoreValue()
								.toLowerCase());

				// System.out.println("ScoreValue1 is: " + scoreValue);

				evaluationFormScore.setScoreLegend(scoreValue);

				evaluationFormScore.setTemplateId(templateVersion
						.getTemplateId());

				String lmsFlag = evaluationFormScore.getLmsFlag();

				// System.out.println("lmsFlag value is: "+ lmsFlag);

				// added by saikat

				// System.out.println("Before score comment flag");

				String scorecommentFlag = evaluationFormScore
						.getScorecommentFlag();

				System.out
						.println("After score comment...Flag value is: "
								+ scorecommentFlag);

				if (lmsFlag == null) {

					evaluationFormScore.setLmsFlag("N");

				}

				sceControl.saveEvaluationFormScore(evaluationFormScore);

				System.out
						.println("After save EvaluationFormScore");

			}

			if ((pageName.equals("edit") && (isAlreadyMapped == true
					&& isModified == true && templateVersion.getPublishFlag()
					.equals("Y")))) {

				sceControl.updateCurrentVersion(

				templateVersion.getTemplateId(), templateVersion

				.getId());

			} else if (pageName.equals("edit")) {

				// System.out.println("isModified:" + isModified);

				if (isModified == true) {

					templateVersion.setPublishFlag("N");

				} else if (isModified == false) {

					// templateVersion.setPublishFlag("Y");

				}
				

				sceControl.updateTemplateVersion(templateVersion);
				
				System.out.println("Updating template table");
				sceControl.updateTemplate(templateVersion);
				System.out.println("Template table updated");
				
			}

		} catch (SQLException sqle) {

			// log.error(LoggerHelper.getStackTrace(sqle));

			throw new SCEException("error.sce.database.communication", sqle);

		} catch (Exception e) {

			// log.error(LoggerHelper.getStackTrace(e));

			throw new SCEException("error.sce.unknown", e);

		}

	}

	public int getUploadFileId()throws SCEException {
		 int UploadFileId = 0;
	        try{
	            // System.out.println("Inside SCEManagerImpl's getUploadFileId method ...");
	             UploadFileId = sceControl.getUploadFileId();
	        }catch (SQLException sqle) {
	            //log.error(LoggerHelper.getStackTrace(sqle));
	            throw new SCEException("error.sce.database.communication",sqle);
			}catch (Exception e) {
	           // log.error(LoggerHelper.getStackTrace(e));
	            throw new SCEException("error.sce.unknown",e);
	        }
	        return UploadFileId;
	}

	public void uploadBlankForm(UploadBlankForm uploadBlankForm)throws SCEException{
        try{
            sceControl.uploadBlankForm(uploadBlankForm);
        }catch (SQLException sqle) {
            //log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
           // log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
   }

	
    public void publishEvaluationTemplate(UploadBlankForm uploadBlankForm)  throws SCEException{
        try{
            // System.out.println("Inside SCEManagerImpl's publishEvaluationTemplate method ..."); 
     sceControl.publishEvaluationTemplate(uploadBlankForm);
        }catch (SQLException sqle) {
            //log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
           // log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
   }

    public EmailTemplate checkEmailTemplate(String scoringSystemIdentifier,String templateName)throws SCEException{
        EmailTemplate emailTemplate=null;
        try{
            emailTemplate=sceControl.checkEmailTemplate(scoringSystemIdentifier,templateName);
        }catch(SQLException sqle){
            //log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
           // log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
        return emailTemplate;
    }

    public String[] getScoreValues(String scoringSystemIdentifier) throws SCEException{
        String[] scoreValues = null;
        try{
            scoreValues = sceControl.getScoreValues(scoringSystemIdentifier);
        }catch (SQLException sqle) {
           // log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
           // log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
        return scoreValues;
    }

    public Integer getNextValEmailTemplateId()  throws SCEException{
        Integer emailTemplateId = new Integer(0);
        try{
            emailTemplateId = sceControl.getNextValEmailTemplateId();
        }catch (SQLException sqle) {
          //  log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
           // log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
    return emailTemplateId;
   }
   

 public void insertIntoEmailTemplate(TemplateVersion templateVersion,Integer templateVersionId,Integer emailTemplateId,String score_value) throws SCEException{
        
        
        try{
            sceControl.insertIntoEmailTemplate(templateVersion, templateVersionId, emailTemplateId,score_value);
        }catch (SQLException sqle) {
                //log.error(LoggerHelper.getStackTrace(sqle));
                throw new SCEException("error.sce.database.communication",sqle);
         }catch (Exception e) {
              //  log.error(LoggerHelper.getStackTrace(e));
                throw new SCEException("error.sce.unknown",e);
         }
   }

 public void updateEmailTemplate(String templateName,String scoringSystemIdentifier,Integer templateVersionId) {
	    
	    try{
	        sceControl.updateEmailTemplateVersion(templateName,scoringSystemIdentifier,templateVersionId);
	    }
	    catch (SQLException sqle) {
	          //  log.error(LoggerHelper.getStackTrace(sqle));
	            //throw new SCEException("error.sce.database.communication",sqle);
			}catch (Exception e) {
	           // log.error(LoggerHelper.getStackTrace(e));
	           // throw new SCEException("error.sce.unknown",e);
	        }
	    
	    
	    }
	
	
	//madhuri end
	
//Apoorva start
 
 public void insertSCEComment(String sceId,String activity_id,String phaseNo,String rep_id,String comment1,String comment2,String comment3,String enteredby1,String enteredby2,String enteredby3,String date1,String date2,String date3,String submitted_by,String submitted_date)throws SCEException, SQLException
 {
	 try
	 {
     sceControl.insertSCEComment(sceId,activity_id,phaseNo,rep_id,comment1,comment2,comment3,enteredby1,enteredby2,enteredby3,date1,date2,date3,submitted_by,submitted_date);
 }
 catch (Exception e) {
     //log.error(LoggerHelper.getStackTrace(e));
     throw new SCEException("error.sce.unknown",e);
 }  
 }
 
 public void updateSCEComment(String sceId,String activity_id,String phaseNo,String rep_id,String comment1,String comment2,String comment3,String enteredby1,String enteredby2,String enteredby3,String date1,String date2,String date3,String submitted_by,String submitted_date)throws SCEException, SQLException
 {
	 try
	 {
     sceControl.updateSCEComment(sceId,activity_id,phaseNo,rep_id,comment1,comment2,comment3,enteredby1,enteredby2,enteredby3,date1,date2,date3,submitted_by,submitted_date);
	 }
     catch (Exception e) {
         //log.error(LoggerHelper.getStackTrace(e));
         throw new SCEException("error.sce.unknown",e);
     } 
}

 public SCEComment getExistingSCEComment(String sceid,String activityid,String repid)throws SCEException, SQLException
 {
	 try
	 {
     return sceControl.getExistingSCEComment(sceid,activityid,repid);
	 }
     catch (Exception e) {
         //log.error(LoggerHelper.getStackTrace(e));
         throw new SCEException("error.sce.unknown",e);
     }
 }
 
 public Integer isSCEComment(String phaseid,String sceid,String repid) throws SCEException, SQLException   
 {       
     try {
		return sceControl.isSCEComment(phaseid,sceid,repid);
	} 
     catch (Exception e) {
         //log.error(LoggerHelper.getStackTrace(e));
         throw new SCEException("error.sce.unknown",e);
     }
 }
 
 
 public String getSceFeedbackId(String trackid)throws SCEException, SQLException
 {
	 try
	 {
     System.out.println("getSceFeedbackId------"+trackid);
     System.out.println("getSceFeedbackId------2--"+sceControl.getSceFeedbackId(trackid));
     return sceControl.getSceFeedbackId(trackid);
	 }
     catch (Exception e) {
         //log.error(LoggerHelper.getStackTrace(e));
         throw new SCEException("error.sce.unknown",e);
     }
 }
 
 public SCEComment[] getSCEComment(String trackid,String repid) throws SCEException, SQLException   
 {   
	 try
	 {
     return sceControl.getSCEComment(trackid,repid);
	 }
	 catch (Exception e) {
         //log.error(LoggerHelper.getStackTrace(e));
         throw new SCEException("error.sce.unknown",e);
     }
 }
 
 public CourseEvalTemplateMapping[] deleteSelLmsRecord(Integer delLmsId, Integer selTemplateId)  throws SCEException, SQLException{
 	 CourseEvalTemplateMapping[] lmsMappings = null;
     try{
          
     	sceControl.deleteSelLmsRecord(delLmsId, selTemplateId);
     	lmsMappings = getAllMappingsForTemplate(selTemplateId);
        
     }catch (Exception e) {
         //log.error(LoggerHelper.getStackTrace(e));
         throw new SCEException("error.sce.unknown",e);
     }   
     return lmsMappings;
 }
 public CourseEvalTemplateMapping[] getAllMappingsForTemplate(Integer evalTemplateId)  throws SCEException, SQLException{
     CourseEvalTemplateMapping[] mappingsForTemplates = null;
     try{
          
         mappingsForTemplates = sceControl.getAllMappingsForTemplate(evalTemplateId); 
        
     }catch (Exception e) {
        // log.error(LoggerHelper.getStackTrace(e));
         throw new SCEException("error.sce.unknown",e);
     }
     return mappingsForTemplates;
 }
 
 public CourseEvalTemplateMapping[] saveMappedRecords(String emplId, String excludedActivityPk, String selectedTemplateId, String selectedTemplateName, String selectedCourseIds) throws SQLException, SCEException
 {
 	CourseEvalTemplateMapping[] lmsMappings = null;
     try{
          
     	lmsMappings=sceControl.saveMappedRecords(emplId,excludedActivityPk,selectedTemplateId,selectedTemplateName,selectedCourseIds);
     	
        
     }
     catch (Exception e)
     {
         //log.error(LoggerHelper.getStackTrace(e));
         throw new SCEException("error.sce.unknown",e);
     }   
     return lmsMappings;

 }
 public CourseDetails[] getCourseDetailsForActPk(String[] alreadyMappedRecs) throws SCEException{
     // System.out.println("Inside getCourseDetailsForActPk(String[] alreadyMappedRecs)");
    
     CourseDetails[] unmappedCourses=null;
     
     try{
     //String array alreadyMappedRecs contains String values of Activity PK
     if(alreadyMappedRecs!=null){
         // System.out.println("Initialize the Course Details array");
         unmappedCourses = new CourseDetails[alreadyMappedRecs.length];
         for(int i=0; i<alreadyMappedRecs.length; i++){
           int excludeActPk = Integer.parseInt(alreadyMappedRecs[i]);
           CourseDetails obj = sceControl.getCourseDetailsForActPk(excludeActPk);
           unmappedCourses[i] = obj;
           // System.out.println("Assigned object as array element");
         }
     }
     // System.out.println("Size of unmappedCourses Array JCS"+unmappedCourses.length);
     
     }catch (Exception e) {
         //log.error(LoggerHelper.getStackTrace(e));
         throw new SCEException("error.sce.unknown",e);
     }
     
     return unmappedCourses;
 }
 public CourseEvalTemplateMapping[] getAlreadyMappedCourses(String selCourseIds, String selCourseCodes, Integer selEvalTempId)  throws SCEException{

   	 CourseEvalTemplateMapping[] alreadyMappedCourses = null;
   	 try
   	 {
   		alreadyMappedCourses = sceControl.getAlreadyMappedCourses(selCourseIds,selCourseCodes, selEvalTempId);
   	 }
   	catch (Exception e) {
	            //log.error(LoggerHelper.getStackTrace(e));
	            throw new SCEException("error.sce.unknown",e);
	        }      
	 return alreadyMappedCourses;
	}
 
 public String[] getAllHomeStatesArr()throws SCEException{
 	
 	try{
                  
          return sceControl.getAllHomeStates();
          
          
     }
       catch (SQLException sqle) 
       {
            //log.error(LoggerHelper.getStackTrace(sqle));
            throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) 
		{
            //log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
		
 }
     
     public String[] getProductForEvent() throws SCEException{
         try{
              
                return sceControl.getProductForEvent();
            }
            
           catch (Exception e) {
                //log.error(LoggerHelper.getStackTrace(e));
                throw new SCEException("error.sce.unknown",e);
            }
  }
    public String[] getGtToRemove(String product)throws SQLException, SCEException
    {
 	   try{
            return sceControl.getGtToRemove(product);
            
           }catch (SQLException sqle) {
                //log.error(LoggerHelper.getStackTrace(sqle));
                throw new SCEException("error.sce.database.communication",sqle);
    		}catch (Exception e) {
               // log.error(LoggerHelper.getStackTrace(e));
                throw new SCEException("error.sce.unknown",e);
            }
    }
    public void addNewGT(GuestTrainer guestTrainer) throws SCEException{
        try{ 
        sceControl.addNewGT(guestTrainer);
        sceControl.addNewGTforMapping(guestTrainer);
       
       }catch (Exception e) {
            //log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
        
    }
     public GuestTrainer[] getAllGTForProduct(String product) throws SCEException{
    	 GuestTrainer[] guestTrainers=null;
    	 try{
    		 guestTrainers= sceControl.getAllGTForProduct(product);
         
        }catch (SQLException sqle) {
             //log.error(LoggerHelper.getStackTrace(sqle));
             throw new SCEException("error.sce.database.communication",sqle);
 		}catch (Exception e) {
            // log.error(LoggerHelper.getStackTrace(e));
             throw new SCEException("error.sce.unknown",e);
         }
    	 return guestTrainers;
     } 
     public GuestTrainer[] getGTFromAtlasByProduct(String product) throws SCEException{
    	 GuestTrainer[] guestTrainers=null;
    	 try{
    		 guestTrainers=sceControl.getGTFromAtlasByProduct(product);
         
        }catch (SQLException sqle) {
             //log.error(LoggerHelper.getStackTrace(sqle));
             throw new SCEException("error.sce.database.communication",sqle);
 		}catch (Exception e) {
             //log.error(LoggerHelper.getStackTrace(e));
             throw new SCEException("error.sce.unknown",e);
         }
    	 return guestTrainers;
         
      }
 
     public String[] getActiveEventName() throws SCEException
     {
     	try
     	{
         return sceControl.getActiveEventName();
     	}
         catch (Exception e) {
             //log.error(LoggerHelper.getStackTrace(e));
             throw new SCEException("error.sce.unknown",e);
         }
     }
     public Event[] getAllEvents() throws SCEException
     {       
         try
     	{
         	return sceControl.getAllEvents();
     	}
         catch (Exception e) {
             //log.error(LoggerHelper.getStackTrace(e));
             throw new SCEException("error.sce.unknown",e);
         }
     }
     
     public WebExDetails[] getWebExDetailsByEventAndUsedBy(String eventName,String usedby)throws SCEException{
    	 WebExDetails[] webExDetails=null;
    	 try{
    		 webExDetails= sceControl.getWebExDetailsByEventAndUsedBy(eventName,usedby);
         }
          catch (SQLException sqle) {
                 //log.error(LoggerHelper.getStackTrace(sqle));
                 throw new SCEException("error.sce.database.communication",sqle);
     		}catch (Exception e) {
                 //log.error(LoggerHelper.getStackTrace(e));
                 throw new SCEException("error.sce.unknown",e);
             }
    	 return webExDetails;
     }
 
 
     public WebExDetails[] getWebExDetailsByEvent(String eventName)throws SCEException
     {
         WebExDetails[] webex=null;
          try{
          webex=sceControl.getWebExDetailsByEvent(eventName);
          
         }
         catch (SQLException sqle) {
             //log.error(LoggerHelper.getStackTrace(sqle));
             throw new SCEException("error.sce.database.communication",sqle);
 		}
          catch(Exception e)
          {
             //log.error(LoggerHelper.getStackTrace(e));
             throw new SCEException("error.sce.unknown",e);
         }
          return webex;
     }
 
 
     public String[] getCourseDates(String eventName,String prodName) throws SCEException
     {
     	try
     	{
         return sceControl.getCourseDates(eventName,prodName);
     	}
         catch (Exception e) {
             //log.error(LoggerHelper.getStackTrace(e));
             throw new SCEException("error.sce.unknown",e);
         }
     }
     
     public String[] getEventCourses(String eventName) throws SCEException
     {
     	try
     	{
         return sceControl.getEventCourses(eventName);
     	}
         catch (Exception e) {
             //log.error(LoggerHelper.getStackTrace(e));
             throw new SCEException("error.sce.unknown",e);
         }
     }
     
     public TrainerLearnerMapping[] getTrainerLearner(String datez,String pro,String eventName) throws SCEException{
         
    	 TrainerLearnerMapping[] lmsMapping=null;
    	 
    	 try{
  
    		 lmsMapping=sceControl.getTrainerLearner(datez,pro,eventName);
         
        }catch (Exception e) {
             //log.error(LoggerHelper.getStackTrace(e));
             throw new SCEException("error.sce.unknown",e);
         }
    	 return lmsMapping;
     }
       
     
/*public void gotoDeleteLTMapping(String delEmail) throws SCEException
     {*/
	
	public void gotoDeleteLTMapping(String delEmail,String product,String courseStartDate) throws SCEException
    {
      try
      {
            /*sceControl.gotoDeleteLTMapping(delEmail);*/
            
            sceControl.gotoDeleteLTMapping(delEmail,product,courseStartDate);
      }
            catch (Exception e) {
                //log.error(LoggerHelper.getStackTrace(e));
                throw new SCEException("error.sce.unknown",e);
            }
     }


               
public TrainerLearnerMapping[] getCountOfEvalsPerTrainer(String eventName,String courseStartDate)throws SCEException{
	    
	TrainerLearnerMapping[] lmsMapping=null;
	try{
		lmsMapping=sceControl.getCountOfEvalsPerTrainer(eventName,courseStartDate);
	    }
	     catch (SQLException sqle) {
	            //log.error(LoggerHelper.getStackTrace(sqle));
	            throw new SCEException("error.sce.database.communication",sqle);
			}catch (Exception e) {
	           // log.error(LoggerHelper.getStackTrace(e));
	            throw new SCEException("error.sce.unknown",e);
	        }
	return lmsMapping;
	}   
     
     
public TrainerLearnerMapping[] getLearnerForMannual(String dates,String eventName) throws SCEException{
   
	TrainerLearnerMapping[] lmsMapping=null;
	try{
         
		lmsMapping=sceControl.getLearnerForMannual(dates,eventName);
   
  }catch (Exception e) {
       //log.error(LoggerHelper.getStackTrace(e));
       throw new SCEException("error.sce.unknown",e);
   }
	return lmsMapping;
}

public TrainerLearnerMapping[] getTrainerForMannual(String produtSel,String eventName) throws SCEException 
{   
	TrainerLearnerMapping[] lmsMapping=null;
   try 
   {
	   lmsMapping=sceControl.getTrainerForMannual(produtSel,eventName);
   }
   catch (Exception e) {
       //log.error(LoggerHelper.getStackTrace(e));
       throw new SCEException("error.sce.unknown",e);
   }
   return lmsMapping;
}

public TrainerLearnerMapping[] getNoOfMailSentByEventTrainer(String eventName,String trainerEmail,String courseStartDate)throws SCEException{
	  
	TrainerLearnerMapping[] lmsMapping=null;
	try
	{
        lmsMapping=sceControl.getNoOfMailSentByEventTrainer(eventName,trainerEmail,courseStartDate);
   }
           catch (SQLException sqle) {
      //log.error(LoggerHelper.getStackTrace(sqle));
      throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
      //log.error(LoggerHelper.getStackTrace(e));
      throw new SCEException("error.sce.unknown",e);
  }
	return lmsMapping;
}

public TrainerLearnerMapping[] getTrainerLocForMannual(String produtSel,String eventName) throws SCEException 
{    
	TrainerLearnerMapping[] lmsMapping=null;
  try
  {
	  lmsMapping=sceControl.getTrainerForMannual(produtSel,eventName);   
  }
   catch (Exception e) {
       //log.error(LoggerHelper.getStackTrace(e));
       throw new SCEException("error.sce.unknown",e);
   }
  return lmsMapping;
}

public void gotosaveTrainerLearnerMap(String selCourseName,String selTrainer,String selLearner,String trainerEmail,String learnerEmail,String whichEventName,String learnerLOC,String trainerLOC,String dates) throws SQLException, SCEException
{
  try
  {
   sceControl.gotoSaveEventMapping(selCourseName, selTrainer,selLearner,trainerEmail, learnerEmail,whichEventName,learnerLOC,trainerLOC,dates);
  }
   catch (Exception e) {
       //log.error(LoggerHelper.getStackTrace(e));
       throw new SCEException("error.sce.unknown",e);
   }

}
public HashMap getLocationForTimeZone()throws SCEException{
	    try{
	        HashMap locZone=new HashMap();
	        String [] loc=sceControl.getLocationForTimeZone("PST");
	        for(int i=0;i<loc.length;i++){
	        locZone.put(loc[i],"PST");
	        }
	        //loc=null;
	        loc=sceControl.getLocationForTimeZone("EST");
	        for(int i=0;i<loc.length;i++){
	        locZone.put(loc[i],"EST");
	        }
	        //loc=null;
	        loc=sceControl.getLocationForTimeZone("CST");
	        for(int i=0;i<loc.length;i++){
	        locZone.put(loc[i],"CST");
	        }
	        //loc=null;
	        loc=sceControl.getLocationForTimeZone("MST");
	        for(int i=0;i<loc.length;i++){
	        locZone.put(loc[i],"MST");
	        }
	        // System.out.println(locZone.size());
	        return locZone;
	    }
	     catch (SQLException sqle) {
	            //log.error(LoggerHelper.getStackTrace(sqle));
	            throw new SCEException("error.sce.database.communication",sqle);
			}catch (Exception e) {
	            //log.error(LoggerHelper.getStackTrace(e));
	            throw new SCEException("error.sce.unknown",e);
	        }
	  }  
	         
   
public Learner[] getLearnerForAuto(String dates,String eventName) throws SCEException{
   
	Learner[] learners=null;
	try{
         
   learners=sceControl.getLearnerForAuto(dates,eventName);

   
  }catch (Exception e) {
       //log.error(LoggerHelper.getStackTrace(e));
       throw new SCEException("error.sce.unknown",e);
   }
	return learners;
}


public TrainerLearnerMapping[] getAlreadyMappedTrainer(String eventName,String courseStartDate)throws SCEException{
   
	TrainerLearnerMapping[] lmsMapping=null;
	try{
		lmsMapping=sceControl.getAlreadyMappedTrainer(eventName,courseStartDate);
  }
   
	catch (Exception e) {
     //log.error(LoggerHelper.getStackTrace(e));
     throw new SCEException("error.sce.unknown",e);
 }
	return lmsMapping;
}

public GuestTrainer[] getTrainerForAuto(String produtSel,String eventName,String courseStartDate) throws SCEException 
{
	GuestTrainer[] guestTrainers=null;
	  try
	  {
		  guestTrainers=sceControl.getTrainerForAuto(produtSel,eventName,courseStartDate);
	  }
   
	catch (Exception e) {
       //log.error(LoggerHelper.getStackTrace(e));
       throw new SCEException("error.sce.unknown",e);
   }
	  return guestTrainers;
}


/* ------------- sanjeev begin trainer learner mapping issue------------------*/	
    
/*public TrainerLearnerMapping[] gotoCheck(String learnerEmail, String whichEventName) throws SCEException
{*/
	
	public TrainerLearnerMapping[] gotoCheck(String learnerEmail, String whichEventName,String selCourseName,String selCourseDateName) throws SCEException
	{
	
		/* ------------- sanjeev end trainer learner mapping issue------------------*/	
	
  //check if mapping is already present 
	TrainerLearnerMapping[] lmsMapping=null;
	  try{
		  /* ------------- sanjeev begin trainer learner mapping issue------------------*/	
		  
		/*  lmsMapping=sceControl.gotoCheck(learnerEmail, whichEventName);*/
		  
		  lmsMapping=sceControl.gotoCheck(learnerEmail, whichEventName,selCourseName,selCourseDateName);
		  
		  /* ------------- sanjeev end trainer learner mapping issue------------------*/	
	      }
	  
	      catch(Exception e)
	       {
	          //log.error(LoggerHelper.getStackTrace(e));
	          throw new SCEException("error.sce.unknown",e);
	      }
  return lmsMapping;
}



public TrainerLearnerMapping[] getNoOfMailSentByEventTrainer(String eventName,String trainerEmail)throws SCEException{
  
	TrainerLearnerMapping[] lmsMapping=null;
	try
	{
        lmsMapping=sceControl.getNoOfMailSentByEventTrainer(eventName,trainerEmail);
   }
           catch (SQLException sqle) {
      //log.error(LoggerHelper.getStackTrace(sqle));
      throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
      //log.error(LoggerHelper.getStackTrace(e));
      throw new SCEException("error.sce.unknown",e);
  }
	return lmsMapping;
}

public void updateWebExDetailsByEventNotUsed(String eventName,String usedby)throws SCEException
{
	  try
	  {
	    sceControl.updateWebExDetailsByEventNotUsed(eventName,usedby);
	  } 
	    catch (Exception e) {
	         //log.error(LoggerHelper.getStackTrace(e));
	         throw new SCEException("error.sce.unknown",e);
	     }
	}


public GuestTrainer[] getGTByEmail(String email) throws SCEException{
  
	GuestTrainer[] guestTrainers=null;
	try
   {
		guestTrainers=sceControl.getGTByEmail(email);
   
  }catch (SQLException sqle) {
       //log.error(LoggerHelper.getStackTrace(sqle));
       throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
      // log.error(LoggerHelper.getStackTrace(e));
       throw new SCEException("error.sce.unknown",e);
   }
	return guestTrainers;
}


public String getTimeZone(String loc)throws SCEException
{
   try{
       return sceControl.getTimeZone(loc);
       
      }catch (Exception e) {
          // log.error(LoggerHelper.getStackTrace(e));
           throw new SCEException("error.sce.unknown",e);
       }
}


public Learner[] getLearnerByEmail(String email)throws SCEException{
	 
	Learner[] learners=null;
	try
	{
		learners=sceControl.getLearnerByEmail(email);
	    }
	     catch (SQLException sqle) {
	            //log.error(LoggerHelper.getStackTrace(sqle));
	            throw new SCEException("error.sce.database.communication",sqle);
			}catch (Exception e) {
	            //log.error(LoggerHelper.getStackTrace(e));
	            throw new SCEException("error.sce.unknown",e);
	        }
	return learners;
	}



public GuestTrainer[] getGTByProduct(String product) throws SCEException{
   
	GuestTrainer[] guestTrainers=null;
	try
   {
		guestTrainers=sceControl.getGTByProduct(product);
   
  }catch (SQLException sqle) {
       //log.error(LoggerHelper.getStackTrace(sqle));
       throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
      // log.error(LoggerHelper.getStackTrace(e));
       throw new SCEException("error.sce.unknown",e);
   }
	return guestTrainers;
}

public HashMap getAllProducts() throws SQLException, SCEException
{   HashMap products = null;
  // templateVersionId=new Integer(10);
   String[] product=sceControl.getProductForEvent();
   products = new LinkedHashMap();
   if (product != null && product.length > 0) {
    for (int i=0; i<product.length; i++){
      // products.put("select","--Select--");
       products.put(product[i],product[i]);
    }
   }

   return products;
}
public GuestTrainer getGTFromAtlasByEmailProduct(String email, String product)throws SCEException
{
   try{
   return sceControl.getGTFromAtlasByEmailProduct(email,product);
   
  }catch (SQLException sqle) {
       //log.error(LoggerHelper.getStackTrace(sqle));
       throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
       //log.error(LoggerHelper.getStackTrace(e));
       throw new SCEException("error.sce.unknown",e);
   }
}

public void removeGT(String email,String product)throws SCEException
{
	  try{
   sceControl.removeGT(email,product);
	  }
   catch (Exception e) {
       //log.error(LoggerHelper.getStackTrace(e));
       throw new SCEException("error.sce.unknown",e);
   }
}

public Integer isPhaseTraining(Integer eventId) throws SQLException, SCEException    
{       
    return sceControl.isPhaseTraining(eventId);              
}

public SCE getNewSCEPhase(Integer eventId, String course) throws SCEException
{
    
    SCE objSCE = new SCE();        
    
    try{
    SCEDetail[] objSCEDetailArr =  sceControl.getSCEDetailsLatestPhase(eventId, course);
    
    // Get The Descriptors For All The Questions and Assign Them
        Descriptor[] objDescriptorArr = sceControl.getDescriptorsLatest(eventId, course);        
        
        if (objDescriptorArr != null && objSCEDetailArr != null) {
            for (int i=0; i<objDescriptorArr.length; i++) {
                for (int j=0; j<objSCEDetailArr.length; j++) {
                    if (objDescriptorArr[i].getQuestionId().equals(objSCEDetailArr[j].getQuestionId())) {
                        
                        objSCEDetailArr[j].addDescriptorList(objDescriptorArr[i]);
                        break;
                    }
                }
            }
        }
        
        for (int i=0; i<objSCEDetailArr.length; i++) {
            if (i==0) {
                objSCE.setTemplateVersionId(objSCEDetailArr[i].getTemplateVersionId());
                objSCE.setFormTitle(objSCEDetailArr[i].getFormTitle());
                objSCE.setEventId(eventId);
                objSCE.setPrecallComments(objSCEDetailArr[i].getPrecallComments());
                objSCE.setPostcallComments(objSCEDetailArr[i].getPostcallComments());                
                objSCE.setHlcCritical(objSCEDetailArr[i].getHlcCritical());
            }
            
            objSCE.addSceDetailList(objSCEDetailArr[i]);
        }

    }catch (SQLException sqle) {
        log.error(LoggerHelper.getStackTrace(sqle));
        throw new SCEException("error.sce.database.communication",sqle);
	}catch (Exception e) {
        log.error(LoggerHelper.getStackTrace(e));
        throw new SCEException("error.sce.unknown",e);
    }
    
    return objSCE;
    
    
}

public SCE[] getSCEHistoryForTRT(Integer eventId, String emplId, String productCode) throws SCEException
{
    SCE[] SCESHistoryForTRT = null;
    try{
        SCESHistoryForTRT = sceControl.getSCEHistoryForTRT(eventId, emplId,productCode);
   }catch (SQLException sqle) {
        log.error(LoggerHelper.getStackTrace(sqle));
        throw new SCEException("error.sce.database.communication",sqle);
                    }catch (Exception e) {
        log.error(LoggerHelper.getStackTrace(e));
        throw new SCEException("error.sce.unknown",e);
    } 
    return SCESHistoryForTRT;
    
}

public void saveSCEPhase(SCE objSCE, SCEDetail[] objSCEDetailArr) throws SCEException
{
    boolean isInsert = false;
    if (objSCE != null) {
        if (objSCE.getId() == null || objSCE.getId().intValue() == SCEConstants.SCE_INVALID_ID.intValue()) {
            isInsert = true;
        }
        
        if (isInsert) {
            if (!checkIfDuplicate(objSCE, objSCEDetailArr)) {
                // Get New SCE ID
                Integer sceId = sceControl.getNextSCEId();
                objSCE.setId(sceId);                
                sceControl.insertSCE(objSCE);
                if (objSCEDetailArr != null) {
                    for (int i=0; i<objSCEDetailArr.length; i++) {
                        SCEDetail objSCEDetail = objSCEDetailArr[i];
                        objSCEDetail.setSceId(objSCE.getId());
                        sceControl.insertSCEDetail(objSCEDetail);
                    }          
                }         
            }
        }
        else {
            sceControl.updateSCE(objSCE);                
            if (objSCEDetailArr != null) {
                for (int i=0; i<objSCEDetailArr.length; i++) {
                    SCEDetail objSCEDetail = objSCEDetailArr[i];
                    objSCEDetail.setSceId(objSCE.getId());
                    sceControl.updateSCEDetail(objSCEDetail);                                                    
                }          
            }
        }            
    }
}

public Role checkRoleAccess(String roleCd, Integer eventId)    
{
    Role role= new Role();
    role=null;
    try {
		role=sceControl.checkRoleAccess(roleCd,eventId);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SCEException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}   
    return role;
}
/*//Introduced by Arpit as part of SCE Migration on 5th August 2014
	public HashMap getAllEvaluationTemplatesMap() throws SCEException {
		// System.out.println("inside getAllEvaluationTemplatesMap method ");
		EmailTemplate[] emailTemplates = null;
		HashMap templateMap = new LinkedHashMap();
		try {
			// System.out.println("calling getevaluationTemplates method ");
			emailTemplates = sceControl.getevaluationTemplates();
			templateMap.put(new Integer(SCEConstants.ALL_EVENT_ID),
					"--Select--");
			if (emailTemplates != null && emailTemplates.length > 0) {
				for (int i = 0; i < emailTemplates.length; i++)
					templateMap.put(
							emailTemplates[i].getEvaluationTemplateId(),
							emailTemplates[i].getEvaluationTemplateName());
			}
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		// System.out.println("The map returned is" + templateMap);
		return templateMap;
	}

	// Introduced by Arpit as part of SCE Migration on 5th August 2014
	public HashMap getScoringOptions(Integer evaluationTemplateId)
			throws SCEException {
		// HashMap scoringOptionsMap = new LinkedHashMap();
    // System.out.println("inside getScoringOptions method");
		List scoringOptionsList = new ArrayList();
		EmailTemplate[] emailTemplates = null;
		String[] scoringOptions = null;
		String[] scoreValue = null;
		HashMap scoringOptionsMap = new HashMap();
		try {
			// System.out.println("Before Calling retrieveScoringOptions method");
			scoringOptions = sceControl.retrieveScoringOptions(evaluationTemplateId);
			// System.out.println("value of scoring options is"+scoringOptions );
			for (int i = 0; i < scoringOptions.length; i++) {
				scoreValue = sceControl.getScoreValues(scoringOptions[i]);
				// System.out.println("scoreValue is "+ scoreValue);
				scoringOptionsMap.put(scoringOptions[i],getScoringOptionString(scoreValue));
			}
			// System.out.println("completed the for loop");
		} catch (SQLException sqle) {
			// log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		// System.out.println("before return");
		return scoringOptionsMap;
	}

	
	// Introduced by Arpit as part of SCE Migration on 5th August 2014
	public String getScoringOptionString(String[] scoreValue) {
  // System.out.println("within getScoringOPtionString method");
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < (scoreValue.length); i++) {
			// System.out.println(scoreValue[i]);
			sb.append(scoreValue[i]);
			sb.append(",");
		}
		String score = sb.toString();
		// System.out.println("value of first score is"+ score);
		int lastIndx = score.length() - 1;
		score = score.substring(0, lastIndx);
		// System.out.println("value of final score is "+ score);
		return score;
	}

	// Introduced by Arpit as part of SCE Migration on 7th August 2014
	 public EmailTemplate[] searchEmailTemplates(Integer evaluationTemplateId,String scoringOption) throws SCEException
	    {
	        // System.out.println("value of evaluationTemplateId "+ evaluationTemplateId);
	        // System.out.println("value of scoringOption"+ scoringOption);
	        EmailTemplate[] emailtemplates = null;
	        try{
	        emailtemplates = sceControl.getAllEmailTemplates(evaluationTemplateId,scoringOption);
	        }catch (SQLException sqle) {
	            //log.error(LoggerHelper.getStackTrace(sqle));
	            throw new SCEException("error.sce.database.communication",sqle);
			}catch (Exception e) {
	            //log.error(LoggerHelper.getStackTrace(e));
	            throw new SCEException("error.sce.unknown",e);
	        }
	        return emailtemplates;
	    }
	
	
	 public String publishEmailTemplate(Integer emailTemplateId) throws SCEException 
	 {
	        
	        String message = "The template has been published";
	        try{
	        SQLParameter[] params = new SQLParameter[2];
	        params[0] = new SQLParameter(emailTemplateId,Types.INTEGER,SQLParameter.IN);
	       // params[1] = new SQLParameter(evaluationTemplateName,Types.VARCHAR,SQLParameter.IN);
	       // params[2] = new SQLParameter(overallScore,Types.VARCHAR,SQLParameter.IN);
	        //params[0] = new SQLParameter(new Integer(1),Types.INTEGER,SQLParameter.OUT);
	        
	       Integer errorCodeSize= sceControl.publishEmailTemplate(emailTemplateId);
	        
	        Integer errorCode = errorCodeSize;
	            
	            if (errorCode != null) {
	                if (errorCode.intValue() == 1)
	                    message = "Unable to publish email template";
	                else if(errorCode.intValue() == 0)
	                    message = "Template published successfully";
	            }
	        
	        }catch (SQLException sqle) {
	           // log.error(LoggerHelper.getStackTrace(sqle));
	            throw new SCEException("error.sce.database.communication",sqle);
			}catch (Exception e) {
	            //log.error(LoggerHelper.getStackTrace(e));
	            throw new SCEException("error.sce.unknown",e);
	        }
	        return message;
	    }
	    
	
	 public EmailTemplateForm getSelectedEmailTemplate(Integer emailTemplateId) throws SCEException
  {
     EmailTemplateForm emailTemplate = null;
     try{
     emailTemplate = sceControl.getSelectedEmailTemplate(emailTemplateId);
     }catch (SQLException sqle) {
         //log.error(LoggerHelper.getStackTrace(sqle));
         throw new SCEException("error.sce.database.communication",sqle);
		}catch (Exception e) {
        // log.error(LoggerHelper.getStackTrace(e));
         throw new SCEException("error.sce.unknown",e);
     }
     return emailTemplate;
  }
*/
 
 
 //apoorva end

    
    public Template[] getAllEvaluationTemplates() throws SCEException
    {   Template[] evaluationTemplates = null;
        try{
        evaluationTemplates = sceControl.getAllEvaluationTemplates(); 
        
        }
        catch (Exception e) {
            //log.error(LoggerHelper.getStackTrace(e));
            throw new SCEException("error.sce.unknown",e);
        }
        return evaluationTemplates;
    }  
 
    
    /* Adbhut Singh Start */

	public EventsCreated[] getAllEventsCreated() {

		// System.out.println("Inside SCEManagerImpl - getAllEventsCreated method ...");
		return sceControl.getAllEventsCreated();
	}

	public EventsCreated getEventsCreatedById(Integer eventId)
			throws SCEException {
		EventsCreated events1 = null;
		try {

			events1 = sceControl.getEventsCreatedById(eventId);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		return events1;
	}

	public void gotoRemoveEvent(Integer eventId) throws SCEException {

		try {

			sceControl.gotoRemoveEvent(eventId);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}

	}

	public EventsCreated getEvent(EventsCreated events) throws SCEException {
		EventsCreated events1 = null;
		try {
			events1 = sceControl.getEvent(events);
		} catch (Exception e) {
			log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}

		return events1;
	}

	public void createEvent(EventsCreated events) throws SCEException {
		try {
			sceControl.createEvent(events);

		} catch (Exception e) {
			log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
	}

	public String[] getEventName() {

		return sceControl.getEventName();
	}
	
	//Added on 4june
	public String[] getEventProductName(String event) {

		return sceControl.getEventProductName(event);
	}
	//end

	public EventCourseProductMapping[] getMappingForEvent(
			String selectedEventName) {

		return sceControl.getMappingForEvent(selectedEventName);
	}

	public CourseDetails[] getAllSearchedCourseDetails(String courseName,
			String courseCode) throws SCEException {

		CourseDetails[] searchedCourseDetails = null;
		try {
			// System.out.println("Inside SCEManagerImpl - getAllSearchedCourseDetails method ...");
			// System.out.println("CourseName = " + courseName+ " && CourseCode = " + courseCode);
			String query = "";
			if (courseName.equals("") && courseCode.equals("")) {// No search
																	// string
																	// entered
				// System.out.println("CHECK 1");
				query = "select distinct (activity_pk) as courseId, "
						+ "activity_code as courseCode, activityname as courseName "
						+ "from tr.mv_usp_activity_hierarchy "
						+ "where (activity_code like('598%') or activity_code like('USFT%') or activity_code like('GCO-NA%')) "
						+ "and active = 1 " + "order by activityname asc";
			} else if (!courseName.equals("") && courseCode.equals("")) {// Only
																			// Course
																			// Name
																			// entered
				// System.out.println("CHECK 2");
				query = "select distinct (activity_pk) as courseId, "
						+ "activity_code as courseCode, activityname as courseName "
						+ "from tr.mv_usp_activity_hierarchy "
						+ "where (activity_code like('598%') or activity_code like('USFT%') or activity_code like('GCO-NA%')) "
						+ "and active = 1 "
						+ "and lower(activityname) like lower('%"
						+ courseName.replaceAll("'", "''")
						+ "%') order by activityname asc";
			} else if (courseName.equals("") && !courseCode.equals("")) {// Only
																			// Course
																			// Code
																			// entered
				// System.out.println("CHECK 3");
				query = "select distinct (activity_pk) as courseId, "
						+ "activity_code as courseCode, activityname as courseName "
						+ "from tr.mv_usp_activity_hierarchy "
						+ "where (activity_code like('598%') or activity_code like('USFT%') or activity_code like('GCO-NA%')) "
						+ "and active = 1 "
						+ "and lower(activity_code) like lower('%" + courseCode
						+ "%') order by activityname asc";
			} else if (!courseName.equals("") && !courseCode.equals("")) {// Course
																			// Name
																			// and
																			// Code
																			// entered
				// System.out.println("CHECK 4");
				query = "select distinct (activity_pk) as courseId, "
						+ "activity_code as courseCode, activityname as courseName "
						+ "from tr.mv_usp_activity_hierarchy "
						+ "where (activity_code like('598%') or activity_code like('USFT%') or activity_code like('GCO-NA%')) "
						+ "and active = 1 "
						+ "and (lower(activity_code) like lower('%"
						+ courseCode + "%') "
						+ "or lower(activityname) like lower('%"
						+ courseName.replaceAll("'", "''")
						+ "%')) order by activityname asc";
			}
			searchedCourseDetails = sceControl
					.getAllSearchedCourseDetails(query);
		} catch (SQLException sqle) {
			log.error(LoggerHelper.getStackTrace(sqle));
			throw new SCEException("error.sce.database.communication", sqle);
		} catch (Exception e) {
			log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		return searchedCourseDetails;

	}

	public void gotoDeleteEventMapping(Integer mappingID) throws SCEException {
		Integer mappingId = mappingID;
		String sql = "DELETE from ";
		sql += "EVENT_PRODUCT_COURSE_MAPPING  ";
		sql += "  where ";
		sql += "MAPPING_ID = " + mappingId;

		try {
			sceControl.gotoDeleteEventMapping(sql);
		} catch (Exception e) {
			log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}

	}

	public EventCourseProductMapping[] gotoCheckDuplicate(String course,
			String whichEventName) throws SCEException {
		// check if mapping is already present
		String selCourse = course;
		String eventName = whichEventName;

		EventCourseProductMapping[] eventCourseProductMapping = null;

		String sql = "";
		sql = " select MAPPING_ID as mapping_id from EVENT_PRODUCT_COURSE_MAPPING where EVENT_NAME like '"
				+ whichEventName
				+ "'  and COURSE_CODE like '"
				+ selCourse
				+ "'";
		try {
			eventCourseProductMapping = sceControl.gotoCheckDuplicate(sql);
		} catch (Exception e) {
			log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}

		return eventCourseProductMapping;
	}

	public void gotoSaveEventMapping(String selectedEventName,
			String selectedProductName, String selectedCourseName)
			throws SCEException {
		String en = selectedEventName;
		String p = selectedProductName;
		String c = selectedCourseName;
		String sql = "insert ";
		sql += "into ";
		sql += "EVENT_PRODUCT_COURSE_MAPPING ";
		sql += "values ";
		sql += "(seq_mapping_id.nextval," + "'" + en + "'" + "," + "'" + p
				+ "'" + "," + "'" + c + "'" + "," + "'a'" + ")";

		try {
			sceControl.gotoSaveEventMapping(sql);
		} catch (Exception e) {
			log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}

	}

	public void gotoDeleteProductMapping(String pName, String selEventName)
			throws SCEException {

		String proName = pName;
		String event = selEventName;
		String sql = "DELETE from ";
		sql += "EVENT_PRODUCT_COURSE_MAPPING  ";
		sql += "  where ";
		sql += "PRODUCT_NAME = '" + proName + "'" + "  AND EVENT_NAME = '"
				+ event + "'";
		sceControl.gotoDeleteEventMapping(sql);

		try {
			sceControl.gotoDeleteEventMapping(sql);
		} catch (Exception e) {
			log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}

	}

	public void updateEvent(EventsCreated events) throws SCEException {
		// System.out.println("Inside the SCEManagerImpl - updateEvent method ...Before update status");

		try {
			sceControl.updateEvent(events);
		} catch (Exception e) {
			log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}
		// System.out.println("Inside the uupdateEvent method ... after the update status");

	}
    
	//Introduced by Arpit as part of SCE Migration on 5th August 2014
		public HashMap getAllEvaluationTemplatesMap() throws SCEException {
			// System.out.println("Inside SCEManagerImpl - getAllEvaluationTemplatesMap method ...");
			EmailTemplate[] emailTemplates = null;
			HashMap templateMap = new LinkedHashMap();
			try {
				// System.out.println("calling getevaluationTemplates method ...");
				emailTemplates = sceControl.getevaluationTemplates();
				templateMap.put(new Integer(SCEConstants.ALL_EVENT_ID),
						"--Select--");
				if (emailTemplates != null && emailTemplates.length > 0) {
					for (int i = 0; i < emailTemplates.length; i++)
						templateMap.put(
								emailTemplates[i].getEvaluationTemplateId(),
								emailTemplates[i].getEvaluationTemplateName());
				}
			} catch (Exception e) {
				// log.error(LoggerHelper.getStackTrace(e));
				throw new SCEException("error.sce.unknown", e);
			}
			// System.out.println("The map returned is" + templateMap);
			return templateMap;
		}

		// Introduced by Arpit as part of SCE Migration on 5th August 2014
		public HashMap getScoringOptions(Integer evaluationTemplateId)
				throws SCEException {
			// HashMap scoringOptionsMap = new LinkedHashMap();
	       // System.out.println("Inside SCEManagerImpl - getScoringOptions method ...");
			List scoringOptionsList = new ArrayList();
			EmailTemplate[] emailTemplates = null;
			String[] scoringOptions = null;
			String[] scoreValue = null;
			HashMap scoringOptionsMap = new HashMap();
			try {
				// System.out.println("Before calling retrieveScoringOptions method");
				scoringOptions = sceControl.retrieveScoringOptions(evaluationTemplateId);
				// System.out.println("value of scoring options is"+scoringOptions );
				for (int i = 0; i < scoringOptions.length; i++) {
					scoreValue = sceControl.getScoreValues(scoringOptions[i]);
					// System.out.println("scoreValue is "+ scoreValue);
					scoringOptionsMap.put(scoringOptions[i],getScoringOptionString(scoreValue));
				}
				// System.out.println("completed the for loop");
			} catch (SQLException sqle) {
				// log.error(LoggerHelper.getStackTrace(sqle));
				throw new SCEException("error.sce.database.communication", sqle);
			} catch (Exception e) {
				// log.error(LoggerHelper.getStackTrace(e));
				throw new SCEException("error.sce.unknown", e);
			}
			// System.out.println("before return");
			return scoringOptionsMap;
		}

		
		// Introduced by Arpit as part of SCE Migration on 5th August 2014
		public String getScoringOptionString(String[] scoreValue) {
	     // System.out.println("within getScoringOPtionString method");
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < (scoreValue.length); i++) {
				// System.out.println(scoreValue[i]);
				sb.append(scoreValue[i]);
				sb.append(",");
			}
			String score = sb.toString();
			// System.out.println("value of first score is"+ score);
			int lastIndx = score.length() - 1;
			score = score.substring(0, lastIndx);
			// System.out.println("value of final score is "+ score);
			return score;
		}

		// Introduced by Arpit as part of SCE Migration on 7th August 2014
		 public EmailTemplate[] searchEmailTemplates(Integer evaluationTemplateId,String scoringOption) throws SCEException
		    {
		        // System.out.println("value of evaluationTemplateId "+ evaluationTemplateId);
		        // System.out.println("value of scoringOption"+ scoringOption);
		        EmailTemplate[] emailtemplates = null;
		        try{
		        emailtemplates = sceControl.getAllEmailTemplates(evaluationTemplateId,scoringOption);
		        }catch (SQLException sqle) {
		            //log.error(LoggerHelper.getStackTrace(sqle));
		            throw new SCEException("error.sce.database.communication",sqle);
				}catch (Exception e) {
		            //log.error(LoggerHelper.getStackTrace(e));
		            throw new SCEException("error.sce.unknown",e);
		        }
		        return emailtemplates;
		    }
		
		
		 public String publishEmailTemplate(Integer emailTemplateId) throws SCEException 
		 {
		        
		        String message = "The template has been published";
		        Integer errorCodeSize=1;
		        try{
		        
		        
		      
		        
		        errorCodeSize= sceControl.publishEmailTemplate(emailTemplateId,errorCodeSize);
		        
		        Integer errorCode = errorCodeSize;
		            
		            if (errorCode != null) {
		                if (errorCode.intValue() == 1)
		                    message = "Unable to publish email template";
		                else if(errorCode.intValue() == 0)
		                    message = "Template published successfully";
		            }
		        
		        }catch (SQLException sqle) {
		           // log.error(LoggerHelper.getStackTrace(sqle));
		            throw new SCEException("error.sce.database.communication",sqle);
				}catch (Exception e) {
		            //log.error(LoggerHelper.getStackTrace(e));
		            throw new SCEException("error.sce.unknown",e);
		        }
		        return message;
		    }
		    
		
		 public EmailTemplateForm getSelectedEmailTemplate(Integer emailTemplateId) throws SCEException
	     {
	        EmailTemplateForm emailTemplate = null;
	        try{
	        emailTemplate = sceControl.getSelectedEmailTemplate(emailTemplateId);
	        }catch (SQLException sqle) {
	            //log.error(LoggerHelper.getStackTrace(sqle));
	            throw new SCEException("error.sce.database.communication",sqle);
			}catch (Exception e) {
	           // log.error(LoggerHelper.getStackTrace(e));
	            throw new SCEException("error.sce.unknown",e);
	        }
	        return emailTemplate;
	     }

		 public String saveEmailTemplate(EmailTemplate emailTemplate) throws SCEException{
		        
		        EmailTemplateForm emailtemplatesforVersion = new EmailTemplateForm();
		        String emailTemplateVersion = emailTemplate.getEmailTemplateVersion();
		        String evaluationTemplateName = emailTemplate.getEvaluationTemplateName();
		        String scoringOption = emailTemplate.getScoringSystemIdentifier();
		        String overallScore = emailTemplate.getOverallScore();
		        Integer emailTemplateIdforVersion = emailTemplate.getEmailTemplateId();  
		        String message = "The template has been modified";
		        try{ 
		            //emailtemplatesforVersion = sceControl.getSelectedEmailTemplate(emailTemplateIdforVersion);
		            emailtemplatesforVersion = sceControl.getlatestEmailTemplateVersion(evaluationTemplateName,scoringOption,overallScore);     
		            if (emailtemplatesforVersion == null){
		            
		            
		            //// System.out.println("email cc" +emailtemplatesforVersion.getEmailSubject());
		                //if (emailTemplateVersion.equalsIgnoreCase("Published"))
		        
		            Integer emailTemplateId = sceControl.getNextValEmailTemplateId();
		            // System.out.println(emailTemplateId);
		            emailTemplate.setEmailTemplateId(emailTemplateId);
		            emailTemplate.setEmailTemplateVersion("Draft");
		            emailTemplate.setPublishFlag("N");
		            sceControl.insertEmailTemplate(emailTemplate);
		            message="Draft version created";
		        }
		        else
		        {
		            emailTemplate.setEmailTemplateId(emailtemplatesforVersion.getEmailTemplateId());
		            sceControl.updateEmailTemplate(emailTemplate);
		            message="Template has been modified successfully";
		        }
		        }catch (SQLException sqle) {
		            //log.error(LoggerHelper.getStackTrace(sqle));
		            throw new SCEException("error.sce.database.communication",sqle);
				}catch (Exception e) {
		           // log.error(LoggerHelper.getStackTrace(e));
		            throw new SCEException("error.sce.unknown",e);
		        }
		        return message;
		}
	  
		 
		 public void deleteEmailTemplate(Integer emailTemplateId) throws SCEException
		    {
		        try{
		        sceControl.deleteEmailTemplate(emailTemplateId);
		        }catch (SQLException sqle) {
		           // log.error(LoggerHelper.getStackTrace(sqle));
		            throw new SCEException("error.sce.database.communication",sqle);
				}catch (Exception e) {
		            //log.error(LoggerHelper.getStackTrace(e));
		            throw new SCEException("error.sce.unknown",e);
		        }
		    }
		 
		 
		 
		//************************************************************
		 /*Added by Manish on 4 feb 2016*/
		 
		 //Added by manish to get no. of evaluation hours
		 
		 public TrainerLearnerMapping[] getevaluationHours(String courseStartDate) throws SCEException
		 {
			 System.out.println("Entering SCEManager Implementation");
			 TrainerLearnerMapping[] lmsMapping=null;
			 try{
				 System.out.println("Entering SCEManager Implementation and before executing method");
	    		 lmsMapping=sceControl.getevaluationHours(courseStartDate);
	    		 System.out.println("Entering SCEManager Implementation and after executing method");
	         
	        }catch (Exception e) {
	             //log.error(LoggerHelper.getStackTrace(e));
	             throw new SCEException("error.sce.unknown",e);
	         }
			return lmsMapping;
		 }
		 
		 //end code

		 
		 
//Added by manish to get time slots
		 
		 public String getTimeSlots(String courseStartDate,String email) throws SCEException
		 {
			 System.out.println("Entering SCEManager Implementation");
			 String lmsMapping="";
			 try{
				 System.out.println("Entering SCEManager Implementation and before executing method");
	    		 lmsMapping=sceControl.getTimeSlots(courseStartDate,email);
	    		 System.out.println("Entering SCEManager Implementation and after executing method");
	    		 System.out.println("lmsmapping:"+lmsMapping);
	        }catch (Exception e) {
	             //log.error(LoggerHelper.getStackTrace(e));
	             throw new SCEException("error.sce.unknown",e);
	         }
			return lmsMapping;
		 }
		 
		 //End Code
		 
		 
		 
		 
		 //***********************************************************

//Added by manish to get trainer name
		 
		 public TrainerLearnerMapping[] getTrainerName(String email) throws SCEException
		 {
			 System.out.println("Entering SCEManager Implementation");
			 TrainerLearnerMapping[] lmsMapping=null;
			 try{
				 System.out.println("Entering SCEManager Implementation and before executing method");
	    		 lmsMapping=sceControl.getTrainerName(email);
	    		 System.out.println("Entering SCEManager Implementation and after executing method");
	    		 System.out.println("lmsmapping:"+lmsMapping);
	        }catch (Exception e) {
	             //log.error(LoggerHelper.getStackTrace(e));
	             throw new SCEException("error.sce.unknown",e);
	         }
			return lmsMapping;
		 }
		 
		 //End Code
		 
//Added by manish to get trainer slots
		 
		 public TrainerLearnerMapping[] getTrainerslots(String email,String event_name,String mapId) throws SCEException
		 {
			 System.out.println("Entering SCEManager Implementation");
			 TrainerLearnerMapping[] lmsMapping=null;
			 try{
				 System.out.println("Entering SCEManager Implementation and before executing method");
	    		 lmsMapping=sceControl.getTrainerSlots(email,event_name,mapId);
	    		 System.out.println("Entering SCEManager Implementation and after executing method");
	    		 System.out.println("lmsmapping:"+lmsMapping);
	        }catch (Exception e) {
	             //log.error(LoggerHelper.getStackTrace(e));
	             throw new SCEException("error.sce.unknown",e);
	         }
			return lmsMapping;
		 }
		 
		 //End Code
		 

		 
		 
}
