package com.pfizer.sce.Action;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.sce.beans.AddEventEditForm;
import com.pfizer.sce.beans.EventsCreated;
import com.pfizer.sce.db.SCEManagerImpl;
import com.pfizer.sce.helper.LegalConsentHelper;
import com.pfizer.sce.utils.LoggerHelper;
import com.pfizer.sce.utils.SCEUtils;

public class EditEvent extends ActionSupport implements ServletRequestAware,
		ModelDriven {

	private static SCEManagerImpl sceManager = new SCEManagerImpl();
	private static LegalConsentHelper legalConsentHelp = new LegalConsentHelper();
	private HashMap<String, String> typeOfEvals = SCEUtils.getTypeOfEvals();
	private static SCEUtils sceUtils = new SCEUtils();

	HttpServletRequest request;
	AddEventEditForm form = new AddEventEditForm();

	private String evalDuration;
	private String numberOfLearners;
	private Integer eventId;
	private Integer h_eventId;
	private Integer selEventId;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public Object getModel() {
		return form;
	}

	public HashMap getTypeOfEvals() {
		return typeOfEvals;
	}

	public void setTypeOfEvals(HashMap typeOfEvals) {
		this.typeOfEvals = typeOfEvals;
	}

	public String gotoEditEvent() {
		// // System.out.println("******inside edit event method******");

		HttpServletRequest req = getRequest();

		try {
			EventsCreated events1 = null;
			Integer eventId = null;

			if (SCEUtils.isFieldNotNullAndComplete(req
					.getParameter("selEventId"))) {

				eventId = new Integer(req.getParameter("selEventId"));
				//  System.out.println("the event id to be edited is" + eventId);
				events1 = sceManager.getEventsCreatedById(eventId);
				req.setAttribute("events1", events1);

				// adbhut - for getting event statuses

				HashMap eventstatuses = sceUtils.getEventStatuses(events1);

				// adbhut - end

				System.out
						.println("******going outside edit event method******");
				form.setEventId(events1.getEventId());
				form.setEventName(events1.getEventName());
				form.setEventDescription(events1.getEventDescription());
				form.setEventStartDate(events1.getEventStartDate());
				form.setEventEndDate(events1.getEventEndDate());
				form.setEventStatus(events1.getEventStatus());
				form.setEvalDuration(events1.getEvalDuration());
				form.setNumberOfEval(events1.getNumberOfEval());
				form.setTypeOfEval(events1.getTypeOfEval());
				form.setEventstatuses(eventstatuses);
				form.setNumberOfLearners(events1.getNumberOfLearners());

				return new String("success");

			}
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");
	}

	public String gotoUpdateEvent() {

		HttpServletRequest req = getRequest();
		HttpSession session = req.getSession();

		try {
			if (form != null) {

				EventsCreated events = new EventsCreated();
//				//  System.out.println(form.getEventName());
//				//  System.out.println(form.getEventStartDate());
				// eventId = new Integer(req.getParameter("eventId"));
				// System.out.println(req.getParameter("h_eventId"));
//				System.out.println(form.getEventId());
	/*			System.out
						.println("******going outside edit event method******"
								+ "event id from request " + eventId);
				System.out
						.println("=================================  event ID    "
								+ (new Integer(req.getParameter("eventId"))));
				System.out
						.println("/////////////////////////////////////////the changed event description is   ");
				System.out
						.println("form.getEventDescription() - event ID      "
								+ form.getEventId());*/
/*				// System.out.println(req.getParameter("h_typeOfEval"));
				// System.out.println(req.getParameter("h_eventStartDate"));
				// System.out.println(req.getParameter("h_eventEndDate"));
				// System.out.println(req.getParameter("h_eventDescription"));
				// System.out.println(req.getParameter("h_numberOfEval"));
				// System.out.println(req.getParameter("h_numberOfLearners"));*/
				
				
				events.setEventStatus(form.getEventStatus());
				events.setEventId(Integer.parseInt(req.getParameter("h_eventId")));
				events.setEventStartDate(form.getEventStartDate());
				events.setEventEndDate(form.getEventEndDate());
				events.setEventDescription(form.getEventDescription());
				events.setEvalDuration(form.getEvalDuration());
				events.setNumberOfEval(form.getNumberOfEval());
				events.setTypeOfEval(form.getTypeOfEval());
				events.setNumberOfLearners(form.getNumberOfLearners());

				sceManager.updateEvent(events);
				// System.out.println("inside *******************updateevent");

				EventsCreated[] eventsList = sceManager.getAllEventsCreated();

				req.setAttribute("eventsList", eventsList);

				// System.out.println("inside *******************updateevent");
				return new String("success");

			}
		} catch (Exception e) {
			req.setAttribute("errorMsg", "error.sce.unknown");
			System.out
					.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			// sceLogger.error(LoggerHelper.getStackTrace(e));
			return new String("failure");
		}
		return new String("success");
	}

	public String getEvalDuration() {
		return evalDuration;
	}

	public void setEvalDuration(String evalDuration) {
		this.evalDuration = evalDuration;
	}

	public String getNumberOfLearners() {
		return numberOfLearners;
	}

	public void setNumberOfLearners(String numberOfLearners) {
		this.numberOfLearners = numberOfLearners;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public Integer getH_eventId() {
		return h_eventId;
	}

	public void setH_eventId(Integer h_eventId) {
		this.h_eventId = h_eventId;
	}

	public Integer getSelEventId() {
		return selEventId;
	}

	public void setSelEventId(Integer selEventId) {
		this.selEventId = selEventId;
	}

}
