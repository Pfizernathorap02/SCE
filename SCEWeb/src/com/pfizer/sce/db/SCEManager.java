package com.pfizer.sce.db;

import com.pfizer.sce.beans.Question;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.TemplateVersion;

import java.sql.Blob;

public interface SCEManager {

	com.pfizer.sce.beans.User getUserByNTId(java.lang.String ntid);

	com.pfizer.sce.beans.SCEDetail[] getSCEDetailsBySCEId(
			java.lang.Integer sceId);

	com.pfizer.sce.beans.Attendee getAttendeeByEmplId(java.lang.String emplId);

	void createUser(com.pfizer.sce.beans.User user);

	void updateUser(com.pfizer.sce.beans.User user);

	void removeUser(java.lang.Integer userId);

	com.pfizer.sce.beans.TemplateVersion getTemplateVersionById(
			java.lang.Integer templateVersionId);

	java.lang.String removeTemplateVersion(java.lang.Integer templateVersionId);

	com.pfizer.sce.beans.SCEData[] getSCEData();

	java.util.HashMap getCoursesMap(com.pfizer.sce.beans.SCEData[] sceData);

	com.pfizer.sce.beans.SCEDetail[] getSCEDetailsByTemplateVersionId(
			java.lang.Integer templateVersionId);

	com.pfizer.sce.beans.User getUserByEmplId(java.lang.String emplId);

	com.pfizer.sce.beans.SCEReport[] getPendingAttendeesByCourse(
			java.lang.String course);

	java.lang.String[] getClassroomsByCourse(java.lang.String course);
	//monika
    java.lang.String[] getEventName();
    
    java.lang.String[] getCoursesForEvent(java.lang.String event) 
			 throws com.pfizer.sce.beans.SCEException;
    
    com.pfizer.sce.beans.Learner[] getLearnersByCourseAndDate(java.lang.String course, java.lang.String frmDate, java.lang.String toDate) throws com.pfizer.sce.beans.SCEException;
   
    com.pfizer.sce.beans.Learner getLearnerByEmailCourse(java.lang.String email, java.lang.String course) throws com.pfizer.sce.beans.SCEException;
   
    void addNewLearner(com.pfizer.sce.beans.Learner learner) throws com.pfizer.sce.beans.SCEException;
    //monika end
    
    /*CHANGES KHATED START*/
	com.pfizer.sce.beans.EventsCreated[] getAllActiveInprogressEvents();
	
	com.pfizer.sce.beans.WebExDetails[] getWebExDetailsByEvent(java.lang.String eventName)throws com.pfizer.sce.beans.SCEException;
	
	void addWebExDetails(com.pfizer.sce.beans.WebExDetails webExDetails) throws SCEException;
	/*CHANGES KHATED END*/

    java.util.HashMap getSCEDataMap_Prev(com.pfizer.sce.beans.SCEData[] sceData);

	java.util.HashMap getSCEDataMap(com.pfizer.sce.beans.SCEData[] sceData);

	java.util.HashMap getProductsMap(com.pfizer.sce.beans.SCEData[] sceData);

	com.pfizer.sce.beans.User[] getUsersByStatus(java.lang.String userStatus);

	com.pfizer.sce.beans.Event[] getEvents();

	com.pfizer.sce.beans.Attendee[] getPendingAttendees(
			java.lang.Integer eventId, java.util.Date trainingDate,
			java.lang.String productCode, java.lang.String course,
			java.lang.String excludeEmplId);

	java.util.HashMap getEventMap();

	void giveAutoCredit(java.lang.String emplId, java.lang.Integer eventId,
			java.lang.String productCode, java.lang.String product,
			java.lang.String course, java.lang.String classroom,
			java.lang.String tableName, java.lang.String submittedByEmplId);

	com.pfizer.sce.beans.Event getEventById(java.lang.Integer eventId);

	java.lang.Integer getEventIdByName(java.lang.String eventName);

	java.lang.Integer getDefaultEventId();

	com.pfizer.sce.beans.SCEReport[] getSCEReport(java.lang.Integer eventId);

