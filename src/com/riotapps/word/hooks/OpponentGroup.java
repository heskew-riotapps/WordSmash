package com.riotapps.word.hooks;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class OpponentGroup {
	
	@SerializedName("id")
	private int id;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("constant")
	private String constant;
	
	@SerializedName("autoActivated")
	private boolean autoActivated;
	
	@SerializedName("sku")
	private String sku = "";
	
 
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
			this.purchase = StoreService.getPurchaseBySku(this.sku);
		}
		return this.purchase.isPurchased();
	}
 
	public Date getPurchaseDate() {
		if (this.purchase == null){
			this.purchase = StoreService.getPurchaseBySku(this.sku);
		}
		return this.purchase.getPurchaseDate();
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
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
