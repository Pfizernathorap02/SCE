package com.pfizer.sce.beans; 

public class Util 
{ 
    public String isNull(String check)
    {
        String Sub = " ";
        if(check == null)
        {
            check = Sub;
        }        
        return check;
    }

    public String isComment(String check)
    {
        String Sub = "Comment";
        if(check == null)
        {
            check = Sub;
        }        
        return check;
    }
} 
