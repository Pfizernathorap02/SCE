package com.pfizer.sce.servlets;

import java.io.FileInputStream;
import java.util.Properties;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.pfizer.sce.common.SCEConstants;

public class SCEProperties extends HttpServlet {

public void init() throws ServletException {
		
		ServletContext sContext =this.getServletContext();
		
		try {
			String propFile = sContext.getInitParameter("properties");
			System.out.println(propFile);

			Properties prop = new Properties();
			FileInputStream inStream1 = new FileInputStream(propFile);
			prop.load(inStream1);
			SCEConstants.loadOauthValues(prop);
			inStream1.close();
			
		
	}
		catch (Exception e){
			String msg = "Failed to initialize : " + e.getMessage();
			System.out.println(msg);
			throw new ServletException(e);
		}
		
	}
}
