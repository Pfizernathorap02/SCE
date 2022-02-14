package com.pfizer.sce.servlets;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pfizer.sce.common.SCEConstants;

public class InviteServlet extends HttpServlet{
public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		ServletContext sContext =this.getServletContext();
		
		try {
			
			
			HttpSession session = request.getSession();
			System.out.println("inside inviteServlet");
			
			SCEConstants.INVITE_FLAG="Y";
			
			String whichEvent=request.getParameter("whichEvent");
			String productName=request.getParameter("productName");
			String strDate=request.getParameter("strDate");
			String GtEmail=request.getParameter("GtEmail");
			System.out.println("event= "+whichEvent+" product = "+productName+" strDate = "+strDate);
			
			
			session.setAttribute("whichEvent", whichEvent);
			session.setAttribute("productName", productName);
			session.setAttribute("strDate", strDate);
			session.setAttribute("GtEmail", GtEmail);
			
			
			System.out.println(request.getContextPath());
						
			/*request.getRequestDispatcher("/evaluation/inviteAuth.jsp").forward(request,response);*/
			
			request.getRequestDispatcher("").forward(request, response);
	        
	        
		}
		catch (Exception e){
			String msg = "Failed to accept invite : " + e.getMessage();
			System.out.println(msg);
			throw new ServletException(e);
		}
}
}
