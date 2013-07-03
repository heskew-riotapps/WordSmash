package com.riotapps.word.hooks;

import java.util.List;

import com.riotapps.word.data.StoreData;
import com.riotapps.word.utils.Constants;

public class StoreService {
	
	public static Purchase getPurchaseBySku(String sku){
		return StoreData.getPurchaseBySku(sku);
	}
	
	public static List<StoreItem> getStoreItems(){
		
		return StoreData.getStoreItems();
	}

	public static boolean isHideInterstitialAdPurchased(){
	 	//		return false;
		Purchase purchase = StoreData.getPurchaseBySku(Constants.sku_google_play_hide_interstitial);
		return purchase.isPurchased() || isPremiumUpgradePurchased();		
	}

	public static boolean isPremiumUpgradePurchased(){
	//	return false;	
		Purchase purchase = StoreData.getPurchaseBySku(Constants.sku_google_play_premium_upgrade);
		return purchase.isPurchased();
	}

	
	
	public static boolean isHopperPeekPurchased(){
		
		return true;
		
//		Purchase purchase = StoreData.getPurchaseBySku(Constants.sku_google_play_hopper_peek);
//		return purchase.isPurchased() || isPremiumUpgradePurchased();
	}
	
	public static boolean isWordDefinitionLookupPurchased(){
		
//		return false;
		Purchase purchase = StoreData.getPurchaseBySku(Constants.sku_google_play_word_definition);
		return purchase.isPurchased() || isPremiumUpgradePurchased();
	}
	
	public static boolean isHideWordLookupAdPurchased(){
		//loop through purchased bundle from google play
		
		return isHideBannerAdsPurchased();
	}
	
	public static boolean isHideBannerAdsPurchased(){
		return isPremiumUpgradePurchased();	
	}

}
