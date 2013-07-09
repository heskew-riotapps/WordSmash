package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.riotapps.word.billing.Inventory;
import com.riotapps.word.data.StoreData;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Logger;

public class StoreService {
	private static final String TAG = StoreService.class.getSimpleName();
	
	public static Purchase getPurchaseBySku(String sku){
		return StoreData.getPurchaseBySku(sku);
	}
	
	public static List<StoreItem> getStoreItems(){
		
		return StoreData.getStoreItems();
	}

	public static List<String> getAllSkus(){
		List<String> skus = new ArrayList<String>();
		skus.add(Constants.SKU_GOOGLE_PLAY_HOPPER_PEEK);
		skus.add(Constants.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL);
		skus.add(Constants.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE);
		skus.add(Constants.SKU_GOOGLE_PLAY_WORD_DEFINITIONS);
		skus.add(Constants.SKU_GOOGLE_PLAY_WORD_HINTS);
		
		return skus;
	}
	
	public static boolean isHideInterstitialAdPurchased(){
	 	//		return false;
		Purchase purchase = StoreData.getPurchaseBySku(Constants.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL);
		return purchase.isPurchased() || isPremiumUpgradePurchased();		
	}

	public static boolean isPremiumUpgradePurchased(){
	//	return false;	
		Purchase purchase = StoreData.getPurchaseBySku(Constants.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE);
		return purchase.isPurchased();
	}

	
	
	public static boolean isHopperPeekPurchased(){
		
		//return true;
		
 		Purchase purchase = StoreData.getPurchaseBySku(Constants.SKU_GOOGLE_PLAY_HOPPER_PEEK);
 		return purchase.isPurchased() || isPremiumUpgradePurchased();
	}
	
	public static boolean isWordDefinitionLookupPurchased(){
		
//		return false;
		Purchase purchase = StoreData.getPurchaseBySku(Constants.SKU_GOOGLE_PLAY_WORD_DEFINITIONS);
		return purchase.isPurchased() || isPremiumUpgradePurchased();
	}
	
	public static boolean isWordHintsPurchased(){
		
//		return false;
		Purchase purchase = StoreData.getPurchaseBySku(Constants.SKU_GOOGLE_PLAY_WORD_HINTS);
		return purchase.isPurchased() || isPremiumUpgradePurchased();
	}
	
	public static boolean isHideBannerAdsPurchased(){
		return isPremiumUpgradePurchased();	
	}
	
	public static String getIABPublicKey(){
		return Constants.IAB_1 + Constants.IAB_2 + Constants.IAB_3 + Constants.IAB_4 +
			   Constants.IAB_5 + Constants.IAB_6 + Constants.IAB_7 + Constants.IAB_8 +
			   Constants.IAB_9 + Constants.IAB_10 + Constants.IAB_11 + Constants.IAB_12 + Constants.IAB_13;
	}

	public static void savePurchase(String sku, String token){
		//check for existing purchase for this sku
		
		 Logger.d(TAG, "savePurchase sku=" + sku);
		
		Purchase purchase = new Purchase(sku, new Date());
		purchase.setPurchaseToken(token);
		
		StoreData.savePurchase(purchase);
		
	}
	
	public static void clearPurchase(String sku){
		
		StoreData.removePurchase(sku);
	}
	
	public static void syncPurchases(Inventory inventory){
		
		 if (inventory.hasPurchase(Constants.SKU_GOOGLE_PLAY_HOPPER_PEEK)){
	    	com.riotapps.word.billing.Purchase skuPeek = inventory.getPurchase(Constants.SKU_GOOGLE_PLAY_HOPPER_PEEK);
	    	savePurchase(skuPeek.getSku(), skuPeek.getToken()); 
	     }
		 else{
			 clearPurchase(Constants.SKU_GOOGLE_PLAY_HOPPER_PEEK);	 
		 }
		 
	     if (inventory.hasPurchase(Constants.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE)){
	        	com.riotapps.word.billing.Purchase skuPremium = inventory.getPurchase(Constants.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE);
	        	savePurchase(skuPremium.getSku(), skuPremium.getToken()); 
	     }  
	     else{
			 clearPurchase(Constants.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE);	 
		 }
	     
	     if (inventory.hasPurchase(Constants.SKU_GOOGLE_PLAY_WORD_DEFINITIONS)){
	        	com.riotapps.word.billing.Purchase skuDefs = inventory.getPurchase(Constants.SKU_GOOGLE_PLAY_WORD_DEFINITIONS);
	        	savePurchase(skuDefs.getSku(), skuDefs.getToken()); 
	     }  
	     else{
			 clearPurchase(Constants.SKU_GOOGLE_PLAY_WORD_DEFINITIONS);	 
		 }
	     
	     if (inventory.hasPurchase(Constants.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL)){
	        	com.riotapps.word.billing.Purchase skuInterstitial = inventory.getPurchase(Constants.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL);
	        	savePurchase(skuInterstitial.getSku(), skuInterstitial.getToken()); 
	     }  
	     else{
			 clearPurchase(Constants.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL);	 
		 }
	     
	     if (inventory.hasPurchase(Constants.SKU_GOOGLE_PLAY_WORD_HINTS)){
	        	com.riotapps.word.billing.Purchase skuHints = inventory.getPurchase(Constants.SKU_GOOGLE_PLAY_WORD_HINTS);
	        	savePurchase(skuHints.getSku(), skuHints.getToken()); 
	     }  
	     else{
			 clearPurchase(Constants.SKU_GOOGLE_PLAY_WORD_HINTS);	 
		 }
	} 
	
}
