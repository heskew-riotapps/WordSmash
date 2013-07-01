package com.riotapps.word.hooks;

import java.util.Date;

public class Purchase {
	private String storeItemId;
	private Date purchaseDate = null;
	private String purchaseToken;
 

	public Purchase (String storeItemId){
		this.storeItemId = storeItemId;
	}
	public Purchase (String storeItemId, Date purchaseDate){
		this.storeItemId = storeItemId;
		this.purchaseDate = purchaseDate;
	}
	
	public String getStoreItemId() {
		return storeItemId;
	}

	public void setStoreItemId(String storeItemId) {
		this.storeItemId = storeItemId;
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