	com.pfizer.sce.beans.SCEReport getTotalCompletedByEventIdCourseId(
			java.lang.Integer eventId, java.lang.Integer courseId);

	com.pfizer.sce.beans.SCEReport[] getSCECountByEventIdCourseId(
			java.lang.Integer eventId, java.lang.Integer courseId);

	com.pfizer.sce.beans.SCEReport[] getPendingAttendeesByCourseAndClass(
			java.lang.Integer eventId, java.lang.Integer courseId,
			java.lang.String classroom);

	java.lang.String[] getClassroomsByCourseId(java.lang.Integer eventId,
			java.lang.Integer courseId);

	com.pfizer.sce.beans.SCE getLastFilledSCE(java.lang.String emplId,
			java.lang.Integer eventId, java.lang.String productCode);

	com.pfizer.sce.beans.User[] getAllUsers();

	java.util.HashMap getAllEventMap();

	com.pfizer.sce.beans.Event[] getAllEvents();

	void deleteSCE(java.lang.Integer sceId);

	void saveSCEOld(com.pfizer.sce.beans.SCE objSCE,
			com.pfizer.sce.beans.SCEDetail[] objSCEDetailArr);

	java.lang.Integer getTemplateVersionIdByTemplateId(java.lang.Integer templid);

	java.util.HashMap getSCEDataMap_Current(
			com.pfizer.sce.beans.SCEData[] sceData);

	com.pfizer.sce.beans.SCEData[] getSCEData_RBU();

	com.pfizer.sce.beans.Attendee[] getPendingAttendeesByFullcriteria(
			java.lang.Integer eventId, java.util.Date trainingDate,
			java.lang.String productCode, java.lang.String course,
			java.lang.String classroom, java.lang.String table,
			java.lang.String excludeEmplId);

	/* Added for CSO requirements */
	/* End of addition */
	/* Added for RBU enhancement */

	com.pfizer.sce.beans.Role checkRoleAccess(java.lang.String roleCd,
			java.lang.Integer eventId);

	java.lang.Integer isPhaseTraining(java.lang.Integer eventId);

	/* Added for SCE evaluation form */

	/* Added to fix the Admin bug */

	com.pfizer.sce.beans.SCEComment getExistingSCEComment(
			java.lang.String sceid, java.lang.String activityid,
			java.lang.String repid);

	void updateSCEComment(java.lang.String sceId, java.lang.String activity_id,
			java.lang.String phaseNo, java.lang.String rep_id,
			java.lang.String comment1, java.lang.String comment2,
			java.lang.String comment3, java.lang.String enteredby1,
			java.lang.String enteredby2, java.lang.String enteredby3,
			java.lang.String date1, java.lang.String date2,
			java.lang.String date3, java.lang.String submitted_by,
			java.lang.String submitted_date);

	void insertSCEComment(java.lang.String sceId, java.lang.String activity_id,
			java.lang.String phaseNo, java.lang.String rep_id,
			java.lang.String comment1, java.lang.String comment2,
			java.lang.String comment3, java.lang.String enteredby1,
			java.lang.String enteredby2, java.lang.String enteredby3,
			java.lang.String date1, java.lang.String date2,
			java.lang.String date3, java.lang.String submitted_by,
			java.lang.String submitted_date);

	com.pfizer.sce.beans.SCEComment[] getSCEComment(java.lang.String trackid,
			java.lang.String repid);

	java.lang.Integer isSCEComment(java.lang.String phaseid,
			java.lang.String sceid, java.lang.String repid);

	com.pfizer.sce.beans.Attendee[] getAttendeesBySearchCriteria(
			java.lang.Integer eventId, java.lang.String lastName,
			java.lang.String firstName, java.lang.String emplId,
			java.lang.String salesPositionId, java.lang.String isPassport,
			java.lang.String isVisible, java.lang.String salesPositionTypeCd);

