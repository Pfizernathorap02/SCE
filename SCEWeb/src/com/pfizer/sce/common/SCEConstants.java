package com.pfizer.sce.common; 

import java.util.HashMap;

public class SCEConstants 
{ 
    public static final String DC = "DC";
    public static final String NI = "NI";
    public static final String UN = "UN";
    public static final String NA = "N/A";
    
    public static final String VIEW_MODE = "View";
    public static final String CREATE_MODE = "Create";
    
    public static final Integer EVENT_FFT = new Integer(1);
    public static final String EVENT_FFT_COURSE = new String("ALL");
    public static final String EVENT_COURSE_ALL = new String("ALL");
    
    //TAGS FOR PARAMETERS IN UP_AUTH
    public static final String UP_AUTH_DOMAIN = "Domain";
    public static final String UP_AUTH_NTID = "LoginID";
    public static final String UP_AUTH_EMPLID = "EmployeeID";
    
    //Question Types
    public static final String QT_SCORE = "SCORE_FILL";
    public static final String QT_SCORE_FETCH = "SCORE_FETCH";
    public static final String QT_COMMENTS = "COMMENTS";    
    
    //SCE Status
    public static final String ST_SUBMITTED = "SUBMITTED";
    public static final String ST_DRAFT = "DRAFT";
    public static final String ST_NEW = "NEW";
    
    // Invalid
    public static Integer SCE_INVALID_ID = new Integer("-1");
    
    public static final String APP_DATASOURCE = "SCE_XA_DataSource";
    
    public static final String TR_SUPERADMIN = "SUPER ADMIN";
    public static final String ALL_EVENT_ID = "-1";
   
    /*Added by Neha*/ 
    public static final String SENDTOLMS = "Y";
    public static final String DONOTSENDTOLMS = "N";
    public static final String SUBMITTED="SUBMITTED";
    //End Neha's code
} 
