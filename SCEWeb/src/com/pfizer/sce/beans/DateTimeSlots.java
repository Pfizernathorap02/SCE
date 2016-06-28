package com.pfizer.sce.beans;

import java.sql.Date;



public class DateTimeSlots 
{
	Integer id;
	long mapId;
	java.util.Date dateSel;
	String slots;
	Integer totalhrs;
	String email;
	String event;
	String product;
	
	
	public DateTimeSlots(Integer id, long mapId, Date dateSel, String slots,Integer totalhrs,String email,String event,String product) 
	{
		this.id = id;
		this.mapId = mapId;
		this.dateSel = dateSel;
		this.slots = slots;
		this.totalhrs = totalhrs;
		this.email = email;
		this.event = event;
		this.product = product;
	}
	
	public DateTimeSlots() {
		
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public long getMapId() {
		return mapId;
	}
	public void setMapId(long mapId) {
		this.mapId = mapId;
	}
	public java.util.Date getDateSel() {
		return dateSel;
	}
	public void setDateSel(java.util.Date dateSel) {
		this.dateSel = dateSel;
	}
	public Integer getTotalhrs() {
		return totalhrs;
	}
	public void setTotalhrs(Integer totalhrs) {
		this.totalhrs = totalhrs;
	}
	public String getSlots() {
		return slots;
	}
	public void setSlots(String slots) {
		this.slots = slots;
	}
	

	
}