	com.pfizer.sce.beans.Attendee[] getPendingAttendeesByFullcriteriaWithCode(
			java.lang.Integer eventId, java.util.Date trainingDate,
			java.lang.String productCode, java.lang.String course,
			java.lang.String classroom, java.lang.String table,
			java.lang.String excludeEmplId, java.lang.String salesPositionTypeCd);

	// Added by Mahua for SCE/TRT Enhancement 2011
	// End Mahua's code

	// Added by Rupinder for SCE/TRT Enhancement 2011

	// End Rupinder's code

	// Added by Padmini for SCE/TRT Enhancement 2011

	// End Padmini's code

	// Added by Shinoy for SCE/TRT Enhancement 2011

	// Added after code integration (09/11/2011:21:00)
	Question[] getQuestionsByTemplateVersionId(
			java.lang.Integer templateVersionId);

	void saveTemplateVersion(
			com.pfizer.sce.beans.TemplateVersion templateVersion,
			java.util.List questionList);

	// End added after code integration (09/11/2011:21:00)

	// End Shinoy's code

	// Added by Neha for SCE/TRT Enhancement 2011

	// java.sql.Blob getByteArray();
	// java.sql.Blob getByteArray(java.lang.Integer sceId);
	// End Neha's code

	/* Author: Mayank Date:27-Oct-2011 SCE Enhancement 2011 */

	/* End : Author: Mayank Date:27-Oct-2011 SCE Enhancement 2011 */

	java.lang.Integer getNumOfFormTitles(java.lang.String formTitle)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.ScoringSystem[] getScoringSystem()
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.TemplateVersion getTemplateVersionByIdNew(
			java.lang.Integer templateVersionId)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.BusinessRule[] getBusinessRulesByTemplateVersionId(
			java.lang.Integer templateVersionId)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.LegalQuestion[] getLegalQuestionsByTemplateVersionId(
			java.lang.Integer templateVersionId)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.EvaluationFormScore[] getEvaluationFormScoresByTemplateVersionId(
			java.lang.Integer templateVersionId)
			throws com.pfizer.sce.beans.SCEException;

	java.lang.String getSceFeedbackId(java.lang.String trackid);

	com.pfizer.sce.beans.Template[] getAllEvaluationTemplates()
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.CourseEvalTemplateMapping[] getAllMappingsForTemplate(
			java.lang.Integer evalTemplateId)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.CourseDetails[] getAllSearchedCourseDetails(
			java.lang.String courseName, java.lang.String courseCode)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.Template[] getTemplateDetailsForCourse(
			java.lang.Integer[] arrCourseId)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.CourseEvalTemplateMapping[] getAlreadyMappedCourses(
			java.lang.String selCourseIds, java.lang.String selCourseCodes,
			java.lang.Integer selEvalTempId)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.CourseDetails[] getCourseDetailsForActPk(
			java.lang.String[] alreadyMappedRecs)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.LegalConsentTemplate fetchLegalContent()
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.UserLegalConsent checkLegalConsentAcceptance(
			java.lang.String ntid) throws com.pfizer.sce.beans.SCEException;

	void acceptLegalConsent(
			com.pfizer.sce.beans.UserLegalConsent userLegalConsent)
			throws com.pfizer.sce.beans.SCEException;

	int fetchMaxAcceptedId() throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.LegalConsentTemplate[] getAllLegalTemplates()
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.LegalConsentTemplate getLegalTemplateById(int lcId)
			throws com.pfizer.sce.beans.SCEException;

	void publishLegalTemplate(
			com.pfizer.sce.beans.LegalConsentTemplate legalConsentTemplate)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.LegalConsentTemplate getPublishedVersion()
			throws com.pfizer.sce.beans.SCEException;

	void unpublishOlderVersion() throws com.pfizer.sce.beans.SCEException;

	int getMaxVersion() throws com.pfizer.sce.beans.SCEException;

	int getMaxLcId() throws com.pfizer.sce.beans.SCEException;

	void overWriteVersion(
			com.pfizer.sce.beans.LegalConsentTemplate legalConsentTemplate)
			throws com.pfizer.sce.beans.SCEException;

