package com.pfizer.sce.beans; 
import java.sql.Blob;
import java.util.Date;

public class UploadBlankForm 
{ 
       
   
        private byte[] uploadedBlankFile;

       //  2 DEC BY RUPINDER---private int templateVersionId;
        private Integer templateVersionId;
        private int uploadedBlankFileId;
        private Blob blobData ;
        private Date uploadedDate;
        private String uploadedBy;

        public void setUploadedBlankFileId(int uploadedBlankFileId)
        {
            this.uploadedBlankFileId = uploadedBlankFileId;
        }

        public int getUploadedBlankFileId()
        {
            return this.uploadedBlankFileId;
        }

     /* 2 DEC BY RUPINDER  public void setTemplateVersionId(int templateVersionId)
        {
            this.templateVersionId = templateVersionId;
        }

        public int getTemplateVersionId()
        {
            return this.templateVersionId;
        }*/
      public void setTemplateVersionId(Integer templateVersionId)
        {
            this.templateVersionId = templateVersionId;
        }

        public Integer getTemplateVersionId()
        {
            return this.templateVersionId;
        }

        public void setUploadedBlankFile(byte[] uploadedBlankFile)
        {
            this.uploadedBlankFile = uploadedBlankFile;
        }

        public byte[] getUploadedBlankFile()
        {
            return this.uploadedBlankFile;
        }
        public void setUploadedDate(Date date)
           {
            this.uploadedDate=date;
            }
             public Date getUploadedDate(){
            return this.uploadedDate;
            }
            public void setUploadedBy(String uploadedBy)
            {
                this.uploadedBy=uploadedBy;
            }
            
            public String getUploadedBy()
            {
                return this.uploadedBy;
            }
        public void setBlobData(Blob blobData){
            
        
        this.blobData=blobData;
        }
        public Blob getBlobData(){
        
        return this.blobData;
        }
    
} 
