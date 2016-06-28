package com.pfizer.sce.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.pfizer.sce.beans.LegalConsentTemplate;
import com.pfizer.sce.beans.User;
import com.pfizer.sce.beans.UserLegalConsent;

import com.pfizer.sce.db.SCEManagerImpl;

public class LegalConsentHelper {	
	 
	public String checkLegalConsent(HttpServletRequest req, HttpSession session) {
		SCEManagerImpl sceManager = new SCEManagerImpl();
		// System.out.println("Entry in to Helper.checkLegalConsent method...");
		// log.debug("Entry in to Helper.checkLegalConsent method...");

		String ntid = "";
		try {
			User user = (User) session.getAttribute("user");
			// System.out.println(" User Object:" + user);
			if (user == null) {
				ntid = req.getHeader("IAMPFIZERUSERPFIZERNTLOGONNAME");
				// System.out.println("Getting ntid from IAM Header ntid:" + ntid);
				// log.debug("Getting ntid from IAM Header ntid:" + ntid);
				String emplid = req.getHeader("IAMPFIZERUSERWORKFORCEID");
				// System.out.println("Getting emplid from IAM Header emplid:"+ emplid);
				// log.debug("Getting emplid from IAM Header emplid:"+ emplid);
				String domain = req
						.getHeader("IAMPFIZERUSERPFIZERNTDOMAINNAME");
				// System.out.println("Getting domain from IAM Header domain:"+ domain);
				// log.debug("Getting domain from IAM Header domain:" + domain);
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
				// log.debug("Exit from Helper.checkLegalConsent method before forwarding to legalConsent.jsp");
				return "success";

			} else {
				System.out
						.println("Exit from Helper.checkLegalConsent method before forwarding to failure");
				// log.debug("Exit from Helper.checkLegalConsent method before forwarding to failure");
				return "failure";
			}
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			// log.error("Exception in checkLegalConsent", e);
			
			// System.out.println("Exception in checkLegalConsent");
			e.printStackTrace();		
			System.out
					.println("Exit from Helper.checkLegalConsent method before forwarding to exception");
			// log.debug("Exit from Helper.checkLegalConsent method before forwarding to exception");
			return "exception";
		}

	}
}