	void createNewVersion(
			com.pfizer.sce.beans.LegalConsentTemplate legalConsentTemplate)
			throws com.pfizer.sce.beans.SCEException;

	java.lang.String[] getScoreValues(java.lang.String scoringSystemIdentifier)
			throws com.pfizer.sce.beans.SCEException;

	void insertIntoEmailTemplate(
			com.pfizer.sce.beans.TemplateVersion templateVersion,
			java.lang.Integer templateVersionId,
			java.lang.Integer emailTemplateId, java.lang.String score_value)
			throws com.pfizer.sce.beans.SCEException;

	java.sql.Blob getBlankForm(java.lang.Integer templateVersionId)
			throws com.pfizer.sce.beans.SCEException;

	int getUploadFileId() throws com.pfizer.sce.beans.SCEException;

	void uploadBlankForm(com.pfizer.sce.beans.UploadBlankForm uploadBlankForm)
			throws com.pfizer.sce.beans.SCEException;

	void publishEvaluationTemplate(
			com.pfizer.sce.beans.UploadBlankForm uploadBlankForm)
			throws com.pfizer.sce.beans.SCEException;

	java.lang.Integer getNextValEmailTemplateId()
			throws com.pfizer.sce.beans.SCEException;

	java.util.HashMap getAllEvaluationTemplatesMap()
			throws com.pfizer.sce.beans.SCEException;

	java.util.HashMap getScoringOptions(java.lang.Integer evaluationTemplateId)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.EmailTemplate[] searchEmailTemplates(
			java.lang.Integer evaluationTemplateId,
			java.lang.String scoringOption)
			throws com.pfizer.sce.beans.SCEException;

	java.lang.String publishEmailTemplate(java.lang.Integer emailTemplateId)
			throws com.pfizer.sce.beans.SCEException;

	void deleteEmailTemplate(java.lang.Integer emailTemplateId)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.EmailTemplateForm getSelectedEmailTemplate(
			java.lang.Integer emailTemplateId)
			throws com.pfizer.sce.beans.SCEException;

	java.lang.String saveEmailTemplate(
			com.pfizer.sce.beans.EmailTemplate emailTemplate)
			throws com.pfizer.sce.beans.SCEException;

	void uploadForm(com.pfizer.sce.beans.EvaluationForm evalForm,
			java.lang.Integer sceId) throws com.pfizer.sce.beans.SCEException;

	java.sql.Blob getByteArray(java.lang.Integer sceId)
			throws com.pfizer.sce.beans.SCEException;

	java.util.HashMap getAllScores(java.lang.Integer templateVersionId)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.SCE[] getSCESHistoryByEventEmplId(
			java.lang.Integer eventId, java.lang.String emplId,
			java.lang.String productName)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.EvaluationFormScore getEvaluationFormValues(
			java.lang.Integer templateVersionId, java.lang.String overallScore)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.User getManagerDetail(java.lang.String emplid)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.EvaluationFormScore[] getEvaluationFormScore(
			java.lang.Integer templateVersionId)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.LegalQuestion[] getLegalQuestion(
			java.lang.Integer templateVersionId)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.LegalQuestionDetail[] getLegalQuestionDetail(
			java.lang.Integer sceId) throws com.pfizer.sce.beans.SCEException;

	void saveSCE(com.pfizer.sce.beans.SCE objSCE,
			com.pfizer.sce.beans.SCEDetail[] objSCEDetailArr,
			com.pfizer.sce.beans.LegalQuestion[] legalDetailArr)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.SCE getSCEById(java.lang.Integer sceId);

	com.pfizer.sce.beans.SCE[] getSCESByEventEmplId(java.lang.Integer eventId,
			java.lang.String emplId) throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.User getUserById(java.lang.Integer id)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.TemplateVersion[] getAllTemplateVersions()
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.Template getTemplateByTemplateVersionId(
			java.lang.Integer templateVersionId)
			throws com.pfizer.sce.beans.SCEException;

	java.util.HashMap getProductsHash();



