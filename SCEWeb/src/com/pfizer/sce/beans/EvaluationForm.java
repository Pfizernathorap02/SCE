package com.pfizer.sce.beans; 


import java.sql.Blob;
import java.util.Date;


public class EvaluationForm {
 private Date evaluationDate;
 private Date uploadDate;
 private String score;
 private String product;
 private String productCode;
 private String status;
 private int versionNumber;
 private int emplid;
 private Integer eventId;
 private Integer templateVersionId;
 private String repEmplid;
 private String uploadTime;
 private String evaluationTitle;
 private int formversion;
 private byte[] content;
 private Blob blobdata;
 private String uploadBy;
 public Date getEvaluationDate() {
	return evaluationDate;
 }
 public void setEvaluationDate(Date evaluationDate) {
	this.evaluationDate = evaluationDate;
 }
 public Date getUploadDate() {
	return uploadDate;
 }
 public void setUploadDate(Date uploadDate) {
	this.uploadDate = uploadDate;
 }
public String getScore() {
	return score;
}
public void setScore(String score) {
	this.score = score;
}
public String getProduct() {
	return product;
}
public void setProduct(String product) {
	this.product = product;
}
public String getProductCode() {
	return productCode;
}
public void setProductCode(String productCode) {
	this.productCode = productCode;
}
public String getStatus() {
		return status;
	}
public void setStatus(String status) {
		this.status = status;
	}

public String getRepEmplid() {
	return repEmplid;
}
public void setRepEmplid(String repEmplid) {
	this.repEmplid = repEmplid;
}

public int getVersionNumber() {
	return versionNumber;
}
public void setVersionNumber(int versionNumber) {
	this.versionNumber = versionNumber;
}
public int getEmplid() {
	return emplid;
}
public void setEmplid(int emplid) {
	this.emplid = emplid;
}

public Integer getEventId() {
	return eventId;
}
public void setEventId(Integer eventId) {
	this.eventId = eventId;
}

public Integer getTemplateVersionId() {
	return templateVersionId;
}
public void setTemplateVersionId(Integer templateVersionId) {
	this.templateVersionId = templateVersionId;
}

public String getUploadTime() {
	return uploadTime;
}
public void setUploadTime(String uploadTime) {
	this.uploadTime = uploadTime;
}
public String getEvaluationTitle() {
	return evaluationTitle;
}
public void setEvaluationTitle(String evaluationTitle) {
	this.evaluationTitle = evaluationTitle;
}
public int getFormversion() {
	return formversion;
}
public void setFormversion(int formversion) {
	this.formversion = formversion;
}
public byte[] getContent() {
	return content;
}
public void setContent(byte[] content) {
	this.content = content;
}
public String getUploadBy() {
	return uploadBy;
}
public void setUploadBy(String uploadBy) {
	this.uploadBy = uploadBy;
}
public Blob getBlobdata() {
		return blobdata;
	}
public void setBlobdata(Blob blobdata) {
		this.blobdata = blobdata;
	}
 
} 
