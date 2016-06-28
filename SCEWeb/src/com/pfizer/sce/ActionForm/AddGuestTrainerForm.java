package com.pfizer.sce.ActionForm;

import java.io.File;

import org.apache.struts.upload.FormFile;

public class AddGuestTrainerForm 
{
    private File uploadGTList;
    private String repFname;
    private String repLname;
    private String repEmail;
    private String repLocation;
    private String repManager;
    private String repRole;
    private String repManagerRole;
    private String repProduct;
    private String message;
    private String[] repProducts;
    private String repManagerEmail;
    private String repNTID;
    
    public String getRepNTID(){
        return this.repNTID;
    }
    public void setRepNTID(String repNTID){
        this.repNTID=repNTID;
    }
    
     public File getUploadGTList() {
return uploadGTList;
}
public void setUploadGTList(File uploadGTList) {
	this.uploadGTList = uploadGTList;
}
    
    
public String getRepFname() {
return repFname;
}
public void setRepFname(String repFname) {
	this.repFname = repFname;
}

public String getRepLname() {
return repLname;
}

public void setRepLname(String repLname) {
	this.repLname = repLname;
}

public String getRepEmail() {
	return repEmail;
}
public void setRepEmail(String repEmail) {
	this.repEmail = repEmail;
}
public String getRepLocation() {
	return repLocation;
}
public void setRepLocation(String repLocation) {
	this.repLocation = repLocation;
}
public String getRepManager() {
	return repManager;
}
public void setRepManager(String repManager) {
	this.repManager = repManager;
}
public String getRepRole() {
	return repRole;
}
public void setRepRole(String repRole) {
	this.repRole = repRole;
}
public String getRepManagerRole() {
	return repManagerRole;
}
public void setRepManagerRole(String repManagerRole) {
	this.repManagerRole = repManagerRole;
}
public String getRepProduct() {
	return repProduct;
}
public void setRepProduct(String repProduct) {
	this.repProduct = repProduct;
}

public String[] getRepProducts() {
	return repProducts;
}
public void setRepProducts(String[] repProducts) {
	this.repProducts = repProducts;
}

public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
	public String getRepManagerEmail() {
	return repManagerEmail;
}
public void setRepManagerEmail(String repManagerEmail) {
	this.repManagerEmail = repManagerEmail;
}


}
