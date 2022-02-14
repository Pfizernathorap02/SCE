package com.pfizer.sce.db;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
































import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.json.JSONObject;

import com.pfizer.sce.beans.Attendee;
import com.pfizer.sce.beans.BU;
import com.pfizer.sce.beans.BusinessRule;
import com.pfizer.sce.beans.CourseDetails;
import com.pfizer.sce.beans.CourseEvalTemplateMapping;
import com.pfizer.sce.beans.DateTimeSlots;
import com.pfizer.sce.beans.Descriptor;
import com.pfizer.sce.beans.EmailTemplate;
import com.pfizer.sce.beans.EmailTemplateForm;
import com.pfizer.sce.beans.EvaluationForm;
import com.pfizer.sce.beans.EvaluationFormScore;
import com.pfizer.sce.beans.Event;
import com.pfizer.sce.beans.EventCourse;
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
import com.pfizer.sce.utils.HibernateUtils;
import com.pfizer.sce.utils.SCEUtils;
































import javax.mail.internet.HeaderTokenizer.Token;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
//added for oAuth shindo
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class SCEControlImpl {
	private String result = "";

	// Logger to create logs
	static Logger log = LogManager.getLogger(SCEControlImpl.class);

	/* GADALP STARTs */
	/*Start: AGARWN21: New function added for employee id in 2021 Bug fix release */
	//Start of Nishtha
	public String getempbyNTID(String ntid) {
		Session session = HibernateUtils.getHibernateSession();
		String res = null;
		StringBuilder queryString = new StringBuilder();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "start of getAdhocDistributionListMember");

			// System.out.println("quer1" + queryString);
			queryString.append( " select u.emplid as emplId from users u where upper(u.ntid) = upper(:ntid) ");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			q.setParameter("ntid", ntid);
			List list = q.list();

			// System.out.println("size " + list.size());

			res = (list.size() > 0) ? (String) list.get(0) : null;
			System.out.println("emp id in control:"+res);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return res;
	}
	//End of Nishtha


	public User getUserByNTIdAndDomain(String ntid) {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List<User> userList = new ArrayList<User>();

		// generic added
		List<Object> ls = new ArrayList<Object>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			/* Start:Muzees:NTDomain removed from 2020 Q2 Bug fix release */
			/* Business unit condition added by MUZEES for PBG and UpJOHN */
			queryString
					.append("select  u.user_id as id, u.emplId as emplId,u.last_name as lastName, u.first_name as firstName, ")
					.append("u.email as email, u.ntid as ntid, u.ntdomain as ntdomain, u.status as status,u.usergroup as userGroup, ")
					.append("u.create_date as createDate,u.last_modified_date as lastModifiedDate,u.BU_ID as businessUnit  from users u where upper(u.ntid) = upper(:ntid)  ");
			// end
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			q.setParameter("ntid", ntid);
			// q.setParameter("ntdomain", ntdomain);
			/* Muzees:End */
			ls = q.list();
			log.debug("log size " + ls.size());

			Iterator<Object> it = ls.iterator();

			User user = null;
			while (it.hasNext()) {
				user = new User();
				Object[] field = (Object[]) it.next();

				user.setId(((BigDecimal) field[0]).intValue());
				user.setEmplId((String) field[1]);
				user.setLastName((String) field[2]);
				user.setFirstName((String) field[3]);
				user.setEmail((String) field[4]);
				user.setNtid((String) field[5]);
				user.setNtdomain((String) field[6]);
				user.setStatus((String) field[7]);
				user.setUserGroup((String) field[8]);
				user.setCreateDate((Date) field[9]);
				user.setLastModifiedDate((Date) field[10]);
				user.setBusinessUnit((String) field[11]);

				userList.add(user);
			}

			// System.out.println("New User List Array Length" +
			// userList.size());
			log.debug("log New User List Array Length" + userList.size());

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			log.error("HibernateException in getUserByNTIdAndDomain", e);
			log.error(e.getMessage());
			// System.out.println("getUserByNTIdAndDomain Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return (userList.size() > 0) ? userList.get(0) : null;
	}

	public String getSalesPositionTypeCd(String ntid) {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List<Object> salesType = new ArrayList<Object>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString
					.append("select sales_position_type_cd from mv_field_employee_rbu where upper(nt_id)= :ntid ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			q.setParameter("ntid", ntid);

			salesType = q.list();

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();

			log.error("HibernateException in getSalesPositionTypeCd", e);
			// System.out.println("HibernateException in getSalesPositionTypeCd");
		}

		finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return (salesType.size() > 0) ? (String) salesType.get(0) : null;

	}

	public String getSceVisibility(String salesPositionTypeCd) {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List sce_visible = null;
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString
					.append("select sce_visible_for_all as sceVisibility from sce_scores_visibility where upper(sales_position_type_cd) = upper(:salesPositionTypeCd)");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			q.setParameter("salesPositionTypeCd", salesPositionTypeCd);

			sce_visible = q.list();

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// log.error("HibernateException in getSceVisibility", e);
			// System.out.println("HibernateException in getSceVisibility");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return (sce_visible.size() > 0) ? (String) sce_visible.get(0) : null;

	}

	public Event[] getEvents() {

		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		// System.out.println("test 2");

		Event[] eventArray = null;

		StringBuilder queryString = new StringBuilder();
		// String queryString = null;
		List<Event> user = new ArrayList<Event>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "start of getAdhocDistributionListMember");

			// System.out.println("quer1" + queryString);

			queryString
					.append("select event_id as id, name as name, isdefault as isDefault,")
					.append("display as display from event where display = 'Y' ")
					.append("order by name asc ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			// Query q = session.createQuery(queryString);
			Query q = session.createSQLQuery(q1);
			List list = q.list();

			List<Event> newListEvent = new ArrayList<Event>();
			// user = list;
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			Event event = null;
			while (it.hasNext()) {
				event = new Event();
				Object[] field = (Object[]) it.next();

				// newUser.setId((Integer)user1[0]);

				event.setId(((BigDecimal) field[0]).intValue());
				event.setName((String) field[1]);
				event.setIsDefault((String) field[2]);
				event.setDisplay((String) field[3]);

				newListEvent.add(event);

			}

			// System.out.println("New User List Array Length"+
			// newListEvent.size());
			// User[] userarray = (User[]) newListUser.toArray();
			eventArray = newListEvent.toArray(new Event[newListEvent.size()]);
			// // System.out.println("User Array Length" + userarray.length);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return eventArray;
	}

	public UserLegalConsent checkLegalConsentAcceptance(String ntid)
			throws SQLException {

		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		// System.out.println("test 2");

		UserLegalConsent legalConsent = null;

		StringBuilder queryString = new StringBuilder();
		// String queryString = null;
		List<Event> user = new ArrayList<Event>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "start of getAdhocDistributionListMember");

			// System.out.println("quer1" + queryString);

			queryString
					.append("select ULC.ACCEPTED_BY, ULC.ACCEPTED_ID, ULC.LC_ACCEPTANCE_DATE, ULC.LC_ID, ULC.USER_NTDOMAIN, ULC.USER_SCE_ROLE  from USER_LEGAL_CONSENT ulc where ")
					.append("ulc.ACCEPTED_BY in (select ntid from users us where UPPER(us.ntid)=UPPER(:ntid)) ")
					.append("and ulc.LC_ID in (select LC_ID from LEGAL_CONSENT_TEMPLATE where publish_flag='Y') ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			// Query q = session.createQuery(queryString);
			Query q = session.createSQLQuery(q1).addEntity(
					UserLegalConsent.class);
			q.setParameter("ntid", ntid);

			List ls = q.list();

			if (ls != null && ls.size() > 0) {

				legalConsent = (UserLegalConsent) ls.get(0);
			}

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("UserLegalConsent Hibernatate Exception");
		}
		/* This Exception is written for testing purpose */
		catch (Exception e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("UserLegalConsent  Exception");
		}

		finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return legalConsent;

	}

	public LegalConsentTemplate fetchLegalContent() throws SQLException {

		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		// System.out.println("test 2");

		LegalConsentTemplate legalConsentTemp = null;

		StringBuilder queryString = new StringBuilder();
		// String queryString = null;
		List<LegalConsentTemplate> user = new ArrayList<LegalConsentTemplate>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "start of getAdhocDistributionListMember");

			// System.out.println("quer1" + queryString);

			queryString
					.append("Select LC_CONTENT as content,LC_ID as lcId from LEGAL_CONSENT_TEMPLATE where PUBLISH_FLAG='Y'");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			// Query q = session.createQuery(queryString);
			Query q = session.createSQLQuery(q1);
			List list = new ArrayList();
			list = q.list();

			List<LegalConsentTemplate> legalConsentTempList = new ArrayList<LegalConsentTemplate>();
			// user = list;
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			LegalConsentTemplate legalConsent = null;
			while (it.hasNext()) {
				legalConsent = new LegalConsentTemplate();
				Object[] field = (Object[]) it.next();

				legalConsent.setContent((String) field[0]);
				legalConsent.setLcId(((BigDecimal) field[1]).intValue());
				legalConsentTempList.add(legalConsent);
			}

			// System.out.println("New User List Array Length"+
			// legalConsentTempList.size());
			// User[] userarray = (User[]) newListUser.toArray();
			legalConsentTemp = (legalConsentTempList.size() > 0) ? legalConsentTempList
					.get(0) : null;
			// // System.out.println("User Array Length" + userarray.length);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			// log.error("Inside fetchLegalContent Hibernate Exception ",e);
			// System.out.println("Inside fetchLegalContent Hibernate Exception");
		} catch (Exception e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			// log.error("Inside fetchLegalContent Exception ",e);
			// System.out.println("Inside fetchLegalContent Exception");
		}

		finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return legalConsentTemp;

	}

	public Integer getDefaultEventId() {
		Session session = HibernateUtils.getHibernateSession();
		Integer res = null;
		StringBuilder queryString = new StringBuilder();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "start of getAdhocDistributionListMember");

			// System.out.println("quer1" + queryString);
			String q1 = " select max(event_id) from event where isDefault = 'Y' and display = 'Y' ";
			Query q = session.createSQLQuery(q1);
			List list = q.list();

			// System.out.println("size " + list.size());

			res = (list.size() > 0) ? (Integer) list.get(0) : null;

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return res;
	}

	public TemplateVersion retrieveFirstEvalForm(String formStatus)
			throws SQLException {
		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();

		List<TemplateVersion> newTemp = new ArrayList<TemplateVersion>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "start of getAdhocDistributionListMember");

			// System.out.println("quer1" + queryString);

			queryString
					.append("SELECT tv.template_version_id AS id,tv.template_id AS templateId,tv.form_title AS templateName, ")
					.append("tv.version AS version,tv.form_title AS formTitle,tv.publish_flag AS publishFlag, ")
					.append("tv.scoring_system_identifier AS scoringSystemIdentifier,tv.create_date AS createDate, ")
					.append("tv.created_by AS createdBy,u.ntid AS createdByNtid  FROM sales_call.template t, ")
					.append("sales_call.template_version tv, sales_call.users u WHERE ")
					.append("t.template_id = tv.template_id AND tv.created_by = u.user_id(+) AND t.hidden= :formStatus AND tv.form_title = 'P2L Sales Call' ")
					.append("AND tv.version =(SELECT MAX (version) FROM sales_call.template_version ")
					.append("WHERE form_title = 'P2L Sales Call' AND publish_flag = 'Y') ");

			String q1 = queryString.toString();
			System.out.println("quer2" + queryString);

			// Query q = session.createQuery(queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("formStatus", formStatus);
			List list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			TemplateVersion templateVersion = null;
			while (it.hasNext()) {
				templateVersion = new TemplateVersion();
				Object[] field = (Object[]) it.next();
				templateVersion.setId(((BigDecimal) field[0]).intValue());
				templateVersion.setTemplateId(((BigDecimal) field[1])
						.intValue());
				templateVersion.setTemplateName((String) field[2]);
				templateVersion.setVersion(((BigDecimal) field[3]).intValue());
				templateVersion.setFormTitle((String) field[4]);
				templateVersion.setPublishFlag(field[5].toString());
				templateVersion.setScoringSystemIdentifier((String) field[6]);
				templateVersion.setCreateDate((Date) field[7]);
				templateVersion
						.setCreatedBy(((BigDecimal) field[8]).intValue());
				templateVersion.setCreatedByNtid((String) field[9]);
				newTemp.add(templateVersion);

			}

			// System.out.println("New User List Array Length" +
			// newTemp.size());
			// User[] userarray = (User[]) newListUser.toArray();

			// // System.out.println("User Array Length" + userarray.length);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return (newTemp.size() > 0) ? newTemp.get(0) : null;
	}

	public TemplateVersion[] retrieveEvalForm(String formStatus)
			throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		TemplateVersion[] templateArray = null;
		StringBuilder queryString = new StringBuilder();
		List<TemplateVersion> templateArrayList = new ArrayList<TemplateVersion>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "start of getAdhocDistributionListMember");

			// System.out.println("quer1" + queryString);

			queryString
					.append("select tv.version as version,tv.template_version_id as id,tv.template_id as templateId, ")
					.append("t.name as templateName,tv.form_title as formTitle,tv.publish_flag as publishFlag, ")
					.append("tv.scoring_system_identifier as scoringSystemIdentifier, tv.create_date as createDate, ")
					.append("tv.created_by as createdBy, u.ntid AS createdByNtid from template t, ")
					.append("template_version tv, users u, ")
					.append("(select max(version) as maxVersion,form_title as formTitle from template_version ")
					.append("where publish_flag='Y'  group by form_title) selectedTemplate  where ")
					.append("t.template_id = tv.template_id and tv.created_by = u.user_id(+) and tv.publish_flag='Y' AND t.hidden= :formStatus ")
					.append("and tv.form_title!='P2L Sales Call' and t.name!='P2L Sales Call'  and ")
					.append("tv.form_title=selectedTemplate.formTitle and t.name=selectedTemplate.formTitle ")
					.append("and tv.version=selectedTemplate.maxVersion and tv.UPLOADED_BLANK_FILE_ID is not null ")
					.append("order by  t.name asc,tv.version asc ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			// Query q = session.createQuery(queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("formStatus", formStatus);
			List list = q.list();

			List<TemplateVersion> newTemplateList = new ArrayList<TemplateVersion>();
			// user = list;
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			TemplateVersion templateVersion = null;
			while (it.hasNext()) {

				templateVersion = new TemplateVersion();
				Object[] field = (Object[]) it.next();

				templateVersion.setVersion(((BigDecimal) field[0]).intValue());
				templateVersion.setId(((BigDecimal) field[1]).intValue());
				templateVersion.setTemplateId(((BigDecimal) field[2])
						.intValue());
				templateVersion.setTemplateName((String) field[3]);
				templateVersion.setFormTitle((String) field[4]);
				templateVersion.setPublishFlag(field[5].toString());
				templateVersion.setScoringSystemIdentifier((String) field[6]);
				templateVersion.setCreateDate((Date) field[7]);
				templateVersion
						.setCreatedBy(((BigDecimal) field[8]).intValue());
				templateVersion.setCreatedByNtid((String) field[9]);

				newTemplateList.add(templateVersion);

			}

			// System.out.println("New User List Array Length"+
			// newTemplateList.size());
			// User[] userarray = (User[]) newListUser.toArray();
			templateArray = newTemplateList
					.toArray(new TemplateVersion[newTemplateList.size()]);
			// // System.out.println("User Array Length" + userarray.length);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return templateArray;
	}

	public Attendee[] getAttendeesBySearchCriteria(String sql) {
		Session session = HibernateUtils.getHibernateSession();

		Attendee[] attendeeArray = null;

		StringBuilder queryString = new StringBuilder();

		List<Attendee> attendeeList = new ArrayList<Attendee>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "start of getAdhocDistributionListMember");

			// System.out.println("quer1" + sql);

			// Query q = session.createQuery(queryString);
			Query q = session.createSQLQuery(sql);
			List list = q.list();

			List<Attendee> newListAttendee = new ArrayList<Attendee>();
			// user = list;
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			Attendee attendee = null;
			while (it.hasNext()) {
				attendee = new Attendee();
				Object[] field = (Object[]) it.next();

				// newUser.setId((Integer)user1[0]);

				attendee.setEmplId((String) field[0]);
				attendee.setFirstName((String) field[1]);
				attendee.setLastName((String) field[2]);
				attendee.setSalesPositionId((String) field[3]);
				attendee.setRoleCd((String) field[4]);
				attendee.setBu((String) field[5]);
				attendee.setSalesOrgDesc((String) field[6]);

				newListAttendee.add(attendee);

			}

			// System.out.println("New User List Array Length"+
			// newListAttendee.size());
			// User[] userarray = (User[]) newListUser.toArray();
			attendeeArray = newListAttendee
					.toArray(new Attendee[newListAttendee.size()]);
			// // System.out.println("User Array Length" + userarray.length);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return attendeeArray;
	}

	public SCE[] getSCESByEventEmplId(Integer eventId, String emplId)
			throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		SCE[] sceArray = null;
		StringBuilder queryString = new StringBuilder();
		// System.out.println();

		List<Attendee> attendeeList = new ArrayList<Attendee>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "start of getAdhocDistributionListMember");

			/* 2020 Q3:Query updated by MUZEES for multiple evaluation */
			queryString
					.append("SELECT id, eventId, emplId, productCode, product, course, classroom, tableName, overallRating, overallScore, ");
			queryString
					.append("submittedDate, status ,uploadedDate,lmsFlag,templateVersionId,registrationDate FROM ((SELECT s.sce_id AS id,s.event_id as eventId, ");
			queryString
					.append("s.emplId AS emplId, s.product_cd AS productCode,s.product_name AS product,s.course_description as course,s.classroom AS classroom, ");
			queryString
					.append("s.table_name AS tableName,s.overall_rating AS overallRating,s.overall_score AS overallScore,s.submitted_date AS submittedDate, ");
			queryString
					.append("s.status AS status, s.upload_date AS uploadedDate, e.LMS_FLAG as lmsFlag,s.template_version_id as templateVersionId,NULL AS registrationDate FROM ");
			queryString
					.append("SCE_FFT s ,evaluation_form_score e WHERE event_id = (case when :eventId = -1 then event_id else :eventId end) ");
			queryString
					.append("and s.emplId = :emplId and status = 'SUBMITTED' and s.sce_id = (select max(sce_id) from sce_fft a where a.product_name = s.product_name ");
			queryString
					.append("and a.emplid = s.emplid ) and s.template_version_id=e.template_version_id and s.overall_rating=e.score_legend  ");
			queryString
					.append("AND (s.event_id, s.emplId, s.product_cd) NOT IN (SELECT DISTINCT v.event_id, v.emplId, v.product_cd FROM v_sce_p2l_source v, lms_course_mapping lms");
			queryString
					.append(" WHERE v.emplId = :emplId AND v.PRODUCT_NAME = lms.lms_course_name))");
			queryString.append("UNION");
			queryString
					.append("(SELECT NULL AS id, v.event_id as eventId,v.emplId AS emplId,v.product_cd AS productCode,v.product_name AS product, ");
			queryString
					.append("v.course_description as course,v.classroom AS classroom,v.table_name AS tableName,NULL AS overallRating,NULL AS overallScore, ");
			queryString
					.append("NULL  AS submittedDate,NULL AS status,NULL  AS uploadedDate,NULL AS lmsFlag,(SELECT max(template_version_id) from ");
			queryString
					.append("template_version where template_id=(select distinct(evaluation_template_id) from lms_course_mapping where ");
			queryString
					.append("lms_course_name=v.product_name))  AS templateVersionId,NULL AS registrationDate FROM v_sce_source v,lms_course_mapping lms WHERE ");
			queryString
					.append("event_id = (case when :eventId = -1 then event_id else :eventId end) and v.emplId = :emplId and v.PRODUCT_NAME=lms.lms_course_name ");
			queryString
					.append("and (v.event_id, v.emplId, v.product_cd) NOT IN (SELECT DISTINCT s.event_id, s.emplId, s.product_cd FROM SCE_FFT s)) ");
			queryString.append("UNION");
			queryString
					.append("(SELECT NULL AS id,v.event_id as eventId,v.emplId AS emplId,v.product_cd AS productCode,v.product_name AS product, ");
			queryString
					.append("v.course_description as course,v.classroom AS classroom,v.table_name AS tableName,NULL AS overallRating,NULL AS overallScore, ");
			queryString
					.append("NULL  AS submittedDate,NULL AS status,NULL  AS uploadedDate,NULL AS lmsFlag,(SELECT max(template_version_id) from ");
			queryString
					.append("template_version where template_id=(select distinct(evaluation_template_id) from lms_course_mapping where ");
			queryString
					.append("lms_course_name=v.product_name)) AS templateVersionId, NULL AS registrationDate FROM v_sce_p2l_source v,lms_course_mapping lms WHERE ");
			queryString
					.append("event_id = (case when :eventId = -1 then event_id else :eventId end) and v.emplId = :emplId and v.PRODUCT_NAME=lms.lms_course_name ");
			queryString
					.append("and (v.event_id, v.emplId, v.product_cd) NOT IN (SELECT DISTINCT s.event_id, s.emplId, s.product_cd FROM SCE_FFT s))");
			queryString.append("UNION");
			queryString
					.append("(SELECT s.sce_id AS id,v.event_id AS eventId, v.emplId AS emplId,v.product_cd AS productCode, v.product_name AS product,v.course_description AS course,");
			queryString
					.append("v.classroom AS classroom,v.table_name AS tableName,s.overall_rating AS overallRating,s.overall_score AS overallScore, s.submitted_date AS submittedDate,");
			queryString
					.append("s.status AS status,s.upload_date AS uploadedDate,e.LMS_FLAG  AS lmsFlag,(SELECT MAX (template_version_id) FROM template_version ");
			queryString
					.append("WHERE template_id =(SELECT DISTINCT (evaluation_template_id) FROM lms_course_mapping WHERE lms_course_name = v.product_name)) AS templateVersionId,");
			queryString
					.append("(SELECT MAX (REGISTRATION_DATE) FROM tr.v_usp_sce_registered p WHERE p.emplid = v.emplid AND p.ACTIVITY_PK = v.product_cd) AS registrationDate ");
			queryString
					.append("FROM v_sce_p2l_source v, lms_course_mapping lms, sce_fft s,evaluation_form_score e WHERE v.event_id = (CASE WHEN :eventId = -1 THEN v.event_id ELSE :eventId  END)");
			queryString
					.append(" AND v.emplId = :emplId AND v.PRODUCT_NAME = lms.lms_course_name AND s.emplid = v.emplid AND s.product_name = v.product_name AND s.event_id = v.event_id");
			queryString
					.append(" AND s.sce_id = (SELECT MAX (sce_id) FROM sce_fft a WHERE a.product_name = s.product_name AND a.emplid = s.emplid) AND  s.template_version_id=e.template_version_id AND s.overall_rating=e.score_legend))");
			queryString
					.append("ORDER BY eventId desc, product, id, submittedDate ");

			// Query q = session.createQuery(queryString);

			String sql = new String(queryString);
			// Query q = session.createSQLQuery(sql);

			Query q = session
					.createSQLQuery(sql)
					.addScalar("id")
					.addScalar("eventId")
					.addScalar("emplId")
					.addScalar("productCode")
					.addScalar("product")
					.addScalar("course")
					.addScalar("classroom")
					.addScalar("tableName")
					.addScalar("overallRating")
					.addScalar("overallScore")
					.addScalar("submittedDate",
							org.hibernate.type.TimestampType.INSTANCE)
					.addScalar("status")
					.addScalar("uploadedDate",
							org.hibernate.type.TimestampType.INSTANCE)
					.addScalar("lmsFlag")
					.addScalar("templateVersionId")
					.addScalar("registrationDate",
							org.hibernate.type.TimestampType.INSTANCE);

			q.setParameter("emplId", emplId);
			q.setParameter("eventId", eventId);
			System.out.println(q.getQueryString());

			List list = q.list();

			List<SCE> newListSCE = new ArrayList<SCE>();
			// user = list;
			// System.out.println("Size :: " + list.size());
			Iterator it = list.iterator();

			SCE scedet = null;
			while (it.hasNext()) {
				scedet = new SCE();
				Object[] field = (Object[]) it.next();

				scedet.setId((field[0] != null) ? ((BigDecimal) field[0])
						.intValue() : null);
				scedet.setEventId((field[1] != null) ? ((BigDecimal) field[1])
						.intValue() : null);
				scedet.setEmplId((String) field[2]);
				scedet.setProductCode((String) field[3]);
				scedet.setProduct((String) field[4]);
				scedet.setCourse((String) field[5]);
				scedet.setClassroom((String) field[6]);
				scedet.setTableName((String) field[7]);
				scedet.setOverallRating((String) field[8]);
				scedet.setOverallScore((field[9] != null) ? ((BigDecimal) field[9])
						.doubleValue() : null);
				scedet.setSubmittedDate((Date) field[10]);
				scedet.setStatus((String) field[11]);
				scedet.setUploadedDate(((Date) field[12]));
				scedet.setLmsFlag(field[13] != null ? (field[13]).toString()
						: null);
				scedet.setTemplateVersionId((field[14] != null) ? ((BigDecimal) field[14])
						.intValue() : null);
				scedet.setRegistrationDate(field[15] != null ? (Date) field[15]
						: null);/*
								 * 2020 Q3:added by MUZEES for multiple
								 * evaluations
								 */
				newListSCE.add(scedet);

			}

			System.out
					.println("New User List Array Length" + newListSCE.size());
			sceArray = newListSCE.toArray(new SCE[newListSCE.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			// System.out.println("HibernateException in getSCESByEventEmplId");
			e.printStackTrace();
			// // System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return sceArray;

	}

	public Integer fetchTemplateVersionId(String productName)
			throws SQLException {

		Session session = HibernateUtils.getHibernateSession();

		Integer res = null;
		StringBuilder queryString = new StringBuilder();
		List list = new ArrayList();

		try {

			// System.out.println("quer1 " + queryString);
			String q1 = " SELECT max(template_version_id) from sales_call.template_version tv where template_id=(select distinct(evaluation_template_id)from sales_call.lms_course_mapping where upper(lms_course_name)=upper(:productName))and tv.Publish_flag='Y' ";

			Query q = session.createSQLQuery(q1);
			q.setParameter("productName", productName);
			list = q.list();

			// System.out.println("size " + list.size());

			res = (list.size() > 0 && list.get(0) != null) ? ((BigDecimal) list
					.get(0)).intValue() : null;

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return null;
		return res;

	}

	public Event[] getAllEvents() {
		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		// System.out.println("test 2");

		Event[] eventArray = null;

		StringBuilder queryString = new StringBuilder();
		// String queryString = null;
		List<Event> user = new ArrayList<Event>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "start of getAdhocDistributionListMember");

			// System.out.println("quer1" + queryString);

			queryString.append("FROM Event e ").append(" order by e.name asc ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			// Query q = session.createQuery(queryString);
			Query q = session.createQuery(q1);
			List list = q.list();

			// List<Event> newListEvent = new ArrayList<Event>();
			// user = list;
			/*
			 * // System.out.println("size " + list.size()); Iterator it =
			 * list.iterator();
			 * 
			 * Event event = null; while (it.hasNext()) { event = new Event();
			 * Object[] field = (Object[]) it.next();
			 * 
			 * // newUser.setId((Integer)user1[0]);
			 * 
			 * event.setId(((BigDecimal) field[0]).intValue());
			 * event.setName((String) field[1]); event.setIsDefault((String)
			 * field[2]); event.setDisplay((String) field[3]);
			 * 
			 * newListEvent.add(event);
			 * 
			 * }
			 */

			/*
			 * // System.out.println("New User List Array Length" +
			 * newListEvent.size());
			 */
			// User[] userarray = (User[]) newListUser.toArray();
			eventArray = (Event[]) list.toArray(new Event[list.size()]);
			// // System.out.println("User Array Length" + userarray.length);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return eventArray;
	}

	public Event[] getAllEventsTest() {
		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		// System.out.println("test 2");

		Event[] eventArray = null;

		StringBuilder queryString = new StringBuilder();
		// String queryString = null;
		List<Event> user = new ArrayList<Event>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "start of getAdhocDistributionListMember");

			// System.out.println("quer1" + queryString);

			queryString.append("select e.event_id, e.name, e.isdefault, ")
					.append("e.display from event e ")
					.append("order by e.name asc ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			// Query q = session.createQuery(queryString);
			Query q = session.createSQLQuery(q1).addEntity(Event.class);
			List list = q.list();

			// System.out.println("List size is " + list.size());

			Iterator it = list.iterator();

			while (it.hasNext()) {
				// System.out.println(((Event) it.next()).getName());

			}

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return eventArray;
	}

	public Attendee getAttendeeByEmplId(String emplId) {

		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();

		List<Attendee> newTemp = new ArrayList<Attendee>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "start of getAdhocDistributionListMember");

			// System.out.println("quer1" + queryString);
			/*
			 * Code change done for fetching CSO employees evaluation results
			 * after clicking on employee id hyperlink
			 */
			queryString
					.append("select fe.emplId as emplId, fe.first_name as firstName, fe.last_name ")
					.append("as lastName, fe.role_cd as roleCd,fe.bu as bu, fe.sales_group as salesOrgDesc, ")
					.append("fe.sales_position_id as salesPositionId from  mv_field_employee_rbu fe ")
					.append("where emplId = :emplid ")
					.append("UNION ")
					.append(" select v.empl_Id as emplId,v.first_name as firstName, v.last_name ")
					.append(" as lastName,v.role as roleCd,v.BUSINESS_UNIT as bu,v.SALES_ORGANISATION as salesOrgDesc,v.SALES_POSITION_ID as salesPositionId from p2l_learners v ")
					.append("where empl_Id =:emplid");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			// Query q = session.createQuery(queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("emplid", emplId);

			List list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			Attendee attendee = null;
			while (it.hasNext()) {
				attendee = new Attendee();
				Object[] field = (Object[]) it.next();

				attendee.setEmplId((String) field[0]);
				attendee.setFirstName((String) field[1]);
				attendee.setLastName((String) field[2]);
				attendee.setRoleCd((String) field[3]);
				attendee.setBu((String) field[4]);
				attendee.setSalesOrgDesc((String) field[5]);
				attendee.setSalesPositionId((String) field[6]);

				newTemp.add(attendee);

			}

			System.out.println("New User List Array Length" + newTemp.size());

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return (newTemp.size() > 0) ? newTemp.get(0) : null;

	}

	public void acceptLegalConsent(UserLegalConsent userLegalConsent)
			throws SQLException {
		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();
		List<Attendee> newTemp = new ArrayList<Attendee>();

		try {
			// System.out.println("quer1" + queryString);

			String q1 = queryString.toString();
			// System.out.println("quer2" + q1);

			Transaction ts = session.beginTransaction();
			session.persist(userLegalConsent);
			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("acceptLegalConsent Hibernatate Exception");

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	public int fetchMaxAcceptedId() throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		Integer acceptedId = null;

		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("select Max(ACCEPTED_ID) as acceptedId from user_legal_consent");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);

			// version=q.getFirstResult();
			List list = q.list();
			// System.out.println("size " + list.size());

			acceptedId = (list.size() > 0) ? ((BigDecimal) list.get(0))
					.intValue() : null;

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return acceptedId;
	}

	public User[] getUsersByStatus(String userStatus, String userGroup) {

		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		// System.out.println("test 2");

		User[] userArray = null;

		StringBuilder queryString = new StringBuilder();
		// String queryString = null;

		try {

			String sql = "select ";
			sql += "user_id , ";
			sql += "emplId , ";
			sql += "last_name , ";
			sql += "first_name , ";
			sql += "email , ";
			sql += "ntid , ";
			sql += "ntdomain , ";
			sql += "status , ";
			sql += "usergroup , ";
			sql += "create_date , ";
			sql += "last_modified_date , ";
			sql += "expiration_date , ";
			sql += "bu_id ";/* added by muzees for PBG and UpJOHN */
			sql += "from users where usergroup = :userGroup";
			if (!userStatus.equalsIgnoreCase("ALL")) {
				sql += " and status = :userStatus";
			}

			/* Author: Mayank Date:07-Oct-2011 SCE Enhancement 2011 */
			// sql +=
			// "and (usergroup <> 'SCE_GuestTrainer_MGR' or usergroup is null)";
			// //commented by muzees during PBG and Upjohn

			String orderBy = "";
			orderBy = " order by last_name asc, first_name asc";

			sql = sql + orderBy;

			Query q = session.createSQLQuery(sql).addEntity(User.class);

			q.setParameter("userGroup", userGroup);
			if (!userStatus.equalsIgnoreCase("ALL")) {
				q.setParameter("userStatus", userStatus);
			}

			List list = q.list();

			// System.out.println("List size is " + list.size());
			Iterator it = list.iterator();

			/*
			 * while(it.hasNext()){ //
			 * System.out.println(((User)it.next()).getName()); }
			 */

			userArray = (User[]) list.toArray(new User[list.size()]);

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return userArray;

	}

	public User[] getUsersByStatusTst(String userStatus) {

		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		// System.out.println("test 2");

		User[] userArray = null;

		StringBuilder queryString = new StringBuilder();
		// String queryString = null;

		try {

			String sql = "select ";
			sql += " * ";
			sql += "from users_tst ut , users us where ut.user_id = us.user_id and ut.user_id = '6' and ut.status = :userStatus ";
			/* Author: Mayank Date:07-Oct-2011 SCE Enhancement 2011 */
			sql += "and (ut.usergroup <> 'SCE_GuestTrainer_MGR' or ut.usergroup is null)";

			String orderBy = "";
			orderBy = " order by ut.last_name asc, ut.first_name asc";

			sql = sql + orderBy;

			Query q = session.createSQLQuery(sql).addEntity(User.class);
			q.setParameter("userStatus", userStatus);

			List list = q.list();

			// System.out.println("List size is " + list.size());
			Iterator it = list.iterator();

			while (it.hasNext()) {
				// System.out.println(((User) it.next()).getLastName());
			}

			userArray = (User[]) list.toArray(new User[list.size()]);

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return userArray;

	}

	public User[] getAllUsers(String userGroup) {

		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		// System.out.println("test 2");

		User[] userArray = null;

		StringBuilder queryString = new StringBuilder();
		// String queryString = null;

		try {

			String sql = "select ";
			sql += "user_id , ";
			sql += "emplId , ";
			sql += "last_name , ";
			sql += "first_name , ";
			sql += "email , ";
			sql += "ntid , ";
			sql += "ntdomain , ";
			sql += "status , ";
			sql += "usergroup , ";
			sql += "create_date , ";
			sql += "last_modified_date , ";
			sql += "expiration_date , ";
			sql += "bu_id ";/* added by muzees for PBG and UpJOHN */
			sql += "from users ";
			sql += "where usergroup =:userGroup";
			/* Author: Mayank Date:07-Oct-2011 SCE Enhancement 2011 */
			// sql +=
			// " where 1 = 1 and (usergroup <> 'SCE_GuestTrainer_MGR' or usergroup is null)";//commented
			// by muzees during PBG and Upjohn

			String orderBy = "";
			orderBy = " order by last_name asc, first_name asc";

			sql = sql + orderBy;

			Query q = session.createSQLQuery(sql).addEntity(User.class);
			q.setParameter("userGroup", userGroup);
			List list = q.list();

			// System.out.println("List size is " + list.size());
			Iterator it = list.iterator();

			/*
			 * while(it.hasNext()){ //
			 * System.out.println(((User)it.next()).getName()); }
			 */

			userArray = (User[]) list.toArray(new User[list.size()]);

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return userArray;

	}

	public User getUserById(Integer id) throws SQLException {

		User user = null;
		StringBuilder queryString = new StringBuilder();

		if (id != null) {
			// System.out.println("test 1");
			Session session = HibernateUtils.getHibernateSession();
			// System.out.println("test 2");

			try {

				queryString
						.append("select ")
						.append("user_id , ")
						.append("emplId , ")
						.append("last_name , ")
						.append("first_name , ")
						.append("email , ")
						.append("ntid , ")
						.append("ntdomain , ")
						.append("status , ")
						.append("usergroup , ")
						.append("create_date , ")
						.append("last_modified_date , ")
						.append("to_char(expiration_date,'mm/dd/yyyy') as  expiration_date , ")
						// . append("expiration_date ")
						.append("bu_id  ")
						// added by muzees for PBG and UpJOHN
						.append("from users ").append("where ")
						.append("user_id = :id ");

				String nativeSql = queryString.toString();

				Query q = session.createSQLQuery(nativeSql).addEntity(
						User.class);
				q.setParameter("id", id);

				List list = q.list();
				// System.out.println("List size is " + list.size());

				/*
				 * while(it.hasNext()){ //
				 * System.out.println(((User)it.next()).getName()); }
				 */

				// userArray = (User[]) list.toArray(new User[list.size()]);

				user = (list.size() > 0) ? (User) list.get(0) : null;

			} catch (HibernateException e) {
				e.printStackTrace();
				// System.out.println("Hibernatate Exception");
			} finally {
				HibernateUtils.closeHibernateSession(session);
			}
			// return ;
		}

		return user;
	}

	public void updateUser(User user) {

		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;
		// System.out.println("test 2");
		// User user = null;

		try {
			// user = (User)session.get(User.class, id);
			// user = (User)session.get(User.class, id);

			/*
			 * String sql =
			 * "UPDATE users us set us.EMPLID = :emplId ,us.FIRST_NAME = :firstName, "
			 * +
			 * "us.LAST_NAME=:lastName,us.EMAIL = :email,us.NTDOMAIN=:ntdomain,us.STATUS= "
			 * +
			 * ":status,us.USERGROUP = :userGroup,us.LAST_MODIFIED_DATE = :lastModifiedDate, "
			 * +
			 * "us.EXPIRATION_DATE =to_date( :expirationDate ,'mm/dd/yyyy') where (us.userGroup <> 'SCE_GuestTrainer_MGR' or us.userGroup is null)"
			 * + " and us.USER_ID = :userId";
			 */

			String sql = "UPDATE users us set us.EMPLID = :emplId ,us.FIRST_NAME = :firstName, "
					+ "us.LAST_NAME=:lastName,us.EMAIL = :email,us.NTDOMAIN=:ntdomain,us.STATUS= "
					+ ":status,us.USERGROUP = :userGroup,us.LAST_MODIFIED_DATE = :lastModifiedDate,us.BU_ID=:bu_id ";// bu_id
																														// added
																														// by
																														// muzees
																														// for
			if (user.getExpirationDate() != null) {
				sql = sql
						+ ",us.EXPIRATION_DATE =to_date( :expirationDate ,'mm/dd/yyyy')";
			}
			sql = sql
					+ " where (us.userGroup <> 'SCE_GuestTrainer_MGR' or us.userGroup is null)"
					+ " and us.USER_ID = :userId";

			ts = session.beginTransaction();
			Query q = session.createSQLQuery(sql);

			q.setParameter("userId", user.getId());
			q.setParameter("emplId", user.getEmplId());
			q.setParameter("firstName", user.getFirstName());
			q.setParameter("lastName", user.getLastName());
			q.setParameter("email", user.getEmail());
			q.setParameter("ntdomain", user.getNtdomain());
			q.setParameter("status", user.getStatus());
			q.setParameter("userGroup", user.getUserGroup());
			q.setParameter("bu_id", user.getBusinessUnit());// added by muzees
															// for PBG and
															// UpJOHN
			q.setParameter("lastModifiedDate", new Date());

			/*
			 * SCEUtils.stringToDate(new SimpleDateFormat("MM/dd/yyyy"),
			 * user.getExpirationDate());
			 */

			if (user.getExpirationDate() != null) {
				q.setParameter("expirationDate", user.getExpirationDate());
			}
			q.setParameter("emplId", user.getEmplId());

			int i = q.executeUpdate();

			// System.out.println("Execute Update :" + i);

			ts.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		// return user;

	}

	public void removeUser(Integer userId) {

		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;
		// System.out.println("test 2");
		// User user = null;

		try {
			// user = (User)session.get(User.class, id);
			// user = (User)session.get(User.class, id);

			String sql = "DELETE from User us where us.id = :userId";

			ts = session.beginTransaction();
			Query q = session.createQuery(sql);
			q.setParameter("userId", userId);

			int i = q.executeUpdate();
			// System.out.println("Execute Update :" + i);

			ts.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		// return user;

	}

	public User getUserByNTId(String ntid) {

		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		// System.out.println("test 2");
		User user = null;

		List ls = null;

		try {
			// user = (User)session.get(User.class, id);
			String s = "FROM User us where upper(us.ntid)=upper(:ntid)";

			Query query = session.createQuery(s);
			query.setParameter("ntid", ntid);

			ls = query.list();

			if ((ls != null) && (ls.size() > 0)) {
				return (User) ls.get(0);
			} else {
				return null;
			}

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return user;

	}

	public void createUser(User user) {
		Session session = HibernateUtils.getHibernateSession();

		try {
			// // System.out.println("quer1" + queryString);

			// String q1 = queryString.toString();
			// // System.out.println("quer2" + q1);
			// muzees added bu_id for PBG and UpJOHN
			String nativeHql = "insert into users (user_id,emplId,first_name,last_name,email,ntid, "
					+ "ntdomain,status,usergroup,create_date,last_modified_date,expiration_date,bu_id "
					+ ") values (sq_users.nextval,:emplId,:firstName,:lastName,:email,:ntid, "
					+ ":ntdomain,:status,:userGroup,sysdate,sysdate,to_date(:expirationDate,'mm/dd/yyyy'),:bu_id) ";

			Transaction ts = session.beginTransaction();

			Query query = session.createSQLQuery(nativeHql);

			query.setParameter("emplId", user.getEmplId());
			query.setParameter("firstName", user.getFirstName());
			query.setParameter("lastName", user.getLastName());
			query.setParameter("email", user.getEmail());
			query.setParameter("ntid", user.getNtid());
			query.setParameter("ntdomain", user.getNtdomain());

			query.setParameter("status", user.getStatus());
			query.setParameter("userGroup", user.getUserGroup());
			query.setParameter("expirationDate", user.getExpirationDate());
			query.setParameter("bu_id", user.getBusinessUnit());// added by
																// muzees for
																// PBG and
																// UpJOHN
			int i = query.executeUpdate();

			// System.out.println(" execute result set " + i);
			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("createUser Hibernatate Exception");

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	public SCE getSCEById(Integer sceId) {
		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();

		List<SCE> newTemp = new ArrayList<SCE>();
		try {
			// System.out.println("quer1" + queryString);
			queryString
					.append("SELECT ")
					.append("s.sce_id as id, ")
					.append("s.event_id as eventId, ")
					.append("s.emplId AS emplId, ")
					.append("s.product_cd AS productCode, ")
					.append("s.product_name AS product, ")
					.append("s.course_description AS course, ")
					.append("s.classroom AS classroom, ")
					.append("s.table_name AS tableName, ")
					.append("s.template_version_id AS templateVersionId, ")
					.append("s.precall_rating AS precallRating, ")
					.append("s.precall_comments AS precallComments, ")
					.append("s.postcall_rating AS postcallRating, ")
					.append("s.postcall_comments AS postcallComments, ")
					.append("s.comments AS comments, ")
					.append("s.hlc_compliant AS hlcCompliant, ")
					.append("s.reviewed AS reviewed, ")
					.append("s.overall_rating AS overallRating, ")
					.append("s.overall_comments AS overallComments, ")
					.append("s.submitted_date AS submittedDate, ")
					.append("s.submitted_by_emplId AS submittedByEmplId, ")
					.append("tv.form_title AS formTitle, ")
					.append("tv.legal_section_flag as legalFG, ")
					.append("s.upload_date as uploadedDate, ")
					.append("s.precall_na as precallNA, ")
					.append("s.postcall_na as postcallNA, ")
					.append("tv.precall_label as precallLabel, ")
					.append("tv.postcall_label as postcallLabel, ")
					.append("tv.precall_flag as precallFlag, ")
					.append("tv.postcall_flag as postcallFlag, ")
					/* Added by Ankit 27April 2016 */
					.append("tv.TAMPLATE_TITLE as templateTitle,  ")
					.append("tv.CALLIMAGE_FLAG as callImageDisplay,    ")
					.append("tv.OVERALL_EVALUATION_LABLE as overallEvaluationLable,    ")
					.append("PRE_CALL_IMAGE as preCallImage,  ")
					.append("POST_CALL_IMAGE as postCallImage,  ")
					.append("CALL_LABEL_DISPLAY as callLabelDisplay,  ")
					.append("CALL_LABEL_VALUE as callLabelValue  ")
					/* end */
					.append("FROM ")
					.append("sce_fft s, ")
					.append("template_version tv ")
					.append("where ")
					.append("s.template_version_id = tv.template_version_id and ")
					.append("s.sce_id = :sceid");

			System.out.println("SCE Id is :-  " + sceId);

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("sceid", sceId);
			List list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			SCE sce = null;
			while (it.hasNext()) {
				sce = new SCE();
				Object[] field = (Object[]) it.next();

				sce.setId(field[0] != null ? ((BigDecimal) field[0]).intValue()
						: null);
				sce.setEventId(field[1] != null ? ((BigDecimal) field[1])
						.intValue() : null);
				sce.setEmplId((String) field[2]);
				sce.setProductCode((String) field[3]);
				sce.setProduct((String) field[4]);
				sce.setCourse((String) field[5]);
				sce.setClassroom((String) field[6]);
				sce.setTableName((String) field[7]);

				sce.setTemplateVersionId(field[8] != null ? ((BigDecimal) field[8])
						.intValue() : null);

				sce.setPrecallRating((String) field[9]);
				sce.setPrecallComments((String) field[10]);

				sce.setPostcallRating((String) field[11]);
				sce.setPostcallComments((String) field[12]);

				sce.setComments((String) field[13]);

				sce.setHlcCompliant(field[14] != null ? ((Character) field[14])
						.toString() : null);
				sce.setReviewed(field[15] != null ? ((Character) field[15])
						.toString() : null);
				sce.setOverallRating((String) field[16]);
				sce.setOverallComments((String) field[17]);

				sce.setSubmittedDate((Date) field[18]);
				sce.setSubmittedByEmplId((String) field[19]);

				sce.setFormTitle((String) field[20]);
				sce.setLegalFG(field[21] != null ? ((Character) field[21])
						.toString() : null);

				sce.setUploadedDate((Date) field[22]);

				sce.setPrecallNA(field[23] != null ? ((Character) field[23])
						.toString() : null);
				sce.setPostcallNA(field[24] != null ? ((Character) field[24])
						.toString() : null);

				sce.setPrecallLabel((String) field[25]);
				sce.setPostcallLabel((String) field[26]);

				sce.setPrecallFlag(field[27] != null ? ((Character) field[27])
						.toString() : null);
				sce.setPostcallFlag(field[28] != null ? ((Character) field[28])
						.toString() : null);

				/* Added by Ankit 27April 2016 */
				sce.setTemplatetitle(field[29] != null ? ((String) field[29])
						: "Sales Call Evaluation");

				sce.setCallImageDisplay(field[30] != null ? field[30]
						.toString().toLowerCase().trim().equals("true") ? true
						: (field[30].toString().trim().equals("1") ? true
								: false) : true);

				sce.setOverallEvaluationLable(field[31] != null ? field[31]
						.toString() : "Overall Sales Call Evaluation");

				sce.setPreCallImage((field[32] != null) ? (field[32].toString())
						: "Y");

				sce.setPostCallImage((field[33] != null) ? (field[33]
						.toString()) : "Y");

				sce.setCallLabelDisplay((field[34] != null) ? (field[34]
						.toString()) : "Y");

				sce.setCallLabelValue((field[35] != null) ? (field[35]
						.toString()) : "Call Section");
				/* end */

				newTemp.add(sce);

			}

			// System.out.println("New User List Array Length" +
			// newTemp.size());

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return (newTemp.size() > 0) ? newTemp.get(0) : null;

	}

	public SCEDetail[] getSCEDetailsBySCEId(Integer sceId) {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		List<SCEDetail> newTemp = new ArrayList<SCEDetail>();
		SCEDetail[] sceArray = null;

		try {
			// System.out.println("quer1" + queryString);
			queryString
					.append("select ")
					.append("sq.sce_detail_id as id, ")
					.append("sq.sce_id as sceId, ")
					.append("sq.question_id as questionId, ")
					.append("sq.question_rating as questionRating, ")
					.append("sq.question_comments as questionComments, ")
					.append("sq.question_score1 as questionScore1, ")
					.append("sq.question_score2 as questionScore2, ")
					.append("sq.question_score3 as questionScore3, ")
					.append("sq.question_fg as questionFg, ")
					.append("q.title as title, ")
					.append("q.display_order as displayOrder, ")
					.append("q.description as description, ")
					.append("q.critical as critical, ")
					.append("q.template_version_id as templateVersionId, ")
					.append("q.type as type, ")
					.append("q.weight as weight, ")
					.append("tv.hlc_critical as hlcCritical, ")
					.append("tv.LEGAL_SECTION_FLAG as legalFG, ")
					.append("tv.HLC_CRITICAL_VALUE as hlcCriticalValue, ")
					.append("tv.CONFLICT_OVERALL_SCORE as conflictOverallScore ")
					.append("from ")
					.append("SCE_DETAIL sq, QUESTION q, TEMPLATE_VERSION tv ")
					.append("where ")
					.append("sq.QUESTION_ID = q.question_id and ")
					.append("q.TEMPLATE_VERSION_ID=tv.TEMPLATE_VERSION_ID and ")
					.append("sq.SCE_ID = :sceid ").append("order by ")
					.append("display_order asc ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("sceid", sceId);
			List list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			SCEDetail sceDetail = null;
			while (it.hasNext()) {
				sceDetail = new SCEDetail();
				Object[] field = (Object[]) it.next();

				sceDetail.setId(field[0] != null ? ((BigDecimal) field[0])
						.intValue() : null);
				sceDetail.setSceId(field[1] != null ? ((BigDecimal) field[1])
						.intValue() : null);
				sceDetail
						.setQuestionId(field[2] != null ? ((BigDecimal) field[2])
								.intValue() : null);

				sceDetail.setQuestionRating((String) field[3]);
				sceDetail.setQuestionComments((String) field[4]);

				sceDetail.setQuestionScore1((Double) field[5]);
				sceDetail.setQuestionScore2((Double) field[6]);
				sceDetail.setQuestionScore3((Double) field[7]);

				sceDetail
						.setQuestionFg(field[8] != null ? ((Character) field[8])
								.toString() : null);
				sceDetail.setTitle((String) field[9]);

				sceDetail
						.setDisplayOrder(field[10] != null ? ((BigDecimal) field[10])
								.intValue() : null);

				sceDetail.setDescription((String) field[11]);

				sceDetail
						.setCritical(field[12] != null ? ((Character) field[12])
								.toString() : null);
				sceDetail
						.setTemplateVersionId(field[13] != null ? ((BigDecimal) field[13])
								.intValue() : null);

				sceDetail.setType((String) field[14]);
				sceDetail.setWeight((Double) field[15]);

				sceDetail
						.setHlcCritical(field[16] != null ? ((Character) field[16])
								.toString() : null);

				sceDetail
						.setLegalFG(field[17] != null ? ((Character) field[17])
								.toString() : null);
				sceDetail.setHLCCriticalValue((String) field[18]);

				sceDetail
						.setConflictOverallScore(field[19] != null ? ((String) field[19])
								: null);

				newTemp.add(sceDetail);
			}

			// System.out.println("New User List Array Length" +
			// newTemp.size());
			// User[] userarray = (User[]) newListUser.toArray();
			sceArray = newTemp.toArray(new SCEDetail[newTemp.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return sceArray;

	}

	public Descriptor[] getDescriptorsBySCEId(Integer sceId) {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		List<Descriptor> newTemp = new ArrayList<Descriptor>();
		Descriptor[] descArray = null;

		try {
			// System.out.println("quer12" + queryString);
			queryString
					.append("select ")
					.append("descriptor_id as id, ")
					.append("question_id as questionId, ")
					.append("description as description ")
					.append("from ")
					.append("descriptor ")
					.append("where ")
					.append("question_id in ")
					.append("( ")
					.append("select question_id from sce_detail where sce_id = :sceId ")
					.append(") ")
					.append("order by question_id asc, descriptor_id asc ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("sceId", sceId);
			List list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			Descriptor descriptor = null;
			while (it.hasNext()) {
				descriptor = new Descriptor();
				Object[] field = (Object[]) it.next();

				descriptor.setId(field[0] != null ? ((BigDecimal) field[0])
						.intValue() : null);
				descriptor.setDescription((String) field[2]);
				descriptor
						.setQuestionId(field[1] != null ? ((BigDecimal) field[1])
								.intValue() : null);
				newTemp.add(descriptor);
			}

			// System.out.println("New User List Array Length" +
			// newTemp.size());

			descArray = newTemp.toArray(new Descriptor[newTemp.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getDescriptorsBySCEId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return descArray;

	}

	public EvaluationFormScore[] getEvaluationFormScore(
			Integer evaluationTemplateId) throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		List<EvaluationFormScore> newTemp = new ArrayList<EvaluationFormScore>();
		EvaluationFormScore[] evalFormArray = null;

		try {
			// System.out.println("quer1" + queryString);
			queryString
					.append("SELECT  ")
					.append("ID AS Id, ")
					.append("FS.SCORING_SYSTEM_IDENTIFIER AS scoringSystemIdentifier, ")
					.append("SI.SCORE_VALUE AS scoreValue, ")
					.append("SI.SCORE_LEGEND AS scoreLegend, ")
					.append("FS.NOTIFICATION_FLAG AS notificationFG, ")
					.append("FS.LMS_FLAG AS lmsFlag, ")
					.append("FS.SCORE_COMMENT AS scorecommentFlag ")
					.append("from  ")
					.append("EVALUATION_FORM_SCORE FS, ")
					.append("SCORING_SYSTEM SI ")
					.append("WHERE  ")
					.append("FS.SCORING_SYSTEM_IDENTIFIER= SI.SCORING_SYSTEM_IDENTIFIER ")
					.append("AND FS.SCORE_VALUE=SI.SCORE_VALUE ")
					.append("AND FS.TEMPLATE_VERSION_ID= :evaluationTemplateId ")
					.append("ORDER BY score_ID ASC ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("evaluationTemplateId", evaluationTemplateId);
			List<Object> list = q.list();

			// user = list;
			// System.out.println("size 1 " + list.size());
			Iterator<Object> it = list.iterator();

			EvaluationFormScore evaluationFormScore = null;
			while (it.hasNext()) {
				evaluationFormScore = new EvaluationFormScore();
				Object[] field = (Object[]) it.next();

				evaluationFormScore
						.setId(field[0] != null ? ((BigDecimal) field[0])
								.intValue() : null);

				evaluationFormScore
						.setScoringSystemIdentifier((String) field[1]);
				evaluationFormScore.setScoreValue((String) field[2]);
				evaluationFormScore.setScoreLegend((String) field[3]);

				evaluationFormScore
						.setNotificationFG(field[4] != null ? ((Character) field[4])
								.toString() : null);
				evaluationFormScore
						.setLmsFlag(field[5] != null ? ((Character) field[5])
								.toString() : null);
				evaluationFormScore.setScorecommentFlag((String) field[6]);

				newTemp.add(evaluationFormScore);
			}

			// System.out.println("New User List Array Length" +
			// newTemp.size());

			evalFormArray = newTemp.toArray(new EvaluationFormScore[newTemp
					.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getDescriptorsBySCEId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return evalFormArray;

	}

	public LegalQuestion[] getLegalQuestion(Integer evaluationTemplateId)
			throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		List<LegalQuestion> newTemp = new ArrayList<LegalQuestion>();
		LegalQuestion[] legalQuestionArray = null;

		try {
			// System.out.println("quer1" + queryString);
			queryString
					.append("SELECT ")
					.append("LQ.LEGAL_QUESTION_ID AS id ,  ")
					.append("LQ.DISPLAY_ORDER AS displayOrder, ")
					.append("LQ.LEGAL_QUESTION_LABEL AS legalQuestionLabel, ")
					.append("LQ.LEGAL_QUESTION AS legalQuestion ")
					.append("from legal_questions LQ ")
					.append("WHERE LQ.TEMPLATE_VERSION_ID = :evaluationTemplateId ")
					.append("ORDER BY ").append("DISPLAY_ORDER ASC ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("evaluationTemplateId", evaluationTemplateId);
			List<Object> list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator<Object> it = list.iterator();

			LegalQuestion legalQuestion = null;
			while (it.hasNext()) {
				legalQuestion = new LegalQuestion();
				Object[] field = (Object[]) it.next();

				legalQuestion.setId(field[0] != null ? ((BigDecimal) field[0])
						.intValue() : null);
				legalQuestion
						.setDisplayOrder(field[1] != null ? ((BigDecimal) field[1])
								.intValue() : null);
				legalQuestion.setLegalQuestionLabel((String) field[2]);
				legalQuestion.setLegalQuestion((String) field[3]);

				newTemp.add(legalQuestion);
			}

			// System.out.println("New User List Array Length" +
			// newTemp.size());

			legalQuestionArray = newTemp.toArray(new LegalQuestion[newTemp
					.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getDescriptorsBySCEId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return legalQuestionArray;
	}

	public LegalQuestionDetail[] getLegalQuestionDetail(Integer sceId)
			throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		List<LegalQuestionDetail> newTemp = new ArrayList<LegalQuestionDetail>();
		LegalQuestionDetail[] legalQuestionArray = null;

		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("SELECT LQ.LEGAL_QUESTION_ID AS legalQuestionId,  ")
					.append("LQ.SCE_ID AS sceId, ")
					.append("LQ.LEGAL_QUESTION_VALUE AS legalQuestionValue ")
					.append("FROM LEGAL_QUESTION_DETAIL LQ ").append("WHERE  ")
					.append("LQ.SCE_ID = :sceId ").append("order by ")
					.append("LQ.LEGAL_QUESTION_ID asc ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("sceId", sceId);

			List<Object> list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator<Object> it = list.iterator();

			LegalQuestionDetail legalQuestionDetail = null;
			while (it.hasNext()) {
				legalQuestionDetail = new LegalQuestionDetail();
				Object[] field = (Object[]) it.next();
				legalQuestionDetail
						.setLegalQuestionId(field[0] != null ? ((BigDecimal) field[0])
								.intValue() : null);
				legalQuestionDetail
						.setSceId(field[0] != null ? ((BigDecimal) field[0])
								.toString() : null);

				legalQuestionDetail.setLegalQuestionValue((String) field[2]);

				newTemp.add(legalQuestionDetail);
			}

			// System.out.println("New User List Array Length" +
			// newTemp.size());

			legalQuestionArray = newTemp
					.toArray(new LegalQuestionDetail[newTemp.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getDescriptorsBySCEId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return legalQuestionArray;

	}

	public Blob getByteArray(Integer sceId) throws SQLException {

		// System.out.println("*******************in getByteArray");
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List salesType = new ArrayList();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			/*
			 * queryString .append("select  ") .append("uploaded_form ")
			 * .append("from sce_fft ") .append("where sce_id = :sceId ");
			 */

			queryString.append("SELECT  ").append("uploadedForm ")
					.append("from SCE ").append("where id = :sceId ");

			String q1 = queryString.toString();
			Query q = session.createQuery(q1);
			q.setParameter("sceId", sceId);

			salesType = q.list();

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();

			// log.error("HibernateException in getSalesPositionTypeCd", e);
			// System.out.println("HibernateException in getSalesPositionTypeCd");
		}

		catch (Exception e) {
			e.printStackTrace();
			// System.out.println("HibernateException in getSalesPositionTypeCd");
		}

		finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return (salesType.size() > 0) ? (Blob) salesType.get(0) : null;

	}

	public SCE[] getSCESHistoryByEventEmplId(Integer eventId, String emplId,
			String productName) throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		SCE[] sceArray = null;
		List<SCE> sceObjList = new ArrayList<SCE>();

		try {
			queryString
					.append("SELECT  ")
					.append("id, eventId, emplId, productCode, product, course, classroom, tableName, overallRating, overallScore, submittedDate, status ,uploadedDate ")
					.append("FROM ")
					.append("( ")
					.append("(SELECT ")
					.append("s.sce_id AS id, ")
					.append("s.event_id as eventId, ")
					.append("s.emplId AS emplId, ")
					.append("s.product_cd AS productCode, ")
					.append("s.product_name AS product, ")
					.append("s.course_description as course, ")
					.append("s.classroom AS classroom, ")
					.append("s.table_name AS tableName,      ")
					.append("s.overall_rating AS overallRating, ")
					.append("s.overall_score AS overallScore, ")
					.append("s.submitted_date AS submittedDate, ")
					.append("s.status AS status, ")
					.append("s.upload_date AS uploadedDate ")
					.append("FROM ")
					.append("SCE_FFT s ")
					.append("WHERE ")
					.append("event_id = (case when :eventId = -1 then event_id else :eventId end) ")
					.append("and emplId = :emplId ")
					.append("and status = 'SUBMITTED' ")
					.append("and product_name=:productName ")
					.append(") ")
					.append("UNION ")
					.append("(SELECT ")
					.append("NULL AS id, ")
					.append("v.event_id as eventId, ")
					.append("v.emplId AS emplId, ")
					.append("v.product_cd AS productCode, ")
					.append("v.product_name AS product, ")
					.append("v.course_description as course, ")
					.append("v.classroom AS classroom, ")
					.append("v.table_name AS tableName,      ")
					.append("NULL AS overallRating, ")
					.append("NULL AS overallScore, ")
					.append("NULL  AS submittedDate, ")
					.append("NULL AS status, ")
					.append("NULL  AS uploadedDate ")
					.append("FROM ")
					.append("v_sce_source v ")
					.append("WHERE ")
					.append("event_id = (case when :eventId = -1 then event_id else :eventId end) ")
					.append("and emplId = :emplId and ")
					.append("product_name=:productName and ")
					.append("(v.event_id, v.emplId, v.product_cd) NOT IN  ")
					.append("(SELECT DISTINCT s.event_id, s.emplId, s.product_cd FROM  ")
					.append("SCE_FFT s) ")
					.append(") ")
					.append("UNION ")
					.append("(SELECT ")
					.append("NULL AS id, ")
					.append("v.event_id as eventId, ")
					.append("v.emplId AS emplId, ")
					.append("v.product_cd AS productCode, ")
					.append("v.product_name AS product, ")
					.append("v.course_description as course, ")
					.append("v.classroom AS classroom, ")
					.append("v.table_name AS tableName,      ")
					.append("NULL AS overallRating, ")
					.append("NULL AS overallScore, ")
					.append("NULL  AS submittedDate, ")
					.append("NULL AS status, ")
					.append("NULL  AS uploadedDate ")
					.append("FROM ")
					.append("v_sce_p2l_source v ")
					.append("WHERE ")
					.append("event_id = (case when :eventId = -1 then event_id else :eventId end) ")
					.append("and emplId = :emplId and ")
					.append("product_name=:productName and ")
					.append("(v.event_id, v.emplId, v.product_cd) NOT IN  ")
					.append("(SELECT DISTINCT s.event_id, s.emplId, s.product_cd FROM  ")
					.append("SCE_FFT s) ").append(") ").append(") ")
					.append("ORDER BY ").append("id desc,submittedDate desc ");//2020 Q4:MUZESS changed the order by from  submittedDate Desc,id desc to id desc,submittedDate Desc

			String q1 = queryString.toString();

			Query q = session
					.createSQLQuery(q1)
					.addScalar("id")
					.addScalar("eventId")
					.addScalar("emplId")
					.addScalar("productCode")
					.addScalar("product")
					.addScalar("course")
					.addScalar("classroom")
					.addScalar("tableName")
					.addScalar("overallRating")
					.addScalar("overallScore")
					.addScalar("submittedDate",
							org.hibernate.type.TimestampType.INSTANCE)
					.addScalar("status")
					.addScalar("uploadedDate",
							org.hibernate.type.TimestampType.INSTANCE);

			q.setParameter("eventId", eventId);
			q.setParameter("emplId", emplId);
			q.setParameter("productName", productName);

			List<Object> list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator<Object> it = list.iterator();

			SCE sceObj = null;
			while (it.hasNext()) {
				sceObj = new SCE();
				Object[] field = (Object[]) it.next();

				sceObj.setId(field[0] != null ? ((BigDecimal) field[0])
						.intValue() : null);

				sceObj.setEventId((field[1] != null) ? ((BigDecimal) field[1])
						.intValue() : null);
				sceObj.setEmplId((String) field[2]);
				sceObj.setProductCode((String) field[3]);
				sceObj.setProduct((String) field[4]);
				sceObj.setCourse((String) field[5]);
				sceObj.setClassroom((String) field[6]);
				sceObj.setTableName((String) field[7]);
				sceObj.setOverallRating((String) field[8]);
				sceObj.setOverallScore((field[9] != null) ? ((BigDecimal) field[9])
						.doubleValue() : null);
				sceObj.setSubmittedDate((Date) field[10]);
				sceObj.setStatus((String) field[11]);
				sceObj.setUploadedDate(((Date) field[12]));

				sceObjList.add(sceObj);

			}

			System.out
					.println("New User List Array Length" + sceObjList.size());

			sceArray = sceObjList.toArray(new SCE[sceObjList.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getDescriptorsBySCEId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return sceArray;

	}

	public void deleteSCE(Integer sceid) {

		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		// System.out.println("test 2");

		// List ls = null;

		try {

			Transaction ts = session.beginTransaction();

			Query query = session.createSQLQuery("CALL SP_DELETE_SCE(:sceID)")
					.setParameter("sceID", sceid);
			int i = 0;
			i = query.executeUpdate();
			ts.commit();

			// System.out.println("value of i" + i);

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			// System.out.println("finally");
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public SCEDetail[] getSCEDetailsLatest(Integer eventId, String course,
			String productName) {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		SCEDetail[] sceDetailArray = null;
		List<SCEDetail> sceObjList = new ArrayList<SCEDetail>();

		try {
			queryString
					.append("SELECT ")
					.append("q.question_id AS questionId, ")
					.append("q.template_version_id AS templateVersionId, ")
					.append("q.title AS title, ")
					.append("q.display_order AS displayOrder, ")
					.append("q.description AS description, ")
					.append("q.critical AS critical, ")
					.append("q.type AS type, ")
					.append("q.weight AS weight, ")
					.append("tv.form_title AS formTitle, ")
					.append("tv.precall_comments as precallComments, ")
					.append("tv.postcall_comments as postcallComments, ")
					.append("tv.precall_label as precallLabel, ")
					.append("tv.postcall_label as postcallLabel, ")
					.append("tv.precall_flag as precallFlag, ")
					.append("tv.postcall_flag as postcallFlag, ")
					.append("tv.hlc_critical as hlcCritical, ")
					.append("tv.LEGAL_SECTION_FLAG as legalFG, ")
					.append("SC.SCORE_LEGEND as hlcCriticalValue, ")
					.append("SC1.SCORE_LEGEND as conflictOverallScore,      ")
					.append("tv.TAMPLATE_TITLE as Templateitle,    ")
					.append("tv.CALLIMAGE_FLAG as callImageDisplay,    ")
					.append("tv.OVERALL_EVALUATION_LABLE as overallEvaluationLable,    ")
					.append("PRE_CALL_IMAGE as preCallImage,  ")
					.append("POST_CALL_IMAGE as postCallImage,  ")
					.append("CALL_LABEL_DISPLAY as callLabelDisplay,  ")
					.append("CALL_LABEL_VALUE as callLabelValue  ")
					.append("FROM ")
					.append("QUESTION q, ")
					.append("TEMPLATE_VERSION tv, ")
					.append("TEMPLATE t, ")
					.append("EVENT_COURSE ec, ")
					.append("SCORING_SYSTEM SC, ")
					.append("SCORING_SYSTEM SC1 ")
					.append("WHERE  ")
					.append("q.template_version_id = tv.template_version_id AND ")
					.append("tv.template_id = t.template_id AND ")
					.append("ec.template_id = t.template_id AND ")
					.append("ec.event_id = :eventId and ec.course_name = :course ")
					.append("AND TV.HLC_CRITICAL_VALUE=SC.SCORE_VALUE(+) ")
					.append("AND TV.CONFLICT_OVERALL_SCORE=SC1.SCORE_VALUE(+) ")
					.append("and tv.template_version_id=(SELECT max(template_version_id)  ")
					.append("from template_version where template_id=(select distinct(evaluation_template_id) ")
					.append("from lms_course_mapping where lms_course_name= :productName )and Publish_flag='Y') ")
					.append("AND SC.SCORING_SYSTEM_IDENTIFIER(+)=TV.SCORING_SYSTEM_IDENTIFIER ")
					.append("AND SC1.SCORING_SYSTEM_IDENTIFIER(+)=TV.SCORING_SYSTEM_IDENTIFIER ")
					.append("order by ").append("display_order asc ");

			String q1 = queryString.toString();

			Query q = session.createSQLQuery(q1);

			q.setParameter("eventId", eventId);
			q.setParameter("course", course);
			q.setParameter("productName", productName);

			System.out.println("query q: " + q.toString());

			System.out.println("Params" + eventId + " " + course + " "
					+ productName);

			System.out.println("query q1: " + q.toString());

			List<Object> list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator<Object> it = list.iterator();

			SCEDetail sceDetail = null;
			while (it.hasNext()) {
				sceDetail = new SCEDetail();
				Object[] field = (Object[]) it.next();

				sceDetail
						.setQuestionId(field[0] != null ? ((BigDecimal) field[0])
								.intValue() : null);
				sceDetail
						.setTemplateVersionId(field[1] != null ? ((BigDecimal) field[1])
								.intValue() : null);
				sceDetail.setTitle((String) field[2]);

				sceDetail
						.setDisplayOrder(field[3] != null ? ((BigDecimal) field[3])
								.intValue() : null);
				sceDetail.setDescription((String) field[4]);

				sceDetail.setCritical(field[5] != null ? ((Character) field[5])
						.toString() : null);
				sceDetail.setType((String) field[6]);

				sceDetail.setWeight((Double) field[7]);
				sceDetail.setFormTitle((String) field[8]);

				sceDetail.setPrecallComments((String) field[9]);
				sceDetail.setPostcallComments((String) field[10]);

				sceDetail.setPrecallLabel((String) field[11]);
				sceDetail.setPostcallLabel((String) field[12]);

				sceDetail
						.setPrecallFlag(field[13] != null ? ((Character) field[13])
								.toString() : null);
				sceDetail
						.setPostcallFlag(field[14] != null ? ((Character) field[14])
								.toString() : null);

				sceDetail
						.setHlcCritical(field[15] != null ? ((Character) field[15])
								.toString() : null);
				sceDetail
						.setLegalFG(field[16] != null ? ((Character) field[16])
								.toString() : null);
				sceDetail.setHLCCriticalValue((String) field[17]);
				sceDetail.setConflictOverallScore((String) field[18]);

				/* Added by Ankit 27 April2016 */
				System.out.println("Template Title" + (String) field[19]);

				sceDetail
						.setTemplatetitle(field[19] != null ? (String) field[19]
								: "Sales Call Evaluation");

				sceDetail.setCallImageDisplay(field[20] != null ? field[20]
						.toString().toLowerCase().trim().equals("true") ? true
						: (field[20].toString().trim().equals("1") ? true
								: false) : true);

				sceDetail
						.setOverallEvaluationLable(field[21] != null ? field[21]
								.toString() : "Overall Sales Call Evaluation");

				sceDetail.setPreCallImage((field[22] != null) ? (field[22]
						.toString()) : "Y");

				sceDetail.setPostCallImage((field[23] != null) ? (field[23]
						.toString()) : "Y");

				sceDetail.setCallLabelDisplay((field[24] != null) ? (field[24]
						.toString()) : "Y");

				sceDetail.setCallLabelValue((field[25] != null) ? (field[25]
						.toString()) : "Call Section");
				/* End */
				sceObjList.add(sceDetail);
			}

			System.out
					.println("New User List Array Length" + sceObjList.size());
			sceDetailArray = sceObjList
					.toArray(new SCEDetail[sceObjList.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getDescriptorsBySCEId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return sceDetailArray;

	}

	public BusinessRule[] getBusinessRules(Integer evaluationTemplateId)
			throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		BusinessRule[] businessRuleArray = null;
		List<BusinessRule> sceObjList = new ArrayList<BusinessRule>();

		try {
			queryString.append("select business_rule_id as businessRuleId, ")
					.append("display_order as displayOrder, ")
					.append("critical_questions as criticalQuestions, ")
					.append("sc.score_legend as score, ")
					.append("sc1.score_legend as overallScore ")
					.append("from ").append("business_rules BR, ")
					.append("SCORING_SYSTEM SC, ")
					.append("SCORING_SYSTEM SC1 ").append("where  ")
					.append("BR.SCORE=SC.SCORE_VALUE(+) ")
					.append("AND BR.OVERALL_SCORE=SC1.SCORE_VALUE(+) ")
					.append("and template_version_id = :evaluationTemplateId ")
					.append("order by  ").append("display_order asc ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			q.setParameter("evaluationTemplateId", evaluationTemplateId);

			List<Object> list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator<Object> it = list.iterator();

			BusinessRule businessRule = null;
			while (it.hasNext()) {
				businessRule = new BusinessRule();
				Object[] field = (Object[]) it.next();

				businessRule
						.setBusinessRuleId(field[0] != null ? ((BigDecimal) field[0])
								.intValue() : null);
				businessRule
						.setDisplayOrder(field[1] != null ? ((BigDecimal) field[1])
								.intValue() : null);
				businessRule
						.setCriticalQuestions(field[2] != null ? ((BigDecimal) field[2])
								.intValue() : null);
				businessRule.setScore((String) field[3]);
				businessRule.setOverallScore((String) field[4]);

				sceObjList.add(businessRule);
			}

			System.out
					.println("New User List Array Length" + sceObjList.size());
			businessRuleArray = sceObjList.toArray(new BusinessRule[sceObjList
					.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getDescriptorsBySCEId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return businessRuleArray;

	}

	public Descriptor[] getDescriptorsLatest(Integer eventId, String course) {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		Descriptor[] descArray = null;
		List<Descriptor> sceObjList = new ArrayList<Descriptor>();

		try {
			queryString
					.append("SELECT ")
					.append("descriptor_id AS id, ")
					.append("question_id AS questionId, ")
					.append("description AS description ")
					.append("FROM ")
					.append("DESCRIPTOR ")
					.append("WHERE ")
					.append("question_id IN ")
					.append("( ")
					.append("SELECT ")
					.append("question_id ")
					.append("FROM ")
					.append("QUESTION q, ")
					.append("TEMPLATE_VERSION tv, ")
					.append("EVENT_COURSE ec, ")
					.append("(select template_id,form_title,max(version) version from template_version where publish_flag='Y' group by (form_title,template_id)) a ")
					.append("WHERE  ")
					.append("ec.template_id = tv.template_id ")
					.append("and tv.template_version_id = q.template_version_id ")
					.append("and tv.version=a.version ")
					.append("and tv.TEMPLATE_ID=a.TEMPLATE_ID ")
					.append("and ec.event_id = :eventId  ")
					.append("and ec.course_name = :course ").append(") ")
					.append("order by question_id asc, descriptor_id asc ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			q.setParameter("eventId", eventId);
			q.setParameter("course", course);

			List<Object> list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator<Object> it = list.iterator();

			Descriptor descriptor = null;
			while (it.hasNext()) {
				descriptor = new Descriptor();
				Object[] field = (Object[]) it.next();

				descriptor.setId(field[0] != null ? ((BigDecimal) field[0])
						.intValue() : null);
				descriptor
						.setQuestionId(field[1] != null ? ((BigDecimal) field[1])
								.intValue() : null);
				descriptor.setDescription((String) field[2]);

				sceObjList.add(descriptor);
			}

			System.out
					.println("New User List Array Length" + sceObjList.size());
			descArray = sceObjList.toArray(new Descriptor[sceObjList.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getDescriptorsBySCEId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return descArray;

	}

	/**
	 * @jc:sql statement="{call SP_FFT_AUTOCREDIT_UI(?,?,?,?,?,?,?,?)}"
	 */
	public void giveAutoCredit(String emplId, Integer eventId,
			String productCode, String product, String course,
			String classroom, String tableName, String submittedByEmplId) {

		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		// System.out.println("test 2");

		// List ls = null;

		try {

			Transaction ts = session.beginTransaction();

			/*
			 * Query query = session .createSQLQuery(
			 * "call SP_FFT_AUTOCREDIT_UI(:emplId,:eventId,:productCode,:product,:course,:classroom,:tableName,:submittedByEmplId)"
			 * );
			 */
			Query query = session
					.createSQLQuery("call SP_FFT_AUTOCREDIT_UI(:emplId,:productCode,:product,:course,:classroom,:tableName,:submittedByEmplId ,:eventId)");

			query.setParameter("emplId", emplId);
			query.setParameter("eventId", eventId);
			query.setParameter("productCode", productCode);
			query.setParameter("product", product);
			query.setParameter("course", course);

			query.setParameter("classroom", classroom);
			query.setParameter("tableName", tableName);
			query.setParameter("submittedByEmplId", submittedByEmplId);

			int i = 0;
			i = query.executeUpdate();
			ts.commit();

			// System.out.println("value of i" + i);

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			// System.out.println("finally");
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public SCEReport[] getPendingAttendeesByCourseAndClass(String query) {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		SCEReport[] sceReportArray = null;
		List<SCEReport> sceRepList = new ArrayList<SCEReport>();

		try {
			Query q = session.createSQLQuery(query);

			List<Object> list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator<Object> it = list.iterator();

			SCEReport sceReport = null;
			while (it.hasNext()) {
				sceReport = new SCEReport();
				Object[] field = (Object[]) it.next();

				// sceReport.setId(field[0] != null ? ((BigDecimal)
				// field[0]).intValue() : null);
				// sceReport.setQuestionId(field[1] != null ? ((BigDecimal)
				// field[1]).intValue() : null);
				// sceReport.setDescription((String)field[2]);

				sceRepList.add(sceReport);
			}

			System.out
					.println("New User List Array Length" + sceRepList.size());
			sceReportArray = sceRepList
					.toArray(new SCEReport[sceRepList.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getDescriptorsBySCEId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return sceReportArray;

	}

	public Event getEventById(Integer eventId) {

		// // System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		// // System.out.println("test 2");
		Event event = null;

		try {
			// user = (User)session.get(User.class, id);
			event = (Event) session.get(Event.class, eventId);

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return event;

	}

	public Attendee[] getOtherPendingAttendeesByFullCriteria(Integer eventId,
			java.util.Date trainingDate, String productCode, String course,
			String classroom, String table, String excludeEmplId) {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		Attendee[] attendeeArray = null;

		List<Attendee> attendeeList = new ArrayList<Attendee>();

		try {
			queryString
					.append("select distinct ")
					.append("v.emplId as emplId, ")
					.append("v.first_name as firstName, ")
					.append("v.last_name as lastName, ")
					.append("v.sales_position_id as salesPositionId, ")
					.append("v.role_cd as roleCd, ")
					.append("v.bu as bu, ")
					.append("v.sales_group as salesOrgDesc ")
					.append("from ")
					.append("mv_field_employee_rbu v, ")
					.append("( ")
					.append("Select  ")
					.append("vs.EMPLID, sce.EMPLID secondID,  ")
					.append("vs.event_id, vs.product_cd, vs.course_description as course,      ")
					.append("vs.classRoom, vs.table_name, ")
					.append("vs.start_time, vs.end_time ")
					.append("from v_sce_source vs, sce_fft sce ")
					.append("where ")
					.append("vs.emplid = sce.emplid(+) and ")
					.append("sce.product_cd(+) = vs.product_cd and ")
					.append("vs.event_id = sce.event_id(+) ")
					.append(") ")
					.append("tmp ")
					.append("where ")
					.append("v.EMPLID = tmp.emplid and ")
					.append("tmp.secondID is null and ")
					.append("tmp.event_id = :eventId and ")
					.append(":trainingDate between trunc(tmp.start_time) and trunc(tmp.end_time) and ")
					.append("tmp.product_cd = :productCode and ")
					.append("tmp.course = :course and ")
					.append("v.EMPLID <> :excludeEmplId ").append("and  ")
					.append("tmp.classRoom = :classroom ").append("and  ")
					.append("tmp.table_name = :table ");

			String query = queryString.toString();
			Query q = session.createSQLQuery(query);

			q.setParameter("eventId", eventId);
			q.setParameter("trainingDate", trainingDate);
			q.setParameter("productCode", productCode);
			q.setParameter("course", course);
			q.setParameter("classroom", classroom);
			q.setParameter("table", table);
			q.setParameter("excludeEmplId", excludeEmplId);

			List<Object> list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator<Object> it = list.iterator();

			Attendee attendee = null;
			while (it.hasNext()) {
				attendee = new Attendee();
				Object[] field = (Object[]) it.next();

				attendee.setEmplId((String) field[0]);
				attendee.setFirstName((String) field[1]);
				attendee.setLastName((String) field[2]);
				attendee.setSalesPositionId((String) field[3]);
				attendee.setRoleCd((String) field[4]);
				attendee.setBu((String) field[5]);
				attendee.setSalesOrgDesc((String) field[6]);

				attendeeList.add(attendee);
			}

			// System.out.println("New User List Array Length"+
			// attendeeList.size());
			attendeeArray = attendeeList.toArray(new Attendee[attendeeList
					.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getDescriptorsBySCEId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return attendeeArray;

	}

	public Attendee[] getPendingAttendeesByFullCriteria(Integer eventId,
			java.util.Date trainingDate, String productCode, String course,
			String classroom, String table) {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		Attendee[] attendeeArray = null;

		List<Attendee> attendeeList = new ArrayList<Attendee>();

		try {
			queryString
					.append("select distinct ")
					.append("v.emplId as emplId, ")
					.append("v.first_name as firstName, ")
					.append("v.last_name as lastName, ")
					.append("v.sales_position_id as salesPositionId, ")
					.append("v.role_cd as roleCd, ")
					.append("v.bu as bu, ")
					.append("v.sales_group as salesOrgDesc ")
					.append("from ")
					.append("mv_field_employee_rbu v, ")
					.append("( ")
					.append("Select  ")
					.append("vs.EMPLID, sce.EMPLID secondID,  ")
					.append("vs.event_id, vs.product_cd, vs.course_description as course,      ")
					.append("vs.classRoom, vs.table_name, ")
					.append("vs.start_time, vs.end_time ")
					.append("from v_sce_source vs, sce_fft sce ")
					.append("where ")
					.append("vs.emplid = sce.emplid(+) and ")
					.append("sce.product_cd(+) = vs.product_cd and ")
					.append("vs.event_id = sce.event_id(+) ")
					.append(") ")
					.append("tmp ")
					.append("where ")
					.append("v.EMPLID = tmp.emplid and ")
					.append("tmp.secondID is null and ")
					.append("tmp.event_id = :eventId and ")
					.append(" :trainingDate between trunc(tmp.start_time) and trunc(tmp.end_time) and ")
					.append("tmp.product_cd = :productCode and ")
					.append("tmp.course = :course ").append("and  ")
					.append("tmp.classRoom = :classroom ").append("and  ")
					.append("tmp.table_name = :table ");

			String query = queryString.toString();
			Query q = session.createSQLQuery(query);

			q.setParameter("eventId", eventId);
			q.setParameter("trainingDate", trainingDate);
			q.setParameter("productCode", productCode);
			q.setParameter("course", course);
			q.setParameter("classroom", classroom);
			q.setParameter("table", table);
			// q.setParameter("excludeEmplId", excludeEmplId);

			List<Object> list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator<Object> it = list.iterator();

			Attendee attendee = null;
			while (it.hasNext()) {
				attendee = new Attendee();
				Object[] field = (Object[]) it.next();

				attendee.setEmplId((String) field[0]);
				attendee.setFirstName((String) field[1]);
				attendee.setLastName((String) field[2]);
				attendee.setSalesPositionId((String) field[3]);
				attendee.setRoleCd((String) field[4]);
				attendee.setBu((String) field[5]);
				attendee.setSalesOrgDesc((String) field[6]);

				attendeeList.add(attendee);
			}

			// System.out.println("New User List Array Length"+
			// attendeeList.size());
			attendeeArray = attendeeList.toArray(new Attendee[attendeeList
					.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getDescriptorsBySCEId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return attendeeArray;

	}

	public User getUserByEmplId(String emplId) {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		User user = null;
		List<User> ls = new ArrayList<User>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString.append("FROM User us where us.emplId = :emplId ");

			String q1 = queryString.toString();
			Query q = session.createQuery(q1);

			q.setParameter("emplId", emplId);
			ls = q.list();
			// System.out.println("size " + ls.size());

			if (ls.size() > 0) {
				user = (User) ls.get(0);
			}
			// System.out.println("New User List Array Length");
			// log.debug("log New User List Array Length" + userList.size());
		} catch (HibernateException e) {
			// System.out.println();
			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			// System.out.println("getUserByNTIdAndDomain Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return user;
	}

	public User getTRUserByEmplId(String emplId) {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		User user = null;
		List<Object> ls = new ArrayList<Object>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString.append("select ").append("user_id , ")
					.append("emplId , ").append("lname , ").append("fname , ")
					.append("email , ").append("nt_id , ")
					.append("nt_domain , ").append("status , ")
					.append("type  ").append("from user_access ")
					.append("where ").append("emplId = :emplId ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			q.setParameter("emplId", emplId);
			ls = q.list();
			// System.out.println("size " + ls.size());

			Iterator<Object> it = ls.iterator();

			while (it.hasNext()) {
				user = new User();
				Object[] field = (Object[]) it.next();

				user.setId(((BigDecimal) field[0]).intValue());
				user.setEmplId((String) field[1]);
				user.setLastName((String) field[2]);
				user.setFirstName((String) field[3]);
				user.setEmail((String) field[4]);
				user.setNtid((String) field[5]);
				user.setNtdomain((String) field[6]);
				user.setStatus((String) field[7]);
				user.setUserGroup((String) field[8]);

			}

			// log.debug("log New User List Array Length" + userList.size());
		} catch (HibernateException e) {
			// System.out.println();
			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			// System.out.println("getUserByNTIdAndDomain Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return user;

	}

	public User getTRUserByNTID(String ntId) {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		User user = null;
		List<Object> ls = new ArrayList<Object>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString.append("select ").append("user_id as id, ")
					.append("emplId as emplId, ").append("lname as lastName, ")
					.append("fname as firstName, ").append("email as email, ")
					.append("nt_id as ntid, ")
					.append("nt_domain as ntdomain, ")
					.append("status as status, ").append("type as userGroup ")
					.append("from user_access ").append("where ")
					.append("nt_id = :ntId ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			q.setParameter("ntId", ntId);
			ls = q.list();
			// System.out.println("size " + ls.size());

			Iterator<Object> it = ls.iterator();

			while (it.hasNext()) {
				user = new User();
				Object[] field = (Object[]) it.next();

				user.setId(((BigDecimal) field[0]).intValue());
				user.setEmplId((String) field[1]);
				user.setLastName((String) field[2]);
				user.setFirstName((String) field[3]);
				user.setEmail((String) field[4]);
				user.setNtid((String) field[5]);
				user.setNtdomain((String) field[6]);
				user.setStatus((String) field[7]);
				user.setUserGroup((String) field[8]);

			}

			// log.debug("log New User List Array Length" + userList.size());
		} catch (HibernateException e) {
			// System.out.println();
			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			// System.out.println("getUserByNTIdAndDomain Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return user;

	}

	public SCEDetail[] getSCEDetailsByTemplateVersionId(
			Integer templateVersionId) {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		List<SCEDetail> newTemp = new ArrayList<SCEDetail>();
		SCEDetail[] sceArray = null;

		try {
			// System.out.println("quer1" + queryString);
			queryString.append("select ")
					.append("q.question_id as questionId, ")
					.append("q.title as title, ")
					.append("q.display_order as displayOrder, ")
					.append("q.description as description, ")
					.append("q.critical as critical, ")
					.append("q.template_version_id as templateVersionId ")
					.append("from QUESTION q ").append("where  ")
					.append("q.template_version_id = :templateVersionId ")
					.append("order by ").append("display_order asc ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("templateVersionId", templateVersionId);
			List list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			SCEDetail sceDetail = null;
			while (it.hasNext()) {
				sceDetail = new SCEDetail();
				Object[] field = (Object[]) it.next();

				sceDetail
						.setQuestionId(field[0] != null ? ((BigDecimal) field[0])
								.intValue() : null);
				sceDetail.setTitle((String) field[1]);
				sceDetail
						.setDisplayOrder(field[2] != null ? ((BigDecimal) field[2])
								.intValue() : null);
				sceDetail.setDescription((String) field[3]);
				sceDetail.setCritical(field[4] != null ? ((Character) field[4])
						.toString() : null);
				sceDetail
						.setTemplateVersionId(field[5] != null ? ((BigDecimal) field[5])
								.intValue() : null);

				newTemp.add(sceDetail);
			}

			// System.out.println("New User List Array Length" +
			// newTemp.size());
			// User[] userarray = (User[]) newListUser.toArray();
			sceArray = newTemp.toArray(new SCEDetail[newTemp.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return sceArray;
	}

	public User getManagerDetail(String emplid) throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List<User> userList = new ArrayList<User>();

		// generic added
		List<User> ls = new ArrayList<User>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString
					.append("SELECT  ")
					.append("first_name as firstName,  ")
					.append("last_name as lastName,  ")
					.append("mv.email_address as email, ")
					.append("nt_id as ntid ")
					.append("FROM mv_field_employee_rbu mv ")
					.append("WHERE emplid IN ( ")
					.append("SELECT NVL2 (r1.email_address, r1.emplid, r2.emplid) ")
					.append("FROM mv_field_employee_rbu r, ")
					.append("mv_field_employee_rbu r1, ")
					.append("mv_field_employee_rbu r2 ")
					.append("WHERE r.reports_to_sales_position_id = r1.sales_position_id(+) ")
					.append("AND r.mgr2_sales_position_id = r2.sales_position_id(+) ")
					// .append("--AND (r1.emplid IS NOT NULL OR r2.emplid IS NOT NULL) ")
					.append("AND  R.EMPLID = :emplid) ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			q.setParameter("emplid", emplid);
			// q.setParameter("ntdomain", ntdomain.toUpperCase());

			ls = q.list();

			// System.out.println("size " + ls.size());

			// log.debug("log size " + ls.size());
			Iterator it = ls.iterator();

			User user = null;
			while (it.hasNext()) {
				user = new User();
				Object[] field = (Object[]) it.next();

				user.setFirstName((String) field[0]);
				user.setLastName((String) field[1]);
				user.setEmail((String) field[2]);
				user.setNtid((String) field[3]);
				userList.add(user);
			}

			// System.out.println("New User List Array Length" +
			// userList.size());

			// log.debug("log New User List Array Length" + userList.size());
			// User[] userarray = (User[]) newListUser.toArray();

			// // System.out.println("User Array Length" + userarray.length);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			// System.out.println();
			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			// System.out.println("getUserByNTIdAndDomain Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return (userList.size() > 0) ? userList.get(0) : null;

	}

	public EmailTemplate getEmailTemplate(Integer templateVersionId,
			String overallScore) throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List<EmailTemplate> emailList = new ArrayList<EmailTemplate>();

		// generic added
		List<Object> ls = new ArrayList<Object>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString
					.append("SELECT ")
					.append("ET.TEMPLATE_VERSION_ID as evaluationTemplateVersionId, ")
					.append("SS.SCORE_LEGEND as overallScore, ")
					.append("ET.EMAIL_CC as emailCc, ")
					.append("ET.EMAIL_BCC as emailBCc, ")
					.append("ET.EMAIL_SUBJECT as emailSubject, ")
					.append("ET.EMAIL_BODY AS emailBody, ")
					.append("ET.PUBLISH_FLAG as publishFlag ")
					.append("FROM EMAIL_TEMPLATE ET, ")
					.append("SCORING_SYSTEM SS ").append("WHERE  ")
					.append("ET.OVERALL_SCORE=SS.SCORE_VALUE  ")
					.append("AND ET.PUBLISH_FLAG = 'Y' ")
					.append("AND SS.SCORE_LEGEND = :overallScore ")
					.append("AND ET.TEMPLATE_VERSION_ID = :templateVersionId ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			q.setParameter("templateVersionId", templateVersionId);
			q.setParameter("overallScore", overallScore);
			// q.setParameter("ntdomain", ntdomain.toUpperCase());

			ls = q.list();

			// System.out.println("size " + ls.size());

			// log.debug("log size " + ls.size());
			Iterator it = ls.iterator();

			EmailTemplate emailTemplate = null;
			while (it.hasNext()) {
				emailTemplate = new EmailTemplate();
				Object[] field = (Object[]) it.next();

				emailTemplate
						.setEvaluationTemplateVersionId((field[0] != null ? ((BigDecimal) field[0])
								.intValue() : null));
				emailTemplate.setOverallScore((String) field[1]);
				emailTemplate.setEmailCc((String) field[2]);
				emailTemplate.setEmailBCc((String) field[3]);
				emailTemplate.setEmailSubject((String) field[4]);
				emailTemplate.setEmailBody((String) field[5]);
				emailTemplate
						.setPublishFlag(field[6] != null ? ((Character) field[6])
								.toString() : null);

				emailList.add(emailTemplate);
			}

			// System.out.println("New User List Array Length" +
			// emailList.size());

			// log.debug("log New User List Array Length" + userList.size());
			// User[] userarray = (User[]) newListUser.toArray();

			// // System.out.println("User Array Length" + userarray.length);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			// System.out.println();
			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			// System.out.println("getUserByNTIdAndDomain Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return (emailList.size() > 0) ? emailList.get(0) : null;

	}

	public SCE getLatestSCE(String emplId, Integer eventId, String productCode,
			String status) {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List<SCE> newTemp = new ArrayList<SCE>();
		try {
			// System.out.println("quer1" + queryString);
			queryString
					.append("SELECT ")
					.append("s.sce_id as id, ")
					.append("s.event_id as eventId, ")
					.append("s.emplId AS emplId, ")
					.append("s.product_cd AS productCode, ")
					.append("s.product_name AS product, ")
					.append("s.course_description AS course, ")
					.append("s.classroom AS classroom, ")
					.append("s.table_name AS tableName,      ")
					.append("s.template_version_id AS templateVersionId, ")
					.append("s.precall_rating AS precallRating, ")
					.append("s.precall_comments AS precallComments, ")
					.append("s.postcall_rating AS postcallRating, ")
					.append("s.postcall_comments AS postcallComments, ")
					.append("s.comments AS comments, ")
					.append("s.hlc_compliant AS hlcCompliant, ")
					.append("s.reviewed AS reviewed, ")
					.append("s.overall_rating AS overallRating, ")
					.append("s.overall_comments AS overallComments, ")
					.append("s.submitted_date AS submittedDate, ")
					.append("s.submitted_by_emplId AS submittedByEmplId, ")
					.append("s.status AS status, ")
					.append("s.prepared_by_emplId AS preparedByEmplId, ")
					.append("s.overall_score AS overallScore, ")
					.append("tv.form_title AS formTitle ")
					.append("FROM ")
					.append("sce_fft s, ")
					.append("template_version tv    ")
					.append("where ")
					.append("s.template_version_id = tv.template_version_id and      ")
					.append("s.emplId = :emplId and ")
					.append("s.event_id = :eventId and ")
					.append("s.product_cd = :productCode and ")
					.append("status = :status and ").append("s.sce_id =  ")
					.append("( ").append("select max(sce_id) from  ")
					.append("sce_fft s where ").append("emplId = :emplId and ")
					.append("event_id = :eventId and ")
					.append("product_cd = :productCode and ")
					.append("status = :status ").append(") ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);

			q.setParameter("emplId", emplId);
			q.setParameter("eventId", eventId);
			q.setParameter("productCode", productCode);
			q.setParameter("status", status);

			List list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			SCE sce = null;
			while (it.hasNext()) {
				sce = new SCE();
				Object[] field = (Object[]) it.next();

				sce.setId(field[0] != null ? ((BigDecimal) field[0]).intValue()
						: null);
				sce.setEventId(field[1] != null ? ((BigDecimal) field[1])
						.intValue() : null);
				sce.setEmplId((String) field[2]);
				sce.setProductCode((String) field[3]);
				sce.setProduct((String) field[4]);
				sce.setCourse((String) field[5]);
				sce.setClassroom((String) field[6]);
				sce.setTableName((String) field[7]);

				sce.setTemplateVersionId(field[8] != null ? ((BigDecimal) field[8])
						.intValue() : null);

				sce.setPrecallRating((String) field[9]);
				sce.setPrecallComments((String) field[10]);

				sce.setPostcallRating((String) field[11]);
				sce.setPostcallComments((String) field[12]);

				sce.setComments((String) field[13]);

				sce.setHlcCompliant(field[14] != null ? ((Character) field[14])
						.toString() : null);
				sce.setReviewed(field[15] != null ? ((Character) field[15])
						.toString() : null);
				sce.setOverallRating((String) field[16]);
				sce.setOverallComments((String) field[17]);

				sce.setSubmittedDate((Date) field[18]);
				sce.setSubmittedByEmplId((String) field[19]);

				sce.setStatus((String) field[20]);
				;
				sce.setPreparedByEmplId((String) field[21]);
				sce.setOverallScore(((Double) field[22]));
				sce.setFormTitle((String) field[23]);

				newTemp.add(sce);

			}

			// System.out.println("New User List Array Length" +
			// newTemp.size());

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return (newTemp.size() > 0) ? newTemp.get(0) : null;

	}

	public Integer getNextSCEId() {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		Integer seq = null;
		try {

			// System.out.println("quer1" + queryString);
			queryString.append("select sq_sce.nextval from dual ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);

			List list = q.list();
			// user = list;
			// System.out.println("size " + list.size());

			seq = ((BigDecimal) list.get(0)).intValue();

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return seq;
	}

	public void insertSCEComment(String sceid, String activity_id,
			String phaseNo, String rep_id, String comment1, String comment2,
			String comment3, String enteredby1, String enteredby2,
			String enteredby3, String date1, String date2, String date3,
			String submitted_by, String submitted_date) {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		try {
			// // System.out.println("quer1" + queryString);

			// String q1 = queryString.toString();
			// // System.out.println("quer2" + q1);
			queryString.append("insert into sce_comments ").append("( ")
					.append("sce_id, ").append("activity_id, ")
					.append("phase_no, ").append("representative_id, ")
					.append("comment_1, ").append("comment_2, ")
					.append("comment_3, ").append("entered_by_1, ")
					.append("entered_by_2, ").append("entered_by_3, ")
					.append("date_1, ").append("date_2, ").append("date_3, ")
					.append("submitted_by, ").append("submitted_date) ")
					.append("values ").append("( ").append(":sceid, ")
					.append(":activity_id, ").append(":phaseNo, ")
					.append(":rep_id, ").append(":comment1, ")
					.append(":comment2, ").append(":comment3, ")
					.append(":enteredby1, ").append(":enteredby2, ")
					.append(":enteredby3, ").append(":date1, ")
					.append(":date2, ").append(":date3, ")
					.append(":submitted_by, ").append(":submitted_date ")
					.append(") ");

			String nativeHql = queryString.toString();

			Transaction ts = session.beginTransaction();

			Query query = session.createSQLQuery(nativeHql);

			query.setParameter("sceid", sceid);
			query.setParameter("activity_id", activity_id);
			query.setParameter("phaseNo", phaseNo);
			query.setParameter("rep_id", rep_id);

			query.setParameter("comment1", comment1);
			query.setParameter("comment2", comment2);
			query.setParameter("comment3", comment3);

			query.setParameter("enteredby1", enteredby1);
			query.setParameter("enteredby2", enteredby2);
			query.setParameter("enteredby3", enteredby3);

			query.setParameter("date1", date1);
			query.setParameter("date2", date2);
			query.setParameter("date3", date3);

			query.setParameter("submitted_by", submitted_by);
			query.setParameter("submitted_date", submitted_date);

			int i = query.executeUpdate();

			// System.out.println(" execute result set " + i);
			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("createUser Hibernatate Exception");

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void insertSCE(SCE objSCE) {

		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();
		List<Attendee> newTemp = new ArrayList<Attendee>();

		try {

			objSCE.setSubmittedDate(new Date());
			if (objSCE.getPreparedByEmplId() != null) {
				objSCE.setPreparedByEmplId(objSCE.getPreparedByEmplId());
			} else {
				objSCE.setPreparedByEmplId(objSCE.getSubmittedByEmplId());
			}

			// System.out.println("quer1" + queryString);

			String q1 = queryString.toString();
			// System.out.println("quer2" + q1);

			Transaction ts = session.beginTransaction();
			session.persist(objSCE);
			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("acceptLegalConsent Hibernatate Exception");

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void insertSCEDetail(SCEDetail objSCEDetail) {

		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();
		List<Attendee> newTemp = new ArrayList<Attendee>();

		try {

			// System.out.println("quer1" + queryString);

			String q1 = queryString.toString();
			// System.out.println("quer2" + q1);

			Transaction ts = session.beginTransaction();
			session.persist(objSCEDetail);
			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("acceptLegalConsent Hibernatate Exception");

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void insertLegalDetail(Integer sceId, Integer id,
			String legalQuestionValue) throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		try {
			// // System.out.println("quer1" + queryString);

			// String q1 = queryString.toString();
			// // System.out.println("quer2" + q1);
			queryString.append("INSERT INTO  ")
					.append("LEGAL_QUESTION_DETAIL  ").append("( ")
					.append("LEGAL_DETAIL_ID, ").append("SCE_ID, ")
					.append("LEGAL_QUESTION_ID, ")
					.append("LEGAL_QUESTION_VALUE ").append(")  ")
					.append("VALUES  ").append("( ")
					.append("SEQ_LEGAL_QUESTION_DETAIL.nextval, ")
					.append(":sceId, ").append(":id, ")
					.append(":legalQuestionValue ").append(") ");

			String nativeHql = queryString.toString();
			Transaction ts = session.beginTransaction();
			Query query = session.createSQLQuery(nativeHql);

			query.setParameter("sceId", sceId);
			query.setParameter("id", id);
			query.setParameter("legalQuestionValue", legalQuestionValue);

			int i = query.executeUpdate();

			// System.out.println(" execute result set " + i);
			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("createUser Hibernatate Exception");

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void updateSCE(SCE objSCE) {

		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		Transaction ts = null;
		// System.out.println("test 2");
		// User user = null;

		try {
			queryString
					.append("update sce_fft set  ")
					.append("precall_rating = :precallRating, ")
					.append("precall_comments = :precallComments, ")
					.append("postcall_rating = :postcallRating, ")
					.append("postcall_comments = :postcallComments, ")
					.append("comments = :comments, ")
					.append("hlc_compliant = :hlcCompliant, ")
					.append("reviewed = :reviewed}, ")
					.append("overall_rating = :overallRating}, ")
					.append("overall_comments = :overallComments}, ")
					.append("submitted_date = sysdate, ")
					.append("submitted_by_emplId = :submittedByEmplId}, ")
					.append("status = :status}, ")
					.append("prepared_by_emplId = (case when :status = 'SUBMITTED' then prepared_by_emplId else :preparedByEmplId end), ")
					.append("overall_score = :overallScore ").append("where  ")
					.append("sce_id = :id ");

			String query = queryString.toString();

			ts = session.beginTransaction();
			Query q = session.createSQLQuery(query);

			q.setParameter("precallRating", objSCE.getPrecallRating());
			q.setParameter("precallComments", objSCE.getPrecallComments());
			q.setParameter("postcallRating", objSCE.getPostcallRating());
			q.setParameter("postcallComments", objSCE.getPostcallComments());
			q.setParameter("comments", objSCE.getComments());
			q.setParameter("hlcCompliant", objSCE.getHlcCompliant());
			q.setParameter("overallComments", objSCE.getOverallComments());
			q.setParameter("overallRating", objSCE.getOverallRating());
			q.setParameter("overall_comments", objSCE.getOverallComments());
			q.setParameter("submittedByEmplId", objSCE.getSubmittedByEmplId());
			q.setParameter("status", objSCE.getStatus());
			q.setParameter("preparedByEmplId", objSCE.getPreparedByEmplId());
			q.setParameter("id", objSCE.getId());

			int i = q.executeUpdate();
			// System.out.println("Execute Update :" + i);
			ts.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void updateSCEDetail(SCEDetail objSCEDetail) {

		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		Transaction ts = null;
		// System.out.println("test 2");
		// User user = null;

		try {
			queryString.append("update sce_detail set  ")
					.append("question_rating   = :questionRating,  ")
					.append("question_comments = :questionComments, ")
					.append("question_fg       = :questionFg, ")
					.append("question_score1   = :questionScore1, ")
					.append("question_score2   = :questionScore2, ")
					.append("question_score3   = :questionScore3 ")
					.append("where  ")
					.append("sce_id = :sceId and question_id = :questionId ");

			String query = queryString.toString();

			ts = session.beginTransaction();
			Query q = session.createSQLQuery(query);

			q.setParameter("questionRating", objSCEDetail.getQuestionRating());

			q.setParameter("questionComments",
					objSCEDetail.getQuestionComments());
			q.setParameter("questionFg", objSCEDetail.getQuestionFg());

			q.setParameter("questionScore1", objSCEDetail.getQuestionScore1());
			q.setParameter("questionScore2", objSCEDetail.getQuestionScore2());
			q.setParameter("questionScore3", objSCEDetail.getQuestionScore3());

			q.setParameter("sceId", objSCEDetail.getSceId());
			q.setParameter("questionId", objSCEDetail.getQuestionId());

			int i = q.executeUpdate();
			// System.out.println("Execute Update :" + i);
			ts.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void updateLegalDetail(Integer sceId, Integer id,
			String legalQuestionValue) throws SQLException {
		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		Transaction ts = null;
		// System.out.println("test 2");
		// User user = null;

		try {
			queryString.append("update legal_question_detail set  ")
					.append("legal_question_value   = :legalQuestionValue ")
					.append("where  ")
					.append("sce_id = :sceId and question_id = :id ");

			String query = queryString.toString();

			ts = session.beginTransaction();
			Query q = session.createSQLQuery(query);

			q.setParameter("legalQuestionValue", legalQuestionValue);

			q.setParameter("sceId", sceId);
			q.setParameter("id", id);
			int i = q.executeUpdate();
			// System.out.println("Execute Update :" + i);
			ts.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public EvaluationFormScore getEvaluationFormValues(
			Integer evaluationTemplateId, String overallScore)
			throws SQLException {

		// System.out.println("test 1");
		Session session = HibernateUtils.getHibernateSession();
		// System.out.println("test 2");

		EvaluationFormScore evaluationFormScore = null;

		StringBuilder queryString = new StringBuilder();
		// String queryString = null;
		List<EvaluationFormScore> newTemp = new ArrayList<EvaluationFormScore>();
		try {

			// System.out.println("quer1" + queryString);

			queryString
					.append("SELECT  ")
					.append("FS.ID as id, ")
					.append("FS.TEMPLATE_VERSION_ID AS templateVersionId, ")
					.append("SS.SCORE_LEGEND AS scoreValue, ")
					.append("FS.NOTIFICATION_FLAG AS notificationFG, ")
					.append("FS.LMS_FLAG AS lmsFlag ")
					.append("from  ")
					.append("EVALUATION_FORM_SCORE FS, ")
					.append("SCORING_SYSTEM SS ")
					.append("WHERE ")
					.append("FS.SCORE_VALUE=SS.SCORE_VALUE(+) ")
					.append("AND FS.TEMPLATE_VERSION_ID= :evaluationTemplateId ")
					.append("AND SS.SCORE_LEGEND= :overallScore ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);// .addEntity(EvaluationFormScore.class);
			q.setParameter("evaluationTemplateId", evaluationTemplateId);
			q.setParameter("overallScore", overallScore);
			List list = q.list();
			/*
			 * 
			 * if (ls != null && ls.size() > 0) {
			 * 
			 * evaluationFormScore = (EvaluationFormScore) ls.get(0); }
			 */

			// System.out.println("size " + list.size());
			Iterator<Object> it = list.iterator();

			while (it.hasNext()) {
				evaluationFormScore = new EvaluationFormScore();
				Object[] field = (Object[]) it.next();

				evaluationFormScore
						.setId(field[0] != null ? ((BigDecimal) field[0])
								.intValue() : null);

				evaluationFormScore
						.setTemplateVersionId(field[1] != null ? ((BigDecimal) field[1])
								.intValue() : null);

				evaluationFormScore.setScoreLegend((String) field[2]);

				evaluationFormScore
						.setNotificationFG(field[3] != null ? ((Character) field[3])
								.toString() : null);
				evaluationFormScore
						.setLmsFlag(field[4] != null ? ((Character) field[4])
								.toString() : null);

				newTemp.add(evaluationFormScore);
			}

			// System.out.println("New User List Array Length" +
			// newTemp.size());

			// evalFormArray = newTemp.toArray(new
			// EvaluationFormScore[newTemp.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("UserLegalConsent Hibernatate Exception");
		}
		/* This Exception is written for testing purpose */
		catch (Exception e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("UserLegalConsent  Exception");
		}

		finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return (newTemp.size() > 0) ? newTemp.get(0) : null;

	}

	public Blob getBlankForm(Integer templateVersionId) throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List<UploadBlankForm> blankForm = new ArrayList<UploadBlankForm>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString
					.append("select template_version_id, ")
					.append("UPLOADED_BLANK_FILE_ID, ")
					.append("uploaded_blank_file ")
					.append("from uploaded_blank_form  ")
					.append("where uploaded_blank_file_id= ")
					.append("(select max(uploaded_blank_file_id) from  ")
					.append("uploaded_blank_form  ")
					.append("where template_version_id=   :templateVersionId  ")
					.append("group by template_version_id ").append(") ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1).addEntity(
					UploadBlankForm.class);
			q.setParameter("templateVersionId", templateVersionId);

			blankForm = q.list();

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();

			// log.error("HibernateException in getSalesPositionTypeCd", e);
			// System.out.println("HibernateException in getSalesPositionTypeCd");
		}

		finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return (blankForm.size() > 0) ? blankForm.get(0).getBlobData() : null;

	}

	public Integer getTemplateVersionId(Integer templateId) throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List templateVersion = null;
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString.append("select template_version_id  ").append("from  ")
					.append("template_version where  ")
					.append("template_id = :templateId  ")
					.append("and version =  ")
					.append("(select max(version) from ")
					.append("template_version where  ")
					.append("template_id = :templateId ")
					.append("and publish_flag='Y') ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			q.setParameter("templateId", templateId);

			templateVersion = q.list();

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// log.error("HibernateException in getSceVisibility", e);
			// System.out.println("HibernateException in getSceVisibility");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return (templateVersion.size() > 0) ? ((BigDecimal) templateVersion
				.get(0)).intValue() : null;

	}

	public ScoringSystem[] getAllScores(Integer templateVersionId)
			throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		List<ScoringSystem> newTemp = new ArrayList<ScoringSystem>();
		ScoringSystem[] scoringSystem = null;

		try {
			// System.out.println("quer1" + queryString);
			queryString
					.append("select  ")
					.append("score_legend as scoreLegend, score_value as scoreValue  ")
					.append("from scoring_system where scoring_system_identifier in ")
					.append("(select scoring_system_identifier from evaluation_form_score ")
					.append("where template_version_id=:templateVersionId) ")
					.append("order by score_legend asc ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("templateVersionId", templateVersionId);
			List list = q.list();

			// user = list;
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			ScoringSystem scoringSystemObj = null;
			while (it.hasNext()) {
				scoringSystemObj = new ScoringSystem();
				Object[] field = (Object[]) it.next();

				scoringSystemObj.setScoreLegend((String) field[0]);
				scoringSystemObj.setScoreValue((String) field[1]);

				newTemp.add(scoringSystemObj);
			}

			// System.out.println("New User List Array Length" +
			// newTemp.size());
			// User[] userarray = (User[]) newListUser.toArray();
			scoringSystem = newTemp.toArray(new ScoringSystem[newTemp.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return scoringSystem;

	}

	public Integer fetchVersionNumber(Integer templateVersionId)
			throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List templateVersion = null;
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString
					.append("select version from sales_call.template_version where template_version_id= :templateVersionId ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			q.setParameter("templateVersionId", templateVersionId);

			templateVersion = q.list();

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// log.error("HibernateException in getSceVisibility", e);
			// System.out.println("HibernateException in getSceVisibility");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return (templateVersion.size() > 0) ? ((BigDecimal) templateVersion
				.get(0)).intValue() : null;
	}

	public String getProductName(String productCode) throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List templateVersion = null;
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString
					.append("select activityname from mv_usp_activity_hierarchy where activity_pk = :productCode ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			q.setParameter("productCode", productCode);

			templateVersion = q.list();

		} catch (HibernateException e) {
			e.printStackTrace();
			// log.error("HibernateException in getSceVisibility", e);
			// System.out.println("HibernateException in getSceVisibility");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return (templateVersion.size() > 0) ? ((String) templateVersion.get(0))
				: null;
	}

	public void uploadForm(EvaluationForm evalForm, Integer sceId)
			throws SQLException {

		Session session = HibernateUtils.getHibernateSession();

		try {

			StringBuilder queryString = new StringBuilder();

			queryString.append("INSERT INTO SCE_FFT  ").append("( ")
					.append("SCE_ID,             ").append("OVERALL_RATING, ")
					.append("UPLOADED_BY , ").append("UPLOADED_FORM, ")
					.append("SUBMITTED_DATE, ").append("UPLOAD_DATE, ")
					.append("PRODUCT_NAME, ").append("EMPLID, ")
					.append("EVENT_ID, ").append("TEMPLATE_VERSION_ID, ")
					.append("PRODUCT_CD, ").append("STATUS ").append(") ")
					.append("VALUES ").append("( ").append("sq_sce.nextval, ")
					.append(":score, ").append(":emplid, ")
					.append(":content, ").append(":evaluationDate, ")
					.append(":uploadDate, ").append(":product, ")
					.append(":repEmplid, ").append(":eventId, ")
					.append(":templateVersionId, ").append(":productCode, ")
					.append(":status ").append(")     ");

			String nativeHql = queryString.toString();
			Transaction ts = session.beginTransaction();
			Query query = session.createSQLQuery(nativeHql);

			query.setParameter("score", evalForm.getScore());
			query.setParameter("emplid", evalForm.getEmplid());
			query.setParameter("content", evalForm.getContent());
			query.setParameter("evaluationDate", evalForm.getEvaluationDate());
			query.setParameter("uploadDate", evalForm.getUploadDate());
			query.setParameter("product", evalForm.getProduct());

			query.setParameter("repEmplid", evalForm.getRepEmplid());
			query.setParameter("eventId", evalForm.getEventId());
			query.setParameter("templateVersionId",
					evalForm.getTemplateVersionId());

			query.setParameter("productCode", evalForm.getProductCode());
			query.setParameter("status", evalForm.getStatus());

			int i = query.executeUpdate();

			// System.out.println(" execute result set " + i);
			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("createUser Hibernatate Exception");

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	/*
	 * public static void main(String args[]) {
	 * 
	 * SCEControlImpl sceControlImpl = new SCEControlImpl(); try {
	 * 
	 * // // System.out.println("Us "+ es.length());
	 * 
	 * 
	 * if(es != null){ // System.out.println("User name " + es.getId()); //
	 * System.out.println("User name " + es.getTemplateVersionId());}
	 * 
	 * 
	 * 
	 * // System.out.println("User name " + user.getLastName()); //
	 * System.out.println("User name " + user.getFirstName()); //
	 * System.out.println("User name " + user.getEmail());
	 * 
	 * 
	 * EventsCreated eventsCreated = new EventsCreated();
	 * eventsCreated.setEventId(1);
	 * eventsCreated.setEventStartDate("09/04/2014");
	 * eventsCreated.setEventEndDate("10/04/2014");
	 * eventsCreated.setEventName("xyz");
	 * 
	 * 
	 * User user = sceControlImpl.getUserById(13);
	 * 
	 * // System.out.println("User is " + user.getExpirationDate());
	 * 
	 * SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
	 * 
	 * user.setExpirationDate(sdf.format(user.getExpirationDate()));
	 * 
	 * // System.out.println("User is "+ SCEUtils.stringToDate(new
	 * SimpleDateFormat("MM/dd/yyyy"), user.getExpirationDate()));
	 * 
	 * Date d = new
	 * SimpleDateFormat("dd/MM/yyyy").parse(user.getExpirationDate());
	 * 
	 * // System.out.println(" Date is " + user.getExpirationDate());
	 * 
	 * 
	 * sceControlImpl.updateEvent(eventsCreated);
	 * 
	 * 
	 * for(SCEDetail user :sceDetail ){ // System.out.println("User name " +
	 * user.getQuestionId() ); // System.out.println("User name " +
	 * user.getCritical()); // System.out.println("User name " +
	 * user.getTemplateVersionId() ); }
	 * 
	 * 
	 * // System.out.println("successfully done");
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * }
	 */
	/* GADALP ENDS HERE */

	public Learner getLearnerByEmailCourse(String email, String course)
			throws SQLException

	{
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		Learner learner = null;
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString
					.append("(SELECT emp_fk AS empno, emp_fname AS firstname, emp_lname AS lastname,");
			queryString
					.append("   email_address AS emailaddress, emp_state AS LOCATION,");
			queryString
					.append("   registration_date AS rdate, activityname AS course,");
			queryString
					.append("   activity_code AS coursecode, activity_start_date AS coursestartdate,");
			queryString.append("   activity_end_date AS courseenddate ");
			queryString.append("   FROM p2l_reg_learners");
			queryString
					.append("   WHERE activityname LIKE :course AND email_address LIKE :email)");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			q.setParameter("course", course);
			q.setParameter("email", email);

			List<Learner> newListLeaner = new ArrayList<Learner>();
			List<Object> list = q.list();

			// System.out.println("size " + list.size());
			Iterator<Object> it = list.iterator();

			while (it.hasNext()) {
				Learner l = new Learner();
				Object[] field = (Object[]) it.next();

				String empNo = field[0].toString();

				l.setEmpNo(empNo);
				l.setFirstName((String) field[1]);
				l.setLastName((String) field[2]);
				l.setEmailAddress((String) field[3]);
				l.setLocation((String) field[4]);
				l.setRdate((Date) field[5]);
				l.setCourse((String) field[6]);
				l.setCourseCode((String) field[7]);
				l.setCourseStartDate((Date) field[8]);
				l.setCourseEndDate((Date) field[9]);
				newListLeaner.add(l);
			}
			learner = (Learner) newListLeaner.get(0);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// log.error("HibernateException in getSceVisibility", e);
			// System.out.println("HibernateException in getSceVisibility");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return learner;

	}

	public void addNewLearner(Learner learner) {
		Session session = HibernateUtils.getHibernateSession();

		try {
			// // System.out.println("quer1" + queryString);

			// String q1 = queryString.toString();
			// // System.out.println("quer2" + q1);

			String nativeHql = "insert into learners_coursecompleted (L_id, first_name, last_name, email_address, location, course, iscoursecomplete, email_issent,registration_date,empno, course_code, course_start_date, course_end_date, EVENT_NAME )"
					+ "values( "
					+ " SQ_LEARNER_COURSECOMPLETED.NEXTVAL,:firstName,:lastName,:emailAddress,:location,:course,'Y','N',:rdate,:empNo,:courseCode,:courseStartDate,:courseEndDate,:eventName  "
					+ ")";

			Transaction ts = session.beginTransaction();

			Query query = session.createSQLQuery(nativeHql);

			query.setParameter("firstName", learner.getFirstName());
			query.setParameter("lastName", learner.getLastName());
			query.setParameter("emailAddress", learner.getEmailAddress());
			query.setParameter("location", learner.getLocation());
			query.setParameter("course", learner.getCourse());
			query.setParameter("rdate", learner.getRdate());
			query.setParameter("empNo", learner.getEmpNo());
			query.setParameter("courseCode", learner.getCourseCode());
			query.setParameter("courseStartDate", learner.getCourseStartDate());
			query.setParameter("courseEndDate", learner.getCourseEndDate());
			query.setParameter("eventName", learner.getEventName());

			int i = query.executeUpdate();

			// System.out.println(" execute result set " + i);
			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("createUser Hibernatate Exception");

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	public String[] getCoursesForEvent(String event) throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		String[] result = null;
		List list = new ArrayList();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString
					.append(" select COURSE_CODE from EVENT_PRODUCT_COURSE_MAPPING where EVENT_NAME like  ");
			queryString.append("'" + event + "'");
			queryString.append(" and ACTIVE_FLAG ='a'");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			list = q.list();
			// System.out.println("******list size***" + list.size());
			result = (String[]) list.toArray(new String[list.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// log.error("HibernateException in getSceVisibility", e);
			// System.out.println("HibernateException in getSceVisibility");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return result;

	}

	public Learner[] getLearnersByCourseAndDate(String course, String frmDate,
			String toDate) throws SQLException

	{
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		Learner[] result = null;
		try {

			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			/* edited on 2/2/2016 */

			/*
			 * queryString .append(
			 * "(SELECT empno AS empno, first_name AS firstname, last_name AS lastname,"
			 * ); queryString
			 * .append(" email_address AS emailaddress, LOCATION AS LOCATION,");
			 * queryString
			 * .append(" registration_date AS rdate, course AS course,");
			 * queryString .append(
			 * " course_code AS coursecode, iscoursecomplete AS iscoursecomplete,"
			 * ); queryString.append(" course_start_date AS coursestartdate,");
			 * queryString.append(" course_end_date AS courseenddate");
			 * queryString.append(" FROM learners_coursecompleted");
			 * queryString.append(" WHERE course LIKE :course"); queryString
			 * .append(
			 * "  AND TRUNC (course_start_date) >= TO_DATE (:frmDate, 'mm/dd/yyyy')"
			 * ); queryString .append(
			 * "   AND TRUNC (course_end_date) <= TO_DATE (:toDate, 'mm/dd/yyyy'))"
			 * ); queryString.append(" UNION"); queryString .append(
			 * "( SELECT emp_fk AS empno, emp_fname AS firstname, emp_lname AS lastname,"
			 * ); queryString
			 * .append(" email_address AS emailaddress, emp_state AS LOCATION,"
			 * ); queryString
			 * .append(" registration_date AS rdate, activityname AS course,");
			 * queryString.append(" activity_code AS coursecode, 'N',");
			 * queryString.append(" activity_start_date AS coursestartdate,");
			 * queryString.append(" activity_end_date AS courseenddate");
			 * queryString.append(" FROM p2l_reg_learners"); queryString
			 * .append(
			 * " WHERE TRUNC (activity_start_date) >= TO_DATE (:frmDate, 'mm/dd/yyyy')"
			 * ); queryString .append(
			 * " AND TRUNC (activity_end_date) <= TO_DATE (:toDate, 'mm/dd/yyyy')"
			 * ); queryString.append(" AND activityname LIKE :course");
			 * queryString
			 * .append(" AND email_address NOT IN (SELECT email_address");
			 * queryString.append(" FROM learners_coursecompleted");
			 * queryString.append(" WHERE course LIKE :course))");
			 */

			/* edited by manish on 2/2/2016 to stop showing duplicate rows */
			/*---------------------------------------------------------	*/
			queryString
					.append("(SELECT empno AS empno, first_name AS firstname, last_name AS lastname,");
			queryString
					.append(" email_address AS emailaddress, LOCATION AS LOCATION,");
			queryString
					.append(" registration_date AS rdate, course AS course,");
			queryString
					.append(" course_code AS coursecode, iscoursecomplete AS iscoursecomplete,");
			queryString.append(" course_start_date AS coursestartdate,");
			queryString.append(" course_end_date AS courseenddate,null as RN");
			queryString.append(" FROM learners_coursecompleted");
			queryString.append(" WHERE course LIKE :course");
			queryString
					.append("  AND TRUNC (course_start_date) >= TO_DATE (:frmDate, 'mm/dd/yyyy')");
			queryString
					.append("   AND TRUNC (course_end_date) <= TO_DATE (:toDate, 'mm/dd/yyyy'))");
			queryString.append(" UNION");
			queryString.append(" (SELECT * FROM ");
			queryString
					.append("( SELECT emp_fk AS empno, emp_fname AS firstname, emp_lname AS lastname,");
			queryString
					.append(" email_address AS emailaddress, emp_state AS LOCATION,");
			queryString
					.append(" registration_date AS rdate, activityname AS course,");
			queryString.append(" activity_code AS coursecode, 'N',");
			queryString.append(" activity_start_date AS coursestartdate,");
			queryString.append(" activity_end_date AS courseenddate,");
			queryString
					.append(" ROW_NUMBER () OVER (PARTITION BY email_address ORDER BY EMP_FNAME DESC) rn");
			queryString.append(" FROM p2l_reg_learners");
			queryString
					.append(" WHERE TRUNC (activity_start_date) >= TO_DATE (:frmDate, 'mm/dd/yyyy')");
			queryString
					.append(" AND TRUNC (activity_end_date) <= TO_DATE (:toDate, 'mm/dd/yyyy')");
			queryString
					.append(" AND email_address NOT IN (SELECT email_address");
			queryString.append(" FROM learners_coursecompleted");
			queryString.append(" WHERE course LIKE :course)");
			queryString.append(" AND activityname LIKE :course)a");
			queryString.append(" WHERE rn = 1)");

			/* ---------------------------------------------------------- */

			String q1 = queryString.toString();

			System.out.println("Query Running:" + q1);

			Query q = session.createSQLQuery(q1);
			q.setParameter("course", course);
			q.setParameter("frmDate", frmDate);
			q.setParameter("toDate", toDate);

			List<Learner> newListLeaner = new ArrayList<Learner>();
			List<Object> list = q.list();

			// System.out.println("size " + list.size());
			Iterator<Object> it = list.iterator();

			while (it.hasNext()) {
				Learner l = new Learner();
				Object[] field = (Object[]) it.next();

				String empNo = field[0].toString();
				String completeStat = field[8].toString();

				l.setEmpNo(empNo);
				l.setFirstName((String) field[1]);
				l.setLastName((String) field[2]);
				l.setEmailAddress((String) field[3]);
				l.setLocation((String) field[4]);
				l.setRdate((Date) field[5]);
				l.setCourse((String) field[6]);
				l.setCourseCode((String) field[7]);
				l.setIsCourseComplete(completeStat);
				l.setCourseStartDate((Date) field[9]);
				l.setCourseEndDate((Date) field[10]);

				newListLeaner.add(l);
			}

			result = newListLeaner.toArray(new Learner[newListLeaner.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// log.error("HibernateException in getSceVisibility", e);
			// System.out.println("HibernateException in getSceVisibility");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return result;

	}

	// end monika
	/* CHANGES KHATED START */
	public EventsCreated[] getAllActiveInprogressEvents() {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		EventsCreated[] eventsCreatedArray = null;
		List<EventsCreated> eventCreatedList = new ArrayList<EventsCreated>();
		try {
			queryString
					.append("select event_id as eventId, event_name as eventName,event_description as eventDescription,to_char(event_start_date,'mm/dd/yyyy') as eventStartDate,to_char(event_end_date,'mm/dd/yyyy')  as eventEndDate,event_status as eventStatus,eval_duration as evalDuration ,number_of_eval as numberOfEval,type_of_eval as typeOfEval,number_of_learners as numberOfLearners from V_Event where EVENT_STATUS IN ( 'ACTIVE','INPROGRESS')");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			eventCreatedList = q.list();
			List<EventsCreated> newEventCreatedList = new ArrayList<EventsCreated>();
			Iterator it = eventCreatedList.iterator();
			EventsCreated event = null;
			while (it.hasNext()) {
				event = new EventsCreated();
				Object[] field = (Object[]) it.next();
				// event.setEventId((Integer)field[0]);
				event.setEventName((String) field[1]);
				event.setEventDescription((String) field[2]);
				event.setEventStartDate((String) field[3]);
				event.setEventEndDate((String) field[4]);
				event.setEventStatus((String) field[5]);
				if (field[6] != null) {
					event.setEvalDuration(Integer.parseInt((String) field[6]));
				}
				if (field[7] != null) {
					event.setNumberOfEval(((BigDecimal) field[7]).intValue());
				}
				/* event.setNumberOfEval((Integer)field[7]); */
				event.setTypeOfEval((String) field[8]);
				if (field[9] != null) {
					event.setNumberOfLearners(((BigDecimal) field[9])
							.intValue());
				}
				/* event.setNumberOfLearners((Integer)field[9]); */
				newEventCreatedList.add(event);

			}
			eventsCreatedArray = newEventCreatedList
					.toArray(new EventsCreated[newEventCreatedList.size()]);
			// System.out.println("eventsCreatedArray---->" +
			// eventsCreatedArray);
		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// log.error("HibernateException in getSceVisibility", e);
			// System.out.println("HibernateException in getSceVisibility");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return eventsCreatedArray;
	}

	public void addWebExDetails(WebExDetails webExDetails) throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		try {
			String insertWebExQry = "insert into webexdetails(event_name,conference_call_number,chairperson_passcode,participant_passcode,meeting_link)values(:eventName,:confCallNumber,:chairPersonPasscode,:participantPasscode,:meetingLink)";
			Transaction ts = session.beginTransaction();
			Query query = session.createSQLQuery(insertWebExQry);

			query.setParameter("eventName", webExDetails.getEventName());
			query.setParameter("confCallNumber",
					webExDetails.getConfCallNumber());
			query.setParameter("chairPersonPasscode",
					webExDetails.getChairPersonPasscode());
			query.setParameter("participantPasscode",
					webExDetails.getParticipantPasscode());
			query.setParameter("meetingLink", webExDetails.getMeetingLink());

			int i = query.executeUpdate();

			// System.out.println(" execute result set " + i);
			ts.commit();
		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("Add web-ex details Hibernatate Exception");

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	/* CHANGES KHATED END */

	// megha start

	public void updateLastEventDate(String product, String trainerEmail,
			String updateDate) throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();

		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("update Guest_trainer set REP_LAST_EVENT_DATE=to_date(:updateDate,'yyyy/mm/dd') where rep_email like :trainerEmail and rep_associated_product like :product ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);

			ts = session.beginTransaction();
			Query q = session.createSQLQuery(q1);
			q.setParameter("product", product);
			q.setParameter("trainerEmail", trainerEmail);
			q.setParameter("updateDate", updateDate);

			int result = q.executeUpdate();

			// System.out.println("Result : " + result);
			ts.commit();
		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("acceptLegalConsent Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
	}

	public String getDateToUpdate(String eventName, String product,
			String trainerEmail) throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		String strings = null;
		String string = new String();
		List list = null;
		List<String> strList = new ArrayList<String>();
		try {
			queryString
					.append("select * from (select REP_LAST_EVENT_DATE from guest_trainer where rep_email like :trainerEmail and rep_associated_product like :product ")
					.append("UNION ")
					.append("select event_end_date from v_event where event_name like :eventName ")
					.append("order by 1 desc NULLS LAST) ")
					.append("where rownum=1 ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("eventName", eventName);
			q.setParameter("product", product);
			q.setParameter("trainerEmail", trainerEmail);

			strList = q.list();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (strList != null) {

				strings = sdf.format(strList.get(0));
			}

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return strings;
	}

	public String[] getProductsForEvent(String event) throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		String[] strings = null;
		String string = new String();
		List list = null;
		List<String> strList = new ArrayList<String>();
		try {
			queryString
					.append("select distinct product_name from event_product_course_mapping where event_name like :event ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("event", event);
			strList = q.list();

			if (strList != null) {

				strings = strList.toArray(new String[strList.size()]);
			}

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return strings;
	}

	public void gotoCallProcToRevoke(String ntID, String event,
			String selEmail, String choice) throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();

		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("call SP_REVOKE_GT_ACCESS(:ntID,:event,:selEmail,:choice) ");

			String q1 = queryString.toString();
			// System.out.println("quer2 " + queryString);

			ts = session.beginTransaction();
			Query q = session.createSQLQuery(q1);
			q.setParameter("ntID", ntID);
			q.setParameter("event", event);
			q.setParameter("selEmail", selEmail);
			q.setParameter("choice", choice);

			int result = q.executeUpdate();

			// System.out.println("Result : " + result);
			ts.commit();
		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("acceptLegalConsent Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	/* Sanjeev begin code change validGT to accept or reject event invitation */
	public String checkValidGtForInvitation(String ntid, String eventName) {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		String strings = null;
		String string = new String();
		List list = null;
		List<String> strList = new ArrayList<String>();

		try {
			queryString
					.append("SELECT rep_email  FROM guest_trainer_event  WHERE rep_email IN (SELECT distinct(rep_email) FROM guest_trainer WHERE UPPER(rep_ntid) LIKE UPPER(:ntid)) AND event_name LIKE :eventName and REP_EMAIL_ISSENT='Y'");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("eventName", eventName);
			q.setParameter("ntid", ntid);
			strList = q.list();

			// System.out.println("list:"+strList);

			/*
			 * if (strList != null && strList.get(0)!=null) { strings =
			 * strList.get(0).toString(); }
			 */

			if (!(strList.isEmpty())) {
				strings = strList.get(0).toString();
			}

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return strings;

	}

	/*
	 * end sanjeev -- before public String checkInviteResponse(String
	 * ntid,String eventName)
	 */
	public String checkInviteResponse(String ntid, String eventName,
			String product) {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		String strings = null;
		String string = new String();
		product = product.toUpperCase();
		List list = null;
		List<Character> strList = new ArrayList<Character>();
		try {
			queryString
					.append("SELECT rep_isaccepted  FROM guest_trainer_event  WHERE rep_email IN (SELECT distinct(rep_email) FROM guest_trainer WHERE UPPER(rep_ntid) LIKE UPPER(:ntid)) AND event_name LIKE :eventName AND upper(product) = :product");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("eventName", eventName);
			q.setParameter("ntid", ntid);
			q.setParameter("product", product);

			strList = q.list();

			if (strList != null && strList.get(0) != null) {
				System.out.println("Value of Indexs" + strList.size());
				strings = strList.get(0).toString();
				System.out.println("Value at first cell at DB level" + strings);
			}

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return strings;
	}

	public String[] getGTemailByNTID(String ntid) {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		String[] strings = null;
		String string = new String();
		List list = null;
		List<String> strList = new ArrayList<String>();
		try {
			queryString
					.append("SELECT distinct(rep_email) FROM guest_trainer WHERE UPPER(rep_ntid) LIKE UPPER(:ntid) ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("ntid", ntid);
			strList = q.list();

			if (strList != null) {

				strings = strList.toArray(new String[strList.size()]);
			}

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return strings;
	}

	/*
	 * Added By Ankit on 23 July To fetch Gt Ntid based on Email
	 */
	public String getNtidByEmail(String email) {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		String string = new String();
		List<String> strList = new ArrayList<String>();
		try {
			queryString
					.append("Select Unique Upper(REP_NTID) from Guest_Trainer where LOWER(REP_EMAIL)=LOWER(:email) ");
			String q1 = queryString.toString();

			Query q = session.createSQLQuery(q1);
			q.setParameter("email", email);
			strList = q.list();
			if (strList != null && strList.get(0) != null) {
				System.out.println("Value of Indexs" + strList.size());
				string = strList.get(0).toString();
				System.out.println("Value at first cell at DB level" + string);
			}

		} catch (HibernateException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return string;
	}

	/*
	 * End
	 */

	/*
	 * Added By Ankit on 23 July To fetch Gt Ntid based on Email
	 */
	public String getMapidForTrainer(String email, String product, String event) {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		String string = new String();
		List<String> strList = new ArrayList<String>();
		try {
			queryString.append("SELECT MAP_ID " + " FROM GUEST_TRAINER_EVENT "
					+ "WHERE     UPPER (event_name) = UPPER (:event) "
					+ "    AND UPPER (rep_Email) = UPPER (:email) "
					+ "  AND UPPER (PRODUCT) = UPPER (:product) ");
			String q1 = queryString.toString();

			Query q = session.createSQLQuery(q1);
			q.setParameter("email", email);
			q.setParameter("product", product);
			q.setParameter("event", event);

			strList = q.list();
			if (strList != null && strList.get(0) != null) {
				string = strList.get(0).toString();
				System.out.println("Value at first cell at DB level" + string);
			}

		} catch (HibernateException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return string;
	}

	/*
	 * End
	 */

	/*
	 * Added By Manish on 2/12/2016 To Store DateTime mapping
	 */
	public void R(String[][] dateTimeArray, String mapId,
			List<Integer> hourList, String email, String event, String product) {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List<Integer> strList = new ArrayList<Integer>();
		Transaction tx = null;
		Integer id;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date d1;

		List<Integer> finalHourList = new ArrayList<Integer>();
		finalHourList = hourList;

		System.out.println("finalHourList2:" + finalHourList);
		// Work in progress

		// work in progress

		// queryString.append("SELECT MAX(ID ) FROM TRAINER_EVENT_DATETIME_SLOTS");
		// String q1 = queryString.toString();
		//
		// Query q = session.createSQLQuery(q1);
		//
		// strList = q.list();
		// if (strList != null && strList.get(0) != null)
		// {
		// id = strList.get(0);
		// }
		// else
		// {
		// id=1;
		// }
		// System.out.println("id="+id);

		try {
			int j = 0;
			DateTimeSlots dt = new DateTimeSlots();

			tx = session.beginTransaction();
			int k = 0;
			for (int i = 0; i < dateTimeArray.length;) {

				if (j == 0) {
					// dt.setId(id++);
					dt.setMapId(Integer.parseInt(mapId));
					d1 = sdf.parse(dateTimeArray[i][0].toString());
					dt.setDateSel(d1);
					j++;
				} else {

					System.out.println("finalHourList3:" + finalHourList);
					dt.setSlots(dateTimeArray[i][1].toString());

					dt.setTotalhrs(finalHourList.get(i));
					dt.setEmail(email);
					dt.setEvent(event);
					dt.setProduct(product);

					j = 0;
					i++;
					// System.out.println("id is"+id);
					session.save(dt);
					dt = new DateTimeSlots();
				}

				if (i % 50 == 0) {
					session.flush();
					session.clear();
				}

			}
			tx.commit();

		} catch (HibernateException e) {
			System.out.println("Method failed Due to Below reason..");
			e.printStackTrace();
			tx.rollback();

		} catch (Exception e) {
			System.out.println("Method failed Due to Below reason..");
			e.printStackTrace();
			tx.rollback();

		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	/*
	 * End
	 */

	/*
	 * Added By Manish on 2/12/2016 To Store DateTime mapping
	 */
	public void saveDateTimeSlots(String[][] dateTimeArray, String mapId,
			List<Integer> hourList, String email, String event, String product)
			throws Exception {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List<Integer> strList = new ArrayList<Integer>();
		Transaction tx = null;
		Integer id;
		Integer max_ID;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date d1;

		List<Integer> finalHourList = new ArrayList<Integer>();
		finalHourList = hourList;

		System.out.println("finalHourList2:" + finalHourList);

		try {
			int j = 0;
			DateTimeSlots dt = new DateTimeSlots();

			tx = session.beginTransaction();
			int k = 0;
			for (int i = 0; i < dateTimeArray.length;) {

				if (j == 0) {
					// dt.setId(id++);
					dt.setMapId(Integer.parseInt(mapId));
					d1 = sdf.parse(dateTimeArray[i][0].toString());
					dt.setDateSel(d1);
					j++;

				} else {

					System.out.println("finalHourList3:" + finalHourList);
					dt.setSlots(dateTimeArray[i][1].toString());

					dt.setTotalhrs(finalHourList.get(i));
					dt.setEmail(email);
					dt.setEvent(event);
					dt.setProduct(product);

					j = 0;
					i++;
					System.out.println("id is" + dt.getId());
					id = (Integer) session.save(dt);
					System.out.println("id 2 is:-" + dt.getId());
					dt = new DateTimeSlots();
				}

				if (i % 50 == 0) {
					session.flush();
					session.clear();
				}

			}
			tx.commit();

		} catch (HibernateException e) {
			System.out.println("Method failed Due to Below reason..");
			e.printStackTrace();
			tx.rollback();
			throw new Exception(e.getCause());
		} catch (Exception e) {
			System.out.println("Method failed Due to Below reason..");
			e.printStackTrace();
			tx.rollback();
			throw e;

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void unpublishOlderVersion() throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();

		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("update LegalConsentTemplate set publishFlag='N' where publishFlag='Y'");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			ts = session.beginTransaction();
			Query q = session.createQuery(q1);

			int result = q.executeUpdate();

			// System.out.println("Result : " + result);
			ts.commit();
		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("acceptLegalConsent Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void publishLegalTemplate(LegalConsentTemplate legalConsentTemplate)
			throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();

		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("Update LegalConsentTemplate set publishFlag='Y', publishedBy=:publishedBy, ")
					.append("publishedDate=sysdate where lcId=:lcId");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			ts = session.beginTransaction();
			Query q = session.createQuery(q1);

			q.setParameter("publishedBy", legalConsentTemplate.getPublishedBy());
			q.setParameter("lcId", legalConsentTemplate.getLcId());

			int result = q.executeUpdate();
			ts.commit();

			// System.out.println("Result : " + result);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("acceptLegalConsent Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
	}

	public LegalConsentTemplate getPublishedVersion() throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		LegalConsentTemplate legalObj = new LegalConsentTemplate();
		List list = null;

		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString
					.append("select version from legal_consent_template where publish_flag='Y'");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			list = q.list();

			if (list != null) {
				legalObj.setVersion(((BigDecimal) list.get(0)).intValue());
				// System.out.println("size " + list.size());
			}

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return legalObj;
	}

	public LegalConsentTemplate[] getAllLegalTemplates() throws SQLException {
		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();

		LegalConsentTemplate[] legalConsentTemplates = null;

		List<LegalConsentTemplate> legList = new ArrayList<LegalConsentTemplate>();

		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("Select version as version, lc_id as lcId from legal_consent_template ORDER BY LC_ID DESC");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);

			List list = q.list();

			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			LegalConsentTemplate legalConsentTemplate = null;
			while (it.hasNext()) {
				legalConsentTemplate = new LegalConsentTemplate();
				Object[] field = (Object[]) it.next();
				legalConsentTemplate.setVersion(((BigDecimal) field[0])
						.intValue());
				legalConsentTemplate
						.setLcId(((BigDecimal) field[1]).intValue());
				legList.add(legalConsentTemplate);

			}

			legalConsentTemplates = legList
					.toArray(new LegalConsentTemplate[legList.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return legalConsentTemplates;

	}

	public int getMaxVersion() throws SQLException {

		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();

		Integer version = null;

		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("select max(Version) from legal_consent_template");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);

			List list = q.list();
			// System.out.println("size " + list.size());

			version = (list.size() > 0) ? ((BigDecimal) list.get(0)).intValue()
					: null;

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return version;

	}

	public LegalConsentTemplate getLegalTemplateById(int lcId)
			throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List<LegalConsentTemplate> legList = new ArrayList<LegalConsentTemplate>();
		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString
					.append("Select lc_content as content, publish_flag as publishFlag,")
					.append("version as version from legal_consent_template where LC_ID=:lcId");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("lcId", lcId);

			List list = q.list();

			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			LegalConsentTemplate legalConsentTemplate = null;
			while (it.hasNext()) {
				legalConsentTemplate = new LegalConsentTemplate();
				Object[] field = (Object[]) it.next();
				legalConsentTemplate.setContent((String) field[0]);
				// legalConsentTemplate.setPublishFlag((String.valueOf(field[1])));
				legalConsentTemplate
						.setPublishFlag(field[1] != null ? ((Character) field[1])
								.toString() : null);
				legalConsentTemplate.setVersion((((BigDecimal) field[2])
						.intValue()));
				legList.add(legalConsentTemplate);
			}

			// System.out.println("New User List Array Length" +
			// legList.size());

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return (legList.size() > 0) ? legList.get(0) : null;

	}

	public int getMaxLcId() throws SQLException {
		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();

		Integer lc_id = null;

		try {
			// System.out.println("quer1" + queryString);

			queryString.append("select max(LC_ID) from legal_consent_template");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			List list = q.list();
			// System.out.println("size " + list.size());

			lc_id = (list.size() > 0) ? ((BigDecimal) list.get(0)).intValue()
					: null;

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return lc_id;

	}

	public void createNewVersion(LegalConsentTemplate legalConsentTemplate)
			throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;

		try {

			String sql = "insert into legal_Consent_Template (lc_Id,lc_content,version,publish_Flag,modified_By,modified_Date) "
					+ " values (:lcId, :content, :version, 'N', :modifiedBy, sysdate) ";

			ts = session.beginTransaction();

			Query q = session.createSQLQuery(sql);

			q.setParameter("lcId", legalConsentTemplate.getLcId());
			q.setParameter("content", legalConsentTemplate.getContent());
			q.setParameter("version", legalConsentTemplate.getVersion());
			q.setParameter("modifiedBy", legalConsentTemplate.getModifiedBy());

			int result = q.executeUpdate();

			ts.commit();

			// System.out.println("Result : " + result);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("acceptLegalConsent Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void overWriteVersion(LegalConsentTemplate legalConsentTemplate)
			throws SQLException {
		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();
		Transaction ts = null;

		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "start of getAdhocDistributionListMember");

			// System.out.println("quer1" + queryString);
			// System.out.println("content" +
			// legalConsentTemplate.getContent());
			// System.out.println("modifiedBy"+
			// legalConsentTemplate.getModifiedBy());
			// System.out.println("version" +
			// legalConsentTemplate.getVersion());

			System.out
					.println("******Inside SCEControlImpl overWriteVersion() - Check content *****"
							+ legalConsentTemplate.getContent());

			queryString
					.append("update LegalConsentTemplate set content=:content,")
					.append("modifiedBy=:modifiedBy, modifiedDate=sysdate where version=:version");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			ts = session.beginTransaction();
			Query q = session.createQuery(q1);

			q.setParameter("content", legalConsentTemplate.getContent());
			q.setParameter("modifiedBy", legalConsentTemplate.getModifiedBy());
			q.setParameter("version", legalConsentTemplate.getVersion());

			int result = q.executeUpdate();
			ts.commit();
			// System.out.println("Result : " + result);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("acceptLegalConsent Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	/**
	 * @jc:sql statement=
	 *         "(SELECT DISTINCT t.rep_email AS repemail, e.rep_isaccepted AS isaccepted, t.REP_FNAME as fname,t.REP_LNAME as lname,  t.rep_location AS replocation,  t.rep_role AS reprole,  t.rep_associated_product AS associatedproduct, e.rep_email_issent AS repemailissent  FROM guest_trainer t JOIN event_product_course_mapping  ON product_name = rep_associated_product  LEFT JOIN guest_trainer_event e ON t.rep_email = e.rep_email   and  e.EVENT_NAME like {event}   WHERE event_product_course_mapping.event_name LIKE {event} )"
	 */
	/* Changes begin Release2 */

	/*
	 * public GuestTrainer[] getGTByEvent(String event) throws SQLException {
	 * Session session = HibernateUtils.getHibernateSession(); StringBuilder
	 * queryString = new StringBuilder(); GuestTrainer[] guestTrainers = null;
	 * List list = null; List<GuestTrainer> guList = new
	 * ArrayList<GuestTrainer>(); GuestTrainer gtObj=new GuestTrainer(); String
	 * product=gtObj.getAssociatedProduct();
	 * System.out.println("product:"+product);
	 * 
	 * try { //
	 * getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
	 * 
	 * queryString.append(
	 * "(SELECT DISTINCT t.rep_email AS repemail, e.rep_isaccepted AS isaccepted, t.REP_FNAME as fname,"
	 * +
	 * "t.REP_LNAME as lname,  t.rep_location AS replocation,  t.rep_role AS reprole,  "
	 * +
	 * "t.rep_associated_product AS associatedproduct, e.rep_email_issent AS repemailissent,"
	 * + "t.REP_LAST_EVENT_DATE as lastEventDate  FROM guest_trainer t " +
	 * "JOIN event_product_course_mapping  ON product_name = rep_associated_product  "
	 * +
	 * "LEFT JOIN guest_trainer_event e ON t.rep_email = e.rep_email   and  e.EVENT_NAME like :event  "
	 * +
	 * " WHERE event_product_course_mapping.event_name LIKE :event )order by e.rep_isaccepted nulls first,"
	 * + "t.rep_associated_product,t.rep_last_event_date nulls first ");
	 * 
	 * queryString .append(
	 * "SELECT DISTINCT t.rep_email AS repemail, e.rep_isaccepted AS isaccepted,"
	 * ) .append("t.REP_FNAME as fname,t.REP_LNAME as lname,")
	 * .append("t.rep_location AS replocation,  t.rep_role AS reprole,")
	 * .append("t.rep_associated_product AS associatedproduct,")
	 * .append("e.rep_email_issent AS repemailissent  FROM guest_trainer t ")
	 * .append(
	 * "JOIN event_product_course_mapping ON product_name = rep_associated_product "
	 * )
	 * .append("LEFT JOIN guest_trainer_event e ON t.rep_email = e.rep_email and "
	 * ) .append(
	 * "e.EVENT_NAME like :event WHERE event_product_course_mapping.event_name LIKE :event"
	 * );
	 * 
	 * String q1 = queryString.toString(); // System.out.println("quer2" +
	 * queryString);
	 * 
	 * Query q = session.createSQLQuery(q1); q.setParameter("event", event);
	 * 
	 * list = q.list(); // System.out.println("size " + list.size()); Iterator
	 * it = list.iterator(); GuestTrainer guestTrainer = null;
	 * 
	 * while (it.hasNext()) { guestTrainer = new GuestTrainer(); Object[] field
	 * = (Object[]) it.next();
	 * 
	 * guestTrainer.setRepEmail((String) field[0]); guestTrainer
	 * .setIsAccepted(field[1] != null ? ((Character) field[1]) .toString() :
	 * null); guestTrainer.setFname((String) field[2]);
	 * guestTrainer.setLname((String) field[3]);
	 * guestTrainer.setRepLocation((String) field[4]);
	 * guestTrainer.setRepRole((String) field[5]);
	 * guestTrainer.setAssociatedProduct((String) field[6]);
	 * guestTrainer.setRepEmailIsSent((String) field[7]);
	 * guestTrainer.setLastEventDate((Date) field[8]); //
	 * System.out.println("rep email is sent:"+
	 * guestTrainer.getRepEmailIsSent()); guList.add(guestTrainer);
	 * 
	 * } guestTrainers = guList.toArray(new GuestTrainer[guList.size()]); }
	 * catch (HibernateException e) { //
	 * getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY, //
	 * "getDistributionListFilters --> HibernateException : ", e);
	 * e.printStackTrace(); //
	 * System.out.println("getAttendeeByEmplId Hibernatate Exception"); } catch
	 * (Exception e) { e.printStackTrace(); //
	 * System.out.println("Exception e "); } finally {
	 * 
	 * HibernateUtils.closeHibernateSession(session); }
	 * 
	 * return guestTrainers; }
	 */

	public GuestTrainer[] getGTByEvent(String event, String product)
			throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		GuestTrainer[] guestTrainers = null;
		List list = null;
		List<GuestTrainer> guList = new ArrayList<GuestTrainer>();
		/*
		 * GuestTrainer gtObj=new GuestTrainer(); String
		 * product=gtObj.getAssociatedProduct();
		 * System.out.println("product:"+product);
		 */
		product = product.toUpperCase();

		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString
					.append("(SELECT DISTINCT t.rep_email AS repemail, e.rep_isaccepted AS isaccepted, t.REP_FNAME as fname,"
							+ "t.REP_LNAME as lname,  t.rep_location AS replocation,  t.rep_role AS reprole,  "
							+ "t.rep_associated_product AS associatedproduct, e.rep_email_issent AS repemailissent,"
							+ "t.REP_LAST_EVENT_DATE as lastEventDate  FROM guest_trainer t "
							+ "JOIN event_product_course_mapping  ON product_name = rep_associated_product  "
							// +
							// "LEFT JOIN guest_trainer_event e ON t.rep_email = e.rep_email   and  e.EVENT_NAME like :event  "
							// +
							// " WHERE event_product_course_mapping.event_name LIKE :event )order by e.rep_isaccepted nulls first,"
							// and e.Product = 'VIAGRA'
							+ "LEFT JOIN guest_trainer_event e ON t.rep_email = e.rep_email   and  e.EVENT_NAME like :event and upper(e.Product) = :product "
							+ " WHERE event_product_course_mapping.event_name LIKE :event and upper(product_name) = :product  )order by e.rep_isaccepted nulls first,"
							+ "t.rep_associated_product,t.rep_last_event_date nulls first ");

			/*
			 * queryString .append(
			 * "SELECT DISTINCT t.rep_email AS repemail, e.rep_isaccepted AS isaccepted,"
			 * ) .append("t.REP_FNAME as fname,t.REP_LNAME as lname,")
			 * .append("t.rep_location AS replocation,  t.rep_role AS reprole,")
			 * .append("t.rep_associated_product AS associatedproduct,")
			 * .append(
			 * "e.rep_email_issent AS repemailissent  FROM guest_trainer t ")
			 * .append(
			 * "JOIN event_product_course_mapping ON product_name = rep_associated_product "
			 * ) .append(
			 * "LEFT JOIN guest_trainer_event e ON t.rep_email = e.rep_email and "
			 * ) .append(
			 * "e.EVENT_NAME like :event WHERE event_product_course_mapping.event_name LIKE :event"
			 * );
			 */
			String q1 = queryString.toString();
			System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("event", event);
			q.setParameter("product", product);

			list = q.list();
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();
			GuestTrainer guestTrainer = null;

			while (it.hasNext()) {
				guestTrainer = new GuestTrainer();
				Object[] field = (Object[]) it.next();

				guestTrainer.setRepEmail((String) field[0]);
				guestTrainer
						.setIsAccepted(field[1] != null ? ((Character) field[1])
								.toString() : null);
				guestTrainer.setFname((String) field[2]);
				guestTrainer.setLname((String) field[3]);
				guestTrainer.setRepLocation((String) field[4]);
				guestTrainer.setRepRole((String) field[5]);
				guestTrainer.setAssociatedProduct((String) field[6]);
				guestTrainer.setRepEmailIsSent((String) field[7]);
				guestTrainer.setLastEventDate((Date) field[8]);
				// System.out.println("rep email is sent:"+
				// guestTrainer.getRepEmailIsSent());
				guList.add(guestTrainer);

			}
			guestTrainers = guList.toArray(new GuestTrainer[guList.size()]);
		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return guestTrainers;
	}

	/* Changes end Release2 */

	/* Changes begin Release2 */

	public String[] getEventProducts(String event) {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		String[] strings = null;
		String string = new String();
		List list = null;
		List<String> strList = new ArrayList<String>();
		try {
			queryString
					.append("select Unique PRODUCT_NAME from EVENT_PRODUCT_COURSE_MAPPING ")
					.append("where event_name = :event");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("event", event);
			strList = q.list();

			if (strList != null) {

				strings = strList.toArray(new String[strList.size()]);
			}

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return strings;
	}

	/** end by ankit **/
	/* Changes end Release2 */

	public String[] getEventName() {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		String[] strings = null;
		String string = new String();
		List list = null;
		List<String> strList = new ArrayList<String>();
		try {
			queryString
					.append("select EVENT_NAME from V_EVENT ")
					.append("where EVENT_STATUS IN ( 'ACTIVE','INPROGRESS') ORDER BY EVENT_CREATED_ON DESC");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			// q.setParameter("bu_id",businessUnit);
			strList = q.list();

			if (strList != null) {

				strings = strList.toArray(new String[strList.size()]);
			}

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return strings;
	}

	// Added on 4june
	public String[] getEventProductName(String event) {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		String[] strings = null;
		String string = new String();
		List list = null;
		List<String> strList = new ArrayList<String>();
		try {
			queryString.append("select  from event_product_course_mapping ")
					.append("where EVENT_NAME = :event");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			strList = q.list();

			if (strList != null) {

				strings = strList.toArray(new String[strList.size()]);
			}

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return strings;
	}

	// end
	/**
	 * @jc:sql statement=
	 *         "(select EVENT_START_DATE as eventStartDate,EVENT_END_DATE as eventEndDate , TYPE_OF_EVAL as typeOfEval,EVAL_DURATION as evalDuration,NUMBER_OF_EVAL as numberOfEval from V_EVENT where EVENT_NAME like {event} )"
	 */
	public EventsCreated getEventByName(String event) throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List list = null;
		List<EventsCreated> evList = new ArrayList<EventsCreated>();

		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString
					.append("select EVENT_START_DATE as eventStartDate,")
					.append("EVENT_END_DATE as eventEndDate , TYPE_OF_EVAL as typeOfEval,")
					.append(" EVAL_DURATION as evalDuration,NUMBER_OF_EVAL as numberOfEval ")
					.append("from V_EVENT where EVENT_NAME like :event");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("event", event);

			list = q.list();
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();
			EventsCreated eventsCreated = null;

			while (it.hasNext()) {
				eventsCreated = new EventsCreated();
				Object[] field = (Object[]) it.next();

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

				eventsCreated.setEventStartDate(df.format((Date) field[0]));
				eventsCreated.setEventEndDate(df.format((Date) field[1]));
				eventsCreated.setTypeOfEval((String) field[2]);
				if (field[3] != null)
					eventsCreated.setEvalDuration(new Integer(field[3]
							.toString()));
				if ((BigDecimal) field[4] != null)
					eventsCreated.setNumberOfEval(((BigDecimal) field[4])
							.intValue());
				evList.add(eventsCreated);

			}

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return (evList.size() > 0) ? evList.get(0) : null;
	}

	public GuestTrainer getGTforAccess(String email) throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		List list = null;
		List<GuestTrainer> guList = new ArrayList<GuestTrainer>();

		try {
			// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,

			queryString
					.append("SELECT DISTINCT REP_FNAME AS fname,REP_LNAME AS lname,")
					.append("REP_NTID  AS ntid  FROM guest_trainer  WHERE REP_EMAIL like :email");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("email", email);

			list = q.list();
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();
			GuestTrainer guestTrainer = null;

			while (it.hasNext()) {
				guestTrainer = new GuestTrainer();
				Object[] field = (Object[]) it.next();

				guestTrainer.setFname((String) field[0]);
				guestTrainer.setLname((String) field[1]);
				guestTrainer.setNtid((String) field[2]);
				guList.add(guestTrainer);

			}

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return (guList.size() > 0) ? guList.get(0) : null;

	}

	/**
	 * @jc:sql statement=
	 *         "(select EVENT_END_DATE from V_EVENT where EVENT_NAME like {event} )"
	 */
	public Date getEventDate(String event) throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		Date date = new Date();
		List list = null;
		List<Date> datList = new ArrayList<Date>();
		try {
			queryString
					.append("select EVENT_END_DATE from V_EVENT where EVENT_NAME like :event ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("event", event);
			datList = q.list();

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return (datList.size() > 0) ? datList.get(0) : null;
	}

	/**
	 * @jc:sql statement="{call SP_GT_ADD_UPDATE(?,?,?,?,?)}"
	 */
	public void gotoCallProcToAccess(String fName, String lName,
			String emailSel, String ntID, Date startDate) throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();

		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("call SP_GT_ADD_UPDATE(:fName,:lName,:emailSel,:ntID,:startDate) ");

			String q1 = queryString.toString();
			// System.out.println("quer2 " + queryString);

			ts = session.beginTransaction();
			Query q = session.createSQLQuery(q1);
			q.setParameter("fName", fName);
			q.setParameter("lName", lName);
			q.setParameter("emailSel", emailSel);
			q.setParameter("ntID", ntID);
			q.setParameter("startDate", startDate);

			int result = q.executeUpdate();

			// System.out.println("Result : " + result);
			ts.commit();
		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("acceptLegalConsent Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	/**
	 * @jc:sql statement:: update trainer_learners_mapping SET EMAIL_SENT ='Y'
	 *         where learner_email like {learnerEmail} and EVENT_NAME like
	 *         {eventName} and COURSE_START_DATE =
	 *         to_date({courseStartDate},'mm/dd/yyyy') and PRODUCT like
	 *         {productName}::
	 */
	public void updateTrainerLearnerMappingEmailSent(String eventName,
			String courseStartDate, String learnerEmail, String productName) {

		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();

		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("update TrainerLearnerMapping SET emailSent ='Y' ")
					.append("where learnerEmail like :learnerEmail and eventName like ")
					.append(":eventName and courseStartDate = to_date(:courseStartDate,")
					.append("'mm/dd/yyyy') and product like :productName");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			ts = session.beginTransaction();
			Query q = session.createQuery(q1);
			q.setParameter("learnerEmail", learnerEmail);
			q.setParameter("eventName", eventName);
			q.setParameter("courseStartDate", courseStartDate);
			q.setParameter("productName", productName);
			int result = q.executeUpdate();

			// System.out.println("Result : " + result);
			ts.commit();
		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("acceptLegalConsent Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	// megha end

	// madhuri start

	TemplateVersion[] getAllTemplateVersions() throws SQLException,
			ParseException {

		Session session = HibernateUtils.getHibernateSession();

		TemplateVersion[] arraycreated = null;

		StringBuilder queryString = new StringBuilder();

		try {

			queryString
					.append("select tv.template_version_id as id,tv.template_id as templateId, t.name as templateName, tv.version as version,tv.form_title as formTitle, tv.publish_flag as publishFlag, tv.scoring_system_identifier as scoringSystemIdentifier, tv.modified_date as modifiedDate, tv.created_by as createdBy, "
							+ "u.ntid AS createdByNtid from template t, template_version tv, "
							+ "users u where t.template_id = tv.template_id and tv.modified_by = u.user_id(+) "
							+ "order by t.name asc,tv.version desc ");

			String q1 = queryString.toString();
			System.out.println("query to get all template versions"
					+ queryString);

			List<TemplateVersion> newListTemplateVersion = new ArrayList<TemplateVersion>();
			Query q = session
					.createSQLQuery(q1)
					.addScalar("id")
					.addScalar("templateId")
					.addScalar("templateName")
					.addScalar("version")
					.addScalar("formTitle")
					.addScalar("publishFlag")
					.addScalar("scoringSystemIdentifier")
					.addScalar("modifiedDate",
							org.hibernate.type.TimestampType.INSTANCE)
					.addScalar("createdBy").addScalar("createdByNtid");

			List<TemplateVersion> list = q.list();

			Iterator it = list.iterator();

			TemplateVersion templateVersion2 = null;
			while (it.hasNext()) {
				templateVersion2 = new TemplateVersion();
				Object[] field = (Object[]) it.next();

				templateVersion2.setId(((BigDecimal) field[0]).intValue());
				templateVersion2.setTemplateId(((BigDecimal) field[1])
						.intValue());
				templateVersion2.setTemplateName(field[2].toString());
				templateVersion2.setVersion(((BigDecimal) field[3]).intValue());
				templateVersion2.setFormTitle(field[4].toString());
				templateVersion2.setPublishFlag(field[5].toString());
				templateVersion2
						.setScoringSystemIdentifier(field[6].toString());

				templateVersion2.setModifiedDate((Date) field[7]);

				/*
				 * Date date; SimpleDateFormat formatter = new
				 * SimpleDateFormat("yyyy-MM-dd");
				 * 
				 * date = formatter.parse(str_date);
				 * 
				 * templateVersion2.setModifiedDate(date);
				 */

				if ((((BigDecimal) field[8]) != null)) {
					templateVersion2.setCreatedBy(((BigDecimal) field[8])
							.intValue());
				}

				String exm = " ";
				try {
					exm = ((String) field[9].toString());
				} catch (Exception e) {
					// System.out.println("the exm field is" + exm);
				}
				// System.out.println("The valur of the exm is" + exm);
				if (!exm.equalsIgnoreCase(" ")) {
					templateVersion2.setCreatedByNtid(field[9].toString());
				}

				newListTemplateVersion.add(templateVersion2);

			}

			// System.out.println("New Template version List Array Length"+
			// newListTemplateVersion.size());

			arraycreated = newListTemplateVersion
					.toArray(new TemplateVersion[newListTemplateVersion.size()]);

			// System.out.println("New User List Array Length"+
			// newListTemplateVersion.size());

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return arraycreated;

	}

	public SCEReport[] getTotalAssigned(Integer eventId) {
		SCEReport[] sce = null;
		return sce;
	}

	public ScoringSystem[] getScoringSystem() {
		System.out
				.println("Inside the SCEControlImpl's getScoringSystem method...");
		Session session = HibernateUtils.getHibernateSession();

		ScoringSystem[] scoreArray = null;

		StringBuilder queryString = new StringBuilder();

		try {

			queryString
					.append("SELECT score_id as scoreId, scoring_system_identifier as scoringSystemIdentifier,")
					.append(" score_value as scoreValue,score_legend as scoreLegend FROM scoring_system ")
					.append("order by score_id asc");

			String q1 = queryString.toString();

			List<ScoringSystem> newListScoringSystem = new ArrayList<ScoringSystem>();
			Query q = session.createSQLQuery(q1);
			List<ScoringSystem> list = q.list();

			Iterator it = list.iterator();

			ScoringSystem scoringSystem2 = null;
			while (it.hasNext()) {
				scoringSystem2 = new ScoringSystem();
				Object[] field = (Object[]) it.next();

				scoringSystem2.setScoreId(((BigDecimal) field[0]).intValue());
				scoringSystem2.setScoringSystemIdentifier(field[1].toString());
				scoringSystem2.setScoreValue(field[2].toString());
				scoringSystem2.setScoreLegend(field[3].toString());

				newListScoringSystem.add(scoringSystem2);

			}

			// System.out.println("New Scoring List Array Length"+
			// newListScoringSystem.size());

			scoreArray = newListScoringSystem
					.toArray(new ScoringSystem[newListScoringSystem.size()]);

			return scoreArray;

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return scoreArray;

	}

	public TemplateVersion getTemplateVersionByIdNew(Integer templateVersionId)
			throws SQLException {
		System.out
				.println("Inside SCEControlImpl's getTemplateVersionByIdNew method ...");
		Session session = HibernateUtils.getHibernateSession();
		List<TemplateVersion> templist = new ArrayList<TemplateVersion>();
		TemplateVersion templateVersion = null;
		StringBuilder queryString = new StringBuilder();
		try {

			queryString
					.append("SELECT tv.template_version_id AS id,tv.template_id AS templateId,")
					.append("t.name AS templateName,tv.version AS version,tv.form_title AS formTitle,")
					.append("tv.create_date AS createDate,")
					.append("tv.created_by AS createdBy,u.ntid AS createdByNtid,tv.precall_comments AS precallComments,")
					.append("tv.postcall_comments AS postcallComments,tv.overall_comments AS overallComments,tv.hlc_critical AS hlcCritical,")
					.append("tv.uploaded_blank_file_id AS uploadedBlankFileId,tv.autocredit_value AS autoCreditValue,tv.publish_flag AS publishFlag,")
					.append("tv.scoring_system_identifier AS scoringSystemIdentifier,tv.legal_section_flag AS legalSectionFlag,")
					.append("tv.hlc_critical_value AS hlcCriticalValue,tv.conflict_overall_score AS conflictOverallScore,tv.comments AS comments,")
					.append("tv.precall_flag AS preCallFlag,tv.postcall_flag AS postCallFlag,tv.precall_label AS preCallLabel,")
					.append("tv.postcall_label AS postCallLabel,tv.modified_date AS modifiedDate,tv.modified_by AS modifiedBy,")
					.append("tv.uploaded_date AS uploadedDate,tv.uploaded_by AS uploadedBy,tv.TAMPLATE_TITLE as templatetitle,tv.CALLIMAGE_FLAG as callImageDisplay,OVERALL_EVALUATION_LABLE as overallEvaluationLable,  ")
					.append("PRE_CALL_IMAGE  as preCallimage, POST_CALL_IMAGE as postCallImage, CALL_LABEL_DISPLAY as callLabelDisplay, CALL_LABEL_VALUE as callLabelValue   ")
					.append("FROM  template t,template_version tv,users u ")
					.append("WHERE t.template_id = tv.template_id AND tv.created_by = u.user_id(+) ")
					.append("AND tv.template_version_id = :templateVersionId ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("templateVersionId", templateVersionId);
			List list = q.list();

			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			while (it.hasNext()) {
				templateVersion = new TemplateVersion();
				Object[] field = (Object[]) it.next();
				templateVersion
						.setId((field[0] != null) ? ((BigDecimal) field[0])
								.intValue() : null);

				templateVersion
						.setTemplateId((field[1] != null) ? ((BigDecimal) field[1])
								.intValue() : null);
				templateVersion.setTemplateName((field[2] != null) ? (field[2]
						.toString()) : null);
				templateVersion
						.setVersion((field[3] != null) ? ((BigDecimal) field[3])
								.intValue() : null);
				templateVersion.setFormTitle((field[4] != null) ? (field[4]
						.toString()) : null);

				templateVersion.setCreateDate((Date) field[5]);

				templateVersion
						.setCreatedBy((field[6] != null) ? ((BigDecimal) field[6])
								.intValue() : null);
				templateVersion.setCreatedByNtid((field[7] != null) ? (field[7]
						.toString()) : null);

				templateVersion
						.setPrecallComments((field[8] != null) ? (field[8]
								.toString()) : null);

				templateVersion
						.setPostcallComments((field[9] != null) ? (field[9]
								.toString()) : null);

				templateVersion
						.setOverallComments((field[10] != null) ? (field[10]
								.toString()) : null);
				templateVersion.setHlcCritical((field[11] != null) ? (field[11]
						.toString()) : null);

				templateVersion
						.setUploadedBlankFileId((field[12] != null) ? ((BigDecimal) field[12])
								.intValue() : null);

				templateVersion
						.setAutoCreditValue((field[13] != null) ? (field[13]
								.toString()) : null);
				templateVersion.setPublishFlag((field[14] != null) ? (field[14]
						.toString()) : null);
				templateVersion
						.setScoringSystemIdentifier((field[15] != null) ? (field[15]
								.toString()) : null);
				templateVersion
						.setLegalSectionFlag((field[16] != null) ? (field[16]
								.toString()) : null);
				templateVersion
						.setHlcCriticalValue((field[17] != null) ? (field[17]
								.toString()) : null);

				templateVersion
						.setConflictOverallScore((field[18] != null) ? (field[18]
								.toString()) : null);

				templateVersion.setComments((field[19] != null) ? (field[19]
						.toString()) : null);
				templateVersion.setPreCallFlag((field[20] != null) ? (field[20]
						.toString()) : null);
				templateVersion
						.setPostCallFlag((field[21] != null) ? (field[21]
								.toString()) : null);
				templateVersion
						.setPreCallLabel((field[22] != null) ? (field[22]
								.toString()) : null);
				templateVersion
						.setPostCallLabel((field[23] != null) ? (field[23]
								.toString()) : null);
				templateVersion.setModifiedDate((Date) field[24]);
				templateVersion
						.setModifiedBy((field[25] != null) ? ((BigDecimal) field[25])
								.intValue() : null);
				templateVersion.setUploadedDate((Date) field[26]);
				templateVersion.setUploadedBy((field[27] != null) ? (field[27]
						.toString()) : null);

				/* Added by Ankit */

				templateVersion
						.setTemplatetitle((field[28] != null) ? (field[28]
								.toString()) : null);

				templateVersion
						.setCallImageDisplay((field[29] != null) ? (field[29]
								.toString().toLowerCase().trim().equals("true") ? true
								: (field[29].toString().trim().equals("1") ? true
										: false))
								: true);

				templateVersion
						.setOverallEvaluationLable((field[30] != null) ? (field[30]
								.toString()) : "Overall Sales Call Evaluation");

				templateVersion
						.setPreCallImage((field[31] != null) ? (field[31]
								.toString()) : "Y");

				templateVersion
						.setPostCallImage((field[32] != null) ? (field[32]
								.toString()) : "Y");

				templateVersion
						.setCallLabelDisplay((field[33] != null) ? (field[33]
								.toString()) : "Y");

				templateVersion
						.setCallLabelValue((field[34] != null) ? (field[34]
								.toString()) : "Call Section");
				/* End */

				templist.add(templateVersion);
			}

			// System.out.println("New User List Array Length is : "+
			// templist.size());

			// System.out.println("the obeject returned is "+
			// templist.get(0).getTemplateName());

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return templist.get(0);
	}

	public Question[] getQuestionsByTemplateVersionId(Integer templateVersionId) {
		// System.out.println("inside the getQuestionsByTemplateVersionId");
		Session session = HibernateUtils.getHibernateSession();

		Question[] question = null;

		StringBuilder queryString = new StringBuilder();

		try {

			queryString
					.append("select q.question_id as id, q.title as title,")
					.append("q.display_order as displayOrder, q.description as description,")
					.append("q.critical as critical,q.template_version_id as templateVersionId ")
					.append("from QUESTION q where q.template_version_id = :templateVersionId ")
					.append("order by display_order asc");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			List<Question> newListQuestions = new ArrayList<Question>();
			Query q = session.createSQLQuery(q1);
			q.setParameter("templateVersionId", templateVersionId);
			List<Question> list = q.list();

			Iterator it = list.iterator();

			Question question2 = null;
			while (it.hasNext()) {
				question2 = new Question();
				Object[] field = (Object[]) it.next();

				question2.setId((((BigDecimal) field[0]).intValue()));
				question2.setTitle(field[1].toString());
				question2.setDisplayOrder((((BigDecimal) field[2]).intValue()));
				question2.setDescription((field[3].toString()));
				question2.setCritical((field[4].toString()));
				question2.setTemplateVersionId((((BigDecimal) field[5])
						.intValue()));

				newListQuestions.add(question2);

			}

			// System.out.println("New Scoring List Array Length"+
			// newListQuestions.size());

			question = newListQuestions.toArray(new Question[newListQuestions
					.size()]);

			return question;

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return question;
	}

	public Descriptor[] getDescriptorsByTemplateVersionId(
			Integer templateVersionId) {
		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();

		Descriptor[] descriptors = null;

		List<Descriptor> desList = new ArrayList<Descriptor>();

		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("select descriptor_id as id,question_id as questionId,description as description ")
					.append("from descriptor where question_id in (select question_id from question ")
					.append("where template_version_id = :templateVersionId) order by question_id asc,descriptor_id asc");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("templateVersionId", templateVersionId);

			List list = q.list();

			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			Descriptor descriptor = null;
			while (it.hasNext()) {
				descriptor = new Descriptor();
				Object[] field = (Object[]) it.next();
				descriptor.setId(((BigDecimal) field[0]).intValue());
				if (field[1] != null)
					descriptor
							.setQuestionId(((BigDecimal) field[1]).intValue());
				descriptor.setDescription((String) field[2]);
				desList.add(descriptor);

			}

			descriptors = desList.toArray(new Descriptor[desList.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return descriptors;

	}

	public BusinessRule[] getBusinessRulesByTemplateVersionId(
			Integer templateVersionId) throws SQLException {
		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();

		BusinessRule[] businessrule = null;

		List<BusinessRule> busruleList = new ArrayList<BusinessRule>();

		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("SELECT b.business_rule_id AS businessRuleId,b.template_version_id AS templateVersionId, ")
					.append("b.display_order AS displayOrder,b.critical_questions AS criticalQuestions, ")
					.append("b.score AS score,b.overall_score AS overallScore FROM business_rules b WHERE ")
					.append("b.template_version_id = :templateVersionId ORDER BY display_order ASC ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("templateVersionId", templateVersionId);

			List list = q.list();

			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			BusinessRule businessrule1 = null;
			while (it.hasNext()) {
				businessrule1 = new BusinessRule();
				Object[] field = (Object[]) it.next();
				businessrule1.setBusinessRuleId(((BigDecimal) field[0])
						.intValue());
				businessrule1.setTemplateVersionId(((BigDecimal) field[1])
						.intValue());
				businessrule1.setDisplayOrder(((BigDecimal) field[2])
						.intValue());
				businessrule1.setCriticalQuestions(((BigDecimal) field[3])
						.intValue());
				businessrule1.setScore((String) field[4]);
				businessrule1.setOverallScore((String) field[5]);

				busruleList.add(businessrule1);

			}

			businessrule = busruleList.toArray(new BusinessRule[busruleList
					.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return businessrule;

	}

	public EvaluationFormScore[] getEvaluationFormScoresByTemplateVersionId(
			Integer templateVersionId) throws SQLException {
		System.out
				.println("Inside SCEControl's getEvaluationFormScoresByTemplateVersionId method...");
		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();

		EvaluationFormScore[] evaluationFormScores = null;

		List<EvaluationFormScore> evalformscoreList = new ArrayList<EvaluationFormScore>();

		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("SELECT e.id AS id,e.template_version_id AS templateVersionId,e.scoring_system_identifier AS scoringSystemIdentifier, ")
					.append("e.score_value AS scoreValue,e.lms_flag AS lmsFlag,e.notification_flag AS notificationFG, ")
					.append("e.score_comment AS scorecommentFlag FROM evaluation_form_score e WHERE ")
					.append("e.template_version_id = :templateVersionId order by id ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("templateVersionId", templateVersionId);

			List list = q.list();

			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			EvaluationFormScore evalformscr1 = null;
			while (it.hasNext()) {
				evalformscr1 = new EvaluationFormScore();
				Object[] field = (Object[]) it.next();
				evalformscr1.setId(((BigDecimal) field[0]).intValue());
				// System.out.println(evalformscr1.getId());
				evalformscr1.setTemplateVersionId(((BigDecimal) field[1])
						.intValue());
				// System.out.println(evalformscr1.getTemplateVersionId());
				evalformscr1.setScoringSystemIdentifier((String) field[2]);
				// System.out.println(evalformscr1.getScoringSystemIdentifier());
				evalformscr1.setScoreValue((String) field[3]);
				// System.out.println(evalformscr1.getScoreValue());
				evalformscr1
						.setLmsFlag(field[4] != null ? ((Character) field[4])
								.toString() : null);
				// System.out.println(evalformscr1.getLmsFlag());
				evalformscr1
						.setNotificationFG(field[5] != null ? ((Character) field[5])
								.toString() : null);
				// System.out.println(evalformscr1.getNotificationFG());
				evalformscr1
						.setScorecommentFlag(field[6] != null ? ((String) field[6])
								: null);
				// System.out.println(evalformscr1.getScorecommentFlag());
				evalformscoreList.add(evalformscr1);

			}

			evaluationFormScores = evalformscoreList
					.toArray(new EvaluationFormScore[evalformscoreList.size()]);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return evaluationFormScores;

	}

	public String getEmailPublishFlag(Integer templateVersionId,
			String scoreValue, String scoringSystemIdentifier)
			throws SQLException {
		// System.out.println("inside getEmailPublishFlag1");
		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();
		List<EmailTemplate> emailpublishflag = new ArrayList<EmailTemplate>();
		EmailTemplate emailTemplate = null;
		try {

			queryString
					.append("select publish_flag from email_template where evaluation_template_name = ")
					.append("(select form_title from template_version where template_version_id = :templateVersionId) ")
					.append("and overall_score = :scoreValue and scoring_system_identifier = :scoringSystemIdentifier");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			q.setParameter("scoringSystemIdentifier", scoringSystemIdentifier);
			q.setParameter("scoreValue", scoreValue);
			q.setParameter("templateVersionId", templateVersionId);
			List list = q.list();
			// System.out.println("the query generated is " + q.toString());
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			while (it.hasNext()) {
				emailTemplate = new EmailTemplate();
				String field = String.valueOf(it.next());
				emailTemplate.setPublishFlag(field);

				emailpublishflag.add(emailTemplate);
			}

			// System.out.println("New User List Array Length"+
			// emailpublishflag.size());

			/*
			 * // System.out.println("the obeject returned is " +
			 * emailpublishflag.get(0).getPublishFlag());
			 */
			// System.out.println("getEmailPublishFlag2");
		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return ((emailpublishflag.size()) > 0) ? emailpublishflag.get(0)
				.getPublishFlag() : null;
	}

	public LegalQuestion[] getLegalQuestionsByTemplateVersionId(
			Integer templateVersionId) throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		LegalQuestion[] legalQuestions = null;
		List<LegalQuestion> legList = new ArrayList<LegalQuestion>();
		try {
			// System.out.println("getLegalQuestionsByTemplateVersionId1");
			// System.out.println("quer1" + queryString);

			queryString
					.append("SELECT l.legal_question_id AS id,l.template_version_id AS templateVersionId, ")
					.append("l.display_order AS displayOrder,l.legal_question_label AS legalQuestionLabel,")
					.append("l.legal_question AS legalQuestion FROM legal_questions l WHERE ")
					.append("l.template_version_id = :templateVersionId ORDER BY display_order ASC ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);

			q.setParameter("templateVersionId", templateVersionId);

			List list = q.list();

			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			LegalQuestion legalQuestion = null;
			while (it.hasNext()) {
				legalQuestion = new LegalQuestion();
				Object[] field = (Object[]) it.next();
				legalQuestion.setId(((BigDecimal) field[0]).intValue());
				if (field[1] != null)
					legalQuestion.setTemplateVersionId(((BigDecimal) field[1])
							.intValue());
				if (field[2] != null)
					legalQuestion.setDisplayOrder(((BigDecimal) field[2])
							.intValue());
				legalQuestion.setLegalQuestionLabel((String) field[3]);
				legalQuestion.setLegalQuestion((String) field[4]);
				legList.add(legalQuestion);
			}

			legalQuestions = legList.toArray(new LegalQuestion[legList.size()]);
			// System.out.println("getLegalQuestionsByTemplateVersionId2");
		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("getAttendeeByEmplId Hibernatate Exception");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return legalQuestions;

	}

	public Integer getNumOfFormTitles(String formTitle) throws SQLException {
		// System.out.println("getNumOfFormTitles1");
		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();
		List<Integer> count = new ArrayList<Integer>();
		Integer i = null;
		try {

			queryString
					.append("SELECT COUNT(name) from template WHERE LOWER(name) = :formTitle ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			q.setParameter("formTitle", formTitle);

			List list = q.list();

			count.add(((BigDecimal) list.get(0)).intValue());

			// System.out.println("the obeject returned is " + count.get(0));

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;
		// System.out.println("getNumOfFormTitles2");
		return count.get(0);
	}

	public Integer findLMSCourseMappingId(Integer templateId)
			throws SQLException {

		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();
		List<Integer> count = new ArrayList<Integer>();
		Integer i = null;
		try {
			// System.out.println("findLMSCourseMappingId1");
			queryString
					.append("SELECT count(course_mapping_id) from lms_course_mapping where evaluation_template_id = :templateId ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			q.setParameter("templateId", templateId);
			// System.out.println("the query formed is" + q);
			List list = q.list();

			count.add(((BigDecimal) list.get(0)).intValue());

			// System.out.println("the obeject returned is " + count.get(0));

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		// System.out.println("findLMSCourseMappingId2");
		return count.get(0);

	}

	public Integer getNextTemplateVersionId() {
		Session session = HibernateUtils.getHibernateSession();
		List list = new ArrayList();
		try {
			// System.out.println("getNextTemplateVersionId1");
			StringBuilder queryString = new StringBuilder();
			queryString.append("select sq_template_version.nextval from dual");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			list = q.list();
			// System.out.println("the value is"+ (((BigDecimal)
			// list.get(0)).intValue()));
			return (((BigDecimal) list.get(0)).intValue());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		// System.out.println("getNextTemplateVersionId2");
		return (((BigDecimal) list.get(0)).intValue());
	}

	public Integer getCurrentVersion(Integer templateId) {
		Session session = HibernateUtils.getHibernateSession();
		List list = new ArrayList();
		try {
			// System.out.println("getCurrentVersion1");
			StringBuilder queryString = new StringBuilder();
			queryString
					.append("select max(version) from TEMPLATE_version tv where tv.template_id = :templateId");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			q.setParameter("templateId", templateId);
			list = q.list();
			// System.out.println("the value is"+ (((BigDecimal)
			// list.get(0)).intValue()));
			// System.out.println("getCurrentVersion2");
			return (((BigDecimal) list.get(0)).intValue());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return (((BigDecimal) list.get(0)).intValue());
	}

	public void saveTemplateVersionNew(TemplateVersion templateVersion) {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		// System.out.println("saveTemplateVersionNew");
		// System.out.println("quer1" + queryString);
		try {

			templateVersion.setCreateDate(new Date());
			templateVersion.setModifiedDate(new Date());

			Transaction ts = session.beginTransaction();

			session.persist(templateVersion);

			ts.commit();
			// System.out.println("saveTemplateVersionNew2");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void deleteBusinessRules(Integer templateVersionId) {
		Session session = HibernateUtils.getHibernateSession();
		List list = new ArrayList();
		Transaction ts = null;
		try {
			// System.out.println("deleteBusinessRules");
			StringBuilder queryString = new StringBuilder();
			ts = session.beginTransaction();
			queryString
					.append("DELETE FROM BusinessRule WHERE templateVersionId = :templateVersionId ");
			String q1 = queryString.toString();
			Query q = session.createQuery(q1);
			q.setParameter("templateVersionId", templateVersionId);
			q.executeUpdate();
			ts.commit();
			// System.out.println("deleteBusinessRules2");
		} catch (HibernateException e) {

			e.printStackTrace();
		}

		finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void deleteEvaluationFormScores(Integer templateVersionId) {
		Session session = HibernateUtils.getHibernateSession();
		List list = new ArrayList();
		Transaction ts = null;
		try {
			// System.out.println("deleteEvaluationFormScores1");
			StringBuilder queryString = new StringBuilder();
			ts = session.beginTransaction();
			queryString
					.append("DELETE FROM EvaluationFormScore WHERE templateVersionId = :templateVersionId ");
			String q1 = queryString.toString();
			Query q = session.createQuery(q1);
			q.setParameter("templateVersionId", templateVersionId);
			q.executeUpdate();
			ts.commit();
			// System.out.println("deleteEvaluationFormScores2");
		} catch (HibernateException e) {

			e.printStackTrace();
		}

		finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void deleteLegalQuestions(Integer templateVersionId) {
		Session session = HibernateUtils.getHibernateSession();
		List list = new ArrayList();
		Transaction ts = null;
		try {
			// System.out.println("deleteLegalQuestions1");
			StringBuilder queryString = new StringBuilder();
			queryString
					.append("DELETE FROM LegalQuestion WHERE templateVersionId = :templateVersionId ");
			String q1 = queryString.toString();
			ts = session.beginTransaction();
			Query q = session.createQuery(q1);
			q.setParameter("templateVersionId", templateVersionId);
			q.executeUpdate();
			ts.commit();
			// System.out.println("deleteLegalQuestions2");
		} catch (HibernateException e) {

			e.printStackTrace();
		}

		finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void deleteDescriptors(Integer templateVersionId) {
		Session session = HibernateUtils.getHibernateSession();
		List list = new ArrayList();
		Transaction ts = null;
		try {
			// System.out.println("deleteDescriptors1");
			StringBuilder queryString = new StringBuilder();
			ts = session.beginTransaction();
			queryString
					.append("DELETE FROM Descriptor WHERE question_Id IN (SELECT question_Id from QUESTION WHERE template_Version_Id = :templateVersionId) ");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			q.setParameter("templateVersionId", templateVersionId);
			q.executeUpdate();
			ts.commit();
			// System.out.println("deleteDescriptors2");
		} catch (HibernateException e) {

			e.printStackTrace();
		}

		finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void deleteQuestions(Integer templateVersionId) {
		Session session = HibernateUtils.getHibernateSession();
		List list = new ArrayList();
		Transaction ts = null;
		try {
			// System.out.println("deleteQuestions1");
			StringBuilder queryString = new StringBuilder();
			ts = session.beginTransaction();
			queryString
					.append("DELETE FROM Question WHERE templateVersionId = :templateVersionId ");
			String q1 = queryString.toString();
			Query q = session.createQuery(q1);
			q.setParameter("templateVersionId", templateVersionId);
			Integer result = q.executeUpdate();
			System.out.println("result" + result);
			ts.commit();
			// System.out.println("deleteQuestions2");
		} catch (HibernateException e) {

			e.printStackTrace();
		}

		finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public Integer getNextTemplateId() {
		Session session = HibernateUtils.getHibernateSession();
		List list = new ArrayList();
		try {
			// System.out.println("getNextTemplateId1");
			StringBuilder queryString = new StringBuilder();
			queryString.append("select sq_template.nextval from dual");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			list = q.list();
			// System.out.println("the value is"+ (((BigDecimal)
			// list.get(0)).intValue()));
			// System.out.println("getNextTemplateId2");
			return (((BigDecimal) list.get(0)).intValue());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return (((BigDecimal) list.get(0)).intValue());
	}

	public void saveTemplate(TemplateVersion templateVersion) {
		// System.out.println("saveTemplate1");
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		LegalQuestion[] legalQuestions = null;
		List<LegalQuestion> legList = new ArrayList<LegalQuestion>();
		try {
			// System.out.println("quer1" + queryString);

			Template t = new Template();
			t.setId(templateVersion.getTemplateId());
			t.setName(templateVersion.getFormTitle());
			t.setLastModifiedDate(templateVersion.getModifiedDate());
			t.setCurrentVersion(templateVersion.getVersion());
			Transaction ts = session.beginTransaction();

			session.persist(t);

			ts.commit();
			// System.out.println("saveTemplate2");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void saveTemplateToEventCourse(Integer templateId) {
		// System.out.println("saveTemplateToEventCourse1");
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List list = new ArrayList();
		try {
			queryString.append("select sq_event_course.nextval from dual");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			list = q.list();
			// System.out.println("quer1" + queryString);

			EventCourse e = new EventCourse();
			e.setEventCourseId((((BigDecimal) list.get(0)).intValue()));
			e.setEventId(4);
			e.setCourseName("ALL");
			e.setTemplateId(templateId);
			e.setTableName("SCE_FFT");
			Transaction ts = session.beginTransaction();

			session.persist(e);

			ts.commit();

			// System.out.println("saveTemplateToEventCourse2");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public Integer getNextLegalQuestionId() {
		// System.out.println("getNextLegalQuestionId1");
		Session session = HibernateUtils.getHibernateSession();
		List list = new ArrayList();
		try {
			StringBuilder queryString = new StringBuilder();
			queryString.append("select sq_legal_question.nextval from dual");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			list = q.list();
			// System.out.println("the value is"+ (((BigDecimal)
			// list.get(0)).intValue()));
			// System.out.println("getNextLegalQuestionId2");
			return (((BigDecimal) list.get(0)).intValue());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return (((BigDecimal) list.get(0)).intValue());
	}

	public void saveBusinessRule(BusinessRule businessRule) {
		// System.out.println("saveBusinessRule1");
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		try {
			// System.out.println("quer1" + queryString);

			Transaction ts = session.beginTransaction();

			session.persist(businessRule);

			ts.commit();
			// System.out.println("saveBusinessRule2");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void saveLegalQuestion(LegalQuestion legalQuestion) {
		// System.out.println("saveLegalQuestion1");
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		try {
			// System.out.println("quer1" + queryString);

			Transaction ts = session.beginTransaction();

			session.persist(legalQuestion);

			ts.commit();

			// System.out.println("saveLegalQuestion2");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public Integer getNextEvaluationFormScoreId() {
		Session session = HibernateUtils.getHibernateSession();
		List list = new ArrayList();
		try {
			// System.out.println("getNextEvaluationFormScoreId1");
			StringBuilder queryString = new StringBuilder();
			queryString
					.append("select sq_evaluation_form_score.nextval from dual ");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			list = q.list();
			// System.out.println("the value is"+ (((BigDecimal)
			// list.get(0)).intValue()));
			// System.out.println("getNextEvaluationFormScoreId2");
			return (((BigDecimal) list.get(0)).intValue());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return (((BigDecimal) list.get(0)).intValue());
	}

	public String getScoreLegend(String lowerCase) {

		Session session = HibernateUtils.getHibernateSession();
		List list = new ArrayList();
		try {
			// System.out.println("getScoreLegend1");
			StringBuilder queryString = new StringBuilder();
			queryString
					.append("select score_legend from scoring_system where LOWER(score_value) = :scoreValue ");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			q.setParameter("scoreValue", lowerCase);
			list = q.list();
			// System.out.println("the value is" + (list.get(0).toString()));
			// System.out.println("getScoreLegend2");
			return list.get(0).toString();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return list.get(0).toString();

	}

	public void saveEvaluationFormScore(EvaluationFormScore evaluationFormScore) {
		// System.out.println("saveEvaluationFormScore1");
		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction ts = session.beginTransaction();

			session.persist(evaluationFormScore);

			ts.commit();
			// System.out.println("saveEvaluationFormScore2");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void updateCurrentVersion(Integer templateId,
			Integer templateVersionId) {
		// System.out.println("updateCurrentVersion1");

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		Transaction ts = session.beginTransaction();
		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("update template set current_version = ")
					.append("(select version from template_version where template_version_id = :templateVersionId),")
					.append("last_modified_date = :sysdate where template_id = :templateId ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("templateVersionId", templateVersionId);
			q.setParameter("templateId", templateId);
			q.setParameter("sysdate", new Date());

			int result = q.executeUpdate();

			// System.out.println("Result : " + result);
			ts.commit();
			// System.out.println("updateCurrentVersion2");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void updateTemplateVersion(TemplateVersion templateVersion)
			throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		System.out.println("modifiedBy " + templateVersion.getCreatedByNtid());
		Transaction ts = session.beginTransaction();
		try {
			// System.out.println("updateTemplateVersion1");

			queryString
					.append("UPDATE template_version set form_title =:formTitle,created_by =:createdBy,")
					.append("precall_comments =:precallComments,postcall_comments =:postcallComments,overall_comments =:overallComments,")
					.append("hlc_critical =:hlcCritical,autocredit_value =:autoCreditValue,publish_flag =:publishFlag,")
					.append("scoring_system_identifier =:scoringSystemIdentifier,legal_section_flag =:legalSectionFlag,hlc_critical_value =:hlcCriticalValue,")
					.append("conflict_overall_score =:conflictOverallScore,comments =:comments,precall_flag =:preCallFlag,")
					.append("postcall_flag =:postCallFlag,precall_label =:preCallLabel,postcall_label =:postCallLabel,")
					.append("modified_date =:sysdate,modified_by =:modifiedBy,TAMPLATE_TITLE =:templatetitle,CALLIMAGE_FLAG =:CALLIMAGE_FLAG,OVERALL_EVALUATION_LABLE =:overallEvaluationLable,  ")
					.append("PRE_CALL_IMAGE =:PRE_CALL_IMAGE, POST_CALL_IMAGE =:POST_CALL_IMAGE, CALL_LABEL_DISPLAY =:CALL_LABEL_DISPLAY, CALL_LABEL_VALUE =:CALL_LABEL_VALUE  ")
					.append("WHERE  (template_version_id =:id AND template_id =:templateId AND version =:version)");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("formTitle", templateVersion.getFormTitle());
			q.setParameter("createdBy", templateVersion.getCreatedBy());
			q.setParameter("precallComments",
					templateVersion.getPrecallComments());
			q.setParameter("postcallComments",
					templateVersion.getPostcallComments());
			q.setParameter("overallComments",
					templateVersion.getOverallComments());
			q.setParameter("hlcCritical", templateVersion.getHlcCritical());
			q.setParameter("autoCreditValue",
					templateVersion.getAutoCreditValue());
			q.setParameter("publishFlag", templateVersion.getPublishFlag());
			q.setParameter("scoringSystemIdentifier",
					templateVersion.getScoringSystemIdentifier());
			q.setParameter("legalSectionFlag",
					templateVersion.getLegalSectionFlag());
			q.setParameter("hlcCriticalValue",
					templateVersion.getHlcCriticalValue());
			q.setParameter("conflictOverallScore",
					templateVersion.getConflictOverallScore());
			q.setParameter("comments", templateVersion.getComments());
			q.setParameter("preCallFlag", templateVersion.getPreCallFlag());
			q.setParameter("postCallFlag", templateVersion.getPostCallFlag());
			q.setParameter("preCallLabel", templateVersion.getPreCallLabel());
			q.setParameter("postCallLabel", templateVersion.getPostCallLabel());
			q.setParameter("sysdate", new Date());
			q.setParameter("modifiedBy", templateVersion.getModifiedBy());
			q.setParameter("id", templateVersion.getId());
			q.setParameter("templateId", templateVersion.getTemplateId());
			q.setParameter("version", templateVersion.getVersion());
			q.setParameter("templatetitle", templateVersion.getTemplatetitle());
			q.setParameter("CALLIMAGE_FLAG", (templateVersion
					.isCallImageDisplay() == true ? "true" : "false"));
			q.setParameter("overallEvaluationLable",
					templateVersion.getOverallEvaluationLable());
			q.setParameter("PRE_CALL_IMAGE", templateVersion.getPreCallImage());
			q.setParameter("POST_CALL_IMAGE",
					templateVersion.getPostCallImage());
			q.setParameter("CALL_LABEL_DISPLAY",
					templateVersion.getCallLabelDisplay());
			q.setParameter("CALL_LABEL_VALUE",
					templateVersion.getCallLabelValue());

			int result = q.executeUpdate();

			// System.out.println("Result : " + result);
			ts.commit();
			// System.out.println("updateTemplateVersion2");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public Integer getNextBusinessRuleId() {
		// System.out.println("getNextBusinessRuleId1");
		Session session = HibernateUtils.getHibernateSession();
		List list = new ArrayList();
		try {
			StringBuilder queryString = new StringBuilder();
			queryString.append("select sq_business_rule.nextval from dual ");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			list = q.list();
			// System.out.println("the value is"+ (((BigDecimal)
			// list.get(0)).intValue()));
			// System.out.println("getNextBusinessRuleId2");
			return (((BigDecimal) list.get(0)).intValue());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return (((BigDecimal) list.get(0)).intValue());
	}

	public void saveDescriptor(Descriptor descriptor) {
		// System.out.println("saveDescriptor1");
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List list = new ArrayList();
		try {
			queryString.append("select sq_descriptor.nextval from dual ");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			list = q.list();
			descriptor.setId(((BigDecimal) list.get(0)).intValue());

			// System.out.println("quer1" + queryString);

			Transaction ts = session.beginTransaction();

			session.persist(descriptor);

			ts.commit();
			// System.out.println("saveDescriptor2");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public void saveQuestion(Question question) {
		// System.out.println("saveQuestion1");
		Session session = HibernateUtils.getHibernateSession();
		try {
			Transaction ts = session.beginTransaction();

			session.persist(question);

			ts.commit();

			// System.out.println("saveQuestion2");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	public Integer getNextQuestionId() {
		// System.out.println("getNextQuestionId1");
		Session session = HibernateUtils.getHibernateSession();
		List list = new ArrayList();
		try {
			StringBuilder queryString = new StringBuilder();
			queryString.append("select sq_question.nextval from dual ");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			list = q.list();
			// System.out.println("the value is"+ (((BigDecimal)
			// list.get(0)).intValue()));
			// System.out.println("getNextQuestionId2");
			return (((BigDecimal) list.get(0)).intValue());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return (((BigDecimal) list.get(0)).intValue());
	}

	public int getUploadFileId() throws SQLException {
		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();
		List<Integer> id = new ArrayList<Integer>();
		Integer i = null;
		try {

			queryString
					.append("select max(uploaded_blank_file_id) as id from uploaded_blank_form ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			// System.out.println("the query formed is" + q);
			List list = q.list();

			id.add(((BigDecimal) list.get(0)).intValue());

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return id.get(0);

	}

	public void uploadBlankForm(UploadBlankForm uploadBlankForm)
			throws SQLException {/*
								 * Session session =
								 * HibernateUtils.getHibernateSession();
								 * StringBuilder queryString = new
								 * StringBuilder(); try { //
								 * System.out.println("quer1" + queryString);
								 * 
								 * Transaction ts = session.beginTransaction();
								 * 
								 * session.persist(uploadBlankForm);
								 * 
								 * ts.commit(); } catch (HibernateException e) {
								 * // TODO Auto-generated catch block
								 * e.printStackTrace(); } finally {
								 * HibernateUtils
								 * .closeHibernateSession(session); }
								 */

		Session session = HibernateUtils.getHibernateSession();

		try {

			StringBuilder queryString = new StringBuilder();

			queryString.append("INSERT INTO UPLOADED_BLANK_FORM  ")
					.append("( ").append("UPLOADED_BLANK_FILE_ID, ")
					.append("TEMPLATE_VERSION_ID, ")
					.append("UPLOADED_BLANK_FILE  ").append(") ")
					.append("VALUES ").append("( ").append(":fileId, ")
					.append(":versionId, ").append(":content ").append(")");

			String nativeHql = queryString.toString();
			System.out.println("" + nativeHql);
			Transaction ts = session.beginTransaction();
			Query query = session.createSQLQuery(nativeHql);

			query.setParameter("fileId",
					uploadBlankForm.getUploadedBlankFileId());
			query.setParameter("versionId",
					uploadBlankForm.getTemplateVersionId());
			query.setParameter("content",
					uploadBlankForm.getUploadedBlankFile());

			/*
			 * query.setParameter("evaluationDate",
			 * evalForm.getEvaluationDate()); query.setParameter("uploadDate",
			 * evalForm.getUploadDate()); query.setParameter("product",
			 * evalForm.getProduct());
			 * 
			 * query.setParameter("repEmplid", evalForm.getRepEmplid());
			 * query.setParameter("eventId", evalForm.getEventId());
			 * query.setParameter("templateVersionId",
			 * evalForm.getTemplateVersionId());
			 * 
			 * query.setParameter("productCode", evalForm.getProductCode());
			 * query.setParameter("status", evalForm.getStatus());
			 */

			int i = query.executeUpdate();

			// System.out.println(" execute result set " + i);
			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("createUser Hibernatate Exception");

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void publishEvaluationTemplate(UploadBlankForm uploadBlankForm)
			throws SQLException {
		// System.out.println("updateCurrentVersion1");

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		Transaction ts = session.beginTransaction();
		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("update template_version set publish_flag='Y',uploaded_blank_file_id=:uploadedBlankFileId,")
					.append("uploaded_date= :sysdate,uploaded_by=:uploadedBy ")
					.append("where template_version_id=:templateVersionId ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("uploadedBlankFileId",
					uploadBlankForm.getUploadedBlankFileId());
			q.setParameter("sysdate", new Date());
			q.setParameter("uploadedBy", uploadBlankForm.getUploadedBy());
			q.setParameter("templateVersionId",
					uploadBlankForm.getTemplateVersionId());
			int result = q.executeUpdate();

			// System.out.println("Result : " + result);
			ts.commit();
			// System.out.println("updateCurrentVersion2");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public EmailTemplate checkEmailTemplate(String scoringSystemIdentifier,
			String templateName) throws SQLException {

		Session session = HibernateUtils.getHibernateSession();

		EmailTemplate emailtemplate = null;

		StringBuilder queryString = new StringBuilder();
		// String queryString = null;
		List<EmailTemplate> emaillist = new ArrayList<EmailTemplate>();
		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("select scoring_system_identifier as scoringSystemIdentifier from email_template where scoring_system_identifier=:scoringSystemIdentifier ")
					.append("and evaluation_template_name=:templateName ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("scoringSystemIdentifier", scoringSystemIdentifier);
			q.setParameter("templateName", templateName);

			List list = q.list();
			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			while (it.hasNext()) {
				emailtemplate = new EmailTemplate();
				// Object[] field = it.next();
				emailtemplate.setScoringSystemIdentifier(it.next().toString());
				emaillist.add(emailtemplate);

			}

			// System.out.println("New User List Array Length" +
			// emaillist.size());

			emailtemplate = emaillist.size() > 0 ? emaillist.get(0) : null;

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return emailtemplate;

	}

	public Integer getNextValEmailTemplateId() throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		List list = new ArrayList();
		try {

			StringBuilder queryString = new StringBuilder();
			queryString.append("select sq_email_template.nextval from dual");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			list = q.list();
			// System.out.println("the value is"+ (((BigDecimal)
			// list.get(0)).intValue()));
			return (((BigDecimal) list.get(0)).intValue());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		// System.out.println("getNextTemplateVersionId2");
		return (((BigDecimal) list.get(0)).intValue());
	}

	public void insertIntoEmailTemplate(TemplateVersion templateVersion,
			Integer templateVersionId, Integer emailTemplateId,
			String score_value) throws SQLException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		EmailTemplate email = new EmailTemplate();
		try {
			// System.out.println("quer1" + queryString);
			email.setEmailTemplateId(emailTemplateId);
			email.setEmailTemplateVersion("Draft");
			email.setEvaluationTemplateVersionId(templateVersionId);
			email.setScoringSystemIdentifier(templateVersion
					.getScoringSystemIdentifier());
			email.setPublishFlag("N");
			email.setEvaluationTemplateName(templateVersion.getTemplateName());
			email.setOverallScore(score_value);
			Transaction ts = session.beginTransaction();

			session.persist(email);

			ts.commit();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void updateEmailTemplateVersion(String templateName,
			String scoringSystemIdentifier, Integer templateVersionId)
			throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();

		Transaction ts = session.beginTransaction();
		try {
			// System.out.println("quer1" + queryString);

			queryString
					.append("update email_template set template_version_id=:templateVersionId ")
					.append("where evaluation_template_name=:templateName and scoring_system_identifier=:scoringSystemIdentifier ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			Query q = session.createSQLQuery(q1);
			q.setParameter("templateVersionId", templateVersionId);
			q.setParameter("templateName", templateName);
			q.setParameter("scoringSystemIdentifier", scoringSystemIdentifier);

			int result = q.executeUpdate();

			// System.out.println("Result : " + result);
			ts.commit();

		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	// madhuri end
	// Apoorva start

	/*
	 * public void insertSCEComment(String sceid,String activity_id,String
	 * phaseNo,String rep_id,String comment1,String comment2,String
	 * comment3,String enteredby1,String enteredby2,String enteredby3,String
	 * date1,String date2,String date3,String submitted_by,String
	 * submitted_date)throws SCEException { // SCEControlImpl sceControl=new
	 * SCEControlImpl(); Session session = HibernateUtils.getHibernateSession();
	 * CourseEvalTemplateMapping[] mappingsForTemplates = null; Transaction ts =
	 * null; StringBuilder queryString = new StringBuilder();
	 * 
	 * List<CourseEvalTemplateMapping> attendeeList = new
	 * ArrayList<CourseEvalTemplateMapping>();
	 * 
	 * try {
	 * 
	 * queryString.append("update sce_comments set comment_1 = "+comment1+
	 * ",comment_2 = "+comment2+",comment_3 = "+comment3+", ")
	 * .append("entered_by_1 = "
	 * +enteredby1+",entered_by_2 = "+enteredby2+",entered_by_3 = "
	 * +enteredby3+", ")
	 * .append("date_1 = "+date1+",date_2 = "+date2+",date_3 = "
	 * +date3+",submitted_by = "+submitted_by+", ")
	 * .append("submitted_date ="+submitted_date
	 * +" where sce_id ="+sceid+" and phase_no ="
	 * +phaseNo+" and representative_id ="+rep_id+" ");
	 * 
	 * String q1 = queryString.toString();
	 * 
	 * // System.out.println("quer2" + queryString); Query q =
	 * session.createSQLQuery(q1);
	 * 
	 * 
	 * // Query q = //
	 * session.createSQLQuery(q1).addEntity(CourseEvalTemplateMapping.class); ts
	 * = session.beginTransaction(); int result = q.executeUpdate();
	 * ts.commit(); // System.out.println("deleted" + result);
	 * 
	 * } catch (Exception e) { // log.error(LoggerHelper.getStackTrace(e));
	 * e.printStackTrace(); throw new SCEException("error.sce.unknown", e); }
	 * finally { HibernateUtils.closeHibernateSession(session); }
	 * 
	 * }
	 */

	public void updateSCEComment(String sceid, String activity_id,
			String phaseNo, String rep_id, String comment1, String comment2,
			String comment3, String enteredby1, String enteredby2,
			String enteredby3, String date1, String date2, String date3,
			String submitted_by, String submitted_date) throws SCEException {
		// SCEControlImpl sceControl=new SCEControlImpl();
		Session session = HibernateUtils.getHibernateSession();
		CourseEvalTemplateMapping[] mappingsForTemplates = null;
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();

		List<CourseEvalTemplateMapping> attendeeList = new ArrayList<CourseEvalTemplateMapping>();

		try {

			queryString
					.append("update sce_comments set comment_1 =:comment1,comment_2 =:comment2,comment_3 =:comment3, ")
					.append("entered_by_1 =:enteredby1,entered_by_2 =:enteredby2,entered_by_3 =:enteredby3, ")
					.append("date_1 =:date1,date_2 =:date2,date_3 =:date3,submitted_by =:submitted_by, ")
					.append("submitted_date =:submitted_date where sce_id =:sceid and phase_no =:phaseNo and representative_id =:rep_id ");

			String q1 = queryString.toString();

			System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("comment1", comment1);
			q.setParameter("comment2", comment2);
			q.setParameter("comment3", comment3);
			q.setParameter("enteredby1", enteredby1);
			q.setParameter("enteredby2", enteredby2);
			q.setParameter("enteredby3", enteredby3);
			q.setParameter("date1", date1);
			q.setParameter("date2", date2);
			q.setParameter("date3", date3);
			q.setParameter("submitted_by", submitted_by);
			q.setParameter("submitted_date", submitted_date);
			q.setParameter("sceid", sceid);
			q.setParameter("phaseNo", phaseNo);
			q.setParameter("rep_id", rep_id);

			// Query q =
			// session.createSQLQuery(q1).addEntity(CourseEvalTemplateMapping.class);
			ts = session.beginTransaction();
			int result = q.executeUpdate();
			ts.commit();
			// System.out.println("deleted" + result);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public SCEComment getExistingSCEComment(String phaseId, String sceId,
			String repId) throws SQLException, SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		SCEComment sceComment = null;
		StringBuilder queryString = new StringBuilder();

		List<SCEComment> sceCommentList = new ArrayList<SCEComment>();

		try {

			queryString
					.append("select sce_id as sceId,activity_id as activityId,phase_no as phaseNo,representative_id as reprenstativeName, ")
					.append("comment_1 as comment1,comment_2 as comment2,comment_3 as comment3,entered_by_1 as enteredBy1,entered_by_2 as enteredBy2,entered_by_3 as enteredBy3, ")
					.append("date_1 as date1,date_2 as date2,date_3 as date3,submitted_by as submittedBy,submitted_date as submittedDate ")
					.append("from sce_comments ")
					.append("where sce_id =:sceId and representative_id =:repId and phase_no=:phaseId ");

			String q1 = queryString.toString();

			System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("phaseId", phaseId);
			q.setParameter("sceId", sceId);
			q.setParameter("repId", repId);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			SCEComment scecomment = null;

			while (it.hasNext()) {

				scecomment = new SCEComment();
				Object[] field = (Object[]) it.next();

				scecomment.setSceId((String) field[0]);
				scecomment.setActivityId((String) field[1]);
				scecomment.setPhaseNo((String) field[2]);
				scecomment.setReprenstativeName((String) field[3]);
				scecomment.setComment1((String) field[4]);
				scecomment.setComment2((String) field[5]);
				scecomment.setComment3((String) field[6]);
				scecomment.setEnteredBy1((String) field[7]);
				scecomment.setEnteredBy2((String) field[8]);
				scecomment.setEnteredBy3((String) field[9]);
				scecomment.setDate1((String) field[10]);
				scecomment.setDate2((String) field[11]);
				scecomment.setDate3((String) field[12]);
				scecomment.setSubmittedBY((String) field[13]);
				scecomment.setSubmittedDate((String) field[14]);

				sceCommentList.add(scecomment);

			}
			// sceComment = sceCommentList;

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return (sceCommentList.size() > 0) ? sceCommentList.get(0) : null;
	}

	public Integer isSCEComment(String phaseId, String sceId, String repId)
			throws SQLException, SCEException {
		Session session = HibernateUtils.getHibernateSession();

		Integer result = 0;
		StringBuilder queryString = new StringBuilder();

		try {

			queryString
					.append("select count(*) from sce_comments where sce_id =:sceId and representative_Id =:repId and phase_No =:phaseId ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);

			q.setParameter("phaseId", phaseId);
			q.setParameter("sceId", sceId);
			q.setParameter("repId", repId);

			List list = q.list();

			result = (list.size() > 0) ? ((BigDecimal) list.get(0)).intValue()
					: null;

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return result;
	}

	public SCEComment[] getSCEComment(String trackid, String repid)
			throws SQLException, SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		SCEComment[] sceComment = null;
		StringBuilder queryString = new StringBuilder();

		List<SCEComment> sceCommentList = new ArrayList<SCEComment>();

		try {

			queryString
					.append("select sce_id as sceId,activity_id as activityId,phase_no as phaseNo,representative_id as reprenstativeName, ")
					.append("comment_1 as comment1,comment_2 as comment2,comment_3 as comment3,entered_by_1 as enteredBy1,entered_by_2 as enteredBy2,entered_by_3 as enteredBy3, ")
					.append("date_1 as date1,date_2 as date2,date_3 as date3,submitted_by as submittedBy,submitted_date as submittedDate ")
					.append("from sce_comments ")
					.append("where sce_id = (select sce_id from feedback_track_mapping where track_id=:trackid) and representative_id =:repid ");

			String q1 = queryString.toString();

			System.out.println("quer 2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("trackid", trackid);
			q.setParameter("repid", repid);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			SCEComment scecomment = null;

			while (it.hasNext()) {

				scecomment = new SCEComment();
				Object[] field = (Object[]) it.next();

				scecomment.setSceId((String) field[0]);
				scecomment.setActivityId((String) field[1]);
				scecomment.setPhaseNo((String) field[2]);
				scecomment.setReprenstativeName((String) field[3]);
				scecomment.setComment1((String) field[4]);
				scecomment.setComment2((String) field[5]);
				scecomment.setComment3((String) field[6]);
				scecomment.setEnteredBy1((String) field[7]);
				scecomment.setEnteredBy2((String) field[8]);
				scecomment.setEnteredBy3((String) field[9]);
				scecomment.setDate1((String) field[10]);
				scecomment.setDate2((String) field[11]);
				scecomment.setDate3((String) field[12]);
				scecomment.setSubmittedBY((String) field[13]);
				scecomment.setSubmittedDate((String) field[14]);

				sceCommentList.add(scecomment);

			}
			sceComment = sceCommentList.toArray(new SCEComment[sceCommentList
					.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return sceComment;
	}

	public String getSceFeedbackId(String trackid) throws SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		String trackId = null;

		StringBuilder queryString = new StringBuilder();

		List<String> trackIdList = new ArrayList<String>();

		try {

			queryString
					.append("select sce_id from feedback_track_mapping where track_id=:trackid ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("trackid", trackid);
			List list = q.list();

			// System.out.println("size " + list.size());

			if (list.size() > 0) {
				trackId = (String) list.get(0);
			}

			// trackIdList.addAll(list);

			// trackId = trackIdList.toString();

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return trackId;
	}

	public Template[] getAllEvaluationTemplates() throws SCEException {
		// SCEControlImpl sceControl=new SCEControlImpl();
		Session session = HibernateUtils.getHibernateSession();
		Template[] evaluationTemplates = null;
		StringBuilder queryString = new StringBuilder();

		List<Template> evaluationTemplatesList = new ArrayList<Template>();
		try {

			queryString
					.append(" select distinct tv.template_id as id, tv.form_title as name,")
					.append("tv.modified_date as lastModifiedDate,tv.version as currentVersion ")
					.append("from template_version tv,event_course ec,")
					.append("(select template_id,form_title,max(version) version from template_version where publish_flag='Y' group by (form_title,template_id)) t ")
					.append("where tv.version=t.version and tv.TEMPLATE_ID=t.TEMPLATE_ID ")
					.append("and ec.template_id = tv.template_id AND ec.event_id = 4 ")
					.append("order by lower(name) asc ");

			String q1 = queryString.toString();
			// System.out.println("quer2" + queryString);

			// Query q = session.createQuery(queryString);
			Query q = session.createSQLQuery(q1);
			List list = q.list();

			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			Template template = null;
			while (it.hasNext()) {

				template = new Template();
				Object[] field = (Object[]) it.next();
				template.setId(((BigDecimal) field[0]).intValue());
				template.setName(field[1].toString());
				template.setLastModifiedDate((Date) field[2]);
				template.setCurrentVersion(((BigDecimal) field[3]).intValue());

				evaluationTemplatesList.add(template);

			}

			// System.out.println("New User List Array Length"+
			// evaluationTemplatesList.size());

			evaluationTemplates = evaluationTemplatesList
					.toArray(new Template[evaluationTemplatesList.size()]);
		}

		catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return evaluationTemplates;
	}

	public CourseEvalTemplateMapping[] getAllMappingsForTemplate(
			Integer evalTemplateId) throws SCEException {
		// SCEControlImpl sceControl=new SCEControlImpl();
		Session session = HibernateUtils.getHibernateSession();
		CourseEvalTemplateMapping[] mappingsForTemplates = null;

		StringBuilder queryString = new StringBuilder();

		List<CourseEvalTemplateMapping> mappingsForTemplatesList = new ArrayList<CourseEvalTemplateMapping>();

		try {

			queryString.append("select course_mapping_id as mappingId,")
					.append("evaluation_form_name as name,")
					.append("evaluation_form_version as version,")
					.append("lms_course_name as courseName,")
					.append("lms_course_code as courseCode,")
					.append("activity_pk as activityPk ")
					.append("from lms_course_mapping ")
					.append("where evaluation_template_id = :evalTemplateId ")
					.append("order by lms_course_code ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("evalTemplateId", evalTemplateId);

			List list = q.list();

			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			CourseEvalTemplateMapping courseEvalTemplateMapping = null;
			while (it.hasNext()) {

				courseEvalTemplateMapping = new CourseEvalTemplateMapping();
				Object[] field = (Object[]) it.next();
				courseEvalTemplateMapping.setMappingId(((BigDecimal) field[0])
						.intValue());
				courseEvalTemplateMapping.setName(field[1].toString());
				courseEvalTemplateMapping.setVersion(((BigDecimal) field[2])
						.intValue());
				courseEvalTemplateMapping.setCourseName(field[3].toString());
				courseEvalTemplateMapping.setCourseCode(field[4].toString());
				courseEvalTemplateMapping.setActivityPk(((BigDecimal) field[5])
						.intValue());

				mappingsForTemplatesList.add(courseEvalTemplateMapping);

			}
			mappingsForTemplates = mappingsForTemplatesList
					.toArray(new CourseEvalTemplateMapping[mappingsForTemplatesList
							.size()]);
		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return mappingsForTemplates;
	}

	public CourseDetails[] getAllSearchedCourseDetails(String courseName,
			String courseCode) throws SCEException {

		Session session = HibernateUtils.getHibernateSession();
		CourseDetails[] searchedCourseDetails = null;

		StringBuilder queryString = new StringBuilder();

		List<CourseDetails> attendeeList = new ArrayList<CourseDetails>();
		try {
			if (courseName.equals("") && courseCode.equals("")) {
				queryString
						.append("select distinct (activity_pk) as courseId,")
						.append("activity_code as courseCode, activityname as courseName ")
						.append("from tr.mv_usp_activity_hierarchy ")
						.append("where (activity_code like('598%') or activity_code like('USFT%') or activity_code like('GCO-NA%')) ")
						.append("and active = 1 ")
						.append("order by activityname asc");
			} else if (!courseName.equals("") && courseCode.equals("")) {
				queryString
						.append("select distinct (activity_pk) as courseId,")
						.append("activity_code as courseCode, activityname as courseName ")
						.append("from tr.mv_usp_activity_hierarchy ")
						.append("where (activity_code like('598%') or activity_code like('USFT%') or activity_code like('GCO-NA%')) ")
						.append("and active = 1 ")
						.append("and lower(activityname) like lower('%"
								+ courseName.replaceAll("'", "''")
								+ "%') order by activityname asc ");
			} else if (courseName.equals("") && !courseCode.equals("")) {
				queryString
						.append("select distinct (activity_pk) as courseId,")
						.append("activity_code as courseCode, activityname as courseName ")
						.append("from tr.mv_usp_activity_hierarchy ")
						.append("where (activity_code like('598%') or activity_code like('USFT%') or activity_code like('GCO-NA%')) ")
						.append("and active = 1 ")
						.append("order by activityname asc");
			} else if (!courseName.equals("") && !courseCode.equals("")) {
				queryString
						.append("select distinct (activity_pk) as courseId,")
						.append("activity_code as courseCode, activityname as courseName ")
						.append("from tr.mv_usp_activity_hierarchy ")
						.append("where (activity_code like('598%') or activity_code like('USFT%') or activity_code like('GCO-NA%')) ")
						.append("and active = 1 ")
						.append("and (lower(activity_code) like lower('%"
								+ courseCode + "%') ")
						.append("or lower(activityname) like lower('%"
								+ courseName.replaceAll("'", "''")
								+ "%')) order by activityname asc");
			}
			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);

			// Query q =
			// session.createSQLQuery(q1).addEntity(CourseEvalTemplateMapping.class);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			CourseDetails template = null;
			while (it.hasNext()) {

				template = new CourseDetails();
				Object[] field = (Object[]) it.next();
				template.setCourseName(field[2].toString());
				// System.out.println(field[2].toString());
				template.setCourseCode(field[1].toString());
				template.setCourseId(((BigDecimal) field[0]).longValue());
				// System.out.println(((BigDecimal) field[0]).longValue());

				attendeeList.add(template);

			}
			searchedCourseDetails = attendeeList
					.toArray(new CourseDetails[attendeeList.size()]);

		} catch (Exception e) {
			e.printStackTrace();
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return searchedCourseDetails;

	}

	public void deleteSelLmsRecord(Integer delLmsId, Integer selTemplateId)
			throws SCEException {
		// SCEControlImpl sceControl=new SCEControlImpl();
		Session session = HibernateUtils.getHibernateSession();
		CourseEvalTemplateMapping[] mappingsForTemplates = null;
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();

		List<CourseEvalTemplateMapping> attendeeList = new ArrayList<CourseEvalTemplateMapping>();

		try {

			queryString.append("delete from lms_course_mapping ")
					.append("where course_mapping_id = :delLmsId ")
					.append("and evaluation_template_id = :selTemplateId ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("delLmsId", delLmsId);
			q.setParameter("selTemplateId", selTemplateId);

			// Query q =
			// session.createSQLQuery(q1).addEntity(CourseEvalTemplateMapping.class);
			ts = session.beginTransaction();
			int result = q.executeUpdate();
			ts.commit();
			// System.out.println("deleted" + result);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public CourseEvalTemplateMapping[] saveMappedRecords(String emplId,
			String excludedActivityPk, String selectedTemplateId,
			String selectedTemplateName, String selectedCourseIds)
			throws SCEException {
		CourseEvalTemplateMapping[] mapping = null;
		try {

			int selTemplateId = 0;
			Session session = HibernateUtils.getHibernateSession();
			Transaction ts = null;
			// Integer[] arrActivityPk=null;
			Integer[] arrActivityPk = null;
			String[] arrExcludeActPk = null;
			// StringBuilder queryString = new StringBuilder();
			// emplId has to be retrieved from session
			// String emplId="00318299";
			String sqlQueryFixed = "insert into lms_course_mapping l "
					+ "(course_mapping_id, " + "evaluation_form_name, "
					+ "evaluation_form_version, " + "lms_course_code, "
					+ "lms_course_name, " + "created_date, " + "created_by, "
					+ "activity_pk, " + "evaluation_template_id) " + "values "
					+ "(sq_lms_course_mapping.nextval, ";
			String sqlQuery = "";
			String delimiter = "";
			// String notInClause="where activity_pk not in(";

			// Convert selected Template ID to int
			if (!selectedTemplateId.equals("")) {
				selTemplateId = Integer.parseInt(selectedTemplateId);
			}

			// Array containing Integer values of ALL mappings to be saved
			if (!selectedCourseIds.equalsIgnoreCase("")
					&& selectedCourseIds != null) {
				String[] strArrActPk = selectedCourseIds.split(",");
				System.out
						.println("JCS:::Leng of String array strArrActPk that have been sent from FE-JPF = "
								+ strArrActPk.length);
				if (strArrActPk != null) {
					arrActivityPk = new Integer[strArrActPk.length];
					for (int i = 0; i < strArrActPk.length; i++) {
						// Integer array containing all Activity PK to be saved
						// //
						// System.out.println("strArrActPk["+i+"] = "+strArrActPk[i]);
						arrActivityPk[i] = new Integer(
								Integer.parseInt(strArrActPk[i]));
					}
				}
				System.out
						.println("Length of Integer array arrActivityPk for courses that need to be mapped= "
								+ arrActivityPk.length);
			}

			// String array containing all Activity Pk that is to be excluded
			// while saving
			if (!excludedActivityPk.equals("")) {
				arrExcludeActPk = excludedActivityPk.split(",");
				// System.out.println("Length of arrExcludeActPk = "+
				// arrExcludeActPk.length);
			}

			// Insert only those records that are sent in
			if (arrActivityPk != null && arrExcludeActPk != null) {
				System.out
						.println("Build query - 1:::Mixed Save. Some records are new");
				for (int z = 0; z < arrActivityPk.length; z++) {
					sqlQuery = "";
					sqlQuery = sqlQueryFixed
							+ "'"
							+ selectedTemplateName
							+ "', "
							+ "(select max(version) from sales_call.template_version t where publish_flag='Y' and t.template_id = "
							+ selTemplateId
							+ "), "
							+ "(select distinct activity_code from tr.mv_usp_activity_hierarchy h where h.activity_pk = "
							+ arrActivityPk[z]
							+ "), "
							+ "(select distinct activityname from tr.mv_usp_activity_hierarchy h where h.activity_pk= "
							+ arrActivityPk[z] + "), " + "sysdate, " + "'"
							+ emplId + "'" + ", " + arrActivityPk[z] + ", "
							+ selTemplateId + ")";

					Query q = session.createSQLQuery(sqlQuery);

					ts = session.beginTransaction();
					int result = q.executeUpdate();
					ts.commit();
				}// End for
			} else if (arrExcludeActPk == null && arrActivityPk != null) { // If
																			// NO
																			// selected
																			// courses
																			// exist
																			// in
																			// the
																			// DB,
																			// arrExcludedActPk
																			// is
																			// empty
				System.out
						.println("Build query - 2:::Clean Save. All new records");
				for (int z = 0; z < arrActivityPk.length; z++) {
					sqlQuery = "";
					sqlQuery = sqlQueryFixed
							+ "'"
							+ selectedTemplateName
							+ "', "
							+ "(select max(version) from sales_call.template_version t where publish_flag='Y' and t.template_id = "
							+ selTemplateId
							+ "), "
							+ "(select distinct activity_code from tr.mv_usp_activity_hierarchy h where h.activity_pk = "
							+ arrActivityPk[z]
							+ "), "
							+ "(select distinct activityname from tr.mv_usp_activity_hierarchy h where h.activity_pk= "
							+ arrActivityPk[z] + "), " + "sysdate, " + "'"
							+ emplId + "'" + ", " + arrActivityPk[z] + ", "
							+ selTemplateId + ")";
					// sceControl.saveMappedRecords(sqlQuery);

					Query q = session.createSQLQuery(sqlQuery);

					ts = session.beginTransaction();
					int result = q.executeUpdate();
					ts.commit();
				}// End for
			}// End If

			if (selTemplateId != 0) {
				mapping = getAllMappingsForTemplate(new Integer(selTemplateId));
				// System.out.println("JCS:::All mappings array length = "+
				// mapping.length);
			}

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		}

		return mapping;
	}

	public CourseDetails getCourseDetailsForActPk(int actPk)
			throws SCEException {
		// SCEControlImpl sceControl=new SCEControlImpl();

		Session session = HibernateUtils.getHibernateSession();
		CourseDetails courseDetails = null;
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();
		List<CourseDetails> courseDetailsList = new ArrayList<CourseDetails>();
		try {

			queryString.append("select distinct activity_pk as courseId,")
					.append("activityname as courseName,")
					.append("activity_code as courseCode ")
					.append("from tr.mv_usp_activity_hierarchy ")
					.append("where activity_pk = :actPk ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("actPk", actPk);

			// Query q =
			// session.createSQLQuery(q1).addEntity(CourseEvalTemplateMapping.class);
			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			while (it.hasNext()) {

				courseDetails = new CourseDetails();
				Object[] field = (Object[]) it.next();
				courseDetails.setCourseName(field[2].toString());
				// System.out.println(field[2].toString());
				courseDetails.setCourseCode(field[1].toString());
				courseDetails.setCourseId(((BigDecimal) field[0]).longValue());
				// System.out.println(((BigDecimal) field[0]).longValue());

				courseDetailsList.add(courseDetails);

			}
			courseDetails = courseDetailsList.get(0);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return courseDetails;
	}

	public CourseEvalTemplateMapping[] getAlreadyMappedCourses(
			String selCourseIds, String selCourseCodes, Integer selEvalTempId)
			throws SCEException {

		Session session = HibernateUtils.getHibernateSession();
		CourseEvalTemplateMapping[] alreadyMappedCourses = null;
		List<CourseEvalTemplateMapping> alreadyMappedCoursesList = new ArrayList<CourseEvalTemplateMapping>();
		try {
			// System.out.println("Inside manager getAlreadyMappedCourses...");
			String[] courseIdArr = null;
			String query = "select "
					+ "course_mapping_id as mappingId, evaluation_template_id as evalTemplateId, "
					+ "evaluation_form_name as name, evaluation_form_version as version, "
					+ "activity_pk as activityPk, lms_course_name as courseName, "
					+ "lms_course_code as courseCode from lms_course_mapping "
					+ "where activity_pk in(";
			String queryParam = "";
			String separator = "";
			int courseId = 0;
			// String andClause = "and evaluation_template_id = "+selEvalTempId;

			// System.out.println("Selected CourseIds = " + selCourseIds);
			// System.out.println("Selected CourseCodes = " + selCourseCodes);

			if (selCourseIds != null || !selCourseIds.equalsIgnoreCase("")) {
				courseIdArr = selCourseIds.split(",");
				// System.out.println("Length of courseIdArr = "+
				// courseIdArr.length);
			}

			if (courseIdArr != null && courseIdArr.length != 0) {
				for (int i = 0; i < courseIdArr.length; i++) {
					if (i == (courseIdArr.length - 1)) {
						separator = "";
					} else {
						separator = ",";
					}
					courseId = Integer.parseInt(courseIdArr[i]);
					queryParam = queryParam + "" + courseId + "" + separator;
				}// End for loop
			} // End main if

			// System.out.println("queryParam = " + queryParam);
			// query = query+""+queryParam+")";
			query = query + "" + queryParam + ")";// +andClause;

			Query q = session.createSQLQuery(query);

			// Query q =
			// session.createSQLQuery(q1).addEntity(CourseEvalTemplateMapping.class);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			CourseEvalTemplateMapping courseEvalTemplateMapping = null;

			while (it.hasNext()) {

				courseEvalTemplateMapping = new CourseEvalTemplateMapping();
				Object[] field = (Object[]) it.next();
				courseEvalTemplateMapping.setMappingId(((BigDecimal) field[0])
						.intValue());

				courseEvalTemplateMapping
						.setEvalTemplateId(((BigDecimal) field[1]).intValue());

				courseEvalTemplateMapping.setName(field[2].toString());

				courseEvalTemplateMapping.setVersion(((BigDecimal) field[3])
						.intValue());

				courseEvalTemplateMapping.setActivityPk(((BigDecimal) field[4])
						.intValue());

				courseEvalTemplateMapping.setCourseName(field[5].toString());

				courseEvalTemplateMapping.setCourseCode(field[6].toString());

				alreadyMappedCoursesList.add(courseEvalTemplateMapping);

			}
			alreadyMappedCourses = alreadyMappedCoursesList
					.toArray(new CourseEvalTemplateMapping[alreadyMappedCoursesList
							.size()]);

		} catch (Exception e) {
			e.printStackTrace();
			// log.error(LoggerHelper.getStackTrace(e));
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return alreadyMappedCourses;
	}

	public String[] getAllHomeStates() throws SQLException, SCEException {
		// SCEControlImpl sceControl=new SCEControlImpl();
		Session session = HibernateUtils.getHibernateSession();
		String[] allHomeStates = null;
		// Transaction ts=null;
		StringBuilder queryString = new StringBuilder();

		List<String> allHomeStatesList = new ArrayList<String>();

		try {

			/*
			 * queryString.append("select location from v_atlas_home_state ")
			 * .append("order by location asc ");
			 */

			queryString
					.append("SELECT DISTINCT (home_state) FROM V_person_tra WHERE TRIM (home_state) IS NOT NULL order by home_state asc");

			String q1 = queryString.toString();

			System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);

			List list = q.list();

			System.out.println("size " + list.size());

			allHomeStatesList.addAll(list);

			allHomeStates = allHomeStatesList
					.toArray(new String[allHomeStatesList.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return allHomeStates;
	}

	public String[] getGtToRemove(String product) throws SQLException,
			SCEException {
		Session session = HibernateUtils.getHibernateSession();
		String[] gtToRemove = null;
		// Transaction ts=null;
		StringBuilder queryString = new StringBuilder();

		List<String> gtToRemoveList = new ArrayList<String>();

		try {

			queryString
					.append("SELECT rep_email ")
					.append("FROM guest_trainer_event ")
					.append("WHERE rep_email_issent LIKE 'Y' ")
					.append("AND event_name IN ")
					.append("( SELECT DISTINCT (epcm.event_name) ")
					.append("FROM event_product_course_mapping epcm JOIN v_event e ")
					.append("ON epcm.event_name = e.event_name ")
					.append("WHERE epcm.product_name like :product ")
					.append("AND e.event_status NOT LIKE 'COMPLETE') ")
					.append("AND (rep_isaccepted NOT LIKE 'N' OR rep_isaccepted is null) ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("product", product);
			List list = q.list();

			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			gtToRemoveList.addAll(list);

			gtToRemove = gtToRemoveList.toArray(new String[gtToRemoveList
					.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return gtToRemove;

	}

	public GuestTrainer[] getAllGTForProduct(String product)
			throws SQLException, SCEException {
		// SCEControlImpl sceControl=new SCEControlImpl();
		Session session = HibernateUtils.getHibernateSession();
		GuestTrainer[] allGTForProduct = null;
		// Transaction ts=null;
		StringBuilder queryString = new StringBuilder();

		List<GuestTrainer> allGTForProductList = new ArrayList<GuestTrainer>();

		try {

			queryString
					.append("(SELECT rep_fname AS fname, rep_lname AS lname, rep_ntid AS ntid,")
					.append("rep_manager_email AS mgremail, rep_email AS repemail,")
					.append("rep_location AS replocation, rep_role AS reprole,")
					.append("rep_associated_product AS associatedproduct, rep_manager AS repmanager,")
					.append("rep_manager_role AS repmanagerrole, rep_isselected AS isselected ")
					.append(" FROM sales_call.guest_trainer ")
					.append(" WHERE rep_associated_product LIKE :product) ")
					.append("UNION ")
					.append("(SELECT first_name AS fname, last_name AS lname, ntid AS ntid,")
					.append("mgr_email_address AS mgremail, email_address AS repemail,")
					.append("home_state AS replocation, job_title AS reprole,")
					.append("product_desc AS associatedproduct, mgr_full_name AS repmanager,")
					.append("mgr_job_title AS repmanagerrole, 'N' AS isselected ")
					.append("FROM sales_call.mv_atlas_guest_trainer ")
					.append("WHERE product_desc LIKE :product ")
					.append("MINUS ")
					.append("SELECT rep_fname AS fname, rep_lname AS lname, rep_ntid AS ntid,")
					.append("rep_manager_email AS mgremail, rep_email AS repemail,")
					.append("rep_location AS replocation, rep_role AS reprole,")
					.append("rep_associated_product AS associatedproduct, rep_manager AS repmanager,")
					.append("rep_manager_role AS repmanagerrole, 'N' AS isselected ")
					.append("FROM sales_call.guest_trainer ")
					.append("WHERE rep_associated_product LIKE :product ")
					.append(") order by ISSELECTED desc ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("product", product);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			GuestTrainer guestTrainer = null;

			while (it.hasNext()) {

				guestTrainer = new GuestTrainer();
				Object[] field = (Object[]) it.next();

				guestTrainer.setFname((String) field[0]);
				;
				guestTrainer.setLname((String) field[1]);
				guestTrainer.setNtid((String) field[2]);
				guestTrainer.setMgrEmail((String) field[3]);
				guestTrainer.setRepEmail((String) field[4]);
				if (field[5] != null)
					guestTrainer.setRepLocation((String) field[5]);
				guestTrainer.setRepRole((String) field[6]);
				guestTrainer.setAssociatedProduct((String) field[7]);
				guestTrainer.setRepManager((String) field[8]);
				guestTrainer.setRepManagerRole((String) field[9]);
				guestTrainer
						.setIsSelected(field[10] != null ? ((Character) field[10])
								.toString() : null);

				allGTForProductList.add(guestTrainer);

			}
			allGTForProduct = allGTForProductList
					.toArray(new GuestTrainer[allGTForProductList.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return allGTForProduct;
	}

	public GuestTrainer[] getGTFromAtlasByProduct(String product)
			throws SQLException, SCEException {
		// SCEControlImpl sceControl=new SCEControlImpl();
		Session session = HibernateUtils.getHibernateSession();
		GuestTrainer[] gTFromAtlasByProduct = null;
		// Transaction ts=null;
		StringBuilder queryString = new StringBuilder();

		List<GuestTrainer> gTFromAtlasByProductList = new ArrayList<GuestTrainer>();

		try {

			queryString
					.append("SELECT FIRST_NAME as fname,LAST_NAME as lname,")
					.append("NTID as ntid,MGR_EMAIL_ADDRESS as mgrEmail,")
					.append("EMAIL_ADDRESS as repEmail,HOME_STATE as repLocation,")
					.append("JOB_TITLE as repRole,PRODUCT_DESC as associatedProduct ,")
					.append("MGR_FULL_NAME as repManager,MGR_JOB_TITLE as repManagerRole,")
					.append("'N' as isSelected from sales_call.MV_ATLAS_GUEST_TRAINER where PRODUCT_DESC like :product");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("product", product);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			GuestTrainer guestTrainer = null;

			while (it.hasNext()) {

				guestTrainer = new GuestTrainer();
				Object[] field = (Object[]) it.next();

				guestTrainer.setFname(field[0].toString());
				guestTrainer.setLname(field[1].toString());
				guestTrainer.setNtid(field[2].toString());
				guestTrainer.setMgrEmail(field[3].toString());
				guestTrainer.setRepEmail(field[4].toString());
				if (field[5] != null)
					guestTrainer.setRepLocation(field[5].toString());
				guestTrainer.setRepRole(field[6].toString());
				guestTrainer.setAssociatedProduct(field[7].toString());
				guestTrainer.setRepManager(field[8].toString());
				guestTrainer.setRepManagerRole(field[9].toString());
				guestTrainer.setIsSelected(field[10].toString());

				gTFromAtlasByProductList.add(guestTrainer);

			}
			gTFromAtlasByProduct = gTFromAtlasByProductList
					.toArray(new GuestTrainer[gTFromAtlasByProductList.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return gTFromAtlasByProduct;
	}

	public String[] getActiveEventName() throws SCEException {

		Session session = HibernateUtils.getHibernateSession();
		String[] activeEventName = null;
		// Transaction ts=null;
		StringBuilder queryString = new StringBuilder();

		List<String> eventList = new ArrayList<String>();

		try {

			queryString.append("select EVENT_NAME ").append(
					"from V_EVENT where EVENT_STATUS ='ACTIVE' ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);

			List list = q.list();

			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			eventList.addAll(list);

			/*
			 * while (it.hasNext()) { String[] field = (String[]) it.next();
			 * productList.add(field[0].toString()); }
			 */
			activeEventName = eventList.toArray(new String[eventList.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return activeEventName;

	}

	/*
	 * public Event[] getAllEvents() throws SQLException, SCEException {
	 * 
	 * Session session = HibernateUtils.getHibernateSession(); Event[] eventArr
	 * = null; StringBuilder queryString = new StringBuilder();
	 * 
	 * List<Event> eventList = new ArrayList<Event>();
	 * 
	 * try{
	 * 
	 * queryString
	 * .append("select event_id as id, name as name, isdefault as isDefault,")
	 * .append("display as display from event ") .append("order by name asc ");
	 * 
	 * 
	 * String q1 = queryString.toString();
	 * 
	 * // System.out.println("quer2" + queryString); Query
	 * q=session.createSQLQuery(q1);
	 * 
	 * 
	 * List list = q.list(); // System.out.println("size " + list.size());
	 * 
	 * Iterator it = list.iterator();
	 * 
	 * Event template = null;
	 * 
	 * while (it.hasNext()) {
	 * 
	 * template = new Event(); Object[] field = (Object[]) it.next();
	 * template.setId(((BigDecimal) field[0]).intValue());
	 * template.setName((String)field[1]);
	 * template.setIsDefault((String)field[2]);
	 * template.setDisplay((String)field[3]);
	 * 
	 * eventList.add(template);
	 * 
	 * } eventArr = eventList.toArray(new Event[eventList.size()]);
	 * 
	 * } catch (Exception e) { //log.error(LoggerHelper.getStackTrace(e));
	 * e.printStackTrace(); throw new SCEException("error.sce.unknown",e); }
	 * return eventArr; }
	 */

	public WebExDetails[] getWebExDetailsByEventAndUsedBy(String eventName,
			String usedby) throws SQLException, SCEException {

		Session session = HibernateUtils.getHibernateSession();
		WebExDetails[] webDetailsArr = null;
		StringBuilder queryString = new StringBuilder();

		List<WebExDetails> webDetailsList = new ArrayList<WebExDetails>();

		try {

			queryString
					.append("select CONFERENCE_CALL_NUMBER as confCallNumber,")
					.append("CHAIRPERSON_PASSCODE as chairPersonPasscode,")
					.append("PARTICIPANT_PASSCODE as participantPasscode,")
					.append("MEETING_LINK as meetingLink,")
					.append("USEDBY_TRAINER as usedby from webexdetails ")
					.append("where EVENT_NAME like :eventName and ")
					.append("USEDBY_TRAINER like :usedby ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("eventName", eventName);
			q.setParameter("usedby", usedby);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			WebExDetails template = null;

			while (it.hasNext()) {

				template = new WebExDetails();
				Object[] field = (Object[]) it.next();

				template.setConfCallNumber((String) field[0]);
				template.setChairPersonPasscode((String) field[1]);
				template.setParticipantPasscode((String) field[2]);
				template.setMeetingLink((String) field[3]);
				template.setUsedby((String) field[4]);

				webDetailsList.add(template);

			}
			webDetailsArr = webDetailsList
					.toArray(new WebExDetails[webDetailsList.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return webDetailsArr;
	}

	public WebExDetails[] getWebExDetailsByEvent(String eventName)
			throws SQLException, SCEException {

		Session session = HibernateUtils.getHibernateSession();
		WebExDetails[] webDetailsArr = null;
		StringBuilder queryString = new StringBuilder();

		List<WebExDetails> webDetailsList = new ArrayList<WebExDetails>();

		try {

			queryString.append("select event_name as eventName,")
					.append("conference_call_number as confCallNumber,")
					.append("chairperson_passcode as chairPersonPasscode,")
					.append("participant_passcode as participantPasscode,")
					.append(" meeting_link as meetingLink,")
					.append("USEDBY_TRAINER as usedby ")
					.append("from WEBEXDETAILS where event_name =:eventName ")
					.append("order by event_name asc ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("eventName", eventName);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			WebExDetails template = null;

			while (it.hasNext()) {

				template = new WebExDetails();
				Object[] field = (Object[]) it.next();
				template.setEventName((String) field[0]);
				template.setConfCallNumber((String) field[1]);
				template.setChairPersonPasscode((String) field[2]);
				template.setParticipantPasscode((String) field[3]);
				template.setMeetingLink((String) field[4]);
				template.setUsedby((String) field[5]);

				webDetailsList.add(template);

			}
			webDetailsArr = webDetailsList
					.toArray(new WebExDetails[webDetailsList.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return webDetailsArr;
	}

	public String[] getCourseDates(String eventName, String prodName)
			throws SCEException {

		Session session = HibernateUtils.getHibernateSession();
		String[] courseDateArr = null;
		// Transaction ts=null;
		StringBuilder queryString = new StringBuilder();

		List<String> courseDateList = new ArrayList<String>();

		try {

			queryString
					.append("select distinct TRUNC(COURSE_START_DATE) ")
					.append("from LEARNERS_COURSECOMPLETED where COURSE IN ")
					.append("(SELECT COURSE_CODE FROM EVENT_PRODUCT_COURSE_MAPPING ")
					.append("WHERE PRODUCT_NAME  LIKE :prodName and EVENT_NAME LIKE :eventName )  ")
					.append(" order by TRUNC(COURSE_START_DATE) ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("prodName", prodName);
			q.setParameter("eventName", eventName);

			List list = q.list();

			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			while (it.hasNext()) {
				Date field = (Date) it.next();
				courseDateList.add((sdf.format(field)));
			}
			courseDateArr = courseDateList.toArray(new String[courseDateList
					.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return courseDateArr;

	}

	public String[] getEventCourses(String eventName) throws SCEException {

		Session session = HibernateUtils.getHibernateSession();
		String[] eventCourses = null;
		// Transaction ts=null;
		StringBuilder queryString = new StringBuilder();

		List<String> eventCoursesList = new ArrayList<String>();

		try {

			queryString.append("select DISTINCT PRODUCT_NAME ")
					.append("from EVENT_PRODUCT_COURSE_MAPPING ")
					.append("where EVENT_NAME LIKE :eventName ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("eventName", eventName);

			List list = q.list();

			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			eventCoursesList.addAll(list);

			/*
			 * while (it.hasNext()) { String[] field = (String[]) it.next();
			 * productList.add(field[0].toString()); }
			 */
			eventCourses = eventCoursesList.toArray(new String[eventCoursesList
					.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return eventCourses;

	}

	public TrainerLearnerMapping[] getTrainerLearner(String datez, String pro,
			String eventName) throws SCEException {

		Session session = HibernateUtils.getHibernateSession();
		TrainerLearnerMapping[] trainLearnArr = null;
		StringBuilder queryString = new StringBuilder();
		String[] h = datez.split(" ");
		List<TrainerLearnerMapping> trainLearnList = new ArrayList<TrainerLearnerMapping>();

		try {

			/* Original query begin */

			/*
			 * queryString .append(
			 * "SELECT learner_name AS learnername, learner_loc AS learnerloc,")
			 * .
			 * append("trainer_loc AS trainerloc, trainer_name AS trainername,")
			 * .append(
			 * "learner_email AS learneremail, trainer_email AS traineremail,")
			 * .
			 * append("EMAIL_SENT as emailSent  FROM trainer_learners_mapping ")
			 * .append(" WHERE product LIKE '" + pro + "'  AND ")
			 * .append("event_name LIKE '" + eventName +
			 * "' AND TRUNC (course_start_date) LIKE TO_DATE ('" + h[0] +
			 * "', 'yyyy-mm-dd') order by trainer_name asc");
			 */
			/* Original query end */
			/*
			 * Date:15 june 2015 New Code Added By Ankit For fetching the full
			 * name in trainer learner mapping.
			 */

			/*
			 * System.out.println("Before Query"); queryString
			 * .append("SELECT * FROM (SELECT l.learner_name AS learnername, ")
			 * .append(" l.learner_loc AS learnerloc, ")
			 * .append("l.trainer_loc AS trainerloc, ")
			 * .append("l.trainer_name AS trainername, ")
			 * .append("l.learner_email AS learneremail, ")
			 * .append("l.trainer_email AS traineremail, ")
			 * .append("l.EMAIL_SENT AS emailSent,") .append(
			 * "CONCAT (CONCAT (lc.FIRST_NAME, ' '), lc.LAST_NAME) AS learnername_1, "
			 * ) .append(
			 * "CONCAT (CONCAT (g.REp_FNAME, ' '), g.REP_LNAME) AS trainername_1, "
			 * ) .append("ROW_NUMBER()")
			 * .append("OVER ( PARTITION BY l.learner_name, ")
			 * .append("l.learner_loc, ").append("l.trainer_loc, ")
			 * .append("l.trainer_name, ").append("l.learner_email, ")
			 * .append("l.trainer_email, ").append("l.EMAIL_SENT ")
			 * .append("ORDER BY NULL) ").append("rn ")
			 * .append("FROM trainer_learners_mapping l ")
			 * .append("INNER JOIN Guest_Trainer g ")
			 * .append("ON     L.TRAINER_EMAIL = g.rep_email ")
			 * .append("AND G.REP_FNAME = L.TRAINER_NAME ")
			 * .append("INNER JOIN LEARNERS_COURSECOMPLETED lc ")
			 * .append("ON     lc.Email_Address = l.learner_email ")
			 * .append("AND LC.FIRST_NAME = L.LEARNER_NAME ")
			 * .append("WHERE     l.product LIKE '" + pro + "'")
			 * .append("AND l.event_name LIKE '" + eventName + "' ")
			 * .append("AND TRUNC (l.course_start_date) LIKE ")
			 * .append("TO_DATE ('" + h[0] + "', 'yyyy-mm-dd') ")
			 * .append("ORDER BY trainer_name ASC) ") .append("WHERE rn = 1 ");
			 * System.out.println("After Query"); System.out.println("Query :" +
			 * queryString);
			 */

			queryString
					.append("SELECT * FROM (  SELECT l.learner_name AS learnername,l.learner_loc AS learnerloc,l.trainer_loc AS trainerloc,")
					.append("l.trainer_name AS trainername,l.learner_email AS learneremail,l.trainer_email AS traineremail,l.EMAIL_SENT AS emailSent,CONCAT (CONCAT (lc.LAST_NAME,','), lc.FIRST_NAME)")
					.append(" AS learnername_1,CONCAT (CONCAT (g.REP_LNAME, ','),  g.REp_FNAME)AS trainername_1,ROW_NUMBER ()OVER (PARTITION BY l.learner_name,")
					.append("l.learner_loc,l.trainer_loc,l.trainer_name,l.learner_email,l.trainer_email,l.EMAIL_SENT ORDER BY NULL) rn")
					.append(" FROM trainer_learners_mapping l INNER JOIN Guest_Trainer g ON     L.TRAINER_EMAIL = g.rep_email AND G.REP_FNAME = L.TRAINER_NAME INNER JOIN LEARNERS_COURSECOMPLETED lc")
					.append(" ON lc.Email_Address = l.learner_email AND LC.FIRST_NAME = L.LEARNER_NAME WHERE     l.product LIKE '"
							+ pro
							+ "'  AND l.event_name LIKE '"
							+ eventName
							+ "'")
					.append(" AND TRUNC (l.course_start_date) LIKE TO_DATE ('"
							+ h[0]
							+ "', 'yyyy-mm-dd') ORDER BY trainername ASC) WHERE rn = 1");

			/*
			 * Ankit changes end for fetching full trainer name and leraner name
			 * in trainer leraner table
			 */

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			// q.setParameter("eventName", eventName);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			TrainerLearnerMapping template = null;

			while (it.hasNext()) {

				template = new TrainerLearnerMapping();
				Object[] field = (Object[]) it.next();

				// template.setLearnerName((String) field[0]);
				template.setLearnerName((String) field[7]);
				template.setLearnerLoc((String) field[1]);
				template.setTrainerLoc((String) field[2]);
				// template.setTrainerName((String) field[3]);
				template.setTrainerName((String) field[8]);
				template.setLearnerEmail((String) field[4]);
				template.setTrainerEmail((String) field[5]);
				template.setEmailSent((String) field[6]);

				trainLearnList.add(template);

			}
			trainLearnArr = trainLearnList
					.toArray(new TrainerLearnerMapping[trainLearnList.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return trainLearnArr;
	}

	public void gotoDeleteEventMapping(String query) {

		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;

		try {

			String sql = query;

			ts = session.beginTransaction();
			Query q = session.createSQLQuery(sql);

			int i = q.executeUpdate();
			// System.out.println("Execute Update :" + i);

			ts.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	/* sanjeev begin delete mapping */
	/* public void gotoDeleteLTMapping(String delEmail) throws SCEException { */

	public void gotoDeleteLTMapping(String delEmail, String product,
			String courseStartDate) throws SCEException {
		// SCEControlImpl sceControl=new SCEControlImpl();
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();
		String startDate = courseStartDate.substring(5, 7) + "/"
				+ courseStartDate.substring(8, 10) + "/"
				+ courseStartDate.substring(0, 4);
		System.out.println("startDate" + startDate);
		try {

			/*
			 * queryString.append("DELETE from TRAINER_LEARNERS_MAPPING ").append
			 * ( "where LEARNER_EMAIL =  '" + delEmail + "' ");
			 */

			queryString.append("DELETE from TRAINER_LEARNERS_MAPPING ").append(
					"where LEARNER_EMAIL =  '" + delEmail + "' and PRODUCT = '"
							+ product + "' and COURSE_START_DATE = to_date('"
							+ startDate + "','mm-dd-yyyy') ");
			/* sanjeev end delete mapping */
			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);

			// Query q =
			// session.createSQLQuery(q1).addEntity(CourseEvalTemplateMapping.class);
			ts = session.beginTransaction();
			int result = q.executeUpdate();
			ts.commit();
			// System.out.println("deleted" + result);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public TrainerLearnerMapping[] getCountOfEvalsPerTrainer(String eventName,
			String courseStartDate) throws SQLException, SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		TrainerLearnerMapping[] countOfEvalsPerTrainer = null;
		StringBuilder queryString = new StringBuilder();

		List<TrainerLearnerMapping> countOfEvalsPerTrainerList = new ArrayList<TrainerLearnerMapping>();

		try {

			queryString
					.append("select count(trainer_email) as trainerCount,trainer_email as trainerEmail,")
					.append("trainer_name as trainerName from trainer_learners_mapping where EVENT_NAME ")
					.append("like :eventName AND TRUNC (course_start_date) = TO_DATE (:courseStartDate, 'yyyy-mm-dd') group by trainer_email,trainer_name ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("eventName", eventName);
			q.setParameter("courseStartDate", courseStartDate);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			TrainerLearnerMapping trainerLearnerMapping = null;

			while (it.hasNext()) {

				trainerLearnerMapping = new TrainerLearnerMapping();
				Object[] field = (Object[]) it.next();

				trainerLearnerMapping.setTrainerCount(((BigDecimal) field[0])
						.intValue());
				trainerLearnerMapping.setTrainerEmail((String) field[1]);
				trainerLearnerMapping.setTrainerName((String) field[2]);

				countOfEvalsPerTrainerList.add(trainerLearnerMapping);

			}
			countOfEvalsPerTrainer = countOfEvalsPerTrainerList
					.toArray(new TrainerLearnerMapping[countOfEvalsPerTrainerList
							.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return countOfEvalsPerTrainer;
	}

	public TrainerLearnerMapping[] getLearnerForMannual(String dates,
			String eventName) throws SQLException, SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		TrainerLearnerMapping[] learnerForMannual = null;
		StringBuilder queryString = new StringBuilder();
		String[] h = dates.split(" ");
		List<TrainerLearnerMapping> learnerForMannualList = new ArrayList<TrainerLearnerMapping>();

		try {

			/*
			 * queryString .append(
			 * "select t.FIRST_NAME as learnerName, t.LOCATION as learnerLoc ,")
			 * .append(
			 * "t.EMAIL_ADDRESS as learnerEmail from LEARNERS_COURSECOMPLETED t "
			 * ) .append("where t.EVENT_NAME LIKE '" + eventName +
			 * "' and  TRUNC(t.course_start_date) = to_date('" + h[0] +
			 * "','yyyy-mm-dd') order by t.FIRST_NAME asc");
			 */
			/*
			 * Sanjeev Code change for fetching full learnerName in manual map
			 * dropdown
			 */
			queryString
					.append("select CONCAT(CONCAT(Last_Name,','),First_Name) as learnerName, t.LOCATION as learnerLoc ,")
					.append("t.EMAIL_ADDRESS as learnerEmail from LEARNERS_COURSECOMPLETED t ")
					.append("where t.EVENT_NAME LIKE '" + eventName
							+ "' and  TRUNC(t.course_start_date) = to_date('"
							+ h[0]
							+ "','yyyy-mm-dd')  order by learnerName asc");

			/* Sanjeev end */
			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			// q.setParameter("eventName", eventName);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			TrainerLearnerMapping trainerLearnerMapping = null;

			while (it.hasNext()) {

				trainerLearnerMapping = new TrainerLearnerMapping();
				Object[] field = (Object[]) it.next();

				trainerLearnerMapping.setLearnerName((String) field[0]);
				trainerLearnerMapping.setLearnerLoc((String) field[1]);
				trainerLearnerMapping.setLearnerEmail((String) field[2]);

				learnerForMannualList.add(trainerLearnerMapping);

			}
			learnerForMannual = learnerForMannualList
					.toArray(new TrainerLearnerMapping[learnerForMannualList
							.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return learnerForMannual;
	}

	public TrainerLearnerMapping[] getTrainerForMannual(String produtSel,
			String eventName) throws SQLException, SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		TrainerLearnerMapping[] trainerForMannual = null;
		StringBuilder queryString = new StringBuilder();

		List<TrainerLearnerMapping> trainerForMannualList = new ArrayList<TrainerLearnerMapping>();

		try {

			/*
			 * queryString .append(
			 * "SELECT distinct g.rep_fname AS trainerName, g.rep_email AS trainerEmail, "
			 * ) .append("g.rep_location AS trainerLoc  FROM guest_trainer g ")
			 * .append("JOIN guest_trainer_event t ON g.REP_EMAIL")
			 * .append("= t.REP_EMAIL WHERE g.rep_associated_product = '" +
			 * produtSel + "'")
			 * .append("and t.REP_ISACCEPTED = 'Y' and t.EVENT_NAME = '" +
			 * eventName + "'") .append("order by g.rep_fname asc");
			 */

			/*******
			 * Sanjeev code change for fetchin trainer full name in manual mapp
			 * dropdown
			 **********/
			queryString
					.append("SELECT distinct CONCAT(CONCAT(REP_LNAME,','),REP_FNAME) AS trainerName, g.rep_email AS trainerEmail, ")
					.append("g.rep_location AS trainerLoc  FROM guest_trainer g ")
					.append("JOIN guest_trainer_event t ON g.REP_EMAIL")
					.append("= t.REP_EMAIL WHERE g.rep_associated_product = '"
							+ produtSel + "'")
					.append("and t.REP_ISACCEPTED = 'Y' and t.EVENT_NAME = '"
							+ eventName + "'")
					.append("order by trainerName asc");
			/*********** Sanjeev end *************/
			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			// q.setParameter("eventName", eventName);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			TrainerLearnerMapping template = null;

			while (it.hasNext()) {

				template = new TrainerLearnerMapping();
				Object[] field = (Object[]) it.next();

				template.setTrainerName((String) field[0]);
				template.setTrainerEmail((String) field[1]);
				template.setTrainerLoc((String) field[2]);

				trainerForMannualList.add(template);

			}
			trainerForMannual = trainerForMannualList
					.toArray(new TrainerLearnerMapping[trainerForMannualList
							.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return trainerForMannual;
	}

	public TrainerLearnerMapping[] getNoOfMailSentByEventTrainer(
			String eventName, String trainerEmail, String courseStartDate)
			throws SQLException, SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		TrainerLearnerMapping[] mailSentByEventTrainer = null;
		StringBuilder queryString = new StringBuilder();
		List<TrainerLearnerMapping> eventList = new ArrayList<TrainerLearnerMapping>();

		try {
			// **** BEGIN changes done by Sanjeev to remove date format issue
			// ****//
			/*
			 * queryString .append(
			 * "select count(learner_email) as trainerCount from trainer_learners_mapping where EVENT_NAME like :eventName and "
			 * ) .append(
			 * "TRAINER_EMAIL like :trainerEmail and COURSE_START_DATE = to_date(:courseStartDate,'yyyy-mm-dd') and EMAIL_SENT ='Y' "
			 * );
			 */

			queryString
					.append("select count(learner_email) as trainerCount from trainer_learners_mapping where EVENT_NAME like :eventName and ")
					.append("TRAINER_EMAIL like :trainerEmail and COURSE_START_DATE = to_date(:courseStartDate,'mm-dd-yyyy') and EMAIL_SENT ='Y' ");

			// ****END changes done by Sanjeev to remove date format issue
			// ****//

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("eventName", eventName);
			q.setParameter("trainerEmail", trainerEmail);
			q.setParameter("courseStartDate", courseStartDate);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			TrainerLearnerMapping template = null;

			while (it.hasNext()) {

				template = new TrainerLearnerMapping();

				template.setTrainerCount(((BigDecimal) it.next()).intValue());

				eventList.add(template);

			}
			mailSentByEventTrainer = eventList
					.toArray(new TrainerLearnerMapping[eventList.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return mailSentByEventTrainer;
	}

	public TrainerLearnerMapping[] getTrainerLocForMannual(String produtSel,
			String eventName) throws SQLException, SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		TrainerLearnerMapping[] trainerLocForMannual = null;
		StringBuilder queryString = new StringBuilder();

		List<TrainerLearnerMapping> trainerLocForMannualList = new ArrayList<TrainerLearnerMapping>();

		try {

			queryString
					.append("SELECT distinct  g.rep_location AS trainerLoc ")
					.append("FROM guest_trainer g JOIN guest_trainer_event t ")
					.append("ON g.REP_EMAIL = t.REP_EMAIL WHERE g.rep_associated_product = '"
							+ produtSel + "' ")
					.append("and t.REP_ISACCEPTED = 'Y' and t.EVENT_NAME = '"
							+ eventName + "'");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			// q.setParameter("eventName", eventName);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			TrainerLearnerMapping template = null;

			while (it.hasNext()) {

				template = new TrainerLearnerMapping();
				Object[] field = (Object[]) it.next();

				template.setTrainerLoc((String) field[0]);

				trainerLocForMannualList.add(template);

			}
			trainerLocForMannual = trainerLocForMannualList
					.toArray(new TrainerLearnerMapping[trainerLocForMannualList
							.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return trainerLocForMannual;
	}

	public void gotoSaveEventMapping(String selCourseName, String selTrainer,
			String selLearner, String trainerEmail, String learnerEmail,
			String whichEventName, String learnerLOC, String trainerLOC,
			String dates) throws SQLException, SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		// trainerLearnerMapping[] trainLearnArr = null;
		StringBuilder queryString = new StringBuilder();

		// List<trainerLearnerMapping> trainLearnList = new
		// ArrayList<trainerLearnerMapping>();
		String course = selCourseName;
		String t = selTrainer;
		String tEmail = trainerEmail;
		String l = selLearner;
		String lEmail = learnerEmail;
		String eventName = whichEventName;
		String learnerLoc = learnerLOC;
		String trainerLoc = trainerLOC;
		Transaction ts = null;
		String[] x = dates.split(" ");

		try {

			queryString.append("insert into TRAINER_LEARNERS_MAPPING ").append(
					"values (SEQ_MAPPING_LEARNER_TRAINER_ID.nextval," + "'" + l
							+ "'" + "," + "'" + t + "'" + "," + "'" + lEmail
							+ "'" + "," + "'" + tEmail + "'" + "," + "'"
							+ course + "'" + "," + "'" + eventName + "'" + ","
							+ "'" + learnerLoc + "'" + "," + "'" + trainerLoc
							+ "', to_date('" + x[0] + "','yyyy-mm-dd'),'N') ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			// q.setParameter("eventName", eventName);

			ts = session.beginTransaction();
			int result = q.executeUpdate();
			ts.commit();

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public String[] getLocationForTimeZone(String timeZone)
			throws SQLException, SCEException {

		Session session = HibernateUtils.getHibernateSession();
		String[] locationForTimeZone = null;
		// Transaction ts=null;
		StringBuilder queryString = new StringBuilder();

		List<String> locationForTimeZoneList = new ArrayList<String>();

		try {

			queryString
					.append("select LOC_CODE from EVENT_TIME_ZONE where TIME_ZONE LIKE :timeZone ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("timeZone", timeZone);

			List list = q.list();

			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			locationForTimeZoneList.addAll(list);

			locationForTimeZone = locationForTimeZoneList
					.toArray(new String[locationForTimeZoneList.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return locationForTimeZone;

	}

	public Learner[] getLearnerForAuto(String dates, String eventName)
			throws SQLException, SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		Learner[] LearnArr = null;
		StringBuilder queryString = new StringBuilder();
		String[] h = dates.split(" ");
		List<Learner> LearnList = new ArrayList<Learner>();

		try {
			queryString
					.append("select t.FIRST_NAME as firstName, t.LOCATION as location "
							+ ",t.EMAIL_ADDRESS as emailAddress from LEARNERS_COURSECOMPLETED t  "
							+ "where t.EVENT_NAME LIKE '"
							+ eventName
							+ "' "
							+ "and TRUNC(t.course_start_date) = to_date('"
							+ h[0]
							+ "','yyyy-mm-dd') "
							+ "MINUS "
							+ "(SELECT learner_name AS learnerName, learner_loc AS learnerLoc, "
							+ "learner_email AS learnerEmail "
							+ "FROM trainer_learners_mapping "
							+ "WHERE event_name LIKE '"
							+ eventName
							+ "' "
							+ "AND TRUNC (course_start_date) = TO_DATE ('"
							+ h[0] + "', 'yyyy-mm-dd'))");
			/*
			 * queryString
			 * .append("select t.FIRST_NAME as firstName, t.LOCATION as location , "
			 * ) .append(
			 * "t.EMAIL_ADDRESS as emailAddress from LEARNERS_COURSECOMPLETED t "
			 * ) .append("where t.EVENT_NAME LIKE '" + eventName + "' and ")
			 * .append("TRUNC(t.course_start_date) = to_date('" + h[0] +
			 * "','yyyy-mm-dd') ");
			 */

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			// q.setParameter("eventName", eventName);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			Learner learner = null;

			while (it.hasNext()) {

				learner = new Learner();
				Object[] field = (Object[]) it.next();

				learner.setFirstName((String) field[0]);
				learner.setLocation((String) field[1]);
				learner.setEmailAddress((String) field[2]);

				LearnList.add(learner);

			}
			LearnArr = LearnList.toArray(new Learner[LearnList.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return LearnArr;
	}

	public TrainerLearnerMapping[] getAlreadyMappedTrainer(String eventName,
			String courseStartDate) throws SQLException, SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		TrainerLearnerMapping[] mappedTrainArr = null;
		StringBuilder queryString = new StringBuilder();
		List<TrainerLearnerMapping> mappedTrainList = new ArrayList<TrainerLearnerMapping>();

		try {

			queryString
					.append("select count(trainer_email) as trainerCount,trainer_email as trainerEmail ")
					.append("from trainer_learners_mapping where EVENT_NAME ")
					.append(" like :eventName AND TRUNC (course_start_date) = TO_DATE (:courseStartDate, 'yyyy-mm-dd') group by trainer_email ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("eventName", eventName);
			q.setParameter("courseStartDate", courseStartDate);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			TrainerLearnerMapping trainerLearnerMapping = null;

			while (it.hasNext()) {

				trainerLearnerMapping = new TrainerLearnerMapping();
				Object[] field = (Object[]) it.next();

				trainerLearnerMapping.setTrainerCount(((BigDecimal) field[0])
						.intValue());
				trainerLearnerMapping.setTrainerEmail((String) field[1]);

				mappedTrainList.add(trainerLearnerMapping);

			}
			mappedTrainArr = mappedTrainList
					.toArray(new TrainerLearnerMapping[mappedTrainList.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return mappedTrainArr;
	}

	public GuestTrainer[] getTrainerForAuto(String produtSel, String eventName,
			String courseStartDate) throws SQLException, SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		GuestTrainer[] trainerForAuto = null;
		StringBuilder queryString = new StringBuilder();
		List<GuestTrainer> TrainList = new ArrayList<GuestTrainer>();

		/*
		 * try {
		 * 
		 * queryString
		 * .append(" SELECT distinct g.rep_fname AS fname,g.rep_lname AS lname,"
		 * ) .append("g.rep_email AS repemail,  g.rep_location AS replocation ")
		 * .
		 * append("FROM guest_trainer g JOIN guest_trainer_event t ON g.REP_EMAIL "
		 * ) .append("= t.REP_EMAIL WHERE g.rep_associated_product = '" +
		 * produtSel + "' ") .append(" AND t.Product ='" + produtSel + "' ") //
		 * Added by // ankit on // 9/11 for // Product // Based // filter on //
		 * accpeted // GTs.
		 * .append(" and t.REP_ISACCEPTED = 'Y' and t.EVENT_NAME = '" +
		 * eventName + "' ");
		 */

		// *****************************************************************************
		// work in progress
		// added by manish on 2/15/2016
		try {

			queryString
					.append("SELECT DISTINCT t.rep_fname AS fname,t.rep_lname AS lname,b.REP_EMAIL,a.TOTAL_HOURS,c.EVAL_DURATION,t.rep_location AS replocation ")
					.append("FROM TRAINER_EVENT_DATETIME_SLOTS a JOIN guest_trainer_event b ")
					.append("ON a.MAP_ID = b.MAP_ID JOIN guest_trainer t ON b.REP_EMAIL = t.REP_EMAIL ")
					.append("JOIN v_event c ON c.EVENT_NAME = b.EVENT_NAME WHERE t.rep_associated_product = '"
							+ produtSel + "' ")
					.append(" AND b.Product ='" + produtSel + "' ")
					.append(" and b.REP_ISACCEPTED = 'Y' and b.EVENT_NAME = '"
							+ eventName + "' ")
					.append(" AND a.DATE_SEL = TO_DATE('" + courseStartDate
							+ "','yyyy-mm-dd')+1");

			// *********************************************************************************

			String q1 = queryString.toString();

			System.out.println("Get Trainer for Auto:" + q1);
			Query q = session.createSQLQuery(q1);
			// q.setParameter("eventName", eventName);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			GuestTrainer template = null;

			while (it.hasNext()) {

				template = new GuestTrainer();
				Object[] field = (Object[]) it.next();

				template.setFname((String) field[0]);
				template.setLname((String) field[1]);
				template.setRepEmail((String) field[2]);
				template.setTotalHours((String) field[3]);
				template.setEvalDuration((String) field[4]);
				template.setRepLocation((String) field[5]);

				TrainList.add(template);

			}
			trainerForAuto = TrainList.toArray(new GuestTrainer[TrainList
					.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return trainerForAuto;
	}

	/*
	 * ------------- sanjeev begin trainer learner mapping
	 * issue------------------
	 */
	/*
	 * public TrainerLearnerMapping[] gotoCheck(String learnerEmail, String
	 * whichEventName) throws SQLException, SCEException
	 * 
	 * {
	 */

	public TrainerLearnerMapping[] gotoCheck(String learnerEmail,
			String whichEventName, String selCourseName,
			String selCourseDateName) throws SQLException, SCEException

	{
		/*
		 * ------------- sanjeev end trainer learner mapping
		 * issue------------------
		 */
		Session session = HibernateUtils.getHibernateSession();
		TrainerLearnerMapping[] gotoCheck = null;
		StringBuilder queryString = new StringBuilder();
		String lEmail = learnerEmail;
		String eventName = whichEventName;
		/* sanjeev */String courseStartDate = selCourseDateName.substring(5, 7)
				+ "/" + selCourseDateName.substring(8, 10) + "/"
				+ selCourseDateName.substring(0, 4);
		List<TrainerLearnerMapping> gotoCheckList = new ArrayList<TrainerLearnerMapping>();

		try {
			/*
			 * ------------- sanjeev begin trainer learner mapping(manual
			 * mapping) : allowing learner to be mapped for different products
			 * in the same event------------------
			 */
			/*
			 * queryString .append(
			 * "select LEARNER_EMAIL as learnerEmail  from TRAINER_LEARNERS_MAPPING "
			 * ) .append("where LEARNER_EMAIL like '" + lEmail +
			 * "' and EVENT_NAME like '" + eventName + "' ");
			 */

			/* 2013-12-10 */
			queryString
					.append("select LEARNER_EMAIL as learnerEmail  from TRAINER_LEARNERS_MAPPING ")
					.append("where LEARNER_EMAIL like '" + lEmail
							+ "' and EVENT_NAME like '" + eventName
							+ "' and PRODUCT like '" + selCourseName
							+ "' and COURSE_START_DATE = to_date('"
							+ courseStartDate + "','mm-dd-yyyy')");

			System.out.println("course_start_date" + selCourseDateName);
			System.out.println("courseStartDate" + courseStartDate);
			/*
			 * ------------- sanjeev end trainer learner mapping
			 * issue------------------
			 */

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			// q.setParameter("eventName", eventName);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			TrainerLearnerMapping template = null;

			while (it.hasNext()) {

				template = new TrainerLearnerMapping();
				String field = (String) it.next();

				template.setLearnerEmail(field);

				gotoCheckList.add(template);

			}
			gotoCheck = gotoCheckList
					.toArray(new TrainerLearnerMapping[gotoCheckList.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return gotoCheck;
	}

	public TrainerLearnerMapping[] getNoOfMailSentByEventTrainer(
			String eventName, String trainerEmail) throws SQLException,
			SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		TrainerLearnerMapping[] mailSentByEventTrainer = null;
		StringBuilder queryString = new StringBuilder();
		List<TrainerLearnerMapping> eventList = new ArrayList<TrainerLearnerMapping>();

		try {

			queryString
					.append("select count(learner_email) as trainerCount from trainer_learners_mapping where EVENT_NAME like :eventName and ")
					.append("TRAINER_EMAIL like :trainerEmail and EMAIL_SENT ='Y' ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("eventName", eventName);
			q.setParameter("trainerEmail", trainerEmail);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			TrainerLearnerMapping template = null;

			while (it.hasNext()) {

				template = new TrainerLearnerMapping();

				template.setTrainerCount(((BigDecimal) it.next()).intValue());

				eventList.add(template);

			}
			mailSentByEventTrainer = eventList
					.toArray(new TrainerLearnerMapping[eventList.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return mailSentByEventTrainer;
	}

	public void updateWebExDetailsByEventNotUsed(String eventName, String usedby)
			throws SCEException

	{
		// SCEControlImpl sceControl=new SCEControlImpl();
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();

		try {

			queryString
					.append("update webexdetails SET USEDBY_TRAINER =:usedby ")
					.append("where EVENT_NAME like :eventName and USEDBY_TRAINER is null ")
					.append("and ROWID IN (select max(ROWID) from webexdetails where event_name LIKE :eventName AND usedby_trainer IS NULL) ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("eventName", eventName);
			q.setParameter("usedby", usedby);

			// Query q =
			// session.createSQLQuery(q1).addEntity(CourseEvalTemplateMapping.class);
			ts = session.beginTransaction();
			int result = q.executeUpdate();
			ts.commit();
			// System.out.println("result" + result);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public GuestTrainer[] getGTByEmail(String email) throws SQLException,
			SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		GuestTrainer[] gtByEmail = null;
		StringBuilder queryString = new StringBuilder();
		List<GuestTrainer> gtByEmailList = new ArrayList<GuestTrainer>();

		try {

			queryString
					.append(" SELECT REP_FNAME as fname,REP_LNAME as lname,")
					.append("Rep_Email as repEmail,REP_LOCATION as repLocation,")
					.append("REP_ASSOCIATED_PRODUCT as associatedProduct ")
					.append("from sales_call.GUEST_TRAINER where Rep_Email like :email ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("email", email);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			GuestTrainer guestTrainer = null;

			while (it.hasNext()) {

				guestTrainer = new GuestTrainer();
				Object[] field = (Object[]) it.next();

				guestTrainer.setFname((String) field[0]);
				guestTrainer.setLname((String) field[1]);
				guestTrainer.setRepEmail((String) field[2]);
				guestTrainer.setRepLocation((String) field[3]);
				guestTrainer.setAssociatedProduct((String) field[4]);
				gtByEmailList.add(guestTrainer);

			}
			gtByEmail = gtByEmailList.toArray(new GuestTrainer[gtByEmailList
					.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return gtByEmail;
	}

	public String getTimeZone(String location) throws SCEException

	{
		// SCEControlImpl sceControl=new SCEControlImpl();
		Session session = HibernateUtils.getHibernateSession();
		String timeZone = null;
		// Transaction ts=null;
		StringBuilder queryString = new StringBuilder();

		List<String> timeZoneList = new ArrayList<String>();

		try {

			queryString
					.append("select TIME_ZONE from event_time_zone where LOC_CODE  LIKE :location ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("location", location);
			List list = q.list();

			// System.out.println("size " + list.size());

			timeZoneList.addAll(list);

			timeZone = timeZoneList.toString();

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return timeZone;
	}

	public Learner[] getLearnerByEmail(String email) throws SQLException,
			SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		Learner[] learnerByEmail = null;
		StringBuilder queryString = new StringBuilder();
		List<Learner> learnerByEmailList = new ArrayList<Learner>();

		try {

			queryString
					.append("select FIRST_NAME as firstName,LAST_NAME as lastName,")
					.append("email_Address as emailAddress,location as location from ")
					.append("learners_coursecompleted where email_address like :email ")
					.append("group by first_name,last_name,email_address,location ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("email", email);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			Learner learner = null;

			while (it.hasNext()) {

				learner = new Learner();
				Object[] field = (Object[]) it.next();

				learner.setFirstName((String) field[0]);
				learner.setLastName((String) field[1]);
				learner.setEmailAddress((String) field[2]);
				learner.setLocation((String) field[3]);

				learnerByEmailList.add(learner);

			}
			learnerByEmail = learnerByEmailList
					.toArray(new Learner[learnerByEmailList.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return learnerByEmail;
	}

	public void gotoConfirmEmailSent(String query) {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		Transaction ts;

		try {
			// System.out.println("quer1" + query);

			// Query q = session.createQuery(queryString);
			Query q = session.createSQLQuery(query);

			ts = session.beginTransaction();
			int result = q.executeUpdate();
			ts.commit();
			// System.out.println("result" + result);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void addNewGT(GuestTrainer guestTrainerObj) throws SQLException,
			SCEException {
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts;
		StringBuilder queryString = new StringBuilder();
		// List<Learner> TrainList = new ArrayList<Learner>();
		/*
		 * Sanjeev begin code change added if else block for null manager
		 * details-- code change done in if block and else block not changed
		 */
		try {

			if (guestTrainerObj.getRepManager() == null
					&& guestTrainerObj.getRepManagerRole() == null
					&& guestTrainerObj.getMgrEmail() == null) {

				queryString
						.append("insert into guest_trainer(rep_id,rep_email, ")
						.append("rep_location,rep_role,rep_manager,rep_manager_role, ")
						.append("rep_associated_product,rep_isselected, rep_eventhistory, ")
						.append("rep_fname,rep_lname,rep_ntid,rep_manager_email) ")
						.append("values ")
						.append("( ")
						.append("SQ_GUEST_TRAINER.NEXTVAL,")
						.append("'" + guestTrainerObj.getRepEmail() + "',")
						.append("'" + guestTrainerObj.getRepLocation() + "',")
						.append("'" + guestTrainerObj.getRepRole() + "',")
						.append("null,")
						.append("null,")
						.append("'" + guestTrainerObj.getAssociatedProduct()
								+ "',")
						.append("'" + guestTrainerObj.getIsSelected() + "',")
						.append("'" + guestTrainerObj.getEventHistory() + "',")
						.append("'" + guestTrainerObj.getFname() + "',")
						.append("'" + guestTrainerObj.getLname() + "',")
						.append("'" + guestTrainerObj.getNtid() + "',")
						.append("null) ");

			} else {
				queryString
						.append("insert into guest_trainer(rep_id,rep_email, ")
						.append("rep_location,rep_role,rep_manager,rep_manager_role, ")
						.append("rep_associated_product,rep_isselected, rep_eventhistory, ")
						.append("rep_fname,rep_lname,rep_ntid,rep_manager_email) ")
						.append("values ")
						.append("( ")
						.append("SQ_GUEST_TRAINER.NEXTVAL,")
						.append("'" + guestTrainerObj.getRepEmail() + "',")
						.append("'" + guestTrainerObj.getRepLocation() + "',")
						.append("'" + guestTrainerObj.getRepRole() + "',")
						.append("'" + guestTrainerObj.getRepManager() + "',")
						.append("'" + guestTrainerObj.getRepManagerRole()
								+ "',")
						.append("'" + guestTrainerObj.getAssociatedProduct()
								+ "',")
						.append("'" + guestTrainerObj.getIsSelected() + "',")
						.append("'" + guestTrainerObj.getEventHistory() + "',")
						.append("'" + guestTrainerObj.getFname() + "',")
						.append("'" + guestTrainerObj.getLname() + "',")
						.append("'" + guestTrainerObj.getNtid() + "',")
						.append("'" + guestTrainerObj.getMgrEmail() + "') ");
			}
			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);

			ts = session.beginTransaction();
			int result = q.executeUpdate();
			ts.commit();
			// System.out.println("result" + result);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void addNewGTforMapping(GuestTrainer guestTrainerObj)
			throws SQLException, SCEException {
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts;
		StringBuilder queryString = new StringBuilder();
		// List<Learner> TrainList = new ArrayList<Learner>();

		try {

			queryString
					.append("insert into guest_trainer_event ")
					.append("(map_id,rep_email,REP_EMAIL_ISSENT) ")
					.append("values(trainer_event_mapping_id.NEXTVAL,'"
							+ guestTrainerObj.getRepEmail() + "','N') ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);

			ts = session.beginTransaction();
			int result = q.executeUpdate();
			ts.commit();
			// System.out.println("result" + result);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public GuestTrainer[] getGTByProduct(String product) throws SQLException,
			SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		GuestTrainer[] gTByProduct = null;
		StringBuilder queryString = new StringBuilder();
		List<GuestTrainer> gTByProductList = new ArrayList<GuestTrainer>();

		try {

			queryString
					.append("SELECT REP_FNAME as fname,REP_LNAME as lname,")
					.append("REP_NTID as ntid,REP_MANAGER_EMAIL as mgrEmail,Rep_Email as repEmail,")
					.append("Rep_Location as repLocation,rep_role as repRole,rep_associated_product as associatedProduct,")
					.append("rep_manager as repManager ,rep_manager_role as repManagerRole,REP_ISSELECTED as isSelected ")
					.append("from sales_call.GUEST_TRAINER where rep_associated_product like :product ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("product", product);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			GuestTrainer guestTrainer = null;

			while (it.hasNext()) {

				guestTrainer = new GuestTrainer();
				Object[] field = (Object[]) it.next();

				guestTrainer.setFname((String) field[0]);
				;
				guestTrainer.setLname((String) field[1]);
				guestTrainer.setNtid((String) field[2]);
				guestTrainer.setMgrEmail((String) field[3]);
				guestTrainer.setRepEmail((String) field[4]);
				if (field[5] != null)
					guestTrainer.setRepLocation((String) field[5]);
				guestTrainer.setRepRole((String) field[6]);
				guestTrainer.setAssociatedProduct((String) field[7]);
				guestTrainer.setRepManager((String) field[8]);
				guestTrainer.setRepManagerRole((String) field[9]);
				guestTrainer
						.setIsSelected(field[10] != null ? ((Character) field[10])
								.toString() : null);

				gTByProductList.add(guestTrainer);

			}

			gTByProduct = gTByProductList
					.toArray(new GuestTrainer[gTByProductList.size()]);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return gTByProduct;
	}

	public GuestTrainer getGTFromAtlasByEmailProduct(String email,
			String product) throws SQLException, SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		GuestTrainer guestTrainerArr = null;
		StringBuilder queryString = new StringBuilder();
		// List<GuestTrainer> TrainList = new ArrayList<GuestTrainer>();

		try {

			queryString
					.append("SELECT FIRST_NAME as fname,LAST_NAME as lname,")
					.append(" NTID as ntid,MGR_EMAIL_ADDRESS as mgrEmail,EMAIL_ADDRESS as repEmail,")
					.append("HOME_STATE as repLocation,JOB_TITLE as repRole,PRODUCT_DESC as associatedProduct ,")
					.append("MGR_FULL_NAME as repManager,MGR_JOB_TITLE as repManagerRole from sales_call.MV_ATLAS_GUEST_TRAINER ")
					.append("where EMAIL_ADDRESS like :email and PRODUCT_DESC like :product ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("product", product);
			q.setParameter("email", email);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			GuestTrainer template = null;

			while (it.hasNext()) {

				template = new GuestTrainer();
				Object[] field = (Object[]) it.next();

				template.setFname((String) field[0]);
				;
				template.setLname((String) field[1]);
				template.setNtid((String) field[2]);
				template.setMgrEmail((String) field[3]);
				template.setRepEmail((String) field[4]);
				if (field[5] != null)
					template.setRepLocation((String) field[5]);
				template.setRepRole((String) field[6]);
				template.setAssociatedProduct((String) field[7]);
				template.setRepManager((String) field[8]);
				template.setRepManagerRole((String) field[9]);

				guestTrainerArr = template;

			}

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return guestTrainerArr;
	}

	public void removeGT(String email, String product) throws SCEException {
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts;
		StringBuilder queryString = new StringBuilder();
		// List<Learner> TrainList = new ArrayList<Learner>();

		try {

			queryString.append(
					"delete from Guest_Trainer where rep_email like :email ")
					.append("and REP_ASSOCIATED_PRODUCT like :product ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);

			q.setParameter("email", email);
			q.setParameter("product", product);

			ts = session.beginTransaction();
			int result = q.executeUpdate();
			ts.commit();
			// System.out.println("deleted" + result);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		}

	}

	/*
	 * //Introduced by Arpit as part of SCE Migration on 5th August 2014
	 */
	public EmailTemplate[] getevaluationTemplates() {
		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();
		List<EmailTemplate> list = new ArrayList<EmailTemplate>();
		EmailTemplate[] temp = null;
		try {

			queryString
					.append("select distinct template_id as evaluationTemplateId,form_title as evaluationTemplateName ")
					.append("from template_version where publish_flag='Y' order by form_title asc ");

			String q1 = queryString.toString();
			// System.out.println("query formed is " + q1);
			Query q = session.createSQLQuery(q1);
			List<EmailTemplate> newList = new ArrayList<EmailTemplate>();
			list = q.list();
			// System.out.println("List fetched from database is" + list);
			Iterator it = list.iterator();
			EmailTemplate empt = null;
			while (it.hasNext()) {
				empt = new EmailTemplate();
				Object[] field = (Object[]) it.next();
				empt.setEvaluationTemplateId(((BigDecimal) field[0]).intValue());
				empt.setEvaluationTemplateName((String) field[1]);
				newList.add(empt);
			}

			// temp=(EmailTemplate[])newList.toArray();
			temp = newList.toArray(new EmailTemplate[newList.size()]);
			// System.out.println("value of temp is " + temp);

		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			// // System.out.println();
			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			// System.out.println("getUserByNTIdAndDomain Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return temp;
	}

	public String[] retrieveScoringOptions(Integer evaluationTemplateId)
			throws SQLException {
		// System.out.println("inside retrieveScoringOptions method");
		// System.out.println("value of evaluationTemplateId"+
		// evaluationTemplateId);
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List<String> ls = new ArrayList<String>();
		String[] temp = null;
		try {
			queryString
					.append("select distinct(scoring_system_identifier) as scoringSystemIdentifier from template_version ")
					.append("where template_id = :evaluationTemplateId and (scoring_system_identifier is not null or scoring_system_identifier='')");

			String q1 = queryString.toString();
			// System.out.println("query formed is " + q1);
			Query q = session.createSQLQuery(q1);
			q.setParameter("evaluationTemplateId", evaluationTemplateId);

			ls = q.list();
			// System.out.println("list fetched is" + ls);

			List<String> newLs = new ArrayList<String>();

			Iterator it = ls.iterator();
			// String s = null;
			while (it.hasNext()) {
				// s = new String();
				// System.out.println("inside iterator");
				// Object[] field = (Object[]) it.next();
				newLs.add((String) it.next());
			}
			// System.out.println("value of new list is" + newLs);
			temp = (String[]) newLs.toArray(new String[newLs.size()]);
		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			// System.out.println("Exception in retrieveScoringOptions method");
			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			// System.out.println("getUserByNTIdAndDomain Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return temp;
	}

	/*
	 * public String[] getScoreValues(String scoringSystemIdentifier) throws
	 * SQLException { // System.out.println("Inside getScoreValues method");
	 * Session session = HibernateUtils.getHibernateSession(); StringBuilder
	 * queryString = new StringBuilder(); List<String> ls = new
	 * ArrayList<String>(); String[] temp= null; try { queryString
	 * .append("select score_value from scoring_system ")
	 * .append(" where scoring_system_identifier=:scoringSystemIdentifier ");
	 * 
	 * String q1 = queryString.toString(); //
	 * System.out.println("query formed is "+ q1); Query q =
	 * session.createSQLQuery(q1); q.setParameter("scoringSystemIdentifier",
	 * scoringSystemIdentifier); ls = q.list(); //
	 * System.out.println("List fetched for the second time is "+ ls);
	 * List<String> newLs = new ArrayList<String>();
	 * 
	 * Iterator it = ls.iterator(); //String s = null; while(it.hasNext()) { //s
	 * = new String(); //Object[] field = (Object[]) it.next();
	 * newLs.add((String)it.next()); } //
	 * System.out.println("value of second new list "+ newLs); temp =
	 * (String[])newLs.toArray(new String[newLs.size()]); } catch
	 * (HibernateException e) { //
	 * getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY, //
	 * "getDistributionListFilters --> HibernateException : ", e); //
	 * System.out.println(); e.printStackTrace();
	 * 
	 * // log.error("HibernateException in getUserByNTIdAndDomain", e); //
	 * System.out.println("getUserByNTIdAndDomain Hibernatate Exception"); }
	 * finally { HibernateUtils.closeHibernateSession(session); }
	 * 
	 * return temp; }
	 */

	/*
	 * public EmailTemplate[] getAllEmailTemplates(Integer
	 * evaluationTemplateId,String scoringOption) throws SQLException,
	 * SCEException { Session session = HibernateUtils.getHibernateSession();
	 * StringBuilder queryString = new StringBuilder(); List<EmailTemplate> list
	 * = new ArrayList<EmailTemplate>(); EmailTemplate[] temp =null; try {
	 * 
	 * queryString .append(
	 * "select et.email_Template_Id as emailTemplateId,et.evaluation_template_name as evaluationTemplateName, "
	 * ) .append(
	 * "et.scoring_system_identifier as scoringSystemIdentifier, et.email_Template_Version as emailTemplateVersion,"
	 * ) .append(
	 * "et.overall_score as overallScore, et.template_version_id as evaluationTemplateVersionId,"
	 * )
	 * .append("et.publish_flag as publishFlag,et.email_subject as emailSubject,"
	 * ) .append(
	 * "et.email_body as emailBody from email_template et,template temp,scoring_system s "
	 * ) .append(
	 * "where temp.template_id = :evaluationTemplateId and et.evaluation_template_name = temp.name "
	 * ) .append(
	 * "and et.scoring_system_identifier = :scoringOption and et.scoring_system_identifier = s.scoring_system_identifier "
	 * ) .append(
	 * "and et.overall_score = s.score_value group by et.email_subject, et.email_body,et.evaluation_template_name,"
	 * ) .append(
	 * "et.scoring_system_identifier,et.publish_flag,et.email_Template_Id,et.template_version_id,et.overall_score,et.email_template_version,s.score_id order by s.score_id"
	 * );
	 * 
	 * 
	 * String q1 = queryString.toString(); //
	 * System.out.println("query formed is "+ q1); Query q =
	 * session.createSQLQuery(q1); q.setParameter("evaluationTemplateId",
	 * evaluationTemplateId); q.setParameter("scoringOption", scoringOption);
	 * 
	 * 
	 * List<EmailTemplate> newList = new ArrayList<EmailTemplate>(); list =
	 * q.list(); // System.out.println("List fetched from database is"+ list);
	 * Iterator it = list.iterator(); EmailTemplate empt = null;
	 * while(it.hasNext()) { // System.out.println("within Iterator"); empt= new
	 * EmailTemplate(); Object[] field = (Object[]) it.next();
	 * empt.setEmailTemplateId(((BigDecimal) field[0]).intValue());
	 * //empt.setEvaluationTemplateId(((BigDecimal) field[0]).intValue()); //
	 * System.out.println("first value set");
	 * empt.setEvaluationTemplateName((String) field[1]); //
	 * System.out.println("second value set");
	 * empt.setScoringSystemIdentifier((String) field[2] ); //
	 * System.out.println("Third value set");
	 * empt.setEmailTemplateVersion((String) field[3]);
	 * empt.setOverallScore((String) field[4]); //
	 * System.out.println("Forth value set");
	 * empt.setEvaluationTemplateVersionId(((BigDecimal) field[5]).intValue());
	 * // System.out.println("fifth value set"); Character publishFlag=
	 * (Character) field[6];
	 * empt.setPublishFlag((String)publishFlag.toString());
	 * empt.setEmailSubject((String) field[7]); //
	 * System.out.println("ten value set"); empt.setEmailBody((String)
	 * field[8]); // System.out.println("eleven value set"); //
	 * System.out.println("value set"); newList.add(empt); }
	 * //temp=(EmailTemplate[])newList.toArray(); temp=newList.toArray(new
	 * EmailTemplate[newList.size()]); //
	 * System.out.println("value of temp is "+ temp);
	 * 
	 * 
	 * }catch (Exception e) { //log.error(LoggerHelper.getStackTrace(e));
	 * e.printStackTrace(); throw new SCEException("error.sce.unknown",e); }
	 * 
	 * return temp;
	 * 
	 * }
	 */

	public String publishEmailTemplate(Integer emailTemplateId)
			throws SQLException, SCEException {
		Session session = HibernateUtils.getHibernateSession();
		final Integer emailTempId = emailTemplateId;
		try {
			session.beginTransaction();
			session.doWork(new Work() {
				public void execute(Connection connection) throws SQLException {

					CallableStatement statement = null;

					statement = (connection)
							.prepareCall("{call SP_PUBLISH_EMAIL_TEMPLATE(?,?)}");
					statement.setInt(1, emailTempId);
					// register the OUT parameter before calling the stored
					// procedure
					statement.registerOutParameter(2, java.sql.Types.VARCHAR);
					statement.executeUpdate();
					// read the OUT parameter now
					result = statement.getString(2);
					// System.out.println("Template Published Successfully::"+
					// result);

				}

			});

			session.getTransaction().commit();

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {

			session.close();

		}
		return result;
	}

	public EmailTemplateForm getSelectedEmailTemplate(Integer emailTemplateId)
			throws SQLException, SCEException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List<EmailTemplateForm> emailList = new ArrayList<EmailTemplateForm>();
		EmailTemplateForm empt = null;
		try {

			queryString
					.append("select evaluation_template_name as evaluationTemplateName,")
					.append("scoring_system_identifier as scoringSystemIdentifier,")
					.append("overall_Score as overallScore,")
					.append("template_version_id as evaluationTemplateVersionId,")
					.append("publish_flag as publishFlag,")
					.append("email_template_version as emailTemplateVersion,")
					.append("email_cc as emailCc,email_bcc as emailBCc,")
					.append("email_subject as emailSubject,email_body as emailBody ")
					.append("from email_template where email_template_id =:emailTemplateId ");

			String q1 = queryString.toString();
			// System.out.println("query formed is " + q1);
			Query q = session.createSQLQuery(q1);
			q.setParameter("emailTemplateId", emailTemplateId);

			List list = q.list();
			// System.out.println("List fetched from database is" + list);
			Iterator it = list.iterator();

			while (it.hasNext()) {
				// System.out.println("within Iterator");
				empt = new EmailTemplateForm();
				Object[] field = (Object[]) it.next();

				empt.setEvaluationTemplateName((String) field[0]);
				empt.setScoringSystemIdentifier((String) field[1]);
				empt.setOverallScore((String) field[2]);
				empt.setEvaluationTemplateVersionId(((BigDecimal) field[3])
						.intValue());
				Character publishFlag = (Character) field[4];
				empt.setPublishFlag((String) publishFlag.toString());
				empt.setEmailTemplateVersion((String) field[5]);
				empt.setEmailCc((String) field[6]);
				empt.setEmailBCc((String) field[7]);
				empt.setEmailSubject((String) field[8]);
				empt.setEmailBody((String) field[9]);

			}
			// temp=(EmailTemplate[])newList.toArray();

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return empt;

	}

	/*
	 * public EmailTemplateForm getlatestEmailTemplateVersion(String
	 * evaluationTemplateName,String scoringOption,String overallScore) throws
	 * SQLException, SCEException { Session session =
	 * HibernateUtils.getHibernateSession(); StringBuilder queryString = new
	 * StringBuilder(); List<EmailTemplateForm> emailList = new
	 * ArrayList<EmailTemplateForm>(); EmailTemplateForm empt = null; try {
	 * 
	 * queryString
	 * .append("select evaluation_template_name as evaluationTemplateName," )
	 * .append("scoring_system_identifier as scoringSystemIdentifier," )
	 * .append("overall_Score as overallScore," )
	 * .append("template_version_id as evaluationTemplateVersionId," )
	 * .append("email_template_id as emailTemplateId,publish_flag as publishFlag,"
	 * )
	 * .append("email_template_version as emailTemplateVersion,email_cc as emailCc,"
	 * ) .append("email_bcc as emailBCc,email_subject as emailSubject,")
	 * .append("email_body as emailBody from email_template ") .append(
	 * "where evaluation_template_name =:evaluationTemplateName and scoring_system_identifier =:scoringOption "
	 * ) .append("and overall_Score =:overallScore and publish_flag='N' ");
	 * 
	 * 
	 * 
	 * 
	 * String q1 = queryString.toString(); //
	 * System.out.println("query formed is "+ q1); Query q =
	 * session.createSQLQuery(q1); q.setParameter("evaluationTemplateName",
	 * evaluationTemplateName); q.setParameter("scoringOption", scoringOption);
	 * q.setParameter("overallScore", overallScore);
	 * 
	 * 
	 * 
	 * 
	 * 
	 * List list = q.list(); //
	 * System.out.println("List fetched from database is"+ list); Iterator it =
	 * list.iterator();
	 * 
	 * 
	 * while(it.hasNext()) { // System.out.println("within Iterator"); empt= new
	 * EmailTemplateForm(); Object[] field = (Object[]) it.next();
	 * 
	 * empt.setEvaluationTemplateName((String) field[0]);
	 * empt.setScoringSystemIdentifier((String) field[1] );
	 * empt.setOverallScore((String) field[2]);
	 * empt.setEvaluationTemplateVersionId(((BigDecimal) field[3]).intValue());
	 * empt.setEmailTemplateId(((BigDecimal) field[4]).intValue()); Character
	 * publishFlag= (Character) field[5];
	 * empt.setPublishFlag((String)publishFlag.toString());
	 * empt.setEmailTemplateVersion((String) field[6]); empt.setEmailCc((String)
	 * field[6]); empt.setEmailBCc((String) field[7]);
	 * empt.setEmailSubject((String) field[8]); empt.setEmailBody((String)
	 * field[9]);
	 * 
	 * 
	 * } //temp=(EmailTemplate[])newList.toArray();
	 * 
	 * 
	 * 
	 * 
	 * } catch (Exception e) { //log.error(LoggerHelper.getStackTrace(e));
	 * e.printStackTrace(); throw new SCEException("error.sce.unknown",e); }
	 * 
	 * return empt;
	 * 
	 * }
	 */

	/*
	 * public void insertEmailTemplate(EmailTemplate objEmailTemplate) throws
	 * SQLException, SCEException
	 * 
	 * {
	 * 
	 * Session session = HibernateUtils.getHibernateSession(); StringBuilder
	 * queryString = new StringBuilder(); Transaction ts=null;
	 * 
	 * 
	 * try{
	 * 
	 * queryString
	 * .append("insert into email_template(email_template_id,template_version_id, "
	 * ) .append(
	 * "email_template_version,evaluation_template_name,scoring_system_identifier, "
	 * ) .append("overall_score,email_cc,email_bcc,email_subject,email_body, ")
	 * .
	 * append("publish_flag) values('"+objEmailTemplate.getEmailTemplateId()+"', "
	 * ) .append("'"+objEmailTemplate.getEvaluationTemplateVersionId()+"', ")
	 * .append("'"+objEmailTemplate.getEmailTemplateVersion()+"',")
	 * .append("'"+objEmailTemplate.getEvaluationTemplateName()+"', ")
	 * .append("'"+objEmailTemplate.getScoringSystemIdentifier()+"', ")
	 * .append("'"
	 * +objEmailTemplate.getOverallScore()+"','"+objEmailTemplate.getEmailCc
	 * ()+"', ")
	 * .append("'"+objEmailTemplate.getEmailBCc()+"','"+objEmailTemplate
	 * .getEmailSubject()+"', ")
	 * .append("'"+objEmailTemplate.getEmailBody()+"', ")
	 * .append("'"+objEmailTemplate.getPublishFlag()+"') ");
	 * 
	 * 
	 * String q1 = queryString.toString();
	 * 
	 * // System.out.println("quer2" + queryString); Query
	 * q=session.createSQLQuery(q1); //q.setParameter("eventName", eventName);
	 * 
	 * 
	 * ts=session.beginTransaction(); int result= q.executeUpdate();
	 * ts.commit();
	 * 
	 * 
	 * } catch (Exception e) { //log.error(LoggerHelper.getStackTrace(e));
	 * e.printStackTrace(); throw new SCEException("error.sce.unknown",e); }
	 * 
	 * }
	 */

	/*
	 * public void updateEmailTemplate(EmailTemplate objEmailTemplate) throws
	 * SQLException, SCEException
	 * 
	 * {
	 * 
	 * Session session = HibernateUtils.getHibernateSession(); StringBuilder
	 * queryString = new StringBuilder(); Transaction ts=null;
	 * 
	 * 
	 * try{
	 * 
	 * queryString .append("update email_template set ")
	 * .append("email_cc = '"+objEmailTemplate.getEmailCc()+"',")
	 * .append("email_bcc = '"+objEmailTemplate.getEmailBCc()+"',")
	 * .append("email_subject = '"+objEmailTemplate.getEmailSubject()+"',")
	 * .append("email_body = '"+objEmailTemplate.getEmailBody()+"' ")
	 * .append("where email_template_id = '"
	 * +objEmailTemplate.getEmailTemplateId()+"' ");
	 * 
	 * 
	 * String q1 = queryString.toString();
	 * 
	 * // System.out.println("quer2" + queryString); Query
	 * q=session.createSQLQuery(q1);
	 * 
	 * 
	 * 
	 * ts=session.beginTransaction(); int result= q.executeUpdate();
	 * ts.commit();
	 * 
	 * 
	 * } catch (Exception e) { //log.error(LoggerHelper.getStackTrace(e));
	 * e.printStackTrace(); throw new SCEException("error.sce.unknown",e); }
	 * 
	 * }
	 * 
	 * 
	 * ;
	 */

	/*
	 * public Integer getNextValEmailTemplateId() throws SQLException,
	 * HibernateException, SCEException { Session session =
	 * HibernateUtils.getHibernateSession(); Integer res = null; StringBuilder
	 * queryString = new StringBuilder(); try {
	 * 
	 * 
	 * String q1 = "select sq_email_template.nextval from dual";
	 * 
	 * // System.out.println("quer1" + queryString);
	 * 
	 * Query q = session.createSQLQuery(q1); List list = q.list();
	 * 
	 * // System.out.println("size " + list.size());
	 * 
	 * res = ((BigDecimal) list.get(0)).intValue();
	 * 
	 * } catch (Exception e) { //log.error(LoggerHelper.getStackTrace(e));
	 * e.printStackTrace(); throw new SCEException("error.sce.unknown",e); }
	 * 
	 * return res; }
	 */

	/*
	 * public void deleteEmailTemplate(Integer emailTemplateId) throws
	 * SQLException, SCEException { //SCEControlImpl sceControl=new
	 * SCEControlImpl(); Session session = HibernateUtils.getHibernateSession();
	 * CourseEvalTemplateMapping[] mappingsForTemplates = null; Transaction
	 * ts=null; StringBuilder queryString = new StringBuilder();
	 * 
	 * List<CourseEvalTemplateMapping> attendeeList = new
	 * ArrayList<CourseEvalTemplateMapping>();
	 * 
	 * try{
	 * 
	 * queryString .append("delete from email_template ")
	 * .append("where email_template_id =:emailTemplateId ")
	 * .append("and email_template_version = 'Draft' ");
	 * 
	 * 
	 * String q1 = queryString.toString();
	 * 
	 * // System.out.println("quer2" + queryString); Query
	 * q=session.createSQLQuery(q1); q.setParameter("emailTemplateId",
	 * emailTemplateId);
	 * 
	 * 
	 * //Query q =
	 * session.createSQLQuery(q1).addEntity(CourseEvalTemplateMapping.class);
	 * ts=session.beginTransaction(); int result= q.executeUpdate();
	 * ts.commit(); // System.out.println("deleted"+result);
	 * 
	 * } catch (Exception e) { //log.error(LoggerHelper.getStackTrace(e));
	 * e.printStackTrace(); throw new SCEException("error.sce.unknown",e); }
	 * 
	 * }
	 */

	// Apoorva end

	public Integer isPhaseTraining(Integer eventId) throws SQLException,
			SCEException {
		Session session = HibernateUtils.getHibernateSession();

		Integer result = 0;
		StringBuilder queryString = new StringBuilder();
		// List<Learner> TrainList = new ArrayList<Learner>();

		try {

			queryString
					.append("select count(*) from phase_evaluation_mapping where event_id= :eventId ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);

			q.setParameter("eventId", eventId);

			List list = q.list();

			result = (list.size() > 0) ? ((BigDecimal) list.get(0)).intValue()
					: null;

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return result;
	}

	public SCE[] getSCEHistoryForTRT(Integer eventId, String emplId,
			String productCode) throws SQLException {
		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();
		List<SCE> sceList = new ArrayList<SCE>();
		SCE[] sceTemp = null;
		try {

			queryString
					.append("SELECT id, eventId, emplId, productCode, product, course, ")
					.append("classroom, tableName, overallRating, overallScore, submittedDate, status ,formTitle ")
					.append("FROM ")
					.append("(SELECT ")
					.append("s.sce_id AS id,")
					.append(" s.event_id as eventId,")
					.append(" s.emplId AS emplId,")
					.append("s.product_cd AS productCode,")
					.append("s.product_name AS product,")
					.append("s.course_description as course,")
					.append(" s.classroom AS classroom,")
					.append("s.table_name AS tableName, ")
					.append("s.overall_rating AS overallRating,")
					.append("s.overall_score AS overallScore,")
					.append("s.submitted_date AS submittedDate,")
					.append("s.status AS status,")
					.append(" tv.form_title AS formTitle ")
					.append("FROM ")
					.append("sales_call.SCE_FFT s ,sales_call.template_version tv ")
					.append("WHERE ")
					.append("s.template_version_id = tv.template_version_id ")
					.append("and s.emplId = :emplId ")
					.append("and status = 'SUBMITTED' ")
					.append("and s.product_cd= :productCode ").append(") ")
					.append("ORDER BY ").append("id desc ");

			String q1 = queryString.toString();

			// System.out.println("QUERY-->"+q1);

			// System.out.println("query formed is " + q1);
			Query q = session.createSQLQuery(q1);

			q.setParameter("emplId", emplId);
			q.setParameter("productCode", productCode);

			List list = q.list();

			// System.out.println("List fetched from database is" + list);

			Iterator it = list.iterator();
			SCE empt = null;

			while (it.hasNext()) {
				empt = new SCE();
				Object[] field = (Object[]) it.next();
				empt.setId(((BigDecimal) field[0]).intValue());
				empt.setEventId(((BigDecimal) field[1]).intValue());
				empt.setEmplId((String) field[2]);
				empt.setProductCode((String) field[3]);
				empt.setProduct((String) field[4]);
				empt.setCourse((String) field[5]);
				empt.setClassroom((String) field[6]);
				empt.setTableName((String) field[7]);
				empt.setOverallRating((String) field[8]);
				empt.setOverallScore((Double) field[9]);
				empt.setSubmittedDate((Date) field[10]);
				empt.setStatus((String) field[11]);
				empt.setFormTitle((String) field[12]);

				sceList.add(empt);
			}

			// temp=(EmailTemplate[])newList.toArray();
			sceTemp = sceList.toArray(new SCE[sceList.size()]);
			// System.out.println("value of temp is " + sceTemp);

		} catch (HibernateException e) {

			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			// //
			// System.out.println("getUserByNTIdAndDomain Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return sceTemp;
	}

	public SCEDetail[] getSCEDetailsLatestPhase(Integer eventId, String course)
			throws SQLException {
		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();
		List<SCEDetail> sceDetailList = new ArrayList<SCEDetail>();
		SCEDetail[] sceDetailTemp = null;
		try {
			queryString
					.append("SELECT ")
					.append("q.question_id AS questionId,")
					.append("q.template_version_id AS templateVersionId,")
					.append("q.title AS title,")
					.append("q.display_order AS displayOrder,")
					.append(" q.description AS description,")
					.append("q.critical AS critical,")
					.append("q.type AS type,")
					.append("q.weight AS weight,")
					.append("tv.form_title AS formTitle,")
					.append("tv.precall_comments as precallComments,")
					.append("tv.postcall_comments as postcallComments,")
					.append("tv.hlc_critical as hlcCritical ")
					.append("FROM ")
					.append("QUESTION q,")
					.append("TEMPLATE_VERSION tv, ")
					.append("TEMPLATE t, ")
					.append(" EVENT_COURSE ec ")
					.append(" WHERE ")
					.append("q.template_version_id = tv.template_version_id AND ")
					.append("tv.template_id = t.template_id AND ")
					.append("tv.version = t.current_version AND ")
					.append("ec.template_id = t.template_id AND ")
					.append("ec.event_id = :eventId and ec.course_name = :course ")
					.append("order by ").append("display_order asc ");

			String q1 = queryString.toString();
			// System.out.println("query formed is " + q1);
			Query q = session.createSQLQuery(q1);

			q.setParameter("eventId", eventId);
			q.setParameter("course", course);

			List list = q.list();

			// System.out.println("List fetched from database is" + list);

			Iterator it = list.iterator();
			SCEDetail empt = null;

			while (it.hasNext()) {
				empt = new SCEDetail();
				Object[] field = (Object[]) it.next();
				empt.setQuestionId(((BigDecimal) field[0]).intValue());
				empt.setTemplateVersionId(((BigDecimal) field[1]).intValue());
				empt.setTitle((String) field[2]);
				empt.setDisplayOrder(((BigDecimal) field[3]).intValue());
				empt.setDescription((String) field[4]);
				empt.setCritical((String) field[5]);
				empt.setType((String) field[6]);
				empt.setWeight((Double) field[7]);
				empt.setFormTitle((String) field[8]);
				empt.setPrecallComments((String) field[9]);
				empt.setPostcallComments((String) field[10]);
				empt.setHlcCritical((String) field[11]);

				sceDetailList.add(empt);
			}

			sceDetailTemp = sceDetailList.toArray(new SCEDetail[sceDetailList
					.size()]);
			// System.out.println("value of temp is " + sceDetailTemp);

		} catch (HibernateException e) {

			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return sceDetailTemp;
	}

	public Role checkRoleAccess(String rolecd, Integer eventId)
			throws SQLException, SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		Role roleArr = null;
		StringBuilder queryString = new StringBuilder();

		try {

			queryString
					.append("select A.role_cd as roleCd, A.role_desc as roleDesc, A.save as isSave,A.submit as isSubmit, ")
					.append(" A.REPORT_TYPE AS reportType from PHASE_EVALUATION_ACCESS A, PHASE_EVALUATION_MAPPING B ")
					.append("WHERE A.ROLE_CD= :rolecd AND B.EVENT_ID= :eventId AND A.REPORT_TYPE=B.REPORT_TYPE ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("rolecd", rolecd);
			q.setParameter("eventId", eventId);

			List list = q.list();
			// System.out.println("size " + list.size());

			Iterator it = list.iterator();

			Role template = null;

			while (it.hasNext()) {

				template = new Role();
				Object[] field = (Object[]) it.next();

				template.setRoleCd((String) field[0]);
				template.setRoleDesc((String) field[1]);
				template.setIsSave((String) field[2]);
				template.setIsSubmit((String) field[3]);
				template.setReportType((String) field[4]);

				roleArr = template;

			}

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return roleArr;
	}

	/* GADALP STARTS HERE */

	/* Adbhut Singh - Start */

	public EventsCreated getEventsCreatedById(Integer eventId) {

		// System.out.println("Inside Hibernate layer - getEventsCreatedById");
		Session session = HibernateUtils.getHibernateSession();

		EventsCreated userArray = null;

		// System.out.println("test 1");

		try {
			// user = (User)session.get(User.class, id);
			userArray = (EventsCreated) session.get(EventsCreated.class,
					eventId);

			// changes the date format - start

			userArray.setEventEndDate(SCEUtils.ifDateNull(
					userArray.getEventEndDate(), " "));
			userArray.setEventStartDate(SCEUtils.ifDateNull(
					userArray.getEventStartDate(), " "));
			// changes the date format - end

			// System.out.println("From final - userArray");
			// System.out.println("userArray - Adbhut Event date "+
			// userArray.getEventEndDate());
		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		return userArray;
	}

	public EventsCreated[] getAllEventsCreated() {

		// System.out.println("Inside Hibernate layer");
		// System.out.println("Adbhut - Test 1");
		Session session = HibernateUtils.getHibernateSession();
		// System.out.println("adbhut -Test 2");

		EventsCreated[] userArray = null;

		StringBuilder queryString = new StringBuilder();
		// String queryString = null;

		try {
			/*
			 * String sql = "select "; sql += "event_id, "; sql +=
			 * "event_name , "; sql += "event_description , ";
			 * 
			 * sql +=
			 * "to_char(event_start_date,'mm/dd/yyyy') as eventStartDate, "; sql
			 * += "to_char(event_end_date,'mm/dd/yyyy')  as eventEndDate, ";
			 * 
			 * sql += " TO_CHAR(event_start_date) as \"", "; sql +=
			 * "event_end_date, "; sql += "event_status , "; sql +=
			 * "eval_duration , "; sql += "number_of_eval , "; sql +=
			 * "type_of_eval , "; sql += "number_of_learners  "; sql += "from ";
			 * sql += "V_Event";
			 */
			String sql = "select event_id,PRODUCT, event_name, event_description, event_start_date, event_end_date, event_status, eval_duration, number_of_eval, type_of_eval, number_of_learners,bu_id from V_Event ORDER BY EVENT_CREATED_ON DESC";

			// System.out.println("Adbhut - Test 3- SQL ::" + sql);

			Query q = session.createSQLQuery(sql)
					.addEntity(EventsCreated.class);

			List list = q.list();

			// System.out.println("List size is::::::::: " + list.size());

			/*
			 * Iterator it = list.iterator();
			 * 
			 * while (it.hasNext()) { // System.out.println(((EventsCreated)
			 * it.next()).getEventName()); }
			 */

			/*
			 * System.out .println(
			 * "AuserArray = (EventsCreated[]) list.toArray(new EventsCreated[list.size()]);"
			 * );
			 */

			userArray = (EventsCreated[]) list.toArray(new EventsCreated[list
					.size()]);

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// return ;

		// System.out.println("Test - 3");

		return userArray;

	}

	/*
	 * code added by KUMAM1234 for Role Mapping for version 3.4 RFC#1136920
	 */
	public String[] getAllRoles() throws SCEException {

		Session session = HibernateUtils.getHibernateSession();
		String[] roles = null;

		List<String> rolesList = new ArrayList<String>();

		try {

			String q1 = "select role_desc,role_cd from guest_trainer_manager_roles order by role_desc ";
			Query q = session.createSQLQuery(q1);

			List list = q.list();

			Iterator it = list.iterator();

			while (it.hasNext()) {

				Object[] field = (Object[]) it.next();

				if (field[0] == null || field[0] == "") {
					rolesList.add((String) field[1]
							+ "- Role description not available");
				} else {
					rolesList.add((String) field[0]);
				}

				roles = rolesList.toArray(new String[rolesList.size()]);

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return roles;
	}

	void gotoRemoveEvent(Integer eventId) {

		// System.out.println("Inside Hibernate layer - gotoRemoveEvent");
		// System.out.println("Adbhut - Test 1 - gotoRemoveEvent");
		Session session = HibernateUtils.getHibernateSession();
		// System.out.println("adbhut -Test 2 - gotoRemoveEvent");

		Transaction ts = null;

		try {

			String sql = "DELETE from EventsCreated  a where a.eventId =:eventId";

			// System.out.println("sql - " + sql);

			ts = session.beginTransaction();

			Query q = session.createQuery(sql);
			// System.out.println("Querry created ::" + q);
			q.setParameter("eventId", eventId);
			// System.out.println("Querry created ::" + q);
			int i = q.executeUpdate();
			// System.out.println("Execute Update - gotoRemoveEvent :" + i);

			ts.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public EventsCreated getEvent(EventsCreated events) {

		// System.out.println("Inside Hibernate layer - getEvent"+
		// (events.getEventName()));
		Session session = HibernateUtils.getHibernateSession();
		EventsCreated userArray = null;

		try {

			String sql = "select event_id, PRODUCT, event_name, event_description, event_start_date, event_end_date, event_status, eval_duration, number_of_eval, type_of_eval, number_of_learners, bu_id from V_Event where EVENT_NAME =:eventName ORDER BY EVENT_CREATED_ON DESC";

			String q1 = sql.toString();

			// System.out.println("Adbhut - querry" + q1);
			Query q = session.createSQLQuery(q1).addEntity(EventsCreated.class);
			q.setParameter("eventName", events.getEventName().trim());
			List<Object> list = q.list();

			// System.out.println("List size is::::::::: " + list.size());

			if (list.size() != 0) {

				userArray = (EventsCreated) list.get(0);
				// System.out.println("Adbhut Singh - Success "+
				// userArray.getEventName());

			}

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return userArray;
	}

	public void createEvent(EventsCreated events) {

		Session session = HibernateUtils.getHibernateSession();

		StringBuilder queryString = new StringBuilder();

		try {

			// queryString.append("insert into v_event (event_id, event_name, event_description, event_start_date, event_end_date, event_created_on,event_status");
			queryString
					.append("insert into v_event (event_id, event_name, event_description, event_start_date, event_end_date, event_created_on,event_status, eval_duration, number_of_eval, type_of_eval, number_of_learners,bu_id ) values (sq_event_created.nextval,:eventName,:eventDescription,to_date(:eventStartDate,'mm/dd/yyyy'),to_date(:eventEndDate,'mm/dd/yyyy'),sysdate,'INPROGRESS'");
			if (events.getEvalDuration() != null) {
				queryString.append(",:evalDuration");
			} else {
				queryString.append(",null");
			}
			if (events.getNumberOfEval() != null) {
				queryString.append(",:numberOfEval");
			} else {
				queryString.append(",null");
			}
			if (events.getTypeOfEval() != null) {
				queryString.append(",:typeOfEval");
			} else {
				queryString.append(",null");
			}
			if (events.getNumberOfLearners() != null) {
				queryString.append(",:numberOfLearners ");
			} else {
				queryString.append(",null ");
			}
			queryString.append(",:bu_id )");
			/*
			 * String nativeSql =
			 * "insert into v_event (event_id, event_name, event_description, event_start_date, event_end_date, event_created_on,"
			 * +
			 * "event_status, eval_duration, number_of_eval, type_of_eval, number_of_learners)"
			 * +
			 * "values (sq_event_created.nextval,:eventName,:eventDescription,to_date(:eventStartDate,'mm/dd/yyyy'),"
			 * +
			 * "to_date(:eventEndDate,'mm/dd/yyyy'),sysdate,'INPROGRESS',:evalDuration,:numberOfEval,"
			 * + ":typeOfEval,:numberOfLearners)";
			 */

			/*
			 * String nativeSql =
			 * "insert into v_event (event_id, event_name, event_description, event_start_date, event_end_date, event_created_on,"
			 * + "event_status, eval_duration, type_of_eval)" +
			 * "values (sq_event_created.nextval,:eventName,:eventDescription,to_date(:eventStartDate,'mm/dd/yyyy'),"
			 * +
			 * "to_date(:eventEndDate,'mm/dd/yyyy'),sysdate,'INPROGRESS',:evalDuration,"
			 * + ":typeOfEval)";
			 */
			String nativeSql = queryString.toString();
			// System.out.println("SQL generate for insert ::: ---" +
			// nativeSql);

			Transaction ts = session.beginTransaction();

			Query query = session.createSQLQuery(nativeSql);

			query.setParameter("eventName", events.getEventName().trim());
			query.setParameter("eventDescription", events.getEventDescription());
			query.setParameter("eventStartDate", events.getEventStartDate());
			query.setParameter("eventEndDate", events.getEventEndDate());
			query.setParameter("bu_id", events.getBusinessUnit());
			// query.setParameter("product", events.getProduct()); ---- to check

			if (events.getEvalDuration() != null) {
				query.setParameter("evalDuration", events.getEvalDuration());
			}

			if (events.getNumberOfEval() != null) {
				query.setParameter("numberOfEval", events.getNumberOfEval());
			}

			if (events.getTypeOfEval() != null) {
				query.setParameter("typeOfEval", events.getTypeOfEval());
			}

			if (events.getNumberOfLearners() != null) {
				query.setParameter("numberOfLearners",
						events.getNumberOfLearners());
			}

			// System.out.println(events.getEventName() + "  "+
			// events.getEventDescription() + " "+ events.getEventStartDate());
			// System.out.println(events.getEventEndDate() + "  "+
			// events.getEvalDuration() + " " + events.getTypeOfEval()+ "   " +
			// events.getNumberOfLearners() + " "+ events.getNumberOfEval());
			// System.out.println("SQL generate for insert ::: ---" + query);

			int i = query.executeUpdate();

			// System.out.println(" execute result set " + i);

			/*
			 * session.beginTransaction(); session.save(events);
			 * session.getTransaction().commit();
			 */

			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();
			System.out.println("createUser Hibernatate Exception");

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	public EventCourseProductMapping[] getMappingForEvent(
			String selectedEventName) {

		String x = selectedEventName;

		Session session = HibernateUtils.getHibernateSession();
		EventCourseProductMapping[] userArray = null;
		StringBuilder queryString = new StringBuilder();

		try {

			String sql = "select ";
			sql += "* ";
			sql += "from ";
			sql += "EVENT_PRODUCT_COURSE_MAPPING ";
			sql += "where EVENT_NAME = :eventName";

			String q1 = sql.toString();

			// System.out.println("Adbhut - querry" + q1);

			Query q = session.createSQLQuery(q1).addEntity(
					EventCourseProductMapping.class);

			q.setParameter("eventName", x);

			List list = q.list();

			userArray = (EventCourseProductMapping[]) list
					.toArray(new EventCourseProductMapping[list.size()]);

			// System.out.println("List size is::::::::: " + list.size());

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return userArray;
	}

	public String[] getProductForEvent() {

		Session session = HibernateUtils.getHibernateSession();
		String[] strarray = null;

		try {

			String sql = "select distinct PRODUCT_DESC from MV_ATLAS_PRODUCT order by PRODUCT_DESC asc";
			String q1 = sql.toString();

			Query q = session.createSQLQuery(q1);
			List<String> list = q.list();

			// System.out.println("Adbhut - List size for getEventName = "+
			// list.size());

			strarray = list.toArray(new String[0]);

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return strarray;
	}

	public CourseDetails[] getAllSearchedCourseDetails(String query)
			throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		CourseDetails[] courseArray = null;
		StringBuilder queryString = new StringBuilder();
		List<CourseDetails> user = new ArrayList<CourseDetails>();

		try {

			String q1 = query.toString();

			Query q = session.createSQLQuery(q1);
			List list = q.list();

			List<CourseDetails> newListCourse = new ArrayList<CourseDetails>();

			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			CourseDetails course = null;

			while (it.hasNext()) {
				course = new CourseDetails();
				Object[] field = (Object[]) it.next();

				course.setCourseId((long) ((BigDecimal) field[0]).intValue());
				course.setCourseCode((String) field[1]);
				course.setCourseName((String) field[2]);

				newListCourse.add(course);
				// System.out.println("New Event Array CourseName =========="+
				// course.getCourseName() + "======== course id "+
				// course.getCourseId());
			}

			// System.out.println("New EventCOurse List Array Length"+
			// newListCourse.size());

			courseArray = newListCourse.toArray(new CourseDetails[newListCourse
					.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return courseArray;
	}

	public EventCourseProductMapping[] gotoCheckDuplicate(String query)
			throws SQLException {

		Session session = HibernateUtils.getHibernateSession();
		EventCourseProductMapping[] eventCourseProductMapping = null;
		StringBuilder queryString = new StringBuilder();
		EventCourseProductMapping course = null;

		try {

			String q1 = query.toString();

			Query q = session.createSQLQuery(q1);
			List list = q.list();

			List<EventCourseProductMapping> newlistCourseProductMapping = new ArrayList<EventCourseProductMapping>();

			// System.out.println("size " + list.size());
			Iterator it = list.iterator();

			while (it.hasNext()) {
				course = new EventCourseProductMapping();

				course.setMapping_id(((BigDecimal) it.next()).intValue());
				newlistCourseProductMapping.add(course);
			}

			eventCourseProductMapping = newlistCourseProductMapping
					.toArray(new EventCourseProductMapping[newlistCourseProductMapping
							.size()]);

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("HelloBean Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return eventCourseProductMapping;
	}

	public void gotoSaveEventMapping(String sql) {

		Session session = HibernateUtils.getHibernateSession();

		try {

			String nativeHql = sql;

			Transaction ts = session.beginTransaction();

			Query query = session.createSQLQuery(nativeHql);

			int i = query.executeUpdate();

			// System.out.println(" execute result set - gotoSaveEventMapping "+
			// i);
			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();
			// System.out.println("createUser Hibernatate Exception");

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception e ");
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	void updateEvent(EventsCreated events) {

		System.out
				.println("/////////////////////////////////////////////////////////////////////////////////////////////");

		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;
		StringBuilder queryBuilder = new StringBuilder();
		try {
			queryBuilder
					.append("update V_Event set event_status = :eventStatus, event_start_date= to_date(:eventStartDate,'mm/dd/yyyy'),event_end_date=to_date(:eventEndDate,'mm/dd/yyyy'),event_description=:eventDescription");
			if (events.getEvalDuration() != null) {
				queryBuilder.append(",eval_duration= :evalDuration");
			} else {
				queryBuilder.append(",eval_duration= null");
			}
			if (events.getNumberOfEval() != null) {
				queryBuilder.append(",number_of_eval= :numberOfEval");
			} else {
				queryBuilder.append(",number_of_eval= null");
			}
			if (events.getTypeOfEval() != null) {
				queryBuilder.append(",type_of_eval= :typeOfEval");
			} else {
				queryBuilder.append(",type_of_eval= null");
			}
			if (events.getNumberOfLearners() != null) {
				queryBuilder.append(",number_of_learners= :numberOfLearners");
			} else {
				queryBuilder.append(",number_of_learners= null");
			}
			queryBuilder.append(" where event_id = :eventId");
			/*
			 * String sql =
			 * "update V_Event set event_status = :eventStatus, event_start_date= to_date(:eventStartDate,'mm/dd/yyyy'),"
			 * +
			 * "event_end_date=to_date(:eventEndDate,'mm/dd/yyyy'),event_description=:eventDescription,"
			 * +
			 * "eval_duration= :evalDuration,number_of_eval= :numberOfEval,type_of_eval= :typeOfEval,"
			 * +
			 * "number_of_learners= :numberOfLearners where event_id = :eventId"
			 * ;
			 */

			String sql = queryBuilder.toString();
			// System.out.println("SQL from updateEvent ===================    "+
			// sql);

			ts = session.beginTransaction();
			Query q = session.createSQLQuery(sql);

			q.setParameter("eventStatus", events.getEventStatus());
			q.setParameter("eventStartDate", events.getEventStartDate());
			q.setParameter("eventEndDate", events.getEventEndDate());
			q.setParameter("eventDescription", events.getEventDescription());
			if (events.getEvalDuration() != null) {
				q.setParameter("evalDuration", events.getEvalDuration());
			}
			if (events.getNumberOfEval() != null) {
				q.setParameter("numberOfEval", events.getNumberOfEval());
			}
			if (events.getTypeOfEval() != null) {
				q.setParameter("typeOfEval", events.getTypeOfEval());
			}
			if (events.getNumberOfLearners() != null) {
				q.setParameter("numberOfLearners", events.getNumberOfLearners());
			}
			q.setParameter("eventId", events.getEventId());

			int i = q.executeUpdate();
			// System.out.println("Execute Update : updateEvent " + i);
			ts.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
	}

	// return ;
	// Introduced by Arpit as part of SCE Migration on 5th August 2014 (Start)

	/*
	 * public String[] retrieveScoringOptions(Integer evaluationTemplateId)
	 * throws SQLException { //
	 * System.out.println("inside retrieveScoringOptions method"); //
	 * System.out.println("value of evaluationTemplateId"+
	 * evaluationTemplateId); Session session =
	 * HibernateUtils.getHibernateSession(); StringBuilder queryString = new
	 * StringBuilder(); List<String> ls = new ArrayList<String>(); String[]
	 * temp= null; try { queryString .append(
	 * "select distinct(scoring_system_identifier) as scoringSystemIdentifier from template_version "
	 * ) .append(
	 * "where template_id = :evaluationTemplateId and (scoring_system_identifier is not null or scoring_system_identifier='')"
	 * );
	 * 
	 * String q1 = queryString.toString(); //
	 * System.out.println("query formed is "+ q1); Query q =
	 * session.createSQLQuery(q1); q.setParameter("evaluationTemplateId",
	 * evaluationTemplateId);
	 * 
	 * ls = q.list(); // System.out.println("list fetched is"+ ls);
	 * 
	 * List<String> newLs = new ArrayList<String>();
	 * 
	 * Iterator it = ls.iterator(); //String s = null; while(it.hasNext()) { //s
	 * = new String(); // System.out.println("inside iterator"); //Object[]
	 * field = (Object[]) it.next(); newLs.add((String) it.next()); } //
	 * System.out.println("value of new list is"+ newLs); temp =
	 * (String[])newLs.toArray(new String[newLs.size()]); } catch
	 * (HibernateException e) { //
	 * getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY, //
	 * "getDistributionListFilters --> HibernateException : ", e); //
	 * System.out.println("Exception in retrieveScoringOptions method");
	 * e.printStackTrace();
	 * 
	 * // log.error("HibernateException in getUserByNTIdAndDomain", e); //
	 * System.out.println("getUserByNTIdAndDomain Hibernatate Exception"); }
	 * finally { HibernateUtils.closeHibernateSession(session); }
	 * 
	 * return temp; }
	 */

	public String[] getScoreValues(String scoringSystemIdentifier)
			throws SQLException {
		// System.out.println("Inside getScoreValues method");
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List<String> ls = new ArrayList<String>();
		String[] temp = null;
		try {
			queryString
					.append("select score_value from scoring_system ")
					.append(" where scoring_system_identifier=:scoringSystemIdentifier ");

			String q1 = queryString.toString();
			// System.out.println("query formed is " + q1);
			Query q = session.createSQLQuery(q1);
			q.setParameter("scoringSystemIdentifier", scoringSystemIdentifier);
			ls = q.list();
			// System.out.println("List fetched for the second time is " + ls);
			List<String> newLs = new ArrayList<String>();

			Iterator it = ls.iterator();
			// String s = null;
			while (it.hasNext()) {
				// s = new String();
				// Object[] field = (Object[]) it.next();
				newLs.add((String) it.next());
			}
			// System.out.println("value of second new list " + newLs);
			temp = (String[]) newLs.toArray(new String[newLs.size()]);
		} catch (HibernateException e) {
			// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
			// "getDistributionListFilters --> HibernateException : ", e);
			// System.out.println();
			e.printStackTrace();

			// log.error("HibernateException in getUserByNTIdAndDomain", e);
			// System.out.println("getUserByNTIdAndDomain Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return temp;
	}

	public EmailTemplate[] getAllEmailTemplates(Integer evaluationTemplateId,
			String scoringOption) throws SQLException, SCEException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List<EmailTemplate> list = new ArrayList<EmailTemplate>();
		EmailTemplate[] temp = null;
		try {

			queryString
					.append("select et.email_Template_Id as emailTemplateId,et.evaluation_template_name as evaluationTemplateName, ")
					.append("et.scoring_system_identifier as scoringSystemIdentifier, et.email_Template_Version as emailTemplateVersion,")
					.append("et.overall_score as overallScore, et.template_version_id as evaluationTemplateVersionId,")
					.append("et.publish_flag as publishFlag,et.email_subject as emailSubject,")
					.append("et.email_body as emailBody from email_template et,template temp,scoring_system s ")
					.append("where temp.template_id = :evaluationTemplateId and et.evaluation_template_name = temp.name ")
					.append("and et.scoring_system_identifier = :scoringOption and et.scoring_system_identifier = s.scoring_system_identifier ")
					.append("and et.overall_score = s.score_value group by et.email_subject, et.email_body,et.evaluation_template_name,")
					.append("et.scoring_system_identifier,et.publish_flag,et.email_Template_Id,et.template_version_id,et.overall_score,et.email_template_version,s.score_id order by s.score_id");

			String q1 = queryString.toString();
			// System.out.println("query formed is " + q1);
			Query q = session.createSQLQuery(q1);
			q.setParameter("evaluationTemplateId", evaluationTemplateId);
			q.setParameter("scoringOption", scoringOption);

			List<EmailTemplate> newList = new ArrayList<EmailTemplate>();
			list = q.list();
			// System.out.println("List fetched from database is" + list);
			Iterator it = list.iterator();
			EmailTemplate empt = null;
			while (it.hasNext()) {
				// System.out.println("within Iterator");
				empt = new EmailTemplate();
				Object[] field = (Object[]) it.next();
				empt.setEmailTemplateId(((BigDecimal) field[0]).intValue());
				// empt.setEvaluationTemplateId(((BigDecimal)
				// field[0]).intValue());
				// System.out.println("first value set");
				empt.setEvaluationTemplateName((String) field[1]);
				// System.out.println("second value set");
				empt.setScoringSystemIdentifier((String) field[2]);
				// System.out.println("Third value set");
				empt.setEmailTemplateVersion((String) field[3]);
				empt.setOverallScore((String) field[4]);
				// System.out.println("Forth value set");
				empt.setEvaluationTemplateVersionId(((BigDecimal) field[5])
						.intValue());
				// System.out.println("fifth value set");
				Character publishFlag = (Character) field[6];
				empt.setPublishFlag((String) publishFlag.toString());
				empt.setEmailSubject((String) field[7]);
				// System.out.println("ten value set");
				empt.setEmailBody((String) field[8]);
				// System.out.println("eleven value set");
				// System.out.println("value set");
				newList.add(empt);
			}
			// temp=(EmailTemplate[])newList.toArray();
			temp = newList.toArray(new EmailTemplate[newList.size()]);
			// System.out.println("value of temp is " + temp);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return temp;

	}

	public Integer publishEmailTemplate(Integer emailTemplateId,
			Integer errorCodeSize) throws SQLException, SCEException {
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts;
		Integer result = 0;
		StringBuilder queryString = new StringBuilder();
		// List<Learner> TrainList = new ArrayList<Learner>();

		try {

			queryString
					.append("call SP_PUBLISH_EMAIL_TEMPLATE(:emailTemplateId,:errorCodeSize) ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);

			q.setParameter("emailTemplateId", emailTemplateId);
			q.setParameter("errorCodeSize", errorCodeSize);

			ts = session.beginTransaction();
			result = q.executeUpdate();
			ts.commit();
			// System.out.println("result" + result);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return result;
	}

	public EmailTemplateForm getlatestEmailTemplateVersion(
			String evaluationTemplateName, String scoringOption,
			String overallScore) throws SQLException, SCEException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List<EmailTemplateForm> emailList = new ArrayList<EmailTemplateForm>();
		EmailTemplateForm empt = null;
		try {

			queryString
					.append("select evaluation_template_name as evaluationTemplateName,")
					.append("scoring_system_identifier as scoringSystemIdentifier,")
					.append("overall_Score as overallScore,")
					.append("template_version_id as evaluationTemplateVersionId,")
					.append("email_template_id as emailTemplateId,publish_flag as publishFlag,")
					.append("email_template_version as emailTemplateVersion,email_cc as emailCc,")
					.append("email_bcc as emailBCc,email_subject as emailSubject,")
					.append("email_body as emailBody from email_template ")
					.append("where evaluation_template_name =:evaluationTemplateName and scoring_system_identifier =:scoringOption ")
					.append("and overall_Score =:overallScore and publish_flag='N' ");

			String q1 = queryString.toString();
			// System.out.println("query formed is " + q1);
			Query q = session.createSQLQuery(q1);
			q.setParameter("evaluationTemplateName", evaluationTemplateName);
			q.setParameter("scoringOption", scoringOption);
			q.setParameter("overallScore", overallScore);

			List list = q.list();
			// System.out.println("List fetched from database is" + list);
			Iterator it = list.iterator();

			while (it.hasNext()) {
				// System.out.println("within Iterator");
				empt = new EmailTemplateForm();
				Object[] field = (Object[]) it.next();

				empt.setEvaluationTemplateName((String) field[0]);
				empt.setScoringSystemIdentifier((String) field[1]);
				empt.setOverallScore((String) field[2]);
				empt.setEvaluationTemplateVersionId(((BigDecimal) field[3])
						.intValue());
				empt.setEmailTemplateId(((BigDecimal) field[4]).intValue());
				Character publishFlag = (Character) field[5];
				empt.setPublishFlag((String) publishFlag.toString());
				empt.setEmailTemplateVersion((String) field[6]);
				empt.setEmailCc((String) field[6]);
				empt.setEmailBCc((String) field[7]);
				empt.setEmailSubject((String) field[8]);
				empt.setEmailBody((String) field[9]);

			}
			// temp=(EmailTemplate[])newList.toArray();

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return empt;

	}

	public void insertEmailTemplate(EmailTemplate objEmailTemplate)
			throws SQLException, SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		Transaction ts = null;

		try {

			queryString
					.append("insert into email_template(email_template_id,template_version_id, ")
					.append("email_template_version,evaluation_template_name,scoring_system_identifier, ")
					.append("overall_score,email_cc,email_bcc,email_subject,email_body, ")
					.append("publish_flag) values('"
							+ objEmailTemplate.getEmailTemplateId() + "', ")
					.append("'"
							+ objEmailTemplate.getEvaluationTemplateVersionId()
							+ "', ")
					.append("'" + objEmailTemplate.getEmailTemplateVersion()
							+ "',")
					.append("'" + objEmailTemplate.getEvaluationTemplateName()
							+ "', ")
					.append("'" + objEmailTemplate.getScoringSystemIdentifier()
							+ "', ")
					.append("'" + objEmailTemplate.getOverallScore() + "','"
							+ objEmailTemplate.getEmailCc() + "', ")
					.append("'" + objEmailTemplate.getEmailBCc() + "','"
							+ objEmailTemplate.getEmailSubject() + "', ")
					.append("'" + objEmailTemplate.getEmailBody() + "', ")
					.append("'" + objEmailTemplate.getPublishFlag() + "') ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			// q.setParameter("eventName", eventName);

			ts = session.beginTransaction();
			int result = q.executeUpdate();
			ts.commit();

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void updateEmailTemplate(EmailTemplate objEmailTemplate)
			throws SQLException, SCEException

	{

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		Transaction ts = null;

		try {

			queryString
					.append("update email_template set ")
					.append("email_cc = '" + objEmailTemplate.getEmailCc()
							+ "',")
					.append("email_bcc = '" + objEmailTemplate.getEmailBCc()
							+ "',")
					.append("email_subject = '"
							+ objEmailTemplate.getEmailSubject() + "',")
					.append("email_body = '" + objEmailTemplate.getEmailBody()
							+ "' ")
					.append("where email_template_id = '"
							+ objEmailTemplate.getEmailTemplateId() + "' ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);

			ts = session.beginTransaction();
			int result = q.executeUpdate();
			ts.commit();

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	public void deleteEmailTemplate(Integer emailTemplateId)
			throws SQLException, SCEException {
		// SCEControlImpl sceControl=new SCEControlImpl();
		Session session = HibernateUtils.getHibernateSession();
		CourseEvalTemplateMapping[] mappingsForTemplates = null;
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();

		List<CourseEvalTemplateMapping> attendeeList = new ArrayList<CourseEvalTemplateMapping>();

		try {

			queryString.append("delete from email_template ")
					.append("where email_template_id =:emailTemplateId ")
					.append("and email_template_version = 'Draft' ");

			String q1 = queryString.toString();

			// System.out.println("quer2" + queryString);
			Query q = session.createSQLQuery(q1);
			q.setParameter("emailTemplateId", emailTemplateId);

			// Query q =
			// session.createSQLQuery(q1).addEntity(CourseEvalTemplateMapping.class);
			ts = session.beginTransaction();
			int result = q.executeUpdate();
			ts.commit();
			// System.out.println("deleted" + result);

		} catch (Exception e) {
			// log.error(LoggerHelper.getStackTrace(e));
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	// Arpit code End
	// After Merge
	// *******************************************************************************************

	/*
	 * --------------------------------------------------------------------------
	 * ------------------------------
	 */
	/*
	 * Added by manish to Get total available time per day of Guest trainer in
	 * hours
	 */

	public TrainerLearnerMapping[] getevaluationHours(String date)
			throws SCEException {

		System.out
				.println("Before query for evaluation hours in SCEController");
		Session session = HibernateUtils.getHibernateSession();
		TrainerLearnerMapping[] countOfEvalsPerHour = null;
		StringBuilder queryString = new StringBuilder();

		List<TrainerLearnerMapping> countOfEvalsPerHourList = new ArrayList<TrainerLearnerMapping>();

		try {

			queryString
					.append("SELECT EMAIL,EVENT,PRODUCT,DATE_SEL,TOTAL_HOURS FROM TRAINER_EVENT_DATETIME_SLOTS ")
					.append("where DATE_SEL=TO_DATE ('" + date
							+ "','yyyy-mm-dd')+1" + "  order by EMAIL asc");

			// .append("like :eventName AND TRUNC (course_start_date) = TO_DATE (:courseStartDate, 'yyyy-mm-dd') group by trainer_email,trainer_name ");

			String q1 = queryString.toString();
			System.out.println("query: " + q1);
			Query q = session.createSQLQuery(q1);

			// q.setParameter("date", date);

			List list = q.list();

			Iterator it = list.iterator();

			TrainerLearnerMapping trainerLearnerMapping = null;

			while (it.hasNext()) {

				trainerLearnerMapping = new TrainerLearnerMapping();
				Object[] field = (Object[]) it.next();

				trainerLearnerMapping.setTrainerEmail((String) field[0]);
				trainerLearnerMapping.setEventName((String) field[1]);
				trainerLearnerMapping.setProduct((String) field[2]);
				trainerLearnerMapping.setCourseStartDate1(((Date) field[3])
						.toString());
				trainerLearnerMapping.setTotalHours((String) field[4]);

				countOfEvalsPerHourList.add(trainerLearnerMapping);

				System.out
						.println("after query for evaluation hours in SCEController");

			}
			countOfEvalsPerHour = countOfEvalsPerHourList
					.toArray(new TrainerLearnerMapping[countOfEvalsPerHourList
							.size()]);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return countOfEvalsPerHour;
	}

	// ********************************************************************************************

	/* Added by manish Get total timeSlots in string format */

	public String getTimeSlots(String date, String email) throws SCEException {

		System.out
				.println("Before query for evaluation hours in SCEController");
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		String result = "";
		try {

			queryString.append(
					"SELECT TIME_SLOTS FROM TRAINER_EVENT_DATETIME_SLOTS ")
					.append("where EMAIL='" + email
							+ "' AND DATE_SEL=TO_DATE ('" + date
							+ "','mm-dd-yyyy')+1" + "  order by EMAIL asc");

			String q1 = queryString.toString();
			System.out.println("query: " + q1);
			Query q = session.createSQLQuery(q1);

			result = (String) q.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		System.out.println("The result is:" + result);
		return result;
	}

	/* Added by manish to Get first and last name of Guest trainer in hours */

	public TrainerLearnerMapping[] getTrainerName(String email)
			throws SCEException {

		System.out.println("Before query for fetching TrainerName");
		Session session = HibernateUtils.getHibernateSession();
		TrainerLearnerMapping[] TrainerFandLname = null;
		StringBuilder queryString = new StringBuilder();

		List<TrainerLearnerMapping> TrainerFandLnameList = new ArrayList<TrainerLearnerMapping>();

		try {

			queryString
					.append("SELECT REP_FNAME,REP_LNAME FROM guest_trainer ")
					.append("where REP_EMAIL='" + email
							+ "' order by REP_EMAIL asc");

			// .append("like :eventName AND TRUNC (course_start_date) = TO_DATE (:courseStartDate, 'yyyy-mm-dd') group by trainer_email,trainer_name ");

			String q1 = queryString.toString();
			System.out.println("query: " + q1);
			Query q = session.createSQLQuery(q1);

			// q.setParameter("date", date);

			List list = q.list();

			Iterator it = list.iterator();

			TrainerLearnerMapping trainerLearnerMapping = null;

			while (it.hasNext()) {

				trainerLearnerMapping = new TrainerLearnerMapping();
				Object[] field = (Object[]) it.next();

				trainerLearnerMapping.setTrainerFname((String) field[0]);
				trainerLearnerMapping.setTrainerLname((String) field[1]);

				TrainerFandLnameList.add(trainerLearnerMapping);

				System.out.println("After query for fetching TrainerName");

			}
			TrainerFandLname = TrainerFandLnameList
					.toArray(new TrainerLearnerMapping[TrainerFandLnameList
							.size()]);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return TrainerFandLname;
	}

	// ********************************************************************************************

	/* Added by manish to Get date and time slots of Guest trainer in hours */

	public TrainerLearnerMapping[] getTrainerSlots(String email,
			String event_name, String mapId) throws SCEException {

		System.out.println("Before query for fetching TrainerSlots");
		Session session = HibernateUtils.getHibernateSession();
		TrainerLearnerMapping[] TrainerDateAndTime = null;
		StringBuilder queryString = new StringBuilder();

		List<TrainerLearnerMapping> TrainerDateAndTimeList = new ArrayList<TrainerLearnerMapping>();

		try {

			queryString
					.append("SELECT to_char(DATE_SEL, 'mm/dd/yyyy'),TIME_SLOTS FROM TRAINER_EVENT_DATETIME_SLOTS ")
					.append("where EMAIL='" + email + "' AND EVENT='"
							+ event_name + "' AND MAP_ID='" + mapId
							+ "' order by DATE_SEL asc");

			// .append("like :eventName AND TRUNC (course_start_date) = TO_DATE (:courseStartDate, 'yyyy-mm-dd') group by trainer_email,trainer_name ");

			String q1 = queryString.toString();
			System.out.println("query: " + q1);
			Query q = session.createSQLQuery(q1);

			// q.setParameter("date", date);

			List list = q.list();

			Iterator it = list.iterator();

			TrainerLearnerMapping trainerLearnerMapping = null;

			while (it.hasNext()) {

				trainerLearnerMapping = new TrainerLearnerMapping();
				Object[] field = (Object[]) it.next();

				trainerLearnerMapping.setSlotDate((field[0]).toString());
				trainerLearnerMapping.setSlotTime(field[1].toString());

				TrainerDateAndTimeList.add(trainerLearnerMapping);

				System.out
						.println("After query for fetching TrainerDate and slots");

			}
			TrainerDateAndTime = TrainerDateAndTimeList
					.toArray(new TrainerLearnerMapping[TrainerDateAndTimeList
							.size()]);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return TrainerDateAndTime;
	}

	// ********************************************************************************************

	/* Added by manish Get NoOfevaluation in Integer format for manual mapping */

	public Integer getNoOfEvaluations(String date, String email)
			throws SCEException {

		System.out
				.println("Before query for evaluation hours in SCEController");
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		Integer result;
		try {

			queryString
					.append("select (t.total_hours*60)/e.eval_duration AS noOfEvaluations from TRAINER_EVENT_DATETIME_SLOTS t INNER JOIN v_event e ")
					.append("ON t.event=e.event_name WHERE t.date_sel=TO_DATE ('"
							+ date
							+ "','mm-dd-yyyy')+1"
							+ " AND T.EMAIL='"
							+ email + "' order by EMAIL asc");

			String q1 = queryString.toString();
			System.out.println("query: " + q1);
			Query q = session.createSQLQuery(q1);

			result = (Integer) q.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		System.out.println("The result is:" + result);
		return result;
	}

	// code added by manish to update template table

	public void updateTemplate(TemplateVersion templateVersion)
			throws SQLException {

		Session session1 = HibernateUtils.getHibernateSession();
		StringBuilder queryString1 = new StringBuilder();
		System.out.println("modifiedBy " + templateVersion.getCreatedByNtid());
		Transaction ts1 = session1.beginTransaction();
		try {
			// System.out.println("updateTemplateVersion1");

			queryString1.append("UPDATE template set name =:formTitle ")
					.append("WHERE template_id =:templateId");

			String q1 = queryString1.toString();
			System.out.println("query running for updating the template:"
					+ queryString1);

			Query q = session1.createSQLQuery(q1);
			q.setParameter("formTitle", templateVersion.getFormTitle());

			System.out.println("===============================");
			System.out.println("Form Title:" + templateVersion.getFormTitle());
			System.out.println("===============================");

			q.setParameter("templateId", templateVersion.getTemplateId());

			int result = q.executeUpdate();

			// System.out.println("Result : " + result);
			ts1.commit();
			// System.out.println("updateTemplateVersion2");

		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session1);
		}

	}

	// code end

	// code added by manish to update template_version table

	public void updateTemplateVersion1(TemplateVersion templateVersion)
			throws SQLException {

		Session session1 = HibernateUtils.getHibernateSession();
		StringBuilder queryString1 = new StringBuilder();
		System.out.println("modifiedBy " + templateVersion.getCreatedByNtid());
		Transaction ts1 = session1.beginTransaction();
		try {
			// System.out.println("updateTemplateVersion1");

			queryString1.append(
					"UPDATE template_version set form_title =:formTitle ")
					.append("WHERE template_id =:templateId");

			String q1 = queryString1.toString();
			System.out.println("query running for updating the template:"
					+ queryString1);

			Query q = session1.createSQLQuery(q1);
			q.setParameter("formTitle", templateVersion.getFormTitle());

			System.out.println("===============================");
			System.out.println("Form Title:" + templateVersion.getFormTitle());
			System.out.println("===============================");

			q.setParameter("templateId", templateVersion.getTemplateId());

			int result = q.executeUpdate();

			// System.out.println("Result : " + result);
			ts1.commit();
			// System.out.println("updateTemplateVersion2");

		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session1);
		}

	}

	// code end

	// code added by manish to insert into request_access

	public void insertRequest(User user) {

		Session session = HibernateUtils.getHibernateSession();

		try {

			String nativeHql = "insert into request_access_user (lastname,firstname,email,emplId,ntid, "
					+ "ntdomain,status,create_date "
					+ ") values (:lastName,:firstName,:email,:emplId,:ntid, "
					+ ":ntdomain,:status,sysdate) ";

			Transaction ts = session.beginTransaction();

			Query query = session.createSQLQuery(nativeHql);

			query.setParameter("lastName", user.getLastName());
			query.setParameter("firstName", user.getFirstName());
			query.setParameter("email", user.getEmail());
			query.setParameter("emplId", user.getEmplId());
			query.setParameter("ntid", user.getNtid());
			query.setParameter("ntdomain", user.getNtdomain());
			query.setParameter("status", user.getStatus());

			int i = query.executeUpdate();

			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	// ------------------------------------------------------------------

	public String getRequestUser(String ntid) throws SCEException {

		StringBuilder queryString = new StringBuilder();
		List<Object> User = new ArrayList<Object>();

		if (ntid != null) {

			Session session = HibernateUtils.getHibernateSession();

			try {

				queryString.append("select ").append("ntid ")
						.append("from request_access_user ").append("where ")
						.append("upper(ntid) = upper(:ntid) ");

				String q1 = queryString.toString();
				Query q = session.createSQLQuery(q1);
				q.setParameter("ntid", ntid);

				User = q.list();

				System.out.println("=============================");
				System.out.println("List size of request_access_users:"
						+ User.size());
				System.out.println("==============================");
			} catch (HibernateException e) {
				// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
				// "getDistributionListFilters --> HibernateException : ", e);
				e.printStackTrace();

				log.error("HibernateException in getSalesPositionTypeCd", e);
				// System.out.println("HibernateException in getSalesPositionTypeCd");
			}

			finally {
				HibernateUtils.closeHibernateSession(session);
			}

		}
		return (User.size() > 0) ? (String) User.get(0) : null;
	}

	// code end

	public String requestUserStatus(String ntid) {

		StringBuilder queryString = new StringBuilder();
		List<Object> User = new ArrayList<Object>();

		if (ntid != null) {

			Session session = HibernateUtils.getHibernateSession();

			try {

				queryString.append("select ").append("status ")
						.append("from request_access_user ").append("where ")
						.append("upper(ntid) = upper(:ntid) ");

				String q1 = queryString.toString();
				Query q = session.createSQLQuery(q1);
				q.setParameter("ntid", ntid);

				User = q.list();

				System.out.println("=============================");
				System.out.println("List size of request_access_users:"
						+ User.size());
				System.out.println("==============================");
			} catch (HibernateException e) {
				// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
				// "getDistributionListFilters --> HibernateException : ", e);
				e.printStackTrace();

				log.error("HibernateException in getSalesPositionTypeCd", e);
				// System.out.println("HibernateException in getSalesPositionTypeCd");
			}

			finally {
				HibernateUtils.closeHibernateSession(session);
			}

		}
		return (User.size() > 0) ? (String) User.get(0) : null;
	}

	// code end

	public void updateRequestUserStatus(User user) {

		Session session = HibernateUtils.getHibernateSession();

		try {

			String nativeHql = "update request_access_user set status='APPROVED' WHERE upper(ntid)=upper(:ntid) ";

			Transaction ts = session.beginTransaction();

			Query query = session.createSQLQuery(nativeHql);

			query.setParameter("ntid", user.getNtid());

			int i = query.executeUpdate();

			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	// start code

	public void updateRequest(User user) {

		Session session = HibernateUtils.getHibernateSession();

		try {

			String nativeHql = "update request_access_user set status='REQUESTED' WHERE upper(ntid)=upper(:ntid) ";

			Transaction ts = session.beginTransaction();

			Query query = session.createSQLQuery(nativeHql);

			query.setParameter("ntid", user.getNtid());

			int i = query.executeUpdate();

			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	// start code to reject req user ststus

	public void rejectRequestUserStatus(User user) {

		Session session = HibernateUtils.getHibernateSession();

		try {

			String nativeHql = "update request_access_user set status='REJECTED' WHERE upper(ntid)=upper(:ntid) ";
			Transaction ts = session.beginTransaction();
			Query query = session.createSQLQuery(nativeHql);
			query.setParameter("ntid", user.getNtid());

			int i = query.executeUpdate();
			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	/*
	 * @manish for getting total approvers *
	 */

	public User[] getApprovers() throws SCEException {

		Session session = HibernateUtils.getHibernateSession();
		User[] email = null;
		StringBuilder queryString = new StringBuilder();

		List<User> emailList = new ArrayList<User>();

		try {

			queryString.append(" select email from request_approvers ");

			String q1 = queryString.toString();
			System.out.println("query: " + q1);
			Query q = session.createSQLQuery(q1);

			List list = q.list();

			System.out.println("List size:" + list.size());
			Iterator it = list.iterator();
			User user = new User();

			while (it.hasNext()) {

				user = new User();
				Object field = (Object) it.next();
				user.setRequestApprovers((field).toString());

				emailList.add(user);
			}
			email = emailList.toArray(new User[emailList.size()]);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return email;
	}

	/* code added by shaikh07 for Role Mapping for version 3.4 RFC#1136920 */
	public String[] getRoles() throws SCEException {

		Session session = HibernateUtils.getHibernateSession();
		String[] roles = null;

		List<String> rolesList = new ArrayList<String>();

		try {

			String q1 = " SELECT DISTINCT ROLE_DESC FROM MV_SALES_POSITION_TRA WHERE role_cd not IN "
					+ "( SELECT ROLE_CD FROM guest_trainer_manager_roles) order by role_desc ";
			Query q = session.createSQLQuery(q1);

			List list = q.list();
			Iterator it = list.iterator();

			while (it.hasNext()) {

				Object field = (Object) it.next();
				rolesList.add(field.toString());

			}
			roles = rolesList.toArray(new String[rolesList.size()]);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return roles;
	}

	/* code added by shaikh07 for Role Mapping for version 3.4 RFC#1136920 */
	public int saveRoles(String roleCd, String roleDescription)
			throws SCEException {

		int status = 0;
		Session session = HibernateUtils.getHibernateSession();
		try {

			String nativeHql = "insert into guest_trainer_manager_roles (ROLE_ID,ROLE_CD,ROLE_DESC) "
					+ "VALUES (seq_role_id.nextval, "
					+ ":roleCd,"
					+ ":roleDescription)";

			Transaction ts = session.beginTransaction();
			Query query = session.createSQLQuery(nativeHql);
			query.setParameter("roleCd", roleCd);
			query.setParameter("roleDescription", roleDescription);
			status = query.executeUpdate();
			ts.commit();

		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return status;
	}

	// start code by manish to remove approvers

	public void removeApprover() {

		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;

		try {

			String sql = "DELETE from request_approvers ";

			ts = session.beginTransaction();
			Query q = session.createSQLQuery(sql);

			int i = q.executeUpdate();

			ts.commit();

		} catch (HibernateException e) {
			e.printStackTrace();

		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		// return user;

	}

	// end code

	// code added by manish to insert into request_approvers

	public void insertApprover(User user) {

		Session session = HibernateUtils.getHibernateSession();

		try {

			String nativeHql = "insert into request_approvers (email,type) "
					+ "SELECT :email1, 'App Owner' FROM DUAL " + "UNION "
					+ "SELECT :email2, 'Buisness Owner 1' FROM DUAL "
					+ "UNION "
					+ "SELECT :email3, 'Buisness Owner 2' FROM DUAL ";

			Transaction ts = session.beginTransaction();

			System.out.println("query executed:" + nativeHql);

			Query query = session.createSQLQuery(nativeHql);

			query.setParameter("email1", user.getRequestApprover1());
			query.setParameter("email2", user.getRequestApprover2());
			query.setParameter("email3", user.getRequestApprover3());

			int i = query.executeUpdate();

			ts.commit();

		} catch (HibernateException e) {

			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

	}

	// ------------------------------------------------------------------

	/*
	 * code added by KUMAM1234 for Guest Trainer manager+Direct Report to check
	 * if the user is the reporting manager for version 3.4 RFC#1136920
	 */
	public String checkIfReportingManager(String guestTrainerEmplid,
			String checkEmplId) throws SCEException {
		System.out
				.println("Before query for checkIfReportingManager in SCEController");
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		String result;
		String checkResult;
		try {

			queryString
					.append(" select emplid from mv_field_employee_rbu where emplid ='"
							+ checkEmplId
							+ "'  and reports_to_emplid = '"
							+ guestTrainerEmplid + "'  ");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			checkEmplId = (String) q.uniqueResult();
			if (checkEmplId == null) {
				result = "false";
			} else {
				result = "true";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		System.out.println("The result is:" + result);
		return result;
	}

	/*
	 * code added by KUMAM1234 for Role Mapping to delete the mapped role from
	 * ODS for version 3.4 RFC#1136920
	 */
	// updated by muzees for erroro in deletion
	public void deleteRole(String roleCd) throws SQLException, SCEException {

		Session session = HibernateUtils.getHibernateSession();
		CourseEvalTemplateMapping[] mappingsForTemplates = null;
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();

		try {
			/* System.out.println("RoleCd:" + roleCd); */
			queryString.append("delete from guest_trainer_manager_roles ")
					.append("where role_cd =:role_cd ");

			String q1 = queryString.toString();

			Query q = session.createSQLQuery(q1);
			q.setParameter("role_cd", roleCd);
			ts = session.beginTransaction();
			// int result =
			q.executeUpdate();
			ts.commit();
			// return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	// added bu manish to hide the seleted evaluation form
	public int hideForm(String formId) throws SQLException, SCEException {

		Session session = HibernateUtils.getHibernateSession();
		CourseEvalTemplateMapping[] mappingsForTemplates = null;
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();

		try {
			/* System.out.println("RoleCd:" + roleCd); */
			queryString.append("update template set hidden='Y' ").append(
					"where TEMPLATE_ID =:form_id ");

			String q1 = queryString.toString();

			Query q = session.createSQLQuery(q1);
			q.setParameter("form_id", formId);
			ts = session.beginTransaction();
			int result = q.executeUpdate();
			ts.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	// added bu manish to unhide the seleted evaluation form
	public int unHideForm(String formId) throws SQLException, SCEException {

		Session session = HibernateUtils.getHibernateSession();
		CourseEvalTemplateMapping[] mappingsForTemplates = null;
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();

		try {
			/* System.out.println("RoleCd:" + roleCd); */
			queryString.append("update template set hidden='N' ").append(
					"where TEMPLATE_ID =:form_id ");

			String q1 = queryString.toString();

			Query q = session.createSQLQuery(q1);
			q.setParameter("form_id", formId);
			ts = session.beginTransaction();
			int result = q.executeUpdate();
			ts.commit();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

	}

	/*
	 * Start of PBG and UpJOHN 2019 MUZEES Start
	 */
	// to filter Non-USField BU,All
	public String[] getBUList() throws SCEException {

		Session session = HibernateUtils.getHibernateSession();
		String[] bu = null;

		List<String> buList = new ArrayList<String>();

		try {

			String q1 = " SELECT DISTINCT BUSINESS_UNIT FROM BUSINESS_UNITS where key not in ('3','4')  ORDER BY BUSINESS_UNIT ASC ";
			Query q = session.createSQLQuery(q1);

			List list = q.list();
			Iterator it = list.iterator();

			while (it.hasNext()) {

				Object field = (Object) it.next();
				buList.add(field.toString());

			}
			bu = buList.toArray(new String[buList.size()]);
		} catch (Exception e) {
			e.printStackTrace();
			// throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return bu;
	}

	// to fetch Non-USField BU
	public String getNonUSBUID() throws SCEException {

		Session session = HibernateUtils.getHibernateSession();
		String bu = null;
		try {

			String q1 = " SELECT DISTINCT BUSINESS_UNIT FROM BUSINESS_UNITS Where key=3";
			Query q = session.createSQLQuery(q1);

			List list = q.list();
			Iterator it = list.iterator();

			while (it.hasNext()) {

				Object field = (Object) it.next();
				bu = field.toString();

			}

		} catch (Exception e) {
			e.printStackTrace();
			// throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return bu;
	}

	public String getAllBUID() throws SCEException {

		Session session = HibernateUtils.getHibernateSession();
		String bu = null;
		try {

			String q1 = " SELECT DISTINCT BUSINESS_UNIT FROM BUSINESS_UNITS Where key=4";
			Query q = session.createSQLQuery(q1);

			List list = q.list();
			Iterator it = list.iterator();

			while (it.hasNext()) {

				Object field = (Object) it.next();
				bu = field.toString();

			}

		} catch (Exception e) {
			e.printStackTrace();
			// throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return bu;
	}

	public String[] getAllBUList() throws SCEException {

		Session session = HibernateUtils.getHibernateSession();
		String[] bu = null;

		List<String> buList = new ArrayList<String>();

		try {

			String q1 = " SELECT DISTINCT BUSINESS_UNIT FROM BUSINESS_UNITS ORDER BY BUSINESS_UNIT ASC ";
			Query q = session.createSQLQuery(q1);

			List list = q.list();
			Iterator it = list.iterator();

			while (it.hasNext()) {

				Object field = (Object) it.next();
				buList.add(field.toString());

			}
			bu = buList.toArray(new String[buList.size()]);
		} catch (Exception e) {
			e.printStackTrace();
			// throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return bu;
	}

	public String[] getEventNameByBU(String businessUnit) throws SCEException {
		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		String[] strings = null;
		String string = new String();
		List list = null;
		List<String> strList = new ArrayList<String>();
		try {
			queryString
					.append("select EVENT_NAME from V_EVENT where EVENT_STATUS IN ( 'ACTIVE','INPROGRESS') and BU_ID=:bu_id ORDER BY EVENT_CREATED_ON DESC");
			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);
			q.setParameter("bu_id", businessUnit);
			strList = q.list();

			if (strList != null) {

				strings = strList.toArray(new String[strList.size()]);
			}

		} catch (HibernateException e) {

			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}
		return strings;
	}

	public String[] getProductForEventbyBu(String businessUnit)
			throws SCEException {

		Session session = HibernateUtils.getHibernateSession();
		String[] strarray = null;

		try {

			String sql = "select distinct PRODUCT_DESC from MV_ATLAS_PRODUCT "
					+ "where SALES_ORGANIZATION_DESC in(SELECT DISTINCT SALES_ORGANIZATION FROM BU_SALESORG_MAPPING WHERE MAPPED_BU=:bu_id) "
					+ "order by PRODUCT_DESC asc";
			String q1 = sql.toString();

			Query q = session.createSQLQuery(q1);
			q.setParameter("bu_id", businessUnit);
			List<String> list = q.list();
			strarray = list.toArray(new String[0]);

		} catch (HibernateException e) {
			e.printStackTrace();
			// System.out.println("Hibernatate Exception");
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return strarray;
	}

	public BU[] getAllMappedSalesOrg(String nonUSfieldBU) throws SCEException {

		Session session = HibernateUtils.getHibernateSession();

		BU[] salesOrgBuList = null;

		List<BU> buList = new ArrayList<BU>();

		try {

			String q1 = "select distinct SALES_ORGANIZATION ,mapped_bu from BU_SALESORG_MAPPING where MAPPED_BU !=:bu_id order by SALES_ORGANIZATION";
			Query q = session.createSQLQuery(q1);
			q.setParameter("bu_id", nonUSfieldBU);
			List list = q.list();

			Iterator it = list.iterator();

			System.out.println("List size:" + list.size());
			Iterator it1 = list.iterator();

			while (it.hasNext()) {

				Object[] obj = (Object[]) it.next();

				String salesOrg = String.valueOf(obj[0]);
				String mappedBU = String.valueOf(obj[1]);
				BU user = new BU();

				user.setMappedBU(mappedBU);
				user.setSalesOrg(salesOrg);

				buList.add(user);

			}

			BU arr[] = new BU[buList.size()];

			salesOrgBuList = buList.toArray(arr);
			System.out.println("array=" + Arrays.toString(salesOrgBuList));

		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return salesOrgBuList;
	}

	public String[] getSalesOrg() throws SCEException {

		Session session = HibernateUtils.getHibernateSession();
		String[] salesorgList = null;

		List<String> listofSalesOrg = new ArrayList<String>();

		try {

			String q1 = "SELECT DISTINCT SALES_ORGANIZATION_DESC FROM mv_sales_position_tra where SALES_ORGANIZATION_DESC NOT IN"
					+ "(SELECT DISTINCT SALES_ORGANIZATION from BU_SALESORG_MAPPING) ORDER BY SALES_ORGANIZATION_DESC";
			Query q = session.createSQLQuery(q1);

			List list = q.list();
			Iterator it = list.iterator();

			while (it.hasNext()) {

				Object field = (Object) it.next();
				listofSalesOrg.add(field.toString());

			}
			salesorgList = listofSalesOrg.toArray(new String[listofSalesOrg
					.size()]);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		return salesorgList;
	}

	public int saveBUMapping(String businessUnit, String salesOrganisation)
			throws SCEException {

		int status = 0;
		Session session = HibernateUtils.getHibernateSession();
		try {

			String nativeHql = "insert into BU_SALESORG_MAPPING (SALES_ORGANIZATION,MAPPED_BU) "
					+ "VALUES (:salesOrganisation,:businessUnit)";
			Transaction ts = session.beginTransaction();
			Query query = session.createSQLQuery(nativeHql);
			query.setParameter("businessUnit", businessUnit);
			query.setParameter("salesOrganisation", salesOrganisation);
			status = query.executeUpdate();
			ts.commit();

		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return status;
	}

	public void deleteSalesOrg(String salesOrganisation) throws SQLException,
			SCEException {

		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;
		StringBuilder queryString = new StringBuilder();
		try {

			queryString.append("delete from BU_SALESORG_MAPPING ").append(
					"where SALES_ORGANIZATION =:salesOrganisation");

			String q1 = queryString.toString();

			Query q = session.createSQLQuery(q1);
			q.setParameter("salesOrganisation", salesOrganisation);
			ts = session.beginTransaction();
			q.executeUpdate();
			ts.commit();

		} catch (Exception e) {
			e.printStackTrace();
			throw new SCEException("error.sce.unknown", e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		System.out.println(result);

	}

	public void updateOverNightGT(User user) {

		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;
		try {

			String sql = "UPDATE users us set us.BU_ID=:bu_id , us.BU_UPDATED='YES' where us.USER_ID = :userId";
			ts = session.beginTransaction();
			Query q = session.createSQLQuery(sql);

			q.setParameter("userId", user.getId());
			q.setParameter("bu_id", user.getBusinessUnit());
			q.executeUpdate();
			ts.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
		// end of MUZEES
	}

	/* 2020 Q3: Start of muzees for multiple evaluation */
	public boolean checkduplicate(SCE checkSCE, SCE newSCE) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;

		try {
			String sql = null;
			sql = "select count(*) from TR.V_USP_SCE_REGISTERED where emplid='"
					+ checkSCE.getEmplId()
					+ "' and activity_pk='"
					+ checkSCE.getProductCode()
					+ "' and REGISTRATION_DATE>(select SUBMITTED_DATE from sales_call.sce_fft where sce_id="
					+ checkSCE.getId() + ")";
			Query q = session.createSQLQuery(sql);
			List list = q.list();
			Integer multipleCount = ((BigDecimal) list.get(0)).intValue();
			// check for multiple registration
			if (multipleCount > 0)
				return false;
			// if it not multiple registration check for LMS flag
			sql = "select distinct lms_flag from sales_call.evaluation_form_score where template_version_id = "
					+ checkSCE.getTemplateVersionId()
					+ " and Score_legend = '"
					+ checkSCE.getOverallRating() + "'";
			q = session.createSQLQuery(sql);
			String latestLMSFlag = null;
			list = q.list();
			if (list.size() > 0) {
				latestLMSFlag = ((Character) list.get(0)).toString();
				if (latestLMSFlag.equalsIgnoreCase("Y")) {
					return true;
				}
			}
			// if latest evaluation flag is 'N' check for new LMS flag and
			// submitted date
			if (newSCE != null) {
				sql = "select distinct lms_flag from sales_call.evaluation_form_score where template_version_id = "
						+ newSCE.getTemplateVersionId()
						+ " and Score_legend = '"
						+ newSCE.getOverallRating()
						+ "'";
				q = session.createSQLQuery(sql);
				if (list.size() > 0) {
					list = q.list();
					String newLMSFlag = ((Character) list.get(0)).toString();
					if (newLMSFlag.equalsIgnoreCase("Y"))
						return false;
				}
				DateFormat dateFormatter = new SimpleDateFormat(
						SCEUtils.DEFAULT_DATE_FORMAT);
				if (newSCE.getSubmittedDate() != null) {
					if (dateFormatter.format(newSCE.getSubmittedDate())
							.compareTo(
									dateFormatter.format(checkSCE
											.getSubmittedDate())) == 0) {
						return true;

					} else
						return false;
				} else {
					if (dateFormatter.format(new Date()).compareTo(
							dateFormatter.format(checkSCE.getSubmittedDate())) == 0) {
						return true;

					} else
						return false;
				}

			}

		}

		catch (Exception e) {
			System.out.println("Exception Message:" + e.getMessage());
			e.printStackTrace();
			log.error(e, e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return false;
	}
	

	public List<SCE> getListSCE(String emplId, Integer eventId,
			String productCode, String status) {

		Session session = HibernateUtils.getHibernateSession();
		StringBuilder queryString = new StringBuilder();
		List<SCE> newTemp = new ArrayList<SCE>();
		try {

			queryString
					.append("SELECT s.sce_id, e.lms_flag")
					.append(" FROM sce_fft s, evaluation_form_score e")
					.append(" WHERE s.template_version_id = e.template_version_id")
					.append(" AND e.Score_legend = s.OVERALL_RATING")
					.append(" AND s.emplid = :emplId")
					.append(" AND s.product_cd = :productCode")
					.append(" AND s.event_id = :eventId")
					.append(" AND status = :status")
					.append(" ORDER BY sce_id DESC");

			String q1 = queryString.toString();
			Query q = session.createSQLQuery(q1);

			q.setParameter("emplId", emplId);
			q.setParameter("eventId", eventId);
			q.setParameter("productCode", productCode);
			q.setParameter("status", status);

			List list = q.list();

			Iterator it = list.iterator();

			SCE sce = null;
			while (it.hasNext()) {
				sce = new SCE();
				Object[] field = (Object[]) it.next();

				sce.setId(field[0] != null ? ((BigDecimal) field[0]).intValue()
						: null);
				sce.setLmsFlag(((Character) field[1]).toString());
				newTemp.add(sce);
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			HibernateUtils.closeHibernateSession(session);
		}

		return newTemp;

	}// end of MUZEES
	/* 2020 Q3,Q4: Start of MUZEES for registartion date with time */
	public Date minRegistrationDate(String emplid, String eventid,
			String productCode) {
		Session session = HibernateUtils.getHibernateSession();
		Transaction ts = null;
		Date registrationDate = null;
		String regSql = null;
		String sql = null;
		Query q = null;
		boolean latestSCEExist = false;
		List list = null;
		Iterator it = null;
		String sce_id = null;
		try {
			List<SCE> listSCE = getListSCE(emplid, Integer.parseInt(eventid),
					productCode, SCEConstants.ST_SUBMITTED);

			for (int i = 0; i < listSCE.size(); i++) {
				if (listSCE.get(i).getLmsFlag().equalsIgnoreCase("Y")) {
					latestSCEExist = true;
					sce_id = "" + listSCE.get(i).getId();
					break;
				} else {
					latestSCEExist = false;
				}
			}

			regSql = "select MIN(REGISTRATION_DATE) from TR.V_USP_SCE_REGISTERED where emplid='"
					+ emplid + "' and activity_pk='" + productCode;
			if (latestSCEExist) {

				regSql = regSql
						+ "' and REGISTRATION_DATE>(select SUBMITTED_DATE from sales_call.sce_fft where sce_id="
						+ sce_id + ")";
			} else
				regSql = regSql + "'";
			q = session.createSQLQuery(regSql);
					/*.addScalar("REGISTRATION_DATE",
					org.hibernate.type.TimestampType.INSTANCE);*/
			list = q.list();
			it = list.iterator();
			while (it.hasNext()) {
				registrationDate = (Date) it.next();
			}

		} catch (Exception e) {
			System.out.println("Exception Message:" + e.getMessage());
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}

		return registrationDate;
	}//end of muzees
	

	// Start: SHINDO : Added for OAUTH changes to invoke OAUTH URL's and retrieve user NTID, EMPLID,NTDOMAIN
	
		public String getAuthenticatedUserID(HttpServletRequest request,HttpServletResponse response) {
			Session session = HibernateUtils.getHibernateSession();
			String res = null;
			StringBuilder queryString = new StringBuilder();
		
				// getServiceBean().logDebug(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
				// "start of getAdhocDistributionListMember");

				// System.out.println("quer1" + queryString);
				

				// System.out.println("HelloBean Hibernatate Exception");
			
			String username = null;
			String userDomain=null;
			String userEmplid=null;
			//String q1 = " select u.emplid as emplId from users u where upper(u.ntid) = upper(:ntid) ";
			


			System.out.println("Authcode: " + request.getParameter("authcode"));

			String authCode = request.getParameter("authcode") != null ? request
					.getParameter("authcode") : null;
					
			
			
			if (authCode != null) {

				Long errorcode = null;
				System.out.println(SCEConstants.OAUTH_TOKEN_URL + authCode
									+ "&redirect_uri="
									+ SCEConstants.OAUTH_REDIRECT_URI
									+SCEConstants.OAUTH_GET_TOKEN_AUTHORIZATION);

			}
			
			Long errorcode=null;
			
			
			try {	
				
				SSLContext sslContext = SSLContext.getInstance("TLSv1.2"); 
				sslContext.init(null, null, new SecureRandom());
				
			
				URL url = new URL(
						SCEConstants.OAUTH_TOKEN_URL + authCode
						+ "&redirect_uri="
						+ SCEConstants.OAUTH_REDIRECT_URI);
				System.out.println("url: "+url);
				
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
				conn.setSSLSocketFactory(sslContext.getSocketFactory());
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded:");
                conn.setRequestProperty("Authorization",SCEConstants.OAUTH_GET_TOKEN_AUTHORIZATION);
                System.out.println("Author: "+SCEConstants.OAUTH_GET_TOKEN_AUTHORIZATION);

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                String access_token="";
                System.out.println("Output from Server .... \n");
                while ((output = br.readLine()) != null) {
                	System.out.println("Buffer: "+output);
                    JSONObject token= new JSONObject(output);
                    access_token=token.getString("access_token");
                    System.out.println("access"+access_token);
                    
                }
                
                URL url1= new URL (SCEConstants.OAUTH_VALIDATE_TOKEN_URL+ access_token);
                System.out.println("url1"+url1);

                HttpsURLConnection conn2 = (HttpsURLConnection) url1.openConnection();
                
				conn2.setSSLSocketFactory(sslContext.getSocketFactory());
                conn2.setRequestMethod("POST");
               // conn2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded:");
                conn2.setRequestProperty("Authorization",SCEConstants.OAUTH_VALIDATE_TOKEN_AUTHORIZATION);
                System.out.println("Author: "+SCEConstants.OAUTH_VALIDATE_TOKEN_AUTHORIZATION);
                if (conn2.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br2 = new BufferedReader(new InputStreamReader(
                        (conn2.getInputStream())));

                String output2="";
                System.out.println("Output from conn2 .... \n");
                JSONObject token2= new JSONObject();
                while ((output2 = br2.readLine()) != null) {
                	System.out.println("Buffer"+output2);
                     token2= new JSONObject(output2);
                     JSONObject details= token2.getJSONObject("access_token");
                     username=details.getString("NTID");
                     userDomain=details.getString("domain");
                                       
                }
                String q1 = " select u.emplid as emplId from users u where upper(u.ntid) = upper(:username) ";
				Query q = session.createSQLQuery(q1);
				List list = q.list();

				// System.out.println("size " + list.size());

				res = (list.size() > 0) ? (String) list.get(0) : null;
                userEmplid=res;
                System.out.println(username +"  "+userDomain+"  "+userEmplid); 
                conn.disconnect();
                conn2.disconnect();

                

            } catch (Exception e) {

                e.printStackTrace();

            }
			return username+","+userDomain+","+userEmplid;
			}
		
}

	

