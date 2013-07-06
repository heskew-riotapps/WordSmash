package com.riotapps.word.hooks;

import java.util.Date;

public class Purchase {
//	private String storeItemId;
	private Date purchaseDate = null;
	private String purchaseToken;
	private String sku = ""; //might not be needed, will be stored by sku
 

	public Purchase (String sku){
		this.sku = sku;
	}
	public Purchase (String sku, Date purchaseDate){
		this.sku = sku;
		this.purchaseDate = purchaseDate;
	}
	
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public boolean isPurchased() {
		return this.purchaseDate != null;
	}
	public String getPurchaseToken() {
		return purchaseToken;
	}
	public void setPurchaseToken(String purchaseToken) {
		this.purchaseToken = purchaseToken;
	}

}
