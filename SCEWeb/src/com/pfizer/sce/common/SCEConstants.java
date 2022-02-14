package com.pfizer.sce.common; 


import java.util.HashMap;
import java.util.Properties;

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
    
    /*Added by shindo for Oauth*/ 
    public static String OAUTH_REDIRECT_URI=null;
    public static String OAUTH_CODE_URL = null;
    public static String OAUTH_TOKEN_URL= null;
    public static String OAUTH_VALIDATE_TOKEN_URL = null;
    public static String OAUTH_GET_TOKEN_AUTHORIZATION = null;
    public static String OAUTH_VALIDATE_TOKEN_AUTHORIZATION=null;
    public static String INVITE_FLAG="N";
    public static String TR_FLAG="N";
     
    public final static void loadOauthValues (Properties prop) throws Exception
    {
    	OAUTH_REDIRECT_URI= prop.getProperty("OauthRedirectURI");
    	if (OAUTH_REDIRECT_URI==null){
    		throw new Exception ("OauthRedirectURI is required");
    	}
    	
    	OAUTH_GET_TOKEN_AUTHORIZATION= prop.getProperty("GetTokenAuthorization");
    	if (OAUTH_GET_TOKEN_AUTHORIZATION==null){
    		throw new Exception ("GetTokenAuthorization is required");
    	}
    	
    	OAUTH_VALIDATE_TOKEN_AUTHORIZATION= prop.getProperty("ValidateTokenAuthorization");
    	if (OAUTH_VALIDATE_TOKEN_AUTHORIZATION==null){
    		throw new Exception ("ValidateTokenAuthorization is required");
    	}
    	
    	OAUTH_CODE_URL= prop.getProperty("OauthCodeURL");
    	if (OAUTH_CODE_URL==null){
    		throw new Exception ("OauthCodeURL is required");
    	}
    	
    	OAUTH_TOKEN_URL= prop.getProperty("OauthGetTokenURL");
    	if (OAUTH_TOKEN_URL==null){
    		throw new Exception ("OauthGetTokenURL is required");
    	}
    	
    	OAUTH_VALIDATE_TOKEN_URL= prop.getProperty("OauthValidateTokenURL");
    	if (OAUTH_VALIDATE_TOKEN_URL==null){
    		throw new Exception ("OauthValidateTokenURL is required");
    	}
    	
    	/*HOST_URL= prop.getProperty("hostURL");
    	if (OAUTH_VALIDATE_TOKEN_URL==null){
    		throw new Exception ("HOST_URL is required");
    	}
    	
    	PORT_NUMBER= Integer.valueOf(prop.getProperty("portNumber"));
    	if (PORT_NUMBER==null){
    		throw new Exception ("PORT_NUMBER is required");
    	}*/
    	System.out.println("OAUTH properties loaded....");
    	
    	System.out.println("OAUTH_REDIRECT_URI  =  "+ OAUTH_REDIRECT_URI);
    	System.out.println("OAUTH_GET_TOKEN_AUTHORIZATION  =  "+ OAUTH_GET_TOKEN_AUTHORIZATION);
    	System.out.println("OAUTH_VALIDATE_TOKEN_AUTHORIZATION  =  "+ OAUTH_VALIDATE_TOKEN_AUTHORIZATION );
    	System.out.println("OAUTH_CODE_URL  =  "+ OAUTH_CODE_URL);
    	System.out.println("OAUTH_TOKEN_URL  =  "+ OAUTH_TOKEN_URL);
    	System.out.println("OAUTH_VALIDATE_TOKEN_URL  =  "+ OAUTH_VALIDATE_TOKEN_URL);
    	/*System.out.println("HOST_URL   =  "+ HOST_URL);*/

    }
    //end of shindo's code
} 
