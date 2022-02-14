package com.pfizer.sce.beans;
import java.util.List;
public class BU {
	 private String mappedBU;
		private String salesOrg;
		private List buList=null;
		
		
		
	    public String getMappedBU() {
			return mappedBU;
		}
		public void setMappedBU(String mappedBU) {
			this.mappedBU = mappedBU;
		}
		public String getSalesOrg() {
			return salesOrg;
		}
		public void setSalesOrg(String salesOrg) {
			this.salesOrg = salesOrg;
		}
		
		public List<BU> getBuList() {
			return buList;
		}
		public void setBuList(List<BU> buList1) {
			this.buList = buList1;
		}

}
