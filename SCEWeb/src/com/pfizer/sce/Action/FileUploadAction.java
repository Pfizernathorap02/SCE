package com.pfizer.sce.Action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.pfizer.sce.ActionForm.UploadBlankFormForm;
import com.pfizer.sce.beans.EmailTemplate;
import com.pfizer.sce.beans.LegalConsentTemplate;
import com.pfizer.sce.beans.SCEException;
import com.pfizer.sce.beans.TemplateVersion;
import com.pfizer.sce.beans.UploadBlankForm;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.beans.UserLegalConsent;
import com.pfizer.sce.db.SCEManager;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.EvaluationControllerHelper;

public class FileUploadAction extends ActionSupport implements
		ServletRequestAware {

	private File fileUpload;
	private String fileUploadContentType;
	private String fileUploadFileName;
	private HttpServletRequest request;
	private static SCEManagerImpl sceManager = new SCEManagerImpl();

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	private String checkLegalConsent(HttpServletRequest req, HttpSession session) {

		// System.out.println("Entry in to Helper.checkLegalConsent method...");
		// sceManager = new SCEManagerImpl();

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
				int nlcId = 0;
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

	public String getFileUploadContentType() {
		return fileUploadContentType;
	}

	public void setFileUploadContentType(String fileUploadContentType) {
		this.fileUploadContentType = fileUploadContentType;
	}

	public String getFileUploadFileName() {
		return fileUploadFileName;
	}

	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}

	public File getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String gotoTemplateAdmin()
    {
		HttpServletRequest req = getRequest();
        HttpSession session = req.getSession();
          String result = checkLegalConsent(req,session);
          System.out.println("*****result*****:"+result);
          if(result != null && result.equals("success")  ){
            System.out.println("*************Forwarding to legalConsent");
            String forwardToHomePage = "N";
            EvaluationControllerHelper.setBookMarkURL(session,req,forwardToHomePage);
            return new String("legalConsent");
          }else if(result != null && result.equals("exception")){
             System.out.println("**********Forwarding to exception");
             return new String("failure");
          }
        TemplateVersion[] templateVersions = null;
        // Get All Template Versions
        try{
 
        templateVersions = sceManager.getAllTemplateVersions();
        req.setAttribute("templateVersions",templateVersions);
        }catch(SCEException scee){
            req.setAttribute("errorMsg",scee.getErrorCode());
            return new String("failure");
        }catch(Exception e){
            req.setAttribute("errorMsg","error.sce.unknown");
            //sceLogger.error(LoggerHelper.getStackTrace(e));
            return new String("failure");
        }
        return new String("success");        
    }
 
	
	
	
	public String uploadBlankForm() {
		// System.out.println("Inside gotoUploadBlankForm method");
		HttpServletRequest req = getRequest();
		HttpSession session = req.getSession();
		String ntid = "";
		/* CHANGE MADE ON 2 DEC BY RUPINDER */
		try {
			// System.out.println("Inside gotoUploadBlankForm");
			Integer templateVersionId = null;
			int id = 0;

			User user = (User) session.getAttribute("user");
			// System.out.println("Inside gotoUploadBlankForm - User Name..."+ user.getName());

			if (user == null) {
				ntid = "bakhsr";
				// System.out.println("ntid ::::::::::::" + ntid);
			} else {
				ntid = user.getNtid();
				System.out
						.println("Inside gotoUploadBlankForm - Employee ID ::::::::::::"
								+ user.getEmplId());
			}

			templateVersionId = new Integer(
					req.getParameter("templateVersionId"));
			// System.out.println("gotoUploadBlankForm -- templateVersionId = "+ templateVersionId);

			String scoringSystemIdentifier = req
					.getParameter("scoringSystemIdentifier");
			//System.out.println("gotoUploadBlankForm -- scoringSystemIdentifier = "+ scoringSystemIdentifier);

			String templateName = req.getParameter("templateName");
			// System.out.println("gotoUploadBlankForm -- templateName = "+ templateName);

			id = sceManager.getUploadFileId();
			// System.out.println("id after gotoUploadBlankForm in JPF" + id);

			id = id + 1;
			// System.out.println("Newly generated Upload blank file id = " + id);

			UploadBlankForm uploadBlankForm = new UploadBlankForm();
			uploadBlankForm.setTemplateVersionId(templateVersionId);
			uploadBlankForm.setUploadedBlankFileId(id);
			uploadBlankForm.setUploadedBy(ntid);

			TemplateVersion templateVersion = new TemplateVersion();

			templateVersion.setPublishFlag("Y");
			templateVersion.setScoringSystemIdentifier(scoringSystemIdentifier);
			templateVersion.setTemplateName(templateName);

			// Get the size of the uploaded file.
			int filesize = (int) getFileUpload().length();
			// System.out.println("filesize = " + filesize);

			File myFile = getFileUpload();
			String contentType = getFileUploadContentType();

			// System.out.println("contentType = " + contentType);

			String fileName = getFileUploadFileName();
			// System.out.println("fileName = " + fileName);

			byte[] fileData = new byte[filesize];
			//byte[] fileData = FileUtils.readFileToByteArray(myFile);
			try {
				FileInputStream fileInputStream = new FileInputStream(myFile);     
            	fileInputStream.read(fileData);     
            	fileInputStream.close();

				//System.out.println("fileData array length = " + fileData.length);

				uploadBlankForm.setUploadedBlankFile(fileData);

			} catch (IOException e) {
				// System.out.println("IO Exception...error in file upload");
				e.printStackTrace();
			}

			// System.out.println("Before calling uploadBlankForm");
			
			//Blob blob = new SerialBlob(fileData);
			
			//uploadBlankForm.setBlobData(blob);
			sceManager.uploadBlankForm(uploadBlankForm);

			// System.out.println("Before calling publishEvaluationTemplate");

			/* DEBUG */
			// System.out.println("uploadBlankForm.getTemplateVersionId() = "+ uploadBlankForm.getTemplateVersionId());
			// System.out.println("uploadBlankForm.getUploadedBlankFileId() = "+ uploadBlankForm.getUploadedBlankFileId());
			// System.out.println("uploadBlankForm.getUploadedBy() = "+ uploadBlankForm.getUploadedBy());
			/* DEBUG */

			sceManager.publishEvaluationTemplate(uploadBlankForm);

			EmailTemplate emailTemplate = sceManager.checkEmailTemplate(
					scoringSystemIdentifier, templateName);
			if (emailTemplate == null) {
				// Modified on 11 dec RUPINDER ends
				String[] scoreValue = sceManager
						.getScoreValues(scoringSystemIdentifier);
				// System.out.println("Before for loop");
				for (int i = 0; i < scoreValue.length; i++) {
					String score_value = scoreValue[i];
					// System.out.println("the score value is::" + score_value);
					Integer emailTemplateId = sceManager
							.getNextValEmailTemplateId();
					// System.out.println("emailTemplateId::" + emailTemplateId);

					// System.out.println("uploadBlankForm.getUploadedBy() = "+ uploadBlankForm.getUploadedBy());

					sceManager.insertIntoEmailTemplate(templateVersion,
							templateVersionId, emailTemplateId, score_value);

				}
			}

			else {

				sceManager.updateEmailTemplate(templateName,
						scoringSystemIdentifier, templateVersionId);
			}
			// Else condition which will update the template version for the
			// same email template

			// Get All Template Versions
			System.out
					.println("Before calling getAllTemplateVersions() in JPF");
			TemplateVersion[] templateVersions = sceManager
					.getAllTemplateVersions();
			req.setAttribute("templateVersions", templateVersions);
			// MODIFIED ON 9 DEC starts
			session = req.getSession(false);
			session.setAttribute("uploadcomplete", "uploadcomplete");
			// MODIFIED ON 9 DEC ends
		} catch (SCEException scee) {
			req.setAttribute("errorMsg", scee.getErrorCode());
			return new String("failure");
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");
	}

	private HttpServletRequest getRequest() {
		// TODO Auto-generated method stub
		return request;
	}

}
