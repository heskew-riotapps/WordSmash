package com.riotapps.word.hooks;

import java.util.Currency;
import java.util.Date;

public class StoreItem {
	private String id;
	private String name;
	private String constant;
	private Currency price;
	private String sku;
	
	private Purchase purchase;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	
	public boolean isPurchased() {
		if (this.purchase == null){
			this.purchase = StoreService.getPurchaseByStoreItemId(this.id);
		}
		return this.purchase.isPurchased();
	}
 
	public Date getPurchaseDate() {
		if (this.purchase == null){
			this.purchase = StoreService.getPurchaseByStoreItemId(this.id);
		}
		return this.purchase.getPurchaseDate();
	}
	public Currency getPrice() {
		return price;
	}
	public void setPrice(Currency price) {
		this.price = price;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}


}
