package com.pfizer.sce.beans; 
import java.util.Date;

public class LegalConsentTemplate 
{ 
     
    private int lcId;
        private Date modifiedDate;

        private String modifiedBy;

        private int version;

        private String templateName;

        private String content;
        private String publishFlag;
        
        private String publishedBy;
        
        private Date publishedDate;

       public void setLcId(int lcId){
        this.lcId=lcId;
       }
       public int getLcId(){
       return lcId;
       }
        public void setContent(String content)
        {
            this.content = content;
        }

        public String getContent()
        {
            return this.content;
        }

        public void setTemplateName(String templateName)
        {
            this.templateName = templateName;
        }

        public String getTemplateName()
        {
            return this.templateName;
        }

        public void setVersion(int version)
        {
            this.version = version;
        }

        public int getVersion()
        {
            return this.version;
        }

        public void setModifiedBy(String modifiedBy)
        {
            this.modifiedBy = modifiedBy;
        }

        public String getModifiedBy()
        {
            return this.modifiedBy;
        }

        public void setModifiedDate(Date modifiedDate)
        {
            this.modifiedDate = modifiedDate;
        }

        public Date getModifiedDate()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null. This type doesn't have
            // a default constructor, so Workshop cannot initialize it for you.

            // TODO: Initialize modifiedDate if it is null.
            //if(this.modifiedDate == null)
            //{
            //    this.modifiedDate = new Date(?);
            //}

            return this.modifiedDate;
        }
        public void setPublishFlag(String publishFlag){
            
            this.publishFlag=publishFlag;
        }
         public String getPublishFlag(){
            
            return publishFlag;
        }
        public void setPublishedBy(String publishedBy)
        {
        this.publishedBy=publishedBy;
        }
    public String getPublishedBy(){
        return publishedBy;
}
public void setPublishedDate(Date publishedDate){
    this.publishedDate=publishedDate;
       
}
public Date getPublishedDate(){
return publishedDate;
}

} 
