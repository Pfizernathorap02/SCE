package com.pfizer.sce.utils; 

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import org.apache.log4j.Logger;


public class LoggerHelper 
{     
   // private static transient Logger sceLogger = Logger.getLogger(LoggerHelper.class.getName());  
	 private static transient Logger sceLogger = LogManager.getLogger(LoggerHelper.class.getName()); 
      
    public static void logSystemError(String msg)
    {
        sceLogger.error(msg);
    }
    
    public static void logSystemError(String msg,Throwable t)
    {
        sceLogger.error(msg,t);
    }    
    
    public static void logSystemDebug(String msg)
    {
        sceLogger.debug(msg);
    }
    
    public static void logSystemDebug(String msg,Throwable t)
    {
        sceLogger.debug(msg,t);
    }        
    
    public static void logSystemWarning(String msg)
    {
        sceLogger.warn(msg);
    }
    
    public static void logSystemWarning(String msg,Throwable t)
    {
        sceLogger.warn(msg,t);
    } 
    
    
    /*Added by Shinoy for SCE/TRT Enhancement 2011*/       
    /** When an exception occurs, this code snippet will print the stack trace
     * of the exception to a string. This way, the exception stack trace at runtime
     * can be recorded in log file.

     */

    public static String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
    
    /*End added by Shinoy for SCE/TRT Enhancement 2011*/    
} 
