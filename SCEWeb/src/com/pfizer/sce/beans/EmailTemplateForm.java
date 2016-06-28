package com.pfizer.sce.beans; 

import java.util.List;
import java.util.ArrayList;

public class EmailTemplateForm 
{
    
    private Integer emailTemplateId;
    private String emailTemplateVersion;
    private String evaluationTemplateName;
    private Integer evaluationTemplateId;
    private Integer evaluationTemplateVersionId;
    private String scoringSystemIdentifier;
    private String overallScore;
    private String emailCc;
    private String emailBCc;
    private String emailSubject;
    private String emailBody;
    private String publishFlag;
    
    
    
    public void setEmailTemplateId(Integer emailTemplateId)
        {
            this.emailTemplateId = emailTemplateId;
        }

        public Integer getEmailTemplateId()
        {
            return this.emailTemplateId;
        }

        public void setEmailTemplateVersion(String emailTemplateVersion)
        {
            this.emailTemplateVersion = emailTemplateVersion;
        }

        public String getEmailTemplateVersion()
        {
            return this.emailTemplateVersion;
        }

        public void setEvaluationTemplateName(String evaluationTemplateName)
        {
            this.evaluationTemplateName = evaluationTemplateName;
        }

        public String getEvaluationTemplateName()
        {
            return this.evaluationTemplateName;
        }

        public void setEvaluationTemplateId(Integer evaluationTemplateId)
        {
            this.evaluationTemplateId = evaluationTemplateId;
        }

        public Integer getEvaluationTemplateId()
        {
            return this.evaluationTemplateId;
        }

        public void setEvaluationTemplateVersionId(Integer evaluationTemplateVersionId)
        {
            this.evaluationTemplateVersionId = evaluationTemplateVersionId;
        }

        public Integer getEvaluationTemplateVersionId()
        {
            return this.evaluationTemplateVersionId;
        }

        public void setEmailCc(String emailCc)
        {
            this.emailCc = emailCc;
        }

        public String getEmailCc()
        {
            return this.emailCc;
        }

        public void setEmailBCc(String emailBCc)
        {
            this.emailBCc = emailBCc;
        }

        public String getEmailBCc()
        {
            
            return this.emailBCc;
        }

        public void setEmailSubject(String emailSubject)
        {
            this.emailSubject = emailSubject;
        }

        public String getEmailSubject()
        {
            return this.emailSubject;
        }

        public void setEmailBody(String emailBody)
        {
            this.emailBody = emailBody;
        }

        public String getEmailBody()
        {
            return this.emailBody;
        }

        public void setScoringSystemIdentifier(String scoringSystemIdentifier)
        {
            this.scoringSystemIdentifier = scoringSystemIdentifier;
        }

        public String getScoringSystemIdentifier()
        {
            return this.scoringSystemIdentifier;
        }

        public void setOverallScore(String overallScore)
        {
            this.overallScore = overallScore;
        }

        public String getOverallScore()
        {
            return this.overallScore;
        }
        
         public void setPublishFlag(String publishFlag)
        {
            this.publishFlag = publishFlag;
        }

        public String getPublishFlag()
        {
            return this.publishFlag;
        }
} 




