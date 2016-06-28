package com.pfizer.sce.beans; 


public class SCEException extends Exception
{ 
    private String      strErrorCode;
    private String      strErrorMessage;
    private Throwable 	nestedException;
    


    public SCEException() {
        super();
    }

   public SCEException(String strErrorCode, Throwable nestedException) {
        this.strErrorCode 		= strErrorCode;
        this.nestedException    = nestedException;
        this.strErrorMessage 	= nestedException.getMessage();
    }


    public SCEException(String strErrorCode) {
        this.strErrorCode    = strErrorCode;
        this.strErrorMessage = strErrorCode;
    }

    public SCEException(String strErrorCode, String strErrorMessage) {
        this.strErrorCode    = strErrorCode;
        this.strErrorMessage = strErrorMessage;
    }
    
    public String getErrorCode() {
        return strErrorCode;
    }

    public String getMessage() {
        return strErrorMessage;
    }

    public Throwable getCause() {
        return nestedException;
    }   
    
} 
