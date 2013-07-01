package com.riotapps.word.hooks;

import java.util.Date;

public class OpponentGroup {
	
	private int id;
	private String name;
	private String constant;
	private boolean autoActivated;
	private String storeItemId = "";
	
 
	private Purchase purchase = null;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getConstant() {
		return constant;
	}
	public void setConstant(String constant) {
		this.constant = constant;
	}
	public boolean isAutoActivated() {
		return autoActivated;
	}
	public void setAutoActivated(boolean autoActivated) {
		this.autoActivated = autoActivated;
	}
	
 
	public boolean isPurchased() {
		if (this.purchase == null){
			this.purchase = StoreService.getPurchaseByStoreItemId(this.storeItemId);
		}
		return this.purchase.isPurchased();
	}
 
	public Date getPurchaseDate() {
		if (this.purchase == null){
			this.purchase = StoreService.getPurchaseByStoreItemId(this.storeItemId);
		}
		return this.purchase.getPurchaseDate();
	}
 
	public String getStoreItemId() {
		return storeItemId;
	}
	public void setStoreItemId(String storeItemId) {
		this.storeItemId = storeItemId;
	}
	
	
	/*
	public Date getActivatedDate() {
		return activatedDate;
	}
	public void setActivatedDate(Date activatedDate) {
		this.activatedDate = activatedDate;
	}
*/

}