	com.pfizer.sce.beans.TemplateVersion[] retrieveEvalForm()
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.TemplateVersion retrieveFirstEvalForm()
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.CourseEvalTemplateMapping[] saveMappedRecords(
			java.lang.String emplId, java.lang.String excludedActivityPk,
			java.lang.String selectedTemplateId,
			java.lang.String selectedTemplateName,
			java.lang.String selectedCourseIds)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.SCE getLatestSCE(java.lang.String emplId,
			java.lang.Integer eventId, java.lang.String productCode,
			java.lang.String status) throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.EmailTemplate getEmailTemplate(
			java.lang.Integer templateVersionId, java.lang.String overallScore)
			throws com.pfizer.sce.beans.SCEException;

	java.lang.Integer findLMSCourseMappingId(java.lang.Integer templateId)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.SCE[] getSCEHistoryForTRT(java.lang.Integer eventId,
			java.lang.String emplId, java.lang.String productCode)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.EmailTemplate checkEmailTemplate(
			java.lang.String scoringSystemIdentifier,
			java.lang.String templateName)
			throws com.pfizer.sce.beans.SCEException;

	void updateEmailTemplate(java.lang.String templateName,
			java.lang.String scoringSystemIdentifier,
			java.lang.Integer templateVersionId);

	com.pfizer.sce.beans.SCE getNewSCEPhase(java.lang.Integer eventId,
			java.lang.String course) throws com.pfizer.sce.beans.SCEException;

	boolean checkIfDuplicate(com.pfizer.sce.beans.SCE objSCE,
			com.pfizer.sce.beans.SCEDetail[] objSCEDetailArr)
			throws com.pfizer.sce.beans.SCEException;

	void saveSCEPhase(com.pfizer.sce.beans.SCE objSCE,
			com.pfizer.sce.beans.SCEDetail[] objSCEDetailArr)
			throws com.pfizer.sce.beans.SCEException;

	int fetchVersionNumber(java.lang.Integer templateVersionId)
			throws com.pfizer.sce.beans.SCEException;

	com.pfizer.sce.beans.SCE getNewSCE(java.lang.Integer eventId,
			java.lang.String course, java.lang.String productName)
			throws com.pfizer.sce.beans.SCEException;

	java.lang.String getProductName(java.lang.String productCode)
			throws com.pfizer.sce.beans.SCEException;

	java.lang.Integer getTemplateVersionId(java.lang.Integer templateId)
			throws com.pfizer.sce.beans.SCEException;

	
	void saveTemplateVersionNew(
			com.pfizer.sce.beans.TemplateVersion templateVersion,
			java.lang.String pageName, boolean isModified,
			boolean isAlreadyMapped) throws com.pfizer.sce.beans.SCEException;


	/* Adbhut Singh Start */

	com.pfizer.sce.beans.EventsCreated[] getAllEventsCreated();

	com.pfizer.sce.beans.EventsCreated getEvent(
			com.pfizer.sce.beans.EventsCreated events)
			throws com.pfizer.sce.beans.SCEException;

	void createEvent(com.pfizer.sce.beans.EventsCreated events)
			throws com.pfizer.sce.beans.SCEException;



	com.pfizer.sce.beans.EventCourseProductMapping[] getMappingForEvent(
			String selectedEventName);

	java.lang.String[] getProductForEvent();

	void gotoDeleteEventMapping(java.lang.Integer mappingID);

	com.pfizer.sce.beans.EventCourseProductMapping[] gotoCheckDuplicate(
			java.lang.String course, java.lang.String whichEventName);

	void gotoSaveEventMapping(java.lang.String selectedEventName,
			java.lang.String selectedProductName,
			java.lang.String selectedCourseName);

	void gotoDeleteProductMapping(java.lang.String pName,
			java.lang.String selEventName);

	void updateEvent(com.pfizer.sce.beans.EventsCreated events);

	/* Adbhut Singh End */
//After Merge
	com.pfizer.sce.beans.CourseEvalTemplateMapping[] deleteSelLmsRecord(
			java.lang.Integer delLmsId, java.lang.Integer templateId)
			throws com.pfizer.sce.beans.SCEException;


}
