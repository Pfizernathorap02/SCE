package com.pfizer.sce.utils;

//import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.pfizer.sce.db.SCEControlImpl;

public class HibernateUtils {
	public static final String HIBERNATE_SESSIONFACTORY_JNDI_NAME = "HibernateSessionFactory";	
	private static SessionFactory sessionFactory = null;
	static{
		//SessionFactory sessionFactory =  new Configuration().configure().buildSessionFactory();
		
		if(sessionFactory == null){
			sessionFactory =  new Configuration().configure().buildSessionFactory();
		}
	}
	
	//static Logger log = Logger.getLogger(HibernateUtils.class.getName());

	public static Session getHibernateSession() {
		//SessionFactory sessionFactory = null;
		Session session = null;
		try {
			/*
			 * Context ctx = new InitialContext(); Object obj =
			 * ctx.lookup(HIBERNATE_SESSIONFACTORY_JNDI_NAME); sessionFactory =
			 * (SessionFactory) obj;
			 */
			
			//SessionFactory sessionFactory =  new Configuration().configure().buildSessionFactory();
			session = sessionFactory.openSession();
			//log.debug("Log getHibernateSession");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return session;

	}

	public static boolean closeHibernateSession(Session session) {
		boolean retVal = false;
		try {
			if (session != null) {
				session.close();
			}

			retVal = true;
		} catch (Exception e) {
			e.printStackTrace();
			retVal = false;
		}
		
		//log.debug("Log closeHibernateSession");
		return retVal;
	}
}
