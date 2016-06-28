package com.pfizer.sce.beans; 

public class SCEinput 
{ 
    
            private String emplId;
            private String activityId;
            private String empName;
            private String evalName;
            private String evalId;
            private String flag;
            private String isSAdmin="false";
            private String trackId;
            private String sceId;
            
            public void setEmplId(String emplId)
        {
            this.emplId = emplId;
        }

        public String getEmplId()
        {
            return this.emplId;
        }
        
         public void setActivityId(String activityId)
        {
            this.activityId = activityId;
        }

        public String getActivityId()
        {
            return this.activityId;
        }
        
        
        public void setEmpName(String empName)
        {
            this.empName = empName;
        }

        public String getEmpName()
        {
            return this.empName;
        }
        
        public void setEvalName(String evalName)
        {
            this.evalName = evalName;
        }

        public String getEvalName()
        {
            return this.evalName;
        }
        
        public void setEvalId(String evalId)
        {
            this.evalId = evalId;
        }

        public String getEvalId()
        {
            return this.evalId;
        }
        
        public void setFlag(String flag)
        {
            this.flag = flag;
        }

        public String getFlag()
        {
            return this.flag;
        }
        
        public void setIsSAdmin(String isSAdmin)
        {
            this.isSAdmin = isSAdmin;
        }

        public String getIsSAdmin()
        {
            return this.isSAdmin;
        }
        
        public void setTrackId(String trackId)
        {
            this.trackId = trackId;
        }

        public String getTrackId()
        {
            return this.trackId;
        }
        
        public void setSceId(String sceId)
        {
            this.sceId = sceId;
        }

        public String getSceId()
        {
            return this.sceId;
        }
} 
